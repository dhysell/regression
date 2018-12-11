package regression.r2.noclock.contactmanager.address;


import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.CountyIdaho;

import repository.ab.contact.ContactDetailsAddressesAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.contact.ContactDetailsHistoryAB;
import repository.ab.pagelinks.PageLinks;
import repository.ab.search.AdvancedSearchAB;
import repository.driverConfiguration.Config;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.helpers.GuidewireHelpers;

import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AbUserHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;

/**
 * @Author sbroderick
 * @Requirement Acceptance criteria:
 * In Edit mode:
 * There are fields for Latitude and Longitude that will display negative or positive values based on location
 * Latitude is before Longitude
 * These fields show below the Valid Until field of address
 * Specific users (admin data manager/Steve) can enter in Lat/Long
 * County offices with physical addresses will have the matching Lat/Long for the office
 * County offices with only PO Box will need their physical address in order to find and enter lat/long
 * This will be via Google Maps or Geocoders
 * Once Lat/Long is entered, the spatial point is created and shown below Lat/Long fields
 * Once user clicks Update, only Spatial Point field is shown (Lat/Long aren't shown in View mode)
 * Lat/Long are unavailable once initially entered
 * A history item is stored for the Spatial Point:
 * Adding
 * Removing
 * Changing
 * @RequirementsLink <a href="http://projects.idfbins.com/contactcenter/Documents/Story%20Cards/CM8%20-%20ContactManager%20-%20Create%20Contact%20-%20Create%20new%20Place.xlsx">Requirements</a>
 * @Description Spatial Mapping for latitude and Longitude are required for County Offices.
 * @DATE Sep 15, 2017
 */
public class CountyOfficesSpacialMapping extends BaseTest {
	private WebDriver driver;
	private String currentLat = "";
	private String currentLong = "";
	private String originalLat = "45";
	private String originalLong = "-123";
	private String changedLat = "46";
	private String changedLong = "-124";
	
	@Test
	public void testSpatialMapping() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        WebDriver driver = buildDriver(cf);
		AbUsers abUser = AbUserHelper.getRandomDeptUser("Admin");
		Agents agent = AgentsHelper.getRandomAgent();
        AdvancedSearchAB search = new AdvancedSearchAB(driver);
		search.loginAndSearchCounty(abUser, CountyIdaho.valueOfName(agent.agentCounty));
        ContactDetailsBasicsAB basics = new ContactDetailsBasicsAB(driver);
		basics.clickContactDetailsBasicsAddressLink();
        ContactDetailsAddressesAB addressPage = new ContactDetailsAddressesAB(driver);
		//get Address to ascertain if it has a po box.
		//ArrayList<String> addresses = addressPage.getAddresses();
//		for(String address : addresses) {
			//ensures lat and long are not visible when viewing the contact.
			if(addressPage.setLat("")) {
				Assert.fail("Check to ensure that the lat and long editboxes are not available when not in edit mode.");
			}
			if(addressPage.setLong("")) {
				Assert.fail("Check to ensure that the lat and long editboxes are not available when not in edit mode.");
			}
					
			addressPage.clickContactDetailsAdressesEditLink();
			
			AddressInfo highlightedAddress = addressPage.getHighlightedAddress();
				
			this.currentLat = addressPage.getLatitude();
			this.currentLong = addressPage.getLongitude();
			//Add, Change and Remove lat and longs for History Items.
			
				addressPage.setLat("");
				addressPage.setLong("");
				addressPage.clickContactDetailsAddressesUpdate();
				addressPage.clickContactDetailsAdressesEditLink();
				addressPage.setLat(originalLat);
				addressPage.setLong(originalLong);
				addressPage.clickContactDetailsAddressesUpdate();
				addressPage.clickContactDetailsAdressesEditLink();
				addressPage.setLat(changedLat);
				addressPage.setLong(changedLong);
				addressPage.clickContactDetailsAddressesUpdate();
				addressPage.clickContactDetailsAdressesEditLink();
								
//					No need yet...				
//					addressPage.setLat(parseSpatialPoint(spatialPoint)[0]);
//					addressPage.setLong(parseSpatialPoint(spatialPoint)[1]);
				addressPage.clickContactDetailsAddressesUpdate();
			//check History
			checkHistoryForSpatialMapping(highlightedAddress, driver);
	}
	
	public String[] parseSpatialPoint(String point) {
		String [] latAndLong = new String[2];
		String[] pointSplit = point.split(" ");
		latAndLong[0] = pointSplit[1].replace(")", ""); 
		latAndLong[1] = pointSplit[0].replace("POINT(", "");		
		return latAndLong;
	}
	
	public void checkHistoryForSpatialMapping(AddressInfo address, WebDriver driver) {
        PageLinks pageLinks = new PageLinks(driver);
		pageLinks.clickContactDetailsBasicsHistoryLink();
		
		//  Removing
        ContactDetailsHistoryAB historyPage = new ContactDetailsHistoryAB(driver);
		ArrayList<String> text = historyPage.verifyHistory("Latitude and longitude removed", "Latitude and longitude removed from address "+address.getLine1(), "Longitude", this.currentLong, " ");
		if(text.size()>0) {
			Assert.fail("A History item should be created when the Longitude and or latitude is removed." + text.get(0));
		}

        historyPage = new ContactDetailsHistoryAB(driver);
		text = historyPage.verifyHistory("Latitude and longitude removed", "Latitude and longitude removed from address "+address.getLine1(), "Latitude", this.currentLat, " ");
		if(text.size()>0) {
			Assert.fail("A History item should be created when the Longitude and or latitude is removed." + text.get(0));
		}
		
	//  Added
        historyPage = new ContactDetailsHistoryAB(driver);
		text = historyPage.verifyHistory("Latitude and longitude added", "Latitude and longitude added to address "+address.getLine1(), "Longitude", " ", originalLong);
		if(text.size()>0) {
			Assert.fail("A History item should be created when the Longitude and or latitude is added." + text.get(0));
		}

        historyPage = new ContactDetailsHistoryAB(driver);
		text = historyPage.verifyHistory("Latitude and longitude added", "Latitude and longitude added to address "+address.getLine1(), "Latitude", " ", originalLat);
		if(text.size()>0) {
			Assert.fail("A History item should be created when the Longitude and or latitude is added." + text.get(0));
		}
					
		

	//  Changed
        historyPage = new ContactDetailsHistoryAB(driver);
		text = historyPage.verifyHistory("Latitude or longitude changed", "Latitude or longitude changed on address "+address.getLine1(), "Longitude", originalLong, changedLong);
		if(text.size()>0) {
			Assert.fail("A History item should be created when the Longitude and or latitude is changed." + text.get(0));
		}

        historyPage = new ContactDetailsHistoryAB(driver);
		text = historyPage.verifyHistory("Latitude or longitude changed", "Latitude or longitude changed on address "+address.getLine1(), "Latitude", originalLat, changedLat);
		if(text.size()>0) {
			Assert.fail("A History item should be created when the Longitude and or latitude is added." + text.get(0));
		}

        pageLinks = new PageLinks(driver);
		pageLinks.clickContactDetailsBasicsAddressLink();	
	}
	
	@Test
	public void adminUserInputLatLong() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        WebDriver driver = buildDriver(cf);
        
		ArrayList<AbUsers> users = new ArrayList<AbUsers>();
		users.add(AbUserHelper.getRandomDeptUser("Admin"));
		users.add(AbUserHelper.getRandomDeptUser("Policy Services"));
		users.add(AbUserHelper.getRandomDeptUser("Federation"));
		users.add(AbUserHelper.getRandomDeptUser("Claim"));
		for(AbUsers user : users) {
			Agents agent = AgentsHelper.getRandomAgent();
            AdvancedSearchAB search = new AdvancedSearchAB(driver);
			search.loginAndSearchCounty(user, CountyIdaho.valueOfName(agent.agentCounty));
            ContactDetailsBasicsAB basics = new ContactDetailsBasicsAB(driver);
			basics.clickContactDetailsBasicsAddressLink();
            ContactDetailsAddressesAB addressPage = new ContactDetailsAddressesAB(driver);
			if(addressPage.setLat("")) {
				Assert.fail("Check to ensure that the lat and long editboxes are not available when not in edit mode.");
			}
			if(addressPage.setLong("")) {
				Assert.fail("Check to ensure that the lat and long editboxes are not available when not in edit mode.");
			}
					
			addressPage.clickContactDetailsAdressesEditLink();
			if(user.getUserDepartment().contains("Admin") && !addressPage.setLat(45+"")){
				Assert.fail("The User admin must be able to enter Lats and Longs. Please check Admin Data.");
			} else if((!user.getUserDepartment().contains("Admin")) && addressPage.setLat(45+"")) {
				Assert.fail("Only the User admin should be able to enter Lats and Longs. Please check Admin Data.");
			}
			
			if(user.getUserDepartment().contains("Admin") && !addressPage.setLong((122*(-1))+"")){
				Assert.fail("The User admin must be able to enter Lats and Longs. Please check Admin Data.");
			} else if((!user.getUserDepartment().contains("Admin")) && addressPage.setLat((122*(-1))+"")) {
				Assert.fail("Only the User admin should be able to enter Lats and Longs. Please check Admin Data.");
			}
			
			new GuidewireHelpers(driver).logout();
		}
		
	}
}

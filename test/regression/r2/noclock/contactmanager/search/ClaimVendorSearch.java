package regression.r2.noclock.contactmanager.search;


import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.ClaimVendorSearchAB;
import repository.ab.search.SimpleSearchAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;

/**
 * @Author sbroderick
 * @Requirement
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Dec 6, 2017
 */
public class ClaimVendorSearch extends BaseTest {
	private WebDriver driver;
	private AbUsers claimUser;
	
	@Test
	public void vendorSearchUsers() throws Exception {
		ArrayList<String> departments = new ArrayList<String>();
		departments.add("IS");
		departments.add("Claims");
		departments.add("UW");
		departments.add("Policy Services");
		departments.add("Sales");
		departments.add("Transition");
		departments.add("Federation");
		departments.add("Training");
		departments.add("Accounting");
		departments.add("Underwriting");
		departments.add("Admin");
		for(String dept : departments) {
			List<AbUsers> departmentUsers = AbUserHelper.getAllDeptUsers(dept);
			for(AbUsers user : departmentUsers) {
				this.claimUser = user;
				System.out.println("Testing user " + user.getUserName());
				claimsUserAbleToClaimSearch("Mountain states");
			}
		}
	}

    public ContactDetailsBasicsAB claimsUserAbleToClaimSearch(String searchCriteria) throws Exception {
    	Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        ClaimVendorSearchAB vendorSearch = new ClaimVendorSearchAB(driver);
		vendorSearch.loginAndSearchVendor(this.claimUser, searchCriteria);
        return new ContactDetailsBasicsAB(driver);
	}
	
	/**
	* @throws Exception 
	 * @Author sbroderick
	* @Requirement 
	* @RequirementsLink <a href="http:// ">Link Text</a>
	* Currently this is testing a defect DE 6741 - https://rally1.rallydev.com/#/115106100984d/detail/defect/168237401044
	* @Description When the company type vendor name differs from the contact name and the vendor is searched for by the Vendor Name in the simple search, the contact is not found.
	* @DATE Dec 6, 2017
	*/
	
	@Test
	public void vendorSimpleSearch() throws Exception {
		String tempVendorName = "Uniquley Unmatched";
		String initialSearchCriteria = "Blue";
		this.claimUser = AbUserHelper.getRandomDeptUser("Claim");
		
		int dayInYear = DateUtils.getCenterDateAsInt(driver, ApplicationOrCenter.ContactManager, "D");
		ContactSubType contactType = ContactSubType.Person;
		if(dayInYear % 2 == 1) {
			contactType = ContactSubType.Company;
		}
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        SimpleSearchAB vendorSimpleSearch = new SimpleSearchAB(driver);
        ContactDetailsBasicsAB basicsPage = vendorSimpleSearch.getSimpleSearchResultsVendor(this.claimUser, initialSearchCriteria, contactType);
		String contactName = basicsPage.getContactDetailsBasicsContactName();
		String vendorName = basicsPage.getVendorName();
		String address = basicsPage.getContactDetailsBasicsContactAddress();
		String[] parseAddress = address.split("\\n");
		String addressLineOne = parseAddress[0];
		basicsPage.clickContactDetailsBasicsEditLink();
		basicsPage.setVendorName(tempVendorName);
		basicsPage.clickContactDetailsBasicsUpdateLink();
		new GuidewireHelpers(driver).logout();
        vendorSimpleSearch = new SimpleSearchAB(driver);
		basicsPage =vendorSimpleSearch.getSimpleSearchVendor(this.claimUser, tempVendorName, contactName,addressLineOne);
		
		basicsPage.clickContactDetailsBasicsEditLink();
		basicsPage.setVendorName(vendorName);
		basicsPage.clickContactDetailsBasicsUpdateLink();
		new GuidewireHelpers(driver).logout();
	}
	
	
	
	
	
}

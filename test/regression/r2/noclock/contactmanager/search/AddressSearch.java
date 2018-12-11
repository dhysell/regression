package regression.r2.noclock.contactmanager.search;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.ab.search.AddressSearchAB;
import repository.ab.sidebar.SidebarAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.entities.Contacts;
import persistence.globaldatarepo.helpers.AbUserHelper;
import persistence.globaldatarepo.helpers.ContactsHelpers;
public class AddressSearch extends BaseTest {
	private WebDriver driver;
	private Contacts contact = null;
	private AbUsers user = null;
	private String address = "Oak";
	private String zip = "83201";
	private String city = "Pocatello";
	
	@Test
	public void testAddressSearch() throws Exception{
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		if(DateUtils.getCenterDateAsInt(cf, ApplicationOrCenter.ContactManager, "dd") % 2 == 1){
			this.contact = ContactsHelpers.getRandomContact("Vendor");
		}else{
			this.contact = ContactsHelpers.getRandomContact("hold");
		}
		this.user = AbUserHelper.getRandomDeptUser("Policy Services");
        Login lp = new Login(driver);
		lp.login(user.getUserName(), user.getUserPassword());
        TopMenuAB menu = new TopMenuAB(driver);
		menu.clickSearchTab();
        SidebarAB sidebarLinks = new SidebarAB(driver);
		sidebarLinks.clickSidebarAddressSearch();
        AddressSearchAB addressSearch = new AddressSearchAB(driver);
		checkSearchCriteria("","","");
		checkSearchCriteria(address,"","");
		checkSearchCriteria("",city,"");
		checkSearchCriteria("","",zip);
		checkSearchCriteria("",city,zip); 
		AddressInfo address = new AddressInfo(contact.getContactAddressLine1(),contact.getContactCity(), State.valueOf(contact.getContactState()), contact.getContactZip());
		addressSearch.setSearchCriteria(address);
		String testResults = addressSearch.searchResultsContainCriteria(this.contact.getContactAddressLine1(), this.contact.getContactCity(), this.contact.getContactZip());
		if(!testResults.equals("Pass")){
			Assert.fail(testResults);
		}
	}
	
	public void checkSearchCriteria(String address, String city, String zip){
        AddressSearchAB addressSearch = new AddressSearchAB(driver);
		if(addressSearch.setSearchCriteria(new AddressInfo(address, city, State.Idaho,zip))){
			Assert.fail("At least City or Postal Code with Address Line 1 must exist to perform search");
		}
		if(addressSearch.hasErrorMessage()){
			Assert.assertTrue(addressSearch.getErrorMessage().contains("At least City or Postal Code with Address Line 1 must exist to perform search"), "The error message should show that a city or a postal code with an address Line 1 should exist.");
		} /*else {
			Assert.fail("When address = " + address + ", city = " +city + ", and zip = " +  zip + " are searched, an error message should be received.");
		}*/
	}
}

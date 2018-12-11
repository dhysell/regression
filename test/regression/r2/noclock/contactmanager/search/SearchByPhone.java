package regression.r2.noclock.contactmanager.search;


import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.search.PhoneNumberSearchAB;
import repository.ab.sidebar.SidebarAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import repository.gw.generate.custom.AdvancedSearchResults;

import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;
public class SearchByPhone extends BaseTest {
    //instance Data
	private WebDriver driver;
    private AbUsers abUser;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
    }

    @Test
    public void searchPhone() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
    	this.abUser = AbUserHelper.getRandomDeptUser("Policy Services");
        AdvancedSearchAB advancedSearch = new AdvancedSearchAB(driver);
    	AdvancedSearchResults searchResults = advancedSearch.clickRandomAdvancedSearchResultWithPhone(this.abUser, "blue");
        String contactPhoneNoDashes = searchResults.getPhone().replace("-", "");
        TopMenuAB menu = new TopMenuAB(driver);
        menu.clickSearchTab();
        SidebarAB sidebarLinks = new SidebarAB(driver);
        sidebarLinks.clickSidebarPhoneNumberSearch();
        PhoneNumberSearchAB phoneSearchPage = new PhoneNumberSearchAB(driver);
        phoneSearchPage.searchNumber(contactPhoneNoDashes, searchResults.getLastNameOrCompanyName());
        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
        String pageTitle = contactPage.getContactPageTitle();
        if (!pageTitle.contains(searchResults.getLastNameOrCompanyName())) {
            throw new Exception("The page title does not match the name of the contact that was to be created.");
        }

    }
}

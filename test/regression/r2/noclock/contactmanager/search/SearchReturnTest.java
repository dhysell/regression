package regression.r2.noclock.contactmanager.search;


import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.ab.activity.NewActivity;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.ActivitySearchAB;
import repository.ab.search.AddressSearchAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.search.ClaimVendorSearchAB;
import repository.ab.search.PhoneNumberSearchAB;
import repository.ab.search.RecentlyViewedAB;
import repository.ab.search.SearchAgentSearchAB;
import repository.ab.search.SimpleSearchAB;
import repository.ab.sidebar.SidebarAB;
import repository.ab.topmenu.TopMenuSearchAB;
import repository.driverConfiguration.Config;
import repository.gw.generate.custom.AdvancedSearchResults;

import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;

/**
 * @Author sbroderick
 * @Requirement After working in a contact, clicking the search button should take you back to the search the user completed to get into the contact.
 * @RequirementsLink <a href="http://projects.idfbins.com/contactcenter/Documents/Story%20Cards/CM8%20-%20ContactManager%20-%20Search%20Contacts%20-%20Search%20for%20Contacts.xlsx">Search Contacts Story Card</a>
 * @Description: Steps to get there:
 * 1.  Search for a contact (taking note of which search you use - Advanced, Simple, Activity, Claim Vendor, etc.)
 * 2.  Go into that contact
 * 3.  Click Search tab again
 * @DATE Sep 27, 2017
 */
public class SearchReturnTest extends BaseTest {
	private WebDriver driver;
    private ArrayList<AbUsers> abUsers = new ArrayList<>();
    private AdvancedSearchResults vendorResults = null;

    public void setUsers() throws Exception {
        if (this.abUsers.isEmpty()) {
            this.abUsers.add(AbUserHelper.getRandomDeptUser("Claims"));
            this.abUsers.add(AbUserHelper.getRandomDeptUser("Policy Services"));
            this.abUsers.add(AbUserHelper.getRandomDeptUser("Admin"));
        }
    }

    @Test
    public void advancedSearch() throws Exception {
        setUsers();
        for (AbUsers user : abUsers) {
        	Config cf = new Config(ApplicationOrCenter.ContactManager);
            driver = buildDriver(cf);
            AdvancedSearchAB searchAB = new AdvancedSearchAB(driver);
            searchAB.loginAndGetToSearch(user);
            SidebarAB sidebar = new SidebarAB(driver);
            AdvancedSearchAB advancedSearch = new AdvancedSearchAB(driver);
            this.vendorResults = advancedSearch.getRandomVendor("abc");
            ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
            basicsPage.clickContactDetailsBasicsEditLink();
            basicsPage.clickContactDetailsBasicsCancel();
            TopMenuSearchAB menu = new TopMenuSearchAB(driver);
            menu.clickSearch();
            AdvancedSearchAB advancedSearchReturn = new AdvancedSearchAB(driver);
            if (!advancedSearchReturn.getAdvancedSearchPageTitle().equals("Search")) {
                Assert.fail("Returning the the Advanced Search Screen should yield Search");
            }
        }
    }

    @Test(dependsOnMethods = {"advancedSearch"})
    public void simpleSearch() throws Exception {
        setUsers();
        for (AbUsers user : abUsers) {
        	Config cf = new Config(ApplicationOrCenter.ContactManager);
            driver = buildDriver(cf);
            AdvancedSearchAB searchAB = new AdvancedSearchAB(driver);
            searchAB.loginAndGetToSearch(user);
            SidebarAB sidebar = new SidebarAB(driver);
            sidebar.clickSidebarSimpleSearchLink();
            SimpleSearchAB simpleSearch = new SimpleSearchAB(driver);
            ContactDetailsBasicsAB basicsPage = simpleSearch.simpleSearchCompany(this.vendorResults.getLastNameOrCompanyName(), this.vendorResults.getAddress().getLine1());
            basicsPage.clickContactDetailsBasicsEditLink();
            basicsPage.clickContactDetailsBasicsCancel();
            TopMenuSearchAB menu = new TopMenuSearchAB(driver);
            menu.clickSearch();
            SimpleSearchAB simpleSearchReturn = new SimpleSearchAB(driver);
            if (!simpleSearchReturn.isSimpleSearch()) {
                Assert.fail("Clicking search should return the user to the Simple Search Screen.");
            }
        }
    }

    @Test
    public void agentSearch() throws Exception {
        setUsers();
        for (AbUsers user : abUsers) {
            if (!user.getUserDepartment().contains("Claim")) {
            	Config cf = new Config(ApplicationOrCenter.ContactManager);
                driver = buildDriver(cf);
                AdvancedSearchAB searchAB = new AdvancedSearchAB(driver);
                searchAB.loginAndGetToSearch(user);
                SidebarAB sidebar = new SidebarAB(driver);
                sidebar.clickSidebarAgentSearchLink();
                SearchAgentSearchAB agentSearch = new SearchAgentSearchAB(driver);
                ContactDetailsBasicsAB basicsPage = agentSearch.searchAgentContact(AgentsHelper.getRandomAgent());
                basicsPage.clickContactDetailsBasicsEditLink();
                basicsPage.clickContactDetailsBasicsCancel();
                TopMenuSearchAB menu = new TopMenuSearchAB(driver);
                menu.clickSearch();
                SearchAgentSearchAB agentSearchReturn = new SearchAgentSearchAB(driver);
                if (!agentSearchReturn.isAgentSearch()) {
                    Assert.fail("Clicking search should return the user to the Agent Search Screen.");
                }
            }
        }
    }

    @Test
    public void activitySearch() throws Exception {
        setUsers();
        for (AbUsers user : abUsers) {
            System.out.println("User is " + user.getUserName());
            if (!user.getUserDepartment().startsWith("Admin")) {
            	createActivity(user);
            	Config cf = new Config(ApplicationOrCenter.ContactManager);
                driver = buildDriver(cf);
                AdvancedSearchAB searchAB = new AdvancedSearchAB(driver);
                searchAB.loginAndGetToSearch(user);
                SidebarAB sidebar = new SidebarAB(driver);
                ActivitySearchAB activitySearch = sidebar.clickSidebarActivitySearchLink();
                ContactDetailsBasicsAB basicsPage = activitySearch.clickFirstActivityForUser(user.getUserLastName());
                basicsPage.clickContactDetailsBasicsEditLink();
                basicsPage.clickContactDetailsBasicsCancel();
                TopMenuSearchAB menu = new TopMenuSearchAB(driver);
                menu.clickSearch();
                ActivitySearchAB activitySearchReturn = new ActivitySearchAB(driver);
                if (!activitySearchReturn.isActivitySearch()) {
                    Assert.fail("Clicking search should return the user to the Activity Search Screen.");
                }
            }
        }
    }

    @Test
    public void claimVendorSearch() throws Exception {
        setUsers();
        for (AbUsers user : abUsers) {
        	Config cf = new Config(ApplicationOrCenter.ContactManager);
            driver = buildDriver(cf);
            AdvancedSearchAB searchAB = new AdvancedSearchAB(driver);
            searchAB.loginAndGetToSearch(user);
            SidebarAB sidebar = new SidebarAB(driver);
            ClaimVendorSearchAB vendorSearch = sidebar.clickClaimVendorSearch();
            ContactDetailsBasicsAB basicsPage = vendorSearch.searchClaimVendorSearch("Demis Pick and Pull");
            basicsPage.clickContactDetailsBasicsEditLink();
            basicsPage.clickContactDetailsBasicsCancel();
            TopMenuSearchAB menu = new TopMenuSearchAB(driver);
            menu.clickSearch();
            vendorSearch = sidebar.clickClaimVendorSearch();
            if (!vendorSearch.isVendorSearch()) {
                Assert.fail("Clicking search should return the user to the Claim Vendor Search Screen.");
            }
        }
    }

    @Test
    public void recentlyViewedSearch() throws Exception {
        setUsers();
        for (AbUsers user : abUsers) {
        	Config cf = new Config(ApplicationOrCenter.ContactManager);
            driver = buildDriver(cf);
            AdvancedSearchAB searchAB = new AdvancedSearchAB(driver);
            searchAB.loginAndGetToSearch(user);
            SidebarAB sidebar = new SidebarAB(driver);
            //who can't see the recently viewed search
            RecentlyViewedAB recentlyViewedSearch = sidebar.clickSidebarRecentlyViewedSearchLink();
            ContactDetailsBasicsAB basicsPage = recentlyViewedSearch.clickRandomSearchResult();
            basicsPage.clickContactDetailsBasicsEditLink();
            basicsPage.clickContactDetailsBasicsCancel();
            TopMenuSearchAB menu = new TopMenuSearchAB(driver);
            menu.clickSearch();
            recentlyViewedSearch = sidebar.clickSidebarRecentlyViewedSearchLink();
            if (!recentlyViewedSearch.isRecentlyViewedSearch()) {
                Assert.fail("Clicking search should return the user to the Activity Search Screen.");
            }
        }
    }

    @Test
    public void phoneNumberSearch() throws Exception {
        setUsers();
        for (AbUsers user : abUsers) {
        	Config cf = new Config(ApplicationOrCenter.ContactManager);
            driver = buildDriver(cf);
            AdvancedSearchAB searchAB = new AdvancedSearchAB(driver);
            searchAB.loginAndGetToSearch(user);
            AdvancedSearchResults searchResults = getClaimVendor("Blue");
            ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
            TopMenuSearchAB menu = new TopMenuSearchAB(driver);
            menu.clickSearch();
            SidebarAB sidebar = new SidebarAB(driver);
            sidebar.clickSidebarPhoneNumberSearch();
            PhoneNumberSearchAB phoneSearch = new PhoneNumberSearchAB(driver);
            phoneSearch.searchNumber(searchResults.getPhone(), searchResults.getLastNameOrCompanyName());
            basicsPage.clickContactDetailsBasicsEditLink();
            basicsPage.clickContactDetailsBasicsCancel();
            menu = new TopMenuSearchAB(driver);
            menu.clickSearch();
            phoneSearch = new PhoneNumberSearchAB(driver);
            if (!phoneSearch.isPhoneSearch()) {
                Assert.fail("Clicking search should return the user to the Activity Search Screen.");
            }
        }
    }

    @Test
    public void addressSearch() throws Exception {
        setUsers();
        for (AbUsers user : abUsers) {
        	Config cf = new Config(ApplicationOrCenter.ContactManager);
            driver = buildDriver(cf);
            AdvancedSearchAB searchAB = new AdvancedSearchAB(driver);
            searchAB.loginAndGetToSearch(user);
            AdvancedSearchResults searchResultsVendor = getClaimVendor("Blue");
            TopMenuSearchAB searchClicker = new TopMenuSearchAB(driver);
            searchClicker.clickSearch();
            SidebarAB sidebar = new SidebarAB(driver);
            sidebar.clickSidebarAddressSearch();
            AddressSearchAB addressSearch = new AddressSearchAB(driver);
            addressSearch.setSearchCriteria(searchResultsVendor.getAddress());
            ContactDetailsBasicsAB basicsPage = addressSearch.clickSearchResults(searchResultsVendor.getAddress().getLine1());
            basicsPage.clickContactDetailsBasicsEditLink();
            basicsPage.clickContactDetailsBasicsCancel();
            TopMenuSearchAB menu = new TopMenuSearchAB(driver);
            menu.clickSearch();
            addressSearch = new AddressSearchAB(driver);
            if (!addressSearch.isActivitySearch()) {
                Assert.fail("Clicking search should return the user to the Activity Search Screen.");
            }
        }
    }

    public AdvancedSearchResults getClaimVendor(String nameStartsWith) throws Exception {
        SidebarAB sidebar = new SidebarAB(driver);
        sidebar.clickSidebarAdvancedSearchLink();
        AdvancedSearchAB advancedSearch = new AdvancedSearchAB(driver);
        return advancedSearch.getRandomClaimPartyWithPhone(nameStartsWith);
    }
    
    public void createActivity(AbUsers userReceivesActivity) throws Exception {
    	System.out.println("About to create an activity for: " + userReceivesActivity.getUserName());
    	Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
    	searchMe.loginAndGetToSearch(AbUserHelper.getRandomDeptUser("Policy Services"));
    	searchMe.getRandomVendor("Red");
        NewActivity newActivity = new NewActivity(driver);
    	newActivity.sendActivity("Address", userReceivesActivity);
    }
    
    
}

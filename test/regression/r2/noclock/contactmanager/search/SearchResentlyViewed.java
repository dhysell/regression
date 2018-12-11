package regression.r2.noclock.contactmanager.search;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.ab.search.AdvancedSearchAB;
import repository.ab.search.RecentlyViewedAB;
import repository.ab.sidebar.SidebarAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.entities.Contacts;
import persistence.globaldatarepo.helpers.AbUserHelper;
import persistence.globaldatarepo.helpers.ContactsHelpers;

/**
 * @Author sbroderick
 * @Requirement There should be a Recently Viewed Search Page.
 * @RequirementsLink <a href=
 * "https://rally1.rallydev.com/#/10075774537d/detail/userstory/46238380630">
 * Rally Story for Recently Viewed Search</a>
 * @Description The users request that they be able to search recently viewed
 * contacts.
 * @DATE Nov 24, 2015
 */
public class SearchResentlyViewed extends BaseTest {
	private WebDriver driver;

    @Test
    public void recentlyViewedSearchTest() throws Exception {
        ArrayList<AbUsers> users = new ArrayList<>();
        users.add(AbUserHelper.getRandomDeptUser("Policy Services"));
        users.add(AbUserHelper.getRandomDeptUser("Admin"));
        users.add(AbUserHelper.getRandomDeptUser("Underwriting"));

        for (AbUsers abUser : users) {

        	Config cf = new Config(ApplicationOrCenter.ContactManager);
            driver = buildDriver(cf);

            Contacts myContact = ContactsHelpers.getRandomContact("Company");

            AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
            searchMe.loginAndSearchContact(abUser, "", myContact.getContactName(), myContact.getContactAddressLine1(), State.valueOf(myContact.getContactState()));
            new GuidewireHelpers(driver).logout();

            Login lp = new Login(driver);
            lp.login(abUser.getUserName(), abUser.getUserPassword());
            TopMenuAB abMenu = new TopMenuAB(driver);
            abMenu.clickSearchTab();
            SidebarAB abSideMenu = new SidebarAB(driver);
            abSideMenu.clickSidebarRecentlyViewedSearchLink();

            RecentlyViewedAB recentSearch = new RecentlyViewedAB(driver);
            recentSearch.setSearchForDate(90);
            recentSearch.clickSearch();
            if (!recentSearch.nameExists(myContact.getContactName())) {
                Assert.fail("The search Results must contain the newly created contact.");
            }
        }
    }

}

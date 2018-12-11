/*Steve Broderick
 * This test will test different Contacts by Role.
 * This test will also ensure you see all the addresses for a contact.
 */

package scratchpad.steve;


import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.search.SearchAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;


@SuppressWarnings("unused")
public class SearchByContactRole extends BaseTest {

    //instance Data

    private String userName = "kharrild";
    private String password = "gw";
    private AddressInfo newAddress = new AddressInfo(true);
    private String lienholder;
    private List<String> lienholderAddresses;
    private WebDriver driver;

    public void searchContactRole(String role, String searchName) throws Exception {
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        this.driver = buildDriver(cf);
        Login lp = new Login(driver);
        lp.login(this.userName, this.password);
        TopMenuAB menu = new TopMenuAB(driver);
        menu.clickSearchTab();
        AdvancedSearchAB roleSearch = new AdvancedSearchAB(driver);
        if (!role.equals("County") || role.equals("Agency")) {
            roleSearch.setCompanyName(searchName);
        }
        roleSearch.setRoles(role);
    }

    public void testSearchResults(String role, String searchName) throws Exception {
        searchContactRole(role, searchName);
        SearchAB searchMe = new SearchAB(driver);

        searchMe.clickSearch();
        AdvancedSearchAB advancedSearchPage = new AdvancedSearchAB(driver);
        String searchMessage = advancedSearchPage.getSearchMessage();
        if (!role.equals("Dealer") && searchMessage.isEmpty()) {
            searchMe = new SearchAB(driver);

            int lcv = 0;
            List<WebElement> searchResults;
            do {
                searchMe = new SearchAB(driver);
                searchResults = searchMe.searchResultsName();
                searchResults.get(lcv).click();

                ContactDetailsBasicsAB agency = new ContactDetailsBasicsAB(driver);
                String[] roles = agency.getRoles();
                boolean found = false;
                for (String rolefound : roles) {
                    if (rolefound.equals(role)) {
                        found = true;
                        break;
                    }
                }
                if (found == false) {
                    Assert.fail(driver.getCurrentUrl() + "When searching by agency, the role of Agency was not found on the contact: " + searchResults.get(lcv).getText());
                }
                TopMenuAB menu = new TopMenuAB(driver);
                menu.clickSearchTab();
                lcv++;
            }
            while (lcv < searchMe.searchResultsName().size());
        }
    }

    @Test
    public void agencySearchTest() throws Exception {
        testSearchResults("Agency", "");
    }

    @Test
    public void agentSearchTest() throws Exception {
        testSearchResults("Agent", "van");
    }

    @Test
    public void bankSearchTest() throws Exception {
        testSearchResults("Bank", "van");
    }

    @Test
    public void claimPartySearchTest() throws Exception {
        testSearchResults("Claim Party", "van");
    }

    @Test
    public void countySearchTest() throws Exception {
        testSearchResults("County", "");
    }


    @Test
    public void dealerSearchTest() throws Exception {
        testSearchResults("Dealer", "van");
    }

    @Test
    public void financeSearchTest() throws Exception {
        testSearchResults("Finance", "Smith");
    }

    @Test
    public void lienholderSearchTest() throws Exception {
        testSearchResults("Lienholder", "Lend");
    }

    @Test
    public void vendorSearchTest() throws Exception {
        testSearchResults("Vendor", "Smart");
    }
}

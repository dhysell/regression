/*Steve Broderick
 *
 * simpleSearchesMatchesAdvancedSearch test the Business Rule that Simple Search contains the Same Results as Advanced Search.
 *
 * searchesStartsWithSearchCriterion test that search Criteria matches search results or DBA's.
 *
 *
 *
 */

package scratchpad.steve;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.ab.search.AdvancedSearchAB;
import repository.ab.search.SimpleSearchAB;
import repository.ab.sidebar.SidebarAB;
import repository.ab.topmenu.TopMenuSearchAB;
import repository.driverConfiguration.Config;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;

public class SearchResults extends BaseTest {
	private WebDriver driver;
    private String userName = "kharrild";
    private String password = "gw";
    private List<String> advancedSearchResultsText = new ArrayList<String>();
    private List<String> simpleSearchResultsText = new ArrayList<String>();
    private String searchCriteria = "abc";


    public void getToAdvancedSearch() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        Login lp = new Login(driver);
        lp.login(this.userName, this.password);
        TopMenuSearchAB menu = new TopMenuSearchAB(driver);
        menu.clickSearch();
        SidebarAB sidebar = new SidebarAB(driver);
        sidebar.clickSidebarAdvancedSearchLink();
    }

    public void getToSimpleSearch() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        Login lp = new Login(driver);
        lp.login(this.userName, this.password);
        TopMenuSearchAB menu = new TopMenuSearchAB(driver);
        menu.clickSearch();
        SidebarAB sidebar = new SidebarAB(driver);
        sidebar.clickSidebarSimpleSearchLink();
    }

    public List<String> converListWebElementToListString(List<WebElement> listOfWebElements) {
        List<String> stringList = new ArrayList<String>();
        for (int i = 0; i < listOfWebElements.size(); i++) {
            stringList.add(listOfWebElements.get(i).getText());
        }
        return stringList;
    }

    @Test
    public void getSearchResults() throws Exception {
        getToAdvancedSearch();
        AdvancedSearchAB searchPage = new AdvancedSearchAB(driver);
        List<WebElement> advancedSearchResults = searchPage.advancedSearchResultsByName(searchCriteria, null, null, null);
        for (int i = 0; i < advancedSearchResults.size(); i++) {
            advancedSearchResultsText.add(advancedSearchResults.get(i).getText());
        }
        SidebarAB sideBarLinks = new SidebarAB(driver);
        sideBarLinks.clickSidebarSimpleSearchLink();
        SimpleSearchAB simpleSearchPage = new SimpleSearchAB(driver);
        List<WebElement> simpleSearchResults = simpleSearchPage.getSimpleSearchResultsName(searchCriteria);
        for (int i = 0; i < simpleSearchResults.size(); i++) {
            simpleSearchResultsText.add(simpleSearchResults.get(i).getText());
        }
    }

    @Test(dependsOnMethods = {"getSearchResults"})
    public void simpleSearchMatchesAdvancedSearch() {
        boolean match = false;
        for (int i = 0; i < advancedSearchResultsText.size(); i++) {
            System.out.println("Advanced Search List: " + advancedSearchResultsText.get(i));
            System.out.println("Simple Search List: " + simpleSearchResultsText.get(i));
            match = false;
            for (int r = 0; r < simpleSearchResultsText.size(); r++) {
                if (simpleSearchResultsText.get(r).equals(advancedSearchResultsText.get(i))) {
                    match = true;
                    break;
                }
                if (r == simpleSearchResultsText.size() - 1 && match == false) {
                    Assert.fail(driver.getCurrentUrl() + "The Advanced search page returned search results that did not match the Simple Search page.");
                }
            }
        }
    }

    @Test(dependsOnMethods = {"simpleSearchMatchesAdvancedSearch"})
    public void searchesStartsWithSearchCriterion() throws Exception {
        if (!startsWith(searchCriteria, advancedSearchResultsText)) {
            Assert.fail(driver.getCurrentUrl() + "The search criteria: " + searchCriteria + "did not match all search results.");
        }
        if (!startsWith(searchCriteria, simpleSearchResultsText)) {
            Assert.fail(driver.getCurrentUrl() + "The search criteria: " + searchCriteria + "did not match all search results.");
        }
    }

    public boolean startsWith(String criterion, List<String> searchResults) throws Exception {
        boolean match = true;
        List<WebElement> searchingForDBA;
        List<String> searchingForDBAString = new ArrayList<String>();
        for (int i = 0; i < searchResults.size(); i++) {
            if (!searchResults.get(i).toLowerCase().startsWith(criterion)) {
                String[] names;
                String[] firstMiddleName;
                searchResults.get(i).contains(",");
                names = searchResults.get(i).split("[,]");
                getToSimpleSearch();
                SimpleSearchAB searchPage = new SimpleSearchAB(driver);
                if (names.length > 1) {
                    firstMiddleName = names[1].trim().split("[ ]");
                    searchingForDBA = searchPage.getSimpleSearchResultsName(names[0] + " " + firstMiddleName[0]);
                } else {
                    searchingForDBA = searchPage.getSimpleSearchResultsName(names[0]);
                }
                for (int x = 0; x < searchingForDBA.size(); x++) {
                    searchingForDBAString.add(searchingForDBA.get(x).getText());
                    if (!searchingForDBAString.get(x).toLowerCase().startsWith(criterion)) {
                        match = false;
                        Assert.fail(driver.getCurrentUrl() + "The search criterion: " + criterion + "did not match all search results: " + searchingForDBAString.get(x) + ".");
                    } else {
                        //Do Nothing
                    }
                }
            }
        }
        return match;
    }
}
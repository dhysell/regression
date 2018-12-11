/*
 * Steve Broderick
 * 
 * simpleSearchesMatchesAdvancedSearch test the Business Rule that Simple Search contains the Same Results as Advanced Search.
 * 
 * searchesStartsWithSearchCriterion test that search Criteria matches search results or DBA's.
 * 
 * 
 * 
 */

package regression.r2.noclock.contactmanager.search;


import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.search.SearchAB;
import repository.ab.search.SimpleSearchAB;
import repository.ab.sidebar.SidebarAB;
import repository.ab.topmenu.TopMenuAB;
import repository.ab.topmenu.TopMenuSearchAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GenerateContactType;
import repository.gw.errorhandling.ErrorHandling;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.AdvancedSearchResults;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;
public class SearchResults extends BaseTest {
	private WebDriver driver;
	private AbUsers abUser;
	private List<String> advancedSearchResultsText = new ArrayList<String>();
	private List<String> simpleSearchResultsText = new ArrayList<String>();
	private String searchCriteria = "abc";
	private String ssn = "5400";

	public void getToAdvancedSearch() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        Login lp = new Login(driver);
		lp.login(this.abUser.getUserName(), this.abUser.getUserPassword());
        TopMenuSearchAB menu = new TopMenuSearchAB(driver);
		menu.clickSearch();
        SidebarAB sidebar = new SidebarAB(driver);
		sidebar.clickSidebarAdvancedSearchLink();
	}

	public void getToSimpleSearch() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        Login lp = new Login(driver);
		lp.login(this.abUser.getUserName(), this.abUser.getUserPassword());
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
		List<WebElement> advancedSearchResults = searchPage.advancedSearchResultsByName(searchCriteria, null, null,
				null);
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

	@Test(dependsOnMethods = { "getSearchResults" })
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
					Assert.fail("The Advanced search page returned search results that did not match the Simple Search page.");
				}
			}
		}
	}

	@Test(dependsOnMethods = { "simpleSearchMatchesAdvancedSearch" })
	public void searchesStartsWithSearchCriterion() throws Exception {
		if (!startsWith(searchCriteria, advancedSearchResultsText)) {
			Assert.fail("The search criteria: " + searchCriteria + "did not match all search results.");
		}
		if (!startsWith(searchCriteria, simpleSearchResultsText)) {
			Assert.fail("The search criteria: " + searchCriteria + "did not match all search results.");
		}
	}

	@Test(dependsOnMethods = { "searchesStartsWithSearchCriterion" })
	public void partialSocialSearch() throws Exception {
		AdvancedSearchAB partialSSNSearch = new AdvancedSearchAB(driver);
		List<WebElement> ssnResults = partialSSNSearch.searchResultsCriteria(this.ssn);
		if (ssnResults.size() <= 0) {
			Assert.fail("No Search Results were found when searching last four of SSN 5400.");
		}
		
		if (!partialSSNSearch.searchResultsContainCriteria("SSN / TIN", this.ssn)) {
			Assert.fail("Search Results were found that do not contain the search criterion: " + this.ssn + ".");
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
				searchingForDBAString.add(searchingForDBA.get(0).getText());
				if (!searchingForDBAString.get(0).toLowerCase().startsWith(criterion)) {
					Assert.fail("The search criterion: " + criterion + "did not match all search results: "
									+ searchingForDBAString.get(0) + ".");
				} else {
					break;
				}
			}
		}
		return match;
	}
	
	@Test
	public void minorDesignatedInSearchResults() throws Exception{
		getToAdvancedSearch();
        AdvancedSearchAB searchPage = new AdvancedSearchAB(driver);
		searchPage.setLastName("Broderick");
		searchPage.clickSearch();
		AdvancedSearchResults advancedResults = getSearchResults(true);
        if (advancedResults.getAge() > 17 || advancedResults.getAge() <= 15) {
			Assert.fail("The minor designation should not appear.");
		}
	}

	public AdvancedSearchResults getSearchResults(boolean retry) throws Exception{
        SearchAB searchResults = new SearchAB(driver);
		AdvancedSearchResults advancedResults = null;
		try{
			advancedResults = searchResults.getSearchResultWithMinorDesignation();
			if(advancedResults.getAge() >17 || advancedResults.getAge() < 0){
				Assert.fail("The minor designation should not appear.");
			}
		} catch(NoSuchElementException e){
			if(!retry){
				Assert.fail("After creating a contact that should have the minor designation, there is no contact with a minor designation.");
			}
			new GuidewireHelpers(driver).logout();
            GenerateContact myContactObj = new GenerateContact.Builder(driver)
					.withCreator(AbUserHelper.getRandomDeptUser("Policy Services"))
					.withFirstLastName("Kid", "Broderick")
					.withPrimaryAddress(new AddressInfo(true))
					.withDOB(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.ContactManager), DateAddSubtractOptions.Year, -16))
                    .withGenerateAccountNumber(false)
					.build(GenerateContactType.Person);
			
			System.out.println("accountNumber: " + myContactObj.accountNumber);
			new GuidewireHelpers(driver).logout();
			getToAdvancedSearch();
            AdvancedSearchAB searchPage = new AdvancedSearchAB(driver);
			searchPage.setLastName("Broderick");
			searchPage.clickSearch();
			getSearchResults(false);
		} 
			return advancedResults;
	}
	
	//Default Search page should be advanced search
	@Test
	public void defaultSearchPages() throws Exception{
		getToAdvancedSearch();
        TopMenuAB topMenu = new TopMenuAB(driver);
		topMenu.clickDesktopTab();
		topMenu.clickSearchTab();
		//Ensures advanced search page by clicking clicking the account number field.
        AdvancedSearchAB advancedSearch = new AdvancedSearchAB(driver);
		advancedSearch.searchByAccountNumberClickName("123456", "Baxter, Everett");
        ContactDetailsBasicsAB basics = new ContactDetailsBasicsAB(driver);
		basics.clickContactDetailsBasicsEditLink();
		String originalSSN = basics.getSsn();
		basics.setContactDetailsBasicsSSN(" ");
		basics.clickContactDetailsBasicsUpdateLink();
		String ssn = basics.getSsn();
		System.out.println("SSN = " + ssn);
		if(!ssn.equals("")){
			Assert.fail("SSN should be null.");
		}
        ErrorHandling errorHandling = new ErrorHandling(driver);
		List<WebElement> messages = errorHandling.text_ErrorHandlingErrorBannerMessages();
		for(WebElement message : messages){
			if(message.getText().contains("StringIndexOutOfBoundsException")){
				Assert.fail("StringIndexOutOfBoundsException should not exist.");
			}
		}
		basics.clickContactDetailsBasicsEditLink();
		basics.setContactDetailsBasicsSSN(originalSSN);	
		basics.clickContactDetailsBasicsUpdateLink();
	}
}

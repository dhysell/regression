package previousProgramIncrement.pi3_090518_111518.nonFeatures.Nucleus;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.CreateNew;
import repository.gw.enums.TaxReportingOption;
import repository.gw.errorhandling.ErrorHandling;
import repository.gw.generate.GenerateContact;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.search.SearchResultsReturnPC;
import repository.pc.topmenu.TopMenuPolicyPC;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AgentsHelper;

public class US16277_MessageDisplaysForNoResultsOnSSNTINSearch extends BaseTest {
	
	GenerateContact contactToSearch;
	String env = "IT";
	private Agents agent;
		
	@Test
	public void testNoResultsSSN() throws Exception {
		this.agent = AgentsHelper.getRandomAgent();
		Config cf = new Config(ApplicationOrCenter.PolicyCenter, env);
		WebDriver driver = buildDriver(cf);
        new Login(driver).login(this.agent.agentUserName, this.agent.agentPassword);
        new TopMenuPolicyPC(driver).clickNewSubmission();
        SearchAddressBookPC searchPC = new SearchAddressBookPC(driver);
        int i = 0;
        SearchResultsReturnPC searchResults = new SearchResultsReturnPC(driver);
        do {
        	searchResults = searchPC.searchAddressBook(true, StringsUtils.generateRandomNumberDigits(9), TaxReportingOption.SSN, null, null, null, null, null, null, null, CreateNew.Do_Not_Create_New);
        	
        	i++;
        }while(i<4 && searchResults.isFound());
        boolean found = false;
        ErrorHandling submissionPageErrors = new ErrorHandling(driver);
        List<WebElement> errors = submissionPageErrors.getValidationMessages();
        for(WebElement error : errors) {
        	if(error.getText().contains("No results found for SSN")) {
        		found = true;
        	}
        }
        Assert.assertTrue(found, "Searching by SSN with no results should show an error message \" No results found for SSN.");
	}
	
	@Test
	public void testNoResultsTIN() throws Exception {
		
		String tin = StringsUtils.generateRandomNumberDigits(9);
		
		this.agent = AgentsHelper.getRandomAgent();
		Config cf = new Config(ApplicationOrCenter.PolicyCenter, env);
		WebDriver driver = buildDriver(cf);
        new Login(driver).login(this.agent.agentUserName, this.agent.agentPassword);
        new TopMenuPolicyPC(driver).clickNewSubmission();
        SearchAddressBookPC searchPC = new SearchAddressBookPC(driver);
        int i = 0;
        SearchResultsReturnPC searchResults = new SearchResultsReturnPC(driver);
        do {
        	searchResults = searchPC.searchAddressBook(true, tin, TaxReportingOption.TIN, null, null, null, null, null, null, null, CreateNew.Do_Not_Create_New);
        	
        	i++;
        }while(i<4 && searchResults.isFound());
        if(searchResults.isFound() && i==4) {
        	Assert.fail("After four attempts, the test was unable to randomly find an unused TIN with which to perform a test.");
        }
        boolean found = false;
        ErrorHandling submissionPageErrors = new ErrorHandling(driver);
        List<WebElement> errors = submissionPageErrors.getValidationMessages();
        for(WebElement error : errors) {
        	if(error.getText().contains("No results found for TIN")) {
        		found = true;
        	}
        }
        Assert.assertTrue(found, "Searching by TIN with no results should show an error message \" No results found for TIN.");
	}
}

package scratchpad.rusty.misc;
/*
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import gw.login.LoginFactory;
import gw.login.interfaces.Login;
import gwclockhelpers.ApplicationOrCenter;
import pc.topmenu.TopMenuFactory;
import pc.topmenu.TopMenu;
import pc.topmenu.TopMenuAccount;
import pc.topmenu.TopMenuAdministration;
import pc.topmenu.TopMenuContact;
import pc.topmenu.TopMenuDesktop;
import pc.topmenu.TopMenuPolicy;
import pc.topmenu.TopMenuSearch;
import com.idfbins.driver.BaseTest;
public class TopMenuTest {

	@BeforeClass
	public void beforeClass() throws Exception {
		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
		Login lp = new Login(driver);
		lp.login("su", "gw");
	}
	
	@AfterClass
	public void afterMethod() throws Exception {

	}
	

	@Test
	public void testMenu() throws Exception {
		ITopMenu menu = TopMenuFactory.getMenu();
		menu.clickPolicyArrow();
		menu.clickAccountArrow();
		menu.clickDesktopArrow();
		menu.clickSearchArrow();
	}
	
	
	@Test
	public void testDesktopMenu() throws Exception {
		ITopMenuDesktop menu = TopMenuFactory.getMenuDesktop();
		Configuration config = Configuration.getInstance();
		if(config.getProductVersion().equalsIgnoreCase("8")){
			menu.clickMyActivities();
			menu.clickMyAcccounts();
			menu.clickMySubmissions();
			menu.clickMyRenewals();
			menu.clickMyOtherPolicyTransactions();
			menu.clickMyQueues();
		}
		if(config.getProductVersion().equalsIgnoreCase("7")){
			menu.clickMyActivities();
			menu.clickMySubmissions();
			menu.clickMyRenewals();
			menu.clickMyOtherWorkOrders();
			menu.clickMyQueues();
			menu.clickProofOfMail();
			menu.clickBulkAgentChange();
		}
	}
	
	@Test
	public void testPolicyMenu(driver) throws Exception {
		ITopMenuPolicy menu = TopMenuFactory.getMenuPolicy();
		Configuration config = Configuration.getInstance();
		if(config.getProductVersion().equalsIgnoreCase("8")){
			menu.clickNewSubmission();
			menu.searchForPolicyNumber("1111");
			menu.searchForSubmissionNumber("1111");
			menu.clickFirst();
			menu.clickSecond();
			menu.clickThird();
			menu.clickFourth();
			menu.clickFifth();
		}
		if(config.getProductVersion().equalsIgnoreCase("7")){
			menu.clickNewSubmission();
			menu.clickFirst();
			menu.clickSecond();
			menu.clickThird();
			menu.clickFourth();
			menu.clickFifth();
			menu.clickSixth();
			menu.clickSeventh();
			menu.clickEighth();
			menu.clickNinth();
			menu.clickTenth();
		}
	}

	@Test
	public void testAccountMenu() throws Exception {
		ITopMenuAccount menu = TopMenuFactory.getMenuAccount();
		Configuration config = Configuration.getInstance();
		if(config.getProductVersion().equalsIgnoreCase("8")){
			menu.clickNewAccount();
			menu.searchFor("111");
			menu.clickFirst();
			menu.clickSecond();
			menu.clickThird();
			menu.clickFourth();
			menu.clickFifth();
		}
		if(config.getProductVersion().equalsIgnoreCase("7")){
			menu.clickFirst();
			menu.clickSecond();
			menu.clickThird();
			menu.clickFourth();
			menu.clickFifth();
			menu.clickSixth();
			menu.clickSeventh();
			menu.clickEighth();
			menu.clickNinth();
		}
	}

	@Test
	public void testAdministrationMenu() throws Exception {
		ITopMenuAdministration menu = TopMenuFactory.getMenuAdministration();
		Configuration config = Configuration.getInstance();
		if(config.getProductVersion().equalsIgnoreCase("8")){
			menu.clickUsersAndSecurityMenu();
			//Users & Security menu options
			menu.clickUserSearch();
			menu.clickGroupSearch();
			menu.clickRoles();
			menu.clickRegions();
			menu.clickOrganizationSearch();
			menu.clickProducerCodes();
			menu.clickAuthorityProfiles();
			menu.clickAttributes();
			menu.clickAffinityGroups();
			
			menu.clickBusinessSettings();
			//Business Settings menu options
			menu.clickActivityPatterns();
			menu.clickHolidays();
			menu.clickPolicyFormPatterns();
			menu.clickPolicyHolds();
			
			menu.clickMonitoring();
			//Monitoring menu options
			menu.clickEventMessages();
			menu.clickMessageQueues();
			menu.clickWorkflows();
			menu.clickWorkflowStatistics();
			
			menu.clickUtilities();
			//Business Settings menu options
			menu.clickImportData();
			menu.clickExportData();
			menu.clickScriptParameters();
			menu.clickSpreadsheetExportFormats();
			menu.clickDataChange();
			
		}
		if(config.getProductVersion().equalsIgnoreCase("7")){
			menu.clickActivityPatterns();
			menu.clickAgentNumberSearch();
			menu.clickAttributes();
			menu.clickAuthorityProfiles();
			//menu.clickBatchDocuments();
			menu.clickDocumentPackets();
			menu.clickEventMessages();
			menu.clickMessageSearch();
			menu.clickGroupSearch();
			menu.clickHolidays();
			menu.clickImportExportData();
			menu.clickImportData();
			menu.clickExportData();
			//menu.clickExportCustomReferenceData();
			menu.clickOrganizationSearch();
			menu.clickPolicyFormPatterns();
			menu.clickPolicyHolds();
			menu.clickRegionSearch();
			menu.clickRegions();
			menu.clickRoles();
			menu.clickScriptParameters();
			menu.clickTownshipRange();
			menu.clickTransitionRenewalAcceptance();
			menu.clickUserSearch();
			menu.clickWorkflows();
			menu.clickWorkflowStatistics();
		}
	}
	
	@Test
	public void testSearchMenu() throws Exception {
		ITopMenuSearch menu = TopMenuFactory.getMenuSearch();
		Configuration config = Configuration.getInstance();
		if(config.getProductVersion().equalsIgnoreCase("8")){
			menu.clickPolicies();
			menu.clickAccounts();
			menu.clickProducerCodes();
			menu.clickActivities();
			menu.clickContacts();
		}
		if(config.getProductVersion().equalsIgnoreCase("7")){
			menu.clickAccounts();
			menu.clickActivities();
			menu.clickContacts();
			menu.clickPolicies();
			menu.clickSubmissions();
			menu.clickOtherJobs();
			menu.clickLienholderBilling();
		}
	}
	
	@Test
	public void testContactMenu() throws Exception {
		ITopMenuContact menu = TopMenuFactory.getMenuContact();
		Configuration config = Configuration.getInstance();
		if(config.getProductVersion().equalsIgnoreCase("8")){
			menu.clickNewCompany();
			menu.clickNewPerson();
			menu.clickSearch();
		}
		if(config.getProductVersion().equalsIgnoreCase("7")){
		}
	}
}
*/
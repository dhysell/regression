package previousProgramIncrement.pi3_090518_111518.f217_PolicyCenterDoubleDataEntry;

import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.Gender;

import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.search.SearchAddressBookPC_basics;
import repository.pc.topmenu.TopMenuPolicyPC;
import repository.pc.workorders.submission.SubmissionCreateAccount;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.helpers.DateAddSubtractOptions;
import persistence.helpers.DateUtils;

/**
* @Author sbroderick
* @Requirement When doing a Solr Search, the user would like search Criteria carried to the Create Account Page.
* @RequirementsLink <a href="https://fbmicoi.sharepoint.com/:w:/r/sites/TeamNucleus/_layouts/15/Doc.aspx?sourcedoc=%7B667A80C8-23C6-461B-B046-9ECF96066127%7D&file=US16269%20-%20CM%20-%20Solr%20Auto%20Populate%20all%20Search%20Fields.docx&action=default&mobileredirect=true">US16269_SOLR*** Auto populate all search fields</a>
* @DATE Sep 13, 2018
* 
* Acceptance criteria:
Ensure that the company name prefills to create contact screen
Ensure that the name (First. Middle, Last) prefills to create contact screen
Ensure that the SSN, TIN prefills to the create contact screen
Ensure that the Street prefills to the create contact screen
Ensure that the City prefills to the create contact screen
Ensure that the State prefills to the create contact screen
Ensure that the zip prefills to the create contact screen
Ensure that there is an alert to the user to double check that the prefilled information is correct.
Ensure that the Advanced Search prefills to the create contact screen according to requirements.
* 
*/
public class US16269_AutoPopulateSearchFields extends BaseTest{
	
	public Contact getToNewSubmission(ContactSubType type, WebDriver driver) throws Exception {
		Contact pniContact;
		if(type.equals(ContactSubType.Person)) {
			pniContact = new Contact("Jake", "Zaugg", Gender.Male, DateUtils.dateAddSubtract(new Date(), DateAddSubtractOptions.Year, -21));
			pniContact.setContactIsPNI(true);
			pniContact.setMiddleName("");
			pniContact.setAddress(new AddressInfo(true));
			pniContact.getAddressList().get(0).setPhoneMobile("2082394369");
			pniContact.setTaxIDNumber(String.valueOf(NumberUtils.generateRandomNumberDigits(9)));
			pniContact.setSocialSecurityNumber(String.valueOf(NumberUtils.generateRandomNumberDigits(9)));

		} else {
			pniContact = new Contact();
			pniContact.setCompanyName("Jake you up");
			pniContact.setAddress(new AddressInfo(true));
			pniContact.getAddressList().get(0).setPhoneBusiness("2082394369");
			pniContact.setTaxIDNumber(String.valueOf(NumberUtils.generateRandomNumberDigits(9)));
			pniContact.setSocialSecurityNumber(String.valueOf(NumberUtils.generateRandomNumberDigits(9)));

		}
		
	    Agents agent = AgentsHelper.getRandomAgent();
	    
		new Login(driver).login(agent.getAgentUserName(), agent.getAgentPassword());
		if (new Login(driver).accountLocked()) {
			agent = new Login(driver).loginAsRandomAgent();
		}
		new TopMenuPolicyPC(driver).clickNewSubmission();
		return pniContact;
	}
	
	@Test
	public void testNamePopulatesInCreateAccountPage() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter, "it");
	    WebDriver driver = buildDriver(cf);
	    
	    Contact pniContact = getToNewSubmission(ContactSubType.Person, driver);
	    
		SearchAddressBookPC_basics basicSearch = new SearchAddressBookPC_basics(driver);
		basicSearch.setSearchCriteria(null, null, null, pniContact.getFirstName(), pniContact.getLastName(), null, null, null, null, null);
		basicSearch.clickBasicSearch();
		SearchAddressBookPC searchPC = new SearchAddressBookPC(driver);
		searchPC.clickSearch();
		searchPC.createNew(null, pniContact.getFirstName(), pniContact.getLastName());
		SubmissionCreateAccount createAccountPage = new SubmissionCreateAccount(driver);
		Assert.assertTrue(createAccountPage.namePrefilled(pniContact.getFirstName(), pniContact.getMiddleName(), pniContact.getLastName()), "The first, middle and last names did not populate on the Create Account Page from the Search Page.");
	}
	
	@Test
	public void testCompanyNamePopulatesInCreateAccountPage() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter, "it");
	    WebDriver driver = buildDriver(cf);
	    
	    Contact pniContact = getToNewSubmission(ContactSubType.Company, driver);
	    
		SearchAddressBookPC_basics basicSearch = new SearchAddressBookPC_basics(driver);
		basicSearch.setSearchCriteria(null, null, pniContact.getCompanyName(), null, null, null, null, null, null, null);
		SearchAddressBookPC searchPC = new SearchAddressBookPC(driver);
		searchPC.clickSearch();
		searchPC.createNew(pniContact.getCompanyName(), null, null);
		SubmissionCreateAccount createAccountPage = new SubmissionCreateAccount(driver);
		Assert.assertTrue(createAccountPage.companyNamePrefilled(pniContact.getCompanyName()), "The Company name did not populate on the Create Account Page from the Search Page.");
	}
	
	@Test
	public void ssnAutoPopulatesOnCreateAccountPage() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter, "it");
		WebDriver driver = buildDriver(cf);
		
	    Contact pniContact = getToNewSubmission(ContactSubType.Person, driver);
	    
		SearchAddressBookPC_basics basicSearch = new SearchAddressBookPC_basics(driver);
		basicSearch.setSearchCriteria(null, pniContact.getSocialSecurityNumber(), pniContact.getFirstName(), pniContact.getLastName(), null, null, null, null, null, null);
		SearchAddressBookPC searchPC = new SearchAddressBookPC(driver);
		searchPC.clickSearch();
		//Defect 7907, addresses the behavior that after you click search, the Create new does not become available.
		searchPC.createNew(null, pniContact.getFirstName(), pniContact.getLastName());
		SubmissionCreateAccount createAccountPage = new SubmissionCreateAccount(driver);
		Assert.assertTrue(createAccountPage.ssnPrefilled(pniContact.getSocialSecurityNumber()), "The SSN did not populate on the Create Account Page from the Search Page.");
	}
	
	@Test
	public void tinAutoPopulatesOnCreateAccountPage() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter, "it");
		WebDriver driver = buildDriver(cf);
		
	    Contact pniContact = getToNewSubmission(ContactSubType.Company, driver);
	    
		SearchAddressBookPC_basics basicSearch = new SearchAddressBookPC_basics(driver);
		basicSearch.setSearchCriteria(pniContact.getTaxIDNumber(), null, pniContact.getCompanyName(), null, null, null, null, null, null, null);
		SearchAddressBookPC searchPC = new SearchAddressBookPC(driver);
		searchPC.clickSearch();
		//Defect 7907, addresses the behavior that after you click search, the Create new does not become available.
		searchPC.createNew(pniContact.getCompanyName(), null, null);
		SubmissionCreateAccount createAccountPage = new SubmissionCreateAccount(driver);
		Assert.assertTrue(createAccountPage.tinPrefilled(pniContact.getTaxIDNumber()), "The tin did not populate on the Create Account Page from the Search Page.");
	}
	
	
	
	@Test  
	public void addressAutoPopulatesOnCreateAccountPage() throws Exception {
						
		Config cf = new Config(ApplicationOrCenter.PolicyCenter, "it");
	    WebDriver driver = buildDriver(cf);
	    
	    Contact pniContact = getToNewSubmission(ContactSubType.Person, driver);
		SearchAddressBookPC_basics basicSearch = new SearchAddressBookPC_basics(driver);
		
		basicSearch.setSearchCriteria(null, null, null, pniContact.getFirstName(), pniContact.getLastName(), pniContact.getAddressList().get(0).getLine1(), pniContact.getAddressList().get(0).getCity(), pniContact.getAddressList().get(0).getState(), pniContact.getAddressList().get(0).getZip(), null);
		basicSearch.clickBasicSearch();//not Clicking search
		SearchAddressBookPC searchPC = new SearchAddressBookPC(driver);
		searchPC.createNew(null, pniContact.getFirstName(), pniContact.getLastName());
		SubmissionCreateAccount createAccountPage = new SubmissionCreateAccount(driver);
		Assert.assertTrue(createAccountPage.addressPrefilled(pniContact.getAddressList().get(0).getLine1(), pniContact.getAddressList().get(0).getCity(), pniContact.getAddressList().get(0).getState(), pniContact.getAddressList().get(0).getZip()), "The Address did not populate on the Create Account Page from the Search Page.");
		Assert.assertTrue(createAccountPage.getConfirmAccountInfoText().contains("Please confirm account information"), "Check to ensure that the Please Confirm Account information exists on the Create Account page after a basic search.");
	}
	

	
	@Test
	public void companyPhoneAutoPopulatesOnCreateAccountPage() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter, "it");
		WebDriver driver = buildDriver(cf);
		
	    Contact pniContact = getToNewSubmission(ContactSubType.Company, driver);
	    
		SearchAddressBookPC_basics basicSearch = new SearchAddressBookPC_basics(driver);

		basicSearch.setSearchCriteria(null, null, pniContact.getCompanyName(), null, null, null, null, null, null, pniContact.getAddressList().get(0).getPhoneBusiness());
		SearchAddressBookPC searchPC = new SearchAddressBookPC(driver);
		searchPC.clickSearch();
		
		searchPC.createNew(pniContact.getCompanyName(), null, null);
		SubmissionCreateAccount createAccountPage = new SubmissionCreateAccount(driver);
		Assert.assertTrue(createAccountPage.companyPhonePrefilled(pniContact.getAddressList().get(0).getPhoneBusiness()), "The business Phone did not populate on the Create Account Page from the Search Page.");
	}
	
	@Test
	public void personPhoneAutoPopulatesOnCreateAccountPage() throws Exception{
		Config cf = new Config(ApplicationOrCenter.PolicyCenter, "it");
		WebDriver driver = buildDriver(cf);
		
	    Contact pniContact = getToNewSubmission(ContactSubType.Person, driver);
	    
		SearchAddressBookPC_basics basicSearch = new SearchAddressBookPC_basics(driver);

		basicSearch.setSearchCriteria(null, null, pniContact.getCompanyName(), null, null, null, null, null, null, pniContact.getAddressList().get(0).getPhoneBusiness());
		SearchAddressBookPC searchPC = new SearchAddressBookPC(driver);
		searchPC.clickSearch();
		
		searchPC.createNew(pniContact.getCompanyName(), null, null);
		SubmissionCreateAccount createAccountPage = new SubmissionCreateAccount(driver);
		Assert.assertTrue(createAccountPage.personMobilePhonePrefilled(pniContact.getAddressList().get(0).getPhoneMobile()), "The first, middle and last names did not populate on the Create Account Page from the Search Page.");
	}
	
	
	@Test
	public void advancedSearchNamePopulatesInCreateAccountPage() throws Exception {
		Contact pniContact = new Contact("Jake", "Zaugg", Gender.Male, DateUtils.dateAddSubtract(new Date(), DateAddSubtractOptions.Year, -21));
		pniContact.setContactIsPNI(true);
		pniContact.setAddress(new AddressInfo(true));
		pniContact.setTaxIDNumber("989898989");
		ProductLineType policyType = ProductLineType.Squire;
				
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
	    WebDriver driver = buildDriver(cf);
	    Agents agent = AgentsHelper.getRandomAgent();
	    
		new Login(driver).login(agent.getAgentUserName(), agent.getAgentPassword());
		if (new Login(driver).accountLocked()) {
			agent = new Login(driver).loginAsRandomAgent();
		}
		new TopMenuPolicyPC(driver).clickNewSubmission();
		SearchAddressBookPC advancedSearch = new SearchAddressBookPC(driver);
		advancedSearch.searchAddressBookByFirstLastName(false, pniContact.getFirstName(), pniContact.getLastName(), pniContact.getAddressList().get(0).getLine1(), pniContact.getAddressList().get(0).getCity(), pniContact.getAddressList().get(0).getState(), pniContact.getAddressList().get(0).getZip(), CreateNew.Create_New_Always);
		SubmissionCreateAccount createAccountPage = new SubmissionCreateAccount(driver);
		Assert.assertTrue(createAccountPage.namePrefilled(pniContact.getFirstName(), pniContact.getMiddleName(), pniContact.getLastName()), "The first, middle and last names did not populate on the Create Account Page from the Search Page.");
		
	}
	
	@Test
	public void advancedSearchCompanyNameAutoPopulatesOnCreateAccountPage() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter, "it");
		WebDriver driver = buildDriver(cf);
		
	    Contact pniContact = getToNewSubmission(ContactSubType.Company, driver);
	    
		SearchAddressBookPC advancedSearch = new SearchAddressBookPC(driver);
		advancedSearch.searchAddressBookByCompanyName(false, pniContact.getCompanyName(), pniContact.getAddressList().get(0), CreateNew.Create_New_Always);
		SubmissionCreateAccount createAccountPage = new SubmissionCreateAccount(driver);
		Assert.assertTrue(createAccountPage.companyNamePrefilled(pniContact.getCompanyName()), "The Company name did not populate on the Create Account Page from the Search Page.");
		
	}	
}

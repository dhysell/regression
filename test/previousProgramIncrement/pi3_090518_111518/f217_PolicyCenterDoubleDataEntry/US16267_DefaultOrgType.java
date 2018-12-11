package previousProgramIncrement.pi3_090518_111518.f217_PolicyCenterDoubleDataEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.Gender;

import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.OrganizationTypePL;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.QuoteType;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuPolicyPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderSquireEligibility;
import repository.pc.workorders.submission.SubmissionCreateAccount;
import repository.pc.workorders.submission.SubmissionNewSubmission;
import repository.pc.workorders.submission.SubmissionProductSelection;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.helpers.DateAddSubtractOptions;
import persistence.helpers.DateUtils;

/**
* @Author sbroderick
* @Requirement When the PNI is a person, the Organization type on the Policy Info page will default to Individual.
* @RequirementsLink <a href="https://fbmicoi.sharepoint.com/:w:/r/sites/TeamNucleus/_layouts/15/Doc.aspx?sourcedoc=%7B0BB0B8F4-DA47-4442-9C0F-EB2D0B3DB6AB%7D&file=US16267%20-%20CM%20-%20SelectingCreating%20a%20Person%20Contact%20default%20to%20individual%20on%20Policy%20Info%20Screen.docx&action=default&mobileredirect=true">US16267_DefaultOrgType</a>
* @DATE Sep 20, 2018
*/
public class US16267_DefaultOrgType extends BaseTest{
	private WebDriver driver;
	
	@Test
	public void testUS16267DefaultOrgType() throws Exception {
		Contact pniContact = new Contact("Jake", "Zaugg", Gender.Male, DateUtils.dateAddSubtract(new Date(), DateAddSubtractOptions.Year, -21));
		pniContact.setContactIsPNI(true);
		pniContact.setAddress(new AddressInfo(true));
		pniContact.setTaxIDNumber("989898989");
		ProductLineType policyType = ProductLineType.Squire;
				
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
	    driver = buildDriver(cf);
	    Agents agent = AgentsHelper.getRandomAgent();
	    
		new Login(driver).login(agent.getAgentUserName(), agent.getAgentPassword());
		if (new Login(driver).accountLocked()) {
			agent = new Login(driver).loginAsRandomAgent();
		}
		new TopMenuPolicyPC(driver).clickNewSubmission();
		List<String> pniNames = new SubmissionNewSubmission(driver).fillOutFormSearchAndEitherCreateNewOrUseExisting(true, CreateNew.Create_New_Always, ContactSubType.Person, pniContact.getLastName(), pniContact.getFirstName(), null, null, pniContact.getAddress().getCity(), pniContact.getAddress().getState(), pniContact.getAddress().getZip());
		pniContact.setLastName(pniNames.get(0));
		SubmissionCreateAccount createAcctPage = new SubmissionCreateAccount(driver);
		createAcctPage.createNewContact(pniContact);
		SubmissionProductSelection selectProductPage = new SubmissionProductSelection(driver);
		String accountNumber = selectProductPage.startQuoteSelectProductAndGetAccountNumber(QuoteType.QuickQuote, policyType);
		System.out.println("Account Number = " + accountNumber);
		GenericWorkorderSquireEligibility squireEligibility = new GenericWorkorderSquireEligibility(driver);
		squireEligibility.clickFarmRevenue(false);
		SideMenuPC sideMenuStuff = new SideMenuPC(driver);
		sideMenuStuff.clickSideMenuPolicyInfo();
		GenericWorkorderPolicyInfo policyInfoPage = new GenericWorkorderPolicyInfo(driver);
		String defaultOrgType = policyInfoPage.getOrganizationTypeCurrentOption();
		if(!defaultOrgType.equals(OrganizationTypePL.Individual.getValue())) {
			Assert.fail("The default Organization type when the insured is a person should be individual, currently this test didn't prove the org type was defaulted to individual.");
		}
	}
	
	@Test
	public void testDefaultOrgTypeCanBeChanged() throws GuidewireException, Exception {
				
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();

        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        PolicyLocation myLocation = new PolicyLocation(new AddressInfo(true), locOneBuildingList);

        locationsList.add(myLocation);

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

		GeneratePolicy myPolicy = new GeneratePolicy.Builder(driver)
				.withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("Individual", "Policy")
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolOrgType(OrganizationType.Joint_Venture)
				.withPolicyLocations(locationsList)
				.build(GeneratePolicyType.QuickQuote);	
		
		new Login(driver).loginAndSearchAccountByAccountNumber(myPolicy.agentInfo.agentUserName, myPolicy.agentInfo.agentPassword, myPolicy.accountNumber);
	    AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
	    accountSummaryPage.clickWorkOrderbySuffix("001");
	    SideMenuPC menu = new SideMenuPC(driver);
	    menu.clickSideMenuPolicyInfo();
	    GenericWorkorderPolicyInfo policyInfoPage = new GenericWorkorderPolicyInfo(driver);
	    policyInfoPage.clickEditPolicyTransaction();
	    String orgTypeCurrent = policyInfoPage.getOrganizationTypeCurrentOption();
	    if(orgTypeCurrent.equals(OrganizationType.Individual.getValue())) {
	    	Assert.fail("The Organization type should default to Individual but be able to be changed to like Joint Venture.");
	    }
	}
	
	@Test
	public void testUS16267NoDefaultOrgTypeForComp() throws Exception {
		Contact pniContact = new Contact();
		pniContact.setPersonOrCompany(ContactSubType.Company);
		pniContact.setCompanyName("NewComp");
		pniContact.setContactIsPNI(true);
		pniContact.setAddress(new AddressInfo(true));
		pniContact.setTaxIDNumber("989898989");
		ProductLineType policyType = ProductLineType.Businessowners;
				
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
	    driver = buildDriver(cf);
	    Agents agent = AgentsHelper.getRandomAgent();
	    
		new Login(driver).login(agent.getAgentUserName(), agent.getAgentPassword());
		if (new Login(driver).accountLocked()) {
			agent = new Login(driver).loginAsRandomAgent();
		}
		new TopMenuPolicyPC(driver).clickNewSubmission();
		List<String> pniNames = new SubmissionNewSubmission(driver).fillOutFormSearchAndEitherCreateNewOrUseExisting(true, CreateNew.Create_New_Always, ContactSubType.Company, null, null, null, pniContact.getCompanyName(), pniContact.getAddress().getCity(), pniContact.getAddress().getState(), pniContact.getAddress().getZip());
		pniContact.setCompanyName(pniNames.get(2));
		SubmissionCreateAccount createAcctPage = new SubmissionCreateAccount(driver);
		createAcctPage.createNewContact(pniContact);
		SubmissionProductSelection selectProductPage = new SubmissionProductSelection(driver);
		String accountNumber = selectProductPage.startQuoteSelectProductAndGetAccountNumber(QuoteType.QuickQuote, policyType);
		System.out.println("Account Number = " + accountNumber);
		SideMenuPC sideMenuStuff = new SideMenuPC(driver);
		sideMenuStuff.clickSideMenuPolicyInfo();
		GenericWorkorderPolicyInfo policyInfoPage = new GenericWorkorderPolicyInfo(driver);
		String defaultOrgType = policyInfoPage.getOrganizationTypeCurrentOption();
		if(defaultOrgType.equals(OrganizationTypePL.Individual.getValue())) {
			Assert.fail("The default Organization type when the insured is a person should be individual, currently this test didn't prove the org type to default to individual.");
		}
	}	
}

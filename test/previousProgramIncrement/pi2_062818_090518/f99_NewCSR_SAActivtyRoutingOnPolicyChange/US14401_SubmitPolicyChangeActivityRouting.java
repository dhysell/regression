package previousProgramIncrement.pi2_062818_090518.f99_NewCSR_SAActivtyRoutingOnPolicyChange;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.State;
import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInsuredRole;
import repository.gw.enums.AdditionalInterestSubType;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ChangeReason;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocationAdditionalInsured;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.gw.helpers.DateUtils;
import repository.pc.account.AccountSummaryPC;
import repository.pc.activity.UWActivityPC;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderLocationAdditionalInsured;
import repository.pc.workorders.generic.GenericWorkorderLocations;
import repository.pc.workorders.generic.GenericWorkorderPayerAssignment;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyCoverages;

/**
 * @Author ecoleman
 * @Requirement
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558461772d/detail/userstory/205325340612">US14401 F99 - COMMON - Activity "Submitted Policy Change" should go to Home Office after CSRs click Submits</a>
 * @Description
COMMON
As a CSR or SA I want to see when I hit the 'submit' button on certain allowable policy changes that the activity goes straight to Home Office for issuance and not need to go to the agent first.

On changes that an agent does not need to approve, CSRs will click �Submit� on changes that should route to the Home Office as a �Submitted Policy Change�

Policy changes that can go from CSR or SA directly to home office underwriting:

    Add/remove/change DBA
    Mailing address
    Membership
    Additional insured/certificate holders: remove/add/change
    Location screen additional insureds
    All sections/items additional interest (property, vehicles, section IV items)

Acceptance Criteria:

    Ensure that CSR and SA are able to make each of the above changes and have the submit button
    Ensure that when CSR and SA do the above changes and click 'submit' that these changes go directly to home office underwriting for issuance
    Ensure that when CSR and SA do any other changes they don't have the 'submit' button and they need to 'request approval' to the agent for the policy change
 * @DATE July 17, 2018
 */

public class US14401_SubmitPolicyChangeActivityRouting extends BaseTest {

    public GeneratePolicy myPolicyObject = null;
    private WebDriver driver;


    public void generatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(OrganizationType.Individual)
				.withDownPaymentType(PaymentType.Cash)
                .withInsFirstLastName("ScopeCreeps", "NonFeature")
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(enabled = true)
    public void MailingAddressChange() throws Exception {
    	
//        try {
            generatePolicy();
//        } catch (Exception e) {
//            e.printStackTrace();
//			Assert.fail("Unable to generate policy. Test cannot continue.");
//        }
    	
        String CSRUsername = "cashcraft";
        
        new Login(driver).loginAndSearchAccountByAccountNumber(CSRUsername,
      "gw", myPolicyObject.accountNumber);
        

		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
		AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
		PolicySummary pcPolicySummaryPage = new PolicySummary(driver);
		GenericWorkorderPolicyInfo pcPolicyInfoPage = new GenericWorkorderPolicyInfo(driver);
		BasePage basePage = new BasePage(driver);
		UWActivityPC activity = new UWActivityPC(driver);
		GenericWorkorderComplete pcCompletePage = new GenericWorkorderComplete(driver);
		
		pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();
		policyChangePage.startPolicyChange("change mailing address", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
		
		AddressInfo newMailingAddress = new AddressInfo(true);
		
		pcSideMenu.clickSideMenuPolicyInfo();
		
		pcPolicyInfoPage.addNewAddress(newMailingAddress, "");

		pcWorkOrder.clickGenericWorkorderQuote();
		
		// Acceptance criteria: submit button should be showing and active
		Assert.assertTrue(basePage.checkIfElementExists("//a[contains(@id, 'RequestApproval')]", 5), "Submit button was not visible, it should be");
		Assert.assertFalse(basePage.checkIfElementExists("//a[contains(@id, 'RequestApproval')][contains(@class, 'x-disabled')]", 5), "Submit button was disabled, it should not be");
		
		
		pcWorkOrder.clickGenericWorkorderSubmitOptionsSubmit();		
        activity.setText("Sending this over stuff and stuff");
        activity.setChangeReason(ChangeReason.AnyOtherChange);
        activity.clickSendRequest();        
        pcCompletePage.clickViewYourPolicy();
        
        String assignedTo = pcPolicySummaryPage.getCurrentActivites_BasicInfo().get(0).getAssignedTo();
        String assignedToFirstName = assignedTo.split(" ")[0];
        String assignedToLastName = assignedTo.split(" ")[1];
        
        Boolean uwAssignedToExists = pcAccountSummaryPage.checkUWExists(assignedToFirstName, assignedToLastName);
        
        Assert.assertTrue(uwAssignedToExists, "UW was not found, assignment did not go to UW!");
    }
    
    @Test(enabled = true)
    	public void PropertyScreenAdditionalInterestChain() throws Exception {
    	
//        try {
            generatePolicy();
//        } catch (Exception e) {
//            e.printStackTrace();
//			Assert.fail("Unable to generate policy. Test cannot continue.");
//        }

        String CSRUsername = "cashcraft";        
        new Login(driver).loginAndSearchAccountByAccountNumber(CSRUsername,
                "gw", myPolicyObject.accountNumber);

		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail pcPropertyDetailPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
		GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
		AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
		PolicySummary pcPolicySummaryPage = new PolicySummary(driver);
		GenericWorkorderAdditionalInterests pcAdditionalInterests = new GenericWorkorderAdditionalInterests(driver);
		GenericWorkorderPayerAssignment pcPayerAssignmentPage = new GenericWorkorderPayerAssignment(driver);
		BasePage basePage = new BasePage(driver);
		UWActivityPC activity = new UWActivityPC(driver);
		GenericWorkorderComplete pcCompletePage = new GenericWorkorderComplete(driver);
		
		pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();
		policyChangePage.startPolicyChange("Property Additional Interest Addition", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
		
		pcSideMenu.clickSideMenuSquirePropertyDetail();
		pcPropertyDetailPage.clickViewOrEditBuildingButton(1);
		
		AddressInfo additionalInterestAddress = new AddressInfo(true);
		additionalInterestAddress.setCity("Anchorage");
		additionalInterestAddress.setState(State.Alaska);
		additionalInterestAddress.setZip("99503");
		AdditionalInterest additionalInterest = new AdditionalInterest("Wells Fargo", additionalInterestAddress); // WELLS FARGO NOT FOUND
		additionalInterest.setAdditionalInterestSubType(AdditionalInterestSubType.PLSectionIProperty);
		additionalInterest.setAdditionalInterestType(AdditionalInterestType.LessorPL);
		pcAdditionalInterests.fillOutAdditionalInterest(true, additionalInterest);
		pcWorkOrder.clickOK();
		
		pcSideMenu.clickSideMenuPayerAssignment();
		pcPayerAssignmentPage.setPayerAssignmentVerificationConfirmationCheckbox(true);
		
		pcWorkOrder.clickGenericWorkorderQuote();
		
		// Acceptance criteria: submit button should be showing and active
		Assert.assertTrue(basePage.checkIfElementExists("//a[contains(@id, 'RequestApproval')]", 5), "Submit button was not visible, it should be");
		Assert.assertFalse(basePage.checkIfElementExists("//a[contains(@id, 'RequestApproval')][contains(@class, 'x-disabled')]", 5), "Submit button was disabled, it should not be");
		
		
		pcWorkOrder.clickGenericWorkorderSubmitOptionsSubmit();		
        activity.setText("Sending this over stuff and stuff");
        activity.setChangeReason(ChangeReason.AnyOtherChange);
        activity.clickSendRequest();        
        pcCompletePage.clickViewYourPolicy();
        
        String assignedTo = pcPolicySummaryPage.getCurrentActivites_BasicInfo().get(0).getAssignedTo();
        String assignedToFirstName = assignedTo.split(" ")[0];
        String assignedToLastName = assignedTo.split(" ")[1];
        
        Boolean uwAssignedToExists = pcAccountSummaryPage.checkUWExists(assignedToFirstName, assignedToLastName);
        
        Assert.assertTrue(uwAssignedToExists, "UW was not found, assignment did not go to UW!");
    }
    
    @Test(enabled = true)
    public void SectionIDeductibleChange() throws Exception {
    	
//        try {
            generatePolicy();
//        } catch (Exception e) {
//            e.printStackTrace();
//			Assert.fail("Unable to generate policy. Test cannot continue.");
//        }

        String CSRUsername = "cashcraft";
        
        new Login(driver).loginAndSearchAccountByAccountNumber(CSRUsername,
                "gw", myPolicyObject.accountNumber);

		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
		AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
		GenericWorkorderSquirePropertyCoverages pcSquirePropertyCoveragesPage = new GenericWorkorderSquirePropertyCoverages(driver);
		BasePage basePage = new BasePage(driver);
		
		pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();
		policyChangePage.startPolicyChange("Change Section I Deductible", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
		
		pcSideMenu.clickSideMenuSquirePropertyCoverages();		
		
		pcSquirePropertyCoveragesPage.selectSectionIDeductible(SectionIDeductible.OneThousand);

		pcWorkOrder.clickGenericWorkorderQuote();
		
		// Acceptance criteria: submit button should be showing and active
		Assert.assertTrue(basePage.checkIfElementExists("//a[contains(@id, 'RequestApproval')]", 5), "Submit button was not visible, it should be");
		Assert.assertTrue(basePage.checkIfElementExists("//a[contains(@id, 'RequestApproval')][contains(@class, 'x-disabled')]", 5), "Submit button was not disabled, it should be");
    }
    
    @Test(enabled = true)
    public void BOPLocationAdditionalInterestChange() throws Exception {
    	
//        try {
        	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
            driver = buildDriver(cf);
        	myPolicyObject = new GeneratePolicy.Builder(driver)
    				.withInsPersonOrCompany(ContactSubType.Company)
    				.withPolOrgType(OrganizationType.Partnership)
    				.withProductType(ProductLineType.Businessowners)
    				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.Apartments))
    				.withCreateNew(CreateNew.Create_New_Always)
    				.withInsCompanyName("ScopCrep")
    				.withDownPaymentType(PaymentType.Cash)
    				.withLineSelection(LineSelection.Businessowners)
    				.build(GeneratePolicyType.PolicyIssued);
//        } catch (Exception e) {
//            e.printStackTrace();
//			Assert.fail("Unable to generate policy. Test cannot continue.");
//        }

        String CSRUsername = "cashcraft";
        
        new Login(driver).loginAndSearchAccountByAccountNumber(CSRUsername,
                "gw", myPolicyObject.accountNumber);

		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
		AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
		BasePage basePage = new BasePage(driver);
		GenericWorkorderLocationAdditionalInsured pcBOPLocationAddtionalInsured = new GenericWorkorderLocationAdditionalInsured(driver);
		GenericWorkorderLocations pcBOPLocationsPage = new GenericWorkorderLocations(driver);
		PolicyLocationAdditionalInsured policyLocationAdditionalInsured = new PolicyLocationAdditionalInsured();
		
		pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();
		policyChangePage.startPolicyChange("Change Section Location AddtionalInterest change", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
		
		pcSideMenu.clickSideMenuLocations();
		
		pcBOPLocationsPage.clickEdit();
		
		policyLocationAdditionalInsured = new PolicyLocationAdditionalInsured();
		policyLocationAdditionalInsured.setAiRole(AdditionalInsuredRole.OwnersOrOtherInterests);
		policyLocationAdditionalInsured.setNewContact(CreateNew.Create_New_Always);
		
		pcBOPLocationAddtionalInsured.addAdditionalInsured(true, policyLocationAdditionalInsured);
		pcWorkOrder.clickOK();

		pcWorkOrder.clickGenericWorkorderQuote();
		
		// Acceptance criteria: submit button should be showing and active
		Assert.assertTrue(basePage.checkIfElementExists("//a[contains(@id, 'RequestApproval')]", 5), "Submit button was not visible, it should be");
		Assert.assertTrue(basePage.checkIfElementExists("//a[contains(@id, 'RequestApproval')][contains(@class, 'x-disabled')]", 5), "Submit button was not disabled, it should be");
    }
}





















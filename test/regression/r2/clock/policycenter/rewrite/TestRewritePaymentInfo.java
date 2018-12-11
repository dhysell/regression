package regression.r2.clock.policycenter.rewrite;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;

import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireEligibility;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : US14154: When payment info is entered and transaction is
 * released back to agent, start the 10 day count over, Old:
 * US9689: Common - Payment information (including cc token) needs
 * to be saved for 10 days after entering
 * @RequirementsLink <a href=
 * "http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Common-Admin/PC8%20-%20Common%20-%20QuoteApplication%20-%20Payment.xlsx">
 * Story card</a>
 * @Description : validating 10day message before release lock and after release
 * lock and payment information is removed after 10 days
 * @DATE Oct 13, 2017
 */
@QuarantineClass
public class TestRewritePaymentInfo extends BaseTest {
	private WebDriver driver;
	GeneratePolicy myPolRewObj;
	GeneratePolicy myStdFirePol;
	String paymentMessage = "All payment information will be removed in 10 days if the transaction is not bound";

	/*
	 * Description: Quote a Rewrite Full Term and enter payment information.  
	 * Move clock 9 days and then "Request Approval."  
	 * Move clock 30 days and Release Lock.  
	 * The screen must display the 10 day message.  
	 * Move clock 10 days and payment information must still display, 
	 * move the clock 1 more day and the payment information must be expired and no longer visible.
	 */
	@Test()
	private void testIssueAndCancelSquirePol() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;
		myPropertyAndLiability.liabilitySection = myLiab;

		Squire mySquire = new Squire(SquireEligibility.Country);
		mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolRewObj = new GeneratePolicy.Builder(driver).withSquire(mySquire).withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL).withInsFirstLastName("Payment", "Rewrite")
				.withPaymentPlanType(PaymentPlanType.Annual).withDownPaymentType(PaymentType.Credit_Debit)
				.build(GeneratePolicyType.PolicyIssued);
        driver.quit();
        
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				myPolRewObj.accountNumber);
        StartCancellation cancelPol = new StartCancellation(driver);
		Date currentDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.ExcessiveErrors, "Testing Purpose", currentDate,
				true);

		// searching again
        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
		policySearchPC.searchPolicyByPolicyNumber(myPolRewObj.squire.getPolicyNumber());
        PolicySummary summary = new PolicySummary(driver);
		if (summary.getTransactionNumber(TransactionType.Cancellation, "Testing Purpose") == null) {
			Assert.fail("Cancellation is not done!!!");
		}
	}
	@Test(dependsOnMethods = {"testIssueAndCancelSquirePol"})
	private void testRewriteBeforeRequestApproval() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolRewObj.agentInfo.getAgentUserName(),
				myPolRewObj.agentInfo.getAgentPassword(), myPolRewObj.accountNumber);
        StartRewrite rewritePage = new StartRewrite(driver);

        PolicyMenu policyMenu = new PolicyMenu(driver);
		policyMenu.clickMenuActions();
		policyMenu.clickRewriteFullTerm();
        rewritePage.rewriteFullTermGuts(myPolRewObj);
		new GuidewireHelpers(driver).editPolicyTransaction();
		
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		coverage.setCoverageALimit(1200000);
		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
		riskAnalysis.Quote();
        sideMenu.clickSideMenuQuote();
		sideMenu.clickSideMenuPayment();
		GenericWorkorderPayment paymentPage = new GenericWorkorderPayment(driver);
		paymentPage.fillOutPaymentPage(myPolRewObj);
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		
		if (!paymentPage.checkPaymentWarningDisplayed() && !paymentPage.getPaymentWarningMessage().contains(paymentMessage)) {
			Assert.fail("Expected Warning message : '"+paymentMessage+"'");
		}

		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.requestApproval();

        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new BasePage(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
        completePage.clickAccountNumber();
        AccountSummaryPC summary = new AccountSummaryPC(driver);
		summary.clickAccountSummaryPendingTransactionByStatus("Rewrite");
		sideMenu.clickSideMenuPayment();

		// Validating after 10days
		moveClockMoreThan10days(10);
		sideMenu.clickSideMenuForms();
		sideMenu.clickSideMenuPayment();
		if (!paymentPage.checkPaymentWarningDisplayed() && !paymentPage.getPaymentWarningMessage()
				.contains(paymentMessage)) {
			Assert.fail(
					"After 10 days of Approval Request - Expected Warning message : '"+paymentMessage+"'");
		}

		// Validating after 30days
		moveClockMoreThan10days(20);
		sideMenu.clickSideMenuForms();
		sideMenu.clickSideMenuPayment();
		if (!paymentPage.checkPaymentWarningDisplayed() && !paymentPage.getPaymentWarningMessage()
				.contains(paymentMessage)) {
			Assert.fail(
					"After 30 days of Approval Request - Expected Warning message : '"+paymentMessage+"'");
		}
	}
	
	@Test(dependsOnMethods = {"testRewriteBeforeRequestApproval"})
	private void testRewriteAfterReleaseLock() throws Exception{
		// Validating after release lock
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolRewObj.underwriterInfo.getUnderwriterUserName(), myPolRewObj.underwriterInfo.getUnderwriterPassword(),
				myPolRewObj.accountNumber);
        PolicySummary policySummary = new PolicySummary(driver);
		policySummary.clickPendingTransaction(TransactionType.Rewrite);
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.approveAll();
		risk.clickReleaseLock();
		new GuidewireHelpers(driver).logout();
		driver.quit();
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchJob(myPolRewObj.agentInfo.getAgentUserName(), myPolRewObj.agentInfo.getAgentPassword(),
				myPolRewObj.accountNumber);
        sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPayment();
		GenericWorkorderPayment paymentPage = new GenericWorkorderPayment(driver);

		// Validating after release lock
		if (!paymentPage.checkPaymentWarningDisplayed()
				&& !paymentPage.getPaymentWarningMessage().contains(paymentMessage)) {
			Assert.fail("After release lock - Expected Warning message : '" + paymentMessage + "'");
		}

		// Validating after 10 days of release lock
		moveClockMoreThan10days(10);
		sideMenu.clickSideMenuForms();
		sideMenu.clickSideMenuPayment();
		if (!paymentPage.checkPaymentWarningDisplayed()
				&& !paymentPage.getPaymentWarningMessage().contains(paymentMessage)) {
			Assert.fail("After 10 days of release lock - Expected Warning message : '" + paymentMessage + "'");
		}

		// Validating after 11 days of release lock
		moveClockMoreThan10days(1);
		sideMenu.clickSideMenuForms();
		sideMenu.clickSideMenuPayment();
		paymentPage = new GenericWorkorderPayment(driver);
		if (paymentPage.getDownPaymentRowCount() > 0) {
			Assert.fail("After 11 days of release lock - Down payment details still exists in FA");
		}
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
	}

	/* @Description :Quote a Rewrite New Term, enter payment information and "Request Approval."
	  Move clock 30 days and Release Lock.  The screen must display the 10 day message.  
	  Move clock 10 days and payment information must still display, 
	  move the clock 1 more day and the payment information must be expired and no longer visible.  
	 */
	@Test()
	private void testIssueAndCancelStdFirePol() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));
		PolicyLocation propLoc = new PolicyLocation(locOnePropertyList);
		propLoc.setPlNumAcres(10);
		propLoc.setPlNumResidence(5);
		locationsList.add(propLoc);

		ArrayList<LineSelection> productLines = new ArrayList<LineSelection>();
		productLines.add(LineSelection.StandardFirePL);

        myStdFirePol = new GeneratePolicy.Builder(driver).withProductType(ProductLineType.StandardFire)
				.withLineSelection(LineSelection.StandardFirePL).withCreateNew(CreateNew.Create_New_Always)
				.withInsFirstLastName("Issue", "Rewrite").withPolOrgType(OrganizationType.Partnership)
				.withPolicyLocations(locationsList).withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Credit_Debit).build(GeneratePolicyType.PolicyIssued);
        driver.quit();
        
        cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				myStdFirePol.accountNumber);
        StartCancellation cancelPol = new StartCancellation(driver);
		Date currentDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);

		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.ExcessiveErrors, "Testing Purpose", currentDate,
				true);

		// searching again
        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
		policySearchPC.searchPolicyByPolicyNumber(myStdFirePol.standardFire.getPolicyNumber());
        PolicySummary summary = new PolicySummary(driver);
		if (summary.getTransactionNumber(TransactionType.Cancellation, "Testing Purpose") == null) {
			Assert.fail("Cancellation is not done!!!");
		}
	}
	
	@Test(dependsOnMethods = {"testIssueAndCancelStdFirePol"})
	private void testRewriteStdFireBeforeApprovalRequest() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(myStdFirePol.underwriterInfo.getUnderwriterUserName(), myStdFirePol.underwriterInfo.getUnderwriterPassword(),
				myStdFirePol.accountNumber);
        //		PolicyMenu policyMenu = new PolicyMenu(driver);
//		policyMenu.clickMenuActions();
//		policyMenu.clickRewriteNewTerm();
        new StartRewrite(driver).startRewriteNewTerm();
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuQualification();
		GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
		qualificationPage.setSquireGeneralFullTo(false);
		qualificationPage.setStandardFireFullTo(false);
		sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		coverage.setCoverageALimit(1200000);
		sideMenu.clickSideMenuQualification();
        GenericWorkorderSquireEligibility eligibilityPage = new GenericWorkorderSquireEligibility(driver);
		for (int i = 0; i < 6; i++) {
			eligibilityPage.clickNext();
        }
		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
        sideMenu.clickSideMenuQuote();
		sideMenu.clickSideMenuPayment();
		myStdFirePol.downPaymentType = PaymentType.ACH_EFT;
		GenericWorkorderPayment paymentPage = new GenericWorkorderPayment(driver);
		paymentPage.fillOutPaymentPage(myStdFirePol);

		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		if (!paymentPage.checkPaymentWarningDisplayed() && !paymentPage.getPaymentWarningMessage().contains(paymentMessage)) {
			Assert.fail("Expected Warning message : '"+paymentMessage+"'");
		}
		sideMenu.clickSideMenuPayment();

		// Validating after 10days
		moveClockMoreThan10days(10);
		sideMenu.clickSideMenuForms();
		sideMenu.clickSideMenuPayment();
		if (!paymentPage.checkPaymentWarningDisplayed() && !paymentPage.getPaymentWarningMessage().contains(paymentMessage)) {
			Assert.fail("After 10 days of Approval Request - Expected Warning message : '"+paymentMessage+"'");
		}

		// Validating after 30days
		moveClockMoreThan10days(20);
		sideMenu.clickSideMenuForms();
		sideMenu.clickSideMenuPayment();
		if (!paymentPage.checkPaymentWarningDisplayed() && !paymentPage.getPaymentWarningMessage().contains(paymentMessage)) {
			Assert.fail("After 30 days of Approval Request - Expected Warning message : '"+paymentMessage+"'");
		}
	}
	
	@Test(dependsOnMethods = {"testRewriteStdFireBeforeApprovalRequest"})
	private void testValidateStdFireRewriteAfterReleaseLock() throws Exception {
		// Validating after release lock
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(myStdFirePol.underwriterInfo.getUnderwriterUserName(), myStdFirePol.underwriterInfo.getUnderwriterPassword(),
				myStdFirePol.accountNumber);
        PolicySummary policySummary = new PolicySummary(driver);
		policySummary.clickPendingTransaction(TransactionType.Rewrite_New_Term);
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.approveAll();
		risk.clickReleaseLock();
		new GuidewireHelpers(driver).logout();
		
		driver.quit();
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchJob(myStdFirePol.agentInfo.getAgentUserName(), myStdFirePol.agentInfo.getAgentPassword(),
				myStdFirePol.accountNumber);
        sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPayment();
		GenericWorkorderPayment paymentPage = new GenericWorkorderPayment(driver);

		// Validating after release lock
		if (!paymentPage.checkPaymentWarningDisplayed()
				&& !paymentPage.getPaymentWarningMessage().contains(paymentMessage)) {
			Assert.fail("After release lock - Expected Warning message : '" + paymentMessage + "'");
		}

		// Validating after 10 days of release lock
		moveClockMoreThan10days(10);
		sideMenu.clickSideMenuForms();
		sideMenu.clickSideMenuPayment();
		if (!paymentPage.checkPaymentWarningDisplayed()
				&& !paymentPage.getPaymentWarningMessage().contains(paymentMessage)) {
			Assert.fail("After 10 days of release lock - Expected Warning message : '" + paymentMessage + "'");
		}

		// Validating after 11 days of release lock
		moveClockMoreThan10days(1);
		sideMenu.clickSideMenuForms();
		sideMenu.clickSideMenuPayment();
		paymentPage = new GenericWorkorderPayment(driver);
		if (paymentPage.getDownPaymentRowCount() > 0) {
			Assert.fail("After 11 days of release lock - Down payment details still exists in FA");
		}
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
	}

	private void moveClockMoreThan10days(int days) throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, days);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Remove_Saved_PaymentInfo);
    }

	

}

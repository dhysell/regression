package regression.r2.clock.policycenter.submission_misc;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.activity.ActivityPopup;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import persistence.globaldatarepo.entities.Underwriters;

/**
 * @Author nvadlamudi
 * @Requirement : US14154: When payment info is entered and transaction is
 *              released back to agent, start the 10 day count over, Old:
 *              US9689: Common - Payment information (including cc token) needs
 *              to be saved for 10 days after entering
 * @RequirementsLink <a href=
 *                   "http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Common-Admin/PC8%20-%20Common%20-%20QuoteApplication%20-%20Payment.xlsx">
 *                   Story card</a>
 * @Description : validating 10day message before release lock and after release
 *              lock and payment information is removed after 10 days
 * @DATE Oct 13, 2017
 */
public class TestFAPaymentInfoAfterReleaseLock extends BaseTest {
	private WebDriver driver;
	GeneratePolicy myPolicyObj;
	Underwriters uw;
	String paymentMessage = "All payment information will be removed in 10 days if the transaction is not bound";

	/*
	 * On a new submission enter payment information and "Request Approval."
	 * Move clock 30 days and Release Lock. The screen must display the 10 day
	 * message. Move clock 10 days and payment information must still display,
	 * move the clock 1 more day and the payment information must be expired and
	 * no longer visible.
	 */
	@Test()
	private void testSquirePolicyFA() throws Exception {
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

        myPolicyObj = new GeneratePolicy.Builder(driver).withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL).withInsFirstLastName("Payment", "Submis")
				.withPaymentPlanType(PaymentPlanType.Annual).withDownPaymentType(PaymentType.ACH_EFT)
				.build(GeneratePolicyType.FullApp);
	}

	@Test(dependsOnMethods = { "testSquirePolicyFA" })
	private void testBeforeRequestApproval() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(),
				myPolicyObj.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquirePropertyCoverages();
		GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		coverage.setCoverageALimit(1200000);
		sideMenu.clickSideMenuRiskAnalysis();
		new GenericWorkorder(driver).clickGenericWorkorderQuote();

		GenericWorkorderPayment paymentPage = new GenericWorkorderPayment(driver);
		paymentPage.fillOutPaymentPage(myPolicyObj);

		if (!paymentPage.checkPaymentWarningDisplayed()
				&& !paymentPage.getPaymentWarningMessage().contains(paymentMessage)) {
			Assert.fail("Expected Warning message : '" + paymentMessage + "'");
		}
		// If user changes Payment Plan, clear out amount but keep other payment
		// information.
		myPolicyObj.paymentPlanType = PaymentPlanType.Quarterly;
		paymentPage.removeFirstDownPayment();
		paymentPage.fillOutPaymentPage(myPolicyObj);

		if (!paymentPage.checkPaymentWarningDisplayed()
				&& !paymentPage.getPaymentWarningMessage().contains(paymentMessage)) {
			Assert.fail("After changing the Payment Plan  - Warning message : '" + paymentMessage + "'");
		}

		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.requestApproval();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new BasePage(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
        GenericWorkorderComplete woComplete = new GenericWorkorderComplete(driver);
		woComplete.clickAccountNumber();
        AccountSummaryPC summary = new AccountSummaryPC(driver);
		uw = summary.getAssignedToUW("Approval Requested");
		summary.clickAccountSummaryPendingTransactionByStatus("Quoted");
		sideMenu.clickSideMenuPayment();

		// Validating after 10days
		moveClockMoreThan10days(10);
		sideMenu.clickSideMenuForms();
		sideMenu.clickSideMenuPayment();
		if (!paymentPage.checkPaymentWarningDisplayed()
				&& !paymentPage.getPaymentWarningMessage().contains(paymentMessage)) {
			Assert.fail("After 10 days - Expected Warning message : '" + paymentMessage + "'");
		}

		// Validating after 30days
		moveClockMoreThan10days(20);
		sideMenu.clickSideMenuForms();
		sideMenu.clickSideMenuPayment();
		if (!paymentPage.checkPaymentWarningDisplayed()
				&& !paymentPage.getPaymentWarningMessage().contains(paymentMessage)) {
			Assert.fail("After 30 days - Expected Warning message : '" + paymentMessage + "'");
		}
	}

	@Test(dependsOnMethods = { "testBeforeRequestApproval" })
	private void testAfterReleaseLock() throws Exception {
		// Validating after release lock
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				myPolicyObj.accountNumber);
        AccountSummaryPC acct = new AccountSummaryPC(driver);
		acct.clickActivitySubject("Approval Requested");
        ActivityPopup actPop = new ActivityPopup(driver);
		actPop.clickCompleteButton();

		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.approveAll();
		risk.clickReleaseLock();
		new GuidewireHelpers(driver).logout();
		driver.quit();
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(),
				myPolicyObj.accountNumber);
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
    	driver = buildDriver(cf);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, days);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Remove_Saved_PaymentInfo);
    }

}

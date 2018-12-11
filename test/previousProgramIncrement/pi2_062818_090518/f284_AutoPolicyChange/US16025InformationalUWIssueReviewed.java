package previousProgramIncrement.pi2_062818_090518.f284_AutoPolicyChange;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationTypePL;
import repository.gw.enums.PLUWIssues;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.UnderwriterIssue;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderSquireCLUEProperty;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement US16025: Resolve Review Button Editability
 * @RequirementsLink <a href=
 *                   "https://rally1.rallydev.com/#/203558458764/detail/userstory/246586879132">
 *                   Resolve Review Button Editability</a>
 * @Description : Validate Review Button is shown for informational UW Issues
 *              and UW able to review them
 * @DATE Aug 20, 2018
 */

/*
 * DE7851: Auto Issued Activity is not showing up for SQ004: Organization Type Other is created for the issue
 */
public class US16025InformationalUWIssueReviewed extends BaseTest {
	private String activityName = "Auto-Issued Submission for UW Review";

	@Test
	public void testCheckUWIssuesInformationalReview() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
		Squire mySquire = new Squire(new GuidewireHelpers(driver).getRandomEligibility());

		GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("Review", "Button")
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.FullApp);

		new Login(driver).loginAndSearchSubmission(myPolicyObjPL);
		SideMenuPC sideMenu = new SideMenuPC(driver);
		new GuidewireHelpers(driver).editPolicyTransaction();
		sideMenu.clickSideMenuPolicyInfo();
		GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);

		// SQ004 - Squire Organization type of other
		policyInfo.setPolicyInfoOrganizationType(OrganizationTypePL.Other);
		policyInfo.setPolicyInfoDescription("Testing Description");

		sideMenu.clickSideMenuSquirePropertyCLUE();
		new GenericWorkorderSquireCLUEProperty(driver).clickRetrieveCLUE();
		new GenericWorkorderRiskAnalysis(driver).Quote();

		sideMenu.clickSideMenuRiskAnalysis();
		boolean checkUWInfo = new GenericWorkorderRiskAnalysis(driver).getUnderwriterIssues().getInformationalList().size() > 0;
		FullUnderWriterIssues uwIssues = new GenericWorkorderRiskAnalysis(driver).getUnderwriterIssues();

		//Adding the below loop for checking the ClaimCenter Down - UW Issues -- Test failed in REGR environments because of this
		for(UnderwriterIssue currentUW :uwIssues.getInformationalList()){
			if(currentUW.getShortDescription().equalsIgnoreCase(PLUWIssues.ClaimCenterDown.getShortDesc())){
				checkUWInfo =  uwIssues.getInformationalList().size() > 1;
			}
		}
		// check the Submit options -> Issue Policy
		sideMenu.clickSideMenuPayment();
		GenericWorkorderPayment payment = new GenericWorkorderPayment(driver);
		payment.fillOutPaymentPage(myPolicyObjPL);
		Assert.assertTrue(payment.SubmitOptionsButtonExists(), "Auto Issuance - Agent can't have Submit Options to submit a policy..");
		if (myPolicyObjPL.squire.squireEligibility.equals(SquireEligibility.FarmAndRanch)) {
			Assert.assertTrue(payment.checkGenericWorkorderSubmitOptionsSubmitOnlyOption(), "Auto Issuance - Agent can't see Submit Options -> Submit option to submit a policy..");
		} else {
			Assert.assertTrue(payment.checkGenericWorkorderSubmitOptionsIssuePolicyOption(), "Auto Issuance - Agent can't see Submit Options -> Issue Policy option to issue a policy..");
			sideMenu.clickSideMenuForms();
			payment.clickGenericWorkorderSubmitOptionsIssuePolicy();
			GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
			new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
			myPolicyObjPL.squire.setPolicyNumber(completePage.getPolicyNumber());
			completePage.clickPolicyNumber();
			PolicySummary polSummary = new PolicySummary(driver);
			Assert.assertNotNull(polSummary.getCompletedTransactionNumberByType(TransactionType.Issuance), "Agent Auto Issue  - completed Issuance job not found.");

			if (checkUWInfo) {
				Assert.assertTrue(polSummary.checkIfActivityExists(activityName), "Expected Activity : '" + activityName + "' is not displayed.");
				Underwriters uw = UnderwritersHelper.getUnderwriterInfoByFullName(polSummary.getActivityAssignment("Auto-Issued Submission for UW Review"));
				new GuidewireHelpers(driver).logout();
				new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.squire.getPolicyNumber());
				polSummary.clickActivity("Auto-Issued Submission for UW Review");
				sideMenu = new SideMenuPC(driver);
				sideMenu.clickSideMenuRiskAnalysis();
				GenericWorkorderRiskAnalysis_UWIssues risk = new GenericWorkorderRiskAnalysis_UWIssues(driver);
				risk.clickUWIssuesTab();

				Assert.assertTrue(risk.checkIfReviewButtonsExist(), "Expected Review button for UW Informational messages is not displayed.");
				risk.reviewAllInformationalUWIssues();

				FullUnderWriterIssues allUWIssues = risk.getUnderwriterIssues();

				for (UnderwriterIssue currentIUW : allUWIssues.getAlreadyApprovedList()) {
					new GenericWorkorder(driver).clickWhenClickable(By.xpath("//a[text()='" + currentIUW.getShortDescription() + "']"));
					Assert.assertTrue(risk.checkUWHistoryName(uw.getUnderwriterFirstName()) > 0, "Underwriter name is not shown un UW Issues History....");
					new GenericWorkorder(driver).clickReturnTo();
				}

			}
		}
	}

}

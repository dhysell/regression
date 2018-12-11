package previousProgramIncrement.pi2_062818_090518.f284_AutoPolicyChange;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PLUWIssues;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.UnderwriterIssue;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireCLUEProperty;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;

/**
 * @Author nvadlamudi
 * @Requirement :US15556: Enable auto-issuance of agent-submitted new business
 *              policies - Squire
 * @RequirementsLink <a href=
 *                   "http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Personal%20Lines%20-%20Common/PC8%20-%20Personal%20Lines%20-%20Auto%20Issuance.xlsx">
 *                   PC8 - Personal Lines - Auto Issuance</a>
 * @Description : Validate Agent able to auto issue a policy if there are no uw
 *              issues or after UW approves it
 * @DATE Aug 16, 2018
 */
public class US15556SquireAgentAutoIssueNewBusiness extends BaseTest {
	private String activityName = "Auto-Issued Submission for UW Review";

	@Test
	public void testSquireAgentAutoIssueAfterUWApproval() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
		Squire mySquire = new Squire(new GuidewireHelpers(driver).getRandomEligibility());

		GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("Submit", "Change")
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.FullApp);

		new Login(driver).loginAndSearchSubmission(myPolicyObjPL);
		SideMenuPC sideMenu = new SideMenuPC(driver);		
		sideMenu.clickSideMenuRiskAnalysis();
		FullUnderWriterIssues uwIssues = new GenericWorkorderRiskAnalysis(driver).getUnderwriterIssues();
		boolean checkUWInfo =  uwIssues.getInformationalList().size() > 0;
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
			new GuidewireHelpers(driver).clickProductLogo();
			Assert.assertTrue(payment.checkGenericWorkorderSubmitOptionsIssuePolicyOption(), "Auto Issuance - Agent can't see Submit Options -> Issue Policy option to issue a policy..");
			sideMenu.clickSideMenuForms();
			payment.clickGenericWorkorderSubmitOptionsIssuePolicy();
			GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
			new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
			completePage.clickPolicyNumber();
			PolicySummary polSummary = new PolicySummary(driver);
			Assert.assertNotNull(polSummary.getCompletedTransactionNumberByType(TransactionType.Issuance), "Agent Auto Issue  - completed Issuance job not found.");
			
			if(checkUWInfo){
				Assert.assertTrue(polSummary.checkIfActivityExists(activityName), "Expected Activity : '" + activityName + "' is not displayed.");
			}
		}
	}

	@Test
	public void testSquireAgentAutoIssueWithOutApproval() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
		Squire mySquire = new Squire(new GuidewireHelpers(driver).getRandomEligibility());

		GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("Submit", "Change")
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.isDraft()
				.build(GeneratePolicyType.FullApp);

		new Login(driver).loginAndSearchSubmission(myPolicyObjPL);
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquirePropertyCLUE();
		new GenericWorkorderSquireCLUEProperty(driver).clickRetrieveCLUE();
		sideMenu.clickSideMenuSquirePropertyDetail();
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(
				driver);
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.ResidencePremises);

		propertyDetail.clickPropertyConstructionTab();
		// PR090 Defensible or not
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(
				driver);
		constructionPage.clickProtectionDetailsTab();
		GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(
				driver);
		
		protectionPage.setDefensibleSpace(false);
		propertyDetail.clickOK();
		propertyDetail.clickOkayIfMSPhotoYearValidationShows();		
		sideMenu.clickSideMenuRiskAnalysis();
		new GenericWorkorderRiskAnalysis(driver).performRiskAnalysisAndQuote(myPolicyObjPL);	
		sideMenu.clickSideMenuRiskAnalysis();
		FullUnderWriterIssues uwIssues = new GenericWorkorderRiskAnalysis(driver).getUnderwriterIssues();
		boolean checkUWInfo =  uwIssues.getInformationalList().size() > 0;
		//Adding the below loop for checking the ClaimCenter Down - UW Issues -- Test failed in REGR environments because of this
		for(UnderwriterIssue currentUW :uwIssues.getInformationalList()){
			if(currentUW.getShortDescription().equalsIgnoreCase(PLUWIssues.ClaimCenterDown.getShortDesc())){
				checkUWInfo =  uwIssues.getInformationalList().size() > 1;
			}
		}
		boolean stillUWIssuesExists = false;
		if(uwIssues.getBlockQuoteList().size()> 0 || uwIssues.getBlockSubmitList().size() > 0){
			stillUWIssuesExists = true;
			
		}
		sideMenu.clickSideMenuPayment();
		GenericWorkorderPayment payment = new GenericWorkorderPayment(driver);
		payment.fillOutPaymentPage(myPolicyObjPL);
		sideMenu.clickSideMenuForms();
		Assert.assertTrue(payment.SubmitOptionsButtonExists(), "Auto Issuance - Agent can't have Submit Options to submit a policy..");
		if (myPolicyObjPL.squire.squireEligibility.equals(SquireEligibility.FarmAndRanch) || stillUWIssuesExists) {
			new GuidewireHelpers(driver).clickProductLogo();
			Assert.assertTrue(payment.checkGenericWorkorderSubmitOptionsSubmitOnlyOption(), "Auto Issuance - Agent can't see Submit Options -> Submit option to submit a policy..");
		} else {
			sideMenu.clickSideMenuPayment();
			new GuidewireHelpers(driver).clickProductLogo();
			Assert.assertTrue(payment.checkGenericWorkorderSubmitOptionsIssuePolicyOption(), "Auto Issuance - Agent can't see Submit Options -> Issue Policy option to issue a policy..");
			sideMenu.clickSideMenuForms();
			payment.clickGenericWorkorderSubmitOptionsIssuePolicy();
			GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
			new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
			completePage.clickPolicyNumber();
			PolicySummary polSummary = new PolicySummary(driver);
			Assert.assertNotNull(polSummary.getCompletedTransactionNumberByType(TransactionType.Issuance), "Agent Auto Issue  - completed Issuance job not found.");
			
			if(checkUWInfo){
				Assert.assertTrue(polSummary.checkIfActivityExists(activityName),"Expected Activity : '" + activityName + "' is not displayed.");
			}
					

		}

	}

}

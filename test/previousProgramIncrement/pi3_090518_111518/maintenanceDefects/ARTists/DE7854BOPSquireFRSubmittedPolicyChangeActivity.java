package previousProgramIncrement.pi3_090518_111518.maintenanceDefects.ARTists;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.activity.ActivityPopup;
import repository.gw.enums.BusinessownersLine;
import repository.gw.enums.ChangeReason;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.Squire;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.activity.UWActivityPC;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
* @Author nvadlamudi
* @Requirement :DE7854: BOP & F&R Submitted policy change activity does not close
* @RequirementsLink <a href="https://rally1.rallydev.com/#/203558458764/detail/defect/249559007448">Link Text</a>
* @Description : The activity should be closed and no longer on the current activities section
* @DATE Sep 10, 2018
*/
@Test(groups= {"ClockMove"})
public class DE7854BOPSquireFRSubmittedPolicyChangeActivity extends BaseTest {
	
	  @Test
		public void testSquireFRPolicyChangeSubmittingActivity() throws Exception {
			Config cf = new Config(ApplicationOrCenter.PolicyCenter);
			WebDriver driver = buildDriver(cf);
			Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);

			GeneratePolicy myPolObjPL = new GeneratePolicy.Builder(driver)
					.withSquire(mySquire)
					.withProductType(ProductLineType.Squire)
					.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
					.withInsFirstLastName("FRChange", "Submit")
					.withPaymentPlanType(PaymentPlanType.Annual)
					.withDownPaymentType(PaymentType.Cash)
					.build(GeneratePolicyType.PolicyIssued);
			
		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolObjPL.agentInfo.getAgentUserName(),
				myPolObjPL.agentInfo.getAgentPassword(), myPolObjPL.accountNumber);

		// start policy change
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("first policy Change", myPolObjPL.squire.getEffectiveDate());
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuRiskAnalysis();
		GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		risk.Quote();
		sideMenu.clickSideMenuRiskAnalysis();
		policyChangePage.clickSubmitPolicyChange();
		UWActivityPC activity = new UWActivityPC(driver);
		activity.setText("Please Approve this Stuff!!");
		activity.setChangeReason(ChangeReason.AnyOtherChange);
		activity.clickSendRequest();
		GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
		completePage.clickViewYourPolicy();
		
		PolicySummary polSummary = new PolicySummary(driver);
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByFullName(new PolicySummary(driver).getActivityAssignment("Submitted policy change"));
		new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolObjPL.accountNumber);
        new PolicySummary(driver).clickActivity("Submitted policy change");
        ActivityPopup actPop = new ActivityPopup(driver);
		actPop.clickActivityCancel();

		StartPolicyChange change = new StartPolicyChange(driver);
		change.clickIssuePolicy();
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
		completePage.clickViewYourPolicy();
		Assert.assertTrue(!polSummary.checkIfActivityExists("Submitted policy change"), "Still activity 'Submitted policy change' exists.");
			
	  }
	  
	  @Test()
	  public void testBOPPolicyChangeSubmittingActivity() throws Exception {
	      Config cf = new Config(ApplicationOrCenter.PolicyCenter);
	      WebDriver driver = buildDriver(cf);

	      GeneratePolicy myPolicyObj = new GeneratePolicy.Builder(driver)
	              .withInsPersonOrCompany(ContactSubType.Person)
	              .withProductType(ProductLineType.Businessowners)
	              .withBusinessownersLine(new PolicyBusinessownersLine(BusinessownersLine.SmallBusinessType.Apartments))
	              .withLineSelection(LineSelection.Businessowners)
	              .build(GeneratePolicyType.PolicyIssued);
	      
	      new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObj.agentInfo.getAgentUserName(),
	    		  myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

			// start policy change
			StartPolicyChange policyChangePage = new StartPolicyChange(driver);
			policyChangePage.startPolicyChange("first policy Change",myPolicyObj.squire.getEffectiveDate());
			SideMenuPC sideMenu = new SideMenuPC(driver);
			sideMenu.clickSideMenuRiskAnalysis();
			GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
			new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
			risk.Quote();
			sideMenu.clickSideMenuRiskAnalysis();
			policyChangePage.clickSubmitPolicyChange();
			UWActivityPC activity = new UWActivityPC(driver);
			activity.setText("Please Approve this Stuff!!");
			activity.setChangeReason(ChangeReason.BuildingLimitsChanged);
			activity.clickSendRequest();
			GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
			new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
			completePage.clickViewYourPolicy();
			
			PolicySummary polSummary = new PolicySummary(driver);
			Underwriters uw = UnderwritersHelper.getUnderwriterInfoByFullName(new PolicySummary(driver).getActivityAssignment("Submitted policy change"));
			new GuidewireHelpers(driver).logout();
	        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.accountNumber);
	        new PolicySummary(driver).clickActivity("Submitted policy change");
	        ActivityPopup actPop = new ActivityPopup(driver);
			actPop.clickActivityCancel();

			StartPolicyChange change = new StartPolicyChange(driver);
			change.clickIssuePolicy();
			new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
			completePage.clickViewYourPolicy();
			Assert.assertTrue(!polSummary.checkIfActivityExists("Submitted policy change"), "Still activity 'Submitted policy change' exists.");
				
	  }
}

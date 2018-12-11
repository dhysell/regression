package regression.r2.noclock.policycenter.cancellation;

import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.ReasonNotTaken;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.actions.ActionsPC;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderInsuranceScore;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers;
import repository.pc.workorders.generic.GenericWorkorderVehicles;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @author nvadlamudi
 * @Requirement :DE4409: COMMON - User cannot start the cancellation if the Cancellation Effective Date = Policy Effective Date
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/46478569317d/detail/defect/77073623512">DE4409</a>
 * @Description
 * @DATE Feb 6, 2017
 */
@QuarantineClass
public class TestCopySubmissionCancelNow extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy notTakenPolicyObj;
	
	@Test()
	public void testQuickQuote() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = new SquirePersonalAuto();

        notTakenPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("SubmissionCopy", "Cancel")
				.withProductType(ProductLineType.Squire)
				.withPolOrgType(OrganizationType.Individual)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.QuickQuote);
	}
	
	@Test(dependsOnMethods = { "testQuickQuote" })
	public void testPolicyNotTaken() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchSubmission(notTakenPolicyObj.agentInfo.getAgentUserName(), notTakenPolicyObj.agentInfo.getAgentPassword(), notTakenPolicyObj.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();
        GenericWorkorder generic = new GenericWorkorder(driver);
		generic.clickNotTaken();

		generic.setReasonNotTakenCode(ReasonNotTaken.PricedTooHigh);
		generic.setNotTakenComments("They said Geico is better!");
        generic.clickButtonNotTaken();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
    }

	@Test(dependsOnMethods = { "testPolicyNotTaken" })
	public void testCopySubmissionAsAgent() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchSubmission(notTakenPolicyObj.agentInfo.getAgentUserName(), notTakenPolicyObj.agentInfo.getAgentPassword(), notTakenPolicyObj.accountNumber);
        ActionsPC actions = new ActionsPC(driver);
		actions.click_Actions();
        actions.click_CopySubmission();
    }
	
	@Test(dependsOnMethods = {"testCopySubmissionAsAgent"})
	public void testIssuePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchSubmission(notTakenPolicyObj.agentInfo.getAgentUserName(), notTakenPolicyObj.agentInfo.getAgentPassword(), notTakenPolicyObj.accountNumber);

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		quote.clickGenericWorkorderFullApp();

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuLineSelection();
		sideMenu.clickSideMenuHouseholdMembers();		
		sideMenu.clickSideMenuQualification();
        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
		qualificationPage.setSquireAutoFullTo(false);
		qualificationPage.setSquireGeneralFullTo(false);

		sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
		policyInfo.fillOutPolicyInfoPageFA(this.notTakenPolicyObj);
		
		sideMenu.clickSideMenuPLInsuranceScore();
        GenericWorkorderInsuranceScore creditReport = new GenericWorkorderInsuranceScore(driver);
		creditReport.fillOutCreditReport(notTakenPolicyObj);
		sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers policyDriver = new GenericWorkorderSquireAutoDrivers(driver);
		policyDriver.fillOutDriversFA(notTakenPolicyObj);

		sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles vehiclePage = new GenericWorkorderVehicles(driver);
		vehiclePage.fillOutVehicles_FA(this.notTakenPolicyObj);
		vehiclePage.clickNext();

        GenericWorkorderRiskAnalysis_UWIssues risk = new GenericWorkorderRiskAnalysis_UWIssues(driver);
		risk.performRiskAnalysisAndQuote(notTakenPolicyObj);

        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.hasBlockBind()) {
			risk.handleBlockSubmit(notTakenPolicyObj);
		}

		sideMenu.clickSideMenuForms();

		new GuidewireHelpers(driver).logout();

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        new GuidewireHelpers(driver).setPolicyType(notTakenPolicyObj, GeneratePolicyType.FullApp);
		notTakenPolicyObj.convertTo(driver, GeneratePolicyType.PolicyIssued);

	}


  @Test(dependsOnMethods = {"testIssuePolicy"})
  public void testCancellation() throws Exception {
	  Config cf = new Config(ApplicationOrCenter.PolicyCenter);
	  driver = buildDriver(cf);
	  Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
      new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), notTakenPolicyObj.squire.getPolicyNumber());
      StartCancellation cancelPol = new StartCancellation(driver);
		Date currentDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
		
		String comment = "Testing Purpose";
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.ThisAccountNotMeetingOurEligibilityRequirements, comment , currentDate, true);

      SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
      policySearchPC.searchPolicyByPolicyNumber(notTakenPolicyObj.squire.getPolicyNumber());
      PolicySummary summary = new PolicySummary(driver);
		if (summary.getTransactionNumber(TransactionType.Cancellation, comment) == null)
			Assert.fail("Cancellation is not done.");
  }
}

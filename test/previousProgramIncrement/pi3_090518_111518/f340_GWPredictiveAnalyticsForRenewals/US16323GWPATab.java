package previousProgramIncrement.pi3_090518_111518.f340_GWPredictiveAnalyticsForRenewals;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.administration.AdminScriptParameters;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.ScriptParameter;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuAdministrationPC;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;

/**
* @Author bhiltbrand
* @Requirement Ensure that the Predictive Analytics tab only shows up on the risk analysis page if the 'PredictiveAnalyticsUIDebugEnabled' Script parameter is set to yes.
* @RequirementsLink <a href="https://rally1.rallydev.com/#/203558478112/detail/userstory/251383423464">Rally Story US16323</a>
* @Description This test will take a policy and verify that the PA tab is not visible. Then, it will change the script parameter and verify that the PA tab is visible. 
* @DATE Oct 23, 2018
*/
@Test(groups = {"ClockMove"}) //This is not technically a clock move test, but it will mess with script parameters, so it needs to run in series.
public class US16323GWPATab extends BaseTest {
	private GeneratePolicy myPolicyObjPL;
	private WebDriver driver;
	private SoftAssert softAssert = new SoftAssert();

	@Test
	public void generateQuickQuoteAndCheckRiskAnalysis() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		Squire mySquire = new Squire(SquireEligibility.City);		
			
		myPolicyObjPL = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("Predictive", "Analytics")
				.build(GeneratePolicyType.QuickQuote);
		
		checkPredictiveAnalyticsTab(true, false);
		
		try {	
			//Need to set Predictive Analytics Enabled Script Parameter to true
			new Login(driver).login("su", "gw");
			new TopMenuAdministrationPC(driver).clickScriptParameters();
			new AdminScriptParameters(driver).editScriptParameter(ScriptParameter.PredictiveAnalyticsEnabled, true);
			new GuidewireHelpers(driver).logout();
			//End Script Parameter Setup
			
			checkPredictiveAnalyticsTab(false, true);			
			checkPredictiveAnalyticsLowerTable(false);
			
			//Need to set Predictive Analytics Debug UI Enabled Script Parameter to true
			new Login(driver).login("su", "gw");
			new TopMenuAdministrationPC(driver).clickScriptParameters();
			new AdminScriptParameters(driver).editScriptParameter(ScriptParameter.PredictiveAnalyticsUIDebugEnabled, true);
			new GuidewireHelpers(driver).logout();
			//End Script Parameter Setup
			
			checkPredictiveAnalyticsLowerTable(true);
			
			softAssert.assertAll();
	  } catch (Exception e) {
		  throw e;
	  } finally {
		  //switch Verisk Score enabled back to false
		  try {
			  new GuidewireHelpers(driver).logout();
		  } catch(Exception e) {
			  //Already logged out.
		  }
			new Login(driver).login("su", "gw");
			new TopMenuAdministrationPC(driver).clickScriptParameters();
			AdminScriptParameters scriptParameters = new AdminScriptParameters(driver);
			scriptParameters.editScriptParameter(ScriptParameter.PredictiveAnalyticsEnabled, false);
			scriptParameters.clickUpToScriptParametersLink();
			scriptParameters.editScriptParameter(ScriptParameter.PredictiveAnalyticsUIDebugEnabled, false);
			new GuidewireHelpers(driver).logout();
			//End Script Parameter Setup
	  }
	}
	
	private void checkPredictiveAnalyticsTab (boolean editPolicyTransaction, boolean expectedResult) {
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL);
		if (editPolicyTransaction) {
			new GuidewireHelpers(driver).editPolicyTransaction();
		}
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuRiskAnalysis();
		
		if (expectedResult) {
			Assert.assertTrue(new GuidewireHelpers(driver).checkIfElementExists("//span[contains(@id, 'Job_RiskAnalysisScreen:RiskAnalysisCV:PredictiveAnalyticsTab-btnEl')]", 1), "Predictive Analystics Tab was not on the page when it should have been there. Test cannot continue.");
		} else {
			softAssert.assertFalse(new GuidewireHelpers(driver).checkIfElementExists("//span[contains(@id, 'Job_RiskAnalysisScreen:RiskAnalysisCV:PredictiveAnalyticsTab-btnEl')]", 1), "Predictive Analystics Tab should not have been here.");
		}
		
		new GuidewireHelpers(driver).logout();
	}
	
	private void checkPredictiveAnalyticsLowerTable (boolean expectedResult) {
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL);
		
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuRiskAnalysis();
		
		new GenericWorkorderRiskAnalysis(driver).clickPredictiveAnalyticsTab();
		
		if (expectedResult) {
			softAssert.assertTrue(new GuidewireHelpers(driver).checkIfElementExists("//div[contains(@id, 'SubmissionWizard:Job_RiskAnalysisScreen:RiskAnalysisCV:SolutionDetailPanel:0')]", 1), "The Solution Details Table was not on the Predictive Analystics Tab when it should have been.");
		} else {
			softAssert.assertFalse(new GuidewireHelpers(driver).checkIfElementExists("//div[contains(@id, 'SubmissionWizard:Job_RiskAnalysisScreen:RiskAnalysisCV:SolutionDetailPanel:0')]", 1), "The Solution Details Table was not on the Predictive Analystics Tab should not have been.");
		}
		
		new GuidewireHelpers(driver).logout();
	}
}

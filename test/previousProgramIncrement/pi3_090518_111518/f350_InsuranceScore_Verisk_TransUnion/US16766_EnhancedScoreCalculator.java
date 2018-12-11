package previousProgramIncrement.pi3_090518_111518.f350_InsuranceScore_Verisk_TransUnion;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.administration.AdminScriptParameters;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.ScriptParameter;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.InsuranceScore_Verisk;
import repository.gw.generate.custom.Squire;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuAdministrationPC;
import repository.pc.workorders.generic.GenericWorkorderInsuranceScore;
import persistence.globaldatarepo.entities.InsuranceScoreTestCases;
import persistence.globaldatarepo.helpers.ISSupportUsersHelper;
import persistence.globaldatarepo.helpers.InsuranceScoreTestCasesHelper;

@Test(groups= {"ClockMove"})
public class US16766_EnhancedScoreCalculator extends BaseTest {

	
	
	
	@Test(enabled = true)
	public void veriskEnhancedScoreCalculator() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
		
		InsuranceScoreTestCases testCase = InsuranceScoreTestCasesHelper.getRandomContact_withDOB();
		
		Squire mySquire = new Squire(SquireEligibility.City);

		try {
			
			//Need to set Verisk Credit Score Enabled Script Parameter to true
			new Login(driver).login("su", "gw");
			new TopMenuAdministrationPC(driver).clickScriptParameters();
			new AdminScriptParameters(driver).editScriptParameter(ScriptParameter.VeriskCreditScoreEnabled, true);
			new GuidewireHelpers(driver).logout();
			//End Script Parameter Setup
			
			GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver)
					.withSquire(mySquire)
					.withProductType(ProductLineType.Squire)
					.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
					.withVeriskContact(testCase)
					.withAdvancedSearch()
					.isDraft()
					.build(GeneratePolicyType.FullApp);
			
			new Login(driver).loginAndSearchSubmission(ISSupportUsersHelper.getRandomUser().getUserName(), "gw", myPolicyObjPL.accountNumber);
			new SideMenuPC(driver).clickSideMenuPLInsuranceScore();
			
			InsuranceScore_Verisk score = new GenericWorkorderInsuranceScore(driver).getInsuranceScore_Verisk();
			int enhacnedScore = score.calculateEnhancedScore(driver, testCase);
			Assert.assertTrue(Integer.valueOf(score.getEnhancedInsuranceScore()) == enhacnedScore, "ENHANCED SCORE ON THE UI DID NOT MATCH THE CALCULATED SCORE. UI: " + score.getEnhancedInsuranceScore() + " CALCULATE: " + enhacnedScore);
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
			new AdminScriptParameters(driver).editScriptParameter(ScriptParameter.VeriskCreditScoreEnabled, false);
			new GuidewireHelpers(driver).logout();
			//End Script Parameter Setup
		}
	
	}
}














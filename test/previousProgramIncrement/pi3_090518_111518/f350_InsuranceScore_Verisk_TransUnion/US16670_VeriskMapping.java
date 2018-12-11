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
import persistence.globaldatarepo.helpers.InsuranceScoreTestCasesHelper;

@Test(groups= {"ClockMove"})
public class US16670_VeriskMapping extends BaseTest {


	@Test(enabled = true)
	public void testVeriskMappinig_InsufficientInforamtion() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);

		InsuranceScoreTestCases testCase = InsuranceScoreTestCasesHelper.getRandomContact_BySSN("666315982");

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
					.isDraft()
					.build(GeneratePolicyType.FullApp);

			new Login(driver).loginAndSearchSubmission(myPolicyObjPL);
			new SideMenuPC(driver).clickSideMenuPLInsuranceScore();
			Assert.assertTrue(new GenericWorkorderInsuranceScore(driver).getVeriskFlag().equals("Insufficient credit data"), "VERISK FLAG DID NOT INDICATE Insufficient credit data WHEN TEST DATA " + myPolicyObjPL.pniContact.getFullName() + " WAS USED FOR ORDERING INSURANCE SCORE");;
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


	@Test(enabled = true)
	public void testVeriskMappinig_NoHit() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);


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
					.isDraft()
					.build(GeneratePolicyType.FullApp);

			new Login(driver).loginAndSearchSubmission(myPolicyObjPL);
			new SideMenuPC(driver).clickSideMenuPLInsuranceScore();

			InsuranceScore_Verisk score = new GenericWorkorderInsuranceScore(driver).getInsuranceScore_Verisk();
			Assert.assertTrue(score.getInsuranceStatus().equals("No Hit"), "WHEN USING RANDOM DATA INSURANCE SCORE DID NOT COME BACK AS \"No Hit\" RECEIVED " + score.getInsuranceStatus());
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

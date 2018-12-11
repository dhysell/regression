package scratchpad.James;

import org.openqa.selenium.WebDriver;
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

public class US17073 extends BaseTest{
	private WebDriver driver;
	private GeneratePolicy myPolicyObjPL;

	
	@Test
	public void testPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		InsuranceScoreTestCases testCase = InsuranceScoreTestCasesHelper.getRandomContact_withDOB();

		try {
	
			//Need to set Verisk Credit Score Enabled Script Parameter to true
			new Login(driver).login("su", "gw");
			new TopMenuAdministrationPC(driver).clickScriptParameters();
			new AdminScriptParameters(driver).editScriptParameter(ScriptParameter.VeriskCreditScoreEnabled, true);
			new GuidewireHelpers(driver).logout();
			//End Script Parameter Setup
			System.out.println("Squire Policy will be issued!");
			
			Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);
			myPolicyObjPL = new GeneratePolicy.Builder(driver)
					.withProductType(ProductLineType.Squire)
					.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
					.withVeriskContact(testCase)
					.withSquire(mySquire)
					.build(GeneratePolicyType.FullApp);
	
			System.out.println("Squire Policy has been successfully issued!");
			System.out.println("StandardFire policy will be issued");
			
			myPolicyObjPL.addLineOfBusiness(ProductLineType.StandardFire, GeneratePolicyType.PolicyIssued);
			
			new Login(driver).loginAndSearchSubmission(ISSupportUsersHelper.getRandomUser().getUserName(), "gw", myPolicyObjPL.accountNumber);
			new SideMenuPC(driver).clickSideMenuPLInsuranceScore();
			
			InsuranceScore_Verisk score = new GenericWorkorderInsuranceScore(driver).getInsuranceScore_Verisk();
			int enhacnedScore = score.calculateEnhancedScore(driver, testCase);
			
			
			String insuranceScore = testCase.getScore1();
			System.out.println("My insurance score is: " + insuranceScore);
			System.out.println("Enhanced insurance score is: " + enhacnedScore);
			
			} catch (Exception e) {
				throw e;
			} finally {
				//switch Verisk Score enabled back to false
				try {
					
					System.out.println("fianlly loggout statement");
					new GuidewireHelpers(driver).logout();
				} catch(Exception e) {
				//Already logged out.
				}
				new Login(driver).login("su", "gw");
				new TopMenuAdministrationPC(driver).clickScriptParameters();
				new AdminScriptParameters(driver).editScriptParameter(ScriptParameter.VeriskCreditScoreEnabled, false);
				new GuidewireHelpers(driver).logout();
				//End Script Parameter Setup
				
				System.out.println("Test is successfully finished! YAY!");
				driver.quit();
			}			
	}	
}

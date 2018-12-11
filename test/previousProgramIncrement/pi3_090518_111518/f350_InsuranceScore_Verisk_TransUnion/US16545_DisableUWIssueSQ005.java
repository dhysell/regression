package previousProgramIncrement.pi3_090518_111518.f350_InsuranceScore_Verisk_TransUnion;

import static org.testng.Assert.assertTrue;

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
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.Squire;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuAdministrationPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
@Test(groups= {"ClockMove"})
public class US16545_DisableUWIssueSQ005 extends BaseTest {

	
	private GeneratePolicy myPolicyObjPL;

	@Test
	public void disableUWIssueSQ005() throws Exception {
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
			
			myPolicyObjPL = new GeneratePolicy.Builder(driver)
					.withSquire(mySquire)
					.withProductType(ProductLineType.Squire)
					.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
					.isDraft()
					.build(GeneratePolicyType.FullApp);
			
			new Login(driver).loginAndSearchSubmission(myPolicyObjPL);
			new GenericWorkorder(driver).clickGenericWorkorderQuote();
			new SideMenuPC(driver).clickSideMenuRiskAnalysis();
			FullUnderWriterIssues uwIssues = new GenericWorkorderRiskAnalysis_UWIssues(driver).getUnderwriterIssues();
			assertTrue(uwIssues.isInList("has Insurance Score less or equal to 500, Underwriting approval required to bind.").equals(UnderwriterIssueType.NONE), "UNDERWRITER ISSUE SQ005 WAS TRIGGERED WHEN INSURANCE SCORE WAS LESS THAN 500. IT SHOULD NOT TRIGGER AT ALL.");
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

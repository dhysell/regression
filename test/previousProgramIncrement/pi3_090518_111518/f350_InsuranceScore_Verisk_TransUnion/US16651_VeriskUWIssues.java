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
import repository.gw.enums.UnderwriterIssues_PL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.Squire;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuAdministrationPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
@Test(groups= {"ClockMove"})
public class US16651_VeriskUWIssues extends BaseTest {

	
	//DISABLED DUE TO FULL FUNCTIONALITY NOT COMPLETE FROM ARTISTS TEAM. 
	//CANNOT QUOTE WITHOUT GETTING ERROR ABOUT NOT ORDERING CREDIT REPORT. WHEN IT WAS ORDERED VIA VERISK
	@Test(enabled = false)
	public void veriskUWIssue() throws Exception {
		
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
			new SideMenuPC(driver).clickSideMenuRiskAnalysis();
			new GenericWorkorder(driver).clickGenericWorkorderQuote();
			if(new GenericWorkorderQuote(driver).isPreQuoteDisplayed()) {
				new GenericWorkorderQuote(driver).clickPreQuoteDetails();
			}
			new SideMenuPC(driver).clickSideMenuRiskAnalysis();
			FullUnderWriterIssues uwIssues = new GenericWorkorderRiskAnalysis_UWIssues(driver).getUnderwriterIssues();
			Assert.assertTrue(uwIssues.isInList(UnderwriterIssues_PL.UnableToRetrieveInsuranceScore_SQ081.getLongDescription()).equals(UnderwriterIssues_PL.UnableToRetrieveInsuranceScore_SQ081.getIssuetype()), "UNDERWRITER ISSUE " + UnderwriterIssues_PL.UnableToRetrieveInsuranceScore_SQ081.name() + "DID NOT INFER AS EXPECTED. FOUND| " + uwIssues.isInList(UnderwriterIssues_PL.UnableToRetrieveInsuranceScore_SQ081.getLongDescription()).name() + " EXPECTED| " + UnderwriterIssues_PL.UnableToRetrieveInsuranceScore_SQ081.getIssuetype().name());
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



















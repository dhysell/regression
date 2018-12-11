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
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.InsuranceScore_Verisk;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.Squire;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuAdministrationPC;
import repository.pc.workorders.generic.GenericWorkorderInsuranceScore;
import persistence.enums.Underwriter.UnderwriterLine;
import persistence.globaldatarepo.entities.InsuranceScoreTestCases;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.InsuranceScoreTestCasesHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

@Test(groups= {"ClockMove"})
public class US16798_ChangeInsuranceScoreContact extends BaseTest {

	
	@Test(enabled = true)
	public void testVeriskMappinig_InsufficientInforamtion() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
		
		InsuranceScoreTestCases testCase_PNI = InsuranceScoreTestCasesHelper.getRandomContact();
		InsuranceScoreTestCases testCase_Member1 = InsuranceScoreTestCasesHelper.getRandomContact();
		InsuranceScoreTestCases testCase_Member2 = InsuranceScoreTestCasesHelper.getRandomContact();
		
		Contact policyMember1 = new Contact(testCase_Member1);
		policyMember1.setRelationshipAB(null);
		Contact policyMember2 = new Contact(testCase_Member2);
		policyMember2.setRelationshipAB(null);
		
		Squire mySquire = new Squire(SquireEligibility.City);
		
		PolicyInfoAdditionalNamedInsured foobert = new PolicyInfoAdditionalNamedInsured(policyMember1);
		PolicyInfoAdditionalNamedInsured foobert3rd = new PolicyInfoAdditionalNamedInsured(policyMember2);
 
		try {
			
			//Need to set Verisk Credit Score Enabled Script Parameter to true
			new Login(driver).login("su", "gw");
			new TopMenuAdministrationPC(driver).clickScriptParameters();
			new AdminScriptParameters(driver).editScriptParameter(ScriptParameter.VeriskCreditScoreEnabled, true);
			new GuidewireHelpers(driver).logout();
			//End Script Parameter Setup
			
			
			GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver)
					.withSquire(mySquire)
					.withANIList(foobert, foobert3rd)
					.withProductType(ProductLineType.Squire)
					.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
					.withVeriskContact(testCase_PNI)
					.isDraft()
					.build(GeneratePolicyType.FullApp);
			
			Underwriters underWriter = UnderwritersHelper.getRandomUnderwriter(UnderwriterLine.Personal);
			new Login(driver).loginAndSearchSubmission(underWriter.getUnderwriterUserName(), underWriter.getUnderwriterPassword(), myPolicyObjPL.accountNumber);
			new SideMenuPC(driver).clickSideMenuPLInsuranceScore();
			new GenericWorkorderInsuranceScore(driver).selectCreditReportIndividual(policyMember1.getLastName());
			new GenericWorkorderInsuranceScore(driver).clickOrderReport();
			InsuranceScore_Verisk score = new GenericWorkorderInsuranceScore(driver).getInsuranceScore_Verisk();
			Assert.assertFalse(score.getInsuranceStatus().equals("No Hit"), "AFTER CHANGING THE SELECTED CONTACT FOR INSURANCE SCORE FOR " + policyMember1.getFullName());
			
			new SideMenuPC(driver).clickSideMenuPolicyInfo();
			new SideMenuPC(driver).clickSideMenuPLInsuranceScore();
			
			new GenericWorkorderInsuranceScore(driver).selectCreditReportIndividual(policyMember2.getLastName());
			new GenericWorkorderInsuranceScore(driver).clickOrderReport();
			InsuranceScore_Verisk score2 = new GenericWorkorderInsuranceScore(driver).getInsuranceScore_Verisk();
			Assert.assertFalse(score2.getInsuranceStatus().equals("No Hit"), "AFTER CHANGING THE SELECTED CONTACT FOR INSURANCE SCORE FOR " + policyMember2.getFullName());
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

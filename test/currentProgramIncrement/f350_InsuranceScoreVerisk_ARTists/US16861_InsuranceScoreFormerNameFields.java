package currentProgramIncrement.f350_InsuranceScoreVerisk_ARTists;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
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
import repository.pc.workorders.generic.GenericWorkorderInsuranceScore;
import persistence.globaldatarepo.entities.InsuranceScoreTestCases;
import persistence.globaldatarepo.helpers.InsuranceScoreTestCasesHelper;

@Test(groups = {"ClockMove"})
public class US16861_InsuranceScoreFormerNameFields  extends BaseTest {

	
	
	@Test(enabled = true)
	public void verifyPayloadForVeriskInsurancescore() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
		SoftAssert softAssert = new SoftAssert();
		
		InsuranceScoreTestCases testCase = InsuranceScoreTestCasesHelper.getRandomContact_withDOB();
		
		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.insuranceScoreReport.setLivedAtAddressLessThan6Months(true);
		mySquire.insuranceScoreReport.setNameChangelast6Months(true);

		try {
			
			//Need to set Verisk Credit Score Enabled Script Parameter to true
			new Login(driver).login("su", "gw");
			new TopMenuAdministrationPC(driver).clickScriptParameters();
			new AdminScriptParameters(driver).editScriptParameter(ScriptParameter.VeriskCreditScoreEnabled, true);
			new GuidewireHelpers(driver).logout();
			//End Script Parameter Setup
			
			GeneratePolicy myPolicyObj = new GeneratePolicy.Builder(driver)
					.withSquire(mySquire)
					.withProductType(ProductLineType.Squire)
					.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
					.withVeriskContact(testCase)
					.withAdvancedSearch()
					.isDraft()
					.build(GeneratePolicyType.QuickQuote);
			
			new Login(driver).loginAndSearchSubmission(myPolicyObj);
			new SideMenuPC(driver).clickSideMenuPLInsuranceScore();
			new GenericWorkorderInsuranceScore(driver).fillOutCreditReport(myPolicyObj);
			new SideMenuPC(driver).clickSideMenuPolicyInfo();
			new SideMenuPC(driver).clickSideMenuPLInsuranceScore();
			
			softAssert.assertTrue(!new GuidewireHelpers(driver).finds(By.xpath("//input[contains(@id, ':AddressLine1-inputEl')]")).isEmpty(), "FORMER ADDRESS FIELDS WERE NOT VISIBLE AFTER LEAVING THE SCREEN AND RETURNING.");
			softAssert.assertTrue(!new GuidewireHelpers(driver).finds(By.xpath("//input[contains(@id, ':AltFIrstName-inputEl')]")).isEmpty(), "FORMER NAME FIELDS WERE NOT VISIBLE AFTER LEAVING THE SCREEN AND RETURNING.");
			
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
			new AdminScriptParameters(driver).editScriptParameter(ScriptParameter.VeriskCreditScoreEnabled, false);
			new GuidewireHelpers(driver).logout();
			//End Script Parameter Setup
		}
	}	
}

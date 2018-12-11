package previousProgramIncrement.pi3_090518_111518.nonFeatures.Achievers;

import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.PolicyTermStatus;
import repository.gw.enums.ProductLineType;
import repository.gw.errorhandling.ErrorHandlingHelpers;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import persistence.globaldatarepo.entities.CSRs;
import persistence.globaldatarepo.entities.SAs;
import persistence.globaldatarepo.helpers.CSRsHelper;
import persistence.globaldatarepo.helpers.SAsHelper;

/**
* @Author jlarsen
* @Requirement 
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description VERIFY CSR AND SA CAN COMPLETE POLICY CHANGES ON A MEMBERS ONLY POLICY
* @DATE Sep 20, 2018
*/
public class US16063_CSR_SA_PA_IssueMOPChange extends BaseTest {

	
	GeneratePolicy myPolObj;
	WebDriver driver;
	
	
	@Test
	public void verifyCSRSAPACanIssueMOPChange() throws Exception {
		SoftAssert softassert = new SoftAssert();
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);


		myPolObj = new GeneratePolicy.Builder(driver)
				.withProductType(ProductLineType.Membership)
				.build(GeneratePolicyType.PolicyIssued);
		
		CSRs myCSR = CSRsHelper.getCSRByRegion(myPolObj.agentInfo.getAgentRegion().substring(0, 8));
		new Login(driver).loginAndSearchAccountByAccountNumber(myCSR.getCsruserName(), "gw", myPolObj.accountNumber);
		new AccountSummaryPC(driver).clickAccountSummaryPolicyTermByStatus(PolicyTermStatus.InForce);
		new StartPolicyChange(driver).startPolicyChange("CSR change", null);
		new GenericWorkorder(driver).clickGenericWorkorderQuote();
		long endTime = new Date().getTime() + 10000;
		do {
			if(new ErrorHandlingHelpers(driver).getErrorMessage().equals("Could not find this Policy in Billing System")) {
				new GuidewireHelpers(driver).editPolicyTransaction();
				new GenericWorkorder(driver).clickGenericWorkorderQuote();
			}
		} while(endTime > new Date().getTime() || !new ErrorHandlingHelpers(driver).areThereErrorMessages());
		if(new ErrorHandlingHelpers(driver).getErrorMessage().equals("Could not find this Policy in Billing System")) {
			Assert.fail("UNABLE TO PROPERLY ISSUE THE POLICY CHANGE DUE TO POLICY HAS NOT MADE IT TO BILLING CENTER IN TIME.");
		}
		new GenericWorkorder(driver).clickIssuePolicyButton();
		softassert.assertFalse(new GuidewireHelpers(driver).finds(By.xpath("//span[text()='Policy Change Submitted']")).isEmpty(), "CSR WAS UNABLE TO COMPLETE POLICY CHANGE ON MOP POLICY");
		new GuidewireHelpers(driver).logout();
		
		if(myPolObj.agentInfo.getAgentSA() != null) {
			SAs mySA = SAsHelper.getSAInfoByFullName(myPolObj.agentInfo.getAgentSA());
			new Login(driver).loginAndSearchAccountByAccountNumber(mySA.getSauserName(), "gw", myPolObj.accountNumber);
			new AccountSummaryPC(driver).clickAccountSummaryPolicyTermByStatus(PolicyTermStatus.InForce);
			new StartPolicyChange(driver).startPolicyChange("SA change", null);
			new GenericWorkorder(driver).clickGenericWorkorderQuote();
			new GenericWorkorder(driver).clickIssuePolicyButton();
			softassert.assertFalse(new GuidewireHelpers(driver).finds(By.xpath("//span[text()='Policy Change Submitted']")).isEmpty(), "CSR WAS UNABLE TO COMPLETE POLICY CHANGE ON MOP POLICY");
			new GuidewireHelpers(driver).logout();
		}
		
		softassert.assertAll();
	}
}























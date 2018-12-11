package previousProgramIncrement.pi3_090518_111518.f298_EPLI_BOP;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.helpers.ARUsersHelper;

public class verifyChargesToBC extends BaseTest {

	
	@Test(enabled = false)
	public void checkForNullBundle() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
		
		PolicyBusinessownersLine myBusinessownersLine = new PolicyBusinessownersLine();
		myBusinessownersLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_NumberOfFulltimeEmployees(50);
		
		GeneratePolicy myPolicyObj = new GeneratePolicy.Builder(driver)
				.withBusinessownersLine()
				.withPaymentPlanType(PaymentPlanType.Annual)
				.build(GeneratePolicyType.PolicyIssued);
		
		
		driver.get(cf.getUrlOfCenter(ApplicationOrCenter.BillingCenter));
		new Login(driver).loginAndSearchAccountByAccountNumber(ARUsersHelper.getRandomARUser().getUserName(), "gw", myPolicyObj.accountNumber);
		new BCAccountMenu(driver).clickBCMenuCharges();
		new AccountCharges(driver).getChargeAmount("", "");
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

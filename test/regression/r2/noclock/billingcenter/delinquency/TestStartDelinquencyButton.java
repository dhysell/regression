package regression.r2.noclock.billingcenter.delinquency;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.summary.BCAccountSummary;
import repository.bc.policy.summary.BCPolicySummary;
import repository.bc.search.BCSearchAccounts;
import repository.bc.search.BCSearchPolicies;
import repository.driverConfiguration.Config;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
/**
* @Author bhiltbrand
* @Description This test is used to ensure that the "Start Delinquency" Button has been removed from Billing
* Center, both on the Policy and Account screens.
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/17%20-%20User%20Interface/03%20-%20Account%20Summary%20Screen/03-01%20Start%20Delinquency%20Button.docx">For the Accounts Page</a>
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/17%20-%20User%20Interface/04%20-%20Policy%20Summary%20Screen/04-01%20Exit%20Delinquency%20Button/04-01%20Start%20Delinquency%20Button.docx">For the Policy Page</a>
* @DATE Sep 23, 2015
*/
public class TestStartDelinquencyButton extends BaseTest {
	private WebDriver driver;
	String accountNumber = null;
	
	@Test
	public void verifyButtonDoesNotExistOnAccountScreen() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		ARUsers arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());

		BCSearchAccounts search = new BCSearchAccounts(driver);
		this.accountNumber = search.findDelinquentAccount();

		BCAccountSummary accountSummary = new BCAccountSummary(driver);
		
		boolean passed = false;
		try {
			accountSummary.clickAccountSummaryStartDelinquency();
		} catch (Exception e) {
			System.out.println("The 'Start Delinquency' Button was not found on the page. This is expected. Test Passed");
			passed = true;
		}
		
		if (passed == false) {
			Assert.fail("The 'Start Delinquency' Button was available and was clicked. This should not be the case. Test Failed.");	
		}
	}
	
	@Test (dependsOnMethods = { "verifyButtonDoesNotExistOnAccountScreen" })
	public void verifyButtonDoesNotExistOnPolicyScreen() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		ARUsers arUser = ARUsersHelper.getRandomARUserByCompany(ARCompany.Commercial);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());

		BCSearchPolicies search = new BCSearchPolicies(driver);
		search.searchPolicyByAccountNumber(this.accountNumber);

        BCPolicySummary PolicySummary = new BCPolicySummary(driver);
		boolean passed = false;
		try {
			PolicySummary.clickStartDeliquency();
		} catch (Exception e) {
			System.out.println("The 'Start Delinquency' Button was not found on the page. This is expected. Test Passed");
			passed = true;
		}
		
		if (passed == false) {
			Assert.fail("The 'Start Delinquency' Button was available and was clicked. This should not be the case. Test Failed.");
		}
	}
}

package regression.r2.noclock.billingcenter.payments;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.actions.DesktopActionsMultiplePayment;
import repository.bc.search.BCSearchPolicies;
import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.Config;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author bhiltbrand
* @Requirement This test verifies a couple different issues on the Multiple Payment Entry screen. Specifically, it tests that the correct errors
* show up when required.
* @RequirementsLink <a href="https://rally1.rallydev.com/#/41432362343d/detail/defect/70165594952">Rally Defect DE4162</a>
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/17%20-%20UI%20-%20Desktop%20Screens/17-01%20Multiple%20Payment%20Entry%20Wizard.docx">Multiple Payment Entry Wizard Requirements</a>
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/17%20-%20UI%20-%20Desktop%20Screens/17-02%20County%20Payment%20Entry%20Wizard.docx">county Payment Entry Wizard Requirements</a>
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/17%20-%20UI%20-%20Desktop%20Screens/17-03%20Electronic%20Payment%20Entry%20Wizard.docx">Electronic Multiple Payment Entry Wizard Requirements</a>
* @Description 
* @DATE Feb 20, 2017
*/
public class TestDesktopMultiplePaymentScreen extends BaseTest {
	private WebDriver driver;
	private ARUsers arUser;
	private String policyNumber = null;
	
	@Test(enabled=true)
	public void verifyPolicyLoanBalancesPage() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(this.arUser.getUserName(), this.arUser.getPassword());

		BCSearchPolicies policySearch = new BCSearchPolicies(driver);
		this.policyNumber = policySearch.findPolicyInGoodStanding("259", null, null);
		
		BCTopMenu topMenu = new BCTopMenu(driver);
		topMenu.clickDesktopTab();


		BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuActionsMultiplePayment();


		DesktopActionsMultiplePayment multiplePaymentsPage = new DesktopActionsMultiplePayment(driver);
		int newestRow = multiplePaymentsPage.fillOutNextLineOnMultiPaymentTable(null, null, 15.00);
		multiplePaymentsPage.clickNext();
		

		String errorMessage = null;
		if (new GuidewireHelpers(driver).errorMessagesExist()) {
			errorMessage = new GuidewireHelpers(driver).getFirstErrorMessage();	
			if (!(errorMessage.equals("Account # : Must specify a target for payment: Account #, Policy #, Invoice, or Producer.") || errorMessage.equals("Policy # : Must enter a Policy"))) {
				Assert.fail("The error message displayed was not the message expected for not filling out the policy number. The message displayed was \"" + errorMessage + "\". Test failed.");
			}
		} else {
			Assert.fail("The expected error banner never appeared after not filling out all required information and clicking the next button. Test failed.");
		}


		multiplePaymentsPage.setMultiPaymentPolicyNumber(newestRow, this.policyNumber);
		multiplePaymentsPage.clickNext();
		if (new GuidewireHelpers(driver).errorMessagesExist()) {
			errorMessage = new GuidewireHelpers(driver).getFirstErrorMessage();
			if (errorMessage.contains("Did not find policy # matching " + this.policyNumber + ". Please use the policy picker to select the policy")) {
				Assert.fail("There was an error complaining about the policy number not being found after manually entering it. This should not happen and is a failure related to DE4162. Test Failed.");
			}
			if (!errorMessage.contains("does not have an invoice stream. This payment will be applied to the account.")) {
				Assert.fail("There was an unhandled error. The error was \"" + errorMessage + "\". Test Failed.");
			}
		}
		multiplePaymentsPage.clickFinish();
		new GuidewireHelpers(driver).logout();
	}
}

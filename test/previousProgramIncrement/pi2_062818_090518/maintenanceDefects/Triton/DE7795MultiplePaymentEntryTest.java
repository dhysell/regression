package previousProgramIncrement.pi2_062818_090518.maintenanceDefects.Triton;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.payments.AccountPayments;
import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.actions.DesktopActionsMultiplePayment;
import repository.bc.search.BCSearchAccounts;
import repository.bc.search.BCSearchPolicies;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PolicySearchPolicyProductType;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author JQU
* @Requirement 	DE7795 - ** Hot fix ** NULL pointer on multiple payment screen
* 							
* 				
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/_layouts/15/WopiFrame.aspx?sourcedoc=/billingcenter/Documents/Release%202%20Requirements/17%20-%20UI%20-%20Desktop%20Screens/17-01%20Multiple%20Payment%20Entry%20Wizard.docx&action=default">17-01 Multiple Payment Entry Wizard</a>
* @DATE August 15, 2018
*/
public class DE7795MultiplePaymentEntryTest extends BaseTest {
	private WebDriver driver;
	private String policyNumber = null;
	private double paymentAmount=NumberUtils.generateRandomNumberInt(20, 200);
	private ARUsers arUser = new ARUsers();
	
	@Test
	public void findRandomPolicynumber() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login("su","gw");
		BCSearchPolicies policySearch = new BCSearchPolicies(driver);
		this.policyNumber = policySearch.findPolicyInGoodStanding("245", null, PolicySearchPolicyProductType.Business_Owners);
		System.out.println(policyNumber);
	}
	@Test(dependsOnMethods = { "findRandomPolicynumber" })	
	public void makeMultiplePaymentAndVerify() throws Exception {		
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());
		BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuActionsMultiplePayment();
		DesktopActionsMultiplePayment multiplePaymentsPage = new DesktopActionsMultiplePayment(driver);
		multiplePaymentsPage.makeMultiplePayment(policyNumber, PaymentInstrumentEnum.Cash, paymentAmount);
		//verify that the payment is completed
		BCSearchAccounts search = new BCSearchAccounts(driver);
		search.searchAccountByAccountNumber(policyNumber.substring(3, 9));	
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuPayments();
		AccountPayments payment = new AccountPayments(driver);
		Assert.assertTrue(payment.verifyPaymentAndClick(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, null, null, null, null, null, null, null, policyNumber, paymentAmount, null, null), "Didn't find the payment made from Multiple Payment Entry");
	}
}

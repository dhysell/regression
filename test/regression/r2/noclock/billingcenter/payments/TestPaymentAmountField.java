package regression.r2.noclock.billingcenter.payments;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.search.BCSearchAccounts;
import repository.bc.search.BCSearchInvoices;
import repository.driverConfiguration.Config;
import repository.gw.enums.InvoiceStatus;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author bhiltbrand
* @Requirement The Amount field must not be editable in modify distribution functionality, but must be editable when making a payment from the direct bill payment screen.
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Requirements/Payments/Payments%20�%20Modify%20Distribution.docx">Requirements Documentation</a>
* @RequirementsLink <a href="https://rally1.rallydev.com/#/7832667974d/detail/defect/49074056173">Rally Story</a>
* @Description 
* @DATE Jan 6, 2016
*/
public class TestPaymentAmountField extends BaseTest {
	private WebDriver driver;
	private ARUsers arUser = new ARUsers();
	private double paymentAmount = Double.valueOf((NumberUtils.generateRandomNumberDigits(2) + "." + NumberUtils.generateRandomNumberDigits(2)));
	
	@Test
    public void verifyAmountFieldEditablity() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(this.arUser.getUserName(), this.arUser.getPassword());

		BCSearchInvoices invoiceSearch = new BCSearchInvoices(driver);
		String accountNumber = invoiceSearch.searchForAccountByInvoiceAndAmountRange("246", 150, 500, InvoiceStatus.Due);

		BCSearchAccounts accountSearch = new BCSearchAccounts(driver);
		accountSearch.searchAccountByAccountNumber(accountNumber);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();

        NewDirectBillPayment directBillPayment = new NewDirectBillPayment(driver);
		
		try {
			directBillPayment.makeDirectBillPaymentExecuteWithoutDistribution(this.paymentAmount);
		} catch (Exception e) {
			Assert.fail("The Amount Field was not editable when it should have been. Test Failed.");
		}
		

        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickAccountMenuPayments();


		AccountPayments payments = new AccountPayments(driver);
		payments.clickActionsModifyDistributionByAmount(this.paymentAmount, "Amount");

        directBillPayment = new NewDirectBillPayment(driver);
		
		try {
			directBillPayment.makeDirectBillPaymentExecuteWithoutDistribution(this.paymentAmount + 5);
			Assert.fail("The Amount Field was editable when it should not have been. Test Failed.");
		} catch (Exception e) {
			getQALogger().info("The Amount Field was not editable. This is expected.");
		}
	}
}

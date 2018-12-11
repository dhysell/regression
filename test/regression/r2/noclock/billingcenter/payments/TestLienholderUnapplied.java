package regression.r2.noclock.billingcenter.payments;

import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.common.BCCommonSummary;
import repository.bc.search.BCSearchInvoices;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.InvoiceStatus;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author bhiltbrand
* @Requirement The requirement is that Unapplied funds on a lienholder should not be sucked up automatically to pay invoices.
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description This test goes to a lienholder account that contains invoices billed with amounts still due. It then makes a payment to unapplied
* funds and runs the Automatic Disbursement batch process. It finally checks that the amount in unapplied funds did not change, thus showing that
* funds were not sucked up inadvertently.
* @DATE Sep 24, 2015
*/
public class TestLienholderUnapplied extends BaseTest {
	private WebDriver driver;
	public String lienholderAccountNumber = null;
	public double originalUnappliedFundsAmount = 0;
	public double paymentAmount = randomPaymentAmount();
	public ARUsers arUser = new ARUsers();
	
	private double randomPaymentAmount () {
		String startingRandomAmount = String.valueOf(NumberUtils.generateRandomNumberDigits(4));
		double formattedAmount = Double.valueOf(startingRandomAmount.substring(0, 2) + "." + startingRandomAmount.substring(2, startingRandomAmount.length()));
		return formattedAmount;
	}

	@Test
    public void findLienholderToUse() throws Exception {
		arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());

		BCSearchInvoices invoiceSearch = new BCSearchInvoices(driver);
		Date currentDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
		this.lienholderAccountNumber = invoiceSearch.searchForAccountByAmountRangeAndInvoiceAndDateRange(DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, -45), DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 1), "98", 500, 3000, InvoiceStatus.Due);

	}
	
	@Test (dependsOnMethods = { "findLienholderToUse" })
	public void verifyLienholderAccount() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienholderAccountNumber);

        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickAccountMenuInvoices();

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        AccountInvoices invoices = new AccountInvoices(driver);
		WebElement invoicesTable = invoices.getAccountInvoicesTable();
		String statusGridColumnID = new TableUtils(driver).getGridColumnFromTable(invoicesTable, "Status");
		String dueGridColumnID = new TableUtils(driver).getGridColumnFromTable(invoicesTable, "Due");
		List<WebElement> rowCount = invoicesTable.findElements(By.xpath(".//tr/td[contains(@class,'" + statusGridColumnID + "') and contains(.,'Due')]/parent::tr/td[contains(@class,'" + dueGridColumnID + "') and not (contains(.,'-'))]"));
		if (rowCount.size() < 1) {		
			throw new SkipException("The Lienholder used (Account # " + this.lienholderAccountNumber + ") did not have any invoices with amounts still due. The test cannot continue.");
		}

        menu = new BCAccountMenu(driver);
		menu.clickBCMenuSummary();

		BCCommonSummary summary = new BCCommonSummary(driver);
		this.originalUnappliedFundsAmount = summary.getDefaultUnappliedFundsAmount();
	}
	
	@Test (dependsOnMethods = { "verifyLienholderAccount" })
	public void makePaymentAndVerifyDistribution() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienholderAccountNumber);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();

        NewDirectBillPayment payment = new NewDirectBillPayment(driver);
		payment.makeDirectBillPaymentExecuteWithoutDistribution(this.paymentAmount);
		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Automatic_Disbursement);
		
		driver.navigate().refresh();

		BCCommonSummary summary = new BCCommonSummary(driver);
		double newUnappliedFundsAmount = summary.getDefaultUnappliedFundsAmount();
		
		Assert.assertEquals(newUnappliedFundsAmount, (NumberUtils.round((this.originalUnappliedFundsAmount + this.paymentAmount), 2)), "The amount left in unapplied funds after creating a payment and running batch processes was not was expected. Test Failed.");
	}
	
	@Test (dependsOnMethods = { "makePaymentAndVerifyDistribution" })
	public void cleanUpLienholderAccount() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienholderAccountNumber);

        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickAccountMenuPayments();

		AccountPayments payments = new AccountPayments(driver);
		payments.reversePaymentByAmount(this.paymentAmount);
	}
}

package coreBusiness.delinquencies.renewaltermornottakeninsuredfullcancel;


import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonCharges;
import repository.bc.common.BCCommonDelinquencies;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.TransactionNumber;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GeneratePolicyHelper;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

import java.util.Date;

/**
 * @Author sgunda
 * @Requirement US14909 - Insurance policy w/ Membership line Delinquency - Insured - Renewal (Automate)
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558469364d/detail/userstory/213781909576">Link Text<US14909/a>
 */

public class TestDelinquenciesForCarryOverAfterNotTakenCancellation extends BaseTest {

	private ARUsers arUser;
	private BCAccountMenu acctMenu;
	private BCCommonDelinquencies delinquency;
	private GeneratePolicy myPolicyObj;
	private WebDriver driver;

	@Test
	public void SquirePolicy() throws Exception {


		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		myPolicyObj = new GeneratePolicyHelper(driver).generatePLSectionIAndIIPropertyAndLiabilityLinePLPolicy("InsuredThres", "NTaken",100, repository.gw.enums.PaymentPlanType.Annual, repository.gw.enums.PaymentType.Cash);

		Assert.assertNotNull(myPolicyObj,"Generate failed, please investigate from logs");

	}

	@Test(dependsOnMethods = {"SquirePolicy"})
	public void makeDownPaymentAndMoveClockToRenew() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);

		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);
		NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
		newPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.squire.getPremium().getDownPaymentAmount(), myPolicyObj.squire.getPolicyNumber());	

		repository.gw.helpers.ClockUtils.setCurrentDates(driver, repository.gw.enums.DateAddSubtractOptions.Day, 1);
		new BatchHelpers(driver).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);

		repository.gw.helpers.ClockUtils.setCurrentDates(driver, repository.gw.helpers.DateUtils.dateAddSubtract(myPolicyObj.squire.getExpirationDate(), repository.gw.enums.DateAddSubtractOptions.Day, -50));
		new GuidewireHelpers(driver).logout();
	}


	@Test(dependsOnMethods = {"makeDownPaymentAndMoveClockToRenew"})
	public void renewlPolicyInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new StartRenewal(driver).loginAsUWAndIssueRenewal(myPolicyObj);
	}

	@Test(dependsOnMethods = {"renewlPolicyInPolicyCenter"})
	public void CancelPolicyByTriggeringNTF() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();

		BCCommonCharges charges = new BCCommonCharges(driver); 
		charges.waitUntilChargesFromPolicyContextArrive(repository.gw.enums.TransactionType.Renewal);

		repository.gw.helpers.ClockUtils.setCurrentDates(driver, repository.gw.helpers.DateUtils.dateAddSubtract(myPolicyObj.squire.getExpirationDate(), repository.gw.enums.DateAddSubtractOptions.Day, -15));
		new BatchHelpers(driver).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);

		repository.gw.helpers.ClockUtils.setCurrentDates(driver, myPolicyObj.squire.getExpirationDate());
		new BatchHelpers(driver).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);
		acctMenu.clickBCMenuDelinquencies();
		delinquency = new BCCommonDelinquencies(driver);
		Assert.assertTrue(delinquency.verifyDelinquencyByReason(null, repository.gw.enums.DelinquencyReason.NotTaken,null, myPolicyObj.squire.getExpirationDate()), "Not Taken did not trigger, on Renewal non-pay");
		new GuidewireHelpers(driver).logout();

		new BatchHelpers(driver).runBatchProcess(repository.gw.enums.BatchProcess.PC_Workflow);

		cf.setCenter(ApplicationOrCenter.BillingCenter);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
		charges = new BCCommonCharges(driver); 
		charges.waitUntilChargesFromPolicyContextArrive(240, repository.gw.enums.TransactionType.Cancellation);
		acctMenu.clickBCMenuDelinquencies();
		delinquency = new BCCommonDelinquencies(driver);
		Assert.assertTrue(delinquency.verifyDelinquencyByReason(repository.gw.enums.OpenClosed.Closed, repository.gw.enums.DelinquencyReason.NotTaken,null, null), "Not Taken did not close after cancellation");

		repository.gw.helpers.ClockUtils.setCurrentDates(driver, repository.gw.enums.DateAddSubtractOptions.Day, 5);
		new BatchHelpers(driver).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);
		new BatchHelpers(driver).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);

	}

	@Test(dependsOnMethods = {"CancelPolicyByTriggeringNTF"})
	public void AddCarryOverAndMakeItDelinquent() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();

		BCCommonCharges charges = new BCCommonCharges(driver); 
		double carryOverAmount = 300;
		charges.clickCarryOverChargeAndCreateChargeAndVerifyIfCreatedOrNot(myPolicyObj.accountNumber, TransactionNumber.Premium_Charged, repository.gw.enums.TransactionType.Renewal, myPolicyObj.squire.getPolicyNumber(), repository.gw.enums.TrueFalse.True, carryOverAmount);

		acctMenu.clickAccountMenuInvoices();
		AccountInvoices invoice = new AccountInvoices(driver);
		Date invoiceDate = invoice.invoiceDate(null, null, null, null, repository.gw.enums.InvoiceStatus.Planned, carryOverAmount, carryOverAmount);
		Date dueDate = invoice.dueDate(null, null, null, null, repository.gw.enums.InvoiceStatus.Planned, carryOverAmount, carryOverAmount);

		repository.gw.helpers.ClockUtils.setCurrentDates(driver, invoiceDate);
		new BatchHelpers(driver).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);
		repository.gw.helpers.ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(dueDate, repository.gw.enums.DateAddSubtractOptions.Day, 1));
		new BatchHelpers(driver).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);

		acctMenu.clickBCMenuDelinquencies();
		delinquency = new BCCommonDelinquencies(driver);
		Assert.assertTrue(delinquency.verifyDelinquencyByReason(repository.gw.enums.OpenClosed.Open, repository.gw.enums.DelinquencyReason.PastDueFullCancel,null, null), "Modified Past Due Full Cancel has not triggerd on CarryOver");

	}
}

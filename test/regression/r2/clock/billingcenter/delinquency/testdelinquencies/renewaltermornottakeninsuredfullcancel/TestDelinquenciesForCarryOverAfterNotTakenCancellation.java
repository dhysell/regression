package regression.r2.clock.billingcenter.delinquency.testdelinquencies.renewaltermornottakeninsuredfullcancel;


import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonCharges;
import repository.bc.common.BCCommonDelinquencies;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.RenewalCode;
import repository.gw.enums.TransactionNumber;
import repository.gw.enums.TransactionType;
import repository.gw.enums.TrueFalse;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GeneratePolicyHelper;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

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
		myPolicyObj = new GeneratePolicyHelper(driver).generatePLSectionIAndIIPropertyAndLiabilityLinePLPolicy("InsuredThres", "NTaken",100,PaymentPlanType.Annual,PaymentType.Cash);
	}

	@Test(dependsOnMethods = {"SquirePolicy"})
	public void makeDownPaymentAndMoveClockToRenew() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);

		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);
		NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
		newPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.squire.getPremium().getDownPaymentAmount(), myPolicyObj.squire.getPolicyNumber());	

		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);

		ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(myPolicyObj.squire.getExpirationDate(), DateAddSubtractOptions.Day, -50));
		new GuidewireHelpers(driver).logout();
	}


	@Test(dependsOnMethods = {"makeDownPaymentAndMoveClockToRenew"})
	public void renewlPolicyInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.squire.getPolicyNumber());

		PolicyMenu policyMenu = new PolicyMenu(driver);
		policyMenu.clickRenewPolicy();
		new GuidewireHelpers(driver).logout();
		new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.squire.getPolicyNumber());
		PolicySummary summaryPage = new PolicySummary(driver);
		if (summaryPage.checkIfActivityExists("Pre-Renewal Review")) {
			summaryPage.clickViewPreRenewalDirection();
			PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);

			boolean preRenewalDirectionExists = preRenewalPage.checkPreRenewalExists();
			if (preRenewalDirectionExists) {
				preRenewalPage.clickViewInPreRenewalDirection();
				preRenewalPage.closeAllPreRenewalDirectionExplanations();
				preRenewalPage.clickClosePreRenewalDirection();
				preRenewalPage.clickReturnToSummaryPage();
			}
		}
		StartRenewal renewal = new StartRenewal(driver);
		renewal.quoteAndIssueRenewal(RenewalCode.Renew_Good_Risk, myPolicyObj);

		ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(myPolicyObj.squire.getExpirationDate(), DateAddSubtractOptions.Day, -35));
		new GuidewireHelpers(driver).logout();

	}

	@Test(dependsOnMethods = {"renewlPolicyInPolicyCenter"})
	public void CancelPolicyByTriggeringNTF() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();

		BCCommonCharges charges = new BCCommonCharges(driver); 
		charges.waitUntilChargesFromPolicyContextArrive(TransactionType.Renewal);

		ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(myPolicyObj.squire.getExpirationDate(), DateAddSubtractOptions.Day, -15));
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);

		ClockUtils.setCurrentDates(driver, myPolicyObj.squire.getExpirationDate());
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);
		acctMenu.clickBCMenuDelinquencies();
		delinquency = new BCCommonDelinquencies(driver);
		Assert.assertTrue(delinquency.verifyDelinquencyByReason(null, DelinquencyReason.NotTaken,null, myPolicyObj.squire.getExpirationDate()), "Not Taken did not trigger, on Renewal non-pay");
		new GuidewireHelpers(driver).logout();

		new BatchHelpers(driver).runBatchProcess(BatchProcess.PC_Workflow);

		cf.setCenter(ApplicationOrCenter.BillingCenter);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
		charges = new BCCommonCharges(driver); 
		charges.waitUntilChargesFromPolicyContextArrive(240, TransactionType.Cancellation);
		acctMenu.clickBCMenuDelinquencies();
		delinquency = new BCCommonDelinquencies(driver);
		Assert.assertTrue(delinquency.verifyDelinquencyByReason(OpenClosed.Closed, DelinquencyReason.NotTaken, null,null), "Not Taken did not close after cancellation");

		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 5);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);

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
		charges.clickCarryOverChargeAndCreateChargeAndVerifyIfCreatedOrNot(myPolicyObj.accountNumber, TransactionNumber.Premium_Charged, TransactionType.Renewal, myPolicyObj.squire.getPolicyNumber(), TrueFalse.True, carryOverAmount);

		acctMenu.clickAccountMenuInvoices();
		AccountInvoices invoice = new AccountInvoices(driver);
		Date invoiceDate = invoice.invoiceDate(null, null, null, null, InvoiceStatus.Planned, carryOverAmount, carryOverAmount);
		Date dueDate = invoice.dueDate(null, null, null, null, InvoiceStatus.Planned, carryOverAmount, carryOverAmount);

		ClockUtils.setCurrentDates(driver, invoiceDate);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
		ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(dueDate, DateAddSubtractOptions.Day, 1));
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);

		acctMenu.clickBCMenuDelinquencies();
		delinquency = new BCCommonDelinquencies(driver);
		Assert.assertTrue(delinquency.verifyDelinquencyByReason(OpenClosed.Open, DelinquencyReason.PastDueFullCancel,null, null), "Modified Past Due Full Cancel has not triggerd on CarryOver");

	}
}

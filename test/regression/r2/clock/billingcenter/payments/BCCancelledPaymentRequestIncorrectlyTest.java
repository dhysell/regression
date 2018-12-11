package regression.r2.clock.billingcenter.payments;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPaymentsPaymentRequests;
import repository.bc.account.summary.BCAccountSummary;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.Status;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.BankAccountInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author JQU
* @Requirement 		DE7317: **Hot-Fix** BC cancelled payment request incorrectly
* 					Steps: Create a monthly payment plan policy
					Make down invoice billed
					Now make a payment 					
					move clock one day
					run invoice due batch
					Do a flat cancel
					we will have a credit because of cancellation
					wait for 30 days
					run both invoice and invoice due batches
					then do a rewrite policy to new term
					Now go to rewrite down or next planned invoice make it billed and due.
					This invoice gets paid by cancellation credit
					Now make payment which is less than installment invoice amount and it directly goes to unapplied fund
					When next invoice gets billed, the unapplied fund goes into negative, and the payment request is incorrectly cancelled.
* 
* 
* @DATE Mar 26, 2018
*/
public class BCCancelledPaymentRequestIncorrectlyTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;
	private ARUsers arUser = new ARUsers();
	
	@Test
    public void generatePolicy() throws Exception {
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		PolicyLocationBuilding building1=new PolicyLocationBuilding();	
		locOneBuildingList.add(building1);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));		
		BankAccountInfo bankAccountInfo = new BankAccountInfo();	

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        myPolicyObj = new GeneratePolicy.Builder(driver)
			.withInsPersonOrCompany(ContactSubType.Company)
			.withInsCompanyName("PaymentRequestTest")
			.withPolOrgType(OrganizationType.Partnership)
			.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
			.withPolicyLocations(locationsList)
			.withBankAccountInfo(bankAccountInfo)
			.withPaymentPlanType(PaymentPlanType.Monthly)
			.withDownPaymentType(PaymentType.Cash)
			.build(GeneratePolicyType.PolicyIssued);		
	}
	@Test(dependsOnMethods = { "generatePolicy" })
	public void payDownPayment() throws Exception{
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directPmt = new NewDirectBillPayment(driver);
		directPmt.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPremium().getTotalGrossPremium(), myPolicyObj.accountNumber);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);		
	}
	@Test(dependsOnMethods = { "payDownPayment" })
    public void flatCancelPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.busOwnLine.getPolicyNumber());
        StartCancellation cancelPol = new StartCancellation(driver);
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.OtherPolicyRewrittenOrReplaced, "Flat Cancellation", myPolicyObj.busOwnLine.getEffectiveDate(),true);
	}
	@Test(dependsOnMethods = { "flatCancelPolicy" })
    public void verifyCancellation() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();
        AccountCharges charges = new AccountCharges(driver);
		charges.waitUntilChargesFromPolicyContextArrive(60, TransactionType.Cancellation);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 29);
	}
	@Test(dependsOnMethods = { "verifyCancellation" })
    public void rewriteFullTerm() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.accountNumber);
        StartRewrite rewrite = new StartRewrite(driver);
		rewrite.rewriteFullTerm(myPolicyObj);		
	}	
	@Test(dependsOnMethods = { "rewriteFullTerm" })
	public void verifyPaymentRequest() throws Exception{
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();
        AccountCharges charges = new AccountCharges(driver);
		charges.waitUntilChargesFromPolicyContextArrive(60, TransactionType.Rewrite);
		accountMenu.clickAccountMenuInvoices();
        AccountInvoices invoices = new AccountInvoices(driver);
		//get the first installment amount after rewrite
		Date firstDueDateAfterRewrite = DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getEffectiveDate(), DateAddSubtractOptions.Month, 2);	
		Date firstInvoiceDateAfterRewrite = DateUtils.dateAddSubtract(firstDueDateAfterRewrite, DateAddSubtractOptions.Day, -15);		
		double amount = invoices.getInvoiceAmountByInvoiceDate(firstInvoiceDateAfterRewrite);
		//flat cancellation credit will pay part of rewrite down payment
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		
		double unpaidRewriteDown = invoices.getInvoiceAmountByInvoiceDate(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter));
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directPay = new NewDirectBillPayment(driver);
		directPay.makeDirectBillPaymentExecute(unpaidRewriteDown, myPolicyObj.accountNumber);
		
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		//make extra payment which is less than installment invoice amount
		double payment = NumberUtils.generateRandomNumberInt(10, 50);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
		directPayment.makeDirectBillPaymentExecuteWithoutDistribution(payment, myPolicyObj.accountNumber);
		//make the first installment after rewrite Billed
		ClockUtils.setCurrentDates(cf, firstInvoiceDateAfterRewrite);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		//verify unapplied fund and payment request
		accountMenu.clickAccountMenuPaymentsPaymentRequests();
		AccountPaymentsPaymentRequests request = new AccountPaymentsPaymentRequests(driver);		
		
		if(!request.verifyPaymentRequest(Status.Created, firstInvoiceDateAfterRewrite, firstInvoiceDateAfterRewrite, null, firstDueDateAfterRewrite, firstDueDateAfterRewrite, null, myPolicyObj.busOwnLine.getPolicyNumber(), null, amount-payment)){
			Assert.fail("Incorrect payment request");
        }

        accountMenu.clickBCMenuSummary();
        BCAccountSummary summary = new BCAccountSummary(driver);
		try{
			summary.getUnappliedFundByPolicyNumber(myPolicyObj.accountNumber);
			Assert.fail("There should not be any amount in unapplied fund.");
		}catch (Exception e) {
			getQALogger().info("It's correct, there should not be any amount in unapplied fund.");
		}
		
	}
}


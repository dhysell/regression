package regression.r2.clock.billingcenter.payments;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.payments.AccountPaymentsPaymentRequests;
import repository.bc.common.BCCommonDocuments;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
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
import repository.pc.workorders.change.StartPolicyChange;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
/**
* @Author JQU
* @ Description: DE6787 -- Payment request not recognizing outstanding payment request
* 				Steps: Create a completely insured billed policy 
				Move clock 9 days bind and issue policy with a monthly payment plan and ACH down payment   
				Move the clock 7 day run invoice and invoice due (First installment goes billed, payment request is set up and MNOW is sent)
				Move clock 2 days, lengthen policy 5 days in PC
				Second invoice is set up with a due date 5 days after the first installment
				Move clock 5 days run invoice and invoice due (2nd installment goes billed, payment request is set up, No MNOW is sent-another defect)
				Actual: The first payment request matches the amount on the MNOW that we sent out and the invoice amount.  The second payment request had both the amount of the first and the second invoice and we drafted both amounts thus over drafting the customer's account.
				Expected: If there is an payment request and the system creates another payment request before the first payment request draft date it should see the other pending draft and only try to draft for the difference. 
* 
* @DATE November 10, 2017
* */
public class PaymentRequestNotRecognizingOutstandingPaymentRequest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;
	private ARUsers arUser = new ARUsers();	
	private List<Date> invoiceDateList = new ArrayList<Date>();

    private void verifyPaymentRequestAndDocument(BCAccountMenu acctMenu, Date installmentDueDate, double amount){
		acctMenu.clickAccountMenuPaymentsPaymentRequests();
		AccountPaymentsPaymentRequests request = new AccountPaymentsPaymentRequests(driver);		
		request.verifyPaymentRequest(Status.Created, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), installmentDueDate, installmentDueDate, installmentDueDate, null, null, null, amount);
		
		acctMenu.clickBCMenuDocuments();
        BCCommonDocuments doc = new BCCommonDocuments(driver);
		doc.verifyDocument("Monthly Notice of Withdrawal", null, null, null, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), null);
	}
	@Test
	public void generateStandardFireFullApp() throws Exception {		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		locOneBuildingList.add(new PolicyLocationBuilding());
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));		
		BankAccountInfo bankAccountInfo = new BankAccountInfo();
		bankAccountInfo.setAccountNumber("1234567");
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        myPolicyObj = new GeneratePolicy.Builder(driver)
			.withInsPersonOrCompany(ContactSubType.Company)
			.withInsCompanyName("Payment Request")
			.withPolOrgType(OrganizationType.Partnership)
			.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
			.withPolicyLocations(locationsList)
			.withBankAccountInfo(bankAccountInfo)
			.withPaymentPlanType(PaymentPlanType.Monthly)
			.withDownPaymentType(PaymentType.ACH_EFT)
			.build(GeneratePolicyType.FullApp);			
	}
	@Test(dependsOnMethods = { "generateStandardFireFullApp" })
	public void bindPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		//move 9 days
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 9);		
		//bind the policy
		myPolicyObj.policySubmittedGuts();		
	}
	@Test(dependsOnMethods = { "bindPolicy" })
	public void issuePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		//issue the policy
		myPolicyObj.policyIssuedGuts();
	}	
	@Test(dependsOnMethods = { "issuePolicy" })
	public void triggerFirstPaymentRequest() throws Exception {				
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
        AccountInvoices invoice = new AccountInvoices(driver);
		double firstInstallmentAmount = invoice.getInvoiceAmountByRowNumber(2);
		invoiceDateList = invoice.getListOfInvoiceDates();
		
		//pay down payment -- ACH payment, after running Invoice, Down will be paid by ACH		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);		
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		
		//trigger first Payment Request
		ClockUtils.setCurrentDates(cf, invoiceDateList.get(1));
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		
		//verify the payment request and document	
		Date firstInstallmentDueDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, -myPolicyObj.paymentPlanType.getInvoicingLeadTime());
		verifyPaymentRequestAndDocument(acctMenu, firstInstallmentDueDate, firstInstallmentAmount);
		
		//move 2 days, then extend policy term
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 2);

	}
	@Test(dependsOnMethods = { "triggerFirstPaymentRequest" })
    public void extendTermBy5Days() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        StartPolicyChange policyChange = new StartPolicyChange(driver);
        policyChange.changeExpirationDate(DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getExpirationDate(), DateAddSubtractOptions.Day, 5), "extend term by 5 days");

	}
	@Test(dependsOnMethods = { "extendTermBy5Days" })
    public void triggerSecondPaymentRequestAndVerify() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
        AccountCharges charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPayer(60, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), TransactionType.Policy_Change, myPolicyObj.accountNumber);
		double newCharge = NumberUtils.getCurrencyValueFromElement(charge.getChargesOrChargeHoldsPopupTableCellValue("Amount", DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, null, null, TransactionType.Policy_Change, null, null, null, null, null, null, null, null, null, null, null));
		
		//move to the invoice date of the new charge
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 5);
		
		//trigger the second Payment Request		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		//verify the payment request and document	
		Date secondInstallmentDueDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, -myPolicyObj.paymentPlanType.getInvoicingLeadTime());
		verifyPaymentRequestAndDocument(acctMenu, secondInstallmentDueDate, newCharge);

    }
}

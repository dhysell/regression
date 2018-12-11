package regression.r2.clock.billingcenter.delinquency;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.account.payments.AccountPaymentsPaymentRequests;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.policy.summary.BCPolicySummary;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentReversalReason;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author jqu
 * @Requirement DE8037 Payment Request Screen - Add Bank Info Column
 * @Requirement: Payment requests must display the associated bank information on the user interface in the �Payment Instrument� column,
 * formatted as the bank account type followed by the last 4 digits of the bank account number (in parenthesis) followed by the bank name.
 * �	Ex: Per Chk (7763) US Bank Na
 * �	Note: The �Payment Instrument� column should be placed to the right of the �Policy Number� column and to the left of the �Amount� column.
 * @Requirement DE4035 Delinquency: Delinquency amount also includes billed amount when delinquency triggered at policy level
 * @Steps: Steps to reproduce:
 * 1. Generate a monthly policy
 * 2. Payoff down payment
 * 3. Move clock to second Invoice Date and make it billed
 * 4. Move clock to pass Due date of second invoice without payment
 * 5. Reverse the payment for Down payment. Verify the delinquency doesn't include the billed amount.
 * @DATE October 12, 2016
 */
@QuarantineClass
public class DelinquencyAmountAndPaymentRequestInstrumentTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;	
	private ARUsers arUser = new ARUsers();
	private BCAccountMenu acctMenu;	
	private double secondInv;
	
	@Test
	public void generate() throws Exception {
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		PolicyLocationBuilding building1=new PolicyLocationBuilding();			
		locOneBuildingList.add(building1);	
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
        this.myPolicyObj = new GeneratePolicy.Builder(driver).withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsCompanyName("DelinquencyAmountAndPaymentRequest")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Monthly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);		
	}
	@Test(dependsOnMethods = { "generate" })	
	public void paymentAndReverseAndVerify() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		//wait for Trouble Ticket to come
        new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.busOwnLine.getPolicyNumber());

        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
		policyMenu.clickBCMenuTroubleTickets();
        BCCommonTroubleTickets tt = new BCCommonTroubleTickets(driver);
		tt.waitForTroubleTicketsToArrive(60);
		policyMenu.clickTopInfoBarAccountNumber();
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuInvoices();
        AccountInvoices invoice = new AccountInvoices(driver);
		secondInv=invoice.getInvoiceAmountByRowNumber(2);
        Date sencondInvDueDate = DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getEffectiveDate(), DateAddSubtractOptions.Month, 1);
		//pay down payment		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
        directPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), myPolicyObj.accountNumber);
		//make second invoice Billed
		ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(sencondInvDueDate, DateAddSubtractOptions.Day, -15));
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		//move clock to pass Due Date of second invoice, reverse the payment 
		ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(sencondInvDueDate, DateAddSubtractOptions.Day, 3));
		acctMenu.clickAccountMenuPayments();
        AccountPayments payment = new AccountPayments(driver);
        payment.reversePayment(myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), null, null, PaymentReversalReason.Payment_Moved);
		//verify the delinquency
		acctMenu.clickBCMenuDelinquencies();
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		try{
            delinquency.getDelinquencyTableRow(OpenClosed.Open, DelinquencyReason.PastDueFullCancel, null, null, null, null, null, myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount() + secondInv);
			Assert.fail("the delinquency should not include the Billed amount.");
		}catch(Exception e){
			
		}
		try{
            delinquency.getDelinquencyTableRow(OpenClosed.Open, DelinquencyReason.PastDueFullCancel, null, null, null, null, null, myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount());
		}catch(Exception e){
			Assert.fail("didn't find the correct delinquency");
		}	
	}
	@Test(dependsOnMethods = { "paymentAndReverseAndVerify" })
    public void verifyPaymentInstrument() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.busOwnLine.getPolicyNumber());
        Date thirdInvDueDate = DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getEffectiveDate(), DateAddSubtractOptions.Month, 2);
		//get payment instrument from policy summary screen
        BCPolicySummary summary = new BCPolicySummary(driver);
		String paymentInstrument=summary.getPaymentInstrumentValue();
		summary.clickAccountNumberLink();
		//make payment to exit the delinquency
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
        directPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount() + secondInv, myPolicyObj.accountNumber);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		//move the third invoice date and create payment request
		ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(thirdInvDueDate, DateAddSubtractOptions.Day, -15));
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Payment_Request);
		acctMenu.clickAccountMenuPayments();
        acctMenu.clickAccountMenuPaymentsPaymentRequests();
		AccountPaymentsPaymentRequests request=new AccountPaymentsPaymentRequests(driver);
		try{
            request.getPaymentRequestRow(null, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, null, null, null, null, myPolicyObj.busOwnLine.getPolicyNumber(), paymentInstrument, secondInv);
		}catch(Exception e){
			Assert.fail("didn't find the correct Payment Request.");
		}
		
	}
}

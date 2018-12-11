package regression.r2.clock.billingcenter.invoicing;

import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.cancellation.StartCancellation;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.enums.Underwriter.UnderwriterLine;
import persistence.enums.Underwriter.UnderwriterTitle;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
/**
* @Author JQU
* @Requirement 	US14026: Membership Dues Invoicing - Issuance
				On a Membership Dues only policy, the down invoice will be billed and due per criteria listed below
				If the effective date of the policy is in the future, the invoice and due date are same as the effective date of the policy
				If the effective date of the policy is in the past or the day the charges are received, the invoice and due date are same as date the charges are received
				Membership dues will be billed in a single invoice and not spread out
				Should work for Dues only policy and insurance w/ dues line
				
				@Requirement 	US14303: Membership Dues Invoicing - Removal
				Flat cancel (Credit form PC) -- Credit invoice and due date are same as charge received date
* @DATE Mar 06, 2018
*/
public class MembershipEffectiveDatesAndFlatCancellationTest extends BaseTest {
	private ARUsers arUser;
	private GeneratePolicy myPolicyObj;
	private boolean effectiveDateOnFuture = false;
	private int num;
	private Date effectiveDate;
	private WebDriver driver;

	@Test
	public void generatePolicy() throws GuidewireException, Exception {	
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		num = NumberUtils.generateRandomNumberInt(1, 20);
		if(num % 2 == 0) {
			effectiveDateOnFuture = true;
			effectiveDate= DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, num);
		}
				
		myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsFirstLastName("Dues", "IssueCancel")
				.withProductType(ProductLineType.Membership)
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Cash)
				.withPolEffectiveDate(effectiveDate) 
				.build(GeneratePolicyType.PolicyIssued);		
	}
	@Test(dependsOnMethods = { "generatePolicy" })
	public void verifyInvoiceAndDueDates() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuInvoices();
		AccountInvoices invoices = new AccountInvoices(driver);
		if(effectiveDateOnFuture){
			if(!invoices.verifyInvoice(effectiveDate, effectiveDate, null, null, null, null, null, null)){
				Assert.fail("Invoice/Due dates are incorrect.");
			}
		}else{
			if(!invoices.verifyInvoice(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), null, null, null, null, null, null)){
				Assert.fail("Invoice/Due dates are incorrect.");
			}
		}				
	}
	@Test(dependsOnMethods = { "verifyInvoiceAndDueDates" })
	public void payDownPayment() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		if(effectiveDateOnFuture){
			ClockUtils.setCurrentDates(driver, effectiveDate);
		}
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		//Payoff DownPayment
		accountMenu.clickAccountMenuActionsNewDirectBillPayment();
		NewDirectBillPayment payment = new NewDirectBillPayment(driver);
		payment.makeDirectBillPaymentExecute(myPolicyObj.membership.getPremium().getDownPaymentAmount(), myPolicyObj.accountNumber);
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 3);	
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);
	}
	@Test(dependsOnMethods = { "payDownPayment" })
	public void flatCancelPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(UnderwriterLine.Personal, UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolicyObj.membership.getPolicyNumber());
        StartCancellation cancellation = new StartCancellation(driver);
        cancellation.cancelPolicy(CancellationSourceReasonExplanation.OtherOther, "Flat Cancel", myPolicyObj.membership.getEffectiveDate(), true);
	}
	@Test(dependsOnMethods = { "flatCancelPolicy" })
	public void verifyCreditInvioce() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuInvoices();
		AccountInvoices invoices = new AccountInvoices(driver);
		if(!invoices.verifyInvoice(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), null, InvoiceType.Shortage, null, InvoiceStatus.Planned, myPolicyObj.membership.getPremium().getMembershipDuesAmount()*(-1), null))
			Assert.fail("Got increct invoice item after Flat Cancellation.");
	}
}

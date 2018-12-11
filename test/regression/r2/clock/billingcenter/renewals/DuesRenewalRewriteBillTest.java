package regression.r2.clock.billingcenter.renewals;

import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDocuments;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RenewalCode;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.renewal.StartRenewal;
import repository.pc.workorders.rewrite.StartRewrite;
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
* @Requirement 	US14302: Membership Dues Invoicing - Renewal
				Renewal (For Dues only policy)
				Due date is one day prior to renewal effective date
				Invoice date is 46 prior to renewal effective date
				Membership dues will be billed in a single invoice and not spread out
				Should work for Dues only policy and insurance w/ dues line (Insured and Lien payer)
				For insurance w/ Dues line will continue to work as defined in requirements
				
				US14301 -- Membership Dues Invoicing - Rewrites
* @DATE Mar 06, 2018
*/
public class DuesRenewalRewriteBillTest extends BaseTest {
	private ARUsers arUser;
	private GeneratePolicy myPolicyObj;	
	private Date day50;
	private WebDriver driver;

	@Test
	public void generatePolicy() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		cf.setCenter(ApplicationOrCenter.PolicyCenter);		
		myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsFirstLastName("Membership", "Policy")
				.withProductType(ProductLineType.Membership)				
				.withDownPaymentType(PaymentType.Cash)				
				.build(GeneratePolicyType.PolicyIssued);		
	}		
	@Test(dependsOnMethods = { "generatePolicy" })
	public void payDues() throws Exception{
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuActionsNewDirectBillPayment();
		NewDirectBillPayment directPmt = new NewDirectBillPayment(driver);
		directPmt.makeDirectBillPaymentExecute(myPolicyObj.membership.getPremium().getMembershipDuesAmount(), myPolicyObj.accountNumber);
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);
	}
	@Test(dependsOnMethods = { "payDues" })
	public void CancelPolicyInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.membership.getPolicyNumber());
        StartCancellation cancelPol = new StartCancellation(driver);
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.OtherOther, "Flat Cancellation", myPolicyObj.membership.getEffectiveDate(),true);
	}
	//verify cancellation in BC
	@Test(dependsOnMethods = { "CancelPolicyInPolicyCenter" })
	public void verifyCancellation() throws Exception{		
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);		
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();
		AccountCharges charges = new AccountCharges(driver);
		charges.waitUntilChargesFromPolicyContextArrive(60, TransactionType.Cancellation);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);
	}
	//rewrite
	@Test(dependsOnMethods = { "verifyCancellation" })
    public void rewriteFullTerm() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.membership.getPolicyNumber());
        StartRewrite rewrite = new StartRewrite(driver);
		rewrite.rewriteFullTerm(myPolicyObj);		
	}
	//verify rewrite in BC
	@Test(dependsOnMethods = { "rewriteFullTerm" })
	public void verifyRewriteAndPay() throws Exception{		
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);		
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();
		AccountCharges charges = new AccountCharges(driver);
		charges.waitUntilChargesFromPolicyContextArrive(60, TransactionType.Rewrite);
		accountMenu.clickAccountMenuInvoices();
		AccountInvoices invoices = new AccountInvoices(driver);
		if(!invoices.verifyInvoice(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), null, InvoiceType.RewriteDownPayment, null, InvoiceStatus.Planned, myPolicyObj.membership.getPremium().getMembershipDuesAmount(), myPolicyObj.membership.getPremium().getMembershipDuesAmount())){
			Assert.fail("Got Incorredt Rewrite Invoice.");
		}
		//flat cancellation credit will pay the rewrite down payment
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);
	}
	@Test(dependsOnMethods = { "verifyRewriteAndPay" })
	public void manuallyRenewPolicy() throws Exception{
		day50 = DateUtils.dateAddSubtract(myPolicyObj.membership.getExpirationDate(), DateAddSubtractOptions.Day, -50);
		ClockUtils.setCurrentDates(driver, day50);
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(UnderwriterLine.Personal, UnderwriterTitle.Underwriter);
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolicyObj.membership.getPolicyNumber());
        StartRenewal renewalWorkflow = new StartRenewal(driver);
		renewalWorkflow.renewPolicyManually(RenewalCode.Renew_Good_Risk, myPolicyObj);
		new GuidewireHelpers(driver).logout();
	}
	@Test(dependsOnMethods = { "manuallyRenewPolicy" })
	public void verifyInvoiceAndDueDatesOnRenewal() throws Exception {
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 4); // move to day 46	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		//wait for renewal to come
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();
		AccountCharges charges = new AccountCharges(driver);
		charges.waitUntilChargesFromPolicyContextArrive(60, TransactionType.Renewal);
		
		accountMenu.clickAccountMenuInvoices();
		AccountInvoices invoices = new AccountInvoices(driver);		
		if(!invoices.verifyInvoice(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, 45), null, InvoiceType.RenewalDownPayment, null, InvoiceStatus.Planned, myPolicyObj.membership.getPremium().getMembershipDuesAmount(), myPolicyObj.membership.getPremium().getMembershipDuesAmount())){
			Assert.fail("Got Incorredt Renewal Invoice.");
		}	
		//verify renewal document
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
		accountMenu.clickBCMenuDocuments();
		BCCommonDocuments document = new BCCommonDocuments(driver);
		if(!document.verifyDocument("Membership Renewal", null, null, null, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter))){
			Assert.fail("Couldn't find the correct Membership Renewal Document.");
		}
	}	
}

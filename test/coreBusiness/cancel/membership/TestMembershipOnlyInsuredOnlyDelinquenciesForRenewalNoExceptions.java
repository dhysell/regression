package coreBusiness.cancel.membership;

import java.util.Calendar;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.common.BCCommonCharges;
import repository.bc.common.BCCommonDelinquencies;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentReturnedPaymentReason;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.TransactionType;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

@Test(groups = {"ClockMove","Delinquencies","BillingCenter"})
public class TestMembershipOnlyInsuredOnlyDelinquenciesForRenewalNoExceptions extends BaseTest {
	private GeneratePolicy myPolicyObj = null;
	public ARUsers arUser;	
	private Date dueDate,invoiceDate;
	private double invoiceAmount ;
	private BCAccountMenu acctMenu;
	private String getTypeOfCancel;
	private WebDriver driver;
	
	/**
	* @Author sgunda
	* @Requirement US13896 - [Continued] Delinquencies on Membership only policy
	* @RequirementsLink <a href="https://rally1.rallydev.com/#/68999021356d/detail/defect/62536628605">Link Text<US13896 - [Continued] Delinquencies on Membership only policy/a>
	* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/06%20-%20Delinquency%20Cancel/06-01%20Insured%20Non-Pay%20Full%20Cancel%20Delinquency.docx">Link Text<06-01 Insured Non-Pay Full Cancel Delinquency: See requirement 06-01-09 %/a>
	* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/06%20-%20Delinquency%20Cancel/06-02%20Lienholder%20Non-Pay%20Full%20Cancel%20Delinquency.docx">Link Text<06-02 Lienholder Non-Pay Full Cancel Delinquency: See requirement 06-02-04 %/a>
	* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/06%20-%20Delinquency%20Cancel/06-03%20Insured%20Non-Pay%20Partial%20Cancel%20Delinquency.docx">Link Text<06-03 Insured Non-Pay Partial Cancel Delinquency: See requirement 06-03-08 %/a>
	* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/06%20-%20Delinquency%20Cancel/06-04%20Lienholder%20Non-Pay%20Partial%20Cancel%20Delinquency.docx">Link Text<06-04 Lienholder Non-Pay Partial Cancel Delinquency: See requirement 06-04-06/a>
	* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/06%20-%20Delinquency%20Cancel/06-07%20Not%20Taken%20Cancel%20Delinquency.docx">Link Text<06-07 Not Taken Cancel Delinquency: See requirement 06-07-06/a>
	* 
	* @RequirementsLink<a href="http://projects.idfbins.com/billingcenter/Documents/Release 2 Requirements/06 - Delinquency Cancel/Supporting Documentation/Delinquency Scenario Membership 2.0.xlsx">Link Text<Delinquency Scenario Membership 2.0 Spreadsheet/a> 
	* 
	* Scenario 1 : Renewal down invoice not paid , triggers Not Taken Full
	* Scenario 2 : Renewal down invoice returned , Triggers Past Due Full
	* 
	* @DATE March 09, 2018
	*/
	

	public String whatToTest() {
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_MONTH);
		if (day % 2 == 0 ) {
			return getTypeOfCancel = "NonPay";			
		} else {
			return getTypeOfCancel = "ReversePayment";
		}
	}
	
	@Test
	public void generatePolicy() throws GuidewireException, Exception {	
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsFirstLastName("MembershipOnly", "FullCancel")
				.withProductType(ProductLineType.Membership)
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}
	
	@Test(dependsOnMethods = { "generatePolicy" })
	public void makeDownpayment() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);
		NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.membership.getPremium().getDownPaymentAmount(), myPolicyObj.membership.getPolicyNumber());
        ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);
        new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);   
	}

	@Test(dependsOnMethods = { "makeDownpayment" })
	public void moveClocksSoThatWeCanRenewPolicy() throws Exception {
		ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(myPolicyObj.membership.getExpirationDate(), DateAddSubtractOptions.Day, -48));
	}
	
	@Test(dependsOnMethods = { "moveClocksSoThatWeCanRenewPolicy" })
	public void runInvoiceDueAndCheckDelinquency() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.membership.getPolicyNumber());
		PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickRenewPolicy();
        new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.membership.getPolicyNumber());
        PolicySummary policySummary = new PolicySummary(driver);
        if(!policySummary.isTransactionComplete(TransactionType.Renewal, null)){
        	new BatchHelpers(driver).runBatchProcess(BatchProcess.PC_Workflow);        	
        }
        
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuToolsBilling();
        sideMenu.clickSideMenuToolsSummary();
        Assert.assertTrue(policySummary.isTransactionComplete(TransactionType.Renewal, null), "Renewal failed");

	}

	@Test(dependsOnMethods = { "runInvoiceDueAndCheckDelinquency" })
	public void makeRenewalInvoiceBilled() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);
		acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
		BCCommonCharges charges = new BCCommonCharges(driver);
		Assert.assertTrue(charges.waitUntilChargesFromPolicyContextArrive(120, TransactionType.Renewal),"BC didn't get renewal charges");
		acctMenu.clickAccountMenuInvoices();
		
		AccountInvoices accountInvoices = new AccountInvoices(driver);
		invoiceDate = accountInvoices.getInvoiceDateByInvoiceType(InvoiceType.RenewalDownPayment);	
		dueDate = accountInvoices.getInvoiceDueDateByInvoiceType(InvoiceType.RenewalDownPayment);
		invoiceAmount = accountInvoices.getInvoiceDueAmountByDueDate(dueDate);
		
		ClockUtils.setCurrentDates(driver, invoiceDate);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
		
		whatToTest();
		if (getTypeOfCancel.equals("ReversePayment")) {
			acctMenu.clickAccountMenuActionsNewDirectBillPayment();
			NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
	        newPayment.makeDirectBillPaymentExecute(invoiceAmount, myPolicyObj.membership.getPolicyNumber());

	        acctMenu.clickAccountMenuInvoices();
	    	Assert.assertTrue(accountInvoices.verifyInvoice(invoiceDate, dueDate, null, InvoiceType.RenewalDownPayment, null, InvoiceStatus.Billed, null, 0.00), "Payment didn't apply to the invoice");				
		}
	
	}
	
	@Test(dependsOnMethods = { "makeRenewalInvoiceBilled" })
	public void verifyDelinquenciesTriggered() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);
		acctMenu = new BCAccountMenu(driver);
		BCCommonDelinquencies delinquencies =  new BCCommonDelinquencies(driver);

		Date dayPassedDueDate = DateUtils.dateAddSubtract(dueDate, DateAddSubtractOptions.Day, 1);
		ClockUtils.setCurrentDates(driver, dayPassedDueDate);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);
		
		Date releaseTTDate = DateUtils.dateAddSubtract(dueDate, DateAddSubtractOptions.Day, 7);
		ClockUtils.setCurrentDates(driver, releaseTTDate);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Release_Trouble_Ticket_Holds);
		
		if (getTypeOfCancel.equals("NonPay")) {
			acctMenu.clickBCMenuDelinquencies();
			Assert.assertTrue(delinquencies.verifyDelinquencyByReason(null, DelinquencyReason.NotTaken,null, null),"Please verify, Not Taken has not triggered ");
			
		} else {
	        ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Month, 1);
	        new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
	        new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);
	        
			AccountPayments accountPayments = new AccountPayments(driver);
	        accountPayments.reversePaymentAtFault(invoiceDate, myPolicyObj.membership.getPremium().getDownPaymentAmount(), null, null, PaymentReturnedPaymentReason.AccountFrozen);
			
	        acctMenu.clickBCMenuDelinquencies();
			Assert.assertTrue(delinquencies.verifyDelinquencyByReason(null, DelinquencyReason.PastDueFullCancel, null,DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter)),"Please verify Past Due Full Cancel has not triggered");
		}		
	}
	
}

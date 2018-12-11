package regression.r2.clock.billingcenter.cancel.membershipcancels;

import java.util.Calendar;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountPolicies;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.common.BCCommonDelinquencies;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentReturnedPaymentReason;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.cancellation.StartCancellation;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

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
* Scenario 1 : Cancel policy before TT closes , triggers Underwriting Full
* Scenario 2 : Cancellation mid-term and payment is reversed , Triggers Modified Past Due Full
* 
* @DATE March 09, 2018
*/

public class TestMembershipOnlyInsuredOnlyUnderwritingFull extends BaseTest {
	private GeneratePolicy myPolicyObj = null;
	public ARUsers arUser;	
	private BCAccountMenu acctMenu;
	private StartCancellation startCancellation;
	private String getTypeOfCancel;
	private Date date;
	private WebDriver driver;

	
	public String whatToTest() {
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_MONTH);
		if (day % 2 != 0 ){
			return getTypeOfCancel = "UWFull";
		} else  {
			return getTypeOfCancel = "ModifiedPastDueFull" ;
		}		
	}	
	
	@Test
	public void generatePolicy() throws GuidewireException, Exception {	
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsFirstLastName("MembershipOnly", "UW")
				.withProductType(ProductLineType.Membership)
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}
	
	@Test(dependsOnMethods = { "generatePolicy" })
	public void runInvoiceInBCAndMakingDownpaymentOnOneMethod() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);
        
        whatToTest();
        if (getTypeOfCancel.equals("UWFull")) {
        	new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
		}else {
			NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
	        newPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.membership.getPremium().getDownPaymentAmount(), myPolicyObj.membership.getPolicyNumber());						
		}
	}

	@Test(dependsOnMethods = { "runInvoiceInBCAndMakingDownpaymentOnOneMethod" })
	public void moveClocksRunBatches() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(this.arUser.getUserName(), this.arUser.getPassword());
		
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);		
	}
	
	@Test(dependsOnMethods = { "moveClocksRunBatches" })
	public void cancelPolicyInPC() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.membership.getPolicyNumber());
        
        if(getTypeOfCancel.equals("UWFull")) {
			ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 8);
			startCancellation = new StartCancellation(driver);
			startCancellation.cancelPolicy(CancellationSourceReasonExplanation.MembershipNoReasonGiven,"Other", date=DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), true);

		}else {			
			ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 30);
			startCancellation = new StartCancellation(driver);
			startCancellation.cancelPolicy(CancellationSourceReasonExplanation.MembershipNoReasonGiven,"Other", date=DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), true);
		}
	}

	@Test(dependsOnMethods = { "cancelPolicyInPC" })
	public void verifyCancelationCompletionAndTestDelinquencies() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
        AccountPolicies accountPolicies = new AccountPolicies(driver);
        BCCommonDelinquencies acctDelinquency = new BCCommonDelinquencies(driver);

        acctMenu.clickAccountMenuPolicies();
        Assert.assertTrue(accountPolicies.verifyCancellation(myPolicyObj.membership.getPolicyNumber()),"Policy haven't cancelled");
               
        if (getTypeOfCancel.equals("UWFull")) {
            acctMenu.clickBCMenuDelinquencies();
        	Assert.assertTrue(acctDelinquency.verifyDelinquencyByReason(OpenClosed.Open, DelinquencyReason.UnderwritingFullCancel,null, date),"UW Full did not trigger");
        	
		}else {
			ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 10);
			acctMenu.clickAccountMenuPaymentsPayments();
			
			AccountPayments accountPayments = new AccountPayments(driver);
	        accountPayments.reversePaymentAtFault(null,myPolicyObj.membership.getPremium().getDownPaymentAmount(), null, null, PaymentReturnedPaymentReason.AccountFrozen);
	        date = DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
	        
	        acctMenu.clickBCMenuDelinquencies();
	        Assert.assertTrue(acctDelinquency.verifyDelinquencyByReason(OpenClosed.Open, DelinquencyReason.PastDueFullCancel,null, date),"Past Due Full Cancel did not trigger");
				
		}
		
	}
}

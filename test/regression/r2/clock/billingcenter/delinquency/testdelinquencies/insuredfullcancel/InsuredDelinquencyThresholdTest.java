package regression.r2.clock.billingcenter.delinquency.testdelinquencies.insuredfullcancel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.desktop.actions.DesktopActionsMultiplePayment;
import repository.bc.search.BCSearchAccounts;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentReversalReason;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GeneratePolicyHelper;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author sgunda
 * @Requirement DE3956 - Delinquency not closed even if delinquent amount is zero or less than 10 %
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/68999021356d/detail/defect/62536628605">Link Text<DE3956 - Delinquency not closed even if delinquent amount is zero or less than 10 %/a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/06%20-%20Delinquency%20Cancel/06-01%20Insured%20Non-Pay%20Full%20Cancel%20Delinquency.docx">Link Text<06-01 Insured Non-Pay Full Cancel Delinquency: See requirement 06-01-09 %/a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/06%20-%20Delinquency%20Cancel/06-02%20Lienholder%20Non-Pay%20Full%20Cancel%20Delinquency.docx">Link Text<06-02 Lienholder Non-Pay Full Cancel Delinquency: See requirement 06-02-04 %/a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/06%20-%20Delinquency%20Cancel/06-03%20Insured%20Non-Pay%20Partial%20Cancel%20Delinquency.docx">Link Text<06-03 Insured Non-Pay Partial Cancel Delinquency: See requirement 06-03-08 %/a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/06%20-%20Delinquency%20Cancel/06-04%20Lienholder%20Non-Pay%20Partial%20Cancel%20Delinquency.docx">Link Text<06-04 Lienholder Non-Pay Partial Cancel Delinquency: See requirement 06-04-06/a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/06%20-%20Delinquency%20Cancel/06-07%20Not%20Taken%20Cancel%20Delinquency.docx">Link Text<06-07 Not Taken Cancel Delinquency: See requirement 06-07-06/a>
 * @Description Test if delinquencies are correctly triggered/not triggered and/or closed in the following scenarios: 1)Equal to 100% of original delinquent amount payment,  2)More than 100% of original delinquent amount payment
 * @DATE May 12, 2017
 */
public class InsuredDelinquencyThresholdTest extends BaseTest {
	private WebDriver driver;
	private ARUsers arUser;
	private BCAccountMenu acctMenu;
	private BCCommonDelinquencies  acctDelinquency;
	private AccountPayments payment;
	private GeneratePolicy myPolicyObj;
	private NewDirectBillPayment directPayment;
	private DesktopActionsMultiplePayment makePayment;
	private double orgDelinquentAmount  ;   
	private Date reversalDate;
	
	private OpenClosed openOrClosed;
	private String errorMsg;
	private HashMap<String, Object> getValues;
	private double payAmount;
	
	public HashMap<String, Object> getVariablesToRunTest(double orgDelinquentAmount ) {
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String printMsg = null;
		Double payDelinquentAmountBeforeRounding;
						
		switch (day) {
			case 1: case 6: case 11: case 16: case 21: case 26:
			//Making Payment for 100% of Delinquent amount
				payAmount = orgDelinquentAmount;
				openOrClosed = OpenClosed.Closed;
				errorMsg = "Delinquency still exists after making payment for 100% of Original Delinquent Amount which is not Expected";
				printMsg = "This test is running a scenario where it pays 100% Original Delinquent Amount and check if delinquent exits or not";
				break;
				
			case 2: case 7: case 12: case 17: case 22: case 27:
			//Making Payment for less than 90% of Original Delinquent amount, which should exit delinquency 
				payDelinquentAmountBeforeRounding = (orgDelinquentAmount)*0.89;		
				payAmount= BigDecimal.valueOf(payDelinquentAmountBeforeRounding).setScale(2, RoundingMode.UP).doubleValue();
				openOrClosed = OpenClosed.Open;
				errorMsg = "Delinquency closed. This not expected, it should be open because payment made is less than 90% of orginal Delinquent amount - which is below Threshold";
				printMsg = "This test is running a scenario where it pays 89% Original Delinquent Amount and check if delinquent exits or not";
				break;
				
			case 3: case 8: case 13: case 18: case 23: case 28:
			//Making Payment for 90% of Delinquent amount 
				payDelinquentAmountBeforeRounding = (orgDelinquentAmount)*0.90;		
				payAmount = BigDecimal.valueOf(payDelinquentAmountBeforeRounding).setScale(2, RoundingMode.UP).doubleValue();
				openOrClosed = OpenClosed.Closed;
				errorMsg = "Delinquency still exists after making payment for 90% of Original Delinquent Amount which is not Expected" ;
				printMsg = "This test is running a scenario where it pays 90% Original Delinquent Amount and check if delinquent exits or not";
				break;
				
			case 4: case 9: case 14: case 19: case 24: case 29:
			//Making Payment for more than 100% of Original Delinquent amount, which should exit delinquency 
				payDelinquentAmountBeforeRounding = (orgDelinquentAmount)*1.05;		
				payAmount = BigDecimal.valueOf(payDelinquentAmountBeforeRounding).setScale(2, RoundingMode.DOWN).doubleValue();
				openOrClosed = OpenClosed.Closed;
				errorMsg = "Delinquency still exists after making payment for more than 100% of Original Delinquent Amount which is not Expected";
				printMsg = "This test is running a scenario where it pays 105% Original Delinquent Amount and check if delinquent exits or not";
				break;
				
			case 5: case 10: case 15: case 20:case 25: case 30:
			//Making Payment for more than 90% of Delinquent amount 
				payDelinquentAmountBeforeRounding = (orgDelinquentAmount)*0.92;		
				payAmount = BigDecimal.valueOf(payDelinquentAmountBeforeRounding).setScale(2, RoundingMode.DOWN).doubleValue();
				openOrClosed = OpenClosed.Closed;
				errorMsg = "Delinquency still exists after making payment for more than 90% of Original Delinquent Amount which is not Expected";				
				printMsg = "This test is running a scenario where it pays 92% Original Delinquent Amount and check if delinquent exits or not";
				break;
				
			default:
			//Making Payment for less than 90% of Original Delinquent amount, which should exit delinquency 
				payDelinquentAmountBeforeRounding = (orgDelinquentAmount)*0.89;		
				payAmount= BigDecimal.valueOf(payDelinquentAmountBeforeRounding).setScale(2, RoundingMode.UP).doubleValue();
				openOrClosed = OpenClosed.Open;
				errorMsg = "Delinquency closed. This not expected, it should be open because payment made is less than 90% of orginal Delinquent amount - which is below Threshold";
				printMsg = "This test is running a scenario where it pays 89% Original Delinquent Amount and check if delinquent exits or not";
				break;
		}
		
		System.out.println(printMsg);
		getValues = new HashMap<String, Object>();
				getValues.put("payAmount", payAmount);
				getValues.put("openOrClosed", openOrClosed);
				getValues.put("errorMsg", errorMsg);
		
		return getValues;
	}
	
	
	
	private void gotoAccount(String accountNumber){
		BCSearchAccounts search=new BCSearchAccounts(driver);
		search.searchAccountByAccountNumber(accountNumber);
	}
	
	@Test
	public void SquirePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		myPolicyObj = new GeneratePolicyHelper(driver).generatePLSectionIAndIIPropertyAndLiabilityLinePLPolicy("ThresholdTest", "Insured",null,PaymentPlanType.Annual,PaymentType.Cash);

		System.out.println(myPolicyObj.accountNumber);
        System.out.println(myPolicyObj.squire.getPolicyNumber());
	}
	
	@Test (dependsOnMethods = { "SquirePolicy" })
	public void makeDownPayment() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
		new Login(driver).login(arUser.getUserName() , arUser.getPassword());

        makePayment = new DesktopActionsMultiplePayment(driver);
        makePayment.makeInsuredMultiplePaymentDownpayment(myPolicyObj, myPolicyObj.squire.getPolicyNumber());
		
		repository.gw.helpers.ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 10);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		
		new GuidewireHelpers(driver).logout();

	}
	
	
	@Test( dependsOnMethods = { "makeDownPayment" } )
	public void makeInvoiceDue() throws Exception	{
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		
		repository.gw.helpers.ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 20);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

	}	
	
	@Test( dependsOnMethods = { "makeInvoiceDue" } )
    public void reversePaymentMadeOnDownPaymentAndCheckForDelinquency() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuPayments();
        payment = new AccountPayments(driver);
        payment = new AccountPayments(driver);
        payment.reversePayment(myPolicyObj.squire.getPremium().getDownPaymentAmount(), null, null, PaymentReversalReason.Payment_Moved);
		
		// letting account to go Delinquent in this search time 
		gotoAccount(myPolicyObj.accountNumber);	
		
		reversalDate= DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
		
		acctMenu.clickBCMenuDelinquencies();
        acctDelinquency = new BCCommonDelinquencies(driver);

        Assert.assertTrue(acctDelinquency.verifyDelinquencyStatus(OpenClosed.Open, myPolicyObj.accountNumber, myPolicyObj.squire.getPolicyNumber(), reversalDate), "Delinquency does not exist Which is not Expected");
        orgDelinquentAmount = acctDelinquency.getOriginalDelinquentAmount(OpenClosed.Open, myPolicyObj.accountNumber, myPolicyObj.squire.getPolicyNumber(), reversalDate);
		
	}
	
	@Test( dependsOnMethods = { "reversePaymentMadeOnDownPaymentAndCheckForDelinquency" } )
    public void payDelinquentAmountAndCheckDelinquency() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
		
		getVariablesToRunTest(orgDelinquentAmount);
			//getting values from HashMap
				payAmount = (double) getValues.get("payAmount");
				openOrClosed = (OpenClosed) getValues.get("openOrClosed");
				errorMsg = (String) getValues.get("errorMsg");

        directPayment = new NewDirectBillPayment(driver);
		directPayment.makeDirectBillPaymentExecute(payAmount, myPolicyObj.accountNumber);
		
		//letting account to exit Delinquent in this search time 
		gotoAccount(myPolicyObj.accountNumber);
		
		acctMenu.clickBCMenuDelinquencies();
        acctDelinquency = new BCCommonDelinquencies(driver);
        Assert.assertTrue(acctDelinquency.verifyDelinquencyStatus(openOrClosed, myPolicyObj.accountNumber, myPolicyObj.squire.getPolicyNumber(), reversalDate), errorMsg);

	}
	

}

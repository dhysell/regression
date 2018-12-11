package regression.r2.clock.billingcenter.delinquency.testdelinquencies.insuredfullcancel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.HashMap;

import repository.gw.helpers.GeneratePolicyHelper;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.search.BCSearchAccounts;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author sgunda
 * @Requirement DE6030 - Paid 90% of down payment for lienholder. Cancelled triggered
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/7832667974d/detail/defect/133114378212">Link Text< DE6030 - Paid 90% of down payment for lienholder. Cancelled triggered /a>
 * @Description 1.New Squire all LH billed/Create BOP policy fully lien billed 2.Paid 90% of down, 3.Moved clock to day past invoice due date , 4.Ran invoice and invoice due
 * 5.Actual Result: Past Due Lien Partial Cancel Triggered
 * Expected Result: Policy should not cancel and a shortage invoice should create to take care of the remaining amount unpaid
 * @DATE Aug 1, 2017
 */
public class InsuredThresholdForTriggeringDelinquency extends BaseTest {
	private WebDriver driver;	
	private ARUsers arUser;
	private BCAccountMenu acctMenu;
	private BCCommonDelinquencies  acctDelinquency;
	private GeneratePolicy myPolicyObj;
	
	private String errorMsg;
	private HashMap<String, Object> getValues;
	private double payAmount;
	private boolean delinquencyFound;
	
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
		
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(),arUser.getPassword(), this.myPolicyObj.accountNumber);

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);

        getVariablesToRunTest(myPolicyObj.squire.getPremium().getDownPaymentAmount());
		//getting values from HashMap
			delinquencyFound = (boolean) getValues.get("delinquencyFound");
			payAmount = (double) getValues.get("payAmount");
			errorMsg = (String) getValues.get("errorMsg");

        newPayment.makeInsuredDownpayment(myPolicyObj, payAmount, myPolicyObj.squire.getPolicyNumber());

		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 9);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Release_Trouble_Ticket_Holds);
		
		gotoAccount(this.myPolicyObj.accountNumber);

        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuDelinquencies();
        acctDelinquency = new BCCommonDelinquencies(driver);

        boolean trueOrFalse = acctDelinquency.verifyDelinquencyStatus(null, null, myPolicyObj.squire.getPolicyNumber(), null);
		if(trueOrFalse!=delinquencyFound)
			{
				Assert.fail(errorMsg);
			}
		
	}
		
	public HashMap<String, Object> getVariablesToRunTest(double payingAmount ) {
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String printMsg = null;
		Double payInvoiceAmountBeforeRounding;
						
		switch (day) {
			case 1: case 6: case 11: case 16: case 21: case 26:
			//Making Payment for 100% of invoice amount
				delinquencyFound = false;
				payAmount = payingAmount;
				errorMsg = "Delinquency Trigger even though 100% of invoice is amount is paid which is not Expected";
				printMsg = "This test is running a scenario where it pays 100% of invoice and check if delinquent exits or not";
				break;
				
			case 2: case 7: case 12: case 17: case 22: case 27:
			//Making Payment for less than 90% of invoice amount, which should exit delinquency 
				delinquencyFound = true;
				payInvoiceAmountBeforeRounding = (payingAmount)*0.89;		
				payAmount= BigDecimal.valueOf(payInvoiceAmountBeforeRounding).setScale(2, RoundingMode.UP).doubleValue();
				errorMsg = "Delinquency not Trigger even though less than 90% of invoice amount - Not expected";
				printMsg = "This test is running a scenario where it pays 89% of invoice and check if delinquent exits or not";
				break;
				
			case 3: case 8: case 13: case 18: case 23: case 28:
			//Making Payment for 90% of invoice amount 
				delinquencyFound = false;
				payInvoiceAmountBeforeRounding = (payingAmount)*0.90;		
				payAmount = BigDecimal.valueOf(payInvoiceAmountBeforeRounding).setScale(2, RoundingMode.UP).doubleValue();
				errorMsg = "Delinquency Trigger even though 90% of invoice amount is paid which is not Expected" ;
				printMsg = "This test is running a scenario where it pays 90% of invoice and check if delinquent exits or not";
				break;
				
			case 4: case 9: case 14: case 19: case 24: case 29:
			//Making Payment for more than 100% of invoice amount, which should exit delinquency 
				delinquencyFound = false;
				payInvoiceAmountBeforeRounding = (payingAmount)*1.05;		
				payAmount = BigDecimal.valueOf(payInvoiceAmountBeforeRounding).setScale(2, RoundingMode.DOWN).doubleValue();
				errorMsg = "Delinquency Trigger even after making payment for more than 100% of invoice Amount which is not Expected";
				printMsg = "This test is running a scenario where it pays 105% of invoice and check if delinquent exits or not";
				break;
				
			case 5: case 10: case 15: case 20:case 25: case 30:
			//Making Payment for more than 90% of invoice amount 
				delinquencyFound = false;
				payInvoiceAmountBeforeRounding = (payingAmount)*0.92;		
				payAmount = BigDecimal.valueOf(payInvoiceAmountBeforeRounding).setScale(2, RoundingMode.DOWN).doubleValue();
				errorMsg = "Delinquency Trigger even though  more than 90% of invoice Amount is paid ,  which is not Expected";				
				printMsg = "This test is running a scenario where it pays 92% of invoice and check if delinquent exits or not";
				break;
				
			default:
			//Making Payment for less than 90% of invoice amount, which should exit delinquency 
				delinquencyFound = true;
				payInvoiceAmountBeforeRounding = (payingAmount)*0.89;		
				payAmount= BigDecimal.valueOf(payInvoiceAmountBeforeRounding).setScale(2, RoundingMode.UP).doubleValue();
				errorMsg = "Delinquency not Trigger even though less than 90% of invoice amount - Not expected";
				printMsg = "This test is running a scenario where it pays 89% of invoice and check if delinquent exits or not";
				break;
		}
		
		
		System.out.println(printMsg);
		getValues = new HashMap<String, Object>();
				getValues.put("delinquencyFound", delinquencyFound);
				getValues.put("payAmount", payAmount);
				getValues.put("errorMsg", errorMsg);
		
		return getValues;
	}
	
	
}

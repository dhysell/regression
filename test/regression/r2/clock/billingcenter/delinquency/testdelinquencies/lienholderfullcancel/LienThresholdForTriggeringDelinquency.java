package regression.r2.clock.billingcenter.delinquency.testdelinquencies.lienholderfullcancel;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.search.BCSearchAccounts;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.GenerateContactType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.GeneratePolicyHelper;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * @Author sgunda
 * @Requirement DE6030 - Paid 90% of down payment for lienholder. Cancelled triggered
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/7832667974d/detail/defect/133114378212">Link Text< DE6030 - Paid 90% of down payment for lienholder. Cancelled triggered /a>
 * @Description 1.New Squire all LH billed/Create BOP policy fully lien billed 2.Paid 90% of down, 3.Moved clock to day past invoice due date , 4.Ran invoice and invoice due
 * 5.Actual Result: Past Due Lien Partial Cancel Triggered
 * Expected Result: Policy should not cancel and a shortage invoice should create to take care of the remaining amount unpaid
 * @DATE Aug 1, 2017
 */
public class LienThresholdForTriggeringDelinquency extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;
	private ARUsers arUser = new ARUsers();

	private String lienholderNumber = null;
	private String lienholderLoanNumber = null;
	private double lienholderLoanPremiumAmount;
	
	private BCAccountMenu acctMenu;
	private BCCommonDelinquencies  acctDelinquency;
	private boolean delinquencyFound;
	private String errorMsg;
	private HashMap<String, Object> getValues;
	private double payAmount;
	
	private void gotoAccount(String accountNumber){
		BCSearchAccounts search= new BCSearchAccounts(driver);
		search.searchAccountByAccountNumber(accountNumber);	
	}	
	
	@Test
	public void generate() throws Exception {
		ArrayList<GenerateContact> generateContacts= new ArrayList<>();
		Config cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);

		ArrayList<repository.gw.enums.ContactRole> rolesToAdd = new ArrayList<repository.gw.enums.ContactRole>();
		rolesToAdd.add(repository.gw.enums.ContactRole.Lienholder);

		GenerateContact generateContact = new GenerateContact.Builder(driver)
				.withCompanyName("LH FullLienBilled")
				.withRoles(rolesToAdd)
				.withGeneratedLienNumber(true)
				.withUniqueName(true)
				.build(GenerateContactType.Company);

		generateContacts.add(generateContact);
		driver.quit();

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		myPolicyObj = new GeneratePolicyHelper(driver).generateFullyLienBilledBOPPolicy("LH ThresholdTest",null, repository.gw.enums.PaymentPlanType.Annual, repository.gw.enums.PaymentType.Cash , generateContacts);

		this.lienholderNumber = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
		this.lienholderLoanNumber = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLoanContractNumber();
		this.lienholderLoanPremiumAmount = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();
		
	}
	
	@Test(dependsOnMethods = { "generate" })
	public void payLienholderAmount() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienholderNumber);
		
		ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 25);
		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);

        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
		
		getVariablesToRunTest(this.lienholderLoanPremiumAmount);
		//getting values from HashMap
			delinquencyFound = (boolean) getValues.get("delinquencyFound");
			payAmount = (double) getValues.get("payAmount");
			errorMsg = (String) getValues.get("errorMsg");

        newPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPolicyNumber(), this.lienholderLoanNumber, payAmount);

		ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 15);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);
		
		gotoAccount(this.myPolicyObj.accountNumber);

        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuDelinquencies();
        acctDelinquency = new BCCommonDelinquencies(driver);

        boolean trueOrFalse = acctDelinquency.verifyDelinquencyStatus(null, null, myPolicyObj.busOwnLine.getPolicyNumber(), null);
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

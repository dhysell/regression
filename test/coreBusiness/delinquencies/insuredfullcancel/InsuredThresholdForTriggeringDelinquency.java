package coreBusiness.delinquencies.insuredfullcancel;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.PaymentPlanType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.BillingCenterHelper;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.GeneratePolicyHelper;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.HashMap;


public class InsuredThresholdForTriggeringDelinquency extends BaseTest {
	private WebDriver driver;	
	private GeneratePolicy myPolicyObj;
	private String policyNumber;
	private double downPayment;

	
	@Test
	public void SquirePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		// Creating a policy randomly
		myPolicyObj = Calendar.getInstance().get(Calendar.WEEK_OF_MONTH) % 2 == 0 ?
				new GeneratePolicyHelper(driver).generatePLSectionIAndIIPropertyAndLiabilityLinePLPolicy("InsuredPayer", "ThresholdTest",null, repository.gw.enums.PaymentPlanType.Annual, repository.gw.enums.PaymentType.Cash):
				new GeneratePolicyHelper(driver).generateBasicBOPPolicy("InsuredPayerThresholdTest",null, PaymentPlanType.Annual, repository.gw.enums.PaymentType.Cash);

		policyNumber = new BillingCenterHelper(driver).getPolicyNumberFromGeneratePolicyObject(myPolicyObj);
		downPayment = ((myPolicyObj.productType == repository.gw.enums.ProductLineType.Businessowners) ? myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount() : myPolicyObj.squire.getPremium().getDownPaymentAmount());

	}
	
	@Test (dependsOnMethods = { "SquirePolicy" })
	public void makeDownPayment() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new BillingCenterHelper(driver).loginAsRandomARUserAndVerifyIssuancePolicyPeriod(myPolicyObj);

		HashMap<String, Object> getValues = new BillingCenterHelper(driver).getVariablesToRunDelinquencyTestScenarios(downPayment);
		//getting values from HashMap
			boolean delinquencyFound = (boolean) getValues.get("delinquencyFound");
			double payAmount = (double) getValues.get("payAmount");
			String errorMsg = (String) getValues.get("errorMsg");

		new NewDirectBillPayment(driver).makeInsuredDownpayment(myPolicyObj, payAmount, policyNumber);

		ClockUtils.setCurrentDates(driver, repository.gw.enums.DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);
		ClockUtils.setCurrentDates(driver, repository.gw.enums.DateAddSubtractOptions.Day, 9);
		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Release_Trouble_Ticket_Holds);

		new BCAccountMenu(driver).clickAccountMenuInvoices();
		new BCAccountMenu(driver).clickBCMenuDelinquencies();

		Assert.assertEquals(new BCCommonDelinquencies(driver).verifyDelinquencyByReason(repository.gw.enums.OpenClosed.Open, repository.gw.enums.DelinquencyReason.PastDueFullCancel, myPolicyObj.accountNumber, null), delinquencyFound, errorMsg);

	}

	
}

package coreBusiness.delinquencies.insuredfullcancel;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.common.BCCommonDelinquencies;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentReturnedPaymentReason;
import repository.gw.generate.GeneratePolicy;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.BillingCenterHelper;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GeneratePolicyHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class InsuredDelinquencyThresholdTest extends BaseTest {

	private WebDriver driver;
	private BCAccountMenu acctMenu;
	private BCCommonDelinquencies  acctDelinquency;
	private GeneratePolicy myPolicyObj;
	private double orgDelinquentAmount  ;
	private Date reversalDate;

	private String policyNumber ;
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
	public void makeDownPayment_ReversePaymentAndCheckForDelinquency() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);

		new BillingCenterHelper(driver).loginAsRandomARUserAndVerifyIssuancePolicyPeriod(myPolicyObj);
		new NewDirectBillPayment(driver).makeInsuredDownpayment(myPolicyObj,downPayment, policyNumber);
		repository.gw.helpers.ClockUtils.setCurrentDates(driver, repository.gw.enums.DateAddSubtractOptions.Day, 30);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);

        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuPayments();

		AccountPayments payment = new AccountPayments(driver);
		reversalDate= DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
		payment.reversePaymentAtFault(null,downPayment, null, null, PaymentReturnedPaymentReason.AccountFrozen);

		acctMenu.clickAccountMenuInvoices();
		acctMenu.clickBCMenuDelinquencies();

        acctDelinquency = new BCCommonDelinquencies(driver);
        Assert.assertTrue(acctDelinquency.verifyDelinquencyByReason(repository.gw.enums.OpenClosed.Open, repository.gw.enums.DelinquencyReason.PastDueFullCancel, myPolicyObj.accountNumber, reversalDate), "Delinquency does not exist Which is not Expected");
        orgDelinquentAmount = acctDelinquency.getOriginalDelinquentAmount(repository.gw.enums.OpenClosed.Open, myPolicyObj.accountNumber, policyNumber, reversalDate);
		
	}
	
	@Test( dependsOnMethods = { "makeDownPayment_ReversePaymentAndCheckForDelinquency" } )
    public void payDelinquentAmountAndCheckDelinquency() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);

		new BillingCenterHelper(driver).loginAsRandomARUserAndSearchAccount(myPolicyObj);

        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();

		HashMap<String, Object> getValues = new BillingCenterHelper(driver).getVariablesToRunDelinquencyTestScenarios(orgDelinquentAmount);
			//getting values from HashMap
			double	payAmount = (double) getValues.get("payAmount");
			repository.gw.enums.OpenClosed openOrClosed  = (repository.gw.enums.OpenClosed) getValues.get("openOrClosed");
			String	errorMsg = (String) getValues.get("errorMsg");

		new NewDirectBillPayment(driver).makeDirectBillPaymentExecute(payAmount, myPolicyObj.accountNumber);

		acctMenu.clickAccountMenuInvoices();
		acctMenu.clickBCMenuDelinquencies();
        acctDelinquency = new BCCommonDelinquencies(driver);
        Assert.assertTrue(acctDelinquency.verifyDelinquencyStatus(openOrClosed, myPolicyObj.accountNumber,policyNumber, reversalDate), errorMsg);

	}
	

}

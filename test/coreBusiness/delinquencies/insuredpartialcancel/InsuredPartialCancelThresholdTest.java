package coreBusiness.delinquencies.insuredpartialcancel;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.search.BCSearchAccounts;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.PaymentReturnedPaymentReason;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.BillingCenterHelper;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GeneratePolicyHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class InsuredPartialCancelThresholdTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj ;
	private String lienholderNumber = null;
	private String lienholderLoanNumber = null;
	private double lienholderLoanPremiumAmount;
	private String policyNumber ;
	
	private BCAccountMenu acctMenu;
	private BCCommonDelinquencies  acctDelinquency;
	private double orgDelinquentAmount;
	private Date reversalDate;

	private double downPayment;
	

	private void gotoAccount(String accountNumber){
		BCSearchAccounts search = new BCSearchAccounts(driver);
		search.searchAccountByAccountNumber(accountNumber);	
	}

	@Test 
	public void generatePolicy() throws Exception {
		ArrayList<GenerateContact> generateContacts= new ArrayList<>();
		Config cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);

		ArrayList<repository.gw.enums.ContactRole> rolesToAdd = new ArrayList<repository.gw.enums.ContactRole>();
		rolesToAdd.add(repository.gw.enums.ContactRole.Lienholder);

		GenerateContact generateContact = new GenerateContact.Builder(driver)
				.withCompanyName("LH PartialLienBilled")
				.withRoles(rolesToAdd)
				.withGeneratedLienNumber(true)
				.withUniqueName(true)
				.build(GenerateContactType.Company);

		generateContacts.add(generateContact);

		driver.get(cf.getUrlOfCenter(ApplicationOrCenter.PolicyCenter));

		myPolicyObj = Calendar.getInstance().get(Calendar.WEEK_OF_MONTH) % 2 == 0 ?
				new GeneratePolicyHelper(driver).generateInsuredAndLienBilledSquirePolicy("InsuredPayer", "ThresholdTest",null, repository.gw.enums.PaymentPlanType.Annual, repository.gw.enums.PaymentType.Cash,generateContacts):
				new GeneratePolicyHelper(driver).generateInsuredAndLienBilledBOPPolicy("ThresholdTestIns",null, repository.gw.enums.PaymentPlanType.Annual, repository.gw.enums.PaymentType.Cash , generateContacts);

		policyNumber = new BillingCenterHelper(driver).getPolicyNumberFromGeneratePolicyObject(myPolicyObj);


		if(myPolicyObj.productType == repository.gw.enums.ProductLineType.Businessowners){
			lienholderNumber = myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
			lienholderLoanNumber = myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLoanContractNumber();
			lienholderLoanPremiumAmount = myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();
			downPayment = myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount();
		} else {
			lienholderNumber = myPolicyObj.squire.propertyAndLiability.locationList.get(1).getPropertyList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
			lienholderLoanNumber = myPolicyObj.squire.propertyAndLiability.locationList.get(1).getPropertyList().get(0).getAdditionalInterestList().get(0).getLoanContractNumber();
			lienholderLoanPremiumAmount = myPolicyObj.squire.propertyAndLiability.locationList.get(1).getPropertyList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();
			downPayment = myPolicyObj.squire.getPremium().getDownPaymentAmount();

		}
	}

	@Test(dependsOnMethods = { "generatePolicy" })
	public void payDownPayments_reversePaymentAndCheckForDelinquency() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new BillingCenterHelper(driver).loginAsRandomARUserAndVerifyIssuancePolicyPeriod(myPolicyObj);
		
		new NewDirectBillPayment(driver).makeInsuredDownpayment(myPolicyObj, downPayment , policyNumber);
		repository.gw.helpers.ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 25);

		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);
		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);

		// Make payment on LH invoice
		gotoAccount(lienholderNumber);
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
		new NewDirectBillPayment(driver).makeDirectBillPaymentExecute(policyNumber, lienholderLoanNumber, lienholderLoanPremiumAmount);

		repository.gw.helpers.ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 30);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);

		repository.gw.helpers.ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 10);

		// go reverse the payment to trigger insured DEL
		gotoAccount( myPolicyObj.accountNumber);

		acctMenu.clickAccountMenuPayments();

		reversalDate= DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
		new AccountPayments(driver).reversePaymentAtFault(null,downPayment, null, null, PaymentReturnedPaymentReason.AccountFrozen);

		// verify Delinquency
		acctMenu.clickAccountMenuInvoices();
		acctMenu.clickBCMenuDelinquencies();
        acctDelinquency = new BCCommonDelinquencies(driver);
		Assert.assertTrue(acctDelinquency.verifyDelinquencyByReason(repository.gw.enums.OpenClosed.Open, repository.gw.enums.DelinquencyReason.PastDuePartialCancel, myPolicyObj.accountNumber, reversalDate), "Delinquency does not exist Which is not Expected");
        orgDelinquentAmount = acctDelinquency.getOriginalDelinquentAmount(repository.gw.enums.OpenClosed.Open, myPolicyObj.accountNumber, policyNumber, reversalDate);
	}
	

	@Test( dependsOnMethods = { "payDownPayments_reversePaymentAndCheckForDelinquency" } )
    public void payDelinquentAmountAndCheckDelinquency() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);

		new BillingCenterHelper(driver).loginAsRandomARUserAndSearchAccount(myPolicyObj);
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();

		HashMap<String, Object> getValues = new BillingCenterHelper(driver).getVariablesToRunDelinquencyTestScenarios(orgDelinquentAmount);
			//getting values from HashMap
			double payAmount = (double) getValues.get("payAmount");
			repository.gw.enums.OpenClosed openOrClosed = (repository.gw.enums.OpenClosed) getValues.get("openOrClosed");
			String errorMsg = (String) getValues.get("errorMsg");

        new NewDirectBillPayment(driver).makeDirectBillPaymentExecute(payAmount, policyNumber);

		acctMenu.clickAccountMenuInvoices();
		acctMenu.clickBCMenuDelinquencies();
        acctDelinquency = new BCCommonDelinquencies(driver);
        Assert.assertTrue(acctDelinquency.verifyDelinquencyStatus(openOrClosed, myPolicyObj.accountNumber,policyNumber, reversalDate), errorMsg);

	}
	
}


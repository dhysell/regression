package coreBusiness.delinquencies.lienholderfullcancel;

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



public class LHFullCancelThresholdTest extends BaseTest {

	private WebDriver driver;
	private GeneratePolicy myPolicyObj;

	private String lienholderNumber = null;
	private String lienholderLoanNumber = null;
	private double lienholderLoanPremiumAmount ;
	
	private BCAccountMenu acctMenu;
	private BCCommonDelinquencies  acctDelinquency;
	private double orgDelinquentAmount ;
	private Date reversalDate;

	private String  policyNumber;
	
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

		driver.get(cf.getUrlOfCenter(ApplicationOrCenter.PolicyCenter));


		myPolicyObj = Calendar.getInstance().get(Calendar.WEEK_OF_MONTH) % 2 == 0 ? new GeneratePolicyHelper(driver).generateFullyLienBilledBOPPolicy("LHPastDue LienPar",null, repository.gw.enums.PaymentPlanType.Annual, repository.gw.enums.PaymentType.Cash , generateContacts):
				new GeneratePolicyHelper(driver).generateFullyLienBilledSquirePLPolicy("LHPastDue","LienPar",null , repository.gw.enums.PaymentPlanType.Annual, repository.gw.enums.PaymentType.Cash , generateContacts);
		
		policyNumber = new BillingCenterHelper(driver).getPolicyNumberFromGeneratePolicyObject(myPolicyObj);

		if(myPolicyObj.productType == repository.gw.enums.ProductLineType.Businessowners) {
			lienholderNumber = myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
			lienholderLoanNumber = myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLoanContractNumber();
			lienholderLoanPremiumAmount = myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();
		} else {
			lienholderNumber = myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
			lienholderLoanNumber = myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLoanContractNumber();
			lienholderLoanPremiumAmount = myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();
		}
	}
	
	@Test(dependsOnMethods = { "generate" })
	public void payLienholderDown_reversePaymentMadeAndCheckForDelinquency() throws Exception {

		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new BillingCenterHelper(driver).loginAsRandomARUserAndVerifyIssuancePolicyPeriod(myPolicyObj);

		repository.gw.helpers.ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 25);
		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);

        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        new NewDirectBillPayment(driver).makeDirectBillPaymentExecute(policyNumber, lienholderLoanNumber, lienholderLoanPremiumAmount);

		repository.gw.helpers.ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 15);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);

		acctMenu.clickAccountMenuPayments();

		reversalDate= DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
		new AccountPayments(driver).reversePaymentAtFault(null,lienholderLoanPremiumAmount, null, null, PaymentReturnedPaymentReason.AccountFrozen);

		gotoAccount(myPolicyObj.accountNumber);
		acctMenu.clickBCMenuDelinquencies();

		acctDelinquency = new BCCommonDelinquencies(driver);
		Assert.assertTrue(acctDelinquency.verifyDelinquencyByReason(repository.gw.enums.OpenClosed.Open, repository.gw.enums.DelinquencyReason.PastDueLienPartialCancel, lienholderNumber, reversalDate), "Delinquency does not exist Which is not Expected");

        orgDelinquentAmount = acctDelinquency.getOriginalDelinquentAmount(repository.gw.enums.OpenClosed.Open, lienholderNumber, policyNumber, reversalDate);
		repository.gw.helpers.ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 10);

	}
	
	@Test( dependsOnMethods = { "payLienholderDown_reversePaymentMadeAndCheckForDelinquency" } )
    public void payDelinquentAmountAndCheckDelinquencyOnInsured() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);

		new BillingCenterHelper(driver).loginAsRandomARUser();
		gotoAccount(lienholderNumber);

        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();


		HashMap<String, Object> getValues = new BillingCenterHelper(driver).getVariablesToRunDelinquencyTestScenarios(orgDelinquentAmount);
		//getting values from HashMap
			double payAmount = (double) getValues.get("payAmount");
			repository.gw.enums.OpenClosed openOrClosed = (repository.gw.enums.OpenClosed) getValues.get("openOrClosed");
			String errorMsg = (String) getValues.get("errorMsg");

		new NewDirectBillPayment(driver).makeLienHolderPaymentExecute(payAmount, policyNumber, lienholderLoanNumber);
		
		gotoAccount(myPolicyObj.accountNumber);
		
		acctMenu.clickBCMenuDelinquencies();
        acctDelinquency = new BCCommonDelinquencies(driver);
        Assert.assertTrue(acctDelinquency.verifyDelinquencyStatus(openOrClosed, lienholderNumber, policyNumber, reversalDate), errorMsg);

	}

}

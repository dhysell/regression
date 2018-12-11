package regression.r2.clock.billingcenter.cancel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.search.BCSearchAccounts;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.Cancellation.CancellationStatus;
import repository.gw.enums.ChargeCategory;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.ChargeObject;
import repository.pc.policy.PolicySummary;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
public class TestLHPartialCancelBOP extends BaseTest {
	private WebDriver driver;
	private ARUsers arUser = new ARUsers();
	private ArrayList<PolicyLocation> locationsList = new ArrayList<>();
	private ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<>();
	private ArrayList<PolicyLocationBuilding> locTwoBuildingList = new ArrayList<>();
	private ArrayList<PolicyLocationBuilding> locThreeBuildingList = new ArrayList<>();
	private GeneratePolicy myPolicyObj = null;
	private List<ChargeObject> chargesList = null;
	private double delinquentAmount;

	@Test
	public void generatePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		int randomYear = DateUtils.getCenterDateAsInt(cf, ApplicationOrCenter.PolicyCenter, "yyyy") - NumberUtils.generateRandomNumberInt(0, 50);

		PolicyBusinessownersLineAdditionalCoverages myLineAddCov = new PolicyBusinessownersLineAdditionalCoverages(false, true);
		PolicyBusinessownersLine myline = new PolicyBusinessownersLine(SmallBusinessType.StoresRetail);
		myLineAddCov.setEquipmentBreakdownCoverage(true);
		myline.setAdditionalCoverageStuff(myLineAddCov);

		ArrayList<AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<>();
		ArrayList<AdditionalInterest> loc3Bldg1AdditionalInterests = new ArrayList<>();

		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setYearBuilt(randomYear);
		loc1Bldg1.setClassClassification("storage");

		this.locOneBuildingList.add(loc1Bldg1);

		PolicyLocationBuilding loc2Bldg1 = new PolicyLocationBuilding();
		loc2Bldg1.setYearBuilt(randomYear);
		loc2Bldg1.setClassClassification("storage");

		AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc2Bldg1AddInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
		loc2Bldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);
		loc2Bldg1.setAdditionalInterestList(loc2Bldg1AdditionalInterests);

		this.locTwoBuildingList.add(loc2Bldg1);
		this.locationsList.add(new PolicyLocation(new AddressInfo(), this.locOneBuildingList));
		this.locationsList.add(new PolicyLocation(new AddressInfo(), this.locTwoBuildingList));

		PolicyLocationBuilding loc3Bldg1 = new PolicyLocationBuilding();
		loc3Bldg1.setYearBuilt(randomYear);
		loc3Bldg1.setClassClassification("storage");

		AdditionalInterest loc3Bldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc3Bldg1AddInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
		loc3Bldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc3Bldg1AdditionalInterests.add(loc3Bldg1AddInterest);
		loc3Bldg1.setAdditionalInterestList(loc3Bldg1AdditionalInterests);

		this.locThreeBuildingList.add(loc3Bldg1);
		this.locationsList.add(new PolicyLocation(new AddressInfo(), this.locOneBuildingList));
		this.locationsList.add(new PolicyLocation(new AddressInfo(), this.locTwoBuildingList));
		this.locationsList.add(new PolicyLocation(new AddressInfo(), this.locThreeBuildingList));

        myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("Partial Lien Cancel")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(myline)
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}

	@Test(dependsOnMethods = { "generatePolicy" })
	public void makeInsuredDownPayment() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), myPolicyObj.busOwnLine.getPolicyNumber());
	}

	@Test(dependsOnMethods = { "makeInsuredDownPayment" })
	public void moveClocks() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Month, 1);
	}

	@Test(dependsOnMethods = { "moveClocks" })
	public void runInvoiceDueAndCheckDelinquency() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.busOwnLine.locationList.get(1).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber());

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		
		//Only paying 20% of the lienholder invoice to have an amount paid, but not enough so that delinquency is still triggered.
		double lienholderAmountPaid = myPolicyObj.busOwnLine.locationList.get(1).getBuildingList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount() * .20;

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPolicyNumber(), myPolicyObj.busOwnLine.locationList.get(1).getBuildingList().get(0).getAdditionalInterestList().get(0).getLoanContractNumber(), lienholderAmountPaid);

        BCSearchAccounts accountSearch = new BCSearchAccounts(driver);
		accountSearch.searchAccountByAccountNumber(myPolicyObj.accountNumber);
		
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickBCMenuDelinquencies();

		Date targetDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		boolean delinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Open, myPolicyObj.busOwnLine.locationList.get(1).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber(), null, targetDate);

		if (!delinquencyFound) {
			Assert.fail("The Delinquency was either non-existent or not in an 'open' status.");
		}

        this.delinquentAmount = delinquency.getDelinquentAmount(OpenClosed.Open, null, myPolicyObj.busOwnLine.getPolicyNumber(), DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter));
	}
	
	@Test(dependsOnMethods = { "runInvoiceDueAndCheckDelinquency" })
	public void verifyPartialNonPayCancelInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ARUsers arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.busOwnLine.getPolicyNumber());
        PolicySummary summaryPage = new PolicySummary(driver);
		boolean partialNonPayCancelFound = summaryPage.verifyPendingCancellation(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 20), CancellationStatus.Cancelling, true, this.delinquentAmount, 120);
		if (!partialNonPayCancelFound) {
			Assert.fail("The policy did not get a Partial Nonpay Cancellation activity from BC after waiting for 2 minutes.");
		}
	}
	
	@Test(dependsOnMethods = { "verifyPartialNonPayCancelInPolicyCenter" })
    public void moveClocks2() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 20);
	}

	@Test(dependsOnMethods = { "moveClocks2" })
	public void removeLienholderCoveragesInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ARUsers arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());

		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Partial_Nonpay_Cancel);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Partial_Nonpay_Cancel_Documents);
	}

	@Test(dependsOnMethods = { "removeLienholderCoveragesInPolicyCenter" })
    public void verifyChargesInBillingCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();


        AccountCharges accountCharges = new AccountCharges(driver);
//		List<ChargeObject> chargesList = accountCharges.getChargesTable().getSize().
		for (int i = 0; i < chargesList.size(); i++) {
			String payerNumber;
			if (chargesList.get(i).getLinePayer().equalsIgnoreCase("Insured")) {
				payerNumber = myPolicyObj.accountNumber;
			} else {
				payerNumber = chargesList.get(i).getLinePayer();
			}
			try {
                accountCharges.getChargesOrChargeHoldsPopupTableRow(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), payerNumber, null, ChargeCategory.Premium, TransactionType.Policy_Change, null, null, null, myPolicyObj.busOwnLine.getPolicyNumber(), chargesList.get(i).getLineAmount(), null, null, null, null, null, null);
			} catch (Exception e) {
				Assert.fail("The charge from the Lienholder Partial cancel for the account " + payerNumber + " in the amount of " + chargesList.get(i).getLineAmount() + " was not found in the charges table. Test failed.");
			}
		}
	}
}
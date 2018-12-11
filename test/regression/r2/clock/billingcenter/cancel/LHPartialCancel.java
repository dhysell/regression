package regression.r2.clock.billingcenter.cancel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
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
import repository.gw.helpers.GuidewireHelpers;
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

@QuarantineClass
public class LHPartialCancel extends BaseTest {
	private WebDriver driver;
	private ArrayList<PolicyLocation> locationsList = new ArrayList<>();
	private ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<>();
	private ArrayList<PolicyLocationBuilding> locTwoBuildingList = new ArrayList<>();
	private GeneratePolicy myPolicyObj = null;
	private List<ChargeObject> chargesList = null;
	private double delinquentAmount = 0.0 ;

	@Test
	public void generatePolicy() throws Exception {

		int randomYear = DateUtils.getCenterDateAsInt(driver, ApplicationOrCenter.PolicyCenter, "yyyy") - NumberUtils.generateRandomNumberInt(0, 50);

		PolicyBusinessownersLineAdditionalCoverages myLineAddCov = new PolicyBusinessownersLineAdditionalCoverages(false, true);
		PolicyBusinessownersLine myline = new PolicyBusinessownersLine(SmallBusinessType.StoresRetail);
		myLineAddCov.setEquipmentBreakdownCoverage(true);
		myline.setAdditionalCoverageStuff(myLineAddCov);

		ArrayList<AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<>();

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

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

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
		billingCenterLoginAndFindPolicy();

		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), myPolicyObj.busOwnLine.getPolicyNumber());
		
		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "makeInsuredDownPayment" })
	public void moveClocks() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Month, 1);
	}

	@Test(dependsOnMethods = { "moveClocks" })
	public void runInvoiceDueAndCheckDelinquency() throws Exception {
		billingCenterLoginAndFindPolicy();

		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);

		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);

		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);

        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickBCMenuDelinquencies();

		Date targetDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		boolean delinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Open, myPolicyObj.busOwnLine.locationList.get(1).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber(), null, targetDate);

		if (!delinquencyFound) {
			Assert.fail("The Delinquency was either non-existant or not in an 'open' status.");
		}

        delinquentAmount = delinquency.getDelinquentAmount(OpenClosed.Open, null, myPolicyObj.busOwnLine.getPolicyNumber(), DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter));

		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "runInvoiceDueAndCheckDelinquency" })
	public void verifyPartialNonPayCancelInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ARUsers arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        PolicySummary polSummaryPage = new PolicySummary(driver);
        boolean pendingCancellationFound = polSummaryPage.verifyPendingCancellation(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 20), CancellationStatus.Cancelling, true, delinquentAmount, 120);
		
		if (!pendingCancellationFound) {
			Assert.fail("The policy did not get any Pending Cancellation after waiting for 2 minutes.");
		}
		
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 20);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Partial_Nonpay_Cancel);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Partial_Nonpay_Cancel_Documents);
		
		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "verifyPartialNonPayCancelInPolicyCenter" })
	public void removeLienholderCoveragesInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		ARUsers arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.busOwnLine.getPolicyNumber());

        PolicySummary polSummaryPage = new PolicySummary(driver);
		boolean automatedNonPayCancellationOrPolicyChangeFound  = polSummaryPage.verifyPolicyTransaction(null,DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), null, TransactionType.Policy_Change, null, 120);
		
		if (!automatedNonPayCancellationOrPolicyChangeFound) {
			Assert.fail("The policy did not get automated policy change after waiting for 2 minutes.");
		}
						
		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "removeLienholderCoveragesInPolicyCenter" })
	public void verifyChargesInBillingCenter() throws Exception {
		billingCenterLoginAndFindPolicy();

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();


        AccountCharges accountCharges = new AccountCharges(driver);
		for (int i = 0; i < this.chargesList.size(); i++) {
			String payerNumber = null;
			if (this.chargesList.get(i).getLinePayer().equalsIgnoreCase("Insured")) {
				payerNumber = myPolicyObj.accountNumber;
			} else {
				payerNumber = this.chargesList.get(i).getLinePayer();
			}
			try {
                accountCharges.getChargesOrChargeHoldsPopupTableRow(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), payerNumber, null, ChargeCategory.Premium, TransactionType.Policy_Change, null, null, null, myPolicyObj.busOwnLine.getPolicyNumber(), chargesList.get(i).getLineAmount(), null, true, null, null, null, null);
								
			} catch (Exception e) {
				Assert.fail("The charge from the Lienholder Partial cancel for the account " + payerNumber + " in the amount of " + this.chargesList.get(i).getLineAmount() + " was not found in the charges table. Test failed.");
			}
		}
	}

	private void billingCenterLoginAndFindPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);

		ARUsers arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
	}

}
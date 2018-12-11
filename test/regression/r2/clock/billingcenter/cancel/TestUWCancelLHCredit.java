package regression.r2.clock.billingcenter.cancel;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPaymentsCreditDistributions;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
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
import repository.pc.workorders.cancellation.StartCancellation;
@QuarantineClass
public class TestUWCancelLHCredit extends BaseTest {
	private WebDriver driver;
	private ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
	private ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
	private ArrayList<PolicyLocationBuilding> locTwoBuildingList = new ArrayList<PolicyLocationBuilding>();
	private GeneratePolicy myPolicyObj = null;

	@Test
	public void generatePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		int randomYear = DateUtils.getCenterDateAsInt(cf, ApplicationOrCenter.PolicyCenter, "yyyy")
				- NumberUtils.generateRandomNumberInt(0, 50);

		PolicyBusinessownersLineAdditionalCoverages myLineAddCov = new PolicyBusinessownersLineAdditionalCoverages(
				false, true);
		PolicyBusinessownersLine myline = new PolicyBusinessownersLine(SmallBusinessType.StoresRetail);
		myline.setAdditionalCoverageStuff(myLineAddCov);

		ArrayList<AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();

		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setYearBuilt(randomYear);
		loc1Bldg1.setClassClassification("storage");

		this.locOneBuildingList.add(loc1Bldg1);

		PolicyLocationBuilding loc2Bldg1 = new PolicyLocationBuilding();
		loc2Bldg1.setYearBuilt(randomYear);
		loc2Bldg1.setClassClassification("storage");

		AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(
				ContactSubType.Company);
		loc2Bldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);
		loc2Bldg1.setAdditionalInterestList(loc2Bldg1AdditionalInterests);

		this.locTwoBuildingList.add(loc2Bldg1);
		this.locationsList.add(new PolicyLocation(new AddressInfo(true), this.locOneBuildingList));
		this.locationsList.add(new PolicyLocation(new AddressInfo(true), this.locTwoBuildingList));

        myPolicyObj = new GeneratePolicy.Builder(driver).withCreateNew(CreateNew.Create_New_Only_If_Does_Not_Exist)
				.withInsPersonOrCompany(ContactSubType.Company).withInsCompanyName("UW CancelLH")
				.withPolOrgType(OrganizationType.Partnership).withBusinessownersLine(myline)
				.withPolicyLocations(locationsList).withPaymentPlanType(PaymentPlanType.Semi_Annual)
				.withDownPaymentType(PaymentType.Cash).build(GeneratePolicyType.PolicyIssued);
	}

	@Test(dependsOnMethods = { "generatePolicy" })
	public void makeInsuredDownPayment() throws Exception {
		billingCenterLoginAndFindPolicy(myPolicyObj.accountNumber);

		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);

		if (!(myPolicyObj.downPaymentType == PaymentType.ACH_EFT)
				|| !(myPolicyObj.downPaymentType == PaymentType.Credit_Debit)) {
            BCAccountMenu accountMenu = new BCAccountMenu(driver);
            accountMenu.clickAccountMenuActionsNewDirectBillPayment();

            NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
            newPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount());

			new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);
		}
		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "makeInsuredDownPayment" })
	public void moveClocks() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Month, 1);
	}

	@Test(dependsOnMethods = { "moveClocks" })
    public void makeLienHolderDownPayment() throws Exception {
		billingCenterLoginAndFindPolicy(myPolicyObj.busOwnLine.locationList.get(1).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber());

		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);

		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPolicyNumber(), myPolicyObj.busOwnLine.locationList.get(1).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber(), myPolicyObj.busOwnLine.getPremium().getTotalAdditionalInterestPremium());

		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "makeLienHolderDownPayment" })
    public void moveClocks2() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Month, 2);
	}

	@Test(dependsOnMethods = { "moveClocks2" })
    public void uwCancelPolicyInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchPolicyByAccountNumber("sbrunson", "gw", myPolicyObj.accountNumber);

        StartCancellation policyCancel = new StartCancellation(driver);
		policyCancel.cancelPolicy(CancellationSourceReasonExplanation.PoorLossHistory, "UW Cancel", null, true);

		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "uwCancelPolicyInPolicyCenter" })
    public void verifyLienHolderCharges() throws Exception {
		billingCenterLoginAndFindPolicy(myPolicyObj.busOwnLine.locationList.get(1).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber());

        BCAccountMenu sideMenu = new BCAccountMenu(driver);
		sideMenu.clickAccountMenuPayments();
        sideMenu.clickAccountMenuPaymentsCreditDistributions();

		AccountPaymentsCreditDistributions creditDistributions = new AccountPaymentsCreditDistributions(driver);
		creditDistributions.clickTableRowByPaymentDate(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter));

		try {
			//creditDistributions.clickSuspenseItemsTab();
		} catch (Exception e) {
			Assert.fail("The Suspense Items tab is not on the Credit Distributions page. This indicates that this test has failed. Please investigate.");
		}
	}

    private void billingCenterLoginAndFindPolicy(String accountNumber) throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber("sbrunson", "gw", accountNumber);
	}
}

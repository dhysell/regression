package regression.r2.clock.billingcenter.invoicing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.bc.common.BCCommonDelinquencies;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ReinstateReason;
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
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.reinstate.StartReinstate;
@QuarantineClass
public class TestEarnedPremiumReversal extends BaseTest {
	private WebDriver driver;
	private ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
	private ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
	private GeneratePolicy myPolicyObj = null;
	private Date targetDate = null;
	private double delinquentAmount = 0;

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

		ArrayList<AdditionalInterest> loc1Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();

		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setYearBuilt(randomYear);
		loc1Bldg1.setClassClassification("storage");

		this.locOneBuildingList.add(loc1Bldg1);

		AdditionalInterest loc1Bldg1AddInterest = new AdditionalInterest(
				ContactSubType.Company);
		loc1Bldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_All);
		loc1Bldg1AdditionalInterests.add(loc1Bldg1AddInterest);
		loc1Bldg1.setAdditionalInterestList(loc1Bldg1AdditionalInterests);

		this.locationsList.add(new PolicyLocation(new AddressInfo(), this.locOneBuildingList));

        myPolicyObj = new GeneratePolicy.Builder(driver).withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Company).withInsCompanyName("Earned Premium Test")
				.withPolOrgType(OrganizationType.Partnership).withBusinessownersLine(myline)
				.withPolicyLocations(locationsList).withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Cash).build(GeneratePolicyType.PolicyIssued);
	}

	@Test(dependsOnMethods = { "generatePolicy" })
	public void moveClocks() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
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

		this.targetDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		boolean delinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Open, myPolicyObj.busOwnLine.locationList.get(0)
				.getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber(), null, targetDate);

		if (!delinquencyFound) {
			Assert.fail("The Delinquency was either non-existant or not in an 'open' status.");
		}

		this.delinquentAmount = delinquency.getDelinquentAmount(OpenClosed.Open, myPolicyObj.busOwnLine.locationList.get(0)
				.getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber(), null, targetDate);

		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "runInvoiceDueAndCheckDelinquency" })
    public void changePayerAddress() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchPolicyByAccountNumber("sbrunson", "gw", myPolicyObj.accountNumber);

        StartPolicyChange policyChange = new StartPolicyChange(driver);
		editAdditionalInterest(1, 1, myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0)
				.getAdditionalInterestList().get(0).getLienholderNameFromPolicyCenter(), null);
	}
	
	public void editAdditionalInterest(int locationNumber, int buildingNumber, String originalAdditionalInterestName, Date effectiveDate) throws Exception {
		StartPolicyChange policyChange = new StartPolicyChange(driver);
		if (effectiveDate == null) {
			policyChange.startPolicyChange("Edit Current Additional Interest", null);
        } else {
        	policyChange.startPolicyChange("Edit Current Additional Interest", effectiveDate);
        }

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuBuildings();
        GenericWorkorderBuildings buildings = new GenericWorkorderBuildings(driver);
        buildings.clickBuildingsLocationsRow(locationNumber);
        buildings.clickBuildingsBuildingEdit(buildingNumber);

        GenericWorkorderAdditionalInterests buildingAdditionalInterests = new GenericWorkorderAdditionalInterests(driver);
        buildingAdditionalInterests.clickBuildingsPropertyAdditionalInterestsLink(originalAdditionalInterestName);
        buildingAdditionalInterests.changePropertyAdditionalInterestAddressListing();
        buildingAdditionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();

        buildings = new GenericWorkorderBuildings(driver);
        buildings.clickOK();

        policyChange.quoteAndIssue();
    }

	@Test(dependsOnMethods = { "changePayerAddress" })
    public void cancelPolicyInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchPolicyByAccountNumber("sbrunson", "gw", myPolicyObj.accountNumber);

        StartCancellation cancellation = new StartCancellation(driver);
		cancellation.cancelPolicy(CancellationSourceReasonExplanation.AccountClosed, "Closing Account", null, true,
				this.delinquentAmount);

		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "cancelPolicyInPolicyCenter" })
    public void reinstatePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchPolicyByAccountNumber("sbrunson", "gw", myPolicyObj.accountNumber);

        StartReinstate reinstate = new StartReinstate(driver);
		reinstate.reinstatePolicy(ReinstateReason.Payment_Received, "Just for Cause");

		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "reinstatePolicy" })
    public void verifyEarnedPremiumInBillingCenter() throws Exception {
		billingCenterLoginAndFindPolicy();

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();


        AccountCharges charges = new AccountCharges(driver);
		List<Double> cancellationChargesList = charges.getListOfChargesPerContext(TransactionType.Cancellation);
		System.out.println("Cancellation Charges List:");
		System.out.println(cancellationChargesList);
        charges = new AccountCharges(driver);
		List<Double> reinstatementChargesList = charges.getListOfChargesPerContext(TransactionType.Reinstatement);
		System.out.println("Reinstatement Charges List:");
		System.out.println(reinstatementChargesList);

        charges = new AccountCharges(driver);
		List<Double> newReinstatementChargesList = new ArrayList<Double>();
		for (Double reinstatementValue : reinstatementChargesList) {
			Double newReinstatementValue = -reinstatementValue;
			newReinstatementChargesList.add(newReinstatementValue);
		}
		System.out.println("Flipped Reinstatement Charges List:");
		System.out.println(newReinstatementChargesList);

		Assert.assertTrue(
				cancellationChargesList.containsAll(newReinstatementChargesList)
						&& newReinstatementChargesList.containsAll(cancellationChargesList),
				"The amounts listed on the Charges table did not wax on / off as they were supposed to. Test Failed.");
	}

    private void billingCenterLoginAndFindPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchAccountByAccountNumber("sbrunson", "gw", myPolicyObj.accountNumber);
	}

}

package regression.r2.noclock.billingcenter.disbursements;

import java.util.ArrayList;
import java.util.Random;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountDisbursements;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.search.BCSearchAccounts;
import repository.bc.wizards.CreateAccountDisbursementWizard;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DisbursementReason;
import repository.gw.enums.DisbursementStatus;
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
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author bhiltbrand
* @Requirement LH and Insured Disbursements must decrease / cancel if new charges come in.
* @RequirementsLink <a href="https://rally1.rallydev.com/#/68999021356d/detail/userstory/45325851968">Rally Story US5944</a>
* @RequirementsLink <a href="https://rally1.rallydev.com/#/68999021356d/detail/userstory/75231063960">Rally Story US9820</a>
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/07%20-%20Disbursements/07-02%20Insured%20Disbursement%20Requirements.docx">Insured Disbursement Requirements Documentation</a>
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/07%20-%20Disbursements/07-08%20LH%20Account%20Disbursement%20Requirements.docx">LH Disbursement Requirements Documentation</a>
* @Description This test will create a policy with a LH and an Insured. It will then over pay all charges and manually create a disbursement for the LH and the insured.
* It will also create automatic disbursements on both the LH and the Insured. It will then make a premium increase policy change on the LH and the Insured and verify the fallout from that change.
* @DATE Feb 3, 2017
*/
public class TestDecreaseExistingDisbursements extends BaseTest {
	private WebDriver driver;
	private ARUsers arUser = new ARUsers();
	private ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
	private ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
	private GeneratePolicy myPolicyObj = null;
	private boolean approveSystemCreatedDisbursement = getRandomBoolean();
	private boolean payoffEnoughToCancelDisbursement = getRandomBoolean();
	private double extraAmount = 100.00;//Double.valueOf(NumberUtils.generateRandomNumberInt(50, 100));
	private double manualDisbursementAmount = 20.00;
	private AdditionalInterest additionalInterest = null;
	
	private boolean getRandomBoolean() {
		Random rnd = new Random();
		return rnd.nextBoolean();
	}
	
	@Test
	public void generate() throws Exception {		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		int randomYear = DateUtils.getCenterDateAsInt(cf, ApplicationOrCenter.PolicyCenter, "yyyy") - NumberUtils.generateRandomNumberInt(0, 50);

		PolicyBusinessownersLineAdditionalCoverages myLineAddCov = new PolicyBusinessownersLineAdditionalCoverages(false, true);
		PolicyBusinessownersLine myline = new PolicyBusinessownersLine(SmallBusinessType.StoresRetail);
		myline.setAdditionalCoverageStuff(myLineAddCov);

		ArrayList<AdditionalInterest> loc1Bldg2AdditionalInterests = new ArrayList<AdditionalInterest>();

		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setYearBuilt(randomYear);
		loc1Bldg1.setClassClassification("storage");
		loc1Bldg1.setUsageDescription("Insured Building");

		PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
		loc1Bldg2.setYearBuilt(randomYear);
		loc1Bldg2.setClassClassification("storage");
		loc1Bldg2.setUsageDescription("LH Building");

		AdditionalInterest loc1Bldg2AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1Bldg2AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1Bldg2AdditionalInterests.add(loc1Bldg2AddInterest);
		loc1Bldg2.setAdditionalInterestList(loc1Bldg2AdditionalInterests);

		this.locOneBuildingList.add(loc1Bldg1);
		this.locOneBuildingList.add(loc1Bldg2);
		this.locationsList.add(new PolicyLocation(new AddressInfo(), this.locOneBuildingList));

        myPolicyObj = new GeneratePolicy.Builder(driver).withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("Decrease Distribution")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(myline)
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		
		this.additionalInterest = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0);
	}
	
	@Test(dependsOnMethods = { "generate" })	
	public void payAllChargesWithExtraAmount() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);

        NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
        directPayment.makeInsuredDownpayment(this.myPolicyObj, (this.myPolicyObj.busOwnLine.getPremium().getInsuredPremium() + this.extraAmount), this.myPolicyObj.busOwnLine.getPolicyNumber());

		BCSearchAccounts accountSearch = new BCSearchAccounts(driver);
		accountSearch.searchAccountByAccountNumber(this.additionalInterest.getLienholderNumber());

        directPayment = new NewDirectBillPayment(driver);
        directPayment.makeLienHolderPaymentExecute((this.additionalInterest.getAdditionalInterestPremiumAmount() + this.extraAmount), this.myPolicyObj.busOwnLine.getPolicyNumber(), this.additionalInterest.getLoanContractNumber());
		
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "payAllChargesWithExtraAmount" })	
	public void createDisbursements() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuActionsNewTransactionDisbursement();

		CreateAccountDisbursementWizard accountDisbursement = new CreateAccountDisbursementWizard(driver);
        accountDisbursement.createAccountDisbursement(this.myPolicyObj.busOwnLine.getPolicyNumber(), this.myPolicyObj.busOwnLine.getPolicyNumber(), this.manualDisbursementAmount, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DisbursementReason.Overpayment);

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuDisbursements();

		AccountDisbursements disbursements = new AccountDisbursements(driver);
        if (!disbursements.verifyDisbursements(myPolicyObj.busOwnLine.getPolicyNumber(), DisbursementStatus.Approved, this.manualDisbursementAmount)) {
			Assert.fail("The manually created disbursement for the Insured did not appear in the disbursements table in the status expected.");
		}

		BCSearchAccounts accountSearch = new BCSearchAccounts(driver);
		accountSearch.searchAccountByAccountNumber(this.additionalInterest.getLienholderNumber());

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuActionsNewTransactionDisbursement();

		accountDisbursement = new CreateAccountDisbursementWizard(driver);
        accountDisbursement.createAccountDisbursement(this.myPolicyObj.busOwnLine.getPolicyNumber(), this.additionalInterest.getLoanContractNumber(), this.manualDisbursementAmount, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DisbursementReason.Overpayment);

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuDisbursements();

		disbursements = new AccountDisbursements(driver);
		if (!disbursements.verifyDisbursements("Default", DisbursementStatus.Approved, this.manualDisbursementAmount)) {
			Assert.fail("The manually created disbursement for the LienHolder did not appear in the disbursements table in the status expected.");
		}
		
		this.extraAmount = this.extraAmount - this.manualDisbursementAmount;
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "createDisbursements" })	
	public void makePolicyChange1() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.busOwnLine.getPolicyNumber());

        StartPolicyChange policyChange = new StartPolicyChange(driver);
		policyChange.startPolicyChange("change coverage", null);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuBuildings();
		GenericWorkorderBuildings building = new GenericWorkorderBuildings(driver);
		building.clickBuildingsBuildingEdit(1);
		building.setBuildingLimit(this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getBuildingLimit() + 5000);
		building.setBuildingPersonalPropertyLimit(this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getBppLimit() + 5000);
		building.clickOK();
		building.clickBuildingsBuildingEdit(2);
		building.setBuildingLimit(this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getBuildingLimit() + 5000);
		building.setBuildingPersonalPropertyLimit(this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getBppLimit() + 5000);
		building.clickOK();
		policyChange.quoteAndIssue();

		this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).setBuildingLimit(this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getBuildingLimit() + 5000);
		this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).setBuildingLimit(this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getBuildingLimit() + 5000);
		
		this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).setBppLimit(this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getBppLimit() + 5000);
		this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).setBppLimit(this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getBppLimit() + 5000);
		
		new GuidewireHelpers(driver).logout();
	}
		
	@Test(dependsOnMethods = { "makePolicyChange1" })	
	public void verifyThatDisbursementsDidNotChange() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuDisbursements();

		AccountDisbursements disbursements = new AccountDisbursements(driver);
        if (!disbursements.verifyDisbursements(myPolicyObj.busOwnLine.getPolicyNumber(), DisbursementStatus.Approved, this.manualDisbursementAmount)) {
			Assert.fail("The manually created disbursement for the Insured did not appear in the disbursements table in the status expected.");
		}

		BCSearchAccounts accountSearch = new BCSearchAccounts(driver);
		accountSearch.searchAccountByAccountNumber(this.additionalInterest.getLienholderNumber());

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuDisbursements();

		disbursements = new AccountDisbursements(driver);
		if (!disbursements.verifyDisbursements("Default", DisbursementStatus.Approved, this.manualDisbursementAmount)) {
			Assert.fail("The manually created disbursement for the LienHolder did not appear in the disbursements table in the status expected.");
		}
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "verifyThatDisbursementsDidNotChange" })	
	public void createAutomaticDisbursements() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Disbursement);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Automatic_Disbursement);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.FBM_Lienholder_Automatic_Disbursement);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();

		AccountCharges charges = new AccountCharges(driver);
		if (charges.waitUntilChargesFromTransactionDescriptionArrive(120, "change coverage")) {
			this.extraAmount = this.extraAmount - charges.getChargeAmount(this.myPolicyObj.accountNumber, "change coverage");
		} else {
			Assert.fail("The charges from the first policy change didn't come over after waiting for 2 minutes. Test cannot continue.");
		}

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuDisbursements();


		AccountDisbursements disbursements = new AccountDisbursements(driver);
		WebElement disbursementRow = null;
		try {
            disbursementRow = disbursements.getDisbursementsTableRow(null, null, null, this.myPolicyObj.busOwnLine.getPolicyNumber(), DisbursementStatus.Awaiting_Approval, null, this.extraAmount, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("The automatically created disbursement for the Insured did not appear in the disbursements table in the status expected.");
		}
		
		if (this.approveSystemCreatedDisbursement) {			
			disbursementRow.click();
			disbursements.clickAccountDisbursementsEdit();
			disbursements.clickAccountDisbursementsApprove();
		}

		BCSearchAccounts accountSearch = new BCSearchAccounts(driver);
		accountSearch.searchAccountByAccountNumber(this.additionalInterest.getLienholderNumber());

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuDisbursements();


		disbursements = new AccountDisbursements(driver);
		WebElement lienHolderDisbursementRow = null;
		try {
			lienHolderDisbursementRow = disbursements.getDisbursementsTableRow(null, null, null, "Default", DisbursementStatus.Awaiting_Approval, null, this.extraAmount, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("The automatically created disbursement for the Lienholder did not appear in the disbursements table in the status expected.");
		}
		
		if (this.approveSystemCreatedDisbursement) {			
			lienHolderDisbursementRow.click();
			disbursements.clickAccountDisbursementsEdit();
			disbursements.clickAccountDisbursementsApprove();
		}
		
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "createAutomaticDisbursements" })	
	public void makePolicyChange2() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.busOwnLine.getPolicyNumber());

        StartPolicyChange policyChange = new StartPolicyChange(driver);
		policyChange.startPolicyChange("change coverage 2", null);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuBuildings();
		GenericWorkorderBuildings building = new GenericWorkorderBuildings(driver);
		building.clickBuildingsBuildingEdit(1);
		double amountExtra = 0.00;
		if (this.payoffEnoughToCancelDisbursement) {
			amountExtra = 100000.00;
		} else {
			amountExtra = 5000.00;
		}
		building.setBuildingLimit(this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getBuildingLimit() + amountExtra);
		building.setBuildingPersonalPropertyLimit(this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getBppLimit() + amountExtra);
		building.clickOK();
		building.clickBuildingsBuildingEdit(2);
		building.setBuildingLimit(this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getBuildingLimit() + amountExtra);
		building.setBuildingPersonalPropertyLimit(this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getBppLimit() + amountExtra);
		building.clickOK();
		policyChange.quoteAndIssue();

		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "makePolicyChange2" })	
	public void verifyFinalDisbursements() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();

		AccountCharges charges = new AccountCharges(driver);
		if (charges.waitUntilChargesFromTransactionDescriptionArrive(120, "change coverage 2")) {
			this.extraAmount = this.extraAmount - charges.getChargeAmount(this.myPolicyObj.accountNumber, "change coverage 2");
		} else {
			Assert.fail("The charges from the second policy change didn't come over after waiting for 2 minutes. Test cannot continue.");
		}
		
		if (this.extraAmount < 0) {
			this.extraAmount = 0.00;
		}

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuDisbursements();

		AccountDisbursements disbursement = new AccountDisbursements(driver);
		if (this.payoffEnoughToCancelDisbursement) {
            if (!disbursement.verifyDisbursements(this.myPolicyObj.busOwnLine.getPolicyNumber(), DisbursementStatus.Rejected, null)) {
				Assert.fail("The policy had a policy change with enough to force a rejection of the disbursement. However, the disbursement is still available. Test Failed.");
			}
		} else {
			if (this.approveSystemCreatedDisbursement) {
                if (!disbursement.verifyDisbursements(this.myPolicyObj.busOwnLine.getPolicyNumber(), DisbursementStatus.Approved, this.extraAmount)) {//may switch to awaiting approval and create a new activity
					Assert.fail("The disbursement should have automatically adjusted, but did not. Test Failed.");
				}
			} else {
                if (!disbursement.verifyDisbursements(this.myPolicyObj.busOwnLine.getPolicyNumber(), DisbursementStatus.Awaiting_Approval, this.extraAmount)) {
					Assert.fail("The disbursement should have automatically adjusted, but did not. Test Failed.");
				}
			}
		}

		BCSearchAccounts accountSearch = new BCSearchAccounts(driver);
		accountSearch.searchAccountByAccountNumber(this.additionalInterest.getLienholderNumber());

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuDisbursements();


		disbursement = new AccountDisbursements(driver);
		if (this.payoffEnoughToCancelDisbursement) {
			if (!disbursement.verifyDisbursements("Default", DisbursementStatus.Rejected, this.extraAmount)) {
				Assert.fail("The policy had a policy change with enough to force a rejection of the disbursement. However, the disbursement is still available. Test Failed.");
			}
		} else {
			if (this.approveSystemCreatedDisbursement) {
				if (!disbursement.verifyDisbursements("Default", DisbursementStatus.Approved, this.extraAmount)) {//may switch to awaiting approval and create a new activity
					Assert.fail("The disbursement should have automatically adjusted, but did not. Test Failed.");
				}
			} else {
				if (!disbursement.verifyDisbursements("Default", DisbursementStatus.Awaiting_Approval, this.extraAmount)) {
					Assert.fail("The disbursement should have automatically adjusted, but did not. Test Failed.");
				}
			}
		}
		new GuidewireHelpers(driver).logout();
	}
}

package regression.r2.noclock.billingcenter.charges;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.topmenu.BCTopMenuAccount;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
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
public class PayerAssignmentBOP extends BaseTest {
	private WebDriver driver;
	private ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
	private ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
	private ArrayList<PolicyLocationBuilding> locTwoBuildingList = new ArrayList<PolicyLocationBuilding>();
	private GeneratePolicy myPolicyObj = null;

	@Test
	public void generatePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		int randomYear = DateUtils.getCenterDateAsInt(cf, ApplicationOrCenter.PolicyCenter, "yyyy") - NumberUtils.generateRandomNumberInt(0, 50);

		PolicyBusinessownersLineAdditionalCoverages myLineAddCov = new PolicyBusinessownersLineAdditionalCoverages(false, true);
		PolicyBusinessownersLine myline = new PolicyBusinessownersLine(SmallBusinessType.StoresRetail);
		myline.setAdditionalCoverageStuff(myLineAddCov);

		ArrayList<AdditionalInterest> loc1Bldg2AdditionalInterests = new ArrayList<AdditionalInterest>();
		ArrayList<AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();

		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setYearBuilt(randomYear);
		loc1Bldg1.setClassClassification("storage");
		loc1Bldg1.setUsageDescription("Insured Building");

		PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
		loc1Bldg2.setYearBuilt(randomYear);
		loc1Bldg2.setClassClassification("storage");
		loc1Bldg2.setUsageDescription("1st LH Building");

		AdditionalInterest loc1Bldg2AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1Bldg2AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1Bldg2AdditionalInterests.add(loc1Bldg2AddInterest);
		loc1Bldg2.setAdditionalInterestList(loc1Bldg2AdditionalInterests);

		this.locOneBuildingList.add(loc1Bldg1);
		this.locOneBuildingList.add(loc1Bldg2);

		PolicyLocationBuilding loc2Bldg1 = new PolicyLocationBuilding();
		loc2Bldg1.setYearBuilt(randomYear);
		loc2Bldg1.setClassClassification("storage");
		loc2Bldg1.setUsageDescription("2nd LH Building");

		AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc2Bldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Insured);
		loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);
		loc2Bldg1.setAdditionalInterestList(loc2Bldg1AdditionalInterests);

		this.locTwoBuildingList.add(loc2Bldg1);
		this.locationsList.add(new PolicyLocation(new AddressInfo(), this.locOneBuildingList));
		this.locationsList.add(new PolicyLocation(new AddressInfo(), this.locTwoBuildingList));

		myPolicyObj = new GeneratePolicy.Builder(driver).withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("Payer Assignment")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(myline)
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}

	@Test(dependsOnMethods = { "generatePolicy" })
	public void makePolicyCashDownPayment() throws Exception {
		billingCenterLoginAndFindPolicy();

		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), myPolicyObj.busOwnLine.getPolicyNumber());

		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);
		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "makePolicyCashDownPayment" })
	public void verifyChargesFromOriginalPayers() throws Exception {
		billingCenterLoginAndFindPolicy();

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();

		AccountCharges charges = new AccountCharges(driver);
		boolean found = charges.verifyChargesMatchPayers(myPolicyObj);
		if (!found) {
			Assert.fail("The verification step on the charges page in Billing Center failed. One or more charges did not match what was expected.");
		}

		new GuidewireHelpers(driver).logout();
	}

    private void billingCenterLoginAndFindPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);

		Login loginPage = new Login(driver);
		loginPage.login("sbrunson", "gw");

		BCTopMenuAccount accountTopMenu = new BCTopMenuAccount(driver);
		accountTopMenu.menuAccountSearchAccountByAccountNumber(myPolicyObj.accountNumber);
	}

}

//In order for this Test and Defect to work, the Edit Disbursement Reason permission on the Billing Manager Role.
package regression.r2.clock.billingcenter.disbursements;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountDisbursements;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DisbursementReason;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
public class TestDisbursementEditButton extends BaseTest {
	private WebDriver driver;
	private AccountDisbursements acctDisbmt;
	public GeneratePolicy myPolicyObjOnlyInsured = null;
	private GeneratePolicy myPolicyObj;

	@Test(enabled = true)
	// creating a new insured policy
	public void testBasicIssuanceInsuredOnly() throws Exception {

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		locOneBuildingList.add(new PolicyLocationBuilding());
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("Test Policy")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Monthly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);

        System.out.println(new GuidewireHelpers(driver).getCurrentPolicyType(myPolicyObj).toString());
	}

	@Test(dependsOnMethods = { "testBasicIssuanceInsuredOnly" }, enabled = true)
    public void testCreateDisbursementOnAccount() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		// login as a Clerical
		new Login(driver).loginAndSearchAccountByAccountNumber("asnorris", "gw", myPolicyObj.accountNumber);

		// runbatchprocess invoice
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

		// Pay premium amount and $30.12 extra to create disbursement
        double premiumAmount = myPolicyObj.busOwnLine.getPremium().getInsuredPremium();
		double extraAmount = 30.12;
		double totalAmount = premiumAmount + extraAmount;
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment paymentPage = new NewDirectBillPayment(driver);
        paymentPage.makeDirectBillPaymentExecute(totalAmount, myPolicyObj.busOwnLine.getPolicyNumber());

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Automatic_Disbursement);

		// reaching to the disbursement page
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuDisbursements();
        acctDisbmt = new AccountDisbursements(driver);
		acctDisbmt.clickRowInDisbursementsTableByText(StringsUtils.currencyRepresentationOfNumber(extraAmount));

		// validate that the edit reason box is displayed for clerical
		acctDisbmt.clickAccountDisbursementsEdit();
        acctDisbmt.selectDisbursementReason(DisbursementReason.Cancellation);
	}

	@Test(dependsOnMethods = { "testCreateDisbursementOnAccount" }, enabled = true)
    public void testChangeDisbursementStatus() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber("sbrunson", "gw", myPolicyObj.accountNumber);

		// reaching to the disbursement page
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuDisbursements();
        acctDisbmt = new AccountDisbursements(driver);

		// Change the status to Approved
		acctDisbmt.clickAccountDisbursementsEdit();
        acctDisbmt.clickAccountDisbursementsApprove();
    }

	@Test(dependsOnMethods = { "testChangeDisbursementStatus" }, enabled = true)
    public void testValidateDisbursementReasonBillingManager() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		// login as billing manager
		new Login(driver).loginAndSearchAccountByAccountNumber("sbrunson", "gw", myPolicyObj.accountNumber);

		// reaching to the disbursement page
        BCAccountMenu acntMenu = new BCAccountMenu(driver);
		acntMenu.clickAccountMenuDisbursements();
        acctDisbmt = new AccountDisbursements(driver);

		// Validate that the edit reason box is displaying for Billing Manager -
		// the method is right but the test is going to fail because the
		// functionality is not working properly currently.
		acctDisbmt.clickAccountDisbursementsEdit();
        try {
			acctDisbmt.selectDisbursementReason(DisbursementReason.Cancellation);
		} catch (Exception e) {
			Assert.fail("The selectDisbursementReason combo box should have been editable, but was not. Test failed.");
		}
    }

	@Test(dependsOnMethods = { "testChangeDisbursementStatus" }, enabled = true)
    public void testValidateDisbursementReasonClerical() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		// log in as Clerical
		new Login(driver).loginAndSearchAccountByAccountNumber("asnorris", "gw", myPolicyObj.accountNumber);

		// reaching to the disbursement page
        BCAccountMenu actMenu = new BCAccountMenu(driver);
		actMenu.clickAccountMenuDisbursements();
        acctDisbmt = new AccountDisbursements(driver);

		// Validate that the edit reason box is not displaying for Clerical
		acctDisbmt.clickAccountDisbursementsEdit();

		try {
			acctDisbmt.selectDisbursementReason(DisbursementReason.Other);
		} catch (Exception e) {
			System.out.println("Disbursement reason is not editable" + e.getMessage());
		}
	}

	@Test(dependsOnMethods = { "testValidateDisbursementReasonClerical", "testValidateDisbursementReasonBillingManager" }, enabled = true)
    public void moveClockToChangeStatusToSent() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 20);
	}

	@Test(dependsOnMethods = { "moveClockToChangeStatusToSent" }, enabled = true)
    public void validateSentStatusAndDisbursementReason() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		// login as billing manager
		new Login(driver).loginAndSearchAccountByAccountNumber("sbrunson", "gw", this.myPolicyObj.accountNumber);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Disbursement);

		// reaching to the disbursement page
        BCAccountMenu acntsMenu = new BCAccountMenu(driver);
		acntsMenu.clickAccountMenuDisbursements();
        acctDisbmt = new AccountDisbursements(driver);

		// Validate that the edit reason box is not displaying Billing Manager
		acctDisbmt.clickAccountDisbursementsEdit();
        try {
			acctDisbmt.selectDisbursementReason(DisbursementReason.Other);
		} catch (Exception e) {
			System.out.println("Disbursement reason is not editable" + e.getMessage());
		}
	}
}

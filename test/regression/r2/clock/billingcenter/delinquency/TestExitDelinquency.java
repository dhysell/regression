package regression.r2.clock.billingcenter.delinquency;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.policy.summary.BCPolicySummary;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PolicyTermStatus;
import repository.gw.generate.GeneratePolicy;
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
import repository.pc.account.AccountSummaryPC;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author bhiltbrand
 * @Requirement When a delinquency is exited in Billing Center, it should set the policy status to "In-force".
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/7832667974d/detail/defect/44080906582">Rally Defect</a>
 * @Description This test issues a policy and sends it delinquent. It then manually exits the delinquency in Billing Center.
 * This should set the policy back to an "In-Force" state. The test checks for this.
 * @DATE Oct 7, 2015
 */
@QuarantineClass
public class TestExitDelinquency extends BaseTest {
	private WebDriver driver;
	private ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
	private ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
	private GeneratePolicy myPolicyObj = null;
	private Date targetDate = null;
	private double delinquentAmount = 0;
	private double unbilledAmount = 0;
	private ARUsers arUser;

	@Test
	public void generatePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		int randomYear = DateUtils.getCenterDateAsInt(cf, ApplicationOrCenter.PolicyCenter, "yyyy") - NumberUtils.generateRandomNumberInt(0, 50);

		PolicyBusinessownersLineAdditionalCoverages myLineAddCov = new PolicyBusinessownersLineAdditionalCoverages(false, true);
		PolicyBusinessownersLine myline = new PolicyBusinessownersLine(SmallBusinessType.StoresRetail);
		myline.setAdditionalCoverageStuff(myLineAddCov);

		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setYearBuilt(randomYear);
		loc1Bldg1.setClassClassification("storage");

		this.locOneBuildingList.add(loc1Bldg1);
		this.locationsList.add(new PolicyLocation(new AddressInfo(), this.locOneBuildingList));

        myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("Policy Cancellation")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(myline)
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}

	@Test(dependsOnMethods = { "generatePolicy" })
	public void runInvoiceWithoutMakingDownpayment() throws Exception {
		/*
		 * This method runs the invoice batch process and clears the promised
		 * payment trouble ticket to prepare for going delinquent
		 */
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.busOwnLine.getPolicyNumber());

        BCCommonTroubleTickets troubleTicket = new BCCommonTroubleTickets(driver);
		troubleTicket.closeFirstTroubleTicket();

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "runInvoiceWithoutMakingDownpayment" })
	public void moveClocks() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
	}

	@Test(dependsOnMethods = { "moveClocks" })
	public void runInvoiceDueAndCheckDelinquency() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickBCMenuDelinquencies();

		this.targetDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		boolean delinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Open, myPolicyObj.accountNumber, null, targetDate);

		if (!delinquencyFound) {
			Assert.fail("The Delinquency was either non-existant or not in an 'open' status.");
		}

		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "runInvoiceDueAndCheckDelinquency" })
    public void verifyCancelationCompletionInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        AccountSummaryPC summaryPage = new AccountSummaryPC(driver);
        boolean pendingCancelFound = summaryPage.verifyCancellationInPolicyCenter(myPolicyObj.busOwnLine.getPolicyNumber(), 120);
		if (!pendingCancelFound) {
			Assert.fail("The policy did not get a pending cancellation transaction from BC after waiting for 2 minutes.");
		}

        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 20);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);

        summaryPage = new AccountSummaryPC(driver);
        boolean cancelledStatusFound = summaryPage.verifyPolicyStatusInPolicyCenter(myPolicyObj.busOwnLine.getPolicyNumber(), PolicyTermStatus.Canceled, 300);
		if (!cancelledStatusFound) {
			Assert.fail("The policy had not entered delinquency after 5 minutes of waiting.");
		}

		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "verifyCancelationCompletionInPolicyCenter" })
    public void verifyDelinquencyStepsInBillingCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickBCMenuDelinquencies();

        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		this.delinquentAmount = delinquency.getDelinquentAmount(OpenClosed.Open, myPolicyObj.accountNumber, null, targetDate);
		this.unbilledAmount = delinquency.getUnbilledAmount();

        menu = new BCAccountMenu(driver);
		menu.clickAccountMenuInvoices();

        AccountInvoices invoices = new AccountInvoices(driver);
		invoices.verifyDelinquencyCancellationInvoicingRollup(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), unbilledAmount, delinquentAmount);

		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "verifyDelinquencyStepsInBillingCenter" })
    public void exitDelinquency() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickBCMenuDelinquencies();

        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		delinquency.clickExitDelinquencyButton();
		boolean delinquencyClosed = delinquency.verifyDelinquencyStatus(OpenClosed.Closed, myPolicyObj.accountNumber, null, targetDate);
		if (!delinquencyClosed) {
			Assert.fail("The delinquency did not close after clicking the 'Exit Delinquncy' button.");
		}
		
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "exitDelinquency" })
    public void verifyCancellationStatus() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        BCPolicySummary policySummary = new BCPolicySummary(driver);
		String cancellationStatus = policySummary.getPolicyCancellationStatus();
		if (cancellationStatus.equalsIgnoreCase("Pending Reinstatement")) {
			Assert.fail("The Policy Cancellation Status was set as 'Pending Reinstatement'. This is incorrect and should not be the case. Test Failed.");
		}
	}
}

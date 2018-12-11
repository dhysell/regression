package regression.r2.clock.billingcenter.delinquency;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.hibernate.qa.guidewire.environments.Urls;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.BCDesktopMyActivities;
import repository.bc.desktop.BCDesktopMyQueues;
import repository.bc.topmenu.BCTopMenu;
import repository.bc.topmenu.BCTopMenuAccount;
import repository.driverConfiguration.Config;
import repository.gw.enums.ActivityQueuesBillingCenter;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.CancellationEvent;
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
import persistence.globaldatarepo.helpers.EnvironmentsHelper;
public class DelinquencyDrivenCancellation extends BaseTest {
	private WebDriver driver;
	private ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
	private ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
	private GeneratePolicy myPolicyObj = null;
	private Date targetDate = null;
	private Date tsiDate = null;
	private double delinquentAmount = 0.00;
	private boolean writeOff = getRandomBoolean();
	private String userName;
	private String arUsername;
	private String arPassword;
	private String uwUserName;
	private String accountNumber;
	private String policyNumber;

	public DelinquencyDrivenCancellation() {
	}

	public DelinquencyDrivenCancellation(boolean _writeOff) {
		setWriteOff(_writeOff);
	}

	private void setWriteOff(boolean _writeOff) {
		if (_writeOff == true) {
			writeOff = true;
		} else if (_writeOff == false) {
			writeOff = false;
		} else {
			writeOff = getRandomBoolean();
		}
	}

	private boolean getRandomBoolean() {
		Random rnd = new Random();
		return rnd.nextBoolean();
	}

	// ///////////////////
	// Main Test Methods//
	// ///////////////////

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

		locOneBuildingList.add(loc1Bldg1);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("Policy Cancellation")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(myline)
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Monthly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		
		userName = myPolicyObj.agentInfo.getAgentUserName();
		uwUserName = myPolicyObj.underwriterInfo.getUnderwriterUserName();
		accountNumber = myPolicyObj.accountNumber;
        policyNumber = myPolicyObj.busOwnLine.getPolicyNumber();

		System.out.println("#############\nAccount Number: " + accountNumber);
		System.out.println("Policy Number: " + policyNumber);
		System.out.println("Under Writer: " + uwUserName);
		System.out.println("Agent: " + userName + "\n#############");
		
	}

	@Test(dependsOnMethods = { "generatePolicy" })
	public void runInvoiceWithoutMakingDownpayment() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		ARUsers arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		
		arUsername = arUser.getUserName();
		arPassword = arUser.getPassword();
		
		new Login(driver).loginAndSearchAccountByAccountNumber(arUsername, arPassword, accountNumber);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
	}

	@Test(dependsOnMethods = { "runInvoiceWithoutMakingDownpayment" })
	public void moveClocks() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 11);
	}

	/**
	* @Author bmartin
	* @Requirement DE5693 Not getting a delinquency on a nonpay on Down
	* @RequirementsLink <a href="https://rally1.rallydev.com/#/18670464636d/detail/defect/120752051572">DE5693</a>
	* @Description Did not pay down payment. Check that delinquency showed up after moving 11 days. 
	* @DATE Jun 12, 2017
	* @throws Exception
	*/
	@Test(dependsOnMethods = { "moveClocks" })
	public void runInvoiceDueAndCheckDelinquency() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUsername, arPassword, accountNumber);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Release_Trouble_Ticket_Holds);

        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickBCMenuDelinquencies();

		targetDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		boolean delinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Open, accountNumber, null, targetDate);

		if (!delinquencyFound) {
			Assert.fail("The Delinquency was either non-existant or not in an 'open' status.");
		}

	}

	@Test(dependsOnMethods = { "runInvoiceDueAndCheckDelinquency" })
	public void verifyCancelationCompletionInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUsername, arPassword, accountNumber);

        AccountSummaryPC summaryPage = new AccountSummaryPC(driver);
		boolean pendingCancelFound = summaryPage.verifyCancellationInPolicyCenter(policyNumber, 120);
		if (!pendingCancelFound) {
			Assert.fail("The policy did not get a pending cancellation transaction from BC after waiting for 2 minutes.");
		}

        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 20);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);

        summaryPage = new AccountSummaryPC(driver);
		boolean cancelledStatusFound = summaryPage.verifyPolicyStatusInPolicyCenter(policyNumber, PolicyTermStatus.Canceled, 300);
		if (!cancelledStatusFound) {
			Assert.fail("The policy had not entered delinquency after 5 minutes of waiting.");
		}

	}

	@Test(dependsOnMethods = { "verifyCancelationCompletionInPolicyCenter" })
	public void verifyDelinquencyStepsInBillingCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUsername, arPassword, accountNumber);

        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickBCMenuDelinquencies();

        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		delinquentAmount = delinquency.getDelinquentAmount(OpenClosed.Open, accountNumber, null, targetDate);

        menu = new BCAccountMenu(driver);
		menu.clickAccountMenuInvoices();

        menu = new BCAccountMenu(driver);
		menu.clickBCMenuDelinquencies();

		moveThroughDelinquencySteps();

		if (writeOff == false) {
			boolean found = checkForTSIFile();
			if (!found) {
				Assert.fail("The TSI Collections file was not found.");
			}
		}
	}

	// //////////////////////////////////////////////
	// Private Methods Used to support Test Methods//
	// //////////////////////////////////////////////

    private void moveThroughDelinquencySteps() throws Exception {
		driver.navigate().refresh();
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		boolean delinquencyStepFound = delinquency.verifyDelinquencyEventCompletion(CancellationEvent.CancellationBillingInstructionReceived);

		if (!delinquencyStepFound) {
			Assert.fail("The Delinquency Event 'Cancellation Billing Instruction Received' never triggered.");
		}

		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);

		new BatchHelpers(driver).runBatchProcess(BatchProcess.BC_Workflow);

		driver.navigate().refresh();
        delinquency = new BCCommonDelinquencies(driver);
		delinquencyStepFound = delinquency.verifyDelinquencyEventCompletion(CancellationEvent.SendBalanceDue);

		if (!delinquencyStepFound) {
			Assert.fail("The Delinquency Event 'Send Balance Due' never triggered.");
		}

		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 21);

		new BatchHelpers(driver).runBatchProcess(BatchProcess.BC_Workflow);

		driver.navigate().refresh();
        delinquency = new BCCommonDelinquencies(driver);
		delinquencyStepFound = delinquency.verifyDelinquencyEventCompletion(CancellationEvent.SendSecondBalanceDue);

		if (!delinquencyStepFound) {
			Assert.fail("The Delinquency Event 'Send Second Balance Due' never triggered.");
		}

		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 21);

        delinquency = new BCCommonDelinquencies(driver);
		delinquentAmount = delinquency.getDelinquentAmount(OpenClosed.Open, accountNumber, null, targetDate);

		if (writeOff) {
			writeOffWorkflow();
		} else {
			if (delinquentAmount < 50) {
				System.out.println("Warning: The Write-Off path chosen was to send to TSI, however the Delinquent Amount was under $50.00. The test will continue, but will only Write-Off.");
				writeOffWorkflow();
			} else {
				tsiWorkflow();
			}
		}
	}

	private void makePaymentForWriteOff() {
		double amountToPay = 0;
		if (delinquentAmount > 50) {
			amountToPay = delinquentAmount - 49;
		}

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
		newPayment.makeDirectBillPaymentExecute(amountToPay, policyNumber);

    }

	private void writeOffWorkflow() {
		makePaymentForWriteOff();

		new BatchHelpers(driver).runBatchProcess(BatchProcess.BC_Workflow);

		driver.navigate().refresh();
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		boolean delinquencyStepFound = delinquency.verifyDelinquencyEventCompletion(CancellationEvent.Writeoff);

		if (!delinquencyStepFound) {
			Assert.fail("The Delinquency Event 'Write-Off' never triggered.");
		}

		boolean delinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Closed, accountNumber, null, targetDate);

		if (!delinquencyFound) {
			Assert.fail("The Delinquency finished the workflow, but remained open.");
		}
	}

	private void tsiWorkflow() throws Exception {
		new BatchHelpers(driver).runBatchProcess(BatchProcess.BC_Workflow);

		BCTopMenu topMenu = new BCTopMenu(driver);
		topMenu.clickDesktopTab();

        BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuMyQueues();

        BCDesktopMyQueues desktopQueues = new BCDesktopMyQueues(driver);
		desktopQueues.selectAndAssignReviewDelinquencyActivity(ActivityQueuesBillingCenter.ARSupervisorWesternCommunity, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), "Review Delinquency", accountNumber);

        desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuMyActivities();

		BCDesktopMyActivities myActivities = new BCDesktopMyActivities(driver);
		myActivities.completeActivity(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), "Review Delinquency", accountNumber);

        BCTopMenuAccount accountTopMenu = new BCTopMenuAccount(driver);
		accountTopMenu.menuAccountSearchAccountByAccountNumber(accountNumber);

        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickBCMenuDelinquencies();

        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		boolean delinquencyStepFound = delinquency.verifyDelinquencyEventCompletion(CancellationEvent.ReviewTheDelinquency);

		if (!delinquencyStepFound) {
			Assert.fail("The Delinquency Event 'Review the Delinquency' never triggered.");
		}

		new BatchHelpers(driver).runBatchProcess(BatchProcess.FBM_Write_TSI_FIle_Process);

		driver.navigate().refresh();
		tsiDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);

		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 76);

		new BatchHelpers(driver).runBatchProcess(BatchProcess.BC_Workflow);

		driver.navigate().refresh();
        delinquency = new BCCommonDelinquencies(driver);
		delinquencyStepFound = delinquency.verifyDelinquencyEventCompletion(CancellationEvent.Writeoff);

		if (!delinquencyStepFound) {
			Assert.fail("The Delinquency Event 'Write-Off' never triggered.");
		}

		boolean delinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Closed, accountNumber, null, targetDate);

		if (!delinquencyFound) {
			Assert.fail("The Delinquency finished the workflow, but remained open.");
		}
	}

	private boolean checkForTSIFile() throws Exception {
		boolean found = false;
		String tsiFileLocation = null;
		String tsiFileName = "TSICollections_" + DateUtils.dateFormatAsString("yyyy-MM-dd", tsiDate) + ".csv";
		String server = new Config(ApplicationOrCenter.BillingCenter).getEnv().toUpperCase();
        Urls environmentInfo = new EnvironmentsHelper().getGuideWireEnvironments(server).get(ApplicationOrCenter.BillingCenter);

		switch (environmentInfo.getEnvs().getName()) {
		case "QA":
			tsiFileLocation = "\\\\fbmsqa11\\guidewire\\billingcenter\\tsi\\QA\\";
			break;
		case "DEV":
			tsiFileLocation = "\\\\fbmsqa11\\guidewire\\billingcenter\\tsi\\DEV\\";
			break;
		case "IT":
			tsiFileLocation = "\\\\fbmsqa11\\guidewire\\billingcenter\\tsi\\IT\\";
			break;
		case "UAT":
			tsiFileLocation = "\\\\fbmsqa11\\guidewire\\billingcenter\\tsi\\UAT\\";
			break;
		case "REGR01":
			tsiFileLocation = "\\\\fbmsqa11\\guidewire\\billingcenter\\tsi\\REGR01\\";
			break;
		case "REGR02":
			tsiFileLocation = "\\\\fbmsqa11\\guidewire\\billingcenter\\tsi\\REGR02\\";
			break;
		case "REGR03":
			tsiFileLocation = "\\\\fbmsqa11\\guidewire\\billingcenter\\tsi\\REGR03\\";
			break;
		case "REGR04":
			tsiFileLocation = "\\\\fbmsqa11\\guidewire\\billingcenter\\tsi\\REGR04\\";
			break;
		}

		File tsiFile = new File(new GuidewireHelpers(driver).sanitizeFilePath(tsiFileLocation + tsiFileName));
		if (tsiFile.exists() && !tsiFile.isDirectory()) {
			found = true;
		}
		return found;
	}

	public GeneratePolicy getPolicyObj() {
		return myPolicyObj;
	}

	public double getDelinquentAmount() {
		return delinquentAmount;
	}
}

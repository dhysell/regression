package regression.r2.clock.billingcenter.delinquency;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.hibernate.qa.guidewire.environments.Urls;
import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.BCDesktopMyActivities;
import repository.bc.desktop.BCDesktopMyQueues;
import repository.bc.search.BCSearchPolicies;
import repository.bc.topmenu.BCTopMenu;
import repository.bc.topmenu.BCTopMenuAccount;
import repository.driverConfiguration.Config;
import repository.gw.enums.ActivityQueuesBillingCenter;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.CancellationEvent;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PolicyTermStatus;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireUmbrellaIncreasedLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.SquireUmbrellaInfo;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.EnvironmentsHelper;
@QuarantineClass
@SuppressWarnings({"unused"})
public class DelinquencyDrivenCancellationPLMultiPolicy extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private Date targetDate = null;
	private Date tsiDate = null;
	private double firstPolicyDelinquentAmount = 0;
	private double secondPolicyDelinquentAmount = 0;
	private double firstPolicyunbilledAmount = 0;
	private double secondPolicyunbilledAmount = 0;
	private boolean writeOff = getRandomBoolean();
	private ARUsers arUser;

	public DelinquencyDrivenCancellationPLMultiPolicy() {
	}

	public DelinquencyDrivenCancellationPLMultiPolicy(boolean _writeOff) {
		setWriteOff(_writeOff);
	}

	private void setWriteOff(boolean _writeOff) {
		if (_writeOff == true) {
			this.writeOff = true;
		} else if (_writeOff == false) {
			this.writeOff = false;
		} else {
			this.writeOff = getRandomBoolean();
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

//		this.agent = AgentsHelper.getRandomAgent();
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		locationsList.add(new PolicyLocation(locOnePropertyList));

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;
		myPropertyAndLiability.liabilitySection = myLiab;

		Squire mySquire = new Squire();
		mySquire.propertyAndLiability = myPropertyAndLiability;

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("Non-Pay3", "Cancel")
				.withInsPrimaryAddress(locationsList.get(0).getAddress())
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}
	
	@Test(dependsOnMethods = { "generatePolicy" })
	public void addPolicyToAccount() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
		squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_1000000);

		myPolicyObj.squireUmbrellaInfo = squireUmbrellaInfo;
		myPolicyObj.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.PolicyIssued);
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
		new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.squire.getPolicyNumber());

        BCCommonTroubleTickets troubleTicket = new BCCommonTroubleTickets(driver);
		troubleTicket.closeFirstTroubleTicket();

        BCSearchPolicies policySearch = new BCSearchPolicies(driver);
		policySearch.searchPolicyByPolicyNumber(this.myPolicyObj.squire.getPolicyNumber());

        troubleTicket = new BCCommonTroubleTickets(driver);
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
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickBCMenuDelinquencies();

		this.targetDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		boolean firstPolicyDelinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Open, myPolicyObj.accountNumber, myPolicyObj.squire.getPolicyNumber(), targetDate);
		boolean secondPolicyDelinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Open, myPolicyObj.accountNumber, myPolicyObj.squireUmbrellaInfo.getPolicyNumber(), targetDate);

		if (!firstPolicyDelinquencyFound) {
			Assert.fail("The first Delinquency was either non-existant or not in an 'open' status.");
		}
		
		if (!secondPolicyDelinquencyFound) {
			Assert.fail("The second Delinquency was either non-existant or not in an 'open' status.");
		}

		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "runInvoiceDueAndCheckDelinquency" })
	public void verifyCancelationCompletionInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        AccountSummaryPC summaryPage = new AccountSummaryPC(driver);
		boolean pendingCancelFound = summaryPage.verifyCancellationInPolicyCenter(myPolicyObj.squire.getPolicyNumber(), 120);
		if (!pendingCancelFound) {
			Assert.fail("The policy did not get a pending cancellation transaction from BC after waiting for 2 minutes.");
		}

        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 20);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);

        summaryPage = new AccountSummaryPC(driver);
		boolean firstPolicyCancelledStatusFound = summaryPage.verifyPolicyStatusInPolicyCenter(myPolicyObj.squire.getPolicyNumber(), PolicyTermStatus.Canceled, 300);
		boolean secondPolicyCancelledStatusFound = summaryPage.verifyPolicyStatusInPolicyCenter(myPolicyObj.squireUmbrellaInfo.getPolicyNumber(), PolicyTermStatus.Canceled, 300);
		if (!firstPolicyCancelledStatusFound) {
			Assert.fail("The first policy had not entered delinquency after 5 minutes of waiting.");
		}
		
		if (!secondPolicyCancelledStatusFound) {
			Assert.fail("The second policy had not entered delinquency after 5 minutes of waiting.");
		}

		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "verifyCancelationCompletionInPolicyCenter" })
	public void verifyDelinquencyStepsInBillingCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickBCMenuDelinquencies();

        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		this.firstPolicyDelinquentAmount = delinquency.getDelinquentAmount(OpenClosed.Open, myPolicyObj.accountNumber, myPolicyObj.squire.getPolicyNumber(), targetDate);
		this.firstPolicyunbilledAmount = delinquency.getUnbilledAmount();

		this.secondPolicyDelinquentAmount = delinquency.getDelinquentAmount(OpenClosed.Open, myPolicyObj.accountNumber, myPolicyObj.squireUmbrellaInfo.getPolicyNumber(), targetDate);
		this.secondPolicyunbilledAmount = delinquency.getUnbilledAmount();

		/*menu = AccountFactory.getAccountMenuPage();
		menu.clickAccountMenuInvoices();*/

		/*AccountInvoices invoices = AccountFactory.getAccountInvoicesPage();
		invoices.verifyDelinquencyCancellationInvoicingRollup(DateUtils.getCenterDate(Product.BillingCenter), unbilledAmount, delinquentAmount);*/

        menu = new BCAccountMenu(driver);
		menu.clickBCMenuDelinquencies();

		moveThroughDelinquencySteps();

		if (writeOff == false) {
			boolean found = checkForTSIFile();
			if (!found) {
				Assert.fail("The TSI Collections file was not found.");
			}
			// TODO Open TSI file and verify recordset is correct.
		}
		new GuidewireHelpers(driver).logout();
	}

	// //////////////////////////////////////////////
	// Private Methods Used to support Test Methods//
	// //////////////////////////////////////////////

    private void moveThroughDelinquencySteps() throws Exception {
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		driver.navigate().refresh();
		
		verifyDelinquencyEventCompletion(CancellationEvent.CancellationBillingInstructionReceived);
		
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);

		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);

		new BatchHelpers(driver).runBatchProcess(BatchProcess.BC_Workflow);

		driver.navigate().refresh();
		verifyDelinquencyEventCompletion(CancellationEvent.SendBalanceDue);

		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 21);

		new BatchHelpers(driver).runBatchProcess(BatchProcess.BC_Workflow);

		driver.navigate().refresh();
		verifyDelinquencyEventCompletion(CancellationEvent.SendSecondBalanceDue);
		
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 21);

        delinquency = new BCCommonDelinquencies(driver);
		this.firstPolicyDelinquentAmount = delinquency.getDelinquentAmount(OpenClosed.Open, myPolicyObj.accountNumber, myPolicyObj.squire.getPolicyNumber(), targetDate);
		this.secondPolicyDelinquentAmount = delinquency.getDelinquentAmount(OpenClosed.Open, myPolicyObj.accountNumber, myPolicyObj.squireUmbrellaInfo.getPolicyNumber(), targetDate);

		if (writeOff) {
			writeOffWorkflow();
		} else {
			if (this.firstPolicyDelinquentAmount < 50) {
				System.out.println("Warning: The Write-Off path chosen was to send to TSI, however the Delinquent Amount was under $50.00. The test will continue, but will only Write-Off.");
				writeOffWorkflow();
			} else {
				tsiWorkflow();
			}
		}
	}
	
	private void verifyDelinquencyEventCompletion (CancellationEvent cancellationEvent) {
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		boolean delinquencyStepFound = false;
		HashMap<String, Double> delinquencyVerificationFailureMap = null;
        int tableRows = new TableUtils(driver).getRowCount(delinquency.getDelinquencyTable());
		for (int i = 1; i <= tableRows; i++) {
			new TableUtils(driver).clickRowInTableByRowNumber(delinquency.getDelinquencyTable(), i);
            delinquency = new BCCommonDelinquencies(driver);
			delinquencyStepFound = delinquency.verifyDelinquencyEventCompletion(cancellationEvent);

			if (!delinquencyStepFound) {
				if (delinquencyVerificationFailureMap == null) {
					delinquencyVerificationFailureMap = new HashMap<String, Double>();
				}
				delinquencyVerificationFailureMap.put(new TableUtils(driver).getCellTextInTableByRowAndColumnName(delinquency.getDelinquencyTable(), new TableUtils(driver).getHighlightedRowNumber(delinquency.getDelinquencyTable()), "Delinquency Target"), NumberUtils.getCurrencyValueFromElement(new TableUtils(driver).getCellTextInTableByRowAndColumnName(delinquency.getDelinquencyTable(), new TableUtils(driver).getHighlightedRowNumber(delinquency.getDelinquencyTable()), "Delinquent Ammount")));
			}
		}
		
		if (delinquencyVerificationFailureMap != null) {
			String errorMessage = "The Delinquency Event \"" + cancellationEvent.getValue() + "\" never triggered./n";
			for (Map.Entry<String, Double> entry : delinquencyVerificationFailureMap.entrySet()) {
				errorMessage += "Delinquency Target: " + entry.getKey() + ", Delinquent Amount: " + StringsUtils.currencyRepresentationOfNumber(entry.getValue()) + "/n";
			}
			Assert.fail(errorMessage);
		}
	}

	private void makePaymentForWriteOff() {
		double amountToPay = 0;
		if (this.firstPolicyDelinquentAmount > 50) {
			amountToPay = this.firstPolicyDelinquentAmount - 49;
		}

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
		newPayment.makeDirectBillPaymentExecute(amountToPay, myPolicyObj.squire.getPolicyNumber());

    }

	private void writeOffWorkflow() {
		makePaymentForWriteOff();

		new BatchHelpers(driver).runBatchProcess(BatchProcess.BC_Workflow);

		driver.navigate().refresh();
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		verifyDelinquencyEventCompletion(CancellationEvent.Writeoff);

		boolean firstPolicyDelinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Closed, myPolicyObj.accountNumber, myPolicyObj.squire.getPolicyNumber(), targetDate);
		boolean secondPolicyDelinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Closed, myPolicyObj.accountNumber, myPolicyObj.squireUmbrellaInfo.getPolicyNumber(), targetDate);

		if (!firstPolicyDelinquencyFound) {
			Assert.fail("The Delinquency finished the workflow, but remained open.");
		}
		
		if (!secondPolicyDelinquencyFound) {
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
		desktopQueues.selectAndAssignReviewDelinquencyActivity(ActivityQueuesBillingCenter.ARSupervisorWesternCommunity, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), "Review Delinquency", myPolicyObj.accountNumber);
		desktopQueues.selectAndAssignReviewDelinquencyActivity(ActivityQueuesBillingCenter.ARSupervisorWesternCommunity, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), "Review Delinquency", myPolicyObj.accountNumber);

        desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuMyActivities();

		BCDesktopMyActivities myActivities = new BCDesktopMyActivities(driver);
		myActivities.completeActivity(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), "Review Delinquency", myPolicyObj.accountNumber);
		myActivities.completeActivity(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), "Review Delinquency", myPolicyObj.accountNumber);

        BCTopMenuAccount accountTopMenu = new BCTopMenuAccount(driver);
		accountTopMenu.menuAccountSearchAccountByAccountNumber(myPolicyObj.accountNumber);

        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickBCMenuDelinquencies();

		verifyDelinquencyEventCompletion(CancellationEvent.ReviewTheDelinquency);
		
		//Hard delay to ensure both policies are prepared to write out to TSI file.

		new BatchHelpers(driver).runBatchProcess(BatchProcess.FBM_Write_TSI_FIle_Process);

		driver.navigate().refresh();
		this.tsiDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);

		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 76);

		new BatchHelpers(driver).runBatchProcess(BatchProcess.BC_Workflow);

		driver.navigate().refresh();
		verifyDelinquencyEventCompletion(CancellationEvent.Writeoff);

        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		boolean firstPolicyDelinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Closed, myPolicyObj.accountNumber, myPolicyObj.squire.getPolicyNumber(), targetDate);
		boolean secondPolicyDelinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Closed, myPolicyObj.accountNumber, myPolicyObj.squireUmbrellaInfo.getPolicyNumber(), targetDate);

		if (!firstPolicyDelinquencyFound) {
			Assert.fail("The Delinquency finished the workflow, but remained open.");
		}
		
		if (!secondPolicyDelinquencyFound) {
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
		return firstPolicyDelinquentAmount;
	}
}

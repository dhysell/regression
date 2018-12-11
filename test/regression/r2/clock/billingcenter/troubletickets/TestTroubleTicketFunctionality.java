package regression.r2.clock.billingcenter.troubletickets;

import java.util.ArrayList;
import java.util.Random;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.bc.common.BCCommonTroubleTickets;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
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
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author bhiltbrand
* @Requirement This test creates a policy and randomly chooses various scenarios designed to test trouble ticket functionality.
* @RequirementsLink <a href="https://rally1.rallydev.com/#/41432362343d/detail/userstory/62554799557">Rally Story US9181</a>
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/21%20-%20Write-offs/21-01%20Collections%20Writeoffs.docx">Delinquency Requirements</a>
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/21%20-%20Write-offs/Supporting%20Documentation/TSI%20Guidelines.xls">TSI File Requirements</a>
* @Description 
* @DATE Mar 14, 2017
*/
public class TestTroubleTicketFunctionality extends BaseTest {
	private WebDriver driver;
	private boolean multiplePolicies = getRandomBoolean();
	private boolean manuallyCreateTroubleTickets = getRandomBoolean();
	private boolean manuallyCloseTroubleTickets = getRandomBoolean();
	
	private GeneratePolicy mainPolicyObject = null;

    private ARUsers arUser;
	
	private boolean getRandomBoolean() {
		Random rnd = new Random();
		return rnd.nextBoolean();
	}

	// ///////////////////
	// Main Test Methods//
	// ///////////////////

	@Test
    public void generatePolicy() throws Exception {
		
		getQALogger().info("*************************************************************");
		getQALogger().info("Multiple Policies Boolean Value: " + this.multiplePolicies);
		getQALogger().info("Manually Create Trouble Tickets Boolean Value: " + this.manuallyCreateTroubleTickets);
		getQALogger().info("Manually Close Trouble Tickets Boolean Value: " + this.manuallyCloseTroubleTickets);
		getQALogger().info("*************************************************************");
				
		
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
        this.mainPolicyObject = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("Non-Pay5", "Cancel")
				.withInsPrimaryAddress(locationsList.get(0).getAddress())
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}
	
	@Test(dependsOnMethods = { "generatePolicy" })
	public void createUmbrellaIfNeeded() throws Exception {
		if (this.multiplePolicies) {
            Config cf = new Config(ApplicationOrCenter.PolicyCenter);
            driver = buildDriver(cf);

            SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
			squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_1000000);

            mainPolicyObject.squireUmbrellaInfo = squireUmbrellaInfo;
            mainPolicyObject.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.PolicyIssued);
		} else {
			getQALogger().info("The Multiple Policies flag was set to false. Skipping creating the Umbrella policy.");
		}
	}

	@Test(dependsOnMethods = { "createUmbrellaIfNeeded" })
	public void runInvoiceWithoutMakingDownpayment() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver). loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.mainPolicyObject.squire.getPolicyNumber());

        BCCommonTroubleTickets troubleTicket = new BCCommonTroubleTickets(driver);
		troubleTicket.closeFirstTroubleTicket();

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new GuidewireHelpers(driver).logout();
	}

	/*@Test(dependsOnMethods = { "runInvoiceWithoutMakingDownpayment" })
	public void moveClocks() throws Exception {
		ClockUtils.setCurrentDates(DateAddSubtractOptions.Day, 1);
	}

	@Test(dependsOnMethods = { "moveClocks" })
	public void runInvoiceDueAndCheckDelinquency() throws Exception {
		loginToProductAndSearchAccountByAccountNumber(ApplicationOrCenter.BillingCenter, this.arUser.getUserName(), this.arUser.getPassword(), this.mainPolicyObject.accountNumber);

		runBatchProcess(BatchProcess.Invoice_Due);

		IAccountMenu menu = AccountFactory.getAccountMenuPage();
		menu.clickAccountMenuDelinquencies();

		this.targetDate = DateUtils.getCenterDate(ApplicationOrCenter.BillingCenter);
		BCCommonDelinquencies delinquency = AccountFactory.getBCCommonDelinquenciesPage();
		boolean delinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Open, mainPolicyObject.accountNumber, mainPolicyObject.policyNumber, targetDate);

		if (!delinquencyFound) {
			throw new GuidewireBillingCenterException(getCurrentURL(), mainPolicyObject.accountNumber, "The Delinquency was either non-existant or not in an 'open' status.");
		}

		logout();
	}

	@Test(dependsOnMethods = { "runInvoiceDueAndCheckDelinquency" })
	public void verifyCancelationCompletionInPolicyCenter() throws Exception {
		loginToProductAndSearchAccountByAccountNumber(ApplicationOrCenter.PolicyCenter,arUser.getUserName(), arUser.getPassword(), mainPolicyObject.accountNumber);		
		
		IAccountSummary summaryPage = pc.account.AccountFactory.getAccountSummaryPage();
		boolean pendingCancelFound = summaryPage.verifyCancellationInPolicyCenter(mainPolicyObject.policyNumber, 120);
		if (!pendingCancelFound) {
			throw new GuidewirePolicyCenterException(getCurrentURL(), mainPolicyObject.accountNumber, "The policy did not get a pending cancellation transaction from BC after waiting for 2 minutes.");
		}

				ClockUtils.setCurrentDates(DateAddSubtractOptions.Day, 20);

		runBatchProcess(BatchProcess.PC_Workflow);

		summaryPage = pc.account.AccountFactory.getAccountSummaryPage();
		boolean cancelledStatusFound = summaryPage.verifyPolicyStatusInPolicyCenter(mainPolicyObject.policyNumber, "Canceled", 300);
		if (!cancelledStatusFound) {
			throw new GuidewirePolicyCenterException(getCurrentURL(), mainPolicyObject.accountNumber, "The policy had not entered delinquency after 5 minutes of waiting.");
		}

		logout();
	}

	@Test(dependsOnMethods = { "verifyCancelationCompletionInPolicyCenter" })
	public void verifyDelinquencyStepsInBillingCenter() throws Exception {
		loginToProductAndSearchAccountByAccountNumber(ApplicationOrCenter.BillingCenter, this.arUser.getUserName(), this.arUser.getPassword(), this.mainPolicyObject.accountNumber);

		IAccountMenu menu = AccountFactory.getAccountMenuPage();
		menu.clickAccountMenuDelinquencies();

		BCCommonDelinquencies delinquency = AccountFactory.getBCCommonDelinquenciesPage();
		this.delinquentAmount = delinquency.getDelinquentAmount(OpenClosed.Open, mainPolicyObject.accountNumber, mainPolicyObject.policyNumber, targetDate);
		this.unbilledAmount = delinquency.getUnbilledAmount();
		
		menu = AccountFactory.getAccountMenuPage();
		menu.clickAccountMenuDelinquencies();

		moveThroughDelinquencySteps();

		if (writeOff == false) {
			boolean found = checkForTSIFile();
			if (!found) {
				Assert.fail("The TSI Collections file was not found. Test Failed");
			}
			boolean recordsMatch = true;
			if (this.mainPolicyObject.insCompanyName != null) {
				recordsMatch = verifyTSIFile(this.mainPolicyObject.policyNumber, this.mainPolicyObject.insCompanyName, this.mainPolicyObject.insPrimaryAddress, this.tsiDate, this.delinquentAmount);
			} else {
				recordsMatch = verifyTSIFile(this.mainPolicyObject.policyNumber, (this.mainPolicyObject.insLastName + ", " + this.mainPolicyObject.insFirstName), this.mainPolicyObject.insPrimaryAddress, this.tsiDate, this.delinquentAmount);
			}
			if (!recordsMatch) {
				Assert.fail("The TSI Collections file was found, but the recordset did not match what was expected. Please see console output for specific errors. Test failed.");
			}
		}
		logout();
	}

	// //////////////////////////////////////////////
	// Private Methods Used to support Test Methods//
	// //////////////////////////////////////////////

	private void moveThroughDelinquencySteps() throws Exception {
		BCCommonDelinquencies delinquency = AccountFactory.getBCCommonDelinquenciesPage();
		Configuration.getWebDriver().navigate().refresh();
		
		verifyDelinquencyEventCompletion(CancellationEvent.CancellationBillingInstructionReceived);
		
		runBatchProcess(BatchProcess.Invoice);
		runBatchProcess(BatchProcess.Invoice_Due);

		ClockUtils.setCurrentDates(DateAddSubtractOptions.Day, 1);

		runBatchProcess(BatchProcess.BC_Workflow);

		Configuration.getWebDriver().navigate().refresh();
		verifyDelinquencyEventCompletion(CancellationEvent.SendBalanceDue);
		this.dateOfDebt = DateUtils.getCenterDate(ApplicationOrCenter.BillingCenter);

		ClockUtils.setCurrentDates(DateAddSubtractOptions.Day, 21);

		runBatchProcess(BatchProcess.BC_Workflow);

		Configuration.getWebDriver().navigate().refresh();
		verifyDelinquencyEventCompletion(CancellationEvent.SendSecondBalanceDue);
		
		ClockUtils.setCurrentDates(DateAddSubtractOptions.Day, 21);

		delinquency = AccountFactory.getBCCommonDelinquenciesPage();
		this.delinquentAmount = delinquency.getDelinquentAmount(OpenClosed.Open, mainPolicyObject.accountNumber, mainPolicyObject.policyNumber, targetDate);
		
		if (writeOff) {
			writeOffWorkflow();
		} else {
			if (this.delinquentAmount < 50) {
				System.out.println("Warning: The Write-Off path chosen was to send to TSI, however the Delinquent Amount was under $50.00. The test will continue, but will only Write-Off.");
				writeOffWorkflow();
			} else {
				tsiWorkflow();
			}
		}
	}
	
	private void verifyDelinquencyEventCompletion (CancellationEvent cancellationEvent) throws GuidewireBillingCenterException {
		BCCommonDelinquencies delinquency = AccountFactory.getBCCommonDelinquenciesPage();
		boolean delinquencyStepFound = false;
		HashMap<String, Double> delinquencyVerificationFailureMap = null;
				int tableRows = TableUtils.getRowCount(delinquency.getDelinquencyTable());
		for (int i = 1; i <= tableRows; i++) {
			TableUtils.clickRowInTableByRowNumber(delinquency.getDelinquencyTable(), i);
						delinquency = AccountFactory.getBCCommonDelinquenciesPage();
			delinquencyStepFound = delinquency.verifyDelinquencyEventCompletion(cancellationEvent);

			if (!delinquencyStepFound) {
				if (delinquencyVerificationFailureMap == null) {
					delinquencyVerificationFailureMap = new HashMap<String, Double>();
				}
				delinquencyVerificationFailureMap.put(TableUtils.getCellTextInTableByRowAndColumnName(delinquency.getDelinquencyTable(), TableUtils.getHighlightedRowNumber(delinquency.getDelinquencyTable()), "Delinquency Target"), NumberUtils.getCurrencyValueFromElement(TableUtils.getCellTextInTableByRowAndColumnName(delinquency.getDelinquencyTable(), TableUtils.getHighlightedRowNumber(delinquency.getDelinquencyTable()), "Delinquent Ammount")));
			}
		}
		
		if (delinquencyVerificationFailureMap != null) {
			String errorMessage = "The Delinquency Event \"" + cancellationEvent.getValue() + "\" never triggered./n";
			for (Map.Entry<String, Double> entry : delinquencyVerificationFailureMap.entrySet()) {
				errorMessage += "Delinquency Target: " + entry.getKey() + ", Delinquent Amount: " + StringsUtils.currencyRepresentationOfNumber(entry.getValue()) + "/n";
			}
			throw new GuidewireBillingCenterException(getCurrentURL(), mainPolicyObject.accountNumber, errorMessage);
		}
	}

	private void makePaymentForWriteOff() {
		double amountToPay = 0;
		if (this.delinquentAmount > 50) {
			amountToPay = this.delinquentAmount - 49;
		}

		IAccountMenu accountMenu = AccountFactory.getAccountMenuPage();
		accountMenu.clickAccountMenuActionsNewDirectBillPayment();

		NewDirectBillPayment newPayment = AccountFactory.getNewDirectBillPaymentPage();
		newPayment.makeDirectBillPaymentExecute(amountToPay, mainPolicyObject.policyNumber);

			}

	private void writeOffWorkflow() throws GuidewireBillingCenterException {
		makePaymentForWriteOff();

		runBatchProcess(BatchProcess.BC_Workflow);

		Configuration.getWebDriver().navigate().refresh();
		BCCommonDelinquencies delinquency = AccountFactory.getBCCommonDelinquenciesPage();
		verifyDelinquencyEventCompletion(CancellationEvent.Writeoff);

		boolean delinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Closed, mainPolicyObject.accountNumber, mainPolicyObject.policyNumber, targetDate);
		
		if (!delinquencyFound) {
			throw new GuidewireBillingCenterException(getCurrentURL(), mainPolicyObject.accountNumber, "The Delinquency finished the workflow, but remained open.");
		}
	}

	private void tsiWorkflow() throws Exception {
		runBatchProcess(BatchProcess.BC_Workflow);

		bc.topmenu.interfaces.ITopMenu topMenu = TopMenuFactory.getMenu();
		topMenu.clickDesktopTab();

		IDesktopMenu desktopMenu = DesktopFactory.getDesktopMenuPage();
		desktopMenu.clickDesktopMenuMyQueues();

		IDesktopMyQueues desktopQueues = DesktopFactory.getDesktopMyQueuesPage();
		if (this.mainPolicyObject.productType.equals(ProductLineType.Businessowners) || this.mainPolicyObject.productType.equals(ProductLineType.CPP)) {
			desktopQueues.selectAndAssignReviewDelinquencyActivity(ActivityQueuesBillingCenter.ARSupervisorWesternCommunity, DateUtils.getCenterDate(ApplicationOrCenter.BillingCenter), "Review Delinquency", mainPolicyObject.accountNumber);
		} else {
			desktopQueues.selectAndAssignReviewDelinquencyActivity(ActivityQueuesBillingCenter.ARSupervisorFarmBureau, DateUtils.getCenterDate(ApplicationOrCenter.BillingCenter), "Review Delinquency", mainPolicyObject.accountNumber);
		}
		
		desktopMenu = DesktopFactory.getDesktopMenuPage();
		desktopMenu.clickDesktopMenuMyActivities();

		IDesktopMyActivities myActivities = DesktopFactory.getDesktopMyActivitiesPage();
		myActivities.completeActivity(DateUtils.getCenterDate(ApplicationOrCenter.BillingCenter), "Review Delinquency", mainPolicyObject.accountNumber);
		
		ITopMenuAccount accountTopMenu = TopMenuFactory.getMenuAccount();
		accountTopMenu.menuAccountSearchAccountByAccountNumber(mainPolicyObject.accountNumber);

		IAccountMenu menu = AccountFactory.getAccountMenuPage();
		menu.clickAccountMenuDelinquencies();

		verifyDelinquencyEventCompletion(CancellationEvent.ReviewTheDelinquency);
		
		//Hard delay to ensure policies are prepared to write out to TSI file.

		runBatchProcess(BatchProcess.FBM_Write_TSI_FIle_Process);

		Configuration.getWebDriver().navigate().refresh();
		this.tsiDate = DateUtils.getCenterDate(ApplicationOrCenter.BillingCenter);

		ClockUtils.setCurrentDates(DateAddSubtractOptions.Day, 76);

		runBatchProcess(BatchProcess.BC_Workflow);

		Configuration.getWebDriver().navigate().refresh();
		verifyDelinquencyEventCompletion(CancellationEvent.Writeoff);

		BCCommonDelinquencies delinquency = AccountFactory.getBCCommonDelinquenciesPage();
		boolean delinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Closed, mainPolicyObject.accountNumber, mainPolicyObject.policyNumber, targetDate);
		
		if (!delinquencyFound) {
			throw new GuidewireBillingCenterException(getCurrentURL(), mainPolicyObject.accountNumber, "The Delinquency finished the workflow, but remained open.");
		}
	}

	private boolean checkForTSIFile() throws Exception {
		boolean found = false;
		String tsiFileLocation = null;
		String tsiFileName = "";
		if (this.mainPolicyObject.productType.equals(ProductLineType.Businessowners) || this.mainPolicyObject.productType.equals(ProductLineType.CPP)) {
			tsiFileName = "TSICollectionsWCIC_" + DateUtils.dateFormatAsString("yyyy-MM-dd", new Date()) + ".csv";
		} else {
			tsiFileName = "TSICollectionsFBM_" + DateUtils.dateFormatAsString("yyyy-MM-dd", new Date()) + ".csv";
		}
		String server = Configuration.getInstance().getInstanceServer();
		Environments environmentInfo = EnvironmentsHelper.getBCEnvironmentInfo(server);

		if (environmentInfo.isIsDeveloper()) {
			if (this.mainPolicyObject.productType.equals(ProductLineType.Businessowners) || this.mainPolicyObject.productType.equals(ProductLineType.CPP)) {
				tsiFileLocation = "\\\\fbmsqa11\\guidewire\\billingcenter\\tsi\\DEV\\WC\\";
			} else {
				tsiFileLocation = "\\\\fbmsqa11\\guidewire\\billingcenter\\tsi\\DEV\\FB\\";
			}
		} else {
			if (this.mainPolicyObject.productType.equals(ProductLineType.Businessowners) || this.mainPolicyObject.productType.equals(ProductLineType.CPP)) {
				switch (environmentInfo.getServer()) {
				case "QA": case "QA2":
					tsiFileLocation = "\\\\fbmsqa11\\guidewire\\billingcenter\\tsi\\QA\\WC\\";
					break;
				case "DEV": case "DEV2":
					tsiFileLocation = "\\\\fbmsqa11\\guidewire\\billingcenter\\tsi\\DEV\\WC\\";
					break;
				case "IT": case "IT2":
					tsiFileLocation = "\\\\fbmsqa11\\guidewire\\billingcenter\\tsi\\IT\\WC\\";
					break;
				case "UAT": case "UAT2":
					tsiFileLocation = "\\\\fbmsqa11\\guidewire\\billingcenter\\tsi\\UAT\\WC\\";
					break;
				}
			} else {
				switch (environmentInfo.getServer()) {
				case "QA": case "QA2":
					tsiFileLocation = "\\\\fbmsqa11\\guidewire\\billingcenter\\tsi\\QA\\FB\\";
					break;
				case "DEV": case "DEV2":
					tsiFileLocation = "\\\\fbmsqa11\\guidewire\\billingcenter\\tsi\\DEV\\FB\\";
					break;
				case "IT": case "IT2":
					tsiFileLocation = "\\\\fbmsqa11\\guidewire\\billingcenter\\tsi\\IT\\FB\\";
					break;
				case "UAT": case "UAT2":
					tsiFileLocation = "\\\\fbmsqa11\\guidewire\\billingcenter\\tsi\\UAT\\FB\\";
					break;
				}
			}
		}

		this.tsiFile = new File(new GuidewireHelpers(driver).sanitizeFilePath(tsiFileLocation + tsiFileName));
		if (tsiFile.exists() && !tsiFile.isDirectory()) {
			found = true;
		}
		return found;
	}
	
	private boolean verifyTSIFile(String policyNumber, String debtorName, AddressInfo policyAddress, Date delinquencyDate, double delinquentAmount) throws Exception {
		if (policyNumber.contains("-")) {
			systemOut("There were dashes in the Policy Number that was passed in. We need this number to be dash free for verification purposes. Removing them now...");
			String[] policyNumberSplit = policyNumber.split("-");
			policyNumber = policyNumberSplit[0] + policyNumberSplit[1] + policyNumberSplit[2];
		}
		ArrayList<TSIRecord> tsiRecords = TSIRecordHelper.getTSIRecords(this.tsiFile, true);
		boolean found = false;
		boolean recordsMatch = true;
		for (TSIRecord tsiRecord : tsiRecords) {
			if (tsiRecord.getTransmittalNumber().contains(policyNumber)) {
				systemOut("The TSI record corresponding to the Policy Number was found.");
				found = true;
				if (tsiRecord.getTransmittalNumber().length() > 10) {
					recordsMatch = false;
					systemOut("The Debtor Transmittal Number was longer than the maximum 10 characters.");
				}
				if (!tsiRecord.getDebtorName().contains(debtorName)) {
					recordsMatch = false;
					systemOut("The Debtor Name did not match the policy name from the test.");
				}
				if (tsiRecord.getDebtorName().length() > 30) {
					recordsMatch = false;
					systemOut("The Debtor Name was longer than the maximum 30 characters.");
				}
				if (!tsiRecord.getStreet().contains(policyAddress.getLine1())) {
					recordsMatch = false;
					systemOut("The Debtor Street address did not match the policy address line 1 from the test.");
				}
				if (tsiRecord.getStreet().length() > 30) {
					recordsMatch = false;
					systemOut("The Debtor Address was longer than the maximum 30 characters.");
				}
				if (!tsiRecord.getCity().contains(policyAddress.getCity())) {
					recordsMatch = false;
					systemOut("The Debtor City did not match the policy City from the test.");
				}
				if (tsiRecord.getCity().length() > 15) {
					recordsMatch = false;
					systemOut("The Debtor City was longer than the maximum 15 characters.");
				}
				if (!tsiRecord.getState().equals(policyAddress.getState())) {
					recordsMatch = false;
					systemOut("The Debtor State did not match the policy State from the test.");
				}
				if (!tsiRecord.getZip().contains(policyAddress.getZip().substring(0, 5))) {
					recordsMatch = false;
					systemOut("The Debtor Zip did not match the policy Zip from the test.");
				}
				if (tsiRecord.getZip().length() > 5) {
					recordsMatch = false;
					systemOut("The Zip code was longer than the requisite 5 digits.");
				}
				if (!tsiRecord.getDateOfDebt().equals(DateUtils.getDateValueOfFormat(this.dateOfDebt,"MM/dd/yyyy"))) {
					recordsMatch = false;
					systemOut("The Debtor Date of Debt did not match the policy Delinquency Date from the test.");
				}
				if (!tsiRecord.getBalance().equals(delinquentAmount)) {
					recordsMatch = false;
					systemOut("The Debtor Balance did not match the policy Delinquent Amount from the test.");
				}
			}
		}
		if (!found) {
			Assert.fail("The TSI record corresponding to the policy number passed in was not found. Test Failed.");
		}
		return recordsMatch;
	}*/
}

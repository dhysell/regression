package regression.r2.clock.billingcenter.other;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountPolicyLoanBalances;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.wizards.CreateAccountDisbursementWizard;
import repository.bc.wizards.NewTransferWizard;
import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.Cancellation.CancellationStatus;
import repository.gw.enums.ChargeCategory;
import repository.gw.enums.ChargeHoldStatus;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DisbursementReason;
import repository.gw.enums.DisbursementStatus;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentLocation;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.TransactionType;
import repository.gw.enums.TransferReason;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;

import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
@QuarantineClass
public class TestLHPolicyLoanBalancesTransactionTracking extends BaseTest {
	private WebDriver driver;
	private ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
	private ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
	private ArrayList<AdditionalInterest> loc1Bldg2AdditionalInterests = new ArrayList <AdditionalInterest>();
	private GeneratePolicy myPolicyObj = null;
	private Date targetDate = null;
	private ARUsers arUser = new ARUsers();
	private String lienholderName = null;
	private String lienholderNumber = null;
	private String lienholderLoanNumber = null;
	private double lienholderPaymentAmount = 50.00;
	private double lienholderPaymentTracking = this.lienholderPaymentAmount;
	private double lienholderTransferTracking = 0.00;
	private double delinquentAmount = 0.00;

	@Test
	public void generatePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		int yearTest = DateUtils.getCenterDateAsInt(cf, ApplicationOrCenter.PolicyCenter, "yyyy") - NumberUtils.generateRandomNumberInt(0, 50);
		
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setYearBuilt(yearTest);
		loc1Bldg1.setClassClassification("storage");
		
		PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
		loc1Bldg2.setYearBuilt(2010);
		loc1Bldg2.setClassClassification("storage");
		
		AddressInfo addIntTest = new AddressInfo();
		addIntTest.setLine1("PO Box 711");
		addIntTest.setCity("Pocatello");
		addIntTest.setState(State.Idaho);
		addIntTest.setZip("83204");//-0711
		
		AdditionalInterest loc1Bld2AddInterest = new AdditionalInterest("Additional Interest", addIntTest);
		loc1Bld2AddInterest.setNewContact(CreateNew.Create_New_Always);
		loc1Bld2AddInterest.setAddress(addIntTest);
		loc1Bld2AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1Bldg2AdditionalInterests.add(loc1Bld2AddInterest);
		loc1Bldg2.setAdditionalInterestList(loc1Bldg2AdditionalInterests);
		
		this.locOneBuildingList.add(loc1Bldg1);
		this.locOneBuildingList.add(loc1Bldg2);
		this.locationsList.add(new PolicyLocation(new AddressInfo(), this.locOneBuildingList));

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("LH Transaction Test")
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolOrgType(OrganizationType.Partnership)
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		this.lienholderName = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNameFromPolicyCenter();
		this.lienholderNumber = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNumber();
		this.lienholderLoanNumber = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLoanContractNumber();
	}

	@Test(dependsOnMethods = { "generatePolicy" })
	public void makeInsuredDownPayment() throws Exception {
				
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);

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
    public void paySmallLienholderAmount() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienholderNumber);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPolicyNumber(), this.lienholderLoanNumber, this.lienholderPaymentAmount);

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuPolicyLoanBalances();

        AccountPolicyLoanBalances policyLoanBalances = new AccountPolicyLoanBalances(driver);
		WebElement policyLoanBalancesRow = null;
		try {
            policyLoanBalancesRow = policyLoanBalances.getAccountPolicyLoanBalancesTableRow(myPolicyObj.busOwnLine.getPolicyNumber(), this.lienholderLoanNumber, myPolicyObj.busOwnLine.getPremium().getTotalAdditionalInterestPremium(), null, this.lienholderPaymentAmount, null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("The Payments cell did not update as expected on the Policy Loan Balances Table. Test Failed.");
		}
		

		new BasePage(driver).clickWhenClickable(policyLoanBalancesRow);

		policyLoanBalances.clickPolicyLoanBalancesChargesTab();

		try {
            policyLoanBalances.getAccountPolicyLoanBalancesChargesTableRow(DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Month, -1), this.lienholderNumber, null, ChargeCategory.Premium, TransactionType.Policy_Issuance, null, ChargeHoldStatus.Not_Held, null, myPolicyObj.busOwnLine.getPolicyNumber(), myPolicyObj.busOwnLine.getPremium().getTotalAdditionalInterestPremium(), null, null, this.lienholderLoanNumber, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("The line item expected for the original charges from issuance was not found. Test Failed.");
		}
		
		policyLoanBalances.clickPolicyLoanBalancesPaymentsTab();

		try {
            policyLoanBalances.getAccountPolicyLoanBalancesPaymentsTableRow(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), this.myPolicyObj.busOwnLine.getPolicyNumber(), this.lienholderLoanNumber, null, null, null, PaymentLocation.BillingCenter, null, "Default", this.lienholderPaymentAmount, this.lienholderPaymentAmount, null);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("The Line item expected for the recently made payment was not found. Test Failed.");
		}
	}

	@Test(dependsOnMethods = { "paySmallLienholderAmount" })
	public void runInvoiceDueAndCheckDelinquency() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);

		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickBCMenuDelinquencies();

		this.targetDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		boolean delinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Open, this.lienholderNumber, null, targetDate);

		if (!delinquencyFound) {
			Assert.fail("The Delinquency was either non-existant or not in an 'open' status.");
		}

        delinquentAmount = delinquency.getDelinquentAmount(OpenClosed.Open, null, myPolicyObj.busOwnLine.getPolicyNumber(), DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter));
		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "runInvoiceDueAndCheckDelinquency" })
	public void verifyPartialNonPayCancelInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        PolicySummary polSummaryPage = new PolicySummary(driver);
        boolean pendingCancellationFound = polSummaryPage.verifyPendingCancellation(DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 20), CancellationStatus.Cancelling, true, delinquentAmount, 120);
		
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
        new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.busOwnLine.getPolicyNumber());

        PolicySummary polSummaryPage = new PolicySummary(driver);
		boolean automatedNonPayCancellationOrPolicyChangeFound  = polSummaryPage.verifyPolicyTransaction(null,DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), null, TransactionType.Policy_Change, null, 120);
		
		if (!automatedNonPayCancellationOrPolicyChangeFound) {
			Assert.fail("The policy did not get automated policy change after waiting for 2 minutes.");
		}
						
		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "removeLienholderCoveragesInPolicyCenter" })
    public void verifyPolicyLoanBalancesScreen() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienholderNumber);
		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuPolicyLoanBalances();

        AccountPolicyLoanBalances policyLoanBalances = new AccountPolicyLoanBalances(driver);
		WebElement policyLoanBalancesRow = null;
		int policyLoanBalancesRowNumber = 0;
		try {
			//this block is for the new way of doing this test
			this.lienholderTransferTracking = this.lienholderTransferTracking - this.lienholderPaymentAmount;
            policyLoanBalancesRow = policyLoanBalances.getAccountPolicyLoanBalancesTableRow(myPolicyObj.busOwnLine.getPolicyNumber(), this.lienholderLoanNumber, null, null, this.lienholderPaymentAmount, null, this.lienholderTransferTracking, null, null);
			//end block
			//The function above will return what will be when functionality is restored later. The function below will return what the page is now.
			//policyLoanBalancesRow = policyLoanBalances.getAccountPolicyLoanBalancesTableRow(myPolicyObj.policyNumber, this.lienholderLoanNumber, null, null, this.lienholderPaymentAmount, null, null, this.lienholderPaymentAmount, this.lienholderPaymentAmount);
			policyLoanBalancesRowNumber = new TableUtils(driver).getHighlightedRowNumber(policyLoanBalances.getPolicyLoanBalancesTable());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("The Charges, Total Cash, and Excess Cash cells did not update as expected on the Policy Loan Balances Table. Test Failed.");
		}
		
		new BasePage(driver).clickWhenClickable(policyLoanBalancesRow);

        policyLoanBalances.clickPolicyLoanBalancesChargesTab();

        double cancelationAmountTotal = 0;
		List<WebElement> tableRows = new TableUtils(driver).getAllTableRows(policyLoanBalances.getPolicyLoanBalancesChargesTable());
		for (WebElement row : tableRows) {
			if (new TableUtils(driver).getCellTextInTableByRowAndColumnName(policyLoanBalances.getPolicyLoanBalancesChargesTable(), new TableUtils(driver).getRowNumberFromWebElementRow(row), "Masqueraded As").equalsIgnoreCase("Cancellation")) {
				cancelationAmountTotal = cancelationAmountTotal + NumberUtils.getCurrencyValueFromElement(new TableUtils(driver).getCellTextInTableByRowAndColumnName(policyLoanBalances.getPolicyLoanBalancesChargesTable(), new TableUtils(driver).getRowNumberFromWebElementRow(row), "Amount"));	
			}			
		}
        if (Math.abs(cancelationAmountTotal) != myPolicyObj.busOwnLine.getPremium().getTotalAdditionalInterestPremium()) {
			Assert.fail("The charges that appeared in the charges table on the policy loan balances screen did not equal the absolute value of the original premium. Test failed.");
		}
		
		policyLoanBalances.clickPolicyLoanBalancesCreditsTab();

        double downPaymentInvoiceAmount = 0.00;
		double installmentInvoiceAmount = 0.00;
		try {
			for (WebElement tableRow : new TableUtils(driver).getAllTableRows(policyLoanBalances.getPolicyLoanBalancesCreditsTable())) {
				new BasePage(driver).clickWhenClickable(tableRow);

                downPaymentInvoiceAmount = downPaymentInvoiceAmount + NumberUtils.getCurrencyValueFromElement(new TableUtils(driver).getCellTextInTableByRowAndColumnName(policyLoanBalances.getPolicyLoanBalancesCreditsPaymentDetailsTable(), new TableUtils(driver).getRowNumberFromWebElementRow(new TableUtils(driver).getRowInTableByColumnNameAndValue(policyLoanBalances.getPolicyLoanBalancesCreditsPaymentDetailsTable(), "Type", "Down Payment")), "Amount"));
				installmentInvoiceAmount = installmentInvoiceAmount + NumberUtils.getCurrencyValueFromElement(new TableUtils(driver).getCellTextInTableByRowAndColumnName(policyLoanBalances.getPolicyLoanBalancesCreditsPaymentDetailsTable(), new TableUtils(driver).getRowNumberFromWebElementRow(new TableUtils(driver).getRowInTableByColumnNameAndValue(policyLoanBalances.getPolicyLoanBalancesCreditsPaymentDetailsTable(), "Type", "Installment")), "Amount"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("The Credits Payment Details Table did not exist, or the rows searched encountered an error. Test Failed.");
		}
		
		if ((downPaymentInvoiceAmount / 2) != (Math.abs(installmentInvoiceAmount))) {
			Assert.fail("The Credits Payment Details section did not match invoice amounts as expected. Test Failed.");
		}
		
		//This block of code is for the future way of doing this test.
        accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeDirectBillPaymentExecuteWithoutDistribution(myPolicyObj.busOwnLine.getPolicyNumber(), this.lienholderLoanNumber, 35.00);
		this.lienholderPaymentTracking = this.lienholderPaymentTracking + 35.00;

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuPolicyLoanBalances();

        policyLoanBalances = new AccountPolicyLoanBalances(driver);
        policyLoanBalancesRowNumber = new TableUtils(driver).getRowNumberFromWebElementRow(policyLoanBalances.getAccountPolicyLoanBalancesTableRow(myPolicyObj.busOwnLine.getPolicyNumber(), this.lienholderLoanNumber, null, null, this.lienholderPaymentTracking, null, this.lienholderTransferTracking, 35.00, 35.00));
		//End Block
		
		policyLoanBalances.clickActionsTransferLink(policyLoanBalancesRowNumber);

        NewTransferWizard transferWizard = new NewTransferWizard(driver);
		transferWizard.createNewTransfer("randomInsured", null, 15.00, null, null, TransferReason.Misapplied);
		this.lienholderTransferTracking = this.lienholderTransferTracking - 15.00;
		
		try {
			//new way:
            policyLoanBalances.getAccountPolicyLoanBalancesTableRow(myPolicyObj.busOwnLine.getPolicyNumber(), this.lienholderLoanNumber, null, null, this.lienholderPaymentTracking, null, this.lienholderTransferTracking, 20.00, 20.00);
			//policyLoanBalances.getAccountPolicyLoanBalancesTableRow(myPolicyObj.policyNumber, this.lienholderLoanNumber, null, null, this.lienholderPaymentAmount, null, -15.00, this.lienholderPaymentTracking, this.lienholderPaymentTracking);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("The Charges, Total Cash, Excess Cash, and transfers cells did not update as expected on the Policy Loan Balances Table. Test Failed.");
		}
		
		policyLoanBalances.clickPolicyLoanBalancesTransfersTab();

        try {
			//new way:
			policyLoanBalances.getAccountPolicyLoanBalancesTransfersTableRow(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, 15.00, null, null, null, null, null, null, TransferReason.Misapplied);
			//policyLoanBalances.getAccountPolicyLoanBalancesTransfersTableRow(DateUtils.getCenterDate(ApplicationOrCenter.BillingCenter), null, 15.00, null, null, null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("The Line item expected for the recently made Transfer was not found. Test Failed.");
		}
		
		policyLoanBalances.clickActionsDisbursementLink(policyLoanBalancesRowNumber);

        CreateAccountDisbursementWizard disbursementWizard = new CreateAccountDisbursementWizard(driver);
		disbursementWizard.createAccountDisbursement(20.00, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DisbursementReason.Overpayment);
		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Disbursement);
		driver.navigate().refresh();

        try {
			//new way:
            policyLoanBalances.getAccountPolicyLoanBalancesTableRow(myPolicyObj.busOwnLine.getPolicyNumber(), this.lienholderLoanNumber, null, null, this.lienholderPaymentTracking, -20.00, this.lienholderTransferTracking, 0.00, 0.00);
			//policyLoanBalances.getAccountPolicyLoanBalancesTableRow(myPolicyObj.policyNumber, this.lienholderLoanNumber, null, null, this.lienholderPaymentAmount, -20.00, -15.00, this.lienholderPaymentTracking, this.lienholderPaymentTracking);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("The Charges, Total Cash, Excess Cash, disbursements, and transfers cells did not update as expected on the Policy Loan Balances Table. Test Failed.");
		}
		
		policyLoanBalances.clickPolicyLoanBalancesDisbursementsTab();

        try {
			policyLoanBalances.getAccountPolicyLoanBalancesDisbursementsTableRow(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, null, null, DisbursementStatus.Sent, null, 20.00, this.lienholderName, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("The Line item expected for the recently made Disbursement was not found. Test Failed.");
		}
		
		new GuidewireHelpers(driver).logout();		
	}
}
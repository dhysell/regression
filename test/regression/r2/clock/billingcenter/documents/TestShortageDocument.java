package regression.r2.clock.billingcenter.documents;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.common.BCCommonDocuments;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Building.CoverageOrNot;
import repository.gw.enums.Building.RoofingType;
import repository.gw.enums.Building.UtilitiesCoverageAppliesTo;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.Cancellation.CancellationStatus;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.Location.AutoIncreaseBlgLimitPercentage;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PolicyTermStatus;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.PolicyLocationBuildingAdditionalCoverages;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderBuildingAdditionalCoverages;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderPayerAssignment;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author bhiltbrand
 * @Requirement This test makes sure that when invoices go billed, that corresponding documents are created.
 * @RequirementsLink <a href=https://rally1.rallydev.com/#/7832667974d/detail/defect/47388849413>Rally Defect DE3072</a>
 * @DATE Dec 18, 2015
 */
public class TestShortageDocument extends BaseTest {
	private WebDriver driver;
	private ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
	private ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser;
	private Date targetDate = null;
	private PolicyLocation location1 = null;
	private PolicyLocationBuilding loc1Bldg1 = null;
	private PolicyLocationBuilding loc1Bldg2 = null;
	private AdditionalInterest loc1Bldg2AddInterest = null;
	private double delinquentAmount = 0;
	
	@Test
	public void generatePolicy() throws Exception {

		PolicyBusinessownersLineAdditionalCoverages myLineAddCov = new PolicyBusinessownersLineAdditionalCoverages(false, true);
		PolicyBusinessownersLine myline = new PolicyBusinessownersLine(SmallBusinessType.StoresRetail);
		myline.setAdditionalCoverageStuff(myLineAddCov);

		ArrayList<AdditionalInterest> loc1Bldg2AdditionalInterests = new ArrayList<AdditionalInterest>();
		
		loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setUsageDescription("Insured Building");
		loc1Bldg1.setClassClassification("storage");
		loc1Bldg1.setNumFireExtinguishers(6);
		loc1Bldg1.setYearBuilt(2015);
		loc1Bldg1.setTotalArea("6000");
		loc1Bldg1.setSprinklered(false);
		loc1Bldg1.setRoofingType(RoofingType.ShinglesAsphalt);

		loc1Bldg2 = new PolicyLocationBuilding();
		loc1Bldg2.setUsageDescription("LH Building");
		loc1Bldg2.setClassClassification("storage");
		loc1Bldg2.setBuildingLimit(300000);
		loc1Bldg2.setBppLimit(300000);
		loc1Bldg2.setYearBuilt(2010);
		loc1Bldg2.setNumStories(2);
		loc1Bldg2.setTotalArea("4000");
		loc1Bldg2.setSprinklered(false);
		loc1Bldg2.setRoofingType(RoofingType.ShinglesAsphalt);

		loc1Bldg2AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1Bldg2AddInterest.setLoanContractNumber("LN 12345");
		loc1Bldg2AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1Bldg2AdditionalInterests.add(loc1Bldg2AddInterest);
		loc1Bldg2.setAdditionalInterestList(loc1Bldg2AdditionalInterests);

		this.locOneBuildingList.add(loc1Bldg1);
		this.locOneBuildingList.add(loc1Bldg2);
		
		location1 = new PolicyLocation();
		location1.setAutoautoIncrease(AutoIncreaseBlgLimitPercentage.AutoInc4Perc);
		location1.setAnnualGrossReceipts(500000);
		location1.setBuildingList(locOneBuildingList);
		
		this.locationsList.add(location1);

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("DE3027 Scenario")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(myline)
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}

	@Test(dependsOnMethods = { "generatePolicy" })
	public void makeInsuredCashDownPayment() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), this.myPolicyObj.busOwnLine.getPolicyNumber());

		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "makeInsuredCashDownPayment" })
    public void moveClocks1Month() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Month, 1);
	}

	@Test(dependsOnMethods = { "moveClocks1Month" })
	public void runInvoiceDueAndCheckDelinquency() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickBCMenuDelinquencies();

		this.targetDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		boolean delinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Open, myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNumber(), null, targetDate);

		if (!delinquencyFound) {
			Assert.fail("The Delinquency was either non-existant or not in an 'open' status.");
		}
		
		this.delinquentAmount = delinquency.getDelinquentAmount(OpenClosed.Open, myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNumber(), null, targetDate);

		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "runInvoiceDueAndCheckDelinquency" })
	public void verifyPartialNonPayCancelInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.busOwnLine.getPolicyNumber());

        PolicySummary summaryPage = new PolicySummary(driver);
		boolean partialNonPayCancelFound = summaryPage.verifyPendingCancellation(DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 20), CancellationStatus.Cancelling, true, this.delinquentAmount, 120);
		if (!partialNonPayCancelFound) {
			Assert.fail("The policy did not get a Partial Nonpay Cancellation activity from BC after waiting for 2 minutes.");
		}
		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "verifyPartialNonPayCancelInPolicyCenter" })
	public void removeLienholderCoveragesInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);

        StartPolicyChange policyChange = new StartPolicyChange(driver);
		policyChange.startPolicyChange("LH Partial Cancel",	DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 20));

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuBuildings();


        GenericWorkorderBuildings buildings = new GenericWorkorderBuildings(driver);
		buildings.removeBuildingOnLocation(1, 2);

        policyChange = new StartPolicyChange(driver);
		policyChange.quoteAndIssue();

		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "removeLienholderCoveragesInPolicyCenter" })
    public void moveClocks20Days() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 20);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);
	}
	
	@Test(dependsOnMethods = { "moveClocks20Days" })
    public void moveClocks2Days() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 2);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
	}
	
	@Test(dependsOnMethods = { "moveClocks2Days" })
	public void reinstateLHBuilding() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);

        StartPolicyChange policyChange = new StartPolicyChange(driver);
		policyChange.startPolicyChange("Reinstate LH Building W New LH", DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 20));

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuBuildings();


		ArrayList<AdditionalInterest> loc1Bldg2NewAdditionalInterests = new ArrayList<AdditionalInterest>();
		
		AdditionalInterest loc1Bldg2NewAddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1Bldg2NewAddInterest.setLoanContractNumber("LN 12345");
		loc1Bldg2NewAddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1Bldg2NewAdditionalInterests.add(loc1Bldg2NewAddInterest);
		loc1Bldg2.getAdditionalInterestList().clear();
		loc1Bldg2.setAdditionalInterestList(loc1Bldg2NewAdditionalInterests);

        GenericWorkorderBuildings buildings = new GenericWorkorderBuildings(driver);
		buildings.addBuildingOnLocation(true, 1, this.loc1Bldg2);
		
		location1.getBuildingList().clear();
		this.locOneBuildingList.clear();
		
		this.locOneBuildingList.add(loc1Bldg1);
		this.locOneBuildingList.add(loc1Bldg2);
		
		location1.setBuildingList(locOneBuildingList);
		
		this.locationsList.clear();
		this.locationsList.add(location1);

        sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPayerAssignment();

        GenericWorkorderPayerAssignment payerAssignment = new GenericWorkorderPayerAssignment(driver);
		payerAssignment.setPayerAssignmentBillCoveragesAsRequired(this.myPolicyObj);

        policyChange = new StartPolicyChange(driver);
		policyChange.quoteAndIssue();
		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);
		
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "reinstateLHBuilding" })
    public void moveClocksToFirstQuarterlyInvoiceDueDate() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        Date firstQuarter = DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getEffectiveDate(), DateAddSubtractOptions.Quarter, 1);
		ClockUtils.setCurrentDates(cf, firstQuarter);
	}
	
	@Test(dependsOnMethods = { "moveClocksToFirstQuarterlyInvoiceDueDate" })
    public void sendEntirePolicyDelinquent() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickBCMenuDelinquencies();

		this.targetDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		boolean delinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Open, myPolicyObj.accountNumber, null, targetDate);

		if (!delinquencyFound) {
			Assert.fail("The Delinquency was either non-existant or not in an 'open' status.");
		}
		
		this.delinquentAmount = delinquency.getDelinquentAmount(OpenClosed.Open, myPolicyObj.accountNumber, null, targetDate);

		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "sendEntirePolicyDelinquent" })
	public void verifyTotalNonPayCancelInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);

        StartCancellation policyCancel = new StartCancellation(driver);
		policyCancel.cancelPolicy(CancellationSourceReasonExplanation.NoPaymentReceived, "No Payment Recieved", DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 20), false, this.delinquentAmount);

		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "verifyTotalNonPayCancelInPolicyCenter" })
    public void moveClock20DaysAndCancelPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);

        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 20);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);

        AccountSummaryPC summaryPage = new AccountSummaryPC(driver);
        boolean cancelledStatusFound = summaryPage.verifyPolicyStatusInPolicyCenter(myPolicyObj.busOwnLine.getPolicyNumber(), PolicyTermStatus.Canceled, 300000);
		if (!cancelledStatusFound) {
			Assert.fail("The policy had not entered delinquency after 5 minutes of waiting.");
		}

		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "moveClock20DaysAndCancelPolicy" })
    public void moveClocks22Days() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 22);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
	}
	
	@Test(dependsOnMethods = { "moveClocks22Days" })
	public void rewriteRemainderOfTerm() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), this.myPolicyObj.accountNumber);

        StartRewrite policyRewrite = new StartRewrite(driver);
		policyRewrite.rewriteRemainderOfTerm(myPolicyObj);

		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "rewriteRemainderOfTerm" })
    public void moveClocks1Day() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
	}
	
	@Test(dependsOnMethods = { "moveClocks1Day" })
	public void changePayerAssignmentToInsured() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginToPCAndSearchPolicyByPolicyNumberAndTerm(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.busOwnLine.getPolicyNumber(), PolicyTermStatus.InForce);
		
		//THIS IS A TEMPORARY WORKAROUND NECESSARY FOR THE TIME-BEING. WHEN CLICKING ON A POLICY TERM IN THE SEARCH TABLE, PC IS ACTUALLY SENDING THE USER TO THE FIRST TERM, REGARDLESS OF
		//WHICH TERM WAS CHOSEN IN SEARCH. THIS WORKAROUND WILL CLICK THE ACCOUNT LINK, AND THEN CLICK THE CORRECT TERM LINK FROM THE POLICY TERM TABLE THERE. THIS WILL NEED TO BE REMOVED
		//ONCE THE DEFECT DESCRIBED ABOVE IS FILED AND FIXED!!!!! -BHILTBRAND DEFECT WORKAROUND TRACKING
        InfoBar infoBar = new InfoBar(driver);
		infoBar.clickInfoBarAccountNumber();
        AccountSummaryPC accountSumary = new AccountSummaryPC(driver);
		accountSumary.clickAccountSummaryPolicyTermByStatus(PolicyTermStatus.InForce);
        //END WORKAROUND

        StartPolicyChange policyChange = new StartPolicyChange(driver);
		policyChange.startPolicyChange("Change Payer Assignment to Insured", null);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPayerAssignment();


        GenericWorkorderPayerAssignment payerAssignment = new GenericWorkorderPayerAssignment(driver);
		payerAssignment.resetAllCoveragesBackToInsured();

        policyChange = new StartPolicyChange(driver);
		policyChange.quoteAndIssue();
		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);
		
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "changePayerAssignmentToInsured" })
    public void moveClocks28Days() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 28);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
	}
	
	@Test(dependsOnMethods = { "moveClocks28Days" })
	public void addUtilitiesCoverage() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginToPCAndSearchPolicyByPolicyNumberAndTerm(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.busOwnLine.getPolicyNumber(), PolicyTermStatus.InForce);
		
		//THIS IS A TEMPORARY WORKAROUND NECESSARY FOR THE TIME-BEING. WHEN CLICKING ON A POLICY TERM IN THE SEARCH TABLE, PC IS ACTUALLY SENDING THE USER TO THE FIRST TERM, REGARDLESS OF
		//WHICH TERM WAS CHOSEN IN SEARCH. THIS WORKAROUND WILL CLICK THE ACCOUNT LINK, AND THEN CLICK THE CORRECT TERM LINK FROM THE POLICY TERM TABLE THERE. THIS WILL NEED TO BE REMOVED
		//ONCE THE DEFECT DESCRIBED ABOVE IS FILED AND FIXED!!!!! -BHILTBRAND DEFECT WORKAROUND TRACKING
        InfoBar infoBar = new InfoBar(driver);
		infoBar.clickInfoBarAccountNumber();
        AccountSummaryPC accountSumary = new AccountSummaryPC(driver);
		accountSumary.clickAccountSummaryPolicyTermByStatus(PolicyTermStatus.InForce);
        //END WORKAROUND

        StartPolicyChange policyChange = new StartPolicyChange(driver);
		policyChange.startPolicyChange("Add Utilities Coverage to Building", null);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuBuildings();


        GenericWorkorderBuildings buildings = new GenericWorkorderBuildings(driver);
		buildings.clickBuildingsBuildingEdit(1);
        buildings.clickAdditionalCoverages();

		PolicyLocationBuildingAdditionalCoverages additionalCoveragesStuff = new PolicyLocationBuildingAdditionalCoverages();
		additionalCoveragesStuff.setUtilitiesDirectDamageCoverage(true);
		additionalCoveragesStuff.setUtilitiesDirectDamageCoverageAppliesTo(UtilitiesCoverageAppliesTo.BuildingAndBusinessPersonalProperty);
		additionalCoveragesStuff.setUtilitiesDirectDamageDirectLossLimit(500000);
		additionalCoveragesStuff.setUtilitiesDirectDamageWaterSupply(CoverageOrNot.Covered);
		additionalCoveragesStuff.setUtilitiesDirectDamageCommunicationsIncOHLines(CoverageOrNot.Covered);
		additionalCoveragesStuff.setUtilitiesDirectDamagePowerIncOHLines(CoverageOrNot.Covered);
		additionalCoveragesStuff.setUtilitiesTimeElementCoverage(true);
		additionalCoveragesStuff.setUtilitiesTimeElementCoverageAppliesTo(UtilitiesCoverageAppliesTo.BuildingAndBusinessPersonalProperty);
		additionalCoveragesStuff.setUtilitiesTimeElementDirectLossLimit(500000);
		additionalCoveragesStuff.setUtilitiesTimeElementWaterSupply(CoverageOrNot.Covered);
		additionalCoveragesStuff.setUtilitiesTimeElementCommunicationsIncOHLines(CoverageOrNot.Covered);
		additionalCoveragesStuff.setUtilitiesTimeElementPowerIncOHLines(CoverageOrNot.Covered);
		
		loc1Bldg1.setAdditionalCoveragesStuff(additionalCoveragesStuff);

        GenericWorkorderBuildingAdditionalCoverages buildingAdditionalCoverages = new GenericWorkorderBuildingAdditionalCoverages(driver);
		buildingAdditionalCoverages.setUtilitiesTimeElementCoverage(loc1Bldg1);

        buildings = new GenericWorkorderBuildings(driver);
		buildings.clickOK();
		

        policyChange = new StartPolicyChange(driver);
		policyChange.quoteAndIssue();
		
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "addUtilitiesCoverage" })
	public void verifyShortageDocumentInBC() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();

        AccountCharges accountCharges = new AccountCharges(driver);
        if (!accountCharges.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), TransactionType.Policy_Change, this.myPolicyObj.busOwnLine.getPolicyNumber())) {
			Assert.fail("The charges from the most recent policy change did not show up in BC after 2 minutes. Test cannot continue.");
		}
		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuInvoices();
		

        AccountInvoices invoices = new AccountInvoices(driver);
		try {
			invoices.getAccountInvoiceTableRow(DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf,ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, -28), null, null, InvoiceType.Shortage, null, InvoiceStatus.Carried_Forward, null, null);
		} catch (Exception e) {
			Assert.fail("The Invoice Item for the expected shortage was not found on the Invoices Table. Test Failed.");
		}

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuDocuments();


        BCCommonDocuments documents = new BCCommonDocuments(driver);
		boolean documentFound = documents.verifyDocument(DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf,ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, -28), "Shortage Bill");
		
		if (!documentFound) {
			Assert.fail("The Shortage Bill was not found on the documents page. Test Failed.");
		}
	}
	
	@Test(dependsOnMethods = { "verifyShortageDocumentInBC" })
    public void moveClocks1MonthSecondTime() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Month, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
	}
	
	@Test(dependsOnMethods = { "moveClocks1MonthSecondTime" })
	public void verifyShortageDocumentInBCAgain() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuInvoices();
		

        Date nextInvoiceDate = DateUtils.dateAddSubtract(DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getEffectiveDate(), DateAddSubtractOptions.Month, 7), DateAddSubtractOptions.Day, -20);
        AccountInvoices invoices = new AccountInvoices(driver);
		try {
			invoices.getAccountInvoiceTableRow(nextInvoiceDate, null, null, InvoiceType.Shortage, null, InvoiceStatus.Billed, null, null);
		} catch (Exception e) {
			Assert.fail("The Invoice Item for the expected shortage was not found on the Invoices Table. Test Failed.");
		}

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuDocuments();


        BCCommonDocuments documents = new BCCommonDocuments(driver);
		boolean documentFound = documents.verifyDocument(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), "Shortage Bill");
		
		if (!documentFound) {
			Assert.fail("The Shortage Bill was not found on the documents page. Test Failed.");
		}
	}
}

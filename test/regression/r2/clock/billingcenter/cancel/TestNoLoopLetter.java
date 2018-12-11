package regression.r2.clock.billingcenter.cancel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.common.BCCommonDocuments;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Cancellation.CancellationStatus;
import repository.gw.enums.CancellationEvent;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author bhiltbrand
* @Requirement This test verifies that when a policy partial cancels, but the delinquent amount is less than $10.00, the Loop Letter is not created.
* @RequirementsLink <a href="https://rally1.rallydev.com/#/68999021356d/detail/userstory/37180235891">Rally Story US5121</a>
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/13%20-%20Printed%20Documents/07%20-%20Partial%20Cancel%20Balance%20Due/13-07%20Partial%20Cancel%20Balance%20Due%20Document%20Change%20Request.docx">Delinquency Requirements Documentation</a>
* @Description 
* @DATE Jan 24, 2017
*/
public class TestNoLoopLetter extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private Date targetDate = null;
	private ARUsers arUser;
	private String lienholderNumber;
	private double delinquentAmount = 0.00;

	// ///////////////////
	// Main Test Methods//
	// ///////////////////

	@Test
    public void generatePolicy() throws Exception {
				
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		
		AdditionalInterest loc1Property1AdditionalInterest = new AdditionalInterest(ContactSubType.Company);
		loc1Property1AdditionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1Property1AdditionalInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
		loc1Property1AdditionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		
		PLPolicyLocationProperty location1Property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		location1Property1.setBuildingAdditionalInterest(loc1Property1AdditionalInterest);
		
		AdditionalInterest loc1Property2AdditionalInterest = new AdditionalInterest(ContactSubType.Company);
		loc1Property2AdditionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1Property2AdditionalInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
		loc1Property2AdditionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		
		PLPolicyLocationProperty location1Property2 = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises);
		location1Property2.setBuildingAdditionalInterest(loc1Property2AdditionalInterest);
		
		locOnePropertyList.add(location1Property1);
		locOnePropertyList.add(location1Property2);
		
		PolicyLocation locationOne = new PolicyLocation(locOnePropertyList);
		locationOne.setPlNumResidence(2);
		locationsList.add(locationOne);
		
		SquireLiability liabilitySection = new SquireLiability();
		liabilitySection.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("Non-Pay1", "Cancel")
				.withInsPrimaryAddress(locationsList.get(0).getAddress())
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		this.lienholderNumber = myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(1).getAdditionalInterestList().get(0).getLienholderNumber();
	}
	
	@Test(dependsOnMethods = { "generatePolicy" })
	public void makeInsuredDownPayment() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.squire.getPremium().getDownPaymentAmount(), myPolicyObj.squire.getPolicyNumber());
		
		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "makeInsuredDownPayment" })
	public void moveClocks() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		ClockUtils.setCurrentDates(cf,DateAddSubtractOptions.Month, 1);
	}
	
	@Test(dependsOnMethods = { "moveClocks" })
    public void makeFirstLienholderDownPayment() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLienholderNumber());

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeLienHolderPaymentExecute(this.myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount(), myPolicyObj.squire.getPolicyNumber(), this.myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLoanContractNumber());
		
		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "makeFirstLienholderDownPayment" })
	public void runInvoiceDueAndCheckDelinquency() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickBCMenuDelinquencies();

		this.targetDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		boolean delinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Open, this.lienholderNumber, null, targetDate);

		if (!delinquencyFound) {
			Assert.fail("The Delinquency was either non-existant or not in an 'open' status.");
		}

        this.delinquentAmount = delinquency.getDelinquentAmount(OpenClosed.Open, null, myPolicyObj.squire.getPolicyNumber(), DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter));
		
		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "runInvoiceDueAndCheckDelinquency" })
	public void verifyPartialNonPayCancelInPolicyCenter() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        
		new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.squire.getPolicyNumber());

        PolicySummary summaryPage = new PolicySummary(driver);
		boolean partialNonPayCancelFound = summaryPage.verifyPendingCancellation(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 20), CancellationStatus.Cancelling, true, this.delinquentAmount, 120);
		if (!partialNonPayCancelFound) {
			Assert.fail("The policy did not get a Partial Nonpay Cancellation activity from BC after waiting for 2 minutes.");
		}
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "verifyPartialNonPayCancelInPolicyCenter" })
    public void moveClocks2() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 20);
	}

	@Test(dependsOnMethods = { "moveClocks2" })
    public void removeLienholderCoveragesInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.squire.getPolicyNumber());

        StartPolicyChange policyChange = new StartPolicyChange(driver);
		removeBuildingOnLocationInPL("2");
		new GuidewireHelpers(driver).logout();
	}
	
	private void removeBuildingOnLocationInPL(String bldgNum) throws Exception {
		StartPolicyChange policyChange = new StartPolicyChange(driver);
		policyChange.startPolicyChange("remove a building", null);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquireProperty();
        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
        propertyDetail.removeBuilding(bldgNum);
        policyChange.quoteAndIssue();
    }

	@Test(dependsOnMethods = { "removeLienholderCoveragesInPolicyCenter" })
    public void verifyDelinquencyStepsInBillingCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickBCMenuDelinquencies();

		verifyDelinquencyEventCompletion(CancellationEvent.SendNoticeOfIntentToCancel);

        menu = new BCAccountMenu(driver);
		menu.clickBCMenuCharges();

        AccountCharges charges = new AccountCharges(driver);
		if (!charges.waitUntilChargesFromTransactionDescriptionArrive(120, "remove a building")) {
			Assert.fail("The charges from the partial cancel did not come over to Billing Center within 2 minutes.");
		}

        menu = new BCAccountMenu(driver);
		menu.clickAccountMenuInvoices();

        AccountInvoices invoices = new AccountInvoices(driver);
		int invoiceRowNumber = 0;
		try {
            invoiceRowNumber = new TableUtils(driver).getRowNumberFromWebElementRow(invoices.getAccountInvoiceTableRow(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), null, null, InvoiceType.LienholderOnset, this.myPolicyObj.squire.getPolicyNumber(), InvoiceStatus.Planned, null, null));
		} catch (Exception e) {
			Assert.fail("The expected Lienholder Onset invoice was not found in the invoices table.");
		}
		double invoiceAmount = invoices.getInvoiceDueAmountByRowNumber(invoiceRowNumber);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        menu = new BCAccountMenu(driver);
		menu.clickBCMenuDelinquencies();

        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		boolean delinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Closed, this.lienholderNumber, null, targetDate);
		
		if (!delinquencyFound) {
			Assert.fail("The Original LH Delinquency was either non-existant or not in a 'Closed' status.");
		}
		
		boolean loopLetterDelinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Open, this.myPolicyObj.accountNumber, null, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter));

		if (!loopLetterDelinquencyFound) {
			Assert.fail("The New Loop Letter Delinquency was either non-existant or not in an 'Open' status.");
		}
		
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.BC_Workflow);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuDocuments();

        BCCommonDocuments accountDocuments = new BCCommonDocuments(driver);
		if (!accountDocuments.verifyDocument(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), "Partial Cancel Balance Due")) {
			Assert.fail("The First Partial Cancel Balance Due Document was not found on the document page. Test Failed.");
		}
		
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Month, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.BC_Workflow);

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuSummary();
        accountMenu.clickBCMenuDocuments();

        accountDocuments = new BCCommonDocuments(driver);
		if (!accountDocuments.verifyDocument(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), "Partial Cancel Balance Due")) {
			Assert.fail("The Second Partial Cancel Balance Due Document was not found on the document page. Test Failed.");
		}

        accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeDirectBillPaymentExecute(invoiceAmount - 9.99, this.myPolicyObj.squire.getPolicyNumber());
		
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Month, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.BC_Workflow);

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuSummary();
        accountMenu.clickBCMenuDocuments();

        accountDocuments = new BCCommonDocuments(driver);
		if (accountDocuments.verifyDocument(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), "Partial Cancel Balance Due")) {
			Assert.fail("The Third Partial Cancel Balance Due Document was found on the document page, even though payment was made to get the delinquent amount less than 10 dollars. Test Failed.");
		}
		
		new GuidewireHelpers(driver).logout();
	}

	// //////////////////////////////////////////////
	// Private Methods Used to support Test Methods//
	// //////////////////////////////////////////////
	
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
}

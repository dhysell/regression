package regression.r2.clock.billingcenter.cancel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.common.BCCommonDocuments;
import repository.bc.common.BCCommonTroubleTickets;
import repository.driverConfiguration.Config;
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
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
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
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author bhiltbrand
* @Requirement This test verifies that when a policy cancelled, but the delinquent amount is less than $10.00, the First and Final Notice of Cancelled Balance Due are not created.
* @RequirementsLink <a href="https://rally1.rallydev.com/#/68999021356d/detail/userstory/37180084379">Rally Story US5120</a>
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/13%20-%20Printed%20Documents/08%20-%20First%20Notice%20Balance%20Due/13-08%20First%20Notice%20Balance%20Due%20Document%20Change%20Request.docx">Delinquency Requirements Documentation</a>
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/13%20-%20Printed%20Documents/09%20-%20Final%20Notice%20Balance%20Due/13-09%20Final%20Notice%20Balance%20due%20Document%20Change%20Request.docx">Delinquency Requirements Documentation</a>
* @Description 
* @DATE Jan 24, 2017
*/
public class TestNoNoticeOfCancelBalanceDue extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private Date targetDate = null;
	private ARUsers arUser;
	private Date firstNoticeOfCancelledBalanceDueDocumentDate = null;
	private Date finalNoticeOfCancelledBalanceDueDocumentDate = null;

	// ///////////////////
	// Main Test Methods//
	// ///////////////////

	@Test
    public void generatePolicy() throws Exception {
				
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
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("Non-Pay2", "Cancel")
				.withInsPrimaryAddress(locationsList.get(0).getAddress())
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
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.squire.getPolicyNumber());

        BCCommonTroubleTickets troubleTicket = new BCCommonTroubleTickets(driver);
		troubleTicket.closeFirstTroubleTicket();

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "runInvoiceWithoutMakingDownpayment" })
	public void moveClocks() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 10);
	}

	@Test(dependsOnMethods = { "moveClocks" })
	public void runInvoiceDueAndCheckDelinquency() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        double paymentAmount = NumberUtils.round((this.myPolicyObj.squire.getPremium().getTotalGrossPremium() / 365 * 30), 2);
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(myPolicyObj, paymentAmount, myPolicyObj.squire.getPolicyNumber());

        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickBCMenuDelinquencies();

		this.targetDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
        boolean firstPolicyDelinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Open, myPolicyObj.accountNumber, myPolicyObj.squire.getPolicyNumber(), targetDate);
	
		if (!firstPolicyDelinquencyFound) {
			Assert.fail("The first Delinquency was either non-existant or not in an 'open' status.");
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
		if (!firstPolicyCancelledStatusFound) {
			Assert.fail("The first policy had not entered delinquency after 5 minutes of waiting.");
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

		moveThroughDelinquencySteps();

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuDocuments();

        BCCommonDocuments accountDocuments = new BCCommonDocuments(driver);
		if (accountDocuments.verifyDocument(this.firstNoticeOfCancelledBalanceDueDocumentDate, "First Notice Balance Due On Cancelled Policy")) {
			Assert.fail("The First Notice of Balance Due was found on the document page, even though the delinquent amount was less than 10 dollars and the document should not have been created. Test Failed.");
		}
		if (accountDocuments.verifyDocument(this.finalNoticeOfCancelledBalanceDueDocumentDate, "Final Notice Balance Due On Cancelled Policy")) {
			Assert.fail("The Final Notice of Balance Due was found on the document page, even though the delinquent amount was less than 10 dollars and the document should not have been created. Test Failed.");
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

        double currentDelinquentAmount = delinquency.getDelinquentAmount(OpenClosed.Open, myPolicyObj.accountNumber, myPolicyObj.squire.getPolicyNumber(), targetDate);
		if (currentDelinquentAmount > 10.00) {
			Double amountToPay = currentDelinquentAmount - 9.99;
            NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
            newPayment.makeInsuredDownpayment(myPolicyObj, amountToPay, myPolicyObj.squire.getPolicyNumber());
        }
		driver.navigate().refresh();
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);

		new BatchHelpers(driver).runBatchProcess(BatchProcess.BC_Workflow);

		driver.navigate().refresh();
		verifyDelinquencyEventCompletion(CancellationEvent.SendBalanceDue);
		this.firstNoticeOfCancelledBalanceDueDocumentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);

		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 21);

		new BatchHelpers(driver).runBatchProcess(BatchProcess.BC_Workflow);

		driver.navigate().refresh();
		verifyDelinquencyEventCompletion(CancellationEvent.SendSecondBalanceDue);
		this.finalNoticeOfCancelledBalanceDueDocumentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
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
}

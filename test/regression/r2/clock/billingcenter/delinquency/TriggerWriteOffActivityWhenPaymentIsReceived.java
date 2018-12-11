package regression.r2.clock.billingcenter.delinquency;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonActivities;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.actions.DesktopActionsMultiplePayment;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.search.BCSearchPolicies;
import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.ActivityStatus;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.CancellationEvent;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.PaymentInstrumentEnum;
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
 * @Author sreddy
 * @Requirement : US5348   Write-offs: Activity to let user know to reverse collections write-off when payment is received
 * @Description : 1. Issue a policy with Quarterly Payment plan
 * 2. On BC make the account go delinquent and complete delinquency steps and write off
 * 3. Make a payment and check details on activity "Payment Received on Policy with Collections Write-off"
 * @DATE Feb 15, 2017
 */
@QuarantineClass
public class TriggerWriteOffActivityWhenPaymentIsReceived extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private Date targetDate = null;
	private Date currentDate;
	private double delinquentAmount = 0;
	@SuppressWarnings("unused")
	private double unbilledAmount = 0;
	private ARUsers arUser;
	private double aaAmount1 = 10.00;
	private String activitySubject="Payment Received on Policy with Collections Write-off";

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
        .withInsFirstLastName("TriggerWriteOff", "Activity")
        .withInsPrimaryAddress(locationsList.get(0).getAddress())
        .withPaymentPlanType(PaymentPlanType.Annual)
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
      new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.squire.getPolicyNumber());

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
    new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

    new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

      BCAccountMenu menu = new BCAccountMenu(driver);
    menu.clickBCMenuDelinquencies();

    this.targetDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
      BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
      boolean delinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Open, myPolicyObj.accountNumber, myPolicyObj.squire.getPolicyNumber(), targetDate);

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
      boolean pendingCancelFound = summaryPage.verifyCancellationInPolicyCenter(myPolicyObj.squire.getPolicyNumber(), 120);
    if (!pendingCancelFound) {
      Assert.fail("The policy did not get a pending cancellation transaction from BC after waiting for 2 minutes.");
    }

      ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 20);

    new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);

      summaryPage = new AccountSummaryPC(driver);
      boolean cancelledStatusFound = summaryPage.verifyPolicyStatusInPolicyCenter(myPolicyObj.squire.getPolicyNumber(), PolicyTermStatus.Canceled, 300);
    if (!cancelledStatusFound) {
      Assert.fail("The policy had not entered delinquency after 5 minutes of waiting.");
    }

    new GuidewireHelpers(driver).logout();
  }

  @Test(dependsOnMethods = { "verifyCancelationCompletionInPolicyCenter" })
  public void triggerAndVerifyPaymentReceivedOnPolicyWithCollectionsWriteOffActivity() throws Exception {
	  Config cf = new Config(ApplicationOrCenter.BillingCenter);
	  driver = buildDriver(cf);
    new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

      BCAccountMenu menu = new BCAccountMenu(driver);
    menu.clickBCMenuDelinquencies();

      BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
      this.delinquentAmount = delinquency.getDelinquentAmount(OpenClosed.Open, myPolicyObj.accountNumber, myPolicyObj.squire.getPolicyNumber(), targetDate);
    this.unbilledAmount = delinquency.getUnbilledAmount();

      menu = new BCAccountMenu(driver);
    menu.clickBCMenuDelinquencies();

    moveThroughDelinquencySteps();

      BCTopMenu topMenu1 = new BCTopMenu(driver);
	topMenu1.clickDesktopTab();

      BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
	desktopMenu.clickDesktopMenuActionsMultiplePayment();
      DesktopActionsMultiplePayment multiplePaymentsPage = new DesktopActionsMultiplePayment(driver);

      multiplePaymentsPage.makeMultiplePayment(myPolicyObj.squire.getPolicyNumber(), PaymentInstrumentEnum.Cash, aaAmount1);

      BCSearchPolicies search = new BCSearchPolicies(driver);
      search.searchPolicyByPolicyNumber(myPolicyObj.squire.getPolicyNumber());

      BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
	policyMenu.clickBCMenuActivities();

	currentDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);

      BCCommonActivities activity = new BCCommonActivities(driver);
      try {
		activity.clickActivityTableSubject(currentDate, DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 3), null, ActivityStatus.Open, activitySubject);
	}catch(Exception e){
		Assert.fail("Did Not Trigger The Activity : "+ activitySubject);
	}

      //verify the activity details
	Map<String, String> paymentReceivedOnPolicyWithCollectionsWriteOff = new LinkedHashMap<String, String>() {	
		private static final long serialVersionUID = 1L;
		{
			this.put("Subject", activitySubject);
			this.put("Description", "Payment or credit received on a policy that has a past due amount written off with a reason of collections. The write off may need to be reversed and TSI may need to be notified.");
			this.put("Priority", "High");
			this.put("Due Date", DateUtils.dateFormatAsString("MM/dd/yyyy", DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 3)));
			this.put("Escalation Date", DateUtils.dateFormatAsString("MM/dd/yyyy", DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 5)));
			this.put("Account", myPolicyObj.fullAccountNumber);
            this.put("Policy Period", myPolicyObj.squire.getPolicyNumber() + "-1");
			this.put("Assigned to", "AR Supervisor Farm Bureau - Disbursement Specialist Farm Bureau");
			this.put("Status", "Open");					
	}};

      if(!activity.verifyActivityInfo(paymentReceivedOnPolicyWithCollectionsWriteOff)) {
		Assert.fail(activitySubject + " : Activity verification failed.");
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
      this.delinquentAmount = delinquency.getDelinquentAmount(OpenClosed.Open, myPolicyObj.accountNumber, myPolicyObj.squire.getPolicyNumber(), targetDate);
    writeOffWorkflow();
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
    if (this.delinquentAmount > 50) {
      amountToPay = this.delinquentAmount - 49;
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

      boolean delinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Closed, myPolicyObj.accountNumber, myPolicyObj.squire.getPolicyNumber(), targetDate);

      if (!delinquencyFound) {
      Assert.fail("The Delinquency finished the workflow, but remained open.");
    }
  }
}

package regression.r2.clock.policycenter.reinstate;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.account.payments.AccountPaymentsPaymentRequests;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.search.BCSearchPolicies;
import repository.driverConfiguration.Config;
import repository.gw.enums.DateDifferenceOptions;
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
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.reinstate.StartReinstate;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

//import gw.enums.InvoiceType;
/**
 * @Author nvadlamudi
 * @Requirement :DE4318: Reinstate -- Trying to Reinstate Doesn't Work Because of Dues
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Dec 8, 2016
 * @Author bmartin
 * @Requirement DE5534: BC is sending zero dollar for NSF full cancel
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/46478569317d/detail/defect/116450021164">Rally defect DE5534</a>
 * @Description Create policy, cancel for non-pay, reinstate, renew, make first and second payment, move to third payment billed, reverse first two payments, check crystal ball BC did not send a zero dollar amount, and PC does not show zero dollars on the pending cancellation.
 * @DATE Jun 22, 2017
 */

/**
 * @Author bmartin
 * @Requirement DE5534: BC is sending zero dollar for NSF full cancel
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/46478569317d/detail/defect/116450021164">Rally defect DE5534</a>
 * @Description Create policy, cancel for non-pay, reinstate, renew, make first and second payment, move to third payment billed, reverse first two payments, check crystal ball BC did not send a zero dollar amount, and PC does not show zero dollars on the pending cancellation.
 * @DATE Jun 22, 2017
 */
@QuarantineClass
public class TestPLReinstatePolicy extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy mySQPolicyObjPL = null;
	private Date invoiceDate2;
	private String userName;
	private String arUsername;
	private String arPassword;
	private String uwUserName;
	private String uwPassword;
	private String accountNumber;
	private String policyNumber;

	@Test()
	private void testIssueSquirePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		PLPolicyLocationProperty prop = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		locOnePropertyList.add(prop);
		locationsList.add(new PolicyLocation(locOnePropertyList));

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        mySQPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withPolTermLengthDays(70)
				.withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("Test", "Reinstate")
				.withPaymentPlanType(repository.gw.enums.PaymentPlanType.Quarterly)
				.withDownPaymentType(repository.gw.enums.PaymentType.Cash)
				.build(repository.gw.enums.GeneratePolicyType.PolicyIssued);
		
		userName = mySQPolicyObjPL.agentInfo.getAgentUserName();
		uwUserName = mySQPolicyObjPL.underwriterInfo.getUnderwriterUserName();
		uwPassword = mySQPolicyObjPL.underwriterInfo.getUnderwriterPassword();
		accountNumber = mySQPolicyObjPL.accountNumber;
        policyNumber = mySQPolicyObjPL.squire.getPolicyNumber();

		System.out.println("#############\nAccount Number: " + accountNumber);
		System.out.println("Policy Number: " + policyNumber);
		System.out.println("Under Writer: " + uwUserName);
		System.out.println("Agent: " + userName + "\n#############");

	}

	@Test(dependsOnMethods = {"testIssueSquirePolicy"}, enabled = true)
	private void moveClockRunBatches() throws Exception {
		ARUsers arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		
		arUsername = arUser.getUserName();
		arPassword = arUser.getPassword();
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUsername, arPassword, accountNumber);
		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);

		//Move clock 1 day
		ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 1);


		//Run Invoice due
		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.BC_Workflow);

		//Move clock 20 days
		ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 20);


		//Run Workflows
		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.BC_Workflow);
        BCSearchPolicies search = new BCSearchPolicies(driver);
		search.searchPolicyByPolicyNumber(policyNumber);


        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
		policyMenu.clickBCMenuTroubleTickets();

        BCCommonTroubleTickets troubleTicket = new BCCommonTroubleTickets(driver);
		troubleTicket.closeFirstTroubleTicket();

		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.BC_Workflow);
        //delay required
	}

	@Test (dependsOnMethods = {"moveClockRunBatches"}, enabled = true)
	private void testMoveClockToCancelPolicy() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uwUserName, uwPassword, accountNumber);

        PolicySummary summary = new PolicySummary(driver);
		String transactionEffDate = summary.getPendingPolicyTransactionByColumnName(repository.gw.enums.TransactionType.Cancellation.getValue(), "Trans Eff Date");

		int noOfDays = DateUtils.getDifferenceBetweenDates(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateUtils.convertStringtoDate(transactionEffDate, "MM/dd/yyyy"), DateDifferenceOptions.Day);

		ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, (noOfDays+3));

		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.PC_Workflow);
    }
	
	@Test (dependsOnMethods = {"testMoveClockToCancelPolicy"}, enabled = true)
    public void testReinstatePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uwUserName, uwPassword, accountNumber);
        PolicyMenu policyMenu = new PolicyMenu(driver);
		policyMenu.clickMenuActions();
		policyMenu.clickReinstatePolicy();
        String errorMessage = "";
        StartReinstate reinstatePolicy = new StartReinstate(driver);
		reinstatePolicy.setReinstateReason("Payment received");
        StartCancellation cancelPol = new StartCancellation(driver);
		cancelPol.setDescription("Testing purpose");

		reinstatePolicy.quoteAndIssue();

        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
		policySearchPC.searchPolicyByAccountNumber(accountNumber);

        PolicySummary summary = new PolicySummary(driver);
		if(summary.getTransactionNumber(repository.gw.enums.TransactionType.Reinstatement, "Testing purpose") == null){
			errorMessage = errorMessage + "Reinstatement is not done for current term !!!\n";
		} 
	} 

	@Test (dependsOnMethods = {"testReinstatePolicy"}, enabled = true)
    public void payCurrentDue() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUsername, arPassword, accountNumber);
		
		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);

        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();

        AccountInvoices acctInvoice = new AccountInvoices(driver);
		double dueAmt= acctInvoice.getInvoiceAmountByRowNumber(1);
		
		if(dueAmt !=0.0 ){
            acctMenu.clickAccountMenuActionsNewDirectBillPayment();

            NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
			directPayment.makeDirectBillPaymentExecute(dueAmt, accountNumber);
		} else {
			System.out.println("Payment already made by PC ");
		}
	}
	
	@Test(dependsOnMethods = { "payCurrentDue" })
	public void manualRenewal() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(arUsername, arPassword, policyNumber );

        PolicySummary summaryPage = new PolicySummary(driver);

        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickRenewPolicy();
        

        InfoBar infoBar = new InfoBar(driver);
		infoBar.clickInfoBarPolicyNumber();
        if (summaryPage.checkIfActivityExists("Pre-Renewal")) {
			summaryPage.clickViewPreRenewalDirection();
            PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);

			boolean preRenewalDirectionExists = preRenewalPage.checkPreRenewalExists();
			if (preRenewalDirectionExists) {
				preRenewalPage.clickViewInPreRenewalDirection();
				preRenewalPage.closeAllPreRenewalDirectionExplanations();
				preRenewalPage.clickClosePreRenewalDirection();
				preRenewalPage.clickReturnToSummaryPage();
            }
        }
        StartRenewal renewal = new StartRenewal(driver);
		renewal.quoteAndIssueRenewal(repository.gw.enums.RenewalCode.Renew_Good_Risk, mySQPolicyObjPL);
    }

	
	@Test (dependsOnMethods = { "manualRenewal" })
    public void moveClockForNxtInvoiceDate() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUsername, arPassword, accountNumber);
		
		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);

        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
        AccountInvoices acctInvoice = new AccountInvoices(driver);
		WebElement billedRow = acctInvoice.getAccountInvoiceTableRow(null, null, null, null, null, repository.gw.enums.InvoiceStatus.Billed, null, null);
		int billedStatusRow = Integer.valueOf(billedRow.findElement(By.xpath(".//parent::td/parent::tr")).getAttribute("data-recordindex")) + 1;	
		invoiceDate2 = DateUtils.getDateValueOfFormat(acctInvoice.getListOfDueDates().get(billedStatusRow), "MM/dd/yyyy");
        ClockUtils.setCurrentDates(cf, invoiceDate2);
		
		double dueAmt= acctInvoice.getInvoiceDueAmountByRowNumber(billedStatusRow);
		
		if (dueAmt != 0.0 ) {
            acctMenu.clickAccountMenuActionsNewDirectBillPayment();

            NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
			directPayment.makeDirectBillPaymentExecute(dueAmt, accountNumber);
		}else {
			System.out.println("Payment already made by PC ");
		}
		
		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);

        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
        acctInvoice = new AccountInvoices(driver);
		billedRow = acctInvoice.getAccountInvoiceTableRow(null, null, null, null, null, repository.gw.enums.InvoiceStatus.Billed, null, null);
		billedStatusRow = Integer.valueOf(billedRow.findElement(By.xpath(".//parent::td/parent::tr")).getAttribute("data-recordindex")) + 1;	
		invoiceDate2 = DateUtils.getDateValueOfFormat(acctInvoice.getListOfDueDates().get(billedStatusRow), "MM/dd/yyyy");
        ClockUtils.setCurrentDates(cf, invoiceDate2);
		
		double dueAmt2= acctInvoice.getInvoiceDueAmountByRowNumber(billedStatusRow);
		
		 if (dueAmt2 != 0.0 ) {
             acctMenu.clickAccountMenuActionsNewDirectBillPayment();

             NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
			directPayment.makeDirectBillPaymentExecute(dueAmt2, accountNumber);
		} else {
			System.out.println("Payment already made by PC ");
		}
		
		ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 1);

        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuPaymentsPayments();

        AccountPayments payment = new AccountPayments(driver);
		payment.reversePayment(dueAmt, dueAmt, null	, repository.gw.enums.PaymentReversalReason.Payment_Modification);
		payment.reversePayment(dueAmt2, dueAmt2, null	, repository.gw.enums.PaymentReversalReason.Payment_Modification);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);
		
	}
	
	@Test(dependsOnMethods = { "moveClockForNxtInvoiceDate" }, enabled = false)
    public void chechPaymentRequest() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUsername, arPassword, accountNumber);
		
		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuPaymentsPaymentRequests();
		
		AccountPaymentsPaymentRequests acctPaymentReq = new AccountPaymentsPaymentRequests(driver);
		acctPaymentReq.checkPaymentRequestStatus(invoiceDate2, null, null, null, repository.gw.enums.Status.Created);

	}
	
}

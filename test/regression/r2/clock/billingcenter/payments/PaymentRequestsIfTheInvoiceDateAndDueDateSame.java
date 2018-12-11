package regression.r2.clock.billingcenter.payments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.payments.AccountPayments;
import repository.bc.account.payments.AccountPaymentsPaymentRequests;
import repository.bc.common.BCCommonDelinquencies;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentReturnedPaymentReason;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.TransactionType;
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
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.reinstate.StartReinstate;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author sgunda
 * @Requirement US13962 - **HOT-FIX** Do not create Payment Request on invoices that are billed and due same day
 * @RequirementsLink <a href=https://rally1.rallydev.com/#/7832667974d/detail/userstory/192523628672>Rally Defect US13962</a>
 * @DATE Mar 1, 2018
 */
public class PaymentRequestsIfTheInvoiceDateAndDueDateSame extends BaseTest {
	private WebDriver driver;
    private ARUsers arUser;
    private GeneratePolicy myPolicyObj;
    private Date revDate;
    private AccountPayments payment;
    private BCAccountMenu acctMenu;
    private AccountPaymentsPaymentRequests paymentRequest;
    private TransactionType transactionType;
    private double dueAmount;
    private Date invoiceDate;
    private int dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

    @Test()
    public void createPolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        PLPolicyLocationProperty locationOneProperty = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        locOnePropertyList.add(locationOneProperty);
        locationsList.add(new PolicyLocation(locOnePropertyList));

        SquireLiability liabilitySection = new SquireLiability();

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withInsFirstLastName("PL", "Policy")
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withPaymentPlanType(PaymentPlanType.Monthly)
                .withDownPaymentType(PaymentType.ACH_EFT)
                .build(GeneratePolicyType.PolicyIssued);
        System.out.println("Account number is = " + myPolicyObj.accountNumber);

    }

    @Test(dependsOnMethods = {"createPolicy"})
    public void reversePaymentInBCAndMakeAccountDelinquent() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 10);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Release_Trouble_Ticket_Holds);

        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuPayments();

        payment = new AccountPayments(driver);
        revDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
        payment.reversePaymentAtFault(null,new GuidewireHelpers(driver).getPolicyPremium(myPolicyObj).getDownPaymentAmount(), null, null, PaymentReturnedPaymentReason.AccountFrozen);

        acctMenu.clickBCMenuDelinquencies();
        BCCommonDelinquencies acctDelinquency = new BCCommonDelinquencies(driver);
        Assert.assertTrue(acctDelinquency.verifyDelinquencyByReason(OpenClosed.Open, DelinquencyReason.PastDueFullCancel,null, revDate));

        AccountInvoices acctInvoices = new AccountInvoices(driver);
        acctMenu.clickAccountMenuInvoices();
        acctInvoices.clickAccountInvoicesTableRowByRowNumber(2);
        ClockUtils.setCurrentDates(cf, acctInvoices.getListOfInvoiceDates().get(1));
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
    }

    @Test(dependsOnMethods = {"reversePaymentInBCAndMakeAccountDelinquent"})
    public void cancelPolicyInPC() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        PolicySummary policySummary = new PolicySummary(driver);
        ClockUtils.setCurrentDates(cf, policySummary.getTransactionEffectiveDate(TransactionType.Cancellation));

        new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);
    }

    @Test(dependsOnMethods = {"cancelPolicyInPC"})
    public void verifyBCRecivedCancellationCharges() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickBCMenuCharges();
        AccountCharges acctCharges = new AccountCharges(driver);
        acctCharges.waitUntilChargesFromPolicyContextArrive(180, TransactionType.Cancellation);

    }

    @Test(dependsOnMethods = {"verifyBCRecivedCancellationCharges"})
    public void rewriteOrReinstateInPC() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        switch (dayOfMonth % 2) {
            case 0:
                try {
                    System.out.println("Reinstating policy");
                    transactionType = TransactionType.Reinstatement;
                    PolicyMenu policyMenu = new PolicyMenu(driver);
                    policyMenu.clickMenuActions();
                    policyMenu.clickReinstatePolicy();
                    StartReinstate reinstatePolicy = new StartReinstate(driver);
                    reinstatePolicy.setReinstateReason("Payment received");

                    reinstatePolicy.quoteAndIssue();

                } catch (Exception e) {
                    Assert.fail("Reinstate failed");
                }
                break;
            case 1:
                try {
                    System.out.println("Rewrite policy");
                    transactionType = TransactionType.Rewrite;
                    PolicyMenu policyMenu = new PolicyMenu(driver);
                    policyMenu.clickMenuActions();
                    policyMenu.clickReinstatePolicy();
                    StartRewrite rewritePolicy = new StartRewrite(driver);
                    rewritePolicy.rewriteFullTermGuts(myPolicyObj);
                    SideMenuPC sideMenu = new SideMenuPC(driver);
                    sideMenu.clickSideMenuRiskAnalysis();
                    GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
                    risk.approveAll();
                    rewritePolicy.clickIssuePolicy();
                } catch (Exception e) {
                    Assert.fail("Rewrite failed");
                }
                break;
            default:
                try {
                    System.out.println("Rewrite policy");
                    transactionType = TransactionType.Rewrite;
                    StartRewrite rewritePolicy = new StartRewrite(driver);
                    rewritePolicy.rewriteNewTerm(LineSelection.PropertyAndLiabilityLinePL);
                } catch (Exception e) {
                    Assert.fail("Rewrite failed");
                }
                break;
        }
    }

    @Test(dependsOnMethods = {"rewriteOrReinstateInPC"})
    public void verifyIfPaymentRequestIsCreated() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        acctMenu.clickBCMenuCharges();
        AccountCharges acctCharges = new AccountCharges(driver);
        acctCharges.waitUntilChargesFromPolicyContextArrive(180, transactionType);

        AccountInvoices acctInvoices = new AccountInvoices(driver);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        acctMenu.clickAccountMenuInvoices();
        invoiceDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
        dueAmount = acctInvoices.getInvoiceDueAmountByDueDate(invoiceDate);
        String invoiceNumber = acctInvoices.getInvoiceNumber(invoiceDate, invoiceDate, null,null);
        Assert.assertTrue(acctInvoices.verifyInvoice(invoiceDate, invoiceDate, null, InvoiceType.Shortage, null, InvoiceStatus.Billed, null, null), "Invoice was not found or something is wrong");
        acctMenu.clickAccountMenuPaymentsPaymentRequests();
        paymentRequest = new AccountPaymentsPaymentRequests(driver);
        Assert.assertFalse(paymentRequest.verifyPaymentRequest(null, null, null, null, null, null, invoiceNumber, null, null, dueAmount), "Payment request created, This should now happend");

    }

}


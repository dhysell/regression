package previousProgramIncrement.pi3_090518_111518.maintenanceDefects.NoExceptions2;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.payments.AccountPayments;
import repository.bc.common.BCCommonCharges;
import repository.bc.common.BCCommonDelinquencies;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.InvoiceStatus;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GeneratePolicyHelper;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

import java.util.Date;


@Test(groups = {"ClockMove", "BillingCenter"})
public class DE7831WorkflowGoesToCancelledStepAutomaticallyNoExceptions extends BaseTest {

    private GeneratePolicy generatePolicy;
    private WebDriver driver;
    private ARUsers arUser;
    private Date date;

    @Test
    public void generatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        generatePolicy = new GeneratePolicyHelper(driver).generatePLSectionIAndIIWithAutoPLPolicy("DE7831","RenewalCancel",59, repository.gw.enums.PaymentPlanType.Monthly, repository.gw.enums.PaymentType.ACH_EFT);
    }

    @Test(dependsOnMethods = {"generatePolicy"})
    public void makePaymentsAndReverseItToMakePolicyCashOnly() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), generatePolicy.accountNumber);

        new BCAccountMenu(driver).clickAccountMenuPaymentsPayments();
        new AccountPayments(driver).waitUntilBindPaymentsArrive(240);

        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);
        new BCAccountMenu(driver).clickAccountMenuInvoices();
        Assert.assertTrue(new AccountInvoices(driver).verifyInvoice(null, null, null, repository.gw.enums.InvoiceType.NewBusinessDownPayment, null, InvoiceStatus.Billed, null, 0.00), "Down payment done with ACH in PC has not yet made to BC or something went wrong ");

        date = repository.gw.helpers.DateUtils.getCenterDate(driver,ApplicationOrCenter.BillingCenter);

        repository.gw.helpers.ClockUtils.setCurrentDates(driver, repository.gw.enums.DateAddSubtractOptions.Day, 1);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);

        repository.gw.helpers.ClockUtils.setCurrentDates(driver, repository.gw.enums.DateAddSubtractOptions.Day, 16);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);
    }

    @Test(dependsOnMethods = {"makePaymentsAndReverseItToMakePolicyCashOnly"})
    public void renewPolicyInPolicyCenter() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new StartRenewal(driver).loginAsUWAndIssueRenewal(generatePolicy);


    }
    @Test(dependsOnMethods = {"renewPolicyInPolicyCenter"})
    public void verifyIfRenewalChargesMadeItToBC() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), generatePolicy.accountNumber);
        new BCAccountMenu(driver).clickBCMenuCharges();
        Assert.assertTrue(new BCCommonCharges(driver).waitUntilChargesFromPolicyContextArrive(repository.gw.enums.TransactionType.Renewal), "Renewal charges didn't make it to BC, Test can not continue");
    }

    @Test(dependsOnMethods = {"verifyIfRenewalChargesMadeItToBC"})
    public void cancelPolicyInPolicyCenter() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        date = DateUtils.dateAddSubtract(date, repository.gw.enums.DateAddSubtractOptions.Day,59);

        new Login(driver).loginAndSearchPolicy_asUW(generatePolicy);
        new StartCancellation(driver).cancelPolicy(repository.gw.enums.Cancellation.CancellationSourceReasonExplanation.NoReasonGiven, "Cancelling Policy",date, true);
        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"cancelPolicyInPolicyCenter"})
    public void acknowledgeCancellationChargesAndMakeTheInvoiceDue() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), generatePolicy.accountNumber);
        new BCAccountMenu(driver).clickBCMenuCharges();
        Assert.assertTrue(new BCCommonCharges(driver).waitUntilChargesFromPolicyContextArrive(repository.gw.enums.TransactionType.Cancellation), "Cancellation charges didn't make it to BC, Test can not continue");

        repository.gw.helpers.ClockUtils.setCurrentDates(driver, repository.gw.enums.DateAddSubtractOptions.Day, 20);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);

        new BCAccountMenu(driver).clickBCMenuDelinquencies();
        Assert.assertTrue(new BCCommonDelinquencies(driver).verifyDelinquencyByReason(repository.gw.enums.OpenClosed.Open, repository.gw.enums.DelinquencyReason.PastDueFullCancel,null,null));
        Assert.assertFalse(new BCCommonDelinquencies(driver).verifyDelinquencyEventCompletion(repository.gw.enums.CancellationEvent.CancellationBillingInstructionReceived),"This Delinquency Event Completion has been marked as complete with out getting cancels on this term");
    }

}

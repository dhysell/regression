package previousProgramIncrement.pi3_090518_111518.maintenanceDefects.NoExceptions2;


import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.payments.AccountPayments;
import repository.bc.common.BCCommonCharges;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.common.BCCommonDocuments;
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
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

import java.util.Date;


@Test(groups = {"ClockMove", "BillingCenter"})
public class DE6732_DE7931_MNOWPrintsOnCancelledPolicy extends  BaseTest{

    private GeneratePolicy generatePolicy;
    private WebDriver driver;
    private ARUsers arUser;

    @Test
    public void generatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        generatePolicy = new GeneratePolicyHelper(driver).generatePLSectionIAndIIWithAutoPLPolicy("DE6732_DE7931","MNOW",null, repository.gw.enums.PaymentPlanType.Monthly, repository.gw.enums.PaymentType.ACH_EFT);

    }

    @Test(dependsOnMethods = {"generatePolicy"})
    public void makePayment() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), generatePolicy.accountNumber);

        new BCAccountMenu(driver).clickAccountMenuPaymentsPayments();
        new AccountPayments(driver).waitUntilBindPaymentsArrive(240);

        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);
        new BCAccountMenu(driver).clickAccountMenuInvoices();
        Assert.assertTrue(new AccountInvoices(driver).verifyInvoice(null, null, null, repository.gw.enums.InvoiceType.NewBusinessDownPayment, null, repository.gw.enums.InvoiceStatus.Billed, null, 0.00), "Down payment done with ACH in PC has not yet made to BC or something went wrong ");

        repository.gw.helpers.ClockUtils.setCurrentDates(driver, repository.gw.enums.DateAddSubtractOptions.Day, 1);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);
        repository.gw.helpers.ClockUtils.setCurrentDates(driver, repository.gw.enums.DateAddSubtractOptions.Day, 9);
    }

    @Test(dependsOnMethods = {"makePayment"})
    public void cancelPolicyInPolicyCenter() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchPolicy_asUW(generatePolicy);
        new StartCancellation(driver).cancelPolicy(repository.gw.enums.Cancellation.CancellationSourceReasonExplanation.NoReasonGiven, "Cancelling Policy", repository.gw.helpers.DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver,ApplicationOrCenter.BillingCenter), repository.gw.enums.DateAddSubtractOptions.Day,30), true);
        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"cancelPolicyInPolicyCenter"})
    public void acknowledgeCancellationChargesAndMakeTheInvoiceDue() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), generatePolicy.accountNumber);
        BCAccountMenu bcAccountMenu = new BCAccountMenu(driver);
        bcAccountMenu.clickBCMenuCharges();
        Assert.assertTrue(new BCCommonCharges(driver).waitUntilChargesFromPolicyContextArrive(repository.gw.enums.TransactionType.Cancellation), "Cancellation charges didn't make it to BC, Test can not continue");

        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);

        bcAccountMenu.clickBCMenuDelinquencies();
        Assert.assertTrue(new BCCommonDelinquencies(driver).verifyDelinquencyByReason(repository.gw.enums.OpenClosed.Open, repository.gw.enums.DelinquencyReason.UnderwritingFullCancel,null,null));
        bcAccountMenu.clickAccountMenuInvoices();
        AccountInvoices accountInvoices = new AccountInvoices(driver);
        Date date = accountInvoices.getInvoiceDateByInvoiceType(repository.gw.enums.InvoiceType.Scheduled);
        String invoiceNumber = accountInvoices.getInvoiceNumber(null,null,null, repository.gw.enums.InvoiceType.Scheduled);
        repository.gw.helpers.ClockUtils.setCurrentDates(driver, date);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);

        bcAccountMenu.clickBCMenuSummary();
        bcAccountMenu.clickAccountMenuInvoices();
        Assert.assertTrue(accountInvoices.verifyInvoice(null,null, invoiceNumber, repository.gw.enums.InvoiceType.Scheduled,null, InvoiceStatus.Billed,null,null));
        bcAccountMenu.clickBCMenuDocuments();
        Assert.assertFalse(new BCCommonDocuments(driver).verifyDocument("Monthly Notice of Withdrawal", repository.gw.enums.DocumentType.Monthly_Notice_Of_Withdrawal, null, null, date,null), "MNOW document should not be created.");

    }
}
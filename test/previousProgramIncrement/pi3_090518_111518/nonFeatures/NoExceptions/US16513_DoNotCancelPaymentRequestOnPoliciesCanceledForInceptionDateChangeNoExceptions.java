package previousProgramIncrement.pi3_090518_111518.nonFeatures.NoExceptions;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.payments.AccountPayments;
import repository.bc.account.payments.AccountPaymentsPaymentRequests;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.OrganizationType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
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
public class US16513_DoNotCancelPaymentRequestOnPoliciesCanceledForInceptionDateChangeNoExceptions extends BaseTest {

    private WebDriver driver;
    private GeneratePolicy generatePolicy;
    private ARUsers arUser;
    private Date secondInvoiceDate ,secondDueDate;

    @Test
    public void generatePolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        generatePolicy = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PersonalAutoLinePL)
                .withCreateNew(repository.gw.enums.CreateNew.Create_New_Always)
                .withInsPersonOrCompany(repository.gw.enums.ContactSubType.Person)
                .withInsFirstLastName("US16513", "CxPol")
                .withPolOrgType(OrganizationType.Individual)
                .withPaymentPlanType(repository.gw.enums.PaymentPlanType.Monthly)
                .withDownPaymentType(repository.gw.enums.PaymentType.ACH_EFT)
                .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);
        driver.quit();
    }

    @Test(dependsOnMethods = {"generatePolicy"})
    public void verifyChargesInBC() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
        Config cf1 = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf1);
        new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.generatePolicy.accountNumber);

        BCAccountMenu menu = new BCAccountMenu(driver);
        menu.clickBCMenuCharges();

        AccountCharges charges = new AccountCharges(driver);
        charges.waitUntilIssuanceChargesArrive();

        menu.clickAccountMenuPaymentsPayments();
        new AccountPayments(driver).waitUntilBindPaymentsArrive(300);

        new BatchHelpers(cf1).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);
        ClockUtils.setCurrentDates(cf1, repository.gw.enums.DateAddSubtractOptions.Day, 1);
        new BatchHelpers(cf1).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);
        menu.clickAccountMenuInvoices();

        secondInvoiceDate = new AccountInvoices(driver).getListOfInvoiceDates().get(1);
        secondDueDate = new AccountInvoices(driver).getListOfDueDates().get(1);

        ClockUtils.setCurrentDates(cf1, secondInvoiceDate);
        new BatchHelpers(cf1).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);

        menu.clickBCMenuCharges();
        menu.clickAccountMenuInvoices();

        Assert.assertTrue(new AccountInvoices(driver).verifyInvoice(secondInvoiceDate, null, null, repository.gw.enums.InvoiceType.Scheduled, null, repository.gw.enums.InvoiceStatus.Billed, null, null), "Invoice haven't got billed");

        menu.clickAccountMenuPaymentsPaymentRequests();
        Assert.assertTrue(new AccountPaymentsPaymentRequests(driver).getPaymentRequestStatus(secondInvoiceDate, secondDueDate, null, null).equals(repository.gw.enums.Status.Created), "Payment request has not been created");
        driver.quit();
    }


    @Test(dependsOnMethods = {"verifyChargesInBC"})
    public void cancelPolicyInPC() throws Exception {
        driver = buildDriver(new Config(ApplicationOrCenter.PolicyCenter));

        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), generatePolicy.squire.getPolicyNumber());
        new StartCancellation(driver).cancelPolicy(repository.gw.enums.Cancellation.CancellationSourceReasonExplanation.OtherPolicyRewrittenOrReplaced, "Cancelling Policy", DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), true);
        driver.quit();
    }


    @Test(dependsOnMethods = {"cancelPolicyInPC"})
    public void testValidation() throws Exception {
        Config cf1 = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf1);
        new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.generatePolicy.accountNumber);
        BCAccountMenu menu = new BCAccountMenu(driver);

        menu.clickBCMenuCharges();
        new AccountCharges(driver).waitUntilChargesFromPolicyContextArrive(repository.gw.enums.TransactionType.Cancellation);
        ClockUtils.setCurrentDates(cf1, repository.gw.enums.DateAddSubtractOptions.Day, 2);
        new BatchHelpers(cf1).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);
        new BatchHelpers(cf1).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);

        menu.clickAccountMenuPaymentsPaymentRequests();
        Assert.assertFalse(new AccountPaymentsPaymentRequests(driver).getPaymentRequestStatus(secondInvoiceDate,secondDueDate,null,null).equals(repository.gw.enums.Status.Canceled),"Payment request has been cancelled, it should not ");

        menu.clickAccountMenuInvoices();
        ClockUtils.setCurrentDates(cf1,secondDueDate);
        new BatchHelpers(cf1).runBatchProcess(repository.gw.enums.BatchProcess.Payment_Request);
        new BatchHelpers(cf1).runBatchProcess(repository.gw.enums.BatchProcess.Payment_Request);
        new BatchHelpers(cf1).runBatchProcess(repository.gw.enums.BatchProcess.Payment_Request);

        menu.clickAccountMenuPaymentsPaymentRequests();
        Assert.assertTrue(new AccountPaymentsPaymentRequests(driver).getPaymentRequestStatus(secondInvoiceDate,secondDueDate,null,null).equals(repository.gw.enums.Status.Closed),"Payment request has been cancelled, it should not ");

        driver.quit();
    }

}

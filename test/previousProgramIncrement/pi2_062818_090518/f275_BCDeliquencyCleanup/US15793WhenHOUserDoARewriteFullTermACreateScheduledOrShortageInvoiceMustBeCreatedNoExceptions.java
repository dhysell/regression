package previousProgramIncrement.pi2_062818_090518.f275_BCDeliquencyCleanup;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Cancellation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.BillingCenterHelper;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

import java.util.Calendar;


/**
 * @Author sgunda
 * @Description US15793 - Create scheduled/shortage invoice when HO users are processing rewrite new and full term
 * @DATE August 3, 2018
 */

@Test(groups = {"ClockMove", "BillingCenter"})
public class US15793WhenHOUserDoARewriteFullTermACreateScheduledOrShortageInvoiceMustBeCreatedNoExceptions extends BaseTest {

    private WebDriver driver;
    private GeneratePolicy generatePolicy;
    private ARUsers arUser;
    private InvoiceType scheduledOrShortage;
    private PaymentPlanType paymentPlanType;

    @Test
    public void generatePolicy() throws Exception {

        if(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)/2==0){
            paymentPlanType = PaymentPlanType.Monthly;
            scheduledOrShortage = InvoiceType.Scheduled;
        }else{
            paymentPlanType = PaymentPlanType.Annual;
            scheduledOrShortage = InvoiceType.Shortage;
        }

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        generatePolicy = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("Rewrite", "FullT")
                .withPolOrgType(OrganizationType.Individual)
                .withPaymentPlanType(paymentPlanType)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        System.out.println("AccountNumber: " + generatePolicy.accountNumber);
    }

    @Test(dependsOnMethods = {"generatePolicy"})
    public void verifyChargesInBC() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.generatePolicy.accountNumber);

        new BillingCenterHelper(driver).verifyIssuancePolicyPeriod_TroubleTicketAndBindPayment(generatePolicy);

        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
        driver.quit();
    }

    @Test(dependsOnMethods = { "verifyChargesInBC" })
    public void cancelPolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), generatePolicy.squire.getPolicyNumber());
        new StartCancellation(driver).cancelPolicy(Cancellation.CancellationSourceReasonExplanation.Rewritten, "Cancelling Policy", null, true);
        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = { "cancelPolicy" })
    public void verifyCancellationChargesInBC() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.generatePolicy.accountNumber);

        BCAccountMenu menu = new BCAccountMenu(driver);
        menu.clickBCMenuCharges();

        AccountCharges charges = new AccountCharges(driver);
        charges.waitUntilChargesFromPolicyContextArrive(TransactionType.Cancellation);
        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
        driver.quit();
    }

    @Test(dependsOnMethods = { "verifyCancellationChargesInBC" })
    public void rewriteFullTerm() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), generatePolicy.squire.getPolicyNumber());
        new StartRewrite(driver).rewriteFullTerm(generatePolicy);
        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"rewriteFullTerm"})
    public void verifyReinstateChargesInBCAndVerifyIfDelinquencyClosedOrNot() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.generatePolicy.accountNumber);

        BCAccountMenu menu = new BCAccountMenu(driver);
        menu.clickBCMenuCharges();
        AccountCharges charges = new AccountCharges(driver);
        charges.waitUntilChargesFromPolicyContextArrive(TransactionType.Rewrite);

        menu.clickAccountMenuInvoices();
        AccountInvoices accountInvoices = new AccountInvoices(driver);
        Assert.assertFalse(accountInvoices.verifyInvoice(null,null,null,InvoiceType.RewriteDownPayment,null,null,null,null),"It did not create the type of invoice it should be creating");
        Assert.assertTrue(accountInvoices.verifyInvoice(null,null,null,scheduledOrShortage,null,InvoiceStatus.Planned,null,null),"It did not create the type of invoice it should be creating");
        driver.quit();
    }
}

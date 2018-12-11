package previousProgramIncrement.pi3_090518_111518.nonFeatures.NoExceptions;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.RewriteType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.policy.PolicyMenu;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

import java.util.List;


@Test(groups = {"ClockMove", "BillingCenter"})
public class US16500_PaymentPreviewOnRewriteNoExceptions extends BaseTest {

    private WebDriver driver;
    private GeneratePolicy generatePolicy;
    private ARUsers arUser;
    private List<Double> paymentPreviewList , invoiceAmountList;



    private List<Double> rewriteNewTerm() throws Exception {
        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickRewriteNewTerm();

        StartRewrite startRewrite = new StartRewrite(driver);
        startRewrite.clickNext();
        startRewrite.clickThroughAllPagesUntilRiskAnalysis(generatePolicy, RewriteType.NewTerm);
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.clickPriorPolicyCardTab();
        risk.clickClaimsCardTab();
        risk.clickPriorLossesCardTab();
        risk.Quote();
        startRewrite.clickPolicyChangeNext();
        startRewrite.clickPolicyChangeNext();

        // Getting amount list from Payment preview
            new SideMenuPC(driver).clickSideMenuPayment();
            repository.pc.workorders.generic.GenericWorkorderPayment getLinkPreviewLink = new repository.pc.workorders.generic.GenericWorkorderPayment(driver);
            getLinkPreviewLink.clickPreviewPayment();
            List<Double> invoiceAmounts = getLinkPreviewLink.getListOfInvoiceAmounts();
            getLinkPreviewLink.clickReturnToPayment();
            startRewrite.clickIssuePolicy();

        return invoiceAmounts;
    }


    @Test
    public void generatePolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        generatePolicy = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PersonalAutoLinePL)
                .withCreateNew(repository.gw.enums.CreateNew.Create_New_Always)
                .withInsPersonOrCompany(repository.gw.enums.ContactSubType.Person)
                .withInsFirstLastName("RW Payment", "Preview")
                .withPolOrgType(OrganizationType.Individual)
                .withPaymentPlanType(repository.gw.enums.PaymentPlanType.Monthly)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"generatePolicy"})
    public void verifyChargesInBC() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.generatePolicy.accountNumber);

        BCAccountMenu menu = new BCAccountMenu(driver);
        menu.clickBCMenuCharges();
        new AccountCharges(driver).waitUntilIssuanceChargesArrive();

        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);
        ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 10);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Release_Trouble_Ticket_Holds);
        driver.quit();
    }

    @Test(dependsOnMethods = { "verifyChargesInBC" })
    public void cancelPolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), generatePolicy.squire.getPolicyNumber());
        new StartCancellation(driver).cancelPolicy(repository.gw.enums.Cancellation.CancellationSourceReasonExplanation.NoReasonGiven, "Cancelling Policy ", DateUtils.getCenterDate(driver,ApplicationOrCenter.BillingCenter), true);
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
        charges.waitUntilChargesFromPolicyContextArrive(repository.gw.enums.TransactionType.Cancellation);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);
        ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 1);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);

        menu.clickBCMenuDelinquencies();
        double delinquentAmount = new BCCommonDelinquencies(driver).getDelinquentAmount(repository.gw.enums.OpenClosed.Open , null , null, null);

        new BCAccountMenu(driver).clickAccountMenuActionsNewDirectBillPayment();
        new NewDirectBillPayment(driver).makeDirectBillPaymentExecute(delinquentAmount,generatePolicy.squire.getPolicyNumber());

        ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 2);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);
        driver.quit();
    }

    @Test(dependsOnMethods = { "verifyCancellationChargesInBC" })
    public void rewriteNewTermOnPolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), generatePolicy.squire.getPolicyNumber());
        paymentPreviewList = rewriteNewTerm();
        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"rewriteNewTermOnPolicy"})
    public void verifyRewritePreviewPaymentsAndInvoices() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.generatePolicy.accountNumber);

        BCAccountMenu menu = new BCAccountMenu(driver);
        menu.clickBCMenuCharges();
        AccountCharges charges = new AccountCharges(driver);
        charges.waitUntilChargesFromPolicyContextArrive(repository.gw.enums.TransactionType.Rewrite);

        menu.clickAccountMenuInvoices();
        AccountInvoices accountInvoices = new AccountInvoices(driver);
        accountInvoices.selectPolicyPeriod(generatePolicy.squire.getPolicyNumber()+"-2");
        invoiceAmountList = accountInvoices.getListOfInvoiceDueAmounts();
        if(!invoiceAmountList.equals(paymentPreviewList)) {
            System.out.println("Amounts from Invoice page in BC are : "+ invoiceAmountList);
            System.out.println("Amounts from Payment preview page in PC are " + paymentPreviewList);
            Assert.fail("Preview amount and invoice amount does not match , Please investigate with above printed amount ");
        }
        driver.quit();
    }
}

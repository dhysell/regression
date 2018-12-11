package previousProgramIncrement.pi2_062818_090518.f275_BCDeliquencyCleanup;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Cancellation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.BillingCenterHelper;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.account.AccountSummaryPC;
import repository.pc.workorders.cancellation.StartCancellation;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;


/**
 * @Author sgunda
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558469364d/detail/userstory/238168103228"><US15790 - Payment message from BC should trigger an activity in PC/a>
 * @Description US15790 - Payment message from BC should trigger an activity in PC
 * @DATE August 3, 2018
 */


@Test(groups = {"ClockMove", "BillingCenter"})
public class US15790PaymentMessageFromBCShouldTriggerAnActivityInPCNoExceptions extends BaseTest {

    private WebDriver driver;
    private GeneratePolicy generatePolicy;
    private ARUsers arUser;

    @Test
    public void generatePolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        generatePolicy = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("PayActivity", "ForCxPolicy")
                .withPolOrgType(OrganizationType.Individual)
                .withPaymentPlanType(PaymentPlanType.Annual)
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
        repository.gw.helpers.ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
        repository.gw.helpers.ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 9);
        driver.quit();
    }

    @Test(dependsOnMethods = { "verifyChargesInBC" })
    public void cancelPolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), generatePolicy.squire.getPolicyNumber());
        new StartCancellation(driver).cancelPolicy(Cancellation.CancellationSourceReasonExplanation.NoReasonGiven, "Cancelling Policy", DateUtils.getCenterDate(driver,ApplicationOrCenter.BillingCenter), true);
        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"cancelPolicy"})
    public void verifyCancellationChargesInBC() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.generatePolicy.accountNumber);

        BCAccountMenu menu = new BCAccountMenu(driver);
        menu.clickBCMenuCharges();

        AccountCharges charges = new AccountCharges(driver);
        charges.waitUntilChargesFromPolicyContextArrive(TransactionType.Cancellation);

        repository.gw.helpers.ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        menu.clickBCMenuDelinquencies();
        Assert.assertTrue(new BCCommonDelinquencies(driver).verifyDelinquencyStatus(OpenClosed.Open, generatePolicy.accountNumber, generatePolicy.squire.getPolicyNumber(),null),"Delinquency should have been triggered here ");
        menu.clickAccountMenuActionsNewDirectBillPayment();
        new NewDirectBillPayment(driver).makeDirectBillPaymentExecute(10,generatePolicy.squire.getPolicyNumber());
        driver.quit();
    }

    @Test(dependsOnMethods = { "verifyCancellationChargesInBC" })
    public void reinstatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        //new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), generatePolicy.squire.getPolicyNumber());
        new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.generatePolicy.accountNumber);
        Assert.assertTrue(new AccountSummaryPC(driver).verifyCurrentActivity("Payment within 30 days of cancel",100),"Activity did not trigger after making payment in BillingCenter");
        driver.quit();
    }
}
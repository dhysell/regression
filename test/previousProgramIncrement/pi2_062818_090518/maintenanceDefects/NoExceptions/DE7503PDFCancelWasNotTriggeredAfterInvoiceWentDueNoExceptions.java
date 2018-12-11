package previousProgramIncrement.pi2_062818_090518.maintenanceDefects.NoExceptions;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.common.BCCommonDelinquencies;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentReturnedPaymentReason;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

import java.util.Date;


@Test(groups = {"ClockMove"})
public class DE7503PDFCancelWasNotTriggeredAfterInvoiceWentDueNoExceptions extends BaseTest {

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
                .withInsFirstLastName("DE7503", "NoEx")
                .withPolOrgType(OrganizationType.Individual)
                .withPaymentPlanType(PaymentPlanType.Monthly)
                .withDownPaymentType(PaymentType.ACH_EFT)
                .build(GeneratePolicyType.PolicyIssued);

        System.out.println("AccountNumber: " + generatePolicy.accountNumber);
    }

    @Test(dependsOnMethods = {"generatePolicy"})
    public void verifyChargesPaymentsInBCAndTestDelinquencyTriggers() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.generatePolicy.accountNumber);

        BCAccountMenu menu = new BCAccountMenu(driver);
        menu.clickBCMenuCharges();
        AccountCharges charges = new AccountCharges(driver);
        charges.waitUntilIssuanceChargesArrive();

        menu.clickAccountMenuPaymentsPayments();
        AccountPayments accountPayments = new AccountPayments(driver);
        accountPayments.waitUntilBindPaymentsArrive(240);

            new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
            ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
            new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
            ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 4);

        menu.clickBCMenuCharges();
        menu.clickAccountMenuPaymentsPayments();
        AccountPayments payment = new AccountPayments(driver);
        payment.reversePaymentAtFault(null,generatePolicy.squire.getPremium().getDownPaymentAmount(), null, null, PaymentReturnedPaymentReason.InsufficientFunds);

            Date reversalDate = DateUtils.getCenterDate(driver,ApplicationOrCenter.BillingCenter);
            menu.clickBCMenuDelinquencies();
            BCCommonDelinquencies bcCommonDelinquencies = new BCCommonDelinquencies(driver);
            Assert.assertTrue(bcCommonDelinquencies.verifyDelinquencyByReason(OpenClosed.Open,DelinquencyReason.PastDueFullCancel,null,reversalDate));

        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 3);

        menu.clickAccountMenuActionsNewDirectBillPayment();
        double secondReversalAmount = generatePolicy.squire.getPremium().getDownPaymentAmount() + 0.10;
        new NewDirectBillPayment(driver).makeDirectBillPaymentExecute(secondReversalAmount,generatePolicy.squire.getPolicyNumber());
        menu.clickBCMenuCharges();
        menu.clickBCMenuDelinquencies();
        Assert.assertTrue(bcCommonDelinquencies.verifyDelinquencyByReason(OpenClosed.Closed,DelinquencyReason.PastDueFullCancel,null,reversalDate));

            menu.clickAccountMenuPaymentsPayments();
            payment.reversePaymentAtFault(null,secondReversalAmount, null, null, PaymentReturnedPaymentReason.InsufficientFunds);
            reversalDate = DateUtils.getCenterDate(driver,ApplicationOrCenter.BillingCenter);
            menu.clickBCMenuCharges();
            menu.clickBCMenuDelinquencies();
            Assert.assertTrue(bcCommonDelinquencies.verifyDelinquencyByReason(OpenClosed.Open,DelinquencyReason.PastDueFullCancel,null,reversalDate));
            ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 5);

        menu.clickAccountMenuActionsNewDirectBillPayment();
        new NewDirectBillPayment(driver).makeDirectBillPaymentExecute(generatePolicy.squire.getPremium().getDownPaymentAmount(),generatePolicy.squire.getPolicyNumber());
        menu.clickBCMenuCharges();
        menu.clickBCMenuDelinquencies();
        Assert.assertTrue(bcCommonDelinquencies.verifyDelinquencyByReason(OpenClosed.Closed,DelinquencyReason.PastDueFullCancel,null,reversalDate));

        menu.clickAccountMenuInvoices();
        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 6);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 15);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
        reversalDate = DateUtils.getCenterDate(driver,ApplicationOrCenter.BillingCenter);
        menu.clickBCMenuCharges();

        menu.clickBCMenuDelinquencies();
        Assert.assertTrue(bcCommonDelinquencies.verifyDelinquencyByReason(OpenClosed.Open,DelinquencyReason.PastDueFullCancel,null,reversalDate));
        driver.quit();
    }
}

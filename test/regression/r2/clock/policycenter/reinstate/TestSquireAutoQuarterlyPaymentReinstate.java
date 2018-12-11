package regression.r2.clock.policycenter.reinstate;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.policy.BCPolicyMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DateDifferenceOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentReturnedPaymentReason;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.TransactionType;
import repository.gw.enums.Vehicle.Comprehensive_CollisionDeductible;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.reinstate.StartReinstate;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement :DE5219:Validations on reinstate - need to make sure the information is saved, not removing the validations.
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/33274298124d/detail/defect/102354203720">Link Text</a>
 * @Description
 * @DATE May 1, 2017
 */
@QuarantineClass
public class TestSquireAutoQuarterlyPaymentReinstate extends BaseTest {
	private WebDriver driver;
    private GeneratePolicy mySqAutoOnlyPol;
    private ARUsers arUser;
    private Underwriters uw;

    @Test
    public void testIssueSquireAutoOnlyPol() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh,
                MedicalLimit.TenK, true, UninsuredLimit.Fifty, false, UnderinsuredLimit.Fifty);

        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle toAdd = new Vehicle();
        toAdd.setComprehensiveDeductible(Comprehensive_CollisionDeductible.FiveHundred500);
        toAdd.setCollisionDeductible(Comprehensive_CollisionDeductible.OneThousand1000);
        vehicleList.add(toAdd);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);
        squirePersonalAuto.setVehicleList(vehicleList);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        mySqAutoOnlyPol = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withInsFirstLastName("Reinstate", "Auto")
                .withPaymentPlanType(PaymentPlanType.Quarterly)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testIssueSquireAutoOnlyPol"})
    private void testRunInvoiceWithMakingDownpayment() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.mySqAutoOnlyPol.squire.getPolicyNumber());

        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
        policyMenu.clickTopInfoBarAccountNumber();
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuInvoices();
        AccountInvoices invoice = new AccountInvoices(driver);
        if (invoice.getInvoiceStatus().equals(InvoiceStatus.Planned)) {
            new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        }
        acctMenu.clickBCMenuTroubleTickets();
        acctMenu.clickAccountMenuInvoices();
        if (invoice.getInvoiceStatus().equals(InvoiceStatus.Billed)) {
            acctMenu = new BCAccountMenu(driver);
            acctMenu.clickAccountMenuActionsNewDirectBillPayment();
            NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
            directPayment.makeDirectBillPaymentExecute(mySqAutoOnlyPol.squire.getPremium().getDownPaymentAmount(), mySqAutoOnlyPol.accountNumber);
        } else
            Assert.fail("running the invoice batch process  failed!!!!");
    }


    @Test(dependsOnMethods = {"testRunInvoiceWithMakingDownpayment"})
    private void testMoveClockAndRunWorkflow() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
        new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(),
                this.arUser.getPassword(), this.mySqAutoOnlyPol.accountNumber);

        // Move clock 5 days
        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 5);

        BCAccountMenu acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuPayments();
        AccountPayments payment = new AccountPayments(driver);
        payment.reversePaymentAtFault(null,mySqAutoOnlyPol.squire.getPremium().getDownPaymentAmount(), null, null, PaymentReturnedPaymentReason.InsufficientFunds);

        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        new BatchHelpers(cf).runBatchProcess(BatchProcess.BC_Workflow);
        // delay required

        new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);
        // delay required
        new GuidewireHelpers(driver).logout();

    }

    @Test(dependsOnMethods = {"testMoveClockAndRunWorkflow"})
    private void testPCPendingCancellationAndMoveClock() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySqAutoOnlyPol.squire.getPolicyNumber());

        PolicySummary pcSummary = new PolicySummary(driver);
        List<WebElement> pendingTrans = pcSummary.getPendingPolicyTransaction(TransactionType.Cancellation);
        if (pendingTrans.size() > 0) {
            String transactionEffDate = pcSummary.getPendingPolicyTransactionByColumnName(TransactionType.Cancellation.getValue(), "Trans Eff Date");
            int noOfDays = DateUtils.getDifferenceBetweenDates(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateUtils.convertStringtoDate(transactionEffDate, "MM/dd/yyyy"), DateDifferenceOptions.Day);
            ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, (noOfDays + 3));
            new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);
        } else {
            Assert.fail("Pending Cancellation is not shown in PC!!!!!!!");
        }
    }


    @Test(dependsOnMethods = {"testPCPendingCancellationAndMoveClock"})
    public void testReinstatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySqAutoOnlyPol.accountNumber);
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
        policySearchPC.searchPolicyByPolicyNumber(mySqAutoOnlyPol.squire.getPolicyNumber());

        PolicySummary summary = new PolicySummary(driver);
        if (summary.getTransactionNumber(TransactionType.Reinstatement, "Testing purpose") == null) {
            errorMessage = errorMessage + "Reinstatement is not done for current term !!!\n";
        }

        new GuidewireHelpers(driver).logout();

        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }
    }
}

package previousProgramIncrement.pi3_090518_111518.maintenanceDefects.NoExceptions;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.common.BCCommonCharges;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.search.BCSearchAccounts;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.PaymentReturnedPaymentReason;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.*;
import repository.gw.helpers.DateUtils;
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

import java.util.ArrayList;
import java.util.Date;


@Test(groups = {"ClockMove", "BillingCenter"})
public class DE7939_PaymentReversalShouldTriggerRightDelinquencyWhenWeGetCancelledRenewalNoExceptions extends BaseTest {

    private GeneratePolicy generatePolicy;
    private WebDriver driver;

    private String lienHolderNumber = null;
    private String lienholderLoanNumber = null;
    private double lienHolderLoanPremiumAmount;
    private ARUsers arUser = new ARUsers();


    @Test
    public void testGenerateFullyLienBilledPLPolicy() throws  Exception {

        ArrayList<GenerateContact> generateContacts= new ArrayList<>();
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);

        ArrayList<repository.gw.enums.ContactRole> lienOneRolesToAdd = new ArrayList<repository.gw.enums.ContactRole>();
        lienOneRolesToAdd.add(repository.gw.enums.ContactRole.Lienholder);

        GenerateContact lienOne = new GenerateContact.Builder(driver)
                .withCompanyName("LH From Hell")
                .withRoles(lienOneRolesToAdd)
                .withGeneratedLienNumber(true)
                .withUniqueName(true)
                .build(GenerateContactType.Company);

        generateContacts.add(lienOne);

        driver.get(cf.getUrlOfCenter(ApplicationOrCenter.PolicyCenter));
        generatePolicy = new GeneratePolicyHelper(driver).generateInsuredAndLienBilledSquirePolicy("DE7939" ,"CxPol",null, repository.gw.enums.PaymentPlanType.Annual, repository.gw.enums.PaymentType.Cash,generateContacts);

        this.lienHolderNumber = this.generatePolicy.squire.propertyAndLiability.locationList.get(1).getPropertyList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
        this.lienholderLoanNumber = this.generatePolicy.squire.propertyAndLiability.locationList.get(1).getPropertyList().get(0).getAdditionalInterestList().get(0).getLoanContractNumber();
        this.lienHolderLoanPremiumAmount = this.generatePolicy.squire.propertyAndLiability.locationList.get(1).getPropertyList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();
    }


    @Test(dependsOnMethods = { "testGenerateFullyLienBilledPLPolicy" })
    public void makeInsuredDownPayment() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.generatePolicy.accountNumber);
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(generatePolicy, generatePolicy.squire.getPremium().getDownPaymentAmount(), generatePolicy.squire.getPolicyNumber());
        repository.gw.helpers.ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 1);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);
        repository.gw.helpers.ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 20);

        new BCSearchAccounts(driver).searchAccountByAccountNumber(this.lienHolderNumber);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);

        new BCAccountMenu(driver).clickAccountMenuActionsNewDirectBillPayment();
        newPayment.makeDirectBillPaymentExecute(generatePolicy.squire.getPolicyNumber(), this.lienholderLoanNumber, this.lienHolderLoanPremiumAmount);

        repository.gw.helpers.ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 15);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);

        new GuidewireHelpers(driver).logout();
    }


    @Test(dependsOnMethods = {"makeInsuredDownPayment"})
    public void issueRenewal() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        repository.gw.helpers.ClockUtils.setCurrentDates(cf, repository.gw.helpers.DateUtils.dateAddSubtract(generatePolicy.squire.getExpirationDate(), repository.gw.enums.DateAddSubtractOptions.Day,-47));

        new StartRenewal(driver).loginAsUWAndIssueRenewal(generatePolicy);

    }

    @Test(dependsOnMethods = { "issueRenewal" })
    public void verifyRenewal() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.generatePolicy.accountNumber);

        new BCAccountMenu(driver).clickBCMenuCharges();
        BCCommonCharges charges = new BCCommonCharges(driver);
        Assert.assertTrue(charges.waitUntilChargesFromPolicyContextArrive(repository.gw.enums.TransactionType.Renewal), "Renewal charges didn't make it to BC, Test can not continue");
        new GuidewireHelpers(driver).logout();
    }


    @Test(dependsOnMethods = {"verifyRenewal"})
    public void cancelRenewalInPolicyCenter() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchPolicy_asUW(generatePolicy);
        new StartCancellation(driver).cancelPolicy(repository.gw.enums.Cancellation.CancellationSourceReasonExplanation.PolicyNotTaken, "Cancelling Policy",generatePolicy.squire.getExpirationDate(), true);
        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = { "cancelRenewalInPolicyCenter" })
    public void waitForCancellationAndReversePayments() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), generatePolicy.accountNumber);

        new BillingCenterHelper(driver).waitUntilCancellationChargesArrive();

        repository.gw.helpers.ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 10);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);

        Date reversalDate = DateUtils.getCenterDate(driver,ApplicationOrCenter.BillingCenter);
        new BCAccountMenu(driver).clickAccountMenuPaymentsPayments();
        new AccountPayments(driver).reversePaymentAtFault(null,generatePolicy.squire.getPremium().getDownPaymentAmount(), null, null, PaymentReturnedPaymentReason.InsufficientFunds);

        new BCAccountMenu(driver).clickBCMenuDelinquencies();
        BCCommonDelinquencies bcCommonDelinquencies = new BCCommonDelinquencies(driver);
        Assert.assertTrue(bcCommonDelinquencies.verifyDelinquencyByReason(repository.gw.enums.OpenClosed.Open, repository.gw.enums.DelinquencyReason.PastDuePartialCancel,null,reversalDate));
        new GuidewireHelpers(driver).logout();
    }

}

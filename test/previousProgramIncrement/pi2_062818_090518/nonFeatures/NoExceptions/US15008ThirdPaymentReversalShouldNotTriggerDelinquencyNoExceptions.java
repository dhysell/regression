package previousProgramIncrement.pi2_062818_090518.nonFeatures.NoExceptions;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.common.BCCommonDelinquencies;
import com.idfbins.driver.BaseTest;
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
import repository.gw.enums.Property;
import repository.gw.enums.TransactionStatus;
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
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.policy.PolicyDocuments;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * @Author sgunda
 * @UserStory US15008 - Third payment reversal on a invoice stream should not trigger non-pay delinquency, when the Cash Only Roll up goes due, it should trigger NSF non-pay delinquency
 * @Defect DE7697 - On Third Reversal, the Billed Renewal Down changes to Carry Forward
 * @DATE Jul 21, 2018
 */
@Test(groups = {"ClockMove", "BillingCenter"})
public class US15008ThirdPaymentReversalShouldNotTriggerDelinquencyNoExceptions extends BaseTest {

    private ARUsers arUser;
    private GeneratePolicy myPolicyObj;
    private WebDriver driver;
    private BCAccountMenu bcAccountMenu;
    private AccountInvoices accountInvoices;
    private NewDirectBillPayment newDirectBillPayment;
    private AccountPayments accountPayments;
    private BCCommonDelinquencies bcCommonDelinquencies;
    private Date cashOnlyInvoicePastDueDate;

    @Test
    public void generatePolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        PLPolicyLocationProperty locationOneProperty = new PLPolicyLocationProperty(Property.PropertyTypePL.ResidencePremises);
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
                .withInsFirstLastName("NotAJohnDoe","US15008" )
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withPaymentPlanType(PaymentPlanType.Monthly)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        System.out.println("AccountNumber: " + myPolicyObj.accountNumber);
        driver.quit();
    }

    @Test(dependsOnMethods = {"generatePolicy"})
    public void makePaymentsAndReverseItToMakePolicyCashOnly() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);

        arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager,ARCompany.Personal);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        bcAccountMenu = new BCAccountMenu(driver);
        newDirectBillPayment = new NewDirectBillPayment(driver);
        newDirectBillPayment.makeInsuredDownpayment(myPolicyObj,myPolicyObj.squire.getPremium().getDownPaymentAmount(), myPolicyObj.squire.getPolicyNumber());
        bcAccountMenu.clickAccountMenuActionsNewDirectBillPayment();
        newDirectBillPayment.makeDirectBillPaymentExecuteWithoutDistribution(20.0,myPolicyObj.squire.getPolicyNumber());

        bcAccountMenu.clickAccountMenuActionsNewDirectBillPayment();
        newDirectBillPayment.makeDirectBillPaymentExecuteWithoutDistribution(30.0,myPolicyObj.squire.getPolicyNumber());

        bcAccountMenu.clickAccountMenuInvoices();
        accountInvoices = new AccountInvoices(driver);
        Assert.assertTrue(accountInvoices.getInvoiceTableRowCount()>=12,"This should have 12 invoices in total");

        ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        bcAccountMenu.clickAccountMenuPayments();
        bcAccountMenu.clickAccountMenuPaymentsPayments();
        accountPayments = new AccountPayments(driver);
        Date revDate = DateUtils.getCenterDate(driver,ApplicationOrCenter.BillingCenter);
        accountPayments.reversePaymentAtFault(null,20.0,null,null,PaymentReturnedPaymentReason.InsufficientFunds);
        accountPayments.reversePaymentAtFault(null,30.0,null,null,PaymentReturnedPaymentReason.InsufficientFunds);

        bcAccountMenu.clickBCMenuDelinquencies();
        bcCommonDelinquencies = new BCCommonDelinquencies(driver);
        Assert.assertFalse(bcCommonDelinquencies.verifyDelinquencyExists(null,null,myPolicyObj.accountNumber,null,myPolicyObj.squire.getPolicyNumber(),revDate,null,null));

        ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);
        revDate = DateUtils.getCenterDate(driver,ApplicationOrCenter.BillingCenter);

        bcAccountMenu.clickAccountMenuPaymentsPayments();
        accountPayments.reversePaymentAtFault(null,myPolicyObj.squire.getPremium().getDownPaymentAmount(),null,null,PaymentReturnedPaymentReason.InsufficientFunds);

        bcAccountMenu.clickBCMenuDelinquencies();
        Assert.assertFalse(bcCommonDelinquencies.verifyDelinquencyExists(null,null,myPolicyObj.accountNumber,null,myPolicyObj.squire.getPolicyNumber(),revDate,null,null));

        bcAccountMenu.clickAccountMenuInvoices();
        Assert.assertTrue(accountInvoices.getInvoiceTableRowCount()<=12,"All the planned invoice should rollup into cash only");
        double expectedInvoiceAmount = myPolicyObj.squire.getPremium().getTotalCostToInsured()+(2*20)-(myPolicyObj.squire.getPremium().getDownPaymentAmount());
        Assert.assertTrue(accountInvoices.verifyInvoice(InvoiceType.CashonlyRollup, InvoiceStatus.Planned, expectedInvoiceAmount),"Rollup of charges did not take place properly, please investigate ");

        Date cashOnlyPlannedDate = accountInvoices.getInvoiceDateByInvoiceType(InvoiceType.CashonlyRollup);
        Date cashOnlyDueDate = accountInvoices.getInvoiceDueDateByInvoiceType(InvoiceType.CashonlyRollup);
        cashOnlyInvoicePastDueDate = DateUtils.dateAddSubtract(cashOnlyDueDate,DateAddSubtractOptions.Day,1);

        Assert.assertEquals(cashOnlyPlannedDate,cashOnlyDueDate,"Cash only rollup invoice bill and due date should be same ");

        bcAccountMenu.clickAccountMenuActionsNewDirectBillPayment();
        newDirectBillPayment.makeDirectBillPaymentExecute(myPolicyObj.squire.getPremium().getDownPaymentAmount(),myPolicyObj.squire.getPolicyNumber());

        ClockUtils.setCurrentDates(driver,cashOnlyPlannedDate);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        ClockUtils.setCurrentDates(driver,cashOnlyInvoicePastDueDate);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        bcAccountMenu.clickBCMenuDelinquencies();
        Assert.assertTrue(bcCommonDelinquencies.verifyDelinquencyExists(OpenClosed.Open,DelinquencyReason.PastDueFullCancel,myPolicyObj.accountNumber,null,myPolicyObj.squire.getPolicyNumber(),cashOnlyInvoicePastDueDate,null,expectedInvoiceAmount));

    }

    @Test(dependsOnMethods = {"makePaymentsAndReverseItToMakePolicyCashOnly"})
    public void checkInPolicyCenterIfItTriggerRightCancellationOrNot() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(),myPolicyObj.squire.getPolicyNumber());
        PolicySummary policySummary = new PolicySummary(driver);
        Assert.assertTrue(policySummary.verifyPendingTransactions(null,null,TransactionStatus.Canceling,TransactionType.Cancellation,null,240),"Cancellation did not trigger in PC after even after policy went delinquent ");
        new SideMenuPC(driver).clickSideMenuToolsDocuments();
        PolicyDocuments policyDocuments = new PolicyDocuments(driver);
        policyDocuments.sortByLatestGeneratedDocument();
        policyDocuments.verifyDocumentByNameDescriptionDateGeneratedAndAuthor("IWBC1000061112","NSF Cancel Notice",cashOnlyInvoicePastDueDate,ApplicationOrCenter.BillingCenter.getValue());
    }

}
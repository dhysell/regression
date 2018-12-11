package regression.r2.clock.billingcenter.documents;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.common.BCCommonDocuments;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DocumentType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentReturnedPaymentReason;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author sgunda
 * @Requirement **HOT FIX** Monthly Notice of Withdrawal not printing when invoice is carried forward.
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/7832667974d/detail/defect/195973726680">Rally Defect DE7148</a>
 * @DATE Feb 22, 2018
 */
public class TestMonthlyNoticeOfWithdrawalOnDelinqencies extends BaseTest {
	private WebDriver driver;
    private GeneratePolicy myPolicyObj = null;
    private ARUsers arUser = new ARUsers();
    private String thirdInvoiceNumber, forthInvoiceNumber;
    private AccountInvoices acctInvoice;
    private BCAccountMenu accountMenu;
    private List<Date> dueDatesList, invoiceDatesList;
    private double delAmount, sumOfInvoiceAmount;
    private Date date;

    @Test
    public void generatePolicy() throws Exception {

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("Insured Policy")
                .withPolOrgType(OrganizationType.Partnership)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Monthly)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
        System.out.println(myPolicyObj.busOwnLine.getPolicyNumber());
    }

    @Test(dependsOnMethods = {"generatePolicy"})
    public void payDownpaymentAndMoveClockAndPaySecondInvoiceWithPaymentRequest() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), myPolicyObj.busOwnLine.getPolicyNumber());

        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuInvoices();

        acctInvoice = new AccountInvoices(driver);
        invoiceDatesList = acctInvoice.getListOfInvoiceDates();
        dueDatesList = acctInvoice.getListOfDueDates();
        thirdInvoiceNumber = acctInvoice.getInvoiceNumber(invoiceDatesList.get(2), dueDatesList.get(2), null,null);
        forthInvoiceNumber = acctInvoice.getInvoiceNumber(invoiceDatesList.get(3), dueDatesList.get(3), null,null);

        ClockUtils.setCurrentDates(cf, invoiceDatesList.get(1));
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        accountMenu.clickBCMenuDocuments();

        {//Verify if MNOW is created before the delinqency
            BCCommonDocuments documents = new BCCommonDocuments(driver);
            Assert.assertTrue(documents.verifyDocument("Monthly Notice of Withdrawal", DocumentType.Monthly_Notice_Of_Withdrawal, null, null, invoiceDatesList.get(1), null), "MNOW not created ");
        }

        ClockUtils.setCurrentDates(cf, dueDatesList.get(1));

        { //This helps paying invoice with Payment Request
            new BatchHelpers(cf).runBatchProcess(BatchProcess.Payment_Request);
            new BatchHelpers(cf).runBatchProcess(BatchProcess.Payment_Request);
            new BatchHelpers(cf).runBatchProcess(BatchProcess.Payment_Request);
        }

        accountMenu.clickBCMenuSummary();
        accountMenu.clickAccountMenuInvoices();
        sumOfInvoiceAmount = acctInvoice.getInvoiceAmountByInvoiceDate(invoiceDatesList.get(2)) + acctInvoice.getInvoiceAmountByInvoiceDate(invoiceDatesList.get(3));

        Assert.assertTrue(acctInvoice.verifyInvoice(invoiceDatesList.get(1), dueDatesList.get(1), null, InvoiceType.Scheduled, null, InvoiceStatus.Billed, null, 0.00), "Failed to pay with payment request");

        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"payDownpaymentAndMoveClockAndPaySecondInvoiceWithPaymentRequest"})
    public void reversePaymentAndTriggerDelinqency() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);
       new Login(driver). loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);

        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 5);

        accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuPaymentsPayments();

        AccountPayments payment = new AccountPayments(driver);
        payment.reversePaymentAtFault(null,null, null, null, PaymentReturnedPaymentReason.AccountFrozen);
        accountMenu.clickBCMenuDelinquencies();

        BCCommonDelinquencies acctDelinquency = new BCCommonDelinquencies(driver);
        Assert.assertTrue(acctDelinquency.verifyDelinquencyStatus(OpenClosed.Open, myPolicyObj.accountNumber, myPolicyObj.busOwnLine.getPolicyNumber(), date = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter)), "Delinquency does not exist Which is not Expected");
        delAmount = acctDelinquency.getDelinquentAmount(OpenClosed.Open, myPolicyObj.accountNumber, myPolicyObj.busOwnLine.getPolicyNumber(), date);
        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"reversePaymentAndTriggerDelinqency"})
    public void tryToBillThirdInvoiceOnOpenDelinquency() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

        ClockUtils.setCurrentDates(cf, invoiceDatesList.get(2));
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuInvoices();
        acctInvoice = new AccountInvoices(driver);
        try {
            Assert.assertFalse(acctInvoice.verifyInvoice(invoiceDatesList.get(2), dueDatesList.get(2), thirdInvoiceNumber, null, null, null, null, null), "This invoice should be Carried Forward and vanished, Checking If the invoice status has been changed to Carried Forward");
        } catch (Exception e) {
            Assert.assertTrue(acctInvoice.verifyInvoice(invoiceDatesList.get(2), dueDatesList.get(2), thirdInvoiceNumber, null, null, InvoiceStatus.Carried_Forward, null, null), "This test cannot continue, since this invoice failed to Carried Forward the charges on this invoice");
            System.out.println("Invoice Carry Forward successful, let's go ahead and close deliquency by making payment");
        }

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeDirectBillPaymentExecute(delAmount, myPolicyObj.busOwnLine.getPolicyNumber());
        accountMenu.clickBCMenuDelinquencies();

        BCCommonDelinquencies acctDelinquency = new BCCommonDelinquencies(driver);
        Assert.assertTrue(acctDelinquency.verifyDelinquencyStatus(OpenClosed.Closed, myPolicyObj.accountNumber, myPolicyObj.busOwnLine.getPolicyNumber(), date), "Delinquency did not close after making payment, Test can not continue");

    }

    @Test(dependsOnMethods = {"tryToBillThirdInvoiceOnOpenDelinquency"})
    public void checkIfMNOWIsGenrateWhenInvoiceWithCarriedForwardChargesGoesBilled() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

        ClockUtils.setCurrentDates(cf, invoiceDatesList.get(3));
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuInvoices();
        acctInvoice = new AccountInvoices(driver);
        Assert.assertTrue(acctInvoice.verifyInvoice(invoiceDatesList.get(3), invoiceDatesList.get(3), forthInvoiceNumber, InvoiceType.Scheduled, null, InvoiceStatus.Billed, sumOfInvoiceAmount, null), "The invoice did not go billed or something failed");

        accountMenu.clickBCMenuDocuments();
        BCCommonDocuments documents = new BCCommonDocuments(driver);
        Assert.assertTrue(documents.verifyDocument("Monthly Notice of Withdrawal", DocumentType.Monthly_Notice_Of_Withdrawal, null, null, invoiceDatesList.get(3), null), "MNOW not created ");

    }

}

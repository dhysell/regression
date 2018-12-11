package regression.r2.clock.billingcenter.installmentscheduling;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.payments.AccountPaymentsPaymentRequests;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.Status;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
/**
 * @Author JQU
 * @ Description: US13535 -- Carry forward the billed invoice if term extension is received before it goes due
 * 				If a policy change to extend is received when an invoice is in billed state where the payment plan is monthly then carry forward the billed invoice before the change to the next available scheduled planned invoice after the change.
Cancel the payment request
Create a new payment request
The testing for term extension will be limited to under a month
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/11%20-%20Installment%20Scheduling/11-07%20Policy%20Change%20Installment%20Scheduling.docx">See req# 11-07-12</a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/12%20-%20Payments/12-02%20Payment%20Requests.docx">See req# 12-02-21</a>
 * @DATE January 3rd, 2018
 * */
public class CarryForwardBilledInvoiceOnTermExtension extends BaseTest {
	private WebDriver driver;
    private GeneratePolicy myPolicyObj;
    private ARUsers arUser = new ARUsers();
    private int daysToExtend = NumberUtils.generateRandomNumberInt(1, 29);
    private double chargeFromExtension;
    private double firstInstallmentAmount;
    private Date firstInstallmentDueDate, firstInstallmentInvoiceDate;

    @Test
    public void issuePolicy() throws Exception {

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("Payment Request")
                .withPolOrgType(OrganizationType.Partnership)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Monthly)
                .withDownPaymentType(PaymentType.ACH_EFT)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"issuePolicy"})
    public void triggerFirstPaymentRequest() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuInvoices();
        AccountInvoices invoice = new AccountInvoices(driver);
        firstInstallmentAmount = invoice.getInvoiceAmountByRowNumber(2);
        firstInstallmentDueDate = DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getEffectiveDate(), DateAddSubtractOptions.Month, 1);
        firstInstallmentInvoiceDate = DateUtils.dateAddSubtract(firstInstallmentDueDate, DateAddSubtractOptions.Day, myPolicyObj.paymentPlanType.getInvoicingLeadTime());

        //pay down payment
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        //trigger first Payment Request
        ClockUtils.setCurrentDates(cf, firstInstallmentInvoiceDate);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        //verify the payment request and document
        acctMenu.clickAccountMenuPaymentsPaymentRequests();
        AccountPaymentsPaymentRequests request = new AccountPaymentsPaymentRequests(driver);
        request.verifyPaymentRequest(Status.Created, firstInstallmentInvoiceDate, firstInstallmentInvoiceDate, firstInstallmentDueDate, firstInstallmentDueDate, firstInstallmentDueDate, null, null, null, firstInstallmentAmount);
    }

    //extend the policy by 1 to 29 days
    @Test(dependsOnMethods = {"triggerFirstPaymentRequest"})
    public void extendTermBy5Days() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        StartPolicyChange policyChange = new StartPolicyChange(driver);
        policyChange.changeExpirationDate(DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getExpirationDate(), DateAddSubtractOptions.Day, daysToExtend), "extend term by " + daysToExtend + " days");
    }

    @Test(dependsOnMethods = {"extendTermBy5Days"})
    public void triggerSecondPaymentRequestAndVerify() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
        acctMenu.clickBCMenuCharges();
        AccountCharges charge = new AccountCharges(driver);
        charge.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPayer(60, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), TransactionType.Policy_Change, myPolicyObj.accountNumber);
        chargeFromExtension = NumberUtils.getCurrencyValueFromElement(charge.getChargesOrChargeHoldsPopupTableCellValue("Amount", DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, null, null, TransactionType.Policy_Change, null, null, null, null, null, null, null, null, null, null, null));

        //verify the Billed invoice was carried forward to next planned invoice
        acctMenu.clickAccountMenuInvoices();
        AccountInvoices invoice = new AccountInvoices(driver);
        boolean billedInvCarriedForwarded = invoice.verifyInvoice(firstInstallmentInvoiceDate, firstInstallmentDueDate, null, InvoiceType.Scheduled, null, InvoiceStatus.Carried_Forward, 0.0, 0.0);

        Date secondInstallmentDueDate = DateUtils.dateAddSubtract(firstInstallmentDueDate, DateAddSubtractOptions.Day, daysToExtend);
        Date secondInstallmentInvoiceDate = DateUtils.dateAddSubtract(secondInstallmentDueDate, DateAddSubtractOptions.Day, myPolicyObj.paymentPlanType.getInvoicingLeadTime());

        boolean newPlannedInv = invoice.verifyInvoice(secondInstallmentInvoiceDate, secondInstallmentDueDate, null, InvoiceType.Scheduled, null, InvoiceStatus.Planned, firstInstallmentAmount + chargeFromExtension, firstInstallmentAmount + chargeFromExtension);
        if (!billedInvCarriedForwarded || !newPlannedInv) {
            Assert.fail("The Billed invoice was not carried forward correctly.");
        }
        //trigger the second Payment Request
        ClockUtils.setCurrentDates(cf, secondInstallmentInvoiceDate);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        //verify that old payment request was closed and new one was created
        acctMenu.clickAccountMenuPaymentsPaymentRequests();
        AccountPaymentsPaymentRequests request = new AccountPaymentsPaymentRequests(driver);
        boolean cancelled = request.verifyPaymentRequest(Status.Canceled, firstInstallmentInvoiceDate, firstInstallmentInvoiceDate, firstInstallmentDueDate, firstInstallmentDueDate, firstInstallmentDueDate, null, null, null, firstInstallmentAmount);
        boolean newCreated = request.verifyPaymentRequest(Status.Created, secondInstallmentInvoiceDate, secondInstallmentInvoiceDate, secondInstallmentDueDate, secondInstallmentDueDate, secondInstallmentDueDate, null, null, null, firstInstallmentAmount + chargeFromExtension);
        if (!cancelled || !newCreated) {
            Assert.fail("Payment Requests are not correct.");
        }
    }
}
package regression.r2.clock.billingcenter.delinquency.testdelinquencies.renewaltermornottakeninsuredfullcancel;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.desktop.actions.DesktopActionsMultiplePayment;
import repository.bc.search.BCSearchAccounts;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.PaymentReversalReason;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GeneratePolicyHelper;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * @Author sgunda
 * @Requirement DE3956 - Delinquency not closed even if delinquent amount is zero or less than 10 %
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/68999021356d/detail/defect/62536628605">Link Text<DE3956 - Delinquency not closed even if delinquent amount is zero or less than 10 %/a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/06%20-%20Delinquency%20Cancel/06-01%20Insured%20Non-Pay%20Full%20Cancel%20Delinquency.docx">Link Text<06-01 Insured Non-Pay Full Cancel Delinquency: See requirement 06-01-09 %/a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/06%20-%20Delinquency%20Cancel/06-02%20Lienholder%20Non-Pay%20Full%20Cancel%20Delinquency.docx">Link Text<06-02 Lienholder Non-Pay Full Cancel Delinquency: See requirement 06-02-04 %/a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/06%20-%20Delinquency%20Cancel/06-03%20Insured%20Non-Pay%20Partial%20Cancel%20Delinquency.docx">Link Text<06-03 Insured Non-Pay Partial Cancel Delinquency: See requirement 06-03-08 %/a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/06%20-%20Delinquency%20Cancel/06-04%20Lienholder%20Non-Pay%20Partial%20Cancel%20Delinquency.docx">Link Text<06-04 Lienholder Non-Pay Partial Cancel Delinquency: See requirement 06-04-06/a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/06%20-%20Delinquency%20Cancel/06-07%20Not%20Taken%20Cancel%20Delinquency.docx">Link Text<06-07 Not Taken Cancel Delinquency: See requirement 06-07-06/a>
 * @Description Test if delinquencies are correctly triggered/not triggered and/or closed in the following scenarios: Equal to 100% of original delinquent amount payment
 * @DATE May 12, 2017
 */
public class NotTakenInsuredFullCancelThresholdTest extends BaseTest {
	private WebDriver driver;
    private ARUsers arUser;
    private BCAccountMenu acctMenu;
    private AccountInvoices acctInvoice;
    private BCCommonDelinquencies acctDelinquency;
    private AccountPayments payment;
    private GeneratePolicy myPolicyObj;
    private NewDirectBillPayment directPayment;
    private DesktopActionsMultiplePayment makePayment;
    private double orgDelinquentAmount, renewalDownPaymentDueAmount;
    private Date reversalDate;

    private repository.gw.enums.OpenClosed openOrClosed;
    private String errorMsg;
    private HashMap<String, Object> getValues;
    private double payAmount;

    private void gotoAccount(String accountNumber) {
    	BCSearchAccounts search = new BCSearchAccounts(driver);
        search.searchAccountByAccountNumber(accountNumber);
    }

    public HashMap<String, Object> getVariablesToRunTest(double orgDelinquentAmount) {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String printMsg = null;
        Double payDelinquentAmountBeforeRounding;

        switch (day) {
            case 1:
            case 6:
            case 11:
            case 16:
            case 21:
            case 26:
                //Making Payment for 100% of Delinquent amount
                payAmount = orgDelinquentAmount;
                openOrClosed = repository.gw.enums.OpenClosed.Closed;
                errorMsg = "Delinquency still exists after making payment for 100% of Original Delinquent Amount which is not Expected";
                printMsg = "This test is running a scenario where it pays 100% Original Delinquent Amount and check if delinquent exits or not";
                break;

            case 2:
            case 7:
            case 12:
            case 17:
            case 22:
            case 27:
                //Making Payment for less than 90% of Original Delinquent amount, which should exit delinquency
                payDelinquentAmountBeforeRounding = (orgDelinquentAmount) * 0.89;
                payAmount = BigDecimal.valueOf(payDelinquentAmountBeforeRounding).setScale(2, RoundingMode.UP).doubleValue();
                openOrClosed = repository.gw.enums.OpenClosed.Open;
                errorMsg = "Delinquency closed. This not expected, it should be open because payment made is less than 90% of orginal Delinquent amount - which is below Threshold";
                printMsg = "This test is running a scenario where it pays 89% Original Delinquent Amount and check if delinquent exits or not";
                break;

            case 3:
            case 8:
            case 13:
            case 18:
            case 23:
            case 28:
                //Making Payment for 90% of Delinquent amount
                payDelinquentAmountBeforeRounding = (orgDelinquentAmount) * 0.90;
                payAmount = BigDecimal.valueOf(payDelinquentAmountBeforeRounding).setScale(2, RoundingMode.UP).doubleValue();
                openOrClosed = repository.gw.enums.OpenClosed.Closed;
                errorMsg = "Delinquency still exists after making payment for 90% of Original Delinquent Amount which is not Expected";
                printMsg = "This test is running a scenario where it pays 90% Original Delinquent Amount and check if delinquent exits or not";
                break;

            case 4:
            case 9:
            case 14:
            case 19:
            case 24:
            case 29:
                //Making Payment for more than 100% of Original Delinquent amount, which should exit delinquency
                payDelinquentAmountBeforeRounding = (orgDelinquentAmount) * 1.05;
                payAmount = BigDecimal.valueOf(payDelinquentAmountBeforeRounding).setScale(2, RoundingMode.DOWN).doubleValue();
                openOrClosed = repository.gw.enums.OpenClosed.Closed;
                errorMsg = "Delinquency still exists after making payment for more than 100% of Original Delinquent Amount which is not Expected";
                printMsg = "This test is running a scenario where it pays 105% Original Delinquent Amount and check if delinquent exits or not";
                break;

            case 5:
            case 10:
            case 15:
            case 20:
            case 25:
            case 30:
                //Making Payment for more than 90% of Delinquent amount
                payDelinquentAmountBeforeRounding = (orgDelinquentAmount) * 0.92;
                payAmount = BigDecimal.valueOf(payDelinquentAmountBeforeRounding).setScale(2, RoundingMode.DOWN).doubleValue();
                openOrClosed = repository.gw.enums.OpenClosed.Closed;
                errorMsg = "Delinquency still exists after making payment for more than 90% of Original Delinquent Amount which is not Expected";
                printMsg = "This test is running a scenario where it pays 92% Original Delinquent Amount and check if delinquent exits or not";
                break;

            default:
                //Making Payment for less than 90% of Original Delinquent amount, which should exit delinquency
                payDelinquentAmountBeforeRounding = (orgDelinquentAmount) * 0.89;
                payAmount = BigDecimal.valueOf(payDelinquentAmountBeforeRounding).setScale(2, RoundingMode.UP).doubleValue();
                openOrClosed = repository.gw.enums.OpenClosed.Open;
                errorMsg = "Delinquency closed. This not expected, it should be open because payment made is less than 90% of orginal Delinquent amount - which is below Threshold";
                printMsg = "This test is running a scenario where it pays 89% Original Delinquent Amount and check if delinquent exits or not";
                break;
        }

        System.out.println(printMsg);
        getValues = new HashMap<String, Object>();
        getValues.put("payAmount", payAmount);
        getValues.put("openOrClosed", openOrClosed);
        getValues.put("errorMsg", errorMsg);

        return getValues;
    }


    @Test
    public void SquirePolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        myPolicyObj = new GeneratePolicyHelper(driver).generatePLSectionIAndIIPropertyAndLiabilityLinePLPolicy("InsuredThres", "NTaken",100, repository.gw.enums.PaymentPlanType.Annual, repository.gw.enums.PaymentType.Cash);

    }

    @Test(dependsOnMethods = {"SquirePolicy"})
    public void makeDownPaymentAndMoveClockToRenew() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).login(arUser.getUserName(), arUser.getPassword());

        makePayment = new DesktopActionsMultiplePayment(driver);
        makePayment.makeInsuredMultiplePaymentDownpayment(myPolicyObj, myPolicyObj.squire.getPolicyNumber());

        repository.gw.helpers.ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 10);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);

        repository.gw.helpers.ClockUtils.setCurrentDates(cf, repository.gw.helpers.DateUtils.dateAddSubtract(myPolicyObj.squire.getExpirationDate(), repository.gw.enums.DateAddSubtractOptions.Day, -50));

        new GuidewireHelpers(driver).logout();

    }


    @Test(dependsOnMethods = {"makeDownPaymentAndMoveClockToRenew"})
    public void renewlPolicyInPolicyCenter() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.squire.getPolicyNumber());

        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickRenewPolicy();

        new GuidewireHelpers(driver).logout();

        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.squire.getPolicyNumber());

        PolicySummary summaryPage = new PolicySummary(driver);
        if (summaryPage.checkIfActivityExists("Pre-Renewal Review")) {
            summaryPage.clickViewPreRenewalDirection();
            PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);

            boolean preRenewalDirectionExists = preRenewalPage.checkPreRenewalExists();
            if (preRenewalDirectionExists) {
                preRenewalPage.clickViewInPreRenewalDirection();
                preRenewalPage.closeAllPreRenewalDirectionExplanations();
                preRenewalPage.clickClosePreRenewalDirection();
                preRenewalPage.clickReturnToSummaryPage();
            }
        }
        StartRenewal renewal = new StartRenewal(driver);
        renewal.quoteAndIssueRenewal(repository.gw.enums.RenewalCode.Renew_Good_Risk, myPolicyObj);
        new GuidewireHelpers(driver).logout();

    }

    @Test(dependsOnMethods = {"renewlPolicyInPolicyCenter"})
    public void runBatches() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);
    }

    @Test(dependsOnMethods = {"runBatches"})
    public void makeRenewalDownPayment() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        repository.gw.helpers.ClockUtils.setCurrentDates(cf, repository.gw.helpers.DateUtils.dateAddSubtract(myPolicyObj.squire.getExpirationDate(), repository.gw.enums.DateAddSubtractOptions.Day, -2));

        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);

        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuInvoices();

        //Get Invoice Date and amount for second
        acctInvoice = new AccountInvoices(driver);
        renewalDownPaymentDueAmount = acctInvoice.getInvoiceDueAmountByDueDate(repository.gw.helpers.DateUtils.dateAddSubtract(myPolicyObj.squire.getExpirationDate(), repository.gw.enums.DateAddSubtractOptions.Day, -1));

        acctMenu.clickAccountMenuActionsNewDirectBillPayment();

        directPayment = new NewDirectBillPayment(driver);
        directPayment.makeDirectBillPaymentExecute(renewalDownPaymentDueAmount, myPolicyObj.accountNumber);

        repository.gw.helpers.ClockUtils.setCurrentDates(cf, repository.gw.helpers.DateUtils.dateAddSubtract(myPolicyObj.squire.getExpirationDate(), repository.gw.enums.DateAddSubtractOptions.Day, 2));
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);
    }

    @Test(dependsOnMethods = {"makeRenewalDownPayment"})
    public void reversePaymentMadeOnRenewalDownPaymentAndCheckForDelinquency() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuPayments();
        payment = new AccountPayments(driver);
        payment.reversePayment(renewalDownPaymentDueAmount, null, null, PaymentReversalReason.Payment_Moved);

        // letting account to go Delinquent in this search time
        gotoAccount(myPolicyObj.accountNumber);

        reversalDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);

        acctMenu.clickBCMenuDelinquencies();
        acctDelinquency = new BCCommonDelinquencies(driver);

        Assert.assertTrue(acctDelinquency.verifyDelinquencyStatus(repository.gw.enums.OpenClosed.Open, myPolicyObj.accountNumber, myPolicyObj.squire.getPolicyNumber(), reversalDate), "Delinquency does not exist Which is not Expected");
        orgDelinquentAmount = acctDelinquency.getOriginalDelinquentAmount(repository.gw.enums.OpenClosed.Open, myPolicyObj.accountNumber, myPolicyObj.squire.getPolicyNumber(), reversalDate);
    }

    @Test(dependsOnMethods = {"reversePaymentMadeOnRenewalDownPaymentAndCheckForDelinquency"})
    public void payDelinquentAmountAndCheckDelinquency() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();

        getVariablesToRunTest(orgDelinquentAmount);
        //getting values from HashMap
        payAmount = (double) getValues.get("payAmount");
        openOrClosed = (repository.gw.enums.OpenClosed) getValues.get("openOrClosed");
        errorMsg = (String) getValues.get("errorMsg");

        directPayment = new NewDirectBillPayment(driver);
        directPayment.makeDirectBillPaymentExecute(payAmount, myPolicyObj.accountNumber);

        //letting account to exit Delinquent in this search time
        gotoAccount(myPolicyObj.accountNumber);

        acctMenu.clickBCMenuDelinquencies();
        acctDelinquency = new BCCommonDelinquencies(driver);
        Assert.assertTrue(acctDelinquency.verifyDelinquencyStatus(openOrClosed, myPolicyObj.accountNumber, myPolicyObj.squire.getPolicyNumber(), reversalDate), errorMsg);

    }

}

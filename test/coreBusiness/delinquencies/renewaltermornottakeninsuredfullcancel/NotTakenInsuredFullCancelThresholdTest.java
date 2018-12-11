package coreBusiness.delinquencies.renewaltermornottakeninsuredfullcancel;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.PaymentPlanType;
import repository.gw.generate.GeneratePolicy;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.gw.helpers.*;
import repository.gw.helpers.DateUtils;
import repository.pc.workorders.renewal.StartRenewal;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;



public class NotTakenInsuredFullCancelThresholdTest extends BaseTest {

	private WebDriver driver;
    private GeneratePolicy myPolicyObj;

    private String policyNumber;
    private double downPayment;


    @Test
    public void SquirePolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // Creating a policy randomly
        myPolicyObj = Calendar.getInstance().get(Calendar.WEEK_OF_MONTH) % 2 == 0 ?
                new GeneratePolicyHelper(driver).generatePLSectionIAndIIPropertyAndLiabilityLinePLPolicy("InsuredPayer", "ThresholdTest",null, repository.gw.enums.PaymentPlanType.Annual, repository.gw.enums.PaymentType.Cash):
                new GeneratePolicyHelper(driver).generateBasicBOPPolicy("InsuredPayerThresholdTest",null, PaymentPlanType.Annual, repository.gw.enums.PaymentType.Cash);

        policyNumber = new BillingCenterHelper(driver).getPolicyNumberFromGeneratePolicyObject(myPolicyObj);
        downPayment = ((myPolicyObj.productType == repository.gw.enums.ProductLineType.Businessowners) ? myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount() : myPolicyObj.squire.getPremium().getDownPaymentAmount());


    }

    @Test(dependsOnMethods = {"SquirePolicy"})
    public void makeDownPaymentAndMoveClockToRenew() throws Exception {

        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);

        new BillingCenterHelper(driver).loginAsRandomARUserAndVerifyIssuancePolicyPeriod(myPolicyObj);
        new NewDirectBillPayment(driver).makeInsuredDownpayment(myPolicyObj,downPayment, policyNumber);
        repository.gw.helpers.ClockUtils.setCurrentDates(driver, repository.gw.enums.DateAddSubtractOptions.Day, 30);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);
        repository.gw.helpers.ClockUtils.setCurrentDates(cf, repository.gw.helpers.DateUtils.dateAddSubtract(myPolicyObj.squire.getExpirationDate(), repository.gw.enums.DateAddSubtractOptions.Day, -48));

        new GuidewireHelpers(driver).logout();

    }

    @Test(dependsOnMethods = {"makeDownPaymentAndMoveClockToRenew"})
    public void renewPolicyInPolicyCenter() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
    	driver = buildDriver(cf);
        new StartRenewal(driver).loginAsUWAndIssueRenewal(myPolicyObj);
    }


    @Test(dependsOnMethods = {"renewPolicyInPolicyCenter"})
    public void makeRenewalDownPayment() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);

        new BillingCenterHelper(driver).loginAsRandomARUserAndSearchAccount(myPolicyObj);
        new BillingCenterHelper(driver).waitUntilRenewalChargesArrive();

        BCAccountMenu bcAccountMenu = new BCAccountMenu(driver);
        bcAccountMenu.clickAccountMenuInvoices();
        AccountInvoices accountInvoices = new AccountInvoices(driver);

        repository.gw.helpers.ClockUtils.setCurrentDates(cf, accountInvoices.getInvoiceDateByInvoiceType(repository.gw.enums.InvoiceType.RenewalDownPayment));
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);

        bcAccountMenu.clickBCMenuDelinquencies();
        bcAccountMenu.clickAccountMenuInvoices();

        Date dueDate = accountInvoices.getInvoiceDueDateByInvoiceType(repository.gw.enums.InvoiceType.RenewalDownPayment);

        double renewalDownPaymentDueAmount = accountInvoices.getInvoiceDueAmountByDueDate(dueDate);


        HashMap<String, Object> getValues = new BillingCenterHelper(driver).getVariablesToRunDelinquencyTestScenarios(renewalDownPaymentDueAmount);
        //getting values from HashMap
        boolean delinquencyFound = (boolean) getValues.get("delinquencyFound");
        double payAmount = (double) getValues.get("payAmount");
        String errorMsg = (String) getValues.get("errorMsg");

        bcAccountMenu.clickAccountMenuActionsNewDirectBillPayment();

        new NewDirectBillPayment(driver).makeDirectBillPaymentExecute(payAmount, myPolicyObj.accountNumber);

        repository.gw.helpers.ClockUtils.setCurrentDates(cf, repository.gw.helpers.DateUtils.dateAddSubtract(dueDate, repository.gw.enums.DateAddSubtractOptions.Day, 1));
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);
        repository.gw.helpers.ClockUtils.setCurrentDates(driver, repository.gw.enums.DateAddSubtractOptions.Day, 7);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Release_Trouble_Ticket_Holds);

        bcAccountMenu.clickAccountMenuInvoices();
        bcAccountMenu.clickBCMenuDelinquencies();

        Assert.assertEquals(new BCCommonDelinquencies(driver).verifyDelinquencyByReason(repository.gw.enums.OpenClosed.Open, repository.gw.enums.DelinquencyReason.NotTaken, myPolicyObj.accountNumber, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter)), delinquencyFound, errorMsg);

    }


}

package scratchpad.sai.sgunda;


import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

public class PaymentRequest extends BaseTest {
    private ARUsers arUser = new ARUsers();
    private AccountInvoices acctInvoice;
    private BCAccountMenu accountMenu;
    private List<Date> dueDatesList, invoiceDatesList;
    private WebDriver driver;

    @Test
    public void SquirePolicy() throws Exception {

        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), "339986");
        accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuInvoices();

        acctInvoice = new AccountInvoices(driver);
        invoiceDatesList = acctInvoice.getListOfInvoiceDates();
        System.out.println(invoiceDatesList.get(0));
        System.out.println(invoiceDatesList.get(1));
        System.out.println(invoiceDatesList.get(2));

        dueDatesList = acctInvoice.getListOfDueDates();
        System.out.println("--------" + dueDatesList.get(0));
        System.out.println("--------" + dueDatesList.get(1));
        System.out.println("--------" + dueDatesList.get(2));


        ClockUtils.setCurrentDates(driver, invoiceDatesList.get(1));
        BatchHelpers batchHelpers = new BatchHelpers(driver);
        batchHelpers.runBatchProcess(BatchProcess.Invoice);
        ClockUtils.setCurrentDates(driver, dueDatesList.get(1));
        batchHelpers.runBatchProcess(BatchProcess.Payment_Request);
        batchHelpers.runBatchProcess(BatchProcess.Payment_Request);
        batchHelpers.runBatchProcess(BatchProcess.Payment_Request);
        accountMenu.clickBCMenuSummary();
        accountMenu.clickAccountMenuInvoices();
        System.out.println(acctInvoice.verifyInvoice(invoiceDatesList.get(1), dueDatesList.get(1), null, InvoiceType.Scheduled, null, InvoiceStatus.Billed, null, 0.00));
        ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);
        batchHelpers.runBatchProcess(BatchProcess.Invoice_Due);

        new GuidewireHelpers(driver).logout();

    }

}

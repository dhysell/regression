package regression.r2.noclock.billingcenter.activities;

import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.BCDesktopMyQueues;
import repository.bc.search.BCSearchAccounts;
import repository.bc.topmenu.BCTopMenu;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.AccountType;
import repository.gw.enums.ActivityQueuesBillingCenter;
import repository.gw.enums.DocumentType;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.actions.ActionsPC;
import repository.pc.actions.NewDocumentPC;

/**
 * @Description: Payment Voucher Activity should go to the General Queue
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Requirements/Activities/BC%20Requirements%20-%20Activities,%20Queues,%20Groups.docx">Activity Requirements</a>
 * @Author: By:Jessica Qu
 * @Test Environment: QA
 * Date: Oct. 09, 2015
 */
public class PaymentVoucherActivityGoToQueueTest extends BaseTest {
	private WebDriver driver;
    private String username = "sbrunson";
    private String password = "gw";
    private String documentDirectoryPath = "\\\\fbmsqa11\\test_data\\test-documents";
    private BCTopMenu topMenu;
    private String acctNum;

    //randomly find an existing "In Good Standing"account
    @Test
    public void findAccountNumber() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).login(username, password);
        BCSearchAccounts schAccts = new BCSearchAccounts(driver);
        acctNum = schAccts.findRecentAccountInGoodStanding("08-", AccountType.Insured);
        System.out.println("acctount number is : " + acctNum);
    }

    @Test(dependsOnMethods = {"findAccountNumber"})
    //@Test
    public void uploadPaymentVoucher() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByAccountNumber(username, password, acctNum.substring(0, 6));
        ActionsPC actions = new ActionsPC(driver);
        actions.click_Actions();
        actions.click_NewDocument();
        actions.click_LinkToExistingDoc();
        NewDocumentPC doc = new NewDocumentPC(driver);
        doc.setAttachment(documentDirectoryPath, "\\just_a_doc");
        doc.selectRelatedTo("Account");
        doc.selectDocumentType(DocumentType.Receipt_Of_Payment);
        doc.clickUpdate();
        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"uploadPaymentVoucher"})
    public void verifyPaymentActivityInGeneralQueue() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).login(username, password);
        topMenu = new BCTopMenu(driver);
        topMenu.clickDesktopTab();
        BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
        desktopMenu.clickDesktopMenuMyQueues();
        BCDesktopMyQueues myQueue = new BCDesktopMyQueues(driver);
        myQueue.setMyQueuesFilter(ActivityQueuesBillingCenter.ARGeneralWesternCommunity);
        //sort twice to get the latest activity
        myQueue.sortQueueTableByTitle("Opened");
        myQueue.sortQueueTableByTitle("Opened");
        boolean foundPmtVoucher = myQueue.verifyMyQueueTable(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), "Scanned Receipt of Payment Received", acctNum);
        if (!foundPmtVoucher) {
            Assert.fail("couldn't find the payment voucher in the general queue.");
        }
        new GuidewireHelpers(driver).logout();
    }
}


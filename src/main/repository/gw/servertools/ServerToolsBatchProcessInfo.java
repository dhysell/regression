package repository.gw.servertools;

import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.payments.AccountPaymentsPaymentRequests;
import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.BatchProcess;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;

import java.util.Date;

public class ServerToolsBatchProcessInfo extends BasePage {

    private repository.gw.helpers.TableUtils tableUtils;
    private Config batchConfig;
    private WebDriver driver;

    public ServerToolsBatchProcessInfo(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.tableUtils = new repository.gw.helpers.TableUtils(driver);
    }

    public ServerToolsBatchProcessInfo(WebDriver driver, Config batchConfig) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
        this.batchConfig = batchConfig;
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------

    public repository.gw.elements.Guidewire8Select select_BatchProcessInfoProcessType() {
        return new Guidewire8Select(driver, "//table[(contains(@id,'BatchProcessInfo:BatchProcessScreen:BatchProcessesLV:ProcessUsageFilter-triggerWrap')]");
    }

    public WebElement button_BatchProcessInfoRefresh() {
        return find(By.xpath("//div[@id='BatchProcessInfo:BatchProcessScreen:BatchProcessesLV']"));
    }

    public WebElement button_BatchProcessInfoDownload() {
        return find(By.xpath("//div[@id='BatchProcessInfo:BatchProcessScreen:BatchProcessesLV']"));
    }

    public WebElement table_BatchProcessProcesses() {
        return find(By.xpath("//div[@id='BatchProcessInfo:BatchProcessScreen:BatchProcessesLV']"));
    }

    private WebElement button_RunBatchProcess(int batchProcessTableRowNumber) {

        String xPathToCheck = "";
        if (getDriver().getCurrentUrl().toUpperCase().contains(ApplicationOrCenter.ClaimCenter.getValue())) {
            xPathToCheck = "//a[contains(@id, 'BatchProcessInfo:BatchProcessScreen:BatchProcessesLV:" + batchProcessTableRowNumber + ":RunBatchWithoutNotify')]";
        } else {
            xPathToCheck = "//a[contains(@id, 'BatchProcessInfo:BatchProcessScreen:BatchProcessesLV:" + (batchProcessTableRowNumber - 1) + ":RunBatchWithoutNotify')]";
        }

        return find(By.xpath(xPathToCheck));
    }

    private WebElement button_StopBatchProcess(int batchProcessTableRowNumber) {
        String xPathToCheck = "//a[contains(@id, 'BatchProcessInfo:BatchProcessScreen:BatchProcessesLV:" + (batchProcessTableRowNumber - 1) + ":TerminateBatchWithoutNotify')]";
        return find(By.xpath(xPathToCheck));
    }

    private WebElement button_StopScheduledRun(int batchProcessTableRowNumber) {
        String xPathToCheck = "//a[contains(@id, 'BatchProcessInfo:BatchProcessScreen:BatchProcessesLV:" + (batchProcessTableRowNumber - 1) + ":ScheduleStop')]";
        return find(By.xpath(xPathToCheck));
    }

    private WebElement button_StartScheduledRun(int batchProcessTableRowNumber) {
        String xPathToCheck = "//a[contains(@id, 'BatchProcessInfo:BatchProcessScreen:BatchProcessesLV:" + (batchProcessTableRowNumber - 1) + ":ScheduleStart')]";
        return find(By.xpath(xPathToCheck));
    }

    private WebElement button_BatchProcessDownloadHistory(int batchProcessTableRowNumber) {
        String xPathToCheck = "//a[contains(@id, 'BatchProcessInfo:BatchProcessScreen:BatchProcessesLV:" + (batchProcessTableRowNumber - 1) + ":DownloadHistory')]";
        return find(By.xpath(xPathToCheck));
    }

    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------


    public int getBatchProcessTableRowNumber(repository.gw.enums.BatchProcess batchProcess) {
        int batchProcessRowNumber = tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValueEquals(table_BatchProcessProcesses(), "Batch Process", batchProcess.getServerToolsValue()));
        return batchProcessRowNumber;
    }


    public void runBatchProcess(repository.gw.enums.BatchProcess batchProcess) {
        try {
            openBatchProcessPageInNewWindow();
            int batchProcessTableRowNumber = getBatchProcessTableRowNumber(batchProcess);
            clickWhenClickable(button_RunBatchProcess(batchProcessTableRowNumber));
            verifyBatchProcessCompletion(batchProcess);
            logOutAndCloseBatchProcessWindow();
        } catch (Exception e) {
            systemOut("There was an exception caught while Running batch processes. Attempting to log out gracefully...");
            e.printStackTrace(System.out);
            logOutAndCloseBatchProcessWindow();
        }

        if (!(batchProcess.getCorrespondingWorkQueue() == null)) {
            new BatchHelpers(this.batchConfig).verifyWorkQueueProcessing(batchProcess.getCorrespondingWorkQueue());
        }
    }


    public void stopScheduledRun(repository.gw.enums.BatchProcess batchProcess) {
        try {
            openBatchProcessPageInNewWindow();
            int batchProcessTableRowNumber = getBatchProcessTableRowNumber(batchProcess);
            clickWhenClickable(button_StopScheduledRun(batchProcessTableRowNumber));

            logOutAndCloseBatchProcessWindow();
        }catch ( Exception e) {
            systemOut("\n @@@@@@@@@@@@ Either batch is already stopped or something failed, Please inspect."+ getDriver().getCurrentUrl() +" @@@@@@@@@@@@@@@... \n");
            systemOut("There was an exception caught while Stopping batch processes. Attempting to log out gracefully...");
            e.printStackTrace(System.out);
            logOutAndCloseBatchProcessWindow();
         }
    }

   public void startScheduledRun(repository.gw.enums.BatchProcess batchProcess) {
       try {
           openBatchProcessPageInNewWindow();
           int batchProcessTableRowNumber = getBatchProcessTableRowNumber(batchProcess);
           clickWhenClickable(button_StartScheduledRun(batchProcessTableRowNumber));

           logOutAndCloseBatchProcessWindow();
       }catch ( Exception e) {
           systemOut("\n @@@@@@@@@@@@ Either batch is already started or something failed, Please inspect."+ getDriver().getCurrentUrl() +" @@@@@@@@@@@@@@@... \n");
           systemOut("There was an exception caught while Starting batch processes. Attempting to log out gracefully...");
           e.printStackTrace(System.out);
           logOutAndCloseBatchProcessWindow();
       }
    }


    public void clickBatchProcessDownloadHistory(repository.gw.enums.BatchProcess batchProcess) {
        openBatchProcessPageInNewWindow();

        int batchProcessTableRowNumber = getBatchProcessTableRowNumber(batchProcess);

        clickWhenClickable(button_BatchProcessDownloadHistory(batchProcessTableRowNumber));

        logOutAndCloseBatchProcessWindow();
    }


    public void runPaymentRequest(Date invoiceDate, Date dueDate, String invoiceNumber, Double amount) throws Exception {
        BCAccountMenu menu = new BCAccountMenu(getDriver());
        menu.clickAccountMenuPayments();
        menu.clickAccountMenuPaymentsPaymentRequests();

        AccountPaymentsPaymentRequests paymentRequests = new AccountPaymentsPaymentRequests(getDriver());
        boolean paymentRequestExists = paymentRequests.paymentRequestExists(invoiceDate, dueDate, invoiceNumber, amount);
        if (paymentRequestExists) {
            if (!(paymentRequests.getPaymentRequestStatus(invoiceDate, dueDate, invoiceNumber, amount).getValue().equalsIgnoreCase("Created"))) {
                Assert.fail("The Payment Request was not in a 'Created' Status. The test cannot continue.");
            }
        } else {
            Assert.fail("The Payment Request could not be found in the requests table. The test cannot continue.");
        }

        ClockUtils.setCurrentDates(driver, dueDate);
        runBatchProcess(repository.gw.enums.BatchProcess.Payment_Request);

        refreshPage();

        if (!(paymentRequests.getPaymentRequestStatus(invoiceDate, dueDate, invoiceNumber, amount).getValue().equalsIgnoreCase("Requested"))) {
            Assert.fail("The Payment Request was not in a 'Requested' Status. The test cannot continue.");
        }

        runBatchProcess(repository.gw.enums.BatchProcess.Payment_Request);

        refreshPage();

        if (!(paymentRequests.getPaymentRequestStatus(invoiceDate, dueDate, invoiceNumber, amount).getValue().equalsIgnoreCase("Drafted"))) {
            Assert.fail("The Payment Request was not in a 'Closed' Status. The test cannot continue.");
        }

        runBatchProcess(repository.gw.enums.BatchProcess.Payment_Request);

        refreshPage();

        if (!(paymentRequests.getPaymentRequestStatus(invoiceDate, dueDate, invoiceNumber, amount).getValue().equalsIgnoreCase("Closed"))) {
            Assert.fail("The Payment Request was not in a 'Closed' Status. The test cannot continue.");
        }
    }

    private void openBatchProcessPageInNewWindow() {
        repository.gw.login.Login loginPage = new Login(getDriver());
        loginPage.login("su", "gw");

        goToBatchProcessPageAfterLogin(driver);
        waitForPageLoad();
    }

    private void goToBatchProcessPageAfterLogin(WebDriver driver) {
        pressAltShiftT();

        ServerToolsSideBar serverToolsSideBar = new ServerToolsSideBar(getDriver());
        serverToolsSideBar.clickBatchProcessInfo();
    }

    private void verifyBatchProcessCompletion(BatchProcess batchProcess) {
        String lastRunStatus = null;
        for (int i = 1; i < 90; i++) {
            refreshPage();
            sleep(1); //Used to ensure that the page refreshes fully.
            lastRunStatus = tableUtils.getCellTextInTableByRowAndColumnName(table_BatchProcessProcesses(), getBatchProcessTableRowNumber(batchProcess), "Last Run Status");

            if (lastRunStatus.equalsIgnoreCase("Completed")) {
                break;
            }
            if (i > 1 && lastRunStatus.equalsIgnoreCase("Failed/Interrupted")) {
                Assert.fail("The Batch Process " + batchProcess + " failed or did not complete. Please investigate the cause.");
            }
        }
        if (!lastRunStatus.equalsIgnoreCase("Completed")) {
            Assert.fail("The Batch Process " + batchProcess + " did not return a status of 'completed' after 90 attempts.");
        }
    }

    private void logOutAndCloseBatchProcessWindow() {
        ServerToolsSideBar serverToolsSideBar = new ServerToolsSideBar(getDriver());
        serverToolsSideBar.logOutOfApplication();
        getDriver().close();
        getDriver().quit();
    }
}

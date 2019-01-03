package repository.gw.servertools;

import com.idfbins.helpers.EmailUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.WorkQueue;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;

import java.util.ArrayList;

public class ServerToolsWorkQueueInfo extends BasePage {
	
    private WebDriver driver;
    private TableUtils tableUtils;
    
    public ServerToolsWorkQueueInfo(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    private int itemsAvailable = 0;
    private int itemsCheckedOut = 0;
    private int executorsRunning = 1;
    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------

    @FindBy(xpath = "//span[@id='WorkQueueInfo:WorkQueueInfoScreen:WorkQueueInfoLV_tb:refresh-btnInnerEl']")
	private WebElement button_WorkQueueInfoRefresh;

	@FindBy(xpath = "//span[@id='WorkQueueInfo:WorkQueueInfoScreen:WorkQueueInfoLV_tb:download-btnInnerEl']")
	private WebElement button_WorkQueueInfoDownload;

	@FindBy(xpath = "//span[@id='WorkQueueInfo:WorkQueueInfoScreen:WorkQueueInfoLV_tb:downloadRawData-btnInnerEl']")
	private WebElement button_WorkQueueInfoDownloadRawData;

	@FindBy(xpath = "//div[@id='WorkQueueInfo:WorkQueueInfoScreen:WorkQueueInfoLV']")
	private WebElement table_WorkQueueProcesses;

    private WebElement button_RunWorkQueueWriter(int batchProcessTableRowNumber) {
        String xPathToCheck = "//a[contains(@id, 'WorkQueueInfo:WorkQueueInfoScreen:WorkQueueInfoLV:" + (batchProcessTableRowNumber - 1) + ":RunWriter')]";
        return find(By.xpath(xPathToCheck));
    }

    private WebElement button_NotifyWorkQueueExecutor(int batchProcessTableRowNumber) {
        String xPathToCheck = "//a[contains(@id, 'WorkQueueInfo:WorkQueueInfoScreen:WorkQueueInfoLV:" + (batchProcessTableRowNumber - 1) + ":Notify')]";
        return find(By.xpath(xPathToCheck));
    }

    private WebElement button_StopWorkQueueExecutor(int batchProcessTableRowNumber) {
        String xPathToCheck = "//a[contains(@id, 'WorkQueueInfo:WorkQueueInfoScreen:WorkQueueInfoLV:" + (batchProcessTableRowNumber - 1) + ":StopWorkers')]";
        return find(By.xpath(xPathToCheck));
    }

    private WebElement button_RestartWorkQueueExecutor(int batchProcessTableRowNumber) {
        String xPathToCheck = "//a[contains(@id, 'WorkQueueInfo:WorkQueueInfoScreen:WorkQueueInfoLV:" + (batchProcessTableRowNumber - 1) + ":RestartWorkers')]";
        return find(By.xpath(xPathToCheck));
    }

    private WebElement button_WorkQueueDownloadHistory(int batchProcessTableRowNumber) {
        String xPathToCheck = "//a[contains(@id, 'WorkQueueInfo:WorkQueueInfoScreen:WorkQueueInfoLV:" + (batchProcessTableRowNumber - 1) + ":PrintHistory')]";
        return find(By.xpath(xPathToCheck));
    }

    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------


    public int getWorkQueueTableRowNumber(WorkQueue workQueue) {
        int batchProcessRowNumber = tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValueEquals(table_WorkQueueProcesses, "Work Queue", workQueue.getValue()));
        return batchProcessRowNumber;
    }


    private void verifyWorkQueueWorkerCompletion(WorkQueue workQueue) {
        ArrayList<String> emailsToSendTo = new ArrayList<String>();
        emailsToSendTo.add("bhiltbrand@idfbins.com");
        emailsToSendTo.add("ryoung@idfbins.com");

        String emailBody = "To Whom it May Concern,<br/><br/><p>While checking the work queue to ensure all items had processed, the "
                + workQueue + " Work Queue timed out while processing items with items still left to be processed."
                + " This indicates that there was an issue with the workers that needs investigated. Please see below for detailed information"
                + " regarding the queue:<br/><br/><b>Environment: " + driver.getCurrentUrl() + "</b><br/><b>Items Available: "
                + itemsAvailable + "</b><br/><b>Items Checked-Out: " + itemsCheckedOut + "</b><br/><b>Please investigate this issue to determine "
                + "the cause. Thank you.</b>";

        boolean timeout = waitForItemsToProcess(workQueue);

        if (((itemsAvailable > 0) || (itemsCheckedOut > 0)) && (timeout == true)) {
            new EmailUtils().sendEmail(emailsToSendTo, workQueue + " Work Queue workers stopped", emailBody, null);
            Assert.fail("The Work Queue Executor for the " + workQueue.getValue() + " Queue has been restarted and still stopped before finishing work items. Please investigate.");
        }
    }


    public void verifyWorkQueueWorkerCompletionInNewWindow(WorkQueue workQueue) {
        try {
            openWorkQueuePageInNewWindow();
            verifyWorkQueueWorkerCompletion(workQueue);
            logOutAndCloseWorkQueueWindow();
        } catch (Exception e) {
            systemOut("There was an exception caught while processing work items. Attempting to log out gracefully...");
            e.printStackTrace(System.out);
            logOutAndCloseWorkQueueWindow();
        }
    }


    public void clickWorkQueueRunWriterButton(WorkQueue workQueue) {
        int workQueueTableRowNumber = getWorkQueueTableRowNumber(workQueue);

        clickWhenClickable(button_RunWorkQueueWriter(workQueueTableRowNumber));
    }


    public void clickWorkQueueNotifyExecutorButton(WorkQueue workQueue) {
        int workQueueTableRowNumber = getWorkQueueTableRowNumber(workQueue);

        clickWhenClickable(button_NotifyWorkQueueExecutor(workQueueTableRowNumber));
    }


    public void clickWorkQueueStopExecutorButton(WorkQueue workQueue) {
        int workQueueTableRowNumber = getWorkQueueTableRowNumber(workQueue);

        clickWhenClickable(button_StopWorkQueueExecutor(workQueueTableRowNumber));
    }


    public void clickWorkQueueRestartExecutorButton(WorkQueue workQueue) {
        int workQueueTableRowNumber = getWorkQueueTableRowNumber(workQueue);

        clickWhenClickable(button_RestartWorkQueueExecutor(workQueueTableRowNumber));
    }


    public void clickWorkQueueDownloadHistoryButton(WorkQueue workQueue) {
        int workQueueTableRowNumber = getWorkQueueTableRowNumber(workQueue);

        clickWhenClickable(button_WorkQueueDownloadHistory(workQueueTableRowNumber));
    }

    private void openWorkQueuePageInNewWindow() {
        Login loginPage = new Login(driver);
        loginPage.login("su", "gw");

        goToWorkQueuePageAfterLogin(driver);
    }

    private void goToWorkQueuePageAfterLogin(WebDriver driver) {
        pressAltShiftT();

        repository.gw.servertools.ServerToolsSideBar serverToolsSideBar = new repository.gw.servertools.ServerToolsSideBar(driver);
        serverToolsSideBar.clickWorkQueueInfo();
    }

    private boolean waitForItemsToProcess(WorkQueue workQueue) {
        ArrayList<Integer> itemsAvailableList = new ArrayList<Integer>();
        ArrayList<Integer> itemsCheckedOutList = new ArrayList<Integer>();
        ArrayList<Integer> executorsRunningList = new ArrayList<Integer>();
        int i = 0;
        boolean restartExecutorTrigger = true;
        boolean timeout = false;
        int timer = 20 * 60; //minutes to work on processing the queue * 60 seconds in a minute.

        getDriver().navigate().refresh();
        sleep(1); //Used to ensure the page refreshes fully.
        itemsAvailable = Integer.valueOf(tableUtils.getCellTextInTableByRowAndColumnName(table_WorkQueueProcesses, getWorkQueueTableRowNumber(workQueue), "Available"));
        itemsCheckedOut = Integer.valueOf(tableUtils.getCellTextInTableByRowAndColumnName(table_WorkQueueProcesses, getWorkQueueTableRowNumber(workQueue), "Checked Out"));
        executorsRunning = Integer.valueOf(tableUtils.getCellTextInTableByRowAndColumnName(table_WorkQueueProcesses, getWorkQueueTableRowNumber(workQueue), "Executors Running"));

        while (((!(itemsAvailable == 0)) || (!(itemsCheckedOut == 0))) && (!(timer <= 0))) {
            itemsAvailableList.add(itemsAvailable);
            itemsCheckedOutList.add(itemsCheckedOut);
            executorsRunningList.add(executorsRunning);
            i++;

            if ((i > 2) && restartExecutorTrigger) {
                restartExecutorTrigger = false;
                boolean itemsAvailableMatch = listItemsMatch(itemsAvailableList);
                boolean itemsCheckedOutMatch = listItemsMatch(itemsCheckedOutList);
                boolean executorsRunningMatch = listItemsMatch(executorsRunningList);
                if (itemsAvailableMatch && itemsCheckedOutMatch && executorsRunningMatch) {
                    clickWorkQueueRestartExecutorButton(workQueue);
                }
            }

            getDriver().navigate().refresh();
            sleep(1); //Used to ensure the page refreshes fully.

            itemsAvailable = Integer.valueOf(tableUtils.getCellTextInTableByRowAndColumnName(table_WorkQueueProcesses, getWorkQueueTableRowNumber(workQueue), "Available"));
            itemsCheckedOut = Integer.valueOf(tableUtils.getCellTextInTableByRowAndColumnName(table_WorkQueueProcesses, getWorkQueueTableRowNumber(workQueue), "Checked Out"));
            executorsRunning = Integer.valueOf(tableUtils.getCellTextInTableByRowAndColumnName(table_WorkQueueProcesses, getWorkQueueTableRowNumber(workQueue), "Executors Running"));

            if (((!(itemsAvailable == 0)) || (!(itemsCheckedOut == 0)))) {
                sleep(10); //Used to wait for 10 seconds in between page refreshes and checks for the queues  to be clear.
                timer = timer - 12;
            }
        }

        if (((!(itemsAvailable == 0)) || (!(itemsCheckedOut == 0))) && (timer <= 0)) {
            timeout = true;
        }
        return timeout;
    }

    private boolean listItemsMatch(ArrayList<Integer> list) {
        int firstItem = list.get(0);
        boolean match = true;
        for (int item : list) {
            if (firstItem == item) {
                match = true;
            } else {
                match = false;
                break;
            }
        }
        return match;
    }

    private void logOutAndCloseWorkQueueWindow() {
        repository.gw.servertools.ServerToolsSideBar serverToolsSideBar = new ServerToolsSideBar(driver);
        serverToolsSideBar.logOutOfApplication();
        getDriver().close();
        getDriver().quit();
    }
}

package repository.gw.helpers;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.payments.AccountPaymentsPaymentRequests;
import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.driverConfiguration.DriverBuilder;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.WorkQueue;
import repository.gw.servertools.ServerToolsBatchProcessInfo;
import repository.gw.servertools.ServerToolsWorkQueueInfo;

import java.util.Date;


/**
 *  Holder Place for things used to in Guidewire that don't fit in other util classes or page factories.
 *
 */
public class BatchHelpers {

	private Config batchConfig;
	
	public BatchHelpers(Config config) {
		this.batchConfig = config;
	}
	
	public BatchHelpers(WebDriver driver) {
		try {
			this.batchConfig = new Config(driver.getCurrentUrl());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Unable to get URL from current Driver.");
		}
	}

	/**
     * This method starts the passed-in batch process.
     *
     * @param batchProcess BatchProcess to run. See {@link repository.gw.enums.BatchProcess}  for BatchProcess options.
     */
    public void runBatchProcess(repository.gw.enums.BatchProcess batchProcess) {
    	WebDriver batchDriver = null;
		try {
			batchDriver = new DriverBuilder().buildGWWebDriver(this.batchConfig);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Unable to spin up driver");
		}
        
        System.out.println("Running Batch Process: " + batchProcess.getServerToolsValue());
        ServerToolsBatchProcessInfo serverToolsBatchProcessInfo = new ServerToolsBatchProcessInfo(batchDriver, batchConfig);
        serverToolsBatchProcessInfo.runBatchProcess(batchProcess);
    }

	/**
     * This method starts the passed-in batch process.
     *
     * @param batchProcess BatchProcess to run. See {@link repository.gw.enums.BatchProcess}  for BatchProcess options.
     */
    public void stopBatchProcess(repository.gw.enums.BatchProcess batchProcess) {
    	WebDriver batchDriver = null;
		try {
			batchDriver = new DriverBuilder().buildGWWebDriver(this.batchConfig);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Unable to spin up driver");
		}

        System.out.println("Stopping Batch Process: " + batchProcess.getServerToolsValue());
        ServerToolsBatchProcessInfo serverToolsBatchProcessInfo = new ServerToolsBatchProcessInfo(batchDriver, batchConfig);
        serverToolsBatchProcessInfo.stopScheduledRun(batchProcess);
    }

	/**
     * This method starts the passed-in batch process.
     *
     * @param batchProcess BatchProcess to run. See {@link repository.gw.enums.BatchProcess}  for BatchProcess options.
     */
    public void startBatchProcess(repository.gw.enums.BatchProcess batchProcess) {
    	WebDriver batchDriver = null;
		try {
			batchDriver = new DriverBuilder().buildGWWebDriver(this.batchConfig);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Unable to spin up driver");
		}

        System.out.println("Starting Batch Process: " + batchProcess.getServerToolsValue());
        ServerToolsBatchProcessInfo serverToolsBatchProcessInfo = new ServerToolsBatchProcessInfo(batchDriver, batchConfig);
        serverToolsBatchProcessInfo.startScheduledRun(batchProcess);
    }

    /**
     * This method checks a running WorkQueue and waits for it to complete processing items.
     *
     * @param workQueue WorkQueue to verify for completion. See {@link repository.gw.enums.WorkQueue}  for WorkQueue options.
     */
    public void verifyWorkQueueProcessing(WorkQueue workQueue) {
    	WebDriver batchDriver = null;
		try {
			batchDriver = new DriverBuilder().buildGWWebDriver(this.batchConfig);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Unable to spin up driver");
		}
		
        System.out.println("Verifying Work Queue Completion: " + workQueue.getValue());
        repository.gw.servertools.ServerToolsWorkQueueInfo serverToolsWorkQueueInfo = new ServerToolsWorkQueueInfo(batchDriver);
        serverToolsWorkQueueInfo.verifyWorkQueueWorkerCompletionInNewWindow(workQueue);
    }
    
    
    
    
    
    
    
    
    
    /**This method will run the payment request batch in billing center. It will verify that the payment changes state as required and fail if it doesn't
     * IMPORTANT: THIS METHOD WORKS DIFFERENTLY THAN OTHER BATCH METHODS! THE ORIGINAL DRIVER MUST BE PASSED IN SO THAT EXECUTION CAN CONTINUE WITH THE CORRECT DRIVER!
     * @param driver This is the driver passed in from test exectution
     * @param invoiceDate
     * @param dueDate
     * @param invoiceNumber
     * @param amount
     * @throws Exception
     */
    public void runPaymentRequest(WebDriver driver, Date invoiceDate, Date dueDate, String invoiceNumber, Double amount) throws Exception {
		BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickAccountMenuPayments();
		menu.clickAccountMenuPaymentsPaymentRequests();
		
		AccountPaymentsPaymentRequests paymentRequests = new AccountPaymentsPaymentRequests(driver);
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
		
		new BasePage(driver).refreshPage();
		
		if (!(paymentRequests.getPaymentRequestStatus(invoiceDate, dueDate, invoiceNumber, amount).getValue().equalsIgnoreCase("Requested"))) {
			Assert.fail("The Payment Request was not in a 'Requested' Status. The test cannot continue.");
		}
		
		runBatchProcess(repository.gw.enums.BatchProcess.Payment_Request);
		
		new BasePage(driver).refreshPage();
		
		if (!(paymentRequests.getPaymentRequestStatus(invoiceDate, dueDate, invoiceNumber, amount).getValue().equalsIgnoreCase("Drafted"))) {
			Assert.fail("The Payment Request was not in a 'Closed' Status. The test cannot continue.");
		}
		
		runBatchProcess(BatchProcess.Payment_Request);
		
		new BasePage(driver).refreshPage();
		
		if (!(paymentRequests.getPaymentRequestStatus(invoiceDate, dueDate, invoiceNumber, amount).getValue().equalsIgnoreCase("Closed"))) {
			Assert.fail("The Payment Request was not in a 'Closed' Status. The test cannot continue.");
		}
	}
}

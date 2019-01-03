package repository.gw.administration;

import com.idfbins.enums.OkCancel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import persistence.globaldatarepo.entities.EventMessaging;
import persistence.globaldatarepo.helpers.EventMessagingHelper;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.*;
import repository.gw.exception.GuidewireException;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminEventMessages extends BasePage {

    private TableUtils tableUtils;
    private WebDriver driver;

    public AdminEventMessages(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    // //////////
    // Elements//
    // //////////

    @FindBy(xpath = "//div[contains(@id, ':MessagingDestinationControlListScreen:MessagingDestinationsControlLV')]")
    private WebElement table_MessageQueues;
       
    @FindBy(xpath = "//div[contains(@id, 'MessageControlForDestinationListScreen:MessageControlForDestinationListLV')]")
    private WebElement table_SafeOrderObjectTable;

    @FindBy(xpath = "//div[contains(@id, 'MessageControlForSOOListScreen:MessageControlForSOOListLV')]")
    private WebElement table_NonSafeOrderedMessagesTable;

    @FindBy(xpath = "//a[contains(@id, 'MessagingDestinationControlListScreen:MessagingDestinationControlList_SuspendButton')]")
    public WebElement button_SuspendMessageQueue;

    @FindBy(xpath = "//a[contains(@id, 'MessagingDestinationControlListScreen:MessagingDestinationControlList_ResumeButton')]")
    public WebElement button_ResumeMessageQueue;

    @FindBy(xpath = "//a[contains(@id, 'MessagingDestinationControlListScreen:MessagingDestinationControlList_RestartButton')]")
    public WebElement button_RestartMessageQueue;

    @FindBy(xpath = "//a[contains(@id, 'MessagingDestinationControlListScreen:MessagingDestinationControlList_RestartMessagingEngineButton')]")
    public WebElement button_RestartMessagingEngine;

    @FindBy(xpath = "//a[contains(@id, 'MessageControlForSOOListScreen:MessageControlForSOOListScreen_RetryButton') or contains(@id, 'MessageControlForDestinationListScreen:MessageControlForDestinationListScreen_RetryButton')]")
    public WebElement button_RetryMessage;

    @FindBy(xpath = "//a[contains(@id, 'MessageControlForSOOListScreen:MessageControlForSOOListScreen_SkipButton') or contains(@id, 'MessageControlForDestinationListScreen:MessageControlForDestinationListScreen_SkipButton')]")
    public WebElement button_SkipMessage;

    @FindBy(xpath = "//a[contains(@id, 'MessageControlForDestinationListScreen:MessageControlForDestinationListScreen_ResyncButton')]")
    public WebElement button_ResyncMessage;

    @FindBy(xpath = "//textarea[contains(@id, 'MessagePayloadPopup:MessagePayloadScreen:PayLoadDV:Payload-inputEl') or contains(@id, 'MessagePayloadPopup:MessagePayloadScreen:PayLoadDV:PCPayload-inputEl')]")
    private WebElement text_MessagePayload;

    @FindBy(xpath = "//a[contains(@id, 'MessagePayloadPopup:__crumb__')]")
    public WebElement link_ReturnToMessagesList;

    @FindBy(xpath = "//a[contains(@id, 'MessageControlForSOOList:MessageControlForSOOList_UpLink')]")
    public WebElement link_UpToMessagingDestination;

    @FindBy(xpath = "//a[contains(@id, 'MessageControlForDestinationList_UpLink')]")
    public WebElement link_UpToMessageQueues;

    public Guidewire8Select select_Filter() {
        return new Guidewire8Select(driver, "//table[contains(@id,':SOOMessageFilter-triggerWrap')]");
    }

    private WebElement queueLink(MessageQueue messageQueue) {
        return find(By.xpath("//a[contains(text(),'" + messageQueue.getValue() + "')]"));
    }

    // /////////
    // Methods//
    // /////////


    public WebElement getEventMessageQueuesTable() {
        return table_EventMessages;
    }


    public WebElement getEventMessageQueuesSafeOrderObjectTable() {
        return table_SafeOrderObjectTable;
    }


    public WebElement getEventMessageQueuesNonSafeOrderObjectTable() {
        return table_NonSafeOrderedMessagesTable;
    }


    public WebElement getMessageQueuesTableRow(MessageQueue messageQueue, Integer messageQueueID, MessageQueueStatus messageQueueStatus, Integer messageQueueFailedCount, Integer messageQueueRetryableErrorCount, Integer messageQueueInFlightCount, Integer messageQueueUnsentCount, Integer messageQueueBatchedCount, Integer messageQueueAwaitingRetryCount) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();

        if (messageQueue != null) {
            columnRowKeyValuePairs.put("Destination", messageQueue.getValue());
        }
        if (messageQueueID != null) {
            columnRowKeyValuePairs.put("ID", String.valueOf(messageQueueID));
        }
        if (messageQueueStatus != null) {
            columnRowKeyValuePairs.put("Status", messageQueueStatus.getValue());
        }
        if (messageQueueFailedCount != null) {
            columnRowKeyValuePairs.put("Failed", String.valueOf(messageQueueFailedCount));
        }
        if (messageQueueRetryableErrorCount != null) {
            columnRowKeyValuePairs.put("Retryable Error", String.valueOf(messageQueueRetryableErrorCount));
        }
        if (messageQueueInFlightCount != null) {
            columnRowKeyValuePairs.put("In Flight", String.valueOf(messageQueueInFlightCount));
        }
        if (messageQueueUnsentCount != null) {
            columnRowKeyValuePairs.put("Unsent", String.valueOf(messageQueueUnsentCount));
        }
        if (messageQueueBatchedCount != null) {
            columnRowKeyValuePairs.put("Batched", String.valueOf(messageQueueBatchedCount));
        }
        if (messageQueueAwaitingRetryCount != null) {
            columnRowKeyValuePairs.put("Awaiting Retry", String.valueOf(messageQueueAwaitingRetryCount));
        }

        return tableUtils.getRowInTableByColumnsAndValues(table_MessageQueues, columnRowKeyValuePairs);
    }


    public WebElement getSafeOrderObjectTableRow(String safeOrderObjectName, Date sendTime, Integer messagesFailedCount, Integer messagesRetryableErrorCount, Integer messagesInFlightCount, Integer messagesUnsentCount, String messagesErrorMessage) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();

        if (safeOrderObjectName != null) {
            columnRowKeyValuePairs.put("Safe Order Object Name", safeOrderObjectName);
        }
        if (sendTime != null) {
            columnRowKeyValuePairs.put("Send Time", DateUtils.dateFormatAsString("MM/dd/yyyy", sendTime));
        }
        if (messagesFailedCount != null) {
            columnRowKeyValuePairs.put("Failed", String.valueOf(messagesFailedCount));
        }
        if (messagesRetryableErrorCount != null) {
            columnRowKeyValuePairs.put("Retryable Error", String.valueOf(messagesRetryableErrorCount));
        }
        if (messagesInFlightCount != null) {
            columnRowKeyValuePairs.put("In Flight", String.valueOf(messagesInFlightCount));
        }
        if (messagesUnsentCount != null) {
            columnRowKeyValuePairs.put("Unsent", String.valueOf(messagesUnsentCount));
        }
        if (messagesErrorMessage != null) {
            columnRowKeyValuePairs.put("Error Message", messagesErrorMessage);
        }

        return tableUtils.getRowInTableByColumnsAndValues(table_SafeOrderObjectTable, columnRowKeyValuePairs);
    }


    public WebElement getNonSafeOrderedMessagesTableRow(Date sendTime, String nonSafeOrderedMessagesEventName, MessageQueue nonSafeOrderedMessagesDestination, Integer nonSafeOrderedMessagesOrder, String nonSafeOrderedMessagesSenderReference, String nonSafeOrderedMessagesStatus, Integer nonSafeOrderedMessagesRetries, String nonSafeOrderedMessagesResponse, String nonSafeOrderedMessagesDescription) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();

        if (sendTime != null) {
            columnRowKeyValuePairs.put("Send Time", DateUtils.dateFormatAsString("MM/dd/yyyy", sendTime));
        }
        if (nonSafeOrderedMessagesEventName != null) {
            columnRowKeyValuePairs.put("Event Name", nonSafeOrderedMessagesEventName);
        }
        if (nonSafeOrderedMessagesDestination != null) {
            columnRowKeyValuePairs.put("Destination", nonSafeOrderedMessagesDestination.getValue());
        }
        if (nonSafeOrderedMessagesOrder != null) {
            columnRowKeyValuePairs.put("Order", String.valueOf(nonSafeOrderedMessagesOrder));
        }
        if (nonSafeOrderedMessagesSenderReference != null) {
            columnRowKeyValuePairs.put("Sender Reference", nonSafeOrderedMessagesSenderReference);
        }
        if (nonSafeOrderedMessagesStatus != null) {
            columnRowKeyValuePairs.put("Status", nonSafeOrderedMessagesStatus);
        }
        if (nonSafeOrderedMessagesRetries != null) {
            columnRowKeyValuePairs.put("Retries", String.valueOf(nonSafeOrderedMessagesRetries));
        }
        if (nonSafeOrderedMessagesResponse != null) {
            columnRowKeyValuePairs.put("Response", nonSafeOrderedMessagesResponse);
        }
        if (nonSafeOrderedMessagesDescription != null) {
            columnRowKeyValuePairs.put("Description", nonSafeOrderedMessagesDescription);
        }

        return tableUtils.getRowInTableByColumnsAndValues(table_NonSafeOrderedMessagesTable, columnRowKeyValuePairs);
    }


    public void returnTo() {
        link_ReturnToMessagesList.click();
    }


    public List<WebElement> getMessageList() {
        return table_MessageQueues.findElements(By.xpath(".//tr/child::td/div/a[contains(@id, ':MessageName')]"));
    }


    public int getMessageQueueColumnCount(MessageQueue messageQueue, String columnName) {
        String columnCountGridColumnID = tableUtils.getGridColumnFromTable(table_MessageQueues, columnName);
        int columnCount = Integer.valueOf(getMessageQueuesTableRow(messageQueue, null, null, null, null, null, null, null, null).findElement(By.xpath(".//parent::tr/td[contains(@class,'" + columnCountGridColumnID + "')]/div")).getText());
        return columnCount;
    }


    public void checkMessageQueueCheckbox(MessageQueue messageQueue) {
        int messageQueueRowNumber = Integer.valueOf(getMessageQueuesTableRow(messageQueue, null, null, null, null, null, null, null, null).findElement(By.xpath(".//parent::td/parent::tr")).getAttribute("data-recordindex")) + 1;
        tableUtils.setCheckboxInTable(table_MessageQueues, messageQueueRowNumber, true);
    }


    public void clickSafeOrderObjectLink(String safeOrderObjectName) {
        tableUtils.clickLinkInTable(table_SafeOrderObjectTable, safeOrderObjectName);
    }


    public void clickSafeOrderObjectLink(MessageQueuesSafeOrderObjectLinkOptions safeOrderObjectLinkOption) {
        clickSafeOrderObjectLink(safeOrderObjectLinkOption.getValue());
    }


    public void clickSuspend() {
        clickWhenClickable(button_SuspendMessageQueue);
    }


    public void clickResume() {
        clickWhenClickable(button_ResumeMessageQueue);
    }


    public void clickRestart() {
        clickWhenClickable(button_RestartMessageQueue);
    }


    public void clickRestartMessagingEngine() {
        clickWhenClickable(button_RestartMessagingEngine);
    }


    public void clickResyncMessage() {
        clickWhenClickable(button_ResyncMessage);
    }


    public void clickRetryMessage() {
        clickWhenClickable(button_RetryMessage);
    }


    public void clickSkipMessage() {
        clickWhenClickable(button_SkipMessage);
    }


    public void skipEventMessage() {
        clickSkipMessage();
        
        selectOKOrCancelFromPopup(OkCancel.OK);
        
    }


    public String getMessagePayload() {
        return text_MessagePayload.getAttribute("value");
    }


    public String getMessageBody() {
        String body = "";
        String payload = text_MessagePayload.getText();
        final Pattern pattern = Pattern.compile("<body>(.+?)</body>");
        final Matcher matcher = pattern.matcher(payload);
        matcher.find();
        body = matcher.group(1);
        System.out.println(body);
        return body;
    }


    public void suspendQueue(MessageQueue messageQueue) throws GuidewireException {
        if (checkMessageQueueStatus(messageQueue, MessageQueueStatus.Started)) {
            checkMessageQueueCheckbox(messageQueue);
            
            clickSuspend();
            
        } else {
            Assert.fail("The message queue " + messageQueue.getValue() + "was already in a Suspended status.");
        }
    }


    public void resumeQueue(MessageQueue messageQueue) throws GuidewireException {
        if (checkMessageQueueStatus(messageQueue, MessageQueueStatus.Suspended)) {
            checkMessageQueueCheckbox(messageQueue);
            clickResume();
        } else {
            Assert.fail("The message queue " + messageQueue.getValue() + "was already in a Started status.");
        }
    }


    public boolean checkMessageQueueStatus(MessageQueue messageQueue, MessageQueueStatus messageQueueStatus) {
        boolean queueInStatus = false;
        try {
            getMessageQueuesTableRow(messageQueue, null, messageQueueStatus, null, null, null, null, null, null);
            queueInStatus = true;
        } catch (Exception e) {
            queueInStatus = false;
        }
        return queueInStatus;
    }

    public int getQueueCount(MessageQueue queue, String columnName) {
    	waitForPostBack();
    	TableUtils tableUtils = new TableUtils(driver);
    	String stringValue = tableUtils.getCellTextInTableByRowAndColumnName(table_MessageQueues, tableUtils.getRowInTableByColumnNameAndValue(table_MessageQueues, "Destination", queue.getValue()), columnName);
    	return Integer.parseInt(stringValue);
    }

    public void clickMessageQueue(MessageQueue messageQueue) {
    	waitForPostBack();
        clickWhenClickable(queueLink(messageQueue));
    }


    public boolean compareEmail(String messageText) throws Exception {
        try {
            clickSafeOrderObjectLink(MessageQueuesSafeOrderObjectLinkOptions.Outbound_Email);
            selectSafeOrderObjectFilterOption(MessageQueuesSafeOrderObjectSelectOptions.Safe_Order_Object_With_Any_Unfinished_Messages);
        } catch (Exception e) {
        }
        

        try {
            // go into the non-safe messages
            clickSafeOrderObjectLink(MessageQueuesSafeOrderObjectLinkOptions.Non_Safe_Ordered_Messages);
        } catch (Exception e) {
            throw new Exception("There are no non-safe messages.");
        }
        boolean isAMatch = false;
        // open the messages
        String messageBody = "";
        String destination = "Outbound E-mail";
        List<WebElement> destinations;
        WebElement link;
        int counter = 0;
        do {
            destinations = finds(By.xpath("//a[contains(text(), '" + destination + "')]"));
            if (destinations.isEmpty() || destinations.size() < 1) {
                // throw exception: Email not found
                throw new Exception("There were no emails with the destination of " + destination);
            }
            link = destinations.get(counter);
            link.click();
            messageBody = getMessageBody();
            isAMatch = messageBody.matches(messageText);
            if (isAMatch) {
                return isAMatch;
            }
            // go back
            returnTo();
        } while (counter++ < destinations.size());

        return isAMatch;
    }


    public void selectSafeOrderObjectFilterOption(MessageQueuesSafeOrderObjectSelectOptions safeOrderObjectSelectOption) {
        Guidewire8Select filter = select_Filter();
        filter.selectByVisibleText(safeOrderObjectSelectOption.getValue());
    }


    public void selectMessageFilterOption(MessageQueuesFilterSelectOptions filterSelection) {
        
        Guidewire8Select filter = select_Filter();
        filter.selectByVisibleTextPartial(filterSelection.getValue());
        
    }


    public void clickAccountNumberLink(String accountNumber) {
        clickSafeOrderObjectLink(accountNumber);
    }


    public List<WebElement> getDestinationList() {
        return getMessageList();
    }

    @FindBy(xpath = "//div[@id='MessagingDestinationControlList:MessagingDestinationControlListScreen:MessagingDestinationsControlLV']/div[contains(@id, 'headercontainer-')]/div[contains(@id, 'headercontainer-')]/div[contains(@id, 'headercontainer-')]")
    public WebElement table_EventMessagesHeader;

    @FindBy(xpath = "//div[@id='MessagingDestinationControlList:MessagingDestinationControlListScreen:MessagingDestinationsControlLV']/div[@id='MessagingDestinationControlList:MessagingDestinationControlListScreen:MessagingDestinationsControlLV-body']/div/table")
    public WebElement table_EventMessages;

    @FindBy(xpath = "//div[contains(@id,'MessagingDestinationControlList')]//tbody/tr[3]/td//tr[contains(.,'Email')]")
    public WebElement table_EmailRow;

    private List<String> getTableHeadersInOrder() {
        List<WebElement> spans = table_EventMessagesHeader.findElements(By.xpath(".//div[contains(@class, 'column-header')]/div/span[contains(@id, '-textEl')]"));

        List<String> headers = new ArrayList<String>();

        for (WebElement span : spans) {
            headers.add(span.getText());
        }

        return headers;
    }

    private List<WebElement> getTableRows() {
        return table_EventMessages.findElements(By.xpath(".//tbody/tr"));
    }


    public Date writeRowValuesToDatabase() throws Exception {
        List<WebElement> tableRows = getTableRows();
        List<String> tableHeaders = getTableHeadersInOrder();
        Date nowDateTime = new Date();
        String server = new GuidewireHelpers(getDriver()).getEnvironment();
        String center = new GuidewireHelpers(getDriver()).getCurrentCenter().getValue();

        int destinationNameColNum = 0;
        String search = "Destination";
        for (int i = 0; i < tableHeaders.size(); i++) {
            if (tableHeaders.get(i).trim().equals(search)) {
                destinationNameColNum = i + 1;
            }
        }
        WebElement linkDestinationName;
        String destinationName;

        int destinationStatusColNum = 0;
        search = "Status";
        for (int i = 0; i < tableHeaders.size(); i++) {
            if (tableHeaders.get(i).trim().equals(search)) {
                destinationStatusColNum = i + 1;
            }
        }
        WebElement spanDestinationStatus;
        String destinationStatus;

        int failedColNum = 0;
        search = "Failed";
        for (int i = 0; i < tableHeaders.size(); i++) {
            if (tableHeaders.get(i).trim().equals(search)) {
                failedColNum = i + 1;
            }
        }
        WebElement spanFailed;
        int numFailed = 0;

        int retryableErrorColNum = 0;
        search = "Retryable Error";
        for (int i = 0; i < tableHeaders.size(); i++) {
            if (tableHeaders.get(i).trim().equals(search)) {
                retryableErrorColNum = i + 1;
            }
        }
        WebElement spanRetryableError;
        int numRetryableError = 0;

        int inFlightColNum = 0;
        search = "In Flight";
        for (int i = 0; i < tableHeaders.size(); i++) {
            if (tableHeaders.get(i).trim().equals(search)) {
                inFlightColNum = i + 1;
            }
        }
        WebElement spanInFlight;
        int numInFlight = 0;

        int unsentColNum = 0;
        search = "Unsent";
        for (int i = 0; i < tableHeaders.size(); i++) {
            if (tableHeaders.get(i).trim().equals(search)) {
                unsentColNum = i + 1;
            }
        }
        WebElement spanUnsent;
        int numUnsent = 0;

        int batchedColNum = 0;
        search = "Batched";
        for (int i = 0; i < tableHeaders.size(); i++) {
            if (tableHeaders.get(i).trim().equals(search)) {
                batchedColNum = i + 1;
            }
        }
        WebElement spanBatched;
        int numBatched = 0;

        int awaitingRetryColNum = 0;
        search = "Awaiting Retry";
        for (int i = 0; i < tableHeaders.size(); i++) {
            if (tableHeaders.get(i).trim().equals(search)) {
                awaitingRetryColNum = i + 1;
            }
        }
        WebElement spanAwaitingRetry;
        int numAwaitingRetry = 0;

        for (WebElement row : tableRows) {
            linkDestinationName = row.findElement(By.xpath(".//td[" + destinationNameColNum + "]/div//a[contains(@id, ':DestinationName')]"));
            destinationName = linkDestinationName.getText();

            spanDestinationStatus = row.findElement(By.xpath(".//td[" + destinationStatusColNum + "]/div"));
            destinationStatus = spanDestinationStatus.getText();

            spanFailed = row.findElement(By.xpath(".//td[" + failedColNum + "]/div"));
            numFailed = Integer.valueOf(spanFailed.getText());

            spanRetryableError = row.findElement(By.xpath(".//td[" + retryableErrorColNum + "]/div"));
            numRetryableError = Integer.valueOf(spanRetryableError.getText());

            spanInFlight = row.findElement(By.xpath(".//td[" + inFlightColNum + "]/div"));
            numInFlight = Integer.valueOf(spanInFlight.getText());

            spanUnsent = row.findElement(By.xpath(".//td[" + unsentColNum + "]/div"));
            numUnsent = Integer.valueOf(spanUnsent.getText());

            spanBatched = row.findElement(By.xpath(".//td[" + batchedColNum + "]/div"));
            numBatched = Integer.valueOf(spanBatched.getText());

            spanAwaitingRetry = row.findElement(By.xpath(".//td[" + awaitingRetryColNum + "]/div"));
            numAwaitingRetry = Integer.valueOf(spanAwaitingRetry.getText());

            EventMessagingHelper.createNewEventMessagingData(nowDateTime, server, center, destinationName, destinationStatus, numFailed, numRetryableError, numInFlight, numUnsent, numBatched, numAwaitingRetry);
        }

        return nowDateTime;
    }

    private List<EventMessaging> getValuesForGivenTimeStampAndFilterOutGoodOnes(Date checkTime) throws Exception {
        List<EventMessaging> myRows = EventMessagingHelper.getEventMessagingByDateTimeStamp(checkTime);
        List<EventMessaging> myRowsToEmail = new ArrayList<EventMessaging>();

        String destStatus = "";
        int failedNum = 0;
        int retryErrNum = 0;
        int inFlightNum = 0;
        int destinationNum = 0;
        int batchedNum = 0;
        int awaitingRetryNum = 0;

        for (EventMessaging row : myRows) {
            destStatus = row.getDestinationStatus();
            failedNum = row.getDestinationFailedNum();
            retryErrNum = row.getDestinationRetryErrNum();
            inFlightNum = row.getDestinationInFlightNum();
            destinationNum = row.getDestinationUnsentNum();
            batchedNum = row.getDestinationBatchedNum();
            awaitingRetryNum = row.getDestinationAwaitingRetryNum();

            if (!destStatus.equals("Started") || failedNum > 0 || retryErrNum > 0 || inFlightNum > 0
                    || destinationNum > 0 || batchedNum > 0 || awaitingRetryNum > 0) {
                myRowsToEmail.add(row);
            }
        }

        return myRowsToEmail;
    }


    public String getEventMessagingEmailTableFromBadRows(Date checkTime) throws Exception {
        List<EventMessaging> rowsToEmail = getValuesForGivenTimeStampAndFilterOutGoodOnes(checkTime);
        String allBody = "<br/>" + "<table style=\"width:100%; border:1px solid black\">" + "<tr>"
                + "<th style=\"border:1px solid black\">Center</th>"
                + "<th style=\"border:1px solid black\">Destination Name</th>"
                + "<th style=\"border:1px solid black\">Status</th>"
                + "<th style=\"border:1px solid black\">Failed</th>"
                + "<th style=\"border:1px solid black\">Retryable Errors</th>"
                + "<th style=\"border:1px solid black\">In Flight</th>"
                + "<th style=\"border:1px solid black\">Unsent</th>"
                + "<th style=\"border:1px solid black\">Batched</th>"
                + "<th style=\"border:1px solid black\">Awaiting Retry</th>" + "</tr>";

        for (EventMessaging row : rowsToEmail) {
            allBody = allBody + "<tr>" + "<td style=\"border:1px solid black\">" + row.getCenter() + "</td>"
                    + "<td style=\"border:1px solid black\">" + row.getDestinationName() + "</td>"
                    + "<td style=\"border:1px solid black\">" + row.getDestinationStatus() + "</td>"
                    + "<td style=\"border:1px solid black\">" + row.getDestinationFailedNum() + "</td>"
                    + "<td style=\"border:1px solid black\">" + row.getDestinationRetryErrNum() + "</td>"
                    + "<td style=\"border:1px solid black\">" + row.getDestinationInFlightNum() + "</td>"
                    + "<td style=\"border:1px solid black\">" + row.getDestinationUnsentNum() + "</td>"
                    + "<td style=\"border:1px solid black\">" + row.getDestinationBatchedNum() + "</td>"
                    + "<td style=\"border:1px solid black\">" + row.getDestinationAwaitingRetryNum() + "</td>"
                    + "</tr>";
        }

        allBody = allBody + "</table><br/>";

        return allBody;

    }

    public void clickContact(String name) {
        List<WebElement> nameLinks = new ArrayList<WebElement>();
        if (tableUtils.hasMultiplePages(table_MessageQueues)) {
            for (int i = 1; i <= tableUtils.getNumberOfTablePages(table_MessageQueues); i++) {
                tableUtils.setTablePageNumber(table_MessageQueues, i);
                nameLinks = table_MessageQueues.findElements(By.xpath(".//table//tr/td/div/a[contains(., '" + name + "')]"));
                if (nameLinks.size() > 0) {
                    break;
                }
            }
            if (nameLinks.size() <= 0) {
                System.out.println("Unable to find name in table.");
            } else {
                nameLinks.get(0).click();
            }
        } else {
            nameLinks = table_MessageQueues.findElements(By.xpath(".//table//tr/td/div/a[contains(., '" + name + "')]"));
            nameLinks.get(0).click();
        }
    }

    public int unsentCount(String contactName) {
        clickMessageQueue(MessageQueue.PolicyCenterContactBroadcast);
        selectMessageFilterOption(MessageQueuesFilterSelectOptions.Unfinished_Messages);
        
        waitUntilElementIsVisible(table_SafeOrderObjectTable);
        int row = tableUtils.getRowNumberInTableByText(table_SafeOrderObjectTable, contactName);
        return Integer.parseInt(tableUtils.getCellTextInTableByRowAndColumnName(table_SafeOrderObjectTable, row, "Unsent"));
    }

    /*
     * private List<EventMessaging> getEmailUnsentCount() { WebElement myRows = table_EmailRow; List<EventMessaging> myRowsToEmail = new ArrayList<EventMessaging>(); String destStatus = ""; // int failedNum = 0; // int retryErrNum = 0; // int inFlightNum = 0; int destinationNum = 0; // int batchedNum = 0; // int awaitingRetryNum = 0; if(table_EmailRow) { // destStatus = row.getDestinationStatus(); // failedNum = row.getDestinationFailedNum(); // retryErrNum = row.getDestinationRetryErrNum(); // inFlightNum = row.getDestinationInFlightNum(); destinationNum = row.getDestinationUnsentNum(); // batchedNum = row.getDestinationBatchedNum(); // awaitingRetryNum = row.getDestinationAwaitingRetryNum(); if((!destStatus.equals("Started")) || ((failedNum > 0) || (retryErrNum > 0) || (inFlightNum > 0) || (destinationNum > 0) || (batchedNum > 0) || (awaitingRetryNum > 0))) { myRowsToEmail.add(row); } } return myRowsToEmail; }
     */
    
    public ArrayList<String> getErrorMessageColumn(MessageQueue queue){
    	ArrayList<String> errors = new ArrayList<>();
    	clickMessageQueue(queue);
    	waitUntilElementIsVisible(table_SafeOrderObjectTable);
    	selectMessageFilterOption(MessageQueuesFilterSelectOptions.Unfinished_Messages);
    	TableUtils tableHelp = new TableUtils(driver);
    	if(tableUtils.hasMultiplePages(table_SafeOrderObjectTable)) {
    		 for (int i = 1; i <= tableUtils.getNumberOfTablePages(table_SafeOrderObjectTable); i++) {
                 tableUtils.setTablePageNumber(table_SafeOrderObjectTable, i);
                 ArrayList<String> errorMessages = tableUtils.getAllCellTextFromSpecificColumn(table_SafeOrderObjectTable, "Error Message");
                 errors.addAll(errorMessages);
                 
    		 }
    		 link_UpToMessageQueues.click();
    	} else {
    		errors.addAll(tableHelp.getAllCellTextFromSpecificColumn(table_SafeOrderObjectTable, "Error Message"));
    		link_UpToMessageQueues.click();
    	}
    	return errors;
    }
    
    public ArrayList<String> getNonSafeOrderedMessagesPayload() {
    	ArrayList<String> messagesToReturn = new ArrayList<>();
    	List<WebElement> messages = table_NonSafeOrderedMessagesTable.findElements(By.xpath(".//a[contains(@id, 'MessageName')]"));
    	for(int i = 0; i < messages.size(); i++) {
    		waitForPageLoad();
    		messages = table_NonSafeOrderedMessagesTable.findElements(By.xpath(".//a[contains(@id, 'MessageName')]"));
    		messages.get(i).click();
    		waitForPageLoad();
    		messagesToReturn.add(text_MessagePayload.getText());
    		clickWhenClickable(link_ReturnToMessagesList);
    	}
    	
    	return messagesToReturn;
    }
}

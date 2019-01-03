package repository.bc.common;

import com.idfbins.enums.OkCancel;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.bc.account.BCAccountMenu;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.DocumentStatus;
import repository.gw.enums.DocumentType;
import repository.gw.enums.SearchBy;
import repository.gw.enums.SearchSince;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BCCommonDocuments extends BasePage {

    private TableUtils tableUtils;
    private WebDriver driver;

    public BCCommonDocuments(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    //////////////////////////////////////////
    // "Recorded Elements" and their XPaths //
    //////////////////////////////////////////

    @FindBy(xpath = "//input[contains(@id,':DateSearchCriteriaRangeChoice_Choice-inputEl')]")
    public WebElement radio_Since;

    @FindBy(xpath = "//input[contains(@id,':DateSearchCriteriaDirectChoice_Choice-inputEl')]")
    public WebElement radio_FromTo;

    @FindBy(xpath = "//input[contains(@id,':DocumentSearchDV:DateSearchCriteria:DateSearchCriteriaStartDate-inputEl')]")
    public WebElement editbox_From;

    @FindBy(xpath = "//input[contains(@id,':DocumentSearchDV:DateSearchCriteria:DateSearchCriteriaEndDate-inputEl')]")
    public WebElement editbox_To;

    @FindBy(xpath = "//div[contains (@id,'AccountDetailDocuments:AccountDetailDocumentsScreen:DocumentsLV') or contains(@id,'PolicyDetailDocuments:PolicyDetailDocumentsScreen:DocumentsLV') or contains(@id,'PolicyDetailDocuments:PolicyDetailDocumentsScreen:PolicyDocumentsLV') or contains(@id, ':Policy_DocumentsScreen:DocumentsLV')]")
    public WebElement table_DocumentTable;

    @FindBy(xpath = "//div[contains (@id,'DocumentDetailsPopup:DocumentDetailsScreen:DocumentDetailsDV:DocumentMetadataInputSet:RelatedTo-inputEl')]")
    public WebElement text_Document_DocumentDetailsRelatedTo;

    public Guidewire8Select comboBox_DocumentsSearchBy() {
        return new Guidewire8Select(driver, "//table[contains(@id,':DocumentSearchDV:DateSearchCriteria:DateSearchCriteriaChosenOption-triggerWrap')]");
    }

    public Guidewire8Select comboBox_DocumentsSearchBySinceCombobox() {
        return new Guidewire8Select(driver, "//table[contains(@id,':DateSearchCriteria:DateSearchCriteriaRangeValue-triggerWrap')]");
    }

    ///////////////////////////////////////
    // Helper Methods for Above Elements //
    ///////////////////////////////////////

    public WebElement getDocumentsTableRow(String documentName, DocumentType docType, DocumentStatus status, String author, Date createDate, Date dateModified) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        if (documentName != null) {
            columnRowKeyValuePairs.put("Name", documentName);
        }
        if (docType != null) {
            columnRowKeyValuePairs.put("Type", docType.getValue());
        }
        if (status != null) {
            columnRowKeyValuePairs.put("Status", status.getValue());
        }
        if (author != null) {
            columnRowKeyValuePairs.put("Author", author);
        }
        if (createDate != null) {
            columnRowKeyValuePairs.put("Date Created", DateUtils.dateFormatAsString("MM/dd/yyyy", createDate));
        }
        if (dateModified != null) {
            columnRowKeyValuePairs.put("Date Modified", DateUtils.dateFormatAsString("MM/dd/yyyy", dateModified));
        }
        try {
            WebElement tableRow = tableUtils.getRowInTableByColumnsAndValues(table_DocumentTable, columnRowKeyValuePairs);
            return tableRow;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean waitUntilDocumentStatusChanges(int secondsToWait, String documentName, DocumentType docType, DocumentStatus statusExpected, String author, Date createDate, Date dateModified) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        if (!(documentName == null)) {
            columnRowKeyValuePairs.put("Name", documentName);
        }
        if (!(docType == null)) {
            columnRowKeyValuePairs.put("Type", docType.getValue());
        }
        if (!(statusExpected == null)) {
            columnRowKeyValuePairs.put("Status", statusExpected.getValue());
        }
        if (!(author == null)) {
            columnRowKeyValuePairs.put("Author", author);
        }
        if (!(createDate == null)) {
            columnRowKeyValuePairs.put("Date Created", DateUtils.dateFormatAsString("MM/dd/yyyy", createDate));
        }
        if (!(dateModified == null)) {
            columnRowKeyValuePairs.put("Date Modified", DateUtils.dateFormatAsString("MM/dd/yyyy", dateModified));
        }
        List<WebElement> tableRows = tableUtils.getRowsInTableByColumnsAndValues(table_DocumentTable, columnRowKeyValuePairs);
        boolean found = false;
        long secondsRemaining = secondsToWait;
        int delayInterval = 5;
        while (!found && (secondsRemaining > 0)) {
            if (tableRows.size() > 0) {
                found = true;
            } else {
                sleep(delayInterval); //Used to wait for the amount of time specified in the delayInterval variable before attempting to check for the document status to change again.
                secondsRemaining -= delayInterval;
                repository.bc.account.BCAccountMenu acctMenuStuff = new BCAccountMenu(getDriver());
                acctMenuStuff.clickAccountMenuInvoices();
                acctMenuStuff.clickBCMenuDocuments();
                tableRows = tableUtils.getRowsInTableByColumnsAndValues(table_DocumentTable, columnRowKeyValuePairs);
            }
        }
        return found;
    }

    public int getDocumentTableRowCount() {
        return tableUtils.getRowCount(table_DocumentTable);
    }

    public List<WebElement> getSearchResults() {
        return table_DocumentTable.findElements(By.xpath(".//tbody/child::tr"));
    }

    public void selectDocumentStatus(DocumentStatus status) {
        Guidewire8Select mySelect = new Guidewire8Select(driver, "//table[contains(@id,'DocumentSearchDV:StatusCriterion-triggerWrap')]");
        mySelect.selectByVisibleText(status.getValue());
    }

    public void selectDocumentType(DocumentType type) {
        Guidewire8Select mySelect = new Guidewire8Select(driver, "//table[contains(@id,':PolicyDetailDocumentsScreen:PolicyDocumentSearchDV:TypeCriterion-triggerWrap') or contains(@id, ':Policy_DocumentSearchDV:DocumentType-triggerWrap')]");
        mySelect.selectByVisibleText(type.getValue());
    }

    public void setDocumentsFromDate(Date date) {
        waitUntilElementIsVisible(editbox_From);
        editbox_From.sendKeys(Keys.CONTROL + "a");
        editbox_From.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", date));
    }

    public void setDocumentsToDate(Date date) {
        waitUntilElementIsVisible(editbox_To);
        editbox_To.sendKeys(Keys.CONTROL + "a");
        editbox_To.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", date));
    }

    public void selectSearchBy(SearchBy searchby) {
        Guidewire8Select mySelect = comboBox_DocumentsSearchBy();
        mySelect.selectByVisibleText(searchby.getValue());
    }

    public void selectSinceDate(SearchSince since) {
        Guidewire8Select mySelect = comboBox_DocumentsSearchBySinceCombobox();
        mySelect.selectByVisibleText(since.getValue());
    }

    public void clickRadioSince() {
        radio_Since.click();
    }

    public void clickRadioFromTo() {
        radio_FromTo.click();
    }

    public String getDocumentNameByRow(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_DocumentTable, row, "Name");
    }

    public String getDocumentDescriptionByRow(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_DocumentTable, row, "Description");
    }

    public boolean verifyDocument(String documentName, DocumentType docType, DocumentStatus status, String author, Date createDate, Date dateModified) {
        WebElement tableRow = getDocumentsTableRow(documentName, docType, status, author, createDate, dateModified);
        return tableRow != null;
    }

    public boolean verifyDocument(Date date, String documentName) {
        return verifyDocument(documentName, null, null, null, date, null);
    }

    public void deleteDocument(Date date, String documentName) {
        tableUtils.clickLinkInTableByRowAndColumnName(table_DocumentTable, tableUtils.getRowNumberFromWebElementRow(getDocumentsTableRow(documentName, null, null, null, date, null)), "Delete");
        selectOKOrCancelFromPopup(OkCancel.OK);
    }

    public String clickViewDocumentInTable(String documentName, DocumentType docType, DocumentStatus status, String author, Date createDate, Date dateModified) {
        tableUtils.clickLinkInTableByRowAndColumnName(table_DocumentTable, tableUtils.getRowNumberFromWebElementRow(getDocumentsTableRow(documentName, docType, status, author, createDate, dateModified)), "Actions");
        WebDriver driver = getDriver();
        String mainBCWindow = driver.getWindowHandle();
        for (String windowHandle : driver.getWindowHandles()) {
            driver.switchTo().window(windowHandle);
            if (driver.getTitle().contains("Guidewire BillingCenter")) {
                systemOut("Switching to Billing Center Window");
                break;
            }
        }
        return mainBCWindow;
    }

    public String getTextInRelatedToField() {
        return text_Document_DocumentDetailsRelatedTo.getText();
    }



    public void clickDocumentNameInTable(String documentName, DocumentType docType, DocumentStatus status, String author, Date createDate, Date dateModified) {
        tableUtils.clickLinkInTableByRowAndColumnName(table_DocumentTable, tableUtils.getRowNumberFromWebElementRow(getDocumentsTableRow(documentName, docType, status, author, createDate, dateModified)), "Name");
    }

    public boolean isDocumentPresentInImageRight() {
        List<WebElement> IRErrorMessage = finds(By.xpath("//*[contains(., 'There was a problem fetching the document from ImageRight. Please contact the Help Desk at extension 4350.')]"));
        return IRErrorMessage.isEmpty();
    }

    @FindBy(xpath = "//a[contains(@id,':SearchLinksNotesInputSet:ViewPCNotes')]")
    public WebElement button_ViewInPolicyCenter;

    public String clickViewInPolicyCenterButton() {
        clickWhenClickable(button_ViewInPolicyCenter, 1000);
        
        return new GuidewireHelpers(driver).switcWebDriverWindow("Guidewire PolicyCenter (Service Account)");
    }
}

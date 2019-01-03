package repository.pc.policy;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.exception.GuidewireException;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PolicyDocuments extends BasePage {

    private WebDriver driver;
    private TableUtils tableUtils;

    public PolicyDocuments(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
    }

    @FindBy(xpath = "//input[contains(@id, ':Policy_DocumentSearchDV:Name-inputEl')]")
    private WebElement editbox_DocumentName;

    @FindBy(xpath = "//input[contains(@id, ':Policy_DocumentSearchDV:Description-inputEl')]")
    private WebElement editbox_DocumentDescription;

    public Guidewire8Select select_RelatedTo() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':Policy_DocumentSearchDV:RelatedTo-triggerWrap')]");
    }

    public Guidewire8Select select_DocumentType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':DocumentType-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id, ':Policy_DocumentSearchDV:DateFrom-inputEl')]")
    private WebElement editbox_DateFrom;

    @FindBy(xpath = "//input[contains(@id, ':Policy_DocumentSearchDV:DateTo-inputEl')]")
    private WebElement editbox_DateTo;

    @FindBy(xpath = "//input[contains(@id, ':Policy_DocumentSearchDV:Author-inputEl')]")
    private WebElement editbox_Author;

    @FindBy(xpath = "//input[contains(@id, ':Policy_DocumentSearchDV:IncludeObsoletes_true-inputEl')]")
    private WebElement radio_IncludeHiddenYes;

    @FindBy(xpath = "//input[contains(@id, ':Policy_DocumentSearchDV:IncludeObsoletes_false-inputEl')]")
    private WebElement radio_IncludeHiddenNo;

    @FindBy(xpath = "//div[contains(@id,'PolicyFile_Documents:Policy_DocumentsScreen:DocumentsLV-body')]/parent::div")
    public WebElement table_DocumentsResultsTable;

    @FindBy(xpath = "//a[contains(@id,':DocumentsLV_PreViewLink')]")
    private WebElement button_Preview;

    @FindBy(xpath = "//a[contains(@id,'DocumentsLV_ViewLink')]")
    private WebElement button_View;

    @FindBy(xpath = "//div[contains(@id,'PolicyFile_Documents:Policy_DocumentsScreen:_msgs')]/div[contains(text(),'You do not have the permission required to perform this action:')]")
    private List<WebElement> error_UserHaveNoPermission;

    @FindBy(xpath = "//span[contains(@id, 'DocumentDetailsPopup:DocumentDetailsScreen:ttlBar')]")
    public WebElement text_DocumentDetails;

    @FindBy(xpath = "//*[contains(@id, 'DocumentDetailsPopup:DocumentDetailsScreen:Edit-btnEl')]")
    public WebElement button_DocumentDetailsEdit;

    @FindBy(xpath = "//a[contains(., 'Return to Documents') and contains(@id, 'DocumentDetailsPopup')]")
    public WebElement link_ReturnToDocuments;

    @FindBy(xpath = "//div[contains(@id,'fieldcontainer')]//a[contains(@id,'SearchLinksInputSet:Search') or contains(@id,'SearchLinksDocumentsInputSet:Search')]")
    private WebElement button_Search;

    @FindBy(xpath = "//a[contains(@id,':SearchLinksInputSet:Reset') or contains(@id, ':SearchLinksDocumentsInputSet:Reset')]")
    private WebElement button_Reset;

    @FindBy(xpath = "//div[contains(@id,':Policy_DocumentsScreen:DocumentsLV')]")
    private WebElement table_Documents;

    public WebElement getTableDocuments() {
        return table_Documents;
    }


    public WebElement returnDocumentsTable() {
        waitUntilElementIsVisible(table_DocumentsResultsTable);
        return table_DocumentsResultsTable;
    }


    public void setDocumentName(String documentName) {
        waitUntilElementIsClickable(editbox_DocumentName);
        editbox_DocumentName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_DocumentName.sendKeys(documentName);
    }


    public void setDocumentDescription(String documentDesc) {
        waitUntilElementIsClickable(editbox_DocumentDescription);
        editbox_DocumentDescription.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_DocumentDescription.sendKeys(documentDesc);
    }


    public void selectRelatedTo(String... relatedTo) {
        select_RelatedTo().selectByVisibleTextPartial(relatedTo);
    }


    public void selectDocumentType(String documentType) {
        select_DocumentType().selectByVisibleText(documentType);
    }


    public void setDateRangeFrom(Date dateFrom) {
        waitUntilElementIsClickable(editbox_DateFrom);
        editbox_DateFrom.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_DateFrom.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", dateFrom));
    }


    public void setDateRangeTo(Date dateTo) {
        waitUntilElementIsClickable(editbox_DateTo);
        editbox_DateTo.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_DateTo.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", dateTo));
    }


    public void setAuthor(String author) {
        waitUntilElementIsClickable(editbox_Author);
        editbox_Author.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_Author.sendKeys(author);
    }


    public void setRadioIncludeHiddenDocs(boolean yesNo) {
        if (yesNo) {
            waitUntilElementIsClickable(radio_IncludeHiddenYes);
            radio_IncludeHiddenYes.click();
        } else {
            waitUntilElementIsClickable(radio_IncludeHiddenNo);
            radio_IncludeHiddenNo.click();
        }
    }


    public void verifyDocumentPreviewButton() throws GuidewireException {
        if (!finds(By.xpath("//a[contains(@id,':DocumentsLV_PreViewLink')]")).isEmpty()) {
            clickWhenClickable(button_Preview);
            if (error_UserHaveNoPermission.size() > 0) {
                Assert.fail("User unable to click Preview on Documents page.");
            }
        } else if (!finds(By.xpath("//a[contains(@id,'DocumentsLV_ViewLink')]")).isEmpty()) {
            clickWhenClickable(button_View);
            if (error_UserHaveNoPermission.size() > 0) {
                Assert.fail("User unable to click View on Documents page.");
            }
        }
    }

    public boolean clickPreviewButton(String documentName) {
        List<WebElement> docuemntToPreview = finds(By.xpath("//a[text()='" + documentName + "']/parent::div/parent::td/following-sibling::td/div/a"));
        if (!docuemntToPreview.isEmpty()) {
            clickWhenClickable(docuemntToPreview.get(0));
            return true;
        } else {
            return false;
        }
    }


    public String getDocumentDetailsPageTitle() {
        waitUntilElementIsVisible(text_DocumentDetails);
        return text_DocumentDetails.getText();

    }


    public boolean clickDocumentDetailsEdit() {
        boolean exists = checkIfElementExists(button_DocumentDetailsEdit, 3000);
        if (exists) {
            clickWhenClickable(button_DocumentDetailsEdit);
            return true;
        } else {
            return false;
        }
    }


    public void clickReturnToDocuments() {
        clickWhenClickable(link_ReturnToDocuments);
    }


    public void clickSearch() {
        clickWhenClickable(button_Search);
        waitForPostBack();
    }


    public void clickReset() {
        clickWhenClickable(button_Reset);
    }


    public ArrayList<String> getDocumentsDescriptionsFromTable() {
        ArrayList<String> toReturn = tableUtils.getAllCellTextFromSpecificColumn(table_Documents, "Description");
        if (tableUtils.hasMultiplePages(table_Documents)) {
            boolean nextButtonExists = checkIfElementExists("//div[contains(@id, 'gpaging')]/a[contains(@data-qtip, 'Next Page') and contains(@style,'right: auto;')]", 1000);
            if (nextButtonExists) {
                tableUtils.clickNextPageButton(table_Documents);
                int numPages = tableUtils.getNumberOfTablePages(table_Documents);
                for (int page = 2; page <= numPages; page++) {
                    toReturn.addAll(tableUtils.getAllCellTextFromSpecificColumn(table_Documents, "Description"));
                    if (page < numPages) {
                        tableUtils.clickNextPageButton(table_Documents);
                    }
                }
            }
        }
        return toReturn;
    }


    public String getDateWillPrintColoumnDate(String rowName, String coloumName) {
        int row = tableUtils.getRowNumberInTableByText(table_Documents, rowName);
        return tableUtils.getCellTextInTableByRowAndColumnName(table_Documents, row, coloumName);
    }


    public ArrayList<String> getDocumentNameAddress() {
        return tableUtils.getAllCellTextFromSpecificColumn(table_Documents, "Name & Address");
    }

    public ArrayList<String> getDocumentDate() {
        return tableUtils.getAllCellTextFromSpecificColumn(table_Documents, "Date");
    }


    public int getDocumentCount(String document) {
        List<WebElement> documents = finds(By.xpath("//*[contains(., '" + document + "')]"));
        return documents.size();
    }

    public void sortByLatestGeneratedDocument() {
        tableUtils.sortByHeaderColumn(table_Documents, "Date");
        tableUtils.sortByHeaderColumn(table_Documents, "Date");
    }

    public boolean verifyDocumentByNameDescriptionDateGeneratedAndAuthor(String name, String description, Date date, String author) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        if (name != null) {
            columnRowKeyValuePairs.put("Name", name);
        }
        if (description != null) {
            columnRowKeyValuePairs.put("Description", description);
        }
        if (date != null) {
            columnRowKeyValuePairs.put("Date", DateUtils.dateFormatAsString("MM/dd/yyyy", date));
        }
        if (date != null) {
            columnRowKeyValuePairs.put("Author", author);
        }
        List<WebElement> tableRows = tableUtils.getRowsInTableByColumnsAndValues(table_Documents, columnRowKeyValuePairs);
        return tableRows.size() == 1;
    }


public String getOneColumnWithDocumentName(String DocumentName , String Column){
        return tableUtils.getColumnByRow(table_Documents , DocumentName ,Column );
}

}
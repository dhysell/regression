package repository.ab.contact;

import com.idfbins.enums.CountyIdaho;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.ab.search.AdvancedSearchAB;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.DateDifferenceOptions;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContactDetailsPaidDuesAB extends BasePage {

    private WebDriver driver;
    private TableUtils tableUtils;

    public ContactDetailsPaidDuesAB(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//input[contains(@id,'PaidCountyDuePopup:AccountNumber-inputEl')]")
    private WebElement input_ContactDetailsPaidDuesAccountNumber;

    @FindBy(xpath = "//*[@id='ContactDetail:ABContactDetailScreen:ContactBasicsCardTab-btnEl']")
    private WebElement link_ContactDetailsPaidDuesBasics;

    @FindBy(xpath = "//span[contains(@id, ':Edit-btnEl')]")
    private WebElement button_ContactDetailsPaidDuesEdit;

    @FindBy(xpath = "//*[@id='ContactDetail:ABContactDetailScreen:PaidDuesLV_tb:Edit-btnEl']")
    private WebElement tuple_ContactDetailsPaidDues;

    @FindBy(xpath = "//*[contains(@id, 'ContactDetail:ABContactDetailScreen:PaidDuesLV_tb:Update-btnEl') or contains(@id, 'Update-btnEl')]")
    private WebElement button_ContactDetailsPaidDuesUpdate;

    @FindBy(xpath = "//*[@id='ContactDetail:ABContactDetailScreen:PaidDuesLV_tb:Cancel-btnEl']")
    private WebElement button_ContactDetailsPaidDuesCancel;

    @FindBy(xpath = "//*[@id='ContactDetail:ABContactDetailScreen:PaidDuesLV_tb:Add-btnEl']")
    private WebElement button_ContactDetailsPaidDuesAdd;

    @FindBy(xpath = "//*[@id='ContactDetail:ABContactDetailScreen:PaidDuesLV_tb:Remove-btnEl']")
    private WebElement button_ContactDetailsPaidDuesRemove;

    @FindBy(xpath = "//*[contains(@id, ':ImportDuesButton-btnEl')]")
    private WebElement button_ContactDetailsPaidDuesImportDues;

    @FindBy(xpath = "//div[contains(., '10/31/20')]/ancestor::tr[1]/td/div[contains(., '11/01/20')]")
    private WebElement text_ContactDetailsPaidDuesEffectiveDate;

    @FindBy(xpath = "//div[contains(@id, 'ContactDetail:ABContactDetailScreen:PaidDuesLV') or contains(@id, 'PaidCountyDuePopup') or contains(@id, ':CountyDuesLV-body') or contains(@id, ':CountyDuesLV')]")
    private WebElement table_PaidDuesTable;

    @FindBy(xpath = "//div[contains(@class, 'message')]")
    private WebElement text_PaidDuesErrorMessage;

    @FindBy(xpath = "//span[contains(@id, 'PaidCountyDuePopup:Cancel')]")
    private WebElement text_PaidDuesCancel;

    @FindBy(xpath = "//span[contains(@id, ':SyncDuesButton-btnEl')]")
    private WebElement button_PaidDuesSync;

    // @FindBy(xpath =
    // "//*[@id='ContactDetail:ABContactDetailScreen:PaidDuesLV-body']/div/table/tbody/tr/td[7]")
    // public List<WebElement>
    // text_ContactDetailsPaidDuesRecentExpireDate;

    // *****************************************************************************************
    // Add Link Elements
    // *****************************************************************************************

    @FindBy(xpath = "//div[contains(@id, ':CountyDuesLV')]")
    private WebElement tableDiv_ContactDetailsPaidDuesCounty;

    @FindBy(xpath = "//*[@id='PaidCountyDuePopup:Update-btnEl']")
    private WebElement button_ContactDetailsPaidDuesAddOK;

    @FindBy(xpath = "//*[@id='']")
    private WebElement button_ContactDetailsPaidDuesAddCancel;

    private Guidewire8Select select_NewContactBasicsPaidDuesPolicyNumber() {
        return new Guidewire8Select(driver, "//table[@id='PaidCountyDuePopup:PolicyNumber-triggerWrap']");
    }

    @FindBy(xpath = "//input[contains(@id,'PaidCountyDuePopup:PolicyEffective')]")
    private WebElement editbox_ContactDetailsPaidDuesAddPolicyEffective;

    @FindBy(xpath = "//*[contains(@id,'PaidCountyDuePopup:AccountNumber:SelectAccountNumber')]")
    private WebElement link_ContactDetailsPaidDuesAddAccountFinder;

    @FindBy(xpath = "//*[@id='PaidCountyDuePopup:County:SelectCounty']")
    private WebElement link_ContactDetailsPaidDuesAddCountyFinder;

    @FindBy(xpath = "//*[contains(@id, 'PaidCountyDuePopup:_msgs')]/div")
    private List<WebElement> text_ContactDetailsPaidDuesError;

    // *****************************************************************************************
    // Import Address Elements
    // *****************************************************************************************

    private WebElement tuple_ContactDetailsPaidDues(String expireDate, String status) {
        return find(By.xpath("//div[contains(., '" + status
                + "')]/../preceding-sibling::td/div[contains(., '" + expireDate + "')]"));
    }

    // *****************************************************************************************
    // Helper Methods
    // *****************************************************************************************

    private void clickContactDetailsBasicsLink() {
        waitUntilElementIsClickable(link_ContactDetailsPaidDuesBasics);
        link_ContactDetailsPaidDuesBasics.click();
    }

    public void clickContactDetailsPaidDuesEditLink() {
        
        if (checkIfElementExists(button_ContactDetailsPaidDuesEdit, 1000)) {
            clickWhenClickable(button_ContactDetailsPaidDuesEdit);
        }
        
    }

    private void clickContactDetailsPaidDuesAdd() {
        clickWhenClickable(button_ContactDetailsPaidDuesAdd);
    }

    public void clickContactDetailsPaidDuesImportDuesButton() {
        
        clickWhenClickable(button_ContactDetailsPaidDuesImportDues);
    }

    public void clickContactDetailsPaidDuesUpdateAfterImport() {
        clickWhenClickable(button_ContactDetailsPaidDuesUpdate);
        waitForPageLoad();
    }

    private String getContactDetailsPaidDuesExpireDate(String expireDate, String status) {
        waitUntilElementIsVisible(tuple_ContactDetailsPaidDues(expireDate, status));
        return tuple_ContactDetailsPaidDues(expireDate, status).getText();
    }

    public List<WebElement> getPaidDuesList() {
        return table_PaidDuesTable.findElements(By.xpath(".//div/table/child::tbody/child::tr"));
    }

    private String getPaidDuesStatus(String effectiveDate) {
        int row = tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(table_PaidDuesTable, "Policy Effective", effectiveDate));
        return tableUtils.getCellTextInTableByRowAndColumnName(table_PaidDuesTable, row, "Status");
    }

    private String getErrorMessage() {
        return text_PaidDuesErrorMessage.getText();
    }

    public int findDuesRowInTableByText(String text) {
        return tableUtils.getRowNumberInTableByText(table_PaidDuesTable, text);
    }

    public String getDuesCounty(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_PaidDuesTable, row, "County");
    }

    public String getDuesAmount(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_PaidDuesTable, row, "Amount");
    }

    public String getDuesAccountNumber(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_PaidDuesTable, row, "Account Number");
    }

    public String getDuesPolicyNumber(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_PaidDuesTable, row, "Policy Number");
    }

    public String getDuesEffectiveDate(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_PaidDuesTable, row, "Effective Date");
    }

    private String getDuesExpireDate(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_PaidDuesTable, row, "Expire Date");
    }

    public String getDuesStatus(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_PaidDuesTable, row, "Status");
    }

    private void setStatus(String status) {
        tableUtils.selectValueForSelectInTable(tableDiv_ContactDetailsPaidDuesCounty, tableUtils.getRowCount(tableDiv_ContactDetailsPaidDuesCounty), "Status", status);
    }

    private String getDuesPolicyEffectiveDate(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_PaidDuesTable, row, "Policy Effective");
    }

    public ArrayList<String> getAllDuesPolicyEffectiveDates() {
        
        waitUntilElementIsVisible(table_PaidDuesTable);
        return tableUtils.getAllCellTextFromSpecificColumn(table_PaidDuesTable, "Policy Effective");
    }

    public ArrayList<String> getAllDuesEffectiveDates() {
        
        waitUntilElementIsVisible(table_PaidDuesTable);
        return tableUtils.getAllCellTextFromSpecificColumn(table_PaidDuesTable, "Effective Date");
    }

    public boolean compareUiDuesYearWithAscendingDuesYear(List<String> duesYearAscendingOrder) {

        ArrayList<String> policyEffectiveDates = getAllDuesPolicyEffectiveDates();
        Assert.assertEquals(policyEffectiveDates.size(), duesYearAscendingOrder.size());

        for (int i = 0, x = policyEffectiveDates.size(); i < policyEffectiveDates.size(); i++, x--) {
            System.out.println(policyEffectiveDates.get(i).substring(policyEffectiveDates.get(i).length() - 4));
            System.out.println(duesYearAscendingOrder.get(x - 1));
            if (!policyEffectiveDates.get(i).substring(policyEffectiveDates.get(i).length() - 4).equals(duesYearAscendingOrder.get(x - 1))) {
                return false;
            }
        }
        return true;
    }

    public boolean ensureEffectiveDateListedOnce() {
        ArrayList<String> effectiveDates = getAllDuesEffectiveDates();
        for (int i = 0; i < effectiveDates.size() - 1; i++) {
            for (int x = i + 1; x < effectiveDates.size(); x++)
                if (effectiveDates.get(i).equals(effectiveDates.get(x))) {
                    return false;
                }
        }
        return true;
    }

    public void clickSyncButton() {
        clickWhenClickable(button_PaidDuesSync);
    }
    
    public void addDues(Date duesDate, CountyIdaho county, String status) {
    	clickContactDetailsPaidDuesAdd();
        setPolicyEffectiveDate(duesDate);
        setNewDuesCounty(county);
        setStatus(status);
    }


    // *****************************************************************************************
    // Add Link Methods
    // *****************************************************************************************

    private void clickContactDetailsPaidDuesAddOKButton() {
        clickWhenClickable(button_ContactDetailsPaidDuesAddOK);
        
    }

    private void clickContactDetailsPaidDuesCancelButton() {
        clickWhenClickable(text_PaidDuesCancel);
    }

    private void clickContactDetailsPaidDuesAddCancelButton() {
        clickWhenClickable(button_ContactDetailsPaidDuesAddOK);
    }

    private void setContactDetailsPaidDuesAddPolicyEffectiveEditbox(String effectiveDate) {
        waitUntilElementIsVisible(editbox_ContactDetailsPaidDuesAddPolicyEffective);
        editbox_ContactDetailsPaidDuesAddPolicyEffective.sendKeys(effectiveDate);
        editbox_ContactDetailsPaidDuesAddPolicyEffective.sendKeys(Keys.TAB);
        
    }

    private void setContactDetailsPaidDuesAccount(String accountNumber) {
        clickWhenClickable(link_ContactDetailsPaidDuesAddAccountFinder);
        clickReset();
        AdvancedSearchAB searchAccount = new AdvancedSearchAB(driver);
        searchAccount.accountSearch(accountNumber);
        searchAccount.clickAdvancedSearchAccountNumberSearchResultsSelect(accountNumber);
    }

    private String getContactDetailsPaidDuesAccount() {
        waitUntilElementIsVisible(input_ContactDetailsPaidDuesAccountNumber);
        return input_ContactDetailsPaidDuesAccountNumber.getAttribute("value");
    }


    private void setNewDuesCounty(CountyIdaho county) {
        waitUntilElementIsVisible(tableDiv_ContactDetailsPaidDuesCounty);
        int row = tableUtils.getRowCount(tableDiv_ContactDetailsPaidDuesCounty);
        
        tableUtils.selectValueForSelectInTable(tableDiv_ContactDetailsPaidDuesCounty, row, "County", county.getValue());
        
    }

    private void setNewContactDetailsBasicsPolicyNumber(String policyNumber) {
        tableUtils.selectValueForSelectInTable(tableDiv_ContactDetailsPaidDuesCounty, tableUtils.getRowCount(tableDiv_ContactDetailsPaidDuesCounty), "Policy Number", policyNumber);
    }

    public String getNewContactBasicsPaidDuesEffectiveDate() {
        waitUntilElementIsVisible(text_ContactDetailsPaidDuesEffectiveDate);
        String effectiveDate = text_ContactDetailsPaidDuesEffectiveDate.getText();
        return effectiveDate;
    }

    public String getNewContactBasicsPaidDuesPolicyEffectiveDate() {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_PaidDuesTable, 1, "Policy Effective");
    }

    public ArrayList<String> getExpireDates() {
        
        waitUntilElementIsVisible(table_PaidDuesTable);
        return tableUtils.getAllCellTextFromSpecificColumn(table_PaidDuesTable, "Expire Date");
    }

    private void setPolicyEffectiveDate(Date effectiveDate) {
        tableUtils.setValueForCellInsideTable(tableDiv_ContactDetailsPaidDuesCounty, tableUtils.getRowCount(tableDiv_ContactDetailsPaidDuesCounty), "Policy Effective", "PolicyEffective", DateUtils.dateFormatAsString("MM/dd/yyyy", effectiveDate));
    }

    public boolean addDue(CountyIdaho county, String policyNumber, String status, Date effectiveDate) {
        
        if (checkIfElementExists(button_ContactDetailsPaidDuesEdit, 2000)) {
            clickWhenClickable(button_ContactDetailsPaidDuesEdit);
        }
        int row = tableUtils.getRowCount(tableDiv_ContactDetailsPaidDuesCounty);
        clickAdd();
        
        waitUntilElementIsClickable(tableDiv_ContactDetailsPaidDuesCounty.findElement(By.xpath("//tr[contains(@data-recordindex, '" + row + "')]")));
        
        setNewDuesCounty(county);
        
        setNewContactDetailsBasicsPolicyNumber(policyNumber);
        setStatus(status);
        setPolicyEffectiveDate(effectiveDate);
        clickUpdate();
        
        return !checkIfElementExists("//div[contains(@class, 'message') and contains(text(), 'due record matching the Effective and Expire already exists for Policy Effective')]", 1000);
    }

    public List<WebElement> getPaidDuesMessages() {
        List<WebElement> paidDuesError = text_ContactDetailsPaidDuesError;
        return paidDuesError;
    }

    public String getMostRecentPolicyEffectiveDate() throws ParseException {
        String effectiveDate;
        ArrayList<String> effectiveDates = tableUtils.getAllCellTextFromSpecificColumn(table_PaidDuesTable, "Policy Effective");
        effectiveDate = effectiveDates.get(0);
        for (String policyEffectiveDate : effectiveDates) {
            if (DateUtils.getDifferenceBetweenDates(DateUtils.convertStringtoDate(effectiveDate, "MM/dd/yyyy"), DateUtils.convertStringtoDate(policyEffectiveDate, "MM/dd/yyyy"), DateDifferenceOptions.Day) > 0) {
                effectiveDate = policyEffectiveDate;
            }
        }
        return effectiveDate;
    }
}

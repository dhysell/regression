package repository.ab.contact;

import com.idfbins.enums.State;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import persistence.globaldatarepo.entities.AbUsers;
import repository.ab.search.AdvancedSearchAB;
import repository.driverConfiguration.BasePage;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.ab.ContactHistoryChange;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;

import java.util.ArrayList;
import java.util.List;

public class ContactDetailsHistoryAB extends BasePage {

    private WebDriver driver;
    private TableUtils tableUtils;

    public ContactDetailsHistoryAB(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//span[contains(@id,'ContactDetail:ABContactDetailScreen:ttlBar')]")
    private WebElement tableDiv_ContactDetailsHistoryContactTitle;

    @FindBy(xpath = "//*[contains(@id,'ContactDetail:ABContactDetailScreen:ContactHistoryLV')]")
    private WebElement tableDiv_ContactDetailsHistoryChanges;

    @FindBy(xpath = "//*[contains(@id,':ContactBasicsCardTab-btnEl')]")
    private WebElement link_ContactDetailsBasics;


    /*   Contact History Repository   */

    @FindBy(xpath = "//div[contains(@id,'ContactHistoryDetailPopup:ContactHistoryDetailScreen:HistoryTrackedChangesLV')]")
    private WebElement tableDiv_ContactHistoryTrackedChangesTable;

    @FindBy(xpath = "//*[contains(@id,'ContactHistoryDetailPopup:ContactHistoryDetailScreen:ContactHistoryDV:Date-inputEl')]")
    private WebElement text_ContactHistoryDate;

    @FindBy(xpath = "//*[contains(@id,'ContactHistoryDetailPopup:ContactHistoryDetailScreen:ContactHistoryDV:User-inputEl')]")
    private WebElement text_ContactHistoryUser;

    @FindBy(xpath = "//*[contains(@id,'ContactHistoryDetailPopup:ContactHistoryDetailScreen:ContactHistoryDV:Type-inputEl')]")
    private WebElement text_ContactHistoryType;

    @FindBy(xpath = "//*[contains(@id,'ContactHistoryDetailPopup:ContactHistoryDetailScreen:ContactHistoryDV:Description-inputEl')]")
    private WebElement text_ContactHistoryDescription;

    @FindBy(xpath = "//*[contains(@id,'ContactHistoryDetailPopup:ContactHistoryDetailScreen:HistoryTrackedChangesLV')]")
    private WebElement tableDiv_ContactHistoryTrackedChanges;

    @FindBy(xpath = "//*[contains(@id,'ContactHistoryDetailPopup:ContactHistoryDetailScreen:ContactHistoryDV:Date-inputEl')]")
    private WebElement text_ContactHistoryChangedField;

    @FindBy(xpath = "//*[contains(@id,'ContactHistoryDetailPopup:ContactHistoryDetailScreen:ContactHistoryDV:Date-inputEl')]")
    private WebElement text_ContactHistoryTrackedChangesOldValue;

    @FindBy(xpath = "//*[contains(@id,'ContactHistoryDetailPopup:ContactHistoryDetailScreen:ContactHistoryDV:Date-inputEl')]")
    private WebElement text_ContactHistoryTrackedChangesNewValue;

    @FindBy(xpath = "//*[contains(@id,'ContactHistoryDetailPopup:__crumb__')]")
    private WebElement link_ContactHistoryReturnToContact;

    private List<WebElement> link_ContactBasicsHistory(String type) {
        return finds(By.xpath("//div[contains(@id, 'ContactDetail:ABContactDetailScreen:ContactHistoryLV')]//td/div[contains(., '" + type + "')]/ancestor::tr/td/div/a[contains(., 'Changes')]"));
    }

    private List<WebElement> link_ContactBasicsHistory(String type, String user, String date, String description) {
        return finds(By.xpath("//div[contains(@id, 'ContactDetail:ABContactDetailScreen:ContactHistoryLV')]//td/div[contains(., '" + type + "')]/ancestor::tr/td/div[contains(.,'" + user + "')]/ancestor::tr/td/div[contains(.,'" + date + "')]/ancestor::tr/td/div[contains(.,'" + description + "')]/ancestor::tr/td/div/a[contains(., 'Changes')]"));
    }

    private List<WebElement> link_ContactBasicsHistoryNoChanges(String type, String user, String date, String description) {
        return finds(By.xpath("//div[contains(@id, 'ContactDetail:ABContactDetailScreen:ContactHistoryLV')]//td/div[contains(., '" + type + "')]/ancestor::tr/td/div[contains(.,'" + user + "')]/ancestor::tr/td/div[contains(.,'" + date + "')]/ancestor::tr/td/div[contains(.,'" + description + "') and contains(@class, 'x-grid-cell-inner')]"));
    }

    private List<WebElement> link_ContactBasicsHistory(String type, String description) {
        return finds(By.xpath("//div[contains(@id, 'ContactDetail:ABContactDetailScreen:ContactHistoryLV')]//td/div[contains(., '" + type + "')]/ancestor::tr/td/div[contains(., '" + description + "')]/ancestor::tr/td/div/a[contains(., 'Changes')]"));
    }

    private List<WebElement> text_ContactBasicsHistory(String type) {
        return finds(By.xpath("//div[contains(@id, 'ContactDetail:ABContactDetailScreen:ContactHistoryLV')]//td/div[contains(., '" + type + "')]"));
    }

    @FindBy(xpath = "//div[contains(@id,'ContactHistoryDetailPopup:ContactHistoryDetailScreen:HistoryTrackedChangesLV')]")
    private WebElement tableDiv_ContacthistoryChanges;

    /*   Helper Methods    */

    public String getContactTitle() {
        
        waitUntilElementIsClickable(tableDiv_ContactDetailsHistoryContactTitle);
        return tableDiv_ContactDetailsHistoryContactTitle.getText();
    }

    public void clickContactDetailsBasicsTab() {
        clickWhenClickable(link_ContactDetailsBasics);
    }

    private void clickChanges() {

        tableUtils.clickCellInTableByRowAndColumnName(tableDiv_ContactDetailsHistoryChanges, 1, "Change Details");
    }

    private void clickReturnToContact() {
        clickWhenClickable(link_ContactHistoryReturnToContact);
    }

    /*      Contact History Helper Methods		*/

    private String getChangeDate() {
        waitUntilElementIsClickable(text_ContactHistoryDate);
        return text_ContactHistoryDate.getText();
    }

    private String getUser() {
        waitUntilElementIsVisible(text_ContactHistoryUser);
        return text_ContactHistoryUser.getText();
    }

    private String getType() {
        waitUntilElementIsVisible(text_ContactHistoryType);
        return text_ContactHistoryType.getText();
    }

    private String getDescription() {
        waitUntilElementIsVisible(text_ContactHistoryDescription);
        return text_ContactHistoryDescription.getText();
    }

    private String getChangedField() {
        return tableUtils.getCellTextInTableByRowAndColumnName(tableDiv_ContactHistoryTrackedChanges, 1, "Changed Field");
    }

    private String getPreviousValue() {
        return tableUtils.getCellTextInTableByRowAndColumnName(tableDiv_ContactHistoryTrackedChanges, 1, "Old Value");
    }

    private String getNewValue() {
        return tableUtils.getCellTextInTableByRowAndColumnName(tableDiv_ContactHistoryTrackedChanges, 1, "New Value");
    }

    public void clickChangesByDetails(String type) {
        
        clickWhenClickable(link_ContactBasicsHistory(type).get(0));
    }

    private void clickChangesByTypeAndDescription(String type, String description) {
        
        clickWhenClickable(link_ContactBasicsHistory(type, description).get(0));
        
    }

    public ContactHistoryChange getHistoryItem(String changedField) {
        
        waitUntilElementIsVisible(tableDiv_ContacthistoryChanges);
        String changeField = tableUtils.getCellTextInTableByRowAndColumnName(tableDiv_ContacthistoryChanges, tableUtils.getRowNumberInTableByText(tableDiv_ContacthistoryChanges, changedField), "Changed Field");
        String prevValue = tableUtils.getCellTextInTableByRowAndColumnName(tableDiv_ContacthistoryChanges, tableUtils.getRowNumberInTableByText(tableDiv_ContacthistoryChanges, changedField), "Old Value");
        String newValue = tableUtils.getCellTextInTableByRowAndColumnName(tableDiv_ContacthistoryChanges, tableUtils.getRowNumberInTableByText(tableDiv_ContacthistoryChanges, changedField), "New Value");
        return new ContactHistoryChange(changeField, prevValue, newValue);
    }

    public ArrayList<String> getHistoryByType(String historyType) {
        
        waitUntilElementIsVisible(tableDiv_ContactDetailsHistoryChanges);
        ArrayList<String> historyTypes = new ArrayList<String>();
        List<String> histories = tableUtils.getAllCellTextFromSpecificColumn(tableDiv_ContactDetailsHistoryChanges, "Type");
        for (String historyItem : histories) {
            if (historyItem.contains(historyType)) {
                historyTypes.add(historyItem);
            }
        }
        return historyTypes;
    }

    public ArrayList<String> verifyHistory(String type, String description, String field, String oldValue, String newValue) {
        ArrayList<String> changeResults = new ArrayList<String>();
        clickChangesByTypeAndDescription(type, description);
        
        String changedFieldText = null;
        boolean fieldFound = false;
        int rowCount = tableUtils.getRowCount(tableDiv_ContacthistoryChanges);
        for (int i = 0; i < rowCount; i++) {
            changedFieldText = tableUtils.getCellTextInTableByRowAndColumnName(tableDiv_ContacthistoryChanges, i + 1, "Changed Field");
            if (changedFieldText.equals(field)) {
                fieldFound = true;
                ContactHistoryChange change = getHistoryItem(changedFieldText);
                if (!change.getChangedField().equals(changedFieldText)) {
                    changeResults.add("The Changed Field is expected to be " + changedFieldText + " but it is " + change.getOldValue() + ".");
                }

                if (!change.getOldValue().equals(oldValue) && !change.getOldValue().contains(oldValue)) {
                    changeResults.add("The Old Value Field is expected to be " + oldValue + " but it is " + change.getOldValue() + ".");
                }

                if (!change.getNewValue().equals(newValue) && !change.getNewValue().contains(newValue)) {
                    changeResults.add("The New Value Field is expected to be " + newValue + " but it is " + change.getOldValue() + ".");
                }
            }
        }
        if (!fieldFound) {
            changeResults.add("Field not found in Change Results.");
        }
        clickReturnToContact();
        return changeResults;
    }

    public ArrayList<String> verifyHistoryTypeUserDescriptionNewValue(String type, String description, String user, String field, String newValue) {
        ArrayList<String> changeResults = new ArrayList<String>();
        List<WebElement> historyItems = tableDiv_ContactDetailsHistoryChanges.findElements(By.xpath("./tr/td/div[contains(., '" + type + "')]/parent::td/parent::tr/td/div[contains(., '" + user + "')]/parent::td/parent::tr/td/div[contains(., '" + description + "')]"));
        if (historyItems.size() > 1) {
            return null;
        }
        clickChangesByTypeAndDescription(type, description);
        
        String changedFieldText = null;
        boolean fieldFound = false;
        int rowCount = tableUtils.getRowCount(tableDiv_ContacthistoryChanges);
        for (int i = 0; i < rowCount; i++) {
            changedFieldText = tableUtils.getCellTextInTableByRowAndColumnName(tableDiv_ContacthistoryChanges, i + 1, "Changed Field");
            if (changedFieldText.equals(field)) {
                fieldFound = true;
                ContactHistoryChange change = getHistoryItem(changedFieldText);
                if (!change.getChangedField().equals(changedFieldText)) {
                    changeResults.add("The Changed Field is expected to be " + changedFieldText + " but it is " + change.getOldValue() + ".");
                }

                if (!change.getNewValue().equals(newValue) && !change.getNewValue().equals(newValue + ".000000")) {
                    changeResults.add("The New Value Field is expected to be " + newValue + " but it is " + change.getOldValue() + ".");
                }
            }
        }
        if (!fieldFound) {
            changeResults.add("Field not found in Change Results.");
        }
        clickReturnToContact();
        return changeResults;
    }

    public boolean verifyHistoryItemExists(String type, String user, String date, String description, boolean changes) {
    	if(changes) {
    		List<WebElement> historyItems = link_ContactBasicsHistory(type, user, date, description);
    		return historyItems.size() == 1;
    	} else {
    		List<WebElement> historyItems = link_ContactBasicsHistoryNoChanges(type, user, date, description);
    		if(historyItems.size() == 1) {
    			return true;
    		}
    	}
    	return false;
    }

    /*
            public String getHistoryByTypeTodaysEntry(String historyType){
            
            waitUntilElementIsVisible(tableDiv_ContactDetailsHistoryChanges);
            ArrayList<String> historyTypes = new ArrayList<String>();
            ArrayList<String> columnNames = new ArrayList<>();
            columnNames.add("Type");
            ArrayList<String> columnValues = new ArrayList<>();
            columnValues.add(historyType);
            System.out.println("look at this: " + DateUtils.dateFormatAsString("MMM dd, yyyy", new Date()));
            WebElement row = tableUtils.getRowInTableByColumnsAndValues(tableDiv_ContactDetailsHistoryChanges, columnNames, columnValues);
            return row.getText();
        }
    */
    public String verifyHistoryNoChangeDetail(String type, String description, String date) {
        List<WebElement> histories = finds(By.xpath("//div[contains(.,'" + type + "')]/parent::td/following-sibling::td/div[contains(.,'" + date + "')]/parent::td/following-sibling::td/div[contains(.,'" + description + "')]"));
        if (histories.isEmpty()) {
            return "History item not found";
        } else {
            return "There where " + histories.size() + " history items found.";
        }
    }

    public boolean getToHistoryPage(AbUsers abUser, String firstName, String lastName, String address, State state) throws GuidewireNavigationException {
        repository.ab.search.AdvancedSearchAB searchPage = new AdvancedSearchAB(driver);
        if (searchPage.loginAndSearchContact(abUser, firstName, lastName, address, state)) {
            ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
            basicsPage.clickContactDetailsBasicsHistoryLink();
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<String> verifyStandardizedAddress(String type, String description, AddressInfo address, boolean shouldStandardize) {
        ArrayList<String> changeResults = new ArrayList<String>();
        clickChangesByTypeAndDescription(type, description);
        

        String address1 = tableUtils.getCellTextInTableByRowAndColumnName(tableDiv_ContactHistoryTrackedChangesTable, tableUtils.getRowInTableByColumnNameAndValue(tableDiv_ContactHistoryTrackedChangesTable, "Changed Field", "Address 1"), "New Value");
        String city = tableUtils.getCellTextInTableByRowAndColumnName(tableDiv_ContactHistoryTrackedChangesTable, tableUtils.getRowInTableByColumnNameAndValue(tableDiv_ContactHistoryTrackedChangesTable, "Changed Field", "City"), "New Value");
        String state = tableUtils.getCellTextInTableByRowAndColumnName(tableDiv_ContactHistoryTrackedChangesTable, tableUtils.getRowInTableByColumnNameAndValue(tableDiv_ContactHistoryTrackedChangesTable, "Changed Field", "State"), "New Value");
        String zip = tableUtils.getCellTextInTableByRowAndColumnName(tableDiv_ContactHistoryTrackedChangesTable, tableUtils.getRowInTableByColumnNameAndValue(tableDiv_ContactHistoryTrackedChangesTable, "Changed Field", "Postal Code"), "New Value");
        String standardizedDate = null;
        String standardized = null;
        if (shouldStandardize) {
            standardized = tableUtils.getCellTextInTableByRowAndColumnName(tableDiv_ContactHistoryTrackedChangesTable, tableUtils.getRowInTableByColumnNameAndValue(tableDiv_ContactHistoryTrackedChangesTable, "Changed Field", "Standardize?"), "New Value");
            standardizedDate = tableUtils.getCellTextInTableByRowAndColumnName(tableDiv_ContactHistoryTrackedChangesTable, tableUtils.getRowInTableByColumnNameAndValue(tableDiv_ContactHistoryTrackedChangesTable, "Changed Field", "Standardized On"), "New Value");
        }

        if (!address1.equals(address.getLine1())) {
            changeResults.add("Address Line 1 of the address passed in and the changes does not match.");
        }

        if (!city.equals(address.getCity())) {
            changeResults.add("The city of the address passed in and the changes do not match.");
        }

        if (!state.equals(address.getState().getName())) {
            changeResults.add("The state in the address passed in and the changes do not match.");
        }

        if (!zip.equals(address.getZip())) {
            changeResults.add("The Zip Code of the address passed in and the changes does not match.");
        }

        if (standardized == null && shouldStandardize) {
            changeResults.add("The Address should standardize and there address was not standardized. see address: " + address1 + ", " + city + " " + state + ".");
        }

        if (shouldStandardize) {
            if (!standardizedDate.contains(DateUtils.getCenterDateAsString(getDriver(), ApplicationOrCenter.ContactManager, "EEE, MMM dd "))) {
                changeResults.add("Address Line 1 of the address passed in and the changes does not match.");
            }
        }
        return changeResults;
    }
}

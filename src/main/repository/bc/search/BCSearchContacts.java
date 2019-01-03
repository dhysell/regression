package repository.bc.search;

import com.idfbins.enums.State;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.ContactSubType;
import repository.gw.helpers.TableUtils;

import java.util.HashMap;

public class BCSearchContacts extends BasePage {

    private WebDriver driver;

    public BCSearchContacts(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------
    private Guidewire8Select select_BCSearchContactsType() {
        return new Guidewire8Select(driver, "//table[contains(@id,'ContactSearch:ContactSearchScreen:ContactSearchDV:ContactType-triggerWrap')]");
    }

    @FindBy(xpath = "//div[@id='ContactSearch:ContactSearchScreen:ContactSearchResultsLV']")
    private WebElement table_SearchContactsResults;

    @FindBy(xpath = "//input[@id='ContactSearch:ContactSearchScreen:ContactSearchDV:Keyword-inputEl']")
    private WebElement editbox_BCSearchContactsName;

    @FindBy(xpath = "//input[contains(@id, ':City-inputEl')]")
    private WebElement editBox_BCSearchContactsCity;

    private Guidewire8Select select_BCSearchContactsState() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':State-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id, ':PostalCode-inputEl')]")
    private WebElement editBox_BCSearchContactsZip;

    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------

    public void setSearchContactsType(ContactSubType type) {
        select_BCSearchContactsType().selectByVisibleText(type.getValue());
    }

    public WebElement getSearchContactsResultsTable() {
        return table_SearchContactsResults;
    }

    public void setSearchContactsName(String name) {
        waitUntilElementIsVisible(editbox_BCSearchContactsName);
        editbox_BCSearchContactsName.sendKeys(Keys.CONTROL + "a");
        editbox_BCSearchContactsName.sendKeys(name);
    }

    public void setBCSearchContactsCity(String city) {
        clickWhenClickable(editBox_BCSearchContactsCity);
        editBox_BCSearchContactsCity.sendKeys(Keys.chord(Keys.CONTROL + "a"), city);
    }

    public void setBCSearchContactsState(State stateToSelect) {
        Guidewire8Select bcSearchAccountsState = select_BCSearchContactsState();
        bcSearchAccountsState.selectByVisibleText(stateToSelect.getName());
    }

    public void setBCSearchContactsZip(String zipPostalCode) {
        clickWhenClickable(editBox_BCSearchContactsZip);
        editBox_BCSearchContactsZip.sendKeys(Keys.chord(Keys.CONTROL + "a"), zipPostalCode);
    }

    public boolean contactsTableExist() {
        return table_SearchContactsResults.isDisplayed();
    }

    public boolean verifyContactsTable(String companyName, String address) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        columnRowKeyValuePairs.put("Name", companyName);
        columnRowKeyValuePairs.put("Address", address);
        return new TableUtils(getDriver()).getRowsInTableByColumnsAndValues(table_SearchContactsResults, columnRowKeyValuePairs).size() >= 1;
    }
}

package repository.ab.search;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.gw.helpers.TableUtils;

import java.util.ArrayList;

public class PhoneNumberSearchAB extends SearchAB {

    private TableUtils tableUtils;

    public PhoneNumberSearchAB(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------

    @FindBy(xpath = "//input[contains(@id, 'PhoneSearch:PhoneSearchScreen:PhoneSearchDV:PhoneNumber-inputEl')]")
    public WebElement editbox_PhoneNumberSearchPhone;

    @FindBy(xpath = "//div[contains(., 'The search returned zero results.')]")
    public WebElement text_PhoneNumberSearchZeroResultsMessage;

    @FindBy(xpath = "//div[contains(@id, 'PhoneSearch:PhoneSearchScreen:PhoneSearchLV')]")
    public WebElement div_SearchResultsTable;

    @FindBy(xpath = "//input[contains(@id, ':_ListPaging-inputEl')]")
    private WebElement input_PageSelector;


    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------

    // Input Methods

    public boolean isPhoneSearch() {
        return checkIfElementExists(editbox_PhoneNumberSearchPhone, 1000);
    }

    public void setPhoneNumber(String num) {
        clickWhenClickable(editbox_PhoneNumberSearchPhone);
        editbox_PhoneNumberSearchPhone.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_PhoneNumberSearchPhone.sendKeys(num);
        editbox_PhoneNumberSearchPhone.sendKeys(Keys.TAB);
    }

    public void clickSearch() {
        super.clickSearch();
    }

    public void clickReset() {
        super.clickReset();
    }

    public boolean zeroSearchResultsMessage() {
        return checkIfElementExists("//div[contains(., 'The search returned zero results.')]", 1000);
    }

    public String searchNumber(String number, String contactName) {
        setPhoneNumber(number);
        clickSearch();
        boolean morePages = new TableUtils(getDriver()).hasMultiplePages(find(By.xpath("//div[@id='PhoneSearch:PhoneSearchScreen:PhoneSearchLV']")));
        if (morePages) {
            int maxPages = new TableUtils(getDriver()).getNumberOfTablePages(find(By.xpath("//div[@id='PhoneSearch:PhoneSearchScreen:PhoneSearchLV']")));
            for (int i = 1; i <= maxPages; i++) {
                setTextHitEnter(input_PageSelector, String.valueOf(i));
                ArrayList<String> searchResults = tableUtils.getAllCellTextFromSpecificColumn(div_SearchResultsTable, "Name");
                for (String searchResultName : searchResults) {
                    if (searchResultName.contains(contactName)) {
                        tableUtils.clickLinkInTable(div_SearchResultsTable, contactName);
                        return searchResultName;
                    }
                }
            }
        } else {
            ArrayList<String> searchResults = tableUtils.getAllCellTextFromSpecificColumn(div_SearchResultsTable, "Name");
            tableUtils.clickLinkInTable(div_SearchResultsTable, contactName);
            for (String searchResultName : searchResults) {
                if (searchResultName.contains(contactName)) {
                    return searchResultName;
                }
            }
        }
        return "false"; //searchResults.get(0);
    }
}

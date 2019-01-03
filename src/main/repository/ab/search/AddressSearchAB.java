package repository.ab.search;


import com.idfbins.enums.State;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.ab.contact.ContactDetailsAddressesAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.gw.elements.Guidewire8Select;
import repository.gw.errorhandling.ErrorHandling;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.helpers.TableUtils;

import java.util.ArrayList;
import java.util.List;

public class AddressSearchAB extends SearchAB {

    private WebDriver driver;
    private TableUtils tableUtils;

    public AddressSearchAB(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------

    public Guidewire8Select select_AddressSearchState() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':State-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id, ':AddressLine1-inputEl')]")
    public WebElement editbox_AddressSearchAddressLine1;

    @FindBy(xpath = "//input[contains(@id, ':City-inputEl')]")
    public WebElement editbox_AddressSearchCity;

    @FindBy(xpath = "//input[contains(@id, ':PostalCode-inputEl')]")
    public WebElement editbox_AddressSearchPostalCode;

    @FindBy(xpath = "//div[contains(@id, 'AddressSearch:AddressSearchScreen:AddressSearchLV')]")
    public WebElement tableDiv_AddressSearchSearchResults;

    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------

    // Input Methods

    public boolean isActivitySearch() {
        
        if (checkIfElementExists(editbox_AddressSearchAddressLine1, 1000)) {
            return true;
        } else {
            return false;
        }
    }

    public void setAddress(String street) {
        
        clickWhenClickable(editbox_AddressSearchAddressLine1);
        editbox_AddressSearchAddressLine1.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_AddressSearchAddressLine1.sendKeys(Keys.DELETE);
        editbox_AddressSearchAddressLine1.sendKeys(street);
        editbox_AddressSearchAddressLine1.sendKeys(Keys.TAB);
    }

    public void setCity(String city) {
        
        clickWhenClickable(editbox_AddressSearchCity);
        editbox_AddressSearchCity.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_AddressSearchCity.sendKeys(Keys.DELETE);
        editbox_AddressSearchCity.sendKeys(city);
        editbox_AddressSearchCity.sendKeys(Keys.TAB);
    }

    public void setState(State state) {
        
        select_AddressSearchState().selectByVisibleTextPartial(state.getName());
        
    }

    public void setZip(String zip) {
        
        clickWhenClickable(editbox_AddressSearchPostalCode);
        editbox_AddressSearchPostalCode.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_AddressSearchPostalCode.sendKeys(Keys.DELETE);
        editbox_AddressSearchPostalCode.sendKeys(zip);
        editbox_AddressSearchPostalCode.sendKeys(Keys.TAB);
    }

    public List<WebElement> getSearchResult() {
        return super.getSearchResultsRows();
    }

    public boolean setSearchCriteria(AddressInfo address) {
        setAddress(address.getLine1());
        setCity(address.getCity());
        setState(address.getState());
        setZip(address.getZip());
        
        if (checkIfElementExists("//a[contains(@id,':AddressSearchScreen:AddressSearchDV:Search')]", 1000)) {
            clickSearch();
            return true;
        }
        return false;
    }

    public boolean hasErrorMessage() {
        
        ErrorHandling error = new ErrorHandling(getDriver());
        return error.validationMessageExists("At least City or Postal Code with Address Line 1 must exist to perform search");
    }

    public String getErrorMessage() {
        
        ErrorHandling error = new ErrorHandling(getDriver());
        return error.text_ErrorHandlingErrorBanner().getText();
    }

    public ContactDetailsBasicsAB clickSearchResults(String addressLine1) {
        tableUtils.clickLinkInSpecficRowInTable(tableDiv_AddressSearchSearchResults, tableUtils.getRowNumberInTableByText(tableDiv_AddressSearchSearchResults, addressLine1));
        return new ContactDetailsBasicsAB(driver);
    }

    public String searchResultsContainCriteria(String address, String searchCity, String zip) throws GuidewireNavigationException {
        for (int lcv = 1; lcv <= tableUtils.getRowCount(tableDiv_AddressSearchSearchResults); lcv++) {
            if (!tableUtils.getCellTextInTableByRowAndColumnName(tableDiv_AddressSearchSearchResults, lcv, "Name").equals(" ")) {
                tableUtils.clickLinkInSpecficRowInTable(tableDiv_AddressSearchSearchResults, lcv);
                ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
                String[] roles = basicsPage.getRoles();
                boolean roleFound = false;
                for (String role : roles) {
                    if (role.contains("Vendor") || role.contains("Lienhold")) {
                        roleFound = true;
                    }
                }
                if (roleFound == false) {
                    return address + "Address Search Results line " + lcv + " does not have a role of Lienholder or Vendor.";
                }
                basicsPage.clickContactDetailsBasicsAddressLink();
                ContactDetailsAddressesAB addressPage = new ContactDetailsAddressesAB(driver);
                boolean addressCriterion = false;
                ArrayList<String> stringAddresses = addressPage.getAddresses();
                for (String addressListing : stringAddresses) {
                    if (addressListing.contains(address)) {
                        addressCriterion = true;
                    }
                }
                if (addressCriterion == false) {
                    return "The " + address + "criterion was not found on any addresses of the results of the contact on line " + lcv + ".";
                }
                addressPage.clickReturnToAddressSearch();

            }
        }
        return "Pass";
    }


}

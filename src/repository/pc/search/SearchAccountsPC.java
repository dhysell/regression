package repository.pc.search;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.TaxReportingOption;
import repository.gw.helpers.TableUtils;
import repository.pc.topmenu.TopMenuPC;

public class SearchAccountsPC extends BasePage {

    private WebDriver driver;

    public SearchAccountsPC(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------

    @FindBy(xpath = "//input[contains(@id, 'AccountNumber-inputEl')]")
    private WebElement editBox_SearchAccountsPCAccountNumber;

    @FindBy(xpath = "//div[(contains(@id, 'AccountSearchResultsLV')) or (contains(@id, 'DocumentTemplateSearchResultsLV')) or (contains(@id, 'PolicySearchResultsLV')) or (contains(@id, 'PolicySearch_ResultsLV')) or (contains(@id, 'InvoiceSearchResultsLV')) or (contains(@id, 'LienholderSearchPanelSet:2')) or (contains(@id, 'ContactSearchPopup:ContactSearchScreen')) or (contains(@id, 'ContactSearchLV_FBMPanelSet:1')) or (contains(@id, 'ClaimSearchScreen:ClaimSearchResultsLV'))]")
    private WebElement table_SearchAccountsPCSearchResults;

    @FindBy(css = "input[id$=':SSN-inputEl']")
    private WebElement inputSSN;


    @FindBy(css = "input[id$=':TIN-inputEl']")
    private WebElement inputTIN;

    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------

    private void getToSearch() {
        TopMenuPC topMenu = new TopMenuPC(getDriver());
        topMenu.clickSearchTab();
        SearchSidebarPC sidebar = new SearchSidebarPC(getDriver());
        sidebar.clickAccounts();
    }

    public void setAccountAccountNumber(String numToFill) {
        clickReset();
        setText(editBox_SearchAccountsPCAccountNumber, numToFill);
    }

    public void clickFirstAccountNumber(int rowNumber) {
        new TableUtils(getDriver()).clickLinkInSpecficRowInTable(table_SearchAccountsPCSearchResults, rowNumber);
    }

    public void searchAccountByAccountNumber(String searchAccountNumber) {
        getToSearch();
        setAccountAccountNumber(searchAccountNumber);
        clickSearch();
        clickFirstAccountNumber(1);
        //If the account number is not found, we need to be able to go to the message queues and check for stuck messages.
    }

    public void setSSNTIN(String socialSecurityNumber, TaxReportingOption ssn) {
        new Guidewire8Select(driver, "//table[contains(@id,':TaxReportingOption-triggerWrap')]").selectByVisibleText(ssn.getValue());
        if (ssn.equals(TaxReportingOption.TIN)) {
            setText(inputTIN, socialSecurityNumber);
        } else if (ssn.equals(TaxReportingOption.SSN)) {
            setText(inputSSN, socialSecurityNumber);
        }
    }
}

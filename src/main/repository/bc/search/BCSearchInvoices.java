package repository.bc.search;

import com.idfbins.enums.State;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.InvoiceStatus;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;
import repository.pc.topmenu.TopMenuPC;

import java.util.Date;
import java.util.List;

public class BCSearchInvoices extends BasePage {

    private TableUtils tableUtils;
    private WebDriver driver;

    public BCSearchInvoices(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    /////////////////////
    // Recorded Elements//
    /////////////////////

    @FindBy(xpath = "//input[contains(@id, ':InvoiceNumberCriterion-inputEl')]")
    private WebElement editbox_BCSearchInvoicesInvoiceNumber;

    @FindBy(xpath = "//input[@id='InvoiceSearch:InvoiceSearchScreen:InvoiceSearchDV:EarliestDateCriterion-inputEl']")
    private WebElement editbox_BCSearchInvoicesEarliestDate;

    @FindBy(xpath = "//input[@id='InvoiceSearch:InvoiceSearchScreen:InvoiceSearchDV:LatestDateCriterion-inputEl']")
    private WebElement editbox_BCSearchInvoicesLatestDate;

    @FindBy(xpath = "//input[contains(@id, ':MinAmountCriterion-inputEl')]")
    private WebElement editBox_BCSearchInvoicesMinimumAmount;

    @FindBy(xpath = "//input[contains(@id, ':MaxAmountCriterion-inputEl')]")
    private WebElement editBox_BCSearchInvoicesMaximumAmount;

    @FindBy(xpath = "//input[contains(@id, ':AccountNumberCriterion-inputEl')]")
    private WebElement editbox_BCSearchInvoicesAccountNumber;

    @FindBy(xpath = "//input[contains(@id, ':Name-inputEl')]")
    private WebElement editBox_BCSearchInvoicesCompanyName;

    @FindBy(xpath = "//input[contains(@id, ':FirstName-inputEl')]")
    private WebElement editBox_BCSearchInvoicesFirstName;

    @FindBy(xpath = "//input[contains(@id, ':LastName-inputEl')]")
    private WebElement editBox_BCSearchInvoicesLastName;

    @FindBy(xpath = "//input[contains(@id, ':City-inputEl')]")
    private WebElement editBox_BCSearchInvoicesCity;

    private Guidewire8Select select_BCSearchInvoicesState() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':State-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id, ':PostalCode-inputEl')]")
    private WebElement editBox_BCSearchInvoicesZip;

    @FindBy(xpath = "//div[@id='AccountSearch:AccountSearchScreen:AccountSearchResultsLV']")
    public WebElement table_BCSearchInvoicesSearchResultsTable;

    /////////////////////////////////////
    // Helper Methods for Above Elements//
    /////////////////////////////////////
    private void getToSearch() {
        TopMenuPC topMenu = new TopMenuPC(getDriver());
        topMenu.clickSearchTab();

        repository.bc.search.BCSearchMenu searchMenu = new BCSearchMenu(getDriver());
        searchMenu.clickSearchMenuInvoices();
    }

    // This is a good method to have around, But I didn't have a use for it right away. Please uncomment if you have a use for it in the future.
    /*
     * private String chooseAndSelectRandomInvoiceItem() { int randomRow = NumberUtils.generateRandomNumberInt(1, getRowCount(table_SearchSearchResults)-1); String invoiceItem = getSearchInvoicesInvoiceNumber(randomRow); clickSearchInvoicesInvoiceNumber(randomRow); return invoiceItem; }
     */

    private WebElement selectRandomInvoiceItemRow(InvoiceStatus invoiceStatus, String accountNumber) {
        String invoiceStatusGridColumnID = tableUtils.getGridColumnFromTable(table_BCSearchInvoicesSearchResultsTable, "Status");
        // String invoiceDueAmountGridColumnID = getGridColumnFromTable(table_SearchSearchResultsParent, "Due Amount");
        List<WebElement> invoiceRows = table_BCSearchInvoicesSearchResultsTable.findElements(By.xpath(".//tr/td[(contains(@class,'" + invoiceStatusGridColumnID + "'))]/div[(contains(.,'" + invoiceStatus.getValue() + "'))]"));// ancestor::tr/td[(contains(@class,'" + invoiceDueAmountGridColumnID + "'))]/div[not(contains(.,'-'))]
        if (invoiceRows.size() < 1) {
            tableUtils.sortByHeaderColumn(table_BCSearchInvoicesSearchResultsTable, "Status");
            invoiceRows = table_BCSearchInvoicesSearchResultsTable.findElements(By.xpath(".//tr/td[(contains(@class,'" + invoiceStatusGridColumnID + "'))]/div[(contains(.,'" + invoiceStatus.getValue() + "'))]"));// ancestor::tr/td[(contains(@class,'" + invoiceDueAmountGridColumnID + "'))]/div[not(contains(.,'-'))]
            if (invoiceRows.size() < 1) {
                for (int i = 0; i < 15; i++) {
                    systemOut("The attempt to select a random row matching criteria passed in failed. Trying to find an viable candidate. Now trying attempt " + (i + 1));
                    if (accountNumber.substring(0, 2).equals("98")) {
                        String accountNum = StringsUtils.generateRandomNumber(981, 989);
                        setBCSearchInvoicesAccountNumberWithoutResetting(accountNum);
                    } else {
                        String accountNum = StringsUtils.generateRandomNumber(01, 27);
                        setBCSearchInvoicesAccountNumberWithoutResetting(accountNum);
                    }
                    clickSearch();
                    invoiceStatusGridColumnID = tableUtils.getGridColumnFromTable(table_BCSearchInvoicesSearchResultsTable, "Status");
                    invoiceRows = table_BCSearchInvoicesSearchResultsTable.findElements(By.xpath(".//tr/td[(contains(@class,'" + invoiceStatusGridColumnID + "'))]/div[(contains(.,'" + invoiceStatus.getValue() + "'))]"));// ancestor::tr/td[(contains(@class,'" + invoiceDueAmountGridColumnID + "'))]/div[not(contains(.,'-'))]
                    if (invoiceRows.size() > 0) {
                        break;
                    }
                }
                if (invoiceRows.size() < 1) {
                    Assert.fail("The attempt to select a random row matching the criteria used did not yield any results. Please narrow your criteria and try the search again.");
                }
            }
        }
        return invoiceRows.get(NumberUtils.generateRandomNumberInt(0, invoiceRows.size() - 1));
    }

    public String searchForAccountByInvoiceAndAmountRange(String accountNumber, double minimumAmount, double maximumAmount, InvoiceStatus invoiceStatus) {
        getToSearch();
        setBCSearchInvoicesAccountNumber(accountNumber);
        setBCSearchInvoicesMinimumAmount(minimumAmount);
        setBCSearchInvoicesMaximumAmount(maximumAmount);
        clickSearch();
        String invoiceAccountNumberGridColumnID = tableUtils.getGridColumnFromTable(table_BCSearchInvoicesSearchResultsTable, "Account #");
        WebElement invoiceItemRow = selectRandomInvoiceItemRow(invoiceStatus, accountNumber);
        invoiceAccountNumberGridColumnID = tableUtils.getGridColumnFromTable(table_BCSearchInvoicesSearchResultsTable, "Account #");
        return invoiceItemRow.findElement(By.xpath(".//parent::td/parent::tr/td[(contains(@class,'" + invoiceAccountNumberGridColumnID + "'))]/div")).getText();
    }

    public String searchForAccountByAmountRangeAndInvoiceAndDateRange(Date earliestDate, Date latestDate, String accountNumber, double minimumAmount, double maximumAmount, InvoiceStatus invoiceStatus) {
        getToSearch();
        setBCSearchInvoicesAccountNumber(accountNumber);
        setBCSearchInvoicesMinimumAmount(minimumAmount);
        setBCSearchInvoicesMaximumAmount(maximumAmount);
        setBCSearchInvoicesEarliestDate(earliestDate);
        setBCSearchInvoicesLatestDate(latestDate);
        clickSearch();
        String invoiceAccountNumberGridColumnID = tableUtils.getGridColumnFromTable(table_BCSearchInvoicesSearchResultsTable, "Account #");
        WebElement invoiceItemRow = selectRandomInvoiceItemRow(invoiceStatus, accountNumber);
        invoiceAccountNumberGridColumnID = tableUtils.getGridColumnFromTable(table_BCSearchInvoicesSearchResultsTable, "Account #");
        return invoiceItemRow.findElement(By.xpath(".//parent::td/parent::tr/td[(contains(@class,'" + invoiceAccountNumberGridColumnID + "'))]/div")).getText();
    }

    public String findInvoiceByAccountAndAmountRange(String accountNumber, double minimumAmount, double maximumAmount, InvoiceStatus invoiceStatus) {
        getToSearch();
        setBCSearchInvoicesAccountNumber(accountNumber);
        setBCSearchInvoicesMinimumAmount(minimumAmount);
        setBCSearchInvoicesMaximumAmount(maximumAmount);
        clickSearch();
        String invoiceAccountNumberGridColumnID = tableUtils.getGridColumnFromTable(table_BCSearchInvoicesSearchResultsTable, "Account #");
        WebElement invoiceItemRow = selectRandomInvoiceItemRow(invoiceStatus, accountNumber);
        invoiceAccountNumberGridColumnID = tableUtils.getGridColumnFromTable(table_BCSearchInvoicesSearchResultsTable, "Account #");
        String fullAccountNumber = invoiceItemRow.findElement(By.xpath(".//parent::td/parent::tr/td[(contains(@class,'" + invoiceAccountNumberGridColumnID + "'))]/div")).getText();
        tableUtils.clickLinkInTableByRowAndColumnName(table_BCSearchInvoicesSearchResultsTable, tableUtils.getRowNumberFromWebElementRow(invoiceItemRow), "Invoice #");
        return fullAccountNumber;
    }

    public String getSearchInvoicesInvoiceNumber(int rowNumber) {
        return table_BCSearchInvoicesSearchResultsTable.findElement(By.xpath(".//a[contains(@id,':" + (rowNumber - 1) + ":InvoiceNumber')]")).getText();
    }

    public void clickSearchInvoicesInvoiceNumber(int rowNumber) {
        table_BCSearchInvoicesSearchResultsTable.findElement(By.xpath(".//a[contains(@id,'" + (rowNumber - 1) + ":InvoiceNumber')]")).click();
    }

    public void setBCSearchInvoicesAccountNumber(String accountNumber) {
        clickReset();
        clickWhenClickable(editbox_BCSearchInvoicesAccountNumber);
        editbox_BCSearchInvoicesAccountNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"), accountNumber);
    }

    public void setBCSearchInvoicesAccountNumberWithoutResetting(String accountNumber) {
        clickWhenClickable(editbox_BCSearchInvoicesAccountNumber);
        editbox_BCSearchInvoicesAccountNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"), accountNumber);
    }

    public void setBCSearchInvoicesFirstName(String firstName) {
        clickWhenClickable(editBox_BCSearchInvoicesFirstName);
        editBox_BCSearchInvoicesFirstName.sendKeys(Keys.chord(Keys.CONTROL + "a"), firstName);
    }

    public void setBCSearchInvoicesLastName(String lastName) {
        clickWhenClickable(editBox_BCSearchInvoicesLastName);
        editBox_BCSearchInvoicesLastName.sendKeys(Keys.chord(Keys.CONTROL + "a"), lastName);
    }

    public void setBCSearchInvoicesCity(String city) {
        clickWhenClickable(editBox_BCSearchInvoicesCity);
        editBox_BCSearchInvoicesCity.sendKeys(Keys.chord(Keys.CONTROL + "a"), city);
    }

    public void setBCSearchInvoicesState(State stateToSelect) {
        Guidewire8Select bcSearchAccountsState = select_BCSearchInvoicesState();
        bcSearchAccountsState.selectByVisibleText(stateToSelect.getName());
    }

    public void setBCSearchInvoicesZip(String zipPostalCode) {
        clickWhenClickable(editBox_BCSearchInvoicesZip);
        editBox_BCSearchInvoicesZip.sendKeys(Keys.chord(Keys.CONTROL + "a"), zipPostalCode);
    }

    public void setBCSearchInvoicesEarliestDate(Date date) {
        waitUntilElementIsVisible(editbox_BCSearchInvoicesEarliestDate);
        editbox_BCSearchInvoicesEarliestDate.sendKeys(Keys.CONTROL + "a");
        editbox_BCSearchInvoicesEarliestDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", date));
    }

    public void setBCSearchInvoicesLatestDate(Date date) {
        waitUntilElementIsVisible(editbox_BCSearchInvoicesLatestDate);
        editbox_BCSearchInvoicesLatestDate.sendKeys(Keys.CONTROL + "a");
        editbox_BCSearchInvoicesLatestDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", date));
    }

    public void setBCSearchInvoicesMinimumAmount(double minimumAmount) {
        clickWhenVisible(editBox_BCSearchInvoicesMinimumAmount);
        editBox_BCSearchInvoicesMinimumAmount.sendKeys(Keys.CONTROL + "a");
        editBox_BCSearchInvoicesMinimumAmount.sendKeys(String.valueOf(minimumAmount));
    }

    public void setBCSearchInvoicesMaximumAmount(double maximumAmount) {
        clickWhenVisible(editBox_BCSearchInvoicesMaximumAmount);
        editBox_BCSearchInvoicesMaximumAmount.sendKeys(Keys.CONTROL + "a");
        editBox_BCSearchInvoicesMaximumAmount.sendKeys(String.valueOf(maximumAmount));
    }
}

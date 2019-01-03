package repository.bc.search;

import com.idfbins.enums.State;
import com.idfbins.enums.YesOrNo;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentLocation;
import repository.gw.enums.PaymentType;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;

import java.util.Date;
import java.util.HashMap;

public class BCSearchPayment extends BasePage {

    private TableUtils tableUtils;
    private WebDriver driver;

    public BCSearchPayment(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------
    @FindBy(xpath = "//input[contains(@id, ':AccountNumberCriterion-inputEl')]")
    private WebElement editbox_BCSearchPaymentsAccountNumber;

    @FindBy(xpath = "//a[@id='PaymentSearch:PaymentSearchScreen:PaymentSearchDV:AccountNumberCriterion:SelectAccountNumberCriterion']")
    private WebElement link_BCSearchPaymentsAccountPicker;

    @FindBy(xpath = "//input[contains(@id, ':PolicyNumberCriterion-inputEl')]")
    private WebElement editBox_BCSearchPaymentsPolicyNumber;

    @FindBy(xpath = "//a[@id='PaymentSearch:PaymentSearchScreen:PaymentSearchDV:PolicyNumberCriterion:SelectPolicyNumberCriterion']")
    private WebElement link_BCSearchPaymentsPolicyPicker;

    private Guidewire8Select select_BCSearchPaymentsPaymentMethod() {
        return new Guidewire8Select(driver, "//table[contains(@id,':PaymentSearchDV:MethodCriterion-triggerWrap')]");
    }

    @FindBy(xpath = "//input[@id='PaymentSearch:PaymentSearchScreen:PaymentSearchDV:TokenCriterion-inputEl']")
    private WebElement editbox_BCSearchPaymentsPaymentInstrumentToken;

    @FindBy(xpath = "//input[contains(@id, ':MinAmountCriterion-inputEl')]")
    private WebElement editBox_BCSearchPaymentsMinimumAmount;

    @FindBy(xpath = "//input[contains(@id, ':MaxAmountCriterion-inputEl')]")
    private WebElement editBox_BCSearchPaymentsMaximumAmount;

    @FindBy(xpath = "//input[@id='PaymentSearch:PaymentSearchScreen:PaymentSearchDV:EarliestDateCriterion-inputEl']")
    private WebElement editbox_BCSearchPaymentsEarliestCreatedDate;

    @FindBy(xpath = "//input[@id='PaymentSearch:PaymentSearchScreen:PaymentSearchDV:LatestDateCriterion-inputEl']")
    private WebElement editbox_BCSearchPaymentsLatestCreatedDate;

    @FindBy(xpath = "//input[@id='PaymentSearch:PaymentSearchScreen:PaymentSearchDV:ReversalEarliestDateCriterion-inputEl']")
    private WebElement editbox_BCSearchPaymentsEarliestReversalDate;

    @FindBy(xpath = "//input[@id='PaymentSearch:PaymentSearchScreen:PaymentSearchDV:ReversalLatestDateCriterion-inputEl']")
    private WebElement editbox_BCSearchPaymentsLatestReversalDate;

    @FindBy(xpath = "//input[contains(@id, ':Name-inputEl')]")
    private WebElement editBox_BCSearchPaymentsCompanyName;

    @FindBy(xpath = "//input[contains(@id, ':FirstName-inputEl')]")
    private WebElement editBox_BCSearchPaymentsFirstName;

    @FindBy(xpath = "//input[contains(@id, ':LastName-inputEl')]")
    private WebElement editBox_BCSearchPaymentsLastName;

    @FindBy(xpath = "//input[contains(@id, ':City-inputEl')]")
    private WebElement editBox_BCSearchPaymentsCity;

    private Guidewire8Select select_BCSearchPaymentsState() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':State-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id, ':PostalCode-inputEl')]")
    private WebElement editBox_BCSearchPaymentsZip;

    @FindBy(xpath = "//input[@id='PaymentSearch:PaymentSearchScreen:PaymentSearchDV:CheckNumberCriterion-inputEl']")
    private WebElement editbox_BCSearchPaymentsCheckNumber;

    @FindBy(xpath = "//a[@id='PaymentSearch:PaymentSearchScreen:PaymentSearchResultsTab']")
    private WebElement link_BCSearchPaymentsDirectBillPaymentTab;

    @FindBy(xpath = "//div[@id='PaymentSearch:PaymentSearchScreen:PaymentSearchResultsLV']")
    private WebElement table_BCSearchPaymentsSearchResults;

    @FindBy(xpath = "//div[@id='PaymentSearch:PaymentSearchScreen:SuspensePaymentSearchResultsLV']")
    private WebElement table_BCSearchPaymentsSearchResultsSuspensePaymentsTable;

    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------

    public void setBCSearchPaymentsAccountNumber(String accountNumber) {
        clickReset();
        clickWhenClickable(editbox_BCSearchPaymentsAccountNumber);
        editbox_BCSearchPaymentsAccountNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"), accountNumber);
    }

    public void setBCSearchPaymentsAccountNumberWithoutResetting(String accountNumber) {
        clickWhenClickable(editbox_BCSearchPaymentsAccountNumber);
        editbox_BCSearchPaymentsAccountNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"), accountNumber);
    }

    public void setBCSearchPaymentsPolicyNumber(String policyNumber) {
        waitUntilElementIsClickable(editBox_BCSearchPaymentsPolicyNumber);
        editBox_BCSearchPaymentsPolicyNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        if (policyNumber.length() == 12) {
            editBox_BCSearchPaymentsPolicyNumber.sendKeys(policyNumber);
        } else {
            editBox_BCSearchPaymentsPolicyNumber.sendKeys(StringsUtils.formatPolicyNumber(policyNumber));
        }
    }

    public void setBCSearchDisbursementsPaymentMethod(PaymentType method) {
        select_BCSearchPaymentsPaymentMethod().selectByVisibleText(method.getValue());
    }

    public void setBCSearchDisbursementsMinimumAmount(double minimumAmount) {
        clickWhenVisible(editBox_BCSearchPaymentsMinimumAmount);
        editBox_BCSearchPaymentsMinimumAmount.sendKeys(Keys.CONTROL + "a");
        editBox_BCSearchPaymentsMinimumAmount.sendKeys(String.valueOf(minimumAmount));
    }

    public void setBCSearchDisbursementsMaximumAmount(double maximumAmount) {
        clickWhenVisible(editBox_BCSearchPaymentsMaximumAmount);
        editBox_BCSearchPaymentsMaximumAmount.sendKeys(Keys.CONTROL + "a");
        editBox_BCSearchPaymentsMaximumAmount.sendKeys(String.valueOf(maximumAmount));
    }

    public void setBCSearchPaymentsEarliestCreateDate(String date) {
        editbox_BCSearchPaymentsEarliestCreatedDate.sendKeys(Keys.CONTROL + "a");
        editbox_BCSearchPaymentsEarliestCreatedDate.sendKeys(date);
    }

    public void setBCSearchPaymentsLatestCreateDate(String date) {
        editbox_BCSearchPaymentsLatestCreatedDate.sendKeys(Keys.CONTROL + "a");
        editbox_BCSearchPaymentsLatestCreatedDate.sendKeys(date);
    }

    public void setBCSearchPaymentsEarliestReversalDate(String date) {
        editbox_BCSearchPaymentsEarliestReversalDate.sendKeys(Keys.CONTROL + "a");
        editbox_BCSearchPaymentsEarliestReversalDate.sendKeys(date);
    }

    public void setBCSearchPaymentsLatestReversalDate(String date) {
        editbox_BCSearchPaymentsLatestReversalDate.sendKeys(Keys.CONTROL + "a");
        editbox_BCSearchPaymentsLatestReversalDate.sendKeys(date);
    }

    public void setBCSearchAccountsCompanyName(String companyName) {
        waitUntilElementIsClickable(editBox_BCSearchPaymentsCompanyName);
        if (companyName.length() > 30) {
            editBox_BCSearchPaymentsCompanyName.sendKeys(Keys.chord(Keys.CONTROL + "a"), companyName.substring(0, 29));
        } else {
            editBox_BCSearchPaymentsCompanyName.sendKeys(Keys.chord(Keys.CONTROL + "a"), companyName);
        }
    }

    public void setBCSearchAccountsFirstName(String firstName) {
        clickWhenClickable(editBox_BCSearchPaymentsFirstName);
        editBox_BCSearchPaymentsFirstName.sendKeys(Keys.chord(Keys.CONTROL + "a"), firstName);
    }

    public void setBCSearchAccountsLastName(String lastName) {
        clickWhenClickable(editBox_BCSearchPaymentsLastName);
        editBox_BCSearchPaymentsLastName.sendKeys(Keys.chord(Keys.CONTROL + "a"), lastName);
    }

    public void setBCSearchAccountsCity(String city) {
        clickWhenClickable(editBox_BCSearchPaymentsCity);
        editBox_BCSearchPaymentsCity.sendKeys(Keys.chord(Keys.CONTROL + "a"), city);
    }

    public void setBCSearchAccountsState(State stateToSelect) {
        Guidewire8Select bcSearchAccountsState = select_BCSearchPaymentsState();
        bcSearchAccountsState.selectByVisibleText(stateToSelect.getName());
    }

    public void setBCSearchAccountsZip(String zipPostalCode) {
        clickWhenClickable(editBox_BCSearchPaymentsZip);
        editBox_BCSearchPaymentsZip.sendKeys(Keys.chord(Keys.CONTROL + "a"), zipPostalCode);
    }

    public void clickBCSearchPaymentsDirectBillPaymentTab() {
        clickWhenVisible(link_BCSearchPaymentsDirectBillPaymentTab);
    }

    public WebElement getBCSearchPaymentsSearchResultsTable() {
        waitUntilElementIsVisible(table_BCSearchPaymentsSearchResults);
        return table_BCSearchPaymentsSearchResults;
    }

    public WebElement getBCSearchPaymentsSearchResultsSuspenseTable() {
        waitUntilElementIsVisible(table_BCSearchPaymentsSearchResultsSuspensePaymentsTable);
        return table_BCSearchPaymentsSearchResultsSuspensePaymentsTable;
    }

    public WebElement getBCSearchPaymentsSearchResultsTableRow(Date paymentDate, Double amount, String accountNumber, String policyNumber, String description, YesOrNo distributed, PaymentInstrumentEnum paymentMethod, String refNumber, YesOrNo oneTime, PaymentLocation paymentLocation, Date reversalDate) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        if (paymentDate != null) {
            columnRowKeyValuePairs.put("Payment Date", DateUtils.dateFormatAsString("MM/dd/yyyy", paymentDate));
        }
        if (amount != null) {
            columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(amount));
        }
        if (accountNumber != null) {
            columnRowKeyValuePairs.put("Account #", accountNumber);
        }
        if (policyNumber != null) {
            columnRowKeyValuePairs.put("Policy", policyNumber);
        }
        if (description != null) {
            columnRowKeyValuePairs.put("Description", description);
        }
        if (distributed != null) {
            columnRowKeyValuePairs.put("Distributed", distributed.getValue());
        }
        if (paymentMethod != null) {
            columnRowKeyValuePairs.put("Payment Method", paymentMethod.getValue());
        }
        if (refNumber != null) {
            columnRowKeyValuePairs.put("Ref #", refNumber);
        }
        if (oneTime != null) {
            columnRowKeyValuePairs.put("One-Time", oneTime.getValue());
        }
        if (paymentLocation != null) {
            columnRowKeyValuePairs.put("Payment Location", paymentLocation.getValue());
        }
        if (reversalDate != null) {
            columnRowKeyValuePairs.put("Reversal Date", DateUtils.dateFormatAsString("MM/dd/yyyy", reversalDate));
        }
        return tableUtils.getRowInTableByColumnsAndValues(table_BCSearchPaymentsSearchResults, columnRowKeyValuePairs);
    }

    public String getBCSearchPaymentsEarliestCreatedDate() {
        return editbox_BCSearchPaymentsEarliestCreatedDate.getAttribute("value").toString();
    }

    public String getBCSearchPaymentsLatestCreatedDate() {
        return editbox_BCSearchPaymentsLatestCreatedDate.getAttribute("value").toString();
    }

    public void clickBCSearchPaymentsDateCriteriaPicker(String dateLabel) {
        find(By.xpath("//label[text()='" + dateLabel + "']/parent::td/following-sibling::td/table/tbody/tr/td/div[contains(@class,'-date-trigger')]")).click();
    }

    public void selectBCSearchPaymentsDateFromDatePicker(Date date) {
        find(By.xpath("//table[contains(@id,'udatetimepicker-')]/tbody/tr/td[@title = '" + DateUtils.dateFormatAsString("MMM dd, yyyy", date) + "']")).click();
    }
}

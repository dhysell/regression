package repository.bc.account.actions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;

import java.util.Date;
import java.util.HashMap;

public class NewLienholderPayment extends BasePage {

    private TableUtils tableUtils;
    private WebDriver driver;

    public NewLienholderPayment(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    /////////////////////
    // Recorded Elements//
    /////////////////////

    @FindBy(xpath = "//div[contains(@id,'NewListBillPaymentWizard:NewListBillPaymentSelectInvoiceScreen:DetailPanel:0')]")
    public WebElement table_NewLienholderPaymentInvoiceTable;

    @FindBy(xpath = "//div[contains(@id,'NewListBillPaymentWizard:NewListBillPaymentSelectInvoiceScreen:DetailPanel:InvoiceItemsLV')]")
    public WebElement table_NewLienholderPaymentInvoiceItemsTable;

    @FindBy(xpath = "//span[contains(@id,'NewListBillPaymentWizard:NewListBillPaymentSelectInvoiceScreen:DetailPanel:InvoiceDetailCardTab-btnInnerEl')]")
    public WebElement tab_InvoiceTab;

    @FindBy(xpath = "//a[contains(@id,':ToolbarButton')]")
    public WebElement button_NewLienholderPaymentContinueWithoutSelecting;

    public Guidewire8Select select_NewLienholderPaymentInvoiceFilter() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'DetailPanel:DateFilter-triggerWrap')]");
    }

    //////////////////
    // Helper Methods//
    //////////////////

    public WebElement getInvoiceTableRow(Date invoiceDate, Date dueDate, InvoiceType invoiceType, InvoiceStatus status, Double billedAmount, Double dueAmount) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        if (invoiceDate != null) {
            columnRowKeyValuePairs.put("Invoice Date", DateUtils.dateFormatAsString("MM/dd/yyyy", invoiceDate));
        }
        if (dueDate != null) {
            columnRowKeyValuePairs.put("Due Date", DateUtils.dateFormatAsString("MM/dd/yyyy", dueDate));
        }
        if (invoiceType != null) {
            columnRowKeyValuePairs.put("Invoice Type", invoiceType.getValue());
        }
        if (status != null) {
            columnRowKeyValuePairs.put("Status", status.getValue());
        }
        if (billedAmount != null) {
            columnRowKeyValuePairs.put("Billed Amount", StringsUtils.currencyRepresentationOfNumber(billedAmount));
        }
        if (dueAmount != null) {
            columnRowKeyValuePairs.put("Amount Due", StringsUtils.currencyRepresentationOfNumber(dueAmount));
        }

        WebElement tableRow = tableUtils.getRowInTableByColumnsAndValues(table_NewLienholderPaymentInvoiceTable, columnRowKeyValuePairs);
        return tableRow;
    }


    public void selectInvoiceTableItem(Date invoiceDate, Date dueDate, InvoiceType invoiceType, InvoiceStatus status, Double billedAmount, Double dueAmount) {
        WebElement tableRow = getInvoiceTableRow(invoiceDate, dueDate, invoiceType, status, billedAmount, dueAmount);
        tableUtils.setCheckboxInTable(table_NewLienholderPaymentInvoiceTable, tableUtils.getRowNumberFromWebElementRow(tableRow), true);
    }
}

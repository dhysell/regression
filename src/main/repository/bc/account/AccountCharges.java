package repository.bc.account;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.bc.common.BCCommonCharges;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.enums.TransactionNumber;
import repository.gw.enums.TransactionType;
import repository.gw.helpers.TableUtils;

import java.util.HashMap;

public class AccountCharges extends BCCommonCharges {

    private WebDriver driver;
    public AccountCharges(WebDriver driver) {
    	super(driver);
    	this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    //////////////////////////////////////////
    // "Recorded Elements" and their XPaths //
    //////////////////////////////////////////
    @FindBy(xpath = "//a[contains(@id,'InvoiceItemsLV_tb:EditInvoiceItems')]")
    public WebElement button_ModifyInvoiceItems;

    @FindBy(xpath = "//a[contains(@id,'InvoiceItemsLV_tb:MoveInvoiceItems')]")
    public WebElement button_MoveInvoiceItems;

    public Guidewire8Checkbox checkBox_MarkChargeForSpreading() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id,'MarkForSpreadingChargePopup:MarkForSpreadingChargeDetailScreen:MarkedForSpreading')]");
    }

    //////////////////////////////////////
    // Helper Methods for Above Elements//
    //////////////////////////////////////

    public void clickModifyInvoiceItems() {
        clickWhenClickable(button_ModifyInvoiceItems);
    }

    public boolean checkIfModifyInvoiceItemsButtonIsDisabled() {
        return isElementDisabled(button_ModifyInvoiceItems);
    }

    public void clickMoveInvoiceItems() {
        clickWhenClickable(button_MoveInvoiceItems);
    }

    public boolean checkIfMoveInvoiceItemsButtonIsDisabled() {
        return isElementDisabled(button_MoveInvoiceItems);
    }
    
    public void clickEditSplitMoveCharges(String defaultPayer, TransactionNumber transaction, TransactionType context, String policyNumber) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        columnRowKeyValuePairs.put("Default Payer", defaultPayer);
        columnRowKeyValuePairs.put("Transaction #", transaction.getValue());
        columnRowKeyValuePairs.put("Context", context.getValue());
        columnRowKeyValuePairs.put("Policy", policyNumber);
        new TableUtils(getDriver()).clickLinkInSpecficRowInTable(table_ChargesOrChargeHoldsPopup, new TableUtils(getDriver()).getRowNumberFromWebElementRow(new TableUtils(getDriver()).getRowInTableByColumnsAndValues(table_ChargesOrChargeHoldsPopup, columnRowKeyValuePairs)));
        clickWhenClickable(find(By.linkText("Edit / Split / Move Charge")));
    }
}

package repository.bc.account;

import com.idfbins.enums.YesOrNo;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.InvoiceStatus;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;

import java.util.Date;
import java.util.HashMap;

public class AccountChargesMoveInvoiceItems extends BasePage {

	public AccountChargesMoveInvoiceItems(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	//////////////////////////////////////////
	// "Recorded Elements" and their XPaths //
	//////////////////////////////////////////

	@FindBy(xpath = "//div[contains(@id,'MoveInvoiceItemsPopup:targetInvoicesLV:InvoiceSelectorSimpleLV')]")
	public WebElement table_ChargesMoveInvoiceItemsDestinationInvoice;

	//////////////////////////////////////
	// Helper Methods for Above Elements//
	//////////////////////////////////////
	
	public WebElement getDestinationInvoiceTableRow(InvoiceStatus status, Date statementDate, Date invoiceDueDate, String invoiceNumber, String invoiceStream, YesOrNo paidStatus, Double invoiceAmount, Double dueAmount) {
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
		if (status != null) {
			columnRowKeyValuePairs.put("Status", status.getValue());
		}
		if (statementDate != null) {
			columnRowKeyValuePairs.put("Statement Date", DateUtils.dateFormatAsString("MM/dd/yyyy", statementDate));
		}
		if (invoiceDueDate != null) {
			columnRowKeyValuePairs.put("Due Date", DateUtils.dateFormatAsString("MM/dd/yyyy", invoiceDueDate));
		}
		if (invoiceNumber != null) {
			columnRowKeyValuePairs.put("Invoice Number", invoiceNumber);
		}
		if (invoiceStream != null) {
			columnRowKeyValuePairs.put("Invoice Stream", invoiceStream);
		}
		if (paidStatus != null) {
			columnRowKeyValuePairs.put("Paid Status", paidStatus.getValue());
		}
		if (invoiceAmount != null) {
			columnRowKeyValuePairs.put("Invoice Amount", StringsUtils.currencyRepresentationOfNumber(invoiceAmount));
		}
		if (dueAmount != null) {
			columnRowKeyValuePairs.put("Paid Amount", StringsUtils.currencyRepresentationOfNumber(dueAmount));
		}
		return new TableUtils(getDriver()).getRowInTableByColumnsAndValues(table_ChargesMoveInvoiceItemsDestinationInvoice, columnRowKeyValuePairs);
	}

	public void clickSelectButton(InvoiceStatus status, Date statementDate, Date invoiceDueDate, String invoiceNumber, String invoiceStream, YesOrNo paidStatus, Double invoiceAmount, Double dueAmount) {
		WebElement paymentRow = getDestinationInvoiceTableRow(status, statementDate, invoiceDueDate, invoiceNumber, invoiceStream, paidStatus, invoiceAmount, dueAmount);
		new TableUtils(getDriver()).clickSelectLinkInTable(table_ChargesMoveInvoiceItemsDestinationInvoice, new TableUtils(getDriver()).getRowNumberFromWebElementRow(paymentRow));
	}

}

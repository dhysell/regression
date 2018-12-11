package repository.bc.account.payments;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.gw.enums.Status;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;

import java.util.Date;
import java.util.HashMap;

import static org.testng.Assert.fail;

public class AccountPaymentsPaymentRequests extends AccountPayments {
	
	private TableUtils tableUtils;
	
	public AccountPaymentsPaymentRequests(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		this.tableUtils = new TableUtils(driver);
	}

	// -----------------------------------------------
	// "Recorded Elements" and their XPaths
	// -----------------------------------------------
	
	// Main Payment Request Page
	@FindBy(xpath = "//div[@id='AccountPaymentRequests:AccountDetailPaymentsScreen:RequestsListLV']")
	public WebElement table_PaymentRequestsTable;
	
	@FindBy(xpath = "//a[contains(@id, ':editRequest')]")
	private WebElement button_Edit;
	
	//Edit Payment Request Page
	@FindBy(xpath = "//input[contains(@id, 'PaymentRequestDetailPage:PaymentRequestDV:amount-inputEl')]")
	private WebElement editBox_Amount;
	
	// -------------------------------------------------------
	// Helper Methods for Above Elements
	// -------------------------------------------------------

	public boolean paymentRequestExists (Date invoiceDate, Date dueDate, String invoiceNumber, Double amount) {
		boolean requestExists = false;
		try {
			getPaymentRequestRow(null, invoiceDate, null, null, null, dueDate, invoiceNumber, null, null, amount);
			requestExists = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return requestExists;
	}

	public int getPaymentRequestTableRowCount() {
		int rowCount = tableUtils.getRowCount(table_PaymentRequestsTable);
		return rowCount;
	}
	
	public WebElement getPaymentRequestRow (Status status, Date invoiceDate, Date requestDate, Date changeDeadlineDate, Date draftDate, Date dueDate, String invoiceNumber, String policyNumber, String paymentInstrument, Double amount) {
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
		if (status != null) {
			columnRowKeyValuePairs.put("Status", status.getValue());
		}
		if (invoiceDate != null) {
			columnRowKeyValuePairs.put("Invoice Date", DateUtils.dateFormatAsString("MM/dd/yyyy", invoiceDate));
		}
		if (requestDate != null) {
			columnRowKeyValuePairs.put("Request Date", DateUtils.dateFormatAsString("MM/dd/yyyy", requestDate));
		}
		if (changeDeadlineDate != null) {
			columnRowKeyValuePairs.put("Change Deadline Date", DateUtils.dateFormatAsString("MM/dd/yyyy", changeDeadlineDate));
		}
		if (draftDate != null) {
			columnRowKeyValuePairs.put("Draft Date", DateUtils.dateFormatAsString("MM/dd/yyyy", draftDate));
		}
		if (dueDate != null) {
			columnRowKeyValuePairs.put("Due Date", DateUtils.dateFormatAsString("MM/dd/yyyy", dueDate));
		}
		if (invoiceNumber != null) {
			columnRowKeyValuePairs.put("Invoice", invoiceNumber);
		}
		if (policyNumber != null) {
			columnRowKeyValuePairs.put("Policy", policyNumber);
		}
		if (paymentInstrument != null) {
			columnRowKeyValuePairs.put("Payment Instrument", paymentInstrument);
		}
		if (amount != null) {
			columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(amount));
		}
		return tableUtils.getRowInTableByColumnsAndValues(table_PaymentRequestsTable, columnRowKeyValuePairs);
	}
	
	public boolean verifyPaymentRequest(Status status, Date invoiceDate, Date requestDate, Date changeDeadlineDate, Date draftDate, Date dueDate, String invoiceNumber, String policyNumber, String paymentInstrument, Double amount) {
		boolean found = false;
		try {
			getPaymentRequestRow(status, invoiceDate, requestDate, changeDeadlineDate, draftDate, dueDate, invoiceNumber, policyNumber, paymentInstrument, amount);
			found = true;
		} catch (Exception e) {
			found = false;
		}
		return found;
	}
	
	public Status getPaymentRequestStatus (Date invoiceDate, Date dueDate, String invoiceNumber, Double amount) {
		WebElement rowResult = getPaymentRequestRow(null, invoiceDate, null, null, null, dueDate, invoiceNumber, null, null, amount);
		return Status.valueOf(tableUtils.getCellTextInTableByRowAndColumnName(table_PaymentRequestsTable, rowResult, "Status"));
	}
	
	public void checkPaymentRequestStatus (Date invoiceDate, Date dueDate, String invoiceNumber, Double amount, Status status) {
		if (!getPaymentRequestStatus(invoiceDate, dueDate, invoiceNumber, amount).equals(status)) {
			fail("Payment Request Status was not " + status);
		} 
	}
	
	public double getPaymentRequestAmount (Status status, Date invoiceDate, Date dueDate, String invoiceNumber) {
		WebElement rowResult = getPaymentRequestRow(status, invoiceDate, null, null, null, dueDate, invoiceNumber, null, null, null);
		return NumberUtils.getCurrencyValueFromElement(tableUtils.getCellTextInTableByRowAndColumnName(table_PaymentRequestsTable, rowResult, "Amount"));
	}
	
	public void editPaymentAmount (Date invoiceDate, Date dueDate, String invoiceNumber, Double amount, String newAmount) {
		setCheckBox(Status.Created, invoiceDate, null, null, null, dueDate, invoiceNumber, null, null, amount, true);
		button_Edit.click();
		clickWhenVisible(editBox_Amount);
		setText(editBox_Amount, newAmount);
		clickUpdate();
		checkPaymentAmount(Status.Created, invoiceDate, dueDate, null, newAmount);
	}
	
	public void setCheckBox (Status status, Date invoiceDate, Date requestDate, Date changeDeadlineDate, Date draftDate, Date dueDate, String invoiceNumber, String policyNumber, String paymentInstrument, Double amount, boolean setCheckboxTrueFalse) {
		WebElement paymentRow = getPaymentRequestRow(status, invoiceDate, requestDate, changeDeadlineDate, draftDate, dueDate, invoiceNumber, policyNumber, paymentInstrument, amount);
		tableUtils.setCheckboxInTable(table_PaymentRequestsTable, tableUtils.getRowNumberFromWebElementRow(paymentRow), setCheckboxTrueFalse);		
	}
	
	public void setCheckBox (WebElement paymentRequestRow, boolean setCheckboxTrueFalse) {		
		tableUtils.setCheckboxInTable(table_PaymentRequestsTable, tableUtils.getRowNumberFromWebElementRow(paymentRequestRow), setCheckboxTrueFalse);		
	}
	
	// -------------------------------------------------------
	// Private Methods for Above Methods
	// -------------------------------------------------------
	
	private void checkPaymentAmount (Status status, Date invoiceDate, Date dueDate, String invoiceNumber, String newAmount) {
		String newPaymentAmount = String.valueOf(getPaymentRequestAmount(status, invoiceDate, dueDate, invoiceNumber));
		if (!newAmount.contains(newPaymentAmount)) {
			fail("New Payment Request Amount was not " + newAmount);
		} 
	}	
}

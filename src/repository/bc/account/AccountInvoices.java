package repository.bc.account;

import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.*;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AccountInvoices extends BasePage {

    private TableUtils tableUtils;
    private WebDriver driver;

    public AccountInvoices(WebDriver driver) {
        super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
	}

	////////////////////////////////////////
	// "Recorded Elements" and their XPaths//
	////////////////////////////////////////

	@FindBy(xpath = "//div[@id='AccountDetailInvoices:AccountDetailInvoicesScreen:DetailPanel:AccountInvoicesLV']")
	public WebElement table_AccountInvoicesTable;

	@FindBy(xpath = "//div[@id='AccountDetailInvoices:AccountDetailInvoicesScreen:DetailPanel:InvoiceItemsLV']")
	public WebElement table_AccountInvoiceChargesTable;

	@FindBy(xpath = "//input[contains(@id,':InvoiceDetailDV:PaymentDueDate')]")
	public WebElement editbox_InvoiceDueDate;

	@FindBy(xpath = "//input[contains(@id,':DetailPanel:InvoiceDetailDV:InvoiceDate')]")
	public WebElement editbox_InvoiceDate;

	@FindBy(xpath = "//a[@id='AccountDetailInvoices:AccountDetailInvoicesScreen:DetailPanel:InvoiceDetailDV_tb:Edit']")
	public WebElement button_AccountInvoiceChangeInvoiceDate;

	@FindBy(xpath = "//a[contains(@id,':AccountInvoicesLV_tb:AccountDetailInvoices_NewInvoiceButton')]")
	public WebElement button_CreateNewInvoice;

	@FindBy(xpath = "//a[contains(@id,':AccountInvoicesLV_tb:AccountDetailInvoices_RemoveInvoicesButton')]")
	public WebElement button_DeleteInvoice;

	@FindBy(xpath = "//a[contains(@id,':InvoiceDetailDV_tb:InvoiceDetailDV_ResendInvoice')]")
	public WebElement button_ResendInvoice;

	public Guidewire8Select select_InvoiceType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':InvoiceDetailDV:invoicetype-triggerWrap')]");
	}

	public Guidewire8Select select_AccountInvoiceStream() {
        return new Guidewire8Select(driver, "//table[contains(@id,':AccountInvoicesLV_tb:InvoiceStreamFilter-triggerWrap')]");
	}

	public Guidewire8Select select_InvoicePolicyPeriodFilter() {
		return new Guidewire8Select(driver, "//table[contains(@id,':AccountInvoicesLV_tb:FilterByPolicySelector-triggerWrap')]");
	}

	///////////////////////////////////////
	// Helper Methods for Above Elements //
	///////////////////////////////////////

	public void selectPolicyPeriod(String policyPeriod) {
		Guidewire8Select mySelect = select_InvoicePolicyPeriodFilter();
		mySelect.selectByVisibleTextPartial(policyPeriod);
	}


	public WebElement getAccountInvoicesTable() {
		waitUntilElementIsVisible(table_AccountInvoicesTable);
		return table_AccountInvoicesTable;
	}

	public WebElement getAccountInvoiceChargesTable() {
		waitUntilElementIsVisible(table_AccountInvoiceChargesTable);
		return table_AccountInvoiceChargesTable;
	}

	// This method was converted from qawizpro. Please edit it accordingly.
	public static double policyChangeFutureInvoiceAmount(double actualPremChange, Date changeDate, Date expDate) {
		int diffDays = DateUtils.getDifferenceBetweenDates(expDate, changeDate, DateDifferenceOptions.Day);
		double DailyPremium = actualPremChange / diffDays;

		Date oneYearEarlierEnd = DateUtils.dateAddSubtract(expDate, DateAddSubtractOptions.Year, -1);
		int daysDiffBtExpDateAndOneYearEarlier = DateUtils.getDifferenceBetweenDates(expDate, oneYearEarlierEnd, DateDifferenceOptions.Day);
		double yearlyPremium = DailyPremium * daysDiffBtExpDateAndOneYearEarlier;
		return NumberUtils.round(yearlyPremium, 2);
	}

	public int getInvoiceTableRowCount() {
        int rowCount = tableUtils.getRowCount(table_AccountInvoicesTable);
		return rowCount;
	}

	public int getInvoiceChargesTableRowCount() {
        int rowCount = tableUtils.getRowCount(table_AccountInvoiceChargesTable);
		return rowCount;
	}

	public WebElement getAccountInvoiceTableRow(Date invoiceDate, Date dueDate, String invoiceNumber, InvoiceType invoiceType, String invoiceStream, InvoiceStatus invoiceStatus, Double invoiceAmount, Double invoiceDue) {
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
		if (invoiceDate != null) {
			columnRowKeyValuePairs.put("Invoice Date", DateUtils.dateFormatAsString("MM/dd/yyyy", invoiceDate));
		}
		if (dueDate != null) {
			columnRowKeyValuePairs.put("Due Date", DateUtils.dateFormatAsString("MM/dd/yyyy", dueDate));
		}
		if (invoiceNumber != null) {
			columnRowKeyValuePairs.put("Invoice Number", invoiceNumber);
		}
		if (invoiceType != null) {
			columnRowKeyValuePairs.put("Invoice Type", invoiceType.getValue());
		}
		if (invoiceStream != null) {
			columnRowKeyValuePairs.put("Invoice Stream", invoiceStream);
		}
		if (invoiceStatus != null) {
			columnRowKeyValuePairs.put("Status", invoiceStatus.getValue());
		}
		if (invoiceAmount != null) {
			columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(invoiceAmount));
		}
		if (invoiceDue != null) {
			columnRowKeyValuePairs.put("Due", StringsUtils.currencyRepresentationOfNumber(invoiceDue));
		}
        return tableUtils.getRowInTableByColumnsAndValues(table_AccountInvoicesTable, columnRowKeyValuePairs);
	}

	public WebElement getAccountInvoiceChargesTableRow(String installmentNumber, Date eventDate, String policyNumber, ChargeCategory chargeCategory, TransactionType chargeContext, String chargeGroup, String chargeDescription, ProductLineType product, Double chargeAmount, Double paidAmount, String payerAddress, String deliveryOptions) {
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
		if (installmentNumber != null) {
			columnRowKeyValuePairs.put("Installment #", installmentNumber);
		}
		if (eventDate != null) {
			columnRowKeyValuePairs.put("Event Date", DateUtils.dateFormatAsString("MM/dd/yyyy", eventDate));
		}
		if (policyNumber != null) {
			columnRowKeyValuePairs.put("Policy", policyNumber);
		}
		if (chargeCategory != null) {
			columnRowKeyValuePairs.put("Category", chargeCategory.getValue());
		}
		if (chargeContext != null) {
			columnRowKeyValuePairs.put("Context", chargeContext.getValue());
		}
		if (chargeGroup != null) {
			columnRowKeyValuePairs.put("Charge Group", chargeGroup);
		}
		if (chargeDescription != null) {
			columnRowKeyValuePairs.put("Description", chargeDescription);
		}
		if (product != null) {
			columnRowKeyValuePairs.put("Product", product.getBCName());
		}
		if (chargeAmount != null) {
			columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(chargeAmount));
		}
		if (paidAmount != null) {
			columnRowKeyValuePairs.put("Paid Amount", StringsUtils.currencyRepresentationOfNumber(paidAmount));
		}
		if (payerAddress != null) {
			columnRowKeyValuePairs.put("Payer Address", payerAddress);
		}
		if (deliveryOptions != null) {
			columnRowKeyValuePairs.put("Delivery Options", deliveryOptions);
		}
        return tableUtils.getRowInTableByColumnsAndValues(table_AccountInvoiceChargesTable, columnRowKeyValuePairs);
	}

	public boolean verifyInvoiceCharges(String installmentNumber, Date eventDate, String policyNumber, ChargeCategory chargeCategory, TransactionType chargeContext, String chargeGroup, String chargeDescription, ProductLineType product, Double chargeAmount, Double paidAmount, String payerAddress, String deliveryOptions) {
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
		if (installmentNumber != null) {
			columnRowKeyValuePairs.put("Installment #", installmentNumber);
		}
		if (eventDate != null) {
			columnRowKeyValuePairs.put("Event Date", DateUtils.dateFormatAsString("MM/dd/yyyy", eventDate));
		}
		if (policyNumber != null) {
			columnRowKeyValuePairs.put("Policy", policyNumber);
		}
		if (chargeCategory != null) {
			columnRowKeyValuePairs.put("Category", chargeCategory.getValue());
		}
		if (chargeContext != null) {
			columnRowKeyValuePairs.put("Context", chargeContext.getValue());
		}
		if (chargeGroup != null) {
			columnRowKeyValuePairs.put("Charge Group", chargeGroup);
		}
		if (chargeDescription != null) {
			columnRowKeyValuePairs.put("Description", chargeDescription);
		}
		if (product != null) {
			columnRowKeyValuePairs.put("Product", product.getBCName());
		}
		if (chargeAmount != null) {
			columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(chargeAmount));
		}
		if (paidAmount != null) {
			columnRowKeyValuePairs.put("Paid Amount", StringsUtils.currencyRepresentationOfNumber(paidAmount));
		}
		if (payerAddress != null) {
			columnRowKeyValuePairs.put("Payer Address", payerAddress);
		}
		if (deliveryOptions != null) {
			columnRowKeyValuePairs.put("Delivery Options", deliveryOptions);
		}
        List<WebElement> tableRows = tableUtils.getRowsInTableByColumnsAndValues(table_AccountInvoiceChargesTable, columnRowKeyValuePairs);
		return tableRows.size() == 1;
	}

	public void clickRowByInvoiceDate(Date invoiceDate) {
        tableUtils.clickRowInTableByRowNumber(table_AccountInvoicesTable, tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(table_AccountInvoicesTable, "Invoice Date", DateUtils.dateFormatAsString("MM/dd/yyyy", invoiceDate))));
	}

	public void clickAccountInvoiceTableRow(Date invoiceDate, Date dueDate, String invoiceNumber, InvoiceType invoiceType, String invoiceStream, InvoiceStatus invoiceStatus, Double invoiceAmount, Double invoiceDue) {
		getAccountInvoiceTableRow(invoiceDate, dueDate, invoiceNumber, invoiceType, invoiceStream, invoiceStatus, invoiceAmount, invoiceDue).click();
	}

	public void clickAccountInvoicesTableRowByRowNumber(int rowNumber) {
        tableUtils.clickRowInTableByRowNumber(table_AccountInvoicesTable, rowNumber);
	}

	public String getInvoiceTableCellValue(String headerColumn, Date invoiceDate, Date dueDate, String invoiceNumber, InvoiceType invoiceType, String invoiceStream, InvoiceStatus status, Double amount, Double due) {
		WebElement tableRow = getAccountInvoiceTableRow(invoiceDate, dueDate, invoiceNumber, invoiceType, invoiceStream, status, amount, due);
        return tableUtils.getCellTextInTableByRowAndColumnName(table_AccountInvoicesTable, tableUtils.getRowNumberFromWebElementRow(tableRow), headerColumn);
	}

	public String getInvoiceChargesTableCellValue(int rowNumber, String headerColumn) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_AccountInvoiceChargesTable, rowNumber, headerColumn);
	}

	public String getInvoiceNumber(Date invoiceDate, Date dueDate, Double invoiceAmount,InvoiceType invoiceType) {
		WebElement tableRowContainingInvoiceNumber = getAccountInvoiceTableRow(invoiceDate, dueDate, null, invoiceType, null, null, invoiceAmount, null);
        return tableUtils.getCellTextInTableByRowAndColumnName(table_AccountInvoicesTable, tableUtils.getRowNumberFromWebElementRow(tableRowContainingInvoiceNumber), "Invoice Number");
	}

	public String getInvoiceNumberByRowNumber(int rowNumber) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_AccountInvoicesTable, rowNumber, "Amount");
	}

	public double getInvoiceAmountByInvoiceNumber(String invoiceNumber) {
		return NumberUtils.getCurrencyValueFromElement(tableUtils.getCellTextInTableByRowAndColumnName(table_AccountInvoicesTable, tableUtils.getRowInTableByColumnNameAndValue(table_AccountInvoicesTable, "Invoice Number", invoiceNumber), "Amount"));
	}

    public String getInvoiceChargesTableCellValue(String headerColumn, String installmentNumber, Date eventDate, String policyNumber, ChargeCategory chargeCategory, TransactionType chargeContext, String chargeGroup, String chargeDescription, ProductLineType product, Double chargeAmount, Double paidAmount, String payerAddress, String deliveryOptions) {
		WebElement tableRow = getAccountInvoiceChargesTableRow(installmentNumber, eventDate, policyNumber, chargeCategory, chargeContext, chargeGroup, chargeDescription, product, chargeAmount, paidAmount, payerAddress, deliveryOptions);
        return tableUtils.getCellTextInTableByRowAndColumnName(table_AccountInvoiceChargesTable, tableUtils.getRowNumberFromWebElementRow(tableRow), headerColumn);
	}

    public double getInvoiceAmountByRowNumber(int rowNumber) {
        return NumberUtils.getCurrencyValueFromElement(tableUtils.getCellTextInTableByRowAndColumnName(table_AccountInvoicesTable, rowNumber, "Amount"));
	}

	public double getInvoiceDueAmountByRowNumber(int rowNumber) {
        return NumberUtils.getCurrencyValueFromElement(tableUtils.getCellTextInTableByRowAndColumnName(table_AccountInvoicesTable, rowNumber, "Due"));
	}

    public double getInvoiceAmountByInvoiceDate(Date invoiceDate) {
        return NumberUtils.getCurrencyValueFromElement(tableUtils.getCellTextInTableByRowAndColumnName(table_AccountInvoicesTable, tableUtils.getRowInTableByColumnNameAndValue(table_AccountInvoicesTable, "Invoice Date", DateUtils.dateFormatAsString("MM/dd/yyyy", invoiceDate)), "Amount"));
    }

    public double getInvoiceDueAmountByDueDate(Date dueDate) {
        return NumberUtils.getCurrencyValueFromElement(tableUtils.getCellTextInTableByRowAndColumnName(table_AccountInvoicesTable, tableUtils.getRowInTableByColumnNameAndValue(table_AccountInvoicesTable, "Due Date", DateUtils.dateFormatAsString("MM/dd/yyyy", dueDate)), "Due"));
	}

	public Date getInvoiceDateByInvoiceType(InvoiceType invoiceType) throws Exception {
        return DateUtils.convertStringtoDate(tableUtils.getCellTextInTableByRowAndColumnName(table_AccountInvoicesTable, tableUtils.getRowInTableByColumnNameAndValue(table_AccountInvoicesTable, "Invoice Type", invoiceType.getValue()), "Invoice Date"), "MM/dd/yyyy");
	}

	public Date getInvoiceDueDateByInvoiceType(InvoiceType invoiceType) throws Exception {
        return DateUtils.convertStringtoDate(tableUtils.getCellTextInTableByRowAndColumnName(table_AccountInvoicesTable, tableUtils.getRowInTableByColumnNameAndValue(table_AccountInvoicesTable, "Invoice Type", invoiceType.getValue()), "Due Date"), "MM/dd/yyyy");
	}

    public void selectInvoiceType(InvoiceType type) {
		select_InvoiceType().selectByVisibleText(type.getValue());
	}

	public void changeInvoiceDateTo(Date invoiceDate) {
		editbox_InvoiceDate.sendKeys(Keys.CONTROL + "a");
		editbox_InvoiceDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", invoiceDate));
		clickProductLogo();
	}

	public void changeDueDateTo(Date dueDate) {
		editbox_InvoiceDueDate.sendKeys(Keys.CONTROL + "a");
		editbox_InvoiceDueDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", dueDate));
		clickProductLogo();
	}

    public int getInvoiceTableRowCountOfPlannedPositiveInvoices() {
		int rowCount = 0;
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
		columnRowKeyValuePairs.put("Status", InvoiceStatus.Planned.getValue());
		columnRowKeyValuePairs.put("Amount", "not gw-currency-negative");
        rowCount = tableUtils.getRowsInTableByColumnsAndValues(table_AccountInvoicesTable, columnRowKeyValuePairs).size();
		return rowCount;
	}

	public double getMembershipDuesAlreadyPaid() {
		double totalMembershipDuesPaid = 0;
        List<WebElement> elementsToCheck = tableUtils.getRowsInTableByColumnNameAndValue(table_AccountInvoicesTable, "Status", "not " + InvoiceStatus.Planned.getValue());
		for (WebElement element : elementsToCheck) {
			clickWhenClickable(element);
			WebElement chargeTableRow = getAccountInvoiceChargesTableRow(null, null, null, ChargeCategory.Membership_Dues, null, null, null, null, null, null, null, null);
            totalMembershipDuesPaid += NumberUtils.getCurrencyValueFromElement(tableUtils.getCellTextInTableByRowAndColumnName(table_AccountInvoiceChargesTable, chargeTableRow, "Amount"));
		}
        tableUtils.clickFirstRowInTable(table_AccountInvoicesTable);
		return totalMembershipDuesPaid;
	}

    public void setInvoiceStream(String statusToSelect) {
		select_AccountInvoiceStream().selectByVisibleTextPartial(statusToSelect);
	}

	public boolean checkIfChangeInvoiceDateButtonIsDisabled() {
		return isElementDisabled(button_AccountInvoiceChangeInvoiceDate);
	}

    public void clickChangeInvoiceDateButton() {
		clickWhenVisible(button_AccountInvoiceChangeInvoiceDate);
	}

	public boolean checkIfPolicyLinkIsActive() {
        WebElement policyNumberLinkElement = tableUtils.getCellWebElementInTableByRowAndColumnName(table_AccountInvoiceChargesTable, 1, "Policy");
        return new GuidewireHelpers(getDriver()).checkIfLinkIsActive(policyNumberLinkElement);
	}

	public boolean checkIfCreateNewInvoiceButtonIsDisabled() {
		return isElementDisabled(button_CreateNewInvoice);
	}

    public void clickCreateNewInvoice() {
		clickWhenVisible(button_CreateNewInvoice);
		
	}

    public boolean checkIfDeleteInvoiceButtonIsDisabled() {
		return isElementDisabled(button_DeleteInvoice);
	}

	public void clickDeleteInvoice() {
		clickWhenVisible(button_DeleteInvoice);
		
	}

	public boolean checkIfResendInvoiceButtonIsDisabled() {
		return isElementDisabled(button_ResendInvoice);
	}

	public void clickResendInvoice() {
		clickWhenVisible(button_ResendInvoice);
		
	}

    public InvoiceStatus getInvoiceStatus() {
        int rowNumber = tableUtils.getHighlightedRowNumber(table_AccountInvoicesTable);
        return InvoiceStatus.valueOf(tableUtils.getCellTextInTableByRowAndColumnName(table_AccountInvoicesTable, rowNumber, "Status"));
	}

    public InvoiceStatus getInvoiceStatusByInvoiceType(InvoiceType invoiceType) {
        return InvoiceStatus.valueOf(tableUtils.getCellTextInTableByRowAndColumnName(table_AccountInvoicesTable, tableUtils.getRowInTableByColumnNameAndValue(table_AccountInvoicesTable, "Invoice Type", invoiceType.getValue()), "Status"));
    }

    public boolean verifyFlatPolicyCancellation(GeneratePolicy policyObj) {
		int errorCount = 0;
        int tableRowCount = tableUtils.getRowCount(table_AccountInvoicesTable);
		if (tableRowCount != 2) {
			errorCount++;
			System.out.println("Invoice Break Down table has wrong row count.");
		}
		HashMap<String, String> invoiceTotalsSearchColumnRowKeyValuePairs = new HashMap<String, String>();
		invoiceTotalsSearchColumnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(0.00));
		invoiceTotalsSearchColumnRowKeyValuePairs.put("Due", StringsUtils.currencyRepresentationOfNumber(0.00));
		HashMap<String, String> invoiceLineColumnRowKeyValuePairs = new HashMap<String, String>();
		invoiceLineColumnRowKeyValuePairs.put("Invoice Type", InvoiceType.NewBusinessDownPayment.getValue());
		invoiceLineColumnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(0.00));
		invoiceLineColumnRowKeyValuePairs.put("Due", StringsUtils.currencyRepresentationOfNumber(0.00));

        List<WebElement> invoiceTotalsSearchRows = tableUtils.getRowsInTableByColumnsAndValues(table_AccountInvoicesTable, invoiceTotalsSearchColumnRowKeyValuePairs);
        List<WebElement> invoiceLineRows = tableUtils.getRowsInTableByColumnsAndValues(table_AccountInvoicesTable, invoiceLineColumnRowKeyValuePairs);
		if (invoiceTotalsSearchRows.size() < 1) {
			errorCount++;
			System.out.println("The invoice Due amount is not correct");
		}
		if (invoiceLineRows.size() < 1) {
			errorCount++;
			System.out.println("The Invoice line found did not contain the correct information");
		}
		return errorCount > 0;
	}

    public void verifyDelinquencyCancellationInvoicingRollup(Date invoiceDate, double unbilledAmount, double amountDue) {
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
		columnRowKeyValuePairs.put("Invoice Date", DateUtils.dateFormatAsString("MM/dd/yyyy", invoiceDate));
		columnRowKeyValuePairs.put("Invoice Type", InvoiceType.NewBusinessDownPayment.getValue());
		columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(unbilledAmount));

        List<WebElement> invoiceTotalsSearchRows = tableUtils.getRowsInTableByColumnNameAndValue(table_AccountInvoicesTable, "Due", StringsUtils.currencyRepresentationOfNumber(amountDue));
		if (invoiceTotalsSearchRows.size() < 1) {
            Assert.fail("The total amount due did not match the delinquent amount.");
		}

        List<WebElement> invoiceShortageRollupLineRows = tableUtils.getRowsInTableByColumnsAndValues(table_AccountInvoicesTable, columnRowKeyValuePairs);
		if (invoiceShortageRollupLineRows.size() < 1) {
            Assert.fail("The invoices did not roll-up to one final invoice as expected.");
		}

        double mostRecentInvoiceDueLineRows = NumberUtils.getCurrencyValueFromElement(tableUtils.getCellTextInTableByRowAndColumnName(table_AccountInvoicesTable, invoiceShortageRollupLineRows.get(0), "Due"));
		if (mostRecentInvoiceDueLineRows != amountDue) {
            Assert.fail("The most recent invoice before delinquency was not in a 'due' status.");
		}
	}

    public boolean verifyInvoice(InvoiceType invoiceType, InvoiceStatus status, Double amount) {
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        if (invoiceType != null) {
        columnRowKeyValuePairs.put("Invoice Type", invoiceType.getValue());
        }
        if (status != null) {
        columnRowKeyValuePairs.put("Status", status.getValue());
        }
        if (amount != null) {
        columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(amount));
        }
        List<WebElement> tableRows = tableUtils.getRowsInTableByColumnsAndValues(table_AccountInvoicesTable, columnRowKeyValuePairs);
		return tableRows.size() == 1;
	}

	public boolean verifyInvoice(Date invoiceDate, Date dueDate, String invoiceNumber, InvoiceType invoiceType, String invoiceStream, InvoiceStatus status, Double amount, Double due) {
		boolean found = false;
		try {
			getAccountInvoiceTableRow(invoiceDate, dueDate, invoiceNumber, invoiceType, invoiceStream, status, amount, due);
			found = true;
		} catch (Exception e) {
			found = false;
		}
		return found;
	}

    public List<Date> getListOfInvoiceDates() {
		List<Date> listOfInvoiceDates = new ArrayList<Date>();
        List<String> listOfInvoiceDatesAsStrings = tableUtils.getAllCellTextFromSpecificColumn(table_AccountInvoicesTable, "Invoice Date");
        for (int i = 0; i < listOfInvoiceDatesAsStrings.size(); i++) {
            try {
                listOfInvoiceDates.add(DateUtils.convertStringtoDate((listOfInvoiceDatesAsStrings.get(i)), "MM/dd/yyyy"));
            } catch (ParseException e) {
                e.printStackTrace();
                Assert.fail("There was a problem parsing the string to a date. Test cannot continue.");
            }
        }
        System.out.println(listOfInvoiceDates.size());
        return listOfInvoiceDates;
    }

	public List<Date> getListOfDueDates() {
		List<Date> listOfDueDates = new ArrayList<Date>();
        List<String> listOfDueDatesAsStrings = tableUtils.getAllCellTextFromSpecificColumn(table_AccountInvoicesTable, "Due Date");
		for (int i = 0; i < listOfDueDatesAsStrings.size(); i++) {
			try {
				listOfDueDates.add(DateUtils.convertStringtoDate((listOfDueDatesAsStrings.get(i)), "MM/dd/yyyy"));
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.fail("There was a problem parsing the string to a date. Test cannot continue.");
			}
		}
		return listOfDueDates;
	}

	public List<Date> getListOfDueDatesByInvoiceStream(String invoiceStreamPolicyNumber) {
		setInvoiceStream(invoiceStreamPolicyNumber);
		
		List<Date> listOfDueDates = new ArrayList<Date>();
        List<String> listOfDueDatesAsStrings = tableUtils.getAllCellTextFromSpecificColumn(table_AccountInvoicesTable, "Due Date");
		for (int i = 0; i < listOfDueDatesAsStrings.size(); i++) {
			try {
				listOfDueDates.add(DateUtils.convertStringtoDate((listOfDueDatesAsStrings.get(i)), "MM/dd/yyyy"));
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.fail("There was a problem parsing the string to a date. Test cannot continue.");
			}
		}
		return listOfDueDates;
	}

	public List<Double> getListOfInvoiceDueAmounts() {
		List<Double> listOfInvoiceDueAmounts = new ArrayList<Double>();
        List<String> listOfInvoiceDueAmountsAsStrings = tableUtils.getAllCellTextFromSpecificColumn(table_AccountInvoicesTable, "Due");
		for (int i = 0; i < listOfInvoiceDueAmountsAsStrings.size(); i++) {
			listOfInvoiceDueAmounts.add(NumberUtils.getCurrencyValueFromElement(listOfInvoiceDueAmountsAsStrings.get(i)));
		}
		return listOfInvoiceDueAmounts;
	}

	public List<Double> getListOfInvoiceAmounts() {
		List<Double> listOfInvoiceAmounts = new ArrayList<Double>();
        List<String> listOfInvoiceAmountsAsStrings = tableUtils.getAllCellTextFromSpecificColumn(table_AccountInvoicesTable, "Amount");
		for (int i = 0; i < listOfInvoiceAmountsAsStrings.size(); i++) {
			listOfInvoiceAmounts.add(NumberUtils.getCurrencyValueFromElement(listOfInvoiceAmountsAsStrings.get(i)));
		}
		return listOfInvoiceAmounts;
	}

	public List<InvoiceType> getListOfInvoiceTypes() {
		List<InvoiceType> listOfInvoiceTypes = new ArrayList<InvoiceType>();
        List<String> listOfInvoiceTypesAsStrings = tableUtils.getAllCellTextFromSpecificColumn(table_AccountInvoicesTable, "Invoice Type");
		for (int i = 0; i < listOfInvoiceTypesAsStrings.size(); i++) {
			listOfInvoiceTypes.add(InvoiceType.valueOf(listOfInvoiceTypesAsStrings.get(i)));
		}
		return listOfInvoiceTypes;
	}

    public List<String> getListOfInvoiceNumbers() {
        List<String> listOfInvoiceNumbers = tableUtils.getAllCellTextFromSpecificColumn(table_AccountInvoicesTable, "Invoice Number");
        return listOfInvoiceNumbers;
    }

    /**
	 * calculate and set the Due Date list based on the policy information
	 */
	public List<Date> calculateListOfDueDates(Date effectiveDate, Date expDate, PaymentPlanType paymentPlan) throws ParseException {
		int planOffSet = paymentPlan.getNumberOfMonthsBetweenPaymentPeriods();
		int invoiceCount = paymentPlan.getNumberOfPaymentPeriods();

		List<Date> dueDateList = new ArrayList<Date>();
        Date policyBoundDate = DateUtils.convertStringtoDate(tableUtils.getCellTextInTableByRowAndColumnName(table_AccountInvoicesTable, 1, "Due Date"), "MM/dd/yyyy");
        Date bcSystemDate = DateUtils.getCenterDate(getDriver(), ApplicationOrCenter.BillingCenter);
		boolean firstInvoice = false;
		for (int i = 0; i < invoiceCount; i++) {
			Date currentDate = DateUtils.dateAddSubtract(expDate, DateAddSubtractOptions.Month, -(i + 1) * planOffSet);
			int numDays = DateUtils.getDifferenceBetweenDatesAbsoluteValue(DateUtils.getDateValueOfFormat(currentDate, "MM/dd/yyyy"), DateUtils.getDateValueOfFormat(effectiveDate, "MM/dd/yyyy"), DateDifferenceOptions.Day);
			if (numDays > 21) {

			} else {
				numDays = DateUtils.getDifferenceBetweenDatesAbsoluteValue(DateUtils.getDateValueOfFormat(currentDate, "MM/dd/yyyy"), DateUtils.getDateValueOfFormat(bcSystemDate, "MM/dd/yyyy"), DateDifferenceOptions.Day);
				if (numDays >= 0) {

				} else {
					if (paymentPlan.getValue().equals("Monthly") && firstInvoice)
						break;
					else
						currentDate = bcSystemDate;
				}
				numDays = DateUtils.getDifferenceBetweenDatesAbsoluteValue(DateUtils.getDateValueOfFormat(currentDate, "MM/dd/yyyy"), DateUtils.getDateValueOfFormat(policyBoundDate, "MM/dd/yyyy"), DateDifferenceOptions.Day);
				if (numDays > 0) {
					currentDate = policyBoundDate;
					firstInvoice = true;
				}
			}

			System.out.println("Current date is: " + DateUtils.dateFormatAsString("MM/dd/yyyy", currentDate));
			dueDateList.add(0, currentDate);
		}
		return dueDateList;
	}

	/**
	 * calculate and set the Invoice Date list based on the Due date list
	 */

	public List<Date> calculateListOfInvoiceDates(List<Date> dueDateList, PaymentPlanType paymentPlan) {
		int leadTime = paymentPlan.getInvoicingLeadTime();

		ArrayList<Date> invoiceDateList = new ArrayList<Date>();
		invoiceDateList.add(0, dueDateList.get(0));
		for (int i = 1; i < dueDateList.size(); i++) {
			invoiceDateList.add(i, DateUtils.dateAddSubtract(dueDateList.get(i), DateAddSubtractOptions.Day, leadTime));
		}
		return invoiceDateList;
	}

    /**
	 * This method calculates and sets the Invoice Date and Due Date based on the info from the policy, and verifies the Invoice Date and Due Date from BillingCenter Invoices Screen with the calculated dates.
     *
	 * @throws ParseException
     * @throws Exception
	 */
    public void verifyInvoiceAndDueDate(Date effectiveDate, Date expDate, PaymentPlanType paymentPlan) throws ParseException {
		int i;
		List<Date> calculatedDueDate, calculatedInvoiceDate, invoiceDateFromBC, dueDateFromBC;
		calculatedDueDate = calculateListOfDueDates(effectiveDate, expDate, paymentPlan);
		calculatedInvoiceDate = calculateListOfInvoiceDates(calculatedDueDate, paymentPlan);
		for (i = 0; i < calculatedInvoiceDate.size(); i++) {
			System.out.println("Calculated Invoice Date: " + DateUtils.dateFormatAsString("MM/dd/yyyy", calculatedInvoiceDate.get(i)) + "    Due Date:" + DateUtils.dateFormatAsString("MM/dd/yyyy", calculatedDueDate.get(i)));
		}
		invoiceDateFromBC = getListOfInvoiceDates();
		dueDateFromBC = getListOfDueDates();
		for (i = 0; i < invoiceDateFromBC.size(); i++) {
			System.out.println("Invoice Date from BC: " + DateUtils.dateFormatAsString("MM/dd/yyyy", invoiceDateFromBC.get(i)) + "    Due Date:" + DateUtils.dateFormatAsString("MM/dd/yyyy", dueDateFromBC.get(i)));
		}

		if (calculatedInvoiceDate.size() != invoiceDateFromBC.size() || calculatedDueDate.size() != dueDateFromBC.size()) {
            Assert.fail("The calculated invoice/due date count is " + calculatedInvoiceDate.size() + ", while the invoice/due date count in the UI is " + invoiceDateFromBC.size());
		}

		i = (calculatedInvoiceDate.size() <= invoiceDateFromBC.size()) ? calculatedInvoiceDate.size() : invoiceDateFromBC.size();
		for (int loop = 0; loop < i; loop++) {
			if (!DateUtils.dateFormatAsString("MM/dd/yyyy", calculatedInvoiceDate.get(loop)).equals(DateUtils.dateFormatAsString("MM/dd/yyyy", invoiceDateFromBC.get(loop)))) {
                Assert.fail("On Invoice #" + (loop + 1) + " the calculated Invoice Date is " + DateUtils.dateFormatAsString("MM/dd/yyyy", calculatedInvoiceDate.get(loop)) + ", while the Invoice Date in the UI is " + DateUtils.dateFormatAsString("MM/dd/yyyy", invoiceDateFromBC.get(loop)));
			}

			if (!DateUtils.dateFormatAsString("MM/dd/yyyy", calculatedDueDate.get(loop)).equals(DateUtils.dateFormatAsString("MM/dd/yyyy", dueDateFromBC.get(loop)))) {
                Assert.fail("On Invoice #" + (loop + 1) + " the calculated Due Date is " + DateUtils.dateFormatAsString("MM/dd/yyyy", calculatedDueDate.get(loop)) + ", while the Due Date in the UI is " + DateUtils.dateFormatAsString("MM/dd/yyyy", dueDateFromBC.get(loop)));
			}
		}
	}

	/**
	 * This method calculates and sets the invoicing "Amount" and "Due" values based on the info from the policy, and verifies the Amount and Due values from BillingCenter Invoices Screen with the calculated amounts.
     *
     * @throws Exception
	 */
    public void verifyInvoiceAmount(String acctNum, double premiumAmount, double membershipFee, PaymentPlanType paymentPlan) {
		double[][] invAmtAndDueArrayFromBC = null;
		double[][] calculatedInvAmtAndDueArray;
		calculatedInvAmtAndDueArray = createBillAndDueArrayMemberfeeSpread(premiumAmount, membershipFee, paymentPlan);
		invAmtAndDueArrayFromBC = createInvBillAndDueAmountArrayFromBC(acctNum);

		if (calculatedInvAmtAndDueArray.length != invAmtAndDueArrayFromBC.length) {
            Assert.fail("The expected number of invoices is " + calculatedInvAmtAndDueArray.length + ", while the number of invoices in the UI is " + invAmtAndDueArrayFromBC.length);
		}

		for (int i = 0; i < calculatedInvAmtAndDueArray.length; i++) {
			System.out.println("Calculated Amount and Due are " + calculatedInvAmtAndDueArray[i][0] + " and " + calculatedInvAmtAndDueArray[i][1] + " ===== BC Amount and Due are " + invAmtAndDueArrayFromBC[i][0] + " and " + invAmtAndDueArrayFromBC[i][1]);

			if (calculatedInvAmtAndDueArray[i][0] != invAmtAndDueArrayFromBC[i][0]) {
                Assert.fail("On Invoice #" + (i + 1) + " the expected amount due is " + calculatedInvAmtAndDueArray[i][0] + ", while the amount due in the UI is " + invAmtAndDueArrayFromBC[i][0]);
			}

			if (calculatedInvAmtAndDueArray[i][1] != invAmtAndDueArrayFromBC[i][1]) {
                Assert.fail("On Invoice #" + (i + 1) + " the expected amount invoiced is " + calculatedInvAmtAndDueArray[i][1] + ", while the amount invoiced in the UI is " + invAmtAndDueArrayFromBC[i][1]);
			}
		}
	}

	/**
	 * calculate and set the invoicing Amount and Due values -- Membership fee is spread over the whole payment plan
	 */
	public static double[][] createBillAndDueArrayMemberfeeSpread(double premiumAmount, double membershipFee, PaymentPlanType paymentPlan) {
		double[][] invoiceAmountAndDueArray = new double[paymentPlan.getNumberOfPaymentPeriods()][2];
		double invoiceAmount = NumberUtils.round(((premiumAmount + membershipFee) / paymentPlan.getNumberOfPaymentPeriods()), 2);

		double leftAmount = premiumAmount - invoiceAmount * (invoiceAmountAndDueArray.length - 1);
		if (leftAmount > invoiceAmount) {
			invoiceAmountAndDueArray[0][0] = NumberUtils.round(leftAmount, 2);
			invoiceAmountAndDueArray[0][1] = NumberUtils.round(leftAmount, 2);
			invoiceAmountAndDueArray[invoiceAmountAndDueArray.length - 1][0] = NumberUtils.round(invoiceAmount, 2);
			invoiceAmountAndDueArray[invoiceAmountAndDueArray.length - 1][1] = NumberUtils.round(invoiceAmount, 2);
		} else if (leftAmount < invoiceAmount) {
			invoiceAmountAndDueArray[0][0] = NumberUtils.round(invoiceAmount, 2);
			invoiceAmountAndDueArray[0][1] = NumberUtils.round(invoiceAmount, 2);
			invoiceAmountAndDueArray[invoiceAmountAndDueArray.length - 1][0] = NumberUtils.round(leftAmount, 2);
			invoiceAmountAndDueArray[invoiceAmountAndDueArray.length - 1][1] = NumberUtils.round(leftAmount, 2);
		} else {// leftAmount= invoiceAmount
			invoiceAmountAndDueArray[0][0] = NumberUtils.round(invoiceAmount, 2);
			invoiceAmountAndDueArray[0][1] = NumberUtils.round(invoiceAmount, 2);
			invoiceAmountAndDueArray[invoiceAmountAndDueArray.length - 1][0] = NumberUtils.round(invoiceAmount, 2);
			invoiceAmountAndDueArray[invoiceAmountAndDueArray.length - 1][1] = NumberUtils.round(invoiceAmount, 2);
		}

		for (int i = 1; i < invoiceAmountAndDueArray.length - 1; i++) {
			invoiceAmountAndDueArray[i][0] = NumberUtils.round(invoiceAmount, 2);
			invoiceAmountAndDueArray[i][1] = NumberUtils.round(invoiceAmount, 2);
		}
		return invoiceAmountAndDueArray;
	}

	/**
	 * calculate and set the invoicing "Amount" values for BC -- Membership fee is not spread across invoices
	 */
	public static List<Double> createInvoiceAmountListMemberfeeNotSpread(double premiumAmount, double membershipFee, PaymentPlanType paymentPlan) {
		List<Double> calculatedInvoiceAmountList = new ArrayList<Double>();

		double invoiceAmount = NumberUtils.round((premiumAmount / paymentPlan.getNumberOfPaymentPeriods()), 2);

		double leftOver = premiumAmount;

		for (int i = 0; i < paymentPlan.getNumberOfPaymentPeriods() - 1; i++) {
			leftOver = leftOver - invoiceAmount;
			calculatedInvoiceAmountList.add(NumberUtils.round(invoiceAmount, 2));
		}

		if (leftOver > invoiceAmount) {
			calculatedInvoiceAmountList.add(0, NumberUtils.round((leftOver + membershipFee), 2));
		} else if (leftOver < invoiceAmount) {
			calculatedInvoiceAmountList.add(NumberUtils.round(leftOver, 2));
			calculatedInvoiceAmountList.set(0, NumberUtils.round((invoiceAmount + membershipFee), 2));
		} else {
			calculatedInvoiceAmountList.add(NumberUtils.round(invoiceAmount, 2));
		}
		return calculatedInvoiceAmountList;
	}

	/**
	 * get the values from "Amount" and "Due" columns from BC Invoice screen Modified by Jessica on April 21, 2016
	 */
	public double[][] createInvBillAndDueAmountArrayFromBC(String acctNumber) {
		double[][] invoiceAmountAndDueArray;
        int tableSize = tableUtils.getRowCount(table_AccountInvoicesTable);
		invoiceAmountAndDueArray = new double[tableSize][2]; // not include the footer row of the table
		for (int i = 1; i < tableSize; i++) {
			invoiceAmountAndDueArray[i][0] = getInvoiceAmountByRowNumber(i + 1); // modified on April 21 by Jessica, ohterwise when i=0, recordindex=-1
			invoiceAmountAndDueArray[i][1] = getInvoiceDueAmountByRowNumber(i + 1);// modified on April 21 by Jessica
		}
		return invoiceAmountAndDueArray;
	}

	/**
	 * get the values from "Amount" and "Due" columns from BC Invoice screen
	 */
    public List<List<Double>> getInvBillAndDueAmountListsFromBC(String acctNumber) {
        int tableSize = tableUtils.getRowCount(table_AccountInvoicesTable);
		ArrayList<Double> invoiceAmountBC = new ArrayList<Double>(12), invoiceDueBC = new ArrayList<Double>(12);
		for (int i = 1; i <= tableSize; i++) {
			invoiceAmountBC.add(getInvoiceAmountByRowNumber(i));
			invoiceDueBC.add(getInvoiceDueAmountByRowNumber(i));
		}
		System.out.println("\nThe invoice and due amounts for account " + acctNumber + " in BillingCenter Invoice Screen are: \n");
		for (int i = 0; i < invoiceAmountBC.size(); i++) {
			System.out.println(invoiceAmountBC.get(i) + "   " + invoiceDueBC.get(i) + "\n");
		}
		List<List<Double>> newInvoiceAmountAndDueLists = new ArrayList<List<Double>>();
		newInvoiceAmountAndDueLists.add(invoiceAmountBC);
		newInvoiceAmountAndDueLists.add(invoiceDueBC);
		return newInvoiceAmountAndDueLists;
	}

    public List<List<Double>> calculatedPolicyChangeInvoiceAndDueLists(GeneratePolicy mypolicy, double additionalPremium, Date changeDate, Date effectiveDate, Date ExpirationDate) {
		System.out.println("****** Calculating the new invoice and due amounts for account " + mypolicy.accountNumber + " ******");
        return afterPolicyChangeInvoiceAndDueLists(new GuidewireHelpers(getDriver()).getPolicyPremium(mypolicy).getInsuredPremium(), new GuidewireHelpers(getDriver()).getPolicyPremium(mypolicy).getMembershipDuesAmount(), additionalPremium, changeDate, effectiveDate, ExpirationDate, mypolicy.paymentPlanType);
	}

	public List<List<Double>> afterPolicyChangeInvoiceAndDueLists(double origPremium, double membershipFee, double additionalPremium, Date changeDate, Date effDate, Date expDate, PaymentPlanType paymentPlan) {
		List<Date> calculatedDueDate = null;
		double additionalPremEachInv = 0, leftOver;
		int leadTime = 0, denominator = 1, i;
		List<Double> calculatedInvAmt = createInvoiceAmountListMemberfeeNotSpread(origPremium, membershipFee, paymentPlan), calculatedDueAmt = createInvoiceDueList(origPremium, membershipFee, paymentPlan);
		System.out.println("\nthe invoice and due amounts after issurance are: \n");
		for (i = 0; i < calculatedInvAmt.size(); i++) {
			System.out.println(calculatedInvAmt.get(i) + "   " + calculatedDueAmt.get(i) + "\n");
		}
		double wholeYearPremForChange = policyChangeFutureInvoiceAmount(additionalPremium, changeDate, expDate);
		System.out.println("\nWhole year prem after change is : \n" + wholeYearPremForChange);
		leadTime = Math.abs(paymentPlan.getInvoicingLeadTime());
		denominator = paymentPlan.getNumberOfPaymentPeriods();
		try {
			calculatedDueDate = calculateListOfDueDates(effDate, expDate, paymentPlan);
		} catch (Exception e) {
			e.printStackTrace();
		}
		additionalPremEachInv = NumberUtils.round((wholeYearPremForChange / denominator), 2);
		int listSize = calculatedDueDate.size();
		// same day policy change
		if ((changeDate.getTime() / (24 * 60 * 60 * 1000) - calculatedDueDate.get(0).getTime() / (24 * 60 * 60 * 1000)) < 1) {
			if (paymentPlan.equals(PaymentPlanType.Monthly)) {
				leftOver = NumberUtils.round((additionalPremium - additionalPremEachInv * (listSize - 2)), 2);
				calculatedInvAmt.set(1, calculatedInvAmt.get(1) + leftOver);
				calculatedDueAmt.set(1, calculatedDueAmt.get(1) + leftOver);
			} else {
				leftOver = NumberUtils.round((additionalPremium - additionalPremEachInv * (listSize - 1)), 2);
				calculatedInvAmt.add(1, leftOver);
				calculatedDueAmt.add(1, leftOver);
			}
			for (i = 2; i < calculatedInvAmt.size(); i++) {// the list size might have already changed, some don't use listSize for i
				calculatedInvAmt.set(i, NumberUtils.round((calculatedInvAmt.get(i) + additionalPremEachInv), 2));
				calculatedDueAmt.set(i, NumberUtils.round((calculatedDueAmt.get(i) + additionalPremEachInv), 2));
			}
		} else {// other than same day policy change
			leftOver = additionalPremium;
			for (i = listSize - 1; i > 0; i--) {
				if (calculatedDueDate.get(i - 1).compareTo(changeDate) >= 0) {
					calculatedInvAmt.set(i, calculatedInvAmt.get(i) + additionalPremEachInv);
					calculatedDueAmt.set(i, calculatedDueAmt.get(i) + additionalPremEachInv);
					leftOver = leftOver - additionalPremEachInv;
				} else {
					calculatedDueAmt.set(i - 1, Double.valueOf(0.0));
					int diffDays = DateUtils.getDifferenceBetweenDates(DateUtils.getDateValueOfFormat(calculatedDueDate.get(i), "MM/dd/yyyy"), DateUtils.getDateValueOfFormat(changeDate, "MM/dd/yyyy"), DateDifferenceOptions.Day);
					if (diffDays <= leadTime) {
                        String statusCol = tableUtils.getGridColumnFromTable(table_AccountInvoicesTable, "Status");
						String invStatus = table_AccountInvoicesTable.findElement(By.xpath(".//tr[contains(@data-recordindex,'" + (i + 1) + "')]/td[contains(@class,'" + statusCol + "')]")).getText();

						if (invStatus.equals("Planned")) {
							calculatedInvAmt.set(i, calculatedInvAmt.get(i) + NumberUtils.round(leftOver, 2));
							calculatedDueAmt.set(i, calculatedDueAmt.get(i) + NumberUtils.round(leftOver, 2));

						} else {// billed for current row
							if (paymentPlan.equals(PaymentPlanType.Monthly)) {
								calculatedInvAmt.set(i + 1, calculatedInvAmt.get(i + 1) + NumberUtils.round(leftOver, 2));
								calculatedDueAmt.set(i + 1, calculatedDueAmt.get(i + 1) + NumberUtils.round(leftOver, 2));
							} else {// Quarterly, Semi_Annual, Annual
								calculatedInvAmt.add(i + 1, NumberUtils.round(leftOver, 2));
								calculatedDueAmt.add(i + 1, NumberUtils.round(leftOver, 2));
							}

						}
					} else {// diffDays>leadTime
						if (paymentPlan.equals(PaymentPlanType.Monthly)) {
							calculatedInvAmt.set(i, calculatedInvAmt.get(i + 1) + NumberUtils.round(leftOver, 2));
							calculatedDueAmt.set(i, calculatedDueAmt.get(i + 1) + NumberUtils.round(leftOver, 2));
						} else {// Quarterly, Semi_Annual, Annual
							Date possibleNewInvDueDate = DateUtils.dateAddSubtract(calculatedDueDate.get(i), DateAddSubtractOptions.Month, -1);
							if ((possibleNewInvDueDate.getTime() / (24 * 60 * 60 * 1000) - changeDate.getTime() / (24 * 60 * 60 * 1000)) >= leadTime) {

								calculatedInvAmt.add(i, NumberUtils.round((leftOver - additionalPremEachInv), 2));
								calculatedDueAmt.add(i, NumberUtils.round((leftOver - additionalPremEachInv), 2));
								calculatedInvAmt.set(i + 1, calculatedInvAmt.get(i + 1) + NumberUtils.round(additionalPremEachInv, 2));
								calculatedDueAmt.set(i + 1, calculatedDueAmt.get(i + 1) + NumberUtils.round(additionalPremEachInv, 2));

							} else {
								calculatedInvAmt.set(i, calculatedInvAmt.get(i) + NumberUtils.round(leftOver, 2));
								calculatedDueAmt.set(i, calculatedDueAmt.get(i) + NumberUtils.round(leftOver, 2));
							}
						}
					}
				}
			}
		}

		System.out.println("\nThe new invoice and due amounts after policy change are: \n");
		for (i = 0; i < calculatedInvAmt.size(); i++) {
			System.out.println(calculatedInvAmt.get(i) + "   " + calculatedDueAmt.get(i) + "\n");
		}
		List<List<Double>> newInvoiceAmountAndDueLists = new ArrayList<List<Double>>();
		newInvoiceAmountAndDueLists.add(calculatedInvAmt);
		newInvoiceAmountAndDueLists.add(calculatedDueAmt);
		return newInvoiceAmountAndDueLists;
	}

	public static ArrayList<Double> createInvoiceDueList(double premiumAmount, double membershipFee, PaymentPlanType paymentPlan) {
		ArrayList<Double> calculatedDueAmountList = new ArrayList<>();
		double invoiceAmount = NumberUtils.round((premiumAmount / paymentPlan.getNumberOfPaymentPeriods()), 2);
		double leftOver = premiumAmount;
		for (int i = 0; i < paymentPlan.getNumberOfPaymentPeriods() - 1; i++) {
			leftOver = leftOver - invoiceAmount;
			calculatedDueAmountList.add(NumberUtils.round(invoiceAmount, 2));
		}
		if (leftOver > invoiceAmount) {
			calculatedDueAmountList.add(0, NumberUtils.round((leftOver + membershipFee), 2));
		} else if (leftOver < invoiceAmount) {
			calculatedDueAmountList.add(NumberUtils.round(leftOver, 2));
			calculatedDueAmountList.set(0, NumberUtils.round((invoiceAmount + membershipFee), 2));
		} else {
			calculatedDueAmountList.add(NumberUtils.round(invoiceAmount, 2));
		}
		return calculatedDueAmountList;
	}

    public Date invoiceDate(Date dueDate, String invoiceNumber, InvoiceType invoiceType, String invoiceStream,InvoiceStatus invoiceStatus, Double invoiceAmount, Double invoiceDue )  {
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
		if (dueDate != null) {
			columnRowKeyValuePairs.put("Due Date", DateUtils.dateFormatAsString("MM/dd/yyyy", dueDate));
		}
		if (invoiceNumber != null) {
			columnRowKeyValuePairs.put("Invoice Number", invoiceNumber);
		}
		if (invoiceType != null) {
			columnRowKeyValuePairs.put("Invoice Type", invoiceType.getValue());
		}
		if (invoiceStream != null) {
			columnRowKeyValuePairs.put("Invoice Stream", invoiceStream);
		}
		if (invoiceStatus != null) {
			columnRowKeyValuePairs.put("Status", invoiceStatus.getValue());
		}
		if (invoiceAmount != null) {
			columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(invoiceAmount));
		}
		if (invoiceDue != null) {
			columnRowKeyValuePairs.put("Due", StringsUtils.currencyRepresentationOfNumber(invoiceDue));
		}
		try {
			WebElement tableRow = new TableUtils(getDriver()).getRowInTableByColumnsAndValues(table_AccountInvoicesTable, columnRowKeyValuePairs);
			clickWhenClickable(tableRow);
			String dateAsString = new TableUtils(getDriver()).getCellTextInTableByRowAndColumnName(table_AccountInvoicesTable, new TableUtils(getDriver()).getHighlightedRowNumber(table_AccountInvoicesTable), "Invoice Date");
			
		return DateUtils.convertStringtoDate(dateAsString, "MM/dd/yyyy");
		} catch (ParseException e) {
			e.printStackTrace();
            System.out.println("Was not able to find the requested value");
		return null;
		}
	}

    public Date dueDate(Date invoiceDate, String invoiceNumber, InvoiceType invoiceType, String invoiceStream,InvoiceStatus invoiceStatus, Double invoiceAmount, Double invoiceDue )  {
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
		if (invoiceDate != null) {
			columnRowKeyValuePairs.put("Invoice Date", DateUtils.dateFormatAsString("MM/dd/yyyy", invoiceDate));
		}
		if (invoiceNumber != null) {
			columnRowKeyValuePairs.put("Invoice Number", invoiceNumber);
		}
		if (invoiceType != null) {
			columnRowKeyValuePairs.put("Invoice Type", invoiceType.getValue());
		}
		if (invoiceStream != null) {
			columnRowKeyValuePairs.put("Invoice Stream", invoiceStream);
		}
		if (invoiceStatus != null) {
			columnRowKeyValuePairs.put("Status", invoiceStatus.getValue());
		}
		if (invoiceAmount != null) {
			columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(invoiceAmount));
		}
		if (invoiceDue != null) {
			columnRowKeyValuePairs.put("Due", StringsUtils.currencyRepresentationOfNumber(invoiceDue));
		}
		try {
			WebElement tableRow = new TableUtils(getDriver()).getRowInTableByColumnsAndValues(table_AccountInvoicesTable, columnRowKeyValuePairs);
			clickWhenClickable(tableRow);
			String dateAsString = new TableUtils(getDriver()).getCellTextInTableByRowAndColumnName(table_AccountInvoicesTable, new TableUtils(getDriver()).getHighlightedRowNumber(table_AccountInvoicesTable), "Due Date");
			
		return DateUtils.convertStringtoDate(dateAsString, "MM/dd/yyyy");
		} catch (ParseException e) {
			e.printStackTrace();
            System.out.println("Was not able to find the requested value, returning null");
		return null;
		}
	}

}
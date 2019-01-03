package repository.bc.account.payments;

import gwclockhelpers.ApplicationOrCenter;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.bc.account.BCAccountMenu;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentLocation;
import repository.gw.enums.PaymentReturnedPaymentReason;
import repository.gw.enums.PaymentReversalReason;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AccountPayments extends BasePage {

    private TableUtils tableUtils;
    private WebDriver driver;

    public AccountPayments(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
	}

	// -----------------------------------------------
	// "Recorded Elements" and their XPaths
	// -----------------------------------------------

	@FindBy(xpath = "//div[contains(@id,'ListDetail:AccountDBPaymentsLV') and contains(@id,':AccountDetailPaymentsScreen:DirectBill')] | //div[contains(@id,':DirectBillPaymentsListDetail:AccountDBPaymentsLV')] | //div[contains(@id, ':policyLoanBalanceLV:paymentsTab:LHAccountDBPaymentsLV')] | //div[contains(@id, ':policyLoanBalanceLV:paymentsTab:AccountDBPaymentsLV')]")
	public WebElement table_AccountPaymentsAndCreditDistributionsTable;

	@FindBy(xpath = "//div[@id='AccountPayments:AccountDetailPaymentsScreen:DirectBillPaymentsListDetail:AccountPaymentDistributionItemsCV:DirectBillDistItemsLV']")
	public WebElement table_AccountPaymentsAndCreditDistributionsDetailsTable;

	@FindBy(xpath = "//div[@id='AccountPayments:AccountDetailPaymentsScreen:DirectBillPaymentsListDetail:AccountPaymentDistributionItemsCV:TransferPaymentsLV']")
	public WebElement table_AccountPaymentsAndCreditDistributionsTransfersTable;

	@FindBy(xpath = "//a[contains(@id,':AccountPaymentDistributionItemsCV:PaymentDetailsTab')]")
	public WebElement tab_PaymentDetails;

	@FindBy(xpath = "//a[contains(@id,':AccountPaymentDistributionItemsCV:reversedMoniesTab')]")
	public WebElement tab_PreviousVersions;
	
	@FindBy(xpath = "//*[contains(@id,'DBPaymentReversalConfirmationPopup:Update')]")
	public WebElement button_PaymentReversalOK;

	public Guidewire8Select comboBox_PaymentReversalReason() {
        return new Guidewire8Select(driver, "//table[contains(@id,'DBPaymentReversalConfirmationPopup:Reason-triggerWrap')]");
	}

	public Guidewire8Select comboBox_PaymentReturnedCheckReason() {
        return new Guidewire8Select(driver, "//table[contains(@id,'DBPaymentReversalConfirmationPopup:ReturnedCheckReason-triggerWrap')]");
	}
	
	// -------------------------------------------------------
	// Helper Methods for Above Elements
	// -------------------------------------------------------

	public WebElement getPaymentsAndCreditDistributionsTableRow(Date paymentDate, Date createDate, String policyNumber, String loanNumber, PaymentReversalReason reversalReason, Date reversalDate, PaymentInstrumentEnum paymentInstrument, PaymentLocation paymentLocation, String referenceNumber, String unappliedFund, Double amount, Double amountDistributed, Double amountUndistributed) {
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        if (paymentDate != null) {
            columnRowKeyValuePairs.put("Payment Date", DateUtils.dateFormatAsString("MM/dd/yyyy", paymentDate));
        }
        if (createDate != null) {
            columnRowKeyValuePairs.put("Create Date", DateUtils.dateFormatAsString("MM/dd/yyyy", createDate));
        }
        if (policyNumber != null) {
            columnRowKeyValuePairs.put("Policy", policyNumber);
        }
        if (loanNumber != null) {
            columnRowKeyValuePairs.put("Loan Number", loanNumber);
        }
        if (reversalReason != null) {
            columnRowKeyValuePairs.put("Reversal Reason", reversalReason.getValue());
        }
        if (reversalDate != null) {
            columnRowKeyValuePairs.put("Reversed", DateUtils.dateFormatAsString("MM/dd/yyyy", reversalDate));
        }
        if (paymentInstrument != null) {
            columnRowKeyValuePairs.put("Payment Instrument", paymentInstrument.getValue());
        }
        if (paymentLocation != null) {
            columnRowKeyValuePairs.put("Payment Location", paymentLocation.getValue());
        }
        if (referenceNumber != null) {
            columnRowKeyValuePairs.put("Ref#", referenceNumber);
        }
        if (unappliedFund != null) {
            columnRowKeyValuePairs.put("Unapplied Fund", unappliedFund);
        }
        if (amount != null) {
            columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(amount));
        }
        if (amountDistributed != null) {
            columnRowKeyValuePairs.put("Amount Distributed", StringsUtils.currencyRepresentationOfNumber(amountDistributed));
        }
        if (amountUndistributed != null) {
            columnRowKeyValuePairs.put("Amount Undistributed", StringsUtils.currencyRepresentationOfNumber(amountUndistributed));
        }
        return tableUtils.getRowInTableByColumnsAndValues(table_AccountPaymentsAndCreditDistributionsTable, columnRowKeyValuePairs);
	}
	
	// Click the row in the Payments or Credit Distributions table that you want to inspect, then use one of the methods below to click the corresponding tab
	public WebElement getPaymentsAndCreditDistributionsDetailsTableRow(String invoiceNumber, Date invoiceDate, Date invoiceDateDue, String paymentOwner, String paymentPayer, String policyNumber, String loanNumber, String paymentType, Double amount, Double unpaidAmount, Double grossAmountApplied) {
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
		if (invoiceNumber != null) {
            columnRowKeyValuePairs.put("Invoice #", invoiceNumber);
        }
		if (invoiceDate != null) {
            columnRowKeyValuePairs.put("Invoice Date", DateUtils.dateFormatAsString("MM/dd/yyyy", invoiceDate));
        }
        if (invoiceDateDue != null) {
            columnRowKeyValuePairs.put("Invoice Due Date", DateUtils.dateFormatAsString("MM/dd/yyyy", invoiceDateDue));
        }
        if (paymentOwner != null) {
            columnRowKeyValuePairs.put("Owner", paymentOwner);
        }
        if (paymentPayer != null) {
            columnRowKeyValuePairs.put("Payer", paymentPayer);
        }
        if (policyNumber != null) {
            columnRowKeyValuePairs.put("Policy", policyNumber);
        }
        if (loanNumber != null) {
            columnRowKeyValuePairs.put("Loan Number", loanNumber);
        }
        if (paymentType != null) {
            columnRowKeyValuePairs.put("Type", paymentType);
        }
        if (amount != null) {
            columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(amount));
        }
        if (unpaidAmount != null) {
            columnRowKeyValuePairs.put("Unpaid Amount", StringsUtils.currencyRepresentationOfNumber(unpaidAmount));
        }
        if (grossAmountApplied != null) {
            columnRowKeyValuePairs.put("Gross Amount Applied", StringsUtils.currencyRepresentationOfNumber(grossAmountApplied));
        }
        return tableUtils.getRowInTableByColumnsAndValues(table_AccountPaymentsAndCreditDistributionsDetailsTable, columnRowKeyValuePairs);
	}
	
	// Actions links in Payments table
	public void clickActionsOrReversedButton(int rowNumber) {
        tableUtils.clickActionsLinkInSpecficRowInTable(table_AccountPaymentsAndCreditDistributionsTable, rowNumber);
	}

	public void clickActionsModifyPaymentDetailsLink(int rowNumber) {
        tableUtils.clickActionsLinkInSpecficRowInTable(table_AccountPaymentsAndCreditDistributionsTable, rowNumber, "Modify Payment Details");
	}

	public void clickActionsModifyDistributionLink(int rowNumber) {
        tableUtils.clickActionsLinkInSpecficRowInTable(table_AccountPaymentsAndCreditDistributionsTable, rowNumber, "Modify Distribution");
	}

	public void clickActionsReverseLink(int rowNumber) {
        tableUtils.clickActionsLinkInSpecficRowInTable(table_AccountPaymentsAndCreditDistributionsTable, rowNumber, "Reverse");
	}

	public void clickReversedModifyReversalReasonLink(int rowNumber) {
        tableUtils.clickActionsLinkInSpecficRowInTable(table_AccountPaymentsAndCreditDistributionsTable, rowNumber, "Modify Reversal Reason");
	}

	public void clickActionsUndistributeLink(int rowNumber) {
        tableUtils.clickActionsLinkInSpecficRowInTable(table_AccountPaymentsAndCreditDistributionsTable, rowNumber, "Undistribute");
	}

	public void clickActionsTransactionLink(int rowNumber) {
        tableUtils.clickActionsLinkInSpecficRowInTable(table_AccountPaymentsAndCreditDistributionsTable, rowNumber, "Transaction");
	}

	public void clickActionsWriteoffLink(int rowNumber) {
        tableUtils.clickActionsLinkInSpecficRowInTable(table_AccountPaymentsAndCreditDistributionsTable, rowNumber, "Writeoff");
	}

	public void clickActionsNegativeWriteoffLink(int rowNumber) {
        tableUtils.clickActionsLinkInSpecficRowInTable(table_AccountPaymentsAndCreditDistributionsTable, rowNumber, "Negative Writeoff");
	}

	public void clickActionsNegativeWriteoffReversalLink(int rowNumber) {
        tableUtils.clickActionsLinkInSpecficRowInTable(table_AccountPaymentsAndCreditDistributionsTable, rowNumber, "Negative Writeoff Reversal");
	}
	
	public void clickPaymentDetailsTab() {
		clickWhenClickable(tab_PaymentDetails);
	}

	public void clickPreviousVersionsTab() {
		clickWhenClickable(tab_PreviousVersions);
	}
	
	public void clickTableRowByAmount(double amount) {
		getPaymentsAndCreditDistributionsTableRow(null, null, null, null, null, null, null, null, null, null, amount, null, null).click();
	}
	
	public void clickActionsByAmount(double amount) {
        tableUtils.clickActionsLinkInSpecficRowInTable(table_AccountPaymentsAndCreditDistributionsTable, tableUtils.getRowNumberFromWebElementRow(getPaymentsAndCreditDistributionsTableRow(null, null, null, null, null, null, null, null, null, null, amount, null, null)));
	}
	
	public void clickActionsReverseByAmount(double amount) {
        int rowNumber = tableUtils.getRowNumberFromWebElementRow(getPaymentsAndCreditDistributionsTableRow(null, null, null, null, null, null, null, null, null, null, amount, null, null));
		clickActionsReverseLink(rowNumber);
	}
	
	private WebElement getTableRowByPaymentDate(Date paymentDate) {
		return getPaymentsAndCreditDistributionsTableRow(paymentDate, null, null, null, null, null, null, null, null, null, null, null, null);
	}
	
	public void clickTableRowByPaymentDate(Date paymentDate) {
		getTableRowByPaymentDate(paymentDate).click();
	}
	
	public void clickActionsModifyDistributionByAmount(double amount, String tableHeaderName) {
        int rowNumber = tableUtils.getRowNumberFromWebElementRow(getPaymentsAndCreditDistributionsTableRow(null, null, null, null, null, null, null, null, null, null, amount, null, null));
		clickActionsModifyDistributionLink(rowNumber);
	}
	
	public double getAmountUndistributed(int rowNumber) {
        return NumberUtils.getCurrencyValueFromElement(tableUtils.getCellTextInTableByRowAndColumnName(table_AccountPaymentsAndCreditDistributionsTable, rowNumber, "Amount Undistributed"));
	}

	public double getPaymentAmount(int rowNumber) {
		boolean paymentFound = false;
		int i = 0;
		do {
			refreshPage();
			sleep(1); //To ensure page has fully refreshed.
            paymentFound = tableUtils.verifyRowExistsInTableByColumnsAndValues(table_AccountPaymentsAndCreditDistributionsTable, "Payment Date", DateUtils.getCenterDateAsString(driver, ApplicationOrCenter.BillingCenter, "MM/dd/yyyy"));
			i++;
		} while (!paymentFound && i < 120);
		
		if (!paymentFound) {
			Assert.fail("Could not get payment amount after 2 minutes.");
		}
        return NumberUtils.getCurrencyValueFromElement(tableUtils.getCellTextInTableByRowAndColumnName(table_AccountPaymentsAndCreditDistributionsTable, rowNumber, "Amount"));
	}
		
	public boolean waitUntilBindPaymentsArrive(int secondsToWait) {
        String gridColumnID = tableUtils.getGridColumnFromTable(table_AccountPaymentsAndCreditDistributionsTable, "Payment Location");
		String xpathToCheck = ".//tr/td[(contains(@class,'" + gridColumnID + "'))]/div[(contains(.,'PolicyCenter'))]";
		List<WebElement> numRows = table_AccountPaymentsAndCreditDistributionsTable.findElements(By.xpath(xpathToCheck));

		boolean found = false;
		long secondsRemaining = secondsToWait;
		int delayInterval = 5;
		while ((found == false) && (secondsRemaining > 0)) {
			if (numRows.size() > 0) {
				found = true;
			} else {
				sleep(delayInterval); //Used to wait for the amount of time specified in the delayInterval variable before attempting to check for payments again.
				secondsRemaining = secondsRemaining - delayInterval;
                repository.bc.account.BCAccountMenu acctMenuStuff = new BCAccountMenu(driver);
				acctMenuStuff.clickBCMenuSummary();
				acctMenuStuff.clickAccountMenuPaymentsPayments();

				gridColumnID = tableUtils.getGridColumnFromTable(table_AccountPaymentsAndCreditDistributionsTable, "Payment Location");
				xpathToCheck = ".//tr/td[(contains(@class,'" + gridColumnID + "'))]/div[(contains(.,'PolicyCenter'))]";
				numRows = table_AccountPaymentsAndCreditDistributionsTable.findElements(By.xpath(xpathToCheck));
			}
		}
		return found;
	}
	
	public void reversePayment(Double amount, Double amountDistributed, Double amountUndistributed,	PaymentReversalReason reverseReason) {
		WebElement paymentRow = getPaymentsAndCreditDistributionsTableRow(null, null, null, null, null, null, null, null, null,	null, amount, amountDistributed, amountUndistributed);
		int paymentRowNumber = Integer.valueOf(paymentRow.findElement(By.xpath(".//parent::td/parent::tr")).getAttribute("data-recordindex")) + 1;
		clickActionsReverseLink(paymentRowNumber);
		setPaymentReversalReason(reverseReason);
		clickPaymentReversalOKButton();
	}
	
	public void reversePaymentAtFault(Date paymentDate, Double amount, Double amountDistributed, Double amountUndistributed, PaymentReturnedPaymentReason returnReason) {
		WebElement paymentRow = getPaymentsAndCreditDistributionsTableRow(paymentDate, null, null, null, null, null, null, null, null,	null, amount, amountDistributed, amountUndistributed);
		int paymentRowNumber = Integer.valueOf(paymentRow.findElement(By.xpath(".//parent::td/parent::tr")).getAttribute("data-recordindex")) + 1;
		clickActionsReverseLink(paymentRowNumber);
		setPaymentReversalReasonAtFault(returnReason);
		clickOK();
	}
	
	public String reversePaymentByRandomReason(Date paymentDate, Double amount, Double amountDistributed, Double amountUndistributed) {
		Guidewire8Select mySelectReson = comboBox_PaymentReversalReason();		
		WebElement paymentRow = getPaymentsAndCreditDistributionsTableRow(paymentDate, null, null, null, null, null, null, null, null,	null, amount, amountDistributed, amountUndistributed);
		int paymentRowNumber = Integer.valueOf(paymentRow.findElement(By.xpath(".//parent::td/parent::tr")).getAttribute("data-recordindex")) + 1;
		clickActionsReverseLink(paymentRowNumber);
		String reason = mySelectReson.selectByVisibleTextRandom();
		if(reason.equals(PaymentReversalReason.Return_Payment.getValue()))	{
			comboBox_PaymentReturnedCheckReason().selectByVisibleTextRandom();
			clickOK();
		}
		clickPaymentReversalOKButton();		
		return reason;
	}
	
	public boolean verifyPayment(Date paymentDate, PaymentInstrumentEnum instrument, Double amount) {
		try {
			getPaymentsAndCreditDistributionsTableRow(paymentDate, null, null, null, null, null, instrument, null, null, null, amount, null, null);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean verifyPaymentAndClick(Date paymentDate, Date createDate, String policyNumber, String loanNumber, PaymentReversalReason reversalReason, Date reversalDate, PaymentInstrumentEnum paymentInstrument, PaymentLocation paymentLocation, String referenceNumber, String unappliedFund, Double amount, Double amountDistributed, Double amountUndistributed) {
		WebElement paymentRow = null;
		try {
			paymentRow = getPaymentsAndCreditDistributionsTableRow(paymentDate, createDate, policyNumber, loanNumber, reversalReason,	reversalDate, paymentInstrument, paymentLocation, referenceNumber, unappliedFund, amount, amountDistributed, amountUndistributed);
			paymentRow.click();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean verifyPaymentDetails(String invoiceNumber, Date invoiceDate, Date invoiceDateDue, String paymentOwner, String paymentPayer, String policyNumber, String loanNumber, String paymentType, Double amount, Double unpaidAmount, Double grossAmountApplied) {
		try {
			getPaymentsAndCreditDistributionsDetailsTableRow(invoiceNumber, invoiceDate, invoiceDateDue, paymentOwner, paymentPayer, policyNumber, loanNumber, paymentType, amount, unpaidAmount, grossAmountApplied);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	// These reversal methods might belong elsewhere
	public void setPaymentReversalReason(PaymentReversalReason reversalReason) {
		Guidewire8Select mySelect = comboBox_PaymentReversalReason();
		mySelect.selectByVisibleText(reversalReason.getValue());
	}

	public void setPaymentReversalReasonAtFault(PaymentReturnedPaymentReason returnReason) {
		Guidewire8Select mySelect = comboBox_PaymentReversalReason();
		mySelect.selectByVisibleText("Return Payment");
		comboBox_PaymentReturnedCheckReason().selectByVisibleText(returnReason.getValue());
	}

	public void clickPaymentReversalOKButton() {
		clickWhenVisible(button_PaymentReversalOK);
	}

	public void reversePaymentByAmount(double amount) {
		clickActionsReverseByAmount(amount);
		setPaymentReversalReason(PaymentReversalReason.Processing_Error_Did_Not_Go_To_Bank);
		clickPaymentReversalOKButton();
	}
	
	public boolean verifyPaymentInstrument(String downPaymentTypeFromPolicy) {
        int num = finds(By.xpath("//tbody/tr/td/div[contains(text(), '" + downPaymentTypeFromPolicy + "')]")).size();
		return (num > 0);				
}
}

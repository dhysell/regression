package repository.bc.account;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentRestriction;
import repository.gw.enums.PolicyStatus;
import repository.gw.enums.PolicyTermStatus;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AccountPolicies extends BasePage {

	private TableUtils tableUtils;
	
	public AccountPolicies(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		this.tableUtils = new TableUtils(driver);
	}

	/////////////////////
	// Recorded Elements//
	/////////////////////

	@FindBy(xpath = "//div[@id='AccountDetailPolicies:AccountDetailPoliciesScreen:0:OwnedPolicyPeriods:PolicyPeriodPanelLV']")
	public WebElement table_AccountPolicyTable;

	//////////////////
	// Helper Methods//
	//////////////////

	public WebElement getAccountPoliciesTable() {
		waitUntilElementIsVisible(table_AccountPolicyTable);
		return table_AccountPolicyTable;
	}

	public boolean verifyCancellation(String policyNumber) {
		boolean found = false;
		PolicyStatus policyStatus = PolicyStatus.valueOf(tableUtils.getCellTextInTableByRowAndColumnName(table_AccountPolicyTable, tableUtils.getRowInTableByColumnNameAndValue(table_AccountPolicyTable, "Policy #", policyNumber), "Policy Status"));
		if (policyStatus.equals(PolicyStatus.Canceled)) {
			found = true;
		}
		return found;
	}

	public String getPolicyNumberByRowNumber(int rowNumber) {
		return tableUtils.getCellTextInTableByRowAndColumnName(table_AccountPolicyTable, rowNumber, "Policy #");
	}

	public boolean verifyPaymentRestriction(String policyNumber, PaymentRestriction paymentRestriction) {
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
		columnRowKeyValuePairs.put("Policy #", policyNumber);
		columnRowKeyValuePairs.put("Payment Restriction", paymentRestriction.getValue());
		List<WebElement> allRows = tableUtils.getRowsInTableByColumnsAndValues(table_AccountPolicyTable, columnRowKeyValuePairs);
		return allRows.size() == 1;
	}

	public boolean verifyInvoiceStream(String policyNumber, String invoiceStreamPolicyNumber) {
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
		columnRowKeyValuePairs.put("Policy #", policyNumber);
		columnRowKeyValuePairs.put("Invoice Stream", invoiceStreamPolicyNumber);
		List<WebElement> allRows = tableUtils.getRowsInTableByColumnsAndValues(table_AccountPolicyTable, columnRowKeyValuePairs);
		return allRows.size() == 1;
	}

	public void clickPolicyNumber(String policyNumber) {
		tableUtils.clickLinkInTable(table_AccountPolicyTable, policyNumber);
	}

	public WebElement getAccountPolicyTableRow(String policyNumber, String namedInsured, Date effectiveDate, Date expirationDate, PolicyStatus policyStatus, PolicyTermStatus termStatus, Double totalValue, String invoiceStream, PaymentRestriction paymentRestriction, PaymentInstrumentEnum paymentInstrument) {
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
		if (policyNumber != null) {
			columnRowKeyValuePairs.put("Policy #", policyNumber);
		}
		if (namedInsured != null) {
			columnRowKeyValuePairs.put("Named Insured", namedInsured);
		}
		if (effectiveDate != null) {
			columnRowKeyValuePairs.put("Effective Date", DateUtils.dateFormatAsString("MM/dd/yyyy", effectiveDate));
		}
		if (expirationDate != null) {
			columnRowKeyValuePairs.put("Expiration Date", DateUtils.dateFormatAsString("MM/dd/yyyy", expirationDate));
		}
		if (policyStatus != null) {
			columnRowKeyValuePairs.put("Policy Status", policyStatus.getValue());
		}
		if (termStatus != null) {
			columnRowKeyValuePairs.put("Term Status", termStatus.getValue());
		}
		if (totalValue != null) {
			columnRowKeyValuePairs.put("Total Value", StringsUtils.currencyRepresentationOfNumber(totalValue));
		}
		if (invoiceStream != null) {
			columnRowKeyValuePairs.put("Invoice Stream", invoiceStream);
		}
		if (paymentRestriction != null) {
			columnRowKeyValuePairs.put("Payment Restriction", paymentRestriction.getValue());
		}
		if (paymentInstrument != null) {
			columnRowKeyValuePairs.put("Payment Instrument", paymentInstrument.getValue());
		}
		return tableUtils.getRowInTableByColumnsAndValues(table_AccountPolicyTable, columnRowKeyValuePairs);
	}

	public boolean verifyPolicyInTable(String policyNumber, String namedInsured, Date effectiveDate, Date expirationDate, PolicyStatus policyStatus, PolicyTermStatus termStatus, Double totalValue, String invoiceStream, PaymentRestriction paymentRestriction, PaymentInstrumentEnum paymentInstrument) {
		try {
			getAccountPolicyTableRow(policyNumber, namedInsured, effectiveDate, expirationDate, policyStatus, termStatus, totalValue, invoiceStream, paymentRestriction, paymentInstrument);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void clickPolicyNumberInPolicyTableRow(String policyNumber, String namedInsured, Date effectiveDate, Date expirationDate, PolicyStatus policyStatus, PolicyTermStatus termStatus, Double totalValue, String invoiceStream, PaymentRestriction paymentRestriction, PaymentInstrumentEnum paymentInstrument) {
		WebElement tableRow = getAccountPolicyTableRow(policyNumber, namedInsured, effectiveDate, expirationDate, policyStatus, termStatus, totalValue, invoiceStream, paymentRestriction, paymentInstrument);
		tableUtils.clickLinkInTableByRowAndColumnName(table_AccountPolicyTable, tableUtils.getRowNumberFromWebElementRow(tableRow), "Policy #");
	}
	public String getPaymentInstrument(int rowNumber, String headerColumn) {
		return tableUtils.getCellTextInTableByRowAndColumnName(table_AccountPolicyTable, rowNumber, headerColumn);
	}
}

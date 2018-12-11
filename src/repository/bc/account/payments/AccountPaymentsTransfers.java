package repository.bc.account.payments;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.gw.enums.TransferReason;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;

import java.util.Date;
import java.util.HashMap;

public class AccountPaymentsTransfers extends AccountPayments {
	
	public AccountPaymentsTransfers(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	//////////////////////////////////////////
	// "Recorded Elements" and their XPaths //
	//////////////////////////////////////////

	@FindBy(xpath = "//div[(contains(@id,':AccountTransfers_FBMScreen:AccountTransfers_FBMLV')) or (@id= 'AccountPolicyBalances:AccountPolicyBalancesScreen:policyLoanBalanceLV:AccountTransfers_FBMLV')]")
	public WebElement table_Transfers;

	///////////////////////////////////////
	// Helper Methods for Above Elements //
	///////////////////////////////////////
	
	public WebElement getTransfersTable() {
		return table_Transfers;
	}
	
	public WebElement getAccountPaymentsTransfersTableRow(Date transactionDate, String transactionNumber, Double transferAmount, String sourceAccount, String sourcePolicy, String sourceLoanNumber, String targetAccount, String targetPolicy, String targetLoanNumber, TransferReason transferReason) {
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
		if (transactionDate != null) {
			columnRowKeyValuePairs.put("Transaction Date", DateUtils.dateFormatAsString("MM/dd/yyyy", transactionDate));
		}
		if (transactionNumber != null) {
			columnRowKeyValuePairs.put("Transaction Number", transactionNumber);
		}
		if (transferAmount != null) {
			columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(transferAmount));
		}
		if (sourceAccount != null) {
			columnRowKeyValuePairs.put("Source Account", sourceAccount);
		}
		if (sourcePolicy != null) {
			columnRowKeyValuePairs.put("Source Policy", sourcePolicy);
		}
		if (sourceLoanNumber != null) {
			columnRowKeyValuePairs.put("Source Loan Number", sourceLoanNumber);
		}
		if (targetAccount != null) {
			columnRowKeyValuePairs.put("Target Account", targetAccount);
		}
		if (targetPolicy != null) {
			columnRowKeyValuePairs.put("Target Policy", targetPolicy);
		}
		if (targetLoanNumber != null) {
			columnRowKeyValuePairs.put("Target Loan Number", targetLoanNumber);
		}
		if (transferReason != null) {
			columnRowKeyValuePairs.put("Reason", transferReason.getValue());
		}
		return new TableUtils(getDriver()).getRowInTableByColumnsAndValues(table_Transfers, columnRowKeyValuePairs);
	}
}

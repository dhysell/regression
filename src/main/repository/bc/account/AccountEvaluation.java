package repository.bc.account;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.OpenClosed;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AccountEvaluation extends BasePage {

	public AccountEvaluation(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	// -------------------------------------
	// "Recorded Elements" and their XPaths
	// -------------------------------------

	@FindBy(xpath = "//div[contains(@id,':AccountEvaluationInputSet:AccountEvaluationDelinquenciesLV')]")
	public WebElement table_AccountEvaluationDelinquencyHistory;

	@FindBy(xpath = "//div[contains(@id,':AccountEvaluationInputSet:NumberOfDelinquencies-inputEl')]")
	public WebElement label_NumberOfDelinquencies;

	@FindBy(xpath = "//div[contains(@id,':AccountEvaluationInputSet:NumberOfPejorativePaymentReversals-inputEl')]")
	public WebElement label_NumberOfAtFaultPaymentReversals;

	@FindBy(xpath = "//div[contains(@id,':AccountEvaluationInputSet:NumberOfPolicyCancellations-inputEl')]")
	public WebElement label_NumberOfFullCancellations;

	@FindBy(xpath = "//div[contains(@id,':AccountEvaluationInputSet:NumberOfPartialCancellations-inputEl')]")
	public WebElement label_NumberOfPartialCancellations;

	// ----------------------------------
	// Helper Methods for Above Elements
	// ----------------------------------

	public boolean verifyDelinquencyHistory(Date startDate, String accountNumber, OpenClosed openClosed) {
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
		columnRowKeyValuePairs.put("Status", openClosed.getValue());
		columnRowKeyValuePairs.put("Responsible Payer", accountNumber);
		columnRowKeyValuePairs.put("Delinquency Date", DateUtils.dateFormatAsString("MM/dd/yyyy", startDate));
		List<WebElement> delinquencyRows = new TableUtils(getDriver()).getRowsInTableByColumnsAndValues(table_AccountEvaluationDelinquencyHistory, columnRowKeyValuePairs);
		return (delinquencyRows.size() > 0);
	}

	public int getNumberOfDelinquencies() {
		String delinquency = label_NumberOfDelinquencies.getText();
		return Integer.parseInt(delinquency.substring(0, delinquency.indexOf(' ')));
	}

	public int getNumberOfAtFaultPaymentReversals() {
		String pmtReversals = label_NumberOfAtFaultPaymentReversals.getText();
		return Integer.parseInt(pmtReversals.substring(0, pmtReversals.indexOf(' ')));
	}

	public int getNumberOfFullCancellations() {
		String fullCancellation = label_NumberOfFullCancellations.getText();
		return Integer.parseInt(fullCancellation.substring(0, fullCancellation.indexOf(' ')));
	}

	public int getNumberOfPartialCancellations() {
		String partialCancellation = label_NumberOfPartialCancellations.getText();
		return Integer.parseInt(partialCancellation.substring(0, partialCancellation.indexOf(' ')));
	}

}

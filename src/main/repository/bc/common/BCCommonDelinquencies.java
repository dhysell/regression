package repository.bc.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.CancellationEvent;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.OpenClosed;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;

import java.util.Date;
import java.util.HashMap;

public class BCCommonDelinquencies extends BasePage {
	
	private WebDriver driver;
	private TableUtils tableUtils;
	
	public BCCommonDelinquencies(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		this.driver = driver;
		this.tableUtils = new TableUtils(driver);
	}
	
	// -------------------------------------
	// "Recorded Elements" and their XPaths
	// -------------------------------------

	@FindBy(xpath = "//div[contains(@id,':DelinquencyProcessesDetailScreen:DetailPanel:DelinquencyProcessLV') ]")
	public WebElement table_Delinquencies;

	@FindBy(xpath = "//div[@id=':DelinquencyProcessesDetailScreen:DetailPanel:DelinquencyProcessDetailPanelSet:DelinquencyProcessEventsLV']")
	public WebElement table_DelinquenciesEvents;

	@FindBy(xpath = "//div[@id=':DelinquencyProcessesDetailScreen:DetailPanel:DelinquencyProcessDetailPanelSet:DelinquencyProcessDetailsDV:TotalUnbilled-inputEl']")
	public WebElement text_DelinquenciesUnbilledAmount;

	@FindBy(xpath = "//span[contains(@id,':DelinquencyProcessesDetailScreen:DetailPanel:DelinquencyProcessDetailPanelSet_tb:exitdelinquency_button-btnInnerEl') ]")
	public WebElement button_DelinquenciesExitDelinquency;

	Guidewire8Select select_DeliquencyActiveStatusFilter() {
		return new Guidewire8Select(driver, "//table[contains(@id, 'DelinquencyProcessesDetailScreen:DetailPanel:DelinquencyProcessLV:ActiveFilter-triggerWrap')]");
	}

	// ----------------------------------
	// Helper Methods for Above Elements
	// ----------------------------------

	public void clickExitDelinquencyButton() {
		clickWhenClickable(button_DelinquenciesExitDelinquency);
	}
	
	public WebElement getExitDelinquencyButton() {
		waitUntilElementIsVisible(button_DelinquenciesExitDelinquency);
		return button_DelinquenciesExitDelinquency;
	}

	public WebElement getDelinquencyTable() {
		waitUntilElementIsVisible(table_Delinquencies);
		return table_Delinquencies;
	}
	
	public WebElement getDelinquencyTableRow(OpenClosed status, DelinquencyReason delinquencyReason, String responsiblePayer, String loanNumber, String delinquencyTarget, Date startDate, Double originalDelinquencyAmount, Double delinquentAmount) {
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
		if (status != null) {
			columnRowKeyValuePairs.put("Status", status.getValue());
		}
		if (delinquencyReason != null) {
			columnRowKeyValuePairs.put("Delinquency Reason", delinquencyReason.getValue());
		}
		if (responsiblePayer != null) {
			columnRowKeyValuePairs.put("Responsible Payer", responsiblePayer);
		}
		if (loanNumber != null) {
			columnRowKeyValuePairs.put("Loan Number", loanNumber);
		}
		if (delinquencyTarget != null) {
			columnRowKeyValuePairs.put("Delinquency Target", delinquencyTarget);
		}
		if (startDate != null) {
			columnRowKeyValuePairs.put("Start Date", DateUtils.dateFormatAsString("MM/dd/yyyy", startDate));
		}
		if (originalDelinquencyAmount != null) {
			columnRowKeyValuePairs.put("Original Delinquent Amount", StringsUtils.currencyRepresentationOfNumber(originalDelinquencyAmount));
		}
		if (delinquentAmount != null) {
			columnRowKeyValuePairs.put("Delinquent Amount", StringsUtils.currencyRepresentationOfNumber(delinquentAmount));
		}
		return tableUtils.getRowInTableByColumnsAndValues(table_Delinquencies, columnRowKeyValuePairs);
	}

	public boolean verifyDelinquencyExists(OpenClosed status, DelinquencyReason delinquencyReason, String responsiblePayer, String loanNumber, String delinquencyTarget, Date startDate, Double originalDelinquencyAmount, Double delinquentAmount) {
		boolean found = false;
		try {
			getDelinquencyTableRow(status, delinquencyReason, responsiblePayer, loanNumber, delinquencyTarget, startDate, originalDelinquencyAmount, delinquentAmount);
			found = true;
		} catch (Exception e) {
			found = false;
		}
		return found;
	}
	
	public boolean verifyDelinquencyStatus(OpenClosed openClosed, String accountNumber, String policyNumber, Date startDate) {
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
		if (openClosed != null) {
			columnRowKeyValuePairs.put("Status", openClosed.getValue());
		}
		if (accountNumber != null) {
			columnRowKeyValuePairs.put("Responsible Payer", accountNumber);
		}
		if (policyNumber != null) {
			columnRowKeyValuePairs.put("Delinquency Target", policyNumber);
		}
		if (startDate != null) {
			columnRowKeyValuePairs.put("Start Date", DateUtils.dateFormatAsString("MM/dd/yyyy", startDate));
		}
		try {
			tableUtils.getRowInTableByColumnsAndValues(table_Delinquencies, columnRowKeyValuePairs);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean verifyDelinquencyByReason(OpenClosed openClosed, DelinquencyReason delinquencyReason, String payerAccountNumber, Date startDate) {
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
		if (openClosed != null) {
			columnRowKeyValuePairs.put("Status", openClosed.getValue());
		}
		if (delinquencyReason != null) {
			columnRowKeyValuePairs.put("Delinquency Reason", delinquencyReason.getValue());
		}
		if (payerAccountNumber != null) {
			columnRowKeyValuePairs.put("Responsible Payer", payerAccountNumber);
		}
		if (startDate != null) {
			columnRowKeyValuePairs.put("Start Date", DateUtils.dateFormatAsString("MM/dd/yyyy", startDate));
		}
		try {
			tableUtils.getRowInTableByColumnsAndValues(table_Delinquencies, columnRowKeyValuePairs);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public double getDelinquentAmount(OpenClosed openClosed, String accountNumber, String policyNumber, Date startDate) {
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
		if (openClosed != null) {
			columnRowKeyValuePairs.put("Status", openClosed.getValue());
		}
		if (accountNumber != null) {
			columnRowKeyValuePairs.put("Responsible Payer", accountNumber);
		}
		if (policyNumber != null) {
			columnRowKeyValuePairs.put("Delinquency Target", policyNumber);
		}
		if (startDate != null) {
			columnRowKeyValuePairs.put("Start Date", DateUtils.dateFormatAsString("MM/dd/yyyy", startDate));
		}
		WebElement tableRow = tableUtils.getRowInTableByColumnsAndValues(table_Delinquencies, columnRowKeyValuePairs);
		double delinquentAmount = NumberUtils.getCurrencyValueFromElement(tableUtils.getCellWebElementInTableByRowAndColumnName(table_Delinquencies, tableUtils.getRowNumberFromWebElementRow(tableRow), "Delinquent Amount"));
		clickWhenClickable(tableRow);
		return delinquentAmount;
	}
	
	public double getOriginalDelinquentAmount(OpenClosed openClosed, String accountNumber, String policyNumber, Date startDate) {
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
		if (openClosed != null) {
			columnRowKeyValuePairs.put("Status", openClosed.getValue());
		}
		if (accountNumber != null) {
			columnRowKeyValuePairs.put("Responsible Payer", accountNumber);
		}
		if (policyNumber != null) {
			columnRowKeyValuePairs.put("Delinquency Target", policyNumber);
		}
		if (startDate != null) {
			columnRowKeyValuePairs.put("Start Date", DateUtils.dateFormatAsString("MM/dd/yyyy", startDate));
		}
		WebElement tableRow = tableUtils.getRowInTableByColumnsAndValues(table_Delinquencies, columnRowKeyValuePairs);
		double delinquentAmount = NumberUtils.getCurrencyValueFromElement(tableUtils.getCellWebElementInTableByRowAndColumnName(table_Delinquencies, tableUtils.getRowNumberFromWebElementRow(tableRow), "Original Delinquent Amount"));
		clickWhenClickable(tableRow);
		return delinquentAmount;
	}

	public double getUnbilledAmount() {
		return NumberUtils.getCurrencyValueFromElement(text_DelinquenciesUnbilledAmount);
	}

	public boolean verifyDelinquencyEventCompletion(CancellationEvent cancellationEvent) {
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
		columnRowKeyValuePairs.put("Complete", "Yes");
		columnRowKeyValuePairs.put("Description", cancellationEvent.getValue());
		try {
			tableUtils.getRowInTableByColumnsAndValues(table_DelinquenciesEvents, columnRowKeyValuePairs);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// this method is for the situation that there is only one delinquency
	public String getDelinquencyReason() {
		String delinquencyReasonGridColumnID = tableUtils.getGridColumnFromTable(table_Delinquencies, "Delinquency Reason");
		return table_Delinquencies.findElement(By.xpath(".//tr/td[contains(@class,'" + delinquencyReasonGridColumnID + "')]")).getText();
	}

	// get the delinquency based on "Start Date"
	public String getDelinquencyReason(String startDate) {
		String startdateGridColumnID = tableUtils.getGridColumnFromTable(table_Delinquencies, "Start Date");
		String delinquentReasonGridColumnID = tableUtils.getGridColumnFromTable(table_Delinquencies, "Delinquency Reason");
		return table_Delinquencies.findElement(By.xpath(".//tr/td[contains(@class,'" + startdateGridColumnID + "') and contains(.,'" + startDate + "')]/parent::tr/td[contains(@class, '" + delinquentReasonGridColumnID + "')]")).getText();
	}
	
	public void setDelinquencyStatus(String status) {
		select_DeliquencyActiveStatusFilter().selectByVisibleText(status);
	}
}

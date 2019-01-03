package repository.bc.common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;

import java.util.Date;
import java.util.HashMap;

public class BCCommonHistory extends BasePage {

	public BCCommonHistory(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	//////////////////////////////////////////
	// "Recorded Elements" and their XPaths //
	//////////////////////////////////////////

	@FindBy(xpath = "//div[contains (@id, 'AccountDetailHistory:AccountDetailHistoryScreen:AccountDetailHistoryLV') or contains (@id, 'PolicyDetailHistory:PolicyDetailHistoryScreen:PolicyDetailHistoryLV')]")
	public WebElement table_HistoryTable;

	///////////////////////////////////////
	// Helper Methods for Above Elements //
	///////////////////////////////////////
	
	public WebElement getHistoryTable() {
		waitUntilElementIsVisible(table_HistoryTable);
		return table_HistoryTable;
	}
	
	public boolean historyTableExists() {
		return table_HistoryTable.isDisplayed();
	}

	public boolean verifyHistoryTable(Date date, String item) {
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
		columnRowKeyValuePairs.put("Date", DateUtils.dateFormatAsString("MM/dd/yyyy", date));
		columnRowKeyValuePairs.put("Item", item);
		return new TableUtils(getDriver()).getRowsInTableByColumnsAndValues(table_HistoryTable, columnRowKeyValuePairs).size() >= 1;
	}
}

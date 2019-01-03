package repository.bc.common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.TableUtils;

public class BCCommonJournal extends BasePage {

	public BCCommonJournal(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	//////////////////////////////////////////
	// "Recorded Elements" and their XPaths //
	//////////////////////////////////////////
	
	@FindBy(xpath = "//div[contains (@id, 'DetailJournal:JournalScreen:TAccountOwnersLV']")
	public WebElement table_PolicyJournalTable;

	@FindBy(xpath = "//div[contains (@id, 'DetailJournal:JournalScreen:TransactionsLV']")
	public WebElement table_PolicyJournalTransactionsTable;

	///////////////////////////////////////
	// Helper Methods for Above Elements //
	///////////////////////////////////////
	
	public void clickTransactionNumber(String transactionNum) {
		new TableUtils(getDriver()).clickLinkInTableByRowAndColumnName(table_PolicyJournalTransactionsTable, new TableUtils(getDriver()).getRowNumberFromWebElementRow(new TableUtils(getDriver()).getRowInTableByColumnNameAndValue(table_PolicyJournalTransactionsTable, "Transaction #", transactionNum)), "Transaction #");
	}
}

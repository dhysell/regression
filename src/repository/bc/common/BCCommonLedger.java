package repository.bc.common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class BCCommonLedger extends BasePage{

	public BCCommonLedger(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	//////////////////////////////////////////
	// "Recorded Elements" and their XPaths //
	//////////////////////////////////////////
	
	@FindBy(xpath = "//div[contains (@id, 'DetailLedger:LedgerScreen:TAccountOwnersLV']")
	public WebElement table_PolicyLedgerTable;

	@FindBy(xpath = "//div[contains (@id, 'DetailLedger:LedgerScreen:TAccountsLV']")
	public WebElement table_PolicyT_accountsTable;

	@FindBy(xpath = "//div[contains (@id, 'DetailLedger:LedgerScreen:LineItemsLV']")
	public WebElement table_PolicyLineItemsTable;
	
	///////////////////////////////////////
	// Helper Methods for Above Elements //
	///////////////////////////////////////
	
}

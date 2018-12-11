package repository.bc.account.payments;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.bc.account.BCAccountMenu;
import repository.gw.helpers.TableUtils;

public class AccountPaymentsPromisedPayments extends AccountPayments {
	
	public AccountPaymentsPromisedPayments(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	// -----------------------------------------------
	// "Recorded Elements" and their XPaths
	// -----------------------------------------------
	
	@FindBy(xpath = "//div[@id='AccountPromisedPayments:AccountPromisedPaymentsScreen:PromisedPaymentsListLV']")
	public WebElement table_PromisedPayments;
	
	@FindBy(xpath = "(//div[@id='AccountPromisedPayments:AccountPromisedPaymentsScreen:PromisedPaymentsListLV-body']/div/table/tbody/tr/td/div)[4]")
	public WebElement label_PaymentAmount;
	
	@FindBy(xpath = "(//div[@id='AccountPromisedPayments:AccountPromisedPaymentsScreen:PromisedPaymentsListLV-body']/div/table/tbody/tr/td/div)[1]")
	public WebElement label_PaymentType;
	
	// -------------------------------------------------------
	// Helper Methods for Above Elements
	// -------------------------------------------------------
	
	public boolean waitUntilBindPromisedPaymentsArrive(int secondsToWait) {
		int numRows = new TableUtils(getDriver()).getRowCount(table_PromisedPayments);
		boolean found = false;
		long secondsRemaining = secondsToWait;
		int delayInterval = 5;
		while ((found == false) && (secondsRemaining > 0)) {
			if (numRows > 0) {
				found = true;
			} else {
				sleep(delayInterval); //Used to wait for the amount of time specified in the delayInterval variable before attempting to check for payments again.
				secondsRemaining = secondsRemaining - delayInterval;
				repository.bc.account.BCAccountMenu acctMenuStuff = new BCAccountMenu(getDriver());
				acctMenuStuff.clickBCMenuSummary();
				acctMenuStuff.clickAccountMenuPaymentsPromisedPayments();
				numRows = new TableUtils(getDriver()).getRowCount(table_PromisedPayments);
			}
		}
		return found;
	}
}

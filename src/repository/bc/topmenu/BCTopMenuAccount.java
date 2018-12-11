package repository.bc.topmenu;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.gw.errorhandling.ErrorHandling;

import java.util.List;

public class BCTopMenuAccount extends BCTopMenu {

	public BCTopMenuAccount(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	// ------------------------------------
	// "Recorded Elements" and their XPaths
	// ------------------------------------
	@FindBy(how = How.XPATH, using = "//input[starts-with(@id,'TabBar:AccountsTab:AccountNumberSearchItem-inputEl')]")
	public WebElement editbox_MenuAccountAccountNumber;

	// ---------------------------------
	// Helper Methods for Above Elements
	// ---------------------------------

	public void clickAccountSearch() {
		while (!checkIfElementExists(editbox_MenuAccountAccountNumber, 500)) {
			clickAccountArrow();
		}
		clickWhenVisible(editbox_MenuAccountAccountNumber);
	}

	public void menuAccountSearchAccountByAccountNumber(String accountNumber) {
		clickAccountSearch();
		setAccountNumber(accountNumber);

		ErrorHandling errorMessageList = new ErrorHandling(getDriver());
		List<WebElement> errorMessage = errorMessageList.text_ErrorHandlingValidationResults();

		boolean found = false;
		long secondsRemaining = 60;
		int delayInterval = 5;
		while (!found && (secondsRemaining > 0)) {
			if (errorMessage.size() < 1) {
				found = true;
			} else {
				sleep(delayInterval); //Used to wait for the amount of time specified in the delayInterval variable before attempting to check for the account again.
				secondsRemaining -= delayInterval;

				getDriver().navigate().refresh();
				clickAccountSearch();
				setAccountNumber(accountNumber);
				errorMessage = errorMessageList.text_ErrorHandlingValidationResults();
			}
		}

		if (!found) {
			Assert.fail("The account searched could not be found in Billing Center after 1 minute of searching");
		}
	}

	public void setAccountNumber(String accountNumber) {
		setText(editbox_MenuAccountAccountNumber, accountNumber);
	}

	public void clickFirst() {

	}

	public void clickSecond() {

	}

	public void clickThird() {

	}

	public void clickFourth() {

	}

	public void clickFifth() {

	}

	public void clickSixth() {

	}

	public void clickSeventh() {

	}

	public void clickEighth() {

	}
}

package repository.bc.topmenu;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class BCTopMenuPolicy extends BCTopMenu {

	public BCTopMenuPolicy(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	// ------------------------------------
	// "Recorded Elements" and their XPaths
	// ------------------------------------
	// @FindBy(how = How.XPATH, using = "//input[starts-with(@id,'TabBar:PoliciesTab:PolicyNumberSearchItem-inputEl')]")
	// public WebElement editbox_MenuPolicyPolicyNumber;

	@FindBy(xpath = "//input[@id='TabBar:PoliciesTab:PolicyNumberSearchItem-inputEl']")
	public WebElement editbox_MenuPolicyPolicyNumber;

	// ---------------------------------
	// Helper Methods for Above Elements
	// ---------------------------------

	public void clickPolicySearch() {
		while (!checkIfElementExists(editbox_MenuPolicyPolicyNumber, 500)) {
			clickAccountArrow();
		}
		clickWhenVisible(editbox_MenuPolicyPolicyNumber);
	}

	public void menuPolicySearchPolicyByPolicyNumber(String policyNumber) {
		clickPolicyArrow();
		editbox_MenuPolicyPolicyNumber.sendKeys(policyNumber);
		editbox_MenuPolicyPolicyNumber.sendKeys(Keys.ENTER);

		// Need to do error checking to see if it's able to pull up an account. If not, it most likely means that PC sent the record, but BC never recieved it. Need to check event messages at that point and resume as needed.
	}
}

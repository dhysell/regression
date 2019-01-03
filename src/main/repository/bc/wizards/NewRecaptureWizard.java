package repository.bc.wizards;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;

import java.util.List;

public class NewRecaptureWizard extends BasePage {
	
	private WebDriver driver;

	public NewRecaptureWizard(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	// -----------------------------------------------
	// "Recorded Elements" and their XPaths
	// -----------------------------------------------

	@FindBy(xpath = "//input[@id='AccountNewRecaptureWizard:RecaptureDetailsScreen:Amount-inputEl']")
	public WebElement editbox_NewRecaptureAmount;

	@FindBy(xpath = "//div[contains(@id, ':RecaptureDetailsScreen:policy-inputEl')]")
	public WebElement label_NewRecapturePolicyNumber;

	@FindBy(xpath = "//div[contains(@id, ':RecaptureDetailsScreen:loannumber-inputEl')]")
	public WebElement label_NewRecaptureLoanNumber;

	public Guidewire8Select select_NewRecaptureUnappliedFund() {
		return new Guidewire8Select(driver, "//table[contains(@id, 'RecaptureDetailsScreen:UnappliedFund-triggerWrap')]");
	}

	// -------------------------------------------------------
	// Helper Methods for Above Elements
	// -------------------------------------------------------

	public String getNewRecaptureWizardLoanNumber() {
		waitUntilElementIsVisible(label_NewRecaptureLoanNumber);
		String loanNumber = label_NewRecaptureLoanNumber.getText();
		return loanNumber;
	}

	public String getNewRecaptureWizardPolicyNumber() {
		waitUntilElementIsVisible(label_NewRecapturePolicyNumber);
		String loanNumber = label_NewRecapturePolicyNumber.getText();
		return loanNumber;
	}

	public void setNewRecaptureAmount(double amount) {
		waitUntilElementIsVisible(editbox_NewRecaptureAmount);
		editbox_NewRecaptureAmount.sendKeys(String.valueOf(amount));
	}

	public void setNewRecaptureUnappliedFund(String policyNumber) {
		List<WebElement> buttonElements = finds(By.xpath("//table[contains(@id, 'RecaptureDetailsScreen:UnappliedFund-triggerWrap')]"));
		if (buttonElements.size() > 0) {
			select_NewRecaptureUnappliedFund().selectByVisibleTextPartial(policyNumber);
		} else {
			System.out.println("The Unapplied Funds Dropdown was not on the payment page. If this is incorrect, please investigate.");
		}
	}

	public void performRecapture(String policyNumber, double amount) {
		if (policyNumber != null) {
			setNewRecaptureUnappliedFund(policyNumber);
		}
		setNewRecaptureAmount(amount);
		clickFinish();
	}

	public void performRecapture(double amount) {
		waitUntilElementIsVisible(editbox_NewRecaptureAmount);
		editbox_NewRecaptureAmount.sendKeys(String.valueOf(amount));
		clickFinish();
	}

}

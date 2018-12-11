package repository.bc.common;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.BankAccountType;
import repository.gw.enums.PaymentInstrumentEnum;

public class BCCommonPaymentInstrument extends BasePage {

	private WebDriver driver;
	public BCCommonPaymentInstrument(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	// -----------------------------------------------
	// "Recorded Elements" and their XPaths
	// -----------------------------------------------

	@FindBy(xpath = "//input[contains(@id, ':bankAccountHolderInput-inputEl')]")
	private WebElement editBox_BankAccountHoldersName;

	@FindBy(xpath = "//input[contains(@id, ':routingNumberInput-inputEl')]")
	private WebElement editBox_RoutingNumber;

	@FindBy(xpath = "//input[contains(@id, ':NewPaymentInstrumentACHInputSet:accountNumberInput')]")
	private WebElement editBox_BankAccountNumber;

	@FindBy(xpath = "//input[contains(@id, ':CopyPrimaryContact-inputEl')]")
	private WebElement editBox_CopyPrimaryContactDetails;

	@FindBy(xpath = "//input[contains(@id, ':zipcodeInput-inputEl')]")
	private WebElement editBox_PostalCode;

	@FindBy(xpath = "//input[contains(@id, ':emailInput-inputEl')]")
	private WebElement editBox_EmailAddress;
	
	public Guidewire8Select comboBox_PaymentMethod() {
		return new Guidewire8Select(driver, "//table[contains(@id,'NewPaymentInstrumentPopup:PaymentMethod-triggerWrap')]");
	}

	public Guidewire8Select comboBox_AccountType() {
		return new Guidewire8Select(driver, "//table[contains(@id,':accountTypeDropdown-triggerWrap')]");
	}

	// -------------------------------------------------------
	// Helper Methods for Above Elements
	// -------------------------------------------------------

	public void setPaymentMethod(PaymentInstrumentEnum methodToSelect) {
		try {
//			comboBox_PaymentMethod().click();
//			
			comboBox_PaymentMethod().selectByVisibleText(methodToSelect.getValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void setAccountType(BankAccountType accountType) {
		try {
//			comboBox_AccountType().click();
//			
			comboBox_AccountType().selectByVisibleText(accountType.getValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void setBankAccountHoldersName(String name) {
		clickWhenVisible(editBox_BankAccountHoldersName);
		
		editBox_BankAccountHoldersName.sendKeys(name);
		
	}

	public void setRoutingNumber(String number) {
		editBox_RoutingNumber.sendKeys(number);
		clickProductLogo();
	}

	public void setBankAccountNumber(String number) {
		clickWhenVisible(editBox_BankAccountNumber);
		
		editBox_BankAccountNumber.sendKeys(number);
		clickProductLogo();
	}

	public void clickCopyPrimaryContactDetails() {
		
		clickWhenVisible(editBox_CopyPrimaryContactDetails);
	}

	public void setPostalCode(String code) {
		editBox_PostalCode.sendKeys(Keys.CONTROL + "a");
		editBox_PostalCode.sendKeys(code);
		
	}

	public void setEmailAddress(String emailAddress) {
		
		editBox_EmailAddress.click();
		editBox_EmailAddress.sendKeys(Keys.CONTROL + "a");
		editBox_EmailAddress.sendKeys(emailAddress);
	}

}

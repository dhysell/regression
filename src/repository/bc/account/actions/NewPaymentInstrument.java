package repository.bc.account.actions;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.BankAccountType;

public class NewPaymentInstrument extends BasePage  {
	
	private WebDriver driver;
	
	public NewPaymentInstrument(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	/////////////////////////
	//Elements and Locators//
	/////////////////////////
	
	@FindBy(xpath = "//input[@id='NewPaymentInstrumentPopup:NewPaymentInstrumentDV:cardNumberInput-inputEl']")
	public WebElement editbox_CardNumber;
	
	@FindBy(xpath = "//input[@id='NewPaymentInstrumentPopup:NewPaymentInstrumentDV:securityCodeInput-inputEl']")
	public WebElement editbox_SecurityCode;
	
	@FindBy(xpath = "//input[@id='NewPaymentInstrumentPopup:NewPaymentInstrumentDV:PaymentInstrumentContactInputSet:emailInput-inputEl']")
	public WebElement editbox_EmailAddress;
	
	@FindBy(xpath = "//input[@id='NewPaymentInstrumentPopup:NewPaymentInstrumentDV:NewPaymentInstrumentACHInputSet:bankAccountHolderInput-inputEl']")
	public WebElement editbox_BankAccountHoldersName;
	
	@FindBy(xpath = "//input[@id='NewPaymentInstrumentPopup:NewPaymentInstrumentDV:NewPaymentInstrumentACHInputSet:routingNumberInput-inputEl']")
	public WebElement editbox_RoutingNumber;
	
	@FindBy(xpath = "//input[@id='NewPaymentInstrumentPopup:NewPaymentInstrumentDV:NewPaymentInstrumentACHInputSet:accountNumberInput-inputEl']")
	public WebElement editbox_AccountNumber;
	
	public Guidewire8Select select_ExpirationMonth() {
		return new Guidewire8Select(driver, "//table[@id='NewPaymentInstrumentPopup:NewPaymentInstrumentDV:monthDropdown-triggerWrap']");
	}
	
	public Guidewire8Select select_ExpirationYear() {
		return new Guidewire8Select(driver, "//table[@id='NewPaymentInstrumentPopup:NewPaymentInstrumentDV:yearDropdown-triggerWrap']");
	}
	
	public Guidewire8Select select_PrefillFrom() {
		return new Guidewire8Select(driver, "//table[@id='NewPaymentInstrumentPopup:NewPaymentInstrumentDV:PaymentInstrumentContactInputSet:addressContactInput-triggerWrap']");
	}
	
	public Guidewire8Select select_AccountType() {
		return new Guidewire8Select(driver, "//table[@id='NewPaymentInstrumentPopup:NewPaymentInstrumentDV:NewPaymentInstrumentACHInputSet:accountTypeDropdown-triggerWrap']");
	}
	
	public Guidewire8Checkbox checkBox_BCNewPaymentCopyPrimaryContactDetails() {
		return new Guidewire8Checkbox(driver, "//table[contains(@id,'PaymentInstrumentContactInputSet:CopyPrimaryContact')]");
	}
	///////////////////////
	//Interaction Methods//
	///////////////////////
	
	private void setCardNumber(String cardNumber) {
		clickWhenClickable(editbox_CardNumber);
		editbox_CardNumber.sendKeys(Keys.CONTROL + "a");
		editbox_CardNumber.sendKeys(cardNumber);
		sendArbitraryKeys(Keys.TAB);
		waitForPostBack();
	}
	
	private void setExpMonth(int expMonth) {
		select_ExpirationMonth().selectByVisibleTextPartial(String.valueOf(expMonth));
	}

	private void setExpYear(int expYear) {
		select_ExpirationYear().selectByVisibleTextPartial(String.valueOf(expYear));
	}
	
	private void setSecurityCode(int securityCode) {
		clickWhenClickable(editbox_SecurityCode);
		editbox_SecurityCode.sendKeys(Keys.CONTROL + "a");
		editbox_SecurityCode.sendKeys(String.valueOf(securityCode));
		sendArbitraryKeys(Keys.TAB);
		waitForPostBack();
	}
	
	private void setPrefillFrom(String fromWhere) {
		select_PrefillFrom().selectByVisibleTextPartial(fromWhere);
	}
	
	public void setEmailAddress(String email) {
		clickWhenClickable(editbox_EmailAddress);
		editbox_EmailAddress.sendKeys(Keys.CONTROL + "a");
		editbox_EmailAddress.sendKeys(email);
		sendArbitraryKeys(Keys.TAB);
		waitForPostBack();
	}
	
	public void fillOutCreditDebitDetails(String cardNumber, int expMonth, int expYear, int securityCode, String policyNumber) {
		setCardNumber(cardNumber);
		setExpMonth(expMonth);
		setExpYear(expYear);
		setSecurityCode(securityCode);
		setPrefillFrom(policyNumber);
		setCopyPrimaryContactDetailsCheckBox(true);
	}
	
	private void setAccountType(BankAccountType accountType) {
		select_PrefillFrom().selectByVisibleTextPartial(accountType.getValue());
	}
	
	private void setBankAccountHoldersName(String name) {
		clickWhenClickable(editbox_BankAccountHoldersName);
		editbox_BankAccountHoldersName.sendKeys(Keys.CONTROL + "a");
		editbox_BankAccountHoldersName.sendKeys(name);
		sendArbitraryKeys(Keys.TAB);
		waitForPostBack();
	}
	
	private void setRoutingNumber(String routingNumber) {
		clickWhenClickable(editbox_RoutingNumber);
		editbox_RoutingNumber.sendKeys(Keys.CONTROL + "a");
		editbox_RoutingNumber.sendKeys(routingNumber);
		sendArbitraryKeys(Keys.TAB);
		waitForPostBack();
	}
	
	private void setAccountNumber(String accountNumber) {
		clickWhenClickable(editbox_AccountNumber);
		editbox_AccountNumber.sendKeys(Keys.CONTROL + "a");
		editbox_AccountNumber.sendKeys(accountNumber);
		sendArbitraryKeys(Keys.TAB);
		waitForPostBack();
	}
	
	public void fillOutACHEFTDetails(BankAccountType accountType, String name, String routingNumber, String accountNumber, String policyNumber) {
		setAccountType(accountType);
		setBankAccountHoldersName(name);
		setRoutingNumber(routingNumber);
		setAccountNumber(accountNumber);
		setPrefillFrom(policyNumber);
		setCopyPrimaryContactDetailsCheckBox(true);
	}
	
	public void setCopyPrimaryContactDetailsCheckBox(boolean trueOrFalse) {
		checkBox_BCNewPaymentCopyPrimaryContactDetails().select(trueOrFalse);
	}
}

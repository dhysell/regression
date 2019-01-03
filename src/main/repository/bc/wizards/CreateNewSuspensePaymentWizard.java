package repository.bc.wizards;

import com.idfbins.enums.OkCancel;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.bc.search.BCSearchAccounts;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;

import java.util.Date;

public class CreateNewSuspensePaymentWizard extends BasePage {
	private WebDriver driver;

	public CreateNewSuspensePaymentWizard(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	/////////////////////
	// Recorded Elements//
	/////////////////////

	@FindBy(xpath = "//input[contains(@id, ':paymentDate-inputEl')]")
	public static WebElement editbox_PaymentDate;

	@FindBy(xpath = "//input[contains(@id, ':EditSuspensePayment_FBMDV:Amount-inputEl')]")
	public static WebElement editbox_Amount;

	@FindBy(xpath = "//div[contains(@id, ':EditSuspensePayment_FBMDV:Amount-inputEl')]")
	public static WebElement text_Amount;

	@FindBy(xpath = "//input[contains(@id, ':EditSuspensePayment_FBMDV:AccountNumber-inputEl')]")
	public static WebElement editbox_AccountNumber;


	@FindBy(xpath = "//input[contains(@id, ':EditSuspensePayment_FBMDV:RefNumber-inputEl')]")
	public static WebElement editbox_RefNumber;

	@FindBy(xpath = "//a[contains(@id, ':AccountNumber:AccountPicker')]")
	public static WebElement link_AccountNumberPicker;

	Guidewire8Select select_PaymentMethod() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':EditSuspensePayment_FBMDV:paymentmethod-triggerWrap')]");
	}

	Guidewire8Select select_PaymentLocation() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':SuspensePaymentMethodInputSet:location-triggerWrap')]");
	}

	Guidewire8Select select_PolicyNumber() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':EditSuspensePayment_FBMDV:policyNumber-triggerWrap')]");
	}

	Guidewire8Select select_LoanNumber() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':EditSuspensePayment_FBMDV:loanNumber-triggerWrap')]");
	}

	Guidewire8Select select_County() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':SuspensePaymentMethodInputSet:county-triggerWrap')]");
	}

	Guidewire8Select select_CountyOffice() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':SuspensePaymentMethodInputSet:office-triggerWrap')]");
	}

	@FindBy(xpath = "//a[contains(@id, ':updateapply')]")
	public static WebElement button_UpdateAndApply;

	///////////////////////////////
	// Helper Methods for Elements//
	///////////////////////////////

	public void selectPaymentInstrument(repository.gw.enums.PaymentInstrumentEnum paymentInstrument) {
		select_PaymentMethod().selectByVisibleTextPartial(paymentInstrument.getValue());
		waitForPostBack(20);
	}

	public void selectPaymentLocation(repository.gw.enums.PaymentLocation paymentLocation) {
		select_PaymentLocation().selectByVisibleTextPartial(paymentLocation.getValue());
		waitForPostBack();
	}

	public void selectPaymentMethodRandom() {
		select_PaymentMethod().selectByVisibleTextRandom();
		waitForPostBack();
	}

	public String selectPaymentMethodRandomAndGetValue() {
		String foo = select_PaymentMethod().selectByVisibleTextRandom();
		waitForPostBack();
		return foo;

	}

	public boolean messageOKCancelDisplayed() {
		return checkIfElementExists("//div[contains(@id, 'messagebox-1001-displayfield-inputEl')]", 1000);
	}

	public void selectPaymentLocationRandom() {
		select_PaymentLocation().selectByVisibleTextRandom();
		waitForPostBack();
	}

	public String selectPaymentCountyRandom() {
		return select_County().selectByVisibleTextRandom();

	}

	public void selectPaymentCountyOfficeRandom() {
		select_CountyOffice().selectByVisibleTextRandom();
		waitForPostBack();
	}


	public void selectPolicyNumber(String policyNumber) {
		select_PolicyNumber().selectByVisibleTextPartial(policyNumber);
		waitForPostBack();
		clickProductLogo();
	}

	public void selectLoanNumber(String loanNumber) {
		select_LoanNumber().selectByVisibleText(loanNumber);
		waitForPostBack();
	}

	public void setSuspensePaymentAmount(double amount) {
		clickWhenClickable(editbox_Amount);
		editbox_Amount.sendKeys(Keys.CONTROL + "a");
		waitForPostBack(5);
		editbox_Amount.sendKeys(String.valueOf(amount));
		clickProductLogo();
	}

	public void setSuspensePaymentReferenceLoanNumber(String refLoanNumber) {
		editbox_RefNumber.sendKeys(Keys.CONTROL + "a");
		editbox_RefNumber.sendKeys(refLoanNumber);
		waitForPostBack();
	}

	public void setSuspensePaymentDate(Date paymentDate) {
		waitUntilElementIsClickable(editbox_PaymentDate);
		editbox_PaymentDate.sendKeys(Keys.CONTROL + "a");
		editbox_PaymentDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", paymentDate));
		clickProductLogo();
	}

	public void setAccountNumber(String accountNumber) {
		clickWhenClickable(editbox_AccountNumber, 30);
		waitForPostBack(5);
		editbox_AccountNumber.sendKeys(Keys.CONTROL+"a");
		editbox_AccountNumber.sendKeys(accountNumber);
		clickProductLogo();
	}

	public void clickAccountPicker() {
		clickWhenVisible(link_AccountNumberPicker);
		waitForPageLoad();
	}

	public void setAccountNumberFromPicker(String accountNumber) {
		waitForPostBack(20);
		clickAccountPicker();
		waitForPostBack();
		repository.bc.search.BCSearchAccounts search = new BCSearchAccounts(getDriver());
		search.setBCSearchAccountsAccountNumber(accountNumber);
		super.clickSearch();
		search.clickSelectAccountsButton();
		waitForPostBack();
	}

	public void clickCancel() {
		super.clickCancel();
		waitForPostBack();
	}

	public void clickCreate() {
		super.clickUpdate();
		waitForPostBack();
	}

	public void clickUpdateAndApply() {
		clickWhenVisible(button_UpdateAndApply);
		if(messageOKCancelDisplayed()){
			selectOKOrCancelFromPopup(OkCancel.OK);
			waitForPostBack();
		}
	}
	public double getSuspenseAmount() {
		return NumberUtils.getCurrencyValueFromElement(text_Amount.getText());
	}

	public void createNewSuspensePayment(Date date, double amount, repository.gw.enums.PaymentInstrumentEnum paymentInstrumentEnum, String accountNumber ){
		setSuspensePaymentDate(date);
		setSuspensePaymentAmount(amount);
		selectPaymentInstrument(paymentInstrumentEnum);
		if(paymentInstrumentEnum == repository.gw.enums.PaymentInstrumentEnum.ACH_EFT || paymentInstrumentEnum == PaymentInstrumentEnum.Credit_Debit){
			selectPaymentLocation(repository.gw.enums.PaymentLocation.NexusPayment);
		}
		setAccountNumber(accountNumber);
		clickCreate();
	}
}

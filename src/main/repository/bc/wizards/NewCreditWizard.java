package repository.bc.wizards;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.CreditType;

import java.text.DecimalFormat;
import java.util.List;

public class NewCreditWizard extends BasePage {
	
	private WebDriver driver;

	public NewCreditWizard(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	///////////////////////
	// "Recorded Elements"//
	///////////////////////

	public Guidewire8Select select_UnappliedFund() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':DesignatedUnapplieds-triggerWrap')]");
	}

	public Guidewire8Select select_CreditType() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':CreditTypeSubtype-triggerWrap') or contains(@id, ':CreditDetailsScreen:creditType-triggerWrap')]");
	}
	
	@FindBy(xpath = "//input[contains(@id, ':Amount-inputEl') or contains(@id, ':amount-inputEl')]")
	public WebElement editbox_BCCreditWizardAmount;
	
	public Guidewire8Checkbox checkBox_CreateDisbursement() {
		return new Guidewire8Checkbox(driver, "//table[contains(@id,':CreditDetailsScreen:disbFlag')]");
	}

	//////////////////
	// Helper Methods//
	//////////////////

	public String getUnappliedFund() {
		Guidewire8Select mySelect = select_UnappliedFund();
		return mySelect.getText();
	}

	public List<String> getUnappliedFundDropdownList() {
		Guidewire8Select mySelect = select_UnappliedFund();
		return mySelect.getList();
	}

	public void setUnappliedFund(String policyNumber) {
		Guidewire8Select mySelect = select_UnappliedFund();
		mySelect.selectByVisibleTextPartial(policyNumber);
	}

	public List<String> getCreditTypeDropdownList() {
		Guidewire8Select mySelect = select_CreditType();
		return mySelect.getList();
	}

	public String getCreditType() {
		Guidewire8Select mySelect = select_CreditType();
		return mySelect.getText();
	}
	
	public void setAmount(double amount) {
		DecimalFormat df = new DecimalFormat("####.##");
		clickWhenClickable(editbox_BCCreditWizardAmount);
		editbox_BCCreditWizardAmount.sendKeys(Keys.CONTROL + "a");
		editbox_BCCreditWizardAmount.sendKeys(df.format(amount));
		clickProductLogo();
	}

	public void setCreditType(CreditType creditType) {
		Guidewire8Select mySelect = select_CreditType();
		mySelect.selectByVisibleText(creditType.getValue());
		String value = mySelect.getText();
		if (!value.equalsIgnoreCase(creditType.getValue())) {
			mySelect.selectByVisibleText(creditType.getValue());
		}
	}
	
	public void createCredit(String policyNumber, CreditType type, double amount){
		setUnappliedFund(policyNumber);
		setCreditType(type);
		setAmount(amount);
		clickNext();
		clickFinish();
	}
	public void setCreateDisbursement(boolean trueOrFalse) {
		checkBox_CreateDisbursement().select(trueOrFalse);
	}
}

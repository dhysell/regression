package repository.bc.wizards;

import com.idfbins.enums.State;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.DisbursementReason;
import repository.gw.helpers.DateUtils;

import java.util.Date;

public class CreateDisbursementWizard extends BasePage {
	
	private WebDriver driver;

	public CreateDisbursementWizard(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	/////////////////////
	// Recorded Elements//
	/////////////////////

	Guidewire8Select select_ReasonForDisbursement() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':CreateDisbursementDetailDV:reason-triggerWrap')]");
	}

	Guidewire8Select select_StateProvince() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':CreateDisbursementDetailDV:state-triggerWrap')]");
	}

	@FindBy(xpath = "//input[contains(@id, ':CreateDisbursementDetailDV:amount-inputEl')]")
    public WebElement editbox_AmountToDisburse;

	@FindBy(xpath = "//input[contains(@id, ':CreateDisbursementDetailDV:effectiveDate-inputEl')]")
    public WebElement editbox_DueDate;

	@FindBy(xpath = "//input[contains(@id, ':CreateDisbursementDetailDV:payTo-inputEl')]")
    public WebElement editbox_PayTo;

	@FindBy(xpath = "//input[contains(@id, ':CreateDisbursementDetailDV:addressLIne1-inputEl')]")
    public WebElement editbox_AddressLine1;

	@FindBy(xpath = "//input[contains(@id, ':CreateDisbursementDetailDV:effectiveDate-inputEl')]")
    public WebElement editbox_AddressLine2;

	@FindBy(xpath = "//input[contains(@id, ':CreateDisbursementDetailDV:city-inputEl')]")
    public WebElement editbox_City;

	@FindBy(xpath = "//input[contains(@id, ':CreateDisbursementDetailDV:state-inputEl')]")
    public WebElement editbox_State;

	@FindBy(xpath = "//input[contains(@id, ':CreateDisbursementDetailDV:postalCode-inputEl')]")
    public WebElement editbox_PostalCode;

	///////////////////////////////
	// Helper Methods for Elements//
	///////////////////////////////

	public void selectReasonForDisbursement(DisbursementReason reason) {
		select_ReasonForDisbursement().selectByVisibleText(reason.getValue());
	}

	public void setDisbursementAmount(double amount) {
		editbox_AmountToDisburse.sendKeys(String.valueOf(amount));
	}

	public void setPayToName(String name) {
		editbox_PayTo.sendKeys(name);
	}

	public void setAddressLine1(String addressLine1) {
		editbox_AddressLine1.sendKeys(addressLine1);
	}

	public void setAddressLine2(String addressLine2) {
		editbox_AddressLine2.sendKeys(addressLine2);
	}

	public void setCity(String city) {
		editbox_City.sendKeys(city);
	}

	public void selectState(State state) {
		select_StateProvince().selectByVisibleText(state.getName());
	}

	public void setZipOrPostalCode(String zip) {
		editbox_PostalCode.sendKeys(zip);
	}

	public void setDisbursementDueDate(Date dueDate) {
		editbox_DueDate.sendKeys(Keys.CONTROL + "a");		
		editbox_DueDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", dueDate));
	}

	public void createDisbursement(double amount) {
		setDisbursementAmount(amount);
		clickNext();
		clickFinish();
	}

	public void fillOutPayeeInfo() {
		setPayToName("RandomName-"+DateUtils.dateFormatAsString("yyMMddHHmmss", new Date()));
		sendArbitraryKeys(Keys.TAB);
		sendArbitraryKeys("123 Main St");
		sendArbitraryKeys(Keys.TAB);
		sendArbitraryKeys(Keys.TAB);
		sendArbitraryKeys("Pocatello");
		selectState(State.Idaho);
		setZipOrPostalCode("83021");
	}

}

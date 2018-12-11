package repository.bc.policy;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentScheduleItemsToProcess;

public class PolicyPaymentSchedule extends BasePage {
	
	private WebDriver driver;

	public PolicyPaymentSchedule(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	//////////////////////////////////////////
	// "Recorded Elements" and their XPaths //
	//////////////////////////////////////////
	
	@FindBy(xpath = "//div[contains(@id, 'PolicyDetailPaymentsScreen:PaymentPlan-inputEl')]")
	public WebElement textbox_PaymentPlan;
	
	@FindBy(xpath = "//a[contains(@id,':PolicyDetailPaymentsScreen:ChangePaymentPlan')]")
	public WebElement button_EditSchedule;

	@FindBy(xpath = "//span[contains(@id,'ChangePaymentPlanPopup:Update-btnEl')]")
	public WebElement button_Execute;
	
	Guidewire8Select select_NewPaymentPlan() {
		return new Guidewire8Select(driver, "//table[contains(@id, 'ChangePaymentPlanPopup:PaymentPlan-triggerWrap')]");
	}

	Guidewire8RadioButton radio_RedistributePayments() {
		return new Guidewire8RadioButton(getDriver(), "//table[contains(@id, 'ChangePaymentPlanPopup:RedistributePayments')]");
	}

	Guidewire8RadioButton radio_ItemsToProcess() {
		return new Guidewire8RadioButton(getDriver(), "//div[contains(@id, 'ChangePaymentPlanPopup:InvoiceItemFilterType-containerEl')]");
	}

	//////////////////////////////////////
	// Helper Methods for Above Elements//
	//////////////////////////////////////
	
	public void clickEditSchedule() {
		clickWhenClickable(button_EditSchedule);
	}

	public void selectNewPaymentPlan(PaymentPlanType paymentPlan) {
		Guidewire8Select mySelect = select_NewPaymentPlan();
		mySelect.selectByVisibleTextPartial(paymentPlan.getValue());
	}

	public void clickExecute() {
		waitUntilElementIsVisible(button_Execute);
		button_Execute.click();
	}

	public void setRadioRedistributePaymentsReverseAndRedistribute() {
		radio_RedistributePayments().select(true);
	}

	public void setRadioRedistributePaymentsReverseOnly() {
		radio_RedistributePayments().select(false);
	}

	public void setRadioItemsToProcess( PaymentScheduleItemsToProcess itemsToProcess) {
		radio_ItemsToProcess().select(itemsToProcess.getValue());
	}

	public void changePaymentPlan(PaymentPlanType newPaymentPlanType) {
		clickEditSchedule();
		selectNewPaymentPlan(newPaymentPlanType);
		clickExecute();
	}
}

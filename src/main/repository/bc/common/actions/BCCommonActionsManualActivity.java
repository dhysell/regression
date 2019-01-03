package repository.bc.common.actions;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.ActivityQueuesBillingCenter;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.Priority;
import repository.gw.helpers.DateUtils;

import java.util.Date;

public class BCCommonActionsManualActivity extends BasePage  {
	
	private WebDriver driver;
	
	public BCCommonActionsManualActivity(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	//////////////////////////////////////////////
	// Recorded Elements and Corresponding XPaths//
	//////////////////////////////////////////////

	@FindBy(xpath = "//input[contains(@id,':ActivityDetailDV:ActivityDetailDV_Subject-inputEl')]")
	public WebElement editBox_ManualActivitySubject;

	@FindBy(xpath = "//textarea[contains(@id,':ActivityDetailDV:ActivityDetailDV_Description-inputEl') or contains(@id,':ActivityDetailDV_Description-inputEl')]")
	public WebElement editBox_ManualActivityDescription;


	@FindBy(xpath = "//input[contains(@id,':ActivityDetailDV:ActivityDetailDV_TargetDate-inputEl')]")
	public WebElement editBox_ManualActivityDueDate;

	@FindBy(xpath = "//input[contains(@id,':ActivityDetailDV:ActivityDetailDV_EscalationDate-inputEl')]")
	public WebElement editBox_ManualActivityEscalationDate;

	@FindBy(xpath = "//input[contains(@id,':ActivityDetailDV:EdgeEntitiesInputSet:account-inputEl')]")
	public WebElement editBox_ManualActivityAccount;

	@FindBy(xpath = "//a[contains(@id,':ActivityDetailDV:EdgeEntitiesInputSet:account:acctPicker')]")
	public WebElement button_ManualActivityAccountSearch;

	@FindBy(xpath = "//input[contains(@id,':ActivityDetailDV:EdgeEntitiesInputSet:policyPeriod-inputEl')]")
	public WebElement editBox_ManualActivityPolicyPeriod;

	@FindBy(xpath = "//a[contains(@id,':ActivityDetailDV:EdgeEntitiesInputSet:policyPeriod:ppPicker')]")
	public WebElement button_ManualActivityPolicyPeriodSearch;
	
	@FindBy(xpath = "//a[contains(@id,':ActivityDetailDV_AssignActivity:ActivityDetailDV_AssignActivity_PickerButton')]")
	public WebElement button_ManualActivityAssignedToSearch;

	@FindBy(xpath = "//div[contains(@id,':ActivityDetailDV:ActivityDetailDV_Status-inputEl')]")
	public WebElement label_ManualActivityStatus;

	@FindBy(xpath = "//input[contains(@id,':ParcActivityDescriptionInputSet:ENTERDRAFTDATE-inputEl') or contains(@id,':ParcActivityDescriptionInputSet:CancelDate-inputEl')]")
	public WebElement editBox_ManualActivityPARCDateInput;

	@FindBy(xpath = "//input[contains(@id,':ParcActivityDescriptionInputSet:ENTERDRAFTAMOUNT-inputEl')]")
	public WebElement editBox_ManualActivityPARCDraftAmount;

	@FindBy(xpath = "//input[contains(@id,':ParcActivityDescriptionInputSet:sourcePolicyPeriod-inputEl')]")
	public WebElement editBox_ManualActivityPARCTargetPolicy;

	@FindBy(xpath = "//input[contains(@id, ':ParcActivityDescriptionInputSet:periodicity-inputEl')]")
	public WebElement editBox_ManualActivityPARCNewPaymentPlan;


	public Guidewire8Select select_ManualActivityPriority() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':ActivityDetailDV:ActivityDetailDV_Priority-triggerWrap')]");
	}
	
	public Guidewire8Select select_ManualActivityAssignedTo() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':ActivityDetailDV:ActivityDetailDV_AssignActivity-triggerWrap')]");
	}

	Guidewire8Select select_NewPaymentPlan() {
		return new Guidewire8Select(driver, "//input[contains(@id, ':ParcActivityDescriptionInputSet:periodicity-inputEl')]");
	}

	////////////////////////////////////////////////
	// Helper Methods for Interacting With Elements//
	////////////////////////////////////////////////
	
	public void clickManualActivitySubject() {
		clickWhenClickable(editBox_ManualActivitySubject);
		
	}

	public void selectNewPaymentPlan(PaymentPlanType paymentPlan) {
		clickNewPaymentPlan();
		Guidewire8Select mySelect = select_NewPaymentPlan();
		mySelect.selectByVisibleTextPartial(paymentPlan.getValue());
	}

	public void clickNewPaymentPlan() {
		editBox_ManualActivityPARCNewPaymentPlan.click();
	}

	public void setManualActivitySubject(String subject) {
		clickWhenClickable(editBox_ManualActivitySubject);
		editBox_ManualActivitySubject.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editBox_ManualActivitySubject.sendKeys(subject);
	}
	
	public String getManualActivitySubject() {
		return editBox_ManualActivitySubject.getAttribute("value");
	}
	
	public void setManualActivityDescription(String description) {
		clickWhenClickable(editBox_ManualActivityDescription);
		editBox_ManualActivityDescription.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editBox_ManualActivityDescription.sendKeys(description);
	}
	
	public String getManualActivityDescription() {
		return editBox_ManualActivityDescription.getAttribute("value");
	}
	
	public void selectManualActivityPriority(Priority priority) {
		Guidewire8Select mySelect = select_ManualActivityPriority();
		mySelect.selectByVisibleText(priority.getValue());
		
	}
	
	public String getManualActivityPriority() {
		Guidewire8Select mySelect = select_ManualActivityPriority();
		return mySelect.getText();
	}
	
	public void setManualActivityDueDate(Date dueDate) {
		clickWhenClickable(editBox_ManualActivityDueDate);
		editBox_ManualActivityDueDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editBox_ManualActivityDueDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", dueDate));
	}
	
	public void setManualActivityDueDate(String dueDate) {
		clickWhenClickable(editBox_ManualActivityDueDate);
		editBox_ManualActivityDueDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		if (dueDate.equals("")) {
			editBox_ManualActivityDueDate.sendKeys(Keys.BACK_SPACE);
			editBox_ManualActivityDueDate.sendKeys(Keys.TAB);
		} else {
			editBox_ManualActivityDueDate.sendKeys(dueDate);
		}
	}
	
	public Date getManualActivityDueDate() {
		try {
			return (DateUtils.convertStringtoDate(editBox_ManualActivityDueDate.getAttribute("value"), "MM/dd/yyyy"));
		} catch (Exception e) {
			return null;
		}
	}
	
	public void setManualActivityEscalationDate(Date escalationDate) {
		clickWhenClickable(editBox_ManualActivityEscalationDate);
		editBox_ManualActivityEscalationDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editBox_ManualActivityEscalationDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", escalationDate));
	}
	
	public Date getManualActivityEscalationDate() {
		try {
			return (DateUtils.convertStringtoDate(editBox_ManualActivityEscalationDate.getAttribute("value"),
					"MM/dd/yyyy"));
		} catch (Exception e) {
			return null;
		}
	}
	
	public void setManualActivityAccount(String account) {
		clickWhenClickable(editBox_ManualActivityAccount);
		editBox_ManualActivityAccount.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editBox_ManualActivityAccount.sendKeys(account);
	}
	
	public String getManualActivityAccount() {
		return editBox_ManualActivityAccount.getAttribute("value");
	}
	
	public void clickManualActivityAccountSearch() {
		clickWhenClickable(button_ManualActivityAccountSearch);
		
	}
	
	public void setManualActivityPolicyPeriod(String policyPeriod) {
		clickWhenClickable(editBox_ManualActivityPolicyPeriod);
		editBox_ManualActivityPolicyPeriod.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editBox_ManualActivityPolicyPeriod.sendKeys(policyPeriod);
	}
	
	public String getManualActivityPolicyPeriod() {
		return editBox_ManualActivityPolicyPeriod.getAttribute("value");
	}
	
	public void clickManualActivityPolicyPeriodSearch() {
		clickWhenClickable(button_ManualActivityPolicyPeriodSearch);
		
	}
	
	public void selectManualActivityAssignedTo(ActivityQueuesBillingCenter queueToAssignActivity) {
		Guidewire8Select mySelect = select_ManualActivityAssignedTo();
		mySelect.selectByVisibleTextPartial(queueToAssignActivity.getValue());
		
	}
	
	public void selectManualActivityAssignedTo(String queueToAssignActivity) {
		Guidewire8Select mySelect = select_ManualActivityAssignedTo();
		mySelect.selectByVisibleTextPartial(queueToAssignActivity);
		
	}
	
	public String getManualActivityAssignedTo() {
		Guidewire8Select mySelect = select_ManualActivityAssignedTo();
		return mySelect.getText();
	}
	
	public void clickManualActivityAssignedToSearch() {
		clickWhenClickable(button_ManualActivityAssignedToSearch);
		
	}
}

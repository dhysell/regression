package repository.bc.account.summary;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;

public class InsuredDelinquencyPlan extends BasePage {

    private WebDriver driver;

    public InsuredDelinquencyPlan(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // -----------------------------------------------
    // "Recorded Elements" and their XPaths for Insured Delinquency Plan
    // -----------------------------------------------

    @FindBy(xpath = "//input[@id='DelinquencyPlanDetailPopup:DelinquencyPlanDetailScreen:DelinquencyPlanDetailDV:ExpirationDate-inputEl']")
    public WebElement editbox_InsuredDelinquencyPlanExpirationDate;

    @FindBy(xpath = "//a[@id='DelinquencyPlanDetailPopup:DelinquencyPlanDetailScreen:PlanReasonsTab']")
    public WebElement tab_InsuredDelinquencyPlanWorkflow;

    @FindBy(xpath = "//a[@id='DelinquencyPlanDetailPopup:DelinquencyPlanDetailScreen:GeneralDetailsTab']")
    public WebElement tab_InsuredDelinquencyPlanGeneral;

    @FindBy(xpath = "//a[@id='DelinquencyPlanDetailPopup:DelinquencyPlanDetailScreen:DelinquencyPlanReasonsDV:PlanReasonPanel:DelinquencyPlanReasonsLV_tb:Add']")
    public WebElement button_InsuredDelinquencyPlanWorkflowAddDelinquency;

    @FindBy(xpath = "//a[@id='DelinquencyPlanDetailPopup:DelinquencyPlanDetailScreen:DelinquencyPlanReasonsDV:DetailDV:DelinquencyPlanEventsLV_tb:Add']")
    public WebElement button_InsuredDelinquencyPlanWorkflowAddEvent;

    @FindBy(xpath = "//a[@id='DelinquencyPlanDetailPopup:DelinquencyPlanDetailScreen:DelinquencyPlanReasonsDV:PlanReasonPanel:DelinquencyPlanReasonsLV_tb:Remove']")
    public WebElement button_InsuredDelinquencyPlanWorkflowRemoveDelinquency;

    @FindBy(xpath = "//a[@id='DelinquencyPlanDetailPopup:DelinquencyPlanDetailScreen:DelinquencyPlanReasonsDV:DetailDV:DelinquencyPlanEventsLV_tb:Remove']")
    public WebElement button_InsuredDelinquencyPlanWorkflowRemoveEvent;

    @FindBy(xpath = "//input[@id='DelinquencyPlanEventPopup:Automatic_true-inputEl']")
    public WebElement radiobox_InsuredDelinquencyPlanEventAutomaticYes;

    @FindBy(xpath = "//input[@id='DelinquencyPlanEventPopup:Automatic_false-inputEl']")
    public WebElement radiobox_InsuredDelinquencyPlanEventAutomaticNo;

    public Guidewire8Select comboBox_AccountSummaryInsuredDelinquencyPlanEventName() {
        return new Guidewire8Select(driver, "//table[contains(@id,'DelinquencyPlanEventPopup:Name-triggerWrap')]");
    }

    public Guidewire8Select comboBox_AccountSummaryInsuredDelinquencyPlanEventTrigger() {
        return new Guidewire8Select(driver, "//table[contains(@id,'DelinquencyPlanEventPopup:TriggerBasis-triggerWrap')]");
    }

    // -------------------------------------------------------
    // Helper Methods for Above Elements for Insured Delinquency Plan
    // -------------------------------------------------------

    public void setInsuredDelinquencyPlanExpirationDate(String date) {
        clickWhenVisible(editbox_InsuredDelinquencyPlanExpirationDate);
        editbox_InsuredDelinquencyPlanExpirationDate.sendKeys(Keys.CONTROL + "a");
        editbox_InsuredDelinquencyPlanExpirationDate.sendKeys(date);
        
    }

    public void clickInsuredDelinquencyPlanGeneralTab() {
        clickWhenVisible(tab_InsuredDelinquencyPlanGeneral);
        
    }

    public void clickInsuredDelinquencyPlanWorkflowTab() {
        clickWhenVisible(tab_InsuredDelinquencyPlanWorkflow);
        
    }

    public void clickInsuredDelinquencyPlanAutomaticYes() {
        clickWhenVisible(radiobox_InsuredDelinquencyPlanEventAutomaticYes);
        
    }

    public void clickInsuredDelinquencyPlanAutomaticNo() {
        clickWhenVisible(radiobox_InsuredDelinquencyPlanEventAutomaticNo);
        
    }

    public void setInsuredDelinquencyPlanName(String name) {
        comboBox_AccountSummaryInsuredDelinquencyPlanEventName().selectByVisibleText(name);
        
    }

    public void setInsuredDelinquencyPlanTrigger(String trigger) {
        comboBox_AccountSummaryInsuredDelinquencyPlanEventTrigger().selectByVisibleText(trigger);
        
    }

    public void clickInsuredDelinquencyPlanWorkflowAddDelinquency() {
        clickWhenVisible(button_InsuredDelinquencyPlanWorkflowAddDelinquency);
        
    }

    public void clickInsuredDelinquencyPlanWorkflowAddEvent() {
        clickWhenVisible(button_InsuredDelinquencyPlanWorkflowAddEvent);
        
    }

    public void clickInsuredDelinquencyPlanWorkflowRemoveDelinquency() {
        clickWhenVisible(button_InsuredDelinquencyPlanWorkflowRemoveDelinquency);
        
    }

    public void clickInsuredDelinquencyPlanWorkflowRemoveEvent() {
        clickWhenVisible(button_InsuredDelinquencyPlanWorkflowRemoveEvent);
        
    }
}

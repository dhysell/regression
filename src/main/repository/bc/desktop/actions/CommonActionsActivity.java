package repository.bc.desktop.actions;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.NumberUtils;

import java.util.List;

public class CommonActionsActivity extends BasePage {

    private WebDriver driver;

    public CommonActionsActivity(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------

    @FindBy(xpath = "//*[contains(@id, 'ActivityDetailDV_Description') and contains(@label, 'Description')]")
    public WebElement editbox_CommonActionsActivityDescription;

    @FindBy(xpath = "//input[contains(@id, 'ActivityDetailDV_Subject') and contains(@label, 'Subject')]")
    public WebElement editbox_CommonActionsActivitySubject;

    @FindBy(xpath = "//select[contains(@id, 'ActivityDetailDV_Priority') and contains(@label, 'Priority')]")
    public WebElement select_CommonActionsActivityPriority;

    @FindBy(xpath = "//input[contains(@id, 'ActivityDetailDV_TargetDate') and contains(@label, 'Due Date')]")
    public WebElement editbox_CommonActionsActivityDueDate;

    @FindBy(xpath = "//input[contains(@id, 'ActivityDetailDV_EscalationDate') and contains(@label, 'Escalation Date')]")
    public WebElement editbox_CommonActionsActivityEscalationDate;

    @FindBy(xpath = "//input[contains(@id, 'account') and contains(@label, 'Account')]")
    public WebElement editbox_CommonActionsActivityAccount;

    @FindBy(xpath = "//a[contains(@id, 'account:acctPicker')]")
    public WebElement link_CommonActionsActivityAccountPicker;

    @FindBy(xpath = "//input[contains(@id, 'policyPeriod') and contains(@label, 'Policy Period')]")
    public WebElement editbox_CommonActionsActivityPolicyPeriod;

    @FindBy(xpath = "//a[contains(@id, 'policyPeriod:ppPicker')]")
    public WebElement link_CommonActionsActivityPolicyPeriodPicker;

    // @FindBy(xpath = "//select[contains(@id,
    // 'ActivityDetailDV_AssignActivity') and contains(@label, 'Assigned to')]")
    // public WebElement select_CommonActionsActivityAssignedTo;

    public Guidewire8Select comboBox_CommonActionsActivityAssignedTo() {
        return new Guidewire8Select(driver, "//table[contains(@id,':ActivityDetailDV_AssignActivity-triggerWrap')]");
    }

    @FindBy(xpath = "//a[contains(@id, 'ActivityDetailDV_AssignActivity_PickerButton')]")
    public WebElement link_CommonActionsActivityAssignedToPicker;

    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------


    public void setDesktopActionsActivitySubject(String subjectToFill) {
        waitUntilElementIsVisible(editbox_CommonActionsActivitySubject);
        editbox_CommonActionsActivitySubject.sendKeys(Keys.CONTROL + "a");
        editbox_CommonActionsActivitySubject.sendKeys(subjectToFill);
    }


    public void setDesktopActionsActivityDescription(String descriptionToFill) {
        waitUntilElementIsVisible(editbox_CommonActionsActivityDescription);
        editbox_CommonActionsActivityDescription.sendKeys(Keys.CONTROL + "a");
        editbox_CommonActionsActivityDescription.sendKeys(descriptionToFill);
    }


    public void setDesktopActionsActivityPriority(String priorityToSelect) {
        waitUntilElementIsVisible(select_CommonActionsActivityPriority);
        new Select(select_CommonActionsActivityPriority).selectByVisibleText(priorityToSelect);
    }


    public void setDesktopActionsActivityDueDate(String dateToFill) {
        waitUntilElementIsVisible(editbox_CommonActionsActivityDueDate);
        editbox_CommonActionsActivityDueDate.sendKeys(Keys.CONTROL + "a");
        editbox_CommonActionsActivityDueDate.sendKeys(dateToFill);
    }


    public void setDesktopActionsActivityEscalationDate(String dateToFill) {
        waitUntilElementIsVisible(editbox_CommonActionsActivityEscalationDate);
        editbox_CommonActionsActivityEscalationDate.sendKeys(Keys.CONTROL + "a");
        editbox_CommonActionsActivityEscalationDate.sendKeys(dateToFill);
    }


    public void setDesktopActionsActivityAccount(String numToFill) {
        waitUntilElementIsVisible(editbox_CommonActionsActivityAccount);
        editbox_CommonActionsActivityAccount.sendKeys(numToFill);
    }


    public void setDesktopActionsActivityPolicyPeriod(String periodToFill) {
        waitUntilElementIsVisible(editbox_CommonActionsActivityPolicyPeriod);
        editbox_CommonActionsActivityPolicyPeriod.sendKeys(periodToFill);
    }


    public void setDesktopActionsActivityAssignedTo(String nameToFill) {
        comboBox_CommonActionsActivityAssignedTo().selectByVisibleTextPartial(nameToFill);

    }


    public void randomSetDesktopActionsActivityAssignedTo() {
        List<String> theList = comboBox_CommonActionsActivityAssignedTo().getList();
        int listSize = theList.size();
        int currentRan = NumberUtils.generateRandomNumberInt(3, listSize);
        comboBox_CommonActionsActivityAssignedTo().selectByVisibleTextPartial(theList.get(currentRan - 1));

    }


    public void clickDesktopActionsActivityAccountPicker() {
        clickWhenVisible(link_CommonActionsActivityAccountPicker);
    }


    public void clickDesktopActionsActivityAssignedToPicker() {
        clickWhenVisible(link_CommonActionsActivityAssignedToPicker);
    }


    public void clickDesktopActionsActivityPolicyPeriodPicker() {
        clickWhenVisible(link_CommonActionsActivityPolicyPeriodPicker);
    }


    public void setDesktopActionsActivityAssignedTo(int indexToSelect) {
        // TODO Auto-generated method stub

    }


    public String getCurrentItemInAssignedToCombobox() {
        return comboBox_CommonActionsActivityAssignedTo().getText();

    }

}

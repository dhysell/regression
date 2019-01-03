package repository.ab.activity;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import persistence.globaldatarepo.entities.AbUsers;
import repository.ab.sidebar.SidebarAB;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;

public class NewActivity extends BasePage {
    private WebDriver driver;

    public NewActivity(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//span[contains(@id,'NewActivity:NewActivityScreen:NewActivity_UpdateButton-btnEl')]")
    private WebElement button_NewActivityUpdate;

    @FindBy(xpath = "//span[contains(@id,'NewActivity:NewActivityScreen:NewActivity_CancelButton-btnEl')]")
    private WebElement button_NewActivityCancel;
    //NewActivity:NewActivityScreen:NewActivityDV:Activity_Subject-inputEl
    @FindBy(xpath = "//input[contains(@id,'NewActivity:NewActivityScreen:NewActivityDV:Activity_Subject-inputEl')]")
    private WebElement editbox_NewActivitySubject;

    @FindBy(xpath = "//textarea[contains(@id,'NewActivity:NewActivityScreen:NewActivityDV:Activity_Description-inputEl')]")
    private WebElement textarea_NewActivityDescription;

    @FindBy(xpath = "//input[contains(@id,'NewActivity:NewActivityScreen:NewActivityDV:Activity_DueDate-inputEl')]")
    private WebElement editbox_NewActivityDueDate;

    @FindBy(xpath = "//input[contains(@id,'NewActivity:NewActivityScreen:NewActivityDV:Activity_EscalationDate-inputEl')]")
    private WebElement button_NewActivityEscalationDate;

    private Guidewire8Select select_NewActivityPriority() {
        return new Guidewire8Select(driver, "//table[contains(@id,'NewActivity:NewActivityScreen:NewActivityDV:Activity_Priority-triggerWrap')]");
    }

    private Guidewire8Select select_NewActivityAssignedGroup() {
        return new Guidewire8Select(driver, "//table[contains(@id,'NewActivity:NewActivityScreen:NewActivityDV:Activity_Group-triggerWrap')]");
    }

    private Guidewire8Select select_NewActivityAssignedUser() {
        return new Guidewire8Select(driver, "//table[contains(@id,'NewActivity:NewActivityScreen:NewActivityDV:Activity_User-triggerWrap')]");
    }


    // ***************************************************************************************************
    // Items Below are the Helper methods for the NewActivity Page Repository Items.
    // ***************************************************************************************************

    private void clickNewActivityUpdate() {
        clickWhenClickable(button_NewActivityUpdate);
    }

    private void clickNewActivityCancel() {
        clickWhenClickable(button_NewActivityCancel);
    }

    private void clickNewActivitySubject(String subject) {
    	setText(editbox_NewActivitySubject, subject);
    }

    private void clickNewActivityDescription(String description) {
    	setText(textarea_NewActivityDescription, description);
    }

    private void clickNewActivityDueDate(String dueDate) {
    	setText(editbox_NewActivityDueDate, dueDate);
    }

    private void clickNewActivityEscalationDate(String escalationDate) {
    	setText(button_NewActivityEscalationDate, escalationDate);
    }

    private void selectNewActivityPriority(String priority) {
        select_NewActivityPriority().selectByVisibleText(priority);
    }

    private void selectNewActivityAssignedGroup(String group) {
        select_NewActivityAssignedGroup().selectByVisibleText(group);
    }

    private void selectNewActivityAssignedUser(String user) {
        select_NewActivityAssignedUser().selectByVisibleText(user);
    }

    public void initiateActivity(String activityType) {
        repository.ab.sidebar.SidebarAB sideMenu = new SidebarAB(getDriver());
        sideMenu.clickActions();
        sideMenu.initiateReviewContactActivity(activityType);
    }

    public void sendActivity(String activityType, AbUsers userReceivesActivity) {
        initiateActivity(activityType);
        selectNewActivityAssignedGroup(userReceivesActivity.getUserDepartment());
        selectNewActivityAssignedUser(userReceivesActivity.getUserFirstName() + " " + userReceivesActivity.getUserLastName());
        clickNewActivityUpdate();
    }
}

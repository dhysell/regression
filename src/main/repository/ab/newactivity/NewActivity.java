package repository.ab.newactivity;


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

    @FindBy(xpath = "//*[contains(@id,'NewActivity:NewActivityScreen:NewActivityDV:Activity_Subject-inputEl')]")
    public WebElement input_NewActivitySubject;

    @FindBy(xpath = "//*[contains(@id,'NewActivity:NewActivityScreen:NewActivityDV:Activity_Description-bodyEl')]")
    public WebElement input_NewActivityDescription;

    @FindBy(xpath = "//*[contains(@id,'NewActivity:NewActivityScreen:NewActivityDV:Activity_DueDate-inputEl')]")
    public WebElement input_NewActivityDueDate;

    @FindBy(xpath = "//*[contains(@id,'NewActivity:NewActivityScreen:NewActivityDV:Activity_EscalationDate-inputEl')]")
    public WebElement input_NewActivityEscalationDate;

    public Guidewire8Select select_NewActivityPriority() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'NewActivity:NewActivityScreen:NewActivityDV:Activity_Priority-triggerWrap')]");
    }

    public Guidewire8Select select_NewActivityAssignedGroup() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'NewActivity:NewActivityScreen:NewActivityDV:Activity_Group-triggerWrap')]");
    }

    public Guidewire8Select select_NewActivityAssignedUser() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'NewActivity:NewActivityScreen:NewActivityDV:Activity_User-triggerWrap')]");
    }

    public void clickUpdate() {
        super.clickUpdate();
    }

    public void clickCancel() {
        super.clickCancel();
    }


    public void selectPriority(String priority) {
        Guidewire8Select relatedToSelect = select_NewActivityPriority();
        relatedToSelect.selectByVisibleText(priority);
    }

    public void selectGroup(AbUsers user) {
        Guidewire8Select relatedToSelect = select_NewActivityAssignedGroup();
        relatedToSelect.selectByVisibleText(user.getUserDepartment());
    }

    public void selectUser(AbUsers user) {
        Guidewire8Select relatedToSelect = select_NewActivityAssignedUser();
        relatedToSelect.selectByVisibleText(user.getUserFirstName() + " " + user.getUserLastName());
    }

    public void createReviewContactActivity(AbUsers userToGetActivity, String activityType) {
        repository.ab.sidebar.SidebarAB sideMenu = new SidebarAB(getDriver());
        sideMenu.initiateReviewContactActivity(activityType);
        selectGroup(userToGetActivity);
        selectUser(userToGetActivity);
        clickUpdate();

    }

}

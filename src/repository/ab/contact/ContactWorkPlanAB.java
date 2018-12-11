package repository.ab.contact;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.AbActivitySearchFilter;

public class ContactWorkPlanAB extends BasePage {

    private WebDriver driver;

    public ContactWorkPlanAB(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Elements and Xpaths

    // Assign Workplan Page
    @FindBy(xpath = "//a[@id='ContactActivities:ActivityAssignButton']")
    private WebElement button_ContactWorkPlanAssign;

    private WebElement checkbox_ContactWorkPlanActivity(String description) {
        return find(By.xpath("//div[contains(.,'" + description + "')]/ancestor::tr/td/div/img"));
    }

    private Guidewire8Select comboBox_ContactWorkPlanActivityFilter() {
        return new Guidewire8Select(driver, "//table[@id='ContactActivities:ActivityFilter-triggerWrap']");
    }

    // Assign on Assign Activities Page.
    @FindBy(xpath = "//a[@id='AssignActivitiesPopup:AssignmentPopupScreen:AssignmentPopupDV:AssignmentByGroupPopupScreen_ButtonButton']")
    private WebElement button_AssignActivitiesAssign;

    @FindBy(xpath = "//a[@id='ContactActivities:ActivityCompleteButton']")
    private WebElement button_ContactWorkplanComplete;

    @FindBy(xpath = "//span[@id='ContactActivities:DesktopQueuedActivities_AssignSelectedButton-btnEl']")
    private WebElement button_ContactWorkplanAssignSelectedToMe;

    public void selectContactWorkPlanActivityFilter(AbActivitySearchFilter filterBy) {
        comboBox_ContactWorkPlanActivityFilter().selectByVisibleText(filterBy.getValue());
    }

    private void selectContactWorkPlanAssignActivities(String user) {
        Guidewire8Select contactSuffix = new Guidewire8Select(driver, "AssignActivitiesPopup:AssignmentPopupScreen:AssignmentPopupDV:SelectFromList-triggerWrap");
        contactSuffix.selectByVisibleText(user);
    }

    public boolean clickActivityCheckbox(String description) {
        checkbox_ContactWorkPlanActivity(description).click();
        if(checkbox_ContactWorkPlanActivity(description).getAttribute("class").contains("checked")) {
        	return true;
        } else {
        	return false;
        }
    }

    // Assign on WorkPlan Page
    private void clickAssignButton() {
        waitUntilElementIsClickable(button_ContactWorkPlanAssign);
        button_ContactWorkPlanAssign.click();
    }

    private boolean assignButtonExists() {
        return checkIfElementExists(button_ContactWorkPlanAssign, 500);
    }

    // Assign on Assign Activities Page
    private void clickAssignActivitiesAssignButton() {
        waitUntilElementIsClickable(button_AssignActivitiesAssign);
        button_AssignActivitiesAssign.click();
    }

    private void clickContactWorkplanCompleteButton() {
        waitUntilElementIsClickable(button_ContactWorkplanComplete);
        button_ContactWorkplanComplete.click();
    }

    private void clickContactWorkplanAssignSelectedToMe() {
        clickWhenClickable(button_ContactWorkplanAssignSelectedToMe);
    }
    
    public void checkActivity(AbActivitySearchFilter filter, String activity, String activityType) {
    	selectContactWorkPlanActivityFilter(filter);
        clickActivityCheckbox(activity);
        clickActivityCheckbox(activityType);
    }
}

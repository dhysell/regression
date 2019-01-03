package repository.pc.search;


import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.ActivityStatus;
import repository.gw.enums.Priority;

public class SearchActivitiesPC extends BasePage {
	
	private WebDriver driver;

    public SearchActivitiesPC(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------
    @FindBy(xpath = "//input[contains(@id, ':AssignedUser-inputEl')]")
    private WebElement editbox_AssignedTo;

    public Guidewire8Select select_ActivityStatus() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':ActivitySearchDV:ActivityStatus-triggerWrap')]");
    }

    public Guidewire8Select select_Priority() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':ActivityPriority-triggerWrap')]");
    }

    public Guidewire8Select select_OverdueNow() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':ActivitySearchDV:OverdueNow-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id, ':ActivitiesSearchLV_tb:PrintMe-btnEl')]")
    private WebElement button_PrintExport;

    @FindBy(xpath = "//input[contains(@id, ':ActivitySearch_AssignButton-btnEl')]")
    private WebElement button_Assign;


    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------
    
    public void setAssignTo(String name) {
        waitUntilElementIsClickable(editbox_AssignedTo);
        editbox_AssignedTo.sendKeys(Keys.chord(Keys.CONTROL + "a"), name);
    }

    public void setActivityStatus(ActivityStatus status) {
        Guidewire8Select selectStatus = new Guidewire8Select(driver, "//table[contains(@id, 'SearchDV:ActivityStatus-triggerWrap')]");
        selectStatus.selectByVisibleTextPartial(status.getValue());
    }

    public void setPriority(Priority priority) {
        Guidewire8Select selectPriority = new Guidewire8Select(driver, "//table[contains(@id, 'SearchDV:ActivityPriority-triggerWrap')]");
        selectPriority.selectByVisibleTextPartial(priority.getValue());
    }

    public void setOverdueNow(boolean yesno) {
        String text;
        if (yesno) {
            text = "Yes";
        } else {
            text = "No";
        }
        Guidewire8Select selectPriority = new Guidewire8Select(driver, "//table[contains(@id, 'SearchDV:OverdueNow-triggerWrap')]");
        selectPriority.selectByVisibleTextPartial(text);
    }

    public void clickPrintExport() {
        clickWhenClickable(button_PrintExport);
    }

    public void clickAssign() {
        clickWhenClickable(button_Assign);
    }
}

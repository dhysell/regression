package repository.pc.desktop;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import repository.driverConfiguration.BasePage;

public class DesktopMyQueuesPC extends BasePage {

    public DesktopMyQueuesPC(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//select[contains(@id, 'DesktopAssignableQueues:DesktopAssignableQueuesScreen:DesktopAssignableQueuesLV_tb:AvailableQueues-inputEl')]")
    public WebElement select_DesktopMyQueuesFilter;

    @FindBy(xpath = "//select[contains(@id,'DesktopAssignableQueues:DesktopAssignableQueuesScreen:DesktopAssignableQueuesLV_tb:StatusFilter-inputEl')]")
    public WebElement select_DesktopMyQueuesStatusFilter;

    @FindBy(xpath = "//a[@id,'DesktopAssignableQueues:DesktopAssignableQueuesScreen:DesktopAssignableQueuesLV_tb:AssignFirstButton-btnInnerEl']")
    public WebElement button_DesktopMyQueuesAssignFirstToMeButton;

    @FindBy(xpath = "//a[@id,'DesktopAssignableQueues:DesktopAssignableQueuesScreen:DesktopAssignableQueuesLV_tb:AssignSelectedButton-btnInnerEl']")
    public WebElement button_DesktopMyQueuesFilterAssignSelectedToMeButton;

    @FindBy(xpath = "//a[@id,'DesktopAssignableQueues:DesktopAssignableQueuesScreen:DesktopAssignableQueuesLV_tb:AssignButton-btnInnerEl']")
    public WebElement button_DesktopMyQueuesAssignButton;

    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------


    public void setDesktopMyActivitiesMyQueuesFilter(String statusToSelect) {
        waitUntilElementIsVisible(select_DesktopMyQueuesFilter);
        new Select(select_DesktopMyQueuesFilter).selectByVisibleText(statusToSelect);
    }


    public void setDesktopMyActivitiesMyQueuesStatusFilter(String statusToSelect) {
        waitUntilElementIsVisible(select_DesktopMyQueuesStatusFilter);
        new Select(select_DesktopMyQueuesStatusFilter).selectByVisibleText(statusToSelect);
    }


    public void clickDesktopMyActivitiesMyQueuesAssignFirstToMeButton() {
        waitUntilElementIsClickable(button_DesktopMyQueuesAssignFirstToMeButton);
        button_DesktopMyQueuesAssignFirstToMeButton.click();
    }


    public void clickDesktopMyActivitiesMyQueuesAssignSelectedToMeButton() {
        waitUntilElementIsClickable(button_DesktopMyQueuesFilterAssignSelectedToMeButton);
        button_DesktopMyQueuesFilterAssignSelectedToMeButton.click();
    }


    public void clickDesktopMyActivitiesMyQueuesAssignButton() {
        waitUntilElementIsClickable(button_DesktopMyQueuesAssignButton);
        button_DesktopMyQueuesAssignButton.click();
    }
}
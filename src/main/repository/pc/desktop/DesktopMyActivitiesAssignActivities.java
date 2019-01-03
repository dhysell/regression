package repository.pc.desktop;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import repository.driverConfiguration.BasePage;

public class DesktopMyActivitiesAssignActivities extends BasePage {

    public DesktopMyActivitiesAssignActivities(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // Return to Activities
    @FindBy(xpath = "//a[@id='AssignActivitiesPopup:__crumb__']")
    public WebElement link_DesktopMyActivitiesAssignActivitiesReturnToActivitiesLink;

    // Current test to be edited//

    // On Assign Activities
    @FindBy(xpath = "//a[@id='AssignActivitiesPopup:AssignmentPopupScreen:AssignmentPopupScreen_CancelButton']")
    public WebElement button_DesktopMyActivitiesAssignActivitiesCancel;

    // RadioButton
    @FindBy(xpath = "//a[@id='AssignActivitiesPopup:AssignmentPopupScreen:AssignmentPopupDV:2')]")
    public WebElement select_DesktopMyActivitiesAssignActivitiesFindUserRadioButton;

    // Search for User
    @FindBy(xpath = "//a[@id='AssignActivitiesPopup:AssignmentPopupScreen:AssignmentPopupDV:AssignmentSearchInputSet:SearchFor')]")
    public WebElement select_DesktopMyActivitiesAssignActivitiesSearchFor;

    @FindBy(xpath = "//a[@id='AssignActivitiesPopup:AssignmentPopupScreen:AssignmentPopupDV:AssignmentSearchInputSet:Username')]")
    public WebElement select_DesktopMyActivitiesAssignActivitiesUserName;

    @FindBy(xpath = "//a[@id='AssignActivitiesPopup:AssignmentPopupScreen:AssignmentPopupDV:AssignmentSearchInputSet:FirstName')]")
    public WebElement select_DesktopMyActivitiesAssignActivitiesFirstName;

    @FindBy(xpath = "//a[@id='AssignActivitiesPopup:AssignmentPopupScreen:AssignmentPopupDV:AssignmentSearchInputSet:LastName')]")
    public WebElement select_DesktopMyActivitiesAssignActivitiesLastName;

    // SearchButton
    @FindBy(xpath = "//a[@id='AssignActivitiesPopup:AssignmentPopupScreen:AssignmentPopupDV:SearchAndResetInputSet:SearchLinksInputSet:Search_link')]")
    public WebElement button_DesktopMyActivitiesAssignActivitiesSearch;

    // ResetButton
    @FindBy(xpath = "//a[@id='AssignActivitiesPopup:AssignmentPopupScreen:AssignmentPopupDV:SearchAndResetInputSet:SearchLinksInputSet:Reset_link')]")
    public WebElement button_DesktopMyActivitiesAssignActivitiesReset;

    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------


    public void clickDesktopMyActivitiesAssignActivities() {
        waitUntilElementIsVisible(select_DesktopMyActivitiesAssignActivitiesFindUserRadioButton);
        new Select(select_DesktopMyActivitiesAssignActivitiesFindUserRadioButton);
    }


    public void clickDesktopMyActivitiesAssignCancel() {
        waitUntilElementIsVisible(button_DesktopMyActivitiesAssignActivitiesCancel);
        new Select(button_DesktopMyActivitiesAssignActivitiesCancel);
    }


    public void clickDesktopMyActivitiesAssignUserName() {
        waitUntilElementIsVisible(select_DesktopMyActivitiesAssignActivitiesUserName);
        new Select(select_DesktopMyActivitiesAssignActivitiesUserName);
    }


    public void clickDesktopMyActivitiesAssignFirstName() {
        waitUntilElementIsVisible(select_DesktopMyActivitiesAssignActivitiesFirstName);
        new Select(select_DesktopMyActivitiesAssignActivitiesFirstName);
    }


    public void clickDesktopMyActivitiesAssignLastName() {
        waitUntilElementIsVisible(select_DesktopMyActivitiesAssignActivitiesLastName);
        new Select(select_DesktopMyActivitiesAssignActivitiesLastName);
    }


    public void clickDesktopMyActivitiesAssignSearch() {
        waitUntilElementIsVisible(button_DesktopMyActivitiesAssignActivitiesSearch);
        new Select(button_DesktopMyActivitiesAssignActivitiesSearch);
    }


    public void clickDesktopMyActivitiesAssignReset() {
        waitUntilElementIsVisible(button_DesktopMyActivitiesAssignActivitiesReset);
        new Select(button_DesktopMyActivitiesAssignActivitiesReset);
    }


    public void clickDesktopMyActivitiesAssignSearchFor() {
        waitUntilElementIsVisible(select_DesktopMyActivitiesAssignActivitiesSearchFor);
        new Select(select_DesktopMyActivitiesAssignActivitiesSearchFor);
    }


    public void clickDesktopMyActivitiesAssignActivitiesReturnToActivitiesLink() {
        waitUntilElementIsVisible(link_DesktopMyActivitiesAssignActivitiesReturnToActivitiesLink);
        link_DesktopMyActivitiesAssignActivitiesReturnToActivitiesLink.click();
    }
}

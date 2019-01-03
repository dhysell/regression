package repository.cc.claim;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.WaitUtils;

public class VacationStatusCC extends BasePage {

    private WebDriver driver;
    private WaitUtils waitUtils;

    public VacationStatusCC(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.waitUtils = new WaitUtils(getDriver());
        PageFactory.initElements(driver, this);
    }

    // Elements

    public Guidewire8Select select_VacationStatus() {
        return new Guidewire8Select(driver, "//table[@id='UserVacationWorksheet:UserVacationScreen:UserVacationDV:VacationStatus-triggerWrap']");
    }

    public Guidewire8Select select_BackupUser() {
        return new Guidewire8Select(driver, "//table[@id='UserVacationWorksheet:UserVacationScreen:UserVacationDV:BackupUser-triggerWrap']");
    }

    @FindBy(xpath = "//span[contains(text(),'Vacation Status')]")
    public WebElement label_VacationStatus;

    @FindBy(xpath = "//a[@id='UserVacationWorksheet:UserVacationScreen:Update']")
    public WebElement button_Update;

    @FindBy(xpath = "//a[@id='UserVacationWorksheet:UserVacationScreen:Cancel']")
    public WebElement button_Cancel;

    // Helper Methods

    public void clickUpdateButton() {
        clickWhenClickable(button_Update);
    }

    public void clickCancelButton() {
        clickWhenClickable(button_Cancel);
    }

    public void selectVacationStatus(String selectString) {
        select_VacationStatus().selectByVisibleText(selectString);
    }

    public void setVacationStatusAtWork() {
        if (vacationStatusPopup()) {
            selectVacationStatus("At work");
            clickUpdateButton();
        }
    }

    @FindBy(xpath = "//span[contains(text(),'Vacation Status')]")
    private WebElement elementVacationStatus;

    public boolean vacationStatusPopup() {

        WebElement vacationStatusLabel = null;
        boolean popupFound = false;

        checkIfElementExists(vacationStatusLabel, 5000);

        try {
            waitUtils.waitUntilElementIsVisible(vacationStatusLabel, 3000);
            popupFound = true;
        } catch (Exception e) {
            System.out.println("Vacation Status popup not found.");
        }

        return popupFound;
    }

}

package repository.ab.vacationstatus;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;

public class VacationStatus extends BasePage {

    private WebDriver driver;
    public VacationStatus(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    Guidewire8Select select_VacationStatus() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'UserVacationWorksheet:UserVacationScreen:UserVacationDV:VacationStatus-triggerWrap')]");
    }

    Guidewire8Select select_VacationStatusBackupUser() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'UserVacationWorksheet:UserVacationScreen:UserVacationDV:BackupUser-triggerWrap')]");
    }

    @FindBy(xpath = "//span[contains(@id, 'UserVacationWorksheet:UserVacationScreen:Update-btnEl')]")
    private WebElement button_VacationStatusUpdate;

    @FindBy(xpath = "//span[contains(@id, 'UserVacationWorksheet:UserVacationScreen:Cancel-btnEl')]")
    private WebElement button_VacationStatusCancel;

    /*
     * Methods
     **/

    public void selectVacationStatus(repository.gw.enums.VacationStatus status) {
        select_VacationStatus().selectByVisibleText(status.getValue());
    }

    public void selectBackupUser(String user) {
        select_VacationStatusBackupUser().selectByVisibleText(user);
    }
}

package repository.cc.claim.incidents;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.NumberUtils;

import java.time.LocalDate;


public class NewUnverifiedVehicle extends BasePage {

    private WebDriver driver;

    public NewUnverifiedVehicle(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public void addNewVehicleRandom() {
        setOlieItem(Integer.toString(NumberUtils.generateRandomNumberInt(100, 999)));
        setYear(Integer.toString(NumberUtils.generateRandomNumberInt(1980, LocalDate.now().getYear())));
        setMake("Subaru");
        setModel("Outback");

        clickOk();
    }

    @FindBy(css = "span[id*=':NewClaimWizard_NewPolicyVehicleScreen:Update-btnInnerEl']")
    private WebElement okButton;

    private void clickOk() {
        clickWhenClickable(okButton);
    }

    @FindBy(css = "input[id*=':OlieItemNum-inputEl']")
    private WebElement olieItemInput;

    private void setOlieItem(String keysToSend) {
        waitUntilElementIsClickable(olieItemInput);
        olieItemInput.sendKeys(keysToSend);
    }

    @FindBy(css = "input[id*=':Year-inputEl']")
    private WebElement yearInput;

    private void setYear(String keysToSend) {
        yearInput.sendKeys(keysToSend);
    }

    @FindBy(css = "input[id*=':Make-inputEl']")
    private WebElement makeInput;

    private void setMake(String keysToSend) {
        makeInput.sendKeys(keysToSend);
    }

    @FindBy(css = "input[id*=':Model-inputEl']")
    private WebElement modelInput;

    private void setModel(String keysToSend) {
        modelInput.sendKeys(keysToSend);
    }
}

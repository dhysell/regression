package repository.cc.claim.incidents;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class EditVehicleIncidents extends BasePage {

    public EditVehicleIncidents(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//input[contains(@id,':FNOLVehicleIncidentScreen:LossParty_true-inputEl')]")
    private WebElement lossPartyInsured;

    private void clickLossPartyInsured() {
        clickWhenClickable(lossPartyInsured);
    }

    @FindBy(xpath = "//textarea[contains(@id,':FNOLVehicleIncidentScreen:Description-inputEl')]")
    private WebElement textAreaDamageDescription;

    private void setDamageDescription(String input) {
        textAreaDamageDescription.sendKeys(input);
    }

    @FindBy(xpath = "//a[contains(@id,':FNOLVehicleIncidentScreen:Update')]")
    private WebElement buttonOk;

    private void clickOkButton() {
        clickWhenClickable(buttonOk);
    }

    public void vehicle() {

        clickLossPartyInsured();
        
        setDamageDescription("Edit Vehicle Incident");
        
        clickOkButton();
    }

    public void thirdPartyVehicle() {
        waitUntilElementIsClickable(By.xpath("//textarea[contains(@id,':FNOLVehicleIncidentScreen:Description-inputEl')]"));
        setDamageDescription("Edit 3rd Party Vehicle Incident");
        
        clickOkButton();
    }

    public void recVehicle() {
        waitUntilElementIsClickable(By.xpath("//textarea[contains(@id,':FNOLVehicleIncidentScreen:Description-inputEl')]"));
        setDamageDescription("Edit Rec Vehicle Incident");
        
        clickOkButton();
    }
}

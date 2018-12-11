package repository.cc.claim;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;

import java.util.ArrayList;
import java.util.List;

public class ResidentialGlass extends BasePage {

    private WebDriver driver;

    public ResidentialGlass(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Elements
    // =============================================================================

    public Guidewire8Select select_ReportedByNameResidentialGlass() {
        return new Guidewire8Select(driver, "//table[@id='FNOLWizard:FNOLWizard_NewQuickClaimScreen:QuickClaimPanelSet:ReportedBy_Name-triggerWrap']");
    }

    public Guidewire8Select select_RelationResidentialGlass() {
        return new Guidewire8Select(driver, "//table[@id='FNOLWizard:FNOLWizard_NewQuickClaimScreen:QuickClaimPanelSet:Claim_ReportedByType-triggerWrap']");
    }

    public Guidewire8Select select_LossCauseResidentialGlass() {
        return new Guidewire8Select(driver, "//table[@id='FNOLWizard:FNOLWizard_NewQuickClaimScreen:QuickClaimPanelSet:Claim_LossCause-triggerWrap']");
    }

    @FindBy(xpath = "//textarea[@id='FNOLWizard:FNOLWizard_NewQuickClaimScreen:QuickClaimPanelSet:Claim_Description-inputEl']")
    public WebElement editbox_lossDescriptionResidentialGlass;

    @FindBy(xpath = "//a[@id='FNOLWizard:Finish']")
    public WebElement button_Finish;

    @FindBy(xpath = "//a[@id='NewClaimDuplicatesWorksheet:NewClaimDuplicatesScreen:NewClaimDuplicatesWorksheet_CloseButton']")
    public WebElement button_Close;

    // HELPERS
    // ==============================================================================

    public void clickFinishButton() {
        clickWhenClickable(button_Finish);
    }

    public void selectSpecific_ReportedByNameResidentialGlass(String selectString) {
        select_ReportedByNameResidentialGlass().selectByVisibleText(selectString);
    }

    public void selectSpecific_RelationResidentialGlass(String selectString) {
        select_RelationResidentialGlass().selectByVisibleText(selectString);
    }

    public void selectRandom_LossCauseResidentialGlass() {
        select_LossCauseResidentialGlass().selectByVisibleTextRandom();
    }

    public void setLossDescriptionResidentialGlass() {
        editbox_lossDescriptionResidentialGlass.sendKeys("Test - Damage Description");
    }

    public void closeAndFinish() {

        List<WebElement> closeButtons = finds(By.xpath(
                "//span[@id='NewClaimDuplicatesWorksheet:NewClaimDuplicatesScreen:NewClaimDuplicatesWorksheet_CloseButton-btnInnerEl']"));

        if (closeButtons.size() > 0) {
            try {
                clickWhenClickable(button_Close);
                clickWhenClickable(button_Finish);
            } catch (Exception e) {

            }
        }
    }

    public void getErrorMessagesResidentialGlass() {
        List<String> errorMessages = new ArrayList<>();
        boolean pass = true;

        List<WebElement> errorMessageElements = finds(By.xpath("//div[@class='message']"));
        if (errorMessageElements.size() > 0) {
            pass = false;
            for (WebElement webElement : errorMessageElements) {
                errorMessages.add(webElement.getText());
            }
            System.out.println(
                    "!!! ------------------------------------------- ERROR MESSAGES ------------------------------------------- !!!");
            for (String singleMessage : errorMessages) {
                System.out.println(singleMessage);
            }
            System.out.println(
                    "!!! ------------------------------------------- ERROR MESSAGES ------------------------------------------- !!!");
            Assert.assertTrue(pass);
        }
    }

    public void errorChecking() {

        boolean errorExist = true;

        while (errorExist == true) {
            List<WebElement> closeButton = finds(By.xpath(
                    "//a[@id='NewClaimDuplicatesWorksheet:NewClaimDuplicatesScreen:NewClaimDuplicatesWorksheet_CloseButton']"));
            List<WebElement> clearButton = finds(By
                    .xpath("//a[@id='WebMessageWorksheet:WebMessageWorksheetScreen:WebMessageWorksheet_ClearButton']"));

            if (closeButton.size() > 0) {
                closeAndFinish();
            } else if (clearButton.size() > 0) {
                getErrorMessagesResidentialGlass();
            } else {
                errorExist = false;
            }
        }
    }
}

package repository.cc.claim;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;

import java.util.ArrayList;
import java.util.List;

public class AutoERSorGlass extends BasePage {

    private WebDriver driver;

    public AutoERSorGlass(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ELEMENTS
    // =============================================================================

    public Guidewire8Select select_ReportedByNameERS() {
        return new Guidewire8Select(driver, "//table[@id='FNOLWizard:FNOLWizard_NewQuickClaimScreen:QuickClaimPanelSet:ReportedBy_Name-triggerWrap']");
    }

    public Guidewire8Select select_RelationERS() {
        return new Guidewire8Select(driver, "//table[@id='FNOLWizard:FNOLWizard_NewQuickClaimScreen:QuickClaimPanelSet:Claim_ReportedByType-triggerWrap']");
    }

    public Guidewire8Select select_LossCauseERS() {
        return new Guidewire8Select(driver, "//table[@id='FNOLWizard:FNOLWizard_NewQuickClaimScreen:QuickClaimPanelSet:Claim_LossCause-triggerWrap']");
    }

    @FindBy(xpath = "//textarea[@id='FNOLWizard:FNOLWizard_NewQuickClaimScreen:QuickClaimPanelSet:Claim_Description-inputEl']")
    public WebElement editBox_lossDescriptionERS;

    @FindBy(xpath = "//span[@id='NewClaimDuplicatesWorksheet:NewClaimDuplicatesScreen:NewClaimDuplicatesWorksheet_CloseButton-btnInnerEl']")
    public WebElement button_Close;

    @FindBy(xpath = "//input[@id='FNOLWizard:FNOLWizard_NewQuickClaimScreen:QuickClaimPanelSet:Claim_RepairQuestion_true-inputEl']")
    public WebElement radio_RepairYes;

    @FindBy(xpath = "//a[@id='FNOLWizard:Finish']")
    public WebElement button_Finish;

    // HELPERS
    // ==============================================================================

    public void clickFinishButton() {
        clickWhenClickable(button_Finish);
    }

    public void selectSpecific_ReportedByNameERS(String selectString) {
        Guidewire8Select mySelect = select_ReportedByNameERS();
        mySelect.selectByVisibleText(selectString);
    }

    public void selectSpecific_RelationERS(String selectString) {
        Guidewire8Select mySelect = select_RelationERS();
        mySelect.selectByVisibleText(selectString);
    }

    public String selectRandom_LossCauseERS() {
        Guidewire8Select mySelect = select_LossCauseERS();
        return mySelect.selectByVisibleTextRandom();
    }

    public void selectSpecific_LossCauseERS(String cause) {
        Guidewire8Select mySelect = select_LossCauseERS();
        mySelect.selectByVisibleTextPartial(cause);
    }

    public void selectRoadSide_LossCauseERS() {
        select_LossCauseERS().selectByVisibleText("Roadside Assistance");
    }

    public String getLossCauseERSValue() {
        String selectedText = select_LossCauseERS().getText();
        select_LossCauseERS().sendKeys(Keys.ESCAPE);
        return selectedText;
    }

    public void setLossDescriptionERS(String description) {
        editBox_lossDescriptionERS.sendKeys(description);
    }

    public void clickRadioRepairYes() {
        radio_RepairYes.click();
    }

    public void closeAndFinish() {

        boolean available = false;
        try {
            waitUntilElementIsVisible(By.xpath(
                    "//span[@id='NewClaimDuplicatesWorksheet:NewClaimDuplicatesScreen:NewClaimDuplicatesWorksheet_CloseButton-btnInnerEl']"));
            available = true;
        } catch (Exception e) {

        }

        List<WebElement> closeButtons = finds(By.xpath(
                "//span[@id='NewClaimDuplicatesWorksheet:NewClaimDuplicatesScreen:NewClaimDuplicatesWorksheet_CloseButton-btnInnerEl']"));

        if (closeButtons.size() > 0 && available) {
            clickWhenClickable(button_Close);
            waitUntilElementIsNotVisible(button_Close);
            waitUntilElementNotClickable(By.cssSelector("div[role='presentation'][class='x-mask x-mask-fixed']"));
            clickWhenClickable(button_Finish);

        }
    }

    public void errorChecking() {
        List<String> errMessages = new ArrayList<>();
        boolean pass;

        closeAndFinish();
        List<WebElement> clearButton = finds(
                By.xpath("//a[@id='WebMessageWorksheet:WebMessageWorksheetScreen:WebMessageWorksheet_ClearButton']"));
        if (clearButton.size() > 0) {
            List<WebElement> errorMessageElements = finds(By.xpath("//div[@class='message']"));
            if (errorMessageElements.size() > 0) {
                pass = false;
                for (WebElement webElement : errorMessageElements) {
                    errMessages.add(webElement.getText());
                }
                System.out.println("The Safelite FNOL returned the following error messages:");
                for (String webElement : errMessages) {
                    System.out.println(webElement);
                }
                Assert.assertEquals(pass, true,
                        "Check Console log, more than likely the test tried to pick an incident without valid coverage.");
            }
        }

        closeAndFinish();

    }
}

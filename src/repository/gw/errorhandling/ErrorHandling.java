package repository.gw.errorhandling;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

import java.util.List;

public class ErrorHandling extends BasePage {

    public ErrorHandling(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------

    public WebElement button_Clear() {
        return find(By.xpath("//span[contains(@id, 'ClearButton-btnEl') or contains(@id, 'WebMessageWorksheet_ClearButton-btnEl')]"));
    }


    public WebElement text_ErrorHandlingErrorBanner() {
        return find(By.xpath("//div[contains(@id, '_msgs')]/div[1]"));
    }


    public WebElement text_ErrorHandlingValidationResultsErrorBanner() {
        return find(By.xpath("//div[contains(@id, 'grpMsgs')]"));
    }


    public List<WebElement> text_ErrorHandlingValidationResults() {
        return finds(By.xpath("//div[@id='WebMessageWorksheet:WebMessageWorksheetScreen:grpMsgs']"));
    }


    public WebElement text_ErrorHandlingJobType() {
        return find(By.xpath("//span[contains(@id, ':JobLabel-btnInnerEl')]"));
    }


    public List<WebElement> text_ErrorHandlingErrorBannerMessages() {
        return finds(By.xpath("//div[contains(@id, ':_msgs')]/child::div | //div[contains(@id, 'galertbar')]/descendant::span[contains(@class, 'g-link')]"));
    }


    public WebElement link_ErrorHandlingOnAdditionalCoverages() {
        return find(By.linkText("On \"Additional Coverages\":"));
    }


    public WebElement link_ErrorHandlingOnDetails() {
        return find(By.linkText("On \"Details\":"));
    }


    public WebElement link_ErrorHandlingOnIncludedCoverages() {
        return find(By.linkText("On \"Included Coverages\":"));
    }


    public WebElement link_ErrorHandlingOnLocationDetails() {
        return find(By.linkText("On \"Details\":"));
    }


    public WebElement text_ValidationResults() {
        return find(By.xpath("//span[contains(text(), 'Validation Results')]"));
    }


    public boolean checkIfValidationResultsExists() {
        return checkIfElementExists("//span[contains(text(), 'Validation Results')]", 1000);
    }


    public List<WebElement> getValidationMessages() {
        return finds(By.xpath("//div[contains(@class, 'message')]"));
    }


    public boolean validationMessageExists(String messageText) {
        for (WebElement msg : getValidationMessages()) {
            if (msg.getText().contains(messageText)) {
                return true;
            }
        }
        return false;
    }
}

package repository.cc.claim;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.WaitUtils;

public class ValidationResults extends BasePage {

    private WaitUtils waitUtils;

    public ValidationResults(WebDriver webDriver) {
        super(webDriver);
        this.waitUtils = new WaitUtils(webDriver);
    }

    @FindBy(css = "a[id*=':WebMessageWorksheet_ClearButton']")
    private WebElement buttonClear;

    public void clickClearButton() {
        waitUtils.waitUntilElementIsClickable(buttonClear, 5);
        buttonClear.click();
    }

    public boolean isValidationResultsVisible() {
        try {
            waitUtils.waitUntilElementIsVisible(buttonClear);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

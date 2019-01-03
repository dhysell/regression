package repository.cc.claim;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.WaitUtils;

public class OrderPoliceReport extends BasePage {

    WaitUtils waitUtils;

    public OrderPoliceReport(WebDriver webDriver) {
        super(webDriver);
        this.waitUtils = new WaitUtils(webDriver);
        PageFactory.initElements(webDriver, this);
    }

    @FindBy(css = "span[id*='DelayReport-btnInnerEl']")
    private WebElement buttonRandomDelayTest;

    public void clickRandomDelayTest() {
        waitUtils.waitUntilElementIsClickable(buttonRandomDelayTest);
        buttonRandomDelayTest.click();
        waitUtils.waitUntilElementIsNotVisible(buttonRandomDelayTest, 20);
    }

    @FindBy(css = "span[id*='SuccessReport-btnInnerEl']")
    private WebElement buttonRandomSuccessTest;

    public void clickRandomSuccessTest() {
        waitUtils.waitUntilElementIsClickable(buttonRandomSuccessTest);
        buttonRandomSuccessTest.click();
        waitUtils.waitUntilElementIsNotVisible(buttonRandomSuccessTest, 20);
    }

}

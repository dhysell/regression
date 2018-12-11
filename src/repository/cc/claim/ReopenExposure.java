package repository.cc.claim;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.WaitUtils;

public class ReopenExposure extends BasePage {

    private WaitUtils waitUtils;
    private WebDriver driver;

    public ReopenExposure(WebDriver driver) {
        super(driver);
        this.waitUtils = new WaitUtils(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//a[contains(@id,':Update')]")
    WebElement buttonReopenExposure;

    public void clickReopenExposureButton() {
        clickWhenClickable(buttonReopenExposure);
    }

    @FindBy(xpath = "//a[@id='ReopenExposurePopup:ReopenExposureScreen:Cancel']")
    WebElement button_Cancel;

    public void clickCancelButton() {
        clickWhenClickable(button_Cancel);
    }

    @FindBy(xpath = "//textarea[contains(@id,':Note-inputEl')]")
    WebElement textAreaNote;

    public void setNote(String note) {
        waitUtils.waitUntilElementIsVisible(textAreaNote);
        textAreaNote.sendKeys(note);
    }

    public Guidewire8Select select_Reason() {
        return new Guidewire8Select(
                driver, "//table[@id='ReopenExposurePopup:ReopenExposureScreen:ReopenExposureInfoDV:Reason-triggerWrap']");
    }

    public void selectReason(String reason) {
        select_Reason().selectByVisibleText(reason);
    }

    public void ReopenExposure() {
        setNote("Test Reopen exposure Note Field.");
        selectReason("Mistake");
        clickReopenExposureButton();
    }

}

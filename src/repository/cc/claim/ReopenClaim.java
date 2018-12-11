package repository.cc.claim;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;

public class ReopenClaim extends BasePage {

    private WebDriver driver;

    public ReopenClaim(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//a[contains(@id,':Update')]")
    WebElement buttonReopenClaim;

    public void clickReopenClaimButton() {
        clickWhenClickable(buttonReopenClaim);
    }

    @FindBy(xpath = "//a[@id='ReopenClaimPopup:ReopenClaimScreen:Cancel']")
    WebElement button_Cancel;

    public void clickCancelButton() {
        clickWhenClickable(button_Cancel);
    }

    @FindBy(xpath = "//textarea[@id='ReopenClaimPopup:ReopenClaimScreen:ReopenClaimInfoDV:Note-inputEl']")
    WebElement textArea_Note;

    public void setNote(String note) {
        textArea_Note.sendKeys(note);
    }

    public Guidewire8Select select_Reason() {
        return new Guidewire8Select(
                driver, "//table[@id='ReopenClaimPopup:ReopenClaimScreen:ReopenClaimInfoDV:Reason-triggerWrap']");
    }

    public void selectReason(String reason) {
        select_Reason().selectByVisibleText(reason);
    }

    public void reopenClaim() {
        setNote("Test Reopen claim Note Field.");
        selectReason("Mistake");
        clickReopenClaimButton();
    }

}

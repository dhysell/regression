package repository.ab.contact;

import com.idfbins.enums.OkCancel;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.ReturnMailReason;

public class ReturnMailPopupAB extends BasePage {
    private WebDriver driver;

    public ReturnMailPopupAB(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ***************************************************************************************************
    // Items Below are the Repository Items in the Contact Details Addresses
    // Page.
    // ***************************************************************************************************

    @FindBy(xpath = "//*[@id='ReturnMailPopup:MarkReturnMail-btnEl']")
    private WebElement button_ReturnMailMarkReturnMail;

    @FindBy(xpath = "//*[@id='ReturnMailPopup:UnmarkReturnMail-btnEl']")
    private WebElement button_ReturnMailUnmarkReturnMail;

    private Guidewire8Select select_ReturnMailPopupReturnMailReason() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'ReturnMailPopup:ReturnMailReason-triggerWrap')]");
    }

    public void clickOK() {
        super.clickOK();
    }

    public void clickCancel() {
        super.clickCancel();
    }

    public void clickMarkReturnMail(ReturnMailReason reason) {
        clickWhenClickable(button_ReturnMailMarkReturnMail);
        selectOKOrCancelFromPopup(OkCancel.OK);
        selectReturnReason(reason);
        clickOK();
        waitForPageLoad();
    }

    private void clickUnmarkReturnMail() {
        clickWhenClickable(button_ReturnMailUnmarkReturnMail);
    }

    private void selectReturnReason(ReturnMailReason reason) {
        Guidewire8Select mySelect = select_ReturnMailPopupReturnMailReason();
        mySelect.selectByVisibleText(reason.getValue());
    }
}
	
	


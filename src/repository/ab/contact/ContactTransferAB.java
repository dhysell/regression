package repository.ab.contact;


import com.idfbins.enums.OkCancel;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.ab.search.AdvancedSearchAB;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8RadioButton;

public class ContactTransferAB extends BasePage {

    private WebDriver driver;

    public ContactTransferAB(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // **************************************************************************
    // Methods below are the Repository Items to the Contact Details Basics
    // Page.
    // ***************************************************************************

    @FindBy(xpath = "//span[contains(@id, ':ToolbarButtonTransfer-btnWrap')]")
    private WebElement button_ContactTransferTransfer;

    @FindBy(xpath = "//div[@id = 'Transfer:ToolbarButtonCancel-btnEl']")
    private WebElement button_ContactTransferCancel;

    @FindBy(xpath = "//div[contains(@id, ':ValidContact:SelectValidContact')]")
    private WebElement button_ContactTransferContactPicker;

    private Guidewire8RadioButton radio_ContactTransferWithActivityYes() {
        return new Guidewire8RadioButton(getDriver(), "//table[@id='Transfer:WithActivity_true']");
    }

    private Guidewire8RadioButton select_ContactTransferWithActivityNo() {
        return new Guidewire8RadioButton(getDriver(), "//table[@id='Transfer:WithActivity_false']");
    }

    // **************************************************************************
    // Methods below are the helper methods to the Contact Details Basics Page.
    // ***************************************************************************

    private void clickTransferButton() {
        clickWhenClickable(button_ContactTransferTransfer);
        selectOKOrCancelFromPopup(OkCancel.OK);
        ContactDetailsBasicsAB newContactPage = new ContactDetailsBasicsAB(driver);
        int lcv = 0;
        boolean found = false;
        do {
            newContactPage = new ContactDetailsBasicsAB(driver);
            String contactPageTitle = newContactPage.getContactPageTitle();
            if (!contactPageTitle.isEmpty()) {
                found = true;
            }
            lcv++;
        } while (found == false && lcv < 10);
    }

    private void clickContactPicker() {
        clickWhenClickable(button_ContactTransferContactPicker);
    }

    private void contactTransferRadioClicker(boolean radioValue) {
        Guidewire8RadioButton radio = new Guidewire8RadioButton(getDriver(), "//div[contains(@id, 'ContactTransfer:TransferPanelSet:WithActivity-containerEl')]/table");
        radio.select(radioValue);
    }
    
    public void transferContact(String acctNum) {
    	clickContactPicker();
        repository.ab.search.AdvancedSearchAB searchNewContact = new AdvancedSearchAB(driver);
        searchNewContact.searchByAccountNumber(acctNum);
        contactTransferRadioClicker(true);
        clickTransferButton();
    }
}

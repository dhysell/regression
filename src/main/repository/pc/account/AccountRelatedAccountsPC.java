package repository.pc.account;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;

public class AccountRelatedAccountsPC extends BasePage {

    private WebDriver driver;

    public AccountRelatedAccountsPC(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------

    @FindBy(xpath = "//span[contains(@id, 'AddRelatedAccount-btnEl')]")
    public WebElement button_Add;

    @FindBy(xpath = "//span[contains(@id, 'RemoveRelatedAccount')]")
    public WebElement button_Remove;

    @FindBy(xpath = "//span[contains(@id, ':AccountSearchResultsLV_tb:Search-btnEl')]")
    public WebElement button_Search;

    // ACCOUNT RELATIONSHIP
    @FindBy(xpath = "//span[contains(@id, 'RelatedAccountPopup:Cancel-btnEl')]")
    public WebElement button_Cancel;

    @FindBy(xpath = "//span[contains(@id, 'RelatedAccountPopup:Update-btnEl')]")
    public WebElement button_Update;

    public Guidewire8Select select_Relationship() {
        return new Guidewire8Select(driver, "//table[(@id='RelatedAccountPopup:RelationshipType-triggerWrap')]");
    }

    @FindBy(xpath = "//input[(@id='RelatedAccountPopup:RelatedAccount')]")
    public WebElement textbox_RelatedAccount;

    @FindBy(xpath = "//div[contains(@id, ':SelectRelatedAccount')]")
    public WebElement link_SearchIcon;

    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------


    public void clickAdd() {
        waitUntilElementIsClickable(button_Add);
        button_Add.click();
    }


    public void clickRemove() {
        waitUntilElementIsClickable(button_Remove);
        button_Remove.click();
    }


    public void clickSearch() {
        waitUntilElementIsClickable(button_Search);
        button_Search.click();
    }


    public void clickCancel() {
        waitUntilElementIsClickable(button_Cancel);
        button_Cancel.click();
    }


    public void clickUpdate() {
        waitUntilElementIsClickable(button_Update);
        button_Update.click();
    }


    public void clickSearchIcon() {
        hoverOverAndClick(link_SearchIcon);
        // link_SearchIcon.click();
    }


    public void selectRelationship(String relationship) {
        select_Relationship().selectByVisibleText(relationship);
    }


    public String getRelatedAccount() {
        return textbox_RelatedAccount.getText();
    }


    public void setRelatedAccount(String accountNumber) {
        waitUntilElementIsClickable(textbox_RelatedAccount);
        textbox_RelatedAccount.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        textbox_RelatedAccount.sendKeys(accountNumber);
    }

}

package repository.ab.addresstandardization;

import com.idfbins.enums.State;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;

public class AddressStandardizationError extends BasePage {

    private WebDriver driver;

    public AddressStandardizationError(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//*[contains(@id,'AddressStandardizationPopup:LocationScreen:ttlBar')]")
    private WebElement text_AddressStandardizationErrorTitle;

    @FindBy(xpath = "//*[contains(@id,'AddressStandardizationPopup:__crumb__')]")
    private WebElement link_AddressStandardizationErrorReturnTo;

    @FindBy(xpath = "//*[contains(@id,'AddressStandardizationPopup:LocationScreen:Standardize-btnEl')]")
    private WebElement button_AddressStandardizationErrorStandardize;

    @FindBy(xpath = "//*[contains(@id,'AddressStandardizationPopup:LocationScreen:Update-btnEl')]")
    private WebElement button_AddressStandardizationErrorOverride;

    @FindBy(xpath = "//*[contains(@id,'AddressStandardizationPopup:LocationScreen:AddressLine1-inputEl')]")
    private WebElement editbox_AddressStandardizationErrorAddressLine1;

    @FindBy(xpath = "//*[contains(@id,'AddressStandardizationPopup:LocationScreen:AddressLine2-inputEl')]")
    private WebElement editbox_AddressStandardizationErrorAddressLine2;

    @FindBy(xpath = "//*[contains(@id,'AddressStandardizationPopup:LocationScreen:City-inputEl')]")
    private WebElement editbox_AddressStandardizationErrorCity;

    @FindBy(xpath = "//*[contains(@id,'AddressStandardizationPopup:LocationScreen:PostalCode-inputEl')]")
    private WebElement editbox_AddressStandardizationErrorZip;

    private Guidewire8Select select_AddressStandardizationErrorState() {
        return new Guidewire8Select(driver, "//table[@id='AddressStandardizationPopup:LocationScreen:State-triggerWrap']");
    }

    // ***************************************************************************************************
    // Items Below are the Helper methods for the AddressStandardizationError
    // Page Repository Items.
    // ***************************************************************************************************

    public String getAddressStandardizationErrorPageTitle() {
        waitUntilElementIsVisible(text_AddressStandardizationErrorTitle);
        String text = text_AddressStandardizationErrorTitle.getText();
        return text;
    }

    public void clickAddressStandardizationErrorReturnTo() {
        waitUntilElementIsClickable(link_AddressStandardizationErrorReturnTo);
        link_AddressStandardizationErrorReturnTo.click();
    }

    public void clickAddressStandardizationErrorStandardizeLink() {
        waitUntilElementIsClickable(button_AddressStandardizationErrorStandardize);
        button_AddressStandardizationErrorStandardize.click();
    }

    public void clickAddressStandardizationErrorOverrideLink() {
        waitUntilElementIsClickable(button_AddressStandardizationErrorOverride);
        button_AddressStandardizationErrorOverride.click();
    }

    public boolean checkAddressStandardizationErrorOverrideLinkExists() {
        boolean overrideExists = checkIfElementExists(
                "//*[contains(@id,'AddressStandardizationPopup:LocationScreen:Update-btnEl')]", 3);
        if (overrideExists) {
            waitUntilElementIsClickable(button_AddressStandardizationErrorOverride);
            button_AddressStandardizationErrorOverride.click();
        }
        return overrideExists;
    }

    public void setAddressStandardizationErrorAddressLine1(String addressLine1) {
        waitUntilElementIsClickable(editbox_AddressStandardizationErrorAddressLine1);
        editbox_AddressStandardizationErrorAddressLine1.sendKeys(addressLine1);
    }

    public void setAddressStandardizationErrorAddressLine2(String addressLine2) {
        waitUntilElementIsClickable(editbox_AddressStandardizationErrorAddressLine2);
        editbox_AddressStandardizationErrorAddressLine2.sendKeys(addressLine2);
    }

    public void setAddressStandardizationErrorCity(String city) {
        waitUntilElementIsClickable(editbox_AddressStandardizationErrorCity);
        editbox_AddressStandardizationErrorCity.sendKeys(city);
    }

    public void setAddressStandardizationErrorZip(String zip) {
        waitUntilElementIsClickable(editbox_AddressStandardizationErrorZip);
        editbox_AddressStandardizationErrorZip.sendKeys(zip);
    }

    public void selectSpecific_State(State stateToSelect) {
        select_AddressStandardizationErrorState().selectByVisibleText(stateToSelect.getName());
    }

}

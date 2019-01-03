package repository.gw.addressstandardization;

import com.idfbins.enums.State;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.generate.custom.AddressInfo;

import java.util.List;

public class AddressStandardization extends BasePage {
	
	private WebDriver driver;

    public AddressStandardization(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // **********************************Elements***************************************************

    @FindBy(xpath = "//span[contains(@id, 'AddressStandardizationPopup:LocationScreen:ttlBar')]")
    public WebElement text_AddressStandardizationTitle;

    @FindBy(xpath = "//a[contains(@id, 'AddressStandardizationPopup:__crumb__')]")
    public WebElement link_AddressStandardizationReturnTo;

    public List<WebElement> link_AddressStandardizationSelect(String partialAddressLine1) {
        return finds(By.xpath("//div[contains(@id, 'AddressStandardizationPopup:LocationScreen:4-body')]/descendant::tr[contains(., '" + partialAddressLine1 + "')]/descendant::a[contains(., 'Select')]"));
    }

    @FindBy(xpath = "//*[contains(@id,'AddressStandardizationPopup:LocationScreen:Standardize-btnEl')]")
    public WebElement button_AddressStandardizationStandardize;

    @FindBy(xpath = "//*[contains(@id,'AddressStandardizationPopup:LocationScreen:Update-btnEl')]")
    public WebElement button_AddressStandardizationOverride;

    @FindBy(xpath = "//*[contains(@id,'AddressStandardizationPopup:LocationScreen:AddressLine1-inputEl')]")
    public WebElement editbox_AddressStandardizationAddressLine1;

    @FindBy(xpath = "//*[contains(@id,'AddressStandardizationPopup:LocationScreen:AddressLine2-inputEl')]")
    public WebElement editbox_AddressStandardizationAddressLine2;

    @FindBy(xpath = "//*[contains(@id,'AddressStandardizationPopup:LocationScreen:City-inputEl')]")
    public WebElement editbox_AddressStandardizationCity;

    @FindBy(xpath = "//*[contains(@id,'AddressStandardizationPopup:LocationScreen:PostalCode-inputEl')]")
    public WebElement editbox_AddressStandardizationZip;

    public Guidewire8Select select_AddressStandardizationState() {
        return new Guidewire8Select(driver, "//table[@id='AddressStandardizationPopup:LocationScreen:State-triggerWrap']");
    }

    // ***************************************************************************************************
    // Items Below are the Helper methods for the AddressStandardization Page
    // Repository Items.
    // ***************************************************************************************************


    public void selectAddress(String partialAddressLine1) {
        List<WebElement> selects = link_AddressStandardizationSelect(partialAddressLine1);
        selects.get(0).click();
    }


    public String getAddressStandardizationPageTitle() {
        waitUntilElementIsVisible(text_AddressStandardizationTitle);
        String text = text_AddressStandardizationTitle.getText();
        return text;
    }


    public void clickAddressStandardizationReturnTo() {
        waitUntilElementIsClickable(link_AddressStandardizationReturnTo);
        link_AddressStandardizationReturnTo.click();
    }


    public void clickAddressStandardizationStandardizeLink() {
        waitUntilElementIsClickable(button_AddressStandardizationStandardize);
        button_AddressStandardizationStandardize.click();
    }


    public void clickAddressStandardizationOverrideLink() {
        waitUntilElementIsClickable(button_AddressStandardizationOverride);
        button_AddressStandardizationOverride.click();
    }


    public boolean checkAddressStandardizationOverrideLinkExists() {
        boolean overrideExists = checkIfElementExists(
                "//*[contains(@id,'AddressStandardizationPopup:LocationScreen:Update-btnEl')]", 3);
        if (overrideExists) {
            waitUntilElementIsClickable(button_AddressStandardizationOverride);
            button_AddressStandardizationOverride.click();
        }
        return overrideExists;
    }


    public void setAddressStandardizationAddressLine1(String addressLine1) {
        waitUntilElementIsClickable(editbox_AddressStandardizationAddressLine1);
        editbox_AddressStandardizationAddressLine1.sendKeys(addressLine1);
    }


    public void setAddressStandardizationAddressLine2(String addressLine2) {
        waitUntilElementIsClickable(editbox_AddressStandardizationAddressLine2);
        editbox_AddressStandardizationAddressLine2.sendKeys(addressLine2);
    }


    public void setAddressStandardizationCity(String city) {
        waitUntilElementIsClickable(editbox_AddressStandardizationCity);
        editbox_AddressStandardizationCity.sendKeys(city);
    }


    public void setAddressStandardizationZip(String zip) {
        waitUntilElementIsClickable(editbox_AddressStandardizationZip);
        editbox_AddressStandardizationZip.sendKeys(zip);
    }


    public void selectSpecific_State(State stateToSelect) {
        select_AddressStandardizationState().selectByVisibleText(stateToSelect.getName());
    }

    /*
     * Steve Broderick 9/8/2015 When you add an address to a policy, you may be
     * taken to the location Information page if the address does not
     * standardize. therefore, I put this method in Common.
     */

    public boolean handleAddressStandardization(String validAddressLine1) {
        if (checkIfElementExists(text_AddressStandardizationTitle, 3000)) {

            if (checkIfElementExists(find(By.xpath("//a[contains(@id, ':Update')]")), 2000)) {
                clickOverride();
                
            } else {
                selectAddress(validAddressLine1);
                
            }
            return true;
        } else {
            return false;
        }

    }


    public boolean handleAddressStandardization(AddressInfo validAddress) {
        if (checkIfElementExists(text_AddressStandardizationTitle, 3000)) {
            // String title = getAddressStandardizationPageTitle();
            // boolean exists =
            // checkIfElementExists(button_GenericWorkorderOverride, 2000);
            if (checkIfElementExists(find(By.xpath("//a[contains(@id, ':Update')]")), 2000)) {
                clickOverride();
                
            } else {
                selectAddress(validAddress.getLine1());
                
            }
            return true;
        } else {
            return false;
        }

    }


    public boolean isStandardAddress() {
        return finds(By.xpath("//span[contains(@id, 'AddressStandardizationPopup:LocationScreen:ttlBar')]"))
                .size() <= 0;
    }

}

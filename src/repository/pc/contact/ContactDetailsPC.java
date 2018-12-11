package repository.pc.contact;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class ContactDetailsPC extends BasePage {

    public ContactDetailsPC(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//span[contains(@id, ':AddressesCardTab-btnEl')]")
    private WebElement link_addressTab;

    @FindBy(xpath = "//span[contains(@id, ':AccountContactDetailsCardTab-btnEl')]")
    private WebElement link_detailTab;
    
    @FindBy(xpath = "//a[contains(@id, ':LinkAddressMenuMenuIcon')]/img")
    private WebElement link_DesignatedAddressDropdown;

    @FindBy(xpath = "//div[contains(@id, ':BusinessPhone:GlobalPhoneInputSet:PhoneDisplay-inputEl')]")
    private WebElement textBox_BusinessPhone;

    @FindBy(xpath = "//div[contains(@id, ':WorkPhone:GlobalPhoneInputSet:PhoneDisplay-inputEl')]")
    private WebElement textBox_WorkPhone;

    @FindBy(xpath = "//div[contains(@id, ':HomePhone:GlobalPhoneInputSet:PhoneDisplay-inputEl')]")
    private WebElement textBox_HomePhone;

    @FindBy(xpath = "//div[contains(@id, ':MobilePhone:GlobalPhoneInputSet:PhoneDisplay-inputEl')]")
    private WebElement textBox_MobilePhone;

    @FindBy(xpath = "//div[contains(@id, ':FaxPhone:GlobalPhoneInputSet:PhoneDisplay-inputEl')]")
    private WebElement textBox_FaxPhone;


    public void clickAddress() {
        link_addressTab.click();
    }


    public void clickDetails() {
        link_detailTab.click();
    }


    public String getBusinessPhone() {
        return textBox_BusinessPhone.getText();
    }


    public String getWorkPhone() {
        return textBox_WorkPhone.getText();
    }


    public String getHomePhone() {
        return textBox_HomePhone.getText();
    }


    public String getMobilePhone() {
        return textBox_MobilePhone.getText();
    }


    public String getFaxPhone() {
        return textBox_FaxPhone.getText();
    }
    
    public boolean designatedAddressDropdownExists() {
    	if(checkIfElementExists(link_DesignatedAddressDropdown, 1)) {
    		return true;
    	} else {
    		return false;
    	}
    }

}

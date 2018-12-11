package repository.pc.workorders.generic;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.idfbins.enums.State;

import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.AddressType;
import repository.gw.enums.DeliveryOptionType;
import repository.gw.enums.PhoneType;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.helpers.DateUtils;

public class GenericWorkorderPolicyInfoContact extends GenericWorkorder {
	
	private WebDriver driver;

    public GenericWorkorderPolicyInfoContact(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//div//a[contains(text(), 'Attention')]")
    private WebElement deliveryOptionLink;

    @FindBy(xpath = "//span[contains(@id, ':FBM_PolicyContactDetailsDV:Add-btnEl') or contains(@id, ':FBM_PolicyContactDetailsDV:DeliveryOptionsInputSet:DeliveryOptionListInputSet:ToolbarAddButton-btnEl')]")
    private WebElement addDeliveryOptions;

    @FindBy(xpath = "//*[contains(@id, 'EditPolicyContactRolePopup:ContactDetailScreen:FinishPCR-btnEl')]")
    private WebElement button_GenericWorkorderPolicyInfoContactUpdate;

    @FindBy(xpath = "//span[contains(@id,'EditPolicyContactRolePopup:ContactDetailScreen:Update-btnInnerEl')]")
    public WebElement button_EditPolicyContactOK;

    public Guidewire8Select select_GenericWorkOrderPolicyInfoContactAddressListing() {
        return new Guidewire8Select(driver, "//table[@id='EditPolicyContactRolePopup:ContactDetailScreen:PolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:AddressListing-triggerWrap']");
    }

    @FindBy(xpath = "//input[contains(@id,'EditPolicyContactRolePopup:ContactDetailScreen:PolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:CountryInputSet:globalAddressContainer:GlobalAddressInputSet:AddressLine1-inputEl')]")
    public WebElement editbox_GenericWorkOrderPolicyInfoContactAddressLine1;

    @FindBy(xpath = "//input[contains(@id,'EditPolicyContactRolePopup:ContactDetailScreen:PolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:CountryInputSet:globalAddressContainer:GlobalAddressInputSet:AddressLine2-inputEl')]")
    public WebElement editbox_GenericWorkOrderPolicyInfoContactAddressLine2;

    @FindBy(xpath = "//input[contains(@id,'EditPolicyContactRolePopup:ContactDetailScreen:PolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:CountryInputSet:globalAddressContainer:GlobalAddressInputSet:City-inputEl')]")
    public WebElement editbox_GenericWorkOrderPolicyInfoContactCity;

    @FindBy(xpath = "//input[contains(@id,'EditPolicyContactRolePopup:ContactDetailScreen:PolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:CountryInputSet:globalAddressContainer:GlobalAddressInputSet:County-inputEl')]")
    public WebElement editbox_GenericWorkOrderPolicyInfoContactCounty;

    @FindBy(xpath = "//input[contains(@id,'EditPolicyContactRolePopup:ContactDetailScreen:PolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:CountryInputSet:globalAddressContainer:GlobalAddressInputSet:PostalCode-inputEl')]")
    public WebElement editbox_GenericWorkOrderPolicyInfoContactZip;

    @FindBy(xpath = "//input[contains(@id, ':PolicyContactRoleNameAdditionalInputsInputSet:PCRIdentificationInputSet:SSN-inputEl')]")
    public WebElement editbox_GenericWorkOrderPolicyInfoContactSSN;

    @FindBy(xpath = "//input[contains(@id, ':PolicyContactRoleNameAdditionalInputsInputSet:PCRIdentificationInputSet:InputWithHelpTextInputSet:Input-inputEl')]")
    public WebElement editbox_GenericWorkOrderPolicyInfoContactAltID;

    public Guidewire8Select select_GenericWorkOrderPolicyInfoContactState() {
        return new Guidewire8Select(driver, "//table[@id='EditPolicyContactRolePopup:ContactDetailScreen:PolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:CountryInputSet:globalAddressContainer:GlobalAddressInputSet:State-triggerWrap']");
    }
    
    public Guidewire8Select select_GenericWorkOrderPolicyInfoContactAddressType() {
        return new Guidewire8Select(driver, "//table[@id='EditPolicyContactRolePopup:ContactDetailScreen:PolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:CountryInputSet:AddressType-triggerWrap']");
    }

    @FindBy(xpath = "//*[contains(@id,'EditPolicyContactRolePopup:ContactDetailScreen:PolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:ContactChangeReason_FBM-inputEl')]")
    public WebElement textbox_GenericWorkOrderPolicyInfoContactReasonContactChange;

    @FindBy(xpath = "//a[contains(@id,'EditPolicyContactRolePopup:ContactDetailScreen:PolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:LinkedAddressFBMInputSet:LinkAddressMenu:LinkAddressMenuMenuIcon')]")
    public WebElement link_DesignatedAddress;

    @FindBy(xpath = "//span[contains(@id, 'EditPolicyContactRolePopup:ContactDetailScreen:PolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:LinkedAddressFBMInputSet:LinkAddressMenu:')  and (text()='New Location') ]")
    public WebElement click_new_DesignatedAddress;
    
    private Guidewire8Select select_AddressType() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':AddressType-triggerWrap')]");
	}

    //*************************************************************************************************************************************************************************************************************************************************
    //               Helper Methods
    //************************************************************************************************************************************************************************************************************************************************************


    public void setPolicyInfoContactAddressListingSelectList(
            String addressToSelect) {
        select_GenericWorkOrderPolicyInfoContactAddressListing().selectByVisibleText(addressToSelect);
    }

    private void setAddressType(AddressType type) {
		Guidewire8Select mySelect = select_AddressType();
		mySelect.selectByVisibleText(type.getValue());
	}

    public void setPolicyInfoContactState(String stateToSelect) {
        select_GenericWorkOrderPolicyInfoContactAddressListing().selectByVisibleText(stateToSelect);
    }

    public void setPolicyInfoContactAddressLine1(AddressInfo newAddress) {
        clickWhenClickable(editbox_GenericWorkOrderPolicyInfoContactAddressLine1);
        editbox_GenericWorkOrderPolicyInfoContactAddressLine1.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_GenericWorkOrderPolicyInfoContactAddressLine1.sendKeys(newAddress.getLine1());
    }

    public void setPolicyInfoContactChangeReason(String reason) {
        waitUntilElementIsVisible(textbox_GenericWorkOrderPolicyInfoContactReasonContactChange);
        textbox_GenericWorkOrderPolicyInfoContactReasonContactChange.click();
        textbox_GenericWorkOrderPolicyInfoContactReasonContactChange.sendKeys(reason);
    }

    public void setPolicyChangePolicyInfoContactAddressType(String addressType) {
        
        select_GenericWorkOrderPolicyInfoContactAddressType().selectByVisibleText(addressType);
    }

    public void setPolicyInfoContactZip(AddressInfo newAddress) {
        clickWhenClickable(editbox_GenericWorkOrderPolicyInfoContactZip);
        editbox_GenericWorkOrderPolicyInfoContactZip.sendKeys(Keys.CONTROL + "a");
        editbox_GenericWorkOrderPolicyInfoContactZip.sendKeys(newAddress.getZip());
        clickProductLogo();
    }

    public void setPolicyInfoContactCity(AddressInfo newAddress) {
        
        waitUntilElementIsVisible(editbox_GenericWorkOrderPolicyInfoContactCity);
        
        editbox_GenericWorkOrderPolicyInfoContactCity.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        
        editbox_GenericWorkOrderPolicyInfoContactCity.sendKeys(newAddress.getCity());
        
        clickProductLogo();
    }

    public void setPolicyInfoContactAddressLine2(AddressInfo newAddress) {
        clickWhenClickable(editbox_GenericWorkOrderPolicyInfoContactAddressLine2);
        editbox_GenericWorkOrderPolicyInfoContactAddressLine2.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_GenericWorkOrderPolicyInfoContactAddressLine2.sendKeys(Keys.DELETE);
        editbox_GenericWorkOrderPolicyInfoContactAddressLine2.sendKeys(newAddress.getLine2());
        clickProductLogo();
    }

    public void clickPolicyInfoContactOk() {
        clickWhenClickable(button_EditPolicyContactOK);
    }

    public void setPolicyInfoContactCounty(AddressInfo newAddress) {
        clickWhenClickable(editbox_GenericWorkOrderPolicyInfoContactCounty);
        editbox_GenericWorkOrderPolicyInfoContactCounty.sendKeys(newAddress.getCounty());
    }

    public void clickAddDeliveryOptions() {
        clickWhenClickable(addDeliveryOptions);
    }

    public void clickOK() {
        if (checkIfElementExists("//span[contains(text(), 'OK')]/parent::span", 1000)) {
            clickOK();
        } else {
            clickWhenClickable(button_GenericWorkorderPolicyInfoContactUpdate);
        }
    }

    public void clickUpdate() {
        button_GenericWorkorderPolicyInfoContactUpdate.click();
    }

    public boolean isOptionClickable(DeliveryOptionType optionType) {
        boolean isFound = false;
        try {
            find(By.xpath("//div//a[contains(text(), '" + optionType.getTypeValue() + "')]"));
            isFound = true;
        } catch (Exception e) {
        }
        return isFound;
    }


    public void setPolicyChangePolicyInfoContactAddressListingSelectList(String addressToSelect) {
        // TODO Auto-generated method stub

    }


    public void setNewAddress(boolean isNewAddress, AddressInfo newAddress) {
        if (isNewAddress) {
            setPolicyInfoContactAddressListingSelectList("New...");
        } else {
            setPolicyInfoContactAddressListingSelectList("(" + newAddress.getState() + ") " + newAddress.getCity() + " - " + newAddress.getLine1());
        }
        select_GenericWorkOrderPolicyInfoContactState().selectByVisibleTextPartial("Ida");
        setPolicyInfoContactAddressLine1(newAddress);
        setAddressType(AddressType.Work);
        setPolicyInfoContactCity(newAddress);
        setPolicyInfoContactZip(newAddress);
        setPolicyInfoContactChangeReason("New work address");
    }

    public Contact gatherPolicyMemberDataInfoToValidate() {

        Contact gatheredPerson = new Contact();
        gatheredPerson.removeDefaultValues();

        By firstName = By.cssSelector("input[id$='FirstName-inputEl']"); // First Name input
        By lastName = By.cssSelector("input[id$='LastName-inputEl']"); // Last Name input
        By ssn = By.cssSelector("input[id$='SSN-inputEl']"); // SSN input
        By dob = By.cssSelector("input[id$='DateOfBirth-inputEl']"); // Date of Birth

        By addressLine1 = By.cssSelector("div[id*='AddressLine1']");
        By city = By.cssSelector("div[id*='City-inputEl']");
        By state = By.cssSelector("div[id$='State-inputEl']");
        By zip = By.cssSelector("div[id*='PostalCode-inputEl']");
        By homePhone = By.cssSelector("div[id*='HomePhone']");
        By mobilePhone = By.cssSelector("div[id*='MobilePhone']");
        By primaryPhone = By.cssSelector("div[id*='primaryPhone-inputEl']");
        By email = By.cssSelector("input[id$='EmailAddress1-inputEl']");


        gatheredPerson.setFirstName(find(firstName).getAttribute("value"));
        gatheredPerson.setLastName(find(lastName).getAttribute("value"));
        gatheredPerson.setSocialSecurityNumber(find(ssn).getAttribute("value"));
        try {
            gatheredPerson.setDob(driver, DateUtils.convertStringtoDate(find(dob).getAttribute("value"), "MM/dd/yyyy"));
        } catch (Exception e) {

        }
        AddressInfo aInfo = new AddressInfo();
        aInfo.removeDefaultValues();

        aInfo.setLine1(find(addressLine1).getText());
        aInfo.setCity(find(city).getText());
        aInfo.setState(State.valueOf(find(state).getText()));
        aInfo.setZip(find(zip).getText());
        aInfo.setPhoneHome(find(homePhone).getText());
        aInfo.setPhoneMobile(find(mobilePhone).getText());
        aInfo.setPhonePrimary(PhoneType.valueOf(find(primaryPhone).getText()));
        aInfo.setEmailAddress(find(email).getAttribute("value"));

        gatheredPerson.setAddress(aInfo);
        return gatheredPerson;
    }

    public void clearAltID() {
        editbox_GenericWorkOrderPolicyInfoContactAltID.clear();
        editbox_GenericWorkOrderPolicyInfoContactAltID.sendKeys(Keys.TAB);
    }

    public void setSSN(String ssn) {
        
        clickWhenClickable(editbox_GenericWorkOrderPolicyInfoContactSSN);
        editbox_GenericWorkOrderPolicyInfoContactSSN.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_GenericWorkOrderPolicyInfoContactSSN.sendKeys(String.valueOf(ssn));
        
    }

    public void setDesignatedNewAddress(AddressInfo newAddress) {
        
        clickWhenClickable(link_DesignatedAddress );
        
        clickWhenClickable(click_new_DesignatedAddress);
        select_GenericWorkOrderPolicyInfoContactState().selectByVisibleTextPartial("Ida");
        setPolicyInfoContactAddressLine1(newAddress);
        setAddressType(AddressType.Mailing);
        setPolicyInfoContactCity(newAddress);
        setPolicyInfoContactZip(newAddress);
        setPolicyInfoContactChangeReason("New  Mailing Address");
    }
}

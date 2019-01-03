package repository.pc.workorders.generic;

import com.idfbins.enums.State;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.AdditionalInsuredRole;
import repository.gw.enums.AddressType;
import repository.gw.enums.PhoneType;
import repository.pc.contact.ContactEditPC;

import java.util.ArrayList;
import java.util.List;

public class GenericWorkorderAdditionalInsured extends ContactEditPC {


    private WebDriver driver;

    public GenericWorkorderAdditionalInsured(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//span[contains(@id, 'PolicyAddnlInsuredContactRoleDetailsCV:PolicyContactRoleDetailCardTab-btnEl') or contains(@id, 'NewPolicyContactRoleDetailsCV:PolicyContactRoleDetailCardTab-btnEl')]")
    private WebElement link_ContactDetailTab;

    @FindBy(xpath = "//span[contains(@id, 'ContactDetailScreen:PolicyAddnlInsuredContactRoleDetailsCV:AddressesCardTab-btnEl')]")
    public WebElement link_Addresses;

    @FindBy(xpath = "//span[contains(@id, ':LocationDetailCV:AdditionalInsuredLV_tb:ToolbarButton-btnEl')]")
    public WebElement button_Search;

    @FindBy(xpath = ("//textarea[contains(@id, ':WhatActivities-inputEl')]"))
    public WebElement editbox_DescriptionOfActivities;

    public Guidewire8RadioButton radio_SpecialWording() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, ':SpecialWording')]");
    }

    @FindBy(xpath = ("//textarea[contains(@id, ':ContactDetailScreen:SpecialWordDescr-inputEl')]"))
    public WebElement editbox_SpecialWordingDesc;

    @FindBy(xpath = ("//textarea[contains(@id, ':ContactDetailScreen:Acord101Descr-inputEl')]"))
    public WebElement editbox_Acord101Desc;

    @FindBy(xpath = "//a[contains(@id, ':ContactDetailScreen:CheckForDuplicates')]")
    private WebElement checkForDuplicates;

    @FindBy(xpath = "//a[contains(@id, ':ContactRelatedContactsCardTab')]")
    private WebElement relatedContacts;

    public Guidewire8RadioButton radio_WavierSubroBP_04() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, ':WaiverofSubro')]");
    }

    @FindBy(xpath = "//textarea[contains(@id, ':EquipmentDescription-inputEl')]")
    public WebElement editbox_DescOfLeasedEquipment;

    @FindBy(xpath = "//input[contains (@id, ':DateOfBirth-inputEl')]")
    public WebElement editbox_DOB;

    @FindBy(xpath = "//input[contains (@id, ':SSN-inputEl')]")
    public WebElement editbox_SSN;

    public Guidewire8RadioButton radio_OilAndGas() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, ':IsInOilandGasIndustry')]");
    }

    public Guidewire8RadioButton radio_UndergroundTanks() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, ':IsInUndergroundTanksIndustry')]");
    }

    public Guidewire8RadioButton radio_AircraftAirport() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, ':IsInAircraftandAirportIndustry')]");
    }

    public Guidewire8RadioButton radio_BridgeConstruction() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, ':IsInBridgeConstructionIndustr')]");
    }

    public Guidewire8RadioButton radio_Firearms() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, ':IsInFirearmsIndustry')]");
    }

    public Guidewire8RadioButton radio_Railroads() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, ':IsRailroadsIndustry')]");
    }

    public Guidewire8RadioButton radio_DamsAndReservoirs() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, ':IsInDamsandReservoirsIndustry')]");
    }

    public Guidewire8RadioButton radio_Mining() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, ':IsInMiningIndustry')]");
    }

    public Guidewire8Select select_Roles() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':insuredType-triggerWrap')]");
    }

    public Guidewire8Select select_AddressListing() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':PolicyAddnlInsuredContactRoleDetailsCV:FBM_PolicyContactDetailsDV:AddressListing-triggerWrap')]");
    }

    public Guidewire8Select select_Country() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':Country-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id, ':BusinessPhone:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl')]")
    public WebElement editbox_BusinessPhone;

    @FindBy(xpath = "//input[contains(@id, ':WorkPhone:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl')]")
    public WebElement editbox_WorkPhone;

    @FindBy(xpath = "//input[contains(@id, ':HomePhone:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl')]")
    public WebElement editbox_HomePhone;

    @FindBy(xpath = "//input[contains(@id, ':MobilePhone:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl')]")
    public WebElement editbox_MobilePhone;

    @FindBy(xpath = "//input[contains(@id, ':FaxPhone:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl')]")
    public WebElement editbox_Fax;

    public Guidewire8Select select_PrimaryPhone() {
        return new Guidewire8Select(driver, "table[contains(@id, ':FBM_PolicyContactDetailsDV:CountryInputSet:primaryPhone-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id, ':PolicyContactRoleAdditionalInputSet:FBAccountInfoInputSet:Website-inputEl')]")
    public WebElement editbox_Website;
    
      

    //////////////////////
    // HELPER METHODS/////
    //////////////////////


    public void clickOK() {
        super.clickOK();
    }


    public void clickUpdate() {
        super.clickUpdate();
    }


    public void clickCancel() {
        super.clickCancel();
    }


    public void clickSearch() {
        clickWhenClickable(button_Search);
    }


    public void setStatesInsuredPerformsActivities(ArrayList<State> stateList) {
        for (State state : stateList) {
            int timesToTry = 0;
            if (finds(By.xpath("//label[contains(text(), '" + state.getName() + "')]/preceding-sibling::input")).size() > 0) {
                do {
                    Guidewire8Checkbox myCheckbox = new Guidewire8Checkbox(driver, "//label[contains(text(), '" + state.getName() + "')]/parent::div/parent::td/parent::tr/parent::tbody/parent::table");
                    myCheckbox.select(true);
                    timesToTry++;
                }
                while (!new Guidewire8Checkbox(driver, "//label[contains(text(), '" + state.getName() + "')]/parent::div/parent::td/parent::tr/parent::tbody/parent::table").isSelected() && timesToTry < 2);

            }
        }
    }


    public void setDescribeActivities(String desc) {
        setText(editbox_DescriptionOfActivities, desc);
    }


    public void setSpecialWordingRadio(Boolean yesno) {
        radio_SpecialWording().select(yesno);
    }


    public void setSpecialWording(String desc) {
        editbox_SpecialWordingDesc.click();
        editbox_SpecialWordingDesc.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_SpecialWordingDesc.sendKeys(desc);
    }


    public void setAcord101Desc(String desc) {
        editbox_Acord101Desc.click();
        editbox_Acord101Desc.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_Acord101Desc.sendKeys(desc);
    }


    public void setWaiverOfSubroBP04(Boolean yesno) {
        radio_WavierSubroBP_04().select(yesno);
    }

    public void setLeasedEquipmentDescription(String desc) {
        editbox_DescOfLeasedEquipment.click();
        editbox_DescOfLeasedEquipment.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_DescOfLeasedEquipment.sendKeys(desc);
    }


    public void setOilGasRadio(Boolean yesno) {
        radio_OilAndGas().select(yesno);
    }


    public void setUndergroundTanksRadio(Boolean yesno) {
        radio_UndergroundTanks().select(yesno);
    }


    public void setAircraftAirportRadio(Boolean yesno) {
        radio_AircraftAirport().select(yesno);
    }


    public void setBridgeConstructionRadio(Boolean yesno) {
        radio_BridgeConstruction().select(yesno);
    }


    public void setFirearmsRadio(Boolean yesno) {
        radio_Firearms().select(yesno);
    }


    public void setRailroadsRadio(Boolean yesno) {
        radio_Railroads().select(yesno);
    }


    public void setDamsAndReservoirsRadio(Boolean yesno) {
        radio_DamsAndReservoirs().select(yesno);
    }


    public void setMiningRadio(Boolean yesno) {
        radio_Mining().select(yesno);
    }


    public void clickContactDetailsTab() {
        link_ContactDetailTab.click();
    }


    public void clickAddressesTab() {
        link_Addresses.click();
    }


    public void selectRoles(AdditionalInsuredRole role) {
        Guidewire8Select mySelect = select_Roles();
        mySelect.selectByVisibleText(role.getRole());
    }


    public String getAIRole() {
        return select_Roles().getText();
    }


    public void setSSN(String ssn) {
    	setText(editbox_SSN, ssn);
    }


    public void setTIN(String tin) {
    }


    public void setMainEmail(String mailEmail) {

    }


    public void setAltEmail(String altEmail) {

    }


    public void selectAddressListing(String address) {
        if (address.contains("New")) {
            List<WebElement> newButton = finds(By.xpath("//a[contains(@id, ':SelectedAddress:SelectedAddressMenuIcon') or contains(@id, ':AddressListingForNewContactsMenuIcon') or contains(@id, ':AddressListingMenuIcon')]"));
            if (!newButton.isEmpty()) {
                clickWhenClickable(newButton.get(0));
                clickWhenClickable(find(By.xpath("//div[contains(@id, ':newAddressBuddy')]")));
                return;
            }
        }
        Guidewire8Select addressListing = super.select_ContactEditAddressListing();
        if (addressListing.isItemInList(address)) {
            addressListing.selectByVisibleTextPartial(address);
        } else {
            addressListing.selectByVisibleTextRandom();
        }
    }


    public void selectCounrty(String country) {

    }


    public void setAddressLine1(String line1) {
        super.setContactEditAddressLine1(line1);
    }

    public void setZipCode(String zip) {
        super.setContactEditAddressZipCode(zip);
    }

    public void selectAddressType(AddressType type) {
        super.select_ContactEditAddressAddressType().selectByVisibleText(type.getValue());
    }


    public void setBusinessPhone(String phone) {
        editbox_BusinessPhone.click();
        editbox_BusinessPhone.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_BusinessPhone.sendKeys(phone);
    }


    public void setWorkPhone(String phone) {
        editbox_WorkPhone.click();
        editbox_WorkPhone.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_WorkPhone.sendKeys(phone);
    }


    public void setHomePhone(String phone) {
        editbox_HomePhone.click();
        editbox_HomePhone.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_HomePhone.sendKeys(phone);
    }


    public void setMobilePhone(String phone) {
        editbox_MobilePhone.click();
        editbox_MobilePhone.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_MobilePhone.sendKeys(phone);
    }


    public void setFax(String phone) {
        editbox_Fax.click();
        editbox_Fax.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_Fax.sendKeys(phone);
    }


    public void selectPrimaryPhone(PhoneType type) {
        select_PrimaryPhone().selectByVisibleText(type.getValue());
    }


    public void setWebsite(String website) {
        editbox_Website.click();
        editbox_Website.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_Website.sendKeys(website);
    }


    public void clickCheckForDuplicatesButton() {
        clickWhenClickable(checkForDuplicates);

    }


    public void checkForDuplicateContactSelectButton() {
        // TODO Auto-generated method stub

    }


    public void setDOB(String dob) {
    	setText(editbox_DOB, dob);

    }


    public void clickRelatedContactsTab() {
        clickWhenClickable(relatedContacts);
    }

    private Guidewire8Select select_State() {
    	return new Guidewire8Select(driver, "Set:State-triggerWrap");
    }
    
	public void setState(State state) {
		Guidewire8Select mySelect = select_State();
		mySelect.selectByVisibleText(state.name());
		
	}
}

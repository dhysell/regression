package repository.pc.workorders.generic;

import com.idfbins.enums.State;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.AdditionalInsuredRole;
import repository.gw.enums.AddressType;
import repository.pc.contact.ContactEditPC;

import java.util.List;

public class GenericWorkorderBusinessownersLineAdditionalInsured extends ContactEditPC {

    private WebDriver driver;

    public GenericWorkorderBusinessownersLineAdditionalInsured(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------
//	@FindBy(xpath = "//select[contains(@id, 'FBM_PolicyContactDetailsDV:PolicyContactRoleNameInputSet:PolicyContactRoleInputSet:0:AdditionalInsuredType')]")
//	public WebElement select_SubmissionEditAdditionalInsuredBOLineRole;

    public Guidewire8Select select_SubmissionEditAdditionalInsuredBOLineRole() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':InterestType-triggerWrap')]");
    }

    public WebElement checkbox_SubmissionEditAdditionalInsuredBOLineWherePerformActivities(State state) {
        return find(By.xpath("//label[contains(text(), '" + state.getName() + "')]/preceding-sibling::input[contains(@role, 'checkbox')]"));
    }

    @FindBy(xpath = "//textarea[contains(@id, ':WhatActivities-inputEl')]")
    public WebElement editbox_SubmissionEditAdditionalInsuredBOLineWhatActivities;

//	@FindBy(xpath = "//input[contains(@id, 'ContactDetailScreen:SpecialWording_true')]")
//	public WebElement radio_SubmissionEditAdditionalInsuredBOLineSpecialWordingYes;
//	
//	@FindBy(xpath = "//input[contains(@id, 'ContactDetailScreen:SpecialWording_false')]")
//	public WebElement radio_SubmissionEditAdditionalInsuredBOLineSpecialWordingNo;

    @FindBy(xpath = "//textarea[contains(@id, ':SpecialWordDescr-inputEl')]")
    public WebElement editbox_SubmissionEditAdditionalInsuredBOLineSpecialWordingDescription;

    @FindBy(xpath = "//textarea[contains(@id, ':Accord101Descr-inputEl')]")
    public WebElement editbox_SubmissionEditAdditionalInsuredBOLineSpecialWordingAcord101Description;

//	@FindBy(xpath = "//input[contains(@id, 'ContactDetailScreen:IsInOilandGasIndustry_true')]")
//	public WebElement radio_SubmissionEditAdditionalInsuredBOLineIndustryOilGasYes;
//	
//	@FindBy(xpath = "//input[contains(@id, 'ContactDetailScreen:IsInOilandGasIndustry_false')]")
//	public WebElement radio_SubmissionEditAdditionalInsuredBOLineIndustryOilGasNo;
//	
//	@FindBy(xpath = "//input[contains(@id, 'ContactDetailScreen:IsInUndergroundTanksIndustry_true')]")
//	public WebElement radio_SubmissionEditAdditionalInsuredBOLineIndustryUndergroundTanksYes;
//	
//	@FindBy(xpath = "//input[contains(@id, 'ContactDetailScreen:IsInUndergroundTanksIndustry_false')]")
//	public WebElement radio_SubmissionEditAdditionalInsuredBOLineIndustryUndergroundTanksNo;
//	
//	@FindBy(xpath = "//input[contains(@id, 'ContactDetailScreen:IsInAircraftandAirportIndustry_true')]")
//	public WebElement radio_SubmissionEditAdditionalInsuredBOLineIndustryAircraftAirportYes;
//	
//	@FindBy(xpath = "//input[contains(@id, 'ContactDetailScreen:IsInAircraftandAirportIndustry_false')]")
//	public WebElement radio_SubmissionEditAdditionalInsuredBOLineIndustryAircraftAirportNo;
//	
//	@FindBy(xpath = "//input[contains(@id, 'ContactDetailScreen:IsInBridgeConstructionIndustr_true')]")
//	public WebElement radio_SubmissionEditAdditionalInsuredBOLineIndustryBridgeConstYes;
//	
//	@FindBy(xpath = "//input[contains(@id, 'ContactDetailScreen:IsInBridgeConstructionIndustr_false')]")
//	public WebElement radio_SubmissionEditAdditionalInsuredBOLineIndustryBridgeConstNo;
//	
//	@FindBy(xpath = "//input[contains(@id, 'ContactDetailScreen:IsInDamsandReservoirsIndustry_true')]")
//	public WebElement radio_SubmissionEditAdditionalInsuredBOLineIndustryDamsResoYes;
//	
//	@FindBy(xpath = "//input[contains(@id, 'ContactDetailScreen:IsInDamsandReservoirsIndustry_false')]")
//	public WebElement radio_SubmissionEditAdditionalInsuredBOLineIndustryDamsResoNo;
//	
//	@FindBy(xpath = "//input[contains(@id, 'ContactDetailScreen:IsInFirearmsIndustry_true')]")
//	public WebElement radio_SubmissionEditAdditionalInsuredBOLineIndustryFirearmsYes;
//	
//	@FindBy(xpath = "//input[contains(@id, 'ContactDetailScreen:IsInFirearmsIndustry_false')]")
//	public WebElement radio_SubmissionEditAdditionalInsuredBOLineIndustryFirearmsNo;
//	
//	@FindBy(xpath = "//input[contains(@id, 'ContactDetailScreen:IsRailroadsIndustry_true')]")
//	public WebElement radio_SubmissionEditAdditionalInsuredBOLineIndustryRailroadsYes;
//	
//	@FindBy(xpath = "//input[contains(@id, 'ContactDetailScreen:IsRailroadsIndustry_false')]")
//	public WebElement radio_SubmissionEditAdditionalInsuredBOLineIndustryRailroadsNo;
//	
//	@FindBy(xpath = "//input[contains(@id, 'ContactDetailScreen:IsInMiningIndustry_true')]")
//	public WebElement radio_SubmissionEditAdditionalInsuredBOLineIndustryMiningYes;
//	
//	@FindBy(xpath = "//input[contains(@id, 'ContactDetailScreen:IsInMiningIndustry_false')]")
//	public WebElement radio_SubmissionEditAdditionalInsuredBOLineIndustryMiningNo;

//	@FindBy(xpath = "//input[contains(@id, ':WaiverofSubro_true')]")
//	public WebElement radio_SubmissionEditAdditionalInsuredBOLineWaiverSubroYes;
//	
//	@FindBy(xpath = "//input[contains(@id, ':WaiverofSubro_false')]")
//	public WebElement radio_SubmissionEditAdditionalInsuredBOLineWaiverSubroNo;

    @FindBy(xpath = "//textarea[contains(@id, 'ListProducts-inputEl')]")
    public WebElement editbox_SubmissionEditAdditionalInsuredBOLineListProducts;

    @FindBy(xpath = "//input[contains(@id, 'ListProducts-inputEl')]")
    public WebElement editbox_SubmissionEditAdditionalInsuredBOLineListProductsAlt;

    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------

    public void setEditAdditionalInsuredBOLineWherePerformActivities(State state, boolean trueFalseChecked) {
        
        waitUntilElementIsClickable(checkbox_SubmissionEditAdditionalInsuredBOLineWherePerformActivities(state));
        if (trueFalseChecked) {
            if (!checkbox_SubmissionEditAdditionalInsuredBOLineWherePerformActivities(state).isSelected()) {
            	clickWhenClickable(checkbox_SubmissionEditAdditionalInsuredBOLineWherePerformActivities(state));
            }
        } else {
            if (checkbox_SubmissionEditAdditionalInsuredBOLineWherePerformActivities(state).isSelected()) {
            	clickWhenClickable(checkbox_SubmissionEditAdditionalInsuredBOLineWherePerformActivities(state));
            }
        }
        
    }


    public void setEditAdditionalInsuredBOLineWhatActivities(String activities) {
        waitUntilElementIsClickable(editbox_SubmissionEditAdditionalInsuredBOLineWhatActivities);
        editbox_SubmissionEditAdditionalInsuredBOLineWhatActivities.sendKeys(activities);
    }


    public void setEditAdditionalInsuredBOLineSpecialWording(boolean radioValue) {
        Guidewire8RadioButton radio = new Guidewire8RadioButton(driver, "//tr[contains(@id, ':SpecialWording-inputRow')]");
        radio.select(radioValue);
    }


    public void setEditAdditionalInsuredBOLineSpecialWordingDescription(String description) {
        waitUntilElementIsClickable(editbox_SubmissionEditAdditionalInsuredBOLineSpecialWordingDescription);
        editbox_SubmissionEditAdditionalInsuredBOLineSpecialWordingDescription.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_SubmissionEditAdditionalInsuredBOLineSpecialWordingDescription.sendKeys(description);
        
    }


    public void setEditAdditionalInsuredBOLineSpecialWordingAcord101Description(String description) {
        waitUntilElementIsClickable(editbox_SubmissionEditAdditionalInsuredBOLineSpecialWordingAcord101Description);
        editbox_SubmissionEditAdditionalInsuredBOLineSpecialWordingAcord101Description.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_SubmissionEditAdditionalInsuredBOLineSpecialWordingAcord101Description.sendKeys(description);
        
    }


    public void setEditAdditionalInsuredBOLineIndustryOilGas(boolean radioValue) {
        Guidewire8RadioButton radio = new Guidewire8RadioButton(driver, "//tr[contains(@id, 'IsInOilandGasIndustry-inputRow')]");
        radio.select(radioValue);
    }


    public void setEditAdditionalInsuredBOLineIndustryUndergroundTanks(boolean radioValue) {
        Guidewire8RadioButton radio = new Guidewire8RadioButton(driver, "//tr[contains(@id, ':IsInUndergroundTanksIndustry-inputRow')]");
        radio.select(radioValue);
    }


    public void setEditAdditionalInsuredBOLineIndustryAircraftAirport(boolean radioValue) {
        Guidewire8RadioButton radio = new Guidewire8RadioButton(driver, "//tr[contains(@id, ':IsInAircraftandAirportIndustry-inputRow')]");
        radio.select(radioValue);
    }


    public void setEditAdditionalInsuredBOLineIndustryBridgeConst(boolean radioValue) {
        Guidewire8RadioButton radio = new Guidewire8RadioButton(driver, "//tr[contains(@id, ':IsInBridgeConstructionIndustr-inputRow')]");
        radio.select(radioValue);
    }


    public void setEditAdditionalInsuredBOLineIndustryDamsReso(boolean radioValue) {
        Guidewire8RadioButton radio = new Guidewire8RadioButton(driver, "//tr[contains(@id, ':IsInDamsandReservoirsIndustry-inputRow')]");
        radio.select(radioValue);
    }


    public void setEditAdditionalInsuredBOLineIndustryFirearms(boolean radioValue) {
        Guidewire8RadioButton radio = new Guidewire8RadioButton(driver, "//tr[contains(@id, ':IsInFirearmsIndustry-inputRow')]");
        radio.select(radioValue);
    }


    public void setEditAdditionalInsuredBOLineIndustryRailroads(boolean radioValue) {
        Guidewire8RadioButton radio = new Guidewire8RadioButton(driver, "//tr[contains(@id, ':IsRailroadsIndustry-inputRow')]");
        radio.select(radioValue);
    }


    public void setEditAdditionalInsuredBOLineIndustryMining(boolean radioValue) {
        Guidewire8RadioButton radio = new Guidewire8RadioButton(driver, "//tr[contains(@id, ':IsInMiningIndustry-inputRow')]");
        radio.select(radioValue);
    }


    public void setEditAdditionalInsuredBOLineIndustryAll(boolean radioValue) {
        setEditAdditionalInsuredBOLineIndustryOilGas(radioValue);
        setEditAdditionalInsuredBOLineIndustryUndergroundTanks(radioValue);
        setEditAdditionalInsuredBOLineIndustryAircraftAirport(radioValue);
        setEditAdditionalInsuredBOLineIndustryBridgeConst(radioValue);
        setEditAdditionalInsuredBOLineIndustryDamsReso(radioValue);
        setEditAdditionalInsuredBOLineIndustryFirearms(radioValue);
        setEditAdditionalInsuredBOLineIndustryRailroads(radioValue);
        setEditAdditionalInsuredBOLineIndustryMining(radioValue);
    }


    public void setEditAdditionalInsuredBOLineWaiverSubro(boolean radioValue) {
        Guidewire8RadioButton radio = new Guidewire8RadioButton(driver, "//tr[contains(@id, ':WaiverofSubro-inputRow')]");
        radio.select(radioValue);
        
    }


    public void setEditAdditionalInsuredBOLineListProducts(String products) {
        try {
            waitUntilElementIsClickable(editbox_SubmissionEditAdditionalInsuredBOLineListProducts);
            editbox_SubmissionEditAdditionalInsuredBOLineListProducts.sendKeys(products);
            
        } catch (Exception e) {
            waitUntilElementIsClickable(editbox_SubmissionEditAdditionalInsuredBOLineListProductsAlt);
            editbox_SubmissionEditAdditionalInsuredBOLineListProductsAlt.sendKeys(products);
            
        }
    }


    public void setEditAdditionalInsuredBOLineRole(AdditionalInsuredRole role) {
        Guidewire8Select selectRole = select_SubmissionEditAdditionalInsuredBOLineRole();
        selectRole.selectByVisibleText(role.getRole());
        
    }


    public void setEditAdditionalInsuredBOLineAddressListing(String addressListing) {
        super.setContactEditAddressListing(addressListing);
        
    }

    public void clickEditAdditionalInsuredBOLineOK() {
        //jlarsen CODE-CAHNGE
        // contact edit screens OK buttons changed to UPDATE
        List<WebElement> updateButton = finds(By.xpath("//span[contains(@id, ':FinishPCR-btnEl')]"));
        if (updateButton.isEmpty()) {
            super.clickOK();
        } else {
        	clickWhenClickable(updateButton.get(0));
        }

        
    }


    public void setEditAdditionalInsuredBOLineAddressLine1(String address) {
        super.setContactEditAddressLine1(address);
        
    }


    public void setEditAdditionalInsuredBOLineAddressCity(String city) {
        super.setContactEditAddressCity(city);
        
    }


    public void setEditAdditionalInsuredBOLineAddressState(State state) {
        super.setContactEditAddressState(state);
        
    }


    public void setEditAdditionalInsuredBOLineAddressZipCode(String zip) {
        super.setContactEditAddressZipCode(zip);
        
    }


    public void setEditAdditionalInsuredBOLineAddressAddressType(AddressType addType) {
        super.setContactEditAddressAddressType(addType);
        
    }


    public void checkAdditionalRolesRemoved() throws Exception {
        Guidewire8Select selectRole = select_SubmissionEditAdditionalInsuredBOLineRole();
        if (selectRole.isItemInList("BP 04 13") || selectRole.isItemInList("BP 04 08")) {
            throw new Exception("ERROR: -- Drop down contains additional options.");
        }
    }

}













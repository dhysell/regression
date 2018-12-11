package repository.pc.workorders.generic;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.idfbins.enums.OkCancel;

import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.DocFormEvents;
import repository.gw.enums.InlandMarineTypes.BoatType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.InlandMarineTypes.WatercraftTypes;
import repository.gw.enums.InlandMarineTypes.WatercratItems;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.errorhandling.ErrorHandling;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.SquireIMWatercraft;
import repository.gw.generate.custom.WatercraftScheduledItems;
import repository.gw.helpers.TableUtils;
import repository.pc.sidemenu.SideMenuPC;

public class GenericWorkorderSquireInlandMarine_Watercraft extends GenericWorkorder {

    private WebDriver driver;
    private TableUtils tableUtils;

    public GenericWorkorderSquireInlandMarine_Watercraft(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }


    public void fillOutWatercraft(GeneratePolicy policy) throws Exception {
        repository.pc.sidemenu.SideMenuPC sideMenu = new SideMenuPC(driver);

        if (policy.squire.inlandMarine.inlandMarineCoverageSelection_PL_IM.contains(InlandMarine.Watercraft)) {
            policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.Squire_IM_AddWatercraft);
            sideMenu.clickSideMenuIMWatercraft();
            for (SquireIMWatercraft watercraft : policy.squire.inlandMarine.watercrafts_PL_IM) {
                watercraft(watercraft);
            }

            //Go to Section II and add specific Watercraft Coverage
            try {
                sideMenu.clickSideMenuSquirePropertyCoverages();
            } catch (Exception e) {
                ErrorHandling softMsg = new ErrorHandling(getDriver());
                if (checkIfElementExists(softMsg.text_ErrorHandlingErrorBanner(), 1000)) {
                    sideMenu.clickSideMenuSquirePropertyCoverages();
                }
            }

            //Add Section II
            repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(getDriver());
            coverages.clickSectionIICoveragesTab();
            repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages sectionII = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(getDriver());
            int motorBoat = 0;
            int personalWatercraft = 0;
            int sailboat = 0;

            for (SquireIMWatercraft waterToy : policy.squire.inlandMarine.watercrafts_PL_IM) {
                if (waterToy.getWaterToyType() == WatercraftTypes.BoatAndMotor) {
                    motorBoat = motorBoat + waterToy.getScheduledItems().size();
                } else if (waterToy.getWaterToyType() == WatercraftTypes.PersonalWatercraft) {
                    personalWatercraft++;
                } else if (waterToy.getWaterToyType() == WatercraftTypes.Sailboat) {
                    sailboat++;
                }
            }

            if (motorBoat > 0) {
                sectionII = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(getDriver());
                SectionIICoverages myCoverages = new SectionIICoverages(SectionIICoveragesEnum.MotorBoats, 0, motorBoat);
                sectionII.addCoverages(myCoverages);
                sectionII.setQuantity(myCoverages);
            }
            if (personalWatercraft > 0) {
                sectionII = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(getDriver());
                SectionIICoverages myCoverages2 = new SectionIICoverages(SectionIICoveragesEnum.PersonalWatercraft, 0, personalWatercraft);
                sectionII.addCoverages(myCoverages2);
                sectionII.setQuantity(myCoverages2);
            }
            if (sailboat > 0) {
                sectionII = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(getDriver());
                SectionIICoverages myCoverages3 = new SectionIICoverages(SectionIICoveragesEnum.Sailboats, 0, sailboat);
                sectionII.addCoverages(myCoverages3);
                sectionII.setQuantity(myCoverages3);
            }

            //watercraft Length
            sectionII = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(getDriver());

            if (policy.squire.inlandMarine.watercrafts_PL_IM.size() > 0) {
                sideMenu.clickSideMenuSquirePropertyCoverages();
                coverages.clickSectionIICoveragesTab();
                sectionII.addCoverages(new SectionIICoverages(SectionIICoveragesEnum.WatercraftLength, 0, 0));
                for (SquireIMWatercraft watercraft : policy.squire.inlandMarine.watercrafts_PL_IM) {
                    for (WatercraftScheduledItems watercraftItem : watercraft.getScheduledItems()) {
                        sectionII.setWatercraftLength(watercraftItem.getModel(), watercraftItem.getLength());
                    }
                }
                sectionII.clicknext();
            }
        }
    }


    //	Details
    @FindBy(xpath = "//span[contains(@id, 'WatercraftPopup:PIMWatercraftAdditionalInsuredDV:AdditionalInsuredLV_tb:Add-btnEl')]")
    private WebElement button_IMWatercraftAdd;

    @FindBy(xpath = "//span[contains(@id, 'WatercraftPopup:PIMWatercraftAdditionalInsuredDV:AdditionalInsuredLV_tb:Add-btnEl')]")
    private WebElement table_IMWatercraftAdd;

    @FindBy(xpath = "//input[contains(@id, 'WatercraftPopup:WatercraftDetailsDV:TitleOwner-inputEl')]")
    private WebElement editbox_IMWatercraftRegistration;

    @FindBy(xpath = "//input[contains(@id, 'WatercraftPopup:WatercraftDetailsDV:HasExistingDamage-inputEl')]")
    private WebElement editbox_IMWatercraftDamageDescription;


    public Guidewire8Select select_IMWatercraftType() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'WatercraftPopup:WatercraftDetailsDV:Type-triggerWrap')]");
    }

    public Guidewire8Select select_IMWatercraftDeductible() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'WatercraftPopup:WatercraftDetailsDV:Deductible-triggerWrap')]");
    }

    public Guidewire8RadioButton radio_IMWatercraftInspected() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'WatercraftPopup:WatercraftDetailsDV:Inspected-containerEl')]/table");
    }

    public Guidewire8RadioButton radio_IMWatercraftExistingDamage() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'WatercraftPopup:WatercraftDetailsDV:ExistingDamage-containerEl')]/table");
    }

    @FindBy(xpath = "//span[contains(@id, 'WatercraftPopup:PIMWatercraftAdditionalInsuredDV:AdditionalInsuredLV_tb:Add-btnEl')]")
    private WebElement button_IMWatercraftAddAdditionalInsureds;

    @FindBy(xpath = "//div[contains(@id, ':PIMWatercraftAdditionalInsuredDV:AdditionalInsuredLV')]")
    private WebElement table_IMWatercraftAdditionalInsureds;

    @FindBy(xpath = "//span[contains(@id, ':AdditionalInsuredLV_tb:Remove-btnEl')]")
    public WebElement button_AditionalInsuredRemove;

    @FindBy(xpath = "//div[contains(@id, 'messagebox') and contains(., 'The deductible was changed on ')]")
    private WebElement text_IMWatercraftDeductibleMessagebox;

    @FindBy(xpath = "//div[contains(@id, 'WatercraftPopup:WatercraftDetailsDV:Limit-inputEl')]")
    private WebElement text_IMWatercraftLimit;

    //	Scheduled Items

    @FindBy(xpath = "//span[contains(@id, 'WatercraftPopup:WatercraftScheduleItemsLDV:Blank_tb:Add-btnEl')]")
    private WebElement button_IMWatercraftAddScheduledItems;

    @FindBy(xpath = "//input[contains(@id, 'WatercraftPopup:WatercraftScheduleItemsLDV:WatercraftScheduleItemDetailsDV:Year-inputEl')]")
    private WebElement editbox_IMWatercraftYear;

    @FindBy(xpath = "//input[contains(@id, 'WatercraftPopup:WatercraftScheduleItemsLDV:WatercraftScheduleItemDetailsDV:Make-inputEl')]")
    private WebElement editbox_IMWatercraftMake;

    @FindBy(xpath = "//input[contains(@id, 'WatercraftPopup:WatercraftScheduleItemsLDV:WatercraftScheduleItemDetailsDV:Model-inputEl')]")
    private WebElement editbox_IMWatercraftModel;

    @FindBy(xpath = "//input[contains(@id, 'WatercraftPopup:WatercraftScheduleItemsLDV:WatercraftScheduleItemDetailsDV:Serial-inputEl')]")
    private WebElement editbox_IMWatercraftVINSerial;

    @FindBy(xpath = "//input[contains(@id, 'WatercraftPopup:WatercraftScheduleItemsLDV:WatercraftScheduleItemDetailsDV:Description-inputEl')]")
    private WebElement editbox_IMWatercraftDescription;

    @FindBy(xpath = "//input[contains(@id, 'WatercraftPopup:WatercraftScheduleItemsLDV:WatercraftScheduleItemDetailsDV:Limit-inputEl')]")
    private WebElement editbox_IMWatercraftLimit;

    @FindBy(xpath = "//input[contains(@id, 'WatercraftPopup:WatercraftScheduleItemsLDV:WatercraftScheduleItemDetailsDV:Length-inputEl')]")
    private WebElement editbox_IMWatercraftLength;

    public Guidewire8Select select_IMWatercraftItem() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'WatercraftPopup:WatercraftScheduleItemsLDV:WatercraftScheduleItemDetailsDV:ItemType-triggerWrap')]");
    }

    public Guidewire8Select select_IMWatercraftBoatType() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'WatercraftPopup:WatercraftScheduleItemsLDV:WatercraftScheduleItemDetailsDV:BoatType-triggerWrap')]");
    }

    @FindBy(xpath = "//div[contains(@id, ':WatercraftPanelSet:MainSelectListView:CoverableLV')]")
    private static WebElement table_WaterCraft;

    @FindBy(xpath = "//span[contains(@id, 'WatercraftPopup:AdditionalInterestDetailsDV:AdditionalInterestLV_tb:AddContactsButton')]")
    private WebElement additionalInterestAddExisting;

    @FindBy(xpath = "//a[contains(@id, 'WatercraftPopup:AdditionalInterestDetailsDV:AdditionalInterestLV_tb:AddContactsButton:AddOtherContact-itemEl')]")
    private WebElement additionalInterestAddExistingOtherContacts;

    @FindBy(xpath = "//div[contains(@id, ':AdditionalInterestDetailsDV:AdditionalInterestLV_tb:AddContactsButton:AddOtherContact-arrowEl')]")
    private WebElement link_AdditionalInterestsOtherContactArrow;


    public WebElement list_additionalInterestExistingBySpecific(String name) {
        return find(By.xpath("//a[contains(@id,':acctContact-itemEl')and contains(.,'" + name + "')]"));
    }

    public Guidewire8Select select_SquireWatercraftdditionalInterestType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':PolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:InterestType-triggerWrap')]");
    }

    @FindBy(xpath = "//a[contains(@id, 'FinishPCR')]")
    private WebElement additionalInterestUpdate;

    @FindBy(xpath = "//div[contains(@id, ':AdditionalInterestDetailsDV:AdditionalInterestLV')]")
    private static WebElement table_WaterCraftAdditionalInterest;
	
	/*
	  					Methods
	 */


    public void selectWatercraftType(WatercraftTypes watercraftType) {
        
        select_IMWatercraftType().selectByVisibleText(watercraftType.getValue());
        
    }


    public String getWatercraftLimit() {
        
        return text_IMWatercraftLimit.getText();
    }


    public void selectDeductible(String deductible) {
        
        select_IMWatercraftDeductible().selectByVisibleText(deductible);
    }


    public void setWatercraftInspectedRadio(boolean trueFalse) {
        
        radio_IMWatercraftInspected().select(trueFalse);
        
    }


    public void setWatercraftExistingDamageRadio(boolean trueFalse) {
        
        radio_IMWatercraftExistingDamage().select(trueFalse);
        
    }


    public void setWatercraftDamageDescription(String description) {
        clickWhenClickable(editbox_IMWatercraftDamageDescription);
        editbox_IMWatercraftDamageDescription.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_IMWatercraftDamageDescription.sendKeys(description);
        editbox_IMWatercraftDamageDescription.sendKeys(Keys.TAB);
    }


    public void setWatercraftOwner(String owner) {
        
        clickWhenClickable(editbox_IMWatercraftRegistration);
        
        editbox_IMWatercraftRegistration.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_IMWatercraftRegistration.sendKeys(owner);
        editbox_IMWatercraftRegistration.sendKeys(Keys.TAB);
    }


    public void clickAddAdditionalInsureds() {
        
        clickWhenClickable(button_IMWatercraftAddAdditionalInsureds);
        
    }


    public void addAdditionalInsured(String name) {
        
        clickAddAdditionalInsureds();
        
        tableUtils.setValueForCellInsideTable(table_IMWatercraftAdditionalInsureds, tableUtils.getNextAvailableLineInTable(table_IMWatercraftAdditionalInsureds), "Name", "FirstName", name);
    }


    public void removeAdditionalInsuredByName(String name) {
        int row = tableUtils.getRowNumberInTableByText(table_IMWatercraftAdditionalInsureds, name);
        tableUtils.setCheckboxInTable(table_IMWatercraftAdditionalInsureds, row, true);
        clickWhenClickable(button_AditionalInsuredRemove);
        
    }


    public String getAdditionalInsuredNameByRowNumber(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_IMWatercraftAdditionalInsureds, row, "Name");
    }


    public void setWatercraftItem(String item) {
        
        select_IMWatercraftItem().selectByVisibleTextPartial(item);
        
    }


    public void setWatercraftYear(String year) {
        clickWhenClickable(editbox_IMWatercraftYear);
        editbox_IMWatercraftYear.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_IMWatercraftYear.sendKeys(year);
        editbox_IMWatercraftYear.sendKeys(Keys.TAB);
    }


    public void setWatercraftMake(String make) {
        clickWhenClickable(editbox_IMWatercraftMake);
        editbox_IMWatercraftMake.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_IMWatercraftMake.sendKeys(make);
        editbox_IMWatercraftMake.sendKeys(Keys.TAB);
    }


    public void setWatercraftVIN(String vin) {
        clickWhenClickable(editbox_IMWatercraftVINSerial);
        editbox_IMWatercraftVINSerial.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_IMWatercraftVINSerial.sendKeys(vin);
        editbox_IMWatercraftVINSerial.sendKeys(Keys.TAB);
    }


    public void setWatercraftDescription(String description) {
        clickWhenClickable(editbox_IMWatercraftDescription);
        editbox_IMWatercraftDescription.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_IMWatercraftDescription.sendKeys(description);
        editbox_IMWatercraftDescription.sendKeys(Keys.TAB);
    }


    public void setWatercraftModel(String model) {
        clickWhenClickable(editbox_IMWatercraftModel);
        editbox_IMWatercraftModel.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_IMWatercraftModel.sendKeys(model);
        editbox_IMWatercraftModel.sendKeys(Keys.TAB);

    }


    public void setWatercraftLimit(int limit) {
        clickWhenClickable(editbox_IMWatercraftLimit);
        editbox_IMWatercraftLimit.click();
        editbox_IMWatercraftLimit.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_IMWatercraftLimit.sendKeys(Integer.toString(limit));
        editbox_IMWatercraftLimit.sendKeys(Keys.TAB);

    }


    public String getDeductibleChangeMessage() {
        String txt = text_IMWatercraftDeductibleMessagebox.getText();
        selectOKOrCancelFromPopup(OkCancel.OK);
        return txt;
    }


    public void clickSearch() {
        super.clickSearch();
    }


    public void clickAdd() {
        super.clickAdd();
    }


    public void clickCancel() {
        super.clickCancel();
    }


    public void clickOK() {
        super.clickOK();
    }


    public void clickAddScheduledItem() {
        clickWhenClickable(button_IMWatercraftAddScheduledItems);
        
    }


    public void setWatercraftLength(int length) {
        clickWhenClickable(editbox_IMWatercraftLength);
        editbox_IMWatercraftLength.click();
        editbox_IMWatercraftLength.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_IMWatercraftLength.sendKeys(Integer.toString(length));
        editbox_IMWatercraftLength.sendKeys(Keys.TAB);
    }


    public void setWatercraftBoatType(BoatType boatType) {
        
        select_IMWatercraftBoatType().selectByVisibleTextPartial(boatType.getValue());
        
    }

    //Fills out just required fields.

    public void watercraft(SquireIMWatercraft tubToy) {
        clickAdd();
        
        selectWatercraftType(tubToy.getWaterToyType());//(recEquip.getRecEquipmentType());
        setWatercraftInspectedRadio(tubToy.getInspected());
        if (tubToy.getDamageDescription() == null || tubToy.getDamageDescription().equals("")) {
            setWatercraftExistingDamageRadio(false);
        } else {
            
            setWatercraftExistingDamageRadio(true);
            setWatercraftDamageDescription(tubToy.getDamageDescription());

            
        }
        setWatercraftOwner(tubToy.getRegisteredOwner());
        selectDeductible(tubToy.getWaterToyDeductible().getValue());
        setWatercraftScheduledItems(tubToy.getScheduledItems());
        clickOK();
    }

    //Adds Scheduled Items

    public void setWatercraftScheduledItems(List<WatercraftScheduledItems> vehicleList) {
        for (int i = 0; i < vehicleList.size(); i++) {
            clickAddScheduledItem();
            
            setWatercraftItem(vehicleList.get(i).getItem().getValue());
            setWatercraftYear(vehicleList.get(i).getYearString());
            sendArbitraryKeys(Keys.TAB);
            waitForPostBack();
            setWatercraftMake(vehicleList.get(i).getMake());
            setWatercraftVIN(vehicleList.get(i).getVin());
            setWatercraftModel(vehicleList.get(i).getModel());
            setWatercraftLimit(vehicleList.get(i).getLimit());
            setWatercraftDescription(vehicleList.get(i).getDescription());
            
            if (!(vehicleList.get(i).getItem().getValue().equals(WatercraftTypes.PersonalWatercraft.getValue())) && !(vehicleList.get(i).getItem().getValue().equals(WatercratItems.Trailer.getValue())) && !(vehicleList.get(i).getItem().getValue().equals(WatercratItems.Motor.getValue())) && !(vehicleList.get(i).getItem().getValue().equals(WatercratItems.Other.getValue()))) {
                setWatercraftLength(vehicleList.get(i).getLength());
                setWatercraftBoatType(vehicleList.get(i).getBoatType());
            }
            if (!(i == vehicleList.size() - 1)) {
                clickAddScheduledItem();
            }
        }
    }


    public void clickEditButton() {
        super.clickEdit();
    }


    public int getWatercraftItemNumberByType(WatercraftTypes type) {
        int returnValue = 0;
        int row = tableUtils.getRowNumberInTableByText(table_WaterCraft, type.getValue());
        returnValue = Integer.parseInt(tableUtils.getCellTextInTableByRowAndColumnName(table_WaterCraft, row, "Watercraft #"));
        return returnValue;
    }


    public void addWatercraftExistingAdditionalInterest(String firstName, AdditionalInterestType type) {
        clickWhenClickable(additionalInterestAddExisting);
        
        hoverOverAndClick(additionalInterestAddExistingOtherContacts);
        hoverOverAndClick(link_AdditionalInterestsOtherContactArrow);
        
        clickWhenClickable(list_additionalInterestExistingBySpecific(firstName));
        
        select_SquireWatercraftdditionalInterestType().selectByVisibleTextPartial(type.getValue());
        clickWhenClickable(additionalInterestUpdate);
    }


    public void clickWatercraftAdditionalInterestByName(String name) {
        tableUtils.clickCellInTableByRowAndColumnName(table_WaterCraftAdditionalInterest, tableUtils.getRowNumberInTableByText(table_WaterCraftAdditionalInterest, name), "Name");
    }


    public boolean checkWatercraftAdditionalInterestByName(String name) {
        return tableUtils.getRowNumberInTableByText(table_WaterCraftAdditionalInterest, name) > 0;
    }


}

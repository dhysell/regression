package repository.pc.workorders.generic;

import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.beust.jcommander.converters.BigDecimalConverter;

import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.Building.ValuationMethod;
import repository.gw.enums.Location.ProtectionClassCode;
import repository.gw.enums.Measurement;
import repository.gw.enums.Property.NumberOfUnits;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;
import persistence.globaldatarepo.entities.AddressTemp2;
import repository.pc.sidemenu.SideMenuPC;

public class GenericWorkorderSquirePropertyDetail extends GenericWorkorder {

    private WebDriver driver;
    private TableUtils tableUtils;

    public GenericWorkorderSquirePropertyDetail(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:propertyType:SelectpropertyType')]")
    public WebElement button_SquirePropertyDetailPropertyType;

    @FindBy(xpath = "//div[contains(@id,':LineWizardStepSet:HOWizardStepGroup:HODwellingHOEScreen:HODwellingSingleHOEPanelSet:1')]")
    private WebElement table_PropertyDetailLocations;

    @FindBy(xpath = "//div[contains(@id,':LineWizardStepSet:HOWizardStepGroup:HODwellingHOEScreen:HODwellingSingleHOEPanelSet:DwellingsPanelSet:DwellingListDetailPanel:HODwellingListLV')]")
    private WebElement table_PropertyDetailProperties;

    public WebElement button_PropertyTypeSelect(PropertyTypePL description) {
        return find(By.xpath("//span[text()='" + description.getValue() + "']/parent::td/preceding-sibling::td/a[contains(., 'Select')]"));
    }

    Guidewire8RadioButton radio_DwellingVacant() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:DwellingVacant-containerEl')]/table");
    }

    public Guidewire8Select selectUnitsInBuilding() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:NumUnits-triggerWrap')]");
    }

    Guidewire8RadioButton radio_fireplace() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:FireWoodStove-containerEl')]/table");
    }

    Guidewire8RadioButton radio_PropertyDetailsChimneysCleanedRegularly() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:ChimneysCleanedReg-containerEl')]/table");
    }

    @FindBy(xpath = "//input[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:DateLastCleaned-inputEl')]")
    public WebElement input_SquirePropertyDetailsDateChimneyLastCleaned;

    Guidewire8RadioButton radio_swimmingPool() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:SwimmingPool-containerEl')]/table");
    }

    Guidewire8RadioButton radio_PeropertyDetailsPoolFence() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:PropertyFenced-containerEl')]/table");
    }

    Guidewire8RadioButton radio_PeropertyDetailsPoolDivingBoard() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:DivingBoard-containerEl')]/table");
    }

    Guidewire8RadioButton radio_PeropertyDetailsPoolSafetyCover() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:SafetyCover-containerEl')]/table");
    }

    Guidewire8RadioButton radio_WaterLeakage() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:WaterLeakage-containerEl')]/table");
    }

    Guidewire8RadioButton radio_ExoticPets() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:AnyAnimals-containerEl')]/table");
    }

    @FindBy(xpath = "//input[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:ExoticAnimDesc-inputEl')]")
    public WebElement input_SquirePropertyDetailPropertyDescribeExoticPets;

    @FindBy(xpath = "//span[contains(@id, ':propertyConstructionTab-btnE')]")
    public WebElement link_SquirePropertyDetailPropertyConstruction;

    @FindBy(xpath = "//div[contains(@id, 'HOBuilding_FBMPopup:DwellingSingleProtectionIdTab-btnEl')]")
    public WebElement link_SquirePropertyDetailProtectionDetails;
    
    @FindBy(css = "span[id*='SingleProtectionIdTab-btnWrap']")
    public WebElement link_SquirePropertyDetailProtectionDetailsTab;

    @FindBy(xpath = "//span[contains(@id, 'HOBuilding_FBMPopup:DwellingDetailsSingleIDTab-btnEl')]")
    public WebElement link_SquirePropertyDetailDetails;

    @FindBy(xpath = "//table[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:DwellingVacant')]")
    public WebElement table_SquirePropertyDetailDwellingVacant;

    @FindBy(xpath = "//table[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:FireWoodStove')]")
    public WebElement table_SquirePropertyDetailWoodFirePlaceWoodStove;


    @FindBy(xpath = "//table[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:SwimmingPool')]")
    public WebElement table_SquirePropertyDetailSwimmingPool;

    @FindBy(xpath = "//span[contains(@id, 'HOBuilding_FBMPopup:HOPropertyOccupantsFBMDV:DwellingOwnerLV_tb:AddContactsButton-btnEl')]")
    public WebElement link_OwnerAddExisting;

    @FindBy(xpath = "//span[contains(@id, 'HOBuilding_FBMPopup:HOPropertyOccupantsFBMDV:DwellingOwnerLV_tb:AddContactsButton:AddExistingContact')]")
    public WebElement link_AddExistingPolicyMember;

    @FindBy(xpath = "//span[contains(@id, ':AddExistingContact:0:ExistingDwellingOwner-textEl')]")
    public WebElement link_AddExistingPolicyMember_Contact;
    //there are two Remove buttons on this page, can't use the method from Common.jave
    @FindBy(xpath = "//a[contains(@id, ':AdditionalInterestLV_tb:Remove')]")
    public WebElement button_PropertyAdditionalInterest_Remove;

    @FindBy(xpath = "//span[contains(@id, ':HOWizardStepGroup:HODwellingHOEScreen:HODwellingSingleHOEPanelSet:DwellingsPanelSet:DwellingListDetailPanel:HODwellingListLV_tb:Remove-btnInnerEl') or contains(@id, ':HOWizardStepGroup:HODwellingHOEScreen:HODwellingSingleHOEPanelSet:DwellingsPanelSet:DwellingListDetailPanel:HODwellingListLV_tb:Remove-btnInnerEl')]")
    public WebElement button_SquirePropertyDetailRemove;

    @FindBy(xpath = "//span[contains(@id, ':AdditionalInsuredLV_tb:Remove-btnEl')]")
    public WebElement button_AditionalInsuredRemove;

    @FindBy(xpath = "//label[contains(@id, 'HOBuilding_FBMPopup:AdditionalInterestDetailsDV:0') and contains(., 'Property')]")
    public WebElement label_SquirePropertyDetailPropertyAdditionalInterests;

    @FindBy(xpath = "//span[contains(@id, ':HOPropertyAdditionalInsuredDV:AdditionalInsuredLV_tb:Add-btnInnerEl')]")
    public WebElement button_SquirePropertyDetailPropertyAdditionalInsured;

    @FindBy(xpath = "//label[contains(@id, 'HOBuilding_FBMPopup:2')]")
    private WebElement label_propertyPageValidationMessage;

    @FindBy(xpath = "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:HORiskAndCategoryInputSet:riskVal-inputEl') or contains(@id, 'HODwellingDetailsHOEDV:HORiskAndCategoryInputSet:riskVal-inputEl')]")
    private WebElement text_readOnlyRisk;

    @FindBy(xpath = "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:HORiskAndCategoryInputSet:ReinsureanceOverrideReadonly-inputEl')]")
    private WebElement text_readOnlyOverrideCategoryCode;

    @FindBy(xpath = "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:HORiskAndCategoryInputSet:ReinsuranceCategoryCode-inputEl') or contains(@id, 'HODwellingDetailsHOEDV:HORiskAndCategoryInputSet:ReinsuranceCategoryCode-inputEl')]")
    private WebElement text_readOnlyCategoryCode;

    @FindBy(xpath = "//input[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:HORiskAndCategoryInputSet:riskVal-inputEl')]")
    private WebElement input_Risk;


    @FindBy(xpath = "//input[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:HORiskAndCategoryInputSet:ReinsureanceOverride-inputE') or contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:HORiskAndCategoryInputSet:ReinsureanceOverride-inputE1') ]")
    private WebElement check_OverrideCategoryCode;

    public Guidewire8Select selectCategoryCode() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:HORiskAndCategoryInputSet:ReinsuranceCategoryCode-triggerWrap')]");
    }

    @FindBy(xpath = "//textarea [contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:HORiskAndCategoryInputSet:ReinsuranceCategoryDesc-inputEl')]")
    private WebElement text_CategoryCodeOverrideReason;

    @FindBy(xpath = "//*[contains(@id, ':ISRBFireProtectionClassAuto-inputEl')]")
    private WebElement text_AutoPublicProptectionClassCode;

    @FindBy(xpath = "//*[contains(@id, ':LongitudeIDAuto-inputEl') or contains(@id, ':LongitudeIDManual-inputEl')]")
    private WebElement text_Longitude;

    @FindBy(xpath = "//*[contains(@id, ':LatitudeIDAuto-inputEl') or contains(@id, ':LatitudeIDManual-inputEl')]")
    private WebElement text_Latitude;

    public Guidewire8Select selectManualPublicProtectionClassCode() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':ISRBFireProtectionClassManual-triggerWrap')]");
    }

    @FindBy(xpath = "//a[contains(@id, ':AdditionalInterestDetailsDV:AdditionalInterestLV_tb:AddContactsButton')]")
    private WebElement button_AdditionalInterests_AddExisting;

    @FindBy(xpath = "//a[contains(@id, ':AdditionalInterestDetailsDV:AdditionalInterestLV_tb:AddContactsButton:AddExistingContact-itemEl') or contains(@id, ':AddContactsButton:AddOtherContact-itemEl')]")
    private WebElement link_AdditionalInterests_ExistingAdditionalInterest;

    @FindBy(xpath = "//div[contains(@id, ':AdditionalInterestDetailsDV:AdditionalInterestLV_tb:AddContactsButton:AddExistingContact-arrowEl')or contains(@id, ':AddContactsButton:AddOtherContact-arrowEl')]")
    private WebElement link_AdditionalInterests_ExistingAdditionalInterestArrow;


    @FindBy(xpath = "//a[contains(text(), 'Return to Property Detail')]")
    private WebElement link_AdditionalInterests_ReturnToPropertyDetails;

    @FindBy(xpath = "//div[contains(@id, ':AdditionalInterestDetailsDV:AdditionalInterestLV')]")
    private WebElement table_propertyAdditionalInterest;


    public Guidewire8Select selectCoverageAPropertyTypeAttached() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:AssociatedDwelling-triggerWrap')]");
    }

    @FindBy(xpath = "//table[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:AssociatedDwelling-triggerWrap')]")
    public WebElement table_SquirePropertyDetailCoverageAPropertyTypeAttached;

    /*
     * Methods
     */


    public void clickAdd() {
        super.clickAdd();
        
    }


    public void clickNext() {
        super.clickNext();
        
    }


    public void clickOk() {
        super.clickOK();
        
    }


    public void clickReturnToPropertyDetail() {
        clickWhenClickable(link_AdditionalInterests_ReturnToPropertyDetails);
        
    }


    public void clickCancel() {
        super.clickCancel();
    }

    public void clickRemovePropertyAdditionalInterest() {
        clickWhenClickable(button_PropertyAdditionalInterest_Remove);
    }


    public void selectBuilding(String buildingNum) {
        
        tableUtils.setCheckboxInTable(table_PropertyDetailProperties, tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(table_PropertyDetailProperties, "Building Number", buildingNum)), true);
        
    }


    public void removeBuilding(String buildingNum) {
        selectBuilding(buildingNum);
        clickWhenClickable(button_SquirePropertyDetailRemove);
    }


    public void clickPropertyInformationDetailsTab() {
        clickWhenClickable(link_SquirePropertyDetailDetails);
    }


    public void clickPropertyConstructionTab() {
        clickWhenClickable(link_SquirePropertyDetailPropertyConstruction);
        
    }


    public void clickProtectionDetailsTab() {
        clickWhenClickable(link_SquirePropertyDetailProtectionDetails);
    }
    
    public void clickProtectionDetailsTabSecondary() { 
        clickWhenClickable(link_SquirePropertyDetailProtectionDetailsTab);
    }


    public void clickPropertyTypeLink() {
        clickWhenClickable(button_SquirePropertyDetailPropertyType);
    }


    public void setPropertyType(PropertyTypePL propertyDescription) {
        clickPropertyTypeLink();
        clickWhenClickable(button_PropertyTypeSelect(propertyDescription));

    }


    public boolean checkPropertyTypeLinkExists() {
        return checkIfElementExists(button_SquirePropertyDetailPropertyType, 1000);
    }


    public void setUnits(NumberOfUnits numberOfUnits) {
        selectUnitsInBuilding().selectByVisibleText(numberOfUnits.getValue());
    }


    public void setDwellingVacantRadio(boolean truefalse) {
        radio_DwellingVacant().select(truefalse);
    }


    public void addAddtionalInsured() {
        clickWhenClickable(button_SquirePropertyDetailPropertyAdditionalInsured);
    }


    public void setAddtionalInsuredName(String additionalInsured) {
        tableUtils.setValueForCellInsideTable(table_addtionalInsured, tableUtils.getNextAvailableLineInTable(table_addtionalInsured, "Name"), "Name", "FirstName", additionalInsured);
    }


    public void removeAdditionalInsuredByName(String additionalInsured) {
        int row = tableUtils.getRowNumberInTableByText(table_addtionalInsured, additionalInsured);
        tableUtils.setCheckboxInTable(table_addtionalInsured, row, true);
        clickWhenClickable(button_AditionalInsuredRemove);
        
    }


    public String getAdditionalInsuredNameByRowNumber(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_addtionalInsured, row, "Name");
    }


    @FindBy(xpath = "//input[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:OccupantIndicator-inputEl')]")
    public WebElement editbox_HowOrByWhomIsThisOccupied;

    @FindBy(xpath = "//div[contains(@id, ':AdditionalInsuredLV')]")
    private WebElement table_addtionalInsured;


    public WebElement getPropertyDetailLocationsTable() {
        waitUntilElementIsVisible(table_PropertyDetailLocations);
        return table_PropertyDetailLocations;
    }


    public WebElement getPropertyDetailPropertiesTable() {
        waitUntilElementIsVisible(table_PropertyDetailProperties);
        return table_PropertyDetailProperties;
    }


    public void setHowOrByWhomIsThisOccupied(String howOrByWhom) {
        waitUntilElementIsClickable(editbox_HowOrByWhomIsThisOccupied);
        editbox_HowOrByWhomIsThisOccupied.sendKeys(howOrByWhom);
    }


    public boolean isHowOrByWhomIsThisOccupiedExist() {
        return checkIfElementExists(editbox_HowOrByWhomIsThisOccupied, 5000);
    }


    public void setWoodFireplaceRadio(boolean truefalse) {
        
        radio_fireplace().select(truefalse);
        
    }


    public void setChimneyCleanedRegularly(boolean trueFalse) {
        
        radio_PropertyDetailsChimneysCleanedRegularly().select(trueFalse);
        
    }


    public void setDateLastCleaned(Date dateCleaned) {
        clickWhenClickable(input_SquirePropertyDetailsDateChimneyLastCleaned);
        input_SquirePropertyDetailsDateChimneyLastCleaned
                .sendKeys(DateUtils.dateFormatAsString("MM/yyyy", dateCleaned));
        
    }


    public void setSwimmingPoolRadio(boolean truefalse) {
        
        radio_swimmingPool().select(truefalse);
        
    }


    public void setPoolFence(boolean truefalse) {
        
        radio_PeropertyDetailsPoolFence().select(truefalse);
        
    }


    public void setPoolDivingBoard(boolean truefalse) {
        
        radio_PeropertyDetailsPoolDivingBoard().select(truefalse);
        
    }


    public void setSafetyCover(boolean truefalse) {
        
        radio_PeropertyDetailsPoolSafetyCover().select(truefalse);
        
    }


    public void setWaterLeakageRadio(boolean truefalse) {
        radio_WaterLeakage().select(truefalse);
        
    }


    public void setExoticPetsRadio(boolean truefalse) {
        
        radio_ExoticPets().select(truefalse);
        
    }


    public void setDescriptionOfExoticPets(String description) {
        setText(input_SquirePropertyDetailPropertyDescribeExoticPets, description);
    }


    public void clickEdit() {
        super.clickEdit();
    }


    public void clickViewOrEditBuildingButton(int bldgNumber) {
        int buildingRowNumber = tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(table_PropertyDetailProperties, "Building Number", String.valueOf(bldgNumber)));
        tableUtils.clickLinkInSpecficRowInTable(table_PropertyDetailProperties, buildingRowNumber);
    }


    public void clickViewOrEditBuildingButtonByRowNumber(int rowNumber) {
        tableUtils.clickLinkInSpecficRowInTable(table_PropertyDetailProperties, rowNumber);
    }


    public void clickViewOrEditBuildingByPropertyType(PropertyTypePL propertyType) {
        int buildingRowNumber = tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(table_PropertyDetailProperties, "Property Type", propertyType.getValue()));
        tableUtils.clickLinkInSpecficRowInTable(table_PropertyDetailProperties, buildingRowNumber);
    }


    public String getAdditionalInterestsLabel() {
        waitUntilElementIsVisible(label_SquirePropertyDetailPropertyAdditionalInterests);
        return label_SquirePropertyDetailPropertyAdditionalInterests.getText();
    }


    public void clickLocation(PolicyLocation location) {
		
	/*	Testing Purposes
	 * while(!(location.getTownshipRange().getTownship().equals("05") && location.getTownshipRange().getTownshipDirection().equals("S") && location.getTownshipRange().getRange().equals("34") && location.getTownshipRange().getRangeDirection().equals("E") )){
			location.setTownshipRange(CountyIdaho.Bannock);
			System.out.println("Township = " + location.getTownshipRange().getTownship());
			System.out.println("TownshipDirection = " + location.getTownshipRange().getTownshipDirection());
			System.out.println("Range = " + location.getTownshipRange().getRange());
			System.out.println("RangeDirection = " + location.getTownshipRange().getRangeDirection());
		}*/

        
        switch (location.getLocationType()) {
            case Address:
                tableUtils.clickRowInTableByRowNumber(table_PropertyDetailLocations, tableUtils.getRowNumberInTableByText(table_PropertyDetailLocations, location.getFullAddressString()));
                break;
            case Legal:
                //Please insert Code here.
                System.out.println("Who knows what to do here?");
                break;
            case SectionTownshipRange:

                String rowToClick = location.getTownshipRange().getTownship() + location.getTownshipRange().getTownshipDirection().toLowerCase() +
                        ", " + location.getTownshipRange().getRange() + location.getTownshipRange().getRangeDirection().toLowerCase() + ", " + location.getTownshipRange().getCounty();
                System.out.println("Row to click is " + rowToClick);

                int row = Integer.valueOf(table_PropertyDetailLocations.findElement(By.xpath("//a[contains(.,'" + rowToClick + "')]/ancestor::tr[1]")).getAttribute("data-recordindex")) + 1;
                tableUtils.clickRowInTableByRowNumber(table_PropertyDetailLocations, row);
                
                break;
            default:
                break;

        }
        
    }


    public void setPropertyDetailsQQ(boolean basicSearch, PLPolicyLocationProperty property) throws GuidewireNavigationException {
        clickAdd();
        setPropertyType(property.getpropertyType());
        if (checkIfElementExists(table_SquirePropertyDetailCoverageAPropertyTypeAttached, 2000)) {
            selectCoverageAPropertyTypeAttached().selectByVisibleTextRandom();
        } else {
            property.setAutoProtectionClass(ProtectionClassCode.valueOfName(getAutoPublicProtectionClassCode()));
            if (property.isOverrideProtectionclass()) {
                setManualPublicProtectionClassCode(property.getManualPublicProtectionClassCode());
            } else {
                if (property.getAutoProtectionClass() != null) {
                    property.setmanualPublicProtectionClassCode(property.getAutoProtectionClass());
                    setManualPublicProtectionClassCode(property.getAutoProtectionClass());
                } else {
                    setManualPublicProtectionClassCode(property.getManualPublicProtectionClassCode());
                }
            }

            try {
                property.getAddress().getDbAddress().setProtectionClass(getAutoPublicProtectionClassCode());
                property.getAddress().getDbAddress().setLatitudeFbm(new BigDecimalConverter("lat").convert(getPropertyLatitude()));
                property.getAddress().getDbAddress().setLongitudeFbm(new BigDecimalConverter("long").convert(getPropertyLogitude()));
                AddressTemp2.updateProtectionClass(property.getAddress().getDbAddress());
            } catch (Exception e) {
            }

            if (checkIfElementExists(table_SquirePropertyDetailDwellingVacant, 2000)) {

                if (!property.getpropertyType().equals(PropertyTypePL.GrainSeed) && !property.getpropertyType().equals(PropertyTypePL.Potatoes))
                    setUnits(property.getNumberOfUnits());

                if (checkIfElementExists(table_SquirePropertyDetailWoodFirePlaceWoodStove, 2000)) {
                    setWoodFireplaceRadio(property.getWoodFireplace());
                    
                    if (property.getWoodFireplace()) {
                        setChimneyCleanedRegularly(property.getChimneysCleanedRegularly());
                        setDateLastCleaned(property.getLastCleaned());
                    }
                }
            }
        }
        if (property.getBuildingAdditionalInterest().size() > 0) {
            for (AdditionalInterest bldgAddInterest : property.getBuildingAdditionalInterest()) {
                GenericWorkorderAdditionalInterests additionalInterest = new GenericWorkorderAdditionalInterests(driver);
                additionalInterest.fillOutAdditionalInterest(basicSearch, bldgAddInterest);
            }
        }

        if (property.owner) {
            GenericWorkorderSquirePropertyDetail propertyDetail = new GenericWorkorderSquirePropertyDetail(driver);
            propertyDetail.AddExistingOwner();
        }

        if (!property.getpropertyType().equals(PropertyTypePL.TrellisedHops) && !property.getpropertyType().equals(PropertyTypePL.SolarPanels)) {
            clickPropertyConstructionTab();
        }
    }


    public void editPropertyDetailsFA(PLPolicyLocationProperty property) {
        clickViewOrEditBuildingButton(property.getPropertyNumber());
        if (checkIfElementExists(table_SquirePropertyDetailDwellingVacant, 500)) {
            setDwellingVacantRadio(property.getDwellingVacant());
            if (isHowOrByWhomIsThisOccupiedExist()) {
                setHowOrByWhomIsThisOccupied("Supernaturally");
            }
            if (checkIfElementExists(table_SquirePropertyDetailSwimmingPool, 500)) {
                setSwimmingPoolRadio(property.getSwimmingPool());
                
                if (property.getSwimmingPool()) {
                    setPoolDivingBoard(property.getDivingBoard());
                    setSafetyCover(property.getPoolSafetyCover());
                }
                
                setWaterLeakageRadio(property.getWaterLeaking());
                
                setExoticPetsRadio(property.getAnimals());
                if (property.getAnimals()) {
                    
                    setDescriptionOfExoticPets(property.getAnimalsDescription());
                }
            }
        }
        //jlarsen moved to fillout property construciton tab method.
        //should NEVER assume that the user will ALWAYS want to go to the next tab.
//		if(!property.getpropertyType().equals(PropertyTypePL.TrellisedHops) && !property.getpropertyType().equals(PropertyTypePL.SolarPanels)){
//			clickPropertyConstructionTab();
//		}

    }


    public void setPropertyDetailsFA(PLPolicyLocationProperty property) {
        clickAdd();
        setPropertyType(property.getpropertyType());

        if (property.getpropertyType().equals(PropertyTypePL.DetachedGarage)) {
            selectCoverageAPropertyTypeAttached().selectByVisibleTextRandom();
        }
        if (property.getpropertyType().equals(PropertyTypePL.TrellisedHops) || property.getpropertyType().equals(PropertyTypePL.Barn))
            setManualPublicProtectionClassCode(ProtectionClassCode.Prot3);
        if (checkIfElementExists(table_SquirePropertyDetailDwellingVacant, 2000)) {
            setManualPublicProtectionClassCode(ProtectionClassCode.Prot3);
            setDwellingVacantRadio(property.getDwellingVacant());
            if (isHowOrByWhomIsThisOccupiedExist()) {
                setHowOrByWhomIsThisOccupied("Supernaturally");
            }

            if (!property.getpropertyType().equals(PropertyTypePL.GrainSeed) && !property.getpropertyType().equals(PropertyTypePL.Potatoes)) {
                setUnits(property.getNumberOfUnits());

                if (checkIfElementExists(table_SquirePropertyDetailWoodFirePlaceWoodStove, 2000)) {
                    setWoodFireplaceRadio(property.getWoodFireplace());
                    
                    if (property.getWoodFireplace()) {
                        setChimneyCleanedRegularly(property.getChimneysCleanedRegularly());
                        setDateLastCleaned(property.getLastCleaned());
                    }
                }
                if (checkIfElementExists(table_SquirePropertyDetailSwimmingPool, 2000)) {
                    setSwimmingPoolRadio(property.getSwimmingPool());
                    
                    if (property.getSwimmingPool()) {
                        setPoolDivingBoard(property.getDivingBoard());
                        setSafetyCover(property.getPoolSafetyCover());
                    }
                    setWaterLeakageRadio(property.getWaterLeaking());
                    
                    setExoticPetsRadio(property.getAnimals());
                    if (property.getAnimals()) {
                        
                        setDescriptionOfExoticPets(property.getAnimalsDescription());
                    }
                }
            }
        }

        if (!property.getpropertyType().equals(PropertyTypePL.TrellisedHops) && !property.getpropertyType().equals(PropertyTypePL.SolarPanels)) {
            clickPropertyConstructionTab();
        }

    }

//	


    public void setCoverageE(PLPolicyLocationProperty property, SectionIDeductible deductible) {
        clickPropertyConstructionTab();

        GenericWorkorderSquirePropertyDetailConstruction shopConstructionPage = new GenericWorkorderSquirePropertyDetailConstruction(driver);
        shopConstructionPage.setYearBuilt(property.getYearBuilt());
        shopConstructionPage.setConstructionType(property.getConstructionType());
        shopConstructionPage.setSquareFootage(property.getSquareFootage());
        shopConstructionPage.setFoundationType(property.getFoundationType());
        shopConstructionPage.setRoofType(property.getRoofType());
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction genericWorkorderSquirePropertyAndLiabilityPropertyDetail_propertyConstruction = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        genericWorkorderSquirePropertyAndLiabilityPropertyDetail_propertyConstruction.setMeasurement(Measurement.SQFT);
        genericWorkorderSquirePropertyAndLiabilityPropertyDetail_propertyConstruction.setPolyurethaneAndSandwichAndDescription(property.getPolyurethane(), property.getSandwichedPolyurethane(), property.getSandwichedPolyurethaneDescription());
        if(property.getPolyurethane() && !property.getSandwichedPolyurethane()){
            genericWorkorderSquirePropertyAndLiabilityPropertyDetail_propertyConstruction.setOpenFlameCoverageWanted(property.isOpenFalme());
        }
        genericWorkorderSquirePropertyAndLiabilityPropertyDetail_propertyConstruction.clickOK();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(
                driver);
        propertyDetail.clickOkayIfMSPhotoYearValidationShows();

        property.setPropertyNumber(getSelectedBuildingNum());
        clickNext();


        GenericWorkorderSquirePropertyCoverages shopCoverages = new GenericWorkorderSquirePropertyCoverages(driver);
        shopCoverages.clickSpecificBuilding(1, property.getPropertyNumber());
        shopCoverages.selectSectionIDeductible(deductible);
        shopCoverages.setCoverageELimit(property.getPropertyCoverages().getCoverageA().getLimit());
        shopCoverages.setCoverageECoverageType(property.getPropertyCoverages().getCoverageC().getCoverageType());
        shopCoverages.setCoverageEValuation(ValuationMethod.ActualCashValue);
    }

    // Use this method to add buildings.

    public void setProperty(PLPolicyLocationProperty property, SectionIDeductible deductible) throws GuidewireNavigationException {
        clickAdd();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionDetails = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);

        switch (property.getpropertyType()) {
        case ResidencePremises:
            setPropertyType(property.getpropertyType());
            if (checkIfElementExists(table_SquirePropertyDetailDwellingVacant, 2000)) {
                setDwellingVacantRadio(property.getDwellingVacant());
                setUnits(property.getNumberOfUnits());
                if (property.getWoodFireplace()) {
                    setWoodFireplaceRadio(property.getWoodFireplace());
                    setChimneyCleanedRegularly(property.getChimneysCleanedRegularly());
                    setDateLastCleaned(property.getLastCleaned());
                } else {
                    setWoodFireplaceRadio(property.getWoodFireplace());
                }
                if (property.getSwimmingPool()) {
                    setSwimmingPoolRadio(property.getSwimmingPool());
                    setPoolDivingBoard(property.getDivingBoard());
                    setSafetyCover(property.getPoolSafetyCover());
                } else {
                    setSwimmingPoolRadio(property.getSwimmingPool());
                }
                setWaterLeakageRadio(property.getWaterLeaking());
                
                if (property.getAnimals()) {
                    setExoticPetsRadio(true);
                    
                    setDescriptionOfExoticPets(property.getAnimalsDescription());
                } else {
                    setExoticPetsRadio(false);
                }
            }
            clickPropertyConstructionTab();

            GenericWorkorderSquirePropertyDetailConstruction constructionPage = new GenericWorkorderSquirePropertyDetailConstruction(driver);
            constructionPage.setYearBuilt(property.getYearBuilt());
            constructionPage.setConstructionType(property.getConstructionType());
            constructionDetails.setBathClass(property.getBathClass());
            constructionPage.setSquareFootage(property.getSquareFootage());
            constructionDetails.setBasementFinished(String.valueOf(property.getBasementFinishedPercent()));
            constructionDetails.setStories(property.getstoriesPL());
            constructionDetails.setGarage(property.getGarage());
            
            constructionDetails.setLargeShed(property.getLargeShed());
            constructionDetails.setCoveredPorches(property.getCoveredPorches());
            constructionPage.setFoundationType(property.getFoundationType());
            constructionPage.setRoofType(property.getRoofType());
            constructionDetails.setPrimaryHeating(property.getPrimaryHeating());
            constructionDetails.setPlumbing(property.getPlumbing());
            constructionDetails.setWiring(property.getWiring());
            constructionDetails.setElectricalSystem(property.getElectricalSystem());
            constructionDetails.setAmps(property.getAmps());
            constructionDetails.setKitchenClass(property.getBathClass());
            constructionDetails.clickProtectionDetailsTab();

            new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails(driver).fillOutPropertyProtectionDetails_QQ(property);

            break;
	        case DwellingUnderConstructionCovE:
	            setPropertyType(property.getpropertyType());
	            if (checkIfElementExists(table_SquirePropertyDetailDwellingVacant, 2000)) {
	                setDwellingVacantRadio(property.getDwellingVacant());
	                setUnits(property.getNumberOfUnits());
	            }
	            clickPropertyConstructionTab();
	
	            constructionPage = new GenericWorkorderSquirePropertyDetailConstruction(driver);
	            constructionPage.setYearBuilt(property.getYearBuilt());
	            constructionPage.setConstructionType(property.getConstructionType());
	            constructionDetails.setBathClass(property.getBathClass());
	            constructionPage.setSquareFootage(property.getSquareFootage());
	            constructionDetails.setBasementFinished(String.valueOf(property.getBasementFinishedPercent()));
	            constructionDetails.setStories(property.getstoriesPL());
	            constructionDetails.setGarage(property.getGarage());
	            constructionDetails.setLargeShed(property.getLargeShed());
	            constructionDetails.setCoveredPorches(property.getCoveredPorches());
	            constructionPage.setFoundationType(property.getFoundationType());
	            constructionPage.setRoofType(property.getRoofType());
	            constructionDetails.setPrimaryHeating(property.getPrimaryHeating());
	            constructionDetails.setPlumbing(property.getPlumbing());
	            constructionDetails.setWiring(property.getWiring());
	            constructionDetails.setElectricalSystem(property.getElectricalSystem());
	            constructionDetails.setAmps(property.getAmps());
	            constructionDetails.setKitchenClass(property.getBathClass());
	            constructionDetails.setMeasurement(Measurement.SQFT);
	            clickOk(); // Double up for Photo year validation
	            clickOk(); // Double up for Photo year validation
	            break;
            case Shop:
                setPropertyType(property.getpropertyType());
                setCoverageE(property, deductible);
                break;
            case Quonset:
                setPropertyType(property.getpropertyType());
                setCoverageE(property, deductible);
                break;
            case VegetableCellar:
                setPropertyType(property.getpropertyType());
                setCoverageE(property, deductible);
                break;

            case VegetableWarehouse:
                setPropertyType(property.getpropertyType());
                setCoverageE(property, deductible);
                break;

            case MiscBuilding:
                setPropertyType(property.getpropertyType());
                clickPropertyConstructionTab();
                GenericWorkorderSquirePropertyDetailConstruction miscBuildingConstructionPage = new GenericWorkorderSquirePropertyDetailConstruction(driver);
                constructionDetails = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
                miscBuildingConstructionPage.setYearBuilt(property.getYearBuilt());
                miscBuildingConstructionPage.setConstructionType(property.getConstructionType());
                miscBuildingConstructionPage.setSquareFootage(property.getSquareFootage());
                miscBuildingConstructionPage.setFoundationType(property.getFoundationType());
                miscBuildingConstructionPage.setRoofType(property.getRoofType());
                constructionDetails.setPropertyDescription(property.getConstructionDescription());
                if (constructionDetails.setPolyurethane(true)) {
                    Assert.fail("Polyurethane questions should not be available for Misc Building Property Type.");
                }
                miscBuildingConstructionPage.clickOK();
                clickNext();

                GenericWorkorderSquirePropertyCoverages shopCoverages = new GenericWorkorderSquirePropertyCoverages(driver);
                shopCoverages.selectSectionIDeductible(deductible);
                shopCoverages.setCoverageELimit(property.getPropertyCoverages().getCoverageA().getLimit());
                shopCoverages.setCoverageECoverageType(property.getPropertyCoverages().getCoverageC().getCoverageType());
                shopCoverages.setCoverageEValuation(ValuationMethod.ActualCashValue);
                break;
            default:
                break;
        }
    }


    public int getSelectedBuildingNum() {
        waitUntilElementIsVisible(table_PropertyDetailProperties);
        int row = tableUtils.getHighlightedRowNumber(table_PropertyDetailProperties);
        return Integer.valueOf(tableUtils.getCellTextInTableByRowAndColumnName(table_PropertyDetailProperties, row, "Building Number"));
    }


    public void AddExistingOwner() {
        
        clickWhenClickable(link_OwnerAddExisting);
        hoverOver(link_AddExistingPolicyMember);
        clickWhenClickable(link_AddExistingPolicyMember_Contact);
        clickWhenClickable(link_AddExistingPolicyMember_Contact);
    }


    public int getPropertiesCount() {
        return tableUtils.getRowCount(table_PropertyDetailProperties);
    }


    public void setCheckBoxByRowInPropertiesTable(int row, boolean trueFalse) {
        tableUtils.setCheckboxInTable(table_PropertyDetailProperties, row, trueFalse);
    }


    public void clickRemoveProperty() {
        clickWhenClickable(button_SquirePropertyDetailRemove);
    }


    public void highLightPropertyLocationByNumber(int row) {
        tableUtils.clickRowInTableByRowNumber(table_PropertyDetailLocations, row);
    }


    public String getPropertyValidationMessage() {
        waitUntilElementIsVisible(label_propertyPageValidationMessage);
        return label_propertyPageValidationMessage.getText();
    }


    public String getReadOnlyRiskValue() {
        return text_readOnlyRisk.getText();
    }


    public String getReadOnlyOverrideCategoryCode() {
        return text_readOnlyOverrideCategoryCode.getText();
    }


    public String getReadOnlyCategoryCode() {
        return text_readOnlyCategoryCode.getText();
    }


    public void setRisk(String text) {
        setText(input_Risk, text);
    }


    public String getRisk() {
        return input_Risk.getAttribute("value");
    }


    public void clickOverrideCategoryCodeCheck() {
        
        check_OverrideCategoryCode.click();
    }


    public void setCategoryCodeReason(String code, String reason) {
        
        selectCategoryCode().selectByVisibleText(code);
        
        text_CategoryCodeOverrideReason.sendKeys(reason);
        
    }


    public String getAutoPublicProtectionClassCode() {
        return text_AutoPublicProptectionClassCode.getText();
    }


    public String getManualPublicProtectionClassCode() {
        return selectManualPublicProtectionClassCode().getText();
    }


    public void setManualPublicProtectionClassCode(ProtectionClassCode code) {
        selectManualPublicProtectionClassCode().selectByVisibleTextPartial(code.getValue());
    }


    public String getPropertyLogitude() {
        return text_Longitude.getText();
    }


    public String getPropertyLatitude() {
        return text_Latitude.getText();
    }


    public void setPropertyLogitude(String longitude) {
        setText(text_Longitude, longitude);
    }


    public void setPropertyLatitude(String latitude) {
        setText(text_Latitude, latitude);
    }


    public List<String> getPropertiesList() {
        return tableUtils.getAllCellTextFromSpecificColumn(table_PropertyDetailProperties, "Property Type");
    }


    public void highLightPropertiesByNumber(int row) {
        tableUtils.clickRowInTableByRowNumber(table_PropertyDetailProperties, row);
    }


    public boolean checkAreChimneysCleanedRegularlySubQuestionExists() {
        return checkIfElementExists("//label[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:ChimneysCleanedReg-labelEl')]", 2000);
    }


    public boolean checkDatelastcleanedSubQuestioExists() {
        return checkIfElementExists("//label[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:DateLastCleaned-labelEl')]", 2000);
    }


    public boolean checkWoodFireplaceOrWoodStoveQuestionExists() {
        return checkIfElementExists("//label[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:FireWoodStove-labelEl')]", 2000);
    }


    public void addExistingAdditionalInterest(String contactName) {
        
        clickWhenClickable(button_AdditionalInterests_AddExisting);
        
        hoverOverAndClick(link_AdditionalInterests_ExistingAdditionalInterest);
        
        hoverOverAndClick(link_AdditionalInterests_ExistingAdditionalInterestArrow);
        
        String xPath = "//span[contains(text(), '" + contactName + "')]/parent::a/parent::div";
        WebElement lienHolder = find(By.xpath(xPath));
        clickWhenClickable(lienHolder);
    }


    public void addAdditionalInterest(boolean basicSearch, AdditionalInterest bldgAddInterest) throws GuidewireNavigationException {
        setBuildingAdditionalInterests(basicSearch, bldgAddInterest);
    }


    public void setBuildingAdditionalInterests(boolean basicSearch, AdditionalInterest bldgAddInterest) throws GuidewireNavigationException {
        GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
        additionalInterests.fillOutAdditionalInterest(basicSearch, bldgAddInterest);
    }


    public void removeAdditionalInterestAsFirstMortgage() {
        
        String firstMortageGridColumnID = tableUtils.getGridColumnFromTable(table_propertyAdditionalInterest, "First Mortgage");
        table_propertyAdditionalInterest.findElement(By.xpath(".//tr/td[contains(@class, '" + firstMortageGridColumnID + "')]/div[contains(text(),'Yes')]/../preceding-sibling::td[contains(@class, 'rowcheckcolumn')]")).click();
        
        clickRemovePropertyAdditionalInterest();
    }


    public void clickAdditionalInterestByName(String name) {
        tableUtils.clickCellInTableByRowAndColumnName(table_propertyAdditionalInterest, tableUtils.getRowNumberInTableByText(table_propertyAdditionalInterest, name), "Name");
    }


    public boolean checkAdditionalInterestByName(String name) {
        return tableUtils.getRowNumberInTableByText(table_propertyAdditionalInterest, name) > 0;
    }


    public void setCoverageAPropertyTypeAttachedTo(String propertyType) {
        
        selectCoverageAPropertyTypeAttached().selectByVisibleTextPartial(propertyType);
        
    }


    public void fillOutPLPropertyQQ(boolean basicSearch, PLPolicyLocationProperty property, SectionIDeductible deductible, PolicyLocation location) throws Exception {
        repository.pc.sidemenu.SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        //MOVEME
        
        clickLocation(location);
        setPropertyDetailsQQ(basicSearch, property);

        GenericWorkorderSquirePropertyDetailConstruction constructionPage = new GenericWorkorderSquirePropertyDetailConstruction(driver);
        //MOVEME
        constructionPage.setCoverageAPropertyDetailsQQ(property);

        if (constructionPage.isProtectionDetailsTabExists()) {
            constructionPage.clickProtectionDetails();
            new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails(driver).fillOutPropertyProtectionDetails_QQ(property);
        }
        
        GenericWorkorderSquirePropertyCoverages coverages2 = new GenericWorkorderSquirePropertyCoverages(driver);
        coverages2.clickOk();
        
        property.setPropertyNumber(getSelectedBuildingNum());
    }//END fillOutPLPropertyQQ


}















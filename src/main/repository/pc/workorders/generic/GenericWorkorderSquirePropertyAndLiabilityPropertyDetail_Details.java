package repository.pc.workorders.generic;

import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.beust.jcommander.converters.BigDecimalConverter;

import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.Location.ProtectionClassCode;
import repository.gw.enums.Property.NumberOfUnits;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;
import persistence.globaldatarepo.entities.AddressTemp2;

public class GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details extends GenericWorkorderSquirePropertyAndLiabilityPropertyDetail {

    private TableUtils tableUtils;
    private WebDriver driver;

    public GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    public void removeAdditionalInterestAsFirstMortgage() {
        
        String firstMortageGridColumnID = new TableUtils(driver).getGridColumnFromTable(table_propertyAdditionalInterest, "First Mortgage");
        table_propertyAdditionalInterest.findElement(By.xpath(".//tr/td[contains(@class, '" + firstMortageGridColumnID + "')]/div[contains(text(),'Yes')]/../preceding-sibling::td[contains(@class, 'rowcheckcolumn')]")).click();
        
        clickRemovePropertyAdditionalInterest();
    }
    
//	private void isOnPage(PLPolicyLocationProperty property) {
//		if(finds(By.xpath("//span[text()='Property Information']")).isEmpty()) {//in a property. assumed to be correct property
//			if(finds(By.xpath("//span[contains(@id, ':HODwellingHOEScreen:ttlBar')]")).isEmpty()) {//on right wizard step
//				new SideMenuPC(driver).clickSideMenuSquirePropertyDetail();
//			}
//			clickViewOrEditBuildingButton(property.getPropertyNumber());
//		} else {
//			//do nothig on right page
//		}
//	}
	

    public void fillOutPropertyDetails_QQ(boolean basicSearch, PLPolicyLocationProperty property) throws GuidewireNavigationException {
        setPropertyType(property);
        

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

//			if (checkIfElementExists(table_SquirePropertyDetailDwellingVacant, 2000)) {
//
//				if (!property.getpropertyType().equals(PropertyTypePL.GrainSeed) && !property.getpropertyType().equals(PropertyTypePL.Potatoes))
//					setUnits(property.getNumberOfUnits());
//
//				if (checkIfElementExists(table_SquirePropertyDetailWoodFirePlaceWoodStove, 2000)) {
//					setWoodFireplaceRadio(property.getWoodFireplace());
//					if (property.getWoodFireplace()) {
//						setChimneyCleanedRegularly(property.getChimneysCleanedRegularly());
//						setDateLastCleaned(property.getLastCleaned());
//					}
//				}
//			}
        }
        if (property.getBuildingAdditionalInterest().size() > 0) {
            setPropertyAdditionalInterests(basicSearch, property);
        }

        if (property.owner && !property.getpropertyType().equals(PropertyTypePL.Garage)) {
            AddExistingOwner();
        }

        //		if (!property.getpropertyType().equals(PropertyTypePL.TrellisedHops) && !property.getpropertyType().equals(PropertyTypePL.SolarPanels)) {
        //			clickPropertyConstructionTab();
        //		}
    }

    public void fillOutPropertyDetails_FA(PLPolicyLocationProperty property) {

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
    }

    public void fillOutPropertyDetails(PLPolicyLocationProperty property) throws GuidewireNavigationException {
        setPropertyType(property);

        if (property.getpropertyType().equals(PropertyTypePL.DetachedGarage)) {
            selectCoverageAPropertyTypeAttached().selectByVisibleTextRandom();
        }
        if (property.getpropertyType().equals(PropertyTypePL.TrellisedHops) || property.getpropertyType().equals(PropertyTypePL.Barn)) {
        	setManualPublicProtectionClassCode(ProtectionClassCode.Prot3);
        }
            
        if (checkIfElementExists(table_SquirePropertyDetailDwellingVacant, 2000)) {
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
        	
//            setManualPublicProtectionClassCode(ProtectionClassCode.Prot3);
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
        
        if (property.getBuildingAdditionalInterest().size() > 0) {
            setPropertyAdditionalInterests(false, property);
        }

        if (property.owner && !property.getpropertyType().equals(PropertyTypePL.Garage)) {
            AddExistingOwner();
        }

        //		if (!property.getpropertyType().equals(PropertyTypePL.TrellisedHops) && !property.getpropertyType().equals(PropertyTypePL.SolarPanels)) {
        //			clickPropertyConstructionTab();
        //		}
    }


    public void setPropertyType(PLPolicyLocationProperty property) {
        setPropertyType(property.getpropertyType());
    }

    public void setPublicProtection(PLPolicyLocationProperty property) {

    }

    public void setRiskAndCategory(PLPolicyLocationProperty property) {

    }

    public void setOwners(PLPolicyLocationProperty property) {

    }

    public void setPropertyAdditionalInterests(boolean basicSearch, PLPolicyLocationProperty property) throws GuidewireNavigationException {
        for (AdditionalInterest bldgAddInterest : property.getBuildingAdditionalInterest()) {
            GenericWorkorderAdditionalInterests additionalInterest = new GenericWorkorderAdditionalInterests(driver);
            additionalInterest.fillOutAdditionalInterest(basicSearch, bldgAddInterest);
        }
    }

    public void setPropertyAdditionalInsureds(PLPolicyLocationProperty property) {

    }

    public void fillOutDetailsQuestions(PLPolicyLocationProperty property) {

    }


    private WebElement button_PropertyTypeSelect(PropertyTypePL description) {
        return find(By.xpath("//span[text()='" + description.getValue() + "']/parent::td/preceding-sibling::td/a[contains(., 'Select')]"));
    }

    public void setPropertyType(PropertyTypePL propertyDescription) {
        clickPropertyTypeLink();
        clickWhenClickable(button_PropertyTypeSelect(propertyDescription));
    }

    public void clickPropertyTypeLink() {
        clickWhenClickable(button_SquirePropertyDetailPropertyType);
        long endTime = new Date().getTime() + 5000;
        do {
            waitForPostBack();
        }
        while (finds(By.xpath("//span[text()='Property Type Search']")).isEmpty() && new Date().getTime() < endTime);
    }

    @FindBy(xpath = "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:propertyType:SelectpropertyType')]")
    private WebElement button_SquirePropertyDetailPropertyType;

    public boolean checkPropertyTypeLinkExists() {
        return checkIfElementExists(button_SquirePropertyDetailPropertyType, 1000);
    }

    @FindBy(xpath = "//table[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:AssociatedDwelling-triggerWrap')]")
    private WebElement table_SquirePropertyDetailCoverageAPropertyTypeAttached;

    public void setDateLastCleaned(Date dateCleaned) {
        clickWhenClickable(input_SquirePropertyDetailsDateChimneyLastCleaned);
        input_SquirePropertyDetailsDateChimneyLastCleaned.sendKeys(DateUtils.dateFormatAsString("MM/yyyy", dateCleaned));
    }

    @FindBy(xpath = "//input[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:DateLastCleaned-inputEl')]")
    private WebElement input_SquirePropertyDetailsDateChimneyLastCleaned;

    public void setChimneyCleanedRegularly(boolean trueFalse) {
        radio_PropertyDetailsChimneysCleanedRegularly().select(trueFalse);
    }

    private Guidewire8RadioButton radio_PropertyDetailsChimneysCleanedRegularly() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:ChimneysCleanedReg-containerEl')]/table");
    }

    public void setWoodFireplaceRadio(boolean truefalse) {
        radio_fireplace().select(truefalse);
    }

    private Guidewire8RadioButton radio_fireplace() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:FireWoodStove-containerEl')]/table");
    }

    @FindBy(xpath = "//table[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:FireWoodStove')]")
    private WebElement table_SquirePropertyDetailWoodFirePlaceWoodStove;

    public void setUnits(NumberOfUnits numberOfUnits) {
        selectUnitsInBuilding().selectByVisibleText(numberOfUnits.getValue());
    }

    public String getUnits() {
        return selectUnitsInBuilding().getText();
    }

    private Guidewire8Select selectUnitsInBuilding() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:NumUnits-triggerWrap')]");
    }

    @FindBy(xpath = "//table[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:DwellingVacant')]")
    private WebElement table_SquirePropertyDetailDwellingVacant;

    public void setManualPublicProtectionClassCode(ProtectionClassCode code) {
        selectManualPublicProtectionClassCode().selectByVisibleTextPartial(code.getValue());
    }

    private Guidewire8Select selectManualPublicProtectionClassCode() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':ISRBFireProtectionClassManual-triggerWrap')]");
    }

    public String getPropertyLogitude() {
        return text_Longitude.getText();
    }

    @FindBy(xpath = "//*[contains(@id, ':LongitudeIDAuto-inputEl') or contains(@id, ':LongitudeIDManual-inputEl')]")
    private WebElement text_Longitude;

    @FindBy(xpath = "//*[contains(@id, ':LatitudeIDAuto-inputEl') or contains(@id, ':LatitudeIDManual-inputEl')]")
    private WebElement text_Latitude;

    public String getAutoPublicProtectionClassCode() {
        return text_AutoPublicProptectionClassCode.getText();
    }

    public String getManualPublicProtectionClassCode() {
        return selectManualPublicProtectionClassCode().getText();
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

    @FindBy(xpath = "//*[contains(@id, ':ISRBFireProtectionClassAuto-inputEl')]")
    private WebElement text_AutoPublicProptectionClassCode;

    public Guidewire8Select selectCoverageAPropertyTypeAttached() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:AssociatedDwelling-triggerWrap')]");
    }

    public void setDwellingVacantRadio(boolean truefalse) {
        radio_DwellingVacant().select(truefalse);
    }

    private Guidewire8RadioButton radio_DwellingVacant() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:DwellingVacant-containerEl')]/table");
    }

    public void setDescriptionOfExoticPets(String description) {
        input_SquirePropertyDetailPropertyDescribeExoticPets.click();
        input_SquirePropertyDetailPropertyDescribeExoticPets.sendKeys(description);
    }

    @FindBy(xpath = "//input[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:ExoticAnimDesc-inputEl')]")
    public WebElement input_SquirePropertyDetailPropertyDescribeExoticPets;

    public void setExoticPetsRadio(boolean truefalse) {
        radio_ExoticPets().select(truefalse);
    }

    private Guidewire8RadioButton radio_ExoticPets() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:AnyAnimals-containerEl')]/table");
    }

    public void setWaterLeakageRadio(boolean truefalse) {
        radio_WaterLeakage().select(truefalse);
    }

    private Guidewire8RadioButton radio_WaterLeakage() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:WaterLeakage-containerEl')]/table");
    }

    public void setSafetyCover(boolean truefalse) {
        radio_PeropertyDetailsPoolSafetyCover().select(truefalse);
    }

    private Guidewire8RadioButton radio_PeropertyDetailsPoolSafetyCover() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:SafetyCover-containerEl')]/table");
    }

    public void setPoolDivingBoard(boolean truefalse) {
        radio_PeropertyDetailsPoolDivingBoard().select(truefalse);
    }

    private Guidewire8RadioButton radio_PeropertyDetailsPoolDivingBoard() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:DivingBoard-containerEl')]/table");
    }

    public void setSwimmingPoolRadio(boolean truefalse) {
        radio_swimmingPool().select(truefalse);
    }

    private Guidewire8RadioButton radio_swimmingPool() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:SwimmingPool-containerEl')]/table");
    }

    @FindBy(xpath = "//table[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:SwimmingPool')]")
    private WebElement table_SquirePropertyDetailSwimmingPool;

    public void setHowOrByWhomIsThisOccupied(String howOrByWhom) {
        waitUntilElementIsClickable(editbox_HowOrByWhomIsThisOccupied);
        editbox_HowOrByWhomIsThisOccupied.sendKeys(howOrByWhom);
    }

    public boolean isHowOrByWhomIsThisOccupiedExist() {
        return checkIfElementExists(editbox_HowOrByWhomIsThisOccupied, 5000);
    }

    @FindBy(xpath = "//input[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:OccupantIndicator-inputEl')]")
    private WebElement editbox_HowOrByWhomIsThisOccupied;

    public void setCoverageAPropertyTypeAttachedTo(String propertyType) {
        selectCoverageAPropertyTypeAttached().selectByVisibleTextPartial(propertyType);
    }

    public void setPoolFence(boolean truefalse) {
        radio_PeropertyDetailsPoolFence().select(truefalse);
    }

    private Guidewire8RadioButton radio_PeropertyDetailsPoolFence() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:PropertyFenced-containerEl')]/table");
    }

    public void clickRemovePropertyAdditionalInterest() {
        clickWhenClickable(button_PropertyAdditionalInterest_Remove);
    }

    @FindBy(xpath = "//a[contains(@id, ':AdditionalInterestLV_tb:Remove')]")
    private WebElement button_PropertyAdditionalInterest_Remove;

    public void clickAdditionalInterestByName(String name) {
        tableUtils.clickCellInTableByRowAndColumnName(table_propertyAdditionalInterest, tableUtils.getRowNumberInTableByText(table_propertyAdditionalInterest, name), "Name");
    }

    @FindBy(xpath = "//div[contains(@id, ':AdditionalInterestDetailsDV:AdditionalInterestLV')]")
    private WebElement table_propertyAdditionalInterest;
    @FindBy(xpath = "//span[contains(@id, ':HOPropertyAdditionalInsuredDV:AdditionalInsuredLV_tb:Add-btnInnerEl')]")
    private WebElement button_SquirePropertyDetailPropertyAdditionalInsured;

    public void addAddtionalInsured() {
        clickWhenClickable(button_SquirePropertyDetailPropertyAdditionalInsured);
    }

    public void setAddtionalInsuredName(String additionalInsured) {
        tableUtils.setValueForCellInsideTable(table_addtionalInsured, tableUtils.getNextAvailableLineInTable(table_addtionalInsured, "Name"), "Name", "FirstName", additionalInsured);
    }

    @FindBy(xpath = "//div[contains(@id, ':AdditionalInsuredLV')]")
    private WebElement table_addtionalInsured;

    public void removeAdditionalInsuredByName(String additionalInsured) {
        int row = tableUtils.getRowNumberInTableByText(table_addtionalInsured, additionalInsured);
        tableUtils.setCheckboxInTable(table_addtionalInsured, row, true);
        clickWhenClickable(button_AditionalInsuredRemove);
    }

    @FindBy(xpath = "//span[contains(@id, ':AdditionalInsuredLV_tb:Remove-btnEl')]")
    private WebElement button_AditionalInsuredRemove;

    public String getAdditionalInsuredNameByRowNumber(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_addtionalInsured, row, "Name");
    }

    public void AddExistingOwner() {
        clickWhenClickable(link_OwnerAddExisting);
        hoverOver(link_AddExistingPolicyMember);
        hoverOver(link_AddExistingPolicyMember_Contact);
        clickWhenClickable(link_AddExistingPolicyMember_Contact);
    }

    @FindBy(xpath = "//span[contains(@id, 'HOBuilding_FBMPopup:HOPropertyOccupantsFBMDV:DwellingOwnerLV_tb:AddContactsButton-btnEl')]")
    private WebElement link_OwnerAddExisting;
    @FindBy(xpath = "//span[contains(@id, 'HOBuilding_FBMPopup:HOPropertyOccupantsFBMDV:DwellingOwnerLV_tb:AddContactsButton:AddExistingContact')]")
    private WebElement link_AddExistingPolicyMember;

    @FindBy(xpath = "//span[contains(@id, ':AddExistingContact:0:ExistingDwellingOwner-textEl')]")
    private WebElement link_AddExistingPolicyMember_Contact;

    public boolean checkAdditionalInterestByName(String name) {
        return tableUtils.getRowNumberInTableByText(table_propertyAdditionalInterest, name) > 0;
    }

    public void setBuildingAdditionalInterests(boolean basicSearch, AdditionalInterest bldgAddInterest) throws GuidewireNavigationException {
        GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
        additionalInterests.fillOutAdditionalInterest(basicSearch, bldgAddInterest);
    }

    public void addExistingAdditionalInterest(String contactName) {
        
        clickWhenClickable(button_AdditionalInterests_AddExisting);
        
        hoverOverAndClick(link_AdditionalInterests_ExistingAdditionalInterest);
        
        hoverOverAndClick(link_AdditionalInterests_ExistingAdditionalInterestArrow);
        
        String xPath = "//span[contains(text(), '" + contactName + "')]/parent::a/parent::div";
        WebElement lienHolder = find(By.xpath(xPath));
        clickWhenClickable(lienHolder);
    }

    @FindBy(xpath = "//a[contains(@id, ':AdditionalInterestDetailsDV:AdditionalInterestLV_tb:AddContactsButton')]")
    private WebElement button_AdditionalInterests_AddExisting;
    @FindBy(xpath = "//a[contains(@id, ':AdditionalInterestDetailsDV:AdditionalInterestLV_tb:AddContactsButton:AddExistingContact-itemEl') or contains(@id, ':AddContactsButton:AddOtherContact-itemEl')]")
    private WebElement link_AdditionalInterests_ExistingAdditionalInterest;
    @FindBy(xpath = "//div[contains(@id, ':AdditionalInterestDetailsDV:AdditionalInterestLV_tb:AddContactsButton:AddExistingContact-arrowEl')or contains(@id, ':AddContactsButton:AddOtherContact-arrowEl')]")
    private WebElement link_AdditionalInterests_ExistingAdditionalInterestArrow;

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

    @FindBy(xpath = "//textarea [contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:HORiskAndCategoryInputSet:ReinsuranceCategoryDesc-inputEl')]")
    private WebElement text_CategoryCodeOverrideReason;
    @FindBy(xpath = "//input[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:HORiskAndCategoryInputSet:ReinsureanceOverride-inputE') or contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:HORiskAndCategoryInputSet:ReinsureanceOverride-inputE1') ]")
    private WebElement check_OverrideCategoryCode;
    @FindBy(xpath = "//input[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:HORiskAndCategoryInputSet:riskVal-inputEl')]")
    private WebElement input_Risk;
    @FindBy(xpath = "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:HORiskAndCategoryInputSet:ReinsuranceCategoryCode-inputEl') or contains(@id, 'HODwellingDetailsHOEDV:HORiskAndCategoryInputSet:ReinsuranceCategoryCode-inputEl')]")
    private WebElement text_readOnlyCategoryCode;
    @FindBy(xpath = "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:HORiskAndCategoryInputSet:ReinsureanceOverrideReadonly-inputEl')]")
    private WebElement text_readOnlyOverrideCategoryCode;
    @FindBy(xpath = "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:HORiskAndCategoryInputSet:riskVal-inputEl') or contains(@id, 'HODwellingDetailsHOEDV:HORiskAndCategoryInputSet:riskVal-inputEl')]")
    private WebElement text_readOnlyRisk;

    private Guidewire8Select selectCategoryCode() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:HORiskAndCategoryInputSet:ReinsuranceCategoryCode-triggerWrap')]");
    }

    public boolean checkWoodFireplaceOrWoodStoveQuestionExists() {
        return checkIfElementExists("//label[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:FireWoodStove-labelEl')]", 2);
    }

    public boolean getWoodFireplaceRadio(boolean trueFalse) {
        return radio_fireplace().isSelected(trueFalse);
    }

    public boolean checkDatelastcleanedSubQuestioExists() {
        return checkIfElementExists("//label[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:DateLastCleaned-labelEl')]", 2);
    }

    public boolean checkAreChimneysCleanedRegularlySubQuestionExists() {
        return checkIfElementExists("//label[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:ChimneysCleanedReg-labelEl')]", 2);
    }


}























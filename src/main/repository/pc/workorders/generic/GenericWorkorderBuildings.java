package repository.pc.workorders.generic;

import com.idfbins.enums.OkCancel;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.Building.*;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.errorhandling.ErrorHandlingBuildings;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.Building;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;
import repository.pc.sidemenu.SideMenuPC;

import java.util.ArrayList;
import java.util.Date;

public class GenericWorkorderBuildings extends GenericWorkorder {

    private WebDriver driver;
    private TableUtils tableUtils;

    public GenericWorkorderBuildings(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//span[contains(@id, ':Update-btnEl')]")
    private WebElement buildingsOKButton;

    @FindBy(xpath = "//*[@id='BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsCardTab-btnEl']")
    public WebElement link_BuildingDetails;

    @FindBy(xpath = "//*[@id='BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_AdditionalCoveragesCardTab-btnEl']")
    public WebElement link_AdditionalCoverages;

    @FindBy(xpath = "//div[contains(@id, 'BOPBuildingsScreen:BOPBuildingsCV:BOPLocationBuildingsPanelSet:BOPLocationsLV')]")
    public WebElement table_SubmissionBuildingsLocations;

    @FindBy(xpath = "//div[contains(@id, 'BOPBuildingsScreen:BOPBuildingsCV:BOPLocationBuildingsPanelSet:BOPLocationBuildingsLV')]")
    public WebElement table_SubmissionBuildings;

    @FindBy(xpath = "//span[contains(@id, ':BOPBuildingsCV:BOPLocationBuildingsPanelSet:BOPLocationBuildingsLV_tb:Add-btnEl')]")
    public WebElement Add;

    @FindBy(xpath = "//*[contains(@id, 'BOPBuildingsCV:BOPLocationBuildingsPanelSet:BOPLocationBuildingsLV_tb:Remove-btnEl')]")
    public WebElement Remove;

    @FindBy(xpath = "//*[contains(@id, 'BOPClassCodeSearchPopup:BOPClassCodeSearchScreen:BOPClassCodeSearchDV:Classification-inputEl')] | //input[contains(@id, 'ClassCodeSearchDV:Classification')]")
    public WebElement Classification;

    @FindBy(xpath = "//*[contains(@id, ':Code-inputEl')]")
    public WebElement Code;

    @FindBy(xpath = "//*[contains(@id, ':BOPBuilding_DetailsDV:BOPBuildingClassCodeRange:SelectBOPBuildingClassCodeRange') or contains(@id, 'ClassCode:SelectClassCode')]")
    public WebElement SelectBOPBuildingClassCodeRange;

    @FindBy(xpath = "//*[contains(@id, ':SearchLinksInputSet:Search')]")
    public WebElement Search_link;

    @FindBy(xpath = "//*[@id='BOPClassCodeSearchPopup:BOPClassCodeSearchScreen:BOPClassCodeSearchDV:SearchAndResetInputSet:SearchLinksInputSet:Reset']")
    public WebElement Reset_link;

    @FindBy(xpath = "//*[contains(@id, ':ClassCodeSearchResultsLV:0:_Select')]")
    public WebElement Select_link;

    @FindBy(xpath = "//*[@id='BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:Description-inputEl']")
    public WebElement Description;

    @FindBy(xpath = "//input[@id='BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:OtherDesc-inputEl']")
    public WebElement otherDescription;

    public Guidewire8Select select_SqFtPercentOccupied() {
        return new Guidewire8Select(driver, "//*[@id='BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:PercentOccupied-triggerWrap']");
    }

    public Guidewire8Select select_InterestType() {
        return new Guidewire8Select(driver, "//*[@id='BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:InterestType-triggerWrap']");
    }

    @FindBy(xpath = "//*[@id='BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:BOPBuildingClassCodeRange-inputEl']")
    public WebElement BOPBuildingClassCodeRange;

    public Guidewire8Checkbox checkBox_BOPBldgCoverage() {
        return new Guidewire8Checkbox(driver, "//*[contains(@id,'-legendTitle') and contains(.,'Building')]/preceding-sibling::table");
    }

    @FindBy(xpath = "//*[@id='BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:0:CoverageInputSet:CovPatternInputGroup:BOPBldgCovLimit-inputEl']")
    public WebElement BOPBldgCovLimit;

    @FindBy(xpath = "//*[@id=\"BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:BasisAmount-inputEl\"]")
    public WebElement BOPBldgBasisAmount;

    public Guidewire8Checkbox checkBox_BOPBldgPersonalCoverage() {
        return new Guidewire8Checkbox(driver, "//*[contains(@id,'-legendTitle') and contains(.,'Business Personal Property')]/preceding-sibling::table");
    }

    @FindBy(xpath = "//*[@id='BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:1:CoverageInputSet:CovPatternInputGroup:BOPBldgPerLimit-inputEl']")
    public WebElement BOPBldgPerLimit;

    //@FindBy(xpath = "//*[@id='BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:FireExtCnt-inputEl']")
    @FindBy(xpath = "//*[contains(@id, ':FireExtCnt-inputEl')]")
    public WebElement FireExtCnt;

    @FindBy(xpath = "//*[@id='BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:ExposureFlamChemYes']")
    public WebElement ExposureFlamChemYes;

    @FindBy(xpath = "//input[contains(@id, ':YearBuilt-inputEl')]")
    public WebElement YearBuilt;

    @FindBy(xpath = "//*[contains(@id, ':NumStories-inputEl')]")
    public WebElement NumOfStories;

    @FindBy(xpath = "//*[contains(@id, ':NoOfBasements-inputEl') or contains(@id, ':NumBasements-inputEl')]")
    public WebElement NumOfBasements;

    @FindBy(xpath = "//*[contains(@id, ':TotalArea-inputEl')]")
    public WebElement TotalArea;

    //@FindBy(xpath = "//*[@id='BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:PhotoYear-inputEl']")
    @FindBy(xpath = "//*[contains(@id, ':PhotoYear-inputEl')]")
    public WebElement PhotoYear;

    public void setPhotoYear(int year) {
        setText(PhotoYear, String.valueOf(year));
    }

    //@FindBy(xpath = "//*[@id='BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:CostEstYear-inputEl']")
    @FindBy(xpath = "//*[contains(@id, ':CostEstYear-inputEl')]")
    public WebElement CostEstYear;

    public void setCostEstimator(int year) {
        setText(CostEstYear, String.valueOf(year));
    }

    @FindBy(xpath = "//*[contains(@id, ':LastUpdateRoofing-inputEl')]")
    public WebElement LastUpdateRoofing;

    @FindBy(xpath = "//*[contains(@id, ':LastUpdateWiring-inputEl')]")
    public WebElement LastUpdateWiring;

    @FindBy(xpath = "//*[contains(@id, ':WiringTypeDesc-inputEl')]")
    public WebElement WiringTypeDesc;

    @FindBy(xpath = "//*[contains(@id, ':WiringUpdDesc-inputEl') or contains(@id, ':WiringUpdateDesc-inputEl')]")
    public WebElement WiringUpdDesc;

    @FindBy(xpath = "//*[contains(@id, ':LastUpdateHeating-inputEl')]")
    public WebElement LastUpdateHeating;

    @FindBy(xpath = "//*[contains(@id, ':HeatingUpdDesc-inputEl') or contains(@id, ':HeatingUpdatedDesc-inputEl')]")
    public WebElement HeatingUpdDesc;

    public Guidewire8RadioButton radio_WoodBurningStove() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, 'BOPBuilding_DetailsDV:BOPBuildingCharacteristicsInputSet:WoodBurnStove') or contains(@id, ':BOPBuilding_DetailsDV:WoodBurnStove')]");
    }

    @FindBy(xpath = "//*[contains(@id, ':LastUpdatePlumbing-inputEl')]")
    public WebElement LastUpdatePlumbing;

    @FindBy(xpath = "//*[contains(@id, ':PlumbingUpdDesc-inputEl') or contains(@id, ':PlumbingUpdatedDesc-inputEl')]")
    public WebElement PlumbingUpdDesc;

    @FindBy(xpath = "//*[contains(@id, ':ExistDamageDesc-inputEl')]")
    public WebElement ExistDamageDesc;

    @FindBy(xpath = "//*[contains(@id, ':AlarmCertificate-inputEl')]")
    public WebElement AlarmCertificate;

    @FindBy(xpath = "//*[contains(@id, ':PolicyHolderwithin100ft-inputEl')]")
    public WebElement PolicyHolderwithin100ft;

    @FindBy(xpath = "//*[contains(@id, ':PolicyNumberforwithin100ft-inputEl')]")
    public WebElement PolicyNumberforwithin100ft;

    public Guidewire8Checkbox checkBox_Potholes() {
        return new Guidewire8Checkbox(driver, "//*[@id = 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:Potholes']");
    }

    public Guidewire8Checkbox checkBox_DeepCracks() {
        return new Guidewire8Checkbox(driver, "//*[@id = 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:DeepCracks']");
    }

    public Guidewire8Checkbox checkBox_Surfaces() {
        return new Guidewire8Checkbox(driver, "//*[@id = 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:Surfaces']");
    }

    public Guidewire8Checkbox checkBox_Hazards() {
        return new Guidewire8Checkbox(driver, "//*[@id = 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:Hazards']");
    }

    public Guidewire8Checkbox checkBox_WellLit() {
        return new Guidewire8Checkbox(driver, "//*[@id = 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:WellLit']");
    }

    public Guidewire8Checkbox checkBox_NoneAbove() {
        return new Guidewire8Checkbox(driver, "//*[@id = 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:NoneAbove']");
    }

    public Guidewire8Checkbox checkBox_HandRail() {
        return new Guidewire8Checkbox(driver, "//*[@id = 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:HandRail']");
    }

    public Guidewire8Checkbox checkBox_NonSlipSurface() {
        return new Guidewire8Checkbox(driver, "//*[@id = 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:NonSlipSurface']");
    }

    public Guidewire8Checkbox checkBox_FloorMat() {
        return new Guidewire8Checkbox(driver, "//*[@id = 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:FloorMat']");
    }

    public Guidewire8Checkbox checkBox_InteriorStairLight() {
        return new Guidewire8Checkbox(driver, "//*[@id = 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:InteriorStairLight']");
    }

    public Guidewire8Checkbox checkBox_Other() {
        return new Guidewire8Checkbox(driver, "//*[@id = 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:Other']");
    }

    public Guidewire8Checkbox checkBox_SafetyNoneAbove() {
        return new Guidewire8Checkbox(driver, "//*[@id='BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:SafetyNoneAbove']");
    }

    public Guidewire8RadioButton radio_OwnerOfBuilding() {
        return new Guidewire8RadioButton(driver, "//table[@id='BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:OwnerOfBuilding']");
    }

    public Guidewire8RadioButton radio_Sprinklered() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, ':PercentSprinklered')]");
    }

    public Guidewire8RadioButton radio_FlatRoof() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, ':FlatRoof')]");
    }

    public Guidewire8RadioButton radio_ExistingDamage() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, ':ExistingDamage')]/table");
    }

    public Guidewire8RadioButton radio_InsuredProperty100ft() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, ':Propertywithin100ft')]/table");
    }

    public Guidewire8RadioButton radio_ExitsMarked() {
        return new Guidewire8RadioButton(driver, "//table[@id='BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:ExitMarked']");
    }

    public Guidewire8RadioButton radio_ExposureToFlammableChemicals() {
        return new Guidewire8RadioButton(driver, "//table[@id='BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:ExposureFlamChem']");
    }

    public Guidewire8RadioButton radio_PermanentFoundation() {
        return new Guidewire8RadioButton(driver, "//table[@id='BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:BOPBuildingCharacteristicsInputSet:PermFoundation']");
    }

    // COMBO

    public Guidewire8Select select_PercentOccupied() {
        return new Guidewire8Select(driver, "//*[@id='BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:PercentOccupied-triggerWrap']");
    }

    public Guidewire8Select select_AreaLeased() {
        return new Guidewire8Select(driver, "//*[@id='BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:AreaLeased-triggerWrap']");
    }

    public Guidewire8Select select_BOPBldgCovValuationMethod() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Building')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Valuation Method')]/parent::td/following-sibling::td/table");
    }

    public Guidewire8Select select_BOPBldgCovCauseOfLoss() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Building')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Cause Of Loss')]/parent::td/following-sibling::td/table");
    }

    public Guidewire8Select select_BOPBldgPerPropValuationMethod() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Business Personal Property')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Valuation Method')]/parent::td/following-sibling::td/table");
    }

    public Guidewire8Select select_BOPBldgPerCauseOfLoss() {
        return new Guidewire8Select(driver, "//*[@id='BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:1:CoverageInputSet:CovPatternInputGroup:BOPBldgPerCauseOfLoss-triggerWrap']");
    }

    public Guidewire8Select select_ExtHouseKeepMaint() {
        return new Guidewire8Select(driver, "//*[@id='BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:ExtHouseKeepMaint-triggerWrap']");
    }

    public Guidewire8Select select_IntHouseKeepMaint() {
        return new Guidewire8Select(driver, "//*[@id='BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:IntHouseKeepMaint-triggerWrap']");
    }

    public Guidewire8Select select_ConstructionType() {
        return new Guidewire8Select(driver, "//*[contains(@id, ':ConstructionType-triggerWrap')]");
    }

    public Guidewire8Select select_FlatRoofMaintSchd() {
        return new Guidewire8Select(driver, "//*[contains(@id, ':FlatRoofMaintSchd-triggerWrap')]");
    }

    public Guidewire8Select select_RoofType() {
        return new Guidewire8Select(driver, "//*[contains(@id, ':RoofType-triggerWrap')]");
    }

    public Guidewire8Select select_RoofCondition() {
        return new Guidewire8Select(driver, "//*[contains(@id, ':RoofCondition-triggerWrap')]");
    }

    public Guidewire8Select select_WireType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':WireType-triggerWrap') or contains(@id, ':WiringType-triggerWrap')]");
    }

    public Guidewire8Select select_BoxType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':BoxType-triggerWrap')]");
    }

    public Guidewire8Select select_BuildingAlarmSystemType() {
        return new Guidewire8Select(driver, "//table[@id='BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:BuildingAlarmSystemType']");
    }

    public Guidewire8Select select_BuildingAlarmType() {
        return new Guidewire8Select(driver, "//table[@id='BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:BuildingAlarmType']");
    }

    public Guidewire8Select select_AlarmGrade() {
        return new Guidewire8Select(driver, "//table[@id='BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:AlarmGrade']");
    }

    @FindBy(xpath = "//a[contains(@id,'BOPBuildingPopup:__crumb__')]")
    private WebElement link_ReturnToBuildings;

    @FindBy(xpath = "//div[@id='messagebox-1001']")
    private WebElement popup_AreYouSure;

    @FindBy(xpath = "//div[contains(@id, ':ISRBFireProtectionClassAuto-inputEl')]")
    public WebElement protectionClassCode;

    public Guidewire8Select select_ManualProtectionClass() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':ISRBFireProtectionClassManual-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id,'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:PolicyHolderwithin100ft-inputEl')]")
    private WebElement editbox_InsuredWithinHundredFtPolicyHolderName;

    @FindBy(xpath = "//input[contains(@id,'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:PolicyNumberforwithin100ft-inputEl')]")
    private WebElement editbox_InsuredWithinHundredFtPolicyNumber;

    /////////////////////////////////////////
    // Helper Methods for the Above Elements//
    /////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // Main Helper Methods (Designed to do the 'Heavy Lifting')//
    ////////////////////////////////////////////////////////////


    public void fillOutBOPBuildings_QQ(GeneratePolicy generatePolicy) throws Exception {
    	new repository.pc.sidemenu.SideMenuPC(generatePolicy.getWebDriver()).clickSideMenuBuildings();
        for(PolicyLocation location : generatePolicy.busOwnLine.locationList) {
        	for(PolicyLocationBuilding building : location.getBuildingList()) {
        		if(building.getNumber() == -1) {
        			clickBuildingsLocationsRow(location.getNumber());
        			clickAddBuilding();
        		} else {
        			clickBuildingsBuildingEdit(building.getNumber());
        		}
                setDescription(building.getUsageDescription());
                setInsuredOwner(building.isNamedInsuredOwner());

                setBuilidngClassCodeAndQuestions(building);
                waitForPostBack();
                setInterestType(building.getOccupancyNamedInsuredInterest());
                setBuildingLimit(building.getBuildingLimit());
                setBuildingPersonalPropertyLimit(building.getBppLimit());
                selectConstructionType(building.getConstructionType());
                setYearBuilt(building.getYearBuilt());
                setSprinklered(building.isSprinklered());

                if (!generatePolicy.busOwnLine.getCurrentPolicyType().equals(GeneratePolicyType.QuickQuote)) {
                    setFullAppQuestions(building);
                }

                setProtectionClassCode(building);

                if (building.getProtectionClassCode() >= 9) {
                    setWoodBurningStove(building.isWoodBurningStove());
                }
                setBuilildingAdditionalInterest(false, building);
                clickAdditionalCoverages();

                //set equipment breakdown coverage
                checkAdditionalCoveragesEquipmentBreakdownCoverage(building.getAdditionalCoveragesStuff().getEquipmentBreakDown_IDBP_31_1002());

                clickOK();

                building.setNumber(getBuildingsBuildingNumberCellLast(building.getUsageDescription()));
        	}
        }

    }


    public void updateBuilding(PolicyLocationBuilding building) throws Exception {
        new repository.pc.sidemenu.SideMenuPC(driver).clickSideMenuBuildings();
        clickBuildingsBuildingEdit(building.getNumber());
        setDescription(building.getUsageDescription());
        setInsuredOwner(building.isNamedInsuredOwner());

        setBuilidngClassCodeAndQuestions(building);
        waitForPostBack();
        setInterestType(building.getOccupancyNamedInsuredInterest());
        setBuildingLimit(building.getBuildingLimit());
        setBuildingPersonalPropertyLimit(building.getBppLimit());
        selectConstructionType(building.getConstructionType());
        setYearBuilt(building.getYearBuilt());
        setSprinklered(building.isSprinklered());

        setFullAppQuestions(building);

        setProtectionClassCode(building);

        if (building.getProtectionClassCode() >= 9) {
            setWoodBurningStove(building.isWoodBurningStove());
        }
        setBuilildingAdditionalInterest(false, building);
        clickAdditionalCoverages();

        //set equipment breakdown coverage
        checkAdditionalCoveragesEquipmentBreakdownCoverage(building.getAdditionalCoveragesStuff().getEquipmentBreakDown_IDBP_31_1002());
        clickOK();
    }


    public WebElement getSubmissionBuildingsLocationsTable() {
        return table_SubmissionBuildingsLocations;
    }


    public WebElement getSubmissionBuildingsTable() {
        return table_SubmissionBuildings;
    }


    public void addBuildingsPerLocation(boolean basicSearch, PolicyLocation loc, boolean quickQuote, boolean retry) throws Exception {
        repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
        sideMenu.clickSideMenuBuildings();
        clickBuildingsLocationsRow(loc.getNumber());
        for (PolicyLocationBuilding bldg : loc.getBuildingList()) {
            clickAddBuilding();
            setDescription(bldg.getUsageDescription());
            setInsuredOwner(bldg.isNamedInsuredOwner());

            setBuilidngClassCodeAndQuestions(bldg);
            waitForPostBack();
            setInterestType(bldg.getOccupancyNamedInsuredInterest());
            setBuildingLimit(bldg.getBuildingLimit());
            setBuildingPersonalPropertyLimit(bldg.getBppLimit());
            selectConstructionType(bldg.getConstructionType());
            setYearBuilt(bldg.getYearBuilt());
            setSprinklered(bldg.isSprinklered());

            if (!quickQuote) {
                setFullAppQuestions(bldg);
            }

            setProtectionClassCode(bldg);

            if (bldg.getProtectionClassCode() >= 9) {
                setWoodBurningStove(bldg.isWoodBurningStove());
            }
            setBuilildingAdditionalInterest(basicSearch, bldg);
            clickAdditionalCoverages();

            //set equipment breakdown coverage
            checkAdditionalCoveragesEquipmentBreakdownCoverage(bldg.getAdditionalCoveragesStuff().getEquipmentBreakDown_IDBP_31_1002());

            clickOK();

            bldg.setNumber(getBuildingsBuildingNumberCellLast(bldg.getUsageDescription()));
        }
        clickBack();
    }


    public void checkAdditionalCoveragesEquipmentBreakdownCoverage(boolean trueFalseChecked) {
        if (trueFalseChecked) {
            if (!finds(By.xpath("//div[contains(text(), 'Equipment Breakdown Enhancement Endorsement IDBP 31 1002')]/preceding-sibling::table")).isEmpty()) {
                if (!find(By.xpath("//div[contains(text(), 'Equipment Breakdown Enhancement Endorsement IDBP 31 1002')]/preceding-sibling::table")).getAttribute("class").contains("cb-checked")) {
                    find(By.xpath("//div[contains(text(), 'Equipment Breakdown Enhancement Endorsement IDBP 31 1002')]/preceding-sibling::table/tbody/tr/child::td[2]/div/input")).click();
                } else {
                    // do nothing
                }
            }
        } else {
            if (!finds(By.xpath("//div[contains(text(), 'Equipment Breakdown Enhancement Endorsement IDBP 31 1002')]/preceding-sibling::table")).isEmpty()) {
                if (find(By.xpath("//div[contains(text(), 'Equipment Breakdown Enhancement Endorsement IDBP 31 1002')]/preceding-sibling::table")).getAttribute("class").contains("cb-checked")) {
                    find(By.xpath("//div[contains(text(), 'Equipment Breakdown Enhancement Endorsement IDBP 31 1002')]/preceding-sibling::table/tbody/tr/child::td[2]/div/input")).click();
                } else {
                    // do nothing
                }
            }
        }
    }


    private void setProtectionClassCode(PolicyLocationBuilding bldg) {
        String classcode = protectionClassCode.getText();
        try {
            if (protectionClassCode.getText() == null || protectionClassCode.getText().equals("")) {
                setManualProtectionClassCode(bldg.getProtectionClassCode());
            } else {
                bldg.setProtectionClassCode(Integer.valueOf(classcode));
            }
        } catch (NumberFormatException e) {
            setManualProtectionClassCode(bldg.getProtectionClassCode());
        }

    }


    public void setManualProtectionClassCode(int protectionClassCode) {
        Guidewire8Select mySelect = select_ManualProtectionClass();
        mySelect.selectByVisibleText(String.valueOf(protectionClassCode));

    }


    private void setBuilildingAdditionalInterest(boolean basicSearch, PolicyLocationBuilding bldg) throws Exception {
        if (bldg.getAdditionalInterestList().size() > 0) {
            for (AdditionalInterest bldgAddInterest : bldg.getAdditionalInterestList()) {
                setBuildingAdditionalInterests(basicSearch, bldgAddInterest, true);
            }
        }
    }

    private void resetBuildingsPage() {
        clickCancel();
        selectOKOrCancelFromPopup(OkCancel.OK);
    }

    private void setBuilidngClassCodeAndQuestions(PolicyLocationBuilding bldg) {
        if (bldg.getClassClassification() != "") {
            selectFirstBuildingCodeResult(bldg.getClassClassification());
        } else if (bldg.getClassCode() != "") {
            selectFirstBuildingCodeResultClassCode(bldg.getClassCode());
        } else {
            Assert.fail("The class code for the buildings page was set to null for both the code and classification cells. One of those options must be specified.");
        }

        repository.pc.workorders.generic.GenericWorkorderBuildingsClassCodes buildings = new GenericWorkorderBuildingsClassCodes(getDriver());
        buildings.handleBuildingClassCodeQuestions(bldg);
    }


    public void setBuildingAdditionalInterests(boolean basicSearch, AdditionalInterest bldgAddInterest, Boolean retry) throws Exception {
//		try {


        repository.pc.workorders.generic.GenericWorkorderAdditionalInterests additionalInterests = new repository.pc.workorders.generic.GenericWorkorderAdditionalInterests(driver);
        additionalInterests.fillOutAdditionalInterest(basicSearch, bldgAddInterest);

//		} catch (GuidewireException e) {
//			systemOut("GuidewireException caught");
//			Assert.fail(e.getMessage());
//		} catch (Exception e) {
//			systemOut("Exception Caught");
//			if (retry) {
//				systemOut("Unable to add Additional Interests to building.\nAttempting to retry.");
//				resetBuildingsAI();
//				setBuildingAdditionalInterests(bldgAddInterest, false);
//			} else {
//				e.printStackTrace();
//				Assert.fail(e.getMessage());
//				throw new GuidewirePolicyCenterException(getCurrentURL(), "Unable to add Additional Intrest. Sorry :(");
//			}
//		}
    }


    @SuppressWarnings("unused")
    private void resetBuildingsAI() {
        systemOut("Running resetBuildingsAI");

        if (finds(By.xpath("//a[contains(text(), 'Return to Search Address Book')]")).size() > 0) {
            find(By.xpath("//a[contains(text(), 'Return to Search Address Book')]")).click();
            selectOKOrCancelFromPopup(OkCancel.OK);
        }

        if (finds(By.xpath("//a[contains(text(), 'Return to Details')]")).size() > 0) {
            find(By.xpath("//a[contains(text(), 'Return to Details')]")).click();
            selectOKOrCancelFromPopup(OkCancel.OK);
        }
    }

    public void completeEachBuildingsPagePerLocation(PolicyLocation loc, boolean retry) {
        clickBuildingsLocationsRow(loc.getNumber());
        for (PolicyLocationBuilding bldg : loc.getBuildingList()) {
            clickBuildingsBuildingEdit(bldg.getNumber());
            setFullAppQuestions(bldg);

            setBuildingAdditionalCoverages(bldg);
            clickOK();
            clickGenericWorkorderSaveDraft();// to mainly run error handling if needed
        }
    }

    @SuppressWarnings("unused")
    private void restBuildingsPage() {
        if (finds(By.xpath("//span[contains(text(), 'Details') and contains(@class, 'g-title')]")).size() > 0) {
            clickCancel();
            selectOKOrCancelFromPopup(OkCancel.OK);
        }
    }


    public void setFullAppQuestions(PolicyLocationBuilding bldg) {
        setInteriorHousekeeping(bldg.getInteriorHouseKeepingAndMaintenance());
//        setManualProtectionClassCode(bldg.getProtectionClassCode());
        setExteriorHousekeeping(bldg.getExteriorHouseKeepingAndMaintenance());
        setParkingLotSidewalkCharacteristics(bldg.getParkingLotCharacteristicsList());
        setSafetyEquipment(bldg.getSafetyEquipmentList());
        setExitsMarked(bldg.isExitsProperlyMarked());
        setNumberOfFireExtinguishers(bldg.getNumFireExtinguishers());
        setExposureToFlammables(bldg.isExposureToFlammablesChemicals());
        setNumOfStories(bldg.getNumStories());
        setNumOfBasements(bldg.getNumBasements());
        setTotalArea(bldg.getTotalArea());
        setPhotoYear(bldg.getPhotoYear());
        setCostEstimator(bldg.getCostEstimatorYear());
        setRoofingType(bldg.getRoofingType());
        setFlatRoof(bldg.isFlatRoof());
        setRoofCondition(bldg.getRoofCondition());
        setWiringType(bldg.getWiringType());
        setBoxType(bldg.getBoxType());
        int yearBuilt = bldg.getYearBuilt();
        int currentSystemYear = DateUtils.getCenterDateAsInt(driver, ApplicationOrCenter.PolicyCenter, "yyyy");
        // If statement using magic number 10.... TODO: create a descriptive VAR
        if (yearBuilt < currentSystemYear - 10) {
            setLastUpdateRoofing(yearBuilt);
            setLastUpdateWiring(yearBuilt);
            setWiringUpdateDescription(bldg.getWiringUpdateDesc());
            setLastUpdateHeating(yearBuilt);
            setHeatingUpdateDescription(bldg.getHeatingUpdateDesc());
            setLastUpdatePlumbing(yearBuilt);
            setPlumbingUpdateDescription(bldg.getPlumbingUpdateDesc());
        }
        setExistingDamage(bldg.isExistingDamage());
        setInsuredPropertyWithin100Feet(bldg.isInsuredPropertyWithin100Ft());
    }

    // THIS METHOD IS ONLY FOR POLICY CHANGES AND IS NOT TO BE USED WHEN
    // GENERATING NEW POLICIES!!!

    public PolicyLocationBuilding addBuildingOnLocation(boolean basicSearch, int locationNumber, PolicyLocationBuilding bldg) throws Exception {
        repository.pc.sidemenu.SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuBuildings();

        clickBuildingsLocationsRow(locationNumber);

        building:
        try {
            clickAddBuilding();

            setDescription(bldg.getUsageDescription());
            setInsuredOwner(bldg.isNamedInsuredOwner());
            setBuilidngClassCodeAndQuestions(bldg);
            setInterestType(bldg.getOccupancyNamedInsuredInterest());
            setBuildingLimitCoverage(bldg.isBuildingLimitCoverage());
            setBuildingLimit(bldg.getBuildingLimit());
            setBuildingPersonalPropertyLimitCoverage(bldg.isBppLimitCoverage());
            setBuildingPersonalPropertyLimit(bldg.getBppLimit());
            selectConstructionType(bldg.getConstructionType());
            setYearBuilt(bldg.getYearBuilt());
            setSprinklered(bldg.isSprinklered());
            setFullAppQuestions(bldg);

            setBuilildingAdditionalInterest(basicSearch, bldg);
            clickAdditionalCoverages();
            clickOK();

            bldg.setNumber(getBuildingsBuildingNumberCellLast(bldg.getUsageDescription()));

        } catch (StaleElementReferenceException e) {

            System.out.println("StaleElementReferenceException THROWN Buildings Page");
            e.printStackTrace();

            resetBuildingsPage();
            System.out.println("Retrying Builidngs Page");
            break building;
        } catch (NoSuchElementException e) {
            System.out.println("NoSuchElementException THROWN Buildings Page: ");
            e.printStackTrace();
            if (finds(By.xpath("//div[contains(@id, '_msgs')]/div[1] | //span[contains(@id, '_msgs_msgs')]/div[1]")).size() > 0) {
                System.out.println("Running Error handling.");
                ErrorHandlingBuildings errorHandlingBldg = new ErrorHandlingBuildings(getDriver());
                errorHandlingBldg.errorHandlingBuildingsPage(1000, bldg);
            } else {
                resetBuildingsPage();
                System.out.println("Retrying Builidngs Page");
                break building;
            }
        } catch (ElementNotVisibleException e) {
            System.out.println("ElementNotVisibleException THROWN Buildings Page");
            e.printStackTrace();
            System.out.println("Running Error handling.");
            ErrorHandlingBuildings errorHandlingBldg = new ErrorHandlingBuildings(getDriver());
            errorHandlingBldg.errorHandlingBuildingsPage(1000, bldg);
        } catch (TimeoutException e) {
            System.out.println("TimeoutException THROWN Buildings Page");
            e.printStackTrace();
            resetBuildingsPage();
            break building;
        } catch (GuidewireException e) {
            System.out.println("Caught GuidewireException");
            throw e;
        } catch (Exception e) {
            System.out.println("caught Exception");
            e.printStackTrace();
            Assert.fail("Unknown failure on Buildings Page.");
        }
        return bldg;
    }


    public void addAdditionalInterest(boolean basicSearch, AdditionalInterest bldgAddInterest) throws Exception {
        setBuildingAdditionalInterests(basicSearch, bldgAddInterest, true);
    }


    public void setBuildingAdditionalCoverages(PolicyLocationBuilding bldg) {
        clickAdditionalCoverages();
        repository.pc.workorders.generic.GenericWorkorderBuildingAdditionalCoverages bldgAddCoverages = new GenericWorkorderBuildingAdditionalCoverages(getDriver());
        if (bldg.getAdditionalCoveragesStuff().isBusinessIncomeOrdinaryPayrollCoverage()) {
            bldgAddCoverages.setBusinessIncomeOrdinaryPayrollCoverage(bldg);
        }

        if (bldg.getAdditionalCoveragesStuff().isDiscretionaryPayrollExpenseCoverage()) {
            bldgAddCoverages.setDiscretionaryPayrollExpenseCoverage(bldg);
        }

        if (bldg.getAdditionalCoveragesStuff().isLossOfRentalValueLandlordAsDesignatedPayeeCoverage()) {
            bldgAddCoverages.setLossOfRentalValueLandlordAsDesignatedPayeeCoverage(bldg);
        }

        if (bldg.getAdditionalCoveragesStuff().isAccountsReceivableOptionalCoverage()) {
            bldgAddCoverages.setAccountsReceivableOptionalCoverage(bldg);
        }

        if (bldg.getAdditionalCoveragesStuff().isCondoCommercialUnitOwnersOptionalCoverage()) {
            bldgAddCoverages.setCondoCommercialUnitOwnersOptionalCoverage(bldg);
        }

        if (bldg.getAdditionalCoveragesStuff().isFoodContaminationCoverage()) {
            bldgAddCoverages.setFoodContaminationCoverage(bldg);
        }

        if (bldg.getAdditionalCoveragesStuff().isOrdinanceOrLawCoverage()) {
            bldgAddCoverages.setOrdinanceOrLawCoverage(bldg);
        }

        if (bldg.getAdditionalCoveragesStuff().isOutdoorPropertyCoverage()) {
            bldgAddCoverages.setOutdoorPropertyCoverage(bldg);
        }

        if (bldg.getAdditionalCoveragesStuff().isSpoilageCoverage()) {
            bldgAddCoverages.setSpoilageCoverage(bldg);
        }

        if (bldg.getAdditionalCoveragesStuff().isValuablePapersOptionalCoverage()) {
            bldgAddCoverages.setValuablePapersOptionalCoverage(bldg);
        }

        if (bldg.getAdditionalCoveragesStuff().isUtilitiesDirectDamageCoverage()) {
            bldgAddCoverages.setUtilitiesDirectDamageCoverage(bldg);
        }

        if (bldg.getAdditionalCoveragesStuff().isUtilitiesTimeElementCoverage()) {
            bldgAddCoverages.setUtilitiesTimeElementCoverage(bldg);
        }
    }


    public boolean removeBuildingByBldgNumber(int bldgNum) {
        String buildingNumberGridColumn = tableUtils.getGridColumnFromTable(table_SubmissionBuildings, "Bldg. #");
        clickWhenClickable(table_SubmissionBuildings.findElement(By.xpath(".//tr/td[contains(@class,'" + buildingNumberGridColumn + "') and contains(.,'" + bldgNum + "')]/parent::tr/td[contains(@class,'-checkcolumn')]")));
        if (checkIfElementExists(Remove, 1000)) {
            Remove.click();
            selectOKOrCancelFromPopup(OkCancel.OK);
            return true;
        } else {
            return false;
        }
    }


    public void removeBuildingByBldgDescription(String buildingDescription) {
        String buildingDescriptionGridColumn = tableUtils.getGridColumnFromTable(table_SubmissionBuildings, "Description");
        clickWhenClickable(table_SubmissionBuildings.findElement(By.xpath(".//tr/td[contains(@class,'" + buildingDescriptionGridColumn + "') and contains(.,'" + buildingDescription + "')]/parent::tr/td[contains(@class,'-checkcolumn')]")));
        Remove.click();
        selectOKOrCancelFromPopup(OkCancel.OK);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Supporting Helper Methods (Designed to Carry Out Minimal Functions to Support Above Methods)//
    ////////////////////////////////////////////////////////////////////////////////////////////////


    public void clickCancel() {
        super.clickCancel();
    }


    public void clickBuildingsBuildingEdit(int bldgNum) {
        int tableRowNumber = tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(table_SubmissionBuildings, "Bldg. #", String.valueOf(bldgNum)));
        tableUtils.clickLinkInSpecficRowInTable(table_SubmissionBuildings, tableRowNumber);
    }


    public void setParkingLotSidewalkCharacteristics(ArrayList<ParkingLotSidewalkCharacteristics> parkingChars) {
        for (ParkingLotSidewalkCharacteristics eachParkingChar : parkingChars) {
            if (eachParkingChar == ParkingLotSidewalkCharacteristics.NoneOfAbove && parkingChars.size() > 1) {
                systemOut("None of the Above was chosen as an option. This is mutually exclusive of the other checkbox options. "
                        + "The None of the Above option will be used for the test. If you would like to change this, Please change your declarations.");
                parkingChars.clear();
                parkingChars.add(ParkingLotSidewalkCharacteristics.NoneOfAbove);
                break;
            }
        }
        if (!checkBox_NoneAbove().isSelected()) {
            for (ParkingLotSidewalkCharacteristics eachParkingChar : parkingChars) {
                switch (eachParkingChar) {
                    case Potholes:
                        checkBox_Potholes().select(true);
                        break;
                    case DeepCracks:
                        checkBox_DeepCracks().select(true);
                        break;
                    case RaisedSunkenSurfaces:
                        checkBox_Surfaces().select(true);
                        break;
                    case TripFallHazards:
                        checkBox_Hazards().select(true);
                        break;
                    case WellLitAtNight:
                        checkBox_WellLit().select(true);
                        break;
                    case NoneOfAbove:
                        checkBox_NoneAbove().select(true);
                        break;
                }
            }
        } else {
            if (!(parkingChars.get(0).equals(ParkingLotSidewalkCharacteristics.NoneOfAbove))) {
                checkBox_NoneAbove().select(false);
                setParkingLotSidewalkCharacteristics(parkingChars);
            }
        }
    }


    public void setSafetyEquipment(ArrayList<SafetyEquipment> safetyEquipmentList) {
        for (SafetyEquipment eachSafety : safetyEquipmentList) {
            if (eachSafety == SafetyEquipment.NoneOfAbove && safetyEquipmentList.size() > 1) {
                systemOut(
                        "None of the Above was chosen as an option. This is mutually exclusive of the other checkbox options. "
                                + "The None of the Above option will be used for the test. If you would like to change this, Please change your declarations.");
                safetyEquipmentList.clear();
                safetyEquipmentList.add(SafetyEquipment.NoneOfAbove);
                break;
            }
        }
        if (!checkBox_SafetyNoneAbove().isSelected()) {
            for (SafetyEquipment eachSafety : safetyEquipmentList) {
                switch (eachSafety) {
                    case HandRailinThreeOrMoreSteps:
                        checkBox_HandRail().select(true);
                        break;
                    case NonSlipSurfaces:
                        checkBox_NonSlipSurface().select(true);
                        break;
                    case FloorMatsEntrances:
                        checkBox_FloorMat().select(true);
                        break;
                    case InteriorStairLighting:
                        checkBox_InteriorStairLight().select(true);
                        break;
                    case Other:
                        checkBox_Other().select(true);
                        otherDescription.sendKeys("Something Other Than Listed");
                        break;
                    case NoneOfAbove:
                        checkBox_SafetyNoneAbove().select(true);
                        break;
                }
            }
        }
    }


    public int getBuildingsBuildingNumberCellLast(String description) {
        int lastTableRow = tableUtils.getRowCount(table_SubmissionBuildings);
        return Integer.valueOf(tableUtils.getCellTextInTableByRowAndColumnName(table_SubmissionBuildings, lastTableRow, "Bldg. #"));
    }


    public void clickBuildingsLocationsRow(int locationNumber) {
        int tableRowNumber = tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(table_SubmissionBuildingsLocations, "Loc. #", String.valueOf(locationNumber)));
        tableUtils.clickRowInTableByRowNumber(table_SubmissionBuildingsLocations, tableRowNumber);
    }


    public void clickBuildingRowByNumber(int buildingNumber) {
        tableUtils.clickRowInTableByText(table_SubmissionBuildings, String.valueOf(buildingNumber));
    }


    public void clickBuildingViewLinkByBuildingNumber(int buildingNumber) {
        tableUtils.clickLinkInSpecficRowInTable(table_SubmissionBuildings, tableUtils.getRowNumberInTableByText(table_SubmissionBuildings, String.valueOf(buildingNumber)));
    }


    public void setDescription(String description) {
        clickWhenClickable(Description);
        Description.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        Description.sendKeys(description);
    }


    public void setBuildingLimitCoverage(boolean trueFalse) {
        if ((finds(By.xpath("//*[contains(@id,'-legendTitle') and contains(.,'Building')]/preceding-sibling::table")).size() > 0)) {
            checkBox_BOPBldgCoverage().select(trueFalse);
        }
    }


    public void setBuildingLimit(double buildingLimit) {
        boolean isVisible = true;

        try {
            waitUntilElementIsClickable(BOPBldgCovLimit);
        } catch (Exception e) {
            isVisible = false;
        }

        if (isVisible) {
            BOPBldgCovLimit.sendKeys(Keys.CONTROL + "a");
            BOPBldgCovLimit.sendKeys("" + buildingLimit);
            Description.click();
        }
    }


    public void setBasisAmount(double basisAmount) {
        boolean isVisible = true;

        try {
            waitUntilElementIsClickable(BOPBldgBasisAmount);
        } catch (Exception e) {
            isVisible = false;
        }

        if (isVisible) {
            BOPBldgBasisAmount.sendKeys(Keys.CONTROL + "a");
            BOPBldgBasisAmount.sendKeys("" + basisAmount);
            Description.click();
        }
    }


    public double getBuildingLimitAmount() {
        waitUntilElementIsClickable(BOPBldgCovLimit);
        String limitText = BOPBldgCovLimit.getText();
//      @editor ecoleman 5/25/18: numberutils requires a preceding $ symbol to parse
        if (limitText.charAt(0) != '$') {
            limitText = "$" + limitText;
        }
        return NumberUtils.getCurrencyValueFromElement(limitText);
    }


    public void setBuildingPersonalPropertyLimitCoverage(boolean trueFalse) {
        if ((finds(By.xpath("//*[contains(@id,'-legendTitle') and contains(.,'Business Personal Property')]/preceding-sibling::table")).size() > 0)) {
            checkBox_BOPBldgPersonalCoverage().select(trueFalse);
        }
    }


    public void setBuildingPersonalPropertyLimit(double bopBldgPerLimit) {
        waitUntilElementIsClickable(BOPBldgPerLimit);
        BOPBldgPerLimit.sendKeys(Keys.CONTROL + "a");
        BOPBldgPerLimit.sendKeys("" + bopBldgPerLimit);
        Description.click();
    }


    public double getBuildingPersonalPropertyAmount() {
        waitUntilElementIsClickable(BOPBldgPerLimit);
        return NumberUtils.getCurrencyValueFromElement(BOPBldgPerLimit.getText());
    }


    public void setInsuredOwner(boolean radioValue) {
        Guidewire8RadioButton radio = new Guidewire8RadioButton(driver, "//table[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:OwnerOfBuilding')]");
        radio.select(radioValue);

    }

    public void setInsuredOperate(boolean radioValue) {
        Guidewire8RadioButton radio = new Guidewire8RadioButton(driver, "//table[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:InsOperBuilding')]");
        radio.select(radioValue);

    }


    public void setNumberOfFireExtinguishers(int count) {
        scrollToElement(FireExtCnt);
        setText(FireExtCnt, String.valueOf(count));
    }


    public void setYearBuilt(int year) {
        clickWhenClickable(YearBuilt);
        YearBuilt.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        YearBuilt.sendKeys(String.valueOf(year));
        clickProductLogo();
    }


    public void setNumOfStories(int count) {
        clickWhenClickable(NumOfStories);
        setText(NumOfStories, String.valueOf(count));
        clickWhenClickable(Description);
        waitForPostBack();
    }


    public void setNumOfBasements(int count) {
        clickWhenClickable(NumOfBasements);
        setText(NumOfBasements, String.valueOf(count));
        clickProductLogo();

        if (!NumOfBasements.getAttribute("value").equals(String.valueOf(count))) {
            clickWhenClickable(NumOfBasements);
            setText(NumOfBasements, String.valueOf(count));
            clickProductLogo();
        }
    }


    public void setTotalArea(String area) {
        waitUntilElementIsClickable(TotalArea);
        TotalArea.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        TotalArea.sendKeys(area);
        Description.click();
    }


    public void setRoofExclusion(boolean radioValue) {
        radio_FlatRoof().select(radioValue);
    }


    public void setWindstormLosses(boolean radioValue) {
        // TODO Add whatever logic is supposed to go here.
    }


    public void setExitsMarked(boolean radioValue) {
        radio_ExitsMarked().select(radioValue);
        waitForPostBack();
    }


    public void setExposureToFlammables(boolean radioValue) {
        WebElement element = find(By.xpath("//input[@id='BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:ExposureFlamChem_false-inputEl']"));
        scrollToElement(element);
        radio_ExposureToFlammableChemicals().select(radioValue);
    }


    public void setSprinklered(boolean radioValue) {
        radio_Sprinklered().select(radioValue);
    }


    public void setFlatRoof(boolean radioValue) {
        radio_FlatRoof().select(radioValue);
    }


    public void setExistingDamage(boolean radioValue) {
        radio_ExistingDamage().select(radioValue);
    }


    public void setDamageDescription(String description) {
        clickWhenClickable(ExistDamageDesc);
        ExistDamageDesc.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        ExistDamageDesc.sendKeys(description);
        ExistDamageDesc.sendKeys(Keys.TAB);
    }


    public void setInsuredPropertyWithin100Feet(boolean radioValue) {
        scrollToElement(find(By.xpath("//input[contains(@id,':Propertywithin100ft_false-inputEl')]")));
        scrollToElement(find(By.xpath("//input[contains(@id,':Propertywithin100ft_false-inputEl')]")));
        radio_InsuredProperty100ft().select(radioValue);
    }


    public void setInsuredWithinHundredFeetPolicyHolderName(String name) {
        clickWhenClickable(editbox_InsuredWithinHundredFtPolicyHolderName);
        editbox_InsuredWithinHundredFtPolicyHolderName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_InsuredWithinHundredFtPolicyHolderName.sendKeys(name);
        editbox_InsuredWithinHundredFtPolicyHolderName.sendKeys(Keys.TAB);
    }


    public void setInsuredWithinHundredFeetPolicyNumber(String policyNumber) {
        clickWhenClickable(editbox_InsuredWithinHundredFtPolicyNumber);
        editbox_InsuredWithinHundredFtPolicyNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_InsuredWithinHundredFtPolicyNumber.sendKeys(policyNumber);
        editbox_InsuredWithinHundredFtPolicyNumber.sendKeys(Keys.TAB);
    }


    public void setSqFtPercentOccupied(String sqFtPercentOccupied) {
        Guidewire8Select mySelect = null;
        mySelect = select_PercentOccupied();
        mySelect.selectByVisibleText(sqFtPercentOccupied);
    }


    public void setPercentAreaLeassedToOthers(String percentAreaLeassedToOthers) {
        Guidewire8Select mySelect = select_AreaLeased();
        mySelect.selectByVisibleText(percentAreaLeassedToOthers);
    }


    public void setInterestType(OccupancyInterestType interestType) {
        Guidewire8Select mySelect = select_InterestType();
        mySelect.selectByVisibleText(interestType.getValue());
    }


    public void setBuildingValuationMethod(String buildingValuationMethod) {
        Guidewire8Select mySelect = select_BOPBldgCovValuationMethod();
        mySelect.selectByVisibleText(buildingValuationMethod);
    }


    public void setBuildingCauseOfLoss(String buildingCauseOfLoss) {
        Guidewire8Select mySelect = select_BOPBldgCovCauseOfLoss();
        mySelect.selectByVisibleText(buildingCauseOfLoss);
    }


    public void setBPPValuationMethod(String BPPValuationMethod) {
        Guidewire8Select mySelect = select_BOPBldgPerPropValuationMethod();
        mySelect.selectByVisibleText(BPPValuationMethod);
    }


    public void setBPPCauseOfLoss(String BPPCauseOfLoss) {
        Guidewire8Select mySelect = select_BOPBldgPerCauseOfLoss();
        mySelect.selectByVisibleText(BPPCauseOfLoss);
    }


    public void setExteriorHousekeeping(HouseKeepingMaint exteriorHousekeeping) {
        Guidewire8Select mySelect = select_ExtHouseKeepMaint();
        mySelect.selectByVisibleText(exteriorHousekeeping.getValue());
    }


    public void setInteriorHousekeeping(HouseKeepingMaint interiorHousekeeping) {
        Guidewire8Select mySelect = select_IntHouseKeepMaint();
        mySelect.selectByVisibleText(interiorHousekeeping.getValue());
    }


    public void selectConstructionType(ConstructionType constructionType) {
        select_ConstructionType().selectByVisibleText(constructionType.getValue());
        if (constructionType.equals(ConstructionType.Manufactured)) {
            radio_PermanentFoundation().select(true);
            Description.click();// changed 7/28/2015 because the TAB wasn't starting page post back
        }
    }


    public void setRoofingType(RoofingType roofingType) {
        select_RoofType().selectByVisibleText(roofingType.getValue());
    }


    public void setMaintenanceSchedule(String maintenanceSchedule) {
        select_FlatRoofMaintSchd().selectByVisibleText(maintenanceSchedule);
    }


    public void setRoofCondition(RoofCondition roofCondition) {
        Guidewire8Select roofSelect = select_RoofCondition();
        roofSelect.selectByVisibleText(roofCondition.getValue());
    }


    public void setLastUpdateRoofing(int year) {
        waitUntilElementIsClickable(LastUpdateRoofing);
        LastUpdateRoofing.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        LastUpdateRoofing.sendKeys("" + year);
        LastUpdateRoofing.sendKeys(Keys.TAB);
    }


    public void setWiringType(WiringType wiringType) {
        select_WireType().selectByVisibleText(wiringType.getValue());
    }


    public void setBoxType(BoxType boxType) {
        select_BoxType().selectByVisibleText(boxType.getValue());
    }


    public void setLastUpdateWiring(int year) {
        waitUntilElementIsClickable(LastUpdateWiring);
        LastUpdateWiring.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        LastUpdateWiring.sendKeys("" + year);
        LastUpdateWiring.sendKeys(Keys.TAB);
    }


    public void setWiringUpdateDescription(String updateDescription) {
        waitUntilElementIsClickable(WiringUpdDesc);
        WiringUpdDesc.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        WiringUpdDesc.sendKeys(updateDescription);
        WiringUpdDesc.sendKeys(Keys.TAB);
    }


    public void setLastUpdateHeating(int year) {
        waitUntilElementIsClickable(LastUpdateHeating);
        LastUpdateHeating.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        LastUpdateHeating.sendKeys("" + year);
        LastUpdateHeating.sendKeys(Keys.TAB);
    }


    public void setHeatingUpdateDescription(String updateDescription) {
        waitUntilElementIsClickable(HeatingUpdDesc);
        HeatingUpdDesc.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        HeatingUpdDesc.sendKeys(updateDescription);
        HeatingUpdDesc.sendKeys(Keys.TAB);
    }


    public void setWoodBurningStove(boolean yesno) {
        radio_WoodBurningStove().select(yesno);
    }


    public void setLastUpdatePlumbing(int year) {
        waitUntilElementIsClickable(LastUpdatePlumbing);
        LastUpdatePlumbing.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        LastUpdatePlumbing.sendKeys("" + year);
        LastUpdatePlumbing.sendKeys(Keys.TAB);
    }


    public void setPlumbingUpdateDescription(String updateDescription) {
        waitUntilElementIsClickable(PlumbingUpdDesc);
        PlumbingUpdDesc.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        PlumbingUpdDesc.sendKeys(updateDescription);
        PlumbingUpdDesc.sendKeys(Keys.TAB);
    }


    public void setTypeOfAlarmSystem(String typeOfAlarmSystem) {
        select_BuildingAlarmSystemType().selectByVisibleText(typeOfAlarmSystem);
    }


    public void setAlarmResponseType(String alarmResponseType) {
        select_BuildingAlarmType().selectByVisibleText(alarmResponseType);
    }


    public void setAlarmGrade(String alarmGrade) {
        select_AlarmGrade().selectByVisibleText(alarmGrade);
    }


    public void clickAddBuilding() {
        Add.click();
    }

    /**
     * Building class code search
     */

    public void clickBuildingClassCodeSearchPopup() {
        clickWhenClickable(SelectBOPBuildingClassCodeRange);
        int counter = 0;
        while (finds(By.xpath("//span[contains(@id, 'ClassCodeSearchScreen:ttlBar')]")).size() <= 0 && counter < 5) {
            clickWhenClickable(SelectBOPBuildingClassCodeRange);
            counter++;
        }
    }


    public void clickBuildingClassCodeSearchButton() {
        waitUntilElementIsVisible(Search_link);
        Search_link.click();
    }


    public void setBuildingClassClassification(String buildingClassCode) {
        waitUntilElementIsVisible(Classification);
        Classification.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        Classification.sendKeys(buildingClassCode);
    }


    public void setBuildingClassCode(String buildingClassCode) {
        clickWhenClickable(Code);
        Code.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        Code.sendKeys(buildingClassCode);
    }


    public void selectFirstBuildingCode() {
        waitUntilElementIsVisible(Select_link);
        Select_link.click();
    }


    public void selectFirstBuildingCodeResult(String buildingClassification) {
        clickBuildingClassCodeSearchPopup();
        setBuildingClassClassification(buildingClassification);
        Search_link.click();
        selectFirstBuildingCode();
    }


    public void selectFirstBuildingCodeResultClassCode(String buildingCode) {
        clickBuildingClassCodeSearchPopup();
        setBuildingClassCode(buildingCode.trim());
        Search_link.click();
        selectFirstBuildingCode();
        waitForPostBack();
    }


    public void selectFirstBuildingClassCode(String buildingCode, String buildingClassification) {
        clickBuildingClassCodeSearchPopup();
        setBuildingClassCode(buildingCode.trim());
        setBuildingClassClassification(buildingClassification.trim());
        Search_link.click();
        selectFirstBuildingCode();
    }

    // @Author ecoleman
    public void changeBuildingClassCode(String buildingCode) {
        clickEdit();
        clickBuildingClassCodeSearchPopup();
        setBuildingClassCode(buildingCode);
        clickSearch();
        selectFirstBuildingCode();
    }

    public void clickBuildingDetails() {
        long stopTime = new Date().getTime() + (10 * 1000);
        do {
            link_BuildingDetails.click();
        }
        while (finds(By.xpath("//span[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:Update-btnEl')]")).size() <= 0 && new Date().getTime() < stopTime);

    }


    public void clickAdditionalCoverages() {
        long stopTime = new Date().getTime() + (10 * 1000);
        do {
            clickWhenClickable(link_AdditionalCoverages);
        }
        while (finds(By.xpath("//span[contains(text(), 'Income / Expense')]")).size() <= 0 && new Date().getTime() < stopTime);
    }

    public void clickOK() {
        clickWhenClickable(buildingsOKButton);
    }

    public void removeBuildingOnLocation(int locationNum, int bldgNum) {
        String locationNumberGridColumn = tableUtils.getGridColumnFromTable(table_SubmissionBuildingsLocations, "Loc. #");
        table_SubmissionBuildingsLocations.findElement(By.xpath(".//tr/td[contains(@class,'" + locationNumberGridColumn + "') and contains(.,'" + locationNum + "')]")).click();
        String buildingNumberGridColumn = tableUtils.getGridColumnFromTable(table_SubmissionBuildings, "Bldg. #");
        table_SubmissionBuildings.findElement(By.xpath(".//tr/td[contains(@class,'" + buildingNumberGridColumn + "') and contains(.,'" + bldgNum + "')]/parent::tr/td[contains(@class,'-checkcolumn')]")).click();
        Remove.click();
        selectOKOrCancelFromPopup(OkCancel.OK);
    }

    public void verifyPopupWarning() throws Exception {
        if (!checkIfElementExists(popup_AreYouSure, 500)) {
            throw new Exception("Popup Warning does not exist on Buildings page.");
        } else {
            if (checkIfElementExists(popup_AreYouSure, 500)) {
                selectOKOrCancelFromPopup(OkCancel.OK);
            }
        }
    }


    //ATTENTION: THIS METHOD SHOULD BE THE ONLY METHOD FOR WORKING WITH ADDITIONAL INTERESTS USED ON THIS PAGE!!!
    //PLEASE USE THE GenericWorkorderAdditionalInterests CLASS TO WORK WITH ADDITIONAL INTERESTS IN OTHER CIRCUMSTANCES!!!

    public void setAdditionalInterestInfo(PolicyLocation location) {
        for (Building building : location.getBuildingList()) {
            for (AdditionalInterest ai : building.getAdditionalInterestList()) {
                clickBuildingViewLinkByBuildingNumber(building.getNumber());
                repository.pc.workorders.generic.GenericWorkorderAdditionalInterests additionalInterest = new GenericWorkorderAdditionalInterests(getDriver());
                ai.setLienholderNumber(ai.getCompanyOrInsured().equals(ContactSubType.Company) ? additionalInterest.getAdditionalInterestAccountNumber(ai.getCompanyName()) :
                        additionalInterest.getAdditionalInterestAccountNumber(ai.getPersonFirstName() + " " + ai.getPersonLastName()));
                if (checkIfElementExists("//a[contains(@id, '__crumb__') or contains(@id, 'HOLocationFBMPopup:__crumb__')]", 500)) {
                    clickReturnTo();
                }
            }
            }
        }
}



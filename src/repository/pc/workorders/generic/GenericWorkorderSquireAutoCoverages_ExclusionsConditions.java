package repository.pc.workorders.generic;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.TableUtils;

public class GenericWorkorderSquireAutoCoverages_ExclusionsConditions extends GenericWorkorderSquireAutoCoverages {
	
	private WebDriver driver;
	private TableUtils tableUtils;
	
    public GenericWorkorderSquireAutoCoverages_ExclusionsConditions(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
    }
    
    private void setCheckboxInFieldsetByText(String tableXpath, String textInTable, boolean value) {
		Guidewire8Checkbox tableCheckbox = new Guidewire8Checkbox(driver, tableXpath + "//legend//*[contains(text(),'" + textInTable + "')]/ancestor::div/table[contains(@class, 'checkbox')]");
		tableCheckbox.select(value);
	}
    
    private void setEditboxInFieldsetByText(String tableXpath, String textInTable, String textForEditbox) {
		WebElement tableEditbox = find(By.xpath(tableXpath + "//legend//*[contains(text(),'" + textInTable	+ "')]/ancestor::fieldset//input[contains(@type, 'text')]"));
		tableEditbox.sendKeys(Keys.chord(Keys.CONTROL, "a"), textForEditbox);
	}

    public void fillOutExclusionsAndConditions(GeneratePolicy policy) {
        fillOutExclusions(policy);
        fillOutConditions(policy);
    }

    public void fillOutExclusions(GeneratePolicy policy) {

    }

    public void fillOutConditions(GeneratePolicy policy) {
        clickCoverageExclusionsTab();
        if (policy.squire.getTypeToGenerate() != GeneratePolicyType.QuickQuote && policy.squire.hagerty == true) {
        	setModification301(policy.squire.squirePA.getCoverages().hasModification_301(), policy.squire.squirePA.getCoverages().getModificationText());	
        }        
    }

    private String conditionsTableXpath = "//div[contains(@id, 'policyLevelExclusionsAndConditions:GenericExclusionCondition_ExtPanelSet:CondDV_ref')]/table";

    public void setModification301(boolean hasCoverage, String coverageDescription) {
        checkAndEnterText(conditionsTableXpath, "Modification of Insured Vehicle Definition Endorsement 301", hasCoverage, coverageDescription);
    }

    private void checkAndEnterText(String tableXpath, String coverage, boolean hasCoverage, String description) {
        setCheckboxInFieldsetByText(tableXpath, coverage, hasCoverage);
        if (hasCoverage) {
            setEditboxInFieldsetByText(tableXpath, coverage, description);
        }
    }

    @FindBy(xpath = "//div[contains(@id, 'policyLevelExclusionsAndConditions:GenericExclusionCondition_ExtPanelSet:CondDV_ref')]/table")
    public WebElement table_ModificationOfInsuredVehicleDefinitionEndorsement_301;

    public void fillOutCoveragesExclutionsConditions() {

//      setModification301(true, ""); @editor ecoleman 8/3/18: This is now set at the qualification page
        clickAdd();
        tableUtils.setValueForCellInsideTable(table_ModificationOfInsuredVehicleDefinitionEndorsement_301, tableUtils.getNextAvailableLineInTable(table_ModificationOfInsuredVehicleDefinitionEndorsement_301, "Year"), "Year", "c1", "2004");
        tableUtils.setValueForCellInsideTable(table_ModificationOfInsuredVehicleDefinitionEndorsement_301, tableUtils.getNextAvailableLineInTable(table_ModificationOfInsuredVehicleDefinitionEndorsement_301, "Make"), "Make", "c2", "BMW");
        tableUtils.setValueForCellInsideTable(table_ModificationOfInsuredVehicleDefinitionEndorsement_301, tableUtils.getNextAvailableLineInTable(table_ModificationOfInsuredVehicleDefinitionEndorsement_301, "Model"), "Model", "c3", "scion");
        tableUtils.setValueForCellInsideTable(table_ModificationOfInsuredVehicleDefinitionEndorsement_301, tableUtils.getNextAvailableLineInTable(table_ModificationOfInsuredVehicleDefinitionEndorsement_301, "VIN Number"), "VIN Number", "c4", "4UZFCHCY8FCG18416");
    }

    private Guidewire8Checkbox checkbox_PAExclusionsAdditionalInsuredEndorsement() {
        return new Guidewire8Checkbox(driver, "//div[contains(.,'Additional Insured Endorsement 361')]/preceding-sibling::table");
    }

    @FindBy(xpath = "//span[contains(@id, ':PAWizardStepGroup:PersonalAutoScreen:PersonalAutoCoveragesPanelSet:policyLevelExclusionsAndConditions:GenericExclusionCondition_ExtPanelSet:CondDV:2:LineCond:CoverageInputSet:CovPatternInputGroup:Add-btnInnerEl')]")
    private WebElement button_PAExclusionsAdditionalInsuredEndorsementEmployerAdd;
    @FindBy(xpath = "//div[contains(@id, ':PAWizardStepGroup:PersonalAutoScreen:PersonalAutoCoveragesPanelSet:policyLevelExclusionsAndConditions:GenericExclusionCondition_ExtPanelSet:CondDV:2:LineCond:CoverageInputSet:CovPatternInputGroup')]/table/descendant::table[1]/descendant::tr/td[2]/div")
    private WebElement text_PAExclusionsAdditionalInsuredEndorsementEmployer;
    @FindBy(xpath = "//textarea[contains(@role, 'textbox') and contains(@name, 'c1')]")
    private WebElement input_PAExclusionsAdditionalInsuredEndorsementEmployer;

    private Guidewire8Checkbox checkbox_PAExclusionsModificationOfInsuredVehicleDefinitionEndorsement() {
        return new Guidewire8Checkbox(driver, "//div[contains(.,'Modification of Insured Vehicle Definition Endorsement 301')]/preceding-sibling::table");
    }

    @FindBy(xpath = "//div[contains(., 'Modification of Insured Vehicle Definition Endorsement 301')]/ancestor::legend/following-sibling::div/descendant::table/descendant::span[contains(@id,':LineCond:CoverageInputSet:CovPatternInputGroup:ScheduleInputSet:Add-btnEl')]")
    private WebElement button_PAExclusionsModificationOfInsuredVehicleAdd;
    @FindBy(xpath = "//div[contains(@id, 'LOBWizardStepGroup:LineWizardStepSet:PAWizardStepGroup:PersonalAutoScreen:PersonalAutoCoveragesPanelSet:policyLevelExclusionsAndConditions:GenericExclusionCondition_ExtPanelSet:CondDV:0:LineCond:CoverageInputSet:CovPatternInputGroup:ScheduleInputSet:0')]")
    private WebElement table_PAExclusionsModificationOfInsuredVehicleMake;
    @FindBy(xpath = "//div[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:PAWizardStepGroup:PersonalAutoScreen:PersonalAutoCoveragesPanelSet:policyLevelExclusionsAndConditions:GenericExclusionCondition_ExtPanelSet:CondDV:0:LineCond:CoverageInputSet:CovPatternInputGroup:ScheduleInputSet:ScheduleLV')]/table/descendant::table[1]/descendant::tr/td[4]/div")
    private WebElement text_PAExclusionsModificationOfInsuredVehicleModel;
    @FindBy(xpath = "//input[contains(@role, 'textbox') and contains(@name, 'c3')]")
    private WebElement input_PAExclusionsModificationOfInsuredVehicleModel;
    @FindBy(xpath = "//div[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:PAWizardStepGroup:PersonalAutoScreen:PersonalAutoCoveragesPanelSet:policyLevelExclusionsAndConditions:GenericExclusionCondition_ExtPanelSet:CondDV:0:LineCond:CoverageInputSet:CovPatternInputGroup:ScheduleInputSet:ScheduleLV')]/table/descendant::table[1]/descendant::tr/td[5]/div")
    private WebElement text_PAExclusionsModificationOfInsuredVehicleVin;
    @FindBy(xpath = "//input[contains(@role, 'textbox') and contains(@name, 'c4')]")
    private WebElement input_PAExclusionsModificationOfInsuredVehicleVIN;
    @FindBy(xpath = "//div[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:PAWizardStepGroup:PersonalAutoScreen:PersonalAutoCoveragesPanelSet:policyLevelExclusionsAndConditions:GenericExclusionCondition_ExtPanelSet:CondDV:0:LineCond:CoverageInputSet:CovPatternInputGroup:ScheduleInputSet:ScheduleLV')]/table/descendant::table[1]/descendant::tr/td[2]/div")
    private WebElement text_PAExclusionsModificationOfInsuredVehicleYear;
    @FindBy(xpath = "//input[contains(@role, 'textbox') and contains(@name, 'c1')]")
    private WebElement input_PAExclusionsModificationOfInsuredVehicleYear;

    private Guidewire8Checkbox checkbox_PAExclusionsSpecialEndorsementForAuto() {
        return new Guidewire8Checkbox(driver, "//div[contains(.,'Special Endorsement for Auto 305')]/preceding-sibling::table");
    }

    @FindBy(xpath = "//span[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:PAWizardStepGroup:PersonalAutoScreen:PersonalAutoCoveragesPanelSet:policyLevelExclusionsAndConditions:GenericExclusionCondition_ExtPanelSet:CondDV:1:LineCond:CoverageInputSet:CovPatternInputGroup:Add-btnEl')]")
    private WebElement button_PAExclusionsSpecialEnsorsementForAutoAdd;
    @FindBy(xpath = "//div[contains(@id, ':LineWizardStepSet:PAWizardStepGroup:PersonalAutoScreen:PersonalAutoCoveragesPanelSet:policyLevelExclusionsAndConditions:GenericExclusionCondition_ExtPanelSet:CondDV:1:LineCond:CoverageInputSet:CovPatternInputGroup:0-body')]/div/table/tbody/tr/td[2]/div")
    private WebElement text_PAExclusionsSpecialEndorsementForAuto305;
    @FindBy(xpath = "//div[contains(@id, ':PAWizardStepGroup:PersonalAutoScreen:PersonalAutoCoveragesPanelSet:policyLevelExclusionsAndConditions:GenericExclusionCondition_ExtPanelSet:CondDV:1:LineCond:CoverageInputSet:CovPatternInputGroup:0-body')]/following-sibling::div/table/tbody/tr/td[2]/textarea")
    private WebElement textarea_PAExclusionsSpecialEndorsementForAuto305;

    private Guidewire8Checkbox checkbox_PAExclusionsDriverExclusionendorsement() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, ':PAWizardStepGroup:PersonalAutoScreen:PersonalAutoCoveragesPanelSet:policyLevelExclusionsAndConditions:GenericExclusionCondition_ExtPanelSet:ExclDV:0:LineExcl:CoverageInputSet:CovPatternInputGroup-legendChk')]");
    }

    @FindBy(xpath = "//div[contains(., 'Driver Exclusion Endorsement 304')]/ancestor::legend/following-sibling::div/descendant::table/descendant::span[contains(@id,':LineExcl:CoverageInputSet:CovPatternInputGroup:AddContactsButton')]")
    private WebElement button_PAExclusionsDriverExclusionEndorsement304AddExisting;

    //span[contains(@id,":LineExcl:CoverageInputSet:CovPatternInputGroup:addButton-btnInnerEl")]
    @FindBy(xpath = "//span[contains(@id,':LineExcl:CoverageInputSet:CovPatternInputGroup:addButton-btnInnerEl')]")
    private WebElement button_PAExclusionsDriverExclusionEndorsement304AddNew;

    @FindBy(xpath = "//div[contains(@id ,'PersonalAutoCoveragesPanelSet:policyLevelExclusionsAndConditions:GenericExclusionCondition_ExtPanelSet:ExclDV:0:LineExcl:CoverageInputSet:CovPatternInputGroup:0-body')]//table//tr//td[2]/div")
    private WebElement button_PAExclusionsDriverExclusionEndorsement304AddNewValue;
    public void clickSpecialEndorsementForAuto305Add() {
        clickWhenClickable(button_PAExclusionsSpecialEnsorsementForAutoAdd);
    }

    public void addAdditionalInsuredEndorsement(String company) {
        checkbox_PAExclusionsAdditionalInsuredEndorsement().select(true);
        clickWhenClickable(button_PAExclusionsAdditionalInsuredEndorsementEmployerAdd);
        text_PAExclusionsAdditionalInsuredEndorsementEmployer.click();
        input_PAExclusionsAdditionalInsuredEndorsementEmployer.sendKeys(company);
    }

    public void addModificationOfInsuredVehicleDefinitionEndorsement(String make, String model, String vin, String year) {
        checkbox_PAExclusionsModificationOfInsuredVehicleDefinitionEndorsement().select(true);
        addNewModificationOfInsuredvehicleDefinitionEndorsement(make, model, vin, year);
    }
    
    public void addNewModificationOfInsuredvehicleDefinitionEndorsement(String make, String model, String vin, String year){
        clickWhenClickable(button_PAExclusionsModificationOfInsuredVehicleAdd);
        tableUtils.selectValueForSelectInTable(table_PAExclusionsModificationOfInsuredVehicleMake, 1, "Make", make);

        text_PAExclusionsModificationOfInsuredVehicleModel.click();
        input_PAExclusionsModificationOfInsuredVehicleModel.sendKeys(model);
        text_PAExclusionsModificationOfInsuredVehicleVin.click();
        input_PAExclusionsModificationOfInsuredVehicleVIN.sendKeys(vin);
        text_PAExclusionsModificationOfInsuredVehicleYear.click();
        input_PAExclusionsModificationOfInsuredVehicleYear.sendKeys(year);
    }

    public void addSpecialEndorsementForAuto(String description) {
        checkbox_PAExclusionsSpecialEndorsementForAuto().select(true);
        clickSpecialEndorsementForAuto305Add();
        clickWhenClickable(text_PAExclusionsSpecialEndorsementForAuto305);
        textarea_PAExclusionsSpecialEndorsementForAuto305.sendKeys(description);
        textarea_PAExclusionsSpecialEndorsementForAuto305.sendKeys(Keys.TAB);
    }


    public void addDriverExclusionEndorsement304(String value) {
        checkbox_PAExclusionsDriverExclusionendorsement().select(true);
		clickProductLogo();
        hoverOverAndClick(button_PAExclusionsDriverExclusionEndorsement304AddExisting);
        String xPath = "//*[contains(text(), '" + value + "')  and contains (@id, ':ExistingAdditionalInterest')]";
        WebElement othercontact = find(By.xpath(xPath));
        clickWhenClickable(othercontact);
    }

    public void addNewDriverExclusionEndorsement304(String value) {
        checkbox_PAExclusionsDriverExclusionendorsement().select(true);
        clickProductLogo();
        hoverOverAndClick(button_PAExclusionsDriverExclusionEndorsement304AddNew);
       clickWhenClickable(button_PAExclusionsDriverExclusionEndorsement304AddNewValue);

        WebElement myText = find(By.xpath("//input[@name = 'c1']"));
        myText.clear();
        myText.sendKeys(value);
    }

    @FindBy(xpath = "//textarea[contains(@id, ':PAWizardStepGroup:PersonalAutoScreen:PersonalAutoCoveragesPanelSet:policyLevelExclusionsAndConditions:GenericExclusionCondition_ExtPanelSet:CondDV:2:LineCond:CoverageInputSet:CovPatternInputGroup:0:CovTermInputSet:StringTermInput-inputEl')]")
    private WebElement textarea_PAExclusionsAdditionalInsuredEndorsementEmployer;

    @FindBy(xpath = "//div[contains(@id, ':PAWizardStepGroup:PersonalAutoScreen:policyLevelExclusionsAndConditions:GenericExclusionCondition_ExtPanelSet:CondDV:2:LineCond:CoverageInputSet:CovPatternInputGroup:0')]")
    private WebElement table_PAExlusionsAdditionalInsuredEndorsementEmployer;

    @FindBy(xpath = "//div[contains(., 'Driver Exclusion Endorsement 304')]/ancestor::legend/following-sibling::div/descendant::table/descendant::span[contains(@id,':ExclDV:0:LineExcl:CoverageInputSet:CovPatternInputGroup:Add-btnEl')]")
    private WebElement button_PAExclusionsDriverExclusionEndorsement304Search;

    @FindBy(xpath = "//div[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:PAWizardStepGroup:PersonalAutoScreen:PersonalAutoCoveragesPanelSet:policyLevelExclusionsAndConditions:GenericExclusionCondition_ExtPanelSet:CondDV:1:LineCond:CoverageInputSet:CovPatternInputGroup:0')]")
    private WebElement div_SpecialEnsorsementForAuto305;

    @FindBy(xpath = "//div[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:PAWizardStepGroup:PersonalAutoScreen:PersonalAutoCoveragesPanelSet:policyLevelExclusionsAndConditions:GenericExclusionCondition_ExtPanelSet:ExclDV:0:LineExcl:CoverageInputSet:CovPatternInputGroup:ScheduleLV')]")
    private WebElement table_DriverToExclude;

    @FindBy(xpath = "//a[contains(@id, ':ExclDV:0:LineExcl:CoverageInputSet:CovPatternInputGroup:removeItem')]")
    private WebElement link_RemoveDriverToExclude;

    public void checkDriverToExcludeContactByName(String name) {
        tableUtils.setCheckboxInTable(table_DriverToExclude, getDriverToExcludeRowByName(name), true);
    }


    public int getDriverToExcludeRowByName(String name) {
        return tableUtils.getRowNumberInTableByText(table_DriverToExclude, name);
    }


    public void clickDriverToExlcudeSearch() {
        clickWhenClickable(button_PAExclusionsDriverExclusionEndorsement304Search);
    }


    public void clickDriverToExcludeDriverRemove() {
        clickWhenClickable(link_RemoveDriverToExclude);
    }
}




























package repository.pc.workorders.generic;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.idfbins.enums.OkCancel;

import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.Building.ValuationMethod;
import repository.gw.enums.CoverageType;
import repository.gw.enums.DocFormEvents;
import repository.gw.enums.FPP.FPPOptionalCoverages;
import repository.gw.enums.FPP.FarmPersonalPropertyTypes;
import repository.gw.enums.Property.FoundationType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.exception.GuidewireException;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PLPropertyCoverages;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SquireFPPTypeItem;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;
import repository.pc.sidemenu.SideMenuPC;

public class GenericWorkorderSquirePropertyCoverages extends GenericWorkorder {

    private WebDriver driver;
    private TableUtils tableUtils;
    
    public GenericWorkorderSquirePropertyCoverages(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    private WebElement checkbox_ExclusionCondition(String labelName) {
        return find(By.xpath("//div/div[text()='" + labelName + "']/preceding-sibling::table//input[contains(@id, ':CoverageInputSet:CovPatternInputGroup:_checkbox')]"));
    }

    private Guidewire8Checkbox checkbox_ExclusionsAndConditions(String labelName) {
        return new Guidewire8Checkbox(driver, "//div[contains(., '" + labelName + "')]/preceding-sibling::table[1]");
    }

    @FindBy(xpath = "//span[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOCoveragesPanelSet:FarmPersonalPropertyTab-btnEl') or contains(@id, ':HOCoveragesPanelSet:FarmPersonalPropertyTab-btnInnerEl')]")
    private WebElement link_SquirePropertyCoveragesFarmPersonalProperty;

    @FindBy(xpath = "//span[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOCoveragesPanelSet:SectionIITab-btnEl')]")
    private WebElement link_SquirePropertyCoveragesSectionIICoverages;

    @FindBy(xpath = "//span[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOCoveragesPanelSet:secIIAddlInterestsTab-btnEl')]")
    private WebElement link_SquirePropertyCoveragesAdditionalInterest;

    @FindBy(xpath = "//span[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOCoveragesPanelSet:HOPolicyLevelExclusionsAndConditionsCardTab-btnEl')]")
    private WebElement link_SquirePropertyCoveragesExclusions;

    @FindBy(xpath = "//span[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOCoveragesPanelSet:ExclusionsAndConditionsCardTab-btnEl')]")
    private WebElement link_SquirePropertyCoveragesBuildingExclusions;

    @FindBy(xpath = "//a[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOCoveragesPanelSet:CoveragesCardTab')]")
    private WebElement link_SquirePropertyCoveragesCoverages;

    @FindBy(xpath = "//label[contains(text(),'Add Glass Coverage')]")
    private WebElement label_AddGlassCoverage;

    @FindBy(xpath = "//label[contains(text(),'Add Weight of Ice and Snow')]")
    private WebElement label_WeightOfIceAndSnowCoverage;

    @FindBy(xpath = "//div[contains(@id, 'SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOCoveragesPanelSet:5')]")
    private WebElement table_CoveragePropertiesTable;

    private WebElement button_ExclusionsConditionsAdd(String labelName) {
        return find(By.xpath("//div[contains(., '" + labelName + "')]/ancestor::fieldset//span[contains(.,'Add')]/parent::span"));
    }

    private WebElement div_ExclusionsConditionsEndorsementContainer(String labelName) {
        return find(By.xpath("//div[contains(., '" + labelName + "')]/ancestor::legend/following-sibling::div/span/div/div/table/tbody/tr/td/div"));
    }

    private WebElement divInput_ExclusionsConditionsEndorsement(String labelName) {
        return div_ExclusionsConditionsEndorsementContainer(labelName).findElement(By.xpath(".//div[3]/div/table/tbody/tr/td[2]/div"));
    }

    private WebElement textarea_ExclusionsConditionsEndorsement(String labelName) {
        return div_ExclusionsConditionsEndorsementContainer(labelName).findElement(By.xpath(".//div[4]/table/tbody/tr/td[2]/textarea"));
    }


    //Other Structure
    @FindBy(xpath = "//div[contains(text(), 'Other Structures')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Detached Garage - Additional Value')]/parent::td/following-sibling::td/input[contains(@id,':DirectTermInput-inputEl')]")
    private WebElement editbox_SquirePropertyCoveragesOtherStructuresAdditionalValue;

    @FindBy(xpath = "//label[contains(text(), 'Valuation Method')]/parent::td/following-sibling::td/div'")
    private WebElement div_SquirePropertyCoveragesOtherStrctureValuationMethod;


    //*************************************************************
    //coverage A - Xpath

    @FindBy(xpath = "//fieldset[contains(., 'Coverage A')]/div//td[.='Limit']/following-sibling::td/input[contains(@id, ':limitTerm-inputEl')]")
    private WebElement editbox_SquirePropertyCoveragesCoverageALimit;

    private Guidewire8RadioButton radio_SquirePropertyCoveragesCoverageAIncreasedReplacementCost() {
        return new Guidewire8RadioButton(driver, "//label[contains(text(), 'Increased Replacement Cost')]/parent::td/parent::tr/parent::tbody/parent::table");
    }

    private Guidewire8RadioButton radio_SquirePropertyCoveragesCoverageAWaiveGlassDeductible() {
        return new Guidewire8RadioButton(driver, "//label[contains(text(), 'Waive Glass Deductible')]/parent::td/parent::tr/parent::tbody/parent::table");
    }

    private Guidewire8Select select_SquirePropertyCoveragesCoverageAValuationMethod() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Coverage A')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Valuation Method')]/parent::td/following-sibling::td/table");
    }

    private Guidewire8Select select_SquirePropertyCoveragesCoverageACoverageType() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Coverage A')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Coverage Type')]/parent::td/following-sibling::td/table");
    }

    //*************************************************************
    //Coverage E - Xpath
    private Guidewire8Select select_SquirePropertyCoveragesCoverageECoverageType() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Coverage E')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Coverage Type')]/parent::td/following-sibling::td/table");
    }

    @FindBy(xpath = "//fieldset[contains(., 'Coverage E')]/div//td[.='Limit']/following-sibling::td/input[contains(@id, ':limitTerm-inputEl')]")
    private WebElement editbox_SquirePropertyCoveragesCoverageELimit;

    private Guidewire8Select select_SquirePropertyCoveragesCoverageEValuationMethod() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Coverage E')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Valuation Method')]/parent::td/following-sibling::td/table");
    }

    //*************************************************************
    //Coverage C - Xpath

	/*@FindBy(xpath = "//input[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOMainCoveragesHOEPanelSet:1:HOCoverageInputSet:OthersCovPatternInputGroup:0:CovTermInputSet:DirectTermInput-inputEl')]")
	private WebElement editbox_SquirePropertyCoveragesOptionalCoverageCLimit;

	@FindBy(xpath = "//input[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOMainCoveragesHOEPanelSet:1:HOCoverageInputSet:OthersCovPatternInputGroup:0:CovTermInputSet:DirectTermInput-inputEl')]")
	private WebElement editbox_StandardFireOptionalCoverageCLimit;*/

    @FindBy(xpath = "//div[contains(text(), 'Coverage C')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Additional Value')]/parent::td/following-sibling::td/input[contains(@id,':DirectTermInput-inputEl')]")
    private WebElement editbox_SquirePropertyCoveragesCoverageCAdditionalValue;

	/*@FindBy(xpath = "//input[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOMainCoveragesHOEPanelSet:1:HOCoverageInputSet:CovAScreenCovPatternInputGroup:fancyLimitTerm-inputEl')]")
	private WebElement editbox_StandardFireCoverageCAdditionalValue;*/

    @FindBy(xpath = "//fieldset[contains(., 'Coverage C')]/div//td[.='Limit']/following-sibling::td/input[contains(@id, ':limitTerm-inputEl') or contains(@id,':DirectTermInput-inputEl') or contains(@id, ':fancyLimitTerm-inputEl')]")
    private WebElement editbox_CovCLimit;
    
    private Guidewire8Checkbox checkbox_CovCEnable() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Coverage C')]/preceding::table[contains(@id, 'legendChk')]");
    }

    private Guidewire8Select select_SquirePropertyCoveragesCoverageCCoverageType() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Coverage C')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Coverage Type')]/parent::td/following-sibling::td/table");
    }

    private Guidewire8Checkbox checkbox_SquirePropertyCoveragesOptionalCoverageC() {
        return new Guidewire8Checkbox(driver, "//div/div[text()='Coverage C']/preceding-sibling::table[contains(@id, ':HOCoverageInputSet:OthersCovPatternInputGroup-legendChk')]");
    }

    private Guidewire8Checkbox checkbox_StandardFireCoverageC() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, 'SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOCoveragesPanelSet:HOMainCoveragesHOEPanelSet:1:HOCoverageInputSet:CovAScreenCovPatternInputGroup-legendChk')]");
    }

    private Guidewire8Select select_SquirePropertyCoveragesCoverageCValuationMethod() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Coverage C')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Valuation Method')]/parent::td/following-sibling::td/table");
    }

    //**********************************************************

    private Guidewire8Checkbox checkbox_SquirePropertyCoveragesSewageSystemBackup() {
        return new Guidewire8Checkbox(driver, "//div[contains(., 'Sewage System Backup')]/preceding-sibling::table[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOCoveragesPanelSet:HOMainCoveragesHOEPanelSet:optionalCoverages:GenericCoverage_ExtPanelSet:LineDV:1:LineCovs:CoverageInputSet:CovPatternInputGroup-legendChk')]");
    }

    @FindBy(xpath = "//fieldset[contains(., 'Sewage System Backup') and contains(., 'Limit') and contains(., '10% - ')]")
    private WebElement text_SquirePropertyCoveragesSewageSystemBackup;

    private Guidewire8Select select_SquirePropertyCoveragesSectionIDeductible() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':CovPatternInputGroup:0:CovTermPOCHOEInputSet:OptionTermInput-triggerWrap')]");
    }

    private Guidewire8Select select_SquirePropertyCoveragesOtherCoverageValuation() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':HOCoverageInputSet:OthersCovPatternInputGroup:OptionTermInput-triggerWrap') or contains(@id,':HOCoveragesPanelSet:HOMainCoveragesHOEPanelSet:1:HOCoverageInputSet:CovAScreenCovPatternInputGroup:0:CovTermInputSet:OptionTermInput-triggerWrap')]");
    }

    @FindBy(xpath = "//div[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:GenericExclusionCondition_ExtPanelSet:ExclDV_ref') or contains(@id, 'SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:PolicyLevelConditionsAndExclusions:HOAdditionalExclusionsAndConditionsPanelSet:ConditionsPanel:HOAdditionalConditionsDV_ref') or contains(@id,'SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:GenericExclusionCondition_ExtPanelSet:CondDV_ref')]/descendant::table[2]/descendant::tr/descendant::div[2]")
    private WebElement text_SquirePropertyCoveragesLineConditions;

    private WebElement lineItem_SquirePropertyCoveragesBuildings(int loc, int building) {
        return find(By.xpath("//div[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOCoveragesPanelSet:5-body')]/descendant::tr/td/div[.= " + loc + "]/../../td/div[.= " + building + "]"));
    }

    private Guidewire8Checkbox checkbox_AccessYesEndorsement209() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Access Yes Endorsement 209')]/preceding-sibling::table");
    }

    private Guidewire8Checkbox checkbox_squirePropertyCoveragesSeweageBackupSystem() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Sewage System Backup')]/preceding-sibling::table");
    }

    @FindBy(xpath = "//label[contains(text(), 'Guns Increased Theft Limit')]/parent::td/following-sibling::td/input")
    private WebElement editbox_SquirePropertyCoverageGunsIncreasedTheftLimit;

    @FindBy(xpath = "//div[contains(text(), 'Guns')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Limit')]/parent::td/following-sibling::td/input")
    private WebElement editbox_SquirePropertyCoverageGuns_Limit;

    @FindBy(xpath = "//label[contains(text(), 'Silverware Increased Theft Limit')]/parent::td/following-sibling::td/input")
    private WebElement editbox_SquirePropertyCoverageSilverwareIncreasedTheftLimit;

    @FindBy(xpath = "//div[contains(text(), 'Silverware')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Limit')]/parent::td/following-sibling::td/input")
    private WebElement editbox_SquirePropertyCoverageSilverware_Limit;

    @FindBy(xpath = "//label[contains(text(), 'Tools Increased Limit')]/parent::td/following-sibling::td/input")
    private WebElement editbox_SquirePropertyCoverageToolsIncreasedLimit;

    @FindBy(xpath = "//div[contains(text(), 'Tools')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Limit')]/parent::td/following-sibling::td/input")
    private WebElement editbox_SquirePropertyCoverageTools_Limit;

    @FindBy(xpath = "//label[contains(text(), 'Saddles and Tack Increased Limit')]/parent::td/following-sibling::td/input")
    private WebElement editbox_SquirePropertyCoverageSaddlesTackIncreasedLimit;

    @FindBy(xpath = "//div[contains(text(), 'Saddles and Tack')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Limit')]/parent::td/following-sibling::td/input")
    private WebElement editbox_SquirePropertyCoverageSaddlesTack_Limit;

    final String name110 = "Specified Property Exclusion Endorsement - 110";
    @FindBy(xpath = "//fieldset[contains(., '" + name110 + "')]//div[contains(@id, ':CoverageInputSet:CovPatternInputGroup:ScheduleInputSet:') and substring(@id, string-length(@id) - 4) = '-body']//table[contains(@id, 'gridview-')]//tr[last()]/td[2]/div")
    public WebElement div_SpecifiedPropertyExclusionEndorsement110Description;

    @FindBy(xpath = "//fieldset[contains(., '" + name110 + "')]//div[contains(@id, ':CoverageInputSet:CovPatternInputGroup:ScheduleInputSet:')]//table[contains(@id, 'textfield-')]//tr[last()]/td[2]/input")
    public WebElement editbox_SpecifiedPropertyExclusionEndorsement110Description;

    @FindBy(xpath = "//fieldset[contains(., '" + name110 + "')]//a[contains(@id, 'CoverageInputSet:CovPatternInputGroup:ScheduleInputSet:Add')]")
    private WebElement button_SpecifiedPropertyExclusionEndorsement110Add;

    @FindBy(xpath = "//div[contains(., 'Coverage C')]/ancestor::legend/following-sibling::div/descendant::table/tbody/tr/td[2]/div")
    private WebElement text_SquirePropertyCoveragesCLimit;

    @FindBy(xpath = "//div[contains(@id, ':LineCovs:CoverageInputSet:CovPatternInputGroup:Limit-inputEl')]")
    private WebElement text_SquirePropertyCoveragesSewageBackupLimit;

    private WebElement checkbox_OptionalCoverages(String labelName) {
        return find(By.xpath("//div/div[text()='" + labelName + "']/preceding-sibling::table//input[contains(@id, ':LineCovs:CoverageInputSet:CovPatternInputGroup:_checkbox')]"));
    }

    @FindBy(xpath = "//div[contains(text(), 'Loss of Income & Extra Expense')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Limit')]/parent::td/following-sibling::td/input")
    private WebElement text_LossIncomeExtraExpense;

    Guidewire8RadioButton radio_IncludeMasonry() {
        return new Guidewire8RadioButton(driver, "//label[contains(text(), 'Include Masonry?')]/parent::td/following-sibling::td/div[contains(@id, ':LineCovs:CoverageInputSet:CovPatternInputGroup:1:CovTermInputSet:BooleanTermInput-containerEl')]/table");
    }

    @FindBy(xpath = "//div[text()='Other Structures']/ancestor::legend/following-sibling::div/descendant::label[text()='Limit']/parent::td/following-sibling::td/div")
    private WebElement otherStructuresLimit;

    @FindBy(xpath = "//div[text()='Coverage B Loss Of Use']/ancestor::legend/following-sibling::div/descendant::label[text()='Limit']/parent::td/following-sibling::td/div")
    private WebElement CoverageBLossOfUse;

    @FindBy(xpath = "//*[contains(@id, ':riskVal-inputEl')]")
    private WebElement Editbox_FarmPersonalPropertyRisk;

    @FindBy(xpath = "//div[contains(@id,':LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOCoveragesPanelSet:3_ref')]")
    private WebElement div_Properties;

    /*
     * Methods
     */

    ///Coverage A Methods///


    public void setCoverageALimit(double limit) {
        clickWhenClickable(editbox_SquirePropertyCoveragesCoverageALimit);
        editbox_SquirePropertyCoveragesCoverageALimit.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_SquirePropertyCoveragesCoverageALimit.sendKeys(String.valueOf(limit));
        editbox_SquirePropertyCoveragesCoverageALimit.sendKeys(Keys.TAB);
        
    }


    public double getCoverageALimit() {
        waitUntilElementIsVisible(editbox_SquirePropertyCoveragesCoverageALimit);
        return Double.parseDouble(editbox_SquirePropertyCoveragesCoverageALimit.getAttribute("value").replace(",", ""));
    }


    public void setCoverageAValuation(ValuationMethod propertyCoverages) {
        select_SquirePropertyCoveragesCoverageAValuationMethod().selectByVisibleText(propertyCoverages.getValue());
        clickProductLogo();
        
    }


    public String getCoverageAValuation() {
        
        clickWhenClickable(text_SquirePropertyCoveragesSewageSystemBackup);
        String text = select_SquirePropertyCoveragesCoverageAValuationMethod().getText();
        clickWhenClickable(text_SquirePropertyCoveragesSewageSystemBackup);
        return text;
    }


    public boolean checkIfCoverageAValuationSelectExists() {
        
        return checkIfElementExists(select_SquirePropertyCoveragesCoverageAValuationMethod().getSelectButtonElement(), 2000);
    }


    public void setCoverageACoverageType(CoverageType coverageAType) {
        select_SquirePropertyCoveragesCoverageACoverageType().selectByVisibleTextPartial(coverageAType.getValue());
    }


    public void setCoverageAIncreasedReplacementCost(boolean radioValue) {
        radio_SquirePropertyCoveragesCoverageAIncreasedReplacementCost().select(radioValue);
        
    }


    public boolean IncreasedReplacementCostExists() {
        try {
            return checkIfElementExists(radio_SquirePropertyCoveragesCoverageAIncreasedReplacementCost().getWebElement(), 100);
        } catch (Exception e) {
            return false;
        }
    }


    public void setCoverageAWaiveGlassDeductible(boolean radioValue) {
        WebElement radioYes = null;
        if (radioValue) {
            radioYes = radio_SquirePropertyCoveragesCoverageAWaiveGlassDeductible().getWebElement().findElement(By.xpath(".//descendant::label[contains(text(), 'Yes')]/preceding-sibling::input"));
        } else {
            radioYes = radio_SquirePropertyCoveragesCoverageAWaiveGlassDeductible().getWebElement().findElement(By.xpath(".//descendant::label[contains(text(), 'No')]/preceding-sibling::input"));
        }
        waitUntilElementIsClickable(radioYes);
        
        clickAndHoldAndRelease(radioYes);
        
        super.selectOKOrCancelFromPopup(OkCancel.OK);
        hoverOver(radioYes);
    }

    ///Coverage C Methods///


    public String getCoverageCLimit() {
        waitUntilElementIsVisible(text_SquirePropertyCoveragesCLimit);
        return text_SquirePropertyCoveragesCLimit.getText();
    }


    public void setCoverageCLimit(double value) {
    	enableCoverageC(); // @editor ecoleman 8/9/18: NOTE that coverage C will be an optional coverage with a checkbox that must be set before (US11238); I may miss some that need adding
        clickWhenClickable(editbox_CovCLimit);
        setText(editbox_CovCLimit, String.valueOf(value));
    }
    
    public Boolean checkCovCCheckboxExists() {
        return checkIfElementExists("//div[contains(text(), 'Coverage C')]/preceding::input[@role='checkbox']", 10);
    }
    
    public Boolean checkCovCCheckboxEnabled() {
    	return checkbox_CovCEnable().isEnabled();
    }
    
    public void enableCoverageC() {
    	if (!checkCovCCheckboxEnabled()) {
    		checkbox_CovCEnable().select(true);
    	}
    }


    public void setCoverageCAdditionalValue(double value) {
    	enableCoverageC();
        clickWhenClickable(editbox_SquirePropertyCoveragesCoverageCAdditionalValue);
        setText(editbox_SquirePropertyCoveragesCoverageCAdditionalValue, String.valueOf(value));
    }


    public void setCoverageCAdditionalValueOrLimit(double number) {
    	enableCoverageC();
        if (checkIfElementExists(editbox_SquirePropertyCoveragesCoverageCAdditionalValue, 2000)) {
            setCoverageCAdditionalValue(number);
        } else if (checkIfElementExists(editbox_CovCLimit, 2000)) {
            setCoverageCLimit(number);
        } else {
            System.out.println("Sorry, the Coverage C addtional Value or the Coverage C Limit was not found on the page and set.");
        }
    }


    public void setCoverageCValuation(PLPropertyCoverages propertyCoverages) {
    	enableCoverageC();
        select_SquirePropertyCoveragesCoverageCValuationMethod().selectByVisibleText(propertyCoverages.getCoverageC().getValuationMethod().getValue());
    }


    public void selectCoverageCCoverageType(CoverageType type) {
    	enableCoverageC();
        try {
            select_SquirePropertyCoveragesCoverageCCoverageType().selectByVisibleTextPartial(type.getValue());
        } catch (Exception E) {
            systemOut("Unable to select the CoverageCCoverageType. This most likely indicates that the value was automatically set and is no longer a drop-down.");
        }
        
    }


    public void setStdFireCoverageCValuation(PLPropertyCoverages propertyCoverages) {
        select_SquirePropertyCoveragesCoverageCValuationMethod().selectByVisibleText(propertyCoverages.getCoverageC().getValuationMethod().getValue());
    }


    public void checkStandardFireCoverageC(boolean trueFalse) {
        
        checkbox_StandardFireCoverageC().select(trueFalse);
        

    }


    public void checkOptionalCoverageC(boolean trueFalse) {
        
        checkbox_SquirePropertyCoveragesOptionalCoverageC().select(trueFalse);
        
    }


    public void selectOtherCoverageCValuation(ValuationMethod cov) {
        
        select_SquirePropertyCoveragesOtherCoverageValuation().selectByVisibleTextPartial(cov.getValue());
        
    }

    ///Coverage E Methods///


    public void setCoverageELimit(double limit) {
        clickWhenClickable(editbox_SquirePropertyCoveragesCoverageELimit);
        editbox_SquirePropertyCoveragesCoverageELimit.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_SquirePropertyCoveragesCoverageELimit.sendKeys(String.valueOf(limit));
        editbox_SquirePropertyCoveragesCoverageELimit.sendKeys(Keys.TAB);
        
    }


    public String getCoverageECoverageTypeValue() {
        return select_SquirePropertyCoveragesCoverageECoverageType().getText();
    }


    public void setCoverageEValuation(ValuationMethod method) {
        
        select_SquirePropertyCoveragesCoverageEValuationMethod().selectByVisibleText(method.getValue());
        
    }


    public void setCoverageECoverageType(CoverageType type) {
        
        select_SquirePropertyCoveragesCoverageECoverageType().selectByVisibleText(type.getValue());
        
    }


    public boolean isCoverageECoverageTypeEditable() {
        try {
            return select_SquirePropertyCoveragesCoverageECoverageType().isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    ///Optional Coverage Methods///

    private void clickEarthquake() {
        clickWhenClickable(checkbox_ExclusionCondition("Earthquake"));
    }


    public void setIncludeMasonry(boolean yesNo) {
        clickEarthquake();
        
        radio_IncludeMasonry().select(yesNo);
        
    }


    public String getSewageBackupSystemLimit() {
        return text_SquirePropertyCoveragesSewageBackupLimit.getText();
    }


    public boolean checkIfSewageSystemBackupExists() {
        return checkIfElementExists("//fieldset[contains(., 'Sewage System Backup') and contains(., 'Limit') and contains(., '10% - ')]", 2000);
    }


    public void setSewageSystemBackup(boolean trueFalse) {
        
        waitUntilElementIsVisible(find(By.xpath("//fieldset[contains(., 'Sewage System Backup')]")));
        checkbox_SquirePropertyCoveragesSewageSystemBackup().select(trueFalse);
    }


    public boolean getSewageSystemBackup() {
        
        waitUntilElementIsVisible(text_SquirePropertyCoveragesSewageSystemBackup);
        return checkbox_SquirePropertyCoveragesSewageSystemBackup().isSelected();
    }

    ///Miscellaneous Coverages Methods///


    public void checkGunsMiscellaneousCoverages(boolean trueFalse) {
        if ((trueFalse && !checkbox_OptionalCoverages("Guns").isSelected()) || (!trueFalse && checkbox_OptionalCoverages("Guns").isSelected()))
            clickWhenClickable(checkbox_OptionalCoverages("Guns"));
        
    }


    public void setGunsLimit(double limit) {
        clickWhenClickable(editbox_SquirePropertyCoverageGuns_Limit);
        editbox_SquirePropertyCoverageGuns_Limit.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_SquirePropertyCoverageGuns_Limit.sendKeys(String.valueOf(limit));
        editbox_SquirePropertyCoverageGuns_Limit.sendKeys(Keys.TAB);
        
    }


    public void setGunIncreasedTheftLimit(double limit) {
        clickWhenClickable(editbox_SquirePropertyCoverageGunsIncreasedTheftLimit);
        editbox_SquirePropertyCoverageGunsIncreasedTheftLimit.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_SquirePropertyCoverageGunsIncreasedTheftLimit.sendKeys(String.valueOf(limit));
        editbox_SquirePropertyCoverageGunsIncreasedTheftLimit.sendKeys(Keys.TAB);
        
    }


    public void checkSilverwareMiscellaneousCoverages(boolean trueFalse) {
        if ((trueFalse && !checkbox_OptionalCoverages("Silverware").isSelected()) || (!trueFalse && checkbox_OptionalCoverages("Silverware").isSelected()))
            clickWhenClickable(checkbox_OptionalCoverages("Silverware"));
        
    }


    public void setSilverwareLimit(double limit) {
        clickWhenClickable(editbox_SquirePropertyCoverageSilverware_Limit);
        editbox_SquirePropertyCoverageSilverware_Limit.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_SquirePropertyCoverageSilverware_Limit.sendKeys(String.valueOf(limit));
        editbox_SquirePropertyCoverageSilverware_Limit.sendKeys(Keys.TAB);
        
    }

    public void setSilverwareIncreasedTheftLimit(double limit) {
        clickWhenClickable(editbox_SquirePropertyCoverageSilverwareIncreasedTheftLimit);
        editbox_SquirePropertyCoverageSilverwareIncreasedTheftLimit.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_SquirePropertyCoverageSilverwareIncreasedTheftLimit.sendKeys(String.valueOf(limit));
        editbox_SquirePropertyCoverageSilverwareIncreasedTheftLimit.sendKeys(Keys.TAB);
        
    }


    public void checkToolsMiscellaneousCoverages(boolean trueFalse) {
        if ((trueFalse && !checkbox_OptionalCoverages("Tools").isSelected()) || (!trueFalse && checkbox_OptionalCoverages("Tools").isSelected()))
            clickWhenClickable(checkbox_OptionalCoverages("Tools"));
        
    }


    public void setToolsLimit(double limit) {
        clickWhenClickable(editbox_SquirePropertyCoverageTools_Limit);
        editbox_SquirePropertyCoverageTools_Limit.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_SquirePropertyCoverageTools_Limit.sendKeys(String.valueOf(limit));
        editbox_SquirePropertyCoverageTools_Limit.sendKeys(Keys.TAB);
        
    }

    public void setToolsIncreasedLimit(double limit) {
        clickWhenClickable(editbox_SquirePropertyCoverageToolsIncreasedLimit);
        editbox_SquirePropertyCoverageToolsIncreasedLimit.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_SquirePropertyCoverageToolsIncreasedLimit.sendKeys(String.valueOf(limit));
        editbox_SquirePropertyCoverageToolsIncreasedLimit.sendKeys(Keys.TAB);
        
    }


    public void checkSaddlesTackMiscellaneousCoverages(boolean trueFalse) {
        if ((trueFalse && !checkbox_OptionalCoverages("Saddles and Tack").isSelected()) || (!trueFalse && checkbox_OptionalCoverages("Saddles and Tack").isSelected()))
            clickWhenClickable(checkbox_OptionalCoverages("Saddles and Tack"));
        
    }


    public void setSaddlesTackLimit(double limit) {
        clickWhenClickable(editbox_SquirePropertyCoverageSaddlesTack_Limit);
        editbox_SquirePropertyCoverageSaddlesTack_Limit.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_SquirePropertyCoverageSaddlesTack_Limit.sendKeys(String.valueOf(limit));
        editbox_SquirePropertyCoverageSaddlesTack_Limit.sendKeys(Keys.TAB);
        
    }

    public void setSaddlesTackIncreasedLimit(double limit) {
        clickWhenClickable(editbox_SquirePropertyCoverageSaddlesTackIncreasedLimit);
        editbox_SquirePropertyCoverageSaddlesTackIncreasedLimit.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_SquirePropertyCoverageSaddlesTackIncreasedLimit.sendKeys(String.valueOf(limit));
        editbox_SquirePropertyCoverageSaddlesTackIncreasedLimit.sendKeys(Keys.TAB);
        
    }

    ///Other Methods///


    public double getOtherStructuresLimit() {
        String otherStructuresLimitText = otherStructuresLimit.getText();
        return NumberUtils.getCurrencyValueFromElement(otherStructuresLimitText.substring(otherStructuresLimitText.indexOf("$")));

    }


    public double getCoverageBLossOfUse() {
        String coverageBLossofUse = CoverageBLossOfUse.getText();
        return NumberUtils.getCurrencyValueFromElement(coverageBLossofUse.substring(coverageBLossofUse.indexOf("$")));
    }


    public void setLossIncomeExtraExpense(double limit) {
        clickWhenClickable(checkbox_OptionalCoverages("Loss of Income & Extra Expense"));
        
        text_LossIncomeExtraExpense.sendKeys(String.valueOf(limit));
    }


    public void setEnds105(String description) {
        checkbox_ExclusionsAndConditions("Special Endorsement for Property 105").select(true);
        
        button_ExclusionsConditionsAdd("105").click();
        
        clickWhenClickable(divInput_ExclusionsConditionsEndorsement("105"));
        
        clickWhenClickable(textarea_ExclusionsConditionsEndorsement("105"));
        
        textarea_ExclusionsConditionsEndorsement("105").sendKeys(Keys.CONTROL + "a");
        
        textarea_ExclusionsConditionsEndorsement("105").sendKeys(description);
        
        textarea_ExclusionsConditionsEndorsement("105").sendKeys(Keys.TAB);
        
    }


    public void setEnds205(String description) {
        
        checkbox_ExclusionsAndConditions("Special Endorsement for Liability 205").select(true);
        
        button_ExclusionsConditionsAdd("205").click();
        
        clickWhenClickable(divInput_ExclusionsConditionsEndorsement("205"));
        
        clickWhenClickable(textarea_ExclusionsConditionsEndorsement("205"));
        
        textarea_ExclusionsConditionsEndorsement("205").sendKeys(Keys.CONTROL + "a");
        
        textarea_ExclusionsConditionsEndorsement("205").sendKeys(description);
        
        textarea_ExclusionsConditionsEndorsement("205").sendKeys(Keys.TAB);
        

    }


    public boolean checkAddGlassCoverageExists() {
        try {
            waitUntilElementIsVisible(label_AddGlassCoverage, 1000);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public boolean checkAddWeightOfIceAndSnowCoverageExists() {
        try {
            waitUntilElementIsVisible(label_WeightOfIceAndSnowCoverage, 1000);
        } catch (Exception e) {
            return false;
        }
        return true;

    }


    public void setFarmPersonalPropertyRisk(String risk) {
        setText(Editbox_FarmPersonalPropertyRisk, risk);
    }


    public String getFarmPersonalPropertyRisk() {
        return Editbox_FarmPersonalPropertyRisk.getText();
    }


    public void clickFarmPersonalProperty() {
        
        clickWhenClickable(link_SquirePropertyCoveragesFarmPersonalProperty);
        
    }


    public void clickAdditionalInterest() {
        
        clickWhenClickable(link_SquirePropertyCoveragesAdditionalInterest);
        
    }


    public void clickSpecificBuilding(int loc, int blding) {
        clickWhenClickable(lineItem_SquirePropertyCoveragesBuildings(loc, blding));
    }


    public void clickSpecificBuilding(WebElement linkToClick) {
        clickWhenClickable(linkToClick);
    }


    public void clickSectionIICoveragesLink() {
        clickWhenClickable(link_SquirePropertyCoveragesSectionIICoverages);
    }


    public void clickCoveragesExclusionsAndConditions() {
        clickWhenClickable(link_SquirePropertyCoveragesExclusions);
        boolean isOnPage = !finds(By.xpath("//span[(@class='g-title') and (text()='Exclusions')]")).isEmpty();
        long endtime = new Date().getTime() + 5000;
        while (!isOnPage && new Date().getTime() < endtime) {
            
            isOnPage = !finds(By.xpath("//span[(@class='g-title') and (text()='Exclusions')]")).isEmpty();
        }
    }


    public void clickBuildingsExclusionsAndConditions() {
        clickWhenClickable(link_SquirePropertyCoveragesBuildingExclusions);
        
    }


    public void clickBuildingsCoverages() {
        clickWhenClickable(link_SquirePropertyCoveragesCoverages);
    }


    public void selectSectionIDeductible(SectionIDeductible deductible) {
        select_SquirePropertyCoveragesSectionIDeductible().selectByVisibleText(deductible.getValue());
    }


    public boolean isSectionIDeductibleEditable() {
        try {
            return select_SquirePropertyCoveragesSectionIDeductible().isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public void selectSectionIDeductibleGrandFatheredDropdownValues() {
        Guidewire8Select SectionIDeductible = select_SquirePropertyCoveragesSectionIDeductible();

        for (String listItem : SectionIDeductible.getList()) {
            if (listItem.equals("0") || listItem.equals("25") || listItem.equals("50") || listItem.equals("100") || listItem.equals("250"))
                Assert.fail("Expected Dropdown values don't match the Actual.");
        }
    }


    public void checkAccessYesEndorsement209(boolean yesno) {
        
        checkbox_AccessYesEndorsement209().select(yesno);
        
    }


    public boolean checkSewegeBackupSystem(boolean yesno) {
        try {
            
            checkbox_squirePropertyCoveragesSeweageBackupSystem().select(yesno);
            
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public void clickOk() {
        super.clickOK();
    }


    public void clickNext() {
        super.clickNext();
    }


    public void clickQuote() {
        super.clickGenericWorkorderQuote();
    }


    public void clickDetachedGarageEndorsement() {
        
        clickWhenClickable(checkbox_ExclusionCondition("Coverage A (Dwellings) Detached Garage and Storage Shed - 107"));
        
    }

    private void setSpecifiedPropertyExclusionEndorsement110Description(String description) {
        clickWhenClickable(div_SpecifiedPropertyExclusionEndorsement110Description, 5000);
        waitUntilElementIsClickable(editbox_SpecifiedPropertyExclusionEndorsement110Description, 5000);
        editbox_SpecifiedPropertyExclusionEndorsement110Description.sendKeys(description);
    }

    private void clickSpecifiedPropertyExclusionEndorsement110Add() {
        clickWhenClickable(button_SpecifiedPropertyExclusionEndorsement110Add);
    }


    public void clickSpecifiedProperty(ArrayList<String> descriptions) {
        
        clickWhenClickable(checkbox_ExclusionCondition(name110));

        for (String desc : descriptions) {
            clickSpecifiedPropertyExclusionEndorsement110Add();
            setSpecifiedPropertyExclusionEndorsement110Description(desc);
        }
    }


    public void clickCashValueLimitationForRoof() {
        
        clickWhenClickable(checkbox_ExclusionCondition("Actual Cash Value Limitation for Roofing - Endorsement 106"));
        
    }


    public void clickLimitedRoofCoverage() {
        
        clickWhenClickable(checkbox_ExclusionCondition("Limited Roof Coverage Endorsement - 134"));
        
    }


    public void clickReplacementCostDeletion() {
        
        clickWhenClickable(checkbox_ExclusionCondition("Replacement Cost Deletion Endorsement - 135"));
        
    }


    public void clickAccessYesEndorsement209() {
        
        clickWhenClickable(checkbox_ExclusionCondition("Access Yes Endorsement 209"));
        
    }


    public int getPropertyTableRowCount() {
        return tableUtils.getRowCount(table_CoveragePropertiesTable);
    }

    public String getPropertyTableSpecificColumnByRow(int rowNumber, String headerName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_CoveragePropertiesTable, rowNumber, headerName);
    }


    public String getSelectedBuildingName() {
        int row = tableUtils.getHighlightedRowNumber(div_Properties);
        return (tableUtils.getCellTextInTableByRowAndColumnName(div_Properties, row, "Property Type"));
    }


    public int getBuildingNumber(PropertyTypePL Property) {
        int row = tableUtils.getRowNumberInTableByText(div_Properties, Property.getValue());
        return row;
    }


    public void setOtherStructureDetachedGarageAdditionalValue(double value) {
        clickWhenClickable(editbox_SquirePropertyCoveragesOtherStructuresAdditionalValue);
        editbox_SquirePropertyCoveragesOtherStructuresAdditionalValue.sendKeys(String.valueOf(value));
        
    }


    public String getOtherStructureDetachedGarageValuationMethod() {
        return div_SquirePropertyCoveragesOtherStrctureValuationMethod.getText();
    }


    private void fillOutSectionI(GeneratePolicy policy) {
        selectSectionIDeductible(policy.squire.propertyAndLiability.section1Deductible);

        for (PolicyLocation loc : policy.squire.propertyAndLiability.locationList) {
            for (PLPolicyLocationProperty prop : loc.getPropertyList()) {
                clickSpecificBuilding(loc.getNumber(), prop.getPropertyNumber());
                

                if (prop.getpropertyType().equals(PropertyTypePL.DwellingPremisesCovE) || prop.getpropertyType().equals(PropertyTypePL.ResidencePremisesCovE) || prop.getpropertyType().equals(PropertyTypePL.VacationHomeCovE) || prop.getpropertyType().equals(PropertyTypePL.CondominiumDwellingPremisesCovE) || prop.getpropertyType().equals(PropertyTypePL.DwellingUnderConstructionCovE) || prop.getpropertyType().equals(PropertyTypePL.CondoVacationHomeCovE) || prop.getpropertyType().equals(PropertyTypePL.FarmOffice) || prop.getpropertyType().equals(PropertyTypePL.BunkHouse) || prop.getpropertyType().equals(PropertyTypePL.Barn) || prop.getpropertyType().equals(PropertyTypePL.Shed) || prop.getpropertyType().equals(PropertyTypePL.DairyComplex)) {
                    setCoverageELimit(prop.getPropertyCoverages().getCoverageE().getLimit());
                    setCoverageECoverageType(prop.getPropertyCoverages().getCoverageE().getCoverageType());
                    

                    if (!prop.getpropertyType().equals(PropertyTypePL.DwellingUnderConstructionCovE) && !prop.getpropertyType().equals(PropertyTypePL.Barn) && !prop.getpropertyType().equals(PropertyTypePL.Shed) && !prop.getpropertyType().equals(PropertyTypePL.DairyComplex))
                        setCoverageCLimit(prop.getPropertyCoverages().getCoverageE().getLimit());

                    if (prop.getpropertyType().equals(PropertyTypePL.FarmOffice) && !prop.getPropertyCoverages().getCoverageE().getCoverageType().equals(CoverageType.Peril_1)) {
                        setCoverageEValuation(ValuationMethod.ActualCashValue);
                    }

                    if (prop.getpropertyType().equals(PropertyTypePL.FarmOffice) || prop.getpropertyType().equals(PropertyTypePL.BunkHouse) || prop.getpropertyType().equals(PropertyTypePL.DwellingPremisesCovE) || prop.getpropertyType().equals(PropertyTypePL.ResidencePremisesCovE) || prop.getpropertyType().equals(PropertyTypePL.CondominiumDwellingPremisesCovE) || prop.getpropertyType().equals(PropertyTypePL.CondoVacationHomeCovE) || prop.getpropertyType().equals(PropertyTypePL.VacationHomeCovE)) {
                        setCoverageCLimit(prop.getPropertyCoverages().getCoverageE().getLimit());
                        setCoverageCValuation(prop.getPropertyCoverages());
                    }

                } else if (prop.getpropertyType().equals(PropertyTypePL.DetachedGarage)) {
                    setOtherStructureDetachedGarageAdditionalValue(prop.getPropertyCoverages().getCoverageE().getLimit());
                } else {
                    setCoverageELimit(prop.getPropertyCoverages().getCoverageE().getLimit());

                    //Requirement: Not available if (Property Type = Dwelling Under Construction) or (Property Class = C or D) - Nagamani added
                    if (!prop.getpropertyType().equals(PropertyTypePL.DwellingUnderConstruction) && (prop.getYearBuilt() > 1954 && !prop.getConstructionType().equals(ConstructionTypePL.MobileHome))) {
                        setCoverageAIncreasedReplacementCost(prop.getPropertyCoverages().getCoverageA().isIncreasedReplacementCost());
                    }

                    //Requirement: Not available if Construction Type = Mobile and Foundation Type = None - Nagamani Added
                    //Requirement: Also no available if Property Type = Condominium Residence Premises - rlonardo
                    if (!prop.getConstructionType().equals(ConstructionTypePL.MobileHome) && !prop.getFoundationType().equals(FoundationType.None) && !prop.getpropertyType().equals(PropertyTypePL.CondominiumDwellingPremises) && !prop.getpropertyType().equals(PropertyTypePL.CondominiumResidencePremise) && !prop.getpropertyType().equals(PropertyTypePL.DwellingUnderConstruction)) {
                        setCoverageAValuation(prop.getPropertyCoverages().getCoverageA().getValuationMethod());
                    }

                    sendArbitraryKeys(Keys.TAB);
                    waitForPostBack();
                    if (!prop.getPropertyCoverages().getCoverageA().getValuationMethod().equals(ValuationMethod.ActualCashValue))
                        setCoverageACoverageType(prop.getPropertyCoverages().getCoverageA().getCoverageType());

                    try {
                        setCoverageCValuation(prop.getPropertyCoverages());
                    } catch (Exception e) {
                    }

                    if (!prop.getPropertyCoverages().getCoverageC().getValuationMethod().equals(ValuationMethod.ActualCashValue) && (prop.getYearBuilt() > 1954 && !prop.getConstructionType().equals(ConstructionTypePL.MobileHome))) {
                        selectCoverageCCoverageType(prop.getPropertyCoverages().getCoverageC().getCoverageType());
                    }


                    if (prop.getPropertyCoverages().getCoverageC().getAdditionalValue() != 0.00) {
                        setCoverageCAdditionalValue(prop.getPropertyCoverages().getCoverageC().getAdditionalValue());
                    }//END IF
                }//END ELSE

                

                clickBuildingsExclusionsAndConditions();

                clickBuildingsCoverages();
            }//END FOR
        }//END FOR
    }// end fillOutSectionI(GeneratePolicy policy)

    private void fillOutFPP(GeneratePolicy policy) throws GuidewireException {

        repository.pc.workorders.generic.GenericWorkorderSquireFarmPersonalProperty fppCovs = new repository.pc.workorders.generic.GenericWorkorderSquireFarmPersonalProperty(driver);
        if (policy.squire.propertyAndLiability.squireFPP.getNonOwnedEquipmentCheck()) {
            clickFarmPersonalProperty();
            fppCovs.selectOptionalCoverageType(FPPOptionalCoverages.NonOwnerEquipment);
            fppCovs.setAdditionalCoveragesLimit(FPPOptionalCoverages.NonOwnerEquipment, policy.squire.propertyAndLiability.squireFPP.getNonOwnedEquipmentLimit());
        }

        if ((policy.squire.isFarmAndRanch() || policy.squire.isCountry()) && !policy.squire.propertyAndLiability.squireFPP.getNonOwnedEquipmentCheck()) {
            if (policy.squire.propertyAndLiability.squireFPP != null) {
                if (!policy.squire.propertyAndLiability.squireFPP.getItems().isEmpty()) {
                    clickFarmPersonalProperty();

                    fppCovs.checkCoverageD(true);
                    
                    fppCovs.selectCoverageType(policy.squire.propertyAndLiability.squireFPP.getCoverageType());
                    fppCovs.selectDeductible(policy.squire.propertyAndLiability.squireFPP.getDeductible());

                    fppCovs.clickFPPAdditionalInterests();
                    GenericWorkorderSquireFPPAdditionalInterest fppInts = new GenericWorkorderSquireFPPAdditionalInterest(driver);
                    if (policy.squire.propertyAndLiability.squireFPP.getAdditionalInterests() != null && policy.squire.propertyAndLiability.squireFPP.getAdditionalInterests().size() > 0) {
                        for (AdditionalInterest ai : policy.squire.propertyAndLiability.squireFPP.getAdditionalInterests()) {
                            fppInts.addInterest(policy.basicSearch, ai);
                        }//END FOR
                    }//END IF

                    fppInts.clickFPPCoverages();
                    fppCovs = new GenericWorkorderSquireFarmPersonalProperty(driver);

                    for (FarmPersonalPropertyTypes parentType : FarmPersonalPropertyTypes.values()) {
                        if (policy.squire.propertyAndLiability.squireFPP.getItems(parentType).size() > 0) {
                            fppCovs.selectCoverages(parentType);

                            ArrayList<SquireFPPTypeItem> allItems = policy.squire.propertyAndLiability.squireFPP.getItems(parentType);
                            for (SquireFPPTypeItem item : allItems) {
                                fppCovs.addItem(item.getType(), policy.squire.propertyAndLiability.squireFPP.getTotalNumItemsPerSubType(item.getType()), policy.squire.propertyAndLiability.squireFPP.getTotalValuePerSubType(item.getType()), item.getDescription());
                            }//END FOR

                            fppCovs.clickFPPAdditionalInterests();
                            fppInts = new GenericWorkorderSquireFPPAdditionalInterest(driver);

                            for (SquireFPPTypeItem item : policy.squire.propertyAndLiability.squireFPP.getItemsWithAdditionalInterests(parentType)) {
                                fppInts.addItem(item.getType(), item.getDescription(), item.getValue(), item.getSerialNumber(), item.getAdditionalInterest());
                            }//END FOR

                            fppInts.clickFPPCoverages();
                        }//END IF
                    }//END FOR
                }
            }
        }
    }//end fillOutFPP(GeneratePolicy policy)

    private void fillOutSectionII(GeneratePolicy policy) throws GuidewireNavigationException {
        clickSectionIICoveragesLink();

        GenericWorkorderSquirePropertyCoveragesSectionII section2Covs = new GenericWorkorderSquirePropertyCoveragesSectionII(driver);
        section2Covs.setGeneralLiabilityLimit(policy.squire.propertyAndLiability.liabilitySection.getGeneralLiabilityLimit());
        section2Covs.setMedicalLimit(policy.squire.propertyAndLiability.liabilitySection.getMedicalLimit());

        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages addCoverage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
        addCoverage.addCoveragesFromList(policy.squire.propertyAndLiability.liabilitySection.getSectionIICoverageList());
        addCoverage.setQuantityAndAmountFromList(policy.squire.propertyAndLiability.liabilitySection.getSectionIICoverageList());
        
    }


    public void fillOutPLPropertyLiabilityCoveragesAndExclusionsConditionsQQ(GeneratePolicy policy) throws Exception {

        fillOutSectionI(policy);

        fillOutFPP(policy);

        fillOutSectionII(policy);

        clickCoveragesExclusionsAndConditions();

        if (policy.squire.propertyAndLiability.propLiabExclusionsConditions != null) {
            GenericWorkorderSquirePropLiabUmbCoveragesExclusions excConds = new GenericWorkorderSquirePropLiabUmbCoveragesExclusions(driver);
            excConds.fillOutExclutionsAndConditions(policy);
        }//END IF
        clickGenericWorkorderSaveDraft();
    }//END fillOutPLPropertyLiabilityCoveragesAndExclusionsConditionsQQ


    public void editPLPropertyLiabilityCoveragesAndExclusionsConditionsFA(GeneratePolicy policy) throws Exception {
        repository.pc.sidemenu.SideMenuPC sidebar = new repository.pc.sidemenu.SideMenuPC(driver);
        sidebar.clickSideMenuSquirePropertyCoverages();

        for (PolicyLocation loc : policy.squire.propertyAndLiability.locationList) {
            for (PLPolicyLocationProperty prop : loc.getPropertyList()) {
                try {
                    clickSpecificBuilding(loc.getNumber(), prop.getPropertyNumber());
                } catch (Exception e) {
                    clickSpecificBuilding(1, prop.getPropertyNumber());
                }//END CATCH
                
                // TODO: Put any new full app stuff here
            }//END FOR
        }//END FOR

        if (policy.squire.propertyAndLiability.squireFPP != null && (policy.squire.isFarmAndRanch() || policy.squire.isCountry())) {
            clickFarmPersonalProperty();
            // TODO: Put any new full app stuff here
        }//END IF

        clickSectionIICoveragesLink();
        // TODO: Put any new full app stuff here

        clickCoveragesExclusionsAndConditions();

        if (policy.squire.propertyAndLiability.propLiabExclusionsConditions != null) {
            // GenericWorkorderSquirePropLiabUmbCoveragesExclusions excConds = new GenericWorkorderSquirePropLiabUmbCoveragesExclusions();
            // TODO: Put any new full app stuff here
        }//END IF
    }//END editPLPropertyLiabilityCoveragesAndExclusionsConditionsFA


    public void fillOutPLPropertyLiabilityCoveragesAndExclusionsConditionsStdFire(GeneratePolicy policy) throws Exception {
        repository.pc.sidemenu.SideMenuPC sidebar = new SideMenuPC(driver);
        sidebar.clickSideMenuSquirePropertyCoverages();

        //        if (!policy.standardFireAndLiability.getLocationList().get(0).getPropertyList().get(0).getpropertyType().equals(PropertyTypePL.TrellisedHops)) {
        if (!policy.standardFire.getLocationList().get(0).getPropertyList().get(0).getpropertyType().equals(PropertyTypePL.TrellisedHops)) {
            selectSectionIDeductible(policy.squire.propertyAndLiability.section1Deductible);
        }//END IF

        //        for (PolicyLocation loc : policy.standardFireAndLiability.getLocationList()) {
        for (PolicyLocation loc : policy.standardFire.getLocationList()) {
            for (PLPolicyLocationProperty prop : loc.getPropertyList()) {
                if (!prop.getpropertyType().equals(PropertyTypePL.TrellisedHops)) {
                    clickSpecificBuilding(loc.getNumber(), prop.getPropertyNumber());
                }//END IF
                
                // Any Std Fire will have this unless Peril 1 is selected
                policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.StdFire_AddCoverageTypePerilGreaterThan1to8);

                switch (prop.getpropertyType()) {
                    case GrainSeed:
                        setCoverageELimit(prop.getPropertyCoverages().getCoverageA().getLimit());
                        setCoverageECoverageType(prop.getPropertyCoverages().getCoverageE().getCoverageType());
                        break;
                    case HayStrawInOpen:
                    case HayStrawInStorage:
                    case TrellisedHops:
                        setCoverageELimit(prop.getPropertyCoverages().getCoverageA().getLimit());
                        break;
                    case SolarPanels:
                        setCoverageELimit(prop.getPropertyCoverages().getCoverageA().getLimit());
                        break;
                    case VacationHomeCovE:
                    case CondominiumDwellingPremisesCovE:
                        setCoverageELimit(prop.getPropertyCoverages().getCoverageA().getLimit());
                        setCoverageECoverageType(prop.getPropertyCoverages().getCoverageE().getCoverageType());
                        setCoverageCLimit(prop.getPropertyCoverages().getCoverageC().getAdditionalValue());
                        setCoverageCValuation(prop.getPropertyCoverages());
                        break;
                    case CondominiumVacationHome:
                        setCoverageALimit(prop.getPropertyCoverages().getCoverageA().getLimit());
                        setCoverageCLimit(prop.getPropertyCoverages().getCoverageC().getAdditionalValue());
                        setStdFireCoverageCValuation(prop.getPropertyCoverages());
                        break;
                    case CondoVacationHomeCovE:
                        setCoverageELimit(prop.getPropertyCoverages().getCoverageA().getLimit());
                        setCoverageECoverageType(prop.getPropertyCoverages().getCoverageE().getCoverageType());
                        setCoverageCLimit(prop.getPropertyCoverages().getCoverageC().getAdditionalValue());
                        setCoverageCValuation(prop.getPropertyCoverages());
                        break;
                    case Potatoes:
                        setCoverageELimit(prop.getPropertyCoverages().getCoverageA().getLimit());
                        setCoverageECoverageType(prop.getPropertyCoverages().getCoverageE().getCoverageType());
                        break;
                    case AlfalfaMill:
                        setCoverageELimit(prop.getPropertyCoverages().getCoverageA().getLimit());
                        setCoverageECoverageType(prop.getPropertyCoverages().getCoverageE().getCoverageType());
                        break;
                    case FarmOffice:
                        setCoverageELimit(prop.getPropertyCoverages().getCoverageA().getLimit());
                        setCoverageECoverageType(prop.getPropertyCoverages().getCoverageE().getCoverageType());

                        if (prop.getpropertyType().equals(PropertyTypePL.FarmOffice) && !prop.getPropertyCoverages().getCoverageE().getCoverageType().equals(CoverageType.Peril_1)) {
                            setCoverageEValuation(ValuationMethod.ActualCashValue);
                        }
                        //PLEASE ADD THE CORRECT LOGIC TO FIX THIS
                        //					try {
                        setCoverageCLimit(prop.getPropertyCoverages().getCoverageC().getAdditionalValue());
                        setCoverageCValuation(prop.getPropertyCoverages());
                        //					} catch (Exception e) {}
                        break;
                    case BunkHouse:
                        setCoverageELimit(prop.getPropertyCoverages().getCoverageA().getLimit());
                        setCoverageECoverageType(prop.getPropertyCoverages().getCoverageE().getCoverageType());
                        setCoverageCLimit(prop.getPropertyCoverages().getCoverageC().getAdditionalValue());
                        setCoverageCValuation(prop.getPropertyCoverages());
                        break;
                    case DwellingPremisesCovE:
                        setCoverageELimit(prop.getPropertyCoverages().getCoverageA().getLimit());
                        setCoverageECoverageType(prop.getPropertyCoverages().getCoverageE().getCoverageType());
                        setCoverageCLimit(prop.getPropertyCoverages().getCoverageC().getAdditionalValue());
                        selectOtherCoverageCValuation(prop.getPropertyCoverages().getCoverageC().getValuationMethod());
                        break;
                    case DwellingPremises:
                        setCoverageALimit(prop.getPropertyCoverages().getCoverageA().getLimit());
                        setCoverageCLimit(prop.getPropertyCoverages().getCoverageC().getAdditionalValue());
                        setCoverageCValuation(prop.getPropertyCoverages());
                        break;
                    case VacationHome:
                        setCoverageALimit(prop.getPropertyCoverages().getCoverageA().getLimit());
                        setCoverageCLimit(prop.getPropertyCoverages().getCoverageC().getAdditionalValue());
                        setCoverageCValuation(prop.getPropertyCoverages());
                        break;
                    default:
                        setCoverageALimit(prop.getPropertyCoverages().getCoverageA().getLimit());
                        setCoverageCLimit(prop.getPropertyCoverages().getCoverageC().getAdditionalValue());
                        setStdFireCoverageCValuation(prop.getPropertyCoverages());
                        break;
                }//EDN SWITCH
                clickBuildingsExclusionsAndConditions();
                // TODO: Set property level exclusionConditions here that the Agent is allowed to set
                clickBuildingsCoverages();
            }//END FOR
        }//END FOR
        clickCoveragesExclusionsAndConditions();

        
        clickGenericWorkorderSaveDraft();
    }//END fillOutPLPropertyLiabilityCoveragesAndExclusionsConditionsStdFire


}

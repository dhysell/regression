package repository.pc.workorders.generic;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.Vehicle.PAComprehensive_CollisionDeductible;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PARentalReimbursement;
import repository.gw.helpers.TableUtils;

public class GenericWorkorderSquirePACoverages extends GenericWorkorderSquireAutoCoverages {

    private WebDriver driver;
    private TableUtils tableUtils;
    
    public GenericWorkorderSquirePACoverages(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    public void fillOutAutoCoverages_QQ(GeneratePolicy policy) {
        new repository.pc.workorders.generic.GenericWorkorderSquirePACoverages_Coverages(driver).fillOutCoverages_QQ(policy);
        new repository.pc.workorders.generic.GenericWorkorderSquirePACoverages_AdditionalCoverages(driver).fillOutAdditionalCoverages_QQ(policy);
        new repository.pc.workorders.generic.GenericWorkorderSquirePACoverages_ExclusionsAndConditions(driver).fillOutExclusionsAndConditions_QQ(policy);
    }

    public void fillOutAutoCoverages_FA(GeneratePolicy policy) {
        new repository.pc.workorders.generic.GenericWorkorderSquirePACoverages_Coverages(driver).fillOutCoverages_FA(policy);
        new repository.pc.workorders.generic.GenericWorkorderSquirePACoverages_AdditionalCoverages(driver).fillOutAdditionalCoverages_FA(policy);
        new repository.pc.workorders.generic.GenericWorkorderSquirePACoverages_ExclusionsAndConditions(driver).fillOutExclusionsAndConditions_FA(policy);
    }

    public void fillOutAutoCoverages(GeneratePolicy policy) {
        new GenericWorkorderSquirePACoverages_Coverages(driver).fillOutCoverages(policy);
        new GenericWorkorderSquirePACoverages_AdditionalCoverages(driver).fillOutAdditionalCoverages(policy);
        new GenericWorkorderSquirePACoverages_ExclusionsAndConditions(driver).fillOutExclusionsAndConditions(policy);
    }
    
    private void setSelectInFieldsetByText(String tableXpath, String textInTable, String textForSelect) {
		Guidewire8Select tableSelect = new Guidewire8Select(driver,tableXpath + "//legend//*[contains(text(),'" + textInTable	+ "')]/ancestor::fieldset//table[contains(@id, '-triggerWrap')]");
		tableSelect.selectByVisibleTextPartial(textForSelect);
	}
	
	private void setCheckboxInFieldsetByText(String tableXpath, String textInTable, boolean value) {
		Guidewire8Checkbox tableCheckbox = new Guidewire8Checkbox(driver,tableXpath + "//legend//*[contains(text(),'" + textInTable + "')]/ancestor::div/table[contains(@class, 'checkbox')]");
		tableCheckbox.select(value);
	}
    
    private void setEditboxInFieldsetByText(String tableXpath, String textInTable, String textForEditbox) {
		WebElement tableEditbox = find(By.xpath(tableXpath + "//legend//*[contains(text(),'" + textInTable	+ "')]/ancestor::fieldset//input[contains(@type, 'text')]"));
		tableEditbox.sendKeys(Keys.chord(Keys.CONTROL, "a"), textForEditbox);
	}

    // tables
    private String vehicleCoveragesTableXpath = "//div[contains(@id, 'PAPerVehiclePanelSet:VehicleCoverageDetailsCV:PersonalAuto_VehicleCoverageDetailDV')]/table";

    private String exclusionsTableXpath = "//div[contains(@id, 'policyLevelExclusionsAndConditions:GenericExclusionCondition_ExtPanelSet:ExclDV_ref')]/table";

    @FindBy(xpath = "//div[contains(@id, 'PersonalAutoScreen:PAPerVehiclePanelSet:VehicleCoveragesLV-body')]/div/table")
    private WebElement table_vehicleRows;

    @FindBy(xpath = "//span[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:PAWizardStepGroup:PersonalAutoScreen:PAPerVehiclePanelSet:VehicleCoverageDetailsCV:VehicleExclusionsConditionsCardTab-btnEl')]")
    private WebElement link_coverageExclusionAndConditions;


    @FindBy(xpath = "//a[contains(@id, 'PersonalAutoScreen:AdditionalInsuredTab')]")
    private WebElement tab_lineAdditionalInsured;

    @FindBy(xpath = "//a[contains(@id, 'VehicleCoverageDetailsCV:VehicleCoverageDetailsCardTab')]")
    private WebElement tab_vehicleCoverages;

    @FindBy(xpath = "//span[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:PAWizardStepGroup:PersonalAutoScreen:PersonalAutoCoveragesPanelSet:exclusionsAndConditionsCardTab-btnEl')]")
    private WebElement tab_vehicleExclusions;

    private Guidewire8Checkbox checkbox_GenericWorkorderSquirePACoveragesDeletionMaterialDamage() {
        return new Guidewire8Checkbox(driver,"//table[contains(@id,'SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:PAWizardStepGroup:PersonalAutoScreen:PAPerVehiclePanelSet:VehicleCoverageDetailsCV:GenericExclusionCondition_ExtPanelSet:ExclDV:1:LineExcl:CoverageInputSet:CovPatternInputGroup-legendChk')]");
    }


    @FindBy(xpath = "//a[contains(@id, 'SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:PAWizardStepGroup:PersonalAutoScreen:PAPerVehiclePanelSet:VehicleCoverageDetailsCV:GenericExclusionCondition_ExtPanelSet:ExclDV:1:LineExcl:CoverageInputSet:CovPatternInputGroup:Add-btnEl')]")
    private WebElement button_GenericWorkorderSquirePACoveragesAddDeletionofMaterialDamage;

    @FindBy(xpath = "//div[contains(@id, ':PAWizardStepGroup:PersonalAutoScreen:PAPerVehiclePanelSet:VehicleCoverageDetailsCV:GenericExclusionCondition_ExtPanelSet:ExclDV:1:LineExcl:CoverageInputSet:CovPatternInputGroup:0-body')]/descendant::table[1]/descendant::tr/td[5]/div")
    private WebElement text_GenericWorkorderSquirePACoveragesMaterialDamageExplanation;

    //    private WebElement button_AutoExclusionsConditionsAdd(String labelName) {
    //        return find(By.xpath("//div[contains(., '" + labelName + "')]/ancestor::fieldset//span[contains(.,'Add')]/parent::span"));
    //    }

    private WebElement div_AutoExclusionsConditionsEndorsementContainer(String labelName) {
        return find(By.xpath("//div[contains(., '" + labelName + "')]/ancestor::legend/following-sibling::div/span/div/div/table/tbody/tr/td/div"));
    }

    private WebElement divInput_AutoExclusionsConditionsEndorsement(String labelName) {
        return div_AutoExclusionsConditionsEndorsementContainer(labelName).findElement(By.xpath(".//div[3]/div/table/tbody/tr/td[2]/div"));
    }

    private WebElement textarea_AutoExclusionsConditionsEndorsement(String labelName) {
        return div_AutoExclusionsConditionsEndorsementContainer(labelName).findElement(By.xpath(".//div[4]/table/tbody/tr/td[2]/textarea"));
    }


    public void setDriverExclusion304(boolean hasCoverage) {
        
        setCheckboxInFieldsetByText(exclusionsTableXpath, "Driver Exclusion Endorsement 304", hasCoverage);
    }

    private String conditionsTableXpath = "//div[contains(@id, 'policyLevelExclusionsAndConditions:GenericExclusionCondition_ExtPanelSet:CondDV_ref')]/table";

    public void setAdditionalInsured361(boolean hasCoverage, String coverageDescription) {
        
        checkAndEnterText(conditionsTableXpath, "Additional Insured Endorsement 361", hasCoverage, coverageDescription);
    }

    private Guidewire8Checkbox specialEndorsementForAuto_305() {
        return new Guidewire8Checkbox(driver,"//div[contains(text(), 'Special Endorsement for Auto 305')]/ancestor::table");
    }

    private boolean checkSpecialEndorsementForAuto_305(boolean checked) {
        specialEndorsementForAuto_305().select(checked);
        return checked;
    }

    private void clickAddButton_SpecialEndorsemenetForAuto_305() {
        clickWhenClickable(find(By.xpath("//div[contains(., '305')]/ancestor::fieldset//span[contains(.,'Add')]/parent::span")));
    }

    public void fillOutSpecialEndorsementForAuto_305(GeneratePolicy policy) {
        if (checkSpecialEndorsementForAuto_305(policy.squire.squirePA.getCoverages().hasSpecialEndorsement_305())) {
            for (String description305 : policy.squire.squirePA.getCoverages().getSpecialEndorsementText()) {
                clickAddButton_SpecialEndorsemenetForAuto_305();
                
                clickWhenClickable(divInput_AutoExclusionsConditionsEndorsement("305"));
                
                clickWhenClickable(textarea_AutoExclusionsConditionsEndorsement("305"));
                
                textarea_AutoExclusionsConditionsEndorsement("305").sendKeys(Keys.CONTROL + "a");
                
                textarea_AutoExclusionsConditionsEndorsement("305").sendKeys(description305);
                
            }
        }
    }

    private void checkAndEnterText(String tableXpath, String coverage, boolean hasCoverage, String description) {
        setCheckboxInFieldsetByText(tableXpath, coverage, hasCoverage);
        
        if (hasCoverage) {
            setEditboxInFieldsetByText(tableXpath, coverage, description);
        }
    }


    public void setComprehensiveCoverage(boolean hasCoverage, PAComprehensive_CollisionDeductible compCoverage) {
        
        checkAndSelectCoverage(vehicleCoveragesTableXpath, "Comprehensive", hasCoverage, compCoverage.getValue());
    }


    public void setCollisionCoverage(boolean hasCoverage, PAComprehensive_CollisionDeductible collisionCoverage) {
        
        checkAndSelectCoverage(vehicleCoveragesTableXpath, "Collision", hasCoverage, collisionCoverage.getValue());
    }


    public void setRentalReimbursement(boolean hasCoverage, PARentalReimbursement rentalReimbursement) {
        
        checkAndSelectCoverage(vehicleCoveragesTableXpath, "Rental Reimbursement", hasCoverage,
                rentalReimbursement.getValue());
    }


    public void setAdditionalLiving(boolean hasCoverage) {
        
        setCheckboxInFieldsetByText(vehicleCoveragesTableXpath, "Additional Living Expense", hasCoverage);
    }


    public void setFireAndTheft(boolean hasCoverage) {
        
        setCheckboxInFieldsetByText(vehicleCoveragesTableXpath, "Fire & Theft", hasCoverage);
    }

    // private helpers
    private void checkAndSelectCoverage(String tableXpath, String coverage, boolean hasCoverage, String coverageAmount) {
        setCheckboxInFieldsetByText(tableXpath, coverage, hasCoverage);
        if (hasCoverage) {
            setSelectInFieldsetByText(tableXpath, coverage, coverageAmount);
        }
    }


    public void clickExclusionsAndConditions() {
        clickWhenClickable(tab_vehicleExclusions);
        
    }


    public void checkMaterialDamageExlusion() {
        
        checkbox_GenericWorkorderSquirePACoveragesDeletionMaterialDamage().select(true);
    }


    public void clickAddMaterialDamage() {
        super.clickAdd();
        
    }


    public void setMaterialDamage(String location, String damage, String damageDescription, String explanation) {
        checkMaterialDamageExlusion();
        clickAddMaterialDamage();
        
        tableUtils.selectValueForSelectInTable(
                find(By.xpath("//div[contains(@id, 'SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:PAWizardStepGroup:PersonalAutoScreen:PAPerVehiclePanelSet:VehicleCoverageDetailsCV:GenericExclusionCondition_ExtPanelSet:ExclDV:1:LineExcl:CoverageInputSet:CovPatternInputGroup:ScheduleLV')]")),
                1, "Location Of Damage", location);
        tableUtils.selectValueForSelectInTable(
                find(By.xpath("//div[contains(@id, 'SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:PAWizardStepGroup:PersonalAutoScreen:PAPerVehiclePanelSet:VehicleCoverageDetailsCV:GenericExclusionCondition_ExtPanelSet:ExclDV:1:LineExcl:CoverageInputSet:CovPatternInputGroup:ScheduleLV')]")),
                1, "Damaged Item", damage);
        tableUtils.selectValueForSelectInTable(
                find(By.xpath("//div[contains(@id, 'SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:PAWizardStepGroup:PersonalAutoScreen:PAPerVehiclePanelSet:VehicleCoverageDetailsCV:GenericExclusionCondition_ExtPanelSet:ExclDV:1:LineExcl:CoverageInputSet:CovPatternInputGroup:ScheduleLV')]")),
                1, "Damage Description", damageDescription);
        if (!explanation.isEmpty()
                || !text_GenericWorkorderSquirePACoveragesMaterialDamageExplanation.getText().equals("N/A")) {
            text_GenericWorkorderSquirePACoveragesMaterialDamageExplanation.click();
            text_GenericWorkorderSquirePACoveragesMaterialDamageExplanation.sendKeys(explanation);
        }
    }


    public void clickCoverageExclusionsAndConditions() {
        clickWhenClickable(link_coverageExclusionAndConditions);
        
    }


    public String getAuto305Text() {
        return find(By.xpath("//div[contains(.,'Special Endorsement for Auto 305')]/ancestor::legend/following-sibling::div/span/div/div/table/tbody/tr/td/div/div[2]/div/table/tbody/tr/td[2]/div")).getText();
    }


}

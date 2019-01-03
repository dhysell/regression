package repository.pc.workorders.generic;


import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.Building.*;
import repository.gw.enums.SpoilageLimit;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.TableUtils;

public class GenericWorkorderBuildingAdditionalCoverages extends BasePage {
	private WebDriver driver;
	private TableUtils tableUtils;
	
    public GenericWorkorderBuildingAdditionalCoverages(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    /////////////////////////
    ////// WebElements //////
    /////////////////////////


    private String returnCheckBoxTable(String coverage) {
        return "//div[contains(text(), '" + coverage + "')]/preceding-sibling::table";
    }

    private String returnSelectTable(String coverage) {
        return "//label[contains(text(), '" + coverage + "')]/parent::td/following-sibling::td[1]/table";
    }

    public Guidewire8Checkbox checkbox_EquipmentBreakdown() {
        return new Guidewire8Checkbox(driver, returnCheckBoxTable("Equipment Breakdown Enhancement Endorsement IDBP 31 1002"));
    }


    public Guidewire8Checkbox checkbox_BusinessIncome() {
        return new Guidewire8Checkbox(driver, returnCheckBoxTable("Business Income - Ordinary Payroll"));
    }

    public Guidewire8Select select_BusinessIncome() {
        return new Guidewire8Select(driver, returnSelectTable("Business Income - Ordinary Payroll"));
    }

    public Guidewire8Checkbox checkbox_DiscretionaryPayroll() {
        return new Guidewire8Checkbox(driver, returnCheckBoxTable("Discretionary Payroll Expense BP 14 30"));
    }

    public Guidewire8Select select_DiscretionaryPayrollNumDays() {
        return new Guidewire8Select(driver, returnSelectTable("Discretionary Payroll Number of Days"));
    }

    @FindBy(xpath = "//fieldset[contains(., 'Discretionary Payroll Expense BP 14 30')]/div//td[.='Annual Discretionary Payroll Amount']/following-sibling::td/input[contains(@id, ':CovPatternInputGroup:BOPBldAnnualDiscretionaryPayrollAmt-inputEl')]")
    public WebElement editbox_AnnualDiscretionaryPayrollAmount;

    @FindBy(xpath = "//fieldset[contains(., 'Discretionary Payroll Expense BP 14 30')]/div//td[.='Job Classification or Name of Employees']/following-sibling::td/textarea[contains(@id, ':CoverageInputSet:CovPatternInputGroup:BOPBldDiscPayJobClass-inputEl')]")
    public WebElement editbox_JobClassificationEmployeenames;

    public Guidewire8Checkbox checkbox_LossOfRentalValue() {
        return new Guidewire8Checkbox(driver, returnCheckBoxTable("Loss of Rental Value - Landlord as Designated Payee BP 05 93"));
    }

    @FindBy(xpath = "//input[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:AdditionalCoveragesFBPanelSet:BuildingAdditionalCoveragesPanelSet:BuildingBOPIncomeExpenseCatPanel:BOPBuildingAdditionalCoverageBOPIncomeExpenseCatDV:2:CoverageInputSet:CovPatternInputGroup:0:CovTermInputSet:DirectTermInput-inputEl')]")
    public WebElement editbox_LimitOfInsurance;

    @FindBy(xpath = "//input[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:AdditionalCoveragesFBPanelSet:BuildingAdditionalCoveragesPanelSet:BuildingBOPIncomeExpenseCatPanel:BOPBuildingAdditionalCoverageBOPIncomeExpenseCatDV:2:CoverageInputSet:CovPatternInputGroup:1:CovTermInputSet:StringTermInput-inputEl')]")
    public WebElement editbox_PersonOrOrgonizationName;

    @FindBy(xpath = "//input[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:AdditionalCoveragesFBPanelSet:BuildingAdditionalCoveragesPanelSet:BuildingBOPIncomeExpenseCatPanel:BOPBuildingAdditionalCoverageBOPIncomeExpenseCatDV:2:CoverageInputSet:CovPatternInputGroup:2:CovTermInputSet:StringTermInput-inputEl')]")
    public WebElement editbox_AddressOfPersonOrOrganization;

    @FindBy(xpath = "//input[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:AdditionalCoveragesFBPanelSet:BuildingAdditionalCoveragesPanelSet:BuildingBOPIncomeExpenseCatPanel:BOPBuildingAdditionalCoverageBOPIncomeExpenseCatDV:2:CoverageInputSet:CovPatternInputGroup:3:CovTermInputSet:StringTermInput-inputEl')]")
    public WebElement editbox_CityStateAndPostalCode;

    public Guidewire8Checkbox checkbox_AccountsReceivable() {
        return new Guidewire8Checkbox(driver, returnCheckBoxTable("Accounts Receivable - Optional Coverage"));
    }

    @FindBy(xpath = "//fieldset[contains(., 'Accounts Receivable - Optional Coverage')]/div//td[.='AR On Premises Limit']/following-sibling::td/input[contains(@id, 'DirectTermInput-inputEl')]")
    public WebElement editbox_AROnPremises;

    public Guidewire8Checkbox checkbox_CondoCommercialCoverage() {
        return new Guidewire8Checkbox(driver, returnCheckBoxTable("Condominium Commercial Unit-Owners Optional Coverage BP 17 03"));
    }

    public Guidewire8Select select_LossAssessmentLimit() {
        return new Guidewire8Select(driver, returnSelectTable("Loss Assessment Limit"));
    }

    @FindBy(xpath = "//fieldset[contains(., 'Condominium Commercial Unit-Owners Optional Coverage BP 17 03')]/div//td[.='Miscellaneous Real Property']/following-sibling::td/input[contains(@id, 'DirectCovTermInputSet:DirectTermInput-inputEl')]")
    public WebElement editbox_MiscRealProperty;

    public Guidewire8Checkbox checkbox_FoodContamination() {
        return new Guidewire8Checkbox(driver, returnCheckBoxTable("Food Contamination Coverage BP 04 31"));
    }

    @FindBy(xpath = "//fieldset[contains(., 'Food Contamination Coverage BP 04 31')]/div//td[.='Food Contamination Limit']/following-sibling::td/input[contains(@id, 'DirectCovTermInputSet:DirectTermInput-inputEl')]")
    public WebElement editbox_FoodContaminationLimit;

    @FindBy(xpath = "//fieldset[contains(., 'Food Contamination Coverage BP 04 31')]/div//td[.='Food Contamination Adv. Limit']/following-sibling::td/input[contains(@id, ':CovTermInputSet:DirectCovTermInputSet:DirectTermInput-inputEl')]")
    public WebElement editbox_FoodContaminationAdvLimit;

    public Guidewire8Checkbox checkbox_OrdinanceOrLaw() {
        return new Guidewire8Checkbox(driver, returnCheckBoxTable("Ordinance or Law BP 04 46"));
    }

    public Guidewire8RadioButton radio_BusinessIncomeExpenceCoverage() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:AdditionalCoveragesFBPanelSet:BuildingAdditionalCoveragesPanelSet:BuildingBuildingOtherCatPanel:BOPBuildingAdditionalCoverageBuildingOtherCatDV:3:CoverageInputSet:CovPatternInputGroup:BOPOrdLawIncomeExpense')]");
    }

    public Guidewire8Select select_LossToTheUndamagedPartOfBuilding() {
        return new Guidewire8Select(driver, returnSelectTable("Loss To The Undamaged part of the Bldg"));
    }

    @FindBy(xpath = "//input[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:AdditionalCoveragesFBPanelSet:BuildingAdditionalCoveragesPanelSet:BuildingBuildingOtherCatPanel:BOPBuildingAdditionalCoverageBuildingOtherCatDV:3:CoverageInputSet:CovPatternInputGroup:LawCov2-inputEl')]")
    public WebElement editbox_DemolitionCostLimit;

    @FindBy(xpath = "//input[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:AdditionalCoveragesFBPanelSet:BuildingAdditionalCoveragesPanelSet:BuildingBuildingOtherCatPanel:BOPBuildingAdditionalCoverageBuildingOtherCatDV:3:CoverageInputSet:CovPatternInputGroup:LawCov3-inputEl')]")
    public WebElement editbox_IncrCostOfConstruction;

    @FindBy(xpath = "//input[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:AdditionalCoveragesFBPanelSet:BuildingAdditionalCoveragesPanelSet:BuildingBuildingOtherCatPanel:BOPBuildingAdditionalCoverageBuildingOtherCatDV:3:CoverageInputSet:CovPatternInputGroup:LawCov23-inputEl')]")
    public WebElement editbox_DemoIncrCostOfConstructionComboLimit;


    public Guidewire8Checkbox checkbox_OutdoorProperty() {
        return new Guidewire8Checkbox(driver, returnCheckBoxTable("Outdoor Property"));
    }

    @FindBy(xpath = "//input[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:AdditionalCoveragesFBPanelSet:BuildingAdditionalCoveragesPanelSet:BuildingBuildingOtherCatPanel:BOPBuildingAdditionalCoverageBuildingOtherCatDV:4:CoverageInputSet:CovPatternInputGroup:0:CovTermInputSet:DirectTermInput-inputEl')]")
    public WebElement editbox_OutdoorInsuranceLimit;

    public Guidewire8Checkbox checkbox_Spoilage() {
        return new Guidewire8Checkbox(driver, returnCheckBoxTable("Spoilage BP 04 15"));
    }

    @FindBy(xpath = "//input[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:AdditionalCoveragesFBPanelSet:BuildingAdditionalCoveragesPanelSet:BuildingBuildingOtherCatPanel:BOPBuildingAdditionalCoverageBuildingOtherCatDV:6:CoverageInputSet:CovPatternInputGroup:BOPSpoilageDescription-inputEl')]")
    public WebElement editbox_ProductDescription;

    @FindBy(xpath = "//input[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:AdditionalCoveragesFBPanelSet:BuildingAdditionalCoveragesPanelSet:BuildingBuildingOtherCatPanel:BOPBuildingAdditionalCoverageBuildingOtherCatDV:6:CoverageInputSet:CovPatternInputGroup:BOPSpoilageCovLimit-inputEl')]")
    public WebElement editbox_SpoilageLimit;

    public Guidewire8Select select_SpoilageDeductible() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Spoilage BP 04 15')]/ancestor::legend/following-sibling::div/span/div/child::table/descendant::label[contains(text(), 'Deductible')]/parent::td/following-sibling::td/table");
    }

    public Guidewire8RadioButton radio_BreakdownOrContamination() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:AdditionalCoveragesFBPanelSet:BuildingAdditionalCoveragesPanelSet:BuildingBuildingOtherCatPanel:BOPBuildingAdditionalCoverageBuildingOtherCatDV:6:CoverageInputSet:CovPatternInputGroup:BOPSpoilageCovBreakContam')]");
    }

    public Guidewire8RadioButton radio_PowerOutage() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:AdditionalCoveragesFBPanelSet:BuildingAdditionalCoveragesPanelSet:BuildingBuildingOtherCatPanel:BOPBuildingAdditionalCoverageBuildingOtherCatDV:6:CoverageInputSet:CovPatternInputGroup:BOPSpoilageCovPowerOutage')]");
    }

    public Guidewire8RadioButton radio_RefrigerationMaintAgreement() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:AdditionalCoveragesFBPanelSet:BuildingAdditionalCoveragesPanelSet:BuildingBuildingOtherCatPanel:BOPBuildingAdditionalCoverageBuildingOtherCatDV:6:CoverageInputSet:CovPatternInputGroup:BOPSpoilageCovFridgeMaintenance')]");
    }

    @FindBy(xpath = "//div[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:AdditionalCoveragesFBPanelSet:BuildingAdditionalCoveragesPanelSet:BuildingBuildingOtherCatPanel:BOPBuildingAdditionalCoverageBuildingOtherCatDV:6:CoverageInputSet:CovPatternInputGroup:RiskClassCode:SelectRiskClass')]")
    public WebElement search_RiskDescription;

    public Guidewire8Checkbox checkbox_ValuablePapers() {
        return new Guidewire8Checkbox(driver, returnCheckBoxTable("Valuable Papers - Optional Coverage"));
    }

    @FindBy(xpath = "//fieldset[contains(., 'Valuable Papers - Optional Coverage')]/div//td[.='Papers On Premises Limit']/following-sibling::td/input[contains(@id, ':CovTermInputSet:DirectCovTermInputSet:DirectTermInput-inputEl')]")
    public WebElement editbox_PapersOnPremisesLimit;

    public Guidewire8Checkbox checkbox_UtilitiesDirectDamage() {
        return new Guidewire8Checkbox(driver, returnCheckBoxTable("Utilities - Direct Damage BP 04 56"));
    }

    public Guidewire8Select select_UtilsDirectCoverageAppliesTo() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Utilities - Direct Damage BP 04 56')]/ancestor::legend/following-sibling::div/span/div/child::table/descendant::label[contains(text(), 'Coverage Applies To')]/parent::td/following-sibling::td/table");
    }

    public Guidewire8Select select_UtilsDirectUtilityIs() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Utilities - Direct Damage BP 04 56')]/ancestor::legend/following-sibling::div/span/div/child::table/descendant::label[contains(text(), 'Utility is')]/parent::td/following-sibling::td/table");
    }

    @FindBy(xpath = "//div[contains(text(), 'Utilities - Direct Damage BP 04 56')]/ancestor::legend/following-sibling::div/span/div/child::table/descendant::label[contains(text(), 'Direct Loss Limit')]/parent::td/following-sibling::td/input")
    public WebElement editbox_UtilsDirectDamDirectLossLimit;

    public Guidewire8Select select_UtilsDirectWaterSupply() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Utilities - Direct Damage BP 04 56')]/ancestor::legend/following-sibling::div/span/div/child::table/descendant::label[contains(text(), 'Water Supply')]/parent::td/following-sibling::td/table");
    }

    public Guidewire8Select select_UtilsDirctCommunicationsNotOH() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Utilities - Direct Damage BP 04 56')]/ancestor::legend/following-sibling::div/span/div/child::table/descendant::label[contains(text(), 'Communications (not O/H lines)')]/parent::td/following-sibling::td/table");
    }

    public Guidewire8Select select_UtilsDirectCommunicationsOH() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Utilities - Direct Damage BP 04 56')]/ancestor::legend/following-sibling::div/span/div/child::table/descendant::label[contains(text(), 'Communications (inc O/H lines)')]/parent::td/following-sibling::td/table");
    }

    public Guidewire8Select select_UtilsDirectPowerNotOH() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Utilities - Direct Damage BP 04 56')]/ancestor::legend/following-sibling::div/span/div/child::table/descendant::label[contains(text(), 'Power (not O/H lines)')]/parent::td/following-sibling::td/table");
    }

    public Guidewire8Select select_UtilsDirectPowerOH() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Utilities - Direct Damage BP 04 56')]/ancestor::legend/following-sibling::div/span/div/child::table/descendant::label[contains(text(), 'Power (inc O/H lines)')]/parent::td/following-sibling::td/table");
    }


    public Guidewire8Checkbox checkbox_UtilitiesTimeelement() {
        return new Guidewire8Checkbox(driver, returnCheckBoxTable("Utilities - Time Element BP 04 57"));
    }

    public Guidewire8Select select_UtilsTimeCoverageAppliesTo() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Utilities - Time Element BP 04 57')]/ancestor::legend/following-sibling::div/span/div/child::table/descendant::label[contains(text(), 'Coverage Applies To')]/parent::td/following-sibling::td/table");
    }

    public Guidewire8Select select_UtilsTimeUtilityIs() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Utilities - Time Element BP 04 57')]/ancestor::legend/following-sibling::div/span/div/child::table/descendant::label[contains(text(), 'Utility is')]/parent::td/following-sibling::td/table");
    }

    @FindBy(xpath = "//div[contains(text(), 'Utilities - Time Element BP 04 57')]/ancestor::legend/following-sibling::div/span/div/child::table/descendant::label[contains(text(), 'Direct Loss Limit')]/parent::td/following-sibling::td/input")
    public WebElement editbox_UtilsTimeDamDirectLossLimit;

    public Guidewire8Select select_UtilsTimeWaterSupply() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Utilities - Time Element BP 04 57')]/ancestor::legend/following-sibling::div/span/div/child::table/descendant::label[contains(text(), 'Water Supply')]/parent::td/following-sibling::td/table");
    }

    public Guidewire8Select select_UtilsTimeCommunicationsNotOH() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Utilities - Time Element BP 04 57')]/ancestor::legend/following-sibling::div/span/div/child::table/descendant::label[contains(text(), 'Communications (not O/H lines)')]/parent::td/following-sibling::td/table");
    }

    public Guidewire8Select select_UtilsTimeCommunicationsOH() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Utilities - Time Element BP 04 57')]/ancestor::legend/following-sibling::div/span/div/child::table/descendant::label[contains(text(), 'Communications (inc O/H lines)')]/parent::td/following-sibling::td/table");
    }

    public Guidewire8Select select_UtilsTimePowerNotOH() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Utilities - Time Element BP 04 57')]/ancestor::legend/following-sibling::div/span/div/child::table/descendant::label[contains(text(), 'Power (not O/H lines)')]/parent::td/following-sibling::td/table");
    }

    public Guidewire8Select select_UtilsTimePowerOH() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Utilities - Time Element BP 04 57')]/ancestor::legend/following-sibling::div/span/div/child::table/descendant::label[contains(text(), 'Power (inc O/H lines)')]/parent::td/following-sibling::td/table");
    }


    @FindBy(xpath = "//span[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsCardTab-btnEl')]")
    public WebElement link_BuildingDetailsTab;

    @FindBy(xpath = "//div[contains(@id, 'RiskClassSearchPopup:RiskClassSearchScreen:RiskClassSearchResultsLV-body')]/div/table")
    public WebElement table_RiskClassSearch;

    ///////////////////////////////////////
    //// HELPER METHODS ///////////////////
    ///////////////////////////////////////

    ////////////////////////////////////////////////////////////
    //Main Helper Methods (Designed to do the 'Heavy Lifting')//
    ////////////////////////////////////////////////////////////


    public void setBusinessIncomeOrdinaryPayrollCoverage(PolicyLocationBuilding bldg) {
        setBusinessIncome(true);
        selectBusinessIncomePayroll(bldg.getAdditionalCoveragesStuff().getBusinessIncomeOrdinaryPayrollType());
    }


    public void setDiscretionaryPayrollExpenseCoverage(PolicyLocationBuilding bldg) {
        setDiscretionaryPayroll(true);
        selectDiscretionaryPayrollDays(bldg.getAdditionalCoveragesStuff().getDiscretionaryPayrollExpenseType());
        setAnnualDiscretionaryPayroll(bldg.getAdditionalCoveragesStuff().getDiscretionaryPayrollExpenseAmount());
        setJobClassificationEmployeeNames(bldg.getAdditionalCoveragesStuff().getDiscretionaryPayrollExpenseJobClassOrNameEmployees());
    }


    public void setLossOfRentalValueLandlordAsDesignatedPayeeCoverage(PolicyLocationBuilding bldg) {
        setLossOfRentalValue(true);
        setLimitOfInsurance(bldg.getAdditionalCoveragesStuff().getLossOfRentalValueLandlordAsDesignatedPayeeLimit());
        setPersonOrgName(bldg.getAdditionalCoveragesStuff().getLossOfRentalValueLandlordAsDesignatedPayeePersonOrgName());
        setAddressOfPersonOrg(bldg.getAdditionalCoveragesStuff().getLossOfRentalValueLandlordAsDesignatedPayeePersonOrgAddress());
        setLossRentalCityStateZip(bldg.getAdditionalCoveragesStuff().getLossOfRentalValueLandlordAsDesignatedPayeePersonOrgCityStZip());
    }


    public void setAccountsReceivableOptionalCoverage(PolicyLocationBuilding bldg) {
        setAccountsReceivable(true);
        setAROnPremisesLimit(bldg.getAdditionalCoveragesStuff().getAccountsReceivableOptionalOnPremisesLimit());
    }


    public void setCondoCommercialUnitOwnersOptionalCoverage(PolicyLocationBuilding bldg) {
        setCondoCommercialUnitCoverage(true);
        selectLossAssesmentLimit(bldg.getAdditionalCoveragesStuff().getCondoCommercialUnitOwnersOptionalLossAssessmentLimit());
        setMiscRealProperty(bldg.getAdditionalCoveragesStuff().getCondoCommercialUnitOwnersOptionalMiscRealProperty());
    }


    public void setFoodContaminationCoverage(PolicyLocationBuilding bldg) {
        setFoodContaminationCoverage(true);
        setFoodcontaminationLimit(bldg.getAdditionalCoveragesStuff().getFoodContaminationLimit());
        setFoodContaminationAdvLimit(bldg.getAdditionalCoveragesStuff().getFoodContaminationAdvLimit());
    }


    public void setOrdinanceOrLawCoverage(PolicyLocationBuilding bldg) {
        setOrdinanceLaw(true);
        setBusinessIncomeExpence(bldg.getAdditionalCoveragesStuff().isOrdinanceOrLawBusinessIncomeExtraExpenseOptionalCoverage());
        selectLossToUndamagedBuilding(bldg.getAdditionalCoveragesStuff().getOrdinanceOrLawLossToUndamagedPartType());
        setDemoCost(bldg.getAdditionalCoveragesStuff().getOrdinanceOrLawDemoCostLimit());
        setCostOfConstruction(bldg.getAdditionalCoveragesStuff().getOrdinanceOrLawIncrCostConstructionLimit());
        setDemoIncrCostOfConstructionCombo(bldg.getAdditionalCoveragesStuff().getOrdinanceOrLawDemoAndIncrCostConstructionCombLimit());
    }


    public void setOutdoorPropertyCoverage(PolicyLocationBuilding bldg) {
        setOutdoorProperty(true);
        setOutdoorInsuranceLimit(bldg.getAdditionalCoveragesStuff().getOutdoorPropertyInsuranceAmount());
    }


    public void setSpoilageCoverage(PolicyLocationBuilding bldg) {
        setSpoilage(true);
        setSpoilageProductDescription(bldg.getAdditionalCoveragesStuff().getSpoilageProductDescription());
        setSpoilageLimit(bldg.getAdditionalCoveragesStuff().getSpoilageLimit());
        selectSpoilageDeductible();
        setSpoilageBreakdowncontamination(bldg.getAdditionalCoveragesStuff().isSpoilageBreakdownContamination());
        setSpoilagePowerOutage(bldg.getAdditionalCoveragesStuff().isSpoilagePowerOutage());
        setSpoilagerefigerationagreement(bldg.getAdditionalCoveragesStuff().isSpoilageRefrigerationMaintAgreement());
        setRiskDescription(bldg.getAdditionalCoveragesStuff().getSpoilageRiskDescriptionClass().getDescription());
    }


    public void setValuablePapersOptionalCoverage(PolicyLocationBuilding bldg) {
        setValuablePapers(true);
        
        setPapersOnPremisesLimit(bldg.getAdditionalCoveragesStuff().getValuablePapersOptionalOnPremisesLimit());
    }


    public void setUtilitiesDirectDamageCoverage(PolicyLocationBuilding bldg) {
        setUtilitiesDirectDamage(true);
        
        selectUtilsDirectAppliesTo(bldg.getAdditionalCoveragesStuff().getUtilitiesDirectDamageCoverageAppliesTo());
        
        selectUtilsDirectUtilityIs(bldg.getAdditionalCoveragesStuff().getUtilitiesDirectDamageUtilityIs());
        
        setUtilsDirectDamageDirectLoss(bldg.getAdditionalCoveragesStuff().getUtilitiesDirectDamageDirectLossLimit());
        
        selectUtilsdirectWatersupply(bldg.getAdditionalCoveragesStuff().getUtilitiesDirectDamageWaterSupply());
        
        selectUtilsDirectCommunicationNotOH(bldg.getAdditionalCoveragesStuff().getUtilitiesDirectDamageCommunicationsNotOHLines());
        
        selectUtilsDirectCommunicationOH(bldg.getAdditionalCoveragesStuff().getUtilitiesDirectDamageCommunicationsIncOHLines());
        
        selectUtildDirectPowerNotOH(bldg.getAdditionalCoveragesStuff().getUtilitiesDirectDamagePowerNotOHLines());
        
        selectUtilsDirectPowerOH(bldg.getAdditionalCoveragesStuff().getUtilitiesDirectDamagePowerIncOHLines());
        
    }


    public void setUtilitiesTimeElementCoverage(PolicyLocationBuilding bldg) {
        setUtilitiesDirectDamage(true);
        
        setUtilitiesDirectDamageCoverage(bldg);
        
        setUtilitiesTimeElement(true);
        
        selectUtilsTimeAppliesTo(bldg.getAdditionalCoveragesStuff().getUtilitiesTimeElementCoverageAppliesTo());
        
        selectUtilsTimeUtilityIs(bldg.getAdditionalCoveragesStuff().getUtilitiesTimeElementUtilityIs());
        
        setUtilsTimeDamageDirectLoss(bldg.getAdditionalCoveragesStuff().getUtilitiesTimeElementDirectLossLimit());
        
        selectUtilsTimeWatersupply(bldg.getAdditionalCoveragesStuff().getUtilitiesTimeElementWaterSupply());
        
        selectUtilsTimeCommunicationNotOH(bldg.getAdditionalCoveragesStuff().getUtilitiesTimeElementCommunicationsNotOHLines());
        
        selectUtilsTimeCommunicationOH(bldg.getAdditionalCoveragesStuff().getUtilitiesTimeElementCommunicationsIncOHLines());
        
        selectUtilsTimePowerNotOH(bldg.getAdditionalCoveragesStuff().getUtilitiesTimeElementPowerNotOHLines());
        
        selectUtilsTimePowerOH(bldg.getAdditionalCoveragesStuff().getUtilitiesTimeElementPowerIncOHLines());
        
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Supporting Helper Methods (Designed to Carry Out Minimal Functions to Support Above Methods)//
    ////////////////////////////////////////////////////////////////////////////////////////////////


    public void clickOK() {
        super.clickOK();
    }


    public void clickCancel() {
        super.clickCancel();
    }


    public void setEquipmentBreakdown(Boolean checked) {
        checkbox_EquipmentBreakdown().select(checked);
    }

    public void clickBuildingDetialsTab() {
    	clickWhenClickable(link_BuildingDetailsTab);
        
    }


    public void setBusinessIncome(Boolean checked) {
        checkbox_BusinessIncome().select(checked);
    }

    public void selectBusinessIncomePayroll(BusinessIncomeOrdinaryPayroll days) {
        Guidewire8Select mySelect = select_BusinessIncome();
        mySelect.selectByVisibleText(days.getValue());
    }

    public void setDiscretionaryPayroll(Boolean checked) {
        checkbox_DiscretionaryPayroll().select(checked);
    }

    public void selectDiscretionaryPayrollDays(DiscretionaryPayrollExpense days) {
        Guidewire8Select mySelect = select_DiscretionaryPayrollNumDays();
        mySelect.selectByVisibleText(days.getValue());
    }

    public void setAnnualDiscretionaryPayroll(long amount) {
        clickWhenClickable(editbox_AnnualDiscretionaryPayrollAmount);
        editbox_AnnualDiscretionaryPayrollAmount.click();
        editbox_AnnualDiscretionaryPayrollAmount.sendKeys("" + amount);
    }

    public void setJobClassificationEmployeeNames(String classificationNames) {
        clickWhenClickable(editbox_JobClassificationEmployeenames);
        editbox_JobClassificationEmployeenames.click();
        editbox_JobClassificationEmployeenames.sendKeys(classificationNames);
    }

    public void setLossOfRentalValue(Boolean checked) {
        checkbox_LossOfRentalValue().select(checked);
    }

    public void setLimitOfInsurance(long limit) {
        clickWhenClickable(editbox_LimitOfInsurance);
        editbox_LimitOfInsurance.click();
        editbox_LimitOfInsurance.sendKeys("" + limit);
    }

    public void setPersonOrgName(String name) {
        clickWhenClickable(editbox_PersonOrOrgonizationName);
        editbox_PersonOrOrgonizationName.click();
        editbox_PersonOrOrgonizationName.sendKeys(name);
    }

    public void setAddressOfPersonOrg(String address) {
        clickWhenClickable(editbox_AddressOfPersonOrOrganization);
        editbox_AddressOfPersonOrOrganization.click();
        editbox_AddressOfPersonOrOrganization.sendKeys(address);
    }

    public void setLossRentalCityStateZip(String cityStateZip) {
        clickWhenClickable(editbox_CityStateAndPostalCode);
        editbox_CityStateAndPostalCode.click();
        editbox_CityStateAndPostalCode.sendKeys(cityStateZip);
    }

    public void setAccountsReceivable(Boolean checked) {
        checkbox_AccountsReceivable().select(checked);
    }

    public void setAROnPremisesLimit(long limit) {
        clickWhenClickable(editbox_AROnPremises);
        editbox_AROnPremises.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_AROnPremises.sendKeys("" + limit);
        clickProductLogo();
    }

    public void setMiscRealProperty(long limit) {
        clickWhenClickable(editbox_MiscRealProperty); 
        editbox_MiscRealProperty.sendKeys(Keys.chord(Keys.CONTROL + "a"));        
        editbox_MiscRealProperty.sendKeys("" + limit);
        clickProductLogo();
    }

    public void setCondoCommercialUnitCoverage(Boolean checked) {
        checkbox_CondoCommercialCoverage().select(checked);
        
    }

    public void selectLossAssesmentLimit(CondoCommercialUnitOwnersLimit limit) {
        Guidewire8Select mySelect = select_LossAssessmentLimit();
        mySelect.selectByVisibleText(limit.getValue());
    }

    public void setFoodContaminationCoverage(Boolean checked) {
        checkbox_FoodContamination().select(checked);
        
    }

    public void setFoodcontaminationLimit(long limit) {
    	waitUntilElementIsClickable(editbox_FoodContaminationLimit);
        clickWhenClickable(editbox_FoodContaminationLimit);
        editbox_FoodContaminationLimit.sendKeys(Keys.chord(Keys.CONTROL + "a"));        
        editbox_FoodContaminationLimit.sendKeys("" + limit);
    }

    public void setFoodContaminationAdvLimit(long advLimit) {
    	waitUntilElementIsClickable(editbox_FoodContaminationAdvLimit);        
        clickWhenClickable(editbox_FoodContaminationAdvLimit);
        editbox_FoodContaminationAdvLimit.sendKeys(Keys.chord(Keys.CONTROL + "a"));        
        editbox_FoodContaminationAdvLimit.sendKeys("" + advLimit);
        clickProductLogo();
    }


    public void setOrdinanceLaw(Boolean checked) {
        checkbox_OrdinanceOrLaw().select(checked);
    }


    public void setBusinessIncomeExpence(Boolean yesno) {
        radio_BusinessIncomeExpenceCoverage().select(yesno);
    }


    public void selectLossToUndamagedBuilding(CoverageOrNot covered) {
        Guidewire8Select mySelect = select_LossToTheUndamagedPartOfBuilding();
        mySelect.selectByVisibleText(covered.getValue());
    }


    public void setDemoCost(long cost) {
        clickWhenClickable(editbox_DemolitionCostLimit);
        editbox_DemolitionCostLimit.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_DemolitionCostLimit.sendKeys(Keys.DELETE);
        editbox_DemolitionCostLimit.sendKeys("" + cost);
        clickProductLogo();
    }


    public void setCostOfConstruction(long cost) {
        clickWhenClickable(editbox_IncrCostOfConstruction);
        editbox_IncrCostOfConstruction.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_IncrCostOfConstruction.sendKeys(Keys.DELETE);
        editbox_IncrCostOfConstruction.sendKeys("" + cost);
        clickProductLogo();
    }


    public void setDemoIncrCostOfConstructionCombo(long cost) {
        setText(editbox_DemoIncrCostOfConstructionComboLimit, "" + cost);
        clickProductLogo();
    }

    public void setOutdoorProperty(Boolean checked) {
        checkbox_OutdoorProperty().select(checked);
    }

    public void setOutdoorInsuranceLimit(long limit) {
        setText(editbox_OutdoorInsuranceLimit, "" + limit);
        clickProductLogo();
    }

    public void setSpoilage(Boolean checked) {
    	
        checkbox_Spoilage().select(checked);
    }

    public void setSpoilageProductDescription(String desc) {
        setText(editbox_ProductDescription, desc);
    }

    public void setSpoilageLimit(SpoilageLimit limit) {
        clickWhenClickable(editbox_SpoilageLimit);
        editbox_SpoilageLimit.click();
        editbox_SpoilageLimit.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_SpoilageLimit.sendKeys("" + limit.getValue());
    }

    public void selectSpoilageDeductible() {
        //do nothing because 500 is the only amount available
    }

    public void setSpoilageBreakdowncontamination(Boolean yesno) {
        radio_BreakdownOrContamination().select(yesno);
    }

    public void setSpoilagePowerOutage(Boolean yesno) {
        radio_PowerOutage().select(yesno);
    }

    public void setSpoilagerefigerationagreement(Boolean yesno) {
        radio_RefrigerationMaintAgreement().select(yesno);
    }

    public void clickSearchRiskDescription() {
        clickWhenClickable(search_RiskDescription);
    }

    public void setRiskDescription(String riskDesc) {
        clickSearchRiskDescription();
        
        tableUtils.clickLinkInSpecficRowInTable(table_RiskClassSearch, tableUtils.getRowNumberInTableByText(table_RiskClassSearch, riskDesc));
       // the below code is not working so caling tableUtils --- Nagamani
        /*List<WebElement> riskClass = table_RiskClassSearch.findElements(By.xpath(".//tbody/child::tr/child::td/div[contains(text(), '" + riskDesc + "')]/parent::td/parent::tr/child::td/div/a[contains(text(), 'Select')]"));
        //CLICK RISK DESCRIPTION
        if (riskClass.size() > 0) {
            riskClass.get(0).click();
        } else {
            Assert.fail("Unable to find the Risk Class" + riskDesc);
        }*/
        
    }

    public void setValuablePapers(Boolean checked) {
    	
        checkbox_ValuablePapers().select(checked);
    }

    public void setPapersOnPremisesLimit(long limit) {
        clickWhenClickable(editbox_PapersOnPremisesLimit);
        editbox_PapersOnPremisesLimit.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_PapersOnPremisesLimit.sendKeys("" + limit);
    }

    public void setUtilitiesDirectDamage(Boolean checked) {
        checkbox_UtilitiesDirectDamage().select(checked);
    }

    public void selectUtilsDirectAppliesTo(UtilitiesCoverageAppliesTo appliedTo) {
        Guidewire8Select mySelect = select_UtilsDirectCoverageAppliesTo();
        mySelect.selectByVisibleText(appliedTo.getValue());
    }

    public void selectUtilsDirectUtilityIs(UtilitiesUtilityIs utilityIs) {
        Guidewire8Select mySelect = select_UtilsDirectUtilityIs();
        mySelect.selectByVisibleText(utilityIs.getValue());
    }

    public void setUtilsDirectDamageDirectLoss(long loss) {
        clickWhenClickable(editbox_UtilsDirectDamDirectLossLimit);
        editbox_UtilsDirectDamDirectLossLimit.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_UtilsDirectDamDirectLossLimit.sendKeys(String.valueOf(loss));
    }

    public void selectUtilsdirectWatersupply(CoverageOrNot covered) {
        Guidewire8Select mySelect = select_UtilsDirectWaterSupply();
        mySelect.selectByVisibleText(covered.getValue());
    }

    public void selectUtilsDirectCommunicationNotOH(CoverageOrNot covered) {
        Guidewire8Select mySelect = select_UtilsDirctCommunicationsNotOH();
        mySelect.selectByVisibleText(covered.getValue());
    }

    public void selectUtilsDirectCommunicationOH(CoverageOrNot covered) {
        Guidewire8Select mySelect = select_UtilsDirectCommunicationsOH();
        mySelect.selectByVisibleText(covered.getValue());
    }

    public void selectUtildDirectPowerNotOH(CoverageOrNot covered) {
        Guidewire8Select mySelect = select_UtilsDirectPowerNotOH();
        mySelect.selectByVisibleText(covered.getValue());
    }

    public void selectUtilsDirectPowerOH(CoverageOrNot covered) {
        Guidewire8Select mySelect = select_UtilsDirectPowerOH();
        mySelect.selectByVisibleText(covered.getValue());
    }

    public void setUtilitiesTimeElement(Boolean checked) {
        checkbox_UtilitiesTimeelement().select(checked);
    }

    public void selectUtilsTimeAppliesTo(UtilitiesCoverageAppliesTo appliedTo) {
        Guidewire8Select mySelect = select_UtilsTimeCoverageAppliesTo();
        mySelect.selectByVisibleText(appliedTo.getValue());
    }

    public void selectUtilsTimeUtilityIs(UtilitiesUtilityIs utilityIs) {
        Guidewire8Select mySelect = select_UtilsTimeUtilityIs();
        mySelect.selectByVisibleText(utilityIs.getValue());
    }

    public void setUtilsTimeDamageDirectLoss(long loss) {
        clickWhenClickable(editbox_UtilsTimeDamDirectLossLimit);
        editbox_UtilsTimeDamDirectLossLimit.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_UtilsTimeDamDirectLossLimit.sendKeys("" + loss);
    }

    public void selectUtilsTimeWatersupply(CoverageOrNot covered) {
        Guidewire8Select mySelect = select_UtilsTimeWaterSupply();
        mySelect.selectByVisibleText(covered.getValue());
    }

    public void selectUtilsTimeCommunicationNotOH(CoverageOrNot covered) {
        Guidewire8Select mySelect = select_UtilsTimeCommunicationsNotOH();
        mySelect.selectByVisibleText(covered.getValue());
    }

    public void selectUtilsTimeCommunicationOH(CoverageOrNot covered) {
        Guidewire8Select mySelect = select_UtilsTimeCommunicationsOH();
        mySelect.selectByVisibleText(covered.getValue());
    }

    public void selectUtilsTimePowerNotOH(CoverageOrNot covered) {
        Guidewire8Select mySelect = select_UtilsTimePowerNotOH();
        mySelect.selectByVisibleText(covered.getValue());
    }

    public void selectUtilsTimePowerOH(CoverageOrNot covered) {
        Guidewire8Select mySelect = select_UtilsTimePowerOH();
        mySelect.selectByVisibleText(covered.getValue());
    }
}






















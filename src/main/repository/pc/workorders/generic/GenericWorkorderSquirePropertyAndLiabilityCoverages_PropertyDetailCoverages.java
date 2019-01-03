package repository.pc.workorders.generic;

import com.idfbins.enums.OkCancel;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.Building.ValuationMethod;
import repository.gw.enums.CoverageType;
import repository.gw.enums.Property.FoundationType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PLPropertyCoverages;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

public class GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages extends GenericWorkorderSquirePropertyAndLiabilityCoverages {

	private WebDriver driver;
	private TableUtils tableUtils;

    public GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }
	
	public void fillOutPropertyDetailCoveragesQQ(GeneratePolicy policy) {
		selectSectionIDeductible(policy.squire.propertyAndLiability.section1Deductible);

		for (PolicyLocation loc : policy.squire.propertyAndLiability.locationList) {
			for (PLPolicyLocationProperty prop : loc.getPropertyList()) {
				clickSpecificBuilding(loc.getNumber(), prop.getPropertyNumber());
				
				if (prop.hasCoverageE()) {
					fillOutCoverageE(prop);
					if(prop.getPropertyCoverages().hasCoverageC()) {
						fillOutCoverageC(prop);
					}

				} else if (prop.getpropertyType().equals(PropertyTypePL.DetachedGarage)) {
					setOtherStructureDetachedGarageAdditionalValue(prop.getPropertyCoverages().getCoverageA().getLimit());
				} else {
					fillOutCoverageA_QQ(prop);
					fillOutCoverageC_QQ(prop);
				}//END ELSE

				
			}//END FOR
		}//END FOR
	}// end fillOutSectionI(GeneratePolicy policy)
	
	public void fillOutPropertyDetailCoverages(GeneratePolicy policy) throws GuidewireNavigationException {
		selectSectionIDeductible(policy.squire.propertyAndLiability.section1Deductible);

		for (PolicyLocation loc : policy.squire.propertyAndLiability.locationList) {
			for (PLPolicyLocationProperty prop : loc.getPropertyList()) {
				clickSpecificBuilding(loc.getNumber(), prop.getPropertyNumber());

				if (prop.hasCoverageE()) {
					fillOutCoverageE(prop);
					if (prop.getpropertyType().equals(PropertyTypePL.DwellingUnderConstructionCovE))  // @editor ecoleman 8/13/18: This particular method of setting covC seems to be unique to DuC_E, will keep an eye on it
						new GenericWorkorderSquirePropertyCoverages(driver).setCoverageCLimit(40000);
				} else if (prop.getpropertyType().equals(PropertyTypePL.DetachedGarage)) {
					setOtherStructureDetachedGarageAdditionalValue(prop.getPropertyCoverages().getCoverageA().getLimit());
				} else {
					if(!prop.getpropertyType().equals(PropertyTypePL.Contents)){
					fillOutCoverageA(prop);
					}
					fillOutCoverageC(prop);
				}//END ELSE

				fillOutOptionalPropertyCoverages(prop);
			}//END FOR
		}//END FOR
	}// end fillOutSectionI(GeneratePolicy policy)
	
	public void fillOutCoverageA_QQ(PLPolicyLocationProperty property) {
		setCoverageALimit(property.getPropertyCoverages().getCoverageA().getLimit());
		
		//Requirement: Not available if (Property Type = Dwelling Under Construction) or (Property Class = C or D) - Nagamani added
		if (!property.getpropertyType().equals(PropertyTypePL.DwellingUnderConstruction) && (property.getYearBuilt() > 1954 && !property.getConstructionType().equals(ConstructionTypePL.MobileHome))) {
			setCoverageAIncreasedReplacementCost(property.getPropertyCoverages().getCoverageA().isIncreasedReplacementCost());
		}
		
		if (!property.getConstructionType().equals(ConstructionTypePL.MobileHome) && !property.getFoundationType().equals(FoundationType.None) && !property.getpropertyType().equals(PropertyTypePL.CondominiumDwellingPremises) && !property.getpropertyType().equals(PropertyTypePL.CondominiumResidencePremise) && !property.getpropertyType().equals(PropertyTypePL.DwellingUnderConstruction)) {
			setCoverageAValuation(property.getPropertyCoverages().getCoverageA().getValuationMethod());
		}
		
		clickProductLogo();
		if (!property.getPropertyCoverages().getCoverageA().getValuationMethod().equals(ValuationMethod.ActualCashValue)) {
			if(!property.getpropertyType().equals(PropertyTypePL.DwellingPremises)) {
				setCoverageACoverageType(property.getPropertyCoverages().getCoverageA().getCoverageType());
			}
		}
	}
	
	public void fillOutCoverageA(PLPolicyLocationProperty property) {
		setCoverageALimit(property.getPropertyCoverages().getCoverageA().getLimit());
		
		//Requirement: Not available if (Property Type = Dwelling Under Construction) or (Property Class = C or D) - Nagamani added
        if (!property.getpropertyType().equals(PropertyTypePL.DwellingUnderConstruction) && (property.getYearBuilt() > 1954 && !property.getConstructionType().equals(ConstructionTypePL.MobileHome) && !property.getConstructionType().equals(ConstructionTypePL.ModularManufactured))) {
			setCoverageAIncreasedReplacementCost(property.getPropertyCoverages().getCoverageA().isIncreasedReplacementCost());
		}

        if (!property.getConstructionType().equals(ConstructionTypePL.MobileHome) && !property.getConstructionType().equals(ConstructionTypePL.ModularManufactured) && !property.getFoundationType().equals(FoundationType.None) && !property.getpropertyType().equals(PropertyTypePL.CondominiumDwellingPremises) && !property.getpropertyType().equals(PropertyTypePL.CondominiumResidencePremise) && !property.getpropertyType().equals(PropertyTypePL.DwellingUnderConstruction)) {
			setCoverageAValuation(property.getPropertyCoverages().getCoverageA().getValuationMethod());
		}
		
		clickProductLogo();
		if (!property.getPropertyCoverages().getCoverageA().getValuationMethod().equals(ValuationMethod.ActualCashValue)) {
			if(!property.getpropertyType().equals(PropertyTypePL.DwellingPremises) && !property.getpropertyType().equals(PropertyTypePL.CondominiumDwellingPremises) && !property.getpropertyType().equals(PropertyTypePL.DwellingUnderConstruction)) {
				setCoverageACoverageType(property.getPropertyCoverages().getCoverageA().getCoverageType());
			}
		}

        if (!property.getpropertyType().equals(PropertyTypePL.DwellingUnderConstruction) && !property.getConstructionType().equals(ConstructionTypePL.MobileHome) && !property.getConstructionType().equals(ConstructionTypePL.ModularManufactured)) {
			waitForPostBack();
        	setCoverageAIncreasedReplacementCost(property.getPropertyCoverages().getCoverageA().isIncreasedReplacementCost());
			setCoverageAWaiveGlassDeductible(property.getPropertyCoverages().getCoverageA().isWaivedGlassDeductible());
		}
	}
	
	public void fillOutCoverageC(PLPolicyLocationProperty property) {
		if(!property.getpropertyType().equals(PropertyTypePL.Contents)){
		setCoverageCLimitPercentage(property);
		}else{
			setCoverageCLimit(property.getPropertyCoverages().getCoverageA().getLimit());
		}

		waitForPostBack();
		setCoverageCValuation(property.getPropertyCoverages());

		if (!property.getPropertyCoverages().getCoverageC().getValuationMethod().equals(ValuationMethod.ActualCashValue) && (property.getYearBuilt() > 1954 && !property.getConstructionType().equals(ConstructionTypePL.MobileHome))) {
			selectCoverageCCoverageType(property.getPropertyCoverages().getCoverageC().getCoverageType());
		}

		if (property.getPropertyCoverages().getCoverageC().getAdditionalValue() != 0.00 && !property.getpropertyType().equals(PropertyTypePL.Contents)) {
			setCoverageCAdditionalValue(property.getPropertyCoverages().getCoverageC().getAdditionalValue());
		}//END IF
	}
	
	private void setCoverageCLimitPercentage(PLPolicyLocationProperty property) {
		property.getPropertyCoverages().getCoverageC().setLimitPercent(Integer.valueOf(getCoverageCLimit().substring(0, getCoverageCLimit().indexOf("%"))));
	}

	public void fillOutCoverageC_QQ(PLPolicyLocationProperty property) {
		setCoverageCValuation(property.getPropertyCoverages());

		if (!property.getPropertyCoverages().getCoverageC().getValuationMethod().equals(ValuationMethod.ActualCashValue) && (property.getYearBuilt() > 1954 && !property.getConstructionType().equals(ConstructionTypePL.MobileHome))) {
			selectCoverageCCoverageType(property.getPropertyCoverages().getCoverageC().getCoverageType());
		}

		if (property.getPropertyCoverages().getCoverageC().getAdditionalValue() != 0.00) {
			setCoverageCAdditionalValue(property.getPropertyCoverages().getCoverageC().getAdditionalValue());
		}//END IF
	}
	
	public void fillOutCoverageE(PLPolicyLocationProperty property) {
		setCoverageELimit(property.getPropertyCoverages().getCoverageA().getLimit());
		setCoverageECoverageType(property.getPropertyCoverages().getCoverageE().getCoverageType());
		if (!property.getpropertyType().equals(PropertyTypePL.DwellingUnderConstructionCovE) && 
				!property.getpropertyType().equals(PropertyTypePL.Barn) && 
				!property.getpropertyType().equals(PropertyTypePL.Shed) && 
				!property.getpropertyType().equals(PropertyTypePL.DairyComplex)&& 
				!property.getpropertyType().equals(PropertyTypePL.AlfalfaMill) &&
				!property.getpropertyType().equals(PropertyTypePL.Arena)&&
				!property.getpropertyType().equals(PropertyTypePL.Garage) &&
				!property.getpropertyType().equals(PropertyTypePL.Hangar)&&
				!property.getpropertyType().equals(PropertyTypePL.Hatchery)				&&
				!property.getpropertyType().equals(PropertyTypePL.CommodityShed)) {
			setCoverageCLimit(property.getPropertyCoverages().getCoverageA().getLimit());
		}
			
		if ((property.getpropertyType().equals(PropertyTypePL.FarmOffice)|| property.getpropertyType().equals(PropertyTypePL.Shed) || property.getpropertyType().equals(PropertyTypePL.Barn) || property.getpropertyType().equals(PropertyTypePL.Garage))&& !property.getPropertyCoverages().getCoverageE().getCoverageType().equals(CoverageType.Peril_1)) {
			setCoverageEValuation(ValuationMethod.ActualCashValue);
		}

		if (property.getpropertyType().equals(PropertyTypePL.FarmOffice) || 
				property.getpropertyType().equals(PropertyTypePL.BunkHouse) || 
				property.getpropertyType().equals(PropertyTypePL.DwellingPremisesCovE) || 
				property.getpropertyType().equals(PropertyTypePL.ResidencePremisesCovE) || 
				property.getpropertyType().equals(PropertyTypePL.CondominiumDwellingPremisesCovE) || 
				property.getpropertyType().equals(PropertyTypePL.CondoVacationHomeCovE) || 
				property.getpropertyType().equals(PropertyTypePL.VacationHomeCovE)) {
			setCoverageCLimit(property.getPropertyCoverages().getCoverageA().getLimit());
			setCoverageCValuation(property.getPropertyCoverages());
		}
	}

	public void fillOutOtherStructures(PLPolicyLocationProperty property) {

	}

	public void fillOutCoverageBLossOfUse(PLPolicyLocationProperty property) {

	}



	public void fillOutOptionalPropertyCoverages(PLPolicyLocationProperty property) throws GuidewireNavigationException {
		//earthquake
		if(property.hadCoverageA() && !(property.getConstructionType().equals(ConstructionTypePL.MobileHome) || property.getConstructionType().equals(ConstructionTypePL.ModularManufactured))) {
			if(checkEarthquake(property.getPropertyCoverages().isEarthquake())) {
				setIncludeMasonry(property.getPropertyCoverages().isIncludeMasonry());
			}
		}
		
		//sewage system backup
		
		//loss of income expense
	}

	public void fillOutMiscellaneousCoverages(PLPolicyLocationProperty property) {
		if(property.getPropertyCoverages().isGuns()) {
			setGunIncreasedTheftLimit(property.getPropertyCoverages().getGuns_IncreaseTheftLimit());
		}
		if(property.getPropertyCoverages().isSilverware()) {
			setSilverwareIncreasedTheftLimit(property.getPropertyCoverages().getSilverware_IncreasedTheftLimit());
		}
		if(property.getPropertyCoverages().isTools()) {
			setToolsIncreasedLimit(property.getPropertyCoverages().getTools_increasedLimit());
		}
		if(property.getPropertyCoverages().isSaddlesAndtack()) {
			setSaddlesTackIncreasedLimit(property.getPropertyCoverages().getSaddlesAndTack_IncreasedLimit());
		}
	}
	
	public void fillOutexclusionsAndConditions(PLPolicyLocationProperty property) {
		clickBuildingsExclusionsAndConditions();
		fillOutExclusions(property);
		fillOutConditions(property);
	}
	
	public void fillOutExclusions(PLPolicyLocationProperty property) {
		//Coverage A (Dwellings) Detached Garage and Storage Shed - 107
		clickDetachedGarageEndorsement(property.getExclusionsConditions().isExclusionCoverageADwellingsDetachedGarageAndStorageShed107());
		//Specified Property Exclusion Endorsement - 110
		fillOutSpecifiedPropertyExclusionEndorsement_110(property.getExclusionsConditions().getExclusionSpecifiedPropertyExclusionEndorsement110DescriptionsOfDamage());
	}
	
	public void fillOutConditions(PLPolicyLocationProperty property) {
		//Actual Cash Value Limitation for Roofing - Endorsement 106
		//Limited Roof Coverage Endorsement - 134
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
	
	public String getSquirePropertyCoveragesSectionIDeductible(){
		return select_SquirePropertyCoveragesSectionIDeductible().getText();
	}

	private Guidewire8Select select_SquirePropertyCoveragesSectionIDeductible() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':CovPatternInputGroup:0:CovTermPOCHOEInputSet:OptionTermInput-triggerWrap')]");
	}

	public void selectSectionIDeductibleGrandFatheredDropdownValues() {
		Guidewire8Select SectionIDeductible = select_SquirePropertyCoveragesSectionIDeductible();

		for (String listItem : SectionIDeductible.getList()) {
			if (listItem.equals("0") || listItem.equals("25") || listItem.equals("50") || listItem.equals("100") || listItem.equals("250"))
				Assert.fail("Expected Dropdown values don't match the Actual.");
		}
	}
	
	private Guidewire8Checkbox checkBox_EquipmentBreakdown() {
        return new Guidewire8Checkbox(driver, "//div[contains(@id, ':LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOCoveragesPanelSet:EquipmentBreakdownInputGroup-legend-innerCt')]/table");
    }
	
	public boolean isEquipmentBreakdownChecked() {
		return checkBox_EquipmentBreakdown().isSelected();
	}
	
	public void setEquipmentBreakdown(boolean checkedTrueOrFalse) {
		checkBox_EquipmentBreakdown().select(checkedTrueOrFalse);
        
	}
	
	@FindBy(xpath = "//fieldset[contains(., 'Equipment Breakdown')]/div//td[.='Limit']/following-sibling::td/div[contains(@id, ':DirectTermInput-inputEl')]")
	private WebElement label_EquipmentBreakdownCoverageLimit;
	
	public double getEquipmentBreakdownLimit() {
		waitUntilElementIsVisible(label_EquipmentBreakdownCoverageLimit);
		return Double.parseDouble(label_EquipmentBreakdownCoverageLimit.getText().trim().replace(",", ""));
	}

	@FindBy(xpath = "//fieldset[contains(., 'Coverage A')]/div//td[.='Limit']/following-sibling::td/input[contains(@id, ':limitTerm-inputEl')]")
	private WebElement editbox_SquirePropertyCoveragesCoverageALimit;

	public void setCoverageALimit(double limit) {
		setText(editbox_SquirePropertyCoveragesCoverageALimit, String.valueOf(limit));
	}

	public double getCoverageALimit() {
		waitUntilElementIsVisible(editbox_SquirePropertyCoveragesCoverageALimit);
		return Double.parseDouble(editbox_SquirePropertyCoveragesCoverageALimit.getAttribute("value").replace(",", ""));
	}

	private Guidewire8Select select_SquirePropertyCoveragesCoverageAValuationMethod() {
		return new Guidewire8Select(driver, "//div[contains(text(), 'Coverage A')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Valuation Method')]/parent::td/following-sibling::td/table");
	}

	public void setCoverageAValuation(ValuationMethod propertyCoverages) {
		select_SquirePropertyCoveragesCoverageAValuationMethod().selectByVisibleText(propertyCoverages.getValue());
		clickProductLogo();
	}

	@FindBy(xpath = "//fieldset[contains(., 'Sewage System Backup') and contains(., 'Limit') and contains(., '10% - ')]")
	private WebElement text_SquirePropertyCoveragesSewageSystemBackup;

	public String getCoverageAValuation() {
		//clickWhenClickable(text_SquirePropertyCoveragesSewageSystemBackup);
		String text = select_SquirePropertyCoveragesCoverageAValuationMethod().getText();
		//clickWhenClickable(text_SquirePropertyCoveragesSewageSystemBackup);
		return text;
	}

	public boolean checkIfCoverageAValuationSelectExists() {
		
		return checkIfElementExists(select_SquirePropertyCoveragesCoverageAValuationMethod().getSelectButtonElement(), 2000);
	}

	private Guidewire8Select select_SquirePropertyCoveragesCoverageACoverageType() {
		return new Guidewire8Select(driver, "//div[contains(text(), 'Coverage A')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Coverage Type')]/parent::td/following-sibling::td/table");
	}
	public void setCoverageACoverageType(CoverageType coverageAType) {
		waitForPageLoad();
		select_SquirePropertyCoveragesCoverageACoverageType().selectByVisibleTextPartial(coverageAType.getValue());
	}

	public String getCoverageACoverageType() {
		return select_SquirePropertyCoveragesCoverageACoverageType().getText();
	}
	private Guidewire8RadioButton radio_SquirePropertyCoveragesCoverageAIncreasedReplacementCost() {
		return new Guidewire8RadioButton(driver, "//label[contains(text(), 'Increased Replacement Cost')]/parent::td/parent::tr/parent::tbody/parent::table");
	}
	public void setCoverageAIncreasedReplacementCost(boolean radioValue) {
		waitForPageLoad();
		radio_SquirePropertyCoveragesCoverageAIncreasedReplacementCost().select(radioValue);
	}

	public boolean IncreasedReplacementCostExists() {
		try {
			return checkIfElementExists(radio_SquirePropertyCoveragesCoverageAIncreasedReplacementCost().getWebElement(), 100);
		} catch (Exception e) {
			return false;
		}
	}

    public boolean IsIncreasedReplacementCostDisabled() {
        return checkIfElementExists("//label[contains(text(), 'Increased Replacement Cost')]/parent::td/following-sibling::td/div/input", 100);
    }

	private Guidewire8RadioButton radio_SquirePropertyCoveragesCoverageAWaiveGlassDeductible() {
		return new Guidewire8RadioButton(driver, "//label[contains(text(), 'Waive Glass Deductible')]/parent::td/parent::tr/parent::tbody/parent::table");
	}
	
	public void setCoverageAWaiveGlassDeductible(boolean radioValue) {
		WebElement radioYes = null;
		if (radioValue) {
			radioYes = radio_SquirePropertyCoveragesCoverageAWaiveGlassDeductible().find(By.xpath(".//descendant::label[contains(text(), 'Yes')]/preceding-sibling::input"));
		} else {
			radioYes = radio_SquirePropertyCoveragesCoverageAWaiveGlassDeductible().find(By.xpath(".//descendant::label[contains(text(), 'No')]/preceding-sibling::input"));
		}
		waitUntilElementIsClickable(radioYes);
		clickAndHoldAndRelease(radioYes);
		super.selectOKOrCancelFromPopup(OkCancel.OK);
		hoverOver(radioYes);
	}

	///Coverage C Methods///

	@FindBy(xpath = "//div[contains(., 'Coverage C')]/ancestor::legend/following-sibling::div/descendant::table/tbody/tr/td[2]/div")
	private WebElement text_SquirePropertyCoveragesCLimit;
	public String getCoverageCLimit() {
		waitUntilElementIsVisible(text_SquirePropertyCoveragesCLimit);
		return text_SquirePropertyCoveragesCLimit.getText();
	}
	@FindBy(xpath = "//fieldset[contains(., 'Coverage C')]/div//td[.='Limit']/following-sibling::td/input[contains(@id, ':limitTerm-inputEl') or contains(@id,':DirectTermInput-inputEl') or contains(@id, ':fancyLimitTerm-inputEl')]")
	private WebElement editbox_CovCLimit;
	public void setCoverageCLimit(double value) {
		setText(editbox_CovCLimit, String.valueOf(value));
	}

	@FindBy(xpath = "//div[contains(text(), 'Coverage C')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Additional Value')]/parent::td/following-sibling::td/input[contains(@id,':DirectTermInput-inputEl')]")
	private WebElement editbox_SquirePropertyCoveragesCoverageCAdditionalValue;
	public void setCoverageCAdditionalValue(double value) {
		waitUntilElementIsClickable(editbox_SquirePropertyCoveragesCoverageCAdditionalValue);
		setText(editbox_SquirePropertyCoveragesCoverageCAdditionalValue, String.valueOf(value));
	}

	public void setCoverageCAdditionalValueOrLimit(double number) {
		if (checkIfElementExists(editbox_SquirePropertyCoveragesCoverageCAdditionalValue, 2000)) {
			setCoverageCAdditionalValue(number);
		} else if (checkIfElementExists(editbox_CovCLimit, 2000)) {
			setCoverageCLimit(number);
		} else {
			System.out.println("Sorry, the Coverage C addtional Value or the Coverage C Limit was not found on the page and set.");
		}
	}

	private Guidewire8Select select_SquirePropertyCoveragesCoverageCValuationMethod() {
		return new Guidewire8Select(driver, "//div[contains(text(), 'Coverage C')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Valuation Method')]/parent::td/following-sibling::td/table");
	}
	public void setCoverageCValuation(PLPropertyCoverages propertyCoverages) {
		select_SquirePropertyCoveragesCoverageCValuationMethod().selectByVisibleText(propertyCoverages.getCoverageC().getValuationMethod().getValue());
	}
	
	public String getCoverageCValuation(){
		return select_SquirePropertyCoveragesCoverageCValuationMethod().getText();
	}

	private Guidewire8Select select_SquirePropertyCoveragesCoverageCCoverageType() {
		return new Guidewire8Select(driver, "//div[contains(text(), 'Coverage C')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Coverage Type')]/parent::td/following-sibling::td/table");
	}
	public void selectCoverageCCoverageType(CoverageType type) {
		try {
			select_SquirePropertyCoveragesCoverageCCoverageType().selectByVisibleTextPartial(type.getValue());
		} catch (Exception E) {
			systemOut("Unable to select the CoverageCCoverageType. This most likely indicates that the value was automatically set and is no longer a drop-down.");
		}
		
	}
	
	public String getCoverageCCoverageType(){
		return select_SquirePropertyCoveragesCoverageCCoverageType().getText();
	}

	public void setStdFireCoverageCValuation(PLPropertyCoverages propertyCoverages) {
		select_SquirePropertyCoveragesCoverageCValuationMethod().selectByVisibleText(propertyCoverages.getCoverageC().getValuationMethod().getValue());
	}

	public boolean getSewageSystemBackup() {
		
		waitUntilElementIsVisible(text_SquirePropertyCoveragesSewageSystemBackup);
		return checkbox_SquirePropertyCoveragesSewageSystemBackup().isSelected();
	}

	private Guidewire8Checkbox checkbox_SquirePropertyCoveragesSewageSystemBackup() {
		return new Guidewire8Checkbox(driver, "//div[contains(., 'Sewage System Backup')]/preceding-sibling::table[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOCoveragesPanelSet:HOMainCoveragesHOEPanelSet:optionalCoverages:GenericCoverage_ExtPanelSet:LineDV:1:LineCovs:CoverageInputSet:CovPatternInputGroup-legendChk')]");
	}

	public void setSewageSystemBackup(boolean trueFalse) {
		
		waitUntilElementIsVisible(find(By.xpath("//fieldset[contains(., 'Sewage System Backup')]")));
		checkbox_SquirePropertyCoveragesSewageSystemBackup().select(trueFalse);
	}


	public void checkGunsMiscellaneousCoverages(boolean trueFalse) {
		if ((trueFalse && !checkbox_OptionalCoverages("Guns").isSelected()) || (!trueFalse && checkbox_OptionalCoverages("Guns").isSelected())) {
			clickWhenClickable(checkbox_OptionalCoverages("Guns"));
		}
		
	}

	@FindBy(xpath = "//div[contains(text(), 'Guns')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Limit')]/parent::td/following-sibling::td/input")
	private WebElement editbox_SquirePropertyCoverageGuns_Limit;
	public void setGunsLimit(double limit) {
		clickWhenClickable(editbox_SquirePropertyCoverageGuns_Limit);
		editbox_SquirePropertyCoverageGuns_Limit.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editbox_SquirePropertyCoverageGuns_Limit.sendKeys(String.valueOf(limit));
		editbox_SquirePropertyCoverageGuns_Limit.sendKeys(Keys.TAB);
		
	}

	@FindBy(xpath = "//label[contains(text(), 'Guns Increased Theft Limit')]/parent::td/following-sibling::td/input")
	private WebElement editbox_SquirePropertyCoverageGunsIncreasedTheftLimit;
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

	@FindBy(xpath = "//div[contains(text(), 'Silverware')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Limit')]/parent::td/following-sibling::td/input")
	private WebElement editbox_SquirePropertyCoverageSilverware_Limit;
	public void setSilverwareLimit(double limit) {
		clickWhenClickable(editbox_SquirePropertyCoverageSilverware_Limit);
		editbox_SquirePropertyCoverageSilverware_Limit.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editbox_SquirePropertyCoverageSilverware_Limit.sendKeys(String.valueOf(limit));
		editbox_SquirePropertyCoverageSilverware_Limit.sendKeys(Keys.TAB);
		
	}

	@FindBy(xpath = "//label[contains(text(), 'Silverware Increased Theft Limit')]/parent::td/following-sibling::td/input")
	private WebElement editbox_SquirePropertyCoverageSilverwareIncreasedTheftLimit;
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

	@FindBy(xpath = "//div[contains(text(), 'Tools')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Limit')]/parent::td/following-sibling::td/input")
	private WebElement editbox_SquirePropertyCoverageTools_Limit;
	public void setToolsLimit(double limit) {
		clickWhenClickable(editbox_SquirePropertyCoverageTools_Limit);
		editbox_SquirePropertyCoverageTools_Limit.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editbox_SquirePropertyCoverageTools_Limit.sendKeys(String.valueOf(limit));
		editbox_SquirePropertyCoverageTools_Limit.sendKeys(Keys.TAB);
		
	}

	@FindBy(xpath = "//label[contains(text(), 'Tools Increased Limit')]/parent::td/following-sibling::td/input")
	private WebElement editbox_SquirePropertyCoverageToolsIncreasedLimit;
	public void setToolsIncreasedLimit(double limit) {
		clickWhenClickable(editbox_SquirePropertyCoverageToolsIncreasedLimit);
		editbox_SquirePropertyCoverageToolsIncreasedLimit.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editbox_SquirePropertyCoverageToolsIncreasedLimit.sendKeys(String.valueOf(limit));
		editbox_SquirePropertyCoverageToolsIncreasedLimit.sendKeys(Keys.TAB);
		
	}

	private WebElement checkbox_OptionalCoverages(String labelName) {
		return find(By.xpath("//div/div[text()='" + labelName + "']/preceding-sibling::table//input[contains(@id, ':LineCovs:CoverageInputSet:CovPatternInputGroup:_checkbox')]"));
	}
	
	public void checkSaddlesTackMiscellaneousCoverages(boolean trueFalse) {
		if ((trueFalse && !checkbox_OptionalCoverages("Saddles and Tack").isSelected()) || (!trueFalse && checkbox_OptionalCoverages("Saddles and Tack").isSelected()))
			clickWhenClickable(checkbox_OptionalCoverages("Saddles and Tack"));
		
	}

	@FindBy(xpath = "//div[contains(text(), 'Saddles and Tack')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Limit')]/parent::td/following-sibling::td/input")
	private WebElement editbox_SquirePropertyCoverageSaddlesTack_Limit;
	public void setSaddlesTackLimit(double limit) {
		clickWhenClickable(editbox_SquirePropertyCoverageSaddlesTack_Limit);
		editbox_SquirePropertyCoverageSaddlesTack_Limit.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editbox_SquirePropertyCoverageSaddlesTack_Limit.sendKeys(String.valueOf(limit));
		editbox_SquirePropertyCoverageSaddlesTack_Limit.sendKeys(Keys.TAB);
		
	}

	@FindBy(xpath = "//label[contains(text(), 'Saddles and Tack Increased Limit')]/parent::td/following-sibling::td/input")
	private WebElement editbox_SquirePropertyCoverageSaddlesTackIncreasedLimit;
	public void setSaddlesTackIncreasedLimit(double limit) {
		clickWhenClickable(editbox_SquirePropertyCoverageSaddlesTackIncreasedLimit);
		editbox_SquirePropertyCoverageSaddlesTackIncreasedLimit.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editbox_SquirePropertyCoverageSaddlesTackIncreasedLimit.sendKeys(String.valueOf(limit));
		editbox_SquirePropertyCoverageSaddlesTackIncreasedLimit.sendKeys(Keys.TAB);
		
	}

	@FindBy(xpath = "//div[text()='Other Structures']/ancestor::legend/following-sibling::div/descendant::label[text()='Limit']/parent::td/following-sibling::td/div")
	private WebElement otherStructuresLimit;
	public double getOtherStructuresLimit() {
		String otherStructuresLimitText = otherStructuresLimit.getText();
		return NumberUtils.getCurrencyValueFromElement(otherStructuresLimitText.substring(otherStructuresLimitText.indexOf("$")));

	}

	@FindBy(xpath = "//div[text()='Coverage B Loss Of Use']/ancestor::legend/following-sibling::div/descendant::label[text()='Limit']/parent::td/following-sibling::td/div")
	private WebElement CoverageBLossOfUse;
	public double getCoverageBLossOfUse() {
		String coverageBLossofUse = CoverageBLossOfUse.getText();
		return NumberUtils.getCurrencyValueFromElement(coverageBLossofUse.substring(coverageBLossofUse.indexOf("$")));
	}

	@FindBy(xpath = "//div[contains(text(), 'Loss of Income & Extra Expense')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Limit')]/parent::td/following-sibling::td/input")
	private WebElement text_LossIncomeExtraExpense;
	public void setLossIncomeExtraExpense(double limit) {
		clickWhenClickable(checkbox_OptionalCoverages("Loss of Income & Extra Expense"));
		
		text_LossIncomeExtraExpense.sendKeys(String.valueOf(limit));
	}

	private WebElement lineItem_SquirePropertyCoveragesBuildings(int loc, int building) {
		return find(By.xpath("//div[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOCoveragesPanelSet:5-body')]/descendant::tr/td/div[.= " + loc + "]/../../td/div[.= " + building + "] | //div[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOCoveragesPanelSet:7-body')]/descendant::tr/td/div[.= " + loc + "]/../../td/div[.= " + building + "]"));
	}
	
	public void clickSpecificBuilding(int loc, int blding) {
		clickWhenClickable(lineItem_SquirePropertyCoveragesBuildings(loc, blding));
	}


//	public void clickSpecificBuilding(WebElement linkToClick) {
//		
//		clickWhenClickable(linkToClick);
//		
//	}

	@FindBy(xpath = "//div[contains(@id, 'SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOCoveragesPanelSet:5')]")
	private WebElement table_CoveragePropertiesTable;
	public String getPropertyTableSpecificColumnByRow(int rowNumber, String headerName) {
		return tableUtils.getCellTextInTableByRowAndColumnName(table_CoveragePropertiesTable, rowNumber, headerName);
	}
	public int getPropertyTableRowCount() {
		return tableUtils.getRowCount(table_CoveragePropertiesTable);
	}

	@FindBy(xpath = "//span[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOCoveragesPanelSet:ExclusionsAndConditionsCardTab-btnEl')]")
	private WebElement link_SquirePropertyCoveragesBuildingExclusions;
	public void clickBuildingsExclusionsAndConditions() {
		clickWhenClickable(link_SquirePropertyCoveragesBuildingExclusions);
		
	}

	@FindBy(xpath = "//fieldset[contains(., 'Coverage E')]/div//td[.='Limit']/following-sibling::td/input[contains(@id, ':limitTerm-inputEl')]")
	private WebElement editbox_SquirePropertyCoveragesCoverageELimit;
	public void setCoverageELimit(double limit) {
		setText(editbox_SquirePropertyCoveragesCoverageELimit, String.valueOf(limit));
	}

	@FindBy(xpath = "//a[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOCoveragesPanelSet:CoveragesCardTab')]")
	private WebElement link_SquirePropertyCoveragesCoverages;
	public void clickBuildingsCoverages() {
		clickWhenClickable(link_SquirePropertyCoveragesCoverages);
	}

	private Guidewire8Select select_SquirePropertyCoveragesCoverageECoverageType() {
		return new Guidewire8Select(driver, "//div[contains(text(), 'Coverage E')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Coverage Type')]/parent::td/following-sibling::td/table");
	}


	public void setCoverageECoverageType(CoverageType type) {
		select_SquirePropertyCoveragesCoverageECoverageType().selectByVisibleText(type.getValue());
	}

	@FindBy(xpath = "//div[contains(text(), 'Other Structures')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Detached Garage - Additional Value')]/parent::td/following-sibling::td/input[contains(@id,':DirectTermInput-inputEl')]")
	private WebElement editbox_SquirePropertyCoveragesOtherStructuresAdditionalValue;
	public void setOtherStructureDetachedGarageAdditionalValue(double value) {
		clickWhenClickable(editbox_SquirePropertyCoveragesOtherStructuresAdditionalValue);
		setText(editbox_SquirePropertyCoveragesOtherStructuresAdditionalValue, String.valueOf(value));
	}

	@FindBy(xpath = "//div[contains(text(), 'Coverage E')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Coverage Type')]/parent::td/following-sibling::td")
	private WebElement text_CoverageEType;
	public String getCoverageETypeText() {
		waitUntilElementIsVisible(text_CoverageEType);
		return text_CoverageEType.getText();
	}

	private Guidewire8Select select_SquirePropertyCoveragesCoverageEValuationMethod() {
		return new Guidewire8Select(driver, "//div[contains(text(), 'Coverage E')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Valuation Method')]/parent::td/following-sibling::td/table");
	}
	public void setCoverageEValuation(ValuationMethod method) {
		select_SquirePropertyCoveragesCoverageEValuationMethod().selectByVisibleText(method.getValue());
	}

	public String getCoverageECoverageTypeValue() {
		return select_SquirePropertyCoveragesCoverageECoverageType().getText();
	}

	public List<String> getCoverageECoverageTypeValues() {
		return select_SquirePropertyCoveragesCoverageECoverageType().getList();
	}


	public boolean isCoverageECoverageTypeEditable() {
		try {
			return select_SquirePropertyCoveragesCoverageECoverageType().isEnabled();
		} catch (Exception e) {
			return false;
		}
	}

	private Guidewire8Checkbox checkbox_squirePropertyCoveragesSeweageBackupSystem() {
		return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Sewage System Backup')]/preceding-sibling::table");
	}
	public boolean checkSewegeBackupSystem(boolean yesno) {
		try {
			
			checkbox_squirePropertyCoveragesSeweageBackupSystem().select(yesno);
			
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	Guidewire8RadioButton radio_IncludeMasonry() {
		return new Guidewire8RadioButton(driver, "//label[contains(text(), 'Include Masonry?')]/parent::td/following-sibling::td/div[contains(@id, ':LineCovs:CoverageInputSet:CovPatternInputGroup:1:CovTermInputSet:BooleanTermInput-containerEl')]/table");
	}
	public void setIncludeMasonry(boolean yesNo) {
		radio_IncludeMasonry().select(yesNo);
		
	}

	private WebElement checkbox_ExclusionCondition(String labelName) {
		return find(By.xpath("//div/div[text()='" + labelName + "']/preceding-sibling::table"));
	}
	private Guidewire8Checkbox earthquake() {
		return new Guidewire8Checkbox(driver, "//div/div[text()='Earthquake']/preceding-sibling::table");
	}
	public boolean checkEarthquake(boolean checked) throws GuidewireNavigationException {
		earthquake().select(checked);
		if(checked) {
			new GuidewireHelpers(driver).isOnPage("//label[contains(text(), 'Include Masonry?')]", 5000, "SUB QUESTIONS DIDN'T DISPLAY AFTER CHECKING EARTHQUAKE COVEAGE");
		}
		return checked;
	}	
	
	private Guidewire8Checkbox coverageADwelingsDetachedGarageAndStorageShed_107() {
		return new Guidewire8Checkbox(driver, "//div/div[text()='Coverage A (Dwellings) Detached Garage and Storage Shed - 107']/preceding-sibling::table");
	}
	
	public boolean clickDetachedGarageEndorsement(boolean checked) {
		coverageADwelingsDetachedGarageAndStorageShed_107().select(checked);
		return checked;
	}

	private Guidewire8Checkbox specifiedPropertyExclusionEndorsement_110() {
		return new Guidewire8Checkbox(driver, "//div/div[text()='Specified Property Exclusion Endorsement - 110']/preceding-sibling::table");
	}
	private boolean checkSpecifiedPropertyExclusionEndorsement_110(boolean checked) {
		specifiedPropertyExclusionEndorsement_110().select(checked);
		return checked;
	}
	
	public void fillOutSpecifiedPropertyExclusionEndorsement_110(ArrayList<String> descriptions) {
		
		checkSpecifiedPropertyExclusionEndorsement_110(true);

		for (String desc : descriptions) {
			clickSpecifiedPropertyExclusionEndorsement110Add();
			setSpecifiedPropertyExclusionEndorsement110Description(desc);
		}
	}	

	final String name110 = "Specified Property Exclusion Endorsement - 110";
	@FindBy(xpath = "//fieldset[contains(., '" + name110 + "')]//div[contains(@id, ':CoverageInputSet:CovPatternInputGroup:ScheduleInputSet:') and substring(@id, string-length(@id) - 4) = '-body']//table[contains(@id, 'gridview-')]//tr[last()]/td[2]/div")
	public WebElement div_SpecifiedPropertyExclusionEndorsement110Description;
	@FindBy(xpath = "//fieldset[contains(., '" + name110 + "')]//div[contains(@id, ':CoverageInputSet:CovPatternInputGroup:ScheduleInputSet:')]//table[contains(@id, 'textfield-')]//tr[last()]/td[2]/input")
	public WebElement editbox_SpecifiedPropertyExclusionEndorsement110Description;
	@FindBy(xpath = "//fieldset[contains(., '" + name110 + "')]//a[contains(@id, 'CoverageInputSet:CovPatternInputGroup:ScheduleInputSet:Add')]")
	private WebElement button_SpecifiedPropertyExclusionEndorsement110Add;
	private void setSpecifiedPropertyExclusionEndorsement110Description(String description) {
		clickWhenClickable(div_SpecifiedPropertyExclusionEndorsement110Description, 5000);
		waitUntilElementIsClickable(editbox_SpecifiedPropertyExclusionEndorsement110Description, 5000);
		editbox_SpecifiedPropertyExclusionEndorsement110Description.sendKeys(description);
	}

	private void clickSpecifiedPropertyExclusionEndorsement110Add() {
		clickWhenClickable(button_SpecifiedPropertyExclusionEndorsement110Add);
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

	public boolean checkIfSewageSystemBackupExists() {
		return checkIfElementExists("//fieldset[contains(., 'Sewage System Backup') and contains(., 'Limit') and contains(., '10% - ')]", 2000);
	}

	@FindBy(xpath = "//div[contains(@id, ':LineCovs:CoverageInputSet:CovPatternInputGroup:Limit-inputEl')]")
	private WebElement text_SquirePropertyCoveragesSewageBackupLimit;
	public String getSewageBackupSystemLimit() {
		return text_SquirePropertyCoveragesSewageBackupLimit.getText();
	}

	private Guidewire8Select select_SquirePropertyCoveragesOtherCoverageValuation() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':HOCoverageInputSet:OthersCovPatternInputGroup:OptionTermInput-triggerWrap') or contains(@id,':HOCoveragesPanelSet:HOMainCoveragesHOEPanelSet:1:HOCoverageInputSet:CovAScreenCovPatternInputGroup:0:CovTermInputSet:OptionTermInput-triggerWrap')]");
	}
	public void selectOtherCoverageCValuation(ValuationMethod cov) {
		
		select_SquirePropertyCoveragesOtherCoverageValuation().selectByVisibleTextPartial(cov.getValue());
		
	}

	@FindBy(xpath = "//label[contains(text(),'Add Weight of Ice and Snow')]")
	private WebElement label_WeightOfIceAndSnowCoverage;
	public boolean checkAddWeightOfIceAndSnowCoverageExists() {
		try {
			waitUntilElementIsVisible(label_WeightOfIceAndSnowCoverage, 1000);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@FindBy(xpath = "//div[contains(@id,':LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOCoveragesPanelSet:3_ref')]")
	private WebElement div_Properties;
	public String getSelectedBuildingName() {
		int row = tableUtils.getHighlightedRowNumber(div_Properties);
		return (tableUtils.getCellTextInTableByRowAndColumnName(div_Properties, row, "Property Type"));
	}

	@FindBy(xpath = "//label[contains(text(),'Add Glass Coverage')]")
	private WebElement label_AddGlassCoverage;
	public boolean checkAddGlassCoverageExists() {
		try {
			waitUntilElementIsVisible(label_AddGlassCoverage, 1000);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public int getBuildingNumber(PropertyTypePL Property) {
		int row = tableUtils.getRowNumberInTableByText(div_Properties, Property.getValue());
		return row;
	}

	private Guidewire8Checkbox checkbox_SquirePropertyCoveragesOptionalCoverageC() {
		return new Guidewire8Checkbox(driver, "//div/div[text()='Coverage C']/preceding-sibling::table[contains(@id, ':HOCoverageInputSet:OthersCovPatternInputGroup-legendChk')]");
	}
	public void checkOptionalCoverageC(boolean trueFalse) {
		
		checkbox_SquirePropertyCoveragesOptionalCoverageC().select(trueFalse);
		
	}
	private Guidewire8RadioButton radio_AddWeightOfIceAndSnow() {
		return new Guidewire8RadioButton(driver, "//label[contains(text(),'Add Weight of Ice and Snow')]/parent::td/parent::tr/parent::tbody/parent::table");
	}

	public boolean getAddWeightOfIceAndSnow(boolean trueFalse){
		return radio_AddWeightOfIceAndSnow().isSelected(trueFalse);
	}
}

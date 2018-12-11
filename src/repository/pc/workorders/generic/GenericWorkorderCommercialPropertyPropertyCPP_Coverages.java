package repository.pc.workorders.generic;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.CommercialProperty.*;
import repository.gw.generate.custom.CPPCommercialProperty_Building;
import repository.gw.generate.custom.CPPCommercialProperty_Building_Coverages;

import java.util.List;

public class GenericWorkorderCommercialPropertyPropertyCPP_Coverages extends GenericWorkorderCommercialPropertyPropertyCPP {

	private WebDriver driver;

	public GenericWorkorderCommercialPropertyPropertyCPP_Coverages(WebDriver driver){
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	//getRadioXpath("", "")
	private String getRadioXpath(String coverage, String title) {
		return "//div[contains(text(), '" + coverage + "')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), '" + title + "')]/parent::td/following-sibling::td";
	}

	//getSelectXpath("", "")
	private String getSelectXpath(String coverage, String title) {
		return "//div[contains(text(), '" + coverage + "')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), '" + title + "')]/parent::td/following-sibling::td/table";
	}


	public void fillOutPropertyCoverages(CPPCommercialProperty_Building building) throws Exception {
		clickCoveragesTab(building);

		List<WebElement> checkedBoxes = finds(By.xpath("//table[contains(@class, 'cb-checked')]/tbody/tr/child::td/div/input"));
		while (!checkedBoxes.isEmpty()) {
			checkedBoxes.get(0).click();
			
			checkedBoxes = finds(By.xpath("//table[contains(@class, 'cb-checked')]/tbody/tr/child::td/div/input"));
		}
		List<PropertyCoverages> buildngList = building.getCoverages().getBuildingCoverageList();
		for (PropertyCoverages coverage : buildngList) {
			switch (coverage) {
			case BuildingCoverage:
				if (checkBuildingCoverages(true)) {
					fillOutBuildingCoverages(building);
				}
				break;
			case BusinessPersonalPropertyCoverage:
				if (checkBusinessPersonalPropertyCoverage(true)) {
					fillOutBusinessPersonProperty(building);
				}
				break;
			case BuildersRiskCoverageForm_CP_00_20:
				if (checkBuildersRiskCoverageForm_CP_00_20(true)) {
					fillOutBuildersRiskCoverageForm_CP_00_20(building);
				}
				break;
			case PropertyInTheOpen:
				if (checkPropertyInTheOpen(true)) {
					fillOutPropertyInTheOpen(building);
				}
				break;
			case PersonalPropertyOfOthers:
				if (checkPropertyOfOthers(true)) {
					fillOutPropertyOfOthers(building);
				}
				break;
			case LegalLiabilityCoverageForm_CP_00_40:
				if (checkLegalLiabilityCoverageForm_CP_00_40(true)) {
					fillOutLegalLiabilityCoverageForm_CP_00_40(building);
				}
				break;
			case CondominiumAssociationCoverageForm_CP_00_17:
				break;
			case CondominiumCommercialUnit_OwnersCoverageForm_CP_00_18:
				break;
			}//end switch
		}//end for
	}//end fillOutPropertyCoverages(CPPCommercialProperty_Building building)


	public void fillOutBuildingCoverages(CPPCommercialProperty_Building building) {
		CPPCommercialProperty_Building_Coverages coverages = building.getCoverages();
		setBuildingCoverages_Limit(coverages.getBuildingCoverage_Limit());
		selectBuildingCoverages_CauseOfLoss(coverages.getBuildingCoverage_CauseOfLoss());
		selectBuildingCoverages_Deductible(coverages.getBuildingCoverage_Deductible());
		selectBuildingCoverages_ValuationMethod(coverages.getBuildingCoverage_ValuationMethod());
		selectBuildingCoverages_Coinsurance(coverages.getBuildingCoverage_Coinsurance());
		selectBuildingCoverages_AutoIncrease(coverages.getBuildingCoverage_AutoIncrease());
	}

	public void fillOutBusinessPersonProperty(CPPCommercialProperty_Building building) {
		CPPCommercialProperty_Building_Coverages coverages = building.getCoverages();
		setBusinessPersonalPropertyCoverage_Limit(coverages.getBusinessPersonalPropertyCoverage_Limit());
		selectBusinessPersonalPropertyCoverage_CauseOfLoss(coverages.getBusinessPersonalPropertyCoverage_CauseOfLoss());
		selectBusinessPersonalPropertyCoverage_Deductible(coverages.getBusinessPersonalPropertyCoverage_Deductible());
		selectBusinessPersonalPropertyCoverage_ValuationMethod(coverages.getBusinessPersonalPropertyCoverage_ValuationMethod());
		selectBusinessPersonalPropertyCoverage_Coinsurance(coverages.getBusinessPersonalPropertyCoverage_Coinsurance());
		selectBusinessPersonalPropertyCoverage_AutoIncrease(coverages.getBusinessPersonalPropertyCoverage_AutoIncreasePercent());
		setBusinessPersonalPropertyCoverage_IsStockIncluded(coverages.isBusinessPersonalPropertyCoverage_IsStockIncluded());
	}

	public void fillOutBuildersRiskCoverageForm_CP_00_20(CPPCommercialProperty_Building building) {
		CPPCommercialProperty_Building_Coverages coverages = building.getCoverages();
		setBuildersRiskCoverageForm_CP_00_20_Limit(coverages.getBuildersRiskCoverageForm_CP_00_20_Limit());
		selectBuildersRiskCoverageForm_CP_00_20_CauseOfLoss(coverages.getBuildersRiskCoverageForm_CP_00_20_CauseOfLoss());
		selectBuildersRiskCoverageForm_CP_00_20_Deductible(coverages.getBuildersRiskCoverageForm_CP_00_20_Deductible());
		if (!setBuildersRiskCoverageForm_CP_00_20_IsBuildingARenovation(coverages.isBuildingARenovation())) {
			setBuildersRiskCoverageForm_CP_00_20_ConstructionPastFoundationStage(coverages.isConstructionPastFoundationStage());
		}

	}

	public void fillOutPropertyInTheOpen(CPPCommercialProperty_Building building) {
		CPPCommercialProperty_Building_Coverages coverages = building.getCoverages();
		setPropertyInTheOpen_Limit(coverages.getPropertyInTheOpen_Limit());
		selectPropertyInTheOpen_CauseOfLoss(coverages.getPropertyInTheOpen_CauseOfLoss());
		selectPropertyInTheOpen_Deductible(coverages.getPropertyInTheOpen_Deductible());
		selectPropertyInTheOpen_ValuationMethod(coverages.getPropertyInTheOpen_ValuationMethod());
		selectPropertyInTheOpen_Coinsurance(coverages.getPropertyInTheOpen_Coinsurance());
		selectPropertyInTheOpen_AutoIncrease(coverages.getPropertyInTheOpen_AutoIncrease());
	}

	public void fillOutPropertyOfOthers(CPPCommercialProperty_Building building) {
		CPPCommercialProperty_Building_Coverages coverages = building.getCoverages();
		setPropertyOfOthers_Limit(coverages.getPropertyOfOthers_Limit());
		selectPropertyOfOthers_CauseOfLoss(coverages.getPropertyOfOthers_CauseOfLoss());
		selectPropertyOfOthers_Deductible(coverages.getPropertyOfOthers_Deductible());
		selectPropertyOfOthers_Cosinsurance(coverages.getPropertyOfOthers_Coinsurance());
	}

	public void fillOutLegalLiabilityCoverageForm_CP_00_40(CPPCommercialProperty_Building building) {
		CPPCommercialProperty_Building_Coverages coverages = building.getCoverages();
		setLegalLiabilityCoverageForm_CP_00_40_Limit(coverages.getLegalLiabilityCoverageForm_CP_00_40_Limit());
		//		selectLegalLiabilityCoverageForm_CP_00_40_CauseOfLoss(coverages.getLegalLiabilityCoverageForm_CP_00_40_CauseOfLoss());
	}


	/////////////////////
	// BUILDING COVERAGES
	/////////////////////
	Guidewire8Checkbox checkbox_BuildingCoverages() {
		return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Building Coverage')]/preceding-sibling::table");
	}

	public boolean checkBuildingCoverages(boolean checked) {
		checkbox_BuildingCoverages().select(checked);
		
		return checked;
	}

	@FindBy(xpath = "//div[contains(text(), 'Building Coverage')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Limit')]/parent::td/following-sibling::td/input")
	private WebElement editbox_BuildingCoverages_Limit;

	private void setBuildingCoverages_Limit(String text) {
		setText(editbox_BuildingCoverages_Limit, text);
		
	}

	Guidewire8Select select_BuildingCoverages_CauseOfLoss() {
		return new Guidewire8Select(driver, "//label[contains(text(), 'Cause Of Loss')]/parent::td/following-sibling::td/table");
	}


	private void selectBuildingCoverages_CauseOfLoss(BuildingCoverageCauseOfLoss causeOfLoss) {
		Guidewire8Select mySelect = select_BuildingCoverages_CauseOfLoss();
		mySelect.selectByVisibleText(causeOfLoss.getValue());
		
	}

	Guidewire8Select select_BuildingCoverages_Deductible() {
		return new Guidewire8Select(driver, getSelectXpath("Building Coverage", "Deductible"));
	}


	private void selectBuildingCoverages_Deductible(BuildingCoverageDeductible deductible) {
		Guidewire8Select mySelect = select_BuildingCoverages_Deductible();
		mySelect.selectByVisibleText(deductible.getValue());
		
	}

	Guidewire8Select select_BuildingCoverages_ValuationMethod() {
		return new Guidewire8Select(driver, getSelectXpath("Building Coverage", "Valuation Method"));
	}


	private void selectBuildingCoverages_ValuationMethod(BuildingCoverageValuationMethod valuationMethod) {
		Guidewire8Select mySelect = select_BuildingCoverages_ValuationMethod();
		mySelect.selectByVisibleText(valuationMethod.getValue());
		
	}

	Guidewire8Select select_BuildingCoverages_Coinsurance() {
		return new Guidewire8Select(driver, getSelectXpath("Building Coverage", "Coinsurance %"));
	}


	private void selectBuildingCoverages_Coinsurance(BuildingCoverageCoinsurancePercent coinsurance) {
		Guidewire8Select mySelect = select_BuildingCoverages_Coinsurance();
		mySelect.selectByVisibleText(coinsurance.getValue());
		
	}

	Guidewire8Select select_BuildingCoverages_AutoIncrease() {
		return new Guidewire8Select(driver, getSelectXpath("Building Coverage", "Auto Increase %"));
	}


	private void selectBuildingCoverages_AutoIncrease(BuildingCoverageAutoIncreasePercent autoIncrease) {
		Guidewire8Select mySelect = select_BuildingCoverages_AutoIncrease();
		mySelect.selectByVisibleText(autoIncrease.getValue());
		
	}

	@FindBy(xpath = "//div[contains(text(), 'Building Coverage')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Basic Group I Rate')]/parent::td/following-sibling::td/input")
	private WebElement editbox_BuildingCoverages_BasicGroup1Rate;
	@FindBy(xpath = "//div[contains(text(), 'Building Coverage')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Basic Group II Rate')]/parent::td/following-sibling::td/input")
	private WebElement editbox_BuildingCoverages_BasicGroup2Rate;
	@FindBy(xpath = "//div[contains(text(), 'Building Coverage')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Basic Group II Symbol')]/parent::td/following-sibling::td/textarea")
	private WebElement editbox_BuildingCoverages_BasicGroup2Symbol;
	@FindBy(xpath = "//div[contains(text(), 'Building Coverage')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Prefix Number')]/parent::td/following-sibling::td/input")
	private WebElement editbox_BuildingCoverages_PrefixNumber;

	/////////////////////////////////////
	//BUSINESS PERSONAL PROPERTY COVERAGE
	/////////////////////////////////////
	Guidewire8Checkbox checkbox_BusinessPersonalPropertyCoverage() {
		return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Business Personal Property Coverage')]/preceding-sibling::table");
	}

	public boolean checkBusinessPersonalPropertyCoverage(boolean checked) {
		checkbox_BusinessPersonalPropertyCoverage().select(checked);
		
		return checked;
	}

	@FindBy(xpath = "//div[contains(text(), 'Business Personal Property Coverage')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Limit')]/parent::td/following-sibling::td/input")
	private WebElement editbox_BusinessPersonalPropertyCoverage_Limit;

	private void setBusinessPersonalPropertyCoverage_Limit(String text) {
		setText(editbox_BusinessPersonalPropertyCoverage_Limit, text);
		
	}

	Guidewire8Select select_BusinessPersonalPropertyCoverage_CauseOfLoss() {
		return new Guidewire8Select(driver, getSelectXpath("Business Personal Property Coverage", "Cause Of Loss"));
	}

	private void selectBusinessPersonalPropertyCoverage_CauseOfLoss(BusinessPersonalPropertyCoverageCauseOfLoss causeofloss) {
		Guidewire8Select mySelect = select_BusinessPersonalPropertyCoverage_CauseOfLoss();
		mySelect.selectByVisibleText(causeofloss.getValue());
		
	}

	Guidewire8Select select_BusinessPersonalPropertyCoverage_Deductible() {
		return new Guidewire8Select(driver, getSelectXpath("Business Personal Property Coverage", "Deductible"));
	}

	private void selectBusinessPersonalPropertyCoverage_Deductible(BusinessPersonalPropertyCoverageDeductible deductible) {
		Guidewire8Select mySelect = select_BusinessPersonalPropertyCoverage_Deductible();
		mySelect.selectByVisibleText(deductible.getValue());
		
	}

	Guidewire8Select select_BusinessPersonalPropertyCoverage_ValuationMethod() {
		return new Guidewire8Select(driver, getSelectXpath("Business Personal Property Coverage", "Valuation Method"));
	}

	private void selectBusinessPersonalPropertyCoverage_ValuationMethod(BusinessPersonalPropertyCoverageValuationMethod valuationMethod) {
		Guidewire8Select mySelect = select_BusinessPersonalPropertyCoverage_ValuationMethod();
		mySelect.selectByVisibleText(valuationMethod.getValue());
		
	}

	Guidewire8Select select_BusinessPersonalPropertyCoverage_Coinsurance() {
		return new Guidewire8Select(driver, getSelectXpath("Business Personal Property Coverage", "Coinsurance %"));
	}

	private void selectBusinessPersonalPropertyCoverage_Coinsurance(BusinessPersonalPropertyCoverageCoinsurancePercent coinsurance) {
		Guidewire8Select mySelect = select_BusinessPersonalPropertyCoverage_Coinsurance();
		mySelect.selectByVisibleText(coinsurance.getValue());
		
	}

	Guidewire8Select select_BusinessPersonalPropertyCoverage_AutoIncrease() {
		return new Guidewire8Select(driver, getSelectXpath("Business Personal Property Coverage", "Auto Increase %"));
	}

	private void selectBusinessPersonalPropertyCoverage_AutoIncrease(BusinessPersonalPropertyCoverageAutoIncreasePercent autoIncrease) {
		Guidewire8Select mySelect = select_BusinessPersonalPropertyCoverage_AutoIncrease();
		mySelect.selectByVisibleText(autoIncrease.getValue());
		
	}

	Guidewire8RadioButton radio_BusinessPersonalPropertyCoverage_IsStockIncluded() {
		return new Guidewire8RadioButton(driver, getRadioXpath("Business Personal Property Coverage", "Is Stock Included?"));
	}

	private void setBusinessPersonalPropertyCoverage_IsStockIncluded(boolean yesno) {
		radio_BusinessPersonalPropertyCoverage_IsStockIncluded().select(yesno);
		
	}

	@FindBy(xpath = "//div[contains(text(), 'Business Personal Property Coverage')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Basic Group I Rate')]/parent::td/following-sibling::td/input")
	private WebElement editbox_BusinessPersonalPropertyCoverage_BasicGroup1Rate;

	@FindBy(xpath = "//div[contains(text(), 'Business Personal Property Coverage')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Basic Group II Rate')]/parent::td/following-sibling::td/input")
	private WebElement editbox_BusinessPersonalPropertyCoverage_BasicGroup2Rate;

	@FindBy(xpath = "//div[contains(text(), 'Business Personal Property Coverage')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Basic Group II Symbol')]/parent::td/following-sibling::td/textarea")
	private WebElement editbox_BusinessPersonalPropertyCoverage_BasicGroup2Symbol;

	@FindBy(xpath = "//div[contains(text(), 'Business Personal Property Coverage')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Prefix Number')]/parent::td/following-sibling::td/input")
	private WebElement editbox_BusinessPersonalPropertyCoverage_PrefixNumber;

	//////////////////////////////////////
	//BUILDERS RISK COVERAGE FORM CP 00 20
	//////////////////////////////////////
	Guidewire8Checkbox checkbox_BuildersRiskCoverageForm_CP_00_20() {
		return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Risk Coverage Form CP 00 20')]/preceding-sibling::table");
	}

	public boolean checkBuildersRiskCoverageForm_CP_00_20(boolean checked) {
		checkbox_BuildersRiskCoverageForm_CP_00_20().select(checked);
		
		return checked;
	}

	@FindBy(xpath = "//div[contains(text(), 'Risk Coverage Form CP 00 20')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Limit')]/parent::td/following-sibling::td/input")
	private WebElement editbox_BuildersRiskCoverageForm_CP_00_20_Limit;

	private void setBuildersRiskCoverageForm_CP_00_20_Limit(int limit) {
		setText(editbox_BuildersRiskCoverageForm_CP_00_20_Limit, String.valueOf(limit));
		
	}

	Guidewire8Select select_BuildersRiskCoverageForm_CP_00_20_CauseOfLoss() {
		return new Guidewire8Select(driver, getSelectXpath("Risk Coverage Form CP 00 20", "Cause Of Loss"));
	}

	private void selectBuildersRiskCoverageForm_CP_00_20_CauseOfLoss(BuildersRiskCoverageFormCP0020CauseOfLoss causeofloss) {
		Guidewire8Select mySelect = select_BuildersRiskCoverageForm_CP_00_20_CauseOfLoss();
		mySelect.selectByVisibleText(causeofloss.getValue());
		
	}

	Guidewire8Select select_BuildersRiskCoverageForm_CP_00_20_Deductible() {
		return new Guidewire8Select(driver, getSelectXpath("Risk Coverage Form CP 00 20", "Deductible"));
	}

	private void selectBuildersRiskCoverageForm_CP_00_20_Deductible(BuildersRiskCoverageFormCP0020Deductible deductible) {
		Guidewire8Select mySelect = select_BuildersRiskCoverageForm_CP_00_20_Deductible();
		mySelect.selectByVisibleText(deductible.getValue());
		
	}

	@FindBy(xpath = "//div[contains(text(), 'Risk Coverage Form CP 00 20')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Valuation Method')]/parent::td/following-sibling::td/div")
	private WebElement textbox_BuildersRiskCoverageForm_CP_00_20_ValuationMethod;

	@FindBy(xpath = "//div[contains(text(), 'Risk Coverage Form CP 00 20')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Coinsurance %')]/parent::td/following-sibling::td/div")
	private WebElement textbox_BuildersRiskCoverageForm_CP_00_20_CoinsureancePercent;

	Guidewire8RadioButton radio_BuildersRiskCoverageForm_CP_00_20_ConstructionPastFoundationStage() {
		return new Guidewire8RadioButton(driver, getRadioXpath("Risk Coverage Form CP 00 20", "Has the construction progressed past the foundation stage"));
	}

	private void setBuildersRiskCoverageForm_CP_00_20_ConstructionPastFoundationStage(boolean yesno) {
		radio_BuildersRiskCoverageForm_CP_00_20_ConstructionPastFoundationStage().select(yesno);
		
	}

	@FindBy(xpath = "//div[contains(text(), 'Risk Coverage Form CP 00 20')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Basic Group I Rate')]/parent::td/following-sibling::td/input")
	private WebElement editbox_BuildersRiskCoverageForm_CP_00_20_BasicGroup1Rate;

	@FindBy(xpath = "//div[contains(text(), 'Risk Coverage Form CP 00 20')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Basic Group II Rate')]/parent::td/following-sibling::td/input")
	private WebElement editbox_BuildersRiskCoverageForm_CP_00_20_BasicGroup2Rate;

	@FindBy(xpath = "//div[contains(text(), 'Risk Coverage Form CP 00 20')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Basic Group II Symbol')]/parent::td/following-sibling::td/textarea")
	private WebElement editbox_BuildersRiskCoverageForm_CP_00_20_BasicGroup2Symbol;

	@FindBy(xpath = "//div[contains(text(), 'Risk Coverage Form CP 00 20')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Prefix Number')]/parent::td/following-sibling::td/input")
	private WebElement editbox_BuildersRiskCoverageForm_CP_00_20_PrefixNumber;

	Guidewire8RadioButton radio_BuildersRiskCoverageForm_CP_00_20_IsBuildingARenovation() {
		return new Guidewire8RadioButton(driver, getRadioXpath("Risk Coverage Form CP 00 20", "Is this building a renovation"));
	}

	private boolean setBuildersRiskCoverageForm_CP_00_20_IsBuildingARenovation(boolean yesno) {
		radio_BuildersRiskCoverageForm_CP_00_20_IsBuildingARenovation().select(yesno);
		
		return yesno;
	}


	//////////////////////
	//PROPERTY IN THE OPEN
	//////////////////////
	Guidewire8Checkbox checkbox_PropertyInTheOpen() {
		return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Property In The Open')]/preceding-sibling::table");
	}

	public boolean checkPropertyInTheOpen(boolean checked) {
		checkbox_PropertyInTheOpen().select(checked);
		
		return checked;
	}

	@FindBy(xpath = "//div[contains(text(), 'Property In The Open')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Limit')]/parent::td/following-sibling::td/input")
	private WebElement editbox_PropertyInTheOpen_Limit;

	private void setPropertyInTheOpen_Limit(int limit) {
		setText(editbox_PropertyInTheOpen_Limit, String.valueOf(limit));
		
	}

	Guidewire8Select select_PropertyInTheOpen_CauseOfLoss() {
		return new Guidewire8Select(driver, getSelectXpath("Property In The Open", "Cause Of Loss"));
	}

	private void selectPropertyInTheOpen_CauseOfLoss(PropertyInTheOpenCauseOfLoss causeofloss) {
		Guidewire8Select mySelect = select_PropertyInTheOpen_CauseOfLoss();
		mySelect.selectByVisibleText(causeofloss.getValue());
		
	}

	Guidewire8Select select_PropertyInTheOpen_Deductible() {
		return new Guidewire8Select(driver, getSelectXpath("Property In The Open", "Deductible"));
	}

	private void selectPropertyInTheOpen_Deductible(PropertyInTheOpenDeductible deductible) {
		Guidewire8Select mySelect = select_PropertyInTheOpen_Deductible();
		mySelect.selectByVisibleText(deductible.getValue());
		
	}

	Guidewire8Select select_PropertyInTheOpen_ValuationMethod() {
		return new Guidewire8Select(driver, getSelectXpath("Property In The Open", "Valuation Method"));
	}

	private void selectPropertyInTheOpen_ValuationMethod(PropertyInTheOpenValuationMethod valuationMethod) {
		Guidewire8Select mySelect = select_PropertyInTheOpen_ValuationMethod();
		mySelect.selectByVisibleText(valuationMethod.getValue());
		
	}

	Guidewire8Select select_PropertyInTheOpen_Coinsurance() {
		return new Guidewire8Select(driver, getSelectXpath("Property In The Open", "Coinsurance %"));
	}

	private void selectPropertyInTheOpen_Coinsurance(PropertyInTheOpenCoinsurancePercent coinsurance) {
		Guidewire8Select mySelect = select_PropertyInTheOpen_Coinsurance();
		mySelect.selectByVisibleText(coinsurance.getValue());
		
	}

	Guidewire8Select select_PropertyInTheOpen_AutoIncrease() {
		return new Guidewire8Select(driver, getSelectXpath("Property In The Open", "Auto Increase %"));
	}

	private void selectPropertyInTheOpen_AutoIncrease(PropertyInTheOpenAutoIncreasePercent autoIncrease) {
		Guidewire8Select mySelect = select_PropertyInTheOpen_AutoIncrease();
		mySelect.selectByVisibleText(autoIncrease.getValue());
		
	}

	@FindBy(xpath = "//div[contains(text(), 'Property In The Open')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Usage Description')]/parent::td/following-sibling::td/input")
	private WebElement editbox_PropertyInTheOpen_UsageDescription;

	/////////////////////
	//PROPERTY OF OTHERS
	/////////////////////
	Guidewire8Checkbox checkbox_PropertyOfOthers() {
		return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Property Of Others')]/preceding-sibling::table");
	}

	private boolean checkPropertyOfOthers(boolean checked) {
		checkbox_PropertyOfOthers().select(checked);
		return checked;
	}

	@FindBy(xpath = "//div[contains(text(), 'Property Of Others')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Limit')]/parent::td/following-sibling::td/input")
	private WebElement editbox_PropertyOfOthers_Limit;

	private void setPropertyOfOthers_Limit(int limit) {
		setText(editbox_PropertyOfOthers_Limit, String.valueOf(limit));
		
	}

	Guidewire8Select select_PropertyOfOthers_CauseOfLoss() {
		return new Guidewire8Select(driver, getSelectXpath("Property Of Others", "Cause Of Loss"));
	}

	private void selectPropertyOfOthers_CauseOfLoss(PropertyOfOthersCauseOfLoss causeOfLoss) {
		Guidewire8Select mySelect = select_PropertyOfOthers_CauseOfLoss();
		mySelect.selectByVisibleText(causeOfLoss.getValue());
		
	}

	Guidewire8Select select_PropertyOfOthers_Deductible() {
		return new Guidewire8Select(driver, getSelectXpath("Property Of Others", "Deductible"));
	}

	private void selectPropertyOfOthers_Deductible(PropertyOfOthersDeductible deductible) {
		Guidewire8Select mySelect = select_PropertyOfOthers_Deductible();
		mySelect.selectByVisibleText(deductible.getValue());
		
	}

	@FindBy(xpath = "//div[contains(text(), 'Property Of Others')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Valuation Method')]/parent::td/following-sibling::td/div")
	private WebElement editbox_PropertyOfOthers_ValuationMethod;

	Guidewire8Select select_PropertyOfOthers_Cosinsurance() {
		return new Guidewire8Select(driver, getSelectXpath("Property Of Others", "Coinsurance %"));
	}

	private void selectPropertyOfOthers_Cosinsurance(PropertyOfOthersCoinsurancePercent coinsurance) {
		Guidewire8Select mySelect = select_PropertyOfOthers_Cosinsurance();
		mySelect.selectByVisibleText(coinsurance.getValue());
		
	}

	@FindBy(xpath = "//div[contains(text(), 'Risk Coverage Form CP 00 20')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Basic Group I Rate')]/parent::td/following-sibling::td/input")
	private WebElement editbox_PropertyOfOthers_BasicGroup1Rate;

	@FindBy(xpath = "//div[contains(text(), 'Risk Coverage Form CP 00 20')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Basic Group II Rate')]/parent::td/following-sibling::td/input")
	private WebElement editbox_PropertyOfOthers_BasicGroup2Rate;

	@FindBy(xpath = "//div[contains(text(), 'Risk Coverage Form CP 00 20')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Basic Group II Symbol')]/parent::td/following-sibling::td/textarea")
	private WebElement editbox_PropertyOfOthers_BasicGroup2Symbol;
	@FindBy(xpath = "//div[contains(text(), 'Risk Coverage Form CP 00 20')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Prefix Number')]/parent::td/following-sibling::td/input")
	private WebElement editbox_PropertyOfOthers_PrefixNumber;

	////////////////////////////////////////
	//LEGAL LIABILITY COVERAGE FORM CP 00 40
	////////////////////////////////////////
	Guidewire8Checkbox checkbox_LegalLiabilityCoverageForm_CP_00_40() {
		return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Legal Liability Coverage Form CP 00 40')]/preceding-sibling::table");
	}

	public boolean checkLegalLiabilityCoverageForm_CP_00_40(boolean checked) {
		checkbox_LegalLiabilityCoverageForm_CP_00_40().select(checked);
		return checked;
	}

	@FindBy(xpath = "//div[contains(text(), 'Legal Liability Coverage Form CP 00 40')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Limit')]/parent::td/following-sibling::td/input")
	private WebElement editbox_LegalLiabilityCoverageForm_CP_00_40_Limit;

	private void setLegalLiabilityCoverageForm_CP_00_40_Limit(int limit) {
		setText(editbox_LegalLiabilityCoverageForm_CP_00_40_Limit, String.valueOf(limit));
		
	}

	//	Guidewire8Select select_LegalLiabilityCoverageForm_CP_00_40_CauseOfLoss() {
	//		return new Guidewire8Select(getSelectXpath("Legal Liability Coverage Form CP 00 40", "Cause Of Loss"));
	//	}
	//	private void selectLegalLiabilityCoverageForm_CP_00_40_CauseOfLoss(LegalLiabilityCoverageFormCP0040CauseOfLoss causeOfLoss) {
	//		Guidewire8Select mySelect = select_LegalLiabilityCoverageForm_CP_00_40_CauseOfLoss();
	//		mySelect.selectByVisibleText(causeOfLoss.getValue());
	//		
	//	}
	//End of Coverages Tab


}

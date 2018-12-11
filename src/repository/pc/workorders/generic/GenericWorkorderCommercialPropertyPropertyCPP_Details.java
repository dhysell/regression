package repository.pc.workorders.generic;

import com.beust.jcommander.converters.BigDecimalConverter;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import persistence.globaldatarepo.entities.AddressTemp2;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.Building.*;
import repository.gw.enums.CommercialProperty.Condominium;
import repository.gw.enums.CommercialProperty.PropertyCoverages;
import repository.gw.enums.CommercialProperty.RateType;
import repository.gw.enums.Location.ProtectionClassCode;
import repository.gw.enums.Property.PrimaryHeating;
import repository.gw.generate.custom.CPPCommercialProperty_Building;
import repository.pc.sidemenu.SideMenuPC;

import java.math.BigDecimal;

public class GenericWorkorderCommercialPropertyPropertyCPP_Details extends BasePage {

	private WebDriver driver;

	public GenericWorkorderCommercialPropertyPropertyCPP_Details(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	GenericWorkorderCommercialPropertyPropertyCPP commercialPropertyProperty = new GenericWorkorderCommercialPropertyPropertyCPP(driver);
	GenericWorkorderBuildings buildings = new GenericWorkorderBuildings(driver);
	repository.pc.sidemenu.SideMenuPC sideMenu = new SideMenuPC(driver);


	private void isOnPage(CPPCommercialProperty_Building building) throws Exception {
		if (finds(By.xpath("//label[contains(@id, ':BuildingLocation-inputEl')]")).isEmpty()) {
			if (finds(By.xpath("//span[contains(@id, 'CPBuildingPopup:ttlBar')]")).isEmpty()) {
				sideMenu.clickSideMenuCPProperty();
				commercialPropertyProperty.editPropertyByNumber(building.getNumber());
			}
			commercialPropertyProperty.clickDetailsTab();
		}
	}


	/**
	 * @param building
	 * @author jlarsen
	 * @date 5/2/2017
	 * @description Fill out Property Information section of Property Details
	 */
	public void fillOutPropertyInformation(CPPCommercialProperty_Building building) {
		setUsageDescription(building.getUsageDescription());
		if (selectMultipleOccupancy(building.isMultipleOccupancy())) {
			
			selectMultipleOccupancyClassCode(building.getMultipleOccupancyClassCode());
		}
		selectFirstBuildingCodeResultClassCode(building.getClassCode());
		
		selectCondominium(building.getCondominium());
	}

	/**
	 * @param building
	 * @author jlarsen
	 * @date 5/2/2017
	 * @description Set Rating values on Property Details Page
	 */
	public void fillOutRating(CPPCommercialProperty_Building building) {

	}

	/**
	 * @param building
	 * @author jlarsen
	 * @date 5/2/2017
	 * @description Set Protective safe guards values on Property Details Page
	 */
	public void fillOutProtectiveSafegaurds(CPPCommercialProperty_Building building) {

	}

	/**
	 * @param building
	 * @author jlarsen
	 * @date 5/2/2017
	 * @description Set Public Protection values(Lat, Long, manual Protection class) on Property Details Page
	 */
	public void fillOutPublicProtection(CPPCommercialProperty_Building building) {
		selectManualProtectionClass(building);
	}

	/**
	 * @param building
	 * @throws Exception
	 * @author jlarsen
	 * @date 5/2/2017
	 * @description Set Building Characteristics values on Property Details Page
	 */
	public void fillOutBuildingCharacteristics(CPPCommercialProperty_Building building) throws Exception {
		isOnPage(building);
		selectConstructionType(building.getConstructionTypeCPP());
		setYearBuilt(building.getYearBuilt());
		setNumOfStories(building.getNumStories());
		setNumOfBasements(building.getNumBasements());
		setTotalArea(building.getTotalArea());
		setSprinklered(building.isSprinklered());
		setPhotoYear("2017");
		setCostEstimatorYear("2017");
		selectBuildingFullyEnclosed(building.isFullyEnclosed());
		setSqFtPercentOccupied(building.getPercentOccupied());
		setPercentRentedToOthers(building.getPercentRentedToOthers());
	}

	/**
	 * @param building
	 * @author jlarsen
	 * @date 5/2/2017
	 * @description Set Roofing values on Property Details Page
	 */
	public void fillOutRoofing(CPPCommercialProperty_Building building) {
		selectRoofingType(building.getRoofTypeCPP());
		setFlatRoof(building.isFlatRoof());
		selectRoofCondition(building.getRoofCondition());
		setyearRoofReplaced(building.getYearRoofReplaced());
	}

	/**
	 * @param building
	 * @author jlarsen
	 * @date 5/2/2017
	 * @description Set Wireing values Property Details Page
	 */
	public void fillOutWireing(CPPCommercialProperty_Building building) {
		buildings.setWiringType(building.getWiringType());
		buildings.setBoxType(building.getBoxType());
		setYearLastWireUpdate(building.getYearLastMajorWiringUpdate());
		buildings.setWiringUpdateDescription(building.getWiringUpdateDesc());
	}

	/**
	 * @param building
	 * @author jlarsen
	 * @date 5/2/2017
	 * @description Set Heating Info Property Details Page
	 */
	public void fillOutHeating(CPPCommercialProperty_Building building) {
		setYearLastHeatingUpdate(building.getYearLastMajorHeatingUpdate());
		buildings.setHeatingUpdateDescription(building.getHeatingUpdateDesc());
		selectPrimaryHeatSource(building.getHeatingSource());
	}

	/**
	 * @param building
	 * @author jlarsen
	 * @date 5/2/2017
	 * @description Set Plumbing values Property Details Page
	 */
	public void fillOutPlumbing(CPPCommercialProperty_Building building) {
		setYearLastPlumbingUpdate(building.getYearLastMajorPlumbingUpdate());
		buildings.setPlumbingUpdateDescription(building.getPlumbingUpdateDesc());
	}

	private Guidewire8Select burglaryAndRobbery_AlarmType() {
		return new Guidewire8Select(driver, "//table[contains(@id, 'CPBuildingDetail_CP1211SafeguardsInputSet:AlarmType-triggerWrap')]");
	}

	private void fillOutBurglaryAndRobbery(FireBurglaryAlarmType alarmType) {
		Guidewire8Select mySelect = burglaryAndRobbery_AlarmType();
		mySelect.selectByVisibleText(alarmType.getValue());
		
	}

	//	private Guidewire8Select select_RateType() {
	//		return new Guidewire8Select(driver, "//table[contains(@id, 'CPBuildingDetail_PropInfoInputSet:RateType-triggerWrap')]");
	//	}
	public void setBuildingRateType(RateType rateType) {


	}


	public void fillOutPropertyDetailsQQ(CPPCommercialProperty_Building building) {
		//QQ
		commercialPropertyProperty.clickDetailsTab();
		

		building.setNumber(Integer.valueOf(find(By.xpath("//div[contains(@id, ':BuildingNumber-inputEl')]")).getText()));

		for (PropertyCoverages coverage : building.getCoverages().getBuildingCoverageList()) {
			switch (coverage) {
			case BuildersRiskCoverageForm_CP_00_20:
				fillOutPropertyDetails_BuildersRiskCoverageForm_CP_00_20QQ(building);
				break;
			case BuildingCoverage:
				fillOutPropertyDetials_BuildingCoverageQQ(building);
				break;
			case BusinessPersonalPropertyCoverage:
				fillOutPropertyDetails_BusinessPersonalPropertyQQ(building);
				break;
			case LegalLiabilityCoverageForm_CP_00_40:
				break;
			case PersonalPropertyOfOthers:
				break;
			case PropertyInTheOpen:
				fillOutPropertyDetails_PropertyInTheOpenQQ(building);
				break;
			case CondominiumAssociationCoverageForm_CP_00_17:
				break;
			case CondominiumCommercialUnit_OwnersCoverageForm_CP_00_18:
				break;
			}
		}
	}

	public void fillOutPropertyDetailsFA(CPPCommercialProperty_Building building) {

		commercialPropertyProperty.clickDetailsTab();
		
		for (PropertyCoverages coverage : building.getCoverages().getBuildingCoverageList()) {
			switch (coverage) {
			case BuildersRiskCoverageForm_CP_00_20:
				fillOutPropertyDetails_BuildersRiskCoverageForm_CP_00_20FA(building);
				break;
			case BuildingCoverage:
				fillOutPropertyDetials_BuildingCoverageFA(building);
				fillOutBurglaryAndRobbery(building.getBurglaryAndRobberyProtectiveSafeguards_CP_12_11());
				break;
			case BusinessPersonalPropertyCoverage:
				fillOutPropertyDetails_BusinessPersonalPropertyFA(building);
				if (building.getCondominium().equals(Condominium.UnitOwner)) {
					fillOutBurglaryAndRobbery(building.getBurglaryAndRobberyProtectiveSafeguards_CP_12_11());
				}
				break;
			case LegalLiabilityCoverageForm_CP_00_40:
				break;
			case PersonalPropertyOfOthers:
				break;
			case PropertyInTheOpen:
				fillOutPropertyDetails_PropertyInTheOpenFA(building);
				break;
			case CondominiumAssociationCoverageForm_CP_00_17:
				break;
			case CondominiumCommercialUnit_OwnersCoverageForm_CP_00_18:
				break;
			}
		}


	}

	public void fillOutPropertyDetials_BuildingCoverageQQ(CPPCommercialProperty_Building building) {
		//multiple occupancy
		if (selectMultipleOccupancy(building.isMultipleOccupancy())) {
			
			selectMultipleOccupancyClassCode(building.getMultipleOccupancyClassCode());
		}
		if (building.isSpecialClassCode()) {
			selectFirstBuildingCodeResultClassCode(building.getClassCode(), building.getClassCodeDescription());
		} else {
			selectFirstBuildingCodeResultClassCode(building.getClassCode());
		}

		
		selectCondominium(building.getCondominium());
		selectManualProtectionClass(building);
		setYearBuilt(building.getYearBuilt());
		selectConstructionType(building.getConstructionTypeCPP());
		setSprinklered(building.isSprinklered());
		selectBuildingFullyEnclosed(building.isFullyEnclosed());
		buildings.setWiringType(building.getWiringType());
		buildings.setBoxType(building.getBoxType());
		selectPrimaryHeatSource(building.getHeatingSource());

	}

	public void fillOutPropertyDetails_BusinessPersonalPropertyQQ(CPPCommercialProperty_Building building) {
		if (building.isSpecialClassCode()) {
			selectFirstBuildingCodeResultClassCode(building.getClassCode(), building.getClassCodeDescription());
		} else {
			selectFirstBuildingCodeResultClassCode(building.getClassCode());
		}
		
		selectCondominium(building.getCondominium());
		selectManualProtectionClass(building);
		setSprinklered(building.isSprinklered());
		selectConstructionType(building.getConstructionTypeCPP());
		selectBuildingFullyEnclosed(building.isFullyEnclosed());

		if (building.getCondominium().equals(Condominium.UnitOwner)) {
			setYearBuilt(building.getYearBuilt());
			buildings.setWiringType(building.getWiringType());
			buildings.setBoxType(building.getBoxType());
			selectPrimaryHeatSource(building.getHeatingSource());
		}
	}

	public void fillOutPropertyDetails_PropertyInTheOpenQQ(CPPCommercialProperty_Building building) {
		if (building.isSpecialClassCode()) {
			selectFirstBuildingCodeResultClassCode(building.getClassCode(), building.getClassCodeDescription());
		} else {
			selectFirstBuildingCodeResultClassCode(building.getClassCode());
		}
		
		selectManualProtectionClass(building);
	}

	public void fillOutPropertyDetials_BuildingCoverageFA(CPPCommercialProperty_Building building) {
		setUsageDescription(building.getUsageDescription());
		setNumOfStories(building.getNumStories());
		setNumOfBasements(building.getNumBasements());
		clickProductLogo();
		
		setTotalArea(building.getTotalArea());
		setSqFtPercentOccupied(building.getPercentOccupied());
		setPercentRentedToOthers(building.getPercentRentedToOthers());
		selectRoofingType(building.getRoofTypeCPP());
		setFlatRoof(building.isFlatRoof());
		selectRoofCondition(building.getRoofCondition());
		setyearRoofReplaced(building.getYearRoofReplaced());
		setYearLastWireUpdate(building.getYearLastMajorWiringUpdate());
		buildings.setWiringUpdateDescription(building.getWiringUpdateDesc());
		setYearLastHeatingUpdate(building.getYearLastMajorHeatingUpdate());
		buildings.setHeatingUpdateDescription(building.getHeatingUpdateDesc());
		setYearLastPlumbingUpdate(building.getYearLastMajorPlumbingUpdate());
		buildings.setPlumbingUpdateDescription(building.getPlumbingUpdateDesc());
	}


	public void fillOutPropertyDetails_BusinessPersonalPropertyFA(CPPCommercialProperty_Building building) {
		//USEAGE DESC
		setUsageDescription(building.getUsageDescription());
		//CLASS CODE QQ
		//CONDOMINIUM QQ
		//MANUAL PROT CLASS QQ
		//CONSTRUCTION TYPE QQ
		//SPRINKLERED QQ
		//FULL ENCLOSED QQ

	}

	public void fillOutPropertyDetails_BuildersRiskCoverageForm_CP_00_20FA(CPPCommercialProperty_Building building) {
		//USEAGE DESC
		setUsageDescription(building.getUsageDescription());
		//MANUAL PROT CLASS QQ
		//CONSTUSTION TYPE QQ
		//# STORIES
		setNumOfStories(building.getNumStories());
		//TOTAL AREA
		setTotalArea(building.getTotalArea());

	}

	public void fillOutPropertyDetails_PropertyInTheOpenFA(CPPCommercialProperty_Building building) {
		//USEAGE DESC
		setUsageDescription(building.getUsageDescription());
		//CLASS CODE QQ
		//CONDOMINIUM QQ
		//PROTECTION CLASS QQ
	}


	public void selectCondominium(Condominium condominium) {
		Guidewire8Select mySelect = new Guidewire8Select(driver, "//table[contains(@id, ':CoverageForm-triggerWrap')]");
		mySelect.selectByVisibleText(condominium.getValue());
	}


	public void selectRateType(RateType rateType) {
		Guidewire8Select mySelect = select_RateType();
		mySelect.selectByVisibleText(rateType.getValue());
	}


	private void selectManualProtectionClass(CPPCommercialProperty_Building building) {
		GenericWorkorderBuildings buildings = new GenericWorkorderBuildings(driver);
		try {
			if (building.getPropertyLocation().getAddress().getDbAddress() != null) {
				//jlarsen 1/8/2017
				//THIS SECTION WILL UPDATE THE LAT, LONG, AND PROTECTION CLASS CODE IN THE DATABASE OF THE PULLED ADDRESS.
				BigDecimal latitude = new BigDecimalConverter("Latitude").convert(find(By.xpath("//div[contains(@id, ':LatitudeIDAuto-inputEl')]")).getText());
				BigDecimal longitiude = new BigDecimalConverter("longitude").convert(find(By.xpath("//div[contains(@id, ':LongitudeIDAuto-inputEl')]")).getText());

				building.getPropertyLocation().getAddress().getDbAddress().setProtectionClass(getAutoProtectionClass().getValue());
				building.getPropertyLocation().getAddress().getDbAddress().setLatitudeFbm(latitude);
				building.getPropertyLocation().getAddress().getDbAddress().setLongitudeFbm(longitiude);

				AddressTemp2.updateProtectionClass(building.getPropertyLocation().getAddress().getDbAddress());
			}
		} catch (Exception e) {
		}
		building.setAutoProtecitonClass(getAutoProtectionClass());

		if (building.isOverrideProtectionClass()) {
			buildings.setManualProtectionClassCode(Integer.valueOf(building.getProtectionClassCode().getValue()));
		} else {
			buildings.setManualProtectionClassCode(Integer.valueOf(building.getAutoProtecitonClass().getValue()));
		}
	}


	private void setYearBuilt(int year) {
		GenericWorkorderBuildings buildings = new GenericWorkorderBuildings(driver);
		buildings.setYearBuilt(year);
	}


	private void setSprinklered(boolean yesno) {
		GenericWorkorderBuildings buildings = new GenericWorkorderBuildings(driver);
		buildings.setSprinklered(yesno);
	}


	private void selectBuildingFullyEnclosed(boolean yesno) {
		Guidewire8RadioButton myRadio = new Guidewire8RadioButton(driver, "//label[contains(text(), 'Is building fully enclosed?')]/parent::td/parent::tr/parent::tbody/parent::table");
		myRadio.select(yesno);
	}


//    private void selectWireingtype(WiringType wireingType) {
//        GenericWorkorderBuildings buildings = new GenericWorkorderBuildings(driver);
//        buildings.setWiringType(wireingType);
//    }
//
//
//    private void selectBoxType(BoxType boxType) {
//        GenericWorkorderBuildings buildings = new GenericWorkorderBuildings(driver);
//        buildings.setBoxType(boxType);
//    }


	private void selectPrimaryHeatSource(PrimaryHeating heatSource) {
		Guidewire8Select mySelect = new Guidewire8Select(driver, "//table[contains(@id, ':HeatingType-triggerWrap')]");
		mySelect.selectByVisibleText(heatSource.getValue());
	}


	public void selectFirstBuildingCodeResultClassCode(String classcode) {
		buildings.selectFirstBuildingCodeResultClassCode(classcode);
	}

	public void selectFirstBuildingCodeResultClassCode(String buildingCode, String buildingClassification) {
		buildings.selectFirstBuildingClassCode(buildingCode, buildingClassification);
	}


	@FindBy(xpath = "//div[contains(@id, 'ISRBFireProtectionClassAuto-inputEl')]")
	public WebElement text_AutoProtectionClass;

	public ProtectionClassCode getAutoProtectionClass() {
		if (finds(By.xpath("//div[contains(@id, 'ISRBFireProtectionClassAuto-inputEl')]")).isEmpty()) {
			return ProtectionClassCode.Prot3;
		} else {
			ProtectionClassCode returnType = ProtectionClassCode.valueOfName(text_AutoProtectionClass.getText());
			return returnType;
		}
	}


	@FindBy(xpath = "//input[contains (@id, ':Description-inputEl')]")
	private WebElement editbox_UsageDescription;

	private void setUsageDescription(String text) {
		setText(editbox_UsageDescription, text);
	}

	Guidewire8RadioButton checkbox_MultipleOccupancy() {
		return new Guidewire8RadioButton(driver, "//table[contains(@id, ':MultipleOccupancy')]");
	}

	private boolean selectMultipleOccupancy(boolean yesno) {
		checkbox_MultipleOccupancy().select(yesno);
		return yesno;
	}

	Guidewire8Select select_MultipleOccupancyClassCode() {
		return new Guidewire8Select(driver, "//label[text()='Class Code']/parent::td/following-sibling::td/table");
	}

	private void selectMultipleOccupancyClassCode(MultipleOccupancyClassCode classCode) {
		Guidewire8Select mySelect = select_MultipleOccupancyClassCode();
		mySelect.selectByVisibleText(classCode.getValue());
	}

	@FindBy(xpath = "//div[contains(@id, 'ClassCode:SelectClassCode')]")
	private WebElement link_PropertyClassCodeSearch;


	@FindBy(xpath = "//textArea[contains(@id, ':ClassDescription-inputEl')]")
	private WebElement textbox_PropertyClassDescription;

	public String getPropertyClassDescription() {
		return textbox_PropertyClassDescription.getText();
	}

	Guidewire8Select select_CondominiumType() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':CPBuildingDetail_PropInfoInputSet:CoverageForm-triggerWrap')]");
	}

	public void selectCondominiumType() {
		Guidewire8Select mySelect = select_CondominiumType();
		mySelect.selectByVisibleText("//table[contains(@id, ':CPBuildingDetail_PropInfoInputSet:CoverageForm-triggerWrap')]");
	}

	@FindBy(xpath = "//div[contains(@id, ':CPBuildingDetail_PropInfoInputSet:RateType-inputEl')]")
	private WebElement textbox_RateType;

	public String getRateType() {
		return textbox_RateType.getText();
	}

	private Guidewire8Select select_RateType() {
		return new Guidewire8Select(driver, "//table[contains(@id, 'RateType-triggerWrap')]");
	}


	private Guidewire8Select select_ConstructionType() {
		return new Guidewire8Select(driver, "//table[contains(@id, 'ConstructionType-triggerWrap')]");
	}


	private void selectConstructionType(ConstructionTypeCPP constructionType) {
		
		select_ConstructionType().selectByVisibleText(constructionType.getValue());
		
		if (constructionType.equals(ConstructionTypeCPP.NonCombustible)) {

		}
		
	}


	@FindBy(xpath = "//input[contains(@id, 'NumStories-inputEl')]")
	private WebElement editbox_NumberOfStories;

	private void setNumOfStories(int count) {
		
		scrollToElement(editbox_NumberOfStories);
		
		editbox_NumberOfStories.click();
		
		editbox_NumberOfStories.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		
		editbox_NumberOfStories.sendKeys(String.valueOf(count));
		
	}

	@FindBy(xpath = "//input[contains(@id, 'NumBasements-inputEl')]")
	private WebElement editbox_NumberOfBasements;

	private void setNumOfBasements(int count) {
		clickWhenClickable(editbox_NumberOfBasements);
		editbox_NumberOfBasements.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editbox_NumberOfBasements.sendKeys(String.valueOf(count));
		

		if (!editbox_NumberOfBasements.getAttribute("value").equals(String.valueOf(count))) {
			clickWhenClickable(editbox_NumberOfBasements);
			editbox_NumberOfBasements.sendKeys(Keys.chord(Keys.CONTROL + "a"));
			editbox_NumberOfBasements.sendKeys(String.valueOf(count));
			
		}
	}

	@FindBy(xpath = "//input[contains(@id, 'TotalArea-inputEl')]")
	private WebElement editbox_TotalArea_NotBasement;

	private void setTotalArea(String area) {
		waitUntilElementIsClickable(editbox_TotalArea_NotBasement);
		editbox_TotalArea_NotBasement.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editbox_TotalArea_NotBasement.sendKeys(area);
	}


	@FindBy(xpath = "//input[contains(@id, ':CPBuildingDetail_ConstructionInputSet:PhotoYear-inputEl')]")
	private WebElement editbox_PhotoYear;

	private void setPhotoYear(String photoYear) {
		waitUntilElementIsClickable(editbox_PhotoYear);
		editbox_PhotoYear.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editbox_PhotoYear.sendKeys(photoYear);
	}

	@FindBy(xpath = "//input[contains(@id, ':CPBuildingDetail_ConstructionInputSet:PhotoYear-inputEl')]")
	private WebElement editbox_CostEstimatorYear;

	private void setCostEstimatorYear(String costEstimatorYear) {
		waitUntilElementIsClickable(editbox_CostEstimatorYear);
		editbox_CostEstimatorYear.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editbox_CostEstimatorYear.sendKeys(costEstimatorYear);
	}

	private Guidewire8Select select_PercentOccupied() {
		return new Guidewire8Select(driver, "//table[contains (@id, ':SqFtOccupied-triggerWrap')]");
	}

	private void setSqFtPercentOccupied(SqFtPercOccupied sqFtPerOccupied) {
		Guidewire8Select mySelect = select_PercentOccupied();
		mySelect.selectByVisibleText(sqFtPerOccupied.getValue());
		
		if (sqFtPerOccupied.equals(SqFtPercOccupied.SeventyFiveOneHundredPerc)) {
			
		}

	}

	@FindBy(xpath = "//input[contains(@id, ':CPBuildingDetail_ConstructionInputSet:SqFtRented-inputEl')]")
	private WebElement editbox_AreaLeased;

	private void setPercentRentedToOthers(String areaLeasedToOthers) {
		waitUntilElementIsClickable(editbox_AreaLeased);
		editbox_AreaLeased.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editbox_AreaLeased.sendKeys(areaLeasedToOthers);
	}

	private Guidewire8Select select_RoofType() {
		return new Guidewire8Select(driver, "//table[contains( @id, ':CPBuildingDetail_ConstructionInputSet:RoofingType-triggerWrap')]");
	}

	private void selectRoofingType(RoofingTypeCPP roofingType) {
		
		select_RoofType().selectByVisibleText(roofingType.getValue());
		
	}

	private Guidewire8RadioButton radio_FlatRoof() {
		return new Guidewire8RadioButton(driver, "//table[contains( @id, ':CPBuildingDetail_ConstructionInputSet:FlatRoof')]");
	}

	private void setFlatRoof(boolean radioValue) {
		
		radio_FlatRoof().select(radioValue);
		
	}

	private Guidewire8Select select_RoofCondition() {
		return new Guidewire8Select(driver, "//table[contains( @id, ':CPBuildingDetail_ConstructionInputSet:RoofCondition-triggerWrap')]");
	}

	private void selectRoofCondition(RoofCondition roofCondition) {
		
		select_RoofCondition().selectByVisibleText(roofCondition.getValue());
		
	}

	@FindBy(xpath = "//input[contains(@id, ':CPBuildingDetail_ConstructionInputSet:RoofReplacedYear-inputEl')]")
	private WebElement LastUpdateRoofing;

	private void setyearRoofReplaced(int year) {
		waitUntilElementIsClickable(LastUpdateRoofing);
		LastUpdateRoofing.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		
		LastUpdateRoofing.sendKeys("" + year);
		LastUpdateRoofing.sendKeys(Keys.TAB);
		
	}


	@FindBy(xpath = "//input[contains(@id, 'LastUpdateHeating-inputEl') or contains(@id, ':HeatingUpdateYear-inputEl')]")
	private WebElement editbox_LastUpdateHeating;

	private void setYearLastHeatingUpdate(int year) {
		setText(editbox_LastUpdateHeating, String.valueOf(year));
	}

	@FindBy(xpath = "//input[contains(@id, 'LastUpdatePlumbing-inputEl') or contains(@id, ':PlumbingUpdateYear-inputEl')]")
	private WebElement editbox_LastUpdatePlumbing;

	private void setYearLastPlumbingUpdate(int year) {
		setText(editbox_LastUpdatePlumbing, String.valueOf(year));
	}


	@FindBy(xpath = "//input[contains(@id, 'LastUpdateWiring-inputEl') or contains(@id, ':WiringUpdateYear-inputEl')]")
	private WebElement editbox_LastUpdateWireing;

	private void setYearLastWireUpdate(int year) {
		setText(editbox_LastUpdateWireing, String.valueOf(year));
	}

	Guidewire8Select select_RentedToOthers() {
		return new Guidewire8Select(driver, "//table[contains(@id, 'RentedOthers-inputEl')]");
	}

	private void fillOutPropertyDetails_BuildersRiskCoverageForm_CP_00_20QQ(CPPCommercialProperty_Building building) {
		//MANUAL PROTECTION CLASS
		selectManualProtectionClass(building);
		//CONTRUCTION TYPE
		selectConstructionType(building.getConstructionTypeCPP());
	}


}

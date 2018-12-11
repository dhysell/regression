package repository.pc.workorders.generic;

import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.Measurement;
import repository.gw.enums.Property.ElectricalSystem;
import repository.gw.enums.Property.FoundationType;
import repository.gw.enums.Property.Garage;
import repository.gw.enums.Property.KitchenBathClass;
import repository.gw.enums.Property.NumberOfStories;
import repository.gw.enums.Property.Plumbing;
import repository.gw.enums.Property.PrimaryHeating;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.RoofType;
import repository.gw.enums.Property.Wiring;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.helpers.DateUtils;
import gwclockhelpers.ApplicationOrCenter;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Date;
import java.util.List;

public class GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction extends GenericWorkorderSquirePropertyAndLiabilityPropertyDetail {

    private WebDriver driver;

    public GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }
    
    public void fillOutPropertyConstruction_QQ(PLPolicyLocationProperty property) throws GuidewireNavigationException {
        waitForPostBack();
        if (!property.getpropertyType().equals(PropertyTypePL.TrellisedHops) && !property.getpropertyType().equals(PropertyTypePL.SolarPanels)) {
            if (!property.getpropertyType().equals(PropertyTypePL.Potatoes) && !property.getpropertyType().equals(PropertyTypePL.HayStrawInOpen) &&
                    !property.getpropertyType().equals(PropertyTypePL.HayStrawInStorage) &&
                    !property.getpropertyType().equals(PropertyTypePL.GrainSeed)) {
                clickPropertyConstructionTab();
                setConstructionType(property.getConstructionType());
                if(property.isSetMSPhotoYears() &&  !property.getpropertyType().equals(PropertyTypePL.Contents) ) {
                	String years = DateUtils.dateFormatAsString("MM/yyyy", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
                	if(property.getPhotoYear2() != null) {
                		setPhotoYear(DateUtils.dateFormatAsString("MM/yyyy", property.getPhotoYear2()));
                	} else {
                		setPhotoYear(years);
                	}
                	waitForPostBack();
                	 
                	if(!property.getpropertyType().equals(PropertyTypePL.DwellingUnderConstructionCovE) && 
                			!property.getpropertyType().equals(PropertyTypePL.VacationHome) && 
                			!property.getpropertyType().equals(PropertyTypePL.Garage)&& 
                			!property.getpropertyType().equals(PropertyTypePL.Trellis) && 
                			!property.getpropertyType().equals(PropertyTypePL.CommodityShed) &&
                			!property.getpropertyType().equals(PropertyTypePL.DwellingPremisesCovE) &&
                			!property.getpropertyType().equals(PropertyTypePL.ResidencePremisesCovE) &&
                			!property.getpropertyType().equals(PropertyTypePL.VacationHomeCovE) &&
                			!property.getpropertyType().equals(PropertyTypePL.AlfalfaMill) &&
                            !property.getpropertyType().equals(PropertyTypePL.DeckPatio)&&
                            !property.getpropertyType().equals(PropertyTypePL.BunkHouse) &&
                            !property.getpropertyType().equals(PropertyTypePL.CondoVacationHomeCovE) &&
                            !property.getpropertyType().equals(PropertyTypePL.FarmOffice) &&
                            !property.getpropertyType().equals(PropertyTypePL.Windmill)) {
                		if(property.getMsYear() != null) {
                			setMSYear(DateUtils.dateFormatAsString("MM/yyyy", property.getMsYear()));
                		} else {
                			setMSYear(years);
                		}
                	}
                }

                if (!property.getpropertyType().equals(PropertyTypePL.Contents)) {
                    setYearBuilt(property.getYearBuilt());
                    setSquareFootage(property.getSquareFootage());
                }

                if (!property.getpropertyType().equals(PropertyTypePL.ResidencePremises) &&
                        !property.getpropertyType().equals(PropertyTypePL.DwellingPremises) &&
                        !property.getpropertyType().equals(PropertyTypePL.VacationHome) &&
                        !property.getpropertyType().equals(PropertyTypePL.CondominiumDwellingPremises) &&
                        !property.getpropertyType().equals(PropertyTypePL.DwellingUnderConstruction) &&
                        !property.getpropertyType().equals(PropertyTypePL.CondominiumResidencePremise) &&
                        !property.getpropertyType().equals(PropertyTypePL.CondominiumVacationHome) &&
                        !property.getpropertyType().equals(PropertyTypePL.DetachedGarage) &&
                        !property.getpropertyType().equals(PropertyTypePL.Contents)) {

                    setMeasurement(property.getMeasurement());
                }
                if (!property.getpropertyType().equals(PropertyTypePL.DetachedGarage) && !property.getpropertyType().equals(PropertyTypePL.Trellis)) {
                    //setFoundationType(property.getFoundationType());
                }
                if (!property.getpropertyType().equals(PropertyTypePL.Contents) && property.getConstructionType().equals(ConstructionTypePL.ModularManufactured) || (property.getConstructionType().equals(ConstructionTypePL.MobileHome) && property.getFoundationType().equals(FoundationType.None))) {
                    setSerialNumber(property.getSerialNumber());
                    setMake(property.getMake());
                    setModel(property.getModel());
                }
                if (property.getpropertyType().equals(PropertyTypePL.DetachedGarage) || property.getpropertyType().equals(PropertyTypePL.FarmOffice) || property.getpropertyType().equals(PropertyTypePL.BunkHouse) || property.getpropertyType().equals(PropertyTypePL.Barn) || property.getpropertyType().equals(PropertyTypePL.Shed) || property.getpropertyType().equals(PropertyTypePL.AlfalfaMill) || property.getpropertyType().equals(PropertyTypePL.DairyComplex)) {
                    //setRoofType(property.getRoofType());
                } else {
                    //setGarage(property.getGarage());
                }
            } else {
                selectStorageType(property.getStorageType());
                if (property.getStorageType().equals("Other")) {
                    setOtherStorageDesciption(property.getStorageTypeOtherDescription());
                }//end if
            }//end else
        }//end if
    }

    public void fillOutPropertyConstrustion_FA(PLPolicyLocationProperty property) throws GuidewireNavigationException {
        clickPropertyConstructionTab();
        if (!property.getpropertyType().equals(PropertyTypePL.TrellisedHops) && !property.getpropertyType().equals(PropertyTypePL.SolarPanels)) {
            if (!property.getpropertyType().equals(PropertyTypePL.GrainSeed) && (!property.getpropertyType().equals(PropertyTypePL.HayStrawInOpen)) && (!property.getpropertyType().equals(PropertyTypePL.HayStrawInStorage)) && (!property.getpropertyType().equals(PropertyTypePL.HayStrawWithClearSpace)) && (!property.getpropertyType().equals(PropertyTypePL.Potatoes))) {
//				setYearBuilt(property.getYearBuilt());
//				setConstructionType(property.getConstructionType());
//				setSquareFootage(property.getSquareFootage());
                switch (property.getpropertyType()) {
                    case CondominiumDwellingPremisesCovE:
                    case CondoVacationHomeCovE:
                    case DwellingPremisesCovE:
                    case DwellingUnderConstructionCovE:
                    case ResidencePremisesCovE:
                    case VacationHomeCovE:
                    case DairyComplex:
                    case Arena:
                    case AlfalfaMill:
                    case Barn:
                    case BunkHouse:
                    case FarmOffice:
                    case Garage:
                    case Hangar:
                    case Hatchery:
                        setMeasurement(property.getMeasurement());
                        break;
                    default:
                        break;
                }//end switch

                if (!property.getpropertyType().equals(PropertyTypePL.BeeStation) && !property.getpropertyType().equals(PropertyTypePL.Contents)) {
                    setFoundationType(property.getFoundationType());
                    setRoofType(property.getRoofType());
                }//end if

                if (property.getConstructionType().equals(ConstructionTypePL.MobileHome) && property.getFoundationType().equals(FoundationType.NoFoundation)) {
                    setMake(property.getMake());

                }
                if (!property.getpropertyType().equals(PropertyTypePL.FarmOffice) &&
                        !property.getpropertyType().equals(PropertyTypePL.Barn) &&
                        !property.getpropertyType().equals(PropertyTypePL.BeeStation) &&
                        !property.getpropertyType().equals(PropertyTypePL.BunkHouse) &&
                        !property.getpropertyType().equals(PropertyTypePL.AlfalfaMill) &&
                        !property.getpropertyType().equals(PropertyTypePL.Arena) &&
                        !property.getpropertyType().equals(PropertyTypePL.DairyComplex) &&
                        !property.getpropertyType().equals(PropertyTypePL.Garage) &&
                        !property.getpropertyType().equals(PropertyTypePL.Shed) &&
                        !property.getpropertyType().equals(PropertyTypePL.Hangar) &&
                        !property.getpropertyType().equals(PropertyTypePL.Hatchery) &&
                        !property.getpropertyType().equals(PropertyTypePL.Contents)) {
                    setBasementFinished(String.valueOf(property.getBasementFinishedPercent()));
                    setStories(property.getstoriesPL());
                    setGarage(property.getGarage());
                    setCoveredPorches(property.getCoveredPorches());
//					setPrimaryHeating(property.getPrimaryHeating());
//					setPlumbing(property.getPlumbing());
//					setWiring(property.getWiring());
//					setElectricalSystem(property.getElectricalSystem());
//					setAmps(property.getAmps());
                    setKitchenClass(property.getKitchenClass());
                    setBathClass(property.getBathClass());
                    setLargeShed(property.getLargeShed());
                }
            } else {
                selectStorageType(property.getStorageType());
                if (property.getStorageType().equals("Other")) {
                    setOtherStorageDesciption(property.getStorageTypeOtherDescription());
                }//end if
            }//end else
        }//end if
    }

    public void fillOutPropertyConstruction(PLPolicyLocationProperty property) throws GuidewireNavigationException {
        fillOutPropertyConstruction_QQ(property);
        fillOutPropertyConstrustion_FA(property);
    }


    private void setOtherStorageDesciption(String description) {
        setText(editbox_StorageOtherDescription, description);
    }

    @FindBy(xpath = "//input[contains(@id, ':CostEstimatorYear-inputEl')]")
    private WebElement editbox_MSYear;
    private void setMSYear(String year) {
    	setText(editbox_MSYear, year);
    }
    
    @FindBy(xpath = "//input[contains(@id, ':OtherStorageTypeInfo-inputEl')]")
    private WebElement editbox_StorageOtherDescription;

    private void selectStorageType(String type) {
        select_StorageType().selectByVisibleTextPartial(type);
    }

    private Guidewire8Select select_StorageType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':StorageType-triggerWrap')]");
    }

    public void setBathClass(KitchenBathClass classType) {
        select_SquirePropertyBath().selectByVisibleText(classType.getValue());
    }

    private Guidewire8Select select_SquirePropertyBath() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:BathClass-triggerWrap')]");
    }

    public void setKitchenClass(KitchenBathClass classType) {
        select_SquirePropertyKitchen().selectByVisibleText(classType.getValue());
    }

    private Guidewire8Select select_SquirePropertyKitchen() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:KitchenClass-triggerWrap')]");
    }

    public void setAmps(int amps) {
        editbox_SquirePropertyConstructionAmps.sendKeys(Integer.toString(amps));
    }

    @FindBy(xpath = "//input[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:NumberofAmps-inputEl')]")
    private WebElement editbox_SquirePropertyConstructionAmps;

    public void setElectricalSystem(ElectricalSystem type) {
        select_SquirePropertyElectrical().selectByVisibleText(type.getValue());
    }

    private Guidewire8Select select_SquirePropertyElectrical() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:ElectricalSystem-triggerWrap')]");
    }

    public void setWiring(Wiring type) {
        select_SquirePropertyWiring().selectByVisibleText(type.getValue());
    }

    private Guidewire8Select select_SquirePropertyWiring() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:Wiring-triggerWrap')]");
    }

    public void setPlumbing(Plumbing type) {
        Guidewire8Select mySelect = select_SquirePropertyPlumbing();
        mySelect.selectByVisibleText(type.getValue());
    }

    private Guidewire8Select select_SquirePropertyPlumbing() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:PlumbingSystem-triggerWrap')]");
    }

    public void setPrimaryHeating(PrimaryHeating type) {
        select_SquirePropertyPrimaryHeating().selectByVisibleText(type.getValue());
    }

    private Guidewire8Select select_SquirePropertyPrimaryHeating() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:PrimaryHeating-triggerWrap')]");
    }

    public void setCoveredPorches(boolean trueFalse) {
        waitForPostBack();
        radio_SquirePropertyCoveredPorchesBreezeways().select(trueFalse);
    }

    private Guidewire8RadioButton radio_SquirePropertyCoveredPorchesBreezeways() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:CoveredPorches-containerEl')]/table");
    }

    public void setGarage(Garage type) {
        clickProductLogo();
        select_SquirePropertyGarage().selectByVisibleText(type.getValue());
    }

    private Guidewire8Select select_SquirePropertyGarage() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:Garage-triggerWrap')]");
    }

    public void setStories(NumberOfStories stories) {
        select_SquirePropertyStories().selectByVisibleText(stories.getValue());
    }

    private Guidewire8Select select_SquirePropertyStories() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:NumStories-triggerWrap')]");
    }

    public void setBasementFinished(String percent) {
        clickWhenClickable(editbox_SquirePropertyConstructionBasementPercentFinished);
        editbox_SquirePropertyConstructionBasementPercentFinished.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_SquirePropertyConstructionBasementPercentFinished.sendKeys(percent);
        clickProductLogo();
    }

    @FindBy(xpath = "//input[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:BasementPercentComplete-inputEl')]")
    private WebElement editbox_SquirePropertyConstructionBasementPercentFinished;

    public void setRoofType(RoofType type) {
        select_SquirePropertyRoofType().selectByVisibleText(type.getValue());
    }

    private Guidewire8Select select_SquirePropertyRoofType() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:RoofType-triggerWrap')]");
    }

    public void setFoundationType(FoundationType type) {
        select_SquirePropertyFoundation().selectByVisibleText(type.getValue());
    }

    private Guidewire8Select select_SquirePropertyFoundation() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:FoundationType-triggerWrap')]");
    }

    public void setMeasurement(Measurement measurement) {
        select_SquirePropertySizeMeasurement().selectByVisibleText(measurement.getValue());
    }

    private Guidewire8Select select_SquirePropertySizeMeasurement() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:SizeUnits-triggerWrap')]");
    }

    public void setSquareFootage(int squareFootage) {
    	setText(editbox_SquirePropertyConstructionSquareFootage, String.valueOf(squareFootage));
    }

    @FindBy(xpath = "//input[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:ApproxSqFoot-inputEl')]")
    private WebElement editbox_SquirePropertyConstructionSquareFootage;

    public void setConstructionType(ConstructionTypePL type) {
        select_SquirePropertyConstructionType().selectByVisibleText(type.getValue());
    }

    private Guidewire8Select select_SquirePropertyConstructionType() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:ConstructionType-triggerWrap')]");
    }

    public void setYearBuilt(int year) {
    	setText(editbox_SquirePropertyConstructionYearBuilt, Integer.toString(year));
    }

    @FindBy(css = "input[id*='HODwellingMSPhotoYearsInputSet:PhotoYear']")
    private WebElement editbox_SquirePropertyConstructionPhotoYear;
    public void setPhotoYear(String yearAndMonth) {
    	setText(editbox_SquirePropertyConstructionPhotoYear, yearAndMonth);
    }





    @FindBy(xpath = "//input[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:YearBuilt-inputEl')]")
    private WebElement editbox_SquirePropertyConstructionYearBuilt;
    //	public void clickPropertyConstructionTab() throws GuidewireNavigationException {
//		clickWhenClickable(link_SquirePropertyDetailPropertyConstruction);
//		isOnPage("//label[contains(@id, ':YearBuilt-labelEl')]", 10000, "UNABLE TO GET TO PROPERTY CONSTRUCTION PAGE AFTER CLICKING THE TAB");
//	}
    @FindBy(xpath = "//span[contains(@id, ':propertyConstructionTab-btnE')]")
    private WebElement link_SquirePropertyDetailPropertyConstruction;

    public void setModel(String model) {
        setText(editbox_ModularMobileModel, "Model 1");
    }

    @FindBy(xpath = "//input[@id='HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:Model-inputEl']")
    private WebElement editbox_ModularMobileModel;

    public void setMake(String make) {
        setText(editbox_ModularMobileMake, "Model 1");
    }

    @FindBy(xpath = "//input[@id='HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:SerialNumber-inputEl']")
    private WebElement editbox_ModularMobileSerialNum;

    @FindBy(xpath = "//input[@id='HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:Make-inputEl']")
    private WebElement editbox_ModularMobileMake;

    public void setSerialNumber(String serialNumber) {
        setText(editbox_ModularMobileSerialNum, DateUtils.dateFormatAsString("yyMMddHHmmss", new Date()));
    }

    public String getFoundationType() {
        Guidewire8Select mySelect = select_SquirePropertyFoundation();
        return mySelect.getText();
    }

    public void editCoverageAPropertyDetailsFA(PLPolicyLocationProperty property) throws GuidewireNavigationException {
        if (!property.getpropertyType().equals(PropertyTypePL.Potatoes) && !property.getpropertyType().equals(PropertyTypePL.HayStrawInOpen) && !property.getpropertyType().equals(PropertyTypePL.HayStrawInStorage) && !property.getpropertyType().equals(PropertyTypePL.GrainSeed) && !property.getpropertyType().equals(PropertyTypePL.FarmOffice) && !property.getpropertyType().equals(PropertyTypePL.BunkHouse) && !property.getpropertyType().equals(PropertyTypePL.TrellisedHops) && !property.getpropertyType().equals(PropertyTypePL.SolarPanels) && !property.getpropertyType().equals(PropertyTypePL.AlfalfaMill) && !property.getpropertyType().equals(PropertyTypePL.Shed)) {
            clickPropertyConstructionTab();
            setBasementFinished(String.valueOf(property.getBasementFinishedPercent()));
            setStories(property.getstoriesPL());
            setCoveredPorches(property.getCoveredPorches());
            setGarage(property.getGarage());
            setFoundationType(property.getFoundationType());
            setRoofType(property.getRoofType());
            setKitchenClass(property.getKitchenClass());
            setBathClass(property.getBathClass());
        }
    }

    public List<String> getConstructionTypeValues() {
        return select_SquirePropertyConstructionType().getList();

    }

    public boolean isFoundationTypeExist() {
        return select_SquirePropertyFoundation().checkIfElementExists(1000);
    }

    public List<String> getFoundationTypeValues() {
        return select_SquirePropertyFoundation().getList();
    }

    public void setLargeShed(boolean trueFalse) {
        radio_SquirePropertyShed().select(false);
    }


    public void setCoverageShed(boolean trueFalse) {
        radio_SquirePropertyShed().select(true);
        radio_DoYouWantCoverageShed().select(trueFalse);
        clickOK();
    }

    private Guidewire8RadioButton radio_SquirePropertyShed() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:ShedLargerThan200-containerEl')]/table");
    }

    private Guidewire8RadioButton radio_DoYouWantCoverageShed() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:CoverageForShed-containerEl')]/table");
    }

    public boolean setPolyurethane(boolean trueFalse) {
        if (checkIfElementExists("//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:PolyurethaneInsulation-containerEl')]/table", 3000)) {
            radio_SquirePropertyPolyurethane().select(trueFalse);
            return true;
        } else {
            return false;
        }
    }


    public void setPolyurethaneAndSandwichAndDescription(boolean insWithPoly, boolean sandwiched, String sandwichDesc) {
        setPolyurethane(insWithPoly);
        setPolyurethaneSandwiched(sandwiched);
        setPolyurethaneSandwichedDescription(sandwichDesc);
    }


    public void setPolyurethaneSandwiched(boolean trueFalse) {
        radio_SquirePropertyPolyurethaneSandwiched().select(trueFalse);
    }


    public void setPolyurethaneSandwichedDescription(String sandwichedDescription) {
        editbox_SquirePropertyConstructionSandwichedPolyurethaneDescription.sendKeys(sandwichedDescription);
        clickProductLogo();
    }

    @FindBy(xpath = "//textarea[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:DwellingDescription-inputEl')]")
    private WebElement editbox_SquirePropertyConstructionDescription;

    public void setPropertyDescription(String Description) {
        clickWhenClickable(editbox_SquirePropertyConstructionDescription);
        editbox_SquirePropertyConstructionDescription.sendKeys(Description);
        clickProductLogo();
    }

    private Guidewire8RadioButton radio_SquirePropertyPolyurethane() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:PolyurethaneInsulation-containerEl')]/table");
    }

    private Guidewire8RadioButton radio_SquirePropertyPolyurethaneSandwiched() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:DetachedGarageIncreaseCov-containerEl')]/table");
    }

    @FindBy(xpath = "//input[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:PolyurethaneDescription-inputEl')]")
    private WebElement editbox_SquirePropertyConstructionSandwichedPolyurethaneDescription;

    public void setRatedYear(int year) {
        clickWhenClickable(editbox_SquirePropertyConstructionRatedYear);
        editbox_SquirePropertyConstructionRatedYear.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_SquirePropertyConstructionRatedYear.sendKeys(Integer.toString(year));
        clickProductLogo();
    }

    @FindBy(xpath = "//input[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:ratedYear-inputEl')]")
    private WebElement editbox_SquirePropertyConstructionRatedYear;

    public boolean isPhotoYearExist() {
        if (checkIfElementExists(editbox_SquirePropertyConstructionPhotoYear, 1000)) {
            return true;
        } else {
            return false;
}
    }

    private Guidewire8RadioButton radio_isOpenFlameCoverageWanted() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:OpenFlame-containerEl')]/table");
    }

    public boolean isOpenFlameCoverageWantedExist() {
        try {
            return radio_isOpenFlameCoverageWanted().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void setOpenFlameCoverageWanted(boolean trueFalse) {
        radio_isOpenFlameCoverageWanted().select(trueFalse);
    }
}

package repository.gw.generate.custom;

import repository.gw.enums.Building;
import repository.gw.enums.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PLPolicyLocationProperty {

	private int number = -1;
	private int yearBuilt = 2013;
	private Property.PropertyTypePL propertyType = Property.PropertyTypePL.ResidencePremises;
	private Building.ConstructionTypePL constructionTypePL = Building.ConstructionTypePL.Frame;
	private int squareFootage = 3124;
	private Property.KitchenBathClass kitchenClass = Property.KitchenBathClass.Basic;
	private Property.NumberOfStories storiesPL = Property.NumberOfStories.One;
	private Property.Garage garage = Property.Garage.AttachedGarage;
	private boolean largeShed = false;
	private boolean coveredPorches = false;
	private double basementFinishedPercent = 90.0;
	private Property.FoundationType foundation = Property.FoundationType.Foundation;
	private Property.RoofType roofType = Property.RoofType.WoodShingles;
	private Property.PrimaryHeating heating = Property.PrimaryHeating.Gas;
	private Property.Plumbing plumbing = Property.Plumbing.Copper;
	private Property.Wiring wiring = Property.Wiring.Copper;
	private Property.ElectricalSystem electricalSystem = Property.ElectricalSystem.CircuitBreaker;
	private int amps = 100;
	private Property.KitchenBathClass bathClass = Property.KitchenBathClass.Basic;
	private boolean polyurethane;
	private boolean sandwichedPolyurethane;
	private String sandwichedPolyurethaneDescription;
	private boolean openFalme;
	private String constructionDescription;
	private String storageType = "Shop";
	private String storageTypeOtherDescription = "Card Board Boxes";
	private repository.gw.enums.Measurement measurement = repository.gw.enums.Measurement.SQFT;
//	private int photoYear = 0;
	private boolean setMSPhotoYears = true;
	private Date msYear = null;
	private Date photoYear2 = null;


	// Protection Details
	private boolean overrideProtectionclass = false;
	private Location.ProtectionClassCode autoProtectionClass = null;
	private Location.ProtectionClassCode manualPublicProtectionClassCode = Location.ProtectionClassCode.Prot3;
	private Property.SprinklerSystemType sprinkerSystem = Property.SprinklerSystemType.Full;
	private boolean fireExtinguishers = true;
	private boolean smokeAlarms = true;
	private boolean nonSmoker = true;
	private boolean deadboltLocks = true;
	private boolean defensibleSpace = true;

	// Property Details
	private Property.NumberOfUnits units = Property.NumberOfUnits.OneUnit;
	private boolean dwellingVacant = false;
	private boolean woodFireplace = false;
	private boolean chimneysCleanedRegularly = false;
	private Date lastCleaned = new Date();
	private boolean swimmingPool = false;
	private boolean poolFence = false;
	private boolean divingBoard = false;
	private boolean poolSafetyCover = false;
	private String serialNumber = "123456";
	private String make = "Make 1";
	private String model = "Make 1";

	private boolean waterLeaking = false;
	private boolean animals = false;
	public boolean owner = false;

	private String animalsDescription = "African Grey Parrot";
	// Coverage Details
	private repository.gw.generate.custom.PLPropertyCoverages propertyCoverages = new repository.gw.generate.custom.PLPropertyCoverages();

	private ArrayList<repository.gw.generate.custom.AdditionalInterest> additionalInterestList = new ArrayList<repository.gw.generate.custom.AdditionalInterest>();

	private repository.gw.generate.custom.SquirePropertySpecificExclusionsConditions exclusionsConditions = new repository.gw.generate.custom.SquirePropertySpecificExclusionsConditions();

	private repository.gw.generate.custom.AddressInfo address = null;

	private repository.gw.enums.SectionI_UnderwriterIssues uwIssue = null;
	
	// Constructors
	public PLPolicyLocationProperty() {
	}

	public PLPolicyLocationProperty(Property.PropertyTypePL propertyType, Building.ConstructionTypePL constructionTypePL, ArrayList<repository.gw.generate.custom.AdditionalInterest> additionalInterestList) {
		setpropertyType(propertyType);
		setConstructionType(constructionTypePL);
		setPolicyLocationBuildingAdditionalInterestArrayList(additionalInterestList);
	}

	public PLPolicyLocationProperty(Property.PropertyTypePL propertyType, ArrayList<repository.gw.generate.custom.AdditionalInterest> additionalInterestList) {
		setpropertyType(propertyType);
		setPolicyLocationBuildingAdditionalInterestArrayList(additionalInterestList);

	}

	public PLPolicyLocationProperty(Property.PropertyTypePL _propertyType) {
		setpropertyType(_propertyType);
	}

	public PLPolicyLocationProperty(ArrayList<repository.gw.generate.custom.AdditionalInterest> additionalInterestList) {
		setPolicyLocationBuildingAdditionalInterestArrayList(additionalInterestList);
	}

	// Getters and Setters
	public void setConstructionType(Building.ConstructionTypePL type) {
		this.constructionTypePL = type;
	}

	public Building.ConstructionTypePL getConstructionType() {
		return this.constructionTypePL;
	}

	public void setKitchenClass(Property.KitchenBathClass type) {
		this.kitchenClass = type;
	}

	public Property.KitchenBathClass getKitchenClass() {
		return this.kitchenClass;
	}

	public void setBathClass(Property.KitchenBathClass type) {
		this.bathClass = type;
	}

	public Property.KitchenBathClass getBathClass() {
		return this.bathClass;
	}

	public void setGarage(Property.Garage type) {
		this.garage = type;
	}

	public Property.Garage getGarage() {
		return this.garage;
	}

	public void setLargeShed(boolean trueFalse) {
		this.largeShed = trueFalse;
	}

	public boolean getLargeShed() {
		return this.largeShed;
	}

	public void setCoveredPorches(boolean trueFalse) {
		this.coveredPorches = trueFalse;
	}

	public boolean getCoveredPorches() {
		return this.coveredPorches;
	}

	public void setFoundationType(Property.FoundationType type) {
		this.foundation = type;
	}

	public Property.FoundationType getFoundationType() {
 		return this.foundation;
	}

	public void setRoofType(Property.RoofType type) {
		this.roofType = type;
	}

	public Property.RoofType getRoofType() {
		return this.roofType;
	}

	public void setPrimaryHeating(Property.PrimaryHeating type) {
		this.heating = type;
	}

	public Property.PrimaryHeating getPrimaryHeating() {
		return this.heating;
	}

	public void setPlumbing(Property.Plumbing type) {
		this.plumbing = type;
	}

	public Property.Plumbing getPlumbing() {
		return this.plumbing;
	}

	public void setWiring(Property.Wiring type) {
		this.wiring = type;
	}

	public Property.Wiring getWiring() {
		return this.wiring;
	}


	public void setMeasurement(repository.gw.enums.Measurement measurement){
		this.measurement = measurement;
	}

	public Measurement getMeasurement(){
		return this.measurement;
	}
	
//	public int getPhotoYear() {
//		return photoYear;
//	}
//
//	public void setPhotoYear(int photoYear) {
//		this.photoYear = photoYear;
//	}

	public void setElectricalSystem(Property.ElectricalSystem type) {
		this.electricalSystem = type;
	}

	public Property.ElectricalSystem getElectricalSystem() {
		return this.electricalSystem;
	}

	public void setAmps(int amps) {
		this.amps = amps;
	}

	public int getAmps() {
		return this.amps;
	}

	public void setpropertyType(Property.PropertyTypePL type) {
		this.propertyType = type;
	}

	public Property.PropertyTypePL getpropertyType() {
		return this.propertyType;
	}

	public void setstoriesPL(Property.NumberOfStories stories) {
		this.storiesPL = stories;
	}

	public Property.NumberOfStories getstoriesPL() {
		return this.storiesPL;
	}

	public void setFireExtinguishers(boolean fireExtinguishers) {
		this.fireExtinguishers = fireExtinguishers;
	}

	public boolean getFireExtinguishers() {
		return this.fireExtinguishers;
	}

	public void setSmokeAlarms(boolean smokeAlarms) {
		this.smokeAlarms = smokeAlarms;
	}

	public boolean getSmokeAlarms() {
		return this.smokeAlarms;
	}

	public void setNonSmoker(boolean nonSmoker) {
		this.nonSmoker = nonSmoker;
	}

	public boolean getNonSmoker() {
		return this.nonSmoker;
	}

	public void setDeadboltLocks(boolean deadboltLocks) {
		this.deadboltLocks = deadboltLocks;
	}

	public boolean getDeadboltLocks() {
		return this.deadboltLocks;
	}

	public void setDefensibleSpace(boolean defensibleSpace) {
		this.defensibleSpace = defensibleSpace;
	}

	public boolean getDefensibleSpace() {
		return this.defensibleSpace;
	}

	public void setPropertyNumber(int number) {
		this.number = number;
	}

	public int getPropertyNumber() {
		return this.number;
	}

	public void setYearBuilt(int yearBuilt) {
		this.yearBuilt = yearBuilt;
	}

	public int getYearBuilt() {
		return this.yearBuilt;
	}

	public int getSquareFootage() {
		return this.squareFootage;
	}

	public void setSquareFootage(int squareFootage) {
		this.squareFootage = squareFootage;
	}

	public Property.SprinklerSystemType getSprinkerSystem() {
		return this.sprinkerSystem;
	}

	public void setSprinklerSystem(Property.SprinklerSystemType sprinkerSystem) {
		this.sprinkerSystem = sprinkerSystem;
	}

	public void setBuildingAdditionalInterest(repository.gw.generate.custom.AdditionalInterest ai) {
		this.additionalInterestList.add(ai);
	}

	public ArrayList<repository.gw.generate.custom.AdditionalInterest> getBuildingAdditionalInterest() {
		return this.additionalInterestList;
	}

	public void setPolicyLocationBuildingAdditionalInterestArrayList(
			ArrayList<repository.gw.generate.custom.AdditionalInterest> aiList) {
		this.additionalInterestList = aiList;
	}

	public void setbasementFinishedPercent(double _basementFinishedPercent) {
		this.basementFinishedPercent = _basementFinishedPercent;
	}

	public double getBasementFinishedPercent() {
		return this.basementFinishedPercent;
	}

	public Property.NumberOfUnits getNumberOfUnits() {
		return this.units;
	}

	public void setNumberOfUnits(Property.NumberOfUnits _units) {
		this.units = _units;
	}

	public boolean getDwellingVacant() {
		return this.dwellingVacant;
	}

	public void setDwellingVacant(boolean trueFalse) {
		this.dwellingVacant = trueFalse;
	}

	public boolean getWoodFireplace() {
		return this.woodFireplace;
	}

	public void setWoodFireplace(boolean trueFalse) {
		this.woodFireplace = trueFalse;
	}

	public boolean getSwimmingPool() {
		return this.swimmingPool;
	}

	public void setSwimmingPool(boolean trueFalse) {
		this.swimmingPool = trueFalse;
	}

	public boolean getWaterLeaking() {
		return this.waterLeaking;
	}

	public void setWaterLeaking(boolean trueFalse) {
		this.waterLeaking = trueFalse;
	}

	public boolean getAnimals() {
		return this.animals;
	}

	public void setAnimals(boolean trueFalse) {
		this.animals = trueFalse;
	}

	public String getAnimalsDescription() {
		return this.animalsDescription;
	}

	public void setAnimalsDescription(String _animalsDescription) {
		this.animalsDescription = _animalsDescription;
	}

	public Date getLastCleaned() {
		return this.lastCleaned;
	}

	public void setLastCleaned(Date _lastCleaned) {
		this.lastCleaned = _lastCleaned;
	}

	public boolean getChimneysCleanedRegularly() {
		return this.chimneysCleanedRegularly;
	}

	public void setChimneysCleanedRegularly(boolean trueFalse) {
		this.chimneysCleanedRegularly = trueFalse;
	}

	public boolean getPoolFence() {
		return this.poolFence;
	}

	public void setPoolFence(boolean trueFalse) {
		this.poolFence = trueFalse;
	}

	public boolean getDivingBoard() {
		return this.divingBoard;
	}

	public void setDivingBoard(boolean trueFalse) {
		this.divingBoard = trueFalse;
	}

	public boolean getPoolSafetyCover() {
		return this.poolSafetyCover;
	}

	public void setPoolSafetyCover(boolean trueFalse) {
		this.poolSafetyCover = trueFalse;
	}

	public String getSandwichedPolyurethaneDescription() {
		return this.sandwichedPolyurethaneDescription;
	}

	public void setSandwichedPolyurethaneDescription(String _sandwichedPolyurethaneDescription) {
		this.sandwichedPolyurethaneDescription = _sandwichedPolyurethaneDescription;
	}

	public void setPolyurethane(boolean trueFalse) {
		this.polyurethane = trueFalse;
	}

	public boolean getPolyurethane() {
		return this.polyurethane;
	}

	public void setSandwichedPolyurethane(boolean trueFalse) {
		this.sandwichedPolyurethane = trueFalse;
	}

	public boolean getSandwichedPolyurethane() {
		return this.sandwichedPolyurethane;
	}

	public void setConstructionDescription(String _constructionDescription) {
		this.constructionDescription = _constructionDescription;
	}

	public String getConstructionDescription() {
		return this.constructionDescription;
	}

	public repository.gw.generate.custom.SquirePropertySpecificExclusionsConditions getExclusionsConditions() {
		return exclusionsConditions;
	}

	public void setExclusionsConditions(SquirePropertySpecificExclusionsConditions exclusionsConditions) {
		this.exclusionsConditions = exclusionsConditions;
	}

	public repository.gw.generate.custom.PLPropertyCoverages getPropertyCoverages() {
		return propertyCoverages;
	}

	public void setPropertyCoverages(PLPropertyCoverages propertyCoverages) {
		this.propertyCoverages = propertyCoverages;
	}

	public String getStorageType(){
		return this.storageType;
	}

	public void setmanualPublicProtectionClassCode(Location.ProtectionClassCode _manualPublicProtectionClassCode) {
		this.manualPublicProtectionClassCode = _manualPublicProtectionClassCode;
	}

	public Location.ProtectionClassCode getManualPublicProtectionClassCode() {
		return this.manualPublicProtectionClassCode;
	}

	public ArrayList<repository.gw.generate.custom.AdditionalInterest> getAdditionalInterestList() {
		return additionalInterestList;
	}

	public void setAdditionalInterestList(ArrayList<AdditionalInterest> additionalInterestList) {
		this.additionalInterestList = additionalInterestList;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setOwner(boolean trueFalse) {
		this.owner = trueFalse;
	}

	public String getStorageTypeOtherDescription() {
		return storageTypeOtherDescription;
	}

	public void setStorageTypeOtherDescription(String storageTypeOtherDescription) {
		this.storageTypeOtherDescription = storageTypeOtherDescription;
	}

	public repository.gw.generate.custom.AddressInfo getAddress() {
		return address;
	}

	public void setAddress(AddressInfo address) {
		this.address = address;
	}

	public Location.ProtectionClassCode getAutoProtectionClass() {
		return autoProtectionClass;
	}

	public void setAutoProtectionClass(Location.ProtectionClassCode autoProtectionClass) {
		this.autoProtectionClass = autoProtectionClass;
	}

	public boolean isOverrideProtectionclass() {
		return overrideProtectionclass;
	}

	public void setOverrideProtectionclass(boolean overrideProtectionclass) {
		this.overrideProtectionclass = overrideProtectionclass;
	}

	public repository.gw.enums.SectionI_UnderwriterIssues getUWIssue() {
		return uwIssue;
	}

	public void setUWIssue(SectionI_UnderwriterIssues assignedUWIssue) {
		this.uwIssue = assignedUWIssue;
	}


	public boolean hadCoverageA() {
		List<Property.PropertyTypePL> coverageAList = Arrays.asList(
				Property.PropertyTypePL.DwellingPremises,
				Property.PropertyTypePL.ResidencePremises,
				Property.PropertyTypePL.VacationHome,
				Property.PropertyTypePL.CondominiumDwellingPremises,
				Property.PropertyTypePL.DwellingUnderConstruction,
				Property.PropertyTypePL.CondominiumResidencePremise,
				Property.PropertyTypePL.CondominiumVacationHome);

		return coverageAList.contains(propertyType);
	}

	public boolean hasCoverageC() {

		return false;
	}

	public boolean hasOtherStructures() {

		return false;
	}

	public boolean hasCoverageB() {

		return false;
	}


	public boolean hasCoverageE() {
		List<Property.PropertyTypePL> coverageEList = Arrays.asList(
				Property.PropertyTypePL.DwellingPremises,
				Property.PropertyTypePL.ResidencePremises,
				Property.PropertyTypePL.VacationHome,
				Property.PropertyTypePL.CondominiumDwellingPremises,
				Property.PropertyTypePL.DwellingUnderConstruction,
				Property.PropertyTypePL.CondominiumResidencePremise,
				Property.PropertyTypePL.CondominiumResidencePremise,
				Property.PropertyTypePL.Contents,
				Property.PropertyTypePL.CondominiumVacationHome);

		return !coverageEList.contains(propertyType);
	}

	public Date getMsYear() {
		return msYear;
	}

	public Date getPhotoYear2() {
		return photoYear2;
	}

	public void setMsYear(Date msYear) {
		this.msYear = msYear;
	}

	public void setPhotoYear2(Date photoYear2) {
		this.photoYear2 = photoYear2;
	}

	public boolean isSetMSPhotoYears() {
		return setMSPhotoYears;
	}

	public void setSetMSPhotoYears(boolean setMSPhotoYears) {
		this.setMSPhotoYears = setMSPhotoYears;
	}


	public boolean isOpenFalme() {
		return openFalme;
	}

	public void setOpenFalme(boolean openFalme) {
		this.openFalme = openFalme;
	}
}

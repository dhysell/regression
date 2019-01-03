package repository.gw.generate.custom;

import persistence.globaldatarepo.entities.BuildingClassCodes;
import repository.gw.enums.SectionI_UnderwriterIssues;

import java.util.ArrayList;
import java.util.Arrays;

public class PolicyLocationBuilding extends Building {

	//private int number;
	private boolean namedInsuredOwner = false;
	private repository.gw.enums.Building.SqFtPercOccupied occupancySqFtPercOccupied;
	private repository.gw.enums.Building.PercAreaLeasedToOthers occupancyPercAreaLeasedToOthers;
	private repository.gw.enums.Building.OccupancyInterestType occupancyNamedInsuredInterest = repository.gw.enums.Building.OccupancyInterestType.TenantOperator;
	private String classClassification = "";
	private boolean buildingLimitCoverage = true;
	private float buildingLimit = 250000;
	private repository.gw.enums.Building.ValuationMethod buildingValuationMethod = repository.gw.enums.Building.ValuationMethod.ReplacementCost;
	private repository.gw.enums.Building.CauseOfLoss buildingCauseOfLoss = repository.gw.enums.Building.CauseOfLoss.Special;
	private boolean buildingRoofExlusiongEndorsement = false;
	private boolean buildingWindstormHailLossesToRoofSurfacing = false;
	private boolean bppLimitCoverage = true;
	private float bppLimit = 250000;
	private repository.gw.enums.Building.ValuationMethod bppValuationMethod = repository.gw.enums.Building.ValuationMethod.ReplacementCost;
	private repository.gw.enums.Building.CauseOfLoss bppCauseOfLoss = repository.gw.enums.Building.CauseOfLoss.Special;
	private repository.gw.enums.Building.HouseKeepingMaint exteriorHouseKeepingAndMaintenance = repository.gw.enums.Building.HouseKeepingMaint.Superior;
	private repository.gw.enums.Building.HouseKeepingMaint interiorHouseKeepingAndMaintenance = repository.gw.enums.Building.HouseKeepingMaint.Superior;
	private ArrayList<repository.gw.enums.Building.ParkingLotSidewalkCharacteristics> parkingLotCharacteristicsList = new ArrayList<repository.gw.enums.Building.ParkingLotSidewalkCharacteristics>(Arrays.asList(repository.gw.enums.Building.ParkingLotSidewalkCharacteristics.NoneOfAbove));
	private ArrayList<repository.gw.enums.Building.SafetyEquipment> safetyEquipmentList = new ArrayList<repository.gw.enums.Building.SafetyEquipment>(Arrays.asList(repository.gw.enums.Building.SafetyEquipment.NoneOfAbove));
	private boolean exitsProperlyMarked = true;
	private int numFireExtinguishers = 5;
	private boolean exposureToFlammablesChemicals = false;
	private String exposureToFlammablesChemicalsDesc;
	private boolean sprinklered = true;
	private int photoYear = 2017;
	private int costEstimatorYear = 2017;
	private boolean existingDamage = false;
	private String existingDamageDesc;
	private repository.gw.enums.Building.FireBurglaryTypeOfSystem fireBurglaryTypeOfSystem;
	private repository.gw.enums.Building.FireBurglaryResponseType fireBurglaryResponseType;
	private repository.gw.enums.Building.FireBurglaryAlarmGrade fireBurglaryAlarmGrade;
	private String alarmCertificate;
	private boolean insuredPropertyWithin100Ft = false;
	private String insuredPropertyWithin100FtPolicyHolderName;
	private String insuredPropertyWithin100FtPolicyNumber;
	private ArrayList<repository.gw.generate.custom.AdditionalInterest> additionalInterestList = new ArrayList<repository.gw.generate.custom.AdditionalInterest>();
	private repository.gw.generate.custom.PolicyLocationBuildingAdditionalCoverages additionalCoveragesStuff = new repository.gw.generate.custom.PolicyLocationBuildingAdditionalCoverages();
	
	private repository.gw.generate.custom.AddressInfo locationAddress = null;
	private repository.gw.enums.SectionI_UnderwriterIssues sectionIUWIssue = null;
	
	
	
	private int protectionClassCode = 4;
	private boolean WoodBurningStove = false;

	public PolicyLocationBuilding() {

	}
	
	public PolicyLocationBuilding(String classCode) {
		this.classCode = classCode;
	}

	public PolicyLocationBuilding(ArrayList<repository.gw.generate.custom.AdditionalInterest> additionalInterestList) {
		setAdditionalInterestList(additionalInterestList);
	}

	public boolean isNamedInsuredOwner() {
		return namedInsuredOwner;
	}

	public void setNamedInsuredOwner(boolean namedInsuredOwner) {
		this.namedInsuredOwner = namedInsuredOwner;
	}

	public repository.gw.enums.Building.SqFtPercOccupied getOccupancySqFtPercOccupied() {
		return occupancySqFtPercOccupied;
	}

	public void setOccupancySqFtPercOccupied(repository.gw.enums.Building.SqFtPercOccupied occupancySqFtPercOccupied) {
		this.occupancySqFtPercOccupied = occupancySqFtPercOccupied;
	}

	public repository.gw.enums.Building.PercAreaLeasedToOthers getOccupancyPercAreaLeasedToOthers() {
		return occupancyPercAreaLeasedToOthers;
	}

	public void setOccupancyPercAreaLeasedToOthers(repository.gw.enums.Building.PercAreaLeasedToOthers occupancyPercAreaLeasedToOthers) {
		this.occupancyPercAreaLeasedToOthers = occupancyPercAreaLeasedToOthers;
	}

	public repository.gw.enums.Building.OccupancyInterestType getOccupancyNamedInsuredInterest() {
		return occupancyNamedInsuredInterest;
	}

	public void setOccupancyNamedInsuredInterest(repository.gw.enums.Building.OccupancyInterestType occupancyNamedInsuredInterest) {
		this.occupancyNamedInsuredInterest = occupancyNamedInsuredInterest;
	}

	public String getClassClassification() {
		return classClassification;
	}

	public void setClassClassification(String classClassification) {
		this.classClassification = classClassification;
	}

	public void setClassClassification(BuildingClassCodes building) {
		this.classClassification = building.getDescription();
	}

	public void setClassCode(BuildingClassCodes classCode) {
		this.classCode = classCode.getCode();
	}

	public boolean isBuildingLimitCoverage() {
		return buildingLimitCoverage;
	}

	public void setBuildingLimitCoverage(boolean buildingLimitCoverage) {
		this.buildingLimitCoverage = buildingLimitCoverage;
	}

	public float getBuildingLimit() {
		return buildingLimit;
	}

	public void setBuildingLimit(float buildingLimit) {
		this.buildingLimit = buildingLimit;
	}

	public repository.gw.enums.Building.ValuationMethod getBuildingValuationMethod() {
		return buildingValuationMethod;
	}

	public void setBuildingValuationMethod(repository.gw.enums.Building.ValuationMethod buildingValuationMethod) {
		this.buildingValuationMethod = buildingValuationMethod;
	}

	public repository.gw.enums.Building.CauseOfLoss getBuildingCauseOfLoss() {
		return buildingCauseOfLoss;
	}

	public void setBuildingCauseOfLoss(repository.gw.enums.Building.CauseOfLoss buildingCauseOfLoss) {
		this.buildingCauseOfLoss = buildingCauseOfLoss;
	}

	public boolean isBuildingRoofExlusiongEndorsement() {
		return buildingRoofExlusiongEndorsement;
	}

	public void setBuildingRoofExlusiongEndorsement(boolean buildingRoofExlusiongEndorsement) {
		this.buildingRoofExlusiongEndorsement = buildingRoofExlusiongEndorsement;
	}

	public boolean isBuildingWindstormHailLossesToRoofSurfacing() {
		return buildingWindstormHailLossesToRoofSurfacing;
	}

	public void setBuildingWindstormHailLossesToRoofSurfacing(boolean buildingWindstormHailLossesToRoofSurfacing) {
		this.buildingWindstormHailLossesToRoofSurfacing = buildingWindstormHailLossesToRoofSurfacing;
	}

	public boolean isBppLimitCoverage() {
		return bppLimitCoverage;
	}

	public void setBppLimitCoverage(boolean bppLimitCoverage) {
		this.bppLimitCoverage = bppLimitCoverage;
	}

	public float getBppLimit() {
		return bppLimit;
	}

	public void setBppLimit(float bppLimit) {
		this.bppLimit = bppLimit;
	}

	public repository.gw.enums.Building.ValuationMethod getBppValuationMethod() {
		return bppValuationMethod;
	}

	public void setBppValuationMethod(repository.gw.enums.Building.ValuationMethod bppValuationMethod) {
		this.bppValuationMethod = bppValuationMethod;
	}

	public repository.gw.enums.Building.CauseOfLoss getBppCauseOfLoss() {
		return bppCauseOfLoss;
	}

	public void setBppCauseOfLoss(repository.gw.enums.Building.CauseOfLoss bppCauseOfLoss) {
		this.bppCauseOfLoss = bppCauseOfLoss;
	}

	public repository.gw.enums.Building.HouseKeepingMaint getExteriorHouseKeepingAndMaintenance() {
		return exteriorHouseKeepingAndMaintenance;
	}

	public void setExteriorHouseKeepingAndMaintenance(repository.gw.enums.Building.HouseKeepingMaint exteriorHouseKeepingAndMaintenance) {
		this.exteriorHouseKeepingAndMaintenance = exteriorHouseKeepingAndMaintenance;
	}

	public repository.gw.enums.Building.HouseKeepingMaint getInteriorHouseKeepingAndMaintenance() {
		return interiorHouseKeepingAndMaintenance;
	}

	public void setInteriorHouseKeepingAndMaintenance(repository.gw.enums.Building.HouseKeepingMaint interiorHouseKeepingAndMaintenance) {
		this.interiorHouseKeepingAndMaintenance = interiorHouseKeepingAndMaintenance;
	}

	public ArrayList<repository.gw.enums.Building.ParkingLotSidewalkCharacteristics> getParkingLotCharacteristicsList() {
		return parkingLotCharacteristicsList;
	}

	public void setParkingLotCharacteristicsList(
			ArrayList<repository.gw.enums.Building.ParkingLotSidewalkCharacteristics> parkingLotCharacteristicsList) {
		this.parkingLotCharacteristicsList = parkingLotCharacteristicsList;
	}

	public ArrayList<repository.gw.enums.Building.SafetyEquipment> getSafetyEquipmentList() {
		return safetyEquipmentList;
	}

	public void setSafetyEquipmentList(ArrayList<repository.gw.enums.Building.SafetyEquipment> safetyEquipmentList) {
		this.safetyEquipmentList = safetyEquipmentList;
	}

	public boolean isExitsProperlyMarked() {
		return exitsProperlyMarked;
	}

	public void setExitsProperlyMarked(boolean exitsProperlyMarked) {
		this.exitsProperlyMarked = exitsProperlyMarked;
	}

	public int getNumFireExtinguishers() {
		return numFireExtinguishers;
	}

	public void setNumFireExtinguishers(int numFireExtinguishers) {
		this.numFireExtinguishers = numFireExtinguishers;
	}

	public boolean isExposureToFlammablesChemicals() {
		return exposureToFlammablesChemicals;
	}

	public void setExposureToFlammablesChemicals(boolean exposureToFlammablesChemicals) {
		this.exposureToFlammablesChemicals = exposureToFlammablesChemicals;
	}

	public String getExposureToFlammablesChemicalsDesc() {
		return exposureToFlammablesChemicalsDesc;
	}

	public void setExposureToFlammablesChemicalsDesc(String exposureToFlammablesChemicalsDesc) {
		this.exposureToFlammablesChemicalsDesc = exposureToFlammablesChemicalsDesc;
	}

	public repository.gw.enums.Building.ConstructionType getConstructionType() {
		return constructionType;
	}

	public void setConstructionType(repository.gw.enums.Building.ConstructionType constructionType) {
		this.constructionType = constructionType;
	}

	public boolean isSprinklered() {
		return sprinklered;
	}

	public void setSprinklered(boolean sprinklered) {
		this.sprinklered = sprinklered;
	}

	public int getPhotoYear() {
		return photoYear;
	}

	public void setPhotoYear(int photoYear) {
		this.photoYear = photoYear;
	}

	public int getCostEstimatorYear() {
		return costEstimatorYear;
	}

	public void setCostEstimatorYear(int costEstimatorYear) {
		this.costEstimatorYear = costEstimatorYear;
	}


	public boolean isExistingDamage() {
		return existingDamage;
	}

	public void setExistingDamage(boolean existingDamage) {
		this.existingDamage = existingDamage;
	}

	public String getExistingDamageDesc() {
		return existingDamageDesc;
	}

	public void setExistingDamageDesc(String existingDamageDesc) {
		this.existingDamageDesc = existingDamageDesc;
	}

	public repository.gw.enums.Building.FireBurglaryTypeOfSystem getFireBurglaryTypeOfSystem() {
		return fireBurglaryTypeOfSystem;
	}

	public void setFireBurglaryTypeOfSystem(repository.gw.enums.Building.FireBurglaryTypeOfSystem fireBurglaryTypeOfSystem) {
		this.fireBurglaryTypeOfSystem = fireBurglaryTypeOfSystem;
	}

	public repository.gw.enums.Building.FireBurglaryResponseType getFireBurglaryResponseType() {
		return fireBurglaryResponseType;
	}

	public void setFireBurglaryResponseType(repository.gw.enums.Building.FireBurglaryResponseType fireBurglaryResponseType) {
		this.fireBurglaryResponseType = fireBurglaryResponseType;
	}

	public repository.gw.enums.Building.FireBurglaryAlarmGrade getFireBurglaryAlarmGrade() {
		return fireBurglaryAlarmGrade;
	}

	public void setFireBurglaryAlarmGrade(repository.gw.enums.Building.FireBurglaryAlarmGrade fireBurglaryAlarmGrade) {
		this.fireBurglaryAlarmGrade = fireBurglaryAlarmGrade;
	}

	public String getAlarmCertificate() {
		return alarmCertificate;
	}

	public void setAlarmCertificate(String alarmCertificate) {
		this.alarmCertificate = alarmCertificate;
	}

	public boolean isInsuredPropertyWithin100Ft() {
		return insuredPropertyWithin100Ft;
	}

	public void setInsuredPropertyWithin100Ft(boolean insuredPropertyWithin100Ft) {
		this.insuredPropertyWithin100Ft = insuredPropertyWithin100Ft;
	}

	public String getInsuredPropertyWithin100FtPolicyHolderName() {
		return insuredPropertyWithin100FtPolicyHolderName;
	}

	public void setInsuredPropertyWithin100FtPolicyHolderName(String insuredPropertyWithin100FtPolicyHolderName) {
		this.insuredPropertyWithin100FtPolicyHolderName = insuredPropertyWithin100FtPolicyHolderName;
	}

	public String getInsuredPropertyWithin100FtPolicyNumber() {
		return insuredPropertyWithin100FtPolicyNumber;
	}

	public void setInsuredPropertyWithin100FtPolicyNumber(String insuredPropertyWithin100FtPolicyNumber) {
		this.insuredPropertyWithin100FtPolicyNumber = insuredPropertyWithin100FtPolicyNumber;
	}

	public ArrayList<repository.gw.generate.custom.AdditionalInterest> getAdditionalInterestList() {
		return additionalInterestList;
	}

	public void setAdditionalInterestList(ArrayList<AdditionalInterest> additionalInterestList) {
		this.additionalInterestList = additionalInterestList;
	}

	public repository.gw.generate.custom.PolicyLocationBuildingAdditionalCoverages getAdditionalCoveragesStuff() {
		return additionalCoveragesStuff;
	}

	public void setAdditionalCoveragesStuff(PolicyLocationBuildingAdditionalCoverages additionalCoveragesStuff) {
		this.additionalCoveragesStuff = additionalCoveragesStuff;
	}

	public int getProtectionClassCode() {
		return protectionClassCode;
	}

	public void setProtectionClassCode(int protectionClassCode) {
		this.protectionClassCode = protectionClassCode;
	}

	public boolean isWoodBurningStove() {
		return WoodBurningStove;
	}

	public void setWoodBurningStove(boolean woodBurningStove) {
		WoodBurningStove = woodBurningStove;
	}

	public repository.gw.generate.custom.AddressInfo getLocationAddress() {
		return locationAddress;
	}

	public void setLocationAddress(AddressInfo locationAddress) {
		this.locationAddress = locationAddress;
	}

	public repository.gw.enums.SectionI_UnderwriterIssues getSectionIUWIssue() {
		return sectionIUWIssue;
	}

	public void setSectionIUWIssue(SectionI_UnderwriterIssues sectionIUWIssue) {
		this.sectionIUWIssue = sectionIUWIssue;
	}

}

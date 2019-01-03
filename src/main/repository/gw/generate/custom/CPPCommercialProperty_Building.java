package repository.gw.generate.custom;

import persistence.globaldatarepo.entities.CPClassCodes;
import persistence.globaldatarepo.entities.CPSpecialClassCodes;
import persistence.globaldatarepo.entities.CPUWQuestions;
import persistence.globaldatarepo.helpers.CPClassCodesHelper;
import persistence.globaldatarepo.helpers.CPSpecialClassCodesHelper;
import persistence.globaldatarepo.helpers.CPUWQuestionsHelper;
import repository.gw.enums.CPUWIssues;
import repository.gw.enums.CommercialProperty;
import repository.gw.enums.Location;
import repository.gw.enums.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class CPPCommercialProperty_Building extends Building {

	private boolean fillOutAllFields = false;

	private CPPCommercialPropertyProperty propertyLocation = null;

	private String buildingDescription = "Building Foo";

	private repository.gw.enums.CPUWIssues uwIssue;
	private boolean generateUWIssuesFromQuestions = false;
	private boolean multipleOccupancy = false;
	private repository.gw.enums.Building.MultipleOccupancyClassCode multipleOccupancyClassCode = repository.gw.enums.Building.MultipleOccupancyClassCode.LargeArea0433;
	protected String classCode = "0074";
	private String itemNumber = "";
	private String classCodeDescription = "";
	private String classStatus = "";
	private boolean isSpecialClassCode = false;

	private boolean overrideProtectionClass = false;
	private Location.ProtectionClassCode protectionClassCode = Location.ProtectionClassCode.Prot3;
	private Location.ProtectionClassCode autoProtecitonClass = Location.ProtectionClassCode.Prot3;
	private repository.gw.enums.Building.ConstructionTypeCPP constructionType = repository.gw.enums.Building.ConstructionTypeCPP.Frame;

	private CommercialProperty.Condominium condominium = CommercialProperty.Condominium.NotACondominium;
	

	private repository.gw.enums.Building.SqFtPercOccupied percentOccupied = repository.gw.enums.Building.SqFtPercOccupied.SeventyFiveOneHundredPerc;
	private String percentRentedToOthers = "0";
	
	private repository.gw.enums.Building.RoofingTypeCPP roofTypeCPP = repository.gw.enums.Building.RoofingTypeCPP.Aluminum;
	private repository.gw.enums.Building.RoofMaintScheduleCPP roofMaintSchedule = repository.gw.enums.Building.RoofMaintScheduleCPP.EveryYear;
	
	private boolean sprinklered = true;
	private boolean fullyEnclosed = true;
	private Property.PrimaryHeating heatingSource = Property.PrimaryHeating.Gas;

	private String coverageForm;//CREATE ENUM FOR THIS ONE
	private CommercialProperty.RateType rateType = CommercialProperty.RateType.Class;
	private boolean overrideRateType = false;

	private repository.gw.enums.Building.FireBurglaryAlarmType burglaryAndRobberyProtectiveSafeguards_CP_12_11 = repository.gw.enums.Building.FireBurglaryAlarmType.NONE;
	private boolean ProtectiveSafeguards_CP_04_11_P2 = false;
	private boolean ProtectiveSafeguards_CP_04_11_P3 = false;
	private boolean ProtectiveSafeguards_CP_04_11_P4 = false;
	private boolean ProtectiveSafeguards_CP_04_11_P5 = false;
	private boolean ProtectiveSafeguards_CP_04_11_P9 = false;
	

	private CPPCommercialProperty_Building_Coverages coverages = new CPPCommercialProperty_Building_Coverages();

	private CPPCommercialProperty_Building_AdditionalCoverages additionalCoverages = new CPPCommercialProperty_Building_AdditionalCoverages();

	private CPPCommercialProperty_Building_ExclusionsConditions exclusionsConditions = new CPPCommercialProperty_Building_ExclusionsConditions();


	//UNDERWRITING QUESTIONS
	private List<repository.gw.generate.custom.CPPCPClassCodeUWQuestions> uwQuestionList = new ArrayList<repository.gw.generate.custom.CPPCPClassCodeUWQuestions>();

	private String doesthebuildinghaveafoundation = "� Rock	� Masonry � Sender Block � Other";//	FA
	private String pleasedescribeother = "Other Stuff and Some Things"; //	QQ
	private boolean havethewallsbeenstarted = false;//	QQ
	private String whoisthebuildingdeededto = "Me, Myself, and Some guy Named Carl";//	FA
	private boolean doesstockincludesecondhandinvetory = false; //	FA
	private String providebreakdownofpercentageofinventory_typeofinvetoryetc = "No!!!";//	FA
	private boolean isthisbuildingarenovation = false; //	Boolean Radio	-	QQ
	private String whatwasthestartdate = "1/1/1990";//	Date Field	-	
	private String whatistheanticipatedcompletiondate = "1/1/202";//	Date Field	-	
	private String indicatetypeofproject = "� New construction	� Addition or remodeling";	
	private boolean hastheconstructionprogressedpastthefoundationstage = true;//	Boolean Radio	-	
	private boolean isthepropertybeingworkedonbytheapplicantinsuredcontractor = false;//	Boolean Radio	-	-
	private boolean areAdditionalInsurableIntereststobeincluded = false;//	Boolean Radio	-	QQ
	private boolean existingDamage = false;
	private String existingDamageDescription = "One wall is missing, and foundation is split in two. Nothing Major";
	private String isTheApplicantTheBuildingOwnerOfTennant = "Owner";
	
	private repository.gw.enums.Building.HouseKeepingMaint exteriorHousekeepingMaint = repository.gw.enums.Building.HouseKeepingMaint.Superior;
	private repository.gw.enums.Building.HouseKeepingMaint interiorHousekeepingMaint = repository.gw.enums.Building.HouseKeepingMaint.Superior;
	
	
	public CPPCommercialProperty_Building(CommercialProperty.CommercialProperty_ExclusionsAndConditions...exclusionConditions) {
		for(CommercialProperty.CommercialProperty_ExclusionsAndConditions exclusionCondition : exclusionConditions) {
			setExclusionCondition(exclusionCondition);
		}
	}
	
	public CPPCommercialProperty_Building(CommercialProperty.AdditionalCoverages...additionalCoverages) {
		for(CommercialProperty.AdditionalCoverages additionalCoverage : additionalCoverages) {
			setAdditionalCoverage(additionalCoverage);
		}
	}
	
	public CPPCommercialProperty_Building(CommercialProperty.PropertyCoverages coverage) {
		if(coverage.equals(CommercialProperty.PropertyCoverages.BuildersRiskCoverageForm_CP_00_20)) this.classCode = "1150";
		try {
			uwQuestions(classCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(coverage.equals(CommercialProperty.PropertyCoverages.PropertyInTheOpen)) {
			try {
				CPSpecialClassCodes specialClassCode = CPSpecialClassCodesHelper.getRandomSpecialClassCode();
				this.classCode = specialClassCode.getClassCode();
				this.itemNumber = specialClassCode.getItemNumber();
				this.classCodeDescription = specialClassCode.getDescription();
				this.classStatus = specialClassCode.getClassStatus();
				this.isSpecialClassCode = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		List<CommercialProperty.PropertyCoverages> buildingCoverageList = new ArrayList<CommercialProperty.PropertyCoverages>();
		buildingCoverageList.add(coverage);
		this.getCoverages().setBuildingCoveragesList(buildingCoverageList);
	}

	public CPPCommercialProperty_Building(CommercialProperty.PropertyCoverages coverage1, CommercialProperty.PropertyCoverages coverage2) {
		try {
			uwQuestions(classCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<CommercialProperty.PropertyCoverages> buildingCoverageList = new ArrayList<CommercialProperty.PropertyCoverages>();
		buildingCoverageList.add(coverage1);
		buildingCoverageList.add(coverage2);
		this.getCoverages().setBuildingCoveragesList(buildingCoverageList);
	}

	public CPPCommercialProperty_Building() {
		System.out.println("creating new buildng");
		try {
			uwQuestions(classCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CPPCommercialProperty_Building(repository.gw.enums.CPUWIssues uwIssue) {
		this.uwIssue = uwIssue;
		try {
			uwQuestions(classCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CPPCommercialProperty_Building(boolean randomClassCode) {
		try {
			CPClassCodes classcode = CPClassCodesHelper.getRandomClassCode();
			this.classCode = classcode.getClassCode();
			this.classCodeDescription = classcode.getDescription();
			switch(classcode.getRated()) {
			case "Class":
				this.rateType = CommercialProperty.RateType.Class;
				break;
			case "Specific":
				this.rateType = CommercialProperty.RateType.Specific;
				break;
			case "Tentative":
				this.rateType = CommercialProperty.RateType.Tentative;
				break;
			}
			uwQuestions(classCode);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public CPPCommercialProperty_Building(String classCode) {
		this.classCode = classCode;
		try {
			CPClassCodes classcode = CPClassCodesHelper.getPropertyClassCodeByCode(classCode);
			this.classCodeDescription = classcode.getDescription();
			switch(classcode.getRated()) {
			case "Class":
				this.rateType = CommercialProperty.RateType.Class;
				break;
			case "Specific":
				this.rateType = CommercialProperty.RateType.Specific;
				break;
			case "Tentative":
				this.rateType = CommercialProperty.RateType.Tentative;
				break;
			}
			uwQuestions(classCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(classCode.equals("0702(7)")) {
			if(coverages.buildingCoverageList.contains(CommercialProperty.PropertyCoverages.BuildingCoverage)) {
				coverages.setBuildingCoverage_Deductible(CommercialProperty.BuildingCoverageDeductible.FiveThousand);
			}
		}
	}

	public boolean isFullyEnclosed() {
		return fullyEnclosed;
	}

	public void setFullyEnclosed(boolean fullyEnclosed) {
		this.fullyEnclosed = fullyEnclosed;
	}

	public boolean isSprinklered() {
		return sprinklered;
	}

	public void setSprinklered(boolean sprinklered) {
		this.sprinklered = sprinklered;
	}

	public CommercialProperty.Condominium getCondominium() {
		return condominium;
	}

	public void setCondominium(CommercialProperty.Condominium condominium) {
		this.condominium = condominium;
	}
	
	public void setRandomCondominium(CommercialProperty.Condominium...condominium) {
		List<CommercialProperty.Condominium> condoList = new ArrayList<CommercialProperty.Condominium>();
		for(CommercialProperty.Condominium condo : condominium) {
			condoList.add(condo);
		}
		this.condominium = condoList.get(new Random().nextInt(condoList.size()));
	}

	public String getCoverageForm() {
		return coverageForm;
	}

	public void setCoverageForm(String coverageForm) {
		this.coverageForm = coverageForm;
	}

	public CommercialProperty.RateType getRateType() {
		return rateType;
	}

	public void setRateType(CommercialProperty.RateType rateType) {
		this.rateType = rateType;
	}

	public CPPCommercialProperty_Building_Coverages getCoverages() {
		return coverages;
	}

	public void setCoverages(CPPCommercialProperty_Building_Coverages coverages) {
		this.coverages = coverages;
	}

	public CPPCommercialProperty_Building_AdditionalCoverages getAdditionalCoverages() {
		return additionalCoverages;
	}

	public void setAdditionalCoverages(CPPCommercialProperty_Building_AdditionalCoverages additionalCoverages) {
		this.additionalCoverages = additionalCoverages;
	}

	public CPPCommercialProperty_Building_ExclusionsConditions getExclusionsConditions() {
		return exclusionsConditions;
	}

	public void setExclusionsConditions(CPPCommercialProperty_Building_ExclusionsConditions exclusionsConditions) {
		this.exclusionsConditions = exclusionsConditions;
	}

	public String getDoesthebuildinghaveafoundation() {
		return doesthebuildinghaveafoundation;
	}

	public void setDoesthebuildinghaveafoundation(String doesthebuildinghaveafoundation) {
		this.doesthebuildinghaveafoundation = doesthebuildinghaveafoundation;
	}

	public String getPleasedescribeother() {
		return pleasedescribeother;
	}

	public void setPleasedescribeother(String pleasedescribeother) {
		this.pleasedescribeother = pleasedescribeother;
	}

	public boolean isHavethewallsbeenstarted() {
		return havethewallsbeenstarted;
	}

	public void setHavethewallsbeenstarted(boolean havethewallsbeenstarted) {
		this.havethewallsbeenstarted = havethewallsbeenstarted;
	}

	public String getWhoisthebuildingdeededto() {
		return whoisthebuildingdeededto;
	}

	public void setWhoisthebuildingdeededto(String whoisthebuildingdeededto) {
		this.whoisthebuildingdeededto = whoisthebuildingdeededto;
	}

	public boolean isDoesstockincludesecondhandinvetory() {
		return doesstockincludesecondhandinvetory;
	}

	public void setDoesstockincludesecondhandinvetory(boolean doesstockincludesecondhandinvetory) {
		this.doesstockincludesecondhandinvetory = doesstockincludesecondhandinvetory;
	}

	public String getProvidebreakdownofpercentageofinventory_typeofinvetoryetc() {
		return providebreakdownofpercentageofinventory_typeofinvetoryetc;
	}

	public void setProvidebreakdownofpercentageofinventory_typeofinvetoryetc(String providebreakdownofpercentageofinventory_typeofinvetoryetc) {
		this.providebreakdownofpercentageofinventory_typeofinvetoryetc = providebreakdownofpercentageofinventory_typeofinvetoryetc;
	}

	public boolean isIsthisbuildingarenovation() {
		return isthisbuildingarenovation;
	}

	public void setIsthisbuildingarenovation(boolean isthisbuildingarenovation) {
		this.isthisbuildingarenovation = isthisbuildingarenovation;
	}

	public String getWhatwasthestartdate() {
		return whatwasthestartdate;
	}

	public void setWhatwasthestartdate(String whatwasthestartdate) {
		this.whatwasthestartdate = whatwasthestartdate;
	}

	public String getWhatistheanticipatedcompletiondate() {
		return whatistheanticipatedcompletiondate;
	}

	public void setWhatistheanticipatedcompletiondate(String whatistheanticipatedcompletiondate) {
		this.whatistheanticipatedcompletiondate = whatistheanticipatedcompletiondate;
	}

	public String getIndicatetypeofproject() {
		return indicatetypeofproject;
	}

	public void setIndicatetypeofproject(String indicatetypeofproject) {
		this.indicatetypeofproject = indicatetypeofproject;
	}

	public boolean isHastheconstructionprogressedpastthefoundationstage() {
		return hastheconstructionprogressedpastthefoundationstage;
	}

	public void setHastheconstructionprogressedpastthefoundationstage(boolean hastheconstructionprogressedpastthefoundationstage) {
		this.hastheconstructionprogressedpastthefoundationstage = hastheconstructionprogressedpastthefoundationstage;
	}

	public boolean isIsthepropertybeingworkedonbytheapplicantinsuredcontractor() {
		return isthepropertybeingworkedonbytheapplicantinsuredcontractor;
	}

	public void setIsthepropertybeingworkedonbytheapplicantinsuredcontractor(boolean isthepropertybeingworkedonbytheapplicantinsuredcontractor) {
		this.isthepropertybeingworkedonbytheapplicantinsuredcontractor = isthepropertybeingworkedonbytheapplicantinsuredcontractor;
	}

	public boolean isAreAdditionalInsurableIntereststobeincluded() {
		return areAdditionalInsurableIntereststobeincluded;
	}

	public void setAreAdditionalInsurableIntereststobeincluded(boolean areAdditionalInsurableIntereststobeincluded) {
		this.areAdditionalInsurableIntereststobeincluded = areAdditionalInsurableIntereststobeincluded;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
		//jlarsen bad coding practice. :(
		try {
			uwQuestions(classCode);
		} catch (Exception e) {
		}
	}

	public Location.ProtectionClassCode getProtectionClassCode() {
		return protectionClassCode;
	}

	public void setProtectionClassCodeCPP(Location.ProtectionClassCode protectionClassCode) {
		this.protectionClassCode = protectionClassCode;
	}

	public repository.gw.enums.Building.ConstructionTypeCPP getConstructionTypeCPP() {
		return constructionType;
	}

	public void setConstructionType(repository.gw.enums.Building.ConstructionTypeCPP constructionType) {
		this.constructionType = constructionType;
	}

	public Property.PrimaryHeating getHeatingSource() {
		return heatingSource;
	}

	public void setHeatingSource(Property.PrimaryHeating heatingSource) {
		this.heatingSource = heatingSource;
	}

	public repository.gw.enums.Building.SqFtPercOccupied getPercentOccupied() {
		return percentOccupied;
	}

	public void setPercentOccupied(repository.gw.enums.Building.SqFtPercOccupied percentOccupied) {
		this.percentOccupied = percentOccupied;
	}

	public String getPercentRentedToOthers() {
		return percentRentedToOthers;
	}

	public void setPercentRentedToOthers(String percentRentedToOthers) {
		this.percentRentedToOthers = percentRentedToOthers;
	}

	public repository.gw.enums.Building.RoofingTypeCPP getRoofTypeCPP() {
		return roofTypeCPP;
	}

	public void setRoofTypeCPP(repository.gw.enums.Building.RoofingTypeCPP roofTypeCPP) {
		this.roofTypeCPP = roofTypeCPP;
	}

	public boolean isMultipleOccupancy() {
		return multipleOccupancy;
	}

	public void setMultipleOccupancy(boolean multipleOccupancy) {
		this.multipleOccupancy = multipleOccupancy;
	}

	public repository.gw.enums.Building.MultipleOccupancyClassCode getMultipleOccupancyClassCode() {
		return multipleOccupancyClassCode;
	}

	public void setMultipleOccupancyClassCode(repository.gw.enums.Building.MultipleOccupancyClassCode multipleOccupancyClassCode) {
		this.multipleOccupancyClassCode = multipleOccupancyClassCode;
	}

	public repository.gw.enums.CPUWIssues getUwIssue() {
		return uwIssue;
	}

	public void setUwIssue(CPUWIssues uwIssue) {
		this.uwIssue = uwIssue;
	}

	private void uwQuestions(String classCode) throws Exception {
		System.out.println("Getting UW Questions for building class code.......may take a bit depending on number of questions");
		uwQuestionList = new ArrayList<repository.gw.generate.custom.CPPCPClassCodeUWQuestions>();
		List<CPUWQuestions> uwQuestions = CPUWQuestionsHelper.getUWQuestionsClassCode(classCode);
		if (uwQuestions != null) {
			for (CPUWQuestions question : uwQuestions) {
				repository.gw.generate.custom.CPPCPClassCodeUWQuestions objectQuestion = new repository.gw.generate.custom.CPPCPClassCodeUWQuestions(question);
				uwQuestionList.add(objectQuestion);
			}
		}
	}

	public List<repository.gw.generate.custom.CPPCPClassCodeUWQuestions> getUwQuestionList() {
		return uwQuestionList;
	}

	public void setUwQuestionList(List<CPPCPClassCodeUWQuestions> uwQuestionList) {
		this.uwQuestionList = uwQuestionList;
	}

	public boolean isExistingDamage() {
		return existingDamage;
	}

	public void setExistingDamage(boolean existingDamage) {
		this.existingDamage = existingDamage;
	}

	public String getExistingDamageDescription() {
		return existingDamageDescription;
	}

	public void setExistingDamageDescription(String existingDamageDescription) {
		this.existingDamageDescription = existingDamageDescription;
	}

	public boolean isGenerateUWIssuesFromQuestions() {
		return generateUWIssuesFromQuestions;
	}

	public void setGenerateUWIssuesFromQuestions(boolean generateUWIssuesFromQuestions) {
		this.generateUWIssuesFromQuestions = generateUWIssuesFromQuestions;
	}

	public Location.ProtectionClassCode getAutoProtecitonClass() {
		return autoProtecitonClass;
	}

	public void setAutoProtecitonClass(Location.ProtectionClassCode autoProtecitonClass) {
		this.autoProtecitonClass = autoProtecitonClass;
	}

	public boolean isOverrideProtectionClass() {
		return overrideProtectionClass;
	}

	public void setOverrideProtectionClass(boolean overrideProtectionClass) {
		this.overrideProtectionClass = overrideProtectionClass;
	}

	public CPPCommercialPropertyProperty getPropertyLocation() {
		return propertyLocation;
	}

	public void setPropertyLocation(CPPCommercialPropertyProperty propertyLocation) {
		this.propertyLocation = propertyLocation;
	}

	public String getBuildingDescription() {
		return buildingDescription;
	}

	public void setBuildingDescription(String buildingDescription) {
		this.buildingDescription = buildingDescription;
	}

	public boolean isFillOutAllFields() {
		return fillOutAllFields;
	}

	public void setFillOutAllFields(boolean fillOutAllFields) {
		this.fillOutAllFields = fillOutAllFields;
	}

	public repository.gw.enums.Building.FireBurglaryAlarmType getBurglaryAndRobberyProtectiveSafeguards_CP_12_11() {
		return burglaryAndRobberyProtectiveSafeguards_CP_12_11;
	}

	public void setBurglaryAndRobberyProtectiveSafeguards_CP_12_11(
			repository.gw.enums.Building.FireBurglaryAlarmType burglaryAndRobberyProtectiveSafeguards_CP_12_11) {
		this.burglaryAndRobberyProtectiveSafeguards_CP_12_11 = burglaryAndRobberyProtectiveSafeguards_CP_12_11;
	}

	public String getIsTheApplicantTheBuildingOwnerOfTennant() {
		return isTheApplicantTheBuildingOwnerOfTennant;
	}

	public void setIsTheApplicantTheBuildingOwnerOfTennant(String isTheApplicantTheBuildingOwnerOfTennant) {
		this.isTheApplicantTheBuildingOwnerOfTennant = isTheApplicantTheBuildingOwnerOfTennant;
	}

	public boolean isOverrideRateType() {
		return overrideRateType;
	}

	public void setOverrideRateType(boolean overrideRateType) {
		this.overrideRateType = overrideRateType;
	}


	private void setAdditionalCoverage(CommercialProperty.AdditionalCoverages additionalCoverage) {
		switch(additionalCoverage) {
		case AdditionalBuildingProperty_CP_14_15:
			this.additionalCoverages.setAdditionalBuildingProperty_CP_14_15(true);
			break;
		case AdditionalCoveredProperty_CP_14_10:
			this.additionalCoverages.setAdditionalCoveredProperty_CP_14_10(true);
			break;
		case AdditionalInsured_BuildingOwner_CP_12_19:
			this.additionalCoverages.setAdditionalInsured_BuildingOwner_CP_12_19(true);
			break;
		case BuildersRiskRenovations_CP_11_13:
			this.additionalCoverages.setBuildersRiskRenovations_CP_11_13(true);
			break;
		case BuildingGlass_TenantsPolicy_CP_14_70:
			break;
		case CondominiumCommercialUnit_OwnersOptionalCoverages_CP_04_18:
			this.additionalCoverages.setCondominiumCommercialUnit_OwnersOptionalCoverages_CP_04_18(true);
			break;
		case DischargeFromSewerDrainOrSumpNotFlood_Related_CP_10_38:
			this.additionalCoverages.setDischargeFromSewerDrainOrSumpNotFlood_Related_CP_10_38(true);
			break;
		case EquipmentBreakdownEnhancementEndorsementID_CP_31_1002:
			this.additionalCoverages.setEquipmentBreakdownEnhancementEndorsementID_CP_31_1002(true);
			break;
		case FunctionalBuildingValuation_CP_04_38:
			this.additionalCoverages.setFunctionalBuildingValuation_CP_04_38(true);
			break;
		case LeasedProperty_CP_14_60:
			this.additionalCoverages.setLeasedProperty_CP_14_60(true);
			break;
		case LossPayableProvisions_CP_12_18:
			this.additionalCoverages.setLossPayableProvisions_CP_12_18(true);
			break;
		case OrdinanceorLawCoverage_CP_04_05:
			this.additionalCoverages.setOrdinanceorLawCoverage_CP_04_05(true);
			break;
		case OutdoorSigns_CP_14_40:
			this.additionalCoverages.setOutdoorSigns_CP_14_40(true);
			break;
		case PayrollLimitationOrExclusion_CP_15_10:
			this.additionalCoverages.setPayrollLimitationOrExclusion_CP_15_10(true);
			break;
		case PeakSeasonLimitOfInsurance_CP_12_30:
			this.additionalCoverages.setPeakSeasonLimitOfInsurance_CP_12_30(true);
			break;
		case RadioOrTelevisionAntennas_CP_14_50:
			this.additionalCoverages.setRadioOrTelevisionAntennas_CP_14_50(true);
			break;
		case SpoilageCoverage_CP_04_40:
			this.additionalCoverages.setSpoilageCoverage_CP_04_40(true);
			break;
		case TheftExclusion_CP_10_33:
			this.additionalCoverages.setTheftExclusion_CP_10_33(true);
			break;
		case TheftOfBuildingMaterialsAndSuppliesOtherThanBuildersRisk_CP_10_44:
			this.additionalCoverages.setTheftOfBuildingMaterialsAndSuppliesOtherThanBuildersRisk_CP_10_44(true);
			break;
		case UtilityServices_DirectDamage_CP_04_17:
			this.additionalCoverages.setUtilityServices_DirectDamage_CP_04_17(true);
			break;
		case UtilityServices_TimeElements_CP_15_45:
			this.additionalCoverages.setUtilityServices_DirectDamage_CP_04_17(true);
			break;
		case BusinessIncomeCoverageForm:
			break;
		case BusinessIncome_LandlordAsAdditionalInsuredRental_Value_CP_15_03:
			break;
		case DiscretionaryPayrollExpense_CP_15_04:
			break;
		case ExtraExpenseCoverageForm_CP_00_50:
			break;
		case FoodContaminationBusinessInterruptionAndExtraExpense_CP_15_05:
			break;
		}
	}

	public repository.gw.enums.Building.HouseKeepingMaint getExteriorHousekeepingMaint() {
		return exteriorHousekeepingMaint;
	}

	public void setExteriorHousekeepingMaint(repository.gw.enums.Building.HouseKeepingMaint exteriorHousekeepingMaint) {
		this.exteriorHousekeepingMaint = exteriorHousekeepingMaint;
	}

	public repository.gw.enums.Building.HouseKeepingMaint getInteriorHousekeepingMaint() {
		return interiorHousekeepingMaint;
	}

	public void setInteriorHousekeepingMaint(repository.gw.enums.Building.HouseKeepingMaint interiorHousekeepingMaint) {
		this.interiorHousekeepingMaint = interiorHousekeepingMaint;
	}

	public repository.gw.enums.Building.RoofMaintScheduleCPP getRoofMaintSchedule() {
		return roofMaintSchedule;
	}

	public void setRoofMaintSchedule(repository.gw.enums.Building.RoofMaintScheduleCPP roofMaintSchedule) {
		this.roofMaintSchedule = roofMaintSchedule;
	}


	private void setExclusionCondition(CommercialProperty.CommercialProperty_ExclusionsAndConditions exclusionCondition) {
		switch (exclusionCondition) {
		case AdditionalPropertyNotCovered_CP_14_20:
			break;
		case BindingArbitration_CR_20_12:
			break;
		case BrokenOrCrackedGlassExclusionForm_IDCP_31_1006:
			break;
		case BurglaryAndRobberyProtectiveSafeguards_CP_12_11:
			break;
		case ChangeInControlOfTheInsured_NoticeToTheCompany_CR_20_29:
			break;
		case CommercialCrimeManuscriptEndorsement_IDCR_31_1001:
			break;
		case CommercialPropertyConditions_CP_00_90:
			break;
		case CommercialPropertyManuscriptEndorsement_IDCP_31_1005:
			break;
		case ConvertToAnAggregateLimitOfInsurance_CR_20_08:
			break;
		case ExcludeCertainRisksInherentInInsuranceOperations_CR_25_23:
			break;
		case ExcludeDesignatedPersonsOrClassesOfPersonsAsEmployees_CR_25_01:
			break;
		case ExcludeDesignatedPremises_CR_35_13:
			break;
		case ExcludeSpecifiedProperty_CR_35_01:
			break;
		case ExcludeUnauthorizedAdvancesRequireAnnualAudit_CR_25_25:
			break;
		case ExclusionOfLossDueToBy_ProductsOfProductionOrProcessingOperationsRentalProperties_CP_10_34:
			break;
		case ExclusionOfLossDueToVirusOrBacteria_CP_01_40:
			break;
		case IdahoChanges_CR_02_12:
			break;
		case LimitationsOnCoverageForRoofSurfacing_CP_10_36:
			break;
		case MultipleDeductibleForm_IDCP_31_1001:
			break;
		case ProtectiveSafeguards_CP_04_11:
			break;
		case RoofExclusionEndorsement_IDCP_31_1004:
			break;
		case SprinklerLeakageExclusion_CP_10_56:
			break;
		case TentativeRate_CP_99_93:
			break;
		case TheftExclusion_CP_10_33:
			break;
		case VandalismExclusion_CP_10_55:
			break;
		case WindstormOrHailExclusion_CP_10_54:
			break;
		}
		
	}
	
	public boolean isProtectiveSafeguards_CP_04_11_P2() {
		return ProtectiveSafeguards_CP_04_11_P2;
	}

	public void setProtectiveSafeguards_CP_04_11_P2(boolean protectiveSafeguards_CP_04_11_P2) {
		ProtectiveSafeguards_CP_04_11_P2 = protectiveSafeguards_CP_04_11_P2;
	}

	public boolean isProtectiveSafeguards_CP_04_11_P3() {
		return ProtectiveSafeguards_CP_04_11_P3;
	}

	public void setProtectiveSafeguards_CP_04_11_P3(boolean protectiveSafeguards_CP_04_11_P3) {
		ProtectiveSafeguards_CP_04_11_P3 = protectiveSafeguards_CP_04_11_P3;
	}

	public boolean isProtectiveSafeguards_CP_04_11_P4() {
		return ProtectiveSafeguards_CP_04_11_P4;
	}

	public void setProtectiveSafeguards_CP_04_11_P4(boolean protectiveSafeguards_CP_04_11_P4) {
		ProtectiveSafeguards_CP_04_11_P4 = protectiveSafeguards_CP_04_11_P4;
	}

	public boolean isProtectiveSafeguards_CP_04_11_P5() {
		return ProtectiveSafeguards_CP_04_11_P5;
	}

	public void setProtectiveSafeguards_CP_04_11_P5(boolean protectiveSafeguards_CP_04_11_P5) {
		ProtectiveSafeguards_CP_04_11_P5 = protectiveSafeguards_CP_04_11_P5;
	}

	public boolean isProtectiveSafeguards_CP_04_11_P9() {
		return ProtectiveSafeguards_CP_04_11_P9;
	}

	public void setProtectiveSafeguards_CP_04_11_P9(boolean protectiveSafeguards_CP_04_11_P9) {
		ProtectiveSafeguards_CP_04_11_P9 = protectiveSafeguards_CP_04_11_P9;
	}

	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	public String getClassCodeDescription() {
		return classCodeDescription;
	}

	public void setClassCodeDescription(String classCodeDescription) {
		this.classCodeDescription = classCodeDescription;
	}

	public String getClassStatus() {
		return classStatus;
	}

	public void setClassStatus(String classStatus) {
		this.classStatus = classStatus;
	}

	public boolean isSpecialClassCode() {
		return isSpecialClassCode;
	}

	public void setSpecialClassCode(boolean isSpecialClassCode) {
		this.isSpecialClassCode = isSpecialClassCode;
	}




}














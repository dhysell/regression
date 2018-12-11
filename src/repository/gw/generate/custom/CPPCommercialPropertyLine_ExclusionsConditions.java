package repository.gw.generate.custom;

import java.util.ArrayList;
import java.util.List;


public class CPPCommercialPropertyLine_ExclusionsConditions {
	
	public CPPCommercialPropertyLine_ExclusionsConditions() {
		this.personsOrClassesOfPersones.add("Foo");
		this.excludeDesignatedPremises_CR_35_13_InsideThePremises_TheftOfMoneyAndSecurities.add(new AddressInfo(true));
		this.excludeDesignatedPremises_CR_35_13_InsideThePremises_RobberyOrSafeBurglaryOfOtherProperty.add(new AddressInfo(true));
		this.excludeDesignatedPremises_CR_35_13_OutsideThePremises.add(new AddressInfo(true));
		this.excludeDesignatedPremises_CR_35_13_InsideThePremises_TheftOfOtherProperty.add(new AddressInfo(true));
		this.excludeSpecifiedProperty_CR_35_01_InsideThePremises_TheftOfMoneyAndSecurities.add("Foo");
		this.excludeSpecifiedProperty_CR_35_01_InsideThePremises_RobberyOrSafeBurglaryOfOtherProperty.add("Foo");
		this.excludeSpecifiedProperty_CR_35_01_OutsideThePremises.add("Foo");
		this.excludeSpecifiedProperty_CR_35_01_InsideThePremises_TheftOfOtherProperty.add("Foo");
		this.commercialCrimeManuscriptEndorsement_IDCR_31_1001_Condition_List.add("Manuscript Endorsement String.");
		this.commercialPropertyManuscriptEndorsement_IDCP_31_1005_Condition_List.add("Manuscript Endorsement String.");
	}
	
	private boolean hasChanged = false;

	//EXCLUSIONS

	private boolean excludeCertainRisksInherentInInsuranceOperations_CR_25_23 = false;

	private boolean excludeDesignatedPersonsOrClassesOfPersonsAsEmployees_CR_25_01 = false;
	private List<String> personsOrClassesOfPersones = new ArrayList<String>();

	private boolean excludeDesignatedPremises_CR_35_13 = false;
	private List<AddressInfo> excludeDesignatedPremises_CR_35_13_InsideThePremises_TheftOfMoneyAndSecurities = new ArrayList<AddressInfo>();
	private List<AddressInfo> excludeDesignatedPremises_CR_35_13_InsideThePremises_RobberyOrSafeBurglaryOfOtherProperty = new ArrayList<AddressInfo>();
	private List<AddressInfo> excludeDesignatedPremises_CR_35_13_OutsideThePremises = new ArrayList<AddressInfo>();
	private List<AddressInfo> excludeDesignatedPremises_CR_35_13_InsideThePremises_TheftOfOtherProperty = new ArrayList<AddressInfo>();

	
	private boolean excludeSpecifiedProperty_CR_35_01 = false;
	private List<String> excludeSpecifiedProperty_CR_35_01_InsideThePremises_TheftOfMoneyAndSecurities = new ArrayList<String>();
	private List<String> excludeSpecifiedProperty_CR_35_01_InsideThePremises_RobberyOrSafeBurglaryOfOtherProperty = new ArrayList<String>();
	private List<String> excludeSpecifiedProperty_CR_35_01_OutsideThePremises = new ArrayList<String>();
	private List<String> excludeSpecifiedProperty_CR_35_01_InsideThePremises_TheftOfOtherProperty = new ArrayList<String>();

	private boolean excludeUnauthorizedAdvancesRequireAnnualAudit_CR_25_25 = false;

	private boolean exclusionOfLossDueToVirusOrBacteria_CP_01_40 = false;
	
	//CONDITIONS
	private boolean bindingArbitration_CR_20_12 = true;
	
	@SuppressWarnings("unused")
	private boolean changeInControlOfTheInsured_NoticeToTheCompany_CR_20_29 = true;
	
	private boolean commercialCrimeManuscriptEndorsement_IDCR_31_1001_Condition = false;
	private List<String> commercialCrimeManuscriptEndorsement_IDCR_31_1001_Condition_List = new ArrayList<String>();
	
	private boolean commercialPropertyConditions_CP_00_90 = false;
	
	private boolean commercialPropertyManuscriptEndorsement_IDCP_31_1005_Condition = false;
	private List<String> commercialPropertyManuscriptEndorsement_IDCP_31_1005_Condition_List = new ArrayList<String>();
	
	private boolean convertToAnAggregateLimitOfInsurance_CR_20_08 = false;
	private int convertToAnAggregateLimitOfInsurance_CR_20_08_AggregateLimit = 100;
	
	@SuppressWarnings("unused")
	private boolean idahoChanges_CR_02_12 = true;
	
	private boolean multipleDeductibleForm_IDCP_31_1001 = false;
	
	

	public boolean isExcludeCertainRisksInherentInInsuranceOperations_CR_25_23() {
		return excludeCertainRisksInherentInInsuranceOperations_CR_25_23;
	}

	public void setExcludeCertainRisksInherentInInsuranceOperations_CR_25_23(boolean excludeCertainRisksInherentInInsuranceOperations_CR_25_23) {
		this.excludeCertainRisksInherentInInsuranceOperations_CR_25_23 = excludeCertainRisksInherentInInsuranceOperations_CR_25_23;
	}

	public boolean isExcludeDesignatedPersonsOrClassesOfPersonsAsEmployees_CR_25_01() {
		return excludeDesignatedPersonsOrClassesOfPersonsAsEmployees_CR_25_01;
	}

	public void setExcludeDesignatedPersonsOrClassesOfPersonsAsEmployees_CR_25_01(boolean excludeDesignatedPersonsOrClassesOfPersonsAsEmployees_CR_25_01) {
		this.excludeDesignatedPersonsOrClassesOfPersonsAsEmployees_CR_25_01 = excludeDesignatedPersonsOrClassesOfPersonsAsEmployees_CR_25_01;
	}

	public boolean isExcludeDesignatedPremises_CR_35_13() {
		return excludeDesignatedPremises_CR_35_13;
	}

	public void setExcludeDesignatedPremises_CR_35_13(boolean excludeDesignatedPremises_CR_35_13) {
		this.excludeDesignatedPremises_CR_35_13 = excludeDesignatedPremises_CR_35_13;
	}

	public boolean isExcludeSpecifiedProperty_CR_35_01() {
		return excludeSpecifiedProperty_CR_35_01;
	}

	public void setExcludeSpecifiedProperty_CR_35_01(boolean excludeSpecifiedProperty_CR_35_01) {
		this.excludeSpecifiedProperty_CR_35_01 = excludeSpecifiedProperty_CR_35_01;
	}

	public boolean isExclusionOfLossDueToVirusOrBacteria_CP_01_40() {
		return exclusionOfLossDueToVirusOrBacteria_CP_01_40;
	}

	public void setExclusionOfLossDueToVirusOrBacteria_CP_01_40(boolean exclusionOfLossDueToVirusOrBacteria_CP_01_40) {
		this.exclusionOfLossDueToVirusOrBacteria_CP_01_40 = exclusionOfLossDueToVirusOrBacteria_CP_01_40;
	}

	public boolean isBindingArbitration_CR_20_12() {
		return bindingArbitration_CR_20_12;
	}

	public void setBindingArbitration_CR_20_12(boolean bindingArbitration_CR_20_12) {
		this.bindingArbitration_CR_20_12 = bindingArbitration_CR_20_12;
	}

	public boolean isCommercialCrimeManuscriptEndorsement_IDCR_31_1001_Condition() {
		return commercialCrimeManuscriptEndorsement_IDCR_31_1001_Condition;
	}

	public void setCommercialCrimeManuscriptEndorsement_IDCR_31_1001_Condition(boolean commercialCrimeManuscriptEndorsement_IDCR_31_1001) {
		this.commercialCrimeManuscriptEndorsement_IDCR_31_1001_Condition = commercialCrimeManuscriptEndorsement_IDCR_31_1001;
	}

	public boolean isCommercialPropertyConditions_CP_00_90() {
		return commercialPropertyConditions_CP_00_90;
	}

	public void setCommercialPropertyConditions_CP_00_90(boolean commercialPropertyConditions_CP_00_90) {
		this.commercialPropertyConditions_CP_00_90 = commercialPropertyConditions_CP_00_90;
	}

	public boolean isCommercialPropertyManuscriptEndorsement_IDCP_31_1005_Condition() {
		return commercialPropertyManuscriptEndorsement_IDCP_31_1005_Condition;
	}

	public void setCommercialPropertyManuscriptEndorsement_IDCP_31_1005_Condition(boolean commercialPropertyManuscriptEndorsement_IDCP_31_1005) {
		this.commercialPropertyManuscriptEndorsement_IDCP_31_1005_Condition = commercialPropertyManuscriptEndorsement_IDCP_31_1005;
	}

	public boolean isMultipleDeductibleForm_IDCP_31_1001() {
		return multipleDeductibleForm_IDCP_31_1001;
	}

	public void setMultipleDeductibleForm_IDCP_31_1001(boolean multipleDeductibleForm_IDCP_31_1001) {
		this.multipleDeductibleForm_IDCP_31_1001 = multipleDeductibleForm_IDCP_31_1001;
	}

	public boolean isExcludeUnauthorizedAdvancesRequireAnnualAudit_CR_25_25() {
		return excludeUnauthorizedAdvancesRequireAnnualAudit_CR_25_25;
	}

	public void setExcludeUnauthorizedAdvancesRequireAnnualAudit_CR_25_25(boolean excludeUnauthorizedAdvancesRequireAnnualAudit_CR_25_25) {
		this.excludeUnauthorizedAdvancesRequireAnnualAudit_CR_25_25 = excludeUnauthorizedAdvancesRequireAnnualAudit_CR_25_25;
	}

	public int getConvertToAnAggregateLimitOfInsurance_CR_20_08_AggregateLimit() {
		return convertToAnAggregateLimitOfInsurance_CR_20_08_AggregateLimit;
	}

	public void setConvertToAnAggregateLimitOfInsurance_CR_20_08_AggregateLimit(int convertToAnAggregateLimitOfInsurance_CR_20_08_AggregateLimit) {
		this.convertToAnAggregateLimitOfInsurance_CR_20_08_AggregateLimit = convertToAnAggregateLimitOfInsurance_CR_20_08_AggregateLimit;
	}

	public boolean isConvertToAnAggregateLimitOfInsurance_CR_20_08() {
		return convertToAnAggregateLimitOfInsurance_CR_20_08;
	}

	public void setConvertToAnAggregateLimitOfInsurance_CR_20_08(boolean convertToAnAggregateLimitOfInsurance_CR_20_08) {
		this.convertToAnAggregateLimitOfInsurance_CR_20_08 = convertToAnAggregateLimitOfInsurance_CR_20_08;
	}

	public boolean hasChanged() {
		return hasChanged;
	}

	public void setHasChanged(boolean hasChanged) {
		this.hasChanged = hasChanged;
	}

	public List<String> getCommercialCrimeManuscriptEndorsement_IDCR_31_1001_Condition_List() {
		return commercialCrimeManuscriptEndorsement_IDCR_31_1001_Condition_List;
	}

	public void setCommercialCrimeManuscriptEndorsement_IDCR_31_1001_Condition_List(List<String> commercialCrimeManuscriptEndorsement_IDCR_31_1001_Condition_List) {
		this.commercialCrimeManuscriptEndorsement_IDCR_31_1001_Condition_List = commercialCrimeManuscriptEndorsement_IDCR_31_1001_Condition_List;
	}

	public List<String> getCommercialPropertyManuscriptEndorsement_IDCP_31_1005_Condition_List() {
		return commercialPropertyManuscriptEndorsement_IDCP_31_1005_Condition_List;
	}

	public void setCommercialPropertyManuscriptEndorsement_IDCP_31_1005_Condition_List(List<String> commercialPropertyManuscriptEndorsement_IDCP_31_1005_Condition_List) {
		this.commercialPropertyManuscriptEndorsement_IDCP_31_1005_Condition_List = commercialPropertyManuscriptEndorsement_IDCP_31_1005_Condition_List;
	}

	public List<AddressInfo> getExcludeDesignatedPremises_CR_35_13_InsideThePremises_TheftOfMoneyAndSecurities() {
		return excludeDesignatedPremises_CR_35_13_InsideThePremises_TheftOfMoneyAndSecurities;
	}

	public void setExcludeDesignatedPremises_CR_35_13_InsideThePremises_TheftOfMoneyAndSecurities(List<AddressInfo> excludeDesignatedPremises_CR_35_13_InsideThePremises_TheftOfMoneyAndSecurities) {
		this.excludeDesignatedPremises_CR_35_13_InsideThePremises_TheftOfMoneyAndSecurities = excludeDesignatedPremises_CR_35_13_InsideThePremises_TheftOfMoneyAndSecurities;
	}

	public List<AddressInfo> getExcludeDesignatedPremises_CR_35_13_InsideThePremises_RobberyOrSafeBurglaryOfOtherProperty() {
		return excludeDesignatedPremises_CR_35_13_InsideThePremises_RobberyOrSafeBurglaryOfOtherProperty;
	}

	public void setExcludeDesignatedPremises_CR_35_13_InsideThePremises_RobberyOrSafeBurglaryOfOtherProperty(List<AddressInfo> excludeDesignatedPremises_CR_35_13_InsideThePremises_RobberyOrSafeBurglaryOfOtherProperty) {
		this.excludeDesignatedPremises_CR_35_13_InsideThePremises_RobberyOrSafeBurglaryOfOtherProperty = excludeDesignatedPremises_CR_35_13_InsideThePremises_RobberyOrSafeBurglaryOfOtherProperty;
	}

	public List<AddressInfo> getExcludeDesignatedPremises_CR_35_13_OutsideThePremises() {
		return excludeDesignatedPremises_CR_35_13_OutsideThePremises;
	}

	public void setExcludeDesignatedPremises_CR_35_13_OutsideThePremises(List<AddressInfo> excludeDesignatedPremises_CR_35_13_OutsideThePremises) {
		this.excludeDesignatedPremises_CR_35_13_OutsideThePremises = excludeDesignatedPremises_CR_35_13_OutsideThePremises;
	}

	public List<AddressInfo> getExcludeDesignatedPremises_CR_35_13_InsideThePremises_TheftOfOtherProperty() {
		return excludeDesignatedPremises_CR_35_13_InsideThePremises_TheftOfOtherProperty;
	}

	public void setExcludeDesignatedPremises_CR_35_13_InsideThePremises_TheftOfOtherProperty(List<AddressInfo> excludeDesignatedPremises_CR_35_13_InsideThePremises_TheftOfOtherProperty) {
		this.excludeDesignatedPremises_CR_35_13_InsideThePremises_TheftOfOtherProperty = excludeDesignatedPremises_CR_35_13_InsideThePremises_TheftOfOtherProperty;
	}

	public List<String> getExcludeSpecifiedProperty_CR_35_01_InsideThePremises_TheftOfMoneyAndSecurities() {
		return excludeSpecifiedProperty_CR_35_01_InsideThePremises_TheftOfMoneyAndSecurities;
	}

	public void setExcludeSpecifiedProperty_CR_35_01_InsideThePremises_TheftOfMoneyAndSecurities(List<String> excludeSpecifiedProperty_CR_35_01_InsideThePremises_TheftOfMoneyAndSecurities) {
		this.excludeSpecifiedProperty_CR_35_01_InsideThePremises_TheftOfMoneyAndSecurities = excludeSpecifiedProperty_CR_35_01_InsideThePremises_TheftOfMoneyAndSecurities;
	}

	public List<String> getExcludeSpecifiedProperty_CR_35_01_InsideThePremises_RobberyOrSafeBurglaryOfOtherProperty() {
		return excludeSpecifiedProperty_CR_35_01_InsideThePremises_RobberyOrSafeBurglaryOfOtherProperty;
	}

	public void setExcludeSpecifiedProperty_CR_35_01_InsideThePremises_RobberyOrSafeBurglaryOfOtherProperty(List<String> excludeSpecifiedProperty_CR_35_01_InsideThePremises_RobberyOrSafeBurglaryOfOtherProperty) {
		this.excludeSpecifiedProperty_CR_35_01_InsideThePremises_RobberyOrSafeBurglaryOfOtherProperty = excludeSpecifiedProperty_CR_35_01_InsideThePremises_RobberyOrSafeBurglaryOfOtherProperty;
	}

	public List<String> getExcludeSpecifiedProperty_CR_35_01_OutsideThePremises() {
		return excludeSpecifiedProperty_CR_35_01_OutsideThePremises;
	}

	public void setExcludeSpecifiedProperty_CR_35_01_OutsideThePremises(List<String> excludeSpecifiedProperty_CR_35_01_OutsideThePremises) {
		this.excludeSpecifiedProperty_CR_35_01_OutsideThePremises = excludeSpecifiedProperty_CR_35_01_OutsideThePremises;
	}

	public List<String> getExcludeSpecifiedProperty_CR_35_01_InsideThePremises_TheftOfOtherProperty() {
		return excludeSpecifiedProperty_CR_35_01_InsideThePremises_TheftOfOtherProperty;
	}

	public void setExcludeSpecifiedProperty_CR_35_01_InsideThePremises_TheftOfOtherProperty(List<String> excludeSpecifiedProperty_CR_35_01_InsideThePremises_TheftOfOtherProperty) {
		this.excludeSpecifiedProperty_CR_35_01_InsideThePremises_TheftOfOtherProperty = excludeSpecifiedProperty_CR_35_01_InsideThePremises_TheftOfOtherProperty;
	}

	public List<String> getPersonsOrClassesOfPersones() {
		return personsOrClassesOfPersones;
	}

	public void setPersonsOrClassesOfPersones(ArrayList<String> personsOrClassesOfPersones) {
		this.personsOrClassesOfPersones = personsOrClassesOfPersones;
	}

	
	
	
	
	
	
}















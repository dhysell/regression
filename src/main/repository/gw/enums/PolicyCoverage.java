package repository.gw.enums;

public enum PolicyCoverage {
	
	EmployeeDishonestyOptionalCoverage("Employee Dishonesty-Optional Coverage"), 
	ForgeryandAlteration("Forgery and Alteration"), 
	TheftofClientsPropertyBP_14_03("Theft of Client's Property BP 14 03"),
	HiredAutoBP_04_04("Hired Auto BP 04 04"), 
	NonownedAutoLiabilityBP_04_04("Non-owned Auto Liability BP 04 04"), 
	LiquorLiabilityBP_04_88("Liquor Liability BP 04 88"),
	LiquorLiabilityCoverageBP_04_89("Liquor Liability Coverage BP 04 89"), 
	BeautySalonsandBarberShopProfessionalLiabilityIDBP_31_2002("Beauty Salons and Barber Shop Professional Liability IDBP 31 2002"), 
	ElectronicData("Electronic Data"),
	EquipmentBreakdownEnhancementEndorsementIDBP_31_1002("Equipment Breakdown Enhancement Endorsement IDBP 31 1002"), 
	InsuranceToValueBP_04_83("Insurance To Value BP 04 83"), 
	AdditionalInsured_EngineersArchitectsOrSurveyorsBP_04_13("Additional Insured - Engineers, Architects, Or Surveyors BP 04 13"),
	ComprehensiveBusinessLiabilityExclusionBP_04_01("Comprehensive Business Liability Exclusion BP 04 01"),
	Exclusion_DamageToWorkPerformedBySubcontractorsonYourBehalfBP_14_19("Exclusion - Damage To Work Performed By Subcontractors on Your Behalf BP 14 19"), 
	LimitationofCoveragetoDesignatedPremisesorProjectBP_04_12("Limitation of Coverage to Designated Premises or Project BP 04 12"), 
	CondominiumAssociationCoverageBP_17_01("Condominium Association Coverage BP 17 01"),
	CondominiumCommercialUnit_OwnersCoverageBP_17_02("Condominium Commercial Unit-Owners Coverage BP 17 02"), 
	LiabilityManuscriptEndorsementIDBP_31_2003("Liability Manuscript Endorsement IDBP 31 2003"),
	PropertyManuscriptEndorsementIDBP_31_1003("Property Manuscript Endorsement IDBP 31 1003");
	
	String value;
	
	private PolicyCoverage(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}

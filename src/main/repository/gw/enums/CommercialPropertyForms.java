package repository.gw.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public enum CommercialPropertyForms {
	
	BuildingAndPersonalPropertyCoverageForm_CP_00_10("Building And Personal Property Coverage Form"),
	CondominiumAssociationCoverageForm_CP_00_17("Condominium Association Coverage Form"),
	CondominiumCommercialUnit_OwnersCoverageForm_CP_00_18("Condominium Commercial Unit-Owners Coverage Form"),
	BuildersRiskCoverageForm_CP_00_20("Builders' Risk Coverage Form"),
	BusinessIncome_AndExtraExpense_CoverageForm_CP_00_30("Business Income (And Extra Expense) Coverage Form"),
	BusinessIncome_WithoutExtraExpense_CoverageForm_CP_00_32("Business Income (Without Extra Expense) Coverage Form"),
	LegalLiabilityCoverageForm_CP_00_40("Legal Liability Coverage Form"),
	ExtraExpenseCoverageForm_CP_00_50("Extra Expense Coverage Form"),
	CommercialPropertyConditions_CP_00_90("Commercial Property Conditions"),
	ExclusionOfLossDueToVirusOrBacteria_CP_01_40("Exclusion Of Loss Due To Virus Or Bacteria"),
	OrdinanceOrLawCoverage_CP_04_05("Ordinance Or Law Coverage"),
	ProtectiveSafeguards_CP_04_11("Protective Safeguards"),
	UtilityServices_DirectDamage_CP_04_17("Utility Services � Direct Damage"),
	CondominiumCommercialUnit_OwnersOptionalCoverages_CP_04_18("Condominium Commercial Unit-Owners Optional Coverages"),
	FunctionalBuildingValuation_CP_04_38("Functional Building Valuation"),
	SpoilageCoverage_CP_04_40("Spoilage Coverage"),
	CausesOfLoss_BasicForm_CP_10_10("Causes Of Loss - Basic Form"),
	CausesOfLoss_BroadForm_CP_10_20("Causes Of Loss - Broad Form"),
	CausesOfLoss_SpecialForm_CP_10_30("Causes Of Loss - Special Form"),
	TheftExclusion_CP_10_33("Theft Exclusion"),
	ExclusionOfLossDueToBy_ProductsOfProductionOrProcessingOperations_RentalProperties__CP_10_34("Exclusion Of Loss Due To By-Products Of Production Or Processing Operations (Rental Properties)"),
	LimitationsOnCoverageForRoofSurfacing_CP_10_36("Limitations On Coverage For Roof Surfacing"),
	DischargeFromSewer_DrainOrSump_NotFlood_Related_CP_10_38("Discharge From Sewer, Drain Or Sump (Not Flood-Related)"),
	TheftOfBuildingMaterialsAndSupplies_OtherThanBuildersRisk__CP_10_44("Theft Of Building Materials And Supplies (Other Than Builders Risk)"),
	WindstormOrHailExclusion_CP_10_54("Windstorm Or Hail Exclusion"),
	VandalismExclusion_CP_10_55("Vandalism Exclusion"),
	SprinklerLeakageExclusion_CP_10_56("Sprinkler Leakage Exclusion"),
	BuildersRiskRenovations_CP_11_13("Builders' Risk Renovations"),
	BurglaryAndRobberyProtectiveSafeguards_CP_12_11("Burglary And Robbery Protective Safeguards"),
	LossPayableProvisions_CP_12_18("Loss Payable Provisions"),
	AdditionalInsured_BuildingOwner_CP_12_19("Additional Insured - Building Owner"),
	PeakSeasonLimitOfInsurance_CP_12_30("Peak Season Limit Of Insurance"),
	UnscheduledBuildingPropertyTenantsPolicy_CP_14_02("Unscheduled Building Property Tenant's Policy"),
	AdditionalCoveredProperty_CP_14_10("Additional Covered Property"),
	AdditionalBuildingProperty_CP_14_15("Additional Building Property"),
	AdditionalPropertyNotCovered_CP_14_20("Additional Property Not Covered"),
	OutdoorSigns_CP_14_40("Outdoor Signs"),
	RadioOrTelevisionAntennas_CP_14_50("Radio Or Television Antennas"),
	LeasedProperty_CP_14_60("Leased Property"),
	BusinessIncome_LandlordAsAdditionalInsured_RentalValue__CP_15_03("Business Income - Landlord As Additional Insured (Rental Value)"),
	DiscretionaryPayrollExpense_CP_15_04("Discretionary Payroll Expense"),
	FoodContamination_BusinessInterruptionAndExtraExpense__CP_15_05("Food Contamination (Business Interruption And Extra Expense)"),
	PayrollLimitationOrExclusion_CP_15_10("Payroll Limitation Or Exclusion"),
	UtilityServices_TimeElements_CP_15_45("Utility Services � Time Elements"),
	TentativeRate_CP_99_93("Tentative Rate"),
	LegalLiabilityCoverageSchedule_CP_DS_05("Legal Liability Coverage Schedule"),
	CommercialPropertyCoveragePartDeclarationsPage_IDCP_03_0001("Commercial Property Coverage Part Declarations Page"),
	PropertyDisclosureNotice_IDCP_10_0002("Property Disclosure Notice"),
	ProtectiveDeviceLetter_IDCP_11_0001("Protective Device Letter"),
	HighValueLetter_IDCP_11_0002("High Value Letter"),
	MultipleDeductibleForm_IDCP_31_1001("Multiple Deductible Form"),
	EquipmentBreakdownEnhancementEndorsement_IDCP_31_1002("Equipment Breakdown Enhancement Endorsement"),
	RoofExclusionEndorsement_IDCP_31_1004("Roof Exclusion Endorsement"),
	CommercialPropertyManuscriptEndorsement_IDCP_31_1005("Commercial Property Manuscript Endorsement"),
	BrokenOrCrackedGlassExclusionForm_IDCP_31_1006("Broken Or Cracked Glass Exclusion Form"),
	CommercialCrimeCoverageForm_LossSustainedForm__CR_00_21("Commercial Crime Coverage Form (Loss Sustained Form)"),
	IdahoChanges_CR_02_12("Idaho Changes"),
	ClientsProperty_CR_04_01("Clients' Property"),
	InsideThePremises_TheftOfOtherProperty_CR_04_05("Inside The Premises - Theft Of Other Property"),
	GuestsProperty_CR_04_11("Guests' Property"),
	ConvertToAnAggregateLimitOfInsurance_CR_20_08("Convert To An Aggregate Limit Of Insurance"),
	BindingArbitration_CR_20_12("Binding Arbitration"),
	JointLossPayable_CR_20_15("Joint Loss Payable"),
	ChangeInControlOfTheInsured_NoticeToTheCompany_CR_20_29("Change In Control Of The Insured - Notice To The Company"),
	ExcludeDesignatedPersonsOrClassesOfPersonsAsEmployees_CR_25_01("Exclude Designated Persons Or Classes Of Persons As Employees"),
	IncludeVolunteerWorkersOtherThanFundSolicitorsAsEmployees_CR_25_10("Include Volunteer Workers Other Than Fund Solicitors As Employees"),
	ExcludeCertainRisksInherentInInsuranceOperations_CR_25_23("Exclude Certain Risks Inherent In Insurance Operations"),
	ExcludeUnauthorizedAdvances_RequireAnnualAudit_CR_25_25("Exclude Unauthorized Advances, Require Annual Audit"),
	ExcludeSpecifiedProperty_CR_35_01("Exclude Specified Property"),
	ExcludeDesignatedPremises_CR_35_13("Exclude Designated Premises"),
	CommercialCrimeManuscriptEndorsement_IDCR_31_1001("Commercial Crime Manuscript Endorsement"),
	CommercialCrimePolicyDeclarations_IDCR_03_0001("Commercial Crime Policy Declarations"),
	CrimePolicyholderDisclosureNotice_IDCR_10_0001("Crime Policyholder Disclosure Notice"),
	CertificateOfPropertyInsurance_ACORD_24("Certificate Of Property Insurance"),
	EvidenceOfCommercialPropertyInsurance_ACORD_28("Evidence Of Commercial Property Insurance");
	
	String value;
	
	private CommercialPropertyForms(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
	
	private static final List<CommercialPropertyForms> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
	public static CommercialPropertyForms random()  {
		return VALUES.get(new Random().nextInt(VALUES.size()));
	}
	
}

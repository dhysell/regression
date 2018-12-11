package repository.gw.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum CommercialAutoForm {
	BusinessAutoCoverageForm_CA_00_01("Business Auto Coverage Form"),
	IdahoChanges_CA_01_18("Idaho Changes"),
	DeductibleLiabilityCoverage_CA_03_01("Deductible Liability Coverage"),
	ExclusionOfFederalEmployeesUsingAutosInGovernmentBusiness_CA_04_42("Exclusion Of Federal Employees Using Autos In Government Business"),
	WaiverOfTransferOfRightsOfRecoveryAgainstOthersToUs_WaiverOfSubrogation_CA_04_44("Waiver Of Transfer Of Rights Of Recovery Against Others To Us (Waiver Of Subrogation)"),
	PrimaryAndNoncontributory_OtherInsuranceCondition_CA_04_49("Primary And Noncontributory - Other Insurance Condition"),
	Lessor_AdditionalInsuredAndLossPayee_CA_20_01("Lessor - Additional Insured And Loss Payee"),
	MobileHomesContentsCoverage_CA_20_16("Mobile Homes Contents Coverage"),
	ProfessionalServicesNotCovered_CA_20_18("Professional Services Not Covered"),
	DesignatedInsuredForCoveredAutosLiabilityCoverage_CA_20_48("Designated Insured For Covered Autos Liability Coverage"),
	EmployeeHiredAutos_CA_20_54("Employee Hired Autos"),
	CoverageForCertainOperationsInConnectionWithRailroads_CA_20_70("Coverage For Certain Operations In Connection With Railroads"),
	AutoLoan_LeaseGapCoverage_CA_20_71("Auto Loan/Lease Gap Coverage"),
	Explosives_CA_23_01("Explosives"),
	RollingStores_CA_23_04("Rolling Stores"),
	WrongDeliveryOfLiquidProducts_CA_23_05("Wrong Delivery Of Liquid Products"),
	MotorCarriers_InsuranceForNon_TruckingUse_CA_23_09("Motor Carriers - Insurance For Non-Trucking Use"),
	PublicOrLiveryPassengerConveyanceExclusion_CA_23_44("Public Or Livery Passenger Conveyance Exclusion"),
	SilicaOrSilica_relatedDustExclusionForCoveredAutosExposure_CA_23_94("Silica Or Silica-related Dust Exclusion For Covered Autos Exposure"),
	AmphibiousVehicles_CA_23_97("Amphibious Vehicles"),
	PublicTransportationAutos_CA_24_02("Public Transportation Autos"),
	IdahoUninsuredMotoristsCoverage_CA_31_15("Idaho Uninsured Motorists Coverage"),
	IdahoUnderinsuredMotoristsCoverage_CA_31_18("Idaho Underinsured Motorists Coverage"),
	AutoMedicalPaymentsCoverage_CA_99_03("Auto Medical Payments Coverage"),
	DriveOtherCarCoverage_BroadenedCoverageForNamedIndividuals_CA_99_10("Drive Other Car Coverage - Broadened Coverage For Named Individuals"),
	FireFireAndTheftFireTheftAndWindstormAndLimitedSpecifiedCausesOfLossCoverages_CA_99_14("Fire, Fire And Theft, Fire, Theft And Windstorm And Limited Specified Causes Of Loss Coverages"),
	IndividualNamedInsured_CA_99_17("Individual Named Insured"),
	RentalReimbursementCoverage_CA_99_23("Rental Reimbursement Coverage"),
	GaragekeepersCoverage_CA_99_37("Garagekeepers Coverage"),
	ExclusionOrExcessCoverageHazardsOtherwiseInsured_CA_99_40("Exclusion Or Excess Coverage Hazards Otherwise Insured"),
	AudioVisualandDataElectronicEquipmentCoverageAddedLimits_CA_99_60("Audio, Visual and Data Electronic Equipment Coverage Added Limits"),
	MCS_90("Endorsement For Motor Carrier Policies Of Insurance For Public Liability Under Sections 29 and 30 Of the Motor Carrier Act of 1980"),
	BusinessAutoDeclarations_IDCA_03_0001("Business Auto Declarations"),
	PermanentLiabilityCards_IDCA_04_0001("Permanent Liability Cards"),
	AutoLossPayableEvidenceOfInsurance_IDCA_04_0002("Auto Loss Payable Evidence Of Insurance"),
	IdahoUninsuredMotoristAndUnderinsuredMotoristDisclosureStatement_IDCA_10_0001("Idaho Uninsured Motorist And Underinsured Motorist Disclosure Statement"),
	IdahoUninsuredMotoristAndUnderinsuredMotoristDisclosureStatement_IDCA_10_0002("Idaho Uninsured Motorist And Underinsured Motorist Disclosure Statement"),
	AutoDisclosureNotice_IDCA_10_0003("Auto Disclosure Notice"),
	ExcludedDriverAcknowledgmentLetter_IDCA_18_0001("Excluded Driver Acknowledgment Letter"),
	LossPayableClause_IDCA_31_3001("Loss Payable Clause"),
	LossPayableClause_AudioVisualAndDataElectronicEquipmentCoverageAddedLimits_IDCA_31_3002("Loss Payable Clause - Audio, Visual And Data Electronic Equipment Coverage Added Limits"),
	MobileHomesContentsNotCovered_IDCA_31_3003("Mobile Homes Contents Not Covered"),
	LiabilityCoverageForRecreationalOrPersonalUseTrailers_IDCA_31_3005("Liability Coverage For Recreational Or Personal Use Trailers"),
	RemovalOfPropertyDamageCoverage_IDCA_31_3006("Removal Of Property Damage Coverage"),
	ExcludedDriverEndorsement_IDCA_31_3007("Excluded Driver Endorsement"),
	RoadsideAssistanceEndorsement_IDCA_31_3008("Roadside Assistance Endorsement"),
	AdditionalNamedInsuredForDesignatedPersonOrOrganization_IDCA_31_3009("Additional Named Insured For Designated Person Or Organization "),
	OutOfStateVehicleExclusion_IDCA_31_3011("Out Of State Vehicle Exclusion"),
	MotorCarrierEndorsement_IDCA_31_3012("Motor Carrier Endorsement"),
	CommercialAutoManuscriptEndorsement_IDCA_31_3013("Commercial Auto Manuscript Endorsement"),
	OtherVehicleInsuranceEndorsement_IDCA_31_3015("Other Vehicle Insurance Endorsement");
	
	String value;
	
	private CommercialAutoForm(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
	
	public static CommercialAutoForm valueOfName(final String name) {
		final String enumName = name.replaceAll(" ", "").replace("-", "_").replace(",", "").replace("/", "");
		try {
			return valueOf(enumName);
		} catch (final IllegalArgumentException e) {
			return null;
		}
	}
	
	private static final List<CommercialAutoForm> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
	private static final int SIZE = VALUES.size();
	private static final Random RANDOM = new Random();

	public static CommercialAutoForm random()  {
		return VALUES.get(RANDOM.nextInt(SIZE));
	}
}

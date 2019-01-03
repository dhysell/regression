package repository.gw.generate.custom;

import repository.gw.enums.GeneralLiability;

import java.util.ArrayList;
import java.util.List;

public class CPPGeneralLiabilityCoverages {
	
	private boolean hasChanged = false;
	
	private repository.gw.generate.custom.PolicyLocation location;
	
	//STANDARD COVERAGES
	private GeneralLiability.StandardCoverages.CG0001_OccuranceLimit generalLiabilityOccuranceLimitCG_00_01 = GeneralLiability.StandardCoverages.CG0001_OccuranceLimit.FiveHundredThousand;
	private GeneralLiability.StandardCoverages.CG0001_MedicalPaymentsPerPerson generalLiablilityMedicalPayments = GeneralLiability.StandardCoverages.CG0001_MedicalPaymentsPerPerson.FiveThousand;
	private GeneralLiability.StandardCoverages.CG0001_PersonalAdvertisingInjury generalLiabilityPersonalAdvertisingInjuryLimit = GeneralLiability.StandardCoverages.CG0001_PersonalAdvertisingInjury.None;
	private GeneralLiability.StandardCoverages.CG0001_OperationsAggregateLimit operationsAggregateLimit = GeneralLiability.StandardCoverages.CG0001_OperationsAggregateLimit.None;
	private GeneralLiability.StandardCoverages.CG0001_DeductibleLiabilityInsCG0300 deductibleLiabilityIns_CG_03_00 = GeneralLiability.StandardCoverages.CG0001_DeductibleLiabilityInsCG0300.FiveHundred;
	
	//ADDITIONAL COVERAGES
	private boolean AdditionalInsured_CharitableInstitutionsCG2020 = false;
	private boolean AdditionalInsured_ClubMembersCG2002 = false;
	private boolean AdditionalInsured_EngineersArchitectsOrSurveyorsCG2007 = false;
	private boolean AdditionalInsured_UsersOfGolfmobilesCG2008 = false;
	private boolean AdditionalInsured_UsersOfTeamsDraftOrSaddleAnimalsCG2014 = false;
	private boolean AmendmentOfLiquorLiabilityExclusion_ExceptionForScheduledActivitiesCG2151 = false;
		private String ExceptionForScheduledActivitiesCG2151_Description = "Foo";
	private boolean DesignatedConstructionProjectGeneralAggregateLimitCG2503 = false;
	private boolean DesignatedLocationGeneralAggregateLimitCG2504 = false;
	private boolean EmploymentPracticesLiabilityInsuranceIDCG312013 = true;
	private int numberFullTimeEmployees = 1;
	private int numberPartTimeEmployees = 0;
	private GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_AggregateLimit aggragateLimit = GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_AggregateLimit.FiftyThousand;
	private GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_Deductible employmentPracticesLiabilityInsuranceDeductible = GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_Deductible.FiveThousand;
	private boolean thirdPartyLiability = false;
	private String handRated = "";
	private GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_NumberLocations employmentPracticesLiabilityInsuranceNumberLocations = GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_NumberLocations.ONE;
	
	private GeneralLiability.AdditionalCoverages.LawnCareServicesCoverage_Limit lawnCareServicesCoverageLimit = GeneralLiability.AdditionalCoverages.LawnCareServicesCoverage_Limit.OneFiftyK_ThreeHundredK;
	
	private boolean LimitedCoverageForDesignatedUnmannedAircraftCG2450 = false;
		private int UnmannedAircraftCG2450_Limit = 0;
		private List<String> unmannedAircraft_CG_24_50_MakeModelSerialNumber = new ArrayList<String>();
		private List<String> unmannedAircraft_CG_24_50_DescriptionOfOpperations = new ArrayList<String>();
	
	private boolean LiquorLiability_BringYourOwnAlcoholEstablishmentsCG2406 = false;
	private boolean LiquorLiabilityCoverageFormCG0033 = false;
	private String LiquorLiabilityCoverageFormCG0033_Description = "";
	private double LiquorLiabilityCoverageFormCG0033_Sales = 1.0;
	private int LiquorLiabilityCoverageFormCG0033_TastingRooms = 0;
	
	private boolean PrimaryAndNoncontributory_OtherInsuranceConditionCG2001 = false;
	
	
	
	//EXCLUSIONS AND CONDITIONS
	public boolean ExcessProvisionVendors_CG_24_10 = false;
	public boolean LimitationOfCoverageToDesignatedPremisesOrProject_CG_21_44 = false;
	public boolean LimitationOfCoverageToInsuredPremises_CG_28_06 = false;
	public boolean CommunicableDiseaseExclusion__CG_21_32 = false;
	public boolean Exclusion_AdultDayCareCenters_CG_22_87 = false;
	public boolean Exclusion_AllHazardsInConnectionWithDesignatedPremises_CG_21_00 = false;
	public boolean Exclusion_CoverageCMedicalPayments_CG_21_35 = false;
	public boolean Exclusion_DescribedHazards_CarnivalsCircusesAndFairs_CG_22_58 = false;
	public boolean Exclusion_DesignatedOngoingOperations_CG_21_53 = false;
	public boolean Exclusion_DesignatedProducts_CG_21_33 = false;
	public boolean Exclusion_DesignatedWork_CG_21_34 = false;
	public boolean Exclusion_IntercompanyProductsSuits_CG_21_41 = false;
	public boolean Exclusion_MedicalPaymentsToChildrenDayCareCenters_CG_22_40 = false;
	public boolean Exclusion_NewEntities_CG_21_36 = false;
	public boolean Exclusion_RollingStockRailroadConstruction_CG_22_46 = false;
	public boolean Exclusion_UndergroundResourcesAndEquipment_CG_22_57 = false;
	public boolean Exclusion_VolunteerWorkers_CG_21_66 = false;

	
	
	
	//ADDITIONAL INSUREDS
	List<CPPGLCoveragesAdditionalInsureds> additionalInsuredslist = new ArrayList<CPPGLCoveragesAdditionalInsureds>();
	
	
	//UNDERWRITING QUESTIONS
	List<CPPGLCoveragesUWQuestions> underWritingQuestions_Coverages = new ArrayList<CPPGLCoveragesUWQuestions>();
	
	
	/*public CPPGeneralLiabilityCoverages(PolicyLocation location) throws Exception {
		this.location = location;
		GeneralLiabilityCoverages randCoverage = GeneralLiabilityCoverages.getRandomCoverage();
		this.coverageCode = randCoverage.getValue();
		List<Gluwquestions> uwQuestions = UWQuestionsHelper.getUWQuestionsCo
		if (uwQuestions != null) {
			for (Gluwquestions question : uwQuestions) {
				CPPGLCoveragesUWQuestions objectQuestion = new CPPGLCoveragesUWQuestions(question);
				underWritingQuestions.add(objectQuestion);
			}
		}
		
		
	}*/
	

	public CPPGeneralLiabilityCoverages() {

	}

	
	
	
	
	
	public boolean isAdditionalInsured_CharitableInstitutionsCG2020() {
		return AdditionalInsured_CharitableInstitutionsCG2020;
	}

	public void setAdditionalInsured_CharitableInstitutionsCG2020(
			boolean additionalInsured_CharitableInstitutionsCG2020) {
		AdditionalInsured_CharitableInstitutionsCG2020 = additionalInsured_CharitableInstitutionsCG2020;
	}

	public boolean isAdditionalInsured_ClubMembersCG2002() {
		return AdditionalInsured_ClubMembersCG2002;
	}

	public void setAdditionalInsured_ClubMembersCG2002(boolean additionalInsured_ClubMembersCG2002) {
		AdditionalInsured_ClubMembersCG2002 = additionalInsured_ClubMembersCG2002;
	}

	public boolean isAdditionalInsured_EngineersArchitectsOrSurveyorsCG2007() {
		return AdditionalInsured_EngineersArchitectsOrSurveyorsCG2007;
	}

	public void setAdditionalInsured_EngineersArchitectsOrSurveyorsCG2007(
			boolean additionalInsured_EngineersArchitectsOrSurveyorsCG2007) {
		AdditionalInsured_EngineersArchitectsOrSurveyorsCG2007 = additionalInsured_EngineersArchitectsOrSurveyorsCG2007;
	}

	public boolean isAdditionalInsured_UsersOfGolfmobilesCG2008() {
		return AdditionalInsured_UsersOfGolfmobilesCG2008;
	}

	public void setAdditionalInsured_UsersOfGolfmobilesCG2008(boolean additionalInsured_UsersOfGolfmobilesCG2008) {
		AdditionalInsured_UsersOfGolfmobilesCG2008 = additionalInsured_UsersOfGolfmobilesCG2008;
	}

	public boolean isAdditionalInsured_UsersOfTeamsDraftOrSaddleAnimalsCG2014() {
		return AdditionalInsured_UsersOfTeamsDraftOrSaddleAnimalsCG2014;
	}

	public void setAdditionalInsured_UsersOfTeamsDraftOrSaddleAnimalsCG2014(
			boolean additionalInsured_UsersOfTeamsDraftOrSaddleAnimalsCG2014) {
		AdditionalInsured_UsersOfTeamsDraftOrSaddleAnimalsCG2014 = additionalInsured_UsersOfTeamsDraftOrSaddleAnimalsCG2014;
	}

	public boolean isAmendmentOfLiquorLiabilityExclusion_ExceptionForScheduledActivitiesCG2151() {
		return AmendmentOfLiquorLiabilityExclusion_ExceptionForScheduledActivitiesCG2151;
	}

	public void setAmendmentOfLiquorLiabilityExclusion_ExceptionForScheduledActivitiesCG2151(
			boolean amendmentOfLiquorLiabilityExclusion_ExceptionForScheduledActivitiesCG2151) {
		AmendmentOfLiquorLiabilityExclusion_ExceptionForScheduledActivitiesCG2151 = amendmentOfLiquorLiabilityExclusion_ExceptionForScheduledActivitiesCG2151;
	}

	public String getExceptionForScheduledActivitiesCG2151_Description() {
		return ExceptionForScheduledActivitiesCG2151_Description;
	}

	public void setExceptionForScheduledActivitiesCG2151_Description(
			String exceptionForScheduledActivitiesCG2151_Description) {
		ExceptionForScheduledActivitiesCG2151_Description = exceptionForScheduledActivitiesCG2151_Description;
	}

	public boolean isDesignatedConstructionProjectGeneralAggregateLimitCG2503() {
		return DesignatedConstructionProjectGeneralAggregateLimitCG2503;
	}

	public void setDesignatedConstructionProjectGeneralAggregateLimitCG2503(
			boolean designatedConstructionProjectGeneralAggregateLimitCG2503) {
		DesignatedConstructionProjectGeneralAggregateLimitCG2503 = designatedConstructionProjectGeneralAggregateLimitCG2503;
	}

	public boolean isDesignatedLocationGeneralAggregateLimitCG2504() {
		return DesignatedLocationGeneralAggregateLimitCG2504;
	}

	public void setDesignatedLocationGeneralAggregateLimitCG2504(
			boolean designatedLocationGeneralAggregateLimitCG2504) {
		DesignatedLocationGeneralAggregateLimitCG2504 = designatedLocationGeneralAggregateLimitCG2504;
	}

	public boolean isEmploymentPracticesLiabilityInsuranceIDCG312013() {
		return EmploymentPracticesLiabilityInsuranceIDCG312013;
	}

	public void setEmploymentPracticesLiabilityInsuranceIDCG312013(
			boolean employmentPracticesLiabilityInsuranceIDCG312013) {
		EmploymentPracticesLiabilityInsuranceIDCG312013 = employmentPracticesLiabilityInsuranceIDCG312013;
	}

	public int getNumberFullTimeEmployees() {
		return numberFullTimeEmployees;
	}

	public void setNumberFullTimeEmployees(int numberFullTimeEmployees) {
		this.numberFullTimeEmployees = numberFullTimeEmployees;
	}

	public int getNumberPartTimeEmployees() {
		return numberPartTimeEmployees;
	}

	public void setNumberPartTimeEmployees(int numberPartTimeEmployees) {
		this.numberPartTimeEmployees = numberPartTimeEmployees;
	}

	public GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_AggregateLimit getAggragateLimit() {
		return aggragateLimit;
	}

	public void setAggragateLimit(GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_AggregateLimit aggragateLimit) {
		this.aggragateLimit = aggragateLimit;
	}

	public GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_Deductible getEmploymentPracticesLiabilityInsuranceDeductible() {
		return employmentPracticesLiabilityInsuranceDeductible;
	}

	public void setEmploymentPracticesLiabilityInsuranceDeductible(GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_Deductible employmentPracticesLiabilityInsuranceDeductible) {
		this.employmentPracticesLiabilityInsuranceDeductible = employmentPracticesLiabilityInsuranceDeductible;
	}

	public boolean isThirdPartyLiability() {
		return thirdPartyLiability;
	}

	public void setThirdPartyLiability(boolean thirdPartyLiability) {
		this.thirdPartyLiability = thirdPartyLiability;
	}

	public String getHandRated() {
		return handRated;
	}

	public void setHandRated(String handRated) {
		this.handRated = handRated;
	}

	public GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_NumberLocations getEmploymentPracticesLiabilityInsuranceNumberLocations() {
		return employmentPracticesLiabilityInsuranceNumberLocations;
	}

	public void setEmploymentPracticesLiabilityInsuranceNumberLocations(GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_NumberLocations employmentPracticesLiabilityInsuranceNumberLocations) {
		this.employmentPracticesLiabilityInsuranceNumberLocations = employmentPracticesLiabilityInsuranceNumberLocations;
	}

	public GeneralLiability.AdditionalCoverages.LawnCareServicesCoverage_Limit getLawnCareServicesCoverageLimit() {
		return lawnCareServicesCoverageLimit;
	}

	public void setLawnCareServicesCoverageLimit(GeneralLiability.AdditionalCoverages.LawnCareServicesCoverage_Limit lawnCareServicesCoverageLimit) {
		this.lawnCareServicesCoverageLimit = lawnCareServicesCoverageLimit;
	}

	public boolean isLimitedCoverageForDesignatedUnmannedAircraftCG2450() {
		return LimitedCoverageForDesignatedUnmannedAircraftCG2450;
	}

	public void setLimitedCoverageForDesignatedUnmannedAircraftCG2450(boolean limitedCoverageForDesignatedUnmannedAircraftCG2450) {
		LimitedCoverageForDesignatedUnmannedAircraftCG2450 = limitedCoverageForDesignatedUnmannedAircraftCG2450;
	}

	public int getUnmannedAircraftCG2450_Limit() {
		return UnmannedAircraftCG2450_Limit;
	}

	public void setUnmannedAircraftCG2450_Limit(int unmannedAircraftCG2450_Limit) {
		UnmannedAircraftCG2450_Limit = unmannedAircraftCG2450_Limit;
	}

	public boolean isLiquorLiability_BringYourOwnAlcoholEstablishmentsCG2406() {
		return LiquorLiability_BringYourOwnAlcoholEstablishmentsCG2406;
	}

	public void setLiquorLiability_BringYourOwnAlcoholEstablishmentsCG2406(boolean liquorLiability_BringYourOwnAlcoholEstablishmentsCG2406) {
		LiquorLiability_BringYourOwnAlcoholEstablishmentsCG2406 = liquorLiability_BringYourOwnAlcoholEstablishmentsCG2406;
	}

	public boolean isLiquorLiabilityCoverageFormCG0033() {
		return LiquorLiabilityCoverageFormCG0033;
	}

	public void setLiquorLiabilityCoverageFormCG0033(boolean liquorLiabilityCoverageFormCG0033) {
		LiquorLiabilityCoverageFormCG0033 = liquorLiabilityCoverageFormCG0033;
	}

	public String getLiquorLiabilityCoverageFormCG0033_Description() {
		return LiquorLiabilityCoverageFormCG0033_Description;
	}

	public void setLiquorLiabilityCoverageFormCG0033_Description(String liquorLiabilityCoverageFormCG0033_Description) {
		LiquorLiabilityCoverageFormCG0033_Description = liquorLiabilityCoverageFormCG0033_Description;
	}

	public double getLiquorLiabilityCoverageFormCG0033_Sales() {
		return LiquorLiabilityCoverageFormCG0033_Sales;
	}

	public void setLiquorLiabilityCoverageFormCG0033_Sales(double liquorLiabilityCoverageFormCG0033_Sales) {
		LiquorLiabilityCoverageFormCG0033_Sales = liquorLiabilityCoverageFormCG0033_Sales;
	}

	public int getLiquorLiabilityCoverageFormCG0033_TastingRooms() {
		return LiquorLiabilityCoverageFormCG0033_TastingRooms;
	}

	public void setLiquorLiabilityCoverageFormCG0033_TastingRooms(int liquorLiabilityCoverageFormCG0033_TastingRooms) {
		LiquorLiabilityCoverageFormCG0033_TastingRooms = liquorLiabilityCoverageFormCG0033_TastingRooms;
	}

	public boolean isPrimaryAndNoncontributory_OtherInsuranceConditionCG2001() {
		return PrimaryAndNoncontributory_OtherInsuranceConditionCG2001;
	}

	public void setPrimaryAndNoncontributory_OtherInsuranceConditionCG2001(boolean primaryAndNoncontributory_OtherInsuranceConditionCG2001) {
		PrimaryAndNoncontributory_OtherInsuranceConditionCG2001 = primaryAndNoncontributory_OtherInsuranceConditionCG2001;
	}

	public class exclusionsAndConditions {

		// Exclusions
		public boolean AmendmentOfLiquorLiabilityExclusionCG2150 = false;
		public boolean CommercialGeneralLiabilityManuscriptEndorsementIDCG312012_Exclusion = false;
		public boolean CommunicableDiseaseExclusionCG2132 = false;
		public boolean EndorsementExcludingBodilyInjuryClaimByYourEmployeeAgainstAnAdditionalInsuredIDCG312008 = false;
		public boolean Exclusion_AdultDayCareCentersCG2287 = false;
		public boolean Exclusion_AllHazardsInConnectionWithDesignatedPremisesCG2100 = false;
		public boolean Exclusion_AthleticOrSportsParticipantsCG2101 = false;
		public boolean Exclusion_CampsOrCampgroundsCG2239 = false;
		public boolean Exclusion_ConstructionManagementErrorsAndOmissionsCG2234 = false;
		public boolean Exclusion_Contractors_ProfessionalLiabilityCG2279 = false;
		public boolean Exclusion_CorporalPunishmentCG2230 = false;
		public boolean Exclusion_CounselingServicesCG2157 = false;
		public boolean Exclusion_CoverageC_MedicalPaymentsCG2135 = false;
		public boolean Exclusion_DamageToWorkPerformedBySubcontractorsOnYourBehalfCG2294 = false;
		public boolean Exclusion_DescribedHazardsCarnivalsCircusesAndFairsCG2258 = false;
		public boolean Exclusion_DesignatedOngoingOperationsCG2153 = false;
		public boolean Exclusion_DesignatedProductsCG2133 = false;
		public boolean Exclusion_DesignatedProfessionalServicesCG2116 = false;
		public boolean Exclusion_DesignatedWorkCG2134 = false;
		public boolean Exclusion_DiagnosticTestingLaboratoriesCG2159 = false;
		public boolean Exclusion_EngineersArchitectsOrSurveyorsProfessionalLiabilityCG2243 = false;
		public boolean Exclusion_ErroneousDeliveryOrMixtureAndResultingFailureOfSeedToGerminate_SeedMerchantsCG2281 = false;
		public boolean Exclusion_FailureToSupplyCG2250 = false;
		public boolean Exclusion_FiduciaryOrRepresentativeLiabilityOfFinancialInstitutionsCG2238 = false;
		public boolean Exclusion_FinancialServicesCG2152 = false;
		public boolean Exclusion_FuneralServicesCG2156 = false;
		public boolean Exclusion_InspectionAppraisalAndSurveyCompaniesCG2224 = false;
		public boolean Exclusion_InsuranceAndRelatedOperationsCG2248 = false;
		public boolean Exclusion_IntercompanyProductsSuitsCG2141 = false;
		public boolean Exclusion_InternetServiceProvidersAndInternetAccessProvidersErrorsAndOmissionsCG2298 = false;
		public boolean Exclusion_LaundryAndDryCleaningDamageCG2253 = false;
		public boolean Exclusion_MedicalPaymentsToChildrenDayCareCentersCG2240 = false;
		public boolean Exclusion_NewEntitiesCG2136 = false;
		public boolean Exclusion_OilOrGasProducingOperationsCG2273 = false;
		public boolean Exclusion_PersonalAndAdvertisingInjuryCG2138 = false;
		public boolean Exclusion_ProductsAndProfessionalServicesDruggistsCG2236 = false;
		public boolean Exclusion_ProductsAndProfessionalServicesOpticalAndHearingAidEstablishmentsCG2237 = false;
		public boolean Exclusion_Products_CompletedOperationsHazardCG2104 = false;
		public boolean Exclusion_ProfessionalServices_BloodBanksCG2232 = false;
		public boolean Exclusion_ProfessionalVeterinarianServicesCG2158 = false;
		
		
		
		public boolean Exclusion_RealEstateAgentsOrBrokersErrorsOrOmissionsCG2301 = false;
		public boolean Exclusion_RollingStock_RailroadConstructionCG2246 = false;
		public boolean Exclusion_ServicesFurnishedByHealthCareProvidersCG2244 = false;
		public boolean Exclusion_SpecifiedTherapeuticOrCosmeticServicesCG2245 = false;
		public boolean Exclusion_TestingOrConsultingErrorsAndOmissionsCG2233 = false;
		public boolean Exclusion_UndergroundResourcesAndEquipmentCG2257 = false;
		public boolean Exclusion_VolunteerWorkersCG2166 = false;
		public boolean ExclusionOfCoverageForStructuresBuiltOutsideOfDesignatedAreasEndorsementIDCG312007 = false;
		public boolean FarmMachineryOperationsByContractorsExclusionEndorsementIDCG312009 = false;
		public boolean FertilizerDistributorsAndDealersExclusionEndorsementIDCG312010 = false;
		public boolean LimitationOfCoverage_RealEstateOperationsCG2260 = false;
		public boolean LimitedExclusion_PersonalAndAdvertisingInjury_LawyersCG2296 = false;
		public boolean MisdeliveryOfLiquidProductsCoverageCG2266 = false;
		public boolean ProfessionalLiabilityExclusion_ComputerDataProcessingCG2277 = false;
		public boolean ProfessionalLiabilityExclusion_ElectronicDataProcessingServicesAndComputerConsultingOrProgrammingServicesCG2288 = false;
		public boolean ProfessionalLiabilityExclusion_SpasOrPersonalEnhancementFacilitiesCG2290 = false;
		public boolean ProfessionalLiabilityExclusion_WebSiteDesignersCG2299 = false;
		public boolean RealEstatePropertyManagedCG2270 = false;
		public boolean TotalPollutionExclusionEndorsementCG2149 = false;
		public boolean TotalPollutionExclusionwithAHostileFireExceptionCG2155 = false;

		// Conditions
		public boolean Amendment_TravelAgencyToursLimitationOfCoverageCG2228 = false;
		public boolean BoatsCG2412 = false;
		public boolean CanoesOrRowboatsCG2416 = false;
		public boolean CollegesOrSchoolsLimitedFormCG2271 = false;
		public boolean CommercialGeneralLiabilityManuscriptEndorsementIDCG312012_Conditions = false;
		public boolean ExcessProvision_VendorsCG2410 = false;
		public boolean GovernmentalSubdivisionsCG2409 = false;
		public boolean LimitationOfCoverageToDesignatedPremisesOrProjectCG2144 = false;
		public boolean LimitationOfCoverageToInsuredPremisesCG2806 = false;
		public boolean OperationOfCustomersAutosOnParticularPremisesCG2268 = false;
		public boolean SnowPlowOperationsCoverageCG2292 = false;
		public boolean WaiverOfTransferOfRightsOfRecoveryAgainstOthersToUsCG2404 = false;

		// Getters and Setters
		/**
		 * @return the amendmentOfLiquorLiabilityExclusionCG2150
		 */
		public boolean isAmendmentOfLiquorLiabilityExclusionCG2150() {
			return AmendmentOfLiquorLiabilityExclusionCG2150;
		}

		/**
		 * @param amendmentOfLiquorLiabilityExclusionCG2150
		 *            the amendmentOfLiquorLiabilityExclusionCG2150 to set
		 */
		public void setAmendmentOfLiquorLiabilityExclusionCG2150(boolean amendmentOfLiquorLiabilityExclusionCG2150) {
			AmendmentOfLiquorLiabilityExclusionCG2150 = amendmentOfLiquorLiabilityExclusionCG2150;
		}

		/**
		 * @return the commercialGeneralLiabilityManuscriptEndorsementIDCG312012
		 */
		public boolean isCommercialGeneralLiabilityManuscriptEndorsementIDCG312012_Exclusion() {
			return CommercialGeneralLiabilityManuscriptEndorsementIDCG312012_Exclusion;
		}

		/**
		 * @param commercialGeneralLiabilityManuscriptEndorsementIDCG312012
		 *            the
		 *            commercialGeneralLiabilityManuscriptEndorsementIDCG312012
		 *            to set
		 */
		public void setCommercialGeneralLiabilityManuscriptEndorsementIDCG312012_Exclusion(
				boolean commercialGeneralLiabilityManuscriptEndorsementIDCG312012_Exclusion) {
			CommercialGeneralLiabilityManuscriptEndorsementIDCG312012_Exclusion = commercialGeneralLiabilityManuscriptEndorsementIDCG312012_Exclusion;
		}

		/**
		 * @return the communicableDiseaseExclusionCG2132
		 */
		public boolean isCommunicableDiseaseExclusionCG2132() {
			return CommunicableDiseaseExclusionCG2132;
		}

		/**
		 * @param communicableDiseaseExclusionCG2132
		 *            the communicableDiseaseExclusionCG2132 to set
		 */
		public void setCommunicableDiseaseExclusionCG2132(boolean communicableDiseaseExclusionCG2132) {
			CommunicableDiseaseExclusionCG2132 = communicableDiseaseExclusionCG2132;
		}

		/**
		 * @return the
		 *         endorsementExcludingBodilyInjuryClaimByYourEmployeeAgainstAnAdditionalInsuredIDCG312008
		 */
		public boolean isEndorsementExcludingBodilyInjuryClaimByYourEmployeeAgainstAnAdditionalInsuredIDCG312008() {
			return EndorsementExcludingBodilyInjuryClaimByYourEmployeeAgainstAnAdditionalInsuredIDCG312008;
		}

		/**
		 * @param endorsementExcludingBodilyInjuryClaimByYourEmployeeAgainstAnAdditionalInsuredIDCG312008
		 *            the
		 *            endorsementExcludingBodilyInjuryClaimByYourEmployeeAgainstAnAdditionalInsuredIDCG312008
		 *            to set
		 */
		public void setEndorsementExcludingBodilyInjuryClaimByYourEmployeeAgainstAnAdditionalInsuredIDCG312008(
				boolean endorsementExcludingBodilyInjuryClaimByYourEmployeeAgainstAnAdditionalInsuredIDCG312008) {
			EndorsementExcludingBodilyInjuryClaimByYourEmployeeAgainstAnAdditionalInsuredIDCG312008 = endorsementExcludingBodilyInjuryClaimByYourEmployeeAgainstAnAdditionalInsuredIDCG312008;
		}

		/**
		 * @return the exclusion_AdultDayCareCentersCG2287
		 */
		public boolean isExclusion_AdultDayCareCentersCG2287() {
			return Exclusion_AdultDayCareCentersCG2287;
		}

		/**
		 * @param exclusion_AdultDayCareCentersCG2287
		 *            the exclusion_AdultDayCareCentersCG2287 to set
		 */
		public void setExclusion_AdultDayCareCentersCG2287(boolean exclusion_AdultDayCareCentersCG2287) {
			Exclusion_AdultDayCareCentersCG2287 = exclusion_AdultDayCareCentersCG2287;
		}

		/**
		 * @return the
		 *         exclusion_AllHazardsInConnectionWithDesignatedPremisesCG2100
		 */
		public boolean isExclusion_AllHazardsInConnectionWithDesignatedPremisesCG2100() {
			return Exclusion_AllHazardsInConnectionWithDesignatedPremisesCG2100;
		}

		/**
		 * @param exclusion_AllHazardsInConnectionWithDesignatedPremisesCG2100
		 *            the
		 *            exclusion_AllHazardsInConnectionWithDesignatedPremisesCG2100
		 *            to set
		 */
		public void setExclusion_AllHazardsInConnectionWithDesignatedPremisesCG2100(
				boolean exclusion_AllHazardsInConnectionWithDesignatedPremisesCG2100) {
			Exclusion_AllHazardsInConnectionWithDesignatedPremisesCG2100 = exclusion_AllHazardsInConnectionWithDesignatedPremisesCG2100;
		}

		/**
		 * @return the exclusion_AthleticOrSportsParticipantsCG2101
		 */
		public boolean isExclusion_AthleticOrSportsParticipantsCG2101() {
			return Exclusion_AthleticOrSportsParticipantsCG2101;
		}

		/**
		 * @param exclusion_AthleticOrSportsParticipantsCG2101
		 *            the exclusion_AthleticOrSportsParticipantsCG2101 to set
		 */
		public void setExclusion_AthleticOrSportsParticipantsCG2101(
				boolean exclusion_AthleticOrSportsParticipantsCG2101) {
			Exclusion_AthleticOrSportsParticipantsCG2101 = exclusion_AthleticOrSportsParticipantsCG2101;
		}

		/**
		 * @return the exclusion_CampsOrCampgroundsCG2239
		 */
		public boolean isExclusion_CampsOrCampgroundsCG2239() {
			return Exclusion_CampsOrCampgroundsCG2239;
		}

		/**
		 * @param exclusion_CampsOrCampgroundsCG2239
		 *            the exclusion_CampsOrCampgroundsCG2239 to set
		 */
		public void setExclusion_CampsOrCampgroundsCG2239(boolean exclusion_CampsOrCampgroundsCG2239) {
			Exclusion_CampsOrCampgroundsCG2239 = exclusion_CampsOrCampgroundsCG2239;
		}

		/**
		 * @return the exclusion_ConstructionManagementErrorsAndOmissionsCG2234
		 */
		public boolean isExclusion_ConstructionManagementErrorsAndOmissionsCG2234() {
			return Exclusion_ConstructionManagementErrorsAndOmissionsCG2234;
		}

		/**
		 * @param exclusion_ConstructionManagementErrorsAndOmissionsCG2234
		 *            the
		 *            exclusion_ConstructionManagementErrorsAndOmissionsCG2234
		 *            to set
		 */
		public void setExclusion_ConstructionManagementErrorsAndOmissionsCG2234(
				boolean exclusion_ConstructionManagementErrorsAndOmissionsCG2234) {
			Exclusion_ConstructionManagementErrorsAndOmissionsCG2234 = exclusion_ConstructionManagementErrorsAndOmissionsCG2234;
		}

		/**
		 * @return the exclusion_Contractors_ProfessionalLiabilityCG2279
		 */
		public boolean isExclusion_Contractors_ProfessionalLiabilityCG2279() {
			return Exclusion_Contractors_ProfessionalLiabilityCG2279;
		}

		/**
		 * @param exclusion_Contractors_ProfessionalLiabilityCG2279
		 *            the exclusion_Contractors_ProfessionalLiabilityCG2279 to
		 *            set
		 */
		public void setExclusion_Contractors_ProfessionalLiabilityCG2279(
				boolean exclusion_Contractors_ProfessionalLiabilityCG2279) {
			Exclusion_Contractors_ProfessionalLiabilityCG2279 = exclusion_Contractors_ProfessionalLiabilityCG2279;
		}

		/**
		 * @return the exclusion_CorporalPunishmentCG2230
		 */
		public boolean isExclusion_CorporalPunishmentCG2230() {
			return Exclusion_CorporalPunishmentCG2230;
		}

		/**
		 * @param exclusion_CorporalPunishmentCG2230
		 *            the exclusion_CorporalPunishmentCG2230 to set
		 */
		public void setExclusion_CorporalPunishmentCG2230(boolean exclusion_CorporalPunishmentCG2230) {
			Exclusion_CorporalPunishmentCG2230 = exclusion_CorporalPunishmentCG2230;
		}

		/**
		 * @return the exclusion_CounselingServicesCG2157
		 */
		public boolean isExclusion_CounselingServicesCG2157() {
			return Exclusion_CounselingServicesCG2157;
		}

		/**
		 * @param exclusion_CounselingServicesCG2157
		 *            the exclusion_CounselingServicesCG2157 to set
		 */
		public void setExclusion_CounselingServicesCG2157(boolean exclusion_CounselingServicesCG2157) {
			Exclusion_CounselingServicesCG2157 = exclusion_CounselingServicesCG2157;
		}

		/**
		 * @return the exclusion_CoverageC_MedicalPaymentsCG2135
		 */
		public boolean isExclusion_CoverageC_MedicalPaymentsCG2135() {
			return Exclusion_CoverageC_MedicalPaymentsCG2135;
		}

		/**
		 * @param exclusion_CoverageC_MedicalPaymentsCG2135
		 *            the exclusion_CoverageC_MedicalPaymentsCG2135 to set
		 */
		public void setExclusion_CoverageC_MedicalPaymentsCG2135(boolean exclusion_CoverageC_MedicalPaymentsCG2135) {
			Exclusion_CoverageC_MedicalPaymentsCG2135 = exclusion_CoverageC_MedicalPaymentsCG2135;
		}

		/**
		 * @return the
		 *         exclusion_DamageToWorkPerformedBySubcontractorsOnYourBehalfCG2294
		 */
		public boolean isExclusion_DamageToWorkPerformedBySubcontractorsOnYourBehalfCG2294() {
			return Exclusion_DamageToWorkPerformedBySubcontractorsOnYourBehalfCG2294;
		}

		/**
		 * @param exclusion_DamageToWorkPerformedBySubcontractorsOnYourBehalfCG2294
		 *            the
		 *            exclusion_DamageToWorkPerformedBySubcontractorsOnYourBehalfCG2294
		 *            to set
		 */
		public void setExclusion_DamageToWorkPerformedBySubcontractorsOnYourBehalfCG2294(
				boolean exclusion_DamageToWorkPerformedBySubcontractorsOnYourBehalfCG2294) {
			Exclusion_DamageToWorkPerformedBySubcontractorsOnYourBehalfCG2294 = exclusion_DamageToWorkPerformedBySubcontractorsOnYourBehalfCG2294;
		}

		/**
		 * @return the exclusion_DescribedHazardsCarnivalsCircusesAndFairsCG2258
		 */
		public boolean isExclusion_DescribedHazardsCarnivalsCircusesAndFairsCG2258() {
			return Exclusion_DescribedHazardsCarnivalsCircusesAndFairsCG2258;
		}

		/**
		 * @param exclusion_DescribedHazardsCarnivalsCircusesAndFairsCG2258
		 *            the
		 *            exclusion_DescribedHazardsCarnivalsCircusesAndFairsCG2258
		 *            to set
		 */
		public void setExclusion_DescribedHazardsCarnivalsCircusesAndFairsCG2258(
				boolean exclusion_DescribedHazardsCarnivalsCircusesAndFairsCG2258) {
			Exclusion_DescribedHazardsCarnivalsCircusesAndFairsCG2258 = exclusion_DescribedHazardsCarnivalsCircusesAndFairsCG2258;
		}

		/**
		 * @return the exclusion_DesignatedOngoingOperationsCG2153
		 */
		public boolean isExclusion_DesignatedOngoingOperationsCG2153() {
			return Exclusion_DesignatedOngoingOperationsCG2153;
		}

		/**
		 * @param exclusion_DesignatedOngoingOperationsCG2153
		 *            the exclusion_DesignatedOngoingOperationsCG2153 to set
		 */
		public void setExclusion_DesignatedOngoingOperationsCG2153(
				boolean exclusion_DesignatedOngoingOperationsCG2153) {
			Exclusion_DesignatedOngoingOperationsCG2153 = exclusion_DesignatedOngoingOperationsCG2153;
		}

		/**
		 * @return the exclusion_DesignatedProductsCG2133
		 */
		public boolean isExclusion_DesignatedProductsCG2133() {
			return Exclusion_DesignatedProductsCG2133;
		}

		/**
		 * @param exclusion_DesignatedProductsCG2133
		 *            the exclusion_DesignatedProductsCG2133 to set
		 */
		public void setExclusion_DesignatedProductsCG2133(boolean exclusion_DesignatedProductsCG2133) {
			Exclusion_DesignatedProductsCG2133 = exclusion_DesignatedProductsCG2133;
		}

		/**
		 * @return the exclusion_DesignatedProfessionalServicesCG2116
		 */
		public boolean isExclusion_DesignatedProfessionalServicesCG2116() {
			return Exclusion_DesignatedProfessionalServicesCG2116;
		}

		/**
		 * @param exclusion_DesignatedProfessionalServicesCG2116
		 *            the exclusion_DesignatedProfessionalServicesCG2116 to set
		 */
		public void setExclusion_DesignatedProfessionalServicesCG2116(
				boolean exclusion_DesignatedProfessionalServicesCG2116) {
			Exclusion_DesignatedProfessionalServicesCG2116 = exclusion_DesignatedProfessionalServicesCG2116;
		}

		/**
		 * @return the exclusion_DesignatedWorkCG2134
		 */
		public boolean isExclusion_DesignatedWorkCG2134() {
			return Exclusion_DesignatedWorkCG2134;
		}

		/**
		 * @param exclusion_DesignatedWorkCG2134
		 *            the exclusion_DesignatedWorkCG2134 to set
		 */
		public void setExclusion_DesignatedWorkCG2134(boolean exclusion_DesignatedWorkCG2134) {
			Exclusion_DesignatedWorkCG2134 = exclusion_DesignatedWorkCG2134;
		}

		/**
		 * @return the exclusion_DiagnosticTestingLaboratoriesCG2159
		 */
		public boolean isExclusion_DiagnosticTestingLaboratoriesCG2159() {
			return Exclusion_DiagnosticTestingLaboratoriesCG2159;
		}

		/**
		 * @param exclusion_DiagnosticTestingLaboratoriesCG2159
		 *            the exclusion_DiagnosticTestingLaboratoriesCG2159 to set
		 */
		public void setExclusion_DiagnosticTestingLaboratoriesCG2159(
				boolean exclusion_DiagnosticTestingLaboratoriesCG2159) {
			Exclusion_DiagnosticTestingLaboratoriesCG2159 = exclusion_DiagnosticTestingLaboratoriesCG2159;
		}

		/**
		 * @return the
		 *         exclusion_EngineersArchitectsOrSurveyorsProfessionalLiabilityCG2243
		 */
		public boolean isExclusion_EngineersArchitectsOrSurveyorsProfessionalLiabilityCG2243() {
			return Exclusion_EngineersArchitectsOrSurveyorsProfessionalLiabilityCG2243;
		}

		/**
		 * @param exclusion_EngineersArchitectsOrSurveyorsProfessionalLiabilityCG2243
		 *            the
		 *            exclusion_EngineersArchitectsOrSurveyorsProfessionalLiabilityCG2243
		 *            to set
		 */
		public void setExclusion_EngineersArchitectsOrSurveyorsProfessionalLiabilityCG2243(
				boolean exclusion_EngineersArchitectsOrSurveyorsProfessionalLiabilityCG2243) {
			Exclusion_EngineersArchitectsOrSurveyorsProfessionalLiabilityCG2243 = exclusion_EngineersArchitectsOrSurveyorsProfessionalLiabilityCG2243;
		}

		/**
		 * @return the
		 *         exclusion_ErroneousDeliveryOrMixtureAndResultingFailureOfSeedToGerminate_SeedMerchantsCG2281
		 */
		public boolean isExclusion_ErroneousDeliveryOrMixtureAndResultingFailureOfSeedToGerminate_SeedMerchantsCG2281() {
			return Exclusion_ErroneousDeliveryOrMixtureAndResultingFailureOfSeedToGerminate_SeedMerchantsCG2281;
		}

		/**
		 * @param exclusion_ErroneousDeliveryOrMixtureAndResultingFailureOfSeedToGerminate_SeedMerchantsCG2281
		 *            the
		 *            exclusion_ErroneousDeliveryOrMixtureAndResultingFailureOfSeedToGerminate_SeedMerchantsCG2281
		 *            to set
		 */
		public void setExclusion_ErroneousDeliveryOrMixtureAndResultingFailureOfSeedToGerminate_SeedMerchantsCG2281(
				boolean exclusion_ErroneousDeliveryOrMixtureAndResultingFailureOfSeedToGerminate_SeedMerchantsCG2281) {
			Exclusion_ErroneousDeliveryOrMixtureAndResultingFailureOfSeedToGerminate_SeedMerchantsCG2281 = exclusion_ErroneousDeliveryOrMixtureAndResultingFailureOfSeedToGerminate_SeedMerchantsCG2281;
		}

		/**
		 * @return the exclusion_FailureToSupplyCG2250
		 */
		public boolean isExclusion_FailureToSupplyCG2250() {
			return Exclusion_FailureToSupplyCG2250;
		}

		/**
		 * @param exclusion_FailureToSupplyCG2250
		 *            the exclusion_FailureToSupplyCG2250 to set
		 */
		public void setExclusion_FailureToSupplyCG2250(boolean exclusion_FailureToSupplyCG2250) {
			Exclusion_FailureToSupplyCG2250 = exclusion_FailureToSupplyCG2250;
		}

		/**
		 * @return the
		 *         exclusion_FiduciaryOrRepresentativeLiabilityOfFinancialInstitutionsCG2238
		 */
		public boolean isExclusion_FiduciaryOrRepresentativeLiabilityOfFinancialInstitutionsCG2238() {
			return Exclusion_FiduciaryOrRepresentativeLiabilityOfFinancialInstitutionsCG2238;
		}

		/**
		 * @param exclusion_FiduciaryOrRepresentativeLiabilityOfFinancialInstitutionsCG2238
		 *            the
		 *            exclusion_FiduciaryOrRepresentativeLiabilityOfFinancialInstitutionsCG2238
		 *            to set
		 */
		public void setExclusion_FiduciaryOrRepresentativeLiabilityOfFinancialInstitutionsCG2238(
				boolean exclusion_FiduciaryOrRepresentativeLiabilityOfFinancialInstitutionsCG2238) {
			Exclusion_FiduciaryOrRepresentativeLiabilityOfFinancialInstitutionsCG2238 = exclusion_FiduciaryOrRepresentativeLiabilityOfFinancialInstitutionsCG2238;
		}

		/**
		 * @return the exclusion_FinancialServicesCG2152
		 */
		public boolean isExclusion_FinancialServicesCG2152() {
			return Exclusion_FinancialServicesCG2152;
		}

		/**
		 * @param exclusion_FinancialServicesCG2152
		 *            the exclusion_FinancialServicesCG2152 to set
		 */
		public void setExclusion_FinancialServicesCG2152(boolean exclusion_FinancialServicesCG2152) {
			Exclusion_FinancialServicesCG2152 = exclusion_FinancialServicesCG2152;
		}

		/**
		 * @return the exclusion_FuneralServicesCG2156
		 */
		public boolean isExclusion_FuneralServicesCG2156() {
			return Exclusion_FuneralServicesCG2156;
		}

		/**
		 * @param exclusion_FuneralServicesCG2156
		 *            the exclusion_FuneralServicesCG2156 to set
		 */
		public void setExclusion_FuneralServicesCG2156(boolean exclusion_FuneralServicesCG2156) {
			Exclusion_FuneralServicesCG2156 = exclusion_FuneralServicesCG2156;
		}

		/**
		 * @return the exclusion_InspectionAppraisalAndSurveyCompaniesCG2224
		 */
		public boolean isExclusion_InspectionAppraisalAndSurveyCompaniesCG2224() {
			return Exclusion_InspectionAppraisalAndSurveyCompaniesCG2224;
		}

		/**
		 * @param exclusion_InspectionAppraisalAndSurveyCompaniesCG2224
		 *            the exclusion_InspectionAppraisalAndSurveyCompaniesCG2224
		 *            to set
		 */
		public void setExclusion_InspectionAppraisalAndSurveyCompaniesCG2224(
				boolean exclusion_InspectionAppraisalAndSurveyCompaniesCG2224) {
			Exclusion_InspectionAppraisalAndSurveyCompaniesCG2224 = exclusion_InspectionAppraisalAndSurveyCompaniesCG2224;
		}

		/**
		 * @return the exclusion_InsuranceAndRelatedOperationsCG2248
		 */
		public boolean isExclusion_InsuranceAndRelatedOperationsCG2248() {
			return Exclusion_InsuranceAndRelatedOperationsCG2248;
		}

		/**
		 * @param exclusion_InsuranceAndRelatedOperationsCG2248
		 *            the exclusion_InsuranceAndRelatedOperationsCG2248 to set
		 */
		public void setExclusion_InsuranceAndRelatedOperationsCG2248(
				boolean exclusion_InsuranceAndRelatedOperationsCG2248) {
			Exclusion_InsuranceAndRelatedOperationsCG2248 = exclusion_InsuranceAndRelatedOperationsCG2248;
		}

		/**
		 * @return the exclusion_IntercompanyProductsSuitsCG2141
		 */
		public boolean isExclusion_IntercompanyProductsSuitsCG2141() {
			return Exclusion_IntercompanyProductsSuitsCG2141;
		}

		/**
		 * @param exclusion_IntercompanyProductsSuitsCG2141
		 *            the exclusion_IntercompanyProductsSuitsCG2141 to set
		 */
		public void setExclusion_IntercompanyProductsSuitsCG2141(boolean exclusion_IntercompanyProductsSuitsCG2141) {
			Exclusion_IntercompanyProductsSuitsCG2141 = exclusion_IntercompanyProductsSuitsCG2141;
		}

		/**
		 * @return the
		 *         exclusion_InternetServiceProvidersAndInternetAccessProvidersErrorsAndOmissionsCG2298
		 */
		public boolean isExclusion_InternetServiceProvidersAndInternetAccessProvidersErrorsAndOmissionsCG2298() {
			return Exclusion_InternetServiceProvidersAndInternetAccessProvidersErrorsAndOmissionsCG2298;
		}

		/**
		 * @param exclusion_InternetServiceProvidersAndInternetAccessProvidersErrorsAndOmissionsCG2298
		 *            the
		 *            exclusion_InternetServiceProvidersAndInternetAccessProvidersErrorsAndOmissionsCG2298
		 *            to set
		 */
		public void setExclusion_InternetServiceProvidersAndInternetAccessProvidersErrorsAndOmissionsCG2298(
				boolean exclusion_InternetServiceProvidersAndInternetAccessProvidersErrorsAndOmissionsCG2298) {
			Exclusion_InternetServiceProvidersAndInternetAccessProvidersErrorsAndOmissionsCG2298 = exclusion_InternetServiceProvidersAndInternetAccessProvidersErrorsAndOmissionsCG2298;
		}

		/**
		 * @return the exclusion_LaundryAndDryCleaningDamageCG2253
		 */
		public boolean isExclusion_LaundryAndDryCleaningDamageCG2253() {
			return Exclusion_LaundryAndDryCleaningDamageCG2253;
		}

		/**
		 * @param exclusion_LaundryAndDryCleaningDamageCG2253
		 *            the exclusion_LaundryAndDryCleaningDamageCG2253 to set
		 */
		public void setExclusion_LaundryAndDryCleaningDamageCG2253(
				boolean exclusion_LaundryAndDryCleaningDamageCG2253) {
			Exclusion_LaundryAndDryCleaningDamageCG2253 = exclusion_LaundryAndDryCleaningDamageCG2253;
		}

		/**
		 * @return the exclusion_MedicalPaymentsToChildrenDayCareCentersCG2240
		 */
		public boolean isExclusion_MedicalPaymentsToChildrenDayCareCentersCG2240() {
			return Exclusion_MedicalPaymentsToChildrenDayCareCentersCG2240;
		}

		/**
		 * @param exclusion_MedicalPaymentsToChildrenDayCareCentersCG2240
		 *            the
		 *            exclusion_MedicalPaymentsToChildrenDayCareCentersCG2240 to
		 *            set
		 */
		public void setExclusion_MedicalPaymentsToChildrenDayCareCentersCG2240(
				boolean exclusion_MedicalPaymentsToChildrenDayCareCentersCG2240) {
			Exclusion_MedicalPaymentsToChildrenDayCareCentersCG2240 = exclusion_MedicalPaymentsToChildrenDayCareCentersCG2240;
		}

		/**
		 * @return the exclusion_NewEntitiesCG2136
		 */
		public boolean isExclusion_NewEntitiesCG2136() {
			return Exclusion_NewEntitiesCG2136;
		}

		/**
		 * @param exclusion_NewEntitiesCG2136
		 *            the exclusion_NewEntitiesCG2136 to set
		 */
		public void setExclusion_NewEntitiesCG2136(boolean exclusion_NewEntitiesCG2136) {
			Exclusion_NewEntitiesCG2136 = exclusion_NewEntitiesCG2136;
		}

		/**
		 * @return the exclusion_OilOrGasProducingOperationsCG2273
		 */
		public boolean isExclusion_OilOrGasProducingOperationsCG2273() {
			return Exclusion_OilOrGasProducingOperationsCG2273;
		}

		/**
		 * @param exclusion_OilOrGasProducingOperationsCG2273
		 *            the exclusion_OilOrGasProducingOperationsCG2273 to set
		 */
		public void setExclusion_OilOrGasProducingOperationsCG2273(
				boolean exclusion_OilOrGasProducingOperationsCG2273) {
			Exclusion_OilOrGasProducingOperationsCG2273 = exclusion_OilOrGasProducingOperationsCG2273;
		}

		/**
		 * @return the exclusion_PersonalAndAdvertisingInjuryCG2138
		 */
		public boolean isExclusion_PersonalAndAdvertisingInjuryCG2138() {
			return Exclusion_PersonalAndAdvertisingInjuryCG2138;
		}

		/**
		 * @param exclusion_PersonalAndAdvertisingInjuryCG2138
		 *            the exclusion_PersonalAndAdvertisingInjuryCG2138 to set
		 */
		public void setExclusion_PersonalAndAdvertisingInjuryCG2138(
				boolean exclusion_PersonalAndAdvertisingInjuryCG2138) {
			Exclusion_PersonalAndAdvertisingInjuryCG2138 = exclusion_PersonalAndAdvertisingInjuryCG2138;
		}

		/**
		 * @return the exclusion_ProductsAndProfessionalServicesDruggistsCG2236
		 */
		public boolean isExclusion_ProductsAndProfessionalServicesDruggistsCG2236() {
			return Exclusion_ProductsAndProfessionalServicesDruggistsCG2236;
		}

		/**
		 * @param exclusion_ProductsAndProfessionalServicesDruggistsCG2236
		 *            the
		 *            exclusion_ProductsAndProfessionalServicesDruggistsCG2236
		 *            to set
		 */
		public void setExclusion_ProductsAndProfessionalServicesDruggistsCG2236(
				boolean exclusion_ProductsAndProfessionalServicesDruggistsCG2236) {
			Exclusion_ProductsAndProfessionalServicesDruggistsCG2236 = exclusion_ProductsAndProfessionalServicesDruggistsCG2236;
		}

		/**
		 * @return the
		 *         exclusion_ProductsAndProfessionalServicesOpticalAndHearingAidEstablishmentsCG2237
		 */
		public boolean isExclusion_ProductsAndProfessionalServicesOpticalAndHearingAidEstablishmentsCG2237() {
			return Exclusion_ProductsAndProfessionalServicesOpticalAndHearingAidEstablishmentsCG2237;
		}

		/**
		 * @param exclusion_ProductsAndProfessionalServicesOpticalAndHearingAidEstablishmentsCG2237
		 *            the
		 *            exclusion_ProductsAndProfessionalServicesOpticalAndHearingAidEstablishmentsCG2237
		 *            to set
		 */
		public void setExclusion_ProductsAndProfessionalServicesOpticalAndHearingAidEstablishmentsCG2237(
				boolean exclusion_ProductsAndProfessionalServicesOpticalAndHearingAidEstablishmentsCG2237) {
			Exclusion_ProductsAndProfessionalServicesOpticalAndHearingAidEstablishmentsCG2237 = exclusion_ProductsAndProfessionalServicesOpticalAndHearingAidEstablishmentsCG2237;
		}

		/**
		 * @return the exclusion_Products_CompletedOperationsHazardCG2104
		 */
		public boolean isExclusion_Products_CompletedOperationsHazardCG2104() {
			return Exclusion_Products_CompletedOperationsHazardCG2104;
		}

		/**
		 * @param exclusion_Products_CompletedOperationsHazardCG2104
		 *            the exclusion_Products_CompletedOperationsHazardCG2104 to
		 *            set
		 */
		public void setExclusion_Products_CompletedOperationsHazardCG2104(
				boolean exclusion_Products_CompletedOperationsHazardCG2104) {
			Exclusion_Products_CompletedOperationsHazardCG2104 = exclusion_Products_CompletedOperationsHazardCG2104;
		}

		/**
		 * @return the exclusion_ProfessionalServices_BloodBanksCG2232
		 */
		public boolean isExclusion_ProfessionalServices_BloodBanksCG2232() {
			return Exclusion_ProfessionalServices_BloodBanksCG2232;
		}

		/**
		 * @param exclusion_ProfessionalServices_BloodBanksCG2232
		 *            the exclusion_ProfessionalServices_BloodBanksCG2232 to set
		 */
		public void setExclusion_ProfessionalServices_BloodBanksCG2232(
				boolean exclusion_ProfessionalServices_BloodBanksCG2232) {
			Exclusion_ProfessionalServices_BloodBanksCG2232 = exclusion_ProfessionalServices_BloodBanksCG2232;
		}

		/**
		 * @return the exclusion_ProfessionalVeterinarianServicesCG2158
		 */
		public boolean isExclusion_ProfessionalVeterinarianServicesCG2158() {
			return Exclusion_ProfessionalVeterinarianServicesCG2158;
		}

		/**
		 * @param exclusion_ProfessionalVeterinarianServicesCG2158
		 *            the exclusion_ProfessionalVeterinarianServicesCG2158 to
		 *            set
		 */
		public void setExclusion_ProfessionalVeterinarianServicesCG2158(
				boolean exclusion_ProfessionalVeterinarianServicesCG2158) {
			Exclusion_ProfessionalVeterinarianServicesCG2158 = exclusion_ProfessionalVeterinarianServicesCG2158;
		}

		/**
		 * @return the
		 *         exclusion_RealEstateAgentsOrBrokersErrorsOrOmissionsCG2301
		 */
		public boolean isExclusion_RealEstateAgentsOrBrokersErrorsOrOmissionsCG2301() {
			return Exclusion_RealEstateAgentsOrBrokersErrorsOrOmissionsCG2301;
		}

		/**
		 * @param exclusion_RealEstateAgentsOrBrokersErrorsOrOmissionsCG2301
		 *            the
		 *            exclusion_RealEstateAgentsOrBrokersErrorsOrOmissionsCG2301
		 *            to set
		 */
		public void setExclusion_RealEstateAgentsOrBrokersErrorsOrOmissionsCG2301(
				boolean exclusion_RealEstateAgentsOrBrokersErrorsOrOmissionsCG2301) {
			Exclusion_RealEstateAgentsOrBrokersErrorsOrOmissionsCG2301 = exclusion_RealEstateAgentsOrBrokersErrorsOrOmissionsCG2301;
		}

		/**
		 * @return the exclusion_RollingStock_RailroadConstructionCG2246
		 */
		public boolean isExclusion_RollingStock_RailroadConstructionCG2246() {
			return Exclusion_RollingStock_RailroadConstructionCG2246;
		}

		/**
		 * @param exclusion_RollingStock_RailroadConstructionCG2246
		 *            the exclusion_RollingStock_RailroadConstructionCG2246 to
		 *            set
		 */
		public void setExclusion_RollingStock_RailroadConstructionCG2246(
				boolean exclusion_RollingStock_RailroadConstructionCG2246) {
			Exclusion_RollingStock_RailroadConstructionCG2246 = exclusion_RollingStock_RailroadConstructionCG2246;
		}

		/**
		 * @return the exclusion_ServicesFurnishedByHealthCareProvidersCG2244
		 */
		public boolean isExclusion_ServicesFurnishedByHealthCareProvidersCG2244() {
			return Exclusion_ServicesFurnishedByHealthCareProvidersCG2244;
		}

		/**
		 * @param exclusion_ServicesFurnishedByHealthCareProvidersCG2244
		 *            the exclusion_ServicesFurnishedByHealthCareProvidersCG2244
		 *            to set
		 */
		public void setExclusion_ServicesFurnishedByHealthCareProvidersCG2244(
				boolean exclusion_ServicesFurnishedByHealthCareProvidersCG2244) {
			Exclusion_ServicesFurnishedByHealthCareProvidersCG2244 = exclusion_ServicesFurnishedByHealthCareProvidersCG2244;
		}

		/**
		 * @return the exclusion_SpecifiedTherapeuticOrCosmeticServicesCG2245
		 */
		public boolean isExclusion_SpecifiedTherapeuticOrCosmeticServicesCG2245() {
			return Exclusion_SpecifiedTherapeuticOrCosmeticServicesCG2245;
		}

		/**
		 * @param exclusion_SpecifiedTherapeuticOrCosmeticServicesCG2245
		 *            the exclusion_SpecifiedTherapeuticOrCosmeticServicesCG2245
		 *            to set
		 */
		public void setExclusion_SpecifiedTherapeuticOrCosmeticServicesCG2245(
				boolean exclusion_SpecifiedTherapeuticOrCosmeticServicesCG2245) {
			Exclusion_SpecifiedTherapeuticOrCosmeticServicesCG2245 = exclusion_SpecifiedTherapeuticOrCosmeticServicesCG2245;
		}

		/**
		 * @return the exclusion_TestingOrConsultingErrorsAndOmissionsCG2233
		 */
		public boolean isExclusion_TestingOrConsultingErrorsAndOmissionsCG2233() {
			return Exclusion_TestingOrConsultingErrorsAndOmissionsCG2233;
		}

		/**
		 * @param exclusion_TestingOrConsultingErrorsAndOmissionsCG2233
		 *            the exclusion_TestingOrConsultingErrorsAndOmissionsCG2233
		 *            to set
		 */
		public void setExclusion_TestingOrConsultingErrorsAndOmissionsCG2233(
				boolean exclusion_TestingOrConsultingErrorsAndOmissionsCG2233) {
			Exclusion_TestingOrConsultingErrorsAndOmissionsCG2233 = exclusion_TestingOrConsultingErrorsAndOmissionsCG2233;
		}

		/**
		 * @return the exclusion_UndergroundResourcesAndEquipmentCG2257
		 */
		public boolean isExclusion_UndergroundResourcesAndEquipmentCG2257() {
			return Exclusion_UndergroundResourcesAndEquipmentCG2257;
		}

		/**
		 * @param exclusion_UndergroundResourcesAndEquipmentCG2257
		 *            the exclusion_UndergroundResourcesAndEquipmentCG2257 to
		 *            set
		 */
		public void setExclusion_UndergroundResourcesAndEquipmentCG2257(
				boolean exclusion_UndergroundResourcesAndEquipmentCG2257) {
			Exclusion_UndergroundResourcesAndEquipmentCG2257 = exclusion_UndergroundResourcesAndEquipmentCG2257;
		}

		/**
		 * @return the exclusion_VolunteerWorkersCG2166
		 */
		public boolean isExclusion_VolunteerWorkersCG2166() {
			return Exclusion_VolunteerWorkersCG2166;
		}

		/**
		 * @param exclusion_VolunteerWorkersCG2166
		 *            the exclusion_VolunteerWorkersCG2166 to set
		 */
		public void setExclusion_VolunteerWorkersCG2166(boolean exclusion_VolunteerWorkersCG2166) {
			Exclusion_VolunteerWorkersCG2166 = exclusion_VolunteerWorkersCG2166;
		}

		/**
		 * @return the
		 *         exclusionOfCoverageForStructuresBuiltOutsideOfDesignatedAreasEndorsementIDCG312007
		 */
		public boolean isExclusionOfCoverageForStructuresBuiltOutsideOfDesignatedAreasEndorsementIDCG312007() {
			return ExclusionOfCoverageForStructuresBuiltOutsideOfDesignatedAreasEndorsementIDCG312007;
		}

		/**
		 * @param exclusionOfCoverageForStructuresBuiltOutsideOfDesignatedAreasEndorsementIDCG312007
		 *            the
		 *            exclusionOfCoverageForStructuresBuiltOutsideOfDesignatedAreasEndorsementIDCG312007
		 *            to set
		 */
		public void setExclusionOfCoverageForStructuresBuiltOutsideOfDesignatedAreasEndorsementIDCG312007(
				boolean exclusionOfCoverageForStructuresBuiltOutsideOfDesignatedAreasEndorsementIDCG312007) {
			ExclusionOfCoverageForStructuresBuiltOutsideOfDesignatedAreasEndorsementIDCG312007 = exclusionOfCoverageForStructuresBuiltOutsideOfDesignatedAreasEndorsementIDCG312007;
		}

		/**
		 * @return the
		 *         farmMachineryOperationsByContractorsExclusionEndorsementIDCG312009
		 */
		public boolean isFarmMachineryOperationsByContractorsExclusionEndorsementIDCG312009() {
			return FarmMachineryOperationsByContractorsExclusionEndorsementIDCG312009;
		}

		/**
		 * @param farmMachineryOperationsByContractorsExclusionEndorsementIDCG312009
		 *            the
		 *            farmMachineryOperationsByContractorsExclusionEndorsementIDCG312009
		 *            to set
		 */
		public void setFarmMachineryOperationsByContractorsExclusionEndorsementIDCG312009(
				boolean farmMachineryOperationsByContractorsExclusionEndorsementIDCG312009) {
			FarmMachineryOperationsByContractorsExclusionEndorsementIDCG312009 = farmMachineryOperationsByContractorsExclusionEndorsementIDCG312009;
		}

		/**
		 * @return the
		 *         fertilizerDistributorsAndDealersExclusionEndorsementIDCG312010
		 */
		public boolean isFertilizerDistributorsAndDealersExclusionEndorsementIDCG312010() {
			return FertilizerDistributorsAndDealersExclusionEndorsementIDCG312010;
		}

		/**
		 * @param fertilizerDistributorsAndDealersExclusionEndorsementIDCG312010
		 *            the
		 *            fertilizerDistributorsAndDealersExclusionEndorsementIDCG312010
		 *            to set
		 */
		public void setFertilizerDistributorsAndDealersExclusionEndorsementIDCG312010(
				boolean fertilizerDistributorsAndDealersExclusionEndorsementIDCG312010) {
			FertilizerDistributorsAndDealersExclusionEndorsementIDCG312010 = fertilizerDistributorsAndDealersExclusionEndorsementIDCG312010;
		}

		/**
		 * @return the limitationOfCoverage_RealEstateOperationsCG2260
		 */
		public boolean isLimitationOfCoverage_RealEstateOperationsCG2260() {
			return LimitationOfCoverage_RealEstateOperationsCG2260;
		}

		/**
		 * @param limitationOfCoverage_RealEstateOperationsCG2260
		 *            the limitationOfCoverage_RealEstateOperationsCG2260 to set
		 */
		public void setLimitationOfCoverage_RealEstateOperationsCG2260(
				boolean limitationOfCoverage_RealEstateOperationsCG2260) {
			LimitationOfCoverage_RealEstateOperationsCG2260 = limitationOfCoverage_RealEstateOperationsCG2260;
		}

		/**
		 * @return the
		 *         limitedExclusion_PersonalAndAdvertisingInjury_LawyersCG2296
		 */
		public boolean isLimitedExclusion_PersonalAndAdvertisingInjury_LawyersCG2296() {
			return LimitedExclusion_PersonalAndAdvertisingInjury_LawyersCG2296;
		}

		/**
		 * @param limitedExclusion_PersonalAndAdvertisingInjury_LawyersCG2296
		 *            the
		 *            limitedExclusion_PersonalAndAdvertisingInjury_LawyersCG2296
		 *            to set
		 */
		public void setLimitedExclusion_PersonalAndAdvertisingInjury_LawyersCG2296(
				boolean limitedExclusion_PersonalAndAdvertisingInjury_LawyersCG2296) {
			LimitedExclusion_PersonalAndAdvertisingInjury_LawyersCG2296 = limitedExclusion_PersonalAndAdvertisingInjury_LawyersCG2296;
		}

		/**
		 * @return the misdeliveryOfLiquidProductsCoverageCG2266
		 */
		public boolean isMisdeliveryOfLiquidProductsCoverageCG2266() {
			return MisdeliveryOfLiquidProductsCoverageCG2266;
		}

		/**
		 * @param misdeliveryOfLiquidProductsCoverageCG2266
		 *            the misdeliveryOfLiquidProductsCoverageCG2266 to set
		 */
		public void setMisdeliveryOfLiquidProductsCoverageCG2266(boolean misdeliveryOfLiquidProductsCoverageCG2266) {
			MisdeliveryOfLiquidProductsCoverageCG2266 = misdeliveryOfLiquidProductsCoverageCG2266;
		}

		/**
		 * @return the
		 *         professionalLiabilityExclusion_ComputerDataProcessingCG2277
		 */
		public boolean isProfessionalLiabilityExclusion_ComputerDataProcessingCG2277() {
			return ProfessionalLiabilityExclusion_ComputerDataProcessingCG2277;
		}

		/**
		 * @param professionalLiabilityExclusion_ComputerDataProcessingCG2277
		 *            the
		 *            professionalLiabilityExclusion_ComputerDataProcessingCG2277
		 *            to set
		 */
		public void setProfessionalLiabilityExclusion_ComputerDataProcessingCG2277(
				boolean professionalLiabilityExclusion_ComputerDataProcessingCG2277) {
			ProfessionalLiabilityExclusion_ComputerDataProcessingCG2277 = professionalLiabilityExclusion_ComputerDataProcessingCG2277;
		}

		/**
		 * @return the
		 *         professionalLiabilityExclusion_ElectronicDataProcessingServicesAndComputerConsultingOrProgrammingServicesCG2288
		 */
		public boolean isProfessionalLiabilityExclusion_ElectronicDataProcessingServicesAndComputerConsultingOrProgrammingServicesCG2288() {
			return ProfessionalLiabilityExclusion_ElectronicDataProcessingServicesAndComputerConsultingOrProgrammingServicesCG2288;
		}

		/**
		 * @param professionalLiabilityExclusion_ElectronicDataProcessingServicesAndComputerConsultingOrProgrammingServicesCG2288
		 *            the
		 *            professionalLiabilityExclusion_ElectronicDataProcessingServicesAndComputerConsultingOrProgrammingServicesCG2288
		 *            to set
		 */
		public void setProfessionalLiabilityExclusion_ElectronicDataProcessingServicesAndComputerConsultingOrProgrammingServicesCG2288(
				boolean professionalLiabilityExclusion_ElectronicDataProcessingServicesAndComputerConsultingOrProgrammingServicesCG2288) {
			ProfessionalLiabilityExclusion_ElectronicDataProcessingServicesAndComputerConsultingOrProgrammingServicesCG2288 = professionalLiabilityExclusion_ElectronicDataProcessingServicesAndComputerConsultingOrProgrammingServicesCG2288;
		}

		/**
		 * @return the
		 *         professionalLiabilityExclusion_SpasOrPersonalEnhancementFacilitiesCG2290
		 */
		public boolean isProfessionalLiabilityExclusion_SpasOrPersonalEnhancementFacilitiesCG2290() {
			return ProfessionalLiabilityExclusion_SpasOrPersonalEnhancementFacilitiesCG2290;
		}

		/**
		 * @param professionalLiabilityExclusion_SpasOrPersonalEnhancementFacilitiesCG2290
		 *            the
		 *            professionalLiabilityExclusion_SpasOrPersonalEnhancementFacilitiesCG2290
		 *            to set
		 */
		public void setProfessionalLiabilityExclusion_SpasOrPersonalEnhancementFacilitiesCG2290(
				boolean professionalLiabilityExclusion_SpasOrPersonalEnhancementFacilitiesCG2290) {
			ProfessionalLiabilityExclusion_SpasOrPersonalEnhancementFacilitiesCG2290 = professionalLiabilityExclusion_SpasOrPersonalEnhancementFacilitiesCG2290;
		}

		/**
		 * @return the professionalLiabilityExclusion_WebSiteDesignersCG2299
		 */
		public boolean isProfessionalLiabilityExclusion_WebSiteDesignersCG2299() {
			return ProfessionalLiabilityExclusion_WebSiteDesignersCG2299;
		}

		/**
		 * @param professionalLiabilityExclusion_WebSiteDesignersCG2299
		 *            the professionalLiabilityExclusion_WebSiteDesignersCG2299
		 *            to set
		 */
		public void setProfessionalLiabilityExclusion_WebSiteDesignersCG2299(
				boolean professionalLiabilityExclusion_WebSiteDesignersCG2299) {
			ProfessionalLiabilityExclusion_WebSiteDesignersCG2299 = professionalLiabilityExclusion_WebSiteDesignersCG2299;
		}

		/**
		 * @return the realEstatePropertyManagedCG2270
		 */
		public boolean isRealEstatePropertyManagedCG2270() {
			return RealEstatePropertyManagedCG2270;
		}

		/**
		 * @param realEstatePropertyManagedCG2270
		 *            the realEstatePropertyManagedCG2270 to set
		 */
		public void setRealEstatePropertyManagedCG2270(boolean realEstatePropertyManagedCG2270) {
			RealEstatePropertyManagedCG2270 = realEstatePropertyManagedCG2270;
		}

		/**
		 * @return the totalPollutionExclusionEndorsementCG2149
		 */
		public boolean isTotalPollutionExclusionEndorsementCG2149() {
			return TotalPollutionExclusionEndorsementCG2149;
		}

		/**
		 * @param totalPollutionExclusionEndorsementCG2149
		 *            the totalPollutionExclusionEndorsementCG2149 to set
		 */
		public void setTotalPollutionExclusionEndorsementCG2149(boolean totalPollutionExclusionEndorsementCG2149) {
			TotalPollutionExclusionEndorsementCG2149 = totalPollutionExclusionEndorsementCG2149;
		}

		/**
		 * @return the totalPollutionExclusionwithAHostileFireExceptionCG2155
		 */
		public boolean isTotalPollutionExclusionwithAHostileFireExceptionCG2155() {
			return TotalPollutionExclusionwithAHostileFireExceptionCG2155;
		}

		/**
		 * @param totalPollutionExclusionwithAHostileFireExceptionCG2155
		 *            the totalPollutionExclusionwithAHostileFireExceptionCG2155
		 *            to set
		 */
		public void setTotalPollutionExclusionwithAHostileFireExceptionCG2155(
				boolean totalPollutionExclusionwithAHostileFireExceptionCG2155) {
			TotalPollutionExclusionwithAHostileFireExceptionCG2155 = totalPollutionExclusionwithAHostileFireExceptionCG2155;
		}

		/**
		 * @return the amendment_TravelAgencyToursLimitationOfCoverageCG2228
		 */
		public boolean isAmendment_TravelAgencyToursLimitationOfCoverageCG2228() {
			return Amendment_TravelAgencyToursLimitationOfCoverageCG2228;
		}

		/**
		 * @param amendment_TravelAgencyToursLimitationOfCoverageCG2228
		 *            the amendment_TravelAgencyToursLimitationOfCoverageCG2228
		 *            to set
		 */
		public void setAmendment_TravelAgencyToursLimitationOfCoverageCG2228(
				boolean amendment_TravelAgencyToursLimitationOfCoverageCG2228) {
			Amendment_TravelAgencyToursLimitationOfCoverageCG2228 = amendment_TravelAgencyToursLimitationOfCoverageCG2228;
		}

		/**
		 * @return the boatsCG2412
		 */
		public boolean isBoatsCG2412() {
			return BoatsCG2412;
		}

		/**
		 * @param boatsCG2412
		 *            the boatsCG2412 to set
		 */
		public void setBoatsCG2412(boolean boatsCG2412) {
			BoatsCG2412 = boatsCG2412;
		}

		/**
		 * @return the canoesOrRowboatsCG2416
		 */
		public boolean isCanoesOrRowboatsCG2416() {
			return CanoesOrRowboatsCG2416;
		}

		/**
		 * @param canoesOrRowboatsCG2416
		 *            the canoesOrRowboatsCG2416 to set
		 */
		public void setCanoesOrRowboatsCG2416(boolean canoesOrRowboatsCG2416) {
			CanoesOrRowboatsCG2416 = canoesOrRowboatsCG2416;
		}

		/**
		 * @return the collegesOrSchoolsLimitedFormCG2271
		 */
		public boolean isCollegesOrSchoolsLimitedFormCG2271() {
			return CollegesOrSchoolsLimitedFormCG2271;
		}

		/**
		 * @param collegesOrSchoolsLimitedFormCG2271
		 *            the collegesOrSchoolsLimitedFormCG2271 to set
		 */
		public void setCollegesOrSchoolsLimitedFormCG2271(boolean collegesOrSchoolsLimitedFormCG2271) {
			CollegesOrSchoolsLimitedFormCG2271 = collegesOrSchoolsLimitedFormCG2271;
		}

		/**
		 * @return the commercialGeneralLiabilityManuscriptEndorsementIDCG312012
		 */
		public boolean isCommercialGeneralLiabilityManuscriptEndorsementIDCG312012_Conditions() {
			return CommercialGeneralLiabilityManuscriptEndorsementIDCG312012_Conditions;
		}

		/**
		 * @param commercialGeneralLiabilityManuscriptEndorsementIDCG312012
		 *            the
		 *            commercialGeneralLiabilityManuscriptEndorsementIDCG312012
		 *            to set
		 */
		public void setCommercialGeneralLiabilityManuscriptEndorsementIDCG312012_Conditions(
				boolean commercialGeneralLiabilityManuscriptEndorsementIDCG312012_Conditions) {
			CommercialGeneralLiabilityManuscriptEndorsementIDCG312012_Conditions = commercialGeneralLiabilityManuscriptEndorsementIDCG312012_Conditions;
		}

		/**
		 * @return the excessProvision_VendorsCG2410
		 */
		public boolean isExcessProvision_VendorsCG2410() {
			return ExcessProvision_VendorsCG2410;
		}

		/**
		 * @param excessProvision_VendorsCG2410
		 *            the excessProvision_VendorsCG2410 to set
		 */
		public void setExcessProvision_VendorsCG2410(boolean excessProvision_VendorsCG2410) {
			ExcessProvision_VendorsCG2410 = excessProvision_VendorsCG2410;
		}

		/**
		 * @return the governmentalSubdivisionsCG2409
		 */
		public boolean isGovernmentalSubdivisionsCG2409() {
			return GovernmentalSubdivisionsCG2409;
		}

		/**
		 * @param governmentalSubdivisionsCG2409
		 *            the governmentalSubdivisionsCG2409 to set
		 */
		public void setGovernmentalSubdivisionsCG2409(boolean governmentalSubdivisionsCG2409) {
			GovernmentalSubdivisionsCG2409 = governmentalSubdivisionsCG2409;
		}

		/**
		 * @return the limitationOfCoverageToDesignatedPremisesOrProjectCG2144
		 */
		public boolean isLimitationOfCoverageToDesignatedPremisesOrProjectCG2144() {
			return LimitationOfCoverageToDesignatedPremisesOrProjectCG2144;
		}

		/**
		 * @param limitationOfCoverageToDesignatedPremisesOrProjectCG2144
		 *            the
		 *            limitationOfCoverageToDesignatedPremisesOrProjectCG2144 to
		 *            set
		 */
		public void setLimitationOfCoverageToDesignatedPremisesOrProjectCG2144(
				boolean limitationOfCoverageToDesignatedPremisesOrProjectCG2144) {
			LimitationOfCoverageToDesignatedPremisesOrProjectCG2144 = limitationOfCoverageToDesignatedPremisesOrProjectCG2144;
		}

		/**
		 * @return the limitationOfCoverageToInsuredPremisesCG2806
		 */
		public boolean isLimitationOfCoverageToInsuredPremisesCG2806() {
			return LimitationOfCoverageToInsuredPremisesCG2806;
		}

		/**
		 * @param limitationOfCoverageToInsuredPremisesCG2806
		 *            the limitationOfCoverageToInsuredPremisesCG2806 to set
		 */
		public void setLimitationOfCoverageToInsuredPremisesCG2806(
				boolean limitationOfCoverageToInsuredPremisesCG2806) {
			LimitationOfCoverageToInsuredPremisesCG2806 = limitationOfCoverageToInsuredPremisesCG2806;
		}

		/**
		 * @return the operationOfCustomersAutosOnParticularPremisesCG2268
		 */
		public boolean isOperationOfCustomersAutosOnParticularPremisesCG2268() {
			return OperationOfCustomersAutosOnParticularPremisesCG2268;
		}

		/**
		 * @param operationOfCustomersAutosOnParticularPremisesCG2268
		 *            the operationOfCustomersAutosOnParticularPremisesCG2268 to
		 *            set
		 */
		public void setOperationOfCustomersAutosOnParticularPremisesCG2268(
				boolean operationOfCustomersAutosOnParticularPremisesCG2268) {
			OperationOfCustomersAutosOnParticularPremisesCG2268 = operationOfCustomersAutosOnParticularPremisesCG2268;
		}

		/**
		 * @return the snowPlowOperationsCoverageCG2292
		 */
		public boolean isSnowPlowOperationsCoverageCG2292() {
			return SnowPlowOperationsCoverageCG2292;
		}

		/**
		 * @param snowPlowOperationsCoverageCG2292
		 *            the snowPlowOperationsCoverageCG2292 to set
		 */
		public void setSnowPlowOperationsCoverageCG2292(boolean snowPlowOperationsCoverageCG2292) {
			SnowPlowOperationsCoverageCG2292 = snowPlowOperationsCoverageCG2292;
		}

		/**
		 * @return the waiverOfTransferOfRightsOfRecoveryAgainstOthersToUsCG2404
		 */
		public boolean isWaiverOfTransferOfRightsOfRecoveryAgainstOthersToUsCG2404() {
			return WaiverOfTransferOfRightsOfRecoveryAgainstOthersToUsCG2404;
		}

		/**
		 * @param waiverOfTransferOfRightsOfRecoveryAgainstOthersToUsCG2404
		 *            the
		 *            waiverOfTransferOfRightsOfRecoveryAgainstOthersToUsCG2404
		 *            to set
		 */
		public void setWaiverOfTransferOfRightsOfRecoveryAgainstOthersToUsCG2404(
				boolean waiverOfTransferOfRightsOfRecoveryAgainstOthersToUsCG2404) {
			WaiverOfTransferOfRightsOfRecoveryAgainstOthersToUsCG2404 = waiverOfTransferOfRightsOfRecoveryAgainstOthersToUsCG2404;
		}

	}

	public GeneralLiability.StandardCoverages.CG0001_OccuranceLimit getGeneralLiabilityOccuranceLimitCG_00_01() {
		return generalLiabilityOccuranceLimitCG_00_01;
	}

	public void setGeneralLiabilityOccuranceLimitCG_00_01(GeneralLiability.StandardCoverages.CG0001_OccuranceLimit generalLiabilityOccuranceLimitCG_00_01) {
		this.generalLiabilityOccuranceLimitCG_00_01 = generalLiabilityOccuranceLimitCG_00_01;
	}

	public GeneralLiability.StandardCoverages.CG0001_MedicalPaymentsPerPerson getGeneralLiablilityMedicalPayments() {
		return generalLiablilityMedicalPayments;
	}

	public void setGeneralLiablilityMedicalPayments(GeneralLiability.StandardCoverages.CG0001_MedicalPaymentsPerPerson generalLiablilityMedicalPayments) {
		this.generalLiablilityMedicalPayments = generalLiablilityMedicalPayments;
	}

	public GeneralLiability.StandardCoverages.CG0001_PersonalAdvertisingInjury getGeneralLiabilityPersonalAdvertisingInjuryLimit() {
		return generalLiabilityPersonalAdvertisingInjuryLimit;
	}

	public void setGeneralLiabilityPersonalAdvertisingInjuryLimit(GeneralLiability.StandardCoverages.CG0001_PersonalAdvertisingInjury generalLiabilityPersonalAdvertisingInjuryLimit) {
		this.generalLiabilityPersonalAdvertisingInjuryLimit = generalLiabilityPersonalAdvertisingInjuryLimit;
	}

	public GeneralLiability.StandardCoverages.CG0001_OperationsAggregateLimit getOperationsAggregateLimit() {
		return operationsAggregateLimit;
	}

	public void setOperationsAggregateLimit(GeneralLiability.StandardCoverages.CG0001_OperationsAggregateLimit operationsAggregateLimit) {
		this.operationsAggregateLimit = operationsAggregateLimit;
	}

	public GeneralLiability.StandardCoverages.CG0001_DeductibleLiabilityInsCG0300 getDeductibleLiabilityIns_CG_03_00() {
		return deductibleLiabilityIns_CG_03_00;
	}

	public void setDeductibleLiabilityIns_CG_03_00(GeneralLiability.StandardCoverages.CG0001_DeductibleLiabilityInsCG0300 deductibleLiabilityIns_CG_03_00) {
		this.deductibleLiabilityIns_CG_03_00 = deductibleLiabilityIns_CG_03_00;
	}






	public List<CPPGLCoveragesAdditionalInsureds> getAdditionalInsuredslist() {
		return additionalInsuredslist;
	}






	public void setAdditionalInsuredslist(List<CPPGLCoveragesAdditionalInsureds> additionalInsuredslist) {
		this.additionalInsuredslist = additionalInsuredslist;
	}
	
	public repository.gw.generate.custom.PolicyLocation getLocation() {
		return location;
	}

	public void setLocation(PolicyLocation location) {
		this.location = location;
	}






	public boolean isExcessProvisionVendors_CG_24_10() {
		return ExcessProvisionVendors_CG_24_10;
	}






	public void setExcessProvisionVendors_CG_24_10(boolean excessProvisionVendors_CG_24_10) {
		ExcessProvisionVendors_CG_24_10 = excessProvisionVendors_CG_24_10;
	}






	public boolean isLimitationOfCoverageToDesignatedPremisesOrProject_CG_21_44() {
		return LimitationOfCoverageToDesignatedPremisesOrProject_CG_21_44;
	}






	public void setLimitationOfCoverageToDesignatedPremisesOrProject_CG_21_44(boolean limitationOfCoverageToDesignatedPremisesOrProject_CG_21_44) {
		LimitationOfCoverageToDesignatedPremisesOrProject_CG_21_44 = limitationOfCoverageToDesignatedPremisesOrProject_CG_21_44;
	}






	public boolean isLimitationOfCoverageToInsuredPremises_CG_28_06() {
		return LimitationOfCoverageToInsuredPremises_CG_28_06;
	}






	public void setLimitationOfCoverageToInsuredPremises_CG_28_06(boolean limitationOfCoverageToInsuredPremises_CG_28_06) {
		LimitationOfCoverageToInsuredPremises_CG_28_06 = limitationOfCoverageToInsuredPremises_CG_28_06;
	}






	public boolean isCommunicableDiseaseExclusion__CG_21_32() {
		return CommunicableDiseaseExclusion__CG_21_32;
	}






	public void setCommunicableDiseaseExclusion__CG_21_32(boolean communicableDiseaseExclusion__CG_21_32) {
		CommunicableDiseaseExclusion__CG_21_32 = communicableDiseaseExclusion__CG_21_32;
	}






	public boolean isExclusion_AdultDayCareCenters_CG_22_87() {
		return Exclusion_AdultDayCareCenters_CG_22_87;
	}






	public void setExclusion_AdultDayCareCenters_CG_22_87(boolean exclusion_AdultDayCareCenters_CG_22_87) {
		Exclusion_AdultDayCareCenters_CG_22_87 = exclusion_AdultDayCareCenters_CG_22_87;
	}






	public boolean isExclusion_AllHazardsInConnectionWithDesignatedPremises_CG_21_00() {
		return Exclusion_AllHazardsInConnectionWithDesignatedPremises_CG_21_00;
	}






	public void setExclusion_AllHazardsInConnectionWithDesignatedPremises_CG_21_00(boolean exclusion_AllHazardsInConnectionWithDesignatedPremises_CG_21_00) {
		Exclusion_AllHazardsInConnectionWithDesignatedPremises_CG_21_00 = exclusion_AllHazardsInConnectionWithDesignatedPremises_CG_21_00;
	}






	public boolean isExclusion_CoverageCMedicalPayments_CG_21_35() {
		return Exclusion_CoverageCMedicalPayments_CG_21_35;
	}






	public void setExclusion_CoverageCMedicalPayments_CG_21_35(boolean exclusion_CoverageCMedicalPayments_CG_21_35) {
		Exclusion_CoverageCMedicalPayments_CG_21_35 = exclusion_CoverageCMedicalPayments_CG_21_35;
	}






	public boolean isExclusion_DescribedHazards_CarnivalsCircusesAndFairs_CG_22_58() {
		return Exclusion_DescribedHazards_CarnivalsCircusesAndFairs_CG_22_58;
	}






	public void setExclusion_DescribedHazards_CarnivalsCircusesAndFairs_CG_22_58(boolean exclusion_DescribedHazards_CarnivalsCircusesAndFairs_CG_22_58) {
		Exclusion_DescribedHazards_CarnivalsCircusesAndFairs_CG_22_58 = exclusion_DescribedHazards_CarnivalsCircusesAndFairs_CG_22_58;
	}






	public boolean isExclusion_DesignatedOngoingOperations_CG_21_53() {
		return Exclusion_DesignatedOngoingOperations_CG_21_53;
	}






	public void setExclusion_DesignatedOngoingOperations_CG_21_53(boolean exclusion_DesignatedOngoingOperations_CG_21_53) {
		Exclusion_DesignatedOngoingOperations_CG_21_53 = exclusion_DesignatedOngoingOperations_CG_21_53;
	}






	public boolean isExclusion_DesignatedProducts_CG_21_33() {
		return Exclusion_DesignatedProducts_CG_21_33;
	}






	public void setExclusion_DesignatedProducts_CG_21_33(boolean exclusion_DesignatedProducts_CG_21_33) {
		Exclusion_DesignatedProducts_CG_21_33 = exclusion_DesignatedProducts_CG_21_33;
	}






	public boolean isExclusion_DesignatedWork_CG_21_34() {
		return Exclusion_DesignatedWork_CG_21_34;
	}






	public void setExclusion_DesignatedWork_CG_21_34(boolean exclusion_DesignatedWork_CG_21_34) {
		Exclusion_DesignatedWork_CG_21_34 = exclusion_DesignatedWork_CG_21_34;
	}






	public boolean isExclusion_IntercompanyProductsSuits_CG_21_41() {
		return Exclusion_IntercompanyProductsSuits_CG_21_41;
	}






	public void setExclusion_IntercompanyProductsSuits_CG_21_41(boolean exclusion_IntercompanyProductsSuits_CG_21_41) {
		Exclusion_IntercompanyProductsSuits_CG_21_41 = exclusion_IntercompanyProductsSuits_CG_21_41;
	}






	public boolean isExclusion_MedicalPaymentsToChildrenDayCareCenters_CG_22_40() {
		return Exclusion_MedicalPaymentsToChildrenDayCareCenters_CG_22_40;
	}






	public void setExclusion_MedicalPaymentsToChildrenDayCareCenters_CG_22_40(boolean exclusion_MedicalPaymentsToChildrenDayCareCenters_CG_22_40) {
		Exclusion_MedicalPaymentsToChildrenDayCareCenters_CG_22_40 = exclusion_MedicalPaymentsToChildrenDayCareCenters_CG_22_40;
	}

	public boolean isExclusion_NewEntities_CG_21_36() {
		return Exclusion_NewEntities_CG_21_36;
	}

	public void setExclusion_NewEntities_CG_21_36(boolean exclusion_NewEntities_CG_21_36) {
		Exclusion_NewEntities_CG_21_36 = exclusion_NewEntities_CG_21_36;
	}

	public boolean isExclusion_RollingStockRailroadConstruction_CG_22_46() {
		return Exclusion_RollingStockRailroadConstruction_CG_22_46;
	}

	public void setExclusion_RollingStockRailroadConstruction_CG_22_46(boolean exclusion_RollingStockRailroadConstruction_CG_22_46) {
		Exclusion_RollingStockRailroadConstruction_CG_22_46 = exclusion_RollingStockRailroadConstruction_CG_22_46;
	}

	public boolean isExclusion_UndergroundResourcesAndEquipment_CG_22_57() {
		return Exclusion_UndergroundResourcesAndEquipment_CG_22_57;
	}

	public void setExclusion_UndergroundResourcesAndEquipment_CG_22_57(boolean exclusion_UndergroundResourcesAndEquipment_CG_22_57) {
		Exclusion_UndergroundResourcesAndEquipment_CG_22_57 = exclusion_UndergroundResourcesAndEquipment_CG_22_57;
	}

	public boolean isExclusion_VolunteerWorkers_CG_21_66() {
		return Exclusion_VolunteerWorkers_CG_21_66;
	}

	public void setExclusion_VolunteerWorkers_CG_21_66(boolean exclusion_VolunteerWorkers_CG_21_66) {
		Exclusion_VolunteerWorkers_CG_21_66 = exclusion_VolunteerWorkers_CG_21_66;
	}
	
	public boolean hasChanged() {
		return hasChanged;
	}

	public void setHasChanged(boolean hasChanged) {
		this.hasChanged = hasChanged;
	}






	public List<String> getUnmannedAircraft_CG_24_50_MakeModelSerialNumber() {
		return unmannedAircraft_CG_24_50_MakeModelSerialNumber;
	}






	public void setUnmannedAircraft_CG_24_50_MakeModelSerialNumber(List<String> unmannedAircraft_CG_24_50_MakeModelSerialNumber) {
		this.unmannedAircraft_CG_24_50_MakeModelSerialNumber = unmannedAircraft_CG_24_50_MakeModelSerialNumber;
	}






	public List<String> getUnmannedAircraft_CG_24_50_DescriptionOfOpperations() {
		return unmannedAircraft_CG_24_50_DescriptionOfOpperations;
	}






	public void setUnmannedAircraft_CG_24_50_DescriptionOfOpperations(List<String> unmannedAircraft_CG_24_50_DescriptionOfOpperations) {
		this.unmannedAircraft_CG_24_50_DescriptionOfOpperations = unmannedAircraft_CG_24_50_DescriptionOfOpperations;
	}
}

package repository.gw.enums;

public enum GeneralLiabilityForms {
	/**
	 * These are set differently styles to find out which version we like best. 
	 * After a few uses we will change all of them to the same style. 
	 */
	CertificateofLiabilityInsurance("Certificate of Liability Insurance"),
	CommercialGeneralLiabilityCoverageForm_CG_00_01("Commercial General Liability Coverage Form CG 00 01"),
	LiquorLiabilityCoverageForm_CG_00_33("Liquor Liability Coverage Form CG 00 33"),
	DeductibleLiabilityInsurance_CG_03_00("Deductible Liability Insurance CG 03 00"),
	PrimaryAndNoncontributory_OtherInsuranceCondition_CG_20_01("Primary And Noncontributory - Other Insurance Condition CG 20 01"),
	AdditionalInsured_ClubMembers_CG_20_02("Additional Insured - Club Members CG 20 02"),
	AdditionalInsured_ConcessionairesTradingUnderYourName_CG_20_03("Additional Insured - Concessionaires Trading Under Your Name CG 20 03"),
	AdditionalInsured_CondominiumUnitOwners_CG_20_04("Additional Insured - Condominium Unit Owners CG 20 04"),
	AdditionalInsured_ControllingInterest_CG_20_05("Additional Insured - Controlling Interest CG 20 05"),
	AdditionalInsured_EngineersArchitectsOrSurveyors_CG_20_07("Additional Insured - Engineers, Architects Or Surveyors CG 20 07"),
	AdditionalInsured_UsersOfGolfmobiles_CG_20_08("Additional Insured - Users Of Golfmobiles CG 20 08"),
	AdditionalInsured_OwnersLesseesOrContractors_ScheduledPersonOrOrganization_CG_20_10("Additional Insured - Owners, Lessees Or Contractors - Scheduled Person Or Organization CG 20 10"),
	AdditionalInsured_ManagersOrLessorsOfPremises_CG_20_11("Additional Insured - Managers Or Lessors Of Premises CG 20 11"),
	AdditionalInsured_StateOrGovernmentalAgencyOrSubdivisionOrPoliticalSubdivision_PermitsOrAuthorizations_CG_20_12("Additional Insured - State Or Governmental Agency Or Subdivision Or Political Subdivision - Permits Or Authorizations CG 20 12"),
	AdditionalInsured_StateOrGovernmentalAgencyOrSubdivisionOrPoliticalSubdivision_PermitsOrAuthorizationsRelatingToPremises_CG_20_13("Additional Insured - State Or Governmental Agency Or Subdivision Or Political Subdivision - Permits Or Authorizations Relating To Premises CG 20 13"),
	AdditionalInsured_UsersOfTeamsDraftOrSaddleAnimals_CG_20_14("Additional Insured - Users Of Team, Draft Or Saddle Animals CG 20 14"),
	AdditionalInsured_Vendors_CG_20_15("Additional Insured - Vendors CG 20 15"),
	AdditionalInsured_TownhouseAssociations_CG_20_17("Additional Insured - Townhouse Associations CG 20 17"),
	AdditionalInsured_MortgageeAssigneeOrReceiver_CG_20_18("Additional Insured - Mortgagee, Assignee Or Receiver CG 20 18"),
	AdditionalInsured_CharitableInstitutions_CG_20_20("Additional Insured - Charitable Institutions CG 20 20"),
	AdditionalInsured_ChurchMembersAndOfficers_CG_20_22("Additional Insured - Church Members And Officers CG 20 22"),
	AdditionalInsured_ExecutorsAdministratorsTrusteesOrBeneficiaries_CG_20_23("Additional Insured - Executors, Administrators, Trustees Or Beneficiaries CG 20 23"),
	AdditionalInsured_OwnersOrOtherInterestsFromWhomLandHasBeenLeased_CG_20_24("Additional Insured - Owners Or Other Interests From Whom Land Has Been Leased CG 20 24"),
	AdditionalInsured_DesignatedPersonOrOrganization_CG_20_26("Additional Insured - Designated Person Or Organization CG 20 26"),
	AdditionalInsured_Co_OwnerOfInsuredPremises_CG_20_27("Additional Insured - Co-Owner Of Insured Premises"),
	AdditionalInsured_LessorOfLeasedEquipment_CG_20_28("Additional Insured - Lessor Of Leased Equipment CG 20 28"),
	AdditionalInsured_GrantorOfFranchise_CG_20_29("Additional Insured - Grantor Of Franchise CG 20 29"),
	AdditionalInsured_EngineersArchitectsOrSurveyorsNotEngagedByTheNamedInsured_CG_20_32("Additional Insured - Engineers, Architects Or Surveyors Not Engaged By The Named Insured CG 20 32"),
	AdditionalInsured_GrantorOfLicenses_CG_20_36("Additional Insured - Grantor Of Licenses CG 20 36"),
	AdditionalInsured_OwnersLesseesOrContractors_CompletedOperations_CG_20_37("Additional Insured - Owners, Lessees Or Contractors - Completed Operations CG 20 37"),
	Exclusion_AllHazardsInConnectionWithDesignatedPremises_CG_21_00("Exclusion - All Hazards In Connection With Designated Premises CG 21 00"),
	Exclusion_AthleticOrSportsParticipants_CG_21_01("Exclusion - Athletic Or Sports Participants CG 21 01"),
	Exclusion_Products_CompletedOperationsHazard_CG_21_04("Exclusion - Products-Completed Operations Hazard CG 21 04"),
	Exclusion_AccessOrDisclosureOfConfidentialOrPersonalInformationAndData_RelatedLiability_LimitedBodilyInjuryExceptionNotIncluded_CG_21_07("Exclusion - Access Or Disclosure Of Confidential Or Personal Information And Data-Related Liability - Limited Bodily Injury Exception Not Included CG 21 07"),
	Exclusion_UnmannedAircraft_CG_21_09("Exclusion - Unmanned Aircraft CG 21 09"),
	Exclusion_UnmannedAircraftCoverageBOnly_CG_21_11("Exclusion - Unmanned Aircraft (Coverage B Only) CG 21 11"),
	Exclusion_DesignatedProfessionalServices_CG_21_16("Exclusion - Designated Professional Services CG 21 16"),
	Exclusion_MovementOfBuildingsOrStructures_CG_21_17("Exclusion - Movement Of Buildings Or Structures CG 21 17"),
	CommunicableDiseaseExclusion_CG_21_32("Communicable Disease Exclusion CG 21 32"),
	Exclusion_DesignatedProducts_CG_21_33("Exclusion - Designated Products CG 21 33"),
	Exclusion_DesignatedWork_CG_21_34("Exclusion - Designated Work CG 21 34"),
	Exclusion_CoverageC_MedicalPayments_CG_21_35("Exclusion - Coverage C - Medical Payments CG 21 35"),
	Exclusion_NewEntities_CG_21_36("Exclusion - New Entities CG 21 36"),
	Exclusion_PersonalAndAdvertisingInjury_CG_21_38("Exclusion - Personal And Advertising Injury CG 21 38"),
	Exclusion_IntercompanyProductsSuits_CG_21_41("Exclusion - Intercompany Products Suits CG 21 41"),
	LimitationOfCoverageToDesignatedPremisesProjectOrOperation_CG_21_44("Limitation Of Coverage To Designated Premises, Project Or Operation CG 21 44"),
	AbuseOrMolestationExclusion_CG_21_46("Abuse Or Molestation Exclusion CG 21 46"),
	Employment_RelatedPracticesExclusion_CG_21_47("Employment-Related Practices Exclusion CG 21 47"),
	TotalPollutionExclusionEndorsement_CG_21_49("Total Pollution Exclusion Endorsement CG 21 49"),
	AmendmentOfLiquorLiabilityExclusion_CG_21_50("Amendment Of Liquor Liability Exclusion CG 21 50"),
	AmendmentOfLiquorLiabilityExclusion_ExceptionForScheduledActivities_CG_21_51("Amendment Of Liquor Liability Exclusion - Exception For Scheduled Activities CG 21 51"),
	Exclusion_FinancialServices_CG_21_52("Exclusion - Financial Services CG 21 52"),
	Exclusion_DesignatedOngoingOperations_CG_21_53("Exclusion - Designated Ongoing Operations CG 21 53"),
	TotalPollutionExclusionwithAHostileFireException_CG_21_55("Total Pollution Exclusion with A Hostile Fire Exception CG 21 55"),
	Exclusion_FuneralServices_CG_21_56("Exclusion - Funeral Services CG 21 56"),
	Exclusion_CounselingServices_CG_21_57("Exclusion - Counseling Services CG 21 57"),
	Exclusion_ProfessionalVeterinarianServices_CG_21_58("Exclusion - Professional Veterinarian Services CG 21 58"),
	Exclusion_DiagnosticTestingLaboratories_CG_21_59("Exclusion - Diagnostic Testing Laboratories CG 21 59"),
	Exclusion_Year2000Computer_RelatedAndOtherElectronicProblems_CG_21_60("Exclusion - Year 2000 Computer-Related And Other Electronic Problems CG 21 60"),
	Exclusion_VolunteerWorkers_CG_21_66("Exclusion - Volunteer Workers CG 21 66"),
	FungiOrBacteriaExclusion_CG_21_67("Fungi Or Bacteria Exclusion CG 21 67"),
	ExclusionOfCertifiedNuclearBiologicalChemicalOrRadiologicalActsOfTerrorismCapOnCoveredCertifiedActsLossesFromCertifiedActsOfTerrorism_CG_21_84("Exclusion Of Certified Nuclear, Biological, Chemical Or Radiological Acts Of Terrorism; Cap On Covered Certified Acts Losses From Certified Acts Of Terrorism CG 21 84"),
	Exclusion_ExteriorInsulationAndFinishSystems_CG_21_86("Exclusion - Exterior Insulation And Finish Systems CG 21 86"),
	SilicaorSilica_RelatedDustExclusion_CG_21_96("Silica or Silica-Related Dust Exclusion CG 21 96"),
	Exclusion_InspectionAppraisalAndSurveyCompanies_CG_22_24("Exclusion - Inspection, Appraisal And Survey Companies CG 22 24"),
	Amendment_TravelAgencyToursLimitationOfCoverage_CG_22_28("Amendment - Travel Agency Tours (Limitation Of Coverage) CG 22 28"),
	Exclusion_PropertyEntrusted_CG_22_29("Exclusion - Property Entrusted CG 22 29"),
	Exclusion_CorporalPunishment_CG_22_30("Exclusion - Corporal Punishment CG 22 30"),
	Exclusion_TestingOrConsultingErrorsAndOmissions_CG_22_33("Exclusion - Testing Or Consulting Errors And Omissions CG 22 33"),
	Exclusion_ConstructionManagementErrorsAndOmissions_CG_22_34("Exclusion - Construction Management Errors And Omissions CG 22 34"),
	Exclusion_ProductsAndProfessionalServicesDruggists_CG_22_36("Exclusion - Products And Professional Services (Druggists) CG 22 36"),
	Exclusion_ProductsAndProfessionalServicesOpticalAndHearingAidEstablishments_CG_22_37("Exclusion - Products And Professional Services (Optical And Hearing Aid Establishments) CG 22 37"),
	Exclusion_FiduciaryOrRepresentativeLiabilityOfFinancialInstitutions_CG_22_38("Exclusion - Fiduciary Or Representative Liability Of Financial Institutions CG 22 38"),
	Exclusion_CampsOrCampgrounds_CG_22_39("Exclusion - Camps Or Campgrounds CG 22 39"),
	Exclusion_MedicalPaymentsToChildrenDayCareCenters_CG_22_40("Exclusion - Medical Payments To Children Day Care Centers CG 22 40"),
	Exclusion_EngineersArchitectsOrSurveyorsProfessionalLiability_CG_22_43("Exclusion - Engineers, Architects Or Surveyors Professional Liability CG 22 43"),
	Exclusion_ServicesFurnishedByHealthCareProviders_CG_22_44("Exclusion - Services Furnished By Health Care Providers CG 22 44"),
	Exclusion_SpecifiedTherapeuticOrCosmeticServices_CG_22_45("Exclusion - Specified Therapeutic Or Cosmetic Services CG 22 45"),
	Exclusion_RollingStock_RailroadConstruction_CG_22_46("Exclusion - Rolling Stock - Railroad Construction CG 22 46"),
	Exclusion_InsuranceAndRelatedOperations_CG_22_48("Exclusion - Insurance And Related Operations CG 22 48"),
	Exclusion_FailureToSupply_CG_22_50("Exclusion - Failure To Supply CG 22 50"),
	Exclusion_LaundryAndDryCleaningDamage_CG_22_53("Exclusion - Laundry And Dry Cleaning Damage CG 22 53"),
	Exclusion_UndergroundResourcesAndEquipment_CG_22_57("Exclusion - Underground Resources And Equipment CG 22 57"),
	Exclusion_DescribedHazardsCarnivalsCircusesAndFairs_CG_22_58("Exclusion - Described Hazards (Carnivals, Circuses And Fairs) CG 22 58"),
	LimitationOfCoverage_RealEstateOperations_CG_22_60("Limitation Of Coverage - Real Estate Operations CG 22 60"),
	MisdeliveryOfLiquidProductsCoverage_CG_22_66("Misdelivery Of Liquid Products Coverage CG 22 66"),
	OperationOfCustomersAutosOnParticularPremises_CG_22_68("Operation Of Customers Autos On Particular Premises CG 22 68"),
	RealEstatePropertyManaged_CG_22_70("Real Estate Property Managed CG 22 70"),
	CollegesOrSchoolsLimitedForm_CG_22_71("Colleges Or Schools (Limited Form) CG 22 71"),
	Exclusion_OilOrGasProducingOperations_CG_22_73("Exclusion - Oil Or Gas Producing Operations CG 22 73"),
	ProfessionalLiabilityExclusion_ComputerDataProcessing_CG_22_77("Professional Liability Exclusion - Computer Data Processing CG 22 77"),
	Exclusion_Contractors_ProfessionalLiability_CG_22_79("Exclusion - Contractors - Professional Liability CG 22 79"),
	Exclusion_ErroneousDeliveryOrMixtureAndResultingFailureOfSeedToGerminate_SeedMerchants_CG_22_81("Exclusion - Erroneous Delivery Or Mixture And Resulting Failure Of Seed To Germinate - Seed Merchants CG 22 81"),
	Exclusion_AdultDayCareCenters_CG_22_87("Exclusion - Adult Day Care Centers CG 22 87"),
	ProfessionalLiabilityExclusion_ElectronicDataProcessingServicesAndComputerConsultingOrProgrammingServices_CG_22_88("Professional Liability Exclusion - Electronic Data Processing Services And Computer Consulting Or Programming Services CG 22 88"),
	ProfessionalLiabilityExclusion_SpasOrPersonalEnhancementFacilities_CG_22_90("Professional Liability Exclusion - Spas Or Personal Enhancement Facilities CG 22 90"),
	Exclusion_TelecommunicationEquipmentOrServiceProvidersErrorsAndOmissions_CG_22_91("Exclusion - Telecommunication Equipment Or Service Providers Errors And Omissions CG 22 91"),
	SnowPlowOperationsCoverage_CG_22_92("Snow Plow Operations Coverage CG 22 92"),
	Exclusion_DamageToWorkPerformedBySubcontractorsOnYourBehalf_CG_22_94("Exclusion - Damage To Work Performed By Subcontractors On Your Behalf CG 22 94"),
	LimitedExclusion_PersonalAndAdvertisingInjury_Lawyers_CG_22_96("Limited Exclusion - Personal And Advertising Injury - Lawyers CG 22 96"),
	Exclusion_InternetServiceProvidersAndInternetAccessProvidersErrorsAndOmissions_CG_22_98("Exclusion - Internet Service Providers And Internet Access Providers Errors And Omissions CG 22 98"),
	ProfessionalLiabilityExclusion_WebSiteDesigners_CG_22_99("Professional Liability Exclusion - Web Site Designers CG 22 99"),
	Exclusion_RealEstateAgentsOrBrokersErrorsOrOmissions_CG_23_01("Exclusion - Real Estate Agents Or Brokers Errors Or Omissions CG 23 01"),
	WaiverOfTransferOfRightsOfRecoveryAgainstOthersToUs_CG_24_04("Waiver Of Transfer Of Rights Of Recovery Against Others To Us CG 24 04"),
	LiquorLiability_BringYourOwnAlcoholEstablishments_CG_24_06("Liquor Liability - Bring Your Own Alcohol Establishments CG 24 06"),
	Products_CompletedOperationsHazardRedefined_CG_24_07("Products/Completed Operations Hazard Redefined CG 24 07"),
	ExcessProvision_Vendors_CG_24_10("Excess Provision - Vendors CG 24 10"),
	Boats_CG_24_12("Boats CG 24 12"),
	CanoesOrRowboats_CG_24_16("Canoes Or Rowboats CG 24 16"),
	ContractualLiability_Railroads_CG_24_17("Contractual Liability - Railroads CG 24 17"),
	AmendmentOfInsuredContractDefinition_CG_24_26("Amendment Of Insured Contract Definition CG 24 26"),
	LimitedCoverageForDesignatedUnmannedAircraftCoverageAOnly_CG_24_51("Limited Coverage For Designated Unmanned Aircraft (Coverage A Only) CG 24 51"),
	DesignatedConstructionProjectsGeneralAggregateLimit_CG_25_03("Designated Construction Project(s) General Aggregate Limit CG 25 03"),
	DesignatedLocationsGeneralAggregateLimit_CG_25_04("Designated Location(s) General Aggregate Limit CG 25 04"),
	LimitationOfCoverageToInsuredPremises_CG_28_06("Limitation Of Coverage To Insured Premises CG 28 06"),
	EmploymentPracticeLiabilityInsuranceID_CG_31_2013("Employment Practice Liability Insurance IDCG 31 2013"),
	CommercialGeneralLiabilityDeclarationsID_CG_03_0001("Commercial General Liability Declarations IDCG 03 0001"),
	LiquorLiabilityDeclarationsID_CG_03_0002("Liquor Liability Declarations IDCG 03 0002"),
	IdahoProfessionalApplicatorCertificateOfInsuranceID_CG_04_0001("Idaho Professional Applicator Certificate Of Insurance IDCG 04 0001"),
	Exclusion_ExplosionCollapseAndUndergroundPropertyDamageHazardSpecifiedOperationsID_CG_31_2001("Exclusion - Explosion, Collapse And Underground Property Damage Hazard (Specified Operations) IDCG 31 2001"),
	Exclusion_DesignatedOperationsCoveredByAConsolidatedWrap_UpInsuranceProgramID_CG_31_2002("Exclusion - Designated Operations Covered By A Consolidated (Wrap-Up) Insurance Program IDCG 31 2002"),
	MobileEquipmentModificationEndorsementID_CG_31_2003("Mobile Equipment Modification Endorsement IDCG 31 2003"),
	AffiliateAndSubsidiaryDefinitionEndorsementID_CG_31_2004("Affiliate And Subsidiary Definition Endorsement IDCG 31 2004"),
	PollutantsDefinitionEndorsementID_CG_31_2005("Pollutants Definition Endorsement IDCG 31 2005"),
	LawnCareServicesCoverageID_CG_31_2006("Lawn Care Services Coverage IDCG 31 2006"),
	ExclusionOfCoverageForStructuresBuiltOutsideOfDesignatedAreasEndorsementID_CG_31_2007("Exclusion Of Coverage For Structures Built Outside Of Designated Areas Endorsement IDCG 31 2007"),
	EndorsementExcludingBodilyInjuryClaimByYourEmployeeAgainstAnAdditionalInsuredID_CG_31_2008("Endorsement Excluding Bodily Injury Claim By Your Employee Against An Additional Insured IDCG 31 2008"),
	FarmMachineryOperationsByContractorsExclusionEndorsementID_CG_31_2009("Farm Machinery Operations By Contractors Exclusion Endorsement IDCG 31 2009"),
	FertilizerDistributorsAndDealersExclusionEndorsementID_CG_31_2010("Fertilizer Distributors And Dealers Exclusion Endorsement IDCG 31 2010"),
	CommercialGeneralLiabilityManuscriptEndorsementID_CG_31_2012("Commercial General Liability Manuscript Endorsement IDCG 31 2012"),
	EmploymentPracticesLiabilitySupplementalDeclarationsIDCW030001("Employment Practices Liability Supplemental Declarations IDCW 03 0001"),
	SupplementalExtendedReportingPeriodEndorsementIDCW310002("Supplemental Extended Reporting Period Endorsement IDCW 31 0002"),
	EmploymentPracticesLiabilityWarrantyStatementIDCW320001("Employment Practices Liability Warranty Statement IDCW 32 0001");

	String value;
	
	GeneralLiabilityForms(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
	
}

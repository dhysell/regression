package repository.gw.enums;

import gwclockhelpers.ApplicationOrCenter;

import java.util.ArrayList;

public class DocFormEvents {
	
	public enum PolicyCenter {
		
		//Because these sync up with the Document Events database, these values must be unique.
		BOP_AddProtectiveDevices("BOP: Add Protective Devices", repository.gw.enums.ProductLineType.Businessowners, repository.gw.enums.LineSelection.Businessowners, true),
		BOP_AddProtectiveSafeguards("BOP: Add Protective Safeguards", repository.gw.enums.ProductLineType.Businessowners, repository.gw.enums.LineSelection.Businessowners, true),
		BOP_BOLine_EquipmentBreakdown("BOP: BO Line -- Equipment Breakdown Coverage", repository.gw.enums.ProductLineType.Businessowners, repository.gw.enums.LineSelection.Businessowners, true),
		BOP_Building_ClassCode09411SelfStorage("BOP: Building -- Class Code - 09411 Self Storage", repository.gw.enums.ProductLineType.Businessowners, repository.gw.enums.LineSelection.Businessowners, true),
		BOP_Building_CondoUnitOwner("BOP: Building -- Condo - Association", repository.gw.enums.ProductLineType.Businessowners, repository.gw.enums.LineSelection.Businessowners, true),
		BOP_Building_CondoAssociation("BOP: Building -- Condo - Unit Owner", repository.gw.enums.ProductLineType.Businessowners, repository.gw.enums.LineSelection.Businessowners, true),
		BOP_DeclinePDDeductible("BOP: Decline PD Deductible", repository.gw.enums.ProductLineType.Businessowners, repository.gw.enums.LineSelection.Businessowners, true),
		BOP_Job_CancelInsuredRequest("BOP: Job -- Cancel - Insured Request", repository.gw.enums.ProductLineType.Businessowners, repository.gw.enums.LineSelection.Businessowners, true),
		BOP_Job_CancelNonPay("BOP: Job -- Cancel - NSF", repository.gw.enums.ProductLineType.Businessowners, repository.gw.enums.LineSelection.Businessowners, true),
		BOP_Job_CancelNSF("BOP: Job -- Cancel - Non Pay", repository.gw.enums.ProductLineType.Businessowners, repository.gw.enums.LineSelection.Businessowners, true),
		BOP_Job_Issuance("BOP: Job -- Issuance", repository.gw.enums.ProductLineType.Businessowners, repository.gw.enums.LineSelection.Businessowners, true),
		BOP_Job_RenewalMonthly("BOP: Job -- Renewal - Monthly", repository.gw.enums.ProductLineType.Businessowners, repository.gw.enums.LineSelection.Businessowners, true),
		BOP_Job_RenewalNonMonthly("BOP: Job -- Renewal - Non Monthly", repository.gw.enums.ProductLineType.Businessowners, repository.gw.enums.LineSelection.Businessowners, true),
		BOP_Job_RenewalTransition("BOP: Job -- Renewal - Transition", repository.gw.enums.ProductLineType.Businessowners, repository.gw.enums.LineSelection.Businessowners, true),
		BOP_Job_RewriteFullTerm("BOP: Job -- Rewrite - Full Term", repository.gw.enums.ProductLineType.Businessowners, repository.gw.enums.LineSelection.Businessowners, true),
		BOP_Job_RewriteNewAccount("Job -- Rewrite - New Account", repository.gw.enums.ProductLineType.Businessowners, repository.gw.enums.LineSelection.Businessowners, true),
		BOP_Job_RewriteNewTerm("BOP: Job -- Rewrite - New Term", repository.gw.enums.ProductLineType.Businessowners, repository.gw.enums.LineSelection.Businessowners, true),
		BOP_Job_RewriteRemainderOfTerm("Job -- Rewrite - Remainder of Term", repository.gw.enums.ProductLineType.Businessowners, repository.gw.enums.LineSelection.Businessowners, true),
		BOP_Job_Submission("BOP: Job -- Submission", repository.gw.enums.ProductLineType.Businessowners, repository.gw.enums.LineSelection.Businessowners, true),
		BOP_Renewal_PremiumIncrease("BOP: Renewal -- Premium Increase", repository.gw.enums.ProductLineType.Businessowners, repository.gw.enums.LineSelection.Businessowners, true),
		
		Squire_IM("Squire: Inland Marine", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.InlandMarineLinePL, true),
		Squire_IM_AddDeathOfLivestock("Squire: Add IM Death Livestock", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.InlandMarineLinePL, true),
		Squire_IM_AddLivestock("Squire: Add IM Livestock", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.InlandMarineLinePL, true),
		Squire_IM_AddWatercraft("Squire: Add Watercraft", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.InlandMarineLinePL, true),
		Squire_IM_RecEquipmentOrWheelchair("Squire: Add Recreation Equipment or Medical Personal Property", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.InlandMarineLinePL, true),
		Squire_IM_AddRefrigeratedMilk("Squire: Add Refrigerated Milk", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.InlandMarineLinePL, true),
		Squire_IM_AddSportingEquipment("Squire: Add Sporting Equipment", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.InlandMarineLinePL, true),
		Squire_IM_AddOutsidePersonalProperty("Squire: Add Outside Personal Property", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.InlandMarineLinePL, true),
		Squire_IM_AddBeeContainers("Squire: Add Bee Containers", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.InlandMarineLinePL, true),
		Squire_IM_AddBlanketRadios("Squire: Add Blanket Radios", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.InlandMarineLinePL, true),
		Squire_IM_AddCargoClassIorII("Squire: Add Cargo Class I or II", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.InlandMarineLinePL, true),
		Squire_IM_AddCargoClassIII("Squire: Add Cargo Class III", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.InlandMarineLinePL, true),
		Squire_IM_AddPersonalPropertyMilkContamination("Squire: Add Personal Property Milk", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.InlandMarineLinePL, true),
		Squire_IM_AddIMScheduledItemLessor("Squire: Add Lessor to Scheduled Item", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.InlandMarineLinePL, true),
		Squire_IM_FarmEquipmentSpecial("Squire: Add Farm Equipment Special Form", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.InlandMarineLinePL, true),
		Squire_IM_FarmEquipmentBroad("Squire: Farm Equipment Broad Form", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.InlandMarineLinePL, true),
		Squire_IM_FarmEquipmentAddInsured("Squire: Add Farm Equipment Additional Insured", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.InlandMarineLinePL, true),
		Squire_IM_SpecialEndorsementForIM405("Squire: Special Endorsement for Inland Marine 405", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.InlandMarineLinePL, true),
		Squire_IM_IrrigationEquipmentEmdorsement("Squire: Irrigation Equipment Emdorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.InlandMarineLinePL, true),
		
		Squire_Liab_AddCoveragePrivateLandingStrips("Squire - Liability: Add Private Landing Strips", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, false),
		Squire_Liab_AddExclusionConditionVendorAsAdditionalInsuredEndorsement207("Squire - Liability: Add ExclusionsCondition Vendor As Additional Insured Endorsement 207", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, false),
		Squire_Liab_CountrySquirePolicy("Squire: Country Squire Policy", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_BuffaloAndElkEndorsement("Squire: Buffalo and Elk Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_SpecialEndorsementProperty("Squire: Special Endorsement for Property", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_ActualCashValueLimitationRoofing("Squire: Actual Cash Value Limitation for Roofing Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_CoverageADetachedGarageStorageShed("Squire: Coverage A (Dwellings) Detached Garage and Storage Shed", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_CoverageDExtensionEndorsement("Squire: Coverage D Extension Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_SpecifiedPropertyExclusionEndorsement("Squire: Specified Property Exclusion Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_SewageSumpSystemBackupEndorsement("Squire: Sewage or Sump System Backup Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_EarthquakeDamageEndorsement("Squire: Earthquake Damage Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_EarthquakeDamageEndorsementMasonry("Squire: Earthquake Damage Endorsement - Masonry", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_LimitedRoofCoverageEndorsement("Squire: Limited Roof Coverage Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_ReplacementCostDeletionEndorsement("Squire: Replacement Cost Deletion Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_CoverageDAdditionalInsuredEndorsement("Squire: Coverage D Additional Insured Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_FreezingLivestockEndorsement("Squire: Freezing of Livestock Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_SpecialEndorsementLiabiliy("Squire: Special Endorsement for Liability", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_PermittedIncidentalOccupancy("Squire: Permitted Incidental Occupancy", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_VendorAdditionalInsuredEndorsement("Squire: Vendor as Additional Insured Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_AccessYesEndorsement("Squire: Access Yes Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_WatercraftEndorsement("Squire: Watercraft Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_LimitedSeedProductCoverageEndorsement("Squire: Limited Seed Product Coverage Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_RawMilkExclusionEndorsement("Squire: Raw Milk Exclusion Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_CropDustingSprayingEndorsement("Squire: Crop Dusting and Spraying Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_ChildCareEndorsement("Squire: Child Care Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_FeedlotCustomFarmingEndorsement("Squire: Feedlot Custom Farming Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_CustomFarmingFireEndorsement("Squire: Custom Farming Fire Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_CanineExclusionEndorsement("Squire: Canine Exclusion Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_LimitedPollutionCoverageEndorsement("Squire: Limited Pollution Coverage Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_AdditionalInsuredLandlordEndorsement("Squire: Additional Insured - Landlord Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_CondoUnitEndorsement("Squire: Condo Unit Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_CitySquirePolicy("Squire: City Squire Policy Booklet", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_CitySquirePolicyDeclarations("Squire: City Squire Policy Declarations", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_CountrySquirePolicyDeclarations("Squire: Country Squire Policy Declarations", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_FarmRanchSquirePolicyBooklet("Squire: Farm & Ranch Squire Policy Booklet", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_EmployersNonowenshipLiability("Squire: Employer's Nonownership Liability", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_HorseBoardingPasturingEndorsement("Squire: Horse Boarding and Pasturing Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_AdditionalLivestockEndorsement("Squire: Additional Livestock Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_ModularMobileHomeEndorsement("Squire: Modular Mobile Home Endorsement - Excluding Peril 17", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_MobileHomeEndorsement("Squire: Mobile Home Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_CoverageESpecialFormEndorsment("Squire: Coverage E Special Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_CoverageEBroadFormEndorsment("Squire: Coverage E Broad Form Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_LossOfIncomeExtraExpenseEndorsement("Squire: Loss of Income and Extra Expense Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_OpenFlameWarrantyEndorsment("Squire: Open Flame Warranty Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_LeasongEndorsmentSectionISectionII("Squire: Leasing Endorsement - Sections I & II", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab_LimittedPollutionBasicCoverage("Squire: Limited Pollution Coverage (Basic Coverage)", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab__LimittedPollutionGovernmentCleanUp("Squire: Limited Pollution Coverage Endorsement (Government Cleanup)", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab__LimittedPollutionMnurePit("Squire: Limited Pollution Coverage Endorsement (Manure Pit)", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_Liab__RecreationalVehicleCertificateOfLiability("Squire: Recreational Vehicle Certificate of Liability", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_FarmRanchPolicyDeclarations("Squire: Farm & Ranch Policy Declarations", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_NoticeOfPolicyOnPrivacy("Notice of Policy on Privacy", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Squire_AdverseActionLetter("Adverse Action Letter", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		PL_RenewalReviewAudit("PL Renewal Review Audit", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		PL_PersonalLinesApplication("Personal Lines Application", null, null, true),
		PL_InsuredReinstatementNoticePartial("Insured Reinstatement Notice - Partial", null, null, true),
		Squire_CommoditySheet("Squire: Commodity Sheet", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		
		Squire_Auto_AutomobilePolicy("Squire: Automobile Policy", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PersonalAutoLinePL, true),
		Squire_Auto_ModificationInsuredVehicle("Squire: Modification of Insured Vehicle Definition Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PersonalAutoLinePL, true),
		Squire_Auto_ShowCarDriverExclusion("Squire: Show Car Driver Exclusion Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PersonalAutoLinePL, true),
		Squire_Auto_DriverExclusion("Squire: Driver Exclusion Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PersonalAutoLinePL, true),
		Squire_Auto_SpecialEndorsement("Squire: Special Endorsement For Auto", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PersonalAutoLinePL, true),
		Squire_Auto_DeletionMaterialDamage("Squire: Deletion of Material Damage Coverage Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PersonalAutoLinePL, true),
		Squire_Auto_DriveOtherCar("Squire: Driver other Car", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PersonalAutoLinePL, true),
		Squire_Auto_AdditionalLivingExpense("Squire: Additional Living Expenses Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PersonalAutoLinePL, true),
		Squire_Auto_AdditionalInsuredEndorsement("Squire: Additional Insured Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PersonalAutoLinePL, true),
		Squire_Auto_ClassicCarLimitationCoverage("Squire: Classic Car Limitation of Coverage Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PersonalAutoLinePL, true),
		Squire_Auto_InsuredVehicleLeasingEndorsement("Squire: Insured Vehicle Leasing Endorsement", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PersonalAutoLinePL, true),
		Squire_Auto_InsuranceScoreDiscount("Squire: Insurance Score Discount", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PersonalAutoLinePL, true),
		Squire_Auto_CertificateofLiabilityInsurance("Squire: Certificate of Liability Insurance", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PersonalAutoLinePL, true),
		Squire_Auto_StandardAutoPolicyDeclarations("Squire: Standard Auto Policy Declarations", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PersonalAutoLinePL, true),
		
		Squire_AdditionalInterestDeclarations("Squire: Additional Interest Declarations", repository.gw.enums.ProductLineType.Squire, null, true),
		Squire_PersonalLinesApplication("Squire: Personal Lines Application", repository.gw.enums.ProductLineType.Squire, null, true),
		Squire_PersonalLinesRenewalApplication("Squire: PL Renewal Review Audit", repository.gw.enums.ProductLineType.Squire, null, true),
		
		PersonalUmbrella_Job_PersonalUmbrellaPolicyBooklet("PersonalUmbrella: Personal Umbrella Policy Booklet", repository.gw.enums.ProductLineType.PersonalUmbrella, null, true),
		PersonalUmbrella_UnderlyingSquireHasPrivateLandingStrips("PersonalUmbrella: Underlying Squire Has Private Landing Strips", repository.gw.enums.ProductLineType.PersonalUmbrella, null, true),
		PersonalUmbrella_UnderlyingSquireHas207("PersonalUmbrella: Underlying Squire Has 207", repository.gw.enums.ProductLineType.PersonalUmbrella, null, true),
		PersonalUmbrella_AdditionalUnderlyingInsuranceEndorsement217("PersonalUmbrella: Additional Underlying Insurance Umbrella Endorsement 217", repository.gw.enums.ProductLineType.PersonalUmbrella, null, true),
		PersonalUmbrella_AddExclusionConditionSpecialEndorsementForLiability205("PersonalUmbrella: Add ExclusionCondition - Special Endorsement For Liability 205", repository.gw.enums.ProductLineType.PersonalUmbrella, null, true),
		PersonalUmbrella_AddExclusionConditionExcludedDriverEndorsement246("PersonalUmbrella: Add ExclusionCondition - Excluded Driver Endorsement 246", repository.gw.enums.ProductLineType.PersonalUmbrella, null, true),
		PersonalUmbrella_AddExclusionConditionExcludedDriverEndorsement224("PersonalUmbrella: Add ExclusionCondition - Excluded Driver Endorsement 224", repository.gw.enums.ProductLineType.PersonalUmbrella, null, true),
		PersonalUmbrella_AddExclusionConditionUmbrellaLimitationEndo270("PersonalUmbrella: Add ExclusionCondition - Umbrella Limitation Endorsement 270", repository.gw.enums.ProductLineType.PersonalUmbrella, null, true),
		PersonalUmbrella_SilicaOrSilicaRelatedDustExclusionEndorsement219("PersonalUmbrella: Silica or Silica-Related Dust Exclusion Endorsement", repository.gw.enums.ProductLineType.PersonalUmbrella, null, true),
		PersonalUmbrella_PersonalUmbrellaPolicyDeclarations("PersonalUmbrella: Personal Umbrella Policy Declarations", repository.gw.enums.ProductLineType.PersonalUmbrella, null, true),
		PersonalUmbrella_FarmRanchUmbrellaPolicyBooklet("PersonalUmbrella: Farm & Ranch Umbrella Policy Booklet", repository.gw.enums.ProductLineType.PersonalUmbrella, null, true),
		PersonalUmbrella_FarmUmbrellaPolicyDeclarations("PersonalUmbrella: Farm Umbrella Policy Declarations", repository.gw.enums.ProductLineType.PersonalUmbrella, null, true),

        StdFire_Job_Submission("StdFire: Job -- Submission", repository.gw.enums.ProductLineType.StandardFire, repository.gw.enums.LineSelection.StandardFirePL, true),
        StdFire_AddPropertyTypeWhereYouLive("StdFire: Add Property Type Where You Live", repository.gw.enums.ProductLineType.StandardFire, repository.gw.enums.LineSelection.StandardFirePL, true),
        StdFire_AddPropertyTypeCondo("StdFire: Add Property Type Condo", repository.gw.enums.ProductLineType.StandardFire, repository.gw.enums.LineSelection.StandardFirePL, true),
        StdFire_AddCoverageTypePerilGreaterThan1to8("StdFire: Add Coverage Type Peril Greater Than 1 to 8", repository.gw.enums.ProductLineType.StandardFire, repository.gw.enums.LineSelection.StandardFirePL, true),
        StdFire_AddExclusionConditionSpecialEndorsemenForProperty105("StdFire: Add ExclusionCondition - Special Endorsement for Property 105", repository.gw.enums.ProductLineType.StandardFire, repository.gw.enums.LineSelection.StandardFirePL, true),
        StdFire_AddExclusionConditionSpecifiedPropertyExclusionEndorsement110("StdFire: Add ExclusionCondition - Specified Property Exclusion Endorsement 110", repository.gw.enums.ProductLineType.StandardFire, repository.gw.enums.LineSelection.StandardFirePL, true),
        StdFire_AddExclusionConditionLimitedRoofCoverageEndorsement134("StdFire: Add ExclusionCondition - Limited Roof Coverage Endorsement 134", repository.gw.enums.ProductLineType.StandardFire, repository.gw.enums.LineSelection.StandardFirePL, true),
        StdFire_AddConstructionTypeModularWithoutFoundation168("StdFire: Add Construction Type - Modular Without Foundation 168", repository.gw.enums.ProductLineType.StandardFire, repository.gw.enums.LineSelection.StandardFirePL, true),
        StdFire_AddConstructionTypeMobileWithoutFoundation169("StdFire: Add Construction Type - Mobile Without Foundation 169", repository.gw.enums.ProductLineType.StandardFire, repository.gw.enums.LineSelection.StandardFirePL, true),
        StdFire_AddPropertyTypeShopWithPolyNoSandwich173("StdFire: Add Property Type Shop With Poly No Sandwich 173", repository.gw.enums.ProductLineType.StandardFire, repository.gw.enums.LineSelection.StandardFirePL, true),
        StdFire_HopsPropertyCoverage("StdFire: Hops Property Coverage Part Value Reporting Basis", repository.gw.enums.ProductLineType.StandardFire, repository.gw.enums.LineSelection.StandardFirePL, true),
        StdFire_CoverageEBroadFormEndorsement146("StdFire: Coverage E Broad Form Endorsement", repository.gw.enums.ProductLineType.StandardFire, repository.gw.enums.LineSelection.StandardFirePL, true),
        StdFire_AdverseActionLetter("StdFire: Adverse Action Letter", repository.gw.enums.ProductLineType.StandardFire, repository.gw.enums.LineSelection.StandardFirePL, true),
        StdFire_StandardFirePolicyDeclarations("StdFire: Standard Fire Policy Declarations", repository.gw.enums.ProductLineType.StandardFire, repository.gw.enums.LineSelection.StandardFirePL, true),
        StdFire_PropertyPolicyBooklet("StdFire: Property Policy Booklet", repository.gw.enums.ProductLineType.StandardFire, repository.gw.enums.LineSelection.StandardFirePL, true),
        StdFire_CosmeticRoofDamageEndorsement117("StdFire: Cosmetic Roof Damage Endorsement", repository.gw.enums.ProductLineType.StandardFire, repository.gw.enums.LineSelection.StandardFirePL, true),
        StdFire_LimitedFungiWetorDryRotorBacteriaEndorsement131("StdFire: Limited Fungi, Wet or Dry Rot, or Bacteria Endorsement", repository.gw.enums.ProductLineType.StandardFire, repository.gw.enums.LineSelection.StandardFirePL, true),
        StdFire_NoticeOfPolicyOnPrivacy("StdFire: Notice of Policy on Privacy", repository.gw.enums.ProductLineType.StandardFire, repository.gw.enums.LineSelection.StandardFirePL, true),

        StdFireLiab("StdFire: Personal Lines Application", repository.gw.enums.ProductLineType.StandardFire, null, true),
        StdFireLiabRenewal("Squire: PL Renewal Review Audit", repository.gw.enums.ProductLineType.StandardFire, null, true),

        StdLiab_Job_SubmissionLessThan5Acres("StdLiab: Job -- Submission Less Than 5 Acres", repository.gw.enums.ProductLineType.StandardLiability, repository.gw.enums.LineSelection.StandardLiabilityPL, true),
        StdLiab_Job_SubmissionGreaterThanEqualTo5Acres("StdLiab: Job -- Submission Greater Than Equal To 5 Acres", repository.gw.enums.ProductLineType.StandardLiability, repository.gw.enums.LineSelection.StandardLiabilityPL, true),
        StdLiab_AddGeneralLiabilityLimitsCSLLessThan5Acres("StdLiab: Add General Liability Limits CSL Less Than 5 Acres", repository.gw.enums.ProductLineType.StandardLiability, repository.gw.enums.LineSelection.StandardLiabilityPL, true),
        StdLiab_AddGeneralLiabilityLimitsCSLGreaterThanEqualTo5Acres("StdLiab: Add General Liability Limits CSL Greater Than Equal To 5 Acres", repository.gw.enums.ProductLineType.StandardLiability, repository.gw.enums.LineSelection.StandardLiabilityPL, true),
        StdLiab_AddExclusionConditionAccessYesEndorsement209("StdLiab: Add ExclusionCondition - Access Yes Endorsement 209", repository.gw.enums.ProductLineType.StandardLiability, repository.gw.enums.LineSelection.StandardLiabilityPL, true),
        StdLiab_AddExclusionConditionSpecialEndorsementForLiability205("StdLiab: Add ExclusionCondition - Special Endorsement For Liability 205", repository.gw.enums.ProductLineType.StandardLiability, repository.gw.enums.LineSelection.StandardLiabilityPL, true),
        StdLiab_LimitedEmployersLiabilityEndorsement("StdLiab: Limited Employer's Liability Endorsement", repository.gw.enums.ProductLineType.StandardLiability, repository.gw.enums.LineSelection.StandardLiabilityPL, true),
        StdLiab_LimitedPollutionCoverageEndorsement("StdLiab: Limited Pollution Coverage Endorsement", repository.gw.enums.ProductLineType.StandardLiability, repository.gw.enums.LineSelection.StandardLiabilityPL, true),
        StdLiab_FarmLiabilityPolicyDeclarations("StdLiab: Farm Liability Policy Declarations", repository.gw.enums.ProductLineType.StandardLiability, repository.gw.enums.LineSelection.StandardLiabilityPL, true),
        StdLiab_PersonalLiabilityPolicyDeclarations("StdLiab: Personal Liability Policy Declarations", repository.gw.enums.ProductLineType.StandardLiability, repository.gw.enums.LineSelection.StandardLiabilityPL, true),
		
		StdIM_FarmEquipmentEndorsementNamedPerils("StdIM: Farm Equipment Endorsement (Named Perils)", repository.gw.enums.ProductLineType.StandardIM, repository.gw.enums.LineSelection.StandardInlandMarine, true),
		StdIM_FarmEquipmentEndorsementScheduleNamedPerils("StdIM: Farm Equipment Endorsement Schedule (Named Perils)", repository.gw.enums.ProductLineType.StandardIM, repository.gw.enums.LineSelection.StandardInlandMarine, true),
		StdIM_MiscellaneousArticlesEndorsement("StdIM: Miscellaneous Articles Endorsement", repository.gw.enums.ProductLineType.StandardIM, repository.gw.enums.LineSelection.StandardInlandMarine, true),
		StdIM_MiscellaneousArticlesScheduleEndorsement("StdIM: Miscellaneous Articles Schedule Endorsement", repository.gw.enums.ProductLineType.StandardIM, repository.gw.enums.LineSelection.StandardInlandMarine, true),
		StdIM_FarmEquipmentAdditionalInsuredEndorsement("StdIM: Farm Equipment Additional Insured Endorsement", repository.gw.enums.ProductLineType.StandardIM, repository.gw.enums.LineSelection.StandardInlandMarine, true),
		StdIM_LeasingEndorsement("StdIM: Leasing Endorsement", repository.gw.enums.ProductLineType.StandardIM, repository.gw.enums.LineSelection.StandardInlandMarine, true),
		StdIM_StandardInlandMarinePolicyDeclarations("StdIM: Standard Inland Marine Policy Declarations", repository.gw.enums.ProductLineType.StandardIM, repository.gw.enums.LineSelection.StandardInlandMarine, true),
		StdIM_InlandMarinePolicyBooklet("StdIM: Inland Marine Policy Booklet", repository.gw.enums.ProductLineType.StandardIM, repository.gw.enums.LineSelection.StandardInlandMarine, true),
		
		Reinstate_Job_ReinstateNoticeAdditionalInterestUW("PL: Reinstatement Notice Additional Interest UW", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		Reinstate_Job_ReinstateNoticeUW("PL: Reinstatement Notice UW", repository.gw.enums.ProductLineType.Squire, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, true),
		
		CPP_GL_CommercialGeneralLiabilityCoverageForm_CG_00_01("Commercial General Liability Coverage Form CG 00 01", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_LiquorLiabilityCoverageForm_CG_00_33("Liquor Liability Coverage Form CG 00 33", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_DeductibleLiabilityInsurance_CG_03_00("Deductible Liability Insurance CG 03 00", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_PrimaryAndNoncontributory_OtherInsuranceCondition_CG_20_01("Primary And Noncontributory - Other Insurance Condition CG 20 01", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_AI_ClubMembers_CG_20_02("Additional Insured - Club Members CG 20 02", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_AI_ConcessionairesTradingUnderYourName_CG_20_03("Additional Insured - Concessionaires Trading Under Your Name CG 20 03", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_AI_CondominiumUnitOwners_CG_20_04("Additional Insured - Condominium Unit Owners CG 20 04", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_AI_ControllingInterest_CG_20_05("Additional Insured - Controlling Interest CG 20 05", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_AI_EngineersArchitectsOrSurveyors_CG_20_07("Additional Insured - Engineers, Architects Or Surveyors CG 20 07", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_AI_UsersOfGolfmobiles_CG_20_08("Additional Insured - Users Of Golfmobiles CG 20 08", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_AI_OwnersLesseesOrContractors_ScheduledPersonOrOrganization_CG_20_10("Additional Insured - Owners, Lessees Or Contractors - Scheduled Person Or Organization CG 20 10", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_AI_ManagersOrLessorsOfPremises_CG_20_11("Additional Insured - Managers Or Lessors Of Premises CG 20 11", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_AI_StateOrGovernmentalAgencyOrSubdivisionOrPoliticalSubdivision_PermitsOrAuthorizations_CG_20_12("Additional Insured - State Or Governmental Agency Or Subdivision Or Political Subdivision - Permits Or Authorizations CG 20 12", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_AI_StateOrGovernmentalAgencyOrSubdivisionOrPoliticalSubdivision_PermitsOrAuthorizationsRelatingToPremises_CG_20_13("Additional Insured - State Or Governmental Agency Or Subdivision Or Political Subdivision - Permits Or Authorizations Relating To Premises CG 20 13", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_AI_UsersOfTeamsDraftOrSaddleAnimals_CG_20_14("Additional Insured - Users Of Team, Draft Or Saddle Animals CG 20 14", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_AI_Vendors_CG_20_15("Additional Insured - Vendors CG 20 15", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_AI_TownhouseAssociations_CG_20_17("Additional Insured - Townhouse Associations CG 20 17", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_AI_MortgageeAssigneeOrReceiver_CG_20_18("Additional Insured - Mortgagee, Assignee Or Receiver CG 20 18", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_AI_CharitableInstitutions_CG_20_20("Additional Insured - Charitable Institutions CG 20 20", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_AI_ChurchMembersAndOfficers_CG_20_22("Additional Insured - Church Members And Officers CG 20 22", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_AI_ExecutorsAdministratorsTrusteesOrBeneficiaries_CG_20_23("Additional Insured - Executors, Administrators, Trustees Or Beneficiaries CG 20 23", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_AI_OwnersOrOtherInterestsFromWhomLandHasBeenLeased_CG_20_24("Additional Insured - Owners Or Other Interests From Whom Land Has Been Leased CG 20 24", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_AI_DesignatedPersonOrOrganization_CG_20_26("Additional Insured - Designated Person Or Organization CG 20 26", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_AI_Co_OwnerOfInsuredPremises_CG_20_27("Additional Insured - Co-Owner Of Insured Premises CG 20 27", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_20_28("Additional Insured - Lessor Of Leased Equipment CG 20 28", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_20_29("Additional Insured - Grantor Of Franchise CG 20 29", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_20_32("Additional Insured - Engineers, Architects Or Surveyors Not Engaged By The Named Insured CG 20 32", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_20_36("Additional Insured - Grantor Of Licenses CG 20 36", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_20_37("Additional Insured - Owners, Lessees Or Contractors - Completed Operations CG 20 37", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_00("Exclusion - All Hazards In Connection With Designated Premises CG 21 00", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_01("Exclusion - Athletic Or Sports Participants CG 21 01", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_04("Exclusion - Products-Completed Operations Hazard CG 21 04", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_07("Exclusion - Access Or Disclosure Of Confidential Or Personal Information And Data-Related Liability - Limited Bodily Injury Exception Not Included CG 21 07", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_09("Exclusion - Unmanned Aircraft CG 21 09", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_16("Exclusion - Designated Professional Services CG 21 16", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_17("Exclusion - Movement Of Buildings Or Structures CG 21 17", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_32("Communicable Disease Exclusion CG 21 32", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_33("Exclusion - Designated Products CG 21 33", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_34("Exclusion - Designated Work CG 21 34", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_35("Exclusion - Coverage C - Medical Payments CG 21 35", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_36("Exclusion - New Entities CG 21 36", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_38("Exclusion - Personal And Advertising Injury CG 21 38", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_41("Exclusion - Intercompany Products Suits CG 21 41", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_44("Limitation Of Coverage To Designated Premises Or Project CG 21 44", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_46("Abuse Or Molestation Exclusion CG 21 46", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_47("Employment-Related Practices Exclusion CG 21 47", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_49("Total Pollution Exclusion Endorsement CG 21 49", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_50("Amendment Of Liquor Liability Exclusion CG 21 50", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_51("Amendment Of Liquor Liability Exclusion - Exception For Scheduled Activities CG 21 51", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_52("Exclusion - Financial Services CG 21 52", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_53("Exclusion - Designated Ongoing Operations CG 21 53", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_55("Total Pollution Exclusion with A Hostile Fire Exception CG 21 55", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_56("Exclusion - Funeral Services CG 21 56", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_57("Exclusion - Counseling Services CG 21 57", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_58("Exclusion - Professional Veterinarian Services CG 21 58", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_59("Exclusion - Diagnostic Testing Laboratories CG 21 59", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_60("Exclusion - Year 2000 Computer-Related And Other Electronic Problems CG 21 60", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_66("Exclusion - Volunteer Workers CG 21 66", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_67("Fungi Or Bacteria Exclusion CG 21 67", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_84("Exclusion Of Certified Nuclear, Biological, Chemical Or Radiological Acts Of Terrorism; Cap On Covered Certified Acts Losses From Certified Acts Of Terrorism CG 21 84", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_86("Exclusion - Exterior Insulation And Finish Systems CG 21 86", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_21_96("Silica or Silica-Related Dust Exclusion CG 21 96", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_22_24("Exclusion - Inspection, Appraisal And Survey Companies CG 22 24", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_22_28("Amendment - Travel Agency Tours (Limitation Of Coverage) CG 22 28", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_22_29("Exclusion - Property Entrusted CG 22 29", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_22_30("Exclusion - Corporal Punishment CG 22 30", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_22_32("Exclusion - Professional Services - Blood Banks CG 22 32", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_22_33("Exclusion - Testing Or Consulting Errors And Omissions CG 22 33", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_22_34("Exclusion - Construction Management Errors And Omissions CG 22 34", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_22_36("Exclusion - Products And Professional Services (Druggists) CG 22 36", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_22_37("Exclusion - Products And Professional Services (Optical And Hearing Aid Establishments) CG 22 37", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_22_38("Exclusion - Fiduciary Or Representative Liability Of Financial Institutions CG 22 38", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG2239("Exclusion - Camps Or Campgrounds CG 22 39", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CG_22_40("Exclusion - Medical Payments To Children Day Care Centers CG 22 40", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_Exclusion_EngineersArchitectsOrSurveyorsProfessionalLiability_CG_22_43("Exclusion - Engineers, Architects Or Surveyors Professional Liability CG 22 43", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_Exclusion_ServicesFurnishedByHealthCareProvidersCG2244("Exclusion - Services Furnished By Health Care Providers CG 22 44", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_Exclusion_SpecifiedTherapeuticOrCosmeticServicesCG2245("Exclusion - Specified Therapeutic Or Cosmetic Services CG 22 45", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_Exclusion_RollingStock_RailroadConstructionCG2246("Exclusion - Rolling Stock - Railroad Construction CG 22 46", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_Exclusion_InsuranceAndRelatedOperationsCG2248("Exclusion - Insurance And Related Operations CG 22 48", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_Exclusion_FailureToSupplyCG2250("Exclusion - Failure To Supply CG 22 50", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_Exclusion_LaundryAndDryCleaningDamageCG2253("Exclusion - Laundry And Dry Cleaning Damage CG 22 53", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_Exclusion_UndergroundResourcesAndEquipmentCG2257("Exclusion - Underground Resources And Equipment CG 22 57", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_Exclusion_DescribedHazardsCarnivalsCircusesAndFairsCG2258("Exclusion - Described Hazards (Carnivals, Circuses And Fairs) CG 22 58", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_LimitationOfCoverage_RealEstateOperationsCG2260("Limitation Of Coverage - Real Estate Operations CG 22 60", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_MisdeliveryOfLiquidProductsCoverageCG2266("Misdelivery Of Liquid Products Coverage CG 22 66", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_OperationOfCustomersAutosOnParticularPremisesCG2268("Operation Of Customers Autos On Particular Premises CG 22 68", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_RealEstatePropertyManagedCG2270("Real Estate Property Managed CG 22 70", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CollegesOrSchoolsLimitedFormCG2271("Colleges Or Schools (Limited Form) CG 22 71", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_Exclusion_OilOrGasProducingOperationsCG2273("Exclusion - Oil Or Gas Producing Operations CG 22 73", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_ProfessionalLiabilityExclusion_ComputerDataProcessingCG2277("Professional Liability Exclusion - Computer Data Processing CG 22 77", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_Exclusion_Contractors_ProfessionalLiabilityCG2279("Exclusion - Contractors - Professional Liability CG 22 79", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_Exclusion_ErroneousDeliveryOrMixtureAndResultingFailureOfSeedToGerminate_SeedMerchantsCG2281("Exclusion - Erroneous Delivery Or Mixture And Resulting Failure Of Seed To Germinate - Seed Merchants CG 22 81", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_Exclusion_AdultDayCareCentersCG2287("Exclusion - Adult Day Care Centers CG 22 87", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_ProfessionalLiabilityExclusion_ElectronicDataProcessingServicesAndComputerConsultingOrProgrammingServicesCG2288("Professional Liability Exclusion - Electronic Data Processing Services And Computer Consulting Or Programming Services CG 22 88", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_ProfessionalLiabilityExclusion_SpasOrPersonalEnhancementFacilitiesCG2290("Professional Liability Exclusion - Spas Or Personal Enhancement Facilities CG 22 90", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_Exclusion_TelecommunicationEquipmentOrServiceProvidersErrorsAndOmissionsCG2291("Exclusion - Telecommunication Equipment Or Service Providers Errors And Omissions CG 22 91", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_SnowPlowOperationsCoverageCG2292("Snow Plow Operations Coverage CG 22 92", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_Exclusion_DamageToWorkPerformedBySubcontractorsOnYourBehalfCG2294("Exclusion - Damage To Work Performed By Subcontractors On Your Behalf CG 22 94", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_LimitedExclusion_PersonalAndAdvertisingInjury_LawyersCG2296("Limited Exclusion - Personal And Advertising Injury - Lawyers CG 22 96", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_Exclusion_InternetServiceProvidersAndInternetAccessProvidersErrorsAndOmissionsCG2298("Exclusion - Internet Service Providers And Internet Access Providers Errors And Omissions CG 22 98", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_ProfessionalLiabilityExclusion_WebSiteDesignersCG2299("Professional Liability Exclusion - Web Site Designers CG 22 99", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_Exclusion_RealEstateAgentsOrBrokersErrorsOrOmissionsCG2301("Exclusion - Real Estate Agents Or Brokers Errors Or Omissions CG 23 01", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_WaiverOfTransferOfRightsOfRecoveryAgainstOthersToUsCG2404("Waiver Of Transfer Of Rights Of Recovery Against Others To Us CG 24 04", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_LiquorLiability_BringYourOwnAlcoholEstablishmentsCG2406("Liquor Liability - Bring Your Own Alcohol Establishments CG 24 06", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_ProductsCompletedOperationsHazardRedefinedCG2407("Products/Completed Operations Hazard Redefined CG 24 07", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_GovernmentalSubdivisionsCG2409("Governmental Subdivisions CG 24 09", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_ExcessProvision_VendorsCG2410("Excess Provision - Vendors CG 24 10", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_BoatsCG2412("Boats CG 24 12", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CanoesOrRowboatsCG2416("Canoes Or Rowboats CG 24 16", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_ContractualLiability_RailroadsCG2417("Contractual Liability - Railroads CG 24 17", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_AmendmentOfInsuredContractDefinitionCG2426("Amendment Of Insured Contract Definition CG 24 26", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_LimitedCoverageForDesignatedUnmannedAircraft("Limited Coverage For Designated Unmanned Aircraft", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_DesignatedConstructionProjectsGeneralAggregateLimitCG2503("Designated Construction Project(s) General Aggregate Limit CG 25 03", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_DesignatedLocationsGeneralAggregateLimitCG2504("Designated Location(s) General Aggregate Limit CG 25 04", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_LimitationOfCoverageToInsuredPremisesCG2806("Limitation Of Coverage To Insured Premises CG 28 06", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CommercialGeneralLiabilityDeclarationsIDCG030001("Commercial General Liability Declarations IDCG 03 0001", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_LiquorLiabilityDeclarationsIDCG030002("Liquor Liability Declarations IDCG 03 0002", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_IdahoProfessionalApplicatorCertificateOfInsuranceIDCG040001("Idaho Professional Applicator Certificate Of Insurance IDCG 04 0001", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_Exclusion_ExplosionCollapseAndUndergroundPropertyDamageHazardSpecifiedOperationsIDCG312001("Exclusion - Explosion, Collapse And Underground Property Damage Hazard (Specified Operations) IDCG 31 2001", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_Exclusion_DesignatedOperationsCoveredByAConsolidatedWrap_UpInsuranceProgramIDCG312002("Exclusion - Designated Operations Covered By A Consolidated (Wrap-Up) Insurance Program IDCG 31 2002", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_MobileEquipmentModificationEndorsementIDCG312003("Mobile Equipment Modification Endorsement IDCG 31 2003", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_AffiliateAndSubsidiaryDefinitionEndorsementIDCG312004("Affiliate And Subsidiary Definition Endorsement IDCG 31 2004", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_PollutantsDefinitionEndorsementIDCG312005("Pollutants Definition Endorsement IDCG 31 2005", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_LawnCareServicesCoverageIDCG312006("Lawn Care Services Coverage IDCG 31 2006", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_ExclusionOfCoverageForStructuresBuiltOutsideOfDesignatedAreasEndorsementIDCG312007("Exclusion Of Coverage For Structures Built Outside Of Designated Areas Endorsement IDCG 31 2007", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_EndorsementExcludingBodilyInjuryClaimByYourEmployeeAgainstAnAdditionalInsuredIDCG312008("Endorsement Excluding Bodily Injury Claim By Your Employee Against An Additional Insured IDCG 31 2008", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_FarmMachineryOperationsByContractorsExclusionEndorsementIDCG312009("Farm Machinery Operations By Contractors Exclusion Endorsement IDCG 31 2009", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_FertilizerDistributorsAndDealersExclusionEndorsementIDCG312010("Fertilizer Distributors And Dealers Exclusion Endorsement IDCG 31 2010", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_SupplementalExtendedReportingPeriodEndorsementIDCG312011("Supplemental Extended Reporting Period Endorsement IDCG 31 2011", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_CommercialGeneralLiabilityManuscriptEndorsementIDCG312012("Commercial General Liability Manuscript Endorsement IDCG 31 2012", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_EmploymentPracticeLiabilityInsuranceIDCG312013("Employment Practice Liability Insurance IDCG 31 2013", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_EmploymentPracticesLiabilitySupplementalDeclarationsIDCW030001("Employment Practices Liability Supplemental Declarations IDCW 03 0001", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true),
		CPP_GL_EmploymentPracticesLiabilityWarrantyStatementIDCW320001("Employment Practices Liability Warranty Statement IDCW 32 0001", repository.gw.enums.ProductLineType.CPP, repository.gw.enums.LineSelection.GeneralLiabilityLineCPP, true);

		
		
			
		
		String eventName;
		ApplicationOrCenter application = ApplicationOrCenter.PolicyCenter;
		repository.gw.enums.ProductLineType product;
		repository.gw.enums.LineSelection line;
		boolean directlyTriggersDocuments;
		
		PolicyCenter(String eventName, repository.gw.enums.ProductLineType product, repository.gw.enums.LineSelection line, boolean directlyTriggersDocuments) {
			this.eventName = eventName;
			this.product = product;
			this.line = line;
			this.directlyTriggersDocuments = directlyTriggersDocuments;
		}
		
		public String getEventName() {
			return this.eventName;
		}
		
		public ApplicationOrCenter getApplication() {
			return this.application;
		}
		
		public repository.gw.enums.ProductLineType getProduct() {
			return this.product;
		}
		
		public repository.gw.enums.LineSelection getLine() {
			return this.line;
		}
		
		public boolean getDirectlyTriggersDocuments() {
			return this.directlyTriggersDocuments;
		}
		
		public static ArrayList<String> getEventNamesFromListOfEvents(ArrayList<DocFormEvents.PolicyCenter> listOfEvents) {
			ArrayList<String> toReturn = new ArrayList<String>();
			for(DocFormEvents.PolicyCenter dfe : listOfEvents) {
				toReturn.add(dfe.getEventName());
			}
			
			return toReturn;
		}
		
	}

	public enum BillingCenter {
		
		PaymentReversalAtFaultSecond("BC: PaymentReversal_AtFaultSecond"),
		PaymentReversalAtFaultThird("BC: PaymentReversal_AtFaultThird");
		
		String eventName;
		ApplicationOrCenter application = ApplicationOrCenter.BillingCenter;
		repository.gw.enums.ProductLineType product;
		repository.gw.enums.LineSelection line;
		
		BillingCenter(String eventName) {
			this.eventName = eventName;
		}
		
		public String getEventName() {
			return this.eventName;
		}
		
		public ApplicationOrCenter getApplication() {
			return this.application;
		}
		
		public ProductLineType getProduct() {
			return this.product;
		}
		
		public LineSelection getLine() {
			return this.line;
		}
		
		public static ArrayList<String> getEventNamesFromListOfEvents(ArrayList<DocFormEvents.BillingCenter> listOfEvents) {
			ArrayList<String> toReturn = new ArrayList<String>();
			for(DocFormEvents.BillingCenter dfe : listOfEvents) {
				toReturn.add(dfe.getEventName());
			}
			
			return toReturn;
		}
		
	}	
	
}

package repository.pc.workorders.generic;


import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8Select;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;

public class GenericWorkorderGeneralLiabilityCoverages_ExclusionsConditions extends BasePage {

    private TableUtils tableUtils;
    private WebDriver driver;

    public GenericWorkorderGeneralLiabilityCoverages_ExclusionsConditions(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    Guidewire8Checkbox checkbox_AmendmentOfLiquorLiabilityExclusionCG2150() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Amendment Of Liquor Liability Exclusion CG 21 50')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_CommercialGeneralLiabilityManuscriptEndorsementIDCG312012_Exclusion() {
        return new Guidewire8Checkbox(driver, "//span[contains(text(),'Exclusions')]/ancestor::div[contains(@id, ':ExclDV_ref')]//div[contains(text(), 'Commercial General Liability Manuscript Endorsement IDCG 31 2012')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_CommunicableDiseaseExclusionCG2132() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Communicable Disease Exclusion CG 21 32')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_EndorsementExcludingBodilyInjuryClaimByYourEmployeeAgainstAnAdditionalInsuredIDCG312008() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Endorsement Excluding Bodily Injury Claim By Your Employee Against An Additional Insured IDCG 31 2008')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_AdultDayCareCentersCG2287() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Adult Day Care Centers CG 22 87')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_AllHazardsInConnectionWithDesignatedPremisesCG2100() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - All Hazards In Connection With Designated Premises CG 21 00')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_AthleticOrSportsParticipantsCG2101() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Athletic Or Sports Participants CG 21 01')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_CampsOrCampgroundsCG2239() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Camps Or Campgrounds CG 22 39')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_ConstructionManagementErrorsAndOmissionsCG2234() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Construction Management Errors And Omissions CG 22 34')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_Contractors_ProfessionalLiabilityCG2279() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Contractors - Professional Liability CG 22 79')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_CorporalPunishmentCG2230() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Corporal Punishment CG 22 30')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_CounselingServicesCG2157() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Counseling Services CG 21 57')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_CoverageC_MedicalPaymentsCG2135() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Coverage C - Medical Payments CG 21 35')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_DamageToWorkPerformedBySubcontractorsOnYourBehalfCG2294() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Damage To Work Performed By Subcontractors On Your Behalf CG 22 94')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_DescribedHazardsCarnivalsCircusesAndFairsCG2258() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Described Hazards (Carnivals, Circuses And Fairs) CG 22 58')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_DesignatedOngoingOperationsCG2153() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Designated Ongoing Operations CG 21 53')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_DesignatedProductsCG2133() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Designated Products CG 21 33')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_DesignatedProfessionalServicesCG2116() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Designated Professional Services CG 21 16')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_DesignatedWorkCG2134() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Designated Work CG 21 34')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_DiagnosticTestingLaboratoriesCG2159() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Diagnostic Testing Laboratories CG 21 59')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_EngineersArchitectsOrSurveyorsProfessionalLiabilityCG2243() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Engineers, Architects Or Surveyors Professional Liability CG 22 43')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_ErroneousDeliveryOrMixtureAndResultingFailureOfSeedToGerminate_SeedMerchantsCG2281() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Erroneous Delivery Or Mixture And Resulting Failure Of Seed To Germinate - Seed Merchants CG 22 81')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_FailureToSupplyCG2250() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Failure To Supply CG 22 50')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_FiduciaryOrRepresentativeLiabilityOfFinancialInstitutionsCG2238() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Fiduciary Or Representative Liability Of Financial Institutions CG 22 38')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_FinancialServicesCG2152() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Financial Services CG 21 52')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_FuneralServicesCG2156() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Funeral Services CG 21 56')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_InspectionAppraisalAndSurveyCompaniesCG2224() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Inspection, Appraisal And Survey Companies CG 22 24')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_InsuranceAndRelatedOperationsCG2248() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Insurance And Related Operations CG 22 48')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_IntercompanyProductsSuitsCG2141() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Intercompany Products Suits CG 21 41')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_InternetServiceProvidersAndInternetAccessProvidersErrorsAndOmissionsCG2298() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Internet Service Providers And Internet Access Providers Errors And Omissions CG 22 98')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_LaundryAndDryCleaningDamageCG2253() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Laundry And Dry Cleaning Damage CG 22 53')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_MedicalPaymentsToChildrenDayCareCentersCG2240() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Medical Payments To Children Day Care Centers CG 22 40')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_NewEntitiesCG2136() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - New Entities CG 21 36')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_OilOrGasProducingOperationsCG2273() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Oil Or Gas Producing Operations CG 22 73')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_PersonalAndAdvertisingInjuryCG2138() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Personal And Advertising Injury CG 21 38')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_ProductsAndProfessionalServicesDruggistsCG2236() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Products And Professional Services (Druggists) CG 22 36')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_ProductsAndProfessionalServicesOpticalAndHearingAidEstablishmentsCG2237() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Products And Professional Services (Optical And Hearing Aid Establishments) CG 22 37')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_Products_CompletedOperationsHazardCG2104() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Products-Completed Operations Hazard CG 21 04')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_ProfessionalServices_BloodBanksCG2232() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Professional Services - Blood Banks CG 22 32')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_ProfessionalVeterinarianServicesCG2158() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Professional Veterinarian Services CG 21 58')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_RealEstateAgentsOrBrokersErrorsOrOmissionsCG2301() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Real Estate Agents Or Brokers Errors Or Omissions CG 23 01')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_RollingStock_RailroadConstructionCG2246() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Rolling Stock - Railroad Construction CG 22 46')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_ServicesFurnishedByHealthCareProvidersCG2244() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Services Furnished By Health Care Providers CG 22 44')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_SpecifiedTherapeuticOrCosmeticServicesCG2245() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Specified Therapeutic Or Cosmetic Services CG 22 45')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_TestingOrConsultingErrorsAndOmissionsCG2233() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Testing Or Consulting Errors And Omissions CG 22 33')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_UndergroundResourcesAndEquipmentCG2257() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Underground Resources And Equipment CG 22 57')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_Exclusion_VolunteerWorkersCG2166() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Volunteer Workers CG 21 66')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_ExclusionOfCoverageForStructuresBuiltOutsideOfDesignatedAreasEndorsementIDCG312007() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion Of Coverage For Structures Built Outside Of Designated Areas Endorsement IDCG 31 2007')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_FarmMachineryOperationsByContractorsExclusionEndorsementIDCG312009() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Farm Machinery Operations By Contractors Exclusion Endorsement IDCG 31 2009')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_FertilizerDistributorsAndDealersExclusionEndorsementIDCG312010() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Fertilizer Distributors And Dealers Exclusion Endorsement IDCG 31 2010')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_LimitationOfCoverage_RealEstateOperationsCG2260() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Limitation Of Coverage - Real Estate Operations CG 22 60')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_LimitedExclusion_PersonalAndAdvertisingInjury_LawyersCG2296() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Limited Exclusion - Personal And Advertising Injury -Lawyers CG 22 96')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_MisdeliveryOfLiquidProductsCoverageCG2266() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Misdelivery Of Liquid Products Coverage CG 22 66')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_ProfessionalLiabilityExclusion_ComputerDataProcessingCG2277() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Professional Liability Exclusion - Computer Data Processing CG 22 77')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_ProfessionalLiabilityExclusion_ElectronicDataProcessingServicesAndComputerConsultingOrProgrammingServicesCG2288() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Professional Liability Exclusion - Electronic Data Processing Services And Computer Consulting Or Programming Services CG 22 88')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_ProfessionalLiabilityExclusion_SpasOrPersonalEnhancementFacilitiesCG2290() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Professional Liability Exclusion - Spas Or Personal Enhancement Facilities CG 22 90')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_ProfessionalLiabilityExclusion_WebSiteDesignersCG2299() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Professional Liability Exclusion - Web Site Designers CG 22 99')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_RealEstatePropertyManagedCG2270() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Real Estate Property Managed CG 22 70')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_TotalPollutionExclusionEndorsementCG2149() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Total Pollution Exclusion Endorsement CG 21 49')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_TotalPollutionExclusionwithAHostileFireExceptionCG2155() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Total Pollution Exclusion with A Hostile Fire Exception CG 21 55')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_DesignatedProfessionalServices_CG_21_16() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclusion - Designated Professional Services CG 21 16')]/preceding-sibling::table");
    }

    @FindBy(xpath = "//div[contains(text(), 'Exclusion - Designated Professional Services CG 21 16')]/ancestor::legend/following-sibling::div/descendant::span[contains(text(), 'Description of Professional Services')]/ancestor::div[5]/following-sibling::div/div/table/tbody/child::tr/child::td[2]/div")
    public WebElement editbox_DesignatedProfessionalServices_CG_21_16;

    @FindBy(xpath = "//div[contains(text(), 'Exclusion - Designated Professional Services CG 21 16')]/ancestor::fieldset//span[contains(@id,':Add-btnInnerEl')]")
    private WebElement link_Add_DesignatedProfessionalServices_CG_21_16;

    Guidewire8Checkbox checkbox_Amendment_TravelAgencyToursLimitationOfCoverageCG2228() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Amendment - Travel Agency Tours (Limitation Of Coverage) CG 22 28')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_BoatsCG2412() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Boats CG 24 12')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_CanoesOrRowboatsCG2416() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Canoes Or Rowboats CG 24 16')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_CollegesOrSchoolsLimitedFormCG2271() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Colleges Or Schools (Limited Form) CG 22 71')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_CommercialGeneralLiabilityManuscriptEndorsementIDCG312012_Conditions() {
        return new Guidewire8Checkbox(driver, "//span[contains(text(),'Conditions')]/ancestor::div[contains(@id, ':CondDV_ref')]//div[contains(text(), 'Commercial General Liability Manuscript Endorsement IDCG 31 2012')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_ExcessProvision_VendorsCG2410() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Excess Provision - Vendors CG 24 10')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_GovernmentalSubdivisionsCG2409() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Governmental Subdivisions CG 24 09')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_LimitationOfCoverageToDesignatedPremisesOrProjectCG2144() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Limitation Of Coverage To Designated Premises, Project Or Operation CG 21 44')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_LimitationOfCoverageToInsuredPremisesCG2806() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Limitation Of Coverage To Insured Premises CG 28 06')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_OperationOfCustomersAutosOnParticularPremisesCG2268() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Operation Of Customers Autos On Particular Premises CG 22 68')]/preceding-sibling::table");
    }


    @FindBy(xpath = "//div[contains(text(), 'Products/Completed Operations Hazard Redefined CG 24 07')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Description of Premises and Operations')]/parent::td/following-sibling::td/textarea")
    public WebElement editbox_DescriptionofPremisesAndOperations;

    @FindBy(xpath = "//div[contains(text(), 'Exclusion - Athletic Or Sports Participants CG 21 01')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Description Of Operations')]/parent::td/following-sibling::td/textarea")
    public WebElement editbox_DescriptionofOperations_AthleticSports;


    Guidewire8Checkbox checkbox_SnowPlowOperationsCoverageCG2292() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Snow Plow Operations Coverage CG 22 92')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_WaiverOfTransferOfRightsOfRecoveryAgainstOthersToUsCG2404() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Waiver Of Transfer Of Rights Of Recovery Against Others To Us CG 24 04')]/preceding-sibling::table");
    }

    @FindBy(xpath = "//div[contains(text(), 'Limitation Of Coverage To Insured Premises CG 28 06')]/ancestor::fieldset//span[contains(@id,':Add-btnInnerEl')]")
    private WebElement link_Add_LimitationOfCoverageToInsuredPremises;

    @FindBy(xpath = "//div[contains(text(), 'Limitation Of Coverage To Insured Premises CG 28 06')]/ancestor::fieldset//span[contains(@id,':Remove-btnInnerEl')]")
    private WebElement link_Remove_LimitationOfCoverageToInsuredPremises;

    @FindBy(xpath = "//div[contains(text(), 'Limitation Of Coverage To Insured Premises CG 28 06')]/ancestor::fieldset//div[contains(@id,':CoverageInputSet:CovPatternInputGroup:ScheduleInputSet')]")
    public WebElement table_LimitationOfCoverageToInsuredPremises;


    public void checkAmendmentOfLiquorLiabilityExclusionCG2150(boolean checked) {
        checkbox_AmendmentOfLiquorLiabilityExclusionCG2150().select(checked);
    }


    public void checkCommercialGeneralLiabilityManuscriptEndorsementIDCG312012_Exclusion(boolean checked) {
        checkbox_CommercialGeneralLiabilityManuscriptEndorsementIDCG312012_Exclusion().select(checked);
    }


    public void checkCommunicableDiseaseExclusionCG2132(boolean checked) {
        checkbox_CommunicableDiseaseExclusionCG2132().select(checked);
    }


    public void checkEndorsementExcludingBodilyInjuryClaimByYourEmployeeAgainstAnAdditionalInsuredIDCG312008(boolean checked) {
        checkbox_EndorsementExcludingBodilyInjuryClaimByYourEmployeeAgainstAnAdditionalInsuredIDCG312008().select(checked);
    }


    public void checkExclusion_AdultDayCareCentersCG2287(boolean checked) {
        checkbox_Exclusion_AdultDayCareCentersCG2287().select(checked);
    }


    public void checkExclusion_AllHazardsInConnectionWithDesignatedPremisesCG2100(boolean checked) {
        checkbox_Exclusion_AllHazardsInConnectionWithDesignatedPremisesCG2100().select(checked);
    }


    public void checkExclusion_AthleticOrSportsParticipantsCG2101(boolean checked) {
        checkbox_Exclusion_AthleticOrSportsParticipantsCG2101().select(checked);
    }

    @FindBy(xpath = "//div[contains(text(), 'Exclusion - Athletic Or Sports Participants CG 21 01')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Description Of Operations')]/parent::td/following-sibling::td/textarea")
    public WebElement editbox_AthleticOrSportsParticipantsDescription;


    public void setExclusion_AthleticOrSportsParticipantsCG2101_Description(String description) {
        setText(editbox_AthleticOrSportsParticipantsDescription, description);
    }


    public void checkExclusion_CampsOrCampgroundsCG2239(boolean checked) {
        checkbox_Exclusion_CampsOrCampgroundsCG2239().select(checked);
    }


    public void checkExclusion_ConstructionManagementErrorsAndOmissionsCG2234(boolean checked) {
        checkbox_Exclusion_ConstructionManagementErrorsAndOmissionsCG2234().select(checked);
    }


    public void checkExclusion_Contractors_ProfessionalLiabilityCG2279(boolean checked) {
        checkbox_Exclusion_Contractors_ProfessionalLiabilityCG2279().select(checked);
    }


    public void checkExclusion_CorporalPunishmentCG2230(boolean checked) {
        checkbox_Exclusion_CorporalPunishmentCG2230().select(checked);
    }


    public void checkExclusion_CounselingServicesCG2157(boolean checked) {
        checkbox_Exclusion_CounselingServicesCG2157().select(checked);
    }


    public void checkExclusion_CoverageC_MedicalPaymentsCG2135(boolean checked) {
        checkbox_Exclusion_CoverageC_MedicalPaymentsCG2135().select(checked);
    }


    public void checkExclusion_DamageToWorkPerformedBySubcontractorsOnYourBehalfCG2294(boolean checked) {
        checkbox_Exclusion_DamageToWorkPerformedBySubcontractorsOnYourBehalfCG2294().select(checked);
    }


    public void checkExclusion_DescribedHazardsCarnivalsCircusesAndFairsCG2258(boolean checked) {
        checkbox_Exclusion_DescribedHazardsCarnivalsCircusesAndFairsCG2258().select(checked);
    }


    public void checkExclusion_DesignatedOngoingOperationsCG2153(boolean checked) {
        checkbox_Exclusion_DesignatedOngoingOperationsCG2153().select(checked);
    }


    public void checkExclusion_DesignatedProductsCG2133(boolean checked) {
        checkbox_Exclusion_DesignatedProductsCG2133().select(checked);
    }


    public void checkExclusion_DesignatedProfessionalServicesCG2116(boolean checked) {
        checkbox_Exclusion_DesignatedProfessionalServicesCG2116().select(checked);
    }


    public void checkExclusion_DesignatedWorkCG2134(boolean checked) {
        checkbox_Exclusion_DesignatedWorkCG2134().select(checked);
    }


    public void checkExclusion_DiagnosticTestingLaboratoriesCG2159(boolean checked) {
        checkbox_Exclusion_DiagnosticTestingLaboratoriesCG2159().select(checked);
    }


    public void checkExclusion_EngineersArchitectsOrSurveyorsProfessionalLiabilityCG2243(boolean checked) {
        checkbox_Exclusion_EngineersArchitectsOrSurveyorsProfessionalLiabilityCG2243().select(checked);
    }


    public void checkExclusion_ErroneousDeliveryOrMixtureAndResultingFailureOfSeedToGerminate_SeedMerchantsCG2281(boolean checked) {
        checkbox_Exclusion_ErroneousDeliveryOrMixtureAndResultingFailureOfSeedToGerminate_SeedMerchantsCG2281().select(checked);
    }


    public void checkExclusion_FailureToSupplyCG2250(boolean checked) {
        checkbox_Exclusion_FailureToSupplyCG2250().select(checked);
    }


    public void checkExclusion_FiduciaryOrRepresentativeLiabilityOfFinancialInstitutionsCG2238(boolean checked) {
        checkbox_Exclusion_FiduciaryOrRepresentativeLiabilityOfFinancialInstitutionsCG2238().select(checked);
    }


    public void checkExclusion_FinancialServicesCG2152(boolean checked) {
        checkbox_Exclusion_FinancialServicesCG2152().select(checked);
    }


    public void checkExclusion_FuneralServicesCG2156(boolean checked) {
        checkbox_Exclusion_FuneralServicesCG2156().select(checked);
    }


    public void checkExclusion_InspectionAppraisalAndSurveyCompaniesCG2224(boolean checked) {
        checkbox_Exclusion_InspectionAppraisalAndSurveyCompaniesCG2224().select(checked);
    }


    public void checkExclusion_InsuranceAndRelatedOperationsCG2248(boolean checked) {
        checkbox_Exclusion_InsuranceAndRelatedOperationsCG2248().select(checked);
    }


    public void checkExclusion_IntercompanyProductsSuitsCG2141(boolean checked) {
        checkbox_Exclusion_IntercompanyProductsSuitsCG2141().select(checked);
    }


    public void checkExclusion_InternetServiceProvidersAndInternetAccessProvidersErrorsAndOmissionsCG2298(boolean checked) {
        checkbox_Exclusion_InternetServiceProvidersAndInternetAccessProvidersErrorsAndOmissionsCG2298().select(checked);
    }


    public void checkExclusion_LaundryAndDryCleaningDamageCG2253(boolean checked) {
        checkbox_Exclusion_LaundryAndDryCleaningDamageCG2253().select(checked);
    }


    public void checkMedicalPaymentsToChildrenDayCareCentersCG2240(boolean checked) {
        checkbox_Exclusion_MedicalPaymentsToChildrenDayCareCentersCG2240().select(checked);
    }


    public void checkExclusion_NewEntitiesCG2136(boolean checked) {
        checkbox_Exclusion_NewEntitiesCG2136().select(checked);
    }


    public void checkExclusion_OilOrGasProducingOperationsCG2273(boolean checked) {
        checkbox_Exclusion_OilOrGasProducingOperationsCG2273().select(checked);
    }


    public void checkExclusion_PersonalAndAdvertisingInjuryCG2138(boolean checked) {
        checkbox_Exclusion_PersonalAndAdvertisingInjuryCG2138().select(checked);
    }


    public void checkExclusion_ProductsAndProfessionalServicesDruggistsCG2236(boolean checked) {
        checkbox_Exclusion_ProductsAndProfessionalServicesDruggistsCG2236().select(checked);
    }


    public void checkExclusion_ProductsAndProfessionalServicesOpticalAndHearingAidEstablishmentsCG2237(boolean checked) {
        checkbox_Exclusion_ProductsAndProfessionalServicesOpticalAndHearingAidEstablishmentsCG2237().select(checked);
    }


    public void checkExclusion_Products_CompletedOperationsHazardCG2104(boolean checked) {
        checkbox_Exclusion_Products_CompletedOperationsHazardCG2104().select(checked);
    }


    public void checkExclusion_ProfessionalServices_BloodBanksCG2232(boolean checked) {
        checkbox_Exclusion_ProfessionalServices_BloodBanksCG2232().select(checked);
    }


    public void checkExclusion_ProfessionalVeterinarianServicesCG2158(boolean checked) {
        checkbox_Exclusion_ProfessionalVeterinarianServicesCG2158().select(checked);
    }


    public void checkExclusion_RealEstateAgentsOrBrokersErrorsOrOmissionsCG2301(boolean checked) {
        checkbox_Exclusion_RealEstateAgentsOrBrokersErrorsOrOmissionsCG2301().select(checked);
    }


    public void checkExclusion_RollingStock_RailroadConstructionCG2246(boolean checked) {
        checkbox_Exclusion_RollingStock_RailroadConstructionCG2246().select(checked);
    }


    public void checkExclusion_ServicesFurnishedByHealthCareProvidersCG2244(boolean checked) {
        checkbox_Exclusion_ServicesFurnishedByHealthCareProvidersCG2244().select(checked);
    }


    public void checkExclusion_SpecifiedTherapeuticOrCosmeticServicesCG2245(boolean checked) {
        checkbox_Exclusion_SpecifiedTherapeuticOrCosmeticServicesCG2245().select(checked);
    }


    public void checkExclusion_TestingOrConsultingErrorsAndOmissionsCG2233(boolean checked) {
        checkbox_Exclusion_TestingOrConsultingErrorsAndOmissionsCG2233().select(checked);
    }


    public void checkExclusion_UndergroundResourcesAndEquipmentCG2257(boolean checked) {
        checkbox_Exclusion_UndergroundResourcesAndEquipmentCG2257().select(checked);
    }


    public void checkExclusion_VolunteerWorkersCG2166(boolean checked) {
        checkbox_Exclusion_VolunteerWorkersCG2166().select(checked);
    }


    public void checkExclusionOfCoverageForStructuresBuiltOutsideOfDesignatedAreasEndorsementIDCG312007(boolean checked) {
        checkbox_ExclusionOfCoverageForStructuresBuiltOutsideOfDesignatedAreasEndorsementIDCG312007().select(checked);
    }


    public void checkFarmMachineryOperationsByContractorsExclusionEndorsementIDCG312009(boolean checked) {
        checkbox_FarmMachineryOperationsByContractorsExclusionEndorsementIDCG312009().select(checked);
    }


    public void checkFertilizerDistributorsAndDealersExclusionEndorsementIDCG312010(boolean checked) {
        checkbox_FertilizerDistributorsAndDealersExclusionEndorsementIDCG312010().select(checked);
    }


    public void checkLimitationOfCoverage_RealEstateOperationsCG2260(boolean checked) {
        checkbox_LimitationOfCoverage_RealEstateOperationsCG2260().select(checked);
    }


    public void checkLimitedExclusion_PersonalAndAdvertisingInjury_LawyersCG2296(boolean checked) {
        checkbox_LimitedExclusion_PersonalAndAdvertisingInjury_LawyersCG2296().select(checked);
    }


    public void checkMisdeliveryOfLiquidProductsCoverageCG2266(boolean checked) {
        checkbox_MisdeliveryOfLiquidProductsCoverageCG2266().select(checked);
    }


    public void checkProfessionalLiabilityExclusion_ComputerDataProcessingCG2277(boolean checked) {
        checkbox_ProfessionalLiabilityExclusion_ComputerDataProcessingCG2277().select(checked);
    }


    public void checkProfessionalLiabilityExclusion_ElectronicDataProcessingServicesAndComputerConsultingOrProgrammingServicesCG2288(boolean checked) {
        checkbox_ProfessionalLiabilityExclusion_ElectronicDataProcessingServicesAndComputerConsultingOrProgrammingServicesCG2288()
                .select(checked);
    }


    public void checkProfessionalLiabilityExclusion_SpasOrPersonalEnhancementFacilitiesCG2290(boolean checked) {
        checkbox_ProfessionalLiabilityExclusion_SpasOrPersonalEnhancementFacilitiesCG2290().select(checked);
    }


    public void checkProfessionalLiabilityExclusion_WebSiteDesignersCG2299(boolean checked) {
        checkbox_ProfessionalLiabilityExclusion_WebSiteDesignersCG2299().select(checked);
    }


    public void checkRealEstatePropertyManagedCG2270(boolean checked) {
        checkbox_RealEstatePropertyManagedCG2270().select(checked);
    }


    public void checkTotalPollutionExclusionEndorsementCG2149(boolean checked) {
        checkbox_TotalPollutionExclusionEndorsementCG2149().select(checked);
    }


    public void checkTotalPollutionExclusionwithAHostileFireExceptionCG2155(boolean checked) {
        checkbox_TotalPollutionExclusionwithAHostileFireExceptionCG2155().select(checked);
    }


    public void checkDesignatedProfessionalServices_CG_21_16(boolean checked) {
        checkbox_DesignatedProfessionalServices_CG_21_16().select(checked);
    }


    public void setDesignatedProfessionalServices_CG_21_16(String desc) {
        clickWhenClickable(link_Add_DesignatedProfessionalServices_CG_21_16);
        
        boolean scheduledItems = !finds(By.xpath("//div[contains(text(), 'Exclusion - Designated Professional Services CG 21 16')]/ancestor::legend/following-sibling::div/span/div/div/table/tbody/tr/td/div/child::div/div/table/tbody/tr")).isEmpty();
        if (!scheduledItems) {
            Assert.fail("SCHEDULED ITEM TABLE DID NOT ADD A NEW LINE AFTER CLICKING ADD");
        }
        clickWhenClickable(editbox_DesignatedProfessionalServices_CG_21_16);
        find(By.xpath("//textarea[contains(@name, 'c1')] | //input[contains(@name, 'c1')]")).sendKeys(Keys.chord(Keys.CONTROL + "a"));
        find(By.xpath("//textarea[contains(@name, 'c1')] | //input[contains(@name, 'c1')]")).sendKeys(desc);
        find(By.xpath("//div[contains(text(), 'Exclusion - Designated Professional Services CG 21 16')]")).click();
    }

    @FindBy(xpath = "//div[contains(text(), 'Exclusion - Explosion, Collapse And Underground Property Damage Hazard (Specified Operations) IDCG 31 2001')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Description Of Operations')]/parent::td/following-sibling::td/textarea")
    private WebElement editbox_ExplosionCollapseAndUndergroundPropertyDamageHazard_IDCG_31_2001_DescriptionOfOpperations;

    private Guidewire8Select ExplosionCollapseAndUndergroundPropertyDamageHazard_IDCG_31_2001_ExcludedHazards() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Exclusion - Explosion, Collapse And Underground Property Damage Hazard (Specified Operations) IDCG 31 2001')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Excluded Hazard(s)')]/parent::td/following-sibling::td/table");
    }

    public void setExplosionCollapseAndUndergroundPropertyDamageHazard_IDCG_31_2001_DescriptionOfOpperations(String description) {
        clickWhenClickable(editbox_ExplosionCollapseAndUndergroundPropertyDamageHazard_IDCG_31_2001_DescriptionOfOpperations);
        editbox_ExplosionCollapseAndUndergroundPropertyDamageHazard_IDCG_31_2001_DescriptionOfOpperations.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ExplosionCollapseAndUndergroundPropertyDamageHazard_IDCG_31_2001_DescriptionOfOpperations.sendKeys(description);
        clickProductLogo();
    }

    public void selectExplosionCollapseAndUndergroundPropertyDamageHazard_IDCG_31_2001_ExcludedHazards(String description) {
        Guidewire8Select mySelect = ExplosionCollapseAndUndergroundPropertyDamageHazard_IDCG_31_2001_ExcludedHazards();
        mySelect.selectByVisibleText("Explosion and Underground Property Damage Hazard");
    }


    // END OF EXCLUTIONS

    // CONDITIONS

    public void checkAmendment_TravelAgencyToursLimitationOfCoverageCG2228(boolean checked) {
        checkbox_Amendment_TravelAgencyToursLimitationOfCoverageCG2228().select(checked);
    }


    public void checkBoatsCG2412(boolean checked) {
        checkbox_BoatsCG2412().select(checked);
    }


    public void checkCanoesOrRowboatsCG2416(boolean checked) {
        checkbox_CanoesOrRowboatsCG2416().select(checked);
    }


    public void checkCollegesOrSchoolsLimitedFormCG2271(boolean checked) {
        checkbox_CollegesOrSchoolsLimitedFormCG2271().select(checked);
    }


    public void checkCommercialGeneralLiabilityManuscriptEndorsementIDCG312012_Conditions(boolean checked) {
        checkbox_CommercialGeneralLiabilityManuscriptEndorsementIDCG312012_Conditions().select(checked);
    }


    public void checkExcessProvision_VendorsCG2410(boolean checked) {
        checkbox_ExcessProvision_VendorsCG2410().select(checked);
    }


    public void checkGovernmentalSubdivisionsCG2409(boolean checked) {
        checkbox_GovernmentalSubdivisionsCG2409().select(checked);
    }


    public void checkLimitationOfCoverageToDesignatedPremisesOrProjectCG2144(boolean checked) {
        checkbox_LimitationOfCoverageToDesignatedPremisesOrProjectCG2144().select(checked);
    }


    public void checkLimitationOfCoverageToInsuredPremisesCG2806(boolean checked) {
        checkbox_LimitationOfCoverageToInsuredPremisesCG2806().select(checked);
    }


    public void checkOperationOfCustomersAutosOnParticularPremisesCG2268(boolean checked) {
        checkbox_OperationOfCustomersAutosOnParticularPremisesCG2268().select(checked);
    }


    public void setProductsCompletedOperationsHazardRedefined_CG_24_07(String desc) {
        clickWhenClickable(editbox_DescriptionofPremisesAndOperations);
        editbox_DescriptionofPremisesAndOperations.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_DescriptionofPremisesAndOperations.sendKeys(desc);
    }


    public void setExclusionAthleticOrSportsParticipants_CG_21_01(String desc) {
        clickWhenClickable(editbox_DescriptionofOperations_AthleticSports);
        editbox_DescriptionofOperations_AthleticSports.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_DescriptionofOperations_AthleticSports.sendKeys(desc);
    }


    public void checkSnowPlowOperationsCoverageCG2292(boolean checked) {
        checkbox_SnowPlowOperationsCoverageCG2292().select(checked);
    }


    public void checkWaiverOfTransferOfRightsOfRecoveryAgainstOthersToUsCG2404(boolean checked) {
        checkbox_WaiverOfTransferOfRightsOfRecoveryAgainstOthersToUsCG2404().select(checked);
    }

    // * Limitation Of Coverage To Insured Premises CG 28 06

    public void setLimitationOfCoverageToInsuredPremises_CG_28_06(String Description, PolicyLocation location) {
        
        clickWhenClickable(link_Add_LimitationOfCoverageToInsuredPremises);
        
        tableUtils.clickCellInTableByRowAndColumnName(table_LimitationOfCoverageToInsuredPremises, tableUtils.getRowCount(table_LimitationOfCoverageToInsuredPremises), "Description");
        tableUtils.setValueForCellInsideTable(table_LimitationOfCoverageToInsuredPremises, "c1", Description);
        
    }


    @FindBy(xpath = "//div[contains(text(), 'Boats CG 24 12')]/ancestor::legend/following-sibling::div/descendant::table[1]/tbody[1]/tr[1]/td[1]/div[1]")
    public WebElement table_BoatsCG2412;

    public void clickAddBoat() {
        table_BoatsCG2412.findElement(By.xpath(".//span[contains(@id, ':Add-btnEl')]")).click();
    }


    //Add values for the Boat Endorsement
    public void addBoat() {
        clickAddBoat();
        
        tableUtils.clickCellInTableByRowAndColumnName(table_BoatsCG2412, tableUtils.getRowCount(table_BoatsCG2412), "Body Type");
        tableUtils.setValueForCellInsideTable(table_BoatsCG2412, "c9", "Flat");
        
        tableUtils.clickCellInTableByRowAndColumnName(table_BoatsCG2412, tableUtils.getRowCount(table_BoatsCG2412), "Length");
        tableUtils.setValueForCellInsideTable(table_BoatsCG2412, "c8", "12");
        
        tableUtils.clickCellInTableByRowAndColumnName(table_BoatsCG2412, tableUtils.getRowCount(table_BoatsCG2412), "Limit");
        tableUtils.setValueForCellInsideTable(table_BoatsCG2412, "c7", "10000");
        
        tableUtils.clickCellInTableByRowAndColumnName(table_BoatsCG2412, tableUtils.getRowCount(table_BoatsCG2412), "Model");
        tableUtils.setValueForCellInsideTable(table_BoatsCG2412, "c6", "Model");
        
        tableUtils.clickCellInTableByRowAndColumnName(table_BoatsCG2412, tableUtils.getRowCount(table_BoatsCG2412), "Make");
        tableUtils.setValueForCellInsideTable(table_BoatsCG2412, "c5", "Make");
        
        tableUtils.clickCellInTableByRowAndColumnName(table_BoatsCG2412, tableUtils.getRowCount(table_BoatsCG2412), "VIN/ Serial #");
        tableUtils.setValueForCellInsideTable(table_BoatsCG2412, "c4", "991VFG" + NumberUtils.generateRandomNumberDigits(3));
        
        tableUtils.clickCellInTableByRowAndColumnName(table_BoatsCG2412, tableUtils.getRowCount(table_BoatsCG2412), "Year");
        tableUtils.setValueForCellInsideTable(table_BoatsCG2412, "c3", "2000");
        
        tableUtils.clickCellInTableByRowAndColumnName(table_BoatsCG2412, tableUtils.getRowCount(table_BoatsCG2412), "Description");
        tableUtils.setValueForCellInsideTable(table_BoatsCG2412, "c2", "Something other than Jon being Dumb.");
        
    }


    //Add values for the Boat Endorsement
    public void addBoat(String bodyType, int length, int limit, String model, String make, String vin, int year, String description) {
        clickAddBoat();
        
        tableUtils.clickCellInTableByRowAndColumnName(table_BoatsCG2412, tableUtils.getRowCount(table_BoatsCG2412), "Body Type");
        tableUtils.setValueForCellInsideTable(table_BoatsCG2412, "c9", bodyType);
        
        tableUtils.clickCellInTableByRowAndColumnName(table_BoatsCG2412, tableUtils.getRowCount(table_BoatsCG2412), "Length");
        tableUtils.setValueForCellInsideTable(table_BoatsCG2412, "c8", String.valueOf(length));
        
        tableUtils.clickCellInTableByRowAndColumnName(table_BoatsCG2412, tableUtils.getRowCount(table_BoatsCG2412), "Limit");
        tableUtils.setValueForCellInsideTable(table_BoatsCG2412, "c7", String.valueOf(limit));
        
        tableUtils.clickCellInTableByRowAndColumnName(table_BoatsCG2412, tableUtils.getRowCount(table_BoatsCG2412), "Model");
        tableUtils.setValueForCellInsideTable(table_BoatsCG2412, "c6", model);
        
        tableUtils.clickCellInTableByRowAndColumnName(table_BoatsCG2412, tableUtils.getRowCount(table_BoatsCG2412), "Make");
        tableUtils.setValueForCellInsideTable(table_BoatsCG2412, "c5", make);
        
        tableUtils.clickCellInTableByRowAndColumnName(table_BoatsCG2412, tableUtils.getRowCount(table_BoatsCG2412), "VIN/ Serial #");
        tableUtils.setValueForCellInsideTable(table_BoatsCG2412, "c4", vin + NumberUtils.generateRandomNumberDigits(3));
        
        tableUtils.clickCellInTableByRowAndColumnName(table_BoatsCG2412, tableUtils.getRowCount(table_BoatsCG2412), "Year");
        tableUtils.setValueForCellInsideTable(table_BoatsCG2412, "c3", String.valueOf(year));
        
        tableUtils.clickCellInTableByRowAndColumnName(table_BoatsCG2412, tableUtils.getRowCount(table_BoatsCG2412), "Description");
        tableUtils.setValueForCellInsideTable(table_BoatsCG2412, "c2", description);
        
    }


}



























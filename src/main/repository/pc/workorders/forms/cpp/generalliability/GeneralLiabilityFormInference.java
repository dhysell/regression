package repository.pc.workorders.forms.cpp.generalliability;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import persistence.globaldatarepo.entities.Glforms;
import persistence.globaldatarepo.helpers.GLClassCodeHelper;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.AdditionalInsuredTypeGL;
import repository.gw.enums.GeneralLiability.StandardCoverages.CG0001_PersonalAdvertisingInjury;
import repository.gw.enums.GeneralLiabilityForms;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPGLCoveragesAdditionalInsureds;
import repository.gw.generate.custom.CPPGeneralLiabilityExposures;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.NumberUtils;

import java.util.ArrayList;
import java.util.List;

public class GeneralLiabilityFormInference extends BasePage {


    private WebDriver driver;

    public GeneralLiabilityFormInference(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    List<Glforms> formsList = new ArrayList<Glforms>();


    @SuppressWarnings("serial")
    public  GeneratePolicy CreateFormsPolicyObject(List<GeneralLiabilityForms> formsList) throws Exception {
        GeneratePolicy myPolicy = new GeneratePolicy(driver);

        if (myPolicy.commercialPackage.locationList == null) {
            myPolicy.commercialPackage.locationList = new ArrayList<PolicyLocation>() {{
                this.add(new PolicyLocation(new AddressInfo(true), true));
            }};
        }

        for (GeneralLiabilityForms form : formsList) {
            switch (form) {

                //Liquor Liability Coverage Form CG 00 33
                case LiquorLiabilityCoverageForm_CG_00_33:
                    //� Available when one of the following class codes are selected: 70412, 58161, 50911, 59211, 58165, or 58166.
                    List<String> classCodes = new ArrayList<String>() {{
                        this.add("70412");
                        this.add("58161");
                        this.add("50911");
                        this.add("59211");
                        this.add("58165");
                        this.add("58166");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;
                //Deductible Liability Insurance CG 03 00
                case DeductibleLiabilityInsurance_CG_03_00:
                    //Deductible Liability Insurance CG 03 00

                    break;

                //Additional Insured - Club Members CG 20 02
                case AdditionalInsured_ClubMembers_CG_20_02:
                    //� This becomes required when one of the following class codes are selected: 41668, 41667, 41670, 41669, or 11138.
                    classCodes = new ArrayList<String>() {{
                        this.add("41668");
                        this.add("41667");
                        this.add("41670");
                        this.add("41669");
                        this.add("11138");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;
                //Additional Insured - Concessionaires Trading Under Your Name CG 20 03
                case AdditionalInsured_ConcessionairesTradingUnderYourName_CG_20_03:
                    //� Available when Additional Insured - Concessionaires Trading Under Your Name CG 20 03 is selected on the Screen New Additional Insured.
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getAdditionalInsuredslist().add(new CPPGLCoveragesAdditionalInsureds() {{
                        this.setType(AdditionalInsuredTypeGL.AdditionalInsured_ConcessionairesTradingUnderYourName_CG_20_03);
                    }});
                    break;
                //Additional Insured - Condominium Unit Owners CG 20 04
                case AdditionalInsured_CondominiumUnitOwners_CG_20_04:
                    //� This becomes required when one of the following class codes are selected: 62000, 62001, 62002, or 62003.
                    classCodes = new ArrayList<String>() {{
                        this.add("62000");
                        this.add("62001");
                        this.add("62002");
                        this.add("62003");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;
                //Additional Insured - Controlling Interest CG 20 05
                case AdditionalInsured_ControllingInterest_CG_20_05:
                    //� Available when Additional Insured - Controlling Interest CG 20 05 is selected on the Screen New Additional Insured.
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getAdditionalInsuredslist().add(new CPPGLCoveragesAdditionalInsureds() {{
                        this.setType(AdditionalInsuredTypeGL.AdditionalInsured_ControllingInterest_CG_20_05);
                    }});
                    break;

                //Additional Insured - Users Of Golfmobiles CG 20 08
                case AdditionalInsured_UsersOfGolfmobiles_CG_20_08:
                    //� This becomes required when one of the following class codes are selected: 11138, 44070, 44072, 45190, 45191, 45192, or 45193.
                    classCodes = new ArrayList<String>() {{
                        this.add("11138");
                        this.add("44070");
                        this.add("44072");
                        this.add("45190");
                        this.add("45191");
                        this.add("45192");
                        this.add("45193");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;
                //Additional Insured - Owners, Lessees Or Contractors - Scheduled Person Or Organization CG 20 10
                case AdditionalInsured_OwnersLesseesOrContractors_ScheduledPersonOrOrganization_CG_20_10:
                    //� Available when Additional Insured - Owners, Lessees Or Contractors - Scheduled Person Or Organization CG 20 10 is selected on the Screen New Additional Insured.
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getAdditionalInsuredslist().add(new CPPGLCoveragesAdditionalInsureds() {{
                        this.setType(AdditionalInsuredTypeGL.AdditionalInsured_OwnersLesseesOrContractors_ScheduledPersonOrOrganization_CG_20_10);
                    }});
                    break;
                //Additional Insured - Managers Or Lessors Of Premises CG 20 11
                case AdditionalInsured_ManagersOrLessorsOfPremises_CG_20_11:
                    //� Available when Additional Insured - Managers Or Lessors Of Premises CG 20 11 is selected on the Screen New Additional Insured.
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getAdditionalInsuredslist().add(new CPPGLCoveragesAdditionalInsureds() {{
                        this.setType(AdditionalInsuredTypeGL.AdditionalInsured_ManagersOrLessorsOfPremises_CG_20_11);
                    }});
                    break;
                //Additional Insured - State Or Governmental Agency Or Subdivision Or Political Subdivision - Permits Or Authorizations CG 20 12
                case AdditionalInsured_StateOrGovernmentalAgencyOrSubdivisionOrPoliticalSubdivision_PermitsOrAuthorizations_CG_20_12:
                    //� Available when Additional Insured - State Or Governmental Agency Or Subdivision Or Political Subdivision - Permits Or Authorizations Relating To Premises CG 20 13 is selected on the Screen New Additional Insured.
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getAdditionalInsuredslist().add(new CPPGLCoveragesAdditionalInsureds() {{
                        this.setType(AdditionalInsuredTypeGL.AdditionalInsured_StateOrGovernmentalAgencyOrSubdivisionOrPoliticalSubdivision_PermitsOrAuthorizations_CG_20_12);
                    }});
                    break;
                //Additional Insured - State Or Governmental Agency Or Subdivision Or Political Subdivision - Permits Or Authorizations Relating To Premises CG 20 13
                case AdditionalInsured_StateOrGovernmentalAgencyOrSubdivisionOrPoliticalSubdivision_PermitsOrAuthorizationsRelatingToPremises_CG_20_13:
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getAdditionalInsuredslist().add(new CPPGLCoveragesAdditionalInsureds() {{
                        this.setType(AdditionalInsuredTypeGL.AdditionalInsured_StateOrGovernmentalAgencyOrSubdivisionOrPoliticalSubdivision_PermitsOrAuthorizationsRelatingToPremises_CG_20_13);
                    }});
                    break;
                //Additional Insured - Vendors CG 20 15
                case AdditionalInsured_Vendors_CG_20_15:
                    //� Available when Additional Insured - Vendors CG 20 15 is selected on the Screen New Additional Insured.
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getAdditionalInsuredslist().add(new CPPGLCoveragesAdditionalInsureds() {{
                        this.setType(AdditionalInsuredTypeGL.AdditionalInsured_Vendors_CG_20_15);
                    }});
                    break;
                //Additional Insured - Townhouse Associations CG 20 17
                case AdditionalInsured_TownhouseAssociations_CG_20_17:
                    //� This becomes required when class code 68500 is selected.
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), "68500"));
                    break;
                //Additional Insured - Mortgagee, Assignee Or Receiver CG 20 18
                case AdditionalInsured_MortgageeAssigneeOrReceiver_CG_20_18:
                    //� Available when Additional Insured - Mortgagee, Assignee Or Receiver CG 20 18 is selected on the Screen New Additional Insured.
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getAdditionalInsuredslist().add(new CPPGLCoveragesAdditionalInsureds() {{
                        this.setType(AdditionalInsuredTypeGL.AdditionalInsured_MortgageeAssigneeOrReceiver_CG_20_18);
                    }});
                    break;
                //Additional Insured - Charitable Institutions CG 20 20
                case AdditionalInsured_CharitableInstitutions_CG_20_20:
                    //� This becomes required when the question �Is the applicant/insured a not for profit organization whose major purposes is charitable causes?� is answered Yes.

                    break;
                //Additional Insured - Church Members And Officers CG 20 22
                case AdditionalInsured_ChurchMembersAndOfficers_CG_20_22:
                    //� This becomes required when class code 41650 is selected
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), "41650"));
                    break;
                //Additional Insured - Executors, Administrators, Trustees Or Beneficiaries CG 20 23
                case AdditionalInsured_ExecutorsAdministratorsTrusteesOrBeneficiaries_CG_20_23:
                    //� Available when Organization Type is Trust. Located on the Policy Info.

                    break;
                //Additional Insured - Owners Or Other Interests From Whom Land Has Been Leased CG 20 24
                case AdditionalInsured_OwnersOrOtherInterestsFromWhomLandHasBeenLeased_CG_20_24:
                    //� Available when Additional Insured - Owners Or Other Interests From Whom Land Has Been Leased CG 20 24 is selected on the Screen New Additional Insured.
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getAdditionalInsuredslist().add(new CPPGLCoveragesAdditionalInsureds() {{
                        this.setType(AdditionalInsuredTypeGL.AdditionalInsured_OwnersOrOtherInterestsFromWhomLandHasBeenLeased_CG_20_24);
                    }});
                    break;
                //Additional Insured - Designated Person Or Organization CG 20 26
                case AdditionalInsured_DesignatedPersonOrOrganization_CG_20_26:
                    //� Available when Additional Insured - Designated Person Or Organization CG 20 26 is selected on the Screen New Additional Insured.
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getAdditionalInsuredslist().add(new CPPGLCoveragesAdditionalInsureds() {{
                        this.setType(AdditionalInsuredTypeGL.AdditionalInsured_DesignatedPersonOrOrganization_CG_20_26);
                    }});
                    break;
                //Additional Insured - Co-Owner Of Insured Premises CG 20 27
                case AdditionalInsured_Co_OwnerOfInsuredPremises_CG_20_27:
                    //� Available when Additional Insured - Co-Owner Of Insured Premises CG 20 27 is selected on the Screen New Additional Insured.
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getAdditionalInsuredslist().add(new CPPGLCoveragesAdditionalInsureds() {{
                        this.setType(AdditionalInsuredTypeGL.AdditionalInsured_CoownerOfInsuredPremises_CG_20_27);
                    }});
                    break;
                //Additional Insured - Lessor Of Leased Equipment CG 20 28
                case AdditionalInsured_LessorOfLeasedEquipment_CG_20_28:
                    //� Available when Additional Insured - Lessor Of Leased Equipment CG 20 28 is selected on the Screen New Additional Insured.
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getAdditionalInsuredslist().add(new CPPGLCoveragesAdditionalInsureds() {{
                        this.setType(AdditionalInsuredTypeGL.AdditionalInsured_LessorOfLeasedEquipment_CG_20_28);
                    }});
                    break;
                //Additional Insured - Grantor Of Franchise CG 20 29
                case AdditionalInsured_GrantorOfFranchise_CG_20_29:
                    //� Available when Additional Insured - Grantor Of Franchise CG 20 29 is selected on the Screen New Additional Insured.
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getAdditionalInsuredslist().add(new CPPGLCoveragesAdditionalInsureds() {{
                        this.setType(AdditionalInsuredTypeGL.AdditionalInsured_GrantorOfFranchise_CG_20_29);
                    }});
                    break;
                //Additional Insured - Engineers, Architects Or Surveyors Not Engaged By The Named Insured CG 20 32
                case AdditionalInsured_EngineersArchitectsOrSurveyorsNotEngagedByTheNamedInsured_CG_20_32:
                    //� Available when Additional Insured - Engineers, Architects Or Surveyors Not Engaged By The Named Insured CG 20 32 is selected on the Screen New Additional Insured.
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getAdditionalInsuredslist().add(new CPPGLCoveragesAdditionalInsureds() {{
                        this.setType(AdditionalInsuredTypeGL.AdditionalInsured_EngineersArchitectsOrSurveyorsNotEngagedByTheNamedInsured_CG_20_32);
                    }});
                    break;
                //Additional Insured - Grantor Of Licenses CG 20 36
                case AdditionalInsured_GrantorOfLicenses_CG_20_36:
                    //� Available when Additional Insured - Grantor Of Licenses CG 20 36 is selected on the Screen New Additional Insured.
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getAdditionalInsuredslist().add(new CPPGLCoveragesAdditionalInsureds() {{
                        this.setType(AdditionalInsuredTypeGL.AdditionalInsured_GrantorOfLicenses_CG_20_36);
                    }});
                    break;
                //Exclusion - All Hazards In Connection With Designated Premises CG 21 00
                case Exclusion_AllHazardsInConnectionWithDesignatedPremises_CG_21_00:
                    break;
                //Exclusion - Athletic Or Sports Participants CG 21 01
                case Exclusion_AthleticOrSportsParticipants_CG_21_01:
                    //� Available when one of the following class codes are selected: 63218, 63217, 63220, 63219, 43421, or 43424.
                    classCodes = new ArrayList<String>() {{
                        this.add("63218");
                        this.add("63217");
                        this.add("63220");
                        this.add("63219");
                        this.add("43421");
                        this.add("43424");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;
                //Exclusion - Products-Completed Operations Hazard CG 21 04
                case Exclusion_Products_CompletedOperationsHazard_CG_21_04:
                    //� Available when the class premium base does not include (+) and Exclude is chosen on CG 00 01 - Products / Completed Operations Aggregate Limit (no premium charge is made) attach this form as required.
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().setGeneralLiabilityPersonalAdvertisingInjuryLimit(CG0001_PersonalAdvertisingInjury.EXCLUDED);
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), GLClassCodeHelper.getRandomPlusGLPremiumBaseClassCode().getCode()));
                    break;

                //Exclusion - Unmanned Aircraft CG 21 09
                case Exclusion_UnmannedAircraft_CG_21_09:
                    //� Not available when CG 24 50 is selected.
                    //Electable
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().setLimitedCoverageForDesignatedUnmannedAircraftCG2450(true);
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().setUnmannedAircraftCG2450_Limit(100);
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getUnmannedAircraft_CG_24_50_MakeModelSerialNumber().add("Mine Silly Litte Flying Saucer");
                    break;
                //Exclusion - Designated Professional Services CG 21 16
                case Exclusion_DesignatedProfessionalServices_CG_21_16:
                    //� This becomes required when one of the following class codes are selected: 41650, 41677, 91805, 47052, 58408, 58409, 58456, 58457, 58458, or 58459. Other than that this remains electable.
                    classCodes = new ArrayList<String>() {{
                        this.add("41650");
                        this.add("41677");
                        this.add("91805");
                        this.add("47052");
                        this.add("58408");
                        this.add("58409");
                        this.add("58456");
                        this.add("58457");
                        this.add("58458");
                        this.add("58459");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;
                //Exclusion - Movement Of Buildings Or Structures CG 21 17
                case Exclusion_MovementOfBuildingsOrStructures_CG_21_17:
                    //� Available when question "Does applicant/insured move buildings or structures?" is answered yes.
                    //"� Available when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if the following class codes are selected: 91111, 91150, 91302, 91405, 91481, 91507, 91523, 92215, 94225, 95505, 96816, 97047, 97050, 97221, 97222, 97223, 98699, 98710, 98993, 99310, 99471, and 99975. However if you have a class code that starts with a ""9"" as well that is not on the list and is on the policy with one of them then this questions is also available.
                    //� This does not get attached to a location under the Underwriting Questions Tab"

                    //SPECIAL CASE

                    break;

                //Exclusion - Personal And Advertising Injury CG 21 38
                case Exclusion_PersonalAndAdvertisingInjury_CG_21_38:
                    //� This becomes required when one of the following class codes are selected: 91130, 91636, 43200, 65007, 66123, 66122, 46822, 46882, 46881, 47052, or 98751. Other than that this remains electable.
                    classCodes = new ArrayList<String>() {{
                        this.add("91130");
                        this.add("91636");
                        this.add("43200");
                        this.add("65007");
                        this.add("66123");
                        this.add("66122");
                        this.add("46822");
                        this.add("46882");
                        this.add("46881");
                        this.add("47052");
                        this.add("98751");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;
                //Employment-Related Practices Exclusion CG 21 47
                case Employment_RelatedPracticesExclusion_CG_21_47:
                    //� Available when Employement Practices Liability Insurance IDCG 31 2013 is not selected.
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().setEmploymentPracticesLiabilityInsuranceIDCG312013(false);
                    break;
                //Total Pollution Exclusion Endorsement CG 21 49
                case TotalPollutionExclusionEndorsement_CG_21_49:
                    //� Not available if CG 21 55 is selected.
                    break;
                //Amendment Of Liquor Liability Exclusion CG 21 50
                case AmendmentOfLiquorLiabilityExclusion_CG_21_50:
                    //� Available when one of the following class codes are selected: 16905 or 16906.
                    classCodes = new ArrayList<String>() {{
                        this.add("16905");
                        this.add("16906");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;

                //Exclusion - Financial Services CG 21 52
                case Exclusion_FinancialServices_CG_21_52:
                    //� Available when class code 61223 is selected.
                    //� If class code 61227 or 61226 is selected and the question ""Is applicant/insured involved in any of the following or related activities: Accounting, banking, credit card company, credit reporting, credit union, financial investment services, securities broker or dealer or tax preparation?"" is answered yes automatically attach this endorsement."
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), "61223"));
                    break;

                //Total Pollution Exclusion with A Hostile Fire Exception CG 21 55
                case TotalPollutionExclusionwithAHostileFireException_CG_21_55:
                    //� Not available if CG 21 49 is selected.
                    //Electable
                    break;
                //Exclusion - Funeral Services CG 21 56
                case Exclusion_FuneralServices_CG_21_56:
                    //� Available when one of the following class codes is selected: 41604, 41603, 41697, 41696, 43889, 46005, and/or 46004.
                    classCodes = new ArrayList<String>() {{
                        this.add("41604");
                        this.add("41603");
                        this.add("41697");
                        this.add("41696");
                        this.add("43889");
                        this.add("46005");
                        this.add("46004");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;
                //Exclusion - Counseling Services CG 21 57
                case Exclusion_CounselingServices_CG_21_57:
                    //� Available when one of the following class codes are selected: 48600 or 41650.
                    classCodes = new ArrayList<String>() {{
                        this.add("48600");
                        this.add("41650");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;
                //Exclusion - Professional Veterinarian Services CG 21 58
                case Exclusion_ProfessionalVeterinarianServices_CG_21_58:
                    //� Available when one of the following class codes are selected: 91200 or 99851.
                    classCodes = new ArrayList<String>() {{
                        this.add("91200");
                        this.add("99851");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;
                //Exclusion - Diagnostic Testing Laboratories CG 21 59
                case Exclusion_DiagnosticTestingLaboratories_CG_21_59:
                    //CG 21 59
                    break;
                //Exclusion - Exterior Insulation And Finish Systems CG 21 86
                case Exclusion_ExteriorInsulationAndFinishSystems_CG_21_86:
                    //� Available when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy: 96816, 97047, or 97050.
                    classCodes = new ArrayList<String>() {{
                        this.add("96816");
                        this.add("97047");
                        this.add("97050");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;
                //Exclusion - Inspection, Appraisal And Survey Companies CG 22 24
                case Exclusion_InspectionAppraisalAndSurveyCompanies_CG_22_24:
                    //� Available when one of the following class codes are selected: 61223, or 96317.
                    classCodes = new ArrayList<String>() {{
                        this.add("61223");
                        this.add("96317");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;
                //Amendment - Travel Agency Tours (Limitation Of Coverage) CG 22 28
                case Amendment_TravelAgencyToursLimitationOfCoverage_CG_22_28:
                    //� Available when class code 49333 is selected.
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), "49333"));
                    break;
                //Exclusion - Property Entrusted CG 22 29
                case Exclusion_PropertyEntrusted_CG_22_29:
                    //� Available when class code 98751, 49763, and/or 18991 is on the policy.
                    classCodes = new ArrayList<String>() {{
                        this.add("98751");
                        this.add("49763");
                        this.add("18991");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;
                //Exclusion - Corporal Punishment CG 22 30
                case Exclusion_CorporalPunishment_CG_22_30:
                    //� Available when one of the following class codes are selected: 67513, 67512, 47474, or 47477.
                    classCodes = new ArrayList<String>() {{
                        this.add("67513");
                        this.add("67512");
                        this.add("47474");
                        this.add("47477");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;
                //Exclusion - Testing Or Consulting Errors And Omissions CG 22 33
                case Exclusion_TestingOrConsultingErrorsAndOmissions_CG_22_33:
                    //� This becomes required when one of the following class codes are selected: 91135, 97003, or 97002.
                    classCodes = new ArrayList<String>() {{
                        this.add("91135");
                        this.add("97003");
                        this.add("97002");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;
                //Exclusion - Construction Management Errors And Omissions CG 22 34
                case Exclusion_ConstructionManagementErrorsAndOmissions_CG_22_34:
                    //� Available when class code 41620 is selected.
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), "41620"));
                    break;
                //Exclusion - Products And Professional Services (Druggists) CG 22 36
                case Exclusion_ProductsAndProfessionalServicesDruggists_CG_22_36:
                    //� Available when one of the following class codes are selected: 12375, 12374, or 45900.
                    classCodes = new ArrayList<String>() {{
                        this.add("12375");
                        this.add("12374");
                        this.add("45900");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;
                //Exclusion - Products And Professional Services (Optical And Hearing Aid Establishments) CG 22 37
                case Exclusion_ProductsAndProfessionalServicesOpticalAndHearingAidEstablishments_CG_22_37:
                    //� Available when one of the following class codes are selected: 13759 or 15839.
                    classCodes = new ArrayList<String>() {{
                        this.add("13759");
                        this.add("15839");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;
                //Exclusion - Fiduciary Or Representative Liability Of Financial Institutions CG 22 38
                case Exclusion_FiduciaryOrRepresentativeLiabilityOfFinancialInstitutions_CG_22_38:
                    //"� Available when one of the following class codes are selected: 61223.
                    //				� If class code 61227 or 61226 is selected and the question ""Does applicant/insured act in a fiduciary capacity?"" is answered yes automatically attach this endorsement."

                    break;
                //Exclusion - Camps Or Campgrounds CG 22 39
                case Exclusion_CampsOrCampgrounds_CG_22_39:
                    //� Available when one of the following class codes are selected: 10332, 10331, or 41422.
                    classCodes = new ArrayList<String>() {{
                        this.add("10332");
                        this.add("10331");
                        this.add("41422");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;

                //Exclusion - Engineers, Architects Or Surveyors Professional Liability CG 22 43
                case Exclusion_EngineersArchitectsOrSurveyorsProfessionalLiability_CG_22_43:
                    //� Available when one of the following class codes are selected: 92663 or 99471. However if CG 22 79 is on the policy then the endorsement (CG 22 43) is not availabile.
                    classCodes = new ArrayList<String>() {{
                        this.add("92663");
                        this.add("99471");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;
                //Exclusion - Services Furnished By Health Care Providers CG 22 44
                case Exclusion_ServicesFurnishedByHealthCareProviders_CG_22_44:
                    //� This becomes required when one of the following class codes are selected: 40032, 40031, 43551, 66561.
                    classCodes = new ArrayList<String>() {{
                        this.add("40032");
                        this.add("40031");
                        this.add("43551");
                        this.add("66561");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;
                //Exclusion - Specified Therapeutic Or Cosmetic Services CG 22 45
                case Exclusion_SpecifiedTherapeuticOrCosmeticServices_CG_22_45:
                    //� This becomes required when one of the following class codes are selected: 10113, 10115, 11128, 11127, 11234, 12356, 45190, 45192, 14655, 15600, 18912, 18911, 45191, or 45193.
                    classCodes = new ArrayList<String>() {{
                        this.add("10113");
                        this.add("10115");
                        this.add("11128");
                        this.add("11127");
                        this.add("11234");
                        this.add("12356");
                        this.add("45190");
                        this.add("45192");
                        this.add("14655");
                        this.add("15600");
                        this.add("18912");
                        this.add("18911");
                        this.add("45191");
                        this.add("45193");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;

                //Exclusion - Insurance And Related Operations CG 22 48
                case Exclusion_InsuranceAndRelatedOperations_CG_22_48:
                    //� This becomes required when one of the following class codes are selected: 61223 or 45334. Other than that this remains electable.
                    classCodes = new ArrayList<String>() {{
                        this.add("61223");
                        this.add("45334");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;
                //Exclusion - Failure To Supply CG 22 50
                case Exclusion_FailureToSupply_CG_22_50:
                    //� Available when one of the following class codes are selected: 13410, 92445, 97501, 97502, and/or 99943.
                    classCodes = new ArrayList<String>() {{
                        this.add("13410");
                        this.add("92445");
                        this.add("97501");
                        this.add("97502");
                        this.add("99943");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;
                //Exclusion - Laundry And Dry Cleaning Damage CG 22 53
                case Exclusion_LaundryAndDryCleaningDamage_CG_22_53:
                    //� Available when one of the following class codes are selected: 14731, 19007, or 45678.
                    classCodes = new ArrayList<String>() {{
                        this.add("14731");
                        this.add("19007");
                        this.add("45678");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;

                //Limitation Of Coverage - Real Estate Operations CG 22 60
                case LimitationOfCoverage_RealEstateOperations_CG_22_60:
                    //"� This is not available when class code 47052 is on the policy
                    //� This becomes required when class code 47050 is selected."
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), "47050"));
                    break;
                //Misdelivery Of Liquid Products Coverage CG 22 66
                case MisdeliveryOfLiquidProductsCoverage_CG_22_66:
                    //� This becomes required when one of the following class codes are selected: 10036, 12683, 13410 or 57001.
                    classCodes = new ArrayList<String>() {{
                        this.add("10036");
                        this.add("12683");
                        this.add("13410");
                        this.add("57001");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;
                //Operation Of Customers Autos On Particular Premises CG 22 68
                case OperationOfCustomersAutosOnParticularPremises_CG_22_68:
                    //� Available when one of the following class codes are selected: 10072, 10073, 10367, 13453, 13455, or 18616.
                    classCodes = new ArrayList<String>() {{
                        this.add("10072");
                        this.add("10073");
                        this.add("10367");
                        this.add("13453");
                        this.add("13455");
                        this.add("18616");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;
                //Real Estate Property Managed CG 22 70
                case RealEstatePropertyManaged_CG_22_70:
                    //� This becomes required when class code 47052 is selected.
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), "47052"));
                    break;
                //Colleges Or Schools (Limited Form) CG 22 71
                case CollegesOrSchoolsLimitedForm_CG_22_71:
                    //� Available when one of the following class codes are selected: 67513, 67512,  47474, or 47477.
                    classCodes = new ArrayList<String>() {{
                        this.add("67513");
                        this.add("67512");
                        this.add("47474");
                        this.add("47477");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;
                //Exclusion - Oil Or Gas Producing Operations CG 22 73
                case Exclusion_OilOrGasProducingOperations_CG_22_73:
                    //� This becomes required when class code 99969 is selected. Other than that this remains electable.
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), "99969"));
                    break;
                //Professional Liability Exclusion - Computer Data Processing CG 22 77
                case ProfessionalLiabilityExclusion_ComputerDataProcessing_CG_22_77:
                    //� This becomes required when class code 43151 is selected.
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), "43151"));
                    break;
                //Exclusion - Contractors - Professional Liability CG 22 79
                case Exclusion_Contractors_ProfessionalLiability_CG_22_79:
                    //				� Available when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy: 99471.

                    break;
                //Exclusion - Erroneous Delivery Or Mixture And Resulting Failure Of Seed To Germinate - Seed Merchants CG 22 81
                case Exclusion_ErroneousDeliveryOrMixtureAndResultingFailureOfSeedToGerminate_SeedMerchants_CG_22_81:
                    //				� Available when one of the following class codes are selected: 94225 or 16890.
                    classCodes = new ArrayList<String>() {{
                        this.add("94225");
                        this.add("16890");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;

                //Professional Liability Exclusion - Electronic Data Processing Services And Computer Consulting Or Programming Services CG 22 88
                case ProfessionalLiabilityExclusion_ElectronicDataProcessingServicesAndComputerConsultingOrProgrammingServices_CG_22_88:
                    //� This becomes required when one of the following class codes are selected: 41675.
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), "41675"));
                    break;
                //Professional Liability Exclusion - Spas Or Personal Enhancement Facilities CG 22 90
                case ProfessionalLiabilityExclusion_SpasOrPersonalEnhancementFacilities_CG_22_90:
                    //� This becomes required when class code 18200 is selected.
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), "18200"));
                    break;
                //Exclusion - Telecommunication Equipment Or Service Providers Errors And Omissions CG 22 91
                case Exclusion_TelecommunicationEquipmentOrServiceProvidersErrorsAndOmissions_CG_22_91:
                    //� Available when class code 18575 and/or 99600 is selected.
                    classCodes = new ArrayList<String>() {{
                        this.add("18575");
                        this.add("99600");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;
                //Snow Plow Operations Coverage CG 22 92
                case SnowPlowOperationsCoverage_CG_22_92:
                    //� Available when class code 99310 is selected.
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), "99310"));
                    break;
                //Exclusion - Damage To Work Performed By Subcontractors On Your Behalf CG 22 94
                case Exclusion_DamageToWorkPerformedBySubcontractorsOnYourBehalf_CG_22_94:
                    //� Required when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if the following class codes are the only class code selected: 97047 or 97050. Agents cannot add/remove this endorsement. When this endorsement is not required, Underwriter can add/remove this endorsement. UW cannot remove the endorsement when it is attached as required.  If the coverage goes from being required to not being required, it should automatically uncheck the coverage

                    break;
                //Limited Exclusion - Personal And Advertising Injury - Lawyers CG 22 96
                case LimitedExclusion_PersonalAndAdvertisingInjury_Lawyers_CG_22_96:
                    //"� Not available when CG 21 38 is selected.
                    //				� This becomes required when one of the following class codes are selected: 66123 or 66122."
                    classCodes = new ArrayList<String>() {{
                        this.add("66123");
                        this.add("66122");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;
                //Exclusion - Internet Service Providers And Internet Access Providers Errors And Omissions CG 22 98
                case Exclusion_InternetServiceProvidersAndInternetAccessProvidersErrorsAndOmissions_CG_22_98:
                    //				� Available when one of the following class codes are selected: 45334 and/or 47610.
                    classCodes = new ArrayList<String>() {{
                        this.add("45334");
                        this.add("47610");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;
                //Professional Liability Exclusion - Web Site Designers CG 22 99
                case ProfessionalLiabilityExclusion_WebSiteDesigners_CG_22_99:
                    //� This becomes required when class code 96930 is selected.
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), "96930"));
                    break;
                //Exclusion - Real Estate Agents Or Brokers Errors Or Omissions CG 23 01
                case Exclusion_RealEstateAgentsOrBrokersErrorsOrOmissions_CG_23_01:
                    //� This becomes required when class code 47050 is selected.
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), "47050"));
                    break;
                //Waiver Of Transfer Of Rights Of Recovery Against Others To Us CG 24 04
                case WaiverOfTransferOfRightsOfRecoveryAgainstOthersToUs_CG_24_04:
                    //� If Waiver of Subrogation is marked Yes under the Additional Insured screen then add this endorsement as required. This endorsement cannot be unselected. It is removed if Waiver of Subrogation is changed to a No.

                    break;
                //Liquor Liability - Bring Your Own Alcohol Establishments CG 24 06
                case LiquorLiability_BringYourOwnAlcoholEstablishments_CG_24_06:
                    //				"� This becomes required when one of the following class codes are selected: 16905 or 16906.
                    //				� This also becomes required when the question ""Does applicant/insured allow patrons to bring their own alcoholic beverages?"" is answered Yes."
                    classCodes = new ArrayList<String>() {{
                        this.add("16905");
                        this.add("16906");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;
                //Products/Completed Operations Hazard Redefined CG 24 07
                case Products_CompletedOperationsHazardRedefined_CG_24_07:
                    //� Available when one of the following class codes are selected: 14401, 16820, 16819, 16900, 16901, 16902, 16905, 16906, 16910, 16911, 16915, 16916, 16930, 16931, and/or 16941.
                    classCodes = new ArrayList<String>() {{
                        this.add("14401");
                        this.add("16820");
                        this.add("16819");
                        this.add("16900");
                        this.add("16901");
                        this.add("16902");
                        this.add("16905");
                        this.add("16906");
                        this.add("16910");
                        this.add("16911");
                        this.add("16915");
                        this.add("16916");
                        this.add("16930");
                        this.add("16931");
                        this.add("16941");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;
                //Boats CG 24 12
                case Boats_CG_24_12:
                    //� Required when class codes 10117, 40115, 40140, 40117, or 43760 is selected.
                    classCodes = new ArrayList<String>() {{
                        this.add("10117");
                        this.add("40115");
                        this.add("40140");
                        this.add("40117");
                        this.add("43760");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;
                //Canoes Or Rowboats CG 24 16
                case CanoesOrRowboats_CG_24_16:
                    //� Available when one of the following class codes are selected: 41668, 41667, 45190, 45192, 64074, 10110, 40111, 41422, 45191, 45193, or 64075.
                    classCodes = new ArrayList<String>() {{
                        this.add("41668");
                        this.add("41667");
                        this.add("45190");
                        this.add("45192");
                        this.add("64074");
                        this.add("10110");
                        this.add("40111");
                        this.add("41422");
                        this.add("45191");
                        this.add("45193");
                        this.add("64075");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;
                //Contractual Liability - Railroads CG 24 17
                case ContractualLiability_Railroads_CG_24_17:
                    //� This is only available for underwriters to select. The agent can see the endorsement only when the underwriter selects it and then they can also see what the underwriter has written but they are not able to edit it. The agent can unselect the endorsement but cannot reselect it, the endorsement will no longer be visible.

                    break;

                //Limited Coverage For Designated Unmanned Aircraft CG 24 50
                case LimitedCoverageForDesignatedUnmannedAircraftCoverageAOnly_CG_24_51:
                    //� Not available when CG 21 09 is selected.

                    break;
                //Designated Construction Project(s) General Aggregate Limit CG 25 03
                case DesignatedConstructionProjectsGeneralAggregateLimit_CG_25_03:
                    //� Available when Designated Construction Project(s) General Aggregate Limit CG 25 03 located under the Additional Insured screen is selected Yes.

                    break;
                //Designated Location(s) General Aggregate Limit CG 25 04
                case DesignatedLocationsGeneralAggregateLimit_CG_25_04:
                    //� Available when Designated Location(s) General Aggregate Limit CG 25 04 located under the Additional Insured screen is selected Yes.

                    break;
                //Commercial General Liability Declarations IDCG 03 0001
                case CommercialGeneralLiabilityDeclarationsID_CG_03_0001:
                    break;
                //Liquor Liability Declarations IDCG 03 0002
                case LiquorLiabilityDeclarationsID_CG_03_0002:
                    //Liquor Liability Coverage Form CG 00 33

                    break;
                //Idaho Professional Applicator Certificate Of Insurance IDCG 04 0001
                case IdahoProfessionalApplicatorCertificateOfInsuranceID_CG_04_0001:
                    //� Available for Underwriters to select all the time. The agent can see the endorsement when the underwriter selects it and what the underwriter has entered in however they cannot edit it or select it. However if class code 97047 or 97050 is selected and the question "Is applicant/insured a licensed applicator?" is answered yes then the agent can select the endorsement. Also the agent can remove the endorsement whenever they want.

                    break;
                //Exclusion - Explosion, Collapse And Underground Property Damage Hazard (Specified Operations) IDCG 31 2001
                case Exclusion_ExplosionCollapseAndUndergroundPropertyDamageHazardSpecifiedOperationsID_CG_31_2001:
                    //� Available when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy: 91111, 91150, 91405, 91481, 91523, 94225, 96816, 97047, 97050, 98993, 99310, 99471, or 99975.

                    break;
                //Exclusion - Designated Operations Covered By A Consolidated (Wrap-Up) Insurance Program IDCG 31 2002
                case Exclusion_DesignatedOperationsCoveredByAConsolidatedWrap_UpInsuranceProgramID_CG_31_2002:
                    //� Available when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy: 94225, 96816, 97047, 97050, 99471, or 99975.

                    break;
                //Lawn Care Services Coverage IDCG 31 2006
                case LawnCareServicesCoverageID_CG_31_2006:
                    //� This becomes available when class code 97047 and/or 97050 is selected.
                    classCodes = new ArrayList<String>() {{
                        this.add("97047");
                        this.add("97050");
                    }};
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1))));
                    break;
                //Exclusion Of Coverage For Structures Built Outside Of Designated Areas Endorsement IDCG 31 2007
                case ExclusionOfCoverageForStructuresBuiltOutsideOfDesignatedAreasEndorsementID_CG_31_2007:
                    //� Required when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy:  94225, 97047, 97050, and 96816.

                    break;
                //Endorsement Excluding Bodily Injury Claim By Your Employee Against An Additional Insured IDCG 31 2008
                case EndorsementExcludingBodilyInjuryClaimByYourEmployeeAgainstAnAdditionalInsuredID_CG_31_2008:
                    //� Available when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy: 99471.

                    break;
                //Farm Machinery Operations By Contractors Exclusion Endorsement IDCG 31 2009
                case FarmMachineryOperationsByContractorsExclusionEndorsementID_CG_31_2009:
                    //� This becomes required when class code 94225 is selected.
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), "94225"));
                    break;
                //Fertilizer Distributors And Dealers Exclusion Endorsement IDCG 31 2010
                case FertilizerDistributorsAndDealersExclusionEndorsementID_CG_31_2010:
                    //� This becomes required when class code 12683 is selected.
                    myPolicy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicy.commercialPackage.locationList.get(0), "12683"));
                    break;
                //Supplemental Extended Reporting Period Endorsement IDCG 31 2011
                //Commercial General Liability Manuscript Endorsement IDCG 31 2012
                case CommercialGeneralLiabilityManuscriptEndorsementID_CG_31_2012:
                    break;
                //Employment Practice Liability Insurance IDCG 31 2013
                case EmploymentPracticeLiabilityInsuranceID_CG_31_2013:
                    //� Available when Employement Practices Liability Insurance IDCG 31 2013 is not selected.

                    break;
                //Employment Practices Liability Supplemental Declarations IDCW 03 0001
                case EmploymentPracticesLiabilitySupplementalDeclarationsIDCW030001:
                    break;
                //Employment Practices Liability Warranty Statement IDCW 32 0001
                case EmploymentPracticesLiabilityWarrantyStatementIDCW320001:
                    //When endorsement IDCG 31 2013 is selected display the message.
                    break;


                //ELECTABLE
                //Primary And Noncontributory - Other Insurance Condition CG 20 01
                case PrimaryAndNoncontributory_OtherInsuranceCondition_CG_20_01:
                    //Electable
                    break;
                //Limitation Of Coverage To Insured Premises CG 28 06
                case LimitationOfCoverageToInsuredPremises_CG_28_06:
                    //Electable
                    break;
                //Excess Provision - Vendors CG 24 10
                case ExcessProvision_Vendors_CG_24_10:
                    //Electable
                    break;
                //Exclusion - Adult Day Care Centers CG 22 87
                case Exclusion_AdultDayCareCenters_CG_22_87:
                    //Electable
                    break;
                //Exclusion - Underground Resources And Equipment CG 22 57
                case Exclusion_UndergroundResourcesAndEquipment_CG_22_57:
                    //Electable
                    break;
                //Exclusion - Described Hazards (Carnivals, Circuses And Fairs) CG 22 58
                case Exclusion_DescribedHazardsCarnivalsCircusesAndFairs_CG_22_58:
                    //Electable
                    break;
                //Additional Insured - Engineers, Architects Or Surveyors CG 20 07
                case AdditionalInsured_EngineersArchitectsOrSurveyors_CG_20_07:
                    //Electable
                    break;
                //Additional Insured - Users Of Teams, Draft Or Saddle Animals CG 20 14
                case AdditionalInsured_UsersOfTeamsDraftOrSaddleAnimals_CG_20_14:
                    //Electable
                    break;
                //Communicable Disease Exclusion CG 21 32
                case CommunicableDiseaseExclusion_CG_21_32:
                    //Electable
                    break;
                //Exclusion - Designated Products CG 21 33
                case Exclusion_DesignatedProducts_CG_21_33:
                    //Electable
                    break;
                //Exclusion - Designated Work CG 21 34
                case Exclusion_DesignatedWork_CG_21_34:
                    //Electable
                    break;
                //Exclusion - Coverage C - Medical Payments CG 21 35
                case Exclusion_CoverageC_MedicalPayments_CG_21_35:
                    //Electable
                    break;
                //Exclusion - New Entities CG 21 36
                case Exclusion_NewEntities_CG_21_36:
                    //Electable
                    break;
                //Exclusion - Year 2000 Computer-Related And Other Electronic Problems CG 21 60
                case Exclusion_Year2000Computer_RelatedAndOtherElectronicProblems_CG_21_60:
                    //Required
                    break;
                //Exclusion - Volunteer Workers CG 21 66
                case Exclusion_VolunteerWorkers_CG_21_66:
                    //Electable
                    break;
                //Exclusion - Medical Payments To Children Day Care Centers CG 22 40
                case Exclusion_MedicalPaymentsToChildrenDayCareCenters_CG_22_40:
                    //Electable
                    break;
                //Exclusion - Rolling Stock - Railroad Construction CG 22 46
                case Exclusion_RollingStock_RailroadConstruction_CG_22_46:
                    //Electable
                    break;
                //Additional Insured - Owners, Lessees Or Contractors - Completed Operations CG 20 37
                case AdditionalInsured_OwnersLesseesOrContractors_CompletedOperations_CG_20_37:
                    //Electable
                    break;
                //Exclusion - Intercompany Products Suits CG 21 41
                case Exclusion_IntercompanyProductsSuits_CG_21_41:
                    //Electable
                    break;
                //Limitation Of Coverage To Designated Premises Or Project CG 21 44
                case LimitationOfCoverageToDesignatedPremisesProjectOrOperation_CG_21_44:
                    //Electable
                    break;
                //Amendment Of Liquor Liability Exclusion - Exception For Scheduled Activities CG 21 51
                case AmendmentOfLiquorLiabilityExclusion_ExceptionForScheduledActivities_CG_21_51:
                    //Electable
                    break;
                //Exclusion - Designated Ongoing Operations CG 21 53
                case Exclusion_DesignatedOngoingOperations_CG_21_53:
                    //Electable
                    break;


                //REQUIRED
                //Commercial General Liability Coverage Form CG 00 01
                case CommercialGeneralLiabilityCoverageForm_CG_00_01:
                    //Required
                    break;
                //Pollutants Definition Endorsement IDCG 31 2005
                case PollutantsDefinitionEndorsementID_CG_31_2005:
                    //Required
                    break;
                //Affiliate And Subsidiary Definition Endorsement IDCG 31 2004
                case AffiliateAndSubsidiaryDefinitionEndorsementID_CG_31_2004:
                    //Required
                    break;
                //Mobile Equipment Modification Endorsement IDCG 31 2003
                case MobileEquipmentModificationEndorsementID_CG_31_2003:
                    //Required
                    break;
                //Amendment Of Insured Contract Definition CG 24 26
                case AmendmentOfInsuredContractDefinition_CG_24_26:
                    //Required
                    break;
                //Silica or Silica-Related Dust Exclusion CG 21 96
                case SilicaorSilica_RelatedDustExclusion_CG_21_96:
                    //Required
                    break;
                //Exclusion - Access Or Disclosure Of Confidential Or Personal Information And Data-Related Liability - Limited Bodily Injury Exception Not Included CG 21 07
                case Exclusion_AccessOrDisclosureOfConfidentialOrPersonalInformationAndData_RelatedLiability_LimitedBodilyInjuryExceptionNotIncluded_CG_21_07:
                    //Required
                    break;
                //Abuse Or Molestation Exclusion CG 21 46
                case AbuseOrMolestationExclusion_CG_21_46:
                    //Required
                    break;
                //Fungi Or Bacteria Exclusion CG 21 67
                case FungiOrBacteriaExclusion_CG_21_67:
                    //Required
                    break;
                //Exclusion Of Certified Nuclear, Biological, Chemical Or Radiological Acts Of Terrorism; Cap On Covered Certified Acts Losses From Certified Acts Of Terrorism CG 21 84
                case ExclusionOfCertifiedNuclearBiologicalChemicalOrRadiologicalActsOfTerrorismCapOnCoveredCertifiedActsLossesFromCertifiedActsOfTerrorism_CG_21_84:
                    //Required
                    break;

                // TODO
                case CertificateofLiabilityInsurance:
                    break;
                case Exclusion_UnmannedAircraftCoverageBOnly_CG_21_11:
                    break;
                case SupplementalExtendedReportingPeriodEndorsementIDCW310002:
                    break;
            }//END SWITCH
        }//END FOR
        return myPolicy;
    }//END CreateFormsPolicyObject


    /**
     * @param policy
     */
    public GeneralLiabilityFormInference(GeneratePolicy policy, WebDriver driver) {
        super(driver);

        // Commercial General Liability Coverage Form CG 00 01
        // Liquor Liability Coverage Form CG 00 33
        if (policy.generalLiabilityCPP.isLiquorLiability()) {
            policy.generalLiabilityCPP.glForms.add(GeneralLiabilityForms.LiquorLiabilityCoverageForm_CG_00_33);
        }
        // Deductible Liability Insurance CG 03 00
        // Primary And Noncontributory - Other Insurance Condition CG 20 01

        // Additional Insured - Club Members CG 20 02
        if (policy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().isAdditionalInsured_ClubMembersCG2002()) {
            policy.generalLiabilityCPP.glForms.add(GeneralLiabilityForms.AdditionalInsured_ClubMembers_CG_20_02);
        }
        // Additional Insured - Condominium Unit Owners CG 20 04
        // Additional Insured - Engineers, Architects Or Surveyors CG 20 07
        if (policy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().isAdditionalInsured_EngineersArchitectsOrSurveyorsCG2007()) {
            policy.generalLiabilityCPP.glForms.add(GeneralLiabilityForms.AdditionalInsured_EngineersArchitectsOrSurveyors_CG_20_07);
        }
        // Additional Insured - Users Of Golfmobiles CG 20 08
        if (policy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().isAdditionalInsured_UsersOfGolfmobilesCG2008()) {
            policy.generalLiabilityCPP.glForms.add(GeneralLiabilityForms.AdditionalInsured_UsersOfGolfmobiles_CG_20_08);
        }
        // Additional Insured - Users Of Teams, Draft Or Saddle Animals CG 20 14
        if (policy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().isAdditionalInsured_UsersOfTeamsDraftOrSaddleAnimalsCG2014()) {
            policy.generalLiabilityCPP.glForms.add(GeneralLiabilityForms.AdditionalInsured_UsersOfTeamsDraftOrSaddleAnimals_CG_20_14);
        }
        // Additional Insured - Townhouse Associations CG 20 17
        // Additional Insured - Charitable Institutions CG 20 20
        if (policy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().isAdditionalInsured_CharitableInstitutionsCG2020()) {
            policy.generalLiabilityCPP.glForms.add(GeneralLiabilityForms.AdditionalInsured_CharitableInstitutions_CG_20_20);
        }
        // Additional Insured - Church Members And Officers CG 20 22
        // Additional Insured - Executors, Administrators, Trustees Or Beneficiaries CG 20 23

        if (!policy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getAdditionalInsuredslist().isEmpty()) {
            policy.generalLiabilityCPP.glForms.add(GeneralLiabilityForms.CertificateofLiabilityInsurance);
        }

        for (CPPGLCoveragesAdditionalInsureds additionalInsured : policy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getAdditionalInsuredslist()) {
            switch (additionalInsured.getType()) {
                case AdditionalInsured_ConcessionairesTradingUnderYourName_CG_20_03:
                    policy.generalLiabilityCPP.glForms.add(GeneralLiabilityForms.AdditionalInsured_ConcessionairesTradingUnderYourName_CG_20_03);
                    break;
                case AdditionalInsured_ControllingInterest_CG_20_05:
                    policy.generalLiabilityCPP.glForms.add(GeneralLiabilityForms.AdditionalInsured_ControllingInterest_CG_20_05);
                    break;
                case AdditionalInsured_CoownerOfInsuredPremises_CG_20_27:
                    policy.generalLiabilityCPP.glForms.add(GeneralLiabilityForms.AdditionalInsured_Co_OwnerOfInsuredPremises_CG_20_27);
                    break;
                case AdditionalInsured_DesignatedPersonOrOrganization_CG_20_26:
                    policy.generalLiabilityCPP.glForms.add(GeneralLiabilityForms.AdditionalInsured_DesignatedPersonOrOrganization_CG_20_26);
                    break;
                case AdditionalInsured_EngineersArchitectsOrSurveyorsNotEngagedByTheNamedInsured_CG_20_32:
                    policy.generalLiabilityCPP.glForms.add(GeneralLiabilityForms.AdditionalInsured_EngineersArchitectsOrSurveyorsNotEngagedByTheNamedInsured_CG_20_32);
                    break;
                case AdditionalInsured_GrantorOfFranchise_CG_20_29:
                    policy.generalLiabilityCPP.glForms.add(GeneralLiabilityForms.AdditionalInsured_GrantorOfFranchise_CG_20_29);
                    break;
                case AdditionalInsured_GrantorOfLicenses_CG_20_36:
                    policy.generalLiabilityCPP.glForms.add(GeneralLiabilityForms.AdditionalInsured_GrantorOfLicenses_CG_20_36);
                    break;
                case AdditionalInsured_LessorOfLeasedEquipment_CG_20_28:
                    policy.generalLiabilityCPP.glForms.add(GeneralLiabilityForms.AdditionalInsured_LessorOfLeasedEquipment_CG_20_28);
                    break;
                case AdditionalInsured_ManagersOrLessorsOfPremises_CG_20_11:
                    policy.generalLiabilityCPP.glForms.add(GeneralLiabilityForms.AdditionalInsured_ManagersOrLessorsOfPremises_CG_20_11);
                    break;
                case AdditionalInsured_MortgageeAssigneeOrReceiver_CG_20_18:
                    policy.generalLiabilityCPP.glForms.add(GeneralLiabilityForms.AdditionalInsured_MortgageeAssigneeOrReceiver_CG_20_18);
                    break;
                case AdditionalInsured_OwnersLesseesOrContractors_CompletedOperations_CG_20_37:
                    policy.generalLiabilityCPP.glForms.add(GeneralLiabilityForms.AdditionalInsured_OwnersLesseesOrContractors_CompletedOperations_CG_20_37);
                    break;
                case AdditionalInsured_OwnersLesseesOrContractors_ScheduledPersonOrOrganization_CG_20_10:
                    policy.generalLiabilityCPP.glForms.add(GeneralLiabilityForms.AdditionalInsured_OwnersLesseesOrContractors_ScheduledPersonOrOrganization_CG_20_10);
                    break;
                case AdditionalInsured_OwnersOrOtherInterestsFromWhomLandHasBeenLeased_CG_20_24:
                    policy.generalLiabilityCPP.glForms.add(GeneralLiabilityForms.AdditionalInsured_OwnersOrOtherInterestsFromWhomLandHasBeenLeased_CG_20_24);
                    break;
                case AdditionalInsured_StateOrGovernmentalAgencyOrSubdivisionOrPoliticalSubdivision_PermitsOrAuthorizations_CG_20_12:
                    policy.generalLiabilityCPP.glForms.add(GeneralLiabilityForms.AdditionalInsured_StateOrGovernmentalAgencyOrSubdivisionOrPoliticalSubdivision_PermitsOrAuthorizations_CG_20_12);
                    break;
                case AdditionalInsured_StateOrGovernmentalAgencyOrSubdivisionOrPoliticalSubdivision_PermitsOrAuthorizationsRelatingToPremises_CG_20_13:
                    policy.generalLiabilityCPP.glForms.add(GeneralLiabilityForms.AdditionalInsured_StateOrGovernmentalAgencyOrSubdivisionOrPoliticalSubdivision_PermitsOrAuthorizationsRelatingToPremises_CG_20_13);
                    break;
                case AdditionalInsured_Vendors_CG_20_15:
                    policy.generalLiabilityCPP.glForms.add(GeneralLiabilityForms.AdditionalInsured_Vendors_CG_20_15);
                    break;
                case AdditionalInsured_CoOwnerOfInsuredPremises_CG_20_27:
                    policy.generalLiabilityCPP.glForms.add(GeneralLiabilityForms.AdditionalInsured_Co_OwnerOfInsuredPremises_CG_20_27);
                    break;
                default:
                    break;
            }//END SWITCH
        }//END FOR
    }//END GeneralLiabilityFormInferance


    public List<Glforms> getFormsList() {
        for (Glforms form : formsList) {
            systemOut(form.getName());
        }
        return formsList;
    }


}














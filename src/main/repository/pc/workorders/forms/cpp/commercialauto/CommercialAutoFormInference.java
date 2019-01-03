package repository.pc.workorders.forms.cpp.commercialauto;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import persistence.globaldatarepo.entities.FormsCA;
import persistence.globaldatarepo.helpers.CAFormsHelpers;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.AdditionalInsuredTypeCA;
import repository.gw.enums.CommercialAutoForm;
import repository.gw.enums.CommercialAutoLine.*;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.Vehicle.*;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.*;
import repository.gw.helpers.NumberUtils;

import java.util.ArrayList;
import java.util.List;

public class CommercialAutoFormInference extends BasePage {

    private WebDriver driver;

    public CommercialAutoFormInference(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    List<FormsCA> formsList = new ArrayList<>();

    //

    /**
     * @param formsList
     * @return GeneratePolicy
     * @throws Exception
     * @author bmartin
     * @Description - input a list of all the forms that are needed to be generated and this method will add all the required objects to a generate object and return it.
     * @DATE - Updated 15 November 2017
     */
    @SuppressWarnings("serial")
    public GeneratePolicy CreateFormsPolicyObject(List<CommercialAutoForm> formsList) throws Exception {
        GeneratePolicy myPolicy = new GeneratePolicy(driver);
        myPolicy.commercialAutoCPP = new CPPCommercialAuto();

        if (myPolicy.commercialPackage.locationList == null) {
            myPolicy.commercialPackage.locationList = new ArrayList<PolicyLocation>() {{
                this.add(new PolicyLocation(new AddressInfo(true), true));
            }};
        }

        for (CommercialAutoForm form : formsList) {
            switch (form) {
                case BusinessAutoCoverageForm_CA_00_01:
                case IdahoChanges_CA_01_18:
                case SilicaOrSilica_relatedDustExclusionForCoveredAutosExposure_CA_23_94:
                case BusinessAutoDeclarations_IDCA_03_0001:
                case PublicOrLiveryPassengerConveyanceExclusion_CA_23_44:
                    //REQUIRED
                    break;
                case DeductibleLiabilityCoverage_CA_03_01:
                    myPolicy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_Coverages().setDeductibleLiabilityCoverageCA0301(DeductibleLiabilityCoverage.FiveHundred500);
                    break;
                case ExclusionOfFederalEmployeesUsingAutosInGovernmentBusiness_CA_04_42:
                    myPolicy.commercialAutoCPP.getCommercialAutoLine().getExclutionsAndConditions().add(CALineExclutionsAndConditions.ExclusionOfFederalEmployeesUsingAutosInGovernmentBusinessCA0442);
                    break;
                case WaiverOfTransferOfRightsOfRecoveryAgainstOthersToUs_WaiverOfSubrogation_CA_04_44:

                    //ADDITIONAL INTERESTS FORM
                    break;
                case Lessor_AdditionalInsuredAndLossPayee_CA_20_01:
                    AdditionalInterest lessorLossPayee = new AdditionalInterest(ContactSubType.Company) {{
                        this.setAdditionalInterestTypeCPP(AdditionalInterestTypeCPP.Lessor_AdditionalInsuredAndLossPayeeCA_20_01);
                    }};
                    Vehicle lossPayee = new Vehicle() {{
                        System.out.println("Adding Vehicle: lossPayee");
                        this.setGaragedAt(myPolicy.commercialPackage.locationList.get(0).getAddress());
                        this.setAdditionalInterest(new ArrayList<AdditionalInterest>() {{
                            this.add(lessorLossPayee);
                        }});
                        this.setVehicleName("lossPayee");
                    }};
                    myPolicy.commercialAutoCPP.getVehicleList().add(lossPayee);
                    break;
                case MobileHomesContentsCoverage_CA_20_16:
                    Vehicle mobileCovered = new Vehicle() {{
                        System.out.println("Adding Vehicle: mobileCovered");
                        this.setGaragedAt(myPolicy.commercialPackage.locationList.get(0).getAddress());
                        this.setVehicleType(VehicleType.Miscellaneous);
                        //OR
                        this.setBodyType(BodyType.MotorHomesMoreThan22Feet);
                        this.setBodyType(BodyType.MotorHomesUpTo22Feet);
                        this.setBodyType(BodyType.TrailerEquippedAsLivingQuarters);

                        this.setMotorHomeHaveLivingQuarters(true);
                        this.setMotorHomeContentsCoverage(true);
                        this.setComprehensive(true);
                        this.setMobileHomesContentsCoverageCA2016Limit(250);
                        this.setVehicleName("mobileCovered");

                    }};
                    myPolicy.commercialAutoCPP.getVehicleList().add(mobileCovered);
                    break;
                case ProfessionalServicesNotCovered_CA_20_18:
                    myPolicy.commercialAutoCPP.getCommercialAutoLine().getExclutionsAndConditions().add(CALineExclutionsAndConditions.ProfessionalServicesNotCoveredCA2018);
                    break;
                case DesignatedInsuredForCoveredAutosLiabilityCoverage_CA_20_48:
                    //additional insured 20 48
                    myPolicy.commercialAutoCPP.getCommercialAutoLine().getAdditionalInterest().add(new AdditionalInsured_CommercialAuto() {{
                        this.setAdditionalInsuredType(AdditionalInsuredTypeCA.DesignatedInsuredForCoveredAutosLiabilityCoverage_CA_20_48);
                    }});
                    break;
                case EmployeeHiredAutos_CA_20_54:
                    myPolicy.commercialAutoCPP.getCommercialAutoLine().getExclutionsAndConditions().add(CALineExclutionsAndConditions.EmployeeHiredAutosCA2054);
                    break;
                case CoverageForCertainOperationsInConnectionWithRailroads_CA_20_70:
                    myPolicy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().setCoverageForCertainOperationsInConnectionWithRailroadsCA2070(true);
                    myPolicy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().setRailRoadCA2070_Premium(10);
                    break;
                case AutoLoan_LeaseGapCoverage_CA_20_71:
                    AdditionalInterest autoLoanGap = new AdditionalInterest(ContactSubType.Company) {{
                        this.setAdditionalInterestTypeCPP(AdditionalInterestTypeCPP.LossPayableClauseIDCA_31_3001);
                    }};
                    Vehicle autoLoanGapVehicle = new Vehicle() {{
                        System.out.println("Adding Vehicle: autoLoanGapVehicle");
                        this.setGaragedAt(myPolicy.commercialPackage.locationList.get(0).getAddress());
                        this.setAutoLoanGapCoverage_CA2071(true);
                        this.setAdditionalInterest(new ArrayList<AdditionalInterest>() {{
                            this.add(autoLoanGap);
                        }});
                        this.setVehicleName("autoLoanGapVehicle");
                    }};
                    myPolicy.commercialAutoCPP.getVehicleList().add(autoLoanGapVehicle);
                    break;
                case Explosives_CA_23_01:
                    myPolicy.commercialAutoCPP.getCommercialAutoLine().getExclutionsAndConditions().add(CALineExclutionsAndConditions.ExplosivesCA2301);
                    break;
                case WrongDeliveryOfLiquidProducts_CA_23_05:
                    myPolicy.commercialAutoCPP.getCommercialAutoLine().getExclutionsAndConditions().add(CALineExclutionsAndConditions.WrongDeliveryOfLiquidProductsCA2305);
                    break;
                case MotorCarriers_InsuranceForNon_TruckingUse_CA_23_09:
                    Vehicle motorCarrier = new Vehicle() {{
                        System.out.println("Adding Vehicle: motorCarrier");
                        this.setGaragedAt(myPolicy.commercialPackage.locationList.get(0).getAddress());
                        this.getCaVehicleAdditionalCoveragesList().add(CAVehicleAdditionalCoverages.MotorCarriersInsuranceForNonTruckingUseCA2309);
                        this.setVehicleName("motorCarrier");
                    }};
                    myPolicy.commercialAutoCPP.getVehicleList().add(motorCarrier);
                    break;
                case AmphibiousVehicles_CA_23_97:
                    myPolicy.commercialAutoCPP.getCommercialAutoLine().getExclutionsAndConditions().add(CALineExclutionsAndConditions.AmphibiousVehiclesCA2397);
                    break;
                case PublicTransportationAutos_CA_24_02:
                    Vehicle publicTransport = new Vehicle() {{
                        System.out.println("Adding Vehicle: publicTransport");
                        this.setGaragedAt(myPolicy.commercialPackage.locationList.get(0).getAddress());
                        this.setVehicleType(VehicleType.PublicTransportation);
                        this.setBodyType(BodyType.MotelCourtesyBus);
                        this.setSeatingCapacity(SeatingCapacity.OneToEight);
                        this.setVehicleName("publicTransport");
                    }};
                    myPolicy.commercialAutoCPP.getVehicleList().add(publicTransport);
                    break;
                case IdahoUninsuredMotoristsCoverage_CA_31_15:
                    myPolicy.commercialAutoCPP.getCPP_CAStateInfo().setUninsuredMotoristCA3115(true);
                    break;
                case IdahoUnderinsuredMotoristsCoverage_CA_31_18:
                    myPolicy.commercialAutoCPP.getCPP_CAStateInfo().setUnderinsuredMotoristCA3118(true);
                    break;
                case AutoMedicalPaymentsCoverage_CA_99_03:
                    myPolicy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_Coverages().setMedicalPaymentsCA9903(true);
                    myPolicy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_Coverages().setMedicalPaymentsCA9903Limit(MedicalPaymentsLimit.random());
                    break;
                case DriveOtherCarCoverage_BroadenedCoverageForNamedIndividuals_CA_99_10:
                    switch (NumberUtils.generateRandomNumberInt(0, 5)) {
                        case 0:
                            myPolicy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().setOtherCarCollision(true);
                            myPolicy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().setCollision(OtherCarCollision.random());
                            break;
                        case 1:
                            myPolicy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().setOtherCarCompresensive(true);
                            myPolicy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().setComprehensive(OtherCarComprehensive.random());
                            break;
                        case 2:
                            myPolicy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().setOtherCarLiability(true);
                            break;
                        case 3:
                            myPolicy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().setOtherCarMedicalPayments(true);
                            break;
                        case 4:
                            myPolicy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().setOtherCarUnderinsuredMotorist(true);
                            myPolicy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().setUnderinsuredMotorist(OtherCarUnderinsuredMotorist.random());
                            break;
                        case 5:
                            myPolicy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().setOtherCarUninsuredMotorist(true);
                            myPolicy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().setUninsuredMotorist(OtherCarUninsuredMotorist.random());
                    }

                    break;
                case FireFireAndTheftFireTheftAndWindstormAndLimitedSpecifiedCausesOfLossCoverages_CA_99_14:
                    Vehicle fireTheftVehicle = new Vehicle() {{
                        System.out.println("Adding Vehicle: fireTheftVehicle");
                        this.setGaragedAt(myPolicy.commercialPackage.locationList.get(0).getAddress());
                        this.setVehicleType(VehicleType.Trucks);
                        this.setSpecifiedCauseofLoss(true);
                        this.setAdditionalCoveragesCauseOfLoss(SpecifiedCauseOfLoss.FireTheftAndWindstormOnly);
                        this.setVehicleName("fireTheftVehicle");
                    }};
                    myPolicy.commercialAutoCPP.getVehicleList().add(fireTheftVehicle);
                    break;
                case RentalReimbursementCoverage_CA_99_23:
                    Vehicle rentalReimbursement = new Vehicle() {{
                        System.out.println("Adding Vehicle: rentalReimbursement");
                        this.setGaragedAt(myPolicy.commercialPackage.locationList.get(0).getAddress());
                        this.getCaVehicleAdditionalCoveragesList().add(CAVehicleAdditionalCoverages.RentalReimbursementCA9923);
                        this.setVehicleName("rentalReimbursement");
                    }};
                    myPolicy.commercialAutoCPP.getVehicleList().add(rentalReimbursement);
                    break;
                case GaragekeepersCoverage_CA_99_37:
                    myPolicy.commercialAutoCPP.getGarageKeepers().add(new CPPCommercialAutoGarageKeepers(myPolicy.commercialPackage.locationList.get(0).getAddress()));
                    break;
                case ExclusionOrExcessCoverageHazardsOtherwiseInsured_CA_99_40:
                    Vehicle exclusionExcess = new Vehicle() {{
                        System.out.println("Adding Vehicle: exclusionExcess");
                        this.setGaragedAt(myPolicy.commercialPackage.locationList.get(0).getAddress());
                        this.getCaVehicleExclusionsAndConditionsList().add(CAVehicleExclusionsAndConditions.ExclusionOrExcessCoverageHazardsOtherwiseInsuredCA9940);
                        this.setVehicleName("exclusionExcess");
                    }};
                    myPolicy.commercialAutoCPP.getVehicleList().add(exclusionExcess);
                    break;
                case AudioVisualandDataElectronicEquipmentCoverageAddedLimits_CA_99_60:
                    AdditionalInterest audioVisualInterest = new AdditionalInterest(ContactSubType.Company) {{
                        this.setAdditionalInterestTypeCPP(AdditionalInterestTypeCPP.LossPayableClause_AudioVisualAndDataElectronicEquipmentCoverageAddedLimitsIDCA_31_3002);
                    }};
                    Vehicle audioVisualCar = new Vehicle() {{
                        System.out.println("Adding Vehicle: audioVisual");
                        this.setGaragedAt(myPolicy.commercialPackage.locationList.get(0).getAddress());
                        this.getCaVehicleAdditionalCoveragesList().add(CAVehicleAdditionalCoverages.AudioVisualDataElectronicEquipmentCoverageAddedLimitsCA9960);
                        this.setAdditionalInterest(new ArrayList<AdditionalInterest>() {{
                            this.add(audioVisualInterest);
                        }});
                        this.setVehicleName("audioVisual");
                    }};
                    myPolicy.commercialAutoCPP.getVehicleList().add(audioVisualCar);
                    break;
                case MCS_90:
                    myPolicy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().setMcs_90(true);
                    break;
                case IdahoUninsuredMotoristAndUnderinsuredMotoristDisclosureStatement_IDCA_10_0001:
                    myPolicy.commercialAutoCPP.getCPP_CAStateInfo().setUninsuredMotoristCA3115(false);
                    break;
                case IdahoUninsuredMotoristAndUnderinsuredMotoristDisclosureStatement_IDCA_10_0002:
                    myPolicy.commercialAutoCPP.getCPP_CAStateInfo().setUnderinsuredMotoristCA3118(false);
                    break;
                case ExcludedDriverAcknowledgmentLetter_IDCA_18_0001:
                case ExcludedDriverEndorsement_IDCA_31_3007:
                    Contact excludedDriver = new Contact() {{
                        this.setExcludedDriver(true);
                    }};
                    myPolicy.commercialAutoCPP.getDriversList().add(excludedDriver);
                    break;
                case LossPayableClause_IDCA_31_3001:
                    AdditionalInterest lossPayableClause = new AdditionalInterest(ContactSubType.Company) {{
                        this.setAdditionalInterestTypeCPP(AdditionalInterestTypeCPP.LossPayableClauseIDCA_31_3001);
                    }};
                    Vehicle lossPayableClauseVehicle = new Vehicle() {{
                        System.out.println("Adding Vehicle: lossPayableClauseVehicle");
                        this.setGaragedAt(myPolicy.commercialPackage.locationList.get(0).getAddress());
                        this.setAdditionalInterest(new ArrayList<AdditionalInterest>() {{
                            this.add(lossPayableClause);
                        }});
                        this.setVehicleName("lossPayableClauseVehicle");
                    }};
                    myPolicy.commercialAutoCPP.getVehicleList().add(lossPayableClauseVehicle);
                    break;
                case LossPayableClause_AudioVisualAndDataElectronicEquipmentCoverageAddedLimits_IDCA_31_3002:
                    AdditionalInterest lossPayableAudio = new AdditionalInterest(ContactSubType.Company) {{
                        this.setAdditionalInterestTypeCPP(AdditionalInterestTypeCPP.LossPayableClause_AudioVisualAndDataElectronicEquipmentCoverageAddedLimitsIDCA_31_3002);
                    }};
                    Vehicle lossPayable = new Vehicle() {{
                        System.out.println("Adding Vehicle: lossPayable");
                        this.setGaragedAt(myPolicy.commercialPackage.locationList.get(0).getAddress());
                        this.setAdditionalInterest(new ArrayList<AdditionalInterest>() {{
                            this.add(lossPayableAudio);
                        }});
                        this.setVehicleName("lossPayable");
                    }};
                    myPolicy.commercialAutoCPP.getVehicleList().add(lossPayable);
                    break;
                case MobileHomesContentsNotCovered_IDCA_31_3003:
                    Vehicle mobileNotCovered = new Vehicle() {{
                        System.out.println("Adding Vehicle: mobileNotCovered");
                        this.setGaragedAt(myPolicy.commercialPackage.locationList.get(0).getAddress());
                        this.setVehicleType(VehicleType.Miscellaneous);
                        //OR
                        this.setBodyType(BodyType.MotorHomesMoreThan22Feet);
                        this.setBodyType(BodyType.MotorHomesUpTo22Feet);
                        this.setBodyType(BodyType.TrailerEquippedAsLivingQuarters);

                        this.setMotorHomeHaveLivingQuarters(true);
                        this.setMotorHomeContentsCoverage(false);
                        this.setVehicleName("mobileNotCovered");
                    }};
                    myPolicy.commercialAutoCPP.getVehicleList().add(mobileNotCovered);
                    break;
                case LiabilityCoverageForRecreationalOrPersonalUseTrailers_IDCA_31_3005:
                    myPolicy.commercialAutoCPP.getCommercialAutoLine().getExclutionsAndConditions().add(CALineExclutionsAndConditions.LiabilityCoverageForRecreationalOrPersonalUseTrailersIDCA313005);
                    break;
                case RemovalOfPropertyDamageCoverage_IDCA_31_3006:
                    Vehicle existingDamagevehicle = new Vehicle() {{
                        System.out.println("Adding Vehicle: existingDamagevehicle");
                        this.setGaragedAt(myPolicy.commercialPackage.locationList.get(0).getAddress());
                        this.setExistingDamage(true);
                        this.setCollision(true);
                        this.setCollisionDeductible(Comprehensive_CollisionDeductible.FiveHundred500);
                        this.setVehicleName("existingDamagevehicle");
                    }};
                    myPolicy.commercialAutoCPP.getVehicleList().add(existingDamagevehicle);
                    break;
                case RoadsideAssistanceEndorsement_IDCA_31_3008:
                    Vehicle roadsideAssistanceVehicle = new Vehicle() {{
                        System.out.println("Adding Vehicle: roadsideAssistanceVehicle");
                        this.setGaragedAt(myPolicy.commercialPackage.locationList.get(0).getAddress());
                        this.setVehicleType(VehicleType.PrivatePassenger);
                        this.setVehicleName("roadsideAssistanceVehicle");
                    }};
                    myPolicy.commercialAutoCPP.getVehicleList().add(roadsideAssistanceVehicle);
                    break;
                case AdditionalNamedInsuredForDesignatedPersonOrOrganization_IDCA_31_3009:
                    Vehicle additonalNamed = new Vehicle() {{
                        System.out.println("Adding Vehicle: additonalNamed");
                        this.setGaragedAt(myPolicy.commercialPackage.locationList.get(0).getAddress());
                        this.getCaVehicleAdditionalCoveragesList().add(CAVehicleAdditionalCoverages.AdditionalNamedInsuredForDesignatedPersonOrOrganizationIDCA313009);
                        this.setVehicleName("additonalNamed");
                    }};
                    myPolicy.commercialAutoCPP.getVehicleList().add(additonalNamed);
                    break;
                case OutOfStateVehicleExclusion_IDCA_31_3011:
                    myPolicy.commercialAutoCPP.setSetCoveredVehicle(true);
                    break;
                case MotorCarrierEndorsement_IDCA_31_3012:
                    Vehicle motorCarrierVehicle = new Vehicle() {{
                        System.out.println("Adding Vehicle: motorCarrierVehicle");
                        this.setGaragedAt(myPolicy.commercialPackage.locationList.get(0).getAddress());
                        this.setVehicleType(VehicleType.Trucks);
                        this.setSizeClass(SizeClass.LightTrucksGVWOf10000PoundsOrLess);
                        this.setSecondaryClassType(SecondaryClassType.Truckers);
                        this.setSecondaryClass(SecondaryClass.CommonCarriers);
                        this.setVehicleName("motorCarrierVehicle");
                    }};
                    myPolicy.commercialAutoCPP.getVehicleList().add(motorCarrierVehicle);
                    break;
                case CommercialAutoManuscriptEndorsement_IDCA_31_3013:
                    myPolicy.commercialAutoCPP.getCommercialAutoLine().getExclutionsAndConditions().add(CALineExclutionsAndConditions.CommercialAutoManuscriptEndorsementIDCA313013);
                    break;
                case IndividualNamedInsured_CA_99_17:
                    break;
                case RollingStores_CA_23_04:
                    break;
                case OtherVehicleInsuranceEndorsement_IDCA_31_3015:
                    break;
                default:
                    break;
            }//END SWITCH
        }//END FOR


        if (myPolicy.commercialAutoCPP.getVehicleList().isEmpty()) {
            Vehicle basicVehicle = new Vehicle() {{
                System.out.println("Adding Vehicle: basicVehicle");
                this.setGaragedAt(myPolicy.commercialPackage.locationList.get(0).getAddress());
            }};
            myPolicy.commercialAutoCPP.getVehicleList().add(basicVehicle);
        }

        if (myPolicy.commercialAutoCPP.getDriversList().isEmpty()) {
            Contact basicPerson = new Contact();
            myPolicy.commercialAutoCPP.getDriversList().add(basicPerson);
        }

        return myPolicy;
    }


    /**
     * @param policyObject
     * @throws Exception
     * @Description -
     * this method cycles through the entire policy object and adds every form that meets inference criteria
     */
    public CommercialAutoFormInference(GeneratePolicy policyObject, WebDriver wd) throws Exception {
        super(wd);
        formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.BusinessAutoDeclarations_IDCA_03_0001.getValue()));
        formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.BusinessAutoCoverageForm_CA_00_01.getValue()));
        formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.SilicaOrSilica_relatedDustExclusionForCoveredAutosExposure_CA_23_94.getValue()));
        formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.IdahoChanges_CA_01_18.getValue()));
        formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.LiabilityCoverageForRecreationalOrPersonalUseTrailers_IDCA_31_3005.getValue()));
        formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.PublicOrLiveryPassengerConveyanceExclusion_CA_23_44.getValue()));
        formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.OtherVehicleInsuranceEndorsement_IDCA_31_3015.getValue()));

        //BusinessAutoDeclarationsIDCA030001
        if (policyObject.commercialAutoCPP.isSetCoveredVehicle()) {
            formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.BusinessAutoDeclarations_IDCA_03_0001.getValue()));
        }

        //OutOfStateVehicleExclusionIDCA313011
        if (policyObject.commercialAutoCPP.isSetCoveredVehicle()) {
            formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.OutOfStateVehicleExclusion_IDCA_31_3011.getValue()));
        }

        //Designated Insured For Covered Autos Liability Coverage CA 20 48
        for (AdditionalInsured_CommercialAuto ai : policyObject.commercialAutoCPP.getCommercialAutoLine().getAdditionalInterest()) {
            if (ai.getAdditionalInsuredType().equals(AdditionalInsuredTypeCA.DesignatedInsuredForCoveredAutosLiabilityCoverage_CA_20_48)) {
                formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.DesignatedInsuredForCoveredAutosLiabilityCoverage_CA_20_48.getValue()));
            }
        }


//		MotorCarrierEndorsementIDCA313012
        for (Vehicle vehicle : policyObject.commercialAutoCPP.getVehicleList()) {
            switch (vehicle.getSecondaryClassType()) {
                case Truckers:
                case FoodDelivery:
                case DumpAndTransitMix:
                case Contractors:
                    formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.MotorCarrierEndorsement_IDCA_31_3012.getValue()));
                    break;
                default:
                    break;
            }
            if (vehicle.getCaVehicleAdditionalCoveragesList().indexOf(CAVehicleAdditionalCoverages.MotorCarriersInsuranceForNonTruckingUseCA2309) >= 0) {
                formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.MotorCarrierEndorsement_IDCA_31_3012.getValue()));
                break;
            }
        }


//		RoadsideAssistanceEndorsementIDCA313008
        for (Vehicle vehicle : policyObject.commercialAutoCPP.getVehicleList()) {
            if (vehicle.isLiabilityCoverage()) {
                if (vehicle.getVehicleType().equals(VehicleType.PrivatePassenger)) {
                    formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.RoadsideAssistanceEndorsement_IDCA_31_3008.getValue()));
                    break;
                } else if (vehicle.getVehicleType().equals(VehicleType.Trucks)) {
                    if (vehicle.getSizeClass().equals(SizeClass.LightTrucksGVWOf10000PoundsOrLess) || vehicle.getSizeClass().equals(SizeClass.MediumTrucksGVWOf10001To20000Pounds)) {
                        formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.RoadsideAssistanceEndorsement_IDCA_31_3008.getValue()));
                        break;
                    }
                }
            }
        }


//		RemovalOfPropertyDamageCoverageIDCA313006
        for (Vehicle vehicle : policyObject.commercialAutoCPP.getVehicleList()) {
            if (vehicle.hasExistingDamage()) {
                formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.RemovalOfPropertyDamageCoverage_IDCA_31_3006.getValue()));
            }
        }


//		LossPayableClause_AudioVisualAndDataElectronicEquipmentCoverageAddedLimitsIDCA313002
        for (Vehicle vehicle : policyObject.commercialAutoCPP.getVehicleList()) {
            for (AdditionalInterest additionalInterest : vehicle.getAdditionalInterest()) {
                if (additionalInterest.getAdditionalInterestTypeCPP().equals(AdditionalInterestTypeCPP.LossPayableClause_AudioVisualAndDataElectronicEquipmentCoverageAddedLimitsIDCA_31_3002)) {
                    formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.LossPayableClause_AudioVisualAndDataElectronicEquipmentCoverageAddedLimits_IDCA_31_3002.getValue()));
                    break;
                }
            }
        }

//		LossPayableClauseIDCA313001
        for (Vehicle vehicle : policyObject.commercialAutoCPP.getVehicleList()) {
            for (AdditionalInterest additionalInterest : vehicle.getAdditionalInterest()) {
                if (additionalInterest.getAdditionalInterestTypeCPP().equals(AdditionalInterestTypeCPP.LossPayableClauseIDCA_31_3001)) {
                    formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.LossPayableClause_IDCA_31_3001.getValue()));
                    break;
                }
            }
        }

//		FireFireAndTheftFireTheftAndWindstormAndLimitedSpecifiedCausesOfLossCoveragesCA9914
        for (Vehicle vehicle : policyObject.commercialAutoCPP.getVehicleList()) {
            if (vehicle.hasSpecifiedCauseofLoss()) {
                if (vehicle.getSpecifiedCauseOfLoss().equals(SpecifiedCauseOfLoss.FireTheftAndWindstormOnly)) {
                    formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.FireFireAndTheftFireTheftAndWindstormAndLimitedSpecifiedCausesOfLossCoverages_CA_99_14.getValue()));
                }
            }
        }

//		PublicTransportationAutosCA2402
        for (Vehicle vehicle : policyObject.commercialAutoCPP.getVehicleList()) {
            if (vehicle.getVehicleType().equals(VehicleType.PublicTransportation)) {
                formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.PublicTransportationAutos_CA_24_02.getValue()));
            }
        }

//		WrongDeliveryOfLiquidProductsCA2305
        if (policyObject.commercialAutoCPP.getCommercialAutoLine().getExclutionsAndConditions().indexOf(CALineExclutionsAndConditions.WrongDeliveryOfLiquidProductsCA2305) >= 0) {
            formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.WrongDeliveryOfLiquidProducts_CA_23_05.getValue()));
        }

//		ExplosivesCA2301
        if (policyObject.commercialAutoCPP.getCommercialAutoLine().getExclutionsAndConditions().indexOf(CALineExclutionsAndConditions.ExplosivesCA2301) >= 0) {
            formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.Explosives_CA_23_01.getValue()));
        }

//		AutoLoanLeaseGapCoverageCA2071
        for (Vehicle vehicle : policyObject.commercialAutoCPP.getVehicleList()) {
            if (!vehicle.getAdditionalInterest().isEmpty()) {
                if (vehicle.getCaVehicleAdditionalCoveragesList().indexOf(CAVehicleAdditionalCoverages.AutoLoanLeaseGapCoverageCA2071) >= 0) {
                    formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.AutoLoan_LeaseGapCoverage_CA_20_71.getValue()));
                }
            }
        }


//		EmployeeHiredAutosCA2054
        if (policyObject.commercialAutoCPP.getCommercialAutoLine().getExclutionsAndConditions().indexOf(CALineExclutionsAndConditions.EmployeeHiredAutosCA2054) >= 0) {
            formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.EmployeeHiredAutos_CA_20_54.getValue()));
        }


//		ProfessionalServicesNotCoveredCA2018
        //If Funeral Limo, Hearse, Flower Car, Church Bus, and Motel Courtesy Bus is chosen in Body Type then attach CA 20 18 endorsement as required other than that it is electable (not checked)
        for (Vehicle vehicle : policyObject.commercialAutoCPP.getVehicleList()) {
            if (vehicle.getBodyType().equals(BodyType.FuneralLimo) || vehicle.getBodyType().equals(BodyType.FlowerCar) || vehicle.getBodyType().equals(BodyType.MotelCourtesyBus)) {
                formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.ProfessionalServicesNotCovered_CA_20_18.getValue()));
            }
        }
        if (policyObject.commercialAutoCPP.getCommercialAutoLine().getExclutionsAndConditions().indexOf(CALineExclutionsAndConditions.ProfessionalServicesNotCoveredCA2018) >= 0) {
            formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.ProfessionalServicesNotCovered_CA_20_18.getValue()));
        }


//		Lessor_AdditionalInsuredAndLossPayeeCA2001
        for (Vehicle vehicle : policyObject.commercialAutoCPP.getVehicleList()) {
            for (AdditionalInterest additionalInterest : vehicle.getAdditionalInterest()) {
                if (additionalInterest.getAdditionalInterestTypeCPP().equals(AdditionalInterestTypeCPP.Lessor_AdditionalInsuredAndLossPayeeCA_20_01)) {
                    formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.Lessor_AdditionalInsuredAndLossPayee_CA_20_01.getValue()));
                    break;
                }
            }
        }


//		WaiverOfTransferOfRightsOfRecoveryAgainstOthersToUsWaiverOfSubrogationCA0444
        for (AdditionalInsured_CommercialAuto additionalInsured : policyObject.commercialAutoCPP.getCommercialAutoLine().getAdditionalInterest()) {
            if (additionalInsured.isSubro()) {
                formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.WaiverOfTransferOfRightsOfRecoveryAgainstOthersToUs_WaiverOfSubrogation_CA_04_44.getValue()));
                break;
            }
        }


//		ExclusionOfFederalEmployeesUsingAutosInGovernmentBusinessCA0442
        if (policyObject.commercialAutoCPP.getCommercialAutoLine().getExclutionsAndConditions().indexOf(CALineExclutionsAndConditions.ExclusionOfFederalEmployeesUsingAutosInGovernmentBusinessCA0442) >= 0) {
            formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.ExclusionOfFederalEmployeesUsingAutosInGovernmentBusiness_CA_04_42.getValue()));
        }


        for (Vehicle vehicle : policyObject.commercialAutoCPP.getVehicleList()) {
            if (vehicle.isLiabilityCoverage()) {
                formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.RoadsideAssistanceEndorsement_IDCA_31_3008.getValue()));
                break;
            }
        }


        if (policyObject.commercialAutoCPP.getCommercialAutoLine().getExclutionsAndConditions().indexOf(CALineExclutionsAndConditions.AmphibiousVehiclesCA2397) >= 0) {
            formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.AmphibiousVehicles_CA_23_97.getValue()));
        }


        if (!policyObject.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_Coverages().getDeductibleLiabilityCoverageCA0301().equals(DeductibleLiabilityCoverage.NONE)) {
            formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.DeductibleLiabilityCoverage_CA_03_01.getValue()));
        }


        //Auto Medical Payments Coverage CA 99 03
        if (policyObject.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_Coverages().isMedicalPaymentsCA9903()) {
            formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.AutoMedicalPaymentsCoverage_CA_99_03.getValue()));
        }

        //Mobile Homes Contents Coverage CA 20 16
        //Mobile Homes Contents Not Covered IDCA 31 3003
        for (Vehicle vehicle : policyObject.commercialAutoCPP.getVehicleList()) {
            if (vehicle.getBodyType().equals(BodyType.MotorHomesMoreThan22Feet) || vehicle.getBodyType().equals(BodyType.MotorHomesUpTo22Feet) || vehicle.getBodyType().equals(BodyType.TrailerEquippedAsLivingQuarters)) {
                if (vehicle.isMotorHomeContentsCoverage()) {
                    formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.MobileHomesContentsCoverage_CA_20_16.getValue()));
                } else {
                    formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.MobileHomesContentsNotCovered_IDCA_31_3003.getValue()));
                }
            }
        }//END VEHICLE FOR


        //Coverage For Certain Operations In Connection With Railroads CA 20 70
        if (policyObject.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().isCoverageForCertainOperationsInConnectionWithRailroadsCA2070()) {
            formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.CoverageForCertainOperationsInConnectionWithRailroads_CA_20_70.getValue()));
        }

        //Motor Carriers - Insurance For Non-Trucking Use CA 23 09
        for (Vehicle vehicle : policyObject.commercialAutoCPP.getVehicleList()) {
            for (CAVehicleAdditionalCoverages coverage : vehicle.getCaVehicleAdditionalCoveragesList()) {
                if (coverage.equals(CAVehicleAdditionalCoverages.MotorCarriersInsuranceForNonTruckingUseCA2309)) {
                    formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.MotorCarriers_InsuranceForNon_TruckingUse_CA_23_09.getValue()));
                    break;
                }
            }//END VEHICLE COVERAGE FOR
        }//END VEHICLE FOR


        //Idaho Uninsured Motorists Coverage CA 31 15
        //Idaho Uninsured Motorist And Underinsured Motorist Disclosure Statement IDCA 10 0001
        if (policyObject.commercialAutoCPP.getCPP_CAStateInfo().isUninsuredMotoristCA3115()) {
            formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.IdahoUninsuredMotoristsCoverage_CA_31_15.getValue()));
        } else {
            formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.IdahoUninsuredMotoristAndUnderinsuredMotoristDisclosureStatement_IDCA_10_0001.getValue()));
            formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.IdahoUninsuredMotoristAndUnderinsuredMotoristDisclosureStatement_IDCA_10_0002.getValue()));
        }


        //Idaho Underinsured Motorists Coverage CA 31 18
        //Idaho Uninsured Motorist And Underinsured Motorist Disclosure Statement IDCA 10 0002
        if (policyObject.commercialAutoCPP.getCPP_CAStateInfo().isUnderinsuredMotoristCA3118()) {
            formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.IdahoUnderinsuredMotoristsCoverage_CA_31_18.getValue()));
        } else {
            formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.IdahoUninsuredMotoristAndUnderinsuredMotoristDisclosureStatement_IDCA_10_0001.getValue()));
            formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.IdahoUninsuredMotoristAndUnderinsuredMotoristDisclosureStatement_IDCA_10_0002.getValue()));
        }


        //Drive Other Car Coverage - Broadened Coverage For Named Individuals CA 99 10
        if (policyObject.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().isOtherCarCompresensive() ||
                policyObject.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().isOtherCarLiability() ||
                policyObject.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().isOtherCarMedicalPayments() ||
                policyObject.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().isOtherCarUnderinsuredMotorist() ||
                policyObject.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().isOtherCarUninsuredMotorist() ||
                policyObject.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().isOtherCarCollision()) {

            formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.DriveOtherCarCoverage_BroadenedCoverageForNamedIndividuals_CA_99_10.getValue()));
        }


        //Rental Reimbursement Coverage CA 99 23
        for (Vehicle vehicle : policyObject.commercialAutoCPP.getVehicleList()) {
            for (CAVehicleAdditionalCoverages coverage : vehicle.getCaVehicleAdditionalCoveragesList()) {
                if (coverage.equals(CAVehicleAdditionalCoverages.RentalReimbursementCA9923)) {
                    formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.RentalReimbursementCoverage_CA_99_23.getValue()));
                    break;
                }
            }//END VEHICLE COVERAGE FOR
        }//END VEHICLE FOR


        //Garagekeepers Coverage CA 99 37
        if (!policyObject.commercialAutoCPP.getGarageKeepers().isEmpty()) {
            formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.GaragekeepersCoverage_CA_99_37.getValue()));
        }


        //Exclusion Or Excess Coverage Hazards Otherwise Insured CA 99 40
        for (Vehicle vehicle : policyObject.commercialAutoCPP.getVehicleList()) {
            for (CAVehicleExclusionsAndConditions exclusion : vehicle.getCaVehicleExclusionsAndConditionsList()) {
                if (exclusion.equals(CAVehicleExclusionsAndConditions.ExclusionOrExcessCoverageHazardsOtherwiseInsuredCA9940)) {
                    formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.ExclusionOrExcessCoverageHazardsOtherwiseInsured_CA_99_40.getValue()));
                }
            }
        }//END VEHICLE FOR


        //Audio, Visual and Data Electronic Equipment Coverage Added Limits CA 99 60
        for (Vehicle vehicle : policyObject.commercialAutoCPP.getVehicleList()) {
            for (CAVehicleAdditionalCoverages coverage : vehicle.getCaVehicleAdditionalCoveragesList()) {
                if (coverage.equals(CAVehicleAdditionalCoverages.AudioVisualDataElectronicEquipmentCoverageAddedLimitsCA9960)) {
                    formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.AudioVisualandDataElectronicEquipmentCoverageAddedLimits_CA_99_60.getValue()));
                    break;
                }
            }//END VEHICLE COVERAGE FOR
        }//END VEHICLE FOR


        //Excluded Driver Endorsement IDCA 31 3007
        //Excluded Driver Acknowledgment Letter
        for (Contact driver : policyObject.commercialAutoCPP.getDriversList()) {
            if (driver.isExcludedDriver()) {
                formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.ExcludedDriverEndorsement_IDCA_31_3007.getValue()));
                formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.ExcludedDriverAcknowledgmentLetter_IDCA_18_0001.getValue()));
                break;
            }
        }//END DRIVERS FOR


        //Additional Named Insured For Designated Person Or Organization IDCA 31 3009
        for (Vehicle vehicle : policyObject.commercialAutoCPP.getVehicleList()) {
            for (CAVehicleAdditionalCoverages coverage : vehicle.getCaVehicleAdditionalCoveragesList()) {
                if (coverage.equals(CAVehicleAdditionalCoverages.AdditionalNamedInsuredForDesignatedPersonOrOrganizationIDCA313009)) {
                    formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.AdditionalNamedInsuredForDesignatedPersonOrOrganization_IDCA_31_3009.getValue()));
                }
            }//END VEHICLE COVERAGE FOR
        }//END VEHICLE FOR


        //Commercial Auto Manuscript Endorsement IDCA 31 3013
        for (CALineExclutionsAndConditions exclusion : policyObject.commercialAutoCPP.getCommercialAutoLine().getExclutionsAndConditions()) {
            if (exclusion.equals(CALineExclutionsAndConditions.CommercialAutoManuscriptEndorsementIDCA313013)) {
                formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.CommercialAutoManuscriptEndorsement_IDCA_31_3013.getValue()));
            }
        }


        //Endorsement For Motor Carrier Policies Of Insurance For Public Liability Under Sections 29 and 30 Of the Motor Carrier Act of 1980
        if (policyObject.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().isMcs_90()) {
            formsList.add(CAFormsHelpers.getCommercialAutoFormByName(CommercialAutoForm.MCS_90.getValue()));
        }
    }


    @SuppressWarnings("unused")
    private void addFormToList(GeneratePolicy policyObject, CommercialAutoForm caForm) {
        if (notInList(policyObject.commercialAutoCPP.getCommercialAutoForms(), caForm)) {
            policyObject.commercialAutoCPP.getCommercialAutoForms().add(caForm);
        }
    }


    private boolean notInList(List<CommercialAutoForm> formsList, CommercialAutoForm caForm) {
        for (CommercialAutoForm form : formsList) {
            if (form.equals(caForm)) {
                return false;
            }
        }
        return true;
    }


    public List<FormsCA> getFormsList() {
//		for(FormsCA form : formsList) {
//			systemOut(form.getName());
//		}
        return formsList;
    }


}

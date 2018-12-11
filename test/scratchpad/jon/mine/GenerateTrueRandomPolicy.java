package scratchpad.jon.mine;

import java.util.ArrayList;
import java.util.Random;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.enums.AdditionalInsuredRole;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.Building.BoxType;
import repository.gw.enums.Building.CauseOfLoss;
import repository.gw.enums.Building.ConstructionType;
import repository.gw.enums.Building.FireBurglaryAlarmGrade;
import repository.gw.enums.Building.FireBurglaryResponseType;
import repository.gw.enums.Building.FireBurglaryTypeOfSystem;
import repository.gw.enums.Building.HouseKeepingMaint;
import repository.gw.enums.Building.OccupancyInterestType;
import repository.gw.enums.Building.ParkingLotSidewalkCharacteristics;
import repository.gw.enums.Building.PercAreaLeasedToOthers;
import repository.gw.enums.Building.RoofCondition;
import repository.gw.enums.Building.RoofingType;
import repository.gw.enums.Building.SafetyEquipment;
import repository.gw.enums.Building.SqFtPercOccupied;
import repository.gw.enums.Building.ValuationMethod;
import repository.gw.enums.Building.WiringType;
import repository.gw.enums.BusinessownersLine.EmpDishonestyLimit;
import repository.gw.enums.BusinessownersLine.EmployeeDishonestyAuditsConductedFrequency;
import repository.gw.enums.BusinessownersLine.EmployeeDishonestyAuditsPerformedBy;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.Location.AutoIncreaseBlgLimitPercentage;
import repository.gw.enums.Location.ProtectionClassCode;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationAdditionalCoverages;
import repository.gw.generate.custom.PolicyLocationAdditionalInsured;
import repository.gw.generate.custom.PolicyLocationBuilding;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;

/**
 * Jon Larsen 6/23/2015
 * This Class is to generate a random policy with random number of locations, buildings,
 * Additional Insureds, limit values, etc....
 */
public class GenerateTrueRandomPolicy extends BaseTest {
    GeneratePolicy myPolicyObj = null;
    private ArrayList<String> buildingClassCodesForPDDeductible = new ArrayList<String>() {
        private static final long serialVersionUID = 1L;

        {
            this.add("63851");
            this.add("63830");
            this.add("63841");
        }
    };

    private int numLocations = new Random().nextInt(5);
    private int numBuildings = new Random().nextInt(3);
    private int numBOLineAI = new Random().nextInt(3);
    private int numBuildingAI = new Random().nextInt(3);


    @SuppressWarnings("serial")
    @Test(description = "Generates QuickQuote Submission")
    public void generateQuickQuote() throws Exception {


        // BUSINESS OWNERS LINE
        PolicyBusinessownersLine boLine = new PolicyBusinessownersLine(SmallBusinessType.StoresRetail) {{
            this.setAdditionalCoverageStuff(new PolicyBusinessownersLineAdditionalCoverages(false, true) {{
                this.setEmployeeDishonestyCoverage(true);
                this.setEmpDisLimit(EmpDishonestyLimit.Dishonest10000);
                this.setEmpDisNumCoveredEmployees(5);
                this.setEmpDisReferencesChecked(true);
                this.setEmpDisHowOftenAudits(EmployeeDishonestyAuditsConductedFrequency.Semi_Annually);
                this.setEmpDisAuditsPerformedBy(EmployeeDishonestyAuditsPerformedBy.Private_Auditing_Firm);
                this.setEmpDisDiffWriteThanReconcile(true);
                this.setEmpDisLargeCheckProcedures(true);
            }}); // END ADDITIONAL COVERAGES
            this.setAdditonalInsuredBOLineList(new ArrayList<PolicyBusinessownersLineAdditionalInsured>() {
                private static final long serialVersionUID = 1L;

                {
                    for (int i = 0; i <= numBOLineAI; i++) {
                        this.add(new PolicyBusinessownersLineAdditionalInsured(ContactSubType.Company, "Wells Fargo", AdditionalInsuredRole.CertificateHolderOnly, new AddressInfo(true)) {{
                            this.setAddress(new AddressInfo());
                            this.setCompanyName("Wells Fargo");
                            this.setContactRole("Affiliate");
                            this.setMemberNumber("");
                            this.setPersonFirstName("Jim");
                            this.setPersonLastName("Bowe");
                            this.setPhone("2082323231");
                            this.setSocialSecurityTaxNum("589625474");
                            //					this.setAiRole(AdditionalInsuredRole.CoOwnerOfInsuredPremises);
                            this.setCompanyOrPerson(ContactSubType.Company);
                            this.setSpecialWording(false);
                            this.setSpecialWordingAcord101Desc("The Fat Cat Ate The Fat Rat");
                            this.setSpecialWordingDesc("And The Fat Bat");
                            //					this.setVendorRoleListProducts("Cow's");
                            this.setWaiverOfSubro(false);
                        }}); //END BUSINESS OWNERS LINE ADDITIONAL INSURED

                    }//END BOLINE AI FOR LOOP

                }
            }); //END BUSINESS OWNERS LINE ADDITIONAL INSURED LIST
        }};    // END BUSINESS OWNERS LINE


        // LOCATIONS
        ArrayList<PolicyLocation> locationsLists = new ArrayList<PolicyLocation>() {
            private static final long serialVersionUID = 1L;

            {
                for (int i = 0; i <= numLocations; i++) {
                    this.add(new PolicyLocation(new AddressInfo(true), false) {{
                        this.setManualProtectionClassCode(ProtectionClassCode.Prot5);
                        this.setAutoautoIncrease(AutoIncreaseBlgLimitPercentage.AutoInc2Perc);
                        this.setAdditionalCoveragesStuff(new PolicyLocationAdditionalCoverages() {{
                            this.setMoneyAndSecuritiesCoverage(true);
                            this.setMoneySecNumMessengersCarryingMoney(5);
                            this.setMoneySecDepositDaily(true);
                            this.setMoneySecHowOftenDeposit("Hourly");
                            this.setMoneySecOffPremisesLimit(230);
                            this.setMoneySecOnPremisesLimit(920);
                            this.setOutdoorSignsCoverage(true);
                            this.setOutdoorSignsLimit(6700);
                            this.setWaterBackupAndSumpOverflow(true);
                        }}); // END ADDITIONAL COVERAGES STUFF
                        this.setAdditionalInsuredLocationsList(new ArrayList<PolicyLocationAdditionalInsured>() {{

                        }});
                        // LOCATION BULDING
                        this.setBuildingList(new ArrayList<PolicyLocationBuilding>() {
                                                 private static final long serialVersionUID = 1L;

                                                 {
                                                     numBuildings = new Random().nextInt(3);
                                                     for (int i = 0; i <= numBuildings; i++) {
                                                         this.add(new PolicyLocationBuilding() {{
                                                             this.setUsageDescription("Building 1");
                                                             this.setNamedInsuredOwner(false);
                                                             this.setOccupancySqFtPercOccupied(SqFtPercOccupied.SeventyFiveOneHundredPerc);
                                                             this.setOccupancyPercAreaLeasedToOthers(PercAreaLeasedToOthers.ZeroTenPerc);
                                                             this.setOccupancyNamedInsuredInterest(OccupancyInterestType.TenantOperator);
                                                             //this.setClassClassification(BuildingClassCode.ClothingorWearingApparelLadiesandGirlsCoatsSuitsandDresses.getClassification());
                                                             this.setClassCode(buildingClassCodesForPDDeductible.get(new Random().nextInt(buildingClassCodesForPDDeductible.size()))); //SET FOR PD DEDUCTIBLE

                                                             this.setBuildingLimit(5000);
                                                             this.setBuildingValuationMethod(ValuationMethod.ActualCashValue);
                                                             this.setBuildingCauseOfLoss(CauseOfLoss.Special);
                                                             this.setBuildingRoofExlusiongEndorsement(false);
                                                             this.setBuildingWindstormHailLossesToRoofSurfacing(false);

                                                             this.setBppLimit(5000);
                                                             this.setBppValuationMethod(ValuationMethod.ReplacementCost);
                                                             this.setBppCauseOfLoss(CauseOfLoss.Special);

                                                             this.setExteriorHouseKeepingAndMaintenance(HouseKeepingMaint.Superior);
                                                             this.setInteriorHouseKeepingAndMaintenance(HouseKeepingMaint.Good);

                                                             //parking lot characteristics
                                                             this.setParkingLotCharacteristicsList(new ArrayList<ParkingLotSidewalkCharacteristics>() {
                                                                 private static final long serialVersionUID = 1L;

                                                                 {
                                                                     this.add(ParkingLotSidewalkCharacteristics.Potholes);
                                                                     this.add(ParkingLotSidewalkCharacteristics.RaisedSunkenSurfaces);
                                                                 }
                                                             }); // END PARKINGLOT CHARACERISTICS

                                                             //safety equipment
                                                             this.setSafetyEquipmentList(new ArrayList<SafetyEquipment>() {
                                                                 private static final long serialVersionUID = 1L;

                                                                 {
                                                                     this.add(SafetyEquipment.HandRailinThreeOrMoreSteps);
                                                                     this.add(SafetyEquipment.NonSlipSurfaces);
                                                                 }
                                                             }); //END SAFETY EQUIPMENT LIST

                                                             this.setExitsProperlyMarked(true);
                                                             this.setNumFireExtinguishers(12);
                                                             this.setExposureToFlammablesChemicals(false);
                                                             this.setExposureToFlammablesChemicalsDesc("acids");

                                                             this.setConstructionType(ConstructionType.MasonryNonCombustible);
                                                             this.setYearBuilt(1990);
                                                             this.setNumStories(2);
                                                             this.setNumBasements(0);
                                                             this.setTotalArea("2564");
                                                             //SET BASEMENT FINISHED SQ FEET
                                                             //SET BASEMENT UNFINISHED SQ FEET
                                                             this.setSprinklered(true);
                                                             this.setPhotoYear(1993);
                                                             this.setCostEstimatorYear(1999);

                                                             this.setRoofingType(RoofingType.Aluminum);
                                                             this.setFlatRoof(false);
                                                             this.setRoofCondition(RoofCondition.NoIssues);
                                                             this.setYearRoofReplaced(2000);

                                                             this.setWiringType(WiringType.Romex);
                                                             this.setBoxType(BoxType.CircuitBreaker);
                                                             this.setYearLastMajorWiringUpdate(2000);
                                                             this.setWiringUpdateDesc("Bens Kite");

                                                             this.setYearLastMajorHeatingUpdate(2000);
                                                             this.setHeatingUpdateDesc("Camp Fire");

                                                             this.setYearLastMajorPlumbingUpdate(2000);
                                                             this.setPlumbingUpdateDesc("Out House");

                                                             this.setExistingDamage(false);
                                                             this.setExistingDamageDesc("Hammers");

                                                             this.setFireBurglaryTypeOfSystem(FireBurglaryTypeOfSystem.FireBurglary);
                                                             this.setFireBurglaryResponseType(FireBurglaryResponseType.PrivateMonitored);
                                                             this.setFireBurglaryAlarmGrade(FireBurglaryAlarmGrade.SecurityServiceWithTimingDevice);
                                                             this.setAlarmCertificate("Omni");

                                                             this.setInsuredPropertyWithin100Ft(false);
                                                             this.setInsuredPropertyWithin100FtPolicyHolderName("Bob the Builder");
                                                             this.setInsuredPropertyWithin100FtPolicyNumber("08-353652-01");
                                                             this.setAdditionalInterestList(new ArrayList<AdditionalInterest>() {
                                                                 private static final long serialVersionUID = 1L;

                                                                 {
                                                                     numBuildingAI = new Random().nextInt(3);
                                                                     for (int i = 0; i <= numBuildingAI; i++) {
													this.add(new AdditionalInterest("Wells Fargo", null) {{
                                                                             this.setAdditionalInterestType(AdditionalInterestType.Mortgagee);
                                                                             this.setAddress(new AddressInfo(true));
                                                                             this.setAppliedToBPP(true);
                                                                             this.setAppliedToBuilding(true);
                                                                             this.setCompanyName("Wells Fargo");
                                                                             this.setCompanyOrInsured(ContactSubType.Company);
                                                                             this.setFirstMortgage(true);
                                                                             this.setLoanContractNumber("");
                                                                             this.setLienholderNumber("");
                                                                             this.setPersonFirstName("Jim");
                                                                             this.setPersonLastName("Bowe");
                                                                             this.setPhone("2082323231");
                                                                             this.setSocialSecurityTaxNum("589625478");
                                                                         }}); //END BUILDING ADDITIONAL INTREST
                                                                     }//END BULDINGS AI FOR LOOP
                                                                 }
                                                             }); //END BUILDING ADDITIONAL INTREST LIST
                                                         }}); //END BUILDING

                                                     }//END BUILDING FOR LOOP

                                                 }
                                             } //END BUILDING LIST
                        ); //END SET BUILDING LIST
                    }});//END POLICY LOCATION

                }//END LOCATION FOR LOOP


            }
        }; //END LOCATION LIST

        // END LOCATIONS

        System.out.println("GENERATE \n");

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);

        // GENERATE POLICY
        myPolicyObj = new GeneratePolicy.Builder(driver)
                //		.withAgentUserName(agentUsername)
                .withBusinessownersLine(boLine)
                .withPolicyLocations(locationsLists)
                .withCreateNew(CreateNew.Create_New_Only_If_Does_Not_Exist)
                .withInsPersonOrCompany(ContactSubType.Person) //CURRENTLY MUST BE SET TO PERSON TO TEST MEMBERSHIP DUES
                .withInsFirstLastName("Small", "Defects")
                //			.withInsCompanyName("Small Defects")
                .withPolOrgType(OrganizationType.Partnership)
                //		.withPolDuesCounty(CountyIdaho.Bonneville)
                //Membership Dues
                .withMembershipDuesOnAllInsureds() //SET TO VERIFY MEMBERSHIP DUES IN AB
                .withInsPrimaryAddress(new AddressInfo(true))
                //		.withPolTermLengthDays(60)
                //		.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.Offices))
                .withPaymentPlanType(PaymentPlanType.Monthly)
                .withDownPaymentType(PaymentType.Cash)

                .build(GeneratePolicyType.QuickQuote);

        System.out.println(myPolicyObj.accountNumber);
        System.out.println(myPolicyObj.agentInfo.getAgentUserName());

    }
}

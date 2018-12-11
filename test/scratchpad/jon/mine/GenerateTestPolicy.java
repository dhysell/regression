package scratchpad.jon.mine;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import repository.gw.enums.Building.BoxType;
import repository.gw.enums.Building.CauseOfLoss;
import repository.gw.enums.Building.ConstructionType;
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
import repository.gw.enums.BusinessownersLine.LiabilityLimits;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
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
import repository.gw.generate.custom.PolicyLocationBuildingAdditionalCoverages;
import repository.gw.helpers.NumberUtils;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.Names;
import persistence.globaldatarepo.helpers.NamesHelper;

public class GenerateTestPolicy extends BaseTest {

    public GeneratePolicy myPolicy = null;


    List<String> failed = new ArrayList<String>();


    @DataProvider(name = "test1")
    public static Object[][] primeNumbers() {
        return new Object[][]{{1, 2, 1, null, null}};
    }

    @Test()
    public Boolean generateRandomPolicy() throws Exception {
        return generatePolicy(1, 2, 1, null, null, false, false, false);
    }//END TEST

    @Test()
    public Boolean generateRandomPolicy(String policyName) throws Exception {
        return generatePolicy(1, 2, 1, policyName, null, false, false, false);
    }//END TEST

    @Test()
    public Boolean generateRandomPolicy(GeneratePolicyType policyType) throws Exception {
        return generatePolicy(1, 2, 1, null, policyType, false, false, false);
    }//END TEST

    @Test()
    public Boolean generateRandomPolicy(String policyName, GeneratePolicyType policyType) throws Exception {
        return generatePolicy(1, 2, 1, policyName, policyType, false, false, false);
    }//END TEST

    @Test()
    public Boolean generateRandomPolicy(int numberOfPolicies, int numberOfLocations, int buildingsPerLocation) throws Exception {
        return generatePolicy(numberOfPolicies, numberOfLocations, buildingsPerLocation, null, null, false, false, false);
    }//END TEST

    @Test()
    public Boolean generateRandomPolicy(int numberOfPolicies, int numberOfLocations, int buildingsPerLocation, String policyName) throws Exception {
        return generatePolicy(numberOfPolicies, numberOfLocations, buildingsPerLocation, policyName, null, false, false, false);
    }//END TEST

    @Test()
    public Boolean generateRandomPolicy(int numberOfPolicies, int numberOfLocations, int buildingsPerLocation, String policyName, GeneratePolicyType policyType) throws Exception {
        return generatePolicy(numberOfPolicies, numberOfLocations, buildingsPerLocation, policyName, policyType, false, false, false);
    }

    @Test()
    public Boolean generateRandomPolicy(
            Boolean AdditonalInsuredBOLine,
            Boolean AdditonalInsuredLocation,
            Boolean AdditonalIntrestBuilding
    ) throws Exception {
        return generatePolicy(1, 2, 1, null, null, AdditonalInsuredBOLine, AdditonalInsuredLocation, AdditonalIntrestBuilding);
    }//END TEST

    @Test()
    public Boolean generateRandomPolicy(
            int numberOfPolicies,
            int numberOfLocations,
            int buildingsPerLocation,
            String policyName,
            GeneratePolicyType policyType,
            Boolean AdditonalInsuredBOLine,
            Boolean AdditonalInsuredLocation,
            Boolean AdditonalIntrestBuilding
    ) throws Exception {
        return generatePolicy(numberOfPolicies, numberOfPolicies, buildingsPerLocation, policyName, policyType, AdditonalInsuredBOLine, AdditonalInsuredLocation, AdditonalIntrestBuilding);
    }//END TEST


    private Boolean generatePolicy(
            int numberOfPolicies,
            int numberOfLocations,
            int buildingsPerLocation,
            String policyName,
            GeneratePolicyType policyType,
            final Boolean AdditonalInsuredBOLine,
            final Boolean AdditonalInsuredLocation,
            final Boolean AdditonalIntrestBuilding
    ) throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);

        int numPoliciesToGenerate = numberOfPolicies;
        final int numLocations = numberOfLocations;
        final int numBuildingsPerLocation = buildingsPerLocation;
        policyType = ((policyType == null) ? GeneratePolicyType.PolicyIssued : policyType);
        PaymentPlanType paymentPlan = PaymentPlanType.Annual;
        PaymentType paymentType = PaymentType.Credit_Debit;
        long startTime = new Date().getTime();
        long stopTime = 0;

        ArrayList<PolicyLocation> locationsLists = null;

        //		for(String agent : agentNames) {
        for (int i = 0; i < numPoliciesToGenerate; i++) {
//			try {

            OrganizationType orgType = OrganizationType.Joint_Venture;
            Names name = NamesHelper.getRandomName();

            // BUSINESS OWNERS LINE
            PolicyBusinessownersLine boLine = new PolicyBusinessownersLine(SmallBusinessType.random()) {{
                this.setLiabilityLimits(LiabilityLimits.Five00_1000_1000);
                this.setAdditionalCoverageStuff(new PolicyBusinessownersLineAdditionalCoverages(false, true) {{
                }}); // END ADDITIONAL COVERAGES

                this.setAdditonalInsuredBOLineList(new ArrayList<PolicyBusinessownersLineAdditionalInsured>() {
                    private static final long serialVersionUID = 1L;

                    {
                        if (AdditonalInsuredBOLine) {
                            this.add(new PolicyBusinessownersLineAdditionalInsured());
                        }
                    }
                }); //END BUSINESS OWNERS LINE ADDITIONAL INSURED LIST
            }};    // END BUSINESS OWNERS LINE


            // LOCATIONS
            locationsLists = new ArrayList<PolicyLocation>() {
                private static final long serialVersionUID = 1L;

                {
                    for (int k = 1; k <= numLocations; k++) {
                        this.add(new PolicyLocation(new AddressInfo(true), false) {{
                            this.setManualProtectionClassCode(ProtectionClassCode.Prot5);
                            this.setAutoautoIncrease(AutoIncreaseBlgLimitPercentage.AutoInc2Perc);
                            this.setAdditionalCoveragesStuff(new PolicyLocationAdditionalCoverages() {{
                            }}); // END ADDITIONAL COVERAGES STUFF
                            this.setAdditionalInsuredLocationsList(new ArrayList<PolicyLocationAdditionalInsured>() {
                                private static final long serialVersionUID = 1L;

                                {
                                    if (AdditonalInsuredLocation) {
                                        this.add(new PolicyLocationAdditionalInsured());
                                    }
                                }
                            });


                            // LOCATION BULDING
                            this.setBuildingList(new ArrayList<PolicyLocationBuilding>() {
                                private static final long serialVersionUID = 1L;

                                {
                                    for (int j = 1; j <= numBuildingsPerLocation; j++) {

                                        this.add(new PolicyLocationBuilding() {{
                                            this.setUsageDescription("Building " + NamesHelper.getRandomName().getWord());
                                            this.setNamedInsuredOwner(false);
                                            this.setOccupancySqFtPercOccupied(SqFtPercOccupied.SeventyFiveOneHundredPerc);
                                            this.setOccupancyPercAreaLeasedToOthers(PercAreaLeasedToOthers.ZeroTenPerc);
                                            this.setOccupancyNamedInsuredInterest(OccupancyInterestType.TenantOperator);
                                            //this.setClassClassification(BuildingClassCode.ClothingorWearingApparelLadiesandGirlsCoatsSuitsandDresses.getClassification());
//												this.setClassCode(BuildingClassCode.random().getClassCode());
//												this.setClassCode(BuildingClassCode.MotelsNoRestaurants.getClassCode());
                                            this.setClassCode("69171");
                                            this.setBuildingLimit(NumberUtils.generateRandomNumberInt(5000, 300000));
                                            this.setBuildingValuationMethod(ValuationMethod.ActualCashValue);
                                            this.setBuildingCauseOfLoss(CauseOfLoss.Special);
                                            this.setBuildingRoofExlusiongEndorsement(false);
                                            this.setBuildingWindstormHailLossesToRoofSurfacing(false);

                                            this.setBppLimit(NumberUtils.generateRandomNumberInt(5000, 300000));
                                            this.setBppValuationMethod(ValuationMethod.ReplacementCost);
                                            this.setBppCauseOfLoss(CauseOfLoss.Special);

                                            this.setExteriorHouseKeepingAndMaintenance(HouseKeepingMaint.Superior);
                                            this.setInteriorHouseKeepingAndMaintenance(HouseKeepingMaint.Good);

                                            //parking lot characteristics
                                            this.setParkingLotCharacteristicsList(new ArrayList<ParkingLotSidewalkCharacteristics>() {
                                                private static final long serialVersionUID = 1L;

                                                {
//														this.add(ParkingLotSidewalkCharacteristics.NoneOfAbove);
                                                    this.add(ParkingLotSidewalkCharacteristics.random());
                                                }
                                            }); // END PARKINGLOT CHARACERISTICS

                                            //safety equipment
                                            this.setSafetyEquipmentList(new ArrayList<SafetyEquipment>() {
                                                private static final long serialVersionUID = 1L;

                                                {
                                                    this.add(SafetyEquipment.random());
                                                }
                                            }); //END SAFETY EQUIPMENT LIST

                                            this.setExitsProperlyMarked(true);
                                            this.setNumFireExtinguishers(NumberUtils.generateRandomNumberInt(1, 20));
                                            this.setExposureToFlammablesChemicals(false);
                                            this.setExposureToFlammablesChemicalsDesc("acids");

                                            this.setConstructionType(ConstructionType.random());
                                            this.setYearBuilt(2013);
                                            this.setNumStories(1);
                                            this.setNumBasements(0);
                                            this.setTotalArea(String.valueOf(NumberUtils.generateRandomNumberInt(1000, 5000)));
                                            this.setSprinklered(true);
                                            this.setPhotoYear(1993);
                                            this.setCostEstimatorYear(1999);
                                            RoofingType roof = RoofingType.random();
                                            while (roof.equals(RoofingType.RolledRoofing)) {
                                                //Rolled Roofs block user from moving on.
                                                roof = RoofingType.random();
                                            }
                                            this.setRoofingType(roof);
                                            this.setFlatRoof(false);
                                            this.setRoofCondition(RoofCondition.NoIssues);
                                            this.setYearRoofReplaced(2000);

                                            this.setWiringType(WiringType.Romex);
                                            this.setBoxType(BoxType.CircuitBreaker);

                                            this.setInsuredPropertyWithin100Ft(false);
                                            this.setInsuredPropertyWithin100FtPolicyHolderName("Bob the Builder");
                                            this.setInsuredPropertyWithin100FtPolicyNumber("08-353652-01");
                                            this.setAdditionalCoveragesStuff(new PolicyLocationBuildingAdditionalCoverages() {{
                                                this.setSpoilageCoverage(false);
                                            }});
                                            this.setAdditionalInterestList(new ArrayList<AdditionalInterest>() {
                                                private static final long serialVersionUID = 1L;

                                                {
                                                    if (AdditonalIntrestBuilding) {
                                                        this.add(new AdditionalInterest());
                                                    }
                                                }
                                            }); //END BUILDING ADDITIONAL INTREST LIST
                                        }}); //END BUILDING
                                    }//END BUILDINGS FOR LOOP
                                }
                            }); //END BUILDING LIST

                        }});//END POLICY LOCATION
                    }//END LOCATIONS LIST LOOP
                }
            }; //END LOCATION LIST


            System.out.println("GENERATE \n");


            // GENERATE POLICY
            myPolicy = new GeneratePolicy.Builder(driver)
                    //					.withAgentUserName(agent)
                    .withBusinessownersLine(boLine)
                    .withPolicyLocations(locationsLists)
                    .withCreateNew(CreateNew.Create_New_Always)
                    .withInsPersonOrCompanyRandom((policyName == null) ? name.getFirstName() : policyName.split(" ")[0], (policyName == null) ? name.getFirstName() : policyName.split(" ")[1], (policyName == null) ? name.getCompanyName() : policyName)
                    .withPolOrgType(orgType)
                    .withInsPrimaryAddress(new AddressInfo(true))
                    .withPaymentPlanType(paymentPlan)
                    .withDownPaymentType(paymentType)

                    .build(policyType);

            System.out.println(myPolicy.accountNumber);
            System.out.println(myPolicy.agentInfo.getAgentUserName());
            accountsCreated.add(myPolicy.accountNumber + " - " + myPolicy.agentInfo.getAgentUserName());
//			} catch (Exception e) {
//				System.out.println("######################\nFAILED TO GENERATE POLICY");
//
//				e.printStackTrace();
//				return false;
//			}// END MAIN TRY CATCH
            stopTime = new Date().getTime();
            System.out.println(((stopTime - startTime) / 1000) / 60);
        }//END POLICY FOR LOOP

        System.out.println("#########################\nACCOUNTS CREATED");
        System.out.println(accountsCreated);
        System.out.println("ACCOUNTS FAILED TO CREATE\n");
        System.out.println(failed);
        driver.quit();
        return true;

    }


    //LISTS FOR RANDOM ACCESS VARIABLES AND ENUMS

    private List<String> accountsCreated = new ArrayList<String>();
}

















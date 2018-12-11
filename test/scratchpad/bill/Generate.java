/**
 * @author bmartin Aug 7, 2015
 * @notes This test generates a policy.
 * The purpose of this test is to prepare a policy for other tests.
 */
package scratchpad.bill;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.enums.AdditionalInsuredRole;
import repository.gw.enums.AdditionalInterestType;
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
import repository.gw.enums.BuildingClassCode;
import repository.gw.enums.BusinessownersLine.LiabilityLimits;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
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
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;

//import bc.topmenu.TopMenuFactory;
//import bc.topmenu.interfaces.ITopMenu;
//import bc.topmenu.interfaces.ITopMenuAccount;
//import gw.enums.AdditionalInsuredRole;
//import gw.enums.AdditionalInterestBilling;
//import com.idfbins.enums.State;
//import gw.helpers.DateUtils;
public class Generate extends BaseTest {

    private WebDriver driver;

    @Test()
    public void generatePolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        // Number of policies to create
        int numPoliciesToGenerate = 1;
        // Number of locations to create
        final int numLocations = 2;
        // number of buildings per location
        final int numBuildingsPerLocation = 1;
        // What type of policy should be made
        GeneratePolicyType policyType = GeneratePolicyType.FullApp;
        // what type of payment type will be created
        PaymentPlanType paymentPlan = PaymentPlanType.Quarterly;
        // what type of payment method will be used
        PaymentType paymentType = PaymentType.Cash;
        long startTime = new Date().getTime();
        long stopTime = 0;

        ArrayList<PolicyLocation> locationsLists = null;

        // Start of policies to create loop
        for (int i = 0; i < numPoliciesToGenerate; i++) {
            try {
                OrganizationType orgType;
                if (new GuidewireHelpers(driver).getRandBoolean()) {
                    orgType = orgTypeCompany.get(NumberUtils.generateRandomNumberInt(0, (orgTypeCompany.size() - 1)));
                } else {
                    orgType = orgTypePerson.get(NumberUtils.generateRandomNumberInt(0, (orgTypePerson.size() - 1)));
                }
                String name1 = randomWords.get(NumberUtils.generateRandomNumberInt(0, (randomWords.size() - 1)));
                String name2 = randomWords.get(NumberUtils.generateRandomNumberInt(0, (randomWords.size() - 1)));

                // BUSINESS OWNERS LINE
                PolicyBusinessownersLine boLine = new PolicyBusinessownersLine(
                        smallBusiness.get(NumberUtils.generateRandomNumberInt(0, (smallBusiness.size() - 1)))) {
                    {
                        this.setLiabilityLimits(LiabilityLimits.Five00_1000_1000);
                        this.setAdditionalCoverageStuff(new PolicyBusinessownersLineAdditionalCoverages(false, true) {
                            {
                            }
                        }); // END ADDITIONAL COVERAGES
                        // Start BUSINESS OWNERS LINE ADDITIONAL INSURED LIS
                        this.setAdditonalInsuredBOLineList(new ArrayList<PolicyBusinessownersLineAdditionalInsured>() {
                            private static final long serialVersionUID = 1L;

                            {
                            }
                        }); // END BUSINESS OWNERS LINE ADDITIONAL INSURED LIST
                    }
                }; // END BUSINESS OWNERS LINE

                // Start of LOCATIONS
                locationsLists = new ArrayList<PolicyLocation>() {
                    private static final long serialVersionUID = 1L;

                    {
                        for (int k = 1; k <= numLocations; k++) {
                            this.add(new PolicyLocation(new AddressInfo(true), false) {
                                {
                                    this.setManualProtectionClassCode(ProtectionClassCode.Prot5);
                                    this.setAutoautoIncrease(AutoIncreaseBlgLimitPercentage.AutoInc2Perc);
                                    // Start ADDITIONAL COVERAGES STUFF
                                    this.setAdditionalCoveragesStuff(new PolicyLocationAdditionalCoverages() {
                                        {
                                        }
                                    }); // END ADDITIONAL COVERAGES STUFF
                                    this.setAdditionalInsuredLocationsList(
                                            new ArrayList<PolicyLocationAdditionalInsured>() {
                                                private static final long serialVersionUID = 1L;

                                                {
											/*this.add(new PolicyLocationAdditionalInsured(ContactSubType.Company) {
												{
													this.setAiRole(
															AdditionalInsuredRole.MortgageesAssigneesOrReceivers);
												}
											});*/
                                                }
                                            });

                                    for (int j = 1; j <= numBuildingsPerLocation; j++) {
                                        // LOCATION BULDING
                                        this.setBuildingList(new ArrayList<PolicyLocationBuilding>() {
                                            private static final long serialVersionUID = 1L;

                                            {
                                                this.add(new PolicyLocationBuilding() {
                                                    {
                                                        this.setUsageDescription("Building " + randomWords.get(NumberUtils.generateRandomNumberInt(0, (randomWords.size() - 1))));
                                                        this.setNamedInsuredOwner(false);
                                                        this.setOccupancySqFtPercOccupied(SqFtPercOccupied.SeventyFiveOneHundredPerc);
                                                        this.setOccupancyPercAreaLeasedToOthers(PercAreaLeasedToOthers.ZeroTenPerc);
                                                        this.setOccupancyNamedInsuredInterest(OccupancyInterestType.TenantOperator);
                                                        // this.setClassClassification(BuildingClassCode.ClothingorWearingApparelLadiesandGirlsCoatsSuitsandDresses.getClassification());
                                                        this.setClassCode(buildingClasses[new Random().nextInt(buildingClasses.length)].getClassCode());
                                                        // this.setClassCode(BuildingClassCode.MotelsNoRestaurants.getClassCode());
                                                        this.setBuildingLimit(NumberUtils.generateRandomNumberInt(5000, 300000));
                                                        this.setBuildingValuationMethod(ValuationMethod.ActualCashValue);
                                                        this.setBuildingCauseOfLoss(CauseOfLoss.Special);
                                                        this.setBuildingRoofExlusiongEndorsement(false);
                                                        this.setBuildingWindstormHailLossesToRoofSurfacing(false);

                                                        this.setBppLimit(NumberUtils.generateRandomNumberInt(5000, 300000));
                                                        this.setBppValuationMethod(ValuationMethod.ReplacementCost);
                                                        this.setBppCauseOfLoss(CauseOfLoss.Special);

                                                        this.setExteriorHouseKeepingAndMaintenance(HouseKeepingMaint.Superior);
                                                        this.setInteriorHouseKeepingAndMaintenance(HouseKeepingMaint.Superior);

                                                        // parking lot
                                                        // characteristics
                                                        this.setParkingLotCharacteristicsList(new ArrayList<ParkingLotSidewalkCharacteristics>() {
                                                            private static final long serialVersionUID = 1L;

                                                            {
                                                                this.add(parkingLotChar[new Random().nextInt(parkingLotChar.length)]);
                                                            }
                                                        }); // END PARKINGLOT
                                                        // CHARACERISTICS

                                                        // safety equipment
                                                        this.setSafetyEquipmentList(new ArrayList<SafetyEquipment>() {
                                                            private static final long serialVersionUID = 1L;

                                                            {
                                                                this.add(safty[new Random().nextInt(safty.length)]);
                                                            }
                                                        }); // END SAFETY
                                                        // EQUIPMENT LIST

                                                        this.setExitsProperlyMarked(true);
                                                        this.setNumFireExtinguishers(
                                                                NumberUtils.generateRandomNumberInt(1, 20));
                                                        this.setExposureToFlammablesChemicals(false);
                                                        // NEXT AREA IS IF THE ABOVE IS SET TO TRUE
                                                        this.setExposureToFlammablesChemicalsDesc("acids");

                                                        this.setConstructionType(ConstructionType.random());
                                                        this.setYearBuilt(2000);
                                                        this.setNumStories(1);
                                                        this.setNumBasements(0);
                                                        this.setTotalArea(String.valueOf(NumberUtils.generateRandomNumberInt(1000, 15000)));
                                                        this.setSprinklered(true);
                                                        this.setPhotoYear(1993);
                                                        this.setCostEstimatorYear(1999);

                                                        this.setRoofingType(roofType.get(NumberUtils.generateRandomNumberInt(0, (roofType.size() - 1))));
                                                        this.setFlatRoof(false);
                                                        this.setRoofCondition(RoofCondition.NoIssues);
                                                        this.setYearRoofReplaced(2000);

                                                        this.setWiringType(WiringType.Romex);
                                                        this.setBoxType(BoxType.CircuitBreaker);
                                                        this.setYearLastMajorWiringUpdate(2000);
                                                        this.setWiringUpdateDesc("Static Shock Inc. installed");

                                                        this.setYearLastMajorHeatingUpdate(2000);
                                                        this.setHeatingUpdateDesc("Mr. Freeze Inc. installed");

                                                        this.setYearLastMajorPlumbingUpdate(2000);
                                                        this.setPlumbingUpdateDesc("Mario and Brothers Inc. installed");

                                                        this.setExistingDamage(false);
                                                        // NEXT AREA IS IF THE ABOVE IS SET TO TRUE
                                                        this.setExistingDamageDesc("Joker gag reels");

                                                        this.setInsuredPropertyWithin100Ft(false);
                                                        // NEXT AREA IS IF THE ABOVE IS SET TO TRUE
                                                        this.setInsuredPropertyWithin100FtPolicyHolderName("Wayne Industries");
                                                        this.setInsuredPropertyWithin100FtPolicyNumber("08-353652-01");
                                                        this.setAdditionalCoveragesStuff(
                                                                new PolicyLocationBuildingAdditionalCoverages() {
                                                                    {
                                                                        this.setSpoilageCoverage(false);
                                                                    }
                                                                });
                                                        this.setAdditionalInterestList(
                                                                new ArrayList<AdditionalInterest>() {
                                                                    private static final long serialVersionUID = 1L;

                                                                    {
                                                                        this.add(new AdditionalInterest(
                                                                                ContactSubType.Company) {
                                                                            {
                                                                                // this.setAdditionalInterestType(AdditionalInterestType.Mortgagee);
                                                                                // this.setAdditionalInterestType(addIntrestType[new Random().nextInt(addIntrestType.length)]);
                                                                            }
                                                                        });
                                                                    }
                                                                }); // END BUILDING ADDITIONAL INTREST LIST
                                                    }
                                                }); // END BUILDING
                                            }
                                        }); // END BUILDING LIST
                                    } // END BUILDINGS FOR LOOP
                                }
                            });// END POLICY LOCATION
                        } // END LOCATIONS LIST LOOP
                    }
                }; // END LOCATION LIST

                System.out.println("GENERATE \n");

                // GENERATE POLICY
                myPolicy = new GeneratePolicy.Builder(driver)
                        .withBusinessownersLine(boLine)
                        .withPolicyLocations(locationsLists)
                        .withInsPersonOrCompanyRandom(name1, name2, name1 + name2)
//						.withInsPersonOrCompanyRandom(name1, name2, name1 + " " + name2)
                        .withPolOrgType(orgType)
                        .withInsPrimaryAddress(new AddressInfo(true))
                        .withPaymentPlanType(paymentPlan)
                        .withDownPaymentType(paymentType)
                        .build(policyType);

                System.out.println(myPolicy.accountNumber);
                System.out.println(myPolicy.agentInfo.getAgentUserName());
                accountsCreated.add(myPolicy.accountNumber + " - " + myPolicy.agentInfo.getAgentUserName());
            } catch (Exception e) {
                System.out.println("######################\nFAILED TO GENERATE POLICY");

                e.printStackTrace();
            } // END MAIN TRY CATCH
            stopTime = new Date().getTime();
            System.out.println(((stopTime - startTime) / 1000) / 60);
        } // END POLICY FOR LOOP

        System.out.println("#########################\nACCOUNTS CREATED");
        System.out.println(accountsCreated);
    }// END TEST


    // LISTS FOR RANDOM ACCESS VARIABLES AND ENUMS
    public GeneratePolicy myPolicy = null;
    private List<String> accountsCreated = new ArrayList<String>();

    @SuppressWarnings("unused")
    private List<AdditionalInsuredRole> roleList = new ArrayList<AdditionalInsuredRole>() {
        private static final long serialVersionUID = 1L;

        {
            this.add(AdditionalInsuredRole.LessorOfLeasedEquipment);
            this.add(AdditionalInsuredRole.ManagersOrLessorsOrPremises);
            this.add(AdditionalInsuredRole.Vendors);
            this.add(AdditionalInsuredRole.DesignatedPersonOrOrganization);
            this.add(AdditionalInsuredRole.GrantorOfFranchise);
        }
    };

    private List<SmallBusinessType> smallBusiness = new ArrayList<SmallBusinessType>() {
        private static final long serialVersionUID = 1L;

        {
            this.add(SmallBusinessType.Apartments);
            this.add(SmallBusinessType.Offices);
            this.add(SmallBusinessType.Condominium);
            this.add(SmallBusinessType.SelfStorageFacilities);
        }
    };

    private List<RoofingType> roofType = new ArrayList<RoofingType>() {
        private static final long serialVersionUID = 1L;

        {
            this.add(RoofingType.Aluminum);
            this.add(RoofingType.BuiltUpSmooth);
            this.add(RoofingType.BuiltUpTarGravel);
            this.add(RoofingType.Copper);
            this.add(RoofingType.FiberglassTranslucentPanels);
            this.add(RoofingType.MetalSandwichPanels);
            this.add(RoofingType.ShakesWood);
            this.add(RoofingType.ShinglesAsphalt);
            this.add(RoofingType.ShinglesFiberglass);
            this.add(RoofingType.SinglePlyMembrane);
            this.add(RoofingType.Slate);
            this.add(RoofingType.Steel);
            this.add(RoofingType.SteelPorcelainCoated);
            this.add(RoofingType.TileClay);
            this.add(RoofingType.TileConcrete);
            this.add(RoofingType.TinTerne);
        }
    };

    private List<String> randomWords = new ArrayList<String>() {
        private static final long serialVersionUID = 1L;

        {
            this.add("bat");
            this.add("super");
            this.add("wonder");
            this.add("cloud");
            this.add("moon");
            this.add("water");
            this.add("computer");
            this.add("school");
            this.add("network");
            this.add("hammer");
            this.add("walking");
            this.add("violently");
            this.add("mediocre");
            this.add("literature");
            this.add("woman");
            this.add("man");
            this.add("window");
            this.add("cords");
            this.add("musical");
            this.add("zebra");
            this.add("xylophone");
            this.add("penguin");
            this.add("home");
            this.add("dog");
            this.add("final");
            this.add("ink");
            this.add("teacher");
            this.add("boy");
            this.add("website");
            this.add("banana");
            this.add("uncle");
            this.add("softly");
            this.add("mega");
            this.add("girl");
            this.add("awesome");
            this.add("attatch");
            this.add("blue");
            this.add("internet");
            this.add("bottle");
            this.add("beetle");
            this.add("zone");
            this.add("tomato");
            this.add("jedi");
            this.add("hydro");
            this.add("cleaning");
            this.add("telivision");
            this.add("green");
            this.add("frog");
            this.add("cup");
            this.add("book");
            this.add("zooming");
            this.add("knight");
            this.add("arrow");
            this.add("gamer");
            this.add("lantern");
            this.add("spider");
            this.add("planet");
            this.add("captain");
            this.add("booster");
            this.add("gold");
            this.add("thudding");
            this.add("guitar");
            this.add("marvel");
            this.add("hair");
            this.add("soccer");
            this.add("aqua");
            this.add("rocket");
            this.add("table");
            this.add("late");
            this.add("media");
            this.add("desktop");
            this.add("flipper");
            this.add("club");
            this.add("flying");
            this.add("wayne");
            this.add("monster");
            this.add("purple");
            this.add("guardian");
            this.add("bold");
            this.add("hyperlink");
            this.add("presentation");
            this.add("world");
            this.add("national");
            this.add("comment");
            this.add("element");
            this.add("magic");
            this.add("lion");
            this.add("sand");
            this.add("crust");
            this.add("mac");
            this.add("jam");
            this.add("hunter");
            this.add("forest");
            this.add("foraging");
            this.add("silently");
            this.add("tawesomated");
            this.add("joshing");
            this.add("squirrel");
            this.add("hyperdrive");
            this.add("star");
            this.add("lord");
            this.add("raccoon");
            this.add("boom");
            this.add("interwebs");
            this.add("ingress");
            this.add("hero");
            this.add("insurance");
            this.add("comicbook");
            this.add("convention");
            this.add("kirk");
            this.add("spock");
            this.add("ant");
            this.add("atom");
            this.add("captain");
            this.add("general");
            this.add("complicated");
            this.add("pow");
            this.add("computer");
            this.add("hackers");
            this.add("geeks");
            this.add("nerds");
        }
    };

    private BuildingClassCode[] buildingClasses = BuildingClassCode.values();
    @SuppressWarnings("unused")
    private AdditionalInterestType[] addIntrestType = AdditionalInterestType.values();
    private ParkingLotSidewalkCharacteristics[] parkingLotChar = ParkingLotSidewalkCharacteristics.values();
    private SafetyEquipment[] safty = SafetyEquipment.values();

    private List<OrganizationType> orgTypeCompany = new ArrayList<OrganizationType>() {
        private static final long serialVersionUID = 1L;

        {
            this.add(OrganizationType.Partnership);
            this.add(OrganizationType.Joint_Venture);
            this.add(OrganizationType.Organization);
            this.add(OrganizationType.Trust);
        }
    };

    private List<OrganizationType> orgTypePerson = new ArrayList<OrganizationType>() {
        private static final long serialVersionUID = 1L;

        {
            this.add(OrganizationType.Individual);
            this.add(OrganizationType.Partnership);
            this.add(OrganizationType.Joint_Venture);
        }
    };

}

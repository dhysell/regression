package scratchpad.jon.templates;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;

import repository.driverConfiguration.Config;
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
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.Location.AutoIncreaseBlgLimitPercentage;
import repository.gw.enums.Location.ProtectionClassCode;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PolicyCoverage;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.StateInfo.Un_UnderInsuredMotoristLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPCommercialAuto;
import repository.gw.generate.custom.CPPCommercialAutoLine;
import repository.gw.generate.custom.CPPCommercialAutoStateInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationAdditionalCoverages;
import repository.gw.generate.custom.PolicyLocationAdditionalInsured;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.PolicyLocationBuildingAdditionalCoverages;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.NumberUtils;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.Names;
import persistence.globaldatarepo.helpers.NamesHelper;

@SuppressWarnings({"serial", "unused"})
public class GenerateTestPolicyTemp extends BaseTest {

    public GeneratePolicy myPolicy = null;
    private WebDriver driver;

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
    public Boolean generateRandomPolicy(ArrayList<PolicyCoverage> endorsements) throws Exception {
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


    @BeforeClass
    public void beforeClass() {

    }

    @AfterClass
    public void afterClass() {
    }




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

        int numPoliciesToGenerate = numberOfPolicies;
        final int numLocations = numberOfLocations;
        final int numBuildingsPerLocation = buildingsPerLocation;
        policyType = ((policyType == null) ? GeneratePolicyType.PolicyIssued : policyType);
        PaymentPlanType paymentPlan = PaymentPlanType.Annual;
        PaymentType paymentType = PaymentType.Cash;
        long startTime = new Date().getTime();
        long stopTime = 0;

        final AddressInfo pniAddress = new AddressInfo(true);

        ArrayList<PolicyLocation> locationsLists = null;

        //		for(String agent : agentNames) {
        for (int i = 0; i < numPoliciesToGenerate; i++) {
            //			try {

            OrganizationType orgType = OrganizationType.Joint_Venture;
            Names name = NamesHelper.getRandomName();


            // BUSINESS OWNERS LINE
            PolicyBusinessownersLine boLine = new PolicyBusinessownersLine(smallBusiness.get(NumberUtils.generateRandomNumberInt(0, (smallBusiness.size() - 1)))) {{
                this.setLiabilityLimits(LiabilityLimits.Five00_1000_1000);
                this.setAdditionalCoverageStuff(new PolicyBusinessownersLineAdditionalCoverages(false, true) {{
                    this.setAdditionalInsuredEngineersArchitectsSurveyorsCoverage(false);

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
                                        this.add(new PolicyLocationAdditionalInsured() {{
                                        }});

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
                                            this.setClassCode(buildingClasses[new Random().nextInt(buildingClasses.length)].getClassCode());
                                            //												this.setClassCode(BuildingClassCode.MotelsNoRestaurants.getClassCode());
                                            //												this.setClassCode("09621");
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
                                                    this.add(parkingLotChar[new Random().nextInt(parkingLotChar.length)]);
                                                }
                                            }); // END PARKINGLOT CHARACERISTICS

                                            //safety equipment
                                            this.setSafetyEquipmentList(new ArrayList<SafetyEquipment>() {
                                                private static final long serialVersionUID = 1L;

                                                {
                                                    this.add(safty[new Random().nextInt(safty.length)]);
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

                                            this.setRoofingType(roofType.get(NumberUtils.generateRandomNumberInt(0, (roofType.size() - 1))));
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

            CPPCommercialAuto commercialAuto = new CPPCommercialAuto() {{
//					this.setCommercialAutoLine(new CPPCommercialAutoLine(LiabilityLimit.OneMillion1M, DeductibleLiabilityCoverage.TwoThousandFiveHundred2500));
                this.setCommercialAutoLine(new CPPCommercialAutoLine());
                this.setVehicleList(new ArrayList<Vehicle>() {{
                    this.add(new Vehicle() {{
                        this.setMake("VW");
                        //this.setGaragedAt(pniAddress);
                    }});
                    this.add(new Vehicle() {{
                        this.setMake("Pinto");
                        this.setGaragedAt(pniAddress);
                    }});
                }});
                this.setCPP_CAStateInfo(new CPPCommercialAutoStateInfo(true, Un_UnderInsuredMotoristLimit.OneMillion1M, true, Un_UnderInsuredMotoristLimit.OneMillion1M));
                this.setDriversList(new ArrayList<Contact>() {{
                    this.add(new Contact() {{
                        this.setGender(Gender.Male);
                    }});
                    this.add(new Contact() {{
                    }});
                }});
            }};


            //LINE SELECTION
            ArrayList<LineSelection> lineSelectionList = new ArrayList<LineSelection>() {{
                this.add(LineSelection.CommercialAutoLineCPP);
//					this.add(LineSelection.CommercialPropertyLineCPP);
//					this.add(LineSelection.GeneralLiabilityLineCPP);
//					this.add(LineSelection.InlandMarineLineCPP);
                this.add(LineSelection.PersonalAutoLinePL);
                this.add(LineSelection.PropertyAndLiabilityLinePL);
            }};
            //END LINE SELECTION


            setEndorsments(new ArrayList<PolicyCoverage>() {{
                this.add(PolicyCoverage.AdditionalInsured_EngineersArchitectsOrSurveyorsBP_04_13);
            }}, locationsLists, boLine);

            System.out.println("GENERATE \n");

            Config cf = new Config(ApplicationOrCenter.PolicyCenter);
            driver = buildDriver(cf);
            // GENERATE POLICY
            myPolicy = new GeneratePolicy.Builder(driver)
                    //					.withAgentUserName(agent)


                    .withProductType(ProductLineType.CPP)
                    .withCPPCommercialAuto(commercialAuto)
                    .withLineSelection(LineSelection.CommercialAutoLineCPP, LineSelection.PersonalAutoLinePL)
                    .withBusinessownersLine(boLine)
                    .withPolicyLocations(locationsLists)
                    .withCreateNew(CreateNew.Create_New_Always)
                    .withInsPersonOrCompanyRandom((policyName == null) ? name.getFirstName() + i : policyName.split(" ")[0], (policyName == null) ? name.getFirstName() + i : policyName.split(" ")[1], (policyName == null) ? name.getCompanyName() + i : policyName + i)
                    .withPolOrgType(orgType)
//				.withPolTermLengthDays(79)
                    .withInsAge(40)
                    .withInsPrimaryAddress(pniAddress)
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
        return true;
    }


    //LISTS FOR RANDOM ACCESS VARIABLES AND ENUMS

    private List<String> accountsCreated = new ArrayList<String>();


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
            this.add("sausage");
            this.add("blubber");
            this.add("pencil");
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
            this.add("chair");
            this.add("two");
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
            this.add("fun");
            this.add("website");
            this.add("banana");
            this.add("uncle");
            this.add("softly");
            this.add("mega");
            this.add("ten");
            this.add("awesome");
            this.add("attatch");
            this.add("blue");
            this.add("internet");
            this.add("bottle");
            this.add("tight");
            this.add("zone");
            this.add("tomato");
            this.add("prison");
            this.add("hydro");
            this.add("cleaning");
            this.add("telivision");
            this.add("send");
            this.add("frog");
            this.add("cup");
            this.add("book");
            this.add("zooming");
            this.add("falling");
            this.add("evily");
            this.add("gamer");
            this.add("lid");
            this.add("juice");
            this.add("moniter");
            this.add("captain");
            this.add("bonding");
            this.add("loudly");
            this.add("thudding");
            this.add("guitar");
            this.add("shaving");
            this.add("hair");
            this.add("soccer");
            this.add("water");
            this.add("racket");
            this.add("table");
            this.add("late");
            this.add("media");
            this.add("desktop");
            this.add("flipper");
            this.add("club");
            this.add("flying");
            this.add("smooth");
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
            this.add("toast");
            this.add("jam");
            this.add("hunter");
            this.add("forest");
            this.add("foraging");
            this.add("silently��");
            this.add("tawesomated");
            this.add("joshing");
            this.add("pong");
        }
    };

    private BuildingClassCode[] buildingClasses = BuildingClassCode.values();
    private AdditionalInterestType[] addIntrestType = AdditionalInterestType.values();
    private ConstructionType[] constructionType = ConstructionType.values();
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


    List<String> agentNames = new ArrayList<String>() {{
        this.add("bcrofoot");
        this.add("jduke");
        this.add("manderson");
        this.add("tclark");
        this.add("cfaulkner");
        this.add("tferguson");
        this.add("kjeppesen2�");
        this.add("jnichols");
        this.add("rreilly");
        this.add("cstephenson");
        this.add("ehardy");
        this.add("njensen");
        this.add("jpetersen");
        this.add("krichardson");
        this.add("kjeppesen");
        this.add("skunz");
        this.add("bmerrill");
        this.add("rsorenson");
        this.add("tgallup");
        this.add("rharrigfeld");
        this.add("vprice�");
    }};


    private void setEndorsments(ArrayList<PolicyCoverage> coverages, ArrayList<PolicyLocation> locationsLists, PolicyBusinessownersLine boLine) {


        for (PolicyCoverage coverage : coverages) {

            switch (coverage.getValue()) {
                case "Employee Dishonesty-Optional Coverage":
                    boLine.getAdditionalCoverageStuff().setEmployeeDishonestyCoverage(true);

                    break;
                case "Forgery and Alteration":
                    //must set emp dishonesty first
                    boLine.getAdditionalCoverageStuff().setEmployeeDishonestyCoverage(true);

                    break;
                case "Theft of Client's Property BP 14 03":
                    //must set emp dishonesty first
                    boLine.getAdditionalCoverageStuff().setEmployeeDishonestyCoverage(true);

                    break;
                case "Hired Auto BP 04 04":
                    boLine.getAdditionalCoverageStuff().setHiredAutoCoverage(true);
                    break;
            }


        }


    }


}

















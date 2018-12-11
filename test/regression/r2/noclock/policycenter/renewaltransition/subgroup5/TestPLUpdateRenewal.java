package regression.r2.noclock.policycenter.renewaltransition.subgroup5;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.FPP.FPPCoverageTypes;
import repository.gw.enums.FPP.FPPDeductible;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.BoatType;
import repository.gw.enums.InlandMarineTypes.CargoClass;
import repository.gw.enums.InlandMarineTypes.ClassIIICargoTypes;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.InlandMarineTypes.RecreationalEquipmentType;
import repository.gw.enums.InlandMarineTypes.WatercraftTypes;
import repository.gw.enums.InlandMarineTypes.WatercratItems;
import repository.gw.enums.LineSelection;
import repository.gw.enums.LivestockDeductible;
import repository.gw.enums.LivestockScheduledItemType;
import repository.gw.enums.LivestockType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PersonalPropertyDeductible;
import repository.gw.enums.PersonalPropertyType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.ElectricalSystem;
import repository.gw.enums.Property.FoundationType;
import repository.gw.enums.Property.Garage;
import repository.gw.enums.Property.KitchenBathClass;
import repository.gw.enums.Property.NumberOfStories;
import repository.gw.enums.Property.NumberOfUnits;
import repository.gw.enums.Property.Plumbing;
import repository.gw.enums.Property.PrimaryHeating;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.RoofType;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.Property.SectionIIMedicalLimit;
import repository.gw.enums.Property.SprinklerSystemType;
import repository.gw.enums.Property.Wiring;
import repository.gw.enums.RenewalCode;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.TransactionType;
import repository.gw.enums.Vehicle.PAComprehensive_CollisionDeductible;
import repository.gw.enums.Vehicle.Usage;
import repository.gw.enums.Vehicle.VehicleTruckTypePL;
import repository.gw.enums.Vehicle.VehicleType;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.Cargo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.Livestock;
import repository.gw.generate.custom.LivestockList;
import repository.gw.generate.custom.LivestockScheduledItem;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PersonalProperty;
import repository.gw.generate.custom.PersonalPropertyList;
import repository.gw.generate.custom.PersonalPropertyScheduledItem;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.RecreationalEquipment;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireFPP;
import repository.gw.generate.custom.SquireIMCargo;
import repository.gw.generate.custom.SquireIMWatercraft;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquireLiablityCoverageLivestockItem;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.Vehicle;
import repository.gw.generate.custom.WatercraftScheduledItems;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_AdditionalCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_Cargo;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_FarmEquipment;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_Livestock;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_PersonalProperty;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_RecreationalEquipment;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_Watercraft;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;
import repository.pc.workorders.renewal.RenewalInformation;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import persistence.globaldatarepo.helpers.VINHelper;

/**
 * @Author nvadlamudi
 * @Requirement : US4769: Update Renewal, US12285: Renewal Information Screen needs to allow the value of "0" to be entered for the renewal odometer
 * @RequirementsLink <a href=
 * "http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Homeowners/PC8%20-%20HO%20-%20Renewal%20-%20Update%20Renewal.xlsx">Link
 * Text</a>
 * @Description
 * @DATE Oct 20, 2016
 */
@QuarantineClass
public class TestPLUpdateRenewal extends BaseTest {
    private GeneratePolicy mySquirePolicyObjPL = null;
    private Underwriters uw;

    private WebDriver driver;

    @Test()
    public void testIssueSquirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Vin vin = VINHelper.getRandomVIN();
        // Cargo
        Cargo cargoTrailer1 = new Cargo(VehicleType.Trailer, vin.getVin(), Integer.parseInt(vin.getYear()), vin.getMake(), vin.getModel());
        cargoTrailer1.setLimit("3123");
        ArrayList<Cargo> cargoTrailers = new ArrayList<Cargo>();
        cargoTrailers.add(cargoTrailer1);
        ArrayList<String> cargoAddInsured = new ArrayList<String>();
        cargoAddInsured.add("Cor Hofman");
        SquireIMCargo stuff = new SquireIMCargo(CargoClass.ClassIII, ClassIIICargoTypes.Milk, 450, 1000, "Brenda Swindle", false, cargoTrailers, cargoAddInsured);
        SquireIMCargo cargo = new SquireIMCargo(CargoClass.ClassI, 100, 1000, "Brenda Swindle", false, cargoTrailers);
        ArrayList<SquireIMCargo> cargoList = new ArrayList<SquireIMCargo>();
        cargoList.add(cargo);
        cargoList.add(stuff);

        // Watercraft
        WatercraftScheduledItems boat = new WatercraftScheduledItems(WatercraftTypes.BoatAndMotor, DateUtils.convertStringtoDate("01/01/1999", "MM/dd/yyyy"), 10000);
        boat.setLimit(3123);
        boat.setItem(WatercratItems.Boat);
        boat.setLength(28);
        boat.setBoatType(BoatType.Outboard);

        ArrayList<WatercraftScheduledItems> boats = new ArrayList<WatercraftScheduledItems>();
        boats.add(boat);
        ArrayList<String> boatAddInsured = new ArrayList<String>();
        boatAddInsured.add("Cor Hofman");
        ArrayList<AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
        AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Person);
        loc2Bldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
        loc2Bldg1AddInterest.setNewContact(CreateNew.Create_New_Always);
        loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);
        SquireIMWatercraft boatType = new SquireIMWatercraft(WatercraftTypes.BoatAndMotor, PAComprehensive_CollisionDeductible.OneHundred100, boats);
        boatType.setAdditionalInterest(loc2Bldg1AdditionalInterests);
        ArrayList<SquireIMWatercraft> boatTypes = new ArrayList<SquireIMWatercraft>();
        boatTypes.add(boatType);

        // Rec Equipment
        ArrayList<RecreationalEquipment> recVehicle = new ArrayList<RecreationalEquipment>();
        recVehicle.add(new RecreationalEquipment(RecreationalEquipmentType.Snowmobile, "16561", PAComprehensive_CollisionDeductible.Fifty50, "Bill Martin"));

        // Farm Equipment
        IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment", "Manly Farm Equipment", 1000);
        ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
        farmEquip.add(farmThing);

        FarmEquipment imFarmEquip = new FarmEquipment(IMFarmEquipmentType.FarmEquipment, CoverageType.BroadForm, IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
        imFarmEquip.setAdditionalInterests(loc2Bldg1AdditionalInterests);
        ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
        allFarmEquip.add(imFarmEquip);

        // Personal Property
        PersonalPropertyScheduledItem golfStuff = new PersonalPropertyScheduledItem();
        golfStuff.setDescription("Specialty Golf Clubs");
        golfStuff.setLimit(12345);

        ArrayList<PersonalPropertyScheduledItem> propertyList = new ArrayList<PersonalPropertyScheduledItem>();
        propertyList.add(golfStuff);

        PersonalProperty pprop = new PersonalProperty();
        pprop.setType(PersonalPropertyType.GolfEquipment);
        pprop.setDeductible(PersonalPropertyDeductible.Ded25);
        pprop.setScheduledItems(propertyList);
        pprop.setDescription("Testdescription");
        pprop.setLimit(12345);

        ArrayList<String> ppropAdditIns = new ArrayList<String>();
        ppropAdditIns.add("First Guy");
        pprop.setAdditionalInsureds(ppropAdditIns);

        PersonalPropertyList ppropList = new PersonalPropertyList();
        ppropList.setGolfEquipment(pprop);

        // Live Stock
        Livestock livestock = new Livestock();
        livestock.setType(LivestockType.Livestock);
        livestock.setDeductible(LivestockDeductible.Ded100);
        LivestockScheduledItem livSchedItem1 = new LivestockScheduledItem(livestock.getType(), LivestockScheduledItemType.Alpaca, "Test Alpaca 1", 1000);
        ArrayList<String> livSchedItem2AdditInsureds = new ArrayList<String>();
        livSchedItem2AdditInsureds.add("Sched Item Guy1");

        ArrayList<LivestockScheduledItem> livSchedItems = new ArrayList<LivestockScheduledItem>();
        livSchedItems.add(livSchedItem1);

        livestock.setScheduledItems(livSchedItems);
        ArrayList<String> livAdditInsureds = new ArrayList<String>();
        livAdditInsureds.add("Cor Hofman");
        livestock.setAdditionalInsureds(livAdditInsureds);

        Livestock deathOfLivestock = new Livestock();
        deathOfLivestock.setType(LivestockType.DeathOfLivestock);
        LivestockScheduledItem dolSchedItem1 = new LivestockScheduledItem(deathOfLivestock.getType(), LivestockScheduledItemType.SheepD, 500, "Test Sheep Hogs");
        ArrayList<LivestockScheduledItem> dolSchedItems = new ArrayList<LivestockScheduledItem>();
        dolSchedItems.add(dolSchedItem1);
        deathOfLivestock.setScheduledItems(dolSchedItems);
        ArrayList<String> dolAdditInsureds = new ArrayList<String>();
        dolAdditInsureds.add("Cor Hofman");
        deathOfLivestock.setAdditionalInsureds(dolAdditInsureds);

        // Add fourH stuff here
        Livestock fourH = null;

        LivestockList allLivestock = new LivestockList(livestock, deathOfLivestock, fourH);
        ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
        imTypes.add(InlandMarine.Livestock);

        ArrayList<InlandMarine> imCoverages = new ArrayList<InlandMarine>();
        imCoverages.add(InlandMarine.Cargo);
        imCoverages.add(InlandMarine.FarmEquipment);
        imCoverages.add(InlandMarine.RecreationalEquipment);
        imCoverages.add(InlandMarine.PersonalProperty);
        imCoverages.add(InlandMarine.Livestock);
        imCoverages.add(InlandMarine.Watercraft);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);


        SquireLiablityCoverageLivestockItem livestockSectionIICowCoverage = new SquireLiablityCoverageLivestockItem();
        livestockSectionIICowCoverage.setQuantity(100);
        livestockSectionIICowCoverage.setType(LivestockScheduledItemType.Cow);

        SquireLiablityCoverageLivestockItem livestockSectionIIHorseCoverage = new SquireLiablityCoverageLivestockItem();
        livestockSectionIIHorseCoverage.setQuantity(100);
        livestockSectionIIHorseCoverage.setType(LivestockScheduledItemType.Horse);

        ArrayList<SquireLiablityCoverageLivestockItem> coveredLivestockItems = new ArrayList<SquireLiablityCoverageLivestockItem>();
        coveredLivestockItems.add(livestockSectionIIHorseCoverage);
        SectionIICoverages sectionIIcoverages = new SectionIICoverages(SectionIICoveragesEnum.Livestock, coveredLivestockItems);

        myLiab.getSectionIICoverageList().add(sectionIIcoverages);

        SquireFPP squireFPP = new SquireFPP(FPPFarmPersonalPropertySubTypes.Tractors, FPPFarmPersonalPropertySubTypes.HarvestersHeaders, FPPFarmPersonalPropertySubTypes.CirclePivots, FPPFarmPersonalPropertySubTypes.WheelLines);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locationsList.add(locToAdd);

        // Coverages
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK, true, UninsuredLimit.Fifty, true, UnderinsuredLimit.Fifty);
        coverages.setAccidentalDeath(true);

        // driver
        ArrayList<Contact> driversList = new ArrayList<Contact>();
        Contact person1 = new Contact("Test" + StringsUtils.generateRandomNumberDigits(6), "Senior", Gender.Male, DateUtils.convertStringtoDate("01/01/1990", "MM/dd/yyyy"));
        driversList.add(person1);

        // Vehicle
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle toAdd = new Vehicle();
        toAdd.setVehicleTypePL(VehicleTypePL.PrivatePassenger);
        toAdd.setDriverPL(person1);

        Vehicle veh1 = new Vehicle();
        veh1.setVehicleTypePL(VehicleTypePL.FarmTruck);
        veh1.setTruckType(VehicleTruckTypePL.UnderFour);
        veh1.setVin(vin.getVin());
        veh1.setMake(vin.getMake());
        veh1.setModel(vin.getModel());
        veh1.setModelYear(Integer.parseInt(vin.getYear()));
        veh1.setGvw(51000);
        veh1.setUsage(Usage.FarmUseWithOccasionalHire);

        Vehicle veh2 = new Vehicle();
        veh2.setVehicleTypePL(VehicleTypePL.ShowCar);
        veh2.setComprehensive(true);
        veh2.setCollision(true);
        Vin anotherVin = VINHelper.getRandomVIN();
        veh2.setVin(anotherVin.getVin());
        veh2.setMake(anotherVin.getMake());
        veh2.setModel(anotherVin.getModel());
        veh2.setModelYear(Integer.parseInt(anotherVin.getYear()));
        vehicleList.add(toAdd);
        vehicleList.add(veh2);
        vehicleList.add(veh1);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);
        squirePersonalAuto.setVehicleList(vehicleList);
        squirePersonalAuto.setDriversList(driversList);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;
        myPropertyAndLiability.squireFPP = squireFPP;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.watercrafts_PL_IM = boatTypes;
        myInlandMarine.cargo_PL_IM = cargoList;
        myInlandMarine.recEquipment_PL_IM = recVehicle;
        myInlandMarine.farmEquipment = allFarmEquip;
        myInlandMarine.personalProperty_PL_IM = ppropList.getAllPersonalPropertyAsList();
        myInlandMarine.livestock_PL_IM = allLivestock.getAllLivestockAsList();
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;


        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;

        mySquirePolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL, LineSelection.InlandMarineLinePL)
                .withInsFirstLastName("Update", "Quote")
                .withPolTermLengthDays(80)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

    }

    @Test(dependsOnMethods = {"testIssueSquirePolicy"})
    private void testIssueRenewalWithRenewalInfo() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySquirePolicyObjPL.squire.getPolicyNumber());

        StartRenewal renewal = new StartRenewal(driver);
        renewal.startRenewal();

        InfoBar infoBar = new InfoBar(driver);
        infoBar.clickInfoBarPolicyNumber();

        renewal.waitForPreRenewalDirections();

        PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
        preRenewalPage.closePreRenewalExplanations(mySquirePolicyObjPL);

        PolicySummary policySummary = new PolicySummary(driver);
        policySummary.clickPendingTransaction(TransactionType.Renewal);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickRenewalInformation();

        boolean testFailed = false;
        String errorMessage = "";
        RenewalInformation renewalInfo = new RenewalInformation(driver);
        renewalInfo.setCustomFarming(1000);
        for (Vehicle veh : this.mySquirePolicyObjPL.squire.squirePA.getVehicleList()) {
            if (veh.getVehicleTypePL().equals(VehicleTypePL.ShowCar) || veh.getVehicleTypePL().equals(VehicleTypePL.FarmTruck)) {

                String actualVin = renewalInfo.getRenewalSectionIIIDetailsBySpecificVehicle(veh.getModelYear() + " " + veh.getMake(), "VIN");
                if (!veh.getVin().contains(actualVin)) {
                    testFailed = true;
                    errorMessage = errorMessage + " Expected Vin : " + veh.getVin() + " Actual Vin: " + actualVin + "\n";
                }
                String odometer = renewalInfo.getRenewalSectionIIIDetailsBySpecificVehicle(veh.getModelYear() + " " + veh.getMake(), "Current Odometer");
                if (veh.getOdometer() != (Integer.parseInt(odometer))) {
                    testFailed = true;
                    errorMessage = errorMessage + " Expected Current Odometer : " + veh.getOdometer()
                            + " Actual Odometer: " + odometer + "\n";
                }

                String vehicleNumber = renewalInfo.getRenewalSectionIIIDetailsBySpecificVehicle(veh.getModelYear() + " " + veh.getMake(), "Vehicle Number");
                int vehicleNum = Integer.parseInt(vehicleNumber.trim().substring(vehicleNumber.length() - 1, vehicleNumber.length()));

                int updatedOdometer = Integer.parseInt(odometer) + 1000;
                renewalInfo.setRenewalOdometerByVehicleNumber(vehicleNum, "" + 0);
                new GuidewireHelpers(driver).clickNext();
                GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
                if (!guidewireHelpers.errorMessagesExist() && (!guidewireHelpers.getFirstErrorMessage().contains("Renewal Odometer : cannot be less than previous odometer value"))) {
                    testFailed = true;
                    errorMessage = errorMessage + "Expected page validation : 'Renewal Odometer : cannot be less than previous odometer value' is not displayed /n";
                }

                renewalInfo.setRenewalOdometerByVehicleNumber(vehicleNum, String.valueOf(updatedOdometer));
                new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
            }
        }

        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverageScreen = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        double coverageALimit = this.mySquirePolicyObjPL.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getPropertyCoverages().getCoverageA().getLimit();
        double newValue = coverageALimit + (coverageALimit * 2 / 100);
        if (coverageScreen.getCoverageALimit() != newValue) {
            testFailed = true;
            errorMessage = errorMessage + "2% inflation guard is not entered . \n";
        }

        sideMenu.clickSideMenuPACoverages();
        GenericWorkorderSquireAutoCoverages_AdditionalCoverages autoCoveragePage = new GenericWorkorderSquireAutoCoverages_AdditionalCoverages(driver);
        autoCoveragePage.clickAdditionalCoveragesTab();
        if (!autoCoveragePage.getAccidentalDeath()) {
            testFailed = true;
            errorMessage = errorMessage + "Accidental Death is not selected \n";
        }

        // modifying the changes in section I, Section II
        // Locations
        sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        propertyLocations.clickEditLocation(1);
//        propertyLocations.setNumberOfResidence(12);
        propertyLocations.clickOK();

        // Property
        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetails = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);

        // Adding property with Owner name
        propertyDetails.clickAdd();
        propertyDetails.setPropertyType(PropertyTypePL.DwellingPremises);
        propertyDetails.AddExistingOwner();
        propertyDetails.setDwellingVacantRadio(false);
        propertyDetails.setUnits(NumberOfUnits.OneUnit);
        propertyDetails.setWoodFireplaceRadio(false);
        propertyDetails.setSwimmingPoolRadio(false);
        propertyDetails.setWaterLeakageRadio(false);
        propertyDetails.setExoticPetsRadio(false);
        propertyDetails.clickPropertyConstructionTab();
        enterConstructionProptectionPageDetails();
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverages.clickSpecificBuilding(1, 2);
        coverages.setCoverageALimit(200200);
        coverages.setCoverageCValuation(mySquirePolicyObjPL.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getPropertyCoverages());
        sideMenu.clickSideMenuPropertyLocations();

        // Removing property
        sideMenu.clickSideMenuSquirePropertyDetail();
        propertyDetails.setCheckBoxByRowInPropertiesTable(1, true);
        propertyDetails.clickRemoveProperty();

        // Edit property
        propertyDetails.clickEdit();
        propertyDetails.setUnits(NumberOfUnits.ThreeUnits);
        propertyDetails.setRisk("A");

        // Add Insured
        propertyDetails.addAddtionalInsured();
        propertyDetails.setAddtionalInsuredName("stevejobs");
        propertyDetails.clickOk();

        // Coverages
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);

        // Changing Section1Deductible
        coverage.selectSectionIDeductible(SectionIDeductible.OneThousand);

        // Changing Property Coverage
        coverage.setCoverageALimit(200200);

        // FPP Coverage Reduced
        coverage.clickFarmPersonalProperty();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages fppCovs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages(driver);
        fppCovs.selectCoverageType(FPPCoverageTypes.BlanketExclude);
        fppCovs.selectDeductible(FPPDeductible.Ded_1000);

        // Changing SectionIIDeductible
        coverage.clickSectionIICoveragesTab();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages section2Covs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
        section2Covs.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);
        section2Covs.setMedicalLimit(SectionIIMedicalLimit.Limit_2000);

        sideMenu.clickSideMenuPACoverages();
        GenericWorkorderSquireAutoCoverages paCoverages = new GenericWorkorderSquireAutoCoverages(driver);
        mySquirePolicyObjPL.squire.squirePA.getCoverages().setLiability(LiabilityLimit.FiftyHigh);
        paCoverages.fillOutSquireAutoCoverages_Coverages(mySquirePolicyObjPL);

        sideMenu.clickSideMenuIMCoveragePartSelection();
        sideMenu.clickSideMenuIMRecreationVehicle();

        // Changing Recreational Equipment Deductible
        GenericWorkorderSquireInlandMarine_RecreationalEquipment recreationalEquipment = new GenericWorkorderSquireInlandMarine_RecreationalEquipment(driver);
        recreationalEquipment.clickLinkInRecreationalEquipmentTable(1);
        recreationalEquipment.selectDeductible("100");
        recreationalEquipment.clickOK();

        // Changing WaterCraft Deductible
        sideMenu.clickSideMenuIMWatercraft();
        GenericWorkorderSquireInlandMarine_Watercraft watercraft = new GenericWorkorderSquireInlandMarine_Watercraft(driver);
        watercraft.clickEditButton();
        watercraft.selectDeductible("250");
        watercraft.clickOK();

        // Changing FarmEquipment
        sideMenu.clickSideMenuIMFarmEquipment();
        GenericWorkorderSquireInlandMarine_FarmEquipment farmEquipment = new GenericWorkorderSquireInlandMarine_FarmEquipment(driver);
        farmEquipment.clickEditButton();
        farmEquipment.setDeductible(IMFarmEquipmentDeductible.TwoHundredFifty);
        farmEquipment.clickOk();

        // Changing Personal Property
        sideMenu.clickSideMenuIMPersonalEquipment();
        GenericWorkorderSquireInlandMarine_PersonalProperty personalProperty = new GenericWorkorderSquireInlandMarine_PersonalProperty(driver);
        personalProperty.clickEditButton();
        personalProperty.setDeductible(PersonalPropertyDeductible.Ded250);
        personalProperty.clickOk();

        // Changing Cargo
        sideMenu.clickSideMenuIMCargo();
        GenericWorkorderSquireInlandMarine_Cargo cargo = new GenericWorkorderSquireInlandMarine_Cargo(driver);
        cargo.clickEditButtonByRowInCargoCoverageTable(2);
        cargo.setRadius("300");
        cargo.clickOKButton();

        // Changing LiveStock
        sideMenu.clickSideMenuIMLivestock();
        GenericWorkorderSquireInlandMarine_Livestock livestock = new GenericWorkorderSquireInlandMarine_Livestock(driver);
        livestock.clickEditButton();
        livestock.setDeductible(LivestockDeductible.Ded50);
        livestock.clickOk();

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis_UWIssues riskAnaysis = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        riskAnaysis.Quote();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
            riskAnaysis.approveAll_IncludingSpecial();
            riskAnaysis.Quote();
        }
        sideMenu.clickSideMenuQuote();
        sideMenu.clickSideMenuForms();
        sideMenu.clickSideMenuPayment();

        renewal.clickRenewPolicy(RenewalCode.Renew_Good_Risk);
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        completePage.waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());

        if (testFailed)
            Assert.fail(errorMessage);

    }

    private void enterConstructionProptectionPageDetails() throws GuidewireNavigationException {
        // Enter construction details
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        constructionPage.setYearBuilt(1993);
        constructionPage.setConstructionType(ConstructionTypePL.Frame);
        constructionPage.setBathClass(KitchenBathClass.Basic);
        constructionPage.setSquareFootage(1700);
        constructionPage.setStories(NumberOfStories.One);
        constructionPage.setBasementFinished("90");
        constructionPage.setGarage(Garage.AttachedGarage);
        constructionPage.setLargeShed(false);
        constructionPage.setCoveredPorches(false);
        constructionPage.setFoundationType(FoundationType.FullBasement);
        constructionPage.setRoofType(RoofType.WoodShingles);
        constructionPage.setPrimaryHeating(PrimaryHeating.Gas);
        constructionPage.setPlumbing(Plumbing.Copper);
        constructionPage.setWiring(Wiring.Copper);
        constructionPage.setElectricalSystem(ElectricalSystem.CircuitBreaker);
        constructionPage.setAmps(100);
        constructionPage.setKitchenClass(KitchenBathClass.Basic);
        constructionPage.clickProtectionDetailsTab();
        GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);
        protectionPage.setSprinklerSystemType(SprinklerSystemType.Full);
        protectionPage.setFireExtinguishers(true);
        protectionPage.setSmokeAlarm(true);
        protectionPage.setNonSmoker(true);
        protectionPage.setDeadBoltLocks(true);
        protectionPage.setDefensibleSpace(true);
        protectionPage.clickOK();
    }

    //US12285: Renewal Information Screen needs to allow the value of "0" to be entered for the renewal odometer
    @Test()
    private void testValidateZeroRenewalInformation() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locationsList.add(locToAdd);

        // Vehicle
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle toAdd = new Vehicle();
        toAdd.setVehicleTypePL(VehicleTypePL.PrivatePassenger);

        Vehicle veh1 = new Vehicle();
        veh1.setVehicleTypePL(VehicleTypePL.FarmTruck);
        veh1.setTruckType(VehicleTruckTypePL.UnderFour);
        veh1.setGvw(51000);
        veh1.setOdometer(0);
        veh1.setUsage(Usage.FarmUseWithOccasionalHire);
        vehicleList.add(toAdd);
        vehicleList.add(veh1);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setVehicleList(vehicleList);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;

        Squire mySquire = new Squire();
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        GeneratePolicy myRenewPolObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Update", "RenewalInfo")
                .withPolTermLengthDays(80)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();

        // Login with UW
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        Login login = new Login(driver);
        login.loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myRenewPolObjPL.squire.getPolicyNumber());

        StartRenewal renewal = new StartRenewal(driver);
        renewal.startRenewal();

        InfoBar infoBar = new InfoBar(driver);
        infoBar.clickInfoBarPolicyNumber();

        renewal.waitForPreRenewalDirections();

        PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
        preRenewalPage.closePreRenewalExplanations(myRenewPolObjPL);

        PolicySummary policySummary = new PolicySummary(driver);
        policySummary.clickPendingTransaction(TransactionType.Renewal);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickRenewalInformation();

        boolean testFailed = false;
        String errorMessage = "";
        RenewalInformation renewalInfo = new RenewalInformation(driver);
        renewalInfo.setRenewalOdometerByVehicleNumber(2, "" + 0);
        new GuidewireHelpers(driver).clickNext();
        if (guidewireHelpers.errorMessagesExist() && (!guidewireHelpers.getFirstErrorMessage().contains("Renewal Odometer : cannot be less than previous odometer value"))) {
            testFailed = true;
            errorMessage = errorMessage + "UnExpected page validation : 'Renewal Odometer : cannot be less than previous odometer value' is not displayed /n";
        }

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis_UWIssues riskAnaysis = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        riskAnaysis.Quote();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
            riskAnaysis.approveAll_IncludingSpecial();
            riskAnaysis.Quote();
        }
        sideMenu.clickSideMenuQuote();
        sideMenu.clickSideMenuForms();
        sideMenu.clickSideMenuPayment();

        renewal.clickRenewPolicy(RenewalCode.Renew_Good_Risk);
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        completePage.waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());

        if (testFailed)
            Assert.fail(errorMessage);

    }

}

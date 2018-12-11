package regression.r2.noclock.policycenter.submission_misc.subgroup4;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InlandMarineTypes.BoatType;
import repository.gw.enums.InlandMarineTypes.CargoClass;
import repository.gw.enums.InlandMarineTypes.ClassIIICargoTypes;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.InlandMarineTypes.WatercraftTypes;
import repository.gw.enums.InlandMarineTypes.WatercratItems;
import repository.gw.enums.LineSelection;
import repository.gw.enums.LivestockScheduledItemType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.Vehicle.PAComprehensive_CollisionDeductible;
import repository.gw.enums.Vehicle.VehicleType;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.Cargo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PLPropertyCoverages;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
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
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_Cargo;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_Watercraft;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.VINHelper;

/**
 * @Author nvadlamudi
 * @Requirement :US9109 : Add Item Number to Watercraft and Cargo
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Sep 15, 2016
 */
public class TestSquireIMCargoWatercraftItems extends BaseTest {
    private GeneratePolicy myPolicyObjPL;

    private WebDriver driver;

    @Test()
    public void testCreateSquireIM() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Vin vin = VINHelper.getRandomVIN();

        PLPropertyCoverages propertyCoverages = new PLPropertyCoverages();
        propertyCoverages.getCoverageA().setIncreasedReplacementCost(false);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        PLPolicyLocationProperty prop1 = new PLPolicyLocationProperty();
        prop1.setpropertyType(PropertyTypePL.ResidencePremises);
        prop1.setConstructionType(ConstructionTypePL.Frame);
        locOnePropertyList.add(prop1);
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locToAdd.setPlNumResidence(5);
        locationsList.add(locToAdd);


        // Cargo
        Cargo cargoTrailer1 = new Cargo(VehicleType.Trailer, vin.getVin(), Integer.parseInt(vin.getYear()), vin.getMake(), vin.getModel());
        cargoTrailer1.setLimit("3123");
        ArrayList<Cargo> cargoTrailers = new ArrayList<Cargo>();
        cargoTrailers.add(cargoTrailer1);
        ArrayList<String> cargoAddInsured = new ArrayList<String>();
        cargoAddInsured.add("Cor Hofman");
        SquireIMCargo stuff = new SquireIMCargo(CargoClass.ClassIII, ClassIIICargoTypes.Milk, 100, 1000, "Brenda Swindle", false, cargoTrailers, cargoAddInsured);
        SquireIMCargo stuff2 = new SquireIMCargo(CargoClass.ClassII, 100, 1000, "Brenda Swindle", false, cargoTrailers);

        SquireIMCargo cargo = new SquireIMCargo(CargoClass.ClassI, 100, 1000, "Brenda Swindle", false, cargoTrailers);
        ArrayList<SquireIMCargo> cargoList = new ArrayList<SquireIMCargo>();
        cargoList.add(cargo);
        cargoList.add(stuff);
        cargoList.add(stuff2);

        // Watercraft
        ArrayList<AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
        AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(
                ContactSubType.Person);
        loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);
        WatercraftScheduledItems boat = new WatercraftScheduledItems(WatercraftTypes.BoatAndMotor, DateUtils.convertStringtoDate("01/01/1999", "MM/dd/yyyy"), 10000);
        boat.setLimit(3123);
        boat.setItem(WatercratItems.Boat);
        boat.setLength(28);
        boat.setBoatType(BoatType.Outboard);

        WatercraftScheduledItems boat1 = new WatercraftScheduledItems(WatercraftTypes.BoatAndMotor, DateUtils.convertStringtoDate("01/01/1999", "MM/dd/yyyy"), 10000);
        boat1.setLimit(3123);
        boat1.setItem(WatercratItems.Trailer);
        boat1.setLength(28);
        boat1.setBoatType(BoatType.Outboard);

        WatercraftScheduledItems boat0 = new WatercraftScheduledItems(WatercraftTypes.BoatAndMotor, DateUtils.convertStringtoDate("01/01/1999", "MM/dd/yyyy"), 10000);
        boat0.setLimit(3123);
        boat0.setItem(WatercratItems.Motor);
        boat0.setLength(28);
        boat0.setBoatType(BoatType.Outboard);

        ArrayList<WatercraftScheduledItems> boats = new ArrayList<WatercraftScheduledItems>();
        boats.add(boat);
        boats.add(boat1);
        boats.add(boat0);
        ArrayList<String> boatAddInsured = new ArrayList<String>();
        boatAddInsured.add("Cor Hofman");

        SquireIMWatercraft boatType = new SquireIMWatercraft(WatercraftTypes.BoatAndMotor, PAComprehensive_CollisionDeductible.OneHundred100, boats);
        boatType.setAdditionalInterest(loc2Bldg1AdditionalInterests);

        //Personal watercraft
        WatercraftScheduledItems boat2 = new WatercraftScheduledItems(WatercraftTypes.PersonalWatercraft, DateUtils.convertStringtoDate("01/01/1999", "MM/dd/yyyy"), 10000);
        boat2.setLimit(3123);
        boat2.setItem(WatercratItems.PersonalWatercraft);
        boat2.setLength(28);
        boat2.setBoatType(BoatType.Inboard);
        ArrayList<WatercraftScheduledItems> boatsList = new ArrayList<WatercraftScheduledItems>();
        boatsList.add(boat2);
        SquireIMWatercraft boatType2 = new SquireIMWatercraft(WatercraftTypes.PersonalWatercraft, PAComprehensive_CollisionDeductible.OneHundred100, boatsList);
        boatType2.setAdditionalInterest(loc2Bldg1AdditionalInterests);

        //SailBoat
        WatercraftScheduledItems boat3 = new WatercraftScheduledItems(WatercraftTypes.Sailboat, DateUtils.convertStringtoDate("01/01/1999", "MM/dd/yyyy"), 10000);
        boat3.setLimit(3123);
        boat3.setItem(WatercratItems.Boat);
        boat3.setLength(28);
        boat3.setBoatType(BoatType.Inboard);
        ArrayList<WatercraftScheduledItems> sailList = new ArrayList<WatercraftScheduledItems>();
        sailList.add(boat3);
        SquireIMWatercraft boatType3 = new SquireIMWatercraft(WatercraftTypes.Sailboat, PAComprehensive_CollisionDeductible.OneHundred100, sailList);
        boatType3.setAdditionalInterest(loc2Bldg1AdditionalInterests);

        //Yacht
        WatercraftScheduledItems boat4 = new WatercraftScheduledItems(WatercraftTypes.Yacht, DateUtils.convertStringtoDate("01/01/1999", "MM/dd/yyyy"), 10000);
        boat4.setLimit(3123);
        boat4.setItem(WatercratItems.Boat);
        boat4.setLength(28);
        boat4.setBoatType(BoatType.Inboard);
        ArrayList<WatercraftScheduledItems> yatchList = new ArrayList<WatercraftScheduledItems>();
        yatchList.add(boat4);
        SquireIMWatercraft boatType4 = new SquireIMWatercraft(WatercraftTypes.Yacht, PAComprehensive_CollisionDeductible.OneHundred100, yatchList);
        boatType4.setAdditionalInterest(loc2Bldg1AdditionalInterests);
        ArrayList<SquireIMWatercraft> boatTypes = new ArrayList<SquireIMWatercraft>();
        boatTypes.add(boatType);
        boatTypes.add(boatType2);
        boatTypes.add(boatType3);
        boatTypes.add(boatType4);

        //Section II coverages
        SquireLiability liability = new SquireLiability();


        SquireLiablityCoverageLivestockItem livestockSectionIICowCoverage = new SquireLiablityCoverageLivestockItem();
        livestockSectionIICowCoverage.setQuantity(100);
        livestockSectionIICowCoverage.setType(LivestockScheduledItemType.Cow);

        SquireLiablityCoverageLivestockItem livestockSectionIIHorseCoverage = new SquireLiablityCoverageLivestockItem();
        livestockSectionIIHorseCoverage.setQuantity(14);
        livestockSectionIIHorseCoverage.setType(LivestockScheduledItemType.Horse);

        ArrayList<SquireLiablityCoverageLivestockItem> coveredLivestockItems = new ArrayList<SquireLiablityCoverageLivestockItem>();
        coveredLivestockItems.add(livestockSectionIICowCoverage);
        coveredLivestockItems.add(livestockSectionIIHorseCoverage);

        SectionIICoverages livestockCoverage = new SectionIICoverages(SectionIICoveragesEnum.Livestock, coveredLivestockItems);
        liability.getSectionIICoverageList().add(livestockCoverage);

        ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
        imTypes.add(InlandMarine.Cargo);
        imTypes.add(InlandMarine.Watercraft);

        ArrayList<Contact> driversList = new ArrayList<Contact>();
        Contact person = new Contact();
        driversList.add(person);
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle vehicle = new Vehicle(VehicleTypePL.PrivatePassenger, "make new vin", 2003, "Oldie", "But Goodie");
        vehicleList.add(vehicle);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow, MedicalLimit.TenK);
        coverages.setUnderinsured(false);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto(driversList, vehicleList, coverages);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liability;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;
        myInlandMarine.cargo_PL_IM = cargoList;
        myInlandMarine.watercrafts_PL_IM = boatTypes;

        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;

        this.myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.InlandMarineLinePL, LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("IM", "Items")
                .build(GeneratePolicyType.FullApp);


        new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchSubmission(this.myPolicyObjPL.agentInfo.agentUserName, this.myPolicyObjPL.agentInfo.agentPassword, this.myPolicyObjPL.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuIMWatercraft();
        GenericWorkorderSquireInlandMarine_Watercraft watercraftPage = new GenericWorkorderSquireInlandMarine_Watercraft(driver);
        String errorMessage = "";
        for (SquireIMWatercraft watercraft : this.myPolicyObjPL.squire.inlandMarine.watercrafts_PL_IM) {
            if (watercraftPage.getWatercraftItemNumberByType(watercraft.getWaterToyType()) == 0) {
                errorMessage = "WaterCraft # is not displayed correctly \n";
            }
        }

        sideMenu.clickSideMenuIMCargo();
        GenericWorkorderSquireInlandMarine_Cargo cargoPage = new GenericWorkorderSquireInlandMarine_Cargo(driver);
        for (SquireIMCargo goods : this.myPolicyObjPL.squire.inlandMarine.cargo_PL_IM) {
            if (cargoPage.getCargoItemNumberByType(goods.getCargoClass()) == 0) {
                errorMessage = "Cargo # is not displayed correctly \n";
            }
        }

        if (errorMessage != "")
            Assert.fail(errorMessage);

    }

}

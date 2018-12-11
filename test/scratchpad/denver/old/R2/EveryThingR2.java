package scratchpad.denver.old.R2;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.*;
import repository.gw.enums.LivestockType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.Vehicle.PAComprehensive_CollisionDeductible;
import repository.gw.enums.Vehicle.VehicleType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.VINHelper;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class EveryThingR2 extends BaseTest {

    private GeneratePolicy myPolicyObjPL = null;
    private GeneratePolicy umbrellaPolicy = null;
    private GeneratePolicy inlandMarinePolicyObjPL = null;
    private GeneratePolicy stdLiabFullApp = null;
    private GeneratePolicy stdFireLiab_Squire_PolicyObj = null;
    private GeneratePolicy stdFireLiabPolicyObj = null;
    private WebDriver driver;

    @Test()
    public void testFormInference_GenerateFullApp() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Vin vin = VINHelper.getRandomVIN();

        // Cargo
        repository.gw.generate.custom.Cargo cargoTrailer1 = new repository.gw.generate.custom.Cargo(VehicleType.Trailer, vin.getVin(), Integer.parseInt(vin.getYear()), vin.getMake(), vin.getModel());
        cargoTrailer1.setLimit("1000");
        ArrayList<repository.gw.generate.custom.Cargo> cargoTrailers = new ArrayList<repository.gw.generate.custom.Cargo>();
        cargoTrailers.add(cargoTrailer1);
        ArrayList<String> cargoAddInsured = new ArrayList<String>();
        cargoAddInsured.add("Cor Hofman");
        repository.gw.generate.custom.SquireIMCargo stuff = new repository.gw.generate.custom.SquireIMCargo(CargoClass.ClassIII, ClassIIICargoTypes.Milk, 100, 1000, "Brenda Swindle", false, cargoTrailers, cargoAddInsured);
        repository.gw.generate.custom.SquireIMCargo cargo = new repository.gw.generate.custom.SquireIMCargo(CargoClass.ClassI, 100, 1000, "Brenda Swindle", false, cargoTrailers);
        ArrayList<repository.gw.generate.custom.SquireIMCargo> cargoList = new ArrayList<repository.gw.generate.custom.SquireIMCargo>();
        cargoList.add(cargo);
        cargoList.add(stuff);

        // Farm Equipment
        repository.gw.generate.custom.IMFarmEquipmentScheduledItem farmThing = new repository.gw.generate.custom.IMFarmEquipmentScheduledItem("Farm Equipment", "Manly Farm Equipment", 1000);
        ArrayList<repository.gw.generate.custom.IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<repository.gw.generate.custom.IMFarmEquipmentScheduledItem>();
        farmEquip.add(farmThing);
        repository.gw.generate.custom.FarmEquipment imFarmEquip = new repository.gw.generate.custom.FarmEquipment(IMFarmEquipmentType.CircleSprinkler, repository.gw.enums.CoverageType.BroadForm, IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
        ArrayList<repository.gw.generate.custom.FarmEquipment> allFarmEquip = new ArrayList<repository.gw.generate.custom.FarmEquipment>();
        allFarmEquip.add(imFarmEquip);

        // Rec Equip
        ArrayList<repository.gw.generate.custom.RecreationalEquipment> recVehicle = new ArrayList<repository.gw.generate.custom.RecreationalEquipment>();
        recVehicle.add(new repository.gw.generate.custom.RecreationalEquipment(RecreationalEquipmentType.AllTerrainVehicle, "500", PAComprehensive_CollisionDeductible.Fifty50, "Brett Hiltbrand"));

        // PersonalProperty
        repository.gw.generate.custom.PersonalProperty pprop = new repository.gw.generate.custom.PersonalProperty();
        pprop.setType(repository.gw.enums.PersonalPropertyType.MedicalSuppliesAndEquipment);
        pprop.setYear(2010);
        pprop.setMake("Testmake");
        pprop.setModel("Testmodel");
        pprop.setVinSerialNum("123123123");
        pprop.setDescription("Testdescription");
        pprop.setLimit(1000);
        pprop.setDeductible(repository.gw.enums.PersonalPropertyDeductible.Ded25);
        ArrayList<String> ppropAdditIns = new ArrayList<String>();
        ppropAdditIns.add("First Guy");
        ppropAdditIns.add("Second Guy");
        pprop.setAdditionalInsureds(ppropAdditIns);
        repository.gw.generate.custom.PersonalProperty pprop2 = new repository.gw.generate.custom.PersonalProperty();
        pprop2.setType(repository.gw.enums.PersonalPropertyType.MedicalSuppliesAndEquipment);
        pprop2.setYear(2011);
        pprop2.setMake("Testmake2");
        pprop2.setModel("Testmodel2");
        pprop2.setVinSerialNum("456456456");
        pprop2.setDescription("Testdescription2");
        pprop2.setLimit(1000);
        pprop2.setDeductible(repository.gw.enums.PersonalPropertyDeductible.Ded25);
        ArrayList<String> pprop2AdditIns = new ArrayList<String>();
        pprop2AdditIns.add("First Guy2");
        pprop2AdditIns.add("Second Guy2");
        pprop2.setAdditionalInsureds(pprop2AdditIns);
        repository.gw.generate.custom.PersonalProperty pprop3 = new repository.gw.generate.custom.PersonalProperty();
        pprop3.setType(repository.gw.enums.PersonalPropertyType.MilkContaminationAndRefrigeration);
        pprop3.setDescription("Testdescription2");
        pprop3.setLimit(1000);
        repository.gw.generate.custom.PersonalProperty pprop4 = new repository.gw.generate.custom.PersonalProperty();
        pprop4.setType(repository.gw.enums.PersonalPropertyType.RefrigeratedMilk);
        pprop4.setDescription("Testdescription2");
        pprop4.setLimit(2000);
        repository.gw.generate.custom.PersonalProperty pprop5 = new repository.gw.generate.custom.PersonalProperty();
        pprop5.setType(repository.gw.enums.PersonalPropertyType.BeeContainers);
        pprop5.setDeductible(repository.gw.enums.PersonalPropertyDeductible.Ded250);
        pprop5.setLimit(25);
        repository.gw.generate.custom.PersonalPropertyScheduledItem beeScheduledItem = new repository.gw.generate.custom.PersonalPropertyScheduledItem();
        beeScheduledItem.setParentPersonalPropertyType(repository.gw.enums.PersonalPropertyType.BeeContainers);
        beeScheduledItem.setLimit(1000);
        beeScheduledItem.setNumber(500);
        beeScheduledItem.setDescription("Bee Yourself");
        ArrayList<repository.gw.generate.custom.PersonalPropertyScheduledItem> beeScheduledItems = new ArrayList<repository.gw.generate.custom.PersonalPropertyScheduledItem>();
        beeScheduledItems.add(beeScheduledItem);
        pprop5.setScheduledItems(beeScheduledItems);
        repository.gw.generate.custom.PersonalProperty pprop6 = new repository.gw.generate.custom.PersonalProperty();
        pprop6.setType(repository.gw.enums.PersonalPropertyType.SportingEquipment);
        pprop6.setLimit(1000);
        pprop6.setDeductible(repository.gw.enums.PersonalPropertyDeductible.Ded1000);
        repository.gw.generate.custom.PersonalPropertyScheduledItem sportsScheduledItem = new repository.gw.generate.custom.PersonalPropertyScheduledItem();
        sportsScheduledItem.setParentPersonalPropertyType(repository.gw.enums.PersonalPropertyType.SportingEquipment);
        sportsScheduledItem.setLimit(1000);
        sportsScheduledItem.setDescription("Sports Stuff");
        sportsScheduledItem.setType(repository.gw.enums.PersonalPropertyScheduledItemType.Guns);
        sportsScheduledItem.setMake("Honda");
        sportsScheduledItem.setModel("Accord");
        sportsScheduledItem.setYear(2015);
        sportsScheduledItem.setVinSerialNum("abcd12345");
        ArrayList<repository.gw.generate.custom.PersonalPropertyScheduledItem> sportsScheduledItems = new ArrayList<repository.gw.generate.custom.PersonalPropertyScheduledItem>();
        sportsScheduledItems.add(sportsScheduledItem);
        pprop6.setScheduledItems(sportsScheduledItems);
        repository.gw.generate.custom.PersonalProperty pprop7 = new repository.gw.generate.custom.PersonalProperty();
        pprop7.setType(repository.gw.enums.PersonalPropertyType.GolfEquipment);
        pprop7.setLimit(1000);
        pprop7.setDeductible(repository.gw.enums.PersonalPropertyDeductible.Ded1000);
        repository.gw.generate.custom.PersonalPropertyScheduledItem golfScheduledItem = new repository.gw.generate.custom.PersonalPropertyScheduledItem();
        golfScheduledItem.setParentPersonalPropertyType(repository.gw.enums.PersonalPropertyType.GolfEquipment);
        golfScheduledItem.setLimit(1000);
        golfScheduledItem.setDescription("Golf Bag and Stuff");
        ArrayList<repository.gw.generate.custom.PersonalPropertyScheduledItem> golfScheduledItems = new ArrayList<repository.gw.generate.custom.PersonalPropertyScheduledItem>();
        golfScheduledItems.add(golfScheduledItem);
        pprop7.setScheduledItems(golfScheduledItems);
        repository.gw.generate.custom.PersonalProperty pprop8 = new repository.gw.generate.custom.PersonalProperty();
        pprop8.setType(repository.gw.enums.PersonalPropertyType.BlanketRadios);
        pprop8.setDeductible(repository.gw.enums.PersonalPropertyDeductible.Ded100);
        pprop8.setLimit(2000);
        repository.gw.generate.custom.PersonalPropertyScheduledItem blanketRadioScheduledItem = new repository.gw.generate.custom.PersonalPropertyScheduledItem();
        blanketRadioScheduledItem.setParentPersonalPropertyType(repository.gw.enums.PersonalPropertyType.BlanketRadios);
        blanketRadioScheduledItem.setLimit(1000);
        blanketRadioScheduledItem.setType(repository.gw.enums.PersonalPropertyScheduledItemType.Portable);
        blanketRadioScheduledItem.setDescription("Radio on Blanket");
        blanketRadioScheduledItem.setNumber(4);
        ArrayList<repository.gw.generate.custom.PersonalPropertyScheduledItem> blanketRadioScheduledItems = new ArrayList<repository.gw.generate.custom.PersonalPropertyScheduledItem>();
        blanketRadioScheduledItems.add(blanketRadioScheduledItem);
        pprop8.setScheduledItems(blanketRadioScheduledItems);
        repository.gw.generate.custom.PersonalPropertyList ppropList = new repository.gw.generate.custom.PersonalPropertyList();
        ArrayList<repository.gw.generate.custom.PersonalProperty> msaeList = new ArrayList<repository.gw.generate.custom.PersonalProperty>();
        msaeList.add(pprop);
        msaeList.add(pprop2);
        ppropList.setMedicalSuppliesAndEquipment(msaeList);
        ppropList.setRefrigeratedMilk(pprop4);
        ppropList.setMilkContaminationAndRefrigeration(pprop3);
        ppropList.setBeeContainers(pprop5);
        ppropList.setSportingEquipment(pprop6);
        ppropList.setGolfEquipment(pprop7);
        ppropList.setBlanketRadios(pprop8);

        // Livestock
        repository.gw.generate.custom.Livestock livestock = new repository.gw.generate.custom.Livestock();
        livestock.setType(repository.gw.enums.LivestockType.Livestock);
        livestock.setDeductible(repository.gw.enums.LivestockDeductible.Ded100);
        repository.gw.generate.custom.LivestockScheduledItem livSchedItem1 = new repository.gw.generate.custom.LivestockScheduledItem(livestock.getType(), repository.gw.enums.LivestockScheduledItemType.Alpaca, "Test Alpaca 1", 1000);
        repository.gw.generate.custom.LivestockScheduledItem livSchedItem2 = new repository.gw.generate.custom.LivestockScheduledItem(livestock.getType(), repository.gw.enums.LivestockScheduledItemType.Cow, "Test BullCow 1", "TAG123", "Brand1", "Breed1", 5000);
        ArrayList<String> livSchedItem2AdditInsureds = new ArrayList<String>();
        livSchedItem2AdditInsureds.add("Sched Item Guy1");
        livSchedItem2AdditInsureds.add("Sched Item Guy2");
        livSchedItem2.setAdditionalInsureds(livSchedItem2AdditInsureds);
        ArrayList<repository.gw.generate.custom.LivestockScheduledItem> livSchedItems = new ArrayList<repository.gw.generate.custom.LivestockScheduledItem>();
        livSchedItems.add(livSchedItem1);
        livSchedItems.add(livSchedItem2);
        livestock.setScheduledItems(livSchedItems);
        ArrayList<String> livAdditInsureds = new ArrayList<String>();
        livAdditInsureds.add("Cor Hofman");
        livAdditInsureds.add("Rusty Young");
        livestock.setAdditionalInsureds(livAdditInsureds);
        repository.gw.generate.custom.Livestock deathOfLivestock = new repository.gw.generate.custom.Livestock();
        deathOfLivestock.setType(LivestockType.DeathOfLivestock);
        repository.gw.generate.custom.LivestockScheduledItem dolSchedItem1 = new repository.gw.generate.custom.LivestockScheduledItem(deathOfLivestock.getType(), repository.gw.enums.LivestockScheduledItemType.SheepD, 500, "Test Sheep Hogs");
        repository.gw.generate.custom.LivestockScheduledItem dolSchedItem2 = new repository.gw.generate.custom.LivestockScheduledItem(deathOfLivestock.getType(), repository.gw.enums.LivestockScheduledItemType.Other, 20, "Test Other");
        ArrayList<repository.gw.generate.custom.LivestockScheduledItem> dolSchedItems = new ArrayList<repository.gw.generate.custom.LivestockScheduledItem>();
        dolSchedItems.add(dolSchedItem1);
        dolSchedItems.add(dolSchedItem2);
        deathOfLivestock.setScheduledItems(dolSchedItems);
        ArrayList<String> dolAdditInsureds = new ArrayList<String>();
        dolAdditInsureds.add("Cor Hofman");
        dolAdditInsureds.add("Rusty Young");
        deathOfLivestock.setAdditionalInsureds(dolAdditInsureds);
        repository.gw.generate.custom.LivestockList allLivestock = new repository.gw.generate.custom.LivestockList(livestock, deathOfLivestock, null);

        // Watercraft
        ArrayList<repository.gw.generate.custom.AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<repository.gw.generate.custom.AdditionalInterest>();
        repository.gw.generate.custom.AdditionalInterest loc2Bldg1AddInterest = new repository.gw.generate.custom.AdditionalInterest(
                repository.gw.enums.ContactSubType.Person);
        loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);
        repository.gw.generate.custom.WatercraftScheduledItems boat = new repository.gw.generate.custom.WatercraftScheduledItems(WatercraftTypes.BoatAndMotor, DateUtils.convertStringtoDate("01/01/1999", "MM/dd/yyyy"), 10000);
        boat.setLimit(3123);
        boat.setItem(WatercratItems.Boat);
        boat.setLength(28);
        boat.setBoatType(BoatType.Outboard);
        ArrayList<repository.gw.generate.custom.WatercraftScheduledItems> boats = new ArrayList<repository.gw.generate.custom.WatercraftScheduledItems>();
        boats.add(boat);
        ArrayList<String> boatAddInsured = new ArrayList<String>();
        boatAddInsured.add("Cor Hofman");
        repository.gw.generate.custom.SquireIMWatercraft boatType = new repository.gw.generate.custom.SquireIMWatercraft(WatercraftTypes.BoatAndMotor, PAComprehensive_CollisionDeductible.OneHundred100, boats);
        boatType.setAdditionalInterest(loc2Bldg1AdditionalInterests);
        ArrayList<repository.gw.generate.custom.SquireIMWatercraft> boatTypes = new ArrayList<repository.gw.generate.custom.SquireIMWatercraft>();
        boatTypes.add(boatType);

        repository.gw.generate.custom.AdditionalInterest propertyAdditionalInterest = new repository.gw.generate.custom.AdditionalInterest(repository.gw.enums.ContactSubType.Company);
        propertyAdditionalInterest.setAdditionalInterestBilling(repository.gw.enums.AdditionalInterestBilling.Bill_Lienholder);
        propertyAdditionalInterest.setNewContact(repository.gw.enums.CreateNew.Create_New_Only_If_Does_Not_Exist);
        propertyAdditionalInterest.setAdditionalInterestType(repository.gw.enums.AdditionalInterestType.LienholderPL);

        ArrayList<repository.gw.generate.custom.PolicyLocation> locationsList = new ArrayList<repository.gw.generate.custom.PolicyLocation>();
        ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty> locOnePropertyList = new ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty>();
        repository.gw.generate.custom.PLPolicyLocationProperty locationOneProperty = new repository.gw.generate.custom.PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        locationOneProperty.setBuildingAdditionalInterest(propertyAdditionalInterest);
        locOnePropertyList.add(locationOneProperty);
        locationsList.add(new repository.gw.generate.custom.PolicyLocation(locOnePropertyList));

        repository.gw.generate.custom.SquireLiability liabilitySection = new repository.gw.generate.custom.SquireLiability();

        ArrayList<repository.gw.generate.custom.AdditionalInterest> vehicleInterests = new ArrayList<repository.gw.generate.custom.AdditionalInterest>();
        vehicleInterests.add(new repository.gw.generate.custom.AdditionalInterest());

        repository.gw.generate.custom.SquireLiability liability = new repository.gw.generate.custom.SquireLiability();

        ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
        imTypes.add(InlandMarine.Cargo);
        imTypes.add(InlandMarine.FarmEquipment);
        imTypes.add(InlandMarine.RecreationalEquipment);
        imTypes.add(InlandMarine.PersonalProperty);
        imTypes.add(InlandMarine.Livestock);
        imTypes.add(InlandMarine.Watercraft);

        // driver
        ArrayList<repository.gw.generate.custom.Contact> driversList = new ArrayList<repository.gw.generate.custom.Contact>();
        repository.gw.generate.custom.Contact person = new repository.gw.generate.custom.Contact();
        person.setFirstName("jon");
        driversList.add(person);

        // Vehicle
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle toAdd = new Vehicle();
        toAdd.setComprehensive(true);
        toAdd.setCostNew(10000.00);
        toAdd.setCollision(true);
        toAdd.setAdditionalLivingExpense(true);
        vehicleList.add(toAdd);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow, MedicalLimit.TenK);
        coverages.setUnderinsured(false);

        repository.gw.generate.custom.SquirePersonalAuto squirePersonalAuto = new repository.gw.generate.custom.SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);
        squirePersonalAuto.setVehicleList(vehicleList);
        squirePersonalAuto.setDriversList(driversList);

        repository.gw.generate.custom.SquirePropertyAndLiability myPropertyAndLiability = new repository.gw.generate.custom.SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liability;

        repository.gw.generate.custom.SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;
        myInlandMarine.cargo_PL_IM = cargoList;
        myInlandMarine.recEquipment_PL_IM = recVehicle;
        myInlandMarine.personalProperty_PL_IM = ppropList.getAllPersonalPropertyAsList();
        myInlandMarine.livestock_PL_IM = allLivestock.getAllLivestockAsList();
        myInlandMarine.farmEquipment = allFarmEquip;
        myInlandMarine.watercrafts_PL_IM = boatTypes;

        repository.gw.generate.custom.Squire mySquire = new repository.gw.generate.custom.Squire(repository.gw.enums.SquireEligibility.FarmAndRanch);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;

        this.myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, repository.gw.enums.LineSelection.InlandMarineLinePL, repository.gw.enums.LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("IM", "Forms")
                .build(repository.gw.enums.GeneratePolicyType.FullApp);


        System.out.println(myPolicyObjPL.squire.getPolicyNumber());
    }

    /**

     @Test public void umbrella() throws Exception{

     Configuration.setProduct(ApplicationOrCenter.PolicyCenter);

     SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
     squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_1000000);

     this.umbrellaPolicy = new GeneratePolicy.Builder(driver)
     .withProductType(ProductLineType.PersonalUmbrella)
     .withPolOrgType(OrganizationType.Individual)
     .withSquirePolicyUsedForUmbrella(myPolicyObjPL)
     .withSquireUmbrellaInfo(squireUmbrellaInfo)
     .build(GeneratePolicyType.QuickQuote);

     }

     @Test public void stdFire(){

     }

     @Test public void stdFireCommodity() throws GuidewirePolicyCenterException, Exception{
     Configuration.setProduct(ApplicationOrCenter.PolicyCenter);

     ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();

     ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
     locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));

     PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
     locToAdd.setPlNumAcres(11);

     locationsList.add(locToAdd);

     ArrayList<PLPolicyLocationProperty> locTwoPropertyList = new ArrayList<PLPolicyLocationProperty>();
     locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremisesCovE));

     PolicyLocation locToAdd2 = new PolicyLocation(locTwoPropertyList);
     locToAdd2.setPlNumAcres(11);

     locationsList.add(locToAdd2);

     SquireLiability myLiab = new SquireLiability();
     myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

     ArrayList<LineSelection> lines = new ArrayList<LineSelection>();
     lines.add(LineSelection.PropertyAndLiabilityLinePL);

     this.stdFireLiab_Squire_PolicyObj = new GeneratePolicy.Builder(driver)
     .withProductType(ProductLineType.Squire)
     .withSquireEligibility(SquireEligibility.City)
     .withLineSelection(lines)
     .withPolicyLocations(locationsList)
     .withInsFirstLastName("SQ", "MultiLocs")
     .withRandomPaymentPlanType()
     .withSquireLiability(myLiab)
     .build(GeneratePolicyType.FullApp);

     ArrayList<PolicyLocation> locationsList1 = new ArrayList<PolicyLocation>();
     ArrayList<PLPolicyLocationProperty> locOnePropertyList1 = new ArrayList<PLPolicyLocationProperty>();
     locOnePropertyList1.add(new PLPolicyLocationProperty(PropertyTypePL.Potatoes));
     PolicyLocation locToAdd1 = new PolicyLocation(locOnePropertyList1);

     locToAdd1.setPlNumAcres(11);
     locationsList1.add(locToAdd1);

     PLPropertyCoverages propertyCoverages = new PLPropertyCoverages();
     propertyCoverages.getCoverageA().setIncreasedReplacementCost(false);

     ArrayList<LineSelection> productLines1 = new ArrayList<LineSelection>();
     productLines1.add(LineSelection.StandardFirePL);

     Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
     this.stdFireLiabPolicyObj = new GeneratePolicy.Builder(driver)
     .withAgent(this.stdFireLiab_Squire_PolicyObj.agentInfo)
     .withProductType(ProductLineType.StandardFL)
     .withLineSelection(productLines1)
     .withSquirePolicyUsedForStandardFire(this.stdFireLiab_Squire_PolicyObj,true, true)
     .withPolicyLocations(locationsList1)
     .build(GeneratePolicyType.QuickQuote);
     }

     @Test public void stdLiability() throws Exception {
     Agents agent = AgentsHelper.getRandomAgent();

     Configuration.setProduct(ApplicationOrCenter.PolicyCenter);

     // GENERATE POLICY
     ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
     locationsList.add(new PolicyLocation());

     ArrayList<LineSelection> productLines = new ArrayList<LineSelection>();
     productLines.add(LineSelection.StandardLiabilityPL);

     SquireLiability myLiab = new SquireLiability();
     myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

     this.stdLiabFullApp = new GeneratePolicy.Builder(driver)
     .withAgent(agent)
     .withProductType(ProductLineType.StandardFL)
     .withLineSelection(productLines)
     .withSquireLiability(myLiab)
     .withCreateNew(CreateNew.Create_New_Always)
     .withInsPersonOrCompany(ContactSubType.Person)
     .withInsFirstLastName("Guy", "StdLiab")
     .withInsAge(26)
     .withPolOrgType(OrganizationType.Individual)
     .withPolicyLocations(locationsList)
     .build(GeneratePolicyType.FullApp);

     }

     @Test public void testCreateInlandMarinePolicy() throws Exception {
     Configuration.setProduct(ApplicationOrCenter.PolicyCenter);

     ArrayList<InlandMarine> inlandMarineCoverageSelection_PL_IM = new ArrayList<InlandMarine>();
     inlandMarineCoverageSelection_PL_IM.add(InlandMarine.FarmEquipment);
     inlandMarineCoverageSelection_PL_IM.add(InlandMarine.PersonalProperty);

     // FPP
     //Scheduled Item for 1st FPP
     IMFarmEquipmentScheduledItem scheduledItem1 = new IMFarmEquipmentScheduledItem("Circle Sprinkler", "Manly Farm Equipment", 1000);
     ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
     farmEquip.add(scheduledItem1);
     FarmEquipment imFarmEquip1 = new FarmEquipment(IMFarmEquipmentType.CircleSprinkler, CoverageType.BroadForm, IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);

     ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
     allFarmEquip.add(imFarmEquip1);

     // PersonalProperty
     PersonalProperty ppropTool = new PersonalProperty();
     ppropTool.setType(PersonalPropertyType.Tools);
     ppropTool.setDeductible(PersonalPropertyDeductible.Ded100);
     PersonalPropertyScheduledItem tool = new PersonalPropertyScheduledItem();
     tool.setDescription("Big Tool");
     tool.setLimit(50000);

     ArrayList<PersonalPropertyScheduledItem> tools = new ArrayList<PersonalPropertyScheduledItem>();
     tools.add(tool);

     ppropTool.setScheduledItems(tools);
     ArrayList<String> ppropAdditIns = new ArrayList<String>();
     ppropAdditIns.add("First Guy");
     ppropTool.setAdditionalInsureds(ppropAdditIns);
     PersonalPropertyList ppropList = new PersonalPropertyList();
     ppropList.setTools(ppropTool);

     this.inlandMarinePolicyObjPL = new GeneratePolicy.Builder(driver)
     .withProductType(ProductLineType.StandardIM)
     .withPersonalProperty(ppropList)
     .withInlandMarine(inlandMarineCoverageSelection_PL_IM)
     .withFarmEquipment(allFarmEquip)
     .withInsFirstLastName("farm", "equipment")
     .build(GeneratePolicyType.QuickQuote);
     }
     **/
}

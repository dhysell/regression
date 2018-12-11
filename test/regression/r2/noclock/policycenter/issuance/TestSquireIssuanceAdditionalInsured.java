package regression.r2.noclock.policycenter.issuance;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.BoatType;
import repository.gw.enums.InlandMarineTypes.CargoClass;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.InlandMarineTypes.RecreationalEquipmentType;
import repository.gw.enums.InlandMarineTypes.WatercraftTypes;
import repository.gw.enums.InlandMarineTypes.WatercratItems;
import repository.gw.enums.IssuanceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.LivestockDeductible;
import repository.gw.enums.LivestockScheduledItemType;
import repository.gw.enums.LivestockType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PersonalPropertyDeductible;
import repository.gw.enums.PersonalPropertyType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.TransactionType;
import repository.gw.enums.Vehicle.PAComprehensive_CollisionDeductible;
import repository.gw.enums.Vehicle.VehicleType;
import repository.gw.enums.Vehicle.VehicleTypePL;
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
import repository.gw.generate.custom.PLPropertyCoverages;
import repository.gw.generate.custom.PersonalProperty;
import repository.gw.generate.custom.PersonalPropertyList;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.RecreationalEquipment;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireFPP;
import repository.gw.generate.custom.SquireFPPTypeItem;
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

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_Cargo;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_FarmEquipment;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_Livestock;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_PersonalProperty;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_RecreationalEquipment;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_Watercraft;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import persistence.globaldatarepo.helpers.VINHelper;

/**
 * @Author nvadlamudi
 * @Requirement :DE4491: Additional Insured need to be EffDated and not retirable
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/68999021356d/detail/defect/80378092276">DE4491</a>
 * @Description
 * @DATE Jan 26, 2017
 */
@QuarantineClass
public class TestSquireIssuanceAdditionalInsured extends BaseTest {
	private WebDriver driver;
    private GeneratePolicy myPolicyObjPL;
    private Underwriters uw;
    private String Cargo_AdditionalInsured = "Cargo Add";
    private String Second_Cargo_AdditionalInsured = "Second Cargo Add";
    private String FarmTruck_AdditionalInsured = "Farm Add";
    private String Second_FarmTruck_AdditionalInsured = "Second Farm Add";
    private String property_AdditionalInsured = "Property Add";
    private String secondProperty_AdditionalInsured = "Second Property Add";
    private String LiveStock_AdditonalInsured = "LiveStock Add";
    private String Second_Livestock_AdditionalInsured = "Second Livestock Add";
    private String DeathLivestock_AdditionalInsured = "DeathLive Add";
    private String Watercraft_AdditionalInsured = "Watercraft Add";
    private String Second_Watercraft_AdditionalInsured = "Second Watercraft Add";
    private String Vehicle_AdditionalInsured = "Vehicle Add";
    private String Second_Vehicle_AdditionalInsured = "Second Vehicle Add";
    private String RecEqipment_AdditionalInsured = "RecEquip Add";
    private String Second_RecEquipment_AdditionalInsured = "Second RecEquip Add";

    @Test()
    public void testSquirePolBound() throws Exception {
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
        cargoAddInsured.add(Cargo_AdditionalInsured);
        SquireIMCargo cargo = new SquireIMCargo(CargoClass.ClassI, 100, 1000, "Brenda Swindle", false, cargoTrailers);
        ArrayList<SquireIMCargo> cargoList = new ArrayList<SquireIMCargo>();
        cargoList.add(cargo);


        // Farm Equipment
        IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment", "Manly Farm Equipment", 1000);
        ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
        farmEquip.add(farmThing);
        FarmEquipment imFarmEquip1 = new FarmEquipment(IMFarmEquipmentType.CircleSprinkler, CoverageType.BroadForm, IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
        imFarmEquip1.addAdditionalInsured(FarmTruck_AdditionalInsured);
        ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
        allFarmEquip.add(imFarmEquip1);

        // Rec Equip
        ArrayList<RecreationalEquipment> recVehicle = new ArrayList<RecreationalEquipment>();
        RecreationalEquipment recEquip = new RecreationalEquipment(RecreationalEquipmentType.AllTerrainVehicle, "500", PAComprehensive_CollisionDeductible.Fifty50, "Brett Hiltbrand");
        recVehicle.add(recEquip);

        // PersonalProperty
        PersonalProperty pprop = new PersonalProperty();
        pprop.setType(PersonalPropertyType.MedicalSuppliesAndEquipment);
        pprop.setYear(2010);
        pprop.setMake("Testmake");
        pprop.setModel("Testmodel");
        pprop.setVinSerialNum("123123123");
        pprop.setDescription("Testdescription");
        pprop.setLimit(1234);
        pprop.setDeductible(PersonalPropertyDeductible.Ded25);
        ArrayList<String> ppropAdditIns = new ArrayList<String>();
        ppropAdditIns.add(property_AdditionalInsured);
        pprop.setAdditionalInsureds(ppropAdditIns);


        PersonalPropertyList ppropList = new PersonalPropertyList();
        ArrayList<PersonalProperty> msaeList = new ArrayList<PersonalProperty>();
        msaeList.add(pprop);
        ppropList.setMedicalSuppliesAndEquipment(msaeList);

        // Livestock
        Livestock livestock = new Livestock();
        livestock.setType(LivestockType.Livestock);
        livestock.setDeductible(LivestockDeductible.Ded100);
        LivestockScheduledItem livSchedItem2 = new LivestockScheduledItem(livestock.getType(), LivestockScheduledItemType.Cow, "Test Cow 1", "TAG123", "Brand1", "Breed1", 2000);

        ArrayList<String> livSchedItem2AdditInsureds = new ArrayList<String>();
        livSchedItem2AdditInsureds.add("Sched Item Guy1");
        livSchedItem2AdditInsureds.add("Sched Item Guy2");
        livSchedItem2.setAdditionalInsureds(livSchedItem2AdditInsureds);
        ArrayList<LivestockScheduledItem> livSchedItems = new ArrayList<LivestockScheduledItem>();
        livSchedItems.add(livSchedItem2);

        livestock.setScheduledItems(livSchedItems);
        ArrayList<String> livAdditInsureds = new ArrayList<String>();
        livAdditInsureds.add(LiveStock_AdditonalInsured);
        livestock.setAdditionalInsureds(livAdditInsureds);


        Livestock deathOfLivestock = new Livestock();
        deathOfLivestock.setType(LivestockType.DeathOfLivestock);
        LivestockScheduledItem dolSchedItem1 = new LivestockScheduledItem(deathOfLivestock.getType(), LivestockScheduledItemType.HogSwineD, 500, "Test Hog Swine");
        ArrayList<LivestockScheduledItem> dolSchedItems = new ArrayList<LivestockScheduledItem>();
        dolSchedItems.add(dolSchedItem1);
        deathOfLivestock.setScheduledItems(dolSchedItems);
        ArrayList<String> dolAdditInsureds = new ArrayList<String>();
        dolAdditInsureds.add(DeathLivestock_AdditionalInsured);
        deathOfLivestock.setAdditionalInsureds(dolAdditInsureds);
        LivestockList allLivestock = new LivestockList(livestock, deathOfLivestock, null);

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

        ArrayList<WatercraftScheduledItems> boats = new ArrayList<WatercraftScheduledItems>();
        boats.add(boat);
        ArrayList<String> boatAddInsured = new ArrayList<String>();
        boatAddInsured.add(Watercraft_AdditionalInsured);

        SquireIMWatercraft boatType = new SquireIMWatercraft(WatercraftTypes.BoatAndMotor, PAComprehensive_CollisionDeductible.OneHundred100, boats);
        boatType.setAdditionalInterest(loc2Bldg1AdditionalInterests);


        ArrayList<SquireIMWatercraft> boatTypes = new ArrayList<SquireIMWatercraft>();
        boatTypes.add(boatType);

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
        imTypes.add(InlandMarine.FarmEquipment);
        imTypes.add(InlandMarine.RecreationalEquipment);
        imTypes.add(InlandMarine.PersonalProperty);
        imTypes.add(InlandMarine.Livestock);
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

        //FPP
        SquireFPP squireFPP = new SquireFPP(FPPFarmPersonalPropertySubTypes.Cows);
        SquireFPPTypeItem machinery = new SquireFPPTypeItem(squireFPP);
        machinery.setType(FPPFarmPersonalPropertySubTypes.Tractors);
        machinery.setValue(1000);

        ArrayList<SquireFPPTypeItem> listOfFPPItems = new ArrayList<SquireFPPTypeItem>();
        listOfFPPItems.add(machinery);
        squireFPP.setItems(listOfFPPItems);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liability;
        myPropertyAndLiability.squireFPP = squireFPP;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;
        myInlandMarine.cargo_PL_IM = cargoList;
        myInlandMarine.recEquipment_PL_IM = recVehicle;
        myInlandMarine.livestock_PL_IM = allLivestock.getAllLivestockAsList();
        myInlandMarine.watercrafts_PL_IM = boatTypes;
        myInlandMarine.farmEquipment = allFarmEquip;
        myInlandMarine.personalProperty_PL_IM = ppropList.getAllPersonalPropertyAsList();

        Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;

        this.myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL, LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("AddInsured", "Issue")
                .build(GeneratePolicyType.FullApp);
    }

    @Test(dependsOnMethods = {"testSquirePolBound"})
    private void testAddingAdditionalInsured() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.accountNumber);

        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.clickEditPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        coverages.clickFarmPersonalProperty();
        coverages.setFarmPersonalPropertyRisk("A");

        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        propertyDetail.clickViewOrEditBuildingButton(1);
        propertyDetail.addAddtionalInsured();
        propertyDetail.setAddtionalInsuredName(property_AdditionalInsured);
        propertyDetail.clickOk();
        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        vehiclePage.editVehicleByVehicleNumber(1);
        vehiclePage.addAutoAdditionalInsured(Vehicle_AdditionalInsured);
        vehiclePage.clickOK();

        sideMenu.clickSideMenuIMRecreationVehicle();
        GenericWorkorderSquireInlandMarine_RecreationalEquipment recreationalEquipment = new GenericWorkorderSquireInlandMarine_RecreationalEquipment(driver);
        recreationalEquipment.clickLinkInRecreationalEquipmentTable(1);
        recreationalEquipment.addAdditionalInsured(RecEqipment_AdditionalInsured);
        recreationalEquipment.clickOK();

        sideMenu.clickSideMenuIMWatercraft();
        GenericWorkorderSquireInlandMarine_Watercraft watercraft = new GenericWorkorderSquireInlandMarine_Watercraft(driver);
        watercraft.clickEditButton();
        watercraft.addAdditionalInsured(Watercraft_AdditionalInsured);
        watercraft.clickOK();

        sideMenu.clickSideMenuIMPersonalEquipment();
        GenericWorkorderSquireInlandMarine_PersonalProperty personalProperty = new GenericWorkorderSquireInlandMarine_PersonalProperty(driver);
        personalProperty.clickEditButton();

        ArrayList<String> additionalInsureds = new ArrayList<String>();
        additionalInsureds.add(property_AdditionalInsured);
        personalProperty.addAdditionalInsured(additionalInsureds);
        personalProperty.clickOk();

        sideMenu.clickSideMenuIMCargo();
        GenericWorkorderSquireInlandMarine_Cargo cargo = new GenericWorkorderSquireInlandMarine_Cargo(driver);
        cargo.clickEditButtonByRowInCargoCoverageTable(1);
        cargo.addAdditionalInsureds(Cargo_AdditionalInsured);
        cargo.clickOKButton();

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
        riskAnalysis.Quote();
        boolean prequote = false;
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        if (quote.isPreQuoteDisplayed()) {
            prequote = true;
            quote.clickPreQuoteDetails();
        }
        sideMenu.clickSideMenuRiskAnalysis();
        riskAnalysis.approveAll_IncludingSpecial();

        if (prequote && riskAnalysis.isQuotable()) {
            riskAnalysis.Quote();
        }

        sideMenu.clickSideMenuPayment();
        GenericWorkorderPayment payments = new GenericWorkorderPayment(driver);
        payments.makeDownPayment(PaymentPlanType.Annual, PaymentType.Cash, 0.00);
        sideMenu.clickSideMenuForms();
        sideMenu.clickSideMenuPayment();
        payments.SubmitOnly();

        GenericWorkorderComplete submittedPage = new GenericWorkorderComplete(driver);
        this.myPolicyObjPL.squire.setPolicyNumber(submittedPage.getPolicyNumber());

    }

    @Test(dependsOnMethods = {"testAddingAdditionalInsured"})
    private void testIssuanceAddValidateAdditionalInsured() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolicyObjPL.accountNumber);
        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickIssuePolicy();


        //deleting and adding Second Additional Insured
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        propertyDetail.clickViewOrEditBuildingButton(1);
        propertyDetail.removeAdditionalInsuredByName(property_AdditionalInsured);
        propertyDetail.addAddtionalInsured();
        propertyDetail.setAddtionalInsuredName(secondProperty_AdditionalInsured);
        propertyDetail.clickOk();
        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        vehiclePage.editVehicleByVehicleNumber(1);
        vehiclePage.removeAutoAdditionalInsured(Vehicle_AdditionalInsured);
        vehiclePage.addAutoAdditionalInsured(Second_Vehicle_AdditionalInsured);
        vehiclePage.clickOK();

        sideMenu.clickSideMenuIMRecreationVehicle();
        GenericWorkorderSquireInlandMarine_RecreationalEquipment recreationalEquipment = new GenericWorkorderSquireInlandMarine_RecreationalEquipment(driver);
        recreationalEquipment.clickLinkInRecreationalEquipmentTable(1);
        recreationalEquipment.removeAdditionalInsuredByName(RecEqipment_AdditionalInsured);
        recreationalEquipment.addAdditionalInsured(Second_RecEquipment_AdditionalInsured);
        recreationalEquipment.clickOK();

        sideMenu.clickSideMenuIMFarmEquipment();
        GenericWorkorderSquireInlandMarine_FarmEquipment farmEquipment = new GenericWorkorderSquireInlandMarine_FarmEquipment(driver);
        farmEquipment.clickEditButton();
        farmEquipment.removeAdditionalInsuredByName(FarmTruck_AdditionalInsured);
        farmEquipment.setAdditionalInsureds(Second_FarmTruck_AdditionalInsured);
        farmEquipment.clickOk();

        sideMenu.clickSideMenuIMWatercraft();
        GenericWorkorderSquireInlandMarine_Watercraft watercraft = new GenericWorkorderSquireInlandMarine_Watercraft(driver);
        watercraft.clickEditButton();
        watercraft.removeAdditionalInsuredByName(Watercraft_AdditionalInsured);
        watercraft.addAdditionalInsured(Second_Watercraft_AdditionalInsured);
        watercraft.clickOK();

        sideMenu.clickSideMenuIMPersonalEquipment();
        GenericWorkorderSquireInlandMarine_PersonalProperty personalProperty = new GenericWorkorderSquireInlandMarine_PersonalProperty(driver);
        personalProperty.clickEditButton();
        personalProperty.removeAdditionalInsuredByName(property_AdditionalInsured);
        ArrayList<String> additionalInsureds = new ArrayList<String>();
        additionalInsureds.add(secondProperty_AdditionalInsured);
        personalProperty.addAdditionalInsured(additionalInsureds);
        personalProperty.clickOk();

        sideMenu.clickSideMenuIMCargo();
        GenericWorkorderSquireInlandMarine_Cargo cargo = new GenericWorkorderSquireInlandMarine_Cargo(driver);
        cargo.clickEditButtonByRowInCargoCoverageTable(1);
        cargo.removeAdditionalInsuredByName(Cargo_AdditionalInsured);
        cargo.addAdditionalInsureds(Second_Cargo_AdditionalInsured);
        cargo.clickOKButton();

        sideMenu.clickSideMenuIMLivestock();
        GenericWorkorderSquireInlandMarine_Livestock livestock = new GenericWorkorderSquireInlandMarine_Livestock(driver);
        livestock.clickEditButton();
        livestock.removeAdditionalInsured(LiveStock_AdditonalInsured);
        ArrayList<String> liveInsureds = new ArrayList<String>();
        liveInsureds.add(Second_Livestock_AdditionalInsured);
        livestock.addAdditionalInsureds(liveInsureds);
        livestock.clickOk();

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
        riskAnalysis.Quote();
        boolean prequote = false;
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        if (quote.isPreQuoteDisplayed()) {
            prequote = true;
            quote.clickPreQuoteDetails();
        }
        sideMenu.clickSideMenuRiskAnalysis();
        riskAnalysis.approveAll_IncludingSpecial();

        if (prequote && riskAnalysis.isQuotable()) {
            riskAnalysis.Quote();
        }
        sideMenu.clickSideMenuQuote();
        quote.issuePolicy(IssuanceType.NoActionRequired);

        GenericWorkorderComplete submittedPage = new GenericWorkorderComplete(driver);
        myPolicyObjPL.squire.setPolicyNumber(submittedPage.getPolicyNumber());
        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"testIssuanceAddValidateAdditionalInsured"})
    private void testValidateSubmissionAdditionalInsured() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolicyObjPL.accountNumber);
        String errorMessage = "";
        PolicySummary summaryPage = new PolicySummary(driver);
        summaryPage.clickCompletedTransactionByType(TransactionType.Submission);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        if (!propertyDetail.getAdditionalInsuredNameByRowNumber(1).contains(property_AdditionalInsured)) {
            errorMessage = errorMessage + "Expected Additional Insured Name in Property Details is not matched with Submission /n";
        }
        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        if (!vehiclePage.getAdditionalInsuredNameByRowNumber(1).contains(Vehicle_AdditionalInsured)) {
            errorMessage = errorMessage + "Expected Additional Insured Name in Vehicle is not matched with Submission /n";
        }

        sideMenu.clickSideMenuIMRecreationVehicle();
        GenericWorkorderSquireInlandMarine_RecreationalEquipment recreationalEquipment = new GenericWorkorderSquireInlandMarine_RecreationalEquipment(driver);
        if (!recreationalEquipment.getAdditionalInsuredNameByRowNumber(1).contains(RecEqipment_AdditionalInsured)) {
            errorMessage = errorMessage + "Expected Additional Insured Name in Rec Equipment is not matched with Submission /n";
        }

        sideMenu.clickSideMenuIMFarmEquipment();
        GenericWorkorderSquireInlandMarine_FarmEquipment farmEquipment = new GenericWorkorderSquireInlandMarine_FarmEquipment(driver);
        if (!farmEquipment.getAdditionalInsuredNameByRowNumber(1).contains(FarmTruck_AdditionalInsured)) {
            errorMessage = errorMessage + "Expected Additional Insured Name in Farm Equipment is not matched with Submission /n";
        }

        sideMenu.clickSideMenuIMWatercraft();
        GenericWorkorderSquireInlandMarine_Watercraft watercraft = new GenericWorkorderSquireInlandMarine_Watercraft(driver);
        if (!watercraft.getAdditionalInsuredNameByRowNumber(1).contains(Watercraft_AdditionalInsured)) {
            errorMessage = errorMessage + "Expected Additional Insured Name in Watercraft is not matched with Submission /n";
        }

        sideMenu.clickSideMenuIMPersonalEquipment();
        GenericWorkorderSquireInlandMarine_PersonalProperty personalProperty = new GenericWorkorderSquireInlandMarine_PersonalProperty(driver);
        if (!personalProperty.getAdditionalInsuredNameByRowNumber(1).contains(property_AdditionalInsured)) {
            errorMessage = errorMessage + "Expected Additional Insured Name in Personal Property is not matched with Submission /n";
        }


        sideMenu.clickSideMenuIMCargo();
        GenericWorkorderSquireInlandMarine_Cargo cargo = new GenericWorkorderSquireInlandMarine_Cargo(driver);
        if (!cargo.getAdditionalInsuredNameByRowNumber(1).contains(Cargo_AdditionalInsured)) {
            errorMessage = errorMessage + "Expected Additional Insured Name in Cargo is not matched with Submission /n";
        }

        sideMenu.clickSideMenuIMLivestock();
        GenericWorkorderSquireInlandMarine_Livestock livestock = new GenericWorkorderSquireInlandMarine_Livestock(driver);
        if (!livestock.getAdditionalInsuredNameByRowNumber(1).contains(LiveStock_AdditonalInsured)) {
            errorMessage = errorMessage + "Expected Additional Insured Name in Livestock is not matched with Submission /n";
        }


    }

    @Test(dependsOnMethods = {"testValidateSubmissionAdditionalInsured"})
    private void testPolicyChangeAdditionalInsured() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolicyObjPL.accountNumber);

        Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);

        //start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.startPolicyChange("First policy Change", currentSystemDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        propertyDetail.clickViewOrEditBuildingButton(1);
        propertyDetail.removeAdditionalInsuredByName(secondProperty_AdditionalInsured);
        propertyDetail.addAddtionalInsured();
        propertyDetail.setAddtionalInsuredName(property_AdditionalInsured);
        propertyDetail.clickOk();
        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        vehiclePage.editVehicleByVehicleNumber(1);
        vehiclePage.removeAutoAdditionalInsured(Second_Vehicle_AdditionalInsured);
        vehiclePage.addAutoAdditionalInsured(Vehicle_AdditionalInsured);
        vehiclePage.clickOK();

        sideMenu.clickSideMenuIMRecreationVehicle();
        GenericWorkorderSquireInlandMarine_RecreationalEquipment recreationalEquipment = new GenericWorkorderSquireInlandMarine_RecreationalEquipment(driver);
        recreationalEquipment.clickLinkInRecreationalEquipmentTable(1);
        recreationalEquipment.removeAdditionalInsuredByName(Second_RecEquipment_AdditionalInsured);
        recreationalEquipment.addAdditionalInsured(RecEqipment_AdditionalInsured);
        recreationalEquipment.clickOK();

        sideMenu.clickSideMenuIMFarmEquipment();
        GenericWorkorderSquireInlandMarine_FarmEquipment farmEquipment = new GenericWorkorderSquireInlandMarine_FarmEquipment(driver);
        farmEquipment.clickEditButton();
        farmEquipment.removeAdditionalInsuredByName(Second_FarmTruck_AdditionalInsured);
        farmEquipment.setAdditionalInsureds(FarmTruck_AdditionalInsured);
        farmEquipment.clickOk();

        sideMenu.clickSideMenuIMWatercraft();
        GenericWorkorderSquireInlandMarine_Watercraft watercraft = new GenericWorkorderSquireInlandMarine_Watercraft(driver);
        watercraft.clickEditButton();
        watercraft.removeAdditionalInsuredByName(Second_Watercraft_AdditionalInsured);
        watercraft.addAdditionalInsured(Watercraft_AdditionalInsured);
        watercraft.clickOK();

        sideMenu.clickSideMenuIMPersonalEquipment();
        GenericWorkorderSquireInlandMarine_PersonalProperty personalProperty = new GenericWorkorderSquireInlandMarine_PersonalProperty(driver);
        personalProperty.clickEditButton();
        personalProperty.removeAdditionalInsuredByName(secondProperty_AdditionalInsured);
        ArrayList<String> additionalInsureds = new ArrayList<String>();
        additionalInsureds.add(property_AdditionalInsured);
        personalProperty.addAdditionalInsured(additionalInsureds);
        personalProperty.clickOk();

        sideMenu.clickSideMenuIMCargo();
        GenericWorkorderSquireInlandMarine_Cargo cargo = new GenericWorkorderSquireInlandMarine_Cargo(driver);
        cargo.clickEditButtonByRowInCargoCoverageTable(1);
        cargo.removeAdditionalInsuredByName(Second_Cargo_AdditionalInsured);
        cargo.addAdditionalInsureds(Cargo_AdditionalInsured);
        cargo.clickOKButton();

        sideMenu.clickSideMenuIMLivestock();
        GenericWorkorderSquireInlandMarine_Livestock livestock = new GenericWorkorderSquireInlandMarine_Livestock(driver);
        livestock.clickEditButton();
        livestock.removeAdditionalInsured(Second_Livestock_AdditionalInsured);
        ArrayList<String> liveInsureds = new ArrayList<String>();
        liveInsureds.add(LiveStock_AdditonalInsured);
        livestock.addAdditionalInsureds(liveInsureds);
        livestock.clickOk();

        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
        qualificationPage.clickQuote();
        GenericWorkorderRiskAnalysis riskAnaysis = new GenericWorkorderRiskAnalysis(driver);

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        boolean prequote = false;
        if (quote.isPreQuoteDisplayed()) {
            prequote = true;
            quote.clickPreQuoteDetails();
        }
        sideMenu.clickSideMenuRiskAnalysis();
        riskAnaysis.approveAll_IncludingSpecial();

        if (prequote && riskAnaysis.isQuotable()) {
            riskAnaysis.Quote();
        }
        sideMenu.clickSideMenuQuote();


        StartPolicyChange change = new StartPolicyChange(driver);
        change = new StartPolicyChange(driver);
        change.clickIssuePolicy();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
    }

    @Test(dependsOnMethods = {"testPolicyChangeAdditionalInsured"})
    private void testValidateIssuanceAddtionalInsured() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolicyObjPL.accountNumber);
        String errorMessage = "";
        PolicySummary summaryPage = new PolicySummary(driver);
        summaryPage.clickCompletedTransactionByType(TransactionType.Issuance);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        if (!propertyDetail.getAdditionalInsuredNameByRowNumber(1).contains(secondProperty_AdditionalInsured)) {
            errorMessage = errorMessage + "Expected Additional Insured Name in Property Details is not matched with Submission /n";
        }
        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        if (!vehiclePage.getAdditionalInsuredNameByRowNumber(1).contains(Second_Vehicle_AdditionalInsured)) {
            errorMessage = errorMessage + "Expected Additional Insured Name in Vehicle is not matched with Submission /n";
        }

        sideMenu.clickSideMenuIMRecreationVehicle();
        GenericWorkorderSquireInlandMarine_RecreationalEquipment recreationalEquipment = new GenericWorkorderSquireInlandMarine_RecreationalEquipment(driver);
        if (!recreationalEquipment.getAdditionalInsuredNameByRowNumber(1).contains(Second_RecEquipment_AdditionalInsured)) {
            errorMessage = errorMessage + "Expected Additional Insured Name in Rec Equipment is not matched with Submission /n";
        }

        sideMenu.clickSideMenuIMFarmEquipment();
        GenericWorkorderSquireInlandMarine_FarmEquipment farmEquipment = new GenericWorkorderSquireInlandMarine_FarmEquipment(driver);
        if (!farmEquipment.getAdditionalInsuredNameByRowNumber(1).contains(Second_FarmTruck_AdditionalInsured)) {
            errorMessage = errorMessage + "Expected Additional Insured Name in Farm Equipment is not matched with Submission /n";
        }

        sideMenu.clickSideMenuIMWatercraft();
        GenericWorkorderSquireInlandMarine_Watercraft watercraft = new GenericWorkorderSquireInlandMarine_Watercraft(driver);
        if (!watercraft.getAdditionalInsuredNameByRowNumber(1).contains(Second_Watercraft_AdditionalInsured)) {
            errorMessage = errorMessage + "Expected Additional Insured Name in Watercraft is not matched with Submission /n";
        }

        sideMenu.clickSideMenuIMPersonalEquipment();
        GenericWorkorderSquireInlandMarine_PersonalProperty personalProperty = new GenericWorkorderSquireInlandMarine_PersonalProperty(driver);
        if (!personalProperty.getAdditionalInsuredNameByRowNumber(1).contains(secondProperty_AdditionalInsured)) {
            errorMessage = errorMessage + "Expected Additional Insured Name in Personal Property is not matched with Submission /n";
        }

        sideMenu.clickSideMenuIMCargo();
        GenericWorkorderSquireInlandMarine_Cargo cargo = new GenericWorkorderSquireInlandMarine_Cargo(driver);
        if (!cargo.getAdditionalInsuredNameByRowNumber(1).contains(Second_Cargo_AdditionalInsured)) {
            errorMessage = errorMessage + "Expected Additional Insured Name in Cargo is not matched with Submission /n";
        }

        sideMenu.clickSideMenuIMLivestock();
        GenericWorkorderSquireInlandMarine_Livestock livestock = new GenericWorkorderSquireInlandMarine_Livestock(driver);
        if (!livestock.getAdditionalInsuredNameByRowNumber(1).contains(Second_Livestock_AdditionalInsured)) {
            errorMessage = errorMessage + "Expected Additional Insured Name in Livestock is not matched with Submission /n";
        }

    }
}

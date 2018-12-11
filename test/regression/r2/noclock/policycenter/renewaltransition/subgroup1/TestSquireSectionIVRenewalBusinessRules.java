package regression.r2.noclock.policycenter.renewaltransition.subgroup1;

import java.util.ArrayList;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.FPP.FPPCoverageTypes;
import repository.gw.enums.FPP.FPPDeductible;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.FPP.FarmPersonalPropertyTypes;
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
import repository.gw.enums.PersonalPropertyScheduledItemType;
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
import repository.gw.errorhandling.ErrorHandling;
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
import repository.gw.generate.custom.SquireIMCargo;
import repository.gw.generate.custom.SquireIMWatercraft;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.Vehicle;
import repository.gw.generate.custom.WatercraftScheduledItems;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_Livestock;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_LivestockList;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_Watercraft;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import persistence.globaldatarepo.helpers.VINHelper;

/**
 * @Author nvadlamudi
 * @Requirement :US7932: PL - Section IV - Renewal Business Rules
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Jun 27, 2016
 */
@QuarantineClass
public class TestSquireSectionIVRenewalBusinessRules extends BaseTest {
    private GeneratePolicy myPolicyObjPL;
    private Underwriters uw;

    private WebDriver driver;

    @Test(dependsOnMethods = {"createSquireIMPolicy"})
    public void testRenewalBusinessRules() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.accountNumber);

        StartRenewal renewal = new StartRenewal(driver);
        renewal.startRenewal();

        InfoBar infoBar = new InfoBar(driver);
        infoBar.clickInfoBarPolicyNumber();

        PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
        preRenewalPage.closePreRenewalExplanations(myPolicyObjPL);

        PolicySummary policySummary = new PolicySummary(driver);
        policySummary.clickPendingTransaction(TransactionType.Renewal);

        // Adding watercraft for validations
        addingWaterCraft();

        // Adding FPP
        checkingDeathLiveStockAndFPPQuoteError();

        // checking the bind issues/erors
        validateBindIssuesErrorMessages(null);

    }

    private void validateBindIssuesErrorMessages(String[] expectedQuoteMessage) {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        // sideMenu.clickSideMenuRiskAnalysis();
        boolean testFailed = false;
        String errorMessage = "";
        GenericWorkorderRiskAnalysis_UWIssues riskAnalysis = new GenericWorkorderRiskAnalysis_UWIssues(driver);

        String[] expectedUWMessages = {"Personal Watercraft equal to or over $15,000", "Personal Property equal to or over $20,000",
                "Personal Property equal to or over $50,000", "Recreational Equipment equal to or over $50,000", "Watercraft equal to or over $65,000",
                "Recreational Equipment equal or more than $15,000", "Appraisal date older than 2 years",
                "Class III Cargo radius over 500", "Farm Equipment coverage must have Special Form",
                "Valid Death of Livestock"};

        if (expectedQuoteMessage != null) {
            expectedUWMessages = expectedQuoteMessage;
        }

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);

        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
        } else {
            sideMenu.clickSideMenuRiskAnalysis();
        }

        for (String uwIssue : expectedUWMessages) {
            boolean messageFound = false;
            for (int i = 0; i < riskAnalysis.getUWIssuesList().size(); i++) {
                String currentUWIssueText = riskAnalysis.getUWIssuesList().get(i).getText();
                if (!currentUWIssueText.contains("Blocking Bind") && (!currentUWIssueText.contains("Blocking Issuance"))
                        && !currentUWIssueText.contains("Blocking Quote")) {
                    if (currentUWIssueText.contains(uwIssue)) {
                        messageFound = true;
                        break;
                    }
                }
            }
            if (!messageFound) {
                testFailed = true;
                errorMessage = errorMessage + "Expected error Bind Issue : " + uwIssue + " is not displayed.";
            }
        }

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + errorMessage);
        }
    }

    private void addingWaterCraft() throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuIMWatercraft();

        WatercraftScheduledItems personalWater = new WatercraftScheduledItems(WatercraftTypes.PersonalWatercraft,
                DateUtils.convertStringtoDate("01/01/1999", "MM/dd/yyyy"), 16000);
        personalWater.setLimit(16000);
        personalWater.setItem(WatercratItems.PersonalWatercraft);
        ArrayList<WatercraftScheduledItems> personalWaterCrafts = new ArrayList<WatercraftScheduledItems>();
        personalWaterCrafts.add(personalWater);
        SquireIMWatercraft waterCrafts = new SquireIMWatercraft(WatercraftTypes.PersonalWatercraft,
                PAComprehensive_CollisionDeductible.OneHundred100, personalWaterCrafts);

        // Adding watercraft vehicles
        GenericWorkorderSquireInlandMarine_Watercraft watercraftPage = new GenericWorkorderSquireInlandMarine_Watercraft(driver);
        watercraftPage.watercraft(waterCrafts);

        try {
            sideMenu.clickSideMenuSquirePropertyCoverages();
        } catch (Exception e) {
            ErrorHandling softMsg = new ErrorHandling(driver);
            if (softMsg.checkIfElementExists(softMsg.text_ErrorHandlingErrorBanner(), 1000)) {
                sideMenu.clickSideMenuSquirePropertyCoverages();
            }
        }
        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        coverages.clickSectionIICoveragesTab();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages sectionII = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
        SectionIICoverages sectionIICoverage = new SectionIICoverages(SectionIICoveragesEnum.PersonalWatercraft, 0, 2);
        sectionII.addCoverages(sectionIICoverage);
        sectionII.setQuantity(sectionIICoverage);
    }

    private void checkingDeathLiveStockAndFPPQuoteError() throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        // Validating the livestock quote error message
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();
        quote.clickQuote();
        String[] expectedQuoteMessage = {"Livestock per head over $10,000"};
        validateBindIssuesErrorMessages(expectedQuoteMessage);
        new GuidewireHelpers(driver).editPolicyTransaction();
        // changing the existing livestock to death of livestock
        sideMenu.clickSideMenuIMLivestock();
        GenericWorkorderSquireInlandMarine_LivestockList livestockListPage = new GenericWorkorderSquireInlandMarine_LivestockList(driver);
        livestockListPage.removeExistingLiveStock(1);
        livestockListPage.clickAdd();
        GenericWorkorderSquireInlandMarine_Livestock livestockPage = new GenericWorkorderSquireInlandMarine_Livestock(driver);
        livestockPage.setType(LivestockType.DeathOfLivestock);
        livestockPage.addScheduledItemForDeathOfLivestock(LivestockScheduledItemType.SheepD, 1);
        livestockPage.sendArbitraryKeys(Keys.TAB);
        livestockPage.clickOk();

        // removing from Coverages
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        coverages.clickSectionIICoveragesTab();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages sectionII = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
        sectionII.clickLiveStockCheckBox();

        // FPP
        sideMenu.clickSideMenuSquirePropertyCoverages();
        coverages.clickFarmPersonalProperty();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages fppCovs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages(driver);
        fppCovs.checkCoverageD(true);
        fppCovs.selectCoverageType(FPPCoverageTypes.BlanketInclude);
        fppCovs.selectDeductible(FPPDeductible.Ded_500);
        fppCovs.selectCoverages(FarmPersonalPropertyTypes.Machinery);
        fppCovs.addItem(FPPFarmPersonalPropertySubTypes.Tractors, 1, 1000, "Testing Description");

        quote.clickSaveDraftButton();
        quote.clickQuote();
        quote.clickPreQuoteDetails();
    }

    @Test()
    private void createSquireIMPolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Vin vin = VINHelper.getRandomVIN();
        ArrayList<PolicyLocation> locationsList = new ArrayList<>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<>();

        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locationsList.add(locToAdd);

        // Cargo
        Cargo cargoTrailer1 = new Cargo(VehicleType.Trailer, vin.getVin(), Integer.parseInt(vin.getYear()),
                vin.getMake(), vin.getModel());
        cargoTrailer1.setLimit("3123");
        ArrayList<Cargo> cargoTrailers = new ArrayList<Cargo>();
        cargoTrailers.add(cargoTrailer1);
        ArrayList<String> cargoAddInsured = new ArrayList<String>();
        cargoAddInsured.add("Cor Hofman");
        SquireIMCargo stuff = new SquireIMCargo(CargoClass.ClassIII, ClassIIICargoTypes.Milk, 550,
                1000, "Brenda Swindle", false, cargoTrailers, cargoAddInsured);
        SquireIMCargo cargo = new SquireIMCargo(CargoClass.ClassI, 100, 1000, "Brenda Swindle",
                false, cargoTrailers);
        ArrayList<SquireIMCargo> cargoList = new ArrayList<SquireIMCargo>();
        cargoList.add(cargo);
        cargoList.add(stuff);

        // Farm Equipment
        IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment",
                "Manly Farm Equipment", 1000);
        ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
        farmEquip.add(farmThing);
        FarmEquipment imFarmEquip = new FarmEquipment(IMFarmEquipmentType.FarmEquipment, CoverageType.BroadForm,
                IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
        ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
        allFarmEquip.add(imFarmEquip);

        // Rec Equip
        ArrayList<RecreationalEquipment> recVehicle = new ArrayList<RecreationalEquipment>();
        recVehicle.add(new RecreationalEquipment(RecreationalEquipmentType.AllTerrainVehicle, "500",
                PAComprehensive_CollisionDeductible.Fifty50, "Brett Hiltbrand"));
        recVehicle.add(new RecreationalEquipment(RecreationalEquipmentType.Snowmobile, "55000",
                PAComprehensive_CollisionDeductible.Fifty50, "Test Automation"));
        recVehicle.add(new RecreationalEquipment(RecreationalEquipmentType.OffRoadMotorcycle, "16000",
                PAComprehensive_CollisionDeductible.Fifty50, "Test Automation"));

        // PersonalProperty
        PersonalProperty pprop = new PersonalProperty();
        pprop.setType(PersonalPropertyType.MedicalSuppliesAndEquipment);
        pprop.setYear(2010);
        pprop.setMake("Testmake");
        pprop.setModel("Testmodel");
        pprop.setVinSerialNum("123123123");
        pprop.setDescription("Testdescription");
        pprop.setLimit(52000);
        pprop.setDeductible(PersonalPropertyDeductible.Ded25);
        ArrayList<String> ppropAdditIns = new ArrayList<String>();
        ppropAdditIns.add("First Guy");
        ppropAdditIns.add("Second Guy");
        pprop.setAdditionalInsureds(ppropAdditIns);

        PersonalProperty pprop1 = new PersonalProperty();
        pprop1.setType(PersonalPropertyType.Collectibles);
        pprop1.setLimit(1500);
        pprop1.setDeductible(PersonalPropertyDeductible.Ded5Perc);
        ArrayList<PersonalPropertyScheduledItem> antiqueScheduledItems = new ArrayList<PersonalPropertyScheduledItem>();
        pprop1.setScheduledItems(antiqueScheduledItems);

        PersonalPropertyScheduledItem antiqueScheduled = new PersonalPropertyScheduledItem();
        antiqueScheduled.setParentPersonalPropertyType(PersonalPropertyType.Collectibles);
        antiqueScheduled.setDescription("Testing Antique Collectibles");
        antiqueScheduled.setLimit(52000);
        antiqueScheduled.setAppraisalDate(DateUtils.convertStringtoDate("01/01/2014", "MM/dd/YYYY"));
        antiqueScheduledItems.add(antiqueScheduled);

        PersonalProperty pprop2 = new PersonalProperty();
        pprop2.setType(PersonalPropertyType.SportingEquipment);
        pprop2.setLimit(21000);
        pprop2.setDeductible(PersonalPropertyDeductible.Ded1000);
        PersonalPropertyScheduledItem sportsScheduledItem = new PersonalPropertyScheduledItem();
        sportsScheduledItem.setParentPersonalPropertyType(PersonalPropertyType.SportingEquipment);
        sportsScheduledItem.setLimit(21000);
        sportsScheduledItem.setDescription("Sports Stuff");
        sportsScheduledItem.setType(PersonalPropertyScheduledItemType.Guns);
        sportsScheduledItem.setMake("Honda");
        sportsScheduledItem.setModel("Accord");
        sportsScheduledItem.setYear(2015);
        sportsScheduledItem.setVinSerialNum("abcd12345");
        ArrayList<PersonalPropertyScheduledItem> sportsScheduledItems = new ArrayList<PersonalPropertyScheduledItem>();
        sportsScheduledItems.add(sportsScheduledItem);
        pprop2.setScheduledItems(sportsScheduledItems);

        ArrayList<PersonalPropertyScheduledItem> propertyScheduledItems = new ArrayList<PersonalPropertyScheduledItem>();
        propertyScheduledItems.add(antiqueScheduled);
        pprop1.setScheduledItems(propertyScheduledItems);

        PersonalPropertyList ppropList = new PersonalPropertyList();
        ArrayList<PersonalProperty> msaeList = new ArrayList<PersonalProperty>();
        msaeList.add(pprop);
        ppropList.setMedicalSuppliesAndEquipment(msaeList);
        ppropList.setCollectibles(pprop1);
        ppropList.setSportingEquipment(pprop2);

        // Livestock
        Livestock livestock = new Livestock();
        livestock.setType(LivestockType.Livestock);
        livestock.setDeductible(LivestockDeductible.Ded100);
        LivestockScheduledItem livSchedItem1 = new LivestockScheduledItem(livestock.getType(),
                LivestockScheduledItemType.Alpaca, "Test Alpaca 1", 11000);
        LivestockScheduledItem livSchedItem2 = new LivestockScheduledItem(livestock.getType(),
                LivestockScheduledItemType.Bull, "Test BullCow 1", "TAG123", "Brand1", "Breed1", 5000);
        ArrayList<String> livSchedItem2AdditInsureds = new ArrayList<String>();
        livSchedItem2AdditInsureds.add("Sched Item Guy1");
        livSchedItem2AdditInsureds.add("Sched Item Guy2");
        livSchedItem2.setAdditionalInsureds(livSchedItem2AdditInsureds);
        ArrayList<LivestockScheduledItem> livSchedItems = new ArrayList<LivestockScheduledItem>();
        livSchedItems.add(livSchedItem1);
        livSchedItems.add(livSchedItem2);
        livestock.setScheduledItems(livSchedItems);
        ArrayList<String> livAdditInsureds = new ArrayList<String>();
        livAdditInsureds.add("Cor Hofman");
        livAdditInsureds.add("Rusty Young");
        livestock.setAdditionalInsureds(livAdditInsureds);
        LivestockList allLivestock = new LivestockList(livestock, null, null);

        // Watercraft
        ArrayList<AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
        AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Person);
        loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);
        WatercraftScheduledItems boat = new WatercraftScheduledItems(WatercraftTypes.BoatAndMotor,
                DateUtils.convertStringtoDate("01/01/1999", "MM/dd/yyyy"), 66000);
        boat.setLimit(66000);
        boat.setItem(WatercratItems.Boat);
        boat.setLength(28);
        boat.setBoatType(BoatType.Outboard);

        ArrayList<WatercraftScheduledItems> boats = new ArrayList<WatercraftScheduledItems>();
        boats.add(boat);
        ArrayList<String> boatAddInsured = new ArrayList<String>();
        boatAddInsured.add("Cor Hofman");
        SquireIMWatercraft boatType = new SquireIMWatercraft(WatercraftTypes.BoatAndMotor,
                PAComprehensive_CollisionDeductible.OneHundred100, boats);

        boatType.setAdditionalInterest(loc2Bldg1AdditionalInterests);
        ArrayList<SquireIMWatercraft> watercratList = new ArrayList<SquireIMWatercraft>();

        watercratList.add(boatType);

        SquireLiability liability = new SquireLiability();
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

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow,
                MedicalLimit.TenK);
        coverages.setUnderinsured(false);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto(driversList, vehicleList, coverages);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liability;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;
        myInlandMarine.cargo_PL_IM = cargoList;
        myInlandMarine.recEquipment_PL_IM = recVehicle;
        myInlandMarine.personalProperty_PL_IM = ppropList.getAllPersonalPropertyAsList();
        myInlandMarine.livestock_PL_IM = allLivestock.getAllLivestockAsList();
        myInlandMarine.watercrafts_PL_IM = watercratList;
        myInlandMarine.farmEquipment = allFarmEquip;


        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL, LineSelection.PersonalAutoLinePL)
                .withPolTermLengthDays(79)
                .withInsFirstLastName("Renew", "Rules")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }
}

















































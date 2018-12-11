package regression.r2.noclock.policycenter.submission_fire_im.subgroup2;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import services.broker.objects.vin.requestresponse.ValidateVINResponse;
import services.broker.services.vin.ServiceVINValidation;
import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.BoatType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.InlandMarineTypes.RecreationalEquipmentType;
import repository.gw.enums.InlandMarineTypes.WatercraftTypes;
import repository.gw.enums.InlandMarineTypes.WatercratItems;
import repository.gw.enums.LineSelection;
import repository.gw.enums.LivestockDeductible;
import repository.gw.enums.LivestockScheduledItemType;
import repository.gw.enums.LivestockType;
import repository.gw.enums.PersonalPropertyDeductible;
import repository.gw.enums.PersonalPropertyType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.Vehicle.PAComprehensive_CollisionDeductible;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.Livestock;
import repository.gw.generate.custom.LivestockList;
import repository.gw.generate.custom.LivestockScheduledItem;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PersonalProperty;
import repository.gw.generate.custom.PersonalPropertyList;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.RecreationalEquipment;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireIMWatercraft;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.WatercraftScheduledItems;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_LineReview;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_LivestockList;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_RecreationalEquipment;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.VINHelper;

/**
 * @Author nvadlamudi
 * @Requirement
 * @RequirementsLink <a href="http:// "
 * rally1.rallydev.com/#/33274298124d/detail/userstory/
 * 50286397086</a>
 * @Description - Test used to verify the inland marine section 4 item added and removed are reflected in the line review screen. RY 5/5/2017
 * @DATE Mar 3, 2016
 */
@QuarantineClass
public class TestSquireInlandMarineLineReview extends BaseTest {

    public GeneratePolicy myPolicyObjPL = null;
    private WebDriver driver;

    @BeforeMethod
    public void beforeMethod() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
    }



    @Test
    public void validateIMLineReview() throws Exception {
        myPolicyObjPL = createPLAutoPolicy(GeneratePolicyType.FullApp);
        Agents agent = myPolicyObjPL.agentInfo;
        ArrayList<RecreationalEquipment> vehicle = myPolicyObjPL.squire.inlandMarine.recEquipment_PL_IM;

        new Login(driver).loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), myPolicyObjPL.accountNumber);

        // Opening IM Line Review page
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuHouseholdMembers();
        new GuidewireHelpers(driver).editPolicyTransaction();
        sideMenu.clickSideMenuSquireIMLineReview();

        GenericWorkorderSquireInlandMarine_LineReview lineReview = new GenericWorkorderSquireInlandMarine_LineReview(driver);
        // validations
        validateIMLineReviewRecreationalEquiPage(lineReview, vehicle);
        validateIMLineReviewWatercraft(lineReview, myPolicyObjPL.squire.inlandMarine.watercrafts_PL_IM.get(0));
        validateIMLineReviewFarmEquipment(lineReview, myPolicyObjPL.squire.inlandMarine.farmEquipment.get(0));
        validateIMLineReviewPersonalProperty(lineReview, myPolicyObjPL.squire.inlandMarine.personalProperty_PL_IM.get(0));
        validateIMLineReviewLiveStock(lineReview, myPolicyObjPL.squire.inlandMarine.livestock_PL_IM.get(0));
        removeRecreationalEquipValidateLineReviewPage(lineReview, vehicle);

        sideMenu.clickSideMenuIMLivestock();
        GenericWorkorderSquireInlandMarine_LivestockList livestockListPage = new GenericWorkorderSquireInlandMarine_LivestockList(driver);
        livestockListPage.highLihtLivestockByType(LivestockType.DeathOfLivestock);
        if (!livestockListPage.getLimit().contains("2,500 / 25,000"))
            Assert.fail("Expected Death Of Livesock Limit :  2,500 / 25,000");
    }

    private void validateIMLineReviewLiveStock(GenericWorkorderSquireInlandMarine_LineReview lineReview, Livestock livestock) {
        boolean testFailed = false;
        String errorMessage = "";
        lineReview.clickLivestockTab();

        if (("" + livestock.getScheduledItems().get(0).getLimit()).contains(lineReview.getLiveStockTableCellByColumnName(1, "Limit").replace(",", "")))
            System.out.println("Expected Livestock Limit: " + livestock.getScheduledItems().get(0).getLimit() + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected Livestock Limit: " + livestock.getScheduledItems().get(0).getLimit() + " is not displayed.";
        }

        if (livestock.getDeductible().getValue().contains(lineReview.getLiveStockTableCellByColumnName(1, "Deductible")))
            System.out.println("Expected Livestock Deductible: " + livestock.getDeductible().getValue() + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected Livestock Deductible: " + livestock.getDeductible().getValue() + " is not displayed.";
        }

        if (lineReview.getDeathLiveStockTableCellByColumnName(1, "Limit").contains("2,500 / 25,000"))
            System.out.println("Expected Livestock Limit: 2,500 / 25,000 is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected Death Livestock Limit: 2,500/25,000 is not displayed.";
        }

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + errorMessage);
        }
    }

    private void removeRecreationalEquipValidateLineReviewPage(GenericWorkorderSquireInlandMarine_LineReview lineReview, ArrayList<RecreationalEquipment> vehicle) throws Exception {
        boolean testFailed = false;
        String errorMessage = "";
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuIMRecreationVehicle();
        sideMenu.clickSideMenuIMRecreationVehicle();
        GenericWorkorderSquireInlandMarine_RecreationalEquipment recEquipment = new GenericWorkorderSquireInlandMarine_RecreationalEquipment(driver);
        recEquipment.setRETableCheckBoxByTypeAndRemove(recEquipment.getRETableCellByColumnName(2, "Type"));
        sideMenu.clickSideMenuSquireIMLineReview();
        lineReview.clickRecreationalEquipmentTab();
        int currentRERowCount = lineReview.getRecreationalEquipmentTableRowCount();
        if (currentRERowCount == 1)
            System.out.println("Recreational Equipment Row is deleted.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected Recreational Equipment Vehicle : " + lineReview.getRecreationalEquimentVehicleCellByColumnName(2, "Type") + " is not deleted. ";
        }

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + errorMessage);
        }

    }

    private void validateIMLineReviewPersonalProperty(GenericWorkorderSquireInlandMarine_LineReview lineReview, PersonalProperty personalProperty) {
        boolean testFailed = false;
        String errorMessage = "";

        lineReview.clickPersonalPropertyTab();

        if (personalProperty.getType().getValue().contains(lineReview.getPersonalPropertyTableCellByColumnName(1, "Type")))
            System.out.println("Expected Personal Property Type : " + personalProperty.getType().getValue() + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected Personal Property Type : " + personalProperty.getType().getValue() + " is not displayed.";
        }

        if ((personalProperty.getYear() + " " + personalProperty.getMake()).contains(lineReview.getPersonalPropertyTableCellByColumnName(1, "Year/Make")))
            System.out.println("Expected Personal Property Year Make : " + personalProperty.getYear() + " " + personalProperty.getMake() + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected Personal Property Year Make : " + personalProperty.getYear() + " " + personalProperty.getMake() + " is not displayed.";
        }
        int currentLimit = personalProperty.getLimit();
        if (("" + currentLimit).contains(lineReview.getPersonalPropertyTableCellByColumnName(1, "Limit")))
            System.out.println("Expected Personal Property Limit : " + currentLimit + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected Personal Property Limit : " + currentLimit + " is not displayed.";
        }

        if (personalProperty.getDeductible().getValue().contains(lineReview.getPersonalPropertyTableCellByColumnName(1, "Deductible")))
            System.out.println("Expected Personal Property Deductible : " + personalProperty.getDeductible().getValue() + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected Personal Property Deductible : " + personalProperty.getDeductible().getValue() + " is not displayed.";
        }
        if (testFailed)
            Assert.fail(driver.getCurrentUrl() + errorMessage);

    }

    private void validateIMLineReviewFarmEquipment(GenericWorkorderSquireInlandMarine_LineReview lineReview, FarmEquipment plfpp) {
        boolean testFailed = false;
        String errorMessage = "";

        lineReview.clickFarmEquipmentTab();
        // Validations
        if (plfpp.getCoverageType().getValue().contains(lineReview.getFarmEquipmentTableCellByColumnName(1, "Coverage Type")))
            System.out.println("Expected farm Equipment Coverage Type : " + plfpp.getCoverageType().getValue() + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected farm Equipment Coverage Type : " + plfpp.getCoverageType().getValue() + " is not displayed.";
        }

        if (plfpp.getIMFarmEquipmentType().getValue().contains(lineReview.getFarmEquipmentTableCellByColumnName(1, "Type")))
            System.out.println("Expected farm Equipment Type : " + plfpp.getIMFarmEquipmentType().getValue() + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected farm Equipment Type : " + plfpp.getIMFarmEquipmentType().getValue() + " is not displayed.";
        }

        double farmEquipmentLimit = plfpp.getScheduledFarmEquipment().get(0).getLimit();
        double farmEquipmentScheduleLimit = Double.parseDouble(lineReview.getFarmEquipmentVehicleTableCellByColumnName(1, "Limit"));
        if (farmEquipmentLimit - farmEquipmentScheduleLimit == 0)
            System.out.println("Expected farm Equipment Limit : " + farmEquipmentLimit + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected farm Equipment Limit : " + farmEquipmentLimit + " is not displayed.";
        }

        if (plfpp.getDeductible().getValue().contains(lineReview.getFarmEquipmentTableCellByColumnName(1, "Deductible")))
            System.out.println("Expected farm equipment deductible " + plfpp.getDeductible().getValue() + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected farm equipment deductible " + plfpp.getDeductible().getValue() + " is not displayed.";
        }

        if ((plfpp.getScheduledFarmEquipment().get(0).getYear() + " " + plfpp.getScheduledFarmEquipment().get(0).getMake()).contains(lineReview.getFarmEquipmentVehicleTableCellByColumnName(1, "Year/Make")))
            System.out.println("Expected farm Equipment Year Make: " + plfpp.getScheduledFarmEquipment().get(0).getYear() + " " + plfpp.getScheduledFarmEquipment().get(0).getMake() + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected farm Equipment Year Make: " + plfpp.getScheduledFarmEquipment().get(0).getYear() + " " + plfpp.getScheduledFarmEquipment().get(0).getMake() + " is not displayed.";
        }

        if (plfpp.getScheduledFarmEquipment().get(0).getTypeOfEquipment().contains(lineReview.getFarmEquipmentVehicleTableCellByColumnName(1, "Type of Equipment")))
            System.out.println("Expected farm Equipment Schedule type: " + plfpp.getScheduledFarmEquipment().get(0).getTypeOfEquipment() + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected farm Equipment Schedule type: " + plfpp.getScheduledFarmEquipment().get(0).getTypeOfEquipment() + " is not displayed.";
        }

        if (plfpp.getScheduledFarmEquipment().get(0).getVin().contains(lineReview.getFarmEquipmentVehicleTableCellByColumnName(1, "VIN/Serial #")))
            System.out.println("Expected farm Equipment VIN: " + plfpp.getScheduledFarmEquipment().get(0).getVin() + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected farm Equipment VIN: " + plfpp.getScheduledFarmEquipment().get(0).getVin() + " is not displayed.";
        }
        if (testFailed)
            Assert.fail(driver.getCurrentUrl() + errorMessage);
    }

    private void validateIMLineReviewWatercraft(GenericWorkorderSquireInlandMarine_LineReview lineReview, SquireIMWatercraft squireIMWatercraft) {
        boolean testFailed = false;
        String errorMessage = "";

        // validate recreational equipment
        lineReview.clickWatercraftTab();

        // Validations
        if (squireIMWatercraft.getWaterToyType().getValue().contains(lineReview.getWatercraftTableCellByColumnName(1, "Type")))
            System.out.println("Expected Watercraft Type : " + squireIMWatercraft.getWaterToyType().getValue() + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected Watercraft Type : " + squireIMWatercraft.getWaterToyType().getValue() + " is not displayed.";
        }
        double waterCraftLimit = squireIMWatercraft.getScheduledItems().get(0).getLimit();
        System.out.println(lineReview.getWatercraftTableCellByColumnName(1, "Limit"));
        double waterCraftScheduleLimit = Double.parseDouble(lineReview.getWatercraftTableCellByColumnName(1, "Limit"));

        double limitValue = Double.parseDouble(lineReview.getWatercraftVehicleTableCellByColumnName(1, "Limit"));
        if (waterCraftLimit - limitValue == 0 && waterCraftLimit - waterCraftScheduleLimit == 0)
            System.out.println("Expected waterCraft Schedule limit : " + lineReview.getWatercraftTableCellByColumnName(1, "Limit") + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected waterCraft Schedule limit : " + lineReview.getWatercraftTableCellByColumnName(1, "Limit") + " is not displayed.";
        }

        if (squireIMWatercraft.getWaterToyDeductible().getValue().contains(lineReview.getWatercraftTableCellByColumnName(1, "Deductible")))
            System.out.println("Expected Watercraft Deductible value: " + squireIMWatercraft.getWaterToyDeductible().getValue() + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected Watercraft Deductible value: " + squireIMWatercraft.getWaterToyDeductible().getValue() + " is not displayed.";
        }

        if (squireIMWatercraft.getScheduledItems().get(0).getItem().getValue().contains(lineReview.getWatercraftVehicleTableCellByColumnName(1, "Schedule")))
            System.out.println("Expected watercraft Schedule Item : " + squireIMWatercraft.getScheduledItems() + " is displayed. ");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected watercraft Schedule Item : " + squireIMWatercraft.getScheduledItems() + " is not displayed. ";
        }

        String watercraftYearMake = squireIMWatercraft.getScheduledItems().get(0).getYear() + " " + squireIMWatercraft.getScheduledItems().get(0).getMake();
        if (watercraftYearMake.contains(lineReview.getWatercraftVehicleTableCellByColumnName(1, "Year/Make")))
            System.out.println("Expected watercraft Year Make : " + watercraftYearMake + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected watercraft Year Make : " + watercraftYearMake + " is not displayed.";
        }

        if (squireIMWatercraft.getScheduledItems().get(0).getVin().contains(lineReview.getWatercraftVehicleTableCellByColumnName(1, "VIN/Serial #")))
            System.out.println("Expected watercraft VIN/Serial # : " + squireIMWatercraft.getScheduledItems().get(0).getVin() + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected watercraft VIN/Serial # : " + squireIMWatercraft.getScheduledItems().get(0).getVin() + " is not displayed.";
        }
        if (testFailed)
            Assert.fail(driver.getCurrentUrl() + errorMessage);
    }

    /**
     * @param lineReview
     * @param vehicleList
     * @Author nvadlamudi
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description
     * @DATE Mar 3, 2016
     */
    private void validateIMLineReviewRecreationalEquiPage(GenericWorkorderSquireInlandMarine_LineReview lineReview, ArrayList<RecreationalEquipment> vehicleList) {
        boolean testFailed = false;
        String errorMessage = "";
        // validate recreational equipment
        lineReview.clickRecreationalEquipmentTab();
        boolean recordFound = false;

        int vehicleRowCount = lineReview.getRecreationalEquipmentTableRowCount();

        for (RecreationalEquipment vehicle : vehicleList) {
            recordFound = false;
            for (int currentRow = 1; currentRow <= vehicleRowCount; currentRow++) {
                if (vehicle.getRecEquipmentType().getValue().contains(lineReview.getRecreationalEquimentVehicleCellByColumnName(currentRow, "Type"))) {
                    System.out.println("Expected Recreational Equipment Type : " + vehicle.getRecEquipmentType().getValue() + " is displayed.");
                    recordFound = true;
                    String vehicleYearModel = vehicle.getModelYear() + " " + vehicle.getMake();
                    if ((vehicleYearModel).contains(lineReview.getRecreationalEquimentVehicleCellByColumnName(currentRow, "Year/Make"))) {
                        System.out.println("Expected Vehicle Year/Make : " + vehicleYearModel + " is displayed.");
                    } else {
                        testFailed = true;
                        errorMessage = errorMessage + "Expected Vehicle Year/Make : " + vehicleYearModel + " is not displayed.";
                    }
                    if (vehicle.getLimit().contains(lineReview.getRecreationalEquimentVehicleCellByColumnName(1, "Limit")))
                        System.out.println("Expected Limit : " + vehicle.getLimit() + " is displayed.");
                    else {
                        testFailed = true;
                        errorMessage = errorMessage + "Expected Limit : " + vehicle.getLimit() + " is not displayed.";
                    }

                    if (PAComprehensive_CollisionDeductible.Fifty50.getValue().contains(lineReview.getRecreationalEquimentVehicleCellByColumnName(currentRow, "Deductible")))
                        System.out.println("Expected Deductible : " + PAComprehensive_CollisionDeductible.Fifty50.getValue() + " is displayed.");
                    else {
                        testFailed = true;
                        errorMessage = errorMessage + "Expected Deductible : " + PAComprehensive_CollisionDeductible.Fifty50.getValue() + " is not displayed.";
                    }
                }
                if (recordFound)
                    break;
            }
        }

        if (!recordFound) {
            testFailed = true;
            errorMessage = errorMessage + "Expected Vehicle type is not displayed.";
        }

        if (testFailed)
            Assert.fail(driver.getCurrentUrl() + errorMessage);

    }

    /**
     * @param policyType
     * @return
     * @throws Exception
     * @Author nvadlamudi
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description
     * @DATE Mar 4, 2016
     */
    private GeneratePolicy createPLAutoPolicy(GeneratePolicyType policyType) throws Exception {

        ArrayList<InlandMarine> imCoverages = new ArrayList<InlandMarine>();
        imCoverages.add(InlandMarine.Watercraft);
        imCoverages.add(InlandMarine.RecreationalEquipment);
        imCoverages.add(InlandMarine.FarmEquipment);
        imCoverages.add(InlandMarine.PersonalProperty);
        imCoverages.add(InlandMarine.Livestock);

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
        AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(
                ContactSubType.Person);
        loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);
        SquireIMWatercraft boatType = new SquireIMWatercraft(WatercraftTypes.BoatAndMotor, PAComprehensive_CollisionDeductible.OneHundred100, boats);
        boatType.setAdditionalInterest(loc2Bldg1AdditionalInterests);
        ArrayList<SquireIMWatercraft> boatTypes = new ArrayList<SquireIMWatercraft>();
        boatTypes.add(boatType);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        locationsList.add(new PolicyLocation(locOnePropertyList));

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        ArrayList<RecreationalEquipment> vehicleList = new ArrayList<RecreationalEquipment>();
        vehicleList.add(new RecreationalEquipment(RecreationalEquipmentType.Snowmobile, "16561", PAComprehensive_CollisionDeductible.Fifty50, "Bill Martin"));
        vehicleList.add(new RecreationalEquipment(RecreationalEquipmentType.OffRoadMotorcycle, "16561", PAComprehensive_CollisionDeductible.Fifty50, "Bill Martin"));


        // Farm Equipment
        IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment", "Manly Farm Equipment", 1000);
        ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
        farmEquip.add(farmThing);


        FarmEquipment imFarmEquip = new FarmEquipment(IMFarmEquipmentType.CircleSprinkler, CoverageType.BroadForm, IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
        imFarmEquip.setAdditionalInterests(loc2Bldg1AdditionalInterests);
        ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
        allFarmEquip.add(imFarmEquip);

        // Personal Property
        PersonalProperty pprop = new PersonalProperty();
        pprop.setType(PersonalPropertyType.MedicalSuppliesAndEquipment);
        pprop.setDeductible(PersonalPropertyDeductible.Ded25);

        // Vin helper
        Vin vin = VINHelper.getRandomVIN();
        boolean printRequestXMLToConsole = false;
        boolean printResponseXMLToConsole = false;
        ServiceVINValidation testService = new ServiceVINValidation();
        ValidateVINResponse testResponse = testService.validateVIN2(testService.setUpTestValidateVINRequest(vin.getVin()), new GuidewireHelpers(driver).getMessageBrokerConnDetails(), printRequestXMLToConsole, printResponseXMLToConsole);

        pprop.setYear(testResponse.getYear());
        pprop.setMake(testResponse.getMake());
        pprop.setModel(testResponse.getModel());
        pprop.setVinSerialNum(vin.getVin());
        pprop.setDescription("Testdescription");
        pprop.setLimit(1234);

        ArrayList<String> ppropAdditIns = new ArrayList<String>();
        ppropAdditIns.add("First Guy");
        pprop.setAdditionalInsureds(ppropAdditIns);

        PersonalPropertyList ppropList = new PersonalPropertyList();
        ArrayList<PersonalProperty> msaeList = new ArrayList<PersonalProperty>();
        msaeList.add(pprop);
        ppropList.setMedicalSuppliesAndEquipment(msaeList);

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

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;
        myInlandMarine.watercrafts_PL_IM = boatTypes;
        myInlandMarine.farmEquipment = allFarmEquip;
        myInlandMarine.personalProperty_PL_IM = ppropList.getAllPersonalPropertyAsList();
        myInlandMarine.livestock_PL_IM = allLivestock.getAllLivestockAsList();

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.InlandMarineLinePL, LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Anderson", "Thomas")
                .build(policyType);

        return myPolicyObjPL;
    }
}

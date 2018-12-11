package regression.r2.noclock.policycenter.submission_auto.subgroup2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import services.broker.objects.vin.requestresponse.ValidateVINResponse;
import services.broker.services.vin.ServiceVINValidation;
import repository.driverConfiguration.Config;
import repository.gw.enums.CommuteType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.Vehicle.Comprehensive_CollisionDeductible;
import repository.gw.enums.Vehicle.DriverVehicleUsePL;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_ExclusionsConditions;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.generic.GenericWorkorderSquirePACoverages;
import repository.pc.workorders.generic.GenericWorkorderVehicles;
import repository.pc.workorders.generic.GenericWorkorderVehicles_CoverageDetails;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import persistence.globaldatarepo.helpers.VINHelper;

/**
 * @throws Exception
 * @Author nvadlamudi
 * @Requirement
 * @RequirementsLink <a href="http://rally1.rallydev.com/#/33274298124d/detail/
 * userstory/51709646306</a>
 * @Description
 * @DATE Mar 8, 2016
 */
@QuarantineClass
public class TestSquireAutoVehicleCoverages extends BaseTest {

    private GeneratePolicy myPolicyObjPL;
    private Underwriters underwriterInfo;
    private WebDriver driver;

    @BeforeMethod
    public void beforeMethod() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
    }


    @Test
    public void validatePACoveragesAndVehicles() throws Exception {

        // create a policy
        myPolicyObjPL = createPLAutoPolicy(GeneratePolicyType.FullApp);

        // login with Underwriter, open the account
        underwriterInfo = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchSubmission(underwriterInfo.getUnderwriterUserName(), underwriterInfo.getUnderwriterPassword(), myPolicyObjPL.accountNumber);
        // select Full App
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();

        // Add new Policy Member, new driver, new Vehicle
        addhouseholdMembersDriversVehicles();

        // Update Exclusions and Conditions Coverages
        ArrayList<Vehicle> vehicle = myPolicyObjPL.squire.squirePA.getVehicleList();
        addPAExclusionsConditions(vehicle);

        // Validate Vehicle Details
        List<Contact> driversList = myPolicyObjPL.squire.squirePA.getDriversList();
        addPAVehicleCoveragesAndValidateDetails(vehicle, driversList);


        //US6709 : PL - PA - changes to Vehicle screen to ensure the flow is "similar" in CL and PL.
        validateVehicleScreenChanges();

    }

    private void validateVehicleScreenChanges() throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPAVehicles();

        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        GenericWorkorderVehicles_CoverageDetails vehicleCoverages = new GenericWorkorderVehicles_CoverageDetails(driver);
        vehiclePage.createVehicleManually();
        vehiclePage.createGenericVehicle(VehicleTypePL.PassengerPickup);
        vehiclePage.setNOVIN(true);
        vehiclePage.setFactoryCostNew(10000);
        vehiclePage.sendArbitraryKeys(Keys.TAB);
        vehiclePage.setStatedValueWithSpecialEquipment(10000.0, "Testing Description");
        vehiclePage.selectGaragedAtZip("ID");
        vehiclePage.setNoDriverAssigned(true);
        vehiclePage.setMake("Honda");
        vehiclePage.setModel("Accord");
        Date newDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, 3);
        vehiclePage.setModelYear(DateUtils.dateFormatAsInt("yyyy", newDate));
        vehiclePage.clickOK();
        boolean testFailed = false;
        String errorMessage = "";
        String valMessage = "Model Year : Vehicle year cannot be greater than";
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        if (!guidewireHelpers.errorMessagesExist() && (!guidewireHelpers.getFirstErrorMessage().contains(valMessage))) {
            testFailed = true;
            errorMessage = errorMessage + "Expected page validation : '" + valMessage + "' is not displayed. /n";
        }

        Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        vehiclePage.setModelYear(DateUtils.dateFormatAsInt("yyyy", currentDate));
        vehiclePage.clickOK();

        vehiclePage.createVehicleManually();
        vehiclePage.createGenericVehicle(VehicleTypePL.ShowCar);
        vehiclePage.sendArbitraryKeys(Keys.TAB);

        vehiclePage.setNOVIN(true);
        vehiclePage.setMake("Honda");
        vehiclePage.setModel("Accord");
        vehiclePage.setModelYear(DateUtils.dateFormatAsInt("yyyy", currentDate) + 2);
        vehiclePage.selectGaragedAtZip("ID");
        vehiclePage.setStatedValue(20000);
        vehiclePage.setOdometer(0);
        vehicleCoverages.selectVehicleCoverageDetailsTab();
        vehicleCoverages.setComprehensive(true);
        vehicleCoverages.setCollision(true);
        vehicleCoverages.clickOK();

        if (testFailed)
            Assert.fail(errorMessage);

    }

    private void addhouseholdMembersDriversVehicles() throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuHouseholdMembers();
        String firstName = "Williams" + StringsUtils.generateRandomNumberDigits(6);
        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
        household.clickSearch();
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
        addressBook.searchAddressBookByFirstLastName(true, firstName, "Angela", null, null, null, null, CreateNew.Create_New_Always);
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        householdMember.setDateOfBirth(DateUtils.convertStringtoDate("01/01/1980", "MM/dd/YYYY"));
        householdMember.selectRelationshipToInsured(RelationshipToInsured.SignificantOther);
        householdMember.setNewPolicyMemberAlternateID(StringsUtils.generateRandomNumberDigits(12));
        householdMember.selectNotNewAddressListingIfNotExist(myPolicyObjPL.pniContact.getAddress());

        householdMember.sendArbitraryKeys(Keys.TAB);
        householdMember.clickRelatedContactsTab();
        householdMember.clickOK();

        // Add driver
        sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        paDrivers.addExistingDriver("Angela");
        paDrivers.selectMaritalStatus(MaritalStatus.Married);
        paDrivers.selectGender(Gender.Female);
        paDrivers.setOccupation("Software");
        paDrivers.setLicenseNumber("AB123456C");
        State state = State.Idaho;
        paDrivers.selectLicenseState(state);
        paDrivers.selectCommuteType(CommuteType.WorkFromHome);
        paDrivers.setPhysicalImpairmentOrEpilepsy(false);
        paDrivers.selectDriverHaveCurrentInsurance(true);
        paDrivers.setCurrentInsuranceCompanyPolicyNumber("Farm Bureau Policy #: 1B124576");
        paDrivers.clickOk();

        ArrayList<Contact> driversList = new ArrayList<>();
        Contact person = new Contact(firstName, "Angela", Gender.Female, DateUtils.convertStringtoDate("01/01/1980", "MM/dd/YYYY"));
        driversList.add(person);
        myPolicyObjPL.squire.squirePA.setDriversList(driversList);
        //***************************************************************************************//
        //NOTE : This is a temp fix for Discard UnSaved error, snippet will be removed once fixed.
        sideMenu.clickSideMenuPACoverages();
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        if (guidewireHelpers.errorMessagesExist() && (guidewireHelpers.getErrorMessages().toString().contains("Discard Unsaved Change"))) {
            guidewireHelpers.clickDiscardUnsavedChangesLink();
            sideMenu.clickSideMenuPADrivers();
            paDrivers.addExistingDriver("Angela");
            paDrivers.selectMaritalStatus(MaritalStatus.Married);
            paDrivers.selectGender(Gender.Female);
            paDrivers.setOccupation("Software");
            paDrivers.setLicenseNumber("AB123456C");
            paDrivers.selectLicenseState(state);
            paDrivers.selectCommuteType(CommuteType.WorkFromHome);
            paDrivers.setPhysicalImpairmentOrEpilepsy(false);
            paDrivers.selectDriverHaveCurrentInsurance(true);
            paDrivers.setCurrentInsuranceCompanyPolicyNumber("Farm Bureau Policy #: 1B124576");

            paDrivers.clickOk();
        }
        //***************************************************************************************//
        // Add Vehicle
        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        GenericWorkorderVehicles_CoverageDetails vehiclecoverages = new GenericWorkorderVehicles_CoverageDetails(driver);
        vehiclePage.createVehicleManually();
        Vin newvin = vehiclePage.createGenericVehicle(VehicleTypePL.PrivatePassenger);

        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle newVeh = new Vehicle();
        newVeh.setVin(newvin.getVin());
        newVeh.setEmergencyRoadside(true);
        vehicleList.add(newVeh);

        myPolicyObjPL.squire.squirePA.setVehicleList(vehicleList);

//		IGenericWorkorderVehiclePopup vehiclePopup = GenericWorkorderFactory.getGenericWorkorderVehiclePopup();
        vehiclePage.vinAutoFill(myPolicyObjPL.squire.squirePA.getVehicleList().get(0));
        vehiclePage.addDriver(person, DriverVehicleUsePL.Principal);
        vehiclePage.selectGaragedAtZip("ID");
        vehiclePage.setFactoryCostNew(10000);
        // Add Comprehensive coverage
        vehiclecoverages.selectVehicleCoverageDetailsTab();

        vehiclecoverages.setComprehensive(true);
        vehiclecoverages.clickOK();
    }

    private void addPAVehicleCoveragesAndValidateDetails(ArrayList<Vehicle> vehiclesList, List<Contact> driversList) throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPAVehicles();
        boolean vehicleFound = false;
        boolean errorFound = false;
        String errorMessage = "";

        GenericWorkorderVehicles vehicleTab = new GenericWorkorderVehicles(driver);
        System.out.println("Vehicle Details : " + vehiclesList.size());
        System.out.println("Drivers list " + driversList.size());
        System.out.println("Current table rows " + vehicleTab.getVehicleTableRowCount());

        for (Contact driver : driversList) {
            if ((vehicleTab.getVehicleDriverNames("Driver").toString().contains(driver.getLastName())) && (vehicleTab.getVehicleDriverNames("Driver").toString().contains(driver.getFirstName()))) {
                System.out.println("Expected Driver Name : " + driver.getFirstName() + " " + driver.getLastName() + " is displayed.");
            } else {
                errorFound = true;
                errorMessage = errorMessage + "Expected Driver Name : " + driver.getFirstName() + " " + driver.getLastName() + " is not displayed.";
            }
        }

        if (vehicleTab.getVehicleTableCellByColumnName(2, "Comp").contains("500")) {
            System.out.println("Expected Comprehensive Coverage limit : 500 is displayed.");
        } else {
            errorFound = true;
            errorMessage = errorMessage + "Expected Comprehensive Coverage is not displayed.";
        }

        for (Vehicle currentVehicle : vehiclesList) {
            vehicleFound = false;
            for (int currentRow = 1; currentRow <= vehicleTab.getVehicleTableRowCount(); currentRow++) {

                if (currentVehicle.getVin().contains(vehicleTab.getVehicleTableCellByColumnName(currentRow, "VIN"))) {
                    vehicleFound = true;

                    boolean printRequestXMLToConsole = true;
                    boolean printResponseXMLToConsole = true;
                    ServiceVINValidation testService = new ServiceVINValidation();
                    ValidateVINResponse testResponse = testService.validateVIN2(
                            testService.setUpTestValidateVINRequest(currentVehicle.getVin()),
                            new GuidewireHelpers(driver).getMessageBrokerConnDetails(), printRequestXMLToConsole, printResponseXMLToConsole);

                    if (vehicleTab.getVehicleTableCellByColumnName(currentRow, "Description").contains(testResponse.getYear() + " " + testResponse.getMake() + " " + testResponse.getModel())) {
                        System.out.println("Expected Vehicle Make, Model, Year : " + vehicleTab.getVehicleTableCellByColumnName(currentRow, "Description") + " is displayed.");
                    } else {
                        errorFound = true;
                        errorMessage = errorMessage + "Expected Vehicle Make, Model, Year : " + testResponse.getYear() + " " + testResponse.getMake() + " " + testResponse.getModel() + " is not displayed.";
                    }
                }
                if (vehicleFound) {
                    break;
                }
            }
        }

        if (!vehicleFound) {
            errorFound = true;
            errorMessage = errorMessage + "Expected Vehicle VIN not found!";
        }

        if (errorFound) {
            Assert.fail(driver.getCurrentUrl() + errorMessage);
        }

    }

    private void addPAExclusionsConditions(ArrayList<Vehicle> vehicleList) throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPACoverages();

        GenericWorkorderSquirePACoverages paCoverages = new GenericWorkorderSquirePACoverages(driver);
        paCoverages.clickCoverageExclusionsTab();

        GenericWorkorderSquireAutoCoverages_ExclusionsConditions paExclusions = new GenericWorkorderSquireAutoCoverages_ExclusionsConditions(driver);
        paExclusions.addAdditionalInsuredEndorsement("ABC Test Company");
        paExclusions.addModificationOfInsuredVehicleDefinitionEndorsement("BMW", "Beemer", "1B45678645456", "1999");
    }

    private GeneratePolicy createPLAutoPolicy(GeneratePolicyType policyType) throws Exception {

        // Coverages
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK, true, UninsuredLimit.Fifty, true, UnderinsuredLimit.Fifty);
        coverages.setAccidentalDeath(true);
        coverages.setDriveOtherCar(false);

        // driver
        ArrayList<Contact> driversList = new ArrayList<Contact>();
        Contact person = new Contact();
        driversList.add(person);

        // Vehicle
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle toAdd = new Vehicle();
        Vin vin = VINHelper.getRandomVIN();
        toAdd.setVin(vin.getVin());
        toAdd.setEmergencyRoadside(true);
        toAdd.setAdditionalLivingExpense(true);
        toAdd.setComprehensiveDeductible(Comprehensive_CollisionDeductible.FiveHundred500);
        vehicleList.add(toAdd);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);
        squirePersonalAuto.setVehicleList(vehicleList);
        squirePersonalAuto.setDriversList(driversList);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Test", "Vehcovs")
                .build(policyType);

        return myPolicyObjPL;
    }

}

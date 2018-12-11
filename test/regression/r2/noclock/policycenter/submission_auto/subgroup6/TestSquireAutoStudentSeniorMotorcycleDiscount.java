package regression.r2.noclock.policycenter.submission_auto.subgroup6;

import java.util.ArrayList;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.enums.State;

import repository.driverConfiguration.Config;
import repository.gw.enums.CommuteType;
import repository.gw.enums.CreateNew;
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
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.enums.Vehicle.Comprehensive_CollisionDeductible;
import repository.gw.enums.Vehicle.DriverVehicleUsePL;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.FullUnderWriterIssues;
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
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.VINHelper;

public class TestSquireAutoStudentSeniorMotorcycleDiscount extends BaseTest {
    public GeneratePolicy myPolicyObjPL = null;

    private WebDriver driver;

    @BeforeMethod
    public void beforeMethod() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
    }


    @Test(dependsOnMethods = "createAutoPolicy")
    private void validateStudentSeniorMotorCycleDiscount() throws Exception {
        Agents agent = myPolicyObjPL.agentInfo;
        boolean testFailed = false;
        String errorMessage = "";
        new Login(driver).loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), myPolicyObjPL.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();

        // adding policy members with different agents
        Contact student = new Contact("Test" + StringsUtils.generateRandomNumberDigits(6), "Student", Gender.Female, DateUtils.convertStringtoDate("01/01/1999", "MM/dd/yyyy"));
        Contact motorist = new Contact("Test" + StringsUtils.generateRandomNumberDigits(6), "Motorist", Gender.Male, DateUtils.convertStringtoDate("01/01/1979", "MM/dd/yyyy"));
        addNewPolicyMember(myPolicyObjPL.basicSearch, student, RelationshipToInsured.Insured);
        addNewPolicyMember(myPolicyObjPL.basicSearch, motorist, RelationshipToInsured.SignificantOther);

        // adding drivers list
        AddingNewDriver(student.getLastName(), student.getGender(), "FullTime Student", "ABCD1234C");
        AddingNewDriver(motorist.getLastName(), motorist.getGender(), "Relax", "CB54553D");
        SideMenuPC sideMenu = new SideMenuPC(driver);

        //***************************************************************************************//
        //NOTE : This is a temp fix for Discard UnSaved error, snippet will be removed once fixed.
        sideMenu.clickSideMenuPACoverages();
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        if (guidewireHelpers.errorMessagesExist() && (guidewireHelpers.getErrorMessages().toString().contains("Discard Unsaved Change"))) {
            guidewireHelpers.clickDiscardUnsavedChangesLink();
            AddingNewDriver(student.getLastName(), student.getGender(), "FullTime Student", "ABCD1234C");
            AddingNewDriver(motorist.getLastName(), motorist.getGender(), "Relax", "CB54553D");
            sideMenu.clickSideMenuPACoverages();
        }
        //***************************************************************************************//

        // Adding Vehicle for Senior discount
        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        vehiclePage.editVehicleByVehicleNumber(1);
        vehiclePage.setSeniorDiscountRadio(true);
        vehiclePage.clickOK();

        // Adding Vehicles for Student Discount
        vehiclePage.createVehicleManually();
        vehiclePage.createGenericVehicle(VehicleTypePL.PassengerPickup);
        vehiclePage.setModelYear(2014);
        vehiclePage.setMake("Scion");
        vehiclePage.setModel("FR-S");
        vehiclePage.addDriver(student, DriverVehicleUsePL.Principal);
        vehiclePage.setGoodStudentDiscountRadio(true);
        vehiclePage.selectGaragedAtZip(myPolicyObjPL.squire.squirePA.getVehicleList().get(0).getGaragedAt().getCity());
        vehiclePage.selectDriverVehicleUse(DriverVehicleUsePL.Principal);
        vehiclePage.clickOK();

        // Adding Vehicle for motorist Discount
        vehiclePage.createVehicleManually();
        Vin vin = VINHelper.getRandomVIN();
        vehiclePage.createGenericVehicle(VehicleTypePL.Motorcycle);
        vehiclePage.setVIN(vin.getVin());
        vehiclePage.vinAutoFill(myPolicyObjPL.squire.squirePA.getVehicleList().get(0));
        vehiclePage.addDriver(motorist, DriverVehicleUsePL.Principal);
        vehiclePage.setMotorCyclesStarDiscountRadio(true);
        vehiclePage.selectGaragedAtZip(myPolicyObjPL.squire.squirePA.getVehicleList().get(0).getGaragedAt().getCity());
        vehiclePage.clickOK();

        // Checking no discount for new vehicle
        vehiclePage.createVehicleManually();
        Vin vin1 = VINHelper.getRandomVIN();
        vehiclePage.createGenericVehicle(VehicleTypePL.PrivatePassenger);
        vehiclePage.setVIN(vin1.getVin());
        vehiclePage.vinAutoFill(myPolicyObjPL.squire.squirePA.getVehicleList().get(0));
        vehiclePage.addDriver(motorist, DriverVehicleUsePL.Principal);
        testFailed = vehiclePage.checkNoDiscountOptionExists();
        vehiclePage.selectGaragedAtZip(myPolicyObjPL.squire.squirePA.getVehicleList().get(0).getGaragedAt().getCity());
        vehiclePage.clickOK();

        if (testFailed)
            errorMessage = "Discount Exists for unexpected Vehicle \n";

        // Validating the risk analysis page
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();
        quote.clickQuote();
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);

        String[] expectedUWMessages = {"55 and Alive Discount", "Good Student Discount", "Motorcycle Discount and New Business"};

        FullUnderWriterIssues uwIssues = riskAnalysis.getUnderwriterIssues();
        for (String uwIssue : expectedUWMessages) {
            if (uwIssues.isInList(uwIssue).equals(UnderwriterIssueType.NONE)) {
                testFailed = true;
                errorMessage = errorMessage + "Expected error Bind Issue : " + uwIssue + " is not displayed.";
            }
        }

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + errorMessage);
        }
    }

    private void AddingNewDriver(String lastName, Gender gender, String occupation, String license) throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        paDrivers.addExistingDriver(lastName);
        paDrivers.setPhysicalImpairmentOrEpilepsy(false);
        paDrivers.selectMaritalStatus(MaritalStatus.Single);
        paDrivers.selectGender(gender);
        paDrivers.setOccupation(occupation);
        paDrivers.setLicenseNumber(license);
        State state = State.Idaho;
        paDrivers.selectLicenseState(state);
        paDrivers.selectCommuteType(CommuteType.WorkFromHome);
        paDrivers.selectDriverHaveCurrentInsurance(true);
        paDrivers.setCurrentInsuranceCompanyPolicyNumber("Farm Bureau Policy #: 1B124576");
        paDrivers.clickOk();
    }

    private void addNewPolicyMember(boolean basicSearch, Contact person, RelationshipToInsured relationship) throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuHouseholdMembers();

        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
        household.clickSearch();
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
        addressBook.searchAddressBookByFirstLastName(basicSearch, person.getFirstName(), person.getLastName(), null, null, null, null, CreateNew.Create_New_Always);
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        householdMember.setDateOfBirth(person.getDob());
        householdMember.selectRelationshipToInsured(relationship);
        householdMember.setNewPolicyMemberAlternateID(StringsUtils.generateRandomNumberDigits(12));
        householdMember.selectNotNewAddressListingIfNotExist(myPolicyObjPL.pniContact.getAddress());
        householdMember.sendArbitraryKeys(Keys.TAB);
        householdMember.clickRelatedContactsTab();
        householdMember.clickOK();

    }

    @Test
    private void createAutoPolicy() throws Exception {
        // Coverages
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK, true, UninsuredLimit.Fifty, true, UnderinsuredLimit.Fifty);
        coverages.setAccidentalDeath(true);

        // driver
        ArrayList<Contact> driversList = new ArrayList<>();
        Contact person1 = new Contact("Test-" + StringsUtils.generateRandomNumberDigits(4), "Senior", Gender.Male, DateUtils.convertStringtoDate("01/01/1960", "MM/dd/yyyy"));
        driversList.add(person1);

        // Vehicle
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle toAdd = new Vehicle();
        toAdd.setVehicleTypePL(VehicleTypePL.PrivatePassenger);
        toAdd.setEmergencyRoadside(true);
        toAdd.setAdditionalLivingExpense(true);
        toAdd.setDriverPL(person1);
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
                .withInsFirstLastName("Auto", "discount")
                .build(GeneratePolicyType.FullApp);

    }
}

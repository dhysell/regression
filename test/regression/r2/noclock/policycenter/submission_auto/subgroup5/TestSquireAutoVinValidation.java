package regression.r2.noclock.policycenter.submission_auto.subgroup5;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import services.broker.objects.vin.requestresponse.ValidateVINResponse;
import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.errorhandling.ErrorHandlingHelpers;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.VINHelper;

@QuarantineClass
public class TestSquireAutoVinValidation extends BaseTest {

    private String firstName = "Vin";
    private String lastName = "Validation";
    private String agentUsername;
    private String agentPassword;
    private String accountNumber;
    private GeneratePolicy myPolicyObj;

    private WebDriver driver;

    @Test
    public void testSquireAuto_QuickQuote() throws Exception {
        // driver
        ArrayList<Contact> driversList = new ArrayList<Contact>();
        Contact person = new Contact();
        driversList.add(person);

        // vehicle
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle vehicle = new Vehicle(VehicleTypePL.PrivatePassenger, "make new vin", 2003, "Oldie", "But Goodie");
        vehicleList.add(vehicle);

        // line auto coverages
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow, MedicalLimit.TenK);
        coverages.setUnderinsured(false);

        // whole auto line
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto(driversList, vehicleList, coverages);

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName(firstName, lastName)
                .build(GeneratePolicyType.QuickQuote);

        Agents agent = myPolicyObj.agentInfo;
        agentUsername = agent.getAgentUserName();
        agentPassword = agent.getAgentPassword();
        accountNumber = myPolicyObj.accountNumber;
    }

    @Test(dependsOnMethods = {"testSquireAuto_QuickQuote"})
    public void testSquireAuto_VinValidation() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);


        new Login(driver).loginAndSearchJob(agentUsername, agentPassword, accountNumber);

        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC menu = new SideMenuPC(driver);
        menu.clickSideMenuPAVehicles();
        //DE4332 Null Pointer Exception under Edit Vehicle Page
        //Edit Vehicle VIN Number
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        vehiclePage.clickLinkInVehicleTable("Edit");
        Vin vin = null;
        vin = VINHelper.getRandomVIN();
        vehiclePage.setVIN(vin.getVin());
        vehiclePage.vinAutoFill(myPolicyObj.squire.squirePA.getVehicleList().get(0));
        vehiclePage.clickOK();

        boolean testFailed = false;
        String failureString = "";

        if (new ErrorHandlingHelpers(driver).errorExists()) {
            testFailed = true;
            failureString = failureString + "NullPointerException: null  message should not exist when user Edits Vin and clicks OK button";
        }
        // add new vehicle
        vehiclePage.createVehicleManually();

        // try getting a vin number
        //		IGenericWorkorderVehiclePopup vehiclePopup = GenericWorkorderFactory.getGenericWorkorderVehiclePopup();
        String make;
        String model;
        String year;

        vin = VINHelper.getRandomVIN();
        ValidateVINResponse vinResponse = vehiclePage.setVINAndAlsoValidateDirectlyFromBrokerForComparison(vin.getVin());

        if (vinResponse == null) {
            Assert.fail("Returned VIN was NULL");
        }

        // scrape screen and compare to the response object.
        make = vehiclePage.getMake();
        model = vehiclePage.getModel();
        year = vehiclePage.getYear();

        String responseMake = "";
        if (vinResponse.getMake() != null) {
            responseMake = vinResponse.getMake();
        }
        String responseYear = "";
        if (vinResponse.getYear() != null) {
            responseYear = vinResponse.getYear().toString();
        }
        String responseModel = "";
        if (vinResponse.getModel() != null) {
            responseModel = vinResponse.getModel();
        }

        //DE4032 - VIN validation for make, model and year fields empty on screen
        if (responseMake.equals("") || responseYear.equals("") || responseModel.equals(""))
            testFailed = true;
        failureString = failureString + "The make, model and year are empty on screen.";

        if (!make.equals(responseMake) && (vinResponse.getMake() != null || !vinResponse.getMake().equals(""))) {
            testFailed = true;
            failureString = failureString + "The make on screen (" + make + ") is not the same as what was recieved from the broker (" + responseMake + ").";
        }
        if (!model.equalsIgnoreCase(responseModel) && (vinResponse.getModel() != null || !vinResponse.getModel().equals(""))) {
            testFailed = true;
            failureString = failureString + "The model on screen (" + model + ") is not the same as what was recieved from the broker (" + responseModel + ").";
        }
        if (!year.equalsIgnoreCase(responseYear) && (vinResponse.getYear() != null || !String.valueOf(vinResponse.getYear()).equals(""))) {
            testFailed = true;
            failureString = failureString + "The year on screen (" + year + ") is not the same as what was recieved from the broker (" + responseYear + ").";
        }

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + failureString);
        }

        //DE4032 - Squire Auto Only Policy - When VIN is entered and hit on Tab an error message is displayed
        vehiclePage.clearVIN();
        vehiclePage.setFactoryCostNew(10000);

        String actualValidationMessage = vehiclePage.getVehiclePopupValidationMessage();
        String expectedValidationMessage = "VIN Validation failed, please check that it was entered properly.";

        if (!expectedValidationMessage.equals(actualValidationMessage))
            Assert.fail(vehiclePage.getVehiclePopupValidationMessage() + " validation message is not displayed ");
    }

}

package regression.r2.noclock.policycenter.submission_auto.subgroup6;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.Vehicle.DriverVehicleUsePL;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import persistence.globaldatarepo.entities.Agents;

public class TestSquireAutoShowCarOccasionalDriver extends BaseTest {

    private SquirePersonalAuto squirePersonalAuto;
    private String firstName = "Occasional";
    private String lastName = "Driver";
    private Contact person2;
    private String agentUsername;
    private String agentPassword;
    private String accountNumber;

    private WebDriver driver;

    @Test
    public void testSquireAuto_FullApp() throws Exception {

        // driver
        ArrayList<Contact> driversList = new ArrayList<Contact>();
        Contact person = new Contact();
        person2 = new Contact();
        driversList.add(person);
        driversList.add(person2);

        // vehicle
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle vehicle = new Vehicle(VehicleTypePL.PrivatePassenger, "make new vin", 2003, "Oldie", "But Goodie");
        vehicleList.add(vehicle);

        // line auto coverages
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow, MedicalLimit.TenK);
        coverages.setUnderinsured(false);

        // whole auto line
        squirePersonalAuto = new SquirePersonalAuto(driversList, vehicleList, coverages);

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        GeneratePolicy myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName(firstName, lastName)
                .build(GeneratePolicyType.FullApp);

        Agents agent = myPolicyObj.agentInfo;
        agentUsername = agent.getAgentUserName();
        agentPassword = agent.getAgentPassword();
        accountNumber = myPolicyObj.accountNumber;
    }

    /**
     * @throws Exception
     * @Author drichards
     * @Requirement DE3106
     * @RequirementsLink <a href=
     * "https://rally1.rallydev.com/#/33274298124d/detail/defect/48011627874">
     * DE3106</a>
     * @Description When a car that has an occasional driver with no reason is
     * changed into a show car, the validation for the reason
     * should not be triggered.
     * @DATE Feb 1, 2016
     */
    @Test(dependsOnMethods = {"testSquireAuto_FullApp"})
    public void testSquireAuto_ShowCarOccasionalDriver() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchJob(agentUsername, agentPassword, accountNumber);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPAVehicles();

        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        vehiclePage.createVehicleManually();
        vehiclePage.createGenericVehicle(VehicleTypePL.PrivatePassenger);

//		IGenericWorkorderVehiclePopup vehiclePopup = GenericWorkorderFactory.getGenericWorkorderVehiclePopup();
        vehiclePage.addDriver(person2, DriverVehicleUsePL.Occasional);
        vehiclePage.selectVehicleType(VehicleTypePL.ShowCar);
        vehiclePage.setStatedValue(123654.52);
        vehiclePage.setOdometer(3);
        vehiclePage.clickOK();

        if (guidewireHelpers.errorMessagesExist()) {
            String topBannerStuff = guidewireHelpers.getFirstErrorMessage();
            if (topBannerStuff.contains("Occasional Driver Reason")) {
                Assert.fail("The validation for the Occasional Driver Reason should not appear for a Show Car.");
            }
        }
    }

}

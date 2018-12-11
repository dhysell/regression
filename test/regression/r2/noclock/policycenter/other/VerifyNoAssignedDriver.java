package regression.r2.noclock.policycenter.other;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.Vehicle.CommutingMiles;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.errorhandling.ErrorHandling;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.Vehicle;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_SelfReportingIncidents;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;

@QuarantineClass
public class VerifyNoAssignedDriver extends BaseTest {

    private GeneratePolicy policy;

    private Contact firstDriver;

    private Contact secondDriver;
    private WebDriver driver;

    @Test
    public void createSquireAutoQuickQuote() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages();

        firstDriver = new Contact();
        secondDriver = new Contact();

        ArrayList<Contact> drivers = new ArrayList<Contact>();

        drivers.add(firstDriver);
        drivers.add(secondDriver);

        ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleTypePL(VehicleTypePL.FarmTruck);
        vehicles.add(vehicle);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);
        squirePersonalAuto.setVehicleList(vehicles);
        squirePersonalAuto.setDriversList(drivers);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        policy = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Jon", "Boyer")
                .build(GeneratePolicyType.QuickQuote);

    }

    @Test(enabled = true, dependsOnMethods = {"createSquireAutoQuickQuote"})
    public void testAddRemoveDriver() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchJob(policy.agentInfo.getAgentUserName(), policy.agentInfo.getAgentPassword(),
                policy.accountNumber);
        GenericWorkorderPolicyInfo policyInfoPage = new GenericWorkorderPolicyInfo(driver);
        policyInfoPage.clickEditPolicyTransaction();

        SideMenuPC sideBar = new SideMenuPC(driver);
        sideBar.clickSideMenuPAVehicles();

        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);

        vehiclePage.createVehicleManually();
        vehiclePage.createGenericVehicle(VehicleTypePL.Motorcycle);
        vehiclePage.addDriver(firstDriver);
//    	vehiclePage.selectGargedAtPL(policy.insPrimaryAddress);

        if (!vehiclePage.checkMotorCyclesDiscountOptionExists()) {
            Assert.fail(driver.getCurrentUrl() + policy.accountNumber + "If driver is added for Motor Cycle, then Motor Cycle Discount option should be displayed.");
        }

        vehiclePage.removeAssignedDriver();

        if (vehiclePage.checkMotorCyclesDiscountOptionExists()) {
            Assert.fail(driver.getCurrentUrl() + policy.accountNumber +
                    "If driver is removed for Motor Cycle, then Motor Cycle Discount option should not be displayed.");
        }

        vehiclePage.setGaragedAt(policy.pniContact.getAddress());
        vehiclePage.setNoDriverAssigned(true);
        vehiclePage.clickOK();
        boolean msgFound = false;
        ErrorHandling msg = new ErrorHandling(driver);
        List<WebElement> msgs = msg.text_ErrorHandlingErrorBannerMessages();
        for (WebElement errorMsg : msgs) {
            if (errorMsg.getText()
                    .contains("The vehicle must either have a driver assigned or have unassigned driver checked")) {
                msgFound = true;
            }
        }
        if (msgFound) {
            Assert.fail(driver.getCurrentUrl() + policy.accountNumber +
                    "The No Driver Assigned checkbox must be checked or the vehicle must have a driver assigned.");
        }
        vehiclePage = new GenericWorkorderVehicles_Details(driver);

        vehiclePage.editVehicleByVehicleNumber(2);
        vehiclePage.setNoDriverAssigned(true);
        boolean isDriverAdded = true;
        try {
            vehiclePage = new GenericWorkorderVehicles_Details(driver);
            vehiclePage.addDriver(secondDriver);
        } catch (Exception e) {
            isDriverAdded = false;
        }
        if (isDriverAdded) {
            Assert.fail(driver.getCurrentUrl() + policy.accountNumber +
                    "If No Driver Assigned checkbox is true, the user should not be able to assign driver.");
        }

    }

    @Test(enabled = true, dependsOnMethods = {"testAddRemoveDriver"})
    public void testSRPIncidents() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchJob(policy.agentInfo.getAgentUserName(), policy.agentInfo.getAgentPassword(),
                policy.accountNumber);

        SideMenuPC sideBar = new SideMenuPC(driver);
        sideBar.clickSideMenuPAVehicles();

        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        vehiclePage.createVehicleManually();
        vehiclePage.createGenericVehicle(VehicleTypePL.PassengerPickup);
        vehiclePage.selectCommunity(CommutingMiles.WorkSchool10PlusMiles);
        vehiclePage.addDriver(firstDriver);
        vehiclePage.setGaragedAt(policy.pniContact.getAddress());
        vehiclePage.clickOK();

        sideBar.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_SelfReportingIncidents squireDriver = new GenericWorkorderSquireAutoDrivers_SelfReportingIncidents(driver);
        squireDriver.clickEditButtonInDriverTable(1);
        squireDriver.clickSRPIncidentsTab();
        if (!squireDriver.checkSRPFieldsEditableQQ()) {
            Assert.fail(driver.getCurrentUrl() + policy.accountNumber +
                    "If driver is assigned then, then SRP incidents should be editable.");
        }
        squireDriver.clickOk();
        sideBar.clickSideMenuPAVehicles();
        vehiclePage.createVehicleManually();
        vehiclePage.createGenericVehicle(VehicleTypePL.Motorcycle);
        vehiclePage.addDriver(firstDriver);
        vehiclePage.setGaragedAt(policy.pniContact.getAddress());

        vehiclePage.clickOK();

        sideBar.clickSideMenuPADrivers();
        squireDriver.clickEditButtonInDriverTable(1);
        squireDriver.clickSRPIncidentsTab();

        if (!squireDriver.checkSRPFieldsEditableQQ()) {
            Assert.fail(driver.getCurrentUrl() + policy.accountNumber +
                    "If driver is assigned then, then SRP incidents should be editable.");
        }
        squireDriver.clickOk();

        sideBar.clickSideMenuPAVehicles();
        int srpForPassengerPickup = Integer.parseInt(vehiclePage.getVehicleTableCellByColumnName(2, "SRP"));
        int srpForMotorCycle = Integer.parseInt(vehiclePage.getVehicleTableCellByColumnName(3, "SRP"));

        boolean testPassed = false;
        if (srpForPassengerPickup == srpForMotorCycle) {
//			throw new GuidewirePolicyCenterException(Configuration.getWebDriver().getCurrentUrl(), policy.accountNumber,
//					"If driver is assigned to a private passenger or passenger pickup and is also assigned to a motorcycle "
//					+ "the MVR tickets and driver related SRP incidents will be counted on the private passenger or passenger pickup "
//					+ "and not included on the motor cycle.");
        } else {
            testPassed = true;
        }


    }

    @Test(enabled = true, dependsOnMethods = {"testSRPIncidents"})
    public void testNoDriverAssigned_CommutingMiles() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchJob(policy.agentInfo.getAgentUserName(), policy.agentInfo.getAgentPassword(),
                policy.accountNumber);

        SideMenuPC sideBar = new SideMenuPC(driver);
        sideBar.clickSideMenuPAVehicles();

        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        vehiclePage.createVehicleManually();
        vehiclePage.createGenericVehicle(VehicleTypePL.PassengerPickup);

        vehiclePage.selectCommunity(CommutingMiles.WorkSchool10PlusMiles);

        vehiclePage.setNoDriverAssigned(true);

        CommutingMiles miles = vehiclePage.getCommutingMiles();

        if (miles != null && !miles.equals(CommutingMiles.Pleasure1To2Miles)) {
            Assert.fail(driver.getCurrentUrl() + policy.accountNumber +
                    "If No Driver Assigned checkbox is true, the the Commuting miles should be set to Pleasure 1 to 2 Miles.");
        }

        if (!vehiclePage.checkCommunityMilesUneditable()) {
            Assert.fail(driver.getCurrentUrl() + policy.accountNumber +
                    "If No Driver Assigned checkbox is true, the the Commuting miles should be uneditable.");

        }

    }

}

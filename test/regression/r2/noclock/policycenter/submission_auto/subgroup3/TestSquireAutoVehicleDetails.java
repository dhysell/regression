package regression.r2.noclock.policycenter.submission_auto.subgroup3;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.Vehicle.Comprehensive_CollisionDeductible;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.errorhandling.ErrorHandling;
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
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderVehicles_CoverageDetails;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import persistence.globaldatarepo.entities.Vin;

public class TestSquireAutoVehicleDetails extends BaseTest {

    private GeneratePolicy plPolicyObj;
    private Contact driver1;
    private Contact driver2;

    private WebDriver driver;

    @Test
    public void testSquireAuto_QuickQuote() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK);

        ArrayList<Contact> driversList = new ArrayList<Contact>();
        driver1 = new Contact();
        driver2 = new Contact();
        driversList.add(driver1);
        driversList.add(driver2);

        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle toAdd = new Vehicle();
        toAdd.setVehicleTypePL(VehicleTypePL.LocalService1Ton);
        toAdd.setVin("make new vin");
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

        this.plPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Bob", "Saget")
                .build(GeneratePolicyType.FullApp);

    }

    /**
     * @throws Exception
     * @Author Steve Broderick
     * @Requirement Test US5941.
     * @RequirementsLink <a href=
     * "http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/PersonalAuto/PC8%20-%20PA%20-%20Submission%20-%20Vehicle%20Information.xlsx"
     * >PA - Submission - Vehicle Information</a>
     * @Description Added this method to test US5941. Vehicle information should
     * be kept when vehicle type changes.
     * @DATE 3/14/2016
     */

    @Test(enabled = true, dependsOnMethods = {"testSquireAuto_QuickQuote"})
    public void testSquireAuto_MaintainVehicleDetails() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchJob(plPolicyObj.agentInfo.getAgentUserName(), plPolicyObj.agentInfo.getAgentPassword(), plPolicyObj.accountNumber);
        GenericWorkorderPolicyInfo policyInfoPage = new GenericWorkorderPolicyInfo(driver);
        policyInfoPage.clickEditPolicyTransaction();

        SideMenuPC sideBar = new SideMenuPC(driver);
        sideBar.clickSideMenuPAVehicles();

        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        vehiclePage.createVehicleManually();
        Vin vin = vehiclePage.createGenericVehicle(VehicleTypePL.PrivatePassenger);
        vehiclePage.addDriver(driver1);
        vehiclePage.selectGaragedAt(plPolicyObj.squire.squirePA.getVehicleList().get(0).getGaragedAt());
//		IGenericWorkorderVehiclePopup vehiclePopup = GenericWorkorderFactory.getGenericWorkorderVehiclePopup();
        String passVIN = vehiclePage.getVIN();
        String passYear = vehiclePage.getYear();
        String passMake = vehiclePage.getMake();
        String passModel = vehiclePage.getModel();
        vehiclePage = new GenericWorkorderVehicles_Details(driver);
        String passGaragedAt = vehiclePage.getGaragedAt();
        // Garaged At information should not change when the Vehicle Type is
        // changed
        vehiclePage.selectVehicleType(VehicleTypePL.Motorcycle);
        // Make
        if (!(vehiclePage.getMake().equals(passMake))) {
            System.out.println("UI Make = " + vehiclePage.getMake());
            System.out.println("VIN Make = " + vin.getMake());
            Assert.fail(driver.getCurrentUrl() +
                    plPolicyObj.accountNumber + "When the Vehicle type was changed, the Vehicle make was also changed.");
        }
        // Year
        if (!(vehiclePage.getYear().equals(passYear))) {
            System.out.println("UI Make = " + vehiclePage.getYear());
            System.out.println("VIN Make = " + vin.getYear());
            Assert.fail(driver.getCurrentUrl() +
                    plPolicyObj.accountNumber + "When the Vehicle type was changed, the Vehicle year was also changed.");
        }
        // Model
        if (!(vehiclePage.getModel().contains(passModel))) {
            System.out.println("UI Make = " + vehiclePage.getModel());
            System.out.println("VIN Make = " + vin.getModel());
            Assert.fail(driver.getCurrentUrl() +
                    plPolicyObj.accountNumber +
                    "When the Vehicle type was changed, the Vehicle Model was also changed.");
        }
        // Vin
        if (!(vehiclePage.getVIN().equals(passVIN))) {
            System.out.println("UI Make = " + vehiclePage.getVIN());
            System.out.println("VIN Make = " + vin.getVin());
            Assert.fail(driver.getCurrentUrl() +
                    plPolicyObj.accountNumber + "When the Vehicle type was changed, the Vehicle VIN was also changed.");
        }

        // Garaged At
        vehiclePage = new GenericWorkorderVehicles_Details(driver);
        if (!(vehiclePage.getGaragedAt().contains(passGaragedAt))) {
            System.out.println("UI Make = " + vehiclePage.getGaragedAt());
            System.out.println("VIN Make = " + vehiclePage.getVIN());
            Assert.fail(driver.getCurrentUrl() +
                    plPolicyObj.accountNumber +
                    "When the Vehicle type was changed, the Garaged at Select List was also changed.");
        }
    }

    @Test(enabled = true, dependsOnMethods = {"testSquireAuto_MaintainVehicleDetails"})
    public void testSquireAuto_EditVehicleDetails() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchJob(plPolicyObj.agentInfo.getAgentUserName(), plPolicyObj.agentInfo.getAgentPassword(), plPolicyObj.accountNumber);

        SideMenuPC sideBar = new SideMenuPC(driver);
        sideBar.clickSideMenuPAVehicles();

        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        vehiclePage.createVehicleManually();
        vehiclePage.createGenericVehicle(VehicleTypePL.PassengerPickup);
        vehiclePage.createGenericVehicle(VehicleTypePL.Motorcycle);
        vehiclePage.createGenericVehicle(VehicleTypePL.FarmTruck);
        vehiclePage.createGenericVehicle(VehicleTypePL.SemiTrailer);
        vehiclePage.createGenericVehicle(VehicleTypePL.Trailer);
        vehiclePage.createGenericVehicle(VehicleTypePL.MotorHome);
        vehiclePage.createGenericVehicle(VehicleTypePL.ShowCar);
        vehiclePage.createGenericVehicle(VehicleTypePL.LocalService1Ton);
        vehiclePage.createGenericVehicle(VehicleTypePL.PrivatePassenger);

        vehiclePage.addDriver(driver2);
    }

    /**
     * @throws Exception
     * @Author sbroderick
     * @Requirement US6682 No driver assigned to Vehicle
     * @RequirementsLink <a href=
     * "http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/PersonalAuto/PC8%20-%20PA%20-%20QuoteApplication%20-%20Vehicle%20with%20no%20assigned%20driver.xlsx"
     * >PC8 - PA - QuoteApplication Vehicle with no assigned
     * driver</a>
     * @Description Tests the fields on the vehicles details page
     * @DATE Mar 15, 2016
     */
    @Test(enabled = true, dependsOnMethods = {"testSquireAuto_EditVehicleDetails"})
    public void testSquireAuto_NoDriverAssigned() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchJob(plPolicyObj.agentInfo.getAgentUserName(), plPolicyObj.agentInfo.getAgentPassword(), plPolicyObj.accountNumber);

        SideMenuPC sideBar = new SideMenuPC(driver);
        sideBar.clickSideMenuPAVehicles();

        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        vehiclePage.createVehicleManually();
        vehiclePage.createGenericVehicle(VehicleTypePL.PassengerPickup);
        vehiclePage.selectGaragedAt(plPolicyObj.squire.squirePA.getVehicleList().get(0).getGaragedAt());
        vehiclePage.clickOK();
        boolean found = false;
        ErrorHandling msg = new ErrorHandling(driver);
        List<WebElement> msgs = msg.text_ErrorHandlingErrorBannerMessages();
        for (WebElement errorMsg : msgs) {
            if (errorMsg.getText().contains("The vehicle must either have a driver assigned or have unassigned driver checked")) {
                found = true;
            }
        }
        if (!found) {
            Assert.fail(driver.getCurrentUrl() + plPolicyObj.accountNumber + "The No Driver Assigned checkbox must be checked or the vehicle must have a driver assigned.");
        }
        vehiclePage.setNoDriverAssigned(true);
        found = true;
        try {
            vehiclePage.addDriver(driver1);
        } catch (Exception e) {
            found = false;
        }
        if (found) {
            Assert.fail(driver.getCurrentUrl() + plPolicyObj.accountNumber + "If the No Driver Assigned checkbox is checked, the user should not be able to add an assigned driver.");
        }
    }

    /**
     * @throws Exception
     * @Author sbroderick
     * @Requirement US6682 No driver assigned to Vehicle
     * @RequirementsLink <a href=
     * "http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/PersonalAuto/PC8%20-%20PA%20-%20QuoteApplication%20-%20Vehicle%20with%20no%20assigned%20driver.xlsx"
     * >PC8 - PA - QuoteApplication Vehicle with no assigned
     * driver</a>
     * @Description This method is to test DE 3444.  The Requirement is
     * Expected result.Trailer does not get liability coverage  Liability should not be available and User must add at least one of the Material damage coverage choices ( Comprehensive or Comprehensive and Collision or Fire and theft) on the Coverage detail Tab... (the validation that  the vehicle must have at least one coverage should trigger if no coverage added.)
     * @DATE Mar 15, 2016
     */
    @Test(enabled = true, dependsOnMethods = {"testSquireAuto_NoDriverAssigned"})
    public void testTrailerCoverage() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchJob(plPolicyObj.agentInfo.getAgentUserName(), plPolicyObj.agentInfo.getAgentPassword(), plPolicyObj.accountNumber);

//		GenericWorkorderPolicyInfo policyInfoPage = new GenericWorkorderPolicyInfo(driver);
//		policyInfoPage.clickEditPolicyTransaction();

        SideMenuPC sideBar = new SideMenuPC(driver);
        sideBar.clickSideMenuPAVehicles();

        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        GenericWorkorderVehicles_CoverageDetails vehicleCoverage = new GenericWorkorderVehicles_CoverageDetails(driver);
        vehiclePage.createVehicleManually();
        vehiclePage.createGenericVehicle(VehicleTypePL.Trailer);
        vehiclePage.selectGaragedAt(plPolicyObj.squire.squirePA.getVehicleList().get(0).getGaragedAt());
//		IGenericWorkorderVehiclePopup popUp = GenericWorkorderFactory.getGenericWorkorderVehiclePopup();
        vehicleCoverage.selectVehicleCoverageDetailsTab();
        if (vehicleCoverage.checkIfLiabilityCoverageExists()) {
            Assert.fail(driver.getCurrentUrl() + "Liability Coverage should not exist for Trailers.");
        }
        vehicleCoverage.setFireTheftCoverage(true);
        if (vehicleCoverage.checkIfComprehensiveCoverageExists()) {
            Assert.fail(driver.getCurrentUrl() + "Comprehensive Coverage should not exist for Trailers if Fire & Theft is checked.");
        }
        vehicleCoverage.setFireTheftCoverage(false);
        vehicleCoverage.setComprehensive(true);
        if (vehicleCoverage.checkIfFireTheftCoverageExists()) {
            Assert.fail(driver.getCurrentUrl() + "Fire & Theft Coverage should not exist for Trailers if Fire & Theft is checked.");
        }
        vehicleCoverage.setCollision(true);

        new GuidewireHelpers(driver).logout();
    }
}

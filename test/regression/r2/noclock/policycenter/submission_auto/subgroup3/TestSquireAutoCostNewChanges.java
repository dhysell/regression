package regression.r2.noclock.policycenter.submission_auto.subgroup3;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;

import repository.driverConfiguration.Config;
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
import repository.gw.enums.Vehicle.MileageFactor;
import repository.gw.enums.Vehicle.TrailerTypePL;
import repository.gw.enums.Vehicle.Usage;
import repository.gw.enums.Vehicle.VehicleTruckTypePL;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderVehicles_CoverageDetails;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import persistence.globaldatarepo.entities.Agents;

public class TestSquireAutoCostNewChanges extends BaseTest {

    public GeneratePolicy myPolicyObjPL = null;
    private WebDriver driver;

    @BeforeMethod
    public void beforeMethod() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
    }


    /**
     * @throws GuidewireException
     * @throws Exception
     * @Author nvadlamudi
     * @Requirement
     * @RequirementsLink <a href="http:// "
     * rally1.rallydev.com/#/33274298124d/detail/userstory/
     * 52229862304</a>
     * @Description
     * @DATE Mar 22, 2016
     */
    @Test
    public void costNewValidations() throws GuidewireException, Exception {
        // create a policy
        myPolicyObjPL = createPLAutoPolicy(GeneratePolicyType.FullApp);
        Agents agent = myPolicyObjPL.agentInfo;

        // login with Underwriter, open the account
        new Login(driver).loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), myPolicyObjPL.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();


        // Validating the Vehicle Cost New Field
        validateVehicleCostNewField();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();

    }

    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Description : validating Cost New mandatory for different vehicletype
     * @DATE Mar 22, 2016
     */
    private void validateVehicleCostNewField() throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        double costNewValue = 10000.00;
        boolean isDisplayed = false;
        boolean testFailed = false;
        String errorMessage = "";

        sideMenu.clickSideMenuPAVehicles();
        String vinNumber = myPolicyObjPL.squire.squirePA.getVehicleList().get(0).getVin();

        isDisplayed = selectVehicleTypeAndPageValidations(VehicleTypePL.PassengerPickup, "ABCD12345", "Fire&Theft",
                true, costNewValue);
        if (!isDisplayed) {
            testFailed = true;
            errorMessage = errorMessage + "Expected Cost New: Mandatory validation is failed for Vehicle Type :" + VehicleTypePL.PassengerPickup;
        }

        isDisplayed = selectVehicleTypeAndPageValidations(VehicleTypePL.PrivatePassenger, null, "Fire&Theft", false,
                costNewValue);
        if (!isDisplayed) {
            testFailed = true;
            errorMessage = errorMessage + "Expected Cost New: Mandatory validation is failed for Vehicle Type :" + VehicleTypePL.PrivatePassenger;
        }

        isDisplayed = selectVehicleTypeAndPageValidations(VehicleTypePL.Motorcycle, vinNumber, "Comprehensive", true,
                costNewValue);
        if (!isDisplayed) {
            testFailed = true;
            errorMessage = errorMessage + "Expected Cost New: Mandatory validation is failed for Vehicle Type :" + VehicleTypePL.Motorcycle;
        }

        isDisplayed = selectVehicleTypeAndPageValidations(VehicleTypePL.Trailer, null, "Comprehensive", false,
                costNewValue);
        if (!isDisplayed) {
            testFailed = true;
            errorMessage = errorMessage + "Expected Cost New: Mandatory validation is failed for Vehicle Type :" + VehicleTypePL.Trailer;
        }

        isDisplayed = selectVehicleTypeAndPageValidations(VehicleTypePL.SemiTrailer, null, "Comprehensive", false,
                costNewValue);
        if (!isDisplayed) {
            testFailed = true;
            errorMessage = errorMessage + "Expected Cost New: Mandatory validation is failed for Vehicle Type :" + VehicleTypePL.SemiTrailer;
        }

        isDisplayed = selectVehicleTypeAndPageValidations(VehicleTypePL.MotorHome, null, "Comprehensive", false,
                costNewValue);
        if (!isDisplayed) {
            testFailed = true;
            errorMessage = errorMessage + "Expected Cost New: Mandatory validation is failed for Vehicle Type :" + VehicleTypePL.MotorHome;
        }

        isDisplayed = selectVehicleTypeAndPageValidations(VehicleTypePL.FarmTruck, null, "Comprehensive", false,
                costNewValue);
        if (!isDisplayed) {
            testFailed = true;
            errorMessage = errorMessage + "Expected Cost New: Mandatory validation is failed for Vehicle Type :" + VehicleTypePL.FarmTruck;
        }

        isDisplayed = selectVehicleTypeAndPageValidations(VehicleTypePL.LocalService1Ton, null, "Comprehensive", false,
                costNewValue);
        if (!isDisplayed) {
            testFailed = true;
            errorMessage = errorMessage + "Expected Cost New: Mandatory validation is failed for Vehicle Type :" + VehicleTypePL.LocalService1Ton;
        }

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + errorMessage);
        }
    }

    /**
     * @param vehicleType
     * @param vinNumber
     * @param comprehensive
     * @return
     * @Author nvadlamudi
     * @Description : changing different vehicle types
     * @DATE Mar 22, 2016
     */
    private boolean selectVehicleTypeAndPageValidations(VehicleTypePL vehicleType, String vinNumber, String coverageType, boolean comprehensive, double costNewValue) {
        GenericWorkorderVehicles_Details vehicalTab = new GenericWorkorderVehicles_Details(driver);
        GenericWorkorderVehicles_CoverageDetails vehicle_Coverage = new GenericWorkorderVehicles_CoverageDetails(driver);
        Contact personDriver = myPolicyObjPL.squire.squirePA.getDriversList().get(0);
        DriverVehicleUsePL vehicleUse = DriverVehicleUsePL.Principal;

        vehicalTab.selectDriverTableSpecificRowByText(1);

        vehicalTab.clickVehicleDetailsTab();
        vehicalTab.selectVehicleType(vehicleType);
        if (vehicleType.equals(VehicleTypePL.Trailer))
            vehicalTab.selectTrailer(TrailerTypePL.Camper);
        else if (vehicleType.equals(VehicleTypePL.SemiTrailer)) {
            vehicalTab.selectTrailer(TrailerTypePL.Semi);
            vehicalTab.selectUsage(Usage.FarmUse);
            vehicalTab.selectTruckType(VehicleTruckTypePL.OneToSix);
        } else if (vehicleType.equals(VehicleTypePL.FarmTruck)) {
            vehicalTab.setOdometer(50000);
            vehicalTab.selectMileageFactor(MileageFactor.Over49999);
            vehicalTab.selectUsage(Usage.FarmUse);
            vehicalTab.selectTruckType(VehicleTruckTypePL.OneToSix);
        } else if (!vehicleType.equals(VehicleTypePL.LocalService1Ton) && !vehicleType.equals(VehicleTypePL.MotorHome)) {
            vehicalTab.addDriver(personDriver, vehicleUse);
        }
        vehicle_Coverage.selectVehicleCoverageDetailsTab();
        if (coverageType.contains("Fire")) {
            if (comprehensive) {
                vehicle_Coverage.setFireTheftCoverage(true);
            }
        } else if (coverageType.contains("Comprehensive")) {
            if (comprehensive) {
                vehicle_Coverage.setComprehensive(true);
                vehicle_Coverage.setCollision(true);
            }
        }
        vehicalTab.clickVehicleDetailsTab();
        if (vinNumber != null)
            vehicalTab.setVIN(vinNumber);
        vehicalTab.clickOK();
        try {
            String validationMessage = vehicalTab.getVehiclePopupValidationMessage();
            if (validationMessage.contains("Cost New : Missing required")) {
                vehicalTab.setFactoryCostNew(costNewValue);
                vehicalTab.clickOK();
                return true;
            } else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param policyType
     * @return
     * @throws Exception
     * @Author nvadlamudi
     * @Description : Generating policy
     * @DATE Mar 22, 2016
     */
    private GeneratePolicy createPLAutoPolicy(GeneratePolicyType policyType) throws Exception {
        // Coverages
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK, true, UninsuredLimit.Fifty, true, UnderinsuredLimit.Fifty);
        coverages.setAccidentalDeath(true);

        // driver
        Date seniorDOB = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -68);

        ArrayList<Contact> driversList = new ArrayList<Contact>();
        Contact person = new Contact("William" + StringsUtils.generateRandomNumberDigits(5), "RegressionPL", Gender.Male, seniorDOB);
        person.setMaritalStatus(MaritalStatus.Married);
        person.setRelationToInsured(RelationshipToInsured.Insured);
        driversList.add(person);

        // Vehicle
        final ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle newVeh = new Vehicle();
        newVeh.setEmergencyRoadside(true);
        newVeh.setAdditionalLivingExpense(true);
        newVeh.setComprehensiveDeductible(Comprehensive_CollisionDeductible.FiveHundred500);
        vehicleList.add(newVeh);

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
                .withInsFirstLastName("Test", "Costnew")
                .build(policyType);

        return myPolicyObjPL;
    }

}

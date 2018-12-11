package regression.r2.noclock.policycenter.submission_auto.subgroup1;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.Vehicle.CommutingMiles;
import repository.gw.enums.Vehicle.Comprehensive_CollisionDeductible;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.Vehicle;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import persistence.globaldatarepo.entities.Agents;

public class TestSquireAutoUnassignedDriverMessage extends BaseTest {

    public GeneratePolicy myPolicyObjPL = null;
    private WebDriver driver;

    @BeforeMethod
    public void beforeMethod() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
    }


    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Requirement
     * @RequirementsLink <a href="http:// "
     * rally1.rallydev.com/#/33274298124d/detail/defect/
     * 52119128928</a>
     * @Description
     * @DATE Mar 23, 2016
     */
    @Test
    public void validateUnassignedDriver() throws Exception {
        // create a policy
        myPolicyObjPL = createPLAutoPolicy(GeneratePolicyType.FullApp);
        Agents agent = myPolicyObjPL.agentInfo;

        // login with Underwriter, open the account
        new Login(driver).loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), myPolicyObjPL.accountNumber);

        //Edit policy transaction		
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();

        // Validate Unassigned Driver error message and Unassigned driver
        validateVehicleErrorMessage();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();
    }

    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description :Validating vehicle page for error message
     * @DATE Mar 23, 2016
     */
    private void validateVehicleErrorMessage() throws Exception {

        String errorMessage = "";
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPAVehicles();
        Vehicle vehicle = new Vehicle();

        // Add Vehicle
        GenericWorkorderVehicles_Details vehicleDetails = new GenericWorkorderVehicles_Details(driver);
        vehicleDetails.createVehicleManually();

//		IGenericWorkorderVehiclePopup vehiclePopup = GenericWorkorderFactory.getGenericWorkorderVehiclePopup();
        vehicleDetails.selectVehicleType(VehicleTypePL.PassengerPickup);
        vehicleDetails.setVIN(vehicle.getVin());
        vehicleDetails.setModelYear(vehicle.getModelYear());
        vehicleDetails.setMake(vehicle.getMake());
        vehicleDetails.setModel(vehicle.getModel());
        vehicleDetails.selectCommunity(CommutingMiles.Pleasure1To2Miles);
        vehicleDetails.selectGaragedAt(myPolicyObjPL.squire.squirePA.getVehicleList().get(0).getGaragedAt());
        vehicleDetails.clickOK();
        try {
            String validationMessage = vehicleDetails.getVehiclePopupValidationMessage();
            if (validationMessage.contains("The vehicle must either have a driver assigned or have unassigned driver checked")) {
                System.out.println("Expected error message 'The vehicle must either have a driver assigned or have unassigned driver checked'");
                vehicleDetails.setNoDriverAssigned(true);
                vehicleDetails.clickOK();
            } else {
                errorMessage = errorMessage + "Expected unassigned driver message is not displayed.";
            }
        } catch (Exception e) {
            errorMessage = errorMessage + "Expected unassigned driver message is not displayed.";
        }
    }

    /**
     * @param policyType
     * @return
     * @throws Exception
     * @Author nvadlamudi
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description : Creating quick quote
     * @DATE Mar 23, 2016
     */
    private GeneratePolicy createPLAutoPolicy(GeneratePolicyType policyType) throws Exception {
        // Coverages
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK, true, UninsuredLimit.Fifty, true, UnderinsuredLimit.Fifty);
        coverages.setAccidentalDeath(true);

        // driver
        ArrayList<Contact> driversList = new ArrayList<Contact>();
        Contact person = new Contact();
        driversList.add(person);

        // Vehicle
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
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
                .withInsFirstLastName("Test", "Unassdriv")
                .build(policyType);

        return myPolicyObjPL;
    }

}

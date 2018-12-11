package regression.r2.noclock.policycenter.submission_auto.subgroup3;

import java.util.ArrayList;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.idfbins.enums.OkCancel;

import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.CollisionLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.ComprehensiveLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.RentalReimbursementLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.Vehicle.CommutingMiles;
import repository.gw.enums.Vehicle.DriverVehicleUsePL;
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
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderLineReviewPL;
import repository.pc.workorders.generic.GenericWorkorderLineSelection;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages;
import repository.pc.workorders.generic.GenericWorkorderVehicles_CoverageDetails;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.VINHelper;

/**
 * @Author nvadlamudi
 * @Requirement : Test 2 is added due to the Defect: DE4386
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Jan 24, 2017
 */
public class TestSquireAutoCoverageChanges extends BaseTest {

    public GeneratePolicy myPolicyObjPL = null, mySQAutoPolObjPol = null;
    private String insFirstName = "Test";
    private String insLastName = "CovChange";
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
     * rally1.rallydev.com/#/33274298124d/detail/userstory/
     * 47934293823</a>
     * @Description
     * @DATE Mar 17, 2016
     */
    @Test()
    public void coverageChanges() throws Exception {
        // create a policy
        myPolicyObjPL = createPLAutoPolicy(GeneratePolicyType.FullApp);
        Agents agent = myPolicyObjPL.agentInfo;

        // login with Underwriter, open the account
        new Login(driver).loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), myPolicyObjPL.accountNumber);

        // Edit the policy Transaction
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();

        // Adding Vehicle Coverages and validating in second vehicle
        addVehicleCoverages();

        // Adding new Vehicle and changing the coverages
        addNewVehicleWithCoverages();

        // Validating accident Death on line Review papge
        validatePALineReviewPage();

        //DE3600
        validateVehicleCoverage();
    }

    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description : validating line review with Accident death details
     * @DATE Mar 17, 2016
     */
    private void validatePALineReviewPage() throws Exception {
        boolean failedTest = false;
        String errorMessage = "";

        // Adding line selection
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuLineSelection();

        GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(driver);
        lineSelection.checkPersonalPropertyLine(true);
        sideMenu.clickSideMenuSquireLineReview();

        // Taking count from Policy members
        sideMenu.clickSideMenuHouseholdMembers();

        GenericWorkorderPolicyMembers houseHold = new GenericWorkorderPolicyMembers(driver);
        int rowCount = houseHold.getPolicyHouseholdMembersTableRowCount();

        // Validating on line review page
        sideMenu.clickSideMenuSquireLineReview();
        GenericWorkorderLineReviewPL lineReview = new GenericWorkorderLineReviewPL(driver);

        if (lineReview.getLineReviewAccidentalDeath() == rowCount)
            System.out.println("Expected Accidental Death value " + lineReview.getLineReviewAccidentalDeath() + " is displayed.");
        else {
            errorMessage = errorMessage + "Expected Accidental Death value is not displayed.";
            failedTest = true;
        }

        if (failedTest)
            Assert.fail(driver.getCurrentUrl() + errorMessage);

    }

    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description :Adding vehicle with coverage details
     * @DATE Mar 17, 2016
     */
    private void addNewVehicleWithCoverages() throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        // Adding Vehicle

        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        GenericWorkorderVehicles_CoverageDetails vehicleCoverages = new GenericWorkorderVehicles_CoverageDetails(driver);
        vehiclePage.createVehicleManually();
        Vin vin = VINHelper.getRandomVIN();
        vehiclePage.setVIN(vin.getVin());
        vehiclePage.setModelYear(Integer.parseInt(vin.getYear()));
        vehiclePage.setMake(vin.getMake());
        vehiclePage.setModel(vin.getModel());
        vehiclePage.setFactoryCostNew(1000);
        vehiclePage.selectCommunity(CommutingMiles.Pleasure1To2Miles);
        vehiclePage.addDriver(this.myPolicyObjPL.squire.squirePA.getDriversList().get(0), DriverVehicleUsePL.Principal);
        vehiclePage.selectGaragedAtZip(myPolicyObjPL.squire.squirePA.getVehicleList().get(0).getGaragedAt().getCity());


        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle newVeh = new Vehicle();
        newVeh.setEmergencyRoadside(true);
        vehicleList.add(newVeh);
        myPolicyObjPL.squire.squirePA.setVehicleList(vehicleList);

        // validating the default values for 2nd vehicle
        vehicleCoverages.selectVehicleCoverageDetailsTab();

        vehicleCoverages.setComprehensive(true);
        vehicleCoverages.setCollision(true);
        // vehicalTab.setRentalReimbursementCoverage(true);
        vehicleCoverages.setAdditionalLivingExpense(true);
        validateVehicleCoverageValues(vehicleCoverages, ComprehensiveLimit.FiveHundred.getValue(), CollisionLimit.FiveHundred.getValue(), RentalReimbursementLimit.TwentyFive.getValue(), "75");

        // Editing the default values for 2nd vehicle
        vehicleCoverages.setComprehensive(true);
        vehicleCoverages.setComprehensiveDeductible(ComprehensiveLimit.TwoFifty.getValue());
        vehicleCoverages.setCollision(true);
        vehicleCoverages.setCollisionDeductible(CollisionLimit.TwoFifty.getValue());
        vehicleCoverages.setRentalReimbursementDeductible(RentalReimbursementLimit.Fifty.getValue());
        vehiclePage.clickOK();
        vehiclePage.selectOKOrCancelFromPopup(OkCancel.OK);
        new GuidewireHelpers(driver).isOnPage("//span[(@class='g-title') and (contains(text(), 'Vehicle Details'))]", 5000, "UNABLE TO GET TO MAIN VEHICLE PAGE AFTER CLICKING OK AND ACKNOWLAGING POPUP.");

        // Verify 1st Vehicle with same values
        vehiclePage.selectDriverTableSpecificRowByText(1);
        vehiclePage.selectVehicleCoverageDetailsTab();
        validateVehicleCoverageValues(vehicleCoverages, ComprehensiveLimit.TwoFifty.getValue(), CollisionLimit.TwoFifty.getValue(), RentalReimbursementLimit.Fifty.getValue(), "75");
        vehiclePage.clickOK();

    }

    //DE3600 : PL-Deductible not available at new submission
    //validate Comprehensive cannot apply if Fire & Theft is selected and vice-versa
    public void validateVehicleCoverage() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Agents agent = myPolicyObjPL.agentInfo;
        new Login(driver).loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), myPolicyObjPL.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPAVehicles();

        GenericWorkorderVehicles_CoverageDetails vehicle = new GenericWorkorderVehicles_CoverageDetails(driver);
        vehicle.editVehicleByVehicleNumber(1);
        vehicle.selectVehicleCoverageDetailsTab();
        vehicle.setComprehensive(false);

        if (!vehicle.checkIfFireTheftCoverageExists()) {
            Assert.fail("Fire & Theft cannot apply if Comprehensive is selected." + vehicle.checkIfFireTheftCoverageExists());
        }

        vehicle.setFireTheftCoverage(true);
        vehicle.sendArbitraryKeys(Keys.TAB);
        if (vehicle.checkIfComprehensiveCoverageExists()) {
            Assert.fail("Comprehensive cannot apply if Fire & Theft is selected." + vehicle.checkIfComprehensiveCoverageExists());
        }
    }

    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description : Adding Vehicle to the existing details
     * @DATE Mar 17, 2016
     */
    private void addVehicleCoverages() throws Exception {

        SideMenuPC sideMenu = new SideMenuPC(driver);

        // Add Comprehensive coverage
        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_CoverageDetails vehicalTab = new GenericWorkorderVehicles_CoverageDetails(driver);
        vehicalTab.clickLinkInVehicleTable("Edit");
        GenericWorkorderVehicles_Details vehicleDetails = new GenericWorkorderVehicles_Details(driver);
        vehicleDetails.setFactoryCostNew(10000.00);

        vehicalTab.selectVehicleCoverageDetailsTab();

        vehicalTab.setComprehensive(true);
        vehicalTab.setCollision(true);
        // vehicalTab.setRentalReimbursementCoverage(true);
        vehicalTab.setAdditionalLivingExpense(true);
        validateVehicleCoverageValues(vehicalTab, ComprehensiveLimit.FiveHundred.getValue(), CollisionLimit.FiveHundred.getValue(), RentalReimbursementLimit.TwentyFive.getValue(), "75");
        vehicalTab.clickOK();
    }

    /**
     * @param vehicalTab
     * @param comprehensive
     * @param collision
     * @param rentailReimbursement
     * @param additionalLiving
     * @Author nvadlamudi
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description : Validate Vehicle Coverage details
     * @DATE Mar 17, 2016
     */
    private void validateVehicleCoverageValues(GenericWorkorderVehicles_CoverageDetails vehicalTab, String
            comprehensive, String collision, String rentailReimbursement, String additionalLiving) {

        boolean testFailed = false;
        String errorMessage = "";

        // Checking the Comprehensive coverage deductible
        if (vehicalTab.getComprehensiveCoverageLimit().equals(comprehensive))
            System.out.println("Expected Comprehensive Deductible : " + comprehensive + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected Comprehensive Deductible : " + comprehensive + " is not displayed.";
        }

        // Checking the Collision coverage deductible
        if (vehicalTab.getCollisionCoverageLimit().equals(collision))
            System.out.println("Expected Collision Deductible : " + collision + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected Collision Deductible : " + collision + " is not displayed.";
        }

        // Checking the Rental Reimbursement limit
        if (vehicalTab.getRentalReimbursementLimit().equals(rentailReimbursement))
            System.out.println("Expected Rental Reimbursement : " + rentailReimbursement + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected Rental Reimbursement : " + rentailReimbursement + " is not displayed.";
        }

        // Checking the additional Living expenses
        if (vehicalTab.getAdditionalLivingExpense().equals(additionalLiving))
            System.out.println("Expected Living Expenses Limit: " + additionalLiving + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected Living Expenses Limit: " + additionalLiving + " is not displayed.";
        }

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + errorMessage);
        }
    }

    /**
     * @param policyType
     * @return
     * @throws Exception
     * @Author nvadlamudi
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description : Method to create quick quote
     * @DATE Mar 17, 2016
     */
    private GeneratePolicy createPLAutoPolicy(GeneratePolicyType policyType) throws Exception {
        // Coverages
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.OneHundredHigh, MedicalLimit.FiveK, true, UninsuredLimit.OneHundred, true, UnderinsuredLimit.OneHundred);
        coverages.setAccidentalDeath(true);

        // driver
        ArrayList<Contact> driversList = new ArrayList<Contact>();
        Contact person = new Contact();
        driversList.add(person);

        // Vehicle
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle newVeh = new Vehicle();
        newVeh.setEmergencyRoadside(true);
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
                .withInsFirstLastName(insFirstName, insLastName)
                .build(policyType);

        return myPolicyObjPL;
    }


    @Test()
    private void testValidateAutoCoverages() throws Exception {

        // create a policy
        mySQAutoPolObjPol = createPLAutoPolicy(GeneratePolicyType.FullApp);
        Agents agent = mySQAutoPolObjPol.agentInfo;

        // login with Underwriter, open the account
        new Login(driver).loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), mySQAutoPolObjPol.accountNumber);

        // Edit the policy Transaction
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPAVehicles();
        sideMenu.clickSideMenuPACoverages();

        //Issue 1 
        GenericWorkorderSquireAutoCoverages_Coverages coveragePage = new GenericWorkorderSquireAutoCoverages_Coverages(driver);
        coveragePage.setLiabilityCoverage(LiabilityLimit.FiftyHigh);
        coveragePage.sendArbitraryKeys(Keys.TAB);

        coveragePage.setUninsuredCoverage(true, UninsuredLimit.TwentyFive);
        coveragePage.sendArbitraryKeys(Keys.TAB);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        String errorMessage = "";
        if (guidewireHelpers.errorMessagesExist() && (guidewireHelpers.getFirstErrorMessage().contains("null"))) {
            errorMessage = errorMessage + "UnExpected page Error : NullPointerException is displayed. /n";
        }

        //Issue 2
        coveragePage.setUninsuredCoverage(false, null);
        coveragePage.clickNext();
        if (guidewireHelpers.errorMessagesExist() && (guidewireHelpers.getFirstErrorMessage().contains("Under-Insured Motorist Coverage is not permitted without Uninsured Motorist Coverage"))) {
            errorMessage = errorMessage + "UnExpected page Error : 'Under-Insured Motorist Coverage is not permitted without Uninsured Motorist Coverage' is displayed. /n";
        }

        //Issue 3
        sideMenu.clickSideMenuPACoverages();

        coveragePage.setUnderinsuredCoverage(false, null);

        sideMenu.clickSideMenuPAModifiers();

        sideMenu.clickSideMenuPACoverages();

        coveragePage.setUnderinsuredCoverage(true, UnderinsuredLimit.Thirty);


        //Issue 4
        coveragePage.setUninsuredCoverage(false, null);
        coveragePage.setUnderinsuredCoverage(false, null);
        sideMenu.clickSideMenuPAModifiers();

        sideMenu.clickSideMenuPACoverages();

        coveragePage.setUninsuredCoverage(true, UninsuredLimit.Fifty);
        coveragePage.sendArbitraryKeys(Keys.TAB);

        coveragePage.setUnderinsuredCoverage(true, UnderinsuredLimit.Fifty);
        sideMenu.clickSideMenuPAModifiers();

        sideMenu.clickSideMenuPACoverages();

        if (!coveragePage.getUninsuredMotoristBodilyInjury().contains(UninsuredLimit.Fifty.getValue())) {
            errorMessage = errorMessage + "Newly entered Uninsured motorist is not displayed /n";
        }

        coveragePage.clickNext();
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();

        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }
    }


}

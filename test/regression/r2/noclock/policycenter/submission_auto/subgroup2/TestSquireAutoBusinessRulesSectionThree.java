package regression.r2.noclock.policycenter.submission_auto.subgroup2;

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
import repository.gw.enums.NotRatedReason;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.enums.Vehicle.Comprehensive_CollisionDeductible;
import repository.gw.enums.Vehicle.TrailerTypePL;
import repository.gw.enums.Vehicle.Usage;
import repository.gw.enums.Vehicle.VehicleTruckTypePL;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.FullUnderWriterIssues;
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
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import persistence.globaldatarepo.helpers.VINHelper;

/**
 * @Author skandibanda
 * @Requirement : US6314 PL - PA - Vehicle screen business rules validations - Block Issues, Block Quote, validations - UW approval
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/33274298124d/detail/userstory/48236470122">Rally Story</a>
 * @DATE Apr 28, 2016
 */
public class TestSquireAutoBusinessRulesSectionThree extends BaseTest {

    public GeneratePolicy myPolicyObjPL = null;
    public String driverName = null;
    private Underwriters uw;
    private WebDriver driver;

    @BeforeMethod
    public void beforeMethod() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
    }


    @Test()
    public void testGenerateSquireAutoOnly() throws Exception {
        // Creating policy
        myPolicyObjPL = createPLAutoPolicy(GeneratePolicyType.FullApp);
    }

    @Test(dependsOnMethods = {"testGenerateSquireAutoOnly"})
    public void sectionThreeBusinessRulesValidation() throws Exception {
        // Login with agent
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.accountNumber);
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();

        // Adding 6314 validations for Block Issues,quote errors
        addingBlockBindIssuesQuoteScenarios();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();
        quote.clickQuote();
        validateRiskAnalysisApprovals();

        changingVehicleDetails();
    }

    private void changingVehicleDetails() throws Exception {
        boolean testFailed = false;
        String errorMessage = "";
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();
        Vehicle vehicle = new Vehicle();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        vehiclePage.createVehicleManually();
        vehiclePage.createGenericVehicle(VehicleTypePL.SemiTrailer);
        vehiclePage.vinAutoFill(vehicle);
        vehiclePage.selectUsage(Usage.FarmUse);
        vehiclePage.selectTruckType(VehicleTruckTypePL.TractorType);
        vehiclePage.selectTrailer(TrailerTypePL.Semi);
        vehiclePage.selectGaragedAtZip(myPolicyObjPL.squire.squirePA.getVehicleList().get(0).getGaragedAt().getCity());
        vehiclePage.clickOK();
        //Per Zubair, as long as blocks issuance and firing at quote, able to quote w/o validation message
		/*sideMenu.clickSideMenuRiskAnalysis();
				GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();

		String validationMessages = risk.getValidationMessagesText();
		String valMeg1 = "At least one of the semi-trailer's usage and truck type must match that of existing farm trucks";
		if(!validationMessages.contains(valMeg1)) {
			testFailed = false;
			errorMessage = errorMessage + "Expected Validation message : '" + valMeg1 + "' is not displayed.";		} 
		risk.clickClearButton();
		 */
        vehiclePage.setCheckBoxInVehicleTable(4);
        vehiclePage.clickRemoveVehicle();

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();
        String validationMessages = risk.getValidationMessagesText();
        validationMessages = risk.getValidationMessagesText();
        String ValMsg2 = "A farm truck must exist in policy to add a semi-trailer. Please add farm truck";
        if (!validationMessages.contains(ValMsg2)) {
            testFailed = false;
            errorMessage = errorMessage + "Expected Validation message : '" + ValMsg2 + "' is not displayed.";
        }
        risk.clickClearButton();

        if (testFailed)
            Assert.fail(errorMessage);
    }

    private void addingBlockBindIssuesQuoteScenarios() throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPAVehicles();

        //adding student discount
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        vehiclePage.editVehicleByVehicleNumber(1);
        vehiclePage.setGoodStudentDiscountRadio(true);
        vehiclePage.clickOK();

        //adding senior discount
        vehiclePage.editVehicleByVehicleNumber(2);
        vehiclePage.setSeniorDiscountRadio(true);
        vehiclePage.clickOK();

        //adding motorcycle discount
        vehiclePage.editVehicleByVehicleNumber(3);
        vehiclePage.setMotorCyclesStarDiscountRadio(true);
        vehiclePage.clickOK();

        //adding not rated driver
        vehiclePage.editVehicleByVehicleNumber(4);
        vehiclePage.setGVW(51000);
        vehiclePage.clickOK();

        sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        paDrivers.clickEditButtonInDriverTableByDriverName(driverName);
        paDrivers.setNotRatedCheckbox(true);
        paDrivers.selectNotRatedReason(NotRatedReason.ExchangeStudent);
        paDrivers.clickOk();
    }

    private void validateRiskAnalysisApprovals() {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuRiskAnalysis();
        boolean testFailed = false;
        String errorMessage = "";
        GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);

        String[] expectedUWMessages = {"MVR not found", "City squire With Farm Truck",
                "55 and Alive Discount", "Good Student Discount", "Driver under 16", "Motorcycle Discount and New Business", "Not-rated drivers",
                "Farm Trucks over 50,000 GVW", "CLUE Auto Not Ordered"};
        FullUnderWriterIssues uwIssues = riskAnalysis.getUnderwriterIssues();

        for (String uwIssue : expectedUWMessages) {
            if (uwIssues.isInList_ShortDescription(uwIssue).equals(UnderwriterIssueType.NONE)) {
                testFailed = true;
                errorMessage = errorMessage + "Expected error Bind Issue : " + uwIssue + " is not displayed.";
            }
        }

        if (testFailed) {
            Assert.fail(errorMessage);
        }
    }

    private GeneratePolicy createPLAutoPolicy(GeneratePolicyType policyType) throws Exception {

        Contact driver1, driver2, driver3, driver4, driver5, driver6;
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.OneHundredHigh, MedicalLimit.TenK);
        coverages.setUninsuredLimit(UninsuredLimit.OneHundred);
        ArrayList<Contact> driversList = new ArrayList<Contact>();
        String randFirst = StringsUtils.generateRandomNumberDigits(4);

        //Earlier student age was hard coded to 15, now it will calculates from system date
        Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Year, -15);
        String studentDOB = DateUtils.dateFormatAsString("MM/dd/YYYY", changeDate);

        driver1 = new Contact("driver-" + randFirst, "student", Gender.Male, DateUtils.convertStringtoDate(studentDOB, "MM/dd/YYYY"));
        driver2 = new Contact("driver-" + randFirst, "senior", Gender.Female, DateUtils.convertStringtoDate("06/01/1955", "MM/dd/YYYY"));
        driver3 = new Contact("driver-" + randFirst, "motorcycle", Gender.Female, DateUtils.convertStringtoDate("06/01/1980", "MM/dd/YYYY"));
        driver4 = new Contact("driver-" + randFirst, "notRated", Gender.Female, DateUtils.convertStringtoDate("06/01/1981", "MM/dd/YYYY"));
        driverName = "driver-" + randFirst + " notRated";
        driver5 = new Contact("driver-" + randFirst, "farmTruck", Gender.Male, DateUtils.convertStringtoDate("06/01/1982", "MM/dd/YYYY"));
        driver6 = new Contact("driver-" + randFirst, "semitrailer", Gender.Female, DateUtils.convertStringtoDate("06/01/1983", "MM/dd/YYYY"));
        driversList.add(driver1);
        driversList.add(driver2);
        driversList.add(driver3);
        driversList.add(driver4);
        driversList.add(driver5);
        driversList.add(driver6);

        //Vehicles
        Vin vin = VINHelper.getRandomVIN();
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle veh1 = new Vehicle();
        veh1.setVehicleTypePL(VehicleTypePL.PassengerPickup);
        veh1.setVin(vin.getVin());
        //veh1.setVin("5FPYK1F53CB459476");

        veh1.setEmergencyRoadside(true);
        veh1.setAdditionalLivingExpense(true);
        veh1.setComprehensiveDeductible(Comprehensive_CollisionDeductible.FiveHundred500);
        veh1.setDriverPL(driver1);

        vin = VINHelper.getRandomVIN();
        Vehicle veh2 = new Vehicle();
        veh2.setVehicleTypePL(VehicleTypePL.PrivatePassenger);
        veh2.setVin(vin.getVin());
        veh2.setDriverPL(driver2);

        vin = VINHelper.getRandomVIN();
        Vehicle veh3 = new Vehicle();
        veh3.setVehicleTypePL(VehicleTypePL.Motorcycle);
        veh3.setVin(vin.getVin());
        veh3.setDriverPL(driver3);

        vin = VINHelper.getRandomVIN();
        Vehicle veh4 = new Vehicle();
        veh4.setVehicleTypePL(VehicleTypePL.FarmTruck);
        veh4.setTruckType(VehicleTruckTypePL.UnderFour);
        veh4.setGvw(51000);
        veh4.setVin(vin.getVin());
        veh4.setUsage(Usage.FarmUseWithOccasionalHire);

        vehicleList.add(veh1);
        vehicleList.add(veh2);
        vehicleList.add(veh3);
        vehicleList.add(veh4);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);
        squirePersonalAuto.setVehicleList(vehicleList);
        squirePersonalAuto.setDriversList(driversList);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        this.myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Test", "Sec3BusRules")
                .build(policyType);

        return myPolicyObjPL;
    }


}

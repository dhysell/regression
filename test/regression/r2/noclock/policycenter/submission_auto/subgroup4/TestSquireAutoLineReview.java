package regression.r2.noclock.policycenter.submission_auto.subgroup4;

import java.util.ArrayList;

import javax.xml.datatype.DatatypeConfigurationException;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.testng.listener.QuarantineClass;

import services.broker.objects.vin.requestresponse.ValidateVINResponse;
import services.broker.services.vin.ServiceVINValidation;
import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.TransactionType;
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
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderInsuranceScore;
import repository.pc.workorders.generic.GenericWorkorderLineReviewPL;
import repository.pc.workorders.generic.GenericWorkorderLineSelection;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers;
import repository.pc.workorders.generic.GenericWorkorderVehicles;
import repository.pc.workorders.generic.GenericWorkorderVehicles_CoverageDetails;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import persistence.globaldatarepo.helpers.VINHelper;

/**
 * This class is to validate Line Review page under Section 3 auto with
 * Vehicle and coverage details
 *
 * @Author nvadlamudi
 * @Requirement : PC8 - PA - QuoteApplication - Line Review
 * @RequirementsLink <a href=
 * "http:// " >rally1.rallydev.com/#/42213110140d/detail/
 * userstory/ 50286396080</a>
 * @Description
 * @DATE Mar 2, 2016
 */
@QuarantineClass
public class TestSquireAutoLineReview extends BaseTest {

    public GeneratePolicy myPolicyObjPL = null, squireAutoObj = null;
    private Underwriters uw;

    private WebDriver driver;

    @BeforeMethod
    public void beforeMethod() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
    }


    @Test(enabled = true)
    public void generate() throws Exception {
        myPolicyObjPL = createPLAutoPolicy(GeneratePolicyType.QuickQuote);
        Agents agent = myPolicyObjPL.agentInfo;

        new Login(driver).loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), myPolicyObjPL.accountNumber);

        validateLineReviewPage(false);

        SquirePersonalAutoCoverages coverages = myPolicyObjPL.squire.squirePA.getCoverages();
        ArrayList<Vehicle> vehicle = myPolicyObjPL.squire.squirePA.getVehicleList();

        addLineCoverage();
        validateLineReviewPage(true);
        validateLineReviewCoverages(coverages);
        validateLineReviewVehicleDetails(vehicle);
    }

    /**
     * This method is to validate the Line Review page Coverages with the
     * details entered during the new submission
     *
     * @param coverages
     * @throws Exception
     * @Author nvadlamudi
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description
     * @DATE Mar 2, 2016
     */
    private void validateLineReviewCoverages(SquirePersonalAutoCoverages coverages) throws Exception {

        boolean failedTest = false;
        String errorMessage = "";

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuHouseholdMembers();

        GenericWorkorderPolicyMembers houseHold = new GenericWorkorderPolicyMembers(driver);
        int rowCount = houseHold.getPolicyHouseholdMembersTableRowCount();

        sideMenu.clickSideMenuSquireLineReview();
        GenericWorkorderLineReviewPL lineReview = new GenericWorkorderLineReviewPL(driver);

        // Validation for Accidental Death
        if (coverages.hasAccidentalDeath()) {
            if (lineReview.getLineReviewAccidentalDeath() == rowCount)
                System.out.println("Expected Accidental Death value " + lineReview.getLineReviewAccidentalDeath() + " is displayed.");
            else {
                errorMessage = errorMessage + "Expected Accidental Death value is not displayed.";
                failedTest = true;
            }
        } else {
            if (lineReview.getLineReviewAccidentalDeath() == 0)
                System.out.println("Expected Accidental Death " + lineReview.getLineReviewAccidentalDeath() + " displayed.");
            else {
                errorMessage = errorMessage + "Wrong Accidental Death value displayed.";
                failedTest = true;
            }
        }

        // Validating the Liability Coverages value
        if (coverages.getLiability().getValue().equals(lineReview.getLineReviewLiabilityBIPD()))
            System.out.println("Expected Liability coverage " + coverages.getLiability().getValue() + " value is displayed.");
        else {
            errorMessage = errorMessage + "Expected Liability coverage " + coverages.getLiability().getValue() + " value is not displayed.";
            failedTest = true;
        }

        // Validating the Medical Coverages value
        if (coverages.getMedical().getValue().equals(lineReview.getLineReviewMedical()))
            System.out.println("Expected medical coverage " + coverages.getMedical().getValue() + " value is displayed.");
        else {
            errorMessage = errorMessage + "Wrong medical coverage " + coverages.getMedical().getValue() + " value is not displayed.";
            failedTest = true;
        }

        // Validating the Uninsured motorist Coverages value
        if (coverages.getUninsuredLimit().getValue().equals(lineReview.getLineReviewUninsuredMotoristBodilyInjury()))
            System.out.println("Expected Uninsured motorist coverage " + coverages.getUninsuredLimit().getValue() + " value is displayed.");
        else {
            errorMessage = errorMessage + "Wrong Uninsured motorist coverage " + coverages.getUninsuredLimit().getValue() + " value is not displayed.";
            failedTest = true;
        }

        if (failedTest) {
            Assert.fail(driver.getCurrentUrl() + errorMessage);
        }
    }

    /**
     * This method to validate Vehicle details on the Line Review page with the
     * details entered during new submission
     *
     * @param vehicleList
     * @throws Exception
     * @throws DatatypeConfigurationException
     * @Author nvadlamudi
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description
     * @DATE Mar 2, 2016
     */
    private void validateLineReviewVehicleDetails(ArrayList<Vehicle> vehicleList) throws DatatypeConfigurationException, Exception {
        GenericWorkorderLineReviewPL lineReview = new GenericWorkorderLineReviewPL(driver);
        boolean failedTest = false;
        String errorMessage = "";

        for (Vehicle vehicle : vehicleList) {

            // validating the vehicle VIN on the Line Review page
            if (vehicle.getVin().contains(lineReview.getLineReviewVehicleColumnName("VIN").toString().substring(1, lineReview.getLineReviewVehicleColumnName("VIN").toString().length() - 1)))
                System.out.println("Expected VIN number " + vehicle.getVin() + " is displayed.");
            else {
                errorMessage = errorMessage + "Expected VIN number " + vehicle.getVin() + " is not displayed.";
                failedTest = true;
            }

            // validating the vehicle Number on the Line Review page
            if (lineReview.getLineReviewVehicleColumnName("Vehicle Number").toString().contains("" + vehicle.getVehicleNumber()))
                System.out.println("Expected Vehicle number " + vehicle.getVehicleNumber() + " is displayed.");
            else {
                errorMessage = errorMessage + "Expected Vehicle number " + vehicle.getVehicleNumber() + " is not displayed.";
                failedTest = true;
            }

            boolean printRequestXMLToConsole = false;
            boolean printResponseXMLToConsole = false;
            ServiceVINValidation testService = new ServiceVINValidation();
            ValidateVINResponse testResponse = testService.validateVIN2(testService.setUpTestValidateVINRequest(vehicle.getVin()), new GuidewireHelpers(driver).getMessageBrokerConnDetails(), printRequestXMLToConsole, printResponseXMLToConsole);

            // validating the vehicle details (make, model, year) on the Line
            // Review page
            if (lineReview.getLineReviewVehicleColumnName("Vehicle").toString().contains(testResponse.getYear() + " " + testResponse.getMake() + " " + testResponse.getModel()))
                System.out.println("Expected Vehicle details " + testResponse.getYear() + " " + testResponse.getMake() + " " + testResponse.getModel() + " is displayed.");
            else {
                errorMessage = errorMessage + "Expected Vehicle details " + testResponse.getYear() + " " + testResponse.getMake() + " " + testResponse.getModel() + " is not displayed.";
                failedTest = true;
            }

            if (vehicle.hasCollision()) {
                if (lineReview.getLineReviewVehicleColumnName("Coll").toString().contains("Yes"))
                    System.out.println("Expected Collision " + lineReview.getLineReviewVehicleColumnName("Coll").toString() + " value is displayed.");
                else {
                    errorMessage = errorMessage + "Expected Collision " + lineReview.getLineReviewVehicleColumnName("Coll").toString() + " value is not displayed.";
                    failedTest = true;
                }
            }
            if (vehicle.hasComprehensive()) {
                if (lineReview.getLineReviewVehicleColumnName("Comp").toString().contains("Yes"))
                    System.out.println("Expected Comprehensive " + lineReview.getLineReviewVehicleColumnName("Comp").toString() + " value is displayed.");
                else {
                    errorMessage = errorMessage + "Expected Comprehensive " + lineReview.getLineReviewVehicleColumnName("Comp").toString() + " value is not displayed.";
                    failedTest = true;
                }
            }

            if (vehicle.hasEmergencyRoadside()) {
                if (lineReview.getLineReviewVehicleColumnName("ERS").toString().contains("Yes"))
                    System.out.println("Expected Emergency Roadside Assistance " + lineReview.getLineReviewVehicleColumnName("ERS").toString() + " value is displayed.");
                else {
                    errorMessage = errorMessage + "Expected Emergency Roadside Assistance " + lineReview.getLineReviewVehicleColumnName("ERS").toString() + " value is not displayed.";
                    failedTest = true;
                }
            }
            if (vehicle.hasFireAndTheft()) {
                if (lineReview.getLineReviewVehicleColumnName("F&T").toString().contains("Yes"))
                    System.out.println("Expected fire and theft " + lineReview.getLineReviewVehicleColumnName("F&T").toString() + " value is displayed.");
                else {
                    errorMessage = errorMessage + "Expected fire and theft " + lineReview.getLineReviewVehicleColumnName("F&T").toString() + " value is not displayed.";
                    failedTest = true;
                }
            }

            if (vehicle.hasSquireRentalRemburesement()) {
                if (lineReview.getLineReviewVehicleColumnName("Rental").toString().contains("Yes"))
                    System.out.println("Expected Renal Reimbursement " + lineReview.getLineReviewVehicleColumnName("Rental").toString() + " value is displayed.");
                else {
                    errorMessage = errorMessage + "Expected Renal Reimbursement " + lineReview.getLineReviewVehicleColumnName("Rental").toString() + " value is not displayed.";
                    failedTest = true;
                }
            }

            if (vehicle.hasAdditionalLivingExpense()) {
                if (lineReview.getLineReviewVehicleColumnName("ALE").toString().contains("Yes"))
                    System.out.println("Expected Additional Living Expesnes " + lineReview.getLineReviewVehicleColumnName("ALE").toString() + " value is displayed.");
                else {
                    errorMessage = errorMessage + "Expected Additional Living Expesnes " + lineReview.getLineReviewVehicleColumnName("ALE").toString() + " value is not displayed.";
                    failedTest = true;
                }
            }

        }

        if (failedTest) {
            Assert.fail(driver.getCurrentUrl() + errorMessage);
        }
    }

    /**
     * This method is to add few more coverages to see Line Review page
     *
     * @throws Exception
     * @Author nvadlamudi
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description
     * @DATE Mar 2, 2016
     */
    private void addLineCoverage() throws Exception {
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickGenericWorkorderFullApp();
        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
        qualificationPage.setSquireGeneralFullTo(false);
        qualificationPage.setSquireAutoFullTo(false);
        GenericWorkorderPolicyInfo pi = new GenericWorkorderPolicyInfo(driver);
        pi.fillOutPolicyInfoPageFA(myPolicyObjPL);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        // household members
        GenericWorkorderPolicyMembers houseHold = new GenericWorkorderPolicyMembers(driver);
        houseHold.updatePLHouseholdMembersFA(myPolicyObjPL);
        GenericWorkorderInsuranceScore creditReport = new GenericWorkorderInsuranceScore(driver);
        creditReport.fillOutCreditReporting(myPolicyObjPL);
        // PA drivers
        sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers drivers = new GenericWorkorderSquireAutoDrivers(driver);
        drivers.fillOutDriversFA(myPolicyObjPL);

        drivers.clickNext();

        // PA vehicles
        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles vehicles = new GenericWorkorderVehicles(driver);
        vehicles.fillOutVehicles_FA(myPolicyObjPL);

        sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuLineSelection();
        GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(driver);
        lineSelection.checkPersonalPropertyLine(true);
        sideMenu.clickSideMenuSquireLineReview();
    }

    /**
     * This method is to validate Line Review page is displayed with single
     * coverage and multiple coverages
     *
     * @param lineReviewExists
     * @Author nvadlamudi
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description
     * @DATE Mar 2, 2016
     */
    private void validateLineReviewPage(boolean lineReviewExists) {
        try {
            SideMenuPC sideMenu = new SideMenuPC(driver);
            boolean lRExists = sideMenu.isLineReviewPresent();
            if (lineReviewExists && lRExists) {
                System.out.println("Line Review page exists.");

            } else {
                if (!lRExists)
                    System.out.println("Line Review page does not exists.");
                else
                    System.out.println("Line Review page does exists....Please double check the line selection");
            }
        } catch (NoSuchElementException e) {

        }
    }

    /**
     * This method is used to call the existing generate method for quick quote
     *
     * @param policyType
     * @return
     * @throws Exception
     * @Author nvadlamudi
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description
     * @DATE Mar 2, 2016
     */
    private GeneratePolicy createPLAutoPolicy(GeneratePolicyType policyType) throws Exception {
        // Coverages
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK);
        coverages.setUnderinsured(false);
        coverages.setUninsuredLimit(UninsuredLimit.Fifty);
        coverages.setAccidentalDeath(true);

        // driver
        ArrayList<Contact> driversList = new ArrayList<Contact>();
        Contact person = new Contact();
        driversList.add(person);

        // Vehicle
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle toAdd = new Vehicle();
        toAdd.setEmergencyRoadside(true);
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
                .withInsFirstLastName("Test", "Autolinerev")
                .build(policyType);

        return myPolicyObjPL;
    }

    /**
     * @Author skandibanda
     * @Requirement : DE4787 - Loss of Use by Theft
     * @DATE Feb 17, 2017
     */

    @Test
    public void testGenerateAutoQQ() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Contact driver1;
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.OneHundredHigh, MedicalLimit.FiveK, true, UninsuredLimit.OneHundred, true, UnderinsuredLimit.OneHundred);
        ArrayList<Contact> driversList = new ArrayList<Contact>();
        String randFirst = StringsUtils.generateRandomNumberDigits(4);

        driver1 = new Contact("driver-" + randFirst, "motorcycle", Gender.Female, DateUtils.convertStringtoDate("06/01/1980", "MM/dd/YYYY"));
        driversList.add(driver1);


        //Vehicles
        Vin vin = VINHelper.getRandomVIN();
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        vin = VINHelper.getRandomVIN();
        Vehicle veh1 = new Vehicle();
        veh1.setVehicleTypePL(VehicleTypePL.Motorcycle);
        veh1.setVin(vin.getVin());
        veh1.setDriverPL(driver1);
        veh1.setAdditionalLivingExpense(true);

        veh1.setComprehensive(true);
        veh1.setCollision(true);

        vehicleList.add(veh1);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);
        squirePersonalAuto.setVehicleList(vehicleList);
        squirePersonalAuto.setDriversList(driversList);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        squireAutoObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Test", "Auto")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.FullApp);
    }

    @Test(dependsOnMethods = {"testGenerateAutoQQ"})
    public void testAddRentalReimbursement() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(squireAutoObj.agentInfo.getAgentUserName(), squireAutoObj.agentInfo.getAgentPassword(), squireAutoObj.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();

        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_CoverageDetails vehicles = new GenericWorkorderVehicles_CoverageDetails(driver);
        vehicles.clickLinkInVehicleTable("Edit");
        vehicles.clickVehicleCoveragesTab();
        vehicles.setRentalReimbursementDeductible("50");
        vehicles.clickOK();

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();
        new GuidewireHelpers(driver).logout();

    }

    @Test(dependsOnMethods = {"testAddRentalReimbursement"})
    public void testValidateRentalAndLossOfUseByTheftReimbursement() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        squireAutoObj.convertTo(driver, GeneratePolicyType.PolicyIssued);

        new GuidewireHelpers(driver).logout();
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), squireAutoObj.squire.getPolicyNumber());

        PolicySummary summaryPage = new PolicySummary(driver);
        summaryPage.clickCompletedTransactionByType(TransactionType.Issuance);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPADrivers();

        sideMenu.clickSideMenuSquireLineReview();
        GenericWorkorderLineReviewPL lineReview = new GenericWorkorderLineReviewPL(driver);
        if (lineReview.getLineReviewRentalReimbursement() != lineReview.getLineReviewLossOfUseByTheftReimbursement())
            Assert.fail("Loss Of Use By Theft Reimbursement " + lineReview.getLineReviewLossOfUseByTheftReimbursement() + "should match the Rental Reimbursement value " + lineReview.getLineReviewRentalReimbursement());
    }

}

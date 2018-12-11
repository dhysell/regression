package regression.r2.noclock.policycenter.submission_auto.subgroup4;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SRPIncident;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.UnderwriterIssueType;
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
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderInsuranceScore;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_MotorVehicleRecord;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_SelfReportingIncidents;
import repository.pc.workorders.generic.GenericWorkorderVehicles;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandibanda
 * @Requirement : US7028 [Part II] PL - Changes to SRP and US6781 -[Part II] PL - PA - Recalculation of SRP from MVR and CLUE Auto
 * @Requirement : US7503 PL - Aging and Re-calculating SRP
 * @Description Generate Squire Policy, validate SRP Incidents edits in QQ and FA, also check UW blockbind message under Risk Analysis
 * *SRP Override, SRP for New Driver, SRP for driver less vehicle and SRP for assigned/unassigned driver UW issue
 * @DATE Oct 03,2016
 */
@QuarantineClass
public class TestSquireAutoVehicleSRPChanges extends BaseTest {

    public String driverName = null;
    private GeneratePolicy clueAutoPolicy, polToReturn = null;


    private WebDriver driver;

    @Test()
    public void testSRPChanges() throws Exception {
        this.clueAutoPolicy = generatePolicy(GeneratePolicyType.QuickQuote, true, false, false, false, true, true, false);
    }

    //validate SRP Edit check in QQ
    @Test(dependsOnMethods = {"testSRPChanges"})
    public void validateSRPEditsQQ() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(this.clueAutoPolicy.agentInfo.getAgentUserName(), this.clueAutoPolicy.agentInfo.getAgentPassword(), this.clueAutoPolicy.accountNumber);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPADrivers();

        GenericWorkorderSquireAutoDrivers_SelfReportingIncidents squireDriver = new GenericWorkorderSquireAutoDrivers_SelfReportingIncidents(driver);
        squireDriver.clickEditButtonInDriverTable(1);
        squireDriver.clickSRPIncidentsTab();

        if (!squireDriver.checkSRPFieldsEditableQQ()) {
            Assert.fail("SRP Incidents tab fields are not editable in QQ");
        }


        squireDriver.setSRPIncident(SRPIncident.Speed, 2);
        squireDriver.setSRPIncident(SRPIncident.VehicularHomicide, 1);
        squireDriver.clickOk();


        String srpValue = squireDriver.getSRPValue();
        String srpValidationMsg = guidewireHelpers.getErrorMessages().get(0);//driver.getDriverPopupValidationMessage();
        if (!srpValue.contains("Ineligible") && srpValidationMsg.contains("Vehicular Homicide : Driver is ineligible because they have a vehicular homicide."))
            Assert.fail("Driver has SRP value more than 24 : " + squireDriver.getSRPValue());

        squireDriver.clickCancel();
    }

    //Validate SRP Edits in Full App
    @Test(dependsOnMethods = {"validateSRPEditsQQ"})
    public void validateSRPEditsFA() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(this.clueAutoPolicy.agentInfo.getAgentUserName(), this.clueAutoPolicy.agentInfo.getAgentPassword(), this.clueAutoPolicy.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickGenericWorkorderFullApp();

        sideMenu.clickSideMenuQualification();
        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
        qualificationPage.setSquireAutoFullTo(false);
        qualificationPage.setSquireGeneralFullTo(false);

        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.fillOutPolicyInfoPageFA(clueAutoPolicy);
        GenericWorkorderInsuranceScore creditReport = new GenericWorkorderInsuranceScore(driver);
        creditReport.fillOutCreditReporting(clueAutoPolicy);
        sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_ContactDetail squireDriver = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        GenericWorkorderSquireAutoDrivers_SelfReportingIncidents driver_SRP = new GenericWorkorderSquireAutoDrivers_SelfReportingIncidents(driver);
        GenericWorkorderSquireAutoDrivers_MotorVehicleRecord driver_MVR = new GenericWorkorderSquireAutoDrivers_MotorVehicleRecord(driver);
        squireDriver.fillOutDriversFA(clueAutoPolicy);

        for (int i = 1; i <= this.clueAutoPolicy.squire.squirePA.getDriversList().size(); i++) {
            squireDriver.clickEditButtonInDriverTable(i);
            if (i == 1)
                squireDriver.setLicenseNumber("PH105932B");
            if (i == 2)
                squireDriver.setLicenseNumber("GB163157G");
            if (i == 3)
                squireDriver.setLicenseNumber("GB166966C");

            driver_MVR.clickMotorVehicleRecord();
            squireDriver.clickOk();
        }

        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles vehiclePage = new GenericWorkorderVehicles(driver);
        vehiclePage.fillOutVehicles_FA(this.clueAutoPolicy);

        sideMenu.clickSideMenuPADrivers();

        squireDriver.clickEditButtonInDriverTableByDriverName("One");
        squireDriver.clickSRPIncidentsTab();

        boolean editable = driver_SRP.checkSRPFieldsEditableFA();
        if (!editable) {
            Assert.fail("SRP Incidents tab fields are not editable under FA" + editable);
        }

        driver_SRP.setSRPIncident(SRPIncident.UnverifiableDrivingRecord, 1);
        driver_SRP.setSRPIncident(SRPIncident.International, 1);
        driver_SRP.setSRPIncident(SRPIncident.NoProof, 1);
        squireDriver.clickContactDetailTab();
        squireDriver.clickSRPIncidentsTab();
        String srpValue = driver_SRP.getSRPValue();
        if (!srpValue.contains("24")) {
            Assert.fail("Driver has SRP value not 24 : " + driver_SRP.getSRPValue());
        }
        squireDriver.clickOk();
        GenericWorkorder wo = new GenericWorkorder(driver);
        wo.clickGenericWorkorderSaveDraft();

    }

    //Validate Driver less Vehicle SRP
    @Test(dependsOnMethods = {"validateSRPEditsFA"})
    public void validateDriverLessVehicleSRP() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(this.clueAutoPolicy.agentInfo.getAgentUserName(), this.clueAutoPolicy.agentInfo.getAgentPassword(), this.clueAutoPolicy.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles vehiclePage = new GenericWorkorderVehicles(driver);
        String driverStatus = vehiclePage.getVehicleTableCellByColumnName(2, "Driver");
        String vehicleSRPValue = vehiclePage.getVehicleTableCellByColumnName(2, "SRP");

        if (driverStatus.equals("Unassigned")) {
            if (!vehicleSRPValue.equals("3"))
                Assert.fail("Unassigned Driver Vehicle Should have SRP value 3 : " + vehicleSRPValue);
        }

    }

    //Validate SRP calculated based on the information entered in the MVR tab
    @Test(dependsOnMethods = {"validateDriverLessVehicleSRP"})
    public void validateSRPCalculatedBasedOnInformationInMVRTab() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(this.clueAutoPolicy.agentInfo.getAgentUserName(), this.clueAutoPolicy.agentInfo.getAgentPassword(), this.clueAutoPolicy.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_MotorVehicleRecord driver_MVR = new GenericWorkorderSquireAutoDrivers_MotorVehicleRecord(driver);
        GenericWorkorderSquireAutoDrivers_SelfReportingIncidents driver_SRP = new GenericWorkorderSquireAutoDrivers_SelfReportingIncidents(driver);
        driver_MVR.clickEditButtonInDriverTableByDriverName("Two");
        driver_MVR.clickMotorVehicleRecord();
        driver_MVR.selectMVRIncident(1, SRPIncident.ALS);
        driver_MVR.selectMVRIncident(2, SRPIncident.Speed);
        driver_MVR.selectMVRIncident(3, SRPIncident.UnpaidInfraction);
        driver_MVR.clickSRPIncidentsTab();

        //Assign MVR Incidents and check calculation value under SRP Tab
        int SRPValue = driver_SRP.getSRPIncident(SRPIncident.ALS);
        if (SRPValue == 0) {
            Assert.fail("SRP Incidents are not assigned from Driver's Page MVR" + SRPValue);
        }


        String srpValue = driver_SRP.getSRPValue();
        if (srpValue.equals("3")) {
            Assert.fail("SRP Value is not changed Based On the Information In MVR Tab" + SRPValue);
        }

        driver_MVR.clickOk();
    }

    //Validate SRP will be assigned to vehicles based on the assigned driver
    @Test(dependsOnMethods = {"validateSRPCalculatedBasedOnInformationInMVRTab"})
    public void validateVehicleSRPAndAssignedDriverSRPSame() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(this.clueAutoPolicy.agentInfo.getAgentUserName(), this.clueAutoPolicy.agentInfo.getAgentPassword(), this.clueAutoPolicy.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_SelfReportingIncidents squireDriver = new GenericWorkorderSquireAutoDrivers_SelfReportingIncidents(driver);
        squireDriver.clickEditButtonInDriverTableByDriverName("Two");
        squireDriver.clickSRPIncidentsTab();
        squireDriver.setSRPIncident(SRPIncident.International, 1);
        String driverSRPValue = squireDriver.getSRPValue();
        squireDriver.clickOk();
        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles vehiclePage = new GenericWorkorderVehicles(driver);
        String vehicleSRPValue = vehiclePage.getVehicleTableCellByColumnName(1, "SRP");

        if (!driverSRPValue.equals(vehicleSRPValue))
            Assert.fail("Vehicle SRP Value : " + vehicleSRPValue + "Not Same as assigned Driver SRP : " + driverSRPValue);

    }

    //Validate SRP Override in FullApp with UW
    @Test(dependsOnMethods = {"validateRiskAnalysisApprovals"})
    public void validateUWOverrideSRP() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.clueAutoPolicy.accountNumber);
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.clickEditPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPADrivers();

        GenericWorkorderSquireAutoDrivers_SelfReportingIncidents squireDriver = new GenericWorkorderSquireAutoDrivers_SelfReportingIncidents(driver);
        squireDriver.clickEditButtonInDriverTableByDriverName("One");
        squireDriver.clickSRPIncidentsTab();
        squireDriver.setOverrideSRPCheckbox(true);
        squireDriver.setReasonForSRPOverride("Default Reason");
        squireDriver.setSRP(17);
        squireDriver.clickOk();
        int srpValue = squireDriver.getSRPValue("One");

        if (srpValue != 17) {
            Assert.fail("SRP value not changed to 17 : " + srpValue);
        }

        new GuidewireHelpers(driver).logout();
    }

    //check SRP bind message under Risk Analysis
    @Test(dependsOnMethods = {"validateVehicleSRPAndAssignedDriverSRPSame"})
    public void validateRiskAnalysisApprovals() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(this.clueAutoPolicy.agentInfo.getAgentUserName(), this.clueAutoPolicy.agentInfo.getAgentPassword(), this.clueAutoPolicy.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuRiskAnalysis();
        boolean testFailed = false;
        String errorMessage = "";
        GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
        riskAnalysis.Quote();
        sideMenu.clickSideMenuRiskAnalysis();
        String[] expectedUWMessages = {"SRP Greater than 15", "Unassigned driver with higher SRP than assigned driver"};

        FullUnderWriterIssues uwIssues = riskAnalysis.getUnderwriterIssues();

        for (String uwIssue : expectedUWMessages) {
            if (uwIssues.isInList(uwIssue).equals(UnderwriterIssueType.NONE)) {
                testFailed = true;
                errorMessage = errorMessage + "Expected error Bind Issue : " + uwIssue + " is not displayed.";
            }
        }

        if (testFailed)
            Assert.fail(errorMessage);

    }

    private GeneratePolicy generatePolicy(GeneratePolicyType quoteType, boolean person, boolean prefillPersonal, boolean prefillCommercial, boolean insuranceScore, boolean mvr, boolean clueAuto, boolean clueProperty) throws Exception {

        ArrayList<LineSelection> lines = new ArrayList<LineSelection>();
        lines.add(LineSelection.PersonalAutoLinePL);

        Contact driver1, driver2;
        ArrayList<Contact> driversList = new ArrayList<Contact>();
        String randFirst = StringsUtils.generateRandomNumberDigits(4);
        driver1 = new Contact("driver-" + randFirst, "One", Gender.Male, DateUtils.convertStringtoDate("06/01/1982", "MM/dd/YYYY"));
        driver2 = new Contact("driver-" + randFirst, "Two", Gender.Female, DateUtils.convertStringtoDate("06/01/1983", "MM/dd/YYYY"));
        driversList.add(driver1);
        driversList.add(driver2);

        ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
        Vehicle vehicle1 = new Vehicle();
        vehicle1.setVehicleTypePL(VehicleTypePL.PrivatePassenger);
        vehicle1.setDriverPL(driver1);

        Vehicle vehicle2 = new Vehicle();
        vehicle2.setVehicleTypePL(VehicleTypePL.Motorcycle);
        vehicle1.setDriverPL(driver2);

        vehicles.add(vehicle1);
        vehicles.add(vehicle2);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.TwentyFiveHigh, MedicalLimit.FiveK);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setVehicleList(vehicles);
        squirePersonalAuto.setDriversList(driversList);
        squirePersonalAuto.setCoverages(coverages);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        this.polToReturn = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withLexisNexisData(person, prefillPersonal, prefillCommercial, insuranceScore, mvr, clueAuto, clueProperty)
                .build(quoteType);
        return this.polToReturn;
    }
}

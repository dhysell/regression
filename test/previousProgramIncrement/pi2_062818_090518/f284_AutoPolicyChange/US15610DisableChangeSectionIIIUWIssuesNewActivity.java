package previousProgramIncrement.pi2_062818_090518.f284_AutoPolicyChange;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.NotRatedReason;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PLUWIssues;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RelationshipsAB;
import repository.gw.enums.SRPIncident;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.UnderwriterIssueType;
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
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_ExclusionsConditions;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_MotorVehicleRecord;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_SelfReportingIncidents;
import repository.pc.workorders.generic.GenericWorkorderVehicles_CoverageDetails;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import repository.pc.workorders.generic.GenericWorkorderVehicles_ExclusionsAndConditions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class US15610DisableChangeSectionIIIUWIssuesNewActivity extends BaseTest {
    private WebDriver driver;
    public GeneratePolicy myPolicyObjPL = null;
    private String notRatedDriver = "NotRate" + StringsUtils.generateRandomNumberDigits(7);
    private String seniorDiscount = "Senior" + StringsUtils.generateRandomNumberDigits(7);
   
    @Test
    public void testCheckAutoUWIssuesInSubmission() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        SoftAssert softAssert = new SoftAssert();

        // driver
        ArrayList<Contact> driversList = new ArrayList<Contact>();
        Contact student = new Contact();
        student.setFirstName("Student" + StringsUtils.generateRandomNumberDigits(7));
        student.setLastName("Discount" + StringsUtils.generateRandomNumberDigits(7));
        student.setDob(driver, DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter),
                DateAddSubtractOptions.Year, -15));
        student.setRelationshipAB(RelationshipsAB.ChildWard);
        Contact notRated = new Contact();
        notRated.setFirstName(notRatedDriver);
        notRated.setLastName("Discoun" + StringsUtils.generateRandomNumberDigits(7));
        notRated.setRelationshipAB(RelationshipsAB.AffiliateTo);
        Contact senior = new Contact();
        senior.setFirstName(seniorDiscount);
        senior.setLastName("Discount" + StringsUtils.generateRandomNumberDigits(7));
        senior.setDob(driver, DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter),
                DateAddSubtractOptions.Year, -60));
        senior.setRelationshipAB(RelationshipsAB.ParentGuardian);
        driversList.add(student);
        driversList.add(notRated);
        driversList.add(senior);

        // vehicle
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle vehicle = new Vehicle(VehicleTypePL.PrivatePassenger, "make new vin", 2003, "Oldie", "But Goodie");
        // AU016 Show car exists
        Vehicle vehicle1 = new Vehicle(VehicleTypePL.ShowCar, "make new vin", 2003, "Oldie", "But Goodie");
        vehicle1.setComprehensive(true);
        vehicle1.setCollision(true);
        // AU023 Farm truck added
        Vehicle vehicle2 = new Vehicle(VehicleTypePL.FarmTruck, "make new vin", 2003, "Oldie", "But Goodie");
        Vehicle vehicle3 = new Vehicle(VehicleTypePL.PassengerPickup, "make new vin", 2003, "Oldie", "But Goodie");
        vehicle3.setDriverPL(student);
        Vehicle vehicle4 = new Vehicle(VehicleTypePL.Motorcycle, "make new vin", 2003, "Oldie", "But Goodie");
        vehicle4.setDriverPL(senior);

        vehicleList.add(vehicle);
        vehicleList.add(vehicle1);
        vehicleList.add(vehicle2);
        vehicleList.add(vehicle3);
        vehicleList.add(vehicle4);

        // line auto coverages
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow,
                MedicalLimit.TenK);
        coverages.setUnderinsured(false);

        // whole auto line
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto(driversList, vehicleList, coverages);
        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        myPolicyObjPL = new GeneratePolicy.Builder(driver).withSquire(mySquire).withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL).withInsFirstLastName("Sub", "UWIssue").isDraft()
                .withPaymentPlanType(PaymentPlanType.Annual).withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.FullApp);

        new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(),
                myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();

        // AU022 Good Student Discount
        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        vehiclePage.editVehicleByType(VehicleTypePL.PassengerPickup);
        vehiclePage.setGoodStudentDiscountRadio(true);
        vehiclePage.clickOK();

        // AU038 Farm Trucks over 50,000 GVW
        vehiclePage.editVehicleByType(VehicleTypePL.FarmTruck);
        vehiclePage.selectUsage(Usage.FarmUse);
        vehiclePage.selectTruckType(VehicleTruckTypePL.OneToSix);
        vehiclePage.setGVW(56000);
        vehiclePage.setOdometer(20000);
        vehiclePage.clickOK();

        // AU033 Motorcycle Discount and New Business
        vehiclePage.editVehicleByType(VehicleTypePL.Motorcycle);
        vehiclePage.setMotorCyclesStarDiscountRadio(true);
        vehiclePage.clickOK();

        // AU054 55 and Alive discount
        vehiclePage.editVehicleByType(VehicleTypePL.PrivatePassenger);
        vehiclePage.selectDriverToAssign(seniorDiscount);
        vehiclePage.setSeniorDiscountRadio(true);
        vehiclePage.clickOK();

        vehiclePage.editVehicleByType(VehicleTypePL.ShowCar);
        vehiclePage.setOdometer(20000);
        vehiclePage.clickOK();

        // AU028 Business use and Other
        sideMenu.clickSideMenuQualification();
        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
        qualificationPage.clickPL_Business(true);
        qualificationPage.checkBusinessPurposeUsedVehicle("Contractor");
        qualificationPage.clickPL_Employees(false);
        qualificationPage.clickQualificationNext();
        qualificationPage.clickQualificationNext();

        // AU050 Not-rated drivers
        sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(
                driver);

        paDrivers.clickEditButtonInDriverTableByDriverName(notRatedDriver);
        paDrivers.setNotRatedCheckbox(true);
        paDrivers.selectNotRatedReason(NotRatedReason.ExchangeStudent);
        paDrivers.clickOk();

        // AU103 "If "No VIN" checkbox is checked on Vehicles screen"
        sideMenu.clickSideMenuPAVehicles();
        vehiclePage.editVehicleByType(VehicleTypePL.PrivatePassenger);
        vehiclePage.setNOVIN(true);
        vehiclePage.clickOK();

        // AU064 310 added or removed
        GenericWorkorderVehicles_ExclusionsAndConditions vehicleExclusions = new GenericWorkorderVehicles_ExclusionsAndConditions(
                driver);
        sideMenu.clickSideMenuPAVehicles();
        vehiclePage.editVehicleByType(VehicleTypePL.PassengerPickup);
        Vehicle Vehicle = new Vehicle();
        Vehicle.setLocationOfDamage("Left Rear");
        Vehicle.setDamageItem("Tail Light");
        Vehicle.setDamageDescription("Missing");
        vehicleExclusions.fillOutExclusionsAndConditions(Vehicle);
        vehiclePage.clickOK();

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);

        risk.Quote();
        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
        if (quotePage.isPreQuoteDisplayed()) {
            quotePage.clickPreQuoteDetails();
        }

        sideMenu.clickSideMenuRiskAnalysis();
        FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();
        @SuppressWarnings("serial")
        List<PLUWIssues> sectionIIIBlockInfo = new ArrayList<PLUWIssues>() {
            {
                this.add(PLUWIssues.ShowCarExists);
                this.add(PLUWIssues.GoodStudentDiscount);
                this.add(PLUWIssues.MotorCycleDiscountNewBusiness);
                this.add(PLUWIssues.NotRatedDriver);
                this.add(PLUWIssues.Auto55AndAliveDiscount);
                this.add(PLUWIssues.AddedRemoved310);
                this.add(PLUWIssues.NoVIN);
                this.add(PLUWIssues.PolicyHasMotorCycleAndDriverLessThan25);
                this.add(PLUWIssues.FarmTruckOver50K);
            }
        };

        @SuppressWarnings("serial")
        List<PLUWIssues> sectionIIIBlockSubmit = new ArrayList<PLUWIssues>() {
            {
                this.add(PLUWIssues.CitySquireWithFarmTruck);
                this.add(PLUWIssues.VehicleUsedForBusiness);
            }
        };

        for (PLUWIssues uwBlockBindExpected : sectionIIIBlockInfo) {
            softAssert.assertFalse(
                    !uwIssues.isInList(uwBlockBindExpected.getLongDesc()).equals(UnderwriterIssueType.Informational),
                    "Expected UW Informational : " + uwBlockBindExpected.getShortDesc() + " is not displayed");
        }

        for (PLUWIssues uwBlockBindExpected : sectionIIIBlockSubmit) {
            softAssert.assertFalse(
                    !uwIssues.isInList(uwBlockBindExpected.getLongDesc()).equals(UnderwriterIssueType.BlockSubmit),
                    "Expected Blocking Submit : " + uwBlockBindExpected.getShortDesc() + " is not displayed");
        }

        softAssert.assertAll();

    }

    @Test
    public void testSquireAutoMVRCLUE() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // Vehicle
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle newVeh = new Vehicle();
        newVeh.setEmergencyRoadside(true);
        vehicleList.add(newVeh);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh,
                MedicalLimit.FiveK);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);
        squirePersonalAuto.setVehicleList(vehicleList);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        // AU032 New business, Claims >= 2
        GeneratePolicy clueMVRPol = new GeneratePolicy.Builder(driver).withSquire(mySquire)
                .withProductType(ProductLineType.Squire).withLineSelection(LineSelection.PersonalAutoLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .isDraft()
                .withLexisNexisData(true, false, false, false, true, true, false).build(GeneratePolicyType.FullApp);

        new Login(driver).loginAndSearchSubmission(clueMVRPol.agentInfo.getAgentUserName(), clueMVRPol.agentInfo.getAgentPassword(),
                clueMVRPol.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);

        // AU083 - SRP >= 16
        sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_SelfReportingIncidents paDrivers = new GenericWorkorderSquireAutoDrivers_SelfReportingIncidents(
                driver);
        paDrivers.clickEditButtonInDriverTable(1);
        GenericWorkorderSquireAutoDrivers_ContactDetail driverContact = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        driverContact.setLicenseNumber("XW304757A");
        paDrivers.clickSRPIncidentsTab();
        paDrivers.setNoProofInsuranceIncident(1);
        paDrivers.setInternationalDLIncident(1);

        // AU084 Non Allowed Tickets
        paDrivers.clickMotorVehicleRecord();
        GenericWorkorderSquireAutoDrivers_MotorVehicleRecord mvrDrivers = new GenericWorkorderSquireAutoDrivers_MotorVehicleRecord(
                driver);
        paDrivers.clickOrderMVR();
        mvrDrivers.selectMVRIncident(1, SRPIncident.ALS);
        paDrivers.clickOk();

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);

        risk.Quote();
        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
        if (quotePage.isPreQuoteDisplayed()) {
            quotePage.clickPreQuoteDetails();
        }

        sideMenu.clickSideMenuRiskAnalysis();
        FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();

        @SuppressWarnings("serial")
        List<PLUWIssues> sectionIIIBlockInfo = new ArrayList<PLUWIssues>() {
            {
                this.add(PLUWIssues.SRPGreaterThan15);
                this.add(PLUWIssues.NonAllowedTickets);
            }
        };
        SoftAssert softAssert = new SoftAssert();

        for (PLUWIssues uwBlockBindExpected : sectionIIIBlockInfo) {
            softAssert.assertFalse(
                    !uwIssues.isInList(uwBlockBindExpected.getLongDesc()).equals(UnderwriterIssueType.Informational),
                    "Expected Informational : " + uwBlockBindExpected.getShortDesc() + " is not displayed");
        }
        softAssert.assertAll();

    }

    @Test
    public void testPolicyChangeSectionIIIUWInformational() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();

        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.squirePA = squirePersonalAuto;

        GeneratePolicy myPolObjPL = new GeneratePolicy.Builder(driver).withSquire(mySquire)
                .withProductType(ProductLineType.Squire).withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Rules", "SectionIII").withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash).build(GeneratePolicyType.PolicyIssued);
        new Login(driver).loginAndSearchPolicyByAccountNumber(myPolObjPL.agentInfo.getAgentUserName(),
                myPolObjPL.agentInfo.getAgentPassword(), myPolObjPL.accountNumber);

        // start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.startPolicyChange("First policy Change",
                DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));

        SideMenuPC sideMenu = new SideMenuPC(driver);

        // AU058 Change to driver info
        sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_SelfReportingIncidents paDrivers = new GenericWorkorderSquireAutoDrivers_SelfReportingIncidents(
                driver);
        paDrivers.clickEditButtonInDriverTableByDriverName(myPolObjPL.pniContact.getFirstName());
        GenericWorkorderSquireAutoDrivers_ContactDetail driverContact = new GenericWorkorderSquireAutoDrivers_ContactDetail(
                driver);
        Date newDOB = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter),
                DateAddSubtractOptions.Year, -25);
        driverContact.setAutoDriversDOB(newDOB);
        paDrivers.clickOK();

        // AU060 Line level coverage reduced or removed.
        // AU072 Auto Additional insured change
        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        vehiclePage.editVehicleByRowNumber(1);
        vehiclePage.clickVehicleDetailsTab();
        vehiclePage.addAutoAdditionalInsured("Testing purpose");
        vehiclePage.setFactoryCostNew(20000);

        // AU061 Added or removed condition 301
        Vehicle Vehicle = new Vehicle();
        Vehicle.setLocationOfDamage("Left Rear");
        Vehicle.setDamageItem("Tail Light");
        Vehicle.setDamageDescription("Missing");
        GenericWorkorderVehicles_ExclusionsAndConditions vehicleExclusions = new GenericWorkorderVehicles_ExclusionsAndConditions(
                driver);
        vehicleExclusions.fillOutExclusionsAndConditions(Vehicle);
        vehiclePage.selectVehicleCoverageDetailsTab();
        GenericWorkorderVehicles_CoverageDetails vehicleCoverages = new GenericWorkorderVehicles_CoverageDetails(driver);
        vehicleCoverages.setComprehensive(true);
        vehicleCoverages.setCollision(true);

        vehiclePage.clickOK();

        sideMenu.clickSideMenuQualification();
        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
        qualificationPage.clickPL_Hagerty(true);
        qualificationPage.setHagertyVehicleList("Veh1");
        sideMenu.clickSideMenuPACoverages();
        GenericWorkorderSquireAutoCoverages_Coverages coveragePage = new GenericWorkorderSquireAutoCoverages_Coverages(driver);
        coveragePage.setUninsuredCoverage(false, null);
        coveragePage.clickCoverageExclusionsTab();
        GenericWorkorderSquireAutoCoverages_ExclusionsConditions paExclusions = new GenericWorkorderSquireAutoCoverages_ExclusionsConditions(driver);
        paExclusions.addNewModificationOfInsuredvehicleDefinitionEndorsement("BMW", "Beemer", "1B45678645456", "1999");


        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);

        risk.Quote();
        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
        if (quotePage.isPreQuoteDisplayed()) {
            quotePage.clickPreQuoteDetails();
        }

        sideMenu.clickSideMenuRiskAnalysis();
        FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();

        @SuppressWarnings("serial")
        List<PLUWIssues> sectionIIIBlockInformation = new ArrayList<PLUWIssues>() {
            {
                this.add(PLUWIssues.ChangeToDriverInfo);
                this.add(PLUWIssues.LineLevelCoverageReducedRemoved);
                this.add(PLUWIssues.AddedRemoved310);
                this.add(PLUWIssues.AdditionalInsuredChange);
                this.add(PLUWIssues.AddedOrRemovedCondition301);
            }
        };
        SoftAssert softAssert = new SoftAssert();

        for (PLUWIssues uwBlockBindExpected : sectionIIIBlockInformation) {
            softAssert.assertFalse(
                    !uwIssues.isInList(uwBlockBindExpected.getLongDesc()).equals(UnderwriterIssueType.Informational),
                    "Expected Informational : " + uwBlockBindExpected.getShortDesc() + " is not displayed");
        }
        softAssert.assertAll();
    }

}

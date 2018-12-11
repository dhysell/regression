package previousProgramIncrement.pi2_062818_090518.f284_AutoPolicyChange;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.Gender;
import com.idfbins.enums.State;
import repository.driverConfiguration.Config;
import repository.gw.enums.CommuteType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.HowLongWithoutCoverage;
import repository.gw.enums.LineSelection;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PLUWIssues;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SRPIncident;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.Squire;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderLineSelection;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_MotorVehicleRecord;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;


/**
 * @Author nvadlamudi
 * @Requirement :US15805:Disable/change UW Issues - Section 3
 * @RequirementsLink <a href="https://fbmicoi.sharepoint.com/:x:/s/ARTists2/EfkD5tWiAwlAq85ITIpy3M8B9Hg5URkky9PGfOGjeHJQnA?e=u0CR2b">UW Issues to Change for Auto Issue</a>
 * @Description: Checking Section III UW Issues and checking the changes are working.
 * @DATE Jul 23, 2018
 */
public class US15805DisabledChangeSectionIIIUWIssues2 extends BaseTest {
    private WebDriver driver;
    SoftAssert softAssert = new SoftAssert();

//    @Test
//    public void testCheckAutoUWIssuesInSubmission() throws Exception {
//        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
//        driver = buildDriver(cf);
//
//        // driver
//        ArrayList<Contact> driversList = new ArrayList<Contact>();
//        Contact student = new Contact();
//        driversList.add(student);
//
//        // vehicle
//        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
//        Vehicle vehicle = new Vehicle(VehicleTypePL.PrivatePassenger, "make new vin", 2003, "Oldie", "But Goodie");
//        Vehicle vehicle2 = new Vehicle(VehicleTypePL.FarmTruck, "make new vin", 2003, "Oldie", "But Goodie");
//        Vehicle vehicle3 = new Vehicle(VehicleTypePL.Motorcycle, "make new vin", 2003, "Oldie", "But Goodie");
//
//        vehicleList.add(vehicle);
//        vehicleList.add(vehicle2);
//        vehicleList.add(vehicle3);
//
//        // line auto coverages
//        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow,
//                MedicalLimit.TenK);
//        coverages.setUnderinsured(false);
//
//        // whole auto line
//        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto(driversList, vehicleList, coverages);
//        Squire mySquire = new Squire(SquireEligibility.City);
//        mySquire.squirePA = squirePersonalAuto;
//
//        GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver).withSquire(mySquire).withProductType(ProductLineType.Squire)
//                .withLineSelection(LineSelection.PersonalAutoLinePL).withInsFirstLastName("Sub2", "UWIssue").isDraft()
//                .withPaymentPlanType(PaymentPlanType.Annual).withDownPaymentType(PaymentType.Cash)
//                .build(GeneratePolicyType.FullApp);
//
//        new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(),
//                myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);
//        new GuidewireHelpers(driver).editPolicyTransaction();
//        SideMenuPC sideMenu = new SideMenuPC(driver);
//        sideMenu.clickSideMenuPolicyInfo();
//
//        // AU012 No Private Passenger or Pickups
//        sideMenu.clickSideMenuPAVehicles();
//        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
//        vehiclePage.removeVehicleByVehicleNumber(1);
//
//        // AU043 Usage and Truck type for Semi-Trailer
//        vehiclePage.editVehicleByType(VehicleTypePL.FarmTruck);
//        vehiclePage.selectUsage(Usage.FarmUseWithOccasionalHire);
//        vehiclePage.selectTruckType(VehicleTruckTypePL.UnderFour);
//        vehiclePage.setOdometer(20000);
//        vehiclePage.clickOK();
//
//        vehiclePage.createVehicleManually();
//        vehiclePage.createGenericVehicle(VehicleTypePL.SemiTrailer);
//        vehiclePage.selectUsage(Usage.FarmUse);
//        vehiclePage.selectTruckType(VehicleTruckTypePL.TractorType);
//        vehiclePage.selectTrailer(TrailerTypePL.Semi);
//        vehiclePage.selectGaragedAtZip("ID");
//        vehiclePage.clickOK();
//
//        // AU086 Higher SRP unassigned
//        sideMenu.clickSideMenuPADrivers();
//        GenericWorkorderSquireAutoDrivers_SelfReportingIncidents paDrivers = new GenericWorkorderSquireAutoDrivers_SelfReportingIncidents(
//                driver);
//        paDrivers.clickEditButtonInDriverTableByDriverName(
//                myPolicyObjPL.squire.squirePA.getDriversList().get(0).getFirstName());
//        paDrivers.clickSRPIncidentsTab();
//        paDrivers.setNoProofInsuranceIncident(1);
//        paDrivers.setInternationalDLIncident(1);
//        paDrivers.setUnverifiableDrivingRecordIncident(1);
//        paDrivers.clickOk();
//
//        // AU091 CLUE Auto not ordered
//        sideMenu.clickSideMenuRiskAnalysis();
//        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
//
//        risk.Quote();
//        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
//        if (quotePage.isPreQuoteDisplayed()) {
//            quotePage.clickPreQuoteDetails();
//        }
//
//        sideMenu.clickSideMenuRiskAnalysis();
//        FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();
//        @SuppressWarnings("serial")
//        List<PLUWIssues> sectionIIIBlockQuote = new ArrayList<PLUWIssues>() {
//            {
//                this.add(PLUWIssues.NoPrivatePassenferPickup);
//                this.add(PLUWIssues.UnassignedDriverWithHighSRP);
//                this.add(PLUWIssues.CLUEAutoNotOrdered);
//            }
//        };
//
//        PLUWIssues sectionIIIBlockSubmit = PLUWIssues.UsageAndTruckTypeSemiTrailer;
//
//        for (PLUWIssues uwBlockBindExpected : sectionIIIBlockQuote) {
//            softAssert.assertFalse(
//                    !uwIssues.isInList(uwBlockBindExpected.getLongDesc()).equals(UnderwriterIssueType.BlockQuote),
//                    "Expected Blocking Issuance : " + uwBlockBindExpected.getShortDesc() + " is not displayed");
//        }
//
//        softAssert.assertFalse(
//                !uwIssues.isInList(sectionIIIBlockSubmit.getLongDesc()).equals(UnderwriterIssueType.BlockSubmit),
//                "Expected Blocking Issuance : " + sectionIIIBlockSubmit.getShortDesc() + " is not displayed");
//
//        softAssert.assertAll();
//
//    }

    @Test
    public void testPolicyChangeForCLUEAUTO() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Squire mySquire = new Squire(SquireEligibility.City);

        GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver).withSquire(mySquire)
                .withProductType(ProductLineType.Squire).withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(OrganizationType.Individual).withLexisNexisData(true, false, false, false, true, false, false)
                .withPaymentPlanType(PaymentPlanType.Annual).withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObjPL.agentInfo.getAgentUserName(),
                myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);

        // start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.startPolicyChange("First policy Change",
                DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuLineSelection();
        GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(driver);
        lineSelection.checkPersonalAutoLine(true);
        sideMenu.clickSideMenuQualification();
        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
        qualificationPage.setSquireAutoFullTo(false);
        qualificationPage.setSquireGeneralFullTo(false);
        sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        paDrivers.addExistingDriver(myPolicyObjPL.pniContact.getLastName());
        paDrivers.selectMaritalStatus(MaritalStatus.Single);
        paDrivers.selectGender(Gender.Male);
        paDrivers.setOccupation("Testing");
        paDrivers.setLicenseNumber("XW304757A");
        State state = State.Idaho;
        paDrivers.selectLicenseState(state);
        paDrivers.selectCommuteType(CommuteType.WorkFromHome);
        paDrivers.setPhysicalImpairmentOrEpilepsy(false);
        paDrivers.selectDriverHaveCurrentInsurance(false);
        paDrivers.setHowLongWithoutCoverage(HowLongWithoutCoverage.NewDriver);
        paDrivers.clickOrderMVR();
        paDrivers.clickMotorVehicleRecord();
        GenericWorkorderSquireAutoDrivers_MotorVehicleRecord motorRecord = new GenericWorkorderSquireAutoDrivers_MotorVehicleRecord(driver);
        motorRecord.selectMVRIncident(1, SRPIncident.Waived);
        // AU106 Felony Manslaughter   - Defect DE7717  - will comment the below line and test SRP - UW Issue
        //motorRecord.selectMVRIncident(1, SRPIncident.FelonyManslaughter);
        paDrivers.clickOK();

        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehicle = new GenericWorkorderVehicles_Details(driver);
        vehicle.createVehicleManually();
        vehicle.createGenericVehicle(VehicleTypePL.PrivatePassenger);
        vehicle.selectGaragedAtZip("ID");
        vehicle.selectDriverToAssign(myPolicyObjPL.pniContact.getLastName());
        vehicle.clickOK();

        sideMenu.clickSideMenuPACoverages();
        GenericWorkorderSquireAutoCoverages_Coverages coveragePage = new GenericWorkorderSquireAutoCoverages_Coverages(driver);
        coveragePage.setUninsuredCoverage(false, null);
        coveragePage.setUnderinsuredCoverage(false, null);

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);

        risk.Quote();
        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
        if (quotePage.isPreQuoteDisplayed()) {
            quotePage.clickPreQuoteDetails();
        }

        sideMenu.clickSideMenuRiskAnalysis();
        FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();

        PLUWIssues sectionIIIBlockQuote = PLUWIssues.CLUEAutoNotOrdered;
        softAssert.assertFalse(
                !uwIssues.isInList(sectionIIIBlockQuote.getLongDesc()).equals(UnderwriterIssueType.BlockQuote),
                "Expected Blocking Quote : " + sectionIIIBlockQuote.getShortDesc() + " is not displayed");

        softAssert.assertAll();

    }

//    @Test
//    public void testCheckAutoUWIssuesInPolicyChange() throws Exception {
//        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
//        driver = buildDriver(cf);
//
//        // driver
//        ArrayList<Contact> driversList = new ArrayList<Contact>();
//        Contact student = new Contact();
//        driversList.add(student);
//
//        // vehicle
//        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
//        Vehicle vehicle = new Vehicle(VehicleTypePL.PrivatePassenger, "make new vin", 2003, "Oldie", "But Goodie");
//        Vehicle vehicle3 = new Vehicle(VehicleTypePL.Motorcycle, "make new vin", 2003, "Oldie", "But Goodie");
//
//        vehicleList.add(vehicle);
//        vehicleList.add(vehicle3);
//
//        // line auto coverages
//        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow,
//                MedicalLimit.TenK);
//        coverages.setUnderinsured(false);
//
//        // whole auto line
//        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto(driversList, vehicleList, coverages);
//        Squire mySquire = new Squire(SquireEligibility.City);
//        mySquire.squirePA = squirePersonalAuto;
//
//        GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver).withSquire(mySquire).withProductType(ProductLineType.Squire)
//                .withLineSelection(LineSelection.PersonalAutoLinePL).withInsFirstLastName("Sub2", "UWIssue").isDraft()
//                .withPaymentPlanType(PaymentPlanType.Annual).withDownPaymentType(PaymentType.Cash)
//                .build(GeneratePolicyType.PolicyIssued);
//
//        new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObjPL.agentInfo.getAgentUserName(),
//                myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);
//
//        // start policy change
//        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
//        policyChangePage.startPolicyChange("First policy Change",
//                DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));
//
//        SideMenuPC sideMenu = new SideMenuPC(driver);
//
//        // AU012 No Private Passenger or Pickups
//        sideMenu.clickSideMenuPAVehicles();
//        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
//        vehiclePage.removeVehicleByVehicleNumber(1);
//
//        // AU043 Usage and Truck type for Semi-Trailer
//        vehiclePage.createVehicleManually();
//        vehiclePage.createGenericVehicle(VehicleTypePL.FarmTruck);
//        vehiclePage.selectUsage(Usage.FarmUseWithOccasionalHire);
//        vehiclePage.selectTruckType(VehicleTruckTypePL.UnderFour);
//        vehiclePage.setOdometer(20000);
//        vehiclePage.selectGaragedAtZip("ID");
//        vehiclePage.clickOK();
//
//        vehiclePage.createVehicleManually();
//        vehiclePage.createGenericVehicle(VehicleTypePL.SemiTrailer);
//        vehiclePage.selectUsage(Usage.FarmUse);
//        vehiclePage.selectTruckType(VehicleTruckTypePL.TractorType);
//        vehiclePage.selectTrailer(TrailerTypePL.Semi);
//        vehiclePage.selectGaragedAtZip("ID");
//        vehiclePage.clickOK();
//
//        // AU086 Higher SRP unassigned
//        sideMenu.clickSideMenuPADrivers();
//        GenericWorkorderSquireAutoDrivers_SelfReportingIncidents paDrivers = new GenericWorkorderSquireAutoDrivers_SelfReportingIncidents(
//                driver);
//        paDrivers.clickEditButtonInDriverTableByDriverName(
//                myPolicyObjPL.squire.squirePA.getDriversList().get(0).getFirstName());
//        paDrivers.clickSRPIncidentsTab();
//        paDrivers.setNoProofInsuranceIncident(1);
//        paDrivers.setInternationalDLIncident(1);
//        paDrivers.setUnverifiableDrivingRecordIncident(1);
//        paDrivers.clickOk();
//
//        // AU091 CLUE Auto not ordered
//
//        sideMenu.clickSideMenuRiskAnalysis();
//        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
//
//        risk.Quote();
//        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
//        if (quotePage.isPreQuoteDisplayed()) {
//            quotePage.clickPreQuoteDetails();
//        }
//
//        sideMenu.clickSideMenuRiskAnalysis();
//        FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();
//
//        @SuppressWarnings("serial")
//        List<PLUWIssues> sectionIIIBlockQuote = new ArrayList<PLUWIssues>() {
//            {
//                this.add(PLUWIssues.NoPrivatePassenferPickup);
//                this.add(PLUWIssues.UnassignedDriverWithHighSRP);
//            }
//        };
//
//        PLUWIssues sectionIIIBlockSubmit = PLUWIssues.UsageAndTruckTypeSemiTrailer;
//
//        for (PLUWIssues uwBlockBindExpected : sectionIIIBlockQuote) {
//            softAssert.assertFalse(
//                    !uwIssues.isInList(uwBlockBindExpected.getLongDesc()).equals(UnderwriterIssueType.BlockQuote),
//                    "Expected Blocking Issuance : " + uwBlockBindExpected.getShortDesc() + " is not displayed");
//        }
//
//        softAssert.assertFalse(
//                !uwIssues.isInList(sectionIIIBlockSubmit.getLongDesc()).equals(UnderwriterIssueType.BlockSubmit),
//                "Expected Blocking Issuance : " + sectionIIIBlockSubmit.getShortDesc() + " is not displayed");
//
//        softAssert.assertAll();
//
//    }
}

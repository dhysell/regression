package regression.r2.noclock.policycenter.renewaltransition.subgroup7;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.CommuteType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.NotRatedReason;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.TransactionType;
import repository.gw.enums.Vehicle.DriverVehicleUsePL;
import repository.gw.enums.Vehicle.MileageFactor;
import repository.gw.enums.Vehicle.OccasionalDriverReason;
import repository.gw.enums.Vehicle.TrailerTypePL;
import repository.gw.enums.Vehicle.Usage;
import repository.gw.enums.Vehicle.VehicleTruckTypePL;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_AdditionalCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_ExclusionsConditions;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_SelfReportingIncidents;
import repository.pc.workorders.generic.GenericWorkorderSquireCLUEAuto;
import repository.pc.workorders.generic.GenericWorkorderVehicles_CoverageDetails;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import repository.pc.workorders.generic.GenericWorkorderVehicles_ExclusionsAndConditions;
import repository.pc.workorders.renewal.RenewalInformation;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : US6467: Section III Renewal Business rules
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Jul 4, 2016
 */
@QuarantineClass
public class TestSquireSectionIIIRenewalBusinessRules extends BaseTest {
    private GeneratePolicy myPolicyObjPL;
    String randFirst = StringsUtils.generateRandomNumberDigits(6);

    private WebDriver driver;

    private GeneratePolicy clueMVRPol;
    private Underwriters uw;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
    }


    @Test(dependsOnMethods = {"testSquireAutoMVRCLUE"})
    private void testMVRBusinessRules() throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        // Login with UW
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                clueMVRPol.accountNumber);

        StartRenewal renewal = new StartRenewal(driver);
        renewal.startRenewal();

        InfoBar infoBar = new InfoBar(driver);
        infoBar.clickInfoBarPolicyNumber();

        PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
        preRenewalPage.closePreRenewalExplanations(clueMVRPol);

        PolicySummary policySummary = new PolicySummary(driver);
        policySummary.clickPendingTransaction(TransactionType.Renewal);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();
        String[] waivedUWIssues = {"Waived MVR incident", "Not valid license on MVR"};

        validateRiskAnalysisApprovals(waivedUWIssues);
        sideMenu.clickSideMenuPolicyInfo();
        guidewireHelpers.editPolicyTransaction();
        sideMenu.clickSideMenuClueAuto();
        GenericWorkorderSquireCLUEAuto clueAuto = new GenericWorkorderSquireCLUEAuto(driver);
        clueAuto.selectClueAutoLossesCheckBoxByRow(1);
        clueAuto.clickIncludeExclude();
        String[] excludedUWIssues = {"CLUE Auto claim excluded"};
        validateRiskAnalysisApprovals(excludedUWIssues);

    }

    @Test()
    private void testSquireAutoMVRCLUE() throws Exception {

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

        clueMVRPol = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withLexisNexisData(true, false, false, false, true, true, false)
                .withPolTermLengthDays(79)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"createPLAutoPolicy"})
    public void testSectionThreeRenewalBusinessRules() throws Exception {
        // Login with UW
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                myPolicyObjPL.accountNumber);

        StartRenewal renewal = new StartRenewal(driver);
        renewal.startRenewal();

        InfoBar infoBar = new InfoBar(driver);
        infoBar.clickInfoBarPolicyNumber();

        PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
        preRenewalPage.closePreRenewalExplanations(myPolicyObjPL);

        PolicySummary policySummary = new PolicySummary(driver);
        policySummary.clickPendingTransaction(TransactionType.Renewal);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickRenewalInformation();
        RenewalInformation renewalInfo = new RenewalInformation(driver);
        renewalInfo.setRenewalOdometerByVehicleNumber(2, "2100");

        // Policies with no Private Passenger or Passenger Pickup, create UW
        // issue & Any policy with a show car, block issue and create UW issue
        changeToShowCarAndValidate();

        // Adding no liability vehicle
        addVehicleWithNoLiability();

        // adding more vehicles and drivers
        addingBlockBindIssuesQuoteScenarios();
    }

    private void addVehicleWithNoLiability() throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        GenericWorkorderVehicles_CoverageDetails vehicleCoverages = new GenericWorkorderVehicles_CoverageDetails(driver);


        // Adding Vehicles for Student Discount
        vehiclePage.createVehicleManually();
        vehiclePage.createGenericVehicle(VehicleTypePL.PassengerPickup);
        vehiclePage.selectDriverToAssign(this.myPolicyObjPL.squire.squirePA.getDriversList().get(0));
        vehiclePage.setVIN("ABCD4543543");
        vehiclePage.setModelYear(2000);
        vehiclePage.setMake("Honda");
        vehiclePage.setModel("Accord");
        vehiclePage.setFactoryCostNew(20000);
        vehiclePage.selectGaragedAtZip(myPolicyObjPL.pniContact.getAddress().getCity());
        vehicleCoverages.selectVehicleCoverageDetailsTab();
        vehicleCoverages.setRadioLiabilityCoverage(false);
        vehicleCoverages.setComprehensive(true);
        vehicleCoverages.clickOK();
        String[] newUWIssues = {"No vehicles with liability", "Invalid VIN"};
        validateRiskAnalysisApprovals(newUWIssues);
        sideMenu.clickSideMenuPolicyInfo();
        sideMenu.clickSideMenuPAVehicles();
    }

    private void changeToShowCarAndValidate() throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPAVehicles();

        boolean testFailed = false;
        String errorMessage = "";

        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        GenericWorkorderVehicles_CoverageDetails vehicleCoverages = new GenericWorkorderVehicles_CoverageDetails(driver);

        vehiclePage.editVehicleByVehicleNumber(2);
        vehiclePage.setOdometer(2100);
        vehiclePage.clickOK();

        String[] showCarMileage = {"Show Car Mileage over 1000"};
        validateRiskAnalysisApprovals(showCarMileage);
        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();
        sideMenu.clickSideMenuPAVehicles();
        vehiclePage.removeAllVehicles();
        vehiclePage.createVehicleManually();
        vehiclePage.createGenericVehicle(VehicleTypePL.ShowCar);
        /*
         * String currentVin =
         * this.myPolicyObjPL.squirePA.getVehicleList().get(2).getVin();
         * vehiclePage.removeVehicleByVIN(currentVin.substring(currentVin.length
         * ()-5, currentVin.length())); 		 *
         * //adding student discount vehiclePage.editVehicleByVehicleNumber(1);
         *  vehiclePage.selectVehicleType(VehicleTypePL.ShowCar);
         */
        vehiclePage.setStatedValue(20000);
        vehiclePage.sendArbitraryKeys(Keys.TAB);
        vehiclePage.setModelYear(1995);
        vehiclePage.sendArbitraryKeys(Keys.TAB);
        vehiclePage.setMake("Honda");
        vehiclePage.setModel("Accord");
        vehiclePage.sendArbitraryKeys(Keys.TAB);
        vehiclePage.setOdometer(2100);
        vehiclePage.selectGaragedAtZip(myPolicyObjPL.pniContact.getAddress().getCity());
        vehiclePage.clickOK();
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        String valMessage = "Show car vehicles must have the following coverages: Liability, Comprehensive, and Collision";
        if (!guidewireHelpers.errorMessagesExist() && (!guidewireHelpers.getFirstErrorMessage().contains(valMessage))) {
            testFailed = true;
            errorMessage = errorMessage + "Expected page validation : '" + valMessage + "' is not displayed. /n";
        }

        vehicleCoverages.selectVehicleCoverageDetailsTab();
        vehicleCoverages.setComprehensive(true);
        vehicleCoverages.setCollision(true);
        vehicleCoverages.clickOK();
        String[] showCarUWIssues = {"No private passenger or pickup", "Show car exists",
                "Older Than twenty years with comp and collision", "Vehicle level coverage added for first time"};

        validateRiskAnalysisApprovals(showCarUWIssues);
        sideMenu.clickSideMenuPolicyInfo();
        // Semi-trailer cannot exist without a farm truck on a policy, The usage
        // and truck type on a semi-trailer must match the usage and truck type
        // of at least one farm truck
        polInfo.clickEditPolicyTransaction();
        sideMenu.clickSideMenuPAVehicles();
        vehiclePage.createVehicleManually();
        vehiclePage.createGenericVehicle(VehicleTypePL.SemiTrailer);
        vehiclePage.selectUsage(Usage.FarmUse);
        vehiclePage.selectTruckType(VehicleTruckTypePL.TractorType);
        vehiclePage.selectTrailer(TrailerTypePL.Semi);
        vehiclePage.selectGaragedAtZip(myPolicyObjPL.pniContact.getAddress().getCity());
        vehiclePage.clickOK();

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();
        String validationMessages = risk.getValidationMessagesText();
        String valMeg1 = "At least one of the semi-trailer's usage and truck type must match that of existing farm trucks";
        if (!validationMessages.contains(valMeg1)) {
            testFailed = false;
            errorMessage = errorMessage + "Expected Validation message : '" + valMeg1 + "' is not displayed.";
        }

        String valMeg2 = "A farm truck must exist in policy to add a semi-trailer. Please add farm truck";
        if (!validationMessages.contains(valMeg2)) {
            testFailed = false;
            errorMessage = errorMessage + "Expected Validation message : '" + valMeg2 + "' is not displayed.";
        }
        risk.clickClearButton();
        sideMenu.clickSideMenuPAVehicles();
        vehiclePage.removeAllVehicles();

        // That's some light weight Tractor Truck. (If the GVW is equal or less
        // than 50,000, its not a Tractor Type Truck) (AU046)
        vehiclePage.createVehicleManually();
        vehiclePage.createGenericVehicle(VehicleTypePL.FarmTruck);
        vehiclePage.setOdometer(5000);
        vehiclePage.selectTruckType(VehicleTruckTypePL.TractorType);
        vehiclePage.setGVW(32000);
        vehiclePage.selectMileageFactor(MileageFactor.From2500To7499);
        vehiclePage.selectGaragedAtZip(myPolicyObjPL.pniContact.getAddress().getCity());
        vehiclePage.clickOK();
        String valMessage5 = "light weight Tractor Truck";
        if (!guidewireHelpers.errorMessagesExist() && (!guidewireHelpers.getFirstErrorMessage().contains(valMessage5))) {
            testFailed = true;
            errorMessage = errorMessage + "Expected page validation : '" + valMessage5 + "' is not displayed. /n";
        }
        vehiclePage.clickCancel();

        if (testFailed)
            Assert.fail(errorMessage);
    }

    @Test()
    private void createPLAutoPolicy() throws Exception {

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locationsList.add(locToAdd);

        SquirePersonalAutoCoverages sqpaCoverages = new SquirePersonalAutoCoverages(LiabilityLimit.ThreeHundredHigh, MedicalLimit.TenK, true, UninsuredLimit.ThreeHundred, false, UnderinsuredLimit.ThreeHundred);
        sqpaCoverages.setAccidentalDeath(true);

        // Vehicle List
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle veh1 = new Vehicle();
        veh1.setVehicleTypePL(VehicleTypePL.PrivatePassenger);

        Vehicle veh2 = new Vehicle();
        veh2.setVehicleTypePL(VehicleTypePL.ShowCar);
        veh2.setOdometer(1000);
        veh2.setCostNew(10000);
        veh2.setComprehensive(true);
        veh2.setCollision(true);
        vehicleList.add(veh1);
        vehicleList.add(veh2);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(sqpaCoverages);
        squirePersonalAuto.setVehicleList(vehicleList);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        this.myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Test", "Renew")
                .withSocialSecurityNumber(StringsUtils.generateRandomNumber(666000000, 666999999))
                .withPolTermLengthDays(79)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    private void addingBlockBindIssuesQuoteScenarios() throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        Date studentDob = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter),
                DateAddSubtractOptions.Year, -15);
        Date genericDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter),
                DateAddSubtractOptions.Year, -40);
        Date seniorDOB = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter),
                DateAddSubtractOptions.Year, -68);

        Contact student = new Contact("driver-" + randFirst, "Student", Gender.Female,
                DateUtils.getDateValueOfFormat(studentDob, "MM/dd/yyyy"));
        Contact motorist = new Contact("driver-" + randFirst, "Motorist", Gender.Male,
                DateUtils.getDateValueOfFormat(genericDate, "MM/dd/yyyy"));
        Contact notRated = new Contact("driver-" + randFirst, "notRated", Gender.Female,
                DateUtils.getDateValueOfFormat(genericDate, "MM/dd/yyyy"));
        Contact outOfState = new Contact("driver-" + randFirst, "OutOfState", Gender.Male,
                DateUtils.getDateValueOfFormat(genericDate, "MM/dd/yyyy"));
        Contact senior = new Contact("driver-" + randFirst, "Senior", Gender.Female,
                DateUtils.getDateValueOfFormat(seniorDOB, "MM/dd/yyyy"));
        // Adding policy member and driver
        addNewPolicyMember(true, student);
        addNewPolicyMember(true, motorist);
        addNewPolicyMember(true, senior);
        addNewPolicyMember(true, notRated);
        addNewPolicyMember(true, outOfState);

        // adding drivers list
        AddingNewDriver(student.getLastName(), student.getGender(), "FullTime Student", "ABCD1234C");
        AddingNewDriver(motorist.getLastName(), motorist.getGender(), "Relax", "CB54553D");
        AddingNewDriver(senior.getLastName(), senior.getGender(), "Relax", "CB54553D");
        AddingNewDriver(notRated.getLastName(), notRated.getGender(), "Relax", "CB54553D");
        AddingNewDriver(outOfState.getLastName(), outOfState.getGender(), "Relax", "CB54553D");

        // Cannot assign 'Not Rated' or excluded driver to a vehicle
        sideMenu.clickSideMenuPADrivers();
        paDrivers.clickEditButtonInDriverTableByDriverName(notRated.getLastName());
        paDrivers.setNotRatedCheckbox(true);
        paDrivers.selectNotRatedReason(NotRatedReason.ExchangeStudent);
        paDrivers.setPhysicalImpairmentOrEpilepsy(true);
        paDrivers.setExcludedDriverRadio(true);
        paDrivers.clickOk();

        // For any drivers with out-of-state license, create UW issue
        paDrivers.clickEditButtonInDriverTableByDriverName(outOfState.getLastName());
        paDrivers.selectLicenseState(State.Ohio);
        paDrivers.clickSRPIncidentsTab();
        GenericWorkorderSquireAutoDrivers_SelfReportingIncidents driver_SRP = new GenericWorkorderSquireAutoDrivers_SelfReportingIncidents(driver);
        driver_SRP.setInternationalDLIncident(30);
        driver_SRP.sendArbitraryKeys(Keys.TAB);
        driver_SRP.setUnverifiableDrivingRecordIncident(30);
        driver_SRP.sendArbitraryKeys(Keys.TAB);
        driver_SRP.setNoProofInsuranceIncident(30);

        paDrivers.clickOk();

        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        GenericWorkorderVehicles_CoverageDetails vehicleCoverages = new GenericWorkorderVehicles_CoverageDetails(driver);
        GenericWorkorderVehicles_ExclusionsAndConditions vehicleExclusions = new GenericWorkorderVehicles_ExclusionsAndConditions(driver);

        // Adding Vehicles for Student Discount
        vehiclePage.createVehicleManually();
        vehiclePage.createGenericVehicle(VehicleTypePL.PassengerPickup);
        vehiclePage.addDriver(student);
        vehiclePage.selectDriverVehicleUse(DriverVehicleUsePL.Occasional);
        vehiclePage.selectOcassionalDriverReason(OccasionalDriverReason.College);
        vehiclePage.setGoodStudentDiscountRadio(true);
        vehiclePage.selectGaragedAtZip(myPolicyObjPL.pniContact.getAddress().getCity());

        // Adding additional insured
        vehiclePage.addAutoAdditionalInsured("Testing purpose");
        // 310 added or removed
        Vehicle Vehicle = new Vehicle();
        Vehicle.setLocationOfDamage("Left Rear");
        Vehicle.setDamageItem("Tail Light");
        Vehicle.setDamageDescription("Missing");
        vehicleExclusions.fillOutExclusionsAndConditions(Vehicle);

        vehiclePage.clickOK();

        // Adding Vehicle for Senior discount
        vehiclePage.createVehicleManually();
        vehiclePage.createGenericVehicle(VehicleTypePL.PrivatePassenger);
        vehiclePage.setModelYear(2000);
        vehiclePage.setMake("Honda");
        vehiclePage.setModel("Accord");
        vehiclePage.setFactoryCostNew(20000);
        vehiclePage.selectDriverToAssign(senior);
        vehiclePage.setSeniorDiscountRadio(true);
        vehiclePage.selectGaragedAtZip(myPolicyObjPL.pniContact.getAddress().getCity());
        vehicleCoverages.selectVehicleCoverageDetailsTab();
        vehicleCoverages.setFireTheftCoverage(true);
        vehicleCoverages.clickOK();

        // Adding Vehicle for motorist Discount
        vehiclePage.createVehicleManually();
        vehiclePage.createGenericVehicle(VehicleTypePL.Motorcycle);
        vehiclePage.selectDriverToAssign(motorist);
        vehiclePage.setMotorCyclesStarDiscountRadio(true);
        vehiclePage.selectGaragedAtZip(myPolicyObjPL.pniContact.getAddress().getZip());
        vehiclePage.clickOK();

        // adding farm truck
        vehiclePage.createVehicleManually();
        vehiclePage.createGenericVehicle(VehicleTypePL.FarmTruck);
        vehiclePage.setOdometer(5000);
        vehiclePage.selectTruckType(VehicleTruckTypePL.FourPlus);
        vehiclePage.setGVW(52000);
        vehiclePage.setMake("honda");
        vehiclePage.setModel("accord");
        vehiclePage.setModelYear(2002);
        vehiclePage.selectGaragedAtZip(myPolicyObjPL.pniContact.getAddress().getCity());
        vehiclePage.clickOK();

        // If Modification of Insured Vehicle Definition Endorsement 301 is add
        // ed or removed, create UW block issue.
        sideMenu.clickSideMenuPACoverages();
        GenericWorkorderSquireAutoCoverages_Coverages paCoverages = new GenericWorkorderSquireAutoCoverages_Coverages(driver);

//		try {
        //USE CORRECT LOGIC!!
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.clickClearButton();
        sideMenu.clickSideMenuPACoverages();
        //		} catch (Exception e) {
//		}

        paCoverages.setMedicalCoverage(MedicalLimit.OneK);
        paCoverages.setLiabilityCoverage(LiabilityLimit.OneHundredHigh);
        paCoverages.clickAdditionalCoveragesTab();
        GenericWorkorderSquireAutoCoverages_AdditionalCoverages additionalCoverages = new GenericWorkorderSquireAutoCoverages_AdditionalCoverages(driver);
        additionalCoverages.setAccidentalDeath(false);

        paCoverages.clickCoverageExclusionsTab();
        GenericWorkorderSquireAutoCoverages_ExclusionsConditions paExclusions = new GenericWorkorderSquireAutoCoverages_ExclusionsConditions(driver);
        paExclusions.addModificationOfInsuredVehicleDefinitionEndorsement("BMW", "Beemer", "1B45678645456", "1999");

        // modifying the primary driver details
        sideMenu.clickSideMenuPADrivers();
        sideMenu.clickSideMenuPADrivers();
        paDrivers.clickEditButtonInDriverTableByDriverName(this.myPolicyObjPL.pniContact.getLastName());
        paDrivers.setSSN(StringsUtils.generateRandomNumberDigits(9));
        paDrivers.selectMaritalStatus(MaritalStatus.Single);
        paDrivers.clickOk();

        String[] expectedUWMessages = {"Driver under 16", "MVR not found", "City squire With Farm Truck",
                "55 and Alive Discount", "Good Student Discount", "Motorcycle Discount and New Business",
                "Not-rated drivers", "Driver with physical impairment or epilepsy", "Farm Trucks over 50,000 GVW",
                "Out of state license", "Additional insured change", "Not-rated drivers", "Change to driver info",
                "310 added or removed", "Added or removed condition 301", "Line level coverage reduced or removed",
                "Vehicle level coverage added for first time", "SRP is over 20"};

        validateRiskAnalysisApprovals(expectedUWMessages);
    }

    private void AddingNewDriver(String lastName, Gender gender, String occupation, String license) throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        paDrivers.addExistingDriver(lastName);
        paDrivers.selectMaritalStatus(MaritalStatus.Single);
        paDrivers.selectGender(gender);
        paDrivers.setPhysicalImpairmentOrEpilepsy(false);
        paDrivers.setOccupation(occupation);
        paDrivers.setLicenseNumber(license);
        paDrivers.sendArbitraryKeys(Keys.TAB);
        State state = State.Idaho;
        paDrivers.selectLicenseState(state);
        paDrivers.selectCommuteType(CommuteType.WorkFromHome);
        paDrivers.selectDriverHaveCurrentInsurance(true);
        paDrivers.setCurrentInsuranceCompanyPolicyNumber("Farm Bureau Policy #: 1B124576");
        paDrivers.clickOk();
    }

    private void addNewPolicyMember(boolean basicSearch, Contact person) throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuHouseholdMembers();

        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
        household.clickSearch();
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
        addressBook.searchAddressBookByFirstLastName(basicSearch, person.getFirstName(), person.getLastName(), null, null, null,
                null, CreateNew.Create_New_Always);
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        householdMember.setDateOfBirth(person.getDob());
        householdMember.selectRelationshipToInsured(RelationshipToInsured.SignificantOther);
        householdMember.setNewPolicyMemberAlternateID(person.getLastName());
        householdMember.selectNotNewAddressListingIfNotExist(myPolicyObjPL.pniContact.getAddress());
        householdMember.sendArbitraryKeys(Keys.TAB);
        householdMember.clickRelatedContactsTab();
        householdMember.clickOK();

    }

    private void validateRiskAnalysisApprovals(String[] expectedBlockIssues) {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuRiskAnalysis();
        boolean testFailed = false;
        String errorMessage = "";
        GenericWorkorderRiskAnalysis_UWIssues riskAnalysis = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        riskAnalysis.Quote();

        try {
            riskAnalysis.clickClearButton();
            riskAnalysis.Quote();
        } catch (Exception e) {
        }
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);

        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
        } else {
            sideMenu.clickSideMenuRiskAnalysis();
        }

        for (String uwIssue : expectedBlockIssues) {
            boolean messageFound = false;
            for (int i = 0; i < riskAnalysis.getUWIssuesList().size(); i++) {
                String currentUWIssueText = riskAnalysis.getUWIssuesList().get(i).getText();
                if (!currentUWIssueText.contains("Blocking Bind")
                        && (!currentUWIssueText.contains("Blocking Issuance"))) {

                    if (currentUWIssueText.contains(uwIssue)) {
                        messageFound = true;
                        break;
                    }
                }
            }
            if (!messageFound) {
                testFailed = true;
                errorMessage = errorMessage + "Expected error Bind Issue : " + uwIssue + " is not displayed.";
            }
        }

        if (testFailed)
            Assert.fail(errorMessage);

    }
}

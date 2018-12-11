package previousProgramIncrement.pi1_041918_062718.f58_QuickerQuickQuote_ReduceRequiredFieldsPL;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.Gender;
import com.idfbins.enums.State;
import repository.driverConfiguration.Config;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InceptionDateSections;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.InlandMarineTypes.RecreationalEquipmentType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.OrganizationTypePL;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.RelationshipsAB;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.Vehicle.PAComprehensive_CollisionDeductible;
import repository.gw.enums.Vehicle.TrailerTypePL;
import repository.gw.enums.Vehicle.Usage;
import repository.gw.enums.Vehicle.VehicleTruckTypePL;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PLPropertyCoverages;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.RecreationalEquipment;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_MotorVehicleRecord;
import repository.pc.workorders.generic.GenericWorkorderSquireEligibility;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderVehicles;
import repository.pc.workorders.generic.GenericWorkorderVehicles_CoverageDetails;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import persistence.enums.Underwriter.UnderwriterLine;
import persistence.enums.Underwriter.UnderwriterTitle;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * @Author nvadlamudi
 * @Requirement :US14795: Eliminate UW issues @ QQ, PL
 * @RequirementsLink <a href="https://fbmicoi.sharepoint.com/:x:/s/ARTists2/EXLlYMFF2E9OpE1cLcKmIBMBBHXDaL_CyJDsunqadnE76w?e=q0z3J6">US14795 PL QQ UW Issues to Disable</a>
 * @Description : Validating all the UW Issues in QQ Except Umbrella ( new test created)
 * @DATE Jun 6, 2018
 */
public class US14795EliminatePLQQUWIssues extends BaseTest {
    private GeneratePolicy myQQPolObj;
    private String excludedDriverFirstname = "DriveEx" + StringsUtils.generateRandomNumberDigits(6);
    private String newDriver = "NewDri" + StringsUtils.generateRandomNumberDigits(6);

    @Test
    public void testSectionIIIUWIssuesInQQ() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        PLPolicyLocationProperty property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        locOnePropertyList.add(property1);
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(12);
        locationsList.add(locToAdd);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh,
                MedicalLimit.TenK);

        // driver
        ArrayList<Contact> driversList = new ArrayList<Contact>();
        Contact person1 = new Contact(newDriver, "Motor", Gender.Male, DateUtils.dateAddSubtract(
                DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -22));
        person1.setRelationshipAB(RelationshipsAB.ChildWard);		
        Contact person2 = new Contact(excludedDriverFirstname, "Exclu", Gender.Male,
                DateUtils.convertStringtoDate("01/01/1990", "MM/dd/yyyy"));
        person2.setRelationshipAB(RelationshipsAB.AffiliateTo);
        Contact person3 = new Contact("TestNew" + StringsUtils.generateRandomNumberDigits(6), "Added", Gender.Male,
                DateUtils.convertStringtoDate("01/01/1990", "MM/dd/yyyy"));

        driversList.add(person1);
        driversList.add(person2);
        driversList.add(person3);
        // Vehicle List
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle veh1 = new Vehicle();
        veh1.setVehicleTypePL(VehicleTypePL.PrivatePassenger);

        Vehicle veh2 = new Vehicle();
        veh2.setVehicleTypePL(VehicleTypePL.Motorcycle);
        veh2.setDriverPL(person1);

        Vehicle veh3 = new Vehicle();
        veh3.setVehicleTypePL(VehicleTypePL.PassengerPickup);
        veh3.setDriverPL(person3);

        Vehicle veh4 = new Vehicle();
        veh4.setVehicleTypePL(VehicleTypePL.SemiTrailer);
        veh4.setUsage(Usage.FarmUse);
        veh4.setTruckType(VehicleTruckTypePL.TractorType);
        veh4.setTrailerType(TrailerTypePL.Semi);

        Vehicle veh5 = new Vehicle();
        veh5.setVehicleTypePL(VehicleTypePL.FarmTruck);
        veh1.setTruckType(VehicleTruckTypePL.TractorType);
        veh1.setUsage(Usage.FarmUse);
        veh5.setDriverPL(person2);

        vehicleList.add(veh1);
        vehicleList.add(veh2);
        vehicleList.add(veh3);
        vehicleList.add(veh4);
        vehicleList.add(veh5);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);
        squirePersonalAuto.setVehicleList(vehicleList);
        squirePersonalAuto.setDriversList(driversList);

        // Rec Equip
        ArrayList<RecreationalEquipment> recVehicle = new ArrayList<RecreationalEquipment>();
        recVehicle.add(new RecreationalEquipment(RecreationalEquipmentType.WagonsCarriages, "500",
                PAComprehensive_CollisionDeductible.Fifty50, "Brett Hiltbrand"));

        ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
        imTypes.add(InlandMarine.RecreationalEquipment);

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;
        myInlandMarine.recEquipment_PL_IM = recVehicle;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.squirePA = squirePersonalAuto;
        mySquire.inlandMarine = myInlandMarine;

        myQQPolObj = new GeneratePolicy.Builder(driver).withSquire(mySquire).withProductType(ProductLineType.Squire)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withLexisNexisData(true, false, false, false, true, true, false)
                .withPolOrgType(OrganizationType.Individual).withLineSelection(LineSelection.PropertyAndLiabilityLinePL,
                        LineSelection.PersonalAutoLinePL, LineSelection.InlandMarineLinePL)
                .build(GeneratePolicyType.QuickQuote);

        boolean testPassed = true;
        String errorMessage = "QuickQuote - UW Issues for the account : " + myQQPolObj.accountNumber;
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(UnderwriterLine.Personal,
                UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myQQPolObj.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);

        // AU069MVRWaived if any MVR incident was waived.
        sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers paDrivers = new GenericWorkorderSquireAutoDrivers(driver);
        GenericWorkorderSquireAutoDrivers_ContactDetail paDriver = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);

        paDrivers.clickEditButtonInDriverTableByDriverName(myQQPolObj.pniContact.getFirstName());
        paDriver.setLicenseNumber("PH105932B");
        paDriver.selectLicenseState(State.Idaho);
        GenericWorkorderSquireAutoDrivers_MotorVehicleRecord motorRecord = new GenericWorkorderSquireAutoDrivers_MotorVehicleRecord(driver);
        myQQPolObj.pniContact.isSpecial = true;
        motorRecord.fillOutMotorVehicleRecord_FA(myQQPolObj.pniContact);
        paDrivers.clickOK();

        // PAEligibleDriverAssigned All Eligible Drivers must be assigned to
        // Eligible Vehicles
        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles vehicalTab = new GenericWorkorderVehicles(driver);
        vehicalTab.editVehicleByType(VehicleTypePL.PassengerPickup);
        GenericWorkorderVehicles_Details vehDetails = new GenericWorkorderVehicles_Details(driver);

        GenericWorkorderVehicles_CoverageDetails vehicles = new GenericWorkorderVehicles_CoverageDetails(driver);
        vehDetails.clickVehicleCoveragesTab();
        vehicles.setRadioLiabilityCoverage(false);
        vehicles.setComprehensive(false);
        vehicalTab.clickOK();

        vehicalTab.editVehicleByType(VehicleTypePL.Motorcycle);
        vehDetails.removeAssignedDriver();
        // AU098DriverOnMultipleVehicles f the same driver is the designated
        // driver on more than one passenger vehicle or passenger pickup, create
        // UW Issue.

        // AU080YoungMotorcycleRider Driver under 25 not assigned to a
        // motorcycle, when motorcycles exist on policy.
        vehDetails.addDriver(myQQPolObj.pniContact);
        vehDetails.clickVehicleCoveragesTab();
        vehicles.setRadioLiabilityCoverage(false);
        vehicles.setComprehensive(false);
        vehicalTab.clickOK();

        // NoVINSysGenerated_FBM No VIN. System Generated
        vehicalTab.editVehicleByType(VehicleTypePL.PrivatePassenger);
        vehicalTab.setNOVIN(true);
        vehDetails.clickVehicleCoveragesTab();
        vehicles.setRadioLiabilityCoverage(false);
        vehicles.setComprehensive(false);
        vehicalTab.clickOK();

        // TypesMatchForSemi_Trailer_FBM The usage and truck type on a
        // semi-trailer must match the usage and truck type of at least one farm
        // truck
        vehicalTab.editVehicleByType(VehicleTypePL.FarmTruck);
        vehDetails.selectUsage(Usage.FarmUseWithOccasionalHire);
        vehDetails.selectTruckType(VehicleTruckTypePL.FourPlus);
        vehDetails.clickVehicleCoveragesTab();
        vehicles.setRadioLiabilityCoverage(false);
        vehicles.setComprehensive(false);
        vehicalTab.clickOK();

        vehicalTab.editVehicleByType(VehicleTypePL.SemiTrailer);
        vehDetails.clickVehicleCoveragesTab();
        vehicles.setRadioLiabilityCoverage(false);
        vehicles.setComprehensive(false);
        vehicalTab.clickOK();

        // AU093ExcludedDriverMissing Excluded driver has not been added to 304
        sideMenu.clickSideMenuPADrivers();
        paDrivers.clickEditButtonInDriverTableByDriverName(excludedDriverFirstname);
        paDriver.setExcludedDriverRadio(true);
        paDrivers.clickOK();

        // PANewVehicleWithoutLiability A policy can have Auto policy with no
        // Liability coverage. If no vehicle has liability coverage.

        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        if (quote.isPreQuoteDisplayed()) {
            testPassed = false;
            errorMessage = errorMessage + "Unexpected Pre-Quote UW Issues message in QQ. \n";
            quote.clickPreQuoteDetails();
        }

        sideMenu.clickSideMenuRiskAnalysis();
        FullUnderWriterIssues allUWIssues = risk.getUnderwriterIssues();
        if (allUWIssues.getBlockQuoteList().size() > 0 || allUWIssues.getBlockSubmitList().size() > 0) {
            testPassed = false;
            errorMessage = errorMessage
                    + "Unexpected Block Quote, Block Submit and Informatonal UW Issues displayed in QQ. \n";
        }

        Assert.assertTrue(testPassed, errorMessage);
    }

    @Test(dependsOnMethods = {"testSectionIIIUWIssuesInQQ"})
    public void testSectionIUWIssuesInQQ() throws Exception {
        // Squire - Section I PR085 Trellis with Weight of Ice and Snow
        // PR085TrellisWithWeight

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);
        boolean testPassed = true;
        String errorMessage = "QuickQuote - Section I - UW Issues for the account : " + myQQPolObj.accountNumber;
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(UnderwriterLine.Personal,
                UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myQQPolObj.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);

        sideMenu.clickSideMenuSquirePropertyDetail();
        PLPolicyLocationProperty trelliProperty = new PLPolicyLocationProperty(PropertyTypePL.Trellis);
        trelliProperty.setConstructionType(ConstructionTypePL.Frame);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        propertyDetail.clickAdd();
        propertyDetail.fillOutPropertyDetails_QQ(true, trelliProperty);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        constructionPage.fillOutPropertyConstruction_QQ(trelliProperty);
        propertyDetail.clickOk();
        propertyDetail.clickNext();
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverages.clickSpecificBuilding(1, coverages.getBuildingNumber(PropertyTypePL.Trellis));
        coverages.setCoverageELimit(125000);
        coverages.setCoverageECoverageType(CoverageType.BroadForm);
        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.setPolicyInfoOrganizationType(OrganizationTypePL.Individual);
        new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        if (quote.isPreQuoteDisplayed()) {
            testPassed = false;
            errorMessage = errorMessage + "Unexpected Pre-Quote UW Issues message in QQ. \n";
            quote.clickPreQuoteDetails();
        }

        sideMenu.clickSideMenuRiskAnalysis();
        FullUnderWriterIssues allUWIssues = risk.getUnderwriterIssues();
        if (allUWIssues.getBlockQuoteList().size() > 0 || allUWIssues.getBlockSubmitList().size() > 0) {
            testPassed = false;
            errorMessage = errorMessage
                    + "Section I - Unexpected Block Quote, Block Submit and Informatonal UW Issues displayed in QQ. \n";
        }
        Assert.assertTrue(testPassed, errorMessage);
    }


    @Test(dependsOnMethods = {"testSectionIUWIssuesInQQ"})
    public void testAddPolicyLevelUWIssuesAndValidate() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(UnderwriterLine.Personal,
                UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myQQPolObj.accountNumber);
        AccountSummaryPC summary = new AccountSummaryPC(driver);
        summary.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);
        new GuidewireHelpers(driver).editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        boolean testPassed = true;
        String errorMessage = "QuickQuote - Policy level - UW Issues for the account : " + myQQPolObj.accountNumber;

        //Policy Level Forms	AL001	Non-existing policy transfer	AL001PolicyNumberNotFound	X
        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.setTransferedFromAnotherPolicy(true);
        policyInfo.setCheckBoxInInceptionDateByRow(1, true);
        policyInfo.setInceptionDatePolicyNumberDirectly(InceptionDateSections.Policy, "01-111111-01");
        policyInfo.setInceptionDateByRow(1, policyInfo.getPolicyInfoEffectiveDate());
        policyInfo.setCheckBoxInInceptionDateByRow(2, false);
        policyInfo.setCheckBoxInInceptionDateByRow(3, false);
        policyInfo.setCheckBoxInInceptionDateByRow(4, false);
        policyInfo.setCheckBoxInInceptionDateByRow(5, false);
        //Policy Level Forms	SQ017	Effective date "back to the future"		X

        //Policy Level Forms	SQ013	10 day submitting authority	SQ013TenDayBindingAuthority	X
        Date newEffectiveDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter),
                DateAddSubtractOptions.Day, -11);
        policyInfo.setPolicyInfoEffectiveDate(DateUtils.dateFormatAsString("MM/dd/yyyy", newEffectiveDate));
        policyInfo.clickNext();

        //Policy Level Forms	SQ010	Country, or Farm & Ranch Squire Auto only	CitySquireAutoOnly	X
        sideMenu.clickSideMenuSquireEligibility();
        GenericWorkorderSquireEligibility eligibilityPage = new GenericWorkorderSquireEligibility(driver);
        eligibilityPage.chooseCountry(false);

        //Policy Level Forms	SQ041	$10 million policy		X
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverages.setCoverageALimit(2000000);

        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        if (quote.isPreQuoteDisplayed()) {
            testPassed = false;
            errorMessage = errorMessage + "Unexpected Pre-Quote UW Issues message in QQ. \n";
            quote.clickPreQuoteDetails();
        }

        sideMenu.clickSideMenuRiskAnalysis();
        FullUnderWriterIssues allUWIssues = risk.getUnderwriterIssues();
        if (allUWIssues.getBlockQuoteList().size() > 0 || allUWIssues.getBlockSubmitList().size() > 0) {
            testPassed = false;
            errorMessage = errorMessage
                    + "Unexpected Block Quote, Block Submit UW Issues displayed in QQ. \n";
        }

        Assert.assertTrue(testPassed, errorMessage);

    }

    @Test
    public void testStandardFireQQUWIssues() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);
        Squire mySquire = new Squire(SquireEligibility.City);

        GeneratePolicy myNewQQPolObj = new GeneratePolicy.Builder(driver).withSquire(mySquire).withProductType(ProductLineType.Squire)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withPolOrgType(OrganizationType.Individual).withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .build(GeneratePolicyType.QuickQuote);
        // Squire - Section I PR087 Block Bind Trellised Hops
        // standard fire
        ArrayList<PolicyLocation> locationsList1 = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList1 = new ArrayList<PLPolicyLocationProperty>();
        PLPolicyLocationProperty newProp = new PLPolicyLocationProperty(PropertyTypePL.TrellisedHops);
        newProp.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.Peril_1);
        locOnePropertyList1.add(newProp);
        PolicyLocation locToAdd1 = new PolicyLocation(locOnePropertyList1);

        locToAdd1.setPlNumAcres(11);
        locationsList1.add(locToAdd1);

        PLPropertyCoverages propertyCoverages = new PLPropertyCoverages();
        propertyCoverages.getCoverageA().setIncreasedReplacementCost(false);
        StandardFireAndLiability myStandardfire = new StandardFireAndLiability();
        myStandardfire.setLocationList(locationsList1);
        myStandardfire.setStdFireCommodity(true);
        myNewQQPolObj.standardFire = myStandardfire;
        myNewQQPolObj.hasMembershipDuesForPrimaryNamedInsured = false;
        myNewQQPolObj.addLineOfBusiness(ProductLineType.StandardFire, GeneratePolicyType.QuickQuote);
        new Login(driver).loginAndSearchSubmission(myNewQQPolObj);
        new GuidewireHelpers(driver).editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        boolean testPassed = true;
        String errorMessage = "QuickQuote - Policy level - UW Issues for the account : " + myNewQQPolObj.accountNumber;

        if (quote.isPreQuoteDisplayed()) {
            testPassed = false;
            errorMessage = errorMessage + "Unexpected Pre-Quote UW Issues message in QQ. \n";
            quote.clickPreQuoteDetails();
        }

        sideMenu.clickSideMenuRiskAnalysis();
        FullUnderWriterIssues allUWIssues = risk.getUnderwriterIssues();
        if (allUWIssues.getBlockQuoteList().size() > 0 || allUWIssues.getBlockSubmitList().size() > 0) {
            testPassed = false;
            errorMessage = errorMessage
                    + "Section I - Unexpected Block Quote, Block Submit  UW Issues displayed in QQ. \n";
        }
        Assert.assertTrue(testPassed, errorMessage);
    }
}

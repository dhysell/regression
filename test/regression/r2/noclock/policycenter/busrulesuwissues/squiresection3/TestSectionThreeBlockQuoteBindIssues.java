package regression.r2.noclock.policycenter.busrulesuwissues.squiresection3;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.enums.Gender;
import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.CommuteType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.HowLongWithoutCoverage;
import repository.gw.enums.InceptionDateSections;
import repository.gw.enums.LineSelection;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.NotRatedReason;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PLUWIssues;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.TransactionType;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.enums.Vehicle.DriverVehicleUsePL;
import repository.gw.enums.Vehicle.TrailerTypePL;
import repository.gw.enums.Vehicle.Usage;
import repository.gw.enums.Vehicle.VehicleTruckTypePL;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
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
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_AdditionalCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_ExclusionsConditions;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_SelfReportingIncidents;
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
 * @Requirement : Squire-Section III-Product-Model - Bus Rules and UW Issues tab
 * @RequirementsLink <a href=
 *                   "http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/Product%20Model%20Spreadsheets/squire%20product%20model%20spreadhseets/Squire-Section%20III-Product-Model.xlsx">
 *                   Squire-Section III-Product-Model</a>
 * @Description
 * @DATE Sep 18, 2017
 */
@QuarantineClass()
public class TestSectionThreeBlockQuoteBindIssues extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObjPL = null;
	SoftAssert softAssert = new SoftAssert();
	private String au009BusRule = "Vehicle has no coverage. Please select coverage or remove vehicle";
	private String au042BusRule = "A farm truck must exist in policy to add a semi-trailer. Please add farm truck. (AU042)";
	private String studentFirstName = "";
	private GeneratePolicy myPolicyCityObjPL;
	private GeneratePolicy myPolRenewObjPL;

	@BeforeMethod(alwaysRun = true)
	public void beforeMethod() {
		softAssert = new SoftAssert();
	}

	@Test
	public void testGenerateCustomAuto() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = new SquirePersonalAuto();

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Quote", "Auto")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.FullApp);
	}

	@Test(dependsOnMethods = { "testGenerateCustomAuto" })
	private void testAddCustomAutoBlockingQuoteAndQuoteError() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(),
				myPolicyObjPL.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
		GenericWorkorderVehicles_CoverageDetails vehicleCoverages = new GenericWorkorderVehicles_CoverageDetails(driver);
		vehiclePage.editVehicleByVehicleNumber(1);
        vehicleCoverages.selectVehicleCoverageDetailsTab();
        vehicleCoverages.setRadioLiabilityCoverage(false);
		vehicleCoverages.clickOK();
        // AU009 - AUTO: No vehicles with liability
		softAssert.assertFalse((!new GuidewireHelpers(driver).errorMessagesExist() && (!new GuidewireHelpers(driver).getFirstErrorMessage().contains(au009BusRule))),
				"Expected page validation : '" + au009BusRule + "' is not displayed.");
		vehiclePage.clickCancel();

		// AU048 - MVR for each driver
		addNewhouseholdMemberAndDriver(myPolicyObjPL.basicSearch, "NewMem" + StringsUtils.generateRandomNumberDigits(7),
				"Added" + StringsUtils.generateRandomNumberDigits(7), this.myPolicyObjPL.pniContact.getAddress(),
				DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter),
						DateAddSubtractOptions.Year, -25));

        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
		}

		sideMenu.clickSideMenuRiskAnalysis();
		@SuppressWarnings("serial")
		List<PLUWIssues> sqSectionIIIBlockQuote = new ArrayList<PLUWIssues>() {
			{
				this.add(PLUWIssues.MVRForEachDriver);
			}
		};

		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();
		for (PLUWIssues uwBlockSubmitExpected : sqSectionIIIBlockQuote) {
			softAssert.assertFalse(
					!uwIssues.isInList(uwBlockSubmitExpected.getLongDesc()).equals(UnderwriterIssueType.BlockSubmit),
					"Expected error Bind Bind : " + uwBlockSubmitExpected.getShortDesc() + " is not displayed");
		}
		new GuidewireHelpers(driver).editPolicyTransaction();
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		// AU042 - Semi-Trailer and Farm Truck
		sideMenu.clickSideMenuPAVehicles();
        vehiclePage.createVehicleManually();
		vehiclePage.createGenericVehicle(VehicleTypePL.SemiTrailer);
        vehiclePage.selectUsage(Usage.FarmUse);
        vehiclePage.selectTruckType(VehicleTruckTypePL.TractorType);
        vehiclePage.selectTrailer(TrailerTypePL.Semi);
        vehiclePage.selectGaragedAtZip("ID");
		vehiclePage.clickOK();
        risk.Quote();
        softAssert.assertFalse(!risk.getValidationMessagesText().contains(au042BusRule),
				"Expected page validation : '" + au042BusRule + "' is not displayed");
		risk.clickClearButton();

		quotePage.clickSaveDraftButton();
		softAssert.assertAll();
	}

	@Test(dependsOnMethods = { "testAddCustomAutoBlockingQuoteAndQuoteError" })
	private void testAddCustomAutoBlockSubmitBlockIssue() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(),
				myPolicyObjPL.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
		// AU012 - No Private Passenger or Pickups
		sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
		GenericWorkorderVehicles_CoverageDetails vehicleCoverages = new GenericWorkorderVehicles_CoverageDetails(driver);
		vehiclePage.removeAllVehicles();

		// AU016 - Show car exists
		// AU045 - Show car photos
		vehiclePage.createVehicleManually();
        vehiclePage.createGenericVehicle(VehicleTypePL.ShowCar);
		vehiclePage.setStatedValue(20000);
        vehiclePage.setModelYear(1995);
        vehiclePage.setMake("Honda");
		vehiclePage.setModel("Accord");
		vehiclePage.setOdometer(4000);
		vehiclePage.selectGaragedAtZip("ID");
		vehicleCoverages.selectVehicleCoverageDetailsTab();
        vehicleCoverages.setComprehensive(true);
        vehicleCoverages.setCollision(true);
		vehicleCoverages.clickOK();

		// AU020 - SRP higher than 25
		// AU026 - Out of state license
		// AU027 - Driver with physical impairment or epilepsy

		sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
		paDrivers.clickEditButtonInDriverTable(1);
        paDrivers.setPhysicalImpairmentOrEpilepsy(true);
        paDrivers.selectLicenseState(State.Ohio);
		paDrivers.clickSRPIncidentsTab();
        GenericWorkorderSquireAutoDrivers_SelfReportingIncidents driver_SRP = new GenericWorkorderSquireAutoDrivers_SelfReportingIncidents(driver);
		driver_SRP.setInternationalDLIncident(30);
		driver_SRP.setUnverifiableDrivingRecordIncident(30);
		driver_SRP.setNoProofInsuranceIncident(30);
		paDrivers.clickOk();

		// AU023 - Farm truck added
		// AU038 - Farm Trucks over 50,000 GVW
		sideMenu.clickSideMenuPAVehicles();
        vehiclePage.createVehicleManually();
        vehiclePage.createGenericVehicle(VehicleTypePL.FarmTruck);
		vehiclePage.selectTruckType(VehicleTruckTypePL.UnderFour);
		vehiclePage.setGVW(55000);
		vehiclePage.selectGaragedAtZip("ID");
		vehiclePage.setOdometer(20000);
		vehiclePage.setModel("Accord");
		vehiclePage.setModelYear(2013);
		vehiclePage.setMake("Honda");
		vehiclePage.clickOK();

		// AU033 - Motorcycle Discount and New Business
		String motorist = "Motorist" + StringsUtils.generateRandomNumberDigits(7);
		addNewhouseholdMemberAndDriver(myPolicyObjPL.basicSearch, motorist, "Discount" + StringsUtils.generateRandomNumberDigits(5),
				this.myPolicyObjPL.pniContact.getAddress(), DateUtils.dateAddSubtract(
						DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -25));
		sideMenu.clickSideMenuPAVehicles();
		vehiclePage.createVehicleManually();
		vehiclePage.createGenericVehicle(VehicleTypePL.Motorcycle);
		vehiclePage.selectDriverToAssign(motorist);
		vehiclePage.setMotorCyclesStarDiscountRadio(true);
		vehiclePage.selectGaragedAtZip("ID");
		vehiclePage.clickOK();

		// AU043 - Usage and Truck type for Semi-Trailer
		sideMenu.clickSideMenuPAVehicles();
        vehiclePage.createVehicleManually();
		vehiclePage.createGenericVehicle(VehicleTypePL.SemiTrailer);
        vehiclePage.selectUsage(Usage.FarmUse);
        vehiclePage.selectTruckType(VehicleTruckTypePL.TractorType);
        vehiclePage.selectTrailer(TrailerTypePL.Semi);
	    vehiclePage.selectGaragedAtZip("ID");
		vehiclePage.clickOK();

		// AU022 - Good Student Discount
		// AU056 - Driver under 16
		studentFirstName = "Student" + StringsUtils.generateRandomNumberDigits(7);
		addNewhouseholdMemberAndDriver(myPolicyObjPL.basicSearch, studentFirstName, "Discount" + StringsUtils.generateRandomNumberDigits(7),
				this.myPolicyObjPL.pniContact.getAddress(), DateUtils.dateAddSubtract(
						DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -15));
		sideMenu.clickSideMenuPAVehicles();
        vehiclePage.createVehicleManually();
		vehiclePage.createGenericVehicle(VehicleTypePL.Motorcycle);
		vehiclePage.selectDriverToAssign(studentFirstName);
		vehiclePage.selectDriverVehicleUse(DriverVehicleUsePL.Principal);
		vehiclePage.selectGaragedAtZip("ID");
		vehiclePage.clickOK();

		// AU091 - CLUE Auto not ordered

		// AU028 - Business use and Other
		sideMenu.clickSideMenuQualification();
        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
		qualificationPage.clickPL_Business(true);
		qualificationPage.checkBusinessPurposeUsedVehicle("Contractor");
		qualificationPage.clickPL_Employees(false);
		qualificationPage.clickQualificationNext();
		qualificationPage.clickQualificationNext();

		// AU035 - InvalidVIN_FBM
		// AU036 - High Symbol
		sideMenu.clickSideMenuPAVehicles();
		vehiclePage.editVehicleByType(VehicleTypePL.ShowCar);
        vehiclePage.setNOVIN(true);
		vehiclePage.clickOK();

		// AU050 - Not-rated drivers
		// AU075 - SR-22 charge
		String notRatedFirstName = "NotRated" + StringsUtils.generateRandomNumberDigits(7);
		addNewhouseholdMemberAndDriver(myPolicyObjPL.basicSearch, notRatedFirstName, "Discount" + StringsUtils.generateRandomNumberDigits(5),
				this.myPolicyObjPL.pniContact.getAddress(), DateUtils.dateAddSubtract(
						DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -25));
		sideMenu.clickSideMenuPADrivers();
		paDrivers.clickEditButtonInDriverTableByDriverName(notRatedFirstName);
        paDrivers.setNotRatedCheckbox(true);
		paDrivers.selectNotRatedReason(NotRatedReason.ExchangeStudent);
        paDrivers.setSR22Checkbox(true);
		paDrivers.clickOk();

		if (new GuidewireHelpers(driver).errorMessagesExist()) {
			new GuidewireHelpers(driver).clickDiscardUnsavedChangesLink();
            sideMenu.clickSideMenuPADrivers();
            paDrivers.clickEditButtonInDriverTableByDriverName(notRatedFirstName);
            paDrivers.setNotRatedCheckbox(true);
			paDrivers.selectNotRatedReason(NotRatedReason.ExchangeStudent);
            paDrivers.setSR22Checkbox(true);
			paDrivers.clickOk();
		}
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
		}

		@SuppressWarnings("serial")
		List<PLUWIssues> sqSectionIIIBlockSubmit = new ArrayList<PLUWIssues>() {
			{
				this.add(PLUWIssues.PhysicalImpairmentEpilepsy);
				this.add(PLUWIssues.VehicleUsedForBusiness);
				this.add(PLUWIssues.SR22Charge);
			}
		};

		@SuppressWarnings("serial")
		List<PLUWIssues> sqSectionIIIBlockIssue = new ArrayList<PLUWIssues>() {
			{
				this.add(PLUWIssues.UsageAndTruckTypeSemiTrailer);
				this.add(PLUWIssues.NonAllowedTickets);
				this.add(PLUWIssues.NoPrivatePassenferPickup);
				this.add(PLUWIssues.UnassignedDriverWithHighSRP);
				this.add(PLUWIssues.DriverUnder16);
				this.add(PLUWIssues.MotorCycleDiscountNewBusiness);
				this.add(PLUWIssues.OutOfStateLicense);
				this.add(PLUWIssues.ShowCarExists);
				this.add(PLUWIssues.NotRatedDriver);
				this.add(PLUWIssues.NoVIN);
				this.add(PLUWIssues.FarmTruckOver50K);
			}
		};

		@SuppressWarnings("serial")
		List<PLUWIssues> sqSectionIIIInformation = new ArrayList<PLUWIssues>() {
			{
				this.add(PLUWIssues.PolicyHasMotorCycleAndDriverLessThan25);
			}
		};

		@SuppressWarnings("serial")
		List<PLUWIssues> sqSectionIIIApproved = new ArrayList<PLUWIssues>() {
			{
				this.add(PLUWIssues.UMUIMDisclosure);
				this.add(PLUWIssues.CLUEAutoNotOrdered);
			}
		};

		sideMenu.clickSideMenuRiskAnalysis();
        FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();

		for (PLUWIssues uwBlockSubmitExpected : sqSectionIIIBlockSubmit) {
			softAssert.assertFalse(
					!uwIssues.isInList(uwBlockSubmitExpected.getLongDesc()).equals(UnderwriterIssueType.BlockSubmit),
					"Expected error Bind Bind : " + uwBlockSubmitExpected.getShortDesc() + " is not displayed");
		}
		for (PLUWIssues uwBlockSubmitExpected : sqSectionIIIInformation) {
			softAssert.assertFalse(
					!uwIssues.isInList(uwBlockSubmitExpected.getLongDesc()).equals(UnderwriterIssueType.Informational),
					"Expected error Information Message : " + uwBlockSubmitExpected.getShortDesc()
							+ " is not displayed");
		}

		for (PLUWIssues uwBlockSubmitExpected : sqSectionIIIBlockIssue) {
			softAssert.assertFalse(
					!uwIssues.isInList(uwBlockSubmitExpected.getLongDesc()).equals(UnderwriterIssueType.BlockIssuance),
					"Expected error Bind Issuance : " + uwBlockSubmitExpected.getShortDesc() + " is not displayed");
		}

		for (PLUWIssues uwBlockSubmitExpected : sqSectionIIIApproved) {
			softAssert.assertFalse(
					!uwIssues.isInList(uwBlockSubmitExpected.getLongDesc())
							.equals(UnderwriterIssueType.AlreadyApproved),
					"Expected error Bind Bind : " + uwBlockSubmitExpected.getShortDesc() + " is not displayed");
		}

		softAssert.assertAll();
	}

	@Test(dependsOnMethods = { "testAddCustomAutoBlockSubmitBlockIssue" })
	private void testAddCustomAutoBlockSubmitBlockIssueMore() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(),
				myPolicyObjPL.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
		GenericWorkorderVehicles_CoverageDetails vehicleCoverages = new GenericWorkorderVehicles_CoverageDetails(driver);
		vehiclePage.removeAllVehicles();

		// AU054 - 55 and Alive discount
		String seniorFirstName = "Senior" + StringsUtils.generateRandomNumberDigits(7);
		addNewhouseholdMemberAndDriver(myPolicyCityObjPL.basicSearch, seniorFirstName, "Discount" + StringsUtils.generateRandomNumberDigits(6),
				this.myPolicyObjPL.pniContact.getAddress(), DateUtils.dateAddSubtract(
						DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -58));

		// AU018 - Older than twenty years with comp and collision
		sideMenu.clickSideMenuPAVehicles();
		vehiclePage.createVehicleManually();
		vehiclePage.createGenericVehicle(VehicleTypePL.PassengerPickup);
		vehiclePage.selectDriverToAssign(seniorFirstName);
		vehiclePage.setSeniorDiscountRadio(true);
		vehiclePage.selectGaragedAtZip("ID");
		vehiclePage.setModelYear(1995);
		vehicleCoverages.selectVehicleCoverageDetailsTab();
        vehicleCoverages.setComprehensive(true);
        vehicleCoverages.setCollision(true);
		vehiclePage.clickVehicleDetailsTab();
		vehiclePage.setFactoryCostNew(10000);
		vehiclePage.clickOK();

		// Student Discount
		sideMenu.clickSideMenuPAVehicles();
		vehiclePage.createVehicleManually();
		vehiclePage.createGenericVehicle(VehicleTypePL.PassengerPickup);
		vehiclePage.selectDriverToAssign(this.studentFirstName);
        vehiclePage.selectDriverVehicleUse(DriverVehicleUsePL.Principal);
		vehiclePage.setGoodStudentDiscountRadio(true);
		vehiclePage.selectGaragedAtZip("ID");
		vehiclePage.clickOK();
        new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
		}

		@SuppressWarnings("serial")
		List<PLUWIssues> sqSectionIIIBlockIssue = new ArrayList<PLUWIssues>() {
			{
				this.add(PLUWIssues.GoodStudentDiscount);
				this.add(PLUWIssues.Auto55AndAliveDiscount);
				this.add(PLUWIssues.PassengerVehOlderThan20yrsCompColl);
			}
		};
		sideMenu.clickSideMenuRiskAnalysis();

		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();
		for (PLUWIssues uwBlockSubmitExpected : sqSectionIIIBlockIssue) {
			softAssert.assertFalse(
					!uwIssues.isInList(uwBlockSubmitExpected.getLongDesc()).equals(UnderwriterIssueType.BlockIssuance),
					"Expected error Bind Issuance : " + uwBlockSubmitExpected.getShortDesc() + " is not displayed");
		}

		softAssert.assertAll();

	}

	@Test()
	public void testGenerateCityAuto() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = new SquirePersonalAuto();

        myPolicyCityObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("City", "Auto")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.FullApp);
        driver.quit();

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				myPolicyCityObjPL.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
		vehiclePage.createVehicleManually();
        vehiclePage.createGenericVehicle(VehicleTypePL.FarmTruck);
		vehiclePage.selectTruckType(VehicleTruckTypePL.TractorType);
		vehiclePage.setGVW(55000);
		vehiclePage.selectGaragedAtZip("ID");
		vehiclePage.setOdometer(20000);
		vehiclePage.setModel("Accord");
		vehiclePage.setModelYear(2013);
		vehiclePage.setMake("Honda");
		vehiclePage.clickOK();

		// AU093 304 Excluded Driver without 304
		String excludedFirstName = "Excluded" + StringsUtils.generateRandomNumberDigits(7);
		addNewhouseholdMemberAndDriver(myPolicyCityObjPL.basicSearch, excludedFirstName, "Discount" + StringsUtils.generateRandomNumberDigits(5),
				this.myPolicyCityObjPL.pniContact.getAddress(), DateUtils.dateAddSubtract(
						DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -25));
		sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
		paDrivers.clickEditButtonInDriverTableByDriverName(excludedFirstName);
        paDrivers.setExcludedDriverRadio(true);
		// AU097 - no prior insurance
		paDrivers.selectDriverHaveCurrentInsurance(false);
		paDrivers.setHowLongWithoutCoverage(HowLongWithoutCoverage.BetweenSixtyAndSixMonths);
		paDrivers.clickOk();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();
        // AU021 - AUTO: City squire With Farm Truck
		PLUWIssues uwMustBeCorrectedExpected = PLUWIssues.ExcludedDriverwithout304;

		PLUWIssues uwBlockSubmitExpected = PLUWIssues.CitySquireWithFarmTruck;
		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();

		softAssert.assertFalse(
				!uwIssues.isInList(uwMustBeCorrectedExpected.getLongDesc()).equals(UnderwriterIssueType.Informational),
				"Expected error Must be corrected : " + uwMustBeCorrectedExpected.getShortDesc() + " is not displayed");

		softAssert.assertFalse(
				!uwIssues.isInList(uwBlockSubmitExpected.getLongDesc()).equals(UnderwriterIssueType.BlockSubmit),
				"Expected error Must be corrected : " + uwBlockSubmitExpected.getShortDesc() + " is not displayed");

		softAssert.assertFalse(
				!uwIssues.isInList(PLUWIssues.AU097NoPriorInsurance.getLongDesc())
						.equals(UnderwriterIssueType.Informational),
				"Expected Information message : " + PLUWIssues.AU097NoPriorInsurance.getShortDesc()
						+ " is not displayed");

		softAssert.assertAll();
	}

	@Test()
	private void testSquireAutoMVRCLUE() throws Exception {
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

        GeneratePolicy clueMVRPol = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
				.withPolOrgType(OrganizationType.Individual)
				.withLexisNexisData(true, false, false, false, true, true, false).build(GeneratePolicyType.FullApp);
        driver.quit();

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), clueMVRPol.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);

		// AU083 - SRP >= 16
		sideMenu.clickSideMenuPADrivers();

        GenericWorkorderSquireAutoDrivers_SelfReportingIncidents paDrivers = new GenericWorkorderSquireAutoDrivers_SelfReportingIncidents(driver);
		paDrivers.clickEditButtonInDriverTable(1);
		paDrivers.clickSRPIncidentsTab();
        paDrivers.setOverrideSRPCheckbox(true);
		paDrivers.setReasonForSRPOverride("Testing purpose");
		paDrivers.setSRP(25);
        paDrivers.clickOk();

		// AU065 - Not valid license on MVR
		// AU069 - Waived MVR incident
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();
		// AU021 - AUTO: City squire With Farm Truck
		@SuppressWarnings("serial")
		List<PLUWIssues> sqSectionIIIBlockSubmit = new ArrayList<PLUWIssues>() {
			{
				this.add(PLUWIssues.WaivedMVRIncident);
				this.add(PLUWIssues.NotValidLicenseMVR);
			}
		};
		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();
		for (PLUWIssues uwBlockSubmitExpected : sqSectionIIIBlockSubmit) {
			softAssert.assertFalse(
					!uwIssues.isInList(uwBlockSubmitExpected.getLongDesc())
							.equals(UnderwriterIssueType.AlreadyApproved),
					"Expected error Bind Issuance : " + uwBlockSubmitExpected.getShortDesc() + " is not displayed");
		}

		PLUWIssues sqSectionIIIBlockIssuance = PLUWIssues.SRPGreaterThan15;
		softAssert.assertFalse(
				!uwIssues.isInList(sqSectionIIIBlockIssuance.getLongDesc()).equals(UnderwriterIssueType.BlockIssuance),
				"Expected error Bind Issuance : " + sqSectionIIIBlockIssuance.getShortDesc() + " is not displayed");

		softAssert.assertAll();
	}

	@Test()
	private void createPLAutoPolicyRenewal() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

		SquirePersonalAutoCoverages sqpaCoverages = new SquirePersonalAutoCoverages(LiabilityLimit.ThreeHundredHigh,
				MedicalLimit.TenK, true, UninsuredLimit.ThreeHundred, false, UnderinsuredLimit.ThreeHundred);
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

        SquirePropertyAndLiability myProperty = new SquirePropertyAndLiability();
        myProperty.locationList = locationsList;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.propertyAndLiability = myProperty;
        mySquire.squirePA = squirePersonalAuto;


        myPolRenewObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Rules", "Renew")
				.withSocialSecurityNumber(StringsUtils.generateRandomNumber(666000000, 666999999))
                .withPolTermLengthDays(79)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
        driver.quit();

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        // Login with UW
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				myPolRenewObjPL.accountNumber);

        StartRenewal renewal = new StartRenewal(driver);
        renewal.startRenewal();

        InfoBar infoBar = new InfoBar(driver);
		infoBar.clickInfoBarPolicyNumber();

        PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
		preRenewalPage.closePreRenewalExplanations(myPolRenewObjPL);

		// AU014 - Show car mileage over 1000
        PolicySummary policySummary = new PolicySummary(driver);
		policySummary.clickPendingTransaction(TransactionType.Renewal);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickRenewalInformation();
		RenewalInformation renewalInfo = new RenewalInformation(driver);
		renewalInfo.setRenewalOdometerByVehicleNumber(2, "2100");

		sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
		GenericWorkorderVehicles_CoverageDetails vehicleCoverages = new GenericWorkorderVehicles_CoverageDetails(driver);
		GenericWorkorderVehicles_ExclusionsAndConditions vehicleExclusions = new GenericWorkorderVehicles_ExclusionsAndConditions(driver);
		vehiclePage.editVehicleByVehicleNumber(2);
        vehiclePage.setOdometer(2100);
        vehiclePage.clickOK();

        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
		}
		// AU014 - Show car mileage over 1000
		sideMenu.clickSideMenuRiskAnalysis();
		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();
		PLUWIssues sqSectionIIIBlockIssuance = PLUWIssues.ShowCarMileageOver1000;
		softAssert.assertFalse(
				!uwIssues.isInList(sqSectionIIIBlockIssuance.getLongDesc()).equals(UnderwriterIssueType.BlockIssuance),
				"Expected error Bind Issuance : " + sqSectionIIIBlockIssuance.getShortDesc() + " is not displayed");

		new GuidewireHelpers(driver).editPolicyTransaction();

		// AU062 - Vehicle level coverage added for first time
		sideMenu.clickSideMenuPAVehicles();
        vehiclePage.editVehicleByType(VehicleTypePL.PrivatePassenger);
		vehiclePage.setFactoryCostNew(10000);
		vehicleCoverages.selectVehicleCoverageDetailsTab();
        vehicleCoverages.setComprehensive(true);
        vehicleCoverages.setCollision(true);
		// Adding additional insured
		vehiclePage.clickVehicleDetailsTab();
        vehiclePage.addAutoAdditionalInsured("Testing purpose");
		// 310 added or removed
		Vehicle Vehicle = new Vehicle();
		Vehicle.setLocationOfDamage("Left Rear");
		Vehicle.setDamageItem("Tail Light");
		Vehicle.setDamageDescription("Missing");
		vehicleExclusions.fillOutExclusionsAndConditions(Vehicle);

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
		vehiclePage.selectGaragedAtZip("ID");
		vehiclePage.clickOK();

		// If Modification of Insured Vehicle Definition Endorsement 301 is add
		// ed or removed, create UW block issue.
		sideMenu.clickSideMenuPACoverages();
		GenericWorkorderSquireAutoCoverages_Coverages paCoverages = new GenericWorkorderSquireAutoCoverages_Coverages(driver);
		sideMenu.clickSideMenuPACoverages();
        paCoverages.setMedicalCoverage(MedicalLimit.OneK);
		paCoverages.setLiabilityCoverage(LiabilityLimit.OneHundredHigh);
        paCoverages.setUninsuredCoverage(true, UninsuredLimit.OneHundred);
		paCoverages.clickAdditionalCoveragesTab();

		GenericWorkorderSquireAutoCoverages_AdditionalCoverages additioanlCoverages = new GenericWorkorderSquireAutoCoverages_AdditionalCoverages(driver);
		additioanlCoverages.setAccidentalDeath(false);

		paCoverages.clickCoverageExclusionsTab();
		GenericWorkorderSquireAutoCoverages_ExclusionsConditions paExclusions = new GenericWorkorderSquireAutoCoverages_ExclusionsConditions(driver);
		paExclusions.addModificationOfInsuredVehicleDefinitionEndorsement("BMW", "Beemer", "1B45678645456", "1999");

		// modifying the primary driver details
		sideMenu.clickSideMenuPADrivers();
        sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
		paDrivers.clickEditButtonInDriverTableByDriverName(myPolRenewObjPL.pniContact.getLastName());
        paDrivers.setSSN("1" + StringsUtils.generateRandomNumberDigits(8).replace("00", "45"));
		paDrivers.selectMaritalStatus(MaritalStatus.Single);
		paDrivers.clickOk();

		risk.Quote();
        if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();
		uwIssues = risk.getUnderwriterIssues();

		PLUWIssues sqSectionIIIBlockSubmit = PLUWIssues.CitySquireWithFarmTruck;

		@SuppressWarnings("serial")
		List<PLUWIssues> sqSectionIIIBlockIssuances = new ArrayList<PLUWIssues>() {
			{
				this.add(PLUWIssues.FarmTruckOver50K);
				this.add(PLUWIssues.ShowCarMileageOver1000);
				this.add(PLUWIssues.AddedOrRemovedCondition301);
				this.add(PLUWIssues.AddedRemoved310);
				this.add(PLUWIssues.AdditionalInsuredChange);
				this.add(PLUWIssues.ChangeToDriverInfo);
				this.add(PLUWIssues.LineLevelCoverageReducedRemoved);
				this.add(PLUWIssues.VehicleLevelCoverageAddedForFirstTime);
			}
		};
		softAssert.assertFalse(
				!uwIssues.isInList(sqSectionIIIBlockSubmit.getLongDesc()).equals(UnderwriterIssueType.BlockSubmit),
				"Expected error Bind Issuance : " + sqSectionIIIBlockSubmit.getShortDesc() + " is not displayed");

		for (PLUWIssues uwBlockSubmitExpected : sqSectionIIIBlockIssuances) {
			softAssert.assertFalse(
					!uwIssues.isInList(uwBlockSubmitExpected.getLongDesc()).equals(UnderwriterIssueType.BlockIssuance),
					"Expected error Bind Issuance : " + uwBlockSubmitExpected.getShortDesc() + " is not displayed");
		}

		softAssert.assertAll();

	}

	@Test()
	private void TestSiblingPolicyLiabilityLimits() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K,
				MedicalLimit.TenK, true, UninsuredLimit.CSL300K, false, UnderinsuredLimit.CSL300K);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;


        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        GeneratePolicy myInitialSqPolicy = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Initial", "REDWINE")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
        driver.quit();
        
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        Squire mySquire1 = new Squire(SquireEligibility.City);
        mySquire.squirePA = new SquirePersonalAuto();
        GeneratePolicy mySqPol = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire1)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Test", "Sibling")
                .build(GeneratePolicyType.FullApp);

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchSubmission(mySqPol.agentInfo.getAgentUserName(), mySqPol.agentInfo.getAgentPassword(),
				mySqPol.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
		policyInfo.clickEditPolicyTransaction();

		Date dob = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter),
				DateAddSubtractOptions.Year, -22);

		sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
		paDrivers.clickEditButtonInDriverTableByDriverName(mySqPol.pniContact.getFirstName());
        paDrivers.setAutoDriversDOB(dob);
		paDrivers.selectMaritalStatus(MaritalStatus.Single);
		paDrivers.clickOk();

		sideMenu.clickSideMenuPolicyInfo();
		policyInfo.setPolicyInfoOrganizationType(OrganizationType.Sibling);

		// Tab line
        policyInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy, myInitialSqPolicy.squire.getPolicyNumber());
        new GuidewireHelpers(driver).clickNext();

		sideMenu.clickSideMenuPACoverages();
        // checking Squire Auto - Coverages for sibling policy
		LiabilityLimit limit = (new GuidewireHelpers(driver).getRandBoolean()) ? LiabilityLimit.CSL300K
				: ((new GuidewireHelpers(driver).getRandBoolean()) ? LiabilityLimit.OneHundredLow : LiabilityLimit.OneHundredHigh);

		GenericWorkorderSquireAutoCoverages_Coverages coveragePage = new GenericWorkorderSquireAutoCoverages_Coverages(driver);
		coveragePage.setLiabilityCoverage(limit);

		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
		riskAnalysis.Quote();
        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();
        PLUWIssues sqSectionIIIBlockSubmit = PLUWIssues.SiblingPolicyLiabilityLimits;
		FullUnderWriterIssues uwIssues = riskAnalysis.getUnderwriterIssues();
		softAssert.assertFalse(
				!uwIssues.isInList(sqSectionIIIBlockSubmit.getLongDesc()).equals(UnderwriterIssueType.BlockSubmit),
				"Expected error Bind Bind : " + sqSectionIIIBlockSubmit.getShortDesc() + " is not displayed");
		softAssert.assertAll();
	}

	private void addNewhouseholdMemberAndDriver(boolean basicSearch, String firstName, String lastName, AddressInfo address, Date dob)
			throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
		household.clickSearch();
		SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
		addressBook.searchAddressBookByFirstLastName(basicSearch, firstName, lastName, address.getLine1(), address.getCity(),
				address.getState(), address.getZip(), CreateNew.Create_New_Always);
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
		householdMember.setDateOfBirth(dob);
        householdMember.selectRelationshipToInsured(RelationshipToInsured.SignificantOther);
		householdMember.setSSN("2" + StringsUtils.generateRandomNumberDigits(8).replace("00", "34"));
        householdMember.setNewPolicyMemberAlternateID(firstName);
        householdMember.selectNotNewAddressListingIfNotExist(address);
		householdMember.clickRelatedContactsTab();
		householdMember.clickOK();
        sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
		paDrivers.addExistingDriver(firstName);
        paDrivers.selectMaritalStatus(MaritalStatus.Single);
		paDrivers.selectGender(Gender.Male);
		paDrivers.setOccupation("Testing");
		paDrivers.setLicenseNumber("ABCD"+StringsUtils.generateRandomNumberDigits(4));
		State state = State.Idaho;
		paDrivers.selectLicenseState(state);
        paDrivers.selectCommuteType(CommuteType.WorkFromHome);
        paDrivers.setPhysicalImpairmentOrEpilepsy(false);
		paDrivers.selectDriverHaveCurrentInsurance(true);
		paDrivers.setCurrentInsuranceCompanyPolicyNumber("Farm Bureau Policy #: 1B124576");
		paDrivers.clickOk();

		sideMenu.clickSideMenuPAVehicles();
        if (new GuidewireHelpers(driver).errorMessagesExist()) {
			new GuidewireHelpers(driver).clickDiscardUnsavedChangesLink();
            sideMenu.clickSideMenuPADrivers();
            paDrivers.addExistingDriver(firstName);
            paDrivers.selectMaritalStatus(MaritalStatus.Single);
			paDrivers.selectGender(Gender.Male);
			paDrivers.setOccupation("Testing");
			paDrivers.setLicenseNumber("ABCD" + StringsUtils.generateRandomNumberDigits(4));
			paDrivers.selectLicenseState(state);
            paDrivers.selectCommuteType(CommuteType.WorkFromHome);
            paDrivers.setPhysicalImpairmentOrEpilepsy(false);
			paDrivers.selectDriverHaveCurrentInsurance(true);
			paDrivers.setCurrentInsuranceCompanyPolicyNumber("Farm Bureau Policy #: 1B124576");
			paDrivers.clickOk();
            new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();

			sideMenu.clickSideMenuPAVehicles();
        }

	}
}

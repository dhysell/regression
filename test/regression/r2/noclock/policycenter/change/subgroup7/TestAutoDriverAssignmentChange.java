package regression.r2.noclock.policycenter.change.subgroup7;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.enums.State;

import repository.driverConfiguration.Config;
import repository.gw.enums.CommuteType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.HowLongWithoutCoverage;
import repository.gw.enums.LineSelection;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
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
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.PolicyChangeReview;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderLineSelection;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement :US12457: TR - UW Issue needed when 1 driver is rated on more
 *              than 1 passenger vehicle or pickup
 * @RequirementsLink <a href=
 *                   "http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/Product%20Model%20Spreadsheets/squire%20product%20model%20spreadhseets/Squire-Section%20III-Product-Model.xlsx">
 *                   Product model spreadsheet</a>
 * @Description: To validate driver assignment can be removed, added to new
 *               vehicle and motor vehicles.
 * @DATE Oct 26, 2017
 */
public class TestAutoDriverAssignmentChange extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObj;
	private String driverFirstName = "ChangeDr" + StringsUtils.generateRandomNumberDigits(7);
	private GeneratePolicy myNewBusObjPL;
	private Underwriters uw;

	@Test()
	public void testCreateStandardAuto() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		Contact pniDriver = new Contact();
		pniDriver.setFirstName("Three");
		pniDriver.setLastName("Drivers");

		ArrayList<Contact> driversList = new ArrayList<Contact>();
		Contact firstDriver = new Contact();
		firstDriver.setFirstName(driverFirstName);
		driversList.add(firstDriver);

		ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
		Vehicle newVeh = new Vehicle();
		newVeh.setVehicleTypePL(VehicleTypePL.PrivatePassenger);
		newVeh.setDriverPL(pniDriver);
		Vehicle newVeh1 = new Vehicle();
		newVeh1.setVehicleTypePL(VehicleTypePL.PassengerPickup);
		newVeh1.setDriverPL(firstDriver);
		vehicleList.add(newVeh1);
		vehicleList.add(newVeh);

		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh,
				MedicalLimit.FiveK);

		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);
		squirePersonalAuto.setVehicleList(vehicleList);
		squirePersonalAuto.setDriversList(driversList);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        myNewBusObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
				.withInsFirstLastName(pniDriver.getFirstName(), pniDriver.getLastName())
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);

	}

	@Test(dependsOnMethods = { "testCreateStandardAuto" })
	private void testStandardPolDriverChange() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(myNewBusObjPL.agentInfo.getAgentUserName(),
                myNewBusObjPL.agentInfo.getAgentPassword(), myNewBusObjPL.squire.getPolicyNumber());
        Date currentSystemDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter),
				DateAddSubtractOptions.Day, 10);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("Auto Only change", currentSystemDate);
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehicle = new GenericWorkorderVehicles_Details(driver);
		vehicle.removeVehicleByVehicleNumber(1);
        vehicle.editVehicleByType(VehicleTypePL.PrivatePassenger);
        vehicle.selectDriverToAssign(this.driverFirstName);
		vehicle.clickOK();

		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.performRiskAnalysisAndQuote(myNewBusObjPL);

		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter_Supervisor);
		driver.quit();

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				myNewBusObjPL.accountNumber);

        AccountSummaryPC acct = new AccountSummaryPC(driver);
		acct.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);

        sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuRiskAnalysis();
        risk = new GenericWorkorderRiskAnalysis(driver);
		risk.approveAll_IncludingSpecial();
		sideMenu.clickSideMenuQuote();

        policyChangePage = new StartPolicyChange(driver);
		policyChangePage.clickIssuePolicy();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
    }

	@Test(dependsOnMethods = { "testStandardPolDriverChange" })
	private void testAddNewVehicleWithSameDriver() throws Exception {
		// Another change

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                myNewBusObjPL.squire.getPolicyNumber());
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("Add Auto change",
				DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehicle = new GenericWorkorderVehicles_Details(driver);
		vehicle.createVehicleManually();
		vehicle.createGenericVehicle(VehicleTypePL.Motorcycle);
		vehicle.selectGaragedAtZip("ID");
		vehicle.selectDriverToAssign(this.driverFirstName);
		vehicle.clickOK();

		sideMenu.clickSideMenuPolicyChangeReview();
		PolicyChangeReview policyChangeReview = new PolicyChangeReview(driver);
		policyChangeReview.clickChangeConflictsTab();
        policyChangeReview.clickOverrideAllButton();
		policyChangeReview.clickSubmitButton();

		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.performRiskAnalysisAndQuote(myNewBusObjPL);
		driver.quit();

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				myNewBusObjPL.accountNumber);

        AccountSummaryPC acct = new AccountSummaryPC(driver);
		acct.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);

        sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuRiskAnalysis();
        risk = new GenericWorkorderRiskAnalysis(driver);
		risk.approveAll_IncludingSpecial();
		sideMenu.clickSideMenuQuote();

        policyChangePage = new StartPolicyChange(driver);
		policyChangePage.clickIssuePolicy();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());

	}

	//US12623 Move Prior Insurance question from QQ to Driver Screen. validating agent able to answer question to no
	@Test()
	private void testPolicyChangeAddSectionIIIByCSR() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		locationsList.add(new PolicyLocation(locOnePropertyList));

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;


        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        GeneratePolicy myAnoBusObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Adding", "Section")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
        driver.quit();

		// Another change for less than
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(myAnoBusObjPL.agentInfo.getAgentUserName(), myAnoBusObjPL.agentInfo.getAgentPassword(), myAnoBusObjPL.accountNumber);
        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickPolicyNumber(myAnoBusObjPL.squire.getPolicyNumber());

        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("Add Auto change",
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
		paDrivers.addExistingDriver(myAnoBusObjPL.pniContact.getLastName());
        paDrivers.selectMaritalStatus(MaritalStatus.Single);
		paDrivers.selectGender(Gender.Male);
		paDrivers.setOccupation("Testing");
		paDrivers.setLicenseNumber("ABCD454");
		State state = State.Idaho;
		paDrivers.selectLicenseState(state);
        paDrivers.selectCommuteType(CommuteType.WorkFromHome);
        paDrivers.setPhysicalImpairmentOrEpilepsy(false);
		paDrivers.selectDriverHaveCurrentInsurance(false);
		paDrivers.setHowLongWithoutCoverage(HowLongWithoutCoverage.NewDriver);
		paDrivers.clickOk();

		sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehicle = new GenericWorkorderVehicles_Details(driver);
		vehicle.createVehicleManually();
		vehicle.createGenericVehicle(VehicleTypePL.Motorcycle);
		vehicle.selectGaragedAtZip("ID");
		vehicle.selectDriverToAssign(myAnoBusObjPL.pniContact.getLastName());
		vehicle.clickOK();
		
		sideMenu.clickSideMenuPACoverages();
		GenericWorkorderSquireAutoCoverages_Coverages coverages = new GenericWorkorderSquireAutoCoverages_Coverages(driver);
		coverages.setLiabilityCoverage(LiabilityLimit.CSL75K);
		
		coverages.setUninsuredCoverage(true, UninsuredLimit.CSL75K);
        coverages.setUnderinsuredCoverage(true, UnderinsuredLimit.CSL75K);
		sideMenu.clickSideMenuRiskAnalysis();
		
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.performRiskAnalysisAndQuote(myAnoBusObjPL);
		driver.quit();

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter_Supervisor);

		new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				myAnoBusObjPL.accountNumber);

        AccountSummaryPC acct = new AccountSummaryPC(driver);
		acct.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);

        sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuRiskAnalysis();
        risk = new GenericWorkorderRiskAnalysis(driver);
		risk.approveAll_IncludingSpecial();
		sideMenu.clickSideMenuQuote();

        policyChangePage = new StartPolicyChange(driver);
		policyChangePage.clickIssuePolicy();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
    }

}

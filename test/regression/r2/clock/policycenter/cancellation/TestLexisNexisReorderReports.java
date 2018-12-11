package regression.r2.clock.policycenter.cancellation;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.CommuteType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;

import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderInsuranceScore;
import repository.pc.workorders.generic.GenericWorkorderLineSelection;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.generic.GenericWorkorderSquireCLUEAuto;
import repository.pc.workorders.generic.GenericWorkorderSquireCLUEProperty;
import repository.pc.workorders.generic.GenericWorkorderSquireEligibility;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;
import repository.pc.workorders.generic.GenericWorkorderVehicles;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/*
 * Rewrite new Term and full term are not working
 */
/*
 * US13621: Don't force user to click all Risk Analysis tabs to Quote or Submit
 * This test will fail as the latest code is not in REGR environment.
 */
@QuarantineClass
public class TestLexisNexisReorderReports extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy policyObjPLRemoveSecIII, policyObjPLRemoveSecIAndII, myPolicyObjPL, myPolicyObjPLFullTerm;

	@Test
	public void testIssueSquireWithSectionIII() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		locationsList.add(new PolicyLocation(locOnePropertyList));

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();

        SquirePropertyAndLiability property = new SquirePropertyAndLiability();
        property.locationList = locationsList;
        property.liabilitySection = myLiab;

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = property;
        mySquire.squirePA = squirePersonalAuto;


        policyObjPLRemoveSecIII = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL)
                .withLexisNexisData(true, true, false, false, true, true, true)
                .withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
        driver.quit();
        
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter_Supervisor);

        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), policyObjPLRemoveSecIII.squire.getPolicyNumber());
        Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
        Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 1);

		// start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", changeDate);
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles Vehicles = new GenericWorkorderVehicles(driver);
		Vehicles.removeAllVehicles();
        sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers paDrivers = new GenericWorkorderSquireAutoDrivers(driver);
		paDrivers.setCheckBoxInDriverTableByDriverName(policyObjPLRemoveSecIII.pniContact.getLastName());
		paDrivers.clickRemoveButton();
        sideMenu.clickSideMenuLineSelection();
        GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(driver);
		lineSelection.checkPersonalAutoLine(false);
		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
        boolean prequote = false;
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if (quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
            prequote = true;

		}
        sideMenu.clickSideMenuRiskAnalysis();
        risk.approveAll_IncludingSpecial();
        if (prequote) {
			risk.Quote();
        }
		sideMenu.clickSideMenuQuote();
        StartPolicyChange change = new StartPolicyChange(driver);
        change = new StartPolicyChange(driver);
		change.clickIssuePolicy();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new BasePage(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
	}

	@Test
	private void testIssueSquireWithSectionIAndII() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		locationsList.add(new PolicyLocation(locOnePropertyList));

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();

        SquirePropertyAndLiability property = new SquirePropertyAndLiability();
        property.locationList = locationsList;
        property.liabilitySection = myLiab;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = property;

        policyObjPLRemoveSecIAndII = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL)
                .withLexisNexisData(true, true, false, false, true, true, true)
                .withPolTermLengthDays(264)
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
        driver.quit();
        
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter_Supervisor);

		new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                policyObjPLRemoveSecIAndII.squire.getPolicyNumber());
        Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
        Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 1);

		// start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", changeDate);
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propertyDetails = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
		propertyDetails.setCheckBoxByRowInPropertiesTable(1, true);
		propertyDetails.clickRemoveProperty();

		// Try to remove Location after removing property and without changing
		// GaragedAt location
		sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
		propertyLocations.SelectLocationsCheckboxByRowNumber(1);
		propertyLocations.clickRemoveButton();
        sideMenu.clickSideMenuLineSelection();
        GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(driver);
		lineSelection.checkPersonalPropertyLine(false);
		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
        boolean prequote = false;
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if (quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
            prequote = true;

		}
        sideMenu.clickSideMenuRiskAnalysis();
        risk.approveAll_IncludingSpecial();
        if (prequote) {
			risk.Quote();
        }
		sideMenu.clickSideMenuQuote();
        StartPolicyChange change = new StartPolicyChange(driver);
        change = new StartPolicyChange(driver);
		change.clickIssuePolicy();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new BasePage(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
	}

	@Test
	private void testIssueSquirePolWithSectionIAndIIAndIII() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		locationsList.add(new PolicyLocation(locOnePropertyList));

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();

		Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter),
				DateAddSubtractOptions.Day, -55);

        SquirePropertyAndLiability property = new SquirePropertyAndLiability();
        property.locationList = locationsList;
        property.liabilitySection = myLiab;

        Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = property;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL)
                .withPolEffectiveDate(newEff)
				.withLexisNexisData(true, true, false, false, true, true, true)
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
        driver.quit();
        
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				myPolicyObjPL.accountNumber);
        StartCancellation cancelPol = new StartCancellation(driver);
        Date currentDate = myPolicyObjPL.squire.getEffectiveDate();
		Date cancellationDate = DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 20);
		String comment = "Testing Purpose";
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.LossRuns, comment, cancellationDate, true);

	}

	@Test
	private void testIssueSquirePolForRewriteFullTerm() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		locationsList.add(new PolicyLocation(locOnePropertyList));

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();

        SquirePropertyAndLiability property = new SquirePropertyAndLiability();
        property.locationList = locationsList;
        property.liabilitySection = myLiab;

        Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = property;

        myPolicyObjPLFullTerm = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL)
				.withLexisNexisData(true, true, false, false, true, true, true)
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
        driver.quit();
        
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				myPolicyObjPLFullTerm.accountNumber);
        StartCancellation cancelPol = new StartCancellation(driver);
        Date currentDate = myPolicyObjPLFullTerm.squire.getEffectiveDate();
		String comment = "Testing Purpose";
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.LossRuns, comment, currentDate, true);

	}

	@Test(dependsOnMethods = { "testIssueSquirePolForRewriteFullTerm" })
	private void clockMove() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 180);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);
        // delay required to create a claim
	}

	@Test(dependsOnMethods = { "clockMove" })
	private void testRewriteFullTermWithLNOrderings() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObjPLFullTerm.agentInfo.getAgentUserName(),
				myPolicyObjPLFullTerm.agentInfo.getAgentPassword(), myPolicyObjPLFullTerm.accountNumber);
        PolicyMenu policyMenu = new PolicyMenu(driver);
		policyMenu.clickMenuActions();
		policyMenu.clickRewriteFullTerm();
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquireEligibility();
        GenericWorkorderSquireEligibility eligibilityPage = new GenericWorkorderSquireEligibility(driver);
		eligibilityPage.clickNext();
        GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(driver);
		lineSelection.clickNext();

		for (int i = 0; i < 8; i++) {
			eligibilityPage.clickNext();
        }

        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
		coverages.clickSectionIICoveragesTab();
        coverages.clickCoveragesExclusionsAndConditions();

		for (int i = 0; i < 9; i++) {
			eligibilityPage.clickNext();
        }
		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		sideMenu.clickSideMenuPolicyInfo();

		// checking the prefill
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.orderPrefill();
		polInfo.clickReturnToOrderPrefillReport();

		// Insurance CBR
		sideMenu.clickSideMenuPLInsuranceScore();
        GenericWorkorderInsuranceScore insScore = new GenericWorkorderInsuranceScore(driver);
		insScore.clickOrderReport();

		sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers paDrivers = new GenericWorkorderSquireAutoDrivers(driver);
		paDrivers.clickEditButtonInDriverTable(1);
        String errorMessage = "";
		if (!paDrivers.checkIfMVRButtonIsClickable()) {
			errorMessage = errorMessage + "ORDER MVR button is not enabled \n";
		}
		paDrivers.clickOrderMVR();
		paDrivers.clickOk();
        sideMenu.clickSideMenuClueAuto();
        GenericWorkorderSquireCLUEAuto clueAuto = new GenericWorkorderSquireCLUEAuto(driver);
		if (!clueAuto.checkRetrieveCLUE()) {
			errorMessage = errorMessage + "ORDER AUTO button is not enabled \n";
		}
		clueAuto.clickRetrieveCLUE();
		sideMenu.clickSideMenuSquirePropertyCLUE();
        GenericWorkorderSquireCLUEProperty clueProp = new GenericWorkorderSquireCLUEProperty(driver);
		if (!clueProp.checkRetrieveCLUE()) {
			errorMessage = errorMessage + "Clue Property button is not enabled \n";
		}

		clueProp.clickRetrieveCLUE();

		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		risk.Quote();

		if (errorMessage != "") {
			Assert.fail(errorMessage);
		}

	}

	@Test(dependsOnMethods = { "testRewriteFullTermWithLNOrderings", "testIssueSquirePolWithSectionIAndIIAndIII",
			"testIssueSquireWithSectionIII", "testIssueSquireWithSectionIAndII" })
	private void testMoveAnother5Days() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 5);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);
        // delay required to create a claim
	}

	@Test(dependsOnMethods = { "testMoveAnother5Days" })
	private void testAddSectionIIICheckReording() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(policyObjPLRemoveSecIII.agentInfo.getAgentUserName(),
                policyObjPLRemoveSecIII.agentInfo.getAgentPassword(), policyObjPLRemoveSecIII.squire.getPolicyNumber());
        Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
        Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 1);

		// start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("Second", changeDate);
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
		paDrivers.addExistingDriver(policyObjPLRemoveSecIII.pniContact.getFirstName());
        paDrivers.selectMaritalStatus(MaritalStatus.Single);
		paDrivers.selectGender(Gender.Male);
		paDrivers.setOccupation("Testing");
		paDrivers.setLicenseNumber(policyObjPLRemoveSecIII.pniContact.getDriversLicenseNum());
		paDrivers.selectLicenseState(policyObjPLRemoveSecIII.pniContact.getStateLicenced());
		paDrivers.selectCommuteType(CommuteType.WorkFromHome);
        paDrivers.selectDriverHaveCurrentInsurance(true);
		paDrivers.setCurrentInsuranceCompanyPolicyNumber("Farm Bureau Policy #: 1B124576");
		
		String errorMessage = "";
		paDrivers.setPhysicalImpairmentOrEpilepsy(false);
		if (!paDrivers.checkIfMVRButtonIsClickable()) {
			errorMessage = errorMessage + "Order MVR button does not exists \n";
		} else {
			paDrivers.clickOrderMVR();
		}
		paDrivers.clickMotorVehicleRecord();
		paDrivers.clickOk();
        sideMenu.clickSideMenuPAVehicles();

		// Add Vehicle
		sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehicalTab = new GenericWorkorderVehicles_Details(driver);
		vehicalTab.selectFirstVehicleFromPrefillVehicles();
		vehicalTab.editVehicleByVehicleNumber(1);
        vehicalTab.selectDriverToAssign(policyObjPLRemoveSecIII.pniContact.getFirstName());
        vehicalTab.selectGaragedAtZip("ID");
		vehicalTab.clickOK();

		sideMenu.clickSideMenuClueAuto();
        GenericWorkorderSquireCLUEAuto clueAuto = new GenericWorkorderSquireCLUEAuto(driver);
		if (!clueAuto.checkRetrieveCLUE()) {
			errorMessage = errorMessage + "Order CLUE Auto link does not exists in CLUE Auto ";
		}
		clueAuto.clickRetrieveCLUE();
        sideMenu.clickSideMenuRiskAnalysis();

        new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();

		if (errorMessage != "") {
			Assert.fail(errorMessage);
		}
	}

	@Test(dependsOnMethods = { "testMoveAnother5Days" })
	private void testRenewalAddingSectionIAndII() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter_Supervisor);

		new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                policyObjPLRemoveSecIAndII.squire.getPolicyNumber());

        PolicyMenu policyMenu = new PolicyMenu(driver);
		policyMenu.clickRenewPolicy();

        PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
		preRenewalPage.closePreRenewalExplanations(policyObjPLRemoveSecIAndII);
	}

	@Test(dependsOnMethods = { "testRenewalAddingSectionIAndII" })
	private void testAgentLNOrdersByAddingSectionIAndII() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(policyObjPLRemoveSecIAndII.agentInfo.getAgentUserName(),
				policyObjPLRemoveSecIAndII.agentInfo.getAgentPassword(), policyObjPLRemoveSecIAndII.accountNumber);
        AccountSummaryPC acctSummary = new AccountSummaryPC(driver);
		acctSummary.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
		policyInfo.clickEditPolicyTransaction();
		sideMenu.clickSideMenuSquireEligibility();
        sideMenu.clickSideMenuLineSelection();
        GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(driver);
		lineSelection.checkPersonalPropertyLine(true);
		sideMenu.clickSideMenuQualification();
        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
		qualificationPage.setSquireGeneralFullTo(false);
		qualificationPage.setSquireHOFullTo(false, "Testing purpose");
		qualificationPage.clickPL_GLLosses(false);
		qualificationPage.clickPL_GLHazard(false);
		qualificationPage.clickPL_GLManufacturing(false);
		qualificationPage.clickQualificationGLhorses(false);
		qualificationPage.clickPL_GLLivestock(false);
		sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
		propertyLocations.addNewOrSelectExistingLocationQQ(policyObjPLRemoveSecIAndII.squire.propertyAndLiability.locationList.get(0));

		sideMenu.clickSideMenuSquirePropertyDetail();
        PLPolicyLocationProperty property = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
		propertyDetail.fillOutPropertyDetails_FA(property);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        constructionPage.fillOutPropertyConstrustion_FA(property);
		constructionPage.setLargeShed(false);
        GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);
		constructionPage.clickProtectionDetailsTab();
        new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails(driver).fillOutPropertyProtectionDetails_QQ(property);

		protectionPage.clickOK();
        int BuildingNumber = propertyDetail.getSelectedBuildingNum();

		sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		coverages.clickSpecificBuilding(1, BuildingNumber);
		coverages.setCoverageALimit(100000);
		coverages.setCoverageAIncreasedReplacementCost(
				property.getPropertyCoverages().getCoverageA().isIncreasedReplacementCost());
		coverages.setCoverageCValuation(property.getPropertyCoverages());

		sideMenu.clickSideMenuSquirePropertyCLUE();
        GenericWorkorderSquireCLUEProperty clueProp = new GenericWorkorderSquireCLUEProperty(driver);
		if (!clueProp.checkRetrieveCLUE()) {
			Assert.fail("Order CLUE property button does not exists in CLUE Property");
		}
		clueProp.clickRetrieveCLUE();
        sideMenu.clickSideMenuRiskAnalysis();

		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
	}

	@Test(dependsOnMethods = { "testMoveAnother5Days" })
	private void testRewriteNewTermWithLNOrderings() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObjPL.agentInfo.getAgentUserName(),
				myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);
        //        PolicyMenu policyMenu = new PolicyMenu(driver);
//		policyMenu.clickMenuActions();
//		policyMenu.clickRewriteNewTerm();
        new StartRewrite(driver).startRewriteNewTerm();
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquireEligibility();
        GenericWorkorderSquireEligibility eligibilityPage = new GenericWorkorderSquireEligibility(driver);
		eligibilityPage.clickNext();
        GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(driver);
		lineSelection.clickNext();

        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
		qualificationPage.setSquireGeneralFullTo(false);

		qualificationPage.setSquireHOFullTo(false, "Testing purpose");
		qualificationPage.clickPL_GLLosses(false);
		qualificationPage.clickPL_GLHazard(false);
		qualificationPage.clickPL_GLManufacturing(false);
		qualificationPage.clickQualificationGLhorses(false);
		qualificationPage.clickPL_GLLivestock(false);
		qualificationPage.clickQualificationGLCattle(false);

		qualificationPage.setSquireAutoFullTo(false);
		for (int i = 0; i < 8; i++) {
			eligibilityPage.clickNext();
        }

        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
		coverages.clickSectionIICoveragesTab();
        coverages.clickCoveragesExclusionsAndConditions();

		for (int i = 0; i < 9; i++) {
			eligibilityPage.clickNext();
        }
		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		sideMenu.clickSideMenuPolicyInfo();

		// checking the prefill
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.orderPrefill();
		polInfo.clickReturnToOrderPrefillReport();

		// Insurance CBR
		sideMenu.clickSideMenuPLInsuranceScore();
        GenericWorkorderInsuranceScore insScore = new GenericWorkorderInsuranceScore(driver);
		insScore.clickOrderReport();

		sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers paDrivers = new GenericWorkorderSquireAutoDrivers(driver);
		paDrivers.clickEditButtonInDriverTable(1);
        String errorMessage = "";
		if (!paDrivers.checkIfMVRButtonIsClickable()) {
			errorMessage = errorMessage + "Order MVR button is not enabled \n";
		}
		paDrivers.clickOrderMVR();
		paDrivers.clickOk();
        sideMenu.clickSideMenuClueAuto();
        GenericWorkorderSquireCLUEAuto clueAuto = new GenericWorkorderSquireCLUEAuto(driver);
		if (!clueAuto.checkRetrieveCLUE()) {
			errorMessage = errorMessage + "CLUE AUTO button is not enabled \n";
		}
		clueAuto.clickRetrieveCLUE();
		sideMenu.clickSideMenuSquirePropertyCLUE();
        GenericWorkorderSquireCLUEProperty clueProp = new GenericWorkorderSquireCLUEProperty(driver);
		if (!clueProp.checkRetrieveCLUE()) {
			errorMessage = errorMessage + "CLUE Property button is not enabled \n";
		}
		clueProp.clickRetrieveCLUE();

		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		risk.Quote();

		if (errorMessage != "") {
			Assert.fail(errorMessage);
		}
	}

}

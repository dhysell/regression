package regression.r2.noclock.policycenter.busrulesuwissues.umbrella;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.activity.ActivityPopup;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PLUWIssues;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SRPIncident;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.SquireUmbrellaIncreasedLimit;
import repository.gw.enums.TransactionType;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.SquireUmbrellaInfo;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchAccountsPC;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderLineSelection;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_MotorVehicleRecord;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_SelfReportingIncidents;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquireUmbrellaCoverages;
import repository.pc.workorders.generic.GenericWorkorderVehicles;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : Squire-Section III-Product-Model - Bus Rules and UW Issues tab
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/Product%20Model%20Spreadsheets/squire%20product%20model%20spreadhseets/Squire-Section%20III-Product-Model.xlsx">Squire-Section III-Product-Model</a>
 * @Description : Validate all Umbrella business rules
 * @DATE Sep 25, 2017
 */
@QuarantineClass
public class TestUmbrellaBusRules extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObjPL = null;
	private String UB001busRules = "Policy: Underlying squire must have minimum limits of $300,000 CSL. (UB001)";
	private String UB009busRules = "Policy: You are required to at least quote underlying Squire policy renewal in order to quote Umbrella policy renewal. (UB009)";
	private String UB011busRules = "No premium entered for the following coverages: Sixth Million Premium. (UB011)";
	private String UB010busRules = "Policy: You are required to bind underlying Squire policy renewal in order to bind Umbrella policy renewal. (UB010)";
	SoftAssert softAssert = new SoftAssert();
	private GeneratePolicy myPolMVRObjPL;

	@BeforeMethod(alwaysRun = true)
	public void beforeMethod() {
		softAssert = new SoftAssert();
	}

	@Test
	public void testCreateSquireUmbrellaPolFA() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

		SquireLiability liabilitySection = new SquireLiability();
		liabilitySection.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

        SquirePersonalAuto myAuto = new SquirePersonalAuto();
		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K,
				MedicalLimit.TenK);
		coverages.setUninsuredLimit(UninsuredLimit.CSL300K);
		coverages.setUnderinsuredLimit(UnderinsuredLimit.CSL300K);
        myAuto.setCoverages(coverages);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;


        Squire mySquire = new Squire(new GuidewireHelpers(driver).getRandomEligibility());
        mySquire.squirePA = myAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Umbrella", "Rules")
                .build(GeneratePolicyType.FullApp);
        driver.quit();
        
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
		squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_1000000);

        myPolicyObjPL.squireUmbrellaInfo = squireUmbrellaInfo;
        myPolicyObjPL.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.FullApp);
	}

	@Test(dependsOnMethods = {"testCreateSquireUmbrellaPolFA"})
	private void testAddUnderlyingSquireChanges() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				this.myPolicyObjPL.accountNumber);
        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
		accountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);
        new GuidewireHelpers(driver).editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages prCoverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
		prCoverages.clickSectionIICoveragesTab();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages section2Covs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
		section2Covs.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);
        sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_SelfReportingIncidents paDrivers = new GenericWorkorderSquireAutoDrivers_SelfReportingIncidents(driver);
		paDrivers.clickEditButtonInDriverTable(1);
        paDrivers.clickSRPIncidentsTab();
        paDrivers.setInternationalDLIncident(30);
		paDrivers.setUnverifiableDrivingRecordIncident(30);
		paDrivers.setNoProofInsuranceIncident(30);
		paDrivers.clickOk();
        sideMenu.clickSideMenuPACoverages();
        GenericWorkorderSquireAutoCoverages_Coverages coverage = new GenericWorkorderSquireAutoCoverages_Coverages(driver);
		coverage.setUninsuredCoverage(true, UninsuredLimit.Fifty);
		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
		riskAnalysis.performRiskAnalysisAndQuote(myPolicyObjPL);
	}

	@Test(dependsOnMethods = {"testAddUnderlyingSquireChanges"})
	private void testAddUmbrellaBusRules() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter);

		new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                this.myPolicyObjPL.accountNumber);
		new GuidewireHelpers(driver).editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquireUmbrellaCoverages();
        // UB008 - Limit over $2 million
        GenericWorkorderSquireUmbrellaCoverages covs = new GenericWorkorderSquireUmbrellaCoverages(driver);
		covs.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_6000000);

		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
        // UB001 - Umbrella qualification
		softAssert.assertFalse(!risk.getValidationMessagesText().contains(UB001busRules),
				"Expected Quote validation : " + UB001busRules + " is not displayed.");
		risk.clickClearButton();
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();

		// UB003 - <Vehicle> has an SRP of 16 or More
        SearchAccountsPC accountSearchPC = new SearchAccountsPC(driver);
        accountSearchPC.searchAccountByAccountNumber(this.myPolicyObjPL.accountNumber);
        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
		accountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);
        new GuidewireHelpers(driver).editPolicyTransaction();
        sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPACoverages();
        GenericWorkorderSquireAutoCoverages_Coverages coverage = new GenericWorkorderSquireAutoCoverages_Coverages(driver);
		coverage.setLiabilityCoverage(LiabilityLimit.CSL300K);
		coverage.setUninsuredCoverage(true, UninsuredLimit.CSL300K);

		sideMenu.clickSideMenuRiskAnalysis();
		risk.performRiskAnalysisAndQuote(myPolicyObjPL);

        accountSearchPC.searchAccountByAccountNumber(this.myPolicyObjPL.accountNumber);
        accountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.PersonalUmbrella);
        new GuidewireHelpers(driver).editPolicyTransaction();
        sideMenu.clickSideMenuRiskAnalysis();
		risk.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if (quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();

		PLUWIssues umbrellaBlockQuote = PLUWIssues.LimitOver2Million;

		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();

		softAssert.assertFalse(
				!uwIssues.isInList(umbrellaBlockQuote.getLongDesc()).equals(UnderwriterIssueType.BlockQuote),
				"Expected error Block Quote : " + umbrellaBlockQuote.getShortDesc() + " is not displayed");

		PLUWIssues umbrellaBlockBind = PLUWIssues.SRPof16OrMore;

		softAssert.assertFalse(
				!uwIssues.isInList(umbrellaBlockBind.getLongDesc()).equals(UnderwriterIssueType.BlockSubmit),
				"Expected error Bind : " + umbrellaBlockBind.getShortDesc() + " is not displayed");

		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();

		// UB007 - No Auto Umbrella
        accountSearchPC.searchAccountByAccountNumber(this.myPolicyObjPL.accountNumber);
        accountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);
        new GuidewireHelpers(driver).editPolicyTransaction();
        sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles vehicalTab = new GenericWorkorderVehicles(driver);
		vehicalTab.removeAllVehicles();
		sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers paDrivers = new GenericWorkorderSquireAutoDrivers(driver);
		paDrivers.setCheckBoxInDriverTable(1);
		paDrivers.clickRemoveButton();
		sideMenu.clickSideMenuLineSelection();
        GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(driver);
		lineSelection.checkPersonalAutoLine(false);
		sideMenu.clickSideMenuRiskAnalysis();
		risk.performRiskAnalysisAndQuote(myPolicyObjPL);
        accountSearchPC.searchAccountByAccountNumber(this.myPolicyObjPL.accountNumber);
        accountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.PersonalUmbrella);
        new GuidewireHelpers(driver).editPolicyTransaction();
        sideMenu.clickSideMenuRiskAnalysis();
		risk.Quote();
        if (quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();

		umbrellaBlockBind = PLUWIssues.NoAutoUmbrella;
		uwIssues = risk.getUnderwriterIssues();
		softAssert.assertFalse(
				!uwIssues.isInList(umbrellaBlockQuote.getLongDesc()).equals(UnderwriterIssueType.BlockQuote),
				"Expected error Block Quote : " + umbrellaBlockQuote.getShortDesc() + " is not displayed");
		softAssert.assertAll();

	}

	@Test()
	private void testCreateSquireFAWithMVR() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

		SquireLiability liabilitySection = new SquireLiability();
		liabilitySection.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);


        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K, MedicalLimit.TenK);
		coverages.setUninsuredLimit(UninsuredLimit.CSL300K);
		coverages.setUnderinsuredLimit(UnderinsuredLimit.CSL300K);
        SquirePersonalAuto myAuto = new SquirePersonalAuto();
        myAuto.setCoverages(coverages);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;


        Squire mySquire = new Squire(new GuidewireHelpers(driver).getRandomEligibility());
        mySquire.squirePA = myAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolMVRObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
				.withLexisNexisData(true, false, false, false, true, false, false)
                .withInsFirstLastName("Umbrella", "Rules")
                .build(GeneratePolicyType.FullApp);
        driver.quit();
        
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber(myPolMVRObjPL.agentInfo.getAgentUserName(), myPolMVRObjPL.agentInfo.getAgentPassword(), this.myPolMVRObjPL.accountNumber);
        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
		accountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);
        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_MotorVehicleRecord paDrivers = new GenericWorkorderSquireAutoDrivers_MotorVehicleRecord(driver);
		paDrivers.clickEditButtonInDriverTable(1);
        paDrivers.clickMotorVehicleRecord();
		// UB004 - <Driver> has DUI on Squire
		paDrivers.selectMVRIncident(1, SRPIncident.DUI);
		paDrivers.clickOk();

		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
		riskAnalysis.performRiskAnalysisAndQuote(myPolMVRObjPL);
		driver.quit();

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new GuidewireHelpers(driver).setPolicyType(myPolMVRObjPL, GeneratePolicyType.FullApp);
		myPolMVRObjPL.convertTo(driver, GeneratePolicyType.PolicySubmitted);
		driver.quit();

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
		squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_1000000);

        myPolMVRObjPL.squireUmbrellaInfo = squireUmbrellaInfo;
        myPolMVRObjPL.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.FullApp);
	}

	@Test(dependsOnMethods = { "testCreateSquireFAWithMVR" })
	private void testUmbrellaNoPremiumDUIBusRules() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter_Supervisor);

        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolMVRObjPL.accountNumber);
		new GuidewireHelpers(driver).editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
		
		sideMenu.clickSideMenuSquireUmbrellaCoverages();
		// UB008 - Limit over $2 million
        GenericWorkorderSquireUmbrellaCoverages covs = new GenericWorkorderSquireUmbrellaCoverages(driver);
		covs.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_6000000);
		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if (quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();

		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();
		PLUWIssues umbrellaBlockBind = PLUWIssues.DUIOnSquire;

		softAssert.assertFalse(
				!uwIssues.isInList(umbrellaBlockBind.getLongDesc()).equals(UnderwriterIssueType.AlreadyApproved),
				"Expected error Bind : " + umbrellaBlockBind.getShortDesc() + " is not displayed");

        risk.performRiskAnalysisAndQuote(myPolMVRObjPL);
        GenericWorkorderPayment paymentPage = new GenericWorkorderPayment(driver);
        paymentPage.fillOutPaymentPage(myPolMVRObjPL);
		sideMenu.clickSideMenuForms();
		sideMenu.clickSideMenuPayment();
		sideMenu.clickSideMenuQuote();
        ActivityPopup activityPopupPage = new ActivityPopup(driver);
		activityPopupPage.clickCancel();
		sideMenu.clickSideMenuRiskAnalysis();
		
		// Login withAgent to validate UB011 - No premium
        GenericWorkorder workorder = new GenericWorkorder(driver);
		workorder.clickGenericWorkorderSubmitOptionsSubmitOnly();

        GenericWorkorderRiskAnalysis riskPage = new GenericWorkorderRiskAnalysis(driver);
		softAssert.assertFalse(!riskPage.getValidationMessagesText().contains(UB011busRules),
				"Expected Validation Message: ' "+ UB011busRules + " ' is not displayed");

		risk.clickClearButton();
		softAssert.assertAll();
	}

	@Test()
	private void testUmbrellaPolChangeBusRules() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

		SquireLiability liabilitySection = new SquireLiability();
		liabilitySection.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K, MedicalLimit.TenK);
		coverages.setUninsuredLimit(UninsuredLimit.CSL300K);
		coverages.setUnderinsuredLimit(UnderinsuredLimit.CSL300K);
        SquirePersonalAuto myAuto = new SquirePersonalAuto();
        myAuto.setCoverages(coverages);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;


        Squire mySquire = new Squire(new GuidewireHelpers(driver).getRandomEligibility());
        mySquire.squirePA = myAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        GeneratePolicy myPolChangeObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Umbrella", "Rules")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
        driver.quit();

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
		squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_1000000);

        myPolChangeObjPL.squireUmbrellaInfo = squireUmbrellaInfo;
        myPolChangeObjPL.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.PolicyIssued);

        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolChangeObjPL.agentInfo.getAgentUserName(), myPolChangeObjPL.agentInfo.getAgentPassword(), myPolChangeObjPL.squireUmbrellaInfo.getPolicyNumber());
        Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
        Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 1);
        SideMenuPC sideMenu = new SideMenuPC(driver);
		// start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", changeDate);
		sideMenu.clickSideMenuSquireUmbrellaCoverages();
        GenericWorkorderSquireUmbrellaCoverages umbCovs = new GenericWorkorderSquireUmbrellaCoverages(driver);
		umbCovs.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_4000000);

        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if (quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();

		PLUWIssues umbrellaBlockQuote = PLUWIssues.PolicyChange;

		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();

		softAssert.assertFalse(
				!uwIssues.isInList(umbrellaBlockQuote.getLongDesc()).equals(UnderwriterIssueType.BlockQuote),
				"Expected error Block Quote : " + umbrellaBlockQuote.getShortDesc() + " is not displayed");

		softAssert.assertAll();

	}

	@Test()
	private void testUmbrellaRenewalBusRules() throws Exception {
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

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K, MedicalLimit.TenK, true, UninsuredLimit.CSL300K, false, UnderinsuredLimit.CSL300K);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;


        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        GeneratePolicy myRenewPolObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
                .withPolTermLengthDays(79)
                .withInsFirstLastName("Renewal", "Umbrella")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
        driver.quit();

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
		squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_2000000);

        myRenewPolObj.squireUmbrellaInfo = squireUmbrellaInfo;
        myRenewPolObj.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.PolicyIssued);
        driver.quit();

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter_Supervisor);

		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                myRenewPolObj.squire.getPolicyNumber());
        StartRenewal renewal = new StartRenewal(driver);
        renewal.startRenewal();
        InfoBar infoBar = new InfoBar(driver);
		infoBar.clickInfoBarPolicyNumber();

		renewal.waitForPreRenewalDirections();
        PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
		preRenewalPage.closePreRenewalExplanations(myRenewPolObj);

		
		//Umbrella Policy
        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
        policySearchPC.searchPolicyByPolicyNumber(myRenewPolObj.squireUmbrellaInfo.getPolicyNumber());
        renewal.startRenewal();
        infoBar.clickInfoBarPolicyNumber();

		renewal.waitForPreRenewalDirections();
        preRenewalPage.closePreRenewalExplanations(myRenewPolObj);
        PolicySummary policySummary = new PolicySummary(driver);
		policySummary.clickPendingTransaction(TransactionType.Renewal);

		new GuidewireHelpers(driver).editPolicyTransaction();

        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		quotePage.clickQuote();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		softAssert.assertFalse(
				!risk.getValidationMessagesText()
						.contains(UB009busRules),
				"Expected Validation Message: '" +UB009busRules + "' is not displayed");
		risk.clickClearButton();
		
		
		//Quoting Squire policy
        policySearchPC.searchPolicyByPolicyNumber(myRenewPolObj.squire.getPolicyNumber());

        policySummary.clickPendingTransaction(TransactionType.Renewal);

        new GuidewireHelpers(driver).editPolicyTransaction();
        quotePage.clickQuote();

		
		//quoting Umbrella policy
        policySearchPC.searchPolicyByPolicyNumber(myRenewPolObj.squireUmbrellaInfo.getPolicyNumber());
		policySummary.clickPendingTransaction(TransactionType.Renewal);

        quotePage.clickQuote();
        GenericWorkorder genericWO = new GenericWorkorder(driver);
		genericWO.clickGenericWorkorderSubmitOptionsRenew();
        softAssert.assertFalse(
				!risk.getValidationMessagesText()
						.contains(UB010busRules),
				"Expected Validation Message: '" +UB010busRules + "' is not displayed");
		
		softAssert.assertAll();
	}
}

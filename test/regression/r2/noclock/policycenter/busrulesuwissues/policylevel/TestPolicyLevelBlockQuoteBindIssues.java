package regression.r2.noclock.policycenter.busrulesuwissues.policylevel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.driverConfiguration.DriverBuilder;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.AddressType;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InsuranceScore;
import repository.gw.enums.LineSelection;
import repository.gw.enums.Location.ProtectionClassCode;
import repository.gw.enums.LossRatioDiscounts;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.OrganizationTypePL;
import repository.gw.enums.PLUWIssues;
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
import repository.gw.enums.SquireUmbrellaIncreasedLimit;
import repository.gw.enums.TransactionType;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.cc.GenerateExposure;
import repository.gw.generate.cc.GenerateFNOL;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PLPropertyCoverages;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.SquireUmbrellaInfo;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInsured;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderInsuranceScore;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_LossRatios;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_PriorLosses;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.generic.GenericWorkorderSquireEligibility;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : Policy Level Forms Product Model Spreadsheet
 * @RequirementsLink <a href=
 * "http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/Product%20Model%20Spreadsheets/squire%20product%20model%20spreadhseets/Policy%20Level%20Forms%20Product%20Model%20Spreadsheet.xlsx">
 * Policy Level Forms Product Model Spreadsheet </a>
 * @Description: Updated as per the new requirement on US14127
 * @DATE Sep 28, 2017
 */
@QuarantineClass
public class TestPolicyLevelBlockQuoteBindIssues extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObjPL = null;
	private ClaimsUsers user = ClaimsUsers.rburgoyne;
	private String password = "gw";

	// FNOL Specific Strings
	private String incidentName = "Random";
	private String lossDescription = "Loss Description Test";
	private String lossCause = "Random";
	private String lossRouter = "Minor Incident";
	SoftAssert softAssert = new SoftAssert();

	@BeforeMethod(alwaysRun = true)
	public void beforeMethod() {
		softAssert = new SoftAssert();
	}

	@Test()
	public void testCreateSquireFA() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

		ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs = new ArrayList<PolicyInfoAdditionalNamedInsured>();
		listOfANIs.add(new PolicyInfoAdditionalNamedInsured(ContactSubType.Person,
				"Test" + StringsUtils.generateRandomNumberDigits(8), "ANI" + StringsUtils.generateRandomNumberDigits(8),
				AdditionalNamedInsuredType.Spouse, new AddressInfo(true)) {
			{
				this.setNewContact(CreateNew.Create_New_Always);
			}
		});

		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;


		Squire mySquire = new Squire(new GuidewireHelpers(driver).getRandomEligibility());
		mySquire.squirePA = new SquirePersonalAuto();
		mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
				.withInsFirstLastName("PolLevel", "Block")
				.withANIList(listOfANIs)
				.withSocialSecurityNumber(StringsUtils.generateRandomNumberDigits(9))
				.build(GeneratePolicyType.FullApp);
	}

    @Test(dependsOnMethods = {"testCreateSquireFA"})
	private void testAddBlockQuoteBindIssuesRules() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(),
				myPolicyObjPL.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();

		// SQ002 - Term type other
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
		policyInfo.setPolicyInfoTermType("Other");

		// SQ004 - Squire Organization type of other
		policyInfo.setPolicyInfoOrganizationType(OrganizationTypePL.Other);
        policyInfo.setPolicyInfoDescription("Testing Description");

		// SQ017 - Effective date "back to the future"
		// SQ013 - 10 day binding authority
		Date newEffectiveDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter),
				DateAddSubtractOptions.Day, -11);
		policyInfo.setPolicyInfoEffectiveDate(DateUtils.dateFormatAsString("MM/dd/yyyy", newEffectiveDate));

		// SQ042 - Designated Out-of-State
		policyInfo.clickPolicyInfoPrimaryNamedInsured();
		GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
		additionalInterests.setAddressListing("New...");
        GenericWorkorderAdditionalInsured additionalInsured = new GenericWorkorderAdditionalInsured(driver);
		additionalInsured.setAddressLine1("3696 S 6800 W");
		additionalInsured.setContactEditAddressCity("West Valley City");
        // additionalInsured.setState(State.Utah);
		additionalInsured.setZipCode("84128");
        additionalInsured.selectAddressType(AddressType.Home);
        GenericWorkorderAdditionalNamedInsured namedInsured = new GenericWorkorderAdditionalNamedInsured(driver);
        namedInsured.setReasonForContactChange("Testing purpose");
		namedInsured.clickUpdate();

		// Adding more $10 million property
		sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		coverages.clickSpecificBuilding(1, 1);
		coverages.setCoverageALimit(12000000);

		// SQ020 - Address not found
		sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
		propertyLocations.clickNewLocation();
        propertyLocations.selectLocationAddress("New...");
		propertyLocations.addLocationInfoFA(new AddressInfo(), 2, 2);
//        propertyLocations.setAddressLine1("456 N Main St");
		propertyLocations.clickStandardizeAddress();
		propertyLocations.clickOK();

		// SQ043 - High Protection Class
		// SQ044 - Diff. Auto and Manual PC Code
		sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
		propertyDetail.clickViewOrEditBuildingButton(1);
        propertyDetail.setManualPublicProtectionClassCode(ProtectionClassCode.Prot9);
		propertyDetail.clickOk();

		// SQ016 - Prior losses
		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis_PriorLosses risk = new GenericWorkorderRiskAnalysis_PriorLosses(driver);
		risk.clickPriorLossesCardTab();
		risk.setLossHistoryType("Manually Entered");
        risk.setManualLossHistory(1, "Section III - Auto Line",
				DateUtils.dateFormatAsString("MM/dd/yyyy", DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter)),
				"Testing", "2000", "2000", "2000", "Open");

		// SQ005 - Squire Insurance score equal or less than 500

		//SQ048 - Agent Loss Ratio Change
        GenericWorkorderRiskAnalysis_LossRatios lossRatio = new GenericWorkorderRiskAnalysis_LossRatios(driver);
        lossRatio.clickLossRatioTab();
		lossRatio.clickLossRatioAutoTab();
		lossRatio.setLossRatioAutoDiscount(LossRatioDiscounts.ZERODISCOUNT);
		risk.setAutoReasonForChange("Testing purpose");

		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		risk.clickUWIssuesTab();
		risk.Quote();
        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
		}

		sideMenu.clickSideMenuRiskAnalysis();
		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();

		@SuppressWarnings("serial")
		List<PLUWIssues> sqPolicyLevelBlockIssuance = new ArrayList<PLUWIssues>() {
			{
				this.add(PLUWIssues.PriorLosses);
				this.add(PLUWIssues.DesignatedOutOfState);
				this.add(PLUWIssues.OrganizationTypeOfOther);
				this.add(PLUWIssues.TermTypeOther);
			}
		};

		for (PLUWIssues uwBlockBindExpected : sqPolicyLevelBlockIssuance) {
			softAssert.assertFalse(
					!uwIssues.isInList(uwBlockBindExpected.getLongDesc()).equals(UnderwriterIssueType.BlockIssuance),
					"Expected Blocking Issuance : " + uwBlockBindExpected.getShortDesc() + " is not displayed");
		}

		@SuppressWarnings("serial")
		List<PLUWIssues> sqPolicyLevelBlockBind = new ArrayList<PLUWIssues>() {
			{
				this.add(PLUWIssues.HighPropertyLimit);
				this.add(PLUWIssues.TenDaySubmittingAuthority);
				this.add(PLUWIssues.AgentLossRatioChange);
			}
		};

		for (PLUWIssues uwBlockBindExpected : sqPolicyLevelBlockBind) {
			softAssert.assertFalse(
					!uwIssues.isInList(uwBlockBindExpected.getLongDesc()).equals(UnderwriterIssueType.BlockSubmit),
					"Expected Blocking Bind : " + uwBlockBindExpected.getShortDesc() + " is not displayed");
		}

		@SuppressWarnings("serial")
		List<PLUWIssues> sqPolicyLevelInfo = new ArrayList<PLUWIssues>() {
			{
				this.add(PLUWIssues.HighProtectionClass);
				this.add(PLUWIssues.ManualPCCodeChanged);
			}
		};

		for (PLUWIssues uwBlockBindExpected : sqPolicyLevelInfo) {
			softAssert.assertFalse(
					!uwIssues.isInList(uwBlockBindExpected.getLongDesc()).equals(UnderwriterIssueType.Informational),
					"Expected Information : " + uwBlockBindExpected.getShortDesc() + " is not displayed");
		}

		PLUWIssues policyLevelBlockingQuote = PLUWIssues.TenmillionPolicy;

		softAssert.assertFalse(
				!uwIssues.isInList(policyLevelBlockingQuote.getLongDesc()).equals(UnderwriterIssueType.BlockQuote),
				"Expected error Blocking Quote : " + policyLevelBlockingQuote.getShortDesc() + " is not displayed");

        if (!myPolicyObjPL.squire.squireEligibility.equals(SquireEligibility.City)) {
			PLUWIssues policyLevelApproved = PLUWIssues.InsuranceScoreLessThan500;

			softAssert.assertFalse(
					!uwIssues.isInList(policyLevelApproved.getLongDesc()).equals(UnderwriterIssueType.AlreadyApproved),
					"Expected error AlreadyApproved : " + policyLevelApproved.getShortDesc() + " is not displayed");
		}

		softAssert.assertAll();
	}

    @Test(dependsOnMethods = {"testAddBlockQuoteBindIssuesRules"})
	private void testAddBlockQuoteBindIssuesByUW() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter_Supervisor);
		new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				this.myPolicyObjPL.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);

		// SQ009 - Squire Credit Report Contact Removed
		sideMenu.clickSideMenuPLInsuranceScore();
        GenericWorkorderInsuranceScore creditReport = new GenericWorkorderInsuranceScore(driver);
		creditReport.clickEditInsuranceReport();
		creditReport.selectCreditReportIndividual(this.myPolicyObjPL.aniList.get(0).getPersonFirstName());
		creditReport.clickOrderReport();

		sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
		policyInfo.removeANI(this.myPolicyObjPL.aniList.get(0).getPersonFirstName());

		sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers hmember = new GenericWorkorderPolicyMembers(driver);
		hmember.clickRemoveMember(this.myPolicyObjPL.aniList.get(0).getPersonFirstName());

		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
        softAssert.assertFalse(
				!risk.getValidationMessagesText()
				.contains("Policy: Credit Report is required to quote policy. (SQ011)"),
				"Expected validation 'Policy: Credit Report is required to quote policy. (SQ011)' is not displayed.");
		risk.clickClearButton();
		sideMenu.clickSideMenuPLInsuranceScore();
		creditReport.selectCreditReportIndividual(myPolicyObjPL.pniContact.getFirstName());
		creditReport.clickOrderReport();

		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();

		softAssert.assertAll();
	}

	/**
     * @throws Exception
	 * @Author nvadlamudi
	 * @Requirement : US14127: **HOT FIX** Modify UW Issue SQ010 to no longer include City Squire
	 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/Product%20Model%20Spreadsheets/squire%20product%20model%20spreadhseets/Policy%20Level%20Forms%20Product%20Model%20Spreadsheet.xlsx">Policy Level Product Model Spreadsheet</a>
	 * @Description : validating UW Issue is not displayed for City Squire and should display only for Country and Farm & Ranch
	 * @DATE Feb 22, 2018
	 */
	@Test()
	private void testSquireAutoOnly() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.squirePA = new SquirePersonalAuto();

        GeneratePolicy myAutoPolicyObj = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
				.withInsFirstLastName("PolBus", "AutoOnly")
				.withPolOrgType(OrganizationType.Individual)
				.build(GeneratePolicyType.FullApp);

		new Login(driver).loginAndSearchSubmission(myAutoPolicyObj.agentInfo.getAgentUserName(),
				myAutoPolicyObj.agentInfo.getAgentPassword(), myAutoPolicyObj.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquireEligibility();
        GenericWorkorderSquireEligibility eligibilityPage = new GenericWorkorderSquireEligibility(driver);
		eligibilityPage.chooseCity();

		// SQ010 - City, Country, or Farm & Ranch Squire Auto only
		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();

		sideMenu.clickSideMenuRiskAnalysis();
		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();

        //Part of DE7009 - changing the Block Issuance to Block Submit.
		PLUWIssues sqPolicyLevelBlockIssuance = PLUWIssues.CountryFRAutoOnly;

		softAssert.assertFalse(
				uwIssues.isInList(sqPolicyLevelBlockIssuance.getLongDesc()).equals(UnderwriterIssueType.BlockSubmit),
				"UnExpected Blocking Submit : " + sqPolicyLevelBlockIssuance.getShortDesc() + " is displayed for City Squire");

		// changing to Country or Farm & Ranch
        new GuidewireHelpers(driver).editPolicyTransaction();
		SquireEligibility squireType = (new GuidewireHelpers(driver).getRandBoolean()) ? SquireEligibility.Country : SquireEligibility.FarmAndRanch;
		sideMenu.clickSideMenuSquireEligibility();
        eligibilityPage.chooseEligibility(squireType);
		sideMenu.clickSideMenuRiskAnalysis();

		risk.Quote();

		sideMenu.clickSideMenuRiskAnalysis();
		uwIssues = risk.getUnderwriterIssues();

		softAssert.assertFalse(
				!uwIssues.isInList(sqPolicyLevelBlockIssuance.getLongDesc()).equals(UnderwriterIssueType.BlockSubmit),
				"Expected Blocking Submit : " + sqPolicyLevelBlockIssuance.getShortDesc() + " is not displayed.");


		softAssert.assertAll();
	}

	@Test()
	private void testStopBindUWIssue() throws Exception {
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
		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K, MedicalLimit.TenK);
		coverages.setUninsuredLimit(UninsuredLimit.CSL300K);
		coverages.setUnderinsuredLimit(UnderinsuredLimit.CSL300K);
		myAuto.setCoverages(coverages);

		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;
		myPropertyAndLiability.liabilitySection = liabilitySection;


		Squire mySquire = new Squire(new GuidewireHelpers(driver).getRandomEligibility());
		mySquire.squirePA = myAuto;
		mySquire.propertyAndLiability = myPropertyAndLiability;

        GeneratePolicy myPolSQObjPL = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
				.withInsFirstLastName("PolBus", "Rules")
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
        driver.quit();

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
		squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_1000000);

		myPolSQObjPL.squireUmbrellaInfo = squireUmbrellaInfo;
		myPolSQObjPL.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.PolicyIssued);
		driver.quit();
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		// standard fire
		ArrayList<PolicyLocation> locationsList1 = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList1 = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList1.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));
		PolicyLocation locToAdd1 = new PolicyLocation(locOnePropertyList1);

		locToAdd1.setPlNumAcres(11);
		locationsList1.add(locToAdd1);

		PLPropertyCoverages propertyCoverages = new PLPropertyCoverages();
		propertyCoverages.getCoverageA().setIncreasedReplacementCost(false);

		StandardFireAndLiability myStandardFire = new StandardFireAndLiability();
		myStandardFire.setLocationList(locationsList1);
		myStandardFire.setStdFireCommodity(false);
		myPolSQObjPL.standardFire = myStandardFire;
		myPolSQObjPL.pniContact.setInsuranceScore(InsuranceScore.NeedsOrdered);
			
		myPolSQObjPL.lineSelection.add(LineSelection.StandardFirePL);
		myPolSQObjPL.addLineOfBusiness(ProductLineType.StandardFire, GeneratePolicyType.FullApp);
		driver.quit();
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchSubmission(myPolSQObjPL.agentInfo.getAgentUserName(), myPolSQObjPL.agentInfo.getAgentPassword(), myPolSQObjPL.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
        // SQ049 - Stop bind
		sideMenu.clickSideMenuRiskAnalysis();
		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();

		PLUWIssues sqPolicyLevelBlockIssuance = PLUWIssues.StopSubmit;

		softAssert.assertFalse(
				!uwIssues.isInList(sqPolicyLevelBlockIssuance.getLongDesc()).equals(UnderwriterIssueType.BlockSubmit),
				"Expected error Blocking Bind : " + sqPolicyLevelBlockIssuance.getShortDesc() + " is not displayed");

		softAssert.assertAll();
	}

	@Test()
	private void testCheckSiblingPolUWIssues() throws Exception {
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

        GeneratePolicy mySquirePolicy = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
				.withInsFirstLastName("Test", "SqForSib")
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
        driver.quit();
        
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		SquireLiability myLiab1 = new SquireLiability();
		myLiab1.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_100000_CSL);

		SquirePersonalAuto myAuto1 = new SquirePersonalAuto();
		SquirePersonalAutoCoverages coverages1 = new SquirePersonalAutoCoverages(LiabilityLimit.CSL100K,
				MedicalLimit.TenK, true, UninsuredLimit.CSL100K, false, UnderinsuredLimit.CSL100K);
		myAuto1.setCoverages(coverages1);

		SquirePropertyAndLiability myPropertyAndLiability1 = new SquirePropertyAndLiability();
		myPropertyAndLiability1.locationList = locationsList;
		myPropertyAndLiability1.liabilitySection = myLiab1;


		Squire mySquire1 = new Squire(SquireEligibility.City);
		mySquire1.squirePA = myAuto1;
		mySquire1.propertyAndLiability = myPropertyAndLiability1;

        GeneratePolicy mySiblingPolicy = new GeneratePolicy.Builder(driver)
				.withProductType(ProductLineType.Squire)
				.withSiblingPolicy(mySquirePolicy, "Test", "Sibling")
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
				.build(GeneratePolicyType.FullApp);
        driver.quit();
        
        cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(),
				uw.getUnderwriterPassword(), mySiblingPolicy.accountNumber);

		SideMenuPC sideMenu = new SideMenuPC(driver);
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		new GuidewireHelpers(driver).editPolicyTransaction();

		sideMenu.clickSideMenuPolicyInfo();
		// ANI
		PolicyInfoAdditionalNamedInsured ani = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person,
				"Test" + StringsUtils.generateRandomNumberDigits(8),
				"ANINew" + StringsUtils.generateRandomNumberDigits(8), AdditionalNamedInsuredType.ParentGuardian,
				new AddressInfo(false));
		ani.setHasMembershipDues(true);
		ani.setNewContact(CreateNew.Create_New_Always);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.addAdditionalNamedInsured(mySiblingPolicy.basicSearch, ani);
		new GuidewireHelpers(driver).clickNext();
        sideMenu.clickSideMenuHouseholdMembers();
		// checking Squire Auto - Coverages for sibling policy
		sideMenu.clickSideMenuPADrivers();
        paDrivers.clickEditButtonInDriverTableByDriverName(mySiblingPolicy.pniContact.getFirstName());
        paDrivers.selectMaritalStatus(MaritalStatus.Married);
		paDrivers.clickOk();

		sideMenu.clickSideMenuRiskAnalysis();
		risk.Quote();

		sideMenu.clickSideMenuRiskAnalysis();
        // SQ051 - Sibling ANI
		// SQ053 - Sibling and not single

		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();

		softAssert.assertFalse(
				!uwIssues.isInList(PLUWIssues.SiblingANI.getLongDesc()).equals(UnderwriterIssueType.BlockIssuance),
				"Expected Blocking Issuance : " + PLUWIssues.SiblingANI.getShortDesc() + " is not displayed");
		softAssert.assertFalse(
				!uwIssues.isInList(PLUWIssues.SiblingNotSingle.getLongDesc()).equals(UnderwriterIssueType.BlockSubmit),
				"Expected Blocking Bind : " + PLUWIssues.SiblingNotSingle.getShortDesc() + " is not displayed");


		softAssert.assertAll();
	}

	@Test()
	private void testPolicyChangePolicyLevelUWIssues() throws Exception {
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
        Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -9);

		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;
		myPropertyAndLiability.liabilitySection = myLiab;


		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.squirePA = squirePersonalAuto;
		mySquire.propertyAndLiability = myPropertyAndLiability;

        GeneratePolicy mySqChangePolObj = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
				.withInsFirstLastName("PolBus", "Change")
				.withPolEffectiveDate(newEff)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);

		LocalDate dateOfLoss = DateUtils.convertDateToLocalDate(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));

        cf = new Config(ApplicationOrCenter.ClaimCenter);
        WebDriver driver = new DriverBuilder().buildGWWebDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver)
				.withCreatorUserNamePassword(user.toString(), password)
				.withSpecificIncident(incidentName)
				.withLossDescription(lossDescription).withLossCause(lossCause)
				.withtopLevelCoverage(lossCause)
				.withDateOfLoss(dateOfLoss)
				.withLossRouter(lossRouter)
				.withAdress("Random")
				.withPolicyNumber(mySqChangePolObj.squire.getPolicyNumber())
				.build(GenerateFNOLType.Property);
		new GuidewireHelpers(driver).logout();

        new GenerateExposure.Builder(driver)
		.withCreatorUserNamePassword(ClaimsUsers.rburgoyne.toString(), "gw")
		.withClaimNumber(myFNOLObj.claimNumber)
		.withCoverageType("Dwelling - Property Damage")
		.build();

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(mySqChangePolObj.agentInfo.getAgentUserName(), mySqChangePolObj.agentInfo.getAgentPassword(), mySqChangePolObj.squire.getPolicyNumber());

        // add 10 days to current date
		Date changeDate = DateUtils.dateAddSubtract(newEff, DateAddSubtractOptions.Day, 3);

		// start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", changeDate);

		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
		propertyLocations.clickEditLocation(1);
//        propertyLocations.setAddressLine2("APT 4003");
//		propertyLocations.setNumberOfResidence(12);
		propertyLocations.clickStandardizeAddress();
		propertyLocations.clickOK();

		sideMenu.clickSideMenuSquirePropertyDetail();

		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
		propertyDetail.clickViewOrEditBuildingButton(1);
        // SQ014 - Agent claim item change
        // SQ015 - UW claim item change
		// SQ045 - Existing Location Change
		propertyDetail.clickPropertyConstructionTab();
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        constructionPage.setConstructionType(ConstructionTypePL.NonFrame);
		constructionPage.setSquareFootage(1000);
        GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);
		protectionPage.clickOK();

		sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverageScreen = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		coverageScreen.clickSpecificBuilding(1, 1);
		coverageScreen.setCoverageALimit(1200000);
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		risk.Quote();
        sideMenu.clickSideMenuRiskAnalysis();
        FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();

		softAssert.assertFalse(
				!uwIssues.isInList(PLUWIssues.ExistingLocationChange.getLongDesc()).equals(UnderwriterIssueType.Informational),
				"Expected Blocking Information : " + PLUWIssues.ExistingLocationChange.getShortDesc() + " is not displayed");

		softAssert.assertFalse(
				!uwIssues.isInList(PLUWIssues.AgentClaimItemChange.getLongDesc()).equals(UnderwriterIssueType.BlockSubmit),
				"Expected Blocking Information : " + PLUWIssues.AgentClaimItemChange.getShortDesc() + " is not displayed");

		driver.quit();
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySqChangePolObj.squire.getPolicyNumber());
        PolicySummary summaryPage = new PolicySummary(driver);
		summaryPage.clickPendingTransaction(TransactionType.Policy_Change);
        new GuidewireHelpers(driver).editPolicyTransaction();
		sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuRiskAnalysis();
        risk = new GenericWorkorderRiskAnalysis(driver);
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		risk.Quote();
        sideMenu.clickSideMenuRiskAnalysis();
        uwIssues = risk.getUnderwriterIssues();

		softAssert.assertFalse(
				!uwIssues.isInList(PLUWIssues.UWClaimItemChange.getLongDesc()).equals(UnderwriterIssueType.BlockSubmit),
				"Expected Blocking Information : " + PLUWIssues.UWClaimItemChange.getShortDesc() + " is not displayed");


		softAssert.assertAll();
	}

	@Test()
	private void testCreateStandardFireForFinancingRule() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		PLPolicyLocationProperty property1 = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises);
		locOnePropertyList.add(property1);

		PolicyLocation loc = new PolicyLocation(locOnePropertyList);
		loc.setPlNumResidence(1);
		loc.setPlNumAcres(5);
		locationsList.add(loc);
		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_50);

        GeneratePolicy myStdFirePolObj = new GeneratePolicy.Builder(driver)
				.withProductType(ProductLineType.StandardFire)
				.withLineSelection(LineSelection.StandardFirePL)
				.withCreateNew(CreateNew.Create_New_Always)
				.withPolTermLengthDays(45)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("PolBus", "FireRule")
				.withPolOrgType(OrganizationType.Individual)
				.withPolicyLocations(locationsList)
				.build(GeneratePolicyType.FullApp);

		// SQ059 - Financing Rule
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchSubmission(myStdFirePolObj.agentInfo.getAgentUserName(), myStdFirePolObj.agentInfo.getAgentPassword(), myStdFirePolObj.accountNumber);

		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPayment();
        String paymentFinacialMessage = "Choose to finance the policy ONLY if the insured has multiple policies that are billed together and combined total is over or equal to $150. Select same payment plan on ALL policies. Contact Account Receivable when billing policies together.";
        softAssert.assertFalse(!new GuidewireHelpers(driver).checkIfElementExists("//label[contains(text(), '" + paymentFinacialMessage + "')]", 1000),
                "Expected Payment page message : " + paymentFinacialMessage + " is not displayed.");

		new GuidewireHelpers(driver).editPolicyTransaction();
		sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
		propertyLocations.clickNewLocation();
//        propertyLocations.setAddressLine1("abcd");
//		propertyLocations.setAddressLine2("APT 4003");
//        propertyLocations.setCity(myStdFirePolObj.pniContact.getAddress().getCity());
//		propertyLocations.setZipCode(myStdFirePolObj.pniContact.getAddress().getZip());
//        propertyLocations.selectCounty(myStdFirePolObj.pniContact.getAddress().getCounty());
//		propertyLocations.setAcres(10);
//		propertyLocations.setNumberOfResidence(2);
		propertyLocations.clickStandardizeAddress();
        propertyLocations.clickOK();

		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();

		sideMenu.clickSideMenuRiskAnalysis();
		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();

		softAssert.assertFalse(
				!uwIssues.isInList(PLUWIssues.ISRBNotReturnAddress.getLongDesc()).equals(UnderwriterIssueType.Informational),
				"Expected Blocking Information : " + PLUWIssues.ISRBNotReturnAddress.getShortDesc() + " is not displayed");


		softAssert.assertAll();

	}


}

package previousProgramIncrement.pi2_062818_090518.f284_AutoPolicyChange;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.State;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.AddressType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InceptionDateSections;
import repository.gw.enums.LineSelection;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.OrganizationTypePL;
import repository.gw.enums.PLUWIssues;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
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
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInsured;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_PriorLosses;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author nvadlamudi
 * @Requirement :US15543: Disable/change UW Issues - Policy Level (new activity)
 * @RequirementsLink <a href=
 *                   "https://fbmicoi.sharepoint.com/:x:/r/sites/ARTists2/_layouts/15/Doc.aspx?sourcedoc=%7Bd5e603f9-03a2-4009-abce-484c8a72dccf%7D&action=default">
 *                   UW Issues to Change for Auto Issue</a>
 * @Description: Validating SQ004, SQ016, SQ042, SQ051, SQ058 rules in
 *               submission and policy change
 * @DATE Jul 17, 2018
 */
public class US15543DisableChangePolicyLevelUWNewActivity extends BaseTest {
	private GeneratePolicy myPolicyObjPL;	
	WebDriver driver;
	private GeneratePolicy mySquirePolicy;

    @Test
	public void testCheckPolicyLevelUWIssuesInSubmission() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		SoftAssert softAssert = new SoftAssert();
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

		myPolicyObjPL = new GeneratePolicy.Builder(driver).withSquire(mySquire).withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
				.withInsFirstLastName("PolLevel", "Block").withANIList(listOfANIs)
				.withSocialSecurityNumber("4" + StringsUtils.generateRandomNumberDigits(8)).isDraft()
				.build(GeneratePolicyType.FullApp);
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(),
				myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);

		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();
		GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);

		// SQ004 - Squire Organization type of other
		policyInfo.setPolicyInfoOrganizationType(OrganizationTypePL.Other);
		policyInfo.setPolicyInfoDescription("Testing Description");

		// SQ042 - Designated Out-of-State
		policyInfo.clickPolicyInfoPrimaryNamedInsured();
		GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
		additionalInterests.setAddressListing("New...");
		GenericWorkorderAdditionalInsured additionalInsured = new GenericWorkorderAdditionalInsured(driver);
		additionalInsured.setAddressLine1("3696 S 6800 W");
		additionalInsured.setContactEditAddressCity("West Valley City");

		additionalInsured.setState(State.Utah);
		additionalInsured.setZipCode("84128");
		additionalInsured.selectAddressType(AddressType.Home);
		GenericWorkorderAdditionalNamedInsured namedInsured = new GenericWorkorderAdditionalNamedInsured(driver);
		namedInsured.clickUpdate();

		// SQ016 - Prior losses
		sideMenu.clickSideMenuRiskAnalysis();
		GenericWorkorderRiskAnalysis_PriorLosses risk = new GenericWorkorderRiskAnalysis_PriorLosses(driver);
		risk.clickPriorLossesCardTab();
		risk.setLossHistoryType("Manually Entered");
		risk.setManualLossHistory(1, "Section III - Auto Line",
				DateUtils.dateFormatAsString("MM/dd/yyyy",
						DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter)),
				"Testing", "2000", "2000", "2000", "Open");

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
			}
		};

		for (PLUWIssues uwBlockBindExpected : sqPolicyLevelBlockIssuance) {
			softAssert.assertFalse(
					!uwIssues.isInList(uwBlockBindExpected.getLongDesc()).equals(UnderwriterIssueType.Informational),
					"Expected Informational : " + uwBlockBindExpected.getShortDesc() + " is not displayed");
		}

		softAssert.assertAll();
	}

    @Test
    public void testCheckPolicyLevelUWIssuesInPolicyChange() throws Exception {
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

		GeneratePolicy mySquirePolicy = new GeneratePolicy.Builder(driver).withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
				.withInsFirstLastName("Test", "SqForSib").withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);

		new Login(driver).loginAndSearchPolicyByAccountNumber(mySquirePolicy.agentInfo.getAgentUserName(),
				mySquirePolicy.agentInfo.getAgentPassword(), mySquirePolicy.accountNumber);
		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
		Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 10);

		// start policy change
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", changeDate);

		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();
		GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);

		// SQ004 - Squire Organization type of other
		policyInfo.setPolicyInfoOrganizationType(OrganizationTypePL.Other);
		policyInfo.setPolicyInfoDescription("Testing Description");

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

		// SQ016 - Prior losses
		sideMenu.clickSideMenuRiskAnalysis();
		GenericWorkorderRiskAnalysis_PriorLosses risk = new GenericWorkorderRiskAnalysis_PriorLosses(driver);
		risk.clickPriorLossesCardTab();
		risk.setLossHistoryType("Manually Entered");
		risk.setManualLossHistory(1, "Section III - Auto Line",
				DateUtils.dateFormatAsString("MM/dd/yyyy",
						DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter)),
				"Testing", "2000", "2000", "2000", "Open");

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
			}
		};
		SoftAssert softAssert = new SoftAssert();
		for (PLUWIssues uwBlockBindExpected : sqPolicyLevelBlockIssuance) {
			softAssert.assertFalse(
					!uwIssues.isInList(uwBlockBindExpected.getLongDesc()).equals(UnderwriterIssueType.Informational),
					"Policy Chane - Expected Blocking Issuance : " + uwBlockBindExpected.getShortDesc()
							+ " is not displayed");
		}

		softAssert.assertAll();

	}

    @Test
    public void testCheckPolicyLevelSiblingPolUWIssuesInSubmission() throws Exception {
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

		mySquirePolicy = new GeneratePolicy.Builder(driver).withSquire(mySquire).withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
				.withInsFirstLastName("Test", "SqForSib").withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);

		ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs = new ArrayList<PolicyInfoAdditionalNamedInsured>();
		listOfANIs.add(new PolicyInfoAdditionalNamedInsured(ContactSubType.Person,
				"Test" + StringsUtils.generateRandomNumberDigits(8), "ANI" + StringsUtils.generateRandomNumberDigits(8),
				AdditionalNamedInsuredType.Spouse, new AddressInfo(true)) {
			{
				this.setNewContact(CreateNew.Create_New_Always);
			}
		});

		SquirePersonalAuto myAuto1 = new SquirePersonalAuto();
		Squire mySquire1 = new Squire(SquireEligibility.City);
		mySquire1.squirePA = myAuto1;

		GeneratePolicy mySiblingPolicy = new GeneratePolicy.Builder(driver).withSquire(mySquire1)
				.withProductType(ProductLineType.Squire).withANIList(listOfANIs).isDraft()
				.withLineSelection(LineSelection.PersonalAutoLinePL).build(GeneratePolicyType.FullApp);
		new Login(driver).loginAndSearchSubmission(mySiblingPolicy.agentInfo.getAgentUserName(),
				mySiblingPolicy.agentInfo.getAgentPassword(), mySiblingPolicy.accountNumber);

		SideMenuPC sideMenu = new SideMenuPC(driver);
		GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(
				driver);
		sideMenu.clickSideMenuPADrivers();
		GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);

		paDrivers.clickEditButtonInDriverTableByDriverName(mySiblingPolicy.pniContact.getFirstName());
		paDrivers.selectMaritalStatus(MaritalStatus.Single);
		paDrivers.setAutoDriversDOB(DateUtils.dateAddSubtract(
				DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -21));
		new GuidewireHelpers(driver).clickProductLogo();
		paDrivers.clickOk();
		sideMenu.clickSideMenuPolicyInfo();

		GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.setPolicyInfoOrganizationType(OrganizationType.Sibling);
		polInfo.clickProductLogo();
		polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy,
				mySquirePolicy.squire.getPolicyNumber().replace("-", ""));

		new GuidewireHelpers(driver).clickNext();
		sideMenu.clickSideMenuHouseholdMembers();
		sideMenu.clickSideMenuRiskAnalysis();
		risk.Quote();
		GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
		}

		sideMenu.clickSideMenuRiskAnalysis();

		// SQ051 - Sibling ANI
		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertFalse(
				!uwIssues.isInList(PLUWIssues.SiblingANI.getLongDesc()).equals(UnderwriterIssueType.Informational),
				"Expected Informational: " + PLUWIssues.SiblingANI.getShortDesc() + " is not displayed");

		softAssert.assertAll();
	}

	@Test(dependsOnMethods = { "testCheckPolicyLevelSiblingPolUWIssuesInSubmission" })
    public void testCheckPolicyLevelSiblingPolUWIssuesInPolicyChange() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K,
				MedicalLimit.TenK, true, UninsuredLimit.CSL300K, false, UnderinsuredLimit.CSL300K);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);

		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.squirePA = squirePersonalAuto;

		ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs = new ArrayList<PolicyInfoAdditionalNamedInsured>();
		listOfANIs.add(new PolicyInfoAdditionalNamedInsured(ContactSubType.Person,
				"Test" + StringsUtils.generateRandomNumberDigits(8), "ANI" + StringsUtils.generateRandomNumberDigits(8),
				AdditionalNamedInsuredType.Spouse, new AddressInfo(true)) {
			{
				this.setNewContact(CreateNew.Create_New_Always);
			}
		});
		GeneratePolicy myNewPolicy = new GeneratePolicy.Builder(driver).withSquire(mySquire).withANIList(listOfANIs)
				.withProductType(ProductLineType.Squire).withLineSelection(LineSelection.PersonalAutoLinePL)
				.withInsFirstLastName("Test", "SqForSib").withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		new Login(driver).loginAndSearchPolicyByAccountNumber(myNewPolicy.underwriterInfo.getUnderwriterUserName(),
				myNewPolicy.underwriterInfo.getUnderwriterPassword(), myNewPolicy.accountNumber);

		// start policy change
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change",
				DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));

		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();
		sideMenu.clickSideMenuPADrivers();
		GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(
				driver);
		GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);

		paDrivers.clickEditButtonInDriverTableByDriverName(myNewPolicy.pniContact.getFirstName());
		paDrivers.selectMaritalStatus(MaritalStatus.Single);
		paDrivers.setAutoDriversDOB(DateUtils.dateAddSubtract(
				DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -21));
		paDrivers.clickOk();

		sideMenu.clickSideMenuPolicyInfo();
		GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.setPolicyInfoOrganizationType(OrganizationType.Sibling);
		polInfo.clickProductLogo();
		polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy,
				mySquirePolicy.squire.getPolicyNumber().replace("-", ""));
		sideMenu.clickSideMenuRiskAnalysis();
		risk.Quote();
		GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();
		// SQ051 - Sibling ANI
		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertFalse(
				!uwIssues.isInList(PLUWIssues.SiblingANI.getLongDesc()).equals(UnderwriterIssueType.Informational),
				"Policy Change - Expected Informational : " + PLUWIssues.SiblingANI.getShortDesc()
						+ " is not displayed");

		softAssert.assertAll();

	}

    @Test
    public void testCreateStandardFireForFinancingRule() throws Exception {
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
				.withProductType(ProductLineType.StandardFire).withLineSelection(LineSelection.StandardFirePL)
				.withCreateNew(CreateNew.Create_New_Always).withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("PolBus", "FireRule").withPolOrgType(OrganizationType.Individual)
				.withPolicyLocations(locationsList).build(GeneratePolicyType.FullApp);
		new Login(driver).loginAndSearchSubmission(myStdFirePolObj.agentInfo.getAgentUserName(),
				myStdFirePolObj.agentInfo.getAgentPassword(), myStdFirePolObj.accountNumber);
		new GuidewireHelpers(driver).editPolicyTransaction();
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuQualification();
		GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
		qualificationPage.clickPL_STDFireSupportingBusiness(true);

		sideMenu.clickSideMenuRiskAnalysis();
		GenericWorkorderRiskAnalysis_PriorLosses risk = new GenericWorkorderRiskAnalysis_PriorLosses(driver);
		risk.clickUWIssuesTab();
		risk.Quote();
		GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
		}
		SoftAssert softAssert = new SoftAssert();
		sideMenu.clickSideMenuRiskAnalysis();
		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();
		softAssert.assertFalse(
				!uwIssues.isInList(PLUWIssues.SupportingBusinessDeclarationPagesNeeded.getLongDesc())
						.equals(UnderwriterIssueType.Informational),
				"Expected Informational : " + PLUWIssues.SupportingBusinessDeclarationPagesNeeded.getShortDesc()
						+ " is not displayed");

		softAssert.assertAll();
	}

}

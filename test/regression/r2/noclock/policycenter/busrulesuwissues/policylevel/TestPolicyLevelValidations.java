package regression.r2.noclock.policycenter.busrulesuwissues.policylevel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.enums.Gender;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.LossHistoryType;
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
import repository.gw.enums.Vehicle.VehicleTypePL;
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
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderInsuranceScore;
import repository.pc.workorders.generic.GenericWorkorderLineSelection;
import repository.pc.workorders.generic.GenericWorkorderModifiers;
import repository.pc.workorders.generic.GenericWorkorderPayerAssignment;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfoAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_PriorLosses;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailConstruction;
import repository.pc.workorders.generic.GenericWorkorderVehicles;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import persistence.globaldatarepo.helpers.VINHelper;

/**
 * @Author nvadlamudi
 * @Requirement : Policy Level Forms Product Model Spreadsheet
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/Product%20Model%20Spreadsheets/squire%20product%20model%20spreadhseets/Policy%20Level%20Forms%20Product%20Model%20Spreadsheet.xlsx">Policy Level Forms Product Model Spreadsheet </a>
 * @Description
 * @DATE Sep 28, 2017
 */
@QuarantineClass
public class TestPolicyLevelValidations extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObjPL = null;
	SoftAssert softAssert = new SoftAssert();
	private String sq006BusRule = "The SSN exists on another contact. Please click the Cancel button and search for the contact";
	private String sq047BusRules = "is an ANI and cannot be removed";
	private String sq035BusRules = "Please attempt to standardize the address before leaving the page";
	private String expBusRules = "Expiration Date : Expiration date must be greater than or equal to 30 days and less than 18 months from effective date";
	private String sq045BusRules = "The current payer assigned to Section II must also be a current payer assigned to at least one residence on Section I. (SQ054)";
	private String sq011BusRules = "Policy: Credit Report is required to quote policy. (SQ011)";
	private String sq028BusRules = "Occurrence Date : Occurrence Date Cannot be in the Future";
	private String sq046BusRules = "There must be at least one contact with membership dues on the policy. Please add a person under the Membership Dues section below.";
	private String sq052BusRules = "Policy: To write a sibling policy Section III is required. (SQ052)";
	private String sq057BusRules = "Policy: Parent policy has a surcharge. Choose the Close Option Not-Taken and write on an individual policy. (SQ057)";
	private String sq050BusRules = "Please visit the renewal information screen and fill out the required information. (SQ050)";
	private String propDetPerson = "";
	private GeneratePolicy mySquirePolicy;
	private int squirePolInitialDiscount;

	@BeforeMethod(alwaysRun = true)
	public void beforeMethod() {
		softAssert = new SoftAssert();
	}

	@Test
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
				AdditionalNamedInsuredType.Spouse, new AddressInfo(true)) {{
					this.setNewContact(CreateNew.Create_New_Always);
				}});
		
		SquirePropertyAndLiability myProperty = new SquirePropertyAndLiability();
		myProperty.locationList = locationsList;
		
		Squire mySquire = new Squire(new GuidewireHelpers(driver).getRandomEligibility());
		mySquire.squirePA = new SquirePersonalAuto();
		mySquire.propertyAndLiability = myProperty;


        myPolicyObjPL = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
				.withInsFirstLastName("PolLevel", "Rules")
				.withANIList(listOfANIs)
				.build(GeneratePolicyType.FullApp);
	}

	@Test(dependsOnMethods = { "testCreateSquireFA" })
	private void testAddAvailabilityRules() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL);
        new GuidewireHelpers(driver).editPolicyTransaction();
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();

        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
		policyInfo.clickANIContact(this.myPolicyObjPL.aniList.get(0).getPersonFirstName());
		GenericWorkorderPolicyInfoAdditionalNamedInsured editANIPage = new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver);
		// SQ006 - Duplicate SSN
		editANIPage.setEditAdditionalNamedInsuredSSN(this.myPolicyObjPL.pniContact.getSocialSecurityNumber());

		// SQ007 - Squire Additional Insured Under 18
		editANIPage.setEditAdditionalNamedInsuredDateOfBirth(DateUtils.dateAddSubtract(
				DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -17));
		editANIPage.clickEditAdditionalNamedInsuredUpdate();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		softAssert.assertFalse(!risk.getValidationMessagesText().contains(sq006BusRule),
				"Expected validation " + sq006BusRule + " is not displayed.");
		risk.clickClearButton();
		editANIPage.setEditAdditionalNamedInsuredSSN("");
		editANIPage.clickEditAdditionalNamedInsuredUpdate();

		// SQ047 - ANI on policy
		sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
		household.clickRemoveMember(this.myPolicyObjPL.aniList.get(0).getPersonFirstName());
        softAssert.assertFalse(!new GuidewireHelpers(driver).errorMessagesExist() && (!new GuidewireHelpers(driver).getFirstErrorMessage().contains(sq047BusRules)),
				"Expected validation " + sq047BusRules + " is not displayed.");

		// SQ031 - ANI as other role
		sideMenu.clickSideMenuHouseholdMembers();
		household.clickSearch();
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
		SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
		propDetPerson = "PropDet" + StringsUtils.generateRandomNumberDigits(7);
		addressBook.searchAddressBookByFirstLastName(true, propDetPerson,
				"Interest" + StringsUtils.generateRandomNumberDigits(6), myPolicyObjPL.pniContact.getAddress().getLine1(),
				myPolicyObjPL.pniContact.getAddress().getCity(), myPolicyObjPL.pniContact.getAddress().getState(),
				myPolicyObjPL.pniContact.getAddress().getZip(), CreateNew.Create_New_Always);
        householdMember.setDateOfBirth(DateUtils.convertStringtoDate("01/01/1980", "MM/dd/YYYY"));
        householdMember.setNewPolicyMemberAlternateID("Little Bunny Foo Foo");
		householdMember.selectRelationshipToInsured(RelationshipToInsured.SignificantOther);
		householdMember.selectNotNewAddressListingIfNotExist(myPolicyObjPL.pniContact.getAddress());
		householdMember.clickRelatedContactsTab();
        householdMember.clickOK();

		sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetails = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
		propertyDetails.clickViewOrEditBuildingButton(1);
		propertyDetails.addExistingAdditionalInterest(propDetPerson);
		GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
		additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType("Lessor");
		additionalInterests.checkBuildingsPropertyAdditionalInterestsFirstMortgage(true);
		additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();
		propertyDetails.clickOk();
        new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();

		if (new GuidewireHelpers(driver).errorMessagesExist()) {
			new GuidewireHelpers(driver).clickDiscardUnsavedChangesLink();
            sideMenu.clickSideMenuSquirePropertyDetail();

			propertyDetails.clickViewOrEditBuildingButton(1);
			propertyDetails.addExistingAdditionalInterest(propDetPerson);
			additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType("Lessor");
			additionalInterests.checkBuildingsPropertyAdditionalInterestsFirstMortgage(true);
			additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();
			propertyDetails.clickOk();
        }

		sideMenu.clickSideMenuPolicyInfo();
        policyInfo.selectAddExistingOtherContactAdditionalInsured(propDetPerson);
        GenericWorkorderAdditionalNamedInsured ani = new GenericWorkorderAdditionalNamedInsured(driver);
		ani.selectAdditionalInsuredRelationship(RelationshipToInsured.Friend);
        ani.selectAddtionalInsuredAddress("ID");
		ani.clickUpdate();

		// SQ060 - Rated Age rule
		// SQ008 - PNI min age
		sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
		paDrivers.clickEditButtonInDriverTable(1);
		paDrivers.setAutoDriversDOB(DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter),
				DateAddSubtractOptions.Month, -1));		
        Date newdob = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter),
				DateAddSubtractOptions.Day, -15);
		paDrivers.setAutoDriversDOB(newdob);
        paDrivers.selectGender(Gender.Male);
		softAssert.assertFalse(!paDrivers.getDriverRatedAge().equals("0"), "Expected Rated Age 0 is not displayed.");
		paDrivers.clickOk();

		// SQ035 - Non-standardized address
		sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
		propertyLocations.clickNewLocation();
		propertyLocations.addLocationInfoFA(new AddressInfo(), 2, 2);
        propertyLocations.clickOK();
        softAssert.assertFalse(!risk.getValidationMessagesText().contains(sq035BusRules),
				"Expected validation " + sq035BusRules + " is not displayed.");
		risk.clickClearButton();
		propertyLocations.clickStandardizeAddress();
		propertyLocations.clickOK();

		// - Expiration Date on Policy Info screen
		sideMenu.clickSideMenuPolicyInfo();
        policyInfo.setPolicyInfoTermType("Other");
        policyInfo.setPolicyInfoExpirationDate(DateUtils.dateAddSubtract(
				DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 22));
		new GuidewireHelpers(driver).clickNext();
        softAssert.assertFalse(!new GuidewireHelpers(driver).errorMessagesExist() && (!new GuidewireHelpers(driver).getFirstErrorMessage().contains(expBusRules)),
				"Expected validation " + expBusRules + " is not displayed.");
		policyInfo.setPolicyInfoExpirationDate(DateUtils.dateAddSubtract(
				DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, 1));
        policyInfo.setPolicyInfoTermType("Annual");

		// SQ054 - One payer policy
		sideMenu.clickSideMenuPayerAssignment();
		GenericWorkorderPayerAssignment payerAssignment = new GenericWorkorderPayerAssignment(driver);
		payerAssignment.setPayerAssignmentBillLiabilityCoverages("General Liability", true, false, propDetPerson);
		sideMenu.clickSideMenuRiskAnalysis();
		softAssert.assertFalse(!new GuidewireHelpers(driver).errorMessagesExist() && (!new GuidewireHelpers(driver).getFirstErrorMessage().contains(sq045BusRules)),
				"Expected validation " + sq045BusRules + " is not displayed.");

		// SQ055 - Unchecked payer
		payerAssignment.setPayerAssignmentBillAllBuildingOrPropertyCoverages(1, 1, propDetPerson, true, false);
		payerAssignment.setPayerAssignmentBillLiabilityCoverages("General Liability", true, false, "Insured");
		risk.Quote();
        softAssert.assertFalse(
				!risk.getValidationMessagesText().contains("Please check to ensure the payer assignment is correct"),
				"Expected validation 'Please check to ensure the payer assignment is correct' is not displayed.");
		risk.clickClearButton();
		sideMenu.clickSideMenuPayerAssignment();
		payerAssignment.setPayerAssignmentVerificationConfirmationCheckbox(true);
		sideMenu.clickSideMenuRiskAnalysis();
		risk.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if (quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();
		PLUWIssues polBlockingQuote = PLUWIssues.SquirePNIUnder18;

		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();
		softAssert.assertFalse(
				!uwIssues.isInList(polBlockingQuote.getLongDesc()).equals(UnderwriterIssueType.BlockQuote),
				"Expected Blocking quote " + polBlockingQuote.getShortDesc() + " is not displayed.");

		PLUWIssues polBlockingIssuance = PLUWIssues.DriverUnder16;

		softAssert.assertFalse(
				!uwIssues.isInList(polBlockingIssuance.getLongDesc()).equals(UnderwriterIssueType.BlockIssuance),
				"Expected Blocking quote " + polBlockingIssuance.getShortDesc() + " is not displayed.");

		@SuppressWarnings("serial")
		List<PLUWIssues> sqSectionIBlockInformation = new ArrayList<PLUWIssues>() {
			{
				this.add(PLUWIssues.MissingMSYear);
				this.add(PLUWIssues.MissingPhotoYear);
			}
		};

		for (PLUWIssues info : sqSectionIBlockInformation) {
			softAssert.assertFalse(!uwIssues.isInList(info.getLongDesc()).equals(UnderwriterIssueType.Informational),
					"Expected Blocking quote " + info.getShortDesc() + " is not displayed.");
		}

		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();

		softAssert.assertAll();
	}

	@Test(dependsOnMethods = { "testAddAvailabilityRules" })
	private void testAddMoreValidatiosWithUW() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter_Supervisor);

		new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquirePropertyDetail();
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propertyDetails = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
		propertyDetails.clickViewOrEditBuildingButton(1);
		propertyDetails.clickPropertyConstructionTab();
		GenericWorkorderSquirePropertyDetailConstruction constructionPage = new GenericWorkorderSquirePropertyDetailConstruction(driver);

		int yearField = DateUtils.getCenterDateAsInt(cf, ApplicationOrCenter.PolicyCenter, "yyyy");
		int monthField = DateUtils.getCenterDateAsInt(cf, ApplicationOrCenter.PolicyCenter, "MM");
		monthField = (monthField == 1) ? 2 : monthField;

		// SQ033 - MS year over 3 years
		constructionPage.setMSYear((monthField - 1) + "/" + (yearField - 4));

		// SQ032 - Photo year over 6 years
		constructionPage.setPhotoYear((monthField - 1) + "/" + (yearField - 7));
		constructionPage.clickOK();

		// SQ011 - No CBR ordered
		sideMenu.clickSideMenuPLInsuranceScore();
        GenericWorkorderInsuranceScore creditReport = new GenericWorkorderInsuranceScore(driver);
		creditReport.clickEditInsuranceReport();
		GenericWorkorderRiskAnalysis_UWIssues risk = new GenericWorkorderRiskAnalysis_UWIssues(driver);
		risk.Quote();
        softAssert.assertFalse(!risk.getValidationMessagesText().contains(sq011BusRules),
				"Expected validation '" + sq011BusRules + "' is not displayed.");
		risk.clickClearButton();
		creditReport.selectCreditReportIndividual(this.propDetPerson);
		creditReport.clickOrderReport();

		// SQ028 - occurrence date of future
		sideMenu.clickSideMenuRiskAnalysis();

		risk.clickPriorLossesCardTab();
        GenericWorkorderRiskAnalysis_PriorLosses priorLosses = new GenericWorkorderRiskAnalysis_PriorLosses(driver);
        priorLosses.setLossHistory(LossHistoryType.ManuallyEntered);
        Date occuranceDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 22);
		priorLosses.setManualLossHistory(1, "Section III - Auto Line", DateUtils.dateFormatAsString("MM/dd/yyyy", occuranceDate), "Testing DE2363", "100", "1000", "100", "Closed");
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		softAssert.assertFalse(!new GuidewireHelpers(driver).errorMessagesExist() && (!new GuidewireHelpers(driver).getFirstErrorMessage().contains(sq028BusRules)), "Expected validation " + sq028BusRules + " is not displayed.");

		priorLosses.setLossHistory(LossHistoryType.NoLossHistory);
        risk.clickUWIssuesTab();
		risk.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if (quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();
        FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();
		@SuppressWarnings("serial")
		List<PLUWIssues> sqSectionIBlockInformation = new ArrayList<PLUWIssues>() {
			{
				this.add(PLUWIssues.MSYearOver3Yrs);
				this.add(PLUWIssues.PhotoYearOver6Yrs);
			}
		};

		for (PLUWIssues info : sqSectionIBlockInformation) {
			softAssert.assertFalse(!uwIssues.isInList(info.getLongDesc()).equals(UnderwriterIssueType.Informational),
					"Expected Blocking quote " + info.getShortDesc() + " is not displayed.");
		}

		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		softAssert.assertAll();
	}

	@Test()
	private void testCreateSquirePolWithCompany() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		locationsList.add(new PolicyLocation(locOnePropertyList));

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

		ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs = new ArrayList<PolicyInfoAdditionalNamedInsured>();
		PolicyInfoAdditionalNamedInsured ani = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person,
				"Test" + StringsUtils.generateRandomNumberDigits(8), "comp", AdditionalNamedInsuredType.Spouse,
				new AddressInfo(false));
		ani.setHasMembershipDues(true);
		ani.setNewContact(CreateNew.Create_New_Always);
		listOfANIs.add(ani);
		
		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;
		myPropertyAndLiability.liabilitySection = myLiab;
		
		Squire mySquire = new Squire();
		mySquire.propertyAndLiability = myPropertyAndLiability;

        GeneratePolicy mySqCountryPol = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsCompanyName("SQCountry")
				.withANIList(listOfANIs)
				.build(GeneratePolicyType.FullApp);
        driver.quit();
        
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchSubmission(mySqCountryPol.agentInfo.getAgentUserName(),
				mySqCountryPol.agentInfo.getAgentPassword(), mySqCountryPol.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();

		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
		household.clickRemoveMember(mySqCountryPol.aniList.get(0).getPersonFirstName());

		softAssert.assertFalse(!new GuidewireHelpers(driver).errorMessagesExist() && (!new GuidewireHelpers(driver).getFirstErrorMessage().contains("is an ANI and cannot be removed")),
				"Expected validation 'is an ANI and cannot be removed' is not displayed.");

		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
		policyInfo.removeANI(mySqCountryPol.aniList.get(0).getPersonLastName());
		new GuidewireHelpers(driver).clickNext();		
		sideMenu.clickSideMenuPolicyInfo();
        if (!policyInfo.checkMembershipContact(mySqCountryPol.aniList.get(0).getPersonLastName())) {
			// SQ046 - Policy must have person
			softAssert.assertFalse(!new GuidewireHelpers(driver).errorMessagesExist() && (!new GuidewireHelpers(driver).getFirstErrorMessage().contains(sq046BusRules)),
					"Expected validation '"+sq046BusRules +"' is not displayed.");
		}

		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		softAssert.assertAll();
	}

	@Test()
	private void testCreateSiblingPol() throws Exception {
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

        mySquirePolicy = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
				.withInsFirstLastName("Test", "SqForSib")
				.withPaymentPlanType(PaymentPlanType.getRandom())
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
        
		new Login(driver).loginAndSearchPolicyByAccountNumber(this.mySquirePolicy.agentInfo.agentUserName,
				this.mySquirePolicy.agentInfo.agentPassword, this.mySquirePolicy.accountNumber);

		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuModifiers();

        GenericWorkorderModifiers modifier = new GenericWorkorderModifiers(driver);
		squirePolInitialDiscount = modifier.getTotalPolicyDiscount();
	}

	@Test(dependsOnMethods = {"testCreateSiblingPol"})
	private void testCheckSiblingPolValidations() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_100000_CSL);

		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K,
				MedicalLimit.TenK, true, UninsuredLimit.CSL300K, false, UnderinsuredLimit.CSL300K);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);

		// ANI
		ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs = new ArrayList<PolicyInfoAdditionalNamedInsured>();
		PolicyInfoAdditionalNamedInsured ani = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person,
				"Test" + StringsUtils.generateRandomNumberDigits(8),
				"ANINew" + StringsUtils.generateRandomNumberDigits(8), AdditionalNamedInsuredType.ParentGuardian,
				new AddressInfo(false));
		ani.setHasMembershipDues(true);
		ani.setNewContact(CreateNew.Create_New_Always);
		listOfANIs.add(ani);
		
		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;
        
        
        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        GeneratePolicy mySiblingPolicy = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withSiblingPolicy(mySquirePolicy, "Test", "Sibling")
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
				.withANIList(listOfANIs)
				.build(GeneratePolicyType.FullApp);

		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(),
				uw.getUnderwriterPassword(), mySiblingPolicy.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPAModifiers();

		// SQ057 - Sibling with surcharge
        GenericWorkorderModifiers modifier = new GenericWorkorderModifiers(driver);
		modifier.enterSquireModifierAdditionalDiscount(1, "10", "Testing purpose added");

        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();

		softAssert.assertFalse(!risk.getValidationMessagesText().contains(sq057BusRules),
				"Expected validation '" + sq057BusRules + "' is not displayed.");
		risk.clickClearButton();
		sideMenu.clickSideMenuPAModifiers();
        modifier.enterSquireModifierAdditionalDiscount(1, "-" + this.squirePolInitialDiscount, "Testing purpose added");

		// SQ052 - Sibling and Auto
		sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles vehicleDetails = new GenericWorkorderVehicles(driver);
		vehicleDetails.setCheckBoxInVehicleTable(1);
		vehicleDetails.clickRemoveVehicle();

		sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers paDrivers = new GenericWorkorderSquireAutoDrivers(driver);

		paDrivers.setCheckBoxInDriverTable(1);
		paDrivers.clickRemoveButton();

		sideMenu.clickSideMenuLineSelection();
        GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(driver);
		lineSelection.checkPersonalAutoLine(false);

		sideMenu.clickSideMenuRiskAnalysis();
		risk.Quote();

		softAssert.assertFalse(!risk.getValidationMessagesText().contains(sq052BusRules),
				"Expected validation '" + sq052BusRules + "' is not displayed.");
		risk.clickClearButton();

		softAssert.assertAll();
	}

	@Test()
	private void testRenewalInformationValidation() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

		// Vehicle
		ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
		Vehicle toAdd = new Vehicle();
		toAdd.setVehicleTypePL(VehicleTypePL.PrivatePassenger);

		Vehicle veh2 = new Vehicle();
		veh2.setVehicleTypePL(VehicleTypePL.ShowCar);
		veh2.setComprehensive(true);
		veh2.setCollision(true);
		Vin anotherVin = VINHelper.getRandomVIN();
		veh2.setVin(anotherVin.getVin());
		veh2.setMake(anotherVin.getMake());
		veh2.setModel(anotherVin.getModel());
		veh2.setModelYear(Integer.parseInt(anotherVin.getYear()));
		vehicleList.add(toAdd);
		vehicleList.add(veh2);

		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setVehicleList(vehicleList);
		
		SquirePropertyAndLiability myProperty = new SquirePropertyAndLiability();
		myProperty.locationList = locationsList;
		myProperty.liabilitySection = myLiab;
		
		Squire mySquire = new Squire(SquireEligibility.Country);
		mySquire.propertyAndLiability = myProperty;
		mySquire.squirePA = squirePersonalAuto;


        GeneratePolicy renewalAutoPolObj = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL )
				.withInsFirstLastName("PolBus", "Renew")
				.withPolTermLengthDays(79)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		
        // Login with UW
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				renewalAutoPolObj.accountNumber);

        StartRenewal renewal = new StartRenewal(driver);
        renewal.startRenewal();

        InfoBar infoBar = new InfoBar(driver);
		infoBar.clickInfoBarPolicyNumber();

        PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
		preRenewalPage.closePreRenewalExplanations(renewalAutoPolObj);

        PolicySummary policySummary = new PolicySummary(driver);
		policySummary.clickPendingTransaction(TransactionType.Renewal);
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();

		// SQ050 - Renewal information
		softAssert.assertFalse(!risk.getValidationMessagesText().contains(sq050BusRules),
				"Expected validation '" + sq050BusRules + "' is not displayed.");
		risk.clickClearButton();

		softAssert.assertAll();

	}
}

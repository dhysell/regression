package regression.r2.noclock.policycenter.documents.squiresection3;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DocFormEvents;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.RenewalCode;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.ActualExpectedDocumentDifferences;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.DocFormUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicyDocuments;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderForms;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_AdditionalCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_ExclusionsConditions;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.generic.GenericWorkorderVehicles_CoverageDetails;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import repository.pc.workorders.generic.GenericWorkorderVehicles_ExclusionsAndConditions;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import persistence.globaldatarepo.helpers.VINHelper;
@QuarantineClass
public class TestSectionIIIRenewalFormInference extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObjPL;
	private String insFirstName = "Renewal";
	private String insLastName = "FormInfer";
	private String driverFirstName = "testDr";
	private String VehicleAdditionalInterestLastName = "VEHPerson" + StringsUtils.generateRandomNumberDigits(7);
	private ArrayList<DocFormEvents.PolicyCenter> eventsHitDuringSubmissionCreationPlusLocal = new ArrayList<DocFormEvents.PolicyCenter>();
	private ArrayList<String> formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = new ArrayList<String>();
	private ArrayList<String> formsOrDocsActualFromUISubmissionPlusLocal = new ArrayList<String>();
	private ActualExpectedDocumentDifferences actualExpectedDocDifferencesSubmissionPlusLocal = new ActualExpectedDocumentDifferences();
	private Underwriters uw;

	@Test()
	public void testIssueSquireAuto() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		// Coverages
		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow,
				MedicalLimit.TenK);
		coverages.setUnderinsured(false);

		// driver
		ArrayList<Contact> driversList = new ArrayList<Contact>();
		Contact person = new Contact();
		person.setFirstName(driverFirstName);
		driversList.add(person);

		// Vehicle
		ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
		Vehicle toAdd = new Vehicle();
		toAdd.setComprehensive(true);
		toAdd.setCostNew(10000.00);
		toAdd.setCollision(true);
		toAdd.setAdditionalLivingExpense(true);
		vehicleList.add(toAdd);

		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);
		squirePersonalAuto.setVehicleList(vehicleList);
		squirePersonalAuto.setDriversList(driversList);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName(insFirstName, insLastName)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withPolTermLengthDays(79)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

	}

	@Test(dependsOnMethods = { "testIssueSquireAuto" })
	public void testInitiateRenewal() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter_Supervisor);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				myPolicyObjPL.accountNumber);

        PolicyMenu policyMenu = new PolicyMenu(driver);
		policyMenu.clickRenewPolicy();

        PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
		preRenewalPage.closePreRenewalExplanations(myPolicyObjPL);

	}

	@Test(dependsOnMethods = { "testInitiateRenewal" })
	private void testRenewalAddSectionIIICoverages() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				myPolicyObjPL.accountNumber);

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
		accountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);

		new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_NoticeOfPolicyOnPrivacy);

		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_AdverseActionLetter);

		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.PL_RenewalReviewAudit);

		this.eventsHitDuringSubmissionCreationPlusLocal
				.add(DocFormEvents.PolicyCenter.Squire_Auto_StandardAutoPolicyDeclarations);

		// Adding policy member
		sideMenu.clickSideMenuHouseholdMembers();
        new GuidewireHelpers(driver).editPolicyTransaction();
		sideMenu.clickSideMenuHouseholdMembers();

        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
		household.clickSearch();
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
		addressBook.searchAddressBookByFirstLastName(myPolicyObjPL.basicSearch, "Vehicle", this.VehicleAdditionalInterestLastName,
				this.myPolicyObjPL.pniContact.getAddress().getLine1(), this.myPolicyObjPL.pniContact.getAddress().getCity(),
				this.myPolicyObjPL.pniContact.getAddress().getState(), this.myPolicyObjPL.pniContact.getAddress().getZip(),
				CreateNew.Create_New_Always);
        householdMember.setDateOfBirth(DateUtils.convertStringtoDate("01/01/1980", "MM/dd/YYYY"));

		householdMember.selectRelationshipToInsured(RelationshipToInsured.SignificantOther);
		householdMember.setSSN(StringsUtils.generateRandomNumberDigits(9));
        householdMember.setNewPolicyMemberAlternateID(this.VehicleAdditionalInterestLastName);
		
		householdMember.selectNotNewAddressListingIfNotExist(myPolicyObjPL.pniContact.getAddress());
		householdMember.clickRelatedContactsTab();
		householdMember.clickBasicsContactsTab();

		householdMember.clickOK();
        // Adding new member and diver of age less than 21 yrs
		sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
		paDrivers.clickEditButtonInDriverTableByDriverName(driverFirstName);

        Date currentDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
		Date dob = DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Year, -21);
		paDrivers.setAutoDriversDOB(dob);

		paDrivers.setExcludedDriverRadio(true);
        paDrivers.clickOk();

        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		// Adding Line Level coverages to get Forms
        //CORRECT LOGIC GOES HERE!!!!!!!!!!
//		try {
			sideMenu.clickSideMenuPACoverages();
        risk.clickClearButton();
//		} catch (Exception e) {
//
//		}

        GenericWorkorderSquireAutoCoverages_Coverages coverages = new GenericWorkorderSquireAutoCoverages_Coverages(driver);
		coverages.setUnderinsuredCoverage(true, UnderinsuredLimit.Fifty);
		coverages.clickAdditionalCoveragesTab();
        GenericWorkorderSquireAutoCoverages_AdditionalCoverages additionalcoverages = new GenericWorkorderSquireAutoCoverages_AdditionalCoverages(driver);
		additionalcoverages.setAccidentalDeath(true);

		// UW I323 10 14 Drive Other Car
		additionalcoverages.setDriveOtherCar(true);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_Auto_DriveOtherCar);

		// Exclusions and Conditions
		coverages.clickCoverageExclusionsTab();
        GenericWorkorderSquireAutoCoverages_ExclusionsConditions paExclusions = new GenericWorkorderSquireAutoCoverages_ExclusionsConditions(driver);

		// UW I361 01 08 Additional Insured Endorsement
		paExclusions.addAdditionalInsuredEndorsement("Test Company");
		this.eventsHitDuringSubmissionCreationPlusLocal
				.add(DocFormEvents.PolicyCenter.Squire_Auto_AdditionalInsuredEndorsement);

		// UW I301 10 14 Modification of "Insured Vehicle" Definition
		// Endorsement
		paExclusions.addModificationOfInsuredVehicleDefinitionEndorsement("BMW", "Beemer", "1B45678645456", "1999");
		this.eventsHitDuringSubmissionCreationPlusLocal
				.add(DocFormEvents.PolicyCenter.Squire_Auto_ModificationInsuredVehicle);

		// UW I305 01 08 Special Endorsement For Auto
		paExclusions.addSpecialEndorsementForAuto("Special Endorsement");
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_Auto_SpecialEndorsement);

		// UW I304 10 14 Driver Exclusion Endorsement
		paExclusions.addDriverExclusionEndorsement304(driverFirstName);

		sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehicalTab = new GenericWorkorderVehicles_Details(driver);
        GenericWorkorderVehicles_CoverageDetails vehicleCoverageDetails = new GenericWorkorderVehicles_CoverageDetails(driver);
        GenericWorkorderVehicles_ExclusionsAndConditions vehicleExclusions = new GenericWorkorderVehicles_ExclusionsAndConditions(driver);
		vehicalTab.clickLinkInVehicleTable("Edit");
        vehicalTab.setFactoryCostNew(20000);
		// UW I363 10 14 Insured Vehicle Leasing Endorsement
		vehicalTab.addExistingAutoAdditionalInterest(VehicleAdditionalInterestLastName, "Lessor");

		this.eventsHitDuringSubmissionCreationPlusLocal
				.add(DocFormEvents.PolicyCenter.Squire_AdditionalInterestDeclarations);

		this.eventsHitDuringSubmissionCreationPlusLocal
				.add(DocFormEvents.PolicyCenter.Squire_Auto_InsuredVehicleLeasingEndorsement);

		vehicleCoverageDetails.selectVehicleCoverageDetailsTab();
        vehicleCoverageDetails.setComprehensive(true);
		vehicleCoverageDetails.setCollision(true);
		this.eventsHitDuringSubmissionCreationPlusLocal
				.add(DocFormEvents.PolicyCenter.Squire_Auto_CertificateofLiabilityInsurance);

		// UW I335 01 08 Additional Living Expense Endorsement
		vehicleCoverageDetails.setAdditionalLivingExpense(true);
		this.eventsHitDuringSubmissionCreationPlusLocal
				.add(DocFormEvents.PolicyCenter.Squire_Auto_AdditionalLivingExpense);

		// UW I310 10 14 Deletion of Material Damage Coverage Endorsement
		vehicleExclusions.clickExclusionsAndConditionsTab();
        vehicleExclusions.setDeletionOfMaterial310(true);
		this.eventsHitDuringSubmissionCreationPlusLocal
				.add(DocFormEvents.PolicyCenter.Squire_Auto_DeletionMaterialDamage);
		vehicalTab.clickOK();

		Vehicle newVeh = new Vehicle();
		Vin vin = VINHelper.getRandomVIN();
		newVeh.setOdometer(20000);

		// Add Vehicle
		sideMenu.clickSideMenuPAVehicles();
        vehicalTab.createVehicleManually();
        vehicalTab.selectVehicleType(VehicleTypePL.ShowCar);
        vehicalTab.setVIN(vin.getVin());
        vehicalTab.setModelYear(newVeh.getModelYear());
		vehicalTab.setMake(newVeh.getMake());
		vehicalTab.setModel(newVeh.getModel());
		vehicalTab.setStatedValue(newVeh.getStatedValue());
        vehicalTab.setOdometer(newVeh.getOdometer());
        vehicalTab.setOdometer(newVeh.getOdometer());
        vehicalTab.selectGaragedAtZip("ID");
		vehicleCoverageDetails.selectVehicleCoverageDetailsTab();
        vehicleCoverageDetails.setComprehensive(true);
		vehicleCoverageDetails.setCollision(true);
		vehicleExclusions.clickExclusionsAndConditionsTab();
        // UW I303 10 14 Show Car Driver Exclusion Endorsement
		vehicleExclusions.addShowCarDriverExclusionEndorsement303(insFirstName);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_Auto_DriverExclusion);

		vehicalTab.clickOK();

		this.eventsHitDuringSubmissionCreationPlusLocal
				.add(DocFormEvents.PolicyCenter.Squire_Auto_ShowCarDriverExclusion);

		this.eventsHitDuringSubmissionCreationPlusLocal
				.add(DocFormEvents.PolicyCenter.Squire_Auto_ClassicCarLimitationCoverage);

		sideMenu.clickSideMenuRiskAnalysis();

		risk.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if (quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();
		risk.approveAll_IncludingSpecial();

		boolean testfailed = false;
		String errorMessage = "Account Number: " + this.myPolicyObjPL.accountNumber;
		sideMenu.clickSideMenuForms();
        GenericWorkorderForms formsPage = new GenericWorkorderForms(driver);

		this.formsOrDocsActualFromUISubmissionPlusLocal = formsPage.getFormDescriptionsFromTable();
		this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = DocFormUtils
				.getListOfDocumentNamesForListOfEventNames(this.eventsHitDuringSubmissionCreationPlusLocal);
		this.actualExpectedDocDifferencesSubmissionPlusLocal = DocFormUtils.compareListsForDifferences(
				this.formsOrDocsActualFromUISubmissionPlusLocal,
				this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal);

		if (this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface().size() > 0) {
			testfailed = true;
			errorMessage = errorMessage + "ERROR: Documents for Policy Change In Expected But Not in UserInterface - "
					+ this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface() + "\n";
		}
		
		if (this.actualExpectedDocDifferencesSubmissionPlusLocal.getInUserInterfaceNotInExpected().size() > 0) {
		testfailed = true;
		errorMessage = errorMessage
				+ "ERROR: Documents for Renewal In Expected But Not in UserInterface - "
				+ this.actualExpectedDocDifferencesSubmissionPlusLocal.getInUserInterfaceNotInExpected() + "\n";
		}
		sideMenu.clickSideMenuQuote();
        StartRenewal renewal = new StartRenewal(driver);
		renewal.clickRenewPolicy(RenewalCode.Renew_Good_Risk);
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
        new GuidewireHelpers(driver).logout();

		if (testfailed) {
			Assert.fail(errorMessage);
		}
	}
	
	@Test(dependsOnMethods = {"testRenewalAddSectionIIICoverages"})
	private void testValidateSectionIIIDocuments() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				this.myPolicyObjPL.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuToolsDocuments();
        PolicyDocuments docs = new PolicyDocuments(driver);
		docs.selectRelatedTo("Renew");
		docs.clickSearch();
        ArrayList<String> descriptions = docs.getDocumentsDescriptionsFromTable();

		String[] documents = { "Classic Car Limitation of Coverage Endorsement", "Certificate of Liability Insurance",
				"Additional Interest Declarations", "Standard Auto Policy Declarations",
				"Notice of Policy on Privacy", "PL Renewal Review Audit", "Adverse Action Letter", "Definition Endorsement", "Show Car Driver Exclusion Endorsement",
				"Driver Exclusion Endorsement", "Special Endorsement For Auto", "Deletion of Material Damage Coverage Endorsement",
				"Additional Living Expense Endorsement", "Additional Insured Endorsement", "Insured Vehicle Leasing Endorsement"};

		boolean testFailed = false;
		String errorMessage = "Account Number: " + myPolicyObjPL.accountNumber;
		for (String document : documents) {
			boolean documentFound = false;
			for (String desc : descriptions) {
				if (desc.contains(document)) {
					documentFound = true;
					break;
				}
			}

			if (!documentFound) {
				testFailed = true;
				errorMessage = errorMessage + "Expected document : '" + document
						+ "' not available in documents page. \n";
			}
		}
		if (testFailed)
			Assert.fail(errorMessage);

	}
}

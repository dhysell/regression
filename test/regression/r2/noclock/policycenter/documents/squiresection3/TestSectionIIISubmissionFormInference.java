package regression.r2.noclock.policycenter.documents.squiresection3;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DocFormEvents;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RelationshipToInsured;
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
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderForms;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_AdditionalCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_ExclusionsConditions;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.generic.GenericWorkorderVehicles_CoverageDetails;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import repository.pc.workorders.generic.GenericWorkorderVehicles_ExclusionsAndConditions;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import persistence.globaldatarepo.helpers.VINHelper;
@QuarantineClass
public class TestSectionIIISubmissionFormInference extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObjPL;
	private String insFirstName = "Test";
	private String insLastName = "Auto";
	private String driverFirstName = "testDr";
	private String VehicleAdditionalInterestLastName = "VEHPerson" + StringsUtils.generateRandomNumberDigits(7);
	private ArrayList<DocFormEvents.PolicyCenter> eventsHitDuringSubmissionCreationPlusLocal = new ArrayList<DocFormEvents.PolicyCenter>();
	private ArrayList<String> formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = new ArrayList<String>();
	private ArrayList<String> formsOrDocsActualFromUISubmissionPlusLocal = new ArrayList<String>();
	private ActualExpectedDocumentDifferences actualExpectedDocDifferencesSubmissionPlusLocal = new ActualExpectedDocumentDifferences();

	@Test()
	public void testFormInference_GenerateFullApp() throws Exception {

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
				.build(GeneratePolicyType.FullApp);

		eventsHitDuringSubmissionCreationPlusLocal.addAll(this.myPolicyObjPL.policyForms.eventsHitDuringSubmissionCreation);
		formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal
				.addAll(this.myPolicyObjPL.policyForms.formsOrDocsExpectedBasedOnSubmissionEventsHit);
		formsOrDocsActualFromUISubmissionPlusLocal.addAll(this.myPolicyObjPL.policyForms.formsOrDocsActualFromUISubmission);
		actualExpectedDocDifferencesSubmissionPlusLocal.setInExpectedNotInUserInterface(
				this.myPolicyObjPL.policyForms.actualExpectedDocDifferencesSubmission.getInExpectedNotInUserInterface());
		actualExpectedDocDifferencesSubmissionPlusLocal.setInUserInterfaceNotInExpected(
				this.myPolicyObjPL.policyForms.actualExpectedDocDifferencesSubmission.getInUserInterfaceNotInExpected());

	}

	@Test(dependsOnMethods = { "testFormInference_GenerateFullApp" })
	public void testFormInference() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter);

		new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				this.myPolicyObjPL.accountNumber);
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();
		
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.PL_PersonalLinesApplication);
		
		this.eventsHitDuringSubmissionCreationPlusLocal.remove(DocFormEvents.PolicyCenter.Squire_Auto_AutomobilePolicy);
        SideMenuPC sideMenu = new SideMenuPC(driver);

		// Adding policy member
		sideMenu.clickSideMenuHouseholdMembers();
		addNewPolicyMember(myPolicyObjPL.basicSearch, ContactSubType.Person, "Vehicle", this.VehicleAdditionalInterestLastName);

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
		try {
			sideMenu.clickSideMenuPACoverages();
            risk.clickClearButton();
		} catch (Exception e) {

		}

        GenericWorkorderSquireAutoCoverages_Coverages coverages = new GenericWorkorderSquireAutoCoverages_Coverages(driver);
		coverages.setUnderinsuredCoverage(true, UnderinsuredLimit.Fifty);
		coverages.clickAdditionalCoveragesTab();
        GenericWorkorderSquireAutoCoverages_AdditionalCoverages additoianlcoverages = new GenericWorkorderSquireAutoCoverages_AdditionalCoverages(driver);
		additoianlcoverages.setAccidentalDeath(true);
		
		//UW I323	10 14	Drive Other Car
		additoianlcoverages.setDriveOtherCar(true);
		
		// Exclusions and Conditions
		coverages.clickCoverageExclusionsTab();
        GenericWorkorderSquireAutoCoverages_ExclusionsConditions paExclusions = new GenericWorkorderSquireAutoCoverages_ExclusionsConditions(driver);
		
		//UW I361	01 08	Additional Insured Endorsement
		paExclusions.addAdditionalInsuredEndorsement("Test Company");
		
		//UW I301	10 14	Modification of "Insured Vehicle" Definition Endorsement
		paExclusions.addModificationOfInsuredVehicleDefinitionEndorsement("BMW", "Beemer", "1B45678645456", "1999");
		
		//UW I305	01 08	Special Endorsement For Auto
		paExclusions.addSpecialEndorsementForAuto("Special Endorsement");
				
		//UW I304	10 14	Driver Exclusion Endorsement
		paExclusions.addDriverExclusionEndorsement304(driverFirstName);
		
		sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehicalTab = new GenericWorkorderVehicles_Details(driver);
        GenericWorkorderVehicles_CoverageDetails vehcileCoverages = new GenericWorkorderVehicles_CoverageDetails(driver);
        GenericWorkorderVehicles_ExclusionsAndConditions vehicleExclusions = new GenericWorkorderVehicles_ExclusionsAndConditions(driver);
		
		vehicalTab.clickLinkInVehicleTable("Edit");
        vehicalTab.setFactoryCostNew(20000);
        //UW I363	10 14	Insured Vehicle Leasing Endorsement
		vehicalTab.addExistingAutoAdditionalInterest(VehicleAdditionalInterestLastName, "Lessor");
				
		this.eventsHitDuringSubmissionCreationPlusLocal
		.add(DocFormEvents.PolicyCenter.Squire_AdditionalInterestDeclarations);

		this.eventsHitDuringSubmissionCreationPlusLocal
				.add(DocFormEvents.PolicyCenter.Squire_Auto_InsuredVehicleLeasingEndorsement);

		vehcileCoverages.selectVehicleCoverageDetailsTab();
        vehcileCoverages.setComprehensive(true);
		vehcileCoverages.setCollision(true);		
		this.eventsHitDuringSubmissionCreationPlusLocal
		.add(DocFormEvents.PolicyCenter.Squire_Auto_CertificateofLiabilityInsurance);
		
		//UW I335	01 08	Additional Living Expense Endorsement
		vehcileCoverages.setAdditionalLivingExpense(true);
				
		//UW I310	10 14	Deletion of Material Damage Coverage Endorsement		
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
		vehcileCoverages.selectVehicleCoverageDetailsTab();
        vehcileCoverages.setComprehensive(true);
		vehcileCoverages.setCollision(true);
		vehicleExclusions.clickExclusionsAndConditionsTab();
        //UW I303	10 14	Show Car Driver Exclusion Endorsement
		vehicleExclusions.addShowCarDriverExclusionEndorsement303(insFirstName);
		vehicleExclusions.clickOK();		
		this.eventsHitDuringSubmissionCreationPlusLocal
				.add(DocFormEvents.PolicyCenter.Squire_Auto_ClassicCarLimitationCoverage);

        SideMenuPC sideMenuStuff = new SideMenuPC(driver);
		sideMenuStuff.clickSideMenuRiskAnalysis();
        risk.Quote();

		sideMenuStuff.clickSideMenuForms();
        GenericWorkorderForms formsPage = new GenericWorkorderForms(driver);
		this.formsOrDocsActualFromUISubmissionPlusLocal = formsPage.getFormDescriptionsFromTable();
		this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = DocFormUtils
				.getListOfDocumentNamesForListOfEventNames(this.eventsHitDuringSubmissionCreationPlusLocal);
		this.actualExpectedDocDifferencesSubmissionPlusLocal = DocFormUtils.compareListsForDifferences(
				this.formsOrDocsActualFromUISubmissionPlusLocal,
				this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal);

		String errorMessage = "";

		if (this.actualExpectedDocDifferencesSubmissionPlusLocal.getInUserInterfaceNotInExpected().size() > 0) {
			errorMessage = errorMessage
					+ "ERROR: Documents for Submission In UserInterface But Not in Expected - "
					+ this.actualExpectedDocDifferencesSubmissionPlusLocal.getInUserInterfaceNotInExpected() + "\n";
		}

		if (this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface().size() > 0) {
			errorMessage = errorMessage
					+ "ERROR: Documents for Submission In Expected But Not in UserInterface - "
					+ this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface() + "\n";
		}
		

		new GuidewireHelpers(driver).logout();


		if (!errorMessage.equals("")) {
			Assert.fail(errorMessage);
		}

	}

	private void addNewPolicyMember(boolean basicSearch, ContactSubType contact, String firstName, String lastName) throws Exception {
		// Adding policy member
        SideMenuPC sideMenu = new SideMenuPC(driver);

		sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
		household.clickSearch();
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);

		if (contact.equals(ContactSubType.Person)) {
			addressBook.searchAddressBookByFirstLastName(basicSearch, firstName, lastName,
					this.myPolicyObjPL.pniContact.getAddress().getLine1(), this.myPolicyObjPL.pniContact.getAddress().getCity(),
					this.myPolicyObjPL.pniContact.getAddress().getState(), this.myPolicyObjPL.pniContact.getAddress().getZip(),
					CreateNew.Create_New_Always);
            householdMember.setDateOfBirth(DateUtils.convertStringtoDate("01/01/1980", "MM/dd/YYYY"));
        } else {
			addressBook.searchAddressBookByCompanyName(basicSearch, lastName, myPolicyObjPL.pniContact.getAddress(),
					CreateNew.Create_New_Always);
		}

		householdMember.selectRelationshipToInsured(RelationshipToInsured.SignificantOther);
		householdMember.setSSN(StringsUtils.generateRandomNumberDigits(9));
        householdMember.setNewPolicyMemberAlternateID(this.VehicleAdditionalInterestLastName);
		
		householdMember.selectNotNewAddressListingIfNotExist(myPolicyObjPL.pniContact.getAddress());

		boolean relatedContactExists = householdMember.checkRelatedContactsTab();

		if (contact.equals(ContactSubType.Person)) {
			householdMember.clickRelatedContactsTab();
			householdMember.clickBasicsContactsTab();
        }

		if (contact.equals(ContactSubType.Company) && relatedContactExists) {
			Assert.fail("Related Contacts tab exists for the company contact ");
		}
		householdMember.clickOK();
    }
}

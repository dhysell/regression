package regression.r2.noclock.policycenter.documents.squiresection3;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IssuanceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyDocuments;
import repository.pc.policy.PolicyMenu;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_AdditionalCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_ExclusionsConditions;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.generic.GenericWorkorderSquireEligibility;
import repository.pc.workorders.generic.GenericWorkorderVehicles_CoverageDetails;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import repository.pc.workorders.generic.GenericWorkorderVehicles_ExclusionsAndConditions;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import persistence.globaldatarepo.helpers.VINHelper;
public class TestSectionIIIRewriteReminderOfTermFormInference extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObjPL;
	private String insFirstName = "Cancel";
	private String insLastName = "FormInfer";
	private String driverFirstName = "testDr";
	private String VehicleAdditionalInterestLastName = "VEHPerson" + StringsUtils.generateRandomNumberDigits(7);
	private Underwriters uw;

	@Test()
	public void testCreateSquireAutoBound() throws Exception {

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
		
		Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter),
				DateAddSubtractOptions.Day, -5);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName(insFirstName, insLastName)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withPolEffectiveDate(newEff)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicySubmitted);
	}

	@Test(dependsOnMethods = { "testCreateSquireAutoBound" })
	private void testIssueSectionIIIWithForms() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter_Supervisor);

		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				this.myPolicyObjPL.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        PolicyMenu policyMenu = new PolicyMenu(driver);
		policyMenu.clickMenuActions();
        policyMenu.clickIssuePolicy();

		// Adding policy member
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

		// UW I323 10 14 Drive Other Car
		additoianlcoverages.setDriveOtherCar(true);
		
		// Exclusions and Conditions
		coverages.clickCoverageExclusionsTab();
        GenericWorkorderSquireAutoCoverages_ExclusionsConditions paExclusions = new GenericWorkorderSquireAutoCoverages_ExclusionsConditions(driver);

		// UW I361 01 08 Additional Insured Endorsement
		paExclusions.addAdditionalInsuredEndorsement("Test Company");
		
		// UW I301 10 14 Modification of "Insured Vehicle" Definition
		// Endorsement
		paExclusions.addModificationOfInsuredVehicleDefinitionEndorsement("BMW", "Beemer", "1B45678645456", "1999");
		
		// UW I305 01 08 Special Endorsement For Auto
		paExclusions.addSpecialEndorsementForAuto("Special Endorsement");
		
		// UW I304 10 14 Driver Exclusion Endorsement
		paExclusions.addDriverExclusionEndorsement304(driverFirstName);

		sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehicalTab = new GenericWorkorderVehicles_Details(driver);
        GenericWorkorderVehicles_CoverageDetails vehcileCoverages = new GenericWorkorderVehicles_CoverageDetails(driver);
        GenericWorkorderVehicles_ExclusionsAndConditions vehicleExclusions = new GenericWorkorderVehicles_ExclusionsAndConditions(driver);
		vehicalTab.clickLinkInVehicleTable("Edit");

        vehicalTab.setFactoryCostNew(20000);
        // UW I363 10 14 Insured Vehicle Leasing Endorsement
		vehicalTab.addExistingAutoAdditionalInterest(VehicleAdditionalInterestLastName, "Lessor");		
		vehcileCoverages.selectVehicleCoverageDetailsTab();
        vehcileCoverages.setComprehensive(true);
		vehcileCoverages.setCollision(true);
		// UW I335 01 08 Additional Living Expense Endorsement
		vehcileCoverages.setAdditionalLivingExpense(true);
		
		// UW I310 10 14 Deletion of Material Damage Coverage Endorsement
		vehicleExclusions.clickExclusionsAndConditionsTab();
        vehicleExclusions.setDeletionOfMaterial310(true);
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
        // UW I303 10 14 Show Car Driver Exclusion Endorsement
		vehicleExclusions.addShowCarDriverExclusionEndorsement303(insFirstName);		
		vehicalTab.clickOK();
		
		sideMenu.clickSideMenuRiskAnalysis();
		risk.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if (quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();
		risk.approveAll_IncludingSpecial();
        sideMenu.clickSideMenuForms();
		
		sideMenu.clickSideMenuQuote();
        quote.issuePolicy(IssuanceType.NoActionRequired);

        GenericWorkorderComplete submittedPage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(submittedPage.getJobCompleteTitleBar());
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "testIssueSectionIIIWithForms" })
	public void testCancellation() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter_Supervisor);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.accountNumber);

        Date currentDate = myPolicyObjPL.squire.getEffectiveDate();
		Date cancellationDate = DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 2);
        StartCancellation cancelPol = new StartCancellation(driver);

		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.WentWithAnotherCarrier, "Testing Purpose",
				cancellationDate, true);
        new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "testCancellation" })
	private void testRewriteReminderOfTerm() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter_Supervisor);
		
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),this.myPolicyObjPL.accountNumber);

        StartRewrite rewritePage = new StartRewrite(driver);
        PolicyMenu policyMenu = new PolicyMenu(driver);
		policyMenu.clickMenuActions();
		policyMenu.clickRewriteRemainderOfTerm();
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuLineSelection();
        GenericWorkorderSquireEligibility eligibilityPage = new GenericWorkorderSquireEligibility(driver);
		for (int i = 0; i < 10; i++) {
			eligibilityPage.clickNext();
        }
		
		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();

		sideMenu.clickSideMenuRiskAnalysis();
		risk.approveAll_IncludingSpecial();

		sideMenu.clickSideMenuForms();

		sideMenu.clickSideMenuQuote();
		rewritePage.clickIssuePolicy();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());

        new GuidewireHelpers(driver).logout();

	}
	
	@Test(dependsOnMethods = { "testRewriteReminderOfTerm" })
	private void testValidateSectionIIIDocuments() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				this.myPolicyObjPL.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuToolsDocuments();
        PolicyDocuments docs = new PolicyDocuments(driver);
		docs.selectRelatedTo("Rewrite Remainder");
		docs.clickSearch();
        ArrayList<String> descriptions = docs.getDocumentsDescriptionsFromTable();

		String[] documents = { "Classic Car Limitation of Coverage Endorsement", "Certificate of Liability Insurance",
				"Automobile Policy Booklet","Notice of Policy on Privacy","Adverse Action Letter", "Definition Endorsement", "Show Car Driver Exclusion Endorsement",
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

			if (documentFound) {
				testFailed = true;
				errorMessage = errorMessage + "UnExpected document : '" + document
						+ "' available in documents page. \n";
			}
		}
		if (testFailed)
			Assert.fail(errorMessage);

	}
	
}

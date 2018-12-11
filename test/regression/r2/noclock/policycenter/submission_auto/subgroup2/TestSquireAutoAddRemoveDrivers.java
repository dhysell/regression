package regression.r2.noclock.policycenter.submission_auto.subgroup2;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.enums.State;

import repository.driverConfiguration.Config;
import repository.gw.activity.ActivityPopup;
import repository.gw.enums.CommuteType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.Vehicle.Comprehensive_CollisionDeductible;
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

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement :DE4037: RunTimeException and DBException: Error displayed when tried to remove a driver .
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Sep 30, 2016
 */
public class TestSquireAutoAddRemoveDrivers extends BaseTest {
	private WebDriver driver;
    private GeneratePolicy myPolicyObjPL;
    private Underwriters uw;



    @Test
    public void testAddRemoveDrivers() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
		// Coverages
		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK, true, UninsuredLimit.Fifty, true, UnderinsuredLimit.Fifty);
		coverages.setAccidentalDeath(true);

		// driver
		ArrayList<Contact> driversList = new ArrayList<Contact>();
		Contact person1 = new Contact("Test-"+StringsUtils.generateRandomNumberDigits(4), "Senior", Gender.Male, DateUtils.convertStringtoDate("01/01/1960", "MM/dd/yyyy"));
		driversList.add(person1);

		// Vehicle
		ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
		Vehicle toAdd = new Vehicle();
		toAdd.setVehicleTypePL(VehicleTypePL.PrivatePassenger);
		toAdd.setEmergencyRoadside(true);
		toAdd.setAdditionalLivingExpense(true);
		toAdd.setDriverPL(person1);
		toAdd.setComprehensiveDeductible(Comprehensive_CollisionDeductible.FiveHundred500);
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
				.withInsFirstLastName("Auto", "RemoveDr")
				.build(GeneratePolicyType.FullApp);
        driver.quit();

        cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        //Adding more drivers..
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();
		Contact driver1 = new Contact("Test"+StringsUtils.generateRandomNumberDigits(6), "NewDriver", Gender.Female, DateUtils.convertStringtoDate("01/01/1970", "MM/dd/yyyy"));
		Contact driver2 = new Contact("Test"+StringsUtils.generateRandomNumberDigits(6), "TestingDriver", Gender.Male, DateUtils.convertStringtoDate("01/01/1972", "MM/dd/yyyy"));
		addNewPolicyMember(myPolicyObjPL.basicSearch, driver1, RelationshipToInsured.Insured);
		addNewPolicyMember(myPolicyObjPL.basicSearch, driver2, RelationshipToInsured.SignificantOther);

		// adding drivers list
		AddingNewDriver(driver1.getLastName(), driver1.getGender(), "FullTime Student", "ABCD1234C");
		AddingNewDriver(driver2.getLastName(), driver2.getGender(), "Relax", "CB54553D");
        SideMenuPC sideMenu = new SideMenuPC(driver);

		//***************************************************************************************//
		//NOTE : This is a temp fix for Discard UnSaved error, snippet will be removed once fixed.
		sideMenu.clickSideMenuPACoverages();
        GenericWorkorderSquireAutoDrivers paDrivers = new GenericWorkorderSquireAutoDrivers(driver);
		if (new GuidewireHelpers(driver).errorMessagesExist() && (new GuidewireHelpers(driver).getErrorMessages().toString().contains("Discard Unsaved Change"))) {
			new GuidewireHelpers(driver).clickDiscardUnsavedChangesLink();
            AddingNewDriver(driver1.getLastName(), driver1.getGender(), "FullTime Student", "ABCD1234C");
			AddingNewDriver(driver2.getLastName(), driver2.getGender(), "Relax", "CB54553D");
			sideMenu.clickSideMenuPACoverages();
		}
		//***************************************************************************************//

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		quote.clickSaveDraftButton();
		quote.clickQuote();


		if(quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
            GenericWorkorderRiskAnalysis_UWIssues risk = new GenericWorkorderRiskAnalysis_UWIssues(driver);
            risk.clickUWIssuesTab();
            risk.handleBlockQuote(myPolicyObjPL);
            ActivityPopup activityPopupPage = new ActivityPopup(driver);
			activityPopupPage.clickActivityCancel();
		}		
		sideMenu.clickSideMenuQuote();	
		polInfo.clickEditPolicyTransaction();
        //Removing drivers
        sideMenu.clickSideMenuPADrivers();
        //GenericWorkorderSquireDrivers paDrivers = new GenericWorkorderSquireDrivers();
        paDrivers.setCheckBoxInDriverTableByDriverName(driver1.getLastName());
        paDrivers.setCheckBoxInDriverTableByDriverName(driver2.getLastName());
        paDrivers.clickRemoveButton();
        quote.clickSaveDraftButton();
        quote.clickQuote();
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
            GenericWorkorderRiskAnalysis_UWIssues risk = new GenericWorkorderRiskAnalysis_UWIssues(driver);
            risk.clickUWIssuesTab();
            risk.handleBlockQuote(myPolicyObjPL);
		}
        sideMenu.clickSideMenuQuote();
    }

	private void AddingNewDriver(String lastName, Gender gender, String occupation, String license) throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        paDrivers.addExistingDriver(lastName);
        paDrivers.selectMaritalStatus(MaritalStatus.Single);
		paDrivers.selectGender(gender);
		paDrivers.setOccupation(occupation);
		paDrivers.setLicenseNumber(license);
		paDrivers.selectLicenseState(State.Idaho);
		paDrivers.selectCommuteType(CommuteType.WorkFromHome);
        paDrivers.setPhysicalImpairmentOrEpilepsy(false);
		paDrivers.selectDriverHaveCurrentInsurance(true);
		paDrivers.setCurrentInsuranceCompanyPolicyNumber("Farm Bureau Policy #: 1B124576");
		paDrivers.clickOk();
    }
	private void addNewPolicyMember(boolean basicSearch, Contact person, RelationshipToInsured relationship) throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuHouseholdMembers();

        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
		household.clickSearch();
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
		addressBook.searchAddressBookByFirstLastName(basicSearch, person.getFirstName(), person.getLastName(), null, null, null, null, CreateNew.Create_New_Always);
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        householdMember.setDateOfBirth(person.getDob());
		householdMember.selectRelationshipToInsured(relationship);
		householdMember.setNewPolicyMemberAlternateID(StringsUtils.generateRandomNumberDigits(12));
		householdMember.selectNotNewAddressListingIfNotExist(myPolicyObjPL.pniContact.getAddress());
		householdMember.clickRelatedContactsTab();
		householdMember.clickOK();

	}


}

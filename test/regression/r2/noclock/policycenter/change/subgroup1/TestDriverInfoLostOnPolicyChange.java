package regression.r2.noclock.policycenter.change.subgroup1;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.CommuteType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.Vehicle.CommutingMiles;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.errorhandling.ErrorHandling;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandibanda
 * @Requirement : DE4246 : Driver Information (marital status, license number and license state) is lost on Policy Change
 * @Description - Generated Squire III Auto Issuance with additional driver and assign him to vehicle in policy change and quote
 * , also added scenario where additional driver was added and assigned him to vehicle in policy change and quoted,
 * also done for Renewal and Rewrite jobs
 * @DATE Nov 29, 2016
 */
@QuarantineClass
public class TestDriverInfoLostOnPolicyChange extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy polToReturn, myPolicyObj;	
	private Underwriters uw;

	//adding driver part of issuance and assign him during policy change and quote
	@Test()
	public void testGenerateSquireAutoOnlyIssuance() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.FiveK);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        polToReturn = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
				.withProductType(ProductLineType.Squire)				
				.withPolOrgType(OrganizationType.Individual)
				.withInsFirstLastName("Test", "Auto")	
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);	
	}

	@Test (dependsOnMethods = { "testGenerateSquireAutoOnlyIssuance" })
	public void verifyValidationMessagesForPolicyChange() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), polToReturn.squire.getPolicyNumber());

		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);


		//add 10 days to current date
		Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 10);	

		//start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", changeDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);

		sideMenu.clickSideMenuPolicyInfo();		
		sideMenu.clickSideMenuHouseholdMembers();

		Contact newMember = new Contact("Test"+StringsUtils.generateRandomNumberDigits(6), "NewDriver", Gender.Female, DateUtils.convertStringtoDate("01/01/1970", "MM/dd/yyyy"));
        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
		household.clickSearch();
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
		addressBook.searchAddressBookByFirstLastName(polToReturn.basicSearch, newMember.getFirstName(), newMember.getLastName(), null, null, null, null, CreateNew.Create_New_Always);
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        householdMember.setDateOfBirth(newMember.getDob());
		householdMember.selectRelationshipToInsured(RelationshipToInsured.Insured);
		householdMember.selectNotNewAddressListingIfNotExist(polToReturn.pniContact.getAddress());
		householdMember.setNewPolicyMemberAlternateID(StringsUtils.generateRandomNumberDigits(12));
        householdMember.clickRelatedContactsTab();
		householdMember.clickOK();

		// adding driver		
		sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
		paDrivers.addExistingDriver(newMember.getLastName());
		paDrivers.selectMaritalStatus(MaritalStatus.Single);
		paDrivers.selectGender(Gender.Female);
		paDrivers.setOccupation("QA");
		paDrivers.setLicenseNumber("AB123456C");
		State state = State.Idaho;
		paDrivers.selectLicenseState(state);
		paDrivers.selectCommuteType(CommuteType.WorkFromHome);
        paDrivers.setPhysicalImpairmentOrEpilepsy(false);
		paDrivers.clickOk();

		//***************************************************************************************//
		//NOTE : This is a temp fix for Discard UnSaved error, snippet will be removed once fixed.
		sideMenu.clickSideMenuPACoverages();
		if (new GuidewireHelpers(driver).errorMessagesExist() && (new GuidewireHelpers(driver).getErrorMessages().toString().contains("Discard Unsaved Change"))) {
			new GuidewireHelpers(driver).clickDiscardUnsavedChangesLink();
            sideMenu.clickSideMenuPADrivers();
			paDrivers.addExistingDriver(newMember.getLastName());
			paDrivers.selectMaritalStatus(MaritalStatus.Single);
			paDrivers.selectGender(Gender.Female);
			paDrivers.setOccupation("QA");
			paDrivers.setLicenseNumber("AB123456C");
			paDrivers.selectLicenseState(state);
			paDrivers.selectCommuteType(CommuteType.WorkFromHome);
            paDrivers.setPhysicalImpairmentOrEpilepsy(false);
			paDrivers.clickOk();
            sideMenu.clickSideMenuPACoverages();
		}
		//***************************************************************************************//
		
		sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
		vehiclePage.createVehicleManually();
		vehiclePage.createGenericVehicle(VehicleTypePL.PassengerPickup);
		vehiclePage.setVIN("2C3CCABG8EH323885");
		vehiclePage.setModelYear(2000);
		vehiclePage.setMake("Honda");
		vehiclePage.setModel("Accord");
		vehiclePage.selectCommunity(CommutingMiles.Pleasure1To2Miles);
		vehiclePage.selectGaragedAtZip("ID");
		vehiclePage.selectDriverToAssign(newMember);
		vehiclePage.clickOK();

		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if(quote.isPreQuoteDisplayed()){
			risk.clickReturnRiskAnalysisPage();
            risk.clickUWIssuesTab();
			risk.approveAll();
			risk.Quote();			
		}
        ErrorHandling validationResults = new ErrorHandling(driver);
		if(validationResults.checkIfValidationResultsExists())				
			Assert.fail("Validation results should not exists");	
				
		quote.clickIssuePolicy();
	}

	//added additional driver and assign him to vehicle in policy change and quote
	@Test ()
	public void testGenerateSquireAutoPolicyChange() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK);

		ArrayList<Contact> driversList = new ArrayList<Contact>();
		Contact driver1;
		driver1 = new Contact("Test", "Driver", Gender.Female,DateUtils.convertStringtoDate("06/01/1955", "MM/dd/YYYY") );
		driversList.add(driver1);

		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);		
		squirePersonalAuto.setDriversList(driversList);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
				.withInsFirstLastName("Guy", "Auto")				
				.withPolOrgType(OrganizationType.Individual)				
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	} 

	@Test (dependsOnMethods = { "testGenerateSquireAutoPolicyChange" })
	public void verifyValidationMessagesWithoutAddingDriver() throws Exception {		

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.squire.getPolicyNumber());
		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);


        //add 10 days to current date
		Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 10);	

		//start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", changeDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);

		sideMenu.clickSideMenuPolicyInfo();		
		sideMenu.clickSideMenuHouseholdMembers();

		sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
		vehiclePage.createVehicleManually();
		vehiclePage.createGenericVehicle(VehicleTypePL.PassengerPickup);
		vehiclePage.setVIN("2C3CCABG8EH323885");
		vehiclePage.setModelYear(2000);
		vehiclePage.setMake("Honda");
		vehiclePage.setModel("Accord");
		vehiclePage.selectCommunity(CommutingMiles.Pleasure1To2Miles);
		vehiclePage.selectGaragedAtZip("ID");
		vehiclePage.selectDriverToAssign(myPolicyObj.squire.policyMembers.get(1).getFirstName());
		vehiclePage.clickOK();

		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();

        ErrorHandling validationResults = new ErrorHandling(driver);
		if(validationResults.checkIfValidationResultsExists())					
			Assert.fail("Validation results should not exists");

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		quote.clickIssuePolicy();
	}
}

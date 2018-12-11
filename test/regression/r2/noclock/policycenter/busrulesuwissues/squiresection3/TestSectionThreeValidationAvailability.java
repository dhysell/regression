package regression.r2.noclock.policycenter.busrulesuwissues.squiresection3;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.enums.Gender;
import com.idfbins.enums.OkCancel;
import com.idfbins.enums.State;

import repository.driverConfiguration.Config;
import repository.gw.enums.CommuteType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SRPIncident;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.ValidationRules_SectionIII;
import repository.gw.enums.Vehicle.CommutingMiles;
import repository.gw.enums.Vehicle.Comprehensive_CollisionDeductible;
import repository.gw.enums.Vehicle.VehicleTruckTypePL;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_MotorVehicleRecord;
import repository.pc.workorders.generic.GenericWorkorderVehicles_CoverageDetails;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;

/**
 * @Author nvadlamudi
 * @Requirement :Squire-Section I-Product-Model
 * @RequirementsLink <a href=
 * "http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/Product%20Model%20Spreadsheets/squire%20product%20model%20spreadhseets/Squire-Section%20I-Product-Model.xlsx">
 * Squire-Section I-Product-Model</a>
 * @Description :Section I - Avalability rules are added
 * @DATE Sep 11, 2017
 */
public class TestSectionThreeValidationAvailability extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObjPL = null;
	
	SoftAssert softAssert = new SoftAssert();
	private GeneratePolicy myLNPolicyObjPL;

	@Test
	public void testGenerateCustomAuto() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = new SquirePersonalAuto();

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("BusRules", "Auto")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.FullApp);
	}

	@Test(dependsOnMethods = { "testGenerateCustomAuto" })
	private void testAddCustomAutoAvailabilityRules() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(),
				myPolicyObjPL.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);

		// AU039 - Min rated age for driver
		sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
		household.clickSearch();
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
		String personFirstName = "Minor" + StringsUtils.generateRandomNumberDigits(7);
		addressBook.searchAddressBookByFirstLastName(myPolicyObjPL.basicSearch, personFirstName,
				"Added" + StringsUtils.generateRandomNumberDigits(7), this.myPolicyObjPL.pniContact.getAddress().getLine1(),
				this.myPolicyObjPL.pniContact.getAddress().getCity(), this.myPolicyObjPL.pniContact.getAddress().getState(),
				this.myPolicyObjPL.pniContact.getAddress().getZip(), CreateNew.Create_New_Always);
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
		householdMember.setDateOfBirth(DateUtils.dateAddSubtract(
				DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -13));
        householdMember.selectRelationshipToInsured(RelationshipToInsured.SignificantOther);
		householdMember.setSSN(StringsUtils.generateRandomNumberDigits(9));
        householdMember.setNewPolicyMemberAlternateID(personFirstName);
		householdMember.selectNotNewAddressListingIfNotExist(myPolicyObjPL.pniContact.getAddress());
		householdMember.clickRelatedContactsTab();
		householdMember.clickOK();
		sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
		softAssert.assertFalse(paDrivers.checkAddDriverExists(), "Min Rated age for driver is failed and able to add minor age member as driver.");
		
		// AU041 - Principal Driver Age
		sideMenu.clickSideMenuHouseholdMembers();
		household.clickPolicyHolderMembersByName(personFirstName);
		householdMember.setDateOfBirth(DateUtils.dateAddSubtract(
				DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -26));
		householdMember.clickOK();
		sideMenu.clickSideMenuPADrivers();
		paDrivers.addExistingDriver(personFirstName);
        paDrivers.selectMaritalStatus(MaritalStatus.Single);
		paDrivers.selectGender(Gender.Male);
		paDrivers.setOccupation("Testing");
		paDrivers.setLicenseNumber("ABCD454");
		State state = State.Idaho;
		paDrivers.selectLicenseState(state);
        paDrivers.selectCommuteType(CommuteType.WorkFromHome);
        paDrivers.setPhysicalImpairmentOrEpilepsy(false);
		paDrivers.selectDriverHaveCurrentInsurance(true);
		paDrivers.setCurrentInsuranceCompanyPolicyNumber("Farm Bureau Policy #: 1B124576");
		
		paDrivers.clickOk();
        new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		
		
		sideMenu.clickSideMenuPAVehicles();
        if (new GuidewireHelpers(driver).errorMessagesExist()) {
			new GuidewireHelpers(driver).clickDiscardUnsavedChangesLink();
            sideMenu.clickSideMenuPADrivers();
            paDrivers.addExistingDriver(personFirstName);
            paDrivers.selectMaritalStatus(MaritalStatus.Single);
			paDrivers.selectGender(Gender.Male);
			paDrivers.setOccupation("Testing");
			paDrivers.setLicenseNumber("ABCD454");
			paDrivers.selectLicenseState(state);
            paDrivers.selectCommuteType(CommuteType.WorkFromHome);
            paDrivers.setPhysicalImpairmentOrEpilepsy(false);
			paDrivers.selectDriverHaveCurrentInsurance(true);
			paDrivers.setCurrentInsuranceCompanyPolicyNumber("Farm Bureau Policy #: 1B124576");
			
			paDrivers.clickOk();
            new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
			sideMenu.clickSideMenuPAVehicles();
        }
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
		vehiclePage.createVehicleManually();
        vehiclePage.createGenericVehicle(VehicleTypePL.PassengerPickup);
		vehiclePage.setModelYear(2014);
		vehiclePage.setMake("Scion");
		vehiclePage.setModel("FR-S");
		vehiclePage.selectDriverToAssign(personFirstName);	
		vehiclePage.selectGaragedAtZip("ID");
		
		softAssert.assertFalse(!vehiclePage.getVehicleUse().equals("Principal"), "Vehicle Use 'Principal' is not displayed.");
		
		vehiclePage.clickOK();

		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		
		softAssert.assertAll();

	}

	@Test(dependsOnMethods = { "testAddCustomAutoAvailabilityRules" })
	private void testAddCustomAutoValidations() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(),
				myPolicyObjPL.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		
		//AU046	-	Light weight truck.
		sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        GenericWorkorderVehicles_CoverageDetails vehicleCoverages = new GenericWorkorderVehicles_CoverageDetails(driver);
		
		vehiclePage.createVehicleManually();
        vehiclePage.createGenericVehicle(VehicleTypePL.FarmTruck);
		vehiclePage.selectTruckType(VehicleTruckTypePL.TractorType);
		vehiclePage.setGVW(50000);
		vehiclePage.selectGaragedAtZip("ID");
		vehiclePage.setOdometer(20000);
		vehiclePage.setModel("Accord");
		vehiclePage.setModelYear(2013);
		vehiclePage.setMake("Honda");
		vehiclePage.clickOK();

		softAssert.assertTrue((new GuidewireHelpers(driver).containsErrorMessage(ValidationRules_SectionIII.AU046BusRule.getMessage())), "Expected page validation : '"+ValidationRules_SectionIII.AU046BusRule.getMessage() + "' is not displayed." );
		vehiclePage.setGVW(55000);
				
		//AU051	-	Collision deductible >= Comprehensive deductible
		vehicleCoverages.selectVehicleCoverageDetailsTab();
        vehicleCoverages.setComprehensive(true);
        vehicleCoverages.setCollision(true);
		vehicleCoverages.setCollisionDeductible("250");
		new GuidewireHelpers(driver).clickProductLogo();
        vehicleCoverages.clickOK();
        softAssert.assertTrue((new GuidewireHelpers(driver).containsErrorMessage(ValidationRules_SectionIII.AU051BusRule.getMessage())), "Expected page validation : '" + ValidationRules_SectionIII.AU051BusRule.getMessage() + "' is not displayed.");
			
		vehicleCoverages.setCollisionDeductible("500");
		vehicleCoverages.clickOK();		
		
		//AU052	-	Full coverage for show car
		vehiclePage.createVehicleManually();
        vehiclePage.createGenericVehicle(VehicleTypePL.ShowCar);
        
		vehiclePage.setNOVIN(true);
		vehiclePage.setMake("Honda");
		vehiclePage.setModel("Accord");
		vehiclePage.setModelYear(2014);
		vehiclePage.selectGaragedAtZip("ID");
		vehiclePage.setStatedValue(20000);
		vehiclePage.setOdometer(0);
		vehiclePage.clickOK();
        softAssert.assertTrue((new GuidewireHelpers(driver).containsErrorMessage(ValidationRules_SectionIII.AU052BusRule.getMessage())), "Expected page validation : '" + ValidationRules_SectionIII.AU052BusRule.getMessage() + "' is not displayed.");
				
		//AU077	-	Same Comp Deductible
		vehicleCoverages.selectVehicleCoverageDetailsTab();
        vehicleCoverages.setComprehensive(true);

		vehicleCoverages.selectComprehensiveDeductible(Comprehensive_CollisionDeductible.TwoHundredFifty250);

		//AU078	-	Same Coll Deductible
		vehicleCoverages.setCollision(true);

		vehicleCoverages.selectCollisionDeductible(Comprehensive_CollisionDeductible.TwoHundredFifty250);
		vehiclePage.clickOK();
        softAssert.assertFalse(!new GuidewireHelpers(driver).selectOKOrCancelFromPopup(OkCancel.OK).contains(ValidationRules_SectionIII.AU077BusRule.getMessage()), "Expected page validation : '" + ValidationRules_SectionIII.AU077BusRule.getMessage() + "' is not displayed.");

		//AU079	-	Same Rental Remb Limit
		vehiclePage.editVehicleByType(VehicleTypePL.PrivatePassenger);
		vehiclePage.setFactoryCostNew(20000);
		vehicleCoverages.selectVehicleCoverageDetailsTab();
        vehicleCoverages.setComprehensive(true);

		vehicleCoverages.setCollision(true);
		vehicleCoverages.setRentalReimbursementDeductible("50");
		vehicleCoverages.clickOK();

		vehiclePage.editVehicleByType(VehicleTypePL.PassengerPickup);
		vehicleCoverages.selectVehicleCoverageDetailsTab();
        vehicleCoverages.setComprehensive(true);
        vehicleCoverages.setCollision(true);
		vehicleCoverages.setRentalReimbursementDeductible("25");
		vehicleCoverages.clickOK();

        softAssert.assertTrue(new GuidewireHelpers(driver).containsErrorMessage(ValidationRules_SectionIII.AU082BusRule.getMessage()), "Expected page validation : '"+ValidationRules_SectionIII.AU082BusRule.getMessage() + "' is not displayed." );


        //	-	Removing contact from policy member
		sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
		household.clickRemoveMember("Minor");


        softAssert.assertTrue(new GuidewireHelpers(driver).containsErrorMessage("must be removed as a driver before removing as a policy member"), "Expected page validation : 'must be removed as a driver before removing as a policy member' is not displayed" );
				
		softAssert.assertAll();		
	}
	
	////////////////////////////////////////////////////
	//JLARSEN DISABLED BECAUSE IT APPEARS NOT ALL LEXUS NEXUS DATA IS ACCOUNTED FOR IN GENERATE. DONT HAVE TIME TO FIX IT TODAY.
	///////////////////////////////////////////////////
	@Test(enabled = false)
	private void testAddLNCustomAuto() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = new SquirePersonalAuto();

        myLNPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withLexisNexisData(true, false, false, false, true, false, false)
                .build(GeneratePolicyType.FullApp);
		driver.quit();
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchSubmission(myLNPolicyObjPL.agentInfo.getAgentUserName(), myLNPolicyObjPL.agentInfo.getAgentPassword(),
				myLNPolicyObjPL.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.orderPrefill();
		polInfo.clickReturnToOrderPrefillReport();
		
		
		//AU040	-	Prefill Vehicle
		sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
		vehiclePage.selectFirstVehicleFromPrefillVehicles();
		vehiclePage.clickNext();
        //AU094	No Driver Assigned	No Driver Assigned
		softAssert.assertTrue(((new GuidewireHelpers(driver).containsErrorMessage(ValidationRules_SectionIII.AU094BusRule.getMessage()))), "Expected page validation : '"+ValidationRules_SectionIII.AU094BusRule.getMessage() + "' is not displayed." );//no driver assigned
		softAssert.assertTrue(((new GuidewireHelpers(driver).containsErrorMessage(ValidationRules_SectionIII.AU040BusRule.getMessage()))), "Expected page validation : '"+ValidationRules_SectionIII.AU040BusRule.getMessage() + "' is not displayed." );
		vehiclePage.editVehicleByVehicleNumber(2);
		vehiclePage.selectCommunity(CommutingMiles.Pleasure1To2Miles);
		vehiclePage.selectGaragedAtZip("ID");
		vehiclePage.clickOK();

        //AU090	-	Vehicular homicide and driver
		sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_MotorVehicleRecord paDrivers = new GenericWorkorderSquireAutoDrivers_MotorVehicleRecord(driver);
		paDrivers.clickEditButtonInDriverTable(1);
        paDrivers.clickMotorVehicleRecord();
        paDrivers.selectMVRIncident(1, SRPIncident.VehicularHomicide);
		try {
			paDrivers.clickOk();
		} catch(GuidewireNavigationException naviagionException) {
			softAssert.assertTrue(((new GuidewireHelpers(driver).containsErrorMessage(ValidationRules_SectionIII.AU090BusRule.getMessage()))), "Expected page validation : '"+ValidationRules_SectionIII.AU090BusRule.getMessage() + "' is not displayed." );
		}
		
		softAssert.assertAll();
	}
	
	
				
}

package regression.r2.noclock.policycenter.change.subgroup5;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.enums.State;

import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.CommuteType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.BoatType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.InlandMarineTypes.RecreationalEquipmentType;
import repository.gw.enums.InlandMarineTypes.WatercraftTypes;
import repository.gw.enums.InlandMarineTypes.WatercratItems;
import repository.gw.enums.LineSelection;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.TransactionType;
import repository.gw.enums.Vehicle.DriverVehicleUsePL;
import repository.gw.enums.Vehicle.PAComprehensive_CollisionDeductible;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.RecreationalEquipment;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireIMWatercraft;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.Vehicle;
import repository.gw.generate.custom.WatercraftScheduledItems;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : DE4828: Nullpointer Error on OOS Policy Change Handle Preemption
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/33274298124d/detail/defect/89955147272/tasks">Link Text</a>
 * @Description
 * @DATE Feb 16, 2017
 */
public class TestSquireCancelledPolicyChange extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy squirePolicy;
	private Underwriters uw;

	@Test()
	public void testIssueSquirePol() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<>();

		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

		// Watercraft
		WatercraftScheduledItems boat = new WatercraftScheduledItems(WatercraftTypes.BoatAndMotor,
				DateUtils.convertStringtoDate("01/01/1999", "MM/dd/yyyy"), 10000);
		boat.setLimit(3123);
		boat.setItem(WatercratItems.Boat);
		boat.setLength(28);
		boat.setBoatType(BoatType.Outboard);

		ArrayList<WatercraftScheduledItems> boats = new ArrayList<WatercraftScheduledItems>();
		boats.add(boat);
		ArrayList<String> boatAddInsured = new ArrayList<String>();
		boatAddInsured.add("Cor Hofman");
		ArrayList<AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
		AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Person);
		loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);

		SquireIMWatercraft boatType = new SquireIMWatercraft(WatercraftTypes.BoatAndMotor,
				PAComprehensive_CollisionDeductible.OneHundred100, boats);
		boatType.setAdditionalInterest(loc2Bldg1AdditionalInterests);
		ArrayList<SquireIMWatercraft> boatTypes = new ArrayList<SquireIMWatercraft>();
		boatTypes.add(boatType);

		// Farm Equipment
		IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment",
				"Manly Farm Equipment", 1000);
		ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
		farmEquip.add(farmThing);
		FarmEquipment imFarmEquip = new FarmEquipment(IMFarmEquipmentType.FarmEquipment, CoverageType.BroadForm,
				IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
		ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
		allFarmEquip.add(imFarmEquip);

		// Rec Equip
		ArrayList<RecreationalEquipment> recVehicle = new ArrayList<RecreationalEquipment>();
		recVehicle.add(new RecreationalEquipment(RecreationalEquipmentType.AllTerrainVehicle, "500",
				PAComprehensive_CollisionDeductible.Fifty50, "Brett Hiltbrand"));

		SquireLiability liability = new SquireLiability();
		ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
		imTypes.add(InlandMarine.FarmEquipment);
		imTypes.add(InlandMarine.RecreationalEquipment);
		imTypes.add(InlandMarine.Watercraft);

		ArrayList<Contact> driversList = new ArrayList<Contact>();
		Contact person = new Contact();
		driversList.add(person);
		ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
		Vehicle vehicle = new Vehicle(VehicleTypePL.PrivatePassenger, "make new vin", 2003, "Oldie", "But Goodie");
		vehicleList.add(vehicle);

		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow,
				MedicalLimit.TenK);
		coverages.setUnderinsured(false);

		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);
		squirePersonalAuto.setVehicleList(vehicleList);
		squirePersonalAuto.setDriversList(driversList);

        SquirePropertyAndLiability myProperty = new SquirePropertyAndLiability();
        myProperty.locationList = locationsList;
        myProperty.liabilitySection = liability;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.recEquipment_PL_IM = recVehicle;
        myInlandMarine.farmEquipment = allFarmEquip;
        myInlandMarine.watercrafts_PL_IM = boatTypes;
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;

        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myProperty;
        mySquire.inlandMarine = myInlandMarine;

        squirePolicy = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL, LineSelection.InlandMarineLinePL)
                .withInsFirstLastName("Squire", "Change")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
	}

	@Test(dependsOnMethods = { "testIssueSquirePol" })
	private void testSquireCancelled() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				squirePolicy.accountNumber);

        StartCancellation cancelPol = new StartCancellation(driver);
		Date currentDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter),
				DateAddSubtractOptions.Day, 20);

		String comment = "Testing Purpose";
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.LossRuns, comment, currentDate, true);

		String errorMessage = "";

        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
		  policySearchPC.searchPolicyByAccountNumber(squirePolicy.accountNumber);

        PolicySummary summary = new PolicySummary(driver);
		if (summary.getTransactionNumber(TransactionType.Cancellation, comment) == null)
			errorMessage = "Cancellation is not done!!!";

		if (errorMessage != "")
			Assert.fail(errorMessage);
	}

	@Test(dependsOnMethods = { "testSquireCancelled" })
	private void testSquirePolicyChange() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter);
		
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),squirePolicy.accountNumber);

		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
        Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 10);

		// start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", changeDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		Contact newDriver = new Contact("driver-" + StringsUtils.generateRandomNumberDigits(9), "excluded",Gender.Female ,
				DateUtils.getDateValueOfFormat(
						DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter),
								DateAddSubtractOptions.Year, -40),
						"MM/dd/yyyy"));
		sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers hmember = new GenericWorkorderPolicyMembers(driver);
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
		hmember.clickSearch();
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
		addressBook.searchAddressBookByFirstLastName(squirePolicy.basicSearch, newDriver.getFirstName(), newDriver.getLastName(), null, null,
				null, null, CreateNew.Create_New_Always);
        householdMember.setDateOfBirth(newDriver.getDob());
		householdMember.selectRelationshipToInsured(RelationshipToInsured.SignificantOther);
		householdMember.selectNotNewAddressListingIfNotExist(this.squirePolicy.pniContact.getAddress());
		householdMember.setNewPolicyMemberAlternateID(StringsUtils.generateRandomNumberDigits(12));
		householdMember.clickRelatedContactsTab();
        householdMember.clickOK();

		// adding the newly created contact in driver list
		sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
		paDrivers.addExistingDriver(newDriver.getLastName());
		paDrivers.selectMaritalStatus(MaritalStatus.Single);
		paDrivers.selectGender(newDriver.getGender());
		paDrivers.setPhysicalImpairmentOrEpilepsy(false);
		paDrivers.selectDriverHaveCurrentInsurance(true);
		paDrivers.setCurrentInsuranceCompanyPolicyNumber("Farm Bureau Policy #: 1B124576");
		paDrivers.setOccupation("Testing");
		paDrivers.setLicenseNumber("CB54553D");
		State state = State.Idaho;
		paDrivers.selectLicenseState(state);
		paDrivers.selectCommuteType(CommuteType.WorkFromHome);
        paDrivers.clickOk();

		//***************************************************************************************//
		//NOTE : This is a temp fix for Discard UnSaved error, snippet will be removed once fixed.
		sideMenu.clickSideMenuPACoverages();		
		if (new GuidewireHelpers(driver).errorMessagesExist() && (new GuidewireHelpers(driver).getErrorMessages().toString().contains("Discard Unsaved Change"))) {			
			new GuidewireHelpers(driver).clickDiscardUnsavedChangesLink();
			sideMenu.clickSideMenuPADrivers();
			paDrivers.addExistingDriver(newDriver.getLastName());
			paDrivers.selectMaritalStatus(MaritalStatus.Single);
			paDrivers.selectGender(newDriver.getGender());
			paDrivers.setPhysicalImpairmentOrEpilepsy(false);
			paDrivers.setOccupation("Testing");
			paDrivers.setLicenseNumber("CB54553D");		
			paDrivers.selectLicenseState(state);
			paDrivers.selectCommuteType(CommuteType.WorkFromHome);
            paDrivers.clickOk();
            sideMenu.clickSideMenuPACoverages();
		}
		//***************************************************************************************//
		// adding vehicle
		sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
		vehiclePage.createVehicleManually();
		Vin newvin = vehiclePage.createGenericVehicle(VehicleTypePL.PrivatePassenger);

		ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
		Vehicle newVeh = new Vehicle();
		newVeh.setVin(newvin.getVin());
		newVeh.setEmergencyRoadside(true);
		vehicleList.add(newVeh);

		vehiclePage.addDriver(newDriver, DriverVehicleUsePL.Principal);
		vehiclePage.selectGaragedAtZip("ID");
		vehiclePage.setFactoryCostNew(10000);
		vehiclePage.clickOK();

		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
		qualificationPage.clickQuote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if (quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
			sideMenu.clickSideMenuRiskAnalysis();
            GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
			riskAnalysis.approveAll_IncludingSpecial();
            qualificationPage.clickQuote();
			sideMenu.clickSideMenuQuote();
		}
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
		riskAnalysis.approveAll_IncludingSpecial();
		riskAnalysis.approveAll();
        sideMenu.clickSideMenuQuote();

        StartPolicyChange change = new StartPolicyChange(driver);
		change.clickIssuePolicy();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());

	}
}

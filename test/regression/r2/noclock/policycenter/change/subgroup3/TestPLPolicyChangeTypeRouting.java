package regression.r2.noclock.policycenter.change.subgroup3;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.ChangeReason;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.InlandMarineTypes.RecreationalEquipmentType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.Vehicle.PAComprehensive_CollisionDeductible;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.RecreationalEquipment;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.activity.UWActivityPC;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;
import persistence.globaldatarepo.entities.Underwriters;

/**
 * @Author nvadlamudi
 * @Requirement :US8607: Policy Change Type and Routing
 * @RequirementsLink <a href="http://http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Homeowners/PC8%20-%20HO%20-%20Change%20-%20Quote%20Policy%20Change.xlsx ">Link Text</a>
 * @Description
 * @DATE Sep 15, 2016
 */
@QuarantineClass
public class TestPLPolicyChangeTypeRouting extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy mySQPolicyObjPL;


	@Test ()
	private void testIssueSquirePolChange() throws Exception{
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);		

		// Farm Equipment
		IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment", "Manly Farm Equipment", 1000);
		ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
		farmEquip.add(farmThing);
		FarmEquipment imFarmEquip = new FarmEquipment(IMFarmEquipmentType.FarmEquipment, CoverageType.BroadForm, IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
		ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
		allFarmEquip.add(imFarmEquip);

		// Rec Equip
		ArrayList<RecreationalEquipment> recVehicle = new ArrayList<RecreationalEquipment>();
		recVehicle.add(new RecreationalEquipment(RecreationalEquipmentType.AllTerrainVehicle, "500", PAComprehensive_CollisionDeductible.Fifty50, "Brett Hiltbrand"));
		recVehicle.add(new RecreationalEquipment(RecreationalEquipmentType.Snowmobile, "55000", PAComprehensive_CollisionDeductible.Fifty50, "Test Automation"));
		recVehicle.add(new RecreationalEquipment(RecreationalEquipmentType.OffRoadMotorcycle, "16000", PAComprehensive_CollisionDeductible.Fifty50, "Test Automation"));

		SquireLiability liability = new SquireLiability();		
		ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
		imTypes.add(InlandMarine.FarmEquipment);
		imTypes.add(InlandMarine.RecreationalEquipment);

		ArrayList<Contact> driversList = new ArrayList<Contact>();
		Contact person = new Contact();
		driversList.add(person);
		ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
		Vehicle vehicle = new Vehicle(VehicleTypePL.PrivatePassenger, "make new vin", 2003, "Oldie", "But Goodie");
		vehicleList.add(vehicle);

		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow, MedicalLimit.TenK);
		coverages.setUnderinsured(false);

		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;
		myPropertyAndLiability.liabilitySection = liability;

		SquireInlandMarine myInlandMarine = new SquireInlandMarine();
		myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;
		myInlandMarine.recEquipment_PL_IM = recVehicle;
		myInlandMarine.farmEquipment = allFarmEquip;

		Squire mySquire = new Squire(SquireEligibility.Country);
		mySquire.squirePA = new SquirePersonalAuto();
		mySquire.propertyAndLiability = myPropertyAndLiability;
		mySquire.inlandMarine = myInlandMarine;

        mySQPolicyObjPL = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL, LineSelection.InlandMarineLinePL)
				.withInsFirstLastName("SQ", "Routing")
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}	

	@Test(dependsOnMethods = {"testIssueSquirePolChange"})
	private void testAutoOnlyChangeReason() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(mySQPolicyObjPL.agentInfo.getAgentUserName(), mySQPolicyObjPL.agentInfo.getAgentPassword(), mySQPolicyObjPL.squire.getPolicyNumber());
        Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("Auto Only change", currentSystemDate);
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPACoverages();
        GenericWorkorderSquireAutoCoverages coverages = new GenericWorkorderSquireAutoCoverages(driver);
		mySQPolicyObjPL.squire.squirePA.getCoverages().setLiability(LiabilityLimit.CSL100K);
		mySQPolicyObjPL.squire.squirePA.getCoverages().setMedical(MedicalLimit.TenK);
		coverages.fillOutSquireAutoCoverages_Coverages(mySQPolicyObjPL);
        coverages.clickCoverageExclusionsTab();
		coverages.fillOutSquireAutoCoverages_ExclusionsAndConditions(mySQPolicyObjPL);
		coverages.clickNext();
		GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
		qualificationPage.clickQuote();
        sideMenu.clickSideMenuPayment();
        GenericWorkorderPayment payments = new GenericWorkorderPayment(driver);
		payments.SubmitOnly();
        UWActivityPC activity = new UWActivityPC(driver);
		activity.setText("Testing Purpose");
		activity.setChangeReason(ChangeReason.AutoOnlyChange);
        activity.clickSendRequest();
        GenericWorkorderComplete complete = new GenericWorkorderComplete(driver);
		complete.clickAccountNumber();

        AccountSummaryPC summary = new AccountSummaryPC(driver);
		Underwriters uw = summary.getAssignedToUW("Submitted policy change");
		approveChanges(this.mySQPolicyObjPL, uw, mySQPolicyObjPL.squire.getPolicyNumber());
		new GuidewireHelpers(driver).logout();

	}

	private void approveChanges(GeneratePolicy pol, Underwriters uw, String policyNumber) throws Exception{
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), policyNumber);
        PolicySummary policySummary = new PolicySummary(driver);
		policySummary.clickActivity("Submitted policy change");
        StartPolicyChange change = new StartPolicyChange(driver);
		change.clickPolicyChangeNext();
        change.clickIssuePolicy();
		GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if(quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
            GenericWorkorderRiskAnalysis riskAnaysis = new GenericWorkorderRiskAnalysis(driver);
			riskAnaysis.approveAll();
			riskAnaysis.approveAll_IncludingSpecial();
			SideMenuPC sideMenuStuff = new SideMenuPC(driver);
			sideMenuStuff.clickSideMenuQuote();
		}

		change.clickIssuePolicy();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());

	}
	@Test(dependsOnMethods = {"testMultipleChangeReason"})
	private void testAnyOtherchangeReason() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(mySQPolicyObjPL.agentInfo.getAgentUserName(), mySQPolicyObjPL.agentInfo.getAgentPassword(), mySQPolicyObjPL.squire.getPolicyNumber());
        Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("Any Other change", currentSystemDate);
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);

		//add ANI
		PolicyInfoAdditionalNamedInsured pANI = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person, "Test"+StringsUtils.generateRandomNumberDigits(8), "Comp",
				AdditionalNamedInsuredType.Friend, new AddressInfo(true));
		pANI.setNewContact(CreateNew.Create_New_Always);			
		policyInfo.addAdditionalNamedInsured(mySQPolicyObjPL.basicSearch, pANI);

		sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers hmember = new GenericWorkorderPolicyMembers(driver);
		int row = hmember.getPolicyHouseholdMembersTableRow("Comp");
		hmember.clickPolicyHouseHoldTableCell(row, "Name");
        GenericWorkorderPolicyMembers householdMember1 = new GenericWorkorderPolicyMembers(driver);
		householdMember1.setDateOfBirth("01/01/1970");
		householdMember1.clickRelatedContactsTab();
		householdMember1.clickOK();

		GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
		qualificationPage.clickQuote();
        sideMenu.clickSideMenuPayment();
        GenericWorkorderPayment payments = new GenericWorkorderPayment(driver);
		payments.SubmitOnly();
        UWActivityPC activity = new UWActivityPC(driver);
		activity.setText("Testing Purpose");
		activity.setChangeReason(ChangeReason.AnyOtherChange);
		activity.clickSendRequest();

        GenericWorkorderComplete complete = new GenericWorkorderComplete(driver);
		complete.clickAccountNumber();

        AccountSummaryPC summary = new AccountSummaryPC(driver);
		Underwriters uw = summary.getAssignedToUW("Submitted policy change");
		approveChanges(this.mySQPolicyObjPL, uw, mySQPolicyObjPL.squire.getPolicyNumber());
		new GuidewireHelpers(driver).logout();
	}


	@Test(dependsOnMethods = {"testIssueSquirePolChange"})
	private void testMultipleChangeReason() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(mySQPolicyObjPL.agentInfo.getAgentUserName(), mySQPolicyObjPL.agentInfo.getAgentPassword(), mySQPolicyObjPL.squire.getPolicyNumber());
        Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("Multiple changes", currentSystemDate);
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);

		//add ANI
		PolicyInfoAdditionalNamedInsured pANI = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person, "Test"+StringsUtils.generateRandomNumberDigits(8), "Route",
				AdditionalNamedInsuredType.Friend, new AddressInfo(true));
		pANI.setNewContact(CreateNew.Create_New_Always);			
		policyInfo.addAdditionalNamedInsured(mySQPolicyObjPL.basicSearch, pANI);


		sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers hmember = new GenericWorkorderPolicyMembers(driver);
		int row = hmember.getPolicyHouseholdMembersTableRow("Route");
		hmember.clickPolicyHouseHoldTableCell(row, "Name");
        GenericWorkorderPolicyMembers householdMember1 = new GenericWorkorderPolicyMembers(driver);
		householdMember1.setDateOfBirth("01/01/1970");
		householdMember1.clickRelatedContactsTab();	
		householdMember1.clickOK();


		sideMenu.clickSideMenuPACoverages();
        GenericWorkorderSquireAutoCoverages coverages = new GenericWorkorderSquireAutoCoverages(driver);
		mySQPolicyObjPL.squire.squirePA.getCoverages().setLiability(LiabilityLimit.CSL100K);
		mySQPolicyObjPL.squire.squirePA.getCoverages().setMedical(MedicalLimit.TenK);
		coverages.fillOutSquireAutoCoverages_Coverages(mySQPolicyObjPL);
		coverages.fillOutSquireAutoCoverages_ExclusionsAndConditions(mySQPolicyObjPL);
		coverages.clickNext();

		//Property
		sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
		propertyLocations.clickEditLocation(1);
//        propertyLocations.setNumberOfResidence(12);
		propertyLocations.clickOK();


		sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetails = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
		PLPolicyLocationProperty property = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises);
		propertyDetails.fillOutPropertyDetails_FA(property);

        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);
		constructionPage.fillOutPropertyConstrustion_FA(property);
		constructionPage.setLargeShed(false);
        constructionPage.clickProtectionDetailsTab();

        new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails(driver).fillOutPropertyProtectionDetails(property);
		protectionPage.clickOK();	

		int BuildingNumber = propertyDetails.getSelectedBuildingNum();

		sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages prCoverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		prCoverages.clickSpecificBuilding(1, BuildingNumber);
		prCoverages.setCoverageALimit(200000);
		prCoverages.setCoverageAIncreasedReplacementCost(property.getPropertyCoverages().getCoverageA().isIncreasedReplacementCost());
		prCoverages.setCoverageCValuation(property.getPropertyCoverages());
		prCoverages.selectCoverageCCoverageType(CoverageType.BroadForm);	


		GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
		qualificationPage.clickQuote();
        sideMenu.clickSideMenuPayment();
        GenericWorkorderPayment payments = new GenericWorkorderPayment(driver);
		payments.SubmitOnly();
        UWActivityPC activity = new UWActivityPC(driver);
		activity.setText("Testing Purpose");
		activity.setChangeReason(ChangeReason.MultipleChanges);
		activity.clickSendRequest();

        GenericWorkorderComplete complete = new GenericWorkorderComplete(driver);
		complete.clickAccountNumber();

        AccountSummaryPC summary = new AccountSummaryPC(driver);
		Underwriters UW = summary.getAssignedToUW("Submitted policy change");
		approveChanges(this.mySQPolicyObjPL, UW, mySQPolicyObjPL.squire.getPolicyNumber());
		new GuidewireHelpers(driver).logout();
	}


}

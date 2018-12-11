package regression.r2.noclock.policycenter.change.subgroup6;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
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
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.CollisionLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.ComprehensiveLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.RentalReimbursementLimit;
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
import repository.gw.generate.custom.SectionIICoverages;
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
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchOtherJobsPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPolicyReviewPL;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_FarmEquipment;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_RecreationalEquipment;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_Watercraft;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages;
import repository.pc.workorders.generic.GenericWorkorderVehicles_CoverageDetails;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : US4781: PL - Preemptive Policy Change
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Aug 10, 2016
 */
public class TestSquirePreEmptedPolicyChange extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy mySQPolicyObjPL;
	private Underwriters uw;

	/** 
	 * @Author nvadlamudi
	 * @Description : Issue squire policy
	 * @DATE Aug 10, 2016
	 * @throws Exception
	 */
	@Test ()
	private void testIssueSquirePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);	

		// Watercraft
		WatercraftScheduledItems boat = new WatercraftScheduledItems(WatercraftTypes.BoatAndMotor, DateUtils.convertStringtoDate("01/01/1999", "MM/dd/yyyy"), 10000);		
		boat.setLimit(3123);
		boat.setItem(WatercratItems.Boat);
		boat.setLength(28);
		boat.setBoatType(BoatType.Outboard);

		ArrayList<WatercraftScheduledItems> boats = new ArrayList<WatercraftScheduledItems>();
		boats.add(boat);
		ArrayList<String> boatAddInsured = new ArrayList<String>();
		boatAddInsured.add("Cor Hofman");
		ArrayList<AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
		AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(
				ContactSubType.Person);
		loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);	


		SquireIMWatercraft boatType = new SquireIMWatercraft(WatercraftTypes.BoatAndMotor, PAComprehensive_CollisionDeductible.OneHundred100, boats);
		boatType.setAdditionalInterest(loc2Bldg1AdditionalInterests);
		ArrayList<SquireIMWatercraft> boatTypes = new ArrayList<SquireIMWatercraft>();
		boatTypes.add(boatType);


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

		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow, MedicalLimit.TenK);
		coverages.setUnderinsured(false);

		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);
		squirePersonalAuto.setVehicleList(vehicleList);
		squirePersonalAuto.setDriversList(driversList);

		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;
		myPropertyAndLiability.liabilitySection = liability;

		SquireInlandMarine myInlandMarine = new SquireInlandMarine();
		myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;
		myInlandMarine.recEquipment_PL_IM = recVehicle;
		myInlandMarine.watercrafts_PL_IM = boatTypes;
		myInlandMarine.farmEquipment = allFarmEquip;


		Squire mySquire = new Squire(SquireEligibility.Country);
		mySquire.squirePA = squirePersonalAuto;
		mySquire.propertyAndLiability = myPropertyAndLiability;
		mySquire.inlandMarine = myInlandMarine;

        mySQPolicyObjPL = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL, LineSelection.InlandMarineLinePL)
				.withInsFirstLastName("SQ", "Change")
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);		  
	}

	@Test (dependsOnMethods = {"testIssueSquirePolicy"})
	private void testQuoteTwoDifferentPolChanges() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		SideMenuPC sideMenu = new SideMenuPC(driver);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySQPolicyObjPL.squire.getPolicyNumber());

		//First Change
		Date firstChangeDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 20);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First Policy Change",firstChangeDate);

		//Auto coverage canges 
		addAutoVehicle(mySQPolicyObjPL.pniContact.getAddress().getCity(), VehicleTypePL.PassengerPickup,ComprehensiveLimit.OneHundred,CollisionLimit.TwoFifty, RentalReimbursementLimit.TwentyFive);	

		//Recreational Vehicle Changes
		sideMenu.clickSideMenuIMRecreationVehicle();
        RecreationalEquipment recEquip = new RecreationalEquipment(RecreationalEquipmentType.OffRoadMotorcycle, "16000", PAComprehensive_CollisionDeductible.OneHundred100, "Test Automation");
		GenericWorkorderSquireInlandMarine_RecreationalEquipment recEquipPage = new GenericWorkorderSquireInlandMarine_RecreationalEquipment(driver);
		recEquipPage.recEquip(recEquip);

		try{
			sideMenu.clickSideMenuSquirePropertyCoverages();
		}catch(Exception e){
			sideMenu.clickSideMenuSquirePropertyCoverages();
		}

		GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
		coverages.clickSectionIICoveragesTab();
		GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages sectionII = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
		SectionIICoverages sectionIICoverages = new SectionIICoverages(SectionIICoveragesEnum.OffRoadMotorcycles, 0, 1);
		sectionII.addCoverages(sectionIICoverages);
		sectionII.setQuantity(sectionIICoverages);

		//Section II Coverages
		sectionIICoverages = new SectionIICoverages(SectionIICoveragesEnum.HorseBoarding, 0, 2);
		sectionII.addCoverages(sectionIICoverages);
		sectionII.setQuantity(sectionIICoverages);

		//Farm Equipment changes
		sideMenu.clickSideMenuIMFarmEquipment();
        IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment", "Manly Farm Equipment", 1000);
		ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
		farmEquip.add(farmThing);
		FarmEquipment imFarmEquip = new FarmEquipment(IMFarmEquipmentType.CircleSprinkler, CoverageType.BroadForm, IMFarmEquipmentDeductible.TwoHundredFifty, true, false, "Cor Hofman", farmEquip);

		GenericWorkorderSquireInlandMarine_FarmEquipment farmEquipment = new GenericWorkorderSquireInlandMarine_FarmEquipment(driver);
		farmEquipment.addFarmEquip(mySQPolicyObjPL.basicSearch, imFarmEquip);


		//Watercraft changes 
		sideMenu.clickSideMenuIMWatercraft();
        WatercraftScheduledItems personalWater = new WatercraftScheduledItems(WatercraftTypes.PersonalWatercraft, DateUtils.convertStringtoDate("01/01/1999", "MM/dd/yyyy"), 16000);
		personalWater.setLimit(18000);
		personalWater.setItem(WatercratItems.PersonalWatercraft);
		ArrayList<WatercraftScheduledItems> personalWaterCrafts = new ArrayList<WatercraftScheduledItems>();
		personalWaterCrafts.add(personalWater);	
		SquireIMWatercraft waterCrafts = new SquireIMWatercraft(WatercraftTypes.PersonalWatercraft, PAComprehensive_CollisionDeductible.FiveHundred500, personalWaterCrafts);
		GenericWorkorderSquireInlandMarine_Watercraft watercraftPage = new GenericWorkorderSquireInlandMarine_Watercraft(driver);

		watercraftPage.watercraft(waterCrafts);

		GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);

		try{
			sideMenu.clickSideMenuSquirePropertyCoverages();
		}catch(Exception e){
			sideMenu.clickSideMenuSquirePropertyCoverages();
		}
		coverages.clickSectionIICoveragesTab();	
		SectionIICoverages myCoverages = new SectionIICoverages(SectionIICoveragesEnum.PersonalWatercraft, 0, 2);
		sectionII.addCoverages(myCoverages);
		sectionII.setQuantity(myCoverages);

		//Quoting
		sideMenu.clickSideMenuRiskAnalysis();
		qualificationPage.clickQuote();



	}

	@Test(dependsOnMethods = {"testQuoteTwoDifferentPolChanges"})
	private void testIssueInitialPolChange() throws Exception {

		//Second Change	  
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		SideMenuPC sideMenu = new SideMenuPC(driver);
		new Login(driver).loginAndSearchPolicyByPolicyNumber("pdye", mySQPolicyObjPL.agentInfo.getAgentPassword(), mySQPolicyObjPL.squire.getPolicyNumber());

        StartPolicyChange newChangePage = new StartPolicyChange(driver);
		Date secondChangeDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 10);
		newChangePage.startPolicyChange("Second Policy Change",secondChangeDate);

		//Auto coverage canges 
		addAutoVehicle(mySQPolicyObjPL.pniContact.getAddress().getCity(), VehicleTypePL.PassengerPickup,ComprehensiveLimit.FiveHundred,CollisionLimit.FiveHundred, RentalReimbursementLimit.Fifty);	

		//Recreational Vehicle Changes
		sideMenu.clickSideMenuIMRecreationVehicle();
        RecreationalEquipment latestrecEquip = new RecreationalEquipment(RecreationalEquipmentType.OffRoadMotorcycle, "6000", PAComprehensive_CollisionDeductible.TwentyFive25, "Test Automation");
		GenericWorkorderSquireInlandMarine_RecreationalEquipment recEquipPage = new GenericWorkorderSquireInlandMarine_RecreationalEquipment(driver);
		recEquipPage.recEquip(latestrecEquip);
		try{
			sideMenu.clickSideMenuSquirePropertyCoverages();
		}catch(Exception e){
			sideMenu.clickSideMenuSquirePropertyCoverages();
		}
		GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
		coverages.clickSectionIICoveragesTab();
		GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages sectionII = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
		
		SectionIICoverages myCoverages = new SectionIICoverages(SectionIICoveragesEnum.OffRoadMotorcycles, 0, 2);
		
		sectionII.addCoverages(myCoverages);
		sectionII.setQuantity(myCoverages);

		//Section II coverages
		myCoverages = new SectionIICoverages(SectionIICoveragesEnum.HorseBoarding, 0, 8);
		sectionII.addCoverages(myCoverages);
		sectionII.setQuantity(myCoverages);

		//FarmEquipment
		sideMenu.clickSideMenuIMFarmEquipment();
        IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment", "Manly Farm Equipment", 1000);
		ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
		farmEquip.add(farmThing);
		FarmEquipment newImFarmEquip = new FarmEquipment(IMFarmEquipmentType.CircleSprinkler, CoverageType.BroadForm, IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
		GenericWorkorderSquireInlandMarine_FarmEquipment farmEquipment = new GenericWorkorderSquireInlandMarine_FarmEquipment(driver);

		farmEquipment.addFarmEquip(mySQPolicyObjPL.basicSearch, newImFarmEquip);

		//Watercraft
		sideMenu.clickSideMenuIMWatercraft();
        WatercraftScheduledItems personalWater = new WatercraftScheduledItems(WatercraftTypes.PersonalWatercraft, DateUtils.convertStringtoDate("01/01/1999", "MM/dd/yyyy"), 16000);
		personalWater.setLimit(18000);
		personalWater.setItem(WatercratItems.PersonalWatercraft);
		ArrayList<WatercraftScheduledItems> personalWaterCrafts = new ArrayList<WatercraftScheduledItems>();
		personalWaterCrafts.add(personalWater);	
		SquireIMWatercraft latestWaterCrafts = new SquireIMWatercraft(WatercraftTypes.PersonalWatercraft, PAComprehensive_CollisionDeductible.OneHundred100, personalWaterCrafts);
		GenericWorkorderSquireInlandMarine_Watercraft watercraftPage = new GenericWorkorderSquireInlandMarine_Watercraft(driver);
		watercraftPage.watercraft(latestWaterCrafts); 
		try{
			sideMenu.clickSideMenuSquirePropertyCoverages();
		}catch(Exception e){
			sideMenu.clickSideMenuSquirePropertyCoverages();
		}
		coverages.clickSectionIICoveragesTab();	
		myCoverages = new SectionIICoverages(SectionIICoveragesEnum.PersonalWatercraft, 0, 2);
		sectionII.addCoverages(myCoverages);
		sectionII.setQuantity(myCoverages);

		//Quoting
		sideMenu.clickSideMenuRiskAnalysis();
		GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
		qualificationPage.clickQuote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if(quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
            GenericWorkorderRiskAnalysis_UWIssues riskAnaysis = new GenericWorkorderRiskAnalysis_UWIssues(driver);
			riskAnaysis.handleBlockSubmit(mySQPolicyObjPL);
			sideMenu.clickSideMenuQuote();
		}
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
		riskAnalysis.approveAll_IncludingSpecial();
        sideMenu.clickSideMenuQuote();

        StartPolicyChange change = new StartPolicyChange(driver);
		change.clickIssuePolicy();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
    }

	@Test(dependsOnMethods = {"testIssueInitialPolChange"})
	private void testValidatePreEmptedPolChange() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySQPolicyObjPL.accountNumber);

		SearchOtherJobsPC jobSearchPC = new SearchOtherJobsPC(driver);
		jobSearchPC.searchJobByAccountNumber(mySQPolicyObjPL.accountNumber, "003");
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuRiskAnalysis();

        GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
		String valMessage = "You will need to handle these preemptions before continuing";
		String errorMessage = "";
		if(!riskAnalysis.getWarningMessage().contains(valMessage)){
			errorMessage = errorMessage + "Expected Warning message : "+ valMessage + " is not displayed.\n";
		}

		sideMenu.clickSideMenuPolicyChangeReview();
        riskAnalysis.clickHandlePreemption();
		GenericWorkorderPolicyReviewPL polReview = new GenericWorkorderPolicyReviewPL(driver);
		polReview.clickApplyAllChanges();

		GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
		qualificationPage.clickQuote();


		String validationMessages = riskAnalysis.getValidationMessagesText();
		String valMeg1 = "Rental Reimbursement limits on all vehicles must be same";
		if(!validationMessages.contains(valMeg1)) {
			errorMessage = errorMessage + "Expected Validation message : '" + valMeg1 + "' is not displayed.";
		}

		String valMeg2 = "Collision Deductible on all vehicles must be same";
		if(!validationMessages.contains(valMeg2)) {
			errorMessage = errorMessage + "Expected Validation message : '" + valMeg2 + "' is not displayed.";
		}

		String valMeg3 = "Comprehensive Deductible on all vehicles must be same";
		if(!validationMessages.contains(valMeg3)) {
			errorMessage = errorMessage + "Expected Validation message : '" + valMeg3 + "' is not displayed.";
		}

		String valMeg4 = "All Circle Sprinkler, Off-road Motorcycle, and Personal Watercraft must have the same deductible as others of the same type";
		if(!validationMessages.contains(valMeg4)) {
			errorMessage = errorMessage + "Expected Validation message : '" + valMeg4 + "' is not displayed.";
		}

		String valMeg5 = "Coverage Type is required";
		if(!validationMessages.contains(valMeg5)) {
			errorMessage = errorMessage + "Expected Validation message : '" + valMeg5 + "' is not displayed.";
		}

		String valMeg6 = "The number of Off Road Motorcycles specified on Section IV must be equal to or less than the number of Off Road Motorcycles specified on Section II";
		if(!validationMessages.contains(valMeg6)) {
			errorMessage = errorMessage + "Expected Validation message : '" + valMeg6 + "' is not displayed.";
		}
		riskAnalysis.clickClearButton();

		if(errorMessage != "")
			Assert.fail(errorMessage);
	}

	private void addAutoVehicle(String addressCity, VehicleTypePL vehicleType, ComprehensiveLimit comprehensive, CollisionLimit collision, RentalReimbursementLimit rental) throws Exception{
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPAVehicles();

        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
		GenericWorkorderVehicles_CoverageDetails vehicleCoverages = new GenericWorkorderVehicles_CoverageDetails(driver);
		vehiclePage.createVehicleManually();
		vehiclePage.createGenericVehicle(vehicleType);
		vehiclePage.selectGaragedAtZip(addressCity);
		vehiclePage.addDriver(this.mySQPolicyObjPL.squire.squirePA.getDriversList().get(0));
		vehiclePage.setFactoryCostNew(1000);
		vehicleCoverages.selectVehicleCoverageDetailsTab();
        vehicleCoverages.setComprehensiveDeductible(comprehensive.getValue());
        vehicleCoverages.setCollision(true);
		vehicleCoverages.setCollisionDeductible(collision.getValue());
		vehicleCoverages.setRentalReimbursementDeductible(rental.getValue());
		vehicleCoverages.clickOK(); 
	}  
}

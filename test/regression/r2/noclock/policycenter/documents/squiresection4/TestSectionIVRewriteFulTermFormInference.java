package regression.r2.noclock.policycenter.documents.squiresection4;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DocFormEvents;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.BoatType;
import repository.gw.enums.InlandMarineTypes.CargoClass;
import repository.gw.enums.InlandMarineTypes.ClassIIICargoTypes;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.InlandMarineTypes.RecreationalEquipmentType;
import repository.gw.enums.InlandMarineTypes.WatercraftTypes;
import repository.gw.enums.InlandMarineTypes.WatercratItems;
import repository.gw.enums.LineSelection;
import repository.gw.enums.LivestockDeductible;
import repository.gw.enums.LivestockScheduledItemType;
import repository.gw.enums.LivestockType;
import repository.gw.enums.PersonalPropertyDeductible;
import repository.gw.enums.PersonalPropertyScheduledItemType;
import repository.gw.enums.PersonalPropertyType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.Vehicle.PAComprehensive_CollisionDeductible;
import repository.gw.enums.Vehicle.VehicleType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.ActualExpectedDocumentDifferences;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Cargo;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.Livestock;
import repository.gw.generate.custom.LivestockList;
import repository.gw.generate.custom.LivestockScheduledItem;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PersonalProperty;
import repository.gw.generate.custom.PersonalPropertyList;
import repository.gw.generate.custom.PersonalPropertyScheduledItem;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.RecreationalEquipment;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireIMCargo;
import repository.gw.generate.custom.SquireIMWatercraft;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquireLiablityCoverageLivestockItem;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.WatercraftScheduledItems;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.DocFormUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyDocuments;
import repository.pc.policy.PolicyMenu;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderForms;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireIMExclusionsConditions;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_FarmEquipment;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_PersonalProperty;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.TownshipRange;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.TownshipRangeHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import persistence.globaldatarepo.helpers.VINHelper;

/**
 * @Author skandibanda
 * @Requirement : US10152: QA - Stabilization - Automation Forms Testing,US9327 - [Part III] Re-work inference for all forms, jobs and policies
 * @Description -
 * @DATE April 17, 2017
 */
@QuarantineClass
public class TestSectionIVRewriteFulTermFormInference extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy squireIMPolObj;
	private ArrayList<DocFormEvents.PolicyCenter> eventsHitDuringSubmissionCreationPlusLocal = new ArrayList<DocFormEvents.PolicyCenter>();
	private ArrayList<String> formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = new ArrayList<String>();
	private ArrayList<String> formsOrDocsActualFromUISubmissionPlusLocal = new ArrayList<String>();
	private ActualExpectedDocumentDifferences actualExpectedDocDifferencesSubmissionPlusLocal = new ActualExpectedDocumentDifferences();
	private Underwriters uw;
	
	@Test
	public void testGenerateSquireIssuance() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		Vin vin = VINHelper.getRandomVIN();
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

		// Cargo
		Cargo cargoTrailer1 = new Cargo(VehicleType.Trailer, vin.getVin(), Integer.parseInt(vin.getYear()), vin.getMake(), vin.getModel());
		cargoTrailer1.setLimit("1000");
		ArrayList<Cargo> cargoTrailers = new ArrayList<Cargo>();
		cargoTrailers.add(cargoTrailer1);
		ArrayList<String> cargoAddInsured = new ArrayList<String>();
		cargoAddInsured.add("Cor Hofman");
		SquireIMCargo stuff = new SquireIMCargo(CargoClass.ClassIII, ClassIIICargoTypes.Milk, 100, 1000, "Brenda Swindle", false, cargoTrailers, cargoAddInsured);
		SquireIMCargo cargo = new SquireIMCargo(CargoClass.ClassI, 100, 1000, "Brenda Swindle", false, cargoTrailers);
		ArrayList<SquireIMCargo> cargoList = new ArrayList<SquireIMCargo>();
		cargoList.add(cargo);
		cargoList.add(stuff);

		// Farm Equipment
		IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment", "Manly Farm Equipment", 1000);
		ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
		farmEquip.add(farmThing);
		FarmEquipment imFarmEquip = new FarmEquipment(IMFarmEquipmentType.FarmEquipment, CoverageType.SpecialForm, IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
		ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
		allFarmEquip.add(imFarmEquip);

		// Rec Equip
		ArrayList<RecreationalEquipment> recVehicle = new ArrayList<RecreationalEquipment>();
		recVehicle.add(new RecreationalEquipment(RecreationalEquipmentType.AllTerrainVehicle, "500", PAComprehensive_CollisionDeductible.Fifty50, "Brett Hiltbrand"));

		// PersonalProperty
		PersonalProperty pprop = new PersonalProperty();
		pprop.setType(PersonalPropertyType.MedicalSuppliesAndEquipment);
		pprop.setYear(2010);
		pprop.setMake("Testmake");
		pprop.setModel("Testmodel");
		pprop.setVinSerialNum("123123123");
		pprop.setDescription("Testdescription");
		pprop.setLimit(1000);
		pprop.setDeductible(PersonalPropertyDeductible.Ded25);
		ArrayList<String> ppropAdditIns = new ArrayList<String>();
		ppropAdditIns.add("First Guy");
		ppropAdditIns.add("Second Guy");
		pprop.setAdditionalInsureds(ppropAdditIns);
		PersonalProperty pprop2 = new PersonalProperty();
		pprop2.setType(PersonalPropertyType.MedicalSuppliesAndEquipment);
		pprop2.setYear(2011);
		pprop2.setMake("Testmake2");
		pprop2.setModel("Testmodel2");
		pprop2.setVinSerialNum("456456456");
		pprop2.setDescription("Testdescription2");
		pprop2.setLimit(1000);
		pprop2.setDeductible(PersonalPropertyDeductible.Ded25);
		ArrayList<String> pprop2AdditIns = new ArrayList<String>();
		pprop2AdditIns.add("First Guy2");
		pprop2AdditIns.add("Second Guy2");
		pprop2.setAdditionalInsureds(pprop2AdditIns);
		PersonalProperty pprop3 = new PersonalProperty();
		pprop3.setType(PersonalPropertyType.MilkContaminationAndRefrigeration);
		pprop3.setDescription("Testdescription2");
		pprop3.setLimit(1000);
		PersonalProperty pprop4 = new PersonalProperty();
		pprop4.setType(PersonalPropertyType.RefrigeratedMilk);
		pprop4.setDescription("Testdescription2");
		pprop4.setLimit(2000);
		PersonalProperty pprop5 = new PersonalProperty();
		pprop5.setType(PersonalPropertyType.BeeContainers);
		pprop5.setDeductible(PersonalPropertyDeductible.Ded250);
		pprop5.setLimit(25);
		PersonalPropertyScheduledItem beeScheduledItem = new PersonalPropertyScheduledItem();
		beeScheduledItem.setParentPersonalPropertyType(PersonalPropertyType.BeeContainers);
		beeScheduledItem.setLimit(1000);
		beeScheduledItem.setNumber(500);
		beeScheduledItem.setDescription("Bee Yourself");
		ArrayList<PersonalPropertyScheduledItem> beeScheduledItems = new ArrayList<PersonalPropertyScheduledItem>();
		beeScheduledItems.add(beeScheduledItem);
		pprop5.setScheduledItems(beeScheduledItems);
		PersonalProperty pprop6 = new PersonalProperty();
		pprop6.setType(PersonalPropertyType.SportingEquipment);
		pprop6.setLimit(1000);
		pprop6.setDeductible(PersonalPropertyDeductible.Ded1000);
		PersonalPropertyScheduledItem sportsScheduledItem = new PersonalPropertyScheduledItem();
		sportsScheduledItem.setParentPersonalPropertyType(PersonalPropertyType.SportingEquipment);
		sportsScheduledItem.setLimit(1000);
		sportsScheduledItem.setDescription("Sports Stuff");
		sportsScheduledItem.setType(PersonalPropertyScheduledItemType.Guns);
		sportsScheduledItem.setMake("Honda");
		sportsScheduledItem.setModel("Accord");
		sportsScheduledItem.setYear(2015);
		sportsScheduledItem.setVinSerialNum("abcd12345");
		ArrayList<PersonalPropertyScheduledItem> sportsScheduledItems = new ArrayList<PersonalPropertyScheduledItem>();
		sportsScheduledItems.add(sportsScheduledItem);
		pprop6.setScheduledItems(sportsScheduledItems);
		PersonalProperty pprop7 = new PersonalProperty();
		pprop7.setType(PersonalPropertyType.GolfEquipment);
		pprop7.setLimit(1000);
		pprop7.setDeductible(PersonalPropertyDeductible.Ded1000);
		PersonalPropertyScheduledItem golfScheduledItem = new PersonalPropertyScheduledItem();
		golfScheduledItem.setParentPersonalPropertyType(PersonalPropertyType.GolfEquipment);
		golfScheduledItem.setLimit(1000);
		golfScheduledItem.setDescription("Golf Bag and Stuff");
		ArrayList<PersonalPropertyScheduledItem> golfScheduledItems = new ArrayList<PersonalPropertyScheduledItem>();
		golfScheduledItems.add(golfScheduledItem);
		pprop7.setScheduledItems(golfScheduledItems);
		PersonalProperty pprop8 = new PersonalProperty();
		pprop8.setType(PersonalPropertyType.BlanketRadios);
		pprop8.setDeductible(PersonalPropertyDeductible.Ded100);
		pprop8.setLimit(2000);
		PersonalPropertyScheduledItem blanketRadioScheduledItem = new PersonalPropertyScheduledItem();
		blanketRadioScheduledItem.setParentPersonalPropertyType(PersonalPropertyType.BlanketRadios);
		blanketRadioScheduledItem.setLimit(1000);
		blanketRadioScheduledItem.setType(PersonalPropertyScheduledItemType.Portable);
		blanketRadioScheduledItem.setDescription("Radio on Blanket");
		blanketRadioScheduledItem.setNumber(4);
		ArrayList<PersonalPropertyScheduledItem> blanketRadioScheduledItems = new ArrayList<PersonalPropertyScheduledItem>();
		blanketRadioScheduledItems.add(blanketRadioScheduledItem);
		pprop8.setScheduledItems(blanketRadioScheduledItems);
		PersonalPropertyList ppropList = new PersonalPropertyList();
		ArrayList<PersonalProperty> msaeList = new ArrayList<PersonalProperty>();
		msaeList.add(pprop);
		msaeList.add(pprop2);
		ppropList.setMedicalSuppliesAndEquipment(msaeList);
		ppropList.setRefrigeratedMilk(pprop4);
		ppropList.setMilkContaminationAndRefrigeration(pprop3);
		ppropList.setBeeContainers(pprop5);
		ppropList.setSportingEquipment(pprop6);
		ppropList.setGolfEquipment(pprop7);
		ppropList.setBlanketRadios(pprop8);

		// Livestock
		Livestock livestock = new Livestock();
		livestock.setType(LivestockType.Livestock);
		livestock.setDeductible(LivestockDeductible.Ded100);
		LivestockScheduledItem livSchedItem1 = new LivestockScheduledItem(livestock.getType(), LivestockScheduledItemType.Alpaca, "Test Alpaca 1", 1000);
		LivestockScheduledItem livSchedItem2 = new LivestockScheduledItem(livestock.getType(), LivestockScheduledItemType.Cow, "Test BullCow 1", "TAG123", "Brand1", "Breed1", 5000);
		ArrayList<String> livSchedItem2AdditInsureds = new ArrayList<String>();
		livSchedItem2AdditInsureds.add("Sched Item Guy1");
		livSchedItem2AdditInsureds.add("Sched Item Guy2");
		livSchedItem2.setAdditionalInsureds(livSchedItem2AdditInsureds);
		ArrayList<LivestockScheduledItem> livSchedItems = new ArrayList<LivestockScheduledItem>();
		livSchedItems.add(livSchedItem1);
		livSchedItems.add(livSchedItem2);
		livestock.setScheduledItems(livSchedItems);
		ArrayList<String> livAdditInsureds = new ArrayList<String>();
		livAdditInsureds.add("Cor Hofman");
		livAdditInsureds.add("Rusty Young");
		livestock.setAdditionalInsureds(livAdditInsureds);
		Livestock deathOfLivestock = new Livestock();
		deathOfLivestock.setType(LivestockType.DeathOfLivestock);
		LivestockScheduledItem dolSchedItem1 = new LivestockScheduledItem(deathOfLivestock.getType(), LivestockScheduledItemType.SheepD, 500, "Test Sheep Hogs");
		LivestockScheduledItem dolSchedItem2 = new LivestockScheduledItem(deathOfLivestock.getType(), LivestockScheduledItemType.Other, 20, "Test Other");
		ArrayList<LivestockScheduledItem> dolSchedItems = new ArrayList<LivestockScheduledItem>();
		dolSchedItems.add(dolSchedItem1);
		dolSchedItems.add(dolSchedItem2);
		deathOfLivestock.setScheduledItems(dolSchedItems);
		ArrayList<String> dolAdditInsureds = new ArrayList<String>();
		dolAdditInsureds.add("Cor Hofman");
		dolAdditInsureds.add("Rusty Young");
		deathOfLivestock.setAdditionalInsureds(dolAdditInsureds);
		LivestockList allLivestock = new LivestockList(livestock, deathOfLivestock, null);

		// Watercraft
		ArrayList<AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
		AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(
				ContactSubType.Person);
		loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);	
		WatercraftScheduledItems boat = new WatercraftScheduledItems(WatercraftTypes.BoatAndMotor, DateUtils.convertStringtoDate("01/01/1999", "MM/dd/yyyy"), 10000);
		boat.setLimit(3123);
		boat.setItem(WatercratItems.Boat);
		boat.setLength(28);
		boat.setBoatType(BoatType.Outboard);
		ArrayList<WatercraftScheduledItems> boats = new ArrayList<WatercraftScheduledItems>();
		boats.add(boat);
		ArrayList<String> boatAddInsured = new ArrayList<String>();
		boatAddInsured.add("Cor Hofman");
		SquireIMWatercraft boatType = new SquireIMWatercraft(WatercraftTypes.BoatAndMotor, PAComprehensive_CollisionDeductible.OneHundred100, boats);
		boatType.setAdditionalInterest(loc2Bldg1AdditionalInterests);
		ArrayList<SquireIMWatercraft> boatTypes = new ArrayList<SquireIMWatercraft>();
		boatTypes.add(boatType);

		SquireLiability liability = new SquireLiability();
		
		ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
		imTypes.add(InlandMarine.Cargo);
		imTypes.add(InlandMarine.FarmEquipment);
		imTypes.add(InlandMarine.RecreationalEquipment);
		imTypes.add(InlandMarine.PersonalProperty);
		imTypes.add(InlandMarine.Livestock);
		imTypes.add(InlandMarine.Watercraft);



        SquireLiablityCoverageLivestockItem livestockSectionIIHorseCoverage = new SquireLiablityCoverageLivestockItem();
        livestockSectionIIHorseCoverage.setQuantity(14);
        livestockSectionIIHorseCoverage.setType(LivestockScheduledItemType.Horse);

        ArrayList<SquireLiablityCoverageLivestockItem> coveredLivestockItems = new ArrayList<SquireLiablityCoverageLivestockItem>();
        coveredLivestockItems.add(livestockSectionIIHorseCoverage);
        SectionIICoverages livestockcoverage = new SectionIICoverages(SectionIICoveragesEnum.Livestock, coveredLivestockItems);
        
        liability.getSectionIICoverageList().add(livestockcoverage);
		
		
        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liability;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;
        myInlandMarine.watercrafts_PL_IM = boatTypes;
        myInlandMarine.cargo_PL_IM = cargoList;
        myInlandMarine.recEquipment_PL_IM = recVehicle;
        myInlandMarine.farmEquipment = allFarmEquip;
        myInlandMarine.personalProperty_PL_IM = ppropList.getAllPersonalPropertyAsList();
        myInlandMarine.livestock_PL_IM = allLivestock.getAllLivestockAsList();

        Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;

        squireIMPolObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withSquireEligibility(SquireEligibility.FarmAndRanch)
                .withLineSelection(LineSelection.InlandMarineLinePL, LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
				.withInsFirstLastName("IM", "Forms")
				.build(GeneratePolicyType.PolicyIssued);

	}
	
	@Test (dependsOnMethods = { "testGenerateSquireIssuance" })
	public void testSectionIVRewriteFullTermForms() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), squireIMPolObj.squire.getPolicyNumber());
        StartCancellation cancelPol = new StartCancellation(driver);
		Date currentDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);

		String comment = "For Rewrite full term of the policy";
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.Rewritten, comment, currentDate, true);
		driver.quit();

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), squireIMPolObj.squire.getPolicyNumber());

        PolicyMenu policyMenu = new PolicyMenu(driver);
		policyMenu.clickMenuActions();
		policyMenu.clickRewriteFullTerm();

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
		propertyLocations.clickEditLocation(1);
		ArrayList<String> sections = new ArrayList<String>();
		sections.add("01");
		sections.add("02");
		sections.add("03");
		sections.add("04");
		sections.add("05");
		sections.add("06");
			
		TownshipRange townshipRange = TownshipRangeHelper.getRandomTownshipRangeForCounty(squireIMPolObj.squire.propertyAndLiability.locationList.get(0).getAddress().getCounty());
		propertyLocations.addSection(sections);

//		propertyLocations.selectTownshipNumber(townshipRange.getTownship());
//		propertyLocations.selectTownshipDirection(townshipRange.getTownshipDirection());
//		propertyLocations.selectRangeNumber(townshipRange.getRange());
//		propertyLocations.selectRangeDirection(townshipRange.getRangeDirection());
		propertyLocations.clickOK();
		
		sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
		coverages.clickFarmPersonalProperty();
		coverages.clickSectionIICoveragesTab();
		coverages.clickCoveragesExclusionsAndConditions();
		sideMenu.clickSideMenuIMCoveragePartSelection();
		sideMenu.clickSideMenuIMRecreationVehicle();
		
		//Changing FarmEquipment
		sideMenu.clickSideMenuIMFarmEquipment();
        GenericWorkorderSquireInlandMarine_FarmEquipment farmEquipment = new GenericWorkorderSquireInlandMarine_FarmEquipment(driver);
		
		farmEquipment.clickAdd();
        farmEquipment.setType(IMFarmEquipmentType.CircleSprinkler);
        farmEquipment.setCoverageType(CoverageType.SpecialForm);
        farmEquipment.setDeductible(IMFarmEquipmentDeductible.Hundred);
        farmEquipment.setInspected(true);
		farmEquipment.setExistingDamage(false);
        AddressInfo bankAddress = new AddressInfo();
		AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Company);		
		loc2Bldg1AddInterest.setAddress(bankAddress);		
		loc2Bldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LessorPL);

        GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
		additionalInterests.clickBuildingsPropertyAdditionalInterestsSearch();
        SearchAddressBookPC search = new SearchAddressBookPC(driver);
		search.searchAddressBookByCompanyName(squireIMPolObj.basicSearch, loc2Bldg1AddInterest.getCompanyName(), null, null, State.Idaho, null, CreateNew.Do_Not_Create_New);
        additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType(loc2Bldg1AddInterest.getAdditionalInterestType().getValue());
		additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();

		IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment", "Manly Farm Equipment", 1000);
        farmEquipment.setScheduledItem(farmThing);

		farmEquipment.setLocation("1: 01, 02, 03, 04, 05, 06, " +townshipRange.getTownship()+townshipRange.getTownshipDirection()+", "+townshipRange.getRange()+townshipRange.getRangeDirection());

        farmEquipment.addFarmEquipmentScheduledItemExistingAdditionalInterest(loc2Bldg1AddInterest.getPersonFirstName(), AdditionalInterestType.LessorPL);
        farmEquipment.addScheduledItemAdditionalInsured("First Guy");
		farmEquipment.clickOk();
		
		farmEquipment.clickAdd();
        farmEquipment.setType(IMFarmEquipmentType.WheelSprinkler);
        farmEquipment.setDeductible(IMFarmEquipmentDeductible.Hundred);
        farmEquipment.setInspected(true);
		farmEquipment.setExistingDamage(false);
        IMFarmEquipmentScheduledItem farmThing2 = new IMFarmEquipmentScheduledItem("Farm Equipment", "Manly Farm Equipment", 1000);
        farmEquipment.setScheduledItem(farmThing2);
		farmEquipment.clickOk();
		

		//Add Personal Property Radio Receivers and Transmitters
		sideMenu.clickSideMenuIMPersonalEquipment();
        GenericWorkorderSquireInlandMarine_PersonalProperty personalProperty = new GenericWorkorderSquireInlandMarine_PersonalProperty(driver);
        personalProperty.clickAdd();
        personalProperty.setType(PersonalPropertyType.RadioReceiversAndTransmitters);
        personalProperty.setDeductible(PersonalPropertyDeductible.Ded250);

		personalProperty.clickAddScheduledItems();		
		personalProperty.setScheduledItemDescription("Sports Stuff");
        personalProperty.setScheduledItemMake("Honda");
        personalProperty.setScheduledItemModel("Accord");
        personalProperty.setScheduledItemYear(2015);
        personalProperty.setScheduledItemVinSerialNum("abcd12345");
		personalProperty.setScheduledItemLimit(1000);
		personalProperty.clickOk();		
		
		//Special Endorsement for Inland Marine 405
		sideMenu.clickSideMenuIMExclusionsAndConditions();
        GenericWorkorderSquireIMExclusionsConditions exclusionsConditions = new GenericWorkorderSquireIMExclusionsConditions(driver);
		exclusionsConditions.addSpecialEndorsementForIM405("Testing purpose added during Automation");
		
		sideMenu.clickSideMenuLineSelection();
        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
		for (int i = 0; i < 24; i++) {
			qualificationPage.clickQualificationNext();
        }
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_IM_AddBeeContainers);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_IM_AddBlanketRadios);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_IM_AddCargoClassIII);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_IM_AddCargoClassIorII);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_IM_AddDeathOfLivestock);
		//this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_IM_AddIMScheduledItemLessor);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_IM_AddLivestock);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_IM_AddOutsidePersonalProperty);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_IM_AddPersonalPropertyMilkContamination);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_IM_AddRefrigeratedMilk);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_IM_AddSportingEquipment);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_IM_AddWatercraft);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_IM_FarmEquipmentAddInsured);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_IM_FarmEquipmentBroad);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_IM_FarmEquipmentSpecial);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_IM_RecEquipmentOrWheelchair);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_IM_SpecialEndorsementForIM405);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_IM_IrrigationEquipmentEmdorsement);		
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_FarmRanchPolicyDeclarations);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_FarmRanchSquirePolicyBooklet);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_NoticeOfPolicyOnPrivacy);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_AdditionalInterestDeclarations);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_IM_AddPersonalPropertyMilkContamination);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_Liab__RecreationalVehicleCertificateOfLiability);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_Liab_WatercraftEndorsement);		
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_Liab_RawMilkExclusionEndorsement);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdIM_FarmEquipmentAdditionalInsuredEndorsement);		
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdIM_FarmEquipmentEndorsementNamedPerils);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdIM_FarmEquipmentEndorsementScheduleNamedPerils);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdIM_MiscellaneousArticlesEndorsement);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdIM_MiscellaneousArticlesScheduleEndorsement);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_AdverseActionLetter);

        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		sideMenu.clickSideMenuRiskAnalysis();
        risk.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        if (quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();

		sideMenu.clickSideMenuQuote();
		sideMenu.clickSideMenuForms();

        GenericWorkorderForms formsPage = new GenericWorkorderForms(driver);
		this.formsOrDocsActualFromUISubmissionPlusLocal = formsPage.getFormDescriptionsFromTable();
		this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = DocFormUtils
				.getListOfDocumentNamesForListOfEventNames(this.eventsHitDuringSubmissionCreationPlusLocal);
		this.actualExpectedDocDifferencesSubmissionPlusLocal = DocFormUtils.compareListsForDifferences(
				this.formsOrDocsActualFromUISubmissionPlusLocal,
				this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal);

		String errorMessage = "Account Number: " + squireIMPolObj.accountNumber;
		boolean testfailed = false;
		if (this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface().size() > 0) {
			testfailed = true;
			errorMessage = errorMessage + "ERROR: Documents for Rewrite Full Term In Expected But Not in UserInterface - "
					+ this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface() + "\n";
		}

		sideMenu.clickSideMenuQuote();

        StartRewrite rewrite = new StartRewrite(driver);
		rewrite.clickIssuePolicy();

		new GuidewireHelpers(driver).logout();		

		if (testfailed) {
			Assert.fail(errorMessage);
		}
	}

	@Test(dependsOnMethods = {"testSectionIVRewriteFullTermForms"})
	private void testSectionIVRewriteFullTermDocuments() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), squireIMPolObj.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuToolsDocuments();
        PolicyDocuments docs = new PolicyDocuments(driver);
		docs.selectRelatedTo("Rewrite Full Term");
		docs.clickSearch();
        ArrayList<String> descriptions = docs.getDocumentsDescriptionsFromTable();

		String[] documents = {				
				"Additional Interest Declarations", "Farm & Ranch Policy Declarations",
				"Notice of Policy on Privacy",
				"Adverse Action Letter","Special Endorsement for Inland Marine",
				"Livestock Endorsement",
				"Livestock Endorsement Schedule",
				"Farm Equipment Endorsement (Named Perils)",
				"Farm Equipment Endorsement Schedule (Named Perils)",
				"Boat Endorsement",
				"Boat Endorsement Schedule",
				"Recreational Vehicle or Wheelchair Endorsement",
				"Farm Equipment Special Form Endorsement",
				"Farm Equipment Special Form Schedule Endorsement",
				"Refrigerated Milk Endorsement",
				"Sports Equipment Endorsement",
				"Sports Equipment Schedule Endorsement",
				"Scheduled Personal Property Endorsement",
				"Scheduled Personal Property Schedule",
				"Miscellaneous Articles Endorsement",
				"Miscellaneous Articles Schedule Endorsement",
				"Bee Container Endorsement",
				"Bee Container Schedule Endorsement",
				"Communication Equipment Endorsement",
				"Communication Equipment Schedule Endorsement",
				"Owner's Goods Cargo (Class I or II) Endorsement",
				"Owner's Goods Cargo (Class I or II) Schedule Endorsement",
				"Owner's Goods Cargo (Class III) Endorsement",
				"Owner's Goods Cargo (Class III) Schedule Endorsement",
				"Contaminated and Refrigerated Milk Endorsement",
				"Irrigation Equipment Endorsement",
				"Irrigation Equipment Schedule Endorsement",
				"Farm Equipment Additional Insured Endorsement"/*,
				"Leasing Endorsement for Sections II and IV"*/
};

		boolean testFailed = false;		String errorMessage = "Account Number: " + squireIMPolObj.accountNumber;
		for (String document : documents) {	
			boolean documentFound = false;
			for(String desc: descriptions){
				if(desc.equals(document)){
					documentFound = true;
					break;
				}
			}
			if(!documentFound){
				testFailed = true;
				errorMessage = errorMessage + "Expected document : '"+document+ "' not available in documents page. \n";
			}
		}
		if(testFailed)
			Assert.fail(errorMessage);
	}
}

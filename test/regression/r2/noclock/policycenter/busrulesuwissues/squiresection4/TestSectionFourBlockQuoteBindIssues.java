package regression.r2.noclock.policycenter.busrulesuwissues.squiresection4;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import repository.gw.enums.CoverageType;
import repository.gw.enums.DateAddSubtractOptions;
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
import repository.gw.enums.PLUWIssues;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PersonalPropertyDeductible;
import repository.gw.enums.PersonalPropertyScheduledItemType;
import repository.gw.enums.PersonalPropertyType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.enums.Vehicle.PAComprehensive_CollisionDeductible;
import repository.gw.enums.Vehicle.VehicleType;
import repository.gw.errorhandling.ErrorHandling;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Cargo;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.Livestock;
import repository.gw.generate.custom.LivestockScheduledItem;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PersonalProperty;
import repository.gw.generate.custom.PersonalPropertyScheduledItem;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.RecreationalEquipment;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireIMCargo;
import repository.gw.generate.custom.SquireIMWatercraft;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.WatercraftScheduledItems;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_Cargo;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_FarmEquipment;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_Livestock;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_LivestockList;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_PersonalProperty;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_RecreationalEquipment;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_Watercraft;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages;
import repository.pc.workorders.generic.GenericWorkorderStandardIMCoverageSelection;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import persistence.globaldatarepo.helpers.VINHelper;

public class TestSectionFourBlockQuoteBindIssues extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObjPL = null;
	SoftAssert softAssert = new SoftAssert();
	private GeneratePolicy myPolChangeObjPL;

	@BeforeMethod(alwaysRun = true)
	public void beforeMethod() {
		softAssert = new SoftAssert();
	}

	@Test
	public void testCreateSectionIVFA() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

		// Farm Equipment
		IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment",
				"Manly Farm Equipment", 1000);
		ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
		farmEquip.add(farmThing);
		FarmEquipment imFarmEquip = new FarmEquipment(IMFarmEquipmentType.FarmEquipment, CoverageType.BroadForm,
				IMFarmEquipmentDeductible.FiveHundred, true, false, "Corn Hofman", farmEquip);
		ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
		allFarmEquip.add(imFarmEquip);

		SquireLiability liability = new SquireLiability();
		ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
		imTypes.add(InlandMarine.FarmEquipment);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liability;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;
        myInlandMarine.farmEquipment = allFarmEquip;

        Squire mySquire = new Squire(new GuidewireHelpers(driver).getRandomEligibility());
        mySquire.squirePA = new SquirePersonalAuto();
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL, LineSelection.InlandMarineLinePL)
                .withInsFirstLastName("SectionIV", "Block")
                .build(GeneratePolicyType.FullApp);
	}

	@Test(dependsOnMethods = { "testCreateSectionIVFA" })
	private void testAddBlockBindQuoteIssuesAndValidate() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(),
				myPolicyObjPL.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);

		// IM001 - New Jewelry Photos and Appraisal
		sideMenu.clickSideMenuIMCoveragePartSelection();
		GenericWorkorderStandardIMCoverageSelection imSelection = new GenericWorkorderStandardIMCoverageSelection(driver);
		imSelection.checkCoverage(true, InlandMarine.PersonalProperty.getValue());
        sideMenu.clickSideMenuIMPersonalEquipment();
		GenericWorkorderSquireInlandMarine_PersonalProperty ppPage = new GenericWorkorderSquireInlandMarine_PersonalProperty(driver);
        ppPage.clickAdd();
		ppPage.setType(PersonalPropertyType.Jewelry);
        ppPage.setDeductible(PersonalPropertyDeductible.Ded10Perc);
		ppPage.clickAddScheduledItems();
        ppPage.setScheduledItemType(PersonalPropertyScheduledItemType.Bracelet);
		ppPage.setDescription("Testing");
		ppPage.setScheduledItemPhotoUploadDate(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));

		// IM008 - Appraisal date older than 2 years
		Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter),
				DateAddSubtractOptions.Year, -3);
		ppPage.setScheduledItemAppraisalDate(newEff);
		ppPage.setScheduledItemLimit(1000);
		ppPage.clickOk();

		// IM004 - Recreational Equipment equal to or over $50,000
		// IM005 - Recreational Equipment equal to orover $15,000
		sideMenu.clickSideMenuIMCoveragePartSelection();
		imSelection.checkCoverage(true, InlandMarine.RecreationalEquipment.getValue());
        sideMenu.clickSideMenuIMRecreationVehicle();
        RecreationalEquipment allterrainVeh = new RecreationalEquipment(RecreationalEquipmentType.AllTerrainVehicle,
				"500", PAComprehensive_CollisionDeductible.Fifty50, "Brett Hiltbrand");
		RecreationalEquipment snowMobileVeh = new RecreationalEquipment(RecreationalEquipmentType.Snowmobile, "55000",
				PAComprehensive_CollisionDeductible.Fifty50, "Test Automation");
		RecreationalEquipment offRoadMotorVeh = new RecreationalEquipment(RecreationalEquipmentType.OffRoadMotorcycle,
				"16000", PAComprehensive_CollisionDeductible.Fifty50, "Test Automation");

		GenericWorkorderSquireInlandMarine_RecreationalEquipment recreationalEquipment = new GenericWorkorderSquireInlandMarine_RecreationalEquipment(driver);
		recreationalEquipment.recEquip(allterrainVeh);
		recreationalEquipment.recEquip(snowMobileVeh);
		recreationalEquipment.recEquip(offRoadMotorVeh);

		// IM020 - Wagon/Carriage photo yr
		sideMenu.clickSideMenuIMRecreationVehicle();
        RecreationalEquipment wagonVeh = new RecreationalEquipment(RecreationalEquipmentType.WagonsCarriages, "500",
				PAComprehensive_CollisionDeductible.Fifty50, "Brett Hiltbrand");
		recreationalEquipment.recEquip(wagonVeh);

		// IM006 - Personal Watercraft equal to over $15,000
		sideMenu.clickSideMenuIMCoveragePartSelection();
		imSelection.checkCoverage(true, InlandMarine.Watercraft.getValue());
        sideMenu.clickSideMenuIMWatercraft();
		WatercraftScheduledItems personalWater = new WatercraftScheduledItems(WatercraftTypes.PersonalWatercraft,
				DateUtils.convertStringtoDate("01/01/1999", "MM/dd/yyyy"), 16000);
		personalWater.setLimit(16000);
		personalWater.setItem(WatercratItems.PersonalWatercraft);
		ArrayList<WatercraftScheduledItems> personalWaterCrafts = new ArrayList<WatercraftScheduledItems>();
		personalWaterCrafts.add(personalWater);
		SquireIMWatercraft waterCrafts = new SquireIMWatercraft(WatercraftTypes.PersonalWatercraft,
				PAComprehensive_CollisionDeductible.OneHundred100, personalWaterCrafts);

		// Adding watercraft vehicles
		GenericWorkorderSquireInlandMarine_Watercraft watercraftPage = new GenericWorkorderSquireInlandMarine_Watercraft(driver);
		watercraftPage.watercraft(waterCrafts);

		try {
			sideMenu.clickSideMenuSquirePropertyCoverages();
		} catch (Exception e) {
            ErrorHandling softMsg = new ErrorHandling(driver);
			if (new GuidewireHelpers(driver).checkIfElementExists(softMsg.text_ErrorHandlingErrorBanner(), 1000)) {
				sideMenu.clickSideMenuSquirePropertyCoverages();
			}
		}
		GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
		coverages.clickSectionIICoveragesTab();
		GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages sectionII = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
        SectionIICoverages myCoverages = new SectionIICoverages(SectionIICoveragesEnum.PersonalWatercraft, 0, 1);
		sectionII.addCoverages(myCoverages);
		sectionII.setQuantity(myCoverages);

		SectionIICoverages myCoverages2 = new SectionIICoverages(SectionIICoveragesEnum.AllTerrainVehicles, 0, 2);
		sectionII.addCoverages(myCoverages2);
		sectionII.setQuantity(myCoverages2);

		SectionIICoverages myCoverages3 = new SectionIICoverages(SectionIICoveragesEnum.Snowmobiles, 0, 2);
		sectionII.addCoverages(myCoverages3);
		sectionII.setQuantity(myCoverages3);

		SectionIICoverages myCoverages4 = new SectionIICoverages(SectionIICoveragesEnum.OffRoadMotorcycles, 0, 2);
		sectionII.addCoverages(myCoverages4);
		sectionII.setQuantity(myCoverages4);

		SectionIICoverages myCoverages5 = new SectionIICoverages(SectionIICoveragesEnum.MotorBoats, 0, 2);
		sectionII.addCoverages(myCoverages5);
		sectionII.setQuantity(myCoverages5);

		SectionIICoverages myCoverages6 = new SectionIICoverages(SectionIICoveragesEnum.Livestock, 1, 1);
		sectionII.addCoverages(myCoverages6);
		sectionII.setLivestockTypeAndQuantity(LivestockScheduledItemType.Alpaca, 1);
		sectionII.setLivestockTypeAndQuantity(LivestockScheduledItemType.Bull, 1);

		sectionII.addWatercraftLengthCoverage("Testing", 28);

		// IM007 - Watercraft equal to over $65,000
		sideMenu.clickSideMenuIMWatercraft();
        WatercraftScheduledItems boat = new WatercraftScheduledItems(WatercraftTypes.BoatAndMotor,
				DateUtils.convertStringtoDate("01/01/1999", "MM/dd/yyyy"), 66000);
		boat.setLimit(66000);
		boat.setItem(WatercratItems.Boat);
		boat.setLength(28);
		boat.setBoatType(BoatType.Outboard);

		ArrayList<WatercraftScheduledItems> boats = new ArrayList<WatercraftScheduledItems>();
		boats.add(boat);

		SquireIMWatercraft boatType = new SquireIMWatercraft(WatercraftTypes.BoatAndMotor,
				PAComprehensive_CollisionDeductible.OneHundred100, boats);
		watercraftPage.watercraft(boatType);

		// IM009 - Personal Property equal to over $50,000
		PersonalProperty pprop1 = new PersonalProperty();
		pprop1.setType(PersonalPropertyType.Collectibles);
		pprop1.setLimit(1500);
		pprop1.setDeductible(PersonalPropertyDeductible.Ded5Perc);
		PersonalPropertyScheduledItem antiqueScheduled = new PersonalPropertyScheduledItem();
		antiqueScheduled.setParentPersonalPropertyType(PersonalPropertyType.Collectibles);
		antiqueScheduled.setDescription("Testing Antique Collectibles");
		antiqueScheduled.setLimit(52000);
		antiqueScheduled.setAppraisalDate(newEff);

		sideMenu.clickSideMenuIMPersonalEquipment();
        ppPage.clickAdd();
		ppPage.setType(pprop1.getType());
		ppPage.addLimitDeductible(pprop1.getLimit(), pprop1.getDeductible());
		ppPage.addScheduledItem(antiqueScheduled, pprop1.getType());
		ppPage.clickOk();

		// IM010 - Personal Property equal to over $20,000
		PersonalProperty pprop2 = new PersonalProperty();
		pprop2.setType(PersonalPropertyType.SportingEquipment);
		pprop2.setLimit(21000);
		pprop2.setDeductible(PersonalPropertyDeductible.Ded1000);
		PersonalPropertyScheduledItem sportsScheduledItem = new PersonalPropertyScheduledItem();
		sportsScheduledItem.setParentPersonalPropertyType(PersonalPropertyType.SportingEquipment);
		sportsScheduledItem.setLimit(21000);
		sportsScheduledItem.setDescription("Sports Stuff");
		sportsScheduledItem.setType(PersonalPropertyScheduledItemType.Guns);
		sportsScheduledItem.setMake("Honda");
		sportsScheduledItem.setModel("Accord");
		sportsScheduledItem.setYear(2015);
		sportsScheduledItem.setVinSerialNum("abcd12345");

		sideMenu.clickSideMenuIMPersonalEquipment();
        ppPage.clickAdd();
		ppPage.setType(pprop2.getType());
		ppPage.addLimitDeductible(pprop2.getLimit(), pprop2.getDeductible());
		ppPage.addScheduledItem(sportsScheduledItem, pprop2.getType());
		ppPage.clickOk();

		// IM011 - Livestock per head over $10,000
		Livestock livestock = new Livestock();
		livestock.setType(LivestockType.Livestock);
		livestock.setDeductible(LivestockDeductible.Ded100);
		LivestockScheduledItem livSchedItem1 = new LivestockScheduledItem(livestock.getType(),
				LivestockScheduledItemType.Alpaca, "Test Alpaca 1", 11000);
		LivestockScheduledItem livSchedItem2 = new LivestockScheduledItem(livestock.getType(),
				LivestockScheduledItemType.Bull, "Test BullCow 1", "TAG123", "Brand1", "Breed1", 5000);
		ArrayList<String> livSchedItem2AdditInsureds = new ArrayList<String>();
		livSchedItem2AdditInsureds.add("Sched Item Guy1");
		livSchedItem2AdditInsureds.add("Sched Item Guy2");
		livSchedItem2.setAdditionalInsureds(livSchedItem2AdditInsureds);
		ArrayList<LivestockScheduledItem> livSchedItems = new ArrayList<LivestockScheduledItem>();
		livSchedItems.add(livSchedItem1);
		livSchedItems.add(livSchedItem2);
		livestock.setScheduledItems(livSchedItems);

		sideMenu.clickSideMenuIMCoveragePartSelection();
		imSelection.checkCoverage(true, InlandMarine.Livestock.getValue());

		sideMenu.clickSideMenuIMLivestock();
		GenericWorkorderSquireInlandMarine_LivestockList livestockListPage = new GenericWorkorderSquireInlandMarine_LivestockList(driver);
		livestockListPage.clickAdd();

		GenericWorkorderSquireInlandMarine_Livestock livestockPage = new GenericWorkorderSquireInlandMarine_Livestock(driver);
		livestockPage.setType(livestock.getType());
		livestockPage.setDeductible(livestock.getDeductible());

		for (LivestockScheduledItem lsi : livestock.getScheduledItems()) {
			livestockPage.addScheduledItem(lsi);
		}

		if (livestock.getAdditionalInsureds() != null)
			livestockPage.addAdditionalInsureds(livestock.getAdditionalInsureds());

		livestockPage.clickOk();

		// IM012 - Class III Cargo radius over 500
		Vin vin = VINHelper.getRandomVIN();
		Cargo cargoTrailer1 = new Cargo(VehicleType.Trailer, vin.getVin(), Integer.parseInt(vin.getYear()),
				vin.getMake(), vin.getModel());
		cargoTrailer1.setLimit("3123");
		ArrayList<Cargo> cargoTrailers = new ArrayList<Cargo>();
		cargoTrailers.add(cargoTrailer1);
		ArrayList<String> cargoAddInsured = new ArrayList<String>();
		cargoAddInsured.add("Cor Hofman");
		SquireIMCargo stuff = new SquireIMCargo(CargoClass.ClassIII, ClassIIICargoTypes.Milk, 550, 1000,
				"Brenda Swindle", false, cargoTrailers, cargoAddInsured);

		SquireIMCargo cargo = new SquireIMCargo(CargoClass.ClassI, 100, 1000, "Brenda Swindle", false, cargoTrailers);

		sideMenu.clickSideMenuIMCoveragePartSelection();
        imSelection.checkCoverage(true, InlandMarine.Cargo.getValue());
        sideMenu.clickSideMenuIMCargo();

		GenericWorkorderSquireInlandMarine_Cargo cargoPage = new GenericWorkorderSquireInlandMarine_Cargo(driver);

		if (!this.myPolicyObjPL.squire.squireEligibility.equals(SquireEligibility.City)) {
			cargoPage.inputCargoClassIII(stuff);
		} else {
			// IM021 Cargo on City Squire
			cargoPage.inputCargo(cargo);
			cargoPage.setCargoDescription("Testing purpose");
			cargoPage.clickOKButton();
		}

		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
		}

		sideMenu.clickSideMenuRiskAnalysis();
		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();

		@SuppressWarnings("serial")
		List<PLUWIssues> sqSectionIVBlockBind = new ArrayList<PLUWIssues>() {
			{
				this.add(PLUWIssues.LivestockPerheadOver10K);
				this.add(PLUWIssues.PersonalPropertyEqualOver20K);
				this.add(PLUWIssues.PersonalPropertyEqualOver50K);
				this.add(PLUWIssues.RecEquipEqualOver50K);
			}
		};

		@SuppressWarnings("serial")
		List<PLUWIssues> sqSectionIVBlockIssuance = new ArrayList<PLUWIssues>() {
			{
				this.add(PLUWIssues.AppraisalDateOver2yrs);
				this.add(PLUWIssues.ClassIIICargoRaduisOver500);
				this.add(PLUWIssues.PersonalWatercraftequalOver10K);
				this.add(PLUWIssues.WatercraftEqualOver65K);
				this.add(PLUWIssues.RecEquipmentEqualMore15K);
			}
		};

		PLUWIssues sqSectionIVInformation = PLUWIssues.MissingWagonCarriagePhotoYear;

		for (PLUWIssues uwBlockBindExpected : sqSectionIVBlockBind) {
			softAssert.assertFalse(
					!uwIssues.isInList(uwBlockBindExpected.getLongDesc()).equals(UnderwriterIssueType.BlockSubmit),
					"Expected error Bind Issuance : " + uwBlockBindExpected.getShortDesc() + " is not displayed");

		}

		for (PLUWIssues uwBlockBindExpected : sqSectionIVBlockIssuance) {

			if (this.myPolicyObjPL.squire.squireEligibility.equals(SquireEligibility.City) && uwBlockBindExpected
					.getShortDesc().equals(PLUWIssues.ClassIIICargoRaduisOver500.getShortDesc())) {
				continue;
			}
			softAssert.assertFalse(
					!uwIssues.isInList(uwBlockBindExpected.getLongDesc()).equals(UnderwriterIssueType.BlockIssuance),
					"Expected error Information : " + uwBlockBindExpected.getShortDesc() + " is not displayed");
		}

		softAssert.assertFalse(
				!uwIssues.isInList(sqSectionIVInformation.getLongDesc()).equals(UnderwriterIssueType.Informational),
				"Expected error Bind Issuance : " + sqSectionIVInformation.getShortDesc() + " is not displayed");

		PLUWIssues sqSectionIVCityInformation = PLUWIssues.CargoOnCitySquire;

		if (this.myPolicyObjPL.squire.squireEligibility.equals(SquireEligibility.City)) {
			softAssert.assertFalse(
					!uwIssues.isInList(sqSectionIVCityInformation.getLongDesc())
							.equals(UnderwriterIssueType.Informational),
					"Expected error Bind Issuance : " + sqSectionIVCityInformation.getShortDesc()
							+ " is not displayed");
		}

		softAssert.assertAll();
	}

	// IM015 - Inland Marine Coverage change
	@Test
	public void testCreateSectionIVIssueAndPolicyChange() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

		// Farm Equipment
		IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment",
				"Manly Farm Equipment", 1000);
		ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
		farmEquip.add(farmThing);
		FarmEquipment imFarmEquip = new FarmEquipment(IMFarmEquipmentType.FarmEquipment, CoverageType.BroadForm,
				IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
		ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
		allFarmEquip.add(imFarmEquip);

		SquireLiability liability = new SquireLiability();
		ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
		imTypes.add(InlandMarine.FarmEquipment);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liability;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;
        myInlandMarine.farmEquipment = allFarmEquip;

        Squire mySquire = new Squire(new GuidewireHelpers(driver).getRandomEligibility());
        mySquire.squirePA = new SquirePersonalAuto();
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;

        myPolChangeObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL, LineSelection.InlandMarineLinePL)
                .withInsFirstLastName("SectionIV", "Block")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                myPolChangeObjPL.squire.getPolicyNumber());

		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);

		// add 10 days to current date
		Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 10);

		// start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", changeDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuIMFarmEquipment();
		GenericWorkorderSquireInlandMarine_FarmEquipment farmEquipment = new GenericWorkorderSquireInlandMarine_FarmEquipment(driver);
		farmEquipment.removeFarmEquipmentByRowinTable(1);
        FarmEquipment imCirEquip = new FarmEquipment(IMFarmEquipmentType.CircleSprinkler, CoverageType.BroadForm,
				IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);

		farmEquipment.addFarmEquip(myPolChangeObjPL.basicSearch, imCirEquip);
		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
		}

		sideMenu.clickSideMenuRiskAnalysis();
		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();
		PLUWIssues sqSectionIVIssuance = PLUWIssues.InlandMarinCoverageChange;
		softAssert.assertFalse(
				!uwIssues.isInList(sqSectionIVIssuance.getLongDesc()).equals(UnderwriterIssueType.BlockIssuance),
				"Expected error Bind Issuance : " + sqSectionIVIssuance.getShortDesc() + " is not displayed");

		softAssert.assertAll();
	}
}

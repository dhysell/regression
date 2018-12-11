package regression.r2.noclock.policycenter.busrulesuwissues.squiresection4;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import repository.gw.enums.CoverageType;
import repository.gw.enums.FPP.FPPCoverageTypes;
import repository.gw.enums.FPP.FPPDeductible;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.FPP.FarmPersonalPropertyTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.CargoClass;
import repository.gw.enums.InlandMarineTypes.ClassIIICargoTypes;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.InlandMarineTypes.RecreationalEquipmentType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.LivestockScheduledItemType;
import repository.gw.enums.LivestockType;
import repository.gw.enums.PLUWIssues;
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
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.RecreationalEquipment;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireIMCargo;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderLineSelection;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_Cargo;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_FarmEquipment;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_Livestock;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_LivestockList;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_PersonalProperty;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_RecreationalEquipment;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages;
import repository.pc.workorders.generic.GenericWorkorderStandardIMCoverageSelection;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.VINHelper;

public class TestSectionFourValidationAvailability extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObjPL = null;
	SoftAssert softAssert = new SoftAssert();
	private String im018BusRules = "The deductible was changed on All Terrain Vehicle. The change will be applied to all All Terrain Vehicle";

	@BeforeMethod(alwaysRun = true)
	public void beforeMethod() {
		softAssert = new SoftAssert();
	}

	@Test
	public void testGenerateSectionIVFA() throws Exception {
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
        myInlandMarine.farmEquipment = allFarmEquip;
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;

        Squire mySquire = new Squire(new GuidewireHelpers(driver).getRandomEligibility());
        mySquire.inlandMarine = myInlandMarine;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL)
                .withInsFirstLastName("SectionIV", "Rules")
				.build(GeneratePolicyType.FullApp);
	}

	@Test(dependsOnMethods = { "testGenerateSectionIVFA" })
	private void testAddAvailabilityRules() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(),
				myPolicyObjPL.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuLineSelection();
        GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(driver);
		lineSelection.checkPersonalAutoLine(true);
		sideMenu.clickSideMenuIMCoveragePartSelection();
        GenericWorkorderStandardIMCoverageSelection imSelection = new GenericWorkorderStandardIMCoverageSelection(driver);
		imSelection.checkCoverage(true, InlandMarine.Cargo.getValue());
        sideMenu.clickSideMenuIMCargo();
		Vin vin = VINHelper.getRandomVIN();
		Cargo cargoTrailer1 = new Cargo(VehicleType.Trailer, vin.getVin(), Integer.parseInt(vin.getYear()),
				vin.getMake(), vin.getModel());
		cargoTrailer1.setLimit("1000");
		ArrayList<Cargo> cargoTrailers = new ArrayList<Cargo>();
		cargoTrailers.add(cargoTrailer1);
		ArrayList<String> cargoAddInsured = new ArrayList<String>();
		cargoAddInsured.add("Cor Hofman");
		SquireIMCargo stuff = new SquireIMCargo(CargoClass.ClassII, ClassIIICargoTypes.Milk, 100, 1000,
				"Brenda Swindle", false, cargoTrailers, cargoAddInsured);

		// IM013 - Cargo needs all section
        GenericWorkorderSquireInlandMarine_Cargo cargo = new GenericWorkorderSquireInlandMarine_Cargo(driver);
		cargo.inputCargo(stuff);
		if(this.myPolicyObjPL.squire.squireEligibility == SquireEligibility.City){
			cargo.setCargoDescription("Testing purpose");
			cargo.clickOKButton();
		}
		sideMenu.clickSideMenuLineSelection();
        lineSelection.checkSquireLineSelectionByTextNoVerify("Auto Line", false);
        softAssert.assertFalse(
				!new GuidewireHelpers(driver).errorMessagesExist() && (!new GuidewireHelpers(driver).getFirstErrorMessage().contains(
						"Unable to remove the Personal Auto Line because Cargo coverage on the Inland Marine line requires it")),
				"Cargo coverage on the Inland Marine line requires it. is not displayed.");
		
		softAssert.assertAll();

		sideMenu.clickSideMenuIMCargo();
        cargo.clickEditButtonByRowInCargoCoverageTable(1);
		cargo.removeAdditionalInsuredByName("Cor Hofman");
		cargo.clickOKButton();
		cargo.setCheckBoxByRowinCargoCoverageTable(1);
		cargo.clickRemoveButton();
        new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();

		sideMenu.clickSideMenuIMCoveragePartSelection();
		imSelection.checkCoverage(false, InlandMarine.Cargo.getValue());
        sideMenu.clickSideMenuLineSelection();
		lineSelection.checkPersonalAutoLine(false);
		
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
	}

	@Test(dependsOnMethods = { "testAddAvailabilityRules" })
	private void testAddQuoteErrors() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(),
				myPolicyObjPL.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);

		// IM003 - Valid Death of Livestock
		sideMenu.clickSideMenuIMCoveragePartSelection();
        GenericWorkorderStandardIMCoverageSelection imSelection = new GenericWorkorderStandardIMCoverageSelection(driver);
		imSelection.checkCoverage(true, InlandMarine.Livestock.getValue());

		sideMenu.clickSideMenuIMLivestock();
        GenericWorkorderSquireInlandMarine_LivestockList livestockListPage = new GenericWorkorderSquireInlandMarine_LivestockList(driver);
		livestockListPage.clickAdd();
        GenericWorkorderSquireInlandMarine_Livestock livestockPage = new GenericWorkorderSquireInlandMarine_Livestock(driver);
		livestockPage.setType(LivestockType.DeathOfLivestock);
		livestockPage.addScheduledItemForDeathOfLivestock(LivestockScheduledItemType.SheepD, 1);
		livestockPage.clickOk();

        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
		
		// IM014 - FPP and Farm & Irrigation equipment
		if(!myPolicyObjPL.squire.isCity()){
			sideMenu.clickSideMenuSquirePropertyCoverages();
			coverages.clickFarmPersonalProperty();
            GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages fpp = new GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages(driver);
			fpp.checkCoverageD(true);
			fpp.selectCoverageType(FPPCoverageTypes.BlanketInclude);
			fpp.selectDeductible(FPPDeductible.Ded_1000);
            fpp.selectCoverages(FarmPersonalPropertyTypes.Machinery);
			fpp.addItem(FPPFarmPersonalPropertySubTypes.Tractors, 1, 1000, "Testing Description");
		}
		
		// IM018 - ATV Deductible
		sideMenu.clickSideMenuIMCoveragePartSelection();
		imSelection.checkCoverage(true, InlandMarine.RecreationalEquipment.getValue());
        sideMenu.clickSideMenuIMRecreationVehicle();
        RecreationalEquipment latestrecEquip = new RecreationalEquipment(RecreationalEquipmentType.AllTerrainVehicle, "6000", PAComprehensive_CollisionDeductible.TwentyFive25, "Test Automation");
		RecreationalEquipment bicyclerecEquip = new RecreationalEquipment(RecreationalEquipmentType.AllTerrainVehicle, "6000", PAComprehensive_CollisionDeductible.Fifty50, "Test Automation");

        GenericWorkorderSquireInlandMarine_RecreationalEquipment recreationalEquipment = new GenericWorkorderSquireInlandMarine_RecreationalEquipment(driver);
		recreationalEquipment.recEquip(latestrecEquip);
		recreationalEquipment.recEquip(bicyclerecEquip);
        softAssert.assertFalse(!recreationalEquipment.recEquipchooseOKOnConfirmation().contains(im018BusRules), "Expected page validation : '" + im018BusRules + "' is not displayed.");

		try{
			sideMenu.clickSideMenuSquirePropertyCoverages();			
		}catch(Exception e){
            ErrorHandling softMsg = new ErrorHandling(driver);
			if(new GuidewireHelpers(driver).checkIfElementExists(softMsg.text_ErrorHandlingErrorBanner(), 1000)){
				sideMenu.clickSideMenuSquirePropertyCoverages();
			}
		}
		coverages.clickSectionIICoveragesTab();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages addCoverage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
        SectionIICoverages myCoverages = new SectionIICoverages(SectionIICoveragesEnum.AllTerrainVehicles, 0, 2);
		addCoverage.addCoverages(myCoverages);
		addCoverage.setQuantity(myCoverages);
		coverages.clickNext();
		
		// IM019 - Farm Equipment Broad Form
		sideMenu.clickSideMenuIMFarmEquipment();
		IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment",
				"Manly Farm Equipment", 1000);
		ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
		farmEquip.add(farmThing);
		FarmEquipment imFarmEquip = new FarmEquipment(IMFarmEquipmentType.FarmEquipment, CoverageType.SpecialForm,
				IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);

        GenericWorkorderSquireInlandMarine_FarmEquipment farmEquipment = new GenericWorkorderSquireInlandMarine_FarmEquipment(driver);
		farmEquipment.addFarmEquip(myPolicyObjPL.basicSearch, imFarmEquip);
		
		farmEquipment.clickAdd();
        List<String> allValues = farmEquipment.getTypeValues();
		for(String currentValue: allValues){
			softAssert.assertFalse(currentValue.equals(IMFarmEquipmentType.FarmEquipment.getValue()),
						"Policy can have more than Farm Equip w/ Broad Form, Farm Equip w/ Special Form");
		}
		farmEquipment.setType(IMFarmEquipmentType.MovableSetSprinkler);
		farmEquipment.setDeductible(IMFarmEquipmentDeductible.Fifty);
		
		farmEquipment.setInspected(true);
		farmEquipment.setExistingDamage(false);
		farmEquipment.setScheduledItem(farmThing);
		farmEquipment.clickOk();
        farmEquipment.removeFarmEquipmentByRowinTable(2);

		
		// - One Personal Property
		sideMenu.clickSideMenuIMCoveragePartSelection();
        imSelection.checkCoverage(true, InlandMarine.PersonalProperty.getValue());
        sideMenu.clickSideMenuIMPersonalEquipment();
        GenericWorkorderSquireInlandMarine_PersonalProperty personalProperty = new GenericWorkorderSquireInlandMarine_PersonalProperty(driver);
		personalProperty.clickAdd();		
		personalProperty.setType(PersonalPropertyType.Jewelry);
        personalProperty.setDeductible(PersonalPropertyDeductible.Ded10Perc);
		personalProperty.clickAddScheduledItems();
        personalProperty.setScheduledItemType(PersonalPropertyScheduledItemType.Bracelet);
		personalProperty.setDescription("Testing");
		personalProperty.setScheduledItemPhotoUploadDate(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));
		personalProperty.setScheduledItemAppraisalDate(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));
		personalProperty.setScheduledItemLimit(1000);
		personalProperty.clickOk();
        personalProperty.clickAdd();
        for (String personal : personalProperty.getTypeValues()) {
			softAssert.assertFalse(personal.equals(PersonalPropertyScheduledItemType.Bracelet.getValue()),
					"Policy have more than Personal Property values");
		}
		personalProperty.setType(PersonalPropertyType.FineArts);
        personalProperty.setDeductible(PersonalPropertyDeductible.Ded10Perc);
		personalProperty.clickAddScheduledItems();
        personalProperty.setDescription("Testing");
		personalProperty.setScheduledItemAppraisalDate(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));
		personalProperty.setScheduledItemLimit(1000);
		personalProperty.clickOk();

        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
		}
		
		sideMenu.clickSideMenuRiskAnalysis();
		
		PLUWIssues sqSectionIIIBlockBind = PLUWIssues.ValidDeathOfLivestock;
		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();
		softAssert.assertFalse(
					!uwIssues.isInList(sqSectionIIIBlockBind.getLongDesc()).equals(UnderwriterIssueType.BlockQuote),
					"Expected error Bind Issuance : " + sqSectionIIIBlockBind.getShortDesc() + " is not displayed");
		softAssert.assertAll();		
	}
}

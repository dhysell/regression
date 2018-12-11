package previousProgramIncrement.pi2_062818_090518.f284_AutoPolicyChange;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InlandMarineTypes.BoatType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.InlandMarineTypes.RecreationalEquipmentType;
import repository.gw.enums.InlandMarineTypes.WatercraftTypes;
import repository.gw.enums.InlandMarineTypes.WatercratItems;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PLUWIssues;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.enums.Vehicle.PAComprehensive_CollisionDeductible;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.RecreationalEquipment;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireIMWatercraft;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.WatercraftScheduledItems;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_RecreationalEquipment;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages;
import repository.pc.workorders.generic.GenericWorkorderStandardIMCoverageSelection;

/**
* @Author nvadlamudi
* @Requirement :US15611:Disable/change UW Issues - Section 4 (new activity)
* @RequirementsLink <a href="https://fbmicoi.sharepoint.com/:x:/r/sites/ARTists2/_layouts/15/Doc.aspx?sourcedoc=%7B45AD1E27-B290-4BBD-AD76-BC67A17BD898%7D&file=UW%20Issues%20to%20Change%20for%20Auto%20Issue.xlsx&action=default&mobileredirect=true">UW Issues to Change for Auto Issue</a>
* @Description : Test to validate IM005, IM006, IM007, IM015 showing as Informational
* @DATE Aug 6, 2018
*/
public class US15611ChangeSectionIVUWNewActivity extends BaseTest {
	SoftAssert softAssert = new SoftAssert();
	private WebDriver driver;

	@Test
	public void testCheckSectionIVUWIssueInSubmission() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
		imTypes.add(InlandMarine.RecreationalEquipment);
		imTypes.add(InlandMarine.Watercraft);

		// Rec Equip
		// IM005 Recreational Equipment equal to or over $15,000
		ArrayList<RecreationalEquipment> recVehicle = new ArrayList<RecreationalEquipment>();
		recVehicle.add(new RecreationalEquipment(RecreationalEquipmentType.OffRoadMotorcycle, "16000",
				PAComprehensive_CollisionDeductible.Fifty50, "Test Automation"));

		// Watercraft
		// IM006 - Personal Watercraft equal to over $15,000
		ArrayList<AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
		AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Person);
		loc2Bldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);
		WatercraftScheduledItems personalWater = new WatercraftScheduledItems(WatercraftTypes.PersonalWatercraft,
				DateUtils.convertStringtoDate("01/01/1999", "MM/dd/yyyy"), 16000);
		personalWater.setLimit(16000);
		personalWater.setLength(28);
		personalWater.setItem(WatercratItems.PersonalWatercraft);
		ArrayList<WatercraftScheduledItems> personalWaterCrafts = new ArrayList<WatercraftScheduledItems>();
		personalWaterCrafts.add(personalWater);
		SquireIMWatercraft waterCrafts = new SquireIMWatercraft(WatercraftTypes.PersonalWatercraft,
				PAComprehensive_CollisionDeductible.OneHundred100, personalWaterCrafts);
		waterCrafts.setAdditionalInterest(loc2Bldg1AdditionalInterests);
		// IM007 - Watercraft equal to over $65,000
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
		boatType.setAdditionalInterest(loc2Bldg1AdditionalInterests);
		ArrayList<SquireIMWatercraft> boatTypes = new ArrayList<SquireIMWatercraft>();
		boatTypes.add(waterCrafts);
		boatTypes.add(boatType);

		SquireInlandMarine myInlandMarine = new SquireInlandMarine();
		myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;
		myInlandMarine.recEquipment_PL_IM = recVehicle;
		myInlandMarine.watercrafts_PL_IM = boatTypes;			
		
		Squire mySquire = new Squire(new GuidewireHelpers(driver).getRandomEligibility());
		mySquire.inlandMarine = myInlandMarine;

		GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver).withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL, LineSelection.Membership)
				.withInsFirstLastName("SectionIV", "Block").isDraft().build(GeneratePolicyType.FullApp);

		new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(),
				myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);
		new GuidewireHelpers(driver).editPolicyTransaction();
		SideMenuPC sideMenu = new SideMenuPC(driver);
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
		List<PLUWIssues> sqSectionIVBlockInfo = new ArrayList<PLUWIssues>() {
			{
				this.add(PLUWIssues.PersonalWatercraftequalOver10K);
				this.add(PLUWIssues.WatercraftEqualOver65K);
				this.add(PLUWIssues.RecEquipmentEqualMore15K);
			}
		};

		for (PLUWIssues uwBlockBindExpected : sqSectionIVBlockInfo) {
			softAssert.assertFalse(
					!uwIssues.isInList(uwBlockBindExpected.getLongDesc()).equals(UnderwriterIssueType.Informational),
					"Expected Block Informational : " + uwBlockBindExpected.getShortDesc() + " is not displayed");

		}
		softAssert.assertAll();

	}

	@Test
	public void testCheckSectionIVUWIssueInPolicyChange() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
		imTypes.add(InlandMarine.RecreationalEquipment);
		imTypes.add(InlandMarine.Watercraft);

		// Rec Equip
		// IM005 Recreational Equipment equal to or over $15,000
		ArrayList<RecreationalEquipment> recVehicle = new ArrayList<RecreationalEquipment>();
		recVehicle.add(new RecreationalEquipment(RecreationalEquipmentType.OffRoadMotorcycle, "16000",
				PAComprehensive_CollisionDeductible.Fifty50, "Test Automation"));

		// Watercraft
		// IM006 - Personal Watercraft equal to over $15,000
		ArrayList<AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
		AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Person);
		loc2Bldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);
		
		
		WatercraftScheduledItems personalWater = new WatercraftScheduledItems(WatercraftTypes.PersonalWatercraft,
				DateUtils.convertStringtoDate("01/01/1999", "MM/dd/yyyy"), 16000);
		personalWater.setLimit(16000);
		personalWater.setLength(28);
		personalWater.setItem(WatercratItems.PersonalWatercraft);
		ArrayList<WatercraftScheduledItems> personalWaterCrafts = new ArrayList<WatercraftScheduledItems>();
		personalWaterCrafts.add(personalWater);
		SquireIMWatercraft waterCrafts = new SquireIMWatercraft(WatercraftTypes.PersonalWatercraft,
				PAComprehensive_CollisionDeductible.OneHundred100, personalWaterCrafts);
		waterCrafts.setAdditionalInterest(loc2Bldg1AdditionalInterests);

		// IM007 - Watercraft equal to over $65,000
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
		boatType.setAdditionalInterest(loc2Bldg1AdditionalInterests);

		ArrayList<SquireIMWatercraft> boatTypes = new ArrayList<SquireIMWatercraft>();
		boatTypes.add(waterCrafts);
		boatTypes.add(boatType);

		SquireInlandMarine myInlandMarine = new SquireInlandMarine();
		myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;
		myInlandMarine.recEquipment_PL_IM = recVehicle;
		myInlandMarine.watercrafts_PL_IM = boatTypes;
	
		Squire mySquire = new Squire(new GuidewireHelpers(driver).getRandomEligibility());
		mySquire.inlandMarine = myInlandMarine;
		
		GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver).withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL, LineSelection.Membership)
				.withInsFirstLastName("SectionIV", "Block").withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash).build(GeneratePolicyType.PolicyIssued);

		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObjPL.underwriterInfo.getUnderwriterUserName(),
				myPolicyObjPL.underwriterInfo.getUnderwriterPassword(), myPolicyObjPL.accountNumber);

		// start policy change
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change",
				DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));

		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuIMCoveragePartSelection();
		GenericWorkorderStandardIMCoverageSelection imSelection = new GenericWorkorderStandardIMCoverageSelection(
				driver);

		imSelection.checkCoverage(true, InlandMarine.RecreationalEquipment.getValue());
		sideMenu.clickSideMenuIMRecreationVehicle();
		RecreationalEquipment allterrainVeh = new RecreationalEquipment(RecreationalEquipmentType.AllTerrainVehicle,
				"500", PAComprehensive_CollisionDeductible.Fifty50, "Brett Hiltbrand");
		GenericWorkorderSquireInlandMarine_RecreationalEquipment recreationalEquipment = new GenericWorkorderSquireInlandMarine_RecreationalEquipment(
				driver);
		recreationalEquipment.recEquip(allterrainVeh);
		recreationalEquipment.clickNext();
		sideMenu.clickSideMenuSquirePropertyCoverages();
		GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		coverage.clickSectionIICoveragesTab();
		GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages addCoverage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
		SectionIICoverages myCoverages = new SectionIICoverages(SectionIICoveragesEnum.AllTerrainVehicles, 0, 1);		
		addCoverage.addCoverages(myCoverages);
		addCoverage.setQuantity(myCoverages);
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
		List<PLUWIssues> sqSectionIVBlockInfo = new ArrayList<PLUWIssues>() {
			{
				this.add(PLUWIssues.PersonalWatercraftequalOver10K);
				this.add(PLUWIssues.WatercraftEqualOver65K);
				this.add(PLUWIssues.RecEquipmentEqualMore15K);
				this.add(PLUWIssues.InlandMarinCoverageChange);
			}
		};

		for (PLUWIssues uwBlockBindExpected : sqSectionIVBlockInfo) {
			softAssert.assertFalse(
					!uwIssues.isInList(uwBlockBindExpected.getLongDesc()).equals(UnderwriterIssueType.Informational),
					"Expected Block Informational : " + uwBlockBindExpected.getShortDesc() + " is not displayed");

		}

		softAssert.assertAll();

	}
}

package previousProgramIncrement.pi2_062818_090518.f284_AutoPolicyChange;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.CoverageType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PLUWIssues;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PersonalPropertyDeductible;
import repository.gw.enums.PersonalPropertyScheduledItemType;
import repository.gw.enums.PersonalPropertyType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderLineSelection;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_PersonalProperty;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderStandardIMCoverageSelection;

import java.util.ArrayList;
import java.util.Date;

/**
* @Author nvadlamudi
* @Requirement :US15806:Disable/change UW Issues - Section 4
* @RequirementsLink <a href="https://fbmicoi.sharepoint.com/:x:/r/sites/ARTists2/_layouts/15/Doc.aspx?sourcedoc=%7B45AD1E27-B290-4BBD-AD76-BC67A17BD898%7D&file=UW%20Issues%20to%20Change%20for%20Auto%20Issue.xlsx&action=default&mobileredirect=true">UW Issues to Change for Auto Issue</a>
* @Description :Validate IM001, IM008, IM022 UW issues changes
* @DATE Aug 1, 2018
*/
public class US15806DisableChangeSectionIVUWIssues extends BaseTest {
	SoftAssert softAssert = new SoftAssert();

	@BeforeMethod(alwaysRun = true)
	public void beforeMethod() {
		softAssert = new SoftAssert();
	}

	@Test
	public void testCheckSectionIVUWIssueInSubmission() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
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
		mySquire.propertyAndLiability = myPropertyAndLiability;
		mySquire.inlandMarine = myInlandMarine;

		GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver).withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL)
				.withInsFirstLastName("SectionIV", "Block").build(GeneratePolicyType.FullApp);

		new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(),
				myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);
		new GuidewireHelpers(driver).editPolicyTransaction();
		SideMenuPC sideMenu = new SideMenuPC(driver);

		// IM001 - New Jewelry Photos and Appraisal
		sideMenu.clickSideMenuIMCoveragePartSelection();
		GenericWorkorderStandardIMCoverageSelection imSelection = new GenericWorkorderStandardIMCoverageSelection(
				driver);
		imSelection.checkCoverage(true, InlandMarine.PersonalProperty.getValue());
		sideMenu.clickSideMenuIMPersonalEquipment();
		GenericWorkorderSquireInlandMarine_PersonalProperty ppPage = new GenericWorkorderSquireInlandMarine_PersonalProperty(
				driver);
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

		sideMenu.clickSideMenuRiskAnalysis();
		GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);

		risk.Quote();
		GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
		}

		sideMenu.clickSideMenuRiskAnalysis();
		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();

		softAssert.assertFalse(
				!uwIssues.isInList(PLUWIssues.NewJewelryPhotosAndAppraisal.getLongDesc())
						.equals(UnderwriterIssueType.BlockSubmit),
				"Expected Block Submit : " + PLUWIssues.NewJewelryPhotosAndAppraisal.getShortDesc()
						+ " is not displayed");

		softAssert.assertFalse(
				uwIssues.isInList(PLUWIssues.AppraisalDateOver2yrs.getLongDesc())
						.equals(UnderwriterIssueType.BlockSubmit),
				"Unexpected Block Submit : " + PLUWIssues.NewJewelryPhotosAndAppraisal.getShortDesc()
						+ " is displayed");

		softAssert.assertAll();

	}

	@Test
	public void testCheckSectionIVUWIssueInPolicyChange() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
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
		mySquire.propertyAndLiability = myPropertyAndLiability;
		mySquire.inlandMarine = myInlandMarine;

		GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver).withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL)
				.withInsFirstLastName("SectionIV", "Change").withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash).build(GeneratePolicyType.PolicyIssued);

		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObjPL.underwriterInfo.getUnderwriterUserName(),
				myPolicyObjPL.underwriterInfo.getUnderwriterPassword(), myPolicyObjPL.accountNumber);

		// start policy change
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change",
				DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));

		SideMenuPC sideMenu = new SideMenuPC(driver);
		// IM001 - New Jewelry Photos and Appraisal
		sideMenu.clickSideMenuIMCoveragePartSelection();
		GenericWorkorderStandardIMCoverageSelection imSelection = new GenericWorkorderStandardIMCoverageSelection(
				driver);
		imSelection.checkCoverage(true, InlandMarine.PersonalProperty.getValue());
		sideMenu.clickSideMenuIMPersonalEquipment();
		GenericWorkorderSquireInlandMarine_PersonalProperty ppPage = new GenericWorkorderSquireInlandMarine_PersonalProperty(
				driver);
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

		// IM022 Section IV without Sections I and II
		sideMenu.clickSideMenuSquirePropertyDetail();
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(
				driver);
		propertyDetail.setCheckBoxByRowInPropertiesTable(1, true);
		propertyDetail.clickRemoveProperty();

		sideMenu.clickSideMenuPropertyLocations();
		GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(
				driver);
		propertyLocations.SelectLocationsCheckboxByRowNumber(1);
		propertyLocations.clickRemoveButton();
		sideMenu.clickSideMenuLineSelection();
		GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(driver);
		lineSelection.checkPersonalPropertyLine(false);

		sideMenu.clickSideMenuRiskAnalysis();
		GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);

		risk.Quote();
		GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
		}

		sideMenu.clickSideMenuRiskAnalysis();
		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();

		softAssert.assertFalse(
				!uwIssues.isInList(PLUWIssues.NewJewelryPhotosAndAppraisal.getLongDesc())
						.equals(UnderwriterIssueType.BlockSubmit),
				"Expected Block Submit : " + PLUWIssues.NewJewelryPhotosAndAppraisal.getShortDesc()
						+ " is not displayed");
		
		softAssert.assertFalse(
				!uwIssues.isInList(PLUWIssues.SectionIVwithoutSectionsIAndII.getLongDesc())
						.equals(UnderwriterIssueType.BlockSubmit),
				"Expected Block Submit : " + PLUWIssues.SectionIVwithoutSectionsIAndII.getShortDesc()
						+ " is not displayed");

		softAssert.assertFalse(
				uwIssues.isInList(PLUWIssues.AppraisalDateOver2yrs.getLongDesc())
						.equals(UnderwriterIssueType.BlockSubmit),
				"Unexpected Block Submit : " + PLUWIssues.NewJewelryPhotosAndAppraisal.getShortDesc()
						+ " is displayed");

		softAssert.assertAll();

	}
}

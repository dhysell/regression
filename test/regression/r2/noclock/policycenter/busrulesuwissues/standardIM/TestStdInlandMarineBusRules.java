package regression.r2.noclock.policycenter.busrulesuwissues.standardIM;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.CoverageType;
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
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.PersonalProperty;
import repository.gw.generate.custom.PersonalPropertyList;
import repository.gw.generate.custom.PersonalPropertyScheduledItem;
import repository.gw.generate.custom.StandardInlandMarine;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_FarmEquipment;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_PersonalProperty;
import repository.pc.workorders.generic.GenericWorkorderStandardIMCoverageSelection;
import repository.pc.workorders.generic.GenericWorkorderStandardIMFarmEquipment;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
@QuarantineClass
public class TestStdInlandMarineBusRules extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObjPL = null;
	SoftAssert softAssert = new SoftAssert();

	@BeforeMethod(alwaysRun = true)
	public void beforeMethod() {
		softAssert = new SoftAssert();
	}

	@Test()
	public void testStdIMBusRulesFA() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ArrayList<InlandMarine> inlandMarineCoverageSelection_PL_IM = new ArrayList<InlandMarine>();
		inlandMarineCoverageSelection_PL_IM.add(InlandMarine.PersonalProperty);
		inlandMarineCoverageSelection_PL_IM.add(InlandMarine.FarmEquipment);
		// Farm Equipment
		IMFarmEquipmentScheduledItem scheduledItem1 = new IMFarmEquipmentScheduledItem("Circle Sprinkler",
				"Manly Farm Equipment", 1000);
		ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
		farmEquip.add(scheduledItem1);
		FarmEquipment imFarmEquip1 = new FarmEquipment(IMFarmEquipmentType.FarmEquipment, CoverageType.BroadForm,
				IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
		ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
		allFarmEquip.add(imFarmEquip1);

		// PersonalProperty
		PersonalProperty pprop = new PersonalProperty();
		pprop.setType(PersonalPropertyType.SportingEquipment);
		pprop.setLimit(21000);
		pprop.setDeductible(PersonalPropertyDeductible.Ded1000);
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
		pprop.setScheduledItems(sportsScheduledItems);

		PersonalPropertyList ppropList = new PersonalPropertyList();
		ppropList.setSportingEquipment(pprop);

        StandardInlandMarine myStandardInlandMarine = new StandardInlandMarine();
        myStandardInlandMarine.inlandMarineCoverageSelection_Standard_IM = inlandMarineCoverageSelection_PL_IM;
        myStandardInlandMarine.farmEquipment = allFarmEquip;
        myStandardInlandMarine.personalProperty_PL_IM = ppropList.getAllPersonalPropertyAsList();

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardIM)
                .withLineSelection(LineSelection.StandardInlandMarine)
                .withStandardInlandMarine(myStandardInlandMarine)
                .withInsFirstLastName("IMBus", "Rules")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.FullApp);
	}

	@Test(dependsOnMethods = { "testStdIMBusRulesFA" })
	private void testvalidateStdIMBusRules() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter);

		new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				this.myPolicyObjPL.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();
		new GuidewireHelpers(driver).editPolicyTransaction();

		// IM010 - Personal Property equal to over $20,000
		sideMenu.clickSideMenuStandardIMPersonalProperty();
		GenericWorkorderSquireInlandMarine_PersonalProperty personalProperty = new GenericWorkorderSquireInlandMarine_PersonalProperty(driver);
		personalProperty.clickEditButton();
		personalProperty.setScheduledItemLimit(21000);
		personalProperty.clickOk();

		// IM019 - Farm Equipment Broad Form
		sideMenu.clickSideMenuStandardIMFarmEquipment();
		IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment",
				"Manly Farm Equipment", 1000);
		GenericWorkorderSquireInlandMarine_FarmEquipment farmEquipment = new GenericWorkorderSquireInlandMarine_FarmEquipment(driver);
		farmEquipment.clickAdd();
        List<String> allValues = farmEquipment.getTypeValues();
		for (String currentValue : allValues) {
			softAssert.assertFalse(currentValue.equals(IMFarmEquipmentType.FarmEquipment.getValue()),
					"Policy can have more than Farm Equip w/ Broad Form, Farm Equip w/ Special Form");
		}
		farmEquipment.setType(IMFarmEquipmentType.MovableSetSprinkler);
		farmEquipment.setDeductible(IMFarmEquipmentDeductible.Fifty);
		farmEquipment.setInspected(true);
		farmEquipment.setExistingDamage(false);
		farmEquipment.setScheduledItem(farmThing);
		farmEquipment.clickOk();

		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if (quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();

		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();

		softAssert.assertFalse(
				!uwIssues.isInList(PLUWIssues.PersonalPropertyEqualOver20K.getLongDesc())
						.equals(UnderwriterIssueType.BlockSubmit),
				"Expected error Bind Issuance : " + PLUWIssues.PersonalPropertyEqualOver20K.getShortDesc()
						+ " is not displayed");

		softAssert.assertAll();

	}

	@Test()
	private void testStdIMChangeValidations() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ArrayList<InlandMarine> inlandMarineCoverageSelection_PL_IM = new ArrayList<InlandMarine>();
		inlandMarineCoverageSelection_PL_IM.add(InlandMarine.PersonalProperty);
		inlandMarineCoverageSelection_PL_IM.add(InlandMarine.FarmEquipment);
		// Farm Equipment
        IMFarmEquipmentScheduledItem scheduledItem1 = new IMFarmEquipmentScheduledItem("Circle Sprinkler", "Manly Farm Equipment", 1000);
		ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
		farmEquip.add(scheduledItem1);
        FarmEquipment imFarmEquip1 = new FarmEquipment(IMFarmEquipmentType.FarmEquipment, CoverageType.BroadForm, IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
		ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
		allFarmEquip.add(imFarmEquip1);

		// PersonalProperty
		PersonalProperty pprop = new PersonalProperty();
		pprop.setType(PersonalPropertyType.SportingEquipment);
		pprop.setLimit(21000);
		pprop.setDeductible(PersonalPropertyDeductible.Ded1000);
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
		pprop.setScheduledItems(sportsScheduledItems);

		PersonalPropertyList ppropList = new PersonalPropertyList();
		ppropList.setSportingEquipment(pprop);

        StandardInlandMarine myStandardInlandMarine = new StandardInlandMarine();
        myStandardInlandMarine.inlandMarineCoverageSelection_Standard_IM = inlandMarineCoverageSelection_PL_IM;
        myStandardInlandMarine.farmEquipment = allFarmEquip;
        myStandardInlandMarine.personalProperty_PL_IM = ppropList.getAllPersonalPropertyAsList();

        GeneratePolicy myPolNewObjPL = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardIM)
                .withLineSelection(LineSelection.StandardInlandMarine)
                .withStandardInlandMarine(myStandardInlandMarine)
                .withInsFirstLastName("IMBus", "Rules")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
        driver.quit();

		// IM015 - Inland Marine Coverage change
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolNewObjPL.standardInlandMarine.getPolicyNumber());

		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);

		// start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", currentSystemDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuStandardIMFarmEquipment();
		GenericWorkorderStandardIMFarmEquipment farmEquipmentPage = new GenericWorkorderStandardIMFarmEquipment(driver);
		farmEquipmentPage.clickFarmEqipmentTableSpecificCheckbox(1, true);
		farmEquipmentPage.ClickRemove();

		sideMenu.clickSideMenuStandardIMCoverageSelection();
		GenericWorkorderStandardIMCoverageSelection imSelection = new GenericWorkorderStandardIMCoverageSelection(driver);
		imSelection.checkCoverage(false, InlandMarine.FarmEquipment.getValue());

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

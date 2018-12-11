package previousProgramIncrement.pi2_062818_090518.f284_AutoPolicyChange;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.FPP.FPPCoverageTypes;
import repository.gw.enums.FPP.FPPDeductible;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.LivestockScheduledItemType;
import repository.gw.enums.PLUWIssues;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireFPP;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquireLiablityCoverageLivestockItem;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfoAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author nvadlamudi
 * @Requirement :US15864: Disable/change UW Issues - Section 1 & 2 (other
 *              changes)
 * @RequirementsLink <a href=
 *                   "https://fbmicoi.sharepoint.com/:x:/r/sites/ARTists2/_layouts/15/Doc.aspx?sourcedoc=%7B45AD1E27-B290-4BBD-AD76-BC67A17BD898%7D&file=UW%20Issues%20to%20Change%20for%20Auto%20Issue.xlsx&action=default&mobileredirect=true">
 *                   UW Issues to Change for Auto Issue</a>
 * @Description :Validate PR022, PR023, PR057, PR059, PR095 Section I & II UW
 *              Issue changes
 * @DATE Aug 3, 2018
 */
public class US15864ChangeSection1And2UWIssues extends BaseTest {
	SoftAssert softAssert = new SoftAssert();
	WebDriver driver;

	@Test
	public void testCheckSectionIAnd2UWIssueChangesInPolicyChange() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs = new ArrayList<PolicyInfoAdditionalNamedInsured>();
		listOfANIs.add(new PolicyInfoAdditionalNamedInsured(ContactSubType.Person,
				"Remove" + StringsUtils.generateRandomNumberDigits(8),
				"ANI" + StringsUtils.generateRandomNumberDigits(8), AdditionalNamedInsuredType.Spouse,
				new AddressInfo(true)) {
			{
				this.setNewContact(CreateNew.Create_New_Always);
			}
		});

		SquireFPP squireFPP = new SquireFPP(FPPFarmPersonalPropertySubTypes.Horses,
				FPPFarmPersonalPropertySubTypes.Seed, FPPFarmPersonalPropertySubTypes.Hay,
				FPPFarmPersonalPropertySubTypes.Beans);
		squireFPP.setCoverageType(FPPCoverageTypes.BlanketInclude);
		squireFPP.setDeductible(FPPDeductible.Ded_1000);

		SquireLiability liability = new SquireLiability();
		SquireLiablityCoverageLivestockItem liabLivestockHorse = new SquireLiablityCoverageLivestockItem();
		liabLivestockHorse.setQuantity(100);
		liabLivestockHorse.setType(LivestockScheduledItemType.Horse);

		ArrayList<SquireLiablityCoverageLivestockItem> coveredLivestockItems = new ArrayList<SquireLiablityCoverageLivestockItem>();
		coveredLivestockItems.add(liabLivestockHorse);

		SectionIICoverages livestockCoverage = new SectionIICoverages(SectionIICoveragesEnum.Livestock,
				coveredLivestockItems);
		liability.getSectionIICoverageList().add(livestockCoverage);

		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.squireFPP = squireFPP;
		myPropertyAndLiability.liabilitySection = liability;

		Squire mySquire = new Squire(SquireEligibility.Country);
		mySquire.propertyAndLiability = myPropertyAndLiability;

		GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver).withSquire(mySquire)
				.withProductType(ProductLineType.Squire).withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("Section1", "Change").withANIList(listOfANIs)
				.withSocialSecurityNumber("6" + StringsUtils.generateRandomNumberDigits(8))
				.withPaymentPlanType(PaymentPlanType.Annual).withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);

		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObjPL.agentInfo.getAgentUserName(),
				myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);

		// start policy change
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change",
				DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));

		// PR022 Removing or Change to Livestock
		// PR023 Removing or Change to Commodities
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquirePropertyCoverages();
		GenericWorkorderSquirePropertyAndLiabilityCoverages propCoverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(
				driver);
		propCoverages.clickFarmPersonalProperty();
		GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages fpp = new GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages(
				driver);
		fpp.checkCoverageD(false);

		// PR057 PNI or ANI removed
		sideMenu.clickSideMenuPolicyInfo();
		GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.removeANI(myPolicyObjPL.aniList.get(0).getPersonFirstName());

		// PR059 - Policy member contact change
		sideMenu.clickSideMenuHouseholdMembers();
		GenericWorkorderPolicyMembers hmember = new GenericWorkorderPolicyMembers(driver);
		hmember.clickPolicyHolderMembersByName(myPolicyObjPL.pniContact.getFirstName());
		GenericWorkorderPolicyInfoAdditionalNamedInsured editANIPage = new GenericWorkorderPolicyInfoAdditionalNamedInsured(
				driver);
		editANIPage.setEditAdditionalNamedInsuredDateOfBirth(DateUtils.dateAddSubtract(
				DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -24));
		editANIPage.clickOK();
		hmember.clickRemoveMember(myPolicyObjPL.aniList.get(0).getPersonFirstName());

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
		List<PLUWIssues> sectionIIIBlockInfo = new ArrayList<PLUWIssues>() {
			{
				this.add(PLUWIssues.PNIOrANIRemoved);
				this.add(PLUWIssues.RemovingChangeToLivestockFPP);
				this.add(PLUWIssues.RemovingChangeToCommoditiesFPP);
			}
		};

		for (PLUWIssues uwBlockBindExpected : sectionIIIBlockInfo) {
			softAssert.assertFalse(
					!uwIssues.isInList(uwBlockBindExpected.getLongDesc()).equals(UnderwriterIssueType.BlockQuote),
					"Policy Change - Expected UW Quote : " + uwBlockBindExpected.getShortDesc() + " is not displayed");
		}

		softAssert.assertFalse(
				!uwIssues.isInList(PLUWIssues.AContactChanged.getLongDesc()).equals(UnderwriterIssueType.Informational),
				"Expected UW Informational : " + PLUWIssues.AContactChanged.getShortDesc() + " is not displayed");

		softAssert.assertAll();

	}

	@Test
	public void testCheckSectionIAnd2UWIssueChangesInSubmission() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Squire mySquire = new Squire(SquireEligibility.Country);

		GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver).withSquire(mySquire)
				.withProductType(ProductLineType.Squire).withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("Section1", "Submi").isDraft().build(GeneratePolicyType.FullApp);

		new Login(driver).loginAndSearchSubmission(myPolicyObjPL);

		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuRiskAnalysis();
		GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		// PR095 CLUE Property not ordered
		risk.Quote();
		GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
		}

		sideMenu.clickSideMenuRiskAnalysis();
		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();

		softAssert.assertFalse(
				!uwIssues.isInList(PLUWIssues.CluePropertyNotOrder.getLongDesc())
						.equals(UnderwriterIssueType.BlockSubmit),
				"Submission - Expected UW Block Submit : " + PLUWIssues.CluePropertyNotOrder.getShortDesc()
						+ " is not displayed");

		softAssert.assertAll();

	}
}

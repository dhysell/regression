package previousProgramIncrement.pi2_062818_090518.f284_AutoPolicyChange;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.FPP.FPPCoverageTypes;
import repository.gw.enums.FPP.FPPDeductible;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.LivestockScheduledItemType;
import repository.gw.enums.OrganizationTypePL;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireFPP;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquireLiablityCoverageLivestockItem;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_PriorLosses;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages;

import java.util.ArrayList;

/**
 * @Author nvadlamudi
 * @Requirement :US15557: Enable auto-issuance of agent-submitted policy changes
 *              (whether by button or whatever) - Squire
 * @RequirementsLink <a href=
 *                   "https://rally1.rallydev.com/#/203558458764d/detail/userstory/232665128700">
 *                   PC8 - Personal Lines - Auto Issuance</a>
 * @Description : validate agent can auto-issue a policy change on City Squire
 *              and Country Squire policies
 * @DATE Aug 13, 2018
 */
public class US15557SquireAgentAutoSubmitPolicyChanges extends BaseTest {
	private String activityName = "Auto-Issued Policy Change for UW Review";
	@Test
	public void testSquireAgentAutoSubmitPolicyChange() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
		Squire mySquire = new Squire(SquireEligibility.City);

		GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver).withSquire(mySquire)
				.withProductType(ProductLineType.Squire).withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("Submit", "Change")
				.withPaymentPlanType(PaymentPlanType.Annual).withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObjPL.agentInfo.getAgentUserName(),
				myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);

		// start policy change
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change",
				DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));

		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();
		GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);

		// SQ004 - Squire Organization type of other
		policyInfo.setPolicyInfoOrganizationType(OrganizationTypePL.Other);
		policyInfo.setPolicyInfoDescription("Testing Description");

		sideMenu.clickSideMenuRiskAnalysis();
		GenericWorkorderRiskAnalysis_PriorLosses risk = new GenericWorkorderRiskAnalysis_PriorLosses(driver);
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		risk.Quote();
		GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();
		Assert.assertTrue(policyChangePage.checkIssuePolicy(),
				"Agent Login - Expected Issue policy button does not exists.");
		policyChangePage.clickIssuePolicy();
		GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
		completePage.clickPolicyNumber();
		PolicySummary polSummary = new PolicySummary(driver);
		Assert.assertNotNull(polSummary.getCompletedTransactionNumberByType(TransactionType.Policy_Change),
				"Agent Auto Issue  - completed policy change was found.");
		Assert.assertTrue(polSummary.checkIfActivityExists(activityName), "Expected Activity : '"+activityName+"' is not displayed.");

	}

	@Test
	public void testSquireAgentAutoIssuePolicyChangeAfterUW() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);

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
				.withInsFirstLastName("Submit", "Change").withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash).build(GeneratePolicyType.PolicyIssued);

		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObjPL.agentInfo.getAgentUserName(),
				myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);

		// start policy change
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", myPolicyObjPL.squire.getEffectiveDate());

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

		sideMenu.clickSideMenuRiskAnalysis();
		GenericWorkorderRiskAnalysis_PriorLosses risk = new GenericWorkorderRiskAnalysis_PriorLosses(driver);
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		risk.Quote();
		GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();
		Assert.assertTrue(!policyChangePage.checkIssuePolicy(),
				"Agent Login - Unexpected Issue policy button eventhough Block Issues exists.");
		risk.performRiskAnalysisAndQuote(myPolicyObjPL);
		sideMenu.clickSideMenuRiskAnalysis();
		Assert.assertTrue(policyChangePage.checkIssuePolicy(),
				"Agent Login - Expected Issue policy button does not exists.");
		policyChangePage.clickIssuePolicy();
		GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
		completePage.clickPolicyNumber();
		PolicySummary polSummary = new PolicySummary(driver);
		Assert.assertNotNull(polSummary.getCompletedTransactionNumberByType(TransactionType.Policy_Change),
				"Agent Auto Issue  - completed policy change was found.");
		
		Assert.assertTrue(polSummary.checkIfActivityExists(activityName), "Expected Activity : '"+activityName+"' is not displayed.");

	}
}

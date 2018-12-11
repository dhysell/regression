package previousProgramIncrement.pi2_062818_090518.f284_AutoPolicyChange;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.FPP.FPPCoverageTypes;
import repository.gw.enums.FPP.FPPDeductible;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.LivestockScheduledItemType;
import repository.gw.enums.PLUWIssues;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.TransactionType;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireFPP;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquireLiablityCoverageLivestockItem;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;

/**
 * @Author nvadlamudi
 * @Requirement : US16026: Change PR080 to Block Submit UW Issue US16075: Change
 *              PR080 to Informational UW Issue and trigger auto-issue activity
 * @RequirementsLink <a href=
 *                   "https://rally1.rallydev.com/#/203558458764/detail/userstory/246600506764">
 *                   Personal Line Product Model</a>
 * @Description : validate Squire and Standard fire policies for Risk and
 *              category UW Issues. PR080 to show up as an informational UW
 *              issue
 * @DATE Aug 21, 2018
 */
public class US16026ChangePR080BlockSubmitUWIssue extends BaseTest {
	private String activityName = "Auto-Issued Submission for UW Review";
	private WebDriver driver;

	@Test
	public void testSquirePR080UWIssue() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

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
				.withInsFirstLastName("PR080", "UWIssue").isDraft().build(GeneratePolicyType.FullApp);

		checkUWReviewActivity(myPolicyObjPL);

	}

//	@Test
//	public void testStdFirePR080UWIssue() throws Exception {
//
//		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
//		driver = buildDriver(cf);
//
//		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
//		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
//
//		PLPolicyLocationProperty property = new PLPolicyLocationProperty();
//		property.setpropertyType(PropertyTypePL.DwellingPremises);
//
//		locOnePropertyList.add(property);
//		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
//		locToAdd.setPlNumResidence(12);
//		locToAdd.setPlNumAcres(12);
//		locationsList.add(locToAdd);
//
//		GeneratePolicy stdFirePolicyObj = new GeneratePolicy.Builder(driver)
//				.withProductType(ProductLineType.StandardFire).withLineSelection(LineSelection.StandardFirePL)
//				.withCreateNew(CreateNew.Create_New_Always).withInsPersonOrCompany(ContactSubType.Person)
//				.withInsFirstLastName("PR080", "StdFire").withPolicyLocations(locationsList).isDraft()
//				.build(GeneratePolicyType.FullApp);
//		checkUWReviewActivity(stdFirePolicyObj);
//
//	}

	public void checkUWReviewActivity(GeneratePolicy pol) throws Exception {
		new Login(driver).loginAndSearchSubmission(pol.agentInfo.getAgentUserName(), pol.agentInfo.getAgentPassword(),
				pol.accountNumber);
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

		Assert.assertTrue(uwIssues.isInList(PLUWIssues.RiskOnAPropertyOrFPPBlank.getLongDesc()).equals(UnderwriterIssueType.Informational),
				"Expected Block Informational : " + PLUWIssues.RiskOnAPropertyOrFPPBlank.getShortDesc()	+ " is not displayed");

		if(uwIssues.getBlockSubmitList().size() > 0 || uwIssues.getBlockIssuanceList().size() > 0 || uwIssues.getBlockQuoteList().size() > 0){
			new GenericWorkorderRiskAnalysis_UWIssues(driver).handleBlockSubmit(pol);
		}
		sideMenu.clickSideMenuPayment();
		GenericWorkorderPayment payment = new GenericWorkorderPayment(driver);
		payment.fillOutPaymentPage(pol);
		sideMenu.clickSideMenuForms();
		Assert.assertTrue(payment.SubmitOptionsButtonExists(),
				"Auto Issuance - Agent can't have Submit Options to submit a policy..");
		Assert.assertTrue(payment.checkGenericWorkorderSubmitOptionsIssuePolicyOption(),
				"Auto Issuance - Agent can't see Submit Options -> Issue Policy option to issue a policy..");
		sideMenu.clickSideMenuPayment();
		payment.clickGenericWorkorderSubmitOptionsIssuePolicy();
		GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
		completePage.clickPolicyNumber();
		PolicySummary polSummary = new PolicySummary(driver);
		Assert.assertNotNull(polSummary.getCompletedTransactionNumberByType(TransactionType.Issuance),
				"Agent Auto Issue  - completed Issuance job not found.");
		Assert.assertTrue(polSummary.checkIfActivityExists(activityName),
				"Expected Activity : '" + activityName + "' is not displayed.");

	}
}

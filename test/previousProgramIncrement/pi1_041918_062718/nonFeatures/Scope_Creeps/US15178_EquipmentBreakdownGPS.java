package previousProgramIncrement.pi1_041918_062718.nonFeatures.Scope_Creeps;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.FPP.FPPCoverageTypes;
import repository.gw.enums.FPP.FPPDeductible;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.FPP.FarmPersonalPropertyTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IssuanceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderSquireFarmPersonalProperty;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;

/**
 * @Author ecoleman
 * @Requirement 
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558461772d/detail/userstory/221598676120">US15178</a>
 * @Description
 * 
MOST WORK WAS DONE ON US15159 (RELEASING ON 6-5-18) NEED TO TEST THE EQUIPMENT BREAKDOWN ON RAFAEL & BRETT LOCAL. WILL CHECK WITH RAFAEL TO SEE IF ADDED TO HIS WORK

As an agent/PA or home office user I want to be able to find GPS under Tools on FPP and have equipment breakdown total include Tools when coverage is on policy.

Steps to get there:

    New submission F&R with Equipment breakdown coverage and FPP with Tools.
    Under Tools add GPS. Screenshot total of Equipment Breakdown showing tools/GPS is included.
    Issue
    Into term do policy change removing GPS. Make sure total from GPS is taken off the total in equipment breakdown

Acceptance criteria:

    Test FPP: Machinery should no longer have GPS in the drop down selection. It should be in the Tools drop down now.
    The value on tools needs to always be included in the Equipment Breakdown totals.
    
 * @DATE June 7, 2018
 */

public class US15178_EquipmentBreakdownGPS extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObject = null;

	public void generatePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		myPolicyObject = new GeneratePolicy.Builder(driver)
					.withProductType(ProductLineType.Squire)
					.withSquireEligibility(SquireEligibility.FarmAndRanch)
					.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
					.withDownPaymentType(PaymentType.Cash)
					.withPolOrgType(OrganizationType.Individual)
					.withInsFirstLastName("ScopeCreeps", "NonFeature")
					.isDraft()
					.build(GeneratePolicyType.FullApp);	
		driver.quit();
	}

	@Test(enabled = true)
	public void EnsureGPSOnToolsNotMachinery() throws Exception {
		generatePolicy();
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber("msanchez", "gw", myPolicyObject.accountNumber);

		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
		GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages pcPropertyDetailCoveragesPage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
		GenericWorkorderSquireFarmPersonalProperty pcFarmPersonalPropertyPage = new GenericWorkorderSquireFarmPersonalProperty(driver);
		SoftAssert softAssert = new SoftAssert();
		GenericWorkorderRiskAnalysis_UWIssues pcRiskUnderwriterIssuePage = new GenericWorkorderRiskAnalysis_UWIssues(driver);
		GenericWorkorderRiskAnalysis pcRiskAnalysisWorkorder = new GenericWorkorderRiskAnalysis(driver);
		GenericWorkorder pcWorkorder = new GenericWorkorder(driver);
		GenericWorkorderPayment pcPaymentWorkorder = new GenericWorkorderPayment(driver);
		GenericWorkorderComplete pcCompleteWorkorder = new GenericWorkorderComplete(driver);
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		GenericWorkorderSquirePropertyAndLiabilityCoverages pcAddtionalCoveragesWorkorder = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
		
		
		pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);
		pcSideMenu.clickSideMenuSquirePropertyCoverages();
		double EquipmentBreakdownLimit = pcPropertyDetailCoveragesPage.getEquipmentBreakdownLimit();
		softAssert.assertEquals(EquipmentBreakdownLimit, 0.0, "Equipment Breakdown limit should start at 0!");
		pcPropertyDetailCoveragesPage.clickFarmPersonalProperty();
		pcFarmPersonalPropertyPage.checkCoverageD(true);
		pcFarmPersonalPropertyPage.selectCoverages(FarmPersonalPropertyTypes.Tools);
		pcAddtionalCoveragesWorkorder.setFarmPersonalPropertyRisk("A");
		pcFarmPersonalPropertyPage.addItem(FPPFarmPersonalPropertySubTypes.GPS, 1, 1000, "GPS #1");
		String toolsItemTypeText = pcFarmPersonalPropertyPage.getItemTypeByPropertyTypeAndRowNumber(FarmPersonalPropertyTypes.Tools, 1);
		
		softAssert.assertTrue(toolsItemTypeText.contains("GPS"), "GPS was not found under tools, it should be here");
		
		pcFarmPersonalPropertyPage.selectCoverageType(FPPCoverageTypes.BlanketInclude);
		pcFarmPersonalPropertyPage.selectDeductible(FPPDeductible.Ded_500);
		pcSideMenu.clickSideMenuRiskAnalysis();
		
		pcSideMenu.clickSideMenuSquirePropertyCoverages();
		
		softAssert.assertEquals(pcPropertyDetailCoveragesPage.getEquipmentBreakdownLimit(), EquipmentBreakdownLimit + 1000, "After adding GPS worth 1000, the Equipment Breakdown limit should be 1000 higher than when it started!");
		
		pcSideMenu.clickSideMenuRiskAnalysis();
		pcWorkOrder.clickGenericWorkorderQuote();
		pcSideMenu.clickSideMenuRiskAnalysis();
		pcRiskAnalysisWorkorder.approveAll();
		pcRiskAnalysisWorkorder.clickReleaseLock();		
		
		new GuidewireHelpers(driver).logout();
		new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.agentUserName, myPolicyObject.agentInfo.agentPassword, myPolicyObject.accountNumber);
		pcAccountSummaryPage.clickCurrentActivitiesSubject("Underwriter has reviewed this job");
		pcSideMenu.clickSideMenuPayment();
		pcPaymentWorkorder.makeDownPayment(PaymentPlanType.Annual, PaymentType.Cash, 2000.0);
		pcWorkOrder.clickGenericWorkorderSaveDraft();

		new GuidewireHelpers(driver).logout();
		new Login(driver).loginAndSearchAccountByAccountNumber("msanchez", "gw", myPolicyObject.accountNumber);
		pcAccountSummaryPage.clickCurrentActivitiesSubject("Underwriter has reviewed this job");
		pcRiskUnderwriterIssuePage.clickComplete();
		while (pcWorkorder.checkGenericWorkorderNextExists())
			new GuidewireHelpers(driver).clickNext();
		new GuidewireHelpers(driver).logout();
		new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.agentUserName, myPolicyObject.agentInfo.agentPassword, myPolicyObject.accountNumber);
		pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);
		while (pcWorkorder.checkGenericWorkorderNextExists())
			new GuidewireHelpers(driver).clickNext();
		pcWorkOrder.clickGenericWorkorderSubmitOptionsSubmit();
		new GuidewireHelpers(driver).logout();
		new Login(driver).loginAndSearchAccountByAccountNumber("msanchez", "gw", myPolicyObject.accountNumber);
		pcAccountSummaryPage.clickCurrentActivitiesSubject("Submitted Full Application");

		pcWorkOrder.clickGenericWorkorderQuote();
		pcWorkorder.clickGenericWorkorderIssue(IssuanceType.FollowedByPolicyChange);

		pcCompleteWorkorder.clickViewYourPolicy();
		policyChangePage.startPolicyChange("Change#1 Remove location", DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));
		
		pcSideMenu.clickSideMenuSquirePropertyCoverages();
		EquipmentBreakdownLimit = pcPropertyDetailCoveragesPage.getEquipmentBreakdownLimit();
		pcPropertyDetailCoveragesPage.clickFarmPersonalProperty();
		pcFarmPersonalPropertyPage.checkCoverageD(false);		
		
		pcSideMenu.clickSideMenuSquirePropertyCoverages();
		softAssert.assertEquals(pcPropertyDetailCoveragesPage.getEquipmentBreakdownLimit(), EquipmentBreakdownLimit - 1000, "After removing GPS worth 1000, the Equipment Breakdown limit should be 1000 lower than before!");
		
		softAssert.assertAll();
	}




}





















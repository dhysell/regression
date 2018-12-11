package regression.businessowners.noclock.policycenter;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.Cancellation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PolicyTermStatus;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.rewrite.StartRewrite;

/**
 * 
 * @Author ecoleman
 * @Requirement BOP Policy Change
 * @Description testing to make sure that a BOP RFT works
 * @DATE May 25, 2018
 */

public class BOP_RewriteFullTerm extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObject = null;

	public void generatePolicy() throws Exception {
		myPolicyObject = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withPolOrgType(OrganizationType.Partnership)
				.withProductType(ProductLineType.Businessowners)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.Apartments))
				.withCreateNew(CreateNew.Create_New_Always)
				.withInsCompanyName("MMM BOP")
				.withPaymentPlanType(PaymentPlanType.getRandom())
				.withDownPaymentType(PaymentType.getRandom())
				.withLineSelection(LineSelection.Businessowners)
				.build(GeneratePolicyType.PolicyIssued);
	}
	
	@Test(enabled=true)
	public void verifyBOPPolicyRewriteFullTerm() throws Exception  {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		generatePolicy();
		
		new Login(driver).loginAndSearchAccountByAccountNumber("msanchez", "gw", myPolicyObject.accountNumber);

        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
		SoftAssert softAssert = new SoftAssert();
        GenericWorkorderComplete pcWorkorderCompletePage = new GenericWorkorderComplete(driver);
        PolicyMenu pcPolicyMenu = new PolicyMenu(driver);
        StartCancellation pcCancellationPage = new StartCancellation(driver);
        StartRewrite pcRewriteWorkOrder = new StartRewrite(driver);
        GenericWorkorderBuildings pcBuildingsPage = new GenericWorkorderBuildings(driver);
        PolicySummary pcPolicysummaryPage = new PolicySummary(driver);
		double newBuildingLimit = 260000;

		pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(PolicyTermStatus.InForce);
		pcPolicyMenu.clickMenuActions();
		pcPolicyMenu.clickCancelPolicy();
		pcCancellationPage.setSourceReasonAndExplanation(Cancellation.CancellationSourceReasonExplanation.Rewritten);
		pcCancellationPage.clickStartCancellation();
		pcCancellationPage.clickSubmitOptionsCancelNow();
		pcWorkorderCompletePage.clickViewYourPolicy();
		
		pcPolicyMenu.clickMenuActions();
		pcPolicyMenu.clickRewriteFullTerm();
		
	    pcSideMenu.clickSideMenuBuildings();
	    pcBuildingsPage.clickEdit();
	    pcBuildingsPage.setBuildingLimit(newBuildingLimit);
	    pcBuildingsPage.clickOK();
	    
	    pcRewriteWorkOrder.visitAllPages(myPolicyObject); // 5/23 CHECK THIS STEP!
		pcSideMenu.clickSideMenuRiskAnalysis();
		pcWorkOrder.clickGenericWorkorderQuote();
		pcWorkOrder.clickIssuePolicyButton();
		pcWorkorderCompletePage.clickViewYourPolicy();
		pcPolicysummaryPage.clickCompletedTransactionByType(TransactionType.Rewrite_Full_Term);		
		pcSideMenu.clickSideMenuBuildings();
		pcBuildingsPage.clickBuildingViewLinkByBuildingNumber(1);
		
		softAssert.assertEquals(pcBuildingsPage.getBuildingLimitAmount(), newBuildingLimit, "Changed building limit, the change did not work.");
		
		softAssert.assertAll();
	}
}

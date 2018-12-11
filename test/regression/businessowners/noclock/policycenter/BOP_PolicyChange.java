package regression.businessowners.noclock.policycenter;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import repository.gw.enums.BusinessownersLine.SmallBusinessType;
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
import repository.gw.helpers.DateUtils;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderComplete;

/**
 * 
 * @Author ecoleman
 * @Requirement BOP Policy Change
 * @Description testing to make sure that a BOP policy issuance works
 * @DATE May 24, 2018
 */

public class BOP_PolicyChange extends BaseTest {
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
	public void verifyBOPPolicyChange() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		generatePolicy();
		
		new Login(driver).loginAndSearchAccountByAccountNumber("msanchez", "gw", myPolicyObject.accountNumber);

		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
		AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
		SoftAssert softAssert = new SoftAssert();
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		GenericWorkorderBuildings pcBuildingsPage = new GenericWorkorderBuildings(driver);
		PolicySummary pcPolicysummaryPage = new PolicySummary(driver);
		GenericWorkorderComplete pcWorkorderCompletePage = new GenericWorkorderComplete(driver);
	    double newBuildingLimit = 260000;
	    
		
		pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(PolicyTermStatus.InForce);
	    policyChangePage.startPolicyChange("Making change like one of those machines at an arcade.", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
	    
	    pcSideMenu.clickSideMenuBuildings();
	    pcBuildingsPage.clickEdit();
	    pcBuildingsPage.setBuildingLimit(newBuildingLimit);
	    pcBuildingsPage.clickOK();
	    
		pcWorkOrder.clickGenericWorkorderQuote();
		pcSideMenu.clickSideMenuRiskAnalysis();
		pcWorkOrder.clickIssuePolicyButton();
		pcWorkorderCompletePage.clickViewYourPolicy();
		pcPolicysummaryPage.clickCompletedTransactionByType(TransactionType.Policy_Change);		
		pcSideMenu.clickSideMenuBuildings();
		pcBuildingsPage.clickBuildingViewLinkByBuildingNumber(1);
		
		softAssert.assertEquals(pcBuildingsPage.getBuildingLimitAmount(), newBuildingLimit, "Changed building limit, the change did not work.");
		
		softAssert.assertAll();
	}
}

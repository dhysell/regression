package scratchpad.evan;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.Cancellation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
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
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.WaitUtils;
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
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.rewrite.StartRewrite;

/**
 * @Author ecoleman
 * @Requirement BOP Policy Change
 * @Description testing to make sure that a BOP RNT works
 * @DATE May 25, 2018
 */

@Test(groups = {"ClockMove"})
public class BOP_RewriteNewTerm extends BaseTest {

    private GeneratePolicy myPolicyObject = null;
    private WaitUtils pageWaitUtils = null;
    private WebDriver driver;

    public void generatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
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
                .withPolEffectiveDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -40))
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(enabled = true)
    public void verifyBOPPolicyRewriteNewTerm() throws Exception {

        try {
            generatePolicy();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unable to generate policy. Test cannot continue.");
        }

        new GuidewireHelpers(driver).logout();

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
        GenericWorkorderQualification pcQualificationPage = new GenericWorkorderQualification(driver);
        double newBuildingLimit = 260000;
        pageWaitUtils = new WaitUtils(driver);

        pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(PolicyTermStatus.InForce);
        pcPolicyMenu.clickMenuActions();
        pcPolicyMenu.clickCancelPolicy();
        pcCancellationPage.setSourceReasonAndExplanation(Cancellation.CancellationSourceReasonExplanation.FarmBureau);
        pcCancellationPage.clickStartCancellation();
        pcCancellationPage.clickSubmitOptionsCancelNow();
        pcWorkorderCompletePage.clickViewYourPolicy();

        // Clock move 2 days since it has to be >=1 <=30 days from cancel for remainder
        ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);

        pcPolicyMenu.clickMenuActions();
//		5/25/18: No RNT option!
        pcPolicyMenu.clickRewriteNewTerm();
        pcRewriteWorkOrder.clickNext();

        pcSideMenu.clickSideMenuQualification();
        pcQualificationPage.fillOutFullAppQualifications(myPolicyObject);

        pcSideMenu.clickSideMenuBuildings();
        pcBuildingsPage.clickEdit();
        pcBuildingsPage.setBuildingLimit(newBuildingLimit);
        pcBuildingsPage.clickOK();

        pcRewriteWorkOrder.visitAllPages(myPolicyObject); // 5/23 CHECK THIS STEP!
        pcSideMenu.clickSideMenuRiskAnalysis();
        pcWorkOrder.clickGenericWorkorderQuote();
        pcWorkOrder.clickIssuePolicyButton();
        pageWaitUtils.waitForPostBack();
        pcWorkorderCompletePage.clickViewYourPolicy();
        pcPolicysummaryPage.clickCompletedTransactionByType(TransactionType.Rewrite_New_Term);
        pcSideMenu.clickSideMenuBuildings();
        pcBuildingsPage.clickBuildingViewLinkByBuildingNumber(1);

        softAssert.assertEquals(pcBuildingsPage.getBuildingLimitAmount(), newBuildingLimit, "Changed building limit, the change did not work.");

        softAssert.assertAll();
    }
}

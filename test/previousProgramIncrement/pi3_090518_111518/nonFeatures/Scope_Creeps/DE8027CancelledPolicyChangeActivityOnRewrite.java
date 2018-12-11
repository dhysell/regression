package previousProgramIncrement.pi3_090518_111518.nonFeatures.Scope_Creeps;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.OkCancel;
import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.enums.OrganizationType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Activity;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyActivities;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.account.AccountSummaryPC;
import repository.pc.activity.UWActivityPC;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

import java.util.Date;
/**
 * @Author swathiAkarapu
 * @Requirement DE8027
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558454824ud/detail/defect/258888059832">DE8027</a>
 * @Description
 * Start a new submission
 * Issue
 * Cancel policy
 * issue
 * Rewrite/reinstate policy
 * issue
 * do a policy change
 * issue
 * Actual: The activity "Submitted or issued policy change on cancelled policy" is triggering even though the policy is no longer cancelled
 *
 * Expected items:
 * Ensure that the "submitted policy change" activity is only triggered if policy change is started and submitted by a CSR or an SA
 * Ensure that the "review and submit SA/CSR policy change" activity is only triggered if policy change is started and sent request approval by a CSR or an SA
 * Ensure that the "requested approval" activity is only triggered if policy change is started by an agent and agent clicks request approval. It should round robin to underwriter by area
 * Ensure that the "auto-issued policy change for UW review" activity is only triggered if policy change is started by an agent and agent uses auto-issuance and UW needs to review
 * Ensure that if an underwriter starts their own policy change or issues any policy change started by another user there should be no activity
 * @DATE October 12, 2018
 */

public class DE8027CancelledPolicyChangeActivityOnRewrite extends BaseTest {
    private WebDriver driver;
    public GeneratePolicy myPolicyObject = null;

    @Test(enabled = true)
    public void verifyCancelRewitePolicyChange_With_UW() throws Exception {
         generate();
        Underwriters underwriterInfo = UnderwritersHelper.getRandomUnderwriter();
        new Login(driver).loginAndSearchAccountByAccountNumber(underwriterInfo.getUnderwriterUserName(), underwriterInfo.getUnderwriterPassword(), myPolicyObject.accountNumber);
        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        repository.pc.workorders.generic.GenericWorkorderComplete pcWorkorderCompletePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        PolicyMenu pcPolicyMenu = new PolicyMenu(driver);
        StartCancellation pcCancellationPage = new StartCancellation(driver);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(repository.gw.enums.PolicyTermStatus.InForce);
        SoftAssert softAssert = new SoftAssert();
        cancelPolicy(pcPolicyMenu, pcCancellationPage);
        pcWorkorderCompletePage.clickViewYourPolicy();
        rewriteFullAndQuote(pcSideMenu, pcWorkOrder, pcPolicyMenu);
        pcWorkorderCompletePage.clickViewYourPolicy();
        policyChangeWithMailingAddressAndQuote(pcSideMenu, pcWorkOrder);
        pcSideMenu.clickSideMenuRiskAnalysis();
        pcWorkOrder.clickIssuePolicyButton();
        pcWorkorderCompletePage.clickViewYourPolicy();
        PolicyActivities currentActivities = new PolicyActivities(new PolicySummary(driver).getCurrentActivites_BasicInfo());
        boolean activityCancelled = isActivityFound(currentActivities, "Submitted policy change on cancelled policy");
        boolean activitySummitted = isActivityFound(currentActivities, "submitted policy change");
        softAssert.assertFalse(activitySummitted, "Submitted Activity - activity not found");
        softAssert.assertFalse(activityCancelled, "Submitted policy change on cancelled policy - activity found");
        softAssert.assertAll();
    }


    @Test(enabled = true)
    public void verifyCancelRewitePolicyChange_With_Agent_AutoIssue() throws Exception {
        generate();
        Underwriters underwriterInfo = UnderwritersHelper.getRandomUnderwriter();
        new Login(driver).loginAndSearchAccountByAccountNumber(underwriterInfo.getUnderwriterUserName(), underwriterInfo.getUnderwriterPassword(), myPolicyObject.accountNumber);
        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        repository.pc.workorders.generic.GenericWorkorderComplete pcWorkorderCompletePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        PolicyMenu pcPolicyMenu = new PolicyMenu(driver);
        StartCancellation pcCancellationPage = new StartCancellation(driver);
        SoftAssert softAssert = new SoftAssert();
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(repository.gw.enums.PolicyTermStatus.InForce);
        cancelPolicy(pcPolicyMenu, pcCancellationPage);
        pcWorkorderCompletePage.clickViewYourPolicy();
        rewriteFullAndQuote(pcSideMenu, pcWorkOrder, pcPolicyMenu);
        pcWorkorderCompletePage.clickViewYourPolicy();
        PolicyActivities beforeActivities = new PolicyActivities(new PolicySummary(driver).getCurrentActivites_BasicInfo());
        //logout and Login as Agent
        new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();

        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        Date policyChangeEffective = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), repository.gw.enums.DateAddSubtractOptions.Day, 18);
        policyChangePage.startPolicyChange("policy Change coverage Limit change", policyChangeEffective);
        pcSideMenu.clickSideMenuSquirePropertyCoverages();
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyCoverages pcSquirePropertyCoveragesPage = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyCoverages(driver);
        pcSquirePropertyCoveragesPage.selectSectionIDeductible(repository.gw.enums.Property.SectionIDeductible.OneThousand);
        pcWorkOrder.clickGenericWorkorderQuote();
        pcSideMenu.clickSideMenuRiskAnalysis();
        pcWorkOrder.clickIssuePolicyButton();
        repository.pc.workorders.generic.GenericWorkorderComplete pcCompletePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
        pcCompletePage.clickViewYourPolicy();
        PolicyActivities currentActivities = new PolicyActivities(new PolicySummary(driver).getCurrentActivites_BasicInfo());
        softAssert.assertTrue(beforeActivities.getPolicyActivityList().size() + 1 == currentActivities.getPolicyActivityList().size(), "one new Activity should Generate after Agent Auto issue But not");
        boolean activity = isActivityFound(currentActivities, "Auto-Issued Policy Change for UW Review");
        softAssert.assertTrue(activity, "Auto-Issued Policy Change for UW Review - activity found");
        softAssert.assertAll();
    }

    @Test(enabled = true)
    public void verifyCancelRewitePolicyChange_With_CSR_Submit() throws Exception {
        generate();
        Underwriters underwriterInfo = UnderwritersHelper.getRandomUnderwriter();
        new Login(driver).loginAndSearchAccountByAccountNumber(underwriterInfo.getUnderwriterUserName(), underwriterInfo.getUnderwriterPassword(), myPolicyObject.accountNumber);
        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        repository.pc.workorders.generic.GenericWorkorderComplete pcWorkorderCompletePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        PolicyMenu pcPolicyMenu = new PolicyMenu(driver);
        StartCancellation pcCancellationPage = new StartCancellation(driver);
        SoftAssert softAssert = new SoftAssert();
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(repository.gw.enums.PolicyTermStatus.InForce);
        cancelPolicy(pcPolicyMenu, pcCancellationPage);
        pcWorkorderCompletePage.clickViewYourPolicy();
        rewriteFullAndQuote(pcSideMenu, pcWorkOrder, pcPolicyMenu);
        pcWorkorderCompletePage.clickViewYourPolicy();
        PolicyActivities beforeActivities = new PolicyActivities(new PolicySummary(driver).getCurrentActivites_BasicInfo());
        //logout and Login as CSR
        new GuidewireHelpers(driver).logout();
        String CSRUsername = "cashcraft";
        new Login(driver).loginAndSearchAccountByAccountNumber(CSRUsername, "gw", myPolicyObject.accountNumber);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();
        policyChangeWithMailingAddressAndQuote(pcSideMenu, pcWorkOrder);
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis risk = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
        pcSideMenu.clickSideMenuRiskAnalysis();
        pcWorkOrder.clickGenericWorkorderSubmitOptionsSubmit();
        BasePage basePage = new BasePage(driver);
        if (basePage.isAlertPresent()) {
            basePage.selectOKOrCancelFromPopup(OkCancel.OK);
        }
        UWActivityPC uwactivity = new UWActivityPC(driver);
        uwactivity.setText("Sending this over stuff and stuff");
        uwactivity.setChangeReason(repository.gw.enums.ChangeReason.AnyOtherChange);
        uwactivity.clickSendRequest();
        repository.pc.workorders.generic.GenericWorkorderComplete pcCompletePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
        pcCompletePage.clickViewYourPolicy();
        PolicyActivities currentActivities = new PolicyActivities(new PolicySummary(driver).getCurrentActivites_BasicInfo());
        softAssert.assertTrue(beforeActivities.getPolicyActivityList().size() + 1 == currentActivities.getPolicyActivityList().size(), "one new Activity should Generate after Agent Auto issue But not");
        boolean activity = isActivityFound(currentActivities, "Submitted policy change");
        softAssert.assertTrue(activity, "Submitted policy change - activity found");
        softAssert.assertAll();
    }


    @Test(enabled = true)
    public void verifyCancelRewitePolicyChange_With_CSR_RequestApproval() throws Exception {
        generate();
        Underwriters underwriterInfo = UnderwritersHelper.getRandomUnderwriter();
        new Login(driver).loginAndSearchAccountByAccountNumber(underwriterInfo.getUnderwriterUserName(), underwriterInfo.getUnderwriterPassword(), myPolicyObject.accountNumber);
        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        repository.pc.workorders.generic.GenericWorkorderComplete pcWorkorderCompletePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        PolicyMenu pcPolicyMenu = new PolicyMenu(driver);
        StartCancellation pcCancellationPage = new StartCancellation(driver);
        SoftAssert softAssert = new SoftAssert();
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(repository.gw.enums.PolicyTermStatus.InForce);
        cancelPolicy(pcPolicyMenu, pcCancellationPage);
        pcWorkorderCompletePage.clickViewYourPolicy();
        rewriteFullAndQuote(pcSideMenu, pcWorkOrder, pcPolicyMenu);
        pcWorkorderCompletePage.clickViewYourPolicy();
        PolicyActivities beforeActivities = new PolicyActivities(new PolicySummary(driver).getCurrentActivites_BasicInfo());
        //logout and Login as CSR
        new GuidewireHelpers(driver).logout();
        String CSRUsername = "cashcraft";
        new Login(driver).loginAndSearchAccountByAccountNumber(CSRUsername, "gw", myPolicyObject.accountNumber);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();
        // start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        Date policyChangeEffective = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), repository.gw.enums.DateAddSubtractOptions.Day, 18);
        policyChangePage.startPolicyChange("policy Change coverage Limit change", policyChangeEffective);
        pcSideMenu.clickSideMenuSquirePropertyCoverages();
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyCoverages pcSquirePropertyCoveragesPage = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyCoverages(driver);
        pcSquirePropertyCoveragesPage.selectSectionIDeductible(repository.gw.enums.Property.SectionIDeductible.OneThousand);
        pcWorkOrder.clickGenericWorkorderQuote();
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis risk = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
        pcSideMenu.clickSideMenuRiskAnalysis();
        UWActivityPC uwActivity = new UWActivityPC(driver);
        risk.clickRequestApproval();
        uwActivity.setText("Sending this over stuff and stuff");
        uwActivity.setNewNoteSubject("Please Special Approve this Stuff!!");
        uwActivity.clickSendRequest();
        repository.pc.workorders.generic.GenericWorkorderComplete pcCompletePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
        pcCompletePage.clickViewYourPolicy();
        PolicyActivities currentActivities = new PolicyActivities(new PolicySummary(driver).getCurrentActivites_BasicInfo());
        softAssert.assertTrue(beforeActivities.getPolicyActivityList().size() + 1 == currentActivities.getPolicyActivityList().size(), "one new Activity should Generate after Agent requested approval But not");
        boolean activity = isActivityFound(currentActivities, "Review and submit SA/CSR policy change");
        softAssert.assertTrue(activity, "Review and submit SA/CSR policy change - activity not found");
        softAssert.assertAll();
    }


    @Test(enabled = true)
    public void verifyCancelRewitePolicyChange_With_Agent_RequestApproval() throws Exception {
        generate();
        Underwriters underwriterInfo = UnderwritersHelper.getRandomUnderwriter();
        new Login(driver).loginAndSearchAccountByAccountNumber(underwriterInfo.getUnderwriterUserName(), underwriterInfo.getUnderwriterPassword(), myPolicyObject.accountNumber);
        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        repository.pc.workorders.generic.GenericWorkorderComplete pcWorkorderCompletePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        PolicyMenu pcPolicyMenu = new PolicyMenu(driver);
        StartCancellation pcCancellationPage = new StartCancellation(driver);
        SoftAssert softAssert = new SoftAssert();
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(repository.gw.enums.PolicyTermStatus.InForce);
        cancelPolicy(pcPolicyMenu, pcCancellationPage);
        pcWorkorderCompletePage.clickViewYourPolicy();
        rewriteFullAndQuote(pcSideMenu, pcWorkOrder, pcPolicyMenu);
        pcWorkorderCompletePage.clickViewYourPolicy();
        PolicyActivities beforeActivities = new PolicyActivities(new PolicySummary(driver).getCurrentActivites_BasicInfo());
        //logout and Login as Agent
        new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        // start policy change
        Date policyChangeEffective = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), repository.gw.enums.DateAddSubtractOptions.Day, -18);
        policyChangePage.startPolicyChange("policy Change coverage Limit change and Back dated the Policy Change", policyChangeEffective);
        pcSideMenu.clickSideMenuSquirePropertyCoverages();
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyCoverages pcSquirePropertyCoveragesPage = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyCoverages(driver);
        pcSquirePropertyCoveragesPage.selectSectionIDeductible(repository.gw.enums.Property.SectionIDeductible.OneThousand);
        pcWorkOrder.clickGenericWorkorderQuote();
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis risk = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
        pcSideMenu.clickSideMenuRiskAnalysis();
        risk.requestApproval();
        repository.pc.workorders.generic.GenericWorkorderComplete pcCompletePage = new GenericWorkorderComplete(driver);
        pcCompletePage.clickViewYourPolicy();
        PolicyActivities currentActivities = new PolicyActivities(new PolicySummary(driver).getCurrentActivites_BasicInfo());
        softAssert.assertTrue(beforeActivities.getPolicyActivityList().size() + 1 == currentActivities.getPolicyActivityList().size(), "one new Activity should Generate after Agent requested approval But not");
        boolean activity = isActivityFound(currentActivities, "Approval Requested");
        softAssert.assertTrue(activity, "Approval Requested - activity not found");
        softAssert.assertAll();
    }


    private boolean isActivityFound(PolicyActivities currentActivities, String s) {
        boolean activityCancelled = false;
        for (Activity activity : currentActivities.getPolicyActivityList()) {
            if (activity.getSubject().contains(s)) {
                activityCancelled = true;

                break;
            }

        }
        return activityCancelled;
    }

    private void policyChangeWithMailingAddressAndQuote(SideMenuPC pcSideMenu, repository.pc.workorders.generic.GenericWorkorder pcWorkOrder) throws Exception {
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        // start policy change
        Date policyChangeEffective = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), repository.gw.enums.DateAddSubtractOptions.Day, 18);
        policyChangePage.startPolicyChange("policy Change", policyChangeEffective);
        repository.pc.workorders.generic.GenericWorkorderPolicyInfo polInfo = new repository.pc.workorders.generic.GenericWorkorderPolicyInfo(driver);
        pcSideMenu.clickSideMenuPolicyInfo();
        polInfo.clickPolicyInfoPrimaryNamedInsured();
        repository.pc.workorders.generic.GenericWorkorderPolicyInfoContact policyInfoContactPage = new repository.pc.workorders.generic.GenericWorkorderPolicyInfoContact(driver);
        AddressInfo newAddress = new AddressInfo(true);
        policyInfoContactPage.setDesignatedNewAddress(newAddress);
        policyInfoContactPage.clickUpdate();
        pcSideMenu.clickSideMenuRiskAnalysis();
        pcWorkOrder.clickGenericWorkorderQuote();
    }

    private void rewriteFullAndQuote(SideMenuPC pcSideMenu, repository.pc.workorders.generic.GenericWorkorder pcWorkOrder, PolicyMenu pcPolicyMenu) throws Exception {
        pcPolicyMenu.clickMenuActions();
        pcPolicyMenu.clickRewriteFullTerm();
        pcSideMenu.clickSideMenuPropertyLocations();
        pcSideMenu.clickSideMenuSquirePropertyDetail();
        pcSideMenu.clickSideMenuSquirePropertyCoverages();
        StartRewrite rewriteWO = new StartRewrite(driver);
        rewriteWO.visitAllPages(myPolicyObject);
        pcWorkOrder.clickGenericWorkorderQuote();
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis riskAnalysis = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
        pcSideMenu.clickSideMenuRiskAnalysis();
        riskAnalysis.approveAll();
        pcWorkOrder.clickIssuePolicyButton();
    }

    private void cancelPolicy(PolicyMenu pcPolicyMenu, StartCancellation pcCancellationPage) {
        pcPolicyMenu.clickMenuActions();
        pcPolicyMenu.clickCancelPolicy();
        pcCancellationPage.setSource("Carrier");
        pcCancellationPage.setCancellationReason("policy rewritten or replaced (flat cancel)");
        pcCancellationPage.setExplanation("other");
        pcCancellationPage.setDescription("testing");
        pcCancellationPage.setSourceReasonAndExplanation(repository.gw.enums.Cancellation.CancellationSourceReasonExplanation.Rewritten);
        pcCancellationPage.clickStartCancellation();
        pcCancellationPage.clickSubmitOptionsCancelNow();
    }

    private void generate() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withInsFirstLastName("rewrite", "PolicyChange")
                .withPolEffectiveDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter),
                        repository.gw.enums.DateAddSubtractOptions.Day, -20))
                .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);
    }
}

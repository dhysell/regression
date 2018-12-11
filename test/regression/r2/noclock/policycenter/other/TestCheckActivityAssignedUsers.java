package regression.r2.noclock.policycenter.other;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.activity.ActivityPopup;
import repository.gw.enums.ActivtyRequestType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.topinfo.TopInfo;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.actions.ActionsPC;
import repository.pc.activity.GenericActivityPC;
import repository.pc.activity.UWActivityPC;
import repository.pc.search.SearchAccountsPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages;
import persistence.enums.Underwriter.UnderwriterLine;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : US13578:  COMMON - Allow any activity to go back to person who originally requested rather than always the agent
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Common-Admin/PC8%20-%20Common%20-%20CommonAdmin%20-%20Risk%20Analysis%20-%20UW%20Issues.xlsx">PC8 - Common - CommonAdmin - Risk Analysis -UW issues</a>
 * @Description
 * @DATE Jan 19, 2018
 */
public class TestCheckActivityAssignedUsers extends BaseTest {
    Underwriters uw;
    private GeneratePolicy myPolicyObj;
    private GeneratePolicy myPolIssueObj;

    private WebDriver driver;

    @Test
    public void testCreateSquireAutoFA() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = new SquirePersonalAuto();

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Activity", "Request")
                .build(GeneratePolicyType.FullApp);
    }

    // Scenarion 1: After release lock make sure activity is going to requester
    // instead of agent
    @Test(dependsOnMethods = {"testCreateSquireAutoFA"})
    public void testCheckRequestedApproved() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Underwriters uw = UnderwritersHelper.getRandomUnderwriter(UnderwriterLine.Personal);

        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        new GuidewireHelpers(driver).editPolicyTransaction();
        sideMenu.clickSideMenuPACoverages();
        GenericWorkorderSquireAutoCoverages_Coverages autoCoveragePage = new GenericWorkorderSquireAutoCoverages_Coverages(driver);
        autoCoveragePage.setLiabilityCoverage(LiabilityLimit.CSL300K);
        autoCoveragePage.setMedicalCoverage(MedicalLimit.TenK);
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis riskAnaysis = new GenericWorkorderRiskAnalysis(driver);
        riskAnaysis.clickRequestApproval();

        UWActivityPC activity = new UWActivityPC(driver);
        activity.setText("Please Special Approve this Stuff!!");
        activity.setNewNoteSubject("Please Special Approve this Stuff!!");
        activity.clickSendRequest();
        InfoBar infoBar = new InfoBar(driver);
        infoBar.clickInfoBarAccountNumber();
        AccountSummaryPC aSumm = new AccountSummaryPC(driver);
        ArrayList<String> activityOwners = new ArrayList<String>();
        activityOwners = aSumm.getActivityAssignedTo("Approval Requested");
        Underwriters uwSpecialApproval = UnderwritersHelper
                .getUnderwriterInfoByFullName(activityOwners.get(activityOwners.size() - 1));
        TopInfo topInfoStuff = new TopInfo(driver);
        topInfoStuff.clickTopInfoLogout();
        Login loginPage = new Login(driver);
        loginPage.login(uwSpecialApproval.getUnderwriterUserName(), uwSpecialApproval.getUnderwriterPassword());
        SearchAccountsPC search1 = new SearchAccountsPC(driver);
        search1.searchAccountByAccountNumber(myPolicyObj.accountNumber);
        AccountSummaryPC acct = new AccountSummaryPC(driver);
        acct.clickActivitySubject("Approval Requested");
        ActivityPopup actPop = new ActivityPopup(driver);
        actPop.clickCompleteButton();

        SideMenuPC sideBar = new SideMenuPC(driver);
        sideBar.clickSideMenuRiskAnalysis();
        riskAnaysis.clickReleaseLock();
        infoBar.clickInfoBarAccountNumber();
        activityOwners = aSumm.getActivityAssignedTo("Underwriter has reviewed this job");
        boolean assignedToRequester = false;
        for (String currentActivity : activityOwners) {
            if (currentActivity.contains(uw.getUnderwriterFirstName())
                    && currentActivity.contains(uw.getUnderwriterLastName())) {
                assignedToRequester = true;
                break;
            }
        }
        if (!assignedToRequester) {
            Assert.fail("Activity is not assigned to requested person : " + uw.getUnderwriterFirstName() + ""
                    + uw.getUnderwriterLastName());
        }
    }

    //Scenario  2 - creating activity and check user completes the activity
    @Test()
    public void testIssueSquireAuto() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = new SquirePersonalAuto();

        myPolIssueObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Activity", "Request")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

    }

    @Test(dependsOnMethods = {"testIssueSquireAuto"})
    public void testCreateActivity() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Underwriters uw = UnderwritersHelper.getRandomUnderwriter(UnderwriterLine.Personal);

        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolIssueObj.squire.getPolicyNumber());

        ActionsPC myActions = new ActionsPC(driver);
        myActions.requestActivity(ActivtyRequestType.SubmitToUnderwriting);


        GenericActivityPC myActivity = new GenericActivityPC(driver);
        myActivity.setSubject("Submit to Underwriting");
        myActivity.setText("Requesting additional information for test");
        myActivity.clickOK();
        InfoBar infoBar = new InfoBar(driver);
        infoBar.clickInfoBarAccountNumber();
        AccountSummaryPC aSumm = new AccountSummaryPC(driver);
        ArrayList<String> activityOwners = new ArrayList<String>();
        activityOwners = aSumm.getActivityAssignedTo("Submit to Underwriting");
        Underwriters uwSpecialApproval = UnderwritersHelper
                .getUnderwriterInfoByFullName(activityOwners.get(activityOwners.size() - 1));
        TopInfo topInfoStuff = new TopInfo(driver);
        topInfoStuff.clickTopInfoLogout();
        Login loginPage = new Login(driver);
        loginPage.login(uwSpecialApproval.getUnderwriterUserName(), uwSpecialApproval.getUnderwriterPassword());
        SearchAccountsPC search1 = new SearchAccountsPC(driver);
        search1.searchAccountByAccountNumber(myPolIssueObj.accountNumber);
        AccountSummaryPC acct = new AccountSummaryPC(driver);
        acct.clickActivitySubject("Submit to Underwriting");
        UWActivityPC activity = new UWActivityPC(driver);
        activity.completeActivity();
        infoBar.clickInfoBarAccountNumber();
        if (acct.verifyCurrentActivity("Submit to Underwriting", 1000)) {
            Assert.fail("Submit to Underwriting Activity is still open in Account - summary ");
        }
    }
}

package previousProgramIncrement.pi3_090518_111518.f209_UM_UIM_Rule20_NewForm;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.OrganizationType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Activity;
import repository.gw.generate.custom.PolicyActivities;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages;

import java.util.Date;

/**
 * @Author swathiAkarapu
 * @Requirement US16570
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558454824ud/detail/userstory/257384988332">US16570</a>
 * @Description
 * As a home office underwriter and the UM/UIM is being unselected/rejected/removed from policy I need to see an activity for underwriter to check policy for the signed UM/UIM rejection form.
 * Activity for UW to read "UM/UIM has been rejected"
 * Requirements: Activity Patterns
 *
 * Steps to get there:
 * As agent/PA start a policy change on policy that has UM/UIM on section III.
 * On the policy change remove UM/UIM coverage
 * Auto-issue policy change
 * ALSO test when UW starts policy change and when county user needs to sent to UW for approval - both removing UM/UIM coverage
 * Acceptance criteria:
 * Ensure that when agent/PA auto-issue a policy change where the UM/UIM is being rejected then there should be an activity to UW so they can do their process
 * Ensure that if UW starts a policy change that is removing the existing UM/UIM coverage and they issue, there is the activity to UW still
 * Ensure that if county user starts policy change and has to request approval to UW and UW issues, there is the activity to UW still
 * @DATE October 26, 2018
 */
public class VerifyUmUimUnderWriterActivity extends BaseTest {

    private GeneratePolicy myPolicyObject = null;
    private WebDriver driver;

    @Test
    public void rejectUmUimCoverage_Agent() throws  Exception{
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        generateAutoPolicy(AgentsHelper.getRandomAgent());
        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();
        repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail(
                driver);
        // start policy change
        Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        policyChangePage.startPolicyChange("policy Change", currentSystemDate);
        sideMenu.clickPolicyContractSectionIIIAutoLine();
        sideMenu.clickSideMenuPACoverages();
        repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages coveragePage = new repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages(driver);
        coveragePage.setUninsuredCoverage(false, null);
        sideMenu.clickSideMenuRiskAnalysis();
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis risk = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
        risk.Quote();
        repository.pc.workorders.generic.GenericWorkorderQuote quotePage = new repository.pc.workorders.generic.GenericWorkorderQuote(driver);
        if (quotePage.isPreQuoteDisplayed()) {
            quotePage.clickPreQuoteDetails();
        }
         sideMenu.clickSideMenuRiskAnalysis();
        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        pcWorkOrder.clickIssuePolicyButton();
        repository.pc.workorders.generic.GenericWorkorderComplete pcWorkorderCompletePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
        pcWorkorderCompletePage.clickViewYourPolicy();
        PolicyActivities currentActivities = new PolicyActivities(new PolicySummary(driver).getCurrentActivites_BasicInfo());
        String assignedTo="";
        boolean activityfound = false;
        for (Activity activity : currentActivities.getPolicyActivityList()) {
            if (activity.getSubject().contains("UM/UIM coverage has been rejected.")) {
                assignedTo= activity.getAssignedTo();
                activityfound = true;
                break;
            }

        }
        SoftAssert softAssert = new SoftAssert();
        Underwriters assignedUW=  UnderwritersHelper.getUnderwriterInfoByFullName(assignedTo);
        softAssert.assertTrue(activityfound, "activity not found");
        softAssert.assertNotNull( assignedUW.getUnderwriterUserName() , "Activity was not assigned to UW!");
        softAssert.assertAll();
    }


    @Test
    public void rejectUmUimCoverage_uw() throws  Exception{
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
       generateAutoPolicy(AgentsHelper.getRandomAgent());
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
       new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObject.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);

        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();
        repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail(
                driver);
        // start policy change
        Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        policyChangePage.startPolicyChange("policy Change", currentSystemDate);
        sideMenu.clickPolicyContractSectionIIIAutoLine();
        sideMenu.clickSideMenuPACoverages();
        repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages coveragePage = new repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages(driver);
        coveragePage.setUninsuredCoverage(false, null);
        sideMenu.clickSideMenuRiskAnalysis();
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis risk = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
        risk.Quote();
        repository.pc.workorders.generic.GenericWorkorderQuote quotePage = new repository.pc.workorders.generic.GenericWorkorderQuote(driver);
        if (quotePage.isPreQuoteDisplayed()) {
            quotePage.clickPreQuoteDetails();
        }
        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        pcWorkOrder.clickIssuePolicyButton();
        repository.pc.workorders.generic.GenericWorkorderComplete pcWorkorderCompletePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
        pcWorkorderCompletePage.clickViewYourPolicy();
        PolicyActivities currentActivities = new PolicyActivities(new PolicySummary(driver).getCurrentActivites_BasicInfo());
        String assignedTo="";
        boolean activityfound = false;
        for (Activity activity : currentActivities.getPolicyActivityList()) {
            if (activity.getSubject().contains("UM/UIM coverage has been rejected.")) {
                assignedTo= activity.getAssignedTo();
                activityfound = true;

                break;
            }

        }
        SoftAssert softAssert = new SoftAssert();
        Underwriters assignedUW=  UnderwritersHelper.getUnderwriterInfoByFullName(assignedTo);
        softAssert.assertTrue(activityfound, "activity not found");
        softAssert.assertNotNull( assignedUW.getUnderwriterUserName() , "Activity was not assigned to UW!");

        softAssert.assertAll();
    }

    @Test
    public void rejectUmUimCoverage_PA() throws  Exception{
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Agents agentInfo = AgentsHelper.getAgentByUserName("dpfost");
        generateAutoPolicy(agentInfo);
        String paUsername = "kclark"; // hard coded the PAuser name beacuse PAHelper is not working
        new Login(driver).loginAndSearchAccountByAccountNumber(paUsername, "gw", myPolicyObject.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);

        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();
        // start policy change
        Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        policyChangePage.startPolicyChange("policy Change", currentSystemDate);
        sideMenu.clickPolicyContractSectionIIIAutoLine();
        sideMenu.clickSideMenuPACoverages();
        repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages coveragePage = new GenericWorkorderSquireAutoCoverages_Coverages(driver);
        coveragePage.setUninsuredCoverage(false, null);
         sideMenu.clickSideMenuRiskAnalysis();
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis risk = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
        risk.Quote();
        repository.pc.workorders.generic.GenericWorkorderQuote quotePage = new repository.pc.workorders.generic.GenericWorkorderQuote(driver);
        if (quotePage.isPreQuoteDisplayed()) {
            quotePage.clickPreQuoteDetails();
        }


        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues risk_UWIssues = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues(driver);

        risk_UWIssues.handleBlockSubmitAndReleaseLockToRequester(myPolicyObject);

        new Login(driver).loginAndSearchAccountByAccountNumber(paUsername, "gw", myPolicyObject.accountNumber);
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Squire);
        sideMenu.clickSideMenuRiskAnalysis();
        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        pcWorkOrder.clickIssuePolicyButton();
        repository.pc.workorders.generic.GenericWorkorderComplete pcWorkorderCompletePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
        pcWorkorderCompletePage.clickViewYourPolicy();
        PolicyActivities currentActivities = new PolicyActivities(new PolicySummary(driver).getCurrentActivites_BasicInfo());
        String assignedTo="";
        boolean activityfound = false;
        for (Activity activity : currentActivities.getPolicyActivityList()) {
            if (activity.getSubject().contains("UM/UIM coverage has been rejected.")) {
                assignedTo= activity.getAssignedTo();
                activityfound = true;

                break;
            }

        }
        SoftAssert softAssert = new SoftAssert();
        Underwriters assignedUW=  UnderwritersHelper.getUnderwriterInfoByFullName(assignedTo);
        softAssert.assertTrue(activityfound, "activity not found");
        softAssert.assertNotNull( assignedUW.getUnderwriterUserName() , "Activity was not assigned to UW!");
        softAssert.assertAll();
    }


    private void generateAutoPolicy(Agents agentInfo) throws Exception {
        repository.gw.generate.custom.SquirePersonalAutoCoverages coverages = new repository.gw.generate.custom.SquirePersonalAutoCoverages(repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit.FiftyHigh,
                repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit.TwentyFiveK, true, repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit.Fifty,
                true, repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit.Fifty);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);
        Squire mySquire = new Squire(repository.gw.enums.SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withAgent(agentInfo)
                .withSquire(mySquire)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PersonalAutoLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withInsFirstLastName("UM", "UIM")
                .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);
    }
}

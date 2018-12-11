package previousProgramIncrement.pi2_062818_090518.nonFeatures.Scope_Creeps;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderLineSelection;
import repository.pc.workorders.generic.GenericWorkorderLocations;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author ecoleman
 * @Requirement
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558461772d/detail/defect/245278929704">DE7785 - COMMON - If an UW takes activity from another UW, works and releases to agent it needs to actually and visually go to agent/a>
 * @Description If an agent has to request approval and one underwriter takes that activity from another one to finish and then release lock back to agent it is not working. Behind the scenes it does go to the agent, but visually in PC the activity is now at the underwriter who took it and worked on it. The underwriter has to go to activities and reassign it to the agent or the agent will never know they had the activity.
 * <p>
 * Requirements: PC8 - Common - CommonAdmin - Activity Detail (Refer UI Final tab)
 * <p>
 * Steps to get there:
 * <p>
 * As agent do policy that will require approval before can quote.
 * As agent request approval to underwriting
 * Go in as an underwriter that is not the one the request approval went to
 * Approve and release lock back to agent
 * <p>
 * Actual: The release lock activity shows that the activity is still at the underwriter that approved the issue. Behind scenes is at agent however.
 * Expected: The release lock activity show show to the agent and should be at the agent
 * <p>
 * Acceptance criteria:
 * <p>
 * Ensure any underwriter can grab a 'request approval' activity from any other underwriter and finish and release lock and it will go back to the agent
 * @DATE August 14, 2018
 */

public class DE7785_RequestApprovalRouting extends BaseTest {

    public GeneratePolicy myPolicyObject = null;
    private WebDriver driver;


    public void generatePolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.InlandMarineLinePL, LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withDownPaymentType(PaymentType.Cash)
                .withInsFirstLastName("ScopeCreeps", "NonFeature")
                .isDraft()
                .build(GeneratePolicyType.FullApp);
    }

    @Test(enabled = true)
    public void CheckAgentAssignedAfterApproval() throws Exception {


        generatePolicy();


        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.agentUserName, myPolicyObject.agentInfo.agentPassword, myPolicyObject.accountNumber);


        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail pcPropertyDetailPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
        GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
        GenericWorkorderRiskAnalysis pcRiskPage = new GenericWorkorderRiskAnalysis(driver);
        GenericWorkorderComplete pcWorkorderCompletePage = new GenericWorkorderComplete(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        GenericWorkorderRiskAnalysis_UWIssues pcRiskUnderwriterIssuePage = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        GenericWorkorderLocations pcLocationsPage = new GenericWorkorderLocations(driver);
        GenericWorkorderLineSelection pcLineSelectionPage = new GenericWorkorderLineSelection(driver);


        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);

        pcSideMenu.clickSideMenuSquirePropertyDetail();
        pcPropertyDetailPage.removeBuilding("1");
        pcSideMenu.clickSideMenuPropertyLocations();
        pcLocationsPage.removeAllLocations();

        pcSideMenu.clickSideMenuLineSelection();
        pcLineSelectionPage.checkSquireLineSelectionByText("Sections I & II", false);

        pcSideMenu.clickSideMenuRiskAnalysis();
        pcWorkOrder.clickGenericWorkorderQuote();

        pcRiskPage.requestApproval();

        Underwriters myUW = UnderwritersHelper.getRandomUnderwriter();
        pcWorkorderCompletePage.clickAccountNumber();
        String uwAssignedTo = pcAccountSummaryPage.getActivityAssignedTo(1);

        while (myUW.underwriterLastName == uwAssignedTo.split(" ")[1]) {
            myUW = UnderwritersHelper.getRandomUnderwriter();
        }

        new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchAccountByAccountNumber(myUW.underwriterUserName, myUW.underwriterPassword, myPolicyObject.accountNumber);
        pcAccountSummaryPage.clickCurrentActivitiesSubject("Approval Requested");
        pcRiskUnderwriterIssuePage.clickReleaseLock();

        new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.agentUserName, myPolicyObject.agentInfo.agentPassword, myPolicyObject.accountNumber);
        String assignedTo = pcAccountSummaryPage.getActivityAssignedTo(1);

        // Acceptance criteria: Ensure agent is assigned after different UW finishes activity
        Assert.assertTrue(assignedTo.contains(myPolicyObject.agentInfo.agentLastName), "Activity was not assigned to agent!");
    }
}
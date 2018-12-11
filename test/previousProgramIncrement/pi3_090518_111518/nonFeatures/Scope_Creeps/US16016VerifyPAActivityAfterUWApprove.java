package previousProgramIncrement.pi3_090518_111518.nonFeatures.Scope_Creeps;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.OrganizationType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;

/**
 * @Author swathiAkarapu
 * @Requirement US16016
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558454824ud/detail/userstory/246071485960">US16016</a>
 * @Description As a PA I want activities that I initiate to be routed back to me and not the agent so that the agent isn't getting activities they weren't aware of and the process is smooth.
 * <p>
 * Steps to get there:
 * Begin a new submission as a PA
 * Create the policy so that there will be an underwriting issue that will need approved before the full application can be submitted
 * Quote
 * Go to the risk analysis screen and request approval for the UW issue
 * Approve the issue as an UW and release the lock on the policy
 * Acceptance criteria:
 * Ensure that the activity "Underwriting has reviewed this job" is routed to the PA that requested approval
 * Ensure that this works for both new submissions and policy change jobs (for BOP and PL)
 * Ensure the request approvals from
 * @DATE October 9, 2018
 */

public class US16016VerifyPAActivityAfterUWApprove extends BaseTest {
    public GeneratePolicy myPolicyObject = null;
    private WebDriver driver;

    private void generatePolicyFull() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        // Agents agent1 = AgentsHelper.getAgentWithPA(); PA helper is not Working so Hard coding the agent and PA

        Agents agent1 = AgentsHelper.getAgentByUserName("dpfost");
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withAgent(agent1)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withInsFirstLastName("PA", "Activity")
                .isDraft()
                .build(repository.gw.enums.GeneratePolicyType.FullApp);
    }

    @Test
    public void verifyPaActivity() throws Exception {
        generatePolicyFull();
     //   PAs pa = PAsHelper.getPAInfoByAgent(myPolicyObject.agentInfo.agentFullName);
       // new Login(driver).loginAndSearchAccountByAccountNumber(pa.getPauserName(), "gw", myPolicyObject.accountNumber);
        String paUsername = "kclark"; // PA helper is not Working so Hard coding the agent and PA

        new Login(driver).loginAndSearchAccountByAccountNumber(paUsername, "gw", myPolicyObject.accountNumber);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Squire);
        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        pcSideMenu.clickSideMenuPropertyLocations();
        pcSideMenu.clickSideMenuSquirePropertyDetail();
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailConstruction constructionPage = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailConstruction(driver);
        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        propertyDetail.clickViewOrEditBuildingButton(1);
        propertyDetail.clickPropertyConstructionTab();
        constructionPage.editbox_MSYear.clear();
        propertyDetail.clickOk();
        propertyDetail.clickOkayIfMSPhotoYearValidationShows();
        pcSideMenu.clickSideMenuSquirePropertyCoverages();
        pcSideMenu.clickSideMenuSquirePropertyGlLineReview();
        pcSideMenu.clickSideMenuLineSelection();
        pcSideMenu.clickSideMenuMembership();
        pcSideMenu.clickSideMenuMembershipMembershipType();
        pcSideMenu.clickSideMenuMembershipMembers();
        pcSideMenu.clickSideMenuModifiers();
        pcSideMenu.clickSideMenuRiskAnalysis();
        pcWorkOrder.clickGenericWorkorderQuote();
        pcSideMenu.clickSideMenuRiskAnalysis();
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis risk = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
        risk.requestApproval();
        repository.pc.workorders.generic.GenericWorkorderComplete pcWorkorderCompletePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
        pcWorkorderCompletePage.clickAccountNumber();
        String[] split = pcAccountSummaryPage.getActivityAssignedTo(1).split(" ");
        Underwriters myUW = UnderwritersHelper.getUnderwriterInfoByFirstLastName(split[0], split[split.length - 1]);


        new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchAccountByAccountNumber(myUW.underwriterUserName, myUW.underwriterPassword, myPolicyObject.accountNumber);
        pcAccountSummaryPage.clickCurrentActivitiesSubject("Approval Requested");
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues pcRiskUnderwriterIssuePage = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues(driver);
        pcRiskUnderwriterIssuePage.clickReleaseLock();

        new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.agentUserName, myPolicyObject.agentInfo.agentPassword, myPolicyObject.accountNumber);
        String assignedTo = pcAccountSummaryPage.getActivityAssignedTo(1);

//Comments beacuse PA helper is not working expected
       // Assert.assertTrue(assignedTo.contains(pa.getPafirstName()) && assignedTo.contains(pa.getPalastName()), "Activity was not assigned to PA!");
        Assert.assertTrue(assignedTo.contains("Karen") && assignedTo.contains("Clark"), "Activity was not assigned to PA!");
    }
}

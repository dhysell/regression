package previousProgramIncrement.pi3_090518_111518.nonFeatures.Scope_Creeps;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.enums.OrganizationType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Activity;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.account.AccountSummaryPC;
import repository.pc.activity.UWActivityPC;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AgentsHelper;
import repository.pc.workorders.generic.GenericWorkorderComplete;

import java.util.List;

import static org.testng.Assert.assertTrue;

/**
 * @Author swathiAkarapu
 * @Requirement US16095
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558454824ud/detail/userstory/249229786524">US16095</a>
 * @Description Steps to get there:
 * ---------------------
 * As a CSR/SA start a policy change on an existing policy (test both roles)
 * Go to the membership screen
 * Edit the membership line (change county, status, etc.)
 * Quote
 * try to submit
 * Acceptance criteria:
 * ----------------------
 * Ensure that a CSR/SA have the permission to edit the membership line on a squire policy
 * If CSR/SA makes change to member ssn, birthdate, gender or address on the member they will not have the submit button and will need to request approval, which will go to the agent
 * Ensure that if the only change being made is to the membership line, the CSR/SA has the submit button
 * The activity for the change from the CSR/SA should go to Underwriting
 * Ensure that if the CSR/SA make membership line changes AND coverage changes, the CSR or SA has to request approval and cannot submit
 * The request approval activity should go to the CSR/SA's associated agent
 * Ensure that membership only policies are not affected (story being done by Achievers and CSR/SA will be able to issue membership only policies without going to underwriting)
 * @DATE August 10, 2018
 */
public class US16095MemberShipLineCanEditBySAorCSR extends BaseTest {

    public GeneratePolicy myPolicyObject = null;
    private WebDriver driver;

    private void generatePolicyWithAgent(Agents agent) throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withAgent(agent)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withInsFirstLastName("ScopeCreeps", "NonFeature")
                .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);
    }

    @Test(enabled = true)
    public void verifyCSRCanEditAndSubmitMemberShipDetailsWithRenewalStatus() throws Exception {
        String cSRUsername = "cashcraft";
        generatePolicyWithAgent(AgentsHelper.getRandomAgent());


        new Login(driver).loginAndSearchAccountByAccountNumber(cSRUsername, "gw", myPolicyObject.accountNumber);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        PolicySummary pcPolicySummaryPage = new PolicySummary(driver);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.startPolicyChange("change MemberShip details by CSR", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
        pcSideMenu.clickSideMenuMembership();
        pcSideMenu.clickSideMenuMembershipMembers();
        repository.pc.workorders.generic.GenericWorkorderMembershipMembers member = new repository.pc.workorders.generic.GenericWorkorderMembershipMembers(driver);
        member.clickEditMembershipMember(myPolicyObject.pniContact);
        member.selectMembershipMembersRenewalStatus(repository.gw.enums.MembershipRenewalMemberDuesStatus.Remove_At_Renewal);
        member.clickOK();

        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        pcWorkOrder.clickGenericWorkorderQuote();
        BasePage basePage = new BasePage(driver);

        // Acceptance criteria: submit button should be showing and not disable
        Assert.assertTrue(basePage.checkIfElementExists("//a[contains(@id, 'RequestApproval')]", 5), "Submit button was not visible, it should be");
        Assert.assertFalse(basePage.checkIfElementExists("//a[contains(@id, 'RequestApproval')][contains(@class, 'x-disabled')]", 5), "Submit button was disabled, it shouldn't  be");
        pcWorkOrder.clickGenericWorkorderSubmitOptionsSubmit();
        repository.pc.workorders.generic.GenericWorkorderComplete pcCompletePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
        uwActivitySend();
        pcCompletePage.clickViewYourPolicy();

        Boolean uwAssignedToExists = isActivityAssignedToUW(pcAccountSummaryPage, pcPolicySummaryPage);

        Assert.assertTrue(uwAssignedToExists, "UW was not found, assignment did not go to UW!");
    }

    private Boolean isActivityAssignedToUW(AccountSummaryPC pcAccountSummaryPage, PolicySummary pcPolicySummaryPage) {
        String assignedTo = pcPolicySummaryPage.getCurrentActivites_BasicInfo().get(0).getAssignedTo();
        String[] userNameArray = assignedTo.split(" ");
        String underWriterFirstName = userNameArray[0];
        String underWriterLastName = userNameArray[(userNameArray.length-1)];

        return pcAccountSummaryPage.checkUWExists(underWriterFirstName, underWriterLastName);
    }


    @Test(enabled = true)
    public void verifySaCanEditAndSubmitMemberShipDetailsWithCurrentStatus() throws Exception {
        Agents agent = AgentsHelper.getAgentByUserName("tgallup");
        generatePolicyWithAgent(agent);
        String saUsername = "jgallup";
        // Sa helper is not working  SO hardcoded the Username. if Login with SA helper SA ,Submit buttion should be enabled But it not so Hardcoded.
        new Login(driver).loginAndSearchAccountByAccountNumber(saUsername, "gw", myPolicyObject.accountNumber);

        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        SideMenuPC pcSideMenu = new SideMenuPC(driver);

        PolicySummary pcPolicySummaryPage = new PolicySummary(driver);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.startPolicyChange("change MemberShip details by SA", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
        pcSideMenu.clickSideMenuMembership();
        pcSideMenu.clickSideMenuMembershipMembers();
        repository.pc.workorders.generic.GenericWorkorderMembershipMembers member = new repository.pc.workorders.generic.GenericWorkorderMembershipMembers(driver);
        member.clickEditMembershipMember(myPolicyObject.pniContact);
        member.selectMembershipMembersCurrentStatus(repository.gw.enums.MembershipCurrentMemberDuesStatus.Waived);
        member.clickOK();

        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        pcWorkOrder.clickGenericWorkorderQuote();
        BasePage basePage = new BasePage(driver);

        // Acceptance criteria: submit button should be showing and not  disable
        Assert.assertTrue(basePage.checkIfElementExists("//a[contains(@id, 'RequestApproval')]", 5), "Submit button was not visible, it should be");
        Assert.assertFalse(basePage.checkIfElementExists("//a[contains(@id, 'RequestApproval')][contains(@class, 'x-disabled')]", 5), "Submit button was  disabled, it shouldn't  be");
        pcWorkOrder.clickGenericWorkorderSubmitOptionsSubmit();
        repository.pc.workorders.generic.GenericWorkorderComplete pcCompletePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
        uwActivitySend();
        pcCompletePage.clickViewYourPolicy();

        Boolean uwAssignedToExists = isActivityAssignedToUW(pcAccountSummaryPage, pcPolicySummaryPage);

        Assert.assertTrue(uwAssignedToExists, "UW was not found, assignment did not go to UW!");
    }

    @Test(enabled = true)
    public void verifyCSRCanNotEditAndSubmitMemberShipDetailLikeBirthDate() throws Exception {
        String cSRUsername = "cashcraft";
        generatePolicyWithAgent(AgentsHelper.getRandomAgent());
        new Login(driver).loginAndSearchAccountByAccountNumber(cSRUsername, "gw", myPolicyObject.accountNumber);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        SideMenuPC pcSideMenu = new SideMenuPC(driver);

        PolicySummary pcPolicySummaryPage = new PolicySummary(driver);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.startPolicyChange("change MemberShip details- dob by CSR", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
        pcSideMenu.clickSideMenuMembership();
        pcSideMenu.clickSideMenuMembershipMembers();
        repository.pc.workorders.generic.GenericWorkorderMembershipMembers member = new repository.pc.workorders.generic.GenericWorkorderMembershipMembers(driver);
        member.clickEditMembershipMember(myPolicyObject.pniContact);
        member.setMemberDOB(DateUtils.dateAddSubtract(member.getMemberDOB(), repository.gw.enums.DateAddSubtractOptions.Month, -1));
        member.clickOK();

        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        pcWorkOrder.clickGenericWorkorderQuote();
        BasePage basePage = new BasePage(driver);

        // Acceptance criteria: submit button should be showing and disable
        Assert.assertTrue(basePage.checkIfElementExists("//a[contains(@id, 'RequestApproval')]", 5), "Submit button was not visible, it should be");
        Assert.assertTrue(basePage.checkIfElementExists("//a[contains(@id, 'RequestApproval')][contains(@class, 'x-disabled')]", 5), "Submit button was not disabled, it should  be");

        pcSideMenu.clickSideMenuRiskAnalysis();
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis risk = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
        repository.pc.workorders.generic.GenericWorkorderComplete pcCompletePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
        UWActivityPC activity = new UWActivityPC(driver);
        risk.clickRequestApproval();
        activity.setText("Sending this over stuff and stuff");
        activity.setNewNoteSubject("Please Special Approve this Stuff!!");
        activity.clickSendRequest();
        pcCompletePage.clickViewYourPolicy();


        Activity currentActivity = getReviewAndSubmitBySAorCSRActivity(pcPolicySummaryPage);

        assertTrue(currentActivity.getAssignedTo().contains(myPolicyObject.agentInfo.getAgentFirstName()) && currentActivity.getAssignedTo().contains(myPolicyObject.agentInfo.getAgentLastName()) , "Activity was not sent to agent");

    }

    private Activity getReviewAndSubmitBySAorCSRActivity(PolicySummary pcPolicySummaryPage) {
        Activity currentActivity = new Activity();
        List<Activity> activities = pcPolicySummaryPage.getCurrentActivites_BasicInfo();
        for (Activity activityItem :activities) {
            if (activityItem.getSubject().contains("Review and submit SA/CSR policy change")) {
                currentActivity = activityItem;
                break;
            }
        }
        return currentActivity;
    }


    @Test(enabled = true)
    public void verifySaCanNotEditAndSubmitMemberShipDetailsAlongWithCoverageChange() throws Exception {
        Agents agent = AgentsHelper.getAgentByUserName("tgallup");
        generatePolicyWithAgent(agent);
        String saUsername = "jgallup";
        new Login(driver).loginAndSearchAccountByAccountNumber(saUsername, "gw", myPolicyObject.accountNumber);

        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        SideMenuPC pcSideMenu = new SideMenuPC(driver);

        PolicySummary pcPolicySummaryPage = new PolicySummary(driver);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.startPolicyChange("change MemberShip details and Coverage by SA", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
        pcSideMenu.clickSideMenuMembership();
        pcSideMenu.clickSideMenuMembershipMembers();
        repository.pc.workorders.generic.GenericWorkorderMembershipMembers member = new repository.pc.workorders.generic.GenericWorkorderMembershipMembers(driver);
        member.clickEditMembershipMember(myPolicyObject.pniContact);
        member.selectMembershipMembersCurrentStatus(repository.gw.enums.MembershipCurrentMemberDuesStatus.Waived);
        member.clickOK();

        pcSideMenu.clickSideMenuSquirePropertyCoverages();
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyCoverages pcSquirePropertyCoveragesPage = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyCoverages(driver);
        pcSquirePropertyCoveragesPage.selectSectionIDeductible(repository.gw.enums.Property.SectionIDeductible.OneThousand);


        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        pcWorkOrder.clickGenericWorkorderQuote();
        BasePage basePage = new BasePage(driver);

        // Acceptance criteria: submit button should be showing and disable
        Assert.assertTrue(basePage.checkIfElementExists("//a[contains(@id, 'RequestApproval')]", 5), "Submit button was not visible, it should be");
        Assert.assertTrue(basePage.checkIfElementExists("//a[contains(@id, 'RequestApproval')][contains(@class, 'x-disabled')]", 5), "Submit button was not disabled, it should  be");
        repository.pc.workorders.generic.GenericWorkorderComplete pcCompletePage = new GenericWorkorderComplete(driver);

        pcSideMenu.clickSideMenuRiskAnalysis();
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis risk = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
        risk.clickRequestApproval();
        UWActivityPC activity = new UWActivityPC(driver);
        activity.setText("Sending this over stuff and stuff");
        activity.setNewNoteSubject("Please Special Approve this Stuff!!");
        activity.clickSendRequest();
        pcCompletePage.clickViewYourPolicy();


        Activity  currentActivity = getReviewAndSubmitBySAorCSRActivity(pcPolicySummaryPage);

        assertTrue(currentActivity.getAssignedTo().contains(myPolicyObject.agentInfo.getAgentFirstName()) && currentActivity.getAssignedTo().contains(myPolicyObject.agentInfo.getAgentLastName()), "Activity was not sent to agent");
    }

    private void uwActivitySend() {
        UWActivityPC activity = new UWActivityPC(driver);
        activity.setText("Sending this over stuff and stuff");
        activity.setChangeReason(repository.gw.enums.ChangeReason.AnyOtherChange);
        activity.clickSendRequest();
    }

}

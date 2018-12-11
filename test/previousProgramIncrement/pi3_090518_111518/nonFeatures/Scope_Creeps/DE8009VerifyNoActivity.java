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
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.account.AccountSummaryPC;
import repository.pc.activity.UWActivityPC;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import repository.pc.workorders.generic.GenericWorkorderComplete;

import java.util.Date;

/**
 * @Author swathiAkarapu
 * @Requirement DE8009 , US16876
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558454824ud/detail/defect/257874200716">DE8009</a>
 *                   <a href="https://rally1.rallydev.com/#/203558454824ud/detail/userstory/264690025004">US16876</a>
 * @Description DE8009
 * Scenario 1:
 * Have an in force policy
 * Cancel the policy (not flat)
 * Start a policy change effective before the policy was cancelled as an underwriter or coder
 * Issue the policy change
 * Actual: The activity is being assigned to the underwriter that issued the change instead of round-robining through the area
 * Actual: In the case of BOP, the activity is created and is assigned to no one, activity should not trigger (as of 10/4/18 it doesn't in UAT)
 * Expected: The activity should not trigger
 *
 * Scenario 2:
 * Have an in force policy
 * Cancel the policy (not flat)
 * Start a policy change effective before the policy was cancelled as a CSR or SA (make sure the change is one they can immediately submit to UW, e.g. an address change)
 * Submit the policy change
 * Log in as the underwriter the submitted policy change activity was assigned to
 * Issue the policy change
 * Actual: The activity is created again and being assigned to the underwriter that issued the change instead of round-robining through the area
 * Expected: The activity should not trigger
 *  @Description US16876
 *  As a CSR/SA I want to be able to change or edit the mailing address on the policy or on a policy member or on a membership member the submit button should be available so that the policy change goes straight to underwriting instead of the agent. If there is a change other than address that CSR/SA are unable to submit then the submit button would not be available and CSR/SA would need to be request approval to agent.
 * Story is necessary to address conflicts with DE8009 from 3.3 and US16734 for 3.4.
 *
 * Steps to get there:
 * Have existing policy
 * As CSR/SA do policy change updating mailing address
 * As CSR/SA do policy change on policy member changing/editing mailing address
 * Acceptance criteria:
 * Ensure that when CSR/SA updates the mailing address on a policy change that it updates the PNI and ANI and the CSR/SA is still able to have the submit button
 * Ensure that CSR/SA has the ability to update the mailing address on any policy member and is still able to have the submit button
 * Ensure that if CSR/SA does mailing address change or policy member address change along with other changes that do not allow them to submit that the submit button is grayed out and they must 'request approval' to the agent
 * @DATE October12, 2018
 */
public class DE8009VerifyNoActivity  extends BaseTest {


    private WebDriver driver;
    public GeneratePolicy myPolicyObject = null;
    //Scenario 1
    @Test(enabled = true)
    public void verifyAsUnderWriter() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(repository.gw.enums.OrganizationType.Individual)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withInsFirstLastName("cancel", "EditAdd")
                .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);

        Underwriters underwriterInfo = UnderwritersHelper.getRandomUnderwriter();
        new Login(driver).loginAndSearchAccountByAccountNumber(underwriterInfo.getUnderwriterUserName(), underwriterInfo.getUnderwriterPassword(),myPolicyObject.accountNumber);


        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        repository.pc.workorders.generic.GenericWorkorderComplete pcWorkorderCompletePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        PolicyMenu pcPolicyMenu = new PolicyMenu(driver);
        StartCancellation pcCancellationPage = new StartCancellation(driver);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(repository.gw.enums.PolicyTermStatus.InForce);
        pcPolicyMenu.clickMenuActions();


        pcPolicyMenu.clickCancelPolicy();
        pcCancellationPage.setSourceReasonAndExplanation(repository.gw.enums.Cancellation.CancellationSourceReasonExplanation.OtherLackUWInfo);
        pcCancellationPage.setEffectiveDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), repository.gw.enums.DateAddSubtractOptions.Day, 20));
        pcCancellationPage.setDescription("test edit address after cancel");
        pcCancellationPage.clickStartCancellation();
        pcCancellationPage.clickSubmitOptionsCancelNow();
        pcWorkorderCompletePage.clickViewYourPolicy();
        pcPolicyMenu.clickMenuActions();
        pcPolicyMenu.clickChangePolicy();

        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        SoftAssert softAssert = new SoftAssert();
        // start policy change
        Date policyChangeEffective = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), repository.gw.enums.DateAddSubtractOptions.Day, 18);
        String changeNumber = policyChangePage.startPolicyChange("UW policy Change", policyChangeEffective);
        repository.pc.workorders.generic.GenericWorkorderPolicyInfo polInfo = new repository.pc.workorders.generic.GenericWorkorderPolicyInfo(driver);
        pcSideMenu.clickSideMenuPolicyInfo();
        polInfo.clickPolicyInfoPrimaryNamedInsured();
        repository.pc.workorders.generic.GenericWorkorderPolicyInfoContact policyInfoContactPage = new repository.pc.workorders.generic.GenericWorkorderPolicyInfoContact(driver);
        AddressInfo newAddress= new AddressInfo(true);
        policyInfoContactPage.setDesignatedNewAddress(newAddress);
        policyInfoContactPage.clickUpdate();
        pcSideMenu.clickSideMenuRiskAnalysis();
        pcWorkOrder.clickGenericWorkorderQuote();
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis risk = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
        pcSideMenu.clickSideMenuRiskAnalysis();
        risk.approveAll();
        pcSideMenu.clickSideMenuRiskAnalysis();
        pcWorkOrder.clickIssuePolicyButton();
        repository.pc.workorders.generic.GenericWorkorderComplete pcCompletePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
        pcCompletePage.clickViewYourPolicy();
        PolicyActivities currentActivities = new PolicyActivities( new PolicySummary(driver).getCurrentActivites_BasicInfo());
        softAssert.assertTrue(currentActivities.getPolicyActivityList().size()==0 , "The activity trigger but should not");
        softAssert.assertAll();
    }


    @Test(enabled = true)
    public void verifyAsCSR_PolicyChangeWithMAilingAddressFromPolicyMemberScreen() throws Exception {
        Agents agent= AgentsHelper.getRandomAgent();
        Config cf = new Config(ApplicationOrCenter.PolicyCenter );
        driver = buildDriver(cf);

        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        PolicyMenu pcPolicyMenu = new PolicyMenu(driver);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        Date policyChangeEffective = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), repository.gw.enums.DateAddSubtractOptions.Day, 4);

        issueAndFlatCancelPolicy(agent, pcSideMenu, pcAccountSummaryPage, pcWorkOrder, pcPolicyMenu, policyChangePage, policyChangeEffective);

        String cSRUsername = "cashcraft";
        new Login(driver).loginAndSearchAccountByAccountNumber(cSRUsername, "gw", myPolicyObject.accountNumber);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(repository.gw.enums.PolicyTermStatus.Canceled);
        SoftAssert softAssert = new SoftAssert();
        // start policy change by CSR
        policyChangePage.startPolicyChange(" policy Change", policyChangeEffective);
        repository.pc.workorders.generic.GenericWorkorderPolicyInfo polInfo = new repository.pc.workorders.generic.GenericWorkorderPolicyInfo(driver);
        pcSideMenu.clickSideMenuPolicyInfo();
        pcWorkOrder.clickGenericWorkorderQuote();
        pcSideMenu.clickSideMenuPolicyInfo();
        new GuidewireHelpers(driver).editPolicyTransaction();
        pcSideMenu.clickSideMenuHouseholdMembers();
        updateMailingAddressOfPolictMember();
        pcSideMenu.clickSideMenuRiskAnalysis();
        pcWorkOrder.clickGenericWorkorderQuote();
        pcSideMenu.clickSideMenuRiskAnalysis();

        pcWorkOrder.clickGenericWorkorderSubmitOptionsSubmit();

        BasePage basePage = new BasePage(driver);

        if (basePage.isAlertPresent()) {
            basePage.selectOKOrCancelFromPopup(OkCancel.OK);
        }


        UWActivityPC activity = new UWActivityPC(driver);
        activity.setText("Sending this over stuff and stuff");
        activity.setChangeReason(repository.gw.enums.ChangeReason.AnyOtherChange);
        activity.clickSendRequest();


        repository.pc.workorders.generic.GenericWorkorderComplete pcCompletePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
        pcCompletePage.clickViewYourPolicy();
        PolicySummary policySummary =new PolicySummary(driver);

        String assignedTo=null;
        PolicyActivities currentActivitiesBeforeIssue = new PolicyActivities( policySummary.getCurrentActivites_BasicInfo());
        for (Activity currentActivity : currentActivitiesBeforeIssue.getPolicyActivityList()) {
            if (currentActivity.getSubject().contains("Submitted or issued policy change on cancelled policy")) {
                 assignedTo = currentActivity.getAssignedTo();
                break;
            }

        }

        Underwriters uw =UnderwritersHelper.getUnderwriterInfoByFullName(assignedTo);
        new GuidewireHelpers(driver).logout();

        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObject.accountNumber);
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct((repository.gw.enums.ProductLineType.Squire));
        pcWorkOrder.clickIssuePolicyButton();
        pcCompletePage.clickViewYourPolicy();
        PolicyActivities currentActivitiesAfterIssue = new PolicyActivities( policySummary.getCurrentActivites_BasicInfo());

        softAssert.assertEquals(currentActivitiesBeforeIssue.getPolicyActivityList().size(),currentActivitiesAfterIssue.getPolicyActivityList().size(), "The activity triggered again after Issue but should not");

        boolean newActivity=false;
            for (Activity AfterActivity : currentActivitiesAfterIssue.getPolicyActivityList()) {
                if (!AfterActivity.getSubject().contains("Submitted or issued policy change on cancelled policy")) {
                    newActivity=true;
                    break;
                }
            }
        softAssert.assertFalse(newActivity , "new Activity Found But Should n't");

        softAssert.assertAll();


    }

    private void updateMailingAddressOfPolictMember() {
        repository.pc.workorders.generic.GenericWorkorderPolicyMembers policyMembersPage = new repository.pc.workorders.generic.GenericWorkorderPolicyMembers(driver);
        int row = policyMembersPage.getPolicyHouseholdMembersTableRow(myPolicyObject.pniContact.getFirstName());
        policyMembersPage.clickPolicyHouseHoldTableCell(row, "Name");
        AddressInfo newAddress= new AddressInfo(true);
        policyMembersPage.setDesignatedNewAddress(newAddress);
        policyMembersPage.clickOK();
    }


    @Test(enabled = true)
    public void verifyAsCSR_PolicyChangeWithMAilingAddressFromPolicyinfoScreen() throws Exception {
        Agents agent= AgentsHelper.getRandomAgent();
        Config cf = new Config(ApplicationOrCenter.PolicyCenter );
        driver = buildDriver(cf);

        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        PolicyMenu pcPolicyMenu = new PolicyMenu(driver);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        Date policyChangeEffective = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), repository.gw.enums.DateAddSubtractOptions.Day, 4);

        issueAndFlatCancelPolicy(agent, pcSideMenu, pcAccountSummaryPage, pcWorkOrder, pcPolicyMenu, policyChangePage, policyChangeEffective);

        String cSRUsername = "cashcraft";
        new Login(driver).loginAndSearchAccountByAccountNumber(cSRUsername, "gw", myPolicyObject.accountNumber);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(repository.gw.enums.PolicyTermStatus.Canceled);
        SoftAssert softAssert = new SoftAssert();
        // start policy change by CSR
        policyChangePage.startPolicyChange(" policy Change", policyChangeEffective);
        repository.pc.workorders.generic.GenericWorkorderPolicyInfo polInfo = new repository.pc.workorders.generic.GenericWorkorderPolicyInfo(driver);
        pcSideMenu.clickSideMenuPolicyInfo();
        pcWorkOrder.clickGenericWorkorderQuote();
        pcSideMenu.clickSideMenuPolicyInfo();
        new GuidewireHelpers(driver).editPolicyTransaction();
        polInfo.clickPolicyInfoPrimaryNamedInsured();
        repository.pc.workorders.generic.GenericWorkorderPolicyInfoContact policyInfoContactPage = new repository.pc.workorders.generic.GenericWorkorderPolicyInfoContact(driver);
        AddressInfo newAddress= new AddressInfo(true);
        policyInfoContactPage.setDesignatedNewAddress(newAddress);
        policyInfoContactPage.clickUpdate();
        pcSideMenu.clickSideMenuRiskAnalysis();
        pcWorkOrder.clickGenericWorkorderQuote();
        pcSideMenu.clickSideMenuRiskAnalysis();

        pcWorkOrder.clickGenericWorkorderSubmitOptionsSubmit();

        BasePage basePage = new BasePage(driver);

        if (basePage.isAlertPresent()) {
            basePage.selectOKOrCancelFromPopup(OkCancel.OK);
        }


        UWActivityPC activity = new UWActivityPC(driver);
        activity.setText("Sending this over stuff and stuff");
        activity.setChangeReason(repository.gw.enums.ChangeReason.AnyOtherChange);
        activity.clickSendRequest();


        repository.pc.workorders.generic.GenericWorkorderComplete pcCompletePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
        pcCompletePage.clickViewYourPolicy();
        PolicySummary policySummary =new PolicySummary(driver);

        String assignedTo=null;
        PolicyActivities currentActivitiesBeforeIssue = new PolicyActivities( policySummary.getCurrentActivites_BasicInfo());
        for (Activity currentActivity : currentActivitiesBeforeIssue.getPolicyActivityList()) {
            if (currentActivity.getSubject().contains("Submitted or issued policy change on cancelled policy")) {
                assignedTo = currentActivity.getAssignedTo();
                break;
            }

        }

        Underwriters uw =UnderwritersHelper.getUnderwriterInfoByFullName(assignedTo);
        new GuidewireHelpers(driver).logout();

        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObject.accountNumber);
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct((repository.gw.enums.ProductLineType.Squire));
        pcWorkOrder.clickIssuePolicyButton();
        pcCompletePage.clickViewYourPolicy();
        PolicyActivities currentActivitiesAfterIssue = new PolicyActivities( policySummary.getCurrentActivites_BasicInfo());

        softAssert.assertEquals(currentActivitiesBeforeIssue.getPolicyActivityList().size(),currentActivitiesAfterIssue.getPolicyActivityList().size(), "The activity triggered again after Issue but should not");

        boolean newActivity=false;
        for (Activity AfterActivity : currentActivitiesAfterIssue.getPolicyActivityList()) {
            if (!AfterActivity.getSubject().contains("Submitted or issued policy change on cancelled policy")) {
                newActivity=true;
                break;
            }
        }
        softAssert.assertFalse(newActivity , "new Activity Found But Should n't");

        softAssert.assertAll();


    }



    private void issueAndFlatCancelPolicy(Agents agent, SideMenuPC pcSideMenu, AccountSummaryPC pcAccountSummaryPage, repository.pc.workorders.generic.GenericWorkorder pcWorkOrder, PolicyMenu pcPolicyMenu, StartPolicyChange policyChangePage, Date policyChangeEffective) throws Exception {
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withAgent(agent)
                .withInsFirstLastName("cancel", "EditAdd")
              .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);

        Underwriters underwriterInfo = UnderwritersHelper.getRandomUnderwriter();
        new Login(driver).loginAndSearchAccountByAccountNumber(underwriterInfo.getUnderwriterUserName(), underwriterInfo.getUnderwriterPassword(),myPolicyObject.accountNumber);
        StartCancellation pcCancellationPage = new StartCancellation(driver);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(repository.gw.enums.PolicyTermStatus.InForce);
        pcPolicyMenu.clickMenuActions();
        pcPolicyMenu.clickCancelPolicy();
        pcCancellationPage.setSourceReasonAndExplanation(repository.gw.enums.Cancellation.CancellationSourceReasonExplanation.OtherLackUWInfo);
        pcCancellationPage.setEffectiveDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), repository.gw.enums.DateAddSubtractOptions.Day, 5));
        pcCancellationPage.setDescription("test edit address after cancel");
        pcCancellationPage.clickStartCancellation();
        pcCancellationPage.clickSubmitOptionsCancelNow();
        new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(repository.gw.enums.PolicyTermStatus.Canceled);
        // start policy change by Agent First
        policyChangePage.startPolicyChange(" policy Change", policyChangeEffective);
        pcSideMenu.clickSideMenuPolicyInfo();
        pcWorkOrder.clickGenericWorkorderQuote();
        pcSideMenu.clickSideMenuRiskAnalysis();
        pcWorkOrder.clickIssuePolicyButton();
        new GuidewireHelpers(driver).logout();
    }


    @Test(enabled = true)
    public void verifyAsSA_policyChangeWithPolicyInfoScreenMailingAddress() throws Exception {


        Agents agent= AgentsHelper.getAgentByUserName("tgallup");


        Config cf = new Config(ApplicationOrCenter.PolicyCenter );
        driver = buildDriver(cf);
        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        PolicyMenu pcPolicyMenu = new PolicyMenu(driver);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        Date policyChangeEffective = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), repository.gw.enums.DateAddSubtractOptions.Day, 4);

        issueAndFlatCancelPolicy(agent, pcSideMenu, pcAccountSummaryPage, pcWorkOrder, pcPolicyMenu, policyChangePage, policyChangeEffective);

        String saUsername = "jgallup";
        new Login(driver).loginAndSearchAccountByAccountNumber(saUsername, "gw", myPolicyObject.accountNumber);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(repository.gw.enums.PolicyTermStatus.Canceled);
        SoftAssert softAssert = new SoftAssert();
        // start policy change by CSR
        policyChangePage.startPolicyChange(" policy Change", policyChangeEffective);
        repository.pc.workorders.generic.GenericWorkorderPolicyInfo polInfo = new repository.pc.workorders.generic.GenericWorkorderPolicyInfo(driver);
        pcSideMenu.clickSideMenuPolicyInfo();
        pcWorkOrder.clickGenericWorkorderQuote();
        pcSideMenu.clickSideMenuPolicyInfo();
        new GuidewireHelpers(driver).editPolicyTransaction();
        polInfo.clickPolicyInfoPrimaryNamedInsured();
        repository.pc.workorders.generic.GenericWorkorderPolicyInfoContact policyInfoContactPage = new repository.pc.workorders.generic.GenericWorkorderPolicyInfoContact(driver);
        AddressInfo newAddress= new AddressInfo(true);
        policyInfoContactPage.setDesignatedNewAddress(newAddress);
        policyInfoContactPage.clickUpdate();
        pcSideMenu.clickSideMenuRiskAnalysis();
        pcWorkOrder.clickGenericWorkorderQuote();
        pcSideMenu.clickSideMenuRiskAnalysis();

        pcWorkOrder.clickGenericWorkorderSubmitOptionsSubmit();

        BasePage basePage = new BasePage(driver);

        if (basePage.isAlertPresent()) {
            basePage.selectOKOrCancelFromPopup(OkCancel.OK);
        }


        UWActivityPC activity = new UWActivityPC(driver);
        activity.setText("Sending this over stuff and stuff");
        activity.setChangeReason(repository.gw.enums.ChangeReason.AnyOtherChange);
        activity.clickSendRequest();


        repository.pc.workorders.generic.GenericWorkorderComplete pcCompletePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
        pcCompletePage.clickViewYourPolicy();
        PolicySummary policySummary =new PolicySummary(driver);

        String assignedTo=null;
        PolicyActivities currentActivitiesBeforeIssue = new PolicyActivities( policySummary.getCurrentActivites_BasicInfo());
        for (Activity currentActivity : currentActivitiesBeforeIssue.getPolicyActivityList()) {
            if (currentActivity.getSubject().contains("Submitted or issued policy change on cancelled policy")) {
                assignedTo = currentActivity.getAssignedTo();
                break;
            }

        }

        Underwriters uw =UnderwritersHelper.getUnderwriterInfoByFullName(assignedTo);
        new GuidewireHelpers(driver).logout();

        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObject.accountNumber);
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct((repository.gw.enums.ProductLineType.Squire));
        pcWorkOrder.clickIssuePolicyButton();
        pcCompletePage.clickViewYourPolicy();
        PolicyActivities currentActivitiesAfterIssue = new PolicyActivities( policySummary.getCurrentActivites_BasicInfo());

        softAssert.assertEquals(currentActivitiesBeforeIssue.getPolicyActivityList().size(),currentActivitiesAfterIssue.getPolicyActivityList().size(), "The activity triggered again after Issue but should not");

        boolean newActivity=false;
        for (Activity AfterActivity : currentActivitiesAfterIssue.getPolicyActivityList()) {
            if (!AfterActivity.getSubject().contains("Submitted or issued policy change on cancelled policy")) {
                newActivity=true;
                break;
            }
        }
        softAssert.assertFalse(newActivity , "new Activity Found Byt Should n't");

        softAssert.assertAll();


    }

    @Test(enabled = true)
    public void verifyAsSA_PolicyChangeWithMAilingAddressFromPolicyMemberScreen() throws Exception {
        Agents agent= AgentsHelper.getAgentByUserName("tgallup");

        Config cf = new Config(ApplicationOrCenter.PolicyCenter );
        driver = buildDriver(cf);

        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        PolicyMenu pcPolicyMenu = new PolicyMenu(driver);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        Date policyChangeEffective = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), repository.gw.enums.DateAddSubtractOptions.Day, 4);

        issueAndFlatCancelPolicy(agent, pcSideMenu, pcAccountSummaryPage, pcWorkOrder, pcPolicyMenu, policyChangePage, policyChangeEffective);

        String saUsername = "jgallup";
        new Login(driver).loginAndSearchAccountByAccountNumber(saUsername, "gw", myPolicyObject.accountNumber);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(repository.gw.enums.PolicyTermStatus.Canceled);
        SoftAssert softAssert = new SoftAssert();
        // start policy change by CSR
        policyChangePage.startPolicyChange(" policy Change", policyChangeEffective);
        repository.pc.workorders.generic.GenericWorkorderPolicyInfo polInfo = new repository.pc.workorders.generic.GenericWorkorderPolicyInfo(driver);
        pcSideMenu.clickSideMenuPolicyInfo();
        pcWorkOrder.clickGenericWorkorderQuote();
        pcSideMenu.clickSideMenuPolicyInfo();
        new GuidewireHelpers(driver).editPolicyTransaction();
        pcSideMenu.clickSideMenuHouseholdMembers();
        updateMailingAddressOfPolictMember();
        pcSideMenu.clickSideMenuRiskAnalysis();
        pcWorkOrder.clickGenericWorkorderQuote();
        pcSideMenu.clickSideMenuRiskAnalysis();

        pcWorkOrder.clickGenericWorkorderSubmitOptionsSubmit();

        BasePage basePage = new BasePage(driver);

        if (basePage.isAlertPresent()) {
            basePage.selectOKOrCancelFromPopup(OkCancel.OK);
        }


        UWActivityPC activity = new UWActivityPC(driver);
        activity.setText("Sending this over stuff and stuff");
        activity.setChangeReason(repository.gw.enums.ChangeReason.AnyOtherChange);
        activity.clickSendRequest();


        repository.pc.workorders.generic.GenericWorkorderComplete pcCompletePage = new GenericWorkorderComplete(driver);
        pcCompletePage.clickViewYourPolicy();
        PolicySummary policySummary =new PolicySummary(driver);

        String assignedTo=null;
        PolicyActivities currentActivitiesBeforeIssue = new PolicyActivities( policySummary.getCurrentActivites_BasicInfo());
        for (Activity currentActivity : currentActivitiesBeforeIssue.getPolicyActivityList()) {
            if (currentActivity.getSubject().contains("Submitted or issued policy change on cancelled policy")) {
                assignedTo = currentActivity.getAssignedTo();
                break;
            }

        }

        Underwriters uw =UnderwritersHelper.getUnderwriterInfoByFullName(assignedTo);
        new GuidewireHelpers(driver).logout();

        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObject.accountNumber);
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct((repository.gw.enums.ProductLineType.Squire));
        pcWorkOrder.clickIssuePolicyButton();
        pcCompletePage.clickViewYourPolicy();
        PolicyActivities currentActivitiesAfterIssue = new PolicyActivities( policySummary.getCurrentActivites_BasicInfo());

        softAssert.assertEquals(currentActivitiesBeforeIssue.getPolicyActivityList().size(),currentActivitiesAfterIssue.getPolicyActivityList().size(), "The activity triggered again after Issue but should not");

        boolean newActivity=false;
        for (Activity AfterActivity : currentActivitiesAfterIssue.getPolicyActivityList()) {
            if (!AfterActivity.getSubject().contains("Submitted or issued policy change on cancelled policy")) {
                newActivity=true;
                break;
            }
        }
        softAssert.assertFalse(newActivity , "new Activity Found But Should n't");

        softAssert.assertAll();


    }




    @Test(enabled = true)
    public void verifyAsSA_PolicyChangeCoverage_Submit_disable() throws Exception {
        Agents agent= AgentsHelper.getAgentByUserName("tgallup");

        Config cf = new Config(ApplicationOrCenter.PolicyCenter );
        driver = buildDriver(cf);

        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        PolicyMenu pcPolicyMenu = new PolicyMenu(driver);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        Date policyChangeEffective = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), repository.gw.enums.DateAddSubtractOptions.Day, 4);

        issueAndFlatCancelPolicy(agent, pcSideMenu, pcAccountSummaryPage, pcWorkOrder, pcPolicyMenu, policyChangePage, policyChangeEffective);

        String saUsername = "jgallup";
        new Login(driver).loginAndSearchAccountByAccountNumber(saUsername, "gw", myPolicyObject.accountNumber);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(repository.gw.enums.PolicyTermStatus.Canceled);
        SoftAssert softAssert = new SoftAssert();
        // start policy change by CSR
        policyChangePage.startPolicyChange(" policy Change", policyChangeEffective);
        repository.pc.workorders.generic.GenericWorkorderPolicyInfo polInfo = new repository.pc.workorders.generic.GenericWorkorderPolicyInfo(driver);
        pcSideMenu.clickSideMenuPolicyInfo();
        pcWorkOrder.clickGenericWorkorderQuote();
        pcSideMenu.clickSideMenuPolicyInfo();
        new GuidewireHelpers(driver).editPolicyTransaction();
        pcSideMenu.clickSideMenuHouseholdMembers();
        pcSideMenu.clickSideMenuSquirePropertyCoverages();
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyCoverages pcSquirePropertyCoveragesPage = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyCoverages(driver);
        pcSquirePropertyCoveragesPage.selectSectionIDeductible(repository.gw.enums.Property.SectionIDeductible.OneThousand);
        pcWorkOrder.clickGenericWorkorderQuote();
        BasePage basePage = new BasePage(driver);

        // Acceptance criteria: submit button should be showing and disable
        Assert.assertTrue(basePage.checkIfElementExists("//a[contains(@id, 'RequestApproval')]", 5), "Submit button was not visible, it should be");
        Assert.assertTrue(basePage.checkIfElementExists("//a[contains(@id, 'RequestApproval')][contains(@class, 'x-disabled')]", 5), "Submit button was not disabled, it should  be");

    }


    @Test(enabled = true)
    public void verifyAsCSR_PolicyChangeCoverage_Submit_disable() throws Exception {
        Agents agent= AgentsHelper.getRandomAgent();

        Config cf = new Config(ApplicationOrCenter.PolicyCenter );
        driver = buildDriver(cf);

        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        PolicyMenu pcPolicyMenu = new PolicyMenu(driver);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        Date policyChangeEffective = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), repository.gw.enums.DateAddSubtractOptions.Day, 4);

        issueAndFlatCancelPolicy(agent, pcSideMenu, pcAccountSummaryPage, pcWorkOrder, pcPolicyMenu, policyChangePage, policyChangeEffective);

        String cSRUsername = "cashcraft";
        new Login(driver).loginAndSearchAccountByAccountNumber(cSRUsername, "gw", myPolicyObject.accountNumber);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(repository.gw.enums.PolicyTermStatus.Canceled);
        SoftAssert softAssert = new SoftAssert();
        // start policy change by CSR
        policyChangePage.startPolicyChange(" policy Change", policyChangeEffective);
        repository.pc.workorders.generic.GenericWorkorderPolicyInfo polInfo = new repository.pc.workorders.generic.GenericWorkorderPolicyInfo(driver);
        pcSideMenu.clickSideMenuPolicyInfo();
        pcWorkOrder.clickGenericWorkorderQuote();
        pcSideMenu.clickSideMenuPolicyInfo();
        new GuidewireHelpers(driver).editPolicyTransaction();
        pcSideMenu.clickSideMenuHouseholdMembers();
        pcSideMenu.clickSideMenuSquirePropertyCoverages();
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyCoverages pcSquirePropertyCoveragesPage = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyCoverages(driver);
        pcSquirePropertyCoveragesPage.selectSectionIDeductible(repository.gw.enums.Property.SectionIDeductible.OneThousand);
        pcWorkOrder.clickGenericWorkorderQuote();
        BasePage basePage = new BasePage(driver);

        // Acceptance criteria: submit button should be showing and disable
        Assert.assertTrue(basePage.checkIfElementExists("//a[contains(@id, 'RequestApproval')]", 5), "Submit button was not visible, it should be");
        Assert.assertTrue(basePage.checkIfElementExists("//a[contains(@id, 'RequestApproval')][contains(@class, 'x-disabled')]", 5), "Submit button was not disabled, it should  be");

    }


}

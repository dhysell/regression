package regression.r2.noclock.policycenter.other;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.activity.ActivityPopup;
import repository.gw.administration.AdminEventMessages;
import repository.gw.enums.ActivtyRequestType;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.MessageQueue;
import repository.gw.enums.MessageQueuesSafeOrderObjectLinkOptions;
import repository.gw.enums.MessageQueuesSafeOrderObjectSelectOptions;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.actions.ActionsPC;
import repository.pc.activity.GenericActivityPC;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuAdministrationPC;
import scratchpad.jon.mine.GenerateTestPolicy;

/**
 * @Author jlarsen
 * @Requirement Include policy number and name of insured on escalated
 * activities and their emails
 * @RequirementsLink <a href=
 * "http://projects.idfbins.com/policycenter/To%20Be%20Process/Requirements%20Documentation/13.0%20-%20Other%20Policy%20Actions.pptx">
 * 13.1</a>
 * @Description - This class is split up more than really required for ease of
 * adding additional test to it later as needed. //EXAMPLE EMAIL
 * PAYLOAD <?xml version="1.0"?> <emailmessage subject=
 * "PolicyCenter - Activity Assigned"
 * > <sender emailaddress="guidewire@idfbins.com" name=
 * "Guidewire PolicyCenter"
 * /> <replyto emailaddress="qawizpro@idfbins.com" name=
 * "Heidi Hill"/> <body>You have received an activity in
 * PolicyCenter for Account #244582 Named Insured "OOS Change".
 * <p>
 * Please go to PolicyCenter and review.</body>
 * <recipient emailaddress="qawizpro@idfbins.com" name=
 * "Bill Terry" type="TO"/> </emailmessage>
 * @DATE Sep 29, 2015
 * @throws Exception
 */
@QuarantineClass
public class PolicyActivities extends BaseTest {

    GeneratePolicy myPolicyObj = null;
    boolean restartMessageQueue = true;
    private WebDriver driver;

    /**
     * @throws Exception
     * @Author jlarsen
     * @Requirement Include policy number and name of insured on escalated
     * activities and their emails
     * @RequirementsLink <a href=
     * "http://projects.idfbins.com/policycenter/To%20Be%20Process/Requirements%20Documentation/13.0%20-%20Other%20Policy%20Actions.pptx">
     * 13.1</a>
     * @Description - this afterclass method is to ensure that the email message
     * queue gets restarted.
     * @DATE Sep 29, 2015
     */
    @AfterClass

    public void afterClass() throws Exception {
        if (restartMessageQueue) {
            Config cf = new Config(ApplicationOrCenter.PolicyCenter);
            driver = buildDriver(cf);

            // login a SU
            new Login(driver).login("su", "gw");
            // suspend email
            TopMenuAdministrationPC topMenu = new TopMenuAdministrationPC(driver);
            topMenu.clickMessageQueues();
            AdminEventMessages mQues = new AdminEventMessages(driver);
            mQues.resumeQueue(MessageQueue.Email_PC);
            // logout
            new GuidewireHelpers(driver).logout();


        }
    }


    // creates the policy simple.
    @Test(enabled = true)
    public void createPolicy() throws Exception {

        GenerateTestPolicy policy = new GenerateTestPolicy();
        policy.generateRandomPolicy(1, 1, 1, "General Activities");
        this.myPolicyObj = policy.myPolicy;

        System.out.println(myPolicyObj.accountNumber);
        System.out.println(myPolicyObj.agentInfo.getAgentUserName());
    }

    // create an Activity with due date and escalation date set to yesterday so
    // no clock moves are needed.
    @Test(dependsOnMethods = {"createPolicy"})
    public void createActivity() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Login login = new Login(driver);
        login.login("su", "gw");
        // suspend email message queue
        TopMenuAdministrationPC topMenu = new TopMenuAdministrationPC(driver);
        topMenu.clickMessageQueues();
        try {
            AdminEventMessages mQues = new AdminEventMessages(driver);
            mQues.suspendQueue(MessageQueue.Email_PC);
        } catch (GuidewireException e) {
            // Assumed that exception is throw because message queue is already
            // suspended.
            restartMessageQueue = false;
        }
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();

        // create activity on account
        login.loginAndSearchPolicyByAccountNumber("hhill", "gw", myPolicyObj.accountNumber);
        ActionsPC actions = new ActionsPC(driver);
        actions.requestActivity(ActivtyRequestType.SignatureNeeded);

        GenericActivityPC activity = new GenericActivityPC(driver);
        activity.setSubject("Escalation Email Activity");
        activity.setText("this activity is to test the Activities functionality");
        activity.setTargetDueDate(DateUtils.dateFormatAsString("MMddyyyy", DateUtils
                .dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -2)));
        activity.clickOK();

        // verify activity was created
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuToolsDocuments();
        sideMenu.clickSideMenuToolsSummary();

        PolicySummary policySummary = new PolicySummary(driver);
        if (!policySummary.checkIfActivityExists("Signature Needed")) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber +
                    "Test Activity did not get created.");
        }
        guidewireHelpers.logout();
        login.loginAndSearchPolicyByAccountNumber(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
        PolicySummary summaryPage = new PolicySummary(driver);
        summaryPage.clickActivity("Signature Needed");

        ActivityPopup activityPopup = new ActivityPopup(driver);
        String activityDescription = activityPopup.getActivityDescription();
        if (!activityDescription.equals("A signature page is needed to process the policy.")) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber + "Ensure that the description of the Signature Needed Activity is: A signature page is needed to process the policy.");
        }
        guidewireHelpers.logout();
    }

    // verify that an email was sent to the agent on the creation of the
    // activity
    // since there is no defining characteristics in the message queue elements
    // the test must loop through all the
    // elements till it finds the one it needs.
    // This test is disabled due to slow message queues
    @Test(dependsOnMethods = {"createActivity"}, enabled = false)
    public void emailAgentOnActivity() throws Exception {
        boolean emailFound = false;

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).login("su", "gw");

        // verify agent was emailed about activity with account info
        TopMenuAdministrationPC topMenu = new TopMenuAdministrationPC(driver);
        topMenu.clickMessageQueues();

        AdminEventMessages mQues = new AdminEventMessages(driver);
        mQues.clickSafeOrderObjectLink(MessageQueuesSafeOrderObjectLinkOptions.Outbound_Email);
        mQues.selectSafeOrderObjectFilterOption(MessageQueuesSafeOrderObjectSelectOptions.Safe_Order_Object_With_Any_Unfinished_Messages);
        mQues.clickAccountNumberLink(myPolicyObj.accountNumber);
        int listSize = mQues.getDestinationList().size();
        for (int i = 0; i < listSize; i++) {
            List<WebElement> myList = mQues.getDestinationList();
            myList.get(i).click();
            // get mesage payload text
            mQues = new AdminEventMessages(driver);
            String payload = mQues.getMessagePayload();
            // click return to account
            mQues.returnTo();
            // CHECK PAYLOAD TEXT
            if (!payload.contains(myPolicyObj.agentInfo.getAgentFirstName())
                    || !payload.contains(myPolicyObj.agentInfo.getAgentLastName())) {
                continue;
            }
            if (!payload.contains(myPolicyObj.accountNumber)) {
                continue;
            }
            if (myPolicyObj.pniContact.getCompanyName() != null) {
                if (!payload.contains(myPolicyObj.pniContact.getCompanyName())) {
                    continue;
                }
            } else {
                if (!payload.contains(myPolicyObj.pniContact.getFirstName()) && !payload.contains(myPolicyObj.pniContact.getLastName())) {
                    continue;
                }
            }
            emailFound = true;
        }
        if (!emailFound) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber +
                    "No email was found that matches the required information.");
        }
    }

    // run batch process to escalate activity
    @Test(dependsOnMethods = {"createActivity"})
    public void escalateActivity() throws Exception {
        // run escalate activity batch
        System.out.println("Run Escalate Activity Batch");
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new BatchHelpers(driver).runBatchProcess(BatchProcess.Activity_Escalation);

        new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchPolicyByAccountNumber("hhill", "gw", myPolicyObj.accountNumber);

        // verify activity escalation
        PolicySummary policySummary = new PolicySummary(driver);
        // EXAMPLE ESCALATED ACTIVITY SUBJECT - "Activity Escalated on 4/27 -
        // Signature Needed, User: Tom Gallup";

        if (policySummary.checkIfActivityExists("Activity Escalated")) {
            if (!policySummary.checkIfActivityExists("Signature Needed, User:")) {
                Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber + "Did not create Escalated Activity");
            }
        } else {
            Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber + "Did not create Escalated Activity");
        }
    }


    /**
     * @Author jlarsen
     * @Requirement complete the original activity and verify that the escalated
     * activity get closed.
     * @RequirementsLink <a href=
     * "http://projects.idfbins.com/policycenter/To%20Be%20Process/Requirements%20Documentation/13.0%20-%20Other%20Policy%20Actions.pptx">
     * 13.1</a>
     * @Description - complete the original activity and verify that the
     * escalated activity get closed. When an activity that has
     * escalated is completed, complete the "escalated activity"
     * activity that had gone to their supervisor
     * @DATE Sep 29, 2015
     */
    @Test(dependsOnMethods = {"escalateActivity"})
    public void completeAgentActivity() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObj.agentInfo.getAgentUserName(),
                myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        PolicySummary policySummary = new PolicySummary(driver);
        policySummary.clickActivity("Signature Needed");

        GenericActivityPC activity = new GenericActivityPC(driver);
        activity.completeActivity();

        // Return to policy summary
        SearchPoliciesPC search = new SearchPoliciesPC(driver);
        search.searchPolicyByAccountNumber(myPolicyObj.accountNumber);

        // verify escalated activity removed. Activity Escalated on 11/16 -
        // Signature Needed, User: Neil Hazelbaker
        if (policySummary.checkIfActivityExists(" Activity Escalated")) {
            if (policySummary.checkIfActivityExists("Signature Needed, User:")) {
                Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber + "Escalated activity was not closed when main activity was completed.");
            }
        }
    }

}

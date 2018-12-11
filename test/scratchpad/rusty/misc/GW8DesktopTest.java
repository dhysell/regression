package scratchpad.rusty.misc;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.desktop.DesktopBulkAgentChange;
import repository.pc.desktop.DesktopMyActivitiesPC;
import repository.pc.desktop.DesktopMyOtherWorkOrders;
import repository.pc.desktop.DesktopMyQueuesPC;
import repository.pc.desktop.DesktopMyRenewals;
import repository.pc.desktop.DesktopMySubmissions;
import repository.pc.desktop.DesktopSideMenuPC;
import repository.pc.desktop.DesktopUnattachedDocumentsPC;

//@Listeners(listeners.Listener.class)
public class GW8DesktopTest extends BaseTest {
    private WebDriver driver;


    @Test
    public void MyActiviesReviewSubmissonTest() throws Exception {
        //Configuration config = Configuration.getInstance();
        // clicks on the My Activities sidebar link
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).login("hhill", "gw");
        DesktopSideMenuPC myActivities = new DesktopSideMenuPC(driver);
        myActivities.clickMyActivities();

        // Does an action inside the page
        DesktopMyActivitiesPC activities = new DesktopMyActivitiesPC(driver);
        activities.setDesktopMyActivitiesStatusResultsFilter("My Open");

        // Does an action inside the page
        //IDesktopMyActivities checkbox = DesktopFactory.getDesktopMyActivitiesPage();
        //checkbox.clickDesktopMyActivitiesCheckboxFirst();

        //	IDesktopMyActivities assign = DesktopFactory.getDesktopMyActivitiesPage();
        //	assign.clickDesktopMyActivitiesResultsAssign();

        // IDesktopMyActivitiesAssignActivities backToDesktop =
        // DesktopFactory.getDesktopMyActivitiesAssignActivitiesPage();
        // backToDesktop.clickDesktopMyActivitiesAssignActivitiesReturnToActivitiesLink();
        // IDesktopMyActivitiesAssignActivities assign =
        // DesktopFactory.getDesktopMyActivitiesAssignActivitiesPage();
        // assign.setDesktopMyActivitiesAssignActivities("My Open");

        // clicks on the My Submissions sidebar link
        DesktopSideMenuPC mySubmissions = new DesktopSideMenuPC(driver);
        mySubmissions.clickMySubmissions();

        // Does an action inside the page
        DesktopMySubmissions submissions = new DesktopMySubmissions(driver);
        submissions.setDesktopMySubmissionsOptions("Open bound");

        // clicks on the My Renewals sidebar link
        DesktopSideMenuPC myRenewals = new DesktopSideMenuPC(driver);
        myRenewals.clickMyRenewals();

        // Does an action inside the page
        DesktopMyRenewals renewals = new DesktopMyRenewals(driver);
        renewals.setDesktopMyRenewalsStatusResultsFilter("Created in past 7 days");

        // clicks on the My Other Work Orders sidebar link
        DesktopSideMenuPC myOtherWorkOrders = new DesktopSideMenuPC(driver);
        myOtherWorkOrders.clickMyOtherWorkOrders();

        // Does an action inside the page
        DesktopMyOtherWorkOrders otherWorkOrders = new DesktopMyOtherWorkOrders(driver);
        otherWorkOrders.setDesktopMyOtherWorkOrdersFilter("Created in past 7 days");

        // clicks on the My Queues sidebar link
        DesktopSideMenuPC myQueues = new DesktopSideMenuPC(driver);
        myQueues.clickMyQueues();

        DesktopMyQueuesPC filter = new DesktopMyQueuesPC(driver);
        filter.setDesktopMyActivitiesMyQueuesFilter("Special");

        DesktopMyQueuesPC queues = new DesktopMyQueuesPC(driver);
        queues.setDesktopMyActivitiesMyQueuesStatusFilter("Skipped");

        // clicks on the Unattached Documents sidebar link
        DesktopSideMenuPC UnattachedDocuments = new DesktopSideMenuPC(driver);
        UnattachedDocuments.clickUnattachedDocuments();

//					// Does an action inside the page
        DesktopUnattachedDocumentsPC docs = new DesktopUnattachedDocumentsPC(driver);
        docs.clickDesktopUnattachedDocuments();
        //
//					// Does an action inside the page
//					IDesktopUnattachedDocuments filterStatus = DesktopFactory.getDesktopUnattachedDocuments();
//					filterStatus.clickDesktopUnattachedDocumentsStatus("All");
//					delay (1000);
//					
//					// Returns to Unattached Documents Page
//					IDesktopUnattachedDocuments back = DesktopFactory.clickReturnToDesktopUnattachedDocuments();
//					back.clickReturnToDesktopUnattachedDocuments();
//					delay (1000);

        // clicks on the Proof of Mail sidebar link
        DesktopSideMenuPC ProofOfMail = new DesktopSideMenuPC(driver);
        ProofOfMail.clickProofOfMail();

//					// Does an action inside the page
//					IDesktopProofOfMail mail = DesktopFactory.getDesktopProofOfMail();
//					mail.clickDesktopProofOfMailCheckbox();
//					delay (1000);

        // IDesktopProofOfMail preview = DesktopFactory.getDesktopProofOfMail();
        // preview.clickDesktopProofOfMailPreview();

        // clicks on the Bulk Agent Change sidebar link
        DesktopSideMenuPC BulkAgent = new DesktopSideMenuPC(driver);
        BulkAgent.clickBulkAgentChange();

        // Does an action inside the page
        DesktopBulkAgentChange agent = new DesktopBulkAgentChange(driver);
        agent.setDesktopBulkAgentPrintExportSearch("Failed");
    }
}

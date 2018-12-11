package regression.r2.noclock.policycenter.change.subgroup10;


import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchSource;
import repository.gw.enums.BatchType;
import repository.gw.enums.Role;
import repository.gw.errorhandling.ErrorHandling;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.gw.topinfo.TopInfo;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountContactsPC;
import repository.pc.account.AccountSummaryPC;
import repository.pc.account.AccountsSideMenuPC;
import repository.pc.desktop.DesktopBulkAgentChange;
import repository.pc.desktop.DesktopSideMenuPC;
import repository.pc.search.SearchAccountsPC;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.topmenu.TopMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AgentsHelper;
import scratchpad.jon.mine.GenerateTestPolicy;

/*
 * Jon Larsen 5/28/2015 This test set is to do an Agent Change via Policy
 * change. First Generates a policy to make changes to Either logs in as hhill
 * or Agency Manager and does that agent change Then verifies that all the
 * changes occurred in PC and AB PC: Account Summary, Account - Contacts Details
 * Tab, Account - Contacts Agents Tab AB: Account Contact Basic US4364
 */
// Reviewed - 06/04/2015 By Brett Hiltbrand. Ready to move to regression once
// above defect is fixed.
@QuarantineClass
public class AgentOfServiceChange extends BaseTest {
	private WebDriver driver;
    GeneratePolicy myPolicyObj = null;
    Agents oldAgent = null;
    Agents newAgent = null;

    // Generate new policy to make changes to
    @Test(enabled = true)
    public void generatePolicy() throws Exception {

        GenerateTestPolicy policy = new GenerateTestPolicy();
        policy.generateRandomPolicy(1, 1, 1, "ServiceAgent Change");
        this.myPolicyObj = policy.myPolicy;

        oldAgent = myPolicyObj.agentInfo;

        System.out.println(myPolicyObj.accountNumber);
        System.out.println(myPolicyObj.agentInfo.getAgentFirstName() + " " + myPolicyObj.agentInfo.getAgentLastName());
        System.out.println(myPolicyObj.agentInfo.getAgentNum());
    }

    // change Agent of Service as either Heidi Hill or as Agency Manager
    // IF AGENCY MANAGER - extra step have to be done to issue the change
    // Agent is chosen at random from within the Agency
    // IF HHILL - Agent is chosen at random from all Agents
    @Test(dependsOnMethods = {"generatePolicy"}, enabled = true)
    public void changeAgentOfService() throws Exception {

        newAgent = AgentsHelper.getRandomAgent();

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(2);

        if (index > 1) {
            System.out.println("Loggin in as hhill");
            new Login(driver).login("hhill", "gw");

            newAgent = AgentsHelper.getRandomAgent();

            System.out.println("New Random Agent Data");
            System.out.println(newAgent.getAgentFirstName() + " " + newAgent.getAgentLastName());
            System.out.println(newAgent.getAgentNum());
        } else {
            System.out.println("Loggin in as Agency Manager");
            // login(AgentsHelper.getAgencyManager(myPolicyObj.agentInfo.getAgencyName()).getAgentUserName(),
            // "gw");
            new Login(driver).login("hhill", "gw");

            newAgent = AgentsHelper.getRandomAgent();

            System.out.println("New Random Agent Data from Agency");
            System.out.println(newAgent.getAgentFirstName() + " " + newAgent.getAgentLastName());
            System.out.println(newAgent.getAgentNum());
        }

        SearchPoliciesPC search = new SearchPoliciesPC(driver);
        search.searchPolicyByAccountNumber(myPolicyObj.accountNumber);

        StartPolicyChange change = new StartPolicyChange(driver);
        changeAgent(newAgent, myPolicyObj);

        myPolicyObj.agentInfo = newAgent;

        TopInfo topInfoStuff = new TopInfo(driver);
        topInfoStuff.clickTopInfoLogout();

    }
    
    
    public void changeAgent(Agents newAgent, GeneratePolicy myPolicyObj) throws Exception {
    	StartPolicyChange change = new StartPolicyChange(driver);
    	change.startPolicyChange("Change Agent", null);

        try {
            // change agent of service
            GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
            policyInfo.setAgentOfService(newAgent);
            change.quoteAndIssue();
            myPolicyObj.agentInfo = newAgent;
        } catch (Exception e) {
            System.out.println("Error occured on 'changeAgent'.");
            change.clickWithdrawTransaction();
            e.printStackTrace();
            Assert.fail("Error occured while trying change Policy Agent.");
        }
    }

    // Verify that the agent changes happened in all required places
    @Test(dependsOnMethods = {"changeAgentOfService"}, enabled = true)
    public void verifyAgentOfServiceChanges() throws Exception {

        Boolean testFailed = false;
        String failMessage = "";

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).login("hhill", "gw");
        System.out.println("Logged in as hhill");

        SearchAccountsPC search = new SearchAccountsPC(driver);
        search.searchAccountByAccountNumber(myPolicyObj.accountNumber);

        TopMenuPC topMenu = new TopMenuPC(driver);
        topMenu.clickAccountTab();

        AccountSummaryPC summary = new AccountSummaryPC(driver);
        String accountSummaryAgentName = summary.getAgentName();
        String accountSummaryAgentNumber = summary.getAgentNumber();

        System.out.println("PC Account summary: " + accountSummaryAgentName + accountSummaryAgentNumber);

        AccountsSideMenuPC sidebar = new AccountsSideMenuPC(driver);
        sidebar.clickContacts();

        AccountContactsPC contacts = new AccountContactsPC(driver);
        contacts.clickContactDetailTab();
        String accountContactsDetails = contacts.getAssignedAgent();

        System.out.println("PC Account ContactDetails: " + accountContactsDetails);

        contacts.clickAgentsTab();
        String accountContactsAgentsName = contacts.getAccountContactsNameByRole(Role.Agent);
        String accountContactsAgentNumber = contacts.getAccountContactsNumberByRole(Role.Agent);

        System.out.println("PC Account Contact Agents: " + accountContactsAgentsName + accountContactsAgentNumber);

        TopInfo topInfoStuff = new TopInfo(driver);
        topInfoStuff.clickTopInfoLogout();
        driver.quit();
        
        cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);

        new Login(driver).login("kharrild", "gw");

        TopMenuAB abTopMenu = new TopMenuAB(driver);
        abTopMenu.clickSearchTab();

        AdvancedSearchAB advSearch = new AdvancedSearchAB(driver);
        advSearch.searchByAccountNumber(myPolicyObj.accountNumber);
        ContactDetailsBasicsAB contactBasics = new ContactDetailsBasicsAB(driver);
        String contactBasicsAgent = contactBasics.getContactDetailsBasicsAgent();

        System.out.println("Contact Manager: " + contactBasicsAgent);

        String policyAgent = myPolicyObj.agentInfo.getAgentFirstName() + " " + myPolicyObj.agentInfo.getAgentLastName();
        String policyAgentNumber = myPolicyObj.agentInfo.getAgentNum();

        // ACCOUNT SUMMARY
        if (!(policyAgent.contains(myPolicyObj.agentInfo.getAgentFirstName()))
                && !(policyAgent.contains(myPolicyObj.agentInfo.getAgentLastName()))) {
            failMessage = failMessage + "\n Account Summary Page, Displays Wrong Agent Name (" + policyAgent + " / "
                    + accountSummaryAgentName + ")";
            testFailed = true;
        }
        if (!(policyAgentNumber).contains(accountSummaryAgentNumber)) {
            failMessage = failMessage + "\n Account Summary Page, Displays Wrong Agent Number (" + policyAgentNumber
                    + " / " + accountSummaryAgentNumber + ")";
            testFailed = true;
        }

        // ACCOUNT CONTACTS CONTACT DETAILS
        if (!(accountContactsDetails.contains(myPolicyObj.agentInfo.getAgentFirstName()))
                && !(accountContactsDetails.contains(myPolicyObj.agentInfo.getAgentLastName()))) {
            failMessage = failMessage + "\n Account Contacts Contact Details Tab, Displays Wrong Agent ("
                    + accountContactsDetails + " / " + policyAgent + ")";
            testFailed = true;
        }
        if (!(accountContactsDetails.contains(policyAgentNumber))) {
            failMessage = failMessage + "\n Account Contacts Contact Details Tab, Displays Wrong Agent Number ("
                    + accountContactsDetails + " / " + policyAgentNumber + ")";
            testFailed = true;
        }

        // ACCOUNT CONTACTS AGENTS
        if (!(accountContactsAgentsName.contains(myPolicyObj.agentInfo.getAgentFirstName()))
                && !(accountContactsAgentsName.contains(myPolicyObj.agentInfo.getAgentLastName()))) {
            failMessage = failMessage + "\n Account Contacts Agents Tab, Displays Wrong Agent ("
                    + accountContactsAgentsName + " / " + policyAgent + ")";
            testFailed = true;
        }
        if (!(accountContactsAgentNumber.contains(policyAgentNumber))) {
            failMessage = failMessage + "\n Account Contacts Agents Tab, Displays Wrong Agent Number ("
                    + accountContactsAgentNumber + " / " + policyAgentNumber + ")";
            testFailed = true;
        }

        // CONTACT MANAGER CONTACT DETAIL
        if (!(contactBasicsAgent.contains(myPolicyObj.agentInfo.getAgentFirstName()))
                && !(contactBasicsAgent.contains(myPolicyObj.agentInfo.getAgentLastName()))) {
            failMessage = failMessage + "\n Contact Manager Contact Details, Displays Wrong Agent ("
                    + contactBasicsAgent + " / " + policyAgent + ")";
            testFailed = true;
        }
        if (!(contactBasicsAgent.contains(policyAgentNumber))) {
            failMessage = failMessage + "\n Contact Manager Contact Details, Displays Wrong Agent Number ("
                    + contactBasicsAgent + " / " + policyAgentNumber + ")";
            testFailed = true;
        }

        topInfoStuff = new TopInfo(driver);
        topInfoStuff.clickTopInfoLogout();

        if (testFailed) {
            throw new Exception(
                    "TEST FAILED FOR THE FOLLOWING REASONS: \n" + failMessage + "\n" + myPolicyObj.accountNumber);
        }
    }

    // this test is to create a batch item for bulk agent change.
    // to ensure that and bulk agent change by agent number isn't selected the
    // BatchSourse Enum is used where "By Agent Number" is not available for
    // use.
    @Test(dependsOnMethods = {"generatePolicy"}, enabled = true)
    public void bulkAgentChange() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Agents newBulkAgent = AgentsHelper.getRandomAgent();

        new Login(driver).login("psorensen", "gw");
        System.out.println("Logged in as psorensen");
        System.out.println("New Bulk Agent");
        System.out.println(newBulkAgent.getAgentFirstName() + " " + newBulkAgent.getAgentLastName() + " "
                + newBulkAgent.getAgentNum());

        TopMenuPC topMenu = new TopMenuPC(driver);
        topMenu.clickDesktopTab();

        DesktopSideMenuPC sidebar = new DesktopSideMenuPC(driver);
        sidebar.clickBulkAgentChange();

        DesktopBulkAgentChange bulkAgentChange = new DesktopBulkAgentChange(driver);
        bulkAgentChange.clickDesktopBulkAgentCreateNewBatch();
        bulkAgentChange.setBatchType(BatchType.SpecificDateChange);

        Map<ApplicationOrCenter, Date> datesMap = ClockUtils.getCurrentDates(driver);
        Date pcDate = datesMap.get(ApplicationOrCenter.PolicyCenter);
        String currentSystemYear = DateUtils.dateFormatAsString("MM/dd/yyyy", pcDate);

        bulkAgentChange.setBatchTargetDate(currentSystemYear);
        bulkAgentChange.setBatchSource(BatchSource.ListOfPolicies);
        bulkAgentChange.setPolicyList(myPolicyObj.busOwnLine.getPolicyNumber());
        bulkAgentChange.clickNewAgentSearch();
        bulkAgentChange.setAgentNumber(newBulkAgent.getAgentNum());
        bulkAgentChange.clickSearch();
        bulkAgentChange.clickNewAgentResults();
        bulkAgentChange.clickSubmitBatch();
    }

    /**
     * DE2535 Tests if an invalid policy number will create a DB Constraint
     * Exception Added by Dan 8/28/15
     * @throws Exception 
     */
    @Test
    public void testbulkAgentChange() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).login("hhill", "gw");

        DesktopSideMenuPC desktopSidebar = new DesktopSideMenuPC(driver);
        desktopSidebar.clickBulkAgentChange();

        DesktopBulkAgentChange desktopBulkAgent = new DesktopBulkAgentChange(driver);
        desktopBulkAgent.clickDesktopBulkAgentCreateNewBatch();
        desktopBulkAgent.setBatchType(BatchType.SpecificDateChange);
        desktopBulkAgent.setBatchSource(BatchSource.ListOfPolicies);
        desktopBulkAgent.setPolicyList("08-123456-02");
        desktopBulkAgent.clickNewAgentSearch();
        desktopBulkAgent.setAgentNumber("838");
        desktopBulkAgent.clickSearch();
        desktopBulkAgent.clickNewAgentResults();
        desktopBulkAgent.clickSubmitBatch();
        ErrorHandling errorHandle = new ErrorHandling(driver);
        String errorMsg = errorHandle.text_ErrorHandlingErrorBannerMessages().get(0).getText();
        boolean isContained = errorMsg.contains("DBNullConstraintException");
        System.out.println(errorMsg);
        Assert.assertFalse(isContained, "There is a DB Constraint Exception for an invalid policy number.");

    }

}

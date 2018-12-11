package scratchpad.steve.upgrade;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.ab.administration.AdminMenu;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.SearchAgentSearchAB;
import repository.ab.sidebar.SidebarAB;
import repository.ab.topmenu.TopMenuAB;
import repository.ab.topmenu.TopMenuSearchAB;
import repository.driverConfiguration.Config;
import repository.gw.administration.AdminEventMessages;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Suffix;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.DateUtils;
import com.idfbins.helpers.EmailUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.topmenu.TopMenuAccountPC;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AbUserHelper;
import regression.r2.noclock.contactmanager.search.AgentSearchTest;

public class AgentDataChange extends BaseTest {
    private AbUsers abUser;
    private Agents agent;
    private String adminLogin = "su";
    private String adminPass = "gw";
    private WebDriver driver;

    @Test
    public void changeAgent() throws Exception {
        this.abUser = AbUserHelper.getRandomDeptUser("Policy Services");
        AgentSearchTest searchAgent = new AgentSearchTest();
        searchAgent.AgentSearchTestMain();
        this.agent = searchAgent.getAgent();

        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);

        ContactDetailsBasicsAB agentContactPage = new ContactDetailsBasicsAB(driver);
        agentContactPage.clickContactDetailsBasicsEditLink();
        agentContactPage.setContactDetailsBasicsSuffix(Suffix.Esq.getValue());
        agentContactPage.clickContactDetailsBasicsUpdateLink();

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();


        new BatchHelpers(driver).runBatchProcess(BatchProcess.Agent_Information_Update);

        new Login(driver).login(agent.getAgentUserName(), agent.getAgentPassword());
        TopMenuAccountPC menuAccount = new TopMenuAccountPC(driver);
        menuAccount.clickFirst();

        AccountSummaryPC accountPage = new AccountSummaryPC(driver);
        String agentName = accountPage.getAgentName();
        if (!agentName.contains("Esq.")) {
            checkAllEventMessages();
            Assert.fail(driver.getCurrentUrl() + agent.agentFullName + "In ContactManager the agent: " + agent.getAgentFullName() + " had Esq. added to the name.  This change did not make it to PC after the agent info batch was run in PC.");
        }
        guidewireHelpers.logout();
        changeAgentBack();
    }

    public void changeAgentBack() {
        new Login(driver).login(this.abUser.getUserName(), this.abUser.getUserPassword());
        TopMenuSearchAB menu = new TopMenuSearchAB(driver);
        menu.clickSearch();
        SidebarAB sidebar = new SidebarAB(driver);
        sidebar.clickSidebarAgentSearchLink();
        SearchAgentSearchAB searchPage = new SearchAgentSearchAB(driver);
        searchPage.setSearchAgentSearchAgentNumber(this.agent.agentNum);
        searchPage.clickSearchAgentSearchSearch();
        searchPage.clickSearchAgentSearchAgentLink(this.agent.getAgentFirstName() + " " + this.agent.getAgentLastName(), this.agent.getAgentNum());
        ContactDetailsBasicsAB agentContactPage = new ContactDetailsBasicsAB(driver);
        agentContactPage.clickContactDetailsBasicsEditLink();
        agentContactPage.setContactDetailsBasicsSuffix(Suffix.None.getValue());
        agentContactPage.clickContactDetailsBasicsUpdateLink();
    }


    public void checkAllEventMessages() throws Exception {
        Login loginPage = new Login(driver);
        loginPage.login(adminLogin, adminPass);

        TopMenuAB topMenuStuff = new TopMenuAB(driver);
        topMenuStuff.clickAdministrationTab();


        AdminMenu adminMenuStuff = new AdminMenu(driver);
        adminMenuStuff.clickAdminMenuMonitoringMessageQueues();

        AdminEventMessages eventMessPage = new AdminEventMessages(driver);
        Date setDate = eventMessPage.writeRowValuesToDatabase();
        String emailBody = eventMessPage.getEventMessagingEmailTableFromBadRows(setDate);
        if (emailBody.contains("<td")) {
            String formattedDateStamp = DateUtils.dateFormatAsString("yyyy-MM-dd HH:mm:ss.SSS", setDate);
            ArrayList<String> emailsToSendTo = new ArrayList<String>();
            emailsToSendTo.add("sbroderick@idfbins.com");
            emailsToSendTo.add("ktennant@idfbins.com");
            String emailContents = "To Whom it May Concern,<br/><br/><p>We just ran a check against the AB8UAT server.  Here is a list of all the event messaging destinations that were either suspended or had queued messages:</p>" + emailBody
                    + "<p>We have also logged all the other information to a database table that you can query for your own personal investigation:<ul><li><b>Server:</b> "
                    + "FBMS2048</li><li><b>Database:</b> QAWIZPROGlobalDataRepository</li><li><b>Table:</b> EventMessagingDetails</li><li><b>Time Stamp for This Batch:</b> "
                    + formattedDateStamp + "</li><li><b>Sample Query:</b> SELECT * FROM FROM QAWIZPROGlobalDataRepository.dbo.EventMessagingDetails WHERE DateTimeOfCheck='"
                    + formattedDateStamp + "'</li></ul></p>";
            System.out.println(emailContents);
            Date date = new Date();
            new EmailUtils().sendEmail(emailsToSendTo, "AB8UAT Event Messaging Destination Problems - " + new Timestamp(date.getTime()), emailContents, null);
            Assert.fail(driver.getCurrentUrl() + "An email was sent out stating that event messages are stuck.");
        }
    }
}

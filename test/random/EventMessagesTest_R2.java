package random;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.ab.administration.AdminMenu;
import repository.ab.topmenu.TopMenuAB;
import repository.bc.topmenu.BCTopMenuAdministration;
import repository.cc.administration.AdminMenuCC;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.Config;
import repository.gw.administration.AdminEventMessages;
import repository.gw.helpers.DateUtils;
import com.idfbins.helpers.EmailUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.topmenu.TopMenuAdministrationPC;
public class EventMessagesTest_R2 extends BaseTest {

    private String userName = "emessaging";
    private String password = "gw";
    private String environment = "";
    private WebDriver driver;

    @Test()
    public void testBCEventMessaging_DEV() throws Exception {
        this.environment = "DEV";

        if (!testBC(this.environment)) {
            Assert.fail("Event Messaging for BC8" + this.environment + " has Problems.  Look at Console Output");
        }
    }

    @Test()
    public void testABEventMessaging_DEV() throws Exception {
        this.environment = "DEV";

        if (!testAB(this.environment)) {
            Assert.fail("Event Messaging for AB8" + this.environment + " has Problems.  Look at Console Output");
        }
    }

    @Test()
    public void testCCEventMessaging_DEV() throws Exception {
        this.environment = "DEV";

        if (!testCC(this.environment)) {
            Assert.fail("Event Messaging for CC8" + this.environment + " has Problems.  Look at Console Output");
        }
    }

    @Test()
    public void testPCEventMessaging_DEV() throws Exception {
        this.environment = "DEV";

        if (!testPC(this.environment)) {
            Assert.fail("Event Messaging for PC8" + this.environment + " has Problems.  Look at Console Output");
        }
    }

    @Test()
    public void testBCEventMessaging_IT() throws Exception {
        this.environment = "IT";

        if (!testBC(this.environment)) {
            Assert.fail("Event Messaging for BC8" + this.environment + " has Problems.  Look at Console Output");
        }
    }

    @Test()
    public void testABEventMessaging_IT() throws Exception {
        this.environment = "IT";

        if (!testAB(this.environment)) {
            Assert.fail("Event Messaging for AB8" + this.environment + " has Problems.  Look at Console Output");
        }
    }

    @Test()
    public void testCCEventMessaging_IT() throws Exception {
        this.environment = "IT";

        if (!testCC(this.environment)) {
            Assert.fail("Event Messaging for CC8" + this.environment + " has Problems.  Look at Console Output");
        }
    }

    @Test()
    public void testPCEventMessaging_IT() throws Exception {
        this.environment = "IT";

        if (!testPC(this.environment)) {
            Assert.fail("Event Messaging for PC8" + this.environment + " has Problems.  Look at Console Output");
        }
    }

    @Test()
    public void testBCEventMessaging_QA() throws Exception {
        this.environment = "QA";

        if (!testBC(this.environment)) {
            Assert.fail("Event Messaging for BC8" + this.environment + " has Problems.  Look at Console Output");
        }
    }

    @Test()
    public void testABEventMessaging_QA() throws Exception {
        this.environment = "QA";

        if (!testAB(this.environment)) {
            Assert.fail("Event Messaging for AB8" + this.environment + " has Problems.  Look at Console Output");
        }
    }

    @Test()
    public void testCCEventMessaging_QA() throws Exception {
        this.environment = "QA";

        if (!testCC(this.environment)) {
            Assert.fail("Event Messaging for CC8" + this.environment + " has Problems.  Look at Console Output");
        }
    }

    @Test()
    public void testPCEventMessaging_QA() throws Exception {
        this.environment = "QA";

        if (!testPC(this.environment)) {
            Assert.fail("Event Messaging for PC8" + this.environment + " has Problems.  Look at Console Output");
        }
    }

    @Test()
    public void testBCEventMessaging_UAT() throws Exception {
        this.environment = "UAT";

        if (!testBC(this.environment)) {
            Assert.fail("Event Messaging for BC8" + this.environment + " has Problems.  Look at Console Output");
        }
    }

    @Test()
    public void testABEventMessaging_UAT() throws Exception {
        this.environment = "UAT";

        if (!testAB(this.environment)) {
            Assert.fail("Event Messaging for AB8" + this.environment + " has Problems.  Look at Console Output");
        }
    }

    @Test()
    public void testCCEventMessaging_UAT() throws Exception {
        this.environment = "UAT";

        if (!testCC(this.environment)) {
            Assert.fail("Event Messaging for CC8" + this.environment + " has Problems.  Look at Console Output");
        }
    }

    @Test()
    public void testPCEventMessaging_UAT() throws Exception {
        this.environment = "UAT";

        if (!testPC(this.environment)) {
            Assert.fail("Event Messaging for PC8" + this.environment + " has Problems.  Look at Console Output");
        }
    }


    private boolean testBC(String serverToRun) throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter, serverToRun);
		driver = buildDriver(cf);

        Login loginPage = new Login(driver);
        loginPage.login(userName, password);

        BCTopMenuAdministration adminTopMenuStuff = new BCTopMenuAdministration(driver);
        adminTopMenuStuff.clickMontoringMessageQueues();

        return getEventMessagesWriteToDBFilterOutGoodOnesAndSendEmail(cf.getCenter().getValue());
    }

    private boolean testAB(String serverToRun) throws Exception {
    	Config cf = new Config(ApplicationOrCenter.ContactManager, serverToRun);
		driver = buildDriver(cf);

        Login loginPage = new Login(driver);
        loginPage.login(userName, password);

        TopMenuAB topMenuStuff = new TopMenuAB(driver);
        topMenuStuff.clickAdministrationTab();

        AdminMenu adminMenuStuff = new AdminMenu(driver);
        adminMenuStuff.clickAdminMenuMonitoringMessageQueues();

        return getEventMessagesWriteToDBFilterOutGoodOnesAndSendEmail(cf.getCenter().getValue());
    }

    private boolean testCC(String serverToRun) throws Exception {
    	Config cf = new Config(ApplicationOrCenter.ClaimCenter, serverToRun);
		driver = buildDriver(cf);

        new Login(driver).login(userName, password);

        TopMenu topMenuStuff = new TopMenu(driver);
        topMenuStuff.clickAdministrationTab();

        AdminMenuCC adminMenuStuff = new AdminMenuCC(driver);
        adminMenuStuff.clickAdminMenuMonitoryingMessageQueues();

        return getEventMessagesWriteToDBFilterOutGoodOnesAndSendEmail(cf.getCenter().getValue());
    }

    private boolean testPC(String serverToRun) throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter, serverToRun);
		driver = buildDriver(cf);

        Login loginPage = new Login(driver);
        loginPage.login(userName, password);

        TopMenuAdministrationPC adminTopMenuStuff = new TopMenuAdministrationPC(driver);
        adminTopMenuStuff.clickEventMessages();

        return getEventMessagesWriteToDBFilterOutGoodOnesAndSendEmail(cf.getCenter().getValue());
    }

    private boolean getEventMessagesWriteToDBFilterOutGoodOnesAndSendEmail(String app) throws Exception {

        boolean passed;

        app = app.toUpperCase();

        AdminEventMessages eventMessPage = new AdminEventMessages(driver);
        Date setDate = eventMessPage.writeRowValuesToDatabase();
        String emailBody = eventMessPage.getEventMessagingEmailTableFromBadRows(setDate);

        if (emailBody.contains("<td")) {
            passed = false;
            String formattedDateStamp = DateUtils.dateFormatAsString("yyyy-MM-dd HH:mm:ss.SSS", setDate);

            ArrayList<String> emailsToSendTo = new ArrayList<String>();
            emailsToSendTo.add("chofman@idfbins.com");
            emailsToSendTo.add("bhiltbrand@idfbins.com");
            emailsToSendTo.add("ryoung@idfbins.com");
            emailsToSendTo.add("cjoslin@idfbins.com");
            emailsToSendTo.add("rmoreira@idfbins.com");
            emailsToSendTo.add("jnthompson@idfbins.com");
            emailsToSendTo.add("abair@idfbins.com");

            if (app.equals("PC")) {
                emailsToSendTo.add("asmajovic@idfbins.com");
                emailsToSendTo.add("aatkinson@idfbins.com");
                emailsToSendTo.add("tdelezene@idfbins.com");

                if (emailBody.contains("ContactMessageTransport")) {
                    emailsToSendTo.add("ktennant@idfbins.com");
                    emailsToSendTo.add("sbroderick@idfbins.com");
                }

                if (emailBody.contains("BillingSystem") || emailBody.contains("Send Documents To BC")) {
                    emailsToSendTo.add("vshrestha@idfbins.com");
                    emailsToSendTo.add("dbastakoti@idfbins.com");
                    emailsToSendTo.add("ppendyala@idfbins.com");
                    emailsToSendTo.add("nkorada@idfbins.com");
                    emailsToSendTo.add("smedasetti@idfbins.com");
                }
            } else if (app.equals("BC")) {
                emailsToSendTo.add("vshrestha@idfbins.com");
                emailsToSendTo.add("dbastakoti@idfbins.com");
                emailsToSendTo.add("ppendyala@idfbins.com");
                emailsToSendTo.add("nkorada@idfbins.com");
                emailsToSendTo.add("smedasetti@idfbins.com");

                if (emailBody.contains("PAS")) {
                    emailsToSendTo.add("asmajovic@idfbins.com");
                    emailsToSendTo.add("aatkinson@idfbins.com");
                    emailsToSendTo.add("tdelezene@idfbins.com");
                }

                if (emailBody.contains("ContactMessageTransport") || emailBody.contains("UpdateCDMMessaging")) {
                    emailsToSendTo.add("ktennant@idfbins.com");
                    emailsToSendTo.add("sbroderick@idfbins.com");
                }
            } else if (app.equals("AB")) {
                emailsToSendTo.add("dsandborgh@idfbins.com");
                emailsToSendTo.add("sbroderick@idfbins.com");

                if (emailBody.contains("PolicyCenter Contact Broadcast")) {
                    emailsToSendTo.add("asmajovic@idfbins.com");
                    emailsToSendTo.add("aatkinson@idfbins.com");
                    emailsToSendTo.add("tdelezene@idfbins.com");
                }

                if (emailBody.contains("BillingCenter Contact Broadcast")) {
                    emailsToSendTo.add("vshrestha@idfbins.com");
                    emailsToSendTo.add("dbastakoti@idfbins.com");
                    emailsToSendTo.add("ppendyala@idfbins.com");
                    emailsToSendTo.add("nkorada@idfbins.com");
                    emailsToSendTo.add("smedasetti@idfbins.com");
                }
            } else if (app.equals("CC")) {
                emailsToSendTo.add("ktennant@idfbins.com");
                emailsToSendTo.add("dhysell@idfbins.com");

                if (emailBody.contains("Contact Message Transport")) {
                    emailsToSendTo.add("sbroderick@idfbins.com");
                }
            }

            String emailContents = "To Whom it May Concern,<br/><br/><p>We just ran a check against the " + this.environment + "/" + app
                    + " server.  Here is a list of all the event messaging destinations that were either suspended or had queued messages:</p>" + emailBody
                    + "<p>We have also logged all the other information to a database table that you can query for your own personal investigation:<ul><li><b>Server:</b> "
                    + "FBMS2048</li><li><b>Database:</b> QAWIZPROGlobalDataRepository</li><li><b>Table:</b> EventMessagingDetails</li><li><b>Time Stamp for This Batch:</b> "
                    + formattedDateStamp + "</li><li><b>Sample Query:</b> SELECT * FROM FROM QAWIZPROGlobalDataRepository.dbo.EventMessagingDetails WHERE DateTimeOfCheck='"
                    + formattedDateStamp + "'</li></ul></p>";
            System.out.println(emailContents);
            Date date = new Date();
            new EmailUtils().sendEmail(emailsToSendTo, this.environment + "/" + app + " Event Messaging Destination Problems - " + new Timestamp(date.getTime()), emailContents, null);
        } else {
            passed = true;
        }

        return passed;
    }

}

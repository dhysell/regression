package production.eventmessages;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
public class EventMessagesTest extends BaseTest {

    private String userName = "emessaging";
    private String password = "gwqa";
    private WebDriver driver;

    private boolean getEventMessagesWriteToDBFilterOutGoodOnesAndSendEmail(Config configuration) throws Exception {

        boolean passed;

        String app = configuration.getCenter().getValue();

        AdminEventMessages eventMessPage = new AdminEventMessages(driver);
        Date setDate = eventMessPage.writeRowValuesToDatabase();
        String emailBody = eventMessPage.getEventMessagingEmailTableFromBadRows(setDate);

        if (emailBody.contains("<td")) {
            passed = false;
            String formattedDateStamp = DateUtils.dateFormatAsString("yyyy-MM-dd HH:mm:ss.SSS", setDate);

//			String listOfEmailsWithCommasANY = "bhiltbrand@idfbins.com";
            String listOfEmailsWithCommasANY = System.getenv("EMAILSCSVSTRINGANY");
            List<String> emailsToSendToBeforeANY = Arrays.asList(listOfEmailsWithCommasANY.split(","));
            ArrayList<String> emailsToSendTo = new ArrayList<String>();
            emailsToSendTo.addAll(emailsToSendToBeforeANY);

            if (app.equals("PC")) {
//				String listOfEmailsWithCommasPC = "bhiltbrand@idfbins.com";
                String listOfEmailsWithCommasPC = System.getenv("EMAILSCSVSTRINGPC");
                List<String> emailsToSendToBeforePC = Arrays.asList(listOfEmailsWithCommasPC.split(","));
                emailsToSendTo.addAll(emailsToSendToBeforePC);

                if (emailBody.contains("ContactMessageTransport")) {
//					String listOfEmailsWithCommasAB = "bhiltbrand@idfbins.com";
                    String listOfEmailsWithCommasAB = System.getenv("EMAILSCSVSTRINGAB");
                    List<String> emailsToSendToBeforeAB = Arrays.asList(listOfEmailsWithCommasAB.split(","));
                    emailsToSendTo.addAll(emailsToSendToBeforeAB);
                }

                if (emailBody.contains("BillingSystem") || emailBody.contains("Send Documents To BC") || emailBody.contains("Payment Transport") || emailBody.contains("ContactMessageTransport")) {
//					String listOfEmailsWithCommasBC = "bhiltbrand@idfbins.com";
                    String listOfEmailsWithCommasBC = System.getenv("EMAILSCSVSTRINGBC");
                    List<String> emailsToSendToBeforeBC = Arrays.asList(listOfEmailsWithCommasBC.split(","));
                    emailsToSendTo.addAll(emailsToSendToBeforeBC);
                }

                if (emailBody.contains("Printing Transport") || emailBody.contains("Send Documents To BC") || emailBody.contains("DocumentStore")) {
//					String listOfEmailsWithCommasDOCS = "bhiltbrand@idfbins.com";
                    String listOfEmailsWithCommasDOCS = System.getenv("EMAILSCSVSTRINGDOCS");
                    List<String> emailsToSendToBeforeDOCS = Arrays.asList(listOfEmailsWithCommasDOCS.split(","));
                    emailsToSendTo.addAll(emailsToSendToBeforeDOCS);
                }
            } else if (app.equals("BC")) {
//				String listOfEmailsWithCommasBC = "bhiltbrand@idfbins.com";
                String listOfEmailsWithCommasBC = System.getenv("EMAILSCSVSTRINGBC");
                List<String> emailsToSendToBeforeBC = Arrays.asList(listOfEmailsWithCommasBC.split(","));
                emailsToSendTo.addAll(emailsToSendToBeforeBC);

                if (emailBody.contains("PAS")) {
//					String listOfEmailsWithCommasPC = "bhiltbrand@idfbins.com";
                    String listOfEmailsWithCommasPC = System.getenv("EMAILSCSVSTRINGPC");
                    List<String> emailsToSendToBeforePC = Arrays.asList(listOfEmailsWithCommasPC.split(","));
                    emailsToSendTo.addAll(emailsToSendToBeforePC);
                }

                if (emailBody.contains("ContactMessageTransport") || emailBody.contains("UpdateCDMMessaging")) {
//					String listOfEmailsWithCommasAB = "bhiltbrand@idfbins.com";
                    String listOfEmailsWithCommasAB = System.getenv("EMAILSCSVSTRINGAB");
                    List<String> emailsToSendToBeforeAB = Arrays.asList(listOfEmailsWithCommasAB.split(","));
                    emailsToSendTo.addAll(emailsToSendToBeforeAB);
                }

                if (emailBody.contains("Document Store") || emailBody.contains("DocumentProduction")) {
//					String listOfEmailsWithCommasDOCS = "bhiltbrand@idfbins.com";
                    String listOfEmailsWithCommasDOCS = System.getenv("EMAILSCSVSTRINGDOCS");
                    List<String> emailsToSendToBeforeDOCS = Arrays.asList(listOfEmailsWithCommasDOCS.split(","));
                    emailsToSendTo.addAll(emailsToSendToBeforeDOCS);
                }
            } else if (app.equals("AB")) {
//				String listOfEmailsWithCommasAB = "bhiltbrand@idfbins.com";
                String listOfEmailsWithCommasAB = System.getenv("EMAILSCSVSTRINGAB");
                List<String> emailsToSendToBeforeAB = Arrays.asList(listOfEmailsWithCommasAB.split(","));
                emailsToSendTo.addAll(emailsToSendToBeforeAB);

                if (emailBody.contains("PolicyCenter Contact Broadcast")) {
//					String listOfEmailsWithCommasPC = "bhiltbrand@idfbins.com";
                    String listOfEmailsWithCommasPC = System.getenv("EMAILSCSVSTRINGPC");
                    List<String> emailsToSendToBeforePC = Arrays.asList(listOfEmailsWithCommasPC.split(","));
                    emailsToSendTo.addAll(emailsToSendToBeforePC);
                }

                if (emailBody.contains("BillingCenter Contact Broadcast")) {
//					String listOfEmailsWithCommasBC = "bhiltbrand@idfbins.com";
                    String listOfEmailsWithCommasBC = System.getenv("EMAILSCSVSTRINGBC");
                    List<String> emailsToSendToBeforeBC = Arrays.asList(listOfEmailsWithCommasBC.split(","));
                    emailsToSendTo.addAll(emailsToSendToBeforeBC);
                }

                if (emailBody.contains("ClaimCenter Contact Broadcast")) {
//					String listOfEmailsWithCommasCC = "bhiltbrand@idfbins.com";
                    String listOfEmailsWithCommasCC = System.getenv("EMAILSCSVSTRINGCC");
                    List<String> emailsToSendToBeforeCC = Arrays.asList(listOfEmailsWithCommasCC.split(","));
                    emailsToSendTo.addAll(emailsToSendToBeforeCC);
                }
            } else if (app.equals("CC")) {
//				String listOfEmailsWithCommasCC = "bhiltbrand@idfbins.com";
                String listOfEmailsWithCommasCC = System.getenv("EMAILSCSVSTRINGCC");
                List<String> emailsToSendToBeforeCC = Arrays.asList(listOfEmailsWithCommasCC.split(","));
                emailsToSendTo.addAll(emailsToSendToBeforeCC);

                if (emailBody.contains("Contact Message Transport")) {
//					String listOfEmailsWithCommasAB = "bhiltbrand@idfbins.com";
                    String listOfEmailsWithCommasAB = System.getenv("EMAILSCSVSTRINGAB");
                    List<String> emailsToSendToBeforeAB = Arrays.asList(listOfEmailsWithCommasAB.split(","));
                    emailsToSendTo.addAll(emailsToSendToBeforeAB);
                }
            }

            ArrayList<String> emailsToSendToDeduped = (ArrayList<String>) emailsToSendTo.stream().distinct().collect(Collectors.toList());

            String emailContents = "To Whom it May Concern,<br/><br/><p>We just ran a check against the PRD/" + app
                    + " server.  Here is a list of all the event messaging destinations that were either suspended or had queued messages:</p>" + emailBody
                    + "<p>We have also logged all the other information to a database table that you can query for your own personal investigation:<ul><li><b>Server:</b> "
                    + "FBMS2048</li><li><b>Database:</b> QAWIZPROGlobalDataRepository</li><li><b>Table:</b> EventMessagingDetails</li><li><b>Time Stamp for This Batch:</b> "
                    + formattedDateStamp + "</li><li><b>Sample Query:</b> SELECT * FROM FROM QAWIZPROGlobalDataRepository.dbo.EventMessagingDetails WHERE DateTimeOfCheck='"
                    + formattedDateStamp + "'</li></ul></p>";
            System.out.println(emailContents);
            Date date = new Date();
            new EmailUtils().sendEmail(emailsToSendToDeduped, "PRD/" + app + " Event Messaging Destination Problems - " + new Timestamp(date.getTime()), emailContents, null);
        } else {
            passed = true;
        }

        return passed;
    }

    @Test()
    public void testBCEventMessaging() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);

        Login loginPage = new Login(driver);
        loginPage.login(userName, password);

        BCTopMenuAdministration adminTopMenuStuff = new BCTopMenuAdministration(driver);
        adminTopMenuStuff.clickMontoringMessageQueues();

        boolean passed = getEventMessagesWriteToDBFilterOutGoodOnesAndSendEmail(cf);

        if (!passed) {
            Assert.fail("Event Messaging for BC Production has Problems.  Look at Console Output");
        }
    }

    @Test()
    public void testABEventMessaging() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);

        Login loginPage = new Login(driver);
        loginPage.login(userName, password);

        TopMenuAB topMenuStuff = new TopMenuAB(driver);
        topMenuStuff.clickAdministrationTab();

        AdminMenu adminMenuStuff = new AdminMenu(driver);
        adminMenuStuff.clickAdminMenuMonitoringMessageQueues();

        boolean passed = getEventMessagesWriteToDBFilterOutGoodOnesAndSendEmail(cf);

        if (!passed) {
            Assert.fail("Event Messaging for AB Production has Problems.  Look at Console Output");
        }
    }

    @Test()
    public void testCCEventMessaging() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.ClaimCenter);
		driver = buildDriver(cf);

        new Login(driver).login(userName, password);
        TopMenu topMenu = new TopMenu(driver);
        topMenu.clickAdministrationTab();

        AdminMenuCC adminMenuStuff = new AdminMenuCC(driver);
        adminMenuStuff.clickAdminMenuMonitoryingMessageQueues();

        boolean passed = getEventMessagesWriteToDBFilterOutGoodOnesAndSendEmail(cf);

        if (!passed) {
            Assert.fail("Event Messaging for CC Production has Problems.  Look at Console Output");
        }

        driver.quit();
    }

    @Test()
    public void testPCEventMessaging() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        Login loginPage = new Login(driver);
        loginPage.login(userName, password);

        TopMenuAdministrationPC adminTopMenuStuff = new TopMenuAdministrationPC(driver);
        adminTopMenuStuff.clickEventMessages();

        boolean passed = getEventMessagesWriteToDBFilterOutGoodOnesAndSendEmail(cf);

        if (!passed) {
            Assert.fail("Event Messaging for PC Production has Problems.  Look at Console Output");
        }

    }

}

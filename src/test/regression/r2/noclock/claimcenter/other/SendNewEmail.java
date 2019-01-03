package regression.r2.noclock.claimcenter.other;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.cc.actionsmenu.ActionsMenu;
import repository.cc.administration.AdminMenuCC;
import repository.cc.administration.MessageQueuesCC;
import repository.cc.claim.NewEmail;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.Config;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
public class SendNewEmail extends BaseTest {
	private WebDriver driver;
    private String userName;
    private String passWord;
    private String claimNumber;

    @BeforeClass
    public void beforeClass() {

        userName = "su";
        passWord = "gw";
    }

    // Logs in as the user that was specified above
    @BeforeMethod
    public void beforeMethod() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        new Login(driver).login(userName, passWord);
    }

    @Test
    public void suspendBroker() {
        TopMenu topMenu = new TopMenu(driver);
        AdminMenuCC admin = new AdminMenuCC(driver);
        MessageQueuesCC queues = new MessageQueuesCC(driver);
        topMenu.clickAdministrationTab();

        admin.clickAdminMenuMonitoryingMessageQueues();

        queues.checkSpecificDestination("Email");

        queues.clickSuspendButton();

        this.userName = "abatts";
    }

    @Test(dependsOnMethods = {"suspendBroker"})
    public void sendNewEmail() {
        TopMenu topMenu = new TopMenu(driver);
        ActionsMenu actionsMenu = new ActionsMenu(driver);
        NewEmail newEmail = new NewEmail(driver);

        topMenu.clickClaimTab();

        this.claimNumber = driver.findElement(By.xpath("//span[@id='TabBar:ClaimTab-btnInnerEl']")).getText().replaceAll("[^0-9]", "");


        actionsMenu.clickActionsButton();

        actionsMenu.clickNewEmail();

        newEmail.addRecipientEmail("iclouser@idfbins.com");

        newEmail.addSenderName("Ian");

        newEmail.addSenderEmail("qawizpro@idfbins.com");

        newEmail.addSubject("Regression Test Email - Email");

        newEmail.addBodyText("If you are Receiving this email it means the claim center email has passed.");

        newEmail.clickSendEmailButton();

        // change user name and password for admin test.
        this.userName = "su";
        this.passWord = "gw";
    }

    @Test(dependsOnMethods = {"sendNewEmail"})
    public void checkEventMessaging() {

        TopMenu topMenu = new TopMenu(driver);
        AdminMenuCC admin = new AdminMenuCC(driver);
        MessageQueuesCC queues = new MessageQueuesCC(driver);
        topMenu.clickAdministrationTab();

        admin.clickAdminMenuMonitoryingMessageQueues();

        queues.clickEmail();

        queues.selectSpecificFilter();

        driver.navigate().refresh();

        queues.clickSpecificClaimNum(this.claimNumber);

        boolean payloadFound = queues.checkMessagePayload("Regression Test Email");

        Assert.assertTrue(payloadFound,
                "Test was unable to find the expected email message payload, Check the message broker is functioning properly.");

        queues.clickUpToDestination();

        queues.checkSpecificClaimNum(claimNumber);

        queues.clickUpToMessageQueues();

        queues.checkSpecificDestination("Email");

        queues.clickResumeButton();
    }
}

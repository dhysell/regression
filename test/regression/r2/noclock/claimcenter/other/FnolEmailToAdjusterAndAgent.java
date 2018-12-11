package regression.r2.noclock.claimcenter.other;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.testng.listener.QuarantineClass;

import repository.cc.administration.AdminMenuCC;
import repository.cc.administration.MessageQueuesCC;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.generate.cc.GenerateFNOL;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
@QuarantineClass
public class FnolEmailToAdjusterAndAgent extends BaseTest {
	private WebDriver driver;
    private String userName;
    private String passWord;
    private String claimNumber = "01242509012016022201";

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

    // NEW FNOL

    // FNOL Specific Strings
    private String incidentName = "Random";
    private String lossDescription = "Loss Description Test";
    private String lossCause = "Random";
    private String lossRouter = "Random";
    private String address = "Random";
    private String policyNumber = "Random";
    private GenerateFNOLType fnolType = GenerateFNOLType.Auto;

    @Test(dependsOnMethods = {"suspendBroker"})
    public void newFNOL() throws Exception {
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver).withCreatorUserNamePassword(userName, passWord).withSpecificIncident(incidentName).withLossDescription(lossDescription).withLossCause(lossCause).withLossRouter(lossRouter).withAdress(address).withPolicyNumber(policyNumber).build(fnolType);
        this.claimNumber = myFNOLObj.claimNumber;
        this.lossCause = myFNOLObj.lossCause;

    }

    /**
     * @Author iclouser
     * @Requirement
     * @RequirementsLink <a href=
     * "https://rally1.rallydev.com/#/10552165958d/detail/userstory/51405833571">
     * Rally Story</a>
     * @Description - makes sure that the email that is sent to adjusters and
     * agents contain the loss cause and loss description.
     * @DATE Feb 22, 2016
     */
    @Test(dependsOnMethods = {"newFNOL"})
    public void checkEmailsInEventMessaging() {
        TopMenu topMenu = new TopMenu(driver);
        AdminMenuCC admin = new AdminMenuCC(driver);
        MessageQueuesCC queues = new MessageQueuesCC(driver);
        topMenu.clickAdministrationTab();

        admin.clickAdminMenuMonitoryingMessageQueues();

        queues.clickEmail();

        queues.selectSpecificFilter();

        driver.navigate().refresh();

        queues.clickSpecificClaimNum(this.claimNumber);


        String tableXpath = "//div[contains(@id, 'MessageControlForSOOListLV')]";

        int numberOfEmails = new TableUtils(driver).getRowCount(driver.findElement(By.xpath(tableXpath)));
        // Two emails are sent one to the adjuster and one to the agent.
        Assert.assertTrue(numberOfEmails == 2, "Expected there to be two emails in the queue but only found: " + numberOfEmails);

        // Validate the loss cause is correct
        boolean lossCauseFound = queues.checkMessagePayload("Loss Type: " + fnolType.getValue());

        Assert.assertTrue(lossCauseFound, "Didn't find the expected loss cause");


        boolean lossDescriptionFound = queues.checkMessagePayload("- " + lossDescription);

        Assert.assertTrue(lossDescriptionFound, "Didn't find the expected loss description");


        queues.clickUpToDestination();

        queues.clickUpToMessageQueues();

        queues.checkSpecificDestination("Email");

        queues.clickResumeButton();
    }

}

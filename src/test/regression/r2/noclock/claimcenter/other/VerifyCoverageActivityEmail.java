package regression.r2.noclock.claimcenter.other;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.testng.listener.QuarantineClass;

import repository.cc.actionsmenu.ActionsMenu;
import repository.cc.administration.MessageQueuesCC;
import repository.cc.desktop.ActivitiesCC;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
@QuarantineClass
public class VerifyCoverageActivityEmail extends BaseTest {
	private WebDriver driver;
    private String userName = ClaimsUsers.abatts.toString();
    private String password = "gw";
    private String claimNumber = null;

    private ClaimsUsers userToSendActivityTo = ClaimsUsers.lbarber;

    /**
     * @throws Exception
     * @Author iclouser
     * @Requirement Send email to an assigned underwriter when a Verify Coverage Activity is generated from the actions menu (manually).
     * @RequirementsLink <a href="https://rally1.rallydev.com/#/10552165958d/detail/userstory/53488250222">Rally Story</a>
     * @Description suspends message queue then picks a random claim and creates the verify coverage activity to a specified person.
     * Then checks that the email was sent along with it's message payload that it contains the person and the activity name.
     * @DATE Apr 28, 2016
     */
    @Test
    public void verifyEmailSentFromActivity() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);

        new Login(driver).login(userName, password);

        MessageQueuesCC queues = new MessageQueuesCC(driver);
        queues.suspendEmailMessageQue();


        TopMenu topMenu = new TopMenu(driver);
        ActionsMenu actionsMenu = new ActionsMenu(driver);
        ActivitiesCC activities = new ActivitiesCC(driver);
        queues = new MessageQueuesCC(driver);

        topMenu.clickClaimTab(); // goes to the last claim accessed by the user you are logged in as. 

        this.claimNumber = driver.findElement(By.xpath("//span[@id='TabBar:ClaimTab-btnInnerEl']")).getText().replaceAll("[^0-9]", "");

        String activyNameToLookFor = actionsMenu.clickVerifyCoverageActivity().sendVerifyCoverageActivityTo(userToSendActivityTo);

        Assert.assertTrue(activities.validateActivityExists(activyNameToLookFor), "Expected to find an activity named " + activyNameToLookFor + " but didn't.");

        queues.navigateToClaimInEventMessaging(this.claimNumber);

        boolean emailWithActivityNameFound = queues.checkMessagePayload("Verify Coverage Activity");
        boolean emailWentToCorrectPerson = queues.checkMessagePayload(userToSendActivityTo.getName());

        queues.resumeEmailMessageQueue();

        Assert.assertTrue(emailWithActivityNameFound, "Expected an email for the verify coverage activity but didn't.");

        Assert.assertTrue(emailWentToCorrectPerson, "Expected an email for the verify coverage activity but didn't.");
    }


}

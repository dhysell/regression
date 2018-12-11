package regression.r2.noclock.claimcenter.other;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.cc.administration.MessageQueuesCC;
import repository.cc.desktop.DesktopSidebarCC;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.ClaimsUsers;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import repository.gw.servertools.ServerToolsBatchProcessInfo;
import gwclockhelpers.ApplicationOrCenter;
public class ValidateAdjusterNameInEmailManualChecks extends BaseTest {
	private WebDriver driver;
    private ClaimsUsers userName = ClaimsUsers.jgross;
    private String password = "gw";

    /**
     * @throws Exception
     * @Author iclouser
     * @Requirement
     * @RequirementsLink <a href="https://rally1.rallydev.com/#/10552165958d/detail/userstory/55017508135">Story link</a>
     * @Description Add adjuster name to the email that is sent for manual check that need to be put back on the books.
     * @DATE May 19, 2016
     */
    @Test
    public void adjusterEmailValidation() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);

        DesktopSidebarCC sideBar = new DesktopSidebarCC(driver);
        TopMenu topMenu = new TopMenu(driver);

        new Login(driver).login(userName.toString(), password);

        sideBar.clickManualcheckRegisterLink().selectSpecific_MissingCheck("Missing Checks");


        String tableXpath = "//div[contains(@id,':ManualCheckRegisterLV')]";
        List<String> names = new TableUtils(driver).getAllCellTextFromSpecificColumn(driver.findElement(By.xpath(tableXpath)), "Assigned To");

        topMenu.logoutFromClaimCenter();

        MessageQueuesCC queues = new MessageQueuesCC(driver);
        queues.suspendEmailMessageQue();

        ServerToolsBatchProcessInfo bServer = new ServerToolsBatchProcessInfo(driver);
        bServer.runBatchProcess(BatchProcess.Check_Register_Unconfirmed);

        new Login(driver).login("su", "gw");
        queues = new MessageQueuesCC(driver);
        queues.navigateToClaimInEventMessaging("Non-safe-ordered messages");

        boolean allAdjustersFound = false;
        for (int i = 0; i < names.size(); i++) {

            allAdjustersFound = queues.checkMessagePayload(names.get(i));
        }
        queues.resumeEmailMessageQueue();
        Assert.assertTrue(allAdjustersFound);
    }

}

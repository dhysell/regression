package scratchpad.rusty.misc;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.desktop.DesktopSideMenuPC;

//@Listeners(listeners.Listener.class)
public class DesktopSidebarMenuTest extends BaseTest {


    @Test
    public void testDesktopMenu() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);
        new Login(driver).login("hhill", "gw");
        DesktopSideMenuPC DesktopSidebar = new DesktopSideMenuPC(driver);

        DesktopSidebar.clickMySubmissions();


        DesktopSidebar.clickMyActivities();

        DesktopSidebar.clickMyRenewals();

        DesktopSidebar.clickMyOtherWorkOrders();

        DesktopSidebar.clickMyQueues();

        DesktopSidebar.clickUnattachedDocuments();

        DesktopSidebar.clickProofOfMail();

        DesktopSidebar.clickBulkAgentChange();
    }

}
package scratchpad.denver.old;

import repository.cc.administration.AdminSideMenuCC;
import repository.cc.administration.MessageQueuesCC;
import repository.cc.topmenu.TopMenu;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
public class SkipScript extends BaseTest {
	private WebDriver driver;
	
    @Test
    public void unfinishedMessages() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        TopMenu topMenu = new TopMenu(driver);
        Login lp = new Login(driver);
        AdminSideMenuCC asm = new AdminSideMenuCC(driver);
        MessageQueuesCC mq = new MessageQueuesCC(driver);

        String userName = "su";
        String password = "gw";

        lp.login(userName, password);

        topMenu.clickAdministrationTab();
        asm.clickMonitoringLink();
        asm.clickMessageQueues();
        mq.clickEmail();
        mq.selectSpecificFilter();
        mq.clickNonSafeMessages();
        mq.clearMessages();



    }
}

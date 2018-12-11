package scratchpad.denver.old;

import repository.cc.claim.ManualCheckRegister;
import repository.cc.desktop.DesktopSidebarCC;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

public class ManualChecksTest extends BaseTest {
	private WebDriver driver;
    private String userName = "swerth";
    private String password = "gw";


    @Test
    public void missingChecks() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);

        Login login = new Login(driver);
        login.login(userName, password);
        DesktopSidebarCC sideBar = new DesktopSidebarCC(driver);
        ManualCheckRegister checkReg = new ManualCheckRegister(driver);

        sideBar.clickManualcheckRegisterLink();
        checkReg.processCheckData();

    }
}

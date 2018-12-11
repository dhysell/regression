package scratchpad.rusty.misc;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.helpers.PopUpWindow;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.topinfo.TopInfo;
import gwclockhelpers.ApplicationOrCenter;

public class PopUpWindowTest extends BaseTest {

    @Test
    public void testPopUpWindows() throws Exception {


        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);
        Login lpPC = new Login(driver);
        lpPC.login("su", "gw");

        TopInfo Links = new TopInfo(driver);
        //Links.clickTopInfoHelp();
        PopUpWindow PUW = Links.clickTopInfoAbout();

        PUW.closePopUp();//<-- this closes the popup. This may not need to be called if the popup closes itself due something like a form submission
        PUW.returnFocusToOriginalWindow();//returns for to the original window so that commands can be directed there again.
        //Links.clickTopInfoBuildInfo();

    }
}

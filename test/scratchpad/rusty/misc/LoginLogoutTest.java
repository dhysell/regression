package scratchpad.rusty.misc;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.helpers.PopUpWindow;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.topinfo.TopInfo;
import gwclockhelpers.ApplicationOrCenter;

public class LoginLogoutTest extends BaseTest {

    private WebDriver driver;

    @Test()
    public void testPCLinks() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Login lp = new Login(driver);
        lp.login("su", "gw");

        TopInfo Links = new TopInfo(driver);

        PopUpWindow PUWHelp = Links.clickTopInfoHelp();
        PUWHelp.closePopUp();//<-- this closes the popup. This may not need to be called if the popup closes itself due something like a form submission
        PUWHelp.returnFocusToOriginalWindow();//returns for to the original window so that commands can be directed there again.

        PopUpWindow PUWAbout = Links.clickTopInfoAbout();
        PUWAbout.closePopUp();//<-- this closes the popup. This may not need to be called if the popup closes itself due something like a form submission
        PUWAbout.returnFocusToOriginalWindow();//returns for to the original window so that commands can be directed there again.


        Links.clickTopInfoBuildInfo();
        Links.clickTopInfoPreferences();
        Links.clickCancel();
    }

    @Test()
    public void testBCLinks() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        Login lp = new Login(driver);
        lp.login("su", "gw");

        TopInfo Links = new TopInfo(driver);
        Links.clickTopInfoBuildInfo();
        PopUpWindow PUWHelp = Links.clickTopInfoHelp();
        PUWHelp.closePopUp();//<-- this closes the popup. This may not need to be called if the popup closes itself due something like a form submission
        PUWHelp.returnFocusToOriginalWindow();//returns for to the original window so that commands can be directed there again.

        PopUpWindow PUWAbout = Links.clickTopInfoAbout();
        PUWAbout.closePopUp();//<-- this closes the popup. This may not need to be called if the popup closes itself due something like a form submission
        PUWAbout.returnFocusToOriginalWindow();//returns for to the original window so that commands can be directed there again.
    }

    @Test()
    public void testABLinks() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        Login lp = new Login(driver);
        lp.login("su", "gw");

        TopInfo Links = new TopInfo(driver);
        PopUpWindow PUWHelp = Links.clickTopInfoHelp();
        PUWHelp.closePopUp();//<-- this closes the popup. This may not need to be called if the popup closes itself due something like a form submission
        PUWHelp.returnFocusToOriginalWindow();//returns for to the original window so that commands can be directed there again.

        //Links.clickTopInfoAbout(); no about dialog box
        Links.clickTopInfoPreferences();
        Links.clickCancel();
        //Links.clickTopInfoBuildInfo();

    }

    @Test()
    public void testCCLinks() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        Login lp = new Login(driver);
        lp.login("su", "gw");

        TopInfo Links = new TopInfo(driver);
        PopUpWindow PUWHelp = Links.clickTopInfoHelp();
        PUWHelp.closePopUp();//<-- this closes the popup. This may not need to be called if the popup closes itself due something like a form submission
        PUWHelp.returnFocusToOriginalWindow();//returns for to the original window so that commands can be directed there again.

//		PopUpWindow PUWAbout = Links.clickTopInfoAbout();
//		PUWAbout.closePopUp();//<-- this closes the popup. This may not need to be called if the popup closes itself due something like a form submission
//		PUWAbout.returnFocuseToOriginalWindow();//returns for to the original window so that commands can be directed there again.
        //Links.clickTopInfoBuildInfo();

    }
}
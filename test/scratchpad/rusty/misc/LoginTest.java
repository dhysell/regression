package scratchpad.rusty.misc;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;


public class LoginTest extends BaseTest {


    @AfterMethod
    public void afterMethod() throws Exception {

    }

    @Test
    public void testLoginPageAllCenters() throws Exception {

        Config cf = new Config(ApplicationOrCenter.ContactManager);
        WebDriver driver = buildDriver(cf);
        Login lpAB8 = new Login(driver);
        lpAB8.login("su", "gw");


        driver.quit();
        cf.setCenter(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Login lpPC = new Login(driver);
        lpPC.login("su", "gw");


        driver.quit();
        cf.setCenter(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        Login lpBC = new Login(driver);
        lpBC.login("su", "gw");

    }
}

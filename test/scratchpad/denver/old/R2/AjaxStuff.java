package scratchpad.denver.old.R2;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


@SuppressWarnings("unused")
public class AjaxStuff extends BaseTest {

    private String ajaxTestSite = "http://www.w3schools.com/ajax/tryit.asp?filename=tryajax_callback";

    WebDriver browser;
    WebDriverWait wait;

    @BeforeTest
    public void startTest() throws Exception {

        // Launch the demo web page to handle AJAX calls using Webdriver.
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        browser = buildDriver(cf);

    }


    @Test
    public void test_AjaxCalls() {


    }

    @AfterTest
    public void endTest() {
        browser.quit();
    }
}
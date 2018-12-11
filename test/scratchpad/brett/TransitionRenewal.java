package scratchpad.brett;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.renewal.RenewalStartTransitionRenewal;

public class TransitionRenewal extends BaseTest {

    @AfterMethod(alwaysRun = true)
    public void tearDown() throws Exception {

    }

    @Test
    public void startTransitionRenewal() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);
        RenewalStartTransitionRenewal transitionRenewal = new RenewalStartTransitionRenewal(driver);
        transitionRenewal.startTransitionRenewal();
    }
}
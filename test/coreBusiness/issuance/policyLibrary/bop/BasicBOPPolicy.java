package coreBusiness.issuance.policyLibrary.bop;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.helpers.GeneratePolicyHelper;
import gwclockhelpers.ApplicationOrCenter;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

public class BasicBOPPolicy extends BaseTest {
	private WebDriver driver;

    @Test
    public void testGenerateBasicBOPPolicy() throws  Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

       new GeneratePolicyHelper(driver).generateBasicBOPPolicy(null,null,null,null);
    }
}

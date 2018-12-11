package coreBusiness.issuance.policyLibrary.pl;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.helpers.GeneratePolicyHelper;
import gwclockhelpers.ApplicationOrCenter;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

public class FarmAndRanchPolicy extends BaseTest {
	
	private WebDriver driver;

    @Test
    public void testGenerateFarmAndRanchPolicy() throws  Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new GeneratePolicyHelper(driver).generateFarmAndRanchPolicy(null,null,null,null,null);

    }
}

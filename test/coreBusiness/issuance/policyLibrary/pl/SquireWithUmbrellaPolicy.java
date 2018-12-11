package coreBusiness.issuance.policyLibrary.pl;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.helpers.GeneratePolicyHelper;
import gwclockhelpers.ApplicationOrCenter;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

public class SquireWithUmbrellaPolicy extends BaseTest {

// failed
	private WebDriver driver;
	
    @Test
    public void testGenerateSquireWithUmbrellaPLPolicy() throws  Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new GeneratePolicyHelper(driver).generateSquireWithUmbrellaPLPolicy(null,null,null,null,null);
    }
}

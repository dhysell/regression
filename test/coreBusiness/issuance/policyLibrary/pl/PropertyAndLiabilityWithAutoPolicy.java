package coreBusiness.issuance.policyLibrary.pl;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.helpers.GeneratePolicyHelper;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

public class PropertyAndLiabilityWithAutoPolicy extends BaseTest {

//failed

    private WebDriver driver;

    @Test
    public void generatePLSectionIAndIIWithAutoPLPolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

       new GeneratePolicyHelper(driver).generatePLSectionIAndIIWithAutoPLPolicy(null,null,null,null,null);

    }


}

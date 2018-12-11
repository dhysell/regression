package coreBusiness.issuance.policyLibrary.pl;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.helpers.GeneratePolicyHelper;


public class AutoPolicy extends BaseTest {
	
	private WebDriver driver;
	
	@Test
	public void testGeneratePLAutoOnlyPolicy() throws  Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new GeneratePolicyHelper(driver).generatePLSectionIIIPersonalAutoLinePLPolicy(null,null,null,null,null);
	}
}

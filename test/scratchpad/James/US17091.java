package scratchpad.James;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import gwclockhelpers.ApplicationOrCenter;

public class US17091 extends BaseTest {
	 private WebDriver driver;
	 private GeneratePolicy myPolicyObjPL;

	 @Test
	 public void testPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);
		myPolicyObjPL = new GeneratePolicy.Builder(driver)
				.withProductType(ProductLineType.StandardFire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withSquire(mySquire)
				.build(GeneratePolicyType.FullApp);
		driver.quit();
	}
}

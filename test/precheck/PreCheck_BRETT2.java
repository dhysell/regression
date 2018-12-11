package precheck;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import services.enums.Broker;
import repository.gw.helpers.GuidewireIsUpHelper;
public class PreCheck_BRETT2 extends BaseTest {
	
	private String serverSet = "BRETT2";
    private Broker serverMBMQ = Broker.DEV;
	
	@Test()
	public void testIsUp_BRETT2() throws Exception {
		Assert.assertTrue(new GuidewireIsUpHelper().checkIfServersAreUp_BRETT2(), "ERROR: One of the Guidewire applications for " + this.serverSet + " is down.  See emails.");
	}
	
	@Test(dependsOnMethods = {"testIsUp_BRETT2"})
	public void testAddressStandardization_BRETT2() throws Exception {
		new GuidewireIsUpHelper().testAddressStandardization(serverSet, serverMBMQ);
	}

}

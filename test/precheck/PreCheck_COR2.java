package precheck;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import services.enums.Broker;
import repository.gw.helpers.GuidewireIsUpHelper;
public class PreCheck_COR2 extends BaseTest {
	
	private String serverSet = "COR2";
    private Broker serverMBMQ = Broker.DEV;
	
	@Test()
	public void testIsUp_COR2() throws Exception {
		Assert.assertTrue(new GuidewireIsUpHelper().checkIfServersAreUp_COR2(), "ERROR: One of the Guidewire applications for " + this.serverSet + " is down.  See emails.");
	}
	
	@Test(dependsOnMethods = {"testIsUp_COR2"})
	public void testAddressStandardization_COR2() throws Exception {
		new GuidewireIsUpHelper().testAddressStandardization(serverSet, serverMBMQ);
	}

}

package precheck;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import services.enums.Broker;
import repository.gw.helpers.GuidewireIsUpHelper;
public class PreCheck_REGR04 extends BaseTest {
	
	private String serverSet = "REGR04";
    private Broker serverMBMQ = Broker.DEV;
	
	@Test()
	public void testIsUp_REGR04() throws Exception {
		Assert.assertTrue(new GuidewireIsUpHelper().checkIfServersAreUp_REGR04(), "ERROR: One of the Guidewire applications for " + this.serverSet + " is down.  See emails.");
	}
	
	@Test(dependsOnMethods = {"testIsUp_REGR04"})
	public void testAddressStandardization_REGR04() throws Exception {
		new GuidewireIsUpHelper().testAddressStandardization(serverSet, serverMBMQ);
	}

}

package precheck;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import services.enums.Broker;
import repository.gw.helpers.GuidewireIsUpHelper;
public class PreCheck_REGR02 extends BaseTest {
	
	private String serverSet = "REGR02";
    private Broker serverMBMQ = Broker.DEV;
	
	@Test()
	public void testIsUp_REGR02() throws Exception {
		Assert.assertTrue(new GuidewireIsUpHelper().checkIfServersAreUp_REGR02(), "ERROR: One of the Guidewire applications for " + this.serverSet + " is down.  See emails.");
	}
	
	@Test(dependsOnMethods = {"testIsUp_REGR02"})
	public void testAddressStandardization_REGR02() throws Exception {
		new GuidewireIsUpHelper().testAddressStandardization(serverSet, serverMBMQ);
	}

}

package precheck;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import services.enums.Broker;
import repository.gw.helpers.GuidewireIsUpHelper;
public class PreCheck_UAT extends BaseTest {
	
	private String serverSet = "UAT";
    private Broker serverMBMQ = Broker.UAT;
	
	@Test()
	public void testIsUp_UAT() throws Exception {
		Assert.assertTrue(new GuidewireIsUpHelper().checkIfServersAreUp_UAT(), "ERROR: One of the Guidewire applications for " + this.serverSet + " is down.  See emails.");
	}
	
	@Test(dependsOnMethods = {"testIsUp_UAT"})
	public void testAddressStandardization_UAT() throws Exception {
		new GuidewireIsUpHelper().testAddressStandardization(serverSet, serverMBMQ);
	}

}

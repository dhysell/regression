package precheck;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import services.enums.Broker;
import repository.gw.helpers.GuidewireIsUpHelper;
public class PreCheck_QA2 extends BaseTest {
	
	private String serverSet = "QA2";
    private Broker serverMBMQ = Broker.QA;
	
	@Test()
	public void testIsUp_QA2() throws Exception {
		Assert.assertTrue(new GuidewireIsUpHelper().checkIfServersAreUp_QA2(), "ERROR: One of the Guidewire applications for " + this.serverSet + " is down.  See emails.");
	}
	
	@Test(dependsOnMethods = {"testIsUp_QA2"})
	public void testAddressStandardization_QA2() throws Exception {
		new GuidewireIsUpHelper().testAddressStandardization(serverSet, serverMBMQ);
	}

}

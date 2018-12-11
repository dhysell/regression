package production.servers;

import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.helpers.GuidewireIsUpHelper;
import com.idfbins.driver.BaseTest;
public class CheckIsUp extends BaseTest {
	
	// PL Environments
	@Test(enabled = true)
	public void testIsUp_QA2() throws Exception {
		String serverSet = "QA2";

		Assert.assertTrue(new GuidewireIsUpHelper().checkIfServersAreUp_QA2(), "ERROR: One of the Guidewire applications for " + serverSet + " is down.  See emails.");
	}

	@Test(enabled = true)
	public void testIsUp_IT2() throws Exception {
		String serverSet = "IT2";

		Assert.assertTrue(new GuidewireIsUpHelper().checkIfServersAreUp_IT2(), "ERROR: One of the Guidewire applications for " + serverSet + " is down.  See emails.");
	}

	@Test(enabled = true)
	public void testIsUp_UAT2() throws Exception {
		String serverSet = "UAT2";

		Assert.assertTrue(new GuidewireIsUpHelper().checkIfServersAreUp_UAT2(), "ERROR: One of the Guidewire applications for " + serverSet + " is down.  See emails.");
	}

	@Test(enabled = true)
	public void testIsUp_DEV2() throws Exception {
		String serverSet = "DEV2";

		Assert.assertTrue(new GuidewireIsUpHelper().checkIfServersAreUp_DEV2(), "ERROR: One of the Guidewire applications for " + serverSet + " is down.  See emails.");
	}
	
	@Test(enabled = true)
	public void testIsUp_BRETT2() throws Exception {
		String serverSet = "BRETT2";

		Assert.assertTrue(new GuidewireIsUpHelper().checkIfServersAreUp_BRETT2(), "ERROR: One of the Guidewire applications for " + serverSet + " is down.  See emails.");
	}
	
	@Test(enabled = true)
	public void testIsUp_COR2() throws Exception {
		String serverSet = "COR2";

		Assert.assertTrue(new GuidewireIsUpHelper().checkIfServersAreUp_COR2(), "ERROR: One of the Guidewire applications for " + serverSet + " is down.  See emails.");
	}
	
	@Test(enabled = true)
	public void testIsUp_STEVE2() throws Exception {
		String serverSet = "STEVE2";

		Assert.assertTrue(new GuidewireIsUpHelper().checkIfServersAreUp_STEVE2(), "ERROR: One of the Guidewire applications for " + serverSet + " is down.  See emails.");
	}
	
	// STAB Environments
	@Test(enabled = true)
	public void testIsUp_STAB01() throws Exception {
		String serverSet = "STAB01";

		Assert.assertTrue(new GuidewireIsUpHelper().checkIfServersAreUp_STAB01(), "ERROR: One of the Guidewire applications for " + serverSet + " is down.  See emails.");
	}
	
	@Test(enabled = true)
	public void testIsUp_STAB02() throws Exception {
		String serverSet = "STAB02";

		Assert.assertTrue(new GuidewireIsUpHelper().checkIfServersAreUp_STAB02(), "ERROR: One of the Guidewire applications for " + serverSet + " is down.  See emails.");
	}
	
	@Test(enabled = true)
	public void testIsUp_STAB03() throws Exception {
		String serverSet = "STAB03";

		Assert.assertTrue(new GuidewireIsUpHelper().checkIfServersAreUp_STAB03(), "ERROR: One of the Guidewire applications for " + serverSet + " is down.  See emails.");
	}
	
	// BOP Environments
	@Test(enabled = true)
	public void testIsUp_IT() throws Exception {
		String serverSet = "IT";

		Assert.assertTrue(new GuidewireIsUpHelper().checkIfServersAreUp_IT(), "ERROR: One of the Guidewire applications for " + serverSet + " is down.  See emails.");
	}
	
	@Test(enabled = true)
	public void testIsUp_DEV() throws Exception {
		String serverSet = "DEV";

		Assert.assertTrue(new GuidewireIsUpHelper().checkIfServersAreUp_DEV(), "ERROR: One of the Guidewire applications for " + serverSet + " is down.  See emails.");
	}
	
	@Test(enabled = true)
	public void testIsUp_UAT() throws Exception {
		String serverSet = "UAT";

		Assert.assertTrue(new GuidewireIsUpHelper().checkIfServersAreUp_UAT(), "ERROR: One of the Guidewire applications for " + serverSet + " is down.  See emails.");
	}
	
	@Test(enabled = true)
	public void testIsUp_BRETTTRUNK() throws Exception {
		String serverSet = "BRETTTRUNK";

		Assert.assertTrue(new GuidewireIsUpHelper().checkIfServersAreUp_BRETTTRUNK(), "ERROR: One of the Guidewire applications for " + serverSet + " is down.  See emails.");
	}
	
	@Test(enabled = true)
	public void testIsUp_BRETTBRANCH() throws Exception {
		String serverSet = "BRETTBRANCH";

		Assert.assertTrue(new GuidewireIsUpHelper().checkIfServersAreUp_BRETTBRANCH(), "ERROR: One of the Guidewire applications for " + serverSet + " is down.  See emails.");
	}
	
	// CPP Environments
	@Test(enabled = true)
	public void testIsUp_QA3() throws Exception {
		String serverSet = "QA3";

		Assert.assertTrue(new GuidewireIsUpHelper().checkIfServersAreUp_QA3(), "ERROR: One of the Guidewire applications for " + serverSet + " is down.  See emails.");
	}

	@Test(enabled = true)
	public void testIsUp_DEV3() throws Exception {
		String serverSet = "DEV3";

		Assert.assertTrue(new GuidewireIsUpHelper().checkIfServersAreUp_DEV3(), "ERROR: One of the Guidewire applications for " + serverSet + " is down.  See emails.");
	}

	@Test(enabled = true)
	public void testIsUp_BMARTIN() throws Exception {
		String serverSet = "BMARTIN";

		Assert.assertTrue(new GuidewireIsUpHelper().checkIfServersAreUp_BMARTIN(), "ERROR: One of the Guidewire applications for " + serverSet + " is down.  See emails.");
	}

	// OTHER Environments
	@Test(enabled = true)
	public void testIsUp_PRD() throws Exception {
		String serverSet = "PRD";

		Assert.assertTrue(new GuidewireIsUpHelper().checkIfServersAreUp_PRD(), "ERROR: One of the Guidewire applications for " + serverSet + " is down.  See emails.");
	}
	
	@Test(enabled = true)
	public void testIsUp_R2PRDTest() throws Exception {
		String serverSet = "R2-PRD-START-TEST";
		
		Assert.assertTrue(new GuidewireIsUpHelper().checkIfServersAreUp_R2PRDTest(), "ERROR: One of the Guidewire applications for " + serverSet + " is down.  See emails.");
	}

}

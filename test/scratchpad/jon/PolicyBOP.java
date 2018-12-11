package scratchpad.jon;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import repository.gw.generate.GeneratePolicy;
import scratchpad.jon.mine.GenerateTestPolicy;
public class PolicyBOP {

	GeneratePolicy myPolicyObj = null;
	boolean testFailed = false;
	String failureString = "";
	
	
	@AfterMethod(alwaysRun = true)
	public void afterMethod() throws Exception {

    }

	@Test(enabled = true)
	public void createPolicy() throws Exception {

		GenerateTestPolicy policy = new GenerateTestPolicy();
		policy.generateRandomPolicy(1, 1, 1, "Verify Documents");
		this.myPolicyObj = policy.myPolicy;

	}
	
}

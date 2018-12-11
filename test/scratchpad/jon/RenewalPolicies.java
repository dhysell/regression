package scratchpad.jon;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import repository.gw.enums.GeneratePolicyType;
import repository.gw.generate.GeneratePolicy;
import com.idfbins.driver.BaseTest;
import scratchpad.jon.mine.GenerateTestPolicy;
public class RenewalPolicies extends BaseTest {

	
	GeneratePolicy myPolicyObj = null;

	@BeforeClass
    public void beforeClass() {

	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod() throws Exception {

    }
	
	@Test
	public void generateAllInsuredPolicy() throws Exception {
		GenerateTestPolicy policy = new GenerateTestPolicy();
		policy.generateRandomPolicy(2, 2, 2, "All Insured", GeneratePolicyType.FullApp);
		this.myPolicyObj = policy.myPolicy;

		System.out.println(myPolicyObj.accountNumber);
		System.out.println(myPolicyObj.agentInfo.getAgentUserName());
	}
	
	@Test
	public void generateAllLienHolderPolicy() throws Exception {
		GenerateTestPolicy policy = new GenerateTestPolicy();
		policy.generateRandomPolicy(2, 2, 2, "ALL Lien Holder", GeneratePolicyType.FullApp, false, false, true);
		this.myPolicyObj = policy.myPolicy;

		System.out.println(myPolicyObj.accountNumber);
		System.out.println(myPolicyObj.agentInfo.getAgentUserName());
	}
	
	@Test
	public void generateInsuredLienHolderPolicy() throws Exception {
		GenerateTestPolicy policy = new GenerateTestPolicy();
		policy.generateRandomPolicy(2, 2, 2, "Ins/LH ", GeneratePolicyType.FullApp, false, false, true);
		this.myPolicyObj = policy.myPolicy;

		System.out.println(myPolicyObj.accountNumber);
		System.out.println(myPolicyObj.agentInfo.getAgentUserName());
	}
	
	
	
	
	
	
	
	
	
}











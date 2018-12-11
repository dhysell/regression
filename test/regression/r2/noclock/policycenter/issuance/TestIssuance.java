package regression.r2.noclock.policycenter.issuance;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.enums.GeneratePolicyType;
import repository.gw.generate.GeneratePolicy;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import regression.r2.noclock.policycenter.submission_bop.TestBind;

public class TestIssuance extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObjInsuredOnly = null;
	public GeneratePolicy myPolicyObjInsuredAndLien = null;

	@Test
	public void testBasicIssuanceInsuredOnly() throws Exception {

		TestBind bound = new TestBind();
		bound.testBasicBindInsuredOnly();
		this.myPolicyObjInsuredOnly = bound.myPolicyObjInsuredOnly;

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		this.myPolicyObjInsuredOnly.convertTo(driver, GeneratePolicyType.PolicyIssued);
	}

	@Test
	public void testBasicIssuanceInsuredAndLien() throws Exception {

		TestBind bound = new TestBind();
		bound.testBasicBindInsuredAndLien();
		this.myPolicyObjInsuredAndLien = bound.myPolicyObjInsuredAndLien;

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		this.myPolicyObjInsuredAndLien.convertTo(driver, GeneratePolicyType.PolicyIssued);
	}

}

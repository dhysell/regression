package regression.r2.noclock.policycenter.cancellation;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.generate.GeneratePolicy;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.workorders.cancellation.StartCancellation;
import regression.r2.noclock.policycenter.issuance.TestIssuance;
@QuarantineClass
public class TestCancellation extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObjFlatCancelled = null;

	@Test
	public void testFlatCancelInsuredBoundOnAccident() throws Exception {

		TestIssuance issued = new TestIssuance();
		issued.testBasicIssuanceInsuredOnly();
		this.myPolicyObjFlatCancelled = issued.myPolicyObjInsuredOnly;

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        Login myloginPage = new Login(driver);
		myloginPage.login(this.myPolicyObjFlatCancelled.underwriterInfo.getUnderwriterUserName(),
				this.myPolicyObjFlatCancelled.underwriterInfo.getUnderwriterPassword());

        SearchPoliciesPC policySearch = new SearchPoliciesPC(driver);
		policySearch.searchPolicyByAccountNumber(this.myPolicyObjFlatCancelled.accountNumber);

        StartCancellation cancelPol = new StartCancellation(driver);
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.SubmittedOnAccident, "Messup on Agent's Part", null,
				true);

	}

}

/*Steve Broderick
 * Automated test for DE 2929 and 2808
 * Deals with Rewrite Copy Submission 
 * */

package regression.r2.clock.policycenter.rewrite;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.gw.topinfo.TopInfo;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.workorders.rewrite.StartRewrite;
import regression.r2.clock.billingcenter.delinquency.DelinquencyDrivenCancellation;

@QuarantineClass
public class RewriteCopySubmissionWithCollection_Clock extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private double delinquentAmount;

	@Test
	public void createCollection() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		DelinquencyDrivenCancellation rewritePolicy = new DelinquencyDrivenCancellation(false);
        rewritePolicy.generatePolicy();
		rewritePolicy.runInvoiceWithoutMakingDownpayment();
		rewritePolicy.moveClocks();
		rewritePolicy.runInvoiceDueAndCheckDelinquency();
		rewritePolicy.verifyCancelationCompletionInPolicyCenter();
		rewritePolicy.verifyDelinquencyStepsInBillingCenter();
		this.myPolicyObj = rewritePolicy.getPolicyObj();
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 61);
		this.delinquentAmount = rewritePolicy.getDelinquentAmount();
		getQALogger().info("The Account Number is: " + myPolicyObj.accountNumber);
		getQALogger().info("The Delinquent amount is :" + this.delinquentAmount);

	}

	@Test(dependsOnMethods = { "createCollection" })
	public void rewrite() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        Login myloginPage = new Login(driver);
		myloginPage.login(this.myPolicyObj.agentInfo.getAgentUserName(), this.myPolicyObj.agentInfo.getAgentPassword());

        SearchPoliciesPC policySearch = new SearchPoliciesPC(driver);
		policySearch.searchPolicyByAccountNumber(this.myPolicyObj.accountNumber);

		StartRewrite rewritePol = new StartRewrite(driver);
		String pcDelinquentAmount = rewritePol.rewriteNewTermCopySubmission(myPolicyObj);
		double pcDelinquentAmountNumber = NumberUtils.getCurrencyValueFromElement(pcDelinquentAmount);
		if (pcDelinquentAmountNumber != this.delinquentAmount) {
			Assert.fail("The Delinquent amount in PolicyCenter did not match the delinquent amount in BillingCenter.");
		}

        TopInfo topInfoStuff = new TopInfo(driver);
        topInfoStuff.clickTopInfoLogout();
	}

}

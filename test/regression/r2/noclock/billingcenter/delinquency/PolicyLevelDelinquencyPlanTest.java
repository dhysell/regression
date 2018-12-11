package regression.r2.noclock.billingcenter.delinquency;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.policy.summary.BCPolicySummary;
import repository.bc.search.BCSearchAccounts;
import repository.bc.search.BCSearchPolicies;
import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.DelinquencyPlan;
import repository.gw.generate.GeneratePolicy;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import regression.r2.noclock.policycenter.issuance.TestIssuance;
/**
* @Author jqu
* @Description US6837 Set existing accounts to use policy level delinquency (part of the conversion batch process); Set new accounts to use policy level delinquency.
* @DATE February 16, 2016
*/
public class PolicyLevelDelinquencyPlanTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;		
	private ARUsers arUser = new ARUsers();		
	private DelinquencyPlan delinqPlanShouldBe = DelinquencyPlan.PolicyLevelDelinquencyPlan, existingDelinqPlan;
	
	@Test
	public void generate() throws Exception {		
		TestIssuance issued = new TestIssuance();
		issued.testBasicIssuanceInsuredOnly();
		this.myPolicyObj = issued.myPolicyObjInsuredOnly;		
	}
	@Test(dependsOnMethods = { "generate" })	
	public void verifyDelinquencyOnNewPolicy() throws Exception {		
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        BCPolicySummary policySum = new BCPolicySummary(driver);
		try{
			existingDelinqPlan = policySum.getDelinquencyPlan();
			if(!existingDelinqPlan.equals(delinqPlanShouldBe))
                Assert.fail("the policy level delinqency plan is incorrect for the new policy " + myPolicyObj.busOwnLine.getPolicyNumber());
		}catch(Exception e){
            Assert.fail("the policy level delinqency plan doesn't exist for " + myPolicyObj.busOwnLine.getPolicyNumber());
		}		
	}
	@Test(dependsOnMethods = { "verifyDelinquencyOnNewPolicy" })	
	public void verifyDelinquencyOnExistingPolicy() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());
		//get a random existing policy
		BCTopMenu menu = new BCTopMenu(driver);
		menu.clickSearchTab();
		BCSearchAccounts searchAccount=new BCSearchAccounts(driver);
		String existingAcctNum= searchAccount.findAccountInGoodStanding("08-");
		BCSearchPolicies searchPolicy=new BCSearchPolicies(driver);
		searchPolicy.searchPolicyByAccountNumber(existingAcctNum);
        BCPolicySummary policySum = new BCPolicySummary(driver);
		try{
			existingDelinqPlan=policySum.getDelinquencyPlan();
			if(!existingDelinqPlan.equals(delinqPlanShouldBe))
				Assert.fail("the policy level delinqency plan is incorrect for the existing account "+ existingAcctNum);
		}catch(Exception e){
			Assert.fail("the policy level delinqency plan doesn't exist for existing account "+ existingAcctNum);
		}		
	}
}

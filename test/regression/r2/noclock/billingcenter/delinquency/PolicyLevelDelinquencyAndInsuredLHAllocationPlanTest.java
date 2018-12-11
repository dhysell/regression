package regression.r2.noclock.billingcenter.delinquency;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.summary.BCAccountSummary;
import repository.bc.policy.summary.BCPolicySummary;
import repository.bc.search.BCSearchAccounts;
import repository.bc.search.BCSearchPolicies;
import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DelinquencyPlan;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentAllocationPlan;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author jqu
 * @Description US6837 Set existing accounts to use policy level delinquency (part of the conversion batch process); Set new accounts to use policy level delinquency.
 * @Description US6342 Assign payment allocation plan to new accounts. Payment allocation used by the account needs to be set by the account type, insured or lienholder.
 * @DATE February 16, 2016
 */
public class PolicyLevelDelinquencyAndInsuredLHAllocationPlanTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;		
	private ARUsers arUser = new ARUsers();
	private String lienholderNumber;
	private DelinquencyPlan existingDelinqPlan, delinqPlanShouldBe = DelinquencyPlan.PolicyLevelDelinquencyPlan;
	private PaymentAllocationPlan existingInsuredAllocPlan, existingLHAllocPlan, insuredAllocationPlanShouldBe = PaymentAllocationPlan.InsuredPaymentAllocationPlan, lienAllocationPlanShouldBe = PaymentAllocationPlan.LienholderPaymentAllocationPlan;
	
	@Test
	public void generate() throws Exception {		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		
		ArrayList<AdditionalInterest> loc1LNBldg1AdditionalInterests = new ArrayList<AdditionalInterest>();		
		AdditionalInterest loc1LNBldg1AddInterest = new AdditionalInterest(ContactSubType.Company);		
		loc1LNBldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1LNBldg1AdditionalInterests.add(loc1LNBldg1AddInterest);
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setAdditionalInterestList(loc1LNBldg1AdditionalInterests);
		locOneBuildingList.add(loc1Bldg1);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));					

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver).withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("PaymentAllocationPlanAndPolicyLevelDelinqPlanTest")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.SelfStorageFacilities))
				.withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.getRandom())
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		lienholderNumber=myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();		
	}
	
	@Test(dependsOnMethods = { "generate" })	
	public void verifyPmtAllocPlanForInsuredAndLH() throws Exception {		
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.FBM_Convert_Account_To_Policy_Level);
		//verify payment plan type for insured
		BCAccountSummary acctSummary = new BCAccountSummary(driver);
		try{
			existingInsuredAllocPlan=acctSummary.getPaymentAllocationPlan();
			if(!existingInsuredAllocPlan.equals(insuredAllocationPlanShouldBe)){
				Assert.fail("insured has incorrect Payment Allocation Plan.");
			}
		}catch(Exception e){
			Assert.fail("insured Payment Allocation Plan doesn't exist.");
		}
		//verify payment plan type for lienholder
		BCSearchAccounts search=new BCSearchAccounts(driver);
		search.searchAccountByAccountNumber(lienholderNumber);
		acctSummary = new BCAccountSummary(driver);
		try{
			existingLHAllocPlan=acctSummary.getPaymentAllocationPlan();
			if(!existingLHAllocPlan.equals(lienAllocationPlanShouldBe)){
				Assert.fail("lienholder has incorrect Payment Allocation Plan.");
			}
		}catch(Exception e){
			Assert.fail("lienholder Payment Allocation Plan doesn't exist.");
		}		
	}
	@Test(dependsOnMethods = { "verifyPmtAllocPlanForInsuredAndLH" })	
	public void verifyDelinquencyOnNewPolicy() throws Exception {
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
			existingDelinqPlan = policySum.getDelinquencyPlan();
			if(!existingDelinqPlan.equals(delinqPlanShouldBe))
				Assert.fail("the policy level delinqency plan is incorrect for the existing account "+ existingAcctNum);
		}catch(Exception e){
			Assert.fail("the policy level delinqency plan doesn't exist for existing account "+ existingAcctNum);
		}		
	}
}

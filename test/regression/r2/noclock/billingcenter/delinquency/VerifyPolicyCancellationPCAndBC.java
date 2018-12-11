/*
 * Jessica Qu -- June 2nd, 2015
 * 
 */
package regression.r2.noclock.billingcenter.delinquency;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountInvoices;
import repository.bc.account.AccountPolicies;
import repository.bc.account.BCAccountMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.workorders.cancellation.StartCancellation;

@QuarantineClass
public class VerifyPolicyCancellationPCAndBC extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private GeneratePolicy myPolicyObj2 = null;

	@Test
	public void testPolicyBound() throws Exception {

		// Get Basic 1 Location with 1 Building
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		locOneBuildingList.add(new PolicyLocationBuilding());
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        this.myPolicyObj = new GeneratePolicy.Builder(driver).withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("BindIssuance").withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail) {
					{
						this.setAdditionalCoverageStuff(new PolicyBusinessownersLineAdditionalCoverages(false, false));
					}
				}).withPolicyLocations(locationsList)
				// .withRandomPaymentPlanType()
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				// .withRandomDownPaymentType()
				.withDownPaymentType(PaymentType.Check).build(GeneratePolicyType.PolicySubmitted);

	}

	@Test(dependsOnMethods = { "testPolicyBound" })
	public void updateToPolicyIssued() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		this.myPolicyObj2.convertTo(driver, GeneratePolicyType.PolicyIssued);

        System.out.println(new GuidewireHelpers(driver).getCurrentPolicyType(myPolicyObj2).toString());
	}

	@Test(dependsOnMethods = { "updateToPolicyIssued" })
    public void flatCancelInsuredBoundOnAccident() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		Login myloginPage = new Login(driver);
		myloginPage.login(this.myPolicyObj2.underwriterInfo.getUnderwriterUserName(),
				this.myPolicyObj2.underwriterInfo.getUnderwriterPassword());

		// search for policy
        SearchPoliciesPC policySearch = new SearchPoliciesPC(driver);
		policySearch.searchPolicyByAccountNumber(this.myPolicyObj2.accountNumber);

        StartCancellation cancelPol = new StartCancellation(driver);
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.SubmittedOnAccident, "Messup on Agent's Part", null,
				true);

	}

	@Test(dependsOnMethods = { "flatCancelInsuredBoundOnAccident" })
	public void flatCancelVerificationBC() throws Exception {
		boolean errorsFound = false;
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);

		Login lp = new Login(driver);
		lp.login("sbrunson", "gw");

        AccountInvoices myAcctInv = new AccountInvoices(driver);
		errorsFound = myAcctInv.verifyFlatPolicyCancellation(this.myPolicyObj);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuPolicies();

        AccountPolicies myPolicyPage = new AccountPolicies(driver);
        errorsFound = myPolicyPage.verifyCancellation(this.myPolicyObj.busOwnLine.getPolicyNumber());

		if (errorsFound) {
			Assert.fail("The Policy Cancellation in BillingCenter failed, please see the output for the details.");
		}
	}
	
	public GeneratePolicy getIssuedPolicy(){
		return this.myPolicyObj2;
	}
}

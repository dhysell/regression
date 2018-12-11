package regression.r2.clock.billingcenter.cancel.membershipcancels;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountPolicies;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonCharges;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.policy.BCPolicyMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PolicyStatus;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.ReinstateReason;
import repository.gw.enums.TransactionType;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.reinstate.StartReinstate;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

public class TestMembershipOnlyUnderwriterCancellation extends BaseTest {
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser;
	private WebDriver driver;

    @Test
    public void generatePolicy() throws GuidewireException, Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withInsFirstLastName("Membership", "UW Cancel")
                .withProductType(ProductLineType.Membership)
                .withPaymentPlanType(PaymentPlanType.Quarterly)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = { "generatePolicy" })
	public void runInvoiceWithoutMakingDownpayment() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), new GuidewireHelpers(driver).getPolicyNumber(this.myPolicyObj));

		BCCommonTroubleTickets troubleTicket = new BCCommonTroubleTickets(driver);
		troubleTicket.closeFirstTroubleTicket();

		BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
		policyMenu.clickTopInfoBarAccountNumber();
		
		NewDirectBillPayment payment = new NewDirectBillPayment(driver);
		payment.makeInsuredDownpayment(this.myPolicyObj, new GuidewireHelpers(driver).getPolicyPremium(this.myPolicyObj).getDownPaymentAmount(), new GuidewireHelpers(driver).getPolicyNumber(this.myPolicyObj));
		
		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "runInvoiceWithoutMakingDownpayment" })
	public void moveClocks() throws Exception {
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 10);
	}

	@Test(dependsOnMethods = { "moveClocks" })
	public void cancelPolicyInPC() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(), new GuidewireHelpers(driver).getPolicyNumber(this.myPolicyObj));

		StartCancellation cancellation = new StartCancellation(driver);
		cancellation.cancelPolicy(CancellationSourceReasonExplanation.MembershipNoReasonGiven, "Cancel Policy", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), true);
		
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "cancelPolicyInPC" })
	public void verifyCancellationInBC() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);
		
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuPolicies();
		
		AccountPolicies policiesPage = new AccountPolicies(driver);
		boolean cancellationFound = false;
		int timeLeftToCheck = 120;
		while (!cancellationFound && timeLeftToCheck > 0) {
			try {
				policiesPage.getAccountPolicyTableRow(new GuidewireHelpers(driver).getPolicyNumber(this.myPolicyObj), null, null, null, PolicyStatus.Canceled, null, null, null, null, null);
				cancellationFound = true;
			} catch (Exception e) {
				new GuidewireHelpers(driver).refreshPage();
				timeLeftToCheck = timeLeftToCheck - 5;
			}
		}
		
		if (!cancellationFound) {
			Assert.fail("The cancellation billing instruction did not make it to BC withing 2 minutes. Test cannot continue.");
		}
		
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "verifyCancellationInBC" })
	public void reinstatePolicyInPC() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(), new GuidewireHelpers(driver).getPolicyNumber(this.myPolicyObj));
				
		StartReinstate reinstate = new StartReinstate(driver);
		reinstate.reinstatePolicy(ReinstateReason.Other, "Just cause I feel like it");
	}
	
	@Test(dependsOnMethods = { "reinstatePolicyInPC" })
	public void verifyChargesInBC() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);
		
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();
		
		BCCommonCharges accountCharges = new BCCommonCharges(driver);
		if (accountCharges.getListOfChargesPerContext(TransactionType.Policy_Issuance).size() != 1) {
			Assert.fail("There was not only 1 charge for Issuance, Cancellation, and Reinstatement of the policy. Test cannot continue.");
		}
		if (accountCharges.getListOfChargesPerContext(TransactionType.Policy_Issuance).get(0) != new GuidewireHelpers(driver).getPolicyPremium(this.myPolicyObj).getDownPaymentAmount()) {
			Assert.fail("The charge for issuance did not match what it should have been.");
		}
		if (accountCharges.getListOfChargesPerContext(TransactionType.Cancellation).size() > 0 || accountCharges.getListOfChargesPerContext(TransactionType.Reinstatement).size() > 0) {
			Assert.fail("Charges were sent to BC for cancellation or reinstatement of membership dues on an underwriting cancel. This should not happen. Test Failed.");
		}
		
		new GuidewireHelpers(driver).logout();
	}
}

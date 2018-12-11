package coreBusiness.cancel.membership;

import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonCharges;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.common.BCCommonTroubleTickets;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Cancellation.CancellationSource;
import repository.gw.enums.Cancellation.PendingCancellationSourceReasonExplanation;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PolicyTermStatus;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.ReinstateReason;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicySummary;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.reinstate.StartReinstate;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

@Test(groups = {"ClockMove","Delinquencies","BillingCenter"})
public class TestMembershipOnlyDelinquencyCancellationNoExceptions extends BaseTest {
	private GeneratePolicy myPolicyObj = null;
	private Date targetDate = null;
	private ARUsers arUser;
	private WebDriver driver;


    @Test
    public void generatePolicy() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withInsFirstLastName("Membership", "Policy")
                .withProductType(ProductLineType.Membership)
                .withPaymentPlanType(PaymentPlanType.Quarterly)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = { "generatePolicy" })
	public void runInvoiceWithoutMakingDownpayment() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), new GuidewireHelpers(driver).getPolicyNumber(this.myPolicyObj));

		BCCommonTroubleTickets troubleTicket = new BCCommonTroubleTickets(driver);
		troubleTicket.closeFirstTroubleTicket();

		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
		new GuidewireHelpers(driver).logout();
	}

    @Test(dependsOnMethods = { "runInvoiceWithoutMakingDownpayment" })
	public void moveClocks() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
	}

	@Test(dependsOnMethods = { "moveClocks" })
	public void runInvoiceDueAndCheckDelinquency() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);

		BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickBCMenuDelinquencies();

		this.targetDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
		BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
        boolean delinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Closed, myPolicyObj.accountNumber, new GuidewireHelpers(driver).getPolicyNumber(this.myPolicyObj), targetDate);

		if (!delinquencyFound) {
			int timeLeftToCheck = 120;
			while (!delinquencyFound && timeLeftToCheck > 0) {
				new GuidewireHelpers(driver).refreshPage();
				timeLeftToCheck = timeLeftToCheck - 5;
				delinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Closed, myPolicyObj.accountNumber, new GuidewireHelpers(driver).getPolicyNumber(this.myPolicyObj), targetDate);
			}
			
			if (!delinquencyFound) {
				Assert.fail("The Delinquency was either non-existant or not in an 'closed' status.");
			}
		}

		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "runInvoiceDueAndCheckDelinquency" })
	public void verifyCancelationCompletionInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        AccountSummaryPC summaryPage = new AccountSummaryPC(driver);
        boolean cancelledStatusFound = summaryPage.verifyPolicyStatusInPolicyCenter(new GuidewireHelpers(driver).getPolicyNumber(this.myPolicyObj), PolicyTermStatus.Canceled, 300);
		if (!cancelledStatusFound) {
			Assert.fail("The policy had not entered delinquency after 5 minutes of waiting.");
		}
		
		summaryPage.clickActivitySubject("Cancel Policy for Accounts Receivable");
		
		StartCancellation pcCancellation = new StartCancellation(driver);
		if (!pcCancellation.getSourceValue().equals(CancellationSource.Carrier)) {
			Assert.fail("The Cancellation source was not 'Carrier'. Test Failed.");
		}
		if (!pcCancellation.getCancellationReasonValue().equalsIgnoreCase(PendingCancellationSourceReasonExplanation.NoPaymentReceived.getReasonValue())) {
			Assert.fail("The Cancellation Reason was not 'non-payment'. Test Failed.");
		}
		if (!pcCancellation.getCancellationReasonExplanationValue().equalsIgnoreCase(PendingCancellationSourceReasonExplanation.NoPaymentReceived.getExplanationValue())) {
			Assert.fail("The Cancellation Explanation was not 'no payment received'. Test Failed.");
		}
		if (!pcCancellation.getEffectiveDate().equals(new GuidewireHelpers(driver).getPolicyEffectiveDate(this.myPolicyObj))) {
			Assert.fail("The cancellation effective date was not the effective date of the policy. Test Failed.");
		}

		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "verifyCancelationCompletionInPolicyCenter" })
	public void makePaymentInBC() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuActionsNewDirectBillPayment();
		
		NewDirectBillPayment payment = new NewDirectBillPayment(driver);
		payment.makeDirectBillPaymentExecuteWithoutDistribution(new GuidewireHelpers(driver).getPolicyPremium(this.myPolicyObj).getDownPaymentAmount(), new GuidewireHelpers(driver).getPolicyNumber(this.myPolicyObj));
		
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "makePaymentInBC" })
	public void reinstatePolicyInPC() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(), new GuidewireHelpers(driver).getPolicyNumber(this.myPolicyObj));
		
		PolicySummary summaryPage = new PolicySummary(driver);
		if (!summaryPage.checkIfActivityExists("Payment within 30 days")) {
			Assert.fail("The activity for payment on a cancelled policy did not appear. Test cannot continue.");
		}
		
		StartReinstate reinstate = new StartReinstate(driver);
		reinstate.reinstatePolicy(ReinstateReason.Payment_Received, null);
	}
	
	@Test(dependsOnMethods = { "reinstatePolicyInPC" })
	public void verifyChargesInBC() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();
		
		BCCommonCharges accountCharges = new BCCommonCharges(driver);
		if (accountCharges.getListOfChargesPerContext(TransactionType.Policy_Issuance).size() != 1 && accountCharges.getListOfChargesPerContext(TransactionType.Cancellation).size() != 1 && accountCharges.getListOfChargesPerContext(TransactionType.Reinstatement).size() != 1) {
			Assert.fail("There was not only 1 charge for Issuance, Cancellation, and Reinstatement of the policy. Test cannot continue.");
		}
		if (accountCharges.getListOfChargesPerContext(TransactionType.Policy_Issuance).get(0) != new GuidewireHelpers(driver).getPolicyPremium(this.myPolicyObj).getDownPaymentAmount()) {
			Assert.fail("The charge for issuance did not match what it should have been.");
		}
		if (accountCharges.getListOfChargesPerContext(TransactionType.Cancellation).get(0) != -new GuidewireHelpers(driver).getPolicyPremium(this.myPolicyObj).getDownPaymentAmount()) {
			Assert.fail("The charge for cancellation did not match what it should have been.");
		}
		if (accountCharges.getListOfChargesPerContext(TransactionType.Reinstatement).get(0) != new GuidewireHelpers(driver).getPolicyPremium(this.myPolicyObj).getDownPaymentAmount()) {
			Assert.fail("The charge for reinstatement did not match what it should have been.");
		}
		
		new GuidewireHelpers(driver).logout();
	}
}

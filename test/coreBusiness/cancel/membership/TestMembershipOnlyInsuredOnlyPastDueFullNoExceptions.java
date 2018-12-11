package coreBusiness.cancel.membership;

import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.policy.BCPolicyMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.CancellationEvent;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentReturnedPaymentReason;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PolicyTermStatus;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

@Test(groups = {"ClockMove","Delinquencies","BillingCenter"})
public class TestMembershipOnlyInsuredOnlyPastDueFullNoExceptions extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	public ARUsers arUser;	
	private Date targetDate = null;
	private String getTypeOfCancel; 

	public String whatToTest() {
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_MONTH);
		if (day % 2 == 0 ) {
			return getTypeOfCancel = "NonPay";			
		} else {
			return getTypeOfCancel = "ReversePayment";
		}
	}

	@Test
	public void generatePolicy() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsFirstLastName("MembershipOnly", "FullCancel")
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
		whatToTest();

		if (getTypeOfCancel.equals("NonPay")) {
			new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.membership.getPolicyNumber());
			BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
			policyMenu.clickBCMenuTroubleTickets();
			BCCommonTroubleTickets troubleTicket = new BCCommonTroubleTickets(driver);
			troubleTicket.waitForTroubleTicketsToArrive(120);
			new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		}else {
			new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);
			NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
			newPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.membership.getPremium().getDownPaymentAmount(), myPolicyObj.membership.getPolicyNumber());						
		}
	}

	@Test(dependsOnMethods = { "runInvoiceWithoutMakingDownpayment" })
	public void moveClocks() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 11);
	}

	@Test(dependsOnMethods = { "moveClocks" })
	public void runInvoiceDueAndCheckDelinquency() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Release_Trouble_Ticket_Holds);

		if (getTypeOfCancel.equals("NonPay")) {

			BCAccountMenu acctMenu = new BCAccountMenu(driver);
			acctMenu.clickBCMenuDelinquencies();
			targetDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
			BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
			boolean delinquencyFound = delinquency.verifyDelinquencyStatus(null,	this.myPolicyObj.accountNumber, null, targetDate);
			if (!delinquencyFound) {
				Assert.fail("The Delinquency was either non-existant or not in an 'open' status.");
			} 

		}else {
			ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 5);
			BCAccountMenu acctMenu=new BCAccountMenu(driver);
			acctMenu.clickAccountMenuPayments();

			AccountPayments accountPayments = new AccountPayments(driver);
			accountPayments.reversePaymentAtFault(null,myPolicyObj.membership.getPremium().getDownPaymentAmount(), null, null, PaymentReturnedPaymentReason.AccountFrozen);
			acctMenu.clickBCMenuDelinquencies();

			targetDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
			BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
			boolean delinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Open, this.myPolicyObj.accountNumber, null, targetDate);

			if (!delinquencyFound) {
				Assert.fail("The Delinquency was either non-existant or not in an 'open' status.");
			}
		}
	}

	@Test(dependsOnMethods = { "runInvoiceDueAndCheckDelinquency" })
	public void verifyCancelationCompletionInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

		new BatchHelpers(driver).runBatchProcess(BatchProcess.PC_Workflow);

		AccountSummaryPC summaryPage = new AccountSummaryPC(driver);
		boolean cancelledStatusFound = summaryPage.verifyPolicyStatusInPolicyCenter(this.myPolicyObj.membership.getPolicyNumber(), PolicyTermStatus.Canceled, 300);
		if (!cancelledStatusFound) {
			Assert.fail("The policy had not entered delinquency after 5 minutes of waiting.");
		}
	}

	@Test(dependsOnMethods = { "verifyCancelationCompletionInPolicyCenter" })
	public void verifyDelinquencyStepsInBillingCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

		BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickBCMenuDelinquencies();

		BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		if (!delinquency.verifyDelinquencyEventCompletion(CancellationEvent.CancellationBillingInstructionReceived)) {
			Assert.fail("The policy did not get the delinquency instruction back from Policy Center as expected. Test Failed.");
		}		
	}
}

package previousProgramIncrement.pi1_041918_062718.f165_RenewalDatePayments_CancelReinstates;

import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountInvoices;
import repository.bc.account.AccountPolicies;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonCharges;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.common.BCCommonTroubleTicket;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.policy.BCPolicyMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.TransactionType;
import repository.gw.enums.TroubleTicketFilter;
import repository.gw.enums.TroubleTicketType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author sgunda
* @Requirement 7-day grace period on all Renewals
* @RequirementsLink <a href="https://rally1.rallydev.com/#/203558466972d/detail/defect/210867882812">US14831 - 7-day grace period on all Renewals</a>
* @DATE Apr 28, 2018
*/

@Test(groups = {"ClockMove"})
public class US14831TTOf7DayGracePeriodOnAllRenewalsNoExceptions extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObj = null;
	public ARUsers arUser;
	private BCAccountMenu acctMenu;
	private AccountInvoices acctInvoice;
	Date invoiceDate , invoiceDueDate, TTDate , TTDueDate;

	@Test
	public void generatePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		myPolicyObj = new GeneratePolicy.Builder(driver)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PersonalAutoLinePL)
				.withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("US14831", "TT")
				//.withPolTermLengthDays(100)
				.withPolOrgType(OrganizationType.Individual)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}

	@Test(dependsOnMethods = { "generatePolicy" })
	public void downPayment() throws Exception {	 		 
	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

		NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
        directPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.squire.getPremium().getDownPaymentAmount(), myPolicyObj.squire.getPolicyNumber());
        ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
        ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 15);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Release_Trouble_Ticket_Holds);
        ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(myPolicyObj.squire.getExpirationDate(), DateAddSubtractOptions.Day, -30));

	}
	
	@Test(dependsOnMethods = {"downPayment"})
	public void issueRenewal() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new StartRenewal(driver).loginAsUWAndIssueRenewal(myPolicyObj);

	}

	@Test(dependsOnMethods = { "issueRenewal" })
	public void makeTestTTAndVerify() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		acctMenu = new BCAccountMenu(driver);
	     acctMenu.clickBCMenuCharges();
		BCCommonCharges charges = new BCCommonCharges(driver);
	     Assert.assertTrue(charges.waitUntilChargesFromPolicyContextArrive(TransactionType.Renewal), "Renewal charges didn't make it to BC, Test can not continue");

	     acctMenu.clickAccountMenuInvoices();

		acctInvoice = new AccountInvoices(driver);
	     invoiceDate = acctInvoice.getInvoiceDateByInvoiceType(InvoiceType.RenewalDownPayment);
	     invoiceDueDate = acctInvoice.getInvoiceDueDateByInvoiceType(InvoiceType.RenewalDownPayment);
	     
	     ClockUtils.setCurrentDates(driver, invoiceDate);
	     new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

	     acctMenu.clickBCMenuCharges();
	     acctMenu.clickAccountMenuInvoices();
	     Assert.assertTrue(acctInvoice.verifyInvoice(InvoiceType.RenewalDownPayment, InvoiceStatus.Billed, null), "Invoice did not go billed, which wont create TT for holding Deliquency");

	     acctMenu.clickAccountMenuPolicies();
		AccountPolicies accountPolicy = new AccountPolicies(driver);
	     accountPolicy.clickPolicyNumber(myPolicyObj.squire.getPolicyNumber().concat("-2"));

		BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
		policyMenu.clickBCMenuTroubleTickets();

		BCCommonTroubleTickets troubleTickets = new BCCommonTroubleTickets(driver);
	     troubleTickets.clickTroubleTicketNumberBySubjectOrType(TroubleTicketType.RenewalGracePeriod);

		BCCommonTroubleTicket troubleTicket = new BCCommonTroubleTicket(driver);
	     TTDate = troubleTicket.getTroubleTicketCreationDate();
	     TTDueDate = troubleTicket.getTroubleTicketDueDate();
	     
	     Assert.assertEquals(TTDate , invoiceDate,"TT date is wrong");
	     Assert.assertEquals(TTDueDate,DateUtils.dateAddSubtract(invoiceDueDate, DateAddSubtractOptions.Day, 7),"TT due date is wrong");
	     
	     policyMenu.clickTopInfoBarAccountNumber();

	     ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(invoiceDueDate, DateAddSubtractOptions.Day, 1));
	     new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
	     acctMenu.clickAccountMenuInvoices();
	     Assert.assertTrue(acctInvoice.verifyInvoice(InvoiceType.RenewalDownPayment, InvoiceStatus.Due, null), "Invoice did not go Due, Test can not continue");
	     acctMenu.clickBCMenuDelinquencies();
		BCCommonDelinquencies delinquencies = new BCCommonDelinquencies(driver);
	     Assert.assertFalse(delinquencies.verifyDelinquencyExists(null, null, myPolicyObj.accountNumber, null, myPolicyObj.squire.getPolicyNumber().concat("-2"), null, null, null),"Delinquency triggered even when TT is open");
	     
	     acctMenu.clickAccountMenuPolicies();
	     accountPolicy.clickPolicyNumber(myPolicyObj.squire.getPolicyNumber().concat("-2"));
		policyMenu.clickBCMenuTroubleTickets();
	     
	     troubleTickets.setTroubleTicketFilter(TroubleTicketFilter.All);
	     
	     ClockUtils.setCurrentDates(driver, TTDueDate);
	     new BatchHelpers(cf).runBatchProcess(BatchProcess.Release_Trouble_Ticket_Holds);
	     
	     troubleTickets.clickTroubleTicketNumberBySubjectOrType(TroubleTicketType.RenewalGracePeriod);
	     Assert.assertTrue(troubleTicket.verifyIfTroubleTicketIsInClosedStatus(), "TT didnot close after the Release Trouble Ticket Holds batch ran");
	     
	     policyMenu.clickTopInfoBarAccountNumber();
		acctMenu.clickBCMenuDelinquencies();
	     Assert.assertTrue(delinquencies.verifyDelinquencyExists(null, DelinquencyReason.NotTaken, myPolicyObj.accountNumber, null, myPolicyObj.squire.getPolicyNumber().concat("-2"), TTDueDate, null, null),"Delinquency didn't trigger after the TT release");
	}
}
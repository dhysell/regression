package regression.r2.noclock.billingcenter.disbursements;

import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountDisbursements;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.summary.BCAccountSummary;
import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.BCDesktopMyActivities;
import repository.bc.desktop.BCDesktopMyQueues;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.search.BCSearchAccounts;
import repository.bc.search.BCSearchInvoices;
import repository.bc.search.BCSearchPolicies;
import repository.bc.wizards.CreateAccountDisbursementWizard;
import repository.driverConfiguration.Config;
import repository.gw.enums.ActivityQueuesBillingCenter;
import repository.gw.enums.DisbursementReason;
import repository.gw.enums.DisbursementStatus;
import repository.gw.enums.InvoiceStatus;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.FinanceUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.FinanceUsersHelper;

/**
* @Author bhiltbrand
* @Requirement This test verifies that the Finance Clerical and Finance Manager roles have the authority to create a disbursement.
* @RequirementsLink <a href="https://rally1.rallydev.com/#/68999021356d/detail/defect/66805416124">DE4076</a>
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/07%20-%20Disbursements/07-03%20Accounting%20Requested%20Disbursement.docx">Account Request Disbursements Requirements</a>
* @Description 
* @DATE Feb 28, 2017
*/
public class TestDisbursementCreationByFinance extends BaseTest {
	private WebDriver driver;
	public ARUsers arUser = new ARUsers();
	public FinanceUsers financeUser = new FinanceUsers();
	public Agents pcAgent = new Agents();
	public String accountNumber = "";
	public String policyNumber = "";
	public double disbursementAmount = 50.00;
	public ActivityQueuesBillingCenter queueAssignedForDisbursement = null;
	public String disbursementMadeTo = "";

	@Test
	public void findAccountToUse() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRole(ARUserRole.Billing_Manager);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());

		BCSearchInvoices invoiceSearch = new BCSearchInvoices(driver);
		int randamNumber = NumberUtils.generateRandomNumberInt(270, 279);
		this.accountNumber = invoiceSearch.searchForAccountByInvoiceAndAmountRange(Integer.toString(randamNumber), 150, 500, InvoiceStatus.Due);
		this.accountNumber = this.accountNumber.substring(0, 6);

		BCSearchPolicies policySearch = new BCSearchPolicies(driver);
		this.policyNumber = policySearch.searchPolicyByAccountNumber(this.accountNumber);
		getQALogger().info("Account Number Used: " + this.accountNumber);
		getQALogger().info("Policy Number used: " + this.policyNumber);

        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
		policyMenu.clickTopInfoBarAccountNumber();


		BCAccountSummary accountSummary = new BCAccountSummary(driver);
		double unappliedAmount = accountSummary.getUnappliedFundByPolicyNumber(this.policyNumber);
		
		if (unappliedAmount < this.disbursementAmount) {
            BCAccountMenu accountMenu = new BCAccountMenu(driver);
            accountMenu.clickAccountMenuActionsNewDirectBillPayment();
			

            NewDirectBillPayment payment = new NewDirectBillPayment(driver);
			payment.makeDirectBillPaymentExecuteWithoutDistribution(500, this.policyNumber);
		}
		
		new GuidewireHelpers(driver).logout();
	}
		
	@Test (dependsOnMethods = { "findAccountToUse" })
	public void checkAbilityToCreateDisbursements() throws Exception {
		this.financeUser = FinanceUsersHelper.getRandomFinanceUser();
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(financeUser.getUserName(), financeUser.getPassword(), this.accountNumber);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuActionsNewTransactionDisbursement();
		

		Date todaysDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
		CreateAccountDisbursementWizard accountDisbursement = new CreateAccountDisbursementWizard(driver);
		accountDisbursement.setCreateAccountDisbursementWizardPolicyNumberDropDown(this.policyNumber);
		accountDisbursement.setCreateAccountDisbursementWizardUnappliedFund(this.policyNumber);
		accountDisbursement.setCreateAccountDisbursementWizardAmount(this.disbursementAmount);
		accountDisbursement.setCreateAccountDisbursementWizardDueDate(todaysDate);
		accountDisbursement.setCreateAccountDisbursementWizardReasonFor(DisbursementReason.Overpayment);
		accountDisbursement.clickNext();
		if (accountDisbursement.alertBarExists()) {
			String errorMessage = accountDisbursement.getAlertBarBanner();	
			if (!errorMessage.equals("Alert: Completing this change will create a disbursement that will be sent for approval since you do not have sufficient authority.")) {
				Assert.fail("The error message displayed was not the message expected for creating a disbursement as a finance person. The message displayed was \"" + errorMessage + "\". Test failed.");
			}
		} else {
			Assert.fail("The expected error banner never appeared attempting to create a disbursement as a finance person. Test failed.");
		}
		accountDisbursement.clickFinish();

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuDisbursements();


		AccountDisbursements accountDisbursements = new AccountDisbursements(driver);
		if (!accountDisbursements.verifyDisbursements(this.policyNumber, DisbursementStatus.Awaiting_Approval, this.disbursementAmount)) {
			Assert.fail("The disbursement was created, but was not in the status expected. Test Failed.");
		}
		
		this.queueAssignedForDisbursement = ActivityQueuesBillingCenter.valueOfName(new TableUtils(driver).getCellTextInTableByRowAndColumnName(accountDisbursements.getDisbursementsTable(), new TableUtils(driver).getRowCount(accountDisbursements.getDisbursementsTable()), "Assignee"));
		ActivityQueuesBillingCenter queueCheck = null;
		if (this.policyNumber.startsWith("01")) {
			queueCheck = ActivityQueuesBillingCenter.ARSupervisorFarmBureau;
		} else if (this.policyNumber.startsWith("08")) {
			queueCheck = ActivityQueuesBillingCenter.ARSupervisorWesternCommunity;
		}
		
		if (!this.queueAssignedForDisbursement.equals(queueCheck)) {
			Assert.fail("The queue assigned for approval of this disbursement was incorrect. Test failed.");
		}
		
		this.disbursementMadeTo = new TableUtils(driver).getCellTextInTableByRowAndColumnName(accountDisbursements.getDisbursementsTable(), new TableUtils(driver).getRowCount(accountDisbursements.getDisbursementsTable()), "Pay To");
		
		new GuidewireHelpers(driver).logout();
	}
	
	@Test (dependsOnMethods = { "checkAbilityToCreateDisbursements" })
	public void checkDisburementQueueAssignment() throws Exception {
		if (this.policyNumber.startsWith("01")) {
			this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
		} else if (this.policyNumber.startsWith("08")) {
			this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		}
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());

		BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuMyQueues();
		BCDesktopMyQueues myQueues = new BCDesktopMyQueues(driver);
		if (this.policyNumber.startsWith("01")) {
			myQueues.setMyQueuesFilter(ActivityQueuesBillingCenter.ARSupervisorFarmBureau);
		} else if (this.policyNumber.startsWith("08")) {
			myQueues.setMyQueuesFilter(ActivityQueuesBillingCenter.ARSupervisorWesternCommunity);
		}

		String textInQueue = "Approval needed for Disbursement of " + this.disbursementAmount + "0" + " usd requested by " + this.financeUser.getFirstName() + " " + this.financeUser.getLastName();
		myQueues.clickMyQueuesResultTableTitleToSort("Opened");
		myQueues.clickMyQueuesResultTableTitleToSort("Opened");

		myQueues.clickCheckboxInTableByLinkText(textInQueue);
		myQueues.clickAssignSelectedToMe();
		desktopMenu.clickDesktopMenuMyActivities();
		
		BCDesktopMyActivities myActivities= new BCDesktopMyActivities(driver);	
		myActivities.clickActivityTableTitleToSort("Opened");
		myActivities.clickActivityTableTitleToSort("Opened");
		myActivities.clickLinkInActivityResultTable(textInQueue);
		myActivities.clickActivityApprove();

		BCSearchAccounts accountSearch = new BCSearchAccounts(driver);
		accountSearch.searchAccountByAccountNumber(this.accountNumber);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuDisbursements();

		AccountDisbursements accountDisbursements = new AccountDisbursements(driver);
		if (!accountDisbursements.verifyDisbursements(this.policyNumber, DisbursementStatus.Approved, this.disbursementAmount)) {
			Assert.fail("The disbursement was created, but was not in the status expected, namely it was not approved by the disbursement specialist. Test Failed.");
		}
		
		new GuidewireHelpers(driver).logout();
	}
}

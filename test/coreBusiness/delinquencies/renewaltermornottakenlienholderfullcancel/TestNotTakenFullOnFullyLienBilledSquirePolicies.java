package coreBusiness.delinquencies.renewaltermornottakenlienholderfullcancel;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.search.BCSearchAccounts;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.GenerateContactType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GeneratePolicyHelper;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

import java.util.ArrayList;
import java.util.Date;

/**
* @Author sgunda
* @Description   US13897 - Insurance policy w/ Membership line Delinquency: Research and GUnits
* @DATE March 21, 2018
*/


public class TestNotTakenFullOnFullyLienBilledSquirePolicies extends BaseTest {
	
	private GeneratePolicy myPolicyObj;
	private String loanNumber = "ThisIsLoan";
	private ARUsers arUser = new ARUsers();
	private String lienholderNumber = null;
	private String lienholderLoanNumber = null;
	private double lienholderLoanPremiumAmount;

	private BCAccountMenu acctMenu;
	private BCCommonDelinquencies  acctDelinquency;
	private Date dayAfterInvoiceDueDate,invoiceDueDate,invoiceBillDate;
	private WebDriver driver;
		
		
	@Test
	public void squirePolicy() throws Exception {
		ArrayList<GenerateContact> generateContacts= new ArrayList<>();
		Config cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);

		ArrayList<repository.gw.enums.ContactRole> rolesToAdd = new ArrayList<repository.gw.enums.ContactRole>();
		rolesToAdd.add(repository.gw.enums.ContactRole.Lienholder);

		GenerateContact generateContact = new GenerateContact.Builder(driver)
				.withCompanyName("LH FullLienBilled")
				.withRoles(rolesToAdd)
				.withGeneratedLienNumber(true)
				.withUniqueName(true)
				.build(GenerateContactType.Company);

		generateContacts.add(generateContact);
		driver.quit();

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		myPolicyObj = new GeneratePolicyHelper(driver).generateFullyLienBilledSquirePLPolicy("US13897", "PL",null, repository.gw.enums.PaymentPlanType.Annual , repository.gw.enums.PaymentType.ACH_EFT, generateContacts);
		Assert.assertNotNull(myPolicyObj,"Generate failed, please investigate from logs");

		this.lienholderNumber = this.myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
		this.lienholderLoanPremiumAmount = this.myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();

	}
	
	@Test(dependsOnMethods = { "squirePolicy" })
	public void MakeInvoiceBilledAndDue() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienholderNumber);
		
		invoiceDueDate = repository.gw.helpers.DateUtils.dateAddSubtract(new GuidewireHelpers(driver).getPolicyEffectiveDate(myPolicyObj), repository.gw.enums.DateAddSubtractOptions.Month, 1);
		invoiceBillDate = repository.gw.helpers.DateUtils.dateAddSubtract(invoiceDueDate, repository.gw.enums.DateAddSubtractOptions.Day, -20);
		dayAfterInvoiceDueDate = repository.gw.helpers.DateUtils.dateAddSubtract(invoiceDueDate, repository.gw.enums.DateAddSubtractOptions.Day, 1);
		
		//Make Invoice Billed 
		repository.gw.helpers.ClockUtils.setCurrentDates(driver, invoiceBillDate);
		new BatchHelpers(driver).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);
		
		acctMenu= new BCAccountMenu(driver);
		acctMenu.clickAccountMenuActionsNewDirectBillPayment();
		NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
		newPayment.makeDirectBillPaymentExecute(myPolicyObj.squire.getPolicyNumber(), this.lienholderLoanNumber, this.lienholderLoanPremiumAmount);
		
		//Make Invoice Due
		repository.gw.helpers.ClockUtils.setCurrentDates(driver, dayAfterInvoiceDueDate);
		new BatchHelpers(driver).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);
		
		//Verify if Lien Invoice Went Due
		acctMenu.clickAccountMenuInvoices();
		AccountInvoices acctInvoices = new AccountInvoices(driver);
		Assert.assertTrue(acctInvoices.verifyInvoice(invoiceBillDate, invoiceDueDate, null, repository.gw.enums.InvoiceType.NewBusinessDownPayment, null, repository.gw.enums.InvoiceStatus.Due, lienholderLoanPremiumAmount, 0.0), "Lien Invoice didn't go due or paid money was not applied");
		
		repository.gw.helpers.ClockUtils.setCurrentDates(driver, repository.gw.helpers.DateUtils.dateAddSubtract(myPolicyObj.squire.getExpirationDate(), repository.gw.enums.DateAddSubtractOptions.Day, -48));
				
	}

	@Test(dependsOnMethods = {"MakeInvoiceBilledAndDue"})
    public void renewPolicyInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new StartRenewal(driver).loginAsUWAndIssueRenewal(myPolicyObj);

	}
	
	@Test(dependsOnMethods = { "renewPolicyInPolicyCenter" })
	public void verifyIfDelinquencyTriggeredAfterInvoiceGoingDue() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienholderNumber);

		acctMenu= new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
		AccountCharges accountCharges = new AccountCharges(driver);
		Assert.assertTrue(accountCharges.waitUntilChargesFromPolicyContextArrive(120, repository.gw.enums.TransactionType.Renewal),"Policy change charge havent made to BC");
		acctMenu.clickAccountMenuInvoices();
		AccountInvoices acctInvoices = new AccountInvoices(driver);
		
		invoiceDueDate = acctInvoices.getInvoiceDueDateByInvoiceType(repository.gw.enums.InvoiceType.RenewalDownPayment);
		invoiceBillDate = acctInvoices.getInvoiceDateByInvoiceType(repository.gw.enums.InvoiceType.RenewalDownPayment);
		dayAfterInvoiceDueDate = DateUtils.dateAddSubtract(invoiceDueDate, repository.gw.enums.DateAddSubtractOptions.Day, 1);
		
		//Make Invoice Billed 
		repository.gw.helpers.ClockUtils.setCurrentDates(driver, invoiceBillDate);
		new BatchHelpers(driver).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);
		//Make Invoice Due
		repository.gw.helpers.ClockUtils.setCurrentDates(driver, dayAfterInvoiceDueDate);
		new BatchHelpers(driver).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);
		//Verify if Lien Invoice Went Due
		acctMenu.clickBCMenuCharges();
		acctMenu.clickAccountMenuInvoices();
		Assert.assertTrue(acctInvoices.verifyInvoice(invoiceBillDate, invoiceDueDate, null, repository.gw.enums.InvoiceType.RenewalDownPayment, null, repository.gw.enums.InvoiceStatus.Due, null, null), "Lien Invoice didn't go due");
		
		BCSearchAccounts accountSearchBC = new BCSearchAccounts(driver);
        accountSearchBC.searchAccountByAccountNumber(myPolicyObj.accountNumber);
		
        acctMenu.clickBCMenuDelinquencies();
		acctDelinquency = new BCCommonDelinquencies(driver);

        Assert.assertTrue(acctDelinquency.verifyDelinquencyByReason(repository.gw.enums.OpenClosed.Open, repository.gw.enums.DelinquencyReason.NotTaken,null, dayAfterInvoiceDueDate), "Delinquency does not exist Which is not Expected");
	}

}

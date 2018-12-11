package regression.r2.clock.billingcenter.delinquency.testdelinquencies.lienholderfullcancel;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.search.BCSearchAccounts;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.ContactRole;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GeneratePolicyHelper;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.enums.Underwriter.UnderwriterLine;
import persistence.enums.Underwriter.UnderwriterTitle;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
* @Author sgunda
* @Description   US13897 - Insurance policy w/ Membership line Delinquency: Research and GUnits
* @DATE March 21, 2018
*/


public class TestDelinquencyForShortageInvoiceOnFullyLienBilledPolicy extends BaseTest{
	
	private GeneratePolicy myPolicyObj;
	private ARUsers arUser = new ARUsers();
	private Underwriters underwriters = new Underwriters();	
	private String lienholderNumber = null;
	private String lienholderLoanNumber = null;
	private double lienholderLoanPremiumAmount;
	private double changeInPremiumAfterPolicyChange;
	private WebDriver driver;
	
	private BCAccountMenu acctMenu;
	private BCCommonDelinquencies  acctDelinquency;
	private Date dayAfterInvoiceDueDate,invoiceDueDate,invoiceBillDate;
	

	
		
	@Test
	public void squirePolicy() throws Exception {
		ArrayList<GenerateContact> generateContacts= new ArrayList<>();
		Config cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);

		ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
		rolesToAdd.add(ContactRole.Lienholder);

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
		myPolicyObj = new GeneratePolicyHelper(driver).generateFullyLienBilledSquirePLPolicy("US13897", "PL",null,PaymentPlanType.Annual ,PaymentType.ACH_EFT, generateContacts);

		this.lienholderNumber = this.myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
		this.lienholderLoanNumber = this.myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLoanContractNumber();
		this.lienholderLoanPremiumAmount = this.myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();
		
	}
	
	@Test(dependsOnMethods = { "squirePolicy" })
	public void MakeInvoiceBilledAndDue() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienholderNumber);
		
		invoiceDueDate = DateUtils.dateAddSubtract(new GuidewireHelpers(driver).getPolicyEffectiveDate(myPolicyObj), DateAddSubtractOptions.Month, 1);
		invoiceBillDate = DateUtils.dateAddSubtract(invoiceDueDate, DateAddSubtractOptions.Day, -20);
		dayAfterInvoiceDueDate = DateUtils.dateAddSubtract(invoiceDueDate, DateAddSubtractOptions.Day, 1);
		
		//Make Invoice Billed 
		ClockUtils.setCurrentDates(driver, invoiceBillDate);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
		
		acctMenu= new BCAccountMenu(driver);
		acctMenu.clickAccountMenuActionsNewDirectBillPayment();
		NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
		newPayment.makeDirectBillPaymentExecute(myPolicyObj.squire.getPolicyNumber(), this.lienholderLoanNumber, this.lienholderLoanPremiumAmount);
		
		//Make Invoice Due
		ClockUtils.setCurrentDates(driver, dayAfterInvoiceDueDate);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);
		
		//Verify if Lien Invoice Went Due
		acctMenu.clickAccountMenuInvoices();
		AccountInvoices acctInvoices = new AccountInvoices(driver);
		Assert.assertTrue(acctInvoices.verifyInvoice(invoiceBillDate, invoiceDueDate, null, InvoiceType.NewBusinessDownPayment, null, InvoiceStatus.Due, lienholderLoanPremiumAmount, 0.0), "Lien Invoice didn't go due or paid money was not applied");
		
		ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Month, 1));
				
	}

	@Test(dependsOnMethods = { "MakeInvoiceBilledAndDue" })
	public void policyChangeToCreateShortageInvoice() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
		this.underwriters = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(UnderwriterLine.Personal, UnderwriterTitle.Underwriter_Supervisor);	
		new Login(driver).loginAndSearchAccountByAccountNumber(underwriters.getUnderwriterUserName(), underwriters.getUnderwriterPassword(), this.lienholderNumber);
		
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.changePLPropertyCoverage(250000, DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
        
		GenericWorkorderComplete complete = new GenericWorkorderComplete(driver);
		complete.clickViewYourPolicy();
        
		PolicySummary policySummary = new PolicySummary(driver);
        changeInPremiumAfterPolicyChange= policySummary.getPremium("change coverage");
		System.out.println("The cost change after policy change is  : $ "+ changeInPremiumAfterPolicyChange);	
		
	}
	
	@Test(dependsOnMethods = { "policyChangeToCreateShortageInvoice" })
	public void verifyIfDelinquencyTriggeredAfterInvoiceGoingDue() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienholderNumber);

		acctMenu= new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
		AccountCharges accountCharges = new AccountCharges(driver);
		Assert.assertTrue(accountCharges.waitUntilChargesFromPolicyContextArrive(120, TransactionType.Policy_Change),"Policy change charge havent made to BC");
		acctMenu.clickAccountMenuInvoices();
		AccountInvoices acctInvoices = new AccountInvoices(driver);
		
		invoiceDueDate = acctInvoices.getInvoiceDueDateByInvoiceType(InvoiceType.Shortage);
		invoiceBillDate = acctInvoices.getInvoiceDateByInvoiceType(InvoiceType.Shortage);
		dayAfterInvoiceDueDate = DateUtils.dateAddSubtract(invoiceDueDate, DateAddSubtractOptions.Day, 1);
		
		//Make Invoice Billed 
		ClockUtils.setCurrentDates(driver, invoiceBillDate);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
		//Make Invoice Due
		ClockUtils.setCurrentDates(driver, dayAfterInvoiceDueDate);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);
		//Verify if Lien Invoice Went Due
		acctMenu.clickBCMenuCharges();
		acctMenu.clickAccountMenuInvoices();
				
		Assert.assertTrue(acctInvoices.verifyInvoice(InvoiceType.Shortage, InvoiceStatus.Due, changeInPremiumAfterPolicyChange), "Lien Invoice didn't go due");
		
		BCSearchAccounts accountSearchBC = new BCSearchAccounts(driver);
        accountSearchBC.searchAccountByAccountNumber(myPolicyObj.accountNumber);
		
        acctMenu.clickBCMenuDelinquencies();
		acctDelinquency = new BCCommonDelinquencies(driver);

        Assert.assertTrue(acctDelinquency.verifyDelinquencyByReason(OpenClosed.Open,DelinquencyReason.PastDueLienPartialCancel,null, dayAfterInvoiceDueDate), "Delinquency does not exist Which is not Expected");
	}

}

package regression.r2.clock.billingcenter.delinquency.testdelinquencies.lienholderfullcancel;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.common.BCCommonDelinquencies;
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
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GeneratePolicyHelper;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author sgunda
* @Description   US13897 - Insurance policy w/ Membership line Delinquency: Research and GUnits
* @DATE March 21, 2018
*/

public class TestDelinquencyOnDownInvoiceForFullyLienBilledPolicy extends BaseTest{
	
	private GeneratePolicy myPolicyObj;
	private ARUsers arUser = new ARUsers();
	
	private String lienholderNumber = null;
	private double lienholderLoanPremiumAmount;
	
	private BCAccountMenu acctMenu;
	private BCCommonDelinquencies  acctDelinquency;
	private Date dayAfterInvoiceDueDate;
	private WebDriver driver;

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
		this.lienholderLoanPremiumAmount = this.myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();
	}
	
	@Test(dependsOnMethods = { "squirePolicy" })
	public void MakeInvoiceBilledAndDue() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienholderNumber);
		
		Date invoiceDueDate = DateUtils.dateAddSubtract(new GuidewireHelpers(driver).getPolicyEffectiveDate(myPolicyObj), DateAddSubtractOptions.Month, 1);
		Date invoiceBillDate = DateUtils.dateAddSubtract(invoiceDueDate, DateAddSubtractOptions.Day, -20);
		dayAfterInvoiceDueDate = DateUtils.dateAddSubtract(invoiceDueDate, DateAddSubtractOptions.Day, 1);
		
		//Make Invoice Billed 
		ClockUtils.setCurrentDates(driver, invoiceBillDate);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
		
		//Make Invoice Due
		ClockUtils.setCurrentDates(driver, dayAfterInvoiceDueDate);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);
		
		//Verify if Lien Invoice Went Due
		acctMenu= new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
		AccountInvoices acctInvoices = new AccountInvoices(driver);
		Assert.assertTrue(acctInvoices.verifyInvoice(InvoiceType.NewBusinessDownPayment, InvoiceStatus.Due, this.lienholderLoanPremiumAmount), "Lien Invoice didn't go due");
				
	}

	@Test(dependsOnMethods = { "MakeInvoiceBilledAndDue" })
	public void verifyIfDelinquencyTriggeredAfterInvoiceGoingDue() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(),this.myPolicyObj.accountNumber);
		acctMenu=new BCAccountMenu(driver);
		acctMenu.clickBCMenuDelinquencies();
		acctDelinquency = new BCCommonDelinquencies(driver);

        Assert.assertTrue(acctDelinquency.verifyDelinquencyByReason(OpenClosed.Open,DelinquencyReason.PastDueLienPartialCancel,null, dayAfterInvoiceDueDate), "Delinquency does not exist Which is not Expected");
	}

}

package coreBusiness.delinquencies.lienholderfullcancel;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
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

public class TestDelinquencyOnDownInvoiceForFullyLienBilledPolicy extends BaseTest{
	
	private GeneratePolicy myPolicyObj;
	private ARUsers arUser = new ARUsers();
	
	private String lienholderNumber = null;
	private double lienholderLoanPremiumAmount;
	
	private BCAccountMenu acctMenu;
	private BCCommonDelinquencies  acctDelinquency;
	private Date dayAfterInvoiceDueDate;
	private WebDriver driver;


	private void gotoAccount(String accountNumber){
		BCSearchAccounts search= new BCSearchAccounts(driver);
		search.searchAccountByAccountNumber(accountNumber);
	}

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
		driver.get(cf.getUrlOfCenter(ApplicationOrCenter.PolicyCenter));


		myPolicyObj = new GeneratePolicyHelper(driver).generateFullyLienBilledSquirePLPolicy("US13897", "PL",null, repository.gw.enums.PaymentPlanType.Annual , repository.gw.enums.PaymentType.ACH_EFT, generateContacts);

		this.lienholderNumber = this.myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
		this.lienholderLoanPremiumAmount = this.myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();
	}
	
	@Test(dependsOnMethods = { "squirePolicy" })
	public void MakeInvoiceBilledAndDue() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienholderNumber);
		
		Date invoiceDueDate = repository.gw.helpers.DateUtils.dateAddSubtract(new GuidewireHelpers(driver).getPolicyEffectiveDate(myPolicyObj), repository.gw.enums.DateAddSubtractOptions.Month, 1);
		Date invoiceBillDate = repository.gw.helpers.DateUtils.dateAddSubtract(invoiceDueDate, repository.gw.enums.DateAddSubtractOptions.Day, -20);
		dayAfterInvoiceDueDate = DateUtils.dateAddSubtract(invoiceDueDate, repository.gw.enums.DateAddSubtractOptions.Day, 1);
		
		//Make Invoice Billed 
		repository.gw.helpers.ClockUtils.setCurrentDates(driver, invoiceBillDate);
		new BatchHelpers(driver).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);
		
		//Make Invoice Due
		repository.gw.helpers.ClockUtils.setCurrentDates(driver, dayAfterInvoiceDueDate);
		new BatchHelpers(driver).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);
		
		//Verify if Lien Invoice Went Due
		acctMenu= new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
		AccountInvoices acctInvoices = new AccountInvoices(driver);
		Assert.assertTrue(acctInvoices.verifyInvoice(repository.gw.enums.InvoiceType.NewBusinessDownPayment, repository.gw.enums.InvoiceStatus.Due, this.lienholderLoanPremiumAmount), "Lien Invoice didn't go due");

		gotoAccount(myPolicyObj.accountNumber);

		acctMenu=new BCAccountMenu(driver);
		acctMenu.clickBCMenuDelinquencies();
		acctDelinquency = new BCCommonDelinquencies(driver);

        Assert.assertTrue(acctDelinquency.verifyDelinquencyByReason(repository.gw.enums.OpenClosed.Open, repository.gw.enums.DelinquencyReason.PastDueLienPartialCancel, null,dayAfterInvoiceDueDate), "Delinquency does not exist Which is not Expected");
	}

}

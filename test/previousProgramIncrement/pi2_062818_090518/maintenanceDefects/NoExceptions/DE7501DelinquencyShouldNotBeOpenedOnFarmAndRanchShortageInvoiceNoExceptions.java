package previousProgramIncrement.pi2_062818_090518.maintenanceDefects.NoExceptions;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonCharges;
import repository.bc.common.BCCommonDelinquencies;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GeneratePolicyHelper;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

import java.util.Date;

/**
* @Author sgunda
* @Requirement
* @RequirementsLink <a href="https://rally1.rallydev.com/#/203558469364d/detail/defect/219810730632">DE7501 Delinquency triggered on Farm and Ranch shortage invoice</a>
* @DATE Apr 28, 2018
*/

@Test(groups = {"ClockMove"})
public class DE7501DelinquencyShouldNotBeOpenedOnFarmAndRanchShortageInvoiceNoExceptions extends BaseTest {
	
	private WebDriver driver;
	public GeneratePolicy myPolicyObj;
    private ARUsers arUser = new ARUsers();
	private BCAccountMenu acctMenu;
	private AccountInvoices acctInvoice;
	private Date policyChangeDate,invoiceDate,invoiceDueDate;

    @Test
    public void generateFarmAndRanch() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		myPolicyObj = new GeneratePolicyHelper(driver).generateFarmAndRanchPolicy("DE7501", "FarmAndRanch",null,PaymentPlanType.Annual,PaymentType.Cash);
        driver.quit();
    }
    
    @Test(dependsOnMethods = { "generateFarmAndRanch" })	
	public void payDownPayment() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
        directPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.squire.getPremium().getDownPaymentAmount(), myPolicyObj.squire.getPolicyNumber()); 
        
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		policyChangeDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver,ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, 310);

		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
        ClockUtils.setCurrentDates(driver, policyChangeDate);
	}
    
    @Test(dependsOnMethods = { "payDownPayment" })
   	public void doAPolicyChangeToCreateShortage() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchPolicy_asUW(myPolicyObj);
        StartPolicyChange policyChange = new StartPolicyChange(driver);
		policyChange.changePLPropertyCoverage(160000.0, policyChangeDate);
    }

    @Test(dependsOnMethods = { "doAPolicyChangeToCreateShortage" })	
   	public void makeShortageBilledAndDue() throws Exception {	
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        acctMenu = new BCAccountMenu(driver);
	     acctMenu.clickBCMenuCharges();
        BCCommonCharges charges = new BCCommonCharges(driver);
	     Assert.assertTrue(charges.waitUntilChargesFromPolicyContextArrive(TransactionType.Policy_Change), "Policy Change charges didn't make it to BC, Test can not continue");

	     acctMenu.clickAccountMenuInvoices();

        acctInvoice = new AccountInvoices(driver);
	     invoiceDate = acctInvoice.getInvoiceDateByInvoiceType(InvoiceType.Shortage);
	     invoiceDueDate = acctInvoice.getInvoiceDueDateByInvoiceType(InvoiceType.Shortage);
	     
	     ClockUtils.setCurrentDates(driver, invoiceDate);
	     new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 13);
    }
	
    @Test(dependsOnMethods = {"makeShortageBilledAndDue"})
	public void issueRenewal() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new StartRenewal(driver).loginAsUWAndIssueRenewal(myPolicyObj);

	}
  
    @Test(dependsOnMethods = { "issueRenewal" })
    public void checkIfThereIsAnOpenDelinquency() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);

		acctMenu.clickBCMenuCharges();

		BCCommonCharges charges = new BCCommonCharges(driver);
		Assert.assertTrue(charges.waitUntilChargesFromPolicyContextArrive(TransactionType.Renewal), "Renewal charges didn't make it to BC, Test can not continue");

		ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(invoiceDueDate, DateAddSubtractOptions.Day, 1));
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		acctMenu.clickAccountMenuInvoices();

		acctInvoice = new AccountInvoices(driver);
		Assert.assertTrue(acctInvoice.verifyInvoice(InvoiceType.Shortage, InvoiceStatus.Due, null), "Invoice did not go Due, Test can not continue");

		acctMenu.clickBCMenuDelinquencies();
		BCCommonDelinquencies delinquencies = new BCCommonDelinquencies(driver);
		Assert.assertFalse(delinquencies.verifyDelinquencyExists(null, null, myPolicyObj.accountNumber, null, myPolicyObj.squire.getPolicyNumber(), null, null, null),"Delinquency should not trigger for a Farm and Ranch shortage, Test can not continue");
  }

}

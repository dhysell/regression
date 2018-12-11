package regression.r2.clock.billingcenter.invoicing;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.BankAccountInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
/**
* @Author JQU
* @Requirement US11486-When a policy term is shortened the shortage amount needs to be billed is a shortage bill
* 				Current code/requirements take the shortage amount and add it to the next scheduled invoice.  
* 				This needs to be billed in a shortage bill if there is time.  See email attachment for additional detail.
* 
* @DATE August 25, 2017*/
public class InvoicingAfterTermShortendedTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;
	private ARUsers arUser = new ARUsers();	
	private double invoiceAmount;
	private int daysToShorten=10;
	private String policyChangeDescription="Shorten Term";
	
	@Test
    public void generatePolicy() throws Exception {
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		locOneBuildingList.add(new PolicyLocationBuilding());
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));		
		BankAccountInfo bankAccountInfo = new BankAccountInfo();
		bankAccountInfo.setAccountNumber("535");
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        myPolicyObj = new GeneratePolicy.Builder(driver)
			.withInsPersonOrCompany(ContactSubType.Company)
			.withInsCompanyName("ShortenTermTest")
			.withPolOrgType(OrganizationType.Partnership)
			.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
			.withPolicyLocations(locationsList)
			.withBankAccountInfo(bankAccountInfo)
			.withPaymentPlanType(PaymentPlanType.Quarterly)
			.withDownPaymentType(PaymentType.Cash)
			.build(GeneratePolicyType.PolicyIssued);	
	}
	@Test(dependsOnMethods = { "generatePolicy" })
	public void payInsuredAndLH() throws Exception {				
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
        AccountInvoices invoicePage = new AccountInvoices(driver);
		invoiceAmount=invoicePage.getInvoiceAmountByRowNumber(2);
		//pay down payment
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), myPolicyObj.busOwnLine.getPolicyNumber());
		ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, 1));
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		//move clock to one week before next Invoice date
		Date secondDueDate=DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Month, 3);
		ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(secondDueDate, DateAddSubtractOptions.Day, -(myPolicyObj.paymentPlanType.getInvoicingLeadTime()+daysToShorten)));
	}
	@Test(dependsOnMethods = { "payInsuredAndLH" })
    public void shortenTermBy10Days() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        StartPolicyChange policyChange = new StartPolicyChange(driver);
        policyChange.changeExpirationDate(DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getExpirationDate(), DateAddSubtractOptions.Day, -10), policyChangeDescription);
	}
	@Test(dependsOnMethods = { "shortenTermBy10Days" })
	public void verifyInvoicing() throws Exception {				
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
		//wait for policy change come to BC
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
        AccountCharges chargePage = new AccountCharges(driver);
		chargePage.waitUntilChargesFromPolicyContextArrive(120, TransactionType.Policy_Change);
		double policyChangeAmt=chargePage.getChargeAmount(myPolicyObj.accountNumber, policyChangeDescription);
		//verify new invoices
		acctMenu.clickAccountMenuInvoices();
        AccountInvoices invoicePage = new AccountInvoices(driver);
        Date newInvoiceDueDate = DateUtils.dateAddSubtract(DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getEffectiveDate(), DateAddSubtractOptions.Month, 3), DateAddSubtractOptions.Day, -daysToShorten);
		Date newInvoiceDate=DateUtils.dateAddSubtract(newInvoiceDueDate, DateAddSubtractOptions.Day, myPolicyObj.paymentPlanType.getInvoicingLeadTime());
		invoicePage.verifyInvoice(newInvoiceDate, newInvoiceDueDate, null, InvoiceType.Scheduled, null, InvoiceStatus.Planned, invoiceAmount, invoiceAmount);
		invoicePage.verifyInvoice(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, InvoiceType.Shortage, null, InvoiceStatus.Planned, policyChangeAmt, policyChangeAmt);
	}
}
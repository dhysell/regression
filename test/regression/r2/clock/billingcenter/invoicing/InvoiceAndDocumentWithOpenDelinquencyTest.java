package regression.r2.clock.billingcenter.invoicing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.common.BCCommonDocuments;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.DocumentType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
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
* @Author jqu
* @Description DE3891: Printing MNOW during open delinquency. There is an open insured partial cancel delinquency on the insured that has not yet been cancelled. 
* @The policy is monthly. Each month we carry forward the invoice that should go billed because of the open delinquency (CORRECT). 
* @We then print a monthly notice of withdrawal each month. This is incorrect because we cannot send regular invoicing document while there is an open delinquency.
* @DATE August 18, 2016
*/
public class InvoiceAndDocumentWithOpenDelinquencyTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;	
	private ARUsers arUser = new ARUsers();
	private BCAccountMenu acctMenu;	
	private String MNOW="Monthly Notice of Withdrawal";
	private float bppAndBldgLimit=200000;
	private List<Date> bcInvDate;
	
	@Test
	public void generate() throws Exception {
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();	
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();	
		loc1Bldg1.setBppLimit(bppAndBldgLimit);
		loc1Bldg1.setBuildingLimit(bppAndBldgLimit);
		locOneBuildingList.add(loc1Bldg1);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));				

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver).withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("FreeFormBox")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Monthly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);			
	}
	//not pay down payment, move clock and release TT to trigger the delinquency
	@Test(dependsOnMethods = { "generate" })	
		public void payInsuredAndLHWithExtraAmount() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
        AccountInvoices invoice = new AccountInvoices(driver);
		bcInvDate = invoice.getListOfInvoiceDates();
		List<Double> invoiceAmt = invoice.getListOfInvoiceAmounts();		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, 11));
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Release_Trouble_Ticket_Holds);
		//verify the delinquency		
		acctMenu.clickBCMenuDelinquencies();
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		try{
			delinquency.getDelinquencyTableRow(OpenClosed.Open, DelinquencyReason.PastDueFullCancel, myPolicyObj.accountNumber, null, myPolicyObj.accountNumber, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, null);
		}catch(Exception e){
			Assert.fail("Doesn't find the delinquency.");
		}
		//move to next invoice, verify carry forward when running invoice; verify no MNOW doc is triggered
		ClockUtils.setCurrentDates(cf, bcInvDate.get(1));
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		acctMenu.clickAccountMenuInvoices();
        try {//the second invoice should be carried forward to the third one
			invoice.getAccountInvoiceTableRow(bcInvDate.get(2), null, null, InvoiceType.Scheduled, null, InvoiceStatus.Planned, invoiceAmt.get(1)+invoiceAmt.get(2), invoiceAmt.get(1)+invoiceAmt.get(2));
		}catch(Exception e){
			Assert.fail("the invoice is not carried forward.");
		}
		//verify MNOW is not triggered with open delinquency
		acctMenu.clickBCMenuDocuments();
        BCCommonDocuments document = new BCCommonDocuments(driver);
		try{
			document.getDocumentsTableRow(MNOW, DocumentType.Notice_Of_Withdrawal, null, null, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter));
			Assert.fail("the shouldn't be triggered with open delinquency.");
		}catch(Exception e){
			getQALogger().info("The MNOW is not trigger which is expected.");
		}
		ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, 3));
	}
	@Test(dependsOnMethods = { "payInsuredAndLHWithExtraAmount" })
    public void makePolicyChangeToReduceCoverage() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.busOwnLine.getPolicyNumber());
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.changeBuildingCoverage(1, bppAndBldgLimit-100000, bppAndBldgLimit-100000, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter));		
	}
	@Test(dependsOnMethods = { "makePolicyChangeToReduceCoverage" })
    public void verifyCarryForwardAndDocAfterPolicyChange() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		//wait for the policy change to come
		acctMenu.clickBCMenuCharges();
        AccountCharges charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(120, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), TransactionType.Policy_Change, this.myPolicyObj.accountNumber);
		//verify the carry forward
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);//run invoice to apply the credit
		ClockUtils.setCurrentDates(cf, bcInvDate.get(2));
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		/////////////////
		//This space is for DE3936 which the developer is working on
		//
		//verify MNOW is not triggered with open delinquency
		acctMenu.clickBCMenuDocuments();
        BCCommonDocuments document = new BCCommonDocuments(driver);
		try{
			document.getDocumentsTableRow(MNOW, DocumentType.Notice_Of_Withdrawal, null, null, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter));
			Assert.fail("the shouldn't be triggered with open delinquency.");
		}catch(Exception e){
			getQALogger().info("The MNOW is not trigger which is expected.");
		}		
	}
}

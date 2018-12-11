package regression.r2.clock.billingcenter.invoicing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonTroubleTickets;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author jqu
 * @Description DE3688: When invoice (regular invoice, not down payment) is scheduled during Promised Payment Trouble Ticket hold period and the payment <90% of the amount due (total promised payment),
 * @						on the Invoice Date it should be carried forward to next month when Invoice batch process is ran. We should keep the original invoice with a status of "carried forward".*
 * @DATE August 22, 2016
 */
@QuarantineClass
public class InvoiceInHoldPeriodCarryForwardTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;	
	private ARUsers arUser = new ARUsers();
	private BCAccountMenu acctMenu;	
	
	@AfterMethod(alwaysRun = true)
	public void afterMethod() throws Exception {

	}
	@Test
	public void generate() throws Exception {
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();	
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();			
		locOneBuildingList.add(loc1Bldg1);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));				

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver).withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("CarryForwardTest")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				//create a irregular policy so the second invoice date can fall in the TT hold period
				//there will be two invoices in the 10 day hold period
				.withPolTermLengthDays(120+PaymentPlanType.Monthly.getInvoicingLeadTime()+3)
				.withPaymentPlanType(PaymentPlanType.Monthly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);			
	}
	//there are two invoices in the hold period -- Down Payment and the second Scheduled Invoice
	//Pay the two invoices with some amount < 90% totally
	@Test(dependsOnMethods = { "generate" })
	public void payInsuredAndNextInvoice() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        acctMenu = new BCAccountMenu(driver);
		//wait for the trouble ticket
		acctMenu.clickBCMenuTroubleTickets();
        BCCommonTroubleTickets troubleTicket = new BCCommonTroubleTickets(driver);
		troubleTicket.waitForTroubleTicketsToArrive(60);
		//get invoice amount/date lists and make payment
		acctMenu.clickAccountMenuInvoices();
        AccountInvoices invoice = new AccountInvoices(driver);
		List<Double> invoiceList=invoice.getListOfInvoiceAmounts();
		List<Date> invoiceDate=invoice.getListOfInvoiceDates();
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directBillPayment = new NewDirectBillPayment(driver);
        directBillPayment.makeDirectBillPaymentExecute(invoiceList.get(0), myPolicyObj.busOwnLine.getPolicyNumber());
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
		double paymentAmt = NumberUtils.round(invoiceList.get(1)*0.3, 2);
        directBillPayment.makeDirectBillPaymentExecuteWithoutDistribution(paymentAmt, myPolicyObj.busOwnLine.getPolicyNumber());
		//move clock and verify the carry Forward
		ClockUtils.setCurrentDates(cf, invoiceDate.get(1));
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		acctMenu.clickBCMenuSummary();
        acctMenu.clickAccountMenuInvoices();
		try{
			invoice.getAccountInvoiceTableRow(invoiceDate.get(2), null, null, InvoiceType.Scheduled, null, InvoiceStatus.Planned, invoiceList.get(1)+invoiceList.get(2), invoiceList.get(1)+invoiceList.get(2)- NumberUtils.round(invoiceList.get(1)*0.3, 2));
		}catch(Exception e){
			Assert.fail("Carry Forward is not correct.");
		}		
	}

}

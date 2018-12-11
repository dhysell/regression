package regression.r2.noclock.billingcenter.invoicing;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.idfbins.enums.OkCancel;

import repository.bc.account.AccountCreateNewInvoice;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
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
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
/**
* @Author jqu
* @Description (US5491) it tests Account/Invoices/Create New Invoice button, Account/Invoices/Delete Invoices(s) button, Account/Invoices/Resend Invoice, and Account/Invoices/Change 				Invoice Dates (for planned invoices)
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/18%20-%20UI%20-%20Account%20Screens/18-04%20%20Account%20Invoices%20Screen.docx"></a>
* @DATE February 9, 2016
*/
public class ManageInvoicesTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();
	private BCAccountMenu acctMenu;
	private AccountInvoices acctInvoice;

	@Test
	public void generate() throws Exception {
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		locOneBuildingList.add(new PolicyLocationBuilding());
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        this.myPolicyObj = new GeneratePolicy.Builder(driver).withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("PaymentRestrictionAndCashonlyRollup")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Monthly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}
	@Test(dependsOnMethods = { "generate" })	
	public void testButtonsOnInvoiceScreen() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		Date currentDate=DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager,	ARCompany.Commercial);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
        acctInvoice = new AccountInvoices(driver);
		//test Create New Invoice 
		acctInvoice.setInvoiceStream(myPolicyObj.accountNumber);		
		acctInvoice.clickCreateNewInvoice();
		AccountCreateNewInvoice newInv = new AccountCreateNewInvoice(driver);
		newInv.clickUpdate();
		WebElement theRow = null;
		try{
			theRow=acctInvoice.getAccountInvoiceTableRow(currentDate, DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 15), null, InvoiceType.Scheduled, null, null, 0.0, 0.0);
		}catch(Exception e){
			Assert.fail("didn't find the new created invoice.");
		}
		//test Change Invoice Dates
		theRow.click();
		acctInvoice.clickChangeInvoiceDateButton();
		//change invoice due date, Invoice Date will update automatically
		Date newDate=DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 20);
		acctInvoice.changeDueDateTo(newDate);
		acctInvoice.clickUpdate();
		try {
			theRow=acctInvoice.getAccountInvoiceTableRow(DateUtils.dateAddSubtract(newDate, DateAddSubtractOptions.Day, -20), newDate, null, InvoiceType.Scheduled, null, null, 0.0, 0.0);
		}catch(Exception e){
			Assert.fail("didn't find the invoice after Invoice/Due dates change.");
		}
		//test Delete Invoice
		new TableUtils(driver).setCheckboxInTable(acctInvoice.getAccountInvoicesTable(), new TableUtils(driver).getRowNumberFromWebElementRow(theRow), true);
		acctInvoice.clickDeleteInvoice();
		new GuidewireHelpers(driver).selectOKOrCancelFromPopup(OkCancel.OK);
		//test resend Billed Invoice
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		acctMenu.clickBCMenuSummary();
		acctMenu.clickAccountMenuInvoices();
		theRow = acctInvoice.getAccountInvoiceTableRow(currentDate, currentDate, null, InvoiceType.NewBusinessDownPayment, null, InvoiceStatus.Billed, null, null);
		theRow.click();
		try {
			acctInvoice.clickResendInvoice();
		}catch(Exception e){
			Assert.fail("didn't resend the invoice successfully.");
		}		
	}
}

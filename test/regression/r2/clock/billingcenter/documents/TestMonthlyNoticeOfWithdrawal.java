package regression.r2.clock.billingcenter.documents;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDocuments;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DocumentType;
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
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.gw.servertools.ServerToolsBatchProcessInfo;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author bhiltbrand
 * @Requirement A new monthly notice of withdrawal must print every time the amount or due date changes. This was not happening in Production.
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/7832667974d/detail/defect/45135018440">Rally Defect DE2978</a>
 * @DATE Nov 2, 2015
 */
public class TestMonthlyNoticeOfWithdrawal extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();
	private Date originalRegularInvoiceDueDate = null;
	private Date originalRegularInvoiceInvoiceDate = null;
	
	@Test
    public void generatePolicy() throws Exception {
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		locOneBuildingList.add(new PolicyLocationBuilding());
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
			.withCreateNew(CreateNew.Create_New_Always)
			.withInsPersonOrCompany(ContactSubType.Company)
			.withInsCompanyName("Insured Policy")
			.withPolOrgType(OrganizationType.Partnership)
			.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
			.withPolicyLocations(locationsList)
			.withPaymentPlanType(PaymentPlanType.Monthly)
			.withDownPaymentType(PaymentType.Cash)
			.build(GeneratePolicyType.PolicyIssued);
        System.out.println(myPolicyObj.busOwnLine.getPolicyNumber());
	}
	
	@Test (dependsOnMethods = { "generatePolicy" })
	public void payDownpaymentAndMoveClocks() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), myPolicyObj.busOwnLine.getPolicyNumber());

        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuInvoices();
        AccountInvoices acctInvoice = new AccountInvoices(driver);

        ClockUtils.setCurrentDates(cf, acctInvoice.getListOfInvoiceDates().get(1));
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        accountMenu.clickBCMenuDocuments();

        {//Verify if MNOW is created before the delinqency
            BCCommonDocuments documents = new BCCommonDocuments(driver);
            Assert.assertTrue(documents.verifyDocument("Monthly Notice of Withdrawal", DocumentType.Monthly_Notice_Of_Withdrawal, null, null, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null), "MNOW not created ");
        }
        accountMenu.clickBCMenuDocuments();

        ClockUtils.setCurrentDates(cf, acctInvoice.getListOfDueDates().get(1));

        { //This helps paying invoice with Payment Request
            new BatchHelpers(cf).runBatchProcess(BatchProcess.Payment_Request);
            new BatchHelpers(cf).runBatchProcess(BatchProcess.Payment_Request);
            new BatchHelpers(cf).runBatchProcess(BatchProcess.Payment_Request);
        }

        accountMenu.clickBCMenuSummary();
        accountMenu.clickAccountMenuInvoices();
        Assert.assertTrue(acctInvoice.verifyInvoice(acctInvoice.getListOfInvoiceDates().get(1), acctInvoice.getListOfDueDates().get(1), null, InvoiceType.Scheduled, null, InvoiceStatus.Billed, null, 0.00), "Failed to pay with payment request");

        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "payDownpaymentAndMoveClocks" })
    public void policyChangeToGenerateCredit() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

        StartPolicyChange policyChange = new StartPolicyChange(driver);
        policyChange.changeBuildingCoverage(1, 400000, 400000);
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "policyChangeToGenerateCredit" })
	public void verifyNoticeOfWithdrawalInBC() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);
		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuInvoices();

        AccountInvoices accountInvoices = new AccountInvoices(driver);
		double invoiceDueAmount = accountInvoices.getInvoiceDueAmountByDueDate(DateUtils.dateAddSubtract(this.originalRegularInvoiceDueDate, DateAddSubtractOptions.Month, 1));
		
		ServerToolsBatchProcessInfo batchProcess = new ServerToolsBatchProcessInfo(driver);
		Date newInvoiceDueDate = DateUtils.dateAddSubtract(this.originalRegularInvoiceDueDate, DateAddSubtractOptions.Month, 1);
		Date newInvoiceDate = DateUtils.dateAddSubtract(newInvoiceDueDate, DateAddSubtractOptions.Day, -15);
		batchProcess.runPaymentRequest(newInvoiceDate, newInvoiceDueDate, null, invoiceDueAmount);
		
		ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(this.originalRegularInvoiceInvoiceDate, DateAddSubtractOptions.Month, 2));		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickBCMenuDocuments();

        BCCommonDocuments documents = new BCCommonDocuments(driver);
		boolean noticeOfWithdrawalExists = documents.verifyDocument(DateUtils.dateAddSubtract(this.originalRegularInvoiceInvoiceDate, DateAddSubtractOptions.Month, 2), "Monthly Notice of Withdrawal");
		
		if (!noticeOfWithdrawalExists) {
			Assert.fail("The Final Notice of Withdrawal was not created as expected. Test failed.");
		}
		
		new GuidewireHelpers(driver).logout();
	}
}

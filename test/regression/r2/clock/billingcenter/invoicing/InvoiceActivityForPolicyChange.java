package regression.r2.clock.billingcenter.invoicing;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
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
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author sgunda
 * @Requirement DE4829 Created Policy change, invoicing split into scheduled then shortage
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/41432362343d/detail/defect/89955545848">Link Text</a>
 * @DATE Mar 17, 2017
 */
public class InvoiceActivityForPolicyChange extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObj;
	private double costChg;
	private ARUsers arUser = new ARUsers();
	private BCAccountMenu acctMenu;
	private AccountInvoices acctInvoice;
	private Date invoiceDate;
	
	@Test
	public void generate() throws Exception {
		//generate a policy with monthly payment plan
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		locOneBuildingList.add(new PolicyLocationBuilding());
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("DE4406")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)			
				.withPaymentPlanType(PaymentPlanType.Quarterly)			
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
        getQALogger().info(new GuidewireHelpers(driver).getCurrentPolicyType(myPolicyObj).toString());
	}
	
	@Test(dependsOnMethods = { "generate" })	
	public void makePayment() throws Exception {
		
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
        NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
        directPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), myPolicyObj.busOwnLine.getPolicyNumber());
		
	}
	
	@Test(dependsOnMethods = {"makePayment"})
    public void moveClock() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

	// navigating to invoice page and moving the clock to next planned invoice date  
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
        acctInvoice = new AccountInvoices(driver);
		invoiceDate = acctInvoice.getListOfInvoiceDates().get(1);
		
		System.out.println(invoiceDate);
        ClockUtils.setCurrentDates(cf, invoiceDate);
		
		//run Invoice and Invoice due to make next invoice billed 
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
			
	}
	
			
	@Test(dependsOnMethods = { "moveClock" })
    public void increaseCoverage() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.busOwnLine.getPolicyNumber());
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.changeBuildingCoverage(1, 350000, 350000);
        GenericWorkorderComplete complete = new GenericWorkorderComplete(driver);
		complete.clickViewYourPolicy();
        PolicySummary pcSum = new PolicySummary(driver);
		costChg=pcSum.getTransactionPremium(null, "change coverage");
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		System.out.println("The cost change after policy change is  :"+costChg);
		
	}
	
	@Test (dependsOnMethods = { "increaseCoverage" })
    public void verifyInvoiceTypeUsingDate() throws Exception {
			//verify invoices on billing center
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
			new Login(driver).loginAndSearchAccountByAccountNumber("su	", "gw", myPolicyObj.accountNumber);

        acctMenu = new BCAccountMenu(driver);
			acctMenu.clickAccountMenuInvoices();
        AccountInvoices tableData = new AccountInvoices(driver);
			WebElement myInvoiceTable= tableData.getAccountInvoicesTable();

        WebElement invoiceTableRow = new TableUtils(driver).getRowInTableByColumnNameAndValue(myInvoiceTable, "Due Date", DateUtils.dateFormatAsString("MM/dd/yyyy", DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getEffectiveDate(), DateAddSubtractOptions.Month, 4)));
			if (invoiceTableRow != null) {
				String invoiceType = new TableUtils(driver).getCellTextInTableByRowAndColumnName(myInvoiceTable,
						new TableUtils(driver).getRowNumberFromWebElementRow(invoiceTableRow), "Invoice Type");
				System.out.println( "InvoiceType is " + invoiceType);
				Assert.assertEquals(invoiceType, "Shortage", "Invoice Type  is Not  Shortage");
				
			}
			else {
				Assert.fail("Row not found ");
			}
			
		}
	

}

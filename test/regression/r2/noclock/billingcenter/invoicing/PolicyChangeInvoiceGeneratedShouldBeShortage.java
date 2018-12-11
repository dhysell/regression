package regression.r2.noclock.billingcenter.invoicing;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.DateUtils;
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
* @Requirement DE4406 Negative Policy Change invoice for future renewal term is generated as "Scheduled" Invoice instead of "Shortage"
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/11%20-%20Installment%20Scheduling/11-07%20Policy%20Change%20Installment%20Scheduling.docx">Link Text</a>
* @Description When a negative policy change comes in for future renewal term after renewal down invoice has been billed, the invoice with negative charge which created to be billed and due on policy effective date is 
* 				created as "Scheduled" instead of "Shortage".It should be a shortage invoice.
* @DATE Mar 15, 2017
*/
public class PolicyChangeInvoiceGeneratedShouldBeShortage extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObj;
	private double costChg;
	private ARUsers arUser = new ARUsers();
	private BCAccountMenu acctMenu;
	
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
		
	@Test(dependsOnMethods = { "makePayment" })
    public void reduceInsuredCoverage() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.busOwnLine.getPolicyNumber());
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.changeBuildingCoverage(1, 100000, 100000);
        GenericWorkorderComplete complete = new GenericWorkorderComplete(driver);
		complete.clickViewYourPolicy();
		PolicySummary pcSum = new PolicySummary(driver);
		costChg=pcSum.getTransactionPremium(null, "change coverage");
		System.out.println("The cost change after policy change is  :" + costChg);
		
	}
	
	@Test (dependsOnMethods = { "reduceInsuredCoverage" })
		public void verifyInvoices() throws Exception {
			//verify invoices on billing center		
			this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
			Config cf = new Config(ApplicationOrCenter.BillingCenter);
			driver = buildDriver(cf);
			new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        acctMenu = new BCAccountMenu(driver);
			acctMenu.clickBCMenuCharges();
		AccountCharges charge = new AccountCharges(driver);
			charge.waitUntilChargesFromPolicyContextArrive(60, TransactionType.Policy_Change);
			acctMenu.clickAccountMenuInvoices();
        AccountInvoices invoice = new AccountInvoices(driver);
			Date billAndDueDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
			double invoiceCredit = costChg/4;			
			
			Assert.assertTrue((invoice.verifyInvoice(billAndDueDate, billAndDueDate, null, InvoiceType.Shortage, null, null, invoiceCredit, null)), "Didn't find the shortage");
			
		}
	

}

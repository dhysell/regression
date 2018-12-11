package regression.r2.noclock.billingcenter.invoicing;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.AccountPolicies;
import repository.bc.account.BCAccountMenu;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.policy.PolicyPaymentSchedule;
import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PolicyTermStatus;
import repository.gw.enums.RenewalCode;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

//import pc.policy.PolicyMenu;
/**
 * @Author JQu
 * @Description DE5737--Changing payment plan to monthly prior to renewal invoice date changes the lead time
 * Renewal day 50
 * Changed payment schedule from annual to monthly
 * Invoice dates changed from 06/09 due 06/29
 * 06/15 due 06/30 giving only 15 day lead time on a renewal.
 * <p>
 * Expect:  Monthly renewal invoices should give insured 20 day lead time
 * @Requirement
 * @DATE June 16, 2017
 */
public class ChangePmtPlanToMonthlyChangeRenewalDownLeadTime extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;	
	private ARUsers arUser = new ARUsers();
	private Date currentTermExpDate;
	
	@Test
	public void generate() throws Exception {			
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		locOneBuildingList.add(new PolicyLocationBuilding());
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));		
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
			.withInsPersonOrCompany(ContactSubType.Company)
			.withInsCompanyName("PmtScheduleChangeAfterRenewal")			
			.withPolOrgType(OrganizationType.Partnership)
			.withPolTermLengthDays(50)
			.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
			.withPolicyLocations(locationsList)						
			.withDownPaymentType(PaymentType.Cash)
			.withPaymentPlanType(PaymentPlanType.Annual)
			.build(GeneratePolicyType.PolicyIssued);
        currentTermExpDate = this.myPolicyObj.busOwnLine.getExpirationDate();
	}
	@Test(dependsOnMethods = { "generate" })
	public void renewalPolicyInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.busOwnLine.getPolicyNumber());
		StartRenewal manualRenewal = new StartRenewal(driver);
		manualRenewal.renewPolicyManually(RenewalCode.Renew_Good_Risk, myPolicyObj);	
		new GuidewireHelpers(driver).logout();	
	}
	@Test(dependsOnMethods = { "renewalPolicyInPolicyCenter" })
		public void verifyRenewalDownPayment() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		//wait for Renewal to come
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
		AccountCharges charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArrive(60, TransactionType.Renewal);
		//verify the Renewal Down Payment
		acctMenu.clickAccountMenuInvoices();
        AccountInvoices invoice = new AccountInvoices(driver);
		invoice.verifyInvoice(DateUtils.dateAddSubtract(currentTermExpDate, DateAddSubtractOptions.Day, -21), DateUtils.dateAddSubtract(currentTermExpDate, DateAddSubtractOptions.Day, -1), null, InvoiceType.RenewalDownPayment, null, InvoiceStatus.Planned, null, null);
		//change Payment Plan type
		acctMenu.clickAccountMenuPolicies();
        AccountPolicies acctPolicy = new AccountPolicies(driver);
		acctPolicy.clickPolicyNumberInPolicyTableRow(null, null, null, null, null, PolicyTermStatus.Future, null, null, null, null);
		BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
		policyMenu.clickPaymentSchedule();
		PolicyPaymentSchedule schedule = new PolicyPaymentSchedule(driver);
		schedule.clickEditSchedule();
		schedule.selectNewPaymentPlan(PaymentPlanType.Monthly);
		schedule.clickExecute();
		policyMenu.clickTopInfoBarAccountNumber();
		acctMenu.clickAccountMenuInvoices();
		invoice.verifyInvoice(DateUtils.dateAddSubtract(currentTermExpDate, DateAddSubtractOptions.Day, -21), DateUtils.dateAddSubtract(currentTermExpDate, DateAddSubtractOptions.Day, -1), null, InvoiceType.RenewalDownPayment, null, InvoiceStatus.Planned, null, null);
	}
}

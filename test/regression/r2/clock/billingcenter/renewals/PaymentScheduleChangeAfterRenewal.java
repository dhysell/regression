package regression.r2.clock.billingcenter.renewals;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.AccountPolicies;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.policy.PolicyPaymentSchedule;
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
import repository.gw.enums.PolicyTermStatus;
import repository.gw.enums.RenewalCode;
import repository.gw.enums.TransactionType;
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

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

//import pc.policy.PolicyMenu;
/**
 * @Author JQu
 * @Description DE5619--Changing Scheduled Payment Plan prior to renewal is moving renewal down to a scheduled invoice due after renewal
 * Received renewal charges
 * change payment installment from annual to quarterly
 * Invoicing shows correct on preview screen
 * Execute the change
 * Renewal invoice pushed out to next available invoice after renewal
 * Invoice changed from renewal down to scheduled
 * @Requirement
 * @DATE June 9, 2017
 */
public class PaymentScheduleChangeAfterRenewal extends BaseTest {
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
			.withPolTermLengthDays(100)
			.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
			.withPolicyLocations(locationsList)						
			.withDownPaymentType(PaymentType.Cash)
			.withPaymentPlanType(PaymentPlanType.Annual)
			.build(GeneratePolicyType.PolicyIssued);
        currentTermExpDate = this.myPolicyObj.busOwnLine.getExpirationDate();
	}

	@Test(dependsOnMethods = { "generate" })
		public void payInsured() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		//wait for trouble ticket to come
        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
		policyMenu.clickBCMenuTroubleTickets();
        BCCommonTroubleTickets TT = new BCCommonTroubleTickets(driver);
		TT.waitForTroubleTicketsToArrive(60);
		//payoff the insured down payment
		policyMenu.clickTopInfoBarAccountNumber();
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
        directPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), myPolicyObj.accountNumber);
        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);	
		ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(currentTermExpDate, DateAddSubtractOptions.Day, -50));
	}
	@Test(dependsOnMethods = { "payInsured" })
	public void renewalPolicyInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.busOwnLine.getPolicyNumber());
        PolicyMenu pcCenterPolicyMenu = new PolicyMenu(driver);
        pcCenterPolicyMenu.clickRenewPolicy();
        new GuidewireHelpers(driver).logout();

        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.busOwnLine.getPolicyNumber());

        PolicySummary summaryPage = new PolicySummary(driver);
        if (summaryPage.checkIfActivityExists("Pre-Renewal Review")) {
			summaryPage.clickViewPreRenewalDirection();
            PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);

			boolean preRenewalDirectionExists = preRenewalPage.checkPreRenewalExists();
			if (preRenewalDirectionExists) {
				preRenewalPage.clickViewInPreRenewalDirection();
				preRenewalPage.closeAllPreRenewalDirectionExplanations();
				preRenewalPage.clickClosePreRenewalDirection();
				preRenewalPage.clickReturnToSummaryPage();
            }
        }
        StartRenewal renewal = new StartRenewal(driver);
		renewal.quoteAndIssueRenewal(RenewalCode.Renew_Good_Risk, myPolicyObj);	
		new GuidewireHelpers(driver).logout();	
	}
	@Test(dependsOnMethods = { "renewalPolicyInPolicyCenter" })
		public void changePaymentPlanTypeAndVerify() throws Exception {			
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
        schedule.selectNewPaymentPlan(PaymentPlanType.Quarterly);
		schedule.clickExecute();
        policyMenu.clickTopInfoBarAccountNumber();
		acctMenu.clickAccountMenuInvoices();
		invoice.verifyInvoice(DateUtils.dateAddSubtract(currentTermExpDate, DateAddSubtractOptions.Day, -21), DateUtils.dateAddSubtract(currentTermExpDate, DateAddSubtractOptions.Day, -1), null, InvoiceType.RenewalDownPayment, null, InvoiceStatus.Planned, null, null);		
	}
}

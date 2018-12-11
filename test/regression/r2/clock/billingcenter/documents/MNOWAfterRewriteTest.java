package regression.r2.clock.billingcenter.documents;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountPolicies;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDocuments;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.policy.BCPolicyMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DocumentType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PolicyStatus;
import repository.gw.enums.TransactionType;
import repository.gw.enums.TroubleTicketType;
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
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
/**
* @Author jqu
* @Description   DE6370: MNOW is not being generated when the next scheduled invoice after rewrite is billed
* 					1. Create a monthly policy. Pay off the down invoice,and bill it.
					2. Do a flat cancel.
					3. Immediately do a rewrite, but during rewrite make sure that rewritten policy's effective date is the same as the previously canceled policy. 
					4. Next bill and make the rewrite down invoice due after making payment.
					5. Bill the next scheduled invoice for next month.
					 Expected: A monthly notice of withdrawal should be generated.
					Actual: No document is being generated
* 					
* @DATE October 12, 2017
*/
public class MNOWAfterRewriteTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;
	private ARUsers arUser = new ARUsers();	
	private BCAccountMenu acctMenu;
	
	@Test
    public void generate() throws Exception {
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		locOneBuildingList.add(new PolicyLocationBuilding());
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));		

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        myPolicyObj = new GeneratePolicy.Builder(driver)
			.withInsPersonOrCompany(ContactSubType.Company)
			.withInsCompanyName("Insured Policy")
			.withPolOrgType(OrganizationType.Partnership)
			.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
			.withPolicyLocations(locationsList)			
			.withPaymentPlanType(PaymentPlanType.Monthly)
			.withDownPaymentType(PaymentType.Cash)
			.build(GeneratePolicyType.PolicyIssued);
	}
	@Test(dependsOnMethods = { "generate" })	
	public void payDownPayment() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		
		//pay down payment
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directBillPayment = new NewDirectBillPayment(driver);
        directBillPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), myPolicyObj.accountNumber);
		//move a day
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);		
	}
	@Test(dependsOnMethods = { "payDownPayment" })
    public void flatCancelPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.busOwnLine.getPolicyNumber());

        StartCancellation cancellation = new StartCancellation(driver);
		cancellation.cancelPolicy(CancellationSourceReasonExplanation.OtherPolicyRewrittenOrReplaced, "Cancel Policy", DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), true);		
	}
	@Test(dependsOnMethods = { "flatCancelPolicy" })
    public void rewriteFullTerm() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.busOwnLine.getPolicyNumber());

        StartRewrite rewrite = new StartRewrite(driver);
		rewrite.rewriteFullTerm(myPolicyObj);		
	}
	@Test(dependsOnMethods = { "rewriteFullTerm" })
    public void triggerMNOWAndVerify() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		//wait for rewrite to come
		acctMenu.clickBCMenuCharges();
        AccountCharges charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArrive(60, TransactionType.Rewrite);
		acctMenu.clickAccountMenuPolicies();
		//go to policy and clock the Rewrite trouble ticket
        AccountPolicies acctPolicy = new AccountPolicies(driver);
        acctPolicy.clickPolicyNumberInPolicyTableRow(null, null, null, null, PolicyStatus.Open, null, myPolicyObj.busOwnLine.getPremium().getInsuredPremium(), null, null, null);
        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
		policyMenu.clickBCMenuTroubleTickets();
        BCCommonTroubleTickets tt = new BCCommonTroubleTickets(driver);
		tt.closeTroubleTicket(TroubleTicketType.RewriteToHold);
		policyMenu.clickTopInfoBarAccountNumber();

        Date dueDateOfNextScheduledInvoice = DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getEffectiveDate(), DateAddSubtractOptions.Month, 1);
		ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(dueDateOfNextScheduledInvoice, DateAddSubtractOptions.Day, -myPolicyObj.paymentPlanType.getInvoicingLeadTime()));
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		acctMenu.clickBCMenuDocuments();
        BCCommonDocuments document = new BCCommonDocuments(driver);
		document.verifyDocument(DocumentType.Monthly_Notice_Of_Withdrawal.getValue(), DocumentType.Monthly_Notice_Of_Withdrawal, null, null, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null);		
	}	
}
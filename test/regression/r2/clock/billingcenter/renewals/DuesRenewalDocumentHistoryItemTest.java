package regression.r2.clock.billingcenter.renewals;

import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountPolicies;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDocuments;
import repository.bc.common.BCCommonHistory;
import repository.bc.common.BCCommonMenu;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.policy.BCPolicyMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RenewalCode;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.enums.Underwriter.UnderwriterLine;
import persistence.enums.Underwriter.UnderwriterTitle;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
* @Author JQU
* @Requirement 	US13937 - The policy history screen must display a record for when a second renewal document has been generated for Membership Dues only policies where the amount paid is less than 90% of the amount billed on 21 days prior to renewal effective date.
							�	Display history record as �Sent second renewal invoice�

* @DATE April 06, 2018
*/
public class DuesRenewalDocumentHistoryItemTest extends BaseTest {
	private ARUsers arUser;
	private GeneratePolicy myPolicyObj;	
	private int randomPayment;
	private Date expirationDate;
	private String historyItem = "Sent second renewal invoice.";
	//find the back dated effective date
	private Date effectiveDate;
	private String docName = "Membership Renewal";
	private WebDriver driver;

	//create back dated policy
	@Test
	public void generatePolicy() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		expirationDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 50);
		effectiveDate = DateUtils.dateAddSubtract(expirationDate, DateAddSubtractOptions.Year, -1);
		myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsFirstLastName("Membership", "Policy")
				.withProductType(ProductLineType.Membership)	
				.withPolEffectiveDate(effectiveDate)
				.withDownPaymentType(PaymentType.Cash)				
				.build(GeneratePolicyType.PolicyIssued);
		//pay < 90% of the dues
		randomPayment = NumberUtils.generateRandomNumberInt(0, (int) (myPolicyObj.membership.getPremium().getMembershipDuesAmount()*0.8));
	}		
	@Test(dependsOnMethods = { "generatePolicy" })
	public void payDues() throws Exception{
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		BCPolicyMenu bcPolicyMenu = new BCPolicyMenu(driver);
		bcPolicyMenu.clickBCMenuTroubleTickets();
		BCCommonTroubleTickets tt = new BCCommonTroubleTickets(driver);
		tt.waitForTroubleTicketsToArrive(60);
		
		bcPolicyMenu.clickTopInfoBarAccountNumber();
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuActionsNewDirectBillPayment();
		NewDirectBillPayment directPmt = new NewDirectBillPayment(driver);
		directPmt.makeDirectBillPaymentExecute(myPolicyObj.membership.getPremium().getMembershipDuesAmount(), myPolicyObj.accountNumber);		
	}
	@Test(dependsOnMethods = { "payDues" })
	public void manuallyRenewPolicy() throws Exception{		
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(UnderwriterLine.Personal, UnderwriterTitle.Underwriter);
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolicyObj.membership.getPolicyNumber());
        StartRenewal renewalWorkflow = new StartRenewal(driver);
		renewalWorkflow.renewPolicyManually(RenewalCode.Renew_Good_Risk, myPolicyObj);
		new GuidewireHelpers(driver).logout();
	}
	@Test(dependsOnMethods = { "manuallyRenewPolicy" })
	public void verifyDocAndHistoryItem() throws Exception{	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		//wait for renewal to come
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();
		AccountCharges charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArrive(60, TransactionType.Renewal);
		//trigger first document
		Date renewalDownInvDate = DateUtils.dateAddSubtract(expirationDate, DateAddSubtractOptions.Day, -46);		
		ClockUtils.setCurrentDates(driver, renewalDownInvDate);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
		//verify the first doc			
		accountMenu.clickBCMenuDocuments();
		BCCommonDocuments doc = new BCCommonDocuments(driver);
		if(!doc.verifyDocument(docName, null, null, null, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter))){
			Assert.fail("Renewal Bill is not triggered.");
		}
		
		//pay less than 90%
		accountMenu.clickAccountMenuActionsNewDirectBillPayment();
		NewDirectBillPayment directPmt = new NewDirectBillPayment(driver);
		directPmt.makeDirectBillPaymentExecute(randomPayment, myPolicyObj.accountNumber);	
		//trigger second doc on day 21 and verify history item
		ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(expirationDate, DateAddSubtractOptions.Day, -21));
		new BatchHelpers(driver).runBatchProcess(BatchProcess.FBM_Membership_21_Day_Document);
		
		accountMenu.clickBCMenuSummary();
		accountMenu.clickBCMenuDocuments();
		if(!doc.verifyDocument(docName, null, null, null, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter))){
			Assert.fail("The second Renewal Bill is not triggered.");
		}
		
		accountMenu.clickAccountMenuPolicies();		
		AccountPolicies policies = new AccountPolicies(driver);		
		policies.clickPolicyNumber(myPolicyObj.membership.getPolicyNumber()+"-1");
		
		BCCommonMenu bcCommonMenu = new BCCommonMenu(driver);
		bcCommonMenu.clickBCMenuHistory();
		BCCommonHistory history = new BCCommonHistory(driver);
		try{
			history.verifyHistoryTable(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), historyItem);
		}catch (Exception e) {
			Assert.fail("Couldn't find the history item for the second renewal invoice.");
		}
	}
}

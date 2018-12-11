package regression.r2.clock.billingcenter.cancel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.common.BCCommonDocuments;
import repository.bc.common.BCCommonSummary;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.DocumentType;
import repository.gw.enums.GeneratePolicyType;
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
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author jqu
 * @Description DE3486 UW delinquency is not setting inception date
 * DE2697: Cancellation -- UW Cancels must trigger Balance Due Letters
 * @Steps: Generate a policy with quarterly payment plan. Pay insured, move clock to next potential invoice date. Add coverage in PolicyCenter. The new charges should combine to the second invoice item. Move clock to second invoice date, run Invoice. Move clock to couple of days before the Due Date, cancel policy. Verify the UW cancel delinquency and the inception banner.
 * @Test Environment:
 * @DATE April 12, 2016
 */
@QuarantineClass
public class UWFullCancellationAndBalanceDueLettersTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;	
	private ARUsers arUser = new ARUsers();
	private BCAccountMenu acctMenu;	
	private AccountInvoices acctInvoice;
	private float newBuildingBPPLimit=300000;
	private Date currentDate;
	private List<Date> dueDateList;
	private String bannerShouldBe="Underwriting Full Cancel";
	private String firstNoticeLetter="First Notice Balance Due On Cancelled Policy";
	private String finalNoticeLetter="Final Notice Balance Due On Cancelled Policy";

	private void triggerAndVerifyTheBalanceDueLetter(int daysToMove, String balDueLetter, DocumentType docType) throws Exception{
        BCCommonDocuments document = new BCCommonDocuments(driver);
		currentDate=DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, daysToMove);
		ClockUtils.setCurrentDates(driver, currentDate);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.BC_Workflow);
		document.clickSearch();
        try {
			document.getDocumentsTableRow(balDueLetter, docType, null, null, currentDate, currentDate);
		}catch(Exception e){
			Assert.fail("doesn't find the balance due letter '"+balDueLetter+"'.");
		}
	}
	@Test
	public void generate() throws Exception {
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();			
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();	
		loc1Bldg1.setBuildingLimit(30000);
		loc1Bldg1.setBppLimit(30000);
		locOneBuildingList.add(loc1Bldg1);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));	
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("UWDelinquencyTest")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);		
	}
	@Test(dependsOnMethods = { "generate" })	
		public void payDownAndMoveClockToPassNextPotentialInvoiceDate() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);		
		//get Due Dates list
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
        acctInvoice = new AccountInvoices(driver);
		dueDateList=acctInvoice.getListOfInvoiceDates();
		//pay insured
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
        directPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount());
		currentDate=DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, 1);
		ClockUtils.setCurrentDates(cf, currentDate);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		//move clock to the second 'potential' invoice date before the second Invoice row
		currentDate=DateUtils.dateAddSubtract(dueDateList.get(0), DateAddSubtractOptions.Month, 2);
		currentDate=DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, -15);
		ClockUtils.setCurrentDates(cf, currentDate);
	}
	@Test(dependsOnMethods = { "payDownAndMoveClockToPassNextPotentialInvoiceDate" })
    public void policyChangeToAddCoverage() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.accountNumber);
        StartPolicyChange pcChange = new StartPolicyChange(driver);
		pcChange.changeBuildingCoverage(1, newBuildingBPPLimit, newBuildingBPPLimit, currentDate);
	}
	@Test(dependsOnMethods = { "policyChangeToAddCoverage" })
    public void verifyPolicyChangeAndMoveClock() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
        AccountCharges charge = new AccountCharges(driver);
        charge.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(120, currentDate, TransactionType.Policy_Change, this.myPolicyObj.busOwnLine.getPolicyNumber());
		//move clock to second invoice date
		currentDate=DateUtils.dateAddSubtract(dueDateList.get(1), DateAddSubtractOptions.Day, myPolicyObj.paymentPlanType.getInvoicingLeadTime());
		ClockUtils.setCurrentDates(cf, currentDate);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		//move clock to several days before the due date
		currentDate=DateUtils.dateAddSubtract(dueDateList.get(1), DateAddSubtractOptions.Day, -3);
		ClockUtils.setCurrentDates(cf, currentDate);		
	}
	//cancel the policy. 
	@Test(dependsOnMethods = { "verifyPolicyChangeAndMoveClock" })
    public void cancelThePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.accountNumber);
        StartCancellation cancellation = new StartCancellation(driver);
		cancellation.cancelPolicy(CancellationSourceReasonExplanation.OtherInsuredRequest, "Underwriter Cancellation", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), true);
	}
	//earned premium > credit after policy cancellation, so should get UW cancel delinquency.
	@Test(dependsOnMethods = { "cancelThePolicy" })	
	public void verifyUWCancellation() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
		//wait for the cancellation to arrive
        AccountCharges charges = new AccountCharges(driver);
		charges.waitUntilPolicyCancellationArrive(30000);
		acctMenu.clickBCMenuSummary();
        BCCommonSummary summary = new BCCommonSummary(driver);
		try{
			String alertMsg=summary.getDelinquencyAlertMessage();
			if(!alertMsg.contains(bannerShouldBe)){
				Assert.fail("the banner is incorrect.");				
			}
		}catch(Exception e){
			Assert.fail("didn't receive the delinquency banner.");
		}
		//verify the underwriting delinquency
		acctMenu.clickBCMenuDelinquencies();
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		try{
			delinquency.getDelinquencyTableRow(OpenClosed.Open, DelinquencyReason.UnderwritingFullCancel, myPolicyObj.accountNumber, null, myPolicyObj.accountNumber, currentDate, null, null);
		}catch(Exception e){
			Assert.fail("doesn't find Underwriting Full Cancel which is expected.");
		}
	}
	//trigger first balance due letter on UW cancellation inception date+1 day
	//trigger final balance due letter on UW cancellation inception date+22 day	
	@Test(dependsOnMethods = { "verifyUWCancellation" })	
	public void verifyBalanceDueLetters() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuDocuments();		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		//move clock to UW Cancellation inception date + 1 day to trigger the first balance due letter
		triggerAndVerifyTheBalanceDueLetter(1, firstNoticeLetter, DocumentType.First_Notice_Balance_Due);
		//move clock to UW Cancellation inception date + 22 day to trigger the final balance due letter
		triggerAndVerifyTheBalanceDueLetter(21, finalNoticeLetter, DocumentType.Final_Notice_Balance_Due);		
	}
}

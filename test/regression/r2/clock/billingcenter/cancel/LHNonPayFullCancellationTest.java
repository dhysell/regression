package regression.r2.clock.billingcenter.cancel;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.common.BCCommonActivities;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.common.BCCommonDocuments;
import repository.driverConfiguration.Config;
import repository.gw.enums.ActivityStatus;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactRole;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.DocumentType;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
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
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/******
 * @Author jqu
 * @Description: US7722 LH Non-Pay Full Cancellation. Single LH payer only.
 * @Steps:
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/06%20-%20Delinquency%20Cancel/06-02%20Lienholder%20Non-Pay%20Full%20Cancel%20Delinquency.docx">Lienholder Non-Pay Full Cancel Delinquency</a>
 * @DATE June 27, 2016
 */
@QuarantineClass
public class LHNonPayFullCancellationTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;
	private ARUsers arUser = new ARUsers();
	private Date currentDate;	
	private String loanNumber="LN11111";
	private String lienNumber;
	private BCCommonDocuments document;
	private String activityLinkText="Review Delinquency";
	private BCAccountMenu acctMenu;
	private BCCommonDelinquencies delinquency;

    private void moveClockAndRunWorkflow(Config driverConfiguration, int daysToMove, boolean runInvoiceDue) throws Exception {
		currentDate=DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, daysToMove);
		ClockUtils.setCurrentDates(driverConfiguration, currentDate);
		if(runInvoiceDue)
			new BatchHelpers(driverConfiguration).runBatchProcess(BatchProcess.Invoice_Due);
		new BatchHelpers(driverConfiguration).runBatchProcess(BatchProcess.BC_Workflow);
	}
	
	private void verifyBalanceDueDocument(DocumentType docType, Date date) {
        document = new BCCommonDocuments(driver);
		try{
			document.getDocumentsTableRow(docType.getValue(), docType, null, null, date, null);
		}catch(Exception e){
			Assert.fail("doesn't find the "+docType.getValue());
		}
	}
	@Test
	public void generate() throws Exception {
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();			
		ArrayList<AdditionalInterest> loc1LNBldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
		
		Config cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);
		
		ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
		rolesToAdd.add(ContactRole.Lienholder);

        GenerateContact myContactObj = new GenerateContact.Builder(driver)
				.withCompanyName("Lienholder")
				.withRoles(rolesToAdd)
				.withGeneratedLienNumber(true)
				.withUniqueName(true)
				.build(GenerateContactType.Company);
        driver.quit();
		
		AdditionalInterest loc1LNBldg1AddInterest = new AdditionalInterest(myContactObj.companyName, myContactObj.addresses.get(0));
		loc1LNBldg1AddInterest.setLienholderNumber(myContactObj.lienNumber);
		loc1LNBldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_All);		
		loc1LNBldg1AddInterest.setLoanContractNumber(loanNumber);
		loc1LNBldg1AdditionalInterests.add(loc1LNBldg1AddInterest);
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setAdditionalInterestList(loc1LNBldg1AdditionalInterests);
		locOneBuildingList.add(loc1Bldg1);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));			

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
        this.myPolicyObj = new GeneratePolicy.Builder(driver).withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("LNNonPayFullCancel")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Monthly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);

		lienNumber=myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();		
	}
	@Test(dependsOnMethods = { "generate" })
	public void makeLHPastDueWithoutPaymentAndVerifyDelinquecny() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		//move clock 1 day past LH's Due Date
        currentDate = DateUtils.dateAddSubtract(DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getEffectiveDate(), DateAddSubtractOptions.Month, 1), DateAddSubtractOptions.Day, 1);
		ClockUtils.setCurrentDates(cf, currentDate);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		//verify the Past Due Lien Partial Cancel
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuDelinquencies();
        delinquency = new BCCommonDelinquencies(driver);
		try{
			delinquency.getDelinquencyTableRow(OpenClosed.Open, DelinquencyReason.PastDueLienPartialCancel, lienNumber, loanNumber, myPolicyObj.accountNumber, currentDate,null, null);
		}catch (Exception e){
			Assert.fail("doesn't find the Past Due Lien Partial Cancel delinquency.");
		}
		currentDate=DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 10);
		ClockUtils.setCurrentDates(cf, currentDate);
	}
	@Test(dependsOnMethods = { "makeLHPastDueWithoutPaymentAndVerifyDelinquecny" })
    public void cancelPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.busOwnLine.getPolicyNumber());
        StartCancellation cancellation = new StartCancellation(driver);
		cancellation.cancelPolicy(CancellationSourceReasonExplanation.OtherInsuredRequest, "Cancel the Policy", currentDate, true);
	}
	@Test(dependsOnMethods = { "cancelPolicy" })
	public void verifyDelinquencies() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		//wait for cancellation to come
		acctMenu.clickBCMenuCharges();
        AccountCharges charge = new AccountCharges(driver);
		try{
            charge.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(120, currentDate, TransactionType.Cancellation, this.myPolicyObj.busOwnLine.getPolicyNumber());
		}catch(Exception e){
            Assert.fail("doesn't find the cancellation " + "for " + myPolicyObj.busOwnLine.getPolicyNumber());
		}
		//verify insured's new invoice
		acctMenu.clickAccountMenuInvoices();
        AccountInvoices invoice = new AccountInvoices(driver);
		try{
			invoice.getAccountInvoiceTableRow(currentDate, currentDate, null, InvoiceType.Shortage, null, InvoiceStatus.Planned, null, null);
		}catch(Exception e){
            Assert.fail("doesn't find the cancellation " + "for " + myPolicyObj.busOwnLine.getPolicyNumber());
		}
		//move clock to pass due of the insured Shortage invoice
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		currentDate=DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 1);
		ClockUtils.setCurrentDates(cf, currentDate);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		
		acctMenu.clickBCMenuDelinquencies();
        delinquency = new BCCommonDelinquencies(driver);
		//verify that the Past Due Lien Partial Cancel delinquency is closed
		try{
            delinquency.getDelinquencyTableRow(OpenClosed.Closed, DelinquencyReason.PastDueLienPartialCancel, null, loanNumber, myPolicyObj.busOwnLine.getPolicyNumber(), null, null, null);
		}catch (Exception e){
			Assert.fail("doesn't find the Past Due Lien Partial Cancel delinquency.");
		}
		//verify Past Due Full Cancel delinquency is triggered
		try{
            delinquency.getDelinquencyTableRow(OpenClosed.Open, DelinquencyReason.UnderwritingFullCancel, null, null, myPolicyObj.busOwnLine.getPolicyNumber(), null, null, null);
		}catch (Exception e){
			Assert.fail("doesn't find the Past Due Full Cancel delinquency.");
		}
	}
	@Test(dependsOnMethods = { "verifyDelinquencies" })
	public void verifyBalanceDueLettersAndActivity() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);		
		//verify the trigger of first balance due notice
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		moveClockAndRunWorkflow(cf, 1, true);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuDocuments();
		verifyBalanceDueDocument(DocumentType.First_Notice_Balance_Due, currentDate);
		
		//verify the trigger of final balance due notice
		moveClockAndRunWorkflow(cf, 21, false);
		document.clickSearch();
		verifyBalanceDueDocument(DocumentType.Final_Notice_Balance_Due, currentDate);
		//verify the Review Delinquency Activity
		moveClockAndRunWorkflow(cf, 21, false);
		acctMenu.clickBCMenuActivities();
        BCCommonActivities activity = new BCCommonActivities(driver);
		try{
			activity.clickActivityTableSubject(currentDate, null, null, ActivityStatus.Open, activityLinkText);
		}catch(Exception e){
			Assert.fail("doesn't find the Review Delinquency Activity on inception date + 43 days.");
		}
		//click "Write Off" button
		activity.clickWriteoff();

        //verify the delinquency is closed
		acctMenu.clickBCMenuDelinquencies();
        delinquency = new BCCommonDelinquencies(driver);
		try{
            delinquency.getDelinquencyTableRow(OpenClosed.Closed, DelinquencyReason.UnderwritingFullCancel, myPolicyObj.accountNumber, null, myPolicyObj.busOwnLine.getPolicyNumber(), null, null, 0.0);
		}catch(Exception e){
			Assert.fail("the policy is not sucessfully written off.");
		}
	}

}

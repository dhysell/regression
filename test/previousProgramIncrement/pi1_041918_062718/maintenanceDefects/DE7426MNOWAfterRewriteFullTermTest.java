package previousProgramIncrement.pi1_041918_062718.maintenanceDefects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDocuments;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
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
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.rewrite.StartRewrite;
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
* @Requirement 	DE7426 - **Hot Fix** MNOW is not created when scheduled invoice is billed after we got rewrite
* 				STEPS : Create a policy with a monthly payment plan
						Move into policy for few months
						Cancel the policy
						Rewrite the policy after 30 days
						Once we get a rewrite, make down invoice bill and due.
						Go to the next planned scheduled invoice, bill it.
						An MNOW document should get created, However, it won't.

* @DATE April 25, 2018*/
@Test(groups = {"ClockMove"})
public class DE7426MNOWAfterRewriteFullTermTest extends BaseTest {
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();	
	private List<Date> dueDatesList = new ArrayList<Date>();
	private WebDriver driver;
	private BatchHelpers batchHelper;
	private void generatePolicy() throws Exception {
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		PolicyLocationBuilding building1=new PolicyLocationBuilding();

		locOneBuildingList.add(building1);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));		

        myPolicyObj = new GeneratePolicy.Builder(driver)
			.withInsPersonOrCompany(ContactSubType.Company)
			.withInsCompanyName("MNOWAfterRewrite")
			.withPolOrgType(OrganizationType.Partnership)
			.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
			.withPolicyLocations(locationsList)			
			.withPaymentPlanType(PaymentPlanType.Monthly)
			.withDownPaymentType(PaymentType.ACH_EFT)
			.build(GeneratePolicyType.PolicyIssued);        
	}	
	@Test	
	public void payDownAndFirstInstallment() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		generatePolicy();
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		driver.get(cf.getUrlOfCenter(ApplicationOrCenter.BillingCenter));
		
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuInvoices();
        AccountInvoices invoice = new AccountInvoices(driver);
		dueDatesList = invoice.getListOfDueDates();
		//run Invoice to pay down payment
		batchHelper = new BatchHelpers(driver);
		batchHelper.runBatchProcess(BatchProcess.Invoice);
		//pay the first installment
		ClockUtils.setCurrentDates(driver, dueDatesList.get(1));
		String invoiceNumber = invoice.getInvoiceTableCellValue("Invoice Number", DateUtils.dateAddSubtract(dueDatesList.get(1), DateAddSubtractOptions.Day,  myPolicyObj.paymentPlanType.getInvoicingLeadTime()), dueDatesList.get(1), null, null, null, null, null, null);
		double dueAmount = NumberUtils.getCurrencyValueFromElement(invoice.getInvoiceTableCellValue("Due", DateUtils.dateAddSubtract(dueDatesList.get(1), DateAddSubtractOptions.Day,  myPolicyObj.paymentPlanType.getInvoicingLeadTime()), dueDatesList.get(1), null, null, null, null, null, null));
		batchHelper.runBatchProcess(BatchProcess.Invoice_Due);
		batchHelper.runBatchProcess(BatchProcess.Invoice);
		batchHelper.runPaymentRequest(driver, (DateUtils.dateAddSubtract(dueDatesList.get(1), DateAddSubtractOptions.Day, myPolicyObj.paymentPlanType.getInvoicingLeadTime())), dueDatesList.get(1), invoiceNumber, dueAmount);
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);
		batchHelper.runBatchProcess(BatchProcess.Invoice_Due);		
	}
	
	@Test(dependsOnMethods = { "payDownAndFirstInstallment" })
	public void flatCancelPolicy() throws Exception{
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(UnderwriterLine.Personal, UnderwriterTitle.Underwriter);
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolicyObj.busOwnLine.getPolicyNumber());
        StartCancellation cancellation = new StartCancellation(driver);
        cancellation.cancelPolicy(CancellationSourceReasonExplanation.OtherPolicyRewrittenOrReplaced, "flat cancel", DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), true);        
	}
	
	@Test(dependsOnMethods = { "flatCancelPolicy" })
    public void verifyCancellation() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();
        AccountCharges charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArrive(60, TransactionType.Cancellation);
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);	
	}
	
	@Test(dependsOnMethods = { "verifyCancellation" })
	public void rewriteFullTermInPolicyCenter() throws Exception{
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(UnderwriterLine.Personal, UnderwriterTitle.Underwriter);
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolicyObj.busOwnLine.getPolicyNumber());
		StartRewrite rewrite = new StartRewrite(driver);
        rewrite.rewriteFullTerm(myPolicyObj);
	}
	
	@Test(dependsOnMethods = { "rewriteFullTermInPolicyCenter" })	
	public void triggerDocument() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();
        AccountCharges charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArrive(60, TransactionType.Rewrite);
		//pay rewrite down payment by cancellation credit
		batchHelper = new BatchHelpers(driver);
		batchHelper.runBatchProcess(BatchProcess.Invoice);
		accountMenu.clickAccountMenuInvoices();
        AccountInvoices invoice = new AccountInvoices(driver);
//		double leftRewriteDown = invoice.getInvoiceDueAmountByDueDate(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter));
        Date rewriteDueDate = DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getEffectiveDate(), DateAddSubtractOptions.Month, 2);
        
        ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(rewriteDueDate, DateAddSubtractOptions.Day, -15));
        batchHelper.runBatchProcess(BatchProcess.Invoice_Due);
        batchHelper.runBatchProcess(BatchProcess.Invoice);
        double leftRewriteDown = invoice.getInvoiceDueAmountByDueDate(rewriteDueDate);
		if(leftRewriteDown>0){
            accountMenu.clickAccountMenuActionsNewDirectBillPayment();
            NewDirectBillPayment directPay = new NewDirectBillPayment(driver);
			directPay.makeDirectBillPaymentExecute(leftRewriteDown, myPolicyObj.accountNumber);
		}
		//trigger MNOW and verify
		Date nextInvoiceDate = null;		
		boolean found = false;
		int i =0;
		while(!found && i< dueDatesList.size()){
			nextInvoiceDate = DateUtils.dateAddSubtract(dueDatesList.get(i), DateAddSubtractOptions.Day, PaymentPlanType.Monthly.getInvoicingLeadTime());
			if(dueDatesList.get(i).compareTo(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter))>0  && nextInvoiceDate.compareTo(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter))>0){				
				found =true;				
			}else{
				i++;
			}
		}		
//		ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(dueDatesList.get(i), DateAddSubtractOptions.Day, PaymentPlanType.Monthly.getInvoicingLeadTime()));
		batchHelper.runBatchProcess(BatchProcess.Invoice_Due);
		batchHelper.runBatchProcess(BatchProcess.Invoice);
		accountMenu.clickBCMenuDocuments();
        BCCommonDocuments document = new BCCommonDocuments(driver);
		if(!document.verifyDocument("Monthly Notice of Withdrawal", null, null, null, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter))){
			Assert.fail("Didn't find the expected document.");
		}
	}
}

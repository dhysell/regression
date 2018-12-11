package regression.r2.clock.billingcenter.delinquency;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.search.BCSearchAccounts;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInsuredRole;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentReversalReason;
import repository.gw.enums.PaymentType;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
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
 * @Description: The 90% paid delinquency requirement still should be for policy #, loan #, payer.
 * Trigger delinquency for every public coverable ID under the same policy #, loan #, payer.
 * Delinquent amount should only be past due, unpaid items.Everything under this combination goes delinquent.
 * Ability to have multiple delinquency flows open
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/06%20-%20Delinquency%20Cancel/06-02%20Lienholder%20Delinquency%20Cancellation.docx">LH Delinquency Cancellation</a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/06%20-%20Delinquency%20Cancel/Supporting%20Documentation/Delinquency%20Cancel%20Workflow.vsdx">Delinquency Cancel Workflow</a>
 * @Description
 * @DATE March 2nd, 2016
 */
public class LienholderDelinquencyTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;
	private String loanNumber1="LN11111", loanNumber2="LN22222", lienholderNumber;	
	private BCAccountMenu acctMenu;
	private AccountInvoices acctInvoice;
	private double lienPremium, lessThresholdOfPayment, thresholdOfPastDue;
	private float buildingBPPLimit=100000;
	private ARUsers arUser = new ARUsers();
	private Date currentDate;
	
	private void verifyLHDelinquency(OpenClosed status, DelinquencyReason reason, String loanNumber, Date startDate, Double amount){
        BCCommonDelinquencies delinq = new BCCommonDelinquencies(driver);
		try{
			delinq.getDelinquencyTableRow(status, reason, lienholderNumber, loanNumber, null, startDate, null, amount);
		}catch(Exception e){
			Assert.fail("Didn't find the correct delinquecny for lienholder "+lienholderNumber+" with loan number "+ loanNumber1);
		}
	}
	
	private void gotoAccount(String accountNumber){
		BCSearchAccounts search = new BCSearchAccounts(driver);
		search.searchAccountByAccountNumber(accountNumber);	
	}
	
	//generate a policy with two same LHs, the two LHs have different Loan numbers 
	@Test
	public void generatePolicyWithTwoLHs() throws Exception {
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();			
		ArrayList<AdditionalInterest> loc1LNBldg1AdditionalInterests = new ArrayList<AdditionalInterest>();	
		ArrayList<AdditionalInterest> loc1LNBldg2AdditionalInterests = new ArrayList<AdditionalInterest>();		
		AdditionalInterest loc1LNBldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1LNBldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1LNBldg1AddInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);		
		loc1LNBldg1AddInterest.setLoanContractNumber(loanNumber1);			
		loc1LNBldg1AdditionalInterests.add(loc1LNBldg1AddInterest);
		
		AddressInfo additionalInsuredAddress = new AddressInfo();
		additionalInsuredAddress.setLine1(loc1LNBldg1AddInterest.getAddress().getLine1());
		additionalInsuredAddress.setCity(loc1LNBldg1AddInterest.getAddress().getCity());
		additionalInsuredAddress.setState(loc1LNBldg1AddInterest.getAddress().getState());
		additionalInsuredAddress.setZip(loc1LNBldg1AddInterest.getAddress().getZip());
		
		AdditionalInterest loc1LNBldg2AddInterest = new AdditionalInterest(loc1LNBldg1AddInterest.getCompanyName(), additionalInsuredAddress);
		loc1LNBldg2AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1LNBldg2AddInterest.setLoanContractNumber(loanNumber2);
		loc1LNBldg2AdditionalInterests.add(loc1LNBldg2AddInterest);		
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setBuildingLimit(buildingBPPLimit);
		loc1Bldg1.setBppLimit(buildingBPPLimit);
		loc1Bldg1.setAdditionalInterestList(loc1LNBldg1AdditionalInterests);		
		PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
		loc1Bldg2.setBuildingLimit(buildingBPPLimit);
		loc1Bldg2.setBppLimit(buildingBPPLimit);
		loc1Bldg2.setAdditionalInterestList(loc1LNBldg2AdditionalInterests);
		locOneBuildingList.add(loc1Bldg1);	
		locOneBuildingList.add(loc1Bldg2);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));	
		
		//DE2455 blocks LH cancellations for policies with Equipment Breakdown, so for this test I use the policies without Equipment Breakdown and all LH charges.
		//After DE2455 is fixed, will delete the "uncheck equipment breakdown" block to have the policies have the Equipment Breakdown.
		//uncheck equipment breakdown
		AddressInfo address = new AddressInfo();
		PolicyBusinessownersLine boLine = new PolicyBusinessownersLine(SmallBusinessType.SelfStorageFacilities);
		PolicyBusinessownersLineAdditionalCoverages additionalCoverageStuff = new PolicyBusinessownersLineAdditionalCoverages(false, false);
		boLine.setAdditionalCoverageStuff(additionalCoverageStuff);
		ArrayList<PolicyBusinessownersLineAdditionalInsured> additonalInsuredBOLineList = new ArrayList<PolicyBusinessownersLineAdditionalInsured>();
		additonalInsuredBOLineList.add(new PolicyBusinessownersLineAdditionalInsured(ContactSubType.Company, "dafigudhfiuhdafg",AdditionalInsuredRole.CertificateHolderOnly, address));			
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver).withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("LHDelinquency")
				.withPolOrgType(OrganizationType.Partnership)
//				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withBusinessownersLine(boLine)
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		lienholderNumber=myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNumber();	
		//lienholder1 and lienholder2 have the same premium
//		lienPremium=myPolicyObj.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();	
        lienPremium = myPolicyObj.busOwnLine.getPremium().getTotalNetPremium() / 2;
		lessThresholdOfPayment=NumberUtils.round(lienPremium*0.6, 2);		
		//to exit delinquency, need to pay 90% of the past due amount
		thresholdOfPastDue=NumberUtils.round((lienPremium-lessThresholdOfPayment)*0.9, 2);
	}
	@Test(dependsOnMethods = { "generatePolicyWithTwoLHs" })		
	public void payLienholders() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		currentDate=DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		//pay insured
        acctMenu = new BCAccountMenu(driver);
		//DE2455 blocks LH cancellations for policies with Equipment Breakdown, so for this test I use the policies without Equipment Breakdown and all LH charges temporally.		
		//After DE2455 is fixed, I will uncomment this block since I will choose Equipment Breakdown and Insured will have charges.
//		acctMenu.clickAccountMenuInvoices();
//		runBatchProcess(BatchProcess.Invoice);
//		acctMenu.clickAccountMenuActionsNewDirectBillPayment(driver);
//		NewDirectBillPayment directPayment=AccountFactory.getNewDirectBillPaymentPage();
//		directPayment.makeDirectBillPaymentExecute(myPolicyObj.insuredPremium);
//		currentDate=DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 1);
//		ClockUtils.setCurrentDates(currentDate);
//		runBatchProcess(BatchProcess.Invoice_Due);
		
		//move to LH invoice date and pay LHs with lessThresholdOfPayment
		ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 11));		
		gotoAccount(lienholderNumber);
        acctMenu.clickAccountMenuInvoices();
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
        directPayment = new NewDirectBillPayment(driver);
        directPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPolicyNumber(), loanNumber1, lessThresholdOfPayment);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        directPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPolicyNumber(), loanNumber2, lessThresholdOfPayment);
		//move clock after due date and run InvoiceDue, should get two "Past Due Lien Partial Cancel" on insured Delinquency screen
        Date LHDueDate = DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getEffectiveDate(), DateAddSubtractOptions.Month, 1);
		currentDate=DateUtils.dateAddSubtract(LHDueDate, DateAddSubtractOptions.Day, 1);
		ClockUtils.setCurrentDates(cf, currentDate);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		//verify the delinquency 		
		gotoAccount(myPolicyObj.accountNumber);	
		acctMenu.clickBCMenuDelinquencies();
		verifyLHDelinquency(OpenClosed.Open, DelinquencyReason.PastDueLienPartialCancel, loanNumber1, null, lienPremium-lessThresholdOfPayment);
		verifyLHDelinquency(OpenClosed.Open, DelinquencyReason.PastDueLienPartialCancel, loanNumber2, null, lienPremium-lessThresholdOfPayment);
		new GuidewireHelpers(driver).logout();
	}
	//pay extra 90% of the past due for the lienholder with loanNumber1, then verify the Close of its delinquency
	@Test(dependsOnMethods = { "payLienholders" })	
	public void payMoreForLoan1AndVerifyDelinquencyExit() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), lienholderNumber);
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
		directPayment.setAmount(thresholdOfPastDue);
        directPayment.clickOverrideDistribution();
        directPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPolicyNumber(), loanNumber1, thresholdOfPastDue);
		//go to insured and verify that LH delinquency closed after the extra payment
		gotoAccount(myPolicyObj.accountNumber);
		acctMenu.clickBCMenuDelinquencies();
		verifyLHDelinquency(OpenClosed.Closed, DelinquencyReason.PastDueLienPartialCancel, loanNumber1, null, null);
		//verify FBMCarryForward of the unpaid amount, FBMCarryForward works 2 days after due date		
		//FBMCarryForward should carry forward the unpaid amount of loanNumber1 to next available invoice item
		gotoAccount(lienholderNumber);		
		acctMenu.clickAccountMenuInvoices();
        acctInvoice = new AccountInvoices(driver);
		currentDate=DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 1);
		ClockUtils.setCurrentDates(cf, currentDate);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.FBM_Carry_Forward);
		acctMenu.clickBCMenuSummary();
        acctMenu.clickAccountMenuInvoices();
        //verify carry forward items
		try{
			acctInvoice.getAccountInvoiceTableRow(null, null, null, InvoiceType.NewBusinessDownPayment, null, InvoiceStatus.Carried_Forward, null, lienPremium-lessThresholdOfPayment);
		}catch(Exception e){
			Assert.fail("didn't find the uncarried forward amount after running FBMCarryForward.");
		}		
		try{
			acctInvoice.getAccountInvoiceTableRow(null, null, null, InvoiceType.Shortage, null, InvoiceStatus.Planned, null, lienPremium-lessThresholdOfPayment-thresholdOfPastDue);				
		}catch(Exception e){
			Assert.fail("didn't find the Shortage row of the carried forward greaterThanThresholdAmt after running FBMCarryForward.");
		}		
	}	
	//reverse the payment of loanNumber1 and it should trigger the delinquency again.
	@Test(dependsOnMethods = { "payMoreForLoan1AndVerifyDelinquencyExit" })	
	public void reversePaymentAndVerifyDelinquency() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), lienholderNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuPayments();
        AccountPayments payment = new AccountPayments(driver);
		payment.reversePayment(thresholdOfPastDue, thresholdOfPastDue, null, PaymentReversalReason.Payment_Moved);
		//go to insured Delinquency screen to verify that the delinquency is open again for the LH with loanNumber1		
		gotoAccount(myPolicyObj.accountNumber);			
		//verify the delinquency 
		acctMenu.clickBCMenuDelinquencies();
		verifyLHDelinquency(OpenClosed.Open, DelinquencyReason.PastDueLienPartialCancel, loanNumber1, null,lienPremium-lessThresholdOfPayment);		
		//go to lienholder invoice and verify that Carried forward amount is reverted 			
		gotoAccount(lienholderNumber);		
		acctMenu.clickAccountMenuInvoices();
        acctInvoice = new AccountInvoices(driver);
		try{
			acctInvoice.getAccountInvoiceTableRow(null, null, null, InvoiceType.NewBusinessDownPayment, null, InvoiceStatus.Due, null, (lienPremium-lessThresholdOfPayment)*2);
		}catch(Exception e){
			Assert.fail("The invoice type is incorrect after payment is reverted.");
		}		
		try{
			acctInvoice.getAccountInvoiceTableRow(null, null, null, InvoiceType.Shortage, null, InvoiceStatus.Planned, null, lienPremium-lessThresholdOfPayment-thresholdOfPastDue);	
			Assert.fail("this invoice should be gone after payment is reverted.");
		}catch(Exception e){
			getQALogger().info("doesn't find the carried forword itme which is excepted.");
		}
		new GuidewireHelpers(driver).logout();
	}
	//cancel the lienholder with loanNumber1 and verify the delinquency is closed after cancellation
	@Test(dependsOnMethods = { "reversePaymentAndVerifyDelinquency" })
    public void cancelOneLienholder() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.busOwnLine.getPolicyNumber());
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.removeBuildingByBldgNumber(1);
		new GuidewireHelpers(driver).logout();
	}
	//verify LH1's delinquency is closed after the cancellation
	@Test(dependsOnMethods = { "cancelOneLienholder" })
    public void verifyDelinquencyAfterPolicyChange() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
        AccountCharges charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(120, currentDate, TransactionType.Policy_Change, lienholderNumber);
		acctMenu.clickBCMenuDelinquencies();
		verifyLHDelinquency(OpenClosed.Closed, DelinquencyReason.PastDueLienPartialCancel, loanNumber1, currentDate, null);
		new GuidewireHelpers(driver).logout();
	}	
	//cancel policy and verify LH2's delinquency is closed after cancellation
	@Test(dependsOnMethods = { "verifyDelinquencyAfterPolicyChange" })
    public void cancelThePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.busOwnLine.getPolicyNumber());
        StartCancellation cancel = new StartCancellation(driver);
		cancel.cancelPolicy(CancellationSourceReasonExplanation.PoorLossHistory, "cancel the policy", currentDate, true);
		new GuidewireHelpers(driver).logout();
	}
	@Test(dependsOnMethods = { "cancelThePolicy" })		
	public void verifyDelinquencyAfterCancellation() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		//wait for the policy change to come in
		acctMenu.clickBCMenuCharges();
        AccountCharges charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(120, currentDate, TransactionType.Cancellation, lienholderNumber);
		acctMenu.clickBCMenuDelinquencies();
		verifyLHDelinquency(OpenClosed.Closed, DelinquencyReason.PastDueLienPartialCancel, loanNumber2, currentDate, null);
		new GuidewireHelpers(driver).logout();
	}
}

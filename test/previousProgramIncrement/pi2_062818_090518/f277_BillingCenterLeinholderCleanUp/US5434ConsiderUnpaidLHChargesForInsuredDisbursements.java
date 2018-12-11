package previousProgramIncrement.pi2_062818_090518.f277_BillingCenterLeinholderCleanUp;

import repository.bc.account.AccountDisbursements;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.common.BCCommonCharges;
import repository.bc.common.BCCommonTroubleTicket;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.search.BCSearchAccounts;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactRole;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DisbursementStatus;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceType;
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
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

import java.util.ArrayList;

/**
* @Author JQU
* @Requirement 	US5434 -- Disbursements - Consider Unpaid LH Charges for Insured Disbursements
* 				Steps to get there:
					Create a policy with both Insured and Lien billed coverage's (Insured is annual)
					Issue policy
					Overpay down payment for Insured
					Move clock 2 days, run invoice and invoice due, auto disbursement batches
					Insured down should be paid and the extra amount is in applied
					Move clock another 10 days, run Invoice and auto disbursement batch
					There should not be a disbursement created for the insured as long as there are no future plan invoices
					Cancel lien billed coverages
					After wax on/wax off of lien coverage's to the insured, move clock 1 day and run invoice and invoice due and auto disbursement batch
					Excess money in unapplied should be applied to the onset of charges to the insured
					Run clock 1 day
					A disbursement should generate for any amount in unapplied funds for the insured. 
* 				

* @DATE July 23, 2018
* 
* */
@Test(groups = {"ClockMove"})
public class US5434ConsiderUnpaidLHChargesForInsuredDisbursements extends BaseTest{
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();	
	private String loanNumber="LN12345";
	private String lienholderNumber;
	private double extraPayment = NumberUtils.generateRandomNumberInt(1000, 2000);
	
	private void generatePolicy() throws Exception {
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		PolicyLocationBuilding building1=new PolicyLocationBuilding();
		building1.setBuildingLimit(150000);
		building1.setBppLimit(150000);
		locOneBuildingList.add(building1);
		
		Config cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);
		ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
		rolesToAdd.add(ContactRole.Lienholder);
		
        GenerateContact myContactLienLoc1Obj = new GenerateContact.Builder(driver)
				.withCompanyName("Lienholder")
				.withRoles(rolesToAdd)
				.withGeneratedLienNumber(true)
				.withUniqueName(true)
				.build(GenerateContactType.Company);
		
		
		
		ArrayList<AdditionalInterest> loc1LNBldg2AdditionalInterests = new ArrayList<AdditionalInterest>();		
		AdditionalInterest loc1LNBldg2AddInterest = new AdditionalInterest(myContactLienLoc1Obj.companyName, myContactLienLoc1Obj.addresses.get(0));
		
		loc1LNBldg2AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);

		loc1LNBldg2AddInterest.setLoanContractNumber(loanNumber);
		loc1LNBldg2AdditionalInterests.add(loc1LNBldg2AddInterest);
		PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
		loc1Bldg2.setAdditionalInterestList(loc1LNBldg2AdditionalInterests);
		locOneBuildingList.add(loc1Bldg2);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));					

		driver.quit();

		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("ConsiderLHWhenDisburse")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
        lienholderNumber = myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNumber();
		this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();
		driver.quit();
	}
	@Test
	public void makeLHPastDueWithoutPaymentAndVerifyDelinquecny() throws Exception {
		generatePolicy();
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
        new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        BCPolicyMenu bcPolicyMenu = new BCPolicyMenu(driver);
        bcPolicyMenu.clickBCMenuTroubleTickets();
        BCCommonTroubleTicket tt = new BCCommonTroubleTicket(driver);
        tt.waitForTroubleTicketsToArrive(60);
        bcPolicyMenu.clickTopInfoBarAccountNumber();
        //pay insured with extra amount
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
        directPayment.makeDirectBillPaymentExecuteWithoutDistribution(myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount()+extraPayment, myPolicyObj.accountNumber);
        ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, 2));
        
        BatchHelpers batchHelper = new BatchHelpers(cf);
        batchHelper.runBatchProcess(BatchProcess.Invoice);
        batchHelper.runBatchProcess(BatchProcess.Invoice_Due);
        batchHelper.runBatchProcess(BatchProcess.Automatic_Disbursement);
        
        accountMenu.clickAccountMenuPaymentsPayments();
        AccountPayments payment = new AccountPayments(driver);
        payment.verifyPaymentAndClick(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, null, null, null, null, null, null, null, null, myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), extraPayment);

        ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, 10));
        batchHelper.runBatchProcess(BatchProcess.Invoice);
        batchHelper.runBatchProcess(BatchProcess.Invoice_Due);
        batchHelper.runBatchProcess(BatchProcess.Automatic_Disbursement);
        //verify disbursement not created
        accountMenu.clickAccountMenuDisbursements();
        AccountDisbursements disbursement = new AccountDisbursements(driver);
        int disbursementRowCount = disbursement.getDisbursementTableRowCount();
        if(disbursementRowCount > 0){
        	Assert.fail("Disbursment should not be created.");
        }
	}
	@Test(dependsOnMethods = { "makeLHPastDueWithoutPaymentAndVerifyDelinquecny" })
	public void removeLHCharge() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.busOwnLine.getPolicyNumber());
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.removeBuildingByBldgNumber(2);
	}
	@Test(dependsOnMethods = { "removeLHCharge" })
	public void verifyOnsetPaidAndDisbursement() throws Exception {

		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), lienholderNumber);
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickBCMenuCharges();
        BCCommonCharges charge = new BCCommonCharges(driver);
        charge.waitUntilChargesFromPolicyContextArrive(60, TransactionType.Policy_Change);
        accountMenu.clickAccountMenuInvoices();
        AccountInvoices invoice = new AccountInvoices(driver);
        double unpaidAmount = invoice.getInvoiceDueAmountByDueDate(DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getEffectiveDate(), DateAddSubtractOptions.Month, 1));
        if(unpaidAmount > 0){
        	accountMenu.clickAccountMenuActionsNewDirectBillPayment();
        	NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
        	directPayment.makeDirectBillPaymentExecute(myPolicyObj.accountNumber, loanNumber, unpaidAmount);
        }
        BCSearchAccounts search = new BCSearchAccounts(driver);
        search.searchAccountByAccountNumber(myPolicyObj.accountNumber);
        
        accountMenu.clickBCMenuCharges();
        double OnSetAmount = NumberUtils.getCurrencyValueFromElement(charge.getChargesOrChargeHoldsPopupTableCellValue("Amount", null, myPolicyObj.accountNumber, null, null, TransactionType.Policy_Change, null, null, null, null, null, null, null, null, null, null, null));
       //verify OnSet Invoice
        accountMenu.clickAccountMenuInvoices();

        Assert.assertTrue(invoice.verifyInvoice(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, InvoiceType.LienholderOnset, null, null, OnSetAmount, OnSetAmount), "Ensure BC get Lienholder Onset");
        //move 1 day, run batch process
        ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, 1));
        BatchHelpers batchHelper = new BatchHelpers(cf);
        batchHelper.runBatchProcess(BatchProcess.Invoice);
        batchHelper.runBatchProcess(BatchProcess.Invoice_Due);
        batchHelper.runBatchProcess(BatchProcess.Automatic_Disbursement);
        
        accountMenu.clickAccountMenuDisbursements();
        AccountDisbursements disbursement = new AccountDisbursements(driver);
        Assert.assertTrue(disbursement.verifyDisbursement(null, null, null, myPolicyObj.accountNumber, DisbursementStatus.Awaiting_Approval, null, extraPayment- OnSetAmount, null, null, null), "Ensure account "+myPolicyObj.accountNumber+" gets the correct disbursement of $"+(extraPayment- OnSetAmount));
	}
}

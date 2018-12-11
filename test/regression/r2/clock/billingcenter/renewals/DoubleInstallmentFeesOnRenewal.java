package regression.r2.clock.billingcenter.renewals;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountChargesMoveInvoiceItems;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ChargeCategory;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
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
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
/**
* @Author JQU
* @ Description: DE4987 - COMMON - BC Insured Renewal Bill Issue* 
* 					1. Location on the back of the document not always showing (this is the insured location in the LH breakdown)
					2. $8.00 installment fee is showing as being charged on the renewal invoice when only $ 4.00 should (this happens when the $  prior invoice fee is not completely paid but that should show in prior balance)
* @DATE January 4th, 2018
*/
public class DoubleInstallmentFeesOnRenewal extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;
	private ARUsers arUser = new ARUsers();	
	private double installmentFee = 4;
	private Date lastPotentialDueDate;
	private Date policyChangeDate;
	private double policyChangeAmount;
	
	@Test
    public void issuePolicy() throws Exception {
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setBuildingLimit(100000);
		loc1Bldg1.setBppLimit(100000);
		locOneBuildingList.add(loc1Bldg1);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));			

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        myPolicyObj = new GeneratePolicy.Builder(driver)
			.withInsPersonOrCompany(ContactSubType.Company)
			.withInsCompanyName("Insured Policy")
			.withPolOrgType(OrganizationType.Partnership)
			.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
			.withPolicyLocations(locationsList)			
			//create a policy with length of 120 days
			.withPolTermLengthDays(120)
			.withPaymentPlanType(PaymentPlanType.Quarterly)
			.withDownPaymentType(PaymentType.ACH_EFT)
			.build(GeneratePolicyType.PolicyIssued);		
	}
	@Test(dependsOnMethods = { "issuePolicy" })
	public void payInvoicesForCurrentTerm() throws Exception {				
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		
		//run Invoice to pay down payment 		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);	
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		
		//pay second invoice
        ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getExpirationDate(), DateAddSubtractOptions.Month, -3));
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
        directPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPremium().getInsuredPremium() - myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount() + installmentFee, myPolicyObj.accountNumber);
		//move clock to a few days before the last potential invoice date
        lastPotentialDueDate = DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getExpirationDate(), DateAddSubtractOptions.Month, -1);
		policyChangeDate = DateUtils.dateAddSubtract(lastPotentialDueDate, DateAddSubtractOptions.Day, myPolicyObj.paymentPlanType.getInvoicingLeadTime()-3);
		ClockUtils.setCurrentDates(cf, policyChangeDate);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
	}
	@Test(dependsOnMethods = { "payInvoicesForCurrentTerm" })
    public void increaseInsuredCoverage() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.busOwnLine.getPolicyNumber());
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.changeBuildingCoverage(1, 250000, 250000);
        GenericWorkorderComplete complete = new GenericWorkorderComplete(driver);
		complete.clickViewYourPolicy();

		//get lien premium change in PC->ChargeToBC screen
        PolicySummary summary = new PolicySummary(driver);
		policyChangeAmount = summary.getTransactionPremium(TransactionType.Policy_Change, "change coverage");
	}
	@Test(dependsOnMethods = { "increaseInsuredCoverage" })
    public void verifyPolicyChange() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
        AccountCharges charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArrive(60, TransactionType.Policy_Change);		
	}
	@Test(dependsOnMethods = { "verifyPolicyChange" })
	public void renewalPolicyInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.busOwnLine.getPolicyNumber());
		//move to day 50
        ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getExpirationDate(), DateAddSubtractOptions.Day, -50));

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
    public void movePolicyChangeChargeToRenewalDownPaymentAndVerify() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		//wait for Renewal 
		acctMenu.clickBCMenuCharges();
        AccountCharges charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArrive(60, TransactionType.Renewal);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);	
		
		//move policy change charges to renewal down payment
		charge.getChargesOrChargeHoldsPopupTableRow(null, null, null, null, TransactionType.Policy_Change, null, null, null, null, policyChangeAmount, null, null, null, null, null, null).click();
        charge.setChargesOrChargeHoldsInvoiceItemsTableCheckBox(null, null, null, null, null, null, null, TransactionType.Policy_Change, null, policyChangeAmount, null);
        charge.clickMoveInvoiceItems();
        AccountChargesMoveInvoiceItems moveInvItem = new AccountChargesMoveInvoiceItems(driver);
        Date renewalDownDueDate = DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getExpirationDate(), DateAddSubtractOptions.Day, -1);
		moveInvItem.clickSelectButton(null, DateUtils.dateAddSubtract(renewalDownDueDate, DateAddSubtractOptions.Day, myPolicyObj.paymentPlanType.getInvoicingLeadTime()), renewalDownDueDate, null, null, null, null, null);
        //move to renewal down payment invoice date and run invoice
		ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(renewalDownDueDate, DateAddSubtractOptions.Day, myPolicyObj.paymentPlanType.getInvoicingLeadTime()));		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		
		//verify only on installment fee is charged
		acctMenu.clickAccountMenuInvoices();
        AccountInvoices invoice = new AccountInvoices(driver);
		invoice.getAccountInvoiceTableRow(null, renewalDownDueDate, null, InvoiceType.RenewalDownPayment, null, null, null, null).click();
        try {
            invoice.getAccountInvoiceChargesTableRow(null, null, myPolicyObj.busOwnLine.getPolicyNumber().concat("-1"), ChargeCategory.Installment_Fee, null, null, null, null, 4.0, null, null, null);
            Assert.fail("This installment fee from last term should not be charged.");
        } catch (Exception e) {
            getQALogger().info("Couldn't find the installment fee from last term which is expected.");
        }
	}	
}

package regression.r2.clock.billingcenter.installmentscheduling;

import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ChargeCategory;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SR22FilingFee;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author JQU
 * @Description DE6278: Cancellation Credit received is not being applied to Membership Dues first
 * The cancellation credit should be paid with this priority: 1. Recapture, 2. Dues, 3. Sr22/Fees, 4. Premium(with same loan number, policy and charge group as credit), 5. Premium (with same loan number and policy)
 * @Steps 1. Create a fully insured paid quarterly policy.
 * 2. Pay the down.
 * 3. Move clock close to next Invoice Date. Do a policy change to add Dues and Sr-22 to insured. (This should go to second invoice)
 * 4. Make the next  invoice billed, we will have an installment fee now.
 * 5. Now cancel the policy to get cancellation credit.
 * 6. All future invoices will now be rolled up.
 * 7. The credit should first pay off dues, SR-22, and installment fees,
 * @DATE November 7, 2017
 */
@QuarantineClass
public class CancellationCreditAppliedToDuesFirstTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;	
	private double newDues;
	private ARUsers arUser = new ARUsers();
	private Date secondDueDate;
	private double sr22Fee =25;

	@Test
	public void generateSquireAutoOnly() throws Exception {

		// line auto coverages
		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow, MedicalLimit.TenK);
        coverages.setUnderinsured(false);

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = new SquirePersonalAuto();

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Credits", "Dues")
                .withPaymentPlanType(PaymentPlanType.Quarterly)
                .build(GeneratePolicyType.PolicyIssued);
	}
	@Test(dependsOnMethods = { "generateSquireAutoOnly" })
	public void makeDownPaymentAndMoveClock() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.squire.getPremium().getDownPaymentAmount(), myPolicyObj.accountNumber);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		//move clock to a few days before next invoice date, so after policy change, the new charges will be combined to this invoice
        secondDueDate = DateUtils.dateAddSubtract(myPolicyObj.squire.getEffectiveDate(), DateAddSubtractOptions.Month, 3);
		//paymentPlanType.getInvoicingLeadTime() return negative integer
		ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(secondDueDate, DateAddSubtractOptions.Day, myPolicyObj.paymentPlanType.getInvoicingLeadTime()-5));			
	}
	@Test(dependsOnMethods = { "makeDownPaymentAndMoveClock" })
	private void addNewMembershipDues() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.accountNumber);

		// start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		newDues= addMembershipDues();			
	}
	
	public double addMembershipDues() throws Exception {
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("Add Membership Dues", null);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();

        PolicyInfoAdditionalNamedInsured policyANI = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person, "Test" + StringsUtils.generateRandomNumberDigits(8), "Due", AdditionalNamedInsuredType.Friend, new AddressInfo(true));
        policyANI.setNewContact(CreateNew.Create_New_Always);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.addAdditionalNamedInsured(true, policyANI);
        if (!polInfo.setMembershipDues(policyANI.getPersonFirstName(), true)) {
            Assert.fail("Membership dues is not set for newly added ANI");
        }
        sideMenu.clickSideMenuHouseholdMembers();
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis_UWIssues risk = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        risk.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
        }
        sideMenu.clickSideMenuRiskAnalysis();
        risk.approveAll_IncludingSpecial();
        sideMenu.clickSideMenuQuote();
        double priorMembershipDues = quote.getQuoteChangeInCost();
        policyChangePage.clickIssuePolicy();

        return priorMembershipDues;
    }
	@Test(dependsOnMethods = { "addNewMembershipDues" })
    public void verifyNewDuesInBC() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
        AccountCharges charge = new AccountCharges(driver);
		WebElement newDuesRow = charge.getChargesOrChargeHoldsPopupTableRow(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, null, ChargeCategory.Membership_Dues, TransactionType.Policy_Change, null, null, null, null, newDues, null, null, null, null, null, null);
		if(newDuesRow==null){
			Assert.fail("can't find the new Dues.");
		}		
	}
	@Test(dependsOnMethods = { "verifyNewDuesInBC" })
	private void setSR22Fee() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.accountNumber);

		// start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		setSR22Fee(SR22FilingFee.Charged, 1);		
	}
	
	
	public void setSR22Fee(SR22FilingFee sr22, int driverTableRowNum) throws Exception {
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("change coverage", null);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_ContactDetail policyDriver = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        policyDriver.clickEditButtonInDriverTable(driverTableRowNum);
        policyDriver.setSR22Checkbox(true);
        policyDriver.setSR22FilingFee(sr22);
        policyDriver.clickOk();
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis_UWIssues risk = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        risk.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
        }
        sideMenu.clickSideMenuRiskAnalysis();
        risk.approveAll_IncludingSpecial();
        policyChangePage.clickIssuePolicy();
    }
	
	
	
	
	@Test(dependsOnMethods = { "setSR22Fee" })
    public void verifySR22AndMakeNextInvoiceBilled() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
        AccountCharges charge = new AccountCharges(driver);
		WebElement SR22Row = charge.getChargesOrChargeHoldsPopupTableRow(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, null, ChargeCategory.SR22_Fee, TransactionType.Policy_Change, null, null, null, null, sr22Fee, null, null, null, null, null, null);
		if(SR22Row==null){
			Assert.fail("can't find SR-22.");
        }

        ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(secondDueDate, DateAddSubtractOptions.Day, myPolicyObj.paymentPlanType.getInvoicingLeadTime()));
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
	}
	@Test(dependsOnMethods = { "verifySR22AndMakeNextInvoiceBilled" })
	private void cancelPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.accountNumber);

        StartCancellation cancellation = new StartCancellation(driver);
		cancellation.cancelPolicy(CancellationSourceReasonExplanation.OtherInsuredRequest, "Cancel Policy", DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), true);		
	}
	@Test(dependsOnMethods = { "cancelPolicy" })
    public void verifyCreditDistribution() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
        AccountCharges charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArrive(TransactionType.Cancellation);

        acctMenu.clickAccountMenuInvoices();
        AccountInvoices invoice = new AccountInvoices(driver);
		invoice.getAccountInvoiceTableRow(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, null, InvoiceType.Scheduled, null, InvoiceStatus.Billed, null, null).click();

        double paidAmtForNewDues = NumberUtils.getCurrencyValueFromElement(invoice.getInvoiceChargesTableCellValue("Paid Amount", null, null, null, ChargeCategory.Membership_Dues, TransactionType.Policy_Change, null, null, null, null, null, null, null));
		if(paidAmtForNewDues != newDues)
			Assert.fail("dues is not totally paid by the cancellation credit.");
		double paidSR22 = NumberUtils.getCurrencyValueFromElement(invoice.getInvoiceChargesTableCellValue("Paid Amount", null, null, null, ChargeCategory.SR22_Fee, TransactionType.Policy_Change, null, null, null, null, null, null, null));
		if(paidSR22 != sr22Fee)
			Assert.fail("SR-22 fee is not totally paid by the cancellation credit.");		
	}
}

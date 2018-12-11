package regression.r2.clock.billingcenter.installmentscheduling;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.wizards.ModifyInvoiceItemsWizard;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ChargeCategory;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
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
 * @Description US12444: New Batch to spread dues created by BC on reinstatement/rewrite remainder of term to planned/scheduled invoices
 * if the Membership Dues was on a installment is reinstated or rewritten remainder of term, the unpaid portion of the due be spread out in remaining planned invoice by a batch process
 * 1. First create a policy with membership dues.
 * 2. Move that single dues installment equally into future invoices, so that it is spread.(since US11300, membership due is a sum amount)
 * 3. Then move some months, so that some of the dues is paid, and some of them is still planned (for eg., say out of 120 of dues, 20 was paid).
 * 4. Cancel the policy.
 * 5. Then do either reinstate or rewrite remainder of term. BC will create two dues charges of 100 and (100)
 * 6. The positive 100 dollar will be invoiced in single invoice.
 * 7. Now run the batch, it should spread it to future scheduled invoices equally, if everything is working.
 * <p>
 * DE6655: ROP Membership Dues is being applied to all invoice items for the same dues even though they are planned
 * @DATE September 29, 2017
 */
public class FBMDuesInstallmentBatchProcessTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;
	private ARUsers arUser = new ARUsers();	
	private BCAccountMenu acctMenu;
	private List<Date> invoiceDueDateList= new ArrayList<Date>();
	private double dueSpreadToFutureInvoice, dueSpreadToDownPayment, memberShipDueAmount, unpaidDues;

	@Test
	public void generate() throws Exception {		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.CondominiumVacationHome));
		locationsList.add(new PolicyLocation(locOnePropertyList));

        StandardFireAndLiability myStandardFire = new StandardFireAndLiability();
        myStandardFire.setLocationList(locationsList);
        myStandardFire.section1Deductible = SectionIDeductible.FiveHundred;

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withStandardFire(myStandardFire)
				.withInsFirstLastName("Dues", "InstallmentBatchProcess")				
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);				
	}	
	@Test(dependsOnMethods = { "generate" })	
	public void spreadDuesAndPayDownPayment() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		//get invoice due dates
		acctMenu.clickAccountMenuInvoices();
        AccountInvoices invoice = new AccountInvoices(driver);
		invoiceDueDateList=invoice.getListOfDueDates();			
		
		//calculate dues that should spread to down payment and scheduled invoices.
		acctMenu.clickBCMenuCharges();
        AccountCharges charge = new AccountCharges(driver);
		memberShipDueAmount= NumberUtils.getCurrencyValueFromElement(charge.getChargesOrChargeHoldsPopupTableCellValue("Amount", null, null, null, ChargeCategory.Membership_Dues, null, null, null, null, null, null, null, null, null, null, null, null));
		
		dueSpreadToFutureInvoice=NumberUtils.round(memberShipDueAmount/myPolicyObj.paymentPlanType.getNumberOfPaymentPeriods(), 2);
		dueSpreadToDownPayment = NumberUtils.round(memberShipDueAmount- dueSpreadToFutureInvoice*(myPolicyObj.paymentPlanType.getNumberOfPaymentPeriods()-1), 2);		
		
		WebElement memberShipDuesRow=charge.getChargesOrChargeHoldsPopupTableRow(null, null, null, ChargeCategory.Membership_Dues, null, null, null, null, null, null, null, null, null, null, null, null);
		memberShipDuesRow.click();
        charge.clickModifyInvoiceItems();
        ModifyInvoiceItemsWizard invoiceItemWizard = new ModifyInvoiceItemsWizard(driver);
		
		for(int i=1;i<myPolicyObj.paymentPlanType.getNumberOfPaymentPeriods();i++){
			invoiceItemWizard.clickAdd();
            invoiceItemWizard.setAmount(i + 1, dueSpreadToFutureInvoice);
			invoiceItemWizard.setEventDate(i+1, invoiceDueDateList.get(i));
		}
		//set the due for down payment invoice 
		invoiceItemWizard.setAmount(1, dueSpreadToDownPayment);
        invoiceItemWizard.clickUpdate();
		//pay down payment
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directBillPayment = new NewDirectBillPayment(driver);
        directBillPayment.makeDirectBillPaymentExecute(myPolicyObj.standardFire.getPremium().getDownPaymentAmount() + dueSpreadToDownPayment, myPolicyObj.accountNumber);
		unpaidDues = NumberUtils.round(memberShipDueAmount-dueSpreadToDownPayment, 2);
		//move a few days
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 3);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);		
	}
	@Test(dependsOnMethods = { "spreadDuesAndPayDownPayment" })
    public void cancelPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.standardFire.getPolicyNumber());

        StartCancellation cancellation = new StartCancellation(driver);
		cancellation.cancelPolicy(CancellationSourceReasonExplanation.NoReasonGiven, "Cancel Policy", DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), true);		
	}
	@Test(dependsOnMethods = { "cancelPolicy" })
    public void verifyCancellationInBC() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
        AccountCharges charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArrive(60, TransactionType.Cancellation);
	}
	@Test(dependsOnMethods = { "verifyCancellationInBC" })
    public void rewritePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.standardFire.getPolicyNumber());
        StartRewrite rewrite = new StartRewrite(driver);
		rewrite.rewriteRemainderOfTerm(myPolicyObj.lineSelection, ProductLineType.Businessowners);
	}
	@Test(dependsOnMethods = { "rewritePolicy" })	
	public void runFBMDuesInstallmentBatchProcessAndVerify() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
        AccountCharges charge = new AccountCharges(driver);
		//wait for rewrite to come 
		charge.waitUntilChargesFromPolicyContextArrive(60, TransactionType.Reinstatement);
		//calculate unpaid dues to spread to rewrite invoice and future planned invoices		
		double unpaidDueSpreadToFutureInvoice=NumberUtils.round(unpaidDues/myPolicyObj.paymentPlanType.getNumberOfPaymentPeriods(), 2);
		double unpaidDueSpreadToRewriteInvoice = NumberUtils.round(unpaidDues- unpaidDueSpreadToFutureInvoice*(myPolicyObj.paymentPlanType.getNumberOfPaymentPeriods()-1), 2);		
				
		new BatchHelpers(cf).runBatchProcess(BatchProcess.FBM_Dues_Installment_Batch);
		
//		verify the amount after running FBM Dues Installment Batch
		charge.getChargesOrChargeHoldsPopupTableRow(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, null, ChargeCategory.Membership_Dues, TransactionType.Rewrite, null, null, null, null, unpaidDues, null, null, null, null, null, null).click();
        Date rewriteInvoiceDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, 1);
		if(charge.getChargesOrChargeHoldsInvoiceItemsTableRow(null, rewriteInvoiceDate, null, rewriteInvoiceDate, null, null, null, null, ChargeCategory.Down_Payment, unpaidDueSpreadToRewriteInvoice, 0.0) == null)
			Assert.fail("unpaid dues spreaded to Rewrite Down Payment is incorrect.");
		for(int i=1; i<invoiceDueDateList.size(); i++){
			if(charge.getChargesOrChargeHoldsInvoiceItemsTableRow(null, invoiceDueDateList.get(i), null, invoiceDueDateList.get(i), null, null, null, null, ChargeCategory.Installment_Fee, unpaidDueSpreadToFutureInvoice, 0.0) == null)
				Assert.fail("unpaid dues spreaded to future planned invoices is incorrect.");			
		}		
	}	
}

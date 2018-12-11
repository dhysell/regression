package previousProgramIncrement.pi3_090518_111518.f276_BC_Document_Cleanup;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDocuments;
import repository.bc.common.BCCommonTroubleTicket;
import repository.bc.policy.BCPolicyMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DocumentType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author JQU
* * @Requirement 	DE7819 -- MNOW printed when amount due is less than $9.99
* * @Acceptance criteria:
					Ensure that a monthly notice of withdrawal is not printing when the invoice is under the $9.99 threshold. (Req. 13-04-11)
** @Steps to get there:
					Monthly payment plan, collect down in PC to cover down and over 90% of the next scheduled invoice while making sure the amount due in scheduled invoice is less than $9.99
					Scheduled invoice goes billed

Actual: MNOW is printed when amount due is less than $9.99 
Expected: MNOW should not print if the scheduled invoice's amount due is less than $9.99		
					
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/_layouts/15/WopiFrame.aspx?sourcedoc=/billingcenter/Documents/Release%202%20Requirements/13%20-%20Printed%20Documents/04%20-%20Monthly%20Notice%20of%20Withdrawal/13-04%20Monthly%20Notice%20of%20Withdrawal%20Document%20Change%20Request.docx&action=default">13-04 Monthly Notice of Withdrawal</a>
* @DATE September 10, 2018*/
@Test(groups = {"ClockMove"})
public class DE7819MNOWShouldNotPrintWhenAmountDueLessThan10 extends BaseTest{
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();	
	private int amountDue = NumberUtils.generateRandomNumberInt(1, 8);
	
	private void generatePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.CondominiumVacationHome));
		locationsList.add(new PolicyLocation(locOnePropertyList));

        StandardFireAndLiability myStandardFire = new StandardFireAndLiability();
        myStandardFire.setLocationList(locationsList);
        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withStandardFire(myStandardFire)				
				.withInsFirstLastName("NOW", "Bill")				
				.withPaymentPlanType(PaymentPlanType.Monthly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);  
        driver.quit();
	}
	@Test
	public void verifyNOWNotTriggerWithAmountDueLessThan10() throws Exception {	
		generatePolicy();		
		
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		
		new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		BatchHelpers batchHelper = new BatchHelpers(cf);
		//wait for TT to come
		BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
		policyMenu.clickBCMenuTroubleTickets();
		BCCommonTroubleTicket troubleTicket = new BCCommonTroubleTicket(driver);
		troubleTicket.waitForTroubleTicketsToArrive(300);
		
		policyMenu.clickTopInfoBarAccountNumber();
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuInvoices();
		AccountInvoices invoice = new AccountInvoices(driver);
		double installmentAmount = invoice.getInvoiceAmountByRowNumber(2);
		batchHelper.runBatchProcess(BatchProcess.Invoice);
		
		accountMenu.clickAccountMenuActionsNewDirectBillPayment();
		NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
		directPayment.makeDirectBillPaymentExecute(myPolicyObj.standardFire.getPremium().getDownPaymentAmount()+(installmentAmount-amountDue), myPolicyObj.accountNumber);
		
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
		batchHelper.runBatchProcess(BatchProcess.Invoice_Due);
		//move to first installment Invoice date
		Date firstInstallmentInvoiceDate = DateUtils.dateAddSubtract(DateUtils.dateAddSubtract(myPolicyObj.standardFire.getEffectiveDate(), DateAddSubtractOptions.Month, 1), DateAddSubtractOptions.Day, -15);
		ClockUtils.setCurrentDates(cf, firstInstallmentInvoiceDate);
		
		//verify that first installment has due amount = amountDue
		batchHelper.runBatchProcess(BatchProcess.Invoice);
		accountMenu.clickBCMenuSummary();
		accountMenu.clickAccountMenuInvoices();
		
		Assert.assertTrue(invoice.verifyInvoice(firstInstallmentInvoiceDate, null, null, InvoiceType.Scheduled, null, null, installmentAmount, (double)amountDue), 
				"Invoice date is :"+DateUtils.dateFormatAsString("MM/dd/yyyy",  firstInstallmentInvoiceDate)+"\n Amount is "+installmentAmount+"The first installment should have Due of "+amountDue);
		
		//verify NOW is not triggered by invoice with due amount <10		
		accountMenu.clickBCMenuDocuments();
		BCCommonDocuments document = new BCCommonDocuments(driver);
		Assert.assertFalse(document.verifyDocument("Monthly Notice of Withdrawal", DocumentType.Monthly_Notice_Of_Withdrawal, null, null, firstInstallmentInvoiceDate, firstInstallmentInvoiceDate), "NOW document should not be created for invoice with amount due < $10.");
	}
}

package regression.r2.clock.billingcenter.delinquency;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.common.BCCommonDelinquencies;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentReversalReason;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author jqu
 * @Requirement US5527 Delinquency until Renewal on Farm and Ranch Shortage Bills
 * @Steps: Generate a Farm&Ranch policy with Annual Payment plan; Pay 90% of the premium (down payment). Move clock 1 day after Due date and run invoiceDue. Verify the TT which holds the delinquency and the new Shortage invoice in next available invoice date with the unpaid amount. Reverse the payment, verify that the TT is gone and the total premium is in the Shortage invoice. Move to the Shortage invoice and make Payment again. Verify the TT shows again after the Shortage invoice gets Due. Move clock to policy Expiration date, verify that the TT closes on that day.
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/06%20-%20Delinquency%20Cancel/06-03%20Hold%20Delinquency%20On%20Farm%20And%20Ranch%20Policies.docx">Hold Delinquency on Farm And Ranch Policies</a>
 * @DATE September 19, 2016
 */
@QuarantineClass
public class HoldDelinquencyFarmAndRanchShortageBillsTest extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObj;
    private ARUsers arUser = new ARUsers();
	private BCAccountMenu acctMenu;
	private AccountInvoices acctInvoice;		
	private Date currentDate;	
	private Date newShortageInvoiceDueDate;
	private double carryforwardAmt, paymentToMake;

    @Test
    public void generateFarmAndRanch() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locationsList.add(locToAdd);

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withInsFirstLastName("Guy", "Farmranchsqwfpp")
                .withPolOrgType(OrganizationType.Individual)
                .withProductType(ProductLineType.Squire)
                .withSquireEligibility(SquireEligibility.FarmAndRanch)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);        
    }
    @Test(dependsOnMethods = { "generateFarmAndRanch" })	
	public void payNinetyPercentAndVerifyTT() throws Exception {			
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        paymentToMake = NumberUtils.round(myPolicyObj.squire.getPremium().getTotalNetPremium() * 0.9, 2);
        NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
		directPayment.makeDirectBillPaymentExecute(paymentToMake, myPolicyObj.accountNumber);
        carryforwardAmt = myPolicyObj.squire.getPremium().getTotalNetPremium() - paymentToMake;
		currentDate=DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, 1);
		ClockUtils.setCurrentDates(driver, currentDate);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);		
		//the unpaid amount should appear in next available invoice as Shortage
		acctMenu.clickAccountMenuInvoices();
        acctInvoice = new AccountInvoices(driver);
        newShortageInvoiceDueDate = DateUtils.dateAddSubtract(myPolicyObj.squire.getEffectiveDate(), DateAddSubtractOptions.Month, 1);
		try{
			acctInvoice.getAccountInvoiceTableRow(DateUtils.dateAddSubtract(newShortageInvoiceDueDate, DateAddSubtractOptions.Day, -myPolicyObj.paymentPlanType.getInvoicingLeadTime()), newShortageInvoiceDueDate, null, InvoiceType.Shortage, null, InvoiceStatus.Planned, null, carryforwardAmt);
		}catch(Exception e){
			Assert.fail("doesn't find the Shortage invoice for the unpaid amount.");
		}			
	}
	@Test(dependsOnMethods = { "payNinetyPercentAndVerifyTT" })	
	public void reversePaymentAndVerify() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		//move clock to the invoice date of the shortage invoice
		currentDate=DateUtils.dateAddSubtract(newShortageInvoiceDueDate, DateAddSubtractOptions.Day, -myPolicyObj.paymentPlanType.getInvoicingLeadTime());
		ClockUtils.setCurrentDates(driver, currentDate);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		//not pay the shortage and move clock past due
		currentDate=DateUtils.dateAddSubtract(newShortageInvoiceDueDate, DateAddSubtractOptions.Day, 1);
		ClockUtils.setCurrentDates(driver, currentDate);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		//verify that the delinquency is not triggered
		acctMenu.clickBCMenuDelinquencies();
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		try{
			delinquency.getDelinquencyTableRow(OpenClosed.Open, DelinquencyReason.PastDueFullCancel, null, null, null, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), null,null);
			Assert.fail("delinquency is expected to be held.");
		}catch(Exception e){
			getQALogger().info("didn't find delinquency which is expected.");
		}
		//reverse the payment, and verify the delinquency is triggered
		acctMenu.clickAccountMenuPayments();
        AccountPayments payment = new AccountPayments(driver);
		payment.reversePayment(paymentToMake, null, null, PaymentReversalReason.Payment_Moved);
		acctMenu.clickBCMenuDelinquencies();
		try{
			delinquency.getDelinquencyTableRow(OpenClosed.Open, DelinquencyReason.PastDueFullCancel, null, null, null, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), null, null);
		}catch(Exception e){
			Assert.fail("delinquency is expected to be triggered.");
		}
		//manually exit the delinquency, verify the Shortage bill is carried forward to next invoice
		delinquency.clickExitDelinquencyButton();
		acctMenu.clickAccountMenuInvoices();
        acctInvoice = new AccountInvoices(driver);
		newShortageInvoiceDueDate=DateUtils.dateAddSubtract(newShortageInvoiceDueDate, DateAddSubtractOptions.Month, 1);
		try{
			acctInvoice.getAccountInvoiceTableRow(DateUtils.dateAddSubtract(newShortageInvoiceDueDate, DateAddSubtractOptions.Day, -myPolicyObj.paymentPlanType.getInvoicingLeadTime()), newShortageInvoiceDueDate, null, InvoiceType.Shortage, null, InvoiceStatus.Planned, null, carryforwardAmt);
		}catch(Exception e){
			Assert.fail("doesn't find the new Shortage invoice after Exit Delinquency");
		}	
	}
}

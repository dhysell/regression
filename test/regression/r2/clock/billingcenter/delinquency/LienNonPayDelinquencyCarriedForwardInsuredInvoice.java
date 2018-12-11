package regression.r2.clock.billingcenter.delinquency;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPaymentsPaymentRequests;
import repository.bc.common.BCCommonDelinquencies;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Status;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author SGUNDA
 * @Requirement DE6698 - **HOT-FIX** Insured invoices are getting carried forward when there is an open lien non-pay delinquency
 * Create a monthly policy with insured and lien paid charges
 * Pay the insured's down, but do not pay the lien.
 * When the lien's invoice becomes due, it triggers Lien Partial Cancel delinquency correctly.
 * Run clock to insured's next planned invoice bill date.
 * Run Invoice Batch
 * @DATE November 16, 2017
 */
public class LienNonPayDelinquencyCarriedForwardInsuredInvoice extends BaseTest {
	private WebDriver driver;
	private ARUsers arUser = new ARUsers();
	private GeneratePolicy myPolicyObj = null;
	private BCAccountMenu acctMenu;
	private List<Date> invoiceBilledDates ;
	private List<Date> invoiceDueDates;
	private AccountInvoices invoice;
	private AccountPaymentsPaymentRequests paymentRequests;
	private BCCommonDelinquencies  acctDelinquency;
	private String secondInvoiceNumber, thirdInvoiceNumber;

	@Test
	public void generatePolicy() throws Exception {

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		ArrayList<PLPolicyLocationProperty> locTwoPropertyList = new ArrayList<PLPolicyLocationProperty>();
		
		AdditionalInterest loc1Property1AdditionalInterest = new AdditionalInterest(ContactSubType.Company);
		loc1Property1AdditionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1Property1AdditionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		
		PLPolicyLocationProperty location1Property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		location1Property1.setBuildingAdditionalInterest(loc1Property1AdditionalInterest);
		locOnePropertyList.add(location1Property1);
		locationsList.add(new PolicyLocation(locOnePropertyList));
			
		PLPolicyLocationProperty location2Property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		locTwoPropertyList.add(location2Property1);
		locationsList.add(new PolicyLocation(locTwoPropertyList, new AddressInfo(true)));
		
		SquireLiability liabilitySection = new SquireLiability();
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;


        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withInsFirstLastName("DE6698", "LH Non Pay")
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withPaymentPlanType(PaymentPlanType.Monthly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);		
	}
	
	@Test(dependsOnMethods = { "generatePolicy" })
	public void makeInsuredDownPayment() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.squire.getPremium().getDownPaymentAmount(), myPolicyObj.squire.getPolicyNumber());
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "makeInsuredDownPayment" })
	public void makePaymentOnSecondInvoiceAndLetLienGoDelinquent() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();

        invoice = new AccountInvoices(driver);
		
		invoiceBilledDates = invoice.getListOfInvoiceDates();
		invoiceDueDates = invoice.getListOfDueDates();
		secondInvoiceNumber = invoice.getInvoiceNumber(invoiceBilledDates.get(1), invoiceDueDates.get(1), null,null);
		thirdInvoiceNumber = invoice.getInvoiceNumber(invoiceBilledDates.get(2), invoiceDueDates.get(2), null,null);
		
		ClockUtils.setCurrentDates(cf, invoiceBilledDates.get(1));
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Payment_Request);
		
		ClockUtils.setCurrentDates(cf, invoiceDueDates.get(1));
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Payment_Request);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Payment_Request);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Payment_Request);

		acctMenu.clickAccountMenuPaymentsPaymentRequests();
		paymentRequests= new AccountPaymentsPaymentRequests(driver);
		
		Assert.assertTrue(paymentRequests.verifyPaymentRequest(Status.Closed, null, null, null, null, null, secondInvoiceNumber, null, null, null) , "Request Payment request failed or was  ");
		
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		
		acctMenu.clickBCMenuDelinquencies();
        acctDelinquency = new BCCommonDelinquencies(driver);
		Assert.assertTrue(acctDelinquency.verifyDelinquencyByReason(OpenClosed.Open, DelinquencyReason.PastDueLienPartialCancel, null,DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter)), "Past Due Lien Partial Cancel has not been trigered . Test can't continue from here");
	}
	
	@Test(dependsOnMethods = { "makePaymentOnSecondInvoiceAndLetLienGoDelinquent" })
	public void billThirdInvoiceAndMakeSureItGetsBilledAndDue() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.accountNumber);
		
		ClockUtils.setCurrentDates(cf, invoiceBilledDates.get(2));
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Payment_Request);

        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
		
		Assert.assertTrue(invoice.verifyInvoice(invoiceBilledDates.get(2), invoiceDueDates.get(2), thirdInvoiceNumber, InvoiceType.Scheduled, null, InvoiceStatus.Billed, null,null), "Past Due Lien Partial Cancel has not been trigered . Test can't continue from here");
		
		ClockUtils.setCurrentDates(cf, invoiceDueDates.get(2));
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Payment_Request);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Payment_Request);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Payment_Request);
		
		Assert.assertTrue(paymentRequests.verifyPaymentRequest(Status.Closed, null, null, null, null, null, thirdInvoiceNumber, null, null, null) , "Payment request failed ");
		
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		acctMenu.clickAccountMenuInvoices();
		
		Assert.assertTrue(invoice.verifyInvoice(invoiceBilledDates.get(2), invoiceDueDates.get(2), thirdInvoiceNumber, InvoiceType.Scheduled, null, InvoiceStatus.Due, null, 0.00), "Past Due Lien Partial Cancel has not been trigered . Test can't continue from here");				
	}		
}
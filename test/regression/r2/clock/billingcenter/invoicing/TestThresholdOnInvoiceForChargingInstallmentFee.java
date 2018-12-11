package regression.r2.clock.billingcenter.invoicing;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

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
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author sgunda
 * @Requirement DE4912 Invoice fee should not affect billing threshold on any invoices
 * @Description Invoice fee should not be including in the calculation of thresholds on invoicing.
 * example : Schedule bill amount $6.00 below threshold of $9.99 - should not print invoice
 * Don't charge an installment fee if the invoice before the fee is added is less than $10. If an invoice fee is unpaid from a prior balance, that can be included.
 * This needs to be done at the invoicing level. Must consider amounts from prior invoices that are unpaid.
 * @DATE Apr 01, 2017
 */
public class TestThresholdOnInvoiceForChargingInstallmentFee extends BaseTest {
	private WebDriver driver;
	public ARUsers arUser;
	private BCAccountMenu acctMenu;
	private Date invoiceDate2,invoiceDate3;
	private AccountInvoices acctInvoice;
	private double payInvoiceAmt2, payInvoiceAmt3 , amtInvoice2,amtInvoice3, amtInvoiceDue2,chrgeFee = 10.01,noChrgeFee =10.00;
	private boolean foundInstallmentFee2 , foundInstallmentFee3;
	private SoftAssert softAssert;
	Underwriters uw;
	GeneratePolicy myPolicyObj;
	Date cancellationDate = null;
	
	@Test
	public void issuePolicy() throws Exception {
	    ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
	    ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
	    locOneBuildingList.add(new PolicyLocationBuilding());
	    locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

	    Config cf = new Config(ApplicationOrCenter.PolicyCenter);
	    driver = buildDriver(cf);

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
	        .withInsPersonOrCompany(ContactSubType.Company)
	        .withInsCompanyName("DE4912")
	        .withPolOrgType(OrganizationType.Partnership)
	        .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
	        .withPolicyLocations(locationsList)      
	        .withPaymentPlanType(PaymentPlanType.Quarterly)      
	        .withDownPaymentType(PaymentType.Cash)
	        .build(GeneratePolicyType.PolicyIssued);
        getQALogger().info(new GuidewireHelpers(driver).getCurrentPolicyType(myPolicyObj).toString());
	      
	  }
	
	@Test(dependsOnMethods = { "issuePolicy" })
	public void makeDownPayment() throws Exception {	 		 
				
		//make down Payment
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();

        NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
        directPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), myPolicyObj.busOwnLine.getPolicyNumber());
		
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
				
	}
	
	
	@Test(dependsOnMethods = { "makeDownPayment" })
	public void moveClockMakePaymentOnSecondInvoice() throws Exception {
		
				this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
				Config cf = new Config(ApplicationOrCenter.BillingCenter);
				driver = buildDriver(cf);
				new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        acctMenu = new BCAccountMenu(driver);
				acctMenu.clickAccountMenuInvoices();
				
				//Get Invoice Date and amount for second 
        acctInvoice = new AccountInvoices(driver);
				invoiceDate2 = acctInvoice.getListOfInvoiceDates().get(1);
				amtInvoice2= acctInvoice.getInvoiceAmountByRowNumber(2);
							
				payInvoiceAmt2=amtInvoice2-noChrgeFee;

        acctMenu.clickAccountMenuActionsNewDirectBillPayment();

				//Make payment so that $10.00 will left on second invoice 
        NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
				directPayment.makeDirectBillPaymentExecuteWithoutDistribution(payInvoiceAmt2 , myPolicyObj.accountNumber);
				
				
				//Moving clock to the next invoicing date
				ClockUtils.setCurrentDates(cf, invoiceDate2);
				new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);


        //Clicking here and there so that values on Inv tables get updated
				acctMenu.clickBCMenuCharges();
				acctMenu.clickAccountMenuInvoices();

        acctInvoice.clickRowByInvoiceDate(invoiceDate2);
        amtInvoiceDue2 = acctInvoice.getInvoiceDueAmountByRowNumber(2);

        foundInstallmentFee2 = acctInvoice.verifyInvoiceCharges(null, null, myPolicyObj.busOwnLine.getPolicyNumber(), ChargeCategory.Installment_Fee, TransactionType.General, null, null, null, 4.00, null, null, null);
				
				//making payment for second invoice so that this account doesn't go delinquent on next clock move 
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
				directPayment.makeDirectBillPaymentExecute(amtInvoiceDue2, myPolicyObj.accountNumber);
				
				ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Month, 1);
				new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
									
	}
	
	@Test(dependsOnMethods = { "moveClockMakePaymentOnSecondInvoice" })
	public void moveClockMakePaymentOnThirdInvoice() throws Exception {
		

		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
		
		//Get Invoice Date and amount for third invoice 
        acctInvoice = new AccountInvoices(driver);
		invoiceDate3 = acctInvoice.getListOfInvoiceDates().get(2);
		amtInvoice3= acctInvoice.getInvoiceAmountByRowNumber(3);
	
		payInvoiceAmt3=amtInvoice3-chrgeFee;

        acctMenu.clickAccountMenuActionsNewDirectBillPayment();

		// Make payment so that $10.01 will left on second invoice 
        NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
		directPayment.makeDirectBillPaymentExecuteWithoutDistribution(payInvoiceAmt3 , myPolicyObj.accountNumber);
		
		//Moving clock to the next invoicing date
		ClockUtils.setCurrentDates(cf, invoiceDate3);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        //Clicking here and there so that values on Inv tables get updated
		acctMenu.clickBCMenuCharges();
		acctMenu.clickAccountMenuInvoices();

        acctInvoice.clickRowByInvoiceDate(invoiceDate3);

        foundInstallmentFee3 = acctInvoice.verifyInvoiceCharges(null, null, myPolicyObj.busOwnLine.getPolicyNumber(), ChargeCategory.Installment_Fee, TransactionType.General, null, null, null, 4.00, null, null, null);
		System.out.println(foundInstallmentFee3); 

		
	}
	
	@Test(dependsOnMethods = { "moveClockMakePaymentOnThirdInvoice" })
	public void assertInstallmentFee() {
		
		softAssert = new SoftAssert();
		softAssert.assertFalse(foundInstallmentFee2, "Found Installment Fee this should not happend. Since amount left on this invoice is only $10 is should not charge the Installment Fee ");
		softAssert.assertTrue(foundInstallmentFee3, "Didn't find Installment Fee this should not happend. Since amount left on this invoice is more that $10 which is above the threshold it should charge Threshold Installment Fee");
		
		softAssert.assertAll();
		
	}
}

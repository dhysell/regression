package regression.r2.clock.billingcenter.delinquency;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.search.BCSearchAccounts;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactRole;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentReversalReason;
import repository.gw.enums.PaymentType;
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
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author SGUNDA
 * @Requirement DE6715 - When a payment reversal is made on a payment made before the invoice billed, it is not triggering delinquency
 * 1. Create an insured and lien paid quarterly policy. Pay off the lien. Pay the insured's down, make it billed and due.
 * 2. Before the next insured invoice goes billed, make a payment equal to invoice amount (It could be a transfer from another policy as well)
 * 3. Make the next invoice billed and due, there will be no delinquency
 * 4. Now make a payment reversal on the latest insured payment.
 * Actual: No delinquency is triggered, and the unpaid due invoice is carried forward
 * Expected: Now that the due invoice is unpaid, it should trigger delinquency (Past Due Partial in this scenario)
 * @DATE November 28, 2017
 */
public class PaymentReversalOnPaymentMadeBeforeInvoiceBilledIsNotTriggeringDelinquency extends BaseTest {
	private WebDriver driver;
	private ARUsers arUser = new ARUsers();
	private GeneratePolicy myPolicyObj = null;
	private ArrayList<AdditionalInterest> loc1Bldg2AdditionalInterests = new ArrayList <AdditionalInterest>();
	private ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
	private ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();

	private String lienholderNumber = null;
	private String lienholderLoanNumber = null;
	private double lienholderLoanPremiumAmount;
	private double secondInvoiceAmt;
	private String secondInvoiceNumber;
	private PaymentPlanType paymentPlan;

	private BCAccountMenu acctMenu;
	private List<Date> invoiceDueDates;
	private AccountInvoices invoice;
	
	private void gotoAccount(String accountNumber){
		BCSearchAccounts search= new BCSearchAccounts(driver);
		search.searchAccountByAccountNumber(accountNumber);	
	}

	@Test
	public void generatePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);
		
		int yearTest = DateUtils.getCenterDateAsInt(cf, ApplicationOrCenter.PolicyCenter, "yyyy") - NumberUtils.generateRandomNumberInt(0, 50);
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_MONTH);
		if (day % 2 == 0 ) {
				paymentPlan = PaymentPlanType.Monthly;
		} else {paymentPlan = PaymentPlanType.Quarterly;
	       }
			
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setYearBuilt(yearTest);
		loc1Bldg1.setClassClassification("storage");
		
		PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
		loc1Bldg2.setYearBuilt(2010);
		loc1Bldg2.setClassClassification("storage");
		
		
		ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
		rolesToAdd.add(ContactRole.Lienholder);

		AddressInfo addIntTest = new AddressInfo();
		addIntTest.setLine1("PO Box 711");
		addIntTest.setCity("Pocatello");
		addIntTest.setState(State.Idaho);
		addIntTest.setZip("83204");//-0711
		
		ArrayList<AddressInfo> addressInfoList = new ArrayList<AddressInfo>();
		addressInfoList.add(addIntTest);
		GenerateContact myContactLienLoc1Obj = new GenerateContact.Builder(driver)
				.withCompanyName("Lienholder")
				.withRoles(rolesToAdd)
				.withGeneratedLienNumber(true)
				.withUniqueName(true)
				.withAddresses(addressInfoList)
				.build(GenerateContactType.Company);	
		
		driver.quit();
		
		AdditionalInterest loc1Bld2AddInterest = new AdditionalInterest(myContactLienLoc1Obj.companyName, addIntTest);	
		loc1Bld2AddInterest.setNewContact(CreateNew.Create_New_Always);
		loc1Bld2AddInterest.setAddress(addIntTest);
		loc1Bld2AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1Bldg2AdditionalInterests.add(loc1Bld2AddInterest);
		loc1Bldg2.setAdditionalInterestList(loc1Bldg2AdditionalInterests);
				
		this.locOneBuildingList.add(loc1Bldg1);
		this.locOneBuildingList.add(loc1Bldg2);
		this.locationsList.add(new PolicyLocation(new AddressInfo(), this.locOneBuildingList));

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("DE6715 Insured")
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolOrgType(OrganizationType.Partnership)
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(paymentPlan)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		
		this.lienholderNumber = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNumber();
		this.lienholderLoanNumber = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLoanContractNumber();
		this.lienholderLoanPremiumAmount = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();
	
	}
	
	@Test(dependsOnMethods = { "generatePolicy" })
	public void makeInsuredDownPayment() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), myPolicyObj.busOwnLine.getPolicyNumber());
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "makeInsuredDownPayment" })
	public void makePaymentOnSecondInvoiceAndLien() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();

        invoice = new AccountInvoices(driver);
		
		invoiceDueDates = invoice.getListOfDueDates();

		secondInvoiceNumber = invoice.getInvoiceNumber(null, invoiceDueDates.get(1), null,null);

		secondInvoiceAmt=invoice.getInvoiceAmountByRowNumber(2);
		acctMenu.clickBCMenuSummary();
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeDirectBillPaymentExecuteWithoutDistribution(secondInvoiceAmt + 1.02, myPolicyObj.busOwnLine.getPolicyNumber());
		
		gotoAccount(this.lienholderNumber);		
	
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 25);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        newPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPolicyNumber(), this.lienholderLoanNumber, this.lienholderLoanPremiumAmount);

		ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(invoiceDueDates.get(1), DateAddSubtractOptions.Day, 1));
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
	}
	
	@Test(dependsOnMethods = { "makePaymentOnSecondInvoiceAndLien" })
	public void billThirdInvoiceAndMakeSureItGetsBilledAndDue() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.accountNumber);

        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuPayments();
        AccountPayments payments = new AccountPayments(driver);
		acctMenu.clickAccountMenuPaymentsPayments();
	
		payments.reversePayment(secondInvoiceAmt+1.02, secondInvoiceAmt, 1.02, PaymentReversalReason.Payment_Moved);
		
		gotoAccount(myPolicyObj.accountNumber);	
		
		acctMenu.clickAccountMenuInvoices();
        invoice = new AccountInvoices(driver);
		boolean didInvoiceCarriedForward = invoice.verifyInvoice(null, invoiceDueDates.get(1), secondInvoiceNumber, null, null, InvoiceStatus.Carried_Forward, null, null);

		acctMenu.clickBCMenuDelinquencies();
        BCCommonDelinquencies del = new BCCommonDelinquencies(driver);
		boolean wasDelinquencyTriggered = del.verifyDelinquencyByReason(OpenClosed.Open, DelinquencyReason.PastDuePartialCancel,null,DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter));
		if(didInvoiceCarriedForward || !wasDelinquencyTriggered){
			Assert.fail("This test failed to pass . Did invoice carried forward = "+didInvoiceCarriedForward+"(Should not carry forward) . Was delinquency triggered = " + wasDelinquencyTriggered +" (Should trigger delinquency)");
		}
		
	}
		
}
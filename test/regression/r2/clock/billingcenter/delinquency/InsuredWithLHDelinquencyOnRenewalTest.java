package regression.r2.clock.billingcenter.delinquency;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.CarryOverCharge;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestSubType;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
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
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.RenewalCode;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.TransactionNumber;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PLPropertyCoverages;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAccountsPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.enums.Underwriter.UnderwriterLine;
import persistence.enums.Underwriter.UnderwriterTitle;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
* @Author jqu
* @Description   US13897 -- Insured & Lien Renewal 
* 				  Shortage invoice not paid by insured (only carry over) after cancellation
				  Carry over is created after cancellation.  Test both non-pay and PC cancels 					
* @DATE March 30, 2018
*/
public class InsuredWithLHDelinquencyOnRenewalTest extends BaseTest{
	private GeneratePolicy myPolicyObj;
	private ARUsers arUser = new ARUsers();
	private BCAccountMenu accountMenu; 
	private AccountInvoices invoice;
	private AccountCharges charge;
	private String loanNumber = "LN12345";
	private String LHNumber;
	private double LHPremium;
	private String LHName;
	private double carryOverAmt = NumberUtils.generateRandomNumberInt(100, 150);
	private Date carryOverDueDate;
	private WebDriver driver;
	
	private void makePayment(BCAccountMenu accountMenu, double amount, String loanNumber, String accountNumber){
		accountMenu.clickAccountMenuActionsNewDirectBillPayment();
		NewDirectBillPayment payment = new NewDirectBillPayment(driver);
		if(loanNumber!= null){
			payment.makeDirectBillPaymentExecute(accountNumber, loanNumber, amount);	
		}else{
			payment.makeDirectBillPaymentExecute(amount, accountNumber);
		}
	}
	@Test
	public void generate() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		AdditionalInterest additionalInterest = new AdditionalInterest(ContactSubType.Company);
		additionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		additionalInterest.setAdditionalInterestSubType(AdditionalInterestSubType.PLSectionIProperty);
		additionalInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);	
		additionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		additionalInterest.setLoanContractNumber(loanNumber);
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		
		PLPolicyLocationProperty location1Property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		location1Property1.setBuildingAdditionalInterest(additionalInterest);
		
		PLPolicyLocationProperty location1Property2 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);		
		PLPropertyCoverages propertyCoverages = new PLPropertyCoverages();
		propertyCoverages.getCoverageA().setLimit(300);
		location1Property2.setPropertyCoverages(propertyCoverages);
		locOnePropertyList.add(location1Property1);
		locOnePropertyList.add(location1Property2);		
        		
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(18);
		locationsList.add(locToAdd);

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_500000_CSL);

		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;
		myPropertyAndLiability.liabilitySection = myLiab;

		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.propertyAndLiability = myPropertyAndLiability;

		this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("Dues", "Delinquency")
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withPolTermLengthDays(50)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);			

		LHNumber = myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
		LHPremium = myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();
		LHName = myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getCompanyName();
	}
	@Test(dependsOnMethods = { "generate" })	
	public void payInsuredAndLH() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);			
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
		accountMenu = new BCAccountMenu(driver);
		makePayment(accountMenu, myPolicyObj.squire.getPremium().getDownPaymentAmount(), null, myPolicyObj.accountNumber);
		
		//pay LH
		SearchAccountsPC search = new SearchAccountsPC(driver);
		search.searchAccountByAccountNumber(LHNumber);		
		makePayment(accountMenu, LHPremium, loanNumber, myPolicyObj.accountNumber);
	}
	@Test(dependsOnMethods = { "payInsuredAndLH" })
	public void manuallyRenewPolicy() throws Exception{
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(UnderwriterLine.Personal, UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolicyObj.squire.getPolicyNumber());		
        StartRenewal renewalWorkflow = new StartRenewal(driver);
		renewalWorkflow.renewPolicyManually(RenewalCode.Renew_Good_Risk, myPolicyObj);
		new GuidewireHelpers(driver).logout();
	}
	//not pay insured renewal down to trigger Not taken partial
	@Test(dependsOnMethods = { "manuallyRenewPolicy" })	
	public void triggerNotTakePartial() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);		
		accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();
		charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArrive(TransactionType.Renewal);
		
		//move clock to day 46, pay LH
		Date LHRenewalDownInvDate = DateUtils.dateAddSubtract(myPolicyObj.squire.getExpirationDate(), DateAddSubtractOptions.Day, -46);
		ClockUtils.setCurrentDates(driver, LHRenewalDownInvDate);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
		SearchAccountsPC search = new SearchAccountsPC(driver);
		search.searchAccountByAccountNumber(LHNumber);
		accountMenu.clickAccountMenuInvoices();
		invoice = new AccountInvoices(driver);
		//since it's short term policy, the renewal down != current term down payment
		double LHRenewalDown = invoice.getInvoiceAmountByInvoiceDate(LHRenewalDownInvDate);		
		makePayment(accountMenu, LHRenewalDown, loanNumber, myPolicyObj.accountNumber);
		
		//move to day 21 run invoice
		ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(myPolicyObj.squire.getExpirationDate(), DateAddSubtractOptions.Day, -21));
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
		//move to expiration date run invoice due
		ClockUtils.setCurrentDates(driver, myPolicyObj.squire.getExpirationDate());
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);
		//verify delinquency
		search.searchAccountByAccountNumber(myPolicyObj.accountNumber);
		accountMenu.clickBCMenuDelinquencies();
		BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		if(!delinquency.verifyDelinquencyExists(OpenClosed.Open, DelinquencyReason.NotTakenPartial, myPolicyObj.accountNumber, null, myPolicyObj.accountNumber, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), null, null)){		
			Assert.fail("Didn't find the Not Taken Partial delinquency");
		}		
	}	
	@Test(dependsOnMethods = { "triggerNotTakePartial" })
	public void cancelInsured() throws Exception{
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.accountNumber);
        StartPolicyChange change = new StartPolicyChange(driver);
        change.removeBuildingAndChangePayer(myPolicyObj, 2, true, LHName);
	}
	@Test(dependsOnMethods = { "cancelInsured" })	
	public void createCarryOverOnInsured() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		
		accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();
		charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArrive(60, TransactionType.Policy_Change);

		charge.clickCarryOverCharge(myPolicyObj.accountNumber, TransactionNumber.Premium_Charged, TransactionType.Issuance, myPolicyObj.accountNumber);
		CarryOverCharge carryoverCharge = new CarryOverCharge(driver);
		carryoverCharge.createCarryOverCharge(carryOverAmt);
		//verify the carry over on insured
		carryOverDueDate = DateUtils.dateAddSubtract(myPolicyObj.squire.getExpirationDate(), DateAddSubtractOptions.Month, 1);
		
		accountMenu.clickAccountMenuInvoices();
		invoice = new AccountInvoices(driver);
		if(!invoice.verifyInvoice(DateUtils.dateAddSubtract(carryOverDueDate, DateAddSubtractOptions.Day, -20), carryOverDueDate, null, InvoiceType.Shortage, null, InvoiceStatus.Planned, carryOverAmt, carryOverAmt)){		
			Assert.fail(" Didn't find the Carry Over invoice.");
		}		
	}	
	@Test(dependsOnMethods = { "createCarryOverOnInsured" })	
	public void verifyDelinquency() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), LHNumber);
		//pay Lien holder
		accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuInvoices();
		invoice = new AccountInvoices(driver);
		double LHShortageCharge = invoice.getInvoiceDueAmountByDueDate(carryOverDueDate);		
		makePayment(accountMenu, LHShortageCharge, loanNumber, myPolicyObj.accountNumber);		
		
		//trigger Non-premium delinquency and verify
		SearchAccountsPC search = new SearchAccountsPC(driver);
		search.searchAccountByAccountNumber(myPolicyObj.accountNumber);

		ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(carryOverDueDate, DateAddSubtractOptions.Day, -20));
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
		ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(carryOverDueDate, DateAddSubtractOptions.Day, 1));
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);
		accountMenu.clickBCMenuDelinquencies();
		BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		if(!delinquency.verifyDelinquencyExists(OpenClosed.Open, DelinquencyReason.NonPremiumDelinquency, myPolicyObj.accountNumber, null, myPolicyObj.accountNumber, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), carryOverAmt, carryOverAmt)){		
			Assert.fail("Didn't find the Non-Premium Delinquency delinquency");
		}		
	}
}

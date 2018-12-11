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
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.TransactionNumber;
import repository.gw.enums.TransactionType;
import repository.gw.enums.TrueFalse;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
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
* @Author jqu
* @Description   US13897 -- Insured & Lien issuance 
* 				  Shortage invoice not paid by insured (only carry over) triggers Non Premium delinquency
 					
* @DATE March 21, 2018
*/
public class CarryOverNonPayTriggerNoPremiumDelinquencyTest extends BaseTest{
	private GeneratePolicy myPolicyObj;
	private ARUsers arUser = new ARUsers();
	private String loanNumber = "LN12345";
	private String LHNumber;
	private double LHPremium;
	private double carryOverAmt = NumberUtils.generateRandomNumberInt(100, 150);
	private Date carryOverDueDate;
	private WebDriver driver;
	
	@Test
	public void generate() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
		AdditionalInterest additionalInterest = new AdditionalInterest(ContactSubType.Company);
		additionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_All);
		additionalInterest.setAdditionalInterestSubType(AdditionalInterestSubType.PLSectionIProperty);
		additionalInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);	
		additionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		additionalInterest.setLoanContractNumber(loanNumber);		
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		
		PLPolicyLocationProperty location1Property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		location1Property1.setBuildingAdditionalInterest(additionalInterest);
		locOnePropertyList.add(location1Property1);

        StandardFireAndLiability myStandardFire = new StandardFireAndLiability();
        myStandardFire.setLocationList(locationsList);
        myStandardFire.section1Deductible = SectionIDeductible.FiveHundred;			
        cf.setCenter(ApplicationOrCenter.PolicyCenter);			

		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(8);
		locationsList.add(locToAdd);

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;
		myPropertyAndLiability.liabilitySection = myLiab;

		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.propertyAndLiability = myPropertyAndLiability;

		this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("SquireWith", "StdFire")
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);			

		LHNumber = this.myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
		LHPremium = myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();		
	}
	@Test(dependsOnMethods = { "generate" })	
	public void createCarryOverOnInsured() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
		AccountCharges charge = new AccountCharges(driver);
		charge.clickCarryOverChargeAndCreateCharge(LHNumber, TransactionNumber.Premium_Charged, TransactionType.Policy_Issuance, myPolicyObj.accountNumber, TrueFalse.True, carryOverAmt);
		
		//verify the carry over on insured
		carryOverDueDate = DateUtils.dateAddSubtract(myPolicyObj.squire.getEffectiveDate(), DateAddSubtractOptions.Month, 1);
		acctMenu.clickAccountMenuInvoices();
		AccountInvoices invoice = new AccountInvoices(driver);
		if(!invoice.verifyInvoice(DateUtils.dateAddSubtract(carryOverDueDate, DateAddSubtractOptions.Day, myPolicyObj.squire.getPaymentPlanType().getInvoicingLeadTime()), carryOverDueDate, null, InvoiceType.Shortage, null, InvoiceStatus.Planned, carryOverAmt, carryOverAmt)){		
			Assert.fail(" Didn't find the Carry Over invoice.");
		}		
	}
	@Test(dependsOnMethods = { "createCarryOverOnInsured" })	
	public void payLHAndMoveClock() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), LHNumber);
		BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuActionsNewDirectBillPayment();
		NewDirectBillPayment payment = new NewDirectBillPayment(driver);
		payment.makeLienHolderPaymentExecute(LHPremium, myPolicyObj.accountNumber, loanNumber);
		ClockUtils.setCurrentDates(driver, carryOverDueDate);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);		
	}
	@Test(dependsOnMethods = { "payLHAndMoveClock" })	
	public void verifyDelinquency() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuDelinquencies();
		BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		delinquency.verifyDelinquencyExists(OpenClosed.Open, DelinquencyReason.NonPremiumDelinquency, myPolicyObj.accountNumber, null, myPolicyObj.accountNumber, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), carryOverAmt, carryOverAmt);
	}
}

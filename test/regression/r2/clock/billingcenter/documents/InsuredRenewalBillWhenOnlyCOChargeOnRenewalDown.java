package regression.r2.clock.billingcenter.documents;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDocuments;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestSubType;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.RenewalCode;
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
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
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
* @Author JQU
* @Requirement 	DE6773 - Insured Renewal Bill is not being printed if insured has only carryover charges on renewal down
* 				STEPS : Create a fully lien paid policy. 
				Pay the lien charge
				Move to renewal day and get renewal on the policy 
				Move to renewal window and create/send a carryover charge on insured.
				Now the renewal down will only have carryover charge.
				Move to the invoice day of the renewal down and make it bill.
				Actual: Insured Renewal Bill is not being generated.
				Expected: Insured Renewal Bill should be generated.

* @DATE April 06, 2018
*/
public class InsuredRenewalBillWhenOnlyCOChargeOnRenewalDown extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();
	private int carryOverAmt = NumberUtils.generateRandomNumberInt(20, 100);
	private String loanNumber="LN12345";
	private Date expirationDate;
	private String LHNumber;
	private double LHPremium;
	
	@Test 
	public void generatePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		expirationDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 50);
		DateUtils.dateAddSubtract(expirationDate, DateAddSubtractOptions.Year, -1);
		
		
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
				.withInsFirstLastName("RenewalBill", "CO")
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withPolTermLengthDays(50)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);			

		LHNumber = myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
		LHPremium = myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();
	}
	@Test(dependsOnMethods = { "generatePolicy" })	
	public void payLH() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), LHNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment payment = new NewDirectBillPayment(driver);
		payment.makeLienHolderPaymentExecute(LHPremium, myPolicyObj.accountNumber, loanNumber);		
	}
	@Test(dependsOnMethods = { "payLH" })
	public void manuallyRenewPolicy() throws Exception{
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(UnderwriterLine.Personal, UnderwriterTitle.Underwriter);
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolicyObj.squire.getPolicyNumber());
        StartRenewal renewalWorkflow = new StartRenewal(driver);
		renewalWorkflow.renewPolicyManually(RenewalCode.Renew_Good_Risk, myPolicyObj);
		new GuidewireHelpers(driver).logout();
	}
	@Test(dependsOnMethods = { "manuallyRenewPolicy" })
    public void createCarryOverOnInsuredAndVerifyBill() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
        AccountCharges charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArrive(90, TransactionType.Renewal);
		charge.clickCarryOverChargeAndCreateCharge(LHNumber, TransactionNumber.Premium_Charged, TransactionType.Policy_Issuance, myPolicyObj.accountNumber, TrueFalse.True, carryOverAmt);
		
		//verify the carry over on insured
		Date carryOverInvoiceDate = DateUtils.dateAddSubtract(myPolicyObj.squire.getExpirationDate(), DateAddSubtractOptions.Day, -21);
        acctMenu.clickAccountMenuInvoices();
        AccountInvoices invoice = new AccountInvoices(driver);
		if(!invoice.verifyInvoice(carryOverInvoiceDate, null, null, InvoiceType.RenewalDownPayment, null, InvoiceStatus.Planned, (double)carryOverAmt, (double)carryOverAmt)){		
			Assert.fail(" Didn't find the Carry Over invoice.");
		}
		//trigger document and verify
		ClockUtils.setCurrentDates(cf, carryOverInvoiceDate);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		acctMenu.clickBCMenuDocuments();
        BCCommonDocuments document = new BCCommonDocuments(driver);
		if(!document.verifyDocument("Insured Renewal Bill", null, null, null, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null)){
			Assert.fail("Didn't find the correct document.");
		}
	}
}

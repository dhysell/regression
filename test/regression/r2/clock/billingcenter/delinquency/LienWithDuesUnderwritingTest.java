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
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestSubType;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.TransactionNumber;
import repository.gw.enums.TransactionType;
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
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.cancellation.StartCancellation;
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
* @Requirement 	US13897 (US14910) - Flat cancellation of policy				

* @DATE April 19, 2018
*/
public class LienWithDuesUnderwritingTest extends BaseTest {	
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();
	private String loanNumber="LN12345";
	private String LHNumber;
	private double LHPremium;
	private Date LHDownDueDate;
	private WebDriver driver;
	
	@Test 
	public void generatePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		AdditionalInterest additionalInterest = new AdditionalInterest(ContactSubType.Company);
		additionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_All);
		additionalInterest.setAdditionalInterestSubType(AdditionalInterestSubType.PLSectionIProperty);
		additionalInterest.setNewContact(CreateNew.Create_New_Always);	
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
				.withInsFirstLastName("RenewalBill", "CO")
				.withPaymentPlanType(PaymentPlanType.Annual)				
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);			

		LHNumber = myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
		LHPremium = myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();
		LHDownDueDate = DateUtils.dateAddSubtract(myPolicyObj.squire.getEffectiveDate(), DateAddSubtractOptions.Month, 1);
	}
	@Test(dependsOnMethods = { "generatePolicy" })	
	public void moveClock() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), LHNumber);			
		BCAccountMenu accounttMenu = new BCAccountMenu(driver);
		accounttMenu.clickBCMenuCharges();
		AccountCharges charge = new AccountCharges(driver);		
		charge.getChargeAmount(LHNumber, TransactionNumber.Membership_Dues_Charged);
		ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(LHDownDueDate, DateAddSubtractOptions.Day, -20));
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
	}
	@Test(dependsOnMethods = { "moveClock" })
	public void flatCancelPolicy() throws Exception{
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(UnderwriterLine.Personal, UnderwriterTitle.Underwriter);
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolicyObj.squire.getPolicyNumber());		
        StartCancellation cancellation = new StartCancellation(driver);
        cancellation.cancelPolicy(CancellationSourceReasonExplanation.OtherPolicyRewrittenOrReplaced, "Flat Cancel", DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), true);
		new GuidewireHelpers(driver).logout();
	}
	@Test(dependsOnMethods = { "flatCancelPolicy" })
	public void verifyDuesGetPaidFirst() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), LHNumber);
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();
		AccountCharges charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArrive(90, TransactionType.Cancellation);		
		
		//verify that dues and premium are paid by credit
		accountMenu.clickAccountMenuInvoices();
		AccountInvoices invoices = new AccountInvoices(driver);	
		if(!invoices.verifyInvoice(DateUtils.dateAddSubtract(LHDownDueDate, DateAddSubtractOptions.Day, -20), LHDownDueDate, null, InvoiceType.NewBusinessDownPayment, null, null, LHPremium, 0.0)){	
			Assert.fail("Credit didn't pay dues first");
		}
	}
}

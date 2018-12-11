package regression.r2.clock.billingcenter.cancel;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.policy.BCPolicyMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ChargeCategory;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.TransactionNumber;
import repository.gw.enums.TransactionType;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.cancellation.StartCancellation;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
/**
* @Author JQU
* @Requirement US13897 --Cancellation of policy leaving some charges unpaid - Cancellation credit (may or may not) pay off dues and portion of premium leaving some unpaid charges. (Dues would most likely be paid off by money received)
* * 
* @DATE Mar 20, 2018
*/
public class InsuredWithDuesUnderwritingTest extends BaseTest{
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();
	private Date currentDate;
	private WebDriver driver;
	
	@Test
	public void generatePolicy() throws GuidewireException, Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K,
				MedicalLimit.TenK, true, UninsuredLimit.CSL300K, false, UnderinsuredLimit.CSL300K);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);

		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;
		myPropertyAndLiability.liabilitySection = myLiab;

		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.propertyAndLiability = myPropertyAndLiability;
		
		myPolicyObj = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withPolicyLocations(locationsList)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.Membership)
				.withInsFirstLastName("UW", "InsuredDues")
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);		
	}
	@Test(dependsOnMethods = { "generatePolicy" })
	public void runBatchprocessesAndMoveClock() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.squire.getPolicyNumber());
		BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
		policyMenu = new BCPolicyMenu(driver);
		policyMenu.clickBCMenuTroubleTickets();
		
		BCCommonTroubleTickets tt=new BCCommonTroubleTickets(driver);
		if(!tt.waitForTroubleTicketsToArrive(90)){
			throw new GuidewireException("BillingCenter: ",	"doesn't find the Promised Payment Trouble Ticket.");
		}
		policyMenu.clickTopInfoBarAccountNumber();		
		
		BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
		currentDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter),	DateAddSubtractOptions.Day, 10);		
		ClockUtils.setCurrentDates(driver, currentDate);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);
	}
	@Test(dependsOnMethods = { "runBatchprocessesAndMoveClock" })
	public void cancelPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.accountNumber);
        StartCancellation cancellation = new StartCancellation(driver);
		cancellation.cancelPolicy(CancellationSourceReasonExplanation.OtherInsuredRequest, "Cancel the policy", currentDate, true);
	}
	@Test(dependsOnMethods = { "cancelPolicy" })
	public void verifyDuesGetPaidFirst() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
		AccountCharges charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArrive(90, TransactionType.Cancellation);
		//myPolicyObj.squire.getPremium().getMembershipDuesAmount() method returns double dues amount, so get the dues amount from bc/charges
		double duesAmount = charge.getChargeAmount(myPolicyObj.accountNumber, TransactionNumber.Membership_Dues_Charged); 
		//verify UW delinquency
		acctMenu.clickBCMenuDelinquencies();
		BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		if(!delinquency.verifyDelinquencyByReason(OpenClosed.Open, DelinquencyReason.UnderwritingFullCancel, null,DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter))){
			Assert.fail("Didn't find the correct delinquency.");
		}
		//verify that dues are paid first
		acctMenu.clickAccountMenuInvoices();
		AccountInvoices invoices = new AccountInvoices(driver);
		
//		if(!invoices.verifyInvoiceCharges(null, myPolicyObj.squire.getEffectiveDate(), null, ChargeCategory.Membership_Dues, null, null, null, null, myPolicyObj.squire.getPremium().getMembershipDuesAmount(), myPolicyObj.squire.getPremium().getMembershipDuesAmount(), null, null)){
		if(!invoices.verifyInvoiceCharges(null, myPolicyObj.squire.getEffectiveDate(), null, ChargeCategory.Membership_Dues, null, null, null, null, duesAmount, duesAmount, null, null)){	
			Assert.fail("Credit didn't pay dues first");
		}
	}
}

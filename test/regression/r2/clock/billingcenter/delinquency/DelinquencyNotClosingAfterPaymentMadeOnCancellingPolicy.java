package regression.r2.clock.billingcenter.delinquency;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.policy.BCPolicyMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.cancellation.StartCancellation;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author JQU
* @Requirement DE6166 - delinquency not closing after payment made on cancelling policy
* 				Create city squire auto only policy
				move the clock a day
				cancel policy 
				delinquency was created for the earned premium and also for the membership dues.
				paid the balance left owing on the policy.
				move a day
				run invoice and invoice due
				Defect: delinquency DID NOT  close even though it was paid
* 
* @DATE September 11, 2017*/
public class DelinquencyNotClosingAfterPaymentMadeOnCancellingPolicy extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObjPL = null;
	private ARUsers arUser = new ARUsers();
	
	@Test
    public void generatePolicy() throws Exception {
		// Coverages
		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.OneHundredHigh, MedicalLimit.FiveK, true, UninsuredLimit.OneHundred, true, UnderinsuredLimit.OneHundred);
		coverages.setAccidentalDeath(true);

		// Vehicle
		ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
		Vehicle newVeh = new Vehicle();
		newVeh.setEmergencyRoadside(true);
		vehicleList.add(newVeh);

		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);
		squirePersonalAuto.setVehicleList(vehicleList);

        Squire mySquire = new Squire();
        mySquire.squirePA = squirePersonalAuto;

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withPaymentPlanType(PaymentPlanType.Annual)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
				.withInsFirstLastName("CitySquire", "AutoOnly")
				.build(GeneratePolicyType.PolicyIssued);	
	}
	
	@Test(dependsOnMethods = { "generatePolicy" })
	public void runBatchProcessAndMoveClock() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObjPL.accountNumber);
        //wait for TT to come
        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
		policyMenu.clickBCMenuTroubleTickets();
        BCCommonTroubleTickets TT = new BCCommonTroubleTickets(driver);
		TT.waitForTroubleTicketsToArrive(60);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		//move clock one day and make down payment Due
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		//verify Down Payment is due
		policyMenu.clickTopInfoBarAccountNumber();
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
        AccountInvoices invoice = new AccountInvoices(driver);
        invoice.verifyInvoice(myPolicyObjPL.squire.getEffectiveDate(), myPolicyObjPL.squire.getEffectiveDate(), null, InvoiceType.NewBusinessDownPayment, null, InvoiceStatus.Due, myPolicyObjPL.squire.getPremium().getDownPaymentAmount(), myPolicyObjPL.squire.getPremium().getDownPaymentAmount());
	}
	@Test(dependsOnMethods = { "runBatchProcessAndMoveClock" })
    public void CancelPolicyInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObjPL.squire.getPolicyNumber());
        StartCancellation cancelPol = new StartCancellation(driver);
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.OtherInsuredRequest, "Cancel Policy", null,true);		
		new GuidewireHelpers(driver).logout();
	}
	@Test(dependsOnMethods = { "CancelPolicyInPolicyCenter" })
	public void verifyInBillingCenter() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObjPL.accountNumber);
		//wait for Cancellation to come
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
        AccountCharges charges = new AccountCharges(driver);
		charges.waitUntilChargesFromPolicyContextArrive(TransactionType.Cancellation);
		//verify shortage invoice
		acctMenu.clickAccountMenuInvoices();
        AccountInvoices invoice = new AccountInvoices(driver);
		invoice.verifyInvoice(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), null, InvoiceType.Shortage, null, InvoiceStatus.Planned, null, null);
		//verify delinquency and get delinquent amount
		acctMenu.clickBCMenuDelinquencies();
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		delinquency.verifyDelinquencyExists(OpenClosed.Open, DelinquencyReason.UnderwritingFullCancel, myPolicyObjPL.accountNumber, null, myPolicyObjPL.accountNumber, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), null, null);
		double delinquencyAmount=delinquency.getDelinquentAmount(OpenClosed.Open, myPolicyObjPL.accountNumber, myPolicyObjPL.accountNumber, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter));
		//pay the delinquent amount and verify
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment payment = new NewDirectBillPayment(driver);
		payment.makeDirectBillPaymentExecute(delinquencyAmount, myPolicyObjPL.accountNumber);		
		acctMenu.clickBCMenuDelinquencies();
		delinquency.verifyDelinquencyExists(OpenClosed.Closed, DelinquencyReason.UnderwritingFullCancel, myPolicyObjPL.accountNumber, null, myPolicyObjPL.accountNumber, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), delinquencyAmount, 0.0);		
	}	
}

package regression.r2.clock.billingcenter.other;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountDisbursements;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.payments.AccountPayments;
import repository.bc.account.payments.AccountPaymentsCreditDistributions;
import repository.bc.common.BCCommonSummary;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.actions.DesktopActionsMultiplePayment;
import repository.bc.search.BCSearchAccounts;
import repository.bc.topmenu.BCTopMenu;
import repository.bc.wizards.NewTroubleTicketWizard;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DisbursementStatus;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.Priority;
import repository.gw.enums.TroubleTicketType;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author jqu
 * @Description: The test verifies Charges Payers with the Policy Object. It also verifies the Multiple Payment for the account with Null-TargetDate Trouble Ticket.
 * It then verifies that Billing Manager has the permission to modify the Due Date of a Disbursement with "Awaiting Approval" status.
 * @RequirementsLink None
 * @DATE Oct 21, 2015
 */
@QuarantineClass
public class ChargesPayerAndMultiPaymentWithNullTargetDateTroubleTicketTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private BCAccountMenu acctMenu;	
	private ARUsers arUser = new ARUsers();
	private double extraAmount=100;
	private double amtToPay;
	private String lienholderNumber;

	@Test
	public void Generate() throws Exception {

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		locOneBuildingList.add(new PolicyLocationBuilding());
		
		ArrayList<AdditionalInterest> loc1LNBldg2AdditionalInterests = new ArrayList<AdditionalInterest>();
		//create random additional interest
		AdditionalInterest loc1LNBldg2AddInterest = new AdditionalInterest();		
		loc1LNBldg2AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1LNBldg2AdditionalInterests.add(loc1LNBldg2AddInterest);
		PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
		loc1Bldg2.setAdditionalInterestList(loc1LNBldg2AdditionalInterests);
		locOneBuildingList.add(loc1Bldg2);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));					

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver).withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("TestChargePayers_NullTargetDateTroubleTicket_Disbursement")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.SelfStorageFacilities))
				.withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.getRandom())
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		lienholderNumber=myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNumber();
        amtToPay = myPolicyObj.busOwnLine.getPremium().getInsuredPremium() + extraAmount;
		//sometimes myPolicyObj.fullAccountNumber returns null
		System.out.println("the fullAccountNumber is "+ myPolicyObj.fullAccountNumber);
	}

	@Test(dependsOnMethods = { "Generate" })	
	public void verifyPayforCharges() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
		AccountCharges acctChg = new AccountCharges(driver);
		boolean found = acctChg.verifyChargesMatchPayers(myPolicyObj);
		if (!found) {
			Assert.fail("The verification step on the charges page in Billing Center failed. One or more charges did not match what was expected.");
		}
	}
	
	@Test(dependsOnMethods = { "verifyPayforCharges" })		
	public void runBatchProcessesAndMoveClock() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);		
	}
	
	@Test(dependsOnMethods = { "runBatchProcessesAndMoveClock" })	
	public void createTroubleTicketWithTargetAsNull() throws Exception {
		String msg="This account has 2 open trouble tickets";
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuTroubleTickets();
        BCCommonTroubleTickets myTT = new BCCommonTroubleTickets(driver);
		myTT.clickNewButton();
		NewTroubleTicketWizard newTT = new NewTroubleTicketWizard(driver);
        newTT.createNewTroubleTicket(TroubleTicketType.PromisedPayment, Priority.High, DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, 10), myPolicyObj.accountNumber);
		acctMenu.clickBCMenuSummary();
		BCCommonSummary acctSum = new BCCommonSummary(driver);
		if(!acctSum.getTroubleTicketAlertMessage().contains(msg)){
			throw new GuidewireException("BillingCenter", "should have two open trouble tickets");
		}
	}
	
	@Test(dependsOnMethods = { "createTroubleTicketWithTargetAsNull" })	
	public void makeMultiplePaymentAndVerify() throws Exception {			
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());		
		BCTopMenu topMenu=new BCTopMenu(driver);
		topMenu.clickDesktopTab();
		BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuActionsMultiplePayment();
		DesktopActionsMultiplePayment multiPayment = new DesktopActionsMultiplePayment(driver);
		String paymentInstrument =multiPayment.getMultiPayementPaymentInstrument(1);
		multiPayment.makeMultiplePayment(myPolicyObj.fullAccountNumber, null, amtToPay);	
		//verify the payment
		BCSearchAccounts search=new BCSearchAccounts(driver);
		search.searchAccountByAccountNumber(myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuPayments();
		AccountPayments acctPayment = new AccountPayments(driver);
		if(!acctPayment.verifyPayment(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), PaymentInstrumentEnum.get(paymentInstrument), amtToPay)){
			throw new GuidewireException("BillingCenter", "didn't find the payment made from Mulitple Payment Entry.");
		}
	}
	
	@Test(dependsOnMethods = { "makeMultiplePaymentAndVerify" })	
	public void disburseExtraAmountAndEditDueDate() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Automatic_Disbursement);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuDisbursements();
		AccountDisbursements disbursement = new AccountDisbursements(driver);
		if(!disbursement.verifyDisbursements(DisbursementStatus.Awaiting_Approval, extraAmount)){
			throw new GuidewireException("BillingCenter", "didn't find the disbursement.");
		}else{
			disbursement.clickAccountDisbursementsEdit();
			disbursement.setDisbursementsDueDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.BusinessDay, 10));
			disbursement.clickAccountDisbursementsUpdate();
		}		
	}
	
	@Test(dependsOnMethods = { "makeMultiplePaymentAndVerify" })	
	public void verifyLienholerCreditDistributionsPaymentDetailsOwners() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), lienholderNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuPaymentsCreditDistributions();
		AccountPaymentsCreditDistributions creditDistribution=new AccountPaymentsCreditDistributions(driver);
		boolean found = creditDistribution.verifyPaymentDetailsOwner();
		if (!found) {
			Assert.fail("The verification step on the lienholder Payment Credit Distribution page in Billing Center failed.");
		}	
	}
}
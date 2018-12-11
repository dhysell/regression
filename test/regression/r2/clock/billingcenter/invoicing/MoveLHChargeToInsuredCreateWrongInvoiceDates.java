package regression.r2.clock.billingcenter.invoicing;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.search.BCSearchAccounts;
import repository.bc.wizards.EditSplitMoveChargeWizard;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.TransactionNumber;
import repository.gw.enums.TransactionType;
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
 * @Author JQU
 * @Requirement DE5578-Edit/split/move of a charge from lien to insured creates invoice with wrong dates
 * Go to an account which is both paid by lien and insured
 * Select a charge(with Down-Payment Invoice Item), move the charge from lien to insured by edit/split/move
 * The wax off charge is created on the lien (which creates a credit invoice)
 * The invoice dates of this credit invoice are wrong.
 * They should be billed and due on the same day
 * @DATE June 9, 2017
 */
public class MoveLHChargeToInsuredCreateWrongInvoiceDates extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();	
	private String LHNubmer;
	private BCAccountMenu acctMenu;
	private String loanNumber="LN12345";
	private double lienholderPremium;
	
	private void gotoAccount(String accountNumber){
		BCSearchAccounts search=new BCSearchAccounts(driver);
		search.searchAccountByAccountNumber(accountNumber);	
	}
	@Test 
	public void generatePolicy() throws Exception {
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		PolicyLocationBuilding building1=new PolicyLocationBuilding();
		building1.setBuildingLimit(150000);
		building1.setBppLimit(150000);
		locOneBuildingList.add(building1);
		
		ArrayList<AdditionalInterest> loc1LNBldg2AdditionalInterests = new ArrayList<AdditionalInterest>();		
		AdditionalInterest loc1LNBldg2AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1LNBldg2AddInterest.setNewContact(CreateNew.Create_New_Always);
		loc1LNBldg2AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1LNBldg2AddInterest.setLoanContractNumber(loanNumber);
		loc1LNBldg2AdditionalInterests.add(loc1LNBldg2AddInterest);
		PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
		loc1Bldg2.setAdditionalInterestList(loc1LNBldg2AdditionalInterests);
		locOneBuildingList.add(loc1Bldg2);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));					

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("MoveLHChargeToInsured")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		LHNubmer=myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNumber();
		lienholderPremium = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();
	}

	@Test(dependsOnMethods = { "generatePolicy" })
	public void payInsuredAndLH() throws Exception {				
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), myPolicyObj.busOwnLine.getPolicyNumber());
		//move clock to LH invoice date and pay LH
        Date LHDueDate = DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getEffectiveDate(), DateAddSubtractOptions.Month, 1);
		ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(LHDueDate, DateAddSubtractOptions.Day, -20));
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		
		gotoAccount(LHNubmer);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        newPayment = new NewDirectBillPayment(driver);
        newPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPolicyNumber(), loanNumber, lienholderPremium);
	}
	@Test(dependsOnMethods = { "payInsuredAndLH" })
	public void moveLHChargeToInsured() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
		//move days
		ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, 3));
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
        AccountCharges acctCharge = new AccountCharges(driver);
        acctCharge.clickEditSplitMoveCharges(LHNubmer, TransactionNumber.Premium_Charged, TransactionType.Policy_Issuance, myPolicyObj.accountNumber);
		EditSplitMoveChargeWizard moveCharge = new EditSplitMoveChargeWizard(driver);
		moveCharge.clickAdd();
        moveCharge.setPayerFromPicker(1, myPolicyObj.accountNumber);
		moveCharge.setAmount(1,  lienholderPremium);
		moveCharge.clickExecute();		
	}
	@Test(dependsOnMethods = { "moveLHChargeToInsured" })
	public void checkLHCreditDates() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), LHNubmer);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
        AccountInvoices acctInv = new AccountInvoices(driver);
		Assert.assertTrue(acctInv.verifyInvoice(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, InvoiceType.Shortage, null, null, lienholderPremium*(-1), null), "Failed to verify the invoice");
	}
}
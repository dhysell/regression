package regression.r2.noclock.billingcenter.payments;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.CountyIdaho;
import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.AccountPolicies;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.actions.DesktopActionsCountyCashPayment;
import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ChargeCategory;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentRestriction;
import repository.gw.enums.PaymentReturnedPaymentReason;
import repository.gw.enums.PaymentType;
import repository.gw.enums.TransactionNumber;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;

import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author jqu
 * @Description: Test Payment Restrictions on Payment Reversals (3 payments and
 *               reversals) and Cashonly Rollup
 * @RequirementsLink <a href=
 *                   "http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/11%20-%20Invoice%20Scheduling/Charges/Installment%20Fee_Business%20Requirements.docx">
 *                   Business Requirements</a>
 * @DATE Nov 05, 2015
 */
@QuarantineClass
public class PaymentRestrictionAndCashOnlyRollupTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private double paymentReversalFee = 20;
	private BCAccountMenu acctMenu;		
	private AccountInvoices acctInv;
	private AccountCharges acctChg;
	private DesktopActionsCountyCashPayment countyCashPayment;
	private BCDesktopMenu desktopMenu;
	public ARUsers arUser = new ARUsers();

	@Test
	public void generate() throws Exception {
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		locOneBuildingList.add(new PolicyLocationBuilding());
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        this.myPolicyObj = new GeneratePolicy.Builder(driver).withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("PaymentRestrictionAndCashonlyRollup")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Monthly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}

	@Test(dependsOnMethods = { "generate" })	
	public void makePaymentReversalAndVerifyReversalFee() throws Exception {
		Map<Double, PaymentRestriction> reversalRestriction = new LinkedHashMap<Double, PaymentRestriction>();
		reversalRestriction.put(new Double(20), PaymentRestriction.None_One_NSF);
		reversalRestriction.put(new Double(30), PaymentRestriction.Cash_Only_Warning);
		reversalRestriction.put(new Double(40), PaymentRestriction.Cash_Only);

		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(),
				myPolicyObj.accountNumber);
		// make payment and reversal then check the Payment Restriction, verify
		// the reversal fee after the second reversal
		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		
		for (Object key : reversalRestriction.keySet()) {
			getQALogger().info(key + " - " + reversalRestriction.get(key));
            acctMenu = new BCAccountMenu(driver);
			// make the payment
			acctMenu.clickAccountMenuActionsNewDirectBillPayment();
            NewDirectBillPayment directBillPay = new NewDirectBillPayment(driver);
			directBillPay.setAmount(Double.valueOf(key.toString()));
			directBillPay.setUnappliedFund(myPolicyObj.accountNumber);
			directBillPay.clickExecuteWithoutDistribution();
			// reverse the payment
			acctMenu.clickAccountMenuPayments();
			AccountPayments acctPayment = new AccountPayments(driver);
			acctPayment.reversePaymentAtFault(null,Double.valueOf(key.toString()), null, Double.valueOf(key.toString()),
					PaymentReturnedPaymentReason.InsufficientFunds);
			acctMenu.clickAccountMenuPolicies();
            AccountPolicies acctPolicy = new AccountPolicies(driver);
			try{
                acctPolicy.verifyPaymentRestriction(myPolicyObj.busOwnLine.getPolicyNumber(),
                        reversalRestriction.get(key));
			}catch(Exception e){			
				Assert.fail("Didn't find the payment restriction " + reversalRestriction.get(key).getValue() + "for payment amount of " + key);			
			}
			// verify paymentReversalFee in Invoices screen for the second
			// reversal (No reversal fee for the first reversal)
			if (Double.valueOf(key.toString()) == 30) {
				acctMenu.clickBCMenuCharges();
				acctChg = new AccountCharges(driver);
				try{
                    acctChg.getChargesOrChargeHoldsPopupTableRow(null, myPolicyObj.accountNumber, TransactionNumber.Policy_Payment_Reversal_Fee_Charged, ChargeCategory.Policy_Payment_Reversal_Fee, null, null, null, null, myPolicyObj.busOwnLine.getPolicyNumber(), paymentReversalFee, null, null, null, null, null, null);
				}catch(Exception e){
					Assert.fail("didn't find the Payment Reversal Fee after the second payment reversal.");
				}
			}

		}
	}
	// verify CashOnly Rollup after the third reversal
	@Test(dependsOnMethods = { "makePaymentReversalAndVerifyReversalFee" })
	public void verifyCashonlyRollup() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(),myPolicyObj.accountNumber);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
        acctInv = new AccountInvoices(driver);
        if (!acctInv.verifyInvoice(InvoiceType.CashonlyRollup, InvoiceStatus.Billed, (myPolicyObj.busOwnLine.getPremium().getTotalNetPremium() + (paymentReversalFee * 2)))) {
			Assert.fail("the invoices didn't rollup correctly.");
		}	
	}
	
	//Will probably need to add a test for checking delinquencies after moving a day.
	
	@Test(dependsOnMethods = { "verifyCashonlyRollup" })
	public void checkSoftStopOnMultiplePaymentsPage() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());
		desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuActionsCountyCashPayment();
		countyCashPayment = new DesktopActionsCountyCashPayment(driver);
		int rowNumber = countyCashPayment.getNextAvailableLineInTable("Amount");
        countyCashPayment.setCountyCashPolicyNumber(rowNumber, this.myPolicyObj.busOwnLine.getPolicyNumber());
		countyCashPayment.setCountyCashPaymentAmount(rowNumber, 25.00);
		countyCashPayment.selectCountyCashTableCountyCode(rowNumber, CountyIdaho.Bannock);
		new GuidewireHelpers(driver).sendArbitraryKeys(Keys.TAB);
		countyCashPayment.selectCountyCashTableOfficeNumber(rowNumber, "Chubbuck");
		countyCashPayment.clickDesktopActionsCountyCashPmtNext();
		if (new BasePage(driver).checkIfElementExists("//div[contains(@class, 'message')]", 1000)) {
            WebElement cashOnlyWarningMessage = new GuidewireHelpers(driver).find(By.xpath("//div[contains(@class, 'message')]"));
            if (!cashOnlyWarningMessage.getText().equals("Policy " + this.myPolicyObj.busOwnLine.getPolicyNumber() + " is in Cash Only Status.")) {
                Assert.fail("The cash only warning message was not what it should have been. the message should have been \"Policy " + this.myPolicyObj.busOwnLine.getPolicyNumber() + " is in Cash Only Status\", but was actually " + cashOnlyWarningMessage.getText());
			}
		} else {
			Assert.fail("The Cash Only Warning message did not appear as a soft stop. Test Failed.");
		}
		countyCashPayment.clickDesktopActionsCountyCashPmtFinish();
		if (!new BasePage(driver).checkIfElementExists("//span[contains(@id, 'DesktopActivities:DesktopActivitiesScreen:')]", 1000)) {
			Assert.fail("The County Cash Payment screen did not allow payment to finish, even though the policy was in Cash Only Status. Test Failed.");
		}
	}
}

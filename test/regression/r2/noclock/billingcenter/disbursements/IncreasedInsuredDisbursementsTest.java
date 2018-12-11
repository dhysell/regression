package regression.r2.noclock.billingcenter.disbursements;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountDisbursements;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DisbursementStatus;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentReversalReason;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author jqu
 * @Requirement DE3959 Insured Disbursement - Increasing Open Disbursements
 * @Steps: If additional un-applied funds are received while a disbursement is pending approval, increase the disbursement amount, keeping original dates and details.
 * If additional unapplied funds are received while a disbursement is approved (but not sent), do not increase the disbursement amount.
 * Create another (separate) disbursement the next time the batch runs. (If that doesn't work,
 * users are okay waiting until the previous disbursement is sent to create a new disbursement).
 * @RequirementsLink
 * @DATE September 29, 2016
 */
public class IncreasedInsuredDisbursementsTest extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObj;			
	private double firstExtraAmt=new Double(NumberUtils.generateRandomNumberInt(20, 45));
	private double secondExtraAmt=new Double(NumberUtils.generateRandomNumberInt(50, 80));
	private double thirdExtraAmt=new Double(NumberUtils.generateRandomNumberInt(80, 100));
	private BCAccountMenu acctMenu;
	private ARUsers arUser = new ARUsers();
	
	private void verifyDisbursement(AccountDisbursements disbursement, double amount) {
		try{
            disbursement.getDisbursementsTableRow(null, null, null, myPolicyObj.busOwnLine.getPolicyNumber(), DisbursementStatus.Awaiting_Approval, null, amount, null, null, null);
		}catch(Exception e){
			Assert.fail("didn't find the correct disbursement with amount of $"+amount);
		}
	}
	@Test
	public void generate() throws Exception {			
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		locOneBuildingList.add(new PolicyLocationBuilding());
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));		
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
			.withInsPersonOrCompany(ContactSubType.Company)
			.withInsCompanyName("IncreasedInsuredDisbursementTest")			
			.withPolOrgType(OrganizationType.Partnership)
			.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
			.withPolicyLocations(locationsList)			
			.withPaymentPlanType(PaymentPlanType.Annual)			
			.withDownPaymentType(PaymentType.Cash)
			.build(GeneratePolicyType.PolicyIssued);
	}
	@Test(dependsOnMethods = { "generate" })	
		public void payInsurdWithExtraAmount() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
        directPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPremium().getInsuredPremium() + firstExtraAmt, myPolicyObj.accountNumber);
		//disburse extra amount an verify it
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Automatic_Disbursement);
		acctMenu.clickAccountMenuDisbursements();
		AccountDisbursements disbursement = new AccountDisbursements(driver);
		verifyDisbursement(disbursement, firstExtraAmt);
		//make payment of the second extra amount and disburse it
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
		directPayment.makeDirectBillPaymentExecuteWithoutDistribution(secondExtraAmt, myPolicyObj.accountNumber);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Automatic_Disbursement);
		acctMenu.clickBCMenuSummary();
		acctMenu.clickAccountMenuDisbursements();
		//verify the open disbursement amount increased
		verifyDisbursement(disbursement, firstExtraAmt+secondExtraAmt);
	}
	@Test(dependsOnMethods = { "payInsurdWithExtraAmount" })	
		public void approveDisbursementAndMakeNewDisbursement() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuDisbursements();
		AccountDisbursements disbursement = new AccountDisbursements(driver);
		disbursement.clickAccountDisbursementsEdit();
		disbursement.clickAccountDisbursementsApprove();
		acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
		directPayment.makeDirectBillPaymentExecuteWithoutDistribution(thirdExtraAmt, myPolicyObj.accountNumber);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Automatic_Disbursement);
		acctMenu.clickBCMenuSummary();
		acctMenu.clickAccountMenuDisbursements();
		verifyDisbursement(disbursement, firstExtraAmt+secondExtraAmt+thirdExtraAmt);
	}
	@Test(dependsOnMethods = { "approveDisbursementAndMakeNewDisbursement" })	
		public void reverseThirdPaymentAndVerifyDisbursement() throws Exception {		
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuPayments();
		AccountPayments payment = new AccountPayments(driver);
		payment.reversePayment(thirdExtraAmt, null, null, PaymentReversalReason.Payment_Moved);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Automatic_Disbursement);
		acctMenu.clickAccountMenuDisbursements();
		AccountDisbursements disbursement = new AccountDisbursements(driver);
		//verify that after revering third payment, the disbursement is only the sum of first and second extra amount
		verifyDisbursement(disbursement, firstExtraAmt+secondExtraAmt);
	}
}

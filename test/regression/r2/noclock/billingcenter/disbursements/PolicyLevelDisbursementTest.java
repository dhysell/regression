package regression.r2.noclock.billingcenter.disbursements;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountDisbursements;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DisbursementStatus;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.DateUtils;
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
* @Description DE3919: Disbursed amounts does not match the amount to be disbursed.
* 			   US8983: Convert Production Accounts - Disbursements
* 					Note: can't test the Convert part, since the policy is policy level when it's generated.
* @DATE September 1st, 2016
*/
public class PolicyLevelDisbursementTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;	
	private ARUsers arUser = new ARUsers();
	private BCAccountMenu acctMenu;	
	private double extraAmountForPolicy= NumberUtils.generateRandomNumberInt(50, 150);
	private double amountForDefault=NumberUtils.generateRandomNumberInt(200, 500);	
	
	private void verifyDibursement(AccountDisbursements disbursement, Date dateIssued, Date dateRejected, String disbursementNumber, String unappliedFund, DisbursementStatus disbursementStatus, String trackingStatus, Double disbursementAmount) throws GuidewireException{
		try{
			disbursement.getDisbursementsTableRow(dateIssued, dateRejected, disbursementNumber, unappliedFund, disbursementStatus, trackingStatus, disbursementAmount, null, null, null).click();
		}catch(Exception e){
			Assert.fail("doens't find the correct disbursement for "+unappliedFund + " with status of "+ disbursementStatus.getValue());
		}
	}
	
	@Test
	public void generate() throws Exception {
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();	
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();			
		locOneBuildingList.add(loc1Bldg1);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));				

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver).withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("PolicyLevelDibursementTest")
                .withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);			
	}	
	@Test(dependsOnMethods = { "generate" })	
		public void makePaymentsOnDefaultAndPolicy() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directBillPmt = new NewDirectBillPayment(driver);
		//pay by Default
		directBillPmt.makeDirectBillPaymentExecuteWithoutDistribution(amountForDefault);
		//pay by policy
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        directBillPmt.makeDirectBillPaymentExecuteWithoutDistribution(extraAmountForPolicy + myPolicyObj.busOwnLine.getPremium().getTotalNetPremium(), myPolicyObj.busOwnLine.getPolicyNumber());
		
		//disburse the extra amount and the amount paid for Default
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Automatic_Disbursement);
		//verify the two disbursements
		acctMenu.clickAccountMenuDisbursements();
		AccountDisbursements disbursement = new AccountDisbursements(driver);
		verifyDibursement(disbursement, null, null, null, "Default", DisbursementStatus.Awaiting_Approval, null, amountForDefault);
        verifyDibursement(disbursement, null, null, null, myPolicyObj.busOwnLine.getPolicyNumber(), DisbursementStatus.Awaiting_Approval, null, extraAmountForPolicy);
		//test DE3919 second scenario
		disbursement.clickAccountDisbursementsEdit();
		disbursement.setDisbursementsDueDate(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter));
		disbursement.clickAccountDisbursementsApprove();
		//verify the approval
        verifyDibursement(disbursement, null, null, null, myPolicyObj.busOwnLine.getPolicyNumber(), DisbursementStatus.Approved, null, extraAmountForPolicy);
		//sent the disbursement
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Disbursement);
		acctMenu.clickBCMenuSummary();
		acctMenu.clickAccountMenuDisbursements();
		//verify the sent disbursement for the policy level has the correct amount
        verifyDibursement(disbursement, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, null, myPolicyObj.busOwnLine.getPolicyNumber(), DisbursementStatus.Sent, null, extraAmountForPolicy);
	}
}

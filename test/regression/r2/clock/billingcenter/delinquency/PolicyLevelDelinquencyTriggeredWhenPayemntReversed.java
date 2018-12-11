package regression.r2.clock.billingcenter.delinquency;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.search.BCSearchPolicies;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OpenClosed;
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
 * @Requirement DE4012 Delinquency- Delinquency is not triggered for policy level when payment is reversed.
 * @RequirementsLink
 * @DATE September 29, 2016
 */
@QuarantineClass
public class PolicyLevelDelinquencyTriggeredWhenPayemntReversed extends BaseTest {
private WebDriver driver;
public GeneratePolicy myPolicyObj;	
private ARUsers arUser = new ARUsers();
		
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
            .withPaymentPlanType(PaymentPlanType.getRandom())
		.withDownPaymentType(PaymentType.Cash)
		.build(GeneratePolicyType.PolicyIssued);
}

@Test(dependsOnMethods = { "generate" })
	public void payInsurdWithExtraAmount() throws Exception {	
	this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
	Config cf = new Config(ApplicationOrCenter.BillingCenter);
	driver = buildDriver(cf);
    new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.busOwnLine.getPolicyNumber());
	//wait for trouble ticket to come
    BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
	policyMenu.clickBCMenuTroubleTickets();
    BCCommonTroubleTickets TT = new BCCommonTroubleTickets(driver);
	TT.waitForTroubleTicketsToArrive(60);
	//payoff the down payment
	policyMenu.clickTopInfoBarAccountNumber();
	new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
    BCAccountMenu acctMenu = new BCAccountMenu(driver);
    acctMenu.clickAccountMenuActionsNewDirectBillPayment();
    NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
    directPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), myPolicyObj.accountNumber);
	ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, 1));
	new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
	//reverse the payment
	acctMenu.clickAccountMenuPayments();
    AccountPayments payment = new AccountPayments(driver);
    payment.reversePayment(myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), null, null, PaymentReversalReason.Payment_Moved);
	//verify delinquency in account level
	acctMenu.clickBCMenuDelinquencies();
    BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
	try{
		delinquency.getDelinquencyTableRow(OpenClosed.Open, DelinquencyReason.PastDueFullCancel, null, null, null, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, null);
		
	}catch(Exception e){
		Assert.fail("delinquency is expected to be triggered.");
	}
	//verify policy level delinquency
	BCSearchPolicies search = new BCSearchPolicies(driver);
    search.searchPolicyByPolicyNumber(myPolicyObj.busOwnLine.getPolicyNumber());
	policyMenu.clickBCMenuDelinquencies();
    BCCommonDelinquencies policyDelinquency = new BCCommonDelinquencies(driver);
	try{
		policyDelinquency.getDelinquencyTableRow(OpenClosed.Open, DelinquencyReason.PastDueFullCancel, null, null, null, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, null);
		
	}catch(Exception e){
		Assert.fail("delinquency is expected to be triggered.");
	}
	}
}

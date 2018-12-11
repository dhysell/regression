package regression.r2.clock.billingcenter.payments;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountPolicies;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPaymentsPaymentRequests;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.policy.PolicyPaymentSchedule;
import repository.bc.wizards.PaymentRequestWizard;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PolicyStatus;
import repository.gw.enums.Status;
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
* @Author JQU
* @Requirement DE2133 - Payment Request - "Responsive" should not be an option for a Payment Request
* 	1) Monthly policy bound.
	2) PC sends over account and payment instrument.
	3) Account goes to a responsive with a different plan (Annual, Semi Annual, or Quarterly)
	4) Account goes back to Monthly with the corresponding bank as a Payment Instrument.
	5) Payment Request is automatically set up.
	Defect: User edits payment request and "Responsive" is an option.
			"Responsive" is also an option if the user manually creates a payment request.
* 
* @DATE September 1st, 2017*/
public class ResponsiveNotBeAnOptionForPaymentRequest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();	
	private PaymentRequestWizard pmtRequestWizard;
	private BCAccountMenu acctMenu;
	
	private void verifyPaymentOption(){
		pmtRequestWizard = new PaymentRequestWizard(driver);
		try{
			pmtRequestWizard.setPaymentInstrument(PaymentInstrumentEnum.Responsive);
			Assert.fail("Reponsive should not be an option as Payment Instrument for payment request.");
		}catch(AssertionError e){
			getQALogger().info("Responsive is not an option as Payment Instrument for payment request which works as expected.");
		}
	}
	@Test
    public void generatePolicy() throws Exception {
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		locOneBuildingList.add(new PolicyLocationBuilding());
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));		

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        myPolicyObj = new GeneratePolicy.Builder(driver)
			.withInsPersonOrCompany(ContactSubType.Company)
			.withInsCompanyName("PaymentRequest")
			.withPolOrgType(OrganizationType.Partnership)
			.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
			.withPolicyLocations(locationsList)			
			.withPaymentPlanType(PaymentPlanType.Monthly)
			.withDownPaymentType(PaymentType.Cash)
			.build(GeneratePolicyType.PolicyIssued);
	}
	@Test(dependsOnMethods = { "generatePolicy" })
	public void payoffDownPaymentAndCreatePaymentRequest() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), myPolicyObj.busOwnLine.getPolicyNumber());
		//move clock 1 day and run InvoiceDue
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		//move to next invoice due date
        ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getEffectiveDate(), DateAddSubtractOptions.Month, 1));
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Payment_Request);
		//verify payment request was created
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuPaymentsPaymentRequests();
        AccountPaymentsPaymentRequests requestPage = new AccountPaymentsPaymentRequests(driver);
		WebElement paymentRequestRow=requestPage.getPaymentRequestRow(Status.Requested, null, null, null, null, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, null, null, null);
		if(paymentRequestRow==null){
			Assert.fail("Payment Request was not created as expected.");
		}
		else{//verify that Responsive is not an option when Editing Payment Request
			requestPage.setCheckBox(paymentRequestRow, true);
            requestPage.clickEdit();
            verifyPaymentOption();
		}			
	}
	@Test(dependsOnMethods = { "payoffDownPaymentAndCreatePaymentRequest" })
    public void switchPaymentScheduleAndVeryPaymentRequest() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
		//change payment plan type to annual
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuPolicies();
        AccountPolicies acctPoliciesPage = new AccountPolicies(driver);
        acctPoliciesPage.clickPolicyNumberInPolicyTableRow(null, null, myPolicyObj.busOwnLine.getEffectiveDate(), null, PolicyStatus.Open, null, null, null, null, null);
        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
		policyMenu.clickPaymentSchedule();
        PolicyPaymentSchedule paymentSchedule = new PolicyPaymentSchedule(driver);
		paymentSchedule.changePaymentPlan(PaymentPlanType.Annual);
		//change payment plan type to monthly again
		paymentSchedule.changePaymentPlan(PaymentPlanType.Monthly);
		
		//verify again that responsive is not a option when editing it		
		policyMenu.clickTopInfoBarAccountNumber();
		acctMenu.clickAccountMenuPaymentsPaymentRequests();
        AccountPaymentsPaymentRequests requestPage = new AccountPaymentsPaymentRequests(driver);
		WebElement paymentRequestRow=requestPage.getPaymentRequestRow(Status.Requested, null, null, null, null, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, null, null, null);
		requestPage.setCheckBox(paymentRequestRow, true);
		requestPage.clickEdit();

        verifyPaymentOption();
	}
	@Test(dependsOnMethods = { "switchPaymentScheduleAndVeryPaymentRequest" })
    public void manuallyCreatePaymentRequestAndVerify() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuActionsNewPaymentRequest();
	}
}

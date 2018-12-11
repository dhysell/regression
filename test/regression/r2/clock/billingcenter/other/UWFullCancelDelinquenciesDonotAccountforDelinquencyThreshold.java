package regression.r2.clock.billingcenter.other;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.cancellation.StartCancellation;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author sreddy
 * @Requirement : DE4656   Underwriting Full Cancel Delinquencies do not account for Delinquency Threshold
 * @Description : 1. Issue a policy with Annual Payment plan
 * 2. On BC make down payment less than threshold (delinquency not triggered)
 * 3. Cancel the policy in Policy center
 * 4. Verify that UW full cancel delinquency is not triggered
 * @DATE Mar 12, 2017
 */
public class UWFullCancelDelinquenciesDonotAccountforDelinquencyThreshold extends BaseTest {
	private WebDriver driver;
	private ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
	private ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();

	@Test
	public void generatePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		int yearTest = DateUtils.getCenterDateAsInt(cf, ApplicationOrCenter.PolicyCenter, "yyyy") - NumberUtils.generateRandomNumberInt(0, 50);
		
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setYearBuilt(yearTest);
		loc1Bldg1.setClassClassification("storage");
					
		this.locOneBuildingList.add(loc1Bldg1);
		this.locationsList.add(new PolicyLocation(new AddressInfo(), this.locOneBuildingList));

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("US10221MoveLHFunds Test")
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolOrgType(OrganizationType.Partnership)
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		
	}

	@Test(dependsOnMethods = { "generatePolicy" })
	public void makeInsuredDownPayment() throws Exception {
				
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        double tresholdPaymentAmount = myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount() * 0.92;
        newPayment.makeInsuredDownpayment(myPolicyObj, tresholdPaymentAmount, myPolicyObj.busOwnLine.getPolicyNumber());
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 10);
		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "makeInsuredDownPayment" })
    public void runInvoiceDueAndCheckForNoDelinquency() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuDelinquencies();
        BCCommonDelinquencies acctDelinquency = new BCCommonDelinquencies(driver);
		
		try {
			acctDelinquency.verifyDelinquencyByReason(OpenClosed.Open, DelinquencyReason.PastDueFullCancel, null,null);
			} catch (Exception e) {
				System.out.println("As expected there is No 'Past Due Full Cancel' due to 92% amount paid");
			}
			
		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "runInvoiceDueAndCheckForNoDelinquency" })
    public void CancelPolicyInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.busOwnLine.getPolicyNumber());
        StartCancellation cancelPol = new StartCancellation(driver);
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.OtherInsuredRequest, "Test UWFullCancelDelinquenciesDonotAccountforDelinquencyThreshold", null,true);		
		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "CancelPolicyInPolicyCenter" })
    public void runInvoiceDueAndCheckForNoDelinquencyAfterCancellation() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuDelinquencies();
        BCCommonDelinquencies acctDelinquency = new BCCommonDelinquencies(driver);
		
		Assert.assertFalse(acctDelinquency.verifyDelinquencyByReason(OpenClosed.Open, DelinquencyReason.UnderwritingFullCancel, null,null), "Underwriting Full Cancel Exist Which is not Expected");
		System.out.println("As expected there is No 'Underwriting Full Cancel'");
	
		
		new GuidewireHelpers(driver).logout();
	}
	
}

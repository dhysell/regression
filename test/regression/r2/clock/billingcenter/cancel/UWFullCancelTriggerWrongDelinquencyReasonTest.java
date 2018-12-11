package regression.r2.clock.billingcenter.cancel;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.policy.BCPolicyMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OpenClosed;
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
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.cancellation.StartCancellation;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
/**
* @Author jqu
* @Description: DE3523 UW cancellation got wrong delinquency reason
* @Steps: Steps:
			1. Generate a policy with "Promised Trouble Ticket"
			2. Not pay the down payment and move clock some days (< 10 days), run batch processes to make down payment Due
			3. Cancel the policy
     		Defect: sometimes the cancellation triggered "Past Due Full Cancel"
     		Expected: It should be "Underwriting Full Cancel"
* @DATE May 4, 2016
*/
public class UWFullCancelTriggerWrongDelinquencyReasonTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;	
	private ARUsers arUser = new ARUsers();
	private BCAccountMenu acctMenu;
	private Date currentDate;
	private BCPolicyMenu policyMenu;

	@Test
	public void generate() throws Exception {
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		locOneBuildingList.add(new PolicyLocationBuilding());		
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();		
		locOneBuildingList.add(loc1Bldg1);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));					

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver).withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("UWFullCancel")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.SelfStorageFacilities))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);	
	}
	
	@Test(dependsOnMethods = { "generate" })
	public void runBatchprocessesAndMoveClock() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.busOwnLine.getPolicyNumber());

        policyMenu = new BCPolicyMenu(driver);
		policyMenu.clickBCMenuTroubleTickets();

        BCCommonTroubleTickets tt = new BCCommonTroubleTickets(driver);
		if(!tt.waitForTroubleTicketsToArrive(60)){
			throw new GuidewireException("BillingCenter: ",	"doesn't find the Promised Payment Trouble Ticket.");
		}
		policyMenu.clickTopInfoBarAccountNumber();

        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		currentDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter),	DateAddSubtractOptions.Day, 7);		

		ClockUtils.setCurrentDates(cf, currentDate);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
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
	public void verifyDelinquency() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		//wait for the cancellation to come 
		acctMenu.clickBCMenuCharges();
        AccountCharges charge = new AccountCharges(driver);
		charge.waitUntilPolicyCancellationArrive(5000);
		acctMenu.clickBCMenuDelinquencies();
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		try{
			delinquency.getDelinquencyTableRow(OpenClosed.Open, DelinquencyReason.UnderwritingFullCancel, myPolicyObj.accountNumber, null, myPolicyObj.accountNumber, currentDate, null,null);
		}catch(Exception e){
			Assert.fail("doesn't find the Underwriting Full Cancel delinquency.");			
		}
	}
}

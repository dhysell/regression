package previousProgramIncrement.pi2_062818_090518.nonFeatures.Triton;

import repository.bc.account.BCAccountMenu;
import repository.bc.common.BCCommonActivities;
import repository.bc.common.BCCommonDesktopMyActivityAndQueue;
import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.BCDesktopMyQueues;
import repository.bc.wizards.ManualActivityWizard;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.ActivityFilter;
import repository.gw.enums.ActivityQueuesBillingCenter;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.Status;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.BankAccountInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

import java.util.ArrayList;
import java.util.Date;

import static org.testng.Assert.assertTrue;

/**
* @Author JQU
* @Requirement 	US15383 - Add "review assignment" permission to billing manager and advance billing clerical roles
* 	
* @DATE July 5th, 2018
* 
* */
public class US15383ReviewAssignmentPermissionTest extends BaseTest{
	private WebDriver driver;
	private ARUsers arUser = new ARUsers();
	private GeneratePolicy myPolicyObj = null;
	//only some billing managers can view BOP queues
	private String billingMng = "asnorris";
	private String actSubject = "Manual Activity Reminder";
	private String escalatedActSubject = "Escalated Activity - Manual Activity Reminder";
	private int pageNumber=0;
	private boolean foundRow = false;
	private boolean foundRow(BCCommonDesktopMyActivityAndQueue activity, BCDesktopMyQueues queue,Date date, Status status, String accountNumber, boolean foundRow){		
		while(pageNumber<activity.getNumberPages() && !foundRow){
			try{
				activity.getMyActivityOrMyQueueTableRow(date, null, null, status, "Escalated Activity", null, accountNumber, null);
				foundRow = true;
			}catch (Exception e) {
				getQALogger().error("Didn't find the suspense payment on page #"+pageNumber);
				pageNumber++;
				queue.goToNextPage();
			}			
		}
		return foundRow;
	}
	private void generatePolicy() throws Exception {

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        BankAccountInfo bankAccountInfo = new BankAccountInfo();
        bankAccountInfo.setAccountNumber("12345678");
		
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

         myPolicyObj = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("Insured Policy")
                .withPolOrgType(OrganizationType.Partnership)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withBankAccountInfo(bankAccountInfo)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
			
		driver.quit();
	}
	
	@Test
	public void triggerActivityAndEscalation() throws Exception {
		generatePolicy();
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);		
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.fullAccountNumber);

		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuActionsManualActivity();

		ManualActivityWizard manualActivity = new ManualActivityWizard(driver);
		manualActivity.setDueDate(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter));
		manualActivity.setEscalationDate(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter));
		manualActivity.setAccountNumber(myPolicyObj.fullAccountNumber);
		manualActivity.clickUpdate();
		//verify activity
		accountMenu.clickBCMenuActivities();
		BCCommonActivities activity = new BCCommonActivities(driver);
		
		Assert.assertTrue(activity.verifyActivityTable(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), OpenClosed.Open, actSubject), "Didn't find the manual activity: "+actSubject);
		//escalate the activity
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Activity_Escalation);
		accountMenu.clickBCMenuCharges();
		accountMenu.clickBCMenuActivities();
		Assert.assertTrue(activity.verifyActivityTable(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), OpenClosed.Open, escalatedActSubject), "Didn't find the escalated manual activity: "+escalatedActSubject);
	}
	
	
	@Test(dependsOnMethods = { "triggerActivityAndEscalation" })
	public void verifyUserPermission() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);

		new Login(driver).login(billingMng, arUser.getPassword());
		BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuMyQueues();
		BCDesktopMyQueues queue = new BCDesktopMyQueues(driver);
		queue.setMyQueuesFilter(ActivityQueuesBillingCenter.ARSupervisorWesternCommunity);
		
		//find the escalation activity in the queue
		while(pageNumber<queue.getNumberPages() && !foundRow){
			try{
				queue.getMyQueueTableRow(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, null, Status.Open, "Escalated Activity", null, myPolicyObj.fullAccountNumber, null);
				foundRow = true;
			}catch (Exception e) {
				getQALogger().error("Didn't find the suspense payment on page #"+pageNumber);
				pageNumber++;
				queue.goToNextPage();
			}			
		}
			
		assertTrue(foundRow, "Didn't find the escalated acvity in the queue.");
		queue.setCheckBox(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, null, Status.Open, "Escalated Activity", null, myPolicyObj.fullAccountNumber, null, true);
		queue.clickAssignSelectedToMe();
		
		//verify activity
		desktopMenu.clickDesktopMenuMyActivities();
		BCCommonDesktopMyActivityAndQueue myActivity = new BCCommonDesktopMyActivityAndQueue(driver);

		pageNumber=0;
		foundRow = false;		
		foundRow = foundRow(myActivity, queue, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), Status.Open, myPolicyObj.fullAccountNumber, foundRow);
		if(!foundRow){
			Assert.fail("Didn't find the activity assigned to billing manager.");
		}
		//test DE7571 -- Some escalated activities are missing the complete button
		myActivity.clickLinkInActivityOrQueueTable("Subject", DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, null, Status.Open, "Escalated Activity", null, myPolicyObj.fullAccountNumber, null);
		
		myActivity = new BCCommonDesktopMyActivityAndQueue(driver);
		
		myActivity.clickActivityCompleteButton();
		myActivity.setMyActivitiesStatusFilter(ActivityFilter.ClosedLast30Days);
		pageNumber=0;
		foundRow = false;		
		foundRow = foundRow(myActivity, queue, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), Status.Complete, myPolicyObj.fullAccountNumber, foundRow);
		if(!foundRow){
			Assert.fail("Didn't find the activity assigned to billing manager.");
		}	
	}
}

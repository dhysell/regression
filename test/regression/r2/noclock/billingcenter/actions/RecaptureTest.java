package regression.r2.noclock.billingcenter.actions;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.payments.AccountPayments;
import repository.bc.account.summary.BCAccountSummary;
import repository.bc.common.BCCommonActivities;
import repository.bc.common.BCCommonHistory;
import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.BCDesktopMyQueues;
import repository.bc.desktop.actions.DesktopActionsMultiplePayment;
import repository.bc.search.BCSearchAccounts;
import repository.bc.topmenu.BCTopMenu;
import repository.bc.wizards.NewRecaptureWizard;
import repository.bc.wizards.NewTransferWizard;
import repository.driverConfiguration.Config;
import repository.gw.enums.ActivityQueuesBillingCenter;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.TransferReason;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Description: Account Level recapture test
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/02%20-%20Activities/02-01%20Recapture%20Activity.docx">Recapture</a>
 * @Author: By:Jessica Qu
 * @Test Environment: CPP_PL branch
 * Date: Oct. 05, 2015
 */
public class RecaptureTest extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObj;			
	private double extraAmt=100.11;
	private BCAccountMenu acctMenu;
	private int errorCount=0;
	private String lienholderNumber;
	private Date recaptureDate;
	private ARUsers arUser = new ARUsers();
			
	@Test
	public void generate() throws Exception {			
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		locOneBuildingList.add(new PolicyLocationBuilding());
		
		ArrayList<AdditionalInterest> loc1LNBldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
		//create random additional interest
		AdditionalInterest loc1LNBldg1AddInterest = new AdditionalInterest(ContactSubType.Company);		
		loc1LNBldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1LNBldg1AdditionalInterests.add(loc1LNBldg1AddInterest);
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setAdditionalInterestList(loc1LNBldg1AdditionalInterests);
		locOneBuildingList.add(loc1Bldg1);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));		

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
			.withInsPersonOrCompany(ContactSubType.Company)
			.withInsCompanyName("RecaptureTest")			
			.withPolOrgType(OrganizationType.Partnership)
			.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
			.withPolicyLocations(locationsList)			
			.withPaymentPlanType(PaymentPlanType.Annual)			
			.withDownPaymentType(PaymentType.Cash)
			.build(GeneratePolicyType.PolicyIssued);	
		lienholderNumber=myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNumber();
	}
	
	@Test(dependsOnMethods = { "generate" })	
	public void payAndTransferExtraAmount() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		//Configuration.setProduct(ApplicationOrCenter.BillingCenter);
		//login(arUser.getUserName(), arUser.getPassword());
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickBCMenuCharges();
		AccountCharges charges = new AccountCharges(driver);
		charges.waitUntilIssuanceChargesArrive(120);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		BCTopMenu topMenu=new BCTopMenu(driver);
		topMenu.clickDesktopTab();
		BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuActionsMultiplePayment();
		DesktopActionsMultiplePayment multiPayment = new DesktopActionsMultiplePayment(driver);
        multiPayment.setMultiPaymentPolicyNumber(1, myPolicyObj.busOwnLine.getPolicyNumber());
		multiPayment.setMultiPaymentPaymentAmount(1, myPolicyObj.busOwnLine.getPremium().getInsuredPremium() + extraAmt);
		multiPayment.clickNext();

		multiPayment.clickFinish();

		BCSearchAccounts search=new BCSearchAccounts(driver);
		search.searchAccountByAccountNumber(myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickActionsNewTransactionTransfer();
		NewTransferWizard transWiz = new NewTransferWizard(driver);
		transWiz.selectUnappliedFund(myPolicyObj.accountNumber);
		transWiz.clickAdd();
		transWiz.setTransferTableTargetAccount(1, lienholderNumber);
		transWiz.selectTransferTableReason(1, TransferReason.Agent_Request);

		transWiz.setTransferTableAmount(1, extraAmt);

		//transWiz.clickNext();
		//		//why first time clicking next, the policy number doesn't display? defect?
        transWiz.selectTransferTableTargetPolicyNumber(1, myPolicyObj.busOwnLine.getPolicyNumber());
		transWiz.clickNext();
		transWiz.clickFinish();
	}
	
	@Test(dependsOnMethods = { "payAndTransferExtraAmount" })	
	public void reversePaymentAndVerifyUnappliedCredit() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuPayments();
		AccountPayments acctPmt = new AccountPayments(driver);
        acctPmt.reversePaymentByAmount(myPolicyObj.busOwnLine.getPremium().getInsuredPremium() + extraAmt);
		acctMenu.clickBCMenuSummary();
		BCAccountSummary acctSum = new BCAccountSummary(driver);
		//verify that the credit in Summary screen equals to the extraAmt
        double unappledFund = acctSum.getUnappliedFundByPolicyNumber(myPolicyObj.busOwnLine.getPolicyNumber());
		if(unappledFund!=extraAmt*(-1)) 
		{
			System.out.println("The amount to recapture is not correct.");
			errorCount++;
		}
		recaptureDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);	
	}
	
	@Test(dependsOnMethods = { "reversePaymentAndVerifyUnappliedCredit" })		
	public void verifyRecaptureActivityAndQueue() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuActivities();
		//verify recapture in Activities screen
		BCCommonActivities acctAct = new BCCommonActivities(driver);
		boolean findActivity= acctAct.verifyActivityTable(recaptureDate, OpenClosed.Open, "Recapture");
		if(!findActivity) 
		{
			System.out.println("Couldn't find the activity of the recapture.");
			errorCount++;
		}
		else{
			acctAct.clickActivityTableSubject(recaptureDate, OpenClosed.Open, "Recapture");
			String actDescription = acctAct.getActivityDescription();
			if(!(actDescription.contains(myPolicyObj.accountNumber) && actDescription.contains("recapture")&& actDescription.contains(String.valueOf(extraAmt)))){
				System.out.println("The description of the recapture is not correct.");
				errorCount++;
			}
			acctAct.clickCancel();
		}	
		
		//verify the Desktop->My Queue
		BCTopMenu topMenu=new BCTopMenu(driver);
		topMenu.clickDesktopTab();
		BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuMyQueues();
		BCDesktopMyQueues myQueue = new BCDesktopMyQueues(driver);
		//accounts with -008 go to Western Community queue, with -001 go to Disbursement Specialist Farm Bureau queue		
        if (myPolicyObj.busOwnLine.getPolicyNumber().substring(0, 3).equals("08-")) {
			myQueue.setMyQueuesFilter(ActivityQueuesBillingCenter.ARGeneralWesternCommunity);
		}
		else{
			myQueue.setMyQueuesFilter(ActivityQueuesBillingCenter.ARSupervisorFarmBureau);
		}
		//sort twice so the latest activities show in first page
		myQueue.sortQueueTableByTitle("Opened");
		myQueue.sortQueueTableByTitle("Opened");
		boolean foundInQueue = myQueue.verifyMyQueueTable(recaptureDate, "Recapture", myPolicyObj.accountNumber);
		if(!foundInQueue) 
		{
			System.out.println("Couldn't find the recapture in the Queue");
			errorCount++;
		}		
	}
	
	@Test(dependsOnMethods = { "verifyRecaptureActivityAndQueue" })		
	public void makeRecapture() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuActionsNewNewTransactionRecapture();
		NewRecaptureWizard myRecapture = new NewRecaptureWizard(driver);
        myRecapture.performRecapture(this.myPolicyObj.busOwnLine.getPolicyNumber(), extraAmt);
	}
	
	@Test(dependsOnMethods = { "makeRecapture" })	
	public void checkHistoryOfTheRecapture() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuHistory();
		BCCommonHistory acctHistory = new BCCommonHistory(driver);
		boolean foundHistory=acctHistory.verifyHistoryTable(recaptureDate, "Recapture Charge created for Amount $"+extraAmt);
		//boolean foundHistory=acctHistory.verifyHistoryTable("01/15/2016", "Recapture Charge created for Amount $"+extraAmt);
		if(!foundHistory) 
		{
			System.out.println("Couldn't find the history of the recapture.");
			errorCount++;
		}		
		if(errorCount>0)
			Assert.fail("found recapture errors, please see the log for details\n");
	}	
	
}


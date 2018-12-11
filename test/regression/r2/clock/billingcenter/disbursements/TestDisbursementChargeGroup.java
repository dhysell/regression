package regression.r2.clock.billingcenter.disbursements;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountDisbursements;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.administration.BCAdministrationMenu;
import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.Config;
import repository.gw.administration.AdminEventMessages;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.MessageQueue;
import repository.gw.enums.MessageQueuesSafeOrderObjectLinkOptions;
import repository.gw.enums.MessageQueuesSafeOrderObjectSelectOptions;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
@QuarantineClass
public class TestDisbursementChargeGroup extends BaseTest {
	private WebDriver driver;
	private ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
	private ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
	private ArrayList<PolicyLocationBuilding> locTwoBuildingList = new ArrayList<PolicyLocationBuilding>();
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();
	private String lienholderNumber = null;
	private String lienholderLoanNumber = null;

	@Test
	public void generatePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		int randomYear = DateUtils.getCenterDateAsInt(cf, ApplicationOrCenter.PolicyCenter, "yyyy") - NumberUtils.generateRandomNumberInt(0, 50);

		PolicyBusinessownersLineAdditionalCoverages myLineAddCov = new PolicyBusinessownersLineAdditionalCoverages(false, true);
		PolicyBusinessownersLine myline = new PolicyBusinessownersLine(SmallBusinessType.StoresRetail);
		myLineAddCov.setEquipmentBreakdownCoverage(true);
		myline.setAdditionalCoverageStuff(myLineAddCov);

		ArrayList<AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();

		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setYearBuilt(randomYear);
		loc1Bldg1.setClassClassification("storage");

		this.locOneBuildingList.add(loc1Bldg1);

		PolicyLocationBuilding loc2Bldg1 = new PolicyLocationBuilding();
		loc2Bldg1.setYearBuilt(randomYear);
		loc2Bldg1.setClassClassification("storage");

		AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc2Bldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);
		loc2Bldg1.setAdditionalInterestList(loc2Bldg1AdditionalInterests);

		this.locTwoBuildingList.add(loc2Bldg1);
		this.locationsList.add(new PolicyLocation(new AddressInfo(), this.locOneBuildingList));
		this.locationsList.add(new PolicyLocation(new AddressInfo(), this.locTwoBuildingList));

        myPolicyObj = new GeneratePolicy.Builder(driver)
				.withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("Disbursement Charge Group")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(myline)
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		
		this.lienholderNumber =     this.myPolicyObj.busOwnLine.locationList.get(1).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
		this.lienholderLoanNumber = this.myPolicyObj.busOwnLine.locationList.get(1).getBuildingList().get(0).getAdditionalInterestList().get(0).getLoanContractNumber();
		
	}

	@Test(dependsOnMethods = { "generatePolicy" })
	public void makeInsuredDownPayment() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);

		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), myPolicyObj.busOwnLine.getPolicyNumber());
		
		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "makeInsuredDownPayment" })
	public void moveClocks() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Month, 1);
	}

	@Test(dependsOnMethods = { "moveClocks" })
	public void makeLienholderPaymentOverMinimum() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.lienholderNumber);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();
		

        NewDirectBillPayment lienholderPayment = new NewDirectBillPayment(driver);
        lienholderPayment.makeDirectBillPaymentExecute(this.myPolicyObj.busOwnLine.getPolicyNumber(), this.lienholderLoanNumber, (myPolicyObj.busOwnLine.getPremium().getTotalAdditionalInterestPremium() + 200.00));
		
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuDisbursements();

		AccountDisbursements disbursements = new AccountDisbursements(driver);
		disbursements.clickAccountDisbursementsEdit();
		disbursements.setDisbursementsDueDate(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter));
		disbursements.clickAccountDisbursementsApprove();

		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "makeLienholderPaymentOverMinimum" })
	public void checkEventMessages() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login("su", "gw");
		
		BCTopMenu topMenu = new BCTopMenu(driver);
		topMenu.clickAdministrationTab();


		BCAdministrationMenu adminMenu = new BCAdministrationMenu(driver);
		adminMenu.clickAdminMenuMonitoring();

		AdminEventMessages eventMessages = new AdminEventMessages(driver);
		eventMessages.suspendQueue(MessageQueue.Check_Printing);
		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Disbursement);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.FBM_Disbursements_To_SunGard);

		eventMessages = new AdminEventMessages(driver);
		eventMessages.clickMessageQueue(MessageQueue.Check_Printing);
		eventMessages.selectSafeOrderObjectFilterOption(MessageQueuesSafeOrderObjectSelectOptions.Safe_Order_Object_With_Any_Unfinished_Messages);
		eventMessages.clickSafeOrderObjectLink(MessageQueuesSafeOrderObjectLinkOptions.Non_Safe_Ordered_Messages);
		int disbursementEventMessageRow = Integer.valueOf(eventMessages.getNonSafeOrderedMessagesTableRow(null, "OutgoingPaymentAdded", MessageQueue.Check_Printing, null, null, "Pending Send", null, null, null).findElement(By.xpath(".//parent::td/parent::tr")).getAttribute("data-recordindex")) + 1;
		new TableUtils(driver).clickLinkInTableByRowAndColumnName(eventMessages.getEventMessageQueuesNonSafeOrderObjectTable(), disbursementEventMessageRow, "Event Name");
		String messagePayload = eventMessages.getMessagePayload();
		if (messagePayload.contains("2:1:")) {
			Assert.fail("The message payload still contained the charge group. Test Failed.");
		}
		
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "checkEventMessages" })
	public void ensureMessageQueueIsStarted() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login("su", "gw");
		
		BCTopMenu topMenu = new BCTopMenu(driver);
		topMenu.clickAdministrationTab();


		BCAdministrationMenu adminMenu = new BCAdministrationMenu(driver);
		adminMenu.clickAdminMenuMonitoring();

		AdminEventMessages eventMessages = new AdminEventMessages(driver);
		try {
			eventMessages.resumeQueue(MessageQueue.Check_Printing);
		} catch (GuidewireException e) {
			e.printStackTrace();
		}
		
		new GuidewireHelpers(driver).logout();
	}

}

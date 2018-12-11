package regression.r2.noclock.billingcenter.rolespermissions;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonSummary;
import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.BCDesktopMyQueues;
import repository.bc.search.BCSearchAccounts;
import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.ActivityQueuesBillingCenter;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.PolicyCompany;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
public class TestDisbursementApprovalActivitiesAssignment extends BaseTest {
	private WebDriver driver;
	private String farmBureauAccountNumber = "";
	private String westernCommunityAccountNumber = "";
	private double farmBureauPaymentAmount = 450;
	private double westernCommunityPaymentAmount = 500;
	private double farmBureauUnappliedAmount = 0;
	private double westernCommunityUnapliedAmount = 0;
	private String farmBureauAccountHolderName = null;
	private String westernCommunityAccountHolderName = null;
	private ARUsers arUser;
	
	/**
	* @Author bhiltbrand
	* @Requirement This test ensures that disbursement approvals are sent to the correct queues depending on the company.
	* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/02%20-%20Activities/02-04%20Disbursement%20Approval%20Activity.docx">Requirements</a>
	* @RequirementsLink <a href="https://rally1.rallydev.com/#/41432362343d/detail/userstory/41439115964">Rally Story</a>
	* @Description 
	* @DATE Jan 13, 2016
	* @throws Exception
	*/

	@Test
	public void verifyLienholderAccount() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		this.arUser = ARUsersHelper.getARUserByUserName("sbrunson");
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

		BCSearchAccounts searchAccounts = new BCSearchAccounts(driver);
		this.farmBureauAccountNumber = searchAccounts.findAccountInGoodStanding(PolicyCompany.Farm_Bureau);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();

        NewDirectBillPayment payment = new NewDirectBillPayment(driver);
		payment.makeDirectBillPaymentExecuteWithoutDistribution(this.farmBureauPaymentAmount);

		BCCommonSummary summary = new BCCommonSummary(driver);
		this.farmBureauAccountHolderName = summary.getNameOnBillingAddressOrInsuredInfo();
		this.farmBureauUnappliedAmount = summary.getDefaultUnappliedFundsAmount();

		searchAccounts = new BCSearchAccounts(driver);
		this.westernCommunityAccountNumber = searchAccounts.findAccountInGoodStanding(PolicyCompany.Western_Community);

        accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();

        payment = new NewDirectBillPayment(driver);
		payment.makeDirectBillPaymentExecuteWithoutDistribution(this.westernCommunityPaymentAmount);

		summary = new BCCommonSummary(driver);
		this.westernCommunityAccountHolderName = summary.getNameOnBillingAddressOrInsuredInfo();
		this.westernCommunityUnapliedAmount = summary.getDefaultUnappliedFundsAmount();
		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Automatic_Disbursement);
		
		BCTopMenu topMenu = new BCTopMenu(driver);
		topMenu.clickDesktopTab();

		BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuMyQueues();

		BCDesktopMyQueues queues = new BCDesktopMyQueues(driver);
		queues.setMyQueuesFilter(ActivityQueuesBillingCenter.ARSupervisorFarmBureau);
		boolean farmBureauActivityFound = queues.verifyMyQueueTable(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), "Review and Approval Required: Automatic Disbursement of " + StringsUtils.currencyRepresentationOfNumber(this.farmBureauUnappliedAmount) + " to " + this.farmBureauAccountHolderName, this.farmBureauAccountNumber);
		if (!farmBureauActivityFound) {
			farmBureauActivityFound = queues.verifyMyQueueTable(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), "Approval Activity for Disbursement", this.farmBureauAccountNumber);		                                                                                                                 
		}
		if (!farmBureauActivityFound) {
			getQALogger().error(farmBureauAccountNumber);
			Assert.fail("The disbursement activity for Farm Bureau in the amount of " + StringsUtils.currencyRepresentationOfNumber(this.farmBureauUnappliedAmount) + " Was not found in the correct queue. Test Failed.");
		}
		
		queues.setMyQueuesFilter(ActivityQueuesBillingCenter.ARSupervisorWesternCommunity);
		boolean westernCommunityActivityFound = queues.verifyMyQueueTable(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), "Review and Approval Required: Automatic Disbursement of " + StringsUtils.currencyRepresentationOfNumber(this.westernCommunityUnapliedAmount)+ " to "+ this.westernCommunityAccountHolderName, this.westernCommunityAccountNumber);
		if (!westernCommunityActivityFound) {
			westernCommunityActivityFound = queues.verifyMyQueueTable(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), "Approval Activity for Disbursement", this.westernCommunityAccountNumber);
		}
		if (!westernCommunityActivityFound) {
			getQALogger().error(westernCommunityAccountNumber);
			Assert.fail("The disbursement activity for Western Community in the amount of " + StringsUtils.currencyRepresentationOfNumber(this.westernCommunityUnapliedAmount) + " Was not found in the correct queue. Test Failed.");
		}
	}

}

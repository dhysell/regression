package regression.r2.noclock.billingcenter.activities;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.BCAccountMenu;
import repository.bc.common.BCCommonActivities;
import repository.bc.common.actions.BCCommonActionsManualActivity;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.search.BCSearchAccounts;
import repository.bc.search.BCSearchPolicies;
import repository.driverConfiguration.Config;
import repository.gw.activity.ActivityPopup;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.Priority;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author bhiltbrand
* @Requirement This test is intended to test the ability for a user in Billing Center to be able to create, update, and complete activities
* at the Account and Policy levels.
* This does need an updated activity pattern.
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/02%20-%20Activities/02-02%20Create%20Activity%20from%20Account%20or%20Policy.docx">Create Activity from Account or Policy</a>
* @RequirementsLink <a href="https://rally1.rallydev.com/#/41432362343d/detail/userstory/41439115950">User Story in Rally</a>
* @DATE Sep 24, 2015
*/
public class TestCreateManualActivities extends BaseTest {
	private WebDriver driver;
	private String accountNumber = "";
	private String policyNumber = "";
	private ARUsers arUser;
	
	private void testFieldsThatSpanPolicyAndAccount() {
		BCCommonActionsManualActivity manualActivity = new BCCommonActionsManualActivity(driver);
		if(!manualActivity.getManualActivitySubject().equalsIgnoreCase("Manual Activity Reminder")) {
			Assert.fail("The Activity Subject did not default to 'Manual Activity Reminder'");
		}
		if(!(manualActivity.getManualActivityDueDate().equals(DateUtils.getDateValueOfFormat(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter),DateAddSubtractOptions.Day, 10), "MM/dd/yyyy"))) || (manualActivity.getManualActivityDueDate() == null)) {
			Assert.fail("The Activity Due Date was not defaulted to the current day plus 10 days");
		}
		if(!(manualActivity.getManualActivityEscalationDate() == null)) {
			Assert.fail("The Activity Escalation Date did not default to empty.");
		}
		if(!manualActivity.getManualActivityDescription().equalsIgnoreCase("")) {
			Assert.fail("The Activity Description Did not default to empty.");
		}
		if(!manualActivity.getManualActivityAssignedTo().contains((this.arUser.getFirstName() + " " + this.arUser.getLastName()))) {
			Assert.fail("The Activity Assignment did not default to the currently logged-in user.");
		}
		if(!manualActivity.getManualActivityPriority().equalsIgnoreCase(Priority.Normal.getValue())) {
			Assert.fail("The Manual Activity Priority did not default to 'Normal'");
		}
		manualActivity.clickManualActivitySubject();
	}
	
	@Test
	public void createNewActivityAtAccountLevel() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());

		BCSearchAccounts accountSearch = new BCSearchAccounts(driver);
		this.accountNumber = accountSearch.findAccountInGoodStanding("08-");
		

        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickBCMenuActionsManualActivity();


		BCCommonActionsManualActivity manualActivity = new BCCommonActionsManualActivity(driver);
		if (!manualActivity.getManualActivityAccount().equalsIgnoreCase(this.accountNumber)) {
			Assert.fail("The Account field did not prefill with the account number");
		}
		
		testFieldsThatSpanPolicyAndAccount();
		
		manualActivity.setManualActivityDueDate("");
		
		manualActivity.clickUpdate();
		
		if (new GuidewireHelpers(driver).errorMessagesExist()) {
			Assert.fail("There was an error thrown when updating the activity. The error was: " + new GuidewireHelpers(driver).getFirstErrorMessage());
		}
		
		new GuidewireHelpers(driver).logout();
	}

	@Test (dependsOnMethods = { "createNewActivityAtAccountLevel" })
    public void createNewActivityAtPolicyLevel() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());

		BCSearchPolicies policySearch = new BCSearchPolicies(driver);
		this.policyNumber = policySearch.searchPolicyByAccountNumber(this.accountNumber);
		

        BCPolicyMenu menu = new BCPolicyMenu(driver);
		menu.clickBCMenuActionsManualActivity();


		BCCommonActionsManualActivity manualActivity = new BCCommonActionsManualActivity(driver);
		if (!manualActivity.getManualActivityPolicyPeriod().contains(this.policyNumber)) {
			Assert.fail("The Account field did not prefill with the account number");
		}
		
		testFieldsThatSpanPolicyAndAccount();
		
		manualActivity.setManualActivityDueDate("");
		
		manualActivity.clickUpdate();
		
		if (new GuidewireHelpers(driver).errorMessagesExist()) {
			Assert.fail("There was an error thrown when updating the activity. The error was: " + new GuidewireHelpers(driver).getFirstErrorMessage());
		}
		
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "createNewActivityAtPolicyLevel" })
    public void editAccountActivity() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());

		BCSearchAccounts accountSearch = new BCSearchAccounts(driver);
		accountSearch.searchAccountByAccountNumber(this.accountNumber);
		

        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickBCMenuActivities();


		BCCommonActivities activities = new BCCommonActivities(driver);
		activities.clickActivityTableSubject(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), OpenClosed.Open, "Manual Activity Reminder");

		ActivityPopup activityPopUp = new ActivityPopup(driver);
		activityPopUp.setActivitySubject("Change to Activity");
		activityPopUp.setActivityDueDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Month, 3));
		activityPopUp.clickOkOrUpdate();

		activities = new BCCommonActivities(driver);
		try {
			activities.clickActivityTableSubject(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), OpenClosed.Open, "Change to Activity");
		} catch (Exception e) {
			Assert.fail("The Activity did not update as expected.");
		}
		
		new GuidewireHelpers(driver).logout();
	}
}

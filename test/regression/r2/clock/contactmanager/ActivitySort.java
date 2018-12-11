package regression.r2.clock.contactmanager;

import java.util.ArrayList;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.ab.desktop.Desktop;
import repository.driverConfiguration.Config;
import repository.gw.enums.AbActivitySearchFilter;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.generate.custom.AbUserActivity;
import repository.gw.helpers.ClockUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;
@QuarantineClass
public class ActivitySort extends BaseTest {
	private WebDriver driver;
	private AbUsers abUser;
	
	/**
	* @throws Exception 
	* @Author sbroderick
	* @Requirement Closed today, Closed yesterday, Closed in last 7 days are required to be added to the activity sort drop-down.
	* @RequirementsLink <a href="http://projects.idfbins.com/contactcenter/Documents/Story%20Cards/CM8%20-%20ContactManager%20-%20Navigation%20-%20Desktop%20Tab.xlsx">Desktop Story Card</a>
	* @Description Closed today, Closed yesterday, Closed in last 7 days are required to be added to the activity sort drop-down.
	* @DATE Jan 4, 2018
	*/
	
//	@Test
	public void testDesktopActivityFilterOptions() throws Exception {
		ArrayList<String> requiredFilterOptions = new ArrayList<String>();
		requiredFilterOptions.add("Overdue only");
		requiredFilterOptions.add("My activities today");
		requiredFilterOptions.add("Due Within 7 Days");
		requiredFilterOptions.add("All Open");
		requiredFilterOptions.add("Closed today");
		requiredFilterOptions.add("Closed yesterday");
		requiredFilterOptions.add("Closed in last 7 days");
		requiredFilterOptions.add("Closed in last 30 days");
		requiredFilterOptions.add("Closed in last 90 days");
		requiredFilterOptions.add("Closed in last year");
		requiredFilterOptions.add("All Closed");
		
		AbUsers abUser = AbUserHelper.getRandomDeptUser("Policy Services");
		Config cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);
        Login logMeIn = new Login(driver);
		logMeIn.login(abUser.getUserName(), abUser.getUserPassword());
        Desktop desktopPage = new Desktop(driver);
		ArrayList<String> filterOptions = desktopPage.getActivityFilterOptions();
		if(filterOptions.size() != requiredFilterOptions.size()) {
			Assert.fail("The required filter options should match the UI filter options");
		}
		boolean match = false;
		for(String requiredFilterOption : requiredFilterOptions) {
			match = false;
			for(String uiFilterOption : filterOptions) {
				if(uiFilterOption.equals(requiredFilterOption)) {
					match=true;
					break;
				}
			}
			if(match = false) {
				Assert.fail(requiredFilterOption + " was not found in the list of UI Activity Filter options.");
			}
		}		
	}
	
	public Desktop getToDesktopPage() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);
        Login logMeIn = new Login(driver);
		logMeIn.login(abUser.getUserName(), abUser.getUserPassword());
        return new Desktop(driver);
	}
	
	public AbUserActivity getdifferingContactsActivity(AbUserActivity previousActivity, int attempts) throws Exception {
		Desktop desktopPage = getToDesktopPage();
		AbUserActivity toReturn = desktopPage.completeRandomActivity();
		if(previousActivity.getName().equals(toReturn.getName()) && attempts<5) {
			getdifferingContactsActivity(previousActivity, attempts++);
		}
		if(previousActivity.getName().equals(toReturn.getName())) {
			Assert.fail("Unfortunately, after trying 5 times an activity with a differing contact was not found.");
		}
		return toReturn;		
	}
	
	@Test
	public void activitiesFiltered() throws Exception {
		this.abUser = AbUserHelper.getRandomDeptUser("Policy Services");
		Desktop desktopPage = getToDesktopPage();
		AbUserActivity randomActivity = desktopPage.completeRandomActivity();
		desktopPage.selectActivityFilter(AbActivitySearchFilter.ClosedToday);
		if(!desktopPage.activityExistsInTable(randomActivity)) {
			Assert.fail("The completed activity should show in the Closed today filter.");
		}
		
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);
		
		desktopPage = getToDesktopPage();
		AbUserActivity randomActivity1 = getdifferingContactsActivity(randomActivity, 0);
		desktopPage.selectActivityFilter(AbActivitySearchFilter.ClosedToday);
		if(!desktopPage.activityExistsInTable(randomActivity1)) {
			Assert.fail("The completed activity should show in the Closed today filter.");
		}
		if(desktopPage.activityExistsInTable(randomActivity)) {
			Assert.fail("Yesterdays completed activity should not show in the Completed today activity filter.");
		}
		
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);
		desktopPage = getToDesktopPage();
		AbUserActivity randomActivity2 = desktopPage.completeRandomActivity();
		desktopPage.selectActivityFilter(AbActivitySearchFilter.ClosedToday);
		if(!desktopPage.activityExistsInTable(randomActivity2)) {
			Assert.fail("Todays closed activity should show in the Closed today filter.");
		}
		if(desktopPage.activityExistsInTable(randomActivity1)) {
			Assert.fail("Seven Days Ago closed Activity should show in the Completed Seven days ago activity filter.");
		}
		if(desktopPage.activityExistsInTable(randomActivity)) {
			Assert.fail("The completed activity 8 days ago should not show in the Closed Seven Days Activity filter.");
		}
		
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 6);
		desktopPage = getToDesktopPage();
		desktopPage.selectActivityFilter(AbActivitySearchFilter.ClosedInLastSevenDays);
		if(desktopPage.activityExistsInTable(randomActivity)) {
			Assert.fail("The activity closed 8 days ago should not show in the last 7 days filter.");
		}
		
		if(!desktopPage.activityExistsInTable(randomActivity1)) {
			Assert.fail("The activity closed 7 days ago should show in the last 7 days filter.");
		}
		
		if(!desktopPage.activityExistsInTable(randomActivity2)) {
			Assert.fail("The activity closed 6 days ago should show in the last 7 days filter.");
		}
		
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 23);
		desktopPage = getToDesktopPage();
		desktopPage.selectActivityFilter(AbActivitySearchFilter.ClosedInLastThirtyDays);
		if(desktopPage.activityExistsInTable(randomActivity)) {
			Assert.fail("The activity created 31 days ago should not exist in the 30day filter.");
		}
		
		if(!desktopPage.activityExistsInTable(randomActivity1)) {
			Assert.fail("The activity closed 30 days ago should exist in the 30 day filter");
		}
		
		if(!desktopPage.activityExistsInTable(randomActivity1)) {
			Assert.fail("The activity closed 29 days ago should exist in the 30 day filter");
		}
		
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 60);
		desktopPage = getToDesktopPage();
		desktopPage.selectActivityFilter(AbActivitySearchFilter.ClosedInLastNinetyDays);
		if(desktopPage.activityExistsInTable(randomActivity)) {
			Assert.fail("The activity closed 91 days ago should not be in the ninety day filter.");
		}
		if(!desktopPage.activityExistsInTable(randomActivity1)) {
			Assert.fail("The activity closed 90 days ago should exist in the ninety day filter.");
		}
		
		if(!desktopPage.activityExistsInTable(randomActivity2)) {
			Assert.fail("The activity closed 89 days ago should exist in the ninety day filter.");
		}
		
		
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 275);
		desktopPage = getToDesktopPage();
		desktopPage.selectActivityFilter(AbActivitySearchFilter.ClosedInLastYear);
		if(desktopPage.activityExistsInTable(randomActivity)) {
			Assert.fail("The activity closed over a year ago should not exist in the is filter, because the filter gives you everything that was closed on the filter date up to the present.");
		}
		if(!desktopPage.activityExistsInTable(randomActivity1)) {
			Assert.fail("The activity closed a year ago should exist in the closed in last year filter.");
		}
		
		if(!desktopPage.activityExistsInTable(randomActivity2)) {
			Assert.fail("The activity closed less than a year ago should exist in the closed last year filter.");
		}		
	}
}

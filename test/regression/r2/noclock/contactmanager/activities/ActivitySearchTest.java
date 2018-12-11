// Steve Broderick
package regression.r2.noclock.contactmanager.activities;


import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.ab.activity.NewActivity;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.ActivitySearchAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.sidebar.SidebarAB;
import repository.ab.topmenu.TopMenuSearchAB;
import repository.driverConfiguration.Config;
import repository.gw.activity.ActivityPopup;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.entities.Contacts;
import persistence.globaldatarepo.helpers.AbUserHelper;
import persistence.globaldatarepo.helpers.ContactsHelpers;
public class ActivitySearchTest extends BaseTest {
	private WebDriver driver;
	private AbUsers user;

	@Test
	public void activitySearchTestMain() throws Exception {
		this.user = AbUserHelper.getRandomDeptUser("Policy Services");
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		createActivityForUser(this.user);
        Login lp = new Login(driver);
		lp.login(this.user.getUserName(), this.user.getUserPassword());

		TopMenuSearchAB menu = new TopMenuSearchAB(driver);
		menu.clickSearch();
		SidebarAB sidebar = new SidebarAB(driver);
		ActivitySearchAB searchPage = sidebar.clickSidebarActivitySearchLink();

		boolean activitiesExist = searchPage.checkActivitiesExistForUser(this.user);
		if (activitiesExist) {
			// Ensures all activity results have Kari Jo assigned.
			List<WebElement> assignedUsers = searchPage.getAssignedUser();
			for (int i = 0; i < assignedUsers.size(); i++) {
				if (!assignedUsers.get(i).getText().equals(user.getUserFirstName() +" "+ this.user.getUserLastName())) {
					throw new Exception("Assigned User is not " + user.getUserFirstName() +" "+ this.user.getUserLastName());
				}
			}

			// grabs name of first Activity
			String firstActivityContact = searchPage.getFirstActivityContact();
			searchPage.clickFirstActivity();
            ActivityPopup myActivity = new ActivityPopup(driver);
			myActivity.clickActivityCloseWorksheet();
            ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);

			// gets page title and compares it to the activity contact grabbed
			// above.
			String contactPageTitle = contactPage.getContactDetailsBasicsContactName();
			if (!contactPageTitle.equals(firstActivityContact)) {
				Assert.fail("Contact Page Title did not equal the Contact named on the Activity Page. \r\n Activity Name = "+firstActivityContact+" \r\n ContactName = "+contactPageTitle +".");
			}
		} else {
			Assert.fail("No activities existed for user: " + this.user.getUserFirstName() + " " + this.user.getUserLastName()  +" after creating the activity.");
		}
	}
	
	public void createActivityForUser(AbUsers user) throws Exception {
		Contacts contact = ContactsHelpers.getRandomContact("");
		String[] name = contact.getContactName().split("\\s*(=>|,|\\s)\\s*");
		String lastName = name[0];
		String[] firstMiddleName = name[1].split(" ");
		String firstName = firstMiddleName[0];
		
		AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
				 
		searchMe.loginAndSearchContact(AbUserHelper.getRandomDeptUser("Admin"), firstName, lastName, contact.getContactAddressLine1(), State.valueOf(contact.getContactState())); 
		NewActivity anotherActivity = new NewActivity(driver);
		anotherActivity.sendActivity("Review Contact", user);
		new GuidewireHelpers(driver).logout();
	}
}
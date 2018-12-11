package regression.r2.noclock.contactmanager.activities;


import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.ActivitySearchAB;
import repository.ab.sidebar.SidebarAB;
import repository.ab.topmenu.TopMenuSearchAB;
import repository.driverConfiguration.Config;
import repository.gw.activity.ActivityPopup;
import repository.gw.enums.VacationStatus;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;

/* 7-2-2018 - This test class is the start of testing features that have not been developed. The ability for a policy services user to approve another policy Services user that is on vacation is not development complete.   
			  Currently, it has not been determined but is my suspicion that activities will be going away as the ContactManager team works to make ContactManager function more like OOTB. 
			  Cheers. 
*/

public class ActivitiesForUsersOnVacation extends BaseTest {
	private WebDriver driver;
    private AbUsers vacationUser;
    private AbUsers backupUser;

    @Test
    public void testActivitiesForUsersOnVacation() throws Exception {
    	vacationStatus();
    	completeActivity();
    	workStatus();
    }

   
    public void vacationStatus() throws Exception {
        this.vacationUser = AbUserHelper.getRandomDeptUser("Policy Service");
        do {
            this.backupUser = AbUserHelper.getRandomDeptUser("Policy Services");
        } while (vacationUser.getUserName().equals(AbUserHelper.getRandomDeptUser("Policy Services").getUserName()));

        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        Login lp = new Login(driver);
        lp.login(this.vacationUser.getUserName(), this.vacationUser.getUserPassword());

        SidebarAB menu = new SidebarAB(driver);
        menu.clickActions();
        menu.clickVacationStatus();

        repository.ab.vacationstatus.VacationStatus vacationStatusPage = new repository.ab.vacationstatus.VacationStatus(driver);
        vacationStatusPage.selectVacationStatus(VacationStatus.OnVacation);
        vacationStatusPage.selectBackupUser(backupUser.getUserFirstName() + " " + backupUser.getUserLastName());
        vacationStatusPage.clickUpdate();

        new GuidewireHelpers(driver).logout();
    }

    public void completeActivity() throws Exception {
    	driver.quit();
    	Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        Login lp = new Login(driver);
        lp.login(this.backupUser.getUserName(), this.backupUser.getUserPassword());

        TopMenuSearchAB menu = new TopMenuSearchAB(driver);
        menu.clickSearch();
        SidebarAB sidebar = new SidebarAB(driver);

        ActivitySearchAB searchPage = sidebar.clickSidebarActivitySearchLink();
        
        boolean activitiesExist = searchPage.checkActivitiesExistForUser(this.backupUser);
        if (activitiesExist) {
            List<WebElement> assignedUsers = searchPage.getAssignedUser();
            for (int i = 0; i < assignedUsers.size(); i++) {
                if (assignedUsers.get(i).getText().equals(vacationUser.getUserFirstName() + " " + vacationUser.getUserLastName())) {
                    Assert.fail("In the Activity Search, the activities should only be assigned to " + this.backupUser.getUserFirstName() +" " + backupUser.getUserLastName());
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
            String contactPageTitle = contactPage.getContactPageTitle();
            String[] contactPageTitleArray=contactPageTitle.split(" ");
            if (!(firstActivityContact.contains(contactPageTitleArray[0]) && firstActivityContact.contains(contactPageTitleArray[1]))) {
                Assert.fail("Contact Page Title did not equal the Contact named on the Activity Page.");
            }
        } else {
            System.out.println("No activities existed for user: " + vacationUser.getUserName() + ".");
        }
/*
        searchPage.clickActivityByName(vacationUser.getUserFirstName() + " " + vacationUser.getUserLastName());
        ActivityPopup activity = new ActivityPopup(driver);
        activity.clickActivityComplete();
*/
    }

    public void workStatus() throws Exception {
    	driver.quit();
    	Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        Login lp = new Login(driver);
        lp.login(vacationUser.getUserName(), this.vacationUser.getUserPassword());

        SidebarAB menu = new SidebarAB(driver);
        menu.clickActions();
        menu.clickVacationStatus();

        repository.ab.vacationstatus.VacationStatus vacationStatusPage = new repository.ab.vacationstatus.VacationStatus(driver);
        vacationStatusPage.selectVacationStatus(VacationStatus.AtWork);
        vacationStatusPage.selectBackupUser(backupUser.getUserFirstName() + " " + backupUser.getUserLastName());
        vacationStatusPage.clickUpdate();

        new GuidewireHelpers(driver).logout();
    }

}

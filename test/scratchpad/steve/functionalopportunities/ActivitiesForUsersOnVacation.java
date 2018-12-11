package scratchpad.steve.functionalopportunities;


import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

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

// This test class may fail because admin data is not correct.
@QuarantineClass  //tests need to be rewritten so that they are independent.
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
        this.vacationUser = AbUserHelper.getUserByUserName("kharrild");
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

    	Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        Login lp = new Login(driver);
        lp.login(this.backupUser.getUserName(), this.backupUser.getUserPassword());

        TopMenuSearchAB menu = new TopMenuSearchAB(driver);
        menu.clickSearch();
        SidebarAB sidebar = new SidebarAB(driver);
        ActivitySearchAB searchPage = sidebar.clickSidebarActivitySearchLink();
        boolean activitiesExist = searchPage.checkActivitiesExistForUser(vacationUser);
        if (activitiesExist) {
            // Ensures all activity results have Kari Jo assigned.
            List<WebElement> assignedUsers = searchPage.getAssignedUser();
            for (int i = 0; i < assignedUsers.size(); i++) {
                if (!assignedUsers.get(i).getText().equals(vacationUser.getUserFirstName() + " " + vacationUser.getUserLastName())) {
                    throw new Exception("Assigned User is not " + vacationUser.getUserFirstName() + " " + vacationUser.getUserLastName());
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
            String[] contactNameArray = contactPageTitle.split(" ");
            if (!contactPageTitle.equals(firstActivityContact)) {
                throw new Exception("Contact Page Title did not equal the Contact named on the Activity Page.");
            }
        } else {
            System.out.println("No activities existed for user: " + vacationUser.getUserName() + ".");
        }

        searchPage.clickActivityByName(vacationUser.getUserFirstName() + " " + vacationUser.getUserLastName());
        ActivityPopup activity = new ActivityPopup(driver);
        activity.clickActivityComplete();
    }

    public void workStatus() throws Exception {
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

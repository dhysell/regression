package updateUsers;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.login.Login;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.topmenu.TopMenuAdministrationPC;
import persistence.globaldatarepo.entities.UnderwritersBAK;

/**
 * @Author jlarsen
 * @Description This class it to update the UnderwritersMasterBAK via UAT
 * test will get all users from the UI roles table and search each one for needed info
 * saves as it goes to PC_UnderwritersMasterBAK to be manually transfered to the PC_UnderwritersMaster table.
 * @DATE Sep 19, 2017
 */
public class UpdateUnderwritersTable extends BaseTest {

    private List<String> userList = new ArrayList<String>();

    private WebDriver driver;
    private BasePage bp;

    @Test(enabled = true)
    public void getUnderwriterUsers() throws Exception {

        String uwFirstName = null;
        String uwLastName = null;
        String uwUserName = null;
        List<String> uwAccess = new ArrayList<String>();
        List<String> uwRole = null;
        Config cf = new Config(ApplicationOrCenter.PolicyCenter, "DEV");
        driver = buildDriver(cf);
        UnderwritersBAK.dropTableRows();

        Login login = new Login(driver);
        login.login("su", "gw");
        UpdateUsersCommon updateUsersCommon = new UpdateUsersCommon(driver);
        this.bp = new BasePage(driver);
        userList = updateUsersCommon.getUserList("Underwriter");

        for (String user : userList) {
            //excluded users
            switch (user) {
                case "Tara Harrild":
                case "Anna Delezene":
                case "Unassigned Underwriter":
                case "Ryan Misner":
                case "Shelley Brunson":
                case "Rainie Hooker":
                case "Moonyeen Whitaker":
                case "Judy Collins":
                case "Dana Haskett":
                    continue;
            }//end switch
            TopMenuAdministrationPC topMenu = new TopMenuAdministrationPC(driver);
            topMenu.clickUserSearch();
            if (updateUsersCommon.userSearch(user) && updateUsersCommon.isActive()) {
                uwFirstName = bp.find(By.xpath("//div[contains(@id, ':FirstName-inputEl')]")).getText();
                uwLastName = bp.find(By.xpath("//div[contains(@id, ':LastName-inputEl')]")).getText();
                if (!isUnderwriter(user)) {
                    continue;
                }//end if
                uwUserName = updateUsersCommon.getUserName();
                uwAccess = updateUsersCommon.getAccess();
                uwRole = updateUsersCommon.getRoles();

                String line = "Commercial";
                for (String access : uwAccess) {
                    if (access.contains("Personal") || access.contains("PL")) {
                        line = "Personal";
                        break;
                    }//end if
                }//end for

                String title = "Underwriter";
                for (String role : uwRole) {
                    if (role.contains("Underwriting Supervisor")) {
                        title = "Underwriting Supervisor";
                        break;
                    }//end if
                }//end for
                UnderwritersBAK.createNewUnderwriterUser(uwFirstName, uwLastName, uwUserName, "gw", line, title, null);

            }//end if
        }//end for
    }//end getUnderwriterUsers() 


    private boolean isUnderwriter(String user) {
        bp.clickWhenClickable(bp.find(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_ProfileCardTab-btnEl')]")));
        String jobTitle = bp.find(By.xpath("//div[contains(@id, 'UserDetailPage:UserDetailScreen:UserProfileDV:Title-inputEl')]")).getText();
        return jobTitle.contains("Underwrit") || jobTitle.contains("Region Supervisor");
    }//end isUnderwriter(String user)
}//EOF










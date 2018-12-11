package updateUsers;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.driverConfiguration.DriverBuilder;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.topmenu.TopMenuAdministrationPC;
import persistence.globaldatarepo.entities.PAsBAK;

public class UpdatePATable extends BaseTest {

    private List<String> userList = new ArrayList<String>();

    private WebDriver driver;

    @Test(enabled = true)
    public void getPAUsers() throws Exception {

        userList = new ArrayList<String>();
        Config cf = new Config(ApplicationOrCenter.PolicyCenter, "DEV");
        this.driver = new DriverBuilder().buildGWWebDriver(cf);
        PAsBAK.dropTableRows();

        Login login = new Login(driver);
        login.login("su", "gw");
        UpdateUsersCommon updateUsersCommon = new UpdateUsersCommon(driver);
        userList = updateUsersCommon.getUserList("Production Assistant");

        for (String user : userList) {
            String userName = null;
            String associationName = null;
            String firstName = null;
            String lastName = null;
            TopMenuAdministrationPC topMenu = new TopMenuAdministrationPC(driver);
            topMenu.clickUserSearch();

            if (updateUsersCommon.userSearch(user)) {
                userName = updateUsersCommon.getUserName();
                firstName = updateUsersCommon.getFirstName2();
                lastName = updateUsersCommon.getLastName2();
                associationName = updateUsersCommon.getAssociation();
                System.out.println("Adding: " + userName + " to the database");
                PAsBAK.createNewPA(user, userName, firstName, lastName, associationName);

            }
        }
    }


}

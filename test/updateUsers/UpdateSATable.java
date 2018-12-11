package updateUsers;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.driverConfiguration.DriverBuilder;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.topmenu.TopMenuAdministrationPC;
import persistence.globaldatarepo.entities.SAsBAK;

public class UpdateSATable extends BaseTest {

    private List<String> userList = new ArrayList<String>();

    static

    @BeforeClass
    public void beforeClass() {

    }

    @AfterClass(alwaysRun = true)
    public void afterClass() throws Exception {

    }

    private WebDriver driver;

    @Test(enabled = true)
    public void getSAUsers() throws Exception {

        userList = new ArrayList<String>();

        SAsBAK.dropTableRows();

        Config cf = new Config(ApplicationOrCenter.PolicyCenter, "DEV");
        this.driver = new DriverBuilder().buildGWWebDriver(cf);
        UpdateUsersCommon updateUsersCommon = new UpdateUsersCommon(driver);
        Login login = new Login(driver);
        login.login("su", "gw");

        userList = updateUsersCommon.getUserList("Service Associate");

        for (String user : userList) {
            String userName = null;
            String associationName = null;
            String firstName = null;
            String lastName = null;
            TopMenuAdministrationPC topMenu = new TopMenuAdministrationPC(driver);
            topMenu.clickUserSearch();

            if (updateUsersCommon.userSearch(user) && updateUsersCommon.isActive()) {
                userName = updateUsersCommon.getUserName();
                firstName = updateUsersCommon.getFirstName2();
                lastName = updateUsersCommon.getLastName2();
                associationName = updateUsersCommon.getAssociation();
                System.out.println("Adding: " + userName + " to the database");
                SAsBAK.createNewSA(user, userName, firstName, lastName, associationName);
            }
        }
    }
}

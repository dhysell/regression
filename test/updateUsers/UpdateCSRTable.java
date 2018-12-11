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
import persistence.globaldatarepo.entities.CSRsBAK;

public class UpdateCSRTable extends BaseTest {

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
    public void getCSRUsers() throws Exception {

        userList = new ArrayList<String>();

        Config cf = new Config(ApplicationOrCenter.PolicyCenter, "DEV");
        this.driver = new DriverBuilder().buildGWWebDriver(cf);

        CSRsBAK.dropTableRows();

        Login login = new Login(driver);
        login.login("su", "gw");
        UpdateUsersCommon updateUsersCommon = new UpdateUsersCommon(driver);
        userList = updateUsersCommon.getUserList("CSR");

        for (String user : userList) {
            TopMenuAdministrationPC topMenu = new TopMenuAdministrationPC(driver);
            topMenu.clickUserSearch();
            if (user.contains("Janeth Ortiz") ||
                    user.contains("Barbara McClure") ||
                    user.contains("Michelle Coffin") ||
                    user.contains("Betty Inskeep") ||
                    user.contains("Christa Bankhead") ||
                    user.contains("Mitch Calhoun") ||
                    user.contains("CSR")) {
                continue;
            }


            if (updateUsersCommon.userSearch(user) && updateUsersCommon.isActive()) {
                String firstName = null;
                String lastName = null;
                String userName = null;
                String associationName = null;
                String region = null;
                String county = null;
                firstName = updateUsersCommon.getFirstName2();
                lastName = updateUsersCommon.getLastName2();
                userName = updateUsersCommon.getUserName();
                region = updateUsersCommon.getRegion();
                county = updateUsersCommon.getUserCounty().replace(" County", "");
                System.out.println("Adding: " + userName + " to the database");
                CSRsBAK.createNewCSR(firstName, lastName, userName, associationName, region, "gw", county);

            }
        }
    }


}

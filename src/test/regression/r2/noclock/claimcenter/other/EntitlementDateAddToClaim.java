package regression.r2.noclock.claimcenter.other;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
public class EntitlementDateAddToClaim extends BaseTest {
    private String userName = ClaimsUsers.abatts.toString();
    private String password = "gw";
    private WebDriver driver;

    @Test
    public void addEntitlementDateToContact() throws Exception {

        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);

        TopMenu topMenu = new TopMenu(driver);

        new Login(driver).login(userName, password);

        topMenu.searchMedicareSection111().getRandomReadyReportableClaim();
    }
}

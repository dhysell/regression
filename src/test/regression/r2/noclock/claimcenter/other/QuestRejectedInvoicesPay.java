package regression.r2.noclock.claimcenter.other;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
public class QuestRejectedInvoicesPay extends BaseTest {
	private WebDriver driver;
    // Login Info
    private ClaimsUsers userName = ClaimsUsers.abatts;
    private String password = "gw";

    @Test()
    public void controlNumberSearch() throws Exception {

        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        TopMenu topMenu = new TopMenu(driver);

        new Login(driver).login(userName.toString(), password);

        topMenu.clickDesktopTab().clickQuestRejectedInvoices().searchByControlNumberRandom();


    }
}

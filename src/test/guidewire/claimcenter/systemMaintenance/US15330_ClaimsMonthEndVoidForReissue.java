package guidewire.claimcenter.systemMaintenance;

import com.idfbins.driver.BaseTest;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.cc.claim.CheckDetails;
import repository.cc.claim.searchpages.SearchChecksCC;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.login.Login;

/**
 * @Author Denver Hysell
 * @Description - Testing US15330
 * @DATE 06/11/2018
 */

public class US15330_ClaimsMonthEndVoidForReissue extends BaseTest {

    // Login Info
    private ClaimsUsers userName = ClaimsUsers.abatts;
    private String password = "gw";

    @Test
    public void voidForReissue() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter, "DEV");
        WebDriver driver = buildDriver(cf);

        TopMenu topMenu = new TopMenu(driver);

        new Login(driver).login(userName.toString(), password);

        SearchChecksCC checkSearch = topMenu.clickSearchTab().clickCheckSearch();
        CheckDetails checkDetails = checkSearch.searchRegularChecks();
        checkDetails.voidForReissue();

        try {
            checkDetails.validateVoidedName(userName);
        } catch (Exception e) {
            Assert.fail("Cannot confirm that check was properly voided.");
        }

    }

}

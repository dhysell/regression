package scratchpad.rusty.misc;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountClaimsPC;
import repository.pc.account.AccountsSideMenuPC;
import repository.pc.topmenu.TopMenuPC;

public class AccountClaims8Test extends BaseTest {

    public AccountClaims8Test() {
        super();
    }

    private String username = "su";
    private String password = "gw";
    private WebDriver driver;

    @Test
    public void testDesktopMenu() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        TopMenuPC topMenu = new TopMenuPC(driver);

        topMenu.clickAccountTab();//get to accounts tab

        AccountsSideMenuPC accountSidebar = new AccountsSideMenuPC(driver);
        accountSidebar.clickClaims();

        AccountClaimsPC claims = new AccountClaimsPC(driver);
        claims.clickRadioSince();
        claims.clickRadioFrom();
        claims.setPolicyPeriod("No policy in force");
        claims.setProduct("Businessowners");
        //*[@id="AccountFile_Claims:AccountFile_ClaimScreen:SearchAndResetInputSet:SearchLinksInputSet:Search"]
        WebElement search = claims.find(By.xpath("//a[contains(@id, 'SearchLinksInputSet:Search')]"));
        search.click();
    }
}

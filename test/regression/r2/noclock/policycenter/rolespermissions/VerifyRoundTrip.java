package regression.r2.noclock.policycenter.rolespermissions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyDocuments;
import repository.pc.sidemenu.SideMenuPC;
import persistence.globaldatarepo.entities.PcRoundTrip;
import persistence.globaldatarepo.helpers.RoundTripHelper;

@QuarantineClass
public class VerifyRoundTrip extends BaseTest {

    @Test(enabled = true)
    public void verifyRoundTripping() throws Exception {
        PcRoundTrip roundTrip = RoundTripHelper.getRoundTrip();

        if (roundTrip != null) {
            Config cf = new Config(ApplicationOrCenter.PolicyCenter);
            WebDriver driver = buildDriver(cf);
            System.out.println(String.valueOf(roundTrip.getAccountNumber()));
            new Login(driver).loginAndSearchPolicyByAccountNumber("hhill", "gw", String.valueOf(roundTrip.getAccountNumber()));
            SideMenuPC menu = new SideMenuPC(driver);
            menu.clickSideMenuToolsDocuments();
            PolicyDocuments docs = new PolicyDocuments(driver);
            docs.clickReset();
            docs.clickSearch();
            if (docs.finds(By.xpath("//a[contains(text(), 'Businessowners Full Application')]/parent::div/parent::td/following-sibling::td/div/a[contains(text(), 'View')]")).isEmpty()) {
                Assert.fail(driver.getCurrentUrl() + "Documents did not Round Trip");
            }
        } else {
            throw new SkipException("No Account from yesterday because other test failed that should put them into the database");
        }

    }

}

package regression.r2.noclock.policycenter.rolespermissions;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.bc.search.BCSearchAccounts;
import repository.driverConfiguration.Config;
import repository.gw.enums.AccountType;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

@QuarantineClass
public class TestBCToPCCenterConnect extends BaseTest {

    String accountNumber = null;

    private WebDriver driver;

    @Test
    public void verifyButtonDoesNotExistOnAccountScreen() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        ARUsers arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        new Login(driver).login(arUser.getUserName(), arUser.getPassword());

        BCSearchAccounts searchAccount = new BCSearchAccounts(driver);
        this.accountNumber = searchAccount.findRecentAccountInGoodStanding(null, AccountType.Lienholder);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickBCMenuCharges();


        AccountCharges accountCharges = new AccountCharges(driver);
        String mainWindowHandle = null;
        try {
            mainWindowHandle = accountCharges.clickChargeContextCenterConnectHyperlink(null, this.accountNumber, null, "anyText");
        } catch (Exception e) {
            Assert.fail("The test was not able to switch to the pop-up window that should have been there. This most likely indicates that permissions for the agent role were not in place for the Policy link to be clickable. Please verify.");
        }
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();
        driver.switchTo().window(mainWindowHandle);
        guidewireHelpers.logout();
    }
}

package regression.r2.noclock.policycenter.rolespermissions;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.cc.claim.policypages.PolicyGeneral;
import repository.cc.claim.searchpages.AdvancedSearchCC;
import repository.cc.sidemenu.SideMenuCC;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.Config;
import repository.driverConfiguration.DriverBuilder;
import repository.gw.enums.ClaimSearchLineOfBusiness;
import repository.gw.enums.ClaimsUsers;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;

@QuarantineClass
public class TestCCToPCCenterConnect extends BaseTest {

    String accountNumber = null;
    private String userName = ClaimsUsers.sdavis.toString();
    private String password = "gw";

    @Test
    public void verifyCenterConnectButtonWorks() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        WebDriver driver = new DriverBuilder().buildGWWebDriver(cf);
        new Login(driver).login(userName, password);

        TopMenu topMenu = new TopMenu(driver);
        topMenu.clickClaimsAdvancedSearchLink();


        AdvancedSearchCC advancedSearch = new AdvancedSearchCC(driver);
        advancedSearch.findRandomClaimFromLineOfBusiness(ClaimSearchLineOfBusiness.BOP_General_Liability);


        SideMenuCC sideMenu = new SideMenuCC(driver);
        sideMenu.clickPolicyLink();


        PolicyGeneral policyGeneralPage = new PolicyGeneral(driver);
        String mainWindowHandle = null;
        try {
            mainWindowHandle = policyGeneralPage.clickViewInPolicyCenterButton();
        } catch (Exception e) {
            Assert.fail("The test was not able to switch to the pop-up window that should have been there. This most likely indicates that permissions for the adjuster role were not in place for the Policy link to be clickable. Please verify.");
        }


        PolicySummary policySummary = new PolicySummary(driver);
        try {
            policySummary.getAddress();
        } catch (Exception e) {
            Assert.fail("The account summary pop up window for PC did not enter on the account summary page. Test failed.");
        }
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();
        driver.switchTo().window(mainWindowHandle);
        guidewireHelpers.logout();
    }
}

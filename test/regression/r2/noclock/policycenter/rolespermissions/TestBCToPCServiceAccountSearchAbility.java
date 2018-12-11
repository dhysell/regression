package regression.r2.noclock.policycenter.rolespermissions;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.common.BCCommonDocuments;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.search.BCSearchPolicies;
import repository.driverConfiguration.Config;
import repository.gw.enums.PolicySearchPolicyProductType;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.topmenu.TopMenuPC;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

@QuarantineClass
public class TestBCToPCServiceAccountSearchAbility extends BaseTest {
    String policyNumber = null;
    private WebDriver driver;

    /**
     * @throws Exception
     * @Author bhiltbrand
     * @Requirement The center connect button from BC to PC was allowing users to search for accounts in PC after opening a restricted window. That was fixed to not allow that
     * functionality. This test ensures that it stays that way.
     * @RequirementsLink <a href="https://rally1.rallydev.com/#/7832667974d/detail/defect/53429519905">Rally Defect DE3467</a>
     * @Description
     * @DATE May 17, 2016
     */
    @Test
    public void verifyButtonDoesNotExistOnAccountScreen() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ARUsers arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        new Login(driver).login(arUser.getUserName(), arUser.getPassword());

        BCSearchPolicies policySearch = new BCSearchPolicies(driver);
        this.policyNumber = policySearch.findPolicyInGoodStanding("236", null, PolicySearchPolicyProductType.Business_Owners);

        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
        policyMenu.clickBCMenuDocuments();


        BCCommonDocuments policyDocuments = new BCCommonDocuments(driver);
        String mainWindowHandle = null;
        try {
            mainWindowHandle = policyDocuments.clickViewInPolicyCenterButton();
        } catch (Exception e) {
            Assert.fail("The test was not able to switch to the pop-up window that should have been there. This most likely indicates that permissions for the agent role were not in place for the Policy link to be clickable. Please verify.");
        }


        try {
            TopMenuPC topMenu = new TopMenuPC(driver);
            topMenu.clickSearchTab();
            Assert.fail("The Search tab was available on the page and was clicked. This should not happen. Test failed.");
        } catch (Exception e) {
            System.out.println("The Search tab was not available on the page. This was expected. Test passed.");
        }
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();

        driver.switchTo().window(mainWindowHandle);
        guidewireHelpers.logout();
    }
}
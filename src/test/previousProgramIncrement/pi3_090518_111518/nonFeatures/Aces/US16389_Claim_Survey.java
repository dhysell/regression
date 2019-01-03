package previousProgramIncrement.pi3_090518_111518.nonFeatures.Aces;

import repository.cc.claim.searchpages.AdvancedSearchCC;
import repository.cc.desktop.ClaimSurveyListing;
import repository.cc.sidemenu.SideMenuCC;
import repository.cc.topmenu.TopMenu;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimSearchLineOfBusiness;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.Status;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class US16389_Claim_Survey extends BaseTest {
    private WebDriver driver;
    private ClaimsUsers user = ClaimsUsers.emacdonald;
    private String password = "gw";

    private String claimNumber = null;
    private String env = "DEV";

    // FNOL Specific Strings
    private String incidentName = "Random";
    private String lossDescription = "Loss Description Test";
    private String lossCause = "Random";
    private String lossRouter = "Random";
    private String address = "Random";
    private String policyNumber = "Random";

    @Test
    public void claimSurvey() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter, env);
        driver = buildDriver(cf);

        new Login(driver).login( user.toString(), password);

        TopMenu topMenu = new TopMenu(driver);
        AdvancedSearchCC search = topMenu.clickClaimsAdvancedSearchLink();
        search.findRandomClaimFromLineOfBusiness(ClaimSearchLineOfBusiness.City_Squire_Policy_Auto, Status.Closed);
        String policyNumber = topMenu.getPolicyNumber();
        String dateOfLoss = topMenu.getDateOfLoss();
        String lossType = topMenu.getLossType();
        this.claimNumber = topMenu.getClaimNumber();

        topMenu.clickDesktopTab();
        SideMenuCC sideMenu = new SideMenuCC(driver);
        ClaimSurveyListing claimSurvey = sideMenu.clickClaimSurvey().populateClaimSurvey(policyNumber, dateOfLoss,
                lossType, claimNumber);
        if (!claimSurvey.confirmSurveyAdded(claimNumber)) {
            Assert.fail("Claim Survey Not Found");
        }

        new GuidewireHelpers(driver).logout();
    }
}

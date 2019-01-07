package guidewire.claimcenter.nonEpicFeatures.F298_EmploymentPracticeLiabilityCoverageBOP;

import com.idfbins.driver.BaseTest;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.cc.claim.policypages.PolicyGeneral;
import repository.cc.claim.policypages.PolicyLocations;
import repository.cc.claim.searchpages.SearchPageCC;
import repository.cc.enums.Coverages;
import repository.cc.sidemenu.SideMenuCC;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimSearchLineOfBusiness;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.Status;
import repository.gw.login.Login;

public class US12008_NewBOP_EPLI_Coverage extends BaseTest {

    private ClaimsUsers user = ClaimsUsers.abatts;
    private String password = "gw";
    private String claimNumber = null;
    private WebDriver driver;

    @Test
    public void EPLI() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter, "DEV");
        driver = buildDriver(cf);

        TopMenu topMenu = new TopMenu(driver);

        new Login(driver).login(user.toString(), password);

        SearchPageCC search = topMenu.clickSearchTab();
        String claimNumber = search.clickAdvancedSearch().findRandomClaimFromLineOfBusiness(ClaimSearchLineOfBusiness.BOP_General_Liability, Status.Open);

        SideMenuCC sideMenu = new SideMenuCC(driver);
        sideMenu.clickPolicyLink();
        try {
            sideMenu.clickPolicyLocations().clickAddDeleteLocationsButton();
            sideMenu.clickOK();
        } catch (Exception e) {
            System.out.println("Coverage in Question.");
        }

        PolicyGeneral policy = sideMenu.clickPolicyLink();
        policy.clickEditButton();
        policy.clickAddPolicyLevelCoverage();
        policy.buildPolicyLevelCoverage(Coverages.EPLI);
        PolicyLocations policyUpdate = sideMenu.clickPolicyLocations();
        try {
            policyUpdate.clickUpdateButton();
        } catch (Exception e) {
            System.out.println("Update not required.");
        }
        sideMenu.clickPolicyLink();

        if (!policy.confirmCoverageAdded(Coverages.EPLI)) {
            Assert.fail("Coverage was not successfully added to policy.");
        }

        this.claimNumber = topMenu.gatherClaimNumber();
        System.out.println("EPLI coverage added to Claim Number: " + this.claimNumber);
    }
}

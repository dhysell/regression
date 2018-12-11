package regression.r2.noclock.claimcenter.other;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.YesOrNo;
import com.idfbins.testng.listener.QuarantineClass;

import repository.cc.claim.searchpages.MedicareSearchsCC;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimStatus;
import repository.gw.enums.ClaimsUsers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
@QuarantineClass
public class TestMedicareSearches extends BaseTest {
	private WebDriver driver;
    private ClaimsUsers user = ClaimsUsers.gmurray;
    private String password = "gw";
    private String flagshipId = "";
    private String referredDate = "";

    /**
     * @Author iclouser
     * @Requirement Make sure you can search for claims that are reportable to medicare.
     * @RequirementsLink <a href="https://rally1.rallydev.com/#/10552165958d/detail/userstory/53958016624">Rally Story</a>
     * @Description Navigates to Medicare Search, searches for all reportable medicare claims.
     * Gets a random flaghshipNumber and randomReferredDate from the results table for later tests.
     * @DATE Apr 14, 2016
     */
    @Test
    public void medicareSearchBasic() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        new Login(driver).login(user.toString(), password);

        TopMenu topMenu = new TopMenu(driver);

        MedicareSearchsCC medSearch = topMenu.clickSearchTab().clickMedicareSearch111();

        flagshipId = medSearch.searchByReportable("Yes").getRandomFlagshipNumber();

        referredDate = medSearch.getRandomReferredDate();
    }


    /**
     * @Author iclouser
     * @Requirement Make sure you can search by a flagship ID.
     * @RequirementsLink See initial java doc.
     * @DATE Apr 15, 2016
     */
    @Test(dependsOnMethods = {"medicareSearchBasic"})
    public void medicareSearchByFlagshipID() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        new Login(driver).login(user.toString(), password);

        TopMenu topMenu = new TopMenu(driver);
        topMenu.clickSearchTab().clickMedicareSearch111().searchByFlagShipID(flagshipId).checkResultsWereGenerated();
    }

    /**
     * @Author iclouser
     * @Requirement Make sure you can search by a date
     * @RequirementsLink See initial java doc.
     * @DATE Apr 18, 2016
     */
    @Test(dependsOnMethods = {"medicareSearchBasic"})
    public void medicareSearchByReferredDate() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        new Login(driver).login(user.toString(), password);

        TopMenu topMenu = new TopMenu(driver);

        topMenu.clickSearchTab().clickMedicareSearch111().searchByReferredDate(referredDate).checkResultsWereGenerated();

    }

    /**
     * @Author iclouser
     * @Requirement Make sure you can filter by ready to report
     * @DATE Apr 28, 2016
     */
    @Test(dependsOnMethods = {"medicareSearchBasic"})
    public void medicareSearchByReadyToReport() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        new Login(driver).login(user.toString(), password);

        TopMenu topMenu = new TopMenu(driver);
        MedicareSearchsCC medMenu = topMenu.clickSearchTab().clickMedicareSearch111().searchByReadyToReport(YesOrNo.Yes);

        medMenu.searchByReadyToReport(YesOrNo.No);
    }


    /**
     * @Author iclouser
     * @Requirement Make sure you can filter by adjuster.
     * @RequirementsLink <a href="https://rally1.rallydev.com/#/10552165958d/detail/userstory/54673523350">Rally Story</a>
     * @DATE Apr 28, 2016
     */
    @Test
    public void medicareSearchByAdjuster() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        new Login(driver).login(user.toString(), password);
        TopMenu topMenu = new TopMenu(driver);
        topMenu.clickSearchTab().clickMedicareSearch111().searchByAdjuster();

    }


    /**
     * @Author iclouser
     * @Requirement Make sure you can filter by claim status.
     * @RequirementsLink <a href="https://rally1.rallydev.com/#/10552165958d/detail/userstory/54673523350">Rally Story</a>
     * @DATE Apr 28, 2016
     */
    @Test
    public void medicareSearchByClaimStatus() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        new Login(driver).login(user.toString(), password);
        TopMenu topMenu = new TopMenu(driver);
        topMenu.clickSearchTab().clickMedicareSearch111().searchByClaimStatus(ClaimStatus.Open);
    }
}

package previousProgramIncrement.pi2_062818_090518.f93_LexisNexisPoliceReportsIntegration_CC;

import repository.cc.claim.LossDetails;
import repository.cc.claim.searchpages.AdvancedSearchCC;
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

public class US15679_PoliceReportDocuments extends BaseTest {

    private ClaimsUsers user = ClaimsUsers.abatts;
    private String password = "gw";
    private WebDriver driver;

    @Test()
    public void PoliceReportDocuments() throws Exception {

        Config cf = new Config(ApplicationOrCenter.ClaimCenter, "DEV");
        this.driver = buildDriver(cf);

        new Login(this.driver).login(user.toString(), password);

        TopMenu topMenu = new TopMenu(this.driver);
        topMenu.clickAdministrationTab();

        SideMenuCC sideMenu = new SideMenuCC(this.driver);

        AdvancedSearchCC search = topMenu.clickSearchTab().clickAdvancedSearch();
        search.findRandomClaimFromLineOfBusiness(ClaimSearchLineOfBusiness.City_Squire_Policy_Auto, Status.Open);

        LossDetails lossDetails = sideMenu.clickLossDetailsLink();

        int currentReport;
        try {
            currentReport = lossDetails.getNumberPoliceReports();
        } catch (Exception e) {
            currentReport = 0;
        }

        lossDetails.clickOrderNewPoliceReport().clickRandomDelayTest();

        if (!lossDetails.confirmPoliceReportStatus("Waiting To Be Delivered", currentReport+1)) {
            Assert.fail("Police report order status was not \"Waiting To Be Delivered\"");
        }

        lossDetails.clickOrderNewPoliceReport().clickRandomSuccessTest();

        if (!lossDetails.confirmPoliceReportStatus("Ready", currentReport+1)) {
            Assert.fail("Police report order status was not \"Ready\"");
        }

        new GuidewireHelpers(this.driver).logout();
    }

}

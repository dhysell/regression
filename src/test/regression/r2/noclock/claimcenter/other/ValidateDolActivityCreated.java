package regression.r2.noclock.claimcenter.other;

import java.text.ParseException;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.cc.claim.LossDetails;
import repository.cc.claim.WorkplanCC;
import repository.cc.sidemenu.SideMenuCC;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
public class ValidateDolActivityCreated extends BaseTest {
	private WebDriver driver;
    private ClaimsUsers userName = ClaimsUsers.abatts;
    private String passWord = "gw";
    private String activityRefresh = "Date of Loss Changed - Policy Refresh Required";
    private String activityRefreshed = "Policy has been refreshed please see notes.";

    private String claimNumber = null;

    /**
     * @throws GuidewireClaimCenterException
     * @throws ParseException
     * @Author iclouser
     * @Requirement Make sure activities are generated to when the dol is changed. An activity to be refreshed and and activity saying the policy has been refreshed.
     * @RequirementsLink <a href="https://rally1.rallydev.com/#/10552165958d/detail/userstory/56420254925">Rally Story</a>
     * @Description
     * @DATE Jun 10, 2016
     */
    @Test

    public void changeDolCheckForActivity() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);

        new Login(driver).login(userName.toString(), passWord);
        TopMenu topMenu = new TopMenu(driver);
        SideMenuCC sideMenu = new SideMenuCC(driver);

        claimNumber = topMenu.clickSearchTab().clickAdvancedSearch().findRandomClaimReadyForCheckWriting();
        System.out.println("The claim number is: " + claimNumber);

        LossDetails lossDetails = sideMenu.clickLossDetailsLink();
        lossDetails.clickEdit();


        lossDetails.changeDolByXDays(-5);

        lossDetails.clickUpdateButton();

        claimNumber = topMenu.gatherClaimNumber();

        WorkplanCC workplan = sideMenu.clickWorkplanLink();
        workplan.findAndApproveActivityByName(activityRefresh);

        workplan.completeActivityAsPersonAssignedTo(activityRefreshed, claimNumber);

    }

}

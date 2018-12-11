package previousProgramIncrement.pi3_090518_111518.f42_ChecksOffTheMainframe;

import repository.cc.actionsmenu.ActionsMenu;
import repository.cc.claim.Recovery;
import repository.cc.topmenu.TopMenu;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimSearchLineOfBusiness;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.generate.cc.GenerateExposure;
import repository.gw.generate.cc.GenerateFNOL;
import repository.gw.generate.cc.GenerateReserve;
import repository.gw.generate.cc.ReserveLine;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
public class US16300_RecoveryCreation extends BaseTest {
	private WebDriver driver;
    private ClaimsUsers user = ClaimsUsers.gmurray;
    private String password = "gw";

    private String claimNumber = "";
    private String env = "DEV";

    // FNOL Specific Strings
    private String incidentName = "Random";
    private String lossDescription = "Comprehensive Exposure Test";
    private String lossCause = "Collision and Rollover";
    private String lossRouter = "Minor Incident";
    private String address = "Random";
    //private String policyNumber = "08-284915-01";
    private String policyNumber = "01-105260-03";

    // Exposure Strings.
    private String exposureType = "Comprehensive";

    // FNOL
    @Test()
    public void massRecoveryCreation() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter, env);
        driver = buildDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withLOB(ClaimSearchLineOfBusiness.City_Squire_Policy_Auto)
                .withSpecificIncident(incidentName)
                .withLossDescription(lossDescription)
                .withLossCause(lossCause)
                .withLossRouter(lossRouter)
                .withtopLevelCoverage(exposureType)
                .withAdress(address)
                .withPolicyNumber(policyNumber)
                .build(GenerateFNOLType.Auto);

        this.claimNumber = myFNOLObj.claimNumber;
        this.incidentName = myFNOLObj.specificIncident;

        new GuidewireHelpers(driver).logout();

        GenerateExposure exposureObj = new GenerateExposure.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withCoverageType(exposureType)
                .build();

        new GuidewireHelpers(driver).logout();

        ArrayList<ReserveLine> lines = new ArrayList<>();
        ReserveLine line1 = new ReserveLine(incidentName);
        lines.add(line1);

        GenerateReserve myClaimObj = new GenerateReserve.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withReserveLines(lines)
                .build();

        new GuidewireHelpers(driver).logout();

        ClaimsUsers user = ClaimsUsers.adegiulio;
        new Login(driver).login(user.toString(), password);

        TopMenu topMenu = new TopMenu(driver);
        topMenu.clickClaimTabArrow();
        topMenu.setClaimNumberSearch(claimNumber);

        ActionsMenu actions = new ActionsMenu(driver);
        actions.clickActionsButton();
        actions.clickOtherLink();
        actions.clickRecoveryLink();

        Recovery recovery = new Recovery(driver);
        List<String> options = recovery.getRecoveryCategories();
        recovery.clickCancelButton();

        for (String option: options) {
            actions = new ActionsMenu(driver);
            actions.clickActionsButton();
            actions.clickOtherLink();
            actions.clickRecoveryLink();
            recovery.addRecoveries(option);
        }

        new GuidewireHelpers(driver).logout();
        System.out.println(myClaimObj.claimNumber);
    }

}

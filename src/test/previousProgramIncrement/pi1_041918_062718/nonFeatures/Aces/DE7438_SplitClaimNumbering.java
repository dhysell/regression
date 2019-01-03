package previousProgramIncrement.pi1_041918_062718.nonFeatures.Aces;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.cc.actionsmenu.ActionsMenu;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimSearchLineOfBusiness;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.generate.cc.GenerateFNOL;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
public class DE7438_SplitClaimNumbering extends BaseTest {

    private ClaimsUsers user = ClaimsUsers.gmurray;
    private String password = "gw";

    private String claimNumber = null;

    // FNOL Specific Strings
    private GenerateFNOLType claimType = GenerateFNOLType.Auto;
    private String incidentName = "Random";
    private String lossDescription = "Split Claim Test";
    private String lossCause = "Collision and Rollover";
    private String lossRouter = "Minor Incident";
    private String address = "Random";
    private String policyNumber = "01-017070-01";
    private WebDriver driver;

    // FNOL
    @Test
    public void fnol() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter, "DEV");
        System.out.println(cf.getEnv());
        driver = buildDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withLOB(ClaimSearchLineOfBusiness.City_Squire_Policy_Auto)
                .withSpecificIncident(incidentName)
                .withLossDescription(lossDescription)
                .withLossCause(lossCause)
                .withLossRouter(lossRouter)
                .withAdress(address)
                .withPolicyNumber(policyNumber)
                .build(claimType);

        this.claimNumber = myFNOLObj.claimNumber;
        this.incidentName = myFNOLObj.specificIncident;
    }

    @Test(dependsOnMethods = "fnol")
    public void split() throws Exception {

        ClaimsUsers user = ClaimsUsers.abatts;

        Config cf = new Config(ApplicationOrCenter.ClaimCenter, "DEV");
        driver = buildDriver(cf);

        new Login(driver).login(user.toString(), password);

        TopMenu topMenu = new TopMenu(driver);
        topMenu.clickClaimTabArrow();
        topMenu.setClaimNumberSearch(claimNumber);

        ActionsMenu actions = new ActionsMenu(driver);
        String newClaimNumber = actions.createSplitClaimWithOrWithoutNotes();

        if (!newClaimNumber.equalsIgnoreCase(topMenu.gatherClaimNumber())) {
            Assert.fail("Claim Number: " + newClaimNumber);
        }

        System.out.println("test");
    }
}

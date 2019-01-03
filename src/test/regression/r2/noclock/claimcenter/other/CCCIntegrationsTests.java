package regression.r2.noclock.claimcenter.other;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.cc.claim.CCCIntegrations;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.generate.cc.GenerateExposure;
import repository.gw.generate.cc.GenerateFNOL;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
public class CCCIntegrationsTests extends BaseTest {
	private WebDriver driver;
    private ClaimsUsers user = ClaimsUsers.sdavis;
    private String password = "gw";

    private String claimNumber = null;

    // FNOL Specific Strings
    private String incidentName = "Random";
    private String lossDescription = "Loss Description Test";
    private String lossCause = "Collision and Rollover";
    private String lossRouter = "Major Incident";
    private String address = "Random";
    private String policyNumber = "Random";
    private String coverageNeeded = "Collision";

    // Exposure Strings.
    private String exposureType = "Collision";

    // FNOL
    @Test
    public void collisionFNOL() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName)
                .withLossDescription(lossDescription)
                .withtopLevelCoverage(coverageNeeded)
                .withLossCause(lossCause)
                .withLossRouter(lossRouter)
                .withAdress(address)
                .withPolicyNumber(policyNumber)
                .build(GenerateFNOLType.Auto);

        this.claimNumber = myFNOLObj.claimNumber;

    }

    // Exposure
    @Test(dependsOnMethods = {"collisionFNOL"})
    public void createExposureAutoLiabilityPD_Property() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateExposure exposureObj = new GenerateExposure.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withCoverageType(exposureType)
                .build();

        System.out.println(exposureObj.getClaimNumber());
    }

    // CCC Integrations
    @Test(dependsOnMethods = {"createExposureAutoLiabilityPD_Property"})
    public void runCCCIntegrations() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        new Login(driver).login( user.toString(), password);
        TopMenu topMenu = new TopMenu(driver);
        CCCIntegrations ccc = new CCCIntegrations(driver);

        topMenu.clickClaimTabArrow();
        topMenu.setClaimNumberSearch(claimNumber);

        ccc.prepareCCCIncident();

    }
}

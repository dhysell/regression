package scratchpad.denver.old.icd;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.driverConfiguration.DriverBuilder;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.generate.cc.GenerateExposure;
import repository.gw.generate.cc.GenerateFNOL;
import repository.gw.helpers.GuidewireHelpers;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class ICDCodeSearch extends BaseTest {

    private ClaimsUsers user = ClaimsUsers.gmurray;
    private String password = "gw";

    private String claimNumber = null;
    private WebDriver driver;

    // FNOL Specific Strings
    private String incidentName = "Random";
    private String lossDescription = "Med Payments Test";
    private String lossCause = "Collision and RollOver";
    private String lossRouter = "Minor Incident";
    private String address = "Random";
    private String policyNumber = "Random";
    private String coverageNeeded = "Medical Payments";

    // Exposure Strings.
    private String exposureType = "Medical Payments";

    @AfterMethod(alwaysRun = true)
    public void tearDownDriver() {
        this.driver.quit();
    }

    // FNOL
    @Test
    public void medPayFNOL() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        this.driver = new DriverBuilder().buildGWWebDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(this.driver).withCreatorUserNamePassword(user.toString(), password)
                .withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName)
                .withLossDescription(lossDescription)
                .withLossCause(lossCause)
                .withLossRouter(lossRouter)
                .withAdress(address)
                .withPolicyNumber(policyNumber)
                .withtopLevelCoverage(coverageNeeded)
                .build(GenerateFNOLType.Auto);

        this.claimNumber = myFNOLObj.claimNumber;
        new GuidewireHelpers(driver).logout();
    }

    // Exposure
    @Test(dependsOnMethods = {"medPayFNOL"})
    public void createExposureMedPay() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        this.driver = new DriverBuilder().buildGWWebDriver(cf);
        GenerateExposure exposureObj = new GenerateExposure.Builder(this.driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withCoverageType(exposureType)
                .withIsICDtest(true)
                .build();

        System.out.println(exposureObj.getClaimNumber());
    }

}

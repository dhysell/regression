package guidewire.claimcenter.systemMaintenance;

import com.idfbins.driver.BaseTest;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.cc.claim.LossDetails;
import repository.cc.claim.Matters;
import repository.cc.claim.NewMatter;
import repository.cc.sidemenu.SideMenuCC;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimSearchLineOfBusiness;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.generate.cc.GenerateExposure;
import repository.gw.generate.cc.GenerateFNOL;
import repository.gw.login.Login;
public class US15009_MatterSetupLitigationStatus extends BaseTest {

    private ClaimsUsers user = ClaimsUsers.gmurray;
    private String password = "gw";

    private String claimNumber = null;

    // FNOL Specific Strings
    private String incidentName = "Random";
    private String lossDescription = "Comprehensive Exposure Test";
    private String lossCause = "Collision and Rollover";
    private String lossRouter = "Minor Incident";
    private String address = "Random";
    private String policyNumber = "01-067504-11";

    // Exposure Strings.
    private String exposureType = "Comprehensive";

    // FNOL
    @Test
    public void fnol() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter, "DEV");
        System.out.println(cf.getEnv());
        WebDriver driver = buildDriver(cf);
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
    }

    // Exposure
    @Test(dependsOnMethods = {"fnol"})
    public void exposure() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter, "DEV");
        WebDriver driver = buildDriver(cf);
        GenerateExposure exposureObj = new GenerateExposure.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withCoverageType(exposureType)
                .build();

        System.out.println(exposureObj.getClaimNumber());
    }

    // Reserves
    @Test(dependsOnMethods = {"exposure"})
    public void newMatter() throws Exception {

        ClaimsUsers user = ClaimsUsers.adegiulio;

        Config cf = new Config(ApplicationOrCenter.ClaimCenter, "DEV");
        WebDriver driver = buildDriver(cf);

        new Login(driver).login(user.toString(), password);

        TopMenu topMenu = new TopMenu(driver);
        topMenu.clickClaimTabArrow();
        topMenu.setClaimNumberSearch(claimNumber);

        SideMenuCC sm = new SideMenuCC(driver);

        Matters matter = sm.clickLitigationLink();

        NewMatter newMatter = matter.newRandomMatter();
        if (!newMatter.isErrorConfirmed()) {
            Assert.fail("Expected Error is not present.");
        }

        LossDetails ld = sm.clickLossDetailsLink();
        ld.clickEditButton();
        ld.setLitigationStatus("Litigated");
        ld.clickUpdateButton();

        topMenu.clickUnsavedWork();
        topMenu.clickUnsavedWorkText("New Matter");

        ld.clickUpdateButton();
        newMatter.clickUpToLitigation();

        if (!matter.findMatterInTable("Test Matter")) {
            Assert.fail("Matter was not found.");
        }

        System.out.println("test");
    }

}

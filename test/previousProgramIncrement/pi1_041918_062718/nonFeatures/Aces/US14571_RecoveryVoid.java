package previousProgramIncrement.pi1_041918_062718.nonFeatures.Aces;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.cc.claim.FinancialsTransactions;
import repository.cc.claim.Recovery;
import repository.cc.claim.RecoveryDetails;
import repository.cc.enums.CheckLineItemCategory;
import repository.cc.enums.CheckLineItemType;
import repository.cc.sidemenu.SideMenuCC;
import repository.cc.topmenu.TopMenu;
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
public class US14571_RecoveryVoid extends BaseTest {

    private ClaimsUsers user = ClaimsUsers.gmurray;
    private String password = "gw";

    private String claimNumber = null;

    // FNOL Specific Strings
    private String incidentName = "Random";
    private String lossDescription = "Comprehensive Exposure Test";
    private String lossCause = "Collision and Rollover";
    private String lossRouter = "Minor Incident";
    private String address = "Random";
    private String policyNumber = "Random";

    // Exposure Strings.
    private String exposureType = "Comprehensive";

    // Check Specific Strings
    private boolean deductibleToAdd = false;
    private CheckLineItemType paymentType = CheckLineItemType.INDEMNITY;
    private String paymentAmount = "1000";
    private CheckLineItemCategory categoryType = CheckLineItemCategory.INDEMNITY;
    private String deductibleAmount = "250";
    private String companyCheckBook = "Farm Bureau";
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
        driver = buildDriver(cf);
        GenerateExposure exposureObj = new GenerateExposure.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withCoverageType(exposureType)
                .build();

        System.out.println(exposureObj.getClaimNumber());
    }

    // Reserves
    @Test(dependsOnMethods = {"exposure"})
    public void reserves() throws Exception {

        ArrayList<ReserveLine> lines = new ArrayList<>();
        ReserveLine line1 = new ReserveLine(incidentName);
        lines.add(line1);

        Config cf = new Config(ApplicationOrCenter.ClaimCenter, "DEV");
        driver = buildDriver(cf);
        GenerateReserve myClaimObj = new GenerateReserve.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withReserveLines(lines)
                .build();

        System.out.println(myClaimObj.claimNumber);

    }

    // Recoveries
    @Test(dependsOnMethods = {"reserves"})
    public void recoveries() throws Exception {

        ClaimsUsers user = ClaimsUsers.adegiulio;

        Config cf = new Config(ApplicationOrCenter.ClaimCenter, "DEV");
        driver = buildDriver(cf);

        new Login(driver).login(user.toString(), password);

        TopMenu topMenu = new TopMenu(driver);
        topMenu.clickClaimTabArrow();
        topMenu.setClaimNumberSearch(claimNumber);

        Recovery recovery = new Recovery(driver);
        recovery.addRandomRecoveries();

        new GuidewireHelpers(driver).logout();

        System.out.println("TODO");
    }

    @Test(dependsOnMethods = {"recoveries"})
    public void voidRecoveries() throws Exception {
        ClaimsUsers user = ClaimsUsers.adegiulio;

        Config cf = new Config(ApplicationOrCenter.ClaimCenter, "DEV");
        driver = buildDriver(cf);

        new Login(driver).login(user.toString(), password);

        TopMenu topMenu = new TopMenu(driver);
        topMenu.clickClaimTabArrow();
        topMenu.setClaimNumberSearch(claimNumber);

        SideMenuCC sMenu = new SideMenuCC(driver);
        FinancialsTransactions financials = sMenu.clickFinancialTransactions();
        financials.selectSpecificTransactionType("Recoveries");
        RecoveryDetails recovery = financials.selectRecovery();
        recovery.voidRecovery();
        if (!recovery.isSuccessfullVoid()) {
            Assert.fail("Recovery was not voided successfully.");
        }

        System.out.println("test");
    }
}

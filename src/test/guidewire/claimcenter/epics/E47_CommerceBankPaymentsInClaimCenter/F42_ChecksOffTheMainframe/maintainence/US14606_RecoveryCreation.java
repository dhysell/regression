package guidewire.claimcenter.epics.E47_CommerceBankPaymentsInClaimCenter.F42_ChecksOffTheMainframe.maintainence;

import com.idfbins.driver.BaseTest;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.cc.claim.Recovery;
import repository.cc.enums.CheckLineItemCategory;
import repository.cc.enums.CheckLineItemType;
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

import java.util.ArrayList;
public class US14606_RecoveryCreation extends BaseTest {

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

    // FNOL
    @Test
    public void fnol() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter, "DEV");
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
    @SuppressWarnings("Duplicates")
    public void reserves() throws Exception {

        ArrayList<ReserveLine> lines = new ArrayList<>();
        ReserveLine line1 = new ReserveLine(incidentName);
        lines.add(line1);

        Config cf = new Config(ApplicationOrCenter.ClaimCenter, "DEV");
        WebDriver driver = buildDriver(cf);
        GenerateReserve myClaimObj = new GenerateReserve.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withReserveLines(lines)
                .build();

        System.out.println(myClaimObj.claimNumber);

    }

    // Recoveries
    @Test(invocationCount = 2, dependsOnMethods = {"reserves"})
    public void recoveries() throws Exception {

        ClaimsUsers user = ClaimsUsers.adegiulio;

        Config cf = new Config(ApplicationOrCenter.ClaimCenter, "DEV");
        WebDriver driver = buildDriver(cf);

        new Login(driver).login(user.toString(), password);

        TopMenu topMenu = new TopMenu(driver);
        topMenu.clickClaimTabArrow();
        topMenu.setClaimNumberSearch(claimNumber);

        Recovery recovery = new Recovery(driver);
        boolean isRecoveryLocated = recovery.addRandomRecoveries();

        if (!isRecoveryLocated) {
            Assert.fail("Failed to create Recovery");
        }

        new GuidewireHelpers(driver).logout();

        System.out.println("TODO");
    }
}

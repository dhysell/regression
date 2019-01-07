package guidewire.claimcenter.systemMaintenance;

import com.idfbins.driver.BaseTest;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimSearchLineOfBusiness;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.generate.cc.GenerateExposure;
import repository.gw.generate.cc.GenerateFNOL;
import repository.gw.generate.cc.GenerateReserve;
import repository.gw.generate.cc.ReserveLine;

import java.util.ArrayList;
public class US14605_ReserveTransactions extends BaseTest {

    private ClaimsUsers user = ClaimsUsers.gmurray;
    private String password = "gw";

    private String claimNumber = null;

    // FNOL Specific Strings
    private GenerateFNOLType claimType = GenerateFNOLType.Auto;
    private String incidentName = "1998 DODG DURAN 1B4GT44L8WB698997";
    private String lossDescription = "Comprehensive Exposure Test";
    private String lossCause = "Collision and Rollover";
    private String lossRouter = "Minor Incident";
    private String address = "Random";
    private String policyNumber = "01-091152-01";

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
                .build(claimType);

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
}

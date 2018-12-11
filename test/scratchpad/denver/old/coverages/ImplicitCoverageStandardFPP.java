package scratchpad.denver.old.coverages;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.generate.cc.GenerateExposure;
import repository.gw.generate.cc.GenerateFNOL;
import repository.gw.generate.cc.GenerateReserve;
import repository.gw.generate.cc.ReserveLine;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.ArrayList;
public class ImplicitCoverageStandardFPP extends BaseTest {
	private WebDriver driver;
    private ClaimsUsers user = ClaimsUsers.gmurray;
    private String password = "gw";

    private String claimNumber = null;

    // FNOL Specific Strings
    private String incidentName = "PC FPP";
    private String lossDescription = "FPP Implicit Coverages Test";
    private String lossCause = "Collapse";
    private String lossRouter = "Major Incident";
    private String address = "Random";
    private String policyNumber = "01-287078-01";

    @Test
    public void propertyFnolTest() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver).withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName).withLossDescription(lossDescription).withLossCause(lossCause)
                .withLossRouter(lossRouter).withAdress(address).withPolicyNumber(policyNumber)
                .build(GenerateFNOLType.Property);
        this.claimNumber = myFNOLObj.claimNumber;
    }

    // Exposures
    @Test(dependsOnMethods = {"propertyFnolTest"})
    public void exposureTestMiscellaneous() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateExposure exposureObj = new GenerateExposure.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withCoverageType("Miscellaneous")
                .build();

        ArrayList<ReserveLine> lines = new ArrayList<>();
        ReserveLine line1 = new ReserveLine(exposureObj.getIncidentType(), exposureObj.getExposureCoverage());
        lines.add(line1);

        new GenerateReserve.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withReserveLines(lines)
                .withCoverageType(exposureObj.getExposureCoverage())
                .build();
    }

    @Test(dependsOnMethods = {"propertyFnolTest"})
    public void exposureTestMachinery() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateExposure exposureObj = new GenerateExposure.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withCoverageType("Machinery")
                .build();

        ArrayList<ReserveLine> lines = new ArrayList<>();
        ReserveLine line1 = new ReserveLine(exposureObj.getIncidentType(), exposureObj.getExposureCoverage());
        lines.add(line1);

        new GenerateReserve.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withReserveLines(lines)
                .withCoverageType(exposureObj.getExposureCoverage())
                .build();
    }

    @Test(dependsOnMethods = {"propertyFnolTest"})
    public void exposureTestLivestock() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateExposure exposureObj = new GenerateExposure.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withCoverageType("Livestock")
                .build();

        ArrayList<ReserveLine> lines = new ArrayList<>();
        ReserveLine line1 = new ReserveLine(exposureObj.getIncidentType(), exposureObj.getExposureCoverage());
        lines.add(line1);

        new GenerateReserve.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withReserveLines(lines)
                .withCoverageType(exposureObj.getExposureCoverage())
                .build();
    }

    @Test(dependsOnMethods = {"propertyFnolTest"})
    public void exposureTestIrrigationEquipment() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateExposure exposureObj = new GenerateExposure.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withCoverageType("Irrigation Equipment")
                .build();

        ArrayList<ReserveLine> lines = new ArrayList<>();
        ReserveLine line1 = new ReserveLine(exposureObj.getIncidentType(), exposureObj.getExposureCoverage());
        lines.add(line1);

        new GenerateReserve.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withReserveLines(lines)
                .withCoverageType(exposureObj.getExposureCoverage())
                .build();
    }
}


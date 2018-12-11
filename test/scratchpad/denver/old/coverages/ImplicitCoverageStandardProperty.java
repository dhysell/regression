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
public class ImplicitCoverageStandardProperty extends BaseTest {
	private WebDriver driver;
    private ClaimsUsers user = ClaimsUsers.gmurray;
    private String password = "gw";

    private String claimNumber = null;

    // FNOL Specific Strings
    private String incidentName = "Residence Premises";
    private String lossDescription = "Implicit Coverages Test";
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
    public void exposureTestMoney() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateExposure exposureObj = new GenerateExposure.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withCoverageType("Money")
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
    public void exposureTestSecurities() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateExposure exposureObj = new GenerateExposure.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withCoverageType("Securities")
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
    public void exposureTestWatercraft() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateExposure exposureObj = new GenerateExposure.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withCoverageType("Watercraft")
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
    public void exposureTestTrailers() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateExposure exposureObj = new GenerateExposure.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withCoverageType("Trailers")
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
    public void exposureTestGuns() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateExposure exposureObj = new GenerateExposure.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withCoverageType("Guns")
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
    public void exposureTestSilverware() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateExposure exposureObj = new GenerateExposure.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withCoverageType("Silverware")
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
    public void exposureTestElectronicMediaOnPremise() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateExposure exposureObj = new GenerateExposure.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withCoverageType("Electronic Media On Premise")
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
    public void exposureTestElectronicMediaOffPremise() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateExposure exposureObj = new GenerateExposure.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withCoverageType("Electronic Media Off Premise")
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
    public void exposureTestElectronicEquipment() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateExposure exposureObj = new GenerateExposure.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withCoverageType("Electronic Equipment")
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
    public void exposureTestPersonalPropertyOffPremise() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateExposure exposureObj = new GenerateExposure.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withCoverageType("Personal Property Off Premise")
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
    public void exposureTestTools() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateExposure exposureObj = new GenerateExposure.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withCoverageType("Tools")
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
    public void exposureTestSaddlesAndTack() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateExposure exposureObj = new GenerateExposure.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withCoverageType("Saddles and Tack")
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
    public void exposureTestCreditCards() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateExposure exposureObj = new GenerateExposure.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withCoverageType("Credit Cards")
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

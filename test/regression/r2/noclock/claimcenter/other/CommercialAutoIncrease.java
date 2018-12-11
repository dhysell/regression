package regression.r2.noclock.claimcenter.other;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.testng.listener.QuarantineClass;

import repository.cc.claim.FinancialsTransactions;
import repository.cc.sidemenu.SideMenuCC;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.generate.cc.GenerateExposure;
import repository.gw.generate.cc.GenerateFNOL;
import repository.gw.generate.cc.GenerateReserve;
import repository.gw.generate.cc.ReserveLine;
import gwclockhelpers.ApplicationOrCenter;
@QuarantineClass
public class CommercialAutoIncrease extends BaseTest {
	private WebDriver driver;
    private ClaimsUsers user = ClaimsUsers.gmurray;
    private String password = "gw";

    private String claimNumber = null;

    // FNOL Specific Strings
    private String incidentName = "001 Dwelling @ 13113 Cameron Dr, Donnelly, ID 83615-5054";
    private String lossDescription = "Commercial Auto Increase test";
    private String lossCause = "Random";
    private String lossRouter = "Random";
    private String address = "Random";
    private String policyNumber = "08-137749-01";
    private String coverageNeeded = "Building Automatic Increase - Property Damage";


    // Exposure Menu Strings. 
    private String firstExposure = "Building - Property Damage";
    private String secondExposure = "Building Automatic Increase - Property Damage";

    // Reserve Lines for each exposure.
    ReserveLine line1 = new ReserveLine();
    ReserveLine line2 = new ReserveLine();

    @Test
    public void autoIncreaseFnol() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName)
                .withLossDescription(lossDescription)
                .withLossCause(lossCause)
                .withtopLevelCoverage(coverageNeeded)
                .withLossRouter(lossRouter)
                .withAdress(address)
                .withPolicyNumber(policyNumber)
                .build(GenerateFNOLType.Property);
        this.claimNumber = myFNOLObj.claimNumber;
        this.incidentName = myFNOLObj.specificIncident;

    }

    // Exposures
    @Test(dependsOnMethods = {"autoIncreaseFnol"})
    public void autoIncreaseExposures() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateExposure myClaimObj1 = new GenerateExposure.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withCoverageType(firstExposure)
                .build();

        line1.setExposureCoverage(myClaimObj1.getExposureCoverage());
        line1.setExposureNumber(myClaimObj1.getExposureNumber());
        line1.setIncidentName(myClaimObj1.getIncidentType());


        GenerateExposure myClaimObj2 = new GenerateExposure.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withCoverageType(secondExposure)
                .build();

        line2.setExposureCoverage(myClaimObj2.getExposureCoverage());
        line2.setExposureNumber(myClaimObj2.getExposureNumber());
        line2.setIncidentName(myClaimObj2.getIncidentType());
    }

    // Reserves

    /**
     * @throws Exception
     * @Author iclouser
     * @Requirement make sure that automatic increase coverage comes through and calculates the reserve amount correctly.
     * @RequirementsLink <a href="https://rally1.rallydev.com/#/10552165958d/detail/userstory/51536944720">Rally Link</a>
     * @Description
     * @DATE Jun 20, 2016
     */
    @Test(dependsOnMethods = {"autoIncreaseExposures"})
    public void autoIncreaseReserves() throws Exception {
        ArrayList<ReserveLine> lines = new ArrayList<>();

        lines.add(line1);
        lines.add(line2);
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateReserve myClaimObj = new GenerateReserve.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withReserveLines(lines)
                .build();

        System.out.println(myClaimObj.claimNumber);

        SideMenuCC sideMenu = new SideMenuCC(driver);

        String locationAddress = "13113 Cameron Dr, Donnelly, ID 83615-5054";
        String coveredRisk = "001";
        String coverageType = "Building Automatic Increase - Property Damage";
        long calcCostAmount = sideMenu.clickPolicyLink().gatherInfoAndCalculateIncreasedReplacementCostAmount(locationAddress, coveredRisk, coverageType);

        sideMenu.clickFinancialTransactions();

        FinancialsTransactions finTransacitons = new FinancialsTransactions(driver);
        long actualAmount = finTransacitons.getReserveAmountUsingCostCategoryName("Automatic Increase");


        Assert.assertTrue(calcCostAmount == actualAmount, "Amounts don't match");

    }
}

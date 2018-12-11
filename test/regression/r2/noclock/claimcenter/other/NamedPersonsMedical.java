package regression.r2.noclock.claimcenter.other;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.testng.listener.QuarantineClass;

import repository.cc.claim.NewClaimSaved;
import repository.cc.claim.policypages.PolicyGeneral;
import repository.cc.sidemenu.SideMenuCC;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.generate.cc.GenerateFNOL;
import gwclockhelpers.ApplicationOrCenter;
@QuarantineClass
public class NamedPersonsMedical extends BaseTest {
	private WebDriver driver;
    private ClaimsUsers user = ClaimsUsers.bhogan;
    private String password = "gw";

    private String claimNumber = null;

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    // FNOL Specific Strings
    private String incidentName = "Random";
    private String lossDescription = "Loss Description Test";
    private String lossCause = "Random";
    private String lossRouter = "Random";
    private String address = "Random";
    private String policyNumber = "01-029162-01";

    /**
     * @throws Exception
     * @Author iclouser
     * @Requirement Verify new deductible and limits match the new requirements.
     * @RequirementsLink <a href="https://rally1.rallydev.com/#/10552165958d/detail/userstory/57140719713">Rally Story</a>
     * @DATE Aug 26, 2016
     */
    @Test
    public void namedPersonsMed() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver).withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName).withLossDescription(lossDescription).withLossCause(lossCause)
                .withLossRouter(lossRouter).withAdress(address).withPolicyNumber(policyNumber)
                .build(GenerateFNOLType.GeneralLiability);
        this.setClaimNumber(myFNOLObj.claimNumber);

        NewClaimSaved claimsaved = new NewClaimSaved(driver);
        claimsaved.clickGoToClaimLink();
        SideMenuCC sideMenu = new SideMenuCC(driver);
        PolicyGeneral policyGen = sideMenu.clickPolicyLink();

        String locationAddress = "3432 W 2900 N, MOORE, ID 83255";
        String coveredRisk = "Location 01";
        String coverageType = "Named Person Medical";

        List<String> amounts = policyGen.gatherCoverageLimitAmounts(locationAddress, coveredRisk, coverageType);

        Boolean deductibleAmntCorrect = amounts.get(0).equals("100.0");
        Boolean exposureAmntCorrect = amounts.get(1).equals("25000.0");
        Boolean incidentAmntCorrect = amounts.get(2).equals("125000.0");


        Assert.assertTrue(deductibleAmntCorrect, "The deductible amount that was expected was 500.00 but got: " + amounts.get(0));
        Assert.assertTrue(exposureAmntCorrect, "The exposure limit amount that was expected was 10000.00 but got: " + amounts.get(1));
        Assert.assertTrue(incidentAmntCorrect, "The incident limit amount that was expected was 50000.00 but got: " + amounts.get(2));

    }
}

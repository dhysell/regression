package previousProgramIncrement.pi2_062818_090518.nonFeatures.Aces;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.generate.cc.GenerateFNOL;
import gwclockhelpers.ApplicationOrCenter;

public class MembershipFNOL extends BaseTest {

    private ClaimsUsers user = ClaimsUsers.gmurray;
    private String password = "gw";
    private WebDriver driver;
    private String claimNumber = null;
    private String incidentName = "Random";
    private String lossDescription = "Membership FNOL Test";
    private String lossCause = "Random";
    private String lossRouter = "Random";
    private String address = "Random";
    private String policyNumber = "01-278545-01";

    @Test
    public void membershipFNOL() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter, "DEV3");
        driver = buildDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver).withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName).withLossDescription(lossDescription).withLossCause(lossCause)
                .withLossRouter(lossRouter).withAdress(address).withPolicyNumber(policyNumber)
                .build(GenerateFNOLType.Membership);
        this.claimNumber = myFNOLObj.claimNumber;
    }

}

package guidewire.claimcenter.systemMaintenance;

import com.idfbins.driver.BaseTest;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.cc.enums.Catastrophe;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.generate.cc.GenerateFNOL;

import java.time.LocalDate;

public class US15366_CatastropheManagerClaimAssignment extends BaseTest {

    private ClaimsUsers user = ClaimsUsers.gmurray;
    private String password = "gw";
    private WebDriver driver;

    private String claimNumber = null;
    public String getClaimNumber() {
        return claimNumber;
    }
    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    // FNOL Specific Strings
    private LocalDate lossDate = LocalDate.of(2018,4,7);
    private Catastrophe catastrophe = Catastrophe.WindOrHailApril2018;
    private String incidentName = "Random";
    private String lossDescription = "Loss Description Test";
    private String lossCause = "Falling or Moving Object";
    private String lossRouter = "Random";
    private String address = "Random";
    private String policyNumber = "01-173682-04";
    private String env = "dev";

    @Test
    public void autoFNOL() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter, env);
        driver = buildDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver).withCreatorUserNamePassword(user.toString(), password)
                .withDateOfLoss(lossDate).withCatastrophe(catastrophe)
                .withSpecificIncident(incidentName).withLossDescription(lossDescription).withLossCause(lossCause)
                .withLossRouter(lossRouter).withAdress(address).withPolicyNumber(policyNumber)
                .build(GenerateFNOLType.Auto);
        this.setClaimNumber(myFNOLObj.claimNumber);

        if (!myFNOLObj.assignedUser.equalsIgnoreCase("CAT Adjuster")) {
            Assert.fail("Claims with a Catastrophe do not seem to be routing to the correct user.");
        }

    }

}

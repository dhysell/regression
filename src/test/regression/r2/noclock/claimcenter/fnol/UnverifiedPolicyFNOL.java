package regression.r2.noclock.claimcenter.fnol;

import java.time.LocalDate;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.cc.entities.InvolvedParty;
import repository.cc.enums.PolicyType;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.generate.cc.GenerateFNOL;
import gwclockhelpers.ApplicationOrCenter;
public class UnverifiedPolicyFNOL extends BaseTest {
    // User Data
    private ClaimsUsers user = ClaimsUsers.abatts;
    private String password = "gw";
    private WebDriver driver;

    // FNOL Specific Strings
    private String incidentName = "Random";
    private String lossDescription = "Loss Description Test";
    private String lossCause = "Random";
    private String lossRouter = "Random";
    private String address = "Random";
    private String policyNumber = "01-168144-01";
    private LocalDate dateOfLoss = LocalDate.now();

    //FNOL object
    private GenerateFNOL myFNOLObj;

    @Test()
    public void unverifiedPolicyFnol() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter, "dev");
        driver = buildDriver(cf);
        this.myFNOLObj = new GenerateFNOL.Builder(driver).withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName).withLossDescription(lossDescription).withLossCause(lossCause)
                .withLossRouter(lossRouter).withAdress(address).withPolicyNumber(policyNumber)
                .withPolicyType(PolicyType.CITY)
                .withDateOfLoss(dateOfLoss)
                .withVerifiedPolicy(false)
                .build(GenerateFNOLType.Auto);

        InvolvedParty test = myFNOLObj.getPartiesInvolved().get(0);
        System.out.println(test.getFirstName() + " " + test.getLastName());
        driver.quit();
    }

    public GenerateFNOL getFNOL() {
        return this.myFNOLObj;
    }
}

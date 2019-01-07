package guidewire.claimcenter.systemMaintenance;

import com.idfbins.driver.BaseTest;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.cc.entities.InvolvedParty;
import repository.cc.enums.PolicyType;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.generate.cc.GenerateFNOL;

import java.time.LocalDate;

public class DE7775_ClaimAssignmentToClaimsVP extends BaseTest {
    // User Data
    private ClaimsUsers user = ClaimsUsers.ktennant;
    private String password = "gw";
    private WebDriver driver = null;

    // FNOL Specific Strings
    private String incidentName = "Random";
    private String lossDescription = "Loss Description Test";
    private String lossCause = "Random";
    private String lossRouter = "Random";
    private String address = "Random";
    private String policyNumber = "01-546875-01";
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

        String assignedUser = this.myFNOLObj.assignedUser;

        if (!assignedUser.equalsIgnoreCase("Vicki Kinter") && !assignedUser.equalsIgnoreCase("Kristy N Lindauer")) {
            Assert.fail("The claim has been assigned to the wrong user: " + assignedUser);
        }
    }

    public GenerateFNOL getFNOL() {
        return this.myFNOLObj;
    }
}



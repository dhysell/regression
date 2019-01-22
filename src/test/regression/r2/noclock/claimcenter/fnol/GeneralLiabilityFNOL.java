package regression.r2.noclock.claimcenter.fnol;

import com.idfbins.driver.BaseTest;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.generate.cc.GenerateFNOL;
public class GeneralLiabilityFNOL extends BaseTest {
	private WebDriver driver;
    private ClaimsUsers user = ClaimsUsers.bhogan;
    private String password = "gw";

    @SuppressWarnings("unused")
    private String claimNumber = null;

    // FNOL Specific Strings
    private String incidentName = "Random";
    private String lossDescription = "Loss Description Test";
    private String lossCause = "General Liability (including medical)";
    private String lossRouter = "Liability Issue";
    private String address = "Random";
    private String policyNumber = "01-006752-01";


    @Test
    public void genLiabFNOL() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver).withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName).withLossDescription(lossDescription).withLossCause(lossCause)
                .withLossRouter(lossRouter).withAdress(address).withPolicyNumber(policyNumber)
                .build(GenerateFNOLType.GeneralLiability);
        this.claimNumber = myFNOLObj.claimNumber;
    }
}

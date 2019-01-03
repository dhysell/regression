package regression.r2.noclock.claimcenter.fnol;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.generate.cc.GenerateFNOL;
import gwclockhelpers.ApplicationOrCenter;
public class AutoERSorGlassFNOL extends BaseTest {
	private WebDriver driver;
    private ClaimsUsers user = ClaimsUsers.gmurray;
    private String password = "gw";

    @SuppressWarnings("unused")
    private String claimNumber = null;

    // FNOL Specific Strings
    private String incidentName = "Random";
    private String lossDescription = "Loss Description Test";
    private String lossCause = "Random";
    private String lossRouter = "Random";
    private String address = "Random";
    private String policyNumber = "Random";
    private String coverageType = "01-056449-01";

//    @Override
//    @AfterMethod
//    public void tearDownDriver() {
//	    driver.close();
//	    driver.quit();
//    }
    
    @Test
    public void autoERSorGlassFNOL() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName)
                .withLossDescription(lossDescription)
                .withLossCause(lossCause)
                .withLossRouter(lossRouter)
                .withAdress(address)
                .withtopLevelCoverage(coverageType)
                .withPolicyNumber(policyNumber)
                .build(GenerateFNOLType.AutoGlass);
        this.claimNumber = myFNOLObj.claimNumber;
    }
}

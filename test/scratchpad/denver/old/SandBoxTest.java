package scratchpad.denver.old;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.driverConfiguration.DriverBuilder;
import repository.gw.enums.ClaimsUsers;
import repository.gw.generate.cc.GenerateReserve;
import repository.gw.generate.cc.ReserveLine;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
public class SandBoxTest extends BaseTest {

    private ClaimsUsers user = ClaimsUsers.gmurray;
    private String password = "gw";

    private String claimNumber = "01138794032018041011";

    // FNOL Specific Strings
    private String incidentName = "Random";


    private WebDriver driver;

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        this.driver.quit();
    }

    // Reserves
    @Test()
    public void createReserveComprehensive() throws Exception {

        ArrayList<ReserveLine> lines = new ArrayList<>();
        ReserveLine line1 = new ReserveLine(incidentName);
        lines.add(line1);

        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        this.driver = new DriverBuilder().buildGWWebDriver(cf);
        GenerateReserve myClaimObj = new GenerateReserve.Builder(this.driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withReserveLines(lines)
                .build();

        System.out.println(myClaimObj.claimNumber);

    }
}

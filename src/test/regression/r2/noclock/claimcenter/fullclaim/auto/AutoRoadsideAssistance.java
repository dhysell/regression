package regression.r2.noclock.claimcenter.fullclaim.auto;

import com.idfbins.driver.BaseTest;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import repository.cc.enums.CheckLineItemCategory;
import repository.cc.enums.CheckLineItemType;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateCheckType;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.generate.cc.*;

import java.util.ArrayList;
public class AutoRoadsideAssistance extends BaseTest {
	private WebDriver driver;
    private ClaimsUsers user = ClaimsUsers.abatts;
    private String password = "gw";

    private String claimNumber = null;

    // FNOL Specific Strings
    private String incidentName = "Random";
    private String lossDescription = "Roadside Assistance Test";
    private String lossCause = "Collision and Rollover";
    private String lossRouter = "Minor Incident";
    private String address = "Random";
    private String policyNumber = "01-133213-02";
    private String coverageNeeded = "Roadside Assistance";

    // Exposure Strings. 
    private String exposureType = "Roadside Assistance";

    // Check Specific Strings
    private boolean deductibleToAdd = false;
    private CheckLineItemType paymentType = CheckLineItemType.INDEMNITY;
    private String paymentAmount = "1000";
    private CheckLineItemCategory categoryType = CheckLineItemCategory.INDEMNITY;
    private String deductibleAmount = "250";
    private String companyCheckBook = "Farm Bureau";

    // FNOL
    @Test
    public void autoRoadsideAssistanceFNOL() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName)
                .withLossDescription(lossDescription)
                .withLossCause(lossCause)
                .withLossRouter(lossRouter)
                .withAdress(address)
                .withPolicyNumber(policyNumber)
                .withtopLevelCoverage(coverageNeeded)
                .build(GenerateFNOLType.Auto);

        this.claimNumber = myFNOLObj.claimNumber;
    }

    // Exposure
    @Test(dependsOnMethods = {"autoRoadsideAssistanceFNOL"})
    public void createExposureRoadsideAssistance() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateExposure exposureObj = new GenerateExposure.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withCoverageType(exposureType)
                .build();

        System.out.println(exposureObj.getClaimNumber());
    }

    // Reserves
    @Test(dependsOnMethods = {"createExposureRoadsideAssistance"})
    public void createReserveRoadsideAssistance() throws Exception {

        ArrayList<ReserveLine> lines = new ArrayList<ReserveLine>();
        ReserveLine line1 = new ReserveLine();
        lines.add(line1);
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateReserve myClaimObj = new GenerateReserve.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withReserveLines(lines)
                .build();

        System.out.println(myClaimObj.claimNumber);
    }

    // Checks
    @Test(dependsOnMethods = {"createReserveRoadsideAssistance"})
    public void createCheckRoadsideAssistance() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateCheck myCheck = new GenerateCheck.Builder(driver)
                .withCreatorUserNamePassword(user, this.password)
                .withClaimNumber(this.claimNumber)
                .withPaymentType(paymentType)
                .withCategoryType(categoryType)
                .withPaymentAmount("5.00")
                .build(GenerateCheckType.Regular);

        System.out.println(myCheck.claimNumber);

    }
}

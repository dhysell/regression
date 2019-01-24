
package regression.r2.noclock.claimcenter.fullclaim.auto;

import com.idfbins.driver.BaseTest;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import repository.cc.enums.CheckLineItemCategory;
import repository.cc.enums.CheckLineItemType;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimSearchLineOfBusiness;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateCheckType;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.generate.cc.*;

import java.util.ArrayList;
public class AutoComprehensive extends BaseTest {
	private WebDriver driver;
    private ClaimsUsers user = ClaimsUsers.gmurray;
    private String password = "gw";

    private String claimNumber = null;

    // FNOL Specific Strings
    private String incidentName = "Random";
    private String lossDescription = "Comprehensive Exposure Test";
    private String lossCause = "Collision and Rollover";
    private String lossRouter = "Minor Incident";
    private String address = "Random";
    private String policyNumber = "01-194697-01";

    // Exposure Strings. 
    private String exposureType = "Comprehensive";

    // Check Specific Strings
    private boolean deductibleToAdd = false;
    private CheckLineItemType paymentType = CheckLineItemType.INDEMNITY;
    private String paymentAmount = "500";
    private CheckLineItemCategory categoryType = CheckLineItemCategory.INDEMNITY;
    private String deductibleAmount = "250";
    private String companyCheckBook = "Farm Bureau";

    // FNOL
    @Test
    public void comprehensiveFNOL() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withLOB(ClaimSearchLineOfBusiness.City_Squire_Policy_Auto)
                .withSpecificIncident(incidentName)
                .withLossDescription(lossDescription)
                .withLossCause(lossCause)
                .withLossRouter(lossRouter)
                .withtopLevelCoverage(exposureType)
                .withAdress(address)
                .withPolicyNumber(policyNumber)
                .build(GenerateFNOLType.Auto);

        this.claimNumber = myFNOLObj.claimNumber;
        this.incidentName = myFNOLObj.specificIncident;
    }

    // Exposure
    @Test(dependsOnMethods = {"comprehensiveFNOL"})
    public void createExposureComprehensive() throws Exception {
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
    @Test(dependsOnMethods = {"createExposureComprehensive"})
    public void createReserveComprehensive() throws Exception {

        ArrayList<ReserveLine> lines = new ArrayList<>();
        ReserveLine line1 = new ReserveLine(incidentName);
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
    @Test(dependsOnMethods = {"createReserveComprehensive"})
    public void createCheckComprehensive() throws Exception {
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

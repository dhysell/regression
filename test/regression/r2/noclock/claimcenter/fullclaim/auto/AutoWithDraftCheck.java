package regression.r2.noclock.claimcenter.fullclaim.auto;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.cc.enums.CheckLineItemCategory;
import repository.cc.enums.CheckLineItemType;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateCheckType;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.generate.cc.GenerateCheck;
import repository.gw.generate.cc.GenerateExposure;
import repository.gw.generate.cc.GenerateFNOL;
import repository.gw.generate.cc.GenerateReserve;
import repository.gw.generate.cc.ReserveLine;
import repository.gw.helpers.NumberUtils;
import gwclockhelpers.ApplicationOrCenter;
public class AutoWithDraftCheck extends BaseTest {
	private WebDriver driver;
    private ClaimsUsers user = ClaimsUsers.gmurray;
    private String password = "gw";

    private String claimNumber = null;

    // FNOL Specific Strings
    private String incidentName = "Random";
    private String lossDescription = "Loss Description Test";
    private String lossCause = "Collision and Rollover";
    private String lossRouter = "Major Incident";
    private String address = "Random";
    private String policyNumber = "01-090396-01";

    // Exposure Specific Strings
    private String exposureType = "Collision - Vehicle Damage";

    // Check Specific Strings
    private boolean deductibleToAdd = false;
    private CheckLineItemType paymentType = CheckLineItemType.INDEMNITY;
    private String paymentAmount = Integer.toString(NumberUtils.generateRandomNumberInt(600, 2000));
    private CheckLineItemCategory categoryType = CheckLineItemCategory.INDEMNITY;
    private String deductibleAmount = "250";
    private String companyCheckBook = "Farm Bureau";

    @Test
    public void fnolTest() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName)
                .withLossDescription(lossDescription)
                .withtopLevelCoverage(lossCause)
                .withLossCause(lossCause)
                .withLossRouter(lossRouter)
                .withAdress(address)
                .withPolicyNumber(policyNumber)
                .build(GenerateFNOLType.Auto);
        this.claimNumber = myFNOLObj.claimNumber;

    }

    // Exposures
    @Test(dependsOnMethods = {"fnolTest"})
    public void exposuresTest() throws Exception {
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
    @Test(dependsOnMethods = {"exposuresTest"})
    public void reservesTest() throws Exception {

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
    @Test(dependsOnMethods = {"reservesTest"})
    public void checksTest() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateCheck myCheck = new GenerateCheck.Builder(driver).withCreatorUserNamePassword(user, this.password)
                .withClaimNumber(this.claimNumber).withDeductible(deductibleToAdd)
                .withDeductibleAmount(deductibleAmount).withPaymentType(paymentType).withCategoryType(categoryType)
                .withPaymentAmount(paymentAmount).withCompanyCheckBook(companyCheckBook).build(GenerateCheckType.Field);

        System.out.println(myCheck.claimNumber);
        System.out.println(myCheck.getCheckNumber());
        System.out.println(myCheck.getPaymentAmount());
        System.out.println(myCheck.getDeductibleAmount());
    }

}

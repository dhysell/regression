package regression.r2.noclock.claimcenter.fullclaim.genliab;

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
public class GeneralLiabilityFullClaim extends BaseTest {
	private WebDriver driver;
    private ClaimsUsers user = ClaimsUsers.gmurray;
    private String password = "gw";

    private String claimNumber = null;

    // FNOL Specific Strings 
    private String incidentName = "Random";
    private String lossDescription = "Loss Description Test";
    private String lossCause = "General Liability (including medical)";
    private String address = "Random";
    private String policyNumber = "01-006752-01";

    // Check Specific Strings
    private boolean deductibleToAdd = false;
    private CheckLineItemType paymentType = CheckLineItemType.INDEMNITY;
    private String paymentAmount = "5000";
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
                .withLossCause("General Liability (including medical)")
                .withAdress(address)
                .withLossCause(lossCause)
                .withLossRouter("Liability Issue")
                .withPolicyNumber(policyNumber)
                .build(GenerateFNOLType.GeneralLiability);
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
                .withCoverageType("Random")
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
        GenerateCheck myCheck = new GenerateCheck.Builder(driver)
                .withCreatorUserNamePassword(user, this.password)
                .withClaimNumber(this.claimNumber)
                .withDeductible(deductibleToAdd)
                .withDeductibleAmount(deductibleAmount)
                .withPaymentType(paymentType)
                .withCategoryType(categoryType)
                .withPaymentAmount(paymentAmount)
                .withCompanyCheckBook(companyCheckBook)
                .build(GenerateCheckType.Regular);

        System.out.println(myCheck.claimNumber);
    }
}

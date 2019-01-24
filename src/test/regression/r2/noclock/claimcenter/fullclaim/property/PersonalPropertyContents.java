package regression.r2.noclock.claimcenter.fullclaim.property;

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
public class PersonalPropertyContents extends BaseTest {
	private WebDriver driver;
    private ClaimsUsers user = ClaimsUsers.gmurray;
    private String password = "gw";

    private String claimNumber = null;

    // FNOL Specific Strings
    private String incidentName = "Random";
    private String lossDescription = "Loss Description Test";
    private String lossCause = "Random";
    private String lossRouter = "Random";
    private String address = "Random";
    private String policyNumber = "01-232186-01";
    private String coverageNeeded = "Personal Property Contents";

    // Exposure Strings. 
    private String exposureType = "Personal Property Contents - Personal Property Damage";

    // Check Specific Strings
    private boolean deductibleToAdd = false;
    private CheckLineItemType paymentType = CheckLineItemType.INDEMNITY;
    private String paymentAmount = "1000";
    private CheckLineItemCategory categoryType = CheckLineItemCategory.INDEMNITY;
    private String deductibleAmount = "250";
    private String companyCheckBook = "Farm Bureau";

    @Test
    public void personalPCPDFnol() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName)
                .withLossDescription(lossDescription)
                .withLossCause(lossCause)
                .withLOB(ClaimSearchLineOfBusiness.City_Squire_Policy_Property)
                .withLossRouter(lossRouter)
                .withAdress(address)
                .withtopLevelCoverage(coverageNeeded)
                .withPolicyNumber(policyNumber)
                .build(GenerateFNOLType.Property);
        this.claimNumber = myFNOLObj.claimNumber;


    }

    // Exposures
    @Test(dependsOnMethods = {"personalPCPDFnol"})
    public void personalPCExposure() throws Exception {
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
    @Test(dependsOnMethods = {"personalPCExposure"})
    public void personalPCReserves() throws Exception {
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
    @Test(dependsOnMethods = {"personalPCReserves"})
    public void personalPCChecks() throws Exception {
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

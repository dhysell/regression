package previousProgramIncrement.pi2_062818_090518.nonFeatures.Aces;

import repository.cc.claim.LossDetails;
import repository.cc.claim.policypages.PolicyGeneral;
import repository.cc.claim.policypages.PolicyLocations;
import repository.cc.enums.CheckLineItemCategory;
import repository.cc.enums.CheckLineItemType;
import repository.cc.enums.Coverages;
import repository.cc.sidemenu.SideMenuCC;
import repository.cc.topmenu.TopMenu;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateCheckType;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.generate.cc.GenerateCheck;
import repository.gw.generate.cc.GenerateExposure;
import repository.gw.generate.cc.GenerateFNOL;
import repository.gw.generate.cc.GenerateReserve;
import repository.gw.generate.cc.ReserveLine;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.ArrayList;

public class US15895_EPLI_CostCategories extends BaseTest {

    private ClaimsUsers user = ClaimsUsers.abatts;
    private String password = "gw";

    private String claimNumber = null;

    // FNOL Specific Strings
    private String incidentName = "New";
    private String lossDescription = "ELPI Test";
    private String lossCause = "General Liability (including medical)";
    private String lossRouter = "Liability Issue";
    private String address = "Random";
    private String policyNumber = "08-037393-06";

    // Exposure Strings.
    private String exposureType = "Employment Practices Liability Insurance";

    // Check Specific Strings
    private boolean deductibleToAdd = false;
    private CheckLineItemType paymentType = CheckLineItemType.INDEMNITY;
    private String paymentAmount = "1200";
    private CheckLineItemCategory categoryType = CheckLineItemCategory.INDEMNITY;
    private String deductibleAmount = "250";
    private String companyCheckBook = "Farm Bureau";
    private String env = "DEV";

    @Test
    public void genLiabFNOL() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter, env);
        WebDriver driver = buildDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver).withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName).withLossDescription(lossDescription).withLossCause(lossCause)
                .withLossRouter(lossRouter).withAdress(address).withPolicyNumber(policyNumber)
                .build(GenerateFNOLType.GeneralLiability);
        this.claimNumber = myFNOLObj.claimNumber;
        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"genLiabFNOL"})
    public void EPLI() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter, env);
        WebDriver driver = buildDriver(cf);

        TopMenu topMenu = new TopMenu(driver);
        new Login(driver).login(user.toString(), password);

       topMenu.clickClaimTabArrow();
       topMenu.setClaimNumberSearch(this.claimNumber);

        SideMenuCC sideMenu = new SideMenuCC(driver);
        sideMenu.clickPolicyLink();
        try {
            sideMenu.clickPolicyLocations().clickAddDeleteLocationsButton();
            sideMenu.clickOK();
        } catch (Exception e) {
            System.out.println("Coverage in Question.");
        }

        PolicyGeneral policy = sideMenu.clickPolicyLink();
        policy.clickEditButton();
        policy.clickAddPolicyLevelCoverage();
        policy.buildPolicyLevelCoverage(Coverages.EPLI);
        PolicyLocations policyUpdate = sideMenu.clickPolicyLocations();
        try {
            policyUpdate.clickUpdate();
        } catch (Exception e) {
            System.out.println("Update not required.");
        }
        sideMenu.clickPolicyLink();

        policy.clickEditButton();
        policy.clickVerifiedPolicyTrue();
        policy.clickUpdate();

        LossDetails lossDetails = sideMenu.clickLossDetailsLink();
        lossDetails.clickEditButton();
        lossDetails.clickCoverageInQuestionFalse();
        lossDetails.clickUpdateButton();

        this.claimNumber = topMenu.gatherClaimNumber();

        new GuidewireHelpers(driver).logout();
    }

    // Exposure
    @Test(dependsOnMethods = {"EPLI"})
    public void createExposure() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter, env);
        WebDriver driver = buildDriver(cf);
        GenerateExposure exposureObj = new GenerateExposure.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withCoverageType(exposureType)
                .build();

        System.out.println(exposureObj.getClaimNumber());

    }

    // Reserves
    @Test(dependsOnMethods = {"createExposure"})
    public void createReserve() throws Exception {

        ArrayList<ReserveLine> lines = new ArrayList<ReserveLine>();
        ReserveLine line1 = new ReserveLine();
        lines.add(line1);
        Config cf = new Config(ApplicationOrCenter.ClaimCenter, env);
        WebDriver driver = buildDriver(cf);
        GenerateReserve myClaimObj = new GenerateReserve.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withReserveLines(lines)
                .build();

        System.out.println(myClaimObj.claimNumber);
        new GuidewireHelpers(driver).logout();
    }

    // Checks
    @Test(dependsOnMethods = {"createReserve"})
    public void createCheck() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter, env);
        WebDriver driver = buildDriver(cf);
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
        new GuidewireHelpers(driver).logout();
    }
}

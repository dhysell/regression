package regression.r2.noclock.claimcenter.fullclaim.property;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.cc.enums.CheckLineItemCategory;
import repository.cc.enums.CheckLineItemType;
import repository.cc.sidemenu.SideMenuCC;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimSearchLineOfBusiness;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateCheckType;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.generate.cc.GenerateCheck;
import repository.gw.generate.cc.GenerateExposure;
import repository.gw.generate.cc.GenerateFNOL;
import repository.gw.generate.cc.GenerateReserve;
import repository.gw.generate.cc.ReserveLine;
import gwclockhelpers.ApplicationOrCenter;
public class DwellingPropertyDamage extends BaseTest {
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
    private String policyNumber = "01-192789-01";
    private String coverageNeeded = "Dwelling";

    // Exposure Strings. 
    private String exposureType = "Dwelling - Property Damage";

    // Check Specific Strings
    private boolean deductibleToAdd = false;
    private CheckLineItemType paymentType = CheckLineItemType.INDEMNITY;
    private String paymentAmount = "1000";
    private CheckLineItemCategory categoryType = CheckLineItemCategory.INDEMNITY;
    private String deductibleAmount = "250";
    private String companyCheckBook = "Farm Bureau";

    @Test
    public void dwellingPDFnol() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName)
                .withLossDescription(lossDescription)
                .withLossCause(lossCause)
                .withLossRouter(lossRouter)
                .withLOB(ClaimSearchLineOfBusiness.City_Squire_Policy_Property)
                .withAdress(address)
                .withtopLevelCoverage(coverageNeeded)
                .withPolicyNumber(policyNumber)
                .build(GenerateFNOLType.Property);
        this.claimNumber = myFNOLObj.claimNumber;


    }

    // Exposures
    @Test(dependsOnMethods = {"dwellingPDFnol"})
    public void dwellingExposure() throws Exception {
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
    @Test(dependsOnMethods = {"dwellingExposure"})
    public void dwellingReserves() throws Exception {
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
    @Test(dependsOnMethods = {"dwellingReserves"})
    public void dwellingChecks() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateCheck myCheck = new GenerateCheck.Builder(driver).withCreatorUserNamePassword(user, this.password)
                .withClaimNumber(this.claimNumber).withDeductible(deductibleToAdd)
                .withDeductibleAmount(deductibleAmount).withPaymentType(paymentType).withCategoryType(categoryType)
                .withPaymentAmount(paymentAmount).withCompanyCheckBook(companyCheckBook)
                .build(GenerateCheckType.Regular);

        System.out.println(myCheck.claimNumber);

        SideMenuCC sm = new SideMenuCC(driver);
        Assert.assertTrue(sm.clickFinancialsChecks().pickFirstCheckVerifyNoErrorMessages(), "Something went terribly wrong");


    }


}

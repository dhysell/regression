package regression.r2.noclock.claimcenter.other;

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
import gwclockhelpers.ApplicationOrCenter;
public class DraftCheckCreation extends BaseTest {
	private WebDriver driver;
    private GenerateFNOL fnolObject;
    private ClaimsUsers user = ClaimsUsers.bhogan;
    private String password = "gw";

    private String claimNumber = null;

    // FNOL Specific Strings
    private String incidentName = "Random";
    private String lossDescription = "Loss Description Test";
    private String lossCause = "Random";
    private String lossRouter = "Random";
    private String address = "Random";
    private String policyNumber = "Random";

    // Exposure Specific Strings
    private String exposureType = "Random";

    // Check Specific Strings
    private boolean deductibleToAdd = false;
    private CheckLineItemType paymentType = CheckLineItemType.INDEMNITY;
    private String paymentAmount = "600";
    private CheckLineItemCategory categoryType = CheckLineItemCategory.INDEMNITY;
    // private String deductibleAmount = "150";
    private String companyCheckBook = "Farm Bureau";
    private String checkNumber;

/*    @Test
    public void gatherManualCheckBookInformation() {

        IVacationStatus vacationStatus = new VacationStatusCC(this.driver);
        Configuration.setProduct(ApplicationOrCenter.ClaimCenter);
        login(user.toString(), password);
        vacationStatus.setVacationStatusAtWork();


    }*/

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
        fnolObject = myFNOLObj;

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
        line1.setReserveAmount("3000");
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
                .withClaimNumber(this.claimNumber).withPaymentAmount("300")
                .withPaymentType(paymentType).withCategoryType(categoryType)
                .withCompanyCheckBook(companyCheckBook).build(GenerateCheckType.Draft);

        checkNumber = myCheck.getCheckNumber();
        System.out.println(myCheck.claimNumber);


    }

    @Test(dependsOnMethods = {"reservesTest"})
    public void checksTest2() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateCheck myCheck = new GenerateCheck.Builder(driver).withCreatorUserNamePassword(user, this.password)
                .withClaimNumber(this.claimNumber).withPaymentAmount("350")
                .withPaymentType(paymentType).withCategoryType(categoryType)
                .withCheckNumber(checkNumber + 1)
                .withCompanyCheckBook(companyCheckBook).build(GenerateCheckType.Draft);

        checkNumber = myCheck.getCheckNumber();
        System.out.println(myCheck.claimNumber);


    }

    @Test(dependsOnMethods = {"reservesTest"})
    public void checksTest3() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateCheck myCheck = new GenerateCheck.Builder(driver).withCreatorUserNamePassword(user, this.password)
                .withClaimNumber(this.claimNumber).withPaymentAmount("400")
                .withPaymentType(paymentType).withCategoryType(categoryType)
                .withCheckNumber(checkNumber + 1)
                .withCompanyCheckBook(companyCheckBook).build(GenerateCheckType.Draft);

        checkNumber = myCheck.getCheckNumber();
        System.out.println(myCheck.claimNumber);


    }
}

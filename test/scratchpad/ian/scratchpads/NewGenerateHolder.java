package scratchpad.ian.scratchpads;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.cc.enums.CheckLineItemCategory;
import repository.cc.enums.CheckLineItemType;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateCheckType;
import repository.gw.generate.cc.GenerateCheck;
import gwclockhelpers.ApplicationOrCenter;

@SuppressWarnings("unused")
public class NewGenerateHolder extends BaseTest {

    private ClaimsUsers user = ClaimsUsers.abatts;
    private String password = "gw";
    private String claimNumber = "01179872012018041012";

    // FNOL Specific Strings 
    private String incidentName = "Random";
    private String lossDescription = "This is a test Description";
    private String lossCause = "Random";
    private String lossRouter = "Random";
    private String address = "Random";
    private String policyNumber = "Random";


    // Check Specific Strings
    private boolean deductibleToAdd = false;
    private CheckLineItemType paymentType = CheckLineItemType.INDEMNITY;
    private String paymentAmount = "1000";
    private CheckLineItemCategory categoryType = CheckLineItemCategory.INDEMNITY;
    private String deductibleAmount = "250";
    private String companyCheckBook = "Farm Bureau";

    private String exposureType = "Comprehensive";

    private WebDriver driver;

    @AfterMethod(alwaysRun = true)
    public void tearDown() throws Exception {
        this.driver.quit();
    }

//    @Test()
//    public void NewFNOL() throws Exception {
//
//        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver)
//                .withCreatorUserNamePassword(user.toString(), password)
//                .withSpecificIncident(incidentName)
//                .withLossDescription(lossDescription)
//                .withLossCause(lossCause)
//                .withLossRouter(lossRouter)
//                .withAdress(address)
//                .withPolicyNumber(policyNumber)
//                .build(GenerateFNOLType.AutoGlass);
//
//
//        this.claimNumber = myFNOLObj.claimNumber;
//        System.out.println(myFNOLObj.claimNumber);
//        logout();
//    }

//    @Test
//    public void newExposure() throws Exception {
//        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
//        driver = buildDriver(cf);
//        GenerateExposure exposureObj = new GenerateExposure.Builder(driver)
//                .withCreatorUserNamePassword(user.toString(), password)
//                .withClaimNumber(this.claimNumber)
//                .withCoverageType(exposureType)
//                .build();
//
//        System.out.println(exposureObj.getClaimNumber());
//    }

    //     Reserves
//    @Test()
//    public void personalPCReserves() throws Exception {
//        ArrayList<ReserveLine> lines = new ArrayList<>();
//        ReserveLine line1 = new ReserveLine();
//        lines.add(line1);
//        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
//        driver = buildDriver(cf);
//        GenerateReserve myClaimObj = new GenerateReserve.Builder(this.driver)
//                .withCreatorUserNamePassword(user.toString(), password)
//                .withClaimNumber(this.claimNumber)
//                .withReserveLines(lines)
//                .build();
//
//        System.out.println(myClaimObj.claimNumber);
//
//    }
//
    @Test()
    public void createCheckComprehensive() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateCheck myCheck = new GenerateCheck.Builder(this.driver)
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

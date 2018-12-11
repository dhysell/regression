package scratchpad.denver.old;

import repository.cc.enums.CheckLineItemCategory;
import repository.cc.enums.CheckLineItemType;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateCheckType;
import repository.gw.enums.GenerateFNOLType;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import repository.gw.generate.cc.GenerateCheck;

import java.util.ArrayList;
public class MBREFNOL extends BaseTest {
	private WebDriver driver;
    // User Login Information
    private ClaimsUsers user = ClaimsUsers.abatts;
    private String password = "gw";

    // Claim Number Methods
    private String claimNumber = "";

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    // FNOL Specific Strings
    private String incidentName = "Random";
    private String lossDescription = "Loss Description Test";
    private String lossCause = "Explosion";
    private String lossRouter = "Major Incident";
    private String address = "Random";
    private String policyNumber = "08-225497-01";
    private String coverageNeeded = "Equipment Breakdown - Building";

    // Exposure Strings.
    private String exposureType = "Equipment Breakdown - Building";

    // Check Specific Strings
    private boolean deductibleToAdd = false;
    private CheckLineItemType paymentType = CheckLineItemType.INDEMNITY;
    private String paymentAmount = "1000";

    private CheckLineItemCategory categoryType = CheckLineItemCategory.INDEMNITY;
    private String deductibleAmount = "250";
    private String companyCheckBook = "Farm Bureau";


    @Test
    public void mbreFNOL() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);

        repository.gw.generate.cc.GenerateFNOL myFNOLObj = new repository.gw.generate.cc.GenerateFNOL.Builder(driver).withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName).withLossDescription(lossDescription).withLossCause(lossCause)
                .withLossRouter(lossRouter).withAdress(address).withPolicyNumber(policyNumber).withtopLevelCoverage(coverageNeeded)
                .build(GenerateFNOLType.Property);
        this.setClaimNumber(myFNOLObj.claimNumber);
    }

    // Exposure
    @Test(dependsOnMethods = {"mbreFNOL"})
    public void createExposureEquipmentBreakdown() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        repository.gw.generate.cc.GenerateExposure exposureObj = new repository.gw.generate.cc.GenerateExposure.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withCoverageType(exposureType)
                .build();

        System.out.println(exposureObj.getClaimNumber());
    }

    // Reserves
    @Test(dependsOnMethods = {"createExposureEquipmentBreakdown"})
    public void createReserveEquipmentBreakdown() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);

        ArrayList<repository.gw.generate.cc.ReserveLine> lines = new ArrayList<repository.gw.generate.cc.ReserveLine>();
        repository.gw.generate.cc.ReserveLine line1 = new repository.gw.generate.cc.ReserveLine();
        lines.add(line1);

        repository.gw.generate.cc.GenerateReserve myClaimObj = new repository.gw.generate.cc.GenerateReserve.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withReserveLines(lines)
                .build();

        System.out.println(myClaimObj.claimNumber);
    }

    // Checks
    @Test(dependsOnMethods = {"createReserveEquipmentBreakdown"})
    public void createCheckEquipmentBreakdown() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        repository.gw.generate.cc.GenerateCheck myCheck = new GenerateCheck.Builder(driver)
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

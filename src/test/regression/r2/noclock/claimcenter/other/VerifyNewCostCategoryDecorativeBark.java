package regression.r2.noclock.claimcenter.other;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.testng.listener.QuarantineClass;

import repository.cc.claim.FinancialsTransactions;
import repository.cc.sidemenu.SideMenuCC;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimSearchLineOfBusiness;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.generate.cc.GenerateExposure;
import repository.gw.generate.cc.GenerateFNOL;
import repository.gw.generate.cc.GenerateReserve;
import repository.gw.generate.cc.ReserveLine;
import gwclockhelpers.ApplicationOrCenter;
@QuarantineClass
public class VerifyNewCostCategoryDecorativeBark extends BaseTest {
	private WebDriver driver;
    private ClaimsUsers user = ClaimsUsers.gmurray;
    private String password = "gw";

    private String claimNumber = null;

    // FNOL Specific Strings
    private String incidentName = "Random";
    private String lossDescription = "Loss Description Test";
    private String lossCause = "Fire or lightning";
    private String lossRouter = "Random";
    private String address = "Random";
    private String policyNumber = "Random";
    private String coverageNeeded = "Dwelling";
    private ClaimSearchLineOfBusiness lob = ClaimSearchLineOfBusiness.City_Squire_Policy_Property;
    // Exposure Strings. 
    private String exposureType = "Dwelling - Property Damage";

    @Test
    public void fnolDecorativeBark() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName)
                .withLossDescription(lossDescription)
                .withtopLevelCoverage(coverageNeeded)
                .withLossCause(lossCause)
                .withLOB(lob)
                .withLossRouter(lossRouter)
                .withAdress(address)
                .withPolicyNumber(policyNumber)
                .build(GenerateFNOLType.Property);

        this.claimNumber = myFNOLObj.claimNumber;
        this.incidentName = myFNOLObj.specificIncident;

    }

    // Exposures
    @Test(dependsOnMethods = {"fnolDecorativeBark"})
    public void exposuresDecorativeBark() throws Exception {
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

    /**
     * @throws Exception
     * @Author iclouser
     * @Requirement Make sure the cost category decorative bark is added.
     * @RequirementsLink <a href="https://rally1.rallydev.com/#/10552165958d/detail/userstory/53222047228">Rally Story</a>
     * @Description
     * @DATE Jun 6, 2016
     */
    @Test(dependsOnMethods = {"exposuresDecorativeBark"})
    public void reservesDecorativeBark() throws Exception {

        ArrayList<ReserveLine> lines = new ArrayList<ReserveLine>();
        String costCat = "Decorative Bark";
        ReserveLine line1 = new ReserveLine(incidentName, costCat, "Random");
        lines.add(line1);
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateReserve myClaimObj = new GenerateReserve.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withReserveLines(lines)
                .build();


        SideMenuCC sideMenu = new SideMenuCC(driver);
        sideMenu.clickFinancialTransactions();
        FinancialsTransactions financialTrans = new FinancialsTransactions(driver);
        financialTrans.selectReserves();

        boolean costCatCorrect = driver.findElements(By.xpath("//div[text()='Decorative Bark']")).size() > 0;
        Assert.assertTrue(costCatCorrect, "Didn't find the Decorative Bark Cost Category");
        System.out.println(myClaimObj.claimNumber);


    }

}

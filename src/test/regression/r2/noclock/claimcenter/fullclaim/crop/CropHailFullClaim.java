package regression.r2.noclock.claimcenter.fullclaim.crop;

import com.idfbins.driver.BaseTest;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import repository.cc.claim.CropHailCalculator;
import repository.cc.claim.Incidents;
import repository.cc.claim.LossDetails;
import repository.cc.claim.Reserve;
import repository.cc.sidemenu.SideMenuCC;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.generate.cc.GenerateFNOL;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;

import java.time.LocalDate;
public class CropHailFullClaim extends BaseTest {
	private WebDriver driver;
    private ClaimsUsers user = ClaimsUsers.bhogan;
    private String password = "gw";

    private String claimNumber = null;

    // FNOL Specific Strings
    private String incidentName = "Random";
    private String lossDescription = "Loss Description Test";
    private String lossCause = "Random";
    private String lossRouter = "Random";
    private String address = "Random";
    private String policyNumber = "01-076462-04";

    @Test()
    public void newFNOL() throws Exception {

        // Set Local Date (Year, Month, Day of Month)
        LocalDate lossDate = LocalDate.of(2017, 7, 27);
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver).withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName).withLossDescription(lossDescription).withLossCause(lossCause)
                .withLossRouter(lossRouter).withAdress(address).withPolicyNumber(policyNumber).withDateOfLoss(lossDate)
                .build(GenerateFNOLType.CropHail);
        this.claimNumber = myFNOLObj.claimNumber;
    }

//    @Test(dependsOnMethods = {"newFNOL"})
//    public void fnolDocumentCheck() throws Exception {
//
//        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
//        driver = buildDriver(cf);
//
//        TopMenu topMenu = new TopMenu(this.driver);
//        SideMenuCC sideMenu = new SideMenuCC(this.driver);
//        new Login(this.driver).login(user.toString(), this.password);
//        topMenu.//        topMenu.clickClaimTabArrow();
//        topMenu.//        topMenu.setClaimNumberSearch(claimNumber);
//        topMenu.//        sideMenu.clickDocuments();
//        topMenu.//
//        boolean documentsFound = false;
//        int timeElapsed = 0;
//
//        while (!documentsFound && timeElapsed <= 180) {
//            this.driver.navigate().refresh();
//            List<WebElement> documents = sideMenu.finds(By.xpath("//a[contains(., 'Crop')  and @class='g-actionable']"));
//            if (documents.size() == 4) {
//                documentsFound = true;
//            } else {
//                timeElapsed = timeElapsed + 30;
//                System.out.println("Current time taken: " + timeElapsed);
//            }
//        }
//
//        if (!documentsFound && timeElapsed >= 180) {
//            Assert.assertTrue(documentsFound, "Failed to find the crop hail documents the were supposed to be created from the fnol, check that the document plugin is working correctly");
//        }
//
//    }

    @Test(dependsOnMethods = {"newFNOL"})
    public void cropHailTest() throws Exception {

        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);

        Login lp = new Login(driver);
        TopMenu topMenu = new TopMenu(driver);
        SideMenuCC sideMenu = new SideMenuCC(driver);
        LossDetails lossDetails = new LossDetails(driver);
        Incidents incidents = new Incidents(driver);
        CropHailCalculator cropHail = new CropHailCalculator(driver);
        Reserve reserve = new Reserve(driver);

        lp.login(user.toString(), this.password);
        topMenu.clickClaimTabArrow();
        topMenu.setClaimNumberSearch(claimNumber);

        sideMenu.clickLossDetailsLink();

        sideMenu.clickWhenClickable(driver.findElement(By.xpath(
                "//a[@id='ClaimLossDetails:ClaimLossDetailsScreen:LossDetailsPanelSet:LossDetailsCardCV:LossDetailsDV:EditableCropIncidentsLV:0:LineNumber']")));

        lossDetails.clickEditCrop();

        incidents.setCropDamageDescription("Test Damage Description");

        incidents.isReplant();

/*        cropHail.inputBreakOutAcres();
        cropHail.inputRandomPercentGross();
        String value = cropHail.getPaymentAmount();*/

        this.driver.findElement(By.id("EditCropIncidentPopup:EditCropIncidentScreen:Update")).click();
        reserve.approveReserves(this.claimNumber);

        new GuidewireHelpers(driver).logout();

        // If The value isn't zero the FNOL creates both the exposure and
        // reserve for the incident.
        // So we can write a check on them.
/*        if (!value.equals("0") && !value.equals("")) {
            GenerateCheck myCheck = new GenerateCheck.Builder(driver)
                    .withCreatorUserNamePassword(user, this.password)
                    .withClaimNumber(this.claimNumber)
                    .build(GenerateCheckType.Regular);

            System.out.println(myCheck.claimNumber);

        } else {
            System.out.println(
                    "----------------------------------------------------------------------------------------------------");
            System.out.println(
                    "-------Since the payment amount is zero no exposures or reserves were auto-generated. -------------------------");
            System.out.println(
                    "-------This means nothing else can be tested at this point and the test is considered passed. -------------");
            System.out.println(
                    "----------------------------------------------------------------------------------------------------");
        }*/

    }

}

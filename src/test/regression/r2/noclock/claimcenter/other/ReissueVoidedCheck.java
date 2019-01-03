package regression.r2.noclock.claimcenter.other;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.testng.listener.QuarantineClass;

import repository.cc.actionsmenu.ActionsMenu;
import repository.cc.claim.EditExposures;
import repository.cc.claim.ReopenClaim;
import repository.cc.claim.Reserve;
import repository.cc.claim.SummaryOverview;
import repository.cc.claim.SummaryOverviewExposures;
import repository.cc.enums.CheckLineItemCategory;
import repository.cc.enums.CheckLineItemType;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateCheckType;
import repository.gw.generate.cc.GenerateCheck;
import repository.gw.generate.cc.VoidedCheck;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;

@QuarantineClass
public class ReissueVoidedCheck extends BaseTest {
	private WebDriver driver;
    private VoidedCheck voidedCheck = null;

    // Login Info
    private ClaimsUsers userName = ClaimsUsers.jgross;
    private ClaimsUsers managerName = ClaimsUsers.dalley;
    private String password = "gw";

    // Search Info for checks
    private String reserveLine;
    private String costCategory;
    private String approvedBy = "Examiners";
    private String checkStatus = "Voided";
    private String searchRange = "Last 90 days";
    private int rowNumber = -1;                    // Set at -1 for random selection

    @Test
    public void getVoidedCheck() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        VoidedCheck voidedCheck = new VoidedCheck.Builder(driver).withUserNamePassword(userName, this.password)
                .withApprovedBy(approvedBy).withCheckStatus(checkStatus).withSearchRange(searchRange)
                .withRowNumber(rowNumber).build();


        this.voidedCheck = voidedCheck;
    }

    // Reserves
    @Test(dependsOnMethods = {"getVoidedCheck"})
    public void reservesTest() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        new Login(driver).login(managerName.toString(), password);


        TopMenu topMenu = new TopMenu(driver);
        topMenu.clickClaimTabArrow();
        topMenu.setClaimNumberSearch(voidedCheck.getClaimNumber());


        if (topMenu.getClaimStatus().equalsIgnoreCase("Closed")) {
            ActionsMenu actions = new ActionsMenu(driver);

            actions.clickActionsButton();

            actions.clickReopenClaim();

            ReopenClaim rc = new ReopenClaim(driver);
            rc.reopenClaim();

        }

        SummaryOverview summary = new SummaryOverview(driver);
        List<SummaryOverviewExposures> exposures = summary.getExposuresList();
        SummaryOverviewExposures currentExposure = new SummaryOverviewExposures(driver);
        WebElement expoLink = null;

        for (SummaryOverviewExposures expo : exposures) {
            if (this.voidedCheck.getExposureData().contains(expo.getType().getText()) &&
                    this.voidedCheck.getExposureData().contains(expo.getClaimant())) {
                currentExposure = expo;
                expoLink = expo.getType();
                break;
            }
        }

        expoLink.click();


        EditExposures editExposure = new EditExposures(driver);
        String exposureName = editExposure.getExposureName();
        String costCategory = editExposure.getCoverageSubtype();

        this.reserveLine = exposureName;
        this.costCategory = costCategory;

        currentExposure.setNewExposureName(exposureName);

        if (currentExposure.getStatus().equalsIgnoreCase("Closed")) {
            editExposure.clickReopenExposureButton();
        }


        editExposure.clickCreateReserveButton();

        Reserve reserves = new Reserve(driver);
        reserves.setCustomReserveLine(exposureName, this.voidedCheck.getAmount());
    }


    @Test(dependsOnMethods = {"reservesTest"})
    public void reissueVoidedCheck() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateCheck myCheck = new GenerateCheck.Builder(driver).withCreatorUserNamePassword(userName, this.password)
                .withClaimNumber(this.voidedCheck.getClaimNumber()).withDeductible(false)
                .withDeductibleAmount(null).withPaymentType(CheckLineItemType.INDEMNITY).withCategoryType(CheckLineItemCategory.INDEMNITY)
                .withPaymentAmount(this.voidedCheck.getAmount()).withCompanyCheckBook("Farm Bureau").withPayToName(voidedCheck.getPayToName())
                .withMailingAddress(voidedCheck.getMailingAddress()).withTaxReproting(voidedCheck.getTaxReporting()).withReserveLine(reserveLine)
                .withCostCategory(costCategory).withCheckNumber(voidedCheck.getCheckNumber()).build(GenerateCheckType.ReissueVoided);

        System.out.println(myCheck.claimNumber);
    }
}

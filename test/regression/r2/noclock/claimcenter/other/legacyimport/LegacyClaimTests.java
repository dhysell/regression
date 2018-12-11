package regression.r2.noclock.claimcenter.other.legacyimport;


import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.cc.actionsmenu.ActionsMenu;
import repository.cc.claim.EditExposures;
import repository.cc.claim.VacationStatusCC;
import repository.cc.enums.CheckLineItemCategory;
import repository.cc.sidemenu.SideMenuCC;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateCheckType;
import repository.gw.generate.cc.GenerateCheck;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
public class LegacyClaimTests extends BaseTest {
	private WebDriver driver;
    // List of imported Legacy Claims
    private List<String> legacyClaimNumbers = LegacyFileInteractions.readFile();

    // User Login Information
    private ClaimsUsers user = ClaimsUsers.adegiulio;
    private String password = "gw";

    // Check Specific Strings
//    private boolean deductibleToAdd = false;
//    private String paymentType = "Supplemental";
    private String paymentAmount = Integer.toString(NumberUtils.generateRandomNumberInt(5, 100)) + "." +
            Integer.toString(NumberUtils.generateRandomNumberInt(01, 99));
    private CheckLineItemCategory categoryType = CheckLineItemCategory.INDEMNITY;

    @Test(invocationCount = 20, enabled = false)
    public void legacyImport() throws Exception {

        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);

        driver.get("http://fbmisqa100a:8080/cc/ClaimCenter.do");

        VacationStatusCC vacationStatus = new VacationStatusCC(driver);
        TopMenu topMenu = new TopMenu(driver);
        ActionsMenu actionsMenu = new ActionsMenu(driver);

        new Login(driver).login(user.toString(), password);
        vacationStatus.setVacationStatusAtWork();

        List<String> randomLegacyClaimNumbers = getRandomLagacyClaimNumbers(1);

        for (String claimNumber : randomLegacyClaimNumbers) {
            System.out.println("****************************************************");
            System.out.println(claimNumber);

            topMenu.clickSearchTab().clickSimpleSearch().searchByClaimNumberAndSelect(claimNumber);
            actionsMenu.clickActionsOtherLink();
            actionsMenu.clickRecoveryLink().addRandomRecovery();
        }
    }

    @Test(enabled = false)
    public void validateCoverage() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);

        driver.get("http://fbmisqa100a:8080/cc/ClaimCenter.do");

        VacationStatusCC vacationStatus = new VacationStatusCC(driver);
//        TopMenu topMenu = new TopMenu(driver);
//        ActionsMenu actionsMenu = new ActionsMenu(driver);
        Login login = new Login(driver);
        login.login(user.toString(), password);

        vacationStatus.setVacationStatusAtWork();

        List<String> randomLegacyClaimNumbers = getRandomLagacyClaimNumbers(1);
        String claimNumber = randomLegacyClaimNumbers.get(0);

        System.out.println("****************************************************");
        System.out.println(claimNumber);
    }

    @Test(invocationCount = 20)
    public void checkIncidents() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);

        driver.get("http://fbmisqa100a:8080/cc/ClaimCenter.do");

        VacationStatusCC vacationStatus = new VacationStatusCC(driver);
        TopMenu topMenu = new TopMenu(driver);
        Login login = new Login(driver);
        login.login(user.toString(), password);

        vacationStatus.setVacationStatusAtWork();

        List<String> randomLegacyClaimNumbers = getRandomLagacyClaimNumbers(1);
        String claimNumber = randomLegacyClaimNumbers.get(0);

        System.out.println("****************************************************");
        System.out.println(claimNumber);

        topMenu.clickSearchTab().clickSimpleSearch().searchByClaimNumberAndSelect(claimNumber);

//        List<String> incidents = new SideMenuCC(driver).clickLossDetailsLink().getIncidentListV2();

        List<WebElement> claimsExposureLinks = new SideMenuCC(driver).onSideMenu().clickExposuresLink().getIncidentTypeLinks();

        if (claimsExposureLinks.size() > 0) {
            for (int i = 0; i < claimsExposureLinks.size(); i++) {
                EditExposures exposure = new SideMenuCC(driver).onSideMenu().clickExposuresLink().selectExposure(i);
                exposure.onEditExposures().clickEditButton();
                exposure.setCoverageAndUpdateExposure();
                exposure.clickUpToExposures();
            }
        } else {
            Assert.fail("No Exposures Found on Claim.");
        }


        GenerateCheck legacyCheck = new GenerateCheck.Builder(driver)
                .withCreatorUserNamePassword(user, this.password)
                .withClaimNumber(claimNumber)
      //          .withPaymentType(paymentType)
                .withCategoryType(categoryType)
                .withPaymentAmount(paymentAmount)
                .build(GenerateCheckType.Regular);

        System.out.println(legacyCheck.claimNumber);


    }

    private List<String> getRandomLagacyClaimNumbers(int numClaims) {

        List<String> claimNumbers = new ArrayList<>();

        for (int i = 0; i < numClaims; i++) {
            int selectedIndex = NumberUtils.generateRandomNumberInt(0, legacyClaimNumbers.size() - 1);
            claimNumbers.add(legacyClaimNumbers.get(selectedIndex));
            legacyClaimNumbers.remove(selectedIndex);
        }

        return claimNumbers;
    }


}

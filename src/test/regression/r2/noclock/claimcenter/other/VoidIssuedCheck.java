package regression.r2.noclock.claimcenter.other;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.testng.listener.QuarantineClass;

import repository.cc.claim.CheckDetails;
import repository.cc.claim.searchpages.SearchChecksCC;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.PopUpWindow;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;

@QuarantineClass

public class VoidIssuedCheck extends BaseTest {
	private WebDriver driver;
    // Login Info
    private ClaimsUsers userName = ClaimsUsers.abatts;
    private String password = "gw";

    // Search Info for checks
    private String approvedBy = "Examiners";
    private String checkStatus = "Issued";
    private String searchRange = "Last 90 days";

    @SuppressWarnings("unused")
    @Test
    public void voidAnIssuedCheck() throws Exception {

        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);

        TopMenu topMenu = new TopMenu(driver);
        SearchChecksCC searchChecks = new SearchChecksCC(driver);
        CheckDetails checkDetails = new CheckDetails(driver);

        new Login(driver).login(userName.toString(), password);

        topMenu.clickSearchTabArrow();

        topMenu.clickSearchChecks();

        // Search Criteria for Checks
        searchChecks.selectSpecific_ApprovedByGroup(approvedBy);

        searchChecks.selectSpecific_CheckStatus(checkStatus);

        searchChecks.selectSpecific_DateSearchRange(searchRange);

        searchChecks.clickSearchButton();


        // Finds the number of checks on the first page. Then selects one randomly to void.
        List<WebElement> checkLinks = driver.findElements(By.xpath("//a[contains(@id, 'CheckNumber')]"));
        // pick a random check number on the first page
        int randomSelection = NumberUtils.generateRandomNumberInt(0, checkLinks.size() - 1);
        checkLinks.get(randomSelection).click();

        checkDetails.clickVoidStopButton();

        checkDetails.sendReasonVoid("Because Reasons!");

        checkDetails.clickVoidCheckButton();

        PopUpWindow pop = new PopUpWindow(driver, driver.findElement(By.xpath("//span[contains(text(), 'OK')]")));


        checkDetails.validateVoidedName(userName);
        String date = DateUtils.getCenterDateAsString(driver, ApplicationOrCenter.ClaimCenter, "MM/dd/yyyy");
        checkDetails.validateVoidedDate(date);
    }

}

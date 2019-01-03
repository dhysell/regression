package regression.r2.noclock.claimcenter.other;

import java.util.List;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.testng.listener.QuarantineClass;

import repository.cc.claim.Snapshot;
import repository.cc.sidemenu.SideMenuCC;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.cc.GenerateFNOL;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
@QuarantineClass
public class CheckFnolSnapshot extends BaseTest {
	private WebDriver driver;
    private ClaimsUsers user = ClaimsUsers.gmurray;
    private String password = "gw";

    private String claimNumber = "01033769022015120201";

    // FNOL Specific Strings
    private String incidentName = "Random";
    private String lossDescription = "Loss Description Test";
    private String lossCause = "Random";
    private String lossRouter = "Random";
    private String address = "Random";
    private String policyNumber = "Random";

    @Test
    public void propertyFNOL() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver).withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName).withLossDescription(lossDescription).withLossCause(lossCause)
                .withLossRouter(lossRouter).withAdress(address).withPolicyNumber(policyNumber)
                .build(GenerateFNOLType.Property);
        this.claimNumber = myFNOLObj.claimNumber;

    }


    /**
     * @throws GuidewireException
     * @Author iclouser
     * @Requirement
     * @RequirementsLink <a href="https://rally1.rallydev.com/#/10552165958d/detail/userstory/51141943795">Rally Story</a>
     * @Description Test to make sure that the phone number is being generated in fnolsnapshot.
     * @DATE Feb 11, 2016
     */
    @Test(dependsOnMethods = "propertyFNOL")
    public void checkPropertySnapShot() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);

        new Login(driver).login(user.toString(), password);
        TopMenu topMenu = new TopMenu(driver);
        SideMenuCC sideMenu = new SideMenuCC(driver);
        Snapshot snapshot = new Snapshot(driver);
        topMenu.clickClaimTabArrow();
        topMenu.setClaimNumberSearch(claimNumber);

        sideMenu.clickFNOLSnapshot();

        List<WebElement> phoneNumber = driver.findElements(By.xpath("//div[contains(@id,':ClaimStatusInputSet:CreateUserPhone-inputEl')]"));

        // Make sure the div exists before you make sure that it is a phone number.
        Assert.assertTrue(phoneNumber.size() > 0, "Couldn't find the phone number div.");

        // Regex for north american phone numbers
        String phoneRegex = "^\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$";
        Assert.assertTrue(Pattern.matches(phoneRegex, snapshot.getCreatorPhoneNumber()), "Didn't seem to find the phone number, the text that was there was: " + snapshot.getCreatorPhoneNumber());
    }
}

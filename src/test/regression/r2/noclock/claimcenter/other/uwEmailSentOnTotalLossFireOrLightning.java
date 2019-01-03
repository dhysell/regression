package regression.r2.noclock.claimcenter.other;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.testng.listener.QuarantineClass;

import repository.cc.administration.MessageQueuesCC;
import repository.cc.claim.LossDetails;
import repository.cc.claim.incidents.EditPropertyIncidents;
import repository.cc.sidemenu.SideMenuCC;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.generate.cc.GenerateFNOL;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
@QuarantineClass
public class uwEmailSentOnTotalLossFireOrLightning extends BaseTest {

	private WebDriver driver;
    private ClaimsUsers user = ClaimsUsers.gmurray;
    private String password = "gw";

    private String claimNumber = null;

    // FNOL Specific Strings
    private String incidentName = "Random";
    private String lossDescription = "Loss Description Test";
    private String lossCause = "Fire or lightning";
    private String lossRouter = "Major Incident";
    private String address = "Random";
    private String policyNumber = "Random";

    @Test
    public void fireAndLightningFNOL() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver).withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName).withLossDescription(lossDescription).withLossCause(lossCause)
                .withLossRouter(lossRouter).withAdress(address).withPolicyNumber(policyNumber)
                .build(GenerateFNOLType.Property);
        this.claimNumber = myFNOLObj.claimNumber;


        MessageQueuesCC queues = new MessageQueuesCC(driver);
        queues.suspendEmailMessageQue();
        new Login(driver).login(user.toString(), "gw");

        TopMenu topMenu = new TopMenu(driver);
        topMenu.goToClaimByClaimNumber(claimNumber);

        SideMenuCC sideMenu = new SideMenuCC(driver);
        LossDetails lossDetails = sideMenu.clickLossDetailsLink();

        lossDetails.clickIncidentsInTable(0);

        EditPropertyIncidents ePropIncident = new EditPropertyIncidents(driver);
        ePropIncident.totalLossProperty();


        queues = new MessageQueuesCC(driver);
        queues.navigateToClaimInEventMessaging(claimNumber);
        queues.checkMessagePayload("Please send a new DEC to the insured in the event the insurance papers were destroyed.");

        queues.resumeEmailMessageQueue();

    }


}

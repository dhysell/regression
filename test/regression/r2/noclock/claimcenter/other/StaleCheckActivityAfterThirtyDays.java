package regression.r2.noclock.claimcenter.other;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.testng.listener.QuarantineClass;

import repository.cc.actionsmenu.ActionsMenu;
import repository.cc.claim.NewDocument;
import repository.cc.claim.WorkplanCC;
import repository.cc.sidemenu.SideMenuCC;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
@QuarantineClass
public class StaleCheckActivityAfterThirtyDays extends BaseTest {
	private WebDriver driver;
    // Login Info
    private ClaimsUsers userName = ClaimsUsers.abatts;
    private String password = "gw";
    private String documentName = "staleCheckTestDoc";


    @Test()
    public void staleCheckFirstRequest() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);

        new Login(driver).login(userName.toString(), password);

        TopMenu topMenu = new TopMenu(driver);
        topMenu.clickClaimTabArrow();
        topMenu.clickRandomClaim();

        ActionsMenu actions = new ActionsMenu(driver);
        actions.clickActionsButton();
        actions.clickCreateFromTemplate();


        NewDocument document = new NewDocument(driver);
        String documentDirectoryPath = "\\\\fbmsqa11\\test_data\\test-documents\\";

        document.createDocumentFromTemplate("Stale", "FB Stale Check Letter First Request Letter",
                "Testing Stale Check Activities", documentDirectoryPath);

        document.setAttachment(documentDirectoryPath, documentName);


        document.assignDocumentTo();

        document.clickUpdateButton();

        SideMenuCC sideMenu = new SideMenuCC(driver);
        sideMenu.clickWorkplanLink();

        WorkplanCC workPlan = new WorkplanCC(driver);
        workPlan.confirmStaleCheckActivities("Stale Check Letter - Send Stale Check Letter Second Request");

    }

    @Test()
    public void staleCheckSecondRequest() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        new Login(driver).login(userName.toString(), password);

        TopMenu topMenu = new TopMenu(driver);
        topMenu.clickClaimTabArrow();
        topMenu.clickRandomClaim();

        ActionsMenu actions = new ActionsMenu(driver);
        actions.clickActionsButton();
        actions.clickCreateFromTemplate();


        NewDocument document = new NewDocument(driver);
        String documentDirectoryPath = "\\\\fbmsqa11\\test_data\\test-documents\\";

        document.createDocumentFromTemplate("Stale", "FB Stale Check Letter Second Request Letter",
                "Testing Stale Check Activities", documentDirectoryPath);

        document.setAttachment(documentDirectoryPath, documentName);


        document.assignDocumentTo();

        document.clickUpdateButton();

        SideMenuCC sideMenu = new SideMenuCC(driver);
        sideMenu.clickWorkplanLink();

        WorkplanCC workPlan = new WorkplanCC(driver);
        workPlan.confirmStaleCheckActivities("Stale Check Letter - Send Stale Check Letter Final Request");

    }

    @Test()
    public void staleCheckFinalRequest() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        new Login(driver).login(userName.toString(), password);

        TopMenu topMenu = new TopMenu(driver);
        topMenu.clickClaimTabArrow();
        topMenu.clickRandomClaim();

        ActionsMenu actions = new ActionsMenu(driver);
        actions.clickActionsButton();

        actions.clickCreateFromTemplate();


        NewDocument document = new NewDocument(driver);
        String documentDirectoryPath = "\\\\fbmsqa11\\test_data\\test-documents\\";

        document.createDocumentFromTemplate("Stale", "FB Stale Check Letter Final Request Letter",
                "Testing Stale Check Activities", documentDirectoryPath);

        document.setAttachment(documentDirectoryPath, documentName);


        document.assignDocumentTo();

        document.clickUpdateButton();

        SideMenuCC sideMenu = new SideMenuCC(driver);
        sideMenu.clickWorkplanLink();

        WorkplanCC workPlan = new WorkplanCC(driver);
        workPlan.confirmStaleCheckActivities("Stale Check Letter - Send Unclaimed Property Notification");


    }
}

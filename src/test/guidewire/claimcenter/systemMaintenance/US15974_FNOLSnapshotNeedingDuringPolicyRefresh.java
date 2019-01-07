package guidewire.claimcenter.systemMaintenance;

import com.idfbins.driver.BaseTest;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import repository.cc.claim.Documents;
import repository.cc.claim.policypages.PolicyGeneral;
import repository.cc.entities.InvolvedParty;
import repository.cc.enums.PolicyType;
import repository.cc.sidemenu.SideMenuCC;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.generate.cc.GenerateFNOL;
import repository.gw.login.Login;

import java.time.LocalDate;

public class US15974_FNOLSnapshotNeedingDuringPolicyRefresh extends BaseTest {

    private String claimNumber;

    // User Data
    private ClaimsUsers user = ClaimsUsers.ktennant;
    private String password = "gw";
    private WebDriver driver = null;

    // FNOL Specific Strings
    private String incidentName = "Random";
    private String lossDescription = "Loss Description Test";
    private String lossCause = "Random";
    private String lossRouter = "Random";
    private String address = "Random";
    private String policyNumber = "01-123078-01";
    private LocalDate dateOfLoss = LocalDate.now();

    //FNOL object
    private GenerateFNOL myFNOLObj;

    @Test()
    public void unverifiedPolicyFnol() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter, "DEV");
        driver = buildDriver(cf);
        this.myFNOLObj = new GenerateFNOL.Builder(driver).withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName).withLossDescription(lossDescription).withLossCause(lossCause)
                .withLossRouter(lossRouter).withAdress(address).withPolicyNumber(policyNumber)
                .withPolicyType(PolicyType.CITY)
                .withDateOfLoss(dateOfLoss)
                .withVerifiedPolicy(false)
                .build(GenerateFNOLType.Auto);

        InvolvedParty test = myFNOLObj.getPartiesInvolved().get(0);
        System.out.println(test.getFirstName() + " " + test.getLastName());
        this.claimNumber = myFNOLObj.claimNumber;
    }

    @Test(dependsOnMethods = "unverifiedPolicyFnol")
    public void policyRefreshSnapshot() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter, "DEV");
        driver = buildDriver(cf);

        TopMenu topMenu = new TopMenu(driver);
        new Login(driver).login(user.toString(), password);

        topMenu.clickClaimTabArrow();
        topMenu.setClaimNumberSearch(this.claimNumber);

        SideMenuCC sideMenu = new SideMenuCC(driver);

        Documents documents = sideMenu.clickDocuments();
        documents.deleteDocument("FNOL Loss Details Snapshot - CLMS");
        documents.deleteDocument("FNOL Loss Details Snapshot - UNDR");

        PolicyGeneral policy = sideMenu.clickPolicyLink();
        policy.refreshPolicy();

        sideMenu.clickDocuments().clickSearch();

        //TODO
        // Finish conditionals.

        System.out.println();
    }
}

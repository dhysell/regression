package regression.r2.noclock.claimcenter.other;

import java.time.LocalDate;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.testng.listener.QuarantineClass;

import repository.cc.claim.NewClaimSaved;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.generate.cc.GenerateFNOL;
import gwclockhelpers.ApplicationOrCenter;
@QuarantineClass
public class ClaimsAssignedToSameOwnerWithSameDOL extends BaseTest {
	private WebDriver driver;
    private ClaimsUsers user = ClaimsUsers.gmurray;
    private String password = "gw";

    @SuppressWarnings("unused")
    private String claimNumber = "";

    // FNOL Specific Strings
    private String incidentName = "Random";
    private String lossDescription = "Loss Description Test";
    private String lossCause = "Random";
    private String lossRouter = "Minor Incident";
    private String address = "Random";
    private String policyNumber = "01-076829-01";
    private String assignedTo = "";
    LocalDate lossDate = null;

    @Test
    public void validateSameOwner() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);

        TopMenu topMenu;
        NewClaimSaved claimSaved;

        boolean sameAdjuster = false;
        autoFNOL();
        claimSaved = new NewClaimSaved(driver);
        topMenu = new TopMenu(driver);
        claimSaved.clickGoToClaimLink();
        assignedTo = topMenu.gatherClaimOwnerName();
        lossDate = topMenu.gatherDOL();

        propertyFnolTest();
        claimSaved = new NewClaimSaved(driver);
        topMenu = new TopMenu(driver);
        claimSaved.clickGoToClaimLink();
        sameAdjuster = assignedTo.equals(topMenu.gatherClaimOwnerName());
        Assert.assertTrue(sameAdjuster);

        imFnolTest();
        claimSaved = new NewClaimSaved(driver);
        topMenu = new TopMenu(driver);
        claimSaved.clickGoToClaimLink();
        sameAdjuster = assignedTo.equals(topMenu.gatherClaimOwnerName());
        Assert.assertTrue(sameAdjuster);

        genLiabFnolTest();
        claimSaved = new NewClaimSaved(driver);
        topMenu = new TopMenu(driver);
        claimSaved.clickGoToClaimLink();
        sameAdjuster = assignedTo.equals(topMenu.gatherClaimOwnerName());
        Assert.assertTrue(sameAdjuster);
    }

    private void autoFNOL() throws Exception {
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName)
                .withLossDescription(lossDescription)
                .withLossCause(lossCause)
                .withLossRouter(lossRouter)
                .withAdress(address)
                .withPolicyNumber(policyNumber)
                .build(GenerateFNOLType.Auto);
        this.claimNumber = myFNOLObj.claimNumber;

    }

    private void propertyFnolTest() throws Exception {
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName)
                .withLossDescription(lossDescription)
                .withLossCause(lossCause)
                .withLossRouter(lossRouter)
                .withAdress(address)
                .withPolicyNumber(policyNumber)
                .withDateOfLoss(lossDate)
                .build(GenerateFNOLType.Property);
        this.claimNumber = myFNOLObj.claimNumber;
    }

    private void imFnolTest() throws Exception {
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName)
                .withLossDescription(lossDescription)
                .withLossCause(lossCause)
                .withLossRouter(lossRouter)
                .withAdress(address)
                .withPolicyNumber(policyNumber)
                .withDateOfLoss(lossDate)
                .build(GenerateFNOLType.InlandMarine);
        this.claimNumber = myFNOLObj.claimNumber;
    }

    public void genLiabFnolTest() throws Exception {
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName)
                .withLossDescription(lossDescription)
                .withLossCause(lossCause)
                .withLossRouter("Minor Med Claims")
                .withAdress(address)
                .withPolicyNumber(policyNumber)
                .withDateOfLoss(lossDate)
                .build(GenerateFNOLType.GeneralLiability);
        this.claimNumber = myFNOLObj.claimNumber;
    }
}

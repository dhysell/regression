package scratchpad.shiva;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.enums.OrganizationType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;

public class US16729_VerifyPropertyAutoHistoryText extends BaseTest {

    public GeneratePolicy myPolicyObject = null;
    private WebDriver driver;
//ACCOUNT NUMBER: 298889
//AGENT USERNAME: kdegn

    public void submitDraftPolicy() throws Exception {
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
 //               .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withLineSelection(repository.gw.enums.LineSelection.PersonalAutoLinePL, repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withInsFirstLastName("DE16729", "Prop")
                .isDraft()
                .build(repository.gw.enums.GeneratePolicyType.FullApp);
    }
@Test
    public void verifyAutoPropertyHistory() throws Exception{
    Config cf = new Config(ApplicationOrCenter.PolicyCenter, "DEV");
    driver = buildDriver(cf);
   // submitDraftPolicy();
    new Login(driver).loginAndSearchAccountByAccountNumber("kdegn",
            "gw", "298889");
    AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
    pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Squire);
    SideMenuPC sideMenuPC = new SideMenuPC(driver);
    sideMenuPC.clickSideMenuSquirePropertyHistory();
    BasePage bp = new BasePage(driver);
    bp.clickWhenClickable(bp.find(By.xpath(" //*[@id='SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOPriorLossExtScreen:HOPriorLossExtPanelSet:OrderWithConfirmationID']")),50);
    SoftAssert softAssert = new SoftAssert();
    softAssert.assertEquals(bp.find(By.xpath("//*[@id='SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOPriorLossExtScreen:HOPriorLossExtPanelSet:LossHistoryInputSet:0']")).getText(),"No Loss History","Failed 12345");
    softAssert.assertEquals(bp.find(By.xpath("//*[@id='SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOPriorLossExtScreen:HOPriorLossExtPanelSet:APPropReportLabelClaimNotFound']")).getText(),"Claims Activity Profiler: No Matches","Failed at 999999");
    //*[@id='SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOPriorLossExtScreen:HOPriorLossExtPanelSet:LossHistoryInputSet:0']
    softAssert.assertAll();
      System.out.println("Done Shiva");
}
}

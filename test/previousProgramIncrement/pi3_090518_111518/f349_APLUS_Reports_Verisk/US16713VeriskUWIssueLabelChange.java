package previousProgramIncrement.pi3_090518_111518.f349_APLUS_Reports_Verisk;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.administration.AdminScriptParameters;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ScriptParameter;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuAdministrationPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;

/**
 * NOTE : These Test will Fail in Regression Until Verisk Feature Flag Enabled on Regression Environment.
 *
 * @Author swathiAkarapu
 * @Requirement US16713
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558454824ud/detail/userstory/260829997084">US16713</a>
 * @Description Steps to get there:
 * Go to Auto History or Property History screens in a policy
 * Don't order reports
 * Check risk analysis for UW issues
 * Acceptance Criteria:
 * Ensure that when Verisk is toggled on, the validations for property history do not reference "CLUE"
 * Ensure that when Verisk is toggled on, the validations for auto history do not reference "CLUE"
 * Ensure that the validations for both property and auto history still make sense in wording to the user
 * Ensure that the validations for both property and auto history trigger correctly, per previous functionality when ordering those reports
 * @DATE October 30, 2018
 */
@Test(groups = {"ClockMove"}) //This is not technically a clock move test, but it will mess with script parameters, so it needs to run in series.
public class US16713VeriskUWIssueLabelChange extends BaseTest {
    public GeneratePolicy myPolicyObject = null;
    private WebDriver driver;

    private void generatePolicy() throws Exception {

        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, repository.gw.enums.LineSelection.PersonalAutoLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withInsFirstLastName("Update", "text")
                .withPolEffectiveDate(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter))
                .isDraft()
                .build(repository.gw.enums.GeneratePolicyType.FullApp);
    }

    @Test
    public void verifyNoClue() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        AdminScriptParameters scriptParameters = new AdminScriptParameters(driver);
        try {
            //Need to set Verisk feature Enabled Script Parameter to true
            new Login(driver).login("su", "gw");
            new TopMenuAdministrationPC(driver).clickScriptParameters();
            scriptParameters.editScriptParameter(ScriptParameter.VeriskFeatureToggle, true);
            scriptParameters.clickUpToScriptParametersLink();
            scriptParameters.editScriptParameter(ScriptParameter.VeriskFeatureToggleProperty, true);
            new GuidewireHelpers(driver).logout();
            generatePolicy();

            new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);
            AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
            pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Squire);
            SideMenuPC pcSideMenu = new SideMenuPC(driver);
            SoftAssert softAssert = new SoftAssert();
            pcSideMenu.clickSideMenuSquireProperty();
            pcSideMenu.clickSideMenuRiskAnalysis();
            GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
            pcWorkOrder.clickGenericWorkorderQuote();
            GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
            if (quotePage.isPreQuoteDisplayed()) {
                quotePage.clickPreQuoteDetails();
            }


            GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);

            FullUnderWriterIssues underwriterIssues = riskAnalysis.getUnderwriterIssues();

            boolean clueBlockSubmitFound = underwriterIssues.getBlockSubmitList().stream().anyMatch(uw -> (uw.getLongDescription().contains("CLUE") || uw.getLongDescription().contains("CLUE")));

            boolean clueBlockQuoteFound = underwriterIssues.getBlockQuoteList().stream().anyMatch(uw -> (uw.getLongDescription().contains("CLUE") || uw.getLongDescription().contains("CLUE")));
            softAssert.assertFalse(clueBlockSubmitFound, "clue found in BlockSubmitList");
            softAssert.assertFalse(clueBlockQuoteFound, "clue found in BlockQuoteList");
            softAssert.assertAll();
        } catch (Exception e) {
            throw e;
        } finally {
            //switch Verisk feature enabled back to false
            try {
                new GuidewireHelpers(driver).logout();
            } catch (Exception e) {
                //Already logged out.
            }
            new Login(driver).login("su", "gw");
            new TopMenuAdministrationPC(driver).clickScriptParameters();
            scriptParameters.editScriptParameter(ScriptParameter.VeriskFeatureToggle, false);
            scriptParameters.clickUpToScriptParametersLink();
            scriptParameters.editScriptParameter(ScriptParameter.VeriskFeatureToggleProperty, false);
            new GuidewireHelpers(driver).logout();
            //End Script Parameter Setup
        }
    }
}

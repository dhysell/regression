package previousProgramIncrement.pi3_090518_111518.f349_APLUS_Reports_Verisk;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.administration.AdminScriptParameters;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ScriptParameter;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuAdministrationPC;
import repository.pc.workorders.generic.GenericWorkorderSquireCLUEAuto;
import repository.pc.workorders.generic.GenericWorkorderSquireCLUEProperty;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * NOTE : These Test will Fail in Regression Until Verisk Feature Flag Enabled on Regression Environment.
 *
 * @Author swathiAkarapu
 * @Requirement US16729
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558454824ud/detail/userstory/261019414768">US16729</a>
 * @Description As a PolicyCenter user I want to see that when ordering property or auto history and no results are found that there is a message that reads "Request Status: No claims reported. Claims reported at 0 on <date ordered>"  Similar to first attachment below but without the word 'CLUE'
 * <p>
 * Steps to get there:
 * Start new submission and get to property & auto history pages.
 * Order both histories
 * Acceptance criteria:
 * Ensure that property history returns a message for the user showing the report was ordered but there were no results or matches, if that is the case
 * If there are matches or results, those should still show instead
 * Ensure that auto history returns a message for the user showing the report was ordered but there were no results or matches, if that is the case
 * If there are matches or results, those should still show instead
 * @DATE October 30, 2018
 */
@Test(groups = {"ClockMove"}) //This is not technically a clock move test, but it will mess with script parameters, so it needs to run in series.
public class US16729VeriskNoClaims extends BaseTest {
    public GeneratePolicy myPolicyObject = null;
    private WebDriver driver;

    private void generatePolicy() throws Exception {

        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, repository.gw.enums.LineSelection.PersonalAutoLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withInsFirstLastName("Verisk", "noClaims")
                .isDraft()
                .build(repository.gw.enums.GeneratePolicyType.FullApp);
    }

    @Test
    public void noMatches() throws Exception {
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

            Underwriters uw = UnderwritersHelper.getRandomUnderwriter();
            new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObject.accountNumber);


            AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
            pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Squire);
            SideMenuPC pcSideMenu = new SideMenuPC(driver);

            pcSideMenu.clickSideMenuSquirePropertyHistory();
            GenericWorkorderSquireCLUEProperty propHistory = new GenericWorkorderSquireCLUEProperty(driver);
            propHistory.clickOrderPropertyHostory();
            SoftAssert softAssert = new SoftAssert();
            softAssert.assertTrue(propHistory.isNoMatchesFound(), "No Matches not Found on Property History");

            pcSideMenu.clickSideMenuAutoHistory();
            GenericWorkorderSquireCLUEAuto autoHiostory = new GenericWorkorderSquireCLUEAuto(driver);
            autoHiostory.clickOrderAutoHistory();

            softAssert.assertTrue(autoHiostory.isNoMatchesFound(), "No Matches not Found on Auto History");
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

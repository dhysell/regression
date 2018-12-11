package previousProgramIncrement.pi3_090518_111518.nonFeatures.Scope_Creeps;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;

/**
 * @Author swathiAkarapu
 * @Requirement DE7909
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558454824ud/detail/defect/253240774012">DE7909</a>
 * @Description Production, any user, any policy with auto only on any job
 * <p>
 * This happens when trying to visit the modifiers screen on a policy that only has Section III Auto. It is caused by the modifiers screen looking for modifiers on the homeowners line.
 * <p>
 * Steps to get there:
 * Have auto only policy
 * Visit modifier screen
 * Actual: Getting invalid server response
 * Expected: Don't want to get invalid server response
 * @DATE September 13 , 2018
 */
public class DE7909Verifymodifiers extends BaseTest {

    private GeneratePolicy myPolicyObject = null;
    private WebDriver driver;

    public void generatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withInsFirstLastName("Modifiers", "Error")
                .isDraft()
                .build(GeneratePolicyType.FullApp);

    }

    @Test(enabled = true)
    public void verifyModifiers() throws Exception {
        generatePolicy();
        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);
        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);
        pcSideMenu.clickSideMenuModifiers();
    }
}

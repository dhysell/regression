package regression.r2.noclock.policycenter.other;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;

/**
 * @Author skandibanda
 * @Requirement : US9210 : Billing county requirement change- PL only
 * @Description -  Changes to billing county under policy info screen (PL only ) for Owyhee and Bingham county
 * @DATE Oct 21, 2016
 */
public class TestPolicyInfoBillingCountyChanges extends BaseTest {

    private GeneratePolicy myPolicyObj;

    private WebDriver driver;

    @Test
    public void testSquireAutoPolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = new SquirePersonalAuto();

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Test", "Auto")
                .build(GeneratePolicyType.QuickQuote);
    }

    @Test(dependsOnMethods = {"testSquireAutoPolicy"})
    public void verifyBinghamBillingCountyMessage() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
        SideMenuPC sidemenu = new SideMenuPC(driver);
        GenericWorkorderPolicyInfo policyInfoPage = new GenericWorkorderPolicyInfo(driver);
        policyInfoPage.clickEditPolicyTransaction();
        sidemenu.clickSideMenuPolicyInfo();

        //Correct the wording for the question with Bingham county choice
        //"Is the primary insured within the territory limits of: Bingham West Territory (Aberdeen)?"
        policyInfoPage.setPolicyInfoBillingCounty("Bingham");
        String message = policyInfoPage.getBillingCountyMessage();
        String messages[] = message.split("\n");
        String ActualBinghamMessage = messages[0].concat(messages[1]);
        String expectedBinghamMessage = "Is the primary insured within the territory limits of:Bingham West Territory (Aberdeen)?";

        if (!ActualBinghamMessage.contentEquals(expectedBinghamMessage)) {
            Assert.fail("expected message" + expectedBinghamMessage + "is not displayed");
        }

        //Remove East Owyhee county and change West Owyhee county to just Owyhee.
        if (policyInfoPage.verifyPolicyInfoBillingCountyList("East Owyhee"))
            Assert.fail("East Owyhee should not be present in the Billing county List ");

        if (policyInfoPage.verifyPolicyInfoBillingCountyList("West Owyhee"))
            Assert.fail("East Owyhee should not be present in the Billing county List ");

    }

}

/**
 *
 */
package regression.r2.noclock.policycenter.other;

import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.errorhandling.ErrorHandling;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;

/**
 * @Author : Skandibanda
 * @Requirement : DE4201: FA Other Term Over Annual Policy Period error message
 * @Description : Checking for Expiry date login when Term Type as other
 */
@QuarantineClass
public class TestPolicyExpirationDate extends BaseTest {

    private GeneratePolicy myPolicyObj;

    private WebDriver driver;

    @Test
    public void testSquireAuto_FA() throws Exception {

        // line auto coverages
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow, MedicalLimit.TenK);

        // whole auto line
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Test", "Auto")
                .build(GeneratePolicyType.FullApp);

    }

    @Test(dependsOnMethods = {"testSquireAuto_FA"})
    public void validateSquireAuto() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        polInfo.setPolicyInfoTermType("Other");
        Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);

        //Expiry date less than 18 months
        Date expiryDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Month, 15);
        GenericWorkorder expireDate = new GenericWorkorder(driver);
        expireDate.setExpirationDate(expiryDate);
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickQuote();

        String errorMessage = "";
        GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
        ErrorHandling getBanner = new ErrorHandling(driver);
        if (getBanner.checkIfValidationResultsExists()) {
            if (riskAnalysis.getValidationMessagesText().contains("Policy term must be less than or equal to one year"))
                Assert.fail("Policy term must be less than or equal to one year validation message should not be displayed");
        }

        sideMenu.clickSideMenuRiskAnalysis();

        //validate UW block bind and block issuance

        FullUnderWriterIssues uwIssues = riskAnalysis.getUnderwriterIssues();
        if (uwIssues.isInList("Term Type Other").equals(UnderwriterIssueType.NONE)) {
            Assert.fail("Expected error Quote : Term Type Other is not displayed.");
        }

        sideMenu.clickSideMenuPolicyInfo();

        //Expiry date Equals 18 months
        polInfo.clickEditPolicyTransaction();
        Date expiryDate18Months = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Month, 18);
        expireDate.setExpirationDate(expiryDate18Months);
        quote.clickQuote();

        sideMenu.clickSideMenuRiskAnalysis();
        sideMenu.clickSideMenuPolicyInfo();

        //Expiry date More than 18 months
        polInfo.clickEditPolicyTransaction();
        Date expiryDate20Months = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Month, 20);
        expireDate.setExpirationDate(expiryDate20Months);
        quote.clickQuote();

		if (!new GenericWorkorder(driver).getValidationResults().contains("Expiration date must be greater than or equal to 30 days and less than 18 months from effective date")) {
            errorMessage = "Expected page validation : 'Expiration date must be greater than or equal to 30 days and less than 18 months from effective date' is not displayed. /n";
            Assert.fail(driver.getCurrentUrl() + errorMessage);

        }
    }
}






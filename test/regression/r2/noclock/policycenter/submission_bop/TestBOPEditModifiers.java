package regression.r2.noclock.policycenter.submission_bop;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.GeneratePolicyType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderModifiers;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement :DE4295: Modifiers -- Agent Is Not Able to Edit Modifiers on BOP Submission
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Jan 17, 2017
 */
public class TestBOPEditModifiers extends BaseTest {
    private GeneratePolicy bopPolicyObj;
    private int buildingFeature = 1, premisesEquip = 3, managementCredit = 4, employeesCredit = 1, lossExperince = 10, riskElements = 2;

    private WebDriver driver;

    @Test()
    public void testGenerateBusinessownersPolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        this.bopPolicyObj = new GeneratePolicy.Builder(driver)
                .withInsCompanyName("Test BOP")
                .withBusinessownersLine()
                .build(GeneratePolicyType.FullApp);
    }

    @Test(dependsOnMethods = {"testGenerateBusinessownersPolicy"})
    private void testValidateModifiers() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(bopPolicyObj.agentInfo.getAgentUserName(), bopPolicyObj.agentInfo.getAgentPassword(), bopPolicyObj.accountNumber);

        new GuidewireHelpers(driver).editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuModifiers();

        GenericWorkorderModifiers modifier = new GenericWorkorderModifiers(driver);
        modifier.setModifiersBuildingFeaturesCreditDebit(buildingFeature);
        modifier.setModifiersBuildingFeaturesJustification("Testing Building");
        modifier.setModifiersPremisesEquipmentCreditDebit(premisesEquip);
        modifier.setModifiersPremisesEquipmentJustification("Testing Premises");
        modifier.setModifiersManagementCreditDebit(managementCredit);
        modifier.setModifiersManagementJustification("Testing Management");
        modifier.setModifiersEmployeesCreditDebit(employeesCredit);
        modifier.setModifiersEmployeesJustification("Testing Employees");
        new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();

        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();
    }

    @Test(dependsOnMethods = {"testValidateModifiers"})
    private void testValidateModifierWithUW() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Commercial, Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), bopPolicyObj.accountNumber);

        new GuidewireHelpers(driver).editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuModifiers();

        GenericWorkorderModifiers modifier = new GenericWorkorderModifiers(driver);
        modifier.setModifiersLossExperienceCreditDebit(lossExperince);
        modifier.setModifiersLossExperienceJustification("Testing LossEx");
        modifier.setModifiersAdditionalRiskElementsCreditDebit(riskElements);
        modifier.setModifiersAdditionalRiskElementsJustification("Testing Additional");
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();

        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();
    }

    @Test(dependsOnMethods = {"testValidateModifierWithUW"})
    private void testValidateEnteredModifierValues() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(bopPolicyObj.agentInfo.getAgentUserName(), bopPolicyObj.agentInfo.getAgentPassword(), bopPolicyObj.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuModifiers();

        GenericWorkorderModifiers modifier = new GenericWorkorderModifiers(driver);
        String errorMessage = "";
        if (Integer.parseInt(modifier.getModifiersCreditDebitValueByColumnName("Building features")) != buildingFeature) {
            errorMessage = errorMessage + " Building features value is not matched \n";
        }

        if (Integer.parseInt(modifier.getModifiersCreditDebitValueByColumnName("Premises and equipment")) != premisesEquip) {
            errorMessage = errorMessage + " Premises and equipment value is not matched \n";
        }
        if (Integer.parseInt(modifier.getModifiersCreditDebitValueByColumnName("Management")) != managementCredit) {
            errorMessage = errorMessage + " Management value is not matched \n";
        }
        if (Integer.parseInt(modifier.getModifiersCreditDebitValueByColumnName("Employees")) != employeesCredit) {
            errorMessage = errorMessage + " Employees value is not matched \n";
        }
        if (Integer.parseInt(modifier.getModifiersCreditDebitValueByColumnName("Loss Experience")) != lossExperince) {
            errorMessage = errorMessage + " Loss Experience value is not matched \n";
        }
        if (Integer.parseInt(modifier.getModifiersCreditDebitValueByColumnName("Additional Risk Elements")) != riskElements) {
            errorMessage = errorMessage + " Additional Risk Elements value is not matched \n";
        }

        if (errorMessage != "")
            Assert.fail(errorMessage);
    }
}

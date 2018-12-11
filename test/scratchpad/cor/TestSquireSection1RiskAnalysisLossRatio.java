package scratchpad.cor;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.LossRatioDiscounts;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_LossRatios;
import persistence.globaldatarepo.entities.Agents;

/**
 * @Author nvadlamudi
 * @Requirement :US5667
 * @RequirementsLink <a href="http:// "rally1.rallydev.com/#/33274298124d/detail/userstory/43052027451</a>
 * @Description : Validating the Loss Ratio - Property Tab
 * @DATE Mar 30, 2016
 */
public class TestSquireSection1RiskAnalysisLossRatio extends BaseTest {
    private GeneratePolicy myPolicyObjPL = null;
    private WebDriver driver;

    @Test
    public void validateCityRiskAnalysisLossRatio() throws Exception {
        //Creating policy
        myPolicyObjPL = createPLPropertyPolicy(GeneratePolicyType.FullApp, SquireEligibility.City);
        Agents agent = myPolicyObjPL.agentInfo;

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        Login login = new Login(driver);

        guidewireHelpers.logout();
        //Login with agent
        login.loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), myPolicyObjPL.accountNumber);

        //validate Loss Ratio - Property discount
        validatePropertyLossRatioAgent(LossRatioDiscounts.FIFTEENDISCOUNT.getValue());

        guidewireHelpers.logout();

        //Login with Underwriter supervisor role to edit the loss ratios
        login.loginAndSearchSubmission("panderson", "gw", myPolicyObjPL.accountNumber);

        validatePropertyLossRadioDiscount(LossRatioDiscounts.TWENTYFIVESURCHARGE);

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();
        quote.clickQuote();
    }

    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Description : Validating for County
     * @DATE Mar 30, 2016
     */
    @Test
    public void validateCountyRiskAnalysisLossRatio() throws Exception {
        //Creating policy
        myPolicyObjPL = createPLPropertyPolicy(GeneratePolicyType.FullApp, SquireEligibility.City);
        Agents agent = myPolicyObjPL.agentInfo;
        Login login = new Login(driver);
        //Login with agent
        login.loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), myPolicyObjPL.accountNumber);

        //validate Loss Ratio - Property discount
        validatePropertyLossRatioAgent(LossRatioDiscounts.FIFTEENDISCOUNT.getValue());

        new GuidewireHelpers(driver).logout();

        //Login with Underwriter supervisor role to edit the loss ratios
        login.loginAndSearchSubmission("panderson", "gw", myPolicyObjPL.accountNumber);

        validatePropertyLossRadioDiscount(LossRatioDiscounts.TWENTYFIVESURCHARGE);

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();
        quote.clickQuote();
    }

    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Description : Validating for FarmRanch
     * @DATE Mar 30, 2016
     */
    @Test
    public void validateFarmRanchRiskAnalysisLossRatio() throws Exception {
        //Creating policy
        myPolicyObjPL = createPLPropertyPolicy(GeneratePolicyType.FullApp, SquireEligibility.City);
        Agents agent = myPolicyObjPL.agentInfo;

        Login login = new Login(driver);
        //Login with agent
        login.loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), myPolicyObjPL.accountNumber);

        //validate Loss Ratio - Property discount
        validatePropertyLossRatioAgent(LossRatioDiscounts.FIFTEENDISCOUNT.getValue());

        new GuidewireHelpers(driver).logout();

        //Login with Underwriter supervisor role to edit the loss ratios
        login.loginAndSearchSubmission("panderson", "gw", myPolicyObjPL.accountNumber);

        validatePropertyLossRadioDiscount(LossRatioDiscounts.TWENTYFIVESURCHARGE);

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();
        quote.clickQuote();
    }

    /**
     * @param value
     * @throws Exception
     * @Author nvadlamudi
     * @Description : validating Loss Ratio and adding discount
     * @DATE Mar 30, 2016
     */
    private void validatePropertyLossRadioDiscount(LossRatioDiscounts value) throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuHouseholdMembers();
        new GuidewireHelpers(driver).editPolicyTransaction();

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis_LossRatios risk = new GenericWorkorderRiskAnalysis_LossRatios(driver);
        risk.clickLossRatioTab();
        risk.clickLossRatioPropertyTab();

        risk.setLossRatioPropertyDiscount(value);
        risk.setPropertyReasonForChange("Testing purpose");
    }

    /**
     * @param defaultValue
     * @Author nvadlamudi
     * @Description : checking discount field is not editable
     * @DATE Mar 30, 2016
     */
    private void validatePropertyLossRatioAgent(String defaultValue) {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        boolean testFailed = false;
        String errorMessage = "";

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis_LossRatios risk = new GenericWorkorderRiskAnalysis_LossRatios(driver);
        risk.clickLossRatioTab();
        risk.clickLossRatioPropertyTab();

        if (risk.getPropertyDefaultDiscountAmount().contains(defaultValue))
            risk.systemOut("Expected default Loss Ratio for Property : 15% discount is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected default Loss Ratio for Property : 15% discount is not displayed.";
        }

        if (testFailed)
            Assert.fail(driver.getCurrentUrl() + errorMessage);
    }


    private GeneratePolicy createPLPropertyPolicy(GeneratePolicyType policyType, SquireEligibility eligibilityType) throws Exception {

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        locationsList.add(new PolicyLocation(locOnePropertyList));

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire(eligibilityType);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Anderson", "Thomas")
                .withPolOrgType(OrganizationType.Individual)
                .build(policyType);
        return myPolicyObjPL;
    }
}

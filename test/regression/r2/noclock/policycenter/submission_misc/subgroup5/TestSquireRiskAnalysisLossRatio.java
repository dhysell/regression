package regression.r2.noclock.policycenter.submission_misc.subgroup5;

import java.util.ArrayList;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.InlandMarineTypes.RecreationalEquipmentType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.LossRatioDiscounts;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PLUWIssues;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.enums.Vehicle.PAComprehensive_CollisionDeductible;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.RecreationalEquipment;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_LossRatios;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement :US5667,US7370, US13673
 * @RequirementsLink <a href=
 * "http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Squire/PC8%20-%20Squire%20-%20QuoteApplication%20-%20Loss%20Ratio.xlsx">
 * PC8 - Squire - QuoteApplication - Loss Ratio</a>
 * @Description : Validating the Loss Ratio - All the tabs default and 0% values
 * @DATE Mar 30, 2016
 */
public class TestSquireRiskAnalysisLossRatio extends BaseTest {
    private GeneratePolicy myPolicyObjPL = null;
    private Underwriters uw;
    private WebDriver driver;

    @Test()
    public void testCreateSquireFA() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        locationsList.add(new PolicyLocation(locOnePropertyList));

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsFirstLastName("Risk", "Analysis")
                .withSquireEligibility(SquireEligibility.random())
                .withPolOrgType(OrganizationType.Individual)
                .build(GeneratePolicyType.FullApp);
    }

    @Test(dependsOnMethods = {"testCreateSquireFA"})
    public void validateRiskAnalysisLossRatio() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Agents agent = myPolicyObjPL.agentInfo;

        // Login with agent
        new Login(driver).loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), myPolicyObjPL.accountNumber);

        // validate Loss Ratio - Property discount
        validateLossRatioDefaultValues(LossRatioDiscounts.FIFTEENDISCOUNT.getValue());

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();


        // Login with Underwriter supervisor role to edit the loss ratios
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.accountNumber);

        guidewireHelpers.editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis_LossRatios risk = new GenericWorkorderRiskAnalysis_LossRatios(driver);
        risk.clickLossRatioTab();
        risk.clickLossRatioPropertyTab();

        risk.setLossRatioPropertyDiscount(LossRatioDiscounts.TWENTYFIVESURCHARGE);
        risk.sendArbitraryKeys(Keys.TAB);
        risk.setPropertyReasonForChange("Testing purpose");

        risk.clickLossRatioLiabilityTab();
        risk.setLossRatioLiabilityDiscount(LossRatioDiscounts.TWENTYSURCHARGE);
        risk.sendArbitraryKeys(Keys.TAB);
        risk.setLiabilityReasonForChange("Testing purpose");
        //Auto Loss ratio
        risk.clickLossRatioAutoTab();
        risk.setLossRatioAutoDiscount(LossRatioDiscounts.TENSURCHARGE);
        risk.sendArbitraryKeys(Keys.TAB);
        risk.setAutoReasonForChange("Testing purpose");
        //IM Loss ratio
        risk.clickLossRatioInlandMarineTab();
        risk.setLossRatioIMDiscount(LossRatioDiscounts.TWENTYSURCHARGE);
        risk.sendArbitraryKeys(Keys.TAB);
        risk.setIMReasonForChange("Testing purpose");
        risk.Quote();
        sideMenu.clickSideMenuQuote();
    }


    private void validateLossRatioDefaultValues(String defaultValue) {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        boolean testFailed = false;
        String errorMessage = "";

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis_LossRatios risk = new GenericWorkorderRiskAnalysis_LossRatios(driver);
        risk.clickLossRatioTab();
        risk.clickLossRatioPropertyTab();
        if (!risk.getPropertyDefaultDiscountAmount().contains(defaultValue)) {
            testFailed = true;
            errorMessage = errorMessage + "Expected default Loss Ratio for Property : 15% discount is not displayed.";
        }

        risk.clickLossRatioLiabilityTab();
        if (!risk.getLiabilityDefaultDiscountAmount().contains(defaultValue)) {
            testFailed = true;
            errorMessage = errorMessage + "Expected default Loss Ratio for Liability : 15% discount is not displayed.";
        }

        risk.clickLossRatioAutoTab();
        if (!risk.getAutoDefaultDiscountAmount().contains(defaultValue)) {
            testFailed = true;
            errorMessage = errorMessage + "Expected default Loss Ratio for Auto : 15% discount is not displayed.";
        }

        risk.clickLossRatioInlandMarineTab();
        if (!risk.getInlandMarineDefaultDiscountAmount().contains(defaultValue)) {
            testFailed = true;
            errorMessage = errorMessage + "Expected default Loss Ratio for IM : 15% discount is not displayed.";
        }

        if (testFailed)
            Assert.fail(driver.getCurrentUrl() + errorMessage);
    }

    // US7370: [Part II] PL- Add 0% Loss Ratio for Auto
    // Change LR from 15% to 0% and verify UW Issue created
    @Test()
    public void validateRiskAnalysisLossRatioByAgent() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locationsList.add(locToAdd);

        // Rec Equip
        ArrayList<RecreationalEquipment> recVehicle = new ArrayList<RecreationalEquipment>();
        recVehicle.add(new RecreationalEquipment(RecreationalEquipmentType.AllTerrainVehicle, "500", PAComprehensive_CollisionDeductible.Fifty50, "Brett Hiltbrand"));

        SquireLiability liability = new SquireLiability();
        ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
        imTypes.add(InlandMarine.RecreationalEquipment);

        ArrayList<Contact> driversList = new ArrayList<Contact>();
        Contact person = new Contact();
        driversList.add(person);
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle vehicle = new Vehicle(VehicleTypePL.PrivatePassenger, "make new vin", 2003, "Oldie", "But Goodie");
        vehicleList.add(vehicle);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow, MedicalLimit.TenK);
        coverages.setUnderinsured(false);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liability;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;
        myInlandMarine.recEquipment_PL_IM = recVehicle;

        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.squirePA = new SquirePersonalAuto();
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;

        GeneratePolicy myNewPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL, LineSelection.InlandMarineLinePL)
                .withInsFirstLastName("SQ", "LossRatio")
                .build(GeneratePolicyType.FullApp);

        Agents agent = myNewPolicyObjPL.agentInfo;
        new GuidewireHelpers(driver).logout();

        // Login with agent
        new Login(driver).loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), myNewPolicyObjPL.accountNumber);

        // validate default values
        validateLossRatioDefaultValues(LossRatioDiscounts.FIFTEENDISCOUNT.getValue());

        validateLossRatioZeroDiscountAgent(myNewPolicyObjPL);
    }

    // change to Zero as a Agent and validate UW Issue
    private void validateLossRatioZeroDiscountAgent(GeneratePolicy myNewPolicyObjPL) throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuRiskAnalysis();
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();
        GenericWorkorderRiskAnalysis_LossRatios risk = new GenericWorkorderRiskAnalysis_LossRatios(driver);
        risk.clickLossRatioTab();

        //property loss ratio zero and validate UW Issue
        risk.clickLossRatioPropertyTab();
        risk.setLossRatioPropertyDiscount(LossRatioDiscounts.ZERODISCOUNT);
        guidewireHelpers.sendArbitraryKeys(Keys.TAB);
        risk.setPropertyReasonForChange("Testing purpose");
        checkAgentLossRatioUWIssue();
        risk.clickLossRatioTab();
        risk.clickLossRatioPropertyTab();
        risk.setLossRatioPropertyDiscount(LossRatioDiscounts.FIFTEENDISCOUNT);

        //Liability loss ratio zero and validate UW Issue
        risk.clickLossRatioLiabilityTab();
        risk.setLossRatioLiabilityDiscount(LossRatioDiscounts.ZERODISCOUNT);
        guidewireHelpers.sendArbitraryKeys(Keys.TAB);
        risk.setLiabilityReasonForChange("Testing purpose");
        checkAgentLossRatioUWIssue();
        risk.clickLossRatioTab();
        risk.clickLossRatioLiabilityTab();
        risk.setLossRatioLiabilityDiscount(LossRatioDiscounts.FIFTEENDISCOUNT);
        //Auto Loss ratio
        risk.clickLossRatioAutoTab();
        risk.setLossRatioAutoDiscount(LossRatioDiscounts.ZERODISCOUNT);
        guidewireHelpers.sendArbitraryKeys(Keys.TAB);
        risk.setAutoReasonForChange("Testing purpose");
        checkAgentLossRatioUWIssue();
        risk.clickLossRatioTab();
        risk.clickLossRatioAutoTab();
        risk.setLossRatioAutoDiscount(LossRatioDiscounts.FIFTEENDISCOUNT);
        //IM Loss ratio
        risk.clickLossRatioInlandMarineTab();
        risk.setLossRatioIMDiscount(LossRatioDiscounts.ZERODISCOUNT);
        guidewireHelpers.sendArbitraryKeys(Keys.TAB);
        risk.setIMReasonForChange("Testing purpose");
        checkAgentLossRatioUWIssue();

        risk.Quote();
        sideMenu.clickSideMenuQuote();
    }

    private void checkAgentLossRatioUWIssue() {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();
        sideMenu.clickSideMenuRiskAnalysis();
        risk.clickUWIssuesTab();
        FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();
        if (!uwIssues.isInList(PLUWIssues.AgentLossRatioChange.getLongDesc()).equals(UnderwriterIssueType.BlockSubmit)) {
            Assert.fail("Expected UW Issue : Agent Loss Ratio Change: is not displayed");
        }
        new GuidewireHelpers(driver).editPolicyTransaction();
    }



}

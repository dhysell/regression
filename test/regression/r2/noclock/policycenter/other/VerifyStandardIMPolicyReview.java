package regression.r2.noclock.policycenter.other;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.CoverageType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PersonalPropertyDeductible;
import repository.gw.enums.PersonalPropertyType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.PersonalProperty;
import repository.gw.generate.custom.PersonalPropertyList;
import repository.gw.generate.custom.PersonalPropertyScheduledItem;
import repository.gw.generate.custom.StandardInlandMarine;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyReviewPL;
import persistence.globaldatarepo.entities.Agents;

/**
 * @Author chofman (from quarantine)
 * @Requirement
 * @RequirementsLink
 * @Description This creates two types of Std Inland Marine policies (one test for farm equip and one test for personal prop) then verifies the policy review page contains the values expected
 * @DATE May 15, 2017
 */
public class VerifyStandardIMPolicyReview extends BaseTest {

    private GeneratePolicy farmEquipment = null;
    private GeneratePolicy personalProperty = null;

    private WebDriver driver;

    @Test()
    public void testCreateStandardInlandMarine_FarmEquipment() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<InlandMarine> inlandMarineCoverageSelection_PL_IM = new ArrayList<InlandMarine>();
        inlandMarineCoverageSelection_PL_IM.add(InlandMarine.FarmEquipment);

        // FPP
        // Scheduled Item for 1st FPP
        IMFarmEquipmentScheduledItem scheduledItem1 = new IMFarmEquipmentScheduledItem("Circle Sprinkler",
                "Manly Farm Equipment", 1000);
        IMFarmEquipmentScheduledItem scheduledItem2 = new IMFarmEquipmentScheduledItem("Circle Sprinkler",
                "Manly Farm Equipment", 1000);
        ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
        farmEquip.add(scheduledItem1);
        farmEquip.add(scheduledItem2);
        FarmEquipment imFarmEquip1 = new FarmEquipment(IMFarmEquipmentType.CircleSprinkler, CoverageType.BroadForm,
                IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);

        // Scheduled Items for 2nd FPP
        IMFarmEquipmentScheduledItem scheduled2Item1 = new IMFarmEquipmentScheduledItem("Farm Equipment",
                "Manly Farm Equipment", 1000);
        IMFarmEquipmentScheduledItem scheduled2Item2 = new IMFarmEquipmentScheduledItem("Farm Equipment",
                "Manly Farm Equipment", 1000);
        ArrayList<IMFarmEquipmentScheduledItem> farmEquip2 = new ArrayList<IMFarmEquipmentScheduledItem>();
        farmEquip2.add(scheduled2Item1);
        farmEquip2.add(scheduled2Item2);
        FarmEquipment imFarmEquip2 = new FarmEquipment(IMFarmEquipmentType.FarmEquipment, CoverageType.BroadForm,
                IMFarmEquipmentDeductible.TwoHundredFifty, true, false, "Cor Hofman", farmEquip2);

        // No Scheduled Item for 3rd FPP
        IMFarmEquipmentScheduledItem scheduled3Item1 = new IMFarmEquipmentScheduledItem("Movable SetSprinkler",
                "Manly Farm Equipment", 1000);
        ArrayList<IMFarmEquipmentScheduledItem> farmEquip3 = new ArrayList<IMFarmEquipmentScheduledItem>();
        farmEquip3.add(scheduled3Item1);
        FarmEquipment imFarmEquip3 = new FarmEquipment(IMFarmEquipmentType.MovableSetSprinkler, CoverageType.BroadForm,
                IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip3);

        // One Scheduled Item for 4th FPP
        IMFarmEquipmentScheduledItem scheduled4Item1 = new IMFarmEquipmentScheduledItem("Wheel Sprinkler",
                "Manly Farm Equipment", 1000);
        ArrayList<IMFarmEquipmentScheduledItem> farmEquip4 = new ArrayList<IMFarmEquipmentScheduledItem>();
        farmEquip4.add(scheduled4Item1);
        FarmEquipment imFarmEquip4 = new FarmEquipment(IMFarmEquipmentType.WheelSprinkler, CoverageType.BroadForm,
                IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip4);

        // No Sceduled Item for 5th FPP
        IMFarmEquipmentScheduledItem scheduled5Item1 = new IMFarmEquipmentScheduledItem("Pumps panels Motors",
                "Manly Farm Equipment", 1000);
        ArrayList<IMFarmEquipmentScheduledItem> farmEquip5 = new ArrayList<IMFarmEquipmentScheduledItem>();
        farmEquip5.add(scheduled5Item1);
        FarmEquipment imFarmEquip5 = new FarmEquipment(IMFarmEquipmentType.PumpsPanelsMotors, CoverageType.BroadForm,
                IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip5);

        ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
        allFarmEquip.add(imFarmEquip1);
        allFarmEquip.add(imFarmEquip2);
        allFarmEquip.add(imFarmEquip3);
        allFarmEquip.add(imFarmEquip4);
        allFarmEquip.add(imFarmEquip5);

        StandardInlandMarine myStandardInlandMarine = new StandardInlandMarine();
        myStandardInlandMarine.inlandMarineCoverageSelection_Standard_IM = inlandMarineCoverageSelection_PL_IM;
        myStandardInlandMarine.farmEquipment = allFarmEquip;

        farmEquipment = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardIM)
                .withLineSelection(LineSelection.StandardInlandMarine)
                .withStandardInlandMarine(myStandardInlandMarine)
                .withInsFirstLastName("farm", "equipment")
                .build(GeneratePolicyType.FullApp);

        Agents agent = farmEquipment.agentInfo;

        new Login(driver).loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), farmEquipment.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);

        sideMenu.clickSideMenuRiskAnalysis();
        sideMenu.clickSideMenuPolicyReview();

        GenericWorkorderPolicyReviewPL policyReviewPage = new GenericWorkorderPolicyReviewPL(driver);

        validateIMPolicyReviewFarmEquipment(policyReviewPage, farmEquipment.standardInlandMarine.farmEquipment.get(0));
    }

    @Test()
    public void testCreateStandardInlandMarine_PersonalProperty() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<InlandMarine> inlandMarineCoverageSelection_PL_IM = new ArrayList<InlandMarine>();
        inlandMarineCoverageSelection_PL_IM.add(InlandMarine.PersonalProperty);

        // PersonalProperty
        PersonalProperty ppropTool = new PersonalProperty();
        ppropTool.setType(PersonalPropertyType.Tools);
        ppropTool.setDeductible(PersonalPropertyDeductible.Ded25);
        PersonalPropertyScheduledItem tool = new PersonalPropertyScheduledItem();
        tool.setDescription("Big Tool");
        tool.setLimit(5000);

        ArrayList<PersonalPropertyScheduledItem> tools = new ArrayList<PersonalPropertyScheduledItem>();
        tools.add(tool);

        ppropTool.setScheduledItems(tools);
        ArrayList<String> ppropAdditIns = new ArrayList<String>();
        ppropAdditIns.add("First Guy");
        ppropAdditIns.add("Second Guy");
        ppropTool.setAdditionalInsureds(ppropAdditIns);

        PersonalPropertyList ppropList = new PersonalPropertyList();
        ppropList.setTools(ppropTool);
        StandardInlandMarine myStandardInlandMarine = new StandardInlandMarine();
        myStandardInlandMarine.inlandMarineCoverageSelection_Standard_IM = inlandMarineCoverageSelection_PL_IM;
        myStandardInlandMarine.personalProperty_PL_IM = ppropList.getAllPersonalPropertyAsList();


        personalProperty = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardIM)
                .withLineSelection(LineSelection.StandardInlandMarine)
                .withStandardInlandMarine(myStandardInlandMarine)
                .withInsFirstLastName("personal", "property")
                .build(GeneratePolicyType.FullApp);

        Agents agent = personalProperty.agentInfo;

        new Login(driver).loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), personalProperty.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);

        sideMenu.clickSideMenuRiskAnalysis();
        sideMenu.clickSideMenuPolicyReview();

        GenericWorkorderPolicyReviewPL policyReviewPage = new GenericWorkorderPolicyReviewPL(driver);

        System.out.println("personalProperty.personalProperty_PL_IM" + personalProperty.standardInlandMarine.personalProperty_PL_IM);
        PersonalProperty selectedPP = null;

        for (PersonalProperty pp : personalProperty.standardInlandMarine.personalProperty_PL_IM) {
            if (pp != null) {
                selectedPP = pp;
                break;
            }
        }
        validateIMPolicyReviewPersonalProperty(policyReviewPage, selectedPP);

    }

    private void validateIMPolicyReviewPersonalProperty(GenericWorkorderPolicyReviewPL policyReview, PersonalProperty personalProperty) {
        boolean testFailed = false;
        String errorMessage = "";
        policyReview.clickPersonalPropertyTab();

        if ("Tools".equals(policyReview.getPersonalPropertyTableCellByColumnName(1, "Type")))
            System.out.println("Expected Personal Property Type : Tools is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected Personal Property Type : Tools is not displayed.";
        }

        if (personalProperty.getDeductible().getValue().contains(policyReview.getPersonalPropertyTableCellByColumnName(1, "Deductible")))
            System.out.println("Expected Personal Property Deductible : " + personalProperty.getDeductible().getValue() + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected Personal Property Deductible : " + personalProperty.getDeductible().getValue() + " is not displayed.";
        }
        if (testFailed)
            Assert.fail(errorMessage);
    }

    private void validateIMPolicyReviewFarmEquipment(GenericWorkorderPolicyReviewPL policyReview, FarmEquipment farmThing) {
        boolean testFailed = false;
        String errorMessage = "";
        policyReview.clickFarmEquipmentTab();

        if (farmThing.getIMFarmEquipmentType().getValue().contains(policyReview.getFarmEquipmentTableCellByColumnName(1, "Type"))) {
            System.out.println("Expected farm equipment deductible " + farmThing.getDeductible().getValue() + " is displayed.");
        } else {
            testFailed = true;
            errorMessage = errorMessage + "Expected farm equipment deductible " + farmThing.getDeductible().getValue() + " is not displayed.";
        }
        if (farmThing.getDeductible().getValue().contains(policyReview.getFarmEquipmentTableCellByColumnName(1, "Deductible")))
            System.out.println("Expected farm equipment deductible " + farmThing.getDeductible().getValue() + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected farm equipment deductible " + farmThing.getDeductible().getValue() + " is not displayed.";
        }

        double farmEquipmentLimit = farmThing.getScheduledFarmEquipment().get(0).getLimit();
        double farmEquipmentScheduleLimit = Double.parseDouble(policyReview.getFarmEquipmentVehicleTableCellByColumnName(1, "Limit"));

        if (farmEquipmentLimit - farmEquipmentScheduleLimit == 0)
            System.out.println("Expected farm Equipment Limit : " + farmEquipmentLimit + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected farm Equipment Limit : " + farmEquipmentLimit + " is not displayed.";
        }

        if ((farmThing.getScheduledFarmEquipment().get(0).getYear() + " " + farmThing.getScheduledFarmEquipment().get(0).getMake()).contains(policyReview.getFarmEquipmentVehicleTableCellByColumnName(1, "Year/Make")))
            System.out.println("Expected farm Equipment Year Make: " + farmThing.getScheduledFarmEquipment().get(0).getYear() + " " + farmThing.getScheduledFarmEquipment().get(0).getMake() + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected farm Equipment Year Make: " + farmThing.getScheduledFarmEquipment().get(0).getYear() + " " + farmThing.getScheduledFarmEquipment().get(0).getMake() + " is not displayed.";
        }

        if (farmThing.getScheduledFarmEquipment().get(0).getTypeOfEquipment().contains(policyReview.getFarmEquipmentVehicleTableCellByColumnName(1, "Type of Equipment")))
            System.out.println("Expected farm Equipment Schedule type: " + farmThing.getScheduledFarmEquipment().get(0).getTypeOfEquipment() + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected farm Equipment Schedule type: " + farmThing.getScheduledFarmEquipment().get(0).getTypeOfEquipment() + " is not displayed.";
        }

        if (farmThing.getScheduledFarmEquipment().get(0).getVin().contains(policyReview.getFarmEquipmentVehicleTableCellByColumnName(1, "VIN/Serial #")))
            System.out.println("Expected farm Equipment VIN: " + farmThing.getScheduledFarmEquipment().get(0).getVin() + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected farm Equipment VIN: " + farmThing.getScheduledFarmEquipment().get(0).getVin() + " is not displayed.";
        }
        if (testFailed)
            Assert.fail(errorMessage);
    }
}

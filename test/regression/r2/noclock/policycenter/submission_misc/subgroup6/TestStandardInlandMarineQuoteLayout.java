package regression.r2.noclock.policycenter.submission_misc.subgroup6;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.CoverageType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
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
import repository.pc.workorders.generic.GenericWorkorderQuote;

public class TestStandardInlandMarineQuoteLayout extends BaseTest {
    private GeneratePolicy inlandMarinePolicyObjPL;

    private WebDriver driver;

    //Quote Layout validations for Standard InlandMarine
    @Test(dependsOnMethods = {"testCreateInlandMarinePolicy"})
    public void testValidateQuoteSectionIVPremium() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(inlandMarinePolicyObjPL.agentInfo.getAgentUserName(), inlandMarinePolicyObjPL.agentInfo.getAgentPassword(), inlandMarinePolicyObjPL.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuQuote();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        boolean testFailed = false;
        String errorMessage = "";

        if (quote.getSectionIVInlandMarineSummaryRowCount() < inlandMarinePolicyObjPL.squire.inlandMarine.inlandMarineCoverageSelection_PL_IM.size()) {
            testFailed = true;
            errorMessage = errorMessage + "Expected Section IV Inland Marine - Count " + inlandMarinePolicyObjPL.squire.inlandMarine.inlandMarineCoverageSelection_PL_IM.size() + " is not matched \n";
        }

        for (int currentRow = 1; currentRow < quote.getSectionIVInlandMarineSummaryRowCount(); currentRow++) {
            quote.clickLinkSectionIVInlandMarinesummaryLinkByRow(currentRow);
            quote.clickReturnToQuote();
        }

        //Policy Premium
        if (quote.getQuoteTotalGrossPremium() != (quote.getStandardInlandMarinePremium(InlandMarine.FarmEquipment.getValue()) + quote.getStandardInlandMarinePremium(InlandMarine.PersonalProperty.getValue()))) {
            testFailed = true;
            errorMessage = errorMessage + "Total Gross Policy Premium : " + quote.getQuoteTotalGrossPremium() + " is not displayed correctly. \n";
        }

        //Farm Equipment
        quote.clickStandardIMFarmEquipment();

        for (int currentFarm = 1; currentFarm <= quote.getSectionIVFarmEquipmentRowCount(); currentFarm++) {
            quote.clickSectionIVFarmEquipmentDetailsLinkByRow(currentFarm);
            quote.clickReturnToQuote();
        }

        boolean farmFound = false;
        for (FarmEquipment farmEquipment : inlandMarinePolicyObjPL.squire.inlandMarine.farmEquipment) {
            farmFound = false;
            for (int currentFarm = 1; currentFarm <= quote.getSectionIVFarmEquipmentRowCount(); currentFarm++) {
                if (farmEquipment.getIMFarmEquipmentType().getValue().contains(quote.getSectionIVFarmEquipmentDetails(currentFarm, "Type")) && farmEquipment.getDeductible().getValue().equals(quote.getSectionIVFarmEquipmentDetails(currentFarm, "Deductible"))) {
                    farmFound = true;
                    break;
                }
            }

            if (!farmFound) {
                testFailed = true;
                errorMessage = errorMessage + "Expected Farm Equipment: " + farmEquipment.getIMFarmEquipmentType().getValue() + " is not displayed. \n";
            }
        }


        //Personal Property
        quote.clickStandardIMPersonalProperty();
        for (int currentPP = 1; currentPP <= quote.getSectionIVPersonalPropertyRowCount(); currentPP++) {
            quote.clickSectionIVPersonalPropertyLinkByRow(currentPP);
            quote.clickReturnToQuote();
        }

        boolean ppFound = false;
        for (PersonalProperty pp : inlandMarinePolicyObjPL.squire.inlandMarine.personalProperty_PL_IM) {
            ppFound = false;
            if (pp != null) {
                for (int currentPP = 1; currentPP <= quote.getSectionIVPersonalPropertyRowCount(); currentPP++) {
                    if ((quote.getSectionIVPersonalPropertyDetails(currentPP, "Type").contains(pp.getType().getValue()) && (Integer.parseInt(quote.getSectionIVPersonalPropertyDetails(currentPP, "Limit"))) == pp.getScheduledItems().get(0).getLimit())) {
                        ppFound = true;
                        break;
                    }
                }
                if (!ppFound) {
                    testFailed = false;
                    errorMessage = "Expected Personal property " + pp.getType().getValue() + " is not displayed. \n";
                }
            }
        }

        //DE4260: Standard Liability -- Premium Summary Missing on Quote Page
        // Premium Summary table discount
        if (quote.getQuoteTotalDiscountsSurcharges() != (quote.getSquirePremiumSummaryAmount("Section IV", false, false, true))) {
            testFailed = true;
            errorMessage = errorMessage + "Total discount : " + quote.getQuoteTotalDiscountsSurcharges() + " is not displayed correctly. \n";
        }


        //Premium Summary table Net premium
        if (quote.getQuoteTotalGrossPremium() != (quote.getSquirePremiumSummaryAmount("Section IV", false, true, false))) {
            testFailed = true;
            errorMessage = errorMessage + "Total Net Policy Premium : " + quote.getQuoteTotalNetPremium() + " is not displayed correctly. \n";
        }

        //Premium Summary table Gross premium
        if (quote.getQuoteTotalGrossPremium() != (quote.getSquirePremiumSummaryAmount("Section IV", true, false, false))) {
            testFailed = true;
            errorMessage = errorMessage + "Total Gross Policy Premium : " + quote.getQuoteTotalGrossPremium() + " is not displayed correctly. \n";
        }

        if (testFailed)
            Assert.fail(errorMessage);

    }

    @Test()
    public void testCreateInlandMarinePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<InlandMarine> inlandMarineCoverageSelection_PL_IM = new ArrayList<InlandMarine>();
        inlandMarineCoverageSelection_PL_IM.add(InlandMarine.FarmEquipment);
        inlandMarineCoverageSelection_PL_IM.add(InlandMarine.PersonalProperty);

        // FPP
        //Scheduled Item for 1st FPP
        IMFarmEquipmentScheduledItem scheduledItem1 = new IMFarmEquipmentScheduledItem("Circle Sprinkler", "Manly Farm Equipment", 1000);
        ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
        farmEquip.add(scheduledItem1);
        FarmEquipment imFarmEquip1 = new FarmEquipment(IMFarmEquipmentType.CircleSprinkler, CoverageType.BroadForm, IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);

        ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
        allFarmEquip.add(imFarmEquip1);

        // PersonalProperty
        PersonalProperty ppropTool = new PersonalProperty();
        ppropTool.setType(PersonalPropertyType.Tools);
        ppropTool.setDeductible(PersonalPropertyDeductible.Ded100);
        PersonalPropertyScheduledItem tool = new PersonalPropertyScheduledItem();
        tool.setDescription("Big Tool");
        tool.setLimit(50000);

        ArrayList<PersonalPropertyScheduledItem> tools = new ArrayList<PersonalPropertyScheduledItem>();
        tools.add(tool);

        ppropTool.setScheduledItems(tools);
        ArrayList<String> ppropAdditIns = new ArrayList<String>();
        ppropAdditIns.add("First Guy");
        ppropTool.setAdditionalInsureds(ppropAdditIns);
        PersonalPropertyList ppropList = new PersonalPropertyList();
        ppropList.setTools(ppropTool);

        StandardInlandMarine myStandardInlandMarine = new StandardInlandMarine();
        myStandardInlandMarine.inlandMarineCoverageSelection_Standard_IM = inlandMarineCoverageSelection_PL_IM;
        myStandardInlandMarine.farmEquipment = allFarmEquip;
        myStandardInlandMarine.personalProperty_PL_IM = ppropList.getAllPersonalPropertyAsList();

        inlandMarinePolicyObjPL = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardIM)
                .withStandardInlandMarine(myStandardInlandMarine)
                .withInsFirstLastName("farm", "equipment")
                .build(GeneratePolicyType.QuickQuote);
    }

}
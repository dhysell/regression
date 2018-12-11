package regression.r2.noclock.policycenter.submission_misc.subgroup4;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import repository.gw.enums.FPP;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.FPP.FarmPersonalPropertyTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.LineSelection;
import repository.gw.enums.LivestockDeductible;
import repository.gw.enums.LivestockScheduledItemType;
import repository.gw.enums.LivestockType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.errorhandling.ErrorHandling;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Livestock;
import repository.gw.generate.custom.LivestockList;
import repository.gw.generate.custom.LivestockScheduledItem;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquireLiablityCoverageLivestockItem;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages;

public class TestSquireLivestockSectionsMatch extends BaseTest {

    GeneratePolicy quickQuoteLivestock;
    private WebDriver driver;

    @BeforeMethod
    public void beforeMethod() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
    }



    /**
     * @throws Exception
     * @Author Steve Broderick
     * @Description : Test is already available and just adding comment. This test is for adding Farm Equipment, Cargo
     * @DATE Apr 12, 2016
     */

    public void testSquireInlandMarine_QuickQuote() throws Exception {


        SquireLiability liability = new SquireLiability();
        liability.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        SquireLiablityCoverageLivestockItem livestockSectionIIHorseCoverage = new SquireLiablityCoverageLivestockItem();
        livestockSectionIIHorseCoverage.setQuantity(1);
        livestockSectionIIHorseCoverage.setType(LivestockScheduledItemType.Horse);

        SquireLiablityCoverageLivestockItem livestockSectionIICowCoverage = new SquireLiablityCoverageLivestockItem();
        livestockSectionIICowCoverage.setQuantity(1);
        livestockSectionIICowCoverage.setType(LivestockScheduledItemType.Cow);

        ArrayList<SquireLiablityCoverageLivestockItem> coveredLivestockItems = new ArrayList<SquireLiablityCoverageLivestockItem>();
        coveredLivestockItems.add(livestockSectionIICowCoverage);
        coveredLivestockItems.add(livestockSectionIIHorseCoverage);

        SectionIICoverages livestockcoverage = new SectionIICoverages(SectionIICoveragesEnum.Livestock, coveredLivestockItems);

        liability.getSectionIICoverageList().add(livestockcoverage);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locationsList.add(locToAdd);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        // Livestock
        Livestock livestock = new Livestock();
        livestock.setType(LivestockType.Livestock);
        livestock.setDeductible(LivestockDeductible.Ded100);
        LivestockScheduledItem livSchedItem1 = new LivestockScheduledItem(livestock.getType(), LivestockScheduledItemType.Alpaca, "Test Alpaca 1", 1000);
        LivestockScheduledItem livSchedItem2 = new LivestockScheduledItem(livestock.getType(), LivestockScheduledItemType.Cow, "Test BullCow 1", "TAG123", "Brand1", "Breed1", 5000);
        ArrayList<LivestockScheduledItem> livSchedItems = new ArrayList<LivestockScheduledItem>();
        livSchedItems.add(livSchedItem1);
        livSchedItems.add(livSchedItem2);
        livestock.setScheduledItems(livSchedItems);

        LivestockList allLivestock = new LivestockList(livestock, null, null);

        ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
        imTypes.add(InlandMarine.Livestock);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liability;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;
        myInlandMarine.livestock_PL_IM = allLivestock.getAllLivestockAsList();

        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;

        this.quickQuoteLivestock = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withInsFirstLastName("Livestock", "Mustmatch")
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.InlandMarineLinePL, LineSelection.PropertyAndLiabilityLinePL)
                .build(GeneratePolicyType.QuickQuote);

    }

    @Test
    public void horsesMustMatch() throws Exception {
        testSquireInlandMarine_QuickQuote();
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();
        new Login(driver).loginAndSearchJob(quickQuoteLivestock.agentInfo.getAgentUserName(), quickQuoteLivestock.agentInfo.getAgentPassword(), quickQuoteLivestock.accountNumber);

        GenericWorkorder wo = new GenericWorkorder(driver);
        guidewireHelpers.editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyCoverages();

        GenericWorkorderSquirePropertyAndLiabilityCoverages propertyCoverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        propertyCoverages.clickFarmPersonalProperty();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages fppCoveragesPage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages(driver);
        fppCoveragesPage.checkCoverageD(true);
        fppCoveragesPage.selectCoverageType(FPP.FPPCoverageTypes.BlanketInclude);
        fppCoveragesPage.selectDeductible(FPP.FPPDeductible.Ded_500);
        fppCoveragesPage.selectCoverages(FarmPersonalPropertyTypes.Livestock);
        fppCoveragesPage.addItem(FPPFarmPersonalPropertySubTypes.Alpacas, 2, 2000, "Don't eat this.");

        wo = new GenericWorkorder(driver);
        wo.clickGenericWorkorderQuote();

        ErrorHandling errorHandling = new ErrorHandling(driver);
        List<WebElement> errors = errorHandling.getValidationMessages();
        boolean found = false;
        for (WebElement error : errors) {
            if (error.getText().contains("The horses quantity on Section II is less than the sum of horses, mules, donkeys, llamas, and/or alpacas on FPP and/or Section IV. (PGL017)")) {
                found = true;
            }
        }

        if (!found) {
            Assert.fail(driver.getCurrentUrl() + quickQuoteLivestock.accountNumber + "The quantity of horses on FPP and IM must be less than or equal to the livestock quantity on section II");
        }

        fppCoveragesPage.checkCoverageD(false);
        wo = new GenericWorkorder(driver);
        wo.clickGenericWorkorderQuote();
    }

    @Test(dependsOnMethods = {"horsesMustMatch"})
    public void bullCowMustMatch() throws Exception {
        new Login(driver).loginAndSearchJob(quickQuoteLivestock.agentInfo.getAgentUserName(), quickQuoteLivestock.agentInfo.getAgentPassword(), quickQuoteLivestock.accountNumber);
        GenericWorkorder wo = new GenericWorkorder(driver);
        new GuidewireHelpers(driver).editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyCoverages();

        GenericWorkorderSquirePropertyAndLiabilityCoverages propertyCoverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        propertyCoverages.clickFarmPersonalProperty();

        GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages fppCoveragesPage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages(driver);
        fppCoveragesPage.checkCoverageD(true);
        fppCoveragesPage.selectCoverageType(FPP.FPPCoverageTypes.BlanketInclude);
        fppCoveragesPage.selectDeductible(FPP.FPPDeductible.Ded_500);
        fppCoveragesPage.selectCoverages(FarmPersonalPropertyTypes.Livestock);
        fppCoveragesPage.addItem(FPPFarmPersonalPropertySubTypes.Steers, 2, 2000, "Yummy, tastes like Chicken.");

        wo = new GenericWorkorder(driver);
        wo.clickGenericWorkorderQuote();

        ErrorHandling errorHandling = new ErrorHandling(driver);
        List<WebElement> errors = errorHandling.getValidationMessages();
        boolean found = false;
        for (WebElement error : errors) {
            if (error.getText().contains("The cows quantity on Section II is less than the sum of bulls, cows, steers, heifers, and/or calves on FPP and/or Section IV. (PGL018)")) {
                found = true;
            }
        }

        if (!found) {
            Assert.fail(driver.getCurrentUrl() + quickQuoteLivestock.accountNumber + "The cows quantity on Section II is less than the sum of bulls, cows, steers, heifers, and/or calves on FPP and/or Section IV. (PGL018)");
        }

        fppCoveragesPage.checkCoverageD(false);
        wo = new GenericWorkorder(driver);
        wo.clickGenericWorkorderQuote();
    }

}


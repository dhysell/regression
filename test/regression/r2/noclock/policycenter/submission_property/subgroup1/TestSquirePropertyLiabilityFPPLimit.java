package regression.r2.noclock.policycenter.submission_property.subgroup1;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.FPP.FarmPersonalPropertyTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireFPP;
import repository.gw.generate.custom.SquireFPPTypeItem;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderSquireFPPAdditionalInterest;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages;

/**
 * @Author nvadlamudi
 * @Requirement :US9353 :Create a new limit field for Coverage D
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Homeowners/PC8%20-%20HO%20-%20QuoteApplication%20-%20Farm%20Personal%20Property.xlsx"></a>
 * @Description
 * @DATE Oct 7, 2016
 */
public class TestSquirePropertyLiabilityFPPLimit extends BaseTest {

    private GeneratePolicy myCountryPolicyObjPL;
    private GeneratePolicy myFRPolicyObjPL;
    public SoftAssert softAssert = new SoftAssert();
    private WebDriver driver;

    @Test()
    public void testFPPCountrySquirePol() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();

        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));

        PolicyLocation resPrem = new PolicyLocation(locOnePropertyList);
        resPrem.setPlNumAcres(11);
        resPrem.setPlNumResidence(5);

        locationsList.add(resPrem);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquireFPP squireFPP = new SquireFPP(FPPFarmPersonalPropertySubTypes.Tractors, FPPFarmPersonalPropertySubTypes.HarvestersHeaders, FPPFarmPersonalPropertySubTypes.CirclePivots, FPPFarmPersonalPropertySubTypes.WheelLines, FPPFarmPersonalPropertySubTypes.HandTools, FPPFarmPersonalPropertySubTypes.Seed, FPPFarmPersonalPropertySubTypes.Straw, FPPFarmPersonalPropertySubTypes.SpareTruckParts);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;
        myPropertyAndLiability.squireFPP = squireFPP;

        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myCountryPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Country", "FPPLimit")
                .build(GeneratePolicyType.QuickQuote);

        validateFPPLimitField(myCountryPolicyObjPL);
    }


    private void validateFPPLimitField(GeneratePolicy pol) throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(pol.agentInfo.agentUserName, pol.agentInfo.getAgentPassword(), pol.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        coverages.clickFarmPersonalProperty();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages fppCovs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages(driver);
        int totalLimitValues = fppCovs.getTotalValueByType(FarmPersonalPropertyTypes.Commodities) +
                fppCovs.getTotalValueByType(FarmPersonalPropertyTypes.Tools) +
                fppCovs.getTotalValueByType(FarmPersonalPropertyTypes.IrrigationEquipment) +
                fppCovs.getTotalValueByType(FarmPersonalPropertyTypes.Machinery) +
                fppCovs.getTotalValueByType(FarmPersonalPropertyTypes.SpareTruckParts);

        softAssert.assertTrue(totalLimitValues == fppCovs.getCoverageDLimit(), "Total Coverage D Values are not matched with Coverages added : " + totalLimitValues);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();
        sideMenu.clickSideMenuSquirePropertyCoverages();
        coverages.clickFarmPersonalProperty();
        fppCovs.selectCoverages(FarmPersonalPropertyTypes.Livestock);
        fppCovs.addItem(FPPFarmPersonalPropertySubTypes.Cows, 200, 10000, "Testing purpose");

        softAssert.assertTrue((totalLimitValues + 10000) == fppCovs.getCoverageDLimit(), "Total Coverage D Values are not matched with Coverages added : " + (totalLimitValues + 10000));

        softAssert.assertAll();
    }

    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description
     * @DATE Oct 10, 2016
     */
    @Test()
    public void testFPPFarmAndRanchSquirePol() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();

        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));

        PolicyLocation resPrem = new PolicyLocation(locOnePropertyList);
        resPrem.setPlNumAcres(11);
        resPrem.setPlNumResidence(5);

        locationsList.add(resPrem);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        ArrayList<AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
		AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Company, AdditionalInterestType.LessorPL);
        loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);

        SquireFPP squireFPP = new SquireFPP();
        squireFPP.setAdditionalInterests(loc2Bldg1AdditionalInterests);

        SquireFPPTypeItem machinery1 = new SquireFPPTypeItem(squireFPP, FPPFarmPersonalPropertySubTypes.Tractors);
        machinery1.setAdditionalInterest(loc2Bldg1AddInterest);
        machinery1.setDescription("Testing Purpose");
        machinery1.setSerialNumber("13324435435");
        machinery1.setValue(100);

        squireFPP.setItems(machinery1);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;
        myPropertyAndLiability.squireFPP = squireFPP;

        Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myFRPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("FR", "FPPLimit")
                .build(GeneratePolicyType.FullApp);

        new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchSubmission(myFRPolicyObjPL.agentInfo.agentUserName, myFRPolicyObjPL.agentInfo.getAgentPassword(), myFRPolicyObjPL.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        coverages.clickFarmPersonalProperty();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages fppCovs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages(driver);
        fppCovs.clickFPPAdditionalInterests();

        GenericWorkorderSquireFPPAdditionalInterest fppAddInterest = new GenericWorkorderSquireFPPAdditionalInterest(driver);
        String errorMessage = "";
        if (fppAddInterest.getAdditionalInterstRowCount() != this.myFRPolicyObjPL.squire.propertyAndLiability.squireFPP.getAdditionalInterests().size()) {
            errorMessage = "Additional Interest are not removed";
        }

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();
        sideMenu.clickSideMenuSquirePropertyCoverages();
        coverages.clickFarmPersonalProperty();
        fppCovs.clickFPPAdditionalInterests();
        int itemsCount = fppAddInterest.getItemsCountByItemType(FarmPersonalPropertyTypes.Machinery);
        if ((itemsCount - 2) != this.myFRPolicyObjPL.squire.propertyAndLiability.squireFPP.getItems(FarmPersonalPropertyTypes.Machinery).size()) {
            errorMessage = errorMessage + "\n Machinaries with additional interest are removed.";
        }

        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }

        new GuidewireHelpers(driver).logout();
    }


}

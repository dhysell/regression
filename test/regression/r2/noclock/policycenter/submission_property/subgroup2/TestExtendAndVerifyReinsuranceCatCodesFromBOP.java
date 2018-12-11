package regression.r2.noclock.policycenter.submission_property.subgroup2;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.Building.ValuationMethod;
import repository.gw.enums.CoverageType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.FoundationType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
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
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandibanda
 * @Requirement : US7933 - [Part II] Extend and verify reinsurance cat codes from BOP
 * @Description : Script will generate squire residence premises property and then add Dwelling Premises, Barn and
 * Bee Station properties to it, login as UW, add risk A and validate category code based of the property coverage limit
 * when its quoted
 * @DATE Sep 14, 2016
 */
public class TestExtendAndVerifyReinsuranceCatCodesFromBOP extends BaseTest {
    private GeneratePolicy squirePLObject;
    private Underwriters uw;

    private WebDriver driver;

    @Test
    public void createPLPropertyPolicy() throws Exception {
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

        squirePLObject = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Test", "Category")
                .build(GeneratePolicyType.FullApp);
    }


    //validate Risk and Category code
    @Test(dependsOnMethods = {"createPLPropertyPolicy"})
    public void enterValidateRiskCategoryDetails() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Agents agent = squirePLObject.agentInfo;
        new Login(driver).loginAndSearchSubmission(agent.agentUserName, agent.getAgentPassword(), squirePLObject.accountNumber);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);

        guidewireHelpers.editPolicyTransaction();

        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        addNewPropertyCoverageLimit(propertyDetail, PropertyTypePL.DwellingPremises, 150000, CoverageType.BroadForm);
        addNewPropertyCoverageLimit(propertyDetail, PropertyTypePL.Barn, 50000, CoverageType.BroadForm);
        addNewPropertyCoverageLimit(propertyDetail, PropertyTypePL.BeeStation, 25000, CoverageType.BroadForm);

        guidewireHelpers.logout();
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), squirePLObject.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        propertyLocations.clickEditLocation(1);
//        propertyLocations.setNumberOfResidence(5);
        propertyLocations.clickOK();

        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverages.clickSpecificBuilding(1, 1);
        coverages.setCoverageALimit(250000);

        sideMenu.clickSideMenuSquirePropertyDetail();

        propertyDetail.clickViewOrEditBuildingButton(2);
        propertyDetail.setRisk("A");
        propertyDetail.clickOk();

        propertyDetail.clickViewOrEditBuildingButton(3);
        propertyDetail.setRisk("A");
        propertyDetail.clickOk();

        propertyDetail.clickViewOrEditBuildingButton(4);
        propertyDetail.setRisk("A");
        propertyDetail.clickOk();

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();

        //validate Risk and category codes
        sideMenu.clickSideMenuSquirePropertyDetail();

        boolean testFailed = false;
        String errorMessage = " ";

        int properties = propertyDetail.getPropertiesCount();

        for (int i = 1; i < properties; i++) {
            propertyDetail.highLightPropertiesByNumber(i);
            if (!propertyDetail.getReadOnlyRiskValue().contains("A")) {
                testFailed = true;
                errorMessage = "Unexpected Risk Code " + propertyDetail.getReadOnlyRiskValue();
            }
            if (!propertyDetail.getReadOnlyCategoryCode().contains("014")) {
                testFailed = true;
                errorMessage = "Unexpected Category Code " + propertyDetail.getReadOnlyCategoryCode();
            }
        }

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + squirePLObject.accountNumber + errorMessage);
        }
    }

    private void addNewPropertyCoverageLimit(GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail, PropertyTypePL propertyType, double limit, CoverageType cType) throws Exception {

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();


        PLPolicyLocationProperty property = new PLPolicyLocationProperty(propertyType);
        propertyDetail.fillOutPropertyDetails_FA(property);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        if (propertyType.equals(PropertyTypePL.Contents)) {
            constructionPage.setFoundationType(FoundationType.FullBasement);
            constructionPage.setConstructionType(ConstructionTypePL.Frame);
        } else {
            constructionPage.fillOutPropertyConstrustion_FA(property);
            if (!propertyType.equals(PropertyTypePL.Barn) && !propertyType.equals(PropertyTypePL.BeeStation)) {
                constructionPage.setLargeShed(false);
            }
        }

        if (property.getpropertyType().equals(PropertyTypePL.Barn) || property.getpropertyType().equals(PropertyTypePL.BeeStation)) {
            constructionPage.setMeasurement(property.getMeasurement());
        }
        GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);
        if (property.getpropertyType().equals(PropertyTypePL.DwellingPremises)) {
            constructionPage.clickProtectionDetailsTab();

            new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails(driver).fillOutPropertyProtectionDetails(property);
        }
        protectionPage.clickOK();

        int buildingNumber = propertyDetail.getSelectedBuildingNum();
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverages.clickSpecificBuilding(1, buildingNumber);

        if (property.getpropertyType().equals(PropertyTypePL.Barn) || property.getpropertyType().equals(PropertyTypePL.BeeStation)) {
            coverages.setCoverageELimit(limit);
            coverages.setCoverageECoverageType(cType);
            coverages.setCoverageEValuation(ValuationMethod.ReplacementCost);
        } else {
            coverages.setCoverageALimit(limit);
            coverages.setCoverageAIncreasedReplacementCost(property.getPropertyCoverages().getCoverageA().isIncreasedReplacementCost());
            coverages.setCoverageCValuation(property.getPropertyCoverages());
        }
        coverages.clickNext();

    }


}

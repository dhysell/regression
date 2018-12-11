package regression.r2.noclock.policycenter.submission_misc.subgroup5;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.CoverageType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.NumberOfUnits;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.SquireUmbrellaIncreasedLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.SquireUmbrellaInfo;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquireUmbrellaCoverages;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement :US9026: PL - Crop Dusting & Spraying Squire & Umbrella rename
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Aug 31, 2016
 */
@QuarantineClass
public class TestSquireUmbrellaPropertyDetailsChanges extends BaseTest {
    private GeneratePolicy squireFullApp;
    private Underwriters uw;

    private WebDriver driver;


    @Test()
    private void testFullAppSquire() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K, MedicalLimit.TenK);
        coverages.setUninsuredLimit(UninsuredLimit.CSL300K);
        coverages.setUnderinsuredLimit(UnderinsuredLimit.CSL300K);

        SquirePersonalAuto myAuto = new SquirePersonalAuto();
        myAuto.setCoverages(coverages);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locToAdd.setPlNumResidence(6);
        locationsList.add(locToAdd);

        SquireLiability liabilitySection = new SquireLiability();
        liabilitySection.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);
        SectionIICoverages sectionIIcoverage = new SectionIICoverages(SectionIICoveragesEnum.PrivateLandingStrips, 0, 1);
        liabilitySection.getSectionIICoverageList().add(sectionIIcoverage);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;

        Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);
        mySquire.squirePA = myAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        this.squireFullApp = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withInsFirstLastName("QA", "SQProp")
                .withProductType(ProductLineType.Squire)
                .withPolOrgType(OrganizationType.Individual)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
                .build(GeneratePolicyType.FullApp);
    }

    @Test(dependsOnMethods = {"testFullAppSquire"})
    private void testAddingPropertiesSquire() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.squireFullApp.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverages.clickSectionIICoveragesTab();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages section2Covs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
        section2Covs.addCoverages(new SectionIICoverages(SectionIICoveragesEnum.CropDustingAndSpraying, 0, 0));

        //adding Dwelling Under Construction CovE property
        PLPolicyLocationProperty property = new PLPolicyLocationProperty();
        property.setpropertyType(PropertyTypePL.DwellingUnderConstructionCovE);
        property.setDwellingVacant(true);
        property.setNumberOfUnits(NumberOfUnits.FourUnits);

        int yearField = DateUtils.getCenterDateAsInt(driver, ApplicationOrCenter.PolicyCenter, "yyyy");
        property.setYearBuilt(yearField - 6);
        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        propertyDetail.fillOutPropertyDetails_FA(property);

        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        constructionPage.fillOutPropertyConstrustion_FA(property);
        constructionPage.setLargeShed(false);
        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages2 = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        coverages2.clickOk();
        String errorMessage = "";
        boolean testFailed = false;
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);

        // Requiremeny got updated for PropertyTypePL.DwellingUnderConstructionCovE and removing the validation
        int BuildingNumber = propertyDetail.getSelectedBuildingNum();


        //Adding Dwelling under construction validation
        PLPolicyLocationProperty property1 = new PLPolicyLocationProperty();
        property1.setpropertyType(PropertyTypePL.DwellingUnderConstruction);
        property1.setNumberOfUnits(NumberOfUnits.TwoUnits);
        property.setYearBuilt(yearField - 2);

        propertyDetail.fillOutPropertyDetails_FA(property1);
        constructionPage.fillOutPropertyConstrustion_FA(property1);
        constructionPage.setLargeShed(false);
        coverages2.clickOk();
        String valMeg2 = "Dwelling Under Construction cannot be older than";
        String validationMessages = risk.getValidationMessagesText();

        if (!validationMessages.contains(valMeg2)) {
            testFailed = false;
            errorMessage = errorMessage + "Expected Validation message : '" + valMeg2 + "' is not displayed.";
        }
        risk.clickClearButton();
        constructionPage.setYearBuilt(yearField - 1);
        coverages2.clickOk();
        int BuildingNumber1 = propertyDetail.getSelectedBuildingNum();
        sideMenu.clickSideMenuSquirePropertyCoverages();
        coverages.clickSpecificBuilding(1, BuildingNumber);
        coverages.setCoverageELimit(200000);
        coverages.setCoverageECoverageType(CoverageType.Peril_1);

        coverages.clickSpecificBuilding(1, BuildingNumber1);
        coverages.setCoverageALimit(200000);
        coverages.setCoverageCValuation(property1.getPropertyCoverages());
        sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        propertyLocations.clickEditLocation(1);
//        propertyLocations.setNumberOfResidence(12);
        propertyLocations.clickOK();


        sideMenu.clickSideMenuRiskAnalysis();
        risk.performRiskAnalysisAndQuote(squireFullApp);

        if (testFailed)
            Assert.fail(errorMessage);

    }

    @Test(dependsOnMethods = {"testAddingPropertiesSquire"})
    private void testQuoteUmbrella() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
        squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_1000000);

        squireFullApp.squireUmbrellaInfo = squireUmbrellaInfo;
        squireFullApp.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.QuickQuote);


        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.squireFullApp.accountNumber);

        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquireUmbrellaCoverages();
        GenericWorkorderSquireUmbrellaCoverages covs = new GenericWorkorderSquireUmbrellaCoverages(driver);

        if (!covs.checkUnderlyingCoverageExistsByName(SectionIICoveragesEnum.CropDustingAndSpraying.getValue()))
            Assert.fail("Expected Coverage : " + SectionIICoveragesEnum.CropDustingAndSpraying.getValue() + " is not displayed.");

    }
}

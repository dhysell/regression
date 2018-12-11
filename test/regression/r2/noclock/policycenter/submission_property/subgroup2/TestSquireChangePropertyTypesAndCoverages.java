package regression.r2.noclock.policycenter.submission_property.subgroup2;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.Location.ProtectionClassCode;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.FoundationType;
import repository.gw.enums.Property.NumberOfUnits;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;

/**
 * @Author skandibanda
 * @Requirement : US11914 : Changing property types between Cov A, Cov C, and/or Cov E
 * @DATE Aug 17, 2017
 */
@QuarantineClass
public class TestSquireChangePropertyTypesAndCoverages extends BaseTest {
    private GeneratePolicy squirePolicyObj;

    private WebDriver driver;

    @Test
    public void generateSquirePolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));

        PolicyLocation propLoc = new PolicyLocation(locOnePropertyList);
        propLoc.setPlNumAcres(10);
        propLoc.setPlNumResidence(5);
        locationsList.add(propLoc);

        squirePolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withSquireEligibility(SquireEligibility.FarmAndRanch)
                .withInsFirstLastName("Guy", "Auto")
                .withPolicyLocations(locationsList)
                .withPolOrgType(OrganizationType.Individual)
                .build(GeneratePolicyType.FullApp);
    }


    @Test(dependsOnMethods = {"generateSquirePolicy"})
    public void testValidateUWIssuesAndQuoteScreenAmounts() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        new Login(driver).loginAndSearchSubmission(squirePolicyObj.agentInfo.getAgentUserName(), squirePolicyObj.agentInfo.getAgentPassword(), squirePolicyObj.accountNumber);

        guidewireHelpers.editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();


        propertyDetail.clickViewOrEditBuildingButton(2);
        propertyDetail.setUnits(NumberOfUnits.FourUnits);
        propertyDetail.clickPropertyConstructionTab();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);

        constructionPage.setYearBuilt(1950);
        constructionPage.clickOK();
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
        riskAnalysis.Quote();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        double coverageAgrossPremium, coverageAcostToInsured, coverageAdiscountsSurcharges, coverageCgrossPremium, coverageCcostToInsured, coverageCdiscountsSurcharges, coverageEgrossPremium = 0, coverageEcostToInsured = 0, coverageEdiscountsSurcharges = 0;

        coverageAgrossPremium = quote.getQuoteTotalGrossPremium();
        coverageAcostToInsured = quote.getQuoteTotalCostToInsured();
        coverageAdiscountsSurcharges = quote.getQuoteTotalDiscountsSurcharges();


        PropertyTypePL propertyType = (guidewireHelpers.getRandBoolean()) ? PropertyTypePL.Contents : PropertyTypePL.Barn;


        //change Cov A to Cov C(contents)
        guidewireHelpers.editPolicyTransaction();
        addNewPropertyCoverageLimit(propertyType, NumberOfUnits.OneUnit, FoundationType.FullBasement, 250001, (propertyType.equals(PropertyTypePL.Barn) ? null : CoverageType.BroadForm));
        sideMenu.clickSideMenuRiskAnalysis();
        riskAnalysis.Quote();

        sideMenu.clickSideMenuQuote();

        coverageCgrossPremium = quote.getQuoteTotalGrossPremium();
        coverageCcostToInsured = quote.getQuoteTotalCostToInsured();
        coverageCdiscountsSurcharges = quote.getQuoteTotalDiscountsSurcharges();

        if (coverageAgrossPremium == coverageCgrossPremium || coverageAcostToInsured == coverageCcostToInsured || coverageAdiscountsSurcharges == coverageCdiscountsSurcharges) {
            Assert.fail("Quote Screen Amounts should be Changed when Property type changed from Cov A to Cov C");
        }
        sideMenu.clickSideMenuRiskAnalysis();
        validateUWIssuesNotExistForCovCAndE();

        propertyType = (guidewireHelpers.getRandBoolean()) ? PropertyTypePL.ResidencePremisesCovE : (guidewireHelpers.getRandBoolean()) ? PropertyTypePL.VacationHome : PropertyTypePL.CondominiumDwellingPremises;


        guidewireHelpers.editPolicyTransaction();
        addNewPropertyCoverageLimit(propertyType, NumberOfUnits.OneUnit, FoundationType.FullBasement, 250001, (propertyType.equals(PropertyTypePL.ResidencePremisesCovE)) ? null : CoverageType.BroadForm);

        sideMenu.clickSideMenuSquirePropertyDetail();
        propertyDetail.clickViewOrEditBuildingButton(2);
        propertyDetail.clickPropertyConstructionTab();
        constructionPage.setYearBuilt(1950);
        constructionPage.clickOK();

        sideMenu.clickSideMenuRiskAnalysis();
        riskAnalysis.Quote();

        coverageEgrossPremium = quote.getQuoteTotalGrossPremium();
        coverageEcostToInsured = quote.getQuoteTotalCostToInsured();
        coverageEdiscountsSurcharges = quote.getQuoteTotalDiscountsSurcharges();

        if (coverageCgrossPremium == coverageEgrossPremium || coverageCcostToInsured == coverageEcostToInsured || coverageCdiscountsSurcharges == coverageEdiscountsSurcharges) {
            Assert.fail("Quote Screen Amounts should be Changed when Property type changed from Cov C to Cov E");
        }
        sideMenu.clickSideMenuRiskAnalysis();
        validateUWIssuesNotExistForCovCAndE();


    }

    private void addNewPropertyCoverageLimit(PropertyTypePL propertyType, NumberOfUnits noOfUnits, FoundationType foundationType, double limit, CoverageType CType) throws Exception {

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);

        PLPolicyLocationProperty property = new PLPolicyLocationProperty(propertyType);

        propertyDetail.clickViewOrEditBuildingButton(2);
        //propertyDetail.setPropertyDetailsFA(property);
        propertyDetail.setPropertyType(property.getpropertyType());

        propertyDetail.setManualPublicProtectionClassCode(ProtectionClassCode.Prot3);
        if (propertyType.equals(PropertyTypePL.VacationHome)) {
            property.setWoodFireplace(false);
        }
        if (!propertyType.equals(PropertyTypePL.Contents) && !propertyType.equals(PropertyTypePL.Barn)) {
            propertyDetail.setDwellingVacantRadio(property.getDwellingVacant());
            if (propertyDetail.isHowOrByWhomIsThisOccupiedExist()) {
                propertyDetail.setHowOrByWhomIsThisOccupied("Supernaturally");
            }

            if (!property.getpropertyType().equals(PropertyTypePL.GrainSeed) && !property.getpropertyType().equals(PropertyTypePL.Potatoes)) {
                propertyDetail.setUnits(property.getNumberOfUnits());

                if (!property.getpropertyType().equals(PropertyTypePL.ResidencePremisesCovE)) {
                    propertyDetail.setWoodFireplaceRadio(false);
                    propertyDetail.setSwimmingPoolRadio(property.getSwimmingPool());
                    if (property.getSwimmingPool()) {
                        propertyDetail.setPoolDivingBoard(property.getDivingBoard());
                        propertyDetail.setSafetyCover(property.getPoolSafetyCover());
                    }
                    propertyDetail.setWaterLeakageRadio(property.getWaterLeaking());
                    propertyDetail.setExoticPetsRadio(property.getAnimals());
                    if (property.getAnimals()) {
                        propertyDetail.setDescriptionOfExoticPets(property.getAnimalsDescription());
                    }
                }
            }
        }
        propertyDetail.clickPropertyConstructionTab();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);

        if (propertyType.equals(PropertyTypePL.Contents)) {
            constructionPage.setFoundationType(foundationType);
            constructionPage.setConstructionType(ConstructionTypePL.Frame);
        } else {
            constructionPage.fillOutPropertyConstrustion_FA(property);
            if (!propertyType.equals(PropertyTypePL.Barn)) {
                constructionPage.setLargeShed(false);
            }
        }
        GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);

        constructionPage.clickProtectionDetailsTab();
        new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails(driver).fillOutPropertyProtectionDetails(property);
        propertyDetail.clickPropertyInformationDetailsTab();
        if (!propertyType.equals(PropertyTypePL.Barn)) {
            propertyDetail.AddExistingOwner();
        }
        protectionPage.clickOK();

        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverages.clickSpecificBuilding(1, 2);
        coverages.selectSectionIDeductible(SectionIDeductible.FiveHundred);
        if (propertyType.equals(PropertyTypePL.Contents)) {
            coverages.setCoverageCLimit(limit);
        } else if (propertyType.equals(PropertyTypePL.VacationHome)) {
            coverages.setCoverageALimit(limit);
            coverages.setCoverageAIncreasedReplacementCost(property.getPropertyCoverages().getCoverageA().isIncreasedReplacementCost());
        } else if (propertyType.equals(PropertyTypePL.CondominiumDwellingPremises)) {
            coverages.setCoverageALimit(limit);
        } else {
            coverages.setCoverageELimit(limit);
            coverages.setCoverageECoverageType(CoverageType.Peril_1);
        }

        if (CType != null) {
            coverages.selectCoverageCCoverageType(CType);
            coverages.setCoverageCValuation(property.getPropertyCoverages());
        }
    }


    private void validateUWIssuesNotExistForCovCAndE() {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);

        FullUnderWriterIssues uwIssues = riskAnalysis.getUnderwriterIssues();
        if (!uwIssues.isInList("Cov A building built earlier than 1954").equals(UnderwriterIssueType.NONE)) {
            Assert.fail("UnExpected UW Issue : Cov A building built earlier than 1954 displayed.");
        }
    }

}

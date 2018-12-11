package regression.r2.noclock.policycenter.submission_fire_im.subgroup1;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.Location.ProtectionClassCode;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.FoundationType;
import repository.gw.enums.Property.NumberOfUnits;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
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
public class TestStandardFireChangePropertyTypesAndCoverages extends BaseTest {
    private GeneratePolicy stdFirePolicyObj;

    private WebDriver driver;

    @Test
    public void testGenerateStandardFirePolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

        PLPolicyLocationProperty property = new PLPolicyLocationProperty();
        property.setpropertyType(PropertyTypePL.DwellingPremises);

        PLPolicyLocationProperty property1 = new PLPolicyLocationProperty();
        property1.setpropertyType(PropertyTypePL.CondominiumDwellingPremises);

        locOnePropertyList.add(property);
        locOnePropertyList.add(property1);

        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumResidence(12);
        locToAdd.setPlNumAcres(12);
        locationsList.add(locToAdd);

        stdFirePolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("Test", "CovE")
                .withPolicyLocations(locationsList)
                .build(GeneratePolicyType.FullApp);
    }

    @Test(dependsOnMethods = {"testGenerateStandardFirePolicy"})
    public void testValidateUWIssuesAndQuoteScreenAmounts() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(stdFirePolicyObj.agentInfo.getAgentUserName(), stdFirePolicyObj.agentInfo.getAgentPassword(), stdFirePolicyObj.accountNumber);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
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
        double coverageAgrossPremium, coverageAcostToInsured, coverageAdiscountsSurcharges, coverageEgrossPremium, coverageEcostToInsured, coverageEdiscountsSurcharges;
        coverageAgrossPremium = quote.getQuoteTotalGrossPremium();
        coverageAcostToInsured = quote.getQuoteTotalCostToInsured();
        coverageAdiscountsSurcharges = quote.getQuoteTotalDiscountsSurcharges();


        //Cov A to E
        guidewireHelpers.editPolicyTransaction();

        addNewPropertyCoverageLimit(PropertyTypePL.Barn, NumberOfUnits.OneUnit, FoundationType.FullBasement, 250001, null);
        sideMenu.clickSideMenuRiskAnalysis();
        riskAnalysis.Quote();

        coverageEgrossPremium = quote.getQuoteTotalGrossPremium();
        coverageEcostToInsured = quote.getQuoteTotalCostToInsured();
        coverageEdiscountsSurcharges = quote.getQuoteTotalDiscountsSurcharges();


        if (coverageAgrossPremium == coverageEgrossPremium || coverageAcostToInsured == coverageEcostToInsured || coverageAdiscountsSurcharges == coverageEdiscountsSurcharges) {
            Assert.fail("Quote Screen Amounts should be Changed when Property type changed from Cov A to Cov E");
        }
        sideMenu.clickSideMenuRiskAnalysis();
        validateUWIssuesNotExistForCovCAndE();

        //Cov E to A
        guidewireHelpers.editPolicyTransaction();

        addNewPropertyCoverageLimit(PropertyTypePL.CondominiumDwellingPremises, NumberOfUnits.OneUnit, FoundationType.FullBasement, 250001, null);

        sideMenu.clickSideMenuSquirePropertyDetail();
        propertyDetail.clickViewOrEditBuildingButton(2);
        propertyDetail.clickPropertyConstructionTab();
        constructionPage.setYearBuilt(1950);
        constructionPage.clickOK();

        sideMenu.clickSideMenuRiskAnalysis();
        riskAnalysis.Quote();

        sideMenu.clickSideMenuQuote();

        coverageAgrossPremium = quote.getQuoteTotalGrossPremium();
        coverageAcostToInsured = quote.getQuoteTotalCostToInsured();
        coverageAdiscountsSurcharges = quote.getQuoteTotalDiscountsSurcharges();


        if (coverageEgrossPremium == coverageAgrossPremium || coverageEcostToInsured == coverageAcostToInsured || coverageEdiscountsSurcharges == coverageAdiscountsSurcharges) {
            Assert.fail("Quote Screen Amounts should be Changed when Property type changed from Cov E to Cov C");
        }
        sideMenu.clickSideMenuRiskAnalysis();
        validateUWIssuesExistForCOVA();

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
        if (!propertyType.equals(PropertyTypePL.Contents) && !propertyType.equals(PropertyTypePL.Barn)) {
            propertyDetail.setDwellingVacantRadio(property.getDwellingVacant());
            if (propertyDetail.isHowOrByWhomIsThisOccupiedExist()) {
                propertyDetail.setHowOrByWhomIsThisOccupied("Supernaturally");
            }

            if (!property.getpropertyType().equals(PropertyTypePL.GrainSeed) && !property.getpropertyType().equals(PropertyTypePL.Potatoes)) {
                propertyDetail.setUnits(property.getNumberOfUnits());

                if (!property.getpropertyType().equals(PropertyTypePL.ResidencePremisesCovE)) {
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
        if (propertyType.equals(PropertyTypePL.CondominiumDwellingPremises)) {
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

    private void validateUWIssuesExistForCOVA() {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuRiskAnalysis();
        boolean testFailed = false;
        String errorMessage = "";
        GenericWorkorderRiskAnalysis_UWIssues riskAnalysis = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        String[] expectedUWMessages = {"Cov A building built earlier than 1954."};
        for (String uwIssue : expectedUWMessages) {
            boolean messageFound = false;
            for (int i = 0; i < riskAnalysis.getUWIssuesList().size(); i++) {
                String currentUWIssueText = riskAnalysis.getUWIssuesList().get(i).getText();
                if (currentUWIssueText.contains(uwIssue)) {
                    messageFound = true;
                    break;
                }
            }

            if (!messageFound) {
                testFailed = true;
                errorMessage = errorMessage + "Expected UW Issue : " + uwIssue + " is not displayed.";
            }
        }
        if (testFailed)
            Assert.fail(errorMessage);
    }

    private void validateUWIssuesNotExistForCovCAndE() {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuRiskAnalysis();
        boolean testFailed = false;
        String errorMessage = "";
        GenericWorkorderRiskAnalysis_UWIssues riskAnalysis = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        String[] expectedUWMessages = {"Cov A building built earlier than 1954."};
        for (String uwIssue : expectedUWMessages) {
            boolean messageFound = false;
            for (int i = 0; i < riskAnalysis.getUWIssuesList().size(); i++) {
                String currentUWIssueText = riskAnalysis.getUWIssuesList().get(i).getText();
                if (!currentUWIssueText.contains(uwIssue)) {
                    messageFound = true;
                }
            }

            if (!messageFound) {
                testFailed = true;
                errorMessage = errorMessage + "UnExpected UW Issue : " + uwIssue + " displayed.";
            }
        }
        if (testFailed)
            Assert.fail(errorMessage);
    }
}
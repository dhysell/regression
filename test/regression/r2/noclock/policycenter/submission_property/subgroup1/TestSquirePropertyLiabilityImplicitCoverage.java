package regression.r2.noclock.policycenter.submission_property.subgroup1;

import java.util.ArrayList;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.Building.ValuationMethod;
import repository.gw.enums.CoverageType;
import repository.gw.enums.DocFormEvents;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.FoundationType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderLineReviewPL;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;
import persistence.globaldatarepo.entities.Agents;

/**
 * @Author nvadlamudi
 * @Requirement : checking newly added implicit coverages are displayed or not, US7811: Named Person Medical
 * @RequirementsLink <a href="http:// "
 * rally1.rallydev.com/#/33274298124d/detail/testcase/
 * 53634976052</a>
 * @Description
 * @DATE Apr 6, 2016
 */
public class TestSquirePropertyLiabilityImplicitCoverage extends BaseTest {

    private GeneratePolicy myPolicyObjPL = null;
    private GeneratePolicy mySQPolicyObjPL;
    private ArrayList<DocFormEvents.PolicyCenter> eventsHitDuringSubmissionCreationPlusLocal = new ArrayList<DocFormEvents.PolicyCenter>();


    private WebDriver driver;

    @Test()
    public void implicitCoverages() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // Creating policy
        myPolicyObjPL = createPLPropertyAutoPolicy(GeneratePolicyType.FullApp, SquireEligibility.FarmAndRanch);
        Agents agent = myPolicyObjPL.agentInfo;

        // Login with agent
        new Login(driver).loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), myPolicyObjPL.accountNumber);

        // Verifying the Sewage System Backup and adding more properties, coverages
        addMoreSectionISectionIIProperties();

    }

    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Requirement : US8714: Update property coverage
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description
     * @DATE Sep 8, 2016
     */
    @Test()
    public void testSquireWithUpdatedCoverages() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        boolean testFailed = false;
        String errorMessage = "";
        // Creating policy
        mySQPolicyObjPL = createPLPropertyAutoPolicy(GeneratePolicyType.FullApp, SquireEligibility.City);
        Agents agent = mySQPolicyObjPL.agentInfo;

        // Login with agent
        new Login(driver).loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), mySQPolicyObjPL.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();

        // validate 70% Coverage limit is displayed for 1st Residency Premises
        sideMenu.clickSideMenuSquirePropertyCoverages();
        sideMenu.clickSideMenuSquirePropertyLineReview();
        GenericWorkorderLineReviewPL lineReview = new GenericWorkorderLineReviewPL(driver);
        String buildingClass = lineReview.getPropertyCoverageTableCellByRowNumberColumnName(1, "Building Class");
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        String[] coverageCLimit = coverages.getCoverageCLimit().split("%");

        if (coverageCLimit[0].equals("70") && (buildingClass.contains("A"))) {
            System.out.println("Expected Coverage C Limit for Building Class A");
        } else if (coverageCLimit[0].equals("50") && (buildingClass.contains("B") || buildingClass.contains("C") || buildingClass.contains("D"))) {
            System.out.println("Expected Coverage C Limit for Building Class B or C or D");
        } else {
            testFailed = true;
            errorMessage = "Unexpected % Coverage C Limit";
        }
        // Residency Premises - Year <=1954, Mobile home + Foundation Type (either) - Increased Replacement Cost (No)
        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        propertyDetail.clickViewOrEditBuildingButton(1);
        propertyDetail.clickPropertyConstructionTab();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        constructionPage.setYearBuilt(1950);
        constructionPage.setConstructionType(ConstructionTypePL.MobileHome);
        GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);
        protectionPage.clickOK();
        sideMenu.clickSideMenuSquirePropertyCoverages();
        coverages.setCoverageAValuation(ValuationMethod.ReplacementCost);
        coverages.sendArbitraryKeys(Keys.TAB);
        if (coverages.IncreasedReplacementCostExists()) {
            testFailed = true;
            errorMessage = errorMessage + "Increased Replacement cost field exists for Year <=1954, Mobile home \n";
        }
        //Dwelling Under construction  - Increased Replacement Cost - not displayed
        addNewPropertyCoverageLimit(propertyDetail, PropertyTypePL.DwellingUnderConstruction, 65001, CoverageType.BroadForm);
        sideMenu.clickSideMenuSquirePropertyCoverages();
        if (coverages.IncreasedReplacementCostExists()) {
            testFailed = true;
            errorMessage = errorMessage + "Increased Replacement cost field exists for Dwelling Under Construction \n";
        }
        //Content miscellaneous Coverages
        addNewPropertyCoverageLimit(propertyDetail, PropertyTypePL.Contents, 65001, CoverageType.BroadForm);
        coverages.clickSpecificBuilding(1, 3);
        coverages.checkGunsMiscellaneousCoverages(true);
        coverages.setGunsLimit(4000);
        coverages.checkSilverwareMiscellaneousCoverages(true);
        coverages.setSilverwareLimit(80000);
        coverages.checkToolsMiscellaneousCoverages(true);
        coverages.setToolsLimit(8000);
        coverages.checkSaddlesTackMiscellaneousCoverages(true);
        coverages.setSaddlesTackLimit(8000);
        sideMenu.clickSideMenuSquirePropertyDetail();
        propertyDetail.clickViewOrEditBuildingButton(3);
        propertyDetail.AddExistingOwner();
        protectionPage.clickOK();
        propertyDetail.clickViewOrEditBuildingButton(2);
        propertyDetail.AddExistingOwner();
        protectionPage.clickOK();
        //Endorsement 135 -  Endorsement 135 Deletion of Replacement Cost will be automatically inferred if (Coverage = A and Valuation Method = Actual Cash Valuation and Construction Type = Frame or Non-Frame)
        sideMenu.clickSideMenuSquirePropertyDetail();
        propertyDetail.clickViewOrEditBuildingButton(1);
        propertyDetail.AddExistingOwner();
        propertyDetail.clickPropertyConstructionTab();
        constructionPage.setConstructionType(ConstructionTypePL.Frame);
        constructionPage.clickProtectionDetailsTab();
        protectionPage.setDefensibleSpace(false);
        protectionPage.clickOK();
        sideMenu.clickSideMenuSquirePropertyCoverages();
        coverages.setCoverageAValuation(ValuationMethod.ActualCashValue);
        coverages.sendArbitraryKeys(Keys.TAB);
        this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_Liab_ReplacementCostDeletionEndorsement);

        sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation location = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        location.clickEditLocation(mySQPolicyObjPL.squire.propertyAndLiability.locationList.get(0).getNumber());
//        location.setNumberOfResidence(4);
        location.clickOK();

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis_UWIssues risk = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        risk.Quote();

        //UW Issue: Defensible or not	If  answered 'No' on 'Is defensible space maintained' under Protection Detail tab
        sideMenu.clickSideMenuRiskAnalysis();
        boolean UWIssueFound = false;
        for (int i = 0; i < risk.getUWIssuesList().size(); i++) {
            String currentUWIssueText = risk.getUWIssuesList().get(i).getText();
            if (currentUWIssueText.contains("Defensible Or Not")) {
                UWIssueFound = true;
                break;
            }
        }
        if (!UWIssueFound) {
            testFailed = true;
            errorMessage = errorMessage + "Expected UW Issue :Defensible Or Not is not displayed \n";
        }

        if (testFailed) {
            Assert.fail(errorMessage);
        }
    }

    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Description : Adding Section II coverages and validating the sewage
     * backup System and limit
     * @DATE Apr 6, 2016
     */
    private void addMoreSectionISectionIIProperties() throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        boolean testFailed = false;
        String errorMessage = "";

        new GuidewireHelpers(driver).editPolicyTransaction();

        // checking the coverage page for 'Sewage System Backup should not be displayed."
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);

        addNewPropertyCoverageLimit(propertyDetail, PropertyTypePL.DwellingPremises, 25001, CoverageType.BroadForm);
        addNewPropertyCoverageLimit(propertyDetail, PropertyTypePL.Contents, 250001, CoverageType.BroadForm);
        addNewPropertyCoverageLimit(propertyDetail, PropertyTypePL.DwellingPremisesCovE, 45000, CoverageType.Peril_1);

        // verifying the sewage backup system is displayed with expected limit
        sideMenu.clickSideMenuSquirePropertyCoverages();
        coverages.clickSpecificBuilding(1, 2);
        if (!coverages.checkSewegeBackupSystem(true)) {
            testFailed = true;
            errorMessage = errorMessage + "Sewege Backup System option is Not displayed for Dwelling Premises";
        }

        if (!coverages.getSewageBackupSystemLimit().contains("10%")) {
            testFailed = true;
            errorMessage = errorMessage + "Sewege Backup System limit : 10% is Not displayed as expected. ";
        }

        // Adding SectionII coverages
        ArrayList<SectionIICoverages> coveragesList = new ArrayList<SectionIICoverages>();
        coveragesList.add(new SectionIICoverages(SectionIICoveragesEnum.CropDustingAndSpraying, 0, 0));
        coveragesList.add(new SectionIICoverages(SectionIICoveragesEnum.GolfCart, 0, 2));
        coveragesList.add(new SectionIICoverages(SectionIICoveragesEnum.CustomFarming, 0, 0));


        GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages section2Covs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
        section2Covs.clickSectionIICoveragesTab();
        section2Covs.addCoveragesFromList(coveragesList);
        section2Covs.setQuantity(new SectionIICoverages(SectionIICoveragesEnum.GolfCart, 0, 2));
        section2Covs.setAmount(new SectionIICoverages(SectionIICoveragesEnum.CustomFarming, 25000, 0));
        section2Covs.addCoverages(new SectionIICoverages(SectionIICoveragesEnum.CustomFarmingFire, 0, 0));
        section2Covs.addCoverages(new SectionIICoverages(SectionIICoveragesEnum.NamedPersonsMedical, 0, 0));
        section2Covs.setNamedPersonsMedical(this.myPolicyObjPL.pniContact.getLastName());

        if (!section2Covs.getNamedPersonsMedicalDeductible().contains("500")) {
            testFailed = true;
            errorMessage = errorMessage + "Named Persons Medical Deductible 500 is not displayed. \n";
        }

        if (!section2Covs.getNamedPersonsMedicalLimit().replace(",", "").contains(section2Covs.getMedicalLimit().replace(",", "")) && Integer.parseInt(section2Covs.getMedicalLimit().replace(",", "")) < 25000) {
            testFailed = true;
            errorMessage = errorMessage + "Named Persons Medical Deductible 500 is not displayed.(what i think i was really trying to say is that Named Person Medical Limit do not match and/or less than 25000 \n";
        }

        section2Covs.sendArbitraryKeys(Keys.TAB);

        // taking acres on the section II coverages page
        int totalNoAces = section2Covs.getTotalAcres();
        section2Covs.clicknext();

        sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        propertyLocations.clickEditLocation(1);
        int currentLocationsAcres = propertyLocations.getAcres();
        propertyLocations.clickOK();

        if (currentLocationsAcres != totalNoAces) {
            testFailed = true;
            errorMessage = errorMessage + "Total number of acres displayed on the Section II page is not matched.\n";
        }

        //
        sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
        household.clickRemoveMember(this.myPolicyObjPL.pniContact.getLastName());

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        if (!guidewireHelpers.errorMessagesExist() && (!guidewireHelpers.getFirstErrorMessage().contains("is a PNI and cannot be removed"))) {
            testFailed = true;
            errorMessage = errorMessage + "Expected page validation : 'is a PNI and cannot be removed' is not displayed. /n";
        }

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + errorMessage);
        }

    }

    /**
     * @param propertyDetail
     * @param propertyType
     * @param limit
     * @param cType
     * @throws Exception
     * @Author nvadlamudi
     * @Description : Adding new property along with limit
     * @DATE Apr 6, 2016
     */
    private void addNewPropertyCoverageLimit(GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail, PropertyTypePL propertyType, double limit, CoverageType cType) throws Exception {

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        PLPolicyLocationProperty property = new PLPolicyLocationProperty(propertyType);
        property.setbasementFinishedPercent(30);

        if (property.getpropertyType().equals(PropertyTypePL.DwellingUnderConstruction)) {
            int yearField = DateUtils.getCenterDateAsInt(driver, ApplicationOrCenter.PolicyCenter, "yyyy");
            property.setYearBuilt((yearField - 1));
        }
        propertyDetail.fillOutPropertyDetails_FA(property);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);

        if (propertyType.equals(PropertyTypePL.Contents)) {
            constructionPage.setConstructionType(ConstructionTypePL.MobileHome);
            constructionPage.setFoundationType(FoundationType.FullBasement);
        } else {
            constructionPage.fillOutPropertyConstrustion_FA(property);
            constructionPage.setLargeShed(false);
            if (!propertyType.equals(PropertyTypePL.DwellingPremisesCovE) && !propertyType.equals(PropertyTypePL.DwellingUnderConstruction)) {
                constructionPage.clickProtectionDetailsTab();
                new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails(driver).fillOutPropertyProtectionDetails(property);
            }
        }

        protectionPage.clickOK();
        int buildingNumber = propertyDetail.getSelectedBuildingNum();
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverages.clickSpecificBuilding(1, buildingNumber);

        if (propertyType.equals(PropertyTypePL.Contents)) {
            coverages.setCoverageCLimit(limit);
        } else if (propertyType.toString().contains("CovE")) {
            coverages.setCoverageELimit(limit);
        } else {
            coverages.setCoverageALimit(limit);
        }

        if (cType != null) {
            if (propertyType.toString().contains("CovE")) {
                coverages.setCoverageECoverageType(cType);
                coverages.setCoverageCLimit(limit);
                coverages.setCoverageCValuation(property.getPropertyCoverages());
            } else {
                coverages.setCoverageCValuation(property.getPropertyCoverages());

                if (!property.getPropertyCoverages().getCoverageC().getValuationMethod().equals(ValuationMethod.ActualCashValue) && (property.getYearBuilt() > 1954 && !property.getConstructionType().equals(ConstructionTypePL.MobileHome)))
                    coverages.selectCoverageCCoverageType(cType);
            }

            if (propertyType.equals(PropertyTypePL.Contents)) {
                coverages.selectCoverageCCoverageType(cType);
            }

        }
    }

    /**
     * @param policyType
     * @return
     * @throws Exception
     * @Author nvadlamudi
     * @Description : Creating property type policy
     * @DATE Apr 6, 2016
     */
    private GeneratePolicy createPLPropertyAutoPolicy(GeneratePolicyType policyType, SquireEligibility eligibility) throws Exception {

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();

        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation resPrem = new PolicyLocation(locOnePropertyList);
        resPrem.setPlNumAcres(11);
        resPrem.setPlNumResidence(5);

        locationsList.add(resPrem);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire(eligibility);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Test", "ImplicitCovs")
                .build(policyType);

        return myPolicyObjPL;
    }

}

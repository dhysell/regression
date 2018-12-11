package regression.r2.noclock.policycenter.submission_property.subgroup2;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.Building.ValuationMethod;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.FoundationType;
import repository.gw.enums.Property.NumberOfUnits;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.Property.SectionIIMedicalLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderLineReviewPL;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;
import persistence.enums.Underwriter.UnderwriterLine;
import persistence.enums.Underwriter.UnderwriterTitle;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : US6148 Block Issues, Block Quote - UW approval requirement is
 * added
 * @RequirementsLink <a href="http:// "
 * rally1.rallydev.com/#/33274298124d/detail/userstory/
 * 50286553943</a>
 * @Description
 * @DATE Mar 21, 2016
 */
@QuarantineClass
public class TestSquirePropertyLiabilityBusinessRulesSectionOne extends BaseTest {

    public GeneratePolicy myPolicyObjPL1 = null;
    public GeneratePolicy myPolicyObjPL2 = null;
    public GeneratePolicy myPolicyObjPL3 = null;
    private WebDriver driver;

    @BeforeMethod
    public void beforeMethod() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
    }



    @Test
    public void createPLAutoPolicy2() throws Exception {
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        SquireEligibility squireType = (guidewireHelpers.getRandBoolean()) ? SquireEligibility.City : ((guidewireHelpers.getRandBoolean()) ? SquireEligibility.Country : SquireEligibility.FarmAndRanch);
        // Coverages
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow, MedicalLimit.TenK);
        coverages.setUnderinsured(false);
        coverages.setAccidentalDeath(true);

        // driver
        ArrayList<Contact> driversList = new ArrayList<Contact>();
        Contact person = new Contact();
        driversList.add(person);

        // Vehicle
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle newVeh = new Vehicle();
        newVeh.setEmergencyRoadside(true);
        vehicleList.add(newVeh);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        locationsList.add(new PolicyLocation(locOnePropertyList));

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);
        squirePersonalAuto.setVehicleList(vehicleList);
        squirePersonalAuto.setDriversList(driversList);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;


        Squire mySquire = new Squire(squireType);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObjPL1 = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Test", "Propbusrules")
                .build(GeneratePolicyType.FullApp);
    }


    @Test(enabled = true, dependsOnMethods = {"createPLAutoPolicy2"})
    public void testBusinessRules() throws Exception {
        // Login with agent
        new Login(driver).loginAndSearchSubmission(myPolicyObjPL1);

        // validate Property Locations
        switch (myPolicyObjPL1.squire.squireEligibility) {
            case City:
                validatePropertyLocations(11);
                break;
            case Country:
                validatePropertyLocations(3);
                break;
            case CountryIneligibleCustomFarmingCoverage:
                break;
            case CustomAuto:
                break;
            case FarmAndRanch:
                validatePropertyLocations(4);
                break;
        }

        // Validating Property Coverages
        validatePropertyCoverages(myPolicyObjPL1.squire.squireEligibility, myPolicyObjPL1);

        // Adding 6154 validations for Block Issues,quote erros
        addingBlockBindIssuesQuoteScenarios(myPolicyObjPL1.squire.squireEligibility);

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickQuote();
        validateRiskAnalysisApprovals(myPolicyObjPL1.squire.squireEligibility, myPolicyObjPL1);
    }


    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Requirement :Validating for SquireElegibility = City
     * @RequirementsLink <a href="http://
     * rally1.rallydev.com/#/33274298124d/detail/userstory/
     * 50286553943</a>
     * @Description
     * @DATE Mar 21, 2016
     */
    @Test(enabled = false)
    public void testCityBusinessRules() throws Exception {
        // Creating policy
        myPolicyObjPL2 = createPLAutoPolicy(GeneratePolicyType.FullApp, SquireEligibility.City);
        Agents agent = myPolicyObjPL2.agentInfo;

        // Login with agent
        new Login(driver).loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), myPolicyObjPL2.accountNumber);

        // validate Property Locations
        validatePropertyLocations(11);

        // Validating Property Coverages
        validatePropertyCoverages(SquireEligibility.City, myPolicyObjPL2);

        // Adding 6154 validations for Block Issues,quote erros
        addingBlockBindIssuesQuoteScenarios(SquireEligibility.City);

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickQuote();
        validateRiskAnalysisApprovals(SquireEligibility.City, myPolicyObjPL2);
    }

    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Requirement :Validating for SquireElegibility = County
     * @RequirementsLink <a href="http://
     * rally1.rallydev.com/#/33274298124d/detail/userstory/
     * 50286553943</a>
     * @Description
     * @DATE Mar 21, 2016
     */
    @Test(enabled = false)
    public void testCountyBusinessRules() throws Exception {

        // Creating policy
        myPolicyObjPL2 = createPLAutoPolicy(GeneratePolicyType.FullApp, SquireEligibility.Country);
        Agents agent = myPolicyObjPL2.agentInfo;

        // Login with agent
        new Login(driver).loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), myPolicyObjPL2.accountNumber);
        // validate Property Locations


        // Validating Property Coverages
        validatePropertyCoverages(SquireEligibility.Country, myPolicyObjPL2);

        //DE3600
        validateSectionIDeductiblesDropdownvalues();

        // Adding 6154 validations for Block Issues,quote erros
        addingBlockBindIssuesQuoteScenarios(SquireEligibility.Country);

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickQuote();
        validateRiskAnalysisApprovals(SquireEligibility.Country, myPolicyObjPL2);
    }

    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Requirement :Validating for SquireElegibility = Farm And Ranch
     * @RequirementsLink <a href="http://
     * rally1.rallydev.com/#/33274298124d/detail/userstory/
     * 50286553943</a>
     * @Description
     * @DATE Mar 21, 2016
     */
    @Test(enabled = false)
    public void testFarmRanchBusinessRules() throws Exception {
        // Creating policy
        myPolicyObjPL2 = createPLAutoPolicy(GeneratePolicyType.FullApp, SquireEligibility.FarmAndRanch);
        Agents agent = myPolicyObjPL2.agentInfo;

        // Login with agent
        new Login(driver).loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), myPolicyObjPL2.accountNumber);

        // validate Property Locations
        validatePropertyLocations(4);

        // Validating Property Coverages
        validatePropertyCoverages(SquireEligibility.FarmAndRanch, myPolicyObjPL2);

        // Adding 6154 validations for Block Issues,quote erros
        addingBlockBindIssuesQuoteScenarios(SquireEligibility.FarmAndRanch);

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickQuote();
        validateRiskAnalysisApprovals(SquireEligibility.FarmAndRanch, myPolicyObjPL2);
    }

    private void addingBlockBindIssuesQuoteScenarios(SquireEligibility eligibilityType) throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);

        // A property deductible of $10,000 requires UW approval
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverages.selectSectionIDeductible(SectionIDeductible.TwentyFiveThousand);

        // adding gun coverage
        coverages.setGunIncreasedTheftLimit(20200);

        // Add property over $1,500,000
        coverages.setCoverageALimit(200000);

        // Adding If Endorsement 209 (Access Yes Endorsement) is added to the
        // policy, create block issue.
        if (!eligibilityType.equals(SquireEligibility.City)) {

            System.out.println("Access Yes Endorsement 209 IS SET BY UW'S ONLY. SWITCHING USER TO UW");
            GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
            GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
            quote.clickQuote();
            guidewireHelpers.logout();
            Underwriters randomUW = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(UnderwriterLine.Personal, UnderwriterTitle.Underwriter);
            new Login(driver).loginAndSearchSubmission(randomUW.getUnderwriterUserName(), randomUW.getUnderwriterPassword(), myPolicyObjPL1.accountNumber);

            guidewireHelpers.editPolicyTransaction();

            sideMenu = new SideMenuPC(driver);
            sideMenu.clickSideMenuSquirePropertyCoverages();
            GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions exclusiionConditions = new GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions(driver);
            exclusiionConditions.clickCoveragesExclusionsAndConditions();
            exclusiionConditions.checkAccessYesEndorsement209(true);

						/*quote.clickQuote();
						logout();
			loginAndSearchSubmission(myPolicyObjPL);
			
			polInfo.clickEditPolicyTransaction();*/

            sideMenu.clickSideMenuSquirePropertyCoverages();
            exclusiionConditions.clickCoveragesExclusionsAndConditions();
        }

        // Only 4 rental units can be added to a city or country squire without
        // requiring approval. UW approval require to add more than 4 rental
        // units per policy
        sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        propertyLocations.clickEditLocation(1);
//        propertyLocations.setNumberOfResidence(12);
        propertyLocations.clickOK();

        // If a property is vacant, then block issue. and A with construction
        // year prior to 1954 needs to be flagged for the UW to review
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        propertyDetail.clickViewOrEditBuildingButton(1);
        propertyDetail.setDwellingVacantRadio(true);
        propertyDetail.AddExistingOwner();
        propertyDetail.clickPropertyConstructionTab();
        GenericWorkorderSquirePropertyDetailConstruction constructionPage = new GenericWorkorderSquirePropertyDetailConstruction(driver);
        constructionPage.clickOK();

        if (eligibilityType.equals(SquireEligibility.City) || eligibilityType.equals(SquireEligibility.Country)) {
            addNewPropertyCoverageLimit(propertyDetail, PropertyTypePL.DwellingPremises, NumberOfUnits.FourUnits, FoundationType.FullBasement, 25000, CoverageType.BroadForm);
            addNewPropertyCoverageLimit(propertyDetail, PropertyTypePL.CondominiumDwellingPremises, NumberOfUnits.TwoUnits, FoundationType.FullBasement, 40000, CoverageType.BroadForm);
        }

        addNewPropertyCoverageLimit(propertyDetail, PropertyTypePL.Contents, NumberOfUnits.OneUnit, FoundationType.FullBasement, 250001, CoverageType.BroadForm);
        addNewPropertyCoverageLimit(propertyDetail, PropertyTypePL.ResidencePremises, NumberOfUnits.OneUnit, FoundationType.FullBasement, 45000, CoverageType.BroadForm);

    }

    /**
     * @param propertyDetail
     * @param propertyType
     * @param noOfUnits
     * @param foundationType
     * @param limit
     * @param CType
     * @throws Exception
     * @Author nvadlamudi
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description
     * @DATE Apr 4, 2016
     */
    private void addNewPropertyCoverageLimit(GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail, PropertyTypePL propertyType, NumberOfUnits noOfUnits, FoundationType foundationType, double limit, CoverageType CType) throws Exception {

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propertyDetial = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
//		propertyDetial.clickAdd();
        PLPolicyLocationProperty property = new PLPolicyLocationProperty(propertyType);
        property.setFoundationType(foundationType);
        property.setNumberOfUnits(noOfUnits);

        if (propertyType.equals(PropertyTypePL.DwellingUnderConstruction)) {
            property.setYearBuilt(2016);
        }

        if (propertyType.equals(PropertyTypePL.ResidencePremises)) {
            property.setYearBuilt(1950);
        }

        if (propertyType.equals(PropertyTypePL.Contents)) {
            property.setDwellingVacant(false);
        }

        propertyDetail.fillOutPropertyDetails_FA(property);

        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        if (propertyType.equals(PropertyTypePL.Contents)) {
            constructionPage.setFoundationType(foundationType);
            constructionPage.setConstructionType(ConstructionTypePL.Frame);
        } else {
            constructionPage.fillOutPropertyConstrustion_FA(property);
            constructionPage.setLargeShed(false);
        }
        GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);

        constructionPage.clickProtectionDetailsTab();
        new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails(driver).fillOutPropertyProtectionDetails(property);
        propertyDetial.clickPropertyInformationDetailsTab();
        propertyDetail.AddExistingOwner();
        protectionPage.clickOK();
        int BuildingNumber = propertyDetail.getSelectedBuildingNum();

        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverages.clickSpecificBuilding(1, BuildingNumber);

        if (propertyType.equals(PropertyTypePL.Contents)) {
            coverages.setCoverageCLimit(limit);
        } else {
            coverages.setCoverageALimit(limit);
            coverages.setCoverageAIncreasedReplacementCost(property.getPropertyCoverages().getCoverageA().isIncreasedReplacementCost());
        }

        if (CType != null) {
            coverages.selectCoverageCCoverageType(CType);
            coverages.setCoverageCValuation(property.getPropertyCoverages());
        }
    }

    /**
     * @param eligibility
     * @throws GuidewirePolicyCenterException
     * @Author nvadlamudi
     * @Description : Validating the risk analysis page for UW issues
     * @DATE Mar 21, 2016
     */
    private void validateRiskAnalysisApprovals(SquireEligibility eligibility, GeneratePolicy policy) {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuRiskAnalysis();
        boolean testFailed = false;
        String errorMessage = "";
        GenericWorkorderRiskAnalysis_UWIssues riskAnalysis = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        String[] expectedUWMessages = {"acres", "State is not Idaho",
                "Watercraft length over 40 ft", "Incidental Occupancy coverage exists", "Only 4 Rental Units allowed",
                "Vacant Property", "Gun coverage cannot be $20,000 or more",
                "More than 1 rental unit per property", "High property limit", "Section I deductible $10,000 or higher",
                "Cov A building built earlier than 1954", "CLUE Auto Not Ordered", "CLUE Property Not Ordered"};

        for (String uwIssue : expectedUWMessages) {
            boolean messageFound = false;
            for (int i = 0; i < riskAnalysis.getUWIssuesList().size(); i++) {
                String currentUWIssueText = riskAnalysis.getUWIssuesList().get(i).getText();
                if (!currentUWIssueText.contains("Blocking Bind") && (!currentUWIssueText.contains("Blocking Issuance"))) {

                    if ((uwIssue.toLowerCase().contains("rental")) && (eligibility.equals(SquireEligibility.FarmAndRanch))) {
                        messageFound = true;
                        break;
                    }
                    if (currentUWIssueText.contains("City acreage over 10") && uwIssue.contains("acres") && eligibility.equals(SquireEligibility.City)) {
                        messageFound = true;
                        break;
                    }

                    if (currentUWIssueText.contains("acreage") && uwIssue.contains("acres") && (eligibility.equals(SquireEligibility.Country) || eligibility.equals(SquireEligibility.FarmAndRanch))) {
                        messageFound = true;
                        break;
                    }

                    if (currentUWIssueText.contains("Access Yes") && eligibility.equals(SquireEligibility.City)) {
                        messageFound = true;
                        break;
                    }

                    if (currentUWIssueText.contains(uwIssue)) {
                        messageFound = true;
                        break;
                    }
                }
            }
            if (!messageFound) {
                testFailed = true;
                errorMessage = errorMessage + "Expected error Bind Issue : " + uwIssue + " is not displayed.\n";
            }
        }

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + policy.accountNumber + errorMessage);
        }


    }

    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Description: Validating the property coverage and page validations
     * @DATE Mar 21, 2016
     */
    private void validatePropertyCoverages(SquireEligibility eligibility, GeneratePolicy policy) throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        boolean testFailed = false;
        String errorMessage = "";

        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        coverages.clickSectionIICoveragesTab();

        // Changing Section 2 coverages
        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_500000_CSL);
        myLiab.setMedicalLimit(SectionIIMedicalLimit.Limit_10000);

        ArrayList<SectionIICoverages> coveragesList = new ArrayList<SectionIICoverages>();

        // Horse boarding and Pasturing and horse boarding options will not be available for City squire (refer product model document)
        if (!eligibility.equals(SquireEligibility.City)) {
            coveragesList.add(new SectionIICoverages(SectionIICoveragesEnum.SeedsmanEAndO, 0, 0));
            coveragesList.add(new SectionIICoverages(SectionIICoveragesEnum.HorseBoardingAndPasturing, 0, 0));
        }

        coveragesList.add(new SectionIICoverages(SectionIICoveragesEnum.IncidentalOccupancy, 0, 0));

        GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages section2Covs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
        section2Covs.setGeneralLiabilityLimit(myLiab.getGeneralLiabilityLimit());
        section2Covs.setMedicalLimit(myLiab.getMedicalLimit());
        section2Covs.addCoveragesFromList(coveragesList);

        if (!eligibility.equals(SquireEligibility.City)) {
            section2Covs.addCoverages(new SectionIICoverages(SectionIICoveragesEnum.HorseBoarding, 0, 0));
            section2Covs.setQuantity(new SectionIICoverages(SectionIICoveragesEnum.HorseBoarding, 0, 2));
            section2Covs.setQuantity(new SectionIICoverages(SectionIICoveragesEnum.HorseBoardingAndPasturing, 0, 2));
        }

        section2Covs.addIncidentalOccupancy("Testing", policy.squire.propertyAndLiability.locationList.get(0).getAddress().getCity());
        section2Covs.addWatercraftLengthCoverage("Testing", 42);

        if (!eligibility.equals(SquireEligibility.City)) {
            if (!section2Covs.getCoveragesMessage().contains("Cannot add seedsman E and O coverage if liability limits exceed 300,000 CSL or 300/500/100 split")) {
                testFailed = false;
                errorMessage = errorMessage + "Expected message 'Cannot add seedsman E and O coverage if liability limits exceed 300,000 CSL or 300/500/100 split' is not displayed";
            }

            if (!section2Covs.getCoveragesMessage().contains("A policy can either have Horse Boarding or Horse Boarding and Pasturing coverage on Section II and never both")) {
                testFailed = false;
                errorMessage = errorMessage + "Expected message 'A policy can either have Horse Boarding or Horse Boarding and Pasturing coverage on Section II and never both' is not displayed";
            }
            section2Covs.clickHorseBoardingCheckbox();
        } else {
            sideMenu.clickSideMenuSquirePropertyCoverages();
            coverages.clickSectionIICoveragesTab();
        }


        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_100_300_100);
        section2Covs.setGeneralLiabilityLimit(myLiab.getGeneralLiabilityLimit());

        // validating liability and medical in Auto coverages
        sideMenu.clickSideMenuSquireLineReview();
        GenericWorkorderLineReviewPL lineReview = new GenericWorkorderLineReviewPL(driver);

        if (!lineReview.getLineReviewLiabilityBIPD().equals(SectionIIGeneralLiabLimit.Limit_100_300_100.getValue())) {
            testFailed = true;
            errorMessage = errorMessage + "Expected Liability Limit : " + SectionIIGeneralLiabLimit.Limit_100_300_100.getValue() + " is not displayed.";
        }

        if (!lineReview.getLineReviewMedical().equals(myLiab.getMedicalLimit().getValue())) {
            testFailed = true;
            errorMessage = errorMessage + "Expected Medical Limit : " + myLiab.getMedicalLimit().getValue() + " is not displayed.";
        }

        // Modifying the coverage details
        sideMenu.clickSideMenuPACoverages();
        GenericWorkorderSquireAutoCoverages_Coverages paCoverages = new GenericWorkorderSquireAutoCoverages_Coverages(driver);
        paCoverages.setLiabilityCoverage(LiabilityLimit.FiftyHigh);
        paCoverages.setMedicalCoverage(MedicalLimit.TwentyFiveK);

        // Validating in Property Coverages
        sideMenu.clickSideMenuSquirePropertyCoverages();
        coverages.clickSectionIICoveragesTab();

        if (!section2Covs.getGeneralLiabilityLimit().equals(LiabilityLimit.FiftyHigh.getValue())) {
            testFailed = true;
            errorMessage = errorMessage + "Expected General Liability Limit : " + LiabilityLimit.FiftyHigh.getValue() + " is not displayed.";
        }

        if (!section2Covs.getMedicalLimit().equals(MedicalLimit.TwentyFiveK.getValue())) {
            testFailed = true;
            errorMessage = errorMessage + "Expected Medical Limit : " + MedicalLimit.TwentyFiveK.getValue() + " is not displayed.";
        }


        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + policy.accountNumber + errorMessage);
        }


    }

    /**
     * @param acres
     * @throws Exception
     * @Author nvadlamudi
     * @Description : Property locations
     * @DATE Mar 21, 2016
     */
    private void validatePropertyLocations(int acres) throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);

        new GuidewireHelpers(driver).editPolicyTransaction();

        sideMenu.clickSideMenuPropertyLocations();

        // Adding Acre to 0 and validating the page validation
        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        propertyLocations.clickEditLocation(1);
//        propertyLocations.setAcres(0);
//        propertyLocations.clickOK();
//        if (!propertyLocations.getValidationMessage().contains("must have at least 1 acre")) {
//            Assert.fail(driver.getCurrentUrl() + " Expected Locations - Acres page validation message : 'must have at least 1 acre' is not displayed.");
//        }
//        // After validation adding acres 11
//        propertyLocations.clickEditLocation(1);
//        propertyLocations.setAcres(acres);
//        propertyLocations.clickOK();
//
//        // Adding new location with State!= Idaho
//        propertyLocations.clickNewLocation();
//        propertyLocations.selectAddressType("Out of State");
//        propertyLocations.selectState(State.Oregon);
//        propertyLocations.setCity("Bend");
//        propertyLocations.setZipCode("97702");
//        propertyLocations.setCounty("Bend");
//        propertyLocations.setAddressLegal("Testing purpose");
//        propertyLocations.setAcres(2);
        propertyLocations.clickOK();

    }

    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Description : Approve the special approval requests
     * @DATE Mar 21, 2016
     */
	/*private void approveRiskAnalysisUWRequests() throws GuidewirePolicyCenterException {
		ISideMenu sideMenu = SideMenuFactory.getMenu();
		sideMenu.clickSideMenuRiskAnalysis();
		GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
		riskAnalysis.specialApproveAll();
				riskAnalysis.clickGenericWorkorderNext();
		riskAnalysis.clickGenericWorkorderNext();
		riskAnalysis.clickGenericWorkorderNext();
				GenericWorkorderPayment payments = new GenericWorkorderPayment(driver);
		payments.makeDownPayment(PaymentPlanType.Annual, PaymentType.Credit_Debit, 0.00);
	}*/

    //DE3600 - Some Section I deductibles are not available at new submission under Coverages and grandfathered in
    public void validateSectionIDeductiblesDropdownvalues() throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyCoverages();

        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverages.selectSectionIDeductibleGrandFatheredDropdownValues();

    }


    /**
     * @param policyType
     * @param eligibilityType
     * @return
     * @throws Exception
     * @Author nvadlamudi
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description :Generating policy
     * @DATE Mar 21, 2016
     */
    private GeneratePolicy createPLAutoPolicy(GeneratePolicyType policyType, SquireEligibility eligibilityType) throws Exception {
        // Coverages
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow, MedicalLimit.TenK);
        coverages.setUnderinsured(false);
        coverages.setAccidentalDeath(true);

        // driver
        ArrayList<Contact> driversList = new ArrayList<Contact>();
        Contact person = new Contact();
        driversList.add(person);

        // Vehicle
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle newVeh = new Vehicle();
        newVeh.setEmergencyRoadside(true);
        vehicleList.add(newVeh);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        locationsList.add(new PolicyLocation(locOnePropertyList));

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);
        squirePersonalAuto.setVehicleList(vehicleList);
        squirePersonalAuto.setDriversList(driversList);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;


        Squire mySquire = new Squire(eligibilityType);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;


        myPolicyObjPL2 = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Test", "Propbusrules")
                .build(policyType);

        return myPolicyObjPL2;
    }

    @Test
    public void testSewageSystemBackupRules() throws Exception {

        AdditionalInterest propertyAdditionalInterest = new AdditionalInterest(ContactSubType.Company);
        propertyAdditionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
        propertyAdditionalInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
        propertyAdditionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        PLPolicyLocationProperty locationOneProperty = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        locationOneProperty.setBuildingAdditionalInterest(propertyAdditionalInterest);
        locOnePropertyList.add(locationOneProperty);
        locationsList.add(new PolicyLocation(locOnePropertyList));

        this.myPolicyObjPL3 = new GeneratePolicy.Builder(driver)
                .withInsFirstLastName("ins", "Duesdefect")
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Quarterly)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.QuickQuote);

        System.out.println("Account Number: " + myPolicyObjPL3.accountNumber);
        new Login(driver).loginAndSearchJob(myPolicyObjPL3.agentInfo.agentUserName, myPolicyObjPL3.agentInfo.agentPassword, myPolicyObjPL3.accountNumber);
        GenericWorkorderPolicyInfo policyInfoPage = new GenericWorkorderPolicyInfo(driver);
        policyInfoPage.clickEditPolicyTransaction();
        testCoverageA();
        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        coverages.clickQuote();
    }

    public void testCoverageA() throws Exception {
        //Dwelling properties are rentals and not available for special form, therefore they don't apply to this business rule.
        ArrayList<PropertyTypePL> coverageATypes = new ArrayList<PropertyTypePL>();
        coverageATypes.add(PropertyTypePL.ResidencePremises);
        coverageATypes.add(PropertyTypePL.VacationHome);
        coverageATypes.add(PropertyTypePL.CondominiumResidencePremise);
        coverageATypes.add(PropertyTypePL.CondominiumVacationHome);

        SideMenuPC menu = new SideMenuPC(driver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propDetails = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        for (PropertyTypePL type : coverageATypes) {
            menu = new SideMenuPC(driver);
            menu.clickSideMenuSquirePropertyDetail();

            propDetails = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
            propDetails.clickEdit();
            propDetails.setPropertyType(type);
            propDetails.clickOk();

            menu = new SideMenuPC(driver);
            menu.clickSideMenuSquirePropertyCoverages();

            coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
            if (!type.equals(PropertyTypePL.CondominiumResidencePremise)) {
                if (!coverages.getCoverageAValuation().contains("Replacement Cost")) {
                    coverages.setCoverageAValuation(ValuationMethod.ReplacementCost);
                }
            }
            coverages.setCoverageACoverageType(CoverageType.SpecialForm);
            Assert.assertFalse(!coverages.checkIfSewageSystemBackupExists(), "For Coverage A property and Coverage Type Special Form, the Sewage Checkbox should default to True.");

            coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
            coverages.setCoverageACoverageType(CoverageType.BroadForm);
            Assert.assertFalse(!coverages.checkIfSewageSystemBackupExists(), "For Coverage A property and Coverage Type Broad Form, the Sewage Checkbox should default to True.");
            coverages.setSewageSystemBackup(false);
            coverages.setSewageSystemBackup(true);
        }

    }

    @Test(dependsOnMethods = {"testSewageSystemBackupRules"})
    public void testFullAppSewageRule() throws Exception {

        myPolicyObjPL3.convertTo(driver, GeneratePolicyType.FullApp);
        new Login(driver).loginAndSearchJob(myPolicyObjPL3.agentInfo.agentUserName, myPolicyObjPL3.agentInfo.agentPassword, myPolicyObjPL3.accountNumber);
        GenericWorkorderPolicyInfo policyInfoPage = new GenericWorkorderPolicyInfo(driver);
        policyInfoPage.clickEditPolicyTransaction();
        testCoverageA();
        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        coverages.clickQuote();

    }

    @Test(dependsOnMethods = {"testFullAppSewageRule"})
    public void testChangeSewageRule() throws Exception {
        myPolicyObjPL3.convertTo(driver, GeneratePolicyType.PolicyIssued);
        new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObjPL3.underwriterInfo.getUnderwriterUserName(), myPolicyObjPL3.underwriterInfo.getUnderwriterPassword(), myPolicyObjPL3.accountNumber);
//		loginAndSearchPolicyByAccountNumber(myPolicyObjPL3);
        StartPolicyChange change = new StartPolicyChange(driver);
        change.startPolicyChange("Check Sewage Business Rules", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
        testCoverageA();
    }
}

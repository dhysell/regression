package regression.r2.noclock.policycenter.submission_property.subgroup2;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;

import repository.driverConfiguration.Config;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.Building.ValuationMethod;
import repository.gw.enums.CoverageType;
import repository.gw.enums.FPP.FPPCoverageTypes;
import repository.gw.enums.FPP.FPPDeductible;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.FPP.FarmPersonalPropertyTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.LivestockScheduledItemType;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.FoundationType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireFPP;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderLineReviewPL;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

public class TestSquirePropertyLiabilityClassificationForRating extends BaseTest {

    private GeneratePolicy myPolicyObjPL = null;
    private Underwriters underwriter = new Underwriters();
    private WebDriver driver;

    @BeforeMethod
    public void beforeMethod() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
    }





    /*
     * jlarsen 6/9/2017
     * Combined these 3 tests into one and will run a random squire type each night. save an hour of runtime
     */


    @Test(enabled = true)
    public void testPropertyClassificationForRating() throws Exception {


        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        Login login = new Login(driver);
        myPolicyObjPL = createPLPropertyAutoPolicy(GeneratePolicyType.QuickQuote, SquireEligibility.random());
        Agents agent = myPolicyObjPL.agentInfo;

        login.loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), myPolicyObjPL.accountNumber);

        // Adding locations to add multiple properties
        addMultipleResidences(2);

        if (!myPolicyObjPL.squire.squireEligibility.equals(SquireEligibility.City)) {
            // Adding FPP LiveStock Validations
            addingFPPLoveStockQuoteErrors();
        }
        // Validating the quote errors
        addingPropertiesValidateQuoteErrors();

        // Login with Underwriter
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();
        guidewireHelpers.logout();

        underwriter = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        login.loginAndSearchSubmission(underwriter.getUnderwriterUserName(), underwriter.getUnderwriterPassword(), myPolicyObjPL.accountNumber);

        // Adding Rated year
        addingRatedYearValue();
    }


    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Requirement
     * @RequirementsLink <a href="http:// "
     * https://rally1.rallydev.com/#/33274298124d/detail/
     * userstory/34235056456</a>
     * @Description :US4756 ( only quote errors validations covered and rated
     * Year
     * @DATE Mar 29, 2016
     */
    @Test(enabled = false)
    public void testPropertyClassificationForRating_City() throws Exception {

        // Creating policy
        myPolicyObjPL = createPLPropertyAutoPolicy(GeneratePolicyType.QuickQuote, SquireEligibility.City);
        Agents agent = myPolicyObjPL.agentInfo;

        Login login = new Login(driver);
        // Login with agent
        login.loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), myPolicyObjPL.accountNumber);

        // Adding locations to add multiple properties
        addMultipleResidences(2);

        // Validating the quote errors
        addingPropertiesValidateQuoteErrors();

        // Login with Underwriter
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();
        new GuidewireHelpers(driver).logout();

        underwriter = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        login.loginAndSearchSubmission(underwriter.getUnderwriterUserName(), underwriter.getUnderwriterPassword(), myPolicyObjPL.accountNumber);

        // Adding Rated year
        addingRatedYearValue();
        quote.clickSaveDraftButton();

    }

    @Test(enabled = false)
    public void testPropertyClassificationForRating_County() throws Exception {
        // Creating policy
        myPolicyObjPL = createPLPropertyAutoPolicy(GeneratePolicyType.QuickQuote, SquireEligibility.Country);
        Agents agent = myPolicyObjPL.agentInfo;

        Login login = new Login(driver);
        // Login with agent
        login.loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), myPolicyObjPL.accountNumber);

        // Adding locations to add multiple properties
        addMultipleResidences(2);

        // Adding FPP LiveStock Validations
        addingFPPLoveStockQuoteErrors();

        // Validating the quote errors
        addingPropertiesValidateQuoteErrors();

        // Login with Underwriter
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();
        new GuidewireHelpers(driver).logout();

        underwriter = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        login.loginAndSearchSubmission(underwriter.getUnderwriterUserName(), underwriter.getUnderwriterPassword(), myPolicyObjPL.accountNumber);

        // Adding Rated year
        addingRatedYearValue();
        quote.clickSaveDraftButton();

    }

    @Test(enabled = false)
    public void testPropertyClassificationForRating_FarmAndRanch() throws Exception {
        // Creating policy
        myPolicyObjPL = createPLPropertyAutoPolicy(GeneratePolicyType.QuickQuote, SquireEligibility.FarmAndRanch);
        Agents agent = myPolicyObjPL.agentInfo;

        Login login = new Login(driver);
        // Login with agent
        login.loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), myPolicyObjPL.accountNumber);

        // Adding locations to add multiple properties
        addMultipleResidences(2);

        // Adding FPP LiveStock Validations
        addingFPPLoveStockQuoteErrors();

        // Validating the quote errors
        addingPropertiesValidateQuoteErrors();

        // Login with Underwriter
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();
        new GuidewireHelpers(driver).logout();

        underwriter = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        login.loginAndSearchSubmission(underwriter.getUnderwriterUserName(), underwriter.getUnderwriterPassword(), myPolicyObjPL.accountNumber);

        // Adding Rated year
        addingRatedYearValue();
        quote.clickSaveDraftButton();
    }

    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Description : adding Rated Year with underwriter login
     * @DATE Mar 29, 2016
     */
    private void addingRatedYearValue() throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();

        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
        propertyDetail.clickViewOrEditBuildingButton(1);
        propertyDetail.clickPropertyConstructionTab();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        constructionPage.setRatedYear(2015);
        constructionPage.clickOK();

        GenericWorkorderLineReviewPL lineReview = new GenericWorkorderLineReviewPL(driver);

        sideMenu.clickSideMenuSquirePropertyCoverages();
        sideMenu.clickSideMenuSquirePropertyGlLineReview();
        boolean valueFound = false;
        int rowCount = lineReview.getPropertyCoverageTableRowCount();
        for (int currentRow = 1; currentRow <= rowCount; currentRow++) {
            String propertyType = lineReview.getPropertyCoverageTableCellByRowNumberColumnName(currentRow, "Property Type");
            System.out.println(propertyType);
            String ActualpropertyType = this.myPolicyObjPL.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getpropertyType().getValue();
            if (ActualpropertyType.equals(propertyType)) {
                if (lineReview.getPropertyCoverageTableCellByRowNumberColumnName(currentRow, "Rated Yr.").contains("2015")) {
                    valueFound = true;
                    break;
                }
            }
        }

        if (!valueFound) {
            Assert.fail("Expected Rated Year is not available in Line Review");
        }

        sideMenu.clickSideMenuSquirePropertyCoverages();

    }

    /**
     * @param i
     * @throws Exception
     * @Author nvadlamudi
     * @Description : adding no. of residences
     * @DATE Mar 29, 2016
     */
    private void addMultipleResidences(int i) throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuHouseholdMembers();
        new GuidewireHelpers(driver).editPolicyTransaction();

        sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        propertyLocations.clickEditLocation(1);
//        propertyLocations.setNumberOfResidence(i);
        propertyLocations.clickOK();
    }

    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Description : FPP validations
     * @DATE Apr 1, 2016
     */
    private void addingFPPLoveStockQuoteErrors() throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        boolean testFailed = false;
        String errorMessage = "";

        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        coverages.clickSectionIICoveragesTab();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages section2Covs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
        section2Covs.addCoverages(new SectionIICoverages(SectionIICoveragesEnum.Livestock, 0, 0));
        section2Covs.setLivestockTypeAndQuantity(LivestockScheduledItemType.Donkey, 2);
        coverages.clickFarmPersonalProperty();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages fpp = new GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages(driver);
        fpp.checkCoverageD(true);
        fpp.selectCoverageType(FPPCoverageTypes.BlanketInclude);
        fpp.selectDeductible(FPPDeductible.Ded_1000);
        // validation for "Livestock added to Section II liability, and FPP
        // coverage type is blanket include without Livestock. Add Livestock to
        // FPP"
        String valMeg1 = "Livestock added to Section II liability, and FPP coverage type is blanket include without Livestock. Add Livestock to FPP";
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();
        String validationMessages = risk.getValidationMessagesText();

        if (!validationMessages.contains(valMeg1)) {
            testFailed = false;
            errorMessage = errorMessage + "Expected Validation message : '" + valMeg1 + "' is not displayed.";
        }
        risk.clickClearButton();
        sideMenu.clickSideMenuSquirePropertyCoverages();
        coverages.clickSectionIICoveragesTab();
        section2Covs.clickLiveStockCheckBox();
        coverages.clickFarmPersonalProperty();
        fpp.selectCoverages(FarmPersonalPropertyTypes.Livestock);
        coverages.clickQuote();

        String valMeg2 = "FPP includes livestock, add livestock to Section II - liability";

        validationMessages = risk.getValidationMessagesText();

        if (!validationMessages.contains(valMeg2)) {
            testFailed = false;
            errorMessage = errorMessage + "Expected Validation message : '" + valMeg2 + "' is not displayed.";
        }
        risk.clickClearButton();
        fpp.unselectCoverages(FarmPersonalPropertyTypes.Livestock);
        fpp.checkCoverageD(false);
        sideMenu.clickSideMenuSquirePropertyCoverages();
        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + errorMessage);
        }

    }

    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Description : Adding multiple properties and checking validations
     * @DATE Mar 29, 2016
     */
    private void addingPropertiesValidateQuoteErrors() throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        boolean testFailed = false;
        String errorMessage = "";

        // adding multiple properties
        sideMenu.clickSideMenuSquirePropertyDetail();

        // changing values for 'If Property Type = Residence Premises and
        // Construction Type = (Frame or Non-Frame); then Limit >= 15,000'
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        propertyDetail.clickViewOrEditBuildingButton(1);
        propertyDetail.clickPropertyConstructionTab();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        constructionPage.setConstructionType(ConstructionTypePL.NonFrame);
        constructionPage.clickOK();

        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverages.setCoverageALimit(14000);
        String valMeg1 = "For Non-Frame Residence Premises to qualify for Coverage A, the limit must be greater or equal to $15,000. If not then please move the property to Coverage E";

        // adding another Residence premises for 'If Property Type = Residence
        // Premises and Construction Type = Modular/Manufactured; then Limit >=
        // 40,000'
        addNewPropertyCoverageLimit(myPolicyObjPL.basicSearch, propertyDetail, PropertyTypePL.ResidencePremises, ConstructionTypePL.ModularManufactured, 30000, CoverageType.BroadForm, FoundationType.FullBasement);
        String valMeg2 = "For Modular/Manufactered Residence Premises to qualify for Coverage A, the limit must be greater or equal to $40,000. If not then please move the property to Coverage E";

        // adding property for 'If Property Type = Residence Premises,
        // Construction Type = Mobile Home, and Foundation Type != None; then
        // Limit >= 15,000'
        addNewPropertyCoverageLimit(myPolicyObjPL.basicSearch, propertyDetail, PropertyTypePL.ResidencePremises, ConstructionTypePL.MobileHome, 7000, null, FoundationType.FullBasement);
        String valMeg3 = "For Mobile Home with foundation Residence Premises to qualify for Coverage A, the limit must be greater or equal to $15,000. If not then please move the property to Coverage E";

        // adding property for 'If Property Type = Residence Premises,
        // Construction Type = Mobile Home, and Foundation Type = None; then
        // Limit >= 4,000'
        addNewPropertyCoverageLimit(myPolicyObjPL.basicSearch, propertyDetail, PropertyTypePL.ResidencePremises, ConstructionTypePL.MobileHome, 3000, null, FoundationType.None);
        String valMeg4 = "For Mobile Home without foundation Residence Premises to qualify for Coverage A, the limit must be greater or equal to $15,000. If not then please move the property to Coverage E";

        // adding property for 'If Property Type = Condominium Residence
        // Premises and Construction Type = (Frame or Non-Frame); then Limit >=
        // 40,000'
        addNewPropertyCoverageLimit(myPolicyObjPL.basicSearch, propertyDetail, PropertyTypePL.CondominiumResidencePremise, ConstructionTypePL.Frame, 30000, CoverageType.BroadForm, FoundationType.FullBasement);
        String valMeg5 = "For Frame Condominium Residence Premises to qualify for Coverage A, the limit must be greater or equal to $40,000";

        // adding property for 'If Property Type = Vacation Home and
        // Construction Type = (Frame or Non-Frame); then Limit >= 15,000'
        addNewPropertyCoverageLimit(myPolicyObjPL.basicSearch, propertyDetail, PropertyTypePL.VacationHome, ConstructionTypePL.Frame, 3000, CoverageType.BroadForm, FoundationType.FullBasement);
        String valMeg6 = "For Frame Vacation Home to qualify for Coverage A, the limit must be greater or equal to $15,000. If not then please move the property to Coverage E";

        // adding property for 'If Property Type = Vacation Home and
        // Construction Type = Modular/Manufactured; then Limit >=40,000'
        addNewPropertyCoverageLimit(myPolicyObjPL.basicSearch, propertyDetail, PropertyTypePL.VacationHome, ConstructionTypePL.ModularManufactured, 30000, CoverageType.BroadForm, FoundationType.FullBasement);
        String valMeg7 = "For Modular/Manufactered Vacation Home to qualify for Coverage A, the limit must be greater or equal to $40,000. If not then please move the property to Coverage E";

        // adding property for 'If Property Type = Vacation Home, Construction
        // Type = Mobile Home, and Foundation Type != None; then Limit >=
        // 15,000'
        //addNewPropertyCoverageLimit(propertyDetail, PropertyTypePL.VacationHome, ConstructionTypePL.MobileHome, 8000, null, FoundationType.FullBasement);
        //String valMeg8 = "For Mobile Home with foundation Vacation Home to qualify for Coverage A, the limit must be greater or equal to $15,000. If not then please move the property to Coverage E";

        // adding property for 'If Property Type = Condominium Vacation Home and
        // Construction Type = (Frame or Non-Frame); then Limit >= 20,000'
        addNewPropertyCoverageLimit(myPolicyObjPL.basicSearch, propertyDetail, PropertyTypePL.CondominiumVacationHome, ConstructionTypePL.NonFrame, 10000, CoverageType.BroadForm, FoundationType.FullBasement);
        String valMeg9 = "For Non-Frame Condominium Vacation Home to qualify for Coverage A, the limit must be greater or equal to $20,000. If not then please move the property to Coverage E";

        // adding property for 'If Property Type = Dwelling Premises; then Limit
        // >= 20,000'
        addNewPropertyCoverageLimit(myPolicyObjPL.basicSearch, propertyDetail, PropertyTypePL.DwellingPremises, ConstructionTypePL.NonFrame, 15000, CoverageType.BroadForm, FoundationType.FullBasement);
        String valMeg10 = "For Dwelling Premises to qualify for Coverage A, the limit must be greater or equal to $20,000. If not then please move the property to Coverage E";

        // adding property for 'If Property Type = Condominium Dwelling Premises
        // and Construction Type = (Frame or Non-Frame); then Limit >= 20,000'
        addNewPropertyCoverageLimit(myPolicyObjPL.basicSearch, propertyDetail, PropertyTypePL.CondominiumDwellingPremises, ConstructionTypePL.Frame, 10000, CoverageType.BroadForm, FoundationType.FullBasement);
        String valMeg11 = "For Frame Condominium Dwelling Premises to qualify for Coverage A, the limit must be greater or equal to $20,000";

        // adding property for 'If Property Type = Dwelling Under Construction;
        // then Limit >= 40,000'
        addNewPropertyCoverageLimit(myPolicyObjPL.basicSearch, propertyDetail, PropertyTypePL.DwellingUnderConstruction, ConstructionTypePL.Frame, 30000, null, FoundationType.FullBasement);
        String valMeg12 = "For Dwelling Under Construction to qualify for Coverage A, the limit must be greater or equal to $40,000. If not then please move the property to Coverage E";

        // adding property for 'Section I deductible of $5,000 or more requires
        // at least 1 Property Type of (Residence Premises or Condominium
        // Residence Premises) with coverage A limit >= $200,000'
        sideMenu.clickSideMenuSquirePropertyCoverages();
        coverages.selectSectionIDeductible(SectionIDeductible.FiveThousand);
        addNewPropertyCoverageLimit(myPolicyObjPL.basicSearch, propertyDetail, PropertyTypePL.CondominiumResidencePremise, ConstructionTypePL.Frame, 100000, CoverageType.BroadForm, FoundationType.FullBasement);
        String valMeg13 = "Section I deductible of $5,000 or more requires at least 1 Coverage A Residence Premises or Condominium Residence Premises with limit greater or equal to $200,000";

        // Adding property for 'Residence Premises to qualify for Coverage A
        // built after 1968, the limit must be greater or equal to $40,000. If
        // not then please move the property to Coverage E'

        // No. of residences
        String valMeg14 = "Please correct the number of residences for Location";

        // Clicking on quote to validate all the validation messages
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();

        String validationMessages = risk.getValidationMessagesText();

        if (!validationMessages.contains(valMeg1)) {
            testFailed = false;
            errorMessage = errorMessage + "Expected Validation message : '" + valMeg1 + "' is not displayed.";
        }

        if (!validationMessages.contains(valMeg2)) {
            testFailed = false;
            errorMessage = errorMessage + "Expected Validation message : '" + valMeg2 + "' is not displayed.";
        }

        if (!validationMessages.contains(valMeg3)) {
            testFailed = false;
            errorMessage = errorMessage + "Expected Validation message : '" + valMeg3 + "' is not displayed.";
        }

        if (!validationMessages.contains(valMeg4)) {
            testFailed = false;
            errorMessage = errorMessage + "Expected Validation message : '" + valMeg4 + "' is not displayed.";
        }

        if (!validationMessages.contains(valMeg5)) {
            testFailed = false;
            errorMessage = errorMessage + "Expected Validation message : '" + valMeg5 + "' is not displayed.";
        }

        if (!validationMessages.contains(valMeg6)) {
            testFailed = false;
            errorMessage = errorMessage + "Expected Validation message : '" + valMeg6 + "' is not displayed.";
        }

        if (!validationMessages.contains(valMeg7)) {
            testFailed = false;
            errorMessage = errorMessage + "Expected Validation message : '" + valMeg7 + "' is not displayed.";
        }

        //Construction type : Mobile Home option is not displayed.
		/*if(!validationMessages.contains(valMeg8)) {
			testFailed = false;
			errorMessage = errorMessage + "Expected Validation message : '" + valMeg8 + "' is not displayed.";
		}*/

        if (!validationMessages.contains(valMeg9)) {
            testFailed = false;
            errorMessage = errorMessage + "Expected Validation message : '" + valMeg9 + "' is not displayed.";
        }

        if (!validationMessages.contains(valMeg10)) {
            testFailed = false;
            errorMessage = errorMessage + "Expected Validation message : '" + valMeg10 + "' is not displayed.";
        }

        if (!validationMessages.contains(valMeg11)) {
            testFailed = false;
            errorMessage = errorMessage + "Expected Validation message : '" + valMeg11 + "' is not displayed.";
        }

        if (!validationMessages.contains(valMeg12)) {
            testFailed = false;
            errorMessage = errorMessage + "Expected Validation message : '" + valMeg12 + "' is not displayed.";
        }

        if (!validationMessages.contains(valMeg13)) {
            testFailed = false;
            errorMessage = errorMessage + "Expected Validation message : '" + valMeg13 + "' is not displayed.";
        }

        if (!validationMessages.contains(valMeg14)) {
            testFailed = false;
            errorMessage = errorMessage + "Expected Validation message : '" + valMeg14 + "' is not displayed.";
        }

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + errorMessage);
        }

    }


    /**
     * @param propertyDetail
     * @param propertyType
     * @param constructionType
     * @param limit
     * @param CType
     * @param foundationType
     * @throws Exception
     * @Author nvadlamudi
     * @Description : adding property and coverage limit
     * @DATE Mar 29, 2016
     */
    private void addNewPropertyCoverageLimit(boolean basicSearch, GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail, PropertyTypePL propertyType, ConstructionTypePL constructionType, double limit, CoverageType CType, FoundationType foundationType) throws Exception {

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        PLPolicyLocationProperty property = new PLPolicyLocationProperty(propertyType);
        property.setConstructionType(constructionType);
        property.setFoundationType(foundationType);

        if (propertyType.equals(PropertyTypePL.DwellingUnderConstruction)) {
            int yearField = DateUtils.getCenterDateAsInt(driver, ApplicationOrCenter.PolicyCenter, "yyyy");
            System.out.println(" Year " + ClockUtils.getCurrentDates(driver).get(ApplicationOrCenter.PolicyCenter));
            property.setYearBuilt(yearField);
        }

        propertyDetail.fillOutPropertyDetails_QQ(basicSearch, property);

        GenericWorkorderSquirePropertyDetailConstruction constructionPage = new GenericWorkorderSquirePropertyDetailConstruction(driver);
        constructionPage.setCoverageAPropertyDetailsQQ(property);
        GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);

        if (!propertyType.equals(PropertyTypePL.DwellingUnderConstruction)) {
            constructionPage.clickProtectionDetails();
            protectionPage.setProtectionPageQQ(property);
        }
        protectionPage.clickOK();
        int BuildingNumber = propertyDetail.getSelectedBuildingNum();

        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverages.clickSpecificBuilding(1, BuildingNumber);
        coverages.setCoverageALimit(limit);
        try {
            coverages.setCoverageCValuation(property.getPropertyCoverages());
        } catch (Exception e) {
        }

        if (CType != null && !property.getPropertyCoverages().getCoverageC().getValuationMethod().equals(ValuationMethod.ActualCashValue) && (property.getYearBuilt() > 1954 && !property.getConstructionType().equals(ConstructionTypePL.MobileHome)))
            coverages.selectCoverageCCoverageType(CType);

    }

    /**
     * @param policyType
     * @param eligibilityType
     * @return
     * @throws Exception
     * @Author nvadlamudi
     * @Description : generating quick quote
     * @DATE Mar 29, 2016
     */
    private GeneratePolicy createPLPropertyAutoPolicy(GeneratePolicyType policyType, SquireEligibility eligibilityType) throws Exception {

        // Coverages
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow, MedicalLimit.TenK);
        coverages.setUnderinsured(false);
        coverages.setAccidentalDeath(true);

        // driver
        ArrayList<Contact> driversList = new ArrayList<Contact>();
        Contact person = new Contact("Test", "AutoRegression", Gender.Male, DateUtils.convertStringtoDate("01/01/1979", "MM/dd/YYYY"));
        person.setMaritalStatus(MaritalStatus.Married);
        person.setRelationToInsured(RelationshipToInsured.Insured);
        person.setOccupation("Software");
        driversList.add(person);

        // Vehicle
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle toAdd = new Vehicle();
        toAdd.setEmergencyRoadside(true);
        vehicleList.add(toAdd);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locationsList.add(locToAdd);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);
        squirePersonalAuto.setVehicleList(vehicleList);
        squirePersonalAuto.setDriversList(driversList);

        SquireFPP squireFPP = new SquireFPP(FPPFarmPersonalPropertySubTypes.Tractors, FPPFarmPersonalPropertySubTypes.HarvestersHeaders, FPPFarmPersonalPropertySubTypes.CirclePivots, FPPFarmPersonalPropertySubTypes.WheelLines);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;
        myPropertyAndLiability.squireFPP = squireFPP;


        Squire mySquire = new Squire(eligibilityType);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;


        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Test", "Propclassforrating")
                .build(policyType);

        return myPolicyObjPL;
    }

}

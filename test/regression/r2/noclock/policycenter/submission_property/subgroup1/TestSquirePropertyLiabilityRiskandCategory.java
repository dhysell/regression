package regression.r2.noclock.policycenter.submission_property.subgroup1;

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
import repository.gw.enums.FPP.FPPCoverageTypes;
import repository.gw.enums.FPP.FPPDeductible;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.FPP.FarmPersonalPropertyTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.FoundationType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.RiskCategoryCode;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
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
 * @Requirement : US7019 - PL - Risk & Category, US12581: PL - Limit the Risk field on Property to 2 characters
 * @RequirementsLink <a href="http://rally1.rallydev.com/#/33274298124d/detail/userstory/51503929657" </a>
 * @Description : Script will generate squire, standard fire and squire with Standard fire policies
 * and then UW adds the risk to the properties and check the category codes , checking risk field is accepting only alphabets and up to 2 characters alone
 * @DATE May 24, 2016
 */
@QuarantineClass
public class TestSquirePropertyLiabilityRiskandCategory extends BaseTest {

    private GeneratePolicy myPolicyObjPL = null;
    private GeneratePolicy stdFireLiab_Fire_PolicyObj1;
    private Underwriters uw;

    private WebDriver driver;

    //validate Risk and Category for Squire Policy
    @Test
    public void SquireFPPRiskCategoryCodes() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        myPolicyObjPL = createPLPropertyAutoPolicy(GeneratePolicyType.FullApp);
        Agents agent = myPolicyObjPL.agentInfo;
        Login login = new Login(driver);
        login.loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), myPolicyObjPL.accountNumber);

        validateSquireReadOnlyRiskCategoryCodes();

        enterFPPCoverages();
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        new GuidewireHelpers(driver).logout();
        login.loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.accountNumber);

        enterValidateRiskCategoryDetails();
    }

    //validate Risk and Category code for SquirewithStandardFire policy
    @Test(dependsOnMethods = {"testGenerateSquirewithStandardFireFullApp"})
    public void validateSquireStandardFirePolicyRiskCategoryCodes() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(), this.myPolicyObjPL.accountNumber);

        new GuidewireHelpers(driver).logout();
        enterValidateRiskCategoryDetailsforFire(myPolicyObjPL);

    }

    //validate Risk and Category code for StandardFire  only
    @Test(dependsOnMethods = {"testGenerateStandardFireFullApp"})
    public void FirePolicyRiskCategoryCodes() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(stdFireLiab_Fire_PolicyObj1.agentInfo.getAgentUserName(), stdFireLiab_Fire_PolicyObj1.agentInfo.getAgentPassword(), this.stdFireLiab_Fire_PolicyObj1.accountNumber);

        new GuidewireHelpers(driver).logout();
        enterValidateRiskCategoryDetailsforFire(stdFireLiab_Fire_PolicyObj1);

    }

    //add Risk with UW logon
    private void enterValidateRiskCategoryDetailsforFire(GeneratePolicy policy) throws Exception {

        boolean testFailed = false;
        String errorMessage = " ";
        RiskCategoryCode categoryCode = RiskCategoryCode.random();

        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), policy.accountNumber);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();

        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        propertyDetail.clickViewOrEditBuildingButton(1);
        propertyDetail.setRisk("123");
        propertyDetail.clickOk();

        if (!guidewireHelpers.errorMessagesExist() && !guidewireHelpers.getFirstErrorMessage().contains("Risk : Must contain only letters (A-Z) and be 1-2 in length")) {
            testFailed = true;
            errorMessage = errorMessage + "Expected Mandatory Validation for entering in Risk not displayed \n";
        }
        if (!propertyDetail.getRisk().contains("12")) {
            testFailed = true;
            errorMessage = errorMessage + "Risk field accepting more than 2 digits \n";
        }
        propertyDetail.setRisk("@#");
        propertyDetail.clickOk();
        if (!guidewireHelpers.errorMessagesExist() && !guidewireHelpers.getFirstErrorMessage().contains("Risk : Must contain only letters (A-Z) and be 1-2 in length")) {
            testFailed = true;
            errorMessage = errorMessage + "Expected Mandatory Validation for special characters in Risk not displayed \n";
        }
        propertyDetail.setRisk("A");
        propertyDetail.clickPropertyInformationDetailsTab();
        propertyDetail.clickOverrideCategoryCodeCheck();
        propertyDetail.setCategoryCodeReason(categoryCode.getValue(), "Testing purpose");
        propertyDetail.clickOk();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();
        quote.clickQuote();

        sideMenu.clickSideMenuSquirePropertyDetail();

        propertyDetail.clickViewOrEditBuildingButton(1);

        if (!propertyDetail.getReadOnlyRiskValue().contains("A")) {
            testFailed = true;
            errorMessage = "Unexpected Risk Code " + propertyDetail.getReadOnlyRiskValue();
        }
        if (!propertyDetail.getReadOnlyOverrideCategoryCode().contains("Yes")) {
            testFailed = true;
            errorMessage = "Unexpected Override Category Code " + propertyDetail.getReadOnlyOverrideCategoryCode();
        }
        if (!propertyDetail.getReadOnlyCategoryCode().equals(categoryCode.getValue())) {
            testFailed = true;
            errorMessage = "Unexpected Category Code " + propertyDetail.getReadOnlyCategoryCode();
        }

        propertyDetail.clickReturnToPropertyDetail();

        GenericWorkorderRiskAnalysis ra = new GenericWorkorderRiskAnalysis(driver);
        ra.clickReleaseLock();
        if (testFailed) {
            Assert.fail(errorMessage);
        }

    }

    //add risk and set category codes with UW
    private void enterValidateRiskCategoryDetails() throws Exception {
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();

        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        addNewPropertyCoverageLimit(propertyDetail, PropertyTypePL.VacationHome, 15000, CoverageType.BroadForm);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();

        propertyDetail.clickViewOrEditBuildingButton(2);
        propertyDetail.setRisk("123");
        propertyDetail.clickOk();
        String errorMessage = "";
        if (!guidewireHelpers.errorMessagesExist() && !guidewireHelpers.getFirstErrorMessage().contains("Risk : Must contain only letters (A-Z) and be 1-2 in length")) {
            errorMessage = errorMessage + "Expected Mandatory Validation for entering in Risk not displayed \n";
        }
        if (!propertyDetail.getRisk().contains("12")) {
            errorMessage = errorMessage + "Risk field accepting more than 2 digits \n";
        }
        propertyDetail.setRisk("@#");
        propertyDetail.clickOk();
        if (!guidewireHelpers.errorMessagesExist() && !guidewireHelpers.getFirstErrorMessage().contains("Risk : Must contain only letters (A-Z) and be 1-2 in length")) {
            errorMessage = errorMessage + "Expected Mandatory Validation for special characters in Risk not displayed \n";
        }
        propertyDetail.setRisk("B");
        propertyDetail.clickOverrideCategoryCodeCheck();
        propertyDetail.setCategoryCodeReason(RiskCategoryCode.random().getValue(), "Testing purpose");
        propertyDetail.clickOk();


        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }

    }

    //add FPP coverages for Squire Policy
    private void enterFPPCoverages() throws Exception {

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        coverages.clickFarmPersonalProperty();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages fpp = new GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages(driver);
        fpp.checkCoverageD(true);
        fpp.selectCoverageType(FPPCoverageTypes.BlanketInclude);
        fpp.selectDeductible(FPPDeductible.Ded_1000);

        fpp.selectCoverages(FarmPersonalPropertyTypes.Machinery);
        fpp.addItem(FPPFarmPersonalPropertySubTypes.Tractors, 200, 50000, "Testing purpose");

        fpp.selectCoverages(FarmPersonalPropertyTypes.IrrigationEquipment);
        fpp.addItem(FPPFarmPersonalPropertySubTypes.HandLines, 200, 2500, "Testing purpose");

        fpp.selectCoverages(FarmPersonalPropertyTypes.Tools);
        fpp.addItem(FPPFarmPersonalPropertySubTypes.PowerTools, 200, 5000, "Testing purpose");

        fpp.selectCoverages(FarmPersonalPropertyTypes.Commodities);
        fpp.addItem(FPPFarmPersonalPropertySubTypes.Grain, 200, 50000, "Testing purpose");

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickQuote();
        new GuidewireHelpers(driver).logout();

    }

    private void validateSquireReadOnlyRiskCategoryCodes() {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        boolean testFailed = false;
        String errorMessage = " ";

        new GuidewireHelpers(driver).editPolicyTransaction();

        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);

        sideMenu.clickSideMenuSquirePropertyDetail();
        propertyDetail.clickViewOrEditBuildingButton(1);

        if (!propertyDetail.getReadOnlyRiskValue().contains("A")) {
            testFailed = true;
            errorMessage = "Unexpected Risk Code " + propertyDetail.getReadOnlyRiskValue();
        }

        if (!propertyDetail.getReadOnlyOverrideCategoryCode().contains("No")) {
            testFailed = true;
            errorMessage = "Unexpected Override Category Code " + propertyDetail.getReadOnlyOverrideCategoryCode();
        }

        if (propertyDetail.getReadOnlyCategoryCode() == "") {
            testFailed = true;
            errorMessage = "Unexpected Category Code " + propertyDetail.getReadOnlyCategoryCode();
        }
        propertyDetail.clickOk();

        if (testFailed) {
            Assert.fail(errorMessage);
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
            constructionPage.setLargeShed(false);
        }
        constructionPage.clickProtectionDetailsTab();
        GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);
        new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails(driver).fillOutPropertyProtectionDetails(property);
        protectionPage.clickOK();
        int buildingNumber = propertyDetail.getSelectedBuildingNum();
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverages.clickSpecificBuilding(1, buildingNumber);

        if (propertyType.equals(PropertyTypePL.Contents)) {
            coverages.setCoverageCLimit(limit);
        } else {
            coverages.setCoverageALimit(limit);
        }

        if (cType != null) {
            if (propertyType.toString().contains("CovE")) {
                coverages.setCoverageECoverageType(cType);
            } else {
                coverages.selectCoverageCCoverageType(cType);
                coverages.setCoverageAIncreasedReplacementCost(property.getPropertyCoverages().getCoverageA().isIncreasedReplacementCost());
                coverages.setCoverageCValuation(property.getPropertyCoverages());
            }
        }


    }

    private GeneratePolicy createPLPropertyAutoPolicy(GeneratePolicyType policyType) throws Exception {

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

        Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Test", "RiskCategory")
                .withPaymentPlanType(PaymentPlanType.Quarterly)
                .withDownPaymentType(PaymentType.Cash)
                .build(policyType);

        return myPolicyObjPL;
    }


    @Test()
    public void testGenerateStandardFireFullApp() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.VacationHome));
        locationsList.add(new PolicyLocation(locOnePropertyList));

        this.stdFireLiab_Fire_PolicyObj1 = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withInsFirstLastName("Guy", "Stdfire")
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withPolicyLocations(locationsList)
                .build(GeneratePolicyType.FullApp);

    }

    @Test()
    public void testGenerateSquirewithStandardFireFullApp() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        myPolicyObjPL = createPLPropertyAutoPolicy(GeneratePolicyType.FullApp);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.GrainSeed));
        locationsList.add(new PolicyLocation(locOnePropertyList));

        StandardFireAndLiability myStandardFire = new StandardFireAndLiability();
        myStandardFire.setLocationList(locationsList);
        myStandardFire.setStdFireCommodity(true);

        myPolicyObjPL.standardFire = myStandardFire;
        myPolicyObjPL.lineSelection.add(LineSelection.StandardFirePL);
        myPolicyObjPL.addLineOfBusiness(ProductLineType.StandardFire, GeneratePolicyType.FullApp);
    }


}

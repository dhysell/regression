package regression.r2.noclock.policycenter.submission_fire_im.subgroup3;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.FoundationType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PLPropertyCoverages;
import repository.gw.generate.custom.PolicyLocation;
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
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement :US7073
 * @RequirementsLink <a href="http://
 * "rally1.rallydev.com/#/33274298124d/detail/userstory/51876394463</a>
 * @Description
 * @DATE Apr 22, 2016
 */
public class TestSquireStandardFirePropertyLiabilityPhotoMSYear extends BaseTest {

    private GeneratePolicy myPolicyObjPL = null;
    private GeneratePolicy standardfirePolicy = null;
    private Underwriters uw;
    private WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
    }



    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Description : adding more properties and checking the MSYear and Photo
     * Year fields with UW and Agent login
     * @DATE Apr 22, 2016
     */
    @Test(dependsOnMethods = "createStandardFirePolicy")
    public void validateStandardFireMSPhotoYearFields() throws Exception {
        // Login to the newly created account
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                standardfirePolicy.accountNumber);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();

        // Adding MSYear and Photo Year for all the properties entered
        AddingMultiplePropertiesPhotoMSYear(standardfirePolicy);

        // Logout with UW to login again with Agent
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();
        guidewireHelpers.logout();

        // login with agent and verify
        new Login(driver).loginAndSearchSubmission(standardfirePolicy.agentInfo.getAgentUserName(),
                standardfirePolicy.agentInfo.getAgentPassword(), standardfirePolicy.accountNumber);

        // validate agent login and check MS & Photo year fields
        validateMSPhotoYearNotEditableForAgent();

        quote.clickSaveDraftButton();
        guidewireHelpers.logout();

    }

    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Description : Creating Standard Fire policy for validating
     * @DATE Apr 22, 2016
     */
    @Test()
    public void createStandardFirePolicy() throws Exception {
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        PLPolicyLocationProperty property = new PLPolicyLocationProperty();
        property.setpropertyType(PropertyTypePL.DwellingPremises);
        property.setConstructionType(ConstructionTypePL.Frame);
        property.setFoundationType(FoundationType.FullBasement);
        locOnePropertyList.add(property);
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locationsList.add(locToAdd);

        standardfirePolicy = new GeneratePolicy.Builder(driver).withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL).withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Person).withInsFirstLastName("Test", "MSPhotoYear")
                .withInsAge(26).withPolOrgType(OrganizationType.Individual).withPolicyLocations(locationsList)
                .build(GeneratePolicyType.QuickQuote);
    }

    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Description : adding more properties to the Squire and validating with
     * UW & Agent login details
     * @DATE Apr 22, 2016
     */
    @Test(dependsOnMethods = "createPLPropertyAutoPolicy")
    public void validateMSPhotoYear() throws Exception {
        // Login to the newly created account
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);

        Login login = new Login(driver);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        login.loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.accountNumber);

        guidewireHelpers.editPolicyTransaction();

        // Adding MSYear and Photo Year for all the properties entered
        AddingMultiplePropertiesPhotoMSYear(myPolicyObjPL);

        // Logout with UW to login again with Agent
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();
        guidewireHelpers.logout();

        // login with agent and verify
        login.loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(),
                myPolicyObjPL.accountNumber);

        // validate agent login and check MS & Photo year fields
        validateMSPhotoYearNotEditableForAgent();

        quote.clickSaveDraftButton();
        guidewireHelpers.logout();

    }

    /**
     * @throws GuidewireNavigationException
     * @Author nvadlamudi
     * @Description : Validating with agent login and checking no editable
     * fields
     * @DATE Apr 22, 2016
     */
    private void validateMSPhotoYearNotEditableForAgent() throws GuidewireNavigationException {
        boolean testFailed = false;
        String errorMessage = "";
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
        int totalBuildings = propertyDetail.getPropertiesCount();
        for (int currentBuilding = 1; currentBuilding <= totalBuildings; currentBuilding++) {
            propertyDetail.clickViewOrEditBuildingButton(currentBuilding);
            propertyDetail.clickPropertyConstructionTab();
            GenericWorkorderSquirePropertyDetailConstruction constructionPage = new GenericWorkorderSquirePropertyDetailConstruction(driver);
            if (constructionPage.MSPhotoYearExists()) {
                testFailed = true;
                errorMessage = errorMessage + "\n Editable MSYear and Photo exists for building : " + currentBuilding;
            }
            constructionPage.clickOK();
        }

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + errorMessage);
        }
    }

    /**
     * @param myPolicy
     * @throws Exception
     * @Author nvadlamudi
     * @Description : Adding more properties with MS Year & photo Year fields
     * @DATE Apr 22, 2016
     */
    private void AddingMultiplePropertiesPhotoMSYear(GeneratePolicy myPolicy) throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        // adding multiple properties
        sideMenu.clickSideMenuSquirePropertyDetail();

        // Property Type=Residence Premises, Construction Type= Frame or
        // Non-Frame , A+ limit >= 60,000 year > 1968
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        propertyDetail.clickViewOrEditBuildingButton(1);
        propertyDetail.clickPropertyConstructionTab();
        GenericWorkorderSquirePropertyDetailConstruction constructionPage = new GenericWorkorderSquirePropertyDetailConstruction(driver);
        int yearField = DateUtils.getCenterDateAsInt(driver, ApplicationOrCenter.PolicyCenter, "yyyy");
        int monthField = DateUtils.getCenterDateAsInt(driver, ApplicationOrCenter.PolicyCenter, "MM");
        constructionPage.setMSYear(monthField + "/" + yearField);
        constructionPage.setPhotoYear(monthField + "/" + yearField);
        constructionPage.clickOK();

        // Adding another properties and entering the MS&Photo fields
        if (myPolicy.lineSelection.contains(LineSelection.PropertyAndLiabilityLinePL)) {
            addNewPropertyCoverageLimit(myPolicy, propertyDetail, PropertyTypePL.CondominiumResidencePremise, 60000, CoverageType.BroadForm, monthField + "/" + yearField);
            addNewPropertyCoverageLimit(myPolicy, propertyDetail, PropertyTypePL.DwellingPremises, 45000, CoverageType.BroadForm, monthField + "/" + yearField);
            addNewPropertyCoverageLimit(myPolicy, propertyDetail, PropertyTypePL.DwellingUnderConstruction, 60000, null, monthField + "/" + yearField);
            addNewPropertyCoverageLimit(myPolicy, propertyDetail, PropertyTypePL.CondominiumDwellingPremises, 50000, CoverageType.BroadForm, monthField + "/" + yearField);
        }
        addNewPropertyCoverageLimit(myPolicy, propertyDetail, PropertyTypePL.VacationHome, 35000, CoverageType.BroadForm, monthField + "/" + yearField);
        addNewPropertyCoverageLimit(myPolicy, propertyDetail, PropertyTypePL.CondominiumVacationHome, 30000, CoverageType.BroadForm, monthField + "/" + yearField);

    }

    /**
     * @param myPolicy
     * @param propertyDetail
     * @param propertyType
     * @param limit
     * @param CType
     * @param MSPhotoYear
     * @throws Exception
     * @Author nvadlamudi
     * @Description : adding new proerty
     * @DATE Apr 22, 2016
     */
    private void addNewPropertyCoverageLimit(GeneratePolicy myPolicy, GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail, PropertyTypePL propertyType, double limit, CoverageType CType, String MSPhotoYear) throws Exception {

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        PLPolicyLocationProperty property = new PLPolicyLocationProperty(propertyType);
        property.setConstructionType(ConstructionTypePL.Frame);
        property.setFoundationType(FoundationType.FullBasement);
        if (propertyType.equals(PropertyTypePL.DwellingUnderConstruction)) {
            int yearField = DateUtils.getCenterDateAsInt(driver, ApplicationOrCenter.PolicyCenter, "yyyy");
            property.setYearBuilt(yearField);
        }
        propertyDetail.fillOutPropertyDetails_QQ(myPolicy.basicSearch, property);

        GenericWorkorderSquirePropertyDetailConstruction constructionPage = new GenericWorkorderSquirePropertyDetailConstruction(driver);
        constructionPage.setCoverageAPropertyDetailsQQ(property);
        constructionPage.setMSYear(MSPhotoYear);
        constructionPage.setPhotoYear(MSPhotoYear);
        if (myPolicy.lineSelection.contains(LineSelection.PropertyAndLiabilityLinePL)
                && !property.getpropertyType().equals(PropertyTypePL.DwellingUnderConstruction)) {
            constructionPage.clickProtectionDetails();
            GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);
            protectionPage.setProtectionPageQQ(property);
            protectionPage.clickOK();
        } else
            constructionPage.clickOK();

        int BuildingNumber = propertyDetail.getSelectedBuildingNum();

        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverages.clickSpecificBuilding(1, BuildingNumber);
        coverages.setCoverageALimit(limit);
        if (myPolicy.lineSelection.contains(LineSelection.PropertyAndLiabilityLinePL)) {
            if (CType != null) {
                coverages.selectCoverageCCoverageType(CType);
            }
        } else {
            coverages.setCoverageCLimit(limit);
        }

        if (!property.getpropertyType().equals(PropertyTypePL.VacationHome)
                && !property.getpropertyType().equals(PropertyTypePL.DwellingUnderConstruction)
                && !property.getpropertyType().equals(PropertyTypePL.CondominiumVacationHome))
            coverages.setCoverageAIncreasedReplacementCost(
                    property.getPropertyCoverages().getCoverageA().isIncreasedReplacementCost());
        else {
            PLPropertyCoverages propertyCoverages = new PLPropertyCoverages();
            propertyCoverages.getCoverageA().setIncreasedReplacementCost(false);
        }
        coverages.setCoverageCValuation(property.getPropertyCoverages());
    }

    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Description : Creating Squire policy
     * @DATE Apr 22, 2016
     */
    @Test()
    private void createPLPropertyAutoPolicy() throws Exception {

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locToAdd.setPlNumResidence(12);
        locationsList.add(locToAdd);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Test", "MSPhoto")
                .build(GeneratePolicyType.QuickQuote);
    }
}

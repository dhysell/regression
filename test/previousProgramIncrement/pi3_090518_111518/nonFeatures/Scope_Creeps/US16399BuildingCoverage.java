package previousProgramIncrement.pi3_090518_111518.nonFeatures.Scope_Creeps;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.OrganizationType;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.gw.helpers.DateUtils;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author swathiAkarapu
 * @Requirement US16399 , DE8048
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558454824ud/detail/userstory/252311056664">US16399</a>
 *                   <a href="https://rally1.rallydev.com/#/203558454824ud/detail/userstory/252311056664">DE8048</a>
 * @Description
 * US16399
 * As a PolicyCenter user (home office and county office) I need to have the following coverages available on buildings to match what already exists in OLIE. (from conversation and review of PC rules between Lisa and Michelle C.)
 *
 * Steps to get there:
 * As agent start one of each squire policy (city, county, F&R) and standard fire non-commodity policy and a standard fire policy
 * On squire policies add Solar Panels
 * On standard fire policy add Solar Panels and Windmill
 * Per acceptance criteria below try adding appropriate coverages on each building type
 *
 * Acceptance criteria:
 * Ensure that Solar Panels have option for Peril 1, Broad Form, Special Form on squire policies (city, country and F&R) check forms and documents for coverage
 * Ensure that Solar Panels have Peril 1 (only on standard fire non-commodity policy). Check forms and documents for coverage
 * Ensure that Windmill only has option of Peril 1 available on a standard fire policy (01-C) check forms and documents for coverage
 *
 *
 * DE8048
 * Have a squire policy. New submission, policy change and all editable rewrite jobs
 * Add solar panels on every job type
 * Actual: The perils available are Peril 1, Broad Form and Special Form (from US16399 done incorrectly)
 * Expected: The perils available should be Peril 1, Perils 1-9, Broad Form and Special Form
 *
 * @DATE September 24
 */
public class US16399BuildingCoverage extends BaseTest {
    public GeneratePolicy myPolicyObject = null;
    private WebDriver driver;

    private void generatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter );
        driver = buildDriver(cf);
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(repository.gw.enums.OrganizationType.Individual)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withInsFirstLastName("Solar", "panel")
                .isDraft()
                .build(repository.gw.enums.GeneratePolicyType.QuickQuote);
    }

    @Test
    public void verfiyCoverageOptionOnSquare() throws Exception {
        generatePolicy();

        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);

        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        SoftAssert softAssert = new SoftAssert();
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Squire);
        PLPolicyLocationProperty dwellingProp = new PLPolicyLocationProperty(repository.gw.enums.Property.PropertyTypePL.SolarPanels);
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverageSolarPanels= addPropertyandCovergeAndValidateCov(dwellingProp );
        coverageSolarPanels.setCoverageEValuation(repository.gw.enums.Building.ValuationMethod.ActualCashValue);

        List<String> coverageTypes= coverageSolarPanels.getCoverageECoverageTypeValues();
        softAssert.assertTrue(coverageTypes.size() == 5, "There should be  5 Coverage types  but not existing");
        softAssert.assertTrue(coverageTypes.contains(repository.gw.enums.CoverageType.None.getValue()), "This  Coverage type -none  should be available but not Found");
        softAssert.assertTrue(coverageTypes.contains(repository.gw.enums.CoverageType.Peril_1.getValue()), "This  Coverage type -Peril 1  should be available but not Found");
        softAssert.assertTrue(coverageTypes.contains(repository.gw.enums.CoverageType.BroadForm.getValue()), "This  Coverage type -Broad Form  should be available but not Found");
        softAssert.assertTrue(coverageTypes.contains(repository.gw.enums.CoverageType.SpecialForm.getValue()), "This  Coverage type -Special Form  should be available but not Found");
        softAssert.assertTrue(coverageTypes.contains(repository.gw.enums.CoverageType.Peril_1Thru9.getValue()), "This  Coverage type -Peril 1-9  should be available but not Found");
        coverageSolarPanels.setCoverageECoverageType(repository.gw.enums.CoverageType.Peril_1);
        coverageSolarPanels.clickNext();

        softAssert.assertAll();
    }

    private repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages addPropertyandCovergeAndValidateCov(PLPolicyLocationProperty prop1 ) throws GuidewireNavigationException {
        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        pcSideMenu.clickSideMenuSquirePropertyDetail();
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        prop1.getPropertyCoverages().getCoverageE().setCoverageType(repository.gw.enums.CoverageType.Peril_1Thru9);
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propertyDetail1 = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
        propertyDetail1.clickAdd();
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailConstruction constructionPage = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailConstruction(driver);
        propertyDetail.fillOutPropertyDetails(prop1);
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction pconstructionPage = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        pconstructionPage.fillOutPropertyConstruction_QQ(prop1);
        constructionPage.setCoverageAPropertyDetailsQQ(prop1);
        propertyDetail.clickOK();
        propertyDetail.clickOkayIfMSPhotoYearValidationShows();
        prop1.setPropertyNumber(propertyDetail.getSelectedBuildingNum());
        propertyDetail.clickNext();
        coverages.clickSpecificBuilding(1, prop1.getPropertyNumber());
        coverages.setCoverageELimit(prop1.getPropertyCoverages().getCoverageA().getLimit());
        return coverages;

    }



    private void generateStandardFirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(repository.gw.enums.Property.PropertyTypePL.CondominiumVacationHome));
        locationsList.add(new PolicyLocation(locOnePropertyList));
        StandardFireAndLiability myStandardFire = new StandardFireAndLiability();
        myStandardFire.setLocationList(locationsList);
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.StandardFire)
                .withLineSelection(repository.gw.enums.LineSelection.StandardFirePL)
                .withStandardFire(myStandardFire)
                .withInsFirstLastName("solar", "panel")
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .isDraft()
                .build(repository.gw.enums.GeneratePolicyType.QuickQuote);
    }

    @Test
    public void verfiyCoverageOptionOnStandardFire() throws Exception {

       generateStandardFirePolicy();
       new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        SoftAssert softAssert = new SoftAssert();
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByStatus("Draft");

        PLPolicyLocationProperty solarPanel = new PLPolicyLocationProperty(repository.gw.enums.Property.PropertyTypePL.SolarPanels);
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverageSolarPanel=addPropertyandCovergeAndValidateCov(solarPanel);
        List<String> coverageTypes= coverageSolarPanel.getCoverageECoverageTypeValues();
        softAssert.assertTrue(coverageTypes.size() == 3, "There should be  3 Coverage types  but not existing");
        softAssert.assertTrue(coverageTypes.contains(repository.gw.enums.CoverageType.None.getValue()), "This  Coverage type -none  should be available but not Found");
        softAssert.assertTrue(coverageTypes.contains(repository.gw.enums.CoverageType.Peril_1.getValue()), "This  Coverage type -Peril 1  should be available but not Found");
        softAssert.assertTrue(coverageTypes.contains(repository.gw.enums.CoverageType.Peril_1Thru8.getValue()), "This  Coverage type -Peril 1-8  should be available but not Found");
        coverageSolarPanel.setCoverageECoverageType(repository.gw.enums.CoverageType.Peril_1);
        coverageSolarPanel.clickNext();

        PLPolicyLocationProperty windMill = new PLPolicyLocationProperty(repository.gw.enums.Property.PropertyTypePL.Windmill);
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverageWindMill=addPropertyandCovergeAndValidateCov(windMill);
        softAssert.assertEquals(coverageWindMill.getCoverageETypeText(),"Peril 1" , "Peril 1 text is not present");
        coverageWindMill.clickNext();

        softAssert.assertAll();
    }


    private void generatePolicyIssue() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter );
        driver = buildDriver(cf);
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withInsFirstLastName("Solar", "panel")
                .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);
    }

    @Test
    public void verfiyCoverageOptionOnSquarePolicyChange() throws Exception {
        generatePolicyIssue();

        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);

        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.startPolicyChange("first policy Change", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));


        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        SoftAssert softAssert = new SoftAssert();
        pcSideMenu.clickSideMenuPropertyLocations();
        pcSideMenu.clickSideMenuSquirePropertyDetail();


        PLPolicyLocationProperty dwellingProp = new PLPolicyLocationProperty(repository.gw.enums.Property.PropertyTypePL.SolarPanels);
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverageSolarPanels= addPropertyandCovergeAndValidateCov(dwellingProp );
        coverageSolarPanels.setCoverageEValuation(repository.gw.enums.Building.ValuationMethod.ActualCashValue);

        List<String> coverageTypes= coverageSolarPanels.getCoverageECoverageTypeValues();
        softAssert.assertTrue(coverageTypes.size() == 5, "There should be 5 Coverage types  but not existing");
        softAssert.assertTrue(coverageTypes.contains(repository.gw.enums.CoverageType.None.getValue()), "This  Coverage type -none  should be available but not Found");
        softAssert.assertTrue(coverageTypes.contains(repository.gw.enums.CoverageType.Peril_1.getValue()), "This  Coverage type -Peril 1  should be available but not Found");
        softAssert.assertTrue(coverageTypes.contains(repository.gw.enums.CoverageType.BroadForm.getValue()), "This  Coverage type -Broad Form  should be available but not Found");
        softAssert.assertTrue(coverageTypes.contains(repository.gw.enums.CoverageType.SpecialForm.getValue()), "This  Coverage type -Special Form  should be available but not Found");
        softAssert.assertTrue(coverageTypes.contains(repository.gw.enums.CoverageType.Peril_1Thru9.getValue()), "This  Coverage type -Peril 1-9  should be available but not Found");
        coverageSolarPanels.setCoverageECoverageType(repository.gw.enums.CoverageType.Peril_1);
        coverageSolarPanels.clickNext();

        softAssert.assertAll();
    }



}

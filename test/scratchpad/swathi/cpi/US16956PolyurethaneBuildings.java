package scratchpad.swathi.cpi;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.OrganizationType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;

import java.util.Arrays;
import java.util.List;

/**
 * @Author swathiAkarapu
 * @Requirement US16956
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558454824ud/detail/userstory/266246621556">US16956</a>
 * @Description
 * As a PolicyCenter user (Agent/PA, CSR/SA or UW) I want to see the option for polyurethane when adding a Vegetable Warehouse, a Vegetable Cellar, a Shop or a Quonset. I need to see the endorsement (I173) when necessary and the correct rate depending on how questions are answered.
 *
 * Steps to get there:
 * Start a new submission and add the following coverage E outbuildings:
 * Vegetable Warehouse
 * Vegetable Cellar
 * Shop
 * Quonset
 * Questions to read as follows:
 * Is this building insulated with polyurethane? REQUIRED FA
 * Yes (prompts next question)
 * Is it sandwiched? REQUIRED FA if above question is Yes
 * Yes (keeps regular building description and rate) no further questions
 * No (prompts next question) and the Provide description box is required
 * Is open flame coverage wanted? REQUIRED FA if above question is No
 * No (add I173 OPEN FLAME WARRANTY ENDORSEMENT & use regular building rate)
 * Yes (no endorsement is added & is higher building rate (Check rating with Randall & Anthony) Rating is being done and tested by the Systems Team
 * Is this building insulated with polyurethane? REQUIRED FA
 * No (keeps regular coverage E building description rate) no further questions
 *
 *
 *
 * Acceptance criteria:
 * Ensure that Polyurethane is available for Agents, PAs, CSRs/SAs, and Underwriters when adding a Vegetable Warehouse, Vegetable Cellar, Shop, or Quonset
 * Ensure that the new question(Is open flame coverage wanted?) appears for the previously listed buildings
 * Ensure that if the "Is open flame coverage wanted?" answer is No the I173 endorsement infers and rate is regular building rate
 * Ensure that if the "Is open flame coverage wanted?" answer is Yes there is no endorsement inferred and the rate is the higher rated building
 * @DATE November 29, 2018
 */
public class US16956PolyurethaneBuildings extends BaseTest {
    public GeneratePolicy myPolicyObject = null;
    private WebDriver driver;

    private void generatePolicy() throws Exception {
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withInsFirstLastName("Polyurethane", "building")
                .isDraft()
                .build(repository.gw.enums.GeneratePolicyType.FullApp);
    }

    @Test
    public void verfiyPolyurethane() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        generatePolicy();
        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);

        // new Login(driver).loginAndSearchAccountByAccountNumber("tclark", "gw", "310331");
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        SoftAssert softAssert = new SoftAssert();
        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Squire);
        pcSideMenu.clickSideMenuSquirePropertyDetail();
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail pcSquirePropertyPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details pcPropertyDetailsPage = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction pcPropertyConstructionPage = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        int building = 0;
        List<repository.gw.enums.Property.PropertyTypePL> properties = Arrays.asList(repository.gw.enums.Property.PropertyTypePL.VegetableWarehouse, repository.gw.enums.Property.PropertyTypePL.VegetableCellar, repository.gw.enums.Property.PropertyTypePL.Shop, repository.gw.enums.Property.PropertyTypePL.Quonset);
        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        for (repository.gw.enums.Property.PropertyTypePL property : properties) {
            pcSquirePropertyPage.clickAdd();
            pcPropertyDetailsPage.setPropertyType(property);
            pcPropertyDetailsPage.clickPropertyConstructionTab();
            pcPropertyConstructionPage.setYearBuilt((DateUtils.getCenterDateAsInt(driver, ApplicationOrCenter.PolicyCenter, "yyyy") - 2));
            pcPropertyConstructionPage.setSquareFootage(2500);
            pcPropertyConstructionPage.setMeasurement(repository.gw.enums.Measurement.SQFT);
            pcPropertyConstructionPage.setConstructionType(repository.gw.enums.Building.ConstructionTypePL.Frame);
            pcPropertyConstructionPage.setPolyurethaneAndSandwichAndDescription(true, false, "test");
            softAssert.assertTrue(pcPropertyConstructionPage.isOpenFlameCoverageWantedExist(), property.getValue() + "  Should display  'Is open flame coverage wanted?' but not");
            pcPropertyConstructionPage.setOpenFlameCoverageWanted(true);
            pcPropertyConstructionPage.clickOk();
            pcPropertyDetailsPage.clickOkayIfMSPhotoYearValidationShows();
            building = pcPropertyDetailsPage.getSelectedBuildingNum();
            pcPropertyDetailsPage.clickNext();
            repository.pc.workorders.generic.GenericWorkorderSquirePropertyCoverages coverages = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyCoverages(driver);
            coverages.clickSpecificBuilding(1, building);
            coverages.selectSectionIDeductible(repository.gw.enums.Property.SectionIDeductible.FiveHundred);
            coverages.setCoverageELimit(100200);
            coverages.setCoverageECoverageType(repository.gw.enums.CoverageType.BroadForm);
            coverages.setCoverageEValuation(repository.gw.enums.Building.ValuationMethod.ActualCashValue);
            pcSideMenu.clickSideMenuSquirePropertyDetail();
        }
        while (!new GuidewireHelpers(driver).isOnPage("//span[contains(@class, 'g-title') and (text()='Risk Analysis')]")) {
            pcWorkOrder.clickNext();
        }

        pcWorkOrder.clickGenericWorkorderQuote();
        pcSideMenu.clickSideMenuForms();
        repository.pc.workorders.generic.GenericWorkorderForms genericWorkorderForms = new repository.pc.workorders.generic.GenericWorkorderForms(driver);
        List<String> formsDescriptions = genericWorkorderForms.getFormDescriptionsFromTable();
        softAssert.assertFalse(formsDescriptions.contains("Open Flame Warranty Endorsement"), "I173 shouldn't exist if open flame is yes");
        new GuidewireHelpers(driver).editPolicyTransaction();
        pcSideMenu.clickSideMenuSquirePropertyDetail();
        for (repository.gw.enums.Property.PropertyTypePL property : properties) {
            pcSquirePropertyPage.clickViewOrEditBuildingByPropertyType(property);
            pcPropertyDetailsPage.clickPropertyConstructionTab();
            pcPropertyConstructionPage.setOpenFlameCoverageWanted(false);
            pcPropertyConstructionPage.clickOk();
            pcPropertyDetailsPage.clickOkayIfMSPhotoYearValidationShows();
        }
        pcWorkOrder.clickGenericWorkorderQuote();
        pcSideMenu.clickSideMenuForms();
        formsDescriptions = genericWorkorderForms.getFormDescriptionsFromTable();
        softAssert.assertTrue(formsDescriptions.contains("Open Flame Warranty Endorsement"), "I173 should exist if open flame is no");
        softAssert.assertAll();

    }
}

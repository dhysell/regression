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

/**
 * @Author swathiAkarapu
 * @Requirement US16406
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558454824ud/detail/userstory/252661574028">US16406</a>
 * @Description Steps to get there:
 *
 * @DATE November 29, 2018
 */
public class DUC_CovE_10YearPR extends BaseTest {
    public GeneratePolicy myPolicyObject = null;
    private WebDriver driver;

    private void generatePolicy() throws Exception {
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withInsFirstLastName("PR048", "10year")
                .isDraft()
                .build(repository.gw.enums.GeneratePolicyType.FullApp);
    }

    @Test
    public void verfiyPR048() throws Exception {
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
        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);

            pcSquirePropertyPage.clickAdd();
            pcPropertyDetailsPage.setPropertyType(repository.gw.enums.Property.PropertyTypePL.DwellingUnderConstruction);
            pcPropertyDetailsPage.clickPropertyConstructionTab();
            pcPropertyConstructionPage.setYearBuilt((DateUtils.getCenterDateAsInt(driver, ApplicationOrCenter.PolicyCenter, "yyyy") - 11));
            pcPropertyConstructionPage.setSquareFootage(2500);
            pcPropertyConstructionPage.setMeasurement(repository.gw.enums.Measurement.SQFT);
            pcPropertyConstructionPage.setConstructionType(repository.gw.enums.Building.ConstructionTypePL.Frame);
            //pcPropertyConstructionPage.setPolyurethaneAndSandwichAndDescription(true, false, "test");
           // softAssert.assertTrue(pcPropertyConstructionPage.isOpenFlameCoverageWantedExist(), property.getValue() + "  Should display  'Is open flame coverage wanted?' but not");
            //pcPropertyConstructionPage.setOpenFlameCoverageWanted(true);
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

        while (!new GuidewireHelpers(driver).isOnPage("//span[contains(@class, 'g-title') and (text()='Risk Analysis')]")) {
            pcWorkOrder.clickNext();
        }

        pcWorkOrder.clickGenericWorkorderQuote();

        softAssert.assertAll();

    }
}

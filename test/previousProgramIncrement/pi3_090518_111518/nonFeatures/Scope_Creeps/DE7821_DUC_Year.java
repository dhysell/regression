package previousProgramIncrement.pi3_090518_111518.nonFeatures.Scope_Creeps;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.enums.OrganizationType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailConstruction;

import java.util.Date;
/**
 * @Author swathiAkarapu
 * @Requirement DE7821
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558454824ud/detail/defect/247659284244">DE7821</a>
 * @Description
 * Need to look at period start and period end for the allowable year built. The year built should be able to be anywhere between the dates of the entire policy period. Per Ted would be easy to make look at expiry date of policy term.
 * Should almost always have more than one actual year available.
 *
 * Scenario 1
 * As agent start a policy change
 * Have a Dwelling Under Construction built in current year (PROD - term was 12-1-17 to 12-1-18, DUC added on 7-30-18 with effective date of 7-27-18)
 * Hit OK
 * Actual: Receive error "Year Built: Year cannot be in the future"
 * Expected: Should be able to add the year built as the current year
 *
 * See attached screenshots.  Policy in screenshot term was 12-1-17 to 12-1-18, DUC added on 7-30-18 with effective date of 7-27-18
 * @DATE October 25, 2018
 */
public class DE7821_DUC_Year extends BaseTest {
    public GeneratePolicy myPolicyObject = null;
    private WebDriver driver;

    public void generatePolicy() throws Exception {

        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withInsFirstLastName("DE7821", "DUC")
                .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);
    }

    @Test
    public void verifyDucYear() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        generatePolicy();
        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(),
                myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);

        Date expDate = pcAccountSummaryPage.getPolicyTermExpirationDateByStatus(repository.gw.enums.PolicyTermStatus.InForce);
        int expYear = DateUtils.getYearFromDate(expDate);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.startPolicyChange("change property details", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        pcSideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        propertyDetail.clickAdd();
        propertyDetail.setPropertyType(repository.gw.enums.Property.PropertyTypePL.DwellingUnderConstruction);
        propertyDetail.setDwellingVacantRadio(true);
        propertyDetail.setUnits(repository.gw.enums.Property.NumberOfUnits.OneUnit);
        propertyDetail.setWoodFireplaceRadio(false);
        propertyDetail.setSwimmingPoolRadio(false);
        propertyDetail.setWaterLeakageRadio(false);
        propertyDetail.setExoticPetsRadio(false);
        propertyDetail.clickPropertyConstructionTab();
        GenericWorkorderSquirePropertyDetailConstruction constructionPage = new GenericWorkorderSquirePropertyDetailConstruction(driver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction propertyConstruction = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        constructionPage.setYearBuilt(expYear+1);
        constructionPage.setConstructionType(repository.gw.enums.Building.ConstructionTypePL.Frame);
        BasePage bp = new BasePage(driver);
        SoftAssert sf = new SoftAssert();
        boolean errorExist = bp.checkIfElementExists("//*[@id='HOBuilding_FBMPopup:_msgs']/div", 10);
        sf.assertTrue(errorExist, "error message suppose to display but not");
        if (errorExist == true) {
            sf.assertEquals(bp.find(By.xpath("//div[@id='HOBuilding_FBMPopup:_msgs']/div")).getText(), "Year Built : Year cannot be in the future.", "Year Built: Year cannot be in the future-- Not Displayed");
        }
        constructionPage.setYearBuilt(expYear);
        constructionPage.setSquareFootage(4000);
        errorExist = bp.checkIfElementExists("//*[@id='HOBuilding_FBMPopup:_msgs']/div", 10);
        sf.assertFalse(errorExist, "error message Displayed But Not");
        constructionPage.setHeatingUpgrade(false);
        propertyConstruction.setStories(repository.gw.enums.Property.NumberOfStories.One);
        propertyConstruction.setBasementFinished("50");
        propertyConstruction.setGarage(repository.gw.enums.Property.Garage.NoGarage);
        propertyConstruction.setLargeShed(false);
        propertyConstruction.setCoveredPorches(false);
        propertyConstruction.setFoundationType(repository.gw.enums.Property.FoundationType.Foundation);
        propertyConstruction.setRoofType(repository.gw.enums.Property.RoofType.ClayTile);
        propertyConstruction.setKitchenClass(repository.gw.enums.Property.KitchenBathClass.Basic);
        propertyConstruction.setBathClass(repository.gw.enums.Property.KitchenBathClass.Basic);
        propertyConstruction.clickOk();
        propertyConstruction.clickOkayIfMSPhotoYearValidationShows();
        // COVARAGE E Code Testing
        propertyDetail.clickAdd();
        propertyDetail.setPropertyType(repository.gw.enums.Property.PropertyTypePL.DwellingUnderConstructionCovE);
        propertyDetail.setDwellingVacantRadio(true);
        propertyDetail.setUnits(repository.gw.enums.Property.NumberOfUnits.OneUnit);
        propertyDetail.clickPropertyConstructionTab();
        constructionPage.setYearBuilt(expYear+1);
        constructionPage.setConstructionType(repository.gw.enums.Building.ConstructionTypePL.Frame);
        errorExist = bp.checkIfElementExists("//*[@id='HOBuilding_FBMPopup:_msgs']/div", 10);
        sf.assertTrue(errorExist, "error message suppose to display but not for DUC COV E");
        if (errorExist == true) {
            sf.assertEquals(bp.find(By.xpath("//div[@id='HOBuilding_FBMPopup:_msgs']/div")).getText(), "Year Built : Year cannot be in the future.", "Year Built: Year cannot be in the future-- Not Displayed for DUC COV E");
        }
        constructionPage.setYearBuilt(expYear);
        constructionPage.setSquareFootage(4000);
        errorExist = bp.checkIfElementExists("//*[@id='HOBuilding_FBMPopup:_msgs']/div", 10);
        sf.assertFalse(errorExist, "error message Displayed But Not for DUC COV E");
        propertyConstruction.setMeasurement(repository.gw.enums.Measurement.SQFT);
        propertyConstruction.setStories(repository.gw.enums.Property.NumberOfStories.One);
        propertyConstruction.setBasementFinished("50");
        propertyConstruction.setGarage(repository.gw.enums.Property.Garage.NoGarage);
        propertyConstruction.setLargeShed(false);
        propertyConstruction.setCoveredPorches(false);
        propertyConstruction.clickOk();
        propertyConstruction.clickOkayIfMSPhotoYearValidationShows();
        sf.assertAll();
    }
}

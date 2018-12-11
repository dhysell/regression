package scratchpad.shiva;

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
        Config cf = new Config(ApplicationOrCenter.PolicyCenter, "DEV");
        driver = buildDriver(cf);
        //generatePolicy();
        //ACCOUNT NUMBER: 298525
        //AGENT USERNAME: gmoses
        new Login(driver).loginAndSearchAccountByAccountNumber("gmoses",
                "gw", "298525");


        /*new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(),
                myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);*/
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

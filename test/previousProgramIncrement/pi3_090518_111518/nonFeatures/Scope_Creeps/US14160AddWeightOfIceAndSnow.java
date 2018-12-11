package previousProgramIncrement.pi3_090518_111518.nonFeatures.Scope_Creeps;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.OrganizationType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
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
 * @Requirement US14160
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558461772d/detail/userstory/199465021604"></a>
 * @Description
 * Steps to create:
 *----------------------
 * Create a new squire or Std fire submission
 * Enter all required information
 * Go to Coverages wizard >> Coverage E box
 * Acceptance Criteria:
 *-------------------------
 * Ensure that users have an option "Add Weight of ice and snow" when they select Coverage Type 'Peril 1-9' on Coverage E box for Property Type "Commodity Shed".
 * Ensure that "Add Weight of Ice and Snow" is not a required field and has radio button options Yes/No.
 * Ensure that the radio button defaults to "No"
 * @DATE August 6, 2018
 */
public class US14160AddWeightOfIceAndSnow  extends BaseTest {

    public GeneratePolicy myPolicyObject = null;
    private WebDriver driver;

    private void generatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withInsFirstLastName("Weight", "icesnow")
                .isDraft()
                .build(repository.gw.enums.GeneratePolicyType.QuickQuote);
    }

    @Test
    public void verfiyAddWeightOfIceAndSnowOption() throws Exception {

     generatePolicy();

       new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);



        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        SoftAssert softAssert = new SoftAssert();
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Squire);
        pcSideMenu.clickSideMenuSquirePropertyDetail();

        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);

        PLPolicyLocationProperty prop1 = new PLPolicyLocationProperty(repository.gw.enums.Property.PropertyTypePL.CommodityShed);
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
        softAssert.assertFalse(coverages.checkAddWeightOfIceAndSnowCoverageExists(),"Add Weight of Ice and Snow is  visible for CommodityShed when coverage type is not Peril_1Thru9");
        coverages.setCoverageECoverageType(repository.gw.enums.CoverageType.Peril_1Thru9);
        // validate Asserts
        softAssert.assertTrue(coverages.checkAddWeightOfIceAndSnowCoverageExists(),"Add Weight of Ice and Snow is not visible for CommodityShed when coverage type is Peril_1Thru9");
        softAssert.assertTrue(coverages.getAddWeightOfIceAndSnow(false), "Default value of Add Weight of Ice and Snow is not NO");
        softAssert.assertAll();
    }


}

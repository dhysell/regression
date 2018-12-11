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
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;

import java.util.ArrayList;

/**
 * @Author swathiAkarapu
 * @Requirement US16417  Add Weight of Ice and Snow and Glass Coverage options to buildings
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558454824ud/detail/userstory/252958869696">US16417</a>
 * @Description
 * As a PolicyCenter user (home office and county office) I need to have the following coverages available on buildings to match what already exists in OLIE. (from conversation and review of PC rules between Lisa and Michelle C.)
 *
 * Steps to get there:
 * As agent start one of each squire policy (city, county, F&R) and standard fire non-commodity policy
 * On squire policies add Dwelling Under Construction Cov E, Bunk House, Deck/Patio
 * On standard fire policy add Deck/Patio
 * On Bunk House & Deck/Patio when user chooses Coverage Type of Peril 1-9 the option for Add Weight of Ice and Snow should appear and be auto checked to No, but user can choose Yes
 * Per acceptance criteria below try adding appropriate coverages on each building type
 * Acceptance criteria:
 * Ensure that Dwelling Under Construction Cov E have options for Glass Coverage (all squire policies where building type is available) check forms and documents for coverage
 * Ensure that Bunk House has option for Weight of Ice and Snow (all squire policies where building type is available) check forms and documents for coverage
 * Ensure that Deck/Patio has option for Weight of Ice and Snow (all squire & standard fire non-commodity policies where building type is available) check forms and documents for coverage
 * Ensure that on Bunk House and Deck/Patio when user chooses Peril 1-9 the option for Add Weight of Ice and Snow becomes available and is auto checked to No.
 * Ensure that on Bunk House and Deck/Patio when user chooses Peril 1, Special Form or Broad Form that the Add Weight of Ice and Snow option is NOT available
 * http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/ART%20Scope%20Creeps%20Brenda%20mockup%20photos/US16417%20Example%20of%20Screen.PNG
 * @DATE September 24
 */
public class US16417AddWeightOfIceAndSnowAndGlassCov extends BaseTest {
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
    public void verfiyAddWeightOfIceAndSnowOptionOnSquare() throws Exception {

       generatePolicy();

        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);

        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        SoftAssert softAssert = new SoftAssert();
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Squire);
        PLPolicyLocationProperty dwellingProp = new PLPolicyLocationProperty(repository.gw.enums.Property.PropertyTypePL.DwellingPremisesCovE);

        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverageDwelling= addPropertyandCovergeAndValidateCov(softAssert,dwellingProp );
        coverageDwelling.setCoverageCLimit(15000);
        coverageDwelling.setCoverageCValuation(dwellingProp.getPropertyCoverages());
        softAssert.assertTrue(coverageDwelling.checkAddGlassCoverageExists(),"Add Glass Coverageis not  visible for "+ repository.gw.enums.Property.PropertyTypePL.DwellingPremisesCovE.getValue());
        softAssert.assertFalse(coverageDwelling.checkAddWeightOfIceAndSnowCoverageExists(),"Add Weight of Ice and Snow is  visible for "+ repository.gw.enums.Property.PropertyTypePL.DwellingPremisesCovE.getValue()+" when coverage type is not Peril_1Thru9");
        coverageDwelling.setCoverageECoverageType(repository.gw.enums.CoverageType.Peril_1Thru9);
        // validate Asserts
        softAssert.assertTrue(coverageDwelling.checkAddWeightOfIceAndSnowCoverageExists(),"Add Weight of Ice and Snow is not visible for "+ repository.gw.enums.Property.PropertyTypePL.DwellingPremisesCovE.getValue()+" when coverage type is Peril_1Thru9");
        softAssert.assertTrue(coverageDwelling.getAddWeightOfIceAndSnow(false), "Default value of Add Weight of Ice and Snow is not NO for "+ repository.gw.enums.Property.PropertyTypePL.DwellingPremisesCovE.getValue());
        coverageDwelling.clickNext();

        PLPolicyLocationProperty bunkProp = new PLPolicyLocationProperty(repository.gw.enums.Property.PropertyTypePL.BunkHouse);
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverageBunkHouse=addPropertyandCovergeAndValidateCov(softAssert,bunkProp );
        coverageBunkHouse.setCoverageCLimit(15000);
        coverageBunkHouse.setCoverageCValuation(bunkProp.getPropertyCoverages());
        softAssert.assertFalse(coverageBunkHouse.checkAddWeightOfIceAndSnowCoverageExists(),"Add Weight of Ice and Snow is  visible for "+ repository.gw.enums.Property.PropertyTypePL.BunkHouse.getValue()+" when coverage type is not Peril_1Thru9");
        coverageDwelling.setCoverageECoverageType(repository.gw.enums.CoverageType.Peril_1Thru9);
        // validate Asserts
        softAssert.assertTrue(coverageBunkHouse.checkAddWeightOfIceAndSnowCoverageExists(),"Add Weight of Ice and Snow is not visible for "+ repository.gw.enums.Property.PropertyTypePL.BunkHouse.getValue()+" when coverage type is Peril_1Thru9");
        //softAssert.assertTrue(coverageBunkHouse.getAddWeightOfIceAndSnow(false), "Default value of Add Weight of Ice and Snow is not NO for "+Property.PropertyTypePL.DwellingPremisesCovE.getValue());
        coverageBunkHouse.clickNext();

        PLPolicyLocationProperty deckProp = new PLPolicyLocationProperty(repository.gw.enums.Property.PropertyTypePL.DeckPatio);
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverageDeckPatio=addPropertyandCovergeAndValidateCov(softAssert, deckProp);
        softAssert.assertFalse(coverageDeckPatio.checkAddWeightOfIceAndSnowCoverageExists(),"Add Weight of Ice and Snow is  visible for "+ repository.gw.enums.Property.PropertyTypePL.DeckPatio.getValue()+" when coverage type is not Peril_1Thru9");
        coverageDeckPatio.setCoverageECoverageType(repository.gw.enums.CoverageType.Peril_1Thru9);
        // validate Asserts
        softAssert.assertTrue(coverageDeckPatio.checkAddWeightOfIceAndSnowCoverageExists(),"Add Weight of Ice and Snow is not visible for "+ repository.gw.enums.Property.PropertyTypePL.DeckPatio.getValue()+" when coverage type is Peril_1Thru9");
        coverageDeckPatio.setCoverageEValuation(repository.gw.enums.Building.ValuationMethod.ActualCashValue);
        coverageDeckPatio.clickNext();

        softAssert.assertAll();
    }

    private repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages addPropertyandCovergeAndValidateCov(SoftAssert softAssert , PLPolicyLocationProperty prop1 ) throws GuidewireNavigationException {
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
                .withInsFirstLastName("deck", "patio")
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .isDraft()
                .build(repository.gw.enums.GeneratePolicyType.QuickQuote);
    }

    @Test
    public void verfiyAddWeightOfIceAndSnowOptionOnStandardFire() throws Exception {

     generateStandardFirePolicy();

        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);


        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        SoftAssert softAssert = new SoftAssert();
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByStatus("Draft");


        PLPolicyLocationProperty deckProp = new PLPolicyLocationProperty(repository.gw.enums.Property.PropertyTypePL.DeckPatio);
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverageDeckPatio=addPropertyandCovergeAndValidateCov(softAssert, deckProp);
        softAssert.assertFalse(coverageDeckPatio.checkAddWeightOfIceAndSnowCoverageExists(),"Add Weight of Ice and Snow is  visible for "+ repository.gw.enums.Property.PropertyTypePL.DeckPatio.getValue()+" when coverage type is not Peril_1Thru9");
        coverageDeckPatio.setCoverageECoverageType(repository.gw.enums.CoverageType.Peril_1Thru8);
        // validate Asserts
        softAssert.assertTrue(coverageDeckPatio.checkAddWeightOfIceAndSnowCoverageExists(),"Add Weight of Ice and Snow is not visible for "+ repository.gw.enums.Property.PropertyTypePL.DeckPatio.getValue()+" when coverage type is Peril_1Thru9");
        coverageDeckPatio.clickNext();

        softAssert.assertAll();
    }
}

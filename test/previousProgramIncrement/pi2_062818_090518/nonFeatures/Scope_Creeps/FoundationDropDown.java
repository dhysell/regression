package previousProgramIncrement.pi2_062818_090518.nonFeatures.Scope_Creeps;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.Measurement;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderForms;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author swathiAkarapu
 * @Requirement
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558461772d/detail/userstory/244990452840">US15976</a>
 * <a href="https://rally1.rallydev.com/#/203558461772d/detail/userstory/244990487632">US15977</a>
 * <a href="https://rally1.rallydev.com/#/203558461772d/detail/userstory/244998791004">US15978</a>
 * <a href="https://rally1.rallydev.com/#/203558454824ud/detail/userstory/246331771164">US15988</a>
 *
 * <a href="https://rally1.rallydev.com/#/203558461772d/detail/userstory/245044775872">US16021</a>
 * @Description Contents only/renters policy remove foundation
 * Mobile/Manufactured 'foundation, no foundation or pier & beam only
 * Pier & Beam back as foundation choice
 * Infer 169 ends for all mobile homes (if no foundation or pier and beam is selected)
 * Make all coverage E buildings foundation type not required
 * @DATE August 9, 2018
 */

public class FoundationDropDown extends BaseTest {


    @Test(enabled = true)
    public void VerifyFoundationOnNewPolicy() throws Exception {
        GeneratePolicy myPolicyObject = draftGenerate();
        WebDriver driver = myPolicyObject.getWebDriver();

        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);

        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail pcSquirePropertyPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details pcPropertyDetailsPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction pcPropertyConstructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        SoftAssert softAssert = new SoftAssert();
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);
        pcSideMenu.clickSideMenuSquirePropertyDetail();
        //US15976
        List<String> contentsFoundationTypes = getFoundationtypes(pcSideMenu, pcSquirePropertyPage, pcPropertyDetailsPage, pcPropertyConstructionPage, PropertyTypePL.Contents, ConstructionTypePL.Frame);
        softAssert.assertNull(contentsFoundationTypes, "Foundation types present, But  it should not");

        //US15977
        List<String> mobileFoundationTypes = getFoundationtypes(pcSideMenu, pcSquirePropertyPage, pcPropertyDetailsPage, pcPropertyConstructionPage, PropertyTypePL.ResidencePremises, ConstructionTypePL.NonFrame);
        softAssert.assertTrue(mobileFoundationTypes.size() == 4, "There should be  3 foundation types  but not existing");
        softAssert.assertTrue(mobileFoundationTypes.contains(Property.FoundationType.None.getValue()), "This type of foundation is required, but not found for Residence Premises and mobile");
        softAssert.assertTrue(mobileFoundationTypes.contains(Property.FoundationType.Foundation.getValue()), "This type of foundation is required, but not found for Residence Premises and mobile");
        softAssert.assertTrue(mobileFoundationTypes.contains(Property.FoundationType.NoFoundation.getValue()), "This type of no foundation is required, but not found for Residence Premises and mobile");
        softAssert.assertTrue(mobileFoundationTypes.contains(Property.FoundationType.PierAndBeam.getValue()), "This type of Pier and Beam foundation is required, but not found for Residence Premises and mobile");

        List<String> coverageEMobileFoundationTypes = getFoundationtypes(pcSideMenu, pcSquirePropertyPage, pcPropertyDetailsPage, pcPropertyConstructionPage, PropertyTypePL.ResidencePremisesCovE, ConstructionTypePL.NonFrame);
        softAssert.assertTrue(coverageEMobileFoundationTypes.size() == 4, "There should be  3 foundation types  but not existing");
        softAssert.assertTrue(coverageEMobileFoundationTypes.contains(Property.FoundationType.Foundation.getValue()), "This type of foundation is required, but not found for Residence Premises covE and mobile");
        softAssert.assertTrue(coverageEMobileFoundationTypes.contains(Property.FoundationType.NoFoundation.getValue()), "This type of no foundation is required, but not found for Residence Premises covE and mobile");
        softAssert.assertTrue(coverageEMobileFoundationTypes.contains(Property.FoundationType.PierAndBeam.getValue()), "This type of Pier and Beam foundation is required, but not found for Residence Premises covE and mobile");

        softAssert.assertTrue(coverageEMobileFoundationTypes.contains(Property.FoundationType.None.getValue()), "This type of Pier and Beam foundation is required, but not found for Residence Premises covE and mobile");

        List<String> manufacturedFoundationTypes = getFoundationtypes(pcSideMenu, pcSquirePropertyPage, pcPropertyDetailsPage, pcPropertyConstructionPage, PropertyTypePL.VacationHome, ConstructionTypePL.Frame);
        softAssert.assertTrue(manufacturedFoundationTypes.size() == 4, "There should be  3 foundation types  but not existing");
        softAssert.assertTrue(manufacturedFoundationTypes.contains(Property.FoundationType.Foundation.getValue()), "This type of foundation is required, but not found for Vacation Home and Manufactured");
        softAssert.assertTrue(manufacturedFoundationTypes.contains(Property.FoundationType.NoFoundation.getValue()), "This type of no foundation is required, but not found for Vacation Home and Manufactured");
        softAssert.assertTrue(manufacturedFoundationTypes.contains(Property.FoundationType.PierAndBeam.getValue()), "This type of Pier and Beam foundation is required, but not found for Vacation Homes and Manufactured");
        softAssert.assertTrue(manufacturedFoundationTypes.contains(Property.FoundationType.None.getValue()), "This type of Pier and Beam foundation is required, but not found for Vacation Homes and Manufactured");

        List<String> manufacturedFoundationTypesCoveE = getFoundationtypes(pcSideMenu, pcSquirePropertyPage, pcPropertyDetailsPage, pcPropertyConstructionPage, PropertyTypePL.VacationHomeCovE, ConstructionTypePL.Frame);
        softAssert.assertTrue(manufacturedFoundationTypes.size() == 4, "There should be  3 foundation types  but not existing");
        softAssert.assertTrue(manufacturedFoundationTypesCoveE.contains(Property.FoundationType.Foundation.getValue()), "This type of foundation is required, but not found for Vacation Home  covE and Manufactured");
        softAssert.assertTrue(manufacturedFoundationTypesCoveE.contains(Property.FoundationType.NoFoundation.getValue()), "This type of no foundation is required, but not found for Vacation Home covE and Manufactured");
        softAssert.assertTrue(manufacturedFoundationTypesCoveE.contains(Property.FoundationType.PierAndBeam.getValue()), "This type of Pier and Beam foundation is required, but not found for Vacation Home covE and Manufactured");
        softAssert.assertTrue(manufacturedFoundationTypesCoveE.contains(Property.FoundationType.None.getValue()), "This type of Pier and Beam foundation is required, but not found for Vacation Home covE and Manufactured");


        softAssert.assertAll();

    }

    private GeneratePolicy draftGenerate() throws Exception {
        GeneratePolicy myPolicyObject = null;
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);
//        try {

            myPolicyObject = new GeneratePolicy.Builder(driver)
                    .withProductType(ProductLineType.Squire)
                    .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                    .withPolOrgType(OrganizationType.Individual)
                    .withDownPaymentType(PaymentType.Cash)
                    .withInsFirstLastName("ScopeCreeps", "NonFeature")
                    .isDraft()
                    .build(GeneratePolicyType.QuickQuote);
//        } catch (Exception e) {
//            e.printStackTrace();
//            Assert.fail("Unable to generate policy. Test cannot continue.");
//        }
        return myPolicyObject;
    }

    private List<String> getFoundationtypes(SideMenuPC pcSideMenu, GenericWorkorderSquirePropertyAndLiabilityPropertyDetail pcSquirePropertyPage, GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details pcPropertyDetailsPage, GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction pcPropertyConstructionPage, PropertyTypePL propertyType, ConstructionTypePL constructionType) throws GuidewireNavigationException {
        List<String> foundationTypes = null;
        pcSquirePropertyPage.clickAdd();
        pcPropertyDetailsPage.setPropertyType(propertyType);
        pcPropertyDetailsPage.clickPropertyConstructionTab();
        pcPropertyConstructionPage.setConstructionType(constructionType);
        if (pcPropertyConstructionPage.isFoundationTypeExist()) {
            foundationTypes = pcPropertyConstructionPage.getFoundationTypeValues();
        }
        //pcPropertyConstructionPage.setPhotoYear("08/"+DateUtils.getYearFromDate(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter)));
        pcPropertyConstructionPage.clickCancel();
        return foundationTypes;
    }

    /**
     * ModularManufactured  and Foundation - make sure no 168or 169 form
     *
     * @throws Exception
     */

    @Test(enabled = true)
    public void US15988VerifyFormsWhenMobileHomeFoundationSelectedforNO169Form() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);
        GeneratePolicy myPolicyObject = null;
//        try {
            ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
            ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

            PLPolicyLocationProperty property = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
            property.setConstructionType(ConstructionTypePL.ModularManufactured);
            property.setFoundationType(Property.FoundationType.Foundation);
            locOnePropertyList.add(property);

            PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
            locationsList.add(locToAdd);

            SquireLiability myLiab = new SquireLiability();


            SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
            myPropertyAndLiability.locationList = locationsList;
            myPropertyAndLiability.liabilitySection = myLiab;

            Squire mySquire = new Squire(SquireEligibility.City);
            mySquire.propertyAndLiability = myPropertyAndLiability;

            myPolicyObject = new GeneratePolicy.Builder(driver)
                    .withSquire(mySquire)
                    .withProductType(ProductLineType.Squire)
                    .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                    .withPolOrgType(OrganizationType.Individual)
                    .withDownPaymentType(PaymentType.Cash)
                    .withInsFirstLastName("Form", "no169")
                    .build(GeneratePolicyType.FullApp);

//        } catch (Exception e) {
//            e.printStackTrace();
//            Assert.fail("Unable to generate policy. Test cannot continue.");
//        }


        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);

        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);

        SoftAssert softAssert = new SoftAssert();
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);
        pcSideMenu.clickSideMenuSquirePropertyDetail();


        pcSideMenu.clickSideMenuForms();
        GenericWorkorderForms formspage = new GenericWorkorderForms(driver);
        List<String> rewriteForms = formspage.getFormDescriptionsFromTable();
        Assert.assertFalse(rewriteForms.contains("Mobile Home Endorsement"), "Mobile Home Endorsement - found");

        softAssert.assertAll();

    }


    @Test(enabled = true)
    public void US15988VerifyFormsWhenManufactureNoFoundationSelectedfor168Form() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);
        GeneratePolicy myPolicyObject = null;
//        try {
            ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
            ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

            PLPolicyLocationProperty property = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
            property.setConstructionType(ConstructionTypePL.ModularManufactured);
            property.setFoundationType(Property.FoundationType.NoFoundation);
            locOnePropertyList.add(property);

            PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
            locationsList.add(locToAdd);

            SquireLiability myLiab = new SquireLiability();


            SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
            myPropertyAndLiability.locationList = locationsList;
            myPropertyAndLiability.liabilitySection = myLiab;

            Squire mySquire = new Squire(SquireEligibility.City);
            mySquire.propertyAndLiability = myPropertyAndLiability;

            myPolicyObject = new GeneratePolicy.Builder(driver)
                    .withSquire(mySquire)
                    .withProductType(ProductLineType.Squire)
                    .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                    .withPolOrgType(OrganizationType.Individual)
                    .withDownPaymentType(PaymentType.Cash)
                    .withInsFirstLastName("form", "168")
                    .build(GeneratePolicyType.FullApp);

//        } catch (Exception e) {
//            e.printStackTrace();
//            Assert.fail("Unable to generate policy. Test cannot continue.");
//        }

        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);
//295099
        //  new Login(driver).loginAndSearchAccountByAccountNumber("darmstrong", "gw", "295107");

        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);

        SoftAssert softAssert = new SoftAssert();
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);
        pcSideMenu.clickSideMenuSquirePropertyDetail();


        pcSideMenu.clickSideMenuForms();
        GenericWorkorderForms formspage = new GenericWorkorderForms(driver);
        List<String> rewriteForms = formspage.getFormDescriptionsFromTable();
        Assert.assertTrue(rewriteForms.contains("Modular Mobile Home Endorsement - Excluding Peril 17"), "UW I168 - Not found");

        softAssert.assertAll();

    }

    /**
     * US15988
     * Ensure that on frame and non-frame homes that have 'no foundation' there is no 169 ends inferring or printing
     *
     * @throws Exception
     */
    @Test(enabled = true)
    public void US15988VerifyFormsWhenMobileHomeNoFoundationSelectedThen169Form() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);
        GeneratePolicy myPolicyObject = null;
//        try {
            ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
            ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

            PLPolicyLocationProperty property = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
            property.setConstructionType(ConstructionTypePL.MobileHome);
            property.setFoundationType(Property.FoundationType.NoFoundation);
            locOnePropertyList.add(property);

            PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
            locationsList.add(locToAdd);

            SquireLiability myLiab = new SquireLiability();


            SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
            myPropertyAndLiability.locationList = locationsList;
            myPropertyAndLiability.liabilitySection = myLiab;

            Squire mySquire = new Squire(SquireEligibility.City);
            mySquire.propertyAndLiability = myPropertyAndLiability;

            myPolicyObject = new GeneratePolicy.Builder(driver)
                    .withSquire(mySquire)
                    .withProductType(ProductLineType.Squire)
                    .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                    .withPolOrgType(OrganizationType.Individual)
                    .withDownPaymentType(PaymentType.Cash)
                    .withInsFirstLastName("Form", "169")
                    .build(GeneratePolicyType.FullApp);

//        } catch (Exception e) {
//            e.printStackTrace();
//            Assert.fail("Unable to generate policy. Test cannot continue.");
//        }


        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);
        //new Login(driver).loginAndSearchAccountByAccountNumber("blatimore", "gw", "295152");

        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);

        SoftAssert softAssert = new SoftAssert();
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);
        pcSideMenu.clickSideMenuSquirePropertyDetail();


        pcSideMenu.clickSideMenuForms();
        GenericWorkorderForms formspage = new GenericWorkorderForms(driver);
        List<String> rewriteForms = formspage.getFormDescriptionsFromTable();
        Assert.assertTrue(rewriteForms.contains("Mobile Home Endorsement"), "UW I169 -not Found");

        softAssert.assertAll();

    }

    /**
     * US15977
     * Ensure that if coverage A is selected for mobile or manufactured home and foundation type of pier & beam is chosen that the user receives screen stop stating that cannot be coverage A and must move to coverage E building
     *
     * @throws Exception
     */

    @Test(enabled = true)
    public void testUS15977_covAMobilePeirBeamRecieveHardScreenStop() throws Exception {
        GeneratePolicy myPolicyObject = draftGenerate();
        WebDriver driver = myPolicyObject.getWebDriver();

        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);

        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail pcSquirePropertyPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details pcPropertyDetailsPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction pcPropertyConstructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        SoftAssert softAssert = new SoftAssert();
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);
        pcSideMenu.clickSideMenuSquirePropertyDetail();

//        GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);

        pcSquirePropertyPage.clickAdd();
        pcPropertyDetailsPage.setPropertyType(PropertyTypePL.ResidencePremises);
        pcPropertyDetailsPage.clickPropertyConstructionTab();
        pcPropertyConstructionPage.setConstructionType(ConstructionTypePL.Frame);
        pcPropertyConstructionPage.setSquareFootage(3124);
        pcPropertyConstructionPage.setYearBuilt(DateUtils.getYearFromDate(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter)));
        pcPropertyConstructionPage.setFoundationType(Property.FoundationType.PierAndBeam); // Update this to PierAndBeam once it available in dev
        pcPropertyConstructionPage.clickOk();
        //Coverage A Residence Premise, Dwelling Premise, Vacation Home and Dwelling Under Construction cannot have foundation type of pier &amp; beam, please change to a Coverage E Property Type
        //Assert the Show Stoper
        softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Coverage A Residence Premise, Dwelling Premise, Vacation Home and Dwelling Under Construction cannot have foundation type of pier & beam, please cancel and move to a Coverage E home"), "Coverage A Residence Premise, Dwelling Premise, Vacation Home and Dwelling Under Construction cannot have foundation type of pier & beam, please cancel and move to a Coverage E home- not displayed");
        // softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Coverage A Residence Premise, Dwelling Premise, Vacation Home and Dwelling Under Construction cannot have foundation type of pier & beam, please change to a Coverage E Property Type"), "Show stopper- not displayed");
        softAssert.assertAll();

    }

    //US16021 -Ensure that as agent/PA/underwriter all coverage E buildings have foundation type available but not required

    @Test(enabled = true)
    public void testUS16021_coverageEBuildingsFoundationTypeNotRequired() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);
        GeneratePolicy myPolicyObject;
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withDownPaymentType(PaymentType.Cash)
                .withInsFirstLastName("ScopeCreeps", "NonFeature")
                .build(GeneratePolicyType.PolicyIssued);


        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);


        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage =
                new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();
        policyChangePage.startPolicyChange("add new property", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
        pcSideMenu.clickSideMenuSquirePropertyDetail();

        propertyDetail.clickAdd();
        propertyDetail.setPropertyType(PropertyTypePL.CondominiumDwellingPremisesCovE);
        propertyDetail.setUnits(Property.NumberOfUnits.FourUnits);
        propertyDetail.setDwellingVacantRadio(true);

        propertyDetail.clickPropertyConstructionTab();
        constructionPage.setConstructionType(ConstructionTypePL.Frame);
        constructionPage.setSquareFootage(3124);
        constructionPage.setYearBuilt(DateUtils.getYearFromDate(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter)));
        constructionPage.setMeasurement(Measurement.SQFT);
        constructionPage.setStories(Property.NumberOfStories.Three);
        constructionPage.setBasementFinished("100");

        constructionPage.setGarage(Property.Garage.NoGarage);
        constructionPage.setLargeShed(false);
        constructionPage.setCoveredPorches(false);
        constructionPage.clickOK();
        propertyDetail.clickOkayIfMSPhotoYearValidationShows();
        // make sure with out foundation type , we could able to add property
        SoftAssert softassert = new SoftAssert();

        softassert.assertTrue(new GuidewireHelpers(driver).isOnPage("//span[contains(@class, 'g-title') and (text()='Property Detail')]"),
                "Could not able to add property with out foundation ");
        softassert.assertFalse(new GuidewireHelpers(driver).isRequired("Foundation Type : Missing required field \"Foundation Type\""), "Foundation Type : Missing required field \"Foundation Type\" - displayed");
        softassert.assertAll();
    }

}





















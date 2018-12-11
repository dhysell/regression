package regression.r2.noclock.policycenter.submission_misc.subgroup1;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.CoverageType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InsuranceScore;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailConstruction;
import repository.pc.workorders.generic.GenericWorkorderVehicles_CoverageDetails;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;

/**
 * @Author skandibanda
 * @Requirement : US9135 - [Part II] Coverage, Coverage terms, and Coverage term option availability for Cov E and Standard Fire
 * @Description : Verifying "Wood fireplace or Wood Stove" question, "Are chimneys cleaned regularly?"
 * and "Date last cleaned" sub-questions from the details tab on Property Details > Detail tab removed for StdFire (Non-Commodities policies)
 * Verifying coverage terms availability added for on Squire and Standard Fire property types
 * Verifying Inclusive Coverage displayed under Quote Screen
 * @DATE Nov 08, 2016
 */
@QuarantineClass
public class TestCoverageAvailabilityForThings extends BaseTest {

    private GeneratePolicy stdFireLiab_Squire_PolicyObj, myPolicyObjPL, squirePolicyObjPL;
    private String driverFirstName = "testDr";

    private WebDriver driver;

    @Test()
    public void testCreateStandardFireWithSquire() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locationsList.add(locToAdd);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        this.stdFireLiab_Squire_PolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("SQ", "Hops")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.FullApp);

        //standard fire
        ArrayList<PolicyLocation> locationsList1 = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList1 = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList1.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));
        locOnePropertyList1.add(new PLPolicyLocationProperty(PropertyTypePL.VacationHome));
        locOnePropertyList1.add(new PLPolicyLocationProperty(PropertyTypePL.CondominiumVacationHome));
        PolicyLocation locToAdd1 = new PolicyLocation(locOnePropertyList1);

        locToAdd1.setPlNumAcres(11);
        locToAdd1.setPlNumResidence(5);
        locationsList1.add(locToAdd1);

        StandardFireAndLiability myStandardFire = new StandardFireAndLiability();
        myStandardFire.setStdFireCommodity(false);
        myStandardFire.setLocationList(locationsList1);
        stdFireLiab_Squire_PolicyObj.pniContact.setInsuranceScore(InsuranceScore.NeedsOrdered);
        new GuidewireHelpers(driver).logout();

        stdFireLiab_Squire_PolicyObj.standardFire = myStandardFire;
        stdFireLiab_Squire_PolicyObj.lineSelection.add(LineSelection.StandardFirePL);
        stdFireLiab_Squire_PolicyObj.addLineOfBusiness(ProductLineType.StandardFire, GeneratePolicyType.FullApp);
    }


    @Test(dependsOnMethods = {"testCreateStandardFireWithSquire"})
    public void validateWoodfireplaceOrWoodstovequestion() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(stdFireLiab_Squire_PolicyObj.agentInfo.getAgentUserName(), stdFireLiab_Squire_PolicyObj.agentInfo.getAgentPassword(), stdFireLiab_Squire_PolicyObj.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquireProperty();
        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetailsPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);

        String errorMessage = "";

        int row = propertyDetailsPage.getPropertiesCount();
        for (int i = 0; i < row; i++) {
            String property = propertyDetailsPage.getPropertiesList().get(i);
            propertyDetailsPage.clickViewOrEditBuildingButton(i + 1);
            if (propertyDetailsPage.checkWoodFireplaceOrWoodStoveQuestionExists())
                errorMessage = errorMessage + "Wood fireplace or Wood Stove Question should not exist for " + property;

            if (propertyDetailsPage.checkAreChimneysCleanedRegularlySubQuestionExists())
                errorMessage = errorMessage + "Are Chimneys Cleaned Regularly Question should not exist for " + property;

            if (propertyDetailsPage.checkDatelastcleanedSubQuestioExists())
                errorMessage = errorMessage + "Date last Cleaned Question should not exist for " + property;

            propertyDetailsPage.clickOk();

        }

    }

    @Test(dependsOnMethods = {"validateWoodfireplaceOrWoodstovequestion"})
    public void validateStdFireGlassCoverageAndAddWeightOfIceAndSnowCoverages() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(stdFireLiab_Squire_PolicyObj.agentInfo.getAgentUserName(), stdFireLiab_Squire_PolicyObj.agentInfo.getAgentPassword(), stdFireLiab_Squire_PolicyObj.accountNumber);

        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();


        ArrayList<PLPolicyLocationProperty> NewPropertiesList = new ArrayList<PLPolicyLocationProperty>();
        PLPolicyLocationProperty prop0 = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremisesCovE);
        prop0.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.Peril_1);
        PLPolicyLocationProperty prop1 = new PLPolicyLocationProperty(PropertyTypePL.VacationHomeCovE);
        prop1.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.Peril_1);
        PLPolicyLocationProperty prop2 = new PLPolicyLocationProperty(PropertyTypePL.AlfalfaMill);
        prop2.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.Peril_1);
        PLPolicyLocationProperty prop3 = new PLPolicyLocationProperty(PropertyTypePL.Arena);
        prop3.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.Peril_1);
        PLPolicyLocationProperty prop4 = new PLPolicyLocationProperty(PropertyTypePL.DairyComplex);
        prop4.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.Peril_1);
        PLPolicyLocationProperty prop5 = new PLPolicyLocationProperty(PropertyTypePL.FarmOffice);
        prop5.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.Peril_1);
        PLPolicyLocationProperty prop6 = new PLPolicyLocationProperty(PropertyTypePL.Garage);
        prop6.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.Peril_1);
        PLPolicyLocationProperty prop7 = new PLPolicyLocationProperty(PropertyTypePL.Hangar);
        prop7.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.Peril_1);
        PLPolicyLocationProperty prop8 = new PLPolicyLocationProperty(PropertyTypePL.Hatchery);
        prop8.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.Peril_1);
        NewPropertiesList.add(prop0);
        NewPropertiesList.add(prop1);
        NewPropertiesList.add(prop2);
        NewPropertiesList.add(prop3);
        NewPropertiesList.add(prop4);
        NewPropertiesList.add(prop5);
        NewPropertiesList.add(prop6);
        NewPropertiesList.add(prop7);
        NewPropertiesList.add(prop8);
        stdFireLiab_Squire_PolicyObj.standardFire.getLocationList().get(0).setPropertyList(NewPropertiesList);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);

        addNewProperty(prop0);
        prop0.setPropertyNumber(propertyDetail.getSelectedBuildingNum());
        addNewProperty(prop1);
        prop1.setPropertyNumber(propertyDetail.getSelectedBuildingNum());
        addNewProperty(prop2);
        prop2.setPropertyNumber(propertyDetail.getSelectedBuildingNum());
        addNewProperty(prop3);
        prop3.setPropertyNumber(propertyDetail.getSelectedBuildingNum());
        addNewProperty(prop4);
        prop4.setPropertyNumber(propertyDetail.getSelectedBuildingNum());
        addNewProperty(prop5);
        prop5.setPropertyNumber(propertyDetail.getSelectedBuildingNum());
        addNewProperty(prop6);
        prop6.setPropertyNumber(propertyDetail.getSelectedBuildingNum());
        addNewProperty(prop7);
        prop7.setPropertyNumber(propertyDetail.getSelectedBuildingNum());
        addNewProperty(prop8);
        prop8.setPropertyNumber(propertyDetail.getSelectedBuildingNum());

        String errorMessage = "";
        sideMenu.clickSideMenuSquirePropertyCoverages();
        for (PLPolicyLocationProperty prop : stdFireLiab_Squire_PolicyObj.standardFire.getLocationList().get(0).getPropertyList()) {
            coverages.clickSpecificBuilding(1, prop.getPropertyNumber());
            if (prop.hasCoverageE()) {
                coverages.fillOutCoverageE(prop);
            }

            if (!coverages.checkAddGlassCoverageExists())
                errorMessage = errorMessage + "Add glass coverage is not displayed for property" + coverages.getSelectedBuildingName() + "\n";

            if (!coverages.checkAddWeightOfIceAndSnowCoverageExists())
                errorMessage = errorMessage + " Weight of Ice and Snow coverage is not displayed for property" + coverages.getSelectedBuildingName() + "\n";
        }
        if (errorMessage != "")
            Assert.fail(errorMessage);
    }


    @Test()
    public void testGenerateAutoFA() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        // Coverages
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow, MedicalLimit.TenK);

        // driver
        ArrayList<Contact> driversList = new ArrayList<Contact>();
        Contact person = new Contact();
        person.setFirstName(driverFirstName);
        driversList.add(person);

        // Vehicle
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle toAdd = new Vehicle();
        toAdd.setCostNew(10000.00);
        toAdd.setAdditionalLivingExpense(true);
        toAdd.setFireAndTheft(true);
        vehicleList.add(toAdd);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);
        squirePersonalAuto.setVehicleList(vehicleList);
        squirePersonalAuto.setDriversList(driversList);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("NewFA", "Auto")
                .build(GeneratePolicyType.FullApp);
    }

    @Test(dependsOnMethods = {"testGenerateAutoFA"})
    public void validateLossOfUseByTheftReimbursement() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPADrivers();
        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        GenericWorkorderVehicles_CoverageDetails vehicleCoverages = new GenericWorkorderVehicles_CoverageDetails(driver);
        vehiclePage.clickLinkInVehicleTable("Edit");
        vehiclePage.setVIN("2C4RC1CG3DR531542");
        vehiclePage.setFactoryCostNew(2000);
        vehicleCoverages.clickVehicleCoveragesTab();
        vehicleCoverages.setFireTheftCoverage(true);
        vehicleCoverages.clickOK();

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickExpandedViewLink();
        ArrayList<String> description = quote.getDescriptionsFromSectionIIICoveragesTable();
        if (!description.contains("     Loss of Use by Theft - Reimbursement"))
            Assert.fail("Loss of Use by Theft - Reimbursement description is not displayed");
        if (quote.checkDescriptionOptions("Loss of Use by Theft"))
            Assert.fail("Loss of Use by Theft - Reimbursement description is not displayed");
    }

    @Test()
    public void testGenerateSquireSectionI() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));

        locationsList.add(new PolicyLocation(locOnePropertyList));

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        squirePolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Anderson", "Thomas")
                .build(GeneratePolicyType.FullApp);

    }

    @Test(dependsOnMethods = {"testGenerateSquireSectionI"})
    public void validateSquireSetionIInclusiveCoverages() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(squirePolicyObjPL.agentInfo.getAgentUserName(), squirePolicyObjPL.agentInfo.getAgentPassword(), squirePolicyObjPL.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuRiskAnalysis();
        sideMenu.clickSideMenuQuote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickExpandedViewLink();
        String errorMessage = "";
        ArrayList<String> arrayOfDescription = quote.getDescriptionFromSectionILineCoveragesTable();
        if (!arrayOfDescription.contains("Fire Department Service Charge"))
            errorMessage = errorMessage + "Fire Department Service Charge coverage not displayed in quote screen";

        boolean refrigerated = false;
        boolean limitedFungi = false;
        ArrayList<String> propertyDescription = quote.getDescriptionsFromSectionIPropertyDetailsTable();

        for (String description : propertyDescription) {
            if (description.trim().equals("Refrigerated Products"))
                refrigerated = true;


            if (description.trim().equals("Limited Fungi, Wet or Dry Rot, or Bacteria Coverage"))
                limitedFungi = true;
        }

        if (!refrigerated)
            errorMessage = errorMessage + "Refrigerated Products coverage not displayed in quote screen";
        if (!limitedFungi)
            errorMessage = errorMessage + "Limited Fungi, Wet or Dry Rot, or Bacteria Coverage not displayed in quote screen";
        if (errorMessage != "")
            Assert.fail(errorMessage);

    }

    @Test(dependsOnMethods = {"validateSquireSetionIInclusiveCoverages"})
    public void validateGlassCoverageAndAddWeightOfIceAndSnow() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(squirePolicyObjPL.agentInfo.getAgentUserName(), squirePolicyObjPL.agentInfo.getAgentPassword(), squirePolicyObjPL.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        new GuidewireHelpers(driver).editPolicyTransaction();
        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);

        ArrayList<PLPolicyLocationProperty> NewPropertiesList = new ArrayList<>();
        PLPolicyLocationProperty prop1 = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremisesCovE);
        prop1.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.Peril_1);
        PLPolicyLocationProperty prop2 = new PLPolicyLocationProperty(PropertyTypePL.VacationHomeCovE);
        prop2.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.Peril_1);
        PLPolicyLocationProperty prop3 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremisesCovE);
        prop3.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.Peril_1);
        PLPolicyLocationProperty prop4 = new PLPolicyLocationProperty(PropertyTypePL.AlfalfaMill);
        prop4.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.Peril_1);
        PLPolicyLocationProperty prop5 = new PLPolicyLocationProperty(PropertyTypePL.Arena);
        prop5.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.Peril_1);
        PLPolicyLocationProperty prop6 = new PLPolicyLocationProperty(PropertyTypePL.Barn);
        prop6.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.Peril_1);
        PLPolicyLocationProperty prop7 = new PLPolicyLocationProperty(PropertyTypePL.DairyComplex);
        prop7.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.Peril_1);
        PLPolicyLocationProperty prop8 = new PLPolicyLocationProperty(PropertyTypePL.Garage);
        prop8.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.Peril_1);
        NewPropertiesList.add(prop1);
        NewPropertiesList.add(prop2);
        NewPropertiesList.add(prop3);
        NewPropertiesList.add(prop4);
        NewPropertiesList.add(prop5);
        NewPropertiesList.add(prop6);
        NewPropertiesList.add(prop7);
        NewPropertiesList.add(prop8);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);

        squirePolicyObjPL.squire.propertyAndLiability.locationList.get(0).setPropertyList(NewPropertiesList);
        addNewProperty(prop1);
        setConsutrctionPage(prop1);
        prop1.setPropertyNumber(propertyDetail.getSelectedBuildingNum());
        addNewProperty(prop2);
        setConsutrctionPage(prop2);
        prop2.setPropertyNumber(propertyDetail.getSelectedBuildingNum());
        addNewProperty(prop3);
        setConsutrctionPage(prop3);
        prop3.setPropertyNumber(propertyDetail.getSelectedBuildingNum());
        addNewProperty(prop4);
        prop4.setPropertyNumber(propertyDetail.getSelectedBuildingNum());
        addNewProperty(prop5);
        prop5.setPropertyNumber(propertyDetail.getSelectedBuildingNum());
        addNewProperty(prop6);
        prop6.setPropertyNumber(propertyDetail.getSelectedBuildingNum());
        addNewProperty(prop7);
        prop7.setPropertyNumber(propertyDetail.getSelectedBuildingNum());
        addNewProperty(prop8);
        prop8.setPropertyNumber(propertyDetail.getSelectedBuildingNum());
        String errorMessage = "";
        sideMenu.clickSideMenuSquirePropertyCoverages();
        for (PLPolicyLocationProperty prop : squirePolicyObjPL.squire.propertyAndLiability.locationList.get(0).getPropertyList()) {
            coverages.clickSpecificBuilding(1, prop.getPropertyNumber());
            if (prop.hasCoverageE()) {
                coverages.fillOutCoverageE(prop);
            }
            if (!coverages.checkAddGlassCoverageExists())
                errorMessage = errorMessage + "Add glass coverage is not displayed for property" + coverages.getSelectedBuildingName() + "\n";
            if (!coverages.checkAddWeightOfIceAndSnowCoverageExists())
                errorMessage = errorMessage + " Weight of Ice and Snow coverage is not displayed for property" + coverages.getSelectedBuildingName() + "\n";
        }
        if (errorMessage != "")
            Assert.fail(errorMessage);
    }


    private void addNewProperty(PLPolicyLocationProperty property) {
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        GenericWorkorderSquirePropertyDetailConstruction constructionPage = new GenericWorkorderSquirePropertyDetailConstruction(driver);
        propertyDetail.fillOutPropertyDetails_FA(property);
        constructionPage.setCoverageAPropertyDetailsFA(property);

        constructionPage.clickOK();
    }

    private void setConsutrctionPage(PLPolicyLocationProperty property) {
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        constructionPage.setLargeShed(false);
        constructionPage.clickOK();
    }

}

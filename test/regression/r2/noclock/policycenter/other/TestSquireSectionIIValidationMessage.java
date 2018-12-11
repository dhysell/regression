package regression.r2.noclock.policycenter.other;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.CountyIdaho;
import com.idfbins.enums.State;

import repository.driverConfiguration.Config;
import repository.gw.enums.AddressType;
import repository.gw.enums.Building.ValuationMethod;
import repository.gw.enums.CoverageType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderLineSelection;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;

/**
 * @Author nvadlamudi
 * @Requirement :DE4690: PL - Section I & II - Validation message has wrong wording
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/33274298124d/detail/defect/85923759376"</a>
 * @Description
 * @DATE Feb 14, 2017
 */
public class TestSquireSectionIIValidationMessage extends BaseTest {
    private GeneratePolicy myPolicyObj;

    private WebDriver driver;

    @Test()
    public void testSquireAutoFA() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        // Coverages
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh,
                MedicalLimit.TenK);
        coverages.setUnderinsured(false);
        coverages.setUninsuredLimit(UninsuredLimit.Fifty);
        coverages.setAccidentalDeath(true);

        // driver
        ArrayList<Contact> driversList = new ArrayList<Contact>();
        Contact person = new Contact();
        driversList.add(person);

        // Vehicle
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle toAdd = new Vehicle();
        toAdd.setEmergencyRoadside(true);
        vehicleList.add(toAdd);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);
        squirePersonalAuto.setVehicleList(vehicleList);
        squirePersonalAuto.setDriversList(driversList);


        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Test", "Validations")
                .build(GeneratePolicyType.FullApp);
    }

    @Test(dependsOnMethods = {"testSquireAutoFA"})
    private void testAddSectionISectionIIAndValidate() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(this.myPolicyObj.agentInfo.getAgentUserName(),
                this.myPolicyObj.agentInfo.getAgentPassword(), this.myPolicyObj.accountNumber);

        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuLineSelection();

        GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(driver);
        lineSelection.checkPersonalPropertyLine(true);
        sideMenu.clickSideMenuQualification();
        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
        qualificationPage.setSquireGeneralFullTo(false);
        qualificationPage.setSquireHOFullTo(false, "I was in a biker gang staying in motels, and not a homeowner");
        qualificationPage.setSquireGLFullTo(false);

        sideMenu.clickSideMenuPolicyInfo();
        sideMenu.clickSideMenuHouseholdMembers();

        // Locations
        sideMenu.clickSideMenuPropertyLocations();

        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        AddressInfo address = new AddressInfo("6315 W YORK ST", "", "BOISE", State.Idaho, "837047573", CountyIdaho.Ada,
                "United States", AddressType.Home);
        
        PolicyLocation location = new PolicyLocation(address);
        location.setPlNumAcres(5);
        location.setPlNumResidence(1);
        
        propertyLocations.addNewOrSelectExistingLocationQQ(location);

        // Property
        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetails = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        // Adding property with Owner name
        PLPolicyLocationProperty property = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        propertyDetails.fillOutPropertyDetails_FA(property);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        constructionPage.fillOutPropertyConstrustion_FA(property);
        constructionPage.setLargeShed(false);

        GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);
        constructionPage.clickProtectionDetailsTab();
        new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails(driver).fillOutPropertyProtectionDetails(property);
        protectionPage.clickOK();

        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverages.clickSpecificBuilding(1, 1);
        coverages.selectSectionIDeductible(SectionIDeductible.FiveThousand);
        coverages.setCoverageALimit(200200);
        coverages.setCoverageACoverageType(CoverageType.SpecialForm);
        coverages.setCoverageAValuation(ValuationMethod.ActualCashValue);
        coverages.setCoverageCValuation(property.getPropertyCoverages());

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        GenericWorkorder gw = new GenericWorkorder(driver);
        gw.clickQuoteBlockingStep();

        String valMsg = "Section II Coverages tab has not been visited";
        if (!risk.getValidationMessagesText().contains(valMsg)) {
            Assert.fail("Expected Quote Blocking Validation Message : " + valMsg + " is not displayed.");
        }
        risk.clickClearButton();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();
    }

}

package regression.r2.noclock.policycenter.submission_property.subgroup2;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.driverConfiguration.Config;
import repository.gw.enums.CoverageType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandibanda
 * @Requirement DE3501: Section I & II - Coverage Detail screen still shows the property of the location
 * that is removed or changed to Out of state.
 * @RequirementsLink <a href="http:// "rally1.rallydev.com/#/33274298124d/detail/defect/53900089670</a>
 * @Description
 * @DATE May 05, 2016
 */
public class TestSquirePropertyLiabilityCoverageScreenEditsRemoveOutofState extends BaseTest {

    public GeneratePolicy squirePLObject = null;
    Agents agent;
    private Underwriters uw;
    private WebDriver driver;

    @BeforeMethod
    public void beforeMethod() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
    }


    @Test()
    public void propertyLocations() throws Exception {
        // Creating quote
        squirePLObject = createPLAutoPolicy();
        agent = squirePLObject.agentInfo;

        // Login and open the same account
        new Login(driver).loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), squirePLObject.accountNumber);


        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();

        // add new locations
        addPLSection1Locations(State.Oregon, "Bend");

        //add properties
        addPLSectionProperties(2);

        //remove the 1st location and check coverages page
        removeLocationAndValidateCoveragePage();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();

    }


    @Test(dependsOnMethods = {"propertyLocations"})
    private void testAddingNevedaWashington() throws Exception {
        // Login and open the same account
        new Login(driver).loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), squirePLObject.accountNumber);

        //Adding the below locations to test US7429: PL - Add Nevada to out of state
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPropertyLocations();

        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);

        //out of IDaho
        propertyLocations.clickNewLocation();
//        propertyLocations.selectAddressType("Out of State");
//        propertyLocations.selectState(State.Nevada);
//        propertyLocations.setCounty("Test");
//        propertyLocations.setAddressLegal("Testing purpose");
//        propertyLocations.setAcres(2);
        propertyLocations.clickOK();


        if (!propertyLocations.find(By.xpath("//div[contains(@id, ':LocationsEdit_LV-body')]/div/table/tbody/child::tr[last()]/child::td[5]/div/a")).getText().contains(State.Nevada.getAbbreviation())) {
            Assert.fail("Expected State :Nevada is not added");
        }
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();

    }


    @Test(dependsOnMethods = {"propertyLocations"})
    private void testAddWashingtonOutofState() throws Exception {
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), squirePLObject.accountNumber);
        SideMenuPC sideMenuOptions = new SideMenuPC(driver);
        sideMenuOptions.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);

        propertyLocations.clickNewLocation();
//        propertyLocations.selectAddressType("Out of State");
//        propertyLocations.selectState(State.Washington);
//        propertyLocations.setCounty("Testing");
//        propertyLocations.setAddressLegal("Testing purpose");
//        propertyLocations.setAcres(3);
        propertyLocations.clickOK();


        if (!propertyLocations.find(By.xpath("//div[contains(@id, ':LocationsEdit_LV-body')]/div/table/tbody/child::tr[last()]/child::td[5]/div/a")).getText().contains(State.Washington.getAbbreviation())) {
            Assert.fail("Expected State :Washington is not added");
        }
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();
    }


    private void removeLocationAndValidateCoveragePage() throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        int previousCoveragesCount = coverages.getPropertyTableRowCount();
        coverages.clickSpecificBuilding(1, 1);
        coverages.clickSpecificBuilding(2, 2);
        // Selecting edit for 2nd Address
        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        propertyDetail.highLightPropertyLocationByNumber(1);
        propertyDetail.setCheckBoxByRowInPropertiesTable(1, true);
        propertyDetail.clickRemoveProperty();
        sideMenu.clickSideMenuPropertyLocations();
        propertyLocations.setLocationRowToPrimary(2);
        propertyLocations.SelectLocationsCheckboxByRowNumber(1);
        propertyLocations.clickRemoveButton();

        //checking coverage
        sideMenu.clickSideMenuSquirePropertyCoverages();
        int coveragesCount = coverages.getPropertyTableRowCount();
        coverages.clickSpecificBuilding(2, 2);
        if (previousCoveragesCount > coveragesCount)
            System.out.println("coverages count " + coveragesCount);
        else
            Assert.fail("Unexpected locations list on the coverage page");

    }

    private void addPLSectionProperties(int number) throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);

        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        propertyDetail.highLightPropertyLocationByNumber(number);
        PLPolicyLocationProperty property1 = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises);
        propertyDetail.fillOutPropertyDetails_FA(property1);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        constructionPage.fillOutPropertyConstrustion_FA(property1);
        constructionPage.setLargeShed(false);
        constructionPage.clickProtectionDetailsTab();
        GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);
        new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails(driver).fillOutPropertyProtectionDetails(property1);
        protectionPage.clickOK();
        int BuildingNumber = propertyDetail.getSelectedBuildingNum();
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverages.clickSpecificBuilding(2, BuildingNumber);
        coverages.setCoverageALimit(40000);
        coverages.selectCoverageCCoverageType(CoverageType.BroadForm);
        coverages.setCoverageAIncreasedReplacementCost(property1.getPropertyCoverages().getCoverageA().isIncreasedReplacementCost());
        coverages.setCoverageCValuation(property1.getPropertyCoverages());
    }


    private void addPLSection1Locations(State state, String county) throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPropertyLocations();

        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        propertyLocations.clickNewLocation();
        propertyLocations.selectLocationAddress("New...");
        propertyLocations.addLocationInfoFA(new AddressInfo(), 2, 2);
        propertyLocations.clickStandardizeAddress();
        propertyLocations.clickOK();

        //out of IDaho
        propertyLocations.clickNewLocation();
//        propertyLocations.selectAddressType("Out of State");
//        propertyLocations.selectState(state);
//        propertyLocations.setCounty(county);
//        propertyLocations.setAddressLegal("Testing purpose");
//        propertyLocations.setAcres(2);
        propertyLocations.clickOK();

    }


    private GeneratePolicy createPLAutoPolicy() throws Exception {

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

        squirePLObject = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Anderson", "Thomas")
                .build(GeneratePolicyType.FullApp);

        return squirePLObject;
    }

}

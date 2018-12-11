package scratchpad.steve;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.OrganizationTypePL;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.ElectricalSystem;
import repository.gw.enums.Property.FoundationType;
import repository.gw.enums.Property.Garage;
import repository.gw.enums.Property.KitchenBathClass;
import repository.gw.enums.Property.NumberOfStories;
import repository.gw.enums.Property.NumberOfUnits;
import repository.gw.enums.Property.Plumbing;
import repository.gw.enums.Property.PrimaryHeating;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.RoofType;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.enums.Property.SprinklerSystemType;
import repository.gw.enums.Property.Wiring;
import repository.gw.enums.QuoteType;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.driverConfiguration.DriverBuilder;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuPolicyPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderSquireEligibility;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;
import repository.pc.workorders.submission.SubmissionCreateAccount;
import repository.pc.workorders.submission.SubmissionNewSubmission;
import repository.pc.workorders.submission.SubmissionProductSelection;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author Steve Broderick
 * @Requirement US6231, US6232, US4759
 * @RequirementsLink <a href=
 * "https://rally1.rallydev.com/#/33274298124d/detail/userstory/47869497203">
 * PL - PC8 - HO - QuoteApplication - Homeowner's Property
 * Details - Location</a>
 * @Description Tests the Locations page, including protection Detail, Detail
 * Tab, and Construction Detail.
 * @DATE Dec 09, 2015
 */
public class SquirePropertyDetails extends BaseTest {
    private AddressInfo newLocation = new AddressInfo(true);
    private ArrayList<AddressInfo> locations = new ArrayList<AddressInfo>();
    private Underwriters uw;
    private String acctNum;
    private Agents agent;
    private List<String> propertyExclusions = new ArrayList<String>();
    private List<String> lineLevelExclusions = new ArrayList<String>();
    private WebDriver driver;


    @SuppressWarnings("unused")
    @Test
    public void generate() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        this.driver = new DriverBuilder().buildGWWebDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        this.agent = AgentsHelper.getRandomAgent();
        new Login(driver).login(agent.getAgentUserName(), agent.getAgentPassword());
        locations.add(newLocation);
        locations.add(new AddressInfo(true));
        TopMenuPolicyPC menuPolicy = new TopMenuPolicyPC(driver);
        menuPolicy.clickNewSubmission();
        String pniLastName = "Underhill";
        SubmissionNewSubmission newSubmissionPage = new SubmissionNewSubmission(driver);
        AddressInfo addressInfo = new AddressInfo();
        newSubmissionPage.fillOutFormSearchAndEitherCreateNewOrUseExisting(true, CreateNew.Create_New_Always,
                ContactSubType.Person, pniLastName, "Mr", null, null, addressInfo.getCity(), addressInfo.getState(),
                addressInfo.getZip());

        SubmissionCreateAccount createAccountPage = new SubmissionCreateAccount(driver);
        Date birthDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        birthDate = DateUtils.dateAddSubtract(birthDate, DateAddSubtractOptions.Year, -16);
        createAccountPage.setDOB(birthDate);
        createAccountPage.setSubmissionCreateAccountBasicsSSN(
                Integer.toString(NumberUtils.generateRandomNumberInt(111111111, 999999998)));
        createAccountPage.clickAddresses();
        createAccountPage.setAddressOnAddressDetails(locations);
        createAccountPage.clickSubmissionCreateAccountUpdate();

        if (createAccountPage.finds(By.xpath("//span[contains(@id, 'AddressStandardizationPopup:LocationScreen:ttlBar') and contains(text(), 'Location Information')]"))
                .size() > 0) {
            createAccountPage.getSubmissionCreateAccountAddressNotFound();
            createAccountPage.updateAddressStandardization();
        }
        SubmissionProductSelection selectProductPage = new SubmissionProductSelection(driver);
        selectProductPage.startQuoteSelectProductAndGetAccountNumber(QuoteType.FullApplication, ProductLineType.Squire);

        GenericWorkorderSquireEligibility eligibilityPage = new GenericWorkorderSquireEligibility(driver);
        eligibilityPage.chooseCity();
        eligibilityPage.clickNext();

//		IGenericWorkorderLineSelectionPL lineSelect = GenericWorkorderFactory.getGenericWorkorderLineSelectionPL();
//		lineSelect.checkPersonalPropertyLine(true);
//		lineSelect.checkInlandMarine(false);
//		lineSelect.checkPersonalAutoLine(false);
//		lineSelect.clickNext();

        InfoBar myInfoBar = new InfoBar(driver);
        this.acctNum = myInfoBar.getInfoBarAccountNumber();

        GenericWorkorderQualification quals = new GenericWorkorderQualification(driver);
        quals.setSquireGeneralFullTo(false);
        quals.setSquireHOFullTo(false, "Stayed in Motels when I was in a biker gang.");
        quals.setSquireGLFullTo(false);
        quals.clickQualificationNext();

        GenericWorkorderPolicyInfo policyInfoPage = new GenericWorkorderPolicyInfo(driver);
        GenericWorkorder versionCheck = new GenericWorkorder(driver);
        boolean found = versionCheck.checkIfVersionExists();
        versionFound(found);
        policyInfoPage.setPolicyInfoOrganizationType(OrganizationTypePL.Individual);
        policyInfoPage.setPolicyInfoBillingCounty("Ada");
        policyInfoPage.setPolicyInfoDuesCounty("Ada");

        SideMenuPC sidebar = new SideMenuPC(driver);

        sidebar.clickSideMenuSquireProperty();

        GenericWorkorderSquirePropertyAndLiabilityLocation myProperty = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        versionCheck = new GenericWorkorder(driver);
        found = versionCheck.checkIfVersionExists();
        versionFound(found);
        String[] sections = {"36", "35", "34", "33", "32", "31", "30", "29", "28", "27"};

//        boolean empty = myProperty.checkForEmpty(); //one off methods should be in your test
//		if(empty){
//			throw new GuidewirePolicyCenterException(Configuration.getWebDriver().getCurrentUrl(), acctNum, "On the Locations page, the Empty add existing option was found.");
//		}
        sidebar = new SideMenuPC(driver);
        sidebar.clickSideMenuSquireProperty();
        myProperty = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        //Quick test for locations
        List<String> severalAddresses = myProperty.getAddresses();
        if (severalAddresses.size() != this.locations.size()) {
            Assert.fail(myProperty.getCurrentUrl() + "Not all addresses on the policy show up on the Add Locations drop down.");
        }
        myProperty.clickNewLocation();
        myProperty = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
//		myProperty.addLocationInfo(newLocation.getLine1(), "", "", newLocation.getCity(), newLocation.getState(),
//				newLocation.getZip(), newLocation.getCounty(), sections, 1);
//        myProperty.selectTownshipNumber("");
//        myProperty.selectTownshipDirection("");
//        myProperty.selectRangeNumber("");
//        myProperty.selectRangeDirection("");
        myProperty.clickOK();

        SideMenuPC sideBar = new SideMenuPC(driver);
        sideBar.clickSideMenuSquirePropertyDetail();

        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        versionCheck = new GenericWorkorder(driver);
        found = versionCheck.checkIfVersionExists();
        versionFound(found);
        propertyDetail.clickAdd();

        //This bit of code tests that the Property Additional Interests label is on the page.  See DE 3251.
        String lienLabel = propertyDetail.getAdditionalInsuredNameByRowNumber(1);
        if (!lienLabel.equals("Property Additional Interests")) {
            Assert.fail(propertyDetail.getCurrentUrl() + "Please ensure the Property Additional Interests label exists and is correct.");
        }

        propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        propertyDetail.setPropertyType(PropertyTypePL.ResidencePremises);
        propertyDetail.setDwellingVacantRadio(false);
        propertyDetail.setUnits(NumberOfUnits.OneUnit);
        propertyDetail.setWoodFireplaceRadio(false);
        propertyDetail.setExoticPetsRadio(false);
        propertyDetail.setSwimmingPoolRadio(false);
        propertyDetail.setWaterLeakageRadio(false);
        propertyDetail.clickPropertyConstructionTab();

        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        versionCheck = new GenericWorkorder(driver);
        found = versionCheck.checkIfVersionExists();
        versionFound(found);
        constructionPage.setYearBuilt(1993);
        constructionPage.setConstructionType(ConstructionTypePL.Frame);
        constructionPage.setBathClass(KitchenBathClass.Basic);
        constructionPage.setSquareFootage(1700);
        constructionPage.setStories(NumberOfStories.One);
        constructionPage.setGarage(Garage.AttachedGarage);
        constructionPage.setLargeShed(false);
        constructionPage.setCoveredPorches(false);
        constructionPage.setFoundationType(FoundationType.FullBasement);
        constructionPage.setKitchenClass(KitchenBathClass.Basic);
        constructionPage.setRoofType(RoofType.WoodShingles);
        constructionPage.setAmps(100);
        constructionPage.setPrimaryHeating(PrimaryHeating.Gas);
        constructionPage.setPlumbing(Plumbing.Copper);
        constructionPage.setWiring(Wiring.Copper);
        constructionPage.setElectricalSystem(ElectricalSystem.CircuitBreaker);
        constructionPage.clickProtectionDetailsTab();

        GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);
        versionCheck = new GenericWorkorder(driver);
        found = versionCheck.checkIfVersionExists();
        versionFound(found);
        protectionPage.setSprinklerSystemType(SprinklerSystemType.Full);
        protectionPage.setFireExtinguishers(true);
        protectionPage.setSmokeAlarm(true);
        protectionPage.setNonSmoker(true);
        protectionPage.setDeadBoltLocks(true);
        protectionPage.setDefensibleSpace(true);
        protectionPage.clickOK();
        protectionPage.clickNext();

        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverages.selectSectionIDeductible(SectionIDeductible.OneHundred);
        coverages.setCoverageALimit(200000);
        coverages.setCoverageCLimit(140000);
        coverages.selectCoverageCCoverageType(CoverageType.BroadForm);
        coverages.clickNext();
        versionCheck = new GenericWorkorder(driver);
        found = versionCheck.checkIfVersionExists();
        versionFound(found);
    }

    /**
     * @Author Steve Broderick
     * @Requirement US4762
     * @RequirementsLink <a href=
     * "https://rally1.rallydev.com/#/33274298124d/detail/userstory/34235056945">
     * PL - HO - Homeowner's Exclusions and Conditions</a>
     * @Description Test that Agents Can see but not edit the Exclusions and
     * Conditions
     * @DATE Dec 10, 2015
     */

    @Test(dependsOnMethods = {"generate"})
    public void uwExclusions() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        this.driver = new DriverBuilder().buildGWWebDriver(cf);
        Login login = new Login(driver);
        login.loginAndSearchJob(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), acctNum);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyCoverages();

        GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions exclude = new GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions(driver);
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        exclude.clickCoveragesExclusionsAndConditions();
        GenericWorkorder versionCheck = new GenericWorkorder(driver);
        boolean found = versionCheck.checkIfVersionExists();
        versionFound(found);


        //exclude.addAllExclusionsAndConditions();


        exclude.clickCoveragesTab();

        coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverages.clickBuildingsExclusionsAndConditions();
        coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);


        //coverages.checkAllEndorsements();


        coverages.clickNext();
        new GuidewireHelpers(driver).logout();

    }

    @Test(dependsOnMethods = {"uwExclusions"})
    public void agentViewsExclusions() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        this.driver = new DriverBuilder().buildGWWebDriver(cf);
        new Login(driver).loginAndSearchJob(agent.getAgentUserName(), agent.getAgentPassword(), this.acctNum);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyCoverages();

        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);


        GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions exclusions = new GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions(driver);
        exclusions.clickCoveragesExclusionsAndConditions();
        //		propertyExclusions.add(exclusions.getExclusion("280"));
//		propertyExclusions.add(exclusions.getExclusion("209"));
//		propertyExclusions.add(exclusions.getExclusion("105"));
//		propertyExclusions.add(exclusions.getExclusion("205"));
//		propertyExclusions.add(exclusions.getExclusion("207"));
//		propertyExclusions.add(exclusions.getExclusion("291"));
        exclusions.clickCoveragesTab();

        GenericWorkorder versionCheck = new GenericWorkorder(driver);
        boolean found = versionCheck.checkIfVersionExists();
        versionFound(found);

        coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverages.clickBuildingsExclusionsAndConditions();
        coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
//		lineLevelExclusions.add(coverages.getEndorsementByText("107"));
//		lineLevelExclusions.add(coverages.getEndorsementByText("110"));
//		lineLevelExclusions.add(coverages.getEndorsementByText("106"));
//		lineLevelExclusions.add(coverages.getEndorsementByText("134"));
//		lineLevelExclusions.add(coverages.getEndorsementByText("135"));

        if (propertyExclusions.size() != 6) {
            Assert.fail(coverages.getCurrentUrl() + acctNum +
                    "The agent cannot see all Property Exclusions that the Underwriter added.");
        }
        if (lineLevelExclusions.size() != 5) {
            Assert.fail(coverages.getCurrentUrl() + acctNum +
                    "The agent cannot see all Property Exclusions that the Underwriter added.");
        }

        versionCheck = new GenericWorkorder(driver);
        found = versionCheck.checkIfVersionExists();
        versionFound(found);
    }

    private void versionFound(boolean trueFalse) {
        if (trueFalse) {
            Assert.fail(driver.getCurrentUrl() + acctNum +
                    "Check if Side By Side submission is available.  It should not be.");
        }
    }

}
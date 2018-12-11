package regression.r2.noclock.policycenter.submission_misc.subgroup1;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.enums.CountyIdaho;

import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.QuoteType;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.actions.ActionsPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderBusinessownersLineAdditionalCoverages;
import repository.pc.workorders.generic.GenericWorkorderBusinessownersLineIncludedCoverages;
import repository.pc.workorders.generic.GenericWorkorderLocations;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.submission.SubmissionProductSelection;

/**
 * @Author skandibanda
 * @Requirement : DE4258: Common - Editing same location on a policy is bleeding across other policies on the account*
 * @Description : Edit any location on policy should never edit the location on any other policies
 * @DATE Nov 08, 2016
 */
public class TestEditLocationsMultiplePoliciesUnderSameAccount extends BaseTest {

    public PolicyBusinessownersLine busOwnLine;
    public GeneratePolicy stdFireLiab_Liab_PolicyObj;
    private GeneratePolicy propertyLiabilityPol;
    private GeneratePolicy myStandardFirePol;
    String newAddress = "116 W 300 N";

    private WebDriver driver;

    @Test()
    public void testGenerateStandardLiabilityPolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList1 = new ArrayList<PolicyLocation>();
        locationsList1.add(new PolicyLocation());

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        StandardFireAndLiability myStandardLiability = new StandardFireAndLiability();
        myStandardLiability.liabilitySection = myLiab;
        myStandardLiability.setLocationList(locationsList1);

        stdFireLiab_Liab_PolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardLiability)
                .withLineSelection(LineSelection.StandardLiabilityPL)
                .withStandardLiability(myStandardLiability)
                .withInsFirstLastName("Test", "Liability")
                .build(GeneratePolicyType.QuickQuote);

        generateBOPQQ(stdFireLiab_Liab_PolicyObj);
        validateLocationsScreen(stdFireLiab_Liab_PolicyObj);

    }

    @Test()
    public void creatingPropertyLiabilityPolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();

        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation resPrem = new PolicyLocation(locOnePropertyList);
        resPrem.setPlNumAcres(11);
        resPrem.setPlNumResidence(5);
        locationsList.add(resPrem);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        propertyLiabilityPol = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsPrimaryAddress(new AddressInfo(true))
                .withInsFirstLastName("Test", "PollCov")
                .build(GeneratePolicyType.QuickQuote);

        generateBOPQQ(propertyLiabilityPol);
        validateLocationsScreen(propertyLiabilityPol);

    }

    @Test()
    public void testGenerateStandardFireQQ() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // GENERATE POLICY
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));
        PolicyLocation propLoc = new PolicyLocation(locOnePropertyList);
        propLoc.setPlNumAcres(10);
        propLoc.setPlNumResidence(5);
        locationsList.add(propLoc);

        myStandardFirePol = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withInsPrimaryAddress(new AddressInfo(true))
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("Test", "Stdfire")
                .withPolOrgType(OrganizationType.Individual)
                .withPolicyLocations(locationsList)
                .build(GeneratePolicyType.QuickQuote);

        generateBOPQQ(myStandardFirePol);
        validateLocationsScreen(myStandardFirePol);
    }


    private void generateBOPQQ(GeneratePolicy policyObj) throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(policyObj.agentInfo.getAgentUserName(), policyObj.agentInfo.getAgentPassword(), policyObj.accountNumber);
        ActionsPC actions = new ActionsPC(driver);
        actions.click_Actions();
        actions.click_NewSubmission();
        SubmissionProductSelection selectProductPage = new SubmissionProductSelection(driver);
        selectProductPage.startQuoteSelectProductAndGetAccountNumber(QuoteType.QuickQuote, ProductLineType.Businessowners);
        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
        qualificationPage.setQuickQuoteAll(false);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfoPage = new GenericWorkorderPolicyInfo(driver);
        policyInfoPage.setPolicyInfoOrganizationType(policyObj.polOrgType);
//		Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
		policyInfoPage.fillOutBusinessAndOperations(policyObj);
//		String yearBusinessStarted = DateUtils.dateFormatAsString("yyyy", DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Year, -2));
//		delay(1000);
//		policyInfoPage.setPolicyInfoYearBusinessStarted(yearBusinessStarted);
//		policyInfoPage.setPolicyInfoDescriptionOfOperations(null);		
        policyInfoPage.setPolicyInfoBillingCounty(CountyIdaho.Bannock.getValue());
        policyInfoPage.selectMembershipDuesCounty(policyObj.pniContact.getFirstName(), CountyIdaho.Bannock.getValue());
        sideMenu.clickSideMenuBusinessownersLine();
        GenericWorkorderBusinessownersLineIncludedCoverages boLineInclCovPage = new GenericWorkorderBusinessownersLineIncludedCoverages(driver);
        boLineInclCovPage.clickBusinessOwnersLineIncludedCoverages();

        boLineInclCovPage.setSmallBusinessType(SmallBusinessType.Motels);
        boLineInclCovPage.clickBusinessownersLine_AdditionalCoverages();
        GenericWorkorderBusinessownersLineAdditionalCoverages bopAddCov = new GenericWorkorderBusinessownersLineAdditionalCoverages(driver);
        policyObj.busOwnLine.getAdditionalCoverageStuff().setSellingOrChargingLiquor(false);
        bopAddCov.fillOutLiquorCoverages(policyObj.busOwnLine);
        bopAddCov.clickBusinessownersLine_ExclusionsConditions();
        sideMenu.clickSideMenuLocations();
        GenericWorkorderLocations locations = new GenericWorkorderLocations(driver);
        //		location.setLocationsLocationAddress("1292 El Rancho Blvd, Pocatello, ID");
        locations.setLocationsLocationAddress("New...");

        GenericWorkorderSquirePropertyAndLiabilityLocation location = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
//        location.setAddressLine1(newAddress);
//        location.setCity("Malad City");

        locations.clickLocationsStandardizeAddress();
        locations.clickLocationsAdditionalCoverages();
        locations.clickLocationsOk();
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
        sideMenu.clickSideMenuBuildings();
    }

    private void validateLocationsScreen(GeneratePolicy policyObj) throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchJob(policyObj.agentInfo.getAgentUserName(), policyObj.agentInfo.getAgentPassword(), policyObj.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation location = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
//        String libAddress = location.getAddressLine1();
//        if (newAddress.equals(libAddress))
//            Assert.fail("Editing same Location on a Policy is Bleeding across other policies on the Account");
    }

}

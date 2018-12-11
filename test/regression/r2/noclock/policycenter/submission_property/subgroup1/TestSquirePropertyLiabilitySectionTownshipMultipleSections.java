package regression.r2.noclock.policycenter.submission_property.subgroup1;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.policy.BCPolicyMenu;
import repository.bc.policy.PolicyCharges;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestSubType;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author nvadlamudi
 * @Requirement
 * @RequirementsLink <a href="http:// "
 * rally1.rallydev.com/#/33274298124d/detail/userstory/
 * 52176124014</a>
 * @Description :US7232
 * @DATE Mar 29, 2016
 */
@QuarantineClass
public class TestSquirePropertyLiabilitySectionTownshipMultipleSections extends BaseTest {

    public GeneratePolicy myPolicyObjPL = null;

    private WebDriver driver;

    @Test
    private void createSquirePropertyLiabilityQQ() throws Exception {
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

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Test", "Locations")
                .build(GeneratePolicyType.QuickQuote);
    }

    @Test(dependsOnMethods = {"createSquirePropertyLiabilityQQ"})
    public void addingMoreSectionsValidations() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // Login and open the same account
        new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);

        // adding locations
        validateIdahoLocations();

        // adding Out of state locations
        outofStateAddressValidations();
    }

    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Description : Adding out of state location
     * @DATE Mar 29, 2016
     */
    private void outofStateAddressValidations() throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPropertyLocations();
        boolean testFailed = false;
        String errorMessage = "";

        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        // Adding new location with State!= Idaho
//        propertyLocations.clickNewLocation();
//        propertyLocations.selectAddressType("Out of State");
//        propertyLocations.sendArbitraryKeys(Keys.TAB);
//        propertyLocations.selectState(State.Oregon);
//        propertyLocations.setCounty("Bend");
//        propertyLocations.setAddressLegal("Testing purpose");
//        propertyLocations.clickOK();
//        if (propertyLocations.getLocationsValidations().contains("Must enter at least 1 acre")) {
//            System.out.println("Expected Acres: Must enter at least 1 acre validation message is displayed");
//            propertyLocations.setAcres(2);
//            propertyLocations.clickOK();
//        } else {
//            testFailed = true;
//            errorMessage = errorMessage + "Expected Acres: Must enter at least 1 acre validation message is not displayed";
//        }

        if (testFailed)
            Assert.fail(driver.getCurrentUrl() + errorMessage);
    }

    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Description : Adding Idaho location sections
     * @DATE Mar 29, 2016
     */
    private void validateIdahoLocations() throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuHouseholdMembers();
        new GuidewireHelpers(driver).editPolicyTransaction();

        sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        propertyLocations.clickEditLocation(1);
        ArrayList<String> sections = new ArrayList<String>();
        sections.add("01");
        sections.add("02");
        sections.add("03");
        sections.add("04");
        sections.add("05");
        sections.add("06");
        sections.add("07");
//        propertyLocations.selectLocationAddress("New");
//        propertyLocations.setAddressLine1(myPolicyObjPL.pniContact.getAddress().getLine1());
//        propertyLocations.setCity(myPolicyObjPL.pniContact.getAddress().getCity());
//        propertyLocations.setZipCode(myPolicyObjPL.pniContact.getAddress().getZip());
//        propertyLocations.selectCounty(myPolicyObjPL.pniContact.getAddress().getCounty());
//        propertyLocations.addSection(sections);
//        propertyLocations.sendArbitraryKeys(Keys.TAB);
//        propertyLocations.clickStandardizeAddress();
//        TownshipRange townshipRange = TownshipRangeHelper.getRandomTownshipRangeForCounty(myPolicyObjPL.pniContact.getAddress().getCounty());
//        propertyLocations.selectTownshipNumber(townshipRange.getTownship());
//        propertyLocations.selectTownshipDirection(townshipRange.getTownshipDirection());
//        propertyLocations.selectRangeNumber(townshipRange.getRange());
//        propertyLocations.selectRangeDirection(townshipRange.getRangeDirection());
        propertyLocations.clickOK();

    }


    @Test
    public void testChargeGroupInBC() throws Exception {
        SoftAssert softassert = new SoftAssert();

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Date today = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        Date previousFive = DateUtils.dateAddSubtract(today, DateAddSubtractOptions.Day, -5);

        AdditionalInterest propertyAdditionalInterest = new AdditionalInterest(ContactSubType.Company);
        propertyAdditionalInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
        propertyAdditionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
        propertyAdditionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_All);
        propertyAdditionalInterest.setAdditionalInterestSubType(AdditionalInterestSubType.PLSectionIProperty);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();

        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        PLPolicyLocationProperty locationOneProperty = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        locationOneProperty.setBuildingAdditionalInterest(propertyAdditionalInterest);
        locOnePropertyList.add(locationOneProperty);

        ArrayList<PLPolicyLocationProperty> locTwoPropertyList = new ArrayList<PLPolicyLocationProperty>();
        PLPolicyLocationProperty locationTwoProperty = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises);
        locationTwoProperty.setBuildingAdditionalInterest(propertyAdditionalInterest);
        locTwoPropertyList.add(locationTwoProperty);

        PolicyLocation locTwo = new PolicyLocation();
        locTwo.setAddress(new AddressInfo(true));
        locTwo.setPlNumAcres(10);
        locTwo.setPlNumResidence(1);
        locTwo.setPropertyList(locTwoPropertyList);

        locationsList.add(new PolicyLocation(locOnePropertyList));
        locationsList.add(locTwo);

        SquireLiability liabilitySection = new SquireLiability();

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;

        Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        GeneratePolicy myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withInsFirstLastName("STR", "Defect")
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withPolEffectiveDate(previousFive)
                .withPaymentPlanType(PaymentPlanType.Quarterly)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        Login login = new Login(driver);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        login.loginAndSearchPolicyByAccountNumber(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickPolicyContractQuote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        double propertyPremium = quote.getSectionOnePropertyPremium(myPolicyObj.squire.propertyAndLiability.locationList.get(0).getNumber() + "", myPolicyObj.squire.propertyAndLiability.locationList.get(0).getFullAddressString(), myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getPropertyNumber() + "", myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getpropertyType().getValue());
        quote.clickSectionISectionIIPolicyPremium();

        double sectionIIPremium = NumberUtils.getCurrencyValueFromElement(quote.premiumByDescription("Additional Residence"));
        double generalLiabilityPremium = NumberUtils.getCurrencyValueFromElement(quote.premiumByDescription("General Liability (Acres)"));
        double liabilityPremium = generalLiabilityPremium + sectionIIPremium;

        guidewireHelpers.logout();
        ARUsers bcLogin = ARUsersHelper.getRandomARUser();
        login.loginAndSearchPolicyByAccountNumber(bcLogin.getUserName(), bcLogin.getPassword(), myPolicyObj.accountNumber);
        BCPolicyMenu menu = new BCPolicyMenu(driver);
        menu.clickBCMenuCharges();
        PolicyCharges policyCharges = new PolicyCharges(driver);
        System.out.println("Looking for charge group with Amount of: " + liabilityPremium);
        System.out.println("Found " + policyCharges.getChargeGroupForAmount(liabilityPremium));
        softassert.assertTrue(policyCharges.getChargeGroupForAmount(liabilityPremium).contains("1:" + myPolicyObj.squire.propertyAndLiability.locationList.get(0).getFullAddressString() + "-" + myPolicyObj.squire.propertyAndLiability.locationList.get(0).getAddress().getZip4()),
                "Liability with expected charge group : '1:" + myPolicyObj.squire.propertyAndLiability.locationList.get(0).getFullAddressString() + "-" + myPolicyObj.squire.propertyAndLiability.locationList.get(0).getAddress().getZip4() + "' is not displayed");

        System.out.println("Looking for charge group with Amount of: " + propertyPremium);
        System.out.println("Found " + policyCharges.getChargeGroupForAmount(propertyPremium));
        softassert.assertTrue(policyCharges.getChargeGroupForAmount(propertyPremium).contains("1:1:" + myPolicyObj.squire.propertyAndLiability.locationList.get(0).getFullAddressString() + "-" + myPolicyObj.squire.propertyAndLiability.locationList.get(0).getAddress().getZip4()),
                "Property with expected charge group : '1:1:" + myPolicyObj.squire.propertyAndLiability.locationList.get(0).getFullAddressString() + "-" + myPolicyObj.squire.propertyAndLiability.locationList.get(0).getAddress().getZip4() + "' is not displayed");

        softassert.assertAll();
    }
}

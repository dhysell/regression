package regression.r2.noclock.policycenter.submission_property.subgroup2;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderLocations;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;

/**
 * @Author skandibanda
 * @Requirement : DE3875 : R2 BOP - Missing Additional Interest Type field and user is stuck with Locations validation message
 * @Description - Test Standardize address by entering incorrect address under locations screen, also verify item exists under
 * buildings screen -Interest Type, First Mortgage, Applied to Building, Applied to BPP, Loan or Contract Number
 * @DATE Sep 20, 2016
 */
@QuarantineClass
public class TestBOPAdditionalInterestAndLocations extends BaseTest {
    private GeneratePolicy bopPolicyObj;

    private WebDriver driver;


    ////////////////////////////////////////////
    //  jlarsen 2/9/2017
    //  TESTS DISABLED FOR THIS WILL BE TESTED WITH EVERY ADDITIONAL INTEREST BOP TEST.
    ////////////////////////////////////////////


    @Test(enabled = false)
    public void testGenerateBusinessownersPolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        bopPolicyObj = new GeneratePolicy.Builder(driver)
                .withInsCompanyName("Test BOP")
                .withBusinessownersLine()
                .build(GeneratePolicyType.QuickQuote);
    }

    @Test(dependsOnMethods = {"testGenerateBusinessownersPolicy"}, enabled = false)
    public void testVerifyItemsExistsUnderBuildingsScreen() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(bopPolicyObj.agentInfo.getAgentUserName(), bopPolicyObj.agentInfo.getAgentPassword(), bopPolicyObj.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuLocations();

        GenericWorkorderLocations locations = new GenericWorkorderLocations(driver);
        locations.clickPrimaryLocationEdit();
        locations.setLocationsLocationAddress("New...");
        locations.setLocationsAddressLine1(StringsUtils.generateRandomNumberDigits(5));
        locations.setLocationsCity("Pocatello");
        locations.setLocationsZipCode("83201");

        locations.locationStandardizeAddress();
        locations.clickLocationsOk();

        sideMenu.clickSideMenuBuildings();
        GenericWorkorderBuildings buildings = new GenericWorkorderBuildings(driver);
        buildings.clickBuildingsBuildingEdit(1);

        AddressInfo bankAddress = new AddressInfo();
        AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
        loc2Bldg1AddInterest.setNewContact(CreateNew.Create_New_Always);
        loc2Bldg1AddInterest.setAddress(bankAddress);
        loc2Bldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LessorPL);
        GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
        additionalInterests.clickBuildingsPropertyAdditionalInterestsSearch();

        SearchAddressBookPC search = new SearchAddressBookPC(driver);
        search.searchForContact(bopPolicyObj.basicSearch, loc2Bldg1AddInterest);
        boolean testFailed = false;
        String errorMessage = "";

        if (!additionalInterests.checkInterestTypeExists()) {
            testFailed = true;
            errorMessage = errorMessage + "Interest Type field is missing";
        }

        if (!additionalInterests.checkFirstMortgageeExists()) {
            testFailed = true;
            errorMessage = errorMessage + "First Mortgage field is missing";
        }
        if (!additionalInterests.checkAppliedToBuildingExists()) {
            testFailed = true;
            errorMessage = errorMessage + "Applied To Building field is missing";
        }
        if (!additionalInterests.checkAppliedToBPPExists()) {
            testFailed = true;
            errorMessage = errorMessage + "Applied To BPP field is missing";
        }

        if (!additionalInterests.checkContractNumberExists()) {
            testFailed = true;
            errorMessage = errorMessage + "Contract Number field is missing";
        }

        if (testFailed)
            Assert.fail(errorMessage);

    }
}

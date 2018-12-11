package previousProgramIncrement.pi2_062818_090518.nonFeatures.Scope_Creeps;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.account.AccountSummaryPC;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;

import java.util.Date;
import java.util.List;

/**
 * @Author swathiAkarapu
 * @Requirement
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558461772ud/detail/userstory/51f54b6d-ee73-4b72-9fd6-ed41aa56c070"></a>
 * @Description
 * Steps to get there on new business or any policy change adding a driver:
 * Start a new policy
 * Also to a policy change adding a new driver on an in force policy.
 * Acceptance criteria on new business or any policy change adding a driver:
 * On section III when adding marital status see choice of "widowed" and not "spouse deceased"
 * @DATE August 10, 2018
 */

public class US15429_MaritalStatus extends BaseTest {

    public GeneratePolicy myPolicyObject = null;
    private WebDriver driver;


    public void generatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquirePersonalAuto spa = new SquirePersonalAuto();
        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = spa;
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withDownPaymentType(PaymentType.Cash)
                .withInsFirstLastName("ScopeCreeps", "NonFeature")
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(enabled = true)
    public void testMaritialStatusTypesWhenPolicyChange() throws Exception {

    
            generatePolicy();

        new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObject.underwriterInfo.getUnderwriterUserName(),
                myPolicyObject.underwriterInfo.getUnderwriterPassword(), myPolicyObject.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);

        SoftAssert softAssert = new SoftAssert();
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(
                driver);

        // start policy change
        Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        policyChangePage.startPolicyChange("Agent policy Change", currentSystemDate);

        sideMenu.clickSideMenuPolicyInfo();
        String firstName = "NewMem" + StringsUtils.generateRandomNumberDigits(7);
        addNewhouseholdMember(myPolicyObject.basicSearch, firstName,
                "Added" + StringsUtils.generateRandomNumberDigits(7), this.myPolicyObject.pniContact.getAddress(),
                DateUtils.dateAddSubtract(currentSystemDate,
                        DateAddSubtractOptions.Year, -25));

        sideMenu.clickSideMenuPADrivers();
        paDrivers.addExistingDriver(firstName);
        List<String> maritalStatusList=paDrivers.getMaritalStatusValues();
        softAssert.assertTrue(maritalStatusList.contains("Widowed"), "Option Widowed should  present, but not found");
        softAssert.assertAll();

    }

    @Test(enabled = true)
    public void testMaritialStatusTypesWhenNewSubmission() throws Exception {
       Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        SquirePersonalAuto spa = new SquirePersonalAuto();
        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = spa;
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withDownPaymentType(PaymentType.Cash)
                .withInsFirstLastName("ScopeCreeps", "NonFeature")
                .isDraft()
                .build(GeneratePolicyType.QuickQuote);

        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);
        SoftAssert softAssert = new SoftAssert();
        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(
                driver);

        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);
        pcSideMenu.clickSideMenuPolicyInfo();
        pcSideMenu.clickSideMenuPADrivers();
        paDrivers.clickEditButtonInDriverTableByDriverName(myPolicyObject.pniContact.getFirstName());
        List<String> maritalStatusList=paDrivers.getMaritalStatusValues();

        softAssert.assertTrue(maritalStatusList.contains("Widowed"), "Option Widowed should  present, but not found");
        softAssert.assertAll();
    }




    private void addNewhouseholdMember(boolean basicSearch, String firstName, String lastName, AddressInfo address, Date dob)
            throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
        household.clickSearch();
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
        addressBook.searchAddressBookByFirstLastName(basicSearch, firstName, lastName, address.getLine1(), address.getCity(),
                address.getState(), address.getZip(), CreateNew.Create_New_Always);
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        householdMember.setDateOfBirth(dob);
        householdMember.selectRelationshipToInsured(RelationshipToInsured.SignificantOther);
        householdMember.setSSN("2" + StringsUtils.generateRandomNumberDigits(8).replace("00", "34"));
        householdMember.setNewPolicyMemberAlternateID(firstName);
        householdMember.selectNotNewAddressListingIfNotExist(myPolicyObject.pniContact.getAddress());
        householdMember.clickRelatedContactsTab();
        householdMember.clickOK();
    }
}
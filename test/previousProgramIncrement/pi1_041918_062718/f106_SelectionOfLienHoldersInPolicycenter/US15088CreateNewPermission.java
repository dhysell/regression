package previousProgramIncrement.pi1_041918_062718.f106_SelectionOfLienHoldersInPolicycenter;


import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.driver.BaseTest;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.ContactRole;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.CSRs;
import persistence.globaldatarepo.entities.PAs;
import persistence.globaldatarepo.entities.SAs;
import persistence.globaldatarepo.helpers.AbUserHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.CSRsHelper;
import persistence.globaldatarepo.helpers.PAsHelper;
import persistence.globaldatarepo.helpers.SAsHelper;

/**
 * @Author sbroderick
 * @Requirements:  Field office employees are no long able to create Lienholders.
 * @RequirementsLink <a href="https://fbmicoi.sharepoint.com/:w:/r/sites/TeamNucleus/_layouts/15/Doc.aspx?sourcedoc=%7B78599F66-FB5C-461E-8836-ED5188E7B5E1%7D&file=US15088%20-%20CM%20-%20Permission%20Creation%20for%20Agents.docx&action=default&mobileredirect=true">ContactManager Requirements Documentation</a>
 * @DATE May 17, 2018
 */
public class US15088CreateNewPermission extends BaseTest {
    SoftAssert softAssert = new SoftAssert();
    GeneratePolicy saPolicy = null;
    GeneratePolicy paPolicy = null;
    GeneratePolicy csrPolicy = null;
    AdditionalInterest loc1Bldg1AddInterest = null;
    CSRs csr = null;
    WebDriver driver;

    @Test
    public void testCreateNewNotAvailableForFieldEmployees() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        int day = DateUtils.getCenterDateAsInt(driver, ApplicationOrCenter.PolicyCenter, "dd");
//        if (day % 2 == 1) {
            this.softAssert = new SoftAssert();
            testCreateNewNotAvailableForCSR();
            softAssert.assertAll();
 //       } else {
            this.softAssert = new SoftAssert();
            testCreateNewNotAvailableForFieldEmployeesSubmission();
            softAssert.assertAll();
 //       }
    }

    public void testCreateNewNotAvailableForCSR() throws Exception {

        this.csr = CSRsHelper.getRandomCSR();

        this.csrPolicy = createCSRPolicy();
        Login login = new Login(driver);
        login.loginAndSearchAccountByAccountNumber(csr.getCsruserName(), csr.getCsrPassword(), csrPolicy.accountNumber);
        AccountSummaryPC acctSummaryPage = new AccountSummaryPC(driver);
        acctSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);

        softAssert.assertFalse(createNewCheck(), "After search a Lienholder the create new was found on the page. The Create New button should not exist. Check user: " + csr.getCsruserName() + ".");
        softAssert.assertFalse(testAddressIsNotEditable(), "After search a Lienholder, the option to create a new address should not exist for a csr. Check user: " + csr.getCsruserName() + ".");
    }

    public void testCreateNewNotAvailableForFieldEmployeesSubmission() throws Exception {


        Agents agent1 = AgentsHelper.getAgentWithPA();
        Agents agent2 = AgentsHelper.getAgentWithSA();
        PAs pa = PAsHelper.getPAInfoByAgent(agent1.getAgentLastName().trim());
        SAs sa = SAsHelper.getSAInfoByAgent(agent2.getAgentLastName().trim());

        this.paPolicy = createPolicies(agent1);
        this.saPolicy = createPolicies(agent2);
        this.loc1Bldg1AddInterest = new AdditionalInterest(ContactSubType.Company);


        Login login = new Login(driver);
        login.loginAndSearchJob(agent1.getAgentUserName(), agent1.getAgentPassword(), paPolicy.accountNumber);
        softAssert.assertFalse(createNewCheck(), "After search a Lienholder the create new was found on the page. The Create New button should not exist. Check user: " + agent1.getAgentUserName() + ".");
        softAssert.assertFalse(testAddressIsNotEditable(), "The agent should not be able to change a lienholder address. The agent was " + agent1.getAgentUserName() + " on account " + paPolicy.accountNumber + ".");
        new GuidewireHelpers(driver).logout();

        if (pa == null) {
            softAssert.assertNull(pa, "A Production Assistant was not able to be found in the DB.");
        } else {
            new GuidewireHelpers(driver).logout();
            login.loginAndSearchJob(pa.getPauserName(), "gw", paPolicy.accountNumber);
            softAssert.assertFalse(createNewCheck(), "After search a Lienholder the create new was found on the page. The Create New button should not exist. Check user: " + pa.getPauserName() + ".");
            softAssert.assertFalse(testAddressIsNotEditable(), "The Production Assistant should not be able to change a lienholder address. The PA was " + pa.getPauserName() + " on account " + paPolicy.accountNumber + ".");
            new GuidewireHelpers(driver).logout();
        }
        if (sa == null) {
            softAssert.assertNull(sa, "A Service Associate was not able to be found in the DB.");
        } else {
            new GuidewireHelpers(driver).logout();
            login.loginAndSearchJob(sa.getSauserName(), "gw", saPolicy.accountNumber);
            softAssert.assertFalse(createNewCheck(), "After search a Lienholder the create new was found on the page for a Service Associate. The Create New button should not exist. Check user: " + sa.getSauserName() + ".");
            softAssert.assertFalse(testAddressIsNotEditable(), "After search a Lienholder, the option to create a new address should not exist. Check user: " + sa.getSauserName() + ".");
            new GuidewireHelpers(driver).logout();
        }
    }

    private boolean createNewCheck() throws Exception {

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();

        ArrayList<AdditionalInterest> loc1Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();

        this.loc1Bldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
        loc1Bldg1AddInterest.setFirstMortgage(true);
        loc1Bldg1AdditionalInterests.add(loc1Bldg1AddInterest);

        GenericWorkorderBuildings building = new GenericWorkorderBuildings(driver);
        building.clickEdit();
        building.clickSearch();
        SearchAddressBookPC searchPC = new SearchAddressBookPC(driver);
        searchPC.searchAddressBook(true, null, null, null, null, loc1Bldg1AddInterest.getCompanyName(), loc1Bldg1AddInterest.getAddress().getLine1(), loc1Bldg1AddInterest.getAddress().getCity(), loc1Bldg1AddInterest.getAddress().getState(), loc1Bldg1AddInterest.getAddress().getZip(), CreateNew.Do_Not_Create_New);
        return searchPC.checkIfCreateNewButtonExists();
    }

    private boolean testAddressIsNotEditable() {
        SearchAddressBookPC searchPC = new SearchAddressBookPC(driver);
        searchPC.searchForContact(true, loc1Bldg1AddInterest);
        return searchPC.checkIfCreateNewButtonExists();
    }

    private GeneratePolicy createPolicies(Agents agent) throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

        PLPolicyLocationProperty loc1Bldg1 = new PLPolicyLocationProperty();

        loc1Bldg1.setpropertyType(PropertyTypePL.ResidencePremises);

        locOnePropertyList.add(loc1Bldg1);
        locationsList.add(new PolicyLocation(locOnePropertyList, new AddressInfo()));

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withAgent(agent)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Squire", "TestAI")
                .build(GeneratePolicyType.QuickQuote);

        driver.quit();

        return myPolicyObjPL;
    }

    public GeneratePolicy createCSRPolicy() throws Exception {

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

        PLPolicyLocationProperty loc1Bldg1 = new PLPolicyLocationProperty();

        loc1Bldg1.setpropertyType(PropertyTypePL.ResidencePremises);

        locOnePropertyList.add(loc1Bldg1);
        locationsList.add(new PolicyLocation(locOnePropertyList, new AddressInfo()));

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Squire", "TestAI")
                .build(GeneratePolicyType.PolicyIssued);

        new Login(driver).loginAndSearchPolicy_asAgent(myPolicyObjPL);

        StartPolicyChange testChange = new StartPolicyChange(driver);
        testChange.startPolicyChange("Lienholder validations", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
//		testChange.clickGenericWorkorderQuote();
        new GuidewireHelpers(driver).logout();
        return myPolicyObjPL;
    }

 //   @Test
    public void testGuidewireServiceRepsCanCreateLiens() throws Exception {
        AbUsers user = AbUserHelper.getRandomUserByTitle("Guidewire Service Rep");
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
        rolesToAdd.add(ContactRole.Lienholder);

        new GenerateContact.Builder(driver)
                .withCreator(user)
                .withRoles(rolesToAdd)
                .withGeneratedLienNumber(true)
                .withUniqueName(true)
                .build(GenerateContactType.Person);

        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
        boolean found = false;
        for (String role : contactPage.getRoles()) {
            if (role.contains(ContactRole.Lienholder.getValue())) {
                found = true;
            }
        }
        if (!found) {
            Assert.fail("A Lienholder should be able to be created by a Guidewire Service Rep. Please investigate.");
        }
    }

 //   @Test
    public void thoseWithViewPermissionsCanNotCreateNewLiens() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        AbUsers user = AbUserHelper.getRandomUserByTitle("Transition Special");
        AdvancedSearchAB advancedSearchPage = new AdvancedSearchAB(driver);
        Assert.assertFalse(advancedSearchPage.loginAndCreateNewPerson(user, "Nooneinparticular"), "The create new should not be available for Transition Team members.");
    }
}

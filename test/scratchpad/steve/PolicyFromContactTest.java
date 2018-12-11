/* Steve Broderick
 *
 * This test uses generate to create a contact.
 *
 * Uses generate Contact to find the Contact.
 *
 * Uses Generate Policy to create a bound policy from the contact that was created.
 *
 * Logs into ContactManager to clear activities
 *
 * Issues Policy
 *
 * Completes Policy Change and looks for specific Created Activity
 *
 * */
package scratchpad.steve;


import java.util.ArrayList;
import java.util.Arrays;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.enums.CountyIdaho;
import com.idfbins.enums.State;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.contact.ContactWorkPlanAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.sidebar.SidebarAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.AbActivitySearchFilter;
import repository.gw.enums.AddressType;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactRole;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IssuanceType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.gw.topinfo.TopInfo;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.topmenu.TopMenuSearchPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfoContact;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;

@SuppressWarnings("unused")
public class PolicyFromContactTest extends BaseTest {

    //AB instance Data
	private WebDriver driver;
    GenerateContact myContactObj = null;
    private AbUsers user;
    private String firstName = "Denry";
    private String lastName = "Hystensen";
    private String addressLine1 = "261 Collins St";
    private String addressLine2 = "apt A";
    private String city = "Blackfoot";
    private CountyIdaho idahoCounty = CountyIdaho.Bingham;
    private State idaho = State.Idaho;
    private String zip = "83221";
    private String businessPhone = "2082394369";
    private ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>(Arrays.asList(ContactRole.ClaimParty));
    private AddressInfo primaryAddress = new AddressInfo(addressLine1, addressLine2, city, idaho, zip, idahoCounty, "United States of America", AddressType.Business);
    //PC Instance Data
    private boolean createNew = false;
    private AddressInfo newAddress = new AddressInfo("275 Tierra Vista Dr", "", "Pocatello", State.Idaho, "83201", CountyIdaho.Bannock, "United States of America", AddressType.Business);
    GeneratePolicy myPolicyBoundObj = null;

    // If you would like more roles use below
    // private ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>(Arrays.asList((ContactRole.ClaimParty),(ContactRole.Vendor)));

    @Test
    public void testNewPersonAB() throws Exception {

        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        this.user = AbUserHelper.getRandomDeptUser("Numbering");
        this.myContactObj = new GenerateContact.Builder(driver)
                .withCreator(user)
                .withFirstLastName(firstName, lastName)
                .withRoles(rolesToAdd)
                .withPrimaryAddress(primaryAddress)
                .build(GenerateContactType.Person);

        System.out.println(myContactObj.firstName);
        System.out.println(myContactObj.lastName);
        System.out.println(myContactObj.accountNumber);


        ContactDetailsBasicsAB newContact = new ContactDetailsBasicsAB(driver);
        String name = newContact.getContactPageTitle();
        if (name.equals(myContactObj.firstName + " " + myContactObj.lastName)) {
            TopInfo myInfo = new TopInfo(driver);
            myInfo.clickTopInfoLogout();
        } else {
            throw new Exception("The Contact Name does not match what was set up.");
        }
    }

    @Test(dependsOnMethods = {"testNewPersonAB"})
    public void createPolicyFromAB() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        //*************************************************************************
        //this Will not pull an account from ContactManager because of Defect 2348.
        //*************************************************************************

        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));
        this.myPolicyBoundObj = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withCreateNew(CreateNew.Do_Not_Create_New)
                .withInsFirstLastName(myContactObj.firstName, myContactObj.lastName)
                .withPolOrgType(OrganizationType.Joint_Venture)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicySubmitted);

        System.out.println(myPolicyBoundObj.accountNumber);
        System.out.println(myPolicyBoundObj.busOwnLine.getPolicyNumber());
        System.out.println(myPolicyBoundObj.underwriterInfo.getUnderwriterUserName());
    }

    @Test(dependsOnMethods = {"createPolicyFromAB"})
    public void removeActivity() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        Login logMeIn = new Login(driver);
        logMeIn.login(user.getUserName(), user.getUserPassword());

        TopMenuAB menu = new TopMenuAB(driver);
        AdvancedSearchAB search = new AdvancedSearchAB(driver);

        menu.clickSearchTab();
      
        search.searchByFirstLastName(myContactObj.firstName, myContactObj.lastName, myContactObj.addresses.get(0).getLine1());

        SidebarAB contactWorkPlan = new SidebarAB(driver);
        contactWorkPlan.clickSidebarContactWorkplanLink();

        ContactWorkPlanAB activity = new ContactWorkPlanAB(driver);

        activity.selectContactWorkPlanActivityFilter(AbActivitySearchFilter.AllOpen);
//		activity.clickActivityCheckbox("Review Contact");
//		activity.clickAssignButton();
//		activity.selectContactWorkPlanAssignActivities(userName);
//		activity.clickAssignActivitiesAssignButton();
//		
//		activity = ContactFactory.getContactWorkPlanPage();
//		activity.clickActivityCheckbox("Review Contact");
//		activity.clickContactWorkplanCompleteButton();

        TopInfo logout = new TopInfo(driver);
        logout.clickTopInfoLogout();
    }

    @Test(dependsOnMethods = {"removeActivity"})
    public void issuePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Login logMeIn = new Login(driver);
        logMeIn.login(myPolicyBoundObj.underwriterInfo.getUnderwriterUserName(), myPolicyBoundObj.underwriterInfo.getUnderwriterPassword());

        TopMenuSearchPC getToSearch = new TopMenuSearchPC(driver);
        getToSearch.clickAccounts();

        SearchPoliciesPC policySearch = new SearchPoliciesPC(driver);
        policySearch.searchPolicyByAccountNumber(myPolicyBoundObj.accountNumber);

        //IAccountSummary accountSummaryPage = AccountFactory.getAccountSummaryPage();
        //accountSummaryPage.clickPolicyNumber("08-300273-01");
        PolicyMenu issuePolicy = new PolicyMenu(driver);
        issuePolicy.clickMenuActions();
        issuePolicy.clickIssuePolicy();

        GenericWorkorderQuote quoteMe = new GenericWorkorderQuote(driver);
        quoteMe.clickQuote();
        quoteMe.issuePolicy(IssuanceType.NoActionRequired);

        GenericWorkorderComplete getToPolicy = new GenericWorkorderComplete(driver);
        getToPolicy.clickViewYourPolicy();

//		getToSearch = pc.topmenu.TopMenuFactory.getMenuSearch();
//		getToSearch.clickPolicies();
//		
//		policySearch = gw.search.SearchFactory.getPolicySearch();
//		policySearch.searchPolicyByAccountNumber(myPolicyBoundObj.accountNumber);
    }

    @Test(dependsOnMethods = {"issuePolicy"})
    public void changePolicyUW() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Login logMeIn = new Login(driver);
        logMeIn.login(myPolicyBoundObj.underwriterInfo.getUnderwriterUserName(), myPolicyBoundObj.underwriterInfo.getUnderwriterPassword());
        TopMenuSearchPC getToSearch = new TopMenuSearchPC(driver);
        getToSearch.clickPolicies();

        SearchPoliciesPC policySearch = new SearchPoliciesPC(driver);
        policySearch.searchPolicyByAccountNumber(myPolicyBoundObj.accountNumber);

        PolicyMenu initiatePolicyChange = new PolicyMenu(driver);
        initiatePolicyChange.clickMenuActions();
        initiatePolicyChange.clickChangePolicy();

        StartPolicyChange addressChange = new StartPolicyChange(driver);
        addressChange.setDescription("Mailing Address Change");
        addressChange.setEffectiveDate(DateUtils.convertStringtoDate("12/01/2016", "MM/dd/yyyy"));
        addressChange.clickPolicyChangeNext();

        GenericWorkorderPolicyInfo getPolicyInfo = new GenericWorkorderPolicyInfo(driver);
        getPolicyInfo.clickPolicyInfoPrimaryNamedInsured();

        GenericWorkorderPolicyInfoContact contactPage = new GenericWorkorderPolicyInfoContact(driver);
        contactPage.setPolicyInfoContactAddressListingSelectList("New");
//        contactPage.setPolicyChangePolicyInfoContactAddressListingSelectList("New");
        contactPage = new GenericWorkorderPolicyInfoContact(driver);
        contactPage.setPolicyInfoContactAddressLine1(newAddress);
        contactPage.setPolicyInfoContactState(newAddress.getState().getName());
//        contactPage.setPolicyChangePolicyInfoContactState(newAddress);
        contactPage.setPolicyInfoContactAddressLine2(newAddress);
        contactPage.setPolicyInfoContactCity(newAddress);
        contactPage.setPolicyInfoContactZip(newAddress);
        contactPage.setPolicyChangePolicyInfoContactAddressType(AddressType.Home.getValue());
        contactPage.setPolicyInfoContactChangeReason("Moved");
        contactPage.setPolicyInfoContactCounty(newAddress);
        contactPage.clickPolicyInfoContactOk();

        new GenericWorkorder(driver).clickGenericWorkorderQuote();
        getPolicyInfo = new GenericWorkorderPolicyInfo(driver);
        GenericWorkorderQuote quoteChange = new GenericWorkorderQuote(driver);
        quoteChange.issuePolicy(IssuanceType.Issue);
    }

    @Test(dependsOnMethods = {"changePolicyUW"})
    public void checkPolicyChangeAB() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        new AdvancedSearchAB(driver).loginAndSearchContact(user, myPolicyBoundObj.pniContact.getFirstName(), myPolicyBoundObj.pniContact.getLastName(), myPolicyBoundObj.pniContact.getAddress().getLine1(), State.Idaho);
        SidebarAB contactWorkPlan = new SidebarAB(driver);
        contactWorkPlan.clickSidebarContactWorkplanLink();

    }

}

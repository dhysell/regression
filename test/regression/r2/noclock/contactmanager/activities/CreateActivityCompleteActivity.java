package regression.r2.noclock.contactmanager.activities;


import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.ab.contact.ContactWorkPlanAB;
import repository.ab.newactivity.NewActivity;
import repository.ab.search.ActivitySearchAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.sidebar.SidebarAB;
import repository.ab.topmenu.TopMenuAB;
import repository.ab.topmenu.TopMenuSearchAB;
import repository.driverConfiguration.Config;
import repository.gw.activity.ActivityPopup;
import repository.gw.enums.AbActivitySearchFilter;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.entities.Contacts;
import persistence.globaldatarepo.helpers.AbUserHelper;
import persistence.globaldatarepo.helpers.ContactsHelpers;


public class CreateActivityCompleteActivity extends BaseTest {
	private WebDriver driver;
    private AbUsers numberingUser;
    private AbUsers claimsUser;
    private GeneratePolicy myPolicyObj = null;
    Contacts activityContact = null;
    
    @Test
    public void createActivityForClaims() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        this.activityContact = ContactsHelpers.getRandomContact("Company");
        this.numberingUser = AbUserHelper.getRandomDeptUser("Policy Services");
        this.claimsUser = AbUserHelper.getRandomDeptUser("Claims");
        AdvancedSearchAB searchContact = new AdvancedSearchAB(driver);
        searchContact.loginAndSearchContact(numberingUser, "", activityContact.getContactName(), activityContact.getContactAddressLine1(), State.valueOf(activityContact.getContactState()));
        NewActivity newActivity = new NewActivity(driver);
        newActivity.createReviewContactActivity(claimsUser, "Contact");
        new GuidewireHelpers(driver).logout();
        new Login(driver).login(claimsUser.getUserName(), claimsUser.getUserPassword());

        TopMenuSearchAB menu = new TopMenuSearchAB(driver);
        menu.clickSearch();
        SidebarAB sidebar = new SidebarAB(driver);
        ActivitySearchAB searchPage = sidebar.clickSidebarActivitySearchLink();
        searchPage.clickActivitySearchActivitySubjectLink(this.claimsUser,"Review Contact", activityContact.getContactName());

        ActivityPopup activity = new ActivityPopup(driver);
        activity.clickActivityComplete();
        Assert.assertFalse(activity.activityPageExists(), "Ensure that when the user completes an activity that the activity page is closed.");
        
    }

    public void createPolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        // customizing location and building
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        PolicyLocationBuilding building = new PolicyLocationBuilding();
        locOneBuildingList.add(building);
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("dues", "Forme")
                .withPolOrgType(OrganizationType.Individual)
                .withMembershipDuesOnPNI()
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicySubmitted);
        driver.quit();
    }

    @Test
    public void checkActivities() throws Exception {
        createPolicy();
        AbUsers user = AbUserHelper.getRandomDeptUser("Policy Services");
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        //check for dues activity and review contact activity.
        new Login(driver).login(user.getUserName(), user.getUserPassword());
        TopMenuAB getToSearch = new TopMenuAB(driver);
        getToSearch.clickSearchTab();

        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
        searchMe.searchByAccountNumberClickName(myPolicyObj.accountNumber, myPolicyObj.pniContact.getLastName());

        SidebarAB sideBar = new SidebarAB(driver);
        sideBar.clickSidebarContactWorkplanLink();

        ContactWorkPlanAB contactActivityPage = new ContactWorkPlanAB(driver);
        contactActivityPage.selectContactWorkPlanActivityFilter(AbActivitySearchFilter.AllOpen);
        Assert.assertTrue(contactActivityPage.clickActivityCheckbox("Dues"), "An activity should be created in ContactManager when a bound policy has dues.");
    }
}

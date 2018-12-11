package regression.r2.noclock.contactmanager.dues;


import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.CountyIdaho;

import repository.ab.contact.ContactDetailsAccountAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.contact.ContactDetailsPaidDuesAB;
import repository.ab.contact.ContactDetailsRelatedContactsAB;
import repository.ab.contact.ContactWorkPlanAB;
import repository.ab.pagelinks.PageLinks;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.sidebar.SidebarAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.AbActivitySearchFilter;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
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
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;

public class SpouseDues extends BaseTest {
	private WebDriver driver;
	private AbUsers user;
	private GeneratePolicy myPolicyObj = null;
	private GenerateContact myContactObj = null;
	
	@Test
	public void createPolicy() throws Exception {
		// customizing location and building
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		PolicyLocationBuilding building = new PolicyLocationBuilding();
		locOneBuildingList.add(building);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("dues", "Forme")				
				.withPolOrgType(OrganizationType.Individual)
				.withAlternateID("123456792")
                .withMembershipDuesOnPNI()
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicySubmitted);
	}
	
	@Test(dependsOnMethods = {"createPolicy"})
	public void checkActivitiesAndId() throws Exception{
		this.user = AbUserHelper.getRandomDeptUser("Policy Services");		
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		//check for dues activity and review contact activity.
		new Login(driver).login(user.getUserName(), user.getUserPassword());
        TopMenuAB getToSearch = new TopMenuAB(driver);
		getToSearch.clickSearchTab();

        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
		searchMe.searchByAccountNumber(myPolicyObj.accountNumber);

        ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
		String altID = basicsPage.getAlternateID();
		if(!altID.equals(myPolicyObj.pniContact.getAlternateID())){
			Assert.fail("Contact should have been created with an Alternate ID.");
		}

        SidebarAB sideBar = new SidebarAB(driver);
		sideBar.clickSidebarContactWorkplanLink();

        ContactWorkPlanAB contactActivityPage = new ContactWorkPlanAB(driver);
		contactActivityPage.selectContactWorkPlanActivityFilter(AbActivitySearchFilter.AllOpen);
		contactActivityPage.clickActivityCheckbox("Review");
		contactActivityPage.clickActivityCheckbox("Dues");

        sideBar = new SidebarAB(driver);
		sideBar.clickSidebarContactDetailsLink();
		
	}
	
	@Test(dependsOnMethods = {"createPolicy"})
	public void testSpouse() throws Exception{
		
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        this.myContactObj = new GenerateContact.Builder(driver)
				.withCreator(user)
				.withGenerateAccountNumber(true)
				.build(GenerateContactType.Person);

		System.out.println("accountNumber: " + myContactObj.accountNumber);

        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
		contactPage.clickContactDetailsBasicsRelatedContactsLink();

        ContactDetailsRelatedContactsAB relatedContactPage = new ContactDetailsRelatedContactsAB(driver);
		relatedContactPage.addSpouseByAcct(myPolicyObj.accountNumber);
		relatedContactPage.clickContactDetailsBasicsPaidDuesLink();

        ContactDetailsPaidDuesAB paidDues = new ContactDetailsPaidDuesAB(driver);
		ArrayList<String> duesDate = paidDues.getAllDuesPolicyEffectiveDates();
		System.out.println("Contact Manager Member Number is " + myContactObj.accountNumber);
		System.out.println("Policy Number is " + myPolicyObj.accountNumber);
		System.out.println("Current System date is " + DateUtils.dateFormatAsString("MM/dd/yyyy", DateUtils.getCenterDate(cf, ApplicationOrCenter.ContactManager)));
		System.out.println("The dues date is " + duesDate.get(0));
		Assert.assertEquals(duesDate.get(0), DateUtils.dateFormatAsString("MM/dd/yyyy", DateUtils.getCenterDate(cf, ApplicationOrCenter.ContactManager)),"The spouse should have a dues record that is created today.");
		
	}
	
	@Test(dependsOnMethods = {"testSpouse"})
	public void duplicateDuesNotAllowed() throws Exception{
			this.user = AbUserHelper.getRandomDeptUser("Policy Services");
			Config cf = new Config(ApplicationOrCenter.ContactManager);
	        driver = buildDriver(cf);
			
			new Login(driver).login(user.getUserName(), user.getUserPassword());

        TopMenuAB getToSearch = new TopMenuAB(driver);
			getToSearch.clickSearchTab();

        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
			searchMe.searchByAccountNumber(myPolicyObj.accountNumber);

        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
			contactPage.clickContactDetailsBasicsPaidDuesLink();

        ContactDetailsPaidDuesAB duesPage = new ContactDetailsPaidDuesAB(driver);
			duesPage.clickContactDetailsPaidDuesEditLink();
			if(duesPage.addDue(CountyIdaho.Bannock, "08-271811-01", "Paid", DateUtils.getCenterDate(cf, ApplicationOrCenter.ContactManager))){
				Assert.fail("An Effective Date should only be listed once.");
			}		
	}
		
		
	@Test(dependsOnMethods = {"acctRemove"})
	public void cancelPolicyStatus() throws Exception{
		this.user = AbUserHelper.getRandomDeptUser("Policy Services");
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		
		new Login(driver).login(user.getUserName(), user.getUserPassword());

        TopMenuAB getToSearch = new TopMenuAB(driver);
		getToSearch.clickSearchTab();

        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
		searchMe.searchByAccountNumber(myPolicyObj.accountNumber);

        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
		contactPage.clickContactDetailsBasicsEditLink();
		contactPage.clickContactDetailsBasicsAccountsLink();

        ContactDetailsAccountAB accountPage = new ContactDetailsAccountAB(driver);
		accountPage.changeStatus("Cancelled", DateUtils.dateFormatAsString("MM/dd/yyyy", DateUtils.getCenterDate(cf, ApplicationOrCenter.ContactManager)));
		accountPage.clickUpdate();		
	}
	
	@Test(dependsOnMethods = {"testSpouse"})
	public void importDuesOnSpouse() throws Exception{
		this.user = AbUserHelper.getRandomDeptUser("Policy Services");
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		
		new Login(driver).login(user.getUserName(), user.getUserPassword());

        TopMenuAB getToSearch = new TopMenuAB(driver);
		getToSearch.clickSearchTab();

        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
		searchMe.searchByAccountNumber(myPolicyObj.accountNumber);

        ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
		basicsPage.clickContactDetailsBasicsEditLink();
		basicsPage.clickContactDetailsBasicsPaidDuesLink();

        ContactDetailsPaidDuesAB duesPage = new ContactDetailsPaidDuesAB(driver);
		duesPage.clickContactDetailsPaidDuesImportDuesButton();
		
		new GuidewireHelpers(driver).logout();
		
		new Login(driver).login(user.getUserName(), user.getUserPassword());

        getToSearch = new TopMenuAB(driver);
		getToSearch.clickSearchTab();

        searchMe = new AdvancedSearchAB(driver);
		searchMe.searchByAccountNumber(myContactObj.accountNumber);

        basicsPage = new ContactDetailsBasicsAB(driver);
		basicsPage.clickContactDetailsBasicsEditLink();
		basicsPage.clickContactDetailsBasicsPaidDuesLink();

        duesPage = new ContactDetailsPaidDuesAB(driver);
		duesPage.clickContactDetailsPaidDuesImportDuesButton();
		
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = {"testSpouse"})
	public void acctRemove() throws Exception{
		this.user = AbUserHelper.getRandomDeptUser("Policy Services");
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		
		new Login(driver).login(user.getUserName(), user.getUserPassword());

        TopMenuAB getToSearch = new TopMenuAB(driver);
		getToSearch.clickSearchTab();

        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
		searchMe.searchByAccountNumber(myPolicyObj.accountNumber);

        ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
		basicsPage.clickContactDetailsBasicsEditLink();

        PageLinks tabLinks = new PageLinks(driver);
		tabLinks.clickContactDetailsBasicsAccountsLink();

        ContactDetailsAccountAB acctPage = new ContactDetailsAccountAB(driver);
		acctPage.setRemoveDate(myPolicyObj.accountNumber, DateUtils.getCenterDate(cf, ApplicationOrCenter.ContactManager));
		acctPage.clickUpdate();
		//checkDues
        basicsPage = new ContactDetailsBasicsAB(driver);
		basicsPage.clickContactDetailsBasicsPaidDuesLink();

        ContactDetailsPaidDuesAB dues = new ContactDetailsPaidDuesAB(driver);
		ArrayList<String> policyEffectiveDates = dues.getAllDuesPolicyEffectiveDates();
		for(String date : policyEffectiveDates){
			if(!date.equals(DateUtils.getCenterDateAsString(driver, ApplicationOrCenter.PolicyCenter, "MM/dd/yyyy"))){
				Assert.fail("When adding an Account remove date the dues should stay on the contact.");
			}
		}
		dues.clickContactDetailsPaidDuesEditLink();
		dues.clickContactDetailsPaidDuesImportDuesButton();
		new GuidewireHelpers(driver).logout();
	}
	
}


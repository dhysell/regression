package regression.r2.noclock.contactmanager.contact;


import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.CountyIdaho;

import repository.ab.contact.ContactDetailsAccountAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.contact.ContactDetailsPaidDuesAB;
import repository.ab.contact.ContactDetailsRelatedContactsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
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
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;
public class RelatedContact extends BaseTest {
	private WebDriver driver;
	GeneratePolicy myPolicyObj = null;
	GenerateContact myContactObj = null;
	AbUsers user = null;
	private String firstName = "Cor";
	private String middleName = "Zah";
	private String lastName = "Monkey";
	private AddressInfo addressInfo = null;
	
	
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
                .withMembershipDuesOnPNI()
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicySubmitted);
		
		this.user = AbUserHelper.getRandomDeptUser("Policy Services");
		driver.quit();
		
		cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		AbUsers abUser = AbUserHelper.getRandomDeptUser("Policy Services");
        Login logMeIn = new Login(driver);
		logMeIn.login(abUser.getUserName(), abUser.getUserPassword());

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
		
		System.out.println("Policy Contact is " + myPolicyObj.accountNumber);
		
	}
	
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

        contactPage = new ContactDetailsBasicsAB(driver);
		contactPage.clickContactDetailsBasicsAccountsLink();

        ContactDetailsAccountAB accountPage = new ContactDetailsAccountAB(driver);
        String status = accountPage.getPolicyStatus(myPolicyObj.busOwnLine.getPolicyNumber());
		Assert.assertEquals(status, "Cancelled", "The spouse should have the same account status as Related Contact.");
		
		System.out.println("Contact Account is " + myContactObj.accountNumber);
	}
	
	@Test
	public void testSyncButton() throws Exception{
		createPolicy();
		testSpouse();
        addDues(myContactObj.accountNumber, myPolicyObj.busOwnLine.getPolicyNumber(), DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.ContactManager), DateAddSubtractOptions.Year, -2));
        addDues(myPolicyObj.accountNumber, myPolicyObj.busOwnLine.getPolicyNumber(), DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.ContactManager), DateAddSubtractOptions.Year, -3));
		ArrayList<String> policyDuesEffectiveDates = getDues(myPolicyObj.accountNumber);
		ArrayList<String> spouseDuesEffectiveDates = getDues(myContactObj.accountNumber);
		Assert.assertTrue(spouseDuesEffectiveDates.equals(policyDuesEffectiveDates), "After clicking the sync button, the dues should be the same.  Investigate accounts: " + myPolicyObj.accountNumber + "and " + myContactObj.accountNumber +".");
	}
	
	public void addDues(String acctNum, String PolicyNumber, Date duesDate) throws Exception{
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        Login logMeIn = new Login(driver);
		logMeIn.login(user.getUserName(), user.getUserPassword());

        TopMenuAB getToSearch = new TopMenuAB(driver);
		getToSearch.clickSearchTab();

        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
		searchMe.searchByAccountNumber(acctNum);

        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
		contactPage.clickContactDetailsBasicsPaidDuesLink();

        ContactDetailsPaidDuesAB duesPage = new ContactDetailsPaidDuesAB(driver);
		duesPage.addDue(CountyIdaho.Bannock, PolicyNumber, "Paid", duesDate);	
	}
	
	public ArrayList<String> getDues(String acctNum) throws Exception{
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        Login logMeIn = new Login(driver);
		logMeIn.login(user.getUserName(), user.getUserPassword());

        TopMenuAB getToSearch = new TopMenuAB(driver);
		getToSearch.clickSearchTab();

        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
		searchMe.searchByAccountNumber(acctNum);

        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
		contactPage.clickContactDetailsBasicsPaidDuesLink();
        ContactDetailsPaidDuesAB duesPage = new ContactDetailsPaidDuesAB(driver);
		duesPage.clickContactDetailsPaidDuesEditLink();
		duesPage.clickSyncButton();
		return duesPage.getAllDuesEffectiveDates();
		
	}
	
	@Test
	public void newRelatedContactPopulatesBasicPage() throws Exception{

		AbUsers user = AbUserHelper.getRandomDeptUser("Policy Services");
		
		NewPersonTest spouse = new NewPersonTest();
		spouse.testNewPerson();		
		GenerateContact relatedContact = spouse.getPersonContact();
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		this.addressInfo = new AddressInfo(true);
        Login logMeIn = new Login(driver);
		logMeIn.login(user.getUserName(), user.getUserPassword());

        TopMenuAB getToSearch = new TopMenuAB(driver);
		getToSearch.clickSearchTab();

        AdvancedSearchAB search = new AdvancedSearchAB(driver);

        getToSearch = new TopMenuAB(driver);
		getToSearch.clickSearchTab();

        search = new AdvancedSearchAB(driver);
		search.setAdvancedSearchContactType(ContactSubType.Person);
		search.setLastName(this.lastName);
		search.setFirstName(this.firstName);
		if (middleName != null) {
			search.setMiddleName(middleName);
		}
		search.selectSpecificComboBox_State(this.addressInfo.getState());
		search.setCity(this.addressInfo.getCity());
		search.clickSearch();

        search.clickCreateNewPerson();
        ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
		basicsPage.clickContactDetailsBasicsRelatedContactsLink();

        ContactDetailsRelatedContactsAB relatedContactPage = new ContactDetailsRelatedContactsAB(driver);
		relatedContactPage.addSpouseByAcct(relatedContact.accountNumber);
		relatedContactPage.clickContactDetailsBasicsLink();

        basicsPage = new ContactDetailsBasicsAB(driver);
				
		String acctNum = basicsPage.getContactDetailsBasicsAccountNumber();
		String membershipType = basicsPage.getMembershipType();
		String contactAgent = basicsPage.getContactDetailsBasicsAgent();
		String brokerageAgent = basicsPage.getBrokerageAgent();
		
		Assert.assertEquals(acctNum, relatedContact.accountNumber);
		Assert.assertEquals(membershipType.trim(), relatedContact.membershipType.getValue());
		if(!(contactAgent.contains(relatedContact.agent.agentLastName) && contactAgent.contains(relatedContact.agent.agentLastName))){
			Assert.fail("The scrapped agent should be the same as the related contact.");
		}
		Assert.assertEquals(brokerageAgent, "");		
	}
	
}

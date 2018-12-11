package regression.r2.noclock.contactmanager.changes;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.ab.contact.ContactDetailsAddressesAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.contact.ContactDetailsHistoryAB;
import repository.ab.contact.ContactWorkPlanAB;
import repository.ab.pagelinks.PageLinks;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.sidebar.SidebarAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.AbActivitySearchFilter;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import services.helpers.com.idfbins.emailphoneupdate.EmailPhoneUpdateHelper;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Contacts;
import persistence.globaldatarepo.helpers.AbUserHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.ContactsHelpers;
import services.services.com.idfbins.emailphoneupdate.com.example.com.idfbins.ab.dto.CRMContactInfo;

public class CRMWebServices extends BaseTest {
	private WebDriver driver;
	private Contacts contact = null;
	private String newEmailAddress = "ourcustomer@fantasticpeople.com";
	private String newBusinessPhone = "866-468-4968";
	private String newHomePhone = "866-468-4968";
	private String newMobilePhone = "866-468-4968"; 
	private String newWorkPhone = "866-468-4968";
	
	@Test
	public void testUpdateContact() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);
		System.out.println(DateUtils.dateFormatAsString("EEE, MMM dd, yyyy", DateUtils.getCenterDate(cf, ApplicationOrCenter.ContactManager)));
		this.contact = ContactsHelpers.getContactWithPhone();
		AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
		if(this.contact.isContactIsCompany()) {
			//loginAndSearchContact(AbUsers abUser, String firstName, String lastName, String address, State state)
			searchMe.loginAndSearchContact(AbUserHelper.getRandomDeptUser("Admin"), "", this.contact.getContactName(), this.contact.getContactAddressLine1(), State.valueOfName(this.contact.getContactState()));
		} else {
			ArrayList<String> contactName = StringsUtils.lastFirstMiddleInitialNameParser(contact.getContactName());
			searchMe.loginAndSearchContact(AbUserHelper.getRandomDeptUser("Admin"), contactName.get(0), contactName.get(contactName.size()-1), this.contact.getContactAddressLine1(), State.valueOfName(this.contact.getContactState()));
		}
		
		ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);	
		String addressBookUID = basicsPage.getaddressBookUID();//.replace("ab", "");
		String displayName = basicsPage.getContactDetailsBasicsContactName();
		String oldEmail = basicsPage.getContactDetailsBasicsMainEmail();
		basicsPage.clickContactDetailsBasicsAddressLink();
		ContactDetailsAddressesAB addressPage = new ContactDetailsAddressesAB(driver);
		String locationNum = addressPage.getOfficeByAddress(this.contact.getContactAddressLine1());
		String locationType = addressPage.getTypeByAddress(contact.getContactAddressLine1());
		boolean is1099 = false;
		if(locationType.contains("1099")) {
			is1099 = true;
		} else {
			CRMContactInfo contactInfo = mapContactToCRMContactInfo(addressBookUID, displayName, this.newEmailAddress, this.newBusinessPhone, this.newHomePhone, this.newMobilePhone, this.newWorkPhone);
	
			EmailPhoneUpdateHelper updateContact = new EmailPhoneUpdateHelper(driver.getCurrentUrl().replace("/ContactManager.do",""));
			if(!updateContact.testUpdateContact(contactInfo)) {
				System.out.println("The Contacts name is "+contactInfo.getUserDisplayName());
				System.out.println("The Contacts address to update is "+contactInfo.getAddress());
				System.out.println("The Contacts City is "+contactInfo.getCity());
				System.out.println("The Contacts State is "+contactInfo.getStateCode());
				System.out.println("The Contacts Zip is "+contactInfo.getPostalCode());
				System.out.println("The Contacts address book UID is "+contactInfo.getAddressBookUID());
				System.out.println("The Contacts Email address is "+contactInfo.getEmailAddress());
				System.out.println("The Contacts business phone is "+contactInfo.getBusinessPhone());
				System.out.println("The Contacts home phone is "+contactInfo.getHomePhone());
				System.out.println("The Contacts mobile phone is "+contactInfo.getMobilePhone());
				System.out.println("The Contacts work phone is "+contactInfo.getWorkPhone());
				
				Assert.fail("The contact should be updated.");
			} else {
				if(is1099) {
					check1099(addressBookUID, displayName, oldEmail, locationNum, locationType);
				}else {
					checkHistoryItem(displayName, locationNum);
				}
			}
		}
	}
	
	@Test
	public void testUpdateContactPhone() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);
		System.out.println(DateUtils.dateFormatAsString("EEE, MMM dd, yyyy", DateUtils.getCenterDate(cf, ApplicationOrCenter.ContactManager)));
		this.contact = ContactsHelpers.getContactWithPhone();
		AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
		if(this.contact.isContactIsCompany()) {
			//loginAndSearchContact(AbUsers abUser, String firstName, String lastName, String address, State state)
			searchMe.loginAndSearchContact(AbUserHelper.getRandomDeptUser("Admin"), "", this.contact.getContactName(), this.contact.getContactAddressLine1(), State.valueOfName(this.contact.getContactState()));
		} else {
			ArrayList<String> contactName = StringsUtils.lastFirstMiddleInitialNameParser(contact.getContactName());
			searchMe.loginAndSearchContact(AbUserHelper.getRandomDeptUser("Admin"), contactName.get(0), contactName.get(contactName.size()-1), this.contact.getContactAddressLine1(), State.valueOfName(this.contact.getContactState()));
		}
		
		ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);	
		String addressBookUID = basicsPage.getaddressBookUID();//.replace("ab", "");
		String displayName = basicsPage.getContactDetailsBasicsContactName();
		String oldEmail = basicsPage.getContactDetailsBasicsMainEmail();
//		String oldAltEmail = basicsPage.getContactDetailsBasicsAltEmail();
		basicsPage.clickContactDetailsBasicsAddressLink();
		ContactDetailsAddressesAB addressPage = new ContactDetailsAddressesAB(driver);
		String locationNum = addressPage.getOfficeByAddress(this.contact.getContactAddressLine1());
		String locationType = addressPage.getTypeByAddress(contact.getContactAddressLine1());
		String oldBusinessPhone = addressPage.getContactDetailsAddressesBusinessPhone();
		String oldWorkPhone = addressPage.getPhoneOnAddress("Work", this.contact.getContactAddressLine1());
		String oldHomePhone = addressPage.getPhoneOnAddress("Home", this.contact.getContactAddressLine1());
		String oldMobilePhone = addressPage.getPhoneOnAddress("Mobile", this.contact.getContactAddressLine1());
//		String oldFaxPhone = addressPage.getPhoneOnAddress("Fax", this.contact.getContactAddressLine1());
		
		boolean is1099 = false;
		if(locationType.contains("1099")) {
			is1099 = true;
		} else {
			CRMContactInfo contactInfo = mapContactToCRMContactInfo(addressBookUID, displayName, oldEmail, this.newBusinessPhone, oldHomePhone, oldMobilePhone, oldWorkPhone);
	
			EmailPhoneUpdateHelper updateContact = new EmailPhoneUpdateHelper(driver.getCurrentUrl().replace("/ContactManager.do",""));
			if(!updateContact.testUpdateContact(contactInfo)) {
				System.out.println("The Contacts name is "+contactInfo.getUserDisplayName());
				System.out.println("The Contacts address to update is "+contactInfo.getAddress());
				System.out.println("The Contacts City is "+contactInfo.getCity());
				System.out.println("The Contacts State is "+contactInfo.getStateCode());
				System.out.println("The Contacts Zip is "+contactInfo.getPostalCode());
				System.out.println("The Contacts address book UID is "+contactInfo.getAddressBookUID());
				System.out.println("The Contacts Email address is "+contactInfo.getEmailAddress());
				System.out.println("The Contacts business phone is "+contactInfo.getBusinessPhone());
				System.out.println("The Contacts home phone is "+contactInfo.getHomePhone());
				System.out.println("The Contacts mobile phone is "+contactInfo.getMobilePhone());
				System.out.println("The Contacts work phone is "+contactInfo.getWorkPhone());
				
				Assert.fail("The contact should be updated.");
			} else {
				if(is1099) {
					check1099(addressBookUID, displayName, oldEmail, locationNum, locationType);
				}else {
					verifyExtraHistoryItemsDoNotExist(displayName, locationNum);
				}
			}
		}
	}
	
	public void verifyExtraHistoryItemsDoNotExist(String displayName, String locationNum) {
		
		PageLinks clickTab = new PageLinks(driver);
		clickTab.clickContactDetailsBasicsHistoryLink();
		ContactDetailsHistoryAB historyPage = new ContactDetailsHistoryAB(driver);
		
		//Email
		if(historyPage.verifyHistoryItemExists("Email Address Changed", "CRMNewAuthUser", DateUtils.dateFormatAsString("EEE, MMM d, yyyy", DateUtils.getCenterDate(driver, ApplicationOrCenter.ContactManager)), "Main Email Address Changed by "+displayName, true)){
			Assert.fail("A history item for the change of Email Address is not on the history page. Please investigate.");
		}
		//Alt Email
		if(historyPage.verifyHistoryItemExists("Email Address Changed", "CRMNewAuthUser", DateUtils.dateFormatAsString("EEE, MMM d, yyyy", DateUtils.getCenterDate(driver, ApplicationOrCenter.ContactManager)), "Alternate Email Address Changed by "+displayName, true)){
			Assert.fail("A history item for the change of Email Address is not on the history page. Please investigate.");
		}
		
		//Business
		if(!historyPage.verifyHistoryItemExists("Phone Number Updated", "CRMNewAuthUser CRM", DateUtils.dateFormatAsString("EEE, MMM d, yyyy", DateUtils.getCenterDate(driver, ApplicationOrCenter.ContactManager)), "Business Phone Updated by " +displayName+" on office "+ locationNum, true)){
			Assert.fail("A history item for the change of Email Address is not on the history page. Please investigate.");
		}
		//Home
		if(historyPage.verifyHistoryItemExists("Phone Number Updated", "CRMNewAuthUser CRM", DateUtils.dateFormatAsString("EEE, MMM d, yyyy", DateUtils.getCenterDate(driver, ApplicationOrCenter.ContactManager)), "Home Phone Number Updated by " +displayName+" on office "+ locationNum, true)){
			Assert.fail("A history item for the change of Email Address is not on the history page. Please investigate.");
		}
		//Mobile
		if(historyPage.verifyHistoryItemExists("Phone Number Updated", "CRMNewAuthUser CRM", DateUtils.dateFormatAsString("EEE, MMM d, yyyy", DateUtils.getCenterDate(driver, ApplicationOrCenter.ContactManager)), "Cell Number Updated by " +displayName+" on office "+ locationNum, true)){
			Assert.fail("A history item for the change of Email Address is not on the history page. Please investigate.");
		}
		//Work
		if(historyPage.verifyHistoryItemExists("Phone Number Updated", "CRMNewAuthUser CRM", DateUtils.dateFormatAsString("EEE, MMM d, yyyy", DateUtils.getCenterDate(driver, ApplicationOrCenter.ContactManager)), "Work Phone Updated by " +displayName+" on office "+ locationNum, true)){
			Assert.fail("A history item for the change of Email Address is not on the history page. Please investigate.");
		}
	}
	
	public void check1099(String abUID, String displayName, String oldEmail, String locationNum, String locationType) {
		PageLinks clickTab = new PageLinks(driver);
		clickTab.clickContactDetailsBasicsHistoryLink();
		ContactDetailsHistoryAB historyPage = new ContactDetailsHistoryAB(driver);
		//Email
		if(!historyPage.verifyHistoryItemExists("Email Address Changed", "CRMNewAuthUser CRM", DateUtils.dateFormatAsString("EEE, MMM dd, yyyy", DateUtils.getCenterDate(driver, ApplicationOrCenter.ContactManager)), "Main Email Address Changed by "+displayName, true)){
			Assert.fail("A history item for the change of Email Address is not on the history page. Please investigate.");
		}
		if(historyPage.verifyHistoryItemExists("Phone Number Updated", "CRMNewAuthUser", DateUtils.getCenterDateAsString(driver, ApplicationOrCenter.ContactManager, "EEE, MMM dd, yyyy"), displayName, true)) {
			Assert.fail("The history item should not exist as Agent email addresses and Agent work phone numbers should not be updated by the CRM Webservice.");
		}
		
		SidebarAB sideMenu = new SidebarAB(driver);
		sideMenu.clickSidebarContactWorkplanLink();
		ContactWorkPlanAB workPlan = new ContactWorkPlanAB(driver);
		workPlan.selectContactWorkPlanActivityFilter(AbActivitySearchFilter.AllOpen);
		workPlan.clickActivityCheckbox(displayName +" sent from CRM the following phone number(s) to update the address");	
	}
	
	
	public void checkHistoryItem(String displayName, String locationNum) {
		PageLinks clickTab = new PageLinks(driver);
		clickTab.clickContactDetailsBasicsHistoryLink();
		ContactDetailsHistoryAB historyPage = new ContactDetailsHistoryAB(driver);
		//Email
		if(!historyPage.verifyHistoryItemExists("Email Address Changed", "CRMNewAuthUser", DateUtils.dateFormatAsString("EEE, MMM d, yyyy", DateUtils.getCenterDate(driver, ApplicationOrCenter.ContactManager)), "Main Email Address Changed by "+displayName, true)){
			Assert.fail("A history item for the change of Email Address is not on the history page. Please investigate.");
		}
		//Business
		if(!historyPage.verifyHistoryItemExists("Phone Number Updated", "CRMNewAuthUser CRM", DateUtils.dateFormatAsString("EEE, MMM d, yyyy", DateUtils.getCenterDate(driver, ApplicationOrCenter.ContactManager)), "Business Phone Updated by " +displayName+" on office "+ locationNum, true)){
			Assert.fail("A history item for the change of Email Address is not on the history page. Please investigate.");
		}
		//Home
		if(!historyPage.verifyHistoryItemExists("Phone Number Updated", "CRMNewAuthUser CRM", DateUtils.dateFormatAsString("EEE, MMM d, yyyy", DateUtils.getCenterDate(driver, ApplicationOrCenter.ContactManager)), "Home Phone Number Updated by " +displayName+" on office "+ locationNum, true)){
			Assert.fail("A history item for the change of Email Address is not on the history page. Please investigate.");
		}
		//Mobile
		if(!historyPage.verifyHistoryItemExists("Phone Number Updated", "CRMNewAuthUser CRM", DateUtils.dateFormatAsString("EEE, MMM d, yyyy", DateUtils.getCenterDate(driver, ApplicationOrCenter.ContactManager)), "Cell Number Updated by " +displayName+" on office "+ locationNum, true)){
			Assert.fail("A history item for the change of Email Address is not on the history page. Please investigate.");
		}
		//Work
		if(!historyPage.verifyHistoryItemExists("Phone Number Updated", "CRMNewAuthUser CRM", DateUtils.dateFormatAsString("EEE, MMM d, yyyy", DateUtils.getCenterDate(driver, ApplicationOrCenter.ContactManager)), "Work Phone Updated by " +displayName+" on office "+ locationNum, true)){
			Assert.fail("A history item for the change of Email Address is not on the history page. Please investigate.");
		}
	}
/*	
	public void verifyCheckHistoryItem(String displayName, String locationNum) {
		PageLinks clickTab = new PageLinks();
		clickTab.clickContactDetailsBasicsHistoryLink();
		ContactDetailsHistoryAB historyPage = new ContactDetailsHistoryAB();
		//Email
		if(!historyPage.verifyHistoryItemExists("Email Address Changed", "CRMNewAuthUser", DateUtils.dateFormatAsString("EEE, MMM d, yyyy", DateUtils.getCenterDate(ApplicationOrCenter.ContactManager)), "Main Email Address Changed by "+displayName)){
			Assert.fail("A history item for the change of Email Address is not on the history page. Please investigate.");
		}
		//Business
		if(!historyPage.verifyHistoryItemExists("Phone Number Updated", "CRMNewAuthUser CRM", DateUtils.dateFormatAsString("EEE, MMM d, yyyy", DateUtils.getCenterDate(ApplicationOrCenter.ContactManager)), "Business Phone Updated by " +displayName+" on office "+ locationNum)){
			Assert.fail("A history item for the change of Email Address is not on the history page. Please investigate.");
		}
		//Home
		if(!historyPage.verifyHistoryItemExists("Phone Number Updated", "CRMNewAuthUser CRM", DateUtils.dateFormatAsString("EEE, MMM d, yyyy", DateUtils.getCenterDate(ApplicationOrCenter.ContactManager)), "Home Phone Number Updated by " +displayName+" on office "+ locationNum)){
			Assert.fail("A history item for the change of Email Address is not on the history page. Please investigate.");
		}
		//Mobile
		if(!historyPage.verifyHistoryItemExists("Phone Number Updated", "CRMNewAuthUser CRM", DateUtils.dateFormatAsString("EEE, MMM d, yyyy", DateUtils.getCenterDate(ApplicationOrCenter.ContactManager)), "Cell Number Updated by " +displayName+" on office "+ locationNum)){
			Assert.fail("A history item for the change of Email Address is not on the history page. Please investigate.");
		}
		//Work
		if(!historyPage.verifyHistoryItemExists("Phone Number Updated", "CRMNewAuthUser CRM", DateUtils.dateFormatAsString("EEE, MMM d, yyyy", DateUtils.getCenterDate(ApplicationOrCenter.ContactManager)), "Work Phone Updated by " +displayName+" on office "+ locationNum)){
			Assert.fail("A history item for the change of Email Address is not on the history page. Please investigate.");
		}
	}
*/				
	@Test 
	public void crmAgentWorkAddress() throws Exception {
		Agents agent = AgentsHelper.getRandomAgent();
		Config cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);
		AbUsers admin = AbUserHelper.getRandomDeptUser("Admin");
		ContactDetailsBasicsAB agentContactPage = new ContactDetailsBasicsAB(driver);
		agentContactPage.loginAndSearchAgent(admin, agent);
		String abUID = agentContactPage.getaddressBookUID();
		agentContactPage.clickContactDetailsBasicsAddressLink();
		ContactDetailsAddressesAB addressPage = new ContactDetailsAddressesAB(driver);
		AddressInfo address = addressPage.getAddressByType("Work");
		CRMContactInfo contactInfo = mapContactInfoToCRMContactInfo(abUID, address.getLine1(), address.getCity(), address.getState(), address.getZip(), agent.agentUserName, this.newEmailAddress, this.newBusinessPhone, this.newHomePhone, this.newMobilePhone, this.newWorkPhone);

		EmailPhoneUpdateHelper updateContact = new EmailPhoneUpdateHelper(driver.getCurrentUrl().replace("/ContactManager.do",""));
		if(!updateContact.testUpdateContact(contactInfo)) {
			System.out.println("The Contacts name is "+contactInfo.getUserDisplayName());
			System.out.println("The Contacts address to update is "+contactInfo.getAddress());
			System.out.println("The Contacts City is "+contactInfo.getCity());
			System.out.println("The Contacts State is "+contactInfo.getStateCode());
			System.out.println("The Contacts Zip is "+contactInfo.getPostalCode());
			System.out.println("The Contacts address book UID is "+contactInfo.getAddressBookUID());
			System.out.println("The Contacts Email address is "+contactInfo.getEmailAddress());
			System.out.println("The Contacts business phone is "+contactInfo.getBusinessPhone());
			System.out.println("The Contacts home phone is "+contactInfo.getHomePhone());
			System.out.println("The Contacts mobile phone is "+contactInfo.getMobilePhone());
			System.out.println("The Contacts work phone is "+contactInfo.getWorkPhone());
			
			Assert.fail("The contact should be updated.");
		} else {
			PageLinks clickTab = new PageLinks(driver);
			clickTab.clickContactDetailsBasicsHistoryLink();
			ContactDetailsHistoryAB historyPage = new ContactDetailsHistoryAB(driver);
			if(historyPage.verifyHistoryItemExists("Email Address Changed", "CRMNewAuthUser", DateUtils.getCenterDateAsString(driver, ApplicationOrCenter.ContactManager, "EEE, MMM dd, yyyy"), agent.agentUserName, true)) {
				Assert.fail("The history item should not exist as Agent email addresses and Agent work phone numbers should not be updated by the CRM Webservice.");
			}
			if(historyPage.verifyHistoryItemExists("Phone Number Updated", "CRMNewAuthUser", DateUtils.getCenterDateAsString(driver, ApplicationOrCenter.ContactManager, "EEE, MMM dd, yyyy"), agent.agentUserName, true)) {
				Assert.fail("The history item should not exist as Agent email addresses and Agent work phone numbers should not be updated by the CRM Webservice.");
			}
		}
		
		SidebarAB sideMenu = new SidebarAB(driver);
		sideMenu.clickSidebarContactWorkplanLink();
		ContactWorkPlanAB workPlan = new ContactWorkPlanAB(driver);
		workPlan.selectContactWorkPlanActivityFilter(AbActivitySearchFilter.AllOpen);
		workPlan.clickActivityCheckbox(agent.agentUserName+" sent from CRM the following email to update "+this.newEmailAddress);
		workPlan.clickActivityCheckbox(agent.agentUserName+" sent from CRM the following phone number(s) to update the address");		
	}
	
	private CRMContactInfo mapContactToCRMContactInfo(String addressbookUID, String displayName, String emailAddress, String businessPhone, String homePhone, String mobilePhone, String workPhone) {
		State state = State.valueOfName(this.contact.getContactState());
		
		CRMContactInfo contactInfo = new CRMContactInfo();
		contactInfo.setAddress(this.contact.getContactAddressLine1());
		contactInfo.setAddressBookUID(addressbookUID);
		contactInfo.setBusinessPhone(businessPhone);
		contactInfo.setCRMGUID("");
		contactInfo.setCity(this.contact.getContactCity());
		contactInfo.setEmailAddress(emailAddress);
		contactInfo.setHomePhone(homePhone);
		contactInfo.setMobilePhone(mobilePhone);
		contactInfo.setPostalCode(this.contact.getContactZip());
		contactInfo.setStateCode(state.getAbbreviation());
		contactInfo.setUserDisplayName(displayName);
		contactInfo.setWorkPhone(workPhone);
		
		return contactInfo;
		
	}
	
	private CRMContactInfo mapContactInfoToCRMContactInfo(String addressbookUID, String address, String city, State state, String zip, String displayName, String emailAddress, String businessPhone, String homePhone, String mobilePhone, String workPhone) {
		CRMContactInfo contactInfo = new CRMContactInfo();
		contactInfo.setAddressBookUID(addressbookUID);
		contactInfo.setAddress(address);
		contactInfo.setCity(city);
		contactInfo.setStateCode(state.getAbbreviation());
		contactInfo.setPostalCode(zip);
		contactInfo.setEmailAddress(emailAddress);
		contactInfo.setBusinessPhone(businessPhone);		
		contactInfo.setHomePhone(homePhone);		
		contactInfo.setMobilePhone(mobilePhone);
		contactInfo.setWorkPhone(workPhone);
		contactInfo.setUserDisplayName(displayName);
		contactInfo.setCRMGUID("");
		return contactInfo;
		
	}
}

package regression.r2.noclock.contactmanager.changes;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import repository.ab.administration.AdminMenu;
import repository.ab.contact.ContactDetailsAddressesAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.contact.ContactDetailsRelatedContactsAB;
import repository.ab.pagelinks.PageLinks;
import repository.ab.search.AdvancedSearchAB;
import repository.driverConfiguration.Config;
import repository.gw.administration.AdminEventMessages;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DeliveryOptionType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.MessageQueue;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.RelatedContacts;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.helpers.AbUserHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;
@QuarantineClass
public class ABChangesUpdatePC extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy stdFireLiab_Fire_PolicyObj = null;
	private List<RelatedContacts> relatedContacts = new ArrayList<RelatedContacts>();
	private String newFirstName = "Signof";
	private String newLastName = "Testin";
	private int blockedMessageCount = 0;
	
	@Test
	public void verifyChanges() throws Exception {
		createPolicy();
		suspendEventMessages();
		try {
            System.out.println("Starting Account Number change");
			changeMe("accountnumber");
            System.out.println("Starting Gender change. Hope it is not expensive.");
			changeMe("gender");
            System.out.println("Starting Marital Status change");
			changeMe("maritalstatus");
            System.out.println("Starting Agent change");
			changeMe("agent");
            System.out.println("Starting Website change");
			changeMe("website");
            System.out.println("Starting Drivers License change");
			changeMe("dl");
            System.out.println("Starting Address change");
			changeMe("address");
            System.out.println("Starting Address Line 2 change");
			changeMe("line2");
            System.out.println("Starting Phone Number change");
			changeMe("phone");
            System.out.println("Starting Delivery Option change");
			changeMe("deliveryoption");
            System.out.println("Starting Related Contact change");
			changeMe("relatedcontacts");
            System.out.println("Starting SSN change");
			changeMe("ssn");
            System.out.println("Starting dob change");
            changeMe("dob");
            System.out.println("Starting name change");
			changeMe("name");
		} catch(Exception e) {
			unsuspendEventMessages();
			throw e;
		}	
		unsuspendEventMessages();
	}
	
	public void createPolicy() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		PLPolicyLocationProperty loc1Bldg1 = new PLPolicyLocationProperty();
		loc1Bldg1.setpropertyType(PropertyTypePL.DwellingPremises);
		locOnePropertyList.add(loc1Bldg1);
		locationsList.add(new PolicyLocation(locOnePropertyList, new AddressInfo()));
		
		ArrayList<PolicyInfoAdditionalNamedInsured> ani = new ArrayList<PolicyInfoAdditionalNamedInsured>();
		ani.add(new PolicyInfoAdditionalNamedInsured(ContactSubType.Person, "Marvina", "Martian", AdditionalNamedInsuredType.Spouse, new AddressInfo(true)));

        this.stdFireLiab_Fire_PolicyObj = new GeneratePolicy.Builder(driver)
				.withProductType(ProductLineType.StandardFire)
				.withLineSelection(LineSelection.StandardFirePL)
				.withInsFirstLastName("Marvin", "Martian")									
				.withPolicyLocations(locationsList)	
				.withANIList(ani)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);			
	}
	
	public void changeMe(String change) throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
		searchMe.loginAndSearchContact(AbUserHelper.getRandomDeptUser("Policy Services"), this.stdFireLiab_Fire_PolicyObj.pniContact.getFirstName(), this.stdFireLiab_Fire_PolicyObj.pniContact.getLastName(), this.stdFireLiab_Fire_PolicyObj.pniContact.getAddress().getLine1(), this.stdFireLiab_Fire_PolicyObj.pniContact.getAddress().getState());
        ContactDetailsBasicsAB basics = new ContactDetailsBasicsAB(driver);
		basics.clickEdit();
		switch(change) {
		case "name" : basics.setContactDetailsBasicsFirstName(newFirstName);
            this.stdFireLiab_Fire_PolicyObj.pniContact.setFirstName(newFirstName);
					  basics.setContactDetailsBasicsLastName(newLastName);
            this.stdFireLiab_Fire_PolicyObj.pniContact.setLastName(newLastName);
					  break;
		case "accountnumber":   basics.clickContactDetailsBasicsAccountNumberGenerateLink();
								break;
		case "ssn": basics.setContactDetailsBasicsSSN("978465123");
					break;
		case "dob": basics.setDateOfBirth(DateUtils.dateFormatAsString("MM/dd/yyyy", DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.ContactManager), DateAddSubtractOptions.Year, -60)));
					break;
		case "gender":  basics.setGender(Gender.Female.getValue());
						break;
		case "maritalstatus": basics.setMaritalStatus("Single"); 
						break;
            case "agent":
                basics.setContactDetailsBasicsAgent(AgentsHelper.getRandomAgent().getAgentNum(), State.Idaho);
						break;
		case "website": basics.setWebsite("www.warnerbrothers.com");
						break;
		case "dl" : basics.setDL("GW123456P");
					break;		
		case "address": basics.clickContactDetailsBasicsAddressLink();
            ContactDetailsAddressesAB address = new ContactDetailsAddressesAB(driver);
            address.setContactDetailsAddressesAddressLine1("261 Collins St");
            this.stdFireLiab_Fire_PolicyObj.pniContact.getAddress().setLine1("261 Collins St");
            PageLinks tabLinks = new PageLinks(driver);
            tabLinks.clickContactDetailsBasicsLink();
            basics = new ContactDetailsBasicsAB(driver);
						break;
		case "line2": basics.clickContactDetailsBasicsAddressLink();
            ContactDetailsAddressesAB addressTab = new ContactDetailsAddressesAB(driver);
            addressTab.setContactDetailsAddressesAddressLine2("Apt B");
            PageLinks tabLink = new PageLinks(driver);
            tabLink.clickContactDetailsBasicsLink();
            basics = new ContactDetailsBasicsAB(driver);
					  break;
		case "phone": 	basics.clickContactDetailsBasicsAddressLink();
            ContactDetailsAddressesAB addressPhoneTab = new ContactDetailsAddressesAB(driver);
            addressPhoneTab.setContactDetailsAddressesMobilePhone("2082394369");
            PageLinks tab = new PageLinks(driver);
            tab.clickContactDetailsBasicsLink();
            basics = new ContactDetailsBasicsAB(driver);
						break;
		case "deliveryoption":  basics.clickContactDetailsBasicsAddressLink();
            ContactDetailsAddressesAB addressDetail = new ContactDetailsAddressesAB(driver);
            addressDetail.addDeliveryOption(DeliveryOptionType.Attention, "Attn: Steve");
            PageLinks tabs = new PageLinks(driver);
            tabs.clickContactDetailsBasicsLink();
            basics = new ContactDetailsBasicsAB(driver);
								break;
		case "relatedcontacts": basics.clickContactDetailsBasicsRelatedContactsLink();
            ContactDetailsRelatedContactsAB relatedContacts = new ContactDetailsRelatedContactsAB(driver);
            RelatedContacts relatedContact = new RelatedContacts("baby", stdFireLiab_Fire_PolicyObj.pniContact.getLastName(), repository.gw.enums.RelatedContacts.ChildWard, this.stdFireLiab_Fire_PolicyObj.pniContact.getAddress(), false);
            this.relatedContacts.add(relatedContact);
								relatedContacts.addAllDependents(this.relatedContacts);
								break;
		default: break;
			
		}

        basics.clickContactDetailsBasicsUpdateLink();
		checkEventMessageCount(change);
	}

	public void checkEventMessageCount(String change) throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        AdminMenu admin = new AdminMenu(driver);
		admin.getToEventMessages(AbUserHelper.getRandomDeptUser("Admin"));
        AdminEventMessages messagesPage = new AdminEventMessages(driver);
		int messageCount = messagesPage.unsentCount(this.stdFireLiab_Fire_PolicyObj.pniContact.getFirstName() +" "+ this.stdFireLiab_Fire_PolicyObj.pniContact.getLastName());
		if(blockedMessageCount + 1 != messageCount) {
			Assert.fail("After changing the "+change+", the blocked messages did not increase.");
		}
		this.blockedMessageCount = messageCount;		
	}
	
	public void unsuspendEventMessages() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        AdminMenu admin = new AdminMenu(driver);
		admin.getToEventMessages(AbUserHelper.getRandomDeptUser("Admin"));
        AdminEventMessages messagesPage = new AdminEventMessages(driver);
		messagesPage.resumeQueue(MessageQueue.PolicyCenterContactBroadcast);
	}
	
	public void suspendEventMessages() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        AdminMenu admin = new AdminMenu(driver);
		admin.getToEventMessages(AbUserHelper.getRandomDeptUser("Admin"));
        AdminEventMessages messagesPage = new AdminEventMessages(driver);
		messagesPage.suspendQueue(MessageQueue.PolicyCenterContactBroadcast);
		new GuidewireHelpers(driver).logout();
	}
}

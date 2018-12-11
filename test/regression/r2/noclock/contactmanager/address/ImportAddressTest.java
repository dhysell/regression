// Steve Broderick
package regression.r2.noclock.contactmanager.address;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.ab.contact.ContactDetailsAddressesAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.ContactMembershipType;
import repository.gw.enums.GenerateContactType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.helpers.StringsUtils;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.entities.Contacts;
import persistence.globaldatarepo.helpers.AbUserHelper;
import persistence.globaldatarepo.helpers.ContactsHelpers;
public class ImportAddressTest extends BaseTest {
	private WebDriver driver;
	private AbUsers abUser;
	private String firstName = "Stor";
	private String middleName = "Broderman";
	private String lastName = "Andan";
	private Contacts contact = null;
	

	@Test
	public void testImportAddress() throws Exception {
		System.out.println("Initiating test testImportAddress");
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		this.abUser = AbUserHelper.getRandomDeptUser("Policy Services");
		this.contact = ContactsHelpers.getRandomContact("Person");
		ArrayList<String> contactNameArray = StringsUtils.lastFirstMiddleInitialNameParser(contact.getContactName());
		new GenerateContact.Builder(driver)
				
				.withCreator(abUser)
				.withFirstLastName(this.firstName, this.middleName, this.lastName)
				.withImportContact(contactNameArray.get(0), contactNameArray.size()>2 ? contactNameArray.get(1) : "", contactNameArray.get(contactNameArray.size()-1), contact.getContactAddressLine1())
				.build(GenerateContactType.Person);

		ContactDetailsBasicsAB detailsPage = new ContactDetailsBasicsAB(driver);
		if(!detailsPage.getContactDetailsBasicsContactAddress().contains(contact.getContactAddressLine1())) {
			Assert.fail("The imported address must be found on the page.");
		}
	}
	
	@Test
	public void importAddressCompanyPerson() throws Exception{
		System.out.println("Initiating test importAddressCompanyPerson");
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		this.abUser = AbUserHelper.getRandomDeptUser("Policy Services");
		contact = ContactsHelpers.getRandomContact("Company");
		new GenerateContact.Builder(driver)
				
				.withCreator(abUser)
				.withFirstLastName(this.firstName, this.middleName, this.lastName)
				.withImportContact(contact.getContactName(), contact.getContactAddressLine1())
				.build(GenerateContactType.Person);

		ContactDetailsBasicsAB detailsPage = new ContactDetailsBasicsAB(driver);
		if(!detailsPage.getContactDetailsBasicsContactAddress().contains(contact.getContactAddressLine1())) {
			Assert.fail("The imported address must be found on the page.");
		}
	}
	
	@Test
	public void importDeleteAddresses() throws Exception {
		System.out.println("Initiating test importDeleteAddresses");
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		this.abUser = AbUserHelper.getRandomDeptUser("Policy Services");
		this.contact = ContactsHelpers.getRandomContact("Person");
		ArrayList<String> contactNames = StringsUtils.lastFirstMiddleInitialNameParser(contact.getContactName());
		String importFirstName = contactNames.get(0);
		String importLastName = contactNames.get(contactNames.size()-1);
				
		ArrayList<AddressInfo> addresses = new ArrayList<>();
		for(int i = 0; i<5; i++) {
			addresses.add(new AddressInfo(true));
		}

        GenerateContact myContactObj = new GenerateContact.Builder(driver)
				.withCreator(abUser)
				.withFirstLastName(this.firstName, this.middleName, this.lastName)
				.withAddresses(addresses)
				.build(GenerateContactType.Person);

		System.out.println("accountNumber: " + myContactObj.accountNumber);
		
		//With this new contact we cannot use Generate because the test requires the user to remove addresses before clicking update.

		new AdvancedSearchAB(driver).loginAndCreateNewPerson(this.abUser, importFirstName + " "+importLastName);
		
        ContactDetailsBasicsAB contactBasicsPage = new ContactDetailsBasicsAB(driver);
		contactBasicsPage.setContactDetailsBasicsFirstName(importFirstName);
		contactBasicsPage.setContactDetailsBasicsLastName(importLastName);
		contactBasicsPage.setMembershipType(ContactMembershipType.Other);	
		contactBasicsPage.importAddresses(myContactObj.lastName, myContactObj.firstName, myContactObj.addresses.get(0).getLine1());
			
		ArrayList<AddressInfo> addressesToDelete = new ArrayList<>();
		for(int lcv = addresses.size(); lcv > 3; lcv--) {
			addressesToDelete.add(addresses.get(lcv-1));
		}
        ContactDetailsBasicsAB basics = new ContactDetailsBasicsAB(driver);
		basics.clickContactDetailsBasicsAddressLink();
        ContactDetailsAddressesAB addressPage = new ContactDetailsAddressesAB(driver);
		
		addressPage.deleteAddresses(addressesToDelete);
		addressPage.clickContactDetailsAddressesUpdate();
		basics = new ContactDetailsBasicsAB(driver);
		basics.clickContactDetailsBasicsAddressLink();
		addressPage = new ContactDetailsAddressesAB(driver);
		ArrayList<String> contactAddresses = addressPage.getAddresses();
		if(contactAddresses.size()!=3) {
			Assert.fail("The contact should contain three addresses. Ensure that you are able to remove addresses after import.");
		}
	}
}
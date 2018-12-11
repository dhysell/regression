// Steve Broderick
package regression.r2.noclock.contactmanager.dues;


import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.contact.ContactDetailsHistoryAB;
import repository.ab.contact.ContactDetailsPaidDuesAB;
import repository.ab.search.AdvancedSearchAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.ContactMembershipType;
import repository.gw.enums.ContactRole;
import repository.gw.enums.GenerateContactType;
import repository.gw.generate.GenerateContact;

import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;
public class ImportDuesTest extends BaseTest {
	private WebDriver driver;
	private GenerateContact myContactObj = null;
	private AbUsers user;
	private String firstName = "King";
	private String middleName = "ofThe";
	private String lastName = "Hill";
	private String importFirstName = "Heidi";
	private String importMiddleName = "Jo";
	private String importLastName = "Hill";
	private String importAddress = "246 Warren Ave";

	@Test
	public void testImportDues() throws Exception {
		
		this.user = AbUserHelper.getRandomDeptUser("Policy Services");

		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);

		ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
		rolesToAdd.add(ContactRole.Vendor);
		rolesToAdd.add(ContactRole.Lienholder);

        this.myContactObj = new GenerateContact.Builder(driver)
				.withCreator(user)
				.withFirstLastName(this.firstName, this.middleName, this.lastName)
				.withMembershipType(ContactMembershipType.Associate)
				.build(GenerateContactType.Person);

		System.out.println(this.myContactObj.accountNumber);

        ContactDetailsBasicsAB importDues = new ContactDetailsBasicsAB(driver);
		importDues.clickContactDetailsBasicsEditLink();
		importDues.clickContactDetailsBasicsPaidDuesLink();

        ContactDetailsPaidDuesAB dues = new ContactDetailsPaidDuesAB(driver);
		dues.clickContactDetailsPaidDuesImportDuesButton();

        AdvancedSearchAB findContact = new AdvancedSearchAB(driver);
		findContact.searchByFirstLastNameSelect(this.importFirstName, this.importMiddleName, this.importLastName, this.importAddress);
        dues = new ContactDetailsPaidDuesAB(driver);

		ArrayList<String> expireDates = dues.getExpireDates();
		if (expireDates.size() == 0) {
			throw new Exception("Dues were not imported or there were no dues to import");
		}
        dues = new ContactDetailsPaidDuesAB(driver);
		dues.clickContactDetailsPaidDuesUpdateAfterImport();
//		ArrayList<String> duesDates = dues.getAllDuesPolicyEffectiveDates();

        importDues = new ContactDetailsBasicsAB(driver);
		importDues.clickContactDetailsBasicsHistoryLink();

        ContactDetailsHistoryAB history = new ContactDetailsHistoryAB(driver);
		ArrayList<String> historyItems = history.getHistoryByType("Paid Due Added");
		
		Assert.assertSame(historyItems.size(), expireDates.size());
	}
}

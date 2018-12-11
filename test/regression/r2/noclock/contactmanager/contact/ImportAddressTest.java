// Steve Broderick
package regression.r2.noclock.contactmanager.contact;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.enums.GenerateContactType;
import repository.gw.generate.GenerateContact;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;
public class ImportAddressTest extends BaseTest {
	private WebDriver driver;
	private AbUsers abUser;
	private String firstName = "Stor";
	private String middleName = "Broderman";
	private String lastName = "Andan";
	private String companyName = "ABC HOA";
	private String importFirstName = "Heidi";
	private String importMiddleName = "Jo";
	private String importLastName = "Hill";
	private String importAddress1 = "246 Warren Ave";

	@Test
	public void testImportAddress() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		this.abUser = AbUserHelper.getRandomDeptUser("Policy Services");
		// ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
		// rolesToAdd.add(ContactRole.Vendor);
		// rolesToAdd.add(ContactRole.Lienholder);

        GenerateContact myContactObj = new GenerateContact.Builder(driver)
				
				.withCreator(abUser)
				.withFirstLastName(this.firstName, this.middleName, this.lastName)
				.withImportContact(this.importFirstName, this.importMiddleName, this.importLastName,
						this.importAddress1)
				.build(GenerateContactType.Person);

		System.out.println("accountNumber: " + myContactObj.accountNumber);

	}
	
	@Test
	public void importAddressCompanyPerson() throws Exception{
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		this.abUser = AbUserHelper.getRandomDeptUser("Policy Services");
		// ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
		// rolesToAdd.add(ContactRole.Vendor);
		// rolesToAdd.add(ContactRole.Lienholder);

        GenerateContact myContactObj = new GenerateContact.Builder(driver)
				
				.withCreator(abUser)
				.withFirstLastName(this.firstName, this.middleName, this.lastName)
				.withImportContact(this.companyName, this.importAddress1)
				.build(GenerateContactType.Person);

		System.out.println("accountNumber: " + myContactObj.accountNumber);

		
	}
}
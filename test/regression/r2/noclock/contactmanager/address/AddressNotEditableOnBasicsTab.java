package regression.r2.noclock.contactmanager.address;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.GenerateContactType;
import repository.gw.generate.GenerateContact;
import repository.gw.helpers.GuidewireHelpers;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;

public class AddressNotEditableOnBasicsTab extends BaseTest {
	private WebDriver driver;
	private AbUsers creatorUsername;
	private AbUsers claimsUser;
	
	/**
	* @Author sbroderick
	* @Requirement The Primary address on the basics tab should be un-editable. 
	* @RequirementsLink <a href="https://rally1.rallydev.com/#/10075774537ud/detail/userstory/35485215163"> Address is not editable on the Basic Tab</a>
	* @Description For Numbering group only, addresses will be added and maintained on the basics page.
	* @DATE Dec 12, 2016
	* @throws Exception
	*/
	@Test
	public void generateNewContactNumbering() throws Exception{
		this.creatorUsername = AbUserHelper.getRandomDeptUser("Policy Services");
		this.claimsUser = AbUserHelper.getRandomDeptUser("Claims");
		
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);

        new GenerateContact.Builder(driver)
				.withCreator(creatorUsername)
				.build(GenerateContactType.Company);

        ContactDetailsBasicsAB policyContact = new ContactDetailsBasicsAB(driver);
		policyContact.clickContactDetailsBasicsEditLink();

		boolean addressChange;
		try{
			policyContact.setContactDetailsBasicsAddressLine1("addressLine1");
			addressChange = true;
				
		}catch(Exception e){
			addressChange = false;
		}	
		if(addressChange){
			Assert.fail("Addresses should not be editable on the Basics Page.");
		}
		new GuidewireHelpers(driver).logout();
	}
	
	@Test
	public void generateNewContactClaims() throws Exception{
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		this.claimsUser = AbUserHelper.getRandomDeptUser("Claims");
        new GenerateContact.Builder(driver)
				.withCreator(claimsUser)
				.build(GenerateContactType.Company);

        ContactDetailsBasicsAB policyContact = new ContactDetailsBasicsAB(driver);
		policyContact.clickContactDetailsBasicsEditLink();

		boolean addressChange;
		try{
			policyContact.setContactDetailsBasicsAddressLine1("addressLine1");
			addressChange = true;
				
		}catch(Exception e){
			addressChange = false;
		}	
		if(!addressChange){
			Assert.fail("Addresses should be editable on the Basics Page for users that are not in the Policy Services Group.");
		}
		new GuidewireHelpers(driver).logout();
	}
	
}

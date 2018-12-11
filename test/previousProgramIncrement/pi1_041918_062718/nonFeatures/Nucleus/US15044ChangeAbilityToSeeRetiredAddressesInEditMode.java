package previousProgramIncrement.pi1_041918_062718.nonFeatures.Nucleus;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.ab.contact.ContactDetailsAddressesAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.GenerateContactType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.custom.AddressInfo;
import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.helpers.AbUserHelper;

/**
* @Author sbroderick
* @Requirement Change the ability to see retired and active addresses while in edit mode
* @RequirementsLink <a href="https://rally1.rallydev.com/#/211690175148d/detail/userstory/217449640016">Change the ability to see retired and active addresses while in edit mode</a>
* @Description 
* @DATE May 24, 2018
* 
* Acceptance criteria:
Ensure that I have the option to view retired addresses while in edit mode
Ensure that I have the ability to go back to active addresses from the retired addresses while in edit mode 
* 
*/
public class US15044ChangeAbilityToSeeRetiredAddressesInEditMode extends BaseTest {
	private WebDriver driver;
	GenerateContact newContact = null;
	
	@Test
	public void warningRequiredWhenDeletingAnAddress() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);
	    
	    ArrayList<AddressInfo> addresses = new ArrayList<>();
	    addresses.add(new AddressInfo(true));
	    addresses.add(new AddressInfo());

        this.newContact = new GenerateContact.Builder(driver)
	    		.withCreator(AbUserHelper.getRandomDeptUser("Claim"))
	            .withCompanyName("SeeretiredAddresses")
	            .withUniqueName(true)
	            .withAddresses(addresses)
	            .build(GenerateContactType.Company);

        ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
	    basicsPage.clickContactDetailsBasicsAddressLink();
        ContactDetailsAddressesAB addressTab = new ContactDetailsAddressesAB(driver);
	    addressTab.clickEdit();
	    addressTab.retireAddress(addresses.get(1).getLine1());
	    addressTab.clickUpdate();
	    addressTab.clickEdit();
	    addressTab.clickContactDetailsAddressesRetiredLink();
 	    ArrayList<String> retiredAddresses = addressTab.getAddresses();
	    boolean found = false;
	    for(String retiredAddress : retiredAddresses) {
	    	if(retiredAddress.contains(addresses.get(1).getLine1())){
	    		found = true;
	    	}
	    }
	    assertTrue(found, "While in edit mode the retired address should be able to be viewed. See the retired address: "+addresses.get(1).getLine1()+" on Contact: " + this.newContact.companyName +".");
        addressTab = new ContactDetailsAddressesAB(driver);
	    addressTab.clickActive();
	    retiredAddresses = addressTab.getAddresses();
	    for(String retiredAddress : retiredAddresses) {
	    	if(retiredAddress.contains(addresses.get(0).getLine1())){
	    		found = true;
	    	}
	    }
	    assertTrue(found, "While in edit mode the active address should be able to be viewed after viewing the retired address. See the active address: "+addresses.get(0).getLine1()+" on Contact: " + this.newContact.companyName +".");
	}
}

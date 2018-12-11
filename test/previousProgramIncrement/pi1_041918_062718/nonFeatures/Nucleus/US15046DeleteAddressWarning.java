package previousProgramIncrement.pi1_041918_062718.nonFeatures.Nucleus;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.ab.contact.ContactDetailsAddressesAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.GenerateContactType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.custom.AddressInfo;
import gwclockhelpers.ApplicationOrCenter;


/**
* @Author sbroderick
* @Requirement A warning prompt is required when removing an address from a contact.
* @RequirementsLink <a href="https://rally1.rallydev.com/#/211690175148d/detail/userstory/217601444780">Warning prompt when removing an address</a>
* @Description 
* @DATE May 23, 2018
*/
public class US15046DeleteAddressWarning extends BaseTest {
	GenerateContact myContactLienLoc1Obj = null;
	private WebDriver driver;
	
	@Test
	public void warningRequiredWhenDeletingAnAddress() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);
	    
	    ArrayList<AddressInfo> addresses = new ArrayList<>();
	    addresses.add(new AddressInfo());
	    addresses.add(new AddressInfo(true));

        this.myContactLienLoc1Obj = new GenerateContact.Builder(driver)
	            .withCompanyName("DeleteAddress")
	            .withUniqueName(true)
	            .withAddresses(addresses)
	            .build(GenerateContactType.Company);

        ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
	    basicsPage.clickEdit();
	    basicsPage.clickContactDetailsBasicsAddressLink();

        ContactDetailsAddressesAB addressPage = new ContactDetailsAddressesAB(driver);
	    addressPage.removeAddress(addresses.get(1).getLine1());
	    ArrayList<AddressInfo> abAddresses = addressPage.returnAddressInfo();
	    boolean found = false;
	    for(AddressInfo address : abAddresses) {
	    	if(addresses.get(1).getLine1().equals(address.getLine1())) {
	    		found = true;
	    	}
	    }
	    if(found) {
	    	Assert.fail("After attempting to remove the address with address line 1 as "+addresses.get(1)+", the address remains on the contact.  Please investigate.");
	    }
	}

}

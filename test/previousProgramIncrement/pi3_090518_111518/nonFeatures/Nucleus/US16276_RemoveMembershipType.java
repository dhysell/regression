package previousProgramIncrement.pi3_090518_111518.nonFeatures.Nucleus;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.ContactMembershipType;
import repository.gw.enums.GenerateContactType;
import repository.gw.generate.GenerateContact;
import gwclockhelpers.ApplicationOrCenter;

/**
* @Author sbroderick
* @Requirement 
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description 
* @DATE Oct 22, 2018
*/
public class US16276_RemoveMembershipType extends BaseTest{
	
	@Test
	public void testMembershipType() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        WebDriver driver = buildDriver(cf);
		
       new GenerateContact.Builder(driver)
				.withFirstLastName("Membership", "Type")
				.build(GenerateContactType.Person);
        
        Assert.assertFalse(new ContactDetailsBasicsAB(driver).setMembershipType(ContactMembershipType.Regular), "The Membership Type drop down should be disabled");
	}
	

}

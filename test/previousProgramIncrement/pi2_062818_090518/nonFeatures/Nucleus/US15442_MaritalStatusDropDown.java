package previousProgramIncrement.pi2_062818_090518.nonFeatures.Nucleus;

import repository.ab.contact.ContactDetailsBasicsAB;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.MaritalStatus;
import repository.gw.generate.GenerateContact;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;

import java.util.ArrayList;


public class US15442_MaritalStatusDropDown extends BaseTest {
	
	@Test
	public void testMaritalStatusSelectListOptions() throws Exception{		
		AbUsers user = AbUserHelper.getRandomDeptUser("Policy Services");
		
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        WebDriver driver = buildDriver(cf);
		

        GenerateContact myContactObj = new GenerateContact.Builder(driver)
				.withCreator(user)
				.withFirstLastName("Dummy", "", "Data")
				.build(GenerateContactType.Person);

		System.out.println("accountNumber: " + myContactObj.accountNumber);
		ContactDetailsBasicsAB detailsPage = new ContactDetailsBasicsAB(driver);
		detailsPage.clickEdit();
		
		ArrayList<String> actualMaritalStatusOptions = detailsPage.getMaritalStatusOptions();
		boolean found = false; 
		for(MaritalStatus expectedStatus : MaritalStatus.values()) {
			for(String actualStatus : actualMaritalStatusOptions) {
				if(expectedStatus.getValue().equals(actualStatus)) {
					found=true;
					break;
				}
			}
			if(!found) {
				Assert.fail(expectedStatus.getValue() +" was not found in the Marital Status dropdown in ContactManager. Please investigate.");
			}
			found = false;
		}		
	}
}

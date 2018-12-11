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

public class US16709_RemoveCommoditiesWorksheet extends BaseTest{
	
	/**
	* @Author sbroderick
	* @Requirement Disable the Commodities Worksheet as Dues are handled in PolicyCenter.
	* @RequirementsLink <a href="https://fbmicoi.sharepoint.com/:w:/r/sites/TeamNucleus/_layouts/15/Doc.aspx?sourcedoc={be6a6a7b-bac9-4638-ab64-7a14303dd4c1}&action=edit&wdPid=5cd202d1">Commodities Worksheet gone.</a>
	* @Description Disable the Commodities Worksheet as Dues are handled in PolicyCenter.
	* @DATE Oct 29, 2018
	* @throws Exception
	*/
	@Test
	public void testCommoditiesSheetShowsUp() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
	    WebDriver driver = buildDriver(cf);
		
	   new GenerateContact.Builder(driver)
				.withFirstLastName("Commodities", "Sheet")
				.withMembershipType(ContactMembershipType.Regular)
				.build(GenerateContactType.Person);
	    
	    Assert.assertFalse(new ContactDetailsBasicsAB(driver).clickCommoditiesTab(), "The Commodities Tab should not exist.");
	}
}

package previousProgramIncrement.pi3_090518_111518.nonFeatures.Nucleus;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.ab.search.AdvancedSearchAB;
import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.helpers.AbUserHelper;

public class US16706_RetireDealerContactTag extends BaseTest{

	/**
	* @Author sbroderick
	* @Requirement Retire the Dealer role
	* @RequirementsLink <a href="https://fbmicoi.sharepoint.com/:w:/r/sites/TeamNucleus/_layouts/15/Doc.aspx?sourcedoc=%7B5794482E-5A95-471C-AFD2-5A1CB0E3703C%7D&file=US16706%20-%20CM%20-%20Retire%20the%20dealer%20tag.docx&action=default&mobileredirect=true">US16706 Requirements Document</a>
	* @Description The Dealer Contact role is no longer necessary and will not be an option for contacts to have.
	* @DATE Oct 25, 2018
	* @throws Exception
	*/
	@Test
	public void testRetireDealerContactTag() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        WebDriver driver = buildDriver(cf);
        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
        searchMe.loginAndGetToSearch(AbUserHelper.getRandomDeptUser("Policy Service"));
        Assert.assertFalse(searchMe.setRoles("Dealer"), "The \"Dealer\" existed in the Roles drop down and according to requirments should not exist.");
	}
}

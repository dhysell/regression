package previousProgramIncrement.pi1_041918_062718.nonFeatures.Nucleus;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.ab.search.AdvancedSearchAB;
import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.helpers.AbUserHelper;

public class US16706_RetireDealerContactTag extends BaseTest{

	@Test
	public void testRetireDealerContactTag() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        WebDriver driver = buildDriver(cf);
        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
        searchMe.loginAndGetToSearch(AbUserHelper.getRandomDeptUser("Policy Service"));
        Assert.assertFalse(searchMe.setRoles("Dealer"), "The \"Dealer\" existed in the Roles drop down and according to requirments should not exist.");
	}
}

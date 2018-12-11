package random;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.StartablePlugin;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import repository.gw.servertools.ServerToolsSideBar;
import repository.gw.servertools.ServerToolsStartablePlugin;
import gwclockhelpers.ApplicationOrCenter;

public class RestartPolicyFormInferencePlugin extends BaseTest {
	
	@Test
    public void loginToPCAndRestartFormInferencePlugin() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
	    WebDriver driver = buildDriver(cf);
	    
		Login login = new Login(driver);
		login.login("su", "gw");
		
		new GuidewireHelpers(driver).pressAltShiftT();
		new ServerToolsSideBar(driver).clickStartablePlugin();
		new ServerToolsStartablePlugin(driver).stopStartablePlugin(StartablePlugin.FormInferenceConfigurationPlugin);
		new ServerToolsStartablePlugin(driver).startStartablePlugin(StartablePlugin.FormInferenceConfigurationPlugin);
		new GuidewireHelpers(driver).logout();
	}
}

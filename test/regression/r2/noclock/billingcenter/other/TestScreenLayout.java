package regression.r2.noclock.billingcenter.other;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.Config;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
public class TestScreenLayout extends BaseTest {
	private WebDriver driver;
	private String userName = "sbrunson";
	private String password = "gw";

	@Test
	public void testTopMenu() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);

		Login lp = new Login(driver);
		lp.login(userName, password);

		BCTopMenu menu = new BCTopMenu(driver);

		menu.clickDesktopArrow();
		menu.clickSearchArrow();
		menu.clickAccountArrow();
		menu.clickAccountTab();
		menu.clickDesktopTab();
		menu.clickPolicyTab();
		menu.clickSearchTab();
		menu.clickAccountArrow();
	}
	
}

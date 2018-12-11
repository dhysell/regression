package regression.r2.noclock.billingcenter.other;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.actions.DesktopActionsChargeReversal;
import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.Config;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
public class TestCharges extends BaseTest {
	private WebDriver driver;
	private String username = "sbrunson";
	private String password = "gw";

	@Test(enabled=true)
	public void testSettingMinMaxAmounts() throws Exception {
				
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);

		Login lp = new Login(driver);
		lp.login(username, password);

		BCTopMenu menu = new BCTopMenu(driver);
		menu.clickDesktopTab();
		BCDesktopMenu myDesktopMenu = new BCDesktopMenu(driver);
		myDesktopMenu.clickDesktopMenuActionsChargeReversal();
		DesktopActionsChargeReversal myChgReversal = new DesktopActionsChargeReversal(driver);
		myChgReversal.setChargeReversalMinimiumAmount(100.1);
		myChgReversal.setChargeReversalMaximumAmount(200);
		new GuidewireHelpers(driver).logout();

	}

}

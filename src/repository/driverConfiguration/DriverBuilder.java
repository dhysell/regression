package repository.driverConfiguration;

import com.idfbins.driver.BaseDriverBuilder;
import org.openqa.selenium.WebDriver;


public class DriverBuilder extends BaseDriverBuilder {
	public WebDriver buildGWWebDriver(Config cf) throws Exception {
        return super.buildWebDriver(cf);
    }
}
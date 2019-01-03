package repository.cc.framework.init;

import com.idfbins.driver.BaseTest;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import repository.cc.framework.ClaimCenterActions;
import repository.driverConfiguration.Config;

public abstract class ClaimCenterBaseTest extends BaseTest implements InitOperations {

    protected WebDriver driver;
    protected String env;
    protected ClaimCenterActions actions;

    public void init() {
        try {
            this.actions = new ClaimCenterActions(this.driver, this.env);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void initDriverOn(String env) {
        try {
            Config cf = new Config(ApplicationOrCenter.ClaimCenter, env);
            this.driver = buildDriver(cf);
            this.env = env;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unable to initialize WebDriver.");
        }
    }

    @AfterMethod
    public void testDriverTeardown() {
        if (this.driver != null) {
            this.driver.quit();
        }
        this.actions.db.closeConnection();
    }
}

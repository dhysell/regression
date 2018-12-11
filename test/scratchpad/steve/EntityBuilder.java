package scratchpad.steve;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;

public class EntityBuilder extends BaseTest {

    private WebDriver driver;

    @Test
    public void useEntityBuilder() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        scratchpad.steve.repository.EntityBuilder newEntity = new scratchpad.steve.repository.EntityBuilder(driver);
        GeneratePolicy policy = newEntity.buildEntityGetAccountNumber(1, 1, true, false, false, false);
        new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchJob(policy.agentInfo.agentUserName, policy.agentInfo.getAgentPassword(), policy.accountNumber);
    }
}

package scratchpad.cor.random;

import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.helpers.PaymentUtils;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;

public class RoutingNumberContactManagerTest extends BaseTest {

    private WebDriver driver;

    @Test(enabled = true)
    public void testContactManagerAll() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        //Map<String, String> nonExistentInContactManager = PaymentUtils.getListOfInvalidRoutingNumbersFromEntireListContactManager(0);
        new PaymentUtils(driver).getBadDataFromEntireListContactManager(10);
		
		/*if(nonExistentInContactManager.size() > 0) {
			for(Map.Entry<String, String> entry : nonExistentInContactManager.entrySet()) { 
				System.out.println(entry.getValue() + ": " + entry.getKey());
			}
			
			throw new GuidewireException(Configuration.getInstance().getTargetServer(), "There were " + nonExistentInContactManager.size() + " routing numbers that did not validate with ContactManager");
		}*/
    }

    @Test(enabled = false)
    public void testNexusAll() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Map<String, String> nonExistentInNexus = new PaymentUtils(driver).getListOfInvalidRoutingNumbersFromEntireListNexus(0);

        if (nonExistentInNexus.size() > 0) {
            for (Map.Entry<String, String> entry : nonExistentInNexus.entrySet()) {
                System.out.println(entry.getValue() + ": " + entry.getKey());
            }

            Assert.fail("There were " + nonExistentInNexus.size() + " routing numbers that did not validate with Nexus");
        }
    }

}

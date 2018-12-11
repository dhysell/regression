package scratchpad.cor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.hibernate.qa.guidewire.environments.Urls;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import gwclockhelpers.ClockEnvironmentsHelper;

@QuarantineClass
public class ScratchPad extends BaseTest {

    private WebDriver driver;

    @Test
    public void testOneDotZero() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);

        ArrayList<String> listClockMoving = ClockUtils.getAllClockMovingEnvironments();
        for (String clockMove : listClockMoving) {
            System.out.println(clockMove);
        }
        System.out.println();

        Map<ApplicationOrCenter, Date> currDateMap = ClockUtils.getCurrentDates(driver);
        for (Map.Entry<ApplicationOrCenter, Date> entry : currDateMap.entrySet()) {
            System.out.println(entry.getValue());
        }
        System.out.println();

        Map<ApplicationOrCenter, String> environmentsURLs = new HashMap<ApplicationOrCenter, String>();
        String environment = cf.getEnv();
        List<Urls> myUrls = new ClockEnvironmentsHelper().getUrlsListBasedOnEnvironment(environment);
        for (Urls theUrl : myUrls) {
            environmentsURLs.put(ApplicationOrCenter.getApplicationOrCenterFromStrValue(theUrl.getUrlTypes().getName()), theUrl.getUrl());
        }
        Map<ApplicationOrCenter, Date> currDateMap2 = ClockUtils.getCurrentDates(environmentsURLs);
        for (Map.Entry<ApplicationOrCenter, Date> entry2 : currDateMap2.entrySet()) {
            System.out.println(entry2.getValue());
        }
        System.out.println();

        Date toSet = DateUtils.dateAddSubtract(currDateMap.get(ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Month, 1);
        ClockUtils.setCurrentDates(driver, toSet);
        currDateMap = ClockUtils.getCurrentDates(driver);
        for (Map.Entry<ApplicationOrCenter, Date> entry : currDateMap.entrySet()) {
            System.out.println(entry.getValue());
        }
        System.out.println();

        ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);
        currDateMap = ClockUtils.getCurrentDates(driver);
        for (Map.Entry<ApplicationOrCenter, Date> entry : currDateMap.entrySet()) {
            System.out.println(entry.getValue());
        }
        System.out.println();

    }
	
	/*@Test(dependsOnMethods = { "testOneDotZero" })
	public void testOneDotOne() throws Exception {
		Configuration.setProduct(ApplicationOrCenter.BillingCenter);
	
		System.out.println("Test 1.1");
	}
	
	@Test
	public void testTwoDotZero() throws Exception {
		Configuration.setProduct(ApplicationOrCenter.BillingCenter);
	
		System.out.println("Test 2.0");
	}
	
	@Test(dependsOnMethods = { "testTwoDotZero" })
	public void testTwoDotOne() throws Exception {
		Configuration.setProduct(ApplicationOrCenter.BillingCenter);
	
		System.out.println("Test 2.1");
	}
	
	@Test
	public void testThree() throws Exception {
		Configuration.setProduct(ApplicationOrCenter.BillingCenter);
	
		System.out.println("Test 3");
	}*/

}
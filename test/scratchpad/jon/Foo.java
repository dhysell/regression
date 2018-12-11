package scratchpad.jon;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.generate.GeneratePolicy;

import com.idfbins.driver.BaseTest;

import services.enums.Broker;
import services.helpers.com.idfbins.routingnumber.RoutingNumberHelper;
import persistence.globaldatarepo.entities.RoutingNumbers;
import persistence.globaldatarepo.helpers.RoutingNumbersHelper;
import services.services.com.idfbins.routingnumber.RoutingNumber;

public class Foo extends BaseTest {

    GeneratePolicy myPolicy = null;
    private WebDriver driver;
    @SuppressWarnings("serial")
    List<String> serverList = new ArrayList<String>() {{
        this.add("QA3");
        this.add("DEV3");
        this.add("IT");
        this.add("DEV");
        this.add("UAT");
        this.add("QA2");
        this.add("IT2");
        this.add("UAT2");
        this.add("DEV2");
    }};

    @Test
    public void createClassEnum() throws Exception {

//		PermissionsHelper.saveSUPermission("My Permission", "MyPermissionCode");
    	List<RoutingNumbers> numberList = RoutingNumbersHelper.getAllRoutingNumbers();
    	
    	for(RoutingNumbers number : numberList) {
    		
    		
    		RoutingNumber fooBar = new RoutingNumberHelper(Broker.DEV).validateRoutingNumber(number.getRoutingNumber());
    		if(fooBar.getCustomerName() == null) {
    			continue;
    		}
    		System.out.println("UPDATED ROUNTING NUMBER: " + number.getRoutingNumber());
    		RoutingNumbersHelper.updateCsrRegion(number.getRoutingNumber(), fooBar.getCustomerName(), fooBar.getAddress(), fooBar.getCity(), fooBar.getState(), fooBar.getZip(), fooBar.getZipPlus4(), fooBar.getPhoneAreaCode(), fooBar.getPhonePrefix(), fooBar.getPhoneSuffix());
    	}
    	
    }
}

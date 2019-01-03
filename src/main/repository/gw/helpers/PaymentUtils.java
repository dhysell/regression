package repository.gw.helpers;

import com.idfbins.hibernate.qa.guidewire.environments.Urls;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import persistence.globaldatarepo.entities.RoutingNumbers;
import persistence.globaldatarepo.helpers.EnvironmentsHelper;
import persistence.globaldatarepo.helpers.RoutingNumbersHelper;
import services.enums.Broker;
import services.helpers.com.guidewire.policyservices.PolicyServicesHelper;
import services.helpers.com.idfbins.routingnumber.RoutingNumberHelper;
import services.services.com.guidewire.policyservices.ab.dto.BankInfo;
import services.services.com.idfbins.routingnumber.RoutingNumber;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentUtils {

	private WebDriver driver;
	
	public PaymentUtils(WebDriver driver) {
		this.driver = driver;
	}
	
	private Map<ApplicationOrCenter, Urls> environmentsPrep() throws Exception {		
		String environment = new GuidewireHelpers(driver).getEnvironment();

        return new EnvironmentsHelper().getGuideWireEnvironments(environment);
	}
	
	private List<RoutingNumbers> getEntireRoutingNumberList() throws Exception {
		return RoutingNumbersHelper.getAllRoutingNumbers();
	}
	
	private RoutingNumber validateRoutingNumberNexus(String routingNumber) throws Exception {
		//RoutingNumberHelper helper = new RoutingNumberHelper(Broker.CHRISJOSLIN);
        RoutingNumberHelper helper = new RoutingNumberHelper(Broker.DEV);
		
		return helper.validateRoutingNumber(routingNumber);
	}

	private BankInfo validateRoutingNumberContactManager(String routingNumber) throws Exception {
		PolicyServicesHelper helper = new PolicyServicesHelper(environmentsPrep());
		
		return helper.validateRoutingNumber(routingNumber);
	}
	
	public void getBadDataFromEntireListContactManager(int numToTest) throws Exception {
		List<RoutingNumbers> entireList = getEntireRoutingNumberList();
		
		BankInfo returnedBank;
		
		if (numToTest == 0) {
			numToTest = entireList.size();
		}
		
		System.out.println("Number of Routing Numbers to Test: " + numToTest);
		
		for(int counter = 0; counter < numToTest; counter++) {
			returnedBank = validateRoutingNumberContactManager(entireList.get(counter).getRoutingNumber());
			//System.out.println(counter + 1);
			if(returnedBank.getName() != null) {
				System.out.println(entireList.get(counter).getInstitutionName() + ";" + returnedBank.getName());
			}
		}
	}
	
	public Map<String, String> getListOfInvalidRoutingNumbersFromEntireListContactManager(int numToTest) throws Exception {
		List<RoutingNumbers> entireList = getEntireRoutingNumberList();
		Map<String, String> mapToReturn = new HashMap<String, String>();
		BankInfo returnedBank;
		
		if (numToTest == 0) {
			numToTest = entireList.size();
		}
		
		System.out.println("Number of Routing Numbers to Test: " + numToTest);
		
		for(int counter = 0; counter < numToTest; counter++) {
			returnedBank = validateRoutingNumberContactManager(entireList.get(counter).getRoutingNumber());
			System.out.println(counter + 1);
			if(returnedBank.getName() == null) {
				mapToReturn.put(entireList.get(counter).getRoutingNumber(), entireList.get(counter).getInstitutionName());
			}
		}
		
		return mapToReturn;
	}
	
	public Map<String, String> getListOfInvalidRoutingNumbersFromEntireListNexus(int numToTest) throws Exception {
		List<RoutingNumbers> entireList = getEntireRoutingNumberList();
		Map<String, String> mapToReturn = new HashMap<String, String>();
		RoutingNumber returnedBank;
		
		if (numToTest == 0) {
			numToTest = entireList.size();
		}
		
		System.out.println("Number of Routing Numbers to Test: " + numToTest);
		
		for(int counter = 0; counter < numToTest; counter++) {
			returnedBank = validateRoutingNumberNexus(entireList.get(counter).getRoutingNumber());
			System.out.println(counter + 1);
			if(returnedBank.getCustomerName() == null) {
				mapToReturn.put(entireList.get(counter).getRoutingNumber(), entireList.get(counter).getInstitutionName());
			}
		}
		
		return mapToReturn;
	}

	public String getRandomRoutingNumber() throws Exception {
		List<RoutingNumbers> entireList = getEntireRoutingNumberList();
		
		int sizeOfList = entireList.size();
		
		return entireList.get(NumberUtils.generateRandomNumberInt(0, sizeOfList)).getRoutingNumber();
	}
	
}

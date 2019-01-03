package services.helpers.com.guidewire.policyservices;

import java.net.URL;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import com.idfbins.hibernate.qa.guidewire.environments.Urls;

import gwclockhelpers.ApplicationOrCenter;
import services.services.com.guidewire.policyservices.ab.dto.BankInfo;
import services.services.com.guidewire.policyservices.pc.PolicyServices;
import services.services.com.guidewire.policyservices.pc.PolicyServicesPortType;
import services.services.com.guidewire.policyservices.ws.soapheaders.Authentication;
import services.services.com.guidewire.policyservices.ws.soapheaders.ObjectFactory;

public class PolicyServicesHelper {
	
	private Map<ApplicationOrCenter, Urls> environmentsMap;
	private String username = "su";
	private String password = "gw";
	
	public PolicyServicesHelper(Map<ApplicationOrCenter, Urls> environmentsMap) {
		this.environmentsMap = environmentsMap;
	}

	private void serviceSetup() {
		ObjectFactory header = new ObjectFactory();
		Authentication authen = new Authentication();
		authen.setUsername(username);
		authen.setPassword(password);

		header.createAuthentication(authen);
	}
	
	private void attachAuthentication(BindingProvider api) {
		api.getRequestContext().put(
				BindingProvider.USERNAME_PROPERTY, username);
		 api.getRequestContext().put(
				BindingProvider.PASSWORD_PROPERTY, password);
	}
	
	public BankInfo validateRoutingNumber(String routingNumber) throws Exception {
		serviceSetup();
		
		PolicyServices abService;
	
		String abURL = environmentsMap.get(ApplicationOrCenter.ContactManager).getUrl();

		PolicyServicesPortType abApi = null;
		
		BankInfo bankInfoObj = null;
		
		try {
			abService = new PolicyServices(new URL(abURL	+ "/ab/ws/com/idfbins/ab/webservice/PolicyServices?WSDL"));
			abApi = abService.getPolicyServicesSoap11Port();
			attachAuthentication((BindingProvider) abApi);
			bankInfoObj = (abApi.getBankInformation(routingNumber));

		} catch (Exception e) {
			throw e;
		}

		return bankInfoObj;
	}

}

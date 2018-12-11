package services.helpers.com.guidewire.crmservices;

import java.net.URL;

import javax.xml.ws.BindingProvider;

import services.services.com.guidewire.crmservices.com.example.com.idfbins.ab.crm.CRMSearchResponse;
import services.services.com.guidewire.crmservices.com.guidewire.ws.soapheaders.Authentication;
import services.services.com.guidewire.crmservices.com.guidewire.ws.soapheaders.ObjectFactory;
import services.services.com.guidewire.crmservices.com.idfbins.gw.cc.CRMServices;
import services.services.com.guidewire.crmservices.com.idfbins.gw.cc.CRMServicesPortType;

public class CRMServicesHelper {
	
	private String username = "su";
	private String password = "gw";
	private String serverURL = null;
	
	public CRMServicesHelper(String serverURL) {
		this.serverURL = serverURL;
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
	
	public boolean contactHasAccount(String addressBookUID) throws Exception {
		serviceSetup();
		
		CRMServices abService;

		CRMServicesPortType abApi = null;
		
		boolean contactAccountResponse = false;
		
		try {
			abService = new CRMServices(new URL(serverURL	+ "/ws/com/idfbins/ab/webservice/CRMServices?WSDL"));
			abApi = abService.getCRMServicesSoap12Port();
			attachAuthentication((BindingProvider) abApi);
			contactAccountResponse = abApi.contactHasAccount(addressBookUID);

		} catch (Exception e) {
			throw e;
		}

		return contactAccountResponse;
	}
	
	public CRMSearchResponse searchByAccountNumber(String acct) throws Exception {
		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		
		serviceSetup();
		
		CRMServices abService;

		CRMServicesPortType abApi = null;
		
		CRMSearchResponse contactAccountResponse = null;
		
		try {
			abService = new CRMServices(new URL(serverURL	+ "/ws/com/idfbins/ab/webservice/CRMServices?WSDL"));
			abApi = abService.getCRMServicesSoap12Port();
			attachAuthentication((BindingProvider) abApi);
			contactAccountResponse = abApi.searchByAccountNumber(acct);

		} catch (Exception e) {
			throw e;
		}

		return contactAccountResponse;
	}
}

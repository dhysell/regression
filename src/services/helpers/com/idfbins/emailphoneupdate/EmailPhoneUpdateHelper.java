package services.helpers.com.idfbins.emailphoneupdate;
import java.net.URL;

import javax.xml.ws.BindingProvider;
import services.services.com.idfbins.emailphoneupdate.com.guidewire.ws.soapheaders.Authentication;
import services.services.com.idfbins.emailphoneupdate.com.guidewire.ws.soapheaders.ObjectFactory;
import services.services.com.idfbins.emailphoneupdate.com.idfbins.gw.cc.CRMServices;
import services.services.com.idfbins.emailphoneupdate.com.idfbins.gw.cc.CRMServicesPortType;
import services.services.com.idfbins.emailphoneupdate.com.example.com.idfbins.ab.dto.CRMContactInfo;

public class EmailPhoneUpdateHelper {
	
	
	private String username = "su";
	private String password = "gw";
	private String serverURL = "http://ab8uat.idfbins.com/cc";
	
	public EmailPhoneUpdateHelper(String _serverURL) {
		this.serverURL = _serverURL;
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
	
	public boolean testUpdateContact(CRMContactInfo contactInfo) throws Exception {		
		serviceSetup();
		
		CRMServices abService;

		CRMServicesPortType abApi = null;
		
		boolean updateContactResponse = false;
				
		try {
			abService = new CRMServices(new URL(serverURL	+ "/ws/com/idfbins/ab/webservice/CRMServices?WSDL"));
			abApi = abService.getCRMServicesSoap11Port();
			attachAuthentication((BindingProvider) abApi);
			updateContactResponse = abApi.updateContact(contactInfo);
			System.out.println("The contact has been updated " + updateContactResponse);
			return updateContactResponse;

		} catch (Exception e) {
			throw e;
		}		
	}
}

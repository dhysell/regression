package services.helpers.com.idfbins.claimservices;

import java.net.URL;
import java.util.Map;
import javax.xml.ws.BindingProvider;

import com.idfbins.hibernate.qa.guidewire.environments.Urls;

import gwclockhelpers.ApplicationOrCenter;
import services.services.com.guidewire.policyservices.ws.soapheaders.Authentication;
import services.services.com.guidewire.policyservices.ws.soapheaders.ObjectFactory;
import services.services.com.idfbins.claimservices.example.com.idfbins.ab.dto.ClaimVendorInfo;
import services.services.com.idfbins.claimservices.idfbins.gw.cc.ClaimServices;
import services.services.com.idfbins.claimservices.idfbins.gw.cc.ClaimServicesPortType;


public class ClaimServicesHelper {
	private String username = "su";
	private String password = "gw";
	private Map<ApplicationOrCenter, Urls> environmentsMap;
		
	public ClaimServicesHelper(Map<ApplicationOrCenter, Urls> environmentsMap) {
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
	
	public ClaimVendorInfo getSpecificVendorInfo() throws Exception {
		serviceSetup();
		
		ClaimServices claimService;
	
		String abURL = environmentsMap.get(ApplicationOrCenter.ContactManager).getUrl();

		ClaimServicesPortType claimApi = null;
		
		ClaimVendorInfo vendorInfo = null;
		
		try {
			claimService = new ClaimServices(new URL(abURL	+ "ws/com/idfbins/ab/webservice/ClaimServices?WSDL"));
			claimApi = claimService.getClaimServicesSoap11Port();
			attachAuthentication((BindingProvider) claimApi);
			vendorInfo = (claimApi.retrieveCCCVendorPublicID());

		} catch (Exception e) {
			throw e;
		}

		return vendorInfo;
	}
}

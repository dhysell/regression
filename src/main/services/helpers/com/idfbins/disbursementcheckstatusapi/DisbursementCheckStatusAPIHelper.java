package services.helpers.com.idfbins.disbursementcheckstatusapi;

import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import com.idfbins.hibernate.qa.guidewire.environments.Urls;

import gwclockhelpers.ApplicationOrCenter;
import services.services.com.idfbins.disbursementcheckstatusapi.billingcenter.ropexportresponsetobc.SungardROPResponse;
import services.services.com.idfbins.disbursementcheckstatusapi.common.servicestatus.ServiceStatus;
import services.services.com.idfbins.disbursementcheckstatusapi.gw.bc.DisbursementCheckStatusAPI;
import services.services.com.idfbins.disbursementcheckstatusapi.gw.bc.DisbursementCheckStatusAPIPortType;
import services.services.com.idfbins.disbursementcheckstatusapi.gw.bc.UpdateCheckStatus;
import services.services.com.idfbins.disbursementcheckstatusapi.gw.bc.UpdateCheckStatusResponse.Return;
import services.services.com.idfbins.disbursementcheckstatusapi.ws.soapheaders.Authentication;
import services.services.com.idfbins.disbursementcheckstatusapi.ws.soapheaders.ObjectFactory;

public class DisbursementCheckStatusAPIHelper {
	
	private Map<String, Urls> environmentsMap;
	private String username = "su";
	private String password = "gw";
	
	public DisbursementCheckStatusAPIHelper(Map<String, Urls> environmentsMap) {
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
	
	public ServiceStatus updateCheckStatus(String publicId, String checkNumber, String checkStatus, double issueAmount, Date issueDate) throws Exception {
		serviceSetup();
		
		DisbursementCheckStatusAPI bcService;
	
		String test = ApplicationOrCenter.BillingCenter.getValue();
		
		String bcURL = environmentsMap.get(test).getUrl();

		DisbursementCheckStatusAPIPortType bcApi = null;
		
		ServiceStatus toReturn = null;
		
		try {
			SungardROPResponse myResponse = SungardROPResponse.class.newInstance();
			myResponse.setPublicId(publicId);
			myResponse.setCheckNumber(checkNumber);
			myResponse.setCheckStatus(checkStatus);
			BigDecimal bdIssueAmount = new BigDecimal(issueAmount);
			myResponse.setIssueAmount(bdIssueAmount);
			String strIssueDate = new SimpleDateFormat("yyyy-MM-dd").format(issueDate);
			myResponse.setIssueDate(strIssueDate);
			
			UpdateCheckStatus.Response myOtherRespon = UpdateCheckStatus.Response.class.newInstance();
			myOtherRespon.setSungardROPResponse(myResponse);
			
			bcService = new DisbursementCheckStatusAPI(new URL(bcURL	+ "/bc/ws/com/idfbins/bc/webservice/checkstatus/DisbursementCheckStatusAPI?WSDL"));
			bcApi = bcService.getDisbursementCheckStatusAPISoap11Port();
			attachAuthentication((BindingProvider) bcApi);
			Return myFinalRespon = bcApi.updateCheckStatus(myOtherRespon);
			toReturn = myFinalRespon.getServiceStatus();

		} catch (Exception e) {
			throw e;
		}

		return toReturn;
	}

}

package services.broker.services.address;

import javax.xml.datatype.DatatypeConfigurationException;

import com.idfbins.util.mq.MQUtil;
import com.idfbins.util.mq.MessageBrokerConnectionBuilder;
import com.idfbins.util.mq.SynchronousMessageBrokerConnection;

import services.broker.services.generic.GenericService;
import services.enums.Broker;
import services.broker.objects.address.requestresponse.AddressRequest;
import services.broker.objects.address.requestresponse.AddressResponse;

public class ServiceAddressStandardization {
	
	private SynchronousMessageBrokerConnection mbConn;
	private String addressRequestQueue = "NEXUS.ADDRESS.STANDARDIZE.RQ";
	private String addressResponseQueue = "NEXUS.ADDRESS.STANDARDIZE.RS";
	
	
	public services.broker.objects.address.requestresponse.AddressResponse standardizeAddress(services.broker.objects.address.requestresponse.AddressRequest request, Broker mbConnDetails, boolean printRequestXMLToConsole, boolean printResponseXMLToConsole) throws DatatypeConfigurationException {
		this.mbConn = new MessageBrokerConnectionBuilder()
				.withHost(mbConnDetails.getMQHost())
				.withPort(mbConnDetails.getPort())
				.withQueueManager(mbConnDetails.getQueueManager())
				.withChannel(mbConnDetails.getChannel())
				.withRequestQueue(this.addressRequestQueue)
				.withResponseQueue(this.addressResponseQueue)
			    .toSynchronousMessageBrokerConnection();
		GenericService.mqUtil = new MQUtil(this.mbConn);
		
		GenericService.marshallToString(request, printRequestXMLToConsole);
		services.broker.objects.address.requestresponse.AddressResponse testResult = GenericService.putRequestAndGetResponse(request, this.mbConn.getRequestQueue(), this.mbConn.getResponseQueue(), AddressResponse.class, printResponseXMLToConsole);
		return testResult;
	}

	public services.broker.objects.address.requestresponse.AddressRequest setUpTestAddressRequest(String address1, String address2, String city, String state, String county, String zip, String deliveryPoint) throws Exception {
		services.broker.objects.address.requestresponse.AddressRequest testAddress = new AddressRequest();
		testAddress.setAddressLine1(address1);
		testAddress.setAddressLine2(address2);
		testAddress.setCity(city);
		testAddress.setState(state);
		testAddress.setCounty(county);
		testAddress.setZip(zip);
		testAddress.setDeliveryPoint(deliveryPoint);
		
		return testAddress;
	}
	
}

package services.broker.services.vin;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.idfbins.util.mq.MQUtil;
import com.idfbins.util.mq.MessageBrokerConnectionBuilder;
import com.idfbins.util.mq.SynchronousMessageBrokerConnection;

import services.broker.services.generic.GenericService;
import services.enums.Broker;
import services.broker.objects.vin.requestresponse.ValidateVINRequest;
import services.broker.objects.vin.requestresponse.ValidateVINResponse;

public class ServiceVINValidation {

	private SynchronousMessageBrokerConnection mbConn = new MessageBrokerConnectionBuilder().toSynchronousMessageBrokerConnection();
	private String vinRequestQueue = "FB.VALIDATE.VIN.RQ";
	private String vinResponseQueue = "FB.VALIDATE.VIN.RS";

	public ServiceVINValidation() {
		System.out.println("CREATING DEFAULT OBJECT");
	}


	public services.broker.objects.vin.requestresponse.ValidateVINResponse validateVIN2(services.broker.objects.vin.requestresponse.ValidateVINRequest request, Broker mbConnDetails, boolean printRequestXMLToConsole, boolean printResponseXMLToConsole) throws DatatypeConfigurationException {
		this.mbConn = new MessageBrokerConnectionBuilder()
				.withHost(mbConnDetails.getMQHost())
				.withPort(mbConnDetails.getPort())
				.withQueueManager(mbConnDetails.getQueueManager())
				.withChannel(mbConnDetails.getChannel())
				.withRequestQueue(this.vinRequestQueue)
				.withResponseQueue(this.vinResponseQueue)
				.toSynchronousMessageBrokerConnection();
		GenericService.mqUtil = new MQUtil(this.mbConn);

		GenericService.marshallToString(request, printRequestXMLToConsole);
		services.broker.objects.vin.requestresponse.ValidateVINResponse testResult = GenericService.putRequestAndGetResponse(request, this.mbConn.getRequestQueue(), this.mbConn.getResponseQueue(), ValidateVINResponse.class, printResponseXMLToConsole);
		return testResult;
	}

	public services.broker.objects.vin.requestresponse.ValidateVINRequest setUpTestValidateVINRequest(String vin) throws Exception {
		services.broker.objects.vin.requestresponse.ValidateVINRequest testValidateVINRequest = new services.broker.objects.vin.requestresponse.ValidateVINRequest();
		testValidateVINRequest.setVin(vin);

		return testValidateVINRequest;
	}

	public services.broker.objects.vin.requestresponse.ValidateVINRequest setUpTestValidateVINRequest(String vin, Date asOfDate) throws Exception {
		services.broker.objects.vin.requestresponse.ValidateVINRequest testValidateVINRequest = new ValidateVINRequest();
		testValidateVINRequest.setVin(vin);
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(asOfDate);
		XMLGregorianCalendar asOfDate2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		testValidateVINRequest.setAsOfDate(asOfDate2);

		return testValidateVINRequest;
	}

}

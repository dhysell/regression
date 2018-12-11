package services.broker.services.lexisnexis;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.idfbins.util.mq.MQUtil;
import com.idfbins.util.mq.MessageBrokerConnectionBuilder;
import com.idfbins.util.mq.SynchronousMessageBrokerConnection;

import services.broker.services.generic.GenericService;
import services.enums.Broker;
import persistence.globaldatarepo.entities.LexisNexis;
import services.broker.objects.lexisnexis.cbr.response.actual.NcfReport;
import services.broker.objects.lexisnexis.clueauto.response.actual.CluePersonalAuto;
import services.broker.objects.lexisnexis.clueproperty.response.actual.CluePersonalProperty;
import services.broker.objects.lexisnexis.generic.request.*;
import services.broker.objects.lexisnexis.mvr.response.actual.MvrReport;
import services.broker.objects.lexisnexis.mvr.response.intermediate.Result;
import services.broker.objects.lexisnexis.prefill.personalauto.response.actual.DataprefillReport;
import services.broker.objects.lexisnexis.prefill.vin.response.actual.Report;

public class ServiceLexisNexis {

	private SynchronousMessageBrokerConnection mbConn;
	private String crsRequestQueue = "CRS.REPORT.RQ";
	private String crsResponseQueue = "CRS.REPORT.RS";
	
	private String customerId = null;
	private Date customerDob = null;
	private String customerDlNumber = null;
	private services.broker.objects.lexisnexis.generic.request.StateType customerDlState = null;
	private String customerFirstName = null;
	private String customerMiddleName = null;
	private String customerLastName = null;
	private String customerGender = null;
	private String customerAddressId = null;
	private String customerAddressType = null;
	private String addressId = null;
	private String addressStreet = null;
	private String addressCity = null;
	private services.broker.objects.lexisnexis.generic.request.StateType addressState = null;
	private String addressZip = null;
	private String additionalCustomerId = null;
	private String additionalCustomerFirstName = null;
	private String additionalCustomerMiddleName = null;
	private String additionalCustomerLastName = null;
	private Date additionalCustomerDob = null;
	private String additionalCustomerGender = null;
	
	public services.broker.objects.lexisnexis.mvr.response.actual.MvrReport orderMVR(services.broker.objects.lexisnexis.generic.request.Order request, Broker mbConnDetails, boolean printRequestXMLToConsole, boolean printResponseXMLToConsole) throws DatatypeConfigurationException {
		this.mbConn = new MessageBrokerConnectionBuilder()
				.withHost(mbConnDetails.getMQHost())
				.withPort(mbConnDetails.getPort())
				.withQueueManager(mbConnDetails.getQueueManager())
				.withChannel(mbConnDetails.getChannel())
				.withRequestQueue(this.crsRequestQueue)
				.withResponseQueue(this.crsResponseQueue)
			    .toSynchronousMessageBrokerConnection();
		GenericService.mqUtil = new MQUtil(this.mbConn);
		
		GenericService.marshallToString(request, printRequestXMLToConsole);
		Result testResult = GenericService.putRequestAndGetResponse(request, this.mbConn.getRequestQueue(), this.mbConn.getResponseQueue(), Result.class, printResponseXMLToConsole);
		String innerResult = testResult.getProductResults().getMotorVehicleReport().getReport();
		services.broker.objects.lexisnexis.mvr.response.actual.MvrReport report = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(services.broker.objects.lexisnexis.mvr.response.actual.MvrReport.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(innerResult));
			report = (services.broker.objects.lexisnexis.mvr.response.actual.MvrReport) JAXBIntrospector.getValue(jaxbUnmarshaller.unmarshal(reader, MvrReport.class));
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return report;
	}

	public services.broker.objects.lexisnexis.cbr.response.actual.NcfReport orderCBR(services.broker.objects.lexisnexis.generic.request.Order request, Broker mbConnDetails, boolean printRequestXMLToConsole, boolean printResponseXMLToConsole) throws DatatypeConfigurationException {
		this.mbConn = new MessageBrokerConnectionBuilder()
				.withHost(mbConnDetails.getMQHost())
				.withPort(mbConnDetails.getPort())
				.withQueueManager(mbConnDetails.getQueueManager())
				.withChannel(mbConnDetails.getChannel())
				.withRequestQueue(this.crsRequestQueue)
				.withResponseQueue(this.crsResponseQueue)
			    .toSynchronousMessageBrokerConnection();
		GenericService.mqUtil = new MQUtil(this.mbConn);
		
		GenericService.marshallToString(request, printRequestXMLToConsole);
		services.broker.objects.lexisnexis.cbr.response.intermediate.Result testResult = GenericService.putRequestAndGetResponse(request, this.mbConn.getRequestQueue(), this.mbConn.getResponseQueue(), services.broker.objects.lexisnexis.cbr.response.intermediate.Result.class, printResponseXMLToConsole);
		String innerResult = testResult.getProductResults().getNationalCreditFile().getReport().getValue();
		services.broker.objects.lexisnexis.cbr.response.actual.NcfReport report = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(services.broker.objects.lexisnexis.cbr.response.actual.NcfReport.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(innerResult));
			report = (services.broker.objects.lexisnexis.cbr.response.actual.NcfReport) JAXBIntrospector.getValue(jaxbUnmarshaller.unmarshal(reader, NcfReport.class));
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return report;
	}

	public services.broker.objects.lexisnexis.clueauto.response.actual.CluePersonalAuto orderCLUEAuto(services.broker.objects.lexisnexis.generic.request.Order request, Broker mbConnDetails, boolean printRequestXMLToConsole, boolean printResponseXMLToConsole) throws DatatypeConfigurationException {
		this.mbConn = new MessageBrokerConnectionBuilder()
				.withHost(mbConnDetails.getMQHost())
				.withPort(mbConnDetails.getPort())
				.withQueueManager(mbConnDetails.getQueueManager())
				.withChannel(mbConnDetails.getChannel())
				.withRequestQueue(this.crsRequestQueue)
				.withResponseQueue(this.crsResponseQueue)
			    .toSynchronousMessageBrokerConnection();
		GenericService.mqUtil = new MQUtil(this.mbConn);
		
		GenericService.marshallToString(request, printRequestXMLToConsole);
		services.broker.objects.lexisnexis.clueauto.response.intermediate.Result testResult = GenericService.putRequestAndGetResponse(request, this.mbConn.getRequestQueue(), this.mbConn.getResponseQueue(), services.broker.objects.lexisnexis.clueauto.response.intermediate.Result.class, printResponseXMLToConsole);
		String innerResult = testResult.getProductResults().getCluePersonalAuto().getReport().getValue();
		services.broker.objects.lexisnexis.clueauto.response.actual.CluePersonalAuto report = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(services.broker.objects.lexisnexis.clueauto.response.actual.CluePersonalAuto.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(innerResult));
			report = (services.broker.objects.lexisnexis.clueauto.response.actual.CluePersonalAuto) JAXBIntrospector.getValue(jaxbUnmarshaller.unmarshal(reader, CluePersonalAuto.class));
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return report;
	}

	public services.broker.objects.lexisnexis.clueproperty.response.actual.CluePersonalProperty orderCLUEProperty(services.broker.objects.lexisnexis.generic.request.Order request, Broker mbConnDetails, boolean printRequestXMLToConsole, boolean printResponseXMLToConsole) throws DatatypeConfigurationException {
		this.mbConn = new MessageBrokerConnectionBuilder()
				.withHost(mbConnDetails.getMQHost())
				.withPort(mbConnDetails.getPort())
				.withQueueManager(mbConnDetails.getQueueManager())
				.withChannel(mbConnDetails.getChannel())
				.withRequestQueue(this.crsRequestQueue)
				.withResponseQueue(this.crsResponseQueue)
			    .toSynchronousMessageBrokerConnection();
		GenericService.mqUtil = new MQUtil(this.mbConn);
		
		GenericService.marshallToString(request, printRequestXMLToConsole);
		services.broker.objects.lexisnexis.clueproperty.response.intermediate.Result testResult = GenericService.putRequestAndGetResponse(request, this.mbConn.getRequestQueue(), this.mbConn.getResponseQueue(), services.broker.objects.lexisnexis.clueproperty.response.intermediate.Result.class, printResponseXMLToConsole);
		String innerResult = testResult.getProductResults().getCluePersonalProperty().getReport().getValue();
		services.broker.objects.lexisnexis.clueproperty.response.actual.CluePersonalProperty report = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(services.broker.objects.lexisnexis.clueproperty.response.actual.CluePersonalProperty.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(innerResult));
			report = (services.broker.objects.lexisnexis.clueproperty.response.actual.CluePersonalProperty) JAXBIntrospector.getValue(jaxbUnmarshaller.unmarshal(reader, CluePersonalProperty.class));
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return report;
	}
	
	public services.broker.objects.lexisnexis.prefill.personalauto.response.actual.DataprefillReport orderPrefillPersonal(services.broker.objects.lexisnexis.generic.request.Order request, Broker mbConnDetails, boolean printRequestXMLToConsole, boolean printResponseXMLToConsole) throws DatatypeConfigurationException {
		this.mbConn = new MessageBrokerConnectionBuilder()
				.withHost(mbConnDetails.getMQHost())
				.withPort(mbConnDetails.getPort())
				.withQueueManager(mbConnDetails.getQueueManager())
				.withChannel(mbConnDetails.getChannel())
				.withRequestQueue(this.crsRequestQueue)
				.withResponseQueue(this.crsResponseQueue)
			    .toSynchronousMessageBrokerConnection();
		GenericService.mqUtil = new MQUtil(this.mbConn);
		
		GenericService.marshallToString(request, printRequestXMLToConsole);
		services.broker.objects.lexisnexis.prefill.personalauto.response.intermediate.Result testResult = GenericService.putRequestAndGetResponse(request, this.mbConn.getRequestQueue(), this.mbConn.getResponseQueue(), services.broker.objects.lexisnexis.prefill.personalauto.response.intermediate.Result.class, printResponseXMLToConsole);
		String innerResult = testResult.getProductResults().getAutoDataPrefill().getReport().getValue();
		services.broker.objects.lexisnexis.prefill.personalauto.response.actual.DataprefillReport report = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(services.broker.objects.lexisnexis.prefill.personalauto.response.actual.DataprefillReport.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(innerResult));
			report = (services.broker.objects.lexisnexis.prefill.personalauto.response.actual.DataprefillReport) JAXBIntrospector.getValue(jaxbUnmarshaller.unmarshal(reader, DataprefillReport.class));
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return report;
	}
	
	public services.broker.objects.lexisnexis.prefill.vin.response.actual.Report orderPrefillCommercial(services.broker.objects.lexisnexis.generic.request.Order request, Broker mbConnDetails, boolean printRequestXMLToConsole, boolean printResponseXMLToConsole) throws DatatypeConfigurationException {
		this.mbConn = new MessageBrokerConnectionBuilder()
				.withHost(mbConnDetails.getMQHost())
				.withPort(mbConnDetails.getPort())
				.withQueueManager(mbConnDetails.getQueueManager())
				.withChannel(mbConnDetails.getChannel())
				.withRequestQueue(this.crsRequestQueue)
				.withResponseQueue(this.crsResponseQueue)
			    .toSynchronousMessageBrokerConnection();
		GenericService.mqUtil = new MQUtil(this.mbConn);
		
		GenericService.marshallToString(request, printRequestXMLToConsole);
		services.broker.objects.lexisnexis.prefill.vin.response.intermediate.Result testResult = GenericService.putRequestAndGetResponse(request, this.mbConn.getRequestQueue(), this.mbConn.getResponseQueue(), services.broker.objects.lexisnexis.prefill.vin.response.intermediate.Result.class, printResponseXMLToConsole);
		String innerResult = testResult.getProductResults().getVehicleIdentification().getReport().getValue();
		services.broker.objects.lexisnexis.prefill.vin.response.actual.Report report = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(services.broker.objects.lexisnexis.prefill.vin.response.actual.Report.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(innerResult));
			report = (services.broker.objects.lexisnexis.prefill.vin.response.actual.Report) JAXBIntrospector.getValue(jaxbUnmarshaller.unmarshal(reader, Report.class));
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return report;
	}

	public services.broker.objects.lexisnexis.generic.request.Order setUpTestOrder(services.broker.objects.lexisnexis.generic.request.ReportType typeToSetup, LexisNexis[] custsFromDB) throws Exception {
		setUpTestOrderUserValues(typeToSetup, custsFromDB);
		
		services.broker.objects.lexisnexis.generic.request.Order testOrder = new Order();
		testOrder.setSystemOfOrigin(OriginatingSystemType.PC);
		
		services.broker.objects.lexisnexis.generic.request.CustomerType testCustomerType = new services.broker.objects.lexisnexis.generic.request.CustomerType();
		services.broker.objects.lexisnexis.generic.request.CustomerType testCustomerType2 = new CustomerType();
		if(typeToSetup == services.broker.objects.lexisnexis.generic.request.ReportType.AUTO_DATAPREFILL || typeToSetup == services.broker.objects.lexisnexis.generic.request.ReportType.CLUE_PROPERTY || typeToSetup == services.broker.objects.lexisnexis.generic.request.ReportType.COMMERCIAL_DATAPREFILL) {
			testCustomerType.setType("Primary");
		}
		testCustomerType.setId(this.customerId);
		
		if(customerDob != null) {
			int customerDobYear = Integer.valueOf(new SimpleDateFormat("yyyy").format(this.customerDob));
			int customerDobMonth = Integer.valueOf(new SimpleDateFormat("MM").format(this.customerDob));
			int customerDobDay = Integer.valueOf(new SimpleDateFormat("dd").format(this.customerDob));
			GregorianCalendar dob = new GregorianCalendar(customerDobYear, customerDobMonth, customerDobDay);
			XMLGregorianCalendar testDateOfBirth = DatatypeFactory.newInstance().newXMLGregorianCalendar(dob);
			testDateOfBirth.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
			testCustomerType.setDateOfBirth(testDateOfBirth);
		}
		if(this.customerDlNumber != null) {
			services.broker.objects.lexisnexis.generic.request.DriversLicenseNumberType testDriverLicenseNumberType = new DriversLicenseNumberType();
			testDriverLicenseNumberType.setNumber(this.customerDlNumber);
			testDriverLicenseNumberType.setState(this.customerDlState);
			testCustomerType.setDriversLicenseNumber(testDriverLicenseNumberType);
		}
		services.broker.objects.lexisnexis.generic.request.NameType testNameType = new services.broker.objects.lexisnexis.generic.request.NameType();
		testNameType.setFirstName(this.customerFirstName);
		testNameType.setMiddleName(this.customerMiddleName);
		testNameType.setLastName(this.customerLastName);
		testCustomerType.setName(testNameType);
		if(this.customerGender != null) {
			testCustomerType.setGender(this.customerGender);
		}
		if(this.customerAddressId != null) {
			services.broker.objects.lexisnexis.generic.request.CustomerAddresses testCustomerAddresses = new CustomerAddresses();
			services.broker.objects.lexisnexis.generic.request.CustomerAddressType testCustomerAddressType = new CustomerAddressType();
			testCustomerAddressType.setId(this.customerAddressId);
			testCustomerAddressType.setType(this.customerAddressType);
			testCustomerAddresses.getAddress().add(testCustomerAddressType);
			testCustomerType.setAddresses(testCustomerAddresses);
		}
		if(custsFromDB.length > 1) {
			testCustomerType2.setType("Joint");
			testCustomerType2.setId(this.additionalCustomerId);
			int additionalCustomerDobYear = Integer.valueOf(new SimpleDateFormat("yyyy").format(this.additionalCustomerDob));
			int additionalCustomerDobMonth = Integer.valueOf(new SimpleDateFormat("MM").format(this.additionalCustomerDob));
			int additionalCustomerDobDay = Integer.valueOf(new SimpleDateFormat("dd").format(this.additionalCustomerDob));
			if(additionalCustomerDobYear > 0) {
				GregorianCalendar dob2 = new GregorianCalendar(additionalCustomerDobYear, additionalCustomerDobMonth, additionalCustomerDobDay);
				XMLGregorianCalendar testDateOfBirth2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(dob2);
				testDateOfBirth2.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
				testCustomerType2.setDateOfBirth(testDateOfBirth2);
			}
			services.broker.objects.lexisnexis.generic.request.NameType testNameType2 = new NameType();
			testNameType2.setFirstName(this.additionalCustomerFirstName);
			testNameType2.setMiddleName(this.additionalCustomerMiddleName);
			testNameType2.setLastName(this.additionalCustomerLastName);
			testCustomerType2.setName(testNameType2);
			if(this.additionalCustomerGender != null) {
				testCustomerType2.setGender(this.additionalCustomerGender);
			}
		}
		services.broker.objects.lexisnexis.generic.request.Customers testCustomers = new Customers();
		testCustomers.getCustomer().add(testCustomerType);
		if(custsFromDB.length > 1) {
			testCustomers.getCustomer().add(testCustomerType2);
		}
		testOrder.setCustomers(testCustomers);
		if(this.customerAddressId != null) {
			services.broker.objects.lexisnexis.generic.request.AddressType testAddressType = new AddressType();
			testAddressType.setId(this.addressId);
			testAddressType.setStreet(this.addressStreet);
			testAddressType.setCity(this.addressCity);
			testAddressType.setState(this.addressState);
			testAddressType.setZipCode(this.addressZip);
			services.broker.objects.lexisnexis.generic.request.Addresses testAddresses = new Addresses();
			testAddresses.getAddress().add(testAddressType);
			testOrder.setAddresses(testAddresses);
		}
		services.broker.objects.lexisnexis.generic.request.ReportTypes testReportTypes = new ReportTypes();
		testReportTypes.getReportType().add(typeToSetup);
		testOrder.setReportTypes(testReportTypes);
		
		return testOrder;
	}
	
	private void setUpTestOrderUserValues(services.broker.objects.lexisnexis.generic.request.ReportType typeToSetup, LexisNexis[] custsFromDB) throws Exception {
		this.customerId = "S0";
		this.additionalCustomerId = "S1";
		this.customerFirstName = custsFromDB[0].getFirstName();
		this.customerMiddleName = custsFromDB[0].getMiddleName();
		this.customerLastName = custsFromDB[0].getLastName();
		this.customerDob = null;
		this.customerDlNumber = null;
		this.customerDlState = null;
		this.customerGender = null;
		if(custsFromDB.length > 1) {
			this.additionalCustomerFirstName = custsFromDB[1].getFirstName();
			this.additionalCustomerMiddleName = custsFromDB[1].getMiddleName();
			this.additionalCustomerLastName = custsFromDB[1].getLastName();
			this.additionalCustomerDob = null;
			this.additionalCustomerGender = null;
		}
		this.customerAddressId = null;
		this.customerAddressType = null;
		this.addressId = null;
		this.addressStreet = null;
		this.addressCity = null;
		this.addressState = null;
		this.addressZip = null;
		
		if(typeToSetup == services.broker.objects.lexisnexis.generic.request.ReportType.MVR || typeToSetup == services.broker.objects.lexisnexis.generic.request.ReportType.AUTO_DATAPREFILL || typeToSetup == services.broker.objects.lexisnexis.generic.request.ReportType.CLUE_PROPERTY) {
			if(typeToSetup == services.broker.objects.lexisnexis.generic.request.ReportType.MVR) {
				this.customerDlNumber = custsFromDB[0].getDlnumber();
				this.customerDlState = services.broker.objects.lexisnexis.generic.request.StateType.fromValue(custsFromDB[0].getState());
			}
			this.customerDob = custsFromDB[0].getDob();
			this.customerGender = custsFromDB[0].getGender();	
			if(custsFromDB.length > 1) {
				this.additionalCustomerDob = custsFromDB[1].getDob();
				this.additionalCustomerGender = custsFromDB[1].getGender();	
			}
		} 

		if(typeToSetup != ReportType.MVR) {
			this.customerAddressId = "A0";
			this.customerAddressType = "primary";
			this.addressId = customerAddressId;
			this.addressStreet = custsFromDB[0].getStreet();
			this.addressCity = custsFromDB[0].getCity();
			this.addressState = StateType.fromValue(custsFromDB[0].getState());
			this.addressZip = custsFromDB[0].getZip();
		}			
	}
	
}

package services.helpers.com.idfbins.membernumber;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import services.enums.Broker;
import services.services.com.idfbins.membernumber.MembershipQueryResponse;

public class MemberNumberHelper {
	
	private Broker broker;

	public MemberNumberHelper(Broker broker) {
		this.broker = broker;
	}

	public MembershipQueryResponse getContactRecords(String memberNumber) throws JAXBException, MalformedURLException {
		
		JAXBContext jc = JAXBContext.newInstance("services.com.idfbins.membernumber");
		Unmarshaller u = jc.createUnmarshaller();
		URL url = new URL("http://" + this.broker.getMBHost() + ":7080/membership/query?memberNumber=" + memberNumber);

		System.out.println("Getting XML file from:");
		System.out.println(url);

		@SuppressWarnings("rawtypes")
		JAXBElement jaxbelement = (JAXBElement) u.unmarshal(url);
		MembershipQueryResponse response = (MembershipQueryResponse) jaxbelement.getValue();
		return response;
	}

}

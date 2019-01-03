package services.broker.services.generic;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.idfbins.util.mq.MQUtil;

public class GenericService {
	
	public static MQUtil mqUtil;
	
	public static <A> String marshallToString(A requestObject, boolean printRequestXMLToConsole) {
		StringWriter stringWriter = new StringWriter();
		JAXB.marshal(requestObject, stringWriter);
		String toReturn = stringWriter.toString();
		if(printRequestXMLToConsole) {
			System.out.println(toReturn);
		}
		return toReturn;
		
	}

	@SuppressWarnings("unchecked")
	public static <A, B> B putRequestAndGetResponse(A requestObject, String requestQueue, String responseQueue, Class<B> responseClass, boolean printResponseXMLToConsole) {
		B b = null;
		try {
			StringWriter stringWriter = new StringWriter();
			JAXB.marshal(requestObject, stringWriter);
			MQMessage sentMessage = mqUtil.put(stringWriter.toString(), requestQueue);
			MQMessage retreivedMessage = mqUtil.get(responseQueue, sentMessage.messageId);
			String responseXml = retreivedMessage.readStringOfCharLength(retreivedMessage.getDataLength());
			if(printResponseXMLToConsole) {
				System.out.println(responseXml);
			}
			
			JAXBContext jaxbContext = JAXBContext.newInstance(responseClass);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(responseXml));
			b = (B) JAXBIntrospector.getValue(jaxbUnmarshaller.unmarshal(reader, responseClass));
		} catch (MQException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return b;
	}
	
}

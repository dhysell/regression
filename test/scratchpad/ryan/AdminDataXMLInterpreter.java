package scratchpad.ryan;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
public class AdminDataXMLInterpreter {

	@Test
	public void updateAdgentsTable() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		
		File inputFile = new File("C:/Users/rlonardo/Downloads/admin.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
		
        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        NodeList userList = doc.getElementsByTagName("UserContact");
        //NodeList addressList = doc.getElementsByTagName("Address");
        
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        
        
        System.out.println("----------------------------");
		
        for (int temp = 0; temp < userList.getLength(); temp++) {
        	Node nNode = userList.item(temp);
            Element eElement = (Element) nNode;
            
            if(eElement.getElementsByTagName("AssignedAgentNumber_FBM").item(0).getTextContent().isEmpty()) {
            	continue;
            }
            
            String addressID = eElement.getElementsByTagName("PrimaryAddress").item(0).getAttributes().item(0).getTextContent();
            
            XPathExpression expr = xpath.compile("/import/Address[@public-id='"+addressID+"']");
            NodeList addressResult = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);
            
            Node addressNode = addressResult.item(0);
            Element addressElement = (Element) addressNode;
            
            
            System.out.println(eElement.getAttribute("public-id"));
            
            System.out.println(StringUtils.getDigits(eElement.getElementsByTagName("AssignedAgentNumber_FBM").item(0).getTextContent()));
            System.out.println(eElement.getElementsByTagName("AlternateName_FBM").item(0).getTextContent());
            System.out.println(eElement.getElementsByTagName("FirstName").item(0).getTextContent());
            System.out.println(eElement.getElementsByTagName("MiddleName").item(0).getTextContent());
            System.out.println(eElement.getElementsByTagName("LastName").item(0).getTextContent());
            
            System.out.println("Address:");
            System.out.println(addressElement.getElementsByTagName("AddressLine1").item(0).getTextContent());
        }
	}
	
}

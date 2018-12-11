package scratchpad.jon.mine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import persistence.globaldatarepo.entities.CPClassCodes;
import persistence.globaldatarepo.entities.CPUWQuestions;
import persistence.globaldatarepo.helpers.CPClassCodesHelper;
import persistence.globaldatarepo.helpers.CPUWQuestionsHelper;
public class XMLUtilities {

	@Test(enabled = false)
    public void updateCP_ClassCodes() throws Exception {
		List<CPClassCodes> classCodeList = new ArrayList<CPClassCodes>();

		File inputFile = new File("C:/dev/Guidewire/PolicyCenter/modules/configuration/config/resources/systables/cp_class_codes.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(inputFile);
		doc.getDocumentElement().normalize();

		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		NodeList nList = doc.getElementsByTagName("CPClassCode");
		System.out.println("----------------------------");

		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			Element eElement = (Element) nNode;
			if(eElement.getElementsByTagName("SpecialCode").item(0).getTextContent().equals("true")) {
				continue;
			}
			System.out.println(eElement.getElementsByTagName("Code").item(0).getTextContent());
			System.out.println(eElement.getElementsByTagName("Classification").item(0).getTextContent());
			System.out.println(eElement.getElementsByTagName("ClassStatus").item(0).getTextContent());
			System.out.println(eElement.getElementsByTagName("ClassRated").item(0).getTextContent());
			String rated = (eElement.getElementsByTagName("ClassRated").item(0).getTextContent().equals("true")?"Class Rated":"-");

			classCodeList.add(new CPClassCodes(
					eElement.getElementsByTagName("Code").item(0).getTextContent(), 
					eElement.getElementsByTagName("Classification").item(0).getTextContent(), 
					StringUtils.capitalize(eElement.getElementsByTagName("ClassStatus").item(0).getTextContent()), 
					rated));
		}
		CPClassCodesHelper.createNewCPClassCode(classCodeList);
	}

	@Test(enabled = false)
    public void updateAdgentsTable() throws ParserConfigurationException, SAXException, IOException {

		File inputFile = new File("C:/Users/jlarsen/Downloads/admin.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(inputFile);
		doc.getDocumentElement().normalize();

		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		NodeList nList = doc.getElementsByTagName("UserContact");
		System.out.println("----------------------------");

		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			Element eElement = (Element) nNode;

			if(eElement.getElementsByTagName("AssignedAgentNumber_FBM").item(0).getTextContent().isEmpty()) {
				continue;
			}

			System.out.println(eElement.getAttribute("public-id"));

			System.out.println(StringUtils.getDigits(eElement.getElementsByTagName("AssignedAgentNumber_FBM").item(0).getTextContent()));
			System.out.println(eElement.getElementsByTagName("AlternateName_FBM").item(0).getTextContent());
			System.out.println(eElement.getElementsByTagName("FirstName").item(0).getTextContent());
			System.out.println(eElement.getElementsByTagName("MiddleName").item(0).getTextContent());
			System.out.println(eElement.getElementsByTagName("LastName").item(0).getTextContent());
		}
	}



	@Test(enabled = false)
	public void updateQuestionSets() throws Exception {
		List<CPUWQuestions> cpQuestions = new ArrayList<CPUWQuestions>();


		//GET QUESTION SETTINGS INFO
		String classCode = null;//
		String classCodeName = null;//
		String locationOfQuestion = null;
		String parentQuestionCode = null;//
		String questionCode = null;//
		String sequence = null;//
		String questionType = null;//
		String questionText = null;//
		String questionFormat = null;//
		String choiceOptions = "";//
		String requiredAt = null;
		String dependentOnQuesstion = null;//
		String dependentOnAnswer = null;//
		String handleIncorrectAnswer = null;
		String correctAnswer = null;//
		String failureMessage = null;//
		String blockingAction = null;//
		String uwIssueType = null;//
		
		File inputFile = new File("C:/dev/Guidewire/PolicyCenter/modules/configuration/config/resources/productmodel/questionsets/CPCPClassCodeQuestions.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(inputFile);
		doc.getDocumentElement().normalize();
		NodeList nList = doc.getElementsByTagName("Question");

		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			Element eElement = (Element) nNode;

			blockingAction = eElement.getAttribute("blockingAction");
			correctAnswer = eElement.getAttribute("correctAnswer");
			sequence = eElement.getAttribute("priority");
			questionCode = eElement.getAttribute("codeIdentifier");
			questionFormat = eElement.getAttribute("questionFormat");
//			System.out.println("HAS POSTBACK: " + eElement.getAttribute("questionPostOnChange"));
			questionType = eElement.getAttribute("questionType");
//			System.out.println("IS REQUIRED: " + eElement.getAttribute("required"));
			uwIssueType = (eElement.getAttribute("uwIssueType") != null ? eElement.getAttribute("uwIssueType") : "-");
			
			classCode = eElement.getElementsByTagName("AvailabilityScript").item(0).getTextContent().substring(eElement.getElementsByTagName("AvailabilityScript").item(0).getTextContent().indexOf("==")+2).replace("\"", "").trim();
			if(classCode.contains("&")) {
				classCode = classCode.substring(0, classCode.indexOf("&"));
			}
			if(eElement.getElementsByTagName("QuestionFilter") != null) {
				Node foo = eElement.getElementsByTagName("QuestionFilter").item(0);
				Element eElement2 = (Element) foo;
				if(eElement2 != null) {
					parentQuestionCode = eElement2.getAttribute("filterQuestion");
					dependentOnAnswer = eElement2.getAttribute("answer");
				}
			}
			

			//QUESTIONS TEXT - FAILURE MESSAGES - CHOICE OPTIONS
			File file =new File("C:/dev/Guidewire/PolicyCenter/modules/configuration/config/locale/en_US/productmodel.display.properties");
			Scanner in = new Scanner(file);
			while(in.hasNext()) {
				String line=in.nextLine();
				if(line.contains("QuestionSet_CPCPClassCodeQuestions.Question_" + questionCode + ".Text")) {
					questionText = line.substring(line.indexOf("=")+2).trim().replace("\\", "");
					break;
				} else if(line.contains("QuestionSet_CPCPClassCodeQuestions.Question_" + questionCode + ".FailureMessage")) {
					failureMessage = line.substring(line.indexOf("=")+2).trim().replace("\\", "");
					break;
				} else if(line.contains("QuestionSet_CPCPClassCodeQuestions.Question_" + questionCode + ".QuestionChoice_")) {
					choiceOptions += "-" + line.substring(line.indexOf("=")+2).trim().replace("\\", "");
				}
			}
			in.close();
			
			File inputFile1 = new File("C:/dev/Guidewire/PolicyCenter/modules/configuration/config/resources/systables/cp_class_codes.xml");
			DocumentBuilderFactory dbFactory1 = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder1 = dbFactory1.newDocumentBuilder();
			Document doc1 = dBuilder1.parse(inputFile1);
			doc1.getDocumentElement().normalize();

			NodeList nList1 = doc1.getElementsByTagName("CPClassCode");

			for (int temp1 = 0; temp1 < nList1.getLength(); temp1++) {
				Node nNode1 = nList1.item(temp1);
				Element eElement1 = (Element) nNode1;
				if(eElement1.getElementsByTagName("Code").item(0).getTextContent().equals(classCode)) {
					classCodeName = eElement1.getElementsByTagName("Classification").item(0).getTextContent();
					break;
				}
			}

			handleIncorrectAnswer = (failureMessage != null) ? "Yes" : "No";
			
			cpQuestions.add(new CPUWQuestions(classCode, classCodeName, locationOfQuestion, parentQuestionCode, questionCode, sequence, questionType, questionText, questionFormat, choiceOptions, requiredAt, dependentOnQuesstion, dependentOnAnswer, handleIncorrectAnswer, correctAnswer, failureMessage, blockingAction, uwIssueType));
			
			classCode = null;
			classCodeName = null;
			locationOfQuestion = null;
			parentQuestionCode = null;//
			questionCode = null;//
			sequence = null;//
			questionType = null;//
			questionText = null;//
			questionFormat = null;//
			choiceOptions = "";//
			requiredAt = null;
			dependentOnQuesstion = null;//
			dependentOnAnswer = null;//
			handleIncorrectAnswer = null;
			correctAnswer = null;//
			failureMessage = null;//
			blockingAction = null;//
			uwIssueType = null;//
		}//END FOR


		CPUWQuestionsHelper.createNewCPClassCode(cpQuestions);
	}





















}


















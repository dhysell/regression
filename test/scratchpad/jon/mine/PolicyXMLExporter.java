package scratchpad.jon.mine;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import repository.gw.enums.LineSelection;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfoDeliveryOption;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
public class PolicyXMLExporter {

	private static String xmlFile = "";
	private static String xmlFileLocation = "C:/Users/rlonardo/Desktop/testXMLPolicyObject.txt";

//int[] intArray = (int[]) Array.newInstance(int.class, 3);
//	BufferedReader br = new BufferedReader(car);
//
//    Class<?> c = br.getClass();
//    Field f = c.getDeclaredField("cb");
//
//    // cb is a private field
//    f.setAccessible(true);
//    char[] cbVal = char[].class.cast(f.get(br));
	
//	public static Object[] unpack(Object array) {
//	    Object[] array2 = new Object[Array.getLength(array)];
//	    for(int i=0;i<array2.length;i++)
//	        array2[i] = Array.get(array, i);
//	    return array2;
//	}
	public static void createXML(Object object, GeneratePolicy generatePolicy, int depth) throws IllegalArgumentException, IllegalAccessException {
		
		Field[] fieldList = convertToFieldArray(object);
		for(Field field : fieldList) {
			field.setAccessible(true);
			Object value = field.get(object);
			if(value != null) {
				
				//IF STRING, BOOLEAN, ENUM  Boolean
				if(field.getType().toString().contains("boolean") || field.getType().toString().contains("Boolean") || field.getType().toString().contains("String") || field.getType().toString().contains("enums") || field.getType().toString().contains("Date") || field.getType().toString().contains("Integer") || field.getType().isAssignableFrom(int.class) || field.getType().toString().contains("Long")) {
					isPrintable(field, value, depth, false);
					printEndProperty(depth, false, field);
				//IF CUSTOM OBJECT
				} else if(value.getClass().getTypeName().contains("gw.generate.custom") || value.getClass().getTypeName().contains("persistence.globaldatarepo.entities") || field.getType().toString().contains("gw.generate.custom")) {
					isPrintable(field, value, depth, true);
					createXML(value, generatePolicy, depth+1);
					printEndProperty(depth, true, field);
				//IS LIST List
				} else if(field.getType().toString().contains("List")) {
					isPrintable(field, value, depth, true);
					
//					System.out.println(field.getType());
////					Class<?> c = field.getType();
//					System.out.println("Value Object is an ArrayList");
//					
//					System.out.println(field.getType());
//					
//					System.out.println(field.get(object).toString());
					
					
					
//					 out.format("%s%n"
//                             + "           Field: %s%n"
//			       + "            Type: %s%n"
//			       + "  Component Type: %s%n",
//			       field, field.getName(), c, c.getComponentType());
					ArrayList<?> list = (ArrayList<?>)value;
					
					
//					 Object[] myArray = new Object[list.size()];
//					 for(int i=0;i<myArray.length;i++) {
//						 myArray[i] = Array.get(myArray, i);
//					 }
					
					for (int i = 0; i < list.size(); i++) {
						printListObjectHeader(list.get(i), i, depth+1);
						if (list.get(i).getClass().getTypeName().contains("gw.generate.custom") || list.get(i).getClass().getTypeName().contains("persistence.globaldatarepo.entities") || list.get(i).getClass().getTypeName().contains("gw.generate.custom")) {
							createXML(list.get(i), generatePolicy, depth+2);
						} else {
							printListValue(list.get(i), depth+2);
						}
						printEndListObject(depth+1);
					}
					 
					 
					//printList(field.getName(), generatePolicy, depth+1);
					
					printEndProperty(depth, true, field);
					
					//createXML(value, generatePolicy);
				} else {
					//printTabs(depth);
					//System.out.println("NOT A KNOWN OBJECT ---------------");
					//isPrintable(field, value, depth);
					//System.out.println(field.getType());
				}
			} else { //if property is NULL
				xmlFile += printTabs(depth);
				System.out.println("<" + field.getName() + "/>");
				setXmlFile(getXmlFile() + "<" + field.getName() + "/>\n");
			}
		}
		
		try {
		    BufferedWriter out = new BufferedWriter(new FileWriter(xmlFileLocation));
		    out.write(xmlFile);
		    out.close();
		} catch (IOException e) {
		    System.out.println("Exception ");
		}
		
	}
	
	private static void printListValue(Object object, int depth) {
		xmlFile += printTabs(depth);
		
		System.out.println("<" + object.getClass().getTypeName() + ">" + object.toString() + "</" + object.getClass().getTypeName() + ">");
		setXmlFile(getXmlFile() + "<" + object.getClass().getTypeName() + ">" + object.toString() + "</" + object.getClass().getTypeName() + ">\n");
		
	}

	private static void printEndListObject(int depth) {
		xmlFile += printTabs(depth);
		System.out.println("</list_object>");
		setXmlFile(getXmlFile() + "</list_object>\n");
		
	}

	private static void printListObjectHeader(Object listObject, int i, int depth) {
		xmlFile += printTabs(depth);
		System.out.println("<list_object class=\"" + listObject.getClass() + "\">");
		setXmlFile(getXmlFile() + "<list_object class=\"" + listObject.getClass() + "\">\n");
		
	}

	@SuppressWarnings("unused")
	private static void printList(String listName, GeneratePolicy generatePolicy, int depth) throws IllegalArgumentException, IllegalAccessException {

		switch(listName) {
		case "aniList":
			//PolicyInfoAdditionalNamedInsured
			for(PolicyInfoAdditionalNamedInsured aiList : generatePolicy.aniList) {
				createXML(aiList, generatePolicy, depth+1);
			}
			break;
		case "locationList":
			break;
			//PolicyLocation
		case "recEquipment_PL_IM":
			//RecreationalEquipment
			break;
		case "lineSelection":
			//LineSelection
			for(LineSelection aiList : generatePolicy.lineSelection) {
				createXML(aiList, generatePolicy, depth+1);
			}
			break;
		case "inlandMarineCoverageSelection_PL_IM":
		case "inlandMarineCoverageSelection_Standard_IM":
			//InlandMarine
			break;
		case "cargo_PL_IM":
//			/SquireIMCargo
			break;
		case "livestock_PL_IM":
			//Livestock
			break;
		case "watercrafts_PL_IM":
			//SquireIMWatercraft
			break;
		case "eventsHitDuringSubmissionCreation":
		case "eventsHitDuringIssuanceCreation":
			//DocFormEvents
			break;
		case "formsOrDocsExpectedBasedOnSubmissionEventsHit":
		case "formsOrDocsExpectedBasedOnIssuanceEventsHit":
		case "formsOrDocsActualFromUISubmission":
		case "formsOrDocsActualFromUIIssuance":
			//String
			break;
		case "personalProperty_PL_IM":
			//PersonalProperty
			break;
		case "farmEquipment":
			//FarmEquipment
			break;
		case "policyMembers":
			//Person
			break;
		case "deliveryOptionList":
			//AddressInfoDeliveryOption
			for(AddressInfoDeliveryOption delivery : generatePolicy.pniContact.getAddress().getDeliveryOptionList()) {
				createXML(delivery, generatePolicy, depth+1);
			}
			break;
		case "additonalInsuredBOLineList":
			//PolicyBusinessownersLineAdditionalInsured
			break;
		case "glForms":
			//GeneralLiabilityForms
			break;
		case "glExposures":
			//CPPGeneralLiabilityExposures
			break;
		case "residentialTypes":
			//ResidentialConstructionType
			break;
		case "commercialtypes":
			//CommercialConstructionType
			break;
		case "constructionActivityList":
			//ConstructionActivities
			break;
		case "additionalInsuredslist":
			//CPPGLCoveragesAdditionalInsureds
			break;
		case "underWritingQuestions_Coverages":
			//CPPGLCoveragesUWQuestions
			break;
		case "underWritingQuestions_Exposures":
			//CPPGLExposureUWQuestions
			break;
		case "garageKeepers":
			//CPPCommercialAutoGarageKeepers
			break;
		case "vehicleList":
			//Vehicle
			break;
		case "commercialAutoForms":
			//CommercialAutoForm
			break;
		default:
			System.out.println(listName + " NOT IN SWITCH");
			break;
		}


		
		
		
		
	}
	
	

	private static Field[] convertToFieldArray(Object object) {
		return  object.getClass().getDeclaredFields();
	}
	
	
	
	public static void isPrintable(Field field, Object value, int depth, boolean isObject) {
		
		setXmlFile(getXmlFile() + printTabs(depth));
		
		if (!isObject) {
		
			System.out.println("<" + field.getName() + ">");
			setXmlFile(getXmlFile() + "<" + field.getName() + ">\n");
		
			setXmlFile(getXmlFile() + printTabs(depth+1));
			
			System.out.println("<" + field.getType() + ">" + value + "</" + field.getType() + ">");
			setXmlFile(getXmlFile() + "<" + field.getType() + ">" + value + "</" + field.getType() + ">\n");
			
		} else {
			
			System.out.println("<object class=\"" + field.getType() + " name=\"" + field.getName() + "\">");
			setXmlFile(getXmlFile() + "<object class=\"" + field.getType() + " name=\"" + field.getName() + "\">\n");
			
		}
		
		
		
	}
	
	public static void printEndProperty(int depth, boolean isObject, Field field) {
		setXmlFile(getXmlFile() + printTabs(depth));
		if (isObject) {
			System.out.println("</object>");
			setXmlFile(getXmlFile() + "</object>\n");
		} else {
			System.out.println("</" + field.getName() + ">");
			setXmlFile(getXmlFile() + "</" + field.getName() + ">\n");
		}
	}
	
	private static String printTabs(int count) {
		String tabs = "";
		for (int i = 0; i < count; i++) {
			System.out.print("\t");
			tabs += "\t";
		}
		return tabs;
	}





	public static String getXmlFile() {
		return xmlFile;
	}

	public static void setXmlFile(String xmlFile) {
		PolicyXMLExporter.xmlFile = xmlFile;
	}
	
	public static String getXmlFileLocation() {
		return xmlFileLocation;
	}

	public static void setXmlFileLocation(String xmlFileLocation) {
		PolicyXMLExporter.xmlFileLocation = xmlFileLocation;
	}




}

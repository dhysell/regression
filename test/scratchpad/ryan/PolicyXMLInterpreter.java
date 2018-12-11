package scratchpad.ryan;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.openqa.selenium.WebDriver;

import repository.gw.generate.GeneratePolicy;

public class PolicyXMLInterpreter {

    private WebDriver driver;
    private GeneratePolicy policyFromXml = new GeneratePolicy(driver);
    private String xmlFile = "";
    private Stack<String> objectPathStack = new Stack<String>();
    private Stack<Object> objectStack = new Stack<Object>();

    public void createPolicyFromXML(String xmlFileString) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException, ClassNotFoundException {
        xmlFile = xmlFileString;
        interpretXML();

    }

    public void createPolicyFromXMLFile(String fileName) throws NoSuchFieldException, IOException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException, ClassNotFoundException {
        //TODO
        // Change this
        xmlFile = readFile(fileName, StandardCharsets.UTF_8);
        interpretXML();
    }

    public String getXmlFile() {
        return xmlFile;
    }

    public void setXmlFile(String xmlFile) {
        this.xmlFile = xmlFile;
    }

    public GeneratePolicy getPolicyFromXml() {
        return policyFromXml;
    }

    public void setPolicyFromXml(GeneratePolicy policyFromXml) {
        this.policyFromXml = policyFromXml;
    }

    //Private Utility Methods

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void interpretXML() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException, ClassNotFoundException {
        String[] xmlLines = xmlFile.replace("\t", "").replace("\r", "").split("\n");
        Class<?> clazz = policyFromXml.getClass();
        Field currentField = null;
        objectStack.push(policyFromXml);
//		int testCount = 0;

        for (String line : xmlLines) {
//			testCount++;

            // Uses regex to determine what to do on each line
            // Regex conditions must be ordered from most to least specific

            // TODO Handle object properties

            if (line.matches("<.*>.*</.*>")) { // is a value
                String value = line.replaceAll("</.*>", "").replaceAll("<.*>", "");
                currentField.setAccessible(true);

                //TODO Handle date types

                if (!objectPathStack.isEmpty() && objectPathStack.peek().contains("#listItem#")) {

                    if (objectStack.peek().getClass().isEnum()) { //if enum list item
                        Class<Enum> tempClass = (Class<Enum>) objectStack.pop().getClass();
//						ArrayList<Object> tempList = (ArrayList<Object>)objectStack.peek();
                        objectStack.push(Enum.valueOf(tempClass, value));
                    }

                    // TODO May need to handle other primitive types if included in generate policy

                } else {

                    if (currentField.getType().isEnum()) { //if enum
                        currentField.set(objectStack.peek(), Enum.valueOf((Class<Enum>) currentField.getType(), value));

                    } else if (currentField.getType().isAssignableFrom(String.class)) { //if string
                        currentField.set(objectStack.peek(), value);

                    } else if (currentField.getType().isAssignableFrom(boolean.class) || currentField.getType().isAssignableFrom(Boolean.class)) { //if boolean
                        currentField.set(objectStack.peek(), Boolean.valueOf(value));

                    } else if (currentField.getType().isAssignableFrom(Integer.class) || currentField.getType().isAssignableFrom(int.class)) { //if integer
                        currentField.set(objectStack.peek(), Integer.parseInt(value));

                    } else if (currentField.getType().isAssignableFrom(long.class) || currentField.getType().isAssignableFrom(Long.class)) { //if long
                        currentField.set(objectStack.peek(), Long.parseLong(value));
                    }

                }


            } else if (line.matches("</.*>")) { //is end property
                String lastFieldName = objectPathStack.pop();
                if (line.contains("</object>")) {
                    // TODO pop into the field on the object beneath it on the stack
                    Object obj = objectStack.pop();

                    clazz = objectStack.peek().getClass();
                    Field lastField = clazz.getDeclaredField(lastFieldName);
                    lastField.setAccessible(true);
                    lastField.set(objectStack.peek(), obj);

                } else if (line.contains("</list_object>")) {
                    Object obj;
                    obj = objectStack.pop();
                    clazz = objectStack.peek().getClass();
                    ArrayList<Object> tempList = (ArrayList<Object>) objectStack.peek();
                    tempList.add(obj);
                    objectStack.pop();
                    objectStack.push(tempList);

                }

            } else if (line.matches("<.*/>")) { //is a null property
                String fieldName = line.replace("<", "").replace("/>", "");
                currentField = clazz.getDeclaredField(fieldName);
                currentField.setAccessible(true);

                if (currentField.getType().isAssignableFrom(boolean.class) || currentField.getType().isAssignableFrom(Boolean.class)) {
                    currentField.set(objectStack.peek(), false);
                } else if (currentField.getType().isAssignableFrom(Integer.class) || currentField.getType().isAssignableFrom(int.class)) {
                    currentField.set(objectStack.peek(), 0);
                } else if (currentField.getType().isAssignableFrom(long.class) || currentField.getType().isAssignableFrom(Long.class)) {
                    currentField.set(objectStack.peek(), 0);
                } else if (currentField.getType().isAssignableFrom(List.class)) {
                    currentField.set(objectStack.peek(), new ArrayList<>());
                } else {
                    currentField.set(objectStack.peek(), null);
                }

            } else if (line.matches("<.*>")) { // is a start property
                String fieldName;

                if (line.contains("<list_object class=\"")) {

                    //TODO handle primitives in lists

                    String className = line.replaceAll(".*class=\"class ", "").replaceAll("\">", "");
                    objectPathStack.push("#listItem#");

                    if (className.contains("enums")) {
                        Class<Enum> tempClass = (Class<Enum>) Class.forName(className);
                        Object listObj = Enum.valueOf(tempClass, tempClass.getEnumConstants()[0].toString());
                        objectStack.push(listObj);

                    } else {
                        Object listObj = Class.forName(className).getConstructor().newInstance();
                        objectStack.push(listObj);
                        clazz = objectStack.peek().getClass();
                    }

                } else if (line.contains("<object class=\"")) { //if an object property

                    fieldName = line.replaceAll(".*name=\"", "").replaceAll("\">", "");
                    objectPathStack.push(fieldName);
                    currentField = clazz.getDeclaredField(fieldName);
                    currentField.setAccessible(true);


                    if (line.contains("interface java.util.List")) {
                        objectStack.push(new ArrayList<>());
                    } else {
                        Object obj = currentField.get(objectStack.peek());
                        obj = currentField.getType().getConstructor().newInstance();
                        objectStack.push(obj);
                    }


                    clazz = objectStack.peek().getClass();

                } else { // if member property name

                    fieldName = line.replace("<", "").replace(">", "");
                    objectPathStack.push(fieldName);
                    currentField = clazz.getDeclaredField(fieldName);
                }

            }
        }

        System.out.println("done interpreting!");
        setPolicyFromXml((GeneratePolicy) objectStack.pop());

    }

    private String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

}

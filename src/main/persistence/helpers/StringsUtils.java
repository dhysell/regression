package persistence.helpers;

import org.apache.commons.lang3.StringUtils;

public class StringsUtils {
	
	public static String[] getStringSplitFromFullName(String fullName) {
		String[] splitName = fullName.split(" ");
		switch(fullName) { //special people with special last names and such
			case "Donna D Ambra":
				splitName[0] = "Donna";
				splitName[splitName.length -1] = "D Ambra";
				break;
			case "Ryan Bruce Harrigfeld Jr.":
				splitName[0] = "Ryan";
				splitName[splitName.length -1] = "Harrigfeld";
				break;
			case "Alejandro Salinas Jr.":
				splitName[0] = "Alejandro";
				splitName[splitName.length -1] = "Salinas";
				break;
			case "Jerry L Von Brethorst":
				splitName[0] = "Jerry";
				splitName[splitName.length -1] = "Von Brethorst";
				break;
			case "Philip Paul Zemaitis Jr.":
				splitName[0] = "Philip";
				splitName[splitName.length -1] = "Zemaitis";
				break;
			case "John W Hill IV":
				splitName[0] = "John";
				splitName[splitName.length -1] = "Hill";
				break;
			case "Bianca Von Brethorst":
				splitName[0] = "Bianca";
				splitName[splitName.length -1] = "Von Brethorst";
				break;
		}
		return splitName;
	}
	
	/**
	 * Returns a cleaned-up version of the xpath passed in as a
	 * string value. This escapes characters that, if not escaped,
	 * will cause the xpath to not evaluate.
	 *
	 * @param sqlQueryString
	 *            Messy xpath with characters that need to be escaped.
	 * @return String with the special charaters for an xpath (',") escaped.
	 */
	public static String specialCharacterEscape(String sqlQueryString){
	    String returnString = "";
	    String searchString = sqlQueryString;
	    char[] quoteChars = new char[] { '\'', '"' };
	 
	    int quotePos = StringUtils.indexOfAny(searchString, quoteChars);
	    if (quotePos == -1){
	        returnString = searchString;
	    }else{
	        while (quotePos != -1){
	            String subString = searchString.substring(0, quotePos);
	            returnString += subString;
	            if (searchString.substring(quotePos, (quotePos + 1)).equals("'")){
	                returnString += "'";
	            }else{
	                //must be a double quote
	                returnString += "\"";
	            }
	            searchString = searchString.substring(quotePos + 1, searchString.length());
	            quotePos = StringUtils.indexOfAny(searchString, quoteChars);
	        }
	        returnString += searchString;
	    }
	    return returnString;
	}
}

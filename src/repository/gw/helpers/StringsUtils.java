package repository.gw.helpers;

import com.idfbins.enums.State;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import repository.gw.generate.custom.AddressInfo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringsUtils {
	
	/**
	 * Returns a cleaned-up version of the xpath passed in as a
	 * string value. This escapes characters that, if not escaped,
	 * will cause the xpath to not evaluate.
	 *
	 * @param xPathQueryString Messy xpath with characters that need to be escaped.
	 * @return String with the special charaters for an xpath (',") escaped.
	 * @Warning PLEASE NOTE: This method will require an xpath to not be surrounded by single ticks to work correctly. I.E. - findElements(By.xpath(".//div[contains(text(), " + StringsUtils.xPathSpecialCharacterEscape(companyName) + ")]
	 * instead of findElements(By.xpath(".//div[contains(text(), <b>'</b> " + StringsUtils.xPathSpecialCharacterEscape(companyName) + " <b>'</b> )]
	 */
	public static String xPathSpecialCharacterEscape(String xPathQueryString){
	    String returnString = "";
	    String searchString = xPathQueryString;
	    char[] quoteChars = new char[] { '\'', '"' };
	 
	    int quotePos = StringUtils.indexOfAny(searchString, quoteChars);
	    if (quotePos == -1){
	        returnString = "'" + searchString + "'";
	    }else{
	        returnString = "concat(";
	        while (quotePos != -1){
	            String subString = searchString.substring(0, quotePos);
	            returnString += "'" + subString + "', ";
	            if (searchString.substring(quotePos, quotePos + 1).equals("'")){
	                returnString += "\"'\", ";
	            }else{
	                //must be a double quote
	                returnString += "'\"', ";
	            }
	            searchString = searchString.substring(quotePos + 1, searchString.length());
	         // This was in the code originally included. I cannot figure out what it was meant to do.
	         // searchString = searchString.substring(quotePos + 1, searchString.length() - quotePos - 1);
	            quotePos = StringUtils.indexOfAny(searchString, quoteChars);
	        }
	        returnString += "'" + searchString + "')";
	    }
	    return returnString;
	}
	
	public static String[] cityStateZipParserRegex (String cityStateZipComboToParse){
		String[] returnStringArray = cityStateZipComboToParse.split(",\\s*|\\s(?=\\d)");
		return returnStringArray;
	}
	
	/** This method will take a line of text containing a city, state, zip, and postal code combo and split it into individual parts.
	 * @param cityStateZipComboToParse - String combo of city, state, zip, and postal code to parse out.
	 * @return String[] - Array of strings corresponding to the individual pieces of the split-out combo. The map of items is as follows:
	 * <p> 0 - City </p>
	 * <p> 1 - State </p>
	 * <p> 2 - Zip </p>
	 * <p> 3 - Postal Code </p>
	 */
	public static String[] cityStateZipParser (String cityStateZipComboToParse){
		String[] citySplit = cityStateZipComboToParse.split(",");
		String city = citySplit[0];
		String[] stateZip = citySplit[1].trim().split(" ");
		String state = stateZip[0];
		String zip = stateZip[1];
		String postalCode = null;
		if (zip.contains("-")) {
			String[] zipSplit = zip.split("-");
			zip = zipSplit[0];
			postalCode = zipSplit[1];
		}
		
		String[] returnStringArray = {city, state, zip, postalCode};
		return returnStringArray;
	}
	
	public static AddressInfo addressStringParser(String address) {
		String[] parsedAddress = address.split(", ");
		return new AddressInfo(parsedAddress[0], parsedAddress[1], State.valueOfAbbreviation(parsedAddress[2]),parsedAddress[3]);
	}
	
	public static AddressInfo contactDetailsAddressStringParser(String address) {
		String[] parsedAddress = address.split(", ");
		String[] parsedStreet = parsedAddress[0].split("\n");
		String[] parsedStateZip = parsedAddress[1].split(" ");
		return new AddressInfo(parsedStreet[0], parsedStreet[1], State.valueOfAbbreviation(parsedStateZip[0]),parsedStateZip[1]);
	}
	
	public static String[] firstLastNameParser (String fullNameToParse) {
		String[] firstLastNameArray = fullNameToParse.split(" ");
		return firstLastNameArray;
	}
	
	public static String formatDoubleValueToDecimalPlaces (double number, int numberOfDecimalPlaces) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < numberOfDecimalPlaces; i++) {
			stringBuilder.append("0");
		}
		DecimalFormat df = new DecimalFormat("###,###." + stringBuilder);
		return String.valueOf(df.format(number));
	}
	
	public static String currencyRepresentationOfNumber (double number) {
		String currencyRepresentation = "";
		DecimalFormat df = new DecimalFormat("###,###.00");
		df.format(number);
		String numberAsString = String.valueOf(df.format(number));
		if (number < 0) {
			String[] numberAsStringSplit = numberAsString.split("-");
			currencyRepresentation = "($" + numberAsStringSplit[1] + ")";
		} else if (number == 0) {
			currencyRepresentation = "-";
		} else {
			currencyRepresentation = "$" + numberAsString;
		}
		return currencyRepresentation;
	}
	
	public static String getUserNameFromFirstLastName(String firstLastName){
		String firstName = firstLastNameParser(firstLastName)[0];
		String lastName = firstLastNameParser(firstLastName)[1];
		String userName = firstName.substring(0, 1) + lastName;
		return userName;
	}
	
	public static String formatPolicyNumber(String policyNumber) {
		return policyNumber.substring(0, 2) + "-" + policyNumber.substring(2, 8) + "-" + policyNumber.substring(8, policyNumber.length());
	}
	
	public static String formatTIN(String tin) {
//		if(Configuration.getInstance().getProductVersion().equals("7")) {
			return tin.substring(0, 2) + "-" + tin.substring(2, tin.length());
//		} else {
//			if(tin.length() == 9) {
//				return tin;
//			} else if(tin.length() == 11) {
//				return tin.replace("-", "");
//			} else {
//				return "";
//			}
//		}
	}
	
	public static String formatSSN(String ssn) {
		if(ssn.length() == 9) {
			return ssn.substring(0, 3)+ "-" + ssn.substring(3, 5) + "-" + ssn.substring(5, ssn.length());
		} else if(ssn.length() == 11) {
			return ssn;
		} else {
			return "";
		}
	}
	
	/*Valid SSN Rules
  "(?!\\b(\\d)\\1+-?(\\d)\\1+-?(\\d)\\1+\\b)"+           //Don't allow all matching digits for every field with optional dashes
  "(?!123-45-6789|987-65-4321|219-09-9999|078-05-1120)"+ //Don't allow "123-45-6789", "987-65-4321", "219-09-9999", or "078-05-1120"
  "(?!666|000|9\\d{2})\\d{3}"+                           //Don't allow the SSN to begin with 666, 000, or anything between 900-999
  "-?"+                                                  //An optional dash (separating Area and Group numbers)
  "(?!00)\\d{2}"+                                        //Don't allow the Group Number to be "00"            
  "-?"+                                                  //Another optional dash (separating Group and Serial numbers)
  "(?!0{4})\\d{4}$"                                      //Don't allow last four digits to be "0000"
	 */
	public static String getValidSSN() {
        String ssn = generateRandomNumberDigits(9);
        String ssnRegex = "^(?!219099999|078051120)(?!666|000|9\\d{2})\\d{3}(?!00)\\d{2}(?!0{4})\\d{4}$"; // Above regex simplified to test valid rules.
        Pattern pattern = Pattern.compile(ssnRegex);  // Java pattern matching
        Matcher m = pattern.matcher(ssn); // matcher object
        int counter = 0;
        // If first ssn isn't valid loop until you have a valid one.
        while (!m.matches() && counter < 20) {
            ssn = generateRandomNumberDigits(9); // new ssn
            m = pattern.matcher(ssn); // new match check.
            counter++;
        }
		return ssn;		
	}

	public static String getRandomStringFromArray(String[] array) {
		int rnd = new Random().nextInt(array.length);
		return array[rnd];
	}

	public static String getRandomStringFromList(List<String> array) {
		if (array.size() < 1) {
			Assert.fail("The passed-in array is empty.");
		}
		
		int randomOption = repository.gw.helpers.NumberUtils.generateRandomNumberInt(0, array.size()-1);
		return array.get(randomOption);
	}
	
	public static String getRandomStringFromList(List<String> array, String valueToIgnore) {
		if (array.size() < 1) {
			Assert.fail("The passed-in array is empty.");
		}
		
		if (array.size() == 1 && (array.get(array.size() - 1).equals(valueToIgnore))) {
			Assert.fail("The passed-in array only contains 1 value, and that value is the value that was passed in as the 'valueToIgnore' argument. This method will not work for the current instance.");
		}
		
		int randomOption = repository.gw.helpers.NumberUtils.generateRandomNumberInt(0, array.size()-1);
		String chosenOption = array.get(randomOption);
		while (chosenOption.equalsIgnoreCase(valueToIgnore)) {
			randomOption = NumberUtils.generateRandomNumberInt(0, array.size()-1);
			chosenOption = array.get(randomOption);
		}
		return chosenOption;
	}
	
	/**
	 * Returns a pseudo-random number between min and max, inclusive. The
	 * difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min
	 *            Minimum value
	 * @param max
	 *            Maximum value. Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */
	public static String generateRandomNumber(int min, int max) {
		Random rand = new Random(System.currentTimeMillis());
		int randomNum = rand.nextInt(max - min + 1) + min;

		return String.format("%02d", randomNum);
	}

	/**
	 * Returns a pseudo-random number as many digits long as you want.
	 *
	 * @param length Length (Number of Digits) of random number generated.
	 * @return String random number x digits long.
	 * @see java.util.Random#nextInt(int)
	 */
	public static String generateRandomNumberDigits(int length) {
		Random rand = new Random();

		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int digit = rand.nextInt(10);
			buffer.append(digit);
		}
		return buffer.toString();
	}
	
	/**
	 * Returns a pseudo-random number as many digits long as you want.
	 *
	 * @param minLength Minimum length (Number of Digits) of random number generated.
	 * @param maxLength Maximum length (Number of Digits) of random number generated.
	 * @return String random number x digits long.
	 * @see java.util.Random#nextInt(int)
	 */
	public static String generateRandomNumberDigits(int minLength, int maxLength) {
		int digit;
		Random rand = new Random();
		int length = rand.nextInt(maxLength - minLength + 1);
		length += minLength;
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			digit = rand.nextInt(10);
			while (i == 0 && digit == 0) {
				digit = rand.nextInt(10);
			}
			buffer.append(digit);
		}
		return buffer.toString();
	}
	
	public static String removeNewLineFromString(String badString) {
		String[] split = badString.replace("\n", " ").split(" ");
		String returnString = "";
		for(String word : split) {
			if(!word.isEmpty()) {
				returnString = returnString + word.trim() + " ";
			}
		}
		return returnString.trim();
	}

	public static boolean containsNoCase(String stringToSearch, String stringToFind) {
		return stringToSearch.toLowerCase().contains(stringToFind.toLowerCase());
	}
	
	public static ArrayList<String> lastFirstMiddleInitialNameParser(String name){
		System.out.println("The name to Parse is " +name);
		ArrayList<String> returnNameArray = new ArrayList<String>();
		String[] nameSplit = name.split(", ");
		String[] firstNameMI = nameSplit[1].split(" ");
		returnNameArray.add(firstNameMI[0]);
		if(firstNameMI.length > 1) {
			returnNameArray.add(firstNameMI[1]);
		}
		returnNameArray.add(nameSplit[0]);
		return returnNameArray;		
	}
	
	public static ArrayList<String> parseAgentInfo(String agentInfo){
		ArrayList<String> agentName = new ArrayList<String>();
		String[] parseString = agentInfo.split(" ");
		for(String agentStuff : parseString){
			if(agentStuff.matches("[a-zA-Z]+")){
				agentName.add(agentStuff);
			}
		}
		return agentName;
	}
	
	public static String capitalizeName(String name) {
		String nameToLower=name.toLowerCase();
		String firstChar = name.substring(0,1);
		firstChar.toUpperCase();
		String nameToReturn=firstChar + nameToLower.substring(1);		
		return nameToReturn;
	}
	
	
	public static String capitalizeAllWords(String string) {
		String toReturn = "";
		String[] foo = string.split(" ");
		for(String boo : foo) {
			toReturn += " " + capitalizeName(boo);
		}
		
		return toReturn.trim();
	}
	
	
	
	
	public static String getAccountNumberFromTransactionNumber(String transactionNumber) {
		return transactionNumber.substring(2, 8);
	}
	
	public String getUniqueName(String company) {
		String dateString = DateUtils.dateFormatAsString("yyMMddHHmmss", new Date());
		String companyNameToCreate = null;
		companyNameToCreate = company + "-" + dateString;
		if (companyNameToCreate.length() > 30) {
			companyNameToCreate = company.substring(0, 16) + "-" + dateString;
		}
		return companyNameToCreate;
	}

	public String[] getUniqueName(String firstName, String middleName, String lastName) {
		String dateString = DateUtils.dateFormatAsString("yyMMddHHmmss", new Date());
		String lastNameToCreate = null;
		String[] fullName = new String[3];

		lastNameToCreate = lastName + "-" + dateString;
		String tempFullName = null;
		if (middleName != null && middleName.length() > 0) {
			//don't include spaces when calculating the temp full name and lengths.
			tempFullName = firstName + middleName + lastNameToCreate;
        } else {
        	middleName = "-";
        	//don't include spaces when calculating the temp full name and lengths.
        	tempFullName = firstName + lastNameToCreate;
        }
		if (tempFullName.length() > 29) {
			int numberOfCharactersToRemove = tempFullName.length() - 29;
			if (numberOfCharactersToRemove > lastName.length()) {
				lastNameToCreate = lastName.substring(0, (lastName.length() - (numberOfCharactersToRemove / 2))) + "-" + dateString;
				firstName = lastName.substring(0, (firstName.length() - (numberOfCharactersToRemove / 2)));
			} else {
				lastNameToCreate = lastName.substring(0, (lastName.length() - numberOfCharactersToRemove)) + "-" + dateString;
			}
		}
		fullName[0] = firstName;
		fullName[1] = middleName;
		fullName[2] = lastNameToCreate;

		return fullName;
	}
}

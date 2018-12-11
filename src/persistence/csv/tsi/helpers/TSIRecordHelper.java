package persistence.csv.tsi.helpers;

import com.idfbins.enums.State;
import persistence.csv.tsi.entities.TSIRecord;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TSIRecordHelper {

	private static Date convertStringtoDate(String dateString, String dateFormat) throws ParseException {
		Date date = new Date();
		try {
			DateFormat format = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
			date = format.parse(dateString);
		} catch (Exception e) {
			throw new ParseException(("Parse Exception: The String could not be parsed to convert to a date."), 0);
		}
		return date;
	}
	
	private static ArrayList<persistence.csv.tsi.entities.TSIRecord> getTSIRecordGuts(ArrayList<String> csvTextArrayList) throws IOException, NumberFormatException, ParseException {
		ArrayList<persistence.csv.tsi.entities.TSIRecord> listOfRecords = new ArrayList<persistence.csv.tsi.entities.TSIRecord>();
		for (String csvText : csvTextArrayList) {
			String[] stringSplit = csvText.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
			Date dateOfDebt = null;
			Date dateLastPay = null;
			Date birthDate = null;
			
			try {
				dateOfDebt = convertStringtoDate(stringSplit[9], "yyyy-MM-dd");
			} catch (ParseException e) {
				System.out.println("The dateOfDebt field was un-parsable. The String that was attempted to parse was '" + stringSplit[9] + "'. dateOfDebt value will be set to null.");
			}
			
			try {
				dateLastPay = convertStringtoDate(stringSplit[10], "yyyy-MM-dd");
			} catch (ParseException e) {
				System.out.println("The dateLastPay field was un-parsable. The String that was attempted to parse was '" + stringSplit[10] + "'. dateLastPay value will be set to null.");
			}
			
			try {
				birthDate = convertStringtoDate(stringSplit[15], "yyyy-MM-dd");
			} catch (ParseException e) {
				System.out.println("The birthDate field was un-parsable. The String that was attempted to parse was '" + stringSplit[15] + "'. birthDate value will be set to null.");
			}
			
			persistence.csv.tsi.entities.TSIRecord tempTSIRecord = new persistence.csv.tsi.entities.TSIRecord(stringSplit[0], stringSplit[1], stringSplit[2], stringSplit[3], stringSplit[4], stringSplit[5], State.valueOfAbbreviation(stringSplit[6]), stringSplit[7], stringSplit[8], dateOfDebt, dateLastPay, Double.valueOf(stringSplit[11]), Double.valueOf(stringSplit[12]), stringSplit[13], stringSplit[14], birthDate, stringSplit[16], stringSplit[17], stringSplit[18], stringSplit[19]);
			listOfRecords.add(tempTSIRecord);
		}
		return listOfRecords;
	}
	
	public static ArrayList<persistence.csv.tsi.entities.TSIRecord> getTSIRecords(ArrayList<String> recordsArrayList) throws IOException, NumberFormatException, ParseException {
		return getTSIRecordGuts(recordsArrayList);
	}
	
	public static ArrayList<TSIRecord> getTSIRecords(File recordFile, boolean excludeFirstLine) throws FileNotFoundException, IOException, NumberFormatException, ParseException {
		ArrayList<String> fileLineArrayList = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(recordFile))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	fileLineArrayList.add(line);
		    }
		}
		if (excludeFirstLine) {
			fileLineArrayList.remove(0);
		}
		return getTSIRecordGuts(fileLineArrayList);
	}
}

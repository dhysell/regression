package persistence.csv.membershipDuesSpreading.helpers;

import persistence.csv.membershipDuesSpreading.entities.MembershipDuesSpreadingRecord;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;

public class MembershipDuesSpreadingRecordHelper {

	/*private static Date convertStringtoDate(String dateString, String dateFormat) throws ParseException {
		Date date = new Date();
		try {
			DateFormat format = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
			date = format.parse(dateString);
		} catch (Exception e) {
			throw new ParseException(("Parse Exception: The String could not be parsed to convert to a date."), 0);
		}
		return date;
	}*/
	
	private static ArrayList<MembershipDuesSpreadingRecord> getMembershipDuesSpreadingRecordGuts(ArrayList<String> csvTextArrayList) throws IOException, NumberFormatException, ParseException {
		ArrayList<MembershipDuesSpreadingRecord> listOfRecords = new ArrayList<MembershipDuesSpreadingRecord>();
		for (String csvText : csvTextArrayList) {
			String[] stringSplit = csvText.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
			
			MembershipDuesSpreadingRecord tempMembershipDuesSpreadingRecord = new MembershipDuesSpreadingRecord(stringSplit[0], stringSplit[1], stringSplit[2], stringSplit[3], stringSplit[4], stringSplit[5]);
			listOfRecords.add(tempMembershipDuesSpreadingRecord);
		}
		return listOfRecords;
	}
	
	public static ArrayList<MembershipDuesSpreadingRecord> getMembershipDuesSpreadingRecords(ArrayList<String> recordsArrayList) throws IOException, NumberFormatException, ParseException {
		return getMembershipDuesSpreadingRecordGuts(recordsArrayList);
	}
	
	public static ArrayList<MembershipDuesSpreadingRecord> getMembershipDuesSpreadingRecords(File recordFile, boolean excludeFirstLine) throws FileNotFoundException, IOException, NumberFormatException, ParseException {
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
		return getMembershipDuesSpreadingRecordGuts(fileLineArrayList);
	}
	
	public static void writeMembershipDuesSpreadingRecordsToFile(ArrayList<MembershipDuesSpreadingRecord> membershipDuesSpreadingRecordsArrayList, String csvFilePath) {
		new File(csvFilePath.substring(0,csvFilePath.lastIndexOf("\\"))).mkdirs();
		File oldCSVOutputFile = new File(csvFilePath);
		oldCSVOutputFile.delete();
		File csvOutputFile = new File(csvFilePath);
		csvFileOutputWriter(csvOutputFile, "JobNumber,Status,CreateTime,Quote Time,Agent,Premium");
		for (MembershipDuesSpreadingRecord membershipDuesSpreadingRecord : membershipDuesSpreadingRecordsArrayList) {
			csvFileOutputWriter(csvOutputFile, (membershipDuesSpreadingRecord.getJobNumber() + "," + membershipDuesSpreadingRecord.getJobStatus() + "," + membershipDuesSpreadingRecord.getCreateTime() + "," + membershipDuesSpreadingRecord.getQuoteTime() + "," + membershipDuesSpreadingRecord.getAgentName() + "," + membershipDuesSpreadingRecord.getPolicyPremium()));
		}
	}
	
	private static void csvFileOutputWriter(File csvOutputFile, String lineToWrite) {
		BufferedWriter output = null;
        try {
        	FileWriter fileOutput = new FileWriter(csvOutputFile, true);
            output = new BufferedWriter(fileOutput);
            output.append(lineToWrite);
            output.append(System.lineSeparator());
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
          if ( output != null ) {
        	try {
        		output.close();
        	}catch (IOException e2) {
        		e2.printStackTrace();
        	}
          }
        }
	}
}

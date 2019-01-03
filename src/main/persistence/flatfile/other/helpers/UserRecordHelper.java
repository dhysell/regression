package persistence.flatfile.other.helpers;

import flapjack.io.LineRecordReader;
import flapjack.model.ObjectMapping;
import flapjack.model.ObjectMappingStore;
import flapjack.model.SameRecordFactoryResolver;
import flapjack.parser.ParseResult;
import flapjack.parser.RecordParserImpl;
import flapjack.parser.SameRecordLayoutResolver;
import persistence.flatfile.other.entities.UserRecord;
import persistence.flatfile.other.entities.UserRecordFactory;
import persistence.flatfile.other.entities.UserRecordLayout;

import java.io.*;
import java.util.ArrayList;

public class UserRecordHelper {
	
	private static ArrayList<UserRecord> getUserRecordsGuts(String records) throws IOException {

		ObjectMapping userMapping = new ObjectMapping(UserRecord.class);
		userMapping.add("First Name", "firstName");
		userMapping.add("Last Name", "lastName");
		userMapping.add("Username", "username");

		ObjectMappingStore objectMappingStore = new ObjectMappingStore();
		objectMappingStore.add(userMapping);

		RecordParserImpl recordParser = new RecordParserImpl();
		recordParser.setRecordLayoutResolver(new SameRecordLayoutResolver(UserRecordLayout.class));
		recordParser.setRecordFactoryResolver(new SameRecordFactoryResolver(UserRecordFactory.class));
		recordParser.setObjectMappingStore(objectMappingStore);
		recordParser.setIgnoreUnmappedFields(true);

		LineRecordReader recordReader = new LineRecordReader(new ByteArrayInputStream(records.getBytes()));
		ParseResult result = recordParser.parse(recordReader);

		ArrayList<UserRecord> listOfRecords = new ArrayList<UserRecord>();
		UserRecord tempRecord = new UserRecord();
		
		for(Object preUser : result.getRecords()) {
			tempRecord = (UserRecord) preUser;
			tempRecord.setFirstName(tempRecord.getFirstName().trim());
			tempRecord.setLastName(tempRecord.getLastName().trim());
			tempRecord.setUsername(tempRecord.getUsername().trim());
	        
			listOfRecords.add(tempRecord);
		}
		

		return listOfRecords;
		
	}
	
	public static ArrayList<UserRecord> getUserRecords(String lineDelimitedRecords) throws IOException {
		
		return getUserRecordsGuts(lineDelimitedRecords);
		
	}
	
	public static ArrayList<UserRecord> getUserRecords(File recordFile) throws IOException {
		
		String allLines = "";
		
		try (BufferedReader br = new BufferedReader(new FileReader(recordFile))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       allLines = allLines + line + "\n";
		    }
		}
		
		return getUserRecordsGuts(allLines);
		
	}

	public static boolean listContainsUserRecord(ArrayList<UserRecord> listOfUserRecords, UserRecord recordToSearchFor) {
		boolean found = false;
		for (UserRecord toCompare : listOfUserRecords) {
			found = toCompare.equals(recordToSearchFor);
			if (found) {
				break;
			}
		}
		
		return found;
	}
}

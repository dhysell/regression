package persistence.flatfile.mbre.helpers;

import flapjack.io.LineRecordReader;
import flapjack.model.ObjectMapping;
import flapjack.model.ObjectMappingStore;
import flapjack.model.SameRecordFactoryResolver;
import flapjack.parser.ParseResult;
import flapjack.parser.RecordParserImpl;
import flapjack.parser.SameRecordLayoutResolver;
import persistence.flatfile.mbre.entities.*;

import java.io.*;
import java.util.ArrayList;

public class MBRERecordHelper {
	
	// CC Quarterly File
	private static ArrayList<CCQuarterlyRecord> getCCQuarterlyRecordsGuts(String records) throws IOException {

		ObjectMapping mapping = new ObjectMapping(CCQuarterlyRecord.class);
		mapping.add("Policy Number", "policyNumber");
		mapping.add("Claim Number", "claimNumber");
		mapping.add("Date of Loss", "dateOfLoss");
		mapping.add("Named Insured", "namedInsured");
		mapping.add("Total Loss Adjustment Paid", "totalLossAdjustmentPaid");
		mapping.add("Total Loss Payment", "totalLossPayment");

		ObjectMappingStore objectMappingStore = new ObjectMappingStore();
		objectMappingStore.add(mapping);

		RecordParserImpl recordParser = new RecordParserImpl();
		recordParser.setRecordLayoutResolver(new SameRecordLayoutResolver(CCQuarterlyRecordLayout.class));
		recordParser.setRecordFactoryResolver(new SameRecordFactoryResolver(CCQuarterlyRecordFactory.class));
		recordParser.setObjectMappingStore(objectMappingStore);
		recordParser.setIgnoreUnmappedFields(true);

		LineRecordReader recordReader = new LineRecordReader(new ByteArrayInputStream(records.getBytes()));
		ParseResult result = recordParser.parse(recordReader);

		ArrayList<CCQuarterlyRecord> listOfRecords = new ArrayList<CCQuarterlyRecord>();
		CCQuarterlyRecord tempRecord = new CCQuarterlyRecord();
		
		for(Object preUser : result.getRecords()) {
			tempRecord = (CCQuarterlyRecord) preUser;
			tempRecord.setPolicyNumber(tempRecord.getPolicyNumber().trim());
			tempRecord.setClaimNumber(tempRecord.getClaimNumber().trim());
			tempRecord.setDateOfLoss(tempRecord.getDateOfLoss().trim());
			tempRecord.setNamedInsured(tempRecord.getNamedInsured().trim());
			tempRecord.setTotalLossAdjustmentPaid(tempRecord.getTotalLossAdjustmentPaid().trim());
			tempRecord.setTotalLossPayment(tempRecord.getTotalLossPayment().trim());
	        
			listOfRecords.add(tempRecord);
		}

		return listOfRecords;
		
	}
	
	//TODO: this is a pointless proxy method. If it changed the parameters before passing them to the private method it would be useful. 
	//Consider removing this method and renaming the private method it calls.
	public static ArrayList<CCQuarterlyRecord> getCCQuarterlyRecords(String lineDelimitedRecords) throws IOException {
		
		return getCCQuarterlyRecordsGuts(lineDelimitedRecords);
		
	}
	
	public static ArrayList<CCQuarterlyRecord> getCCQuarterlyRecords(File recordFile) throws FileNotFoundException, IOException {
		
		String allLines = "";
		
		try (BufferedReader br = new BufferedReader(new FileReader(recordFile))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       allLines = allLines + line + "\n";
		    }
		}
		
		return getCCQuarterlyRecordsGuts(allLines);
		
	}

	//TODO: remove once no instances on it are found.
	/**
	 * @deprecated use listOfRecords.contains(recordToSearchFor) instead  
	 */
	@Deprecated
	public static boolean listContainsCCQuarterlyRecord(ArrayList<CCQuarterlyRecord> listOfRecords, CCQuarterlyRecord recordToSearchFor) {
		
		return listOfRecords.contains(recordToSearchFor);
	}
	
	// PC Quarterly File
	private static ArrayList<PCQuarterlyRecord> getPCQuarterlyRecordsGuts(String records) throws IOException {
		ObjectMapping mapping = new ObjectMapping(PCQuarterlyRecord.class);
		mapping.add("Named Insured", "namedInsured");
		mapping.add("Policy Number", "policyNumber");
		mapping.add("Policy Effective Date", "policyEffectiveDate");
		mapping.add("Policy Expiration Date", "policyExpirationDate");
		mapping.add("Total Annual Premium", "totalAnnualPremium");

		ObjectMappingStore objectMappingStore = new ObjectMappingStore();
		objectMappingStore.add(mapping);

		RecordParserImpl recordParser = new RecordParserImpl();
		recordParser.setRecordLayoutResolver(new SameRecordLayoutResolver(PCQuarterlyRecordLayout.class));
		recordParser.setRecordFactoryResolver(new SameRecordFactoryResolver(PCQuarterlyRecordFactory.class));
		recordParser.setObjectMappingStore(objectMappingStore);
		recordParser.setIgnoreUnmappedFields(true);

		LineRecordReader recordReader = new LineRecordReader(new ByteArrayInputStream(records.getBytes()));
		ParseResult result = recordParser.parse(recordReader);

		ArrayList<PCQuarterlyRecord> listOfRecords = new ArrayList<PCQuarterlyRecord>();
		PCQuarterlyRecord tempRecord = new PCQuarterlyRecord();
		
		for(Object preUser : result.getRecords()) {
			tempRecord = (PCQuarterlyRecord) preUser;
			tempRecord.setNamedInsured(tempRecord.getNamedInsured().trim());
			tempRecord.setPolicyNumber(tempRecord.getPolicyNumber().trim());
			tempRecord.setPolicyEffectiveDate(tempRecord.getPolicyEffectiveDate().trim());
			tempRecord.setPolicyExpirationDate(tempRecord.getPolicyExpirationDate().trim());
			tempRecord.setTotalAnnualPremium(tempRecord.getTotalAnnualPremium().trim());
			
			listOfRecords.add(tempRecord);
		}

		return listOfRecords;
		
	}
	
	//TODO: this is a pointless proxy method. If it changed the parameters before passing them to the private method it would be useful. 
	//Consider removing this method and renaming the private method it calls.
	public static ArrayList<PCQuarterlyRecord> getPCQuarterlyRecords(String lineDelimitedRecords) throws IOException {
		
		return getPCQuarterlyRecordsGuts(lineDelimitedRecords);
		
	}
	
	public static ArrayList<PCQuarterlyRecord> getPCQuarterlyRecords(File recordFile) throws IOException {
		
		String allLines = "";
		
		try (BufferedReader br = new BufferedReader(new FileReader(recordFile))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       allLines = allLines + line + "\n";
		    }
		}
		
		return getPCQuarterlyRecordsGuts(allLines);
		
	}

	//TODO: remove once no instances on it are found.
	/**
	 * @deprecated use listOfRecords.contains(recordToSearchFor) instead  
	 */
	@Deprecated
	public static boolean listContainsPCQuarterlyRecord(ArrayList<PCQuarterlyRecord> listOfRecords, PCQuarterlyRecord recordToSearchFor) {
		
		return listOfRecords.contains(recordToSearchFor);
	}
	
	// PC Monthly Policy File
	private static ArrayList<PCMonthlyPolicyRecord> getPCMonthlyPolicyRecordsGuts(String records) throws IOException {

		ObjectMapping mapping = new ObjectMapping(PCMonthlyPolicyRecord.class);
		mapping.add("Company Number", "companyNumber");
		mapping.add("Named Insured Line 1", "namedInsuredLine1");
		mapping.add("Named Insured Line 2", "namedInsuredLine2");
		mapping.add("Principal Address Line 1", "principalAddressLine1");
		mapping.add("Principal Address Line 2", "principalAddressLine2");
		mapping.add("Principal City", "principalCity");
		mapping.add("Principal State", "principalState");
		mapping.add("Principal County", "principalCounty");
		mapping.add("Principal Zip Code", "principalZipCode");
		mapping.add("Current Policy Number", "currentPolicyNumber");
		mapping.add("Previous Policy Number", "previousPolicyNumber");
		mapping.add("Policy Effective Date", "policyEffectiveDate");
		mapping.add("Policy Expiration Date", "policyExpirationDate");
		mapping.add("Accounting Date", "accountingDate");
		mapping.add("Transaction Effective Date", "transactionEffectiveDate");
		mapping.add("Equipment Breakdown Premium", "equipmentBreakdownPremium");
		mapping.add("Package/Property Premium", "packagePropertyPremium");
		mapping.add("Deductible", "deductible");
		mapping.add("Transaction Code", "transactionCode");
		mapping.add("Company Branch Code", "companyBranchCode");
		mapping.add("Policy Type", "policyType");

		ObjectMappingStore objectMappingStore = new ObjectMappingStore();
		objectMappingStore.add(mapping);

		RecordParserImpl recordParser = new RecordParserImpl();
		recordParser.setRecordLayoutResolver(new SameRecordLayoutResolver(PCMonthlyPolicyRecordLayout.class));
		recordParser.setRecordFactoryResolver(new SameRecordFactoryResolver(PCMonthlyPolicyRecordFactory.class));
		recordParser.setObjectMappingStore(objectMappingStore);
		recordParser.setIgnoreUnmappedFields(true);

		LineRecordReader recordReader = new LineRecordReader(new ByteArrayInputStream(records.getBytes()));
		ParseResult result = recordParser.parse(recordReader);

		ArrayList<PCMonthlyPolicyRecord> listOfRecords = new ArrayList<PCMonthlyPolicyRecord>();
		PCMonthlyPolicyRecord tempRecord = new PCMonthlyPolicyRecord();
		
		for(Object preUser : result.getRecords()) {
			tempRecord = (PCMonthlyPolicyRecord) preUser;
			tempRecord.setCompanyNumber(tempRecord.getCompanyNumber().trim());
			tempRecord.setNamedInsuredLine1(tempRecord.getNamedInsuredLine1().trim());
			tempRecord.setNamedInsuredLine2(tempRecord.getNamedInsuredLine2().trim());
			tempRecord.setPrincipalAddressLine1(tempRecord.getPrincipalAddressLine1().trim());
			tempRecord.setPrincipalAddressLine2(tempRecord.getPrincipalAddressLine2().trim());
			tempRecord.setPrincipalCity(tempRecord.getPrincipalCity().trim());
			tempRecord.setPrincipalState(tempRecord.getPrincipalState().trim());
			tempRecord.setPrincipalCounty(tempRecord.getPrincipalCounty().trim());
			tempRecord.setPrincipalZipCode(tempRecord.getPrincipalZipCode().trim());
			tempRecord.setCurrentPolicyNumber(tempRecord.getCurrentPolicyNumber().trim());
			tempRecord.setPreviousPolicyNumber(tempRecord.getPreviousPolicyNumber().trim());
			tempRecord.setPolicyEffectiveDate(tempRecord.getPolicyEffectiveDate().trim());
			tempRecord.setPolicyExpirationDate(tempRecord.getPolicyExpirationDate().trim());
			tempRecord.setAccountingDate(tempRecord.getAccountingDate().trim());
			tempRecord.setTransactionEffectiveDate(tempRecord.getTransactionEffectiveDate().trim());
			tempRecord.setEquipmentBreakdownPremium(tempRecord.getEquipmentBreakdownPremium().trim());
			tempRecord.setPackagePropertyPremium(tempRecord.getPackagePropertyPremium().trim());
			tempRecord.setDeductible(tempRecord.getDeductible().trim());
			tempRecord.setTransactionCode(tempRecord.getTransactionCode().trim());
			tempRecord.setCompanyBranchCode(tempRecord.getCompanyBranchCode().trim());
			tempRecord.setPolicyType(tempRecord.getPolicyType().trim());
			
			listOfRecords.add(tempRecord);
		}

		return listOfRecords;
		
	}
	
	//TODO: this is a pointless proxy method. If it changed the parameters before passing them to the private method it would be useful. 
	//Consider removing this method and renaming the private method it calls.
	public static ArrayList<PCMonthlyPolicyRecord> getPCMonthlyPolicyRecords(String lineDelimitedRecords) throws IOException {
		
		return getPCMonthlyPolicyRecordsGuts(lineDelimitedRecords);
		
	}
	
	public static ArrayList<PCMonthlyPolicyRecord> getPCMonthlyPolicyRecords(File recordFile) throws FileNotFoundException, IOException {
		
		String allLines = "";
		
		try (BufferedReader br = new BufferedReader(new FileReader(recordFile))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       allLines = allLines + line + "\n";
		    }
		}
		
		return getPCMonthlyPolicyRecordsGuts(allLines);
		
	}

	//TODO: remove once no instances on it are found.
	/**
	 * @deprecated use listOfRecords.contains(recordToSearchFor) instead  
	 */
	@Deprecated
	public static boolean listContainsPCMonthlyPolicyRecord(ArrayList<PCMonthlyPolicyRecord> listOfRecords, PCMonthlyPolicyRecord recordToSearchFor) {
		
		return listOfRecords.contains(recordToSearchFor);
	}
	
	// PC Monthly Location File
	private static ArrayList<PCMonthlyLocationRecord> getPCMonthlyLocationRecordsGuts(String records) throws IOException {

		ObjectMapping mapping = new ObjectMapping(PCMonthlyLocationRecord.class);
		mapping.add("Current Policy Number", "currentPolicyNumber");
		mapping.add("Transaction Code", "transactionCode");
		mapping.add("Location Sequence Number", "locationSequenceNumber");
		mapping.add("Location Address 1", "locationAddress1");
		mapping.add("Location Address 2", "locationAddress2");
		mapping.add("Location City", "locationCity");
		mapping.add("Location State", "locationState");
		mapping.add("Location County", "locationCounty");
		mapping.add("Location Zip Code", "locationZipCode");
		mapping.add("Location Occupancy Code", "locationOccupancyCode");
		mapping.add("Location Building Value", "locationBuildingValue");
		mapping.add("Location Contents Value", "locationContentsValue");
		mapping.add("Location Business Income Value", "locationBusinessIncomeValue");
		mapping.add("Location Contact - Name", "locationContactName");
		mapping.add("Location Contact - Phone Number", "locationContactPhoneNumber");
		mapping.add("Tenant / Owner", "tenantOwner");

		ObjectMappingStore objectMappingStore = new ObjectMappingStore();
		objectMappingStore.add(mapping);

		RecordParserImpl recordParser = new RecordParserImpl();
		recordParser.setRecordLayoutResolver(new SameRecordLayoutResolver(PCMonthlyLocationRecordLayout.class));
		recordParser.setRecordFactoryResolver(new SameRecordFactoryResolver(PCMonthlyLocationRecordFactory.class));
		recordParser.setObjectMappingStore(objectMappingStore);
		recordParser.setIgnoreUnmappedFields(true);

		LineRecordReader recordReader = new LineRecordReader(new ByteArrayInputStream(records.getBytes()));
		ParseResult result = recordParser.parse(recordReader);

		ArrayList<PCMonthlyLocationRecord> listOfRecords = new ArrayList<PCMonthlyLocationRecord>();
		PCMonthlyLocationRecord tempRecord = new PCMonthlyLocationRecord();
		
		for(Object preUser : result.getRecords()) {
			tempRecord = (PCMonthlyLocationRecord) preUser;
			tempRecord.setCurrentPolicyNumber(tempRecord.getCurrentPolicyNumber().trim());
			tempRecord.setTransactionCode(tempRecord.getTransactionCode().trim());
			tempRecord.setLocationSequenceNumber(tempRecord.getLocationSequenceNumber().trim());
			tempRecord.setLocationAddress1(tempRecord.getLocationAddress1().trim());
			tempRecord.setLocationAddress2(tempRecord.getLocationAddress2().trim());
			tempRecord.setLocationCity(tempRecord.getLocationCity().trim());
			tempRecord.setLocationState(tempRecord.getLocationState().trim());
			tempRecord.setLocationCounty(tempRecord.getLocationCounty().trim());
			tempRecord.setLocationZipCode(tempRecord.getLocationZipCode().trim());
			tempRecord.setLocationOccupancyCode(tempRecord.getLocationOccupancyCode().trim());
			tempRecord.setLocationBuildingValue(tempRecord.getLocationBuildingValue().trim());
			tempRecord.setLocationContentsValue(tempRecord.getLocationContentsValue().trim());
			tempRecord.setLocationBusinessIncomeValue(tempRecord.getLocationBusinessIncomeValue().trim());
			tempRecord.setLocationContactName(tempRecord.getLocationContactName().trim());
			tempRecord.setLocationContactPhoneNumber(tempRecord.getLocationContactPhoneNumber().trim());
			tempRecord.setTenantOwner(tempRecord.getTenantOwner().trim());
			
			listOfRecords.add(tempRecord);
		}

		return listOfRecords;
		
	}
	
	//TODO: this is a pointless proxy method. If it changed the parameters before passing them to the private method it would be useful. 
	//Consider removing this method and renaming the private method it calls.
	public static ArrayList<PCMonthlyLocationRecord> getPCMonthlyLocationRecords(String lineDelimitedRecords) throws Exception {
		
		return getPCMonthlyLocationRecordsGuts(lineDelimitedRecords);
		
	}
	
	public static ArrayList<PCMonthlyLocationRecord> getPCMonthlyLocationRecords(File recordFile) throws Exception {
		
		String allLines = "";
		
		try (BufferedReader br = new BufferedReader(new FileReader(recordFile))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       allLines = allLines + line + "\n";
		    }
		}
		
		return getPCMonthlyLocationRecordsGuts(allLines);
		
	}

	
	//TODO: remove once no instances on it are found.
	/**
	 * @deprecated use listOfRecords.contains(recordToSearchFor) instead  
	 */
	@Deprecated
	public static boolean listContainsPCMonthlyLocationRecord(ArrayList<PCMonthlyLocationRecord> listOfRecords, PCMonthlyLocationRecord recordToSearchFor) {
		
		return listOfRecords.contains(recordToSearchFor);
	}
}

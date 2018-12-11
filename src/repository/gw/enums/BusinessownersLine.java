package repository.gw.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BusinessownersLine {

	public enum SmallBusinessType {
		Apartments("Apartments (including Residential Condominium Associations)"), 
		Condominium("Condominium Commercial Unit-owners"), 
		ConvenienceFoodStores("Convenience Food Stores, Grocery and Supermarkets"), 
		Motels("Motels"), 
		Offices("Offices (Including office & Commercial Condominium Associations)"), 
		ProcessingServiceRisks("Processing & Service Risks"), 
		Restaurants("Restaurants"), 
		SelfStorageFacilities("Self-storage Facilities"), 
		StoresRetail("Stores-retail"), 
		WholesaleRisks("Wholesale Risks");
		
		String value;
		
		private SmallBusinessType(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		private static final List<SmallBusinessType> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		public static SmallBusinessType random()  {
			return VALUES.get(new Random().nextInt(VALUES.size()));
		}
	}
	
	public enum PropertyGlassDeductible {
		HalfK("500"), 
		OneK("1,000"), 
		TwoAndHalfK("2,500"), 
		FiveK("5,000"), 
		SevenAndHalfK("7,500"), 
		TenK("10,000");
		
		String value;
		
		private PropertyGlassDeductible(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		private static final List<PropertyGlassDeductible> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		public static PropertyGlassDeductible random()  {
			return VALUES.get(new Random().nextInt(VALUES.size()));
		}
	}
	
	public enum LiabilityLimits {
		Three00_600_600("300/600/600"), 
		Five00_1000_1000("500/1000/1000"), 
		One000_2000_2000("1000/2000/2000"),
		Two000_4000_4000("2000/4000/4000");
		
		String value;
		
		private LiabilityLimits(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		private static final List<LiabilityLimits> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		public static LiabilityLimits random()  {
			return VALUES.get(new Random().nextInt(VALUES.size()));
		}
	}
	
	//updated PDDeductibleAmount according to requirements 1.4.2.IC.11 page 83 of the power point doc.
	public enum PDDeductibleAmount {
		PD500("500"), 
		PD1000("1,000"), 
		PD2500("2,500"),
		PD5000("5,000"),
		PD7500("7,500"),
		PD10000("10,000");
		
		String value;
		
		private PDDeductibleAmount(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		private static final List<PDDeductibleAmount> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		public static PDDeductibleAmount random()  {
			return VALUES.get(new Random().nextInt(VALUES.size()));
		}
	}
	
	public enum PremisesMedicalExpense {
		Exp5000("5,000"), 
		Exp10000("10,000");
		
		String value;
		
		private PremisesMedicalExpense(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		private static final List<PremisesMedicalExpense> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		public static PremisesMedicalExpense random()  {
			return VALUES.get(new Random().nextInt(VALUES.size()));
		}
	}
	
	public enum EmployeeDishonestyAuditsConductedFrequency {
		Daily("Daily"), 
		Weekly("Weekly"),
		Monthly("Monthly"),
		Quarterly("Quarterly"),
		Semi_Annually("Semi-Annually"),
		Annually("Annually");
		
		String value;
		
		private EmployeeDishonestyAuditsConductedFrequency(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		private static final List<EmployeeDishonestyAuditsConductedFrequency> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		public static EmployeeDishonestyAuditsConductedFrequency random()  {
			return VALUES.get(new Random().nextInt(VALUES.size()));
		}
	}
	
	public enum EmpDishonestyLimit {
		Dishonest5000("5,000"), 
		Dishonest10000("10,000"),
		Dishonest25000("25,000"),
		Dishonest50000("50,000"),
		Dishonest100000("100,000");
		
		String value;
		
		private EmpDishonestyLimit(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		private static final List<EmpDishonestyLimit> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		public static EmpDishonestyLimit random()  {
			return VALUES.get(new Random().nextInt(VALUES.size()));
		}
	}
	
	public enum EmployeeDishonestyAuditsPerformedBy {
		CPA("CPA"), 
		Private_Auditing_Firm("Private Auditing Firm"),
		Other("Other");
		
		String value;
		
		private EmployeeDishonestyAuditsPerformedBy(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		private static final List<EmployeeDishonestyAuditsPerformedBy> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		public static EmployeeDishonestyAuditsPerformedBy random()  {
			return VALUES.get(new Random().nextInt(VALUES.size()));
		}
	}
	
	public enum LiquorSalesType {
		PackageSalesConsumedOffPremises("Package Sales Consumed Off Premises"), 
		DrinksConsumedOnPremises("Drinks Consumed On Premises"),
		Both("Both");
		
		String value;
		
		private LiquorSalesType(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		private static final List<LiquorSalesType> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		public static LiquorSalesType random()  {
			return VALUES.get(new Random().nextInt(VALUES.size()));
		}
	}
	
	public enum SalonTypeOfOperation {
		IndependentContractor("Independent Contractor"), 
		SalonOwner("Salon Owner");
		
		String value;
		
		private SalonTypeOfOperation(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		private static final List<SalonTypeOfOperation> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		public static SalonTypeOfOperation random()  {
			return VALUES.get(new Random().nextInt(VALUES.size()));
		}
	}
}

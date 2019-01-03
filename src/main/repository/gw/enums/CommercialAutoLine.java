package repository.gw.enums;


import java.util.*;


public class CommercialAutoLine {

	public enum LiabilityLimit implements GetValue {
		NONE("<none>"),
		OneHundred100K("100,000"), 
		TwoHundredFifty250K("250,000"), 
		ThreeHundred300K("300,000"), 
		ThreeHundredFifty350K("350,000"), 
		FiveHundred500K("500,000"), 
		SevenHundredFifty750K("750,000"),
		OneMillion1M("1,000,000");
		
		String value;
		
		private LiabilityLimit(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		private static final List<LiabilityLimit> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static LiabilityLimit random()  {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
		
	}
	
	public enum DeductibleLiabilityCoverage implements GetValue {
		NONE("<none>"),
		TwoHundredFifty250("250"), 
		FiveHundred500("500"), 
		OneThousand1000("1,000"), 
		TwoThousandFiveHundred2500("2,500"),
		FiveThousand5000("5,000");
		
		String value;
		
		private DeductibleLiabilityCoverage(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		public List<String> enumConstantsList() {
			List<String> enumList = new ArrayList<String>();
			DeductibleLiabilityCoverage[] enums = DeductibleLiabilityCoverage.values();
			for(DeductibleLiabilityCoverage oneEnum: enums) {
				enumList.add(oneEnum.value);
			}
			return enumList;
		}
		
		private static final List<DeductibleLiabilityCoverage> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static DeductibleLiabilityCoverage random()  {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}
	
	public enum MedicalPaymentsLimit implements GetValue {
		NONE("<none>"),
		FiveHundred500("500"), 
		OneThousand1000("1,000"),
		TwoThousand200("2,000"), 
		FiveThousand5K("5,000"), 
		TenThousand10K("10,000");
		
		String value;
		
		private MedicalPaymentsLimit(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		private static final List<MedicalPaymentsLimit> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static MedicalPaymentsLimit random()  {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}
	
	public enum HiredAutoLiabilityLimitBI implements GetValue {
		NONE("<none>"),
		OneHundred100K("100,000"), 
		TwoHundredFifty250K("250,000"), 
		ThreeHundred300K("300,000"), 
		ThreeHundredFifty350K("350,000"), 
		FiveHundred500K("500,000"), 
		SevenHundredFifty750K("750,000"),
		OneMillion1M("1,000,000");
		
		String value;
		
		private HiredAutoLiabilityLimitBI(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		private static final List<HiredAutoLiabilityLimitBI> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static HiredAutoLiabilityLimitBI random()  {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}
	
	public enum HiredAutoCollisionDeductible implements GetValue {
		NONE("<none>"),
		OneHundred100("100"), 
		FiveHundred("500");
		
		String value;
		
		private HiredAutoCollisionDeductible(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		private static final List<HiredAutoCollisionDeductible> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static HiredAutoCollisionDeductible random()  {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}
	
	public enum NonOwnedAutoLiabilityBI implements GetValue {
		NONE("<none>"),
		OneHundred100K("100,000"), 
		TwoHundredFifty250K("250,000"), 
		ThreeHundred300K("300,000"), 
		ThreeHundredFifty350K("350,000"), 
		FiveHundred500K("500,000"), 
		SevenHundredFifty750K("750,000"),
		OneMillion1M("1,000,000");
		
		String value;
		
		private NonOwnedAutoLiabilityBI(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		private static final List<NonOwnedAutoLiabilityBI> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static NonOwnedAutoLiabilityBI random()  {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}
	
	public enum CALineExclutionsAndConditions implements GetValue {
		ExcludedDriverEndorsementIDCA313007("Excluded Driver Endorsement IDCA 31 3007"), 
		ExclusionOfFederalEmployeesUsingAutosInGovernmentBusinessCA0442("Exclusion Of Federal Employees Using Autos In Government Business CA 04 42"), 
		ExclusionOrExcessCoverageHazardsOtherwiseInsuredCA9940("Exclusion Or Excess Coverage Hazards Otherwise Insured CA 99 40"), 
		ExplosivesCA2301("Explosives CA 23 01"), 
		OutOfStateVehicleExclusionIDCA313011("Out Of State Vehicle Exclusion IDCA 31 3011"), 
		ProfessionalServicesNotCoveredCA2018("Professional Services Not Covered CA 20 18"),
		RollingStoresCA2304("Rolling Stores CA 23 04"),
		SilicaOrSilicarelatedDustExclusionForCoveredAutosExposureCA2394("Silica Or Silica-related Dust Exclusion For Covered Autos Exposure CA 23 94"), 
		WrongDeliveryOfLiquidProductsCA2305("Wrong Delivery Of Liquid Products CA 23 05"), 
		
		AmphibiousVehiclesCA2397("Amphibious Vehicles CA 23 97"), 
		CommercialAutoManuscriptEndorsementIDCA313013("Commercial Auto Manuscript Endorsement IDCA 31 3013"),
		DesignatedInsuredForCoveredAutosLiabilityCoverageCA2048("Designated Insured For Covered Autos Liability Coverage CA 20 48"),
		EmployeeHiredAutosCA2054("Employee Hired Autos CA 20 54"), 
		LiabilityCoverageForRecreationalOrPersonalUseTrailersIDCA313005("Liability Coverage For Recreational Or Personal Use Trailers IDCA 31 3005"), 
		LossPayableClauseIDCA313001("Loss Payable Clause IDCA 31 3001"), 
		PhysicalDamageManuscriptEndorsementIDCA313004("Physical Damage Manuscript Endorsement IDCA 31 3004"),
		PublicTransportationAutosCA2402("Public Transportation Autos CA 24 02"),
		RepossessedAutosManuscriptEndorsementIDCA313010("Repossessed Autos Manuscript Endorsement IDCA 31 3010"),
		WaiverOfTransferOfRightsOfRecoveryAgainstOthersToUs_WaiverOfSubrogation_CA0444("Waiver Of Transfer Of Rights Of Recovery Against Others To Us (Waiver Of Subrogation) CA 04 44");
		
		String value;
		
		private CALineExclutionsAndConditions(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		private static final List<CALineExclutionsAndConditions> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static CALineExclutionsAndConditions random()  {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}
	
	
	public enum OtherCarComprehensive implements GetValue {
		NONE("<none>"),
		TwoHundredFifty("250"), 
		FiveHundred("500"), 
		Onethousand("1,000");
		
		String value;
		
		private OtherCarComprehensive(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		private static final List<OtherCarComprehensive> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static OtherCarComprehensive random()  {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}
	
	
	public enum OtherCarUnderinsuredMotorist implements GetValue {
		NONE("<none>"),
		FiftyThousand("50,000"),
		OneHundred100K("100,000"), 
		TwoHundredFifty250K("250,000"), 
		ThreeHundred300K("300,000"), 
		ThreeHundredFifty350K("350,000"), 
		FiveHundred500K("500,000"), 
		SevenHundredFifty750K("750,000"),
		OneMillion1M("1,000,000");
		
		String value;
		
		private OtherCarUnderinsuredMotorist(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		private static final List<OtherCarUnderinsuredMotorist> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static OtherCarUnderinsuredMotorist random()  {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}
	
	
	public enum OtherCarUninsuredMotorist implements GetValue {
		NONE("<none>"),
		FiftyThousand("50,000"),
		OneHundred100K("100,000"), 
		TwoHundredFifty250K("250,000"), 
		ThreeHundred300K("300,000"), 
		ThreeHundredFifty350K("350,000"), 
		FiveHundred500K("500,000"), 
		SevenHundredFifty750K("750,000"),
		OneMillion1M("1,000,000");
		
		String value;
		
		private OtherCarUninsuredMotorist(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		private static final List<OtherCarUninsuredMotorist> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static OtherCarUninsuredMotorist random()  {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}
	
	
	public enum OtherCarCollision implements GetValue {
		NONE("<none>"),
		TwoHundredFifty("250"), 
		FiveHundred("500"), 
		Onethousand("1,000");
		
		String value;
		
		private OtherCarCollision(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		private static final List<OtherCarCollision> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static OtherCarCollision random()  {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}
	
	
	
	
	
	
	
	
	
}






















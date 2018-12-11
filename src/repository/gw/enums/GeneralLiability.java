package repository.gw.enums;

import java.util.*;

public class GeneralLiability {
	
	public static class StandardCoverages {
		
		public enum CG0001_OccuranceLimit {
			None("<none>"),
			OneHundredThousand("100,000"), 
			ThreeHundredThousand("300,000"), 
			FiveHundredThousand("500,000"), 
			OneMillion1000000("1,000,000"),
			TwoMillion2000000("2,000,000");
			String value;
			
			private CG0001_OccuranceLimit(String value) {
				this.value = value;
			}
			
			public String getValue() {
				return this.value;
			}
		}
		
		public enum CG0001_MedicalPaymentsPerPerson {
			None("<none>"),
			FiveThousand("5,000"), 
			TenThousand("10,000");
			String value;
			
			private CG0001_MedicalPaymentsPerPerson(String value) {
				this.value = value;
			}
			
			public String getValue() {
				return this.value;
			}
		}
		
		public enum CG0001_PersonalAdvertisingInjury {
			None("<none>"),
			EXCLUDED("EXCLUDE");
			String value;
			
			private CG0001_PersonalAdvertisingInjury(String value) {
				this.value = value;
			}
			
			public String getValue() {
				return this.value;
			}
		}
		
		public enum CG0001_DamageToPropertyRentedToYou {
			None("<none>"),
			OneHundredThousand("100,000");
			String value;
			
			private CG0001_DamageToPropertyRentedToYou(String value) {
				this.value = value;
			}
			
			public String getValue() {
				return this.value;
			}
		}
		
		public enum CG0001_OperationsAggregateLimit {
			None("<none>"),
			EXCLUDED("EXCLUDE");
			String value;
			
			private CG0001_OperationsAggregateLimit(String value) {
				this.value = value;
			}
			
			public String getValue() {
				return this.value;
			}
		}
		
		public enum CG0001_DeductibleLiabilityInsCG0300 {
			None("<none>"),
			TwoHundredFifty("250"), 
			FiveHundred("500"), 
			SevenHundredFifty("750"), 
			OneThousand("1,000"),
			TwoThousand("2,000"), 
			ThreeThousand("3,000"), 
			FourThousand("4,000"), 
			FiveThousand("5,000");
			String value;
			
			private CG0001_DeductibleLiabilityInsCG0300(String value) {
				this.value = value;
			}
			
			public String getValue() {
				return this.value;
			}
		}
	}


	
	
	
	public static class AdditionalCoverages {
		
		public enum EmploymentPracticesLiabilityInsurance_AggregateLimit {
			TenThousand("10,000"), 
			TwentyFiveThousand("25,000"), 
			FiftyThousand("50,000"), 
			SeventyFiveThousand("75,000"),
			OneHundredThousand("100,000"), 
			TwoHundredFiftyThousand("250,000"), 
			FiveHundredThousand("500,000"), 
			OneMillion("1,000,000");
			String value;
			
			private EmploymentPracticesLiabilityInsurance_AggregateLimit(String value) {
				this.value = value;
			}
			
			public String getValue() {
				return this.value;
			}
			
			private static final List<EmploymentPracticesLiabilityInsurance_AggregateLimit> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
			private static final Random RANDOM = new Random();

			public static EmploymentPracticesLiabilityInsurance_AggregateLimit random()  {
				return VALUES.get(RANDOM.nextInt(VALUES.size()));
			}
		}
		
		public enum EmploymentPracticesLiabilityInsurance_Deductible {
			TwoThousandFiveHundred("2,500"), 
			FiveThousand("5,000"), 
			TenThousand("10,000"), 
			TwentyFiveThousand("25,000");
			String value;
			
			private EmploymentPracticesLiabilityInsurance_Deductible(String value) {
				this.value = value;
			}
			
			public String getValue() {
				return this.value;
			}
			
			private static final List<EmploymentPracticesLiabilityInsurance_Deductible> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
			private static final Random RANDOM = new Random();

			public static EmploymentPracticesLiabilityInsurance_Deductible random()  {
				return VALUES.get(RANDOM.nextInt(VALUES.size()));
			}
		}
		
		public enum EmploymentPracticesLiabilityInsurance_NumberLocations {
			ONE("1"), 
			TWO("2"), 
			THREE("3"), 
			FOUR("4"),
			FIVE("5"), 
			SixOrMore("6 or more");
			String value;
			
			private EmploymentPracticesLiabilityInsurance_NumberLocations(String value) {
				this.value = value;
			}
			
			public String getValue() {
				return this.value;
			}
			
			private static final List<EmploymentPracticesLiabilityInsurance_NumberLocations> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
			private static final Random RANDOM = new Random();

			public static EmploymentPracticesLiabilityInsurance_NumberLocations random()  {
				return VALUES.get(RANDOM.nextInt(VALUES.size()));
			}
		}
		
		public enum LawnCareServicesCoverage_Limit {
			None("<none>"),
			OneFiftyK_ThreeHundredK("150,000/300,000");
			String value;
			
			private LawnCareServicesCoverage_Limit(String value) {
				this.value = value;
			}
			
			public String getValue() {
				return this.value;
			}
		}
		
		
		
		
		
		
		public enum Operations {
			Blasting("Blasting"),
			Carpentry_Resident("Carpentry - Resident"),
			Concrete("Concrete"),
			Demolition("Demolition"),
			Drywall("Drywall"),
			EIFS_ExteriorInsulationFinishingSystem("EIFS (Exterior Insulation Finishing System)"),
			ElectricalWiring("Electrical Wiring"),
			Excavation("Excavation"),
			ExecutiveSupervisionofJobSitewithEmployees("Executive Supervision of Job Site with Employees"),
			FireRestoration("Fire Restoration"),
			Insulation("Insulation"),
			Landscaping("Landscaping"),
			Masonry("Masonry"),
			MoldRemediation("Mold Remediation"),
			Painting("Painting"),
			Plastering("Plastering"),
			Plumbing("Plumbing"),
			Roofing("Roofing"),
			Sewer_Drainfield("Sewer/Drainfield"),
			SiteCleanup("Site Cleanup"),
			StructureSteel("Structure Steel");
			
			String value;
			
			private Operations(String value) {
				this.value = value;
			}
			
			public String getValue() {
				return this.value;
			}
		}
	}
	
	
	
	
	
	public static class ExposureUnderwritingQuestions {
		
		public enum ResidentialConstructionType {
			One_TwoFamily("1-2 Family"),
			Three_FourFamily("3-4 Family"),
			Apartments("Apartments"),
			Townhouse("Townhouse"),
			Condos("Condos");
			String value;
			
			private ResidentialConstructionType(String value) {
				this.value = value;
			}
			
			public String getValue() {
				return this.value;
			}
		}
		
		public enum CommercialConstructionType {
			Retail("Retail"),
			Office("Office"),
			MFG("MFG"),
			Warehouse("Warehouse"),
			Other("Other");
			String value;
			
			private CommercialConstructionType(String value) {
				this.value = value;
			}
			
			public String getValue() {
				return this.value;
			}
		}
		
		
		public enum ConstructionActivities {
			AirConditioningServiceInstallation("Air conditioning service installation", "LEFT"), 
			Carpentry_Commercial("Carpentry - Commercial", "LEFT"), 
			CarpetRugUpholsteryCleaning("Carpet, Rug, or Upholstery cleaning", "LEFT"),
			ConcreteConstruction("Concrete construction", "LEFT"),
			DrivewayParkingAreaSidewalkRepairPavingRepair("Driveway, Parking Area, Sidewalk Repair, Paving Repair", "LEFT"),
			ElectricalWorkWithinBuildings("Electrical work within buildings", "LEFT"), 
			FenceErectionContractors("Fence erection contractors", "LEFT"),
			FurnitureOrFixturesInstallationInOffices("Furniture or fixtures installation in offices", "LEFT"),
			GradingOfLand("Grading of Land", "LEFT"),
			HeatOrHeat_AirConditioningInstallation_service_NOLPG("Heat or heat/air conditioning installation & service (no LPG)", "LEFT"),
			Insulation_NoFoamOrChemical("Insulation (no foam or chemical. Must be less then 10% of all activities)", "LEFT"),
			Janitorial("Janitorial", "LEFT"),
			Masonry("Masonry", "LEFT"),
			MetalErection_Structural("Metal erection - structural", "LEFT"),
			PaintingInterior("Painting - Interior", "LEFT"),
			PlasteringOrStuccoWork_No_EFIS("Plastering or Stucco work - no EFIS", "LEFT"),
			PlumbingResidential("Plumbing - Residential", "LEFT"),
			RoofingResidential_NoRe_Roofing("Roofing - residential (no re-roofing)", "LEFT"),
			SignPaintingOrLettering_Inside_NoSpraying("Sign painting or lettering - inside - no spraying", "LEFT"),
			SupervisorOnly("Supervisor only", "LEFT"),
			WindowCleaning_3StoriesOrLess("Window cleaning - 3 stories or less", "LEFT"),
			
			ApplianceInstallation_HouseholdOrCommercial("Appliance installation - Household or Commercial", "RIGHT"), 
			Carpentry_Residential("Carpentry - Residential", "RIGHT"),
			CeilingOrWallInstallation_Metal("Ceiling or wall installation - metal", "RIGHT"), 
			DoorWindoWorassembledmillworkinstallationmetal("Door, Window or assembled millwork installation - metal", "RIGHT"),
			DrywallOrWallboardinstallation("Drywall or wallboard installation", "RIGHT"),
			Excavation("Excavation", "RIGHT"),
			FloorCoveringInstalation_NotTileOrStone("Floor covering instalation - not tile or stone", "RIGHT"),
			GlassDealersandGlaziers_SalesAndInstallation("Glass dealers and glaziers - sales and installation", "RIGHT"),
			GutterInstallation("Gutter installation", "RIGHT"),
			HouseFurnishingInstallation("House Furnishing Installation ", "RIGHT"),
			InteriorDecorators("Interior Decorators", "RIGHT"),
			LawnSprinklerInstallation("Lawn sprinkler installation", "RIGHT"),
			MetalErection_NotStructural("Metal erection - not structural", "RIGHT"),
			Painting_Exterior_LessThan3Stories("Painting - Exterior - less than 3 stories", "RIGHT"),
			PaperHanging("Paper hanging", "RIGHT"),
			Plumbing_Commercial_Notindustrial_NoBoiler("Plumbing - Commercial - not industrial (no boiler)", "RIGHT"),
			RoofingCommercial_NoRe_Roofing("Roofing - Commercial (no re-roofing)", "RIGHT"),
			SidingInstallation_3StoriesOrLess("Siding installation - 3 stories or less", "RIGHT"),
			SnowRemoval("Snow Removal", "RIGHT"),
			TileStoneMarbleMosaic_Interior("Tile, Stone, Marble, Mosaic - Interior", "RIGHT");

			private String name;
			private String side;
			private static final Map<String, ConstructionActivities> SIDE_BY_SIDE = new HashMap<String, ConstructionActivities>();

			static {
				for (ConstructionActivities state : values()) {
					SIDE_BY_SIDE.put(state.getSide(), state);
				}
			}
			
			private ConstructionActivities(String name, String side) {
				this.name = name;
				this.side = side;
			}

			public String getSide() {
				return side;
			}
			
			public String getName() {
				return name;
			}

			public static ConstructionActivities getSide(final String side) {
				final ConstructionActivities state = SIDE_BY_SIDE.get(side);
				if (state != null) {
					return state;
				} else {
					return null;
				}
			}

			public static ConstructionActivities valueOfName(final String name) {
				final String enumName = name.replaceAll(" ", "");
				try {
					return valueOf(enumName);
				} catch (final IllegalArgumentException e) {
					return null;
				}
			}
		}
		
	}
	
	
	public enum Electability {
		Required,
		Suggested,
		Electable,
		Available;
	}
	
	public enum PremiumBase {
		S("s"),
		SPlus("s+"),
		T("t"),
		TPlus("t+"),
		M("m"),
		MPlus("m+"),
		A("a"),
		APlus("a+"),
		P("p"),
		PPlus("p+"),
		C("c"),
		CPlus("c+"),
		U("u"),
		UPlus("u+");
		String value;
		
		private PremiumBase(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum Restricted {
		R("R"),
		E("E"),
		Q("Q");
		String value;
		
		private Restricted(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum IndicateIfTheApplicantInsuredIsA {
		EmployeeLeasingFirm("Employee leasing firm"),
		TemporaryHelpFirm("Temporary help firm"),
		PrivateGolfClub("Private golf club"),
		School("School"),
		NoneOfTheAbove("None of the above");
		String value;
		
		private IndicateIfTheApplicantInsuredIsA(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}

}
























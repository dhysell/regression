package repository.gw.enums;

public class Property {
	
	public enum Garage {
		NoGarage("No Garage"),
		AttachedGarage("Attached Garage");
		
		String value;
		
		private Garage(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	
	}
	
	public enum NumberOfStories {
		One("1"),
		OneHalf("1.5"),
		Two("2"),
		TwoHalf("2.5"),
		Three("3"),
		ThreeHalf("3.5"),
		Four("4");
		
		String value;
		
		private NumberOfStories(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum FoundationType {
		Slab("Slab"),
		RaisedSlab("Raised Slab"),
		CrawlSpace("Crawl Space"),
		FullBasement("Full Basement"),
		
		// vvvv These 3 are the only options for construction type: Frame/Non Frame
		Foundation("Foundation"),
		NoFoundation("No Foundation"),
		PierAndBeam("Pier and Beam"),
		// ^^^^
		// @editor bhiltbrand 08/20/18: Currently, the 3 above options are on all construction types, but may change depending on extra stories referenced in
		//							 https://rally1.rallydev.com/#/203558461772d/detail/userstory/229683608520
		None("<none>"); // @editor ecoleman 6/28/18: fixed
		String value;
		
		private FoundationType(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum RoofType {
		CompositionShingles("Composition Shingles including Asphalt"),
		WoodShingles("Wood Shingles including Shakes"),
		RoofingTar("Roofing Tar and Gravel Metal including Steel"),
		FiberCement("Crawl Space"),
		FiberAluminum("Full Basement"),
		Other("Other"),
		Copper("Copper"),
		SlateConcrete("Slate Concrete"),
		ClayTile("Tile, Clay"),
		ConcreteTile("Tile, Concrete");
		
		String value;
		
		private RoofType(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum PrimaryHeating {
		Electricity("Electricity"),
		Gas("Gas"),
		Oil("Oil"),
		Other("Other");
		
		String value;
		
		private PrimaryHeating(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum Plumbing {
		Copper("Copper"),
		Galvanized("Galvanized"),
		PVC("PVC"),
		Other("Other");
		
		String value;
		
		private Plumbing(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum Wiring {
		Copper("Copper"),
		Aluminum("Aluminum"),
		KnobTube("Knob & Tube"),
		Other("Other");
		
		String value;
		
		private Wiring(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum ElectricalSystem {
		CircuitBreaker("Circuit Breaker"),
		Fuses("Fuses"),
		Other("Other");
		
		String value;
		
		private ElectricalSystem(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	
	public enum KitchenBathClass {
		Basic("Basic"),
		BuilderGrade("Builder Grade"),
		Designer("Designer"),
		Custom("Custom"),
		SemiCustom("Semi Custom");
		
		String value;
		
		private KitchenBathClass(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum SprinklerSystemType {
		None("None"),
		Full("Full"),
		Partial("Partial");
		
		String value;
		
		private SprinklerSystemType(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum NumberOfUnits {
		None("<None>"),
		OneUnit("1 Unit"),
		TwoUnits("2 Units"),
		ThreeUnits("3 Units"),
		FourUnits("4 Units");
		
		String value;
		
		private NumberOfUnits(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum PropertyTypePL {

		ResidencePremises("Residence Premises"),
		DwellingPremises("Dwelling Premises"),
		VacationHome("Vacation Home"),
		CondominiumDwellingPremises("Condominium Dwelling Premises"),
		CondominiumDwellingPremisesCovE("Condominium Dwelling Premises Cov E"),
		DwellingUnderConstruction("Dwelling Under Construction"),
		CondominiumResidencePremise("Condominium Residence Premises"),
		CondominiumVacationHome("Condominium Vacation Home"),
		Contents("Contents"),
		DwellingPremisesCovE("Dwelling Premises Cov E"),
		ResidencePremisesCovE("Residence Premises Cov E"),
		VacationHomeCovE("Vacation Home Cov E"),
		DwellingUnderConstructionCovE("Dwelling Under Construction Cov E"),
		CondoVacationHomeCovE("Condo Vacation Home Cov E"),
		AlfalfaMill("Alfalfa Mill"),
		Arena("Arena"),
		AwningCanopy("Awning/Canopy"),
		Barn("Barn"),
		BeeStation("Bee Stations"),
		BoatHouse("Boat House/Dock"),
		BoxCar("Box Car"),
		Bridge("Bridge"),
		BunkHouse("Bunk House"),
		Carport("Carport"),
		CommodityShed("Commodity Shed"),
		Corral("Corral"),
		DetachedGarage("Detached Garage"),
		DairyComplex("Dairy Complex"),
		DairyShade("Dairy Shade"),
		DeckPatio("Deck/Patio"),
		Elevator("Elevator"),
		ElevatorLeg("Elevator Leg"),
		ExtractionPlant("Extraction Plant"),
		FarmOffice("Farm Office"),
		FarrowHouse("Farrow House"),
		FeedGrinder("Feed Grinder"),
		FeedStorageShed("Feed Storage Shed"),
		Feeder("Feeder"),
		Fence("Fence"),
		Garage("Garage"),
		GasPump("Gas Pump"),
		GrainSeed("Grain/Seed"),
		Granary("Granary"),
		Greenhouse("Greenhouse"),
		Hangar("Hangar"),
		Harvestore("Harvestore"),
		Hatchery("Hatchery"),
		HayStorage("Hay Storage"),
		HayStrawInOpen("Hay/Straw In Open"),
		HayStrawInStorage("Hay/Straw In Storage"),
		HayStrawWithClearSpace("Hay/Straw With Clear Space"),
		HobbyHouse("Hobby House"),
		HogHouse("Hog House"),
		HoneyExtractionBuilding("Honey Extraction Building"),
		HopsProcessingBuilding("Hops Processing Building"),
		HotTub("Hot Tub"),
		ImplementShed("Implement Shed"),
		Kennel("Kennel"),
		LaborHouse("Labor House"),
		LambingShed("Lambing Shed"),
		ManureBunker("Manure Bunker"),
		MinkBarn("Mink Barn"),
		MintStill("Mint Still"),
		MiscBuilding("Misc Building"),
		Onions("Onions"),
		PeltingShed("Pelting Shed"),
		PoolHouse("Pool House"),
		Potatoes("Potatoes"),
		PoultryHouse("Poultry House"),
		PowerPole("Power Pole"),
		PumpHouse("Pump House"),
		Quonset("Quonset"),
		SatelliteDish("Satellite Dish"),
		ScaleHouse("Scale House"),
		Shed("Shed"),
		Shop("Shop"),
		Sign("Sign"),
		SiloTank("Silo Tank"),
		SolarPanels("Solar Panels"),
		StockShed("Stock Shed"),
		SwimmingPool("Swimming Pool"),
		TackRoom("Tack Room"),
		Trellis("Trellis"),
		TrellisedHops("Trellised Hops"),
		VegetableCellar("Vegetable Cellar"),
		VegetableWarehouse("Vegetable Warehouse"),
		VegetablesInStorage("Vegetables In Storage"),
		WashRoom("WashRoom"),
		Windmill("Windmill");
		
		String value;
		
		PropertyTypePL(String value) {
			this.value = value;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum SectionIDeductible {

		OneHundred("100"),
		TwoHundredFifty("250"),
		FiveHundred("500"),
		OneThousand("1000"),
		TwentyFiveHundred("2500"),
		FiveThousand("5000"),
		TenThousand("10000"),
		TwentyFiveThousand("25000");
		
		String value;
		
		private SectionIDeductible(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}

	public enum SectionIIGeneralLiabLimit {

		Limit_25_50_15("25/50/15"),
		Limit_25_50_25("25/50/25"),
		Limit_50_100_25("50/100/25"),
		Limit_50_100_50("50/100/50"),
		Limit_100_300_50("100/300/50"),
		Limit_100_300_100("100/300/100"),
		Limit_300_500_100("300/500/100"),
		Limit_300_500_300("300/500/300"),
		Limit_75000_CSL("75,000 CSL"),
		Limit_100000_CSL("100,000 CSL"),
		Limit_300000_CSL("300,000 CSL"),
		Limit_500000_CSL("500,000 CSL"),
		Limit_1000000_CSL("1,000,000 CSL");
		
		String value;
		
		private SectionIIGeneralLiabLimit(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}

	public enum SectionIIMedicalLimit {

		Limit_1000("1,000"),
		Limit_2000("2,000"),
		Limit_5000("5,000"),
		Limit_10000("10,000"),
		Limit_25000("25,000");
		
		String value;
		
		private SectionIIMedicalLimit(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	
	public enum SectionIIMedicalOccuranceLimit {
		Limit_1000("1,000", "5,000"),
		Limit_2000("2,000", "10,000"),
		Limit_5000("5,000", "25,000"),
		Limit_10000("10,000", "50,000"),
		Limit_25000("25,000", "125,000");
		String medicalValue;
		String occuranceLimit;
		
		private SectionIIMedicalOccuranceLimit(String medicalLimit, String occuranceLimit) {
			this.medicalValue = medicalLimit;
			this.occuranceLimit = occuranceLimit;
		}
		
		public String getSectionIIOccuranceMedicalLimit() {
			return this.medicalValue;
		}
		
		public String getSectionIIOccuranceLimit() {
			return this.occuranceLimit;
		}
	}
	
	public enum SectionIICoverageNamedPersonsMedicalDeductible {
		
		Ded_100("100"),
		Ded_250("250"),
		Ded_500("500");
		
		String value;
		
		private SectionIICoverageNamedPersonsMedicalDeductible(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
	}
	
	public enum SectionIICoveragesEnum {
		CropDustingAndSpraying("Crop Dusting and Spraying"),
		AllTerrainVehicles("All Terrain Vehicles"),
		BuffaloAndElk("Buffalo and Elk"),
		ChildCare("Child Care"),
		CustomFarming("Custom Farming"),
		CustomFarmingFire("Custom Farming Fire"),
		GolfCart("Golf Cart"),
		HorseBoardingAndPasturing("Horse Boarding and Pasturing"),
		HorseBoarding("Horse Boarding"),
		IncidentalOccupancy("Incidental Occupancy"),
		Livestock("Livestock"),
		MotorBoats("Motor Boats"),
		NamedPersonsMedical("Named Persons Medical"),
		OffRoadMotorcycles("Off Road Motorcycles"),
		PersonalWatercraft("Personal Watercraft"),
		PrivateLandingStrips("Private Landing Strips"),
		RecreationalVehicles("Recreational Vehicles"),
		Sailboats("Sailboats"),
		SeedsmanEAndO("Seedsman E and O"),
		Snowmobiles("Snowmobiles"),
		WatercraftLength("Watercraft Length"),
		EmployersNOE1("Employers NOE 1"),
		EmployersNOE2("Employers NOE 2"),
		FeedlotCustomFarmingEndo258("Feedlot Custom Farming Endorsement 258");
		
		String value;
		
		private SectionIICoveragesEnum(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
}

package repository.gw.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Building {
	
	public enum SqFtPercOccupied {
		ZeroThirtyOnePerc("0%-31%"), 
		ThirtyTwoFourtyNinePerc("32%-49%"), 
		FiftySeventyFourPerc("50%-74%"), 
		SeventyFiveOneHundredPerc("75%-100%");
		
		String value;
		
		private SqFtPercOccupied(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum PercAreaLeasedToOthers {
		ZeroTenPerc("0%-10%"), 
		ElevenFourtyNinePerc("11%-49%"), 
		FiftyEightyNinePerc("50%-89%"), 
		NinetyOneHundredPerc("90%-100%");
		
		String value;
		
		private PercAreaLeasedToOthers(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}

	public enum OccupancyInterestType {
		CondoUnitOwner("Condo - Unit Owner"), 
		TenantOperator("Tenant - Operator"), 
		BuildingOwnerOperator("Building Owner - Operator"), 
		CondoAssociation("Condo - Association"), 
		BuildingOwnerLessorsRisk("Building Owner - Lessors Risk");
		
		String value;
		
		private OccupancyInterestType(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum ValuationMethod {
		ReplacementCost("Replacement Cost Value"), 
		ActualCashValue("Actual Cash Value"),
		PlActualCashValue("Actual cash value");
		
		String value;
		
		private ValuationMethod(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum CauseOfLoss {
		Special("Special"), 
		NamedPerils("Named Perils BP 10 09");
		
		String value;
		
		private CauseOfLoss(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum HouseKeepingMaint {
		Superior("Superior"), 
		Good("Good"),
		Average("Average"),
		NeedsImprovement("Needs Improvement");
		
		String value;
		
		private HouseKeepingMaint(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum ParkingLotSidewalkCharacteristics {
		Potholes("Potholes"), 
		DeepCracks("Deep Cracks"),
		RaisedSunkenSurfaces("Raised or Sunken Surfaces"),
		TripFallHazards("Trip and Fall Hazards"),
		WellLitAtNight("Well Lit at Night"),
		NoneOfAbove("None Of Above");
		
		String value;
		
		private ParkingLotSidewalkCharacteristics(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		private static final List<ParkingLotSidewalkCharacteristics> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		public static ParkingLotSidewalkCharacteristics random()  {
			return VALUES.get(new Random().nextInt(VALUES.size()));
		}
	}
	
	public enum SafetyEquipment {
		HandRailinThreeOrMoreSteps("Hand Railing For 3 Or More Steps"), 
		NonSlipSurfaces("Non-Slip Surfaces"),
		FloorMatsEntrances("Floor Mats At Entrances"),
		InteriorStairLighting("Interior Stair Lighting"),
		Other("Other"),
		NoneOfAbove("None Of Above");
		
		String value;
		
		private SafetyEquipment(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		private static final List<SafetyEquipment> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		public static SafetyEquipment random()  {
			return VALUES.get(new Random().nextInt(VALUES.size()));
		}
	}
	
	public enum ConstructionType {
		Frame("Frame"), 
		JoistedMasonry("Joisted masonry"),
		Manufactured("Manufactured"),
		MasonryNonCombustible("Masonry non-combustible"),
		FireResistive("Fire-Resistive"),
		ModifiedFireResistive("Modified Fire-Resistive"),
		NonCombustible("Non-combustible");
		
		String value;
		
		private ConstructionType(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		private static final List<ConstructionType> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		public static ConstructionType random()  {
			return VALUES.get(new Random().nextInt(VALUES.size()));
		}
	}
	
	public enum ConstructionTypePL {
		Frame("Frame"), 
		JoistedMasonry("Joisted masonry"),
		Manufactured("Manufactured"),
		MasonryNonCombustible("Masonry non-combustible"),
		FireResistive("Fire-Resistive"),
		ModifiedFireResistive("Modified Fire-Resistive"),
		NonCombustible("Non-combustible"),
		NonFrame("Non-Frame"),
		Metal("Metal"),
		Masonry("Masonry"),
		ModularManufactured("Modular/Manufactured"),
		MobileHome("Mobile Home");
		
		String value;
		
		private ConstructionTypePL(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum ConstructionTypeCPP {
		Frame("Frame"), 
		JoistedMasonry("Joisted Masonry"),
		NonCombustible("Non-Combustible"),
		MasonryNonCombustible("Masonry Non-Combustible"),
		ManufacturedMobileHome("Manufactured/Mobile Home"),
		ModularOnFoundation("Modular/On Foundation"),
		ModifiedFireResistive("Modified Fire Resistive"),
		FireResistive("Fire Resistive");
		
		String value;
		
		private ConstructionTypeCPP(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum RoofingType {
		Aluminum("Aluminum"), 
		BuiltUpSmooth("Built-up/smooth"),
		BuiltUpTarGravel("Built-up/tar and Gravel"),
		Copper("Copper"),
		FiberglassTranslucentPanels("Fiberglass, Translucent Panels"),
		MetalSandwichPanels("Metal Sandwich Panels"),
		RolledRoofing("Rolled Roofing"),
		ShakesWood("Shakes, Wood"),
		ShinglesAsphalt("Shingles, Asphalt"),
		ShinglesFiberglass("Shingles, Fiberglass"),
		SinglePlyMembrane("Single-ply Membrane"),
		Slate("Slate"),
		Steel("Steel"),
		SteelPorcelainCoated("Steel, Porcelain Coated"),
		TileClay("Tile, Clay"),
		TileConcrete("Tile, Concrete"),
		TinTerne("Tin (Terne)");
		
		String value;
		
		private RoofingType(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		private static final List<RoofingType> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		public static RoofingType random()  {
			return VALUES.get(new Random().nextInt(VALUES.size()));
		}
	}
	
	public enum RoofingTypeCPP {
		Aluminum("Aluminum"), 
		BuiltUpSmooth("Built-up/smooth"),
		BuiltUpTarGravel("Built-up/tar and Gravel"),
		Composition("Composition"),
		Copper("Copper"),
		FiberglassTranslucentPanels("Fiberglass, Translucent Panels"),
		MetalSandwichPanels("Metal Sandwich Panels"),
		RolledRoofing("Rolled Roofing"),
		ShakesWood("Shakes, Wood"),
		ShinglesAsphalt("Shingles, Asphalt"),
		ShinglesFiberglass("Shingles, Fiberglass"),
		SinglePlyMembrane("Single-ply Membrane"),
		Slate("Slate"),
		Steel("Steel"),
		SteelPorcelainCoated("Steel, Porcelain Coated"),
		TileClay("Tile, Clay"),
		TileConcrete("Tile, Concrete"),
		TinTerne("Tin (Terne)");
		
		String value;
		
		private RoofingTypeCPP(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		private static final List<RoofingTypeCPP> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		public static RoofingTypeCPP random()  {
			return VALUES.get(new Random().nextInt(VALUES.size()));
		}
	}
	
	public enum RoofCondition {
		HasMajorDamage("Has Major Damage"), 
		HasSomeWearAndTear("Has some Wear and Tear"),
		NoIssues("No Issues"),
		NABppOnly("N/A BPP Only"),
		Other("Other");
		
		String value;
		
		private RoofCondition(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum RoofConditionCPP {
		HasMajorDamage("Has Major Damage"), 
		HasSomeWearAndTear("Has some Wear and Tear"),
		NoIssues("No Issues"),
		Other("Other");
		
		String value;
		
		private RoofConditionCPP(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum RoofMaintScheduleCPP {
		EveryYear("Every Year"), 
		Every2Years("Every 2 Years"),
		Every3_5Years("Every 3 - 5 Years"),
		Over5Years("Over 5 Years"),
		Unknown("Unknown"),;
		
		String value;
		
		private RoofMaintScheduleCPP(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum WiringType {
		Romex("Romex"), 
		Cloth("Cloth"),
		KnobAndTube("Knob and Tube"),
		Other("Other");
		
		String value;
		
		private WiringType(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum BoxType {
		CircuitBreaker("Circuit Breaker"), 
		FuseBox("Fuse Box");
		
		String value;
		
		private BoxType(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum FireBurglaryTypeOfSystem {
		Fire("Fire"), 
		Burglary("Burglary"),
		FireBurglary("Fire and Burglary");
		
		String value;
		
		private FireBurglaryTypeOfSystem(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum FireBurglaryResponseType {
		Local("Local"), 
		CentralStation("Central station"),
		PrivateMonitored("Private Monitored");
		
		String value;
		
		private FireBurglaryResponseType(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum FireBurglaryAlarmGrade {
		HoldupPanic("Holdup/panic"), 
		SecurityServiceWithTimingDevice("Security Service with Timing Device");
		
		String value;
		
		private FireBurglaryAlarmGrade(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum FireBurglaryAlarmType {
		NONE("<none>"), 
		CentralStationWithKeys("Central Station with keys (BR-1)"),
		CentralStationWithOutKeys("Central Station without keys (BR-1)"),
		LocalAlarm("Local Alarm (BR-2)");
		
		String value;
		
		private FireBurglaryAlarmType(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum BusinessIncomeOrdinaryPayroll {
		Days60("60 Days"), 
		Days90("90 Days"), 
		Days120("120 Days"), 
		Days150("150 Days"), 
		Days180("180 Days"), 
		Days270("270 Days"), 
		Days360("360 Days");
		
		String value;
		
		private BusinessIncomeOrdinaryPayroll(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum DiscretionaryPayrollExpense {
		Days90("90 Days"), 
		Days120("120 Days"), 
		Days150("150 Days"), 
		Days180("180 Days"), 
		Days270("270 Days"), 
		Days360("360 Days");
		
		String value;
		
		private DiscretionaryPayrollExpense(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}

	public enum CondoCommercialUnitOwnersLimit {
		Limit1000("1,000"), 
		Limit5000("5,000"), 
		Limit10000("10,000"), 
		Limit15000("15,000"), 
		Limit20000("20,000"), 
		Limit25000("25,000"),
		Limit30000("30,000"),
		Limit35000("35,000"),
		Limit40000("40,000"),
		Limit45000("45,000"),
		Limit50000("50,000");
		
		String value;
		
		private CondoCommercialUnitOwnersLimit(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum CoverageOrNot {
		NotCovered("Not Covered"), 
		Covered("Covered");
		
		String value;
		
		private CoverageOrNot(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum SpoilageDeductible {
		Ded500("500");
		
		String value;
		
		private SpoilageDeductible(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum SpoilageRiskClass {
		BakeryGoods28("28", "Bakery Goods"),
		CheeseShops28("28", "Cheese Shop"),
		ConvenienceFoodStore56("56", "Convenience Food Store"),
		DairyProductsExcludingIceCream56("56", "Dairy Products, excluding Ice Cream"),
		DairyProductsIncludingIceCream74("74", "Dairy Products, including Ice Cream"),
		Delicatessens28("28", "Delicatessens"),
		Florists74("74", "Florists"),
		FruitsAndVegetables28("28", "Fruits & Vegetables"),
		GroceryStores56("56", "Grocery Stores"),
		MeatAndPoultryMarkets56("56", "Meat & Poultry Markets"),
		PharmaceuticalsNonManufacturing56("56", "Pharmaceuticals - Non-Manufacturing"),
		Restaurants28("28", "Restaurants"),
		Seafood74("74", "Seafood"),
		Supermarkets56("56", "Supermarkets");
		
		String hazard;
		String description;
		
		
		private SpoilageRiskClass(String hazard, String description) {
			this.hazard = hazard;
			this.description = description;
		}
		
		public String getHazard() {
			return this.hazard;
		}
		
		public String getDescription() {
			return this.description;
		}
	}
	
	public enum UtilitiesCoverageAppliesTo {
		Building("Building"), 
		BusinessPersonalProperty("Business Personal Property"),
		BuildingAndBusinessPersonalProperty("Building and Business Personal Property");
		
		String value;
		
		private UtilitiesCoverageAppliesTo(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum UtilitiesUtilityIs {
		Public("Public"), 
		NotPublic("Not Public");
		
		String value;
		
		private UtilitiesUtilityIs(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum CoverageForm {
		BuildingandPersonalProperty("Building and Personal Property"), 
		CondominiumUnitOwners("Condominium Unit-Owners"),
		CondominiumAssociation("Condominium Association");
		
		String value;
		
		private CoverageForm(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum CondominiumType {
		Association("Association"), 
		UnitOwner("Unit Owner"),
		NotACondominium("Not a Condominium");
		
		String value;
		
		private CondominiumType(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum MultipleOccupancyClassCode {
		LargeArea0433("0433 - Mercantile � Multiple occupancy � Large area (over 15,000 sq. ft.) � Not class rated"),
		NOTLargeArea0434("0434 - Mercantile � Multiple occupancy � Not large area (15,000 sq. ft. or less) � Not class rated");
		
		String value;
		
		private MultipleOccupancyClassCode(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
}


























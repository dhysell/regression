package repository.gw.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Vehicle {


	public enum VehicleType {
		PrivatePassenger("Private Passenger"),
		PublicTransportation("Public Transportation"),
		Trailer("Trailer"),
		Trucks("Trucks"),
		TruckTractors("Truck-Tractors"),
		Miscellaneous("Miscellaneous");

		String value;

		 VehicleType(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
		
		public static VehicleType getRandomVehicleType()  {
			final Random RANDOM = new Random();
			final List<VehicleType> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
			final int SIZE = VALUES.size();
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}
	
	/**
	 * Vehicle Types for PL, since PL had a different list from CPP
	 * @author drichards
	 *
	 */
	public enum VehicleTypePL {
		PrivatePassenger("Private Passenger","Car, SUV, or Crossover"),
		PassengerPickup("Passenger Pickup","Passenger Pickup or Light Duty Truck"),
		Motorcycle("Motorcycle","Motorcycle"),
		FarmTruck("Farm Truck",""),
		SemiTrailer("Semi-Trailer",""),
		Trailer("Trailer",""),
		MotorHome("Motor Home",""),
		ShowCar("Show Car",""),
		LocalService1Ton("Local Service 1 Ton","");
		String value;
		String portalValue;
		
		 VehicleTypePL(String type, String portalType){
			value = type;
			this.portalValue = portalType;
		}
		
		public String getValue(){
			return value;
		}

		public String getPortalValue(){ return portalValue;}
		
		public static VehicleTypePL getEnumFromStringValue(String text){
			// check that text isn't empty before doing comparison. 
			if(text != null){
				for(VehicleTypePL type : VehicleTypePL.values()){
					if(text.equalsIgnoreCase(type.value)){
						return type;
					}
				}
			}
			return null; // text is null to begin with. 
		}
	}
	
	public enum BodyType {
		None("<none>"),
		Bus("Bus"),
		Convertible("Convertible"),
		Coupe("Coupe"),
		FourDoorSedan("Four-door sedan"),
		Other("Other"),
		PickupTruck("Pickup truck"),
		RVMotorHome("RV/Motor Home"),
		Semitrailers("Semitrailers"),
		StationWagon("Station wagon"),
		SUV("SUV"),
		Tractor("Tractor"),
		TrailersOver2000lbs("Trailers over 2000 lbs"),
		Truck("Truck"),
		TwoDoorSedan("Two-door sedan"),
		UtilityTrailer("Utility trailer"),
		Van("Van"),
		ServiceOrUtilityTrailers("Service or Utility Trailers"),
		ChurchBus("Church Bus"),
		MotelCourtesyBus("Motel Courtesy Bus"),
		VanPoolEmployeeOnly("Van Pool-Employee Only"),
		AntiqueAutos("Antique Autos"),
		FlowerCar("Flower Car"),
		FuneralLimo("Funeral Limo"),
		Hearse("Hearse"),
		MotorHomesUpTo22Feet("Motor Homes - Up to 22 Feet"),
		MotorHomesMoreThan22Feet("Motor Homes More Than 22 Feet"),
		TrailerEquippedAsLivingQuarters("Trailer Equipped As Living Quarters");

		String value;

		 BodyType(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}
	
	public enum SizeClass {
		None("<none>"),
		LightTrucksGVWOf10000PoundsOrLess("Light Trucks (GVW) of 10,000 pounds or less"),
		MediumTrucksGVWOf10001To20000Pounds("Medium Trucks (GVW) of 10,001 � 20,000 pounds"),
		HeavyTrucksGVWOf20001To45000Pounds("Heavy Trucks (GVW) of 20,001 � 45,000 pounds"),
		ExtraHeavyTrucksGVWOver45000Pounds("Extra-heavy Trucks (GVW) over 45,000 pounds"),
		HeavyTruckTractorAGrossCombinationWeightGCWOf45000PoundsOrLess("Heavy truck-tractor a gross combination weight (GCW) of 45,000 pounds or less"),
		ExtraHeavyTruckTractorAGrossCombinationWeightGCWOver45000Pounds("Extra-heavy truck-tractor a gross combination weight (GCW) over 45,000 pounds"),
		Semitrailers("Semitrailers"),
		TrailersOver2000lbs("Trailers over 2000 lbs"),
		ServiceOrUtilityTrailers("Service or Utility Trailers");
		
		String value;
		
		 SizeClass(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum Comprehensive_CollisionDeductible {
		OneHundred("100"),
		TwoHundredFifty250("250"),
		FiveHundred500("500"),
		OneThousand1000("1,000"),
		TwoThousand2000("2,000"),
		ThreeThousand3000("3,000"),
		FiveThousand5000("5,000");
		
		String value;
		
		 Comprehensive_CollisionDeductible(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum SpecifiedCauseOfLoss {
		FireOnly("Fire Only"),
		FireAndTheftOnly("Fire and Theft Only"),
		FireTheftAndWindstormOnly("Fire, Theft, and Windstorm Only"),
		LimitedSpecifiedCausesoOfLoss("Limited Specified Causes of Loss"),
		SpecifiedCauseOfLoss("Specified Causes of Loss"),
		LimitedSpecifiedCausesofLoss_ExceptTheft("Limited Specified Causes of Loss (Except Theft)"),
		SpecifiedCausesofLoss_ExceptTheft("Specified Causes of Loss (Except Theft)");

		String value;

		 SpecifiedCauseOfLoss(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}
	
	public enum SpecifiedCauseOfLossDeductible {
		Zero0("0"),
		TwoHundredFifty250("250"),
		FiveHundred500("500"),
		OneThousand1000("1,000"),
		TwoThousand2000("2,000"),
		FiveThousand5000("5,000");

		String value;

		 SpecifiedCauseOfLossDeductible(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}
	
	public enum CAVehicleAdditionalCoverages {
		AdditionalNamedInsuredForDesignatedPersonOrOrganizationIDCA313009("Additional Named Insured For Designated Person Or Organization IDCA 31 3009"),
		AudioVisualDataElectronicEquipmentCoverageAddedLimitsCA9960("Audio, Visual, Data Electronic Equipment Coverage Added Limits CA 99 60"),
		AutoLoanLeaseGapCoverageCA2071("Auto Loan/Lease Gap Coverage CA 20 71"),
		LessorAdditionalInsuredAndLossPayeeCA2001("Lessor - Additional Insured And Loss Payee CA 20 01"),
		LossPayableClauseAudioVisualAndDataElectronicEquipmentCoverageAddedLimitsIDCA313002("Loss Payable Clause - Audio, Visual And Data Electronic Equipment Coverage Added Limits IDCA 31 3002"),
		MobileHomesContentsCoverageCA2016("Mobile Homes Contents Coverage CA 20 16"),
		MotorCarriersInsuranceForNonTruckingUseCA2309("Motor Carriers - Insurance For Non-Trucking Use CA 23 09"),
		FireFireAndTheftFireTheftAndWindstormAndLimitedSpecifiedCausesOfLossCoveragesCA9914("Fire, Fire And Theft, Fire, Theft And Windstorm And Limited Specified Causes Of Loss Coverages CA 99 14"),
		RentalReimbursementCA9923("Rental Reimbursement CA 99 23"),
		RoadsideAssistanceIDCA313008("Roadside Assistance IDCA 31 3008");

		String value;

		 CAVehicleAdditionalCoverages(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}
	
	public enum CAVehicleExclusionsAndConditions {
		RemovalOfPropertyDamageCoverageIDCA313006("Removal Of Property Damage Coverage IDCA 31 3006"),
		MobileHomesContentsNotCoveredIDCA313003("Mobile Homes Contents Not Covered IDCA 31 3003"),
		ExclusionOrExcessCoverageHazardsOtherwiseInsuredCA9940("Exclusion or Excess Coverage Hazards Otherwise Insured CA 99 40");

		String value;

		 CAVehicleExclusionsAndConditions(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}
	
	public enum CommutingMiles {
		Pleasure1To2Miles("Pleasure 1 - 2 Miles","1 - 2 Miles"),
		WorkSchool3To10Miles("Work/School 3 - 10 Miles","3 - 10 Miles"),
		WorkSchool10PlusMiles("Work/School 10 Plus Miles", "10 Plus Miles"),
		Business("Business",""),
		Farm("Farm","");

		String value;
		String qbValue;

		 CommutingMiles(String value, String qbValue) {
			this.value = value;
			this.qbValue = qbValue;
		}

		public String getValue() {
			return this.value;
		}
		
		public String getQbValue(){
			return this.qbValue;
		}
		
		public static CommutingMiles getEnumFromStringValue(String text) {
			// check that text isn't empty before doing comparison.
			if (text != null) {
				for (CommutingMiles type : CommutingMiles.values()) {
					if (text.equalsIgnoreCase(type.value)) {
						return type;
					}
				}
			}
			return null; // text is null to begin with.
		}
	}
	
	public enum Usage {
		FarmUse("Farm Use"),
		FarmUseWithOccasionalHire("Farm Use With Occasional Hire"),
		FarmUseMoreThanOccasionalHire("Farm Use More Than Occasional Hire");

		String value;

		 Usage(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}
	
	public enum MileageFactor {
		Under2500("Under 2500"),
		From2500To7499("2500 - 7499"),
		From7500To24999("7500 - 24999"),
		From25000To49999("25000 - 49999"),
		Over49999("Over 49999");

		String value;

		 MileageFactor(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}
	
	public enum Radius {
		None("<none>"),
		Local0To100Miles("Local (0 - 100 Miles)"),
		Intermediate101To300Miles("Intermediate (101 - 300 Miles)"),
		LongDistancesOver301Miles("Long distance (Over 300 Miles)"),
		From301To500Miles("301 - 500 Miles"),
		Over500Miles("Over 500 Miles");

		String value;

		 Radius(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}
	
	public enum LongDistanceOptions {
		None("<none>"),
		Denver("Denver"),
		Portland("Portland"),
		SaltLakeCity("Salt Lake City"),
		Pacific("Pacific"),
		Mountain("Mountain");

		String value;

		 LongDistanceOptions(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}
	
	public enum BusinessUseClass {
		None("<none>"),
		ServiceUse("Service Use"),
		RetailUse("Retail Use"),
		CommercialUse("Commercial Use");
		
		String value;

		 BusinessUseClass(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}
	
	public enum SecondaryClassType {
		None("<none>"),
		Truckers("Truckers"),
		FoodDelivery("Food Delivery"),
		SpecializedDelivery("Specialized Delivery"),
		WasteDisposal("Waste Disposal"),
		DumpAndTransitMix("Dump and Transit Mix"),
		Contractors("Contractors"),
		NotOtherwiseSpecified("Not Otherwise Specified");
		
		String value;

		 SecondaryClassType(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}
	
	public enum SecondaryClass {
		CommonCarriers("Common Carriers"),
		ContractCarriersOtherThanChemicalOrIronAndSteelHaulers("Contract Carriers (Other than Chemical or Iron and Steel Haulers)"),
		ExemptCarriersOtherThanLivestockHaulers("Exempt Carriers (Other than Livestock Haulers)"),
		ExemptCarriersHaulingLivestock("Exempt Carriers Hauling Livestock"),
		CarriersEngagedInBothPrivateCarriageAndTransportingGoods("Carriers Engaged in both Private Carriage and Transporting Goods, Materials or Commodities for Others"),
		MaterialsOrCommoditiesForOthers("Materials or Commodities for Others"),
		AllOther("All Other"),
		FilmDelivery("Film Delivery"),
		MagazinesOrNewspapers("Magazines or Newspapers"),
		MailAndParcelPost("Mail and Parcel Post"),
		BuildingCommercial("Building Commercial"),
		BuildingPrivateDwellings("Building Private Dwellings"),
		ElectricalPlumbingMasonryPlasteringAndOtherRepairOrSevice("Electrical, Plumbing, Masonry, Plastering and Other Repair or Sevice"),
		Excavating("Excavating"),
		StreetAndRoad("Street and Road"),
		CanneriesAndPackingPlants("Canneries and Packing Plants"),
		FishAndSeafood("Fish and Seafood"),
		FrozenFood("Frozen Food"),
		FruitAndVegtable("Fruit and Vegtable"),
		MeatAndPoultry("Meat and Poultry"),
		Garbage("Garbage"),
		JunkDealers("Junk Dealers"),
		SandAndGravelOtherThanQuarrying("Sand and Gravel (Other than Quarrying)");

		String value;

		 SecondaryClass(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}
	
	public enum VehiclePrimaryUsed {
		None("<none>"),
		Commercial("Commercial");
		
		String value;

		 VehiclePrimaryUsed(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}
	
	public enum SeatingCapacity {
		None("<none>"),
		OneToEight("1 - 8"),
		NineToTwenty("9 - 20"),
		TwentyOneToSixty("21 - 60"),
		Over60("Over 60");
		
		String value;

		 SeatingCapacity(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}
	
	/**
	 * Truck Type for PL
	 * @author drichards
	 *
	 */
	public enum VehicleTruckTypePL {

		OneToSix("One to Six Ton", true, false),
		UnderFour("Under Four Ton", false, true),
		FourPlus("Four Ton and Over", false, true),
		TractorType("Tractor Type Truck", true, true);
		
		private String value;
		private boolean noHire;
		private boolean withHire;

		 VehicleTruckTypePL(String value, boolean noHire, boolean withHire) {
			this.value = value;
			this.noHire = noHire;
			this.withHire = withHire;
		}

		public String getValue() {
			return this.value;
		}
		
		public boolean getNoHire() {
			return this.noHire;
		}
		
		public boolean getWithHire() {
			return this.withHire;
		}
	}

	/**
	 * Truck Type for PL
	 * @author drichards
	 *
	 */
	public enum TrailerTypePL {

		Livestock("Livestock", false),
		Utility("Utility", false),
		Travel("Travel", false),
		Camper("Camper", false),
		Shell("Shell", false),
		Semi("Semi", true),
		SemiAnd4Wheeler("Semi & 4 Wheeler", true),
		FourWheeler("Four Wheeler", true);
		
		private String value;
		private boolean semiTrailer;

		 TrailerTypePL(String value, boolean semiTrailer) {
			this.value = value;
			this.semiTrailer = semiTrailer;
		}

		public String getValue() {
			return this.value;
		}
		
		public boolean isSemiTrailer() {
			return this.semiTrailer;
		}
		
	}
	
	public enum DriverVehicleUsePL {
		Principal, Occasional
	}
	
	public enum OccasionalDriverReason {
		Other, Permit, College
	}

	public enum PAVehicleCoverages {
		
		Comprehensive("Comprehensive"),
		Collision("Collision"),
		RentalReimbursement("Rental Reimbursement"),
		EmergencyRoadsideService("Emergency Roadside Service"),
		AdditionalLivingExpense("Additional Living Expense"),
		FireTheft("Fire & Theft");
		String value;
		
		 PAVehicleCoverages(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum PAComprehensive_CollisionDeductible {
		TwentyFive25("25"),
		Fifty50("50"),
		OneHundred100("100"),
		TwoHundredFifty250("250"),
		FiveHundred500("500"),
		OneThousand1000("1,000");
		
		String value;
		
		 PAComprehensive_CollisionDeductible(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum PAVehicleCategory {
		VehicleYearAfter1980("Vehicle year >  1980"),
		VehicleYearBefore1980("Vehicle year <= 1980"),
		NoVIN("No VIN");
		
		String value;
		
		 PAVehicleCategory(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum LengthOfLease {
		None("<none>"),
		SixMonthsOrGreater("6 months or greater"),
		LessThan6Months("Less than 6 months");
		
		String value;
		
		 LengthOfLease(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum HowManyTimesAYearDoesApplicantTravelOver300Miles {
		None("<none>"),
		OneToFour("1-4"),
		OverFour("Over 4");
		
		String value;
		
		 HowManyTimesAYearDoesApplicantTravelOver300Miles(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum CAVehicleUnderwritingQuestions {
		DoesApplicantInsuredBackHaulOrTransportGoodsWhileNotOperatingUnderTheAuthorityOfAnotherCarrier("Does applicant/insured back haul or transport goods while not operating under the authority of another carrier"),
		DoesThePartyWhoseAuthorityUnderWhichTheApplicantInsuredHaveCoverageForTheTrucksAndTrailersShownOnThePolicy("Does the party whose authority under which the applicant/insured have coverage for the trucks and trailers shown on the policy"),
		DoesTheApplicantInsuredLeaseOrOperateTheirVehicleUnderAnotherPartiesPUCAuthority("Does the applicant/insured lease or operate their vehicle under another parties PUC authority"),
		DoesTheApplicantInsuredLeaseTheVehicleWithoutADriver("Does the applicant/insured lease the vehicle without a driver"),
		IsBusUsedToTransportSeasonalOrMigrantWorkers("Is bus used to transport seasonal or migrant workers");

		String value;

		 CAVehicleUnderwritingQuestions(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}
	
	public enum LocationOfDamageCPP {
		Front("Front"),
		RightFront("Right Front"),
		LeftFront("Left Front"),
		RightFrontPillar("Right Front Pillar"),
		LeftFrontPillar("Left Front Pillar"),
		RightTBone("Right T Bone"),
		LeftTBone("Left T Bone"),
		RightQuarterPost("Right Quarter Post"),
		LeftQuarterPost("Left Quarter Post"),
		RightRear("Right Rear"),
		LeftRear("Left Rear"),
		Rear("Rear"),
		HoodRoofTrunklid("Hood Roof Trunklid"),
		Undercarriage("Undercarriage"),
		Glass("Glass"),
		Other("Other");
		
		String value;

		 LocationOfDamageCPP(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
		
		private static final List<LocationOfDamageCPP> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static LocationOfDamageCPP random()  {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}

	}
	
	public enum FrontDamagedItemCPP {
		Bumper("Bumper"),
		BumperCover("Bumper Cover"),
		Fender("Fender"),
		FrontSpoilerAirDam("Front Spoiler/Air Dam"),
		Grille("Grille"),
		Other("Other");
		
		String value;

		 FrontDamagedItemCPP(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
		
		private static final List<FrontDamagedItemCPP> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static FrontDamagedItemCPP random()  {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}
	
	public enum RightFrontDamagedItemCPP {
		Fender("Fender"),
		PassengersSideFogLight("Passenger's Side Fog Light"),
		PassengersSideHeadlight("Passenger's Side Headlight"),
		PassengersSideSignalLight("Passenger's Side Signal Light"),
		PassengersSideViewMirror("Passenger's Side View Mirror"),
		QuarterPanel("Quarter Panel"),
		WheelWell("Wheel Well"),
		Other("Other");
		
		String value;

		 RightFrontDamagedItemCPP(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
		
		private static final List<RightFrontDamagedItemCPP> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static RightFrontDamagedItemCPP random()  {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}
	
	public enum LeftFrontDamagedItemCPP {
		DriversSideFogLight("Driver's Side Fog Light"),
		DriversSideHeadlight("Driver's Side Headlight"),
		DriversSideSignalLight("Driver's Side Signal Light"),
		DriversSideViewMirror("Driver's Side View Mirror"),
		Fender("Fender"),
		QuarterPanel("Quarter Panel"),
		WheelWell("Wheel Well"),
		Other("Other");
		
		String value;

		 LeftFrontDamagedItemCPP(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
		
		private static final List<LeftFrontDamagedItemCPP> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static LeftFrontDamagedItemCPP random()  {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}
	
	public enum RightFrontPillarDamagedItemCPP {
		SelectDamageDescription("Select Damage Description"),
		Other("Other");
		
		String value;

		 RightFrontPillarDamagedItemCPP(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
		
		private static final List<RightFrontPillarDamagedItemCPP> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static RightFrontPillarDamagedItemCPP random()  {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}
	
	public enum LeftFrontPillarDamagedItemCPP {
		SelectDamageDescription("Select Damage Description"),
		Other("Other");
		
		String value;

		 LeftFrontPillarDamagedItemCPP(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
		
		private static final List<LeftFrontPillarDamagedItemCPP> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static LeftFrontPillarDamagedItemCPP random()  {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}
	
	public enum RightTBoneDamagedItemCPP {
		PassengersSideFrontDoor("Passenger's Side Front Door"),
		PassengersSideFrontDoorHandle("Passenger's Side Front Door Handle"),
		PassengersSideRearDoor("Passenger's Side Rear Door"),
		PassengersSideRearDoorHandle("Passenger's Side Rear Door Handle"),
		PassengersSideRockerPanelSill("Passenger's Side Rocker Panel/Sill"),
		Other("Other");
		
		String value;

		 RightTBoneDamagedItemCPP(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
		
		private static final List<RightTBoneDamagedItemCPP> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static RightTBoneDamagedItemCPP random()  {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}
	
	public enum LeftTBoneDamagedItemCPP {
		DriversSideFrontDoor("Driver's Side Front Door"),
		DriversSideFrontDoorHandle("Driver's Side Front Door Handle"),
		DriversSideRearDoor("Driver's Side Rear Door"),
		DriversSideRearDoorHandle("Driver's Side Rear Door Handle"),
		DriversSideRockerPanelSill("Driver's Side Rocker Panel/Sill"),
		Other("Other");
		
		String value;

		 LeftTBoneDamagedItemCPP(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
		
		private static final List<LeftTBoneDamagedItemCPP> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static LeftTBoneDamagedItemCPP random()  {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}
	
	public enum RightQuarterPostDamagedItemCPP {
		SelectDamageDescription("Select Damage Description"),
		Other("Other");
		
		String value;

		 RightQuarterPostDamagedItemCPP(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
		
		private static final List<RightQuarterPostDamagedItemCPP> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static RightQuarterPostDamagedItemCPP random()  {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}
	
	public enum LeftQuarterPostDamagedItemCPP {
		SelectDamageDescription("Select Damage Description"),
		Other("Other");
		
		String value;

		 LeftQuarterPostDamagedItemCPP(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
		
		private static final List<LeftQuarterPostDamagedItemCPP> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static LeftQuarterPostDamagedItemCPP random()  {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}
	
	public enum RightRearDamagedItemCPP {
		BrakeLight("Brake Light"),
		Fender("Fender"),
		QuarterPanel("Quarter Panel"),
		SignalLight("Signal Light"),
		TailLight("Tail Light"),
		WheelWell("Wheel Well"),
		Other("Other");
		
		String value;

		 RightRearDamagedItemCPP(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
		
		private static final List<RightRearDamagedItemCPP> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static RightRearDamagedItemCPP random()  {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}
	
	public enum LeftRearDamagedItemCPP {
		BrakeLight("Brake Light"),
		Fender("Fender"),
		QuarterPanel("Quarter Panel"),
		SignalLight("Signal Light"),
		TailLight("Tail Light"),
		WheelWell("Wheel Well"),
		Other("Other");
		
		String value;

		 LeftRearDamagedItemCPP(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
		
		private static final List<LeftRearDamagedItemCPP> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static LeftRearDamagedItemCPP random()  {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}
	
	public enum RearDamagedItemCPP {
		BrakeLight("Brake Light"),
		Bumper("Bumper"),
		BumperCover("Bumper Cover"),
		Fender("Fender"),
		RearSpoilerWing("Rear Spoiler/Wing"),
		Other("Other");
		
		String value;

		 RearDamagedItemCPP(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
		
		private static final List<RearDamagedItemCPP> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static RearDamagedItemCPP random()  {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}
	
	public enum HoodRoofTrunklidDamagedItemCPP {
		SelectDamageDescription("Select Damage Description"),
		Other("Other");
		
		String value;

		 HoodRoofTrunklidDamagedItemCPP(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
		
		private static final List<HoodRoofTrunklidDamagedItemCPP> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static HoodRoofTrunklidDamagedItemCPP random()  {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}
	
	public enum UndercarriageDamagedItemCPP {
		Exhaust("Exhaust"),
		Frame("Frame"),
		Suspension("Suspension"),
		Other("Other");
		
		String value;

		 UndercarriageDamagedItemCPP(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
		
		private static final List<UndercarriageDamagedItemCPP> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static UndercarriageDamagedItemCPP random()  {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}
	
	public enum GlassDamagedItemCPP {
		DriversSideFrontWindow("Driver's Side Front Window"),
		DriversSideRearWindow("Driver's Side Rear Window"),
		FrontWindshield("Front Windshield"),
		PassengersSideFrontWindow("Passenger's Side Front Window"),
		PassengersSideRearWindow("Passenger's Side Rear Window"),
		RearWindshield("Rear Windshield"),
		Sunroof("Sunroof"),
		Other("Other");
		
		String value;

		 GlassDamagedItemCPP(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
		
		private static final List<GlassDamagedItemCPP> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static GlassDamagedItemCPP random()  {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}
	
	public enum OtherDamagedItemCPP {
		FuelFillerDoor("Fuel Filler Door"),
		LicensePlateLamp("License Plate Lamp"),
		WholeVehicle("Whole Vehicle"),
		Other("Other");
		
		String value;

		 OtherDamagedItemCPP(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
		
		private static final List<OtherDamagedItemCPP> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static OtherDamagedItemCPP random()  {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}
	
	public enum DamageDescriptionCPP {
		Broken("Broken"),
		Cracked("Cracked"),
		Dented("Dented"),
		HailDamage("Hail Damage"),
		Missing("Missing"),
		NoMaterialDamageCoverage("No Material Damage Coverage"),
		OxodizedPaint("Oxodized Paint"),
		PeelingChippingPaint("Peeling/Chipping Paint"),
		RockChips("Rock Chips"),
		Scratched("Scratched"),
		Other("Other Please Describe");
		
		String value;

		 DamageDescriptionCPP(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
		
		private static final List<DamageDescriptionCPP> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static DamageDescriptionCPP random()  {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}
	
	public enum PAMaterialDamageLocation {
		RightFront("Right Front"),
		LeftFront("Left Front"),
		RightFrontPillar("Right Front Pillar"),
		LeftFrontPillar("Left Front Pillar"),
		Front("Front");
		
		String value;

		 PAMaterialDamageLocation(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}

	}
	
	public enum VehicleFieldsCPP {
		ModelYear,
		Make,
		Model,
		FactoryCost
	}
	
	public enum RentalReimbursementNumberOfDays {
		Days30("30 Days"),
		Days40("40 Days"),
		Days50("50 Days"),
		Days60("60 Days");
		
		String value;

		 RentalReimbursementNumberOfDays(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}

	}
	
	
	public enum RentalReimbursementLimitPerDay {
		NONE("<none>"),
		Thirty30("30"),
		ThirtyFive35("35"),
		Forty40("40"),
		FortyFive45("45"),
		Fifty50("50"),
		SeventyFive75("75"),
		OneHundred100("100");
		
		String value;

		 RentalReimbursementLimitPerDay(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}

	}
	
	
	public enum AdditionalInterestTypeCPP {
		Lessor_AdditionalInsuredAndLossPayeeCA_20_01("Lessor - Additional Insured and Loss Payee CA 20 01"),
		LossPayableClause_AudioVisualAndDataElectronicEquipmentCoverageAddedLimitsIDCA_31_3002("Loss Payable Clause - Audio, Visual And Data Electronic Equipment Coverage Added Limits IDCA 31 3002"),
		LossPayableClauseIDCA_31_3001("Loss Payable Clause IDCA 31 3001"),
		CP_AdditionalInsured_BuildingOwner_CP_12_19("Additional Insured � Building Owner CP 12 19"),
		CP_BuildingOwnerLossPayableClause_CP_12_18("Building Owner Loss Payable Clause CP 12 18"),
		CP_ContractOfSaleClause_CP_12_18("Contract Of Sale Clause CP 12 18"),
		CP_LendersLossPayableClause_CP_12_18("Lender's Loss Payable Clause CP 12 18"),
		CP_LossPayableClause_CP_12_18("Loss Payable Clause CP 12 18"),
		CP_Mortgagee("Mortgagee");
		
		
		String value;

		 AdditionalInterestTypeCPP(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
		
		private static final List<AdditionalInterestTypeCPP> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static AdditionalInterestTypeCPP random()  {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}

	}
	
}






















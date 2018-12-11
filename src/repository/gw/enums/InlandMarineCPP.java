package repository.gw.enums;

public class InlandMarineCPP {
	
	public enum InlandMarineCoveragePart {
		AccountsReceivable_CM_00_66("Accounts Receivable CM 00 66", "Accounts Receivable"),
		BaileesCustomers_IH_00_85("Bailees Customers IH 00 85", "Bailees Customers"),
		CameraAndMusicalInstrumentDealers_CM_00_21("Camera And Musical Instrument Dealers CM 00 21", "Camera And Musical Instrument Dealers"),
		CommercialArticles_CM_00_20("Commercial Articles CM 00 20", "Commercial Articles"),
		ComputerSystems_IH_00_75("Computer Systems IH 00 75", "Computer Systems"),
		ContractorsEquipment_IH_00_68("Contractors Equipment IH 00 68", "Contractors Equipment"),
		Exhibition_IH_00_92("Exhibition IH 00 92", ""),
		Installation_IDCM_31_4073("Installation IDCM 31 4073", ""),
		MiscellaneousArticles_IH_00_79("Miscellaneous Articles IH 00 79", ""),
		MotorTruckCargo("Motor Truck Cargo", "Motor Truck Cargo Coverage"),
		Signs_CM_00_28("Signs CM 00 28", ""),
		TripTransit_IH_00_78("Trip Transit IH 00 78", "Trip Transit"),
		ValuablePapers_CM_00_67("Valuable Papers CM 00 67", "Valuable Papers And Records");
		String value;
		String locationOfQuesiton;
			
		private InlandMarineCoveragePart(String type, String location){
			value = type;
			locationOfQuesiton = location;
		}
		
		public String getValue(){
			return value;
		}
		
		public String getLocationOfQuestion() {
			return locationOfQuesiton;
		}
		
	}
	
	
	public enum AccountsReceivableCoverageForm_CM_00_66_ClassificationOfRisk {
		NONE("<none>"),
		Manufacturer("Manufacturer"),
		Wholesaler("Wholesaler"),
		InsuranceAgent("Insurance Agent"),
		Other("Other");
		String value;
			
		private AccountsReceivableCoverageForm_CM_00_66_ClassificationOfRisk(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	
	public enum AccountsReceivableCoverageForm_CM_00_66_Coinsurance {
		EightlyPercent("80%"),
		NinetyPercent("90%"),
		OneHundredPercent("100%");
		String value;
			
		private AccountsReceivableCoverageForm_CM_00_66_Coinsurance(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	
	public enum RecepticalType {
		ClassA_4hours("Class A / 4 hours"),
		ClassB_2hours("Class B / 2 hours"),
		ClassC_1hours("Class C / 1 hours"),
		AtleastOne_halfHour("At least One-half Hour"),
		Unlabeledmetalsafeofatleasttwoinchwallthickness("Unlabeled metal safe of at least two inch wall thickness"),
		Vaultwithinnerandoutermetaldoorsseparatedbyatleast12inchesofairspace("Vault with inner and outer metal doors separated by at least 12 inches of air space"),
		Fullyenclosedcontainermadeofmetal("Fully enclosed container made of metal"),
		None("None");
		String value;
			
		private RecepticalType(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	
	public enum PercentDuplicated {
		None("None"),
		FiftyToOne("50% to 1%"),
		Atleast51Percent("At least 51%"),
		AtLeast90Percent("At least 90%");
		String value;
			
		private PercentDuplicated(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum Issuer {
		NONE("<none>"),
		UnderwritersLaboratoriesInc("Underwriter's Laboratories Inc"),
		SafeManufacturersNationalAssociation("Safe Manufacturers National Association");
		String value;
			
		private Issuer(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	
	public enum BaileesCustomersCoverageForm_IH_00_85_Deductible {
		None("<none>"),
		TwoHundredFifty("250"),
		FiveHundred("500"),
		OneThousand("1,000"),
		TwentyFiveHundred("2,500");
		String value;
			
		private BaileesCustomersCoverageForm_IH_00_85_Deductible(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum CommercialArticlesCoverageForm_CM_00_20_Deductible {
		None("<none>"),
		TwoHundredFifty("250"),
		FiveHundred("500"),
		OneThousand("1,000"),
		TwentyFiveHundred("2,500");
		String value;
			
		private CommercialArticlesCoverageForm_CM_00_20_Deductible(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum CamerasBlanket_MotionPicture {
		None("<none>"),
		ExcludingMotionPictureProducers("Excluding Motion Picture Producers"),
		MotionPictureProducers("Motion Picture Producers");
		String value;
		
		private CamerasBlanket_MotionPicture(String type) {
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum MusicalInstrumentsBlanket_MusicalInstruments {
		None("<none>"),
		IndividualsProfessional("Individuals - Professional"),
		OtherThanIndividuals("Other Than Individuals");
		String value;
		
		private MusicalInstrumentsBlanket_MusicalInstruments(String type) {
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum CommercialArticles_EquipmentType {
		None("<none>"),
		Camera("Camera"),
		MusicalInstruments("Musical Instruments");
		String value;
		
		private CommercialArticles_EquipmentType(String type) {
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum CommercialArticles_BandType {
		None("<none>"),
		DanceBandsOrchestras("Dance Bands, Orchestras"),
		AllOtherBands("All Other Bands");
		String value;
		
		private CommercialArticles_BandType(String type) {
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum ContractorsEquipmentCoverageForm_IH_00_68_Deductible {
		None("<none>"),
		TwoHundredFifty("250"),
		FiveHundred("500"),
		OneThousand("1,000"),
		TwentyFiveHundred("2,500"),
		FiveThousand("5,000");
		String value;
			
		private ContractorsEquipmentCoverageForm_IH_00_68_Deductible(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum ContractorsEquipmentCoverageForm_IH_00_68_ContractorType {
		None("<none>"),
		SmallContractor("Small Contractor"),
		BuildingContractor("Building Contractor"),
		HeavyConstruction("Heavy Construction"),
		RoadBuilding("Road / Building"),
		CustomFarming("Custom Farming");
		String value;
			
		private ContractorsEquipmentCoverageForm_IH_00_68_ContractorType(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum ContractorsEquipmentCoverageForm_IH_00_68_Coinsurance {
		None("<none>"),
		EightlyPercent("80%"),
		NinetyPercent("90%"),
		OneHundredPercent("100%");
		String value;
			
		private ContractorsEquipmentCoverageForm_IH_00_68_Coinsurance(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum MiscellaneousItemsBlanketCoverage_IH_68_02_Deductible {
		None("<none>"),
		OneHundred("100"),
		TwoHundredFifty("250"),
		FiveHundred("500"),
		OneThousand("1,000"),
		TwentyFiveHundred("2,500"),
		FiveThousand("5,000");
		String value;
			
		private MiscellaneousItemsBlanketCoverage_IH_68_02_Deductible(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum ToolsAndClothingBelongingToYourEmployees_IH_68_01_Deductible {
		None("<none>"),
		OneHundred("100"),
		TwoHundredFifty("250"),
		FiveHundred("500"),
		OneThousand("1,000"),
		TwentyFiveHundred("2,500"),
		FiveThousand("5,000");
		String value;
			
		private ToolsAndClothingBelongingToYourEmployees_IH_68_01_Deductible(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum ContractorsEquipmentCoverageForm_IH_00_68_EquipmentType {
		None("<none>"),
		ATV_UTV("ATV/UTV (4 - wheelers, snow mobiles)"),
		CranesAndBooms("Cranes and booms"),
		SelfPropelled("Self Propelled"),
		Tools("Tools");
		String value;
			
		private ContractorsEquipmentCoverageForm_IH_00_68_EquipmentType(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum ExhibitionCoverageForm_IH_00_92_Deductible {
		None("<none>"),
		FiveHundred("500"),
		OneThousand("1,000"),
		TwentyFiveHundred("2,500"),
		FiveThousand("5,000");
		String value;
			
		private ExhibitionCoverageForm_IH_00_92_Deductible(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum ExhibitionCoverageForm_IH_00_92_Coinsurance {
		None("<none>"),
		EightlyPercent("80%"),
		NinetyPercent("90%"),
		OneHundredPercent("100%");
		String value;
			
		private ExhibitionCoverageForm_IH_00_92_Coinsurance(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum ExhibitionCoverageForm_IH_00_92_HazardousCategory {
		None("<none>"),
		Low("Low"),
		Medium("Medium"),
		High("High");
		String value;
			
		private ExhibitionCoverageForm_IH_00_92_HazardousCategory(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum MotorTruckCargo_Deductible {
		None("<none>"),
		TwoHundredFifty("250"),
		FiveHundred("500"),
		OneThousand("1,000"),
		TwentyFiveHundred("2,500");
		String value;
			
		private MotorTruckCargo_Deductible(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum MotorTruckCargo_RadiusOfOperation {
		None("<none>"),
		Local("Local (0 - 100 Miles)"),
		Intermediate("Intermediate (101 - 300 Miles)"),
		LongDistance("Long Distance (Over 300 Miles)");
		String value;
			
		private MotorTruckCargo_RadiusOfOperation(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum InlandMarine_Cargo {
		AgriculturalMachineryImplementsParts("Agricultural machinery, implements, parts"),
		AlcoholicBeveragesNOC("Alcoholic Beverages, NOC"),
		AppliancesAllOther("Appliances � all other"),
		AppliancesMajor("Appliances � major"),
		AutomobilePartsAccessories("Automobile parts accessories"),
		Beer("Beer"),
		BeveragesNonAlcoholic("Beverages (non-alcoholic)"),
		Bicycles("Bicycles"),
		Boats("Boats"),
		BuildingOrPavingMaterials("Building or paving materials"),
		Butter("Butter"),
		CablesPipesAndWire("Cables, Pipes & Wire (other than copper)"),
		CamerasPhotographicMaterialsAccessoriesOpticalGoods("Cameras, photographic materials, accessories, optical goods"),
		CandyConfectionery("Candy, confectionery"),
		CannedGoods("Canned goods"),
		Carpeting("Carpeting"),
		CementSandOrGravel("Cement, Sand or Gravel"),
		CeramicGoods("Ceramic Goods"),
		Chemicals("Chemicals"),
		Cigarettes("Cigarettes"),
		CleaningCompoundsBleachesDetergentsSoaps("Cleaning compounds, bleaches, detergents, soaps"),
		Clothing("Clothing"),
		Collectibles("Collectibles"),
		Computers("Computers"),
		ContractorsHeavyEquipment("Contractors Heavy Equipment"),
		CosmeticsAndPerfume("Cosmetics and Perfume"),
		Cotton("Cotton"),
		DairyProducts("Dairy Products (except butter and milk)"),
		Dishes("Dishes"),
		DrugsMedicinesMedicalSupplies("Drugs, medicines, medical supplies"),
		DryGoods("Dry goods. (no clothing or furs)"),
		EDPEquipment("EDP Equipment"),
		Eggs("Eggs"),
		ElectricalOrElectronicSuppliesEquipmentFixturesInstrumentsAppliancesParts("Electrical or electronic supplies, equipment, fixtures, instruments, appliances, parts"),
		Explosives("Explosives"),
		FarmProducts("Farm Products"),
		FeedAndFertilizerGrainAndSeed("Feed and fertilizer, grain, and seed"),
		Fertilizer("Fertilizer (not ammonium nitrate or liquid)"),
		FineArts("Fine Arts"),
		FirearmsAndAmmunitions("Firearms and Ammunitions"),
		Fireworks("Fireworks"),
		Fish("Fish"),
		FloorCoveringRugsCarpetingTiles("Floor covering � rugs, carpeting, tiles"),
		FoodProducts("Food products (potatoes, onions, etc.)"),
		FoodProductsCannedDriedPreserved("Food products � canned, dried, preserved"),
		FoodProductsFrozenOrRefrigerated("Food products � frozen or refrigerated"),
		FrozenRefrigeratedProducts("Frozen & Refrigerated Products"),
		FuelOil("Fuel oil"),
		Furniture("Furniture"),
		Furs("Furs"),
		Gasoline("Gasoline"),
		GeneralMerchandise("General Merchandise"),
		GlassOrMirrors("Glass or mirrors"),
		GlasswareEarthenwareChinaCeramics("Glassware, earthenware, china, ceramics"),
		GrainAndFeed("Grain, and Feed (not hay or straw)"),
		HayAndStraw("Hay and straw"),
		HazardousSubstances("Hazardous Substances"),
		HouseholdGoodsMovers("Household goods movers"),
		Jewelry("Jewelry"),
		LeatherGoodsLuggagePurses("Leather goods, luggage, purses"),
		LiquidHaulers("Liquid Haulers"),
		Liquors("Liquors (not Beer or Wine)"),
		Livestock("Livestock"),
		Lumber("Lumber"),
		MachineryAndPowerTools("Machinery and power tools"),
		MeatPoultryAndSeafood("Meat, poultry, and seafood - fresh"),
		MetalNOCAndMetalProducts("Metal, NOC & Metal Products"),
		MetalProductsFinished("Metal products � finished"),
		MetalSheetCastingsForgingsStampingsFabrications("Metal sheet, castings, forgings, stampings, fabrications"),
		Milk("Milk"),
		MusicalProducts("Musical Products"),
		Narcotics("Narcotics"),
		OfficeEquipment("Office equipment"),
		OilGreasePetroleumProducts("Oil, grease, petroleum products"),
		OpticalGoods("Optical Goods"),
		OrientalRugsAndCarpets("Oriental Rugs & Carpets"),
		PackageDelivery("Package delivery"),
		PaintVarnishPigmentsDyesInk("Paint, varnish, pigments, dyes, ink"),
		PaperAndPaperProducts("Paper and paper products"),
		PetroleumProductsUnder140FFlashPoint("Petroleum Products under 140�F (60�C) flash point"),
		PetroleumProductsNOC("Petroleum Products, NOC"),
		Pharmaceuticals("Pharmaceuticals"),
		PipeMasonryAndPlastic("Pipe- masonry and plastic"),
		PipeMetal("Pipe � metal"),
		PlasticOrRubberProducts("Plastic or rubber products"),
		PlumbingFixturesAndSupplies("Plumbing fixtures and supplies"),
		PoultryForMarket("Poultry for market"),
		PowerTools("Power Tools"),
		PreciousAndSemipreciousMetals("Precious & Semiprecious Metals"),
		ProduceOnionsPotatoesEtc("Produce - Onions, Potatoes, etc."),
		RadiosTVPhonographsTelevisionsRecordersAmplifiersParts("Radios, TV, Phonographs, televisions, recorders, amplifiers, parts"),
		RefrigeratingMachineryAirConditionersEquipmentParts("Refrigerating machinery, air conditioners, equipment, parts"),
		RubberGoods("Rubber goods (Excluding tires and tubes)"),
		RugsAndCarpets("Rugs & Carpets"),
		Shoes("Shoes"),
		SoapAndSoapProducts("Soap and soap products"),
		SportingGoods("Sporting Goods"),
		StructuralSteel("Structural steel"),
		TextileProductsFinished("Textile products � finished (other than clothing)"),
		TextilesFabricsOrPieceGoods("Textiles � fabrics or piece goods"),
		TextileYarnsFiberEtc("Textile yarns, fiber, etc."),
		TiresTubes("Tires, tubes"),
		TobaccoAndTobaccoProducts("Tobacco and Tobacco Products"),
		ToolsAndHardware("Tools and hardware"),
		ToolsPower("Tools � power"),
		Toys("Toys"),
		TVRadiosAndStereos("TV, Radios & Stereos"),
		VideoEquipmentAndTapes("Video Equipment & Tapes"),
		Watches("Watches"),
		Wines("Wines"),
		WireCopper("Wire � copper"),
		WireOtherThanCopper("Wire � other than copper");

		String value;
			
		private InlandMarine_Cargo(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum MotorTruckCargo_Coverage {
		None("<none>"),
		MotorTruckCargoCarriers_IDCM_31_4072("Motor Truck Cargo Carriers IDCM 31 4072"),
		MotorTruckCargoOwners_IDCM_31_4076("Motor Truck Cargo Owners IDCM 31 4076"),
		Both_IDCM_31_4072_and_IDCM_31_4076("Both IDCM 31 4072 and IDCM 31 4076");
		String value;
			
		private MotorTruckCargo_Coverage(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum CameraAndMusicalInstrumentDealers_CM_00_21_Deductible {
		None("<none>"),
		TwoHundredFifty("250"),
		FiveHundred("500"),
		OneThousand("1,000"),
		TwentyFiveHundred("2,500");
		String value;
			
		private CameraAndMusicalInstrumentDealers_CM_00_21_Deductible(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum CameraAndMusicalInstrumentDealers_CM_00_21_Coinsurance {
		None("<none>"),
		EightlyPercent("80%"),
		NinetyPercent("90%"),
		OneHundredPercent("100%");
		String value;
			
		private CameraAndMusicalInstrumentDealers_CM_00_21_Coinsurance(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum ComputerSystemsCoverageForm_IH_00_75_Deductible {
		None("<none>"),
		TwoHundredFifty("250"),
		FiveHundred("500"),
		OneThousand("1,000"),
		TwentyFiveHundred("2,500");
		String value;
			
		private ComputerSystemsCoverageForm_IH_00_75_Deductible(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum ComputerSystemsCoverageForm_IH_00_75_Coinsurance {
		None("<none>"),
		EightlyPercent("80%"),
		NinetyPercent("90%"),
		OneHundredPercent("100%");
		String value;
			
		private ComputerSystemsCoverageForm_IH_00_75_Coinsurance(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum SignsCoverageForm_CM_00_28_Deductible {
		None("<none>"),
		NoDeductible("No Deductible"),
		FivePercent("5%");
		String value;
			
		private SignsCoverageForm_CM_00_28_Deductible(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum SignsCoverageForm_CM_00_28_Coinsurance {
		None("<none>"),
		EightlyPercent("80%"),
		NinetyPercent("90%"),
		OneHundredPercent("100%");
		String value;
			
		private SignsCoverageForm_CM_00_28_Coinsurance(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum SignsCoverageForm_CM_00_28_SignType {
		ElectronicLED("Electronic/LED"),
		Billboards("Billboards"),
		Fluorescent("Fluorescent"),
		Lamps("Lamps"),
		Mechanical("Mechanical"),
		Neon("Neon");
		String value;
			
		private SignsCoverageForm_CM_00_28_SignType(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum InstallationFloaterCoverage_IDCM_31_4073_Deductible {
		None("<none>"),
		FiveHundred("500"),
		OneThousand("1,000"),
		TwentyFiveHundred("2,500");
		String value;
			
		private InstallationFloaterCoverage_IDCM_31_4073_Deductible(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum ValuablePapersAndRecordsCoverageForm_CM_00_67_Deductible {
		None("<none>"),
		TwoHundredFifty("250"),
		FiveHundred("500"),
		OneThousand("1,000"),
		TwentyFiveHundred("2,500");
		String value;
			
		private ValuablePapersAndRecordsCoverageForm_CM_00_67_Deductible(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum MiscellaneousArticlesCoverageForm_IH_00_79_Deductible {
		None("<none>"),
		OneHundred("100"),
		TwoHundredFifty("250"),
		FiveHundred("500"),
		OneThousand("1,000"),
		TwentyFiveHundred("2,500");
		String value;
			
		private MiscellaneousArticlesCoverageForm_IH_00_79_Deductible(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum MiscellaneousArticlesCoverageForm_IH_00_79_Coinsurance {
		None("<none>"),
		EightlyPercent("80%"),
		NinetyPercent("90%"),
		OneHundredPercent("100%");
		String value;
			
		private MiscellaneousArticlesCoverageForm_IH_00_79_Coinsurance(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum TripTransitCoverageForm_IH_00_78_Deductible {
		None("<none>"),
		FiveHundred("500"),
		OneThousand("1,000"),
		TwentyFiveHundred("2,500");
		String value;
			
		private TripTransitCoverageForm_IH_00_78_Deductible(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum TripTransitCoverageForm_IH_00_78_DistanceInMiles {
		None("<none>"),
		From_0_To_250("0 - 250"),
		From_251_To_500("251 - 500"),
		From_501_To_1000("501 - 1,000"),
		From_1001_To_2000("1,001 - 2,000"),
		Over2000("Over 2,000");
		String value;
			
		private TripTransitCoverageForm_IH_00_78_DistanceInMiles(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
}

















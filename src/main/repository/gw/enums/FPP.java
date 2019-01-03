package repository.gw.enums;

import java.util.ArrayList;

public class FPP{
	
	public enum FarmPersonalPropertyTypes {
		Machinery("Machinery"),
		IrrigationEquipment("Irrigation Equipment"),
		Livestock("Livestock"),
		Tools("Tools"),
		Commodities("Commodities"),
		Miscellaneous("Miscellaneous"),	
		SpareTruckParts("Spare Truck Parts");
		
		String value;
		
		private FarmPersonalPropertyTypes(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum FPPOptionalCoverages {
		FreezingLivestock("Freezing of Livestock"),
		NonOwnerEquipment("Non-Owned Equipment");
		
		String value;
		
		private FPPOptionalCoverages(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	public enum FPPCoverageTypes {
		
		BlanketExclude("Blanket Exclude"),
		BlanketInclude("Blanket Include"),
		Schedule("Schedule");
		String value;
		
		private FPPCoverageTypes(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum FPPDeductible {
		
		Ded_100("100"),
		Ded_250("250"),
		Ded_500("500"),
		Ded_1000("1,000"),
		Ded_2500("2,500"),
		Ded_5000("5,000"),
		Ded_10000("10,000"),
		Ded_25000("25,000");
		
		String value;
		
		private FPPDeductible(String value){
			this.value = value;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum FPPFarmPersonalPropertySubTypes{

		Tractors("Tractors", FarmPersonalPropertyTypes.Machinery),
		HarvestersHeaders("Harvesters & Headers", FarmPersonalPropertyTypes.Machinery),
		LandPlanesScrapers("Land Planes & Scrapers", FarmPersonalPropertyTypes.Machinery),
		PlantersDrills("Planters & Drills", FarmPersonalPropertyTypes.Machinery),
		BalersRakesStackers("Balers, Rakes & Stackers", FarmPersonalPropertyTypes.Machinery),
		MowersSwathers("Mowers & Swathers", FarmPersonalPropertyTypes.Machinery),
		GrindersChoppers("Grinders & Choppers", FarmPersonalPropertyTypes.Machinery),
		PlowsDiscsHarrowsSpreadersCultivatorsSubsoilers("Plows, Discs, Harrows, Spreaders, Cultivators & Subsoilers", FarmPersonalPropertyTypes.Machinery),
		ConveyorsSorters("Conveyors & Sorters", FarmPersonalPropertyTypes.Machinery),
		FrontLoadersSkidSteersBackhoes("Front Loaders, Skid-steers & Backhoes", FarmPersonalPropertyTypes.Machinery),
		Sprayers("Sprayers", FarmPersonalPropertyTypes.Machinery),
		MintTubsGrainCartsOnionBins("Mint Tubs - Grain Carts - Onion Bins", FarmPersonalPropertyTypes.Machinery),
		Ditchers("Ditchers", FarmPersonalPropertyTypes.Machinery),
//		@editor ecoleman 5/17/18 : (NOTE) GPS is being moved to TOOLS with US15159
		GPS("GPS", FarmPersonalPropertyTypes.Tools),
		OtherMachinery("Other", FarmPersonalPropertyTypes.Machinery),
		CirclePivots("Circle Pivots", FarmPersonalPropertyTypes.IrrigationEquipment),
		WheelLines("Wheel Lines", FarmPersonalPropertyTypes.IrrigationEquipment),
		HandLines("Hand Lines", FarmPersonalPropertyTypes.IrrigationEquipment),
		Mainlines("Mainlines", FarmPersonalPropertyTypes.IrrigationEquipment),
		Pumps("Pumps", FarmPersonalPropertyTypes.IrrigationEquipment),
		Panels("Panels", FarmPersonalPropertyTypes.IrrigationEquipment),
		Motors("Motors", FarmPersonalPropertyTypes.IrrigationEquipment),
		SyphonsTupesGatedPipe("Syphon Types & Gated Pipe", FarmPersonalPropertyTypes.IrrigationEquipment),
		DriplineEquipment("Dripline Equipment", FarmPersonalPropertyTypes.IrrigationEquipment),
		OtherIrrigationEquipment("Other", FarmPersonalPropertyTypes.IrrigationEquipment),
		Bulls("Bulls", FarmPersonalPropertyTypes.Livestock),
		Cows("Cows", FarmPersonalPropertyTypes.Livestock),
		Steers("Steers", FarmPersonalPropertyTypes.Livestock),
		Heifers("Heifers", FarmPersonalPropertyTypes.Livestock),
		Calves("Calves", FarmPersonalPropertyTypes.Livestock),
		Sheep("Sheep", FarmPersonalPropertyTypes.Livestock),
		Goats("Goats", FarmPersonalPropertyTypes.Livestock),
		Horses("Horses", FarmPersonalPropertyTypes.Livestock),
		Mules("Mules", FarmPersonalPropertyTypes.Livestock),
		Donkeys("Donkeys", FarmPersonalPropertyTypes.Livestock),
		Hogs("Hogs", FarmPersonalPropertyTypes.Livestock),
		Poultry("Poultry", FarmPersonalPropertyTypes.Livestock),
		Llamas("Llamas", FarmPersonalPropertyTypes.Livestock),
		Alpacas("Alpacas", FarmPersonalPropertyTypes.Livestock),
		OtherLivestock("Other", FarmPersonalPropertyTypes.Livestock),
		PowerTools("Power Tools", FarmPersonalPropertyTypes.Tools),
		HandTools("Hand Tools", FarmPersonalPropertyTypes.Tools),
		Welders("Welders", FarmPersonalPropertyTypes.Tools),
		OtherTools("Other", FarmPersonalPropertyTypes.Tools),
		Grain("Grain", FarmPersonalPropertyTypes.Commodities),
		Seed("Seed", FarmPersonalPropertyTypes.Commodities),
		Peas("Peas", FarmPersonalPropertyTypes.Commodities),
		Beans("Beans", FarmPersonalPropertyTypes.Commodities),
		Hay("Hay", FarmPersonalPropertyTypes.Commodities),
		Straw("Straw", FarmPersonalPropertyTypes.Commodities),
		Fodder("Fodder", FarmPersonalPropertyTypes.Commodities),
		OtherCommodities("Other", FarmPersonalPropertyTypes.Commodities),
		VaccinesMedicines("Vaccines & Medicines", FarmPersonalPropertyTypes.Miscellaneous),
		AIEquipmentSupplies("A.I. Equipment & Supplies", FarmPersonalPropertyTypes.Miscellaneous),
		BrandingSuppliesTags("Branding Supplies & Tags", FarmPersonalPropertyTypes.Miscellaneous),
		LiquidFertilizers("Liquid Fertilizers", FarmPersonalPropertyTypes.Miscellaneous),
		DryFertilizers("Dry Fertilizers", FarmPersonalPropertyTypes.Miscellaneous),
		Herbicides("Herbicides", FarmPersonalPropertyTypes.Miscellaneous),
		Perticides("Pesticides", FarmPersonalPropertyTypes.Miscellaneous),
		OtherChemicals("Other Chemicals", FarmPersonalPropertyTypes.Miscellaneous),
		HoneyMintOil("Honey/Mint Oil", FarmPersonalPropertyTypes.Miscellaneous),
		FuelOilGrease("Fuel, Oil, & Grease", FarmPersonalPropertyTypes.Miscellaneous),
		FencingMaterials("Fencing Materials", FarmPersonalPropertyTypes.Miscellaneous),
		BuildingSupplies("Building Supplies", FarmPersonalPropertyTypes.Miscellaneous),
		Paints("Paints", FarmPersonalPropertyTypes.Miscellaneous),
		SaddlesTack("Saddles & Tack", FarmPersonalPropertyTypes.Miscellaneous),
		PortableBuildingsBeeHouses("Portable Buildings & Bee Houses", FarmPersonalPropertyTypes.Miscellaneous),
		OtherMiscellaneous("Other", FarmPersonalPropertyTypes.Miscellaneous),
		SpareTruckParts("Spare Truck Parts", FarmPersonalPropertyTypes.SpareTruckParts);
		
		String value;
		FarmPersonalPropertyTypes parentType;
		
		private FPPFarmPersonalPropertySubTypes(String value, FarmPersonalPropertyTypes parentType){
			this.value = value;
			this.parentType = parentType;
		}
		
		public String getValue(){
			return this.value;
		}
		
		public FarmPersonalPropertyTypes getParentType() {
			return this.parentType;
		}
		
		public static ArrayList<FPPFarmPersonalPropertySubTypes> getSubTypesByParent(FarmPersonalPropertyTypes parentType) {
		
			ArrayList<FPPFarmPersonalPropertySubTypes> toReturn = new ArrayList<FPPFarmPersonalPropertySubTypes>();
			for(FPPFarmPersonalPropertySubTypes type : FPPFarmPersonalPropertySubTypes.values()) {
				if(type.getParentType() == parentType) {
					toReturn.add(type);
				}
			}
			
			return toReturn;
		}
	}
	
}
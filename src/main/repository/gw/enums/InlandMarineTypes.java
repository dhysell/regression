package repository.gw.enums;

public class InlandMarineTypes {
	
	public enum InlandMarine{
		RecreationalEquipment("Recreational Equipment"),
		Watercraft("Watercraft"),
		FarmEquipment("Farm Equipment"),
		PersonalProperty("Personal Property"),
		Cargo("Cargo"),
		Livestock("Livestock");
		
		String value;
		
		private InlandMarine(String _value){
			this.value = _value;
		}
		
		public String getValue(){
			return this.value;
		}
	}
	
	public enum RecreationalEquipmentType{
		none("<none>"),
		AllTerrainVehicle("All Terrain Vehicle"),
		Bicycle("Bicycle"),
		GolfCart("Golf Cart"),
		OffRoadMotorcycle("Off-road Motorcycle"),
		Snowmobile("Snowmobile"),
		UtilityTrailer("Utility Trailer"),
		WagonsCarriages("Wagons/Carriages");
		
		String value;
		
		private RecreationalEquipmentType(String _value){
			this.value = _value;
		}
		
		public String getValue(){
			return this.value;
		}		
	}
	
	public enum WatercraftTypes{
		None("<none>"),
		BoatAndMotor("Boat and Motor"),
		PersonalWatercraft("Personal Watercraft"),
		Sailboat("Sailboat"),
		Yacht("Yacht");
		
		String value;
		
		private WatercraftTypes(String _value){
			this.value = _value;
		}
		
		public String getValue(){
			return this.value;
		}
	}
	
	public enum WatercratItems{
		None("<none>"),
		Boat("Boat"),
		Trailer("Trailer"),
		Motor("Motor"),
		Other("Other"),
		PersonalWatercraft("Personal Watercraft");
		
		String value;
		
		private WatercratItems(String _value){
			this.value = _value;
		}
		
		public String getValue(){
			return this.value;
		}
	}
	
	public enum ClassIIICargoTypes{
		None("<none>"),
		FarmEquip("Farm Equip"),
		Feed("Feed"),
		Straw("Straw"),
		Livestock("Livestock"),
		Fertilizer("Fertilizer"),
		Grain("Grain"),
		Potatoes("Potatoes"),
		Beets("Beets"),
		Onions("Onions"),
		Cream("Cream"),
		Milk("Milk"),
		Poultry("Poultry");
		
		String value;
		
		private ClassIIICargoTypes(String _value){
			this.value = _value;
		}
		
		public String getValue(){
			return this.value;
		}
	}
	
	public enum CargoClass{
		ClassI("Class I Owned Cargo Only"),
		ClassII("Class II Exchange Hauling, not for hire"),
		ClassIII("Class III Owner's and Contract");
		
		String value;
		
		private CargoClass(String _value){
			this.value = _value;
		}
		
		public String getValue(){
			return this.value;
		}
	}
	
	public enum BoatType{
		Inboard("Inboard"),
		Outboard("Outboard"),
		InboardOutboard("Inboard / Outboard");
		
		String value;
		
		private BoatType(String _value){
			this.value = _value;
		}
		
		public String getValue(){
			return this.value;
		}
	}
}

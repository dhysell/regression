package repository.gw.enums;

public class IMFarmEquipment {
	
	
	public enum IMFarmEquipmentType {
		
		None("<none>"),
		FarmEquipment("Farm Equipment"),
		MovableSetSprinkler("Movable Set Sprinkler"),
		CircleSprinkler("Circle Sprinkler"),
		WheelSprinkler("Wheel Sprinkler"),
		PumpsPanelsMotors("Pumps, Panels and Motors");
		
		String value;
		
		private IMFarmEquipmentType(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum IMFarmEquipmentDeductible{
		
		None("<none>"),
		TwentyFive("25"),
		Fifty("50"),
		Hundred("100"),
		TwoHundredFifty("250"),
		FiveHundred("500"),
		OneThousand("1000");
		
		String value;
		
		private IMFarmEquipmentDeductible(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
		
	}
}

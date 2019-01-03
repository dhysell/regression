package repository.gw.enums;

public enum ProductCPP {
	BusinessAuto("Business Auto"), 
	Garagekeepers("Garagekeepers"), 
	MotorCarrierandTruckers("Motor Carrier and Truckers"), 
	BusinessAutoPhysicalDamage("Business Auto Physical Damage");
	String value;
		
	private ProductCPP(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}

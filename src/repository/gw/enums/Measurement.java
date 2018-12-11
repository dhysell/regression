package repository.gw.enums;

public enum Measurement {
	SQFT("Sq. Ft."),
	Feet("Feet"),
	Diameter("Diameter"),
	Bushels("Bushels");
	String value;
	
	Measurement(String status) {
		value = status;
	}
	
	public String getValue(){
		return value;
	}
}

package repository.gw.enums;

public enum FleetCPP { 
	GreaterThan5Units("5 or more self-propelled vehicles"), 
	LessThan5Units("Fewer than 5 self-propelled vehicles");
	String value;
		
	private FleetCPP(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}

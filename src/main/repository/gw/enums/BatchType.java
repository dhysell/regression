package repository.gw.enums;

public enum BatchType {
	ChangeAtRenewal("Change At Renewal"), 
	SpecificDateChange("Specific Date Change");
	String value;
	
	private BatchType(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}

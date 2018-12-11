package repository.gw.enums;

public enum ContactSubType {
	Company("Company"), 
	Contact("Contact"), 
	Person("Person");
	String value;
	
	private ContactSubType(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
	
}

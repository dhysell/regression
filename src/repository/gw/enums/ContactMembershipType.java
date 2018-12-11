package repository.gw.enums;

public enum ContactMembershipType {
	Associate("Associate"), 
	Regular("Regular"), 
	Other("Other");
	String value;
	
	private ContactMembershipType(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}

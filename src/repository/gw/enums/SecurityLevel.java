package repository.gw.enums;

public enum SecurityLevel {

	Unrestricted("Unrestricted"),
	Internal_Only("Internal Only"),
	Confidential("Confidential");	
	
	String value;
		
	private SecurityLevel(String securityLevel){
		value = securityLevel;
	}
	
	public String getValue(){
		return value;
	}
}

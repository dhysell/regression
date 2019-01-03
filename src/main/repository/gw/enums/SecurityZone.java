package repository.gw.enums;

public enum SecurityZone {
	
	Default("Default Security Zone");
	String value;
		
	private SecurityZone(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}

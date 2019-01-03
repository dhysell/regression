package repository.gw.enums;

public enum DriverType {
	Primary("Primary"),
	Occasional("Occasional");
	String value;
		
	private DriverType(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}

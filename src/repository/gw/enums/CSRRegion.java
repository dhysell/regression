package repository.gw.enums;

public enum CSRRegion {
	Region1("Region 1"),
	Region2("Region 2"),
	Region3("Region 3"),
	Region4("Region 4");
	
	String value;
	
	private CSRRegion(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}

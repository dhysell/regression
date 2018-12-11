package repository.gw.enums;

public enum DateDifferenceOptions {
	Hour("hh"), 
	Minute("mi"), 
	Second("ss"), 
	Day("dd"), 
	Week("ww"), 
	Month("mm"), 
	Year("yyyy");
	String value;
		
	private DateDifferenceOptions(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}

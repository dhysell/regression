package repository.gw.enums;

public enum DateAddSubtractOptions {
	Hour("hh"), 
	Minute("mi"), 
	Second("ss"), 
	Millis("ms"), 
	Day("dd"), 
	Week("ww"), 
	Month("mm"), 
	Quarter("qq"), 
	Year("yyyy"), 
	BusinessDay("bd");
	String value;
		
	private DateAddSubtractOptions(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}

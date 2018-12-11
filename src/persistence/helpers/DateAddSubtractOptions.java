package persistence.helpers;

public enum DateAddSubtractOptions {
	Hour("hh"), 
	Minute("mi"), 
	Second("ss"), 
	Millis("ms"), 
	Day("dd"), 
	Week("ww"), 
	Month("mm"), 
	Quarter("qq"), 
	Year("yyyy");
	String value;
		
	private DateAddSubtractOptions(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}

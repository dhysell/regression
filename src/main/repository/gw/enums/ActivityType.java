package repository.gw.enums;

public enum ActivityType {
	General("General"),
	Interview("Interview"),
	NewMail("New mail"),
	Reminder("Reminder"),
	Request("Request"),
	UnderwriterReview("Underwriter Review");
	
	String value;
	
	private ActivityType(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
	

}

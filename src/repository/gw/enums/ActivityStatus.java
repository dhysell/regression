package repository.gw.enums;

public enum ActivityStatus {	
	Open("Open"),
	Skipped("Skipped"),
	Complete("Complete"),	
	Canceled("Canceled"),
	Obsolete("Obsolete");
	
	String value;
	
	private ActivityStatus(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}

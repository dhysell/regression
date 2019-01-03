package repository.gw.enums;

public enum ActivityFilter {
	
	AllOpen("All open"), 
	NewlyOpened("Newly opened (this week)"), 
	ClosedLast30Days("Closed in last 30 days"), 
	OpenUrgent("Open urgent"), 
	Escalated("Escalated"),
	All("All");
	String value;
	
	private ActivityFilter(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}

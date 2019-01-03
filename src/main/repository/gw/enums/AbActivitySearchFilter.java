package repository.gw.enums;

public enum AbActivitySearchFilter {
	OverdueOnly("Overdue only"),
	MyActivitiesToday("My activities today"),
	DueWithinSevenDays("Due Within 7 Days"),
	AllOpen("All Open"),
	ClosedToday("Closed today"),
	ClosedYesterday("Closed yesterday"),
	ClosedInLastSevenDays("Closed in last 7 days"),
	ClosedInLastThirtyDays("Closed in last 30 days"),
	ClosedInLastNinetyDays("Closed in last 90 days"),
	ClosedInLastYear("Closed in last Year"),
	AllClosed("All Closed"),
	AllActivities("All Activities");
	
	String value;
	
	private AbActivitySearchFilter(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}

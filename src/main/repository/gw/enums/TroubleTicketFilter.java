package repository.gw.enums;

public enum TroubleTicketFilter {
	All("All"),
	AllOpenOwned("All open owned"),
	NewOpenedThisWeek("New opened (this week)s"),
	OpenUrgent("Open urgent"),	
	OpenLast365Days("Open last 365 days"),
	ClosedInLast30Days("Closed in last 30 days"),	
	ClosedLast365Days("Closed last 365 days");
	String value;
		
	private TroubleTicketFilter(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}

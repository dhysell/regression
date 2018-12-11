package repository.gw.enums;

public enum DisbursementClosedStatusFilter {
	All("All"),
	Closed_Last_Thirty_Days("Closed in last 30 days"),
	Closed_Last_Sixty_Days("Closed in last 60 days"),
	Closed_Last_Ninety_Days("Closed in last 90 days"),
	Closed_In_Previous_Twelve_Months("Closed in previous 12 months");
	String value;
	
	private DisbursementClosedStatusFilter(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}

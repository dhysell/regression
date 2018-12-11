package repository.gw.enums;

public enum TransactionFilter {

	Last30Days("Last 30 days"),
	Last60Days("Last 60 days"),
	Last90Days("Last 90 days"),
	Prev12Months("Previous 12 months"),
	PreviousMonth("Previous month"),
	ThisMonth("This month"),
	YearToDate("Year to date"),
	All("All");
	String value;
		
	private TransactionFilter(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}

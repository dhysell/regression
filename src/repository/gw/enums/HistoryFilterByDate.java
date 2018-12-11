package repository.gw.enums;

public enum HistoryFilterByDate {
	Last_30_Days("Last 30 days"),
	Last_60_Days("Last 60 days"),
	Last_90_Days("Last 90 days"),
	Last_120_Days("Last 120 days"),
	Last_180_Days("Last 180 days"),
	Last_Year("Last year"),
	Last_3_Years("Last 3 years"),
	All("All");
	String value;
	
	private HistoryFilterByDate(String value){
		this.value = value;
	}
	
	public String getValue(){
		return this.value;
	}
}

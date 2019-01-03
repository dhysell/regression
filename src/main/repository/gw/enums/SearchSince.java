package repository.gw.enums;

public enum SearchSince {
	Any("Any"),
	Today("Today"),
	Seven7DaysAgo("7 days ago"),
	Thirty30DaysAgo("30 days ago"),
	Ninty90DaysAgo("90 days ago"),
	OnehundredEighty180DaysAgo("180 days ago"),
	OneYearAgo("365 days ago");
	String value;
	
	private SearchSince(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}

package repository.gw.enums;

public enum SearchBy {
	DateCreated("Date Created"),
	DateModified("Date Modified");
	String value;
	
	private SearchBy(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}

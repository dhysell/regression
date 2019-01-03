package persistence.enums;

public enum SqlSigns {

	GreaterThan(">"),
	LessThan("<"), 
	EqualTo("="), 
	GreaterThanOrEqualTo(">="),
	LessThanOrEqualTo("<=");
	
	
	String dbValue;
	
	private SqlSigns(String dbValue){
		this.dbValue = dbValue;
	}
	
	public String getDBValue(){
		return dbValue;
	}
}

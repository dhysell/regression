package persistence.enums;

public enum TransactionType {

	Issuance("Issuance"),
	Submission("Submission"), 
	Renewal("Renewal"), 
	PolicyChange("Policy Change"),
	Cancellation("Cancellation"),
	Expired("Expired"),
	Rewrite("Rewrite");
	
	String dbValue;
	
	private TransactionType(String dbValue){
		this.dbValue = dbValue;
	}
	
	public String getDBValue(){
		return dbValue;
	}
	
	public static TransactionType getTypeFromDBValue(String dbValue) {
		TransactionType toReturn = null;
		for(TransactionType ttype : values()) {
			if(ttype.getDBValue().equals(dbValue)) {
				toReturn = ttype;
				break;
			}
		}
		return toReturn;
	}
}